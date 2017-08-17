package com.trekiz.admin.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.RemindDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.IRemindService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;

@Service
public class RemindServiceImpl implements IRemindService {
    @Autowired
    private RemindDao remindDao;
    @Autowired
    private TravelActivityDao travelActivityDao;
    @Autowired
    private ActivityGroupDao activityGroupDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ActivityAirTicketDao activityAirTicketDao;
    @Autowired
    private VisaProductsDao visaProductsDao;
    @Override
    @Transactional(readOnly = true)
    public Remind get(Integer id) {
        return remindDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void delRemind(Integer id) throws Exception {
        try {
            remindDao.setDelFlagForRemind(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void save(Remind remind) throws Exception {
        try {
//            if (Context.SYS_REMIND_ALL_SELECTED == remind.getSelectedRemindType()) {
//                remind.setSelectedRemindOrderType("");
//            }
            // 由userStr，得出提醒接收人，存入中间表，并且获取userList存入remind
//            String [] userStrings = null;
//            if (StringUtils.isNotBlank(remind.getUserStr())) {
//            	userStrings = remind.getUserStr().split(",");
//			}
//            List<Long> userIdList = new ArrayList<>();
//            if (userStrings != null && userStrings.length > 0) {
//            	for (int i = 0; i < userStrings.length; i++) {
//            		userIdList.add(Long.parseLong(userStrings[i]));
//				}
//			}
//            List<User> remindUsers = new ArrayList<>();
//            remindUsers = userDao.findByIds(userIdList);  // 提醒接收人
            // 保存中间表
//            remind.setUserIdList(userIdList);
//            remind.setUserList(remindUsers);
            
            remindDao.save(remind);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Remind> getAllRemindByRemindType(Integer remindType) throws Exception {
        List<Remind> remindList = null;
        try {
            if (remindType == null) {
                remindList = remindDao.findByDelFlagEquals("0");
            } else {
                remindList = remindDao.findByRemindTypeAndDelFlagEquals(remindType, "0");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取提醒列表出错!");
        }
        return remindList;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<ActivityGroup> getActivityGroupsByActivityKind(Integer activityKind) throws Exception {
        List<ActivityGroup> activityGroupList = null;
        try {
            activityGroupList = null;
            // TODO 太慢
            List<TravelActivity> activityIdList = travelActivityDao.findIdByActivityKindAndDelFlag(activityKind, Context.DEL_FLAG_NORMAL);
            if (activityIdList != null && activityIdList.size() > 0) {
                for (TravelActivity activity : activityIdList) {
                    activityGroupList = activityGroupDao.findBySrcActivityIdAndDelFlag(Integer.parseInt(activity.getId().toString()), Context.DEL_FLAG_NORMAL);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        return activityGroupList;
    }

	@Override
	public List<Remind> getRemindRulesByCompany(String companyUuid,
			Integer remindType) {
		return remindDao.findByCompanyAndType(companyUuid, remindType);
	}

	@Override
	public Set<Integer> getProTypesByCompany(String companyUuid,
			Integer remindType) {
		List<Remind> tempReminds = remindDao.findByCompanyAndType(companyUuid, remindType);
		List<Integer> tempProType = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(tempReminds)) {
			for (Remind remind : tempReminds) {
				if (StringUtils.isNotBlank(remind.getSelectedRemindOrderType())) {					
					tempProType.add(Integer.parseInt(remind.getSelectedRemindOrderType()));
				}
			}
		}
		Set<Integer> returnProType = new HashSet<>(tempProType);
		return returnProType;
	}

	@Override
	public List<ActivityGroup> getGroupsByRemindRule(Integer id) {
		Remind remind = remindDao.findOne(id);
		List<ActivityGroup> resultGroups = new ArrayList<>();
		if (remind == null) {
			return null;
		}
		// 如果规则选择了“全部团期”, 则从数据库中查询
		Office office = officeService.findWholeOfficeByUuid(remind.getCompanyUuid());
		if (remind.getSelectedRemindType() == 1) {
			if (StringUtils.isBlank(remind.getSelectedRemindOrderType())) {
				return null;
			}
			resultGroups = activityGroupDao.findByProductTypeAndCompany(Integer.parseInt(remind.getSelectedRemindOrderType()), office.getId());
		} else {
			String groupIdStr = remind.getActivityGroupIds();
			if (StringUtils.isBlank(groupIdStr)) {
				return null;
			}
			String [] groupIdArray = groupIdStr.split(",");
			if (groupIdArray != null && groupIdArray.length > 0) {
				for (int i = 0; i < groupIdArray.length; i++) {
					if (StringUtils.isNotBlank(groupIdArray[i])) {
						resultGroups.add(activityGroupDao.getById(Long.parseLong(groupIdArray[i])));
					}
				}
			}
		}
		return resultGroups;
	}

	@Override
	public List<String> getProductIdListByRemindRule(Integer id) {
		Remind remind = remindDao.findOne(id);
		if (remind == null) {
			return null;
		}
		if (StringUtils.isBlank(remind.getSelectedRemindOrderType())) {
			return null;
		}
		Office office = officeService.findWholeOfficeByUuid(remind.getCompanyUuid());
		List<String> resultProducts = new ArrayList<>();
		
		if (remind.getSelectedRemindType() == 1) {
			if (Context.ACTIVITY_KINDS_QZ.equals(remind.getSelectedRemindOrderType())) {  // 签证
				List<VisaProducts> products = visaProductsDao.findByCompany(office.getId());
				for (VisaProducts visaProducts : products) {
					resultProducts.add(visaProducts.getId().toString());
				}
			} else if (Context.ACTIVITY_KINDS_JP.equals(remind.getSelectedRemindOrderType())) {  // 机票
				List<ActivityAirTicket> products = activityAirTicketDao.findByCompany(office.getId());
				for (ActivityAirTicket activityAirTicket : products) {
					resultProducts.add(activityAirTicket.getId().toString());
				}
			} else {
				List<TravelActivity> products = travelActivityDao.findAllIdsByCompanyAndType(Integer.parseInt(remind.getSelectedRemindOrderType()), office.getId());
				for (TravelActivity travelActivity : products) {
					resultProducts.add(travelActivity.getId().toString());
				}
			}
		} else if (remind.getSelectedRemindType() == 2) {
			String productIdStr = remind.getProductIds();
			String [] productIdArray = productIdStr.split(",");
			if (productIdArray != null && productIdArray.length > 0) {
				for (int i = 0; i < productIdArray.length; i++) {
					if (StringUtils.isNotBlank(productIdArray[i])) {
						resultProducts.add(productIdArray[i]);
					}
				}
			}
		} else {
			return null;
		}
		
		return resultProducts;
	}

	@Override
	public List<User> findUsersByRemind(Remind remind) {
		List<Integer> userIds = remindDao.findAllUserByRemind(remind.getId());
		List<User> returnUsers = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userIds)) {
			for (Integer userid : userIds) {
				User tmepUser = userDao.findById(Long.parseLong(userid.toString()));
				returnUsers.add(tmepUser);
			}
		}
		return returnUsers;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Page<Map<Object, Object>> findRemindList(Map<String, String> parameters, Page<Map<Object, Object>> page) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append("sr.id as id, ");
		sb.append("sr.remind_type as remindType, ");
		sb.append("sr.remind_name as remindName, ");
		sb.append("sr.start_remind_status as startRemindStatus, ");
		sb.append("sr.start_remind_days as startRemindDays, ");
		sb.append("sr.end_remind_status as endRemindStatus, ");
		sb.append("sr.end_remind_days as endRemindDays ");
		sb.append("from sys_remind sr ");
		sb.append("where 1=1 ");
		sb.append("and sr.delFlag=0 ");

		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(StringUtils.isNotBlank(companyUuid)){
			sb.append(" and company_uuid='").append(companyUuid).append("'");
		}
		if(StringUtils.isNotBlank(parameters.get("remindName"))){
			sb.append(" and remind_name like '%").append(parameters.get("remindName")).append("%'");
		}
		if(StringUtils.isNotBlank(parameters.get("remindType"))){
			sb.append(" and remind_type='").append(parameters.get("remindType")).append("'");
		}
		return remindDao.findPageBySql(page, sb.toString(), Map.class);
	}
	
}
