/**
 *
 */
package com.trekiz.admin.modules.sys.service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典Service
 * @author zj
 * @version 2013-11-19
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService {

	@Autowired
	private DictDao dictDao;
	
	public Dict get(Long id) {
		return dictDao.findOne(id);
	}
	
	public Page<Dict> find(Page<Dict> page, Dict dict) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(dict.getType())){
			dc.add(Restrictions.eq("type", dict.getType()));
		}
		if (StringUtils.isNotEmpty(dict.getDescription())){
			dc.add(Restrictions.like("description", "%"+dict.getDescription()+"%"));
		}
		dc.add(Restrictions.eq(Dict.DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
		return dictDao.find(page, dc);
	}
	
	public List<String> findTypeList(){
		return dictDao.findTypeList();
	}
	
	public List<Dict> findByType(String type){
		return dictDao.findByType(type);
	}
	
	public List<Dict> findByType(String type,List<String> values){
		return dictDao.findByType(type,values);
	}
	
	public List<Dict> findByIdList(List<Long> ids) {
		return dictDao.findByIdlist(ids);
	}
	
	@Transactional(readOnly = false)
	public void save(Dict dict) {
		dictDao.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		dictDao.deleteById(id);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	public Dict findByValueAndType(String value, String type){
		return dictDao.findByValueAndType(value, type);
	}

	public boolean isExist(Long id, String name, String type) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		dc.add(Restrictions.eq("value", name));
		dc.add(Restrictions.eq("type", type));
		dc.add(Restrictions.eq("delFlag", "0"));
		List<Dict> dicts = dictDao.find(dc);
		if (dicts != null && dicts.size() > 0) {
			Dict dict = dicts.get(0);
			if (dict.getId().equals(id)) {
				return false;
			}
			return true;
		}
		return false;
	}
}
