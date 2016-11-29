package jnDB.exception;

public class InsertTypeMismatchError extends RuntimeException {
	public InsertTypeMismatchError(){}
	
	public String getMessage(){
		return "Insertion has failed: Types are not matched";
	}
}
