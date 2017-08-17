package com.trekiz.admin.modules.distribution.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 短网址生成工具
 * @author: yang.gao
 * @Time 2017-1-4
 */
public class ShortUrlUtils {

    /**
     * 应用百度接口api生成短链接
     * 缺点：域名固定 如： http://dwz.cn/******
     * 生成短连接信息
     */
    public static String getShortUrlByBaiduApi(String url) {
    	// 短链接
    	String shortUrl = "";
    	// 百度获取短链接接口
    	String baiduUrl = "http://dwz.cn/create.php";
    	Map<String,String> map = new HashMap<String, String>();
    	// 设置请求参数
    	map.put("url", url);
        try {
			JSONObject jsonObj = HttpUtils.doPostByMap(baiduUrl, map, "UTF-8");
			// 通过接口中的key=tinyurl获取值
			shortUrl = jsonObj.get("tinyurl").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shortUrl;
    }
    
    /**
     * 应用微信api生成短链接
     *
     * @param accessToken 调用接口凭证
     * @param longUrl 需要转换的长链接  
     * 注： 请求成功后会返回如下信息               {"errcode":0, "errmsg":"ok", "short_url":"http:XXXX"}
     *    错误代码查看微信短链接开发文档：https://mp.weixin.qq.com/wiki/10/165c9b15eddcfbd8699ac12b0bd89ae6.html
     * @return 
     */
    public static JSONObject getShortUrlByWeiXinApi(String accessToken, String longUrl) {
        // 微信短链接接口
        String weixinUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=" + accessToken;
        JSONObject jsonObj = null;
        JSONObject params = new JSONObject();
        // 需要转换的长链接
		params.put("long_url", longUrl);
		// 代表长链接转短链接
		params.put("action", "long2short");
        try {
			jsonObj = HttpUtils.doPostByJson(weixinUrl, params, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
 
        return jsonObj;
    }
    
    public static void main(String[] args) {
    	System.out.println(getShortUrlByBaiduApi("https://www.baidu.com"));
	}
}