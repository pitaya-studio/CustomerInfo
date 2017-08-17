package com.trekiz.admin.agentToOffice.shelfRights.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.shelfRights.dao.ShelfRightsDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.sys.entity.User;
@Service
public class ShelfRightsDaoImpl extends BaseDaoImpl implements ShelfRightsDao{
	/**
	 * 根据批发商id修改上架权限状态
	 * @param companyId
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void updatShelRightsStatusByCompanyId(Long companyId,Integer status) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("UPDATE sys_office SET shelfRightsStatus=? WHERE id=?");
		this.updateBySql(sbf.toString(), status,companyId);
	}
	/**
	 * 根据批发商id查询该批发商下所有散拼团中的产品的团期总数（出团日期大于等于当前日期）
	 * @param whosalerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer getProductCountByWhosalerId(Integer whosalerId) {
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String format = dateFormat.format(date);
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT count(*) AS count FROM travelactivity activity ");
		sbf.append("LEFT JOIN activitygroup ag ");
		sbf.append("ON activity.id=ag.srcActivityId ");
		sbf.append("WHERE activity.proCompany=? AND ag.delFlag=? AND activity.activityStatus=? AND ag.groupOpenDate>=? ");
		sbf.append("AND activity.delFlag=? AND activity_kind=? ");
		 List<Map<String,Object>> list= this.findBySql(sbf.toString(),Map.class, whosalerId,0,2,format,0,2); 
		 Integer count=Integer.parseInt(list.get(0).get("count").toString());
		return count;
	}
	/**
	 * 查询在T1上上架产品团期数
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public Integer getT1ProductCountByActivityId(Integer activityId){
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String format = dateFormat.format(date);
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT count(*) as count ");
		sbf.append("FROM ");
		sbf.append("activitygroup ag ");
		sbf.append("WHERE  ag.delFlag=? AND ag.groupOpenDate>=? AND ag.srcActivityId=?");
		List<Map<String,Object>> list= this.findBySql(sbf.toString(),Map.class,0,format,activityId);
		Integer count=Integer.parseInt(list.get(0).get("count").toString());
		return count;
	}
	/**
	 * 查询所有的散拼产品
	 * @param whosalerId
	 * @return
	 */
	@Override
	public List<TravelActivity> getAllTravelActivities(Long whosalerId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM travelactivity WHERE delFlag=? AND proCompany=? ");
		sbf.append("AND activityStatus=? AND activity_kind=? ");
		List<TravelActivity> list=this.findBySql(sbf.toString(),TravelActivity.class,0,whosalerId,2,2);
		return list;
	}
	@Override
	public List<User> getUserByCompanyId(Integer companyId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM sys_user WHERE quauqBookOrderPermission=? AND is_quauq_agent_login_user=? AND delFlag=? AND companyId=?");
		List<User> list=this.findBySql(sbf.toString(),User.class, 1,1,0,companyId);
		return list;
	}
	@Override
	public Integer getT1OrderCount(Integer productId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT count(*) AS count FROM productorder WHERE priceType=? AND delFlag=? AND payStatus !=? AND productId=? ");
		List<Map<String,Object>> list=this.findBySql(sbf.toString(),Map.class,2,0,99,productId);
		Integer count=0;
		if(list.get(0).get("count")!=null && list.size()!=0){
			count=Integer.parseInt(list.get(0).get("count").toString());
		}
		return count;
	}
	@Override
	public Integer getQuauqPersonCount(Integer companyId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT SUM(p.orderPersonNum) AS count FROM travelactivity t LEFT JOIN productorder p ON t.id=p.productId ");
		sbf.append("WHERE t.delFlag=? AND t.proCompany=? ");
		sbf.append("AND t.activityStatus=? AND t.activity_kind=? ");
		sbf.append("AND p.priceType=? AND p.delFlag=? AND p.payStatus !=?");
		List<Map<String,Object>> list=this.findBySql(sbf.toString(),Map.class, 0,companyId,2,2,2,0,99);
		Integer count=0;
		if(list.get(0).get("count")!=null && list.size()!=0){
			count=Integer.parseInt(list.get(0).get("count").toString());
		}
		return count;
	}

}
