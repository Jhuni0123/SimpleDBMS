package jnDB.exception;

public class WhereIncomparableError extends RuntimeException {
	public WhereIncomparableError(){}
	
	public String getMessage(){
		return "Where clause try to compare incomparable values";
	}
}
