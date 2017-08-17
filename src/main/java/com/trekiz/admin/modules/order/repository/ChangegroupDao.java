/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.Changegroup;


 /**
 *  文件名: ChangegroupDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:33
 *  @version 1.0
 */
public interface ChangegroupDao extends ChangegroupDaoCustom, CrudRepository<Changegroup, Long> {

}

 /**
 *  文件名: ChangegroupDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:25
 *  @version 1.0
 */
interface ChangegroupDaoCustom extends BaseDao<Changegroup> {

}

 /**
 *  文件名: ChangegroupDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:15
 *  @version 1.0
 */
@Repository
class ChangegroupDaoImpl extends BaseDaoImpl<Changegroup> implements ChangegroupDaoCustom {

}
