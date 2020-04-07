package com.kayakwise.gray.api.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;

import com.kayakwise.gray.api.convert.xml.XmlConvert1;
import com.kayakwise.gray.api.convert.xml.XmlConvert2;

/**
 * Xml格式报文操作相关工具类
 * 
 * @author zuojie
 * @since 2018-04-10 17:29 v1.0
 * @version 1.0
 */
public class XmlUtils {
	private static Map<Class<?>, XmlConvert1<?>> cache1 = new ConcurrentHashMap<>();
	private static Map<Class<?>, XmlConvert2> cache2 = new ConcurrentHashMap<>();
	private static Map<String, String> xmlTemplates = new ConcurrentHashMap<>();

	/**
	 * 将Xml报文转化为指定的实体对象
	 * 
	 * @param clazz
	 *            目标实体类型
	 * @param xml
	 *            Xml报文
	 * @return T
	 * @throws Exception
	 */
	public static <T> T toBean(Class<T> clazz, String xml) throws Exception {
		@SuppressWarnings("unchecked")
		XmlConvert1<T> convert = (XmlConvert1<T>) cache1.get(clazz);
		if (convert == null) {
			convert = new XmlConvert1<>();
			convert.setClazz(clazz);
			cache1.put(clazz, convert);
		}

		return convert.convert(xml);
	}

	/**
	 * 转换模板文件数据，得到xml报文
	 * 
	 * @param obj
	 *            参数对象
	 * @param template
	 *            待替换模板
	 * @return String
	 * @throws Exception
	 */
	public static String toXml(Object obj, String template) throws Exception {
		Class<?> clazz = obj.getClass();
		XmlConvert2 convert = cache2.get(obj.getClass());
		if (convert == null) {
			convert = new XmlConvert2();
			convert.setClazz(clazz);
			cache2.put(clazz, convert);
		}

		return convert.convert(obj, template);
	}

	/**
	 * 获取Xml模板文件字符串
	 * 
	 * @param clazz
	 *            模板文件所在包路经
	 * @param file
	 *            模板文件名
	 * @return String
	 */
	public static String getXmlTemplate(Class<?> clazz, String file) {
		String template = xmlTemplates.get(file);
		if (template == null) {
			try (InputStream is = clazz.getResourceAsStream(file);
					ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				IOUtils.copy(is, os);
				template = os.toString("UTF-8");
				xmlTemplates.put(file, template);
			} catch (Exception e) {
				return null;
			}
		}
		return template;
	}

	/**
	 * 重置缓存的模板文件信息
	 */
	public static void reset() {
		xmlTemplates.clear();
	}

	// public static void main(String[] args) throws Exception {
	// String xml = FileUtils.readFileToString(new File("D:\\a.xml"), "UTF-8");
	//
	// long start = System.currentTimeMillis();
	// Book book = null;
	// for (int i = 0; i < 20; i++) {
	// book = toBean(Book.class, xml);
	// }
	//
	// // long stop = System.currentTimeMillis();
	// //
	// // System.out.println("time: " + (stop - start));
	// // System.out.println(book);
	// }

	// public static class Book {
	// private String bookName;
	//
	// public String getBookName() {
	// return bookName;
	// }
	//
	// public void setBookName(String bookName) {
	// this.bookName = bookName;
	// }
	//
	// }

}
