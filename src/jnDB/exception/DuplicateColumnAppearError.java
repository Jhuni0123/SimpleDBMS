package jnDB.exception;

public class DuplicateColumnAppearError extends RuntimeException {
	public DuplicateColumnAppearError(){}
	
	@Override
	public String getMessage(){
		return "Duplicate column appear in list";
	}
}
