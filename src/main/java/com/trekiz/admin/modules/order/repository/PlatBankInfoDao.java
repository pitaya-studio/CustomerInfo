/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;

/**
 * 文件名: PlatBankInfoDao.java 功能:
 * 
 * 修改记录:
 * 
 * @author chy
 * @DateTime 2014年11月4日15:30:26
 * @version 1.0
 */
public interface PlatBankInfoDao extends PlatBankInfoDaoCustom,
		CrudRepository<PlatBankInfo, Long> {

	@Query("from PlatBankInfo where beLongPlatId = ?1 and platType = ?2 and bankId = ?3")
	public List<PlatBankInfo> findPlatBankInfoByBeLongPlatId(Long beLongPlatId,
			Integer platType, Long bankId);
	
	@Query("from PlatBankInfo where beLongPlatId = ?1 and platType = ?2")
	public List<PlatBankInfo> findBankInfoList(Long beLongPlatId,Integer platType);
	
	@Query("from PlatBankInfo where beLongPlatId = ?1 and platType = ?2")
	public List<PlatBankInfo> findPlatBankInfoByBeLongPlatId(Long beLongPlatId, Integer platType);
	
	@Query("from PlatBankInfo where beLongPlatId = ?1 and platType = ?2 and (defaultFlag = 0 or defaultFlag = 4) order by belongType")
	public List<PlatBankInfo> findDefaultBankInfo(Long beLongPlatId, Integer platType);

	@Query("from PlatBankInfo where beLongPlatId = ?1 and platType = ?2 and bankName = ?3 and bankAccountCode = ?4")
	public List<PlatBankInfo> findPlatBankInfoByBeLongPlatId(Long beLongPlatId, Integer platType, String bankName, String bankAccountCode);
	
	@Query("from PlatBankInfo where beLongPlatId = ?1 and  bankAccountCode = ?2")
	public List<PlatBankInfo> findPlatBankInfoByBeLongPlatIdAndBankAccountCode(Long beLongPlatId,  String bankAccountCode);
	
	/**
	 * 查询批发商默认的账户信息
	 * @param beLongPlatId  平台Id
	 * @param platType  平台类型 0表示批发商,1表示地接社,2表示渠道
	 * @param belongType  1境内,2境外
	 * @return PlatBankInfo
	 * @author 赵海明
	 * @date 2015-10-19
	 * */
	@Query("from PlatBankInfo where beLongPlatId = ?1 and platType = ?2 and belongType = ?3 and defaultFlag = ?4")
	public PlatBankInfo findBankInfo(Long beLongPlatId,Integer platType,Long belongType,String defaultFlag );
	@Query("from PlatBankInfo where platType = ?1 group by bankName")
	public List<PlatBankInfo> findPlatBankInfoByBeLongPlatId(Integer platType);
}

/**
 * 文件名: PlatBankInfoDao.java 功能:
 * 
 * 修改记录:
 * 
 * @author chy
 * @DateTime 2014年11月4日15:31:20
 * @version 1.0
 */
interface PlatBankInfoDaoCustom extends BaseDao<PlatBankInfo> {
   	public List<String> getBankName(Long id,Integer type);
   	public List<String> getBankAccount(Long id,Integer type,String bankName);
	public List<PlatBankInfo> getPlantBankInfo(String bankName, String bankAccount);
	public List<PlatBankInfo> getPlantBankInfoForAgintidAndBankAccount(Long beLongPlatId, String bankAccount);
   public String getAccountName(Long beLongPlatId, Integer platType, String bankName, String bankAccountCode, String bankType);
}

/**
 * 文件名: PlatBankInfoDao.java 功能:
 * 
 * 修改记录:
 * 
 * @author chy
 * @DateTime 2014年11月4日15:31:34
 * @version 1.0
 */
