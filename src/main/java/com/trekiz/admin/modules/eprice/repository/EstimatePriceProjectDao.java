package com.trekiz.admin.modules.eprice.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ListSearchForm;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 
 * @see EstimatePriceProject
 * @author lihua.xu
 */
@Component("estimatePriceProjectDao")
public class EstimatePriceProjectDao extends BaseDaoImpl<EstimatePriceProject> {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceProjectDao.class);

		public void save(EstimatePriceProject transientInstance) {
			log.debug("saving EstimatePriceProject instance");
			try {
				getSession().saveOrUpdate(transientInstance);
				log.debug("save successful");
			} catch (RuntimeException re) {
				log.error("save failed", re);
				throw re;
			}
		}
		
		
		public void update(EstimatePriceProject epp){
			log.debug("saving EstimatePriceProject instance");
			try {
				getSession().update(epp);
				log.debug("save successful");
			} catch (RuntimeException re) {
				log.error("save failed", re);
				throw re;
			}			
		}

		public EstimatePriceProject findById(java.lang.Long id) {
			log.debug("getting EstimatePriceProject instance with id: " + id);
			try {
				EstimatePriceProject instance = (EstimatePriceProject) getSession()
						.get("com.trekiz.admin.modules.eprice.entity.EstimatePriceProject", id);
				if(instance.getStatus().equals(EstimatePriceProject.STATUS_DEL)){
					return null;
				}
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
		}

		/**
		 * 多条件可排序多条件查询分页询价项目列表
		 * @author lihua.xu
		 * @时间 2014年9月17日
		 * @param pageSize 分页大小
		 * @param pageNo 分页页码
		 * @param keyword 搜索关键字（目前搜索计调姓名/国家/客户）
		 * @param salerId 销售id
		 * @param operatorUserId 计调id
		 * @param estimateStatus 询价项目状态 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品
		 * @param type 询价项目类型 1 单团询价  2 机票询价
		 * @param travelCountryId 线路国家id
		 * @param startGroupOutDate 出团时间区间——起始时间
		 * @param endGroupOutDate 出团时间区间——结束始时间
		 * @param sort 排序 {Column:orderValue} Column:排序字段，orderValue:排序方式 0 倒序 1正序
		 * @return Page<EstimatePriceProject>
		 */
		@SuppressWarnings("unchecked")
		public Page<EstimatePriceProject> findByPage(int pageSize, int pageNo,String keyword,Long salerId, Long operatorUserId,
				Integer estimateStatus, Integer type, Long travelCountryId,Date startGroupOutDate, Date endGroupOutDate,
				Date ePriceDateStart,Date ePriceDateEnd,Map<String,Integer> map, String deptHql) {
			//在RBO优化器模式下，表应按结果记录数从大到小的顺序从左到右来排列
			
			String hql = "from  EstimatePriceProject p ";
			boolean rb=false,bib=false,arb=false;
			List<String> qlist = new ArrayList<String>();
			String ts;
			//关键词模糊匹配 涉及 r bi ar 三张表
			if(StringUtils.isNotEmpty(keyword)){
				ts = " ( bi.salerName  like '%"+keyword+"%' or bi.customerName like '%"+keyword+"%' or ar.travelCountry like  '%"+keyword+"%') ";
				qlist.add(ts);
				
				rb = true;
				bib = true;
				arb = true;
			}
			
			//查询本批发商的询价工程
			Long officeId = UserUtils.getUser().getCompany().getId();
			if (officeId != null) {
			ts = " p.companyId = " + officeId;
 				qlist.add(ts);
 				bib = true;
 			}
			
			//销售
			if( salerId!=null&&salerId>0){
				ts = " bi.salerId="+salerId;
				qlist.add(ts);
				bib = true;
			}
			
			//计调id
			if(operatorUserId!=null && operatorUserId>0){
				ts = " (r.aoperatorUserJson like '%\"userId\":"+operatorUserId+",%' )";
				qlist.add(ts);
				rb = true;
			}
			
			if(estimateStatus!=null && estimateStatus>=0){
				ts = " p.estimateStatus="+estimateStatus;
				qlist.add(ts);
			}
			
			
			//过滤掉机票
			if(type!=null&&type==-1){
				ts = " p.type!="+7;
				qlist.add(ts);
			}
			
			if(type!=null && type>0){
				ts = " p.type="+type;
				qlist.add(ts);
			}
			
			
			
			if(travelCountryId!=null && travelCountryId>0){
//				" and  (ar.travel_country_id like '%"+travelCountryId+",%' or ar.travel_country_id like '%"+travelCountryId+"]' ) " ;
				ts = " (ar.travelCountryId like '%"+travelCountryId+",%' or ar.travelCountryId like '%"+travelCountryId+"]' ) " ;
				qlist.add(ts);
				arb = true;
			}
			
			if(startGroupOutDate!=null){
				ts = " ar.dgroupOutDate>=:sdate";
				qlist.add(ts);
				arb = true;
			}
			
			if(endGroupOutDate!=null){
				ts = " ar.dgroupOutDate<=:edate";
				qlist.add(ts);
				arb = true;
			}
			//判断查询条件中是否有最近询价日期的条件
			if(ePriceDateStart!=null){
				ts = " p.lastEstimatePriceTime>=:sedate";
				qlist.add(ts);
				rb = true;
			}
			
			if(ePriceDateEnd!=null){
				ts = " p.lastEstimatePriceTime<=:eedate";
				qlist.add(ts);
				rb = true;
			}
			
			
			if(rb){
				hql += ", EstimatePriceRecord r";
				ts = " p.id=r.pid ";
				qlist.add(ts);
			}
			if(bib){
				hql += ", EstimatePriceBaseInfo bi";
				ts = " p.id=bi.pid ";
				qlist.add(ts);
			}
			if(arb){
				hql += ", EstimatePriceAdmitRequirements ar";
				ts = " p.id=ar.pid ";
				qlist.add(ts);
			}
			
			hql += " where p.status="+EstimatePriceProject.STATUS_NORMAL;
			
			hql += deptHql;
			
			if(qlist.size()>0){
				hql += " and "+join(" and ",qlist.toArray(new String[qlist.size()]));
			}
			hql += " group by p.id";
			hql += " order by ";
			
			boolean t = false;
			if(map!=null){
				Iterator<String> it = map.keySet().iterator();
				String key;
				Integer v;
				while (it.hasNext()) {
					key = it.next();
					v = map.get(key);
					
					String de = " asc ";
					if(v==1){
						de = "  desc ";
					}
					hql += " p."+key+de+" ,";
					t  =  true;
				}
				
				
			}
			
			if(!t){
				hql += " p.modifyTime desc";
			}else{
				hql = hql.substring(0,hql.length()-2);
				
			}
			//String counthql = "select count(a.id) from ("+"select p.id "+hql+") a";
			Query qc = this.getSession().createQuery("select p.id "+hql);
			Query query = this.getSession().createQuery("select p "+hql);
			
			if(startGroupOutDate!=null){
				qc.setTimestamp("sdate", startGroupOutDate);
				query.setTimestamp("sdate", startGroupOutDate);
			}
			
			if(endGroupOutDate!=null){
				qc.setTimestamp("edate", endGroupOutDate);
				query.setTimestamp("edate", endGroupOutDate);
			}
			//设置最近询价时间的参数
			if(ePriceDateStart!=null){
				qc.setTimestamp("sedate", ePriceDateStart);
				query.setTimestamp("sedate", ePriceDateStart);
			}
			
			if(ePriceDateEnd!=null){
				qc.setTimestamp("eedate", ePriceDateEnd);
				query.setTimestamp("eedate", ePriceDateEnd);
			}
			
			//Long count = (Long)qc.uniqueResult();
			int count = qc.list().size();
			//System.out.println("qc.uniqueResult():"+count);
			
			Page<EstimatePriceProject> page = new Page<EstimatePriceProject>(pageNo,pageSize,count);
			
			query.setFirstResult((pageNo - 1) * pageSize);  
	        query.setMaxResults(pageSize);  
	        
	        page.setResult(query.list());
	        
	        
	        return page;  
			
			
		}
		/**
		 *  获取机票计调产品（包括单团中包含的机票计调）
		 * @param pageSize
		 * @param pageNo
		 * @param keyword
		 * @param salerId
		 * @param operatorUserId
		 * @param estimateStatus
		 * @param travelCountryId
		 * @param startGroupOutDate
		 * @param endGroupOutDate
		 * @param map
		 * @param deptHql
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public Page<EstimatePriceProject> findByPage(int pageSize, int pageNo,String keyword,Long salerId, Long operatorUserId,
				Integer estimateStatus,  Long travelCountryId,Date startGroupOutDate, Date endGroupOutDate,
				Date ePriceDateStart,Date ePriceDateEnd,Map<String,Integer> map, String deptHql,ListSearchForm lsf) {
			//在RBO优化器模式下，表应按结果记录数从大到小的顺序从左到右来排列
			String hql = "from  EstimatePriceProject p ";
			boolean rb=false,bib=false;
			List<String> qlist = new ArrayList<String>();
			String ts;
			//关键词模糊匹配 涉及 r bi  二张表
			if(StringUtils.isNotEmpty(keyword)){
				ts = " ( bi.salerName  like '%"+keyword+"%' " + " or bi.customerName like '%"+keyword+"%' ) ";
				qlist.add(ts);
				
				rb = true;
				bib = true;
			}
			
			//销售匹配 涉及 r bi 两张表 
			if(salerId!=null && salerId!=0 ){
				ts = " bi.salerId="+salerId;
				qlist.add(ts);
				bib = true;
			}
			
			//计调id
			if(operatorUserId!=null && operatorUserId>0){
				//ts = " (r.aoperatorUserJson like '%\"userId\":"+operatorUserId+",%' or r.toperatorUserJson like '%\"userId\":"+operatorUserId+",%')";
				ts = " ( r.toperatorUserJson like '%\"userId\":"+operatorUserId+",%')";
				qlist.add(ts);
				rb = true;
			}
			
			if(estimateStatus!=null && estimateStatus>=0){
				ts = " p.estimateStatus="+estimateStatus;
				qlist.add(ts);
			}
			
			
			if(startGroupOutDate!=null){
				ts = " r.lastToperatorStartOutTime>=:sdate";
				qlist.add(ts);
				rb = true;
			}
			
			if(endGroupOutDate!=null){
				ts = " r.lastToperatorStartOutTime<=:edate";
				qlist.add(ts);
				rb = true;
			}
			//判断查询条件中是否有最近询价日期的条件
			if(ePriceDateStart!=null){
				ts = " p.lastEstimatePriceTime>=:sedate";
				qlist.add(ts);
				rb = true;
			}
			
			if(ePriceDateEnd!=null){
				ts = " p.lastEstimatePriceTime<=:eedate";
				qlist.add(ts);
				rb = true;
			}
			
			
			
			if(rb){
				hql += ", EstimatePriceRecord r";
				ts = " p.id=r.pid ";
				qlist.add(ts);
			}
			if(bib){
				hql += ", EstimatePriceBaseInfo bi";
				ts = " p.id=bi.pid ";
				qlist.add(ts);
			}
			
			if(lsf.getType()!=null&&lsf.getType()>0){
				ts = " p.type="+lsf.getType();
				qlist.add(ts);
			}
			 
			
			hql += " where   p.status="+EstimatePriceProject.STATUS_NORMAL;
			
			hql += deptHql;
			
			if(qlist.size()>0){
				hql += " and "+join(" and ",qlist.toArray(new String[qlist.size()]));
			}
			
			if(rb){
				hql += " AND (r.toperatorUserJson IS NOT NULL AND r.toperatorUserJson<>'[]')";
			}
			hql += " group by p.id";
			hql += " order by ";
			
			boolean t = false;
			if(map!=null){
				Iterator<String> it = map.keySet().iterator();
				String key;
				Integer v;
				while (it.hasNext()) {
					key = it.next();
					v = map.get(key);
					
					String de = " asc ";
					if(v==1){
						de = "  desc ";
					}
					hql += " p."+key+de+" ,";
					t = true;
				}
				
			}
			
			if(!t){
				hql += " p.modifyTime desc";
			}else{
				hql = hql.substring(0,hql.length()-2);
				
			}
		//	String str = "select count(a.id) from ("+("select p.id "+hql)+") a";
			Query qc = this.getSession().createQuery("select p.id  "+hql);
			Query query = this.getSession().createQuery("select p "+hql);
			
			if(startGroupOutDate!=null){
				qc.setTimestamp("sdate", startGroupOutDate);
				query.setTimestamp("sdate", startGroupOutDate);
			}
			
			if(endGroupOutDate!=null){
				qc.setTimestamp("edate", endGroupOutDate);
				query.setTimestamp("edate", endGroupOutDate);
			}
			//设置最近询价时间的参数
			if(ePriceDateStart!=null){
				qc.setTimestamp("sedate", ePriceDateStart);
				query.setTimestamp("sedate", ePriceDateStart);
			}
			
			if(ePriceDateEnd!=null){
				qc.setTimestamp("eedate", ePriceDateEnd);
				query.setTimestamp("eedate", ePriceDateEnd);
			}
			
			//Long count = (Long)qc.uniqueResult();
			int count = qc.list().size();
			//System.out.println("qc.uniqueResult():"+count);
			
			Page<EstimatePriceProject> page = new Page<EstimatePriceProject>(pageNo,pageSize,count);
			
			query.setFirstResult((pageNo - 1) * pageSize);  
	        query.setMaxResults(pageSize);  
	        
	        page.setResult(query.list());
	        
	        
	        return page;  
			
			
		}
		
		private static String join(String join,String[] strAry){
	        StringBuffer sb=new StringBuffer();
	        for(int i=0;i<strAry.length;i++){
	             if(i==(strAry.length-1)){
	                 sb.append(strAry[i]);
	             }else{
	                 sb.append(strAry[i]).append(join);
	             }
	        }
	       
	        return new String(sb);
	    }

		/**
		 * 多条件可排序多条件查询分页询价项目列表
		 * @author yue.wang
		 * @时间 2014年01月05日
		 * @param pageSize 分页大小
		 * @param pageNo 分页页码
		 * @param keyword 搜索关键字（目前搜索计调姓名/国家/客户）
		 * @param salerId 销售id
		 * @param operatorUserId 计调id
		 * @param estimateStatus 询价项目状态 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品
		 * @param type 询价项目类型 1 单团询价  2 机票询价
		 * @param travelCountryId 线路国家id
		 * @param startGroupOutDate 出团时间区间——起始时间
		 * @param endGroupOutDate 出团时间区间——结束始时间
		 * @param sort 排序 {Column:orderValue} Column:排序字段，orderValue:排序方式 0 倒序 1正序
		 * @return Page<EstimatePriceProject>
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
        public Page findListByPage(ListSearchForm lsf,int pageSize, int pageNo, String keyword,
				Long salerId, Long operatorUserId, Integer estimateStatus,
				Integer type, Long travelCountryId, Map<String, Integer> map, String deptHql) {
			
			
			String aopFrom =" select p.id as id,p.title as title ,p.user_id as user_id ,p.user_name as user_name ,p.company_id as company_id," +
							"  p.company_name as company_name,p.last_record_id as last_record_id,p.last_base_info_id as last_base_info_id ," +
							"  p.last_admit_requirements_id as last_admit_requirements_id,  p.last_traffic_requirements_id as last_traffic_requirements_id,  " +
							"  p.estimate_status as estimate_status ,p.status as status,p.estimate_price_sum as estimate_price_sum,p.type as type," +
							"  p.last_estimate_price_time as last_estimate_price_time,p.last_operator_given_time as last_operator_given_time," +
							"  p.last_create_product_time as last_create_product_time,p.last_create_order_time as last_create_order_time," +
							"  p.last_cancel_time as last_cancel_time,p.create_time as create_time ,p.modify_time as modify_time ,p.remark as remark " +
					         " from estimate_price_project p , estimate_price_record r, estimate_price_base_info bi, estimate_price_admit_requirements ar "; 
			String aopWhere ="	where p.id=r.pid and   p.id=bi.pid and   p.status="+EstimatePriceProject.STATUS_NORMAL +" and  p.id=ar.pid  "; 
			String topFrom = " select p.id as id,p.title as title ,p.user_id as user_id ,p.user_name as user_name ,p.company_id as company_id," +
							"  p.company_name as company_name,p.last_record_id as last_record_id,p.last_base_info_id as last_base_info_id ," +
							"  p.last_admit_requirements_id as last_admit_requirements_id,  p.last_traffic_requirements_id as last_traffic_requirements_id,  " +
							"  p.estimate_status as estimate_status ,p.status as status,p.estimate_price_sum as estimate_price_sum,p.type as type," +
							"  p.last_estimate_price_time as last_estimate_price_time,p.last_operator_given_time as last_operator_given_time," +
							"  p.last_create_product_time as last_create_product_time,p.last_create_order_time as last_create_order_time," +
							"  p.last_cancel_time as last_cancel_time,p.create_time as create_time ,p.modify_time as modify_time ,p.remark as remark " +
					         " from  estimate_price_project p , estimate_price_record r, estimate_price_base_info bi,  estimate_price_traffic_requirements tr ";   
			String topWhere ="	where p.id=r.pid and   p.id=bi.pid and   p.status="+EstimatePriceProject.STATUS_NORMAL +" and  p.id=tr.pid "; 
			
			
			if(StringUtils.isNotEmpty(keyword)){
				aopWhere +=   "and  (r.aoperator_user_json like '%\"userName\":\""+keyword+"%' or r.toperator_user_json  like '%\"userName\":\""+keyword+"%' "
						+ " or bi.customer_name like '%"+keyword+"%' or ar.travel_country like  '%"+keyword+"%') ";
				 
				topWhere +="and (r.aoperator_user_json like '%\"userName\":\""+keyword+"%' or r.toperator_user_json  like '%\"userName\":\""+keyword+"%' "
						+ " or bi.customer_name like '%"+keyword+"%' ) ";
				
			}
			
//			//计调id
			if(operatorUserId!=null && operatorUserId>0){
				aopWhere += " and  (r.aoperator_user_json like '%\"userId\":"+operatorUserId+",%' or r.toperator_user_json like '%\"userId\":"+operatorUserId+",%')";
				topWhere += " and  (r.aoperator_user_json like '%\"userId\":"+operatorUserId+",%' or r.toperator_user_json like '%\"userId\":"+operatorUserId+",%')";
			}
			
			if(estimateStatus!=null && estimateStatus>=0){
				aopWhere +=  " and  p.estimate_status="+estimateStatus;
				topWhere +=  " and  p.estimate_status="+estimateStatus;
			}
			
			
			if(!StringUtils.isBlank(lsf.getGroupOpenDate())){
				aopWhere +=  " and DATE_FORMAT(ar.dgroup_out_date,'%Y%m%d') >=DATE_FORMAT('"+lsf.getGroupOpenDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(r.last_toperator_start_out_time,'%Y%m%d') >=DATE_FORMAT('"+lsf.getGroupOpenDate()+"','%Y%m%d')"; 
			}
			
			if(!StringUtils.isBlank(lsf.getGroupCloseDate())){
				aopWhere +=  " and DATE_FORMAT(ar.dgroup_out_date,'%Y%m%d') <=DATE_FORMAT('"+lsf.getGroupCloseDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(r.last_toperator_start_out_time,'%Y%m%d') <=DATE_FORMAT('"+lsf.getGroupCloseDate()+"','%Y%m%d')"; 
			}
			
			//动态添加最近询价时间条件
			//开始是时间
			if(!StringUtils.isBlank(lsf.getEpriceStartDate())){
				aopWhere +=  " and DATE_FORMAT(p.last_estimate_price_time,'%Y%m%d') >=DATE_FORMAT('"+lsf.getEpriceStartDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(p.last_estimate_price_time,'%Y%m%d') >=DATE_FORMAT('"+lsf.getEpriceStartDate()+"','%Y%m%d')"; 
			}
			//结束时间
			if(!StringUtils.isBlank(lsf.getEpriceEndDate())){
				aopWhere +=  " and DATE_FORMAT(p.last_estimate_price_time,'%Y%m%d') <=DATE_FORMAT('"+lsf.getEpriceEndDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(p.last_estimate_price_time,'%Y%m%d') <=DATE_FORMAT('"+lsf.getEpriceEndDate()+"','%Y%m%d')"; 
			}
			if(type!=null && type>0){
				aopWhere +=  " and  p.type="+type;
				topWhere +=  " and  p.type="+type;
			}
			
			if(travelCountryId!=null && travelCountryId>0){
				aopWhere +=  " and  (ar.travel_country_id like '%"+travelCountryId+",%' or ar.travel_country_id like '%"+travelCountryId+"]' ) " ;
				topWhere +=  " and 1=2 ";
			}
			
			if(!StringUtils.isBlank(lsf.getCustId())&&!lsf.getCustId().equals("-1")){
				aopWhere +=  " and  bi.customer_agent_id =  " + lsf.getCustId();
				topWhere +=  " and  bi.customer_agent_id =  " + lsf.getCustId() ;
			}
			
			
			if(salerId!=null&&salerId>0){
				aopWhere +=  " and  p.user_id="+salerId;
				topWhere +=  " and  p.user_id="+salerId;
				
				
			}
			
//			//查询本批发商的询价工程
//			Long officeId = UserUtils.getUser().getCompany().getId();
//			if (officeId != null) {
//			ts = " p.companyId = " + officeId;
// 				qlist.add(ts);
// 				bib = true;
// 			}
//			
			
			
			String sql = " select u.id as id," +                                                 //0
						"u.title as title ," +                                                   //1
						"u.user_id as user_id ," +                                                //2
						"u.user_name as user_name ," +                                                //3
						"u.company_id as company_id," +                                                //4
					     " u.company_name as company_name," +                                          //5
					     "u.last_record_id as last_record_id," +                                      //6
					     "u.last_base_info_id as last_base_info_id ," +                               //7
					     " u.last_admit_requirements_id as last_admit_requirements_id, " +          //8
					     " u.last_traffic_requirements_id as last_traffic_requirements_id,  " +      //9
					     "  u.estimate_status as estimate_status ," +  //10
					     "u.status as status," +//11
					     "u.estimate_price_sum as estimate_price_sum," +//12
					     "u.type as type," +//13
					     " u.last_estimate_price_time as last_estimate_price_time," +//14
					     "u.last_operator_given_time as last_operator_given_time," +//15
					     " u.last_create_product_time as last_create_product_time," +//16
					     "u.last_create_order_time as last_create_order_time," +//17
					     " u.last_cancel_time as last_cancel_time," +//18
					     "u.create_time as create_time ," +//19
					     "u.modify_time as modify_time ," +//20
					     "u.remark as remark        " +//21
					     " from ( ("+ aopFrom + aopWhere +") union ("+ topFrom  + topWhere +") ) u  " 
					     + " where 1=1 "+deptHql+
					     " group by" +
					     " u.id,u.title,u.user_id,u.user_name,u.company_id,u.company_name,u.last_record_id,u.last_base_info_id,u.last_admit_requirements_id," +
					     "u.last_traffic_requirements_id,u.estimate_status,u.status,u.estimate_price_sum,u.type,u.last_estimate_price_time," +
					     "u.last_operator_given_time,u.last_create_product_time,u.last_create_order_time,u.last_cancel_time,u.create_time,u.modify_time,u.remark";
			
            String orderBy = " order by ";
			boolean t = false;
			if(map!=null){
				Iterator<String> it = map.keySet().iterator();
				String key;
				Integer v;
				while (it.hasNext()) {
					key = it.next();
					v = map.get(key);

					if("lastOperatorGivenTime".equals(key)){
						key = "last_operator_given_time";
					}
					if("lastEstimatePriceTime".equals(key)){
						key = "last_estimate_price_time";
					}
					if("lastCreateProductTime".equals(key)){
						key = "last_create_product_time";
					}
					if("lastCancelTime".equals(key)){
						key = "last_cancel_time";
					}
					
					String de = " asc ";
					if(v==1){
						de = "  desc ";
					}
					orderBy += " u."+key+de+" ,";
					t  =  true;
				}
			}
			if(!t){
				orderBy += " u.modify_time desc";
			}else{
				orderBy = orderBy.substring(0,orderBy.length()-2);
			}
			
			
			String countSql = " select count(1) as count   from ( " +sql+" ) u1";
			String sql1 =sql + orderBy ;
			Query countQ = this.getSession().createSQLQuery(countSql);
			Query query1 = this.getSession().createSQLQuery(sql1);
			
			int count = Integer.parseInt(((BigInteger)countQ.uniqueResult()).toString());  

			
		
			
			
			query1.setFirstResult((pageNo - 1) * pageSize);  
			query1.setMaxResults(pageSize);  
			
			Page<EstimatePriceProject> page = new Page<EstimatePriceProject>(pageNo,pageSize,count);
	        page.setResult(query1.list()	);
	        
	        return page;  
			
//			
//			//查询本批发商的询价工程
//			Long officeId = UserUtils.getUser().getCompany().getId();
//			if (officeId != null) {
//			ts = " p.companyId = " + officeId;
// 				qlist.add(ts);
// 				bib = true;
// 			}
//			
//			
//			
//			if(type!=null && type>0){
//				ts = " p.type="+type;
//				qlist.add(ts);
//			}
//			
//			
//			
//			if(travelCountryId!=null && travelCountryId>0){
//				ts = "ar.travelCountryId="+travelCountryId;
//				qlist.add(ts);
//				arb = true;
//			}
//			
//			if(startGroupOutDate!=null){
//				ts = " ar.dgroupOutDate>=:sdate";
//				qlist.add(ts);
//				arb = true;
//			}
//			
//			if(endGroupOutDate!=null){
//				ts = " ar.dgroupOutDate<=:edate";
//				qlist.add(ts);
//				arb = true;
//			}
//			
//		
//			
//			if(rb){
//				hql += ", EstimatePriceRecord r";
//				ts = " p.id=r.pid ";
//				qlist.add(ts);
//			}
//			if(bib){
//				hql += ", EstimatePriceBaseInfo bi";
//				ts = " p.id=bi.pid ";
//				qlist.add(ts);
//			}
//			if(arb){
//				hql += ", EstimatePriceAdmitRequirements ar";
//				ts = "  ";
//				qlist.add(ts);
//			}
//			
//			hql += " where p.status="+EstimatePriceProject.STATUS_NORMAL;
//			
//			hql += deptHql;
//			
//			if(qlist.size()>0){
//				hql += " and "+join(" and ",qlist.toArray(new String[qlist.size()]));
//			}
//			hql += " group by p.id";
//			hql += " order by ";
//			
//			 
//			//String counthql = "select count(a.id) from ("+"select p.id "+hql+") a";
//			Query qc = this.getSession().createQuery("select p.id "+hql);
//			Query query = this.getSession().createQuery("select p "+hql);
//			
//			if(startGroupOutDate!=null){
//				qc.setTimestamp("sdate", startGroupOutDate);
//				query.setTimestamp("sdate", startGroupOutDate);
//			}
//			
//			if(endGroupOutDate!=null){
//				qc.setTimestamp("edate", endGroupOutDate);
//				query.setTimestamp("edate", endGroupOutDate);
//			}
//			
//			
//			Page<EstimatePriceProject> page = new Page<EstimatePriceProject>(pageNo,pageSize,count);
//			
//			query.setFirstResult((pageNo - 1) * pageSize);  
//	        query.setMaxResults(pageSize);  
//	        
//	        page.setResult(query.list());
//	        
//	        
//	        return page;  
			
			
		}
		
		/**
		 * 多条件可排序多条件查询分页询价项目列表
		 * @author yue.wang
		 * @时间 2014年01月05日
		 * @param pageSize 分页大小
		 * @param pageNo 分页页码
		 * @param keyword 搜索关键字（目前搜索计调姓名/国家/客户）
		 * @param salerId 销售id
		 * @param operatorUserId 计调id
		 * @param estimateStatus 询价项目状态 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品
		 * @param type 询价项目类型 1 单团询价  2 机票询价
		 * @param travelCountryId 线路国家id
		 * @param startGroupOutDate 出团时间区间——起始时间
		 * @param endGroupOutDate 出团时间区间——结束始时间
		 * @param sort 排序 {Column:orderValue} Column:排序字段，orderValue:排序方式 0 倒序 1正序
		 * @return Page<EstimatePriceProject>
		 W
		@SuppressWarnings({ "rawtypes", "unchecked" })
        public Page findListByPageForManager(ListSearchForm lsf,int pageSize, int pageNo, String keyword,
				Long salerId, Long operatorUserId, Integer estimateStatus,
				Integer type, Long travelCountryId, Map<String, Integer> map) {
			
			String aopFrom =" select p.id as id,p.title as title ,p.user_id as user_id ,p.user_name as user_name ,p.company_id as company_id," +
							"  p.company_name as company_name,p.last_record_id as last_record_id,p.last_base_info_id as last_base_info_id ," +
							"  p.last_admit_requirements_id as last_admit_requirements_id,  p.last_traffic_requirements_id as last_traffic_requirements_id,  " +
							"  p.estimate_status as estimate_status ,p.status as status,p.estimate_price_sum as estimate_price_sum,p.type as type," +
							"  p.last_estimate_price_time as last_estimate_price_time,p.last_operator_given_time as last_operator_given_time," +
							"  p.last_create_product_time as last_create_product_time,p.last_create_order_time as last_create_order_time," +
							"  p.last_cancel_time as last_cancel_time,p.create_time as create_time ,p.modify_time as modify_time ,p.remark as remark, bi.id estimateBaseInfoId " +
					         " from estimate_price_project p , estimate_price_record r, estimate_price_base_info bi, estimate_price_admit_requirements ar "; 
			String aopWhere ="	where p.id=r.pid and   p.id=bi.pid and   p.status="+EstimatePriceProject.STATUS_NORMAL +" and  p.id=ar.pid  "; 
			String topFrom = " select p.id as id,p.title as title ,p.user_id as user_id ,p.user_name as user_name ,p.company_id as company_id," +
							"  p.company_name as company_name,p.last_record_id as last_record_id,p.last_base_info_id as last_base_info_id ," +
							"  p.last_admit_requirements_id as last_admit_requirements_id,  p.last_traffic_requirements_id as last_traffic_requirements_id,  " +
							"  p.estimate_status as estimate_status ,p.status as status,p.estimate_price_sum as estimate_price_sum,p.type as type," +
							"  p.last_estimate_price_time as last_estimate_price_time,p.last_operator_given_time as last_operator_given_time," +
							"  p.last_create_product_time as last_create_product_time,p.last_create_order_time as last_create_order_time," +
							"  p.last_cancel_time as last_cancel_time,p.create_time as create_time ,p.modify_time as modify_time ,p.remark as remark, bi.id estimateBaseInfoId " +
					         " from  estimate_price_project p , estimate_price_record r, estimate_price_base_info bi,  estimate_price_traffic_requirements tr ";   
			String topWhere ="	where p.id=r.pid and   p.id=bi.pid and   p.status="+EstimatePriceProject.STATUS_NORMAL +" and  p.id=tr.pid "; 
			
			String distributionSql = "select estimate_base_id, op_id from estimate_price_distribution where op_manager_id = " + UserUtils.getUser().getId();
			String distributionWhere = "";
			
			if(StringUtils.isNotEmpty(keyword)){
				aopWhere +=   "and  (r.aoperator_user_json like '%\"userName\":\""+keyword+"%' or r.toperator_user_json  like '%\"userName\":\""+keyword+"%' "
						+ " or bi.customer_name like '%"+keyword+"%' or ar.travel_country like  '%"+keyword+"%') ";
				 
				topWhere +="and (r.aoperator_user_json like '%\"userName\":\""+keyword+"%' or r.toperator_user_json  like '%\"userName\":\""+keyword+"%' "
						+ " or bi.customer_name like '%"+keyword+"%' ) ";
				
			}
			
//			//计调id
			if(operatorUserId!=null && operatorUserId>0){
				aopWhere += " and  (r.aoperator_user_json like '%\"userId\":"+operatorUserId+",%' or r.toperator_user_json like '%\"userId\":"+operatorUserId+",%')";
				topWhere += " and  (r.aoperator_user_json like '%\"userId\":"+operatorUserId+",%' or r.toperator_user_json like '%\"userId\":"+operatorUserId+",%')";
			}
			
			if (estimateStatus != null && estimateStatus >= 0) {
				if (estimateStatus == 6) {
					aopWhere +=  " and  p.estimate_status = 1";
					topWhere +=  " and  p.estimate_status = 1";
				} else {
					aopWhere +=  " and  p.estimate_status = " + estimateStatus;
					topWhere +=  " and  p.estimate_status = " + estimateStatus;
				}
				
				if (estimateStatus == 6) {
					distributionWhere = " and op_id is null";
				} else if (estimateStatus == 1) {
					distributionWhere = " and op_id is not null";
				}
			}
			
			
			if(!StringUtils.isBlank(lsf.getGroupOpenDate())){
				aopWhere +=  " and DATE_FORMAT(ar.dgroup_out_date,'%Y%m%d') >=DATE_FORMAT('"+lsf.getGroupOpenDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(r.last_toperator_start_out_time,'%Y%m%d') >=DATE_FORMAT('"+lsf.getGroupOpenDate()+"','%Y%m%d')"; 
			}
			
			if(!StringUtils.isBlank(lsf.getGroupCloseDate())){
				aopWhere +=  " and DATE_FORMAT(ar.dgroup_out_date,'%Y%m%d') <=DATE_FORMAT('"+lsf.getGroupCloseDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(r.last_toperator_start_out_time,'%Y%m%d') <=DATE_FORMAT('"+lsf.getGroupCloseDate()+"','%Y%m%d')"; 
			}
			
			if(type!=null && type>0){
				aopWhere +=  " and  p.type="+type;
				topWhere +=  " and  p.type="+type;
			}
			
			if(travelCountryId!=null && travelCountryId>0){
				aopWhere +=  " and  (ar.travel_country_id like '%"+travelCountryId+",%' or ar.travel_country_id like '%"+travelCountryId+"]' ) " ;
				topWhere +=  " and 1=2 ";
			}
			
			if(!StringUtils.isBlank(lsf.getCustId())&&!lsf.getCustId().equals("-1")){
				aopWhere +=  " and   bi.customer_agent_id =  " + lsf.getCustId();
				topWhere +=  " and  bi.customer_agent_id =  " + lsf.getCustId() ;
			}
			
			
			if(salerId!=null&&salerId>0){
				aopWhere +=  " and  p.user_id="+salerId;
				topWhere +=  " and  p.user_id="+salerId;
				
				
			}
			
			String sql = " select u.id as id," +                                                 //0
						"u.title as title ," +                                                   //1
						"u.user_id as user_id ," +                                                //2
						"u.user_name as user_name ," +                                                //3
						"u.company_id as company_id," +                                                //4
					     " u.company_name as company_name," +                                          //5
					     "u.last_record_id as last_record_id," +                                      //6
					     "u.last_base_info_id as last_base_info_id ," +                               //7
					     " u.last_admit_requirements_id as last_admit_requirements_id, " +          //8
					     " u.last_traffic_requirements_id as last_traffic_requirements_id,  " +      //9
					     "  u.estimate_status as estimate_status ," +  //10
					     "u.status as status," +//11
					     "u.estimate_price_sum as estimate_price_sum," +//12
					     "u.type as type," +//13
					     " u.last_estimate_price_time as last_estimate_price_time," +//14
					     "u.last_operator_given_time as last_operator_given_time," +//15
					     " u.last_create_product_time as last_create_product_time," +//16
					     "u.last_create_order_time as last_create_order_time," +//17
					     " u.last_cancel_time as last_cancel_time," +//18
					     "u.create_time as create_time ," +//19
					     "u.modify_time as modify_time ," +//20
					     "u.remark as remark,        " +//21
					     "distribution.op_id as opId        " +//22
					     " from ( ("+ aopFrom + aopWhere +") union ("+ topFrom  + topWhere +") ) u, (" + distributionSql + distributionWhere + ") distribution " 
					     + " where 1=1 "
					     + "and u.estimateBaseInfoId = distribution.estimate_base_id" 
					     + " group by" +
					     " u.id,u.title,u.user_id,u.user_name,u.company_id,u.company_name,u.last_record_id,u.last_base_info_id,u.last_admit_requirements_id," +
					     "u.last_traffic_requirements_id,u.estimate_status,u.status,u.estimate_price_sum,u.type,u.last_estimate_price_time," +
					     "u.last_operator_given_time,u.last_create_product_time,u.last_create_order_time,u.last_cancel_time,u.create_time,u.modify_time,u.remark";
			
            String orderBy = " order by ";
			boolean t = false;
			if(map!=null){
				Iterator<String> it = map.keySet().iterator();
				String key;
				Integer v;
				while (it.hasNext()) {
					key = it.next();
					v = map.get(key);

					if("lastOperatorGivenTime".equals(key)){
						key = "last_operator_given_time";
					}
					if("lastEstimatePriceTime".equals(key)){
						key = "last_estimate_price_time";
					}
					if("lastCreateProductTime".equals(key)){
						key = "last_create_product_time";
					}
					if("lastCancelTime".equals(key)){
						key = "last_cancel_time";
					}
					
					String de = " asc ";
					if(v==1){
						de = "  desc ";
					}
					orderBy += " u."+key+de+" ,";
					t  =  true;
				}
			}
			if(!t){
				orderBy += " u.modify_time desc";
			}else{
				orderBy = orderBy.substring(0,orderBy.length()-2);
			}
			
			
			String countSql = " select count(1) as count   from ( " +sql+" ) u1";
			String sql1 =sql + orderBy ;
			Query countQ = this.getSession().createSQLQuery(countSql);
			Query query1 = this.getSession().createSQLQuery(sql1);
			
			int count = Integer.parseInt(((BigInteger)countQ.uniqueResult()).toString());  
			
			query1.setFirstResult((pageNo - 1) * pageSize);  
			query1.setMaxResults(pageSize);  
			
			Page<EstimatePriceProject> page = new Page<EstimatePriceProject>(pageNo,pageSize,count);
	        page.setResult(query1.list()	);
	        
	        return page;  
		}
		*/
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
        public Page findListByPageForManager(ListSearchForm lsf,int pageSize, int pageNo, String keyword,
				Long salerId, Long operatorUserId, Integer estimateStatus,
				Integer type, Long travelCountryId, Map<String, Integer> map) {
			
			String aopFrom =" select p.id as id,p.title as title ,p.user_id as user_id ,p.user_name as user_name ,p.company_id as company_id," +
							"  p.company_name as company_name,p.last_record_id as last_record_id,p.last_base_info_id as last_base_info_id ," +
							"  p.last_admit_requirements_id as last_admit_requirements_id,  p.last_traffic_requirements_id as last_traffic_requirements_id,  " +
							"  p.estimate_status as estimate_status ,p.status as status,p.estimate_price_sum as estimate_price_sum,p.type as type," +
							"  p.last_estimate_price_time as last_estimate_price_time,p.last_operator_given_time as last_operator_given_time," +
							"  p.last_create_product_time as last_create_product_time,p.last_create_order_time as last_create_order_time," +
							"  p.last_cancel_time as last_cancel_time,p.create_time as create_time ,p.modify_time as modify_time ,p.remark as remark, bi.id estimateBaseInfoId " +
					         " from estimate_price_project p , estimate_price_record r, estimate_price_base_info bi, estimate_price_admit_requirements ar "; 
			String aopWhere ="	where p.id=r.pid and   p.id=bi.pid and   p.status="+EstimatePriceProject.STATUS_NORMAL +" and  p.id=ar.pid  "; 
			String topFrom = " select p.id as id,p.title as title ,p.user_id as user_id ,p.user_name as user_name ,p.company_id as company_id," +
							"  p.company_name as company_name,p.last_record_id as last_record_id,p.last_base_info_id as last_base_info_id ," +
							"  p.last_admit_requirements_id as last_admit_requirements_id,  p.last_traffic_requirements_id as last_traffic_requirements_id,  " +
							"  p.estimate_status as estimate_status ,p.status as status,p.estimate_price_sum as estimate_price_sum,p.type as type," +
							"  p.last_estimate_price_time as last_estimate_price_time,p.last_operator_given_time as last_operator_given_time," +
							"  p.last_create_product_time as last_create_product_time,p.last_create_order_time as last_create_order_time," +
							"  p.last_cancel_time as last_cancel_time,p.create_time as create_time ,p.modify_time as modify_time ,p.remark as remark, bi.id estimateBaseInfoId " +
					         " from  estimate_price_project p , estimate_price_record r, estimate_price_base_info bi,  estimate_price_traffic_requirements tr ";   
			String topWhere ="	where p.id=r.pid and   p.id=bi.pid and   p.status="+EstimatePriceProject.STATUS_NORMAL +" and  p.id=tr.pid "; 
			
			String distributionSql = "select estimate_base_id, op_id from estimate_price_distribution where op_manager_id = " + UserUtils.getUser().getId();
			String distributionWhere = "";
			
			if(StringUtils.isNotEmpty(keyword)){
				aopWhere +=   "and  (r.aoperator_user_json like '%\"userName\":\""+keyword+"%' or r.toperator_user_json  like '%\"userName\":\""+keyword+"%' "
						+ " or bi.customer_name like '%"+keyword+"%' or ar.travel_country like  '%"+keyword+"%') ";
				 
				topWhere +="and (r.aoperator_user_json like '%\"userName\":\""+keyword+"%' or r.toperator_user_json  like '%\"userName\":\""+keyword+"%' "
						+ " or bi.customer_name like '%"+keyword+"%' ) ";
				
			}
			
//			//计调id
			if(operatorUserId!=null && operatorUserId>0){
				aopWhere += " and  (r.aoperator_user_json like '%\"userId\":"+operatorUserId+",%' or r.toperator_user_json like '%\"userId\":"+operatorUserId+",%')";
				topWhere += " and  (r.aoperator_user_json like '%\"userId\":"+operatorUserId+",%' or r.toperator_user_json like '%\"userId\":"+operatorUserId+",%')";
			}
			
//			if (estimateStatus != null && estimateStatus >= 0) {
//				if (estimateStatus == 6) {
//					aopWhere +=  " and  p.estimate_status = 1";
//					topWhere +=  " and  p.estimate_status = 1";
//				} else {
//					aopWhere +=  " and  p.estimate_status = " + estimateStatus;
//					topWhere +=  " and  p.estimate_status = " + estimateStatus;
//				}
//				
//				if (estimateStatus == 6) {
//					distributionWhere = " and op_id is null";
//				} else if (estimateStatus == 1) {
//					distributionWhere = " and op_id is not null";
//				}
//			}
			if(estimateStatus != null && estimateStatus >= 0){
				aopWhere +=  " and  p.estimate_status = " + estimateStatus;
				topWhere +=  " and  p.estimate_status = " + estimateStatus;
			}
			
			if(!StringUtils.isBlank(lsf.getGroupOpenDate())){
				aopWhere +=  " and DATE_FORMAT(ar.dgroup_out_date,'%Y%m%d') >=DATE_FORMAT('"+lsf.getGroupOpenDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(r.last_toperator_start_out_time,'%Y%m%d') >=DATE_FORMAT('"+lsf.getGroupOpenDate()+"','%Y%m%d')"; 
			}
			
			if(!StringUtils.isBlank(lsf.getGroupCloseDate())){
				aopWhere +=  " and DATE_FORMAT(ar.dgroup_out_date,'%Y%m%d') <=DATE_FORMAT('"+lsf.getGroupCloseDate()+"','%Y%m%d')";
				topWhere += " and DATE_FORMAT(r.last_toperator_start_out_time,'%Y%m%d') <=DATE_FORMAT('"+lsf.getGroupCloseDate()+"','%Y%m%d')"; 
			}
			
			if(type!=null && type>0){
				aopWhere +=  " and  p.type="+type;
				topWhere +=  " and  p.type="+type;
			}
			
			if(travelCountryId!=null && travelCountryId>0){
				aopWhere +=  " and  (ar.travel_country_id like '%"+travelCountryId+",%' or ar.travel_country_id like '%"+travelCountryId+"]' ) " ;
				topWhere +=  " and 1=2 ";
			}
			
			if(!StringUtils.isBlank(lsf.getCustId())&&!lsf.getCustId().equals("-1")){
				aopWhere +=  " and  bi.customer_agent_id =  " + lsf.getCustId();
				topWhere +=  " and  bi.customer_agent_id =  " + lsf.getCustId() ;
			}
			
			
			if(salerId!=null&&salerId>0){
				aopWhere +=  " and  p.user_id="+salerId;
				topWhere +=  " and  p.user_id="+salerId;
				
				
			}
			
			String sql = " select u.id as id," +                                                 //0
						"u.title as title ," +                                                   //1
						"u.user_id as user_id ," +                                                //2
						"u.user_name as user_name ," +                                                //3
						"u.company_id as company_id," +                                                //4
					     " u.company_name as company_name," +                                          //5
					     "u.last_record_id as last_record_id," +                                      //6
					     "u.last_base_info_id as last_base_info_id ," +                               //7
					     " u.last_admit_requirements_id as last_admit_requirements_id, " +          //8
					     " u.last_traffic_requirements_id as last_traffic_requirements_id,  " +      //9
					     "  u.estimate_status as estimate_status ," +  //10
					     "u.status as status," +//11
					     "u.estimate_price_sum as estimate_price_sum," +//12
					     "u.type as type," +//13
					     " u.last_estimate_price_time as last_estimate_price_time," +//14
					     "u.last_operator_given_time as last_operator_given_time," +//15
					     " u.last_create_product_time as last_create_product_time," +//16
					     "u.last_create_order_time as last_create_order_time," +//17
					     " u.last_cancel_time as last_cancel_time," +//18
					     "u.create_time as create_time ," +//19
					     "u.modify_time as modify_time ," +//20
					     "u.remark as remark,        " +//21
					     "distribution.op_id as opId        " +//22
					     " from ( ("+ aopFrom + aopWhere +") union ("+ topFrom  + topWhere +") ) u, (" + distributionSql + distributionWhere + ") distribution " 
					     + " where 1=1 "
					     + "and u.estimateBaseInfoId = distribution.estimate_base_id" 
					     + " group by" +
					     " u.id,u.title,u.user_id,u.user_name,u.company_id,u.company_name,u.last_record_id,u.last_base_info_id,u.last_admit_requirements_id," +
					     "u.last_traffic_requirements_id,u.estimate_status,u.status,u.estimate_price_sum,u.type,u.last_estimate_price_time," +
					     "u.last_operator_given_time,u.last_create_product_time,u.last_create_order_time,u.last_cancel_time,u.create_time,u.modify_time,u.remark";
			
            String orderBy = " order by ";
			boolean t = false;
			if(map!=null){
				Iterator<String> it = map.keySet().iterator();
				String key;
				Integer v;
				while (it.hasNext()) {
					key = it.next();
					v = map.get(key);

					if("lastOperatorGivenTime".equals(key)){
						key = "last_operator_given_time";
					}
					if("lastEstimatePriceTime".equals(key)){
						key = "last_estimate_price_time";
					}
					if("lastCreateProductTime".equals(key)){
						key = "last_create_product_time";
					}
					if("lastCancelTime".equals(key)){
						key = "last_cancel_time";
					}
					
					String de = " asc ";
					if(v==1){
						de = "  desc ";
					}
					orderBy += " u."+key+de+" ,";
					t  =  true;
				}
			}
			if(!t){
				orderBy += " u.modify_time desc";
			}else{
				orderBy = orderBy.substring(0,orderBy.length()-2);
			}
			
			
			String countSql = " select count(1) as count   from ( " +sql+" ) u1";
			String sql1 =sql + orderBy ;
			Query countQ = this.getSession().createSQLQuery(countSql);
			Query query1 = this.getSession().createSQLQuery(sql1);
			
			int count = Integer.parseInt(((BigInteger)countQ.uniqueResult()).toString());  
			
			query1.setFirstResult((pageNo - 1) * pageSize);  
			query1.setMaxResults(pageSize);  
			
			Page<EstimatePriceProject> page = new Page<EstimatePriceProject>(pageNo,pageSize,count);
	        page.setResult(query1.list()	);
	        
	        return page;  
		}

}