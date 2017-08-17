package com.trekiz.admin.modules.eprice.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;
import com.trekiz.admin.modules.eprice.entity.EstimatePricePartReply;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ListSearchForm;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitLinesAreaDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceFileDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceTrafficLineDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePricerReplyDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceAdmitRequirementsService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceBaseInfoService;
import com.trekiz.admin.modules.eprice.service.EstimatePricePartReplyService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceProjectService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficLineService;
import com.trekiz.admin.modules.eprice.service.EstimatePricerReplyService;
import com.trekiz.admin.modules.eprice.utils.EstimatePriceUtils;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;
import com.trekiz.admin.modules.epriceDistribution.query.EstimatePriceDistributionQuery;
import com.trekiz.admin.modules.epriceDistribution.service.EstimatePriceDistributionService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/eprice/manager/project")
public class EstimatePriceProjectController {
	
	@Resource
	private EstimatePriceProjectService estimatePriceProjectService;
	
	@Resource
	private EstimatePriceRecordService estimatePriceRecordService;
	
	@Autowired
	private EstimatePricerReplyService estimatePricerReplyService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	protected LogOperateService logOpeService;

	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private EstimatePriceDistributionService estimatePriceDistributionService;

    @ModelAttribute("menuId")
    protected Integer getMenuId(){
        return 210;//213
    }

	@Resource
    private DepartmentService departmentService;
	
	@Autowired
	private EstimatePriceFileDao estimatePriceFileDao;
	@Autowired
	private EstimatePriceTrafficLineDao estimatePriceTrafficLineDao;
	
	@Autowired
	private EstimatePriceAdmitLinesAreaDao estimatePriceAdmitLinesAreaDao;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private EstimatePricePartReplyService estimatePricePartReplyService;
	
	@Autowired
	private EstimatePricerReplyDao estimatePricerReplyDao;
	@Autowired
	private EstimatePriceBaseInfoService estimatePriceBaseInfoService;
	@Autowired
	private EstimatePriceAdmitRequirementsService estimatePriceAdmitRequirementsService;
	@Autowired
	private EstimatePriceTrafficLineService estimatePriceTrafficLineService;

