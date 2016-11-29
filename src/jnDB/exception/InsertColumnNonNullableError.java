package jnDB.exception;

public class InsertColumnNonNullableError extends RuntimeException {
	private String colName;
	public InsertColumnNonNullableError(String cName){
		colName = cName;
	}
	
	public String getMessage(){
		return "Insertion has failed: '" + colName + "' is not nullable";
	}
}
