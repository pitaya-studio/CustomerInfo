package com.trekiz.admin.agentToOffice.PricingStrategy.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.t1.utils.T1Utils;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.ActivityPricingStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PricingStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.service.ActivityPricingStrategyService;
import com.trekiz.admin.agentToOffice.T2.utils.JudgeStringType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;
import com.trekiz.admin.modules.activity.utils.TravelActivityUtil;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value="${adminPath}/pricingStrategy/manager")
public class PricingStrategyNewController {
	
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ActivityPricingStrategyService activityPricingStrategyService;
	@Autowired
	private ActivityGroupSyncService activityGroupSyncService;
	/*@Autowired
	private ActivityGroupService activityGroupService;*/
	
	@RequestMapping(value={"pricingStrategyList"})
	public String list(HttpServletRequest request, HttpServletResponse response,Model model) {
		//初始化搜索参数
		String activityKind = request.getParameter("activityKind");
		if(StringUtil.isBlank(activityKind)){
			activityKind = "2";
		}else if(!JudgeStringType.isPositiveInteger(activityKind)){
			throw new RuntimeException("参数数据格式错误");
		}
		initSearchParms(activityKind,model);
		
		//根据搜索条件查询符合条件的团期信息
		Page<Map<Object,Object>> page = travelActivityService.searchSPActivityList(new Page<Map<Object, Object>>(request, response),request,model,UserUtils.getUser().getCompany().getId().toString(),"pricingStrategyRequest");
		model.addAttribute("currentPage", page.getPageNo());
		model.addAttribute("totalPage", (page.getCount()%page.getPageSize()==0)?(page.getCount()/page.getPageSize()):(page.getCount()/page.getPageSize()+1));
		int allCount = travelActivityService.getCount(true);
		int someCount = travelActivityService.getCount(false);
		model.addAttribute("allCount", allCount);
		model.addAttribute("someCount", someCount);
		List<Map<Object,Object>> searchList = page.getList();
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
//				if(!StringUtil.isBlank(quauqAdultPrice)||!StringUtil.isBlank(quauqChildPrice)||!StringUtil.isBlank(quauqSpecialPrice)){
				if(quauqAdultPrice != "-"||quauqChildPrice != "-"||quauqSpecialPrice != "-"){
					/*if(currencys.length > 8 &&(!StringUtil.isBlank(quauqAdultPrice)||!StringUtil.isBlank(quauqChildPrice)||!StringUtil.isBlank(quauqSpecialPrice))){*/
					itemActivity.put("alertFlag", true);
				}else{
					itemActivity.put("alertFlag", false);
				}
				itemActivity.put("quauqAdultPrice", currencyMarks.get(currencys[0])+" "+quauqAdultPrice);
				itemActivity.put("quauqChildPrice", currencyMarks.get(currencys[1])+" "+quauqChildPrice);
				itemActivity.put("quauqSpecialPrice", currencyMarks.get(currencys[2])+" "+quauqSpecialPrice);
				itemActivity.put("hasEyelessAgents", TravelActivityUtil.hasEyelessAgents(itemActivity.get("id").toString(), Integer.parseInt(activityKind), settlementAdultPrice,settlementcChildPrice,settlementSpecialPrice,quauqAdultPrice,quauqChildPrice,quauqSpecialPrice,currencys));
			}
			
