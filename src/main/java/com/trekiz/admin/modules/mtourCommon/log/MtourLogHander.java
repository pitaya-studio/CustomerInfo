/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.modules.mtourCommon.log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.quauq.multi.tenant.datasource.DataSourceContainer;
import com.quauq.multi.tenant.hibernate.FacesContext;
import com.trekiz.admin.common.async.receive.IMessageHander;
import com.trekiz.admin.modules.mtourCommon.entity.MtourInterfaceLog; 
import com.trekiz.admin.modules.mtourCommon.service.MtourInterfaceLogService;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-26
 */
public class MtourLogHander implements IMessageHander {
	
	private static Logger logger = LoggerFactory.getLogger(MtourLogHander.class);
	
	@Autowired
	private MtourInterfaceLogService logService;
	private static final String defaultDataSource = "default";
	
	private String queueName;
	 
	/**
	 * @param queueName the queueName to set
	 */
	public void setQueueName(String queueName) {
		if(StringUtils.isBlank(queueName))
			throw new IllegalArgumentException("queueName 不能为空");
		this.queueName = queueName;
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.common.async.receive.IMessageHander#getMessageQueueName()
	 */
	@Override
	public String getMessageQueueName() {
		return this.queueName;
	}

	/* (non-Javadoc)
	 * @see com.trekiz.admin.common.async.receive.IMessageHander#maxMessagePerBatch()
	 */
	@Override
	public long maxMessagePerBatch() { 
		return 100l;
	}

	/* (non-Javadoc)
	 * @see com.trekiz.admin.common.async.receive.IMessageHander#handMessage(java.util.Queue)
	 */
	@Override
	public void handMessage(Queue<String> messages) throws Exception {
		try{
			List<MtourInterfaceLog> logs = new LinkedList<>();
			for(String message : messages){
				MtourInterfaceLog log = JSON.parseObject(message, MtourInterfaceLog.class);
				logs.add(log);
			}
			Map<String ,List<MtourInterfaceLog>> maps = this.groupByTenantId(logs);
			Set<Entry<String, List<MtourInterfaceLog>>> set = maps.entrySet();
			for(Entry<String, List<MtourInterfaceLog>> entity : set){
				if(DataSourceContainer.DEFAULT_DATASOURCE_NAME.equals(entity.getKey())){
					logger.error("发现有无法识别租户信息的数据集，该数据集抛弃不做处理");
					continue;
				}
				FacesContext.setCurrentTenant(entity.getKey());
				logService.batchSave(entity.getValue());
			}
		}catch(Throwable e){ //日志信息-如果发现异常则打印出来后 ，抛弃
			e.printStackTrace();
		} 
	}
	
	/**
	 * 按不同的租户id将MtourInterfaceLog分组
	 * @param logs
	 * @return
	 */
	private Map<String ,List<MtourInterfaceLog>> groupByTenantId(List<MtourInterfaceLog> logs){
		Map<String ,List<MtourInterfaceLog>> maps = new HashMap<>();
		for(MtourInterfaceLog log :logs){
			List<MtourInterfaceLog> list = maps.get(log.getAsyncSysParameterTenantID());
			if(null == list){
				list = new LinkedList<>();
				maps.put(log.getAsyncSysParameterTenantID(), list);
			}
			list.add(log);
		}
		return maps;
	}
}
