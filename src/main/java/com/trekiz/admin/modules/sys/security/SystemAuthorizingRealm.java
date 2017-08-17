/**
 *
 */
package com.trekiz.admin.modules.sys.security;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.manage.entity.UserTenant;
import com.quauq.multi.tenant.manage.service.TenantService;
import com.quauq.multi.tenant.util.MultiTenantUtil;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.servlet.ValidateCodeServlet;
import com.trekiz.admin.common.utils.Encodes;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.sys.web.LoginController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

/**
 * 系统安全认证实现类
 * @author zj
 * @version 2013-11-19
 */
@Service
@DependsOn({"userDao","roleDao","menuDao", "departmentDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private SystemService systemService;
	
	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		if (token.getUserType()==0 && LoginController.isValidateCodeLogin(token.getUsername(), false, false)){
			// 在不是从php网站部分过来登录，并且错误次数没有超过系统限制的情况下检查验证码
			Session session = SecurityUtils.getSubject().getSession();
			String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
				throw new CaptchaException("验证码错误.");
			}
		}

		/**
		 * 多租户开关
		 */
		if(MultiTenantUtil.turnOnMulitTenant()){
			TenantService tenantService=(TenantService)SpringContextHolder.getBean("tenantService");
			UserTenant userTenant=tenantService.findUserTenant(token.getUsername());
			if (userTenant!=null) {
				if (tenantService.checkAndInitTenant(userTenant.getTenantId())) {
					FacesContext.setCurrentTenant(userTenant.getTenantId());
				}else{
					return null;
				}
			}else {
				return null;
			}
		}


		User user = null;
		try {
			user = getSystemService().getUserByLoginName(token.getUsername());
		}catch (Exception e){
			e.printStackTrace();
		}

		if (user != null) {
			byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user), 
					user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}
	}

	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list) {
				if (StringUtils.isNotBlank(menu.getPermission())) {
					// 添加基于Permission的权限信息
					info.addStringPermission(menu.getPermission());
				}
			}
			info.addStringPermission("common:mtour:menu");
			//add by shijun.liu 此处只用于环球行签证借款
			//环球行签证借款，与其他借款有很大差别，现将其用于权限控制
			//环球行目前只有签证借款(无其他产品线的借款)，且是批量借款。结算管理-付款-只显示“借款付款”，内容是批量借款
			if(Context.SUPPLIER_UUID_HQX.equals(user.getCompany().getUuid())){
				info.addStringPermission("tts:visa:borrowMoney");
			}
			//新行者用户签证借款是单个，不是批量。结算管理-付款-只显示“借款付款”，内容是单个借款付款功能
			if(Context.SUPPLIER_UUID_XXZ.equals(user.getCompany().getUuid())){
				info.addStringPermission("xxz:common:borrowMoney");
			}
			//除新行者和环球行之外的批发商，自新审批之后，签证借款付款都是批量借款。
			//所以，结算管理-付款-显示“借款付款”，内容是单个借款付款功能，签证批量借款付款，内容是批量借款。两个菜单
			// add by shijun.liu 2016.03.31
			if(!Context.SUPPLIER_UUID_HQX.equals(user.getCompany().getUuid()) &&
					!Context.SUPPLIER_UUID_XXZ.equals(user.getCompany().getUuid())){
				info.addStringPermission("visa:common:borrowMoney");
			}
			//注释于20151108 update by zhanghao 更新登陆IP和时间的方法转移到登陆操作中（LoginController.index）
			// 更新登录IP和时间
			//getSystemService().updateUserLoginInfo(user.getId());
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private Long id;
		private String loginName;
		private String name;
		private Map<String, Object> cacheMap;

		public Principal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
		}

		public Long getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

	}
	
	/**
	 * 支持的运算符和运算符优先级
	 */
    public static final Map<String, Integer> expMap = new HashMap<String, Integer>(){{
        put("not",0);
        put("!"  ,0);
 
        put("and",0);
        put("&&" ,0);
 
        put("or" ,0);
        put("||" ,0);
 
        put("("  ,1);
        put(")"  ,1);
    }};
    
    public static final Set<String> expList = expMap.keySet();
    
    public SystemAuthorizingRealm() {
    }
 
    public SystemAuthorizingRealm(CacheManager cacheManager) {
        super(cacheManager);
    }
 
    public SystemAuthorizingRealm(CredentialsMatcher matcher) {
        super(matcher);
    }
 
    public SystemAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
    }
 
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        Stack<String> exp = getExp(expList, permission);
        if (exp.size() == 1){
            return super.isPermitted(principals, exp.pop());
        }
        List<String> expTemp = new ArrayList<>();
        //将其中的权限字符串解析成true , false
        for(String temp : exp){
            if (expList.contains(temp)){
                expTemp.add(temp);
            }else{
                expTemp.add(Boolean.toString(super.isPermitted(principals, temp)) );
            }
        }
        //计算逆波兰
        return computeRpn(expList, expTemp);
    }
 
 
    private static boolean computeRpn(Collection<String> expList,Collection<String> exp){
        Stack<Boolean> stack = new Stack<>();
        for(String temp : exp){
            if (expList.contains(temp)){
                if ("!".equals(temp) || "not".equals(temp)){
                    stack.push( !stack.pop() );
                }else if ("and".equals(temp) || "&&".equals(temp)){
                    Boolean s1 = stack.pop();
                    Boolean s2 = stack.pop();
                    stack.push(s1 && s2);
                }else{
                    Boolean s1 = stack.pop();
                    Boolean s2 = stack.pop();
                    stack.push(s1 || s2);
                }
            }else{
                stack.push(Boolean.parseBoolean(temp));
            }
        }
        if (stack.size() > 1){
            throw new RuntimeException("compute error！ stack: "+ exp.toString());
        }else{
            return stack.pop();
        }
    }
 
    /**
     * 
    * @Title: getExp
    * @Description: TODO(获得逆波兰表达式)
    * @param @param expList
    * @param @param exp
    * @param @return    设定文件
    * @return Stack<String>    返回类型
    * @throws
     */
    private static Stack<String> getExp(Collection<String> expList, String exp) {
        Stack<String> s1 = new Stack<>();
        Stack<String> s2 = new Stack<>();
        for (String str : exp.split(" ")){
            str = str.trim();
            String strL = str.toLowerCase();
            if ("".equals(str)){
                continue;
            }
            if ("(".equals(str)){
                //左括号
                s1.push(str);
            }else if (")".equals(str)){
                //右括号
                while(!s1.empty()){
                    String temp = s1.pop();
                    if ("(".equals(temp)){
                        break;
                    }else{
                        s2.push(temp);
                    }
                }
            }else if(expList.contains(strL)){
                //操作符
                if (s1.empty()){
                    s1.push(strL);
                }else {
                    String temp = s1.peek();
                    if ("(".equals(temp) || ")".equals(temp)){
                        s1.push(strL);
                    }else if(expMap.get(strL) >= expMap.get(temp)){
                        s1.push(strL);
                    }else{
                        s2.push(s1.pop());
                        s1.push(strL);
                    }
                }
            }else{
                //运算数
                s2.push(str);
            }
        }
        while(!s1.empty()){
            s2.push(s1.pop());
        }
        return s2;
    }
}
