package jnDB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import jnDB.exception.*;
import jnDB.type.Type;

public class TableSchema {
	private String name;
	ArrayList<Column> columns;
	ArrayList<ArrayList<String>> primaryKey;
	ArrayList<ReferentialConstraint> rcList;
	
	public TableSchema(String name){
		this.name = name;
		columns = new ArrayList<Column>();
		primaryKey = new ArrayList<ArrayList<String>>();
		rcList = new ArrayList<ReferentialConstraint>();
	}
	
	public void addColumn(String cName, Type t, boolean isNotNull) {
		columns.add(new Column(name, cName, t, isNotNull));
	}
	
	public void setPrimaryKey(ArrayList<String> cnList) {
		primaryKey.add(cnList);
	}
	
	public void addReferentialKey(ArrayList<String> cnList, String tableName, ArrayList<String> targetList){
		rcList.add(new ReferentialConstraint(cnList,tableName,targetList));
	}
	
	public static boolean isUniqueList(ArrayList<String> list){
		HashSet<String> s = new HashSet<String>();
		for(String str : list){
			if(s.contains(str))return false;
			s.add(str);
		}
		return true;
	}
	
	public void checkValidity(JnDatabase jndb){
		HashMap<String,Column> colSet = new HashMap<String,Column>();
		for(Column col : columns){
			if(colSet.containsKey(col.getName())) throw new DuplicateColumnDefError();
			col.getType().check();
			colSet.put(col.getName(), col);
		}
		if(primaryKey.size() > 1){ throw new DuplicatePrimaryKeyDefError(); }
		HashSet<String> priSet = new HashSet<String>();
		
		if(primaryKey.isEmpty())primaryKey.add(new ArrayList<String>());
		
		for(String cName : primaryKey.get(0)){
			if(!colSet.containsKey(cName)) throw new NonExistingColumnDefError(cName);
			if(priSet.contains(cName)) throw new DuplicateColumnAppearError();
			priSet.add(cName);
		}
		
		for(ReferentialConstraint rc : rcList){
			Table table = jndb.checkPrimaryKey(rc.tableName, rc.pKeys);
			if(rc.fKeys.size() != rc.pKeys.size()) throw new ReferenceTypeError();
			if((!isUniqueList(rc.fKeys))||(!isUniqueList(rc.pKeys))) throw new DuplicateColumnAppearError();
			int l = rc.fKeys.size();
			for(int i=0;i<l;i++){
				if(!colSet.containsKey(rc.fKeys.get(i))) throw new NonExistingColumnDefError(rc.fKeys.get(i));
				if(!(colSet.get(rc.fKeys.get(i)).getType().equals(table.columns.get(table.colNum.get(rc.pKeys.get(i))).getType()))) throw new ReferenceTypeError();
				if(!(table.columns.get(table.colNum.get(rc.pKeys.get(i))).isPrimaryKey()))throw new ReferenceNonPrimaryKeyError();
			}
		}
	}
	
	public String getName(){ return name; }
	
	class ReferentialConstraint implements java.io.Serializable {
		public ArrayList<String> fKeys, pKeys;
		public String tableName;
		
		public ReferentialConstraint(ArrayList<String> fKeys, String tableName, ArrayList<String> pKeys){
			this.fKeys = fKeys;
			this.tableName = tableName;
			this.pKeys = pKeys;
		}
	}
}