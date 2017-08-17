/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;

/**
 * 自定义字典DAO接口
 * @author liangjingming
 * @version 2014-02-19
 */
public interface SysDefineDictDao extends SysDefineDictDaoCustom, CrudRepository<SysDefineDict, Long> {

	@Query("from SysDefineDict where companyId=?1 and delFlag='" + Dict.DEL_FLAG_NORMAL + "' order by sort")
	public List<SysDefineDict> findAllList(Long companyId);
	
	@Query("from SysDefineDict where companyId=?2 and type = ?1 and delFlag='" + Menu.DEL_FLAG_NORMAL + "' ORDER BY sort")
	public List<SysDefineDict> findSysDefineDictByTarvleType(String type,Long companyId);
	
	@Query("from SysDefineDict where companyId =?1 AND type=?2 order by sort")
	public List<SysDefineDict> findByCompanyIdAndType(Long companyId,String type);
	
	@Query("select label from SysDefineDict where companyId=?2 and type = ?1 and delFlag='" + Menu.DEL_FLAG_NORMAL + "'")
	public List<String> findLibelByTarvleType(String type,Long companyId);

	@Modifying
	@Query("update SysDefineDict set delFlag='" + Menu.DEL_FLAG_DELETE + "' where id = ?1")
	public int delById(Long travelTypeId);
	
	@Query("from SysDefineDict s where s.companyId =?1 AND s.type=?2 AND s.id!= ?3 AND s.value = ?4 ")
	public List<SysDefineDict> findValueIsExist(Long companyId, String type, Long id,String val);
	
	@Query("from SysDefineDict s where s.companyId =?1 AND s.type=?2 AND s.value = ?3 and delFlag= '" + Menu.DEL_FLAG_NORMAL + "'")
	public List<SysDefineDict> findValueIsExist(Long companyId, String type,String val);
	
	@Query("from SysDefineDict s where s.companyId =?1 AND s.type=?2 AND s.id!= ?3 AND s.label = ?4 AND s.delFlag='" + Menu.DEL_FLAG_NORMAL + "'")
	public List<SysDefineDict> findLabelIsExist(Long companyId, String type, Long id,String label);
	
	@Query(value="select max(CONVERT(value,UNSIGNED)) from sysdefinedict s where s.companyId =?1 AND s.type=?2",nativeQuery=true)
	public Number findMaxValueByCompanyIdAndType(Long companyId,String type);
	
	@Query("from SysDefineDict where defaultFlag=1 and companyId = ?1 and delFlag='" + Menu.DEL_FLAG_NORMAL + "'")
	public List<SysDefineDict> findRelevanceFlag(Long companyId);
}

/**
 * DAO自定义接口
 * @author zj
 */
interface SysDefineDictDaoCustom extends BaseDao<SysDefineDict> {

}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class SysDefineDictDaoImpl extends BaseDaoImpl<SysDefineDict> implements SysDefineDictDaoCustom {

}
