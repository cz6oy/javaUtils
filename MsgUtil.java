package com.kayakwise.gray.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.kayakwise.frame.fixinal.FixTransfer;
import com.kayakwise.gray.api.code.ErrorCode;
import com.kayakwise.gray.api.exception.BusiErrorException;
import com.project.msg.PubResponseMsg;

/**
 * 报文参数处理工具类
 * 
 * @author shihonghao
 *
 * 2019年9月4日
 */
public class MsgUtil {


	/**
	 * 对象参数去空格并赋默认值
	 * 
	 * @return
	 * @throws BusiErrorException 
	 * 
	 */
	public static Object format(Object objIn) throws BusiErrorException {
		Class<? extends Object> clazz = objIn.getClass();
		Object objOut = null;
		try {
			objOut = clazz.newInstance();
			objOut = objIn;
			for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
				for (Field f : c.getDeclaredFields()) {
					FixTransfer fix = f.getAnnotation(FixTransfer.class);
					if (fix == null) {
						continue;
					}
					
					Type genericType = f.getGenericType();
					f.setAccessible(true);
					if (genericType instanceof ParameterizedType) {
						Object field = f.get(objOut);
						Class<?> clzz = field.getClass();
						Method sizeMethod = clzz.getDeclaredMethod("size");
						sizeMethod.setAccessible(true);
						int size = (int) sizeMethod.invoke(field);
						for (int i = 0; i < size; i++) {
							Method listGetMethod = clzz.getDeclaredMethod("get", int.class);
							Object obj = listGetMethod.invoke(field, i);
							format(obj);
						}

					} else {
						String defaultVal=fix.defaultValue();
						String str = (String) f.get(objOut);
						str = str.replaceAll(" +", "");
						String name = f.getName();
						
						if(!"".equals(defaultVal)){
							if("".equals(str)){
								str=defaultVal;
							}
							if("defInteger".equals(name)||"num".equals(name)){
								str=removeLing(str);
							}
							}
						f.set(objOut, str);
					}
				}
			}
		} catch (Exception e) {
		throw new BusiErrorException(ErrorCode.SYS9900_系统异常);
		}
		return objOut;
	}
	  public static <T> Object initReq(Class<T> clazz,String transCode) {
		  PubResponseMsg rep;
			try {
				rep = (PubResponseMsg) clazz.newInstance();
				rep.setTrsNo("141");
				rep.setTrsDt(DateUtils.getNowDate());
				rep.setTrsTm(DateUtils.getNowTime());
				rep.setTrsBrOrg("9950");
				rep.setTrsOrgan("9950");
				rep.setTrancode(transCode);
				rep.setFlg1("0");
				rep.setFlg4("E");
				rep.setBlNtFlg("1");
				rep.setWrkStatNo("000");
				rep.setTlrId("11184");
				return rep;
			} catch (Exception e) {
			return null;
			}
			
	   }
	  public static String removeLing(String str){
			int len=str.length();
			char charStr[] = str.toCharArray();
			int index=0;
			for (int i = 0; i <len; i++) {
				if ('0'!=charStr[i]) {
					index=i;
					break;
				}
			}
			return str.substring(index, len);
			
		}
}
