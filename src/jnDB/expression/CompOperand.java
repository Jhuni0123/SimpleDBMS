package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.Value;

public abstract class CompOperand {

	public abstract Value evaluate(ArrayList<Column> columns, Row row);
	
}
