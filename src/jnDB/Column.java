package jnDB;

import jnDB.type.*;

public class Column {
	String name;
	Type type;
	boolean isNotNull, isPrimaryKey, isForeignKey;
	
	public Column(String n, Type t, boolean isnn){
		name = n;
		type = t;
		isNotNull = isnn;
		isPrimaryKey = false;
		isForeignKey = false;
	}
}
