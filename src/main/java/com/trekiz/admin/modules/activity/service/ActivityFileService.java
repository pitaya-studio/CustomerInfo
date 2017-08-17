package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.repository.ActivityFileDao;

/**
 * 产品上传文件信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class ActivityFileService extends BaseService implements IActivityFileService {

	@Autowired
	private ActivityFileDao activityFileDao;
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#save(java.util.Set)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void save(Set<ActivityFile> activityFiles){
		activityFileDao.save(activityFiles);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#findByPidName(java.lang.String, java.lang.Integer)
	 */
	@Override
	public ActivityFile findByPidName(String fileName,Integer srcActivityId){
		return activityFileDao.findByPidName(fileName, srcActivityId);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#findFileListByPid(java.lang.Integer)
	 */
	@Override
	public List<ActivityFile> findFileListByPid(Integer srcActivityId){
		return activityFileDao.findFileListByPid(srcActivityId);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#delFileList(java.util.List)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delFileList(List<ActivityFile> fileList){
		activityFileDao.delete(fileList);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#findById(java.lang.Long)
	 */
	@Override
	public ActivityFile findById(Long id){
		return activityFileDao.findOne(id);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#delActivityFile(com.trekiz.admin.modules.activity.entity.ActivityFile)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivityFile(ActivityFile file){
		activityFileDao.delete(file);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#delActivityFileById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivityFileById(Long id){
		activityFileDao.delActivityFileById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityFileService#delActivityFileByIds(java.util.List)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivityFileByIds(List<Long> ids){
		activityFileDao.delActivityFileByIds(ids);
	}
}
