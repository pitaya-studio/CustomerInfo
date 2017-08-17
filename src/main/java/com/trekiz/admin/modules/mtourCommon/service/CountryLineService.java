/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourCommon.entity.CountryLine;
import com.trekiz.admin.modules.mtourCommon.input.CountryLineInput;
import com.trekiz.admin.modules.mtourCommon.query.CountryLineQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CountryLineService{
	
	public void save (CountryLine countryLine);
	
	public void save (CountryLineInput countryLineInput);
	
	public void update (CountryLine countryLine);
	
	public CountryLine getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CountryLine> find(Page<CountryLine> page, CountryLineQuery countryLineQuery);
	
	public List<CountryLine> find( CountryLineQuery countryLineQuery);
	
	public CountryLine getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
}
