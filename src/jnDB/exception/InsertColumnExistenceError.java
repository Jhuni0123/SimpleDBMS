package jnDB.exception;

public class InsertColumnExistenceError extends RuntimeException {
	String colName;
	public InsertColumnExistenceError(String cName){
		colName = cName;
	}
	public String getMessage(){
		return "Insertion has failed: '" + colName + "' does not exist";	
	}
}
