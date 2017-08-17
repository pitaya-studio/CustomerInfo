/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.hotelPl.module.bean.GuestPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceResultJsonBean;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;
import com.trekiz.admin.modules.hotelPl.service.AutoQuotedPriceService;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/autoQuotedPrice")
public class AutoQuotedPriceController extends BaseController {
	
	//forward paths
	protected static final String QUOTED_PRICE_FORM_PAGE = "modules/hotelPl/quotedPrice/quotedPriceForm";
	
	@Autowired
	private AutoQuotedPriceService autoQuotedPriceService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private AgentinfoService agentinfoService;
	/**
	 * 报价器页面 add by zhanghao
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "quotedPriceForm")
	public String quotedPriceForm(Model model) {
		//读取地接社集合信息
		Long d = UserUtils.getUser().getCompany().getId();
        model.addAttribute("supplierInfos", supplierInfoService.findSupplierInfoByCompanyId(d));
        
        Long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> agentList = agentinfoService.findAllAgentinfo(companyId);
		
        model.addAttribute("userDetail",UserUtils.getUser());
//        model.addAttribute("competitions", JSON.toJSONString(tranferAgentList4Map(agentList)));
		return QUOTED_PRICE_FORM_PAGE;
	}
	
	/**
	 * 数据映射 地接社的数据转换，转换成前端需要的数据格式
	 * @param agentList
	 * @return
	 */
	private List<Map<String,String>> tranferAgentList4Map (List<Agentinfo> agentList){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		for(Agentinfo agent:agentList){
			Map<String,String> agentMap = new HashMap<String,String>();
			agentMap.put("competitionID", agent.getId().toString());
			agentMap.put("competitionText", agent.getAgentName());
			agentMap.put("competitionName", agent.getAgentContact());
			agentMap.put("competitionCellNo", agent.getAgentContactMobile());
			result.add(agentMap);
		}
		return result;
	}
	/**
	 * 自动报价功能操作 add by zhanghao
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "quotedPrice")
	public String quotedPrice(HttpServletRequest request) {
		String queryJsonString = request.getParameter("queryJsonString");
		QuotedPriceQuery query = JSON.parseObject(queryJsonString, QuotedPriceQuery.class);
		QuotedPriceResultJsonBean jsonBean = autoQuotedPriceService.autoQuotedPrice(query);
		setParam(jsonBean);
		String data = JSON.toJSONStringWithDateFormat(jsonBean, "yyyy-MM-dd");
		//System.out.println("json字符串========="+data);
		return data;
	}
	
	/**
	 * 设置动态第N人的TITLE add by zhanghao
	 * @param jsonBean
	 */
	private void setParam(QuotedPriceResultJsonBean jsonBean){
		if(CollectionUtils.isNotEmpty(jsonBean.getQuotedPriceJsonList())){
			List<GuestPriceJsonBean> result = new ArrayList<GuestPriceJsonBean>();
			QuotedPriceJsonBean obj = jsonBean.getQuotedPriceJsonList().get(0);
			if(CollectionUtils.isNotEmpty(obj.getGuestPriceList())){
				for(GuestPriceJsonBean bean :obj.getGuestPriceList()){
					if(bean.getIsThirdPerson()==1){
						GuestPriceJsonBean nb = new GuestPriceJsonBean();
						nb.setTravelerType(bean.getTravelerType());
						nb.setTravelerTypeText(bean.getTravelerTypeText());
						nb.setIsThirdPerson(1);
						result.add(nb);
					}
				}
			}
			jsonBean.setTravelerTypesQuotedList(result);
		}
	}
	
	/**
	 * 验证是否超出选择房型的容住率，add by zhanghao
	 * @param query 报价条件
	 */
	@ResponseBody
	@RequestMapping(value = "checkRoomCapacity")
	public String checkRoomCapacity(HttpServletRequest request) {
		String queryJsonString = request.getParameter("queryJsonString");
		QuotedPriceQuery query = JSON.parseObject(queryJsonString, QuotedPriceQuery.class);
		Map<String, String> result = autoQuotedPriceService.checkRoomCapacity(query);
		String data = JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd");
		//System.out.println("json字符串========="+data);
		return data;
	}
	
}
