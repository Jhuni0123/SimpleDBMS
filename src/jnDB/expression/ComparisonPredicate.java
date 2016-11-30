package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.*;

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
	
	public BooleanValue evaluate(ArrayList<Column> columns, Row row) {
		if(row == null){
			left.evaluate(columns, row);
			right.evaluate(columns, row);
			return null;
		}
		Value lv = left.evaluate(columns,row);
		Value rv = right.evaluate(columns,row);
		if(lv instanceof NullValue || rv instanceof NullValue){ return new Unknown(); }
		int result = lv.compareTo(rv);
		boolean ret;
		
		switch(cop){
		case L : ret = result <  0; break;
		case G : ret = result >  0; break;
		case LE: ret = result <= 0; break;
		case GE: ret = result >= 0; break;
		case EQ: ret = result == 0; break;
		case NE: ret = result != 0; break;
		default: return new False();
		}
		if(ret == false){ return new False(); }
		else { return new True(); }
	}
	
	public String toString(){
		String r = "";
		r = r + left;
		switch(cop){
		case L : r = r + " < "; break;
		case G : r = r + " > "; break;
		case LE: r = r + " <= "; break;
		case GE: r = r + " >= "; break;
		case EQ: r = r + " = "; break;
		case NE: r = r + " != "; break;
		}
		return r + right;
	}
}
