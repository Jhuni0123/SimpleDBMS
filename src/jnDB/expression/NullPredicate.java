package jnDB.expression;

public class NullPredicate extends Predicate {
	String tableName, columnName;
	boolean isNull;
	
	public NullPredicate(String tName, String cName, boolean isnull){
		tableName = tName;
		columnName = cName;
		isNull = isnull;
	}
}
