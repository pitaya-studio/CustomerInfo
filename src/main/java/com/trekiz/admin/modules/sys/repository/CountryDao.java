/**
 *
 */
package com.trekiz.admin.modules.sys.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Country;


 /**
 *  文件名: CountryDao.java
 *  功能:   国家dao
 *      
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-16 上午11:02:27
 *  @version 1.0
 */
public interface CountryDao extends CountryDaoCustom, CrudRepository<Country, Long> {
	
	@Query("from Country where displayStatus = 1")
	public List<Country> getCountrys();
	
	@Query("from Country where id in(?1) and displayStatus = 1")
	public List<Country> getCountrys(String ids);
	
	@Query("from Country where countryName_cn = ?1")
	public Country getCountryByCountryName(String countryName);
	
	@Query("from Country where id = ?1")
	public Country getCountryById(Long id);
}

/**
 * DAO自定义接口
 * @author zj
 */
interface CountryDaoCustom extends BaseDao<Country> {

}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class CountryDaoImpl extends BaseDaoImpl<Country> implements CountryDaoCustom {

}
