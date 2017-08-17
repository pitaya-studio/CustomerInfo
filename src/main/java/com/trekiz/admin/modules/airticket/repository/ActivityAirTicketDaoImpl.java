package com.trekiz.admin.modules.airticket.repository;

import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;

/**
 * 自定义DAO接口实现

 * @Description:TODO

 * @author:midas

 * @time:2014-9-19 上午10:21:23
 */
@Repository
class ActivityAirTicketDaoImpl extends BaseDaoImpl<ActivityAirTicket> implements
		ActivityAirTicketDaoCustom {
	
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
    public Map<String, Object> getMeituIncomeInfoByAirTicketId(Long airTicketId) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT airticket.startingDate,airticket.journey,airticket.group_code AS airGroupCode,air_order.person_num AS personNum,air_order.group_code AS orderGroupCode FROM activity_airticket airticket ");
    	sb.append("LEFT JOIN airticket_order air_order ON airticket.id = air_order.airticket_id WHERE airticket.id=?");
    	List<Map<String, Object>> meituIncomeInfo = findBySql(sb.toString(), Map.class, airTicketId);
    	if(CollectionUtils.isEmpty(meituIncomeInfo)) {
    		return null;
    	}
    	return meituIncomeInfo.get(0);
    }

}