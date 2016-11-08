package jnDB;

import jnDB.type.*;

public class Column {
	private String name;
	private Type type;
	private boolean notNull, primaryKey, foreignKey;
	private String refTableName, refColName;
	
	public Column(String n, Type t, boolean isnn){
		name = n;
		type = t;
		notNull = isnn;
		primaryKey = false;
		foreignKey = false;
	}
	
	public void setForeignKey(String tName, String cName){
		foreignKey = true;
		refTableName = tName;
		refColName = cName;
	}
	
	public boolean isNotNull(){ return notNull; }
	public boolean isPrimaryKey(){ return primaryKey; }
	public boolean isForeignKey(){ return foreignKey; }
}
