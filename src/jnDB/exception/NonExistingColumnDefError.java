package jnDB.exception;

public class NonExistingColumnDefError extends RuntimeException {
	String colName;
	public NonExistingColumnDefError(String cName){
		colName = cName;
	}
	
	@Override
	public String getMessage(){
		return "Create table has failed: '" + colName + "' does not exists in column definition";
	}
}
