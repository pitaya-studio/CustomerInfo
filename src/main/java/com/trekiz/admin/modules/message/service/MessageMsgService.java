package com.trekiz.admin.modules.message.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.entity.MsgMark;
import com.trekiz.admin.modules.message.form.MsgAnnouncementForm;
import com.trekiz.admin.modules.message.form.MsgSelectForm;

public interface MessageMsgService {

	/**
	 * 增加公告
	 * @param form MsgAnnouncementForm 公告前台bean
	 * @return
	 */
	public Map<String,Object > addMsg(MsgAnnouncementForm form);
	
	/**
	 * 修正公告
	 * @param form MsgAnnouncementForm 公告前台bean
	 * @return
	 */
	public Map<String,Object > editMsg(MsgAnnouncementForm form);
	/**
	 * 修正公告状态
	 * @param msg MsgAnnouncement 公告实体
	 * @param status 公告新状态
	 * @return
	 */
	public boolean changeMsgStatus(MsgAnnouncement msg,Integer status);
	/**
	 * 根据ID获取Msg
	 * @param msgId MsgAnnouncement 公告ID
	 * @return
	 */
	public MsgAnnouncement findMsgById(Long msgId);
	/**
	 * 根据相应条件，获取Msg分页列表
	 * @param form 查询条件
	 * @param page
	 * @return
	 */
	public Page<MsgMark> findMsgByUserId(MsgSelectForm form,Page<MsgMark> page) ;
	/**
	 * 根据登录userID 获取未读Msg总数
	 * @param userId 登录userID 
	 * @return
	 */
	public Integer getMsgNum(Long userId);
	/**
	 * 根据msgId，修正msg 已读/未读 状态
	 * @param msgId 
	 * @return
	 */
	public boolean changeMsgRead(Long msgId);

	/**
	 * 根据msgId，修正msg 已还/未还 状态
	 * @param msgId
	 * @return
	 */
	public boolean changeMsgShow(Long msgId);
	
	/**
	 * 查询消息、公告列表
	 * @author gao
	 * @param form 查询条件
	 * @return
	 */
	public List<MsgMark> findMsgByUserId(MsgSelectForm form);
	/**
	 * 查询指定公告、消息
	 * @author gao
	 * @param id MsgMark的ID
	 * @return
	 */
	public MsgMark findMsgMarkById(Long id);

	/**
	 * 通过MsgAnnouncement的ID和登录用户ID，返回对应的MsgUserMarkId
	 * @author gao
	 * @param msgId MsgAnnouncement的ID
	 * @return
	 */
	public Long findMsgUserMarkIdByMsgU(Long msgId);
	/**
	 * 分页查找自己创建的公告列表
	 * @author gao
	 * @param form  查询条件类
	 * @param page 分页类
	 * @return
	 */
	public Page<MsgAnnouncement> findMyCreateMsgByUserId(MsgSelectForm form,Page<MsgAnnouncement> page);
	/**'
	 * 查找自己创建的公告列表
	 * gao
	 */
	public List<MsgAnnouncement> findMyCreateMsgByUserId(MsgSelectForm form);
	/**
	 * 根据 当前登陆人的 公司ID,登陆人ID查找约签信息
	 * @author gao
	 * @return
	 */
	public MsgMark findMsgByCompanyId();
	/**
	 * 清理约签缓存
	 * @author gao
	 * @param key 缓存key
	 */
	public void cleanCache(String key);
	/**
	 * 生成约签缓存key，标记结构： CACHE_NAME_companyId_userID (key标记 _ 约签发布公司ID _ 约签接收人ID)
	 * @author gao
	 * @param companyId
	 * @param userId
	 * @return
	 */
	public  String backKey(Long companyId,Long userId);
	/**
	 * 连表查询公告分页(部门分类)
	 * @author gao
	 * @param form
	 * @param page
	 * @return
	 */
	public Page<MsgMark> findPartBySql(MsgSelectForm form, Page<MsgMark> page);
	/**
	 * 连表查询公告数量(部门分类)
	 * @author gao
	 * @param form
	 * @param page
	 * @return
	 */
	public List<MsgMark> findPartBySqlList(MsgSelectForm form);
	/**
	 * 连表查询自己发布的公告分页(部门分类)
	 * @author gao
	 * @param form
	 * @param page
	 * @return
	 */
	public Page<MsgAnnouncement> findPartMySelfBySql(MsgSelectForm form, Page<MsgAnnouncement> page);
	/**
	 * 连表查询自己发布的公告数量(部门分类)
	 * @author gao
	 * @param form
	 * @param page
	 * @return
	 */
	public List<MsgAnnouncement> findPartMySelfBySqlList(MsgSelectForm form);

	/**
	 * 270 查询提醒的title
	 * @return
     */
	String findRemindTitle();
}
