package com.kayakwise.gray.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金额操作相关工具类
 * 
 * @author yoke7
 *
 */
public class MoneyUtils {
	private static final Pattern CURRY = Pattern.compile("^[a-zA-Z]+");

	/**
	 * 从包含币种信息的金额字段值中分离币种信息
	 * 
	 * @param amount
	 *            包含币种信息的金额字段值
	 * @return String 币种信息
	 */
	public static String splitCurry(String amount) {
		Matcher m = CURRY.matcher(amount);
		String curry = "";
		if (m.find()) {
			curry = m.group(0);
		}
		return curry;
	}

}
