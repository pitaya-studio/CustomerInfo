package com.trekiz.admin.modules.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.IRemindService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 提醒规则controller
 * @author yunpeng.zhang
 *
 */
@Controller
@RequestMapping("${adminPath}/sys/remind")
public class RemindController {
    @Autowired
    private IRemindService remindService;
    @Autowired
    private IActivityGroupService activityGroupService;
    @Autowired
    private ITravelActivityService travelActivityService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DepartmentService departmentService;

    private static final Logger logger = LoggerFactory.getLogger(RemindController.class);

    @ModelAttribute("/remind")
    public void getRemind(@RequestParam(required = false) Integer id, Model model) {
        User user = UserUtils.getUser();
        String companyUuid = UserUtils.getCompanyUuid();
        if (id != null) {
            Remind remind = remindService.get(id);
            remind.setUpdateBy(user);
            remind.setUpdateDate(new Date());
            model.addAttribute("remind", remind);
        } else {
            Remind remind = new Remind();
            remind.setStartRemindStatus(-1);
            remind.setStartRemindDays(10);
            remind.setEndRemindStatus(-1);
            remind.setEndRemindDays(1);
            remind.setIsVisible4Reviewer(0);  // 默认不选中
            remind.setCompanyUuid(companyUuid);
            remind.setCreateBy(user);
            remind.setCreateDate(new Date());
            remind.setUpdateBy(user);
            remind.setUpdateDate(new Date());
            model.addAttribute("remind", remind);
        }
    }

    @ResponseBody
    @RequestMapping("/del")
    public Map<String, Object> del(Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        if (id != null) {
            try {
                remindService.delRemind(id);
                resultMap.put("result", 1);
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("result", 2);
                logger.error("删除提醒失败！");
            }
        }

        return resultMap;
    }

    @RequestMapping("/save")
    public String save(Remind remind, Map<String, Object> map, RedirectAttributes redirectAttributes) {
        Integer id = remind.getId();
        try {
            remindService.save(remind);
            if (id != null) {
                redirectAttributes.addFlashAttribute("message", "修改提醒成功！");
            } else {
                redirectAttributes.addFlashAttribute("message", "保存提醒成功！");
            }
        } catch (Exception e) {
            if (id != null) {
                logger.error("修改提醒失败！");
                redirectAttributes.addFlashAttribute("message", "修改提醒失败！");
            } else {
                logger.error("保存提醒失败！");
                redirectAttributes.addFlashAttribute("message", "保存提醒失败！");
            }
            e.printStackTrace();
        }
        return "redirect:"+ Global.getAdminPath() +"/sys/remind/list";
    }

    /**
     * 新增、修改
     * @param remind
     * @param map
     * @return
     */
    @RequestMapping("/form")
    public String form(Remind remind, Map<String, Object> map) {
        map.put("remind", remind);
        map.put("id", remind.getId());
        // 处理团期信息
        try {
//            handleActivityGroups(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取团期信息出错！");
        }
        return "modules/sys/remind/remindForm";
    }
    
    /**
     * 详情
     * @param remind
     * @param map
     * @return
     */
    @RequestMapping("/info")
    public String info(Remind remind, Map<String, Object> map) {
        map.put("remind", remind);
        map.put("id", remind.getId());
//        map.put("deptId",UserUtils.getUser().getCompany().getId());
        // 处理团期信息
        try {
//            handleActivityGroups(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取团期信息出错！");
        }
        return "modules/sys/remind/remindInfo";
    }

    /**
     * 获取各个产品类型下对应的团期信息
     */
    private void handleActivityGroups(Map<String, Object> map) throws Exception {
        List<ActivityGroup> singleGroupList = null;
        List<ActivityGroup> scatteredGroupList = null;
        List<ActivityGroup> studyGroupList = null;
        List<ActivityGroup> bigCustomerGroupList = null;
        List<ActivityGroup> freeWalkGroupList = null;
        try {
            singleGroupList = remindService.getActivityGroupsByActivityKind(Integer.parseInt(Context.ACTIVITY_KINDS_DT));
            scatteredGroupList = remindService.getActivityGroupsByActivityKind(Integer.parseInt(Context.ACTIVITY_KINDS_SP));
            studyGroupList = remindService.getActivityGroupsByActivityKind(Integer.parseInt(Context.ACTIVITY_KINDS_YX));
            bigCustomerGroupList = remindService.getActivityGroupsByActivityKind(Integer.parseInt(Context.ACTIVITY_KINDS_DKH));
            freeWalkGroupList = remindService.getActivityGroupsByActivityKind(Integer.parseInt(Context.ACTIVITY_KINDS_ZYX));
        } catch (Exception e) {
            throw new Exception(e);
        }
        map.put(Context.ACTIVITY_KINDS_DT, singleGroupList);
        map.put(Context.ACTIVITY_KINDS_SP, scatteredGroupList);
        map.put(Context.ACTIVITY_KINDS_YX, studyGroupList);
        map.put(Context.ACTIVITY_KINDS_DKH, bigCustomerGroupList);
        map.put(Context.ACTIVITY_KINDS_ZYX, freeWalkGroupList);
    }

    /**
     * 提醒规则列表
     * @param map
     * @return
     */
    @RequestMapping(value = {"/list", ""})
    public String list(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("remindType", request.getParameter("remindType"));
        parameters.put("remindName", request.getParameter("remindName"));
        Page<Map<Object, Object>> page = remindService.findRemindList(parameters, new Page<Map<Object, Object>>(request, response));

        map.put("page", page);
        map.put("parameters", parameters);
        return "modules/sys/remind/remindList";
    }
    
