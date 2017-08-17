package com.trekiz.admin.modules.eprice.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecordReply;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitLinesAreaDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceRecordReplyDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordReplyService;
@Service("estimatePriceRecordReplyService")
@Transactional(readOnly = true)
public class EstimatePriceRecordReplyServiceImpl implements
		EstimatePriceRecordReplyService {

	@Autowired
	private EstimatePriceRecordReplyDao estimatePriceRecordReplyDao;
	
	@Autowired
	private EstimatePriceAdmitLinesAreaDao estimatePriceAdmitLinesAreaDao;
	
	@Override
	public List<EstimatePriceRecordReply> findByOpeId(Long operatorUserId,
			Long recordId) {
		List<Object[]> list = new ArrayList<Object[]>(); 
		List<EstimatePriceRecordReply> backList= new ArrayList<EstimatePriceRecordReply>();
		list =estimatePriceRecordReplyDao.findByOpeId(operatorUserId, recordId);
		if(list!=null && list.size()>0){
			Iterator<Object[]> iter = list.iterator();
			while(iter.hasNext()){
				EstimatePriceRecordReply recRep = new EstimatePriceRecordReply();
				Object[] object = iter.next();
				recRep=change(recRep,object);
				String travalCountry = "";
				if(object!=null&&object.length>=14){
					travalCountry = estimatePriceAdmitLinesAreaDao.getLinesNames(String.valueOf(object[13]));
				}
				recRep.setTravelCountry(travalCountry);
				backList.add(recRep);
			}
		}
		return backList;
	}

	@Override
	public List<EstimatePriceRecordReply> findTrafficByOpeId(
			Long operatorUserId, Long recordId) {
		List<EstimatePriceRecordReply> backList= new ArrayList<EstimatePriceRecordReply>();
		List<Object[]> list = new ArrayList<Object[]>(); 
		list =estimatePriceRecordReplyDao.findTrafficByOpeId(operatorUserId, recordId);
		
		if(list!=null && list.size()>0){
			Iterator<Object[]> iter = list.iterator();
			while(iter.hasNext()){
				EstimatePriceRecordReply recRep = new EstimatePriceRecordReply();
				recRep=changeTraffic(recRep,iter.next());
				// 拼接多段航线的始发地和到达地
//				String city =findStartToEnd(recRep.getRid());
//				if(city!=null){
//					recRep.setCity(city);
//				}
				backList.add(recRep);
			}
		}
		return backList;
	}
	
	/**
	 * 单团计调转换
	 * @param recRep
	 * @param object
	 * @return
	 */
	private EstimatePriceRecordReply change(EstimatePriceRecordReply recRep,Object[] object){
		if(recRep!=null && object!=null && object.length>0){
			if(object[0]!=null){
				recRep.setPid(new Long((Integer) object[0]));
			}
			if(object[1]!=null){
				recRep.setRid(new Long((Integer)object[1]));
			}
			if(object[2]!=null){
				recRep.setRpid(new Long((Integer)object[2]));
			}
			if(object[3]!=null){
				recRep.setStatus((Integer)object[3]);
			}
			if(object[4]!=null){
				recRep.setOperatorUserId(new Long((Integer)object[4]));
			}
			if(object[5]!=null){
				recRep.setRecordDate((Date)object[5]);
			}
			if(object[6]!=null){
				recRep.setReplyDate((Date)object[6]);
			}
			if(object[7]!=null){
				recRep.setDgroupOutDate((Date)object[7]);
			}
			if(object[8]!=null){
				recRep.setOutsideDaySum((Integer)object[8]);
			}
			if(object[9]!=null){
				recRep.setOutsideNightSum((Integer)object[9]);
			}
			if(object[10]!=null){
				recRep.setTravelCountry((String)object[10]);
			}
			if(object[11]!=null){
				recRep.setTravelTeamType((Integer)object[11]);
			}
			if(object[12]!=null){
				recRep.setReplyPrice(((BigDecimal)object[12]).toString());
			}
			if(object[14]!=null){
				recRep.setPriceDetail((object[14]).toString());
			}
		}
		
		return recRep;
	}
	
	/**
	 * 机票计调转换
	 * @param recRep
	 * @param object
	 * @return
	 */
	private EstimatePriceRecordReply changeTraffic(EstimatePriceRecordReply recRep,Object[] object){
		if(recRep!=null && object!=null && object.length>0){
			if(object[0]!=null){
				recRep.setPid(new Long((Integer)object[0]));
			}
			if(object[1]!=null){
				recRep.setRid(new Long((Integer)object[1]));
			}
			if(object[2]!=null){
				recRep.setRpid(new Long((Integer)object[2]));
			}
			if(object[3]!=null){
				recRep.setStatus((Integer)object[3]);
			}
			if(object[4]!=null){
				recRep.setOperatorUserId(new Long((Integer)object[4]));
			}
			if(object[5]!=null){
				recRep.setRecordDate((Date)object[5]);
			}
			if(object[6]!=null){
				recRep.setReplyDate((Date)object[6]);
			}
			if(object[7]!=null){
				recRep.setReplyPrice(object[7].toString());
			}
			if(object[9]!=null&&object[8]!=null){
				recRep.setCity(object[8].toString()+"-"+object[9].toString());
			}
			if(object[10]!=null){
				recRep.setPriceDetail(object[10].toString());
			}
		}
		
		return recRep;
	}

	@Override
	public String findStartToEnd(Long recId) {
		String city = estimatePriceRecordReplyDao.findStartToEnd(recId);
		return city;
	}

}
