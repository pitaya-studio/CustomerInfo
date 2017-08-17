/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.multi.tenant.manage.service.TenantUtil;
import com.trekiz.admin.agentToOffice.PricingStrategy.service.ActivityPricingStrategyService;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.agentToOffice.quauqstrategy.service.QuauqGroupStrategyService;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.excel.ExportNewExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.utils.TravelActivityUtil;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.TreeNodeUtil;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.t1.utils.T1Utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 渠道Controller
 * 
 * @author zj
 * @version 2014-01-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	private static final Log LOG = LogFactory.getLog(OfficeController.class);

	@ModelAttribute("menuId")
	protected Integer getMenuId() {
		return 17;
	}

	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private ActivityPricingStrategyService activityPricingStrategyService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private QuauqGroupStrategyService quauqGroupStrategySevice;
	@Autowired
	private CurrencyService currencyService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) Long id, Model model) {
		if (id != null) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "list", "" })
	public String list(Office office, Model model) {
		// User user = UserUtils.getUser();
		// if(user.isAdmin()){
		office.setId(1L);
		// }else{
		// office.setId(user.getOffice().getId());
		// }
		model.addAttribute("office", office);
		List<Office> list = Lists.newArrayList();
		List<Office> sourcelist = officeService.findAll();
		Office.sortList(list, sourcelist, office.getId());
		model.addAttribute("list", list);
		return "modules/sys/officeList";
	}
	
	/**
	 * 522需求-供应价查看页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "officeListForExport")
	public String officeListForExport(HttpServletRequest request, HttpServletResponse response,Model model){
		
		Page<Map<Object,Object>> page = travelActivityService.searchProductAndActivityList(new Page<Map<Object, Object>>(request, response), request, model);
		model.addAttribute("currentPage", page.getPageNo());
		model.addAttribute("totalPage", (page.getCount()%page.getPageSize()==0)?(page.getCount()/page.getPageSize()):(page.getCount()/page.getPageSize()+1));
		int allCount = travelActivityService.getCount(true);
		int someCount = travelActivityService.getCount(false);
		model.addAttribute("allCount", allCount);
		model.addAttribute("someCount", someCount);
		
		//testflag(request,model);
		List<Map<Object, Object>> list = page.getList();
		for(Map<Object, Object> maplist : list){
			Object officeId = maplist.get("id");
			StringBuffer activityGrouopIds = new StringBuffer();
			List<Map<String,Object>> findProductAndActivityList = travelActivityService.findProductAndActivityList(officeId.toString(), request, model);
			for(Map<String,Object> map :findProductAndActivityList){
				Object activityId = map.get("agid") ;
				if(activityId != null){
					Object activityKind = map.get("activity_kind");
					String settlementAdultPrice = map.get("settlementAdultPrice") == null ? " " :map.get("settlementAdultPrice").toString() ;
					String settlementcChildPrice = map.get("settlementcChildPrice") == null ? " ": map.get("settlementcChildPrice").toString();
					String settlementSpecialPrice = map.get("settlementSpecialPrice") == null ? "" : map.get("settlementSpecialPrice").toString();
					String quauqAdultPrice = map.get("quauqAdultPrice") == null ? "" : map.get("quauqAdultPrice").toString();
					String quauqChildPrice = map.get("quauqChildPrice") == null ? "" : map.get("quauqChildPrice").toString();
					String quauqSpecialPrice = map.get("quauqSpecialPrice") == null ? "" : map.get("quauqSpecialPrice").toString();
					String currencyIds = map.get("currency_type").toString();
					String[] currencys = currencyIds.split(",");
					int hasEyelessAgents = TravelActivityUtil.hasEyelessAgents(activityId.toString(), Integer.parseInt(activityKind.toString()), settlementAdultPrice,settlementcChildPrice,settlementSpecialPrice,quauqAdultPrice,quauqChildPrice,quauqSpecialPrice,currencys);
					if(hasEyelessAgents > 0){
						maplist.put("flag", true);
						activityGrouopIds.append(activityId+" ");
					}
				}
				maplist.put("activityGrouopIds", activityGrouopIds.toString());
			}
			//boolean flag = false;
			/*boolean empty = findProductAndActivityList.isEmpty();
			if(!empty){
				maplist.put("flag", true);
				for(Map<String,Object> searchList : findProductAndActivityList){
					Object object = searchList.get("agid");
					activityGrouopIds.append(object+" ");
				}
				maplist.put("activityGrouopIds", activityGrouopIds.toString());
			}*/
		}
		model.addAttribute("page", page);
		return "agentToOffice/quauqAgent/retailPriceList";
	}
	/**
	 * 测试用
	 * @param request
	 * @param model
	 */
	public void testflag(HttpServletRequest request,Model model){
		Object officeId = 72;
		StringBuffer activityGrouopIds = new StringBuffer();
		List<Map<String,Object>> findProductAndActivityList = travelActivityService.findProductAndActivityList(officeId.toString(), request, model);
		if(findProductAndActivityList.size() > 0){
			for(Map<String,Object> searchList : findProductAndActivityList){
				Object activityId = searchList.get("agid");		//团期id
				if(activityId != null){	
				Object productType = searchList.get("activity_kind"); 	//产品类型
				Object currencyIds = searchList.get("currency_type"); 	//产品对应货币id
				String[] currencys = currencyIds.toString().split(",");
				//同行价
				String settlementAdultPrice = searchList.get("settlementAdultPrice") == null ? "" : searchList.get("settlementAdultPrice").toString();
				String settlementcChildPrice = searchList.get("settlementcChildPrice") == null ? "" : searchList.get("settlementcChildPrice").toString();
				String settlementSpecialPrice = searchList.get("settlementSpecialPrice") == null ? "" : searchList.get("settlementSpecialPrice").toString();
				//quauq价
				String quauqAdultPrice =  searchList.get("quauqAdultPrice") == null ? "" : searchList.get("quauqAdultPrice").toString();
				String quauqChildPrice = searchList.get("quauqChildPrice") == null ? "" : searchList.get("quauqChildPrice").toString();
				String quauqSpecialPrice = searchList.get("quauqSpecialPrice") == null ? "" : searchList.get("quauqSpecialPrice").toString();
				//获取供应价
				boolean tag = TravelActivityUtil.changeValueType(activityId.toString(),Integer.parseInt(productType.toString()) 
														,settlementAdultPrice,settlementcChildPrice,settlementSpecialPrice
														,quauqAdultPrice,quauqChildPrice,quauqSpecialPrice,currencys);
				//如果同行价  < 供应价 ,flag为true 显示提示
				if(tag ){
					@SuppressWarnings("unused")
					boolean flag = true;
					activityGrouopIds.append(activityId+" ");
				}
				/*maplist.put("flag", flag);
				maplist.put("activityGrouopIds", activityGrouopIds.toString());*/
				}
			}
		}
	}
	/**
	 * 522
	 * 使用excel导出批发商的信息
	 * @param request
	 * @param response
	 * @param model
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="exportExcelOffice")
	public void exportExcelOffice( HttpServletRequest request, HttpServletResponse response,Model model){
		try {
		Object officeId = request.getParameter("officeId");
		Object activityGrouopIds = request.getParameter("activityGrouopIds");//不满足同行价>供应价的团期的ID
		Long parseLong = null;
		if(officeId != null){
			 parseLong = Long.parseLong(officeId.toString());
		}
		String[] split = activityGrouopIds.toString().split(" ");
		List<ActivityGroup> activityGroupList = new ArrayList<ActivityGroup>();
		
		for(int i = 0; i < split.length; i++){
			ActivityGroup groupListByGroupId = activityGroupService.getGroupListByGroupId(Long.parseLong(split[i]));
			activityGroupList.add(groupListByGroupId);
		}
		String filename = "";
		List<Object[]> resultList = new ArrayList<Object[]>();
		SimpleDateFormat format=new SimpleDateFormat("yy/MM/dd");
		//获取渠道商
		List<Map<Object,Object>> agentList = quauqGroupStrategySevice.getAllT1Agent();
		List<String> agentName = new ArrayList<String>();
		for(Map<Object,Object> map :agentList){
			agentName.add(map.get("agentName").toString());
		}
		
		//int i = 0;
		for(ActivityGroup ag : activityGroupList){
			Object[] result = new Object[12];
			TravelActivity travelActivity = ag.getTravelActivity();
			//文件名
			filename= travelActivity.getProCompanyName();
			//币种
			String currencyType = ag.getCurrencyType();
			String[] currencys = currencyType.split(",");
			Currency currency = currencyService.findCurrency(Long.parseLong(currencys[0]));
			//获取定价策略
			Map<String,String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(travelActivity.getId().toString(),ag.getId().toString());
			result[0] = travelActivity.getProCompanyName();	//批发商名称
			result[1] = travelActivity.getAcitivityName();	//产品名称
			result[2] = ag.getGroupCode();	//团号
			result[3] = format.format(ag.getGroupOpenDate());	//出团日期
			result[4] = travelActivity.getFromAreaName();	//出发城市
			result[5] = ag.getSettlementAdultPrice() == null ? "" :currency.getCurrencyMark() + ag.getSettlementAdultPrice();	//同行价 - 成人
			result[6] = ag.getSettlementcChildPrice() == null ? "" :currency.getCurrencyMark() +ag.getSettlementcChildPrice();	//同行价 - 儿童
			result[7] = ag.getSettlementSpecialPrice() == null ? "" :currency.getCurrencyMark() +ag.getSettlementSpecialPrice();	//同行价 - 特殊人群
			result[8] = pricingStrategy.get("adultPricingStrategy");	//定价策略 - 成人
			result[9] = pricingStrategy.get("childrenPricingStrategy");		//定价策略 - 儿童
			result[10] = pricingStrategy.get("specialPricingStrategy");		//定价策略 - 特殊人群
		
			/*System.out.println("第"+i+"次  团期id " +ag.getId()+new Date());
			i++;*/
			//获取渠道商的对应的价格（供应价>同行价）
			List<String> agentPrice = new ArrayList<String>();
			StringBuffer agentprices = new StringBuffer();
			for(Map<Object,Object> map :agentList){
				//根据团期ID、产品类型、渠道商id获取对应的费率
				Rate rate = RateUtils.getRate(ag.getId(), travelActivity.getActivityKind(), Long.parseLong(map.get("agentId").toString()));
				BigDecimal tempAdultPrice = null;
				BigDecimal tempChildrenPrice = null;
				BigDecimal tempSpecialPrice = null;
				DecimalFormat priceFormat =new DecimalFormat("0.00");
				
				Object[] price = new Object[3];
				price[0] = " ";
				price[1] = " ";
				price[2] = " ";
				//同行价 》 供应价 渠道对应的价格制空	 	同行價 《供應價  顯示價格 
				boolean adultResult = false;
				if(ag.getQuauqAdultPrice() != null){
					//根据quauq价、费率,获取对应的供应价
					tempAdultPrice = OrderCommonUtil.getRetailPrice(ag.getQuauqAdultPrice(),rate,Long.parseLong(currencys[0]));
					//如果同行价 《 供应价 ，则记录下供应价,相反置为空
					if(ag.getSettlementAdultPrice().compareTo(tempAdultPrice) < 1){
						adultResult = true;
						String adultprice = tempAdultPrice == null ? "" :currency.getCurrencyMark() + priceFormat.format(tempAdultPrice);
						agentprices.append(adultprice+" ");
					}else{
						agentprices.append(" ");
					}
				}else{
					agentprices.append(" ");
				}
				boolean childResult = false;
				if(ag.getQuauqChildPrice()!= null){
					tempChildrenPrice = OrderCommonUtil.getRetailPrice(ag.getQuauqChildPrice(),rate,Long.parseLong(currencys[0]));
					if(ag.getSettlementcChildPrice().compareTo(tempChildrenPrice) < 1){
						childResult = true;
						String childprice = tempChildrenPrice == null ? "" :currency.getCurrencyMark() + priceFormat.format(tempChildrenPrice);
						agentprices.append(childprice+" ");
					}else{
						agentprices.append(" ");
					}
				}else{
					agentprices.append(" ");
				}
				boolean spacialResult = false;
				if(ag.getQuauqSpecialPrice() != null){
					tempSpecialPrice = OrderCommonUtil.getRetailPrice(ag.getQuauqSpecialPrice(),rate,Long.parseLong(currencys[0]));
					BigDecimal settlementSpecialPrice = ag.getSettlementSpecialPrice();
					if(ag.getSettlementSpecialPrice().compareTo(tempSpecialPrice) < 1){
						spacialResult = true;
						String speciprice = tempSpecialPrice == null ? "" :currency.getCurrencyMark() + priceFormat.format(tempSpecialPrice);
						agentprices.append(speciprice+" ");
					}else{
						agentprices.append(" ");
					}
				}else{
					agentprices.append(" ");
				}
				//输出所有的渠道商名称
				//当供应价 > 同行价的时候，输出该渠道商的名称和对应的供应价
				//if(adultResult || childResult || spacialResult){
					/*agentName.add(map.get("agentName").toString());
					agentName.add(map.get("agentName").toString());
					agentName.add(map.get("agentName").toString());*/
					
					//agentPrice.add(agentprices.toString());
				//}
			}
			//result[11] = agentPrice;
			result[11] = agentprices.toString();
			resultList.add(result);	
		}
		ExportNewExcel.exportOfficeActivityExcel(filename,resultList,agentName, request, response);
		//更新倒出時間
		Date exportTime = new Date();
		officeService.updateOffice(exportTime, parseLong);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("导出批发商信息出错");
			e.printStackTrace();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		UserUtils.getUser();
		model.addAttribute("is_check_domainName", office.getIsValidateDoma());
		model.addAttribute("is_cancle_order", office.getIsCancleOrder());
		model.addAttribute("is_allow_supplement", office.getIsAllowSupplement());
		model.addAttribute("estimate_model", office.getEstimateModel());
		model.addAttribute("is_need_attention", office.getIsNeedAttention());
		model.addAttribute("mtourCompanyUuid", Global.getConfig("mtourCompanyUuid"));
		model.addAttribute("isAllowModifyAgentInfo", office.getIsAllowModifyAgentInfo());
		model.addAttribute("isAllowAddAgentInfo", office.getIsAllowAddAgentInfo());
		model.addAttribute("is_allow_modify", office.getIsAllowModify());
		model.addAttribute("budgetCostAutoPass", office.getBudgetCostAutoPass());
		model.addAttribute("costAutoPass", office.getCostAutoPass());
		model.addAttribute("confirmPay", office.getConfirmPay());
		model.addAttribute("whenToSheet", office.getWhenToSheet());
		model.addAttribute("budgetCostWhenUpdate", office.getBudgetCostWhenUpdate());
		model.addAttribute("actualCostWhenUpdate", office.getActualCostWhenUpdate());
		model.addAttribute("create_sub_order", office.getCreateSubOrder());
		model.addAttribute("order_pay_mode", office.getOrderPayMode());
		model.addAttribute("query_common_order_list", office.getQueryCommonOrderList());
		model.addAttribute("query_airticket_order_list", office.getQueryAirticketOrderList());
		model.addAttribute("query_common_fields", office.getQueryCommonFields());
		model.addAttribute("query_airticket_fields", office.getQueryAirticketFields());
		model.addAttribute("visaCostPrice",office.getVisaCostPrice());
		model.addAttribute("is_need_groupCode",office.getIsNeedGroupCode());
		model.addAttribute("is_need_cruiseshipControll",office.getIsNeedCruiseshipControll());
		/*是否允许多返佣对象*/
		model.addAttribute("isAllowMultiRebateObject",office.getIsAllowMultiRebateObject());
		model.addAttribute("isShowCancelOrder",office.getIsShowCancelOrder());
		model.addAttribute("isShowDeleteOrder",office.getIsShowDeleteOrder());
