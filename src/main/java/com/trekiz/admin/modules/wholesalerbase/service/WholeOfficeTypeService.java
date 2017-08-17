package com.trekiz.admin.modules.wholesalerbase.service;

import java.util.List;

import com.trekiz.admin.modules.wholesalerbase.entity.WholeOfficeType;

/**
 * 为批发商增加分类
 * @author gao
 *  2015年4月13日
 */
public interface WholeOfficeTypeService {

	/**
	 * 根据公司ID获取公司包含的分类
	 * @author gao
	 * @param companyUUID
	 * @return
	 */
	public List<WholeOfficeType> findByCompanyID(String companyID);
	/**
	 * 根据UUID和批发商ID获取分类
	 * @author gao
	 * @param sysdefinedictUUID
	 * @return
	 */
	public WholeOfficeType findBySysdefinedictUUID(String sysdefinedictUUID,Long companyId);
	/**
	 * 保存批发商分类
	 * @author gao
	 * @param wholeOfficeType
	 * @return
	 */
	public WholeOfficeType save(WholeOfficeType wholeOfficeType);
}
