package com.trekiz.admin.modules.sys.service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRemindService {

    List<Remind> getAllRemindByRemindType(Integer remindType) throws Exception;

    Remind get(Integer id);

    void save(Remind remind) throws Exception;

    void delRemind(Integer id) throws Exception;

    List<ActivityGroup> getActivityGroupsByActivityKind(Integer activityKind) throws Exception;

    /**
     * 根据批发商uuid，提醒类型，获取 批发商下所有正常的remindRules（提醒规则）
     * @param companyUuid 批发商uuid
     * @param remindType 提醒类型
     * @return
     */
	List<Remind> getRemindRulesByCompany(String companyUuid, Integer remindType);

	/**
	 * 根据批发商id，提醒类型，获取批发商下，某中类型的提醒规则涉及到的所有产品类型
	 * @param companyUuid 批发商uuid
	 * @param remindType 提醒类型
	 * @return
	 */
	Set<Integer> getProTypesByCompany(String companyUuid, Integer remindType);

	/**
	 * 根据提醒规则id，获取该规则下所有单团团期
	 * @param id remind规则id
	 * @return
	 */
	List<ActivityGroup> getGroupsByRemindRule(Integer id);

	/**
	 * 根据提醒规则id，获取该规则下所有产品类型
	 * @param id
	 * @return
	 */
	List<String> getProductIdListByRemindRule(Integer id);

	// 获取提醒可见人集合
	List<User> findUsersByRemind(Remind remind);

	/**
	 * 查询提醒列表
	 * @date 2016年4月11日
	 */
	Page<Map<Object,Object>> findRemindList(Map<String, String> parameters, Page<Map<Object, Object>> mapPage);
}
