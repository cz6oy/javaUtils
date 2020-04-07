package com.kayakwise.gray.api.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * Json操作工具类
 * 
 * @author leewc
 * @since 2018-05-08 15:08 v1.0
 * @version 1.0
 */
public class JsonUtils {

	/**
	 * json格式字符串转JSONObject对象
	 * 
	 * @param json
	 *            源json格式字符串
	 * @return JSONObject
	 */
	public static JSONObject toJSONObject(String json) {
		return JSONObject.parseObject(json);
	}

	/**
	 * 将Json转化成Map
	 * 
	 * @param json
	 *            源json格式字符串
	 * @return Map
	 */
	public static Map<String, Object> jsonToMap(String json) {
		Map<String, Object> map = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
		});
		return map;
	}

	/**
	 * 将Map转化成Json
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @return String json格式字符串
	 */
	public static <K, V> String mapToJson(Map<K, V> map) {
		return JSON.toJSONString(map);
	}

	/**
	 * 将Map转化成Json，支持自定义需要序列化的字段
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param fields
	 *            需要序列化的字段名
	 * @return String json格式字符串
	 */
	public static <K, V> String mapToJson(Map<K, V> map, String... fields) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(map.getClass(), fields);
		return JSON.toJSONString(map, filter);
	}

	/**
	 * Object转Json
	 * 
	 * @param obj
	 *            源待转换对象
	 * @return String json格式字符串
	 */
	public static String objectToJson(Object obj) {
		return JSON.toJSONString(obj);
	}
	
	/**
	 * 不过滤null值
	 * @param obj
	 * @return
	 */
	public static String objectToJsonNullStringAsEmpty(Object obj) {
		return JSON.toJSONString(obj,SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullListAsEmpty);
	}
	
	public static String objectToJson(Object obj,boolean flag) {
		if(flag){
			return objectToJsonNullStringAsEmpty(obj);
		}else{
			return objectToJson(obj);
		}
	}
	
	
	/**
	 * 接收大成时使用
	 * @param clazz
	 * @param json
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	
	public static <T> T jsonToObjectDC(Class<T> clazz, String json) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		T t = jsonToObject(clazz, json);
		return t;
	}
	
	public static <T> T genObject(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		T t = clazz.newInstance();
		return t;
	} 
	
	
	/*public static void main(String[] args) {
		String str = "{\"resutlist\":[{\"navdate\":\"20181017\",\"yearinc\":\"0.00\",\"marketvalue\":\"0.9781\"}],\"retmsg\":\"查询组合基金净值列表成功\",\"retcode\":\"0000\"}";
		A003Resp jsonToObject = jsonToObject(A003Resp.class, str);
		System.out.println(jsonToObject);
	}
	*/
	
	
	
	
	/**
	 * 发送大成时使用
	 * @param obj
	 * @param clazz
	 * @return
	 */
	
	public static String objectToJsonDC(Object obj) {
		return JSON.toJSONString(obj);
	}
/*	public static String objectToJsonDC(Object obj,Class<?> clazz) {
		SimplePropertyPreFilter filterIn = new SimplePropertyPreFilter(obj.getClass());
		SimplePropertyPreFilter filterEx = new SimplePropertyPreFilter(obj.getClass());
		Set<String> includes = filterIn.getIncludes();
		Set<String> excludes = filterEx.getExcludes();
		Field[] fi = clazz.getDeclaredFields();
		 for (Field field : fi) {
			 includes.add(field.getName());
			 excludes.add(field.getName());
		}
		 String head = JSON.toJSONString(obj, filterIn);
		 String body = JSON.toJSONString(obj, filterEx);
		 return  String.format("{\"head\":%s,\"body\":%s}", head,body);
	}*/
	
	
	
	

	/**
	 * Objects转Json，支持自定义需要序列化的字段，此方法不适用于转集合类型
	 * 
	 * @param obj
	 *            源待转换对象
	 * @param fields
	 *            需要序列化的字段名
	 * @return 序列化得到的json字符串
	 */
	public static String objectToJson(Object obj, String... fields) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(obj.getClass(), fields);
		return JSON.toJSONString(obj, filter);
	}

	/**
	 * Objects转Json，支持自定义需要序列化的字段，此方法适用于转集合类型
	 * 
	 * @param list
	 *            源待转换对象
	 * @param clazz
	 *            集合元素类型
	 * @param fields
	 *            需要序列化的字段名
	 * @return 序列化得到的json字符串
	 */
	public static String objectToJson(List<?> list, Class<?> clazz, String... fields) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(clazz, fields);
		return JSON.toJSONString(list, filter);
	}

	/**
	 * Json转Object
	 * 
	 * @param <T>
	 *            目标对象Java类型
	 * @param clazz
	 *            目标对象
	 * @param json
	 *            源json格式字符串
	 * @return T 反序列化后的指定Java对象
	 */
	public static <T> T jsonToObject(Class<T> clazz, String json) {
		T t = JSON.parseObject(json, clazz);
		return t;
	}

	/**
	 * Json转List
	 * 
	 * @param <T>
	 *            转换的List的元素类型
	 * @param clazz
	 *            元素类型的Class
	 * @param json
	 *            源json格式字符串
	 * @return 反序列化后的对象的集合
	 */
	public static <T> List<T> jsonToArray(Class<T> clazz, String json) {
		return JSON.parseArray(json, clazz);
	}


	
	/**
	 * 将map转化成json 保留map中null值转换为空串
	 */
	public static String mapToJson_2(Map<String, String> map) throws Exception {
		String jString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty);
		return jString;
	}


}
