package com.trekiz.admin.modules.groupCover.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.groupCover.entity.CoverResult;
import com.trekiz.admin.modules.groupCover.entity.GroupCover;
import com.trekiz.admin.modules.groupCover.service.GroupCoverService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value="${adminPath}/groupCover")
public class GroupCoverController {

	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private GroupCoverService groupCoverService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private QuauqAgentInfoService quauqAgentInfoService;
	@Autowired
	private UserDao userDao;
	
	/** 补位申请列表地址 */
	private static final String LIST_PAGE = "/modules/groupcover/groupCoverList";
	/** 补位申请地址 */
	private static final String GROUP_COVER_PAGE = "/modules/groupcover/groupCoverPage";
	/** 补位申请详情地址 */
	private static final String GROUP_COVER_INFO = "/modules/groupcover/groupCoverInfo";
	/** 补位列表 */
	private static final String COVER_ORDER_PAGE = "/modules/groupcover/groupCoverOrderList";
	
	/**
	 * @Description 补位申请列表
	 * @param groupId 团期id
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	@RequestMapping(value="list/{groupId}")
	public String list(@PathVariable Long groupId, Model model) {
		
		//补位申请记录查询
		List<GroupCover> groupCoverList = groupCoverService.findGroupCoverList(groupId);
		// 查询团期
		ActivityGroup group = activityGroupService.findById(groupId);
		
		//值传递
		model.addAttribute("group", group);
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupCoverList", groupCoverList);
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		model.addAttribute("isAddAgent", UserUtils.getUser().getCompany().getIsAllowAddAgentInfo());
		model.addAttribute("quauqAgentinfoList", quauqAgentInfoService.getAllQuauqAgentinfos());
		model.addAttribute("user", UserUtils.getUser());
		
		return LIST_PAGE;		
	}
	
	/**
	 * @Description 补位申请页面
	 * @param groupId 团期id
	 * @author yakun.bai
	 * @Date 2016-4-21
	 */
	@RequestMapping("/groupCoverPage/{groupId}")
	public String groupCoverPage(@PathVariable Long groupId, Model model) {
		
		// 查询团期、产品
		ActivityGroup productGroup = activityGroupService.findById(groupId);
		Integer groupCoverCount = groupCoverService.getAllGroupcoverNumOfgroupid(productGroup.getId());
		productGroup.setGroupcoverNum(groupCoverCount);
		TravelActivity product = travelActivityService.findById(productGroup.getSrcActivityId().longValue());
		
		// 值传递
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		
		return GROUP_COVER_PAGE;
	}
	
	/**
	 * @Description 补位申请
	 * @author yakun.bai
	 * @Date 2016-4-21
	 */
	@ResponseBody
	@RequestMapping(value="applyGroupCover")
	public Object applyGroupCover(HttpServletRequest request) {
		
		Map<String, String> result = Maps.newHashMap();
		result.put("result", "success");
		
		String groupId = request.getParameter("groupId");
		String coverPosition = request.getParameter("coverPosition");
		String remarks = request.getParameter("remarks");

		if (StringUtils.isBlank(groupId) || StringUtils.isBlank(coverPosition)) {
			if (StringUtils.isBlank(groupId)) {
				result.put("result", "error");
				result.put("error", "团期ID不能为空");
			}
			if (StringUtils.isBlank(coverPosition)) {
				result.put("result", "error");
				result.put("error", "补位人数不能为空");
			}
		} else {
			ActivityGroup productGroup = activityGroupService.findById(Long.parseLong(groupId));
			Integer freePosition = productGroup.getFreePosition();
			Integer groupCoverCount = groupCoverService.getAllGroupcoverNumOfgroupid(Long.parseLong(groupId));
			if (Integer.parseInt(coverPosition) <= freePosition && groupCoverCount <= 0){
				result.put("result", "error");  
				result.put("error", "余位为" + freePosition + ",可直接进行预订");
			}
			GroupCover groupCover = new GroupCover();
			productGroup.setCoverSerNum(productGroup.getCoverSerNum() + 1);
			groupCover.setCoverCode(productGroup.getGroupCode() + "-" + productGroup.getCoverSerNum());
			groupCover.setCoverStatus(Context.COVER_STATUS_DBW);
			groupCover.setActivityGroup(productGroup);
			groupCover.setCoverPosition(Integer.parseInt(coverPosition));
			groupCover.setRemarks(remarks);
			groupCoverService.save(groupCover);
			activityGroupService.save(productGroup);
		}
		
		return result;
	}
	
	/**
	 * 补位详情
	 * @param model
	 * @return
	 */
	@RequestMapping(value="groupCoverInfo/{groupCoverId}")
	public String groupCoverInfo(@PathVariable("groupCoverId") Long groupCoverId, Model model) {
		
		GroupCover groupCover = groupCoverService.getById(groupCoverId);
		
		// 查询团期、产品
		ActivityGroup productGroup = activityGroupService.findById(groupCover.getActivityGroup().getId());
		TravelActivity product = travelActivityService.findById(productGroup.getSrcActivityId().longValue());
		
		// 值传递
		model.addAttribute("groupCover", groupCover);
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		
		return GROUP_COVER_INFO;
	}
	
