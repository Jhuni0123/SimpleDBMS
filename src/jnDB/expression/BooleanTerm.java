package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;

public class BooleanTerm {
	private ArrayList<BooleanFactor> booleanFactors;
	
	public BooleanTerm(ArrayList<BooleanFactor> bFactors){
		booleanFactors = bFactors;
	}
	
	public BooleanTerm(){
		booleanFactors = new ArrayList<BooleanFactor>();
	}

	public boolean evaluate(ArrayList<Column> columns, Row row) {
		for(BooleanFactor bFactor : booleanFactors){
			if(!bFactor.evaluate(columns,row)){
				return false;
			}
		}
		return true;
	}
}
