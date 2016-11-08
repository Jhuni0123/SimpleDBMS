package jnDB.exception;

public class ShowTablesNoTable extends RuntimeException {
	public ShowTablesNoTable(){}
	
	@Override
	public String getMessage(){
		return "There is no table";
	}
}
