package jnDB.type;

public abstract class Value implements java.io.Serializable {

	public abstract int compareTo(Value rv);
	public abstract boolean castTo(Type t);
}