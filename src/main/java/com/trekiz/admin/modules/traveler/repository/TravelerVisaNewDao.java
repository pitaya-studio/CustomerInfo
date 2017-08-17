package com.trekiz.admin.modules.traveler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.entity.TravelerVisaNew;


/**
 * 游客上传文件信息DAO接口
 * @author taoxiaoyang
 *
 */
public interface TravelerVisaNewDao extends TravelerVisaNewDaoCustom,CrudRepository<TravelerVisaNew, Long>{

	
	@Query("from TravelerVisaNew file where file.travelerId = ?1")
	public List<TravelerVisaNew> findVisaListByPid(Long srcTravelerId);

	/**
	 * 获取一个游客的自备签
	 * @param travelerId
	 * @return
     */
	@Query("from TravelerVisaNew visa where visa.travelerId = ?1 and visa.zbqType=1 and visa.delFlag =0")
	public List<TravelerVisaNew> findProvidedVisaByTravelerId(Long travelerId);

	@Query("from TravelerVisaNew visa where visa.travelerId = ?1 and visa.zbqType=1")
	public List<TravelerVisaNew> findByTravelerId(Long travelerId);
	
	@Query("from TravelerVisaNew where travelerId = ?1 and zbqType = 0 and manorId is not null")
	public List<TravelerVisaNew> findApplyVisaListByTravelerId(Long srcTravelerId);
	
	@Modifying
	@Query("delete from TravelerVisaNew where id = ?1")
	public void delTravelerVisaById(Long id);
	
	@Modifying
	@Query("delete from TravelerVisaNew where id in ?1")
	public void delTravelerVisaByIds(List<Long> ids);
	
	@Modifying
	@Query("delete TravelerVisaNew where travelerId = ?1")
	public void delTravelerVisaByTravelerId(Long travelerId);
	
}

/**
 * 自定义接口
 * @author taoxiaoyang
 *
 */
interface TravelerVisaNewDaoCustom extends BaseDao<TravelerVisaNew>{
	
}

/**
 * 自定义接口实现
 * @author taoxiaoyang
 *
 */
@Repository
class TravelerVisaNewDaoImpl extends BaseDaoImpl<TravelerVisaNew> implements TravelerVisaNewDaoCustom{
	
}