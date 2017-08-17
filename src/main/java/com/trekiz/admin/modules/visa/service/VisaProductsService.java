package com.trekiz.admin.modules.visa.service;

import com.google.common.collect.Lists;
import com.ibm.icu.math.BigDecimal;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.entity.VisaProductsCostView;
import com.trekiz.admin.modules.visa.repository.VisaProductsCostViewDao;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class VisaProductsService extends BaseService implements
		IVisaProductsService {

	@Autowired
	private VisaProductsDao visaProductsDao;
	@Autowired
	private VisaProductsCostViewDao visaProductsCostViewDao;
	
	@Autowired
	private CostRecordLogDao costRecordLogDao;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private UserDao userDao;
	
	/*成本录入模块列表页 ，不含 nowLevel 参数*/
	@Override
	public Page<Map<String, Object>> findVisaProductsReviewPage(Page<Map<String, Object>> page,
			VisaProducts visaProducts, Map<String, Object> params) {
		Object productName = params.get("productName");
		Object collarZoning = params.get("collarZoning");
		Object sysCountryId = params.get("sysCountryId");
		Object visaType = params.get("visaType");
		Object commitType = params.get("commitType");
		Object operator = params.get("operator");
		Object isShenYangDept = params.get("isShenYangDept");
		Object isReject = params.get("isReject");	// 540需求 签证页面添加驳回标识 王洋 2017.3.22
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer column = new StringBuffer();
		column.append("t.id, t.groupCode, t.productName, t.sysCountryId, t.visaType, t.currencyId, t.visaPrice, t.visaPay,")
		      .append(" t.collarZoning, t.createDate, t.updateDate, t.operator, t.deptId, IFNULL(COUNT(t_rn.id), 0) AS rejectCount ");
		StringBuffer field = new StringBuffer();
		field.append("p.id, p.groupCode, p.productName, p.sysCountryId, p.visaType, p.currencyId, p.visaPrice, p.visaPay, ")
		     .append(" p.collarZoning, p.createDate, p.updateDate, (SELECT name FROM sys_user su WHERE su.id = p.createBy) ")
			 .append(" AS operator, p.deptId ");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(column).append(" FROM (")
		  .append(" SELECT ").append(field).append(" FROM visa_products p WHERE p.delFlag = 0 ")
		  .append(" AND p.productStatus = 2 AND p.proCompanyId = ").append(companyId)
		  .append(" UNION ")
		  .append(" SELECT ").append(field).append(" FROM visa_products p, visa_order o WHERE p.id = o.visa_product_id ")
		  .append(" AND (p.delFlag = 1 OR p.productStatus = 3) AND p.proCompanyId = ").append(companyId)
		  .append(" ) t LEFT JOIN ( SELECT rn.id, cost.activityId ")
		  // ----- 540需求，运控签证页面添加驳回标识   王洋 2017.3.22
		  .append(" FROM review_new rn INNER JOIN cost_record cost ON cost.pay_review_uuid = rn.id ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--start
		  .append(" AND cost.orderType = rn.product_type AND cost.delFlag = 0 WHERE rn.del_flag = '0' AND rn.process_type = 18 ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--end
		  .append(" AND rn.need_no_review_flag = 0 AND rn.product_type = 6 AND rn.`status` = 0 ")
		  .append(" AND rn.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ")
		  .append(" UNION SELECT rn.id, cost.activityId FROM ")
		  .append(" review_new rn INNER JOIN cost_record cost ON cost.reviewUuid = rn.id ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--start
		  .append(" AND cost.orderType = rn.product_type AND cost.delFlag = 0 WHERE rn.del_flag = '0' ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--end
		  .append(" AND rn.process_type IN (15, 17) AND rn.need_no_review_flag = 0 ")
		  .append(" AND rn.product_type = 6 AND rn.`status` = 0 ")
		  .append(" AND rn.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ")
		  .append(" ) t_rn ON t_rn.activityId = t.id WHERE 1=1 ");
		//未提交  0416   update by shijun.liu   2016.04.29
		// 存在审批通过 并且有未提交付款审批的数据
		if("1".equals(commitType)){//
			sb.append(" AND EXISTS (SELECT c.id FROM cost_record c WHERE c.activityId = t.id AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview IN (5,").append( ReviewConstant.REVIEW_STATUS_REJECTED).append("))) ")
			  .append(" AND c.reviewType = 0 AND c.budgetType = 1 AND c.review = 2 AND c.orderType = ").append(Context.ORDER_TYPE_QZ)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		//已提交  0416   update by shijun.liu   2016.04.29
		//已通过成本审批，并且已提交付款审批(all)
		// 首先必须有成本，并且所有的成本都进行了付款审批的提交
		if("2".equals(commitType)){
			sb.append(" AND NOT EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = t.id AND c.reviewType = 0 AND c.budgetType = 1 ")
			  .append(" AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview IN (5,").append( ReviewConstant.REVIEW_STATUS_REJECTED).append("))) ")
			  .append(" AND c.orderType = ").append(Context.ORDER_TYPE_QZ)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ")
			  .append(" AND EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = t.id AND c.reviewType = 0 AND c.budgetType = 1 ")
			  .append(" AND c.orderType = ").append(Context.ORDER_TYPE_QZ)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		if(Boolean.getBoolean(String.valueOf(isShenYangDept))) {
			sb.append(" AND t.deptId in (25,32,33,39)");
		}
		if(null != productName && StringUtils.isNotEmpty(String.valueOf(productName))) {
			sb.append(" AND t.productName like '%").append(String.valueOf(productName)).append("%'");
		}
		if(null != collarZoning && StringUtils.isNotEmpty(String.valueOf(collarZoning))) {
			sb.append(" AND t.collarZoning = '").append(String.valueOf(collarZoning)).append("' ");
		}
		if(null != sysCountryId && StringUtils.isNotEmpty(String.valueOf(sysCountryId))) {
			sb.append(" AND t.sysCountryId =").append(Integer.parseInt(String.valueOf(sysCountryId)));
		}
		if(null != visaType && StringUtils.isNotEmpty(String.valueOf(visaType))) {
			sb.append(" AND t.visaType =").append(Integer.parseInt(String.valueOf(visaType)));
		}
		if(null != operator && StringUtils.isNotEmpty(String.valueOf(operator))) {
			sb.append(" AND t.operator like '%").append(String.valueOf(operator)).append("%' ");
		}
		
		// 540需求，以id分组，用于统计被驳回的成本审批数量 王洋 2017.3.22
		sb.append(" GROUP BY t.id ");
		if ("1".equals(isReject)) {
			sb.append(" HAVING IFNULL(COUNT(t_rn.id), 0) > 0 ");
		}
		String order = page.getOrderBy();
		if(StringUtils.isBlank(order)){
			page.setOrderBy("createDate DESC");
		}
		return visaProductsDao.findBySql(page, sb.toString(), Map.class);

	}
	
	/*包含 nowLevel 参数*/
	@Override
	public Page<VisaProducts> findVisaReviewPage(Page<VisaProducts> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType, String review,Integer nowLevel,Long companyId,String orderBy) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = visaProductsDao.createDetachedCriteria();

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));

		dc.add(Restrictions.eq("productStatus", visaProducts.getProductStatus()));

		if (StringUtils.isNotBlank(productName)) {
			dc.add(Restrictions.like("productName", "%" + productName + "%"));
		}
		if (StringUtils.isNotBlank(collarZoning)) {
			dc.add(Restrictions.eq("collarZoning", collarZoning));
		}
		dc.add(Restrictions.eq("proCompanyId", companyId));
		
		if (StringUtils.isNotBlank(sysCountryId)) {
			dc.add(Restrictions.eq("sysCountryId", new Integer(sysCountryId)));
		}
		
		if (StringUtils.isNotBlank(review)) {	
			if(review.equals(Context.REVIEW_COST_NEW.toString())){	//待录入			
			    dc.add(Restrictions.eq("review", Context.REVIEW_COST_NEW)); 
			}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//待当前层级审核 			
				dc.add(Restrictions.eq("nowLevel", nowLevel)); 
				dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//(当前层级)已经通过, 并没有被其他层级驳回
			    dc.add(Restrictions.gt("nowLevel", nowLevel)); 
			    dc.add(Restrictions.ne("review", Context.REVIEW_COST_FAIL)); 
			}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回
				//dc.add(Restrictions.eq("nowLevel", nowLevel)); 
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
			}
		}else if(nowLevel >1 ){
			/*经理审核nowLevel=2 时，全部数据列表需要过滤掉 待财务审核 的记录=待录入记录 + nowLevel大于1(财务审核)的记录*/
			 dc.add(Restrictions.or(Restrictions.eq("review", Context.REVIEW_COST_NEW),Restrictions.gt("nowLevel", (Integer)1) )); 
		}
		

		if (StringUtils.isNotBlank(visaType)) {
			dc.add(Restrictions.eq("visaType", new Integer(visaType)));
		}

		if (StringUtils.isEmpty(page.getOrderBy())) {
			page.setOrderBy(orderBy);
			dc.addOrder(Order.desc("id"));
		}
		return visaProductsDao.find(page, dc);

	}
	
	
	/*包含 nowLevel 参数*/
	@Override
	public Page<VisaProductsCostView> findVisaCostViewPage(Page<VisaProductsCostView> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType, String review,Integer nowLevel,Long companyId,
			Long reviewCompanyId,Integer supplyId,Integer agentId,Integer flowType,String orderBy,String createByName) {

		// TODO Auto-generated method stub
		DetachedCriteria dcUser = userDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(createByName)) {
			dcUser.add(Restrictions.like("name", "%" + createByName + "%"));
		}
		
		List<User> list = userDao.find(dcUser);
		User[] createBy = new User[list.size()];
		for (int i = 0; i < list.size(); i++) {
			createBy[i] = list.get(i);
		}
		
		DetachedCriteria dc = visaProductsCostViewDao.createDetachedCriteria();

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
        
       
    	if (flowType==15) {
			dc.add(Restrictions.lt("review", 4));
			dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId));	
		}
		else{
			if ( ! StringUtils.isNotBlank(review)){
				dc.add(Restrictions.lt("payReview", 4));
			}
			dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId));
			dc.add(Restrictions.eq("budgetType", 1));
		}
		//dc.add(Restrictions.eq("productStatus", visaProducts.getProductStatus()));

		if (StringUtils.isNotBlank(productName)) {
			dc.add(Restrictions.like("productName", "%" + productName + "%"));
		}
		if (StringUtils.isNotBlank(collarZoning)) {
			dc.add(Restrictions.eq("collarZoning", collarZoning));
		}
		dc.add(Restrictions.eq("proCompanyId", companyId));
		
		if (StringUtils.isNotBlank(sysCountryId)) {
			dc.add(Restrictions.eq("sysCountryId", new Integer(sysCountryId)));
		}
		
		if(supplyId !=null){
			dc.add(Restrictions.eq("supplyType", 0));
			dc.add(Restrictions.eq("supplyId",supplyId));
		}
		
		if(supplyId !=null){
			dc.add(Restrictions.eq("supplyType", 0));
			dc.add(Restrictions.eq("supplyId",supplyId));
		}
		
		if(agentId !=null){
			dc.add(Restrictions.eq("supplyType", 1));
			dc.add(Restrictions.eq("supplyId",agentId));
		}
		
			
			if (flowType==15) {
				 if(StringUtils.isBlank(review)){	//待本人审核			
					dc.add(Restrictions.eq("nowLevel", nowLevel)); 
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				 }else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中		
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				 }else if(review.equals(Context.REVIEW_COST_PASS.toString())){//(已经通过
				    dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS)); 
				 }else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//已经驳回				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
				 }else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
						dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL)); 
					 }
			}
			if (flowType==18) {
				 if(StringUtils.isBlank(review)){	//待本人审核			
					dc.add(Restrictions.eq("payNowLevel", nowLevel)); 
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				 }else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中			
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				 }else if(review.equals(Context.REVIEW_COST_PASS.toString())){//(已经通过
				    dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS)); 
				 }else if(review.equals("22")){//本人审核通过	   
					dc.add(Restrictions.gt("payNowLevel", nowLevel));
				 }else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//已经驳回				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL)); 
				 }else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
						dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL)); 
					 }
				}
			
		/* else if(nowLevel >1 ){
			//经理审核nowLevel=2 时，全部数据列表需要过滤掉 待财务审核 的记录=待录入记录 + nowLevel大于1(财务审核)的记录
			 dc.add(Restrictions.or(Restrictions.eq("review", Context.REVIEW_COST_NEW),Restrictions.gt("nowLevel", (Integer)1) )); 
		} */
		

		if (StringUtils.isNotBlank(visaType)) {
			dc.add(Restrictions.eq("visaType", new Integer(visaType)));
		}

		if (StringUtils.isEmpty(page.getOrderBy())) {
			page.setOrderBy(orderBy);
			dc.addOrder(Order.desc("id"));
		}

		if (StringUtils.isNotBlank(createByName) && list.size() > 0) {
			dc.add(Restrictions.in("createBy", createBy));
		} else if (StringUtils.isNotBlank(createByName) && list.size() == 0) {
			return page;
		}
		
		return visaProductsCostViewDao.find(page, dc);

	}

    @Override
    public VisaProducts getVisaProductById(Long visaProductId) {
        return visaProductsDao.getById(visaProductId);
    }

    @Override
	public Page<VisaProducts> findVisaProductsPage(Page<VisaProducts> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType, String orderBy, DepartmentCommon common) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = visaProductsDao.createDetachedCriteria();

		dc.add(Restrictions.eq("proCompanyId", UserUtils.getUser().getCompany().getId()));
		
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));

		dc.add(Restrictions.eq("productStatus", visaProducts.getProductStatus()));
		// deptId
		//C207 按产品名称或团号搜索
