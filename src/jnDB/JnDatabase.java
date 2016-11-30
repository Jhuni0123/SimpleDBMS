package jnDB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Cursor;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.rep.stream.Protocol.StartStream;
import com.sun.org.apache.regexp.internal.recompile;

import jnDB.TableSchema.ReferentialConstraint;
import jnDB.exception.*;
import jnDB.expression.BooleanExpression;
import jnDB.type.*;

public class JnDatabase {
	public static final String CreateTableSuccess(String tableName) { return "'" + tableName + "' table is created"; }
	public static final String DropSuccess(String tableName) { return "'" + tableName + "' table is droped"; }
	public static final String NoSuchTable = "No such table";
	public static final String ShowTablesNoTable = "There is no table";
	public static final String DeleteResult(int count){ return count + " row(s) are deleted"; }
	public static final String DeleteReferentialIntegrityPassed(int count){ return count + " row(s) are not deleted due to referential integrity"; }
	public static final String InsertResult = "The row is inserted";
	public static final String DB_KEY = "DB-Key";
	
	Environment myDbEnvironment;
    Database myDatabase;
    HashMap<String, Table> tables;
    
    public JnDatabase(){
    	myDbEnvironment = null;
    	myDatabase = null;
    }
    
    public void open(String locate, String name){
    	// Open or Create Database Environment
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        myDbEnvironment = new Environment(new File(locate), envConfig);
        
        // Open or Create Database
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        //dbConfig.setSortedDuplicates(true);
        myDatabase = myDbEnvironment.openDatabase(null, name, dbConfig);
        
        Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	// try get exists tables
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(DB_KEY)){
        			cursor.close();
        			tables = deserializeTables(foundData.getData());
        			return;
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	
    	tables = new HashMap<String,Table>();
    }
    
    public void close(){
    	Cursor cursor = null;
    	DatabaseEntry key,data;
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		key = new DatabaseEntry(DB_KEY.getBytes("UTF-8"));
    		data = new DatabaseEntry(serializeTables(tables));
    		cursor.put(key, data);
    		cursor.close();
    	}
    	catch (Exception e){
    		if(cursor != null) cursor.close();
    	}
    	
