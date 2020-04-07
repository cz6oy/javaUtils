package com.kayakwise.gray.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

/**
 * 签名工具类
 * 
 * @author leewc
 * @since 2018-04-10 17:29 v1.0
 * @version 1.0
 */
@Deprecated
public class SignUtils {

	private static String KEY_STORE_PKCS12 = "PKCS12";
	private static String KEY_STORE_X509 = "X509";

	// ========================SHA1withRSA算法加签与验签

	/**
	 * 对字符串加密，采用SHA1withRSA算法 得到16进制字符串
	 * 
	 * @throws Exception
	 */
	public static String string2SignHex(String arg, String charset, String privateKeyCertPath, String password,
			String algorithm) throws Exception {
		// 得到待签名数据字节组
		byte[] data = arg.getBytes(charset);
		PrivateKey privateKey = getPrivateKey(privateKeyCertPath, password);
		// 用私钥对数据进行数字签名
		Signature signature = Signature.getInstance(algorithm);
		signature.initSign(privateKey);
		signature.update(data);

		return CodeUtils.byte2Hex(signature.sign());
	}

	/** 字符串验签 */
	public static boolean doCheck(String base64Msg, String signMsg, String publicKeyCertPath, String algorithm)
			throws Exception {
		try {
			PublicKey publicKey = getPublicKey(publicKeyCertPath);
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(CodeUtils.decodeBase64(base64Msg));

			boolean result = signature.verify(CodeUtils.hex2Bytes(signMsg));
			return result;
		} catch (Exception e) {
			throw new Exception("验签失败:" + e);
		}
	}

