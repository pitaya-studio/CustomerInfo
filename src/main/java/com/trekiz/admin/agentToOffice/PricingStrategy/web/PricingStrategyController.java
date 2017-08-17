package com.trekiz.admin.agentToOffice.PricingStrategy.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.AgentPriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.CheckedItem;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.service.PricingStrategyService;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value="${adminPath}/pricingStrategy/manager")
public class PricingStrategyController {
	
	@Autowired
	private TravelActivityService travelActivityService;
	
	@Autowired
	private QuauqAgentInfoService agentInfoService;
	
	@Autowired
	private PricingStrategyService pricingStrategyService;
	
	@Autowired
	private TouristLineService touristLineService;
	
	@RequestMapping(value={"list"})
	public String list(HttpServletRequest request, HttpServletResponse response,Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		//获取查询条件
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("companyId", companyId+"");
		getRequestParam(request,paramMap);
		getAgentParmMsg(model,companyId);
		getOfficeParmMsg(model,companyId);
		Page<PriceStrategy> page = pricingStrategyService.searchPriceStrategyBySupplyid(new Page<PriceStrategy>(request, response), paramMap);
		model.addAttribute("page", page);
		model.addAttribute("searchParm",paramMap);
		return "agentToOffice/pricingStrategy/pricingStrategyList"; 
	}
	@RequiresPermissions("price:update")
	@RequestMapping(value={"edit"})
	public String editPicingStrategy(HttpServletRequest request, HttpServletResponse response,Model model,@Param("priceStrategyId")Long priceStrategyId){
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("priceStrategyId", priceStrategyId);
		if(priceStrategyId != null){
			List<PriceStrategy> priceStrategy = pricingStrategyService.getPriceStrategy(priceStrategyId);
			if(priceStrategy != null && priceStrategy.size() == 1){
				//初始化选中的参数
				initParms(model,priceStrategy.get(0),companyId);
				initPriceDetail(model,priceStrategy.get(0),companyId);
				model.addAttribute("priceStrategy", true);
				return "agentToOffice/pricingStrategy/pricingStrategyUpdate";
			}else{
				//报错
			}
		}else{
			getAgentParmMsg(model,companyId);
			getOfficeParmMsg(model,companyId);
		}
		return "agentToOffice/pricingStrategy/pricingStrategyEdit";
	}
	
	@RequiresPermissions("price:add")
	@RequestMapping(value={"addt"})
	public String editPicingStrategyT(HttpServletRequest request, HttpServletResponse response,Model model,@Param("priceStrategyId")Long priceStrategyId){
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("priceStrategyId", priceStrategyId);
		if(priceStrategyId != null){
			List<PriceStrategy> priceStrategy = pricingStrategyService.getPriceStrategy(priceStrategyId);
			if(priceStrategy != null && priceStrategy.size() == 1){
				//初始化选中的参数
				initParms(model,priceStrategy.get(0),companyId);
				initPriceDetail(model,priceStrategy.get(0),companyId);
				model.addAttribute("priceStrategy", true);
				return "agentToOffice/pricingStrategy/pricingStrategyUpdate";
			}else{
				//报错
			}
		}else{
			getAgentParmMsg(model,companyId);
			getOfficeParmMsg(model,companyId);
		}
		return "agentToOffice/pricingStrategy/pricingStrategyEdit";
	}
	@RequestMapping(value={"add"})
	@ResponseBody
	public Object addPricingStrategy(HttpServletRequest request, HttpServletResponse response,Model model){
		JSONObject resobj = new JSONObject();
		Map<String,Object> formatParm = formatParm(request);
		if(formatParm != null && formatParm.get("errorMsg")==null){
			Long companyId = UserUtils.getUser().getCompany().getId();
			if(judgePriceStrategy(formatParm,companyId,null)){
				PriceStrategy  priceStrategy = (PriceStrategy)formatParm.get("priceStrategy");
				priceStrategy.setSupplyId(companyId);
				priceStrategy.setSupplyName(UserUtils.getUser().getCompany().getCompanyName());
				pricingStrategyService.add(priceStrategy);
				resobj.put("flag", "ok");
			}else{
				resobj.put("flag", "ALREADY_EXISTS");
			}
		}else if(formatParm.get("errorMsg")!=null){
			resobj.put("errorMsg", formatParm.get("errorMsg"));
		}
		return resobj;
	}
	
