package com.trekiz.admin.modules.airticket.repository;

import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
/**
 * 
 * @Description: 自定义DAO接口实现

 * @author:midas

 * @time:2014-9-19 上午10:21:55
 */
interface ActivityAirTicketDaoCustom extends BaseDao<ActivityAirTicket> {

	/**
     * 根据机票产品id获取美途收入单信息
     * @Description: 
     * @param @param airTicketId
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author majiancheng
     * @date 2015-11-11 下午12:25:23
     */
    public Map<String, Object> getMeituIncomeInfoByAirTicketId(Long airTicketId);

}