/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipAnnexInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipAnnexQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipAnnexService{
	/**
	 * 保存接口
	 * @param cruiseshipAnnex
	 * @throws BaseException4Quauq
	 * @author zhangchao
	 * 2016-2-2
	 */
	public void save (CruiseshipAnnex cruiseshipAnnex) throws BaseException4Quauq ;
	
	public void save (CruiseshipAnnexInput cruiseshipAnnexInput);
	
	public void update (CruiseshipAnnex cruiseshipAnnex);
	
	public CruiseshipAnnex getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipAnnex> find(Page<CruiseshipAnnex> page, CruiseshipAnnexQuery cruiseshipAnnexQuery);
	
	public List<CruiseshipAnnex> find( CruiseshipAnnexQuery cruiseshipAnnexQuery);
	
	public CruiseshipAnnex getByUuid(String uuid);
	
	public List<CruiseshipAnnex>  getByStockUuid(String stockUuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据mainuuid获得CruiseshipAnnex type：1游轮 2游轮库存
	 * @param Uuid
	 * @return
	 */
	public List<CruiseshipAnnex> getCruiseshipAnnexByMainUuid(String uuid,Integer type) throws BaseException4Quauq;
	
	public List<CruiseshipAnnex> getFileList(HttpServletRequest request);
}
