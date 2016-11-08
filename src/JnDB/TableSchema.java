package JnDB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
public class TableSchema {
	String name;
	HashMap<String, Type> columnList;
	HashSet<String> publicKey;
	
	public TableSchema(String name){
		this.name = name;
		columnList = new HashMap<String, Type>();
		publicKey = null;
	}
	
	public void addColumn(String n, Type t, boolean isNotnull){
		
	}
	
	public void setPrimaryKey(ArrayList<String> cnList){
		
	}
	
	public void setReferentialKey(ArrayList<String> cnList, String tName, ArrayList<String> targetList){
		
	}
	
}