/**
 *
 */
package com.trekiz.admin.modules.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.Visabasics;


 /**
 *  文件名: VisaBasicsDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-10 下午6:32:33
 *  @version 1.0
 */
public interface VisaBasicsDao extends VisaBasicsDaoCustom, CrudRepository<Visabasics, Long> {

	@Modifying
    @Query("update Visabasics set delFlag='" + Visabasics.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
    @Query("from Visabasics where delFlag='"  + Visabasics.DEL_FLAG_NORMAL +" '")
    public List<Visabasics> findAllList();
    
    @Query("from Visabasics where delFlag='"  + Visabasics.DEL_FLAG_NORMAL +" ' and visaCountry =?1")
    public List<Visabasics> findByVisaCountry(int countryId);
	
}


 /**
 *  文件名: VisaBasicsDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-10 下午6:32:25
 *  @version 1.0
 */
interface VisaBasicsDaoCustom extends BaseDao<Visabasics> {

}

 /**
 *  文件名: VisaBasicsDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-10 下午6:32:15
 *  @version 1.0
 */
@Repository
class VisaBasicsDaoImpl extends BaseDaoImpl<Visabasics> implements VisaBasicsDaoCustom {

}
