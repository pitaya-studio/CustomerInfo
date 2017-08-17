/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipInfoInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipInfoQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipInfoService{
	
	public void save (CruiseshipInfo cruiseshipInfo);
	
	public CruiseshipInfo save (CruiseshipInfoInput cruiseshipInfoInput) throws BaseException4Quauq;
	
	public void update (CruiseshipInfo cruiseshipInfo)throws BaseException4Quauq;
	
	public CruiseshipInfo getById(java.lang.Integer value) throws BaseException4Quauq;
	
	public void removeById(java.lang.Integer value) throws BaseException4Quauq;
	
	public Page<CruiseshipInfo> find(Page<CruiseshipInfo> page, CruiseshipInfoQuery cruiseshipInfoQuery,String orderBy) throws BaseException4Quauq;
	
	public List<CruiseshipInfo> find( CruiseshipInfoQuery cruiseshipInfoQuery);
	
	public CruiseshipInfo getByUuid(String uuid) throws BaseException4Quauq;
	
	public void removeByUuid(String uuid)  throws BaseException4Quauq;
	
	public boolean batchDelete(String[] uuids) throws BaseException4Quauq;
	
	/**
	 * 验证游轮是否被关联接口
	 * @param 游轮的id
	 * @author zhangchao
	 * 2016/2/2
	 */
	public boolean updateCheck(String uuid)  throws BaseException4Quauq;
	/**
	 * 同过游轮uuid查询舱型
	 * @param uuid
	 * @return
	 * @author zhangchao
	 */
	public List<CruiseshipCabin>  getCruiseshipCabinBycruiseshipInfoUuid(String uuid) throws BaseException4Quauq;

	public List<CruiseshipInfo> getCruiseshipInfoByName(String name);

	
	/**
	 * 根据批发商id获取该批发商下所有的游轮基础信息
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<CruiseshipInfo>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-23
	 */
	public List<CruiseshipInfo> findByWholesalerId(Long wholesalerId);

}
