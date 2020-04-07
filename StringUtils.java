package com.kayakwise.gray.api.util;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 * 
 * @author leewc
 * @since 2018-05-08 17:20 v1.0
 * @version 1.0
 */
public class StringUtils {
	private static final Pattern VARIABLE_INT = Pattern.compile("^[-\\+]?[\\d]*$");
	private static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

	private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static String ABC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random random = new Random();

	/**
	 * 获取一定长度的随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomStringByLength(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(ALPHABET.length());
			sb.append(ALPHABET.charAt(number));
		}

		return sb.toString();
	}

	public static String getRandomABCByLength(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(ABC.length());
			sb.append(ABC.charAt(number));
		}

		return sb.toString();
	}

	/**
	 * 判断一个字符串是否为全中文
	 * 
	 * @param str
	 *            源字符串
	 * @return boolean
	 */
	public static boolean isChinese(String str) {
		for (int i = 0; i < str.length(); i++) {
			char n = str.charAt(i);
			if (!isChinese(n))
				return false;
		}
		return true;
	}

	/**
	 * 判断一个字符是否为中文
	 * 
	 * @param c
	 *            源字符
	 * @return boolean
	 */
	public static boolean isChinese(char c) {
		if ("GBK".equals(DEFAULT_CHARSET))
			return c >= 0x80 && c <= 0xff;
		else if ("UTF-8".equals(DEFAULT_CHARSET))
			return c >= 0x4e00 || c <= 0x9fa5;
		else
			return false;
	}