//		model.addAttribute("isSeizedConfirmation",office.getIsSeizedConfirmation());
		//0318新增是否允许修改销售签证订单下的游客信息--s//
		model.addAttribute("isAllowModifyXSVisaOrder",office.getIsAllowModifyXSVisaOrder());
		//0318新增是否允许修改销售签证订单下的游客信息--e//
		// 增加服务费率 
		model.addAttribute("chargeRate",office.getChargeRate());
		// 签证"全部订单"Tab页中禁止显示的订单
		model.addAttribute("banedVisaOrderOfAllTab",office.getBanedVisaOrderOfAllTab());
		// 增加服务费率
		// 0444 预开发票 
		model.addAttribute("preOpenInvoice",office.getPreOpenInvoice());
		model.addAttribute("isMustRefundDate",office.getIsMustRefundDate());
		/*团号规则S*/
		model.addAttribute("groupCodeRuleDT",office.getGroupCodeRuleDT());//団期
		model.addAttribute("groupCodeRuleJP",office.getGroupCodeRuleJP());//机票
		model.addAttribute("groupCodeRuleQZ",office.getGroupCodeRuleQZ());//签证
		// 474 是否解除发票申请限制
		model.addAttribute("isRemoveApplyInvoiceLimit",office.getIsRemoveApplyInvoiceLimit());
		// 0492 T1平台余位状态：0.实时 1.现询
		model.addAttribute("t1FreePosionStatus",office.getT1FreePosionStatus());
		// forbug公司logo不能删掉
		model.addAttribute("logo", office.getLogo());
		// 0517
		// 营业执照
		model.addAttribute("businessLicense", office.getBusinessLicense());
		// 业务资质证书
		model.addAttribute("businessCertificate", office.getBusinessCertificate());
		// 合作证书
		model.addAttribute("cooperationProtocol", office.getCooperationProtocol());
		// 备注
		model.addAttribute("remarks", office.getRemarks());
		/*团号规则E*/
