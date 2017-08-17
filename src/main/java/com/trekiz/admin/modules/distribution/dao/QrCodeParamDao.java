package com.trekiz.admin.modules.distribution.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.distribution.entity.QrCodeParam;

/**
 * QrCodeParamDao.java<br>
 * 保存二维码参数信息
 * @author yang.wang
 * @date 2017.1.12
 * */
public interface QrCodeParamDao extends QrCodeParamDaoCustom,CrudRepository<QrCodeParam, Long> {

	@Query(value = "from QrCodeParam where qrCodeUuid = ?1")
	public List<QrCodeParam> findByUuid(String uuid);
}

interface QrCodeParamDaoCustom extends BaseDao<QrCodeParam> {
	
}

@Repository
class QrCodeParamDaoImpl extends BaseDaoImpl<QrCodeParam> implements QrCodeParamDaoCustom {
	
}
