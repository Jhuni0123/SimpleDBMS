package jnDB.type;

public class Type implements java.io.Serializable {	
	//public Type(){}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)return false;
		if(this.getClass() != obj.getClass()) return false;
		if(this.getClass() == CharType.class){ return ((CharType)this).length() == ((CharType)obj).length(); }
		return true;
	}
	
	public void check(){
		if(this instanceof CharType){
			CharType ct = (CharType)this;
			ct.check();
		}
	}
}