//		model.addAttribute("isMailRemind",office.getIsMailRemind());

		if (StringUtils.isNotBlank(office.getSupplierType())) {
			String[] supplierType = office.getSupplierType().split(",");
			for (int i = 0; i < supplierType.length; i++) {
				if (Integer.parseInt(supplierType[i]) <= 10) {
					model.addAttribute("supplierType".concat(String.valueOf(supplierType[i])), supplierType[i]);
				}
			}
		}
		// 上传的logo返回到前台
		if (null != office.getLogo()) {
			model.addAttribute("docInfo", docInfoDao.findOne(office.getLogo()));
		}
		
		// 上传的营业执照返回前台
		if (null != office.getBusinessLicense() && office.getBusinessLicense().contains(";")) {
			String[] docIds = office.getBusinessLicense().split(";");
			List<DocInfo> docInfoList = new ArrayList<>();
			for (int i = 0; i < docIds.length; i++) {
				docInfoList.add(docInfoDao.findOne(Long.valueOf(docIds[i])));
			}
			model.addAttribute("businessLicenseDocInfos", docInfoList);
		}
		
		// 上传的业务资质证书返回前台
		if (null != office.getBusinessCertificate() && office.getBusinessCertificate().contains(";")) {
			String[] docIds = office.getBusinessCertificate().split(";");
			List<DocInfo> docInfoList = new ArrayList<>();
			for (int i = 0; i < docIds.length; i++) {
				docInfoList.add(docInfoDao.findOne(Long.valueOf(docIds[i])));
			}
			model.addAttribute("businessCertificateDocInfos", docInfoList);
		}
		
		// 上传的合作证书返回前台
		if (null != office.getCooperationProtocol() && office.getCooperationProtocol().contains(";")) {
			String[] docIds = office.getCooperationProtocol().split(";");
			List<DocInfo> docInfoList = new ArrayList<>();
			for (int i = 0; i < docIds.length; i++) {
				docInfoList.add(docInfoDao.findOne(Long.valueOf(docIds[i])));
			}
			model.addAttribute("cooperationProtocolDocInfos", docInfoList);
		}
		model.addAttribute("id", office.getId());
		return "modules/sys/officeForm";
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "saveOffice")
	public String saveOffice(Office office, Model model, RedirectAttributes redirectAttributes) {
		boolean isSave = true;
		if(null != office.getId()) {
			isSave = false;
		}
		if (!beanValidator(model, office)) {
			return form(office, model);
		}

		// 如果批发商名称或logo改变则T1首页缓存需要重新加载
		List<Map<String, Object>> oldList = null;
		if (null != office.getId()) {
			String sqlString = "SELECT name, logo FROM sys_office WHERE id = " + office.getId();
			oldList = officeDao.findBySql(sqlString, Map.class);
		}
		
		if (office.getId() != null && office.getChargeRate() != null) {
			BigDecimal bigDecimal_db = officeDao.getChargeRateByOfficeId(office.getId());
			if(bigDecimal_db.doubleValue() != office.getChargeRate().doubleValue() && UserUtils.getUser().getId()!= 1 ){
				// 只有superadmin才能修改供应商的服务费率
				return "modules/sys/officeForm";
			}
		}
		
		// 在修改厂商的时候，修改部门表中此批发商顶级菜单名称
		if (office.getId() != null) {
			Department dept = departmentService.getParent(office);
			if (dept != null) {
				dept.setName(office.getName());
				dept.setParent(new Department(0L));
				departmentService.save(dept);
			}
			
		}
		
		//新增批发商的时候添加 uuid
		if(null == office.getId()) {
			office.setUuid(UuidUtils.generUuid());
		}

		officeService.save(office);
		addMessage(redirectAttributes, "保存批发商'" + office.getName() + "'成功");
		//为租户管理库增加公司（批发商）与租户的映射20170228
		if(isSave) {
			String userId = UserUtils.getUser().getLoginName();
			String companyId = office.getId() + "";
			if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(companyId)) {
				TenantUtil.addCompany(userId, companyId);
			}
		}
		// 在officeService.save之前更新缓存会导致获取的logo是更新之前的logo
		Long logo = office.getLogo();
		String logoStr = "";
		if (logo != null) {
			logoStr = logo.toString();
		}
		if (null != office.getId() && CollectionUtils.isNotEmpty(oldList)) {
			if (!oldList.get(0).get("name").toString().equals(office.getName())
					|| (null == oldList.get(0).get("logo") && null != office.getLoginLogoPath())
					|| (null != oldList.get(0).get("logo") && !oldList.get(0).get("logo").toString().equals(logoStr))
					|| (null != oldList.get(0).get("logo") && null == logoStr)) {
				T1Utils.updateT1HomeCache();
			}
		}

		// 获取银行账户
		List<String> banks = officeService.getOfficePlatBankInfo(office.getId());
		if (null != banks && banks.size() > 0) {
			model.addAttribute("banks", banks);
		}
		model.addAttribute("id", office.getId());
		return "modules/sys/officePlatBankInfoForm";
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes, Model model) {
		if (Office.isRoot(id)) {
			addMessage(redirectAttributes, "删除批发商失败, 不允许删除顶级批发商或编号空");
		} else {
			List<Office> related = officeService.findRelatedOffices(id);
			Office root = officeService.get(id);
			List<Office> result = TreeNodeUtil.postIterationResult(related, root);
			Office errorDeleteOffice = null;
			for (Office office : result) {
				if (errorDeleteOffice == null) {
					try {
						officeService.delete(office.getId());
					} catch (Exception e) {
						errorDeleteOffice = office;
						LOG.error("批发商id：" + errorDeleteOffice.getId() + "删除失败", e);
					}
				} else {
					break;
				}

			}
			addMessage(redirectAttributes, "删除批发商成功");
		}
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) Long extId,
			@RequestParam(required = false) Long type, @RequestParam(required = false) Long grade,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// User user = UserUtils.getUser();
		List<Office> list = officeService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf(
					"," + extId + ",") == -1))) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", !user.isAdmin() &&
				// e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 保存第三步
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveOfficePlatBankInfoForm")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String saveOfficePlatBankInfoForm(HttpServletRequest request, Model model) {
		String[] belongTypes = request.getParameterValues("belongType");// 所属类型
		String[] defaultFlags = request.getParameterValues("realDefaultFlag");// 默认选择
		String[] accountNames = request.getParameterValues("accountName");// 账户名
		String[] bankNames = request.getParameterValues("bankName");// 开户行名称
		String[] bankAddrs = request.getParameterValues("bankAddr");// 银行地址
		String[] bankAccountCodes = request.getParameterValues("bankAccountCode");// 银行账户
		String[] rountings = request.getParameterValues("Rounting");
		String[] swiftNums = request.getParameterValues("swiftNum");
		String[] phoneNums = request.getParameterValues("phoneNum");
		String[] remarks = request.getParameterValues("remarks");
		
		String submit = request.getParameter("submit");

		String id = request.getParameter("id");
		model.addAttribute("id", id);

		// 获取belongType（1 境内账户 2 境外账户）
		String belongTypeStr = request.getParameter("belongType");
		model.addAttribute("belongTypeStr", belongTypeStr);

		if (StringUtils.isNotBlank(id)) {
			// 获取银行账户
			List<String> banks = officeService.getOfficePlatBankInfo(Long.parseLong(id));
			if (null != banks && banks.size() > 0) {
				// 删除现有银行账户
				officeService.deleteOfficePlatBankInfo(Long.parseLong(id));
			}

			for (int i = 0, m = 0; i < belongTypes.length; i++) {
				// 境内账户
				if ("1".equals(belongTypes[i])) {
					if (StringUtils.isNotBlank(accountNames[i]) && StringUtils.isNotBlank(bankNames[i])
							&& StringUtils.isNotBlank(bankAddrs[i]) && StringUtils.isNotBlank(bankAccountCodes[i])) {
						officeService.insertOfficePlatBankInfo(defaultFlags[i], accountNames[i], bankNames[i],
								bankAddrs[i], bankAccountCodes[i], remarks[i], Long.parseLong(id), belongTypes[i]);
					}
				}
				// 境外账户
				if ("2".equals(belongTypes[i])) {
					if (StringUtils.isNotBlank(accountNames[i]) && StringUtils.isNotBlank(bankNames[i])) {
						officeService.insertOfficePlatBankInfo(defaultFlags[i], accountNames[i], bankNames[i],
								bankAddrs[i], bankAccountCodes[i], remarks[i], Long.parseLong(id), rountings[m],
								swiftNums[m], phoneNums[m], belongTypes[i]);
						m++;
					}
				}

			}

			// 支付宝信息
			List<Map<String, Object>> zfbs = officeService.getOfficeZhifubaoInfo(Long.parseLong(id));
			if (null != zfbs && zfbs.size() > 0) {
				model.addAttribute("zfbs", zfbs);
			}
		}
		
		Office office = new Office();
		office.setId(1L);
		model.addAttribute("office", office);
		List<Office> list = Lists.newArrayList();
		List<Office> sourcelist = officeService.findAll();
		Office.sortList(list, sourcelist, office.getId());
		model.addAttribute("list", list);
