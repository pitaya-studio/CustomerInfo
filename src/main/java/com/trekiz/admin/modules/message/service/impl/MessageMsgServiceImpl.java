package com.trekiz.admin.modules.message.service.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.entity.MsgMark;
import com.trekiz.admin.modules.message.entity.MsgToDepartment;
import com.trekiz.admin.modules.message.entity.MsgUserMark;
import com.trekiz.admin.modules.message.form.MsgAnnouncementForm;
import com.trekiz.admin.modules.message.form.MsgSelectForm;
import com.trekiz.admin.modules.message.repository.MsgAnnouncementDao;
import com.trekiz.admin.modules.message.repository.MsgMarkDao;
import com.trekiz.admin.modules.message.repository.MsgUserMarkDao;
import com.trekiz.admin.modules.message.service.*;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("messageMsgService")
@Transactional(readOnly = true)
public class MessageMsgServiceImpl extends BaseService implements  MessageMsgService{

	private static final Logger logger =LoggerFactory.getLogger(MessageMsgService.class);
	@Autowired
	public  MsgAnnouncementDao msgAnnouncementDao;
	@Autowired
	public  MsgMarkDao msgMarkDao;
	@Autowired
	public  UserDao userDao;
	@Autowired
	public  OfficeDao officeDao;
	@Resource
	private MsgUserMarkService msgUserMarkService;
	@Resource
	private MsgToDepartmentService msgToDepartmentService;
	@Resource
	private AgentinfoService agentinfoService;
	@Autowired
	private MsgUserMarkDao msgUserMarkDao;
	@Resource
	private HttpClientService httpClientService;
	@Resource
	private MessageService messageService;
	
