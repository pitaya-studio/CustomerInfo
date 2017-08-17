/**
 *
 */
package com.trekiz.admin.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
import com.trekiz.admin.modules.sys.repository.SysDefineDictDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author zj
 * @version 2013-11-19
 */
@Service
@Transactional(readOnly = true)
public class SysDefineDictService extends BaseService {
	
	@Autowired
	private SysDefineDictDao sysDefineDictDao;

	//旅游类型维护
	@Transactional(readOnly = false)
	public List<SysDefineDict> findType(String type) {
		Long companyId = StringUtils.toLong(UserUtils.getCompanyIdForData());
		return sysDefineDictDao.findSysDefineDictByTarvleType(type,companyId);
	}
	@Transactional(readOnly = false)
	public List<String> findLabel(String type,Long companyId) {
		return sysDefineDictDao.findLibelByTarvleType(type,companyId);
	}
	@Transactional(readOnly = false)
	public Object deleteType(Long id) {
		CacheUtils.remove(DictUtils.CACHE_DEFINE_DICT_MAP);
		return sysDefineDictDao.delById(id);
	}
	@Transactional(readOnly = false)
	public void save(SysDefineDict sysdefinedict) {
	    logger.error("value is null>>>>>"+sysdefinedict.getValue()+"<<<<<");
//	    System.out.println("value is null>>>>>"+sysdefinedict.getValue()+"<<<<<");
		CacheUtils.remove(DictUtils.CACHE_DEFINE_DICT_MAP);
		if(null==sysdefinedict.getValue()||"".equalsIgnoreCase(sysdefinedict.getValue().trim())) {
		    logger.error("value is null>>>>>"+sysdefinedict.getValue()+"<<<<<");
//		    System.out.println("value is null>>>>>"+sysdefinedict.getValue()+"<<<<<");
			int count = 0;
			do{
				Number value = sysDefineDictDao.findMaxValueByCompanyIdAndType(sysdefinedict.getCompanyId(), sysdefinedict.getType());
				if(value == null){
				    value = 1;
				}else {
				    value = value.intValue()+1;
				}
				sysdefinedict.setValue(String.valueOf(value));
				logger.error("value will not null>>>>>"+value+"<<<<<");
//				System.out.println("value will not null>>>>>"+value+"<<<<<");
				try {
					sysDefineDictDao.save(sysdefinedict);
					count = 3;
				} catch (Exception e) {
					logger.info("error:",e);
					count++;
				}
			}while(count<3);
			//最多只尝试分配3次编号，如果每次都重复，则退出
		}else{
		    logger.error("value not null>>>>>"+sysdefinedict.getValue()+"<<<<<");
//	        System.out.println("value not null>>>>>"+sysdefinedict.getValue()+"<<<<<");
			sysDefineDictDao.save(sysdefinedict);
		}
	}
	@Transactional(readOnly = false)
	public SysDefineDict findOne(Long id) {
		return sysDefineDictDao.findOne(id);
	}
	@Transactional(readOnly = false)
	public List<SysDefineDict> findIsExist(String type,Long id,String checked ,String val) {
		if ("label".equals(checked)){
			return sysDefineDictDao.findLabelIsExist(UserUtils.getUser().getCompany().getId(),type, id, val);
		}
		return sysDefineDictDao.findValueIsExist(UserUtils.getUser().getCompany().getId(),type, id, val);
	}
	
	public void update (SysDefineDict sysDefineDict){
		super.setOptInfo(sysDefineDict, OPERATION_UPDATE);
		sysDefineDictDao.updateObj(sysDefineDict);
	}
}