	@RequestMapping(value={"judgeExist"})
	@ResponseBody
	public Object judgeExist(HttpServletRequest request, HttpServletResponse response){
		JSONObject resobj = new JSONObject();
		Long priceStrategyId = request.getParameter("priceStrategyId")==null?null:new Long(request.getParameter("priceStrategyId"));
		Map<String,Object> formatParm = judgeExistParm(request);
		if(formatParm != null){
			Long companyId = UserUtils.getUser().getCompany().getId();
			if(judgePriceStrategy(formatParm,companyId,priceStrategyId)){
				resobj.put("flag", "ok");
			}else{
				resobj.put("flag", "ALREADY_EXISTS");
			}
		}
		return resobj;
	}
	
	private Map<String, Object> judgeExistParm(HttpServletRequest request) {
		String fromAreas = request.getParameter("fromAreas");
		String targetAreas = request.getParameter("targetAreas");
		String travelTypes = request.getParameter("travelTypes");
		String productTypes = request.getParameter("productTypes");
		String productLevels = request.getParameter("productLevels");
		
		List<String> fromAreaArray = new ArrayList<String>();
		List<String> targetAreaArray = new ArrayList<String>();;
		List<String> travelTypeArray = new ArrayList<String>();;
		List<String> productTypeArray = new ArrayList<String>();;
		List<String> productLevelArray = new ArrayList<String>();;
		
		Map<String,Object> objects = new HashMap<String,Object>();
		
		
		if(fromAreas != null && fromAreas != ""){
			Collections.addAll(fromAreaArray, fromAreas.split(","));
			Collections.sort(fromAreaArray);  
			objects.put("fromAreas", fromAreaArray);
		}
		if(targetAreas != null && targetAreas != ""){
			Collections.addAll(targetAreaArray, targetAreas.split(","));
			Collections.sort(targetAreaArray); 
			objects.put("targetAreas", targetAreaArray);
		}
		if(travelTypes != null && travelTypes != ""){
			Collections.addAll(travelTypeArray, travelTypes.split(","));
			Collections.sort(travelTypeArray);
			objects.put("travelTypes", travelTypeArray);
		}
		if(productTypes != null && productTypes != ""){
			Collections.addAll(productTypeArray, productTypes.split(","));
			Collections.sort(productTypeArray);
			objects.put("productTypes", productTypeArray);
		}
		if(productLevels != null && productLevels != ""){
			Collections.addAll(productLevelArray, productLevels.split(","));
			Collections.sort(productLevelArray);
			objects.put("productLevels", productLevelArray);
		}
		return objects;
	}
	
	@RequestMapping(value={"updateState"})
	@ResponseBody
	public Object updateState(HttpServletRequest request, HttpServletResponse response,Model model,@Param("priceStrategyId")Long priceStrategyId){
		JSONObject resobj = new JSONObject();
		if(priceStrategyId != null){
			PriceStrategy priceStratety = pricingStrategyService.findPriceStrategy(priceStrategyId);
			if(priceStratety.getState() == 1){
				priceStratety.setState(2);
				resobj.put("flag", "true");
			}else if(priceStratety.getState() == 2){
				priceStratety.setState(1);
				resobj.put("flag", "true");
			}else{
				//报错
				resobj.put("flag", "false");
				return resobj;
			}
			pricingStrategyService.updatePriceStrate(priceStratety);
		}else{
			resobj.put("flag", "false");
		}
		return resobj;
	}
	
	@RequestMapping(value={"delete"})
	@ResponseBody
	public Object deletePriceStrategy(HttpServletRequest request, HttpServletResponse response,Model model,@Param("priceStrategyId")Long priceStrategyId){
		JSONObject resobj = new JSONObject();
		if(priceStrategyId != null){
			//检查是否被使用，如果被使用，则不能删除
			PriceStrategy priceStratety = pricingStrategyService.findPriceStrategy(priceStrategyId);
			priceStratety.setDelFlag("1");
			for(AgentPriceStrategy pricingStrategy:priceStratety.getAgentPriceStrategySet()){
				pricingStrategy.setDelFlag("1");
			}
			pricingStrategyService.updatePriceStrate(priceStratety);
			resobj.put("flag", "true");
		}else{
			resobj.put("flag", "false");
		}
		return resobj;
	}
	

