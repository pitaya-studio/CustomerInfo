package com.trekiz.admin.modules.airticket.service;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.repository.AirTicketReserveOrderDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.AirTicketReserveOrder;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.utils.UserUtils;
@Service
@Transactional(readOnly = true)
public class AirTicketReserveOrderServiceImpl  extends BaseService implements
		IAirTicketReserveOrderService {
     @Autowired
	public AirTicketReserveOrderDao airTicketReserveOrderDao;
     
     
	@Override
	public Page<AirTicketReserveOrder> findAirTicketReserveOrder(
			Page<AirTicketReserveOrder> page,ActivityAirTicket airTicket,
			String departureCity, String arrivedCity,String agentId, String minprice,
			String maxprice, String airType, String startTime,
			String endTime,String paymentType,String orderby,DepartmentCommon common) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		 DetachedCriteria dc = DetachedCriteria.forClass(AirTicketReserveOrder.class);
		 dc.add(Restrictions.eqOrIsNull("reserveType", 1));
		 dc.add(Restrictions.eq("activityAirTicket.proCompany", companyId));
		 dc.createAlias("activityAirTicket", "activityAirTicket");
		 if(StringUtils.isNotBlank(agentId)){
			 dc.add(Restrictions.eq("agentId", StringUtils.toLong(agentId)));
		 }
		 if(StringUtils.isNotBlank(departureCity)){
			 dc.add(Restrictions.eq("activityAirTicket.departureCity", departureCity));
		 }
		if(StringUtils.isNotBlank(arrivedCity)){
			dc.add(Restrictions.eq("activityAirTicket.arrivedCity", arrivedCity));
		}
		if(StringUtils.isNotBlank(minprice) ){
			dc.add(Restrictions.ge("activityAirTicket.settlementAdultPrice", StringNumFormat.getBigDecimalValue(minprice)));
		}
		if(StringUtils.isNotBlank(maxprice)){
			dc.add(Restrictions.le("activityAirTicket.settlementAdultPrice", StringNumFormat.getBigDecimalValue(maxprice)));
		}
		if(StringUtils.isNotBlank(airType)){
			dc.add(Restrictions.eq("activityAirTicket.airType", airType));
		}
		if (StringUtils.isNotBlank(airTicket.getAirlines())) {
		    dc.add(Restrictions.like("activityAirTicket.airlines","%"+airTicket.getAirlines()+"%"));
		}
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			java.util.Date startDate = DateUtils.dateFormat(startTime,"yyyy-MM-dd");
			java.util.Date endDate = DateUtils.dateFormat(endTime,"yyyy-MM-dd");
			dc.add(Restrictions.between("createDate", startDate, endDate));
		}
		
		
		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}
		if(StringUtils.isNotBlank(paymentType)){
			dc.add(Restrictions.sqlRestriction("{alias}.agentId in (select id from agentinfo where paymentType = "+paymentType+" )"));
		}
		
		return airTicketReserveOrderDao.find(page, dc);
	}



}