    	if(myDatabase != null) myDatabase.close();
        if(myDbEnvironment != null) myDbEnvironment.close();
    }
    
    private static byte[] serializeTables(HashMap<String,Table> tables) throws IOException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(tables);
        return out.toByteArray();
    }
    
    public static HashMap<String,Table> deserializeTables(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (HashMap<String,Table>) is.readObject();
    }
    
    public void putTable(Table table){
    	tables.put(table.getName(), table);
    }
    
    public Table getTable(String tableName){
    	return tables.get(tableName);
    }
    
    public boolean existsTable(String tableName){
    	return tables.containsKey(tableName);
    }
    
    public void createTable(TableSchema schema){
    	if(existsTable(schema.getName())) throw new TableExistenceError();
    	schema.checkValidity();
    	Table table = new Table(schema);
    	for(ReferentialConstraint rc : schema.rcList){
    		table.fkConstraints.add(new FKConstraint(rc.table.getName(), rc.pKeys, rc.fKeys));
    		Table target = getTable(rc.table.getName());
    		target.referencedByTable.add(table.getName());
    		putTable(target);
    	}
    	putTable(table);
    	printMessage(CreateTableSuccess(schema.getName()));
    }
    
    public void dropTable(String tableName){
    	if(!existsTable(tableName)){
    		printMessage(NoSuchTable);
    		return;
    	}
    	
    	Table table = getTable(tableName);
    	if(!table.isRemovable()){ throw new DropReferencedTableError(table.getName()); }
    	for(FKConstraint fkcons : table.fkConstraints){
			Table t = getTable(fkcons.getRefTableName());
			t.referencedByTable.remove(table.getName());
			putTable(t);
		}
    	
    	tables.remove(tableName);
    	printMessage(DropSuccess(tableName));
	}
    
    public void desc(String tName){
    	Table table = getTable(tName);
    	if(table == null){
    		printMessage(NoSuchTable);
    		return;
    	}
    	System.out.println("-------------------------------------------------");
    	table.descript();
    	System.out.println("-------------------------------------------------");
    }
    
    public void showTables(){
    	if(tables.isEmpty()){
    		printMessage(ShowTablesNoTable);
    		return;
    	}
    	System.out.println("----------------");
    	for(String tableName : tables.keySet()){
    		System.out.println(tableName);
    	}
    	System.out.println("----------------");
    	return;
    }
    
    public Table checkPrimaryKey(String tableName, ArrayList<String> colNameList){
    	Table table = getTable(tableName);
    	if(table == null) throw new ReferenceTableExistenceError();
    	HashSet<String> pri = new HashSet<String>();
    	for(String cName : colNameList){
    		if(pri.contains(cName))throw new DuplicateColumnAppearError();
    		if(!table.colNum.containsKey(cName)) throw new ReferenceColumnExistenceError();
    		pri.add(cName);
    	}
    	ArrayList<String> s = table.getPKSet();
    	for(String str : pri){
    		if(!s.contains(str))throw new ReferenceNonPrimaryKeyError();
    		s.remove(str);
    	}
    	if(!s.isEmpty())throw new ReferenceNonPrimaryKeyError();
    	return table;
    }
    
    public void insert(String tableName, ArrayList<String> cnList, ArrayList<Value> vList){
    	// cnList can be null
    	if(!existsTable(tableName)){
    		printMessage(NoSuchTable);
    		return;
    	}
    	Table table = getTable(tableName);
    	Row newRow = new Row(new ArrayList<Value>());
    	final ArrayList<Column> cols = table.getColumns();
    	
    	if(cnList == null){
    		cnList = new ArrayList<String>();
    		for(Column col : cols){
    			cnList.add(col.getName());
    		}
    	}
    	if(cnList.size() != vList.size()){ throw new InsertTypeMismatchError(); }
    	int num = cnList.size();
    	
    	
    	for(int i=0;i<cols.size();i++){
    		newRow.append(null);
    	}
    	
    	for(int i=0;i<num;i++){
    		String cName = cnList.get(i);
    		int index = table.getColIndex(cName);
    		if(index == -1){ throw new InsertColumnExistenceError(cName); }
    		Value v = vList.get(index);
    		if(cols.get(index).isNotNull() && v instanceof NullValue){ throw new InsertColumnNonNullableError(cName); }
    		boolean success = v.castTo(cols.get(index).getType());
    		if(!success){ throw new InsertTypeMismatchError(); }
    		newRow.setValue(index, v);
    	}
    	for(int i=0;i<cols.size();i++){
    		if(newRow.getValue(i) == null){ newRow.setValue(i, new NullValue()); }
    	}
    	
    	// primary key check
    	ArrayList<String> pks = table.getPKSet();
    	if(!pks.isEmpty()){
    		for(Row row : table.getRows()){
    			boolean dup = true;
    			for(String pk : pks){
    				int index = table.getColIndex(pk);
    				if(row.getValue(index).compareTo(newRow.getValue(index)) != 0){ dup = false; break; }
    			}
    			if(dup){ throw new InsertDuplicatePrimaryKeyError(); }
    		}
    	}
    	
    	// foreign key check
    	for(FKConstraint fkCons : table.fkConstraints){
    		boolean hasNull = false;
    		for(String cName : fkCons.fromColumns){
    			if(newRow.getValue(table.getColIndex(cName)) instanceof NullValue){ hasNull = true; break;}
    		}
    		if(hasNull){ continue; }
    		
    		boolean success = false;
    		Table toTable = getTable(fkCons.toTable);
    		for(Row toRow : toTable.getRows()){
    			boolean diff = false;
    			int fkSize = fkCons.fromColumns.size();
        		for(int i=0;i<fkSize;i++){
        			int toIndex = toTable.getColIndex(fkCons.toColumns.get(i));
        			int fromIndex = table.getColIndex(fkCons.fromColumns.get(i));
        			Value toV = toRow.getValue(toIndex);
        			Value fromV = newRow.getValue(fromIndex);
        			if(!toV.equals(fromV)){ diff = true; break; }
        		}	
        		if(!diff){ success = true; break; }
    		}
    		if(!success){ throw new InsertReferentialIntegrityError(); }
    	}
    	table.addRow(newRow);
    	printMessage(InsertResult);
    }
    
    public void delete(String tableName, BooleanExpression bexp){
    	if(!existsTable(tableName)){
    		printMessage(NoSuchTable);
    		return;
    	}
    	
    	Table table = getTable(tableName);
    	
    	// column test
    	bexp.evaluate(table.getColumns(), null);
    	int suc = 0, fail = 0;
    	for(int i=0;i<table.getRows().size();i++){
    		Row row = table.getRows().get(i);
    		if(bexp.evaluate(table.getColumns(), row) instanceof True){
    			boolean removable = true;
    			// check removable
    			for(String other : table.referencedByTable){
    				Table refer = getTable(other);
    				for(FKConstraint fkCons : refer.fkConstraints){
    					if(fkCons.getRefTableName().equals(tableName)){
    						boolean exists = false;
    						ArrayList<Value> pk = new ArrayList<Value>();
    						for(String k : fkCons.toColumns){
    							pk.add(row.getValue(table.getColIndex(k)));
    						}
    						for(Row refRow : refer.getRows()){
    							ArrayList<Value> fk = new ArrayList<Value>();
    							for(String k : fkCons.fromColumns){
    								fk.add(refRow.getValue(refer.getColIndex(k)));
    							}
    							if(pk.equals(fk)){
    								exists = true;
    							}
    						}
    						if(exists){
    							for(String k : fkCons.fromColumns){
    								if(refer.getColumns().get(refer.getColIndex(k)).isNotNull()){
    									removable = false;
    									break;
    								}
    							}
    						}
    						if(!removable)break;
    					}
    				}
    				if(!removable)break;
    			}
    			
    			if(removable){
        			// remove
    				table.removeRow(i);
        			i--;
        			suc++;
    			}
    			else { fail++; }
    		}
    	}
    	
    	System.out.println(DeleteResult(suc));
    	if(fail > 0){ System.out.println(DeleteReferentialIntegrityPassed(fail)); }
    }
    
    public void select(ArrayList<Pair<Pair<String,String>,String>> selectList, ArrayList<Pair<String,String>> fromList, BooleanExpression bexp){
    	// selectList can be empty
    	// selectList .first.first & .second can be null
    	// fromList pss.second can be null
    	
    	// FROM
    	HashSet<String> tableNameSet = new HashSet<String>();
    	
    	for(Pair<String,String> pss : fromList){
    		String newName;
    		if(!existsTable(pss.first)){ throw new SelectTableExistenceError(pss.first); }
    		if(pss.second == null){
    			newName = pss.first;
    		}
    		else{
    			newName = pss.second;
    		}
    		if(tableNameSet.contains(newName)){ throw new FromDuplicateTableNameError(newName); }
    	}
    	
    	Table res = new Table();
		res.addRow(new Row(new ArrayList<Value>()));

    	for(Pair<String,String> pss : fromList){
    		Table other = getTable(pss.first);
    		String newName = pss.first;
    		if(pss.second != null){ newName = pss.second; }
    		res = res.joinTable(other, newName);
    	}
    	
    	// WHERE
		bexp.evaluate(res.getColumns(), null);
		
		ArrayList<Row> resultRows = new ArrayList<Row>();
		ArrayList<Integer> maxLens = new ArrayList<Integer>();
		for(Column col : res.getColumns()){
			maxLens.add(0);
		}
    	for(Row row : res.getRows()){
    		if(bexp.evaluate(res.getColumns(), row) instanceof True){
    			resultRows.add(row);
    			for(int i=0;i<res.getColumns().size();i++){
    				maxLens.set(i, Integer.max(maxLens.get(i), row.getValue(i).toString().length()));
    			}
    		}
    	}
    	
    	// SELECT
    	if(selectList.isEmpty()){
    		for(Column col : res.getColumns()){
    			selectList.add(new Pair<Pair<String,String>,String>(new Pair<String,String>(col.getTable(),col.getName()),null));
    		}
    	}
    	
    	ArrayList<Integer> colIndexes = new ArrayList<Integer>();
    	ArrayList<String> colNameAs = new ArrayList<String>();
    	int selId = 0;
    	for(Pair<Pair<String,String>,String> pps : selectList){
    		Pair<String,String> pss = pps.first;
    		if(pps.second == null)colNameAs.add(pss.second);
    		else colNameAs.add(pps.second);
    		colIndexes.add(0);
    		int count = 0;
    		for(int i=0;i<res.getColumns().size();i++){
    			if(pss.first == null){
    				if(pss.second.equals(res.getColumns().get(i).getName())){
    					colIndexes.set(selId, i);
    					count++;
    				}
    			}
    			else{
    				if(pss.first.equals(res.getColumns().get(i).getTable()) && pss.second.equals(res.getColumns().get(i).getName())){
    					colIndexes.set(selId, i);
    					count++;
    				}
    			}
    		}
    		if(count != 1){ throw new SelectColumnResolveError(pss.second); }
    		selId++;
    	}
    	
    	String line = "+";
    	for(int i=0;i<selectList.size();i++){
    		int len = Integer.max(maxLens.get(colIndexes.get(i)), colNameAs.get(i).length());
    		for(int j=0;j<len+2;j++){
    			line = line+'-';
    		}
    		line = line + '+';
    	}
    	
    	// PRINT
    	System.out.println(line);
    	System.out.print("|");
    	for(int i=0;i<selectList.size();i++){
    		int len = Integer.max(maxLens.get(colIndexes.get(i)), colNameAs.get(i).length());
    		String format = "%" + (len+1) + "s";
    		System.out.print(String.format(format, colNameAs.get(i)));
    		System.out.print(" |");
    	}
    	System.out.println("");
    	System.out.println(line);
    	
    	for(Row row : resultRows){
    		System.out.print("|");
        	for(int i=0;i<selectList.size();i++){
        		int len = Integer.max(maxLens.get(colIndexes.get(i)), colNameAs.get(i).length());
        		String format = "%" + (len+1) + "s";
        		System.out.print(String.format(format, row.getValue(colIndexes.get(i))));
        		System.out.print(" |");
        	}
        	System.out.println("");
    	}
    	System.out.println(line);
    }
    
    public void printMessage(String s){
    	System.out.println(s);
    }
}
