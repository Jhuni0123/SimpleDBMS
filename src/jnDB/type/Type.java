package jnDB.type;

public class Type{	
	public Type(){}
	
	@Override
	public boolean equals(Object obj){
		if(this.getClass() != obj.getClass()) return false;
		if(this.getClass() == CharType.class){ return ((CharType)this).len == ((CharType)obj).len; }
		return true;
	}
}
