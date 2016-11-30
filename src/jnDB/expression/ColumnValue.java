package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.exception.WhereAmbiguousReference;
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
				else if(count > 1){ throw new WhereAmbiguousReference(); }
				return null;
			}
			boolean exists = false;
			for(Column col : columns){
				if(tableName.equals(col.getTable())){ exists = true; break; }
			}
			if(!exists){ throw new WhereTableNotSpecified(); }
			
			for(Column col : columns){
				if(tableName.equals(col.getTable()) && columnName.equals(col.getName())){ return null; }
			}
			throw new WhereColumnNotExist();
		}
		
		if(tableName == null){
			int index = 0;
			for(Column col : columns){
				if(columnName.equals(col.getName())){ return row.getValue(index); }
				index++;
			}
		}
		int index = 0;
		for(Column col : columns){
			if(tableName.equals(col.getTable()) && columnName.equals(col.getName())){ return row.getValue(index); }
			index++;
		}
		return null;
	}
	
	public String toString(){
		String r = "";
		if(tableName != null)r = r + tableName + ".";
		return r + columnName;
	}
}