	private List<EstimatePriceProject> result;
	/**
	 * 530-导出地接社询价单
	 * @param lsf
	 * @param model
	 * @param request
	 * @param response
	 * @author zhanyu.gu
	 */
	@RequestMapping(value="exportEstimateExcel")
	public void exportEstimateExcel(@Valid ListSearchForm lsf,Model model ,HttpServletRequest request,HttpServletResponse response){
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String time = df.format(new Date());
			
			StringBuffer filename = new StringBuffer();
			filename.append("地接社询价记录");
			filename.append(time);
			DepartmentCommon common = departmentService.setDepartmentPara("inquiry", model);
			String pnstr = request.getParameter("pageNo");
			@SuppressWarnings("unused")
			String psstr = request.getParameter("pageSize");
			Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
			Integer pageSize = 1000;
			//将计调的id置为null，以便导出所有的询价记录
			//Long operatorId = UserUtils.getUser().getId();
			lsf.setOperatorUid(null);
			Page<EstimatePriceProject> page = estimatePriceProjectService.findByPage(lsf, pageSize, pageNo, common);
			List<Object[]> resultList = changeValueTypeForExport(page);
			
			ExportExcel.excelEstimateExcel(filename.toString(),resultList,request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 530-导出计调主管询价单
	 * @param lsf
	 * @param model
	 * @param request
	 * @param response
	 * @author zhanyu.gu
	 */
	
	@RequestMapping(value="exportManagerEstimateExcel")
	public void exportManagerEstimateExcel(@Valid ListSearchForm lsf,Model model ,HttpServletRequest request,HttpServletResponse response){
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String time = df.format(new Date());
			String pnstr = null;
			String psstr = null;
			Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
			/*Integer pageSize = psstr!=null?Integer.valueOf(psstr):1000;*/
			Integer pageSize = 1000;
			//获取计调主管的id
			/*Long operatorId = UserUtils.getUser().getId();
			lsf.setOperatorUid(operatorId);*/
			// 查询计调主管全部单团项目询价
			Page<EstimatePriceProject> page = estimatePriceProjectService.findListByPageForManager(lsf, pageSize, pageNo);
			List<Object[]> resultList = changeValueTypeForExport(page);
			String filename = "计调主管询价记录"+time;
			ExportExcel.excelEstimateExcel(filename,resultList,request,response);
			}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	/**
	 * 将查到的数据进行筛选封装作为输出到excel的数据 -- 仅针对530需求
	 * @param page
	 * @return
	 * @author zhanyu.gu
	 */
	@SuppressWarnings("unused")
	public List<Object[]>  changeValueTypeForExport(Page page){
		List<EstimatePriceProject> result = page.getResult();
		List<Object[]> resultList = new ArrayList<Object[]>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for(EstimatePriceProject estimatePrceProject:result){
			
			Object[] resultObject = new Object[7];
			EstimatePriceBaseInfo lastBaseInfo = estimatePrceProject.getLastBaseInfo();
			EstimatePriceAdmitRequirements lastAdmitRequirements = estimatePrceProject.getLastAdmitRequirements();
			Integer estimatePriceSum = estimatePrceProject.getEstimatePriceSum();
			String salerName = "";
			Date createTime =null;
			String createTimeForExport = "";
			String customerName = "";
			String travelCountry ="";
			Date dgroupOutDate2 = null;
			Integer estimateStatus = null;
			String dgroupOutDate = "";
			String status = "";
				if(estimatePriceSum > 1){
					Long id = estimatePrceProject.getId();
					List<EstimatePriceRecord> recordList = estimatePriceRecordService.getAoperatorUserIdByPid(id);
					
					List<EstimatePriceBaseInfo> baseList = estimatePriceBaseInfoService.findByPid(id);
					List<EstimatePriceAdmitRequirements> countryList = estimatePriceAdmitRequirementsService.findByPid(id);
					
					for(int a = 0; a < recordList.size(); a++){
						StringBuffer aopName = new StringBuffer();
						List<Object> aopList = new ArrayList<Object>();
						 createTime = recordList.get(a).getCreateTime();
						 estimateStatus = recordList.get(a).getEstimateStatus();
						 salerName = baseList.get(a).getSalerName();
						 customerName = baseList.get(a).getCustomerName();
						 if(estimatePrceProject.getType() != 7){
							 travelCountry = countryList.get(a).getTravelCountry();
							 dgroupOutDate2 = countryList.get(a).getDgroupOutDate();
							//获取计调信息
							 String aoperatorUserJson2 = recordList.get(a).getAoperatorUserJson();
							 if(aoperatorUserJson2 != null){
									JSONArray jsonarray = JSONArray.fromObject(aoperatorUserJson2);  
							        List list = (List)JSONArray.toList(jsonarray, User.class);  
							        Iterator it = list.iterator();  
							        while(it.hasNext()){  
							        	User p = (User)it.next(); 
							        	String userName = p.getUserName();
							        	aopList.add(userName);
							        }
								}else{
									aoperatorUserJson2 = " ";
									}
						 }else{
							 EstimatePriceTrafficLine gettrafficeLine = gettrafficeLine(estimatePrceProject);
							dgroupOutDate2 = gettrafficeLine.getStartDate();
							String startCityName = gettrafficeLine.getStartCityName();
							String endCityName = gettrafficeLine.getEndCityName();
							travelCountry = startCityName +" "+endCityName;
							//获取机票计调信息
							 String aoperatorUserJson2 = recordList.get(a).getToperatorUserJson();
							 if(aoperatorUserJson2 != null){
									JSONArray jsonarray = JSONArray.fromObject(aoperatorUserJson2);  
							        List list = (List)JSONArray.toList(jsonarray, User.class);  
							        Iterator it = list.iterator();  
							        while(it.hasNext()){  
							        	User p = (User)it.next(); 
							        	String userName = p.getUserName();
							        	aopList.add(userName);
							        }
								}else{
									aoperatorUserJson2 = " ";
									}
							
						 }
						
						 
						 
					for(Object o: aopList){
						aopName.append(o+"  ");
					}
					
					if(dgroupOutDate2 != null){
						dgroupOutDate = formatter.format(dgroupOutDate2);
					}else{
						dgroupOutDate ="";
					}
					
					String replace = travelCountry.replace("[", "");
					String replace2 = replace.replace("]", "");
					travelCountry = replace2.replaceAll("\"", " ");
					
					//estimateStatus = estimatePrceProject.getEstimateStatus();
					if(estimateStatus == 0){
						status = "已取消";
					}else if(estimateStatus == 1 ){
						status = "待报价";
					}else if(estimateStatus == 2 ){
						status = "已报价";
					}else if(estimateStatus == 3 ){
						status = "确定报价";
					}else if(estimateStatus == 4 ){
						status = "已发产品";
					}else if(estimateStatus == 5 ){
						status = "已生成订单";
					}else if(estimateStatus == 6 ){
						status = "待分配";
					}
					Object[] resultObject1 = new Object[7];
					resultObject1[0] = salerName;
					resultObject1[1] = formatter.format(createTime);
					resultObject1[2] = customerName;
					resultObject1[3] = dgroupOutDate;
					resultObject1[4] = travelCountry;
					resultObject1[5] = aopName;
					resultObject1[6] = status;
					resultList.add(resultObject1);
					}
					
				}else{
				/*if(lastAdmitRequirements != null){*/
					
					salerName = lastBaseInfo.getSalerName();
					createTime = lastBaseInfo.getCreateTime();
					createTimeForExport = formatter.format(createTime);
					customerName = lastBaseInfo.getCustomerName();
					List<Object> aopList = new ArrayList<Object>();
					StringBuffer aopName = new StringBuffer();
					dgroupOutDate2 = null;
					estimateStatus = null;
					//获取计调信息
					Long pid = estimatePrceProject.getId();
					List<EstimatePriceRecord> aoperatorUserIdByPid = estimatePriceRecordService.getAoperatorUserIdByPid(pid);
					if(lastAdmitRequirements != null){
						dgroupOutDate2 = lastAdmitRequirements.getDgroupOutDate();
						travelCountry = lastAdmitRequirements.getTravelCountry();
						for(EstimatePriceRecord e : aoperatorUserIdByPid){
							String aoperatorUserJson2 = e.getAoperatorUserJson();
							if(aoperatorUserJson2 != null){
								JSONArray jsonarray = JSONArray.fromObject(aoperatorUserJson2);  
						        List list = (List)JSONArray.toList(jsonarray, User.class);  
						        Iterator it = list.iterator();  
						        while(it.hasNext()){  
						        	User p = (User)it.next();  
						            aopList.add(p.getUserName());
						        } 
							}else{
								aoperatorUserJson2 = " ";
								}
						}
					}else{
						for(EstimatePriceRecord e : aoperatorUserIdByPid){
							String aoperatorUserJson2 = e.getToperatorUserJson();
							if(aoperatorUserJson2 != null){
								JSONArray jsonarray = JSONArray.fromObject(aoperatorUserJson2);  
						        List list = (List)JSONArray.toList(jsonarray, User.class);  
						        Iterator it = list.iterator();  
						        while(it.hasNext()){  
						        	User p = (User)it.next();  
						            aopList.add(p.getUserName());
						        } 
							}else{
								aoperatorUserJson2 = " ";
								}
						}
						
						EstimatePriceTrafficLine gettrafficeLine = gettrafficeLine(estimatePrceProject);
						dgroupOutDate2 = gettrafficeLine.getStartDate();
						String startCityName = gettrafficeLine.getStartCityName();
						String endCityName = gettrafficeLine.getEndCityName();
						travelCountry = startCityName +" "+endCityName;
					}
					
						
					for(Object o: aopList){
						aopName.append(o+"  ");
					}
					
					
					dgroupOutDate = "";
					if(dgroupOutDate2 != null){
						dgroupOutDate = formatter.format(dgroupOutDate2);
					}else{
						dgroupOutDate ="";
					}
					
					String replace = travelCountry.replace("[", "");
					String replace2 = replace.replace("]", "");
					travelCountry = replace2.replaceAll("\"", " ");
					status = "";
					estimateStatus = estimatePrceProject.getEstimateStatus();
					if(estimateStatus == 0){
						status = "已取消";
					}else if(estimateStatus == 1 ){
						status = "待报价";
					}else if(estimateStatus == 2 ){
						status = "已报价";
					}else if(estimateStatus == 3 ){
						status = "确定报价";
					}else if(estimateStatus == 4 ){
						status = "已发产品";
					}else if(estimateStatus == 5 ){
						status = "已生成订单";
					}else if(estimateStatus == 6 ){
						status = "待分配";
					}
					
					resultObject[0] = salerName;
					resultObject[1] = createTimeForExport;
					resultObject[2] = customerName;
					resultObject[3] = dgroupOutDate;
					resultObject[4] = travelCountry;
					resultObject[5] = aopName;
					resultObject[6] = status;
					resultList.add(resultObject);
					//}
					
			}
		}
		return resultList;
	}
	/**
	 * 通过询价项目获取机票询价的线路
	 * @param estimatePrceProject
	 * @return
	 */
	public EstimatePriceTrafficLine gettrafficeLine(EstimatePriceProject estimatePrceProject){
		EstimatePriceTrafficRequirements lastTrafficRequirements = estimatePrceProject.getLastTrafficRequirements();
		Long id = lastTrafficRequirements.getId();
		Integer trafficType = lastTrafficRequirements.getTrafficType();
		List<EstimatePriceTrafficLine> findByPfid = estimatePriceTrafficLineService.findByPfid(id);
		for(EstimatePriceTrafficLine e :findByPfid){
			String startCityName = e.getStartCityName();
			Date startDate = e.getStartDate();
		}
		EstimatePriceTrafficLine estimatePriceTrafficLine = findByPfid.get(0);
		
		return estimatePriceTrafficLine;
	}
	/**
	 * 获取创建询价项目页面
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param type 创建询价项目类型  a:单团询价项目，t:票务询价项目
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	//@RequiresPermissions("eprice:reply:save")
	@RequestMapping(value="add-{type}-{emode}")
	public String addEPriceProjectPage(@PathVariable("type") String type, @PathVariable("emode") String emode, Model model,HttpServletRequest request, HttpServletResponse response){
		//接待计调用户列表
		List<User> aoperatorUsers = getAoperatorUsers(1);
		
		//机票计调用户列表
		List<User> toperatorUsers = getAoperatorUsers(1);
		
		// 获取询价客户列表（同行）,即渠道商列表
		List<Agentinfo> agentInfoList = new ArrayList<>();
		if (Context.SUPPLIER_UUID_DHJQ.equals(UserUtils.getUser().getCompany().getUuid())) {  // 鼎鸿假期 不要限制
			agentInfoList = agentinfoService.findAllAgentinfosWithoutPemission(UserUtils.getUser().getCompany().getId());
		} else {
			agentInfoList = agentinfoService.findAllAgentinfo();
		}
		// 获取当前用户的线路国家
		List<Map<String,Object>> areamapList = getCountryList();
		
		// 获取城市列表
		model.addAttribute("airportCityList",areaService.findAirportCityList(null)); //机场城市(到达城市)
		model.addAttribute("fromareaslist", areaService.findFromCityList(null));// 出发城市
		model.addAttribute("outAreaList",DictUtils.getDictsByType(null, "outarea"));// 离境口岸\

		model.addAttribute("aoperators", aoperatorUsers);
		model.addAttribute("toperators", toperatorUsers);
		model.addAttribute("saler", UserUtils.getUser());
		model.addAttribute("customerAgents", agentInfoList);
		model.addAttribute("areamapList", areamapList);
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		
		model.addAttribute("teamTypeShow",EstimatePriceUtils.TYPE_SHOW.get(type));
		model.addAttribute("teamType",type);
		model.addAttribute("type",type);
		model.addAttribute("emode", emode);
		// 跳转页面连接
		String view;
		// 询价对象选择模式： 1：询问计调（默认） 2：询问主管 3：询价对象可选
		Office office = UserUtils.getUser().getCompany();
		if(office!=null && office.getEstimateModel()!=null && (office.getEstimateModel()==2 || (office.getEstimateModel() == 3 && "2".equals(emode)))){  // 询问主管、可选但新增时候选择询主管
			// 获取计调主管列表
			List<User> managerList = systemService.findRoleByOffice(Context.ROLE_TYPE_OP_EXECUTIVE, office.getId().toString());
			if(!managerList.isEmpty()){
				
				// 拉美途计调主管只保留陈卡卡
				String companyUuid = UserUtils.getCompanyUuid();
				if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					if (CollectionUtils.isNotEmpty(managerList)) {
						Iterator<User> it = managerList.iterator();
						while(it.hasNext()) {
							long userId = it.next().getId();
							if (userId != 816) {
								it.remove();
							}
						}
					}
				}
				
				model.addAttribute("managerList",managerList);
			}
			if(String.valueOf(EstimatePriceProject.TYPE_FLIGHT).equals(type)){ 
				view = "modules/eprice/addTrafficEPriceForManager";// 机票询价申请(计调主管分配计调)
			}else{
				view = "modules/eprice/addAloneEPriceForManager";// 单团类询价申请(计调主管分配计调)
			}
		}else{
			if(String.valueOf(EstimatePriceProject.TYPE_FLIGHT).equals(type)){
				view = "modules/eprice/addTrafficEPrice";// 机票询价申请
			}else{
				view = "modules/eprice/addAloneEPrice";// 单团类询价申请
			}
		}
		return view;
	}
	
	/**
	 * 再次询价
	 * @param type
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
    @RequestMapping(value="onceagain/{type}/{rid}")
	public String onceAgainRecord(@PathVariable("type") String type,@PathVariable("rid") Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		//接待计调用户列表
		List<User> aoperatorUsers = getAoperatorUsers(1);
		
		//机票计调用户列表
		List<User> toperatorUsers = getAoperatorUsers(1);
		
		// 获取询价客户列表（同行）,即渠道商列表
		List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();
		// 获取当前用户的线路国家
		List<Map<String,Object>> areamapList = getCountryList();
		
		// 再次询价标记
		Integer onceAgain = 1;
		// 跳转地址
		String view;
		// 询价项目
		EstimatePriceRecord record = new EstimatePriceRecord();
		EstimatePriceBaseInfo baseInfo = new EstimatePriceBaseInfo();
		EstimatePriceAdmitRequirements admitRequirements = new EstimatePriceAdmitRequirements();
		// 通过询价记录项目id，得到询价记录
		if(rid!=null){
			record = estimatePriceRecordService.findById(rid);
		}else{
			return null;
		}
		if(record!=null){
			baseInfo = record.getBaseInfo();
			admitRequirements = record.getAdmitRequirements();
			// 修正下日期格式
			if(admitRequirements!=null){
				Date date = admitRequirements.getDgroupOutDate();
				String dateStr = DateUtils.formatCustomDate(date, "yyyy-MM-dd");
				model.addAttribute("dgroupOutDate",dateStr);
			}
		}else{
			return null;
		}
		
		if(baseInfo.getAoperatorUserJson()!=null){
			JSONArray jsonArray = JSONArray.fromObject(baseInfo.getAoperatorUserJson());
			List<String> strAop = (List<String>)jsonArray.toCollection(jsonArray);
			model.addAttribute("aopIdList", strAop);
		}
		
		// 读取接待社计调要求的JSON串
		if(admitRequirements!=null&&admitRequirements.getTravelRequirements()!=null){
			JSONObject jsonObj = JSONObject.fromObject(admitRequirements.getTravelRequirements());
			
//			JSONObject travelTeamType = jsonObj.getJSONObject("travelTeamType");
//			model.addAttribute("travelTeamType",travelTeamType.get("type"));
//			model.addAttribute("travelTeamTitle",travelTeamType.get("title"));
			
			JSONObject visaType = jsonObj.getJSONObject("visaType");
			if(visaType!=null){
//				model.addAttribute("visaType",visaType.get("type"));
//				model.addAttribute("visaTitle",visaType.get("title"));
				model.addAttribute("visaType",visaType.toString());
			}
			
			JSONObject hotelType = jsonObj.getJSONObject("hotelType");
			if(hotelType!=null){
//				model.addAttribute("hotelType",hotelType.get("type"));
//				model.addAttribute("hotelTitle",hotelType.get("title"));
				model.addAttribute("hotelType",hotelType.toString());
			}
			
			JSONObject hotelPosition = jsonObj.getJSONObject("hotelPosition");
			if(hotelPosition!=null){
//				model.addAttribute("hotelPosition",hotelPosition.get("type"));
//				model.addAttribute("hotelPositionTitle",hotelPosition.get("title"));
				model.addAttribute("hotelPosition",hotelPosition.toString());
			}
			
			JSONObject roomType = jsonObj.getJSONObject("roomType");
			if(roomType!=null){
//				model.addAttribute("roomType",roomType.get("type"));
//				model.addAttribute("roomTitle",roomType.get("title"));
				model.addAttribute("roomType",roomType.toString());
			}
			
			JSONObject carType = jsonObj.getJSONObject("carType");
			if(carType!=null){
//				model.addAttribute("carType",carType.get("type"));
//				model.addAttribute("carTitle",carType.get("title"));
//				// 大车座位
//				if(carType.get("info")!=null){
//					JSONObject sitObj = carType.getJSONObject("info");
//					String sit = sitObj.getString("value");
//					model.addAttribute("carSit",sit);
//				}
				model.addAttribute("carType",carType.toString());
			}
			
			
			JSONObject guideTyp = jsonObj.getJSONObject("guideTyp");
			if(guideTyp!=null){
//				model.addAttribute("guideTyp",guideTyp.get("type"));
//				model.addAttribute("guideTitle",guideTyp.get("title"));
				model.addAttribute("guideTyp",guideTyp.toString());
			}
			
			JSONObject leaderType = jsonObj.getJSONObject("leaderType");
			if(leaderType!=null){
//				model.addAttribute("leaderType",leaderType.get("type"));
//				model.addAttribute("leaderTitle",leaderType.get("title"));
				model.addAttribute("leaderType",leaderType.toString());
			}
			
			JSONObject attractionType = jsonObj.getJSONObject("attractionType");
			if(attractionType!=null){
//				model.addAttribute("attractionType",attractionType.get("type"));
//				model.addAttribute("attractionTitle",attractionType.get("title"));
				model.addAttribute("attractionType",attractionType.toString());
			}
			// 公务活动
			JSONObject publicWordType = jsonObj.getJSONObject("publicWordType");
			if(publicWordType!=null){
//				model.addAttribute("publicWordType",publicWordType.get("type"));
//				model.addAttribute("publicWordTitle",publicWordType.get("title"));
//				// 公务活动安排城市
//				if(publicWordType.get("info")!=null){
//					JSONObject cityObj = publicWordType.getJSONObject("info");
//					String city = cityObj.getString("value");
//					model.addAttribute("publicWordCity",city);
//				}
				model.addAttribute("publicWordType",publicWordType.toString());
			}
			
			JSONObject breakfastType = jsonObj.getJSONObject("breakfastType");
			if(breakfastType!=null){
//				model.addAttribute("breakfastType",breakfastType.get("type"));
//				model.addAttribute("breakfastTitle",breakfastType.get("title"));
				model.addAttribute("breakfastType",breakfastType.toString());
			}
			
			JSONObject dinnerType = jsonObj.getJSONObject("dinnerType");
			if(dinnerType!=null){
//				model.addAttribute("dinnerType",dinnerType.get("type"));
//				model.addAttribute("dinnerTitle",dinnerType.get("title"));
				model.addAttribute("dinnerType",dinnerType.toString());
			}
			
			JSONObject visaNeedType = jsonObj.getJSONObject("visaNeedType");
			if(visaNeedType!=null){
//				model.addAttribute("visaNeedType",dinnerType.get("type"));
//				model.addAttribute("visaNeedTitle",dinnerType.get("title"));
				model.addAttribute("visaNeedType",visaNeedType.toString());
			}
			
			JSONArray otherRequirements = jsonObj.getJSONArray("otherRequirements");
			if(!otherRequirements.isEmpty()){
				String back = new String();
				for(int n = 0;n<otherRequirements.size();n++){
					JSONObject obj = (JSONObject)otherRequirements.get(n);
					//back.add(obj.getString("value"));
					back+=obj.getString("value");
					if(n==otherRequirements.size()-1){
					}else{
						back+=",";
					}
				}
				model.addAttribute("otherRequirementsList",back);
			}
			
			JSONArray epriceRequirements = jsonObj.getJSONArray("epriceRequirements");
			if(!epriceRequirements.isEmpty()){
				String back = new String();
				for(int n = 0;n<epriceRequirements.size();n++){
					JSONObject obj = (JSONObject)epriceRequirements.get(n);
					//back.add(obj.getString("value"));
					back+=obj.getString("value");
					if(n==epriceRequirements.size()-1){
					}else{
						back+=",";
					}
				}
				model.addAttribute("epriceRequirementsList",back);
			}
		}
		
		// 再次询价 载入地接计调
		ArrayList<Map<String,String>> aopids = new ArrayList<Map<String,String>>();
		String aopStr = record.getAoperatorUserJson();
		aopids = backJsonMapList(aopids,aopStr);
		model.addAttribute("aopids", aopids);
		
		// 再次询价 载入机票计调
		ArrayList<Map<String,String>> topids = new ArrayList<Map<String,String>>();
		String topStr = record.getToperatorUserJson();
		topids = backJsonMapList(topids,topStr);
		model.addAttribute("topids", topids);
		
		model.addAttribute("fromareaslist", areaService.findFromCityList(null));// 出发城市
//		model.addAttribute("arrivedareas", areaService.findByCityList());// 到达城市
		
		if(!String.valueOf(EstimatePriceRecord.TYPE_FLIGHT).equals(type)){
			// 再次询价 载入线路国家id列表
			ArrayList<String> travelCountryIds = new ArrayList<String>();
			String couStr = record.getAdmitRequirements().getTravelCountryId();
			travelCountryIds = backJsonList(travelCountryIds,couStr);
			
			// 再次询价，载入线路国家名称列表
			ArrayList<String> travelCountryNames = new ArrayList<String>();
			String couNameStr = record.getAdmitRequirements().getTravelCountry();
			
			travelCountryNames = backJsonList(travelCountryNames,couNameStr);
			
			model.addAttribute("travelCountry",backGroupJsonMapList(travelCountryIds,travelCountryNames));
		}
		
		if(EstimatePriceRecord.IS_APP_FLIGHT_YES==record.getIsAppFlight()&&record.getTrafficRequirements()!=null){
			model.addAttribute("trafficLines",estimatePriceTrafficLineDao.findByPfid(record.getTrafficRequirements().getId()));//机票路线
		}
		
		if(record.getAdmitRequirements()!=null){
			EstimatePriceFile example = new EstimatePriceFile();
			example.setPid(record.getAdmitRequirements().getId());
			example.setType(EstimatePriceFile.TYPE_TRIP);
			example.setPtype(EstimatePriceFile.PTYPE_ADMIT);
			List<EstimatePriceFile> xdFileList = estimatePriceFileDao.findByExample(example);
			model.addAttribute("xdFileList" , xdFileList);
		}
		
		
		model.addAttribute("airportCityList",areaService.findAirportCityList(null)); //机场城市
		
		model.addAttribute("outAreaList",DictUtils.getDictsByType(null,"outarea"));// 离境口岸
		model.addAttribute("aoperators", aoperatorUsers);
		model.addAttribute("toperators", toperatorUsers);
		model.addAttribute("saler", UserUtils.getUser());
		model.addAttribute("customerAgents", agentInfoList);
		model.addAttribute("areamapList", areamapList);
		model.addAttribute("onceAgain", onceAgain);
		model.addAttribute("record", record);
		
		
		model.addAttribute("startTimeTypeList", EstimatePriceUtils.startTimeTypeList);
		model.addAttribute("aircraftSpaceLevelList", EstimatePriceUtils.aircraftSpaceLevelList);
		model.addAttribute("aircraftSpaceList", EstimatePriceUtils.aircraftSpaceList);
		model.addAttribute("startTimeList", EstimatePriceUtils.startTimeList);
		
		if(!String.valueOf(EstimatePriceRecord.TYPE_FLIGHT).equals(type)){
			view = "modules/eprice/onceAgainAloneEPrice";
		}else{
			view = "modules/eprice/onceAgainTrafficEPrice";
		}
		
		return view;
	}
	/**
	 * 再次询价
	 * 20150907 新增，询价主管分配计调
	 * @param type
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
    @RequestMapping(value="onceagainformanager/{type}/{rid}")
	public String onceAgainForManagerRecord(@PathVariable("type") String type,@PathVariable("rid") Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		// 获取询价客户列表（同行）,即渠道商列表
		List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();
		// 获取当前用户的线路国家
		List<Map<String,Object>> areamapList = getCountryList();
		
		// 再次询价标记
		Integer onceAgain = 1;
		// 跳转地址
		String view;
		// 询价项目
		EstimatePriceRecord record = new EstimatePriceRecord();
		EstimatePriceBaseInfo baseInfo = new EstimatePriceBaseInfo();
		EstimatePriceAdmitRequirements admitRequirements = new EstimatePriceAdmitRequirements();
		// 通过询价记录项目id，得到询价记录
		if(rid!=null){
			record = estimatePriceRecordService.findById(rid);
		}else{
			return null;
		}
		if(record!=null){
			baseInfo = record.getBaseInfo();
			admitRequirements = record.getAdmitRequirements();
			// 修正下日期格式
			if(admitRequirements!=null){
				Date date = admitRequirements.getDgroupOutDate();
				String dateStr = DateUtils.formatCustomDate(date, "yyyy-MM-dd");
				model.addAttribute("dgroupOutDate",dateStr);
			}
		}else{
			return null;
		}
		
		if(baseInfo.getAoperatorUserJson()!=null){
			JSONArray jsonArray = JSONArray.fromObject(baseInfo.getAoperatorUserJson());
			List<String> strAop = (List<String>)jsonArray.toCollection(jsonArray);
			model.addAttribute("aopIdList", strAop);
		}
		
		// 读取接待社计调要求的JSON串
		if(admitRequirements!=null&&admitRequirements.getTravelRequirements()!=null){
			JSONObject jsonObj = JSONObject.fromObject(admitRequirements.getTravelRequirements());
			
			JSONObject visaType = jsonObj.getJSONObject("visaType");
			if(visaType!=null){
				model.addAttribute("visaType",visaType.toString());
			}
			
			JSONObject hotelType = jsonObj.getJSONObject("hotelType");
			if(hotelType!=null){
				model.addAttribute("hotelType",hotelType.toString());
			}
			
			JSONObject hotelPosition = jsonObj.getJSONObject("hotelPosition");
			if(hotelPosition!=null){
				model.addAttribute("hotelPosition",hotelPosition.toString());
			}
			
			JSONObject roomType = jsonObj.getJSONObject("roomType");
			if(roomType!=null){
				model.addAttribute("roomType",roomType.toString());
			}
			
			JSONObject carType = jsonObj.getJSONObject("carType");
			if(carType!=null){
				model.addAttribute("carType",carType.toString());
			}
			
			
			JSONObject guideTyp = jsonObj.getJSONObject("guideTyp");
			if(guideTyp!=null){
				model.addAttribute("guideTyp",guideTyp.toString());
			}
			
			JSONObject leaderType = jsonObj.getJSONObject("leaderType");
			if(leaderType!=null){
				model.addAttribute("leaderType",leaderType.toString());
			}
			
			JSONObject attractionType = jsonObj.getJSONObject("attractionType");
			if(attractionType!=null){
				model.addAttribute("attractionType",attractionType.toString());
			}
			// 公务活动
			JSONObject publicWordType = jsonObj.getJSONObject("publicWordType");
			if(publicWordType!=null){
				model.addAttribute("publicWordType",publicWordType.toString());
			}
			
			JSONObject breakfastType = jsonObj.getJSONObject("breakfastType");
			if(breakfastType!=null){
				model.addAttribute("breakfastType",breakfastType.toString());
			}
			
			JSONObject dinnerType = jsonObj.getJSONObject("dinnerType");
			if(dinnerType!=null){
				model.addAttribute("dinnerType",dinnerType.toString());
			}
			
			JSONObject visaNeedType = jsonObj.getJSONObject("visaNeedType");
			if(visaNeedType!=null){
				model.addAttribute("visaNeedType",visaNeedType.toString());
			}
			
			JSONArray otherRequirements = jsonObj.getJSONArray("otherRequirements");
			if(!otherRequirements.isEmpty()){
				String back = new String();
				for(int n = 0;n<otherRequirements.size();n++){
					JSONObject obj = (JSONObject)otherRequirements.get(n);
					back+=obj.getString("value");
					if(n==otherRequirements.size()-1){
					}else{
						back+=",";
					}
				}
				model.addAttribute("otherRequirementsList",back);
			}
			
			JSONArray epriceRequirements = jsonObj.getJSONArray("epriceRequirements");
			if(!epriceRequirements.isEmpty()){
				String back = new String();
				for(int n = 0;n<epriceRequirements.size();n++){
					JSONObject obj = (JSONObject)epriceRequirements.get(n);
					back+=obj.getString("value");
					if(n==epriceRequirements.size()-1){
					}else{
						back+=",";
					}
				}
				model.addAttribute("epriceRequirementsList",back);
			}
		}
		
		// 20150907 获取计调主管列表(当前地接机票都是一套主管)
		Office office = UserUtils.getUser().getCompany();
		List<User> managerList = systemService.findRoleByOffice(Context.ROLE_TYPE_OP_EXECUTIVE, office.getId().toString());
		if(!managerList.isEmpty()){
			
			// 拉美途计调主管只保留陈卡卡
			String companyUuid = UserUtils.getCompanyUuid();
			if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				if (CollectionUtils.isNotEmpty(managerList)) {
					Iterator<User> it = managerList.iterator();
					while(it.hasNext()) {
						long userId = it.next().getId();
						if (userId != 816) {
							it.remove();
						}
					}
				}
			}
			
			model.addAttribute("managerList",managerList);
		}
		// 20150907 再次询价 载入地接计调主管
		EstimatePriceDistributionQuery queryAop = new EstimatePriceDistributionQuery();
		queryAop.setEstimateBaseId(Integer.valueOf(baseInfo.getId().toString()));
		queryAop.setType(Context.TYPE_OP_DAN_TUAN);
		queryAop.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<EstimatePriceDistribution> estAopList = estimatePriceDistributionService.find(queryAop);
		if(estAopList!=null && !estAopList.isEmpty()){
			model.addAttribute("aopManager",estAopList.get(0));
		}
		
		// 20150907 再次询价 载入机票计调主管
		EstimatePriceDistributionQuery queryTop = new EstimatePriceDistributionQuery();
		queryTop.setEstimateBaseId(Integer.valueOf(baseInfo.getId().toString()));
		queryTop.setType(Context.TYPE_OP_AIR);
		queryTop.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<EstimatePriceDistribution> estTopList = estimatePriceDistributionService.find(queryTop);
		if(estTopList!=null && !estTopList.isEmpty()){
			model.addAttribute("topManager",estTopList.get(0));
		}
		
		model.addAttribute("fromareaslist", areaService.findFromCityList(null));// 出发城市
		
		if(!String.valueOf(EstimatePriceRecord.TYPE_FLIGHT).equals(type)){
			// 再次询价 载入线路国家id列表
			ArrayList<String> travelCountryIds = new ArrayList<String>();
			String couStr = record.getAdmitRequirements().getTravelCountryId();
			travelCountryIds = backJsonList(travelCountryIds,couStr);
			
			// 再次询价，载入线路国家名称列表
			ArrayList<String> travelCountryNames = new ArrayList<String>();
			String couNameStr = record.getAdmitRequirements().getTravelCountry();
			
			travelCountryNames = backJsonList(travelCountryNames,couNameStr);
			
			model.addAttribute("travelCountry",backGroupJsonMapList(travelCountryIds,travelCountryNames));
		}
		
		if(EstimatePriceRecord.IS_APP_FLIGHT_YES==record.getIsAppFlight()&&record.getTrafficRequirements()!=null){
			model.addAttribute("trafficLines",estimatePriceTrafficLineDao.findByPfid(record.getTrafficRequirements().getId()));//机票路线
		}
		
		if(record.getAdmitRequirements()!=null){
			EstimatePriceFile example = new EstimatePriceFile();
			example.setPid(record.getAdmitRequirements().getId());
			example.setType(EstimatePriceFile.TYPE_TRIP);
			example.setPtype(EstimatePriceFile.PTYPE_ADMIT);
			List<EstimatePriceFile> xdFileList = estimatePriceFileDao.findByExample(example);
			model.addAttribute("xdFileList" , xdFileList);
		}
		
		
		model.addAttribute("airportCityList",areaService.findAirportCityList(null)); //机场城市
		
		model.addAttribute("outAreaList",DictUtils.getDictsByType(null,"outarea"));// 离境口岸
		model.addAttribute("saler", UserUtils.getUser());
		model.addAttribute("customerAgents", agentInfoList);
		model.addAttribute("areamapList", areamapList);
		model.addAttribute("onceAgain", onceAgain);
		model.addAttribute("record", record);
		
		
		model.addAttribute("startTimeTypeList", EstimatePriceUtils.startTimeTypeList);
		model.addAttribute("aircraftSpaceLevelList", EstimatePriceUtils.aircraftSpaceLevelList);
		model.addAttribute("aircraftSpaceList", EstimatePriceUtils.aircraftSpaceList);
		model.addAttribute("startTimeList", EstimatePriceUtils.startTimeList);
		
		if(!String.valueOf(EstimatePriceRecord.TYPE_FLIGHT).equals(type)){
			view = "modules/eprice/onceAgainAloneForManagerEPrice";
		}else{
			view = "modules/eprice/onceAgainTrafficForManagerEPrice";
		}
		
		return view;
	}
	/**
	 * 遍历string， 生成ArrayList<String>
	 * @param list
	 * @param str
	 * @return
	 */
	private ArrayList<String> backJsonList(ArrayList<String> list,String str){
		if(StringUtils.isNotBlank(str)){
			JSONArray json  = null;
			try{
				 json = JSONArray.fromObject(str);
			}catch(Exception e){
				list.add(str);
				return list;
			}
			
			for(int n=0;n<json.size();n++){
				list.add(json.getString(n));
			}
		}
		return list;
	}
	
	/**
	 * 将两个 ArrayList<String> 数组组合成 ArrayList<Map<String,String>>
	 * @param list1
	 * @param list2
	 * @return
	 */
	private ArrayList<Map<String,String>> backGroupJsonMapList(ArrayList<String> list1,ArrayList<String> list2){
		ArrayList<Map<String,String>> back = new ArrayList<Map<String,String>>();
		if(list1.size()>0 && list2.size()>0 && list1.size() == list2.size()){
			for(int n=0;n<list1.size();n++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("key", list1.get(n));
				map.put("value", list2.get(n));
				back.add(map);
			}
		}
		return back;
	}
	
	/**
	 * 遍历string ，生成 ArrayList<Map<String,String>>
	 * @param list
	 * @param str
	 * @return
	 */
	private ArrayList<Map<String,String>> backJsonMapList(ArrayList<Map<String,String>> list,String str){
		if(StringUtils.isNotBlank(str)){
			JSONArray json = JSONArray.fromObject(str);
			for(int n=0;n<json.size();n++){
				Map<String,String> map = new HashMap<String,String>();
				JSONObject objs =  json.getJSONObject(n);
				map.put("key", objs.getString("userId"));
				map.put("value", objs.getString("userName"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 获取计调角色用户列表
	 * @param type
	 * @return
	 */
	private List<User> getAoperatorUsers(Integer type){
		List<User> userList = new ArrayList<User>();
//		List<Role> roleList;
		//暂时不能区分地接和机票
		// 判断，type = 1 ，地接计调主管列表
//		if(type==1){
//			roleList = systemService.findByRoleTypeAndCompanyId(Context.ROLE_TYPE_OP, Context.ROLE_TYPE_OP_EXECUTIVE, UserUtils.getUser().getCompany().getId());
//		}else{ // type==2,机票计调主管列表
//			roleList = systemService.findByRoleTypeAndCompanyId(Context.ROLE_TYPE_OP, Context.ROLE_TYPE_OP_EXECUTIVE, UserUtils.getUser().getCompany().getId());
//		}
		
		
		String hql = " select u.id , u.name  from sys_user u ,sys_user_role ur ,  sys_role r " +
					" where r.id = ur.roleId and ur.userId = u.id and " +
					" (r.roleType = "+Context.ROLE_TYPE_OP+" or r.roleType = "+Context.ROLE_TYPE_OP_EXECUTIVE+")" +
					" and r.companyId = "+UserUtils.getUser().getCompany().getId()+
					" and r.delFlag = '" + Role.DEL_FLAG_NORMAL + "' and u.delFlag = " +Role.DEL_FLAG_NORMAL+	
					" group by u.id,u.name   order by u.name" ;
		@SuppressWarnings("unchecked")
        List<Object[]> list = userDao.getSession().createSQLQuery(hql).list();
		for(Object[] obj:list){
			User user = new User();
			user.setId(Long.parseLong(obj[0].toString()));
			user.setName((String)obj[1]);
			userList.add(user);
		}
		
		String companyUuid = UserUtils.getCompanyUuid();
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
			if (CollectionUtils.isNotEmpty(userList)) {
				Iterator<User> it = userList.iterator();
				while(it.hasNext()) {
					long userId = it.next().getId();
					if (userId != 889 && userId != 7732 && userId != 7691 && userId != 812 && userId != 820 && userId != 2653 && userId != 1925 && userId != 816) {
						it.remove();
					}
				}
			}
		}
		
//		if(roleList!=null && roleList.size()>0){
//			userList = new ArrayList<User>();
//			Set<User> userSet = new HashSet<User>();
//			for(Role role : roleList) {
//				for(User user : role.getUserList())
//					userSet.add(user);
//			}
//			userList.addAll(userSet);
//			Collections.sort(userList, new Comparator<User>() {
//                @Override
//                public int compare(User o1, User o2) {
//                    return (o1.getName() == null ? "" : o1.getName()).compareTo(o2.getName());
//                }
//            });
//			return userList;
//		}
		return userList;
	}
	/**
	 * 获取销售角色用户列表
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
    private List<User> getAoperatorUsers(){
//		List<Role> roleList = systemService.findByRoleTypeAndCompanyId(Context.ROLE_TYPE_SALES, Context.ROLE_TYPE_SALES_EXECUTIVE, UserUtils.getUser().getCompany().getId());
//		
//		if(roleList!=null && roleList.size()>0){
//			List<User> userList = new ArrayList<User>();
//			Set<User> userSet = new HashSet<User>();
//			for(Role role : roleList){
//				if(Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())){
//					for(User user : role.getUserList()) 
//						userSet.add(user);
//				}
//			}
//			userList.addAll(userSet);
//			return userList;
//		}
		List<User> userList = new ArrayList<User>();
		String hql = " select u.id , u.name  from sys_user u ,sys_user_role ur ,  sys_role r " +
				" where r.id = ur.roleId and ur.userId = u.id and " +
				" (r.roleType = "+Context.ROLE_TYPE_SALES_EXECUTIVE+" or r.roleType = "+Context.ROLE_TYPE_SALES+")" +
				" and r.companyId = "+UserUtils.getUser().getCompany().getId()+
				" and u.delFlag = '" + User.DEL_FLAG_NORMAL + "' " +
				" and r.delFlag = '" + Role.DEL_FLAG_NORMAL + "' " +
				 " group by u.id,u.name   order by u.name" ;
		List<Object[]> list = userDao.getSession().createSQLQuery(hql).list();
		for(Object[] obj:list){
			User user = new User();
			user.setId(Long.parseLong(obj[0].toString()));
			user.setName((String)obj[1]);
			userList.add(user);
		}
		return userList;
	}
	
	/**
	 * 获取询价项目页面
	 * @author lihua.xu
	 * @时间 2014年9月25日
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	//@RequiresPermissions("eprice:plist:saler")
	@RequestMapping(value="list4saler")
	public String list4saler(Model model,HttpServletRequest request, HttpServletResponse response){
		//接待计调用户列表,为搜索表单的计调下拉菜单服务，最终改成正式数据接口
		List<User> operatorUsers = Lists.newArrayList();
		operatorUsers = getAoperatorUsers(1);
		model.addAttribute("operators", operatorUsers);
		List<Map<String,Object>> areamapList = getCountryList();
		
		// 获取销售列表
		List<User> sellUser = getAoperatorUsers();
		//按部门展示
		departmentService.setDepartmentPara("inquiry", model);
		// 获取询价客户列表（同行）,即渠道商列表
		List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();
		model.addAttribute("sellUser", sellUser);
		model.addAttribute("agentInfoList", agentInfoList);
		model.addAttribute("areamapList", areamapList);		
		model.addAttribute("estimateModel", UserUtils.getUser().getCompany().getEstimateModel());
		model.addAttribute("showType","216");
		return "modules/eprice/ePriceList4saler";
	}
	
	/**
	 * 计调主管分配计调询价列表
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="list4manager")
	public String list4manager(Model model, HttpServletRequest request, HttpServletResponse response) {
		//接待计调用户列表,为搜索表单的计调下拉菜单服务，最终改成正式数据接口
		List<User> operatorUsers = Lists.newArrayList();
		operatorUsers = getAoperatorUsers(1);
		model.addAttribute("operators", operatorUsers);
		List<Map<String,Object>> areamapList = getCountryList();
		
		// 获取销售列表
		List<User> sellUser = getAoperatorUsers();
		//按部门展示
		departmentService.setDepartmentPara("inquiry", model);
		// 获取询价客户列表（同行）,即渠道商列表
		List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();
		model.addAttribute("sellUser", sellUser);
		model.addAttribute("agentInfoList", agentInfoList);
		model.addAttribute("areamapList", areamapList);
		model.addAttribute("showType","216");
		return "modules/eprice/ePriceList4manager";
	}
	
	/**
	 * 获取分配计调用到的基本信息
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="distributeOp/{rid}")
	public String distributeOp(@PathVariable("rid")Long rid, Model model) {
		EstimatePriceRecord priceRecord = estimatePriceRecordService.findById(rid);
		EstimatePriceBaseInfo baseInfo = priceRecord.getBaseInfo();
		
		List<User> userList = getAoperatorUsers(1);
		
		//获取分配表中对应计调主管数据
		String sql = "SELECT type FROM estimate_price_distribution WHERE op_manager_id = " 
					+ UserUtils.getUser().getId() + " AND estimate_base_id = " + baseInfo.getId();
		List<Map<String, Integer>> typeList = estimatePricerReplyDao.findBySql(sql, Map.class);
		if (CollectionUtils.isNotEmpty(typeList)) {
			String types = "";
			for (Map<String, Integer> map : typeList) {
				types += map.get("type") + ",";
			}
			model.addAttribute("types", types);
		} else {
			model.addAttribute("types", Lists.newArrayList());
		}
		model.addAttribute("baseInfoId", baseInfo.getId());
		model.addAttribute("rid", rid);
		model.addAttribute("opIds", baseInfo.getAoperatorUserJson());
		model.addAttribute("airticketOpIds", baseInfo.getToperatorUserJson());
		model.addAttribute("userList", userList);
		return "modules/eprice/distributeOp";
	}
	
	/**
	 * 计调主管分配计调
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="distribute")
	public String distribute(Model model, HttpServletRequest request) {
		
		//询价记录
		String rid = request.getParameter("rid");
		//询价基本信息
		String baseInfoId = request.getParameter("baseInfoId");
		
		//询价记录
		EstimatePriceRecord record = estimatePriceRecordService.findById(Long.parseLong(rid));
		//询价基本信息
		EstimatePriceBaseInfo baseInfo = estimatePriceBaseInfoService.findById(Long.parseLong(baseInfoId));
		
		//地接计调id数组
		String opIds [] = request.getParameterValues("opId");
		//机票计调id数组
		String airticketOpIds [] = request.getParameterValues("airticketOpId");
		
		JSONArray jat = new JSONArray();
		JSONArray jatRecord = new JSONArray();
		JSONArray jatRecordTemp = new JSONArray();
		if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getAoperatorUserJson())){
			String opUserJson = baseInfo.getAoperatorUserJson();
			if (StringUtils.isNotBlank(opUserJson)) {
				jat = JSONArray.fromObject(opUserJson);
			}
		}
		String opUserJson = record.getAoperatorUserJson();
		if (StringUtils.isNotBlank(opUserJson)) {
			jatRecordTemp = JSONArray.fromObject(opUserJson);
		}
		if (opIds != null && opIds.length > 0) {
			for (String opId : opIds) {
				boolean flag = false;
				for (int i = 0, len = jat.size(); i < len; i++) {
					String userId = jat.getString(i);
					if (opId.equals(userId)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					jat.add(Long.parseLong(opId));
					User u = UserUtils.getUser(Long.parseLong(opId));
					JSONObject jt = new JSONObject();
					jt.put("userId", u.getId());
					jt.put("userName", u.getName());
					jatRecord.add(jt);
					jatRecordTemp.add(jt);
				}
			}
			baseInfo.setAoperatorUserJson(jat.toString());
			if (jatRecord.size() > 0) {
				record.setAoperatorUserJson(jatRecord.toString());
			}
			estimatePricerReplyDao.updateBySql(
					"update estimate_price_distribution set op_id = ? where estimate_base_id = ? and type = 0",
					record.getAoperatorUserJson(), baseInfo.getId());
		} else {
			if (baseInfo.getAoperatorUserJson() == null) {
				baseInfo.setAoperatorUserJson(jat.toString());
			}
			if (record.getAoperatorUserJson() == null) {
				record.setAoperatorUserJson(jatRecord.toString());
			}
		}
		
		JSONArray airticketJat = new JSONArray();
		JSONArray airticketJatRecord = new JSONArray();
		JSONArray airticketJatRecordTemp = new JSONArray();
		String airticketOpUserJson = baseInfo.getToperatorUserJson();
		if (StringUtils.isNotBlank(airticketOpUserJson)) {
			airticketJat = JSONArray.fromObject(airticketOpUserJson);
		}
		String airticketOpUserJsonTemp = record.getToperatorUserJson();
		if (StringUtils.isNotBlank(airticketOpUserJsonTemp)) {
			airticketJatRecordTemp = JSONArray.fromObject(airticketOpUserJsonTemp);
		}
		if (airticketOpIds != null && airticketOpIds.length > 0) {
			for (String opId : airticketOpIds) {
				boolean flag = false;
				for (int i = 0, len = airticketJat.size(); i < len; i++) {
					String userId = airticketJat.getString(i);
					if (opId.equals(userId)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					airticketJat.add(Long.parseLong(opId));
					User u = UserUtils.getUser(Long.parseLong(opId));
					JSONObject jt = new JSONObject();
					jt.put("userId", u.getId());
					jt.put("userName", u.getName());
					airticketJatRecord.add(jt);
					airticketJatRecordTemp.add(jt);
				}
			}
			baseInfo.setToperatorUserJson(airticketJat.toString());
			if (airticketJatRecord.size() > 0) {
				record.setToperatorUserJson(airticketJatRecord.toString());
			}
			
			estimatePricerReplyDao.updateBySql(
					"update estimate_price_distribution set op_id = ? where estimate_base_id = ? and type = 1",
					record.getToperatorUserJson(), baseInfo.getId());
			
		} else {
			if (baseInfo.getToperatorUserJson() == null) {
				baseInfo.setToperatorUserJson(airticketJat.toString());
			}
			if (record.getToperatorUserJson() == null) {
				record.setToperatorUserJson(airticketJatRecord.toString());
			}
		}
		// 分配计调成功，需要将询价记录的状态从“待分配”改变为“待报价”
		record.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_WAITING);
		// 分配计调成功，需要将询价项目的状态从“待分配”改变为“待报价”
		if(baseInfo!=null && baseInfo.getPid()!=null){
			EstimatePriceProject pro = estimatePriceProjectService.findById(baseInfo.getPid());
			if(pro!=null){
				pro.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_WAITING);
				estimatePriceProjectService.save(pro);
			}
		}
		//保存询价记录
		estimatePriceRecordService.save(record);
		//保存基本信息
		estimatePriceBaseInfoService.save(baseInfo);
		//生成回复给计调
		estimatePricerReplyService.send(estimatePriceProjectService.findById(baseInfo.getPid()), record, jatRecord, airticketJatRecord, false);
		if (jatRecordTemp.size() > 0) {
			record.setAoperatorUserJson(jatRecordTemp.toString());
		}
		if (airticketJatRecordTemp.size() > 0) {
			record.setToperatorUserJson(airticketJatRecordTemp.toString());
		}
		//保存询价记录
		estimatePriceRecordService.save(record);
		return "modules/eprice/ePriceList4manager";
	}
	
	
	/**
	 * 回复询价记录的接待询价页面
	 * @author lihua.xu
	 * @时间 2014年9月25日
	 * @param rid 询价记录id
	 * @param model
	 * @param request
	 * @param response
	 * @return  {"record":EstimatePriceRecord 询价记录，"project":EstimatePriceProject 询价项目,
	 * 				"aoperators":接待计调JSONArray,"toperators":机票计调JSONArray}
	 */
	//@RequiresPermissions("eprice:reply:admit")
	@RequestMapping(value="replayaop/{rid}")
	public String replayaop(@PathVariable("rid")Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		
		String types = EstimatePricerReply.TYPE_ADMIT_DT+","+EstimatePricerReply.TYPE_ADMIT_DKH+","+EstimatePricerReply.TYPE_ADMIT_YX+","+EstimatePricerReply.TYPE_ADMIT_ZYX;
		Map<String, Object> map = estimatePriceProjectService.replayopView(rid, UserUtils.getUser(),types);
		
		//判断非正常情况
		/*if(){
			
			
		}*/
		
		EstimatePriceRecord record = (EstimatePriceRecord)map.get("record");
		EstimatePricerReply epr = (EstimatePricerReply)map.get("reply");
		if(epr!=null && epr.getStatus()>=EstimatePricerReply.STATUS_REPLYED){
			JSONObject priceDetailJson = JSONObject.fromObject(epr.getPriceDetail());
			model.addAttribute("priceDetailJson", priceDetailJson);
		}
		
		if(record!=null&&record.getAdmitRequirements()!=null){
			record.getAdmitRequirements().setTravelCountry(estimatePriceAdmitLinesAreaDao.getLinesNames(record.getAdmitRequirements().getId().toString()));
		}
		
		
		JSONArray ajarray = null;
		if(record!=null && StringUtils.isNotBlank(record.getAoperatorUserJson())){
			ajarray = JSONArray.fromObject(record.getAoperatorUserJson());
		}	
		// 机票计调
		JSONArray tjarray = null;
		if(record!=null && StringUtils.isNotBlank(record.getToperatorUserJson())){
			tjarray = JSONArray.fromObject(record.getToperatorUserJson());
		}	
		
		model.addAttribute("aoperators", ajarray);
		model.addAttribute("toperators", tjarray);
		model.addAllAttributes(map);
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		
		// 获取多币种 20150512
		Long companyId = UserUtils.getCompanyIdForData();
		if(companyId!=null){
			List<Currency> currencyList = currencyService.findCurrencyList(companyId);
			if(currencyList!=null && !currencyList.isEmpty()){
				model.addAttribute("currencyList", currencyList);
			}
		}
		return "modules/eprice/replyEPriceInfo4Admit";
	}
	
	/**
	 * 回复询价记录的机票询价页面
	 * @author lihua.xu
	 * @时间 2014年10月8日
	 * @param rid 询价记录id
	 * @param model
	 * @param request
	 * @param response
	 * @return  {"record":EstimatePriceRecord 询价记录，"project":EstimatePriceProject 询价项目,"lineList":交通路线列表}
	 */
	//@RequiresPermissions("eprice:reply:traffic")
	@RequestMapping(value="replaytop/{rid}")
	public String replaytop(@PathVariable("rid")Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> map = estimatePriceProjectService.replayopView(rid, UserUtils.getUser(),String.valueOf(EstimatePricerReply.TYPE_FLIGHT));
		
		
		EstimatePriceRecord record = (EstimatePriceRecord)map.get("record");
		//判断rid对应的询价记录是否有机票询价
		//金额转换
		
		if(record.getIsAppFlight().equals(EstimatePriceRecord.IS_APP_FLIGHT_YES) 
				|| record.getType().equals(EstimatePriceRecord.TYPE_FLIGHT)){
			
		}
		
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		model.addAllAttributes(map);
		// 获取多币种 20150512
		Long companyId = UserUtils.getCompanyIdForData();
		if(companyId!=null){
			List<Currency> currencyList = currencyService.findCurrencyList(companyId);
			if(currencyList!=null && !currencyList.isEmpty()){
				model.addAttribute("currencyList", currencyList);
			}
		}
		return "modules/eprice/replyEPriceInfo4Traffic";
	}
	
	/**
	 * 询价记录详情页面
	 * @author lihua.xu
	 * @时间 2014年9月25日
	 * @param rid 询价记录id
	 * @param model
	 * @param request
	 * @param response
	 * @return  {"record":EstimatePriceRecord 询价记录，"project":EstimatePriceProject 询价项目}
	 */
	//@RequiresPermissions("eprice:record:info")
	@RequestMapping(value="recordinfo/{rid}")
	public String recordInfo(@PathVariable("rid")Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = estimatePriceRecordService.recordInfoShow(rid, UserUtils.getUser());
		
		
		//判断非正常情况
		EstimatePriceRecord record = (EstimatePriceRecord)map.get("record");
		
		JSONArray ajarray = JSONArray.fromObject(record.getAoperatorUserJson());
		// 计算总价
		String operatorPrice = record.getOperatorPrice()!=null?record.getOperatorPrice().toString():"0";
		String outPrice = record.getOutPrice()!=null?record.getOutPrice().toString():"0";
		BigDecimal dec = new BigDecimal(0);
		if((record.getOutPrice()!=null) && (record.getOperatorPrice() !=null)){
			dec = record.getOutPrice().subtract(record.getOperatorPrice());
		}
		String setter = dec.toString();
		
		// 机票相关
		JSONArray tjarray = JSONArray.fromObject(record.getToperatorUserJson());
		model.addAttribute("aoperators", ajarray);
		model.addAttribute("record", record);
		if(setter!=null){
			model.addAttribute("setter",setter);
		}else{
			model.addAttribute("setter","待报价");
		}
		if(outPrice!=null&&new BigDecimal(outPrice).compareTo(BigDecimal.ZERO)>=0){
			model.addAttribute("outPrice",outPrice);
		}else{
			model.addAttribute("outPrice","待报价");
		}
		if(operatorPrice!=null&&new BigDecimal(operatorPrice).compareTo(BigDecimal.ZERO)>=0){
			model.addAttribute("operatorPrice",operatorPrice);
		}else{
			model.addAttribute("operatorPrice","待报价");
		}
		
		// 机票相关
		model.addAttribute("toperators", tjarray);
		model.addAllAttributes(map);
		// 20150907 新增，判断计调主管未指定计调时的详情
		EstimatePriceBaseInfo info = record.getBaseInfo();
		if(info!=null && info.getFormanager()==Context.FOR_MANAGER_YES){
			model.addAttribute("baseInfo", info);
			// 查询接待社计调主管
			EstimatePriceDistributionQuery query = new EstimatePriceDistributionQuery();
			query.setEstimateBaseId(Integer.valueOf(info.getId().toString()));
			query.setType(Context.TYPE_OP_DAN_TUAN);// 分类为地接计调
			query.setDelFlag(Context.DEL_FLAG_NORMAL);
			List<EstimatePriceDistribution> butList = estimatePriceDistributionService.find(query);
			if(butList!=null && !butList.isEmpty()){
				model.addAttribute("AopManager", getUserList(butList));
			}
			
			EstimatePriceDistributionQuery query2 = new EstimatePriceDistributionQuery();
			query2.setEstimateBaseId(Integer.valueOf(info.getId().toString()));
			query2.setType(Context.TYPE_OP_AIR);// 分类为机票计调
			query2.setDelFlag(Context.DEL_FLAG_NORMAL);
			List<EstimatePriceDistribution> butList2 = estimatePriceDistributionService.find(query2);
			if(butList2!=null && !butList2.isEmpty()){
				model.addAttribute("TopManager", getUserList(butList2));
			}
			return "modules/eprice/ePriceInfoForManager";
		}
		
		// 机票相关
		model.addAttribute("isOpManager", request.getParameter("isOpManager"));
		return "modules/eprice/ePriceInfo";
	}
	/**
	 * 获取List<User> 计调主管列表
	 * @param butList
	 * @return
	 */
	private List<User> getUserList(List<EstimatePriceDistribution> butList){
		List<User> userList = new ArrayList<User>();
		if(butList!=null && !butList.isEmpty()){
			for(EstimatePriceDistribution est : butList){
				Integer userId = est.getOpManagerId();
				if(userId!=null){
					User user = UserUtils.getUser(Long.valueOf(userId));
					userList.add(user);
				}
			}
		}
		return userList;
	}
	
	/**
	 * 询价记录修改页面
	 * @author jianning.gao
	 * @时间 2014年10月19日
	 * @param rid 询价记录id
	 * @param model
	 * @param request
	 * @param response
	 * @return  {"record":EstimatePriceRecord 询价记录，"project":EstimatePriceProject 询价项目}
	 */
	@RequestMapping(value="recordinfoChange/{rid}")
	public String recordInfoChange(@PathVariable("rid")Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = estimatePriceRecordService.recordInfoShow(rid, UserUtils.getUser());
		
		
		EstimatePriceRecord record = (EstimatePriceRecord)map.get("record");
		
		JSONArray ajarray = JSONArray.fromObject(record.getAoperatorUserJson());
		// 机票相关
		JSONArray tjarray = JSONArray.fromObject(record.getToperatorUserJson());
		model.addAttribute("aoperators", ajarray);
		model.addAttribute("record", record);
		// 机票相关
		model.addAttribute("toperators", tjarray);
		model.addAllAttributes(map);
		return "modules/eprice/ePriceInfoChange";
	}
	
	
	/**
	 * 询价记录更多列表页面
	 * @author lihua.xu
	 * @时间 2014年10月8日
	 * @param model
	 * @param request
	 * @param response
	 * @return  {"project":EstimatePriceProject 询价项目}
	 */
	//@RequiresPermissions("eprice:record:more")
	@RequestMapping(value="erlist/{pid}")
	public String eRecordListMore(@PathVariable("pid")Long pid,Model model,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = estimatePriceProjectService.erlistMore(pid);
		
		model.addAllAttributes(map);
		
		EstimatePriceProject p = (EstimatePriceProject)map.get("project");
		Integer type = p.getType();
		
		if(type.equals(EstimatePriceProject.TYPE_FLIGHT)){	// 如果为机票类询价
			return "modules/eprice/eTopRecordListMore";
		}else{		// 如果为单团类询价
			return "modules/eprice/eRecordListMore";
		}
		
	}
	/**
	 * 指定计调获取全部询价记录页面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="erecordlist")
	public String eRecordList(Model model,HttpServletRequest request, HttpServletResponse response){
		User user = UserUtils.getUser();
		// 获取销售列表
		List<User> sellUser = getAoperatorUsers();
		model.addAttribute("sellUser",sellUser);
		//线路国家
		
		List<Map<String,Object>> areamapList = getCountryList();
		model.addAttribute("areamapList", areamapList);
	
		model.addAttribute("operatorUid",user.getId());
		model.addAttribute("menuId", 213);
		//按部门展示
//		new DepartmentCommon().getDepartment(request, model, user, departmentService, "inquiry", null, "eprice");
		return "modules/eprice/eRecordList";
	}
	/**
	 * 指定机票计调获取全部机票询价记录页面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="erecordtrafficlist")
	public String eRecordTrafficList(Model model,HttpServletRequest request, HttpServletResponse response){
		User user = UserUtils.getUser();
		// 获取销售列表
		List<User> sellUser = getAoperatorUsers();
		model.addAttribute("sellUser",sellUser);
		model.addAttribute("operatorUid",user.getId());
		model.addAttribute("menuId", 213);
		//按部门展示
//		new DepartmentCommon().getDepartment(request, model, user, departmentService, "inquiry", null, "eprice");
		return "modules/eprice/eRecordTrafficList";
	}
	/**
	 * 跳转到计调报价详情页
	 * @param operMenu 操作菜单：1:地接计调看详情,2:机票计调看详情
	 * @return
	 */
	@RequestMapping(value="ePriceInfoReadOnly/{rid}/{operMenu}")
	public String ePriceInfoReadOnly(@PathVariable("rid")Long rid,@PathVariable("operMenu")String operMenu,Model model,HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("operMenu",operMenu);
		
		Map<String, Object> map = estimatePriceRecordService.recordInfoShow(rid, UserUtils.getUser());
		User user = UserUtils.getUser();
		
		EstimatePriceRecord record = (EstimatePriceRecord)map.get("record");
		
		JSONArray ajarray = JSONArray.fromObject(record.getAoperatorUserJson());
		// 机票相关
		JSONArray tjarray = JSONArray.fromObject(record.getToperatorUserJson());
		model.addAttribute("aoperators", ajarray);
		model.addAttribute("record", record);
		model.addAttribute("theoperator",user.getId());
		// 结算价
		BigDecimal countprice= null;
		if(record.getOutPrice()!=null && record.getOperatorPrice()!=null){
			countprice = (record.getOutPrice()).subtract(record.getOperatorPrice()) ;
		}
		// 机票相关
		model.addAttribute("toperators", tjarray);
		model.addAllAttributes(map);
		
		if(countprice!=null){
			model.addAttribute("countprice",countprice);
		}else{
			model.addAttribute("countprice","待报价");
		}
		if(record.getOutPrice()!=null){
			model.addAttribute("outPrice",record.getOutPrice());
		}else{
			model.addAttribute("outPrice","待报价");
		}
		if(record.getOperatorPrice()!=null){
			model.addAttribute("operatorPrice",record.getOperatorPrice().toString());
		}else{
			model.addAttribute("operatorPrice","待报价");
		}
		
		// 报价记录
		@SuppressWarnings("unchecked")
        List<EstimatePricerReply> alist =	(List<EstimatePricerReply>)map.get("alist");			// 地接计调
		@SuppressWarnings("unchecked")
        List<EstimatePricerReply> flist = 	(List<EstimatePricerReply>)map.get("flist");			// 机票计调
		List<EstimatePricerReply> alistReturn = new ArrayList<EstimatePricerReply>();
		List<EstimatePricerReply> flistReturn = new ArrayList<EstimatePricerReply>();
		// 过滤地接报价记录，只取当前登录计调的报价
		if(alist!=null && alist.size()>0){
			Iterator<EstimatePricerReply> aiter = alist.iterator();
			while(aiter.hasNext()){
				EstimatePricerReply rep = aiter.next();
				if(rep.getOperatorUserId().equals(user.getId())){
					alistReturn.add(rep);
					//接待社回复上传文件
					List<EstimatePriceFile> upRelayFileList = null;
					String upRelayFileDocIds ="";
					if(EstimatePriceRecord.TYPE_ALONE==record.getType()){
						EstimatePriceFile example = new EstimatePriceFile();
						example.setPid(rep.getId());
						example.setType(EstimatePriceFile.TYPE_REPLY);
						example.setPtype(EstimatePriceFile.PTYPE_REPLY);
						upRelayFileList = estimatePriceFileDao.findByExample(example);
						for(EstimatePriceFile file:upRelayFileList){
							upRelayFileDocIds+=","+file.getDocInfoId();
						}
						if(upRelayFileDocIds.length()>0){
							upRelayFileDocIds = upRelayFileDocIds.replaceFirst(",", "");
						}
					}
					model.addAttribute("upRelayFileList",upRelayFileList);
					model.addAttribute("upRelayFileDocIds",upRelayFileDocIds);
					break;
				}
			}
		}
		// 过滤机票报价记录，只取当前登录计调的报价
		if(flist!=null && flist.size()>0){
			Iterator<EstimatePricerReply> fiter = flist.iterator();
			while(fiter.hasNext()){
				EstimatePricerReply rep = fiter.next();
				if(rep.getOperatorUserId().equals(user.getId())){
					flistReturn.add(rep);
					break;
				}
			}
		}
		model.addAttribute("alist",alistReturn);
		model.addAttribute("flist",flistReturn);
		return "modules/eprice/ePriceInfoReadOnly";
	}
	/**
	 * 计调根据已确认报价,跳转到发布产品页面
	 * @param rid
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="sendProject/{rid}")
	public String sendProject(@PathVariable("rid")Long rid,Model model,HttpServletRequest request, HttpServletResponse response){
		if(rid!=null && rid>0){
			return "redirect:"+Global.getAdminPath()+"/activity/manager/form?recordId="+rid;
		}
		return null;
	}
	
	
	/**
	 * 销售删除产品
	 * @param pid
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="backProject/{pid}")
	public String backProject(@PathVariable("pid")Long pid,Model model,HttpServletRequest request, HttpServletResponse response){
		if(pid!=null && pid>0){
			// 获取EstimatePriceProject 产品项目,并将该产品项目设为"已删除产品"
			EstimatePriceProject pro = estimatePriceProjectService.findById(pid);
			if(pro!=null){
				pro.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_WAITING);
				pro.setLastCancelTime(new Date());
				//pro.setStatus(EstimatePriceProject.STATUS_DEL);
				estimatePriceProjectService.save(pro);
				// 恢复询价日志
				logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name , 
						"销售员:(ID "+pro.getUserId()+")"+pro.getUserName()+
						" 恢复编号为：（estimate_price_project.id）"+pro.getId()+
						" 询价客户为："+pro.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(pro.getType())+"询价。"+
						" 操作人："+UserUtils.getUser().getName()+", 操作时间："+new Date()+"。"
						, Context.log_state_up, null, null); 
			}
		}
		return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/list4saler";
	}
	
	
	/**
	 * 销售恢复产品
	 * @param pid
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="delProject/{pid}")
	public String delProject(@PathVariable("pid")Long pid,Model model,HttpServletRequest request, HttpServletResponse response){
		if(pid!=null && pid>0){
			// 获取EstimatePriceProject 产品项目,并将该产品项目设为"已删除产品"
			EstimatePriceProject pro = estimatePriceProjectService.findById(pid);
			
			if(pro!=null && pro.getEstimateStatus()!=EstimatePriceProject.ESTIMATE_STATUS_CANCEL){
				pro.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_CANCEL);
				pro.setLastCancelTime(new Date());
				//pro.setStatus(EstimatePriceProject.STATUS_DEL);
				estimatePriceProjectService.save(pro);
				// 取消询价日志
				logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name , 
						"销售员:(ID "+pro.getUserId()+")"+pro.getUserName()+
						" 取消编号为：（estimate_price_project.id）"+pro.getId()+
						" 询价客户为："+pro.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(pro.getType())+"询价"+
						" 操作人："+UserUtils.getUser().getName()+", 操作时间："+new Date()+"。"
						, Context.log_state_del, null, null);
			}
		}
		return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/list4saler";
	}
	
	/**
	 * 查询当前用户的线路国家
	 * @return
	 */
	public List<Map<String,Object>> getCountryList(){
		//response.setContentType("application/json; charset=UTF-8");
		List<Map<String,Object>> areamapList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList(); 
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();	
		targetAreaIds = travelActivityService.findCountryAreaIds(companyId);
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){			
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		
		for(Area area : targetAreas){
			// 过滤后，只留下境外游国家
			String str = area.getParentIds();
			Long parentId = area.getParent()!=null?area.getParent().getId():0; // 因为数据库sys_area表里不存在id=0的记录，因此需要排除
			Long chinaId = new Long(200000);
			if(StringUtils.isNotBlank(str) && str.length()>12){
				String strback = str.substring(4,5);
				if(str!=null && strback!=null && "1".equals(strback)){
					Map<String,Object> areamap = new HashMap<String,Object>();
					areamap.put("id", area.getId());
					areamap.put("name", area.getName());
					areamapList.add(areamap);
				}
			}else if(parentId.longValue()==chinaId.longValue()){
				Map<String,Object> areamap = new HashMap<String,Object>();
				areamap.put("id", area.getId());
				areamap.put("name", area.getName());
				areamapList.add(areamap);
			}
		}
		return areamapList;
	}
	
	
	@RequestMapping(value="testInterFace/{pid}")
	public String testInterFace(@PathVariable("pid")Long pid,Model model,HttpServletRequest request, HttpServletResponse response){
		 estimatePriceRecordService.releaseProduct(pid,"7");
		return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/list4saler";
	}
	/**
	 * 多币种回复报价（地接）
	 * @author gao
	 * @time:2015 0515
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="replyAopEpriceForm")
	public String replyAopEpriceForm(HttpServletRequest request, HttpServletResponse response){
		String priceType = request.getParameter("priceType"); // 获取报价类型 1 整体报价 2 细分报价 3 机票报价
		String replyId = request.getParameter("replyId"); // 报价类（EstimatePricerReply）的ID
		StringBuffer operatorPrice = new StringBuffer(); // 总价字符串
		if(EstimatePricePartReply.PRICE_TYPE_ALL.equals(priceType)){ // 整体报价
			String[] adultExchangerates = request.getParameterValues("adultExchangerate"); // 成人汇率数组
			String[] adultCurrencyIds = request.getParameterValues("adultCurrencyId"); // 成人币种ID组
			String[] adultAmounts = request.getParameterValues("adultAmount"); // 成人单价金额
			String adultSum = request.getParameter("adultSum"); // 成人数量
			
			String[] childExchangerates = request.getParameterValues("childExchangerate"); // 儿童汇率数组
			String[] childCurrencyIds = request.getParameterValues("childCurrencyId"); // 儿童币种ID组
			String[] childAmounts = request.getParameterValues("childAmount"); // 儿童单价 金额
			String childSum = request.getParameter("childSum"); // 儿童数量
			
			String[] specialExchangerates = request.getParameterValues("specialExchangerate"); // 特殊汇率数组
			String[] specialCurrencyIds = request.getParameterValues("specialCurrencyId"); // 特殊币种ID组
			String[] specialAmounts = request.getParameterValues("specialAmount"); // 特殊单价金额
			String specialSum = request.getParameter("specialSum"); // 特殊数量	
			
			// 成人报价
			if(adultAmounts!=null && adultAmounts.length>0){
				int no = 0; 
				operatorPrice.append("成人地接总价：");
				for(String ex : adultAmounts){
					if(StringUtils.isNotBlank(ex)&&no<adultAmounts.length){
						// 多币种关联类
						EstimatePricePartReply rep = new EstimatePricePartReply();
						if(StringUtils.isNotBlank(adultSum)){
							rep.setPersonNum(Integer.valueOf(adultSum)); // 整体报价成人总数
						}else{
							rep.setPersonNum(Integer.valueOf(0)); // 整体报价成人总数
						}
						rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_ALL)); // 整体报价
						rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
						rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_ADULT));  // 成人
						// 流水实体
						MoneyAmount money = new MoneyAmount();
						money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
						money.setCurrencyId(Integer.valueOf(adultCurrencyIds[no]));/** 币种ID */
						money.setAmount(new BigDecimal(ex));/** 金额 */
						money.setExchangerate(new BigDecimal(adultExchangerates[no]));/** 汇率 */
						/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
						money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
						money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATE);/** 业务类型  询价报价*/
						money.setCreateTime(new Date());/** 生成时间 */
						money.setCreatedBy(UserUtils.getUser().getId());
						
						// 将多币种关联类和流水实体存入数据库
						EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
						money.setUid(repback.getId());
						moneyAmountService.saveOrUpdateMoneyAmount(money);
						// 统计成人总价
						operatorPrice = backSumAll( money, operatorPrice, no,adultAmounts, rep);
					}
					no++;
				}
			}
			// 儿童报价
			if(childAmounts!=null && childAmounts.length>0){
				int no = 0; 
				operatorPrice.append("儿童地接总价：");
				for(String ex : childAmounts){
					if(StringUtils.isNotBlank(ex)&&no<childAmounts.length){
						// 多币种关联类
						EstimatePricePartReply rep = new EstimatePricePartReply();
						if(StringUtils.isNotBlank(childSum)){
							rep.setPersonNum(Integer.valueOf(childSum)); // 整体报价总数
						}else{
							rep.setPersonNum(Integer.valueOf(0)); // 整体报价总数
						}
						rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_ALL)); // 整体报价
						rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
						rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_CHILD));  // 儿童
						// 流水实体
						MoneyAmount money = new MoneyAmount();
						money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
						money.setCurrencyId(Integer.valueOf(childCurrencyIds[no]));/** 币种ID */
						money.setAmount(new BigDecimal(ex));/** 金额 */
						money.setExchangerate(new BigDecimal(childExchangerates[no]));/** 汇率 */
						/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
						money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
						money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATE);/** 业务类型  询价报价*/
						money.setCreateTime(new Date());/** 生成时间 */
						money.setCreatedBy(UserUtils.getUser().getId());
						