	/**
	 * 添加公告
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addMsg(MsgAnnouncementForm form) {
		Map<String, Object> map = new HashMap<String,Object>();
		// 创建公告
		MsgAnnouncement msg = msgAnnouncementDao.save(backMsgAnnouncement(form));
		msgAnnouncementDao.flush();
		if(msg==null ){
			map.put("res", "error");
			map.put("message", "增加公告失败，MsgAnnouncement 保存失败");
			return map;
		}
		
		this.saveLogOperate(Context.log_type_notice, Context.log_type_notice_name,
				"增加‘MsgAnnouncement’类实体 成功！新增id为：" + msg.getId(), Context.log_state_add, null, null);
		// 如果为部门公告，需要添加部门关联
		Long[] departmentIds = null;
		if(msg.getMsgType()==Context.MSG_TYPE_PART){
			departmentIds =form.getDepartmentIds();
			for(Long depId : departmentIds){
				if(depId!=null){
					MsgToDepartment msd = new MsgToDepartment();
					msd.setDepartmentId(depId);
					msd.setMsgId(msg.getId());
					msgToDepartmentService.addMsgToDepartment(msd);
				}
			}
		}
		// 如果为草稿，则无需关联发布
		if(form.getSaveStatus()==Context.MESSAGE_STATUS_SAVE){
			map.put("res", "success");
			map.put("message", "增加草稿成功");
			return map;
		}
		
		//增加公告类和接受人关联类
		Integer type = msg.getMsgType();
		List<User> complist= new ArrayList<User>();
		if(Context.MSG_TYPE_ALL == type){ // 全站公告（包括全公司人员和全部渠道商的全部人员）
			// 获取全公司人员
			Map<String,Object> backMap = findOfficeUserList(msg.getCompanyId());
			complist = (backMap!=null?((List<User>)backMap.get("userList")):new ArrayList<User>());
			// 获取全部渠道商下属的全部user
			complist.addAll(findAllAgentUserList());
		}else if(Context.MSG_TYPE_PART == type){ // 部门公告（包括指定部门人员，如无部门，则是全公司人员，如不选部门，则返回错误）
			// 获取指定部门，如果没有部门，则指定全公司
			if(departmentIds!=null && departmentIds.length>0){  // 获取指定部门
				for(Long depId : departmentIds){
					complist.addAll(findDepartmentIUserList(depId));
				}
			}else{ // 没有部门，则指定为全公司
				// 获取全公司人员
				Map<String,Object> backMap = findOfficeUserList(msg.getCompanyId());
				complist = (backMap!=null?((List<User>)backMap.get("userList")):new ArrayList<User>());
			}
		}else if(Context.MSG_TYPE_AGINE == type){ // 渠道商公告（公司的全部渠道商的全部人员）
			// 获取全部渠道商下属的全部user
			complist.addAll(findAllAgentUserList());
		}else if(Context.MSG_TYPE_ENGAGE == type){ // 约签公告 （全公司约签）
			// 获取全公司人员
			Map<String,Object> backMap = findOfficeUserList(msg.getCompanyId());
			complist = (backMap!=null?((List<User>)backMap.get("userList")):new ArrayList<User>());
			// 获取全部渠道商下属的全部user
			complist.addAll(findAllAgentUserList());
		}
		// 去重并发布公告
		uniqueList(complist,msg);
		
		// 如果为约签公告，则写入缓存
		if(msg.getMsgType()==Context.MSG_TYPE_ENGAGE){
			List<MsgMark> msgEngage = msgMarkDao.findByCompanyID(msg.getCompanyId(),msg.getId());
			if(msgEngage!=null && !msgEngage.isEmpty()){
				Iterator<MsgMark> iter = msgEngage.iterator();
				while(iter.hasNext()){
					MsgMark ms = iter.next();
					//System.out.println("userID:"+ms.getUserId());
						// 生成key
						String key = backKey(ms.getCompanyId(),ms.getUserId());
						//System.out.println("key:"+key);
						if(StringUtils.isNotBlank(key)){
							// 清掉缓存中原来的记录
							if(CacheUtils.get(key)!=null){
								CacheUtils.remove(key);
							}
							// 放入缓存
							CacheUtils.put(key, ms);
						}
				}
			}
		}
		
		map.put("res", "success");
		map.put("message", "增加公告成功");
		return map;
	}
	/**
	 * 修正公告
	 * @author gao
	 * @param form 修正项
	 * @param id 原公告ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> editMsg(MsgAnnouncementForm form) {
		Map<String, Object> map = new HashMap<String,Object>();
		// 获取公告
		MsgAnnouncement msg = msgAnnouncementDao.findOne(form.getMsgId());
		// 如果未找到原有公告或草稿，则返回错误
		if(msg==null){
			map.put("res", "error");
			map.put("message", "公告修改失败，没找到原公告或草稿");
			return map;
		}

		// 使用新参数创建公告
		MsgAnnouncement msgNew = msgAnnouncementDao.save(backMsgAnnouncement(form));
		msgAnnouncementDao.flush();
		if(msgNew==null ){
			map.put("res", "error");
			map.put("message", "增加公告失败，MsgAnnouncement 保存失败");
			return map;
		}
		// 日志
		this.saveLogOperate(Context.log_type_notice, Context.log_type_notice_name,
				"增加‘MsgAnnouncement’类实体 成功！新增id为："+msgNew.getId(), Context.log_state_add, null, null);
		// 如果为部门公告，需要添加部门关联
		Long[] departmentIds = null;
		if(msgNew.getMsgType()==Context.MSG_TYPE_PART){
			departmentIds =form.getDepartmentIds();
			if(departmentIds!=null && departmentIds.length>0){
				for(Long depId : departmentIds){
					if(depId!=null){
						MsgToDepartment msd = new MsgToDepartment();
						msd.setDepartmentId(depId);
						msd.setMsgId(msg.getId());
						msgToDepartmentService.addMsgToDepartment(msd);
					}
				}
			}
		}
		// 将原公告或草稿状态置为删除
		msgAnnouncementDao.delMsgAnnouncement(msg.getId());
		
		//增加公告类和接受人关联类
		Integer type = msgNew.getMsgType();
		List<User> complist= new ArrayList<User>();
		if(Context.MSG_TYPE_ALL == type){ // 全站公告（包括全公司人员和全部渠道商的全部人员）
			// 获取全公司人员
			Map<String,Object> backMap = findOfficeUserList(msgNew.getCompanyId());
			complist = (backMap!=null?((List<User>)backMap.get("userList")):new ArrayList<User>());
			// 获取全部渠道商下属的全部user
			complist.addAll(findAllAgentUserList());
		}else if(Context.MSG_TYPE_PART == type){ // 部门公告（包括指定部门人员，如无部门，则是全公司人员，如不选部门，则返回错误）
			// 获取指定部门，如果没有部门，则指定全公司
			if(departmentIds!=null && departmentIds.length>0){  // 获取指定部门
				for(Long depId : departmentIds){
					complist.addAll(findDepartmentIUserList(depId));
				}
			}else{ // 没有部门，则指定为全公司
				// 获取全公司人员
				Map<String,Object> backMap = findOfficeUserList(msgNew.getCompanyId());
				complist = (backMap!=null?((List<User>)backMap.get("userList")):new ArrayList<User>());
			}
		}else if(Context.MSG_TYPE_AGINE == type){ // 渠道商公告（公司的全部渠道商的全部人员）
			// 获取全部渠道商下属的全部user
			complist.addAll(findAllAgentUserList());
		}else if(Context.MSG_TYPE_ENGAGE == type){ // 约签公告 （全公司约签）
			// 获取全公司人员
			Map<String,Object> backMap = findOfficeUserList(msgNew.getCompanyId());
			complist = (backMap!=null?((List<User>)backMap.get("userList")):new ArrayList<User>());
			// 获取全部渠道商下属的全部user
			complist.addAll(findAllAgentUserList());
		}
		// 去重并发布公告
		uniqueList(complist,msgNew);
		
		map.put("res", "success");
		map.put("message", "公告修改成功");
		return map;
	}
	
	/**
	 * 根据公司ID获取下属全部user
	 * @param officeId
	 * @param map
	 * @return
	 */
	private Map<String,Object> findOfficeUserList(Long officeId){
		Office office =  new Office();
		Map<String,Object> map = new HashMap<String,Object>();
		// 判断公司是否存在
		if(officeId!=null){
			office = officeDao.findOne(officeId);
			if(office==null){
				map.put("res", "error");
				map.put("message", "增加公告失败，公司ID="+officeId+"的公司不存在");
				return map;
			}
		}else{
			map.put("res", "error");
			map.put("message", "增加公告失败，公司ID="+officeId+"的公司不存在");
			return map;
		}
		// 获取公司下属全部user
		List<User> userList = userDao.getUserByCompany(office);
		if(!userList.isEmpty()){
			map.put("userList", userList);
			return map;
		}
		return null;
	}
	/**
	 * 获取指定部门下全体User
	 * @param departmentId
	 * @return
	 */
	private List<User> findDepartmentIUserList(Long departmentId){
		List<Long> userIds = userDao.findByUserDept(departmentId);
		List<User> list = null;
		if(userIds!=null && !userIds.isEmpty()){
			list = userDao.findByIds(userIds);
		}
		return list;
	}
	/**
	 * 返回全部渠道商的下属user
	 * @return
	 */
	private List<User> findAllAgentUserList(){
		List<User> list = new ArrayList<User>();
		// 获取全渠道商
		List<Agentinfo> agentList = agentinfoService.findAllAgentinfo();
		if(!agentList.isEmpty()){
			for(Agentinfo age : agentList){
				list.addAll(userDao.findByAgentId(age.getId()));
			}
		}
		return list;
	}
	/**
	 * 去掉重复值
	 * @param list
	 * @return
	 */
	private HashSet<User> uniqueList(List<User> list,MsgAnnouncement msg){
		HashSet<User> userSet = new HashSet<User>(list);
		// 发布公告到user
		int num = 0;
		int allnum = userSet.size();
		StringBuffer userNames = new StringBuffer("");
		for(User u : userSet){
			MsgUserMark muMark = msgUserMarkService.addMsgUserMark(msg, u.getId());
			if(muMark==null){
				num+=1;
				userNames.append(u.getName()+"  ");
				logger.error("公告通知失败! 公告实体ID:"+msg.getId()+", 接收人ID:"+u.getId()+", 发布时间:"+new Date());
			}
		}
		// 如果有发送失败的公告，则需要给发布人返回失败通知
		if(num>0){
			addMessage(num,allnum,userNames.toString(),msg.getTitle(),msg.getId());
		}
		return userSet;
	}
	/**
	 * 公告通知详情
	 * @author gao
	 * @param num 通知失败数量
	 * @param allnum 总计通知数量
	 * @param userIds 通知失败用户ID组 
	 * @param title 公告标题
	 * @param id 公告id
	 * @return
	 */
	private void addMessage(int num,int allnum,String userNames,String title,Long id){
		MsgAnnouncement msg = new MsgAnnouncement();
		msg.setTitle("公告发布失败详情");
		String content = new String("公告ID:"+id+", 公告标题："+title+", 总计发布通知"+allnum+"条。");
		if(num>0){
			content+="其中通知失败"+num+"条。失败用户如下："+userNames;
		}
		msg.setContent(content);
		msg.setMsgType(Context.MSG_TYPE_MESSAGE);
		msg.setMsgNoticeType(Context.ORDER_TYPE_OT);
		msg.setStatus(Context.MESSAGE_STATUS_ISSUE);
		msg.setCompanyId(UserUtils.getUser().getCompany().getId());
		// 保存该通知
		MsgAnnouncement back = msgAnnouncementDao.save(msg);
		
		ArrayList<Long> ids = new ArrayList<Long>();
		ids.add(UserUtils.getUser().getId());
		messageService.saveMessage(back, ids);
	}
	
