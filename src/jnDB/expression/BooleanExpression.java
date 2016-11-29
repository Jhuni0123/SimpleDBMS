package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.*;

public class BooleanExpression {
	ArrayList<BooleanTerm> booleanTerms;
	
	public BooleanExpression(ArrayList<BooleanTerm> bTerms){
		booleanTerms = bTerms;
	}
	
	public BooleanExpression(boolean b){
		booleanTerms = new ArrayList<BooleanTerm>();
		if(b){
			booleanTerms.add(new BooleanTerm());
		}
	}
	
	public BooleanValue evaluate(ArrayList<Column> columns, Row row){
		BooleanValue result = new False();
		for(BooleanTerm bTerm : booleanTerms){
			BooleanValue bt = bTerm.evaluate(columns, row);
			if(bt instanceof True){
				return new True();
			}
			else if(result instanceof False && bt instanceof Unknown){
				result = new Unknown();
			}
		}
		return result;
	}
}
