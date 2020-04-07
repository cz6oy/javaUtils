package com.kayakwise.gray.api.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对字符串加密
 * 加密算法使用SHA-1,SHA-256,默认使用SHA-256
 *
 */
public class SHA {
	
	private static SHA instance=null;
	
	private SHA(){
		
	}
	
	public static SHA getInstance(){
		if (instance==null)
		instance= new SHA();
		return instance;
	}
	
    /**
     * 对字符串加密,加密算法使用SHA-1,SHA-256,默认使用SHA-256
     * 
     * @param strSrc
     *            要加密的字符串
     * @param encName
     *            加密类型
     * @return
     */
    public static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = BASE64.encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    
}
