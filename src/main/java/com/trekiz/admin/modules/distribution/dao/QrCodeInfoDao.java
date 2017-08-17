package com.trekiz.admin.modules.distribution.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.distribution.entity.QrCodeInfo;

/**
 * QrCodeInfoDao.java<br>
 * 保存二维码信息
 * @author yang.wang
 * @date 2017.1.12
 * */
public interface QrCodeInfoDao extends QrCodeInfoDaoCustom,CrudRepository<QrCodeInfo, Long> {
	
	@Query(value = "from QrCodeInfo where uuid = ?1")
	public List<QrCodeInfo> findByUuid(String uuid);
}

interface QrCodeInfoDaoCustom extends BaseDao<QrCodeInfo> {
	
}

@Repository
class QrCodeInfoDaoImpl extends BaseDaoImpl<QrCodeInfo> implements QrCodeInfoDaoCustom {
	
}
