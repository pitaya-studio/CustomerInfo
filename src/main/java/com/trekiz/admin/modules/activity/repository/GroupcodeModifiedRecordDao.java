package com.trekiz.admin.modules.activity.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;
import com.trekiz.admin.modules.island.util.StringUtil;


public interface GroupcodeModifiedRecordDao extends GroupcodeModifiedRecordDaoCustom, CrudRepository<GroupcodeModifiedRecord, Long>{
	
	/**
	 * @Description:
	 *  该方法用处查询机票产品的团号修改情况，用于各个产品在详情和修改页展示产品团号的变化情况
	 * @author xinwei.wang 
	 * @date 2015年11月26日上午9:58:17
	 * @param productId
	 * @param productType
	 * @return    
	 * @throws
	 */
	@Query("from GroupcodeModifiedRecord where productId = ?1 and productType = ?2 and delFlag = 0 order by createDate asc")
	public List<GroupcodeModifiedRecord> findGroupcodeModifiedRecordByProductIdAndType(Integer productId,Integer productType);
	
	/**
	 * @Description: 该方法用处查询团期类产品的团号修改情况，用于各个产品在详情和修改页展示产品团号的变化情况
	 * @author xinwei.wang
	 * @date 2015年12月1日下午4:49:21
	 * @param activityGroupId
	 * @param productType
	 * @return    
	 * @throws
	 */
	@Query("from GroupcodeModifiedRecord where activityGroupId = ?1 and productType = ?2 and delFlag = 0 order by createDate asc")
	public List<GroupcodeModifiedRecord> findGroupcodeModifiedRecordByActivityGroupIdAndType(Integer activityGroupId,Integer productType);
	
	
	/**
	 * @Description: 该方法用处查询团期类产品的团号修改情况，用于各个产品在详情和修改页展示产品团号的变化情况
	 * @author xinwei.wang
	 * @date 2015年12月1日下午4:49:21
	 * @param activityGroupId
	 * @param productType
	 * @return    
	 * @throws
	 */
	@Query("from GroupcodeModifiedRecord where activityGroupId = ?1 and delFlag = 0 order by createDate asc")
	public List<GroupcodeModifiedRecord> findGroupcodeModifiedRecordByActivityGroupId(Integer activityGroupId);

}


interface GroupcodeModifiedRecordDaoCustom extends BaseDao<GroupcodeModifiedRecord>{

	public Page<Map<String, Object>> queryGroupCodeLibrary4Airticket(HttpServletRequest request, HttpServletResponse response,Long office_id,Long parentDeptId);
	
	/**
	 * 
	 * @Description: 签证团号库
	 * @author xinwei.wang
	 * @date 2016年3月24日下午9:05:41
	 * @param request
	 * @param response
	 * @param office_id
	 * @param parentDeptId
	 * @return    
	 * @throws
	 */
	public Page<Map<String, Object>> queryGroupCodeLibrary4Visa(HttpServletRequest request, HttpServletResponse response,Long office_id,Long parentDeptId);
	
	public Page<Map<String, Object>> queryGroupCodeLibrary4Tuanqi(HttpServletRequest request, HttpServletResponse response,Long office_id,Long parentDeptId,String productType);
	
}


@Repository
class GroupcodeModifiedRecordDaoImpl extends BaseDaoImpl<GroupcodeModifiedRecord> implements GroupcodeModifiedRecordDaoCustom{
	
	private static final int LENGTH = 1;
	private static final int PAGESIZE = 5;

