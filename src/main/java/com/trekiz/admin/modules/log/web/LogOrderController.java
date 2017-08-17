package com.trekiz.admin.modules.log.web;

import com.trekiz.admin.modules.log.service.LogOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zong on 2016/7/26.
 */
@Controller
@RequestMapping(value = "${adminPath}/log/order")
public class LogOrderController {
    @Autowired
    private LogOrderService logOrderService;

    @RequestMapping(value = "list")
    public String getLogOrderList(Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        List<Map<String, Object>> logOrderList = logOrderService.getLogOrderList(orderId);
        model.addAttribute("logOrderList", logOrderList);
        return "modules/visa/modifyDetail";
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "listByCreateDate")
    public Map<String, Object> getLogOrderListByCreateDate(Long orderId, String createDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> logOrderListByCreateDate = logOrderService.getLogOrderListByCreateDate(orderId, createDate);
            map.put("result", "success");
            map.put("list", logOrderListByCreateDate);
            map.put("message", "获取数据成功！");
        } catch (Exception e) {
            map.put("result", "success");
            map.put("message", "获取数据失败！");
        }
        return map;
    }
}
