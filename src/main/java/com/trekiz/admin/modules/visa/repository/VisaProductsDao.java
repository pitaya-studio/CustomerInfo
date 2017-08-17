package com.trekiz.admin.modules.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.visa.entity.VisaProducts;

public interface VisaProductsDao extends VisaProductsDaoCustom,
		CrudRepository<VisaProducts, Long> {

	@Modifying
	@Query("update VisaProducts set productStatus = ?2 where id in ?1")
	public void batchOnOrOffVisaProducts(List<Long> ids, Integer product_status);
	
	@Modifying
	@Query("update VisaProducts set nowLevel=1,review = ?2 where id = ?1 ")
	public void submitReview(Long id, Integer review);
	
	@Modifying
	@Query("update  VisaProducts set review = ?2,nowLevel=?3 where id = ?1")
	public void updateReview(Long id, Integer review,Integer nowLevel);

	@Modifying
	@Query("update VisaProducts set delFlag = '" + VisaProducts.DEL_FLAG_DELETE
			+ "' where id in ?1")
	public void batchDelVisaProducts(List<Long> ids);

	@Query("from VisaProducts where sysCountryId = ?1 and proCompanyId = ?2 and delFlag = 0 and productStatus = 2")
	public List<VisaProducts> findVisaProductsByCountryId(Integer countryId, Long proCompanyId);

	@Query("from VisaProducts where sysCountryId = ?1 and collarZoning = ?2 and proCompanyId = ?3")
	public List<VisaProducts> findVisaProductsByCountryIdAndManor(int countryId, String manor, Long proCompanyId);
	
	/** 可能需要对于批发商做筛选 */
	@Query(value = "select id,countryName_cn from sys_country where id in (select country_id from visa_interview_notice_address where company_id = ?1)", nativeQuery = true)
	public List<Object[]> findCountryInfoList(Long companyId);

	/** 可能需要对于批发商做筛选 */
	@Query(value = "select value, label from sys_dict where type='from_area' and value in (select area from visa_interview_notice_address where country_id = ?1 and company_id = ?2)", nativeQuery = true)
	public List<Object[]> findVisaCountryArea(Integer countryId, Long companyId);
	
	@Query("from VisaProducts where productStatus = 2 and delFlag = " + VisaProducts.DEL_FLAG_NORMAL + " and createBy in ?1")
	public List<VisaProducts> findByUsers(List<User> userList);

	/**
	 * 同一个国家和领区只能发一种类型的签证产品
	 * 
	 * @author jiachen
	 * @DateTime 2014-12-5 上午10:50:04
	 * @return List<VisaProducts>
	 */
	@Query("from VisaProducts where sysCountryId = ?1 and collarZoning = ?2 and visaType = ?3 "
			+ "and delFlag = '" + VisaProducts.DEL_FLAG_NORMAL + "' and proCompanyId = ?4 "
			+ "and id <> ?5 and deptId = ?6")
	public List<VisaProducts> findMoreProduct(Integer sysCountry, String collarZoning, Integer visaType, 
			Long proCompanyId, Long productId, Long deptId);
	
	/**
	 * 根据 签证国家，签证类型，签证领区 
	 * 
	 * @param countryId
	 * @param visaType
	 * @param collarZoning
	 * @return
	 */
	@Query("from VisaProducts where delFlag=0  and productStatus=2 and  sysCountryId = ?1 and visaType = ?2 and collarZoning = ?3 and proCompanyId = ?4")
	public List<VisaProducts> findVisaProductsByCountryTypeCollarZonID(Integer countryId, Integer visaType, String collarZoning, Long proCompanyId);
	
	/**
	 * 根据 签证国家，签证领区
	 * @param countryId
	 * @param collarZoning
	 * @return
	 */
	@Query("from VisaProducts where sysCountryId = ?1 and collarZoning = ?2 and proCompanyId = ?3")
	public List<VisaProducts> findVisaProductsByCountryAndArea(Integer countryId, String area, Long companyId);
	
	/**
	 * 更新计算提成状态
	 * @author zhaohaiming
	 * */
	@Modifying
	@Query("update VisaProducts set iscommission = ?2 where id = ?1")
	public void updateIsCommission(Long id,Integer iscommission);

	@Query("from VisaProducts where proCompanyId = ?1")
	public List<VisaProducts> findByCompany(Long id);
}
