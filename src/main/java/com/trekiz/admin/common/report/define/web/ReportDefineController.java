package com.trekiz.admin.common.report.define.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.report.define.entity.ReportDefine;
import com.trekiz.admin.common.report.define.service.IReportDefineService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * Created by zzk on 2016/4/25.
 */

@Controller
@RequestMapping(value = "${adminPath}/report/define")
public class ReportDefineController {

    @Autowired
    private IReportDefineService reportDefineService;

    /**
     * 报表定义列表
     *
     * @return
     */
    @RequestMapping(value = "list")
    public String getReportList(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(request, response);
            Page<Map<String, Object>> page = reportDefineService.getReportDefineList(pageParam);
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "report/define/reportDefineList";
    }

    /**
     * 删除报表定义
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public Map<String, Object> delete(Long id) {
        Map<String, Object> datas = new HashMap<String, Object>();
        try {
            reportDefineService.deleteReportDefine(id);
            datas.put("result", "success");
            datas.put("message", "删除成功！");
        } catch (Exception e) {
            datas.put("result", "fail");
            datas.put("message", "删除失败！");
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 添加或修改报表定义
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "addOrModifyReportDefine")
    public String addOrModifyReportDefine(Model model, Long id) {
        try {
            if (id != null) {
                ReportDefine reportDefine = reportDefineService.getReportDefineById(id);
                model.addAttribute("reportDefine", reportDefine);
            }
            List<Map<String, Object>> agentList = AgentInfoUtils.getAgentList(68L);
            model.addAttribute("templates", agentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "report/define/addOrModifyReportDefine";
    }

    @RequestMapping(value = "saveOrUpdateReportDefine")
    public String saveOrUpdateReportDefine(Model model, Long id, HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String templateId = request.getParameter("templateId");
            String sql = request.getParameter("sql");

            ReportDefine reportDefine;
            if (id == null) {
                reportDefine = new ReportDefine();
            } else {
                reportDefine = reportDefineService.getReportDefineById(id);
            }
            reportDefine.setName(name);
            reportDefine.setDescription(description);
            reportDefine.setTemplateId(Long.parseLong(templateId));
            reportDefine.setReportQuery(sql);
            reportDefine.setCompanyId(UserUtils.getUser().getCompany().getId());
            reportDefineService.saveOrUpdateReportDefine(reportDefine);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "redirect:" + Global.getAdminPath() + "/report/define/list";
    }

    @RequestMapping(value = "preview")
    public String preview(Model model, String sqls) {
        List<List<Map<String, Object>>> lists = reportDefineService.preview(sqls);
        model.addAttribute("lists", lists);

        Gson gson = new Gson();
        String json = gson.toJson(lists);
        System.out.println(json);

        return "report/define/preview";
    }
}
