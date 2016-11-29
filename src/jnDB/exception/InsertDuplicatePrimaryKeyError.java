package jnDB.exception;

public class InsertDuplicatePrimaryKeyError extends RuntimeException {
	public InsertDuplicatePrimaryKeyError(){}
	
	public String getMessage(){
		return "Insertion has failed: Primary key duplication";
	}
}
