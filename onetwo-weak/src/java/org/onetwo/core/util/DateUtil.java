package org.onetwo.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间通用方法
 * @author cg
 *
 */
@SuppressWarnings({"serial","deprecation"})
abstract public class DateUtil {

	public static enum DateType{
		
		year(Calendar.YEAR),
		month(Calendar.MONTH),
		date(Calendar.DATE),
		hour(Calendar.HOUR_OF_DAY),
		min(Calendar.MINUTE),
		sec(Calendar.SECOND),
		misec(Calendar.MILLISECOND);
		
		private int field;
		
		DateType(int val){
			this.field = val;
		}

		public int getField() {
			return field;
		}
		
	}


	public static final String Date_Only= "yyyy-MM-dd";
	public static final String Date_Time= "yyyy-MM-dd HH:mm:ss";
	public static final String Time_Only= "HH:mm:ss";
	public static final String DateOnly= "yyyyMMdd";
	public static final String DateTime= "yyyyMMddHHmmss";
	public static final String TimeOnly= "HHmmss";

	public static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat(Date_Only);
	public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS= new SimpleDateFormat(Date_Time);
	public static final SimpleDateFormat HH_MM_SS= new SimpleDateFormat(Time_Only);
	public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat(DateOnly);
	public static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat(DateTime);
	public static final SimpleDateFormat HHMMSS = new SimpleDateFormat(TimeOnly);
	
	public static final SimpleDateFormat YYYYMMDDHHMMSS_SSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static final SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");
	public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS_SSS= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	public static final SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static final int UNIT_SECOND = 1000;
	public static final int UNIT_MINUTE = 60 * 1000;
	public static final int UNIT_HOUR = 60 * 60 * 1000;
	
	public static final Map<String, SimpleDateFormat> values = new HashMap<String, SimpleDateFormat>(){
		{
			put(Date_Only, YYYY_MM_DD);
			put(Date_Time, YYYY_MM_DD_HH_MM_SS);
			put(Time_Only, HH_MM_SS);
			put(DateOnly, YYYYMMDD);
			put(DateTime, YYYYMMDDHHMMSS);
			put(TimeOnly, HHMMSS);
		}
	};
	
	/**
	 * 获取跟date间隔days天的时间
	 * @param date 时间
	 * @param days 间隔天数
	 */
	public static Date getDiffDay(Date date,long days){
		long datetime=date.getTime();
		long difftime=days*24*60*60*1000;
		
		return new Date(datetime+difftime);
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
		return formatDateByPattern(date, Date_Time);
	}
	
	public static String format(String pattern, Date date){
		if(StringUtils.isBlank(pattern))
			pattern = Date_Time;
		SimpleDateFormat sdf = getDateFormat(pattern);
		return format(sdf, date);
	}
	
	public static String format(SimpleDateFormat format, Date date){
		return format.format(date);
	}
	
	public static Date parse(SimpleDateFormat format, String dateStr){
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}
	
	public static Date parse(String dateStr, SimpleDateFormat...dateFormats){
		if(dateFormats==null || dateFormats.length==0)
			return parse(YYYY_MM_DD_HH_MM_SS, dateStr);
		Date date = null;
		for(SimpleDateFormat sdf : dateFormats){
			date = parse(sdf, dateStr);
			if(date!=null)
				return date;
		}
		return date;
	}
	
	public static Date parse(String dateStr, String...patterns){
		if(patterns==null || patterns.length==0)
			return parse(YYYY_MM_DD_HH_MM_SS, dateStr);
		Date date = null;
		for(String p : patterns){
			SimpleDateFormat sdf = getDateFormat(p);
			date = parse(sdf, dateStr);
			if(date!=null)
				return date;
		}
		return date;
	}
	
	public static Date parse(String dateStr){
		return parse(dateStr, YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD_HH_MM, YYYY_MM_DD);
	}
	
	public static SimpleDateFormat getDateFormat(String p){
		if(StringUtils.isBlank(p))
			p = DateOnly;
		SimpleDateFormat sdf = values.get(p);
		if(sdf==null){
			sdf = new SimpleDateFormat(p);
			values.put(p, sdf);
		}
		return sdf;
	}
	
    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }
	
    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
	
	public static Date setDateStart(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return setDateStart(calendar);
	}
	
	public static Date setDateEnd(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return setDateEnd(calendar);
	}

	
	public static Date setDateStart(Calendar calendar){
		return start(calendar, DateType.date);
	}
	
	public static Date start(Calendar calendar, DateType dt){
		/*calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);*/
		
		if(dt==null)
			return calendar.getTime();
		
		for(DateType d : DateType.values()){
			if(d.getField()<=dt.getField())
				continue;
			calendar.set(d.getField(), calendar.getActualMinimum(d.getField()));
		}
		return calendar.getTime();
	}
	
	public static void setDateStart(Calendar calendar, int... fields){
		if(fields==null)
			return ;
		for(int field : fields){
			calendar.set(field, calendar.getActualMinimum(field));
		}
	}
	
	public static void setDateEnd(Calendar calendar, int... fields){
		if(fields==null)
			return ;
		for(int field : fields){
			calendar.set(field, calendar.getActualMaximum(field));
		}
	}
	
	public static Date setDateEnd(Calendar calendar){
		return end(calendar, DateType.date);
	}
	
	public static Date end(Calendar calendar, DateType dt){
		/*calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);*/

		if(dt==null)
			return calendar.getTime();
		
		for(DateType d : DateType.values()){
			if(d.getField()<=dt.getField())
				continue;
			calendar.set(d.getField(), calendar.getMaximum(d.getField()));
		}
		return calendar.getTime();
	}
	
	public static Date addDay(Calendar calendar, int numb){
		calendar.add(Calendar.DAY_OF_MONTH, numb);
		return calendar.getTime();
	}
	
	public static Date addDay(Date date, int numb){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		increaseDay(calendar, numb);
		return calendar.getTime();
	}
	
	public static void increaseDay(Calendar calendar, int numb){
		calendar.add(Calendar.DAY_OF_MONTH, numb);
	}
	
	public static long secondToMicroSecond(long microSecond){
		return microSecond * UNIT_SECOND;
	}
	
	public static long minuteToMicroSecond(long microSecond){
		return microSecond * UNIT_MINUTE;
	}
	
	public static long hourToMicroSecond(long microSecond){
		return microSecond * UNIT_HOUR;
	}
	
	public static Date now(){
		return new Date();
	}
	
	public static String nowString(){
		return format(YYYY_MM_DD_HH_MM_SS_SSS, now());
	}
	
	public static String getString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}
	
	public static String getString(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String getString(String format){
		return getString(new Date(), format);
	}
	
	public static String formatDate(Date date){
		return formatDateByPattern(date, DateOnly);
	}
	
	public static String formatTime(Date date){
		return formatDateByPattern(date, TimeOnly);
	}
	
	
	public static void main(String[] args){
		Date date = null;
		date = parse("2010-10-10", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
		System.out.println(date.toLocaleString());
		date = parse("2010-12-10", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
		System.out.println(date.toLocaleString());
	}
}
