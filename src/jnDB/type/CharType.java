package jnDB.type;

import jnDB.exception.*;

public class CharType extends Type {
	private int len;
	public CharType(int l) {
		if(l==0)throw new CharLengthError();
		len = l;
	}
	
	public int length(){
		return len;
	}
	@Override
	public String toString(){
		return "char(" + Integer.toString(len) + ")";
	}
}
