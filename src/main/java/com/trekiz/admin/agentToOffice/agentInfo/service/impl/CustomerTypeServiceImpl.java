package com.trekiz.admin.agentToOffice.agentInfo.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.agentInfo.dao.CustomerTypeDao;
import com.trekiz.admin.agentToOffice.agentInfo.service.CustomerTypeService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.agent.entity.CustomerType;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 客户类型维护服务层
 * @author wangyang
 * @date 2016-08-09
 * */
@Service
@Transactional
public class CustomerTypeServiceImpl implements CustomerTypeService{

	@Autowired
	private CustomerTypeDao customerTypeDao;
	
	@Override
	public Page<Map<String, Object>> getCustomerTypeList(Page<Map<String, Object>> page) {
				
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.id, c.`name`, c.uuid, ")
		   .append(" ( SELECT u.`name` FROM sys_user u WHERE u.id = c.create_by ) AS createBy, ")
		   .append(" c.create_date createDate, c.remark ")
		   .append(" FROM sys_customer_type c ")
		   .append(" WHERE c.del_flag = '").append(CustomerType.DEL_FLAG_NORMAL).append("'");
		
		return customerTypeDao.findBySql(page, sql.toString(), Map.class);
	}
	
	@Override
	public Long addCustomerType(String name, String remark) {
		Long index = (long) -1;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String uuid = UUID.randomUUID().toString();
		CustomerType customerType = new CustomerType(name, remark);
		customerType.setCreateDate(sdf.format(new Date()));
		customerType.setValue(String.valueOf(Integer.valueOf(getMaxValue()) + 1));
		customerType.setCreateBy(UserUtils.getUser());
		customerType.setUuid(uuid.replace("-", ""));
		
		index = customerTypeDao.save(customerType).getId();
		
		return index;
	}

	@Override
	public void delCustomerType(Long id) {
		
		customerTypeDao.delCustomerType(id);
	}

	/**
	 * 获取最大value值
	 * */
	private String getMaxValue() {
		
		String sql = "select max(`value`) as valueMax from sys_customer_type";
		List<Map<String, String>> maxValue = customerTypeDao.findBySql(sql, Map.class);
		return maxValue.get(0).get("valueMax");
	}

	@Override
	public List<Map<String, Object>> getCustomerTypeList4Select() {
		
		List<Map<String, Object>> customerTypeList = new ArrayList<Map<String, Object>>();
		List<CustomerType> customerTypes = customerTypeDao.findAllCustomerType();
		
		for (CustomerType ct : customerTypes) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("value", ct.getValue());
			map.put("name", ct.getName());
			customerTypeList.add(map);
		}
		return customerTypeList;
	}

	@Override
	public boolean checkRepeat(String name) {
		
		Long count = customerTypeDao.checkRepeat(name);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
		
		
	}

	@Override
	public boolean isUsed(Long id) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT 'X' AS result ")
			  .append(" FROM sys_customer_type sct, agentinfo a ")
			  .append(" WHERE a.agent_type = sct.`value` ")
			  .append(" AND sct.id = ").append(id).append(" LIMIT 1");
		
		List<String> results = customerTypeDao.findBySql(buffer.toString());
		
		if (results != null && results.size() > 0) {
			return true;
		}
		return false;
	}
}
