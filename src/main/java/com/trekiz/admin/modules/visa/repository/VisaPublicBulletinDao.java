package com.trekiz.admin.modules.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;

public interface VisaPublicBulletinDao  extends  VisaPublicBulletinDaoCustom, CrudRepository<VisaPublicBulletin, Long>{

	@Modifying
	@Query("update VisaPublicBulletin set delFlag = '"+BaseEntity.DEL_FLAG_DELETE+"' where id in ?1")
	public void batchDelVisaProducts(List<Long> ids);
}
interface VisaPublicBulletinDaoCustom extends BaseDao<VisaPublicBulletin>{}

@Repository
class VisaPublicBulletinDaoImpl extends BaseDaoImpl<VisaPublicBulletin> implements VisaPublicBulletinDaoCustom {
	
}


