package com.trekiz.admin.modules.distribution.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * httpClient工具类
 * @author: yang.gao
 * @Time 2017-1-17
 */
public class HttpUtils {
	
	 /**
     * 默认请求参数
     */
	private static int CONNECTION_REQUEST_TIMEOUT = 3000; // 请求超时时间
    private static int CONNECT_TIMEOUT = 3000; // 链接超时时间
    private static int SOCKET_TIMEOUT = 3000; // socket超时时间
    private static RequestConfig requestConfig; // 该类用来设置请求消息和外部网络环境
    private static boolean IS_SET = false; // 是否设置参数开关  默认关闭
    
	 /**
      * 通过post方法发送请求(参数为json格式)
      * 注：获取方式为读取字符流的形式获取响应数据  代码如：request.getInputStream()
      * @param url        要请求的url
      * @param jsonParams 传送的参数(json格式)
      * @param code 	      传送的参数编码如：UTF-8
      * @return json对象
      */
	public static JSONObject doPostByJson(String url, JSONObject jsonParams, String code) {
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建json对象
		JSONObject jsonObject = null;
	    try {
	    	// 建立HttpPost对象
	    	HttpPost post = new HttpPost(url);
	    	// 设置请求参数
	    	if (IS_SET) {
	    		setRequestParams(post);
	    	}
	    	// 设置参数属性
	    	StringEntity se = new StringEntity(jsonParams.toString());
	    	// 设置发送编码
	    	se.setContentEncoding(code);
	    	// 发送json数据需要设置contentType
	    	se.setContentType("application/json");
	    	// 设置响应数据
			post.setEntity(se);
			jsonObject = executeRequest(httpClient, post, code);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	/**
     * 通过post方法发送请求(参数为Map集合)
     * 注：通过key的形式获取数据 代码如： request.getParameter("key")
     * @param url       要请求的url
     * @param mapParams 传送的参数(Map集合)
     * @param code 		传送的参数编码如：UTF-8
     * @return json对象
     */
	public static JSONObject doPostByMap(String url, Map<String, String> mapParams, String code) {
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建json对象
		JSONObject jsonObject = null;
		try {
			// 建立HttpPost对象
			HttpPost post = new HttpPost(url);
			// 设置请求参数
			if (IS_SET) {
				setRequestParams(post);
	    	}
		    // 建立一个NameValuePair数组，用于存储欲传送的参数
		    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		    if (mapParams != null) {
		    	// 遍历参数Map
			    for (Map.Entry<String, String> entry : mapParams.entrySet()) {
			    	// 添加请求参数
			    	nvps.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
		    	}
		    }
	    	// 设置请求数据及编码
	    	post.setEntity(new UrlEncodedFormEntity(nvps, code));
	    	// 执行请求并返回json对象
			jsonObject = executeRequest(httpClient, post, code);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	/**
     * 通过get方法发送请求(参数为Map集合)
     * @param url       要请求的url(可带参数)
     * @param code 		传送的参数编码如：UTF-8
     * @return json对象
     */
	public static JSONObject doGetByMap(String url, String code) {
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 建立HttpPost对象
		HttpGet get = new HttpGet(url);
		// 设置请求参数
		if (IS_SET) {
			setRequestParams(get);
    	}
		// 执行请求并返回json对象
		JSONObject jsonObject = executeRequest(httpClient, get, code);

		return jsonObject;
	}

	/**
     * 执行请求并返回响应数据
     * @param httpclient  CloseableHttpClient实例
     * @param hur 		      请求类型httpPost/httpGet
     * @param code		      响应数据的编码 如(UTF-8)
     * @return json对象
     */
	private static JSONObject executeRequest(CloseableHttpClient httpclient, HttpRequestBase hur, String code) {
		JSONObject jsonObject = null;
		try {
			// 发送get请求,并返回一个CloseableHttpResponse对象
			CloseableHttpResponse response = httpclient.execute(hur);
			// 执行成功
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String jsonStr = EntityUtils.toString(response.getEntity(), code);
				// 将字符串转换成json对象
				jsonObject = JSONObject.fromObject(jsonStr.replace("\\",""));
			}
			// 关闭流
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
	
	/**
     * 设置请求参数
     * @return RequestConfig对象
     */
	private static void setRequestParams(HttpRequestBase hur) {
		// 设置请求参数
		if (requestConfig == null) {
			// 设置请求参数
			requestConfig = RequestConfig.custom()
			        .setConnectTimeout(CONNECT_TIMEOUT) // 链接超时时间
			        .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 请求超时时间
			        .setSocketTimeout(SOCKET_TIMEOUT) // socket超时时间
			        .build();
		}
		hur.setConfig(requestConfig);
	}
}