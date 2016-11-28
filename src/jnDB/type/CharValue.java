package jnDB.type;

public class CharValue extends Value{
	public String value;
	
	public CharValue(String v){
		value = v;
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
}