	/**
	 * 判断一个字符串是否存在中文字符
	 * 
	 * @param str
	 *            源字符串
	 * @return boolean
	 */
	public static boolean existChinese(String str) {
		for (int i = 0; i < str.length(); i++) {
			char n = str.charAt(i);
			if (isChinese(n))
				return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为全数字
	 * 
	 * @param str
	 *            源字符串
	 * @return boolean
	 */
	public static boolean isInteger(String str) {
		return VARIABLE_INT.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为null或Empty
	 * 
	 * @param str
	 *            源字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty())
			return true;
		return false;
	}

	/**
	 * 判断枚举类型是否为null或值是否为Empty
	 * 
	 * @param arg
	 *            源枚举类型
	 * @return boolean
	 */
	public static boolean isEmpty(Enum<?> arg) {
		if (arg == null || arg.toString().isEmpty())
			return true;
		return false;
	}

	/**
	 * 在目标字符串右侧补充一定字符
	 * <p>
	 * 当源字符串为null时，采用默认字符作为源字符串
	 * </p>
	 * <p>
	 * 当源字符串长度已经超过目标长度时，会返回{@code str.substring(0, length)}
	 * </p>
	 * 
	 * @param str
	 *            源字符串
	 * @param symbol
	 *            补充字符
	 * @param length
	 *            补充后字符串总长度
	 * @param def
	 *            默认字符
	 * @return String 补充后的字符串
	 */
	/*
	 * public static String rightFixedChar(String str, char symbol, int length,
	 * char def) { if (str == null) str = String.valueOf(def); return
	 * rightFixedChar(str, symbol, length); }
	 * 
	 *//**
		 * 在目标字符串右侧补充一定字符
		 * <p>
		 * 当源字符串为null时，直接返回null
		 * </p>
		 * <p>
		 * 当源字符串长度已经超过目标长度时，会返回{@code str.substring(0, length)}
		 * </p>
		 * 
		 * @param str
		 *            源字符串
		 * @param symbol
		 *            补充字符
		 * @param length
		 *            补充后字符串总长度
		 * @return String 补充后的字符串
		 */
	/*
	 * public static String rightFixedChar(String str, char symbol, int length)
	 * { if (str == null) return null; int len = str.length(); if (len <=
	 * length) { String add = getSymbolStr(symbol, length - len); return
	 * String.format("%s%s", str, add); } else { return str.substring(0,
	 * length); } }
	 * 
	 *//**
		 * 在目标字符串左侧补充一定字符
		 * <p>
		 * 当源字符串为null时，采用默认字符作为源字符串
		 * </p>
		 * <p>
		 * 当源字符串长度已经超过目标长度时，会返回{@code str.substring(0, length)}
		 * </p>
		 * 
		 * @param str
		 *            源字符串
		 * @param symbol
		 *            补充字符
		 * @param length
		 *            补充后字符串总长度
		 * @param def
		 *            默认字符
		 * @return String 补充后的字符串
		 */
	/*
	 * public static String leftFixedChar(String str, char symbol, int length,
	 * char def) { if (str == null) str = String.valueOf(def); return
	 * leftFixedChar(str, symbol, length); }
	 * 
	 *//**
		 * 在目标字符串左侧补充一定字符
		 * <p>
		 * 当源字符串为null时，直接返回null
		 * </p>
		 * <p>
		 * 当源字符串长度已经超过目标长度时，会返回{@code str.substring(0, length)}
		 * </p>
		 * 
		 * @param str
		 *            源字符串
		 * @param symbol
		 *            补充字符
		 * @param length
		 *            补充后字符串总长度
		 * @return String 补充后的字符串
		 */
	/*
	 * public static String leftFixedChar(String str, char symbol, int length) {
	 * if (str == null) return null; int len = str.length(); if (len <= length)
	 * { String add = getSymbolStr(symbol, length - len); return
	 * String.format("%s%s", add, str); } else { return str.substring(0,
	 * length); } }
	 * 
	 *//** 获取指定长度的字符 *//*
						 * private static String getSymbolStr(char symbol, int
						 * length) { StringBuffer buffer = new StringBuffer();
						 * for (int i = 0; i < length; i++) {
						 * buffer.append(symbol); } return buffer.toString(); }
						 */

	/***
	 * 判断字符串非空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/***
	 * 判断字符串是否在数组内
	 * 
	 * @param strs
	 * @return
	 */
	public static boolean isInStrings(String src, String... strs) {
		if (strs == null || strs.length < 1) {
			return true;
		}

		for (String str : strs) {
			if (src.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是否为null或Empty
	 * 
	 * @param str
	 *            源字符串
	 * @return 转换后字符串
	 */
	public static String convertEmpty(String str) {
		if (str == null || str.isEmpty())
			return "";
		return str;
	}

	/**
	 * 在目标字符串右侧补充一定字符
	 * <p>
	 * 当源字符串为null时，直接返回null
	 * </p>
	 * <p>
	 * 当源字符串长度已经超过目标长度时，会返回{@code str.substring(0, length)}
	 * </p>
	 * 
	 * @param orgStr
	 *            源字符串
	 * @param aliStr
	 *            补充字符
	 * @param len
	 *            补充后字符串总长度
	 * @return String 补充后的字符串
	 */
	public static String rightFixedChar(String orgStr, char aliStr, int len) {
		if (orgStr == null || orgStr.length() >= len) {
			return orgStr;
		}

		int length = 0;
		StringBuffer res = new StringBuffer();
		StringBuffer bf = new StringBuffer(orgStr);
		for (int i = 0; i < orgStr.length(); i++) {
			int ASCII = Character.codePointAt(orgStr, i);
			if (ASCII >= 0 && ASCII <= 255) {
				length++;
			} else {
				length += 2;
			}

		}
		for (int j = 0; j < len - length; j++) {
			res = bf.append(aliStr);
		}
		return res.toString();

	}

	/**
	 * 在目标字符串右侧补充一定字符
	 * <p>
	 * 当源字符串为null时，直接返回null
	 * </p>
	 * <p>
	 * 当源字符串长度已经超过目标长度时，会返回{@code str.substring(0, length)}
	 * </p>
	 * 
	 * @param orgStr
	 *            源字符串
	 * @param aliStr
	 *            补充字符
	 * @param len
	 *            补充后字符串总长度
	 * @return String 补充后的字符串
	 */
	public static String leftFixedChar(String orgStr, char aliStr, int len) {
		if (orgStr == null || orgStr.length() >= len) {
			return orgStr;
		}

		int length = 0;
		StringBuffer res = new StringBuffer();
		StringBuffer bf = new StringBuffer(orgStr);
		for (int i = 0; i < orgStr.length(); i++) {
			int ASCII = Character.codePointAt(orgStr, i);
			if (ASCII >= 0 && ASCII <= 255) {
				length++;
			} else {
				length += 2;
			}

		}
		for (int j = 0; j < len - length; j++) {
			res = res.append(aliStr);
		}
		return res.toString() + bf;

	}

}
