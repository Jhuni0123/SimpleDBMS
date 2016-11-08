package jnDB.exception;

public class DuplicateColumnDefError extends RuntimeException {
	public DuplicateColumnDefError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: column definition is duplicated";
	}
}
