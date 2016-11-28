package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.Value;

public class ColumnValue extends CompOperand {
	String tableName;
	String columnName;
	
	public ColumnValue(String tName, String cName){
		tableName = tName;
		columnName = cName;
	}
	
	public Value evaluate(ArrayList<Column> columns, Row row) {
		// TODO: get correct value
		return row.getValue(0);
	}

}
