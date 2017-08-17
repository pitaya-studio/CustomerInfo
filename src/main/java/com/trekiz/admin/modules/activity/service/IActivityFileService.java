package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Set;

import com.trekiz.admin.modules.activity.entity.ActivityFile;

public interface IActivityFileService {

	public abstract void save(Set<ActivityFile> activityFiles);

	public abstract ActivityFile findByPidName(String fileName,
			Integer srcActivityId);

	public abstract List<ActivityFile> findFileListByPid(Integer srcActivityId);

	public abstract void delFileList(List<ActivityFile> fileList);

	public abstract ActivityFile findById(Long id);

	public abstract void delActivityFile(ActivityFile file);

	public abstract void delActivityFileById(Long id);

	public abstract void delActivityFileByIds(List<Long> ids);

}