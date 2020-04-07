package com.kayakwise.gray.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期操作工具类
 *
 */
public class DateUtil {

	public static final SimpleDateFormat SDF_DATETIME_19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat SDF_DATETIME_17 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static final SimpleDateFormat SDF_DATETIME_14 = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat SDF_DATE_10 = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat SDF_DATE_8 = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat SDF_TIME_8 = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat SDF_TIME_6 = new SimpleDateFormat("HHmmss");
	public static final SimpleDateFormat SDF_TIME_9 = new SimpleDateFormat("HHmmssSSS");
	public static final SimpleDateFormat SDF_DATETIME_CHN = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
	public static final SimpleDateFormat SDF_DATETIME_19T = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private static final ThreadLocal<Map<String, DateFormat>> LOCAL_FORMAT = new ThreadLocal<Map<String, DateFormat>>() {
		@Override
		protected Map<String, DateFormat> initialValue() {
			return new HashMap<>();
		}
	};

	public static DateFormat getFormat(String pattern) {
		Map<String, DateFormat> local = LOCAL_FORMAT.get();
		DateFormat format = local.get(pattern);
		if (format == null) {
			format = new SimpleDateFormat(pattern);
			local.put(pattern, format);
		}
		return format;
	}

	private static DateFormat getFormat(SimpleDateFormat format) {
		String pattern = format.toPattern();
		return getFormat(pattern);
	}

	public static void delFormat() {
		LOCAL_FORMAT.remove();
	}

	public static String getDateTime(Date date) {
		return getFormat(SDF_DATETIME_14).format(date);
	}

	public static String getDate(Date date) {
		return getFormat(SDF_DATE_8).format(date);
	}

	public static String getTime(Date date) {
		return getFormat(SDF_TIME_6).format(date);
	}
	public static String getTime9(Date date) {
		return getFormat(SDF_TIME_9).format(date);
	}
	
	public static String getNowDateTime9() {
		return getTime9(new Date());
	}
	

	public static String getNowDateTime() {
		return getDateTime(new Date());
	}

	public static String getNowDate() {
		return getDate(new Date());
	}

	public static String getNowTime() {
		return getTime(new Date());
	}

	/** Date转字符串 */
	public static String getDate(Date date, SimpleDateFormat df) {
		return getFormat(df).format(date);
	}

	/** String转Calendar */
	public static Calendar getCalendar(String date) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(SDF_DATE_8.parse(date));
		return c;
	}

	/** 字符串转Date */
	public static Date getDate(String date, SimpleDateFormat df) throws ParseException {
		return getFormat(df).parse(date);
	}

	/**
	 * 检查日期合法性
	 * 
	 * @param date
	 *            被检查日期字符串
	 * @param format
	 *            指定日期格式
	 * @param lenient
	 *            是否宽松模式
	 * @return boolean
	 */
	public static boolean checkFormat(String date, SimpleDateFormat df, boolean lenient) {
		if (date == null || date.isEmpty()) {
			return false;
		}

		try {
			DateFormat format = getFormat(df);
			format.setLenient(lenient);
			format.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 转换日期字符串为指定的其他格式
	 * 
	 * @param date
	 *            日期字符串
	 * @param pre
	 *            原格式
	 * @param pro
	 *            新格式
	 * @return String
	 * @throws ParseException
	 */
	public static String formatConvert(String date, SimpleDateFormat pre, SimpleDateFormat pro) throws ParseException {
		if (date == null || date.isEmpty()) {
			return null;
		}

		Date preDate = getFormat(pre).parse(date);
		return getFormat(pro).format(preDate);
	}

	/**
	 * 日期加法
	 * 
	 * @param date
	 *            基本日期
	 * @param amount
	 *            增减天数
	 * @param format
	 *            日期格式
	 * @return String
	 * @throws ParseException
	 */
	public static String add(String date, int amount, SimpleDateFormat format) throws ParseException {
		return add(date, amount, format, Calendar.DATE);
	}

	/**
	 * Date类加法
	 * 
	 * @param time
	 *            基本日期
	 * @param amount
	 *            增减天数
	 * @param format
	 *            日期格式
	 * @param calendar
	 *            计算类型（例：Calendar.DATE）
	 * @return
	 * @throws ParseException
	 */
	public static String add(String time, int amount, SimpleDateFormat format, int calendar) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(getDate(time, format));
		c.add(calendar, amount);
		return getDate(c.getTime(), format);
	}

	/**
	 * 获取当前时间是周几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDays = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) w = 0;

		return weekDays[w];
	}

	/**
	 * 计算两个日期之间相差的天数，小时数，分钟数，秒数
	 * 
	 * @param endDate
	 *            结束时间
	 * @param nowDate
	 *            开始时间
	 * @return
	 */
	public static Map<String, Object> getDateDiff(String endDateStr, String beginDateStr, SimpleDateFormat format) throws ParseException {
		Date endDate = DateUtil.getDate(endDateStr, format);
		Date beginDate = DateUtil.getDate(beginDateStr, format);
		HashMap<String, Object> result = new HashMap<>();
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff / nh;
		// 计算差多少分钟
		long min = diff / nm;
		// 计算差多少秒//输出结果
		long sec = diff / ns;
		result.put("days", day);
		result.put("hours", hour);
		result.put("mins", min);
		result.put("secs", sec);
		return result;
	}
}
