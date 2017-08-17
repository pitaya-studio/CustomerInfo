package com.trekiz.admin.modules.sys.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.UserDefineDict;
import com.trekiz.admin.modules.sys.repository.AreaDao;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.repository.UserDefineDictDao;

/**
 * 渠道区域Service
 * @author jiachen
 * @version 2014-3-21
 */
@Service
@Transactional(readOnly = true)
public class UserDefineDictService extends BaseService {
	
	@Autowired
	private UserDefineDictDao userDefineDictDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private DictDao dictDao;

	public UserDefineDict get(Long id) {
		return userDefineDictDao.findOne(id);
	}
	public List<Area> findAll() {
		return areaDao.findAllList();
	}
	@Transactional(readOnly = false)
	public List<UserDefineDict> findDefineDict(Long companyId,String type) {
		
		return userDefineDictDao.findByCompanyIdAndType(companyId,type);
	}
	@Transactional(readOnly = false)
	public void saveAreaDefineDict(UserDefineDict areaDefineDict) {
		userDefineDictDao.save(areaDefineDict);
	}
	//分页查询列表(做勾选操作)
	@Transactional(readOnly = false)
	public List<Dict> findPagind(Long companyId,String type) {
        List<Dict> dictList = new ArrayList<Dict>();
        if("flight".equals(type)){
            dictList = dictDao.findByType(Context.TRAFFIC_NAME);
        }else if("fromarea".equals(type)){
            dictList = dictDao.findByType(Context.FROM_AREA);
        }else if("outarea".equals(type)){
            dictList = dictDao.findByType(Context.OUT_AREA);
        }

		List<UserDefineDict> defineDictList = userDefineDictDao.findByCompanyIdAndType(companyId,type);
		Set<Long> set = new HashSet<Long>();
		if(defineDictList.size()!=0) {
			for(UserDefineDict defineDict:defineDictList) {
				set.add(defineDict.getDictId());
			}
			for(Dict dict:dictList) {
				if(set.contains(dict.getId())) {
					dict.setSelected("1");
				}
			}
		}
		return dictList;
	}
//	//分页查询(目前只能满足两个需求flight和fromarea)
//	public Page<Dict> findDictPage(Page<Dict> page,String type) {
//		String sql = "from Dict where type='"+("flight".equals(type)?Context.TRAFFIC_NAME:Context.FROM_AREA)+"' and delFlag='" + Dict.DEL_FLAG_NORMAL + "' ORDER BY sort";
//		return dictDao.find(page,sql);
//	}
	
	//分页查询(目前只能满足两个需求flight和fromarea)
	public Page<Dict> searchDictPage(Page<Dict> page,String type,String term) {
		String sql = null;
		String dictType = null;
		
		if("flight".equals(type)) {
            dictType = "traffic_name";
        }else if("fromarea".equals(type)){
            dictType = "from_area";
        }else if("outarea".equals(type)){
            dictType = "out_area";
        }
		
		if(term == null)
			 sql = "from Dict where type='"+dictType+"' and delFlag='" + Dict.DEL_FLAG_NORMAL + "' ORDER BY sort";
		else
			 sql = "from Dict where type='"+dictType+"' and label like '"+"%"+term+"%"+"' and delFlag='" + Dict.DEL_FLAG_NORMAL + "' ORDER BY sort";
		
		return dictDao.find(page,sql);
	}
	//搜索
	@Transactional(readOnly = false)
	public List<Area> areaSearch(String term) {
		List<Area> searchList = areaDao.findAreaLike("%"+term+"%");
		List<Area> returnList = new ArrayList<Area>();
		Set<Long> searchChildIdSet = new HashSet<Long>();
		List<Long> searchChildIdList = new ArrayList<Long>();
		if(searchList!=null&&searchList.size()!=0){
			for(Area area:searchList) {
				String parentIds = area.getParentIds();
				String[] parentIdsArr = parentIds.split(",");
				if(parentIdsArr.length>2) {
					searchChildIdSet.add(area.getId());
					Long parentId = area.getParent().getId();
					if(parentIdsArr.length == 4 || parentIdsArr.length == 5)
						searchChildIdSet.add(parentId);
					searchChildIdSet.addAll(areaDao.findAnyChild(area.getId()));
//				}else if(parentIdsArr.length == 5) {
//					searchChildIdSet.add(area.getId());
//					searchChildIdSet.add(area.getParent().getId());
				}else if(parentIdsArr.length <= 2) {
					break;
				}
			}
			searchChildIdList.addAll(searchChildIdSet);
			if(searchChildIdList!=null&&searchChildIdList.size()!=0)
				returnList = areaDao.findByFilter(searchChildIdList);
		}
		return returnList;
	}
	
//	@Transactional(readOnly = false)
//	public List<Dict> dictSearch(String term,String type) {
//		return dictDao.findDictLike("%"+term+"%",type);
//	}
	//修改
	@Transactional(readOnly = false)
	public void update(String[] chickIdArr,String[] cancelIdArr,String type,Long companyId){
			for(int i = 0; i<chickIdArr.length; i++) {
					UserDefineDict areaDefineDict = new UserDefineDict();
					areaDefineDict.setDictId(StringUtils.toLong(chickIdArr[i]));
					areaDefineDict.setCompanyId(companyId);
					areaDefineDict.setType(type);
					userDefineDictDao.save(areaDefineDict);
			}
			for(int i = 0; i<cancelIdArr.length; i++) {
				userDefineDictDao.deleleByDictId(companyId, type, StringUtils.toLong(cancelIdArr[i]));
		}
	}
}
