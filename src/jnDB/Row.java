package jnDB;

import java.util.ArrayList;

import jnDB.type.*;

public class Row implements java.io.Serializable {
	ArrayList<Value> values;
	
	public Row(ArrayList<Value> vs){
		values = vs;
	}
	
	public void append(Value v){
		values.add(v);
	}
	
	public void appendAll(Row row){
		values.addAll(row.values);
	}
	
	public Value getValue(int i) {
		return values.get(i);
	}
	
	public void setValue(int i, Value v){
		values.set(i, v);
	}
	
	public boolean equals(Object obj){
		return this == obj;
	}
	
	public String toString(){
		String r = "|";
		for(Value v : values){
			r = r + (" " + v.toString() + " |");
		}
		return r;
	}
}
