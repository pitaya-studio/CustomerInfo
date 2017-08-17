/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.dao.impl;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.pay.dao.PayFeeDao;
import com.trekiz.admin.modules.pay.entity.PayFee;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class PayFeeDaoImpl extends BaseDaoImpl<PayFee>  implements PayFeeDao{
	@Override
	public PayFee getByUuid(String uuid) {
		Object entity = super.createQuery("from PayFee payFee where payFee.uuid=? and payFee.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PayFee)entity;
		}
		return null;
	}
	
	public boolean batchDelete(String[] uuids) {
		if(ArrayUtils.isEmpty(uuids)) {
			return false;
		}
		
		StringBuffer sb = new StringBuffer();
		for(String uuid : uuids) {
			sb.append("'");
			sb.append(uuid);
			sb.append("'");
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		int count = super.createSqlQuery("update pay_fee set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据支付信息id获取支付手续费信息
	 * @Description: 
	 * @param @param refundId
	 * @param @return   
	 * @return List<PayFee>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-7
	 */
	public List<PayFee> findByRefundId(String refundId) {
		return super.find("from PayFee payFee where payFee.refundId=? and payFee.delFlag=?", refundId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
