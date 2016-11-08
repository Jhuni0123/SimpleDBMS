package jnDB.exception;

public class DropReferencedTableError extends RuntimeException {
	String tableName;
	
	public DropReferencedTableError(String tName){
		tableName = tName;
	}
	
	@Override
	public String getMessage(){
		return "Drop table has failed: '" + tableName + "' is referenced by other table";
	}
}