	@Override
	public Page<Map<String, Object>> queryGroupCodeLibrary4Airticket(HttpServletRequest request,
			HttpServletResponse response,Long office_id,Long parentDeptId) {
		
		String groupNo = request.getParameter("groupNo");
		String groupCreateDate = request.getParameter("groupCreateDate");
		
		//UNION
		//SELECT cc.productId,cc.group_code,cc.createDate,cc.id FROM (SELECT gmr.product_id as productId,gmr.group_code_old as group_code,gmr.create_date as createDate,gmr.id, min(gmr.create_date) from groupcode_modified_record gmr where gmr.activity_group_id is null GROUP BY gmr.product_id)cc;
		
		StringBuilder querySqlSb = new StringBuilder();
		querySqlSb.append("SELECT * from (")
		.append("SELECT aa.id  as productId,aa.group_code,aa.createDate,bb.id ")
		.append("FROM (SELECT aa.id,aa.group_code,aa.createDate FROM activity_airticket aa ")
		.append("WHERE aa.createBy IN (")
		.append("SELECT sur.userId  FROM  sys_user_role sur ")
		.append("WHERE sur.roleId IN ( ")
		.append("SELECT sr.id FROM sys_role sr WHERE ")
		.append("sr.deptId IN (SELECT id FROM department d WHERE d.office_id = "+office_id+" AND (d.id = "+parentDeptId+" OR d.parent_ids LIKE '%,"+parentDeptId+",%'))))")
		.append(" AND aa.delflag = 0 AND (aa.productStatus = 2 or aa.productStatus = 3) ) aa ")
		.append(" LEFT JOIN ( ")
		.append(" SELECT gmr.id,gmr.product_id FROM groupcode_modified_record gmr ")
		.append(" WHERE gmr.product_type = 7 ")
		.append(" ) bb ON bb.product_id = aa.id ")	
		.append(" UNION ")
		.append(" SELECT cc.productId,cc.group_code,cc.createDate,cc.id ")
		.append(" FROM(SELECT gmr.product_id AS productId, gmr.group_code_old AS group_code, gmr.create_date AS createDate, gmr.id, min(gmr.create_date) ")
		.append(" FROM groupcode_modified_record gmr LEFT JOIN activity_airticket aac on aac.id = gmr.product_id ")
		.append(" WHERE gmr.activity_group_id IS NULL ").append(" and gmr.product_type = 7")
		.append(" AND gmr.create_by IN (")
		.append("SELECT sur.userId  FROM  sys_user_role sur ")
		.append("WHERE sur.roleId IN ( ")
		.append("SELECT sr.id FROM sys_role sr WHERE ")
		.append("sr.deptId IN (SELECT id FROM department d WHERE d.office_id = "+office_id+" AND (d.id = "+parentDeptId+" OR d.parent_ids LIKE '%,"+parentDeptId+",%'))))")
		.append(" AND aac.delflag = 0 GROUP BY gmr.product_id ) cc) dd ");
		querySqlSb.append(" WHERE 1=1 ");
		
		//处理  按 \ 查询，查不到团号的问题
		if (null!=groupNo) {
			groupNo = filterCtrlChars(groupNo);
			groupNo = groupNo.replace("\\", "\\\\\\\\");
		}
		
		//处理查询条件
		if (StringUtil.isNotEmpty(groupNo)) {
			querySqlSb.append(" and dd.group_code LIKE '%"+groupNo+"%'");
		}
		
		if (StringUtil.isNotEmpty(groupCreateDate)) {
			querySqlSb.append(" and  dd.createDate LIKE '"+groupCreateDate+"%'");
		}
		
		//处理分组 排序
		querySqlSb.append(" GROUP BY  dd.group_code ")
		.append(" ORDER BY  dd.createDate DESC ");
		
		//System.out.println("sql ==="+querySqlSb.toString());
	
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		page.setLength(GroupcodeModifiedRecordDaoImpl.LENGTH);
		int pageSize = 0;
		if (null!=request.getParameter("pageSize")) {
			try {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} catch (Exception e) {
				//如果  
				pageSize = GroupcodeModifiedRecordDaoImpl.PAGESIZE;
			}
			
			if (pageSize==0) {
				pageSize = GroupcodeModifiedRecordDaoImpl.PAGESIZE;
			}
			page.setPageSize(pageSize);
		}else{
			page.setPageSize(GroupcodeModifiedRecordDaoImpl.PAGESIZE);
		}
		//page.setOrderBy(request.);
		return findBySql(page,querySqlSb.toString(), Map.class);
	}
	
	
    /**
     * 对应需求号   c460
     */
	@Override
	public Page<Map<String, Object>> queryGroupCodeLibrary4Visa(HttpServletRequest request,
			HttpServletResponse response,Long office_id,Long parentDeptId) {
		
		String groupNo = request.getParameter("groupNo");
		String groupCreateDate = request.getParameter("groupCreateDate");
		
		//UNION
		//SELECT cc.productId,cc.group_code,cc.createDate,cc.id FROM (SELECT gmr.product_id as productId,gmr.group_code_old as group_code,gmr.create_date as createDate,gmr.id, min(gmr.create_date) from groupcode_modified_record gmr where gmr.activity_group_id is null GROUP BY gmr.product_id)cc;
		
		StringBuilder querySqlSb = new StringBuilder();
		querySqlSb.append("SELECT * from ( ")
		.append("SELECT aa.id AS productId, aa.group_code, aa.createDate, bb.id ")
		.append("FROM ")
		.append("( ")
		.append("SELECT vp.id,vp.groupCode as group_code,vp.createDate ")
		.append("FROM  visa_products vp ")
		.append("WHERE vp.createBy IN ( ")
		.append("SELECT sur.userId FROM sys_user_role sur ") // -- 登录人所有 所在部门父部门的及所有子部门 所有角色 用户的ID
		.append("WHERE sur.roleId IN ( ")
		.append("SELECT sr.id FROM sys_role sr WHERE  ")
		.append("sr.deptId IN ( SELECT id FROM department d WHERE d.office_id = "+office_id+" AND ( d.id = "+parentDeptId+" OR d.parent_ids LIKE '%,"+parentDeptId+",%' )) ")
		.append(") ")
		.append(") ")
		.append("AND vp.delFlag = 0 AND vp.productStatus = 2	 ")				
		.append(") aa ")
		.append("LEFT JOIN ( ")
		.append("SELECT gmr.id, gmr.product_id ")
		.append("FROM groupcode_modified_record gmr ")
		.append("WHERE gmr.product_type = 6 ")
		.append(") bb ON bb.product_id = aa.id ")
		.append("UNION ")
		.append("SELECT cc.productId, cc.group_code, cc.createDate, cc.id ")
		.append("FROM ")
		.append("( ")
		.append("SELECT ")
		.append("gmr.product_id AS productId, ")
		.append("gmr.group_code_old AS group_code, ")
		.append("gmr.create_date AS createDate, ")
		.append("gmr.id, ")
		.append("min(gmr.create_date) ")
		.append("FROM ")
		.append("groupcode_modified_record gmr ")
		.append("LEFT JOIN visa_products vp  ON vp.id = gmr.product_id ")
		.append("WHERE ")
		.append("gmr.activity_group_id IS NULL  ") //-- 签证和机票没有团期Id
		.append("AND gmr.create_by IN ( ")
		.append("SELECT sur.userId FROM sys_user_role sur  ") // -- 登录人所有 所在部门父部门的及所有子部门 所有角色 用户的ID
		.append("WHERE sur.roleId IN ( ")
		.append("SELECT sr.id FROM sys_role sr ")
		.append("WHERE sr.deptId IN ( SELECT id FROM department d WHERE d.office_id = "+office_id+" AND ( d.id = "+parentDeptId+" OR d.parent_ids LIKE '%,"+parentDeptId+",%' )) ")
		.append(") ")
		.append(") ")
		
		
		.append("AND vp.createBy IN ( ")
		.append("SELECT sur.userId FROM sys_user_role sur  ") // -- 登录人所有 所在部门父部门的及所有子部门 所有角色 用户的ID
		.append("WHERE sur.roleId IN ( ")
		.append("SELECT sr.id FROM sys_role sr ")
		.append("WHERE sr.deptId IN ( SELECT id FROM department d WHERE d.office_id = "+office_id+" AND ( d.id = "+parentDeptId+" OR d.parent_ids LIKE '%,"+parentDeptId+",%' )) ")
		.append(") ")
		.append(") ")
		
		.append("AND vp.delflag = 0 and vp.productStatus = 2 ")
		.append("GROUP BY ")
		.append("gmr.product_id ")
		.append(") cc ")
		.append(") dd ")
		.append("WHERE 1 = 1 ");
		
		//处理  按 \ 查询，查不到团号的问题
		if (null!=groupNo) {
			groupNo = filterCtrlChars(groupNo);
			groupNo = groupNo.replace("\\", "\\\\\\\\");
		}
		
		//处理查询条件
		if (StringUtil.isNotEmpty(groupNo)) {
			querySqlSb.append(" and dd.group_code LIKE '%"+groupNo+"%'");
		}
		
		if (StringUtil.isNotEmpty(groupCreateDate)) {
			querySqlSb.append(" and  dd.createDate LIKE '"+groupCreateDate+"%'");
		}
		
		//处理分组 排序
		querySqlSb.append(" GROUP BY  dd.group_code ")
		.append(" ORDER BY  dd.createDate DESC ");
		
		//System.out.println("sql ==="+querySqlSb.toString());
	
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		page.setLength(GroupcodeModifiedRecordDaoImpl.LENGTH);
		int pageSize = 0;
		if (null!=request.getParameter("pageSize")) {
			try {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} catch (Exception e) {
				//如果  
				pageSize = GroupcodeModifiedRecordDaoImpl.PAGESIZE;
			}
			
			if (pageSize==0) {
				pageSize = GroupcodeModifiedRecordDaoImpl.PAGESIZE;
			}
			page.setPageSize(pageSize);
		}else{
			page.setPageSize(GroupcodeModifiedRecordDaoImpl.PAGESIZE);
		}
		//page.setOrderBy(request.);
		return findBySql(page,querySqlSb.toString(), Map.class);
	}
	
	
	
	
	 /**
	  * @Description: 处理SQL查询中  的 ‘\’ 特殊字符，使之变为‘\\’
	  *               但如果‘\’与数字连在一起会有特殊的意义，无法用
	  *               replace 方法进行直接替换
	  * @author xinwei.wang
	  * @date 2015年12月22日下午6:45:20
	  * @param source
	  * @return    
	  * @throws
	  */
	 public static String filterCtrlChars(String source){
         StringBuffer sf = new StringBuffer();
         for (char c : source.toCharArray()){
             if (Character.isISOControl(c)){
                 sf.append("\\").append(Integer.toOctalString(c));       
             }else{
                 sf.append(c);
             }
         }
         return sf.toString();
     }

