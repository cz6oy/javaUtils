package com.kayakwise.gray.api.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

/**
 * SFTP连接操作相关工具类
 * 
 * @author leewc
 * @since 2018-05-08 14:45 v1.0
 * @version 1.0
 */
public class SftpUtils {

	/**
	 * 获取远程路径的指定文件
	 * 
	 * @param host
	 *            远程主机地址
	 * @param port
	 *            远程连接端口
	 * @param user
	 *            远程主机登录名
	 * @param passwd
	 *            远程主机登陆密码
	 * @param remote
	 *            远程路径
	 * @param local
	 *            本地存放路径
	 * @param file
	 *            远程文件名
	 * @return boolean true-获取成功；false-获取失败
	 * @throws Exception
	 */
	public static boolean get(String host, int port, String user, String passwd, String remote, String local,
			String file) throws Exception {
		Session session = null;
		ChannelSftp sftp = null;
		try {
			session = initSession(host, port, user, passwd);
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();

			sftp.get(FileUtils.fullPath(remote, file), FileUtils.fullPath(local, file));
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (sftp != null)
				sftp.quit();
			if (session != null)
				session.disconnect();
		}

	}

	/**
	 * 查询列出远程路径下所有文件信息
	 * 
	 * @param host
	 *            远程主机地址
	 * @param port
	 *            远程连接端口
	 * @param user
	 *            远程主机登录名
	 * @param passwd
	 *            远程主机登陆密码
	 * @param remote
	 *            远程路径
	 * @return List 远程路径下的文件信息
	 * @throws Exception
	 */
	public static List<LsEntry> ls(String host, int port, String user, String passwd, String remote) throws Exception {
		Session session = null;
		ChannelSftp sftp = null;
		try {
			session = initSession(host, port, user, passwd);
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();

			@SuppressWarnings("unchecked")
			Vector<LsEntry> entrys = sftp.ls(remote);

			List<LsEntry> files = new ArrayList<>();
			files.addAll(entrys);

			return files;
		} catch (Exception e) {
			throw e;
		} finally {
			if (sftp != null)
				sftp.quit();
			if (session != null)
				session.disconnect();
		}
	}

	/**
	 * 同步远程路径下的所有数据到本地指定目录
	 * 
	 * @param host
	 *            远程主机地址
	 * @param port
	 *            远程连接端口
	 * @param user
	 *            远程主机登录名
	 * @param passwd
	 *            远程主机登陆密码
	 * @param remote
	 *            远程路径
	 * @param local
	 *            本地存放路径
	 * @throws Exception
	 */
	public static void rsync(String host, int port, String user, String passwd, String remote, String local)
			throws Exception {
		Session session = null;
		ChannelSftp sftp = null;
		try {
			session = initSession(host, port, user, passwd);
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();

			rsync(sftp, remote, new File(local));
		} catch (Exception e) {
			throw e;
		} finally {
			if (sftp != null)
				sftp.quit();
			if (session != null)
				session.disconnect();
		}
	}

	private static Session initSession(String host, int port, String user, String password) throws JSchException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setPassword(password);
		session.setTimeout(10000);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		return session;
	}

	private static void rsync(ChannelSftp sftp, String remote, File local) throws Exception {
		@SuppressWarnings("unchecked")
		Vector<LsEntry> entrys = sftp.ls(remote);
		for (LsEntry entry : entrys) {
			String name = entry.getFilename();
			if (".".equals(name) || "..".equals(name))
				continue;

			File file = new File(local, name);

			SftpATTRS attrs = entry.getAttrs();
			if (attrs.isDir()) {
				if (!file.exists())
					file.mkdir();

				if (file.isDirectory()) {
					rsync(sftp, FileUtils.fullPath(remote, name), file);
				} else {
					file.delete();
					file.mkdir();
				}
			} else {
				if (!file.exists() || file.length() != attrs.getSize()
						|| file.lastModified() / 1000 < attrs.getMTime()) {
					sftp.get(FileUtils.fullPath(remote, name), file.getAbsolutePath());
				}
			}
		}
	}

	// public static void main(String[] args) throws Exception {
	// List<LsEntry> files = ls("acm.scu.edu.cn", 22, "kayak", "kk123456",
	// "install");
	// for (LsEntry e : files) {
	// System.out.printf("%s: %d, %d\n", e.getFilename(),
	// e.getAttrs().getSize(),
	// e.getAttrs().getMTime());
	// }
	//
	// get("acm.scu.edu.cn", 22, "kayak", "kk123456", "install", ".",
	// "nginx-1.12.1.tar.gz");
	//
	// // 天府银行
	// rsync("59.151.65.97", 8622, "cgnb_caiyg", "123456", "/",
	// "d:/temp/epcc/tianfu");
	// rsync("59.151.65.97", 9822, "cgnb_caiyg", "123456", "/",
	// "d:/temp/epcc/tianfu");
	//
	// // 宜宾
	// rsync("59.151.65.97", 8622, "ybshliu", "123456", "/",
	// "d:/temp/epcc/yibin");
	// rsync("59.151.65.97", 9822, "ybshliu", "123456", "/",
	// "d:/temp/epcc/yibin");
	//
	// sftp ybshliu@59.151.65.97 9822
	// }

}