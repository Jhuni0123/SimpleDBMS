package jnDB.exception;

public class WhereColumnNotExist {
	public WhereColumnNotExist(){}
	
	public String getMessage(){
		return "Where clause try to reference non existing column";
	}
}
