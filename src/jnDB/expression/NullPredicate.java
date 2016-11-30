package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.exception.WhereColumnNotExist;
import jnDB.exception.WhereTableNotSpecified;
import jnDB.type.*;

public class NullPredicate extends Predicate {
	String tableName, columnName;
	boolean isNull;
	final Value NULL = new NullValue(); 
	public NullPredicate(String tName, String cName, boolean isnull){
		tableName = tName;
		columnName = cName;
		isNull = isnull;
	}

	public BooleanValue evaluate(ArrayList<Column> columns, Row row) {
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
		// TODO: get correct value
		Value v = row.getValue(0);
		if(v instanceof NullValue){
			if(isNull){ return new True(); }
			else { return new False(); }
		}
		else{
			if(isNull){ return new False(); }
			else { return new True(); }
		}
	}
}
