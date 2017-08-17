package com.trekiz.admin.modules.finance.web;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="${adminPath}/return/price")
public class ReturnPriceController {
	
	@Autowired
	private ReturnDifferenceService returnDifferenceService;
	@Autowired
	private ISelectService selectService;
	@Autowired
	private ProductOrderService productOrderService;
	
	@RequestMapping(value="goReturnPriceDetail")
	public String goReturnPriceDetail(){
		return "modules/finance/differenceDetail";
	}
	/**
	 * 获得差额返还明细
	 * @return
	 * @author chao.zhang
	 * @time 2016-10-17
	 */
	@ResponseBody
	@RequestMapping(value="getReturnPriceDetail")
	public String getReturnPriceDetail(HttpServletRequest request,HttpServletResponse response){
		String input = request.getParameter("input");
		String agentId = request.getParameter("agentId");
		String agentContactId = request.getParameter("agentContactId");
		String payType = request.getParameter("payType");
		Page<Map<String,Object>> page = returnDifferenceService.getReturnPriceDetail(new Page<Map<String,Object>>(request,response) ,input, agentId, agentContactId, payType);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> endMap = new HashMap<String, Object>();
		for(Map<String,Object> pageMap : page.getList()){
			list.add(pageMap);
		}
		endMap.put("page", list);
		endMap.put("count", page.getCount());
		endMap.put("pageSize", page.getPageSize());
		endMap.put("pageNo", page.getPageNo());
		return JSON.toJSONString(endMap);
	}
	
	/**
	 * 更改差额支付状态
	 * @return
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value= "updateDifferencePayStatus")
	public String updateDifferencePayStatus(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		try {
			String orderIds = request.getParameter("orderId");
			String differencePayStatus = request.getParameter("payType");
			String[] orderIdes = orderIds.split(",");
			for(String orderId : orderIdes){
				//根据orderId获得订单
				ProductOrderCommon productOrderCommon = productOrderService.getProductorderById(Long.parseLong(orderId));
				productOrderCommon.setDifferencePayStatus(Integer.parseInt(differencePayStatus));
				returnDifferenceService.updateDifferencePayStatus(productOrderCommon);
			}
			map.put("result", "success");
			return JSON.toJSONString(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "false");
			return JSON.toJSONString(map);
		}
	}
	
	/**
	 * 查询T1渠道
	 * @return
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value = "getT1Agentinfos")
	public String getT1Agentinfos(){
		List<Map<String,Object>> agentinfos = selectService.getT1Agentinfos();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("agentinfos", agentinfos);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 查询T1门店账户下联系人
	 * @return
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value="getT1Users")
	public String getT1Users(){
		List<Map<String,Object>> users = selectService.getT1User();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("agentContacts", users);
		return JSON.toJSONString(map);
	}
}
