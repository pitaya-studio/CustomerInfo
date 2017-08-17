package com.trekiz.admin.modules.sys.entity;

import java.util.Set;

public class UserReviewPermissionResultForm {
	/**用户id*/
	private Long UserId;
	
	/**部门id*/
	private Set<String> deptId;
	
	/**产品类型id*/
	private Set<String> productTypeId;
	
	/**流程类型id*/
	private Set<String> reviewFlowId;

	public Long getUserId() {
		return UserId;
	}

	public void setUserId(Long userId) {
		UserId = userId;
	}

	public Set<String> getDeptId() {
		return deptId;
	}

	public void setDeptId(Set<String> deptId) {
		this.deptId = deptId;
	}

	public Set<String> getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Set<String> productTypeId) {
		this.productTypeId = productTypeId;
	}

	public Set<String> getReviewFlowId() {
		return reviewFlowId;
	}

	public void setReviewFlowId(Set<String> reviewFlowId) {
		this.reviewFlowId = reviewFlowId;
	}

	
}
