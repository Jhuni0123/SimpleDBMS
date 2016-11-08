package JnDB;

public class Type{
	String name;
	int len;
	
	public Type(String name, int len){
		this.name = name;
		if(name.equals("char")){
			this.len = len;
		}
	}
}