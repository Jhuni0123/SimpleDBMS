package jnDB.exception;

public class WhereIncomparableError {
	public WhereIncomparableError(){}
	
	public String getMessage(){
		return "Where clause try to compare incomparable values";
	}
}
