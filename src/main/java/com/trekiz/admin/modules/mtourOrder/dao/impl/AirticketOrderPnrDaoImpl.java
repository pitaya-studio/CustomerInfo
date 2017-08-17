/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnr;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrDaoImpl extends BaseDaoImpl<AirticketOrderPnr>  implements AirticketOrderPnrDao{
	@Override
	public AirticketOrderPnr getByUuid(String uuid) {
		Object entity = super.createQuery("from AirticketOrderPnr airticketOrderPnr where airticketOrderPnr.uuid=? and airticketOrderPnr.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderPnr)entity;
		}
		return null;
	}
	
	public List<AirticketOrderPnr> getByAirticketOrderId(Integer airticketOrderId) {
		return super.find("from AirticketOrderPnr airticketOrderPnr where airticketOrderPnr.airticketOrderId=? and airticketOrderPnr.delFlag=?", airticketOrderId, BaseEntity.DEL_FLAG_NORMAL);
	}

	@Override
	public List<AirticketOrderPnr> findByPNRGroupUuid(String pnrGroupUuid) {
		//codeType=0为PNR
		return super.find("from AirticketOrderPnr where codeType=0 and airticketOrderPnrGroupUuid=? and delFlag=?",pnrGroupUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
    public List<Map<String,Object>> queryAirticketOrderPNCByOrderUuid(Integer airticketOrderId){
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	String sql = "SELECT p.uuid as invoiceOriginalUuid,p.code_type as invoiceOriginalTypeCode,p.flight_pnr AS PNR," +
    			"p.cost_supply_id AS tourOperatorUuid , " +
    			"s.supplierName as tourOperatorName, " +
    			" p.cost_supply_type as tourOperatorTypeCode," + 
                "(select sd.label from sys_dict sd where " +
                " sd.delflag='0' and sd.`value` = p.cost_supply_type and sd.type='travel_agency_type') as tourOperatorTypeName, " +
                "p.cost_bank_name as receiveBank, " +
                "p.cost_account_no as receiveAccountNo " +
    			"from airticket_order_pnr p left join supplier_info s on p.cost_supply_id = s.id where p.delFlag = ? and p.airticket_order_id = ?";
    	try{
    		list =  findBySql(sql, Map.class, BaseEntity.DEL_FLAG_NORMAL,airticketOrderId);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	
    	return list;
	}
   
  /*  public Map<String,Object> queryAirticketOrderListPNCByOrderUuid(Integer orderId){
    	String sql = "SELECT p.code_type as invoiceOriginalTypeCode,case when p.code_type=0 then p.flight_pnr ELSE '' END AS PNR,case when code_type=1 then p.flight_pnr ELSE '' END AS tourOperatorUuid , case when p.code_type=1 then s.supplierName ELSE '' end as tourOperatorName from airticket_order_pnr p left join supplier_info s on p.flight_pnr = s.id where p.delFlag = ? and p.airticket_order_id = ?";
    	List<Map<String,Object>> list =  findBySql(sql, Map.class, BaseEntity.DEL_FLAG_NORMAL,orderId);
    	//根据orderId 查airticket_order agentinfo_id=-1 查询 agent_info 中的nagentname
    	String asql = "SELECT agentinfo_id,nagentName FROM airticket_order where id = ?";
    	List<AirticketOrder> alist =  findBySql(asql, AirticketOrder.class,orderId);
    	if(alist!=null && alist.size()>0){
    		Map<String,Object> typemap = new HashMap<String,Object>();
    		Map<String,Object> channelmap = new HashMap<String,Object>();
    		AirticketOrder airticketOrder = alist.get(0);
    		Long agentInfoId = airticketOrder.getAgentinfoId();
    		
    		if(agentInfoId== -1){
    			typemap.put("channelTypeCode", "2");
    			typemap.put("channelTypeName", "非签约渠道");
    			channelmap.put("channelUuid", "-1");
    			channelmap.put("channelName", airticketOrder.getNagentName());
    		}else{
    			typemap.put("channelTypeCode", "1");
    			typemap.put("channelTypeName", "签约渠道");
    			
    			asql = "SELECT id,agentName FROM agent_info where id = ?";
    			List<Agentinfo> aglist =  findBySql(asql, Agentinfo.class,orderId);
    			if(aglist!=null && aglist.size()>0){
    				Agentinfo agentinfo = aglist.get(0);
        			channelmap.put("channelUuid", agentinfo.getId());
        			channelmap.put("channelName", agentinfo.getAgentName());
    			}
    			
    		}
    	}
    	return list;
    }*/
    
    
}
