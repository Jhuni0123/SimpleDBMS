package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.Value;

public class ComparisonPredicate extends Predicate {
	static final int L = 0;
	static final int G = 1;
	static final int LE = 2;
	static final int GE = 3;
	static final int EQ = 4;
	static final int NE = 5;
	
	CompOperand left, right;
	int cop;
	public ComparisonPredicate(CompOperand l, String op, CompOperand r){
		left = l;
		right = r;
		if(op.equals("<")) cop = L;
		else if(op.equals(">")) cop = G;
		else if(op.equals("<=")) cop = LE;
		else if(op.equals(">=")) cop = GE;
		else if(op.equals("=")) cop = EQ;
		else if(op.equals("!=")) cop = NE;
	}
	
	public boolean evaluate(ArrayList<Column> columns, Row row) {
		Value lv = left.evaluate(columns,row);
		Value rv = right.evaluate(columns,row);
		int result = lv.compareTo(rv);
		
		switch(cop){
		case L : return result <  0;
		case G : return result >  0;
		case LE: return result <= 0;
		case GE: return result >= 0;
		case EQ: return result == 0;
		case NE: return result != 0;
		default: return false;
		}
	}

}
