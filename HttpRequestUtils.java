package com.kayakwise.gray.api.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.project.util.HttpMarvelUtils;

/**
 * @author tangxin
 * @date 2017年10月30日 下午9:50:01
 * 
 */
public class HttpRequestUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class); // 日志记录

	/**
	 * httpPost
	 * 
	 * @param url
	 *            路径
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam) {
		return httpPost(url, jsonParam, false);
	}

	public static String httpPost(String url, String jsonParam) throws Exception {
		return httpPost2(url, jsonParam, false);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
		// post请求返回结果
		CloseableHttpClient client = HttpClients.createDefault();
		JSONObject jsonResult = null;
		HttpPost method = new HttpPost(url);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/x-www-form-urlencoded");
				method.setEntity(entity);
			}
			CloseableHttpResponse result = client.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/** 把json字符串转换成json对象 **/
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
		}
		return jsonResult;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 * @throws Exception
	 */
	public static String httpPost2(String url, String jsonParam, boolean noNeedResponse) throws Exception {
		// post请求返回结果
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);
		String str = "";
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				// entity.setContentType("application/x-www-form-urlencoded");
				method.setEntity(entity);
			}
			HttpResponse result = client.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			logger.info("statusCode={}",result.getStatusLine().getStatusCode());
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/** 把json字符串转换成json对象 **/
					// jsonResult = JSONObject.parseObject(str);
			} 
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
			throw new Exception("post请求提交失败");
		}
		return str;
	}
	
	/**
	 * 超时
	 * @param url
	 * @param jsonParam
	 * @param noNeedResponse
	 * @return
	 * @throws Exception
	 */
	
	public static HttpResponse httpPost(String url, String jsonParam, boolean noNeedResponse) throws Exception {
		// post请求返回结果
		CloseableHttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).build();
		HttpPost method = new HttpPost(url);
		method.setConfig(requestConfig);
		HttpResponse result;
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				// entity.setContentType("application/x-www-form-urlencoded");
				method.setEntity(entity);
			}
			 result = client.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
			throw new Exception("post请求提交失败");
		}
		return result;
	}

/*	public static <T> T httpPost3(String url, Object o, Class<T> clazz) throws Exception {
		logger.info("请求URL={}",url);
		String reqJson = JsonUtils.objectToJson(o);
		logger.info("请求报文={}",reqJson);
		String result = httpPost(url, reqJson);
		logger.info("返回报文={}",result);
		T t = JsonUtils.jsonToObject(clazz, result);
		return t;
	}*/
	
/*	public static <T> T httpPost3(String url, Object o, Class<T> clazz) throws Exception {
		HttpHeaders  headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		logger.info("请求URL={}",url);
		String reqJson = JsonUtils.objectToJson(o);
		logger.info("请求报文={}",reqJson);
		HttpEntity<String>  formEntity =new HttpEntity<String>(reqJson,headers);
		String result = IasRestTemplate.getRestTemplate().postForObject(url, formEntity, String.class);
		logger.info("返回报文={}",result);
		T t = JsonUtils.jsonToObject(clazz, result);
		
		return t;
	}*/
	
	public static <T> T httpPost3(String url, Object o, Class<T> clazz) throws SocketTimeoutException, ConnectException, UnknownHostException, IOException  {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("请求URL={}",url);
		String reqJson = JsonUtils.objectToJson(o);
		logger.info("请求报文={}",reqJson);
		T t = HttpMarvelUtils.restTemplate(url, reqJson, clazz);
		logger.info("返回报文={}",JsonUtils.objectToJson(t));
		stopWatch.stop();
		logger.info("交易["+url+"]耗时:"+stopWatch.getTotalTimeMillis()+"毫秒,"+stopWatch.getTotalTimeSeconds()+"秒");
		return t;
	}
	

	
	/**
	 * 发送大成基金专用
	 * @param url
	 * @param o
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
/*	public static <T> T httpPostDC(String url, Object o, Class<T> clazz) throws Exception{
		String objectToJsonDC = JsonUtils.objectToJsonDC(o);
		logger.info("请求json报文:{}",objectToJsonDC);
		String result = httpPost(url, JsonUtils.objectToJsonDC(o));
		logger.info("返回json报文:{}",result);
		T t = JsonUtils.jsonToObjectDC(clazz, result);
		return t;
	}*/
	
	/**
	 * 发送大成专用
	 * @param url
	 * @param o
	 * @param clazz
	 * @param claz
	 * @return
	 * @throws Exception
	 */
	public static <T, E> T httpPostDC(String url, Object o, Class<T> clazz, Class<E> claz) throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("请求URL={}",url);
		String reqJson = JsonUtils.objectToJsonDC(o);
		logger.info("请求json报文:{}",reqJson);
		String result = httpPost(url, reqJson);
		logger.info("响应json报文:{}",result);
		
		stopWatch.stop();
		logger.info("交易["+url+"]耗时:"+stopWatch.getTotalTimeMillis()+"毫秒,"+stopWatch.getTotalTimeSeconds()+"秒");
		JSONObject jsonObjectLast = new JSONObject();
		JSONObject jsonObject = JsonUtils.toJSONObject(result);
		jsonObjectLast.put("retcode", (String) jsonObject.get("retcode"));
		jsonObjectLast.put("retmsg", (String) jsonObject.get("retmsg"));
		JSONObject jSONObj = (JSONObject) jsonObject.get("retdata");
		JSONArray jSONArray = null;
		Field declaredField = null;

		
		String listname = null;
		if(jSONObj!=null){
			if (jSONObj.containsKey("inputlist")) {
				listname = "inputlist";
			} else if (jSONObj.containsKey("resutlist")) {
				listname = "resutlist";
			} else if (jSONObj.containsKey("assetlist")) {
				listname = "assetlist";
			}
			jsonObjectLast.putAll(jSONObj);
		}
		if(listname != null){
			jSONArray = (JSONArray) jSONObj.get(listname);
			jSONObj.remove(listname);
			declaredField = clazz.getDeclaredField(listname);
			declaredField.setAccessible(true);
		}

		T t = JsonUtils.jsonToObject(clazz, jsonObjectLast.toJSONString());
		List<E> list1 = new ArrayList<E>();
		if (jSONArray != null) {
			for (Object object : jSONArray) {
				JSONObject jsonobj = (JSONObject) object;
				E jsonToObject = JsonUtils.jsonToObject(claz, jsonobj.toJSONString());
				list1.add(jsonToObject);
			}
			declaredField.set(t, list1);
		}

		return t;

	}
	
	
	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            路径
	 * @return
	 */
	public static JSONObject httpGet(String url) {
		// get请求返回结果
		JSONObject jsonResult = null;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				String strResult = EntityUtils.toString(response.getEntity());
				/** 把json字符串转换成json对象 **/
				jsonResult = JSONObject.parseObject(strResult);
				url = URLDecoder.decode(url, "UTF-8");
			} else {
				logger.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		}

		return jsonResult;
	}

	public static ByteArrayOutputStream httpGetToStream(String url) throws Exception {
		InputStream input = null;
		CloseableHttpClient client = null;
		try {
			client = HttpClients.createDefault();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			input = response.getEntity().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len;
			while ((len = input.read(buff)) > -1) {
				baos.write(buff, 0, len);
			}
			baos.flush();
			return baos;
		} catch (Exception e) {
			return null;
		} finally {
			input.close();
			client.close();
		}
	}

}
