package com.trekiz.admin.agentToOffice.agentInfo.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.T2.utils.ExportExcelForAgentUtils;
import com.trekiz.admin.agentToOffice.agentInfo.service.CustomerTypeService;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.security.Base64Util;
import com.trekiz.admin.common.utils.PropertiesLoader;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.AgentInfoContacts;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.service.SupplyContactsService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.mobile.entity.MobileUser;
import com.trekiz.admin.modules.mobile.service.MobileUserService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * QUAUQ渠道商控制器
 * @author yang.jiang 2016-04-27 17:34:10
 *
 */
@Controller
@RequestMapping(value="${adminPath}/quauqAgent/manage")
public class QuauqAgentinfoController extends BaseController{

	@Autowired
	private QuauqAgentInfoService quauqAgentInfoService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SupplyContactsService supplyContactsService;
	@Autowired
	private AgentinfoDao agentInfoDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CustomerTypeService customerTypeService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private MobileUserService mobileUserService;
	/**
	 * 获取当前用户的信息
	 * @return
	 */
	private User getUserInfo(){
		return UserUtils.getUser();
	}
	/**
	 * 从缓存中获取上下级的信息，并把他存入model
	 * @param model
	 * @time 2016-11-24
	 */
	private void getagentParentList(Model model){
		List<Map<String,String>> getagentParentListFromCache = AgentInfoUtils.getagentParentListFromCache();
		model.addAttribute("agentParentList", getagentParentListFromCache);
	}
	/**
	 * 从缓存中获取类型下拉框的信息
	 * @time 2016-11-24
	 */
	private void getTypeProperty(Model model){
		List<Map<String, Object>> customerTypeList = AgentInfoUtils.getTypePropertyFromCache();
		model.addAttribute("customerTypeList", customerTypeList);
	}
	/**
	 * 获取渠道商所属区域的下列表
	 * @param model
	 * @param areaId
	 * @param key
	 */
	@RequiresPermissions("quauqAgent:manage:view")
	@RequestMapping(value={"list",""})
	public String list(@ModelAttribute Agentinfo agentinfo, HttpServletRequest request, HttpServletResponse response,Model model){
		Map<String, Object> paramMap = new HashMap<>();
		/*String isBound = request.getParameter("isBound");
		String accountFrom = request.getParameter("accountForm");*/
		paramMap.put("isBound", agentinfo.getIsBound());	//是否绑定微信账号	全部，已关联,未关联
		paramMap.put("accountFrom", agentinfo.getAccountFrom());	//账号来源 ： 全部：0，内部：1，微信：2
		paramMap.put("agentName", agentinfo.getAgentName());
		paramMap.put("loginName", agentinfo.getLoginName());
		paramMap.put("contactName", agentinfo.getContactName());
		paramMap.put("agentType", agentinfo.getAgentType());
		paramMap.put("agentParent", agentinfo.getAgentParent().equals("无") ? -1 :agentinfo.getAgentParent());
		Page<Map<Object, Object>> page = quauqAgentInfoService.findQuauqAgentList(new Page<Map<Object, Object>>(request, response), paramMap);
		
		model.addAttribute("agentinfo", agentinfo);
        model.addAttribute("page", page);
        model.addAttribute("useruuid",UserUtils.getUser().getId());
        // 类型下拉列表
        List<Map<String, Object>> customerTypeList = customerTypeService.getCustomerTypeList4Select();
        model.addAttribute("customerTypeList", customerTypeList);
		return "agentToOffice/quauqAgent/quauqAgentList";
	}
	
	
	/**
	 * 根据渠道姓名搜索未绑定的T1账号
	 * @param agentinfo  
	 */
	@RequiresPermissions("quauqAgent:manage:view")
	@RequestMapping(value={"notBoundList",""},method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> notBoundList(@ModelAttribute Agentinfo agentinfo){
		Map<String,Object> map = new HashMap<String,Object>();
		/*if(agentinfo == null){
			map.put("result", false);
			map.put("msg", "请求失败，请求参数填写错误！");
		}else{*/
			Map<String, Object> paramMap = new HashMap<>();
			if(agentinfo != null && agentinfo.getAgentName() != null){
				try {
					paramMap.put("agentName", URLDecoder.decode(agentinfo.getAgentName(),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			paramMap.put("enableQuauqAgent", "1");  去掉是否启用筛选条件
			paramMap.put("mobileUserId", false);
			List<Map<Object, Object>> list = quauqAgentInfoService.findAgentNotBoundList(paramMap);
			map.put("result", true);
			map.put("msg", "请求成功！");
			map.put("list", list);
//		}
		return map;
	}
	
	/**
	 * 根据微信账号ID找到匹配的T1用户信息
	 * @param agentinfo  
	 */
	@RequiresPermissions("quauqAgent:manage:view")
	@RequestMapping(value={"getMatchedAccount",""},method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getMatchedAccount(@RequestParam("mobileUserId") Long mobileUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		if(mobileUserId == null){
			map.put("result", false);
			map.put("msg", "请求失败，请求参数填写错误！");
		}else{
			MobileUser user = mobileUserService.getEntity(mobileUserId);
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("telephone", user.getTelePhone());
			paramMap.put("name", user.getName());
			paramMap.put("wechatCode", user.getWechatCode());
			paramMap.put("mobileUserId", user.getId());
			paramMap.put("agentName", user.getAgentName());
			paramMap.put("phone", user.getPhone());
			paramMap.put("areaCode", user.getAreaCode());
			List<Map<String,Object>> t1UserList =mobileUserService.singleMatch(paramMap);
			map.put("result", true);
			map.put("msg", "请求成功！");
			map.put("mobileUserMsg", paramMap);
			map.put("t1UserList", t1UserList);
		}
		return map;
	}
	
	
	
	/**
	 * 根据渠道Id查询渠道商公司地址
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getAgentArea(String id){
		
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		String country = "";
		String city1 = "";
		String city2 = "";
		//根据公司地址所属国家id查省
		if(StringUtils.isNotBlank(ai.getAgentAddress())){
			country = areaService.findNameById(Long.parseLong(ai.getAgentAddress()));
		}
		//根据公司地址所属国家id查市
		if(ai.getAgentAddressProvince()!=null){
			city1 = areaService.findNameById(ai.getAgentAddressProvince());
		}
		//获取公司地址所在城市
		if(ai.getAgentAddressCity() != null){
			city2 = areaService.findNameById(ai.getAgentAddressCity());
		}
		String agentAddressStreet = ai.getAgentAddressStreet();
		return country+" "+city1+" "+city2+" "+agentAddressStreet;
	}
	/**
	 * 564导出渠道商信息表
	 * @param agentinfo
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="exportAgent")
	public void exportAgent(@ModelAttribute Agentinfo agentinfo, HttpServletRequest request, HttpServletResponse response,Model model){
		//获取查询参数
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("agentName", agentinfo.getAgentName());
		paramMap.put("loginName", agentinfo.getLoginName());
		paramMap.put("contactName", agentinfo.getContactName());
		paramMap.put("agentType", agentinfo.getAgentType());
		//paramMap.put("agentParent", agentinfo.getAgentParent());
		paramMap.put("agentParent", agentinfo.getAgentParent().equals("无") ? -1 :agentinfo.getAgentParent());
		//获取查询结果
		Page<Map<Object, Object>> page = quauqAgentInfoService.findQuauqAgentList(new Page<Map<Object, Object>>(request, response,10000), paramMap);
		List<Map<Object, Object>> agentList = page.getList();
		//封装查询结果
		List<Object[]> resultList = new ArrayList<Object[]>();
		int i = 0;
		for(Map<Object, Object> agent :agentList){
			Object agentId = agent.get("agentId");
			String agentArea = getAgentArea(agentId.toString());
			i++;
			Object[] result = new Object[11];
			result[0] = i;	//序号
			result[1] = agent.get("agentName");	//渠道名称
			result[2] = agent.get("abbreviation") == null ? " " : agent.get("abbreviation");	//简称
			result[3] = agent.get("agentBrand")== null ? " " : agent.get("agentBrand");	//渠道品牌
			result[4] = agent.get("loginName")== null ? " " : agent.get("loginName"); //登入名
			result[5] = agent.get("agentType")== null ? " " : agent.get("agentType");	//类型
			result[6] = agent.get("agentParent").toString().equals("-1") ? "无" :agent.get("agentParent");	//关系
			result[7] = agent.get("contactName")== null ? " " : agent.get("contactName");	//联系人姓名
			result[8] = agent.get("contactMobile")== null ? " " : agent.get("contactMobile");	//联系人电话
			/*result[9] = agent.get("agentAddressStreet")== null ? " " : agent.get("agentAddressStreet");	//公司地址*/
			result[9] = agentArea;
			result[10] = Integer.valueOf(agent.get("enableQuauqAgent").toString()) == 1 ? "启用": " ";	//状态
			
			resultList.add(result);
		}
		//文件名称
		String fileName = "渠道商账号信息表";
		//表头数据
		String[] secondTitle = new String[]{"序号","渠道名称","简称","渠道品牌","登入名","类型","关系","联系人姓名","联系人电话","公司地址","状态"};
		try {
			ExportExcelForAgentUtils.createExcel( fileName, resultList,secondTitle, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static PropertiesLoader propertiesLoader;

	/**
	 * 添加quauq渠道账号
	 * @param model
	 * @return
	 */
	@RequestMapping(value="firstForm", method=RequestMethod.GET)
	public String firstForm(Model model, HttpServletRequest request){
		// 微信账号列表，生成t1新账号操作，传入mobileUserId
		String mobileUserId = request.getParameter("mobileUserId");
		if(StringUtils.isNotBlank(mobileUserId)){
			MobileUser mobileUser = mobileUserService.getEntity(Long.parseLong(mobileUserId));
			model.addAttribute("mobileUser", mobileUser);
		}

		// 判断是不是新添加quauq渠道账号，如果是则生成新的密码

		model.addAttribute("isNewUser","Y");//为新增用户
		// 当前登录的角色ID
		model.addAttribute("userId", getUserInfo().toString());
		//1.获取当前登录人根据当前登录人信息获取公司信息;根据公司信息获取该公司下的角色是销售的人员
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(getUserInfo().getCompany().getId()));
		//2.获取国家信息
		Map<String, String> map = agentinfoService.findCountryInfo();
		Set<String> set = map.keySet();
		String key = "";
		//将中国放到国家后面（因此将中国从map中拿出）
		for(String str : set){
			if(map.get(str).equals("中国")){
				model.addAttribute("countryId", str);
				key = str;
			}
		}
		//移掉中国
		map.remove(key);
		model.addAttribute("countryName","中国");
		model.addAttribute("areaMap",map);
		List<Object[]> list = agentinfoService.findAllPaymentType();
		//结款方式
		model.addAttribute("paymentMap",list );
		//读取项目配置文件
		propertiesLoader = new PropertiesLoader("application.properties");
		//获取mtourCompanyUuid值
		
		if(null != propertiesLoader.getProperties().get("mtourCompanyUuid")){
			String mtourCompanyUuid =propertiesLoader.getProperties().get("mtourCompanyUuid").toString();
			//简称信息显示状态（美途国际显示）
			if(mtourCompanyUuid.indexOf(UserUtils.getUser().getCompany().getUuid())!=-1){
				model.addAttribute("agentNameShortFlag",1);
			}
		}
		//上级关系下拉框数据
		getagentParentList(model);
		//类型数据
		getTypeProperty(model);
		return "agentToOffice/quauqAgent/quauqFirstForm";
	}
	
	/**
	 * quauq渠道账号保存（渠道第一步保存）
	 * @param agentInfo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("saveFirstForm")
	public Object saveFirstForm(AgentInfoContacts agentInfoContacts, Model model){
		//由于不同的批发商对某一个quauq渠道会有不同的人员，所以跟进销售不同，到时候指定分配报名权限的账号作为跟进销售
		Agentinfo ai = agentInfoContacts.getAgentinfo();
		Long quauqCompanyId = UserUtils.getUser().getCompany().getId();  // 必须要求是quauq公司  TODO
		ai.setSupplyId(quauqCompanyId);
		ai.setIsQuauqAgent(Context.QUAUQ_AGENT_YES);
		ai.setEnableQuauqAgent(Context.QUAUQ_AGENT_DISABLED);
		ai.setStatus("1");  // 此处对于quauq渠道来说已经算添加完
		
		if (!"1".equals(ai.getAgentType())) {
			ai.setAgentParent("-1");
		}
		
		String agentAddress = ai.getAgentAddress();
		//  agentAddress地址必须是Long类型
		//bug17033 注掉 chao.zhang
//		if(!MoneyNumberFormat.isNumber(agentAddress)){
//			//TODO
//			return "";
//		}

		Agentinfo agentInfo = agentinfoService.save(ai);
		if(agentInfoContacts.getContacts()!=null){
			List<SupplyContacts> supplyContactsList =  agentInfoContacts.getContacts().getSupplyContactses();
			for (Iterator<SupplyContacts> ite = supplyContactsList.iterator(); ite.hasNext();) {
				SupplyContacts supplyContacts = (SupplyContacts) ite.next();
				if(StringUtils.isBlank(supplyContacts.getContactName())
						&& StringUtils.isBlank(supplyContacts.getContactEmail())
						&& StringUtils.isBlank(supplyContacts.getContactFax())
						&& StringUtils.isBlank(supplyContacts.getContactMobile())
						&& StringUtils.isBlank(supplyContacts.getContactPhone())
						&& StringUtils.isBlank(supplyContacts.getContactQQ())){
					ite.remove();
					continue;
				}
				supplyContacts.setSupplierId(agentInfo.getId());
				supplyContacts.setType("0");
				supplyContactsService.save(supplyContacts);
			}
		}
		//保存渠道ID
		model.addAttribute("agentId", agentInfo.getId());
		// 创建quauq渠道登录账号
		List<User> agentUsers =  systemService.getUserByCompanyAndAgent(quauqCompanyId, agentInfo.getId());
		User quauqAgentUser = new User();
		if (CollectionUtils.isNotEmpty(agentUsers)) {
			quauqAgentUser = agentUsers.get(0);
		}
		// 组织用户所需信息
		Map<String, Object> userInfoParam = quauqAgentInfoService.handleUserInfo(quauqAgentUser, agentInfo, quauqCompanyId, agentInfoContacts);
		// 保存用户		
		Map<String, Object> userSaveResult = systemService.saveUserInfo(quauqAgentUser, userInfoParam);
		if(userSaveResult.get("mobileUserId") != null && userSaveResult.get("userId") != null){
			// 账号绑定成功后，数据迁移
			mobileUserService.copyWechatToT1(Long.parseLong(userSaveResult.get("mobileUserId").toString()),Long.parseLong(userSaveResult.get("userId").toString()));
		}

		// 更新缓存
		if(!ai.getAgentType().equals("1")){
			AgentInfoUtils.updateAgentParentListCache();
		}
		// 怎么更好的处理错误信息 TODO
		if (!"success".equals(userSaveResult.get("flag"))) {
			return userSaveResult.get("message").toString();
		}

		Map<String, Object> result = new HashMap<>();
		if (agentInfoContacts.getMobileUserId() != null){// 微信账号列表生成新账号的跳转
			result.put("result", true);
			result.put("url", "/mobileUser/mobileUserPage");
		} else {
			result.put("url", "/quauqAgent/manage/list");
		}
		return result;
	}
	
	/**
	 * 导出密码
	 * @param request
	 * @param response
	 */
    @RequestMapping(value="downloadPwd", method=RequestMethod.POST)
	public void downloadPwd(HttpServletRequest request, HttpServletResponse response) {
		List<User> userList = quauqAgentInfoService.getAllQuauqAgentLoginUser();
		downloadAll(userList, request, response);
	}
    
    /**
     * 下载密码文件
     * @param userList
     * @param request
     * @param response
     */
    private void downloadAll(List<User> userList, HttpServletRequest request, HttpServletResponse response) {
    	// 要导出数据
    	List<Object[]> exportUserList = new ArrayList<Object[]>();
    	if (CollectionUtils.isNotEmpty(userList)) {
    		for (User user : userList) {
    			if (user.getCompany() != null) {
    				Object[] temp = new Object[6];
//        			temp[0] = user.getCompany().getName();
        			temp[0] = user.getName();
        			temp[1] = user.getLoginName();
//        			temp[2] = user.getPassword();
        			temp[2] = Base64Util.getFromBase64(user.getTwo_psw());
//        			temp[5] = DateUtils.date2String(new Date());
        			exportUserList.add(temp);
    			}
    		}
    	}
		//文件名称
		String fileName = "用户信息表";
		//Excel各行名称
		String[] cellTitle =  {"渠道名称","登录名","密码"};
		//文件首行标题
		String firstTitle = "用户信息表";
		try {
			ExportExcel.createExcle(fileName, exportUserList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 删除quauq渠道
     * @param agentid
     * @return
     */
	@RequestMapping(value="del/{agentid}")
	public String delelteQuauqAgent(@PathVariable String agentid){
		quauqAgentInfoService.deleteQuauqAgent(Long.parseLong(agentid));
		return "forward:"+Global.getAdminPath()+"/quauqAgent/manage/list";
	}
	
	/**
	 * quauq渠道详情
	 * @param agentinfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/agentdetail/{id}", method=RequestMethod.GET)
	public String agentdetail(@PathVariable("id") String id, Model model){
		//根据ID把渠道商查出来
		if (StringUtils.isBlank(id)) {
			return null;
		}
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		//把渠道商的联系人查出来
		List<SupplyContacts> contactsList = supplyContactsService.findContactsByAgentInfo(Long.parseLong(id));
		//根据所属国家ID查省
		if(ai.getBelongsArea()!=null){
			agentinfoService.addAreaToModel(model,ai.getBelongsArea(),"belongsProvinceMap");
		}
		//根据所属省查城市信息
		if(ai.getBelongsAreaProvince()!=null){
			agentinfoService.addAreaToModel(model,ai.getBelongsAreaProvince(),"belongsCityMap");
		}
		//根据公司地址所属国家id查省
		if(StringUtils.isNotBlank(ai.getAgentAddress())){
			agentinfoService.addAreaToModel(model,Long.parseLong(ai.getAgentAddress()),"addressProvinceMap");
		}
		//根据公司地址所属国家id查市
		if(ai.getAgentAddressProvince()!=null){
			agentinfoService.addAreaToModel(model,ai.getAgentAddressProvince(),"addressCityMap");
		}
		//
		if (StringUtils.isBlank(ai.getLoginName()) || ai.getLoginId() == null) {
			List<User> users = userDao.findByQuauqAgentIdWithoutDelflag(ai.getId());
			if (CollectionUtils.isNotEmpty(users)) {
				ai.setLoginName(users.get(0).getLoginName());
				ai.setLoginId(users.get(0).getId());
			}
		}
		model.addAttribute("agentinfo",ai);
		model.addAttribute("isDetail", true);
		model.addAttribute("supplyContactsList", contactsList);
		// 获取国家信息
		model.addAttribute("areaMap", agentinfoService.findCountryInfo());
		model.addAttribute("agentId", id);
		return "agentToOffice/quauqAgent/modifyBaseForm";
	}
	
	/**
	 * 页面数据回显(修改操作准备数据)
	 * @return
	 */
	@RequestMapping(value="/modifyBaseForm/{id}", method=RequestMethod.GET)
	public String modifyBaseForm(@PathVariable("id") String id, Model model){
		//根据ID把渠道商查出来
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		//把渠道商的联系人查出来
		List<SupplyContacts> contactsList = supplyContactsService.findContactsByAgentInfo(Long.parseLong(id));
		//根据所属国家ID查省
		if(ai.getBelongsArea()!=null){
			agentinfoService.addAreaToModel(model,ai.getBelongsArea(),"belongsProvinceMap");
		}
		//根据所属省查城市信息
		if(ai.getBelongsAreaProvince()!=null){
			agentinfoService.addAreaToModel(model,ai.getBelongsAreaProvince(),"belongsCityMap");
		}
		//根据公司地址所属国家id查省
		if(StringUtils.isNotBlank(ai.getAgentAddress())){
			agentinfoService.addAreaToModel(model,Long.parseLong(ai.getAgentAddress()),"addressProvinceMap");
		}
		//根据公司地址所属国家id查市
		if(ai.getAgentAddressProvince()!=null){
			//getAreaList(model,ai.getAgentAddressProvince(),"addressCityMap");
			agentinfoService.addAreaToModel(model,ai.getAgentAddressProvince(),"addressCityMap");
		}
		//
		if (StringUtils.isBlank(ai.getLoginName()) || ai.getLoginId() == null) {
			List<User> users = userDao.findByQuauqAgentIdWithoutDelflag(ai.getId());
			if (CollectionUtils.isNotEmpty(users)) {
				ai.setLoginName(users.get(0).getLoginName());
				ai.setLoginId(users.get(0).getId());
				model.addAttribute("differenceRights", users.get(0).getDifferenceRights());
				/** 临时策略（待删除） */
				model.addAttribute("lingxianwangshuai", users.get(0).getLingxianwangshuai());
			}
		}
		model.addAttribute("agentinfo",ai);
		model.addAttribute("isDetail", false);
		model.addAttribute("supplyContactsList", contactsList);
		// 获取国家信息
		model.addAttribute("areaMap", agentinfoService.findCountryInfo());
		model.addAttribute("agentId", id);
		
		//上级关系下拉框数据
		getagentParentList(model);
		
		//客户类型下拉框数据
		getTypeProperty(model);
		
		//客户上级关系
		if (ai.getAgentParent() != null && !"-1".equals(ai.getAgentParent())) {
			Agentinfo agentParent = agentInfoDao.findOne(Long.valueOf(ai.getAgentParent()));
			if (agentParent != null) {
				model.addAttribute("agentParent", agentParent.getAgentName());
			} else {
				model.addAttribute("agentParent", "无");
			}
		} else {
			model.addAttribute("agentParent", "无");
		}
		
		return "agentToOffice/quauqAgent/modifyBaseForm";
	}

	/**
	 * 更新(修改保存)基本信息
	 * @param agentInfoContacts
	 * @return
	 */
	@RequestMapping("updateBase")
	public String updateBase(AgentInfoContacts agentInfoContacts){
		//  前台修改的渠道信息
		Agentinfo ai = agentInfoContacts.getAgentinfo();
		//  取自数据库中原有渠道信息
		Agentinfo ageninfoDB = agentinfoService.findOne(ai.getId());
		//  设置变化值
		ageninfoDB.setAgentName(ai.getAgentName());  // 渠道名称
		ageninfoDB.setAbbreviation(ai.getAbbreviation());	//渠道简称
		ageninfoDB.setAgentBrand(ai.getAgentBrand());  // 渠道品牌
		ageninfoDB.setAgentFirstLetter(ai.getAgentFirstLetter());  // 渠道首字母
		ageninfoDB.setBelongsArea(ai.getBelongsArea());  // 渠道所属区域
		ageninfoDB.setBelongsAreaProvince(ai.getBelongsAreaProvince());  // 渠道所属省份
		ageninfoDB.setBelongsAreaCity(ai.getBelongsAreaCity());  // 渠道所属城市
		ageninfoDB.setAgentAddress(ai.getAgentAddress());  // 渠道公司所在区域
		ageninfoDB.setAgentAddressProvince(ai.getAgentAddressProvince());  // 渠道公司所在省份
		ageninfoDB.setAgentAddressCity(ai.getAgentAddressCity());  // 渠道公司所在城市
		ageninfoDB.setAgentAddressStreet(ai.getAgentAddressStreet());  // 渠道公司所在街区详细地址
		ageninfoDB.setAgentType(ai.getAgentType());// 渠道类型
//		if (!"1".equals(ai.getAgentType())) {
//			ageninfoDB.setAgentParent("-1");// 渠道上级关系
//		} else {
//			ageninfoDB.setAgentParent(ai.getAgentParent());// 渠道上级关系
//		}
		//  保存渠道
		Agentinfo agentInfo = agentInfoDao.save(ageninfoDB);
		// 登录账号
		User userDB = userDao.findById(agentInfoContacts.getLoginId());
		Integer differenceRights = agentInfoContacts.getDifferenceRights();
		if(differenceRights != null){
			userDB.setDifferenceRights(differenceRights);
		}else{
			userDB.setDifferenceRights(0);
		}
		Integer lingxianwangshuai = agentInfoContacts.getLingxianwangshuai();
		if(lingxianwangshuai != null){
			userDB.setLingxianwangshuai(lingxianwangshuai);
		}else{
			userDB.setLingxianwangshuai(0);
		}
		userDB.setName(agentInfo.getAgentName());
		userDB = userDao.save(userDB);  // 保存登录账号
		//  处理联系人
		supplyContactsService.updateAgentContactAI(agentInfo, agentInfoContacts.getContacts().getSupplyContactses());  // 智能更新联系人（自动判断并新增、删除、更新）
		if (!agentInfo.getAgentType().equals("1")) {
			AgentInfoUtils.updateAgentParentListCache();
		}
		
		return "forward:"+Global.getAdminPath()+"/quauqAgent/manage/list";
	}
	
	/**
	 * @Description 渠道统计查询
	 * @author yakun.bai
	 * @Date 2016-5-5
	 */
	@RequestMapping(value ="quauqAgentStatistics")
	public String quauqAgentStatistics(@ModelAttribute Agentinfo agentinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		// 查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        // 参数处理：渠道名称、订单状态、创建开始日期、创建结束日期、订单编号、团号、批发商
        String paras = "agentId,orderStatus,orderTimeBegin,orderTimeEnd,orderNum,groupCode,companyId";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
		
		// 查询渠道统计信息
		Page<Map<Object, Object>> pageAgent = quauqAgentInfoService.quauqAgentStatistics(new Page<Map<Object, Object>>(request, response), mapRequest);
		// 内部销售人员的名单
		model.addAttribute("agentinfo", agentinfo);
		model.addAttribute("agentList", agentinfoService.newGetAllAgent());
		model.addAttribute("officeList", officeService.findByShelfRightsStatus(0));
		model.addAttribute("page", pageAgent);
		return "agentToOffice/agentInfo/quauqAgentStatistics";
	}
	
	/**
	 * @Description 导出所有渠道订单信息
	 * @author yakun.bai
	 * @Date 2016-5-5
	 */
	@RequestMapping(value={"downloadAllOrder"})
	public void downloadAllOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		
		// 查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        // 参数处理：渠道名称、订单状态、创建开始日期、创建结束日期、订单编号、团号、批发商
        String paras = "agentId,orderStatus,orderTimeBegin,orderTimeEnd,orderNum,groupCode,companyId";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
		
		// 查询渠道统计信息
        List<Map<Object, Object>> orders = quauqAgentInfoService.downloadAllOrder(mapRequest);
		
        // 要导出订单信息
		List<Object[]> orderList = Lists.newArrayList();
		
		// 获取订单状态并封装成map
		List<Dict> dictList = DictUtils.getDictList("order_pay_status");
		Map<String, String> dictMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(dictList)) {
			for (Dict dict : dictList) {
				dictMap.put(dict.getValue(), dict.getLabel());
			}
		}
		try {
			if (CollectionUtils.isNotEmpty(orders) && MapUtils.isNotEmpty(dictMap)) {
				int i = 0;
				for (Map<Object, Object> order : orders) {
					i++;
					Object [] obj = new Object[18];
					// 序号
					obj[0] = i;
					// 渠道名称
					obj[1] = order.get("agentName");
					// 下单日期
					if (order.get("createDate") != null) {
						String orderTime = order.get("createDate").toString();
						obj[2] = orderTime.substring(0, orderTime.length() - 2);
					} else {
						obj[2] = "";
					}
					// 订单号
					obj[3] = order.get("orderNum");
					// 团号
					obj[4] = order.get("groupCode");
					// 订单人数
					obj[5] = order.get("orderPersonNum");
					// 订单结算价
					obj[6] = order.get("totalMoney");
					// QUAUQ结算价
					obj[7] = order.get("quauqTotalMoney");
					// 服务费
					obj[8] = order.get("quauqServiceCharge");
					// 订单状态
					obj[9] = dictMap.get(order.get("orderStatus").toString());
					// 供应商
					obj[10] = order.get("officeName");
					orderList.add(obj);
				}
			}
			
			//文件名称
			String fileName = "渠道订单统计";
			//Excel各行名称
			String[] cellTitle =  {"序号","渠道名称","下单日期","订单号","团号","订单人数","订单结算价","QUAUQ结算价","服务费","订单状态","供应商"};
			//文件首行标题
			String firstTitle = "渠道订单统计";
			ExportExcel.createExcle(fileName, orderList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			logger.error("下载出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 启用禁用渠道
	 * @author yang.jiang 2016-5-6
	 * @param request
	 * @return
	 */
	@RequestMapping(value="changeEnableStatus")
	public String changeEnableStatus(HttpServletRequest request) {
		String agentId = request.getParameter("agentId");
		String checkStatus = request.getParameter("checkStatus");
		
		if (StringUtils.isBlank(agentId)) {
			return "渠道信息为空！";
		}
		if (StringUtils.isBlank(checkStatus)) {
			return "状态信息为空！";
		}
		try {			
			Agentinfo agentinfo = agentInfoDao.findOne(Long.parseLong(agentId));
			if (agentinfo == null) {
				return "未获得渠道信息！";
			}
			if (Context.QUAUQ_AGENT_ENABLED.equals(checkStatus)) {
				agentinfo.setEnableQuauqAgent(Context.QUAUQ_AGENT_ENABLED);
			} else if (Context.QUAUQ_AGENT_DISABLED.equals(checkStatus)) {
				agentinfo.setEnableQuauqAgent(Context.QUAUQ_AGENT_DISABLED);
			} else {
				return "状态信息异常！";
			}
			agentInfoDao.save(agentinfo);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return null;
	}
	
}
