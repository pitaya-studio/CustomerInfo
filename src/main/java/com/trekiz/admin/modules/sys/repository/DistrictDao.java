/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.District;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DistrictDao extends DistrictDaoCustom, CrudRepository<District, Long> {

    @Query("from District where name = ?1 and delFlag='" + Context.DEL_FLAG_NORMAL + "' ")
    District getByName(String name);

    @Query("from District where id = ?1 and delFlag='" + Context.DEL_FLAG_NORMAL + "' ")
    District getById(Long id);

    @Query("from District where delFlag=" + Context.DEL_FLAG_NORMAL + " AND name=?1 AND id <> ?2")
    List<District> checkDistrictName(String name, Long id);

    @Modifying
    @Query("update District set delFlag = " + Context.DEL_FLAG_DELETE + " where id = ?1")
    void deleteDistrict(Long id);

//    @Query("from District where delFlag='" + Context.DEL_FLAG_NORMAL + "' ")
//    public List<District> getAllDistrict();

}

interface DistrictDaoCustom extends BaseDao<District> {

}

@Repository
class DistrictDaoImpl extends BaseDaoImpl<District> implements DistrictDaoCustom {

}
