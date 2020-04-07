package com.kayakwise.gray.api.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理相关工具类
 * 
 * @author leewc
 * @since 2018-05-08 12:01 v1.0
 * @version 1.0
 */
public class FileUtils {
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	/**
	 * 获取文件全路径，使用{@code File.separator}
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名称
	 * @return String 全路径
	 */
	public static String fullPath(String path, String name) {
		if (path == null || path.isEmpty())
			return name;
		if (name == null || name.isEmpty())
			return null;

		if (path.endsWith(File.separator))
			return String.format("%s%s", path, name);
		else
			return String.format("%s%s%s", path, File.separator, name);
	}

	/**
	 * 读取文件，按行读取，返回每行数据的集合
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名称
	 * @param charset
	 *            文件编码
	 * @return 文件数据集合
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> read(String path, String name, String charset)
			throws FileNotFoundException, IOException {
		return read(new File(fullPath(path, name)), charset);
	}

	/**
	 * 读取文件，按行读取，返回每行数据的集合
	 * 
	 * @param file
	 *            目标文件对象
	 * @param charset
	 *            文件编码
	 * @return 文件数据集合
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> read(File file, String charset) throws FileNotFoundException, IOException {
		if (!isFile(file)) {
			return null;
		}
		List<String> l = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				if (StringUtil.isNullOrEmpty(line.trim())) {
					continue;
				}
				l.add(line);
			}
		}
		return l;
	}

	/**
	 * 判断File对象是否为一个文件
	 * 
	 * @param file
	 *            需要判断的File对象
	 * @return 当file是文件是返回true，否则返回false
	 */
	public static boolean isFile(File file) {
		return file != null && file.isFile();
	}

	/**
	 * 将字符串内容写到文件中
	 * 
	 * @param str
	 *            待写的字符串
	 * @param charset
	 *            文件编码
	 * @param path
	 *            文件保存路径
	 * @param name
	 *            文件名
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void write(String str, String charset, String path, String name)
			throws FileNotFoundException, IOException {
		write(str, charset, path, name, false);
	}

	/**
	 * 将字符串内容写到文件中
	 * 
	 * @param str
	 *            待写的字符串
	 * @param charset
	 *            文件编码
	 * @param path
	 *            文件保存路径
	 * @param name
	 *            文件名
	 * @param append
	 *            是否追加方式
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void write(String str, String charset, String path, String name, boolean append)
			throws FileNotFoundException, IOException {
		if (str == null) {
			return;
		}
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fullPath(path, name)), charset))) {
			bw.write(str);
			bw.flush();
		}
	}

	/**
	 * 删除指定目录下的所有子目录和所有文件
	 * 
	 * @param directory
	 *            目录
	 * @return 全部删除成功返回true，否则false
	 */
	public static boolean delete(String directory) {
		if (directory == null) {
			return true;
		}
		return delete(new File(directory));
	}

	/**
	 * 删除指定目录下的所有子目录和所有文件
	 * 
	 * @param directory
	 *            目录
	 * @return 全部删除成功返回true，否则false
	 */
	public static boolean delete(File f) {
		if (!f.exists()) {
			return true;
		}
		if (f.isDirectory()) {
			for (String child : f.list()) {
				if (!delete(new File(f, child))) {
					return false;
				}
			}
		}
		return f.delete();
	}

	// public static void main(String[] args) throws FileNotFoundException,
	// IOException {
	// String p = "C:\\Users\\yoke7\\Desktop\\我的桌面";
	// String n = "yillion_bak_20180625.sql";
	// List<String> list = read(p, n, "utf-8");
	// StringBuffer buffer = new StringBuffer();
	// list.stream().forEach(l -> buffer.append(l).append("\n"));
	// write(buffer.toString(), "utf-8", p, n + "_copy");
	// }

}