	@RequestMapping(value={"update"})
	@ResponseBody
	public Object updatePricingStrategy(HttpServletRequest request, HttpServletResponse response,Model model,@Param("priceStrategyId")Long priceStrategyId){
		//判断是否被使用，如果使用则不能更新
		JSONObject resobj = new JSONObject();
		PriceStrategy  priceStrategy = pricingStrategyService.findPriceStrategy(priceStrategyId);
		Integer state = priceStrategy.getState();
		pricingStrategyService.deleteAgentPriceStrates(priceStrategy.getAgentPriceStrategySet());
		if(priceStrategyId !=null && priceStrategy != null){
			//删除字表中的数据
			Map<String,Object> formatParm = formatParm(request);
			if(formatParm != null && formatParm.get("errorMsg") == null){
				//判断要更新的Id是否存在，如果存在则更新
				Long companyId = UserUtils.getUser().getCompany().getId();
				priceStrategy = (PriceStrategy)formatParm.get("priceStrategy");
				priceStrategy.setState(state);
				if(judgePriceStrategy(formatParm,companyId,priceStrategy.getId())){
					pricingStrategyService.updatePriceStrate(priceStrategy);
				}
			}else if(formatParm.get("errorMsg") != null){
				resobj.put("errorMsg",formatParm.get("errorMsg"));
				return resobj;
			}
		}
		resobj.put("flag", "ok");
		return resobj;
	}
	
	/**
	 * 根据价格策略单个参数初始化
	 * @param parFlag 
	 * @param model
	 * @param priceStrategy
	 * @return
	 */
	private Map<String,CheckedItem> initParm(Map<String,String> defaultParms,String checkedItems, String parFlag) {
		Map<String,CheckedItem> afterInitParms = new LinkedHashMap<String,CheckedItem>();
		String[] checkedItemArray = checkedItems.replace(",", " ").trim().split(" ");
		boolean hasNullParm = false;
		for(String checkedItem:checkedItemArray){
			if(checkedItem.equals("-1")){
				hasNullParm = true;
			}
		}
		if(hasNullParm && parFlag == null){
			CheckedItem item = new CheckedItem("（空）",true);
			afterInitParms.put("-1", item);
		}else if(!hasNullParm && parFlag == null){
			CheckedItem item = new CheckedItem("（空）",false);
			afterInitParms.put("-1", item);
		}
		for (Map.Entry<String, String> entry : defaultParms.entrySet()) {  
			boolean checkedFlag = false;
			for(String checkedItem:checkedItemArray){
				if(checkedItem.equals(entry.getKey())){
					checkedFlag = true;
				}
			}
			CheckedItem item = new CheckedItem(entry.getValue(),checkedFlag);
			afterInitParms.put(entry.getKey(), item);
		}  
		
		return afterInitParms;
	}
	
