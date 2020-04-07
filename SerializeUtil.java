package com.kayakwise.gray.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializeUtil {

	private static final Logger log = LogManager.getLogger(SerializeUtil.class);
	
	// 对像序列化
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			Closer.close(baos, oos);
		}
		return null;
	}

	// 对像反序列化
	public static <T> T unserialize(Class<T> cls, byte[] objectByte) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(objectByte);
			ois = new ObjectInputStream(bais);
			return (T)ois.readObject();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			Closer.close(ois, bais);
		}
		return null;
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("qqqqqqqq", "ewewew");
		byte [] ss = SerializeUtil.serialize(map);
		
		Map ret = SerializeUtil.unserialize(Map.class, ss);
		System.out.println(ret.get("qqqqqqqq"));
	}

}
