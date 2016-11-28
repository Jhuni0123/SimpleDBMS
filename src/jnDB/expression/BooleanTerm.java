package jnDB.expression;

import java.util.ArrayList;

public class BooleanTerm {
	private ArrayList<BooleanFactor> booleanFactors;
	
	public BooleanTerm(ArrayList<BooleanFactor> bFactors){
		booleanFactors = bFactors;
	}
	
	public BooleanTerm(){
		booleanFactors = new ArrayList<BooleanFactor>();
	}
}
