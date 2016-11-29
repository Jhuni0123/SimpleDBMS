package jnDB.type;

import jnDB.exception.WhereIncomparableError;

public class NullValue extends Value {
	
	public NullValue(){}
	
	
	public boolean equals(Object obj){
		if(obj instanceof NullValue){ return true; }
		return false;
	}
	
	public int hashCode(){
		return 0;
	}

	@Override
	public int compareTo(Value rv) {
		if(rv instanceof NullValue){ return 0; }
		throw new WhereIncomparableError();
	}
	
	public String toString(){
		return "NULL";
	}


	@Override
	public boolean castTo(Type t) {
		return true;
	}
}
