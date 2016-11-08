package jnDB.type;

import jnDB.exception.*;

public class CharType extends Type {
	int len;
	public CharType(int l) {
		if(l==0)throw new CharLengthError();
		len = l;
	}
}
