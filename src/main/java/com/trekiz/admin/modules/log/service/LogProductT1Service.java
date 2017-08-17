package com.trekiz.admin.modules.log.service;

import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.log.entity.LogProductT1;
import com.trekiz.admin.modules.log.repository.LogProductT1Dao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by zong on 2016/7/26.
 */
@Service
@Transactional(readOnly = true)
public class LogProductT1Service {
    @Autowired
    private LogProductT1Dao logProductT1Dao;
    @Autowired
    private TravelActivityService travelActivityService;
    @Autowired
    private ActivityGroupService activityGroupService;

    public void saveLogProductT1(Long groupId) {
        ActivityGroup activityGroup = activityGroupService.findById(groupId);
        TravelActivity travelActivity = travelActivityService.findById(activityGroup.getSrcActivityId().longValue());

        User user = UserUtils.getUser();

        LogProductT1 logProductT1 = new LogProductT1();
        logProductT1.setUuid(UuidUtils.generUuid());
        logProductT1.setProductType(2);
        logProductT1.setActivityId(travelActivity.getId());
        logProductT1.setGroupId(activityGroup.getId().intValue());
        logProductT1.setBusinessType(4);
        logProductT1.setCreateBy(user.getId());
        logProductT1.setCreateDate(new Date());
        logProductT1.setCompanyId(travelActivity.getProCompany());
        logProductT1Dao.save(logProductT1);
    }
}
