package com.trekiz.admin.modules.eprice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceFileDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceFileService;
import com.trekiz.admin.modules.sys.entity.User;

@Service("estimatePriceFileService")
@Transactional(readOnly = true)
public class EstimatePriceFileServiceImpl implements EstimatePriceFileService {

	@Autowired
	private EstimatePriceFileDao estimatePriceFileDao;
	
	public void save(EstimatePriceFile epf) {
		estimatePriceFileDao.save(epf);
		
	}

	public EstimatePriceFile findById(Long id) {
		if(id==null){
			return null;
		}
		return estimatePriceFileDao.findById(id);
	}

	public void delById(Long id) {
		if(id==null){
			return ;
		}
		EstimatePriceFile epf = new EstimatePriceFile();
		epf.setId(id);
		epf.setStatus(EstimatePriceFile.STATUS_DEL);
		epf.setModifyTime(new Date());
		
		this.save(epf);
		
	}

	@Override
	public Map<String, Object> saveTemp(MultipartFile file,Long id,User user) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		String path;
		String fileName;
		if(file!=null){
			 fileName = file.getOriginalFilename();
			 if(StringUtils.isNotBlank(fileName)) {
				 	//保存文件 
	    	        try {
	    	            path = FileUtils.uploadFile(file.getInputStream(),fileName);
	    	        //保存附件表数据
	    	        } catch (Exception e) {  
	    	            e.printStackTrace();
	    	            map.put("res", "error");
	    	            return map;
	    	        }
			 }else{
				map.put("res", "error");
 	            return map;
			 }
	    }else{
			map.put("res", "error");
	            return map;
		 }
		
		EstimatePriceFile epf=null;
		if(id!=null){
			epf = this.findById(id);
		}
		if(epf==null){
			epf = new EstimatePriceFile();
			epf.setPtype(EstimatePriceFile.PTYPE_ADMIT);
			epf.setStatus(EstimatePriceFile.STATUS_DRAFT);
			epf.setType(EstimatePriceFile.TYPE_TRIP);
			epf.setCreateTime(new Date());
		}
		
		epf.setUserId(user.getId());
		epf.setUserName(user.getName());
		epf.setPath(path);
		epf.setFileName(fileName);
		
		int i = fileName.lastIndexOf(".");
		int l = fileName.length();
		if(i>-1 && i+1<l){
			String ext=fileName.substring(i+1,l);
			epf.setExt(ext);
		}
		
		
		epf.setCreateTime(new Date());
		epf.setModifyTime(new Date());
		
		this.save(epf);
		
		map.put("model", epf);
		return map;
	}

	@Override
	public EstimatePriceFile findByPId(Long pid) {
		// TODO Auto-generated method stub
		return null;
	}

}
