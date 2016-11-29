package jnDB.exception;

public class InsertReferentialIntegrityError {
	public InsertReferentialIntegrityError(){}
	
	public String getMessage(){
		return "Insertion has failed: Referential integrity violation";
	}
}
