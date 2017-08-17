package com.trekiz.admin.review.configuration.extend;

import com.quauq.review.core.extend.identity.IdentityServiceExtend;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务扩展实现类，用于通过组id获取用户id列表
 * @author yanzhenxing
 * @date 2015/12/8
 */

public class QuauqIdentityServiceExtend implements IdentityServiceExtend {

    @Autowired
    private SystemService systemService;
    @Autowired
    private UserDao userDao;

    @Override
    public List<String> findUsersByGroupId(String groupId) {
        if(StringUtils.isBlank(groupId)){
            throw new RuntimeException("groupId 不能为空，且格式为：部门-职位");
        }
        if(groupId.indexOf("-") == -1){
            throw new RuntimeException("groupId 格式不正确，格式应为：部门-职位");
        }
        if(groupId.split("-").length != 2){
            throw new RuntimeException("groupId 格式不正确，格式应为：部门-职位");
        }
        List<String> userIds = new ArrayList<String>();
        String[] values=groupId.split("-");
        Long deptId = Long.parseLong(values[0]);
        Long jobId = Long.parseLong(values[1]);
        String companyUuid=UserUtils.getUser().getCompany().getUuid();
        List<String> allUserIds = systemService.findUserIdsByDeptJob(deptId,jobId,companyUuid);
        //去除删除的用户
        if(CollectionUtils.isNotEmpty(allUserIds)){
            for (String userId : allUserIds){
                Long longUserId = Long.valueOf(userId);
                User existUser = userDao.findById(longUserId);
                if(null != existUser && existUser instanceof User){
                    userIds.add(userId);
                }
            }
        }
        if (CollectionUtils.isEmpty(userIds)){
            throw new RuntimeException("审批用户查询为空");
        }
        return userIds;
    }
}
