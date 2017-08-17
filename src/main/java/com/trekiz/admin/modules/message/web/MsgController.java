package com.trekiz.admin.modules.message.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.http.client.utils.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.entity.MsgMark;
import com.trekiz.admin.modules.message.entity.MsgToDepartment;
import com.trekiz.admin.modules.message.form.MsgAnnouncementForm;
import com.trekiz.admin.modules.message.form.MsgSelectForm;
import com.trekiz.admin.modules.message.service.MessageMsgService;
import com.trekiz.admin.modules.message.service.MsgToDepartmentService;
import com.trekiz.admin.modules.message.service.MsgUserMarkService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
/**
 * 
 * @author gao
 *  2015年2月27日
 */
@Controller
@RequestMapping(value="${adminPath}/message")
public class MsgController {

	@Autowired
	private MessageMsgService messageMsgService;
	@Autowired
	private MsgUserMarkService msgUserMarkService;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private MsgToDepartmentService msgToDepartmentService;
	private Integer[] nums;
	
	/**
	 * 跳转到添加公告页面
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="goToAddMessage")
	public String goToAddMessage(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		
		// 获取部门
		List<Department> departmentSet = UserUtils.getUserDept();
		if(departmentSet!=null && !departmentSet.isEmpty()){
			model.addAttribute("departmentSet",departmentSet);
		}
		
		return "modules/message/addMessage";
	}
	
	/**
	 * 跳转到修改公告页面
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="goToEditMessage/{id}")
	public String goToEditMessage(@PathVariable("id") Long id,HttpServletResponse response,
	        Model model, HttpServletRequest request){
		
		MsgAnnouncement msg = messageMsgService.findMsgById(id);
		List<MsgToDepartment> msgToDepartmentList = new ArrayList<MsgToDepartment>();
		if(msg!=null){
			model.addAttribute("msg",msg);
			if(msg.getOverTime()!=null){
				model.addAttribute("overTime",DateUtils.formatDate(msg.getOverTime(),"yyyy-MM-dd"));
			}
			// 获取下载文件
			List<DocInfo> docList = getDocInfoList(msg);
			if(docList!=null && !docList.isEmpty()){
				model.addAttribute("docList", docList);
			}
			// 获取选取的部门
			msgToDepartmentList = msgToDepartmentService.findMsgToDepartmentList(msg.getId());
			model.addAttribute("departmentList",msgToDepartmentList);
		}
		// 获取部门
		List<Department> departmentSet = UserUtils.getUserDept();
		if(departmentSet!=null && !departmentSet.isEmpty()){
			model.addAttribute("departmentSet",departmentSet);
			// 判断选取部门与获取部门是否相同
			if(msgToDepartmentList!=null && !msgToDepartmentList.isEmpty() && msgToDepartmentList.size()==departmentSet.size()){
				model.addAttribute("equal",1);
			}
		}
		return "modules/message/editMessage";
	}
	
	/**
	 * 增加公告
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="addAJaxMsg", method=RequestMethod.POST)
	public Map<String,Object> addAJaxMsg(@Valid MsgAnnouncementForm form,HttpServletRequest request,BindingResult result){
		
		Map<String,Object> map = new HashMap<String,Object>();
		String saveStatus = request.getParameter("saveStatus");
		if(StringUtils.isNotBlank(saveStatus)){
			form.setSaveStatus(Integer.valueOf(saveStatus));
		}
		if(StringUtils.isNotBlank(form.getTitle()) && StringUtils.isNotBlank(form.getContent())){
			if(form.getMsgType() == Context.MSG_TYPE_ENGAGE){
				if(form.getTitle().length()<1 || form.getTitle().length()>50){
					map.put("res", "data_error");
					map.put("message", "标题长度须在50字以内");
					
					return map;
				}
			}else{
				if(form.getTitle().length()<1 || form.getTitle().length()>20){
					map.put("res", "data_error");
					map.put("message", "标题长度须在20字以内");
					return map;
				}
			}
			
			String cont = form.getContent(); // 公告内容
			String str = StringUtils.delHTMLTag(cont);// 去除标签后的公告内容
//			System.out.println(cont);
//			System.out.println("--------------------------------------------------------------");
//			System.out.println(str);
			if(str.length()<1 || str.length()>10000){
				map.put("res", "data_error");
				map.put("message", "公告内容长度须在10000字以内");
				return map;
			}
		}else{
			map.put("res", "data_error");
			map.put("message", "标题和内容不可为空");
			return map;
		}
		
		// 检查数据错误
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("message", result.getAllErrors());
			return map;
		}else{//检查是否有基础数据业务错误
			String msg =  form.check();
			if(!StringUtils.isBlank(msg)){
				map.put("res", "data_error");
				map.put("message", msg);
				return map;		
			}
		}
		map = messageMsgService.addMsg(form);
		
		return map;
	}
	
	/**
	 * 修正公告
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="editAJaxMsg", method=RequestMethod.POST)
	public Map<String,Object> editAJaxMsg(@Valid MsgAnnouncementForm form,BindingResult result){
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(form.getTitle()) && StringUtils.isNotBlank(form.getContent())){
			if(form.getTitle().length()<1 || form.getTitle().length()>20){
				map.put("res", "data_error");
				map.put("message", "标题长度须在20字以内");
				return map;
			}
			String cont = form.getContent(); // 公告内容
			String str = StringUtils.delHTMLTag(cont);// 去除标签后的公告内容
//			System.out.println(cont);
//			System.out.println("--------------------------------------------------------------");
//			System.out.println(str);
			if(str.length()<1 || str.length()>10000){
				map.put("res", "data_error");
				map.put("message", "公告内容长度须在10000字以内");
				return map;
			}
		}else{
			map.put("res", "data_error");
			map.put("message", "标题和内容不可为空");
			return map;
		}
		// 检查数据错误
		if(result.hasErrors()){
			map.put("res", "data_error");
			return map;
		}
		map = messageMsgService.editMsg(form);
		return map;
	}

	/**
	 * 270 查询提醒的title
	 */
	@RequestMapping("findRemindTitle")
	@ResponseBody
	public String findRemindTitle(){
		String msg = "";
		try{
			msg = messageMsgService.findRemindTitle();
			//将提醒title放入session，不必每次加载wholesaler.jsp都要请求此方法
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession();
			session.setAttribute("remindTitle", msg);
		}catch (Exception e){
			e.printStackTrace();
		}
		return msg;
	}


