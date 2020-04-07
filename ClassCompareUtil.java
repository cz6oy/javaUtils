package com.kayakwise.gray.api.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassCompareUtil {
	/**
	 * 用于比较两个类中的所有属性值
	 * 比较两个实体属性值，返回一个boolean,true则表时两个对象中的属性值无差异
	 * @param oldObject 进行属性比较的对象1
	 * @param newObject 进行属性比较的对象2
	 * @return 属性差异比较结果boolean
	 */
	public static boolean compareObject(Object oldObject, Object newObject) {
		Map<String, Map<String,Object>> resultMap=compareFields(oldObject,newObject);
		System.out.println("resultMap:"+resultMap.size());
		if(resultMap.size()>=0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个Map分别存oldObject,newObject此属性名的值
	 * @param oldObject 进行属性比较的对象1
	 * @param newObject 进行属性比较的对象2
	 * @return 属性差异比较结果map
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Map<String,Object>> compareFields(Object oldObject, Object newObject) {
		Map<String, Map<String, Object>> map = null;
		try{	
			/**
			 * 只有两个对象都是同一类型的才有可比性
			 */
//			if (oldObject.getClass() == newObject.getClass()) {
				map = new HashMap<String, Map<String,Object>>();
				System.out.println("Map:"+map.size());
				Class clazz = oldObject.getClass();
				Class nclazz = newObject.getClass();
				//获取object的所有属性
				Map<String,Object> oldMap=new HashMap<String, Object>();
				Map<String,Object> newMap=new HashMap<String, Object>();
				
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,Object.class).getPropertyDescriptors();
				PropertyDescriptor[] npds = Introspector.getBeanInfo(nclazz,Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					//遍历获取属性名
					String name = pd.getName();
					//获取属性的get方法
					Method readMethod = pd.getReadMethod();
					// 在oldObject上调用get方法等同于获得oldObject的属性值
					Object oldValue = readMethod.invoke(oldObject);
//					System.out.println("name:"+name+" oldValue:"+oldValue);
					oldMap.put(name, oldValue);
				}
				for (PropertyDescriptor npd : npds) {
					//遍历获取属性名
					String name = npd.getName();
					//获取属性的get方法
					Method readMethod = npd.getReadMethod();
					// 在oldObject上调用get方法等同于获得oldObject的属性值
					Object newValue = readMethod.invoke(newObject);
//					System.out.println("name:"+name+" newValue:"+newValue);
					newMap.put(name, newValue);
				}
				
				System.out.println(newMap.get("flag"));
				Map<String,Object> resultMap = new HashMap<String,Object>();
				oldMap.forEach((k,v)->{
					System.out.println("K:"+k+" V:"+v);
					System.out.println("newMap.get(k):"+newMap.get(k.toString()));
					Object obj=newMap.get(k);
					if(v instanceof String){
						if (v==null&&obj==null||v==null&&obj!=null||v!=null&&obj==null) {
							resultMap.put(k,obj.toString());
						}else{
							if(!v.equals(obj.toString())){
								resultMap.put(k,obj.toString());
							}
						}
					}else if (v instanceof Integer) {
						String oldVal=v.toString();
						String newVal=obj.toString();
						if (v==null&&obj==null||v==null&&obj!=null||v!=null&&obj==null) {
							resultMap.put(k,obj.toString());
						}else{
							if(Integer.parseInt(oldVal)!=Integer.parseInt(newVal)){
								resultMap.put(k,obj.toString());
							}
						}
					}	
				});

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
}
