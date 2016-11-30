package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.BooleanValue;

public class ParenthesizedBooleanExpression extends BooleanTest {
	BooleanExpression booleanExpression;
	
	public ParenthesizedBooleanExpression(BooleanExpression bexp){
		booleanExpression = bexp;
	}
	
	public BooleanValue evaluate(ArrayList<Column> columns, Row row){
		return booleanExpression.evaluate(columns, row);
	}
	public String toString(){
		return "(" + booleanExpression + ")";
	}
}