	/**
	 * 查询公告/消息列表
	 * 2016年4月6日 新增@PathVariable("pageType"),用来确认该显示"公告(0)","消息(5)","提醒(7)"中的哪个列表
	 * @param form
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="findMsgList/{pageType}")
	public String findMsgList(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("pageType") String pageType){
		
		String conn = request.getParameter("conn");
		String remindType = request.getParameter("remindType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String type = request.getParameter("msgType");
		String ifRead = request.getParameter("ifRead");
		String[] depIds = request.getParameterValues("msgNoticeType");

		MsgSelectForm form = new MsgSelectForm();
		if(StringUtils.isNotBlank(conn)){
			conn = conn.trim();
			form.setConn(conn);
		}
		if(StringUtils.isNotBlank(startDate)){
			form.setStartDate(startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			form.setEndDate(endDate);
		}
		if(StringUtils.isNotBlank(type)){
			form.setMsgType(Integer.valueOf(type));
		}else{
			//204根据pageType决定该显示哪个列表
			if ("5".equals(pageType)) {        		//消息
				form.setMsgType(5);
			} else if ("7".equals(pageType)) {    	//提醒
				form.setMsgType(7);
			} else {
				form.setMsgType(0);
			}
		}
		if(StringUtils.isNotBlank(ifRead)){
			form.setIfRead(Integer.valueOf(ifRead));
		}
		if(StringUtils.isNotBlank(remindType)){
			form.setRemindType(Integer.valueOf(remindType));
		}
		// 获取前台选中的部门
		List<Long> depidList = new ArrayList<Long>();
		if(depIds!=null && depIds.length>0){
			Integer[] nums = new Integer[depIds.length];
			for(int n=0;n<depIds.length;n++){
				nums[n] = Integer.valueOf(depIds[n]);
				depidList.add(Long.valueOf(depIds[n]));
			}
			form.setDepIds(nums);
			model.addAttribute("depidList",depidList); // 已经选中的部门ID组
		}
		
		// 获取部门
		List<Department> departmentSet = UserUtils.getUserDept();
		if(departmentSet!=null && !departmentSet.isEmpty()){
			model.addAttribute("departmentSet",departmentSet);
		}

		// 全部消息和公告数量
		Integer messageNum = new Integer(0);
		// 全站公告数量
		Integer activeNum = new Integer(0);
		// 部门公告数量
		Integer partActiveNum = new Integer(0);
		// 渠道公告数量
		Integer agentActiveNum = new Integer(0);
		// 约签公告数量
		Integer engageActiveNum = new Integer(0);
		// 消息数量
		Integer messActiveNum = new Integer(0);
		// 财务消息数量
		Integer financeActiveNum = new Integer(0);
		// 提醒数量
		Integer remindNum = new Integer(0);

		// 按照是否有部门id来查找公告列表,因为按照部门查询和不按照部门查询语句不同
		Page<MsgMark> page = null;
		List<MsgMark> list = new ArrayList<MsgMark>();
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			page = messageMsgService.findPartBySql(form, new Page<MsgMark>(request, response));
			list = messageMsgService.findPartBySqlList(form);
		}else{
			page = messageMsgService.findMsgByUserId(form, new Page<MsgMark>(request, response));
			list = messageMsgService.findMsgByUserId(form);
		}
		if(list!=null && !list.isEmpty()){
			model.addAttribute("msgList",page.getList());
			model.addAttribute("page",page);
			List<MsgMark> marks = page.getList();
			List<Map<String, Object>> msg4CW = Lists.newArrayList();
			for (MsgMark msgMark : marks) {				
				if (msgMark.getMsgType() == 6) {
					Map<String, Object> msgMap4CW = Maps.newHashMap();
					msgMap4CW.put("id", msgMark.getId());
					msgMap4CW.put("title", msgMark.getTitle());
					String msgTitle = msgMark.getTitle();
					if (StringUtils.isNotBlank(msgTitle) && msgTitle.contains("#")) {						
						String [] titleParts = msgTitle.split("#");
						String [] title4CW = {titleParts[0], getOrderDetailUrlByIdAndType(titleParts[1], msgMark.getMsgNoticeType()), titleParts[2], titleParts[3]};
						msgMap4CW.put("title4CW", title4CW);
					}
					msg4CW.add(msgMap4CW);
				}
			}
			model.addAttribute("msg4CW",msg4CW);
			//消息数量分布
			Iterator<MsgMark> iter = list.iterator();
			while(iter.hasNext()){
				MsgMark msg = iter.next();
				if(msg.getMsgType()==1){// 全站公告数量
					activeNum+=1;
				}else if(msg.getMsgType()==2){// 部门公告数量
					partActiveNum+=1;
				}else if(msg.getMsgType()==3){// 渠道公告数量
					agentActiveNum+=1;
				}else if(msg.getMsgType()==4){// 约签公告数量
					engageActiveNum+=1;
				}else if(msg.getMsgType()==5){// 消息数量
					messActiveNum+=1;
				}else if(msg.getMsgType()==6){// 财务消息数量
					financeActiveNum+=1;
				}else if(msg.getMsgType()==7){// 提醒数量
					remindNum+=1;
				}
			}
			messageNum=list.size() - remindNum;
		}
		model.addAttribute("messageNum",messageNum);
		model.addAttribute("activeNum",activeNum);
		model.addAttribute("partActiveNum",partActiveNum);
		model.addAttribute("agentActiveNum",agentActiveNum);
		model.addAttribute("engageActiveNum",engageActiveNum);
		model.addAttribute("messActiveNum",messActiveNum);
		model.addAttribute("financeActiveNum",financeActiveNum);
		model.addAttribute("remindNum",remindNum);
		model.addAttribute("form",form);
		
		return "modules/message/messageList";
	}
	
	/**
	 * 获取订单详情页URL
	 * @param orderId 订单ID
	 * @param msgNoticeType 订单类型（消息类型）
	 * @return
	 */
	private String getOrderDetailUrlByIdAndType(String orderId, Integer msgNoticeType) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(orderId) || msgNoticeType == null) {
			return "javascript:void(0);";
		}
		
		String url= null;
		//单团、散拼、游学、游轮、大客户、自由行
		if (Context.ORDER_TYPE_DT == msgNoticeType || 
			Context.ORDER_TYPE_SP == msgNoticeType || 
			Context.ORDER_TYPE_YX == msgNoticeType || 
			Context.ORDER_TYPE_CRUISE == msgNoticeType || 
			Context.ORDER_TYPE_DKH == msgNoticeType || 
			Context.ORDER_TYPE_ZYX == msgNoticeType) {
			
			url = "/orderCommon/manage/orderDetail/" + orderId;			
		} else if (Context.ORDER_TYPE_JP == msgNoticeType) {
			//机票
			url = "/order/manage/airticketOrderDetail?orderId=" + orderId;
		} else if (Context.ORDER_TYPE_QZ == msgNoticeType) {
			//签证
			url = "/visa/order/goUpdateVisaOrder?visaOrderId=" + orderId + "&details=1";
		} else if (Context.ORDER_TYPE_ISLAND == msgNoticeType) {
			//海岛游(暂时不写)
			url = "javascript:void(0);";
		} else if (Context.ORDER_TYPE_HOTEL == msgNoticeType) {
			//酒店(暂时不写)
			url = "javascript:void(0);";
		} else {
			url = "javascript:void(0);";
		}
		
		return url;
	}

	/**
	 * 查询自己发布的公告列表
	 * @param form
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="findMyCreateMsgList")
	public String findMyCreateMsgList(HttpServletRequest request, HttpServletResponse response, Model model){
		
		String conn = request.getParameter("conn");
		String startDate = request.getParameter("startDate");
		
		String endDate = request.getParameter("endDate");
		String type = request.getParameter("msgType");
		String ifRead = request.getParameter("ifRead");

		String[] depIds = request.getParameterValues("msgNoticeType");
		MsgSelectForm form = new MsgSelectForm();
		if(StringUtils.isNotBlank(conn)){
//			try {
//				conn = conn.trim();
//				conn = new String(conn.getBytes("ISO-8859-1"),"UTF-8");
//				form.setConn(conn);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
			conn = conn.trim();
			form.setConn(conn);
		}
		if(StringUtils.isNotBlank(startDate)){
			form.setStartDate(startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			form.setEndDate(endDate);
		}
		if(StringUtils.isNotBlank(type)){
			form.setMsgType(Integer.valueOf(type));
		}else{
			form.setMsgType(0);
		}
		
		if(StringUtils.isNotBlank(ifRead)){
			form.setIfRead(Integer.valueOf(ifRead));
		}
		
		
		// 获取前台选中的部门
				List<Long> depidList = new ArrayList<Long>();
				if(depIds!=null && depIds.length>0){
					Integer[] nums = new Integer[depIds.length];
					for(int n=0;n<depIds.length;n++){
						nums[n] = Integer.valueOf(depIds[n]);
						depidList.add(Long.valueOf(depIds[n]));
					}
					form.setDepIds(nums);
					model.addAttribute("depidList",depidList); // 已经选中的部门ID组
				}
				
				// 获取部门
				List<Department> departmentSet = UserUtils.getUserDept();
				if(departmentSet!=null && !departmentSet.isEmpty()){
					model.addAttribute("departmentSet",departmentSet);
				}
		
		form.setSaveStatus(1); // 已发布的消息
		
		// 全部消息和公告数量
		Integer messageNum = new Integer(0);
		// 全站公告数量
		Integer activeNum = new Integer(0);
		// 部门公告数量
		Integer partActiveNum = new Integer(0);
		// 渠道公告数量
		Integer agentActiveNum = new Integer(0);
		// 约签公告数量
		Integer engageActiveNum = new Integer(0);
		// 消息数量
		Integer messActiveNum = new Integer(0);
		Page<MsgAnnouncement> page = null;
		List<MsgAnnouncement> list =null;
		// 按照是否有部门id来查找公告列表,因为按照部门查询和不按照部门查询语句不同
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			page = messageMsgService.findPartMySelfBySql(form, new Page<MsgAnnouncement>(request, response));
			list = messageMsgService.findPartMySelfBySqlList(form);
		}else{
			page = messageMsgService.findMyCreateMsgByUserId(form, new Page<MsgAnnouncement>(request, response));
			list = messageMsgService.findMyCreateMsgByUserId(form);
		}
		
		
		if(list!=null && !list.isEmpty()){
			model.addAttribute("msgList",page.getList());
			model.addAttribute("page",page);
			/*
			 * 消息数量分布
			 */
			Iterator<MsgAnnouncement> iter = list.iterator();
			while(iter.hasNext()){
				MsgAnnouncement msg = iter.next();
				if(msg.getMsgType()==1){// 全站公告数量
					activeNum+=1;
				}else if(msg.getMsgType()==2){// 部门公告数量
					partActiveNum+=1;
				}else if(msg.getMsgType()==3){// 渠道公告数量
					agentActiveNum+=1;
				}else if(msg.getMsgType()==4){// 约签公告数量
					engageActiveNum+=1;
				}else if(msg.getMsgType()==5){// 消息数量
					messActiveNum+=1;
				}
			}
			messageNum=list.size();
		}
		model.addAttribute("messageNum",messageNum);
		model.addAttribute("activeNum",activeNum);
		model.addAttribute("partActiveNum",partActiveNum);
		model.addAttribute("agentActiveNum",agentActiveNum);
		model.addAttribute("engageActiveNum",engageActiveNum);
		model.addAttribute("messActiveNum",messActiveNum);
		model.addAttribute("form",form);
		
		return "modules/message/messageMyCreateList";
	}
	
	/**
	 * 查询自己发布的草稿列表
	 * @param form
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="findMySaveMsgList")
	public String findMySaveMsgList(HttpServletRequest request, HttpServletResponse response, Model model){
		
		String conn = request.getParameter("conn");
		String startDate = request.getParameter("startDate");
		
		String endDate = request.getParameter("endDate");
		String type = request.getParameter("msgType");
		String ifRead = request.getParameter("ifRead");

		String[] depIds = request.getParameterValues("msgNoticeType");
		MsgSelectForm form = new MsgSelectForm();
		if(StringUtils.isNotBlank(conn)){
//			try {
//				conn = conn.trim();
//				conn = new String(conn.getBytes("ISO-8859-1"),"UTF-8");
//				form.setConn(conn);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
			conn = conn.trim();
			form.setConn(conn);
		}
		if(StringUtils.isNotBlank(startDate)){
			form.setStartDate(startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			form.setEndDate(endDate);
		}
		if(StringUtils.isNotBlank(type)){
			form.setMsgType(Integer.valueOf(type));
		}else{
			form.setMsgType(0);
		}
		
		if(StringUtils.isNotBlank(ifRead)){
			form.setIfRead(Integer.valueOf(ifRead));
		}
		form.setSaveStatus(0);
		// 获取前台选中的部门
		List<Long> depidList = new ArrayList<Long>();
		if(depIds!=null && depIds.length>0){
			Integer[] nums = new Integer[depIds.length];
			for(int n=0;n<depIds.length;n++){
				nums[n] = Integer.valueOf(depIds[n]);
				depidList.add(Long.valueOf(depIds[n]));
			}
			form.setDepIds(nums);
			model.addAttribute("depidList",depidList); // 已经选中的部门ID组
		}
		form.setSaveStatus(0); // 草稿状态的消息
		// 获取部门
		List<Department> departmentSet = UserUtils.getUserDept();
		if(departmentSet!=null && !departmentSet.isEmpty()){
			model.addAttribute("departmentSet",departmentSet);
		}

		// 全部消息和公告数量
		Integer messageNum = new Integer(0);
		// 全站公告数量
		Integer activeNum = new Integer(0);
		// 部门公告数量
		Integer partActiveNum = new Integer(0);
		// 渠道公告数量
		Integer agentActiveNum = new Integer(0);
		// 约签公告数量
		Integer engageActiveNum = new Integer(0);
		// 消息数量
		Integer messActiveNum = new Integer(0);
		// 按照是否有部门id来查找公告列表,因为按照部门查询和不按照部门查询语句不同
		Page<MsgAnnouncement> page =null;
		List<MsgAnnouncement> list = null;
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			page = messageMsgService.findPartMySelfBySql(form, new Page<MsgAnnouncement>(request, response));
			list = messageMsgService.findPartMySelfBySqlList(form);
		}else{
			page = messageMsgService.findMyCreateMsgByUserId(form, new Page<MsgAnnouncement>(request, response));
			list = messageMsgService.findMyCreateMsgByUserId(form);
		}
		if(list!=null && !list.isEmpty()){
			model.addAttribute("msgList",page.getList());
			model.addAttribute("page",page);
			/*
			 * 消息数量分布
			 */
			Iterator<MsgAnnouncement> iter = list.iterator();
			while(iter.hasNext()){
				MsgAnnouncement msg = iter.next();
				if(msg.getMsgType()==1){// 全站公告数量
					activeNum+=1;
				}else if(msg.getMsgType()==2){// 部门公告数量
					partActiveNum+=1;
				}else if(msg.getMsgType()==3){// 渠道公告数量
					agentActiveNum+=1;
				}else if(msg.getMsgType()==4){// 约签公告数量
					engageActiveNum+=1;
				}else if(msg.getMsgType()==5){// 消息数量
					messActiveNum+=1;
				}
			}
			messageNum=list.size();
		}
		model.addAttribute("messageNum",messageNum);
		model.addAttribute("activeNum",activeNum);
		model.addAttribute("partActiveNum",partActiveNum);
		model.addAttribute("agentActiveNum",agentActiveNum);
		model.addAttribute("engageActiveNum",engageActiveNum);
		model.addAttribute("messActiveNum",messActiveNum);
		model.addAttribute("form",form);
		
		return "modules/message/messageMySaveList";
	}
	
	/**
	 * ajax查询单条 公告/消息 预览
	 * @author gao
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="findMsgAjax/{id}", method=RequestMethod.POST)
	public Map<String,Object> findMsgAjax(@PathVariable("id") Long id){
		Map<String,Object> map = new HashMap<String,Object>();
		MsgMark msgMark = messageMsgService.findMsgMarkById(Long.valueOf(id));
		MsgAnnouncement msg = messageMsgService.findMsgById(msgMark.getMsgAnnouncementId());
		if(msg!=null){
			map.put("res", "success");
			map.put("msg", msg);
			// 获取下载文件
			List<DocInfo> docList = getDocInfoList(msg);
			if(docList!=null && !docList.isEmpty()){
				map.put("docList", docList);
			}
			// 发布者
			User user  = msg.getCreateBy();
			map.put("user", user);
		}else{
			map.put("res", "error");
			map.put("message", "没找到这条消息");
		}
		return map;
	}
	
	/**
	 * ajax查询单条 修改公告/草稿 预览
	 * @author gao
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="findMsgSaveAjax/{id}", method=RequestMethod.POST)
	public Map<String,Object> findMsgSaveAjax(@PathVariable("id") Long id){
		Map<String,Object> map = new HashMap<String,Object>();
		MsgAnnouncement msg = messageMsgService.findMsgById(id);
		if(msg!=null){
			map.put("res", "success");
			map.put("msg", msg);
			// 获取下载文件
			List<DocInfo> docList = getDocInfoList(msg);
			if(docList!=null && !docList.isEmpty()){
				map.put("docList", docList);
			}
			// 发布者
			User user  = msg.getCreateBy();
			map.put("user", user);
		}else{
			map.put("res", "error");
			map.put("message", "没找到这条消息");
		}
		return map;
	}
	
	/**
	 * 查找约签公告
	 * @param response
	 * @param model
	 * @param request
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="findVisaAjax", method=RequestMethod.POST)
	public Map<String,Object> findVisaAjax(HttpServletRequest req){
		// 判断session 是否过期
		 Map<String,Object> map = new HashMap<String,Object>();
		HttpSession session = req.getSession(false);
		if(session==null){
			map.put("res", "error");
			map.put("message", "session 已经失效");
			return map;
		}
		
		 MsgMark msgEngage = messageMsgService.findMsgByCompanyId();
		 if(msgEngage!=null){
			 map.put("res", "success");
			 map.put("msgTitle", msgEngage.getTitle());
			 map.put("msgId",msgEngage.getId());
			 map.put("ifRead", msgEngage.getIfRead());
		 }
		 return map;
	}
	
	/**
	 * 跳转至公告列表页详情页
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="goToMessageInfo/{msgId}")
	public String goToMessageInfo(@PathVariable("msgId") Long msgId,HttpServletResponse response,
	        Model model, HttpServletRequest request){
	//	String msgId = request.getParameter("msgId");
		if(msgId!=null){
			MsgMark msgMark = messageMsgService.findMsgMarkById(Long.valueOf(msgId));
			MsgAnnouncement msg = messageMsgService.findMsgById(msgMark.getMsgAnnouncementId());
			if(msg!=null){
				model.addAttribute("msg",msg);
				// 获取下载文件
				List<DocInfo> docList = getDocInfoList(msg);
				if(docList!=null && !docList.isEmpty()){
					model.addAttribute("docList", docList);
				}
				User user  = msg.getCreateBy();
				model.addAttribute("user", user);
				// 修正该公告为已读
				List<Long> idList = new ArrayList<Long>();
				idList.add(Long.valueOf(msgId));
				model.addAttribute("changeNum",ifRead(idList));
				// 如果为约签公告，需要清理缓存
				if(msg.getMsgType()==Context.MSG_TYPE_ENGAGE){
					// key
					String key = messageMsgService.backKey(msg.getCompanyId(), UserUtils.getUser().getId());
					if(StringUtils.isNotBlank(key)){
						// 清掉缓存
						messageMsgService.cleanCache(key);
					}
				}
			}
		}
		return "modules/message/messageInfo";
	}
	
	/**
	 * 跳转至我发布的公告详情页
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	*/
	@RequestMapping(value ="goToSaveMessageInfo/{msgId}")
	public String goToSaveMessageInfo(@PathVariable("msgId") Long msgId,HttpServletResponse response,
	        Model model, HttpServletRequest request){
		if(msgId!=null){
			MsgAnnouncement msg = messageMsgService.findMsgById(msgId);
			if(msg!=null){
				model.addAttribute("msg",msg);
				// 获取下载文件
				List<DocInfo> docList = getDocInfoList(msg);
				if(docList!=null && !docList.isEmpty()){
					model.addAttribute("docList", docList);
				}
				User user  = msg.getCreateBy();
				model.addAttribute("user", user);
				// 修正该公告为已读
				List<Long> idList = new ArrayList<Long>();
				idList.add(Long.valueOf(msgId));
				model.addAttribute("changeNum",ifRead(idList));
			}
		}
		return "modules/message/messageInfo";
	} 
	/**
	 * 批量修改公告、消息的已读状态
	 * @author gao
	 * @param ids MsgUserMark 主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="changMsgListAjax/{type}", method=RequestMethod.POST)
	public Map<String,Object> changMsgListAjax(@PathVariable("type") String type, HttpServletResponse response,HttpServletRequest request){
		String[] ids = request.getParameterValues("ids"); // 被修改的id组
		Map<String,Object> map = new HashMap<String,Object>();
		if(ids==null || ids.length<1){
			map.put("res", "error");
			map.put("message", "未选中任何项。");
			return map;
		}
		List<Long> longIds = new ArrayList<Long>();
		for(String id : ids){
			if(StringUtils.isNotBlank(id)){
				 longIds.add(Long.valueOf(id));
			}
		}
		map.put("res", "success");
		map.put("changNum", Integer.parseInt(type) == 0 ? ifRead(longIds) : isShow(longIds));
		return map;
	}

	/**
	 * 根据主键批量修改消息、公告状态 （0:保存；1：发布；2：已删除；3：已过期）
	 * @author gao
	 * @param ids MsgAnnouncement 主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="changeMsgStatusListAjax", method=RequestMethod.POST)
	public Map<String,Object> changeMsgStatusListAjax(HttpServletResponse response,HttpServletRequest request){
		
		String[] ids = request.getParameterValues("ids"); // 被修改的id组
		String status = request.getParameter("status");	// 修改状态
		Map<String,Object> map = new HashMap<String,Object>();

		if(ids==null || ids.length<1){
			map.put("res", "error");
			map.put("message", "未选中任何项。");
			return map;
		}
		int num = 0;
		if(ids!=null && ids.length>0){
			for(String id : ids){
				MsgAnnouncement msg = messageMsgService.findMsgById(Long.valueOf(id));
				if(msg!=null && messageMsgService.changeMsgStatus(msg, Integer.valueOf(status))){
					num+=1;
				}
			}
			map.put("res", "success");
			map.put("changeNum", num);
		}else{
			map.put("res", "error");
			
		}
		return map;
	}
	
	/**
	 * 根据 公告、消息的id组，将其置为已读状态
	 * @author gao
	 * @param ids 
	 * @return
	 */
	private int ifRead(List<Long> ids){
		int num = 0; // 修改个数
		if(ids!=null && !ids.isEmpty()){
			for(Long id : ids){
				boolean back = messageMsgService.changeMsgRead(id);
				if(back){
					num+=1;
				}
			}
		}
		return num;
	}

	/**
	 * 根据 提醒的id组，将其置为已还
	 * @date 2016年4月9日
	 * @param ids
	 * @return
	 */
	private int isShow(List<Long> ids){
		int num = 0; // 修改个数
		if(ids!=null && !ids.isEmpty()){
			for(Long id : ids){
				boolean back = messageMsgService.changeMsgShow(id);
				if(back){
					num+=1;
				}
			}
		}
		return num;
	}

	/**
	 * 获取公告附件列表
	 * @author gao
	 * @param msg
	 * @return
	 */
	private List<DocInfo> getDocInfoList(MsgAnnouncement msg){
		// 获取下载文件
		if(StringUtils.isNotBlank(msg.getDocinfoIds())){
			String docinfoIds = msg.getDocinfoIds();
			String[] docinfoId = docinfoIds.split(",");
			List<DocInfo> docList = new ArrayList<DocInfo>();
			for(String theId : docinfoId){
				DocInfo info = docInfoService.getDocInfo(Long.valueOf(theId));
				if(info!=null){
					docList.add(info);
				}
			}
			return docList;
		}
		return null;
	}
}


