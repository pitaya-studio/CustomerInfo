package com.trekiz.admin.modules.statisticAnalysis.home.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderTypesService;

import net.sf.json.JSONArray;
/**
 * @author junhao.zhao
 * 2016年12月27日  下午6:16:43
 */
/**
 * 功能： 顶栏——单团、散拼、游学、大客户、自由行、签证、(机票、)游轮--是否展示
 * 0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 (7：机票) 10：游轮
 * 返回数据
   returnData:{
		orderTypes:['单团','散拼','游轮','游学','自由行','大客户','签证',('机票')]
	}
 */

@Controller
@RequestMapping(value = "${adminPath}/statisticHome" ,method = RequestMethod.POST)
public class OrderTypesController {
	
	@Autowired
	private OrderTypesService orderTypesService;
	
	/** 功能： 顶栏——单团、散拼、游学、大客户、自由行、签证、(机票、)游轮--是否展示
	 * @param 
	 * @return orderTypes:['单团','散拼','游轮','游学','自由行','大客户','签证',('机票')]
	 */
	@ResponseBody
	@RequestMapping(value = "orderList")
	public String orderList(){
		List<Map<String, Object>> arrayList = orderTypesService.getOrderTypes();
		String orderTypes = JSONArray.fromObject(arrayList).toString();
        return orderTypes;
	}
}
