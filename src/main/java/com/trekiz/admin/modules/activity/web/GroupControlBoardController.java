package com.trekiz.admin.modules.activity.web;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.IGroupControlBoardService;

@Controller
@RequestMapping(value="${adminPath}/activity/controlBoard")
public class GroupControlBoardController extends BaseController {
	
	@Autowired
	private IGroupControlBoardService groupControlBoardService;

	@ResponseBody
	@RequestMapping(value="getGroupControlBoardList")
	public Object getGroupControlBoardList(HttpServletRequest request, HttpServletResponse response){
		List<Map<String,Object>> result = null;
		String nameOrCode = request.getParameter("nameOrCode"); // 产品名称或团号
		String groupOpenDateFrom = request.getParameter("groupOpenDateFrom"); // 出团起始日期
		String groupOpenDateTo = request.getParameter("groupOpenDateTo"); 	 // 出团结束日期
//		System.out.println("sql begin " + System.currentTimeMillis());
//		List<Map<String,Object>> list = groupControlBoardService.getGroupControlBoardList(nameOrCode, groupOpenDateFrom, groupOpenDateTo);
//		System.out.println("size " + list.size());
//		System.out.println("sql end   " + System.currentTimeMillis());
		Page<ActivityGroup> page = new Page<ActivityGroup>(request, response);
		page.setPageSize(15);
		page.setOrderBy("groupOpenDate"); // 出团日期降序
		try {
			result = groupControlBoardService.getGroupControlBoardListNew(page, nameOrCode, groupOpenDateFrom, groupOpenDateTo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value="groupControlBoardOpePage")
	public Object groupControlBoardOpePage(HttpServletRequest request, HttpServletResponse response){
		String groupId = request.getParameter("groupId"); // 团号
		String opeType = request.getParameter("opeType"); // 操作类型
		Map<String, Object> map = groupControlBoardService.groupControlBoardOpePage(Long.parseLong(groupId),opeType);
		return map;
	}


	@ResponseBody
	@RequestMapping(value="insertGroupControlBoard")
	public Object insertGroupControlBoard(HttpServletRequest request, HttpServletResponse response){
		// TODO 前台针对可能出现的操作失败做一个提示
		Map<String, String> result = new HashMap<String, String>();
		String opeType = request.getParameter("opeType"); // 操作项 1收客
		String amount = request.getParameter("amount"); // 数量
		String remarks = request.getParameter("remarks"); // 备注
		String groupId = request.getParameter("groupId"); // 团期id
		try {
			if (StringUtils.isEmpty(opeType) || StringUtils.isEmpty(amount) || StringUtils.isEmpty(remarks) || StringUtils.isEmpty(groupId)) {
				result.put("message", "传入参数不全!");
				result.put("result", "fail");
			} else {
				result = groupControlBoardService.insertGroupControlBoard(Integer.parseInt(opeType), Integer.parseInt(amount), remarks, Long.parseLong(groupId), -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "error");
			result.put("message", "保存出错!");
		}
		return result;
	}
	
	/**
	 * 单个团期操作页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getSingleOpeRecordPage")
	public String getSingleOpeRecordPage(HttpServletRequest request, HttpServletResponse response, Model model) {
		String groupId = request.getParameter("groupId");
		model.addAttribute("groupId", groupId);
		return "layouts/singleOperationRecord";
	}
	
	@ResponseBody
	@RequestMapping(value = "getSingleOpeRecord")
	public Object getSingleOpeRecord(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = groupControlBoardService.groupContralBoardOpeRecord(request);
		return map;
	}

	/**
	 * 所有团期操作页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getWholeOpeRecordPage")
	public String getWholeOpeRecordPage(HttpServletRequest request, HttpServletResponse response) {
		return "layouts/wholeOperationRecord";
	}
	
	@ResponseBody
	@RequestMapping(value = "getWholeOpeRecord")
	public Object getWholeOpeRecord(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = groupControlBoardService.groupContralBoardOpeRecordAll(request);
		return resultMap;
	}
	
}
