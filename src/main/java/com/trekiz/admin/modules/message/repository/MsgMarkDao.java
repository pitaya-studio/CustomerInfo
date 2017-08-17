package com.trekiz.admin.modules.message.repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.message.entity.MsgMark;
import com.trekiz.admin.modules.message.form.MsgSelectForm;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * 公告视图查询
 * @author gao
 *  2015年2月28日
 */
public interface MsgMarkDao extends MsgMarkDaoCustom,CrudRepository<MsgMark, Long> {

	/**
	 * 只需任意取出一条该用户下的提醒记录，如果没有记录，则默认不显示提醒的弹出条
	 * remindType 1:还款提醒
	 * msgType 7:提醒
	 * isShow 0:显示提醒
     */
	@Query(value="select title from msg_mark_view where remindType=1 and msgType=7 and isShow=0 and userId=?1 order by msgId desc LIMIT 1", nativeQuery = true)
	public String findRemindTitleByUserId(Long userId);
	
}
interface MsgMarkDaoCustom extends BaseDao<MsgMark> {
	Page<MsgMark> findMsgByUserId(MsgSelectForm  from,int pageNo,int pageSize);
	public MsgMark findByCompanyIDOne(Long companyId,Long userId);
	public List<MsgMark> findByCompanyID(Long companyId,Long msgAnnouncementId);
	
}

@Repository
class MsgMarkDaoImpl extends BaseDaoImpl<MsgMark> implements MsgMarkDaoCustom{

	
	/**
	 * 视图查询公告、消息列表
	 * gao
	 * MsgSelectForm 查询条件
	 * pageNo   分页页码
	 * pageSize 分页条数
	 */
	@SuppressWarnings("unchecked")
    @Override
	public Page<MsgMark> findMsgByUserId(MsgSelectForm from,int pageNo,int pageSize) {
		try{
			 StringBuffer queryString = new StringBuffer("from MsgMark userId = "+UserUtils.getUser().getId());
			
			 if(StringUtils.isNotBlank(from.getStartDate()) && StringUtils.isNotBlank(from.getEndDate())){
				  queryString.append(" and createDate >"+from.getStartDate()+" and createDate<="+from.getEndDate());
			  }
			  if(StringUtils.isNotBlank(from.getConn())){
				  queryString.append(" and (title like '%'"+from.getConn()+"'%' or  content like '%'"+from.getConn()+"'%')");
			  }
			  if(from.getMsgType()!=null ){
				  queryString.append(" and msgType = "+from.getMsgType());
			  }
			  org.hibernate.Query queryObject = (org.hibernate.Query) getSession().createQuery(queryString.toString());
			  int totalCount = queryObject!=null ? queryObject.list().size():0;
			  
			  Page<MsgMark> page = new Page<MsgMark>(pageNo,pageSize,totalCount);
			  queryObject.setFirstResult((pageNo-1)*pageSize);
			  queryObject.setMaxResults(pageSize);
			  page.setResult(queryObject.list());
			  return page;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取约签公告（通过公司ID，获取第一条约签公告）
	 * 条件：类型：约签公告，状态：已发布，阅读状态：未阅读
	 * @author gao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MsgMark findByCompanyIDOne(Long companyId,Long userId) {
		StringBuffer hql = new StringBuffer("from MsgMark where  msgType='"+Context.MSG_TYPE_ENGAGE+"' "
				+ "and status='"+Context.MESSAGE_STATUS_ISSUE+"'  "
						//+ "and ifRead ='"+Context.MSG_IFREAD_NO+"'  "
						+ "and companyId = "+companyId+"  and userId = "+userId+"  order by id  Desc" );
		org.hibernate.Query query = getSession().createQuery(hql.toString());
		//query.setParameter(1, companyId);
		query.setMaxResults(1);
		query.setFirstResult(0);
		List<MsgMark> list = (List<MsgMark>)query.list();
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获取约签公告（通过公司ID，获取全部符合条件的约签公告）
	 * 条件：类型：约签公告，状态：已发布，
	 * 阅读状态：未阅读(根据15年3月19日王涛要求，改为不限制,只取最新一条)
	 * @author gao
	 * @param companyId 公司ID
	 * @param msgAnnouncementId 公告ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MsgMark> findByCompanyID(Long companyId,Long msgAnnouncementId){
		StringBuffer hql = new StringBuffer("from MsgMark where  msgType='"+Context.MSG_TYPE_ENGAGE+"' "
				+ "and status='"+Context.MESSAGE_STATUS_ISSUE+"' "
						//+ " and ifRead ='"+Context.MSG_IFREAD_NO+"'  "
						//+ "and companyId = "+companyId+" "
								+ "and msgAnnouncementId="+msgAnnouncementId+" "
						//		+ "order by id desc"
								);
		org.hibernate.Query query = getSession().createQuery(hql.toString());
		//query.setParameter(1, companyId);
//		query.setMaxResults(1);
//		query.setFirstResult(0);
		List<MsgMark> list = (List<MsgMark>)query.list();
		if(list!=null && !list.isEmpty()){
			return list;
		}
		return null;
	}

	
} 