package com.kayakwise.gray.api.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * 字符串编码操作工具类
 * 
 * @author leewc
 * @since 2018-05-08 11:11 v1.0
 * @version 1.0
 */
public class CodeUtils {
	private static final Decoder DECODER = Base64.getDecoder();
	private static final Encoder ENCODER = Base64.getEncoder();
	private static final String HEX = "0123456789abcdef";

	// ========================Base64转换相关

	/**
	 * 字符串进行Base64转码处理，返回字节数组
	 * 
	 * @param arg
	 *            源字符串
	 * @return byte[] Base64转码得到的字节数组
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encodeBase64(String arg) throws UnsupportedEncodingException {
		return ENCODER.encode(arg.getBytes());
	}

	/**
	 * 字符串进行Base64转码处理，返回字节数组
	 * 
	 * @param arg
	 *            源字符串
	 * @param charset
	 *            源字符串字符集
	 * @return byte[] Base64转码得到的字节数组
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encodeBase64(String arg, String charset) throws UnsupportedEncodingException {
		return ENCODER.encode(arg.getBytes(charset));
	}

	/**
	 * 字符串进行Base64转码处理，返回字符串
	 * 
	 * @param arg
	 *            源字符串
	 * @param charset
	 *            源字符串字符集
	 * @return String Base64转码得到的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeBase64ToString(String arg, String charset) throws UnsupportedEncodingException {
		return ENCODER.encodeToString(arg.getBytes(charset));
	}

	/**
	 * 字符串进行Base64转码处理，返回字符串
	 * 
	 * @param arg
	 *            源字符串
	 * @return String Base64转码得到的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeBase64ToString(String arg) throws UnsupportedEncodingException {
		return ENCODER.encodeToString(arg.getBytes());
	}

	/**
	 * 字符串进行Base64解码处理，返回字节数组
	 * 
	 * @param arg
	 *            源Base64字符串
	 * @return byte[] Base64解码得到的字节数组
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] decodeBase64(String arg) throws UnsupportedEncodingException {
		return DECODER.decode(arg);
	}

	/**
	 * 字符串进行Base64解码处理，返回字符串
	 * 
	 * @param arg
	 *            源Base64字符串
	 * @return String Base64解码得到的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeBase64ToString(String arg) throws UnsupportedEncodingException {
		return new String(DECODER.decode(arg));
	}

	/**
	 * 字符串进行Base64解码处理，返回字符串
	 * 
	 * @param arg
	 *            源Base64字符串
	 * @param charset
	 *            源字符串字符集
	 * @return String Base64解码得到的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeBase64ToString(String arg, String charset) throws UnsupportedEncodingException {
		return new String(DECODER.decode(arg), charset);
	}

	// ========================Hex转换相关

	/**
	 * byte数组转16进制字符串
	 * 
	 * @param bytes
	 *            源二进制数组
	 * @return String 16进制编码字符串
	 */
	public static String byte2Hex(byte[] bytes) {
		return String.format("%x", new BigInteger(1, bytes));
	}

	/**
	 * 字符串转16进制字符串
	 * 
	 * @param arg
	 *            源字符串
	 * @return String 16进制编码字符串
	 */
	public static String string2Hex(String arg) {
		if (arg == null)
			return null;
		return byte2Hex(arg.getBytes());
	}

	/**
	 * 16进制字符串转byte数组
	 * 
	 * @param hex
	 *            源字符串
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] hex2Bytes(String hex) throws IOException {
		if (hex == null || hex.length() == 0)
			return null;
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(hex.length() / 2)) {
			for (int i = 0; i < hex.length(); i += 2) {
				out.write(HEX.indexOf(hex.charAt(i)) << 4 | HEX.indexOf(hex.charAt(i + 1)));
			}
			return out.toByteArray();
		}
	}

	/**
	 * 16进制字符串转字符串
	 * 
	 * @param hex
	 *            源字符串
	 * @return String
	 * @throws IOException
	 */
	public static String hex2String(String hex) throws IOException {
		byte[] bytes = hex2Bytes(hex);
		return new String(bytes);
	}

	// public static void main(String[] args) throws IOException {
	// String arg = "这是一个比较不严肃的测试数据，应为这可能是假的数据";
	// String hex2 = string2Hex(arg);
	// System.out.println(hex2);
	// String string = hex2String(hex2);
	// System.out.println(string);
	// }

}
