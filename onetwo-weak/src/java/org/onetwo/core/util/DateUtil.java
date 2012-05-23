package org.onetwo.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	
	public static final String DateOnly= "yyyy-MM-dd";
	public static final String TimeOnly= "HH:mm:ss";
	public static final String DateTime= "yyyy-MM-dd HH:mm:ss";//
	
	private DateUtil(){
	}
	
	public static String formatDateByPattern(Date date, String p){
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		sdf.setTimeZone(TimeZone.getDefault());
		String rs = null;
		try{
			rs = sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public static String formatDateTime(Date date){
		return formatDateByPattern(date, DateTime);
	}
	
	public static String formatDate(Date date){
		return formatDateByPattern(date, DateOnly);
	}
	
	public static String formatTime(Date date){
		return formatDateByPattern(date, TimeOnly);
	}
	
	public static Date parseStrToDateByPattern(String source, String format){
		return parseStrToDateByPattern(source, format, true);
	}
	
	public static Date parseStrToDateByPattern(String source, String format, boolean chs){
		Date rs = null;
		SimpleDateFormat sdf = null;
		if(chs)
			sdf = new SimpleDateFormat(format);
		else{
			sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		try{
			rs = sdf.parse(source);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Date parseDate(String src){
		return parseStrToDateByPattern(src, DateOnly);
	}
	
	public static Date parseTime(String src){
		return parseStrToDateByPattern(src, TimeOnly);
	}
	
	public static Date parseDateTime(String src){
		return parseStrToDateByPattern(src, DateTime);
	}
	
	public static String getCurrentMillisStr(){
		return formatDateByPattern(new Date(), "yyyyMMddHHmmssSSS");
	}
	
	public static void log(String str){
		System.out.println("log: " + str);
	}
	
	public static void main(String[] args){
		log(getCurrentMillisStr());
	}

}
