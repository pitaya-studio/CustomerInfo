package com.trekiz.admin.modules.message.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.sys.entity.Dict;

public interface  MsgAnnouncementDao extends  MsgAnnouncementDaoCustom,CrudRepository<MsgAnnouncement, Long> {
	
	/**
	 * 逻辑删除公告消息
	 * */
	@Modifying
	@Query("update MsgAnnouncement set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	/**
	 * 根据主键获取公共消息
	 * */
	@Query("from MsgAnnouncement where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and id=?1 ")
	public List<MsgAnnouncement> findAllListById(Long Id);
	
	/**
	 * 根据消息类型获取公共消息
	 * */
	@Query("from MsgAnnouncement where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and msg_type=?1 ")
	public List<MsgAnnouncement> findBymsgType(String msgType);
	
	/**
	 * 根据消息类型和公司id获取公共消息
	 * */
	@Query("from MsgAnnouncement where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and companyId=?1 and msg_type=?2 ")
	public List<MsgAnnouncement> findBymsgTypeAndcompanyId(String companyId,String msgType);
	
	/**
	 * 根据公司id获取公共消息
	 * */
	@Query("from MsgAnnouncement where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and companyId=?1 ")
	public List<MsgAnnouncement> findBycompanyId(String companyId);
	
	/**
	 * 根据消息状态获取消息列表
	 */
	@Query("from MsgAnnouncement where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and status=?1 and overTime < ?2")
	public List<MsgAnnouncement> findByStatus(Integer status,Date date);
	
	/**
	 * 修正记录状态
	 * @author gao
	 * @param status
	 * @param id
	 * @return
	 */
	@Modifying
	@Query("update MsgAnnouncement set status=?1 where id = ?2")
	public int updateMsgAnnouncement(Integer status,Long id);
	
	/**
	 * 删除记录
	 * @author gao
	 * @return
	 */
	@Modifying
	@Query("update MsgAnnouncement set status=2 where id = ?1")
	public int delMsgAnnouncement(Long id);
	
	/**
	 * TODO 可用性待定 （建议使用MsgEntityDao）
	 * 根据审批id，规则id，查找msg
	 * @param businessData
	 * @param remindId
	 * @return
	 */
	@Query("from MsgAnnouncement where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and businessData=?1 and sysRemindId < ?2")
	public MsgAnnouncement getMsgByBuisness(String businessData, Integer remindId);
	
}


/**
 * DAO自定义接口
 * @author zj
 */
interface MsgAnnouncementDaoCustom extends BaseDao<MsgAnnouncement> {
	
}
/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class MsgAnnouncementDaoImpl extends BaseDaoImpl<MsgAnnouncement> implements MsgAnnouncementDaoCustom {

}
