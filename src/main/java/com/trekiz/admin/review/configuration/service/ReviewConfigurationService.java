package com.trekiz.admin.review.configuration.service;

import java.io.UnsupportedEncodingException;
import java.util.*;


import com.quauq.review.core.engine.entity.ReviewProcess;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.money.repository.NewProcessMoneyAmountDao;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewConfigService;
import com.quauq.review.core.engine.ReviewProcessService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewConfig;
import com.quauq.review.core.management.ReviewManagementService;
import com.quauq.review.core.support.CommonResult;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.review.configuration.config.ReviewContext;
import org.springframework.util.Assert;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 审批配置相关服务，将Controller中多步操作集中到此服务中管理，保证事务一致性
 *
 * @author zhenxing.yan
 * @date 2015年11月17日
 */
@Service
public class ReviewConfigurationService {

	@Autowired
	private ReviewProcessService reviewProcessService;

	@Autowired
	private ReviewConfigService reviewConfigService;

	@Autowired
	private ReviewManagementService reviewManagementService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ReviewCostPaymentConfigurationService reviewCostPaymentConfigurationService;

	/**
	 * 借用
	 */
	@Autowired
	private NewProcessMoneyAmountDao newProcessMoneyAmountDao;

	/**
	 * 保存流程配置
	 * 
	 * @created_by zhenxing.yan 2015年11月17日
	 *
	 * @param userId
	 *            当前操作用户id
	 * @param companyId
	 *            公司id，使用uuid
	 * @param modelName
	 *            流程模板名称
	 * @param description
	 *            模板描述
	 * @param depts
	 *            部门id列表
	 * @param productTypes
	 *            产品类型列表
	 * @param processTypes
	 *            流程类型列表
	 * @param specialTypes
	 *            特殊需求类型
	 * @return JSONObject
	 */
	@Transactional(rollbackFor = {Exception.class})
	public JSONObject saveConfiguration(String userId, String companyId, String modelName, String description,
					List<String> depts, List<String> productTypes, List<String> processTypes,
					Map<Integer, Integer> specialTypes) {
		JSONObject result = new JSONObject();

		Assert.hasText(userId,"userId should not be empty!");
		Assert.hasText(companyId,"companyId should not be empty!");
		Assert.hasText(modelName,"modelName should not be empty!");
		Assert.hasText(description,"description should not be empty!");
		/**
		 * 1. 验证是否存在已经配置过的流程
		 */
		CommonResult validationResult = reviewProcessService.validateProcess(companyId, depts, productTypes,
						processTypes);
		if (!validationResult.getSuccess()) {
			result.put("code", -1);
			User user= UserUtils.getUser(userId);
			Assert.notNull(user,"user is null with id:"+userId);
			/**
			 * 获取部门信息
			 */
			Map<Long,Department> departmentMap=departmentService.getDepartmentMapByOfficeId(user.getCompany().getId());

			StringBuilder message = new StringBuilder();
			Map<String, Object> params = validationResult.getParams();

			List<ReviewProcess> existProcesses=(List<ReviewProcess>)params.get("reviewProcesses");

			message.append("您已经配置了<br>");
			for (ReviewProcess reviewProcess:existProcesses) {
				message.append(departmentMap.get(Long.parseLong(reviewProcess.getDeptId())).getName());
				message.append("-");
				message.append(ReviewContext.productTypeMap.get(Integer.parseInt(reviewProcess.getProductType())));
				message.append("-");
				message.append(ReviewContext.reviewFlowTypeMap.get(Integer.parseInt(reviewProcess.getReviewFlow())));
				message.append("<br>");
			}
//			if (params.containsKey("deptId")) {
//				message.append(departmentService.findById(Long.parseLong(params.get("deptId").toString())).getName());
//				message.append(" ");
//			}
//			if (params.containsKey("productType")) {
//				message.append(ReviewContext.productTypeMap.get(Integer.parseInt(params.get("productType").toString())));
//				message.append(" ");
//			}
//			if (params.containsKey("processType")) {
//				message.append(ReviewContext.reviewFlowTypeMap.get(Integer.parseInt(params.get("processType")
//								.toString())));
//				message.append(" ");
//			}
			message.append("请勿重复配置");
			result.put("message", message.toString());
			return result;
		}

		/**
		 * 2. 首先处理无需审批的流程
		 */
		// 初始化各类配置值
		boolean needNoReview = false;
		Integer jumpTask = ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_JUMP_TASK;
		Integer multiApply = ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_MULTI_APPLY;
		if (specialTypes != null && specialTypes.size() > 0) {
			if (specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_NEED_NO_REVIEW)) {
				needNoReview = specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_NEED_NO_REVIEW) == 1 ? true : false;
			}
			if (specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_JUMP_TASK)) {
				jumpTask = specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_JUMP_TASK);
			}
			if (specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY)) {
				multiApply = specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY);
			}
		}

		/**
		 * 3. 生成模板
		 */
		String modelId = null;
		if (needNoReview) {
			modelId = ReviewContext.PROCESS_MODEL_ID_NEED_NO_REVIEW;
		} else {
			try {
				modelId = reviewManagementService.generateProcess(modelName, description, companyId);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				result.put("code", -1);
				result.put("message", "生成流程模板失败!");
				return result;
			}
		}
		/**
		 * 4. 创建processKey
		 */
		String processKey = null;
		if (needNoReview) {
			processKey = ReviewConstant.REVIEW_PROCESS_KEY_NOTHING;// 无需审批特有processKey
		} else {
			processKey = "key"+UuidUtils.generUuid();
		}

		/**
		 * 5. 保存部门、产品类型、流程类型以及processKey的关系
		 */
		String serialNumber=UuidUtils.generUuid();
		reviewProcessService.saveReviewProcess(userId, companyId,serialNumber, processKey, modelId, depts, productTypes,
						processTypes);

		/**
		 * 6. 保存流程特殊需求配置信息
		 */
		ReviewConfig reviewConfig = new ReviewConfig();
		reviewConfig.setId(UuidUtils.generUuid());
		reviewConfig.setSerialNumber(serialNumber);
		reviewConfig.setCompanyId(companyId);
		reviewConfig.setCreateBy(userId);
		reviewConfig.setCreateDate(new Date());
		reviewConfig.setDelFlag(0);
		reviewConfig.setIsApplierAutoApprove(ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_AUTO_APPROVE);
		reviewConfig.setIsJumpTaskPermit(jumpTask);
		reviewConfig.setIsMultiApplyPermit(multiApply);
		reviewConfig.setProcessKey(processKey);
		reviewConfigService.save(reviewConfig);

		/**
		 * 7. 如果存在成本付款，保存或更新付款金额等于实际成本金额配置
		 */
		if (processTypes.contains(Context.REVIEW_FLOWTYPE_PAYMENT.toString())){//如果存在成本审批，就直接保存配置
			Integer paymentEqualsCost=specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST)?
					specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST):0;
			reviewCostPaymentConfigurationService.save(userId,companyId,new HashSet<String>(depts),new HashSet<String>(productTypes),Context.REVIEW_FLOWTYPE_PAYMENT.toString(),paymentEqualsCost);
		}

		result.put("code", 0);
		result.put("message", "保存成功!");
		result.put("modelId", modelId);
		result.put("processKey", processKey);
		result.put("serialNumber", serialNumber);
		return result;
	}

	/**
	 * 修改流程配置
	 * 
	 * @created_by zhenxing.yan 2015年11月20日
	 *
	 * @param userId
	 *            当前用户id
	 * @param companyId
	 *            公司id
	 * @param processKey
	 *            流程key
	 * @param serialNumber
	 *            流程序列号（批次号）
	 * @param modelId
	 *            当前流程图id
	 * @param depts
	 *            部门id列表
	 * @param productTypes
	 *            产品列表
	 * @param processTypes
	 *            流程类型列表
	 * @param specialTypes
	 *            特殊权限
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class})
	public JSONObject modifyProcess(String userId, String companyId, String processKey, String serialNumber,String modelId,List<String> depts,
					List<String> productTypes, List<String> processTypes, Map<Integer, Integer> specialTypes) {
		JSONObject result = new JSONObject();
		result.put("code",0);
		// 初始化各类配置值
		boolean needNoReview = false;
		Integer jumpTask = ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_JUMP_TASK;
		Integer multiApply = ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_MULTI_APPLY;
		if (specialTypes != null && specialTypes.size() > 0) {
			if (specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_NEED_NO_REVIEW)) {
				needNoReview = specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_NEED_NO_REVIEW) == 1 ? true : false;
			}
			if (specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_JUMP_TASK)) {
				jumpTask = specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_JUMP_TASK);
			}
			if (specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY)) {
				multiApply = specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY);
			}
		}
		
		/**
		 * 1. 验证修改的流程中是否有已经配置了其他流程的项
		 */
		String originalProcessKey=processKey;
		String currentProcessKey=processKey;
		String currentProcessModelId=modelId;
		if (needNoReview) {
			currentProcessKey=ReviewConstant.REVIEW_PROCESS_KEY_NOTHING;
			currentProcessModelId=ReviewContext.PROCESS_MODEL_ID_NEED_NO_REVIEW;
		}
		CommonResult validationResult = reviewProcessService.validateModifyProcess(processKey,ReviewConstant.REVIEW_PROCESS_KEY_NOTHING,depts, productTypes,
						processTypes,serialNumber);
		if (!validationResult.getSuccess()) {
			result.put("code", -1);
			StringBuilder message = new StringBuilder();
			Map<String, Object> params = validationResult.getParams();
			message.append("流程配置已经被其他流程配置过，请先修改取消另一个流程再进行此流程配置, [ ");
			if (params.containsKey("deptId")) {
				message.append(departmentService.findById(Long.parseLong(params.get("deptId").toString())).getName());
				message.append(" ");
			}
			if (params.containsKey("productType")) {
				message.append(ReviewContext.productTypeMap.get(Integer.parseInt(params.get("productType").toString())));
				message.append(" ");
			}
			if (params.containsKey("processType")) {
				message.append(ReviewContext.reviewFlowTypeMap.get(Integer.parseInt(params.get("processType")
								.toString())));
				message.append(" ");
			}
			message.append("]");
			result.put("message", message.toString());
			return result;
		}

		/**
		 * 2. 如果原来是无需审批，变为普通审批，则需要新创建审批信息
		 */
		if (originalProcessKey.equals(ReviewConstant.REVIEW_PROCESS_KEY_NOTHING)&&!needNoReview) {
				try {
					/**
					 * 设置两个默认的属性
					 */
					String modelName = "quauq";
					String description = "quauq review model";
					currentProcessModelId = reviewManagementService.generateProcess(modelName, description, companyId);
					/**
					 * 设置新的processKey
					 */
					currentProcessKey="key"+UuidUtils.generUuid();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					result.put("code", -1);
					result.put("message", "生成流程模板失败!");
					return result;
				}
		}

		try {
			/**
             * 3. 保存修改后的配置
             */
			reviewProcessService.modifyReviewProcess(userId, companyId, originalProcessKey, currentProcessKey, currentProcessModelId, ReviewConstant.REVIEW_PROCESS_KEY_NOTHING, depts, productTypes, processTypes, serialNumber);

			/**
             * 4. 保存修改后的权限配置
             */
			if (!originalProcessKey.equals(ReviewConstant.REVIEW_PROCESS_KEY_NOTHING)) {
                ReviewConfig reviewConfig = reviewConfigService.findBySerialNumber(serialNumber);
				if (reviewConfig==null) {
					reviewConfig=new ReviewConfig();
					reviewConfig.setId(UuidUtils.generUuid());
					reviewConfig.setSerialNumber(serialNumber);
					reviewConfig.setCompanyId(companyId);
					reviewConfig.setCreateBy(userId);
					reviewConfig.setCreateDate(new Date());
					reviewConfig.setDelFlag(0);
					reviewConfig.setIsApplierAutoApprove(ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_AUTO_APPROVE);
					reviewConfig.setIsJumpTaskPermit(jumpTask);
					reviewConfig.setIsMultiApplyPermit(multiApply);
					reviewConfig.setProcessKey(processKey);
				}else{
					reviewConfig.setIsApplierAutoApprove(ReviewContext.SPECIAL_NEED_DEFAULT_VALUE_AUTO_APPROVE);
					reviewConfig.setIsJumpTaskPermit(jumpTask);
					reviewConfig.setIsMultiApplyPermit(multiApply);
					reviewConfig.setUpdateBy(userId);
					reviewConfig.setUpdateDate(new Date());
				}

				/**
				 * 5. 如果存在成本付款，保存或更新付款金额等于实际成本金额配置
				 */
				if (processTypes.contains(Context.REVIEW_FLOWTYPE_PAYMENT.toString())){//如果存在成本付款审批，就直接保存配置
					Integer paymentEqualsCost=specialTypes.containsKey(ReviewContext.SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST)?
							specialTypes.get(ReviewContext.SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST):0;
					reviewCostPaymentConfigurationService.save(userId,companyId,new HashSet<String>(depts),new HashSet<String>(productTypes),Context.REVIEW_FLOWTYPE_PAYMENT.toString(),paymentEqualsCost);
				}

                reviewConfigService.save(reviewConfig);
            }
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
			result.put("message", "修改!");
			return result;
		}


		return result;
	}

	/**
	 * 搜索已经配置的流程
	 * @param companyId 公司id，使用uuid
	 * @param deptId 部门id
	 * @param productType 产品类型
	 * @param processType 流程类型
     * @return
     */
	public List<List<ReviewProcess>> searchReviewProcess(String companyId,String deptId,String productType,String processType){
		List<List<ReviewProcess>> result=new ArrayList<>();
		Assert.hasText(companyId,"companyId should not be empty!");
		Assert.isTrue(!(StringUtils.isBlank(deptId)&&StringUtils.isBlank(productType)&&StringUtils.isBlank(processType)),"deptId,productType,processType are all empty!");

		/**
		 * 组装查询sql
		 */
		StringBuilder sqlBuilder=new StringBuilder();

		sqlBuilder.append("SELECT * FROM review_process WHERE company_id='");
		sqlBuilder.append(companyId);
		sqlBuilder.append("' AND del_flag=0 AND serial_number IN");
		sqlBuilder.append("(SELECT serial_number FROM review_process WHERE del_flag =0 ");
		if (StringUtils.isNotBlank(deptId)){
			sqlBuilder.append(" AND dept_id =");
			sqlBuilder.append(deptId);
		}
		if (StringUtils.isNotBlank(productType)){
			sqlBuilder.append(" AND product_type =");
			sqlBuilder.append(productType);
		}
		if (StringUtils.isNotBlank(processType)){
			sqlBuilder.append(" AND review_flow =");
			sqlBuilder.append(processType);
		}
		sqlBuilder.append(")");
		sqlBuilder.append(" order by create_date desc");

		List<ReviewProcess> reviewProcesses=newProcessMoneyAmountDao.findBySql(sqlBuilder.toString(),ReviewProcess.class);

		Map<String,List<ReviewProcess>> processesMap=new LinkedHashMap<>();
		if (reviewProcesses!=null&&reviewProcesses.size()>0){
			for (ReviewProcess reviewProcess:reviewProcesses ) {
				if (StringUtils.isNotBlank(reviewProcess.getSerialNumber())){
					if (processesMap.containsKey(reviewProcess.getSerialNumber())){
						List<ReviewProcess> processes=processesMap.get(reviewProcess.getSerialNumber());
						processes.add(reviewProcess);
					}else{
						List<ReviewProcess> processes=new ArrayList<ReviewProcess>();
						processes.add(reviewProcess);
						processesMap.put(reviewProcess.getSerialNumber(),processes);
					}
				}
			}
			for (String key : processesMap.keySet()){
				result.add(processesMap.get(key));
			}
		}
		return result;
	}

	/**
	 * 删除审批流程
	 * @param serialNumber
	 * @param processKey
     * @return
     */
	@Transactional(rollbackFor = {Exception.class})
	public JSONObject deleteReviewProcess(String serialNumber,String processKey){
		Assert.hasText(serialNumber,"serialNumber should not be empty!");
		Assert.hasText(processKey,"processKey should not be empty!");
		JSONObject result=new JSONObject();
		result.put("code",0);
		//如果不是“无需审批”，则需要计算是不是有在审的流程
//		if(ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(processKey)){
//			boolean hasProcessingReviewInstance=reviewManagementService.hasProcessingInstances(processKey);
//			if (hasProcessingReviewInstance){
//				result.put("code",-1);
//				result.put("msg","此流程尚有未审批完成的数据，暂时无法删除");
//			}
//		}
		reviewManagementService.deleteReviewProcess(serialNumber);
		return result;
	}

	/**
	 * 验证是否可以被删除，判断是否有在审的流程
	 * @param serialNumber
	 * @param processKey
     * @return
     */
	public JSONObject deleteValidation(String serialNumber,String processKey){
		Assert.hasText(serialNumber,"serialNumber should not be empty!");
		Assert.hasText(processKey,"processKey should not be empty!");
		JSONObject result=new JSONObject();
		result.put("code",0);
		//如果不是“无需审批”，则需要计算是不是有在审的流程
		if(!ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(processKey)){
			boolean hasProcessingReviewInstance=reviewManagementService.hasProcessingInstances(processKey);
			if (hasProcessingReviewInstance){
				result.put("code",-1);
//				result.put("msg","此流程尚有未审批完成的数据，暂时无法删除");
			}
		}
		return result;
	}
}
