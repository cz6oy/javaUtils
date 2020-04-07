package com.kayakwise.gray.api.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayakwise.frame.Gray;
import com.kayakwise.frame.fixinal.FixMsgTransfer;
import com.kayakwise.frame.service.SysParamService;
import com.kayakwise.gray.api.code.ErrorCode;
import com.kayakwise.gray.api.exception.BusiErrorException;


public class SocketUtils {
	private static final Logger log = LoggerFactory.getLogger(SocketUtils.class);

	
	public static <T> T sendStocket(Object req,Class<T> rep) {
		
		SysParamService sps = Gray.get(SysParamService.class);
    	
		
		String sendMsg="";
		 try{
			    sendMsg=new String(FixMsgTransfer.packFixByteRespAdv(req));
			    log.info("请求核心请求报文:"+sendMsg);
			    String ip = sps.getParamVal("hostip");
				int port  =Integer.parseInt(sps.getParamVal("hostport"));
				int timeout  =Integer.parseInt(sps.getParamVal("hosttimeout"));
				String recv=send(sendMsg, timeout, ip, port);
	            log.info("请求核心时返回的报文:"+recv);
	        	T unpackFixReqAdv = FixMsgTransfer.unpackFixReqAdv(rep, recv,null);
	        	unpackFixReqAdv= (T)MsgUtil.format(unpackFixReqAdv);
	        	return unpackFixReqAdv;
		 	}catch(Exception e) {
		 		e.printStackTrace();
		 		log.info("can not listen to:"+e);//出错，打印出错信息
		 		return null;
	        }
		
		
	} 
//    public static String send(String msg) throws Exception, IOException {
//    	SysParamService sps = Gray.get(SysParamService.class);
//    	String ip = sps.getParamVal("hostip");
//		int port  =Integer.parseInt(sps.getParamVal("hostport"));
//		int timeout  =Integer.parseInt(sps.getParamVal("hosttimeout"));
//		return send(msg, timeout, ip, port, 0);
//    }
    /**
     * 
     * @param msg
     * @param timeout
     * @param ip
     * @param port
     * @param nextPack
     *                后继包标志 =0不判断 >0取第nextPack位开始1位 <0取倒数第nextPack位开始1位
     * @return
     * @throws Exception
     * @throws IOException
     */
    public static String send(String msg, int timeout, String ip, int port) throws Exception, IOException {
	Socket socket = null;
	OutputStream o = null;
	InputStreamReader is = null;
	if (ip == null || ip.equals("") || port <= 0)
	    throw new Exception("ip或port错误！");

	log.info("in SocketClient:send ip=" + ip + " port=" + port);

	
	String responseStr = new String("");
	try {
	    log.info("in SocketClient:send Start Connect!");
	    socket = new Socket(ip, port);
//	    is = new InputStreamReader(socket.getInputStream());

	    o = socket.getOutputStream();

	    log.info("in SocketClient:send Start SendData!");
	    int len = msg.getBytes("GBK").length;
	    log.info("发送的报文长度="+len);
	    System.out.println(StringUtil.lpad(String.valueOf(len), 6, '0'));
        o.write(StringUtil.lpad(String.valueOf(len), 6, '0').getBytes());
        o.flush();
	    o.write((msg).getBytes("GBK"));
	    log.info("socket发送的报文="+msg);
	    o.flush();

	    log.info("in SocketClient:send Start RecvData!");
	   
//	    byte[] len1 = new byte[6];
	    byte[] buffer = new byte[4096];
	    int chars_read;
	 
	    if (timeout <= 0)
		timeout = 10000;

	    while (true) {
		socket.setSoTimeout(timeout);
//		socket.getInputStream().read(len1);
//		byte[] buffer = new byte[Integer.parseInt(new String(len1,"GBK"))];
//		byte [] perRead=new byte[1024*4];
//		int count=0;
//		while(true){
//		int readNum= socket.getInputStream().read(perRead);
//		if(readNum<=0){
//			break;
//		}
//		
//		System.arraycopy(perRead, 0, buffer, count, readNum);
//		count+=readNum;
//		chars_read+=readNum;
//		}
		
		chars_read = socket.getInputStream().read(buffer);
		
		log.info("in SocketClient:send RecvData Success! charsRead="
			+ chars_read);

		if (chars_read <= 0)
		    break;

		String tmp = new String(buffer,0,chars_read,"GBK");
		log.info("in SocketClient:send responseStr=" + tmp);

		responseStr += tmp;

		
	    }
	} catch (IOException ex) {
	    ex.printStackTrace();
	    throw ex;
		} finally {
			try {
				if (is != null) is.close();
			} catch (Exception ex) {
				log.info("is异常:" + ex.getMessage());
			}
			try {
				socket.close();
			} catch (Exception ex) {
				log.info("socket异常:" + ex.getMessage());
			}
		}
	return responseStr.substring(6);
    }
}
