package com.trekiz.admin.review.configuration.service;

import java.util.List;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.trekiz.admin.modules.sys.entity.SysUserReviewCommonPermission;
import com.trekiz.admin.modules.sys.repository.UserPermissionDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户权限检查器
 * @author yanzhenxing
 * @date 2015/12/9
 */
@Service
public class QuauqUserPermissionChecker implements UserReviewPermissionChecker {

    @Autowired
    private UserPermissionDao userPermissionDao;

    @Override
    public boolean hasAutoApprovePermission(String userId) {
        if (StringUtils.isNotBlank(userId)){
           List<SysUserReviewCommonPermission> permissions= userPermissionDao.findByUserId(Long.parseLong(userId));
            if (permissions!=null&&permissions.size()>0){
                return permissions.get(0).getIs_applier_auto_approve()==1?true:false;
            }
        }

        return false;
    }

    @Override
    public boolean hasJumpTaskPermission(String userId) {
        return false;
    }
}
