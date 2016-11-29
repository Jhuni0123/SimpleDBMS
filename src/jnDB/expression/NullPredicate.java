package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.*;

public class NullPredicate extends Predicate {
	String tableName, columnName;
	boolean isNull;
	final Value NULL = new NullValue(); 
	public NullPredicate(String tName, String cName, boolean isnull){
		tableName = tName;
		columnName = cName;
		isNull = isnull;
	}

	public BooleanValue evaluate(ArrayList<Column> columns, Row row) {
		// TODO: get correct value
		Value v = row.getValue(0);
		if(v instanceof NullValue){
			if(isNull){ return new True(); }
			else { return new False(); }
		}
		else{
			if(isNull){ return new False(); }
			else { return new True(); }
		}
	}
}
