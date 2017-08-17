package com.trekiz.admin.modules.mobile.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.modules.mobile.entity.CorrelationUser;
import com.trekiz.admin.modules.mobile.entity.MobileUser;

/**
 * mobileUserService
 * @describe:TODO
 * @author:zhanyu.gu
 * @time:2017-1-22 下午3:54:09
 */
public interface MobileUserService {
	/**
	 * 分页查询
	 * @param page
	 * @param paramMap
	 * @return
	 */
	public JSONObject selectUserList(Map<String, Object> paramMap);

	/**
	 * 确认匹配页面
	 * @param agentId
	 * @param mobileUserId
     * @return
     */
	public Map<String, Object> confirmMatchingPage(long agentId, long mobileUserId);

	/**
	 * 确认匹配
	 * @param correlationUser
	 * @return
     */
	public Map<String, Object> confirmMatch(CorrelationUser correlationUser);


	/**
	 * t1端删除微信用户
	 * @return
	 */
	public boolean delMobileUser(Long mobileUserId);

	/**
	 * t1端解绑微信账号
	 * @return
	 */
	public boolean unBoundMobileUser(Long mobileUserId);

	public boolean copyWechatToT1(Long mobileUserId,Long t1UserId);

	/**
	 * 单个用户匹配T1账号
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> singleMatch(Map<String,Object> user);


	/**
	 * 根据微信用户ID 获取微信用户
	 * @param mobileUserId
	 * @return
	 */
	public MobileUser getEntity(Long mobileUserId);
	
	public void copyProgressData(Long mobileUserId, Long t1UserId);

}