	private void initPriceDetail(Model model, PriceStrategy pariceStrategy,Long companyId){
		Map<Long,Map<String,Map<String,CheckedItem>>> agentPriceStrategyList = new HashMap<Long,Map<String,Map<String,CheckedItem>>>();
		for(AgentPriceStrategy agentPriceStrategy:pariceStrategy.getAgentPriceStrategySet()){
			Map<String,Map<String,CheckedItem>> agentPriceStrategySingle = new HashMap<String,Map<String,CheckedItem>>();
			//渠道类型
			List<Sysdefinedict> agentTypes = agentInfoService.getDefineDictByCompanyIdAndType(companyId,"agent_type");
			String[] agentTypeIds = agentPriceStrategy.getAgentTypeIds().replace(",", " ").trim().split(" ");
			Map<String,CheckedItem> afterInitAgentTypes = new HashMap<String,CheckedItem>();
			for(Sysdefinedict dict:agentTypes){
				boolean chedkedFlag = false;
				for(String agentType:agentTypeIds){
					if(agentType.equals(dict.getId()+"")){
						chedkedFlag = true;
					}
				}
				afterInitAgentTypes.put(dict.getId()+"", new CheckedItem(dict.getLabel(),chedkedFlag));
			}
			agentPriceStrategySingle.put("agentTypes", afterInitAgentTypes);
			//渠道等级
			List<Sysdefinedict> agentLevels = agentInfoService.getDefineDictByCompanyIdAndType(companyId,"agent_grade");
			String[] agentLevelIds = agentPriceStrategy.getAgentLevelIds().replace(",", " ").trim().split(" ");
			Map<String,CheckedItem> afterInitAgentLevels = new HashMap<String,CheckedItem>();
			for(Sysdefinedict dict:agentLevels){
				boolean chedkedFlag = false;
				for(String agentLevel:agentLevelIds){
					if(agentLevel.equals(dict.getId()+"")){
						chedkedFlag = true;
					}
				}
				afterInitAgentLevels.put(dict.getId()+"", new CheckedItem(dict.getLabel(),chedkedFlag));
			}
			agentPriceStrategySingle.put("agentLevels", afterInitAgentLevels);
			//成人优惠价
			String[] adultPriceStrategys = agentPriceStrategy.getAdultPriceStrategy().replace(",", " ").trim().split(" ");
			Map<String,CheckedItem> afterInitAdult = new LinkedHashMap<String,CheckedItem>();
			for(int i = 0; i < adultPriceStrategys.length; ++i){
				afterInitAdult.put(i+"", new CheckedItem(adultPriceStrategys[i].split(":")[0],adultPriceStrategys[i].split(":")[1],true));
			}
			agentPriceStrategySingle.put("adultPriceStrategys", afterInitAdult);
			//儿童优惠价
			if(agentPriceStrategy.getChildrenPriceStrategy()!= null && agentPriceStrategy.getChildrenPriceStrategy() != ""){
				String[] childrenPriceStrategys = agentPriceStrategy.getChildrenPriceStrategy().replace(",", " ").trim().split(" ");
				Map<String,CheckedItem> afterInitChildren = new LinkedHashMap<String,CheckedItem>();
				for(int j = 0; j < childrenPriceStrategys.length; ++j){
					afterInitChildren.put(j+"", new CheckedItem(childrenPriceStrategys[j].split(":")[0],childrenPriceStrategys[j].split(":")[1],true));
				}
				agentPriceStrategySingle.put("childrenPriceStrategys", afterInitChildren);
			}
			//特殊人群优惠价
			if(agentPriceStrategy.getSpecialPriceStrategy()!= null && agentPriceStrategy.getSpecialPriceStrategy() !=""){
				String[] specialPriceStrategys = agentPriceStrategy.getSpecialPriceStrategy().replace(",", " ").trim().split(" ");
				Map<String,CheckedItem> afterInitSpecial = new LinkedHashMap<String,CheckedItem>();
				for(int k = 0; k < specialPriceStrategys.length; ++k){
					afterInitSpecial.put(k+"", new CheckedItem(specialPriceStrategys[k].split(":")[0],specialPriceStrategys[k].split(":")[1],true));
				}
				agentPriceStrategySingle.put("specialPriceStrategys", afterInitSpecial);
			}
			agentPriceStrategyList.put(agentPriceStrategy.getId(), agentPriceStrategySingle);
		}
		model.addAttribute("priceDetail", agentPriceStrategyList);
		
	};
	/**
	 * 初始化价格策略页面显示实体
	 * @param model
	 * @param pariceStrategy
	 * @param companyId
	 */
	private void initParms(Model model, PriceStrategy pariceStrategy,Long companyId){
		//出发城市
		model.addAttribute("fromAreas", initParm(DictUtils.findUserDict(companyId,"fromarea"),pariceStrategy.getFromAreaIds(),"fromarea"));
		//目的城市
		List<TouristLine> areas = touristLineService.getAllTouristLine();
		String[] targetAreaIds = pariceStrategy.getTargetAreaIds().replace(",", " ").trim().split(" ");
		Map<String,CheckedItem> afterInitParms = new HashMap<String,CheckedItem>();
		for(TouristLine area: areas){
			boolean chedkedFlag = false;
			for(String targetArea:targetAreaIds){
				if(area.getId().toString().equals(targetArea)){
					chedkedFlag = true;
				}
			}
			afterInitParms.put(area.getId().toString(), new CheckedItem(area.getLineName().toString(),chedkedFlag));
		}
		model.addAttribute("targetAreaIds",afterInitParms);
		//旅游类型
		model.addAttribute("travelTypes", initParm(DictUtils.getValueAndLabelMap("travel_type",companyId),pariceStrategy.getTravelTypeIds(),null));
		//产品类型
		model.addAttribute("productTypes", initParm(DictUtils.getValueAndLabelMap("product_type",companyId),pariceStrategy.getActivityTypeIds(),null));
		//产品系列
		model.addAttribute("productLevels", initParm(DictUtils.getValueAndLabelMap("product_level",companyId),pariceStrategy.getProductLevelIds(),null));
	}


