package jnDB.exception;

public class FromDuplicateTableNameError extends RuntimeException {
	private String tableName;
	
	public FromDuplicateTableNameError(String tName){
		tableName = tName;
	}
	
	public String getMessage(){
		return "Not unique table '" + tableName + "'";
	}
}
