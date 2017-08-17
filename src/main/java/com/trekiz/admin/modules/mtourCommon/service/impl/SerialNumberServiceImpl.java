/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.MD5Utils;
import com.trekiz.admin.modules.mtourCommon.dao.SerialNumberDao;
import com.trekiz.admin.modules.mtourCommon.entity.SerialNumber;
import com.trekiz.admin.modules.mtourCommon.service.SerialNumberService;

/**
 * serial_number表中tableName为mtour_merge的code值自成一套
 * @author quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class SerialNumberServiceImpl extends BaseService implements
		SerialNumberService {
	
	
	@Autowired
	private SerialNumberDao serialNumberDao;

	/**
	 * 获取流水号
	 * @param tableName
	 * @param recordId
	 * @return
	 */
	public String genSerialNumber(String tableName, Integer recordId) {
		SerialNumber sn = getSerialNumber(tableName, recordId);
		String maxCode = getMaxCodeByTableName(tableName);
		if (sn == null) {
			sn = new SerialNumber();
			sn.setTableName(tableName);
			sn.setRecordId(recordId);
			sn.setCode(String.format("%07d", StringUtils.isNotBlank(maxCode)?Long.parseLong(maxCode)+1:1));
			this.setOptInfo(sn, OPERATION_ADD);
			serialNumberDao.saveObj(sn);
		}
		return sn.getCode();
	}
	
	/**
	 * 获取美图合开支出单流水号（根据选择的支出明细id生成MD5查询，有则返回，没有生成）
	 * 借款：1，退款：2，追加成本：3，这三种类型时 取  airticket_order_moneyAmount
	 * 当 4时 取cost_record表数据
	 * @param tableName  目前固定为：mtour_merge
	 * @param payInfos   支付记录的id和类型信息
	 * @return  序列号
	 */
	@Override
	public String obtainSerialNumber(List<String[]> payInfos) {
		String serialNumber = "";
		if(CollectionUtils.isNotEmpty(payInfos)){
			if(payInfos.size()==1){
				String[] info = payInfos.get(0);
				String tableName = "4".equals(info[0])?"cost_record":"airticket_order_moneyAmount";
				serialNumber = genSerialNumber(tableName,Integer.valueOf(info[1]));
			}else{
				serialNumber = getSerialNumberForMrour(payInfos);
			}
		}
		return serialNumber;
	}

	/**
	 * 
	 * @param payInfos payInfos[1] id payInfos[0] type
	 * @return
	 */
	public String getSerialNumberForMrour(List<String[]> payInfos){
		StringBuilder sb = new StringBuilder();
		for(String[] array : payInfos){
			sb.append(array[0]).append(array[1]);
		}
		String MD5Serial = MD5Utils.generateMD5Code(sb.toString());
		String serialNumber = "";
		SerialNumber sn = getSerialNumberForMtour("mtour_merge", MD5Serial);
		if (sn == null) {
			String maxCode = getMaxCodeByTableNameForMtour();
			sn = new SerialNumber();
			sn.setTableName("mtour_merge");
			sn.setCode(String.format("%07d", StringUtils.isNotBlank(maxCode)?Long.parseLong(maxCode)+1:1));
			this.setOptInfo(sn, OPERATION_ADD);
			sn.setUuid(MD5Serial);
			serialNumberDao.saveObj(sn);
			serialNumber = sn.getCode();
		}else{
			serialNumber = sn.getCode();
		}
		return serialNumber;
	}

	/**
	 * 获取最大code值
	 * @param tableName
	 * @return
	 */
	public String getMaxCodeByTableName(String tableName) {
		String sql_max = "select max(code) from serial_number WHERE tableName !='mtour_merge'";
		String maxCode = (String)serialNumberDao.getSession().createSQLQuery(sql_max).uniqueResult();
		return maxCode;
	}
	
	/**
	 * 获取最大code值（美图合开支出单使用）
	 * @param tableName
	 * @return
	 */
	public String getMaxCodeByTableNameForMtour() {
		String sql_max = "select max(code) from serial_number WHERE tableName ='mtour_merge'";
		String maxCode = (String)serialNumberDao.getSession().createSQLQuery(sql_max).uniqueResult();
		return maxCode;
	}

	/**
	 * 获取流水号对象
	 * @param tableName
	 * @param recordId
	 * @return
	 */
	public SerialNumber getSerialNumber(String tableName, Integer recordId) {
		String sql = "select id,uuid,tableName,recordId,code,createBy,createDate,updateBy,updateDate,delFlag " 
	               + " from serial_number where delFlag = '0' "
				   + " and tableName = '" + tableName + "' and recordId = " + recordId;
		SerialNumber sn = (SerialNumber) serialNumberDao.getSession()
				.createSQLQuery(sql).addEntity(SerialNumber.class)
				.uniqueResult();
		return sn;
	}
	
	/**
	 * 获取流水号对象（美图合开支出单使用）
	 * @param tableName  目前固定为：mtour_merge
	 * @param MD5Serial  根据合开支出记录生成的MD5序列
	 * @return
	 */
	public SerialNumber getSerialNumberForMtour(String tableName, String MD5Serial) {
		String sql = "select id,uuid,tableName,recordId,code,createBy,createDate,updateBy,updateDate,delFlag " 
	               + " from serial_number where delFlag = '0' "
				   + " and tableName = '" + tableName + "' and uuid = '" + MD5Serial+"'";
		SerialNumber sn = (SerialNumber) serialNumberDao.getSession()
				.createSQLQuery(sql).addEntity(SerialNumber.class)
				.uniqueResult();
		return sn;
	}
}