						// 将多币种关联类和流水实体存入数据库
						EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
						money.setUid(repback.getId());
						moneyAmountService.saveOrUpdateMoneyAmount(money);
						
						// 统计儿童总价
						operatorPrice = backSumAll( money, operatorPrice, no,childAmounts, rep);
					}
					no++;
				}
			}
			// 特殊人群报价
			if(specialAmounts!=null && specialAmounts.length>0){
				int no = 0; 
				operatorPrice.append("特殊人群地接总价：");
				for(String ex : specialAmounts){
					if(StringUtils.isNotBlank(ex)&&no<specialAmounts.length){
						// 多币种关联类
						EstimatePricePartReply rep = new EstimatePricePartReply();
						if(StringUtils.isNotBlank(specialSum)){
							rep.setPersonNum(Integer.valueOf(specialSum)); // 整体报价总数
						}else{
							rep.setPersonNum(Integer.valueOf(0)); // 整体报价总数
						}
						rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_ALL)); // 整体报价
						rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
						rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_SPECIAL));  // 特殊
						// 流水实体
						MoneyAmount money = new MoneyAmount();
						money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
						money.setCurrencyId(Integer.valueOf(specialCurrencyIds[no]));/** 币种ID */
						money.setAmount(new BigDecimal(ex));/** 金额 */
						money.setExchangerate(new BigDecimal(specialExchangerates[no]));/** 汇率 */
						/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
						money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
						money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATE);/** 业务类型  询价报价*/
						money.setCreateTime(new Date());/** 生成时间 */
						money.setCreatedBy(UserUtils.getUser().getId());
						
						// 将多币种关联类和流水实体存入数据库
						EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
						money.setUid(repback.getId());
						moneyAmountService.saveOrUpdateMoneyAmount(money);
						
						// 统计特殊人群总价
						operatorPrice = backSumAll( money, operatorPrice, no,specialAmounts, rep);
					}
					no++;
				}
			}
		}else if(EstimatePricePartReply.PRICE_TYPE_PART.equals(priceType)){ // 细分报价
			String[] adultPartExchangerates = request.getParameterValues("adultPartExchangerate"); // 成人汇率数组
			String[] adultPartCurrencyIds = request.getParameterValues("adultPartCurrencyId"); // 成人币种ID组
			String[] adultPartAmounts = request.getParameterValues("adultPartAmount"); // 成人单价
			String[] adultPartSum = request.getParameterValues("adultPartNum"); // 成人组人数
			
			String[] childPartExchangerates = request.getParameterValues("childPartExchangerate"); // 儿童汇率数组
			String[] childPartCurrencyIds = request.getParameterValues("childPartCurrencyId"); // 儿童币种ID组
			String[] childPartAmounts = request.getParameterValues("childPartAmount"); // 儿童单价
			String[] childPartSum = request.getParameterValues("childPartNum"); // 儿童组人数
			
			String[] specialPartExchangerates = request.getParameterValues("specialPartExchangerate"); // 特殊汇率数组
			String[] specialPartCurrencyIds = request.getParameterValues("specialPartCurrencyId"); // 特殊币种ID组
			String[] specialPartAmounts = request.getParameterValues("specialPartAmount"); // 特殊单价
			String[] specialPartSum = request.getParameterValues("specialPartNum"); // 特殊组人数
			// 成人细分报价
			if(adultPartAmounts!=null && adultPartAmounts.length>0){
				int no = 0; 
				operatorPrice.append("成人地接总价：");
				for(String ex : adultPartAmounts){
					// 多币种关联类
					if(StringUtils.isNotBlank(ex)&&no<adultPartAmounts.length){
						EstimatePricePartReply rep = new EstimatePricePartReply();
						if(StringUtils.isNotBlank(adultPartSum[no])){
							rep.setPersonNum(Integer.valueOf(adultPartSum[no])); // 细分报价成人数
						}else{
							rep.setPersonNum(0); // 细分报价成人数
						}
						rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_PART)); // 细分报价类型
						rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
						rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_ADULT));  // 成人
						// 流水实体
						MoneyAmount money = new MoneyAmount();
						money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
						money.setCurrencyId(Integer.valueOf(adultPartCurrencyIds[no]));/** 币种ID */
						money.setAmount(new BigDecimal(ex));/** 金额 */
						money.setExchangerate(new BigDecimal(adultPartExchangerates[no]));/** 汇率 */
						/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
						money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
						money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATE);/** 业务类型  询价报价*/
						money.setCreateTime(new Date());/** 生成时间 */
						money.setCreatedBy(UserUtils.getUser().getId());
						
						// 将多币种关联类和流水实体存入数据库
						EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
						money.setUid(repback.getId());
						moneyAmountService.saveOrUpdateMoneyAmount(money);
						// 统计成人总价
						operatorPrice = backSumAll( money, operatorPrice, no,adultPartAmounts, rep);
					}
					no++;
				}
			}
			// 儿童细分报价
			if(childPartAmounts!=null && childPartAmounts.length>0){
				int no = 0; 
				operatorPrice.append("儿童地接总价：");
				for(String ex : childPartAmounts){
					if(StringUtils.isNotBlank(ex)&&no<childPartAmounts.length){
						// 多币种关联类
						EstimatePricePartReply rep = new EstimatePricePartReply();
						if(StringUtils.isNotBlank(childPartSum[no])){
							rep.setPersonNum(Integer.valueOf(childPartSum[no])); // 细分报价儿童数
						}else{
							rep.setPersonNum(0);
						}
						rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_PART)); // 细分报价类型
						rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
						rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_CHILD));  // 儿童
						// 流水实体
						MoneyAmount money = new MoneyAmount();
						money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
						money.setCurrencyId(Integer.valueOf(childPartCurrencyIds[no]));/** 币种ID */
						money.setAmount(new BigDecimal(ex));/** 金额 */
						money.setExchangerate(new BigDecimal(childPartExchangerates[no]));/** 汇率 */
						/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
						money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
						money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATE);/** 业务类型  询价报价*/
						money.setCreateTime(new Date());/** 生成时间 */
						money.setCreatedBy(UserUtils.getUser().getId());
						
						// 将多币种关联类和流水实体存入数据库
						EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
						money.setUid(repback.getId());
						moneyAmountService.saveOrUpdateMoneyAmount(money);
						// 统计儿童总价
						operatorPrice = backSumAll( money, operatorPrice, no,childPartAmounts, rep);
					}
					no++;
				}
			}
			// 特殊人群细分报价
			if(specialPartAmounts!=null && specialPartAmounts.length>0){
				int no = 0; 
				operatorPrice.append("特殊人群地接总价：");
				for(String ex : specialPartAmounts){
					if(StringUtils.isNotBlank(ex)&&no<specialPartAmounts.length){

						// 多币种关联类
						EstimatePricePartReply rep = new EstimatePricePartReply();
						if(StringUtils.isNotBlank(specialPartSum[no])){
							rep.setPersonNum(Integer.valueOf(specialPartSum[no])); // 细分报价特殊人群分组数
						}else{
							rep.setPersonNum(0);
						}
						rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_PART)); // 细分报价类型
						rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
						rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_SPECIAL));  // 特殊人群
						// 流水实体
						MoneyAmount money = new MoneyAmount();
						money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
						money.setCurrencyId(Integer.valueOf(specialPartCurrencyIds[no]));/** 币种ID */
						money.setAmount(new BigDecimal(ex));/** 金额 */
						money.setExchangerate(new BigDecimal(specialPartExchangerates[no]));/** 汇率 */
						/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
						money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
						money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATE);/** 业务类型  询价报价*/
						money.setCreateTime(new Date());/** 生成时间 */
						money.setCreatedBy(UserUtils.getUser().getId());
						
						// 将多币种关联类和流水实体存入数据库
						EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
						money.setUid(repback.getId()); 
						moneyAmountService.saveOrUpdateMoneyAmount(money);
						// 统计特殊人群总价
						operatorPrice = backSumAll( money, operatorPrice, no,specialPartAmounts, rep);
					}
					no++;
				}
			}
		}
		// 修正 EstimatePricerReply 回复类状态
		String content = request.getParameter("content"); // 计调回复备注
		String[] salerTripFileId = request.getParameterValues("salerTripFileId");//行程文档Id
		String[] salerTripFileName =request.getParameterValues("salerTripFileName"); //行程文档name
		String[] salerTipFilePath =request.getParameterValues("salerTipFilePath");//行程文档path
		estimatePriceRecordService.replyPay4admit(Long.valueOf(replyId), content, salerTripFileId, salerTripFileName, salerTipFilePath,operatorPrice.toString());
		return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordlist";
	}

	// 统计总价(包括整体统计和细分统计)
	private StringBuffer backSumAll(MoneyAmount money,StringBuffer operatorPrice,int no,String[] amounts,EstimatePricePartReply rep){
		Currency currency= currencyService.findCurrency(Long.valueOf(money.getCurrencyId()));
		operatorPrice.append(currency.getCurrencyMark()+money.getAmount().multiply(new BigDecimal(rep.getPersonNum()))); // 计算结果：币种单价*人数
		if((no+1)<amounts.length){
			operatorPrice.append("+");
		}else{
			operatorPrice.append(";");
		}
		return operatorPrice;
	}
	/**
	 * 多币种回复报价（机票）
	 * @author gao
	 * @time:2015 0515
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="replyTopEpriceForm")
	public String replyTopEpriceForm(HttpServletRequest request, HttpServletResponse response){
		String replyId = request.getParameter("rpid"); // 报价类（EstimatePricerReply）的ID
		String[] adultExchangerates = request.getParameterValues("adultExchangerate"); // 成人汇率数组
		String[] adultCurrencyIds = request.getParameterValues("adultCurrencyId"); // 成人币种ID组
		String[] adultAmounts = request.getParameterValues("adultAmount"); // 成人单价金额
		String adultSum = request.getParameter("adultSum"); // 成人数量
		
		String[] childExchangerates = request.getParameterValues("childExchangerate"); // 儿童汇率数组
		String[] childCurrencyIds = request.getParameterValues("childCurrencyId"); // 儿童币种ID组
		String[] childAmounts = request.getParameterValues("childAmount"); // 儿童单价 金额
		String childSum = request.getParameter("childSum"); // 儿童数量
		
		String[] specialExchangerates = request.getParameterValues("specialExchangerate"); // 特殊汇率数组
		String[] specialCurrencyIds = request.getParameterValues("specialCurrencyId"); // 特殊币种ID组
		String[] specialAmounts = request.getParameterValues("specialAmount"); // 特殊单价金额
		String specialSum = request.getParameter("specialSum"); // 特殊数量	
		
		StringBuffer operatorPrice = new StringBuffer(); // 总价字符串
		// 成人报价
		if(adultAmounts!=null && adultAmounts.length>0){
			int no = 0; 
			operatorPrice.append("成人机票总价：");
			for(String ex : adultAmounts){
				if(StringUtils.isNotBlank(ex)&&no<adultAmounts.length){
					// 多币种关联类
					EstimatePricePartReply rep = new EstimatePricePartReply();
					if(StringUtils.isNotBlank(adultSum)){
						rep.setPersonNum(Integer.valueOf(adultSum)); // 整体报价成人总数
					}else{
						rep.setPersonNum(Integer.valueOf(0)); // 整体报价成人总数
					}
					rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_AOP)); // 机票报价
					rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
					rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_ADULT));  // 成人
					// 流水实体
					MoneyAmount money = new MoneyAmount();
					money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
					money.setCurrencyId(Integer.valueOf(adultCurrencyIds[no]));/** 币种ID */
					money.setAmount(new BigDecimal(ex));/** 金额 */
					money.setExchangerate(new BigDecimal(adultExchangerates[no]));/** 汇率 */
					/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
					money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
					money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATETOP);/** 业务类型  机票报价*/
					money.setCreateTime(new Date());/** 生成时间 */
					money.setCreatedBy(UserUtils.getUser().getId());
					
					// 将多币种关联类和流水实体存入数据库
					EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
					money.setUid(repback.getId());
					moneyAmountService.saveOrUpdateMoneyAmount(money);
					// 统计成人总价
					operatorPrice = backSumAll( money, operatorPrice, no,adultAmounts, rep);
				}
				no++;
			}
		}
		// 儿童报价
		if(childAmounts!=null && childAmounts.length>0){
			int no = 0; 
			operatorPrice.append("儿童机票总价：");
			for(String ex : childAmounts){
				if(StringUtils.isNotBlank(ex)&&no<childAmounts.length){
					// 多币种关联类
					EstimatePricePartReply rep = new EstimatePricePartReply();
					if(StringUtils.isNotBlank(childSum)){
						rep.setPersonNum(Integer.valueOf(childSum)); // 整体报价总数
					}else{
						rep.setPersonNum(Integer.valueOf(0)); // 整体报价总数
					}
					rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_AOP)); // 整体报价
					rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
					rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_CHILD));  // 儿童
					// 流水实体
					MoneyAmount money = new MoneyAmount();
					money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
					money.setCurrencyId(Integer.valueOf(childCurrencyIds[no]));/** 币种ID */
					money.setAmount(new BigDecimal(ex));/** 金额 */
					money.setExchangerate(new BigDecimal(childExchangerates[no]));/** 汇率 */
					/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
					money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
					money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATETOP);/** 业务类型  机票报价*/
					money.setCreateTime(new Date());/** 生成时间 */
					money.setCreatedBy(UserUtils.getUser().getId());
					
					// 将多币种关联类和流水实体存入数据库
					EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
					money.setUid(repback.getId());
					moneyAmountService.saveOrUpdateMoneyAmount(money);
					// 统计儿童总价
					operatorPrice = backSumAll( money, operatorPrice, no,adultAmounts, rep);
				}
				no++;
			}
		}
		// 特殊人群报价
		if(specialAmounts!=null && specialAmounts.length>0){
			int no = 0; 
			operatorPrice.append("特殊人群机票总价：");
			for(String ex : specialAmounts){
				if(StringUtils.isNotBlank(ex)&&no<specialAmounts.length){
					// 多币种关联类
					EstimatePricePartReply rep = new EstimatePricePartReply();
					if(StringUtils.isNotBlank(specialSum)){
						rep.setPersonNum(Integer.valueOf(specialSum)); // 整体报价总数
					}else{
						rep.setPersonNum(Integer.valueOf(0)); // 整体报价总数
					}
					rep.setPriceType(Integer.valueOf(EstimatePricePartReply.PRICE_TYPE_AOP)); // 整体报价
					rep.setEstimatePriceReplyId(Long.valueOf(replyId)); // 报价类ID
					rep.setPersonType(Integer.valueOf(EstimatePricePartReply.PERSON_TYPE_SPECIAL));  // 特殊
					// 流水实体
					MoneyAmount money = new MoneyAmount();
					money.setSerialNum(UUID.randomUUID().toString());/** 流水号 */
					money.setCurrencyId(Integer.valueOf(specialCurrencyIds[no]));/** 币种ID */
					money.setAmount(new BigDecimal(ex));/** 金额 */
					money.setExchangerate(new BigDecimal(specialExchangerates[no]));/** 汇率 */
					/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
					money.setMoneyType(Context.MONEY_TYPE_ESTIMATE);/** 款项类型 询价报价*/
					money.setBusindessType(Context.MONEY_BUSINESSTYPE_ESTIMATETOP);/** 业务类型  机票报价*/
					money.setCreateTime(new Date());/** 生成时间 */
					money.setCreatedBy(UserUtils.getUser().getId());
					
					// 将多币种关联类和流水实体存入数据库
					EstimatePricePartReply repback = estimatePricePartReplyService.save(rep);
					money.setUid(repback.getId());
					moneyAmountService.saveOrUpdateMoneyAmount(money);
					// 统计特殊人群总价
					operatorPrice = backSumAll( money, operatorPrice, no,adultAmounts, rep);
				}
				no++;
			}
		}
		// 获取对应的计调回复项
		if(StringUtils.isBlank(replyId)){
			/* 失败日志 */
			return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordtrafficlist";
		}
		EstimatePricerReply epr = estimatePricerReplyService.findById(Long.valueOf(replyId));
		if(epr==null){
			/* 失败日志 */
			return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordtrafficlist";
		}
		// 获得询价记录类
		EstimatePriceRecord eprecord = estimatePriceRecordService.findById(epr.getRid());
		if(eprecord==null){
			/* 失败日志 */
			return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordtrafficlist";
		}
		// 获得机票询价详情类
		EstimatePriceTrafficRequirements require = eprecord.getTrafficRequirements();
		if(require==null){
			/* 失败日志 */
			return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordtrafficlist";
		}
		// 获得询价项目类
		EstimatePriceProject epp =  estimatePriceProjectService.findById(epr.getPid());
		if(epp==null){
			/* 失败日志 */
			return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordtrafficlist";
		}
		// 报价记录状态改为已回复
		String remark = request.getParameter("remark"); // 计调回复备注
		epr.setPriceDetail(operatorPrice.toString()); // 将总价写入
		epr.setRemark(remark);
		epr.setStatus(EstimatePricerReply.STATUS_REPLYED);
		epr.setOperatorPriceTime(new Date());
		estimatePricerReplyService.save(epr);
		// 询价记录改为已报价
		eprecord.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_GIVEN);
		eprecord.setLastToperatorPriceTime(new Date());
		estimatePriceRecordService.save(eprecord);
		
		// 询价项目改为已报价
		if(EstimatePriceProject.ESTIMATE_STATUS_WAITING==epp.getEstimateStatus()
				&& EstimatePriceProject.TYPE_FLIGHT!=epp.getType()){
			List<EstimatePricerReply> list = estimatePricerReplyDao.find(" from EstimatePricerReply where pid = '"+epp.getId()+
					"' and rid = "+eprecord.getId()+" and type !='"+EstimatePricerReply.TYPE_FLIGHT+"' and status >1");
			if(list!=null&&list.size()>0){
				if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>epp.getEstimateStatus()){
					epp.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
				}
			}
		}else{
			if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>epp.getEstimateStatus()){
				epp.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
			}
		}
		epp.setLastOperatorGivenTime(new Date());
		estimatePriceProjectService.save(epp);

		//询价记录日志
		StringBuffer content =new StringBuffer("机票计调:(ID "+UserUtils.getUser().getId()+")"+UserUtils.getUser().getName());
		content.append(", 回复销售员：(ID "+epp.getUserId()+")"+epp.getUserName());
		//content.append("项目编号(estimate_eprice_record.id)：" +record.getId());
		content.append(", 询价客户为 "+epp.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(epp.getType())+"询价。");
		content.append(" 客户预算："+EstimatePriceUtils.backBudgetType(epp.getLastBaseInfo().getBudgetType())+", ");
		content.append("预算金额："+epp.getLastBaseInfo().getBudget()+"， ");
		content.append("预算币种：人民币， ");
		content.append("备注："+epp.getLastBaseInfo().getBudgetRemark()+"， ");
		content.append("申请总人数："+epp.getLastBaseInfo().getAllPersonSum()+"， ");
		content.append("申请成人数："+epp.getLastBaseInfo().getAdultSum()+"， ");
		content.append("申请儿童数："+epp.getLastBaseInfo().getChildSum()+"， ");
		content.append("申请特殊人群数："+epp.getLastBaseInfo().getSpecialPersonSum()+"， ");
		content.append("特殊人群备注："+epp.getLastBaseInfo().getSpecialRemark()+"。 ");		
		content.append(" 操作人："+UserUtils.getUser().getName());
		content.append(", 操作时间："+ new Date());
		
		logOpeService.saveLogOperate(Context.log_type_price,
				Context.log_type_price_name, content.toString(), Context.log_state_add, null, null);
		//location.href=contextPath+"/eprice/manager/project/erecordtrafficlist";
		return "redirect:"+Global.getAdminPath()+"/eprice/manager/project/erecordtrafficlist";
	}
}
