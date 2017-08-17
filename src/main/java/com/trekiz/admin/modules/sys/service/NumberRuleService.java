package com.trekiz.admin.modules.sys.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.NumberRule;
import com.trekiz.admin.modules.sys.entity.RuleParameter;
import com.trekiz.admin.modules.sys.repository.NumberRuleDao;
import com.trekiz.admin.modules.sys.repository.RuleParameterDao;

@Service
@Transactional(readOnly = true)
public class NumberRuleService extends BaseService {

	@Autowired
	private NumberRuleDao numberRuleDao;
	@Autowired
	private RuleParameterDao ruleParameterDao;
	
	/**
	 * 查询参数规则列表
	 * @return
	 */
	public List<NumberRule> findAllNumberRule(){
		Iterable<NumberRule> able =  numberRuleDao.findAll();
		Iterator<NumberRule> it = able.iterator();
		
		List<NumberRule> list = new ArrayList<NumberRule>();
		while(it.hasNext()){
			list.add((NumberRule)it.next());
		}
		return list;
	}
	
	
	/**
	 * 查询一条编号规则
	 * @return
	 */
	public NumberRule findOne(Long id){
		return numberRuleDao.findOne(id);
	}
	
	/**
	 * 查询参数规则列表
	 * @return
	 */
	public List<RuleParameter> findAllParam(){
		Iterable<RuleParameter> able =  ruleParameterDao.findAll();
		Iterator<RuleParameter> it = able.iterator();
		
		List<RuleParameter> list = new ArrayList<RuleParameter>();
		while(it.hasNext()){
			list.add((RuleParameter)it.next());
		}
		return list;
	}
	
	
	
	/**
	 * 添加或者修改编号规则
	 * @param currency
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void saveOrupdate(NumberRule numberRule){
		numberRuleDao.save(numberRule);
	}
	
	/**
	 * 检查编号类型是否存在
	 * @param numberType
	 */
	public long checkIsExist(String numberType){
		return numberRuleDao.checkIsExist(numberType);
	}
		
	/**
	 * 根据标示名查询对应的编号规则
	 * @param markName
	 * @return
	 */
	public String queryNumberByMarkname(String markName){
		return numberRuleDao.queryNumberByMarkname(markName);
	}
	
}
