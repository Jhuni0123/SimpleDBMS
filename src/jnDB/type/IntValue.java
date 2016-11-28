package jnDB.type;

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
}
