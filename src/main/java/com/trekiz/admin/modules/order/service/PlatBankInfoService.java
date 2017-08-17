package com.trekiz.admin.modules.order.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;

@Service
@Transactional(readOnly = true)
public class PlatBankInfoService extends BaseService {

	@Autowired
	private PlatBankInfoDao productorderDao;

	public List<PlatBankInfo> getPlatBankInfoByBeLongPlatId(Long beLongPlatId,
			Integer platType, Long bankId) {

		return productorderDao.findPlatBankInfoByBeLongPlatId(beLongPlatId,
				platType, bankId);
	}
	
	/**
	 * 根据平台类型和平台id查询
	 * @param beLongPlatId
	 * @param platType
	 * @return
	 */
	public List<PlatBankInfo> findByBeLongPlatIdAndPlatType(Long beLongPlatId, Integer platType) {
		return productorderDao.findPlatBankInfoByBeLongPlatId(beLongPlatId, platType);
//		String sql = "select distinct bankName from plat_bank_info where beLongPlatId = ? and platType = ? order by defaultFlag";
//		List<PlatBankInfo> platBankInfo = productorderDao.findBySql(sql,
//				beLongPlatId, platType);
//		return platBankInfo;
	}
	/**
	 * 根据平台类型和平台id查询(去掉重复账户名称)
	 * @param beLongPlatId
	 * @param platType
	 * @return
	 */
	public List<Map<String,Object>> findByBeLongPlatIdAndPlatTypeforDistinct(Long beLongPlatId, Integer platType) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT DISTINCT p.bankName  FROM plat_bank_info p WHERE p.beLongPlatId=? AND p.platType=?");
		List<Map<String,Object>> list=productorderDao.findBySql(sbf.toString(), Map.class,beLongPlatId,platType);
		return list;
//		String sql = "select distinct bankName from plat_bank_info where beLongPlatId = ? and platType = ? order by defaultFlag";
//		List<PlatBankInfo> platBankInfo = productorderDao.findBySql(sql,
//				beLongPlatId, platType);
//		return platBankInfo;
	}
	
	/**
	 * 根据平台类型和平台id查询平台默认账号
	 * @param platBankInfo
	 */
	public List<PlatBankInfo> findDefaultBankInfo(Long beLongPlatId, Integer platType) {
		return productorderDao.findDefaultBankInfo(beLongPlatId, platType);
	}

	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void save(PlatBankInfo platBankInfo) {
		productorderDao.save(platBankInfo);
	}

	/**
	 * 名称不能重复，结果集只包含bankName
	 */
	List<String> getPlatBankNameByPlatTypeAndId(Integer belongParentPlatId,
			Integer platType) {
		String sql = "select distinct bankName from plat_bank_info where beLongPlatId = ? and platType = ? order by defaultFlag";
		List<String> platBankInfo = productorderDao.findBySql(sql,
				belongParentPlatId, platType);
		return platBankInfo;
	}
	
	/**
	 * 更具批发商ID和银行名称进行过滤
	 */
	@SuppressWarnings("rawtypes") List getBankInfo(Long id,String bankName) {
		String deleteOfficeContactsSql = "select id,defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks from plat_bank_info where beLongPlatId = ? and bankName=? order by defaultFlag";
		List platBankInfo = productorderDao.findBySql(deleteOfficeContactsSql, new Object[]{id,bankName});
		return platBankInfo;
	}
	/**
	 * 根据平台ID和平台类型,获取帐户列表
	 * @author gao
	 * @param id
	 * @param platType
	 * @return
	 */
	public List<PlatBankInfo> findBankInfoList(Long beLongPlatId,Integer platType){
		List<PlatBankInfo> list = productorderDao.findBankInfoList(beLongPlatId, platType);
		return list;
	}
	/**
	 * 根据账户ID，获取账户对象
	 * @author gao
	 * @param id
	 * @return
	 */
	public PlatBankInfo findBankInfoById(Long id){
		PlatBankInfo info = productorderDao.findOne(id);
		return info;
	}
	
	
	
	/*
	 * 获取银行账户名
	 * @param beLongPlatId 所属平台id   必填参数
	 * @param platType 所属平台类型  批发商0，地接社1，渠道商2  供应商3  必填参数
	 * @param bankName 银行名称  非必填参数
	 * @param bankAccountCode 银行账号 非必填参数
	 * @return 银行账户名列表，排列顺序：先境内账户（先默认后非默认），后境外账户（先默认）
	 * @author xianglei.dong
	 */
	public String getAccountName(Long beLongPlatId, Integer platType,
			String bankName, String bankAccountCode, String bankType) {
		return productorderDao.getAccountName(beLongPlatId, platType, bankName, bankAccountCode, bankType);
	}
	/**
	 * 根据银行名称和银行账号查询银行信息
	 * @param bankName			银行名称
	 * @param bankAccount		银行账号
	 * @return
	 * @author	shijun.liu
	 * @date	2016.04.13
	 */
	public List<PlatBankInfo> getPlatBankInfo(String bankName, String bankAccount){
		if(StringUtils.isBlank(bankName) || StringUtils.isBlank(bankAccount)){
			return null;
		}
		return productorderDao.getPlantBankInfo(bankName, bankAccount);
	}
	/**
	 * 根据渠道商ID和银行账号查询银行信息
	 * @param beLongPlatId		渠道商ID
	 * @param bankAccount		银行账号
	 * @return
	 * @author	jinxin.gao
	 * @date	2016.04.13
	 */
	List<PlatBankInfo> getPlantBankInfoForAgintidAndBankAccount(
			long beLongPlatId, String bankAccount) {
		if(StringUtils.isBlank(String.valueOf(beLongPlatId)) || StringUtils.isBlank(bankAccount)){
			return null;
		}
		return productorderDao.getPlantBankInfoForAgintidAndBankAccount(beLongPlatId,bankAccount);
	}

}