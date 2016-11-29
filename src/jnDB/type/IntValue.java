package jnDB.type;

import jnDB.exception.WhereIncomparableError;

public class IntValue extends Value {
	public int value;
	
	public IntValue(int v){
		value = v;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof IntValue){
			return value == ((IntValue)obj).value;
		}
		return false;
	}
	
	public int hashCode(){
		return Integer.hashCode(value);
	}

	@Override
	public int compareTo(Value rv) {
		if(rv instanceof IntValue){
			IntValue iv = (IntValue)rv;
			if(value < iv.value)return -1;
			else if (value > iv.value)return 1;
			else return 0;
		}
		throw new WhereIncomparableError();
	}
	
	public String toString(){
		return Integer.toString(value);
	}

	@Override
	public boolean castTo(Type t) {
		if(t instanceof IntType){
			return true;
		}
		return false;
	}
}
