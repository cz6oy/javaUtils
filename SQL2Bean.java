package com.kayakwise.gray.api.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQL2Bean {
	private static final String REGEX = "[ \t]+";
	private static Map<String, String> ENUMS = new HashMap<>();

	static {
		ENUMS.put("tradeType", "TradeTypeEnum");
		ENUMS.put("bizType", "BizTypeEnum");
		ENUMS.put("acctType", "AcctTypeEnum");
		ENUMS.put("acctLvl", "AcctLevelEnum");
		ENUMS.put("certType", "CertTypeEnum");
		ENUMS.put("active", "ActiveStateEnum");
		ENUMS.put("payerAcctType", "AcctTypeEnum");
		ENUMS.put("payerCertType", "CertTypeEnum");
		ENUMS.put("payeeAcctType", "AcctTypeEnum");
		ENUMS.put("payeeCertType", "CertTypeEnum");
		ENUMS.put("currency", "CurrencyEnum");
		ENUMS.put("routeType", "RouteTypeEnum");
		ENUMS.put("priority", "PriorityEnum");
		ENUMS.put("msgDrctn", "MsgDrctnEnum");
		ENUMS.put("chargeType", "ChargeTypeEnum");
		ENUMS.put("refunded", "BooleanEnum");
		ENUMS.put("clearType", "ClearTypeEnum");
		ENUMS.put("checked", "BooleanEnum");
		ENUMS.put("cleared", "BooleanEnum");
		// ENUMS.put("checked", "BooleanEnum");
		// ENUMS.put("checked", "BooleanEnum");
	}

	private static void generate(String packag, String clazz, String extend, String path, String file)
			throws Exception {
		StringBuffer buffer = new StringBuffer("package ");
		buffer.append(packag).append(";\n\n");
		buffer.append("public class ").append(clazz).append(" extends ").append(extend).append(" {\n");
		buffer.append("\t").append("private static final long serialVersionUID = 1L;\n\n");

		File f = new File(file);
		if (!f.exists()) {
			System.out.println("指定的文件不存在：" + file);
			return;
		}
		if (f.isDirectory()) {
			System.out.println("参数file指定的是一个目录：" + file);
			return;
		}

		List<String> list = read(f, "UTF-8");
		String string = analysis(list, extend);

		buffer.append(string).append("\n}");
		String name = clazz + ".java";
		String fullPath = fullPath(path, name);

		// TODO 写文件
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(fullPath)))) {
			bos.write(buffer.toString().getBytes("UTF-8"));
		}
		System.err.println("文件已生成：" + fullPath);
	}

	private static String analysis(List<String> list, String extend) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		Map<String, String> map = new HashMap<>();
		for (String line : list) {
			line = line.trim();
			if (line.isEmpty())
				continue;

			String[] args = line.split(REGEX);
			if (args == null || args.length == 0)
				continue;
			String prefix = args[0].toLowerCase();
			if (prefix == null) {
				continue;
			}
			if (prefix.startsWith("`") || prefix.endsWith("`")) {
				prefix = prefix.replace("`", "");
			}
			if ("create".equals(prefix) || "primary".equals(prefix) || "unique".equals(prefix) || "key".equals(prefix)
					|| ")".equals(prefix) || ")engine".equals(prefix) || ")engine=".equals(prefix)
					|| ")engine=innodb".equals(prefix)) {
				continue;
			}
			if ("BaseDTO".equals(extend)
					&& ("id".equals(prefix) || "create_time".equals(prefix) || "update_time".equals(prefix))) {
				continue;
			}
			if ("TransBase".equals(extend) && ("id".equals(prefix) || "create_time".equals(prefix)
					|| "update_time".equals(prefix) || "server_id".equals(prefix) || "log_id".equals(prefix)
					|| "txn_no".equals(prefix) || "txn_date".equals(prefix) || "txn_time".equals(prefix)
					|| "status".equals(prefix) || "ret_code".equals(prefix) || "ret_msg".equals(prefix))) {
				continue;
			}

			System.out.println("args[0] = " + prefix);
			prefix = sqlToJava(prefix);
			String type = args[1];
			if (type == null || type.isEmpty()) {
				type = "varchar";
			} else {
				type = type.toLowerCase();
			}
			if (type.contains("varchar")) {
				type = ENUMS.get(prefix);
				if (type == null) {
					type = "String";
				}
			} else if (type.contains("decimal")) {
				type = "BigDecimal";
			} else if (type.contains("bigint")) {
				type = "long";
			} else if (type.contains("int")) {
				type = "int";
			} else if (type.contains("datetime")) {
				type = "Date";
			}

			String comment = line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"));

			map.put(prefix, type);
			buffer.append("\t/** ").append(comment).append(" */\n");
			buffer.append("\t").append("private ").append(type).append(" ").append(prefix).append(";\n");
		}
		buffer.append("\n");

		for (String field : map.keySet()) {
			String kkk = String.format("%s%s", Character.toUpperCase(field.charAt(0)), field.substring(1));
			buffer.append("\tpublic ").append(map.get(field)).append(" get").append(kkk).append("() {\n");
			buffer.append("\t\treturn ").append(field).append(";\n").append("\t}\n\n");
			buffer.append("\tpublic void set").append(kkk).append("(").append(map.get(field)).append(" ").append(field)
					.append(") {\n");
			buffer.append("\t\tthis.").append(field).append(" = ").append(field).append(";\n").append("\t}\n\n");
		}

		return buffer.toString();
	}

	/**
	 * SQL风格的字段名(下划线分割)转换成Java风格的字段名(驼峰形式)
	 * 
	 * @param field
	 *            需要转换的字段值
	 * @return 转换后的字段值
	 */
	private static String sqlToJava(String field) {
		String[] a = field.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer(a[0]);
		for (int i = 1; i < a.length; i++) {
			String b = a[i];
			if (b.length() == 0) {
				continue;
			}
			sb.append(Character.toUpperCase(b.charAt(0)));
			sb.append(b.substring(1));
		}
		return sb.toString();
	}

	public static void start() throws Exception {
		String sql = "";
		String packag = "";
		String path = "";
		String clazz = "";
		String extend = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("请输入源SQL文件全路径：");
			sql = br.readLine();
			if (sql == null || sql.isEmpty()) {
				System.err.println("输入的源SQL文件全路径有误！");
				continue;
			}
			break;
		}
		while (true) {
			System.out.println("请输入需要创建的Java类名：");
			clazz = br.readLine();
			if (clazz == null || clazz.isEmpty()) {
				System.err.println("输入的需要创建的Java类名有误！");
				continue;
			}
			break;
		}
		while (true) {
			System.out.println("请输入需要继承的Java类名：");
			extend = br.readLine();
			if (extend == null || extend.isEmpty()) {
				System.err.println("输入的需要继承的Java类名有误！");
				continue;
			}
			break;
		}
		while (true) {
			System.out.println("请输入生成的Java类的包路径：");
			packag = br.readLine();
			if (packag == null || packag.isEmpty()) {
				System.err.println("输入的生成的Java类的包路径有误！");
				continue;
			}
			break;
		}
		while (true) {
			System.out.println("请输入生成的Java类的存放路径：");
			path = br.readLine();
			if (path == null || path.isEmpty()) {
				System.err.println("输入的生成的Java类的存放路径有误！");
				continue;
			}
			break;
		}

		generate(packag, clazz, extend, path, sql);
	}

	public static void main(String[] args) throws Exception {
		String sql = "C:\\Users\\yoke7\\Desktop\\t_contract_entrust.sql";
		String clazz = "CheckDateModel";
		String packag = "com.kayak.api.model.contract";
		String path = "C:\\Users\\yoke7\\Desktop";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// while (true) {
		// System.out.println("请输入源SQL文件全路径：");
		// sql = br.readLine();
		// if (sql == null || sql.isEmpty()) {
		// System.err.println("输入的源SQL文件全路径有误！");
		// continue;
		// }
		// break;
		// }

		while (true) {
			System.out.println("请输入需要创建的Java类名：");
			clazz = br.readLine();
			if (clazz == null || clazz.isEmpty()) {
				System.err.println("输入的需要创建的Java类名有误！");
				continue;
			}
			break;
		}
		String extend = "BaseDTO";
		while (true) {
			System.out.println("请输入需要继承的Java类名：");
			extend = br.readLine();
			if (extend == null || extend.isEmpty()) {
				System.err.println("输入的需要继承的Java类名有误！");
				continue;
			}
			break;
		}
		// while (true) {
		// System.out.println("请输入生成的Java类的包路径：");
		// packag = br.readLine();
		// if (packag == null || packag.isEmpty()) {
		// System.err.println("输入的生成的Java类的包路径有误！");
		// continue;
		// }
		// break;
		// }
		// while (true) {
		// System.out.println("请输入生成的Java类的存放路径：");
		// path = br.readLine();
		// if (path == null || path.isEmpty()) {
		// System.err.println("输入的生成的Java类的存放路径有误！");
		// continue;
		// }
		// break;
		// }

		generate(packag, clazz, extend, path, sql);
	}

	// private static List<String> read(String name, String charset) throws
	// Exception {
	// File file = new File(name);
	// return read(file, charset);
	// }

	private static List<String> read(File file, String charset) throws Exception {
		if (!file.exists())
			return null;
		if (!file.isFile())
			return null;
		try (LineNumberReader reader = new LineNumberReader(
				new InputStreamReader(new FileInputStream(file), charset))) {
			return read(reader);
		} catch (Exception e) {
			throw new Exception("读取文件[" + file.getName() + "]异常", e);
		}
	}

	private static List<String> read(LineNumberReader reader) throws IOException {
		List<String> list = new ArrayList<>();
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			line = line.trim();
			if (line.isEmpty())
				continue;
			list.add(line);
		}
		return list;
	}

	/**
	 * 获取文件全路径，使用{@code File.separator}
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名称
	 * @return String 全路径
	 */
	private static String fullPath(String path, String name) {
		if (path == null || path.isEmpty())
			return name;
		if (name == null || name.isEmpty())
			return null;

		if (path.endsWith(File.separator))
			return String.format("%s%s", path, name);
		else
			return String.format("%s%s%s", path, File.separator, name);
	}

}
