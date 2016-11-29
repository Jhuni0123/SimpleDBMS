package jnDB.exception;

public class SelectColumnResolveError {
	private String colName;
	public SelectColumnResolveError(String cName){
		colName = cName;
	}
	
	public String getMessage(){
		return "Selection has failed: fail to resolve '" + colName + "'";
	}
}