	/**
	 * 创建公告
	 * @param form 
	 * @param user
	 * @param departmentId 部门ID
	 * @param msgType 公告类型
	 * @return
	 */
	private MsgAnnouncement backMsgAnnouncement(MsgAnnouncementForm form){
		MsgAnnouncement msg = new MsgAnnouncement();
		msg.setTitle(form.getTitle());
		if(StringUtils.isNotBlank(form.getTitleLightCss())){
			msg.setTitleLightCss(Integer.valueOf(form.getTitleLightCss()));
		}
		if(StringUtils.isNotBlank(form.getTitleVulgarCss())){
			msg.setTitleVulgarCss(Integer.valueOf(form.getTitleVulgarCss()));
		}
		msg.setContent(form.getContent());
		msg.setContentinfo(form.getIndex());
		msg.setCompanyId(UserUtils.getUser().getCompany().getId());
		if(form.getSaveStatus()==0){
			msg.setStatus(Context.MESSAGE_STATUS_SAVE);
		}else{
			msg.setStatus(Context.MESSAGE_STATUS_ISSUE);
		}
		// 类型，1：全站公告；2：部门公告;3：渠道公告; 4：约签； 5:消息；
		msg.setMsgType(form.getMsgType());
		msg.setMsgNoticeType(form.getMsgNoticeType());
		// 附件id集合
		Long[] doc =  form.getDocinfoIds();
		if(doc!=null&&doc.length>0){
			StringBuffer docStr = new StringBuffer();
			for(int n=0;n<doc.length;n++){
				if(doc[n]!=null && doc[n]>0){
					docStr.append(doc[n]);
					if(n<doc.length-1){
						docStr.append(",");
					}
				}
			}
			msg.setDocinfoIds(docStr.toString());
		}
		if(form.getOverTime()!=null){
			msg.setOverTime(DateUtils.parseDate(form.getOverTime()));
		}
		
		return msg;
	}
	/**
	 * 修正公告状态： 0:保存；1：发布；2：已删除；3：已过期
	 */
	@Override
	public boolean changeMsgStatus(MsgAnnouncement msg,
			Integer status) {
		if(msg!=null){
			msgAnnouncementDao.updateMsgAnnouncement(status,msg.getId());
			return true;
		}else{
			// 该记录不存在
			return false;
		}
	}

