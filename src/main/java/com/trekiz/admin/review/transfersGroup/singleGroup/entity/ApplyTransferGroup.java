package com.trekiz.admin.review.transfersGroup.singleGroup.entity;

import java.util.Map;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 转团申请参数集合bean
 * @author gao
 * @date 2015-11-19
 */
public class ApplyTransferGroup {

	private Long userId; // 执行操作的用户id
	private String companyUuid;// 用户所在公司uuid
	private UserReviewPermissionChecker checker;// 用户审批权限检查器 为空时表示该用户不具备越级权限
	private String businessKey;// 业务key，应具有一定规则，能够通过业务属性唯一定位一条审批记录，可以为空
	private Integer productType;// 原产品类型
	private Integer processType;// 流程类型 
	private Long deptId;// 部门id
	private String comment;//  申请审批时的评论，可以为空
	private Map<String, Object> variables;// 所有涉及到的业务数据，可以为空
	
	
	public UserReviewPermissionChecker getChecker() {
		return checker;
	}
	public void setChecker(UserReviewPermissionChecker checker) {
		this.checker = checker;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public Integer getProcessType() {
		return processType;
	}
	public void setProcessType(Integer processType) {
		this.processType = processType;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
	public Long getUserId() {
		return UserUtils.getUser().getId();
	}
	public String getCompanyUuid() {
		return UserUtils.getUser().getCompany().getUuid();
	}
	
	
}
