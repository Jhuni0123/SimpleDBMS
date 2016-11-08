package jnDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Table implements java.io.Serializable {
	private String name;
	private ArrayList<Column> columns;
	private HashMap<String, Integer> colMap;
	
	public Table(TableSchema schema){
		this.name = schema.getName();
		this.columns = new ArrayList<Column>();
		for(Entry<String, Column> elem : schema.columns.entrySet()){
			columns.add(elem.getValue());
		}
	}
	
	public void printAll(){
		System.out.println("table_name [" + name + "]");
		System.out.format("%-20s%-15s%-15s%-15s\n", "column_name", "type", "null", "key");
		for(Column col : columns){
			System.out.println(col.toString());
		}
	}
	
	public String getName(){ return name; }
}
