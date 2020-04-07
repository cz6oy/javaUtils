package com.kayakwise.gray.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 身份证相关操作工具类
 * 
 * @author leewc
 * @since 2018-05-08 15:11 v1.0
 * @version 1.0
 */
public class IDCardUtils {

	/**
	 * 根据出生日期获取年龄值
	 * 
	 * @param birthday
	 *            出生日期
	 * @param format
	 *            birthday的格式
	 * @return int 年龄
	 * @throws Exception
	 */
	public static int getAge(String birthday, String format) throws Exception {
		if (!DateUtils.formatChecking(birthday, format, false)) {
			throw new ParseException("An illegal date", 0);
		}
		if (DigitUtils.compareTo(birthday, DateUtils.getNowDate()) == 1) { // birthday
																			// >
																			// now
			throw new IllegalArgumentException("The birthday should not be greater than the current date");
		}
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(DateUtils.parse(birthday, format));
		int _year = cal.get(Calendar.YEAR);
		int _month = cal.get(Calendar.MONTH);
		int _day = cal.get(Calendar.DAY_OF_MONTH);
		int age = year - _year;
		if (month < _month) {
			age--;
		} else if (month == _month) {
			if (day < _day) {
				age--;
			}
		}
		return age;
	}

	/**
	 * 18位身份证号码格式校验
	 * 
	 * @param id
	 *            身份证编号
	 * @return 校验通过返回true，否则false
	 * @throws ParseException
	 */
	@Deprecated
	public static boolean checkIdCard(String id) throws ParseException {
		if (id == null || id.isEmpty())
			return false;
		id = id.trim();

		if (id.length() != 18) {
			return false;
		}

		// 前17位全数字校验
		if (!(id.substring(0, 17).matches("^[0-9]*$"))) {
			return false;
		}

		// 前2位地区位验证
		String province = id.substring(0, 2);
		if (!checkProvice(province)) {
			return false;
		}

		// 出生时间验证
		String birthday = id.substring(6, 14);
		DateFormat sdf = DateUtils.getFormat("yyyyMMdd");
		Date date = sdf.parse(birthday);
		String day = sdf.format(date);
		if (!birthday.equals(day)) {
			return false;
		}

		// 最后一位校验位
		String code = id.substring(17);
		checkCode(id.subSequence(0, 17).toString(), code);

		return true;
	}

	@Deprecated
	private static boolean checkProvice(String province) {
		// String [] provinceArray =
		// {"11:北京","12:天津","13:河北","14:山西","15:内蒙古","21:辽宁","22:吉林","23:黑龙江","31:上海","32:江苏","33:浙江","34:安徽",
		// "35:福建","36:江西","37:山东","41:河南","42:湖北
		// ","43:湖南","44:广东","45:广西","46:海南","50:重庆","51:四川","52:贵州","53:云南","54:西藏
		// ","61:陕西","62:甘肃","63:青海","64:宁夏",
		// "65:新疆","71:台湾","81:香港","82:澳门","91:国外"};
		String[] items = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37",
				"41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
				"81", "82", "91" };
		List<String> provinces = Arrays.asList(items);
		if (provinces.contains(province))
			return true;
		return false;
	}

	@Deprecated
	private static boolean checkCode(String str17, String checkCode) {
		char[] char17 = str17.toCharArray();

		// 身份证前17位 每一位权重
		int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

		int sum = 0;
		if (power.length != char17.length) {
			return false;
		}
		// 求和
		for (int i = 0; i < char17.length; i++) {
			int number = Integer.parseInt(String.valueOf(char17[i]));// 身份证每一位
			sum = sum + number * power[i];
		}

		String code = null;
		switch (sum % 11) {
		case 10:
			code = "2";
			break;
		case 9:
			code = "3";
			break;
		case 8:
			code = "4";
			break;
		case 7:
			code = "5";
			break;
		case 6:
			code = "6";
			break;
		case 5:
			code = "7";
			break;
		case 4:
			code = "8";
			break;
		case 3:
			code = "9";
			break;
		case 2:
			code = "X";
			break;
		case 1:
			code = "0";
			break;
		case 0:
			code = "1";
			break;
		}
		if (!code.equalsIgnoreCase(checkCode)) {
			return false;
		}

		return true;
	}

	// public static void main(String[] args) throws Exception {
	// String birthday = "20180209";
	// String format = "yyyyMMdd";
	//
	// int age = getAge(birthday, format);
	// System.out.println(age);
	// }

}
