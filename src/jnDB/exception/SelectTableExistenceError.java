package jnDB.exception;

public class SelectTableExistenceError {
	String tableName;
	public SelectTableExistenceError(String tName){
		tableName = tName;
	}
	
	public String getMessage(){
		return "Selection has failed: '" + tableName + "' does not exist";
	}
}
