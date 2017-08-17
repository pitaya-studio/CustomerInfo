package com.trekiz.admin.modules.traveler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;


/**
 * 游客上传文件信息DAO接口
 * @author taoxiaoyang
 *
 */
public interface TravelerVisaDao extends TravelerVisaDaoCustom,CrudRepository<TravelerVisa, Long>{

	
	@Query("from TravelerVisa file where file.travelerId = ?1")
	public List<TravelerVisa> findVisaListByPid(Long srcTravelerId);

	/**
	 * 获取一个游客的自备签
	 * @param travelerId
	 * @return
     */
	@Query("from TravelerVisa visa where visa.travelerId = ?1 and visa.zbqType=1 and visa.delFlag =0")
	public List<TravelerVisa> findProvidedVisaByTravelerId(Long travelerId);

	@Query("from TravelerVisa visa where visa.travelerId = ?1 and visa.zbqType=1")
	public List<TravelerVisa> findByTravelerId(Long travelerId);
	
	@Query("from TravelerVisa where travelerId = ?1 and zbqType = 0 and manorId is not null")
	public List<TravelerVisa> findApplyVisaListByTravelerId(Long srcTravelerId);
	
	@Modifying
	@Query("delete from TravelerVisa where id = ?1")
	public void delTravelerVisaById(Long id);
	
	@Modifying
	@Query("delete from TravelerVisa where id in ?1")
	public void delTravelerVisaByIds(List<Long> ids);
	
	@Modifying
	@Query("delete TravelerVisa where travelerId = ?1")
	public void delTravelerVisaByTravelerId(Long travelerId);
	
}

/**
 * 自定义接口
 * @author taoxiaoyang
 *
 */
interface TravelerVisaDaoCustom extends BaseDao<TravelerVisa>{
	
}

/**
 * 自定义接口实现
 * @author taoxiaoyang
 *
 */
@Repository
class TravelerVisaDaoImpl extends BaseDaoImpl<TravelerVisa> implements TravelerVisaDaoCustom{
	
}