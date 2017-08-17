package com.trekiz.admin.review.configuration.extend;

import com.trekiz.admin.modules.sys.repository.UserDeptJobNewDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 销售部门查询
 * @author yanzhenxing
 * @date 2015/12/19
 */

@Service("salerDeptChecker")
public class SalerDeptChecker {

    @Autowired
    private UserDeptJobNewDao userDeptJobNewDao;

    /**
     * 检查用户的销售职务所在部门
     * @param userId
     * @return
     */
    public Integer checkDeptId(String userId){
        if (StringUtils.isBlank(userId)) return 0;
        try {
            List<Object[]> results=userDeptJobNewDao.findUserDeptIdAndJobIdAndJobType(Long.parseLong(userId));
            if (results.size()==0) return 0;
            for (Object[] objs:results){
                if (objs.length==3){
                    if ((int)objs[2]==1){//销售类的部门职务
                        return (Integer)objs[0];
                    }
                }
            }
            return 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
