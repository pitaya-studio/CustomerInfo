package com.trekiz.admin.modules.order.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.order.entity.OrderTrackingSetting;
import com.trekiz.admin.modules.order.repository.OrderTrackingSettingDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OrderTrackingSettingService  extends BaseService {
    
    @Autowired
    private OrderTrackingSettingDao orderTrackingSettingDao;
    
    /**
     * 查询订单跟踪设置列表
     * @author yakun.bai
     * @Date 2016-8-12
     */
    public Page<OrderTrackingSetting> find(Page<OrderTrackingSetting> page) {
    	DetachedCriteria dc = orderTrackingSettingDao.createDetachedCriteria();
    	dc.add(Restrictions.eq("companyId", UserUtils.getUser().getCompany().getId()));
    	return orderTrackingSettingDao.find(page, dc);
    }
    
    /**
     * 查询订单跟踪设置
     * @author yakun.bai
     * @Date 2016-8-12
     */
    public OrderTrackingSetting findById(Long settingId) {
    	return orderTrackingSettingDao.findOne(settingId);
    }
    
    /**
     * 保存订单跟踪设置
     * @author yakun.bai
     * @Date 2016-8-12
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void save(OrderTrackingSetting orderTrackingSetting) {
    	orderTrackingSettingDao.save(orderTrackingSetting);
    }
    
}