			//获取对应的定价策略
			Map<String,String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(itemActivity.get("srcActivityId").toString(),itemActivity.get("id").toString());
			itemActivity.put("adultPricingStrategy", pricingStrategy.get("adultPricingStrategy"));
			itemActivity.put("childrenPricingStrategy", pricingStrategy.get("childrenPricingStrategy"));
			itemActivity.put("specialPricingStrategy", pricingStrategy.get("specialPricingStrategy"));
			
		}
		model.addAttribute("page", page);
		model.addAttribute("shelfRightsStatus",UserUtils.getUser().getCompany().getShelfRightsStatus());
		return "agentToOffice/pricingStrategy/ActivityPricingStrategyList";
	}
	
	@RequestMapping(value={"exportData"})
	public void exportData(HttpServletRequest request, HttpServletResponse response) {
		
		//根据搜索条件查询符合条件的团期信息
		List<Map<Object,Object>> searchList = travelActivityService.exportData();
		//获得货币Map
		Map<String,String> currencyMarks = currencyService.getCurrencyMarks();
		List<Object[]> exportUserList = new ArrayList<Object[]>();
		int countIndex = 0;
		for (Map<Object,Object>itemActivity : searchList) {
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

				itemActivity.put("quauqAdultPrice", currencyMarks.get(currencys[0])+" "+quauqAdultPrice);
				itemActivity.put("quauqChildPrice", currencyMarks.get(currencys[1])+" "+quauqChildPrice);
				itemActivity.put("quauqSpecialPrice", currencyMarks.get(currencys[2])+" "+quauqSpecialPrice);
				
				String tgAdultPrice = (itemActivity.get("tgAdultPrice")==null?"-":itemActivity.get("tgAdultPrice").toString());
				String tgChildPrice = (itemActivity.get("tgChildPrice")==null?"-":itemActivity.get("tgChildPrice").toString());
				String tgSpecialPrice = (itemActivity.get("tgSpecialPrice")==null?"-":itemActivity.get("tgSpecialPrice").toString());
				
				itemActivity.put("tgAdultPrice", currencyMarks.get(currencys[0])+" "+tgAdultPrice);
				itemActivity.put("tgChildPrice", currencyMarks.get(currencys[1])+" "+tgChildPrice);
				itemActivity.put("tgSpecialPrice", currencyMarks.get(currencys[2])+" "+tgSpecialPrice);
			} else {
				continue;
			}
			
			//获取对应的定价策略
			Map<String,String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(itemActivity.get("srcActivityId").toString(),itemActivity.get("id").toString());
			String adultPricingStrategy = pricingStrategy.get("adultPricingStrategy");
			String childrenPricingStrategy = pricingStrategy.get("childrenPricingStrategy");
			String specialPricingStrategy = pricingStrategy.get("specialPricingStrategy");
			
			// 要导出数据
			Object[] temp = new Object[21];
			String settlementAdultPrice = itemActivity.get("settlementAdultPrice").toString();
			String settlementcChildPrice = itemActivity.get("settlementcChildPrice").toString();
			String settlementSpecialPrice = itemActivity.get("settlementSpecialPrice").toString();
			String quauqAdultPrice = itemActivity.get("quauqAdultPrice").toString();
			String quauqChildPrice = itemActivity.get("quauqChildPrice").toString();
			String quauqSpecialPrice = itemActivity.get("quauqSpecialPrice").toString();
			String tgAdultPrice = itemActivity.get("tgAdultPrice").toString();
			String tgChildPrice = itemActivity.get("tgChildPrice").toString();
			String tgSpecialPrice = itemActivity.get("tgSpecialPrice").toString();
			countIndex++;
			temp[0] = countIndex;
			temp[1] = itemActivity.get("officeName");
			temp[2] = itemActivity.get("acitivityName");
			temp[3] = itemActivity.get("groupCode");
			temp[4] = itemActivity.get("groupOpenDate");
			temp[5] = itemActivity.get("freePosition");
			temp[6] = settlementAdultPrice;
			temp[7] = settlementcChildPrice;
			temp[8] = settlementSpecialPrice;
			temp[9] = adultPricingStrategy;
			temp[10] = childrenPricingStrategy;
			temp[11] = specialPricingStrategy;
			temp[12] = quauqAdultPrice;
			temp[13] = quauqChildPrice;
			temp[14] = quauqSpecialPrice;
			temp[15] = tgAdultPrice;
			temp[16] = tgChildPrice;
			temp[17] = tgSpecialPrice;
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			String settlementAdult = settlementAdultPrice.split(" ")[1];
			String quauqAdult = quauqAdultPrice.split(" ")[1];
			if (!settlementAdult.equals("-") && !quauqAdult.equals("-") && !settlementAdult.equals(quauqAdult)) {
				temp[18] = df.format((Double.parseDouble(settlementAdult) - Double.parseDouble(quauqAdult))/Double.parseDouble(settlementAdult)*100) + "%";
			} else {
				temp[18] = "";
			}
			String settlementcChild = settlementcChildPrice.split(" ")[1];
			String quauqChild = quauqChildPrice.split(" ")[1];
			if (!settlementcChild.equals("-") && !quauqChild.equals("-") && !settlementcChild.equals(quauqChild)) {
				temp[19] = df.format((Double.parseDouble(settlementcChild) - Double.parseDouble(quauqChild))/Double.parseDouble(settlementcChild)*100) + "%";
			} else {
				temp[19] = "";
			}
			String settlementSpecial = settlementSpecialPrice.split(" ")[1];
			String quauqSpecial = quauqSpecialPrice.split(" ")[1];
			if (!settlementSpecial.equals("-") && !quauqSpecial.equals("-") && !settlementSpecial.equals(quauqSpecial)) {
				temp[20] = df.format((Double.parseDouble(settlementSpecial) - Double.parseDouble(quauqSpecial))/Double.parseDouble(settlementSpecial)*100) + "%";
			} else {
				temp[20] = "";
			}
			exportUserList.add(temp);
		}
		
		Calendar c = Calendar.getInstance();
		//文件名称
		String fileName = "T2 QUAUQ价格策略设置情况统计表--" + c.get(Calendar.YEAR) + "年" 
							+ (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日";
		//Excel各行名称
		String[] cellTitle =  {"序号","批发商","产品名称","团号","出团日期","余位","同行价","","","定价策略","","","QUAUQ价","","","结算价","","","折扣率","",""};
		//文件首行标题
		String firstTitle = "价格策略列表";
		try {
			ExportExcel.createExcle(fileName, exportUserList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 单个团期设置策略--旧版本
	 * @param groupid
	 * @param activityid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"editPricingStrategy/{groupid}/{activityid}"})
	public String editPricingStrategy(@PathVariable String groupid, @PathVariable String activityid,HttpServletRequest request, HttpServletResponse response,Model model) {
		if(!StringUtil.isBlank(groupid)&&!StringUtil.isBlank(activityid)){
			List<PricingStrategy> priningStrategy = activityPricingStrategyService.getActivityPrining(activityid, groupid);
			List<PricingStrategy> adultList = new ArrayList<PricingStrategy>();
			List<PricingStrategy> childrenList = new ArrayList<PricingStrategy>();
			List<PricingStrategy> specialList = new ArrayList<PricingStrategy>();
			for(PricingStrategy tempEntity:priningStrategy){
				if(tempEntity.getPersonType() == 0){
					adultList.add(tempEntity);
				}else if(tempEntity.getPersonType() == 1){
					childrenList.add(tempEntity);
				}else{
					specialList.add(tempEntity);
				}
			}
			model.addAttribute("priningStrategy", activityPricingStrategyService.getActivityPrining(activityid, groupid));
			model.addAttribute("adultList",adultList);
			model.addAttribute("childrenList", childrenList);
			model.addAttribute("specialList", specialList);
			model.addAttribute("groupActivity", activityGroupSyncService.findById(new Long(groupid)));
		}
		return "include/pricingStrtegyFrame";
	};
	
	/**
	 * 单个团期设置策略--522版本
	 * @param
	 * @param
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value={"editPricingStrategy522/{groupid}/{activityid}"})
	public String editPricingStrategy522(@PathVariable String groupid, @PathVariable String activityid,HttpServletRequest request, HttpServletResponse response,Model model) {
		if(!JudgeStringType.isPositiveInteger(activityid)||!JudgeStringType.isPositiveInteger(groupid)){
			throw new RuntimeException("参数数据格式错误");
		}
		List<PricingStrategy> priningStrategy = activityPricingStrategyService.getActivityPrining(activityid, groupid);
		List<PricingStrategy> adultList = new ArrayList<PricingStrategy>();
		List<PricingStrategy> childrenList = new ArrayList<PricingStrategy>();
		List<PricingStrategy> specialList = new ArrayList<PricingStrategy>();
		for(PricingStrategy tempEntity:priningStrategy){
			if(tempEntity.getPersonType() == 0){
				adultList.add(tempEntity);
			}else if(tempEntity.getPersonType() == 1){
				childrenList.add(tempEntity);
			}else{
				specialList.add(tempEntity);
			}
		}
		
		//获取团期信息
		ActivityGroup group = activityGroupSyncService.findById(new Long(groupid));
		
		//获取团期对应的最高：折扣费率 直减费率
		
		model.addAttribute("priningStrategy", priningStrategy);
		model.addAttribute("adultList",adultList);
		model.addAttribute("childrenList", childrenList);
		model.addAttribute("specialList", specialList);
		model.addAttribute("groupActivity", activityGroupSyncService.findById(new Long(groupid)));
		return "agentToOffice/pricingStrategy/activityPricingStrategyFram";
	};*/
	
	@RequestMapping(value={"editBatchPricingStrategy"})
	public String editBatchPricingStrategy(HttpServletRequest request, HttpServletResponse response,Model model) {
		return "include/pricingStrtegyFrame";
	};
	
	private List<Long> saveStrategyBatch(Integer personType,String pricingS){
		if(!StringUtil.isBlank(pricingS)){
			String[] strategys = pricingS.replace(",", " ").trim().split(" ");
			List<Long> strategyList = new ArrayList<Long>();
			for(String strategy:strategys){
				String[] strategyItems = strategy.split(":");
				PricingStrategy strategyEntity = new PricingStrategy(personType,new Integer(strategyItems[0]),new BigDecimal(strategyItems[1]));
				strategyList.add(activityPricingStrategyService.addPricingStrategy(strategyEntity));
			}
			return strategyList;
		}else{
			return null;
		}
	}
	
	private void saveStrategySingle(Long activityid,Long groupid,int personType,List<Long> strategyList){
		if(strategyList != null){
			for(Long strategyid:strategyList){
				ActivityPricingStrategy activityStrategy = new ActivityPricingStrategy(activityid,groupid,strategyid);
				activityPricingStrategyService.saveActivityStrategy(activityStrategy);
			}
		}
	}
	
	@RequestMapping(value={"addBatchActivityStrategy"})
	@ResponseBody
	public Object addBatchActivityStrategy(HttpServletRequest request, HttpServletResponse response,Model model){
		JSONObject resobj = new JSONObject();
		String ids = request.getParameter("ids");
		String activityKind = request.getParameter("activitykind");
		String adultPri = (request.getParameter("adultPri")==null?"":request.getParameter("adultPri"));
		String childrenPri = (request.getParameter("childrenPri")==null?"":request.getParameter("childrenPri"));
		String specialPri = (request.getParameter("specialPri")==null?"":request.getParameter("specialPri"));
		if(!StringUtil.isBlank(ids)){
			List<Long> adultPriList = null ;
			if(!StringUtil.isBlank(adultPri)){
				adultPriList = saveStrategyBatch(0,adultPri);
			}
			List<Long> childrenPriList = null;
			if(!StringUtil.isBlank(childrenPri)){
				childrenPriList = saveStrategyBatch(1,childrenPri);
			}
			List<Long> specialPriList = null;
			if(!StringUtil.isBlank(specialPri)){
				specialPriList = saveStrategyBatch(2,specialPri);
			}
			String[] idArray = ids.replace(",", " ").trim().split(" ");
			for(String idTemp:idArray){
				String[] idTemps = idTemp.split("_");
				//产品ID  团期ID  判断是否有成人价儿童价  和特殊人群价
				if(activityPricingStrategyService.checkAdd(idTemps[1], idTemps[0])){
					List<PricingStrategy> priningStrategy = activityPricingStrategyService.getActivityPrining(idTemps[1], idTemps[0]);
					if(priningStrategy.size() > 0){
						String orgAdultPri = "";
						String orgChildrenPri = "";
						String orgSpecialPri = "";
						for(PricingStrategy entity:priningStrategy){
							if(entity.getPersonType()==0){
								orgAdultPri = orgAdultPri + entity.getFavorableType()+":"+entity.getFavorableNum()+",";
							}else if(entity.getPersonType()==1){
								orgChildrenPri = orgChildrenPri + entity.getFavorableType()+":"+entity.getFavorableNum()+",";
							}else{
								orgSpecialPri = orgSpecialPri + entity.getFavorableType()+":"+entity.getFavorableNum()+",";
							}
						}
						if(!orgAdultPri.equals(adultPri)){
							for(PricingStrategy entity:priningStrategy){
								if(entity.getPersonType() == 0){
									activityPricingStrategyService.updateUsageState(idTemps[0],idTemps[1],entity.getId());
								}
							}
							saveStrategySingle(new Long(idTemps[1]),new Long(idTemps[0]),0,adultPriList);
						}
						if(!orgChildrenPri.equals(childrenPri)){
							for(PricingStrategy entity:priningStrategy){
								if(entity.getPersonType() == 1){
									activityPricingStrategyService.updateUsageState(idTemps[0],idTemps[1],entity.getId());
								}
							}
							saveStrategySingle(new Long(idTemps[1]),new Long(idTemps[0]),1,childrenPriList);
						}
						if(!orgSpecialPri.equals(specialPri)){
							for(PricingStrategy entity:priningStrategy){
								if(entity.getPersonType() == 2){
									activityPricingStrategyService.updateUsageState(idTemps[0],idTemps[1],entity.getId());
								}
							}
							saveStrategySingle(new Long(idTemps[1]),new Long(idTemps[0]),2,specialPriList);
						}
					}else{
						saveStrategySingle(new Long(idTemps[1]),new Long(idTemps[0]),0,adultPriList);
						saveStrategySingle(new Long(idTemps[1]),new Long(idTemps[0]),1,childrenPriList);
						saveStrategySingle(new Long(idTemps[1]),new Long(idTemps[0]),2,specialPriList);
					}
					activityPricingStrategyService.changeQuauqPrice(idTemps[0],idTemps[1]);
				}
				resobj.put("flag","ok");
			}
			//改变策略后，同步更新缓存
			T1Utils.updateT1HomeCache();
		}
		return resobj;
	}

	
	@RequestMapping(value={"addActivityStrategy"})
	@ResponseBody
	public Object addPricingStrategy(HttpServletRequest request, HttpServletResponse response,Model model){
		JSONObject resobj = new JSONObject();
		String groupid = request.getParameter("groupid");
		String activityKind = request.getParameter("activitykind");
		String activityid = request.getParameter("activityid");
		String adultPri = (request.getParameter("adultPri")==null?"":request.getParameter("adultPri"));
		String childrenPri = (request.getParameter("childrenPri")==null?"":request.getParameter("childrenPri"));
		String specialPri = (request.getParameter("specialPri")==null?"":request.getParameter("specialPri"));
		if(!StringUtil.isBlank(groupid) && !StringUtil.isBlank(activityid)){
//			ActivityGroup activityGroup = activityGroupSyncService.findById(new Long(groupid));
			//获取原有策略，判断策略是否改变
			List<PricingStrategy> priningStrategy = activityPricingStrategyService.getActivityPrining(activityid, groupid);
			if(priningStrategy.size() > 0){
				String orgAdultPri = "";
				String orgChildrenPri = "";
				String orgSpecialPri = "";
				for(PricingStrategy entity:priningStrategy){
					if(entity.getPersonType()==0){
						orgAdultPri = orgAdultPri + entity.getFavorableType()+":"+entity.getFavorableNum()+",";
					}else if(entity.getPersonType()==1){
						orgChildrenPri = orgChildrenPri + entity.getFavorableType()+":"+entity.getFavorableNum()+",";
					}else{
						orgSpecialPri = orgSpecialPri + entity.getFavorableType()+":"+entity.getFavorableNum()+",";
					}
				}
				if(!orgAdultPri.equals(adultPri)){
					for(PricingStrategy entity:priningStrategy){
						if(entity.getPersonType() == 0){
							activityPricingStrategyService.updateUsageState(groupid,activityid,entity.getId());
						}
					}
					saveStrategyPrivate(new Long(activityid),new Long(groupid),0,adultPri);
				}
				if(!orgChildrenPri.equals(childrenPri)){
					for(PricingStrategy entity:priningStrategy){
						if(entity.getPersonType() == 1){
							activityPricingStrategyService.updateUsageState(groupid,activityid,entity.getId());
						}
					}
					saveStrategyPrivate(new Long(activityid),new Long(groupid),1,childrenPri);
				}
				if(!orgSpecialPri.equals(specialPri)){
					for(PricingStrategy entity:priningStrategy){
						if(entity.getPersonType() == 2){
							activityPricingStrategyService.updateUsageState(groupid,activityid,entity.getId());
						}
					}
					saveStrategyPrivate(new Long(activityid),new Long(groupid),2,specialPri);
				}
			}else{
				saveStrategyPrivate(new Long(activityid),new Long(groupid),0,adultPri);
				saveStrategyPrivate(new Long(activityid),new Long(groupid),1,childrenPri);
				saveStrategyPrivate(new Long(activityid),new Long(groupid),2,specialPri);
			}
			
			//改变quauq价格
			activityPricingStrategyService.changeQuauqPrice(groupid,activityid);
			activityGroupSyncService.updatePSStatusById(Long.parseLong(groupid), Context.PRICING_NEED_RESET_STATUS );
			//改变策略后，同步更新缓存
			T1Utils.updateT1HomeCache();
			resobj.put("flag","ok");
		}else{
			resobj.put("flag","error");
		}
		return resobj;
	}

	private void saveStrategyPrivate(Long activityid,Long groupid,  Integer personType,String pricingS){
		if(!StringUtil.isBlank(pricingS)){
			String[] strategys = pricingS.replace(",", " ").trim().split(" ");
			for(String strategy:strategys){
				String[] strategyItems = strategy.split(":");
				PricingStrategy strategyEntity = new PricingStrategy(personType,new Integer(strategyItems[0]),new BigDecimal(strategyItems[1]));
				Long id = activityPricingStrategyService.addPricingStrategy(strategyEntity);
				ActivityPricingStrategy activityStrategy = new ActivityPricingStrategy(activityid,groupid,id);
				activityPricingStrategyService.saveActivityStrategy(activityStrategy);
			}
		}
	}
	
	private void initSearchParms(String activityKind,Model model){
		//出发城市  fromArea
		model.addAttribute("fromAreas", travelActivityService.getFromAreas(activityKind,UserUtils.getUser().getCompany().getId().toString()));
		//线路     targetArea
		model.addAttribute("targetAreas", travelActivityService.getTargetAreas(activityKind,UserUtils.getUser().getCompany().getId().toString()));
		//出团日期  groupOpenDate groupCloseDate
		//旅游类型  travelTypeId  overseasFlag
		model.addAttribute("travelTypes", travelActivityService.getTravelTypes(activityKind,UserUtils.getUser().getCompany().getId().toString()));
		//产品类型  activityTypeId
		model.addAttribute("activityTypes", travelActivityService.getActivityTypes(activityKind,UserUtils.getUser().getCompany().getId().toString()));
		//产品系列
		model.addAttribute("activityLevels", travelActivityService.getActivityLevels(activityKind,UserUtils.getUser().getCompany().getId().toString()));
		//交易系统上架状态
		Map<String,String> groupOnlineState = new HashMap<String,String>();
		groupOnlineState.put("2", "已上架");
		groupOnlineState.put("1", "未上架");
		groupOnlineState.put("3", "已下架");
		model.addAttribute("groupOnlineStates", groupOnlineState);
	}
	
	@RequestMapping(value={"changeList/{groupid}/{activityid}"})
	public String changeList(@PathVariable String groupid, @PathVariable String activityid,HttpServletRequest request, HttpServletResponse response,Model model) {
		if(!StringUtil.isBlank(groupid)&&!StringUtil.isBlank(activityid)){
			List<Object> page = travelActivityService.searchChangedList(groupid,activityid);
			model.addAttribute("page", page);
		}
		return "include/changeList";
	};

	@ResponseBody
	@RequestMapping(value = {"checkIsExists"})
	public ResponseJson checkIsExists(HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		String activityId = request.getParameter("activityId");
		String groupId = request.getParameter("groupId");
		List<PricingStrategy> priningStrategy = activityPricingStrategyService.getActivityPrining(activityId, groupId);
		if(priningStrategy.size() == 0){
			json.setFlag(false);
		}else {
			json.setFlag(true);
		}
		return json;
	}
	
}

