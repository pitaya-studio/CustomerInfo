package com.trekiz.admin.modules.eprice.service.impl;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;
import com.trekiz.admin.modules.eprice.repository.EstimatePricerReplyDao;
import com.trekiz.admin.modules.eprice.service.EstimatePricerReplyService;

@Service("estimatePricerReplyService")
@Transactional(readOnly = true)
public class EstimatePricerReplyServiceImpl implements EstimatePricerReplyService {

	@Autowired
	private EstimatePricerReplyDao estimatePricerReplyDao;
	
	public void save(EstimatePricerReply epr) {
		estimatePricerReplyDao.save(epr);
		estimatePricerReplyDao.getSession().flush();
	}

	public EstimatePricerReply findById(Long id) {
		
		return estimatePricerReplyDao.findById(id);
	}

	public void delById(Long id) {
		EstimatePricerReply epr = new EstimatePricerReply();
		epr.setId(id);
		epr.setStatus(EstimatePricerReply.STATUS_DEL);
		epr.setModifyTime(new Date());
		
		this.save(epr);
	}

	@Override
	public void send(EstimatePriceProject epp, EstimatePriceRecord epr, JSONArray jaa, JSONArray jat, boolean getByEntity) {
		// 地接计调
		if (getByEntity) {
			jaa = JSONArray.fromObject(epr.getAoperatorUserJson());
		}
		// 机票计调 
		if(getByEntity){
			jat = JSONArray.fromObject(epr.getToperatorUserJson());
		}
		Date date = new Date();
		EstimatePricerReply epry;
		Long pid = epr.getPid();
		Long rid = epr.getId();
		Long uid = epr.getUserId();
		String userName = epr.getUserName();
		// 地接计调生成
		if(jaa!=null && !jaa.isEmpty()){
			for (int i = 0; i < jaa.size(); i++) {
				JSONObject jt = jaa.getJSONObject(i);
				epry = new EstimatePricerReply();
				epry.setPid(pid);
				epry.setRid(rid);
				epry.setUserId(uid);
				epry.setUserName(userName);
				
				epry.setOperatorUserId(jt.getLong("userId"));
				epry.setOperatorUserName(jt.getString("userName"));
				
				epry.setStatus(EstimatePricerReply.STATUS_WAITTING);
				epry.setType(epp.getType());
				
				epry.setCreateTime(date);
				epry.setModifyTime(date);
				
				this.save(epry);
			}
		}
		// 机票计调生成
		if(jat!=null && !jat.isEmpty()){
			for (int i = 0; i < jat.size(); i++) {
				JSONObject jt = jat.getJSONObject(i);
				epry = new EstimatePricerReply();
				epry.setPid(pid);
				epry.setRid(rid);
				epry.setUserId(uid);
				epry.setUserName(userName);
				
				epry.setOperatorUserId(jt.getLong("userId"));
				epry.setOperatorUserName(jt.getString("userName"));
				
				epry.setStatus(EstimatePricerReply.STATUS_WAITTING);
				epry.setType(EstimatePricerReply.TYPE_FLIGHT);
				
				epry.setCreateTime(date);
				epry.setModifyTime(date);
				
				this.save(epry);
			}
		}
	}

	
	public EstimatePricerReply findReplyByRidAndOperatorUserId(Long rid,Long operatorUserId,String types) {
		if(rid==null || operatorUserId==null){
			return null;
		}
		EstimatePricerReply epr = estimatePricerReplyDao.findReplyByRidAndOperatorUserId(rid, operatorUserId,types);
		
		return epr;
	}

	
	public List<EstimatePricerReply> findReply(Long rid, String types) {
		
		return estimatePricerReplyDao.findReply(rid, types);
	}

	@Override
	public List<EstimatePricerReply> findReplyByAdmit(Long rid) {
		// TODO Auto-generated method stub
		return estimatePricerReplyDao.findReplyByAdmit(rid);
	}


}
