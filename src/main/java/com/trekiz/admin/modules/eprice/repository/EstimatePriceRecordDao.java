package com.trekiz.admin.modules.eprice.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 * @see com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord
 * @author lihua.xu
 */
@Component("estimatePriceRecordDao")
public class EstimatePriceRecordDao extends BaseDaoImpl<EstimatePriceRecord>  {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceRecordDao.class);
	
	private static final String PID = "pid";

	public void save(EstimatePriceRecord transientInstance) {
		log.debug("saving EstimatePriceRecord instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePriceRecord findById(java.lang.Long id) {
		log.debug("getting EstimatePriceRecord instance with id: " + id);
		try {
			EstimatePriceRecord instance = (EstimatePriceRecord) getSession()
					.get("com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord", id);
			// 缺一块非null验证
			if(instance==null){
				return null;
			}
			if(EstimatePriceRecord.STATUS_DEL==instance.getStatus()){
				return null;
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	private List findByProperty(String propertyName, Object value) {
		log.debug("finding EstimatePriceRecord instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EstimatePriceRecord as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EstimatePriceRecord> findByPid(Object pid) {
		return findByProperty(PID, pid);
	}

	/**
	 * 通过询价项目id查询获取可多条件排序的分页询价记录列表
	 * @author lihua.xu
	 * @时间 2014年9月26日
	 * @param pid 询价项目id
	 * * @param type 询价类型：0、单团；1、机票
	 * @param pageSize 分页大小
	 * @param pageNo 分页页码
	 * @param sort 排序条件 {Column:orderValue} Column:排序字段，orderValue:排序方式 0 倒序 1正序
	 * @return Page<EstimatePriceRecord>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Page<EstimatePriceRecord> findByPid(Long pid, Integer type,int pageSize,int pageNo, Map<String, Integer> sort, boolean isOpManager) {
		String hql = " from EstimatePriceRecord p ";
		
		if(pid!=null){
			hql += " where p.status="+EstimatePriceRecord.STATUS_NORMAL+" and p.pid= "+pid;
		}
		if(type!=null && type>0){
			hql += " and p.type="+type;
		}
		if (isOpManager) {
			hql += " and p.baseInfo.id in (select estimateBaseId from EstimatePriceDistribution dis where dis.opManagerId = " + UserUtils.getUser().getId() + ")";
		}
		hql += " order by ";
		
		if(sort!=null&&!sort.isEmpty()){
			Iterator<String> it = sort.keySet().iterator();
			String key;
			Integer v;
			while (it.hasNext()) {
				key = it.next();
				v = sort.get(key);
				
				String de = " asc ";
				if(v==1){
					de = " desc ";
				}
				hql += " p."+key+de+" ,";
			}
			hql = hql.substring(0,hql.length()-2);
		}else{
			hql += " p.id desc";
		}
		
		 
		
		
		Query qc = this.getSession().createQuery("select count(p.id) "+hql);
		Query q = this.getSession().createQuery("select p "+hql );
		
		//Long count = (Long)qc.uniqueResult();
		int count = Integer.parseInt(qc.list().get(0).toString());
		//System.out.println("qc.uniqueResult():"+count);
		
		Page<EstimatePriceRecord> page = new Page<EstimatePriceRecord>(pageNo,pageSize,count);
		q.setFirstResult((pageNo - 1) * pageSize);  
        q.setMaxResults(pageSize);  
        List<EstimatePriceRecord> reList = q.list();
		
        
        for(EstimatePriceRecord record:reList){
        	if(EstimatePriceRecord.TYPE_FLIGHT != record.getType().intValue()){
        		String sql = " select group_concat(area.name)   " +
        				" from   estimate_price_admit_requirements admit , estimate_price_admit_lines_area ala , sys_area area " +
        				" where admit.id = ala.admit_id and area.id = ala.area_id " +
        				" and admit.id = "+record.getAdmitRequirements().getId();
        		Query queryName = this.getSession().createSQLQuery(sql);
        		List nameList  = queryName.list();
        		if(nameList!=null&&nameList.size()>0){
        			record.getAdmitRequirements().setTravelCountry(nameList.get(0)==null||nameList.get(0).equals("")?"(无)":nameList.get(0).toString());
        		} 
        	}
        	
        	if (isOpManager && record.getEstimateStatus() == 1) {
        		Integer distributionType = 0;
        		if (record.getType() == 7) {
        			distributionType = 1;
        		}
        		String sql = "SELECT op_id FROM estimate_price_distribution WHERE op_manager_id = " 
    					+ UserUtils.getUser().getId() + " AND estimate_base_id = " + record.getBaseInfo().getId() + " AND type = " + distributionType;
	    		List<Map<String, String>> opIdList = this.findBySql(sql, Map.class);
	    		if (CollectionUtils.isNotEmpty(opIdList) && StringUtils.isNotBlank(opIdList.get(0).get("op_id"))) {
	    			
	    		} else {
	    			record.setStatus(6);
	    		}
        	}
        }
        page.setResult(reList);
		return page;
	}
	
	@SuppressWarnings("unchecked")
    public Page<EstimatePriceRecord> findByPid(Long pid, int pageSize,int pageNo, Map<String, Integer> sort) {
		String hql = "from EstimatePriceRecord p ";
		
		if(pid!=null){
			hql += " where p.status="+EstimatePriceRecord.STATUS_NORMAL+" and p.pid= "+pid+" and p.toperatorUserJson<>'[]'";
		}
		hql += "group by p.id";
		hql += " order by ";
		
		if(sort!=null){
			Iterator<String> it = sort.keySet().iterator();
			String key;
			Integer v;
			while (it.hasNext()) {
				key = it.next();
				v = sort.get(key);
				
				String de = " desc";
				if(v==1){
					de = " asc";
				}
				hql += " p."+key+de+" ,";
			}
			hql = hql.substring(0,hql.length()-2);
		}else{
			
			hql += " p.id desc";
			
		}
		
		
		
		
		Query qc = this.getSession().createQuery("select p.id "+hql);
		Query q = this.getSession().createQuery(hql);
		
		//Long count = (Long)qc.uniqueResult();
		int count = qc.list().size();
		//System.out.println("qc.uniqueResult():"+count);
		List<EstimatePriceRecord> reList = q.list();
		
		Page<EstimatePriceRecord> page = new Page<EstimatePriceRecord>(pageNo,pageSize,count);
		
		q.setFirstResult((pageNo - 1) * pageSize);  
        q.setMaxResults(pageSize);  
        
        page.setResult(reList);
        
//		System.out.println(reList);
	//	List<EstimatePriceRecord> reList = q.list();
		@SuppressWarnings("unused")
        Iterator<EstimatePriceRecord> it = reList.iterator();
		
//		while(it.hasNext()){
//			EstimatePriceRecord re = it.next();
//			System.out.println("UserName:"+re.getUserName());
//			System.out.println("CompanyName:"+re.getCompanyName());
//			System.out.println("Status:"+re.getStatus());
//			System.out.println("Id:"+re.getId());
//			System.out.println("Pid:"+re.getPid());
//			System.out.println("DgroupOutDate:"+re.getAdmitRequirements().getDgroupOutDate());
//			System.out.println("OutsideDaySum:"+re.getAdmitRequirements().getOutsideDaySum());
//		}
		
		
		return page;
	}
}