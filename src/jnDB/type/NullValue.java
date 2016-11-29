package jnDB.type;

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
		// TODO Auto-generated method stub
		return 0;
	}
}
