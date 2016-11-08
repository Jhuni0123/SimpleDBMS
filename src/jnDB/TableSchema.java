package jnDB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import jnDB.exception.*;
import jnDB.type.Type;

public class TableSchema {
	String name;
	HashMap<String, Column> columns;
	HashSet<String> publicKey;
	
	public TableSchema(String name){
		this.name = name;
		columns = new HashMap<String, Column>();
		publicKey = new HashSet<String>();
	}
	
	public void addColumn(String cName, Type t, boolean isNotnull) throws DuplicateColumnDefError {
		if(columns.get(cName) != null)throw new DuplicateColumnDefError();
	}
	
	public void setPrimaryKey(ArrayList<String> cnList) throws DuplicatePrimaryKeyDefError{
		if(!publicKey.isEmpty())throw new DuplicatePrimaryKeyDefError();
		for(String cName : cnList){
			publicKey.add(cName);
		}
	}
	
	public void setReferentialKey(ArrayList<String> cnList, String tName, ArrayList<String> targetList){
		
	}
	
}