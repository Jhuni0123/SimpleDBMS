package jnDB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import jnDB.TableSchema.ReferentialConstraint;
import jnDB.exception.*;
import jnDB.expression.BooleanExpression;
import jnDB.type.*;

public class JnDatabase {
	public static final String CreateTableSuccess(String tableName) { return "'" + tableName + "' table is created"; }
	public static final String DropSuccess(String tableName) { return "'" + tableName + "' table is droped"; }
	public static final String NO_SUCH_TABLE = "No such table";
	public static final String SHOW_TABLES_NO_TABLE = "There is no table";
	Environment myDbEnvironment;
    Database myDatabase;
    
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
    }
    
    public void close(){
    	if(myDatabase != null) myDatabase.close();
        if(myDbEnvironment != null) myDbEnvironment.close();
    }
    
    private static byte[] serializeTable(Table table) throws IOException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(table);
        return out.toByteArray();
    }
    
    public static Table deserializeTable(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Table) is.readObject();
    }
    
    public void putTable(Table table){
    	Cursor cursor = null;
    	DatabaseEntry key,data;
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		key = new DatabaseEntry(table.getName().getBytes("UTF-8"));
    		data = new DatabaseEntry(serializeTable(table));
    		cursor.put(key, data);
    		cursor.close();
    	}
    	catch (Exception e){
    		if(cursor != null) cursor.close();
    	}
    }
    
    public Table getTable(String tableName){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(tableName)){
        			cursor.close();
        			return deserializeTable(foundData.getData());
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	return null;
    }
    
    public boolean isExistsTable(String tableName){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(tableName)){
        			cursor.close();
        			return true;
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	return false;
    }
    
    public void createTable(TableSchema schema){
    	if(isExistsTable(schema.getName())) throw new TableExistenceError();
    	schema.checkValidity();
    	Table table = new Table(schema);
    	for(ReferentialConstraint rc : schema.rcList){
    		table.referencingTable.add(rc.table.getName());
    		Table target = getTable(rc.table.getName());
    		target.referencedByTable.add(table.getName());
    		putTable(target);
    	}
    	putTable(table);
    	printMessage(CreateTableSuccess(schema.getName()));
    }
    
    public void dropTable(String tableName){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		if(cursor.getFirst(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.NOTFOUND){
    			printMessage(NO_SUCH_TABLE);
    			cursor.close();
    			return;
    		}
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(tableName)){
        			Table table = deserializeTable(foundData.getData());
        			if(!table.isRemovable()) { cursor.close(); throw new DropReferencedTableError(table.getName()); }
        			cursor.delete();
        			for(String tName : table.referencingTable){
        				Table t = getTable(tName);
        				t.referencedByTable.remove(table.getName());
        				putTable(t);
        			}
        			cursor.close();
        			printMessage(DropSuccess(tableName));
        			return;
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(DatabaseException e){
    		e.printStackTrace();
    	} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	cursor.close();
    	printMessage(NO_SUCH_TABLE);
    }
    
    public void desc(String tName){
    	Table table = getTable(tName);
    	if(table == null){
    		printMessage(NO_SUCH_TABLE);
    		return;
    	}
    	System.out.println("-------------------------------------------------");
    	table.printAll();
    	System.out.println("-------------------------------------------------");
    }
    
    public void showTables(){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		if(cursor.getFirst(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
    			printMessage(SHOW_TABLES_NO_TABLE);
    			cursor.close();
    			return;
    		}
    		System.out.println("----------------");
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		System.out.println(keyString);
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
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
    	HashSet<String> s = table.getPKSet();
    	for(String str : pri){
    		if(!s.contains(str))throw new ReferenceNonPrimaryKeyError();
    		s.remove(str);
    	}
    	if(!s.isEmpty())throw new ReferenceNonPrimaryKeyError();
    	return table;
    }
    
    public void insert(String tableName, ArrayList<String> cnList, ArrayList<Value> vList){
    	
    }
    
    public void delete(String tableName, BooleanExpression bexp){
    	
    }
    
    public void select(ArrayList<Pair<Pair<String,String>,String>> selectList, ArrayList<Pair<String,String>> fromList, BooleanExpression bexp){
    	
    }
    
    public void printMessage(String s){
    	System.out.println(s);
    }
}