	/**
	 * @Description 确认补位申请
	 * @author yakun.bai
	 * @Date 2016-4-24
	 */
	@ResponseBody
	@RequestMapping("/confirmCover")
	public Map<String, Object> confirmCover(String coverId) {
		
		Map<String, Object> result = Maps.newHashMap();
		
		//确认补位申请
		CoverResult coverResult = groupCoverService.confirm(coverId);
		
		boolean flag = coverResult.getSuccess();
		if (flag) {
			result.put("result", "success");
			result.put("msg", coverResult.getMessage());
		} else {
			result.put("result", "faild");
			result.put("msg", coverResult.getMessage());
		}
		
		return result;
	}
	
	/**
	 * @Description 取消补位申请
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping("/cancelCover")
	public Map<String, Object> cancelCover(String coverId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = Maps.newHashMap();

		//取消补位申请
		CoverResult coverResult = groupCoverService.cancel(userId, companyId, "", coverId, "", null);

		boolean flag = coverResult.getSuccess();
		if (flag) {
			result.put("result", "success");
			result.put("msg", coverResult.getMessage());
		} else {
			result.put("result", "faild");
			result.put("msg", coverResult.getMessage());
		}

		return result;
	}
	
	/**
	 * @Description 取消补位申请
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping("/rejectCover")
	public Map<String, Object> rejectCover(String coverId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = Maps.newHashMap();

		//取消补位申请
		CoverResult coverResult = groupCoverService.reject(userId, companyId, "", coverId, "", null);

		boolean flag = coverResult.getSuccess();
		if (flag) {
			result.put("result", "success");
			result.put("msg", coverResult.getMessage());
		} else {
			result.put("result", "faild");
			result.put("msg", coverResult.getMessage());
		}

		return result;
	}
	
	/**
	 * @Description 补位列表
	 * @param group
	 * @param model
	 * @author pengfei.shang
	 * @Date 2016-4-20
	 */
	@RequestMapping(value="coverList")
	public String coverlist(Model model, HttpServletRequest request, HttpServletResponse response){
		
		// 查询条件
		Map<String, String> mapRequest = Maps.newHashMap();

		// 参数处理：去除空格和处理特殊字符并传递到后台
		// 参数：补位号或团号、补位状态、申请人、计调、申请提交时间、团期开始时间、团期结束时间
		String paras = "coverCodeOrGroupCode,coverStatus,createBy,activityCreate,createDateBegin,createDateEnd,"
				+ "groupOpenDateBegin,groupOpenDateEnd";
		OrderCommonUtil.handlePara(paras, mapRequest, model, request);
		String groupId = request.getParameter("groupId");
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		mapRequest.put("groupId", groupId);

		// 排序方式：默认按申请提交时间降序排序
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "gc.createDate DESC";
		}
		mapRequest.put("orderBy", orderBy);
		Page<Map<Object, Object>> page = new Page<>(request, response);
		Page<Map<Object, Object>> pageOrder = groupCoverService.findCoverList(page, mapRequest);
		List<Map<Object, Object>> listorder = pageOrder.getList();
		for (Map<Object, Object> listin : listorder) {
			listin.put("ifCanSeeOrderInfo", ifCanSeeOrderInfo(listin));
			if (listin.get("createUserName") != null) {
				listin.put("createUserName", UserUtils.getUser(StringUtils.toLong(listin.get("createUserName"))).getName());
			}
			if (listin.get("coverStatus") != null) {
				listin.put("coverStatusName", getCoverStatusName(Integer.parseInt(listin.get("coverStatus").toString())));
			}
		}
		model.addAttribute("page", pageOrder);
		model.addAttribute("companyUuid", companyUuid);
		model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("isNeedNoticeOrder", UserUtils.getUser().getCompany().getIsNeedAttention());
		model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType("2"));
		model.addAttribute("orderStatus", "2");
		model.addAttribute("orderOrGroup", "group");
		model.addAttribute("quauqAgentinfoList", quauqAgentInfoService.getAllQuauqAgentinfos());
		model.addAttribute("user", UserUtils.getUser());

		return COVER_ORDER_PAGE;
	}
	
	/**
	 * @Description 是否可查看订单详情
	 * @author yakun.bai
	 * @Date 2016-4-27
	 */
	private boolean ifCanSeeOrderInfo(Map<Object, Object> listin) {
		
		boolean flag = false;
		boolean isSale = false;
		boolean isSaleManager = false;
		Long saleDeptId = -1L;
		boolean op = false;
		boolean opManager = false;
		Long opDeptId = -1L;
		Long createBy = Long.parseLong(listin.get("createBy").toString());
		Long groupCreateBy = Long.parseLong(listin.get("createUserName").toString());
		
		//判断当前用户所拥有的角色类型
		User user = UserUtils.getUser();
        List<Role> roleList = user.getRoleListOrderByDept();
        
    	for (Role role : roleList) {
    		Department dept = role.getDepartment();
        	String type = role.getRoleType();
        	
        	if (Context.ROLE_TYPE_MANAGER.equals(type)) {
        		flag = true;
        		break;
        	}
        	if (Context.ROLE_TYPE_SALES.equals(type)) {
        		isSale = true; //是否是销售
        	}
        	if (Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
        		isSaleManager = true; //是否是销售经理
        		saleDeptId = dept.getId();
        	}
        	if (Context.ROLE_TYPE_OP.equals(type)) {
        		op = true; //是否是计调
        	}
        	if (Context.ROLE_TYPE_OP_EXECUTIVE.equals(type)) {
        		opManager = true; //是否是计调经理
        		opDeptId = dept.getId();
        	}
        }
		
    	// 如果不是系统管理员，则判断是否是计调主管下计调产品订单，或销售主管下销售申请记录，或是否是申请人
    	if (!flag) {
    		// 如果是申请人
    		if (isSale && createBy == user.getId()) {
    			return true;
    		}
    		// 如果是计调
    		if (op && groupCreateBy == user.getId()) {
    			return true;
    		}
    		
    		// 如果是计调主管
    		if (opManager) {
    			List<User> userList = userDao.getUserByDepartment(UserUtils.getUser().getCompany().getId(), opDeptId);
    			if (CollectionUtils.isNotEmpty(userList)) {
    				for (User temp : userList) {
    					if (groupCreateBy == temp.getId()) {
    						flag = true;
    						break;
    					}
    				}
    			}
    		}
    		
    		// 如果是销售主管，则可见
    		if (isSaleManager) {
    			List<User> userList = userDao.getUserByDepartment(UserUtils.getUser().getCompany().getId(), saleDeptId);
    			if (CollectionUtils.isNotEmpty(userList)) {
    				for (User temp : userList) {
    					if (createBy == temp.getId()) {
    						flag = true;
    						break;
    					}
    				}
    			}
    		}
    	}
		return flag;
	}
	
	/**
	 * @Description 补位状态中文名称
	 * @author yakun.bai
	 * @Date 2016-4-27
	 */
	private String getCoverStatusName(Integer coverStatus) {
		String coverStatusName = "待补位";
		if (coverStatus != null) {
			if (Context.COVER_STATUS_DBW == coverStatus) {
				coverStatusName = "待补位";
			} else if (Context.COVER_STATUS_YBW == coverStatus) {
				coverStatusName = "已补位";
			} else if (Context.COVER_STATUS_YBH == coverStatus) {
				coverStatusName = "已驳回";
			} else if (Context.COVER_STATUS_YQX == coverStatus) {
				coverStatusName = "已取消";
			} else if (Context.COVER_STATUS_SCDD == coverStatus) {
				coverStatusName = "生成订单";
			}
		} 
		return coverStatusName;
	}
	
	/**
	 * @author yang.jiang 2016-04-22 18:20:48 
	 * 获取补单的相关信息
	 */
    @ResponseBody
	@RequestMapping(value = "getCoverInfo")
	public Map<String, String> getCoverInfo(Model model, HttpServletRequest request) {
    	Map<String, String> resultMap = Maps.newHashMap();
		String coverId = request.getParameter("coverId");
		
		if (StringUtils.isBlank(coverId)) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "补位Id为空");
		}
		
		GroupCover groupCover = groupCoverService.getById(Long.parseLong(coverId));
		
		resultMap.put("groupId", groupCover.getActivityGroup().getId().toString());
		resultMap.put("activityId", groupCover.getActivityGroup().getSrcActivityId().toString());
		resultMap.put("groupCoverNum", groupCover.getCoverPosition().toString());
		return resultMap;
	}
    
    /**
     * @Description 获取团期待补位记录数
     * @author yakun.bai
     * @Date 2016-4-27
     */
    @ResponseBody
	@RequestMapping(value = "getGroupCoverInfo")
	public Map<String, String> getGroupCoverInfo(Model model, HttpServletRequest request) {
    	Map<String, String> resultMap = Maps.newHashMap();
		String groupId = request.getParameter("groupId");
		
		if (StringUtils.isBlank(groupId)) {
			resultMap.put("groupCoverNum", "0");
		} else {
			Integer groupCoverCount = groupCoverService.getAllGroupcoverNumOfgroupid(Long.parseLong(groupId));
			resultMap.put("groupCoverNum", groupCoverCount != null ? groupCoverCount.toString() : "0");
		}
		
		return resultMap;
	}
	
	
}