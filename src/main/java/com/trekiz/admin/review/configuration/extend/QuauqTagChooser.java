package com.trekiz.admin.review.configuration.extend;

import com.quauq.review.core.extend.activitytag.TagChooser;
import com.trekiz.admin.modules.sys.service.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签选择器，用于用户执行审批操作时，当前用户作为什么职务进行审批
 * @author yanzhenxing
 * @date 2015/12/8
 */
public class QuauqTagChooser implements TagChooser{

    @Autowired
    private SystemService systemService;

    @Override
    public List<String> chooseTag(String userId, List<String> tags) {
        List<String> resultTags=new ArrayList<>();
        if (StringUtils.isBlank(userId)||tags==null||tags.isEmpty()) return resultTags;
        if (tags.size()==1) return tags;//如果只有一个标签参数，直接返回
        //获取当前用户的所有职务
        List<String> jobs=systemService.findUserJobsByUserId(userId);
        if (jobs==null||jobs.isEmpty()) return tags;//如果当前用户身上没有职务，直接返回标签参数
        for (String job:jobs){
            if (tags.contains(job)) resultTags.add(job);
        }
        return resultTags;
    }
}
