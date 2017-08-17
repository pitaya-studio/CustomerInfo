package com.trekiz.admin.common.report.template.web;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.report.template.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */

@Controller
@RequestMapping(value = "${adminPath}/report/template")
public class TemplateDaoController {

    @Autowired
    private ITemplateService templateService;

    /**
     * 报表定义列表
     * @return
     */
    @RequestMapping(value = "list")
    public String getReportList(Model model) {
        Page<Map<String, Object>> list = templateService.getReportList();
        model.addAttribute("list", list);
        return "";
    }
}
