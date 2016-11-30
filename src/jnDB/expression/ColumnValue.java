package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.exception.WhereColumnNotExist;
import jnDB.exception.WhereTableNotSpecified;
import jnDB.type.Value;

public class ColumnValue extends CompOperand {
	String tableName;
	String columnName;
	
	public ColumnValue(String tName, String cName){
		tableName = tName;
		columnName = cName;
	}
	
	public Value evaluate(ArrayList<Column> columns, Row row) {
		if(row == null){
			if(tableName == null){
				int count = 0;
				for(Column col : columns){
					if(columnName.equals(col.getName())){ count++; }
				}
				if(count == 0){ throw new WhereColumnNotExist(); }
				else if(count > 1){ throw new WhereTableNotSpecified(); }
				return null;
			}
			for(Column col : columns){
				if(tableName.equals(col.getTable()) && columnName.equals(col.getName())){ return null; }
			}
			throw new WhereColumnNotExist();
		}
		return row.getValue(0);
	}

}
