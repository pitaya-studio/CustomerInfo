/**
 *
 */
package com.trekiz.admin.modules.visa.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.visa.entity.Activityvisafile;


 /**
 *  文件名: ActivityvisafileDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:33
 *  @version 1.0
 */
public interface ActivityvisafileDao extends ActivityvisafileDaoCustom, CrudRepository<Activityvisafile, Long> {

	@Modifying
    @Query("update Activityvisafile set delFlag='" + Activityvisafile.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
    
     /**
     *  功能:
     *              获取所有签证最新的未删除的信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:21:42
     *  @return
     */
    @Query("from Activityvisafile t1 where t1.delFlag='"
            + Activityvisafile.DEL_FLAG_NORMAL
            + "' and updateDate = (select max(updateDate) from  Activityvisafile t2 where t1.countryId = t2.countryId and t1.visaType=t2.visaType and t2.delFlag='"
            +Activityvisafile.DEL_FLAG_NORMAL+"' )")
    public List<Activityvisafile> findAllList();
    
    
     /**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     */
    @Query("from Activityvisafile t1 where t1.delFlag='"
            + Activityvisafile.DEL_FLAG_NORMAL
            + "' and updateDate = (select max(updateDate) from  Activityvisafile t2 where " 
            + " t1.countryId in(?1) and t1.countryId = t2.countryId and t1.visaType=t2.visaType and t2.delFlag='"
            +Activityvisafile.DEL_FLAG_NORMAL+"' )")
    public List<Activityvisafile> findByVisaFileByCountrys(List<Long> ids);
    
    
    /**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     */
    @Query("from Activityvisafile t1 where t1.delFlag='"
            + Activityvisafile.DEL_FLAG_NORMAL
            + "' and updateDate = (select max(updateDate) from  Activityvisafile t2 where " 
            + " t1.countryId = ?1 and t1.countryId = t2.countryId and t1.visaType=t2.visaType and t2.delFlag='"
            +Activityvisafile.DEL_FLAG_NORMAL+"' )")
    public List<Activityvisafile> findByVisaFileByCountry(Long countryId);
    
    /**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     */
    @Query("from DocInfo t1 where t1.delFlag='"
            + Activityvisafile.DEL_FLAG_NORMAL
            + "' and t1.id = ?1 )")
    public List<DocInfo> findByDocInfoByDocInfoId(Long docInfoId);
    
    
    /**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     */
    @Query("from Activityvisafile t1 where t1.delFlag='"
            + Activityvisafile.DEL_FLAG_NORMAL
            + " ' and updateDate = (select max(updateDate) from  Activityvisafile t2 where "
            + " t1.countryId = ?1 and t1.countryId = t2.countryId and t1.visaType=t2.visaType and t1.visaType=?2 and t2.delFlag='"
            +Activityvisafile.DEL_FLAG_NORMAL+"' )")
    public List<Activityvisafile> findByVisaFileByCountryAndType(Long countryId,int visaType);
    
    /**
     * 查询产品的签证信息
     * @param proId
     * @return
     */
    @Query("from Activityvisafile t where t.delFlag='"+Activityvisafile.DEL_FLAG_NORMAL+"' and t.srcActivityId = ?1")
	public List<Activityvisafile> findVisaFileByProid(Long proId);
    
    @Modifying
    @Query("update Activityvisafile set delFlag='"+Activityvisafile.DEL_FLAG_DELETE+"' where srcActivityId = ?1")
    public void delVisaFileByProid(Long proId);
    
    @Modifying
    @Query("update Activityvisafile set delFlag='"+Activityvisafile.DEL_FLAG_DELETE+"' where id in ?1")
    public void delVisaFileByIds(Set<Long> ids);
    
    
    //@Query("select t1.srcActivityId,t1.visaType,t1.countryName,t2.countryId from Activityvisafile t1,DocInfo t2 where t1.srcDocId = t2.id and t1.delFlag='"+Activityvisafile.DEL_FLAG_NORMAL+"' " +
    @Query("select t1.srcActivityId,t1.visaType,t1.countryName,t1.countryId from Activityvisafile t1,DocInfo t2 where t1.srcDocId = t2.id and t1.delFlag='"+Activityvisafile.DEL_FLAG_NORMAL+"' " +
    		"and t1.srcActivityId in ?1 GROUP BY t1.countryId")
    public List<Object[]> findVisas(List<Long> proIds);
    
    @Query("from DocInfo where id in (select srcDocId from Activityvisafile where"
    		+ " srcActivityId = ?1 and visaType = ?2 and countryId = ?3 and delFlag='" + Activityvisafile.DEL_FLAG_NORMAL + "')")
    public List<DocInfo> findFileList(Long activityId, Integer visaType, Long countryId);
}


 /**
 *  文件名: ActivityvisafileDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:25
 *  @version 1.0
 */
interface ActivityvisafileDaoCustom extends BaseDao<Activityvisafile> {

}

 /**
 *  文件名: ActivityvisafileDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:15
 *  @version 1.0
 */
@Repository
class ActivityvisafileDaoImpl extends BaseDaoImpl<Activityvisafile> implements ActivityvisafileDaoCustom {

}
