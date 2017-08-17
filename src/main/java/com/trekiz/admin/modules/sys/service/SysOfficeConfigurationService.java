package com.trekiz.admin.modules.sys.service;

import com.trekiz.admin.modules.sys.entity.SysOfficeProcessType;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.repository.SysOfficeProcessTypeDao;
import com.trekiz.admin.modules.sys.repository.SysOfficeProductTypeDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 
 * Copyright   2015  QUAUQ Technology Co. Ltd.
 *
 * 关于公司配置方面的服务类
 *
 * @author zhenxing.yan
 * @date 2015年11月16日
 */
@Service
public class SysOfficeConfigurationService {

	@Autowired
	private SysOfficeProcessTypeDao officeProcessTypeDao;
	
	@Autowired
	private SysOfficeProductTypeDao officeProductTypeDao;
	
	/**
	 * 获取公司产品类型列表
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId
	 * @return
	 */
	public List<SysOfficeProductType> obtainOfficeProductTypes(String companyId){
		Assert.hasText(companyId, "companyId should not be empty!");
		return officeProductTypeDao.findByCompanyIdAndDelFlag(companyId, 0);
	}
	
	/**
	 * 获取公司流程类型列表
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId
	 * @return
	 */
	public List<SysOfficeProcessType> obtainOfficeProcessTypes(String companyId){
		Assert.hasText(companyId, "companyId should not be empty!");
		return officeProcessTypeDao.findByCompanyIdAndDelFlag(companyId, 0);	
	}

	/**
	 * 保存公司产品类型和公司流程类型
	 * @param officeProductTypes
	 * @param officeProcessTypes
     * @return
     */
	@Transactional
	public JSONObject saveProductTypesAndProcessTypes(List<SysOfficeProductType> officeProductTypes, List<SysOfficeProcessType> officeProcessTypes){
		Assert.notEmpty(officeProductTypes,"officeProductTypes should not be empty!");
		Assert.notEmpty(officeProductTypes,"officeProductTypes should not be empty!");
		JSONObject result=new JSONObject();

		saveOfficeProcessTypes(officeProcessTypes);
		saveOfficeProductTypes(officeProductTypes);

		result.put("code",0);
		return result;
	}

	/**
	 * 保存公司产品类型列表
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param officeProductTypes
	 */
	@Transactional
	public OfficeConfigExecutionResult saveOfficeProductTypes(List<SysOfficeProductType> officeProductTypes){
		OfficeConfigExecutionResult result=new OfficeConfigExecutionResult();
		if (officeProductTypes==null||officeProductTypes.size()==0) {
			result.setSuccess(true);
			return result;
		}
		String companyId=officeProductTypes.get(0).getCompanyId();
		//先删除原来的记录
		officeProductTypeDao.deleteByCompanyId(companyId);
//		List<Integer> types=new ArrayList<Integer>();
//		for (SysOfficeProductType sysOfficeProductType : officeProductTypes) {
//			types.add(sysOfficeProductType.getProductType());
//		}
//		//先进行校验
//		result=validateDuplicatedProductTypes(companyId, types);
//		if (!result.getSuccess()) {
//			return result;
//		}
		//保存新的记录
		officeProductTypeDao.save(officeProductTypes);
		return result;
	}
	
	/**
	 * 保存公司流程类型列表
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param officeProcessTypes
	 */
	@Transactional
	public OfficeConfigExecutionResult saveOfficeProcessTypes(List<SysOfficeProcessType> officeProcessTypes){
		OfficeConfigExecutionResult result=new OfficeConfigExecutionResult();
		if (officeProcessTypes==null||officeProcessTypes.size()==0) {
			result.setSuccess(true);
			return result;
		}
		String companyId=officeProcessTypes.get(0).getCompanyId();
		//先删除原来的
		officeProcessTypeDao.deleteByCompanyId(companyId);
//		List<Integer> types=new ArrayList<Integer>();
//		for (SysOfficeProcessType sysOfficeProcessType : officeProcessTypes) {
//			types.add(sysOfficeProcessType.getProcessType());
//		}
//		//先进行校验
//		result=validateDuplicatedProcessTypes(companyId, types);
//		if (!result.getSuccess()) {
//			return result;
//		}
		//保存新的流程类型列表
		officeProcessTypeDao.save(officeProcessTypes);
		return result;
	}
	
	/**
	 * 校验数据库中的公司产品类型是否已经存在
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId
	 * @param types
	 * @return
	 */
	public OfficeConfigExecutionResult validateDuplicatedProductTypes(String companyId, List<Integer> types){
		OfficeConfigExecutionResult result=new OfficeConfigExecutionResult();
		Assert.hasText(companyId, "companyId should not be empty!");
		if (types==null||types.size()==0) {
			result.setSuccess(true);
			return result;
		}
		List<Integer> existTypes=officeProductTypeDao.findExistTypes(companyId, types);
		if (existTypes!=null&&existTypes.size()>0) {
			result.setSuccess(false);
			result.setMessage("types already exist: "+existTypes);
		}
		result.setSuccess(true);
		return result;	
	}
	
	/**
	 * 校验数据库中的公司流程类型是否已经存在
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId
	 * @param types
	 * @return
	 */
	public OfficeConfigExecutionResult validateDuplicatedProcessTypes(String companyId, List<Integer> types){
		OfficeConfigExecutionResult result=new OfficeConfigExecutionResult();
		Assert.hasText(companyId, "companyId should not be empty!");
		if (types==null||types.size()==0) {
			result.setSuccess(true);
			return result;
		}
		List<Integer> existTypes=officeProcessTypeDao.findExistTypes(companyId, types);
		if (existTypes!=null&&existTypes.size()>0) {
			result.setSuccess(false);
			result.setMessage("types already exist: "+existTypes);
		}
		result.setSuccess(true);
		return result;	
	}
}
