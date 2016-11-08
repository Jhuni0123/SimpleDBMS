package jnDB.exception;

public class ReferenceTableExistenceError extends RuntimeException {
	public ReferenceTableExistenceError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: foreign key references non existing table";
	}
}
