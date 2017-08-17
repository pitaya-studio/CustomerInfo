package com.trekiz.admin.modules.sys.utils;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.sys.entity.SysJobNew;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.review.configuration.config.ReviewContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * 职务工具类
 * @author yanzhenxing
 * @date 2015/11/27
 */
public class JobUtil {

    private static SystemService systemService= SpringContextHolder.getBean(SystemService.class);

    /**
     * 根据id获取职务名称
     * @param id
     * @return
     */
    public static String getJobName(String id){
//        Assert.hasText(id,"id should not be empty!");
        if (StringUtils.isBlank(id)){
            return "";
        }
        //返回条件审批的默认职务名称
        if (ReviewContext.DEFAULT_CONDITION_JOB_ID.equals(id)){
            return ReviewContext.DEFAULT_CONDITION_JOB_NAME;
        }
        try {
            Long idL=Long.parseLong(id);
            SysJobNew job=systemService.findSysJobNewById(idL);
            if (null==job){
                return "";
            }else{
                return job.getName();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }
}