	/**
	 * 将请求参数格式化
	 * @param request
	 * @return
	 */
	private Map<String,Object> formatParm(HttpServletRequest request){
		
		String fromAreas = request.getParameter("fromAreas");
		String targetAreas = request.getParameter("targetAreas");
		String travelTypes = request.getParameter("travelTypes");
		String productTypes = request.getParameter("productTypes");
		String productLevels = request.getParameter("productLevels");
		String priceTrategyMsgsValue = request.getParameter("priceTrategyMsgsValue");
		
		List<String> fromAreaArray = new ArrayList<String>();
		List<String> targetAreaArray = new ArrayList<String>();;
		List<String> travelTypeArray = new ArrayList<String>();;
		List<String> productTypeArray = new ArrayList<String>();;
		List<String> productLevelArray = new ArrayList<String>();;
		
		PriceStrategy priceStrategy = new PriceStrategy();
		Set<AgentPriceStrategy> priceSet = new HashSet<AgentPriceStrategy>();
		if(request.getParameter("priceStrategyId") != null && request.getParameter("priceStrategyId") != ""){
			priceStrategy = pricingStrategyService.findPriceStrategy(new Long( request.getParameter("priceStrategyId")));
		}
		Map<String,Object> objects = new HashMap<String,Object>();
		
		
		if(fromAreas != null && fromAreas != ""){
			Collections.addAll(fromAreaArray, fromAreas.split(","));
			Collections.sort(fromAreaArray);  
			priceStrategy.setFromAreaIds(","+fromAreas.toString()+",");
			priceStrategy.setFromAreaNames( request.getParameter("fromAreaNames"));
			objects.put("fromAreas", fromAreaArray);
		}
		if(targetAreas != null && targetAreas != ""){
			Collections.addAll(targetAreaArray, targetAreas.split(","));
			Collections.sort(targetAreaArray); 
			if(touristLineService.isExist(targetAreaArray)){
				priceStrategy.setTargetAreaIds(","+targetAreas.toString()+",");
				priceStrategy.setTargetAreaNames(request.getParameter("targetAreaNames"));
				objects.put("targetAreas", targetAreaArray);
			}else{
				objects.put("errorMsg", "要添加的线路不存在，不能添加！");
				return objects;
			}
		}
		if(travelTypes != null && travelTypes != ""){
			Collections.addAll(travelTypeArray, travelTypes.split(","));
			Collections.sort(travelTypeArray);
			if(DictUtils.isExist(travelTypeArray,"travel_type")){
				priceStrategy.setTravelTypeIds(","+travelTypes.toString()+",");
				priceStrategy.setTravelTypeNames(request.getParameter("travelTypeNames"));
				objects.put("travelTypes", travelTypeArray);
			}else{
				objects.put("errorMsg", "要添加的旅游类型不存在，不能添加");
				return objects;
			}
		}
		if(productTypes != null && productTypes != ""){
			Collections.addAll(productTypeArray, productTypes.split(","));
			Collections.sort(productTypeArray);
			if(DictUtils.isExist(productTypeArray,"product_type")){
				priceStrategy.setActivityTypeIds(","+productTypes.toString()+",");
				priceStrategy.setActivityTypeIdNames(request.getParameter("productTypeNames"));
				objects.put("productTypes", productTypeArray);
			}else{
				objects.put("errorMsg", "要添加的产品类型不存在，不能添加");
				return objects;
			}
		}
		if(productLevels != null && productLevels != ""){
			Collections.addAll(productLevelArray, productLevels.split(","));
			Collections.sort(productLevelArray);
			if(DictUtils.isExist(productLevelArray,"product_level")){
				priceStrategy.setProductLevelIds(","+productLevels.toString()+",");
				priceStrategy.setProductLevelNames(request.getParameter("productLevelNames"));
				objects.put("productLevels", productLevelArray);
			}else{
				objects.put("errorMsg", "要添加的产品系列不存在，不能添加");
				return objects;
			}
		}
		if(priceTrategyMsgsValue != null && priceTrategyMsgsValue != ""){
			
			Set<Integer> discountIds = new HashSet<Integer>();
			String[] temArray = priceTrategyMsgsValue.split("\\+");
			for(int i = 0; i < temArray.length; ++i){
				String[] varValues = temArray[i].split("-");
				AgentPriceStrategy agentPriceStrategy = new AgentPriceStrategy();
				/*if(varValues.length == 6){
					agentPriceStrategy.setId(new Long(varValues[5]));
				}*/
				//渠道类型
				if(agentInfoService.isExist(varValues[0])){
					agentPriceStrategy.setAgentTypeIds(","+varValues[0]);
				}else{
					objects.put("errorMsg", "要添加的渠道类型不存在，不能添加");
					return objects;
				}
				//渠道类型名称
				agentPriceStrategy.setAgentTypeNames(varValues[1].substring(0,varValues[1].length()-1));
				//渠道等级
				if(agentInfoService.isExist(varValues[2])){
					agentPriceStrategy.setAgentLevelIds(","+varValues[2]);
				}else{
					objects.put("errorMsg", "要添加的渠道等级不存在，不能添加");
					return objects;
				}
				//渠道等级名称
				agentPriceStrategy.setAgentLevelNames(varValues[3].substring(0,varValues[3].length()-1));
				//成人价格
				String[] adultPrice = varValues[4].split(",");
				String adultPriceIds = "";
				String priceStrateDesc = "成人：";
				for(int n = 0; n < adultPrice.length; ++n){
					String[] tempAdult = adultPrice[n].split(":");
					adultPriceIds = adultPriceIds+","+tempAdult[0].split("_")[0]+":"+tempAdult[1];
					discountIds.add(new Integer(tempAdult[0].split("_")[0]));
					if(tempAdult[0].split("_")[0] != null && tempAdult[0].split("_")[0].trim().equals("3")){
						priceStrateDesc = priceStrateDesc + tempAdult[0].split("_")[1]+tempAdult[1]+"%&";
					}else{
						priceStrateDesc = priceStrateDesc + tempAdult[0].split("_")[1]+tempAdult[1]+"&";
					}
				}
				agentPriceStrategy.setAdultPriceStrategy(adultPriceIds+",");
				priceStrateDesc = priceStrateDesc.substring(0,priceStrateDesc.length()-1);
				
				if(varValues.length>5){
					for(int inx = 5; inx < varValues.length; ++inx){
						//儿童价格
						String[] tempPrice =  varValues[inx].split(",");
						String tempPriceIds = "";
						String tempPriceStrategyDesc = "";
						String whichOne = "";
						for(int n = 0; n < tempPrice.length; ++n){
							String[] tempChildren = tempPrice[n].split(":");
							tempPriceIds = tempPriceIds +","+tempChildren[0].split("_")[0]+":"+tempChildren[1].split("_")[0];
							discountIds.add(new Integer(tempChildren[0].split("_")[0]));
							if(tempChildren[0].split("_")[0] != null && tempChildren[0].split("_")[0].trim().equals("3")){
								tempPriceStrategyDesc = tempPriceStrategyDesc+tempChildren[0].split("_")[1]+tempChildren[1].split("_")[0]+"%&";
							}else{
								tempPriceStrategyDesc = tempPriceStrategyDesc+tempChildren[0].split("_")[1]+tempChildren[1].split("_")[0]+"&";
							}
							whichOne = tempChildren[1].split("_")[1];
						}
						if(whichOne.equals("child")){
							priceStrateDesc = priceStrateDesc+"<br>"+"儿童："+tempPriceStrategyDesc;
							agentPriceStrategy.setChildrenPriceStrategy(tempPriceIds+",");
						}else{
							priceStrateDesc = priceStrateDesc+"<br>"+"特殊人群："+tempPriceStrategyDesc;
							agentPriceStrategy.setSpecialPriceStrategy(tempPriceIds+",");
						}
						priceStrateDesc = priceStrateDesc.substring(0,priceStrateDesc.length()-1);
					}
				}
				
				//特殊人群价格
		/*		String[] specialPrice = varValues[4].split(",");
				String specialPriceIds = "";
				priceStrateDesc = priceStrateDesc +"<br>" +"特殊人群：";
				for(int n = 0; n < specialPrice.length; ++n){
					String[] tempSpecial = specialPrice[n].split(":");
					specialPriceIds = specialPriceIds+","+tempSpecial[0].split("_")[0]+":"+tempSpecial[1];
					discountIds.add(new Integer(tempSpecial[0].split("_")[0]));
					if(tempSpecial[0].split("_")[0] != null && tempSpecial[0].split("_")[0].trim().equals("2")){
						priceStrateDesc = priceStrateDesc+tempSpecial[0].split("_")[1]+tempSpecial[1]+"%&";
					}else{
						priceStrateDesc = priceStrateDesc+tempSpecial[0].split("_")[1]+tempSpecial[1]+"&";
					}
				}
				priceStrateDesc = priceStrateDesc.substring(0,priceStrateDesc.length()-1);
				agentPriceStrategy.setSpecialPriceStrategy(specialPriceIds+",");*/
				String discountIdsStr = ",";
				for(int discountId:discountIds){
					discountIdsStr = discountIdsStr +discountId+",";
				}
				agentPriceStrategy.setDiscountIds(discountIdsStr);
				agentPriceStrategy.setPriceStrategyDesc(priceStrateDesc);
				agentPriceStrategy.setPriceStrategy(priceStrategy);
				priceSet.add(agentPriceStrategy);
			}
			priceStrategy.setAgentPriceStrategySet(priceSet);
			priceStrategy.setState(1);
		}else{
			return null;
		}
		objects.put("priceStrategy", priceStrategy);
		return objects;
	}
	
