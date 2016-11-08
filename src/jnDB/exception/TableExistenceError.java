package jnDB.exception;

public class TableExistenceError extends RuntimeException {
	public TableExistenceError(){}
	
	@Override
	public String getMessage(){
		return "Create table has failed: table with the same name already exists";
	}
}
