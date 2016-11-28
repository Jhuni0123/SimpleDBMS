package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;

public abstract class Predicate extends BooleanTest {
	public abstract boolean evaluate(ArrayList<Column> columns, Row row);
}
