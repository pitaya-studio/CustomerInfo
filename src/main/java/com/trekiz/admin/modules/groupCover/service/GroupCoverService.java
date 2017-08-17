package com.trekiz.admin.modules.groupCover.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.groupCover.entity.CoverResult;
import com.trekiz.admin.modules.groupCover.entity.GroupCover;

/**
 * 散拼补位service
 * @author yakun.bai
 * @Date 2016-4-20
 */
public interface GroupCoverService {

	/**
	 * 保存
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public void save(GroupCover GroupCover);

	/**
	 * 修改
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public void update(GroupCover GroupCover);

	/**
	 * 根据ID获取对象
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public GroupCover getById(java.lang.Long value);

	/**
	 * 逻辑删除数据
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public void removeById(java.lang.Integer value);

	/**
	 * 分页查询
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public Page<GroupCover> find(Page<GroupCover> page, GroupCover GroupCover);

	/**
	 * 分页查询
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public List<GroupCover> find(GroupCover GroupCover);
	
	/**
	 * 根据团期ID查询补位申请记录
	 * @author yakun.bai
	 * @Date 2016-4-20
	 */
	public List<GroupCover> findGroupCoverList(Long groupId);

	public Integer getAllGroupcoverNumOfgroupid(Long activityGroupId);

	/**
	 * 计调确认补位
	 * @author yakun.bai
	 * @Date 2016-4-24
	 */
	public CoverResult confirm(String coverId);
	
	public CoverResult cancel(String userId, String companyId, String string,
			String coverId, String string2, Object object);

	public CoverResult reject(String userId, String companyId, String string,
			String coverId, String string2, Object object);

	/**
	 * 分页查询
	 * @param page
	 * @author pengfei.shang
	 * @Date 2016-4-20
	 */
	public Page<Map<Object, Object>> findCoverList( Page<Map<Object, Object>> page, Map<String, String> mapRequest);

}
