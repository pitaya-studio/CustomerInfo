package com.trekiz.admin.modules.message.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.HttpClientUtils;

/**
 * @ClassName: HttpClientService
 * @Description: 处理message远程调用
 * @author 修改为自己的名字
 * @date 2015-2-10 下午6:07:20
 * 
 */
@Service
@Transactional(readOnly = true)
public class HttpClientService extends BaseService {
    public static void main(String[] args) throws IOException {
        HttpClientService hc= new HttpClientService();
        hc.sendMsgAccountToRemote(111L, 1, 2);

    }

    /**
     * sendMsgAccountToRemote(对用户发送请求) (这里描述这个方法适用条件 – 可选)
     * 
     * @param userId
     * @param remoteCount
     * @param msgType     1：全站公告；2：部门公告;3：渠道公告; 4：约签公告；5:消息；
     *            返回类型 void
     * @exception
     * @since 1.0.0
     */
    public void sendMsgAccountToRemote(Long userId, int remoteCount, int msgType) {

        
        try {
            String baseurl=Global.getMessageConfig("");
            
            if( StringUtils.isEmpty(baseurl)){
                baseurl+="http://192.168.1.151/im";
            }
            String url = baseurl + "/msg/" + userId + "/" + remoteCount + "_"+msgType;
            String body = HttpClientUtils.get(url);
            JSONObject returnObj= new JSONObject(body);
            if((Boolean)returnObj.get("ifSuccess")){
                logger.info("发送用户（"+userId+"）信息成功,count("+remoteCount+"),msgType("+msgType+")");
            }else{
                logger.error("发送用户（"+userId+"）信息错误,count("+remoteCount+"),msgType("+msgType+")"); 
            }
        } catch (JSONException e) {
            logger.error("发送用户消息异常",e);
        }
    }

}
