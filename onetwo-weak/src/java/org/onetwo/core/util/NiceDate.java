package org.onetwo.core.util;

import java.util.Calendar;
import java.util.Date;

import org.onetwo.core.util.DateUtil.DateType;

public class NiceDate {
	
	public static NiceDate New(){
		return new NiceDate();
	}
	
	public static enum DatePhase {
		morning(6, 12),
		noon(12, 14),
		afternoon(14, 18),
		night(18, 24),
		deepnight(0, 6);
		
		private int start;
		private int end;
		
		/*****
		 * 
		 * @param start include
		 * @param end exclude
		 */
		private DatePhase(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean inPhase(int hour){
			if(hour>=start && hour<end)
				return true;
			return false;
		}
	}
	
	private Calendar calendar;
	
	private DateType dateType;
	
	public NiceDate(){
		calendar = Calendar.getInstance();
	}
	
	public NiceDate(Date date){
		calendar.setTime(date);
	}

	protected void setDateType(DateType dateType) {
		this.dateType = dateType;
		start();
	}

	/****
	 * @deprecated
	 * @return
	 */
	public Date getDate() {
		return calendar.getTime();
	}

	/****
	 * @deprecated
	 * @return
	 */
	public void setDate(Date date) {
		calendar.setTime(date);
	}

	public Date getTime() {
		return calendar.getTime();
	}

	public void setTime(Date time) {
		calendar.setTime(time);
	}
	
	public NiceDate now(){
		return this;
	}
	
	public NiceDate today(){
		DateUtil.start(calendar, DateType.date);
		return this;
	}
	
	public NiceDate yesterday(){
		return increaseDay(-1);
	}
	
	public NiceDate increaseDay(int numb){
		DateUtil.increaseDay(calendar, numb);
		return this;
	}
	
	public NiceDate tomorrow(){
		return increaseDay(1);
	}
	
	public NiceDate thisYear(){
		setDateType(DateType.year);
		return this;
	}
	
	public NiceDate thisMonth(){
		setDateType(DateType.month);
		return this;
	}
	
	public NiceDate thisHour(){
		setDateType(DateType.hour);
		return this;
	}
	
	public NiceDate thisMin(){
		setDateType(DateType.min);
		return this;
	}
	
	public NiceDate thisSec(){
		setDateType(DateType.sec);
		return this;
	}
	
	public NiceDate thisMisec(){
		setDateType(DateType.misec);
		return this;
	}
	
	public NiceDate start(){
		DateUtil.start(calendar, dateType);
		return this;
	}
	
	public NiceDate end(){
		DateUtil.end(calendar, dateType);
		return this;
	}
	
	public DatePhase getDatePhase(){
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		for(DatePhase p : DatePhase.values()){
			if(p.inPhase(hour))
				return p;
		}
		throw new UnsupportedOperationException("error hour: " +  hour);
	}
	
	public boolean isMorning(){
		return DatePhase.morning == getDatePhase();
	}
	
	public boolean isNoon(){
		return DatePhase.noon == getDatePhase();
	}
	
	public boolean isAfternoon(){
		return DatePhase.afternoon == getDatePhase();
	}
	
	public boolean isNight(){
		return DatePhase.night == getDatePhase();
	}
	
	public boolean isDeepnight(){
		return DatePhase.deepnight == getDatePhase();
	}
	
	public String toString(){
		return DateUtil.formatDateTime(calendar.getTime());
	}
	
	public String format(String format){
		return DateUtil.format(format, calendar.getTime());
	}
	
	public static void main(String[] args){
		NiceDate date = new NiceDate();
		System.out.println(date.getDatePhase());
	}

}
