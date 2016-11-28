package jnDB.type;

public class Type implements java.io.Serializable {	
	//public Type(){}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)return false;
		if(this.getClass() != obj.getClass()) return false;
		if(this.getClass() == CharType.class){ return ((CharType)this).len == ((CharType)obj).len; }
		return true;
	}
}
