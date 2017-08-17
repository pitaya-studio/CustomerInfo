package com.trekiz.admin.review.configuration.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.review.configuration.model.DepartmentNode;

/**
 * 
 * Copyright   2015  QUAUQ Technology Co. Ltd.
 *
 * 部门相关的工具类
 *
 * @author zhenxing.yan
 * @date 2015年11月15日
 */
public class DepartmentUtil {

	/**
	 * 构建一棵部门树，必须有一个根
	 * 父级部门会有一个同名的叶子节点，表示实际的该父级部门
	 * @created_by zhenxing.yan 2015年11月15日
	 *
	 * @param defaultRootId 默认的最顶级根id
	 * @param rootId 要组装的部门树的顶级根
	 * @param departments 需要组装的
	 * @return
	 */
	public static DepartmentNode buildDepartmentTree(Long defaultRootId,Long rootId, List<Department> departments){
		Assert.notNull(defaultRootId, "defaultRootId should not be null!");
		Assert.notNull(rootId, "rootId should not be null!");
		Assert.notEmpty(departments, "departments should not be empty!");
		Map<Long, DepartmentNode> deptNodeMap=new HashMap<Long, DepartmentNode>();
		
		/**
		 * 构造节点
		 */
		for (Department department : departments) {
			DepartmentNode departmentNode=new DepartmentNode();
			BeanUtil.copySimpleProperties(departmentNode, department);
			deptNodeMap.put(department.getId(), departmentNode);
		}
		
		if(!deptNodeMap.containsKey(rootId)){
			return null;
		}
		
		/**
		 * 构造树形结构
		 */
		for (Department department : departments) {
			Long parentId=department.getParentId();
			if (parentId!=null&&!parentId.equals(defaultRootId)) {
				DepartmentNode parentNode=deptNodeMap.get(parentId);
				DepartmentNode childNode=deptNodeMap.get(department.getId());
				if (parentNode!=null&&childNode!=null) {
					parentNode.addChild(childNode);
				}
			}
		}
		
		/**
		 * 为父级部门增加叶子节点
		 */
//		for (Long deptId : deptNodeMap.keySet()) {
//			DepartmentNode parentNode=deptNodeMap.get(deptId);
//			if (parentNode.hasChildren()) {
//				DepartmentNode leafNode=new DepartmentNode();
//				BeanUtil.copySimpleProperties(leafNode, parentNode);
//				leafNode.clearChildren();
//				parentNode.addChild(leafNode);
//			}
//		}
		
		return deptNodeMap.get(rootId);
	}
}
