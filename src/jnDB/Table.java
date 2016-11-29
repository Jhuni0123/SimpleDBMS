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
	HashSet<String> primaryKey;
	HashSet<ArrayList<Value>> currPK;
	ArrayList<Row> rows;
	
	public Table(TableSchema schema){
		this.name = schema.getName();
		this.columns = new ArrayList<Column>();
		this.colNum = new HashMap<String, Integer>();
		this.fkConstraints = new ArrayList<FKConstraint>();
		this.referencedByTable = new HashSet<String>();
		this.primaryKey = new HashSet<String>();
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
	
	public ArrayList<Column> getColumns(){
		return columns;
	}
	public ArrayList<Row> getRows(){
		return rows;
	}
	
	public void printAll(){
		System.out.println("table_name [" + name + "]");
		System.out.format("%-20s%-15s%-15s%-15s\n", "column_name", "type", "null", "key");
		for(Column col : columns){
			System.out.println(col.toString());
		}
	}
	
	public HashSet<String> getPKSet(){		
		return (HashSet<String>) primaryKey.clone();
	}
	public String getName(){ return name; }
	public boolean isRemovable(){ return referencedByTable.isEmpty(); }
}
