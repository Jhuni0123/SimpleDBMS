package jnDB.exception;

public class DuplicatePrimaryKeyDefError extends RuntimeException {
	public DuplicatePrimaryKeyDefError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: primary key definition is duplicated";
	}
}
