package jnDB.exception;

public class WhereAmbiguousReference extends RuntimeException {
	public WhereAmbiguousReference(){}
	
	public String getMessage(){
		return "Where clause contains ambiguous reference";
	}
}
