package jnDB.expression;

public class BooleanFactor {
	boolean not;
	BooleanTest booleanTest;
	
	public BooleanFactor(boolean n, BooleanTest bTest){
		not = n;
		booleanTest = bTest;
	}
}
