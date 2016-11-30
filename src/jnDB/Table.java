package jnDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import jnDB.TableSchema.ReferentialConstraint;
import jnDB.type.Value;

public class Table implements java.io.Serializable {
	private String name;
	ArrayList<Column> columns;
	HashMap<String, Integer> colNum;
	ArrayList<FKConstraint> fkConstraints;
	HashSet<String> referencedByTable;
	ArrayList<String> primaryKey;
	HashSet<ArrayList<Value>> currPK;
	ArrayList<Row> rows;
	
	public Table(TableSchema schema){
		this.name = schema.getName();
		this.columns = new ArrayList<Column>();
		this.colNum = new HashMap<String, Integer>();
		this.fkConstraints = new ArrayList<FKConstraint>();
		this.referencedByTable = new HashSet<String>();
		this.primaryKey = new ArrayList<String>();
		this.currPK = new HashSet<ArrayList<Value>>();
		this.rows = new ArrayList<Row>();
		
		for(Column col : schema.columns){
			colNum.put(col.getName(), columns.size());
			columns.add(col);
		}
		
		for(String pri : schema.primaryKey){
			columns.get(colNum.get(pri)).setPrimaryKey();
			primaryKey.add(pri);
		}
		
		for(ReferentialConstraint rc : schema.rcList){
			int l = rc.fKeys.size();
			for(int i=0;i<l;i++){
				columns.get(colNum.get(rc.fKeys.get(i))).setForeignKey(rc.table.getName(), rc.pKeys.get(i));
			}
		}
	}
	
	public Table(){
		this.name = "";
		this.columns = new ArrayList<Column>();
		this.colNum = new HashMap<String, Integer>();
		this.fkConstraints = new ArrayList<FKConstraint>();
		this.referencedByTable = new HashSet<String>();
		this.primaryKey = new ArrayList<String>();
		this.currPK = new HashSet<ArrayList<Value>>();
		this.rows = new ArrayList<Row>();
	}
	
	public final ArrayList<Column> getColumns(){
		return columns;
	}
	public ArrayList<Row> getRows(){
		return rows;
	}
	
	public void descript(){
		System.out.println("table_name [" + name + "]");
		System.out.format("%-20s%-15s%-15s%-15s\n", "column_name", "type", "null", "key");
		for(Column col : columns){
			System.out.println(col.toString());
		}
	}
	
	public ArrayList<String> getPKSet(){
		return (ArrayList<String>)primaryKey.clone();
	}
	public String getName(){ return name; }
	public boolean isRemovable(){ return referencedByTable.isEmpty(); }
	
	public void printAll(){
		for(Row row : rows){
			System.out.println(row.toString());
		}
	}
	
	public int getColIndex(String cName){
		if(!colNum.containsKey(cName))return -1;
		return colNum.get(cName);
	}
	
	public void addRow(Row row){
		rows.add(row);
	}
	
	public void removeRow(int index){
		rows.remove(index);
	}
	
	public Table joinTable(Table other, String tNameAs){
		Table res = new Table();
		for(Column col : columns){
			res.columns.add(col);
		}
		for(Column col : other.columns){
			res.columns.add(new Column(tNameAs, col.getName(), col.getType(), col.isNotNull()));
		}
		
		for(Row row1 : rows){
			for(Row row2 : other.rows){
				Row newRow = new Row(new ArrayList<Value>());
				newRow.appendAll(row1);
				newRow.appendAll(row2);
				res.addRow(newRow);
			}
		}
		return res;
	}
}
