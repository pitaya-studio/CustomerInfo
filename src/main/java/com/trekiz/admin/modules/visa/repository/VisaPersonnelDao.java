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
import com.trekiz.admin.modules.visa.entity.Visapersonneltype;


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
public interface VisaPersonnelDao extends VisaPersonnelDaoCustom, CrudRepository<Visapersonneltype, Long> {

	@Modifying
    @Query("update Visapersonneltype set delFlag='" + Visapersonneltype.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
    @Query("from Visapersonneltype where delFlag='"  + Visapersonneltype.DEL_FLAG_DELETE +" '")
    public List<Visapersonneltype> findAllList();
    
    @Query("from Visapersonneltype where delFlag='"  + Visapersonneltype.DEL_FLAG_DELETE +" ' and personnelType =?1 and visabasicsId=?2")
    public List<Visapersonneltype> findByTypeAndBasicId(int personnelType,int visaBasicsId);
    
    @Query("from Visapersonneltype where delFlag='"  + Visapersonneltype.DEL_FLAG_DELETE +" ' and visabasicsId=?1")
    public List<Visapersonneltype> findByBasicId(long visaBasicsId);
	
}


 /**
 *  文件名: VisapersonneltypeDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-10 下午6:32:25
 *  @version 1.0
 */
interface VisaPersonnelDaoCustom extends BaseDao<Visapersonneltype> {

}

 /**
 *  文件名: VisapersonneltypeDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-10 下午6:32:15
 *  @version 1.0
 */
@Repository
class VisaPersonnelDaoImpl extends BaseDaoImpl<Visapersonneltype> implements VisaPersonnelDaoCustom {

}
