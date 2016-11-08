package jnDB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import jnDB.exception.*;
import jnDB.type.Type;

public class TableSchema {
	private String name;
	HashMap<String, Column> columns;
	HashSet<String> publicKey;
	
	public TableSchema(String name){
		this.name = name;
		columns = new HashMap<String, Column>();
		publicKey = new HashSet<String>();
	}
	
	public void addColumn(String cName, Type t, boolean isNotNull) throws DuplicateColumnDefError {
		if(columns.get(cName) != null)throw new DuplicateColumnDefError();
		columns.put(cName, new Column(cName, t, isNotNull));
	}
	
	public void setPrimaryKey(ArrayList<String> cnList) throws DuplicatePrimaryKeyDefError{
		if(!publicKey.isEmpty())throw new DuplicatePrimaryKeyDefError();
		for(String cName : cnList){
			publicKey.add(cName);
		}
	}
	
	public void setReferentialKey(ArrayList<String> cnList, String tableName, ArrayList<String> targetList){
		
	}
	
	public void checkValidity(){
		
	}
	
	public String getName(){ return name; }
}