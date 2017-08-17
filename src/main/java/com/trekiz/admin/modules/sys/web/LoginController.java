package com.trekiz.admin.modules.sys.web;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.common.utils.CookieUtils;
import com.trekiz.admin.common.utils.PropertiesLoader;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * 登录Controller
 * 
 * @author zj
 * @version 2013-11-19
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private OfficeService officeService;

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private AgentinfoService agentinfoService; 
	
	@Autowired
	private MenuDao menuDao;

	private static HashSet<String> mtourCompanyUUIDs = null;
	private static String mtourMainPath = null;
	private static PropertiesLoader propertiesLoader;
	static {
		loadMtourCompanyUUID();
		loadMtourMainPath();
	}

	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if (user.getId() != null) {
			return "redirect:" + Global.getAdminPath();
		}
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String login(
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if (user.getId() != null) {
			return "redirect:" + Global.getAdminPath();
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				username);
		model.addAttribute("isValidateCodeLogin",
				isValidateCodeLogin(username, true, false));
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@RequiresUser
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if (user.getId() == null) {
			return "redirect:" + Global.getAdminPath() + "/login";
		}
		
		//quauq登录逻辑--------djw--start-
		Agentinfo agentinfo = agentinfoService.loadAgentInfoById(user.getAgentId());
		//由于不需要考虑session的写入和登录首页是t1还是t2,故注释此处代码
		//System.out.println(request.getRequestURI());
//		if (request.getRequestURI().equals("hqqz.quauqsystem.com.cn")) {
//			if ("1".equals(user.getIsQuauqAgentLoginUser())) {
//				String error = "用户或密码错误, 请重试.";
//				request.setAttribute("error", error);
//				SecurityUtils.getSubject().logout();
//				return "modules/sys/sysLogin";
//			}
//		}else{
//				//此处"1"说明是quauq用户                                                                                     // 此处"1"说明账户是启用状态
//			if (!"1".equals(user.getIsQuauqAgentLoginUser()) || !"1".equals(agentinfo.getEnableQuauqAgent())) {
//				String error = "用户或密码错误, 请重试.";
//				request.setAttribute("error", error);
//				SecurityUtils.getSubject().logout();
//				return "modules/sys/sysLogin";
//			}
//		}
		//quauq登录逻辑--------djw--end-

		// 将登陆用户写入session
		session.setAttribute(UserUtils.ONLINE_USERID, user.getId());
		// 将登陆用户登录名写入session
		session.setAttribute(UserUtils.ONLINE_USER_LOGIN_NAME,
				user.getLoginName());
		// 将登陆用户状态改为“已登陆”
		user.setLoginStatus(1);
		UserUtils.changeLoginStatus(user);
		// 更新登录IP和时间
		systemService.updateUserLoginInfo(user.getId());

		//add 通知中心弹出的提醒，默认显示，当单击忽略，则此次session中不再显示
		session.setAttribute("isRemind", true);

		String s = request.getServerName();

		if (!s.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))")) {

			Long companyId = user.getCompany().getId();
			boolean validate = false;

			if (StringUtils.isNotEmpty(companyId.toString()) && companyId != 1) {
				if (companyId != null && companyId > 0)
					validate = true;
				if (validate) {
				} else {
					String errorMessage = "账号未授权";
					request.setAttribute("Domain_name_error", errorMessage);
					SecurityUtils.getSubject().logout();
					return "modules/sys/sysLogin";
				}
			}
		}
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true); 
		String userType = UserUtils.getUser().getUserType();

		/*
		 * 增加美途逻辑 - 跳转到美途特定的首页 ning.zhang@quauq.com
		 */

		if (mtourCompanyUUIDs.size() != 0
				&& !StringUtils.EMPTY.equals(mtourMainPath)) {
			if (mtourCompanyUUIDs.contains(user.getCompany().getUuid())) {

				/**
				 * by songyang 2015年11月24日20:55:02
				 * 根据不同的用户登录显示不同的首页图片
				 */
				propertiesLoader = new PropertiesLoader("application.properties");
				if(null != propertiesLoader.getProperties().get("mtourCompanyUuid")){
//					String mtourCompanyUuid =propertiesLoader.getProperties().get("mtourCompanyUuid").toString();
					String companyLogo  = propertiesLoader.getProperty("companyLogo").toString();
					String[] companyLogoMap = propertiesLoader.getProperty("companyLogoMap").toString().split(",");
					//简称信息显示状态（美途国际显示）
					String images = "";
					for (int i = 0; i < companyLogoMap.length; i++) {
						String[] companyLogoMaps = companyLogoMap[i].split(":");
						for (int j = 0; j < companyLogoMaps.length; j++) {
							if((UserUtils.getUser().getCompany().getUuid()).equals(companyLogoMaps[0])){
								if(companyLogoMaps.length>1){
									images = companyLogoMaps[1];
									model.addAttribute("images", images);
								}else{
									images = companyLogo;
									model.addAttribute("images", images);
								}
								model.addAttribute("title", "美途国际管理平台");
							}else{
								images = companyLogo;
								model.addAttribute("images", images);
								model.addAttribute("title", "接待社后台");
							}
						}
					}
				}
				return mtourMainPath;
			}
		}
		/*
		 * 美途逻辑结束
		 */
		
		//0463- start ---------------------------------------------------
		//0463默认页签修改   modify by wangyang 2016.6.27
		List<Menu> menuList = UserUtils.getMenuList();
		ok:for(Menu menu : menuList){
			for(Menu childMenu : menu.getChildList()){
				if("成本录入".equals(childMenu.getName())
						&& "运控".equals(childMenu.getParent().getName())){
					
					String[] permitteds = new String[]{"singleCost:manager:view", "looseCost:manager:view", 
							"studyCost:manager:view", "freeCost:manager:view", "bigCustomerCost:manager:view",
							"cruiseCost:manager:view", "cost:airticket:view", "visaCost:manager:view"};
					
					for(String permitted : permitteds){
						if(currentUser.isPermitted(permitted)){
							childMenu.setHref(menuDao.findByPermission(permitted).get(0).getHref());
							break ok;
						}
					}
					break ok;
				}
			}
		}
		UserUtils.putCache(UserUtils.CACHE_MENU_LIST, menuList);
		//0463- end ---------------------------------------------------

		if (Context.USER_TYPE_MAINOFFICE.equalsIgnoreCase(userType)) {
			return "modules/sys/sysIndex";
		} else {
			if (currentUser.isPermitted("product:manager:view")) {
				// 登录成功后，获取上次登录的当前站点ID
				return "redirect:" + Global.getAdminPath()
						+ "/activity/manager/list/2";
			} else {
				// 增加quauq逻辑 - 跳转到quauq特定的首页 --djw---
				if ("1".equals(user.getIsQuauqAgentLoginUser()) && "1".equals(agentinfo.getEnableQuauqAgent())) {
					//进入t1首页	
					
					// 辉腾国际临时策略
					String url = request.getRequestURL().toString();
					if (url.contains("huitengguoji.com")||url.contains("travel.jsjbt")) {
						return "redirect:"+ Global.getAdminPath() +"/t1/jumpParam";
					}else {
						return "redirect:"+ Global.getAdminPath() +"/t1/newHome";
					}
				}else{
					if (!"1".equals(user.getIsQuauqAgentLoginUser())) {
						//进入t2首页
						return "modules/sys/sysIndex";
					}else {
						String errorMessage = "账号未授权";
						request.setAttribute("Domain_name_error", errorMessage);
						SecurityUtils.getSubject().logout();
						return "modules/sys/sysLogin";
					}
				}
			}
		}
	}

	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme,
			HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		} else {
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:" + request.getParameter("url");
	}

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail,
			boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils
				.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}

	/**
	 * 加载需要跳转到美途主页的公司UUID
	 * 
	 * @author ning.zhang@quauq.com
	 */
	private static final void loadMtourCompanyUUID() {
		if (null == LoginController.mtourCompanyUUIDs) {
			LoginController.mtourCompanyUUIDs = new HashSet<>();
			String mtourCompanyUUID = Global.getConfig("mtourCompanyUuid");
			if (StringUtils.isNotBlank(mtourCompanyUUID)) {
				mtourCompanyUUID = mtourCompanyUUID.trim();
				if (mtourCompanyUUID.contains(",")) {
					String[] uuids = mtourCompanyUUID.split(",");
					for (int i = 0; i < uuids.length; i++) {
						LoginController.mtourCompanyUUIDs.add(uuids[i]);
					}
				} else {
					LoginController.mtourCompanyUUIDs.add(mtourCompanyUUID);
				}
			}
		}
	}

	/**
	 * 加载美途主页的path
	 * 
	 * @author ning.zhang@quauq.com
	 */
	private static final void loadMtourMainPath() {
		if (null == LoginController.mtourMainPath) {
			String mtourPath = Global.getConfig("mtourMainPath");
			if (StringUtils.isNotBlank(mtourPath)) {
				LoginController.mtourMainPath = mtourPath.trim();
			} else {
				mtourMainPath = StringUtils.EMPTY;
			}
		}
	}
}
