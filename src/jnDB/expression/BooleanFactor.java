package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;

public class BooleanFactor {
	boolean not;
	BooleanTest booleanTest;
	
	public BooleanFactor(boolean n, BooleanTest bTest){
		not = n;
		booleanTest = bTest;
	}

	public boolean evaluate(ArrayList<Column> columns, Row row) {
		return not ^ booleanTest.evaluate(columns,row);
	}
}
