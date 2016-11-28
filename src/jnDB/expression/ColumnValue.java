package jnDB.expression;

public class ColumnValue extends CompOperand {
	String tableName;
	String columnName;
	
	public ColumnValue(String tName, String cName){
		tableName = tName;
		columnName = cName;
	}
}
