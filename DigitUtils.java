package com.kayakwise.gray.api.util;

import java.math.BigDecimal;

/**
 * 数字计算工具类
 * 
 * @author leewc
 * @since 2018-05-08 15:10 v1.0
 * @version 1.0
 */
public class DigitUtils {

	/**
	 * 金额计算，乘法，保留所有小数位，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被乘数
	 * @param var2
	 *            乘数
	 * @return String 相乘后的字符串值
	 */
	public static String multiply(String var1, String var2) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return big1.multiply(big2).toPlainString();
	}

	/**
	 * 金额计算，乘法，保留所有小数位，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被乘数
	 * @param var2
	 *            乘数
	 * @return String 相乘后的字符串值
	 */
	public static String multiply(BigDecimal var1, BigDecimal var2) {
		return var1.multiply(var2).toPlainString();
	}

	/**
	 * 金额计算，乘法，保留dot位小数，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被乘数
	 * @param var2
	 *            乘数
	 * @param dot
	 *            保留的小数位位数
	 * @return String 相乘后的字符串值
	 */
	public static String multiply(String var1, String var2, int dot) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return multiply(big1, big2, dot);
	}

	/**
	 * 金额计算，乘法，保留dot位小数，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被乘数
	 * @param var2
	 *            乘数
	 * @param dot
	 *            保留的小数位位数
	 * @return String 相乘后的字符串值
	 */
	public static String multiply(BigDecimal var1, BigDecimal var2, int dot) {
		BigDecimal big = var1.multiply(var2).setScale(dot, BigDecimal.ROUND_DOWN);
		return big.toPlainString();
	}

	/**
	 * 金额计算，除法，四舍五入，默认保留2位小数，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被除数
	 * @param var2
	 *            除数
	 * @return String 相除之后的结果
	 */
	public static String divide(String var1, String var2) {
		return divide(var1, var2, 2);
	}

	/**
	 * 金额计算，除法，四舍五入，默认保留2位小数，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被除数
	 * @param var2
	 *            除数
	 * @return String 相除之后的结果
	 */
	public static String divide(BigDecimal var1, BigDecimal var2) {
		return divide(var1, var2, 2);
	}

	/**
	 * 金额计算，除法，四舍五入，默认保留dot位小数，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被除数
	 * @param var2
	 *            除数
	 * @param dot
	 *            保留的小数位位数
	 * @return String 相除之后的结果
	 */
	public static String divide(String var1, String var2, int dot) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return divide(big1, big2, dot);
	}

	/**
	 * 金额计算，除法，四舍五入，默认保留dot位小数，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被除数
	 * @param var2
	 *            除数
	 * @param dot
	 *            保留的小数位位数
	 * @return String 相除之后的结果
	 */
	public static String divide(BigDecimal var1, BigDecimal var2, int dot) {
		BigDecimal big = var1.divide(var2, dot, BigDecimal.ROUND_HALF_UP);
		return big.toPlainString();
	}

	/**
	 * 金额计算，取模，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被模值
	 * @param var2
	 *            模值
	 * @return String 取模的字符串结果
	 */
	public static String remainder(String var1, String var2) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return big1.remainder(big2).toPlainString();
	}

	/**
	 * 金额计算，取模，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被模值
	 * @param var2
	 *            模值
	 * @return String 取模的字符串结果
	 */
	public static String remainder(BigDecimal var1, BigDecimal var2) {
		return var1.remainder(var2).toPlainString();
	}

	/**
	 * 金额计算，加法，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被加数
	 * @param var2
	 *            加数
	 * @return String 相加后的字符串值
	 */
	public static String add(String var1, String var2) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return add(big1, big2);
	}

	/**
	 * 金额计算，加法，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被加数
	 * @param var2
	 *            加数
	 * @return String 相加后的字符串值
	 */
	public static String add(BigDecimal var1, BigDecimal var2) {
		return var1.add(var2).toPlainString();
	}

	/**
	 * 金额计算，减法，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被减数
	 * @param var2
	 *            减数
	 * @return String 相减后的字符串值
	 */
	public static String sub(String var1, String var2) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return sub(big1, big2);
	}

	/**
	 * 金额计算，减法，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被减数
	 * @param var2
	 *            减数
	 * @return String 相减后的字符串值
	 */
	public static String sub(BigDecimal var1, BigDecimal var2) {
		return var1.subtract(var2).toPlainString();
	}

	/**
	 * 金额四舍五入，默认保留2位小数，入参为null可能造成空指针异常
	 * 
	 * @param var
	 *            待取舍值
	 * @return String 取舍后的值
	 */
	public static String scale(String var) {
		return scale(var, 2);
	}

	/**
	 * 金额四舍五入，默认保留2位小数，入参为null可能造成空指针异常
	 * 
	 * @param var
	 *            待取舍值
	 * @return String 取舍后的值
	 */
	public static String scale(BigDecimal var) {
		return scale(var, 2);
	}

	/**
	 * 金额四舍五入，保留dot位小数，入参为null可能造成空指针异常
	 * 
	 * @param var
	 *            待取舍值
	 * @param dot
	 *            保留的小数位位数
	 * @return String 取舍后的值
	 */
	public static String scale(String var, int dot) {
		BigDecimal big = new BigDecimal(var);
		return scale(big, dot);
	}

	/**
	 * 金额四舍五入，保留dot位小数，入参为null可能造成空指针异常
	 * 
	 * @param var
	 *            待取舍值
	 * @param dot
	 *            保留的小数位位数
	 * @return String 取舍后的值
	 */
	public static String scale(BigDecimal var, int dot) {
		return var.setScale(dot, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	/**
	 * 金额比较
	 * 
	 * @param var1
	 *            被比较值
	 * @param var2
	 *            比较值
	 * @return int 比较结果：
	 *         <p>
	 *         var1 大于 var2 return 1
	 *         </p>
	 *         <p>
	 *         var1 小于 var2 return -1
	 *         </p>
	 *         <p>
	 *         var1 等于 var2 return 0
	 *         </p>
	 */
	public static int compareTo(String var1, String var2) {
		BigDecimal big1 = new BigDecimal(var1);
		BigDecimal big2 = new BigDecimal(var2);
		return compareTo(big1, big2);
	}

	/**
	 * 金额比较
	 * 
	 * @param var1
	 *            被比较值
	 * @param var2
	 *            比较值
	 * @return int 比较结果：
	 *         <p>
	 *         var1 大于 var2 return 1
	 *         </p>
	 *         <p>
	 *         var1 小于 var2 return -1
	 *         </p>
	 *         <p>
	 *         var1 等于 var2 return 0
	 *         </p>
	 */
	public static int compareTo(BigDecimal var1, BigDecimal var2) {
		return var1.compareTo(var2);
	}

	/**
	 * 判断传入值是否为空或是0<br>
	 * 在某些需要判断0是否有意义的地方可能需要用到
	 * 
	 * @param var1
	 *            待判断的对象
	 * @return 当var1==null或var1值为0时，返回true，否则false
	 */
	public static boolean isEmptyOrZero(BigDecimal var1) {
		return var1 == null || var1.compareTo(BigDecimal.ZERO) == 0;
	}
	
	
	/**
	 * 金额计算，乘法，保留所有小数位，入参为null可能造成空指针异常
	 * 
	 * @param var1
	 *            被乘数
	 * @param var2
	 *            乘数
	 * @return String 相乘后的字符串值
	 */
	public static String abs(String var1) {
		BigDecimal big1 = new BigDecimal(var1);
		return big1.abs().toPlainString();
	}
	
	public static void main(String[] args) {
		System.out.println(DigitUtils.sub("0.2064", "0.2062"));		
		System.out.println(DigitUtils.multiply("3333",
				DigitUtils.sub("0.2064", "0.2062"),2));
	}
}