	@Override
	public Page<Map<String, Object>> queryGroupCodeLibrary4Tuanqi(HttpServletRequest request,
			HttpServletResponse response, Long office_id, Long parentDeptId,String productType) {
		// TODO Auto-generated method stub
		String groupNo = request.getParameter("groupNo");
		String groupCreateDate = request.getParameter("groupCreateDate");
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT tmp.* FROM  ((SELECT a.id productid,a.groupCode group_code, a.createDate,gmr.id,gmr.activity_group_id ");
		buffer.append("FROM travelactivity t RIGHT JOIN activitygroup a  ON t.id = a.srcActivityId ");
		buffer.append("LEFT JOIN (SELECT  * FROM groupcode_modified_record g  WHERE g.product_type =");
		buffer.append(productType);
		buffer.append(" GROUP BY g.activity_group_id) gmr ON a.id = gmr.activity_group_id ");
		buffer.append("WHERE t.createBy IN (SELECT sur.userId FROM sys_user_role sur ");
		buffer.append("WHERE sur.roleId IN (SELECT sr.id  FROM sys_role sr WHERE sr.deptId IN (SELECT ");
		buffer.append("id FROM department d WHERE d.office_id =");
		buffer.append(office_id);
		buffer.append(" AND (d.id =");
		buffer.append(parentDeptId);
		buffer.append(" OR d.parent_ids LIKE '%,");
		buffer.append(parentDeptId);
		buffer.append(",%')))) AND t.delFlag = 0 AND a.delFlag=0 AND (t.activityStatus = 2 or  t.activityStatus = 3)  and t.activity_kind="+productType);
		buffer.append(" ORDER BY a.createDate DESC ) ");
		buffer.append("UNION ALL ");
		buffer.append("(SELECT gmr.product_id productid,gmr.group_code_old group_code,gmr.create_date createDate,gmr.id,gmr.activity_group_id FROM ");
		buffer.append("groupcode_modified_record gmr ");
		buffer.append("LEFT JOIN  activitygroup a ON gmr.activity_group_id = a.id ");
		buffer.append("WHERE gmr.activity_group_id IS NOT NULL ");
		buffer.append("AND gmr.create_by IN (SELECT sur.userId FROM sys_user_role sur ");
		buffer.append("WHERE sur.roleId IN (SELECT sr.id  FROM sys_role sr WHERE sr.deptId IN (SELECT ");
		buffer.append("id FROM department d WHERE d.office_id =");
		buffer.append(office_id);
		buffer.append(" AND (d.id =");
		buffer.append(parentDeptId);
		buffer.append(" OR d.parent_ids LIKE '%,");
		buffer.append(parentDeptId);
		buffer.append(",%')))) ");
		buffer.append("AND gmr.product_type = ");
		buffer.append(productType);
		buffer.append( " AND a.delFlag=0 GROUP BY gmr.activity_group_id  HAVING gmr.id =min(gmr.id) )) tmp ");
		buffer.append("WHERE 1=1 ");
		
		//处理  按 \ 查询，查不到团号的问题
		if (null!=groupNo) {
			if (null!=groupNo) {
				groupNo = filterCtrlChars(groupNo);
				groupNo = groupNo.replace("\\", "\\\\\\\\");
			}
		}
		
		//处理查询条件
		if (StringUtil.isNotEmpty(groupNo)) {
			buffer.append(" and tmp.group_code LIKE '%"+groupNo+"%'");
		}
		
		if (StringUtil.isNotEmpty(groupCreateDate)) {
			buffer.append(" and  tmp.createDate LIKE '"+groupCreateDate+"%'");
		}
		//排序
		buffer.append(" ORDER BY  tmp.createDate DESC ");
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		page.setLength(GroupcodeModifiedRecordDaoImpl.LENGTH);
		
		int pageSize = 0;
		if (null!=request.getParameter("pageSize")) {
			try {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} catch (Exception e) {
				//如果  
				pageSize = GroupcodeModifiedRecordDaoImpl.PAGESIZE;
			}
			
			if (pageSize==0) {
				pageSize = GroupcodeModifiedRecordDaoImpl.PAGESIZE;
			}
			page.setPageSize(pageSize);
		}else{
			page.setPageSize(GroupcodeModifiedRecordDaoImpl.PAGESIZE);
		}
		
		//System.out.println("sql ==="+buffer.toString());
		
		Page<Map<String, Object>> pageResult =findBySql(page,buffer.toString(), Map.class);
		return pageResult;
	}

	
}


