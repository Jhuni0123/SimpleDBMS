package jnDB.expression;

import jnDB.type.Value;

public class ComparableValue extends CompOperand {
	Value value;
	
	public ComparableValue(Value v){
		value = v;
	}
}
