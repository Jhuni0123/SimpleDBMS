package jnDB;

import java.util.ArrayList;

import jnDB.type.*;

public class Column implements java.io.Serializable {
	private String name, tableName;
	private Type type;
	private boolean notNull, primaryKey, foreignKey;
	private ArrayList<RefConstraint> refList;
	
	public Column(String tName, String n, Type t, boolean isnn){
		tableName = tName;
		name = n;
		type = t;
		notNull = isnn;
		primaryKey = false;
		foreignKey = false;
		refList = new ArrayList<RefConstraint>();
	}
	
	public void setForeignKey(String tName, String cName){
		foreignKey = true;
		refList.add(new RefConstraint(tName,cName));
	}
	
	public void setPrimaryKey(){
		primaryKey = true;
		notNull = true;
	}
	
	public String getName(){ return name; }
	public String getTable(){ return tableName; }
	@Override
	public String toString(){
		return String.format("%-20s%-15s%-15s%-15s", name, type.toString(), notNull?"N":"Y", primaryKey?foreignKey?"PRI/FOR":"PRI":foreignKey?"FOR":"");
	}
	public boolean isNotNull(){ return notNull; }
	public boolean isPrimaryKey(){ return primaryKey; }
	public boolean isForeignKey(){ return foreignKey; }
	public Type getType(){ return type; }
	
	class RefConstraint implements java.io.Serializable {
		String tableName, colName;
		public RefConstraint(String tName, String cName){
			tableName = tName;
			colName = cName;
		}
	}
}
