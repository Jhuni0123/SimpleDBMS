package jnDB.type;

import jnDB.exception.WhereIncomparableError;

public class CharValue extends Value{
	public String value;
	
	public CharValue(String v){
		value = v;
	}
	
	public boolean castTo(Type t){
		if(t instanceof CharType){
			CharType ct = (CharType)t;
			if(value.length() < ct.length()){ return true; }
			value = value.substring(0, ((CharType) t).length());
			return true;
		}
		return false;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof CharValue){
			return value.equals(((CharValue)obj).value);
		}
		return false;
	}
	
	public int hashCode(){
		if(value == null)return 0;
		return value.hashCode();
	}

	@Override
	public int compareTo(Value rv) {
		if(rv instanceof CharValue){
			CharValue cv = (CharValue)rv;
			return value.compareTo(cv.value);
		}
		throw new WhereIncomparableError();
	}
	
	public String toString(){
		return value;
	}
}
