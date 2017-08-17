package com.trekiz.admin.modules.groupCover.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.groupCover.entity.CoverResult;
import com.trekiz.admin.modules.groupCover.entity.GroupCover;
import com.trekiz.admin.modules.groupCover.repository.GroupCoverDao;
import com.trekiz.admin.modules.groupCover.service.GroupCoverService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 散拼补位service
 * @author yakun.bai
 * @Date 2016-4-20
 */
@Service
@Transactional(readOnly = true)
public class GroupCoverServiceImpl extends BaseService implements GroupCoverService {
	@Autowired
	private GroupCoverDao groupCoverDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private UserDao userDao;

	/**
	 * 保存
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public void save(GroupCover GroupCover) {
		groupCoverDao.saveObj(GroupCover);
	}

	/**
	 * 修改
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public void update(GroupCover GroupCover) {
		groupCoverDao.updateObj(GroupCover);
	}

	/**
	 * 根据ID获取对象
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public GroupCover getById(java.lang.Long value) {
		return groupCoverDao.getById(value);
	}

	/**
	 * 逻辑删除数据
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public void removeById(java.lang.Integer value) {
		GroupCover obj = groupCoverDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}

	/**
	 * 分页查询
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public Page<GroupCover> find(Page<GroupCover> page, GroupCover GroupCover) {
		DetachedCriteria dc = groupCoverDao.createDetachedCriteria();

		if (GroupCover.getId() != null) {
			dc.add(Restrictions.eq("id", GroupCover.getId()));
		}
		// if (StringUtils.isNotEmpty(GroupCover.getUuid())){
		// dc.add(Restrictions.like("uuid", "%"+GroupCover.getUuid()+"%"));
		// }
		if (GroupCover.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", GroupCover.getCreateBy()));
		}
		if (GroupCover.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate", GroupCover.getCreateDate()));
		}
		if (GroupCover.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", GroupCover.getUpdateBy()));
		}
		if (GroupCover.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate", GroupCover.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(GroupCover.getDelFlag())) {
			dc.add(Restrictions.like("delFlag", "%" + GroupCover.getDelFlag()
					+ "%"));
		}

		// dc.addOrder(Order.desc("id"));
		return groupCoverDao.find(page, dc);
	}

	/**
	 * 分页查询
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public List<GroupCover> find(GroupCover GroupCover) {
		DetachedCriteria dc = groupCoverDao.createDetachedCriteria();

		if (GroupCover.getId() != null) {
			dc.add(Restrictions.eq("id", GroupCover.getId()));
		}
		// if (StringUtils.isNotEmpty(GroupCover.getUuid())){
		// dc.add(Restrictions.like("uuid", "%"+GroupCover.getUuid()+"%"));
		// }
		if (GroupCover.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", GroupCover.getCreateBy()));
		}
		if (GroupCover.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate", GroupCover.getCreateDate()));
		}
		if (GroupCover.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", GroupCover.getUpdateBy()));
		}
		if (GroupCover.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate", GroupCover.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(GroupCover.getDelFlag())) {
			dc.add(Restrictions.like("delFlag", "%" + GroupCover.getDelFlag()
					+ "%"));
		}

		// dc.addOrder(Order.desc("id"));
		return groupCoverDao.find(dc);
	}
	
	/**
	 * 根据团期ID查询补位申请记录
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public List<GroupCover> findGroupCoverList(Long groupId) {
		return groupCoverDao.findGroupCoverList(groupId);
	}

	/**
	 * 分页查询
	 * @author pengfei.shang
	 * @Date 2016-4-20
	 */
	public Page<Map<Object, Object>> findCoverList( Page<Map<Object, Object>> page, Map<String, String> mapRequest){
		String orderBy = mapRequest.get("orderBy");
		page.setOrderBy(orderBy);
		//sql语句where条件
		String where = this.getGroupCoverSql(mapRequest);
		//获取订单查询sql语句
		String coverOrderSql = getCoverOrderSql(where);
		mapRequest.put("orderSql", coverOrderSql);
		
		return groupCoverDao.findPageBySql(page, coverOrderSql, Map.class);
	}

