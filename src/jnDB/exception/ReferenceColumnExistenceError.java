package jnDB.exception;

public class ReferenceColumnExistenceError extends RuntimeException {
	public ReferenceColumnExistenceError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: foreign key references non existing column";
	}
}
