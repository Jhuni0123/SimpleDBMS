package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.Value;

public class ComparableValue extends CompOperand {
	Value value;
	
	public ComparableValue(Value v){
		value = v;
	}
	
	public Value evaluate(ArrayList<Column> columns, Row row) {
		return value;
	}

}
