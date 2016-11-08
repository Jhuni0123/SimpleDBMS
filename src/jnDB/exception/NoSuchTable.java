package jnDB.exception;

public class NoSuchTable extends RuntimeException {
	public NoSuchTable(){}
	
	@Override
	public String getMessage(){
		return "No such table";
	}
}
