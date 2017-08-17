package com.trekiz.admin.review.guarantee.service;

import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zong on 2016/7/8.
 */

@Service
@Transactional(readOnly = true)
public class GuaranteeService {

    @Autowired
    private ReviewDao reviewDao;

    /**
     * 检查游客是否在审批中
     * @param travelerId
     * @return
     */
    public String isReviewing(Long travelerId, String orderId) {
        String sql = "SELECT status from review_new rn WHERE rn.traveller_id = ? and order_id = ? and rn.process_type = 22 order by create_date DESC";
        List<Map<String, Object>> list = reviewDao.findBySql(sql, Map.class, travelerId, orderId);
        if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(0);
            if ("0".equals(map.get("status").toString()) || "1".equals(map.get("status").toString())) {
                return "yes";
            }
        }
        return "no";
    }
}