	@Override
	public Integer getMsgNum(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修正公告/消息 状态为已读
	 */
	@Override
	public boolean changeMsgRead(Long msgId) {
		if(msgId!=null){
			msgUserMarkDao.readById(msgId,new Date());
			return true;
		}
		return false;
	}

	@Override
	public boolean changeMsgShow(Long msgId) {
		if(msgId!=null){
			msgUserMarkDao.showById(msgId);
			return true;
		}
		return false;
	}

	/**
	 * 分页查找消息、公告列表
	 * gao
	 */
	@Override
	public Page<MsgMark> findMsgByUserId(MsgSelectForm form,Page<MsgMark> page) {
		DetachedCriteria dc = addDetachedCriteria(form);
		return msgMarkDao.find(page, dc);
	}
	/**
	 * 分页查找自己创建的公告列表
	 * gao
	 */
	@Override
	public Page<MsgAnnouncement> findMyCreateMsgByUserId(MsgSelectForm form,Page<MsgAnnouncement> page) {
		DetachedCriteria dc = msgAnnouncementDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(form.getStartDate()) && StringUtils.isNotBlank(form.getEndDate())){
			dc.add(Restrictions.between("createDate", DateUtils.dateFormat(form.getStartDate(),"yyyy-MM-dd"), DateUtils.dateFormat(form.getEndDate(),"yyyy-MM-dd")));
		}
		if(form.getIfRead()!=null){
			dc.add(Restrictions.eq("ifRead", form.getIfRead()));
		}
		if(StringUtils.isNotBlank(form.getConn())){
//			dc.add(Restrictions.like("content", "%"+form.getConn()+"%"));
//			dc.add(Restrictions.like("title", "%"+form.getConn()+"%"));
			dc.add(
					Restrictions.or(
							Restrictions.like("content", "%"+form.getConn()+"%"),
							Restrictions.like("title", "%"+form.getConn()+"%")
					)
			);
		}
		dc.add(Restrictions.eq("createBy",UserUtils.getUser()));
		if(Context.MESSAGE_STATUS_SAVE == form.getSaveStatus()){// 0:只查看草稿
			dc.add(Restrictions.eq("status", Context.MESSAGE_STATUS_SAVE));
		}else{// 1:只看到已发布的消息   0 :表示显示草稿 2:已删除消息，3：已过期
			dc.add(Restrictions.between("status", 1,3));
		}
		if(form.getMsgType()!=0){
			dc.add(Restrictions.eq("msgType", form.getMsgType())); // 类型  1：全站公告；2：部门公告;3：渠道公告; 4：约签公告；5:消息；
		}else{
			dc.add(Restrictions.between("msgType", 1, 4));
		}
		if(form.getMsgNoticeType()!=null){
			dc.add(Restrictions.eq("msgNoticeType", form.getMsgNoticeType()));// 1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：其他；
		}
		//dc.addOrder(Order.desc("createDate"));// 按发布时间排序
		dc.addOrder(Order.asc("status"));// 按照状态排序
		dc.addOrder(Order.desc("id"));// 按主键倒叙排序
		return msgAnnouncementDao.find(page, dc);
	}
	/**
	 * 查找自己创建的公告列表
	 * gao
	 */
	@Override
	public List<MsgAnnouncement> findMyCreateMsgByUserId(MsgSelectForm form) {
		DetachedCriteria dc = msgAnnouncementDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(form.getStartDate()) && StringUtils.isNotBlank(form.getEndDate())){
			dc.add(Restrictions.between("createDate", DateUtils.dateFormat(form.getStartDate(),"yyyy-MM-dd"), DateUtils.dateFormat(form.getEndDate(),"yyyy-MM-dd")));
		}
		if(form.getIfRead()!=null){
			dc.add(Restrictions.eq("ifRead", form.getIfRead()));
		}
		if(StringUtils.isNotBlank(form.getConn())){
//			dc.add(Restrictions.like("content", "%"+form.getConn()+"%"));
//			dc.add(Restrictions.like("title", "%"+form.getConn()+"%"));
			dc.add(
					Restrictions.or(
							Restrictions.like("content", "%"+form.getConn()+"%"),
							Restrictions.like("title", "%"+form.getConn()+"%")
					)
			);
		}

		dc.add(Restrictions.eq("createBy",UserUtils.getUser()));
		dc.add(Restrictions.between("msgType", 1, 4)); // 类型只统计 1：全站公告；2：部门公告;3：渠道公告; 4:约签公告
		if(Context.MESSAGE_STATUS_SAVE == form.getSaveStatus()){// 0:只查看草稿
			dc.add(Restrictions.eq("status", Context.MESSAGE_STATUS_SAVE));
		}else{
			dc.add(Restrictions.between("status", 1,3));// 1:只看到已发布的消息   0 :表示显示草稿 2:已删除消息，3：已过期
		}
		return msgAnnouncementDao.find(dc);
	}
	
	/**
	 * 查找消息、公告列表
	 * gao
	 */
	@Override
	public List<MsgMark> findMsgByUserId(MsgSelectForm form) {
		DetachedCriteria dc = msgMarkDao.createDetachedCriteria();
		dc.add(Restrictions.eq("userId",UserUtils.getUser().getId()));
		dc.add(Restrictions.eq("status", 1));// 1:只看到已发布的消息   0 :表示显示草稿 2:已删除消息，3：已过期
		if(form.getMsgType()==7){
			dc.add(Restrictions.eq("isShow", 0));
		}
		return msgMarkDao.find(dc);
	}
	/**
	 * 插入查找公告、消息列表查询条件
	 * @author gao
	 * @param form
	 * @return
	 */
	private DetachedCriteria  addDetachedCriteria(MsgSelectForm form){
		DetachedCriteria dc = msgMarkDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(form.getStartDate()) && StringUtils.isNotBlank(form.getEndDate())){
			dc.add(Restrictions.between("createDate", DateUtils.dateFormat(form.getStartDate(),"yyyy-MM-dd"), DateUtils.dateFormat(form.getEndDate(),"yyyy-MM-dd")));
		}
		if(form.getIfRead()!=null){
			dc.add(Restrictions.eq("ifRead", form.getIfRead()));
		}
		if(StringUtils.isNotBlank(form.getConn())){
			dc.add(
					Restrictions.or(
							Restrictions.like("content", "%"+form.getConn()+"%"),
							Restrictions.like("title", "%"+form.getConn()+"%")
					)
			);
		}
		dc.add(Restrictions.eq("userId",UserUtils.getUser().getId()));
		dc.add(Restrictions.eq("status", 1));// 1:只看到已发布的消息   0 :表示显示草稿 2:已删除消息，3：已过期
		if(form.getMsgType()!=0){
			dc.add(Restrictions.eq("msgType", form.getMsgType())); // 类型  1：全站公告；2：部门公告;3：渠道公告; 4：约签公告；5:消息；
		}else{
			dc.add(Restrictions.between("msgType", 1, 6));
		}
		if(form.getMsgNoticeType()!=null){
			dc.add(Restrictions.eq("msgNoticeType", form.getMsgNoticeType()));// 1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：其他；
		}
		//270需求只涉及还款提醒
//		if(form.getMsgType() == 7 && form.getRemindType() != null){
//			dc.add(Restrictions.eq("remindType", form.getRemindType()));
//		}
		dc.add(Restrictions.eq("isShow", 0));	//是否提醒 0：提醒 1：不提醒 （不提醒的列表中没有数据）
		dc.addOrder(Order.asc("ifRead"));// 按已读状态排序
		//dc.addOrder(Order.desc("createDate"));// 按发布时间排序
		dc.addOrder(Order.desc("id"));// 按主键倒叙排序
		return dc;
	}


	/**
	 * 270 提醒列表标题拆分
	 *
	 * 形式如下：
	 *	单团借款订单#orderNo#未还款，还款日期还剩10天#Type(1)#orderId#reviewId
	 *	机票借款订单#orderNo#未还款，还款日期还剩10天#Type(2)#orderId#reviewId
	 *	签证(新行者)借款订单#orderNo#未还款，还款日期还剩10天#Type(3)#orderId#reviewId
	 *	签证借款批次#batchNo#未还款，还款日期还剩10天#Type(4)#reviewId
	 */

