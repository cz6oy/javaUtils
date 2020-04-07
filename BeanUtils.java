package com.kayakwise.gray.api.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kayakwise.gray.api.annotation.Parameter;
import com.kayakwise.gray.api.modal.FField;

/**
 * JavaBean工具类
 * 
 * @author leewc
 * @since 2018-04-10 17:29 v1.0
 * @version 1.0
 */
public class BeanUtils {
	private static Map<Class<?>, Field[]> FIELDS1 = new ConcurrentHashMap<>();
	private static Map<Class<?>, List<FField>> FIELDS2 = new ConcurrentHashMap<>();
	private static Map<Class<?>, Map<String, FField>> FIELDS3 = new ConcurrentHashMap<>();

	private static Field[] addFields(Class<?> clazz) {
		Field[] fields = getSimpleFields(clazz);
		FIELDS1.put(clazz, fields);
		return fields;
	}

	private static Field[] getFields1(Class<?> clazz) {
		Field[] fields = FIELDS1.get(clazz);
		if (fields == null) {
			fields = addFields(clazz);
		}
		return fields;
	}

	private static List<FField> getFields2(Class<?> clazz) {
		List<FField> list = FIELDS2.get(clazz);
		if (list == null) {
			list = new ArrayList<>();
			for (Field f : getFields1(clazz)) {
				list.add(new FField(f, f.getAnnotation(Parameter.class)));
			}
			FIELDS2.put(clazz, list);
		}
		return list;
	}

	public static Map<String, FField> getFields3(Class<?> clazz) {
		Map<String, FField> map = FIELDS3.get(clazz);
		if (map == null) {
			map = new HashMap<>();
			for (FField f : getFields2(clazz)) {
				map.put(f.field.getName(), f);
			}
			FIELDS3.put(clazz, map);
		}
		return map;
	}

	/**
	 * 重置缓存
	 */
	public static void reset() {
		FIELDS1.clear();
		FIELDS2.clear();
		FIELDS3.clear();
	}

	/**
	 * 获取对象所有字段
	 * 
	 * @param clazz
	 *            对象
	 * @return 字段数组
	 */
	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> list = new ArrayList<>();
		for (Class<?> c = clazz; !c.equals(Object.class); c = c.getSuperclass()) {
			list.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
		}
		return list;
	}

	/**
	 * 获取一个以字段名为Key值的存放Field对象的Map
	 * 
	 * @param clazz
	 *            指定的类型
	 * @return 存放Field对象的Map对象
	 */
	private static Map<String, Field> getFieldMap(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}

		Map<String, Field> map = new HashMap<>();
		for (Field field : getFields1(clazz)) {
			map.put(field.getName(), field);
		}
		return map;
	}

	/**
	 * 获取指定类型的所有属性字段，包括继承自父类的，但不包含{@code static},{@code final},{@code volatile}修饰的字段
	 * 
	 * @param clazz
	 *            指定的类型
	 * @return 字段属性数组
	 */
	public static Field[] getSimpleFields(Class<?> clazz) {
		List<Field> fields = getAllFields(clazz);
		for (Iterator<Field> iter = fields.iterator(); iter.hasNext();) {
			Field field = iter.next();
			if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
				iter.remove();
			}
			if (Modifier.isVolatile(field.getModifiers())) {
				iter.remove();
			}
		}
		Field[] FFields = fields.toArray(new Field[fields.size()]);
		AccessibleObject.setAccessible(FFields, true);
		return FFields;
	}

	/**
	 * 将一个Object实体类转换成Map
	 * 
	 * @param obj
	 *            待转换对象
	 * @return 转化后以sql风格定义key值的Map对象
	 * @throws Exception
	 */
	public static Map<String, Object> beanToMap(Object obj) throws Exception {
		if (obj == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		for (Field field : getFields1(obj.getClass())) {
			map.put(field.getName(), field.get(obj));
		}

		return map;
	}

	/**
	 * 转换Map对象中的数据到指定的JavaBean中，目标JavaBean中对应的接收字段需要注解{@link com.kayak.gray.api.annotation.Parameter}{@code .mapping()}来表示源容器中的字段名
	 * 
	 * @param clazz
	 *            目标JavaBean类型
	 * @param map
	 *            源数据Map
	 * @return 指定的JavaBean对象
	 * @throws Exception
	 */
	public static <T> T mapToBean(Class<T> clazz, Map<String, ?> map) throws Exception {
		if (map == null)
			return null;

		T t = clazz.newInstance();
		for (FField f : getFields2(clazz)) {
			String mapping = getMapping(f);
			if (mapping == null) {
				continue;
			}
			f.field.set(t, map.get(mapping));
		}
		return t;
	}

	/**
	 * 通过一个JavaBean对象映射一个指定类型的JavaBean对象，目标JavaBean中对应的接收字段需要注解{@link com.kayak.gray.api.annotation.Parameter}{@code .mapping()}来表示源容器中的字段名
	 * 
	 * @param clazz
	 *            目标JavaBean类型
	 * @param obj
	 *            源JavaBean对象
	 * @return 目标类型的JavaBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mapping(Class<T> clazz, Object obj) throws Exception {
		if (clazz == null || obj == null) {
			throw new NullPointerException("Method entry parameter cannot be null");
		}

		if (obj instanceof Map) {
			return mapToBean(clazz, (Map<String, ?>) obj);
		}

		T t = clazz.newInstance();
		Map<String, Field> map = getFieldMap(obj.getClass());
		List<FField> fields = getFields2(clazz);

		for (FField f : fields) {
			String mapping = getMapping(f);
			if (mapping == null) {
				continue;
			}
			Field field = map.get(mapping);
			if (field == null) {
				continue;
			}

			f.field.set(t, field.get(obj));
		}

		return t;
	}

	/**
	 * 获取注解{@link com.kayak.gray.api.annotation.Parameter}.{@code mapping()}的值
	 * 
	 * @param f
	 * @return
	 */
	private static String getMapping(FField f) {
		Parameter annotation = f.annotation;
		if (annotation == null) {
			return null;
		}
		String mapping = annotation.mapping();
		if (mapping == null || mapping.isEmpty()) {
			return null;
		}
		return mapping;
	}

}
