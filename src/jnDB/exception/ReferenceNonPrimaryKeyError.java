package jnDB.exception;

public class ReferenceNonPrimaryKeyError extends RuntimeException {
	public ReferenceNonPrimaryKeyError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: foreign key references non primary key column";
	}
}
