package com.trekiz.admin.agentToOffice.quauqstrategy.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.trekiz.admin.agentToOffice.PricingStrategy.service.ActivityPricingStrategyService;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.agentToOffice.agentInfo.service.CustomerTypeService;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QueryQuauqStrategy;
import com.trekiz.admin.agentToOffice.quauqstrategy.service.QuauqGroupStrategyService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.OfficeService;

@Controller
@RequestMapping(value="${adminPath}/group/strategy")
public class QuauqGroupStrategyController {
	
	@Autowired
	private QuauqGroupStrategyService quauqGroupStrategyService;
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ActivityPricingStrategyService activityPricingStrategyService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CustomerTypeService customerTypeService;
	
	/**
	 * 未设置费率渠道列表
	 * @param request companyUuid(产品所对应的批发商id) and groupId and productType
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	@RequestMapping(value="getNotSetStrategyAgentList")
	public String getNotSetStrategyAgentList(QueryQuauqStrategy quauqStrategy,HttpServletRequest request,HttpServletResponse response,Model model){
		
		Page<Map<String,Object>> page = quauqGroupStrategyService.getAllQuauqAgentStrate(new Page<Map<String,Object>>(request,response),quauqStrategy);
		//客户类型
		List<Map<String, Object>> dicts = customerTypeService.getCustomerTypeList4Select();
		model.addAttribute("page",page);
		if(page.getCount()==0){
			model.addAttribute("pageS",1);
		}else{
			//计算总页数
			if(page.getCount()%page.getPageSize()==0){
				model.addAttribute("pageS",page.getCount()/page.getPageSize());
			}else{
				model.addAttribute("pageS",page.getCount()/page.getPageSize()+1);
			}
		}
		//获得产品名称
		TravelActivity activity = quauqGroupStrategyService.getTravelActivityName(quauqStrategy.getGroupId());
		model.addAttribute("activity",activity);
		model.addAttribute("type","1");
		model.addAttribute("quauqStrategy",quauqStrategy);
		model.addAttribute("dicts", dicts);
		return "/agentToOffice/quauqStrategy/strategyAgentList";
	}
	
	
	/**
	 * 批量设置
	 * @param request productType
	 * @param model
	 * @return
	 * @time 2016/08/16
	 */
	@RequestMapping(value="targetToBatchGrouprate")
	public String targetToBatchGrouprate(QueryQuauqStrategy quauqStrategy,HttpServletRequest request,HttpServletResponse response,Model model){
		String companyUuid = request.getParameter("companyUuid");
		if(!StringUtil.isBlank(companyUuid)&& !StringUtil.isBlank(companyUuid.trim())){
			Office company = officeService.findWholeOfficeByUuid(companyUuid);
			if(company != null){
				model.addAttribute("company", company);
			}else{
				model.addAttribute(Context.ERROR_MESSAGE_KEY, "您要查找的批发商不存在");
				return Context.ERROR_PAGE;
			}
		}else{
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "您提交的批发商ID数据格式错误");
			return Context.ERROR_PAGE;
		}
		String groupids = request.getParameter("groupIds");
		Page<Map<String,Object>> page = quauqGroupStrategyService.getAllQuauqAgent(new Page<Map<String,Object>>(request,response),quauqStrategy);
		List<Map<String, Object>> dicts =  customerTypeService.getCustomerTypeList4Select();
		model.addAttribute("page",page);
		if(page.getCount()==0){
			model.addAttribute("pageS",1);
		}else{
			if(page.getCount()%page.getPageSize()==0){
				model.addAttribute("pageS",page.getCount()/page.getPageSize());
			}else{
				model.addAttribute("pageS",page.getCount()/page.getPageSize()+1);
			}
		}
		model.addAttribute("batch",true);
		model.addAttribute("type","1");
		model.addAttribute("quauqStrategy",quauqStrategy);
		model.addAttribute("groupIds",groupids);
		model.addAttribute("dicts", dicts);
		return "/agentToOffice/quauqStrategy/batchStrategyAgentList";
	}
	
	/**
	 * 批量设置费率保存
	 * @param request
	 * @return
	 * @time 2016/08/17
	 */
	@ResponseBody
	@RequestMapping(value="batchSaveGroupRate")
	public String batchSaveGroupRate(HttpServletRequest request){
		JSONArray array = JSONArray.parseArray(request.getParameter("datas"));
		String groupIds = request.getParameter("groupIds");
		String productType = request.getParameter("productType");
		String companyUuid = request.getParameter("companyUuid");
		if(array==null && array.size()==0){
			return "fail";
		}
		if(StringUtil.isBlank(groupIds)){
			return "fail";
		}
		quauqGroupStrategyService.batchSaveGroupRate(groupIds,array,productType,companyUuid);
		return "success";
	}
	
	/**
	 * 已设置费率渠道列表
	 * @param request groupId 和 productType
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	@RequestMapping(value="getHasSetStrategyAgentList")
	public String getHasSetStrategyAgentList(QueryQuauqStrategy quauqStrategy,HttpServletRequest request,HttpServletResponse response,Model model){
		Page<Map<String,Object>> page = quauqGroupStrategyService.getQuauqAgentStrate(new Page<Map<String,Object>>(request,response),quauqStrategy);
		//获得客户类型
		List<Map<String, Object>> dicts = customerTypeService.getCustomerTypeList4Select();
		if(page.getCount()==0){
			model.addAttribute("pageS",1);
		}else{
			//计算总页数
			if(page.getCount()%page.getPageSize()==0){
				model.addAttribute("pageS",page.getCount()/page.getPageSize());
			}else{
				model.addAttribute("pageS",page.getCount()/page.getPageSize()+1);
			}
		}
		//获得产品名称
		TravelActivity activity = quauqGroupStrategyService.getTravelActivityName(quauqStrategy.getGroupId());
		model.addAttribute("activity",activity);
		model.addAttribute("page",page);
		model.addAttribute("type","2");
		model.addAttribute("quauqStrategy",quauqStrategy);
		model.addAttribute("dicts", dicts);
		return "/agentToOffice/quauqStrategy/strategyAgentList";
	}
	
	/**
	 * 保存和批量保存费率
	 * @param request
	 * @return
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	@ResponseBody
	@RequestMapping(value="batchSaveStrategy")
	public String batchSaveStrategy(HttpServletRequest request){
		JSONArray array = JSONArray.parseArray(request.getParameter("datas"));
		if(array==null && array.size()==0){
			return "fail";
		}
		quauqGroupStrategyService.batchSaveStrategy(array);
		return "success";
	}
	
	/**
	 * 设置费率页面查看
	 * @param request  agentId
	 * @return
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	@ResponseBody
	@RequestMapping(value="getChildrenAgentList")
	public List<Map<String,Object>> getChildrenAgentList(QueryQuauqStrategy quauqStrategy,HttpServletRequest request){
		List<Map<String,Object>> list = quauqGroupStrategyService.getChildrenAgentList(quauqStrategy);
		return list;
	}
	
	@RequestMapping(value={"GroupRateStrategyList"})
	public String groupRateStrategyList(HttpServletRequest request, HttpServletResponse response,Model model) {
		String companyId = request.getParameter("companyId");
		if(!StringUtil.isBlank(companyId)&& !StringUtil.isBlank(companyId.trim())&&companyId.replaceAll("[0-9]", "").trim().length()==0){
			Office company = officeService.get(new Long(companyId));
			if(company != null){
				model.addAttribute("company", company);
			}else{
				//报错   不存在
				model.addAttribute(Context.ERROR_MESSAGE_KEY, "您要查询的批发商不存在");
				return Context.ERROR_PAGE;
			}
		}else{
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "你提交的批发商ID数据格式错误");
			return Context.ERROR_PAGE;
			//报错页面  数据格式不正确
		}
		//初始化搜索参数
		String activityKind = request.getParameter("activityKind");
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}else if(activityKind.replaceAll("[0-9]", "").trim().length()>0){
			//报错   产品类型参数类型不正确
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "您要查询的产品类型有误");
			return Context.ERROR_PAGE;
		}
		initSearchParms(activityKind,model,companyId);
		
		//根据搜索条件查询符合条件的团期信息
		Page<Map<Object,Object>> page = travelActivityService.searchSPActivityList(new Page<Map<Object, Object>>(request, response),request,model,companyId,null);
		model.addAttribute("currentPage", page.getPageNo());
		model.addAttribute("totalPage", (page.getCount()%page.getPageSize()==0)?(page.getCount()/page.getPageSize()):(page.getCount()/page.getPageSize()+1));
		int allCount = travelActivityService.getCount(true);
		int someCount = travelActivityService.getCount(false);
		model.addAttribute("allCount", allCount);
		model.addAttribute("someCount", someCount);
		List<Map<Object,Object>> searchList = page.getList();
		//公司默认费率
//		Rate companyRate = RateUtils.getRate(new Long(itemActivity.get("id").toString()), activityKindInt);
		Integer activityKindInt = new Integer(activityKind);
		//获得货币Map
		Map<String,String> currencyMarks = currencyService.getCurrencyMarks();
		for(Map<Object,Object>itemActivity:searchList){
			//对价格进行处理
			String currency_type = (itemActivity.get("currency_type")==null?null:itemActivity.get("currency_type").toString());
			if(!StringUtil.isBlank(currency_type)){
				String[] currencys = currency_type.split(",");
				String settlementAdultPrice = itemActivity.get("settlementAdultPrice")==null?"-":itemActivity.get("settlementAdultPrice").toString();
				String settlementcChildPrice = itemActivity.get("settlementcChildPrice")==null?"-":itemActivity.get("settlementcChildPrice").toString();
				String settlementSpecialPrice = itemActivity.get("settlementSpecialPrice")==null?"-":itemActivity.get("settlementSpecialPrice").toString();
				if(!StringUtil.isBlank(settlementAdultPrice)){
					itemActivity.put("settlementAdultPrice",currencyMarks.get(currencys[0])+" "+settlementAdultPrice );
				}
				if(!StringUtil.isBlank(settlementcChildPrice)){
					itemActivity.put("settlementcChildPrice", currencyMarks.get(currencys[1])+" "+settlementcChildPrice);
				}
				if(!StringUtil.isBlank(settlementSpecialPrice)){
					itemActivity.put("settlementSpecialPrice", currencyMarks.get(currencys[2])+" "+settlementSpecialPrice);
				}
				String quauqAdultPrice = (itemActivity.get("quauqAdultPrice")==null?"-":itemActivity.get("quauqAdultPrice").toString());
				String quauqChildPrice = (itemActivity.get("quauqChildPrice")==null?"-":itemActivity.get("quauqChildPrice").toString());
				String quauqSpecialPrice = (itemActivity.get("quauqSpecialPrice")==null?"-":itemActivity.get("quauqSpecialPrice").toString());
				if(quauqAdultPrice != "-"||quauqChildPrice != "-"||quauqSpecialPrice != "-"){
					itemActivity.put("alertFlag", true);
				}else{
					itemActivity.put("alertFlag", false);
				}
				//quauq 供应价
				Rate rate = RateUtils.getRate(new Long(itemActivity.get("id").toString()), activityKindInt);
				if(rate.getQuauqRateType() == 0){
					if(!quauqAdultPrice.equals("-")){
						itemActivity.put("supplyAdultPrice", currencyMarks.get(currencys[0])+" "+new BigDecimal(quauqAdultPrice).multiply(new BigDecimal("1").add(rate.getQuauqRate())).setScale(2,BigDecimal.ROUND_HALF_UP));
					}else{
						itemActivity.put("supplyAdultPrice", currencyMarks.get(currencys[0])+"-");
					}
					if(!quauqChildPrice.equals("-")){
						itemActivity.put("supplyChildPrice", currencyMarks.get(currencys[1])+" "+new BigDecimal(quauqChildPrice).multiply(new BigDecimal("1").add(rate.getQuauqRate())).setScale(2,BigDecimal.ROUND_HALF_UP));
											
					}else{
						itemActivity.put("supplyChildPrice", currencyMarks.get(currencys[1])+"-");
					}
					if(!quauqSpecialPrice.equals("-")){
						itemActivity.put("supplySpecialPrice", currencyMarks.get(currencys[2])+" "+new BigDecimal(quauqSpecialPrice).multiply(new BigDecimal("1").add(rate.getQuauqRate())).setScale(2,BigDecimal.ROUND_HALF_UP));
						
					}else{
						itemActivity.put("supplySpecialPrice", currencyMarks.get(currencys[2])+"-");
					}
				}else{
					if(!quauqAdultPrice.equals("-")){
						itemActivity.put("supplyAdultPrice", currencyMarks.get(currencys[0])+" "+new BigDecimal(quauqAdultPrice).add(rate.getQuauqRate()).setScale(2,BigDecimal.ROUND_HALF_UP));
					}else{
						itemActivity.put("supplyAdultPrice", currencyMarks.get(currencys[0])+"-");
					}
					if(!quauqChildPrice.equals("-")){
						itemActivity.put("supplyChildPrice", currencyMarks.get(currencys[1])+" "+new BigDecimal(quauqChildPrice).add(rate.getQuauqRate()).setScale(2,BigDecimal.ROUND_HALF_UP));
											
					}else{
						itemActivity.put("supplyChildPrice", currencyMarks.get(currencys[1])+"-");
					}
					if(!quauqSpecialPrice.equals("-")){
						itemActivity.put("supplySpecialPrice", currencyMarks.get(currencys[2])+" "+new BigDecimal(quauqSpecialPrice).add(rate.getQuauqRate()).setScale(2,BigDecimal.ROUND_HALF_UP));
						
					}else{
						itemActivity.put("supplySpecialPrice", currencyMarks.get(currencys[2])+"-");
					}
				}
				itemActivity.put("quauqAdultPrice", currencyMarks.get(currencys[0])+" "+quauqAdultPrice);
				itemActivity.put("quauqChildPrice", currencyMarks.get(currencys[1])+" "+quauqChildPrice);
				itemActivity.put("quauqSpecialPrice", currencyMarks.get(currencys[2])+" "+quauqSpecialPrice);
			}
			
			//获取对应的定价策略
			Map<String,String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(itemActivity.get("srcActivityId").toString(),itemActivity.get("id").toString());
			itemActivity.put("adultPricingStrategy", pricingStrategy.get("adultPricingStrategy"));
			itemActivity.put("childrenPricingStrategy", pricingStrategy.get("childrenPricingStrategy"));
			itemActivity.put("specialPricingStrategy", pricingStrategy.get("specialPricingStrategy"));
		}
		model.addAttribute("page", page);
		return "agentToOffice/quauqStrategy/GroupRateStrategyList";
	}
	
	private void initSearchParms(String activityKind,Model model,String companyId){
		//出发城市  fromArea
		model.addAttribute("fromAreas", travelActivityService.getFromAreas(activityKind,companyId));
		//抵达城市     targetArea
		model.addAttribute("targetAreas", travelActivityService.getTargetAreas(activityKind,companyId));
		//目的地   Destination
		model.addAttribute("destinations", travelActivityService.getDestinations(activityKind,companyId));
		//产品类型 activityKind
		model.addAttribute("activityKinds", travelActivityService.getActivityKind(activityKind,companyId));
	}
	
	
	
}
