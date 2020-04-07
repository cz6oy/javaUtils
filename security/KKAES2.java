package com.kayakwise.gray.api.util.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class KKAES2 {

	private static KKAES2 instance=null;
	
	private KKAES2(){
		
	}
	
	public static KKAES2 getInstance(){
		if (instance==null)
		instance= new KKAES2();
		return instance;
	}
	
    /**
     * 加密String明文输入,经过BASE64编码String密文输出
     * 
     * @param text,keystr,ivstr
     * @return
     */
        
    public static String encrypt(String text, String keystr) throws Exception
    {
    	String ivStr = "0000000000000000";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] ivBytes = new byte[16];

        byte[] b= keystr.getBytes("UTF-8");
        byte[] v = ivStr.getBytes("UTF-8");

        int len= b.length; 
        int len2 = v.length;

        if (len > keyBytes.length) len = keyBytes.length;
        if (len2> ivBytes.length) len2 = ivBytes.length;

        System.arraycopy(b, 0, keyBytes, 0, len);
        System.arraycopy(v,0,ivBytes,0,len2);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
        byte [] results = cipher.doFinal(text.getBytes("UTF8"));
        return new  String(BASE64.encode(results));
    }

    /**
     * 解密 以BASE64形式String密文输入,String明文输出
     * 
     * @param text,keystr,ivstr
     * @return
     */
    public static String decrypt(String text, String keystr) throws Exception
    {
    	String ivStr = "0000000000000000";
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] ivBytes = new byte[16];
        byte[] b= keystr.getBytes("UTF-8");
        byte[] v = ivStr.getBytes("UTF-8");
        int len= b.length; 
        int len2 = v.length;
        if (len > keyBytes.length) len = keyBytes.length;
        if (len2> ivBytes.length) len2 = ivBytes.length;
               System.arraycopy(b, 0, keyBytes, 0, len);
        System.arraycopy(v,0,ivBytes,0,len2);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        byte [] results = cipher.doFinal(BASE64.decode(text));
        return new String(results,"UTF-8");
    }
   
}