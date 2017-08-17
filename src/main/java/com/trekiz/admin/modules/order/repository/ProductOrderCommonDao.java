package com.trekiz.admin.modules.order.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

public interface ProductOrderCommonDao extends ProductOrderCommonDaoCustom, CrudRepository<ProductOrderCommon, Long> {

	@Modifying
    @Query("update ProductOrderCommon set delFlag='" + ProductOrderCommon.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Query("from ProductOrderCommon where orderNum=?1 and createBy.company.uuid = ?2 and delFlag='" + Area.DEL_FLAG_NORMAL + "' ")
	public List<ProductOrderCommon> findByOrderNum(String orderNum, String companyUuid);
	
	@Query("from ProductOrderCommon where orderNum=?1 and createBy.company.uuid = ?2 and delFlag in(0, 4)")
	public List<ProductOrderCommon> findByOrderNum4Discount(String orderNum, String companyUuid);
	
	@Query("from ProductOrder where id=?1 and delFlag='" + Area.DEL_FLAG_NORMAL + "' ")
	public List<ProductOrder> findByOrderId(long id);

	@Query("from ProductOrderCommon where productGroupId=?1 and delFlag='0'")
	public List<ProductOrderCommon> findByActivityGroupId(Long activityGroupId);
	
	@Query("select orderCompany as agentId,sum(orderPersonNum) as persons,sum(totalMoney) as sales from ProductOrderCommon where orderCompany in ?1 group by orderCompany")
	public List<Object[]> findSalePersonsByAgent(List<Long> agentIds);
	
	@Query("from ProductOrderCommon p where productGroupId = ?1 and (payStatus != 99 and payStatus != 111) and delFlag='" + Area.DEL_FLAG_NORMAL + "' order by  p.id ASC ")
	public List<ProductOrderCommon> findByProductGroupIdOrderByCompany(Long id);
	@Query(value="SELECT GROUP_CONCAT(p.id ORDER BY p.id ASC) FROM productorder p " +
			" WHERE p.productGroupId = ?1 AND p.payStatus != 99 AND p.payStatus != 111 AND p.delFlag = 0 GROUP BY p.salerId ORDER BY p.id",nativeQuery = true)
	public List<Object> findOrderIdsByProductGrouopId(Long orderId);
	
	@Query("from ProductOrderCommon p where orderCompany=?1 and (payStatus != 99 and payStatus != 111)")
	public List<ProductOrderCommon> findByOrderCompany(Long agentId);
	
	@Modifying
    @Query("update ProductOrderCommon set payStatus=?2,cancelDescription=?3 where id = ?1")
    public int cancelOrder(Long id,Integer status, String description);
			
	@Modifying
	@Query("update ProductOrderCommon set orderCompanyName = ?2 where orderCompany = ?1")
	public int updateOrderCompanyName(Long orderCompany, String orderCompanyName);
	
	@Modifying
	@Query("update ProductOrderCommon set remainDays=?2,payStatus=?3,activationDate=?4 where id=?1")
	public int invokeOrder(Long id, Double remainDays, Integer payStatus, Date activationDate);

	@Query(value = "select sum(orderPersonNum) from ProductOrderCommon where productGroupId=?1 and delFlag=0")
	public Long findTotalOrderPersonNumByGroupId(Long groupId);
	
	@Query(value = "select id from ProductOrderCommon where preOrderId = ?1")
	public Long getOrderIdByPreOrderId(Long groupId);
	
	/**
	 *  add bu ruyi.chen
	 *  add date 2014-11-19
	 *  修改主订单表 锁表状态   
	 * @param lockStatus
	 * @param orderId
	 * @return
	 */
	@Modifying
	@Query("update ProductOrderCommon set lockStatus=?1 where id=?2")
    public int updateLockStatus(Integer lockStatus, Long orderId);
	
	@Modifying
	@Query("update ProductOrderCommon set seenFlag = 1 where id in ?1")
    public int changeNotSeenOrderFlag(Set<Long> notSeenOrderIdList);
	
	@Modifying
	@Query(value = "update ProductOrderCommon set isPayedCharge =?2 where id=?1 and delFlag=0")
	public int changeOrderIsPayedCharge(Long id,Integer isPayedCharge);

}


