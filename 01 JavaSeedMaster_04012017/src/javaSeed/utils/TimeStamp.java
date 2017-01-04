package javaSeed.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {
	
    private static DateFormat DateObj = null;
    private static DateFormat TimeObj = null;
	
	public static String ReturnTime(String DateFormat){
		
		DateObj = new SimpleDateFormat(DateFormat); //YYYY-MM-dd);
		//get current date time with Date()
	    Date date = new Date();		
		return DateObj.format(date);
	}
	
	public static String RetunrDate(String TimeFormat){
		
		TimeObj = new SimpleDateFormat(TimeFormat);	//HHmmss);
	    Date date = new Date();
		return TimeObj.format(date);		
	}

}