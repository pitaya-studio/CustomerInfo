/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipAnnexDao  extends BaseDao<CruiseshipAnnex> {
	
	public CruiseshipAnnex getByUuid(String uuid);
	
	public List<CruiseshipAnnex> getByStockUuid(String stockUuid);
	
	public boolean batchDelete(String[] uuids);
	
	public void synDocInfo(String mainUuid ,int type,int wholesalerId, List<CruiseshipAnnex> csaList);
	
}
