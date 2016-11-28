package jnDB.expression;

public class ParenthesizedBooleanExpression extends BooleanTest {
	BooleanExpression booleanExpression;
	
	public ParenthesizedBooleanExpression(BooleanExpression bexp){
		booleanExpression = bexp;
	}
}