    /**
     * 提醒人页面
     * @return
     */
    @RequestMapping(value = {"/receiverPage", ""})
    public String receiver(Map<String, Object> map) {
    	JSONArray jsonArray = new JSONArray();
    	List<Map<String, Object>> mapList = Lists.newArrayList();
        try {
        	List<Department> deptList = departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId());
    		for (int i=0; i<deptList.size(); i++){
    			Department e = deptList.get(i);
	        	//查询此部门下所有角色所属用户
				String sql = " SELECT DISTINCT su.id,su.name FROM sys_user_role sur  LEFT JOIN sys_role sr ON sur.roleId= sr.id "
							+" LEFT JOIN sys_user su ON sur.userId=su.id WHERE su.delFlag='0' AND su.name is not NULL  AND sr.deptId="+e.getId();
				List<Map<String,Object>> resultMap = userDao.findBySql(sql,Map.class);
				//若部门下无关联的账号，还需判断子部门下是否有账号，若也没有，该部门不展示
				if(CollectionUtils.isEmpty(resultMap)){
					//查询子部门的id集合
					List<Object> deptIdStr = departmentService.findByOfficeIdAndParentId(UserUtils.getUser().getCompany().getId(), e.getId());
					boolean flag = true;//是否不显示该部门：如 该部门下无账号，且子部门下也无账号，就不显示。
					for(Object id : deptIdStr){
						//查询此部门下所有角色所属用户
						String sql1 = " SELECT DISTINCT su.id,su.name FROM sys_user_role sur  LEFT JOIN sys_role sr ON sur.roleId= sr.id "
									+" LEFT JOIN sys_user su ON sur.userId=su.id WHERE su.delFlag='0' AND su.name is not NULL  AND sr.deptId="+id.toString();
						List<Map<String,Object>> resultMap1 = userDao.findBySql(sql1,Map.class);
						if(CollectionUtils.isNotEmpty(resultMap1)){
							flag = false;
							break;
						}
					}
					if(flag){
						continue;//若该部门以及所有子部门下没有配置用户，则不显示该部门
					}
				}
				
				//封装部门节点
				Map<String, Object> mapDep = Maps.newHashMap();
				mapDep.put("code", e.getId());
				mapDep.put("pId", e.getParent()!=null?e.getParent().getId():0);
				mapDep.put("name", e.getName());
				mapList.add(mapDep);
				
				if(CollectionUtils.isNotEmpty(resultMap)){
					//数据处理以便调用姓氏排序接口
					String[] userArray = new String[resultMap.size()];
					Map<String,String> userMap = new HashMap<String,String>();
					for(int j = 0 ; j<resultMap.size();j++){
						userArray[j] = resultMap.get(j).get("name").toString();
						userMap.put(resultMap.get(j).get("name").toString(), resultMap.get(j).get("id").toString());
					}
					UserUtils.getSortOfChinese(userArray);//按姓氏拼音排序显示
					
					//封装子节点-用户
					for(String name : userArray){
						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("code", Long.parseLong(userMap.get(name)));
						map1.put("pId", e.getId());
						map1.put("name", name);
						mapList.add(map1);
					}			
				}
    		}
    		// 组织json
    		Set<Integer> hasChildren = new HashSet<>();  // 获取所有父节点code
    		Map<Integer, List<Map<String, Object>>> fathers = new HashMap<>();
    		for (Map<String,Object> srcMap : mapList) {
    			Integer code = Integer.parseInt(srcMap.get("pId").toString());  // 获取code
    			hasChildren.add(code);  // 添加set
    			if (fathers.containsKey(code)) {
    				List<Map<String, Object>> children = fathers.get(code);
    				children.add(srcMap);
    				fathers.put(code, children);
				} else {
					List<Map<String, Object>> children = new ArrayList<>();
					children.add(srcMap);
					fathers.put(code, children);
				}
    		}
    		
    		for (Map<String,Object> srcMap : mapList) {
    			Integer code = Integer.parseInt(srcMap.get("code").toString()); 
    			if (hasChildren.contains(code)) {  // 如果自己是某父节点
    				srcMap.put("children", fathers.get(code));
				}
    		}
    		Map<String, Object> superFather = fathers.get(0).get(0);  // 超级父节点
    		JSONObject jsonObject = JSONObject.fromObject(superFather);
    		jsonArray.add(jsonObject);
    		
        	map.put("recStr", jsonArray.toString());
        } catch (Exception e) {
            logger.error("获取提醒人出错!");
            e.printStackTrace();
        }
        return "modules/sys/remind/remindReceivers";
    }
    
    /**
     * 提醒人
     * @return
     */
    @RequestMapping(value = {"/getReceiver", ""})
    public String getReceiver(HttpServletRequest request, HttpServletResponse response) {
    	JSONArray jsonArray = new JSONArray();
        try {
        	List<User> userList = userDao.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
        	for (User user : userList) {
        		Map<String, Object> userMap = new HashMap<>();
        		userMap.put("name", user.getName());
        		userMap.put("code", Integer.parseInt(user.getId().toString()));
        		JSONObject jsonObject = JSONObject.fromObject(userMap);
        		jsonArray.add(jsonObject);
			}
        } catch (Exception e) {
            logger.error("获取提醒人出错!");
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    /**
     * session中删除isRemind, 即当前session中 不在显示弹出的提醒
     * @date 2016年4月8日
     * @param request
     */
    @RequestMapping(value="/removeIsRemind")
    public void removeIsRemind(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.removeAttribute("isRemind");
    }
    
    /**
	 * TODO
	 * 保存中间表 sys_remind_user
	 */
    @ResponseBody
	@RequestMapping(value = "saveRemindUser")
	public Map<String, Object> saveRemindUser(Model model, HttpServletRequest request) {
    	Map<String, Object> resultMap = new HashMap<>();
		String userIdStrs = request.getParameter("userIdStrs");  // 产品类型
		
		return resultMap;
	}


}
