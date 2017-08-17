package com.trekiz.admin.modules.sys.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Remind;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("JpaQlInspection")
public interface RemindDao extends RemindDaoCustom, CrudRepository<Remind, Integer> {
	
	@Query("from Remind where delFlag = ?1")
    List<Remind> findByDelFlagEquals(String delFlag);

	@Query("from Remind where remindType = ?1 and delFlag = ?2")
    List<Remind> findByRemindTypeAndDelFlagEquals(Integer remindType, String delFlag);

    @Modifying
    @Query("update Remind r set r.delFlag = '1' where r.id = ?1")
    int setDelFlagForRemind(Integer id);
    
    @Query("from Remind r where r.companyUuid = ?1 and r.remindType = ?2 and r.delFlag = 0")
    List<Remind> findByCompanyAndType(String companyUuid, Integer remindType);
    
    /**
	 * 获取remind的可见用户
	 * @param remind_id
	 * @return
	 */
    @Query(value="select user_id from sys_remind_user where remind_id = ?1", nativeQuery=true)
    List<Integer> findAllUserByRemind(Integer remind_id);
}

interface RemindDaoCustom extends BaseDao<Remind> {
	
	/**
	 * 获取remind的可见用户
	 * @param id
	 * @return
	 */
//	List<Integer> findAllUserByRemind(Integer id);
}

@Repository
class RemindDaoImpl extends BaseDaoImpl<Remind> implements RemindDaoCustom {

}