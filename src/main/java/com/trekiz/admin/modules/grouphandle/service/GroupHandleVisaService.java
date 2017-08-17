package com.trekiz.admin.modules.grouphandle.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.grouphandle.dao.GroupHandleVisaDao;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandleVisa;

/**
 * 团控签证service
 * @date 2016-2-3 16:27:04
 * @version V1.0  
 */
@Service
public class GroupHandleVisaService {
	
	@Autowired
	private GroupHandleVisaDao groupHandleVisaDao;
	
	/**
	 * 依据id获取一条团控签证记录
	 * @param orderId
	 * @return
	 */
	public GroupHandleVisa findById(Integer id) {
		return groupHandleVisaDao.findById(id);
	}

	/**
	 * 批量保存
	 * @param tobeSaveVisas
	 */
	public void batchSave(List<GroupHandleVisa> tobeSaveVisas) {
		groupHandleVisaDao.batchSave(tobeSaveVisas);
	}
	
	/**
	 * 批量保存，返回保存成功的实体
	 * @param tobeSaveVisas
	 */
	public List<GroupHandleVisa> batchSaveReturn(List<GroupHandleVisa> tobeSaveVisas) {
		List<GroupHandleVisa> returnVisas = new ArrayList<>();
		for (GroupHandleVisa groupHandleVisa : tobeSaveVisas) {
			returnVisas.add(groupHandleVisaDao.save(groupHandleVisa));
		}
		return returnVisas;
	}

	/**
	 * 批量更新
	 * @param tobeDeleteVisas
	 */
	public void batchUpdate(List<GroupHandleVisa> tobeUpdateVisas) {
		groupHandleVisaDao.batchUpdate(tobeUpdateVisas);
	}
	
	/**
	 * 批量删除
	 * @param tobeDeleteVisas
	 */
	public void batchDelete(List<GroupHandleVisa> tobeDeleteVisas) {
		groupHandleVisaDao.batchDelete(tobeDeleteVisas);
	}

	/**
	 * 依据团控id和游客id获取对应状态的签证记录
	 */
	public List<GroupHandleVisa> getByHandleAndTraveler(Integer handleId, Long travelerId, String delFlagNormal) {
		return groupHandleVisaDao.findByGroupHandleIdAndTravelerId(handleId, Integer.parseInt(travelerId.toString()), delFlagNormal);
	}
	
}
