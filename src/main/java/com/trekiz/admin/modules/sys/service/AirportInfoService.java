package com.trekiz.admin.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;

import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.repository.AirportInfoDao;
@Service
@Transactional(readOnly = true)
public class AirportInfoService  extends BaseService{
	
	@Autowired
	AirportInfoDao airportInfoDao;

    public List<AirportInfo> fromAllAirportInfo(){
    	
    	return (List<AirportInfo>) airportInfoDao.findAll();
    }
	
    public AirportInfo getAirportInfo(Long id){
    	return airportInfoDao.findOne(id);
    }
    
    public List<AirportInfo> queryAirport(Long id){
    	
    	return (List<AirportInfo>) airportInfoDao.queryAirport(id);
    }
    
    
    public List<Map<String, Object>> fromAllAirportMapInfo(){
    	
    	
    	List<Map<String, Object>> list = airportInfoDao.findBySql("SELECT id,airport_name as airportName FROM sys_airport_info", Map.class);
    	
    	return list;
    }
	
	
}
