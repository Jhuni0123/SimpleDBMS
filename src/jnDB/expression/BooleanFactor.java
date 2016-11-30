package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.*;

public class BooleanFactor {
	boolean not;
	BooleanTest booleanTest;
	
	public BooleanFactor(boolean n, BooleanTest bTest){
		not = n;
		booleanTest = bTest;
	}

	public BooleanValue evaluate(ArrayList<Column> columns, Row row) {
		if(row == null){
			booleanTest.evaluate(columns, row);
			return null;
		}
		BooleanValue bt = booleanTest.evaluate(columns, row);
		if(bt instanceof False){
			if(not)return new True();
			else return new False();
		}
		else if(bt instanceof True){
			if(not)return new False();
			else return new True();
		}
		else if(bt instanceof Unknown){ return new Unknown(); }
		
		return null;
	}
	
	public String toString(){
		String r = "";
		if(not)r = r + "not ";
		return r + booleanTest;
	}
}
