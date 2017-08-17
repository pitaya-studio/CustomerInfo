package com.trekiz.admin.modules.airticket.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.repository.FlightInfoDao;
/**
 * 

 * @Description:TODO

 * @author:midas

 * @time:2014-9-19 上午10:23:04
 */
@Service
@Transactional(readOnly = true)
public class FlightInfoServiceImpl extends BaseService implements IFlightInfoService{

	protected Logger logger = LoggerFactory.getLogger(FlightInfoServiceImpl.class);

	@Autowired
	private FlightInfoDao flightInfoDao;

	@Override
	public List<FlightInfo> findByFlightInfoByAirTicketId(Long airticketId) {
		// TODO Auto-generated method stub
		return this.flightInfoDao.findByFlightInfoByAirTicketId(airticketId);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delFlightInfo(FlightInfo flightInfo) {
		// TODO Auto-generated method stub
		flightInfoDao.delete(flightInfo);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delFlightInfoById(Long id) {
		// TODO Auto-generated method stub
		this.flightInfoDao.delete(id);
	}


	@Override
	public void delFlightInfoList(List<FlightInfo> flightInfo) {
		// TODO Auto-generated method stub
		flightInfoDao.delete(flightInfo);
		flightInfoDao.flush();
	}

	@Override
	public FlightInfo findById(Long id) {
		// TODO Auto-generated method stub
		return this.flightInfoDao.findOne(id);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void save(Set<FlightInfo> flightInfos) {
		// TODO Auto-generated method stub
		this.flightInfoDao.save(flightInfos);
	}

	@Override
	public FlightInfo save(FlightInfo flightInfo) {
		// TODO Auto-generated method stub
		return this.flightInfoDao.save(flightInfo);
	}
	

}
