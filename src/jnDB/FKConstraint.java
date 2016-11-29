package jnDB;

import java.util.ArrayList;

public class FKConstraint implements java.io.Serializable {
	String toTable;
	ArrayList<String> toColumns;
	ArrayList<String> fromColumns;
	
	public FKConstraint(String tt, ArrayList<String> tc, ArrayList<String> fc){
		toTable = tt;
		toColumns = tc;
		fromColumns = fc;
	}
	
	public String getRefTableName(){
		return toTable;
	}
}