@Repository
class PlatBankInfoDaoImpl extends BaseDaoImpl<PlatBankInfo> implements
		PlatBankInfoDaoCustom {
	/**
	 * 获取银行名称
	 * @author zhaohaiming
	 * @param id 渠道、地接社、供应商Id
	 * @param type  0批发商，1地接社，2渠道商
	 * @return list
	 * */
	public List<String> getBankName(Long id,Integer type) {
		String deleteOfficeContactsSql = "select distinct bankName from plat_bank_info where beLongPlatId = ? and platType = ? order by defaultFlag";
		List<String> platBankInfo = findBySql(deleteOfficeContactsSql, id,type);
		return platBankInfo;
	}

	/**
	 * 获取银行名称
	 * @author zhaohaiming
	 * @param id 渠道、地接社、供应商Id
	 * @param type  0批发商，1地接社，2渠道商
	 * @param bankName 
	 * @return list
	 * */
	@Override
	public List<String> getBankAccount(Long id, Integer type,String bankName) {
		String sql = "select distinct bankAccountCode from plat_bank_info where beLongPlatId = ? and platType = ? and bankName = ?";
		List<String> list = findBySql(sql,id,type,bankName);
		return list;
	}

	/**
	 * 根据银行名称和银行账号查询银行信息
	 * @param bankName			银行名称
	 * @param bankAccount		银行账号
     * @return
	 * @author	shijun.liu
	 * @date	2016.04.13
     */
	public List<PlatBankInfo> getPlantBankInfo(String bankName, String bankAccount){
		String sql = "SELECT bankName, bankAccountCode FROM plat_bank_info WHERE bankName =? AND bankAccountCode = ? ";
		List<PlatBankInfo> list = findBySql(sql, bankName, bankAccount);
		return list;
	}
	
	/**
	 * 根据渠道商ID和银行账号查询银行信息
	 * @param beLongPlatId		渠道商ID
	 * @param bankAccount		银行账号
	 * @return
	 * @author	jinxin.gao
	 * @date	2016.04.13
	 */
	public List<PlatBankInfo> getPlantBankInfoForAgintidAndBankAccount(Long beLongPlatId, String bankAccount){
		String sql = "SELECT bankName, bankAccountCode FROM plat_bank_info WHERE beLongPlatId =? AND bankAccountCode = ? ";
		List<PlatBankInfo> list = findBySql(sql, beLongPlatId, bankAccount);
		return list;
	}

	/*
	 * 获取银行账户名
	 * @param beLongPlatId 所属平台id   必填参数
	 * @param platType 所属平台类型  批发商0，地接社1，渠道商2   必填参数
	 * @param bankName 银行名称  非必填参数
	 * @param bankAccountCode 银行账号 非必填参数
	 * @return 银行账户名列表，排列顺序：先境内账户（先默认后非默认），后境外账户（先默认）
	 * @author xianglei.dong
	 */
	@Override
	public String getAccountName(Long beLongPlatId, Integer platType,
			String bankName, String bankAccountCode, String bankType) {
		String sql = "select accountName from plat_bank_info where beLongPlatId =";
		StringBuffer sb = new StringBuffer(sql);
		sb.append(beLongPlatId).append(" and platType=").append(platType);
		if(StringUtils.isNotBlank(bankName)) {
			sb.append(" and bankName='").append(bankName).append("'");
		}
		if(StringUtils.isNotBlank(bankAccountCode)) {
			sb.append(" and bankAccountCode='").append(bankAccountCode).append("'");
		}
		if(StringUtils.isNotBlank(bankType)) {
			sb.append(" and belongType=").append(Integer.valueOf(bankType));
			if(Integer.valueOf(bankType)==1) {	//境内
				sb.append(" and delFlag=0");
				sb.append(" order by defaultFlag limit 0,1");				
			}else {
				sb.append(" and delFlag=0");
				sb.append(" order by defaultFlag DESC limit 0,1");
			}
		}else{
			sb.append(" and delFlag=0");
			sb.append(" order by belongType,defaultFlag limit 0,1");
		}
		List<String> accountNames = findBySql(sb.toString());
		if(CollectionUtils.isNotEmpty(accountNames)) {
			String name = accountNames.get(0);
			if(StringUtils.isNotBlank(name)) {
				return name;
			}			
		}
		return "";
	}
}
