package jnDB;

public class Table implements java.io.Serializable {
	private String name;
	public Table(TableSchema schema){
		this.name = schema.getName();
	}
	
	public String getName(){ return name; }
}
