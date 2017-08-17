/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipCabinDao  extends BaseDao<CruiseshipCabin> {
	
	public CruiseshipCabin getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
}