	/**
	 * 获取订单查询的where条件
	* @author pengfei.shang
	 * @Date 2016-4-20
	 */
	private String getGroupCoverSql(Map<String, String> map) {
	    	
	        StringBuffer sqlWhere = new StringBuffer("");
	        
	        //补位号或团号
	        String coverCodeOrGroupCode = map.get("coverCodeOrGroupCode");
	        if (StringUtils.isNotBlank(coverCodeOrGroupCode)){
	        	sqlWhere.append(" and (gc.coverCode like '%" + coverCodeOrGroupCode.trim() + "%' or agp.groupCode like '%" + coverCodeOrGroupCode.trim() + "%') ");
	        }
	        
	        //团期补单状态
	        String coverStatus = map.get("coverStatus");
	        if (StringUtils.isNotBlank(coverStatus) && !"0".equals(coverStatus)){
	        	sqlWhere.append(" and gc.coverStatus = " + coverStatus + " ");
	        }
	        
	        //申请人
	        String createBy = map.get("createBy");
	        if (StringUtils.isNotBlank(createBy)){
	        	sqlWhere.append(" and gc.createBy = " + createBy + " ");
	        }
	        
	        //产品创建者即计调
	        String activityCreate = map.get("activityCreate");
	        if (StringUtils.isNotBlank(activityCreate)){
	        	sqlWhere.append(" and agp.createBy = " + activityCreate + " ");
	        }
	        
	        //申请提交日期
	        String createDateBegin = map.get("createDateBegin");
	        String createDateEnd = map.get("createDateEnd");
	        if (StringUtils.isNotBlank(createDateBegin)) {
	        	sqlWhere.append(" and gc.createDate >= '" + createDateBegin + "'");
	        }
	        if (StringUtils.isNotBlank(createDateEnd)){
	        	sqlWhere.append(" and gc.createDate  <= '" + createDateEnd + "'");
	        }
	        
	        //团期出团日期
	        String groupOpenDateBegin = map.get("groupOpenDateBegin");
	        String groupOpenDateEnd = map.get("groupOpenDateEnd");
	        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
	        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
	        }
	        if (StringUtils.isNotBlank(groupOpenDateEnd)){
	            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
	        }
	        
	        sqlWhere.append(getUsers());
	        
