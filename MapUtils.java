package com.kayakwise.gray.api.util;

import java.util.HashMap;
import java.util.Map;

import com.kayakwise.gray.api.convert.DoubleConverter;
import com.kayakwise.gray.api.convert.IntegerConverter;
import com.kayakwise.gray.api.convert.LongConverter;

/**
 * Map常用操作工具类
 * 
 * @author leewc
 * @since 2018-05-08 v1.0
 * @version 1.0
 */
public class MapUtils {
	private static IntegerConverter ic1 = new IntegerConverter(true);
	private static IntegerConverter ic2 = new IntegerConverter(false);
	private static LongConverter lc1 = new LongConverter(true);
	private static LongConverter lc2 = new LongConverter(false);
	private static DoubleConverter dc1 = new DoubleConverter(true);
	private static DoubleConverter dc2 = new DoubleConverter(false);

	/**
	 * 从指定Map对象中获取键值key所对应的Integer类型的value
	 * <p>
	 * 当value为null时，返回0
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @return Integer
	 */
	public static <K, V> Integer integer(Map<K, V> map, K key) {
		return integer(map, key, false);
	}

	/**
	 * 从指定Map对象中获取键值key所对应的Integer类型的value
	 * <p>
	 * 当value为null时，若nullable为true，则返回null，否则返回0
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @param nullable
	 *            是否保留null
	 * @return Integer
	 */
	public static <K, V> Integer integer(Map<K, V> map, K key, boolean nullable) {
		try {
			if (nullable) {
				return ic1.convert(map.get(key), Integer.class);
			} else {
				return ic2.convert(map.get(key), Integer.class);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 从指定Map对象中获取键值key所对应的Long类型的value
	 * <p>
	 * 当value为null时，返回0L
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @return Long
	 */
	public static <V, K> Long longx(Map<K, V> map, K key) {
		return longx(map, key, false);
	}

	/**
	 * 从指定Map对象中获取键值key所对应的Long类型的value
	 * <p>
	 * 当value为null时，若nullable为true，则返回null，否则返回0L
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @param nullable
	 *            是否保留null
	 * @return Long
	 */
	public static <K, V> Long longx(Map<K, V> map, K key, boolean nullable) {
		try {
			if (nullable) {
				return lc1.convert(map.get(key), Long.class);
			} else {
				return lc2.convert(map.get(key), Long.class);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 从指定Map对象中获取键值key所对应的Double类型的value
	 * <p>
	 * 当value为null时，返回0.0
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @return Double
	 */
	public static <K, V> Double doublex(Map<K, V> map, K key) throws Exception {
		return doublex(map, key, false);
	}

	/**
	 * 从指定Map对象中获取键值key所对应的Double类型的value
	 * <p>
	 * 当value为null时，若nullable为true，则返回null，否则返回0.0
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @param nullable
	 *            是否保留null
	 * @return Double
	 */
	public static <K, V> Double doublex(Map<K, V> map, K key, boolean nullable) {
		Object value = map.get(key);
		try {
			if (nullable) {
				return dc1.convert(value, Double.class);
			} else {
				return dc2.convert(value, Double.class);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 从指定Map对象中获取键值key所对应的String类型的value
	 * <p>
	 * 当value为null时，返回null
	 * </p>
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @param key
	 *            键值
	 * @return String
	 */
	public static <K, V> String string(Map<K, V> map, K key) {
		Object o = map.get(key);
		if (o == null)
			return null;

		return o.toString();
	}

	/**
	 * 对指定的Map对象进行克隆
	 * 
	 * @param <K>
	 *            Map的键的Java类型
	 * @param <V>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 * @return
	 */
	public static <K, V> Map<K, V> clone(Map<K, V> map) {
		Map<K, V> m = new HashMap<>();
		m.putAll(map);
		return m;
	}

	/**
	 * 将指定的Map对象中的所有key转换成小写
	 * 
	 * @param <T>
	 *            Map的值的Java类型
	 * 
	 * @param map
	 *            源Map对象
	 */
	public static <T> void keyToLowerCase(Map<String, T> map) {
		Map<String, T> tmp = MapUtils.clone(map);
		map.clear();
		for (Map.Entry<String, T> entry : tmp.entrySet()) {
			map.put(entry.getKey() != null ? entry.getKey().toLowerCase() : null, entry.getValue());
		}
	}

	// public static void main(String[] args) {
	// Map<String, Object> map1 = new HashMap<String, Object>() {
	// {
	// put("1", 1);
	// }
	// };
	//
	// Map<Integer, Object> map2 = new HashMap<Integer, Object>() {
	// {
	// put(1, 1);
	// }
	// };
	//
	// Integer i1 = integer(map1, "1");
	// Integer i2 = integer(map2, 1);
	// System.out.println(i1 + i2);
	// }

}
