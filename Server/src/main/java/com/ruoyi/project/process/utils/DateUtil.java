package com.ruoyi.project.process.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final long SENCODS_IN_FIVE_DAYS = 1000 * 24 * 60 * 60 * 5;// 5天换算成毫秒数




	public String getFirstDayOfMonth(int year,int month){
		String monthStr = month < 10 ? "0" + month : String.valueOf(month);
		return year + "-"+monthStr+"-" +"01";
	}

	/**
	 * 判断两个日期相差天数
	 * */
	public static int differentDaysByMillisecond(Date date1,Date date2)
	{
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		return days;
	}

	/**
	 * get the last date of given month and year
	 * @param year
	 * @param month
	 * @return
	 */
	public String getLastDayOfMonth(int year,int month){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR , year);
		calendar.set(Calendar.MONTH , month - 1);
		calendar.set(Calendar.DATE , 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR , -1);
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" +
				calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * get Calendar of given year
	 * @param year
	 * @return
	 */
	private Calendar getCalendarFormYear(int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.YEAR, year);
		return cal;
	}



	/**
	 * get start date of given week no of a year
	 * @param year
	 * @param weekNo
	 * @return
	 */
	public String getStartDayOfWeekNo(int year,int weekNo){
		Calendar cal = getCalendarFormYear(year);
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);
		return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
				cal.get(Calendar.DAY_OF_MONTH);

	}

	/**
	 * get the end day of given week no of a year.
	 * @param year
	 * @param weekNo
	 * @return
	 */
	public String getEndDayOfWeekNo(int year,int weekNo){
		Calendar cal = getCalendarFormYear(year);
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
				cal.get(Calendar.DAY_OF_MONTH);
	}





	/**
	 * 根据日期字符串判断当月第几周
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static int getWeek(String str) throws Exception{
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date date =sdf.parse(str);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//第几周
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		//第几天，从周日开始
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return week;
	}


	/**
	 * 判断当前日期是星期几
	 *
	 * @param pTime 修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if(c.get(Calendar.DAY_OF_WEEK) == 1){
			dayForWeek = 7;
		}else{
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/***
	 * 判断当前时间是星期几
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int dayForWeek1(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if(c.get(Calendar.DAY_OF_WEEK) == 1){
			dayForWeek = 7;
		}else{
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}


	/**
	 * 判断当前时间是上午还是下午
	 * */
	public static String timeForAMPM(String pTime) throws Exception{
		String startType= "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date st =sdf.parse(pTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(st);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		System.out.println("小时："+hour);
		if (hour>=0&&hour<=12){
			startType="上午";
		}else{
			startType="下午";
		}

		return startType;
	}

	/**
	 * 判断时间上下午
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public static String timeForAMPM1(Date time) throws Exception{
		System.out.println("判断上下午时间："+time);
		String startType= "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour>=0&&hour<=12){
			startType="上午";
		}else{
			startType="下午";
		}
		return startType;
	}


	/**
	 * 功能或作用：格式化日期时间
	 * 
	 * @param DateValue
	 *            输入日期或时间
	 * @param DateType
	 *            格式化 EEEE是星期, yyyy是年, MM是月, dd是日, HH是小时, mm是分钟, ss是秒
	 * @return 输出字符串
	 */
	public static String formatDate(String DateValue, String DateType) {
		String result;
		SimpleDateFormat formatter = new SimpleDateFormat(DateType);
		try {
			Date mDateTime = formatter.parse(DateValue);
			result = formatter.format(mDateTime);
		} catch (Exception ex) {
			result = ex.getMessage();
		}
		if (result.equalsIgnoreCase("1900-01-01")) {
			result = "";
		}
		return result;
	}

	public static String getDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mDateTime = formatter.format(cal.getTime());
		return (mDateTime);
	}

	public static String formatDateTime(Date d) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mDateTime = formatter.format(d);

		return (mDateTime);
	}

	public static Timestamp getDate() {
		Calendar cal = Calendar.getInstance();
		long timeMillis = cal.getTimeInMillis();
		return new Timestamp(timeMillis);
	}

	public static String formatSecond(Date d) {
		SimpleDateFormat f2 = new SimpleDateFormat("ss");
		return f2.format(d);
	}

	public static String formatDate(Date d) {
		SimpleDateFormat f2 = new SimpleDateFormat("yyyyMMddHHmm");
		return f2.format(d);
	}

	public static boolean isInSomeDays(Date date) {
		Date dateNow = new Date();
		// 获得两个时间的毫秒时间差异
		long diff = dateNow.getTime() - date.getTime();
		if (diff < 0) {
			diff = -diff;
		}
		return SENCODS_IN_FIVE_DAYS > diff;// 5天内true

	}
	/**
	 *
	 * 字符串转换为对应日期
	 *
	 * @param source
	 * @return
	 */
	public static Date stringToDate(String source) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sf.parse(source);
		} catch (Exception e) {
		}
		return date;
	}
	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {// 可以用new
																		// Date().toLocalString()传递参数
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}


}
