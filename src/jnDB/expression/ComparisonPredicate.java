package jnDB.expression;

public class ComparisonPredicate extends Predicate {
	static final String L = "<";
	static final String G = ">";
	static final String LE = "<=";
	static final String GE = ">=";
	static final String EQ = "=";
	static final String NE = "!=";
	
	CompOperand left, right;
	String compOp;
	public ComparisonPredicate(CompOperand l, String op, CompOperand r){
		left = l;
		compOp = op;
		right = r;
	}
}
