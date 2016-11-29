package jnDB.exception;

public class InsertReferentialIntegrityError extends RuntimeException {
	public InsertReferentialIntegrityError(){}
	
	public String getMessage(){
		return "Insertion has failed: Referential integrity violation";
	}
}