//	@Test
	public List splitRemindTitles(String title){
//		String title = "单团借款订单#orderNo#未还款，还款日期还剩10天#Type(1)#orderId#reviewId";
		String[] strArr = title.split("#");
		List titleList = new ArrayList();
		Map map = new HashMap();
		int type = Integer.parseInt(strArr[3]);
		if(strArr.length>0){
			if(type==4){
				map.put("type", type);
				map.put("batchNo", strArr[1]);
				map.put("reviewId", strArr[4]);
				titleList.add(map);
			}else{
				map.put("type", type);
				map.put("orderNo",strArr[1]);
				map.put("orderId",strArr[4]);
				map.put("reviewId",strArr[5]);
				titleList.add(map);
			}
		}
		return titleList;
	}

	@Override
	public String findRemindTitle() {
		Long userId = UserUtils.getUser().getId();
		return msgMarkDao.findRemindTitleByUserId(userId);
	}

	/**
	 * 查找单条消息、公告
	 * gao 
	 */
	@Override
	public MsgAnnouncement findMsgById(Long id){
		return msgAnnouncementDao.findOne(id);
	}
	
	/**
	 * 按公司ID,接收人ID查找约签信息
	 */
	@Override
	public MsgMark findMsgByCompanyId() {
		// 先从缓存中查找约签公告
		MsgMark msg = new MsgMark();
		User user = UserUtils.getUser();
		// 生成key
		String key =backKey(user.getCompany().getId(),user.getId());
		if(StringUtils.isNotBlank(key)){
			msg = (MsgMark)CacheUtils.get(key);
		}
		if(msg==null){// 如果缓存中没有，则从数据库查找约签公告
			msg = msgMarkDao.findByCompanyIDOne(user.getCompany().getId(),user.getId());
			if(msg!=null){
				CacheUtils.put(key.toString(), msg);
			}
		}
		return msg;
	}
	/**
	 * 按照MsgMark id来获取实体
	 */
	@Override
	public MsgMark findMsgMarkById(Long id) {
		
		if(id!=null){
			MsgMark msgMark = msgMarkDao.findOne(id);
			if(msgMark!=null){
				return msgMark;
			}
		}
		return null;
	}
	/**
	 * 通过MsgAnnouncement的ID和登录用户ID，返回对应的MsgUserMarkId
	 */
	@Override
	public Long findMsgUserMarkIdByMsgU(Long msgId){
		if(msgId!=null){
			Long id = msgUserMarkDao.findMsgUserMarkIdByMsgU(msgId, UserUtils.getUser().getId());
			return id;
		}else{
			return null;
		}
	}
	/**
	 * 清掉缓存
	 */
	@Override
	public void cleanCache(String key){
		// 清掉缓存
		CacheUtils.remove(key);
	}

	/**
	 * 生成约签缓存key，标记结构： CACHE_NAME_companyId_userID (key标记 _ 约签发布公司ID _ 约签接收人ID)
	 * @author gao
	 * @param companyId
	 * @param userId
	 * @return
	 */
	@Override
	public  String backKey(Long companyId,Long userId){
		if(companyId!=null && userId!=null){
			StringBuffer key = new StringBuffer(Context.CACHE_NAME);
			key.append(companyId);
			key.append("_");
			key.append(userId);
			return key.toString();
		}
		return null;
	}
	/**
	 * 连表查询(部门分类)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<MsgMark> findPartBySql(MsgSelectForm form, Page<MsgMark> page) {
		String str = backPartSql(form);
		if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = backPartSqlCount(form); 
	        Query query =  msgMarkDao.getSession().createSQLQuery(countSqlString);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
        SQLQuery query = msgMarkDao.getSession().createSQLQuery(str);
        List<MsgMark> msgList = new ArrayList<MsgMark>();
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
	        
	        msgList = getMsgMarkList(query, msgList);
        }
        if(msgList!=null && !msgList.isEmpty()){
            page.setList(msgList);
        }
		return page;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MsgMark> findPartBySqlList(MsgSelectForm form) {
		String str = backPartSql(form);
		SQLQuery query =  msgMarkDao.getSession().createSQLQuery(str);
		List<Object[]> objList = query.list();
		List<MsgMark> msgList = new ArrayList<MsgMark>();
		if(objList!=null && !objList.isEmpty()){
			 msgList = getMsgMarkList(query, msgList);
		}
		return msgList;
	}
	/**
	 * 连表查询(部门分类) 装配 List<MsgMark>
	 * @author gao
	 * @param query
	 * @param msgList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<MsgMark> getMsgMarkList(SQLQuery query,List<MsgMark> msgList){
		List<Object[]> pageList = query.list();
		Iterator<Object[]> iter = pageList.iterator();
		while(iter.hasNext()){
        	MsgMark msg = new MsgMark();
        	Object[] obj = iter.next();
        	if(obj[0]!=null){
		        msg.setId(Long.valueOf(obj[0].toString()));
        	}
        	if(obj[1]!=null){
		        msg.setMsgId(Long.valueOf(obj[1].toString()));
        	}
        	if(obj[2]!=null){
		        msg.setTitle(obj[3].toString());
        	}
        	if(obj[3]!=null){
		        msg.setTitleVulgarCss(Integer.valueOf(obj[3].toString()));
        	}
        	if(obj[4]!=null){
		        msg.setTitleLightCss(Integer.valueOf(obj[4].toString()));
        	}
        	if(obj[5]!=null){
		        msg.setContent(obj[0].toString());
        	}
        	if(obj[6]!=null){
		        msg.setContentinfo(obj[6].toString());
        	}
        	if(obj[7]!=null){
		        msg.setContentUrl(obj[7].toString());
        	}
        	if(obj[8]!=null){
		        msg.setStatus(Integer.valueOf(obj[8].toString()));
        	}
        	if(obj[9]!=null){
		        msg.setCompanyId(Long.valueOf(obj[9].toString()));
        	}
        	if(obj[10]!=null){
		        msg.setAgntinfoId(Long.valueOf(obj[10].toString()));
        	}
        	if(obj[11]!=null){
		        msg.setMsgType(Integer.valueOf(obj[11].toString()));
        	}
        	if(obj[12]!=null){
		        msg.setMsgNoticeType(Integer.valueOf(obj[12].toString()));
        	}
        	if(obj[13]!=null){
		        msg.setOverTime(DateUtils.dateFormat(obj[13].toString(),"yyyy-MM-dd"));
        	}
        	if(obj[14]!=null){
		        msg.setDocinfoIds(obj[14].toString());
        	}
        	if(obj[15]!=null){
		        msg.setMsgAnnouncementId(Long.valueOf(obj[15].toString()));
        	}
        	if(obj[16]!=null){
		        msg.setUserId(Long.valueOf(obj[16].toString()));
        	}
        	if(Boolean.valueOf(obj[17].toString())){
		        msg.setIfRead(1);
        	}else{
        		msg.setIfRead(0);
        	}
        	if(obj[18]!=null){
		        msg.setReadTime(DateUtils.dateFormat(obj[18].toString(), "yyyy-MM-dd"));
        	}
        	if(obj[19]!=null){
		        msg.setMessageStatus(Integer.valueOf(obj[19].toString()));
        	}
        	if(obj[20]!=null){
		        msg.setCreateDate(DateUtils.dateFormat(obj[20].toString(),"yyyy-MM-dd"));
        	}
	        msgList.add(msg);
        }
		return msgList;
	}
	
	/**
	 * 连表查询自己发布的公告(部门分类)
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public Page<MsgAnnouncement> findPartMySelfBySql(MsgSelectForm form, Page<MsgAnnouncement> page) {
		String str = backPartMySelfSql(form);
		if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = backPartMySelfSqlCount(form); 
	        Query query =  msgAnnouncementDao.getSession().createSQLQuery(countSqlString);
			List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
        SQLQuery query = msgAnnouncementDao.getSession().createSQLQuery(str);
        List<MsgAnnouncement> msgList = new ArrayList<MsgAnnouncement>();
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
	        List<Object[]> pageList = query.list();
	        Iterator<Object[]> iter = pageList.iterator();
	        while(iter.hasNext()){
	        	MsgAnnouncement msg = new MsgAnnouncement();
	        	Object[] obj = iter.next();
		        	msg.setId(obj[0]!=null?Long.valueOf(obj[0].toString()):Long.valueOf(0));
		        	msg.setTitle(obj[1]!=null?obj[1].toString():"");
		        	msg.setTitleVulgarCss(obj[2]!=null?Integer.valueOf(obj[2].toString()):0);
		        	msg.setTitleLightCss(obj[3]!=null?Integer.valueOf(obj[3].toString()):0);
		        	msg.setContent(obj[4]!=null?obj[4].toString():"");
		        	msg.setContentinfo(obj[5]!=null?obj[5].toString():"");
		        	msg.setContentUrl(obj[6]!=null?obj[6].toString():"");
		        	msg.setStatus(obj[7]!=null?Integer.valueOf(obj[7].toString()):0);
		        	msg.setCompanyId(obj[8]!=null?Long.valueOf(obj[8].toString()):Long.valueOf(0));
		        	msg.setAgntinfoId(obj[9]!=null?Long.valueOf(obj[9].toString()):Long.valueOf(0));
		        	msg.setMsgType(obj[10]!=null?Integer.valueOf(obj[10].toString()):0);
		        	msg.setMsgNoticeType(obj[11]!=null?Integer.valueOf(obj[11].toString()):0);
		        	msg.setOverTime(obj[12]!=null?DateUtils.dateFormat(obj[12].toString(),"yyyy-MM-dd"):null);
		        	msg.setDocinfoIds(obj[13]!=null?obj[13].toString():"");
		        	msg.setCreateDate(obj[14]!=null?DateUtils.dateFormat(obj[14].toString(), "yyyy-MM-dd"):null);
		        	msgList.add(msg);
	        }
        }
        if(msgList!=null && !msgList.isEmpty()){
            page.setList(msgList);
        }
		return page;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MsgAnnouncement> findPartMySelfBySqlList(MsgSelectForm form) {
		// TODO Auto-generated method stub
		String str = backPartMySelfSql(form);
		Query query =  msgAnnouncementDao.getSession().createSQLQuery(str);
		List<Object[]> objList = query.list();
		List<MsgAnnouncement> msgList = new ArrayList<MsgAnnouncement>();
		if(objList!=null && !objList.isEmpty()){
			Iterator<Object[]> iter = objList.iterator();
			while(iter.hasNext()){
				Object[] obj = iter.next();
				MsgAnnouncement msg = new MsgAnnouncement();
				msg.setId(obj[0]!=null?Long.valueOf(obj[0].toString()):Long.valueOf(0));
	        	msg.setTitle(obj[1]!=null?obj[1].toString():"");
	        	msg.setTitleVulgarCss(obj[2]!=null?Integer.valueOf(obj[2].toString()):0);
	        	msg.setTitleLightCss(obj[3]!=null?Integer.valueOf(obj[3].toString()):0);
	        	msg.setContent(obj[4]!=null?obj[4].toString():"");
	        	msg.setContentinfo(obj[5]!=null?obj[5].toString():"");
	        	msg.setContentUrl(obj[6]!=null?obj[6].toString():"");
	        	msg.setStatus(obj[7]!=null?Integer.valueOf(obj[7].toString()):0);
	        	msg.setCompanyId(obj[8]!=null?Long.valueOf(obj[8].toString()):Long.valueOf(0));
	        	msg.setAgntinfoId(obj[9]!=null?Long.valueOf(obj[9].toString()):Long.valueOf(0));
	        	msg.setMsgType(obj[10]!=null?Integer.valueOf(obj[10].toString()):0);
	        	msg.setMsgNoticeType(obj[11]!=null?Integer.valueOf(obj[11].toString()):0);
	        	msg.setOverTime(obj[12]!=null?DateUtils.dateFormat(obj[12].toString(),"yyyy-MM-dd"):null);
	        	msg.setDocinfoIds(obj[13]!=null?obj[13].toString():"");
	        	msg.setCreateDate(obj[14]!=null?DateUtils.dateFormat(obj[14].toString(), "yyyy-MM-dd"):null);
	        	msgList.add(msg);
			}
		}
		return msgList;
	}
	/**
	 * 连表查询各部门的公告
	 * @author gao
	 * @param form
	 * @return
	 
	private String backPartSql(MsgSelectForm form){
		StringBuffer str = new StringBuffer("select new MsgMark( ma.id,ma.msgId,ma.title,ma.titleVulgarCss,ma.titleLightCss,ma.content,"
				+ "ma.contentinfo,ma.contentUrl,ma.status,ma.companyId,ma.agntinfoId,ma.msgType,ma.msgNoticeType,"
				+ "ma.overTime,ma.docinfoIds,ma.msgAnnouncementId,ma.userId,ma.ifRead,ma.readTime,ma.messageStatus,"
				+ "ma.createDate ) "
				+ "from MsgMark ma,MsgToDepartment md where ma.msgId=md.msgId ");
		str.append(" and ma.userId= "+UserUtils.getUser().getId());
		if(StringUtils.isNotBlank(form.getStartDate())){
			str.append(" and  ma.createDate >= "+form.getStartDate());
		}
		if(StringUtils.isNotBlank(form.getEndDate())){
			str.append(" and  ma.createDate < "+form.getEndDate());
		}
		if(StringUtils.isNotBlank(form.getConn())){
			str.append(" and (ma.title like '%"+form.getConn()+"%' or ma.content like '%"+form.getConn()+"%') ");
		}
		if(form.getMsgType()!=null){
			str.append(" and ma.msgType = "+form.getMsgType());
		}
		if(form.getMsgNoticeType()!=null){
			str.append(" and ma.msgNoticeType= "+form.getMsgNoticeType());
		}
		if(form.getIfRead()!=null){
			str.append(" and ma.ifRead ="+form.getIfRead());
		}
		if(form.getSaveStatus()!=null){
			str.append(" and ma.status= "+form.getSaveStatus());
		}
		//  部门ID组
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			StringBuffer strid = new StringBuffer();
			for(int num=0;num<form.getDepIds().length;num++){
				strid.append(form.getDepIds()[num]);
				if((form.getDepIds().length-1)>num){
					strid.append(",");
				}
			}
			str.append(" and md.departmentId in ("+strid+")");
		}
		// 排列
		str.append(" order by ma.id desc ");
		return str.toString();
	}*/
	private String backPartSql(MsgSelectForm form){
		StringBuffer str = new StringBuffer("select distinct(ma.id),ma.msgId,ma.title,ma.titleVulgarCss,ma.titleLightCss,ma.content,"
				+ "ma.contentinfo,ma.contentUrl,ma.status,ma.companyId,ma.agntinfoId,ma.msgType,ma.msgNoticeType,"
				+ "ma.overTime,ma.docinfoIds,ma.msgAnnouncementId,ma.userId,ma.ifRead,ma.readTime,ma.messageStatus,"
				+ "ma.createDate "
				+ "from msg_mark_view ma,msg_to_department md where ma.msgId=md.msg_id ");
		str.append(" and ma.userId= "+UserUtils.getUser().getId());
		if(StringUtils.isNotBlank(form.getStartDate())){
			str.append(" and  ma.createDate >= "+form.getStartDate());
		}
		if(StringUtils.isNotBlank(form.getEndDate())){
			str.append(" and  ma.createDate < "+form.getEndDate());
		}
		if(StringUtils.isNotBlank(form.getConn())){
			str.append(" and (ma.title like '%"+form.getConn()+"%' or ma.content like '%"+form.getConn()+"%') ");
		}
		if(form.getMsgType()!=null){
			str.append(" and ma.msgType = "+form.getMsgType());
		}
		if(form.getMsgNoticeType()!=null){
			str.append(" and ma.msgNoticeType= "+form.getMsgNoticeType());
		}
		if(form.getIfRead()!=null){
			str.append(" and ma.ifRead ="+form.getIfRead());
		}
		if(form.getSaveStatus()!=null){
			str.append(" and ma.status= "+form.getSaveStatus());
		}
		//  部门ID组
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			StringBuffer strid = new StringBuffer();
			for(int num=0;num<form.getDepIds().length;num++){
				strid.append(form.getDepIds()[num]);
				if((form.getDepIds().length-1)>num){
					strid.append(",");
				}
			}
			str.append(" and md.department_id in ("+strid+")");
		}
		// 排列
		str.append(" order by ma.id desc ");
		return str.toString();
	}
	private String backPartSqlCount(MsgSelectForm form){
		StringBuffer str = new StringBuffer("select count(distinct(ma.id))"
				+ "from msg_mark_view ma,msg_to_department md where ma.msgId=md.msg_id ");
		str.append(" and ma.userId= "+UserUtils.getUser().getId());
		if(StringUtils.isNotBlank(form.getStartDate())){
			str.append(" and  ma.createDate >= "+form.getStartDate());
		}
		if(StringUtils.isNotBlank(form.getEndDate())){
			str.append(" and  ma.createDate < "+form.getEndDate());
		}
		if(StringUtils.isNotBlank(form.getConn())){
			str.append(" and (ma.title like '%"+form.getConn()+"%' or ma.content like '%"+form.getConn()+"%') ");
		}
		if(form.getMsgType()!=null){
			str.append(" and ma.msgType = "+form.getMsgType());
		}
		if(form.getMsgNoticeType()!=null){
			str.append(" and ma.msgNoticeType= "+form.getMsgNoticeType());
		}
		if(form.getIfRead()!=null){
			str.append(" and ma.ifRead ="+form.getIfRead());
		}
		if(form.getSaveStatus()!=null){
			str.append(" and ma.status= "+form.getSaveStatus());
		}
		//  部门ID组
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			StringBuffer strid = new StringBuffer();
			for(int num=0;num<form.getDepIds().length;num++){
				strid.append(form.getDepIds()[num]);
				if((form.getDepIds().length-1)>num){
					strid.append(",");
				}
			}
			str.append(" and md.department_id in ("+strid+")");
		}
		return str.toString();
	}
	
	/**
	 * 连表查询自己创建的部门的公告
	 * @author gao
	 * @param form
	 * @return
	private String backPartMySelfSql(MsgSelectForm form){
		StringBuffer str = new StringBuffer("select new MsgAnnouncement( ma.id,ma.title,ma.titleVulgarCss,"
				+ " ma.titleLightCss,ma.content,ma.contentinfo,ma.contentUrl,ma.status,ma.companyId,"
				+ " ma.agntinfoId,ma.msgType,ma.msgNoticeType,ma.overTime,ma.docinfoIds,ma.createDate ) "
				+ " from MsgAnnouncement AS ma,MsgToDepartment AS md where ma.id=md.msgId ");
		if(StringUtils.isNotBlank(form.getStartDate())){
			str.append(" and  ma.createDate >= "+form.getStartDate());
		}
		if(StringUtils.isNotBlank(form.getEndDate())){
			str.append(" and  ma.createDate < "+form.getEndDate());
		}
		if(StringUtils.isNotBlank(form.getConn())){
			str.append(" and (ma.title like '%"+form.getConn()+"%' or ma.content like '%"+form.getConn()+"%') ");
		}
		if(form.getMsgType()!=null){
			str.append(" and ma.msgType = "+form.getMsgType());
		}
		if(form.getMsgNoticeType()!=null){
			str.append(" and ma.msgNoticeType= "+form.getMsgNoticeType());
		}
		if(form.getSaveStatus()!=null){
			str.append(" and ma.status= "+form.getSaveStatus());
		}
		str.append(" and ma.createBy= "+UserUtils.getUser().getId());
		//  部门ID组
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			StringBuffer strid = new StringBuffer();
			for(int num=0;num<form.getDepIds().length;num++){
				strid.append(form.getDepIds()[num]);
				if((form.getDepIds().length-1)>num){
					strid.append(",");
				}
			}
			str.append(" and md.departmentId in ("+strid+")");
		}
		// 排列
		str.append(" order by ma.id desc ");
		return str.toString();
	}	
	*/
	private String backPartMySelfSql(MsgSelectForm form){
		StringBuffer str = new StringBuffer("select  distinct(ma.id),ma.title,ma.title_vulgar_css,"
				+ " ma.title_light_css,ma.content,ma.contentinfo,ma.content_url,ma.status,ma.company_id,"
				+ " ma.agntinfo_id,ma.msg_type,ma.msg_notice_type,ma.over_time,ma.docinfo_ids,ma.createDate "
				+ " from msg_announcement AS ma,msg_to_department AS md where ma.id=md.msg_id ");
		if(StringUtils.isNotBlank(form.getStartDate())){
			str.append(" and  ma.createDate >= "+form.getStartDate());
		}
		if(StringUtils.isNotBlank(form.getEndDate())){
			str.append(" and  ma.createDate < "+form.getEndDate());
		}
		if(StringUtils.isNotBlank(form.getConn())){
			str.append(" and (ma.title like '%"+form.getConn()+"%' or ma.content like '%"+form.getConn()+"%') ");
		}
		if(form.getMsgType()!=null){
			str.append(" and ma.msg_type = "+form.getMsgType());
		}
		if(form.getMsgNoticeType()!=null){
			str.append(" and ma.msg_notice_type= "+form.getMsgNoticeType());
		}
		if(form.getSaveStatus()!=null){
			str.append(" and ma.status= "+form.getSaveStatus());
		}
		Long userId = UserUtils.getUser().getId();
		str.append(" and ma.createBy= "+userId);
		//  部门ID组
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			StringBuffer strid = new StringBuffer();
			for(int num=0;num<form.getDepIds().length;num++){
				strid.append(form.getDepIds()[num]);
				if((form.getDepIds().length-1)>num){
					strid.append(",");
				}
			}
			str.append(" and md.department_id in ("+strid+")");
		}
		// 排列
		str.append(" order by ma.id desc ");
		return str.toString();
	}
	
	private String backPartMySelfSqlCount(MsgSelectForm form){
		StringBuffer str = new StringBuffer("select count(distinct(ma.id))"
				+ " from msg_announcement AS ma,msg_to_department AS md where ma.id=md.msg_id ");
		if(StringUtils.isNotBlank(form.getStartDate())){
			str.append(" and  ma.createDate >= "+form.getStartDate());
		}
		if(StringUtils.isNotBlank(form.getEndDate())){
			str.append(" and  ma.createDate < "+form.getEndDate());
		}
		if(StringUtils.isNotBlank(form.getConn())){
			str.append(" and (ma.title like '%"+form.getConn()+"%' or ma.content like '%"+form.getConn()+"%') ");
		}
		if(form.getMsgType()!=null){
			str.append(" and ma.msg_type = "+form.getMsgType());
		}
		if(form.getMsgNoticeType()!=null){
			str.append(" and ma.msg_notice_type= "+form.getMsgNoticeType());
		}
		if(form.getSaveStatus()!=null){
			str.append(" and ma.status= "+form.getSaveStatus());
		}
		Long userId = UserUtils.getUser().getId();
		str.append(" and ma.createBy= "+userId);
		//  部门ID组
		if(form.getDepIds()!=null && form.getDepIds().length>0){
			StringBuffer strid = new StringBuffer();
			for(int num=0;num<form.getDepIds().length;num++){
				strid.append(form.getDepIds()[num]);
				if((form.getDepIds().length-1)>num){
					strid.append(",");
				}
			}
			str.append(" and md.department_id in ("+strid+")");
		}
		return str.toString();
	}	
}
