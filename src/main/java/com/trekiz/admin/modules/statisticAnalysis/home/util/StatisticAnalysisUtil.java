package com.trekiz.admin.modules.statisticAnalysis.home.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by quauq on 2016/12/23.
 */
public class StatisticAnalysisUtil {

    /**
     * 参数处理：用request取值、放入条件map
     * @param paras 参数按逗号分隔
     * @param mapRequest
     * @param request
     */
    public static void handlePara(String paras, Map<String,String> mapRequest, HttpServletRequest request) {
        if (StringUtils.isNotBlank(paras)) {
            String common = "";
            for(String para : paras.split(",")) {
                common = request.getParameter(para);
                if(common != null) {
                    common = common.trim().replace("'", "");
                    common = common.replace("\\", "\\\\\\\\");
                    mapRequest.put(para, common);
                } else {
                    mapRequest.put(para, common);
                }
            }
        }
    }

}
