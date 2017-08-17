package com.trekiz.admin.modules.visa.service;

import java.util.UUID;

import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;

@Service
@Transactional(readOnly = true)
public class VisaFlowBatchOprationService extends BaseService {
	
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;

	@Transactional(readOnly = false)
	public void updateBySql(String batchNo, int batchPersonCount, String batchUuid){
		
		//String batchUuid = UUID.randomUUID().toString();
		User user = UserUtils.getUser();
		String sql = "INSERT INTO visa_flow_batch_opration (uuid, batch_no,busyness_type,create_user_id,create_user_name,create_time) " +
				"VALUES ('"+batchUuid+"','"+batchNo+"','3','"+user.getId()+"','"+user.getName()+"','"+new DateTime().toString("yyyy-MM-dd HH:mm:ss")+"')";
		
		
		visaFlowBatchOprationDao.updateBySql(sql);
		
	}
}
