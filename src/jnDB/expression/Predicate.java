package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.BooleanValue;

public abstract class Predicate extends BooleanTest {
	public abstract BooleanValue evaluate(ArrayList<Column> columns, Row row);
}
