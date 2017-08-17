package com.trekiz.admin.modules.activity.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.IGroupcodeModifiedRecordService;

/**  
 * @Title: GroupcodeLibrary.java
 * @Package com.trekiz.admin.modules.activity.web
 * @Description: 出产品团号库的相关操作
 * @author:
 * @date 2015-2015年11月26日 下午3:03:25
 * @version V1.0  
 */
@Controller
@RequestMapping(value="${adminPath}/activity/groupcodelibrary")
public class GroupcodeLibraryController extends BaseController{
	
	@Autowired
	private IGroupcodeModifiedRecordService iGroupcodeModifiedRecordService;
	
	@RequestMapping(value="toGroupcodeLibraryBox")
	public String toGroupcodeLibraryBox(Model model,HttpServletRequest request, HttpServletResponse response){
		String groupNo = request.getParameter("groupNo");
		String groupCreateDate = request.getParameter("groupCreateDate");
		
		//
		/**
		 * 
		 * groupcodelibtype = 7 为机票
		 * groupcodelibtype = 6 为签证
		 * groupcodelibtype = 其他（团期）
		 * 
		 */
		String groupcodelibtype = request.getParameter("groupcodelibtype");
		Page<Map<String, Object>> page = null;
		if ("7".equals(groupcodelibtype)) {
			page = iGroupcodeModifiedRecordService.queryGroupCodeLibrary4Airticket(request, response);
		}else if ("6".equals(groupcodelibtype)) {
			//对应需求号   c460
			page = iGroupcodeModifiedRecordService.queryGroupCodeLibrary4Visa(request, response);
		}else{
			page = iGroupcodeModifiedRecordService.queryGroupCodeLibrary4Tuanqi(request, response);
		}
		 
		//团号库的分页结果集
		model.addAttribute("page", page);
		//返回页面的查询tiaojian
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("groupCreateDate", groupCreateDate);
		model.addAttribute("groupcodelibtype", groupcodelibtype);
		
		return "modules/activity/groupcodelibrary/groupcodeLibraryBox";
	}

}
