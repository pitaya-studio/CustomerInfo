package com.trekiz.admin.modules.visa.repository;

import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.visa.entity.Visafile;

public interface VisafileDao extends VisafileDaoCustom, CrudRepository<Visafile, Long>{
	/*@Modifying
    @Query("update Visafile set delFlag='" + Visafile.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
    
     *//**
     *  功能:
     *              获取所有签证最新的未删除的信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:21:42
     *  @return
     *//*
    @Query("from Visafile t1 where t1.delFlag='"
            + Visafile.DEL_FLAG_NORMAL
            + "' and updateDate = (select max(updateDate) from  Visafile t2 where t1.countryId = t2.countryId and t1.visaType=t2.visaType and t2.delFlag='"
            +Visafile.DEL_FLAG_NORMAL+"' )")
    public List<Visafile> findAllList();
    
    
     *//**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     *//*
  
    @Query("from Visafile t1 where t1.delFlag='"
            + Visafile.DEL_FLAG_NORMAL
            + "' and updateDate = (select max(updateDate) from  Visafile t2 where " 
            + " t1.countryId in(?1) and t1.countryId = t2.countryId and t1.visaType=t2.visaType and t2.delFlag='"
            +Visafile.DEL_FLAG_NORMAL+"' )")
    public List<Visafile> findByVisaFileByCountrys(List<Long> ids);
    
    
    *//**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     *//*
    
    @Query("from Visafile t1 where t1.delFlag='"
            + Visafile.DEL_FLAG_NORMAL
            + "' and updateDate = (select max(updateDate) from  Visafile t2 where " 
            + " t1.countryId = ?1 and t1.countryId = t2.countryId and t1.visaType=t2.visaType and t2.delFlag='"
            +Visafile.DEL_FLAG_NORMAL+"' )")
    public List<Visafile> findByVisaFileByCountry(Long countryId);
    
    *//**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     *//*
   
    @Query("from DocInfo t1 where t1.delFlag='"
            + Visafile.DEL_FLAG_NORMAL
            + "' and t1.id = ?1 )")
    public List<DocInfo> findByDocInfoByDocInfoId(Long docInfoId);
    
    
    *//**
     *  功能:
     *          根据国家找到最新的签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 上午11:31:25
     *  @param countryId
     *  @return
     *//*
    @Query("from Visafile t1 where t1.delFlag='"
            + Visafile.DEL_FLAG_NORMAL
            + " ' and updateDate = (select max(updateDate) from  Visafile t2 where "
            + " t1.countryId = ?1 and t1.countryId = t2.countryId and t1.visaType=t2.visaType and t1.visaType=?2 and t2.delFlag='"
            +Activityvisafile.DEL_FLAG_NORMAL+"' )")
    public List<Activityvisafile> findByVisaFileByCountryAndType(Long countryId,int visaType);
    
    *//**
     * 查询产品的签证信息
     * @param proId
     * @return
     *//*
    @Query("from Visafile t where t.delFlag='"+Visafile.DEL_FLAG_NORMAL+"' and t.visaProductssId = ?1")
	public List<Visafile> findVisaFileByProid(Long proId);
    
    */
}