 /**
 *  文件名: ProductOrderCommonDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:25
 *  @version 1.0
 */
interface ProductOrderCommonDaoCustom extends BaseDao<ProductOrderCommon> {
	public List<ProductOrderCommon> getProductOrderList(Long activityGroupID);
	/**add by kai.xiao
	 * add date 2015-10-26
	 * 补充 SQL 分页查询 方法  在分页前根据业务处理数据
	 * 使用此方法时注意order by内容放到page中，语句末尾不要加order by内容
	 * @param page
	 * @param sqlString
	 * @param mapRequest
	 * @param moneyAmountService
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> findPageBySqlEx(Page<E> page, String sqlString,Map<String, String> mapRequest, MoneyAmountService moneyAmountService, Object... parameter);

	/**
     * 根据团期id查询该团期下的订单id 和 订单orderNo
     * @param productGroupId
     * @return
     * @author xianglei.dong
     */
	public List<Map<String, Object>> findOrderIdAndNoByGroupId(Long productGroupId);
}

 /**
 *  文件名: ProductOrderCommonDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:15
 *  @version 1.0
 */
@Repository
class ProductOrderCommonDaoImpl extends BaseDaoImpl<ProductOrderCommon> implements ProductOrderCommonDaoCustom {
	/**
	 *add by kai.xiao
	 *add date 2015-10-26 
	* @Title: getPageList
	* @Description: TODO(分页方法)
	* @param @param list 源数据
	* @param @param currentPage 当前页
	* @param @param maxNum 每页显示几条
	* @param @param pageNum 总页数
	* @param @return    设定文件
	* @return List    返回类型
	* @throws
	 */
	public static List<?> getPageList(List<?> list, int currentPage, int maxNum, int pageNum) {
		// 从哪里开始截取
		int fromIndex = 0;
		// 截取几个
		int toIndex = 0; 
		if (list == null || list.size() == 0)
			return null;
		//当前页小于或等于总页数时执行
		if (currentPage <= pageNum && currentPage != 0) {
			fromIndex = (currentPage - 1) * maxNum;
			if (currentPage == pageNum) {
				toIndex = list.size();
			} else {
				toIndex = currentPage * maxNum;
			}
		}
		return list.subList(fromIndex, toIndex);
	}
	/**add by kai.xiao
	 * add date 2015-10-26
	 * 补充 SQL 分页查询 方法  在分页前根据业务处理数据
	 * 使用此方法时注意order by内容放到page中，语句末尾不要加order by内容
	 * @param page
	 * @param sqlString
	 * @param mapRequest
	 * @param moneyAmountService
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Page<E> findPageBySqlEx(Page<E> page, String sqlString, Map<String, String> mapRequest, MoneyAmountService moneyAmountService, Object... parameter) {
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(id) " + removeSelect(removeOrders(sqlString));
	        org.hibernate.Query query = createSqlQuery(countQlString, parameter);
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
    	// order by
    	String ql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
		org.hibernate.SQLQuery query = createSqlQuery(ql, parameter);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getMaxResults());
		List<?> pageList = query.list();
        List<Map<Object, Object>> removeList = new ArrayList<Map<Object, Object>>();
        String option = mapRequest.get("option");
        //付款状态
		String payStatus = mapRequest.get("payStatus") == null ? "0" : mapRequest.get("payStatus");
		//达帐状态
		String accountStatus = mapRequest.get("accountStatus") == null ? "0" : mapRequest.get("accountStatus");
        if(StringUtils.isNotBlank(option) && "detail".equals(option)) {
        	if(StringUtils.isNotBlank(payStatus) && StringUtils.isNotBlank(accountStatus)) {
        		// set page
                if (!page.isDisabled()){
        	        query.setFirstResult(page.getFirstResult());
        	        query.setMaxResults(page.getMaxResults()); 
        	        page.setList((List<E>) pageList);
                }
        	}else {
        		for(int i = 0; i < pageList.size(); i++) {
        			Map<Object, Object> m = (Map<Object, Object>)pageList.get(i);
    				//达账金额UUID
        			String accountedMoneyUUID = m.get("accountedMoney") == null ? "" : m.get("accountedMoney").toString();
        			//已付金额UUID
        			String payedMoneyUUID = m.get("payedMoney") == null ? "" : m.get("payedMoney").toString();
        			//订单总额UUID
//        			String totalMoneyUUID = m.get("totalMoney") == null ? "" : m.get("totalMoney").toString();
        			//达账金额
        			BigDecimal accountedMoney = new BigDecimal("0");
        			List<Object[]> accountedMoneyList = moneyAmountService.getMoneyAmonut(accountedMoneyUUID);
        			if(accountedMoneyList != null && accountedMoneyList.size() > 0) {
        				accountedMoney = BigDecimal.valueOf(Double.parseDouble(accountedMoneyList.get(0)[3].toString()));
        			}else {
        				accountedMoney = BigDecimal.valueOf(-0.31415);
        			}
        			//已付金额
        			BigDecimal payedMoney = new BigDecimal("0");
        			List<Object[]> payedMoneyList = moneyAmountService.getMoneyAmonut(payedMoneyUUID);
        			if(payedMoneyList != null && payedMoneyList.size() > 0) {
        				payedMoney = BigDecimal.valueOf(Double.parseDouble(payedMoneyList.get(0)[3].toString()));
        			}else {
        				payedMoney = BigDecimal.valueOf(-0.31415);
        			}
        			//订单总额
//        			BigDecimal totalMoney = new BigDecimal("0");
//        			List<Object[]> totalMoneyList = moneyAmountService.getMoneyAmonut(totalMoneyUUID);
//        			if(totalMoneyList != null && totalMoneyList.size() > 0) {
//        				totalMoney = BigDecimal.valueOf(Double.parseDouble(totalMoneyList.get(0)[3].toString()));
//        			}else {
//        				totalMoney = BigDecimal.valueOf(0.000);
//        			}
        			if(StringUtils.isNotBlank(payStatus) && StringUtils.isNotBlank(accountStatus)) {
        				switch (payStatus) {
//        					case "1" :
//        						//已付全款：即搜出所有已付金额>=订单总额的数据
//        						if(payedMoney.doubleValue() < totalMoney.doubleValue()) {
//        							removeList.add(m);
//        						}
//        						break;
        					case "2" :
        						//未付款：即搜出所有已付金额为空的数据
        						if(payedMoney.doubleValue() != -0.31415) {
        							removeList.add(m);
        						}
        						break;
//        					case "3" :
//        						//未付全款：即搜出所有已付金额<订单总额的数据
//        						if(payedMoney.doubleValue() >= totalMoney.doubleValue() || payedMoney.doubleValue() == 0.000) {
//        							removeList.add(m);
//        						}
//        						break;
        				}
        				switch (accountStatus) {
//	    					case "1" :
//	    						//全款达账：即搜出所有达账金额>=订单总额的数据
//	    						if(accountedMoney.doubleValue() < totalMoney.doubleValue()) {
//	    							removeList.add(m);
//	    						}
//	    						break;
	    					case "2" :
	    						//未达账：即搜出所有达账金额为空的数据
	    						if(accountedMoney.doubleValue() != -0.31415) {
	    							removeList.add(m);
	    						}
	    						break;
//	    					case "3" :
//	    						//部分达账：即搜出所有达账金额<订单总额的数据
//	    						if(accountedMoney.doubleValue() >= totalMoney.doubleValue() || accountedMoney.doubleValue() == 0.000) {
//	    							removeList.add(m);
//	    						}
//	    						break;
        				}
        			}
        		}
        		//删除不需要的数据
        		pageList.removeAll(removeList);
        		//当前页
        		int currentPage = 1;
        		if(StringUtils.isNotBlank(mapRequest.get("pageNo"))) {
        			currentPage = Integer.parseInt(mapRequest.get("pageNo"));
        		}
        		//每页显示几条
        		int maxNum = 10;
        		if(StringUtils.isNotBlank(mapRequest.get("pageSize"))) {
        			maxNum = Integer.parseInt(mapRequest.get("pageSize"));
        		}
        		//总页数
        		int pageNum = 0;
        		if(pageList != null && pageList.size() > 0) {
        			pageNum = pageList.size() / maxNum + 1;
        		}
        		//数据分页处理
        		page.setList((List<E>) getPageList(pageList, currentPage, maxNum, pageNum));
        		//设置总条数
        		page.setCount(pageList.size());
        		//设置当前页
        		page.setPageNo(currentPage);
        		//设置每页显示几条
        		page.setPageSize(maxNum);
        	}
        }
		return page;
	}
	public List<ProductOrderCommon> getProductOrderList(Long activityGroupID){
		List<ProductOrderCommon> list = new ArrayList<ProductOrderCommon>();
		if(activityGroupID!=null){
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT ");
			sql.append(" porder.orderNum as orderNum, ");
			sql.append(" porder.orderCompanyName as orderCompanyName, ");
			sql.append(" porder.salerName as salerName, ");
			sql.append(" porder.createBy as createBy, ");
			sql.append(" porder.orderTime as orderTime, ");
			sql.append(" porder.orderPersonNum as orderPersonNum, ");
			sql.append(" porder.payStatus as payStatus, ");
			sql.append(" porder.total_money as totalMoney, ");
			sql.append(" porder.payed_money as payedMoney, ");
			sql.append(" porder.accounted_money as accountedMoney ");
			sql.append(" FROM productorder porder");
			sql.append(" WHERE delFlag = 0 AND porder.payStatus != 99 AND porder.payStatus != 111 AND porder.productGroupId="+activityGroupID);
		//	sql.append(" FROM productorder porder, activitygroup agroup");
		//	sql.append(" WHERE porder.productGroupId = agroup.id AND agroup.groupCode= "+activityGroupID );
			List<Map<String,Object>> templist = new ArrayList<Map<String,Object>>();  // 查询数据列表
			try{
				templist = findBySql(sql.toString(),Map.class);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(!templist.isEmpty()){
				for(Map<String,Object> map : templist){
					ProductOrderCommon info = new ProductOrderCommon();
					info.setOrderNum(map.get("orderNum")!=null?map.get("orderNum").toString():null);// 订单号
					info.setSalerName(map.get("salerName")!=null?map.get("salerName").toString():null);// 销售名
					info.setOrderCompanyName(map.get("orderCompanyName")!=null?map.get("orderCompanyName").toString():null);// 渠道
					if(map.get("createBy")!=null){
						User user = UserUtils.getUser(Long.valueOf(map.get("createBy").toString()));
						info.setCreateBy(user);// 下单人
					}
					if(map.get("orderTime")!=null){
						info.setOrderTime((Date)map.get("orderTime"));// 预订时间
					}
					info.setOrderPersonNum(map.get("orderPersonNum")!=null?Integer.valueOf(map.get("orderPersonNum").toString()):0); // 总人数
					// 支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消
					info.setPayStatus(map.get("payStatus")!=null?Integer.valueOf(map.get("payStatus").toString()):99);
					info.setTotalMoney(map.get("totalMoney")!=null?map.get("totalMoney").toString():null);
					info.setPayedMoney(map.get("payedMoney")!=null?map.get("payedMoney").toString():null);
					info.setAccountedMoney(map.get("accountedMoney")!=null?map.get("accountedMoney").toString():null);
					list.add(info);
				}
			}
		}
		return list;
	}
	
	/**
     * 根据团期id查询该团期下的订单id 和 订单orderNo
     * @param productGroupId
     * @return
     * @author xianglei.dong
     */
	@Override
	public List<Map<String, Object>> findOrderIdAndNoByGroupId(Long productGroupId) {
		String sql = "select po.id as orderId, po.orderNum as orderNo from productorder po where po.productGroupId=";
		StringBuffer sb = new StringBuffer(sql);
		sb.append(productGroupId).append(" and delFlag='").append(Area.DEL_FLAG_NORMAL).append("' order by po.id ASC");
		List<Map<String, Object>> list = findBySql(sb.toString(), Map.class);
		return list;
	}
	
}
