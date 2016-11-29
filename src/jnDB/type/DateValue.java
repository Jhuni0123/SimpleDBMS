package jnDB.type;

import jnDB.exception.WhereIncomparableError;

public class DateValue extends Value {
	public int year, month, day;
	
	public DateValue(String v){
		String[] arr = v.split("-", 3);
		year = Integer.parseInt(arr[0]);
		month = Integer.parseInt(arr[1]);
		day = Integer.parseInt(arr[2]);
	}
	
	public boolean equals(Object obj){
		if(obj instanceof DateValue){
			DateValue date = (DateValue)obj;
			return year == date.year && month == date.month && day == date.day;
		}
		return false;
	}
	
	public int hashCode(){
		return 31*(31*(31*year + month) + day);
	}

	@Override
	public int compareTo(Value rv) {
		if(rv instanceof DateValue){
			DateValue dv = (DateValue)rv;
			if(year == dv.year){
				if(month == dv.month){
					return day - dv.day;
				}
				return month - dv.month;
			}
			return year - dv.year;
		}
		throw new WhereIncomparableError();
	}
	
	public String toString(){
		return Integer.toString(year, 4) + "-" + Integer.toString(month,2) + "-" + Integer.toString(day,2);
	}

	@Override
	public boolean castTo(Type t) {
		if(t instanceof DateType){
			return true;
		}
		return false;
	}
}
