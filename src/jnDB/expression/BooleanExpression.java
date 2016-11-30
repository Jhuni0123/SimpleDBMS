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
		if(row == null){
			for(BooleanTerm bt : booleanTerms){
				bt.evaluate(columns, row);
			}
			return null;
		}
		BooleanValue result = new False();
		for(BooleanTerm bTerm : booleanTerms){
			BooleanValue bt = bTerm.evaluate(columns, row);
			if(bt instanceof True){
				result = new True();
			}
			else if(result instanceof False && bt instanceof Unknown){
				result = new Unknown();
			}
		}
		return result;
	}
	
	public String toString(){
		if(booleanTerms.isEmpty())return "False";
		String r = "" + booleanTerms.get(0);
		for(int i=1;i<booleanTerms.size();i++){
			r = r + " or " + booleanTerms.get(i);
		}
		return r;
	}
}