//		model.addAttribute("office", list.get(0));
		
		if("提交".equals(submit)){
			return "modules/sys/officeList";
		}else{
			return "modules/sys/officeZhifubaoInfoForm";
		}
	}

	/**
	 * 第三步回显
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "officePlatBankInfoForm")
	public String officePlatBankInfoForm(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		model.addAttribute("id", id);
		if (StringUtils.isNotBlank(id)) {
			// 获取银行账户
			List<String> banks = officeService.getOfficePlatBankInfo(Long.parseLong(id));
			if (null != banks && banks.size() > 0) {
				model.addAttribute("banks", banks);
			}
		}
		return "modules/sys/officePlatBankInfoForm";
	}
	
	/**
	 * 支付宝账户页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "officeZhifubaoInfoForm")
	public String officeZhifubaoInfoForm(HttpServletRequest request, Model model) {
 		String id = request.getParameter("id");
		model.addAttribute("id", id);
		if (StringUtils.isNotBlank(id)) {
			// 获取支付宝账户
			List<Map<String, Object>> zfbs = officeService.getOfficeZhifubaoInfo(Long.parseLong(id));
			if (null != zfbs && zfbs.size() > 0) {
				model.addAttribute("zfbs", zfbs);
			}
		}
		return "modules/sys/officeZhifubaoInfoForm";
	}
	
	/**
	 * 保存支付宝
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveOfficeZhifubaoInfoForm")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String saveOfficeZhifubaoInfoForm(HttpServletRequest request, Model model) {
		String[] defaultFlags = request.getParameterValues("realDefaultFlag");// 默认选择
		String[] names = request.getParameterValues("name");// 支付宝名称
		String[] accounts = request.getParameterValues("account");// 支付宝账号
		String[] remarks = request.getParameterValues("remarks");
 
		String id = request.getParameter("id");
		model.addAttribute("id", id);

		if (StringUtils.isNotBlank(id)) {
			// 获取支付宝账户
			List<Map<String, Object>> banks = officeService.getOfficeZhifubaoInfo(Long.parseLong(id));
			if (null != banks && banks.size() > 0) {
				// 删除现有支付宝账户
				officeService.deleteOfficeZhifubaoInfo(Long.parseLong(id));
			}
			if (names != null && names.length > 0) {
				for (int i = 0; i < names.length; i++) {
					officeService.insertOfficeZhifubaoInfo(defaultFlags[i],names[i], accounts[i], remarks[i],Long.parseLong(id));
				}
			}
		}
		
		Office office = new Office();
		office.setId(1L);
		model.addAttribute("office", office);
		List<Office> list = Lists.newArrayList();
		List<Office> sourcelist = officeService.findAll();
		Office.sortList(list, sourcelist, office.getId());
		model.addAttribute("list", list);
		return "modules/sys/officeList";
	}
}
