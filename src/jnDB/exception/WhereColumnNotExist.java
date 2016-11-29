package jnDB.exception;

public class WhereColumnNotExist extends RuntimeException {
	public WhereColumnNotExist(){}
	
	public String getMessage(){
		return "Where clause try to reference non existing column";
	}
}
