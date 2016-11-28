package jnDB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import jnDB.exception.*;
import jnDB.type.Type;

public class TableSchema {
	private String name;
	ArrayList<Column> columns;
	ArrayList<String> primaryKey;
	ArrayList<ReferentialConstraint> rcList;
	
	public TableSchema(String name){
		this.name = name;
		columns = new ArrayList<Column>();
		primaryKey = new ArrayList<String>();
		rcList = new ArrayList<ReferentialConstraint>();
	}
	
	public void addColumn(String cName, Type t, boolean isNotNull) {
		columns.add(new Column(name, cName, t, isNotNull));
	}
	
	public void setPrimaryKey(ArrayList<String> cnList) {
		if(!primaryKey.isEmpty())throw new DuplicatePrimaryKeyDefError();
		for(String cName : cnList){
			primaryKey.add(cName);
		}
	}
	
	public void addReferentialKey(ArrayList<String> cnList, Table table, ArrayList<String> targetList){
		rcList.add(new ReferentialConstraint(cnList,table,targetList));
	}
	
	public static boolean isUniqueList(ArrayList<String> list){
		HashSet<String> s = new HashSet<String>();
		for(String str : list){
			if(s.contains(str))return false;
			s.add(str);
		}
		return true;
	}
	
	public void checkValidity(){
		HashMap<String,Column> colSet = new HashMap<String,Column>();
		for(Column col : columns){
			if(colSet.containsKey(col.getName())) throw new DuplicateColumnAppearError();
			colSet.put(col.getName(), col);
		}
		
		HashSet<String> priSet = new HashSet<String>();
		for(String cName : primaryKey){
			if(!colSet.containsKey(cName)) throw new NonExistingColumnDefError(cName);
			if(priSet.contains(cName)) throw new DuplicateColumnAppearError();
			priSet.add(cName);
		}
		
		for(ReferentialConstraint rc : rcList){
			if(rc.fKeys.size() != rc.pKeys.size()) throw new ReferenceTypeError();
			if((!isUniqueList(rc.fKeys))||(!isUniqueList(rc.pKeys))) throw new DuplicateColumnAppearError();
			int l = rc.fKeys.size();
			for(int i=0;i<l;i++){
				if(!colSet.containsKey(rc.fKeys.get(i))) throw new NonExistingColumnDefError(rc.fKeys.get(i));
				if(!(colSet.get(rc.fKeys.get(i)).getType().equals(rc.table.columns.get(rc.table.colNum.get(rc.pKeys.get(i))).getType()))) throw new ReferenceTypeError();
				if(!(rc.table.columns.get(rc.table.colNum.get(rc.pKeys.get(i))).isPrimaryKey()))throw new ReferenceNonPrimaryKeyError();
			}
		}
	}
	
	public String getName(){ return name; }
	
	class ReferentialConstraint implements java.io.Serializable {
		ArrayList<String> fKeys, pKeys;
		Table table;
		
		public ReferentialConstraint(ArrayList<String> fKeys, Table table, ArrayList<String> pKeys){
			this.fKeys = fKeys;
			this.table = table;
			this.pKeys = pKeys;
		}
	}
}