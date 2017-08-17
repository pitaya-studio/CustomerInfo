package com.trekiz.admin.modules.activity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityFile;


/**
 * 产品上传文件信息DAO接口
 * @author liangjingming
 *
 */
public interface ActivityFileDao extends ActivityFileDaoCustom,CrudRepository<ActivityFile, Long>{

	@Query("select file from ActivityFile file where file.fileName = ?1 and file.srcActivityId = ?2")
	public ActivityFile findByPidName(String fileName,Integer srcActivityId);
	
	@Query("from ActivityFile file where file.srcActivityId = ?1")
	public List<ActivityFile> findFileListByPid(Integer srcActivityId);
	
	public ActivityFile findBySrcActivityIdAndSrcDocId(Integer srcActivityId, Integer srcDocId);
	
	@Modifying
	@Query("delete from ActivityFile where id = ?1")
	public void delActivityFileById(Long id);
	
	@Modifying
	@Query("delete from ActivityFile where id in ?1")
	public void delActivityFileByIds(List<Long> ids);
	
}

/**
 * 自定义接口
 * @author liangjingming
 *
 */
interface ActivityFileDaoCustom extends BaseDao<ActivityFile>{
	
}

/**
 * 自定义接口实现
 * @author liangjingming
 *
 */
@Repository
class ActivityFileDaoImpl extends BaseDaoImpl<ActivityFile> implements ActivityFileDaoCustom{
	
}