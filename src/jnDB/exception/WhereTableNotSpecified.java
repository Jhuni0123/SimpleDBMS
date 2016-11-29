package jnDB.exception;

public class WhereTableNotSpecified extends RuntimeException {
	public WhereTableNotSpecified(){}
	
	public String getMessage(){
		return "Where clause try to reference tables which are not specified";
	}
}
