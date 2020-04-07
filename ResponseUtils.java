package com.kayakwise.gray.api.util;


import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * @category 响应报文值转换工具类
 * @author zhangwei04
 * @date 2019年4月25日
 */
public class ResponseUtils {
	
	/**
	 * 赎回状态转换
	 * 
	 * @param status
	 * @return
	 */
	public static String convertStatus(String status) {
		String combstatus = "0"; // 0-确认中
		if (StringUtils.isEmpty(status)) {
			return combstatus;
		}
		switch (status) {
		case "S":// S:成功
			combstatus = "1";// 1-成功
			break;
		// F:失败
		case "F":// F:失败
		case "C":// C:已撤单
			combstatus = "2";// 2-失败
			break;
		case "U":// U:未完成
		case "E":// E:异常
		case "X":// X:未知
		default:
			break;
		}
		return combstatus;
	}
	
	/**
	 * 赎回状态转换
	 * // 本地状态   U:未完成、S:成功、F:失败、C:已撤单、E:异常X:未知
		//基金状态     01-申请成功，98撤单成功，09-申请失败 
	 * @param status
	 * @return
	 */
	public static String convertFundStatus(String status) {
		String combstatus = ""; // 基金状态     01-申请成功，98撤单成功，09-申请失败 
		if (StringUtils.isEmpty(status)) {
			return combstatus;
		}
		switch (status) {
		case "01":// 
			combstatus = "U";// 1-成功
			break;
		case "98"://  
			combstatus = "C";// 2-失败
			break;
		case "09"://  
			combstatus = "F";// 2-失败
			break;
		default:
			break;
		}
		return combstatus;
	}

	/**
	 * Y或者N转换为1或者0
	 * @param value
	 * @return
	 */
	public static String convertYN(String value) {
		// 0-是 1-否
		if ("Y".equals(value)) {
			value = "0";
		} else {
			value = "1";
		}
		return value;
	}

	/**
	 * BigDecimal保留几位小数工具类
	 * @param value
	 * @param newScale 位数
	 * @return
	 */
	public static String convertDecimal(BigDecimal value, int newScale) {
		return value.setScale(newScale, BigDecimal.ROUND_DOWN).toString();
	}
	
	/**
	 * String保留几位小数工具类
	 * @param value
	 * @param newScale 位数
	 * @return
	 */
	public static String convertDecimal(String value, int newScale) {
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		return convertDecimal(new BigDecimal(value),newScale);
	}
}
