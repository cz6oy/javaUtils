package com.kayakwise.gray.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanUtil {
	
	private static final Logger log = LoggerFactory.getLogger(JavaBeanUtil.class);
	
	private static final String get = "get";

	private static final String set = "set";
	//缓存GET方法
	private static Map<Class<?>, Map<String, Method>> getMechods = new ConcurrentHashMap<>();
	//缓存SET方法
	private static Map<Class<?>, Map<String, Method>> setMechods = new ConcurrentHashMap<>();

	public static JavaBeanUtil util;
	
	private JavaBeanUtil(){}
	
	//单例模式
	public static JavaBeanUtil instance(){
		if(util == null){
			util = new JavaBeanUtil();
		}
		return util;
	}
	
	public Object getVal(Object obj, String fieldName){
		if (obj == null || fieldName == null || fieldName.isEmpty()) {
			return null;
		}
		
		Method m = getGetMethod(obj.getClass(), fieldName);
		if(m == null){
			return null;
		}
		try {
			return m.invoke(obj, new Object[]{});
		} catch (Exception e) {
			//log.warn("Exce get Method Failure.", e);
			return null;
		} 
	}
	
	public void setVal(Object obj, String fieldName, Object val){
		if (obj == null || fieldName == null || fieldName.isEmpty()) {
			return;
		}
		
		Method m = getSetMethod(obj.getClass(), fieldName);
		if(m != null){
			try {
				m.invoke(obj, new Object[]{val});
			} catch (Exception e) {
				@SuppressWarnings("rawtypes")
				Class [] params =  m.getParameterTypes();
				if(params != null && params.length > 0){
					if(params[0] != val.getClass()){
						log.error("Cann't case form [{}] ==> [{}].", val.getClass().getName(), params[0].getName());
					}
				}
				//log.warn("Exce set Method Failure[{}].", e);
			} 
		}
		
	}
	
	public Method getGetMethod(Class<?> clazz, String fieldName) {
		if (clazz == null || fieldName == null || fieldName.isEmpty()) {
			return null;
		}
//		log.debug("Class[{}], FieldName[{}]", clazz.getName(), fieldName);
		Map<String, Method> methods = getMechods.get(clazz);
		if(methods != null){
			Method m = methods.get(fieldName);
			if(m != null){
				//System.out.println("exists!");
				return m;
			}
		}else{
			//System.out.println("not exists!");
			methods = new ConcurrentHashMap<>();
			getMechods.put(clazz, methods);
		}
		try {
			Field [] fields = clazz.getDeclaredFields();
			String fieldname = "";
			for(Field field : fields){
				String fn = field.getName();
				//System.out.println(fn);
				if(fn.equalsIgnoreCase(fieldName) || fn.equalsIgnoreCase(fieldName.replace("_", ""))){
					fieldname = fn;
					break;
				}
			}
			
			Field [] superfields = clazz.getSuperclass().getDeclaredFields();
			for(Field field : superfields){
				String fn = field.getName();
			//	System.out.println(fn);
				if(fn.equalsIgnoreCase(fieldName) || fn.equalsIgnoreCase(fieldName.replace("_", ""))){
					fieldname = fn;
					break;
				}
			}
			
			if(fieldname == null || fieldname.equals("")){
				return null;
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(get);
			sb.append(fieldname.substring(0, 1).toUpperCase());
			sb.append(fieldname.substring(1));
			Method [] allMethod = clazz.getMethods();
			for(Method m : allMethod){
				if(m.getName().equals(sb.toString())){
					return m;
				}
			}
		} catch (Exception e) {
			//log.warn("Get get Method Failure.", e);
		}
		return null;
	}
	
	public Method getSetMethod(Class<?> clazz, String fieldName) {
		if (clazz == null || fieldName == null || fieldName.isEmpty()) {
			return null;
		}
		
		Map<String, Method> methods = setMechods.get(clazz);
		if(methods != null){
			Method m = methods.get(fieldName);
			if(m != null){
				return m;
			}
		}else{
			methods = new ConcurrentHashMap<>();
			setMechods.put(clazz, methods);
		}
		try {
			Field [] fields = clazz.getDeclaredFields();
			String fieldname = "";
			for(Field field : fields){
				String fn = field.getName();
				if(fn.equalsIgnoreCase(fieldName) || fn.equalsIgnoreCase(fieldName.replace("_", ""))){
					fieldname = fn;
					break;
				}
			}
			
			if(fieldname == null || fieldname.equals("")){
				return null;
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(set);
			sb.append(fieldname.substring(0, 1).toUpperCase());
			sb.append(fieldname.substring(1));
			Method [] allMethod = clazz.getMethods();
			for(Method m : allMethod){
				if(m.getName().equals(sb.toString())){
					return m;
				}
			}
		} catch (Exception e) {
			//log.warn("Get Set Method Failure.", e);
		}
		return null;
	}
	
}
