/**
 *
 */
package com.trekiz.admin.common.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.trekiz.admin.common.beanvalidator.BeanValidators;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SysModuleConfigService;
import com.trekiz.admin.modules.sys.service.SysModulePathService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 控制器支持类
 * @author zj
 * @version 2013-11-19
 */
public abstract class BaseController {
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	@Autowired
	protected LogOperateService logOpeService;
	@Autowired
	protected SysModulePathService sysModulePathService;
	@Autowired
	protected SysModuleConfigService sysModuleConfigService;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
//				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
				setValue(text == null ? null : text.trim());
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}
	/**
	 * @param moduleid 模块ID
	 * @param ope_type 操作状态
	 * @param content 修改内容
	 * @param operateType 1 增 2删 3改 
	 * @param 业务类型
	 * @return
	 */
	public String saveUserOpeLog(String moduleid, 
			String modularname, String content, String ope_type, Integer bussinessType, Long bussinessId){
		
		logOpeService.saveLogOperate(moduleid, modularname, content, ope_type, bussinessType, bussinessId);
		
		return "";
	}
	
		/**
		 * executePath(页面定制入口，根据返回页面路径实现可定制 可编辑的页面)
		 * @param path 返回页面路径
		 * @return
		 * @exception
		 * @since  1.0.0
		 */
		public String executePath(String path){	
			// 获取request对象
//			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			
			//1.判断path是否在sys_module_path中；如果不存在则加入
			sysModulePathService.save(path);
			//2.根据公司，判断path是否在sys_module_config中存在，如果存在，则返回格式化的路径（rootpath/公司id/path）；不存在则返回path
			//如果不是预览，则表示为当前公司，如果是预览则为指定公司
//			String companyId = request.getParameter("acs_company_id")==null?UserUtils.getUser().getCompany().getId().toString():request.getParameter("acs_company_id");
			
//			SysModuleConfig sysModuleConfig = sysModuleConfigService.queryModuleByPathAndCompanyId(path,companyId);
			// 3.
			String execpath = "";
		
			// 根据公司ID和路径不为空，是定制页面
//			if(sysModuleConfig!=null&&sysModuleConfig.getId()!=null){ 
//				execpath = PageCachedFileUtil.execute(path, "RootCompany", companyId);
//				// 文件不存在,同步文件
//				try{
//				if(!PageCachedFileUtil.ismarke(execpath, request.getContextPath())){
//					boolean flag  = PageCachedFileUtil.makefile(execpath, path, request.getContextPath());
//					System.out.println(flag); 
//					
//				}
//				}catch(Exception e){
//					
//				}
//			}else{
//				execpath = path;
//			}
			
			return execpath;
		}
		
		/**
		 * 将request中的参数重新放到request中去
		 * @author majiancheng
		 * 2015年4月9日 
		 */
		protected void exposeRequestParameter(HttpServletRequest request){
			Map<String, Object> params = WebUtils.getParametersStartingWith(request, "");
			//将请求的参数保存到REQUEST中
	        WebUtils.exposeRequestAttributes(request, params);
		}
		/**
		 * 	获取超级管理员或普通批发商用户公司ID（超级管理员公司ID为-1）
			* 
			* @param 
			* @return long
			* @Time 2015-4-13
		 */
		protected Long getCompanyId(){
			User user = UserUtils.getUser();
			long l = 0;
			if(user==null)return l;
			
			if(user.getId()==1){//判断是否是超级用户
				l =  -1;
			}else{//非超级用户查询本公司的字典列表
				Office company = UserUtils.getUser().getCompany();
	        	if(company != null) {
	        		l =  company.getId();
	        	}
	        	
			}
			return l;
	    }
}
