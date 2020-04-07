package com.kayakwise.gray.api.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import com.kayakwise.gray.api.convert.xml.XmlConvert3;
import com.kayakwise.gray.api.convert.xml.XmlConvert4;

/**
 * <p>
 * Xml格式报文操作相关工具类2
 * </p>
 * 提供解析、组装集合的功能；多节点重复属性名的处理
 * 
 * @author Lyee
 * @since 2018-06-21 21:29 v1.0
 * @version 1.0
 */
public class XmlUtils2 {
	private static Map<Class<?>, XmlConvert3<?>> cache1 = new ConcurrentHashMap<>();
	private static Map<Class<?>, XmlConvert4> cache2 = new ConcurrentHashMap<>();

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
		XmlConvert3<T> convert = (XmlConvert3<T>) cache1.get(clazz);
		if (convert == null) {
			convert = new XmlConvert3<>();
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
	public static String toXml(Object obj, String template, String rootNodeName) throws Exception {
		Class<?> clazz = obj.getClass();
		XmlConvert4 convert = cache2.get(obj.getClass());
		if (convert == null) {
			convert = new XmlConvert4();
			convert.setClazz(clazz, rootNodeName);
			cache2.put(clazz, convert);
		}

		return convert.convert(obj, template);
	}

	/**
	 * JavaBean转换成xml 默认编码UTF-8
	 * 
	 * @param obj
	 * @param writer
	 * @return
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static String convertToXml(Object obj) throws JAXBException, SAXException {
		return convertToXml(obj, "UTF-8");
	}

	/**
	 * JavaBean转换成xml
	 * 
	 * @param obj
	 *            参数对象
	 * @param encoding
	 *            转换编码
	 * @return
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static String convertToXml(Object obj, String encoding) throws JAXBException, SAXException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		// 设置是否需要强格式化，默认不需要
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// 设置编码
		marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
		// 忽略xml报文头
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

		StringWriter writer = new StringWriter();
		OutputFormat format = new OutputFormat();
		// 设置缩进
		format.setIndent(true);
		// 设置换行
		format.setNewlines(true);
		format.setEncoding(encoding);
		format.setNewLineAfterDeclaration(false);
		XMLWriter xmlwriter = new XMLWriter(writer, format);
		XMLFilterImpl nsfFilter = new XMLFilterImpl() {
			private boolean ignoreNamespace = false;
			private String rootNamespace = null;
			private boolean isRootElement = true;

			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
			}

			@Override
			public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
				if (this.ignoreNamespace) {
					uri = "";
				}
				if (this.isRootElement) {
					this.isRootElement = false;
				} else if (!uri.equals("") && !localName.contains("xmlns")) {
					localName = localName + " xmlns=\"" + uri + "\"";
				}

				super.startElement(uri, localName, localName, atts);
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (this.ignoreNamespace) {
					uri = "";
				}
				super.endElement(uri, localName, localName);
			}

			@Override
			public void startPrefixMapping(String prefix, String url) throws SAXException {
				if (this.rootNamespace != null) {
					url = this.rootNamespace;
				}
				if (!this.ignoreNamespace) {
					super.startPrefixMapping("", url);
				}
			}
		};
		nsfFilter.setContentHandler(xmlwriter);

		marshaller.marshal(obj, nsfFilter);
		String result = writer.toString();

		return result;
	}

	/**
	 * xml转换成JavaBean
	 * 
	 * @param xml
	 *            需要转换的xml报文
	 * @param c
	 *            目标实体类型
	 * @return T
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static <T> T converyToJavaBean(Class<T> c, String xml) throws JAXBException, SAXException {
		JAXBContext context = JAXBContext.newInstance(c);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		XMLReader reader = XMLReaderFactory.createXMLReader();
		XMLFilterImpl nsfFilter = new XMLFilterImpl() {
			private boolean ignoreNamespace = false;

			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
			}

			@Override
			public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
				if (this.ignoreNamespace) {
					uri = "";
				}
				super.startElement(uri, localName, qName, atts);
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (this.ignoreNamespace) {
					uri = "";
				}
				super.endElement(uri, localName, localName);
			}

			@Override
			public void startPrefixMapping(String prefix, String url) throws SAXException {
				if (!this.ignoreNamespace) {
					prefix = "";
				}
				super.startPrefixMapping(prefix, url);
			}
		};
		nsfFilter.setParent(reader);
		InputSource input = new InputSource(new StringReader(xml));
		SAXSource source = new SAXSource(nsfFilter, input);

		@SuppressWarnings("unchecked")
		T t = (T) unmarshaller.unmarshal(source);

		return t;
	}

}
