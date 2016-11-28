package jnDB.type;

public class DateValue extends Value {
	public int year, month, day;
	
	public DateValue(String v){
		String[] arr = v.split("-", 3);
		year = Integer.parseInt(arr[0]);
		month = Integer.parseInt(arr[1]);
		day = Integer.parseInt(arr[2]);
	}
}