	        return sqlWhere.toString();
	}
	
	private String getUsers() {
		
		String userSql = "";
		
		User user = UserUtils.getUser();
		
		//判断当前用户所拥有的角色类型
        List<Role> roleList = user.getRoleListOrderByDept();
        
    	for (Role role : roleList) {
    		Department dept = role.getDepartment();
        	String type = role.getRoleType();
        	
        	if (Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
        		userSql = "";
        		break;
        	} else if (Context.ROLE_TYPE_OP.equals(type)) {
        		userSql = " and agp.createBy = " + user.getId();
        	} else if (Context.ROLE_TYPE_OP_EXECUTIVE.equals(type)) {
        		String createByIds = user.getId() + ",";
        		List<User> userList = userDao.getUserByDepartment(UserUtils.getUser().getCompany().getId(), dept.getId());
        		if (CollectionUtils.isNotEmpty(userList)) {
        			Iterator<User> it = userList.iterator();
        			int i = 0;
        			while (it.hasNext()) {
        				if (i == userList.size() - 1) {
        					createByIds += it.next().getId();
        				} else {
        					createByIds += it.next().getId() + ",";
        				}
        				i++;
        			}
        		}
        		userSql = " and agp.createBy in (" + createByIds + ")";
        	}
        }
		
		return userSql;
	}

	/**
	 * 设置查询补位订单的sql语句
	 * @author pengfei.shang
	 * @Date 2016-4-20
	 */
	private String getCoverOrderSql(String where) {
		//补位订单查询sql语句
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT gc.id as coverId, gc.orderId, agp.groupCode, gc.coverCode, gc.coverPosition, gc.coverStatus, gc.createBy, users.name userName, gc.createDate, ") 
			.append(" agp.groupOpenDate, agp.groupCloseDate, agp.planPosition, agp.freePosition, agp.createBy as createUserName ") 
			.append(" FROM sys_user users, groupcover gc LEFT JOIN activitygroup agp ON gc.activityGroupId = agp.id " +
					"WHERE users.id = gc.createBy AND users.companyId = " + UserUtils.getUser().getCompany().getId())
			.append(where);
		return sql.toString();
	}


	@Override
	public Integer getAllGroupcoverNumOfgroupid(Long activityGroupId) {
		List<GroupCover> groupcoverList = groupCoverDao.findGroupCoverListUndo(activityGroupId);
		Integer totalNum = 0;
		for (GroupCover groupcover : groupcoverList) {
			Integer coverNum = groupcover.getCoverPosition();
			totalNum += coverNum;
		}
		return totalNum;
	}

	/**
	 * 计调确认补位
	 * @author yakun.bai
	 * @Date 2016-4-24
	 */
	@Override
	public CoverResult confirm(String coverId) {
		Assert.hasText(coverId, "coverID should not be empty!");
		CoverResult result = new CoverResult();
		GroupCover cover = groupCoverDao.findOne(Long.parseLong(coverId));
		if (cover == null) {
			result.setCode(404);
			result.setMessage("您要确认的补位不存在");
			result.setCoverId(coverId);
			result.setSuccess(false);
			return result;
		}else{
			if(cover.getCoverStatus() == Context.COVER_STATUS_DBW){
				
				//如果余位数大于等于补位数，则直接扣减余位数，如果小于补位数，则余位数为0
				GroupCover groupCover = getById(Long.parseLong(coverId));
				ActivityGroup group = groupCover.getActivityGroup();
				
				int coverPosition = groupCover.getCoverPosition();
				int freePosition = group.getFreePosition();
				if (coverPosition <= freePosition) {
					group.setFreePosition(freePosition - coverPosition);
				} else {
					group.setFreePosition(0);
				}
				activityGroupDao.save(group);
				
				//更新补位记录状态为4 已取消
				cover.setCoverStatus(Context.COVER_STATUS_YBW);
				groupCoverDao.updateObj(cover);
				result.setCode(200);
				result.setMessage("确认成功");
				result.setCoverId(coverId);
				result.setSuccess(true);
				return result;
			}else{
				//状态不对，不能取消
				result.setCode(302);
				result.setMessage("您要确认的补位状态不对，不能确认");
				result.setCoverId(coverId);
				result.setSuccess(false);
				return result;
			}
		}
	}
	
	@Override
	public CoverResult cancel(String userId, String companyId, String string,
			String coverId, String string2, Object object) {
			Assert.hasText(userId, "userId should not be empty!");
			Assert.hasText(companyId, "companyId should not be empty!");
			Assert.hasText(coverId, "coverID should not be empty!");
			CoverResult result = new CoverResult();
			GroupCover cover = groupCoverDao.findOne(Long.parseLong(coverId));
			if (cover == null) {
				result.setCode(404);
				result.setMessage("您要取消的补位不存在");
//				result.setMessage("review record not found! reviewId : " + reviewId);
				result.setCoverId(coverId);
				result.setSuccess(false);
				return result;
			}else{
				if(cover.getCreateBy().getId() != Long.parseLong(userId)){
					result.setCode(302);
					result.setMessage("只有补位申请者才可以取消补位。");
					result.setCoverId(coverId);
					result.setSuccess(false);
					return result;
				}
				else if (cover.getCoverStatus() == Context.COVER_STATUS_YBW) {
					//如果已补位 该团的余位中增加该补位记录的补位人数
					ActivityGroup activityGroup = activityGroupDao.findOne(cover.getActivityGroup().getId());
					activityGroup.setFreePosition(activityGroup.getFreePosition()+cover.getCoverPosition());
					activityGroupDao.updateObj(activityGroup);
					cover.setCoverStatus(4);
					groupCoverDao.updateObj(cover);
					result.setCode(200);
					result.setMessage("取消成功");
					result.setCoverId(coverId);
					result.setSuccess(true);
					return result;
				}else if(cover.getCoverStatus() == 1){
					//更新补位记录状态为4 已取消
					cover.setCoverStatus(4);
					groupCoverDao.updateObj(cover);
					result.setCode(200);
					result.setMessage("取消成功");
					result.setCoverId(coverId);
					result.setSuccess(true);
					return result;
				}else{
					//状态不对，不能取消
					result.setCode(302);
					result.setMessage("您要取消的补位状态不对，不能取消");
					result.setCoverId(coverId);
					result.setSuccess(false);
					return result;
				}
			}
	}

	@Override
	public CoverResult reject(String userId, String companyId, String string,
			String coverId, String string2, Object object) {
		Assert.hasText(userId, "userId should not be empty!");
		Assert.hasText(companyId, "companyId should not be empty!");
		Assert.hasText(coverId, "coverID should not be empty!");
		// 鎸傝捣activiti鐨勮繖涓祦绋嬪疄渚�
		CoverResult result = new CoverResult();
		// 鑾峰彇Review瀵硅薄
		GroupCover cover = groupCoverDao.findOne(Long.parseLong(coverId));
		if (cover == null) {
			result.setCode(404);
			result.setMessage("您要驳回的补位不存在");
//			result.setMessage("review record not found! reviewId : " + reviewId);
			result.setCoverId(coverId);
			result.setSuccess(false);
			return result;
		}else if(cover.getCoverStatus() == 1){
			//更新补位记录状态为3 已驳回
			cover.setCoverStatus(3);
			groupCoverDao.updateObj(cover);
			result.setCode(200);
			result.setMessage("成功驳回");
			result.setCoverId(coverId);
			result.setSuccess(true);
			return result;
		}else{
			//状态不对，不能取消
			result.setCode(302);
			result.setMessage("您要驳回的补位状态不对，不能驳回");
			result.setCoverId(coverId);
			result.setSuccess(false);
			return result;
		}
	}
}