	/**
	 * 判断是否存在已有策略
	 * @param objects
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean judgePriceStrategy(Map<String,Object> objects,Long companyId,Long StrategyId){
		List<String> fromAreaArray = (List<String>)objects.get("fromAreas");
		List<String> targetAreaArray = (List<String>)objects.get("targetAreas");
		List<String> travelTypeArray = (List<String>)objects.get("travelTypes");
		List<String> productTypeArray = (List<String>)objects.get("productTypes");
		List<String> productLevelArray = (List<String>)objects.get("productLevels");
		for(String fromArea:fromAreaArray){
			for(String targetArea:targetAreaArray){
				for(String travelType:travelTypeArray){
					for(String productType:productTypeArray){
						for(String productLevel:productLevelArray){
							//查询是否存在满足条件的价格策略
							if(pricingStrategyService.existPriceStrategy(companyId,fromArea,targetArea,travelType,productType,productLevel,StrategyId)) return false;
						}
					}
				}
			}
		}
		return true;
	};
	
	
	/**
	 * 获取查询请求参数请求参数
	 * @param request
	 * @param paramMap
	 */
	private void getRequestParam(HttpServletRequest request,Map<String, String> paramMap) {
		paramMap.put("fromArea", request.getParameter("fromArea"));
		paramMap.put("targetArea", request.getParameter("targetArea"));
		paramMap.put("agentType", request.getParameter("agentType"));
		paramMap.put("agentLevel", request.getParameter("agentLevel"));
		paramMap.put("travelType", request.getParameter("travelType"));
		paramMap.put("productType", request.getParameter("productType"));
		paramMap.put("productLevel", request.getParameter("productLevel"));
		paramMap.put("favorableType", request.getParameter("favorableType"));
	}


	/**
	 * 获取批发商对应的渠道策略参数
	 * @param model
	 * @param companyId
	 */
	public void getAgentParmMsg(Model model,Long companyId){
		//出发城市
		model.addAttribute("fromAreas", DictUtils.findUserDict(companyId,"fromarea"));
		//目的城市
		model.addAttribute("targetAreaIds", touristLineService.getAllTouristLine());
		//旅游类型
		model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",companyId));
		//产品类型
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",companyId));
		//产品系列
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",companyId));
	}
	
	/**
	 * 获取渠道策略参数
	 * @param model
	 */
	public void getOfficeParmMsg(Model model,Long companyId){
		//渠道类型
		model.addAttribute("agentTypes", agentInfoService.getDefineDictByCompanyIdAndType(companyId,"agent_type"));
		//渠道等级
		model.addAttribute("agentLevels", agentInfoService.getDefineDictByCompanyIdAndType(companyId,"agent_grade"));
		//优惠内容
//		model.addAttribute("favorableTypes", DictUtils.getDictList("favorable_type"));
	}
	
}