//		if (StringUtils.isNotBlank(productName)) {
//			dc.add(Restrictions.like("productName", "%" + productName.trim() + "%"));
//		}
		if(StringUtils.isNotBlank(productName)){
			productName = productName.replace("'", "''");
			productName = productName.replace("%", "\\%");
			productName = productName.replace("_", "\\_");
			dc.add(Restrictions.or(Restrictions.like("productName", "%"+productName.trim()+"%"),
					Restrictions.like("groupCode", "%"+productName.trim()+"%")));
		}
		
		if (StringUtils.isNotBlank(collarZoning)) {
			dc.add(Restrictions.eq("collarZoning", collarZoning));
		}

		if (StringUtils.isNotBlank(sysCountryId)) {
			dc.add(Restrictions.eq("sysCountryId", new Integer(sysCountryId)));
		}

		if (StringUtils.isNotBlank(visaType)) {
			dc.add(Restrictions.eq("visaType", new Integer(visaType)));
		}

		systemService.getDepartmentSql("activity", dc, null, common, null);
		
		if (StringUtils.isEmpty(page.getOrderBy())) {
			page.setOrderBy(orderBy);
			dc.addOrder(Order.desc("id"));
		}
		return visaProductsDao.find(page, dc);

	}
    
	@Override
	public VisaProducts findById(Long id) {
		return visaProductsDao.findOne(id);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void batchDelVisaProducts(List<Long> ids) {
		if(ids == null || ids.isEmpty()){
			throw new IllegalArgumentException("产品ids为空");
		}
		for(Long id : ids){
			VisaProducts visaProducts = findById(id);
			try {
				this.delActivity(visaProducts);
			} catch (Exception e) {
				throw new RuntimeException("删除产品失败");
			}
		}
		// TODO Auto-generated method stub
		//this.visaProductsDao.batchDelVisaProducts(ids);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivity(VisaProducts visaProducts){
		visaProducts.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		visaProductsDao.save(visaProducts);
	}

	@Override
	public void batchOnOrOffVisaProducts(List<Long> ids, Integer productStatus) {
		// TODO Auto-generated method stub
		this.visaProductsDao.batchOnOrOffVisaProducts(ids, productStatus);
	}

	@Override
	public void batchOnVisaProductsTmp(List<Long> ids) {
		// TODO Auto-generated method stub
		this.visaProductsDao.batchOnOrOffVisaProducts(ids, new Integer(
				Context.PRODUCT_ONLINE_STATUS));
	}

	@Override
	public void delVisaProducts(VisaProducts visaProducts) {
		// TODO Auto-generated method stub
		this.visaProductsDao.delete(visaProducts);
	}

	@Override
	public List<Map<String, Object>> findAreaIds(Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modSave(MultipartFile introduction,
			MultipartFile costagreement, MultipartFile otheragreement,
			List<MultipartFile> otherfile, List<MultipartFile> signmaterial,
			String groupdata, VisaProducts VisaProducts,
			HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public VisaProducts save(VisaProducts visaProduct) {
		// TODO Auto-generated method stub
		return this.visaProductsDao.save(visaProduct);
	}

	@Override
	public String save(MultipartFile introduction, MultipartFile costagreement,
			MultipartFile otheragreement, List<MultipartFile> otherfile,
			List<MultipartFile> signmaterial, VisaProducts visaProducts,
			String groupOpenDateBegin, String groupCloseDateEnd,
			HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes) {
		// TODO Auto-generated method stub
		return null;
	}

	public VisaProducts findByVisaProductsId(Long visaProductId) {
		return visaProductsDao.findOne(visaProductId);
	}

	@Override
	public List<Map<String, Object>> getProductInfoForForcast(Long productId){
		StringBuffer str = new StringBuffer();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String isLockedIn = "";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			//C457锁定后的数据不计入预报单 add by shijun.liu 2015.12.25
			//预报单锁定之后录入的新订单数据(forecast_locked_in == 1)，不进入预报单,解锁后(forecast_locked_in == null)进入
			isLockedIn = " AND o.forecast_locked_in is null ";
		}
		str.append("SELECT p.groupCode, p.productName,")
		   .append(" (SELECT su.`name` FROM sys_user su WHERE su.id = p.createBy) AS createBy,")
		   .append(" sum(o.travel_num) AS orderPersonNumSum,")
		   .append(" p.createBy as createById,p.lockStatus,p.forcastStatus,p.visaPay AS settlementAdultPrice, ")
		   .append(" p.createDate AS groupOpenDate, p.updateDate AS groupCloseDate, p.updateDate AS groupEndDate ")
		   .append(" FROM visa_products p left join visa_order o on o.visa_product_id = p.id ")
		   .append(" AND o.payStatus not in (7,99,111) AND o.visa_order_status<>100 and o.del_flag = '0' ")
		   .append(isLockedIn).append(" where p.id = ").append(productId);
		return visaProductsDao.findBySql(str.toString(), Map.class);
	}
	
	@Override
	public List<Map<String, Object>> getProductInfoForSettle(Long productId){
		StringBuffer str = new StringBuffer();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String isLockedIn = "";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			//C486锁定后的数据不计入结算单 add by shijun.liu 2015.12.25
			//结算单锁定之后录入的新订单数据(settle_locked_in == 1)，不进入结算单,解锁后(settle_locked_in == null)进入
			isLockedIn = " AND o.settle_locked_in is null ";
		}
		String invoiceTax = "";
		if(Context.SUPPLIER_UUID_YYJQ.equals(currentCompanyUuid)){
			invoiceTax = " ,p.invoice_qz AS invoiceTax ";
		}
		str.append("SELECT p.groupCode, p.productName,")
		   .append(" (SELECT su.`name` FROM sys_user su WHERE su.id = p.createBy) AS createBy,")
		   .append(" sum(o.travel_num) AS orderPersonNumSum,FORMAT(p.visaPay,2) AS settlementAdultPrice,")
		   .append("p.currencyId AS currencyType,")
		   .append(" p.createBy as createById,p.lockStatus,p.forcastStatus ").append(invoiceTax)
		   .append(" FROM visa_products p left join visa_order o on o.visa_product_id = p.id ")
		   .append(" AND o.payStatus not in (7,99,111) AND o.visa_order_status<>100 and o.del_flag = '0' ")
		   .append(isLockedIn).append(" where p.id = ").append(productId);
		return visaProductsDao.findBySql(str.toString(), Map.class);
	}
	
	public List<VisaProducts> findVisaProductsByCountryId(Integer countryId) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		return visaProductsDao.findVisaProductsByCountryId(countryId, companyId);
	}

	/**
	 * 根据国家Id和领区获取签证产品
	 * @return
	 */
	public List<VisaProducts> findVisaProductsByCountryIdAndManor(int countryId, String manor){
		Long companyId = UserUtils.getUser().getCompany().getId();
		return visaProductsDao.findVisaProductsByCountryIdAndManor(countryId, manor, companyId);
	}

	
	/**
	 * 可以办理签证的国家
	 * 
	 * @author jiachen
	 * @DateTime 2014-12-3 下午01:54:25
	 * @return List<Object[]>
	 */
	public List<Object[]> findCountryInfoList() {
		List<Object[]> countryInfoList = new ArrayList<Object[]>();
		Long companyId = UserUtils.getUser().getCompany().getId();
		countryInfoList = visaProductsDao.findCountryInfoList(companyId);
		return countryInfoList;
	}

	public List<Object[]> findCountryInfoList(Long companyId) {
		List<Object[]> countryInfoList = new ArrayList<Object[]>();
		countryInfoList = visaProductsDao.findCountryInfoList(companyId);
		return countryInfoList;
	}

	/**
	 * 办理签证国家的领区
	 * 
	 * @author jiachen
	 * @DateTime 2014-12-3 下午01:55:02
	 * @return List<Object[]>
	 */
	public List<Object[]> findVisaCountryArea(Integer countryId) {
		List<Object[]> visaCountryAreaList = new ArrayList<Object[]>();
		Long companyId = UserUtils.getUser().getCompany().getId();
		visaCountryAreaList = visaProductsDao.findVisaCountryArea(countryId, companyId);
		return visaCountryAreaList;
	}
	
	/**
	 * 根据 签证国家，签证类型，签证领区 
	 * xinwei.wang added 2014-12-03
	 * @param countryId
	 * @param visaType
	 * @param collarZoning
	 * @return
	 */
	public VisaProducts findVisaProductsByCountryTypeCollarZonID(Integer countryId, Integer visaType, String collarZoning){
		List<VisaProducts> visaList = null;
		Long proCompanyId = UserUtils.getUser().getCompany().getId();
		if (null!=countryId&&null!=visaType&&StringUtils.isNotEmpty(collarZoning)) {
			visaList =visaProductsDao.findVisaProductsByCountryTypeCollarZonID(countryId, visaType, collarZoning, proCompanyId);
		}
		if (visaList!=null&&visaList.size()>0) {
			return visaList.get(0);
		}else {
			return null;
		}
	}

	@Override
	public List<Object> findFileListByProId(Long visaProdectsId,
			boolean isDownLoad) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//提交成本审核
	public void submitReview(Long id,Integer review) {		
		this.visaProductsDao.submitReview(id,review);		
	}
	
	//提交成本审核
	public void updateReview(Long id, Integer review,Integer nowLevel,CostRecordLog costRecordLog) {		
		this.costRecordLogDao.save(costRecordLog);
		this.visaProductsDao.updateReview(id,review,nowLevel);	
	}	
	

	/**
	 * 同一个国家和领区只能发一种类型的签证产品
	 */
	@Override
	public String findMoreProduct(Integer sysCountry, String collarZoning,
			Integer visaType, Long proId, String deptId,String groupCode) {
		Office company = UserUtils.getUser().getCompany();
		Long companyId = company.getId();
		String companyUuid = company.getUuid();
		if(null== deptId) {
			return "error";
		}
		List<VisaProducts> productList = visaProductsDao
				.findMoreProduct(sysCountry, collarZoning, visaType, companyId, proId, StringUtils.toLong(deptId));
		//C460 增加手动输入团号的重复校验
		boolean flag = false;//默认不重复
		if(StringUtils.isNotEmpty(groupCode)){
			 flag = activityGroupService.groupNoCheck(groupCode);//true:重复
		}
		
		if (productList.isEmpty()) {
			if(flag){
				return "wrong";//团号重复
			}else{
				return "true";
			}
		} else {
			//C281 青岛凯撒取消唯一性限制 changed by gaoang 2015-10-27
			if("7a8177e377a811e5bc1e000c29cf2586".equals(companyUuid)//青岛凯撒
					|| "58a27feeab3944378b266aff05b627d2".equals(companyUuid)  //日信观光
					|| "c3f9e32fdbff4ef19e40906978c3fc76".equals(companyUuid)  //友爱之旅
					|| "7a81b21a77a811e5bc1e000c29cf2586".equals(companyUuid)  //越柬行踪
					|| "5c05dfc65cd24c239cd1528e03965021".equals(companyUuid)  //起航假期
					|| "4a39518f8de74baebe6b51efcdd57aa3".equals(companyUuid)  //百乐游
					|| "049984365af44db592d1cd529f3008c3".equals(companyUuid)  //鼎鸿假期
					|| "dfafad3ebab448bea81ca13b2eb0673e".equals(companyUuid)  //天马运通
					)
			{
				if(flag){
					return "wrong";//团号重复
				}else{
					return "true";
				}
			}else{
				return "true";
			}
		}
	}
	
	@Override
	public Page<VisaProducts> findVisaProductsPage4PreOrder(Page<VisaProducts> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType, String orderBy, DepartmentCommon common) {

		DetachedCriteria dc = visaProductsDao.createDetachedCriteria();
		
		Criterion criterionCr = Restrictions.eq("proCompanyId", UserUtils.getUser().getCompany().getId());
		Criterion delFlagCr = Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL);
		Criterion productStatusCr = Restrictions.eq("productStatus", visaProducts.getProductStatus());
		
		Criterion deptCr = null;
		List<Criterion> whereList = systemService.getDepartmentSql("activity", null, null, common, null);
		
		// wxw modified  2015-09-02 签证预定列表报错
		if (common.getDept()!=null) {
			//如果是沈阳部门下销售经理或销售，则可以查看北京加拿大签证产品
			if ((common.getDept().getId() == 39 || (common.getDept().getParent() != null && common.getDept().getParentId() == 39))) {
				List<Long> visaProductIdList = getBingjingVisaProducts();
				if (CollectionUtils.isNotEmpty(visaProductIdList)) {
					deptCr = Restrictions.in("id", visaProductIdList);
				}
			}
		}

		if (deptCr != null) {
			if (CollectionUtils.isNotEmpty(whereList)) {
				dc.add(Restrictions.or(Restrictions.and(criterionCr, delFlagCr, productStatusCr, whereList.get(0)), deptCr));
			} else {
				dc.add(Restrictions.or(Restrictions.and(criterionCr, delFlagCr, productStatusCr), deptCr));
			}
		} else {
			if (CollectionUtils.isNotEmpty(whereList)) {
				dc.add(Restrictions.and(criterionCr, delFlagCr, productStatusCr, whereList.get(0)));
			} else {
				dc.add(Restrictions.and(criterionCr, delFlagCr, productStatusCr));
			}
		}
		
		if (StringUtils.isNotBlank(productName)) {
			dc.add(Restrictions.like("productName", "%" + productName + "%"));
		}
		if (StringUtils.isNotBlank(collarZoning)) {
			dc.add(Restrictions.eq("collarZoning", collarZoning));
		}

		if (StringUtils.isNotBlank(sysCountryId)) {
			dc.add(Restrictions.eq("sysCountryId", new Integer(sysCountryId)));
		}

		if (StringUtils.isNotBlank(visaType)) {
			dc.add(Restrictions.eq("visaType", new Integer(visaType)));
		}

		if (StringUtils.isEmpty(page.getOrderBy())) {
			page.setOrderBy(orderBy);
			dc.addOrder(Order.desc("id"));
		}
		return visaProductsDao.find(page, dc);

	}
	
	/**
	 * 获取北京分公司加拿大国家签证产品
	 * @return
	 */
	public List<Long> getBingjingVisaProducts() {
		
		List<Long> visaProductIds = Lists.newArrayList();
		List<User> userList = userDao.getAllUserByDepartment(UserUtils.getUser().getCompany().getId(), 16L, "%," + 16 + ",%");
		if (CollectionUtils.isNotEmpty(userList)) {
			List<VisaProducts> visaProductList = visaProductsDao.findByUsers(userList);
			if (CollectionUtils.isNotEmpty(visaProductList)) {
				for (VisaProducts product : visaProductList) {
					if (401 == product.getSysCountryId()) {
						visaProductIds.add(product.getId());
					}
				}
			}
		}
		return visaProductIds;
	}

	@Transactional(rollbackFor = Exception.class)
	public void copyEntity(Long id) {
		VisaProducts oldP = visaProductsDao.findOne(id);
		VisaProducts newP = new VisaProducts();
		if(null != oldP) {
			BeanUtils.copyProperties(oldP, newP);
			oldP.setRemark("************************************************");
		}
		visaProductsDao.save(oldP);
		newP.setId(null);
		visaProductsDao.save(newP);
	}

	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId, Integer orderType) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String lockedInRefund = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		//拉美途 过滤预报单锁定之后生成的订单,退款数据 C457 add by shijun.liu 2015.12.21
		// forecast_locked_in = 1 表示是预报单锁定之后生成的数据
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			lockedIn = " AND o.forecast_locked_in is null ";
			lockedInRefund = " AND cost.forecast_locked_in is null ";
		}
		 str.append("SELECT id,saler,agentName,")
		    .append(" IFNULL(totalMoney, 0) AS totalMoney,")
		    .append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
		    .append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
		    .append(" IFNULL(r.refundprice, 0) AS refundprice, ")
			.append(" orderPersonNum ")
		    .append(" FROM (")
		    .append(" SELECT o.id,")
		    .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
		    .append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
		    .append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM money_amount m1 ")
		    .append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
		    .append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM money_amount m2 ")
		    .append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney, ")
			.append(" o.travel_num AS orderPersonNum ")
		    .append(" FROM ")
		    .append(" visa_order o")
		    .append(" WHERE o.del_flag = '0' ")
		    .append(" AND o.payStatus NOT IN (7, 99, 111)")
		    .append(" AND o.visa_order_status <> 100 ")
		    .append(" AND o.visa_product_id = ").append(productId)
		    .append(lockedIn)
		    .append(" ) t1 LEFT JOIN (")
		    .append(" SELECT cost.orderId,sum(cost.price) AS refundprice ")
		    .append(" FROM cost_record cost ")
		    .append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =").append(orderType)
		    .append(" AND cost.budgetType = ").append(CostManageService.BUDGET_TYPE).append(lockedInRefund)
		    .append(" GROUP BY cost.orderId ")
		    .append(" ) r on r.orderId = t1.id ");
		List<Map<String, Object>> orderIncomes = visaProductsDao.findBySql(str.toString(), Map.class);
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> exceptIncome4LZJQ = new ArrayList<>();
		
		for (Map<String, Object> orderIncome : orderIncomes) {
			
			Map<String, Object> income = new HashMap<>();
			BigDecimal totalMoney = new BigDecimal(orderIncome.get("totalMoney").toString());
			Integer personNum = (Integer) orderIncome.get("orderPersonNum");
			BigDecimal price = totalMoney.divide(new BigDecimal(personNum));
			
			income.put("agentName", orderIncome.get("agentName"));
			income.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));
			income.put("personNum", personNum);
			income.put("totalPrice", MoneyNumberFormat.getThousandsByRegex(totalMoney.toString(), 2));
			income.put("saler", orderIncome.get("saler"));
			if(orderIncome.get("refundprice") != null && ! orderIncome.get("refundprice").toString().equals("0.00")){
				income.put("remark","退款：￥"+orderIncome.get("refundprice"));
			}
			exceptIncome4LZJQ.add(income);
		}
		map.put("exceptIncome4LZJQ", exceptIncome4LZJQ);
		orderIncomes.add(map);
		return orderIncomes;
	}
	
	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId, Integer orderType) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String lockedInRefund = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.SUPPLIER_UUID_LZJQ.equals(currentCompanyUuid)){
			return getOrderAndRefundInfoSettleForLZJQ(productId,orderType);
		}
		//拉美途 过滤结算单锁定之后生成的订单,退款数据 C486 add by shijun.liu 2015.12.21
		// settle_locked_in = 1 表示是结算单锁定之后生成的数据
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			lockedIn = " AND o.settle_locked_in is null ";
			lockedInRefund = " AND cost.settle_locked_in is null ";
		}
		 str.append("SELECT id,saler,agentName,")
		    .append(" IFNULL(totalMoney, 0) AS totalMoney,")
		    .append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
		    .append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
		    .append(" IFNULL(r.refundprice, 0) AS refundprice, ")
			.append(" orderPersonNum ")
		    .append(" FROM (")
		    .append(" SELECT o.id,")
		    .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
		    .append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
		    .append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM money_amount m1 ")
		    .append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
		    .append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM money_amount m2 ")
		    .append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney, ")
			.append(" o.travel_num AS orderPersonNum ")
		    .append(" FROM ")
		    .append(" visa_order o")
		    .append(" WHERE o.del_flag = '0' ")
		    .append(" AND o.payStatus NOT IN (7, 99, 111)")
		    .append(" AND o.visa_order_status <> 100 ")
		    .append(" AND o.visa_product_id = ").append(productId)
		    .append(lockedIn)
		    .append(" ) t1 LEFT JOIN (")
		    .append(" SELECT cost.orderId,sum(cost.price) AS refundprice ")
		    .append(" FROM cost_record cost ")
		    .append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =").append(orderType)
		    .append(" AND cost.budgetType = ").append(CostManageService.ACTUAL_TYPE).append(lockedInRefund);
		    //start 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy 2016年1月25日09:52:06
		    if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
		    	str.append(" AND cost.reviewStatus in ('审批通过', '审核通过') ");
		    } else {
		    	str.append(" AND cost.reviewStatus not in ('已取消','已驳回') ");
		    }
			//end 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy 2016年1月25日09:52:06
		    str.append(" GROUP BY cost.orderId ").append(" ) r on r.orderId = t1.id ");
		return visaProductsDao.findBySql(str.toString(), Map.class);
	}

	/**
	 * 针对骡子假期的查询使用下面的sql。
	 * @param productId
	 * @param orderType
	 * @return
	 * @author yudong.xu 2016.11.10
	 */
	public List<Map<String, Object>> getOrderAndRefundInfoSettleForLZJQ(Long productId, Integer orderType) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, saler, agentName, IFNULL(totalMoney, 0) AS totalMoney, IFNULL(accountedMoney, 0)")
		.append(" AS accountedMoney, IFNULL((totalMoney - accountedMoney), 0 ) AS notAccountedMoney, orderPersonNum, ")
		.append(" IFNULL(r.refundprice, 0) AS refundprice, ")
		.append("orderPersonNum AS adultNum, FORMAT(visaPay, 2) AS adultPrice, (visaPay * orderPersonNum) AS adultMoney")
		.append(" FROM ( SELECT o.id, ( SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId ) AS saler, ")
		.append("( SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id ) AS agentName,")
		.append("( SELECT ifnull( sum(m1.amount * m1.exchangerate), 0 ) FROM money_amount m1 WHERE ")
		.append("m1.serialNum = o.total_money ) AS totalMoney, ( SELECT ifnull( sum(m2.amount * m2.exchangerate), 0 ) ")
		.append(" FROM money_amount m2 WHERE m2.serialNum = o.accounted_money ) AS accountedMoney, ")
		.append("o.travel_num AS orderPersonNum, o.visa_product_id, p.visaPay FROM visa_order o")
		.append(" LEFT JOIN visa_products p ON o.visa_product_id = p.id WHERE o.del_flag = '0' ")
		.append("AND o.payStatus NOT IN (7, 99, 111) AND o.visa_order_status <> 100 AND o.visa_product_id = ? ) t1 ")
		.append(" LEFT JOIN (SELECT cost.orderId,sum(cost.price) AS refundprice FROM cost_record cost WHERE ")
		.append(" cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType = ? AND cost.budgetType = 1 ")
		.append(" AND cost.reviewStatus not in ('已取消','已驳回') GROUP BY cost.orderId) r on r.orderId = t1.id ");
		List<Map<String,Object>> list =  visaProductsDao.findBySql(sql.toString(), Map.class,productId,orderType);
		for (Map<String, Object> orderIncome : list) {
			BigDecimal totalMoney = new BigDecimal(orderIncome.get("totalMoney").toString());
			Integer personNum = (Integer) orderIncome.get("orderPersonNum");
			BigDecimal price = totalMoney.divide(new BigDecimal(personNum));
			orderIncome.put("adultPrice",MoneyNumberFormat.getThousandsByRegex( price.toString(),2));
			orderIncome.put("adultMoney", MoneyNumberFormat.getThousandsByRegex(totalMoney.toString(),2));
			if(orderIncome.get("refundprice") != null && ! orderIncome.get("refundprice").toString().equals("0.00")){
				orderIncome.put("adultRemark","退款：￥"+orderIncome.get("refundprice"));
			}
		}
		return list;
	}
	
	/**
	 * 处理游客列表数据
	 * @param travelerList
	 * @return
	 * @author xudong.he
	 * @date 2015-11-19
	 */
	public void dealTravelerList(List<Map<String, Object>> travelerList)
	{
		StringBuffer buffer = new StringBuffer();
		//拼接查询条件
		for(int i=0;i<travelerList.size();i++)
		{
			if(travelerList.size()==i+1)
			{
				buffer.append("'");
				buffer.append(travelerList.get(i).get("total_deposit"));
				buffer.append("'");
			}
			else
			{
				buffer.append("'");
				buffer.append(travelerList.get(i).get("total_deposit"));
				buffer.append("',");
			}
		}
		List<Map<String, Object>>  list= getMoneyCurrency(buffer);
		//处理数据重新组装新的travelerList
		for(int i=0;i<travelerList.size();i++)
		{
			for(int j=0;j<list.size();j++)
			{
				if(travelerList.get(i).get("total_deposit").equals(list.get(j).get("serialNum")))
				{
					travelerList.get(i).put("price", list.get(i).get("amount"));
					travelerList.get(i).put("currencyMark", list.get(i).get("currency_mark"));
					travelerList.get(i).put("currencyId", list.get(i).get("currency_id"));
				}
			}
		}
		
	}
	
	/**
	 * 批量获取押金金额信息
	 * @param buffer
	 * @author xudong.he
	 */
	public List<Map<String, Object>> getMoneyCurrency(StringBuffer buffer)
	{
		StringBuffer buff = new StringBuffer();
		buff.append("SELECT c.currency_mark,c.currency_id,mma.amount,mma.reviewId,mma.serialNum FROM  currency c INNER JOIN ( ");
		buff.append("SELECT ma.amount,ma.currencyId,ma.reviewId,ma.serialNum FROM money_amount ma WHERE ma.serialNum in( ");
		buff.append(buffer);
		buff.append("))mma ON c.currency_id = mma.currencyId");
		return visaProductsDao.findBySql(buff.toString(), Map.class);
	}

	/**
	 * 根据游客id获取签证类型
	 * @param TravellerId
	 * @author xudong.he
	 */
	public String getVisaType(String TravellerId)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT v.visa_stauts,v.traveler_id FROM visa v  WHERE v.traveler_id=");
		buffer.append(TravellerId);
		List<Map<String,Object>> list= 	visaProductsDao.findBySql(buffer.toString(), Map.class);
		if(list.size()>0)
			return list.get(0).get("visa_stauts").toString();
		else
		return "False";
	}


}
