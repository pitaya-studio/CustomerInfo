package com.trekiz.admin.modules.wholesalerbase.service;

import java.util.List;

import com.trekiz.admin.modules.wholesalerbase.entity.WholeArea;

/**
* 批发商覆盖地区
 * @author gao
 *  2015年4月22日
 */
public interface WholeAreaService {

	/**
	 * 保存地区
	 * @author gao
	 * @param area
	 */
	public void save(WholeArea area);
	/**
	 * 按批发商ID查询覆盖地区
	 * @author gao
	 * @param companyID
	 * @return
	 */
	public List<WholeArea> findWholeAreaList(Long companyID);
	/**
	 * 根据地区UUID和批发商ID获取指定地区
	 * @author gao
	 * @param UUID
	 * @param companyID
	 * @return
	 */
	public WholeArea findWholeAreaOne(String id,Long companyID);
}
