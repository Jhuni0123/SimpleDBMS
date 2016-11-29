package jnDB.exception;

public class WhereAmbiguousReference {
	public WhereAmbiguousReference(){}
	
	public String getMessage(){
		return "Where clause contains ambiguous reference";
	}
}
