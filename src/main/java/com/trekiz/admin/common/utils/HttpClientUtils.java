package com.trekiz.admin.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName: HttpClientUtils
 * @Description: 通过服务器获取远程请求
 * @author fujz
 * @date 2015-2-10 下午5:24:17
 * 
 */
@SuppressWarnings("deprecation")
public class HttpClientUtils {
    private static Logger log = Logger.getLogger(HttpClientUtils.class);

    /**
     * post(通过post方法发送请求)
     * @param url       要请求的url
     * @param params    传送的参数
     * @return
     * 返回类型 String
     * @exception
     * @since  1.0.0
     */
    public static String post(String url, Map<String, String> params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;

        log.info("create httppost:" + url);
        HttpPost post = postForm(url, params);

        body = invoke(httpclient, post);

        httpclient.getConnectionManager().shutdown();

        return body;
    }

    /**
     * get(通过get方法发送请求)
     * @param url  要请求的url
     * @return
     * 返回类型 String
     * @exception
     * @since  1.0.0
     */
    public static String get(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;

        log.info("create httppost:" + url);
        HttpGet get = new HttpGet(url);
        body = invoke(httpclient, get);

        httpclient.getConnectionManager().shutdown();

        return body;
    }

    /**
     * invoke(执行方法，并返回值)
     * ((注意：只在本类中使用)
     * @param httpclient   http客户端
     * @param httpost      post请求
     * @return
     * 返回类型 String
     * @exception
     * @since  1.0.0
     */
    private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {

        HttpResponse response = sendRequest(httpclient, httpost);
        String body = paseResponse(response);
        return body;
    }

    private static String paseResponse(HttpResponse response) {
        log.info("get response from http server..");
        HttpEntity entity = response.getEntity();

        log.info("response status: " + response.getStatusLine());
        String charset = EntityUtils.getContentCharSet(entity);
        log.info(charset);

        String body = null;
        try {
            body = EntityUtils.toString(entity);
            log.info(body);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    /**
     * sendRequest(发送请求)
     * (注意：只在本类中使用)
     * @param httpclient
     * @param httpost
     * @return
     * 返回类型 HttpResponse
     * @exception
     * @since  1.0.0
     */
    private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
        log.info("execute post...");
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * postForm(组装表单请求)
     * @param url    要请求的url
     * @param params 要传送的参数
     * @return
     * 返回类型 HttpPost
     * @exception
     * @since  1.0.0
     */
    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        if(params!=null){
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }  
        }
       

        try {
            log.info("set utf-8 form entity to httppost");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }
    
    public static void main(String[] args){
      String content=  HttpClientUtils.get("http://192.168.1.151:8080/im/");
       System.out.println("打印远程请求结果："+content);
    }
}
