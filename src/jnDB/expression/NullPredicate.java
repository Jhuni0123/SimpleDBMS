package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.NullValue;
import jnDB.type.Value;

public class NullPredicate extends Predicate {
	String tableName, columnName;
	boolean isNull;
	final Value NULL = new NullValue(); 
	public NullPredicate(String tName, String cName, boolean isnull){
		tableName = tName;
		columnName = cName;
		isNull = isnull;
	}

	public boolean evaluate(ArrayList<Column> columns, Row row) {
		// TODO: get correct value
		Value v = row.getValue(0);
		return v.equals(NULL) == isNull;
	}
}
