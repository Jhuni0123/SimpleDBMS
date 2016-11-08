package jnDB.exception;

public class ReferenceTypeError extends RuntimeException {
	public ReferenceTypeError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: foreign key references wrong type";
	}
}
