package com.trekiz.admin.modules.message.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.message.entity.MsgUserMark;
import com.trekiz.admin.modules.sys.entity.Dict;

public interface  MsgUserMarkDao extends  MsgUserMarkDaoCustom,CrudRepository<MsgUserMark, Long> {
	
	/**
	 * 根据消息id逻辑删除公告消息
	 * @param id 通告编号
	 * */
	@Modifying
	@Query("update MsgUserMark set delFlag='" + Dict.DEL_FLAG_DELETE + "' where msgAnnouncementId = ?1")
	public int deleteById(Long id);
	
	/**
	 * 根据MsgAnnouncement 主键和登录用户id获取登陆用户所能看到的。
	 * @author gao
	 * @param msgId  MsgAnnouncement 主键
	 * @param userId  登录用户ID
	 * @return
	 */
	@Query("from MsgUserMark where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and msgAnnouncementId=?1 and userId=?2")
	public Long findMsgUserMarkIdByMsgU(Long msgId,Long userId);
	
	/**
	 * 根据MsgAnnouncement 主键 获取 usermark。
	 * @author yang.jiang 2016-4-11 10:55:46
	 * @param msgId  MsgAnnouncement 主键
	 * @return
	 */
	@Query("from MsgUserMark where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and msgAnnouncementId = ?1")
	public List<MsgUserMark> findMsgUserMarkByMsgId(Long msgId);
	
	/**
	 * 修正MsgUserMark阅读状态
	 * @author gao
	 * @param id MsgUserMark主键
	 * @param date 当前时间
	 * @return
	 */
	@Modifying
	@Query("update MsgUserMark set ifRead=1,readTime=?2  where id = ?1")
	public int readById(Long id,Date date);

	/**
	 * 修改MsgUserMark是否已还
	 * @param id
	 * @return
     */
	@Modifying
	@Query("update MsgUserMark set isShow=1 where id=?1")
	public void showById(Long id);

	/**
	 * 根据MsgAnnouncement 主键 获取 不再提醒的 userMark
	 * @author yang.jiang 2016-4-11 10:55:46
	 * @param msgId  MsgAnnouncement 主键
	 * @return
	 */
	@Query("from MsgUserMark where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and msgAnnouncementId = ?1 and isShow = 1")
	public List<MsgUserMark> findExceptUserMarkByMsgId(Long id);
}


/**
 * DAO自定义接口
 * @author zj
 */
interface MsgUserMarkDaoCustom extends BaseDao<MsgUserMark> {

}
/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class MsgUserMarkDaoImpl extends BaseDaoImpl<MsgUserMark> implements MsgUserMarkDaoCustom {

	
}
