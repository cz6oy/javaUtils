package com.kayakwise.gray.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期时间操作工具类
 * 
 * @author leewc
 * @since 2018-04-15 12:12 v1.0
 * @version 1.0
 */
public class DateUtils {
	
	public static final SimpleDateFormat SDF_DATE_8 = new SimpleDateFormat("yyyyMMdd");
	
	private static final ThreadLocal<Map<String, DateFormat>> LOCAL_FORMAT = new ThreadLocal<Map<String, DateFormat>>() {
		@Override
		protected Map<String, DateFormat> initialValue() {
			return new HashMap<>();
		}
	};

	/**
	 * 获取一个DateFormat对象
	 * 
	 * @param pattern
	 *            时间格式
	 * @return 返回java.text.DateFormat对象
	 */
	public static DateFormat getFormat(String pattern) {
		Map<String, DateFormat> local = LOCAL_FORMAT.get();
		DateFormat format = local.get(pattern);
		if (format == null) {
			format = new SimpleDateFormat(pattern);
			local.put(pattern, format);
		}
		return format;
	}

	/**
	 * 主动删除DateFormat对象，一般情况下不会需要
	 */
	public static void delFormat() {
		LOCAL_FORMAT.remove();
	}

	/**
	 * 获取yyyyMMddHHmmss格式的当前时间的时间戳字符串
	 * 
	 * @return 14位当前时间时间戳
	 */
	public static String getTimestamp14() {
		return getFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * 获取yyyyMMddHHmmssSSS格式的当前时间的时间戳字符串
	 * 
	 * @return 17位当前时间时间戳
	 */
	public static String getTimestamp17() {
		return getFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	/**
	 * 获取yyyy-MM-dd HH:mm:ss格式的当前时间的时间戳字符串
	 * 
	 * @return 19位当前时间时间戳
	 */
	public static String getTimestamp19() {
		return getFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获取yyyyMMdd格式的当前时间的日期字符串
	 * 
	 * @return 当前日期
	 */
	public static String getNowDate() {
		return getFormat("yyyyMMdd").format(new Date());
	}
	
	/**
	 * 获取yyyy-MM-dd格式的当前时间的日期字符串
	 * 
	 * @return 当前日期
	 */
	public static String getNowDate10() {
		return getFormat("yyyy-MM-dd").format(new Date());
	}
	
	/**
	 * 获取yyyyMMdd格式的当前时间的日期字符串
	 * 
	 * @return 返回前一天日期
	 */
	public static String getBeforeDate(){
		SimpleDateFormat predf = new SimpleDateFormat("yyyyMMdd");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
	}

	/**
	 * 获取HHmmss格式的当前时间的时间字符串
	 * 
	 * @return 当前时间
	 */
	public static String getNowTime() {
		return getFormat("HHmmss").format(new Date());
	}

	/**
	 * 获取指定格式的日期/时间字符串
	 * 
	 * @param date
	 *            时间对象
	 * @param pattern
	 *            格式
	 * @return 返回格式化后的日期字符串
	 */
	public static String format(Date date, String pattern) {
		return getFormat(pattern).format(date);
	}

	/**
	 * 获取指定格式的Date对象
	 * 
	 * @param date
	 *            时间对象
	 * @param pattern
	 *            格式
	 * @return 返回java.util.Date对象
	 * @throws ParseException
	 */
	public static Date parse(String date, String pattern) throws ParseException {
		return getFormat(pattern).parse(date);
	}
	
	
	public static String getDateddMMyy(String date) throws ParseException{
		SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyyy");
		Date parse = SDF_DATE_8.parse(date);
		String format = sdf1.format(parse);
		return format;
	}

	/**
	 * 检查日期合法性
	 * 
	 * @param date
	 *            被检查日期字符串
	 * @param pattern
	 *            日期字符串格式
	 * @param lenient
	 *            是否宽松模式
	 * @return 检查合法返回true，否则false
	 */
	public static boolean formatChecking(String date, String pattern, boolean lenient) {
		if (date == null || date.isEmpty()) {
			return false;
		}
		try {
			DateFormat format = getFormat(pattern);
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
	 * @return String 返回转换后的日期
	 * @throws ParseException
	 */
	public static String formatConvert(String date, String pre, String pro) throws ParseException {
		if (date == null || date.isEmpty()) {
			return null;
		}
		Date _date = parse(date, pre);
		return format(_date, pro);
	}

	/**
	 * 字符串日期转为Calendar
	 * 
	 * @param date
	 *            时间对象
	 * @param pattern
	 *            时间格式
	 * @return 返回java.util.Calendar对象
	 * @throws ParseException
	 */
	public static Calendar getCalendar(String date, String pattern) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(parse(date, pattern));
		return c;
	}

	/**
	 * 日期计算（加减）
	 * 
	 * @param date
	 *            基本日期
	 * @param amount
	 *            增减天数
	 * @param pattern
	 *            日期格式
	 * @return 返回计算之后的日期字符串
	 * @throws ParseException
	 */
	public static String add(String date, String pattern, int amount) throws ParseException {
		return calculation(date, pattern, amount, Calendar.DATE);
	}

	/**
	 * java.util.Date类时间计算方法
	 * 
	 * @param time
	 *            基日期/时间
	 * @param pattern
	 *            time的格式
	 * @param amount
	 *            增减量
	 * @param calendar
	 *            计算类型（例：Calendar.DATE）
	 * @return 返回计算之后的日期字符串
	 * @throws ParseException
	 */
	public static String calculation(String time, String pattern, int amount, int calendar) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(parse(time, pattern));
		c.add(calendar, amount);
		return format(c.getTime(), pattern);
	}

	// public static void main(String[] args) throws ParseException {
	// String time = "240000";
	// int amount = 10;
	// String arg = calculation(time, "HHmmss", amount, Calendar.MINUTE);
	// System.out.println(arg);
	// }
	
	public static String getWholeTime(Date date) {
		DateFormat df = new SimpleDateFormat("HHmmssSSS");
		return df.format(date);
	}
	
	/**
	 * 计算两个日期相差的秒数
	 * @param str1		日期1
	 * @param str2		日期2
	 * @param pattern	日期格式
	 * @return
	 */
	public static String getDistanceTime(String str1, String str2,String pattern) {  
        DateFormat df = new SimpleDateFormat(pattern);  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long diffMin = 0;
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            diffMin = (day * 24 * 60) +  hour * 60 + min;
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return String.valueOf(diffMin);
    } 
	
	/**
	 * 相差后日期
	 * @param diffModth 相差月数
	 * @param pattern 格式化类型
	 * @return
	 * @throws ParseException 
	 */
	public static String getDiffDate(int diffModth,String datetime, String pattern,String type) throws ParseException {
		Calendar c = Calendar.getInstance();//获得一个日历的实例   
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);   
        Date date = sdf.parse(datetime);     
        c.setTime(date);
        if (type.equals("sub")) {
			c.add(Calendar.MONTH, - diffModth);
		}else if (type.equals("add")) {
			c.add(Calendar.MONTH, + diffModth);
		}		
        String strDate = sdf.format(c.getTime());
        return strDate;
	}
	
	/**
	 * 判断日期相差月份
	 * @param start 开始日期
	 * @param end	结束日期
	 * @return
	 * @throws ParseException 
	 */
	public static int getMonth(String startDate, String endDate,String pattern) throws ParseException {
		//设置日期格式
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date start = df.parse(startDate);
        Date end = df.parse(endDate);
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }
	
	/**
	 * 校验日期
	 * @param startDate
	 * @param endDate
	 * @param pattern
	 * @param modth
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkDate(String startDate,String endDate,String pattern,int month) throws ParseException {
		boolean result = false;
		Calendar c = Calendar.getInstance();
		//获得一个日历的实例
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		//初始日期;
		Date date = sdf.parse(startDate);
		//设置日历时间
		c.setTime(date);
		//在日历的月份上增加6个月
		c.add(Calendar.MONTH,month);
		//得到N个月后的日期
		String date2 = sdf.format(c.getTime());
		//判断结束日期是否大于n个月后的日期
		if (Integer.parseInt(endDate) > Integer.parseInt(date2)) {
			return result;
		}
		return true;
    }
	
	/** 字符串转Date */
	public static Date getDate(String date, SimpleDateFormat df) throws ParseException {
		return getFormat(df).parse(date);
	}
	
	private static DateFormat getFormat(SimpleDateFormat format) {
		String pattern = format.toPattern();
		return getFormat(pattern);
	}
	
	/* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s,String pattern){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    //比较两个日期大小  
    public static int compare_date(String DATE1, String DATE2,String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
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
	public static long getDateDiff(String endDateStr, String beginDateStr, SimpleDateFormat format) throws ParseException {
		Date endDate = DateUtil.getDate(endDateStr, format);
		Date beginDate = DateUtil.getDate(beginDateStr, format);
		long nd = 1000 * 24 * 60 * 60;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		return day;
	}
	
	
	public static String getVertical(String date) throws ParseException{
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = SDF_DATE_8.parse(date);
		String format = sdf1.format(parse);
		return format;
	}
	
}
