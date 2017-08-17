package com.trekiz.admin.modules.visa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.visa.entity.VisaProductFile;
import com.trekiz.admin.modules.visa.repository.VisaProductFileDao;

@Service
@Transactional(readOnly = true)
public class VisaProductsFileService extends BaseService {

	@Autowired
	private VisaProductFileDao visaProductFileDao;
	
	@Autowired
	private DocInfoDao docInfoDao;
	
	/**
	 * 保存签证文件
	 * @author jiachen
	 * @DateTime 2014-12-4 上午11:51:21
	 * @return void
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveVisaProductFile(VisaProductFile visaProductFile) {
		visaProductFileDao.save(visaProductFile);
	}
	
	
	/**
	 * 获取签证产品文件,如果是用于打包下载，那么再传一个true
	 * @author jiachen
	 * @DateTime 2014-12-4 下午04:57:36
	 * @return List<Object>
	 */
	public List<Object> findFileListByProId(Long visaProdectsId, boolean isDownLoad) {
		//根据产品id获取文件中间表
		List<VisaProductFile> fileInfoList = new ArrayList<VisaProductFile>();
		//查询单个产品的
		if(visaProdectsId != null) {
			fileInfoList = visaProductFileDao.findFileListByProId(visaProdectsId);
		}
		
		List<Object> docInfoList = new ArrayList<Object>();
		
			//根据中间表获取文件列表
			if(!fileInfoList.isEmpty()) {
				StringBuffer docIds = new StringBuffer();
				for(VisaProductFile flieInfo : fileInfoList) {
					//如果是用户打包下载，则返回文件id
					if(isDownLoad) {
						docIds.append(flieInfo.getdocInfo().toString() + ",");
					//否则返回文件list
					}else{
						docInfoList.add(docInfoDao.findOne(flieInfo.getdocInfo()));
					}
				}
				if(docInfoList.isEmpty()) {
					docInfoList.add(docIds);
				}
			}
		return docInfoList;
	}
	
	/**
	 * 查找产品与文件的中间键
	 * @author jiachen
	 * @DateTime 2014-12-5 下午04:49:19
	 * @return VisaProductFile
	 */
	public VisaProductFile findFile(String proId, String docId) {
		if(StringUtils.isNotBlank(proId) && StringUtils.isNotBlank(docId)) {
			return visaProductFileDao.findBySrcVisaProductIdAndDocInfo(StringUtils.toLong(proId), StringUtils.toLong(docId));
		}else{
			return null;
		}
	}
}
