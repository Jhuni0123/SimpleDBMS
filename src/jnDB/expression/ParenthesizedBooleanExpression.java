package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;

public class ParenthesizedBooleanExpression extends BooleanTest {
	BooleanExpression booleanExpression;
	
	public ParenthesizedBooleanExpression(BooleanExpression bexp){
		booleanExpression = bexp;
	}
	
	public boolean evaluate(ArrayList<Column> columns, Row row) {
		return booleanExpression.evaluate(columns, row);
	}

}
