package com.trekiz.admin.agentToOffice.shelfRights.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.shelfRights.dao.ShelfRightsDao;
import com.trekiz.admin.agentToOffice.shelfRights.service.ShelfRightsService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;

@Service
@Transactional(readOnly = true)
public class ShelfRightsServiceImpl extends BaseService implements ShelfRightsService{
	@Autowired
	private ShelfRightsDao shelfRightsDao;
	
	/**
	 * 分页查询
	 * @author chao.zhang@quauq.com
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<Map<String,Object>> find(Page<Map<String,Object>> page,
			Office office,String type ,String big) {
		 StringBuffer sbf=new StringBuffer();
		 sbf.append("SELECT ");
		sbf.append("r.*,");
		sbf.append("count(t.id) groupCount,");
		sbf.append("IFNULL(s.orderNum,0) T1OrderCount,");
		sbf.append(	"IFNULL(s.personNum,0) quauqPersonCount ");
		sbf.append(	"FROM ");
		sbf.append(	"( ");
		sbf.append(	"SELECT ");
		sbf.append(	"p.proCompany, ");
		sbf.append(	"g.id ");
		sbf.append(	"FROM ");
		sbf.append(	"activitygroup g, ");
		sbf.append(	"travelactivity p ");
		sbf.append(	"WHERE ");
		sbf.append(	"p.id = g.srcActivityId ");
		sbf.append(	"AND g.groupOpenDate>=? ");
		sbf.append(	"AND g.delFlag=? ");
		sbf.append(	"AND p.delFlag=? ");
		sbf.append(	"AND p.activityStatus=? ");
		sbf.append(	"AND p.activity_kind=? ");
		sbf.append(	"GROUP BY 	g.id ");
		sbf.append(	") t RIGHT JOIN ");
		sbf.append(	"sys_office r ");
		sbf.append(	"ON r.id = t.proCompany  ");
		sbf.append("LEFT JOIN( ");
		sbf.append("SELECT p.proCompany,");
		sbf.append("count(o.id) orderNum,");
		sbf.append("sum(o.orderPersonNum) personNum ");
		sbf.append("FROM ");
		sbf.append("productorder o LEFT JOIN travelactivity p ON o.productId=p.id LEFT JOIN sys_office c ON c.id=p.proCompany ");
		sbf.append("WHERE ");
		sbf.append("o.productId = p.id AND o.priceType=? AND o.payStatus!=? AND o.delFlag=0 ");
		sbf.append("AND o.payStatus!=? AND p.delFlag=? AND p.activityStatus=? ");
		sbf.append("AND p.activity_kind=? GROUP BY c.id) s ON r.id=s.proCompany ");
		sbf.append("WHERE r.delFlag=0 ");
		 if(office.getName()!=null && !office.getName().equals("-1")){
			 sbf.append("AND r.name like '%"+office.getName()+"%' ");
		 }
		 if(office.getShelfRightsStatus()!=null && office.getShelfRightsStatus()!=-1){
			 sbf.append("AND r.shelfRightsStatus="+office.getShelfRightsStatus());
		 }
		 sbf.append(" group by r.id");
		 if(StringUtils.isEmpty(type)){
			 sbf.append(" ORDER By count(t.id) ASC");
		 }else if(type.equals("groupCount")){
			 if(big.equals("0")){
				 sbf.append(" ORDER BY count(t.id) DESC");
			 }else{
				 sbf.append(" ORDER BY count(t.id) ASC");
			 }
		 }else if(type.equals("orderCount")){
			 if(big.equals("0")){
				 sbf.append(" ORDER BY s.orderNum DESC");
			 }else{
				 sbf.append(" ORDER BY s.orderNum ASC");
			 }
		 }else{
			 if(big.equals("0")){
				 sbf.append(" ORDER BY s.personNum DESC");
			 }else{
				 sbf.append(" ORDER BY s.personNum ASC");
			 }
		 }
		 Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String format = dateFormat.format(date);
		 page=shelfRightsDao.findPageBySql(page, sbf.toString(), Map.class,format, 0,0,2,2,2,111,99,0,2,2);
		return page;
	}
	
	/**
	 * 查询总数
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public Integer getProductCountByWholeSalerId(Integer whoSalerId) {
		Integer count = shelfRightsDao.getProductCountByWhosalerId(whoSalerId);
		return count;
	}
	/**
	 * 查询T1平台上架产品
	 * @param whosalerId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public Integer getT1ProductCountByActivityId(Integer activityId){
		Integer count = shelfRightsDao.getT1ProductCountByActivityId(activityId);
		return count;
	}
	
	/**
	 * 查询所有散拼的产品
	 * @param whosalerId
	 * @param priceStrategy
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<TravelActivity> getAllTravelActivities(Long whosalerId) {
		List<TravelActivity> activities = shelfRightsDao.getAllTravelActivities(whosalerId);
		return activities;
	}
	
	/**
	 * 获得所有的批发商
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getAllOffice() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM sys_office WHERE delFlag=?");
		List<Map<String,Object>> list=shelfRightsDao.findBySql(sbf.toString(),Map.class, 0);
		return list;
	}
	
	/**
	 * 根据批发商id修改上架权限状态
	 * @param companyId
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void updatShelRightsStatusByCompanyId(Long companyId, Integer status) {
		shelfRightsDao.updatShelRightsStatusByCompanyId(companyId, status);
	}

	@Override
	public Integer getBaoMing(Integer companyId) {
		List<User> list = shelfRightsDao.getUserByCompanyId(companyId);
		if(null==list && list.size()==0){
			return 0;//没有
		}else{
			return 1;//有
		}
	}

	@Override
	public Integer getOrderCount(Integer productId) {
		Integer count = shelfRightsDao.getT1OrderCount(productId);
		return count;
	}

	@Override
	public Integer getQuauqPersonCount(Integer companyId) {
		Integer count = shelfRightsDao.getQuauqPersonCount(companyId);
		return count;
	}

	@Override
	public Integer getCompanyCount(Office office) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT count(*) AS count FROM sys_office WHERE delFlag=0 ");
		 if(office.getName()!=null && !office.getName().equals("-1")){
			 sbf.append("AND name like '%"+office.getName()+"%' ");
		 }
		 if(office.getShelfRightsStatus()!=null && office.getShelfRightsStatus()!=-1){
			 sbf.append("AND shelfRightsStatus="+office.getShelfRightsStatus());
		 }
		List<Map<String,Object>> list=shelfRightsDao.findBySql(sbf.toString(), Map.class);
		Integer count = Integer.parseInt( list.get(0).get("count").toString());
		return count;
	}
	

}
