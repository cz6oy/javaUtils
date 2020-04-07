package com.kayakwise.gray.api.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpPostClient {
	private static final Logger log = LoggerFactory.getLogger(HttpPostClient.class);
	// 接口地址
	private CloseableHttpClient httpClient = null;
	private HttpPost method = null;
	private int status = 0;

	public HttpPostClient(String URL) {
		if (URL != null && !URL.isEmpty()) {
			log.debug(URL);
			httpClient = HttpClients.createDefault();
			method = new HttpPost(URL);
		}
	}

	public String post(String jsonStr){
		
		log.debug("发送业务报文:\r\n" + JsonFormatUtil.formatJson(jsonStr));
		try {
			method.addHeader("Content-type", "application/json;charset=UTF-8");
			//method.setHeader("Accept", "text/html");
			method.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
			//method.addHeader("signdate", signdata);
			
			HttpResponse response = httpClient.execute(method);

			int statusCode = response.getStatusLine().getStatusCode();
			log.debug("statusCode:" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				log.debug("HTTP通讯错误:" + response.getStatusLine());
				status = 1;
				return null;
			}
			
			// Read the response body
			String retStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			if(retStr != null){
				log.debug("响应原文:\n"+retStr);
				log.debug("接收JSON报文:\r\n" + JsonFormatUtil.formatJson(retStr));
				return retStr;
			}else{
				log.debug("接收JSON报文为空");
				return null;
			}
			
		} catch (IOException e) {
			// 网络错误
			status = 3;
			log.debug("网络错误");
			return null;
		} finally {
			log.debug("状态：" + status);
		}
	}
	
	public String post(Object request) {
		if (request == null ) {
			return null;
		}
		
		String jsonStr = JSON.toJSONString(request);

		log.debug("发送业务报文:\r\n" + JsonFormatUtil.formatJson(jsonStr));
		try {
			method.addHeader("Content-type", "application/json;charset=UTF-8");
			//method.setHeader("Accept", "text/html");
			method.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
			//method.addHeader("signdate", signdata);
			
			HttpResponse response = httpClient.execute(method);

			int statusCode = response.getStatusLine().getStatusCode();
			log.debug("statusCode:" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				log.debug("HTTP通讯错误:" + response.getStatusLine());
				status = 1;
				return null;
			}
			
			// Read the response body
			String retStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			if(retStr != null){
				log.debug("响应原文:\n"+retStr);
				log.debug("接收JSON报文:\r\n" + JsonFormatUtil.formatJson(retStr));
				return retStr;
			}else{
				log.debug("接收JSON报文为空");
				return null;
			}
			
		} catch (IOException e) {
			// 网络错误
			status = 3;
			log.debug("网络错误");
			return null;
		} finally {
			log.debug("状态：" + status);
		}
	}
	
	private static int i = 0;
	
	@SuppressWarnings("unused")
	private static synchronized String getSerialNo(int max){
		i ++;
		String s = String.valueOf(max);
		int len = s.length();
		if(i > max){
			return String.valueOf(System.currentTimeMillis()) + String.format("%0" + len + "d", 1);
		}else{
			return String.valueOf(System.currentTimeMillis()) + String.format("%0" + len + "d", i);
		}
	}
	

}