	/** 重载 字符串验签 */
	public static boolean doCheck(String base64Msg, String signMsg, PublicKey publicKey, String algorithm)
			throws Exception {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(base64Msg.getBytes());

			boolean result = signature.verify(CodeUtils.decodeBase64(signMsg));
			return result;
		} catch (Exception e) {
			throw new Exception("验签失败:" + e);
		}
	}

	// ========================证书获取相关

	/** 获取PrivateKey */
	public static PrivateKey getPrivateKey(String keyStorePath, String password) throws Exception {
		KeyStore keyStore = getKeyStore(keyStorePath, password);
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(getAliases(keyStore), password.toCharArray());
		return privateKey;
	}

	/** 重载PrivateKey */
	public static PrivateKey getPrivateKey(KeyStore keyStore, String password) throws Exception {
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(getAliases(keyStore), password.toCharArray());
		return privateKey;
	}

	/** 以PKCS12获取秘钥库 */
	private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		try (FileInputStream input = new FileInputStream(keyStorePath)) {
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_PKCS12);
			char[] charArray = password.toCharArray();
			keyStore.load(input, charArray);
			return keyStore;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 以JKS方式获取秘钥库 */
	public static KeyStore getKeyStoreByJKS(String keyStorePath, String password) throws Exception {
		try (FileInputStream input = new FileInputStream(keyStorePath)) {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			char[] charArray = password.toCharArray();
			keyStore.load(input, charArray);
			return keyStore;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 获取别名 */
	public static String getAliases(KeyStore keyStore) throws KeyStoreException {
		Enumeration<String> aliases = keyStore.aliases();
		String enumeration = null;
		while (true) {
			try {
				enumeration = aliases.nextElement();
			} catch (Exception e) {
				// ...
				break;
			}
		}
		return enumeration;
	}

	/** 获取PublicKey */
	public static PublicKey getPublicKey(String certificatePath) throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		PublicKey publicKey = certificate.getPublicKey();
		return publicKey;
	}

	/** 获取证书对象 */
	private static Certificate getCertificate(String certificatePath) throws Exception {
		CertificateFactory certFactory = CertificateFactory.getInstance(KEY_STORE_X509);
		try (InputStream input = new FileInputStream(new File(certificatePath));) {
			Certificate certificate = certFactory.generateCertificate(input);
			return certificate;
		}
	}

	// *******测试用方法*******//

	// public static void main(String[] args) throws Exception {
	// String publicKey =
	// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnZ85sffIDj7Ag7vXD6UCfl/qSrXPwGWzeP7iMBH2UWO5hvk36AwZQDst2WMyvNmLuZhhKhloGvMOj10XwxKfFsQSRt4gUuOJVVeHKcw6VoMrHjX4GsYkwbG+glnwHTGFChr4w0FUFOtPRolqgzlC4aYBYV4ZhE4wEAPSbxpBG+I/aQ/wms3HB5vXHQ5CwEy7Lxp4lQn+5FQ0+PNn6xmn1uCpNshAEaQJzE3QQK7pnaP1CjU9UmE+xHhKAYIhAqZqQO/cyL19n5sJksHwFPu2DjuqV8PHhh5jr6A/LKXwAa2my0waoopXG9TalCBxdgveS87CuAYK2giB2ZAe3aI/VQIDAQAB";
	// String privateKey =
	// "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCdnzmx98gOPsCDu9cPpQJ+X+pKtc/AZbN4/uIwEfZRY7mG+TfoDBlAOy3ZYzK82Yu5mGEqGWga8w6PXRfDEp8WxBJG3iBS44lVV4cpzDpWgyseNfgaxiTBsb6CWfAdMYUKGvjDQVQU609GiWqDOULhpgFhXhmETjAQA9JvGkEb4j9pD/CazccHm9cdDkLATLsvGniVCf7kVDT482frGafW4Kk2yEARpAnMTdBArumdo/UKNT1SYT7EeEoBgiECpmpA79zIvX2fmwmSwfAU+7YOO6pXw8eGHmOvoD8spfABrabLTBqiilcb1NqUIHF2C95LzsK4BgraCIHZkB7doj9VAgMBAAECggEBAIXnyyACHJbdgLdpUN8dk+zAl044/vG4dKwJ/SZRXCzwx6B/AJvGC32hyOWxfF0rg6R8a69UPjkG3Au3ToSOwVCZ6d0Cv/srCnTSEyXTNoxgirTzrH8ybe+PjQU4WuCyPQ/EsilOypuq/II0I6SvdL42RV3uj0bzFanFVXTFnSKNzwRP3dVK1yxwMWREUto8BsnrBUlR1CJjCKo3umYrRZ5DhG9dCuXq356aIkKJpWo3M+AveH9/6snCLGU/kohDjUJoPQG955vbW+EykhTTPnUDt/gvD/2DQ9jaiLFcFs9rKMe8FHZJL4AKeixgr5IbsWFzZcPFcb5MQl8rEetIpdECgYEA4nx1F+7LT3j/8SVMiYHCrcQ+mPLW/ZSozWfPS2zghOVSqZc18lswACSbUX7o82E7t8aonNTKRgL3CZFktW7t3LzUC97hwzelfeZHwUx+hnG/6f9CkveFI4OgbJ3wDycKR/MbJRinBTlWhuJDE5jfpC2VXTGT5UIKRubr38SKgaMCgYEAsil4NEJmIb4jG9rH/9cD4IzSISBa2qsZwv0LvShvQ/gBTgQFwklqppHk/vchGFXqCpP2c7qYMYZQihnf2Co2zsfbvZe5XY32SFG4IXhUfM1EiJ43Hvr8oq33K8vqdeyr+iJhkZEI8SLy4tPbzbZG3HXJGQ32c+1OEUFojxyAeqcCgYBZxq+7juJ0XChH9FsEXrVGn/WYNxpVZioLUczvvva4dxZvNwshSFgclmYixcWdQ7uiO1L257pOSjqWRlW/PXt39jmvgufGtDYbU765tpKoxEoofRBspUTXMQPUSj6wIu+GWa025TC/d1z/Fo00JXLw50i/qjlPtP/R3xM8nfGxNQKBgQCPSgUQWtrThp2sGHDEVkUbA4c491G7DuOgTG51iyC51KwP9/I9yVhKcYpweYgXj3m1Wj1b5JXlNvZtq9O5gnIkKt+Tg4SlNrj+fyMNYD7rRRg6nK0OcKRhnnLtuYpvn5LNkXtRXQmJDyezwNrGfzRMuTqsuOsfX5trC2UxvKWkfwKBgGV8A1zd+ykcLrWgQE1UKcUhaNmRlX2x+htf21/8VP/SI4kMscnYXhTh2fRPxIXcSl3xQK8oHAzxbffFvJ4pk98b4cYdPRw5bbf+s2u2vKsdduorzMi9quyeU2+QcbM5L97B0xWIVUFuNC91tSdXcS89g+0fJJIyDmZIqIIfCBC3";
	//
	// byte[] buf = new byte[32 * 1024];
	// int k = IOUtils.read(new
	// FileInputStream("src/com/kayak/pay/xwbank/pmc/中金/response.xml"), buf);
	// String templete = new String(buf, 0, k, "UTF-8");
	// }
}
