package jnDB.exception;

public class CharLengthError extends RuntimeException {
	public CharLengthError(){}
	
	@Override
	public String getMessage(){
		return "Char length should be over 0";
	}
}
