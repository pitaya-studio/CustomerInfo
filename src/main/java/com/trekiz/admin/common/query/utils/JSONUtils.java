package com.trekiz.admin.common.query.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 主要做JSON解析使用
 * @author shijun.liu
 * @date    2016.04.08
 */
public class JSONUtils {

    /**
     * 将JAVA对象转换为JSON对象
     * @param object        JAVA对象
     * @return
     * @author  shijun.liu
     * @date    2016.04.08
     */
    public static final String parseToJson(Object object){
        if(null == object){
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    
    /**
     * 将实体POJO转化为JSON
     * @author yang.jiang 2016-5-13 20:02:30
     * @param obj
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static<T> JSONObject objectToJson(T obj) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper();  
        // Convert object to JSON string  
        String jsonStr = "";
        try {
             jsonStr =  mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw e;
        }
        return new JSONObject(jsonStr);
    }

	/**
     * Map<String, String> --> JSONArray
     * @param salerIdArray
     * @return
     */
	public static org.json.JSONArray map2JsonArray(Map<String, String> paramMap) {
		if (paramMap == null) {
			return null;
		}
		org.json.JSONArray resultArr = new org.json.JSONArray();
		Set<String> keys = paramMap.keySet();
		for (String key : keys) {
			org.json.JSONObject resultObj = new org.json.JSONObject();
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("key", key);
			tempMap.put("value", paramMap.get(key));
			try {
				resultObj = objectToJson(tempMap);
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
			resultArr.put(resultObj);
		}
		return resultArr;
	}
    
}
