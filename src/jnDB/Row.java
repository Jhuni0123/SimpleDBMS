package jnDB;

import java.util.ArrayList;

import jnDB.type.*;

public class Row implements java.io.Serializable {
	ArrayList<Value> values;
	ArrayList<Row> referencedBy, referenceTo;
	
	public Row(ArrayList<Value> vs){
		values = vs;
		referencedBy = new ArrayList<Row>();
		referenceTo = new ArrayList<Row>();
	}
	
	public Value getValue(int i) {
		return values.get(i);
	}
	
	public boolean equals(Object obj){
		return this == obj;
	}
}
