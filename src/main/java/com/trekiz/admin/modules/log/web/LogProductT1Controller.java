package com.trekiz.admin.modules.log.web;

import com.trekiz.admin.modules.log.service.LogProductT1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zong on 2016/7/26.
 */
@Controller
@RequestMapping(value = "${adminPath}/log/productT1")
public class LogProductT1Controller {
    @Autowired
    private LogProductT1Service logProductT1Service;

    @RequestMapping(value = "save")
    public void saveLogProductT1(Long groupId) {
        logProductT1Service.saveLogProductT1(groupId);
    }
}
