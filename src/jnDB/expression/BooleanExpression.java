package jnDB.expression;

import java.util.ArrayList;

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
	
}
