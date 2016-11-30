package jnDB.expression;

import java.util.ArrayList;

import jnDB.Column;
import jnDB.Row;
import jnDB.type.*;

public class BooleanTerm {
	private ArrayList<BooleanFactor> booleanFactors;
	
	public BooleanTerm(ArrayList<BooleanFactor> bFactors){
		booleanFactors = bFactors;
	}
	
	public BooleanTerm(){
		booleanFactors = new ArrayList<BooleanFactor>();
	}

	public BooleanValue evaluate(ArrayList<Column> columns, Row row) {
		if(row == null){
			for(BooleanFactor bf : booleanFactors){
				bf.evaluate(columns, row);
			}
			return null;
		}
		BooleanValue result = new True();
		for(BooleanFactor bFactor : booleanFactors){
			BooleanValue bf = bFactor.evaluate(columns, row);
			if(bf instanceof False){
				result = new False();
			}
			else if(result instanceof True && bf instanceof Unknown){
				result = new Unknown();
			}
		}
		return result;
	}
}
