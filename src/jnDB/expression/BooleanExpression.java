package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;

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
	
	public boolean evaluate(ArrayList<Column> columns, Row row){
		for(BooleanTerm bTerm : booleanTerms){
			if(bTerm.evaluate(columns,row)){
				return true;
			}
		}
		return false;
	}
}
