/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.temp.stock.dao.AirticketactivityreserveTempDao;
import com.trekiz.admin.modules.temp.stock.dao.AirticketreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.AirticketactivityreserveTemp;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.AirticketactivityreserveTempInput;
import com.trekiz.admin.modules.temp.stock.query.AirticketactivityreserveTempQuery;
import com.trekiz.admin.modules.temp.stock.service.AirticketactivityreserveTempService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketactivityreserveTempServiceImpl  extends BaseService implements AirticketactivityreserveTempService{
	@Autowired
	private AirticketactivityreserveTempDao airticketactivityreserveTempDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private AirticketreservefileTempDao airticketreservefileTempDao;
	
	@Autowired
	private AgentinfoService agentinfoService;

	public void save (AirticketactivityreserveTemp airticketactivityreserveTemp){
		super.setOptInfo(airticketactivityreserveTemp, BaseService.OPERATION_ADD);
		airticketactivityreserveTempDao.saveObj(airticketactivityreserveTemp);
	}
	
	public void save (AirticketactivityreserveTempInput airticketactivityreserveTempInput){
		AirticketactivityreserveTemp airticketactivityreserveTemp = airticketactivityreserveTempInput.getAirticketactivityreserveTemp();
		super.setOptInfo(airticketactivityreserveTemp, BaseService.OPERATION_ADD);
		airticketactivityreserveTempDao.saveObj(airticketactivityreserveTemp);
	}
	
	public void update (AirticketactivityreserveTemp airticketactivityreserveTemp){
		super.setOptInfo(airticketactivityreserveTemp, BaseService.OPERATION_UPDATE);
		airticketactivityreserveTempDao.updateObj(airticketactivityreserveTemp);
	}
	
	public AirticketactivityreserveTemp getById(java.lang.Integer value) {
		return airticketactivityreserveTempDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketactivityreserveTemp obj = airticketactivityreserveTempDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketactivityreserveTemp> find(Page<AirticketactivityreserveTemp> page, AirticketactivityreserveTempQuery airticketactivityreserveTempQuery) {
		DetachedCriteria dc = airticketactivityreserveTempDao.createDetachedCriteria();
		
	   	if(airticketactivityreserveTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketactivityreserveTempQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketactivityreserveTempQuery.getUuid()));
		}
	   	if(airticketactivityreserveTempQuery.getActivityId()!=null){
	   		dc.add(Restrictions.eq("activityId", airticketactivityreserveTempQuery.getActivityId()));
	   	}
	   	if(airticketactivityreserveTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", airticketactivityreserveTempQuery.getAgentId()));
	   	}
	   	if(airticketactivityreserveTempQuery.getReserveType()!=null){
	   		dc.add(Restrictions.eq("reserveType", airticketactivityreserveTempQuery.getReserveType()));
	   	}
	   	if(airticketactivityreserveTempQuery.getPayReservePosition()!=null){
	   		dc.add(Restrictions.eq("payReservePosition", airticketactivityreserveTempQuery.getPayReservePosition()));
	   	}
	   	if(airticketactivityreserveTempQuery.getSoldPayPosition()!=null){
	   		dc.add(Restrictions.eq("soldPayPosition", airticketactivityreserveTempQuery.getSoldPayPosition()));
	   	}
	   	if(airticketactivityreserveTempQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", airticketactivityreserveTempQuery.getFrontMoney()));
	   	}
	   	if(airticketactivityreserveTempQuery.getLeftpayReservePosition()!=null){
	   		dc.add(Restrictions.eq("leftpayReservePosition", airticketactivityreserveTempQuery.getLeftpayReservePosition()));
	   	}
	   	if(airticketactivityreserveTempQuery.getLeftFontMoney()!=null){
	   		dc.add(Restrictions.eq("leftFontMoney", airticketactivityreserveTempQuery.getLeftFontMoney()));
	   	}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getReservation())){
			dc.add(Restrictions.eq("reservation", airticketactivityreserveTempQuery.getReservation()));
		}
	   	if(airticketactivityreserveTempQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", airticketactivityreserveTempQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getRemark())){
			dc.add(Restrictions.eq("remark", airticketactivityreserveTempQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getReturnRemark())){
			dc.add(Restrictions.eq("returnRemark", airticketactivityreserveTempQuery.getReturnRemark()));
		}
	   	if(airticketactivityreserveTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketactivityreserveTempQuery.getCreateBy()));
	   	}
		if(airticketactivityreserveTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketactivityreserveTempQuery.getCreateDate()));
		}
	   	if(airticketactivityreserveTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketactivityreserveTempQuery.getUpdateBy()));
	   	}
		if(airticketactivityreserveTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketactivityreserveTempQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketactivityreserveTempQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketactivityreserveTempDao.find(page, dc);
	}
	
	public List<AirticketactivityreserveTemp> find( AirticketactivityreserveTempQuery airticketactivityreserveTempQuery) {
		DetachedCriteria dc = airticketactivityreserveTempDao.createDetachedCriteria();
		
	   	if(airticketactivityreserveTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketactivityreserveTempQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketactivityreserveTempQuery.getUuid()));
		}
	   	if(airticketactivityreserveTempQuery.getActivityId()!=null){
	   		dc.add(Restrictions.eq("activityId", airticketactivityreserveTempQuery.getActivityId()));
	   	}
	   	if(airticketactivityreserveTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", airticketactivityreserveTempQuery.getAgentId()));
	   	}
	   	if(airticketactivityreserveTempQuery.getReserveType()!=null){
	   		dc.add(Restrictions.eq("reserveType", airticketactivityreserveTempQuery.getReserveType()));
	   	}
	   	if(airticketactivityreserveTempQuery.getPayReservePosition()!=null){
	   		dc.add(Restrictions.eq("payReservePosition", airticketactivityreserveTempQuery.getPayReservePosition()));
	   	}
	   	if(airticketactivityreserveTempQuery.getSoldPayPosition()!=null){
	   		dc.add(Restrictions.eq("soldPayPosition", airticketactivityreserveTempQuery.getSoldPayPosition()));
	   	}
	   	if(airticketactivityreserveTempQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", airticketactivityreserveTempQuery.getFrontMoney()));
	   	}
	   	if(airticketactivityreserveTempQuery.getLeftpayReservePosition()!=null){
	   		dc.add(Restrictions.eq("leftpayReservePosition", airticketactivityreserveTempQuery.getLeftpayReservePosition()));
	   	}
	   	if(airticketactivityreserveTempQuery.getLeftFontMoney()!=null){
	   		dc.add(Restrictions.eq("leftFontMoney", airticketactivityreserveTempQuery.getLeftFontMoney()));
	   	}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getReservation())){
			dc.add(Restrictions.eq("reservation", airticketactivityreserveTempQuery.getReservation()));
		}
	   	if(airticketactivityreserveTempQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", airticketactivityreserveTempQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getRemark())){
			dc.add(Restrictions.eq("remark", airticketactivityreserveTempQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getReturnRemark())){
			dc.add(Restrictions.eq("returnRemark", airticketactivityreserveTempQuery.getReturnRemark()));
		}
	   	if(airticketactivityreserveTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketactivityreserveTempQuery.getCreateBy()));
	   	}
		if(airticketactivityreserveTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketactivityreserveTempQuery.getCreateDate()));
		}
	   	if(airticketactivityreserveTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketactivityreserveTempQuery.getUpdateBy()));
	   	}
		if(airticketactivityreserveTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketactivityreserveTempQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketactivityreserveTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketactivityreserveTempQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketactivityreserveTempDao.find(dc);
	}
	
	public AirticketactivityreserveTemp getByUuid(String uuid) {
		return airticketactivityreserveTempDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketactivityreserveTemp airticketactivityreserveTemp = getByUuid(uuid);
		airticketactivityreserveTemp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketactivityreserveTemp);
	}
	
	public boolean batchDelete(String[] uuids) {
		return airticketactivityreserveTempDao.batchDelete(uuids);
	}
	/**
	 * @author chao.zhang
	 * @param productCode 产品编号
	 * @param groupCode 团编号
	 * @param agentName 渠道
	 * @param startingDateFront 出团起时间
	 * @param startingDateAfter 出团止时间
	 */
	@Override
	public Page<Map<Object, Object>> findByPage(Page<Map<Object, Object>> page,
			String productCode, String groupCode, String agentName, Date startingDateFront, Date startingDateAfter) {
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" a.id DESC ");
		}
		List<Object> paramList=new ArrayList<Object>();
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT " +
				"a.id," +
				"a.uuid," +
				"a.activityId," +
				"a.agentId," +
				"a.reserveType," +
				"a.payReservePosition," +
				"a.soldPayPosition," +
				"a.frontMoney," +
				"a.leftpayReservePosition," +
				"a.leftFontMoney," +
				"a.reservation," +
				"a.payType," +
				"a.remark," +
				"a.returnRemark," +
				"a.createBy," +
				"a.createDate," +
				"a.updateBy," +
				"a.updateDate," +
				"a.delFlag," +
				"b.group_code as groupCode," +
				"b.startingDate," +
				"b.settlementAdultPrice," +
				"b.settlementcChildPrice," +
				"b.settlementSpecialPrice," +
				"b.freePosition,"+
				"b.reservationsNum," +
				"b.product_Code as productCode," +
				"c.agentName," +
				"d.name," +
				"e.currency_mark as mark"
			
				)
				.append(" FROM airticketactivityreserve_temp a ")
				.append("left join activity_airticket b on a.activityId = b.id ")
				.append("left join agentinfo c on a.agentId=c.id ")
				.append("left join sys_user d on c.agentSalerId=d.id")
				.append(" left join currency e on b.currency_id=e.currency_id")
				.append(" where a.delFlag=0");
				//.append("left join activitygroupreserve d on b.id = d.srcActivityId ");
				//.append("left join activitygroup e on d.activityGroupId=e.id");
			List<Agentinfo> findStockAgentinfo = agentinfoService.findStockAgentinfo();
		 		String agid="";
		 		StringBuffer sb = new StringBuffer();
		 		if(CollectionUtils.isNotEmpty(findStockAgentinfo)) {
		 			for(Agentinfo agentInfo:findStockAgentinfo) {
		 				sb.append(agentInfo.getId());
		 				sb.append(",");
		 			}
		 			sb.deleteCharAt(sb.length()-1);
		 		}
		sbf.append(" and a.agentId in ("+sb.toString()+")");

		//添加页面条件查询
		SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotEmpty(productCode)){
			sbf.append(" and b.product_Code like '%"+productCode+"%'");
		}
		if(StringUtils.isNotEmpty(groupCode)){
			sbf.append(" and b.group_code  =?");
			paramList.add(groupCode);
		}
		if(StringUtils.isNotEmpty(agentName)){
			sbf.append(" and c.id=?");
			paramList.add(agentName);
		}
		if(startingDateFront!=null){
			sbf.append(" and b.startingDate >=?");
			String str1 = date.format(startingDateFront);
			paramList.add(str1+" 00:00:00");
		}
		if(startingDateAfter!=null){
			sbf.append(" and b.startingDate <= ?");
			String str2=date.format(startingDateAfter);
			paramList.add(str2+" 23:59:59");
		}
		Page<Map<Object,Object>> page2 =null;
		if(paramList.size()>0){
			page2=airticketactivityreserveTempDao.findBySql(page, sbf.toString(), Map.class,paramList.toArray());
		}else{
			page2=airticketactivityreserveTempDao.findBySql(page, sbf.toString(), Map.class);
		}
		return page2;
	
	}

	/**
	 * @author chao.zhang
	 * @param agenId 渠道Id
	 * @param activityGroupId 团期id
	 */
	public List<DocInfo> down(Integer agentId, Integer activityGroupId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT doc.docName," +
							"doc.id,"+
							"doc.docPath" +
							"	FROM airticketreservefile_temp a" +
							"	left join docinfo doc on a.srcDocId=doc.id" +
							"	WHERE a.agentId=? and a.activityGroupId=?");
		List<DocInfo> list = airticketactivityreserveTempDao.findBySql(sbf.toString(),DocInfo.class,agentId,activityGroupId);
		return list;
	}
	


	/**
	 * 机票批量存入草稿
	 *
	 * @param reserveJsonData 切位批量信息
	 * @param uploadJsonData  批量文件信息
	 * @param request
	 * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月19日12:13:50
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> batchSave2Draftbox(String reserveJsonData, String uploadJsonData, HttpServletRequest request) throws
			Exception {
		Map<String, Object> datas = new HashMap<String, Object>();
		//切位批量信息

		List<AirticketactivityreserveTemp> groupReserves = JSON.parseArray(reserveJsonData, AirticketactivityreserveTemp.class);
		//批量文件信息
		List<AirticketreservefileTemp> reserveFiles = JSON.parseArray(uploadJsonData, AirticketreservefileTemp.class);
		//待保存的切位文件集合
		List<AirticketreservefileTemp> toSaveReserveFiles = new ArrayList<AirticketreservefileTemp>();
		//根据渠道商将切位文件分组
		Map<String, List<AirticketreservefileTemp>> reserveFileMap = new HashMap<String, List<AirticketreservefileTemp>>();
		if(CollectionUtils.isNotEmpty(reserveFiles)) {
			for(AirticketreservefileTemp reserveFile : reserveFiles) {
				if(reserveFileMap.get(reserveFile.getAgentId().toString()) == null) {
					List<AirticketreservefileTemp> reserveFileList = new ArrayList<AirticketreservefileTemp>();
					reserveFileList.add(reserveFile);
					reserveFileMap.put(reserveFile.getAgentId().toString(), reserveFileList);
				} else {
					reserveFileMap.get(reserveFile.getAgentId().toString()).add(reserveFile);
				}
			}
		}

		if(CollectionUtils.isNotEmpty(groupReserves)) {
			try {
				for(AirticketactivityreserveTemp groupReserve : groupReserves) {
					//获取产品
					ActivityAirTicket activityAirTicket = activityAirTicketDao.getById(groupReserve.getActivityId().longValue());
					//切位时已售出切位 soldPayPosition=0
					groupReserve.setSoldPayPosition(0);
					//填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0
					if(groupReserve.getFrontMoney() == null) {
						groupReserve.setFrontMoney(0.00);
					}
					groupReserve.setActivityId(activityAirTicket.getId().intValue()); // 产品id
					groupReserve.setReserveType(0);//占位方式：0,定金占位；1,全款占位
					groupReserve.setLeftpayReservePosition(0);
					groupReserve.setLeftFontMoney(0d);
					if(groupReserve.getPayReservePosition() == null) {
						groupReserve.setPayReservePosition(0);
					}
					super.setOptInfo(groupReserve, BaseService.OPERATION_ADD);
					airticketactivityreserveTempDao.saveObj(groupReserve);
					//组装切位文件集合
					List<AirticketreservefileTemp> agentReserveFiles = reserveFileMap.get(groupReserve.getAgentId().toString());
					if(CollectionUtils.isNotEmpty(agentReserveFiles)) {
						for(AirticketreservefileTemp agentReserveFile : agentReserveFiles) {
							AirticketreservefileTemp actReserve = new AirticketreservefileTemp();
							actReserve.setAirticketActivityId(groupReserve.getActivityId());
							actReserve.setFileName(agentReserveFile.getFileName());
							actReserve.setSrcDocId(agentReserveFile.getSrcDocId());
							actReserve.setAgentId(agentReserveFile.getAgentId());
							actReserve.setReserveTempUuid(groupReserve.getUuid());
							super.setOptInfo(actReserve, OPERATION_ADD);
							toSaveReserveFiles.add(actReserve);
						}
					}
				}
				airticketreservefileTempDao.batchSave(toSaveReserveFiles);
			} catch(Exception e) {
				e.getStackTrace();
				logger.error(e.getMessage());
				throw new Exception("存入草稿箱失败!");
			}
		}

		datas.put("result", "success");
		datas.put("message", "存入草稿箱成功!");
		return datas;
	}

	/**
	 * 根据uuid查询AirticketreservefileTemp
	 * @author chao.zhang
	 */
	@Override
	public List<AirticketreservefileTemp> down(String uuid) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT id," +
							"airticketActivityId,"+
							"agentId," +
							"srcDocId," +
							"fileName," +
							"reserve_temp_uuid," +
							"createDate," +
							"createBy," +
							"updateDate," +
							"updateBy,"+
							"delFlag,"+
							"reserveOrderId"+
							"	FROM airticketreservefile_temp" +
							"	WHERE reserve_temp_uuid=? and delFlag=0");
		List<AirticketreservefileTemp> list =airticketactivityreserveTempDao.findBySql(sbf.toString(),AirticketreservefileTemp.class,uuid);
		return list;
	}

	
	/**
	 * 根据机票草稿箱uuid集合查询所有的机票草稿箱信息
	 * @Description: 
	 * @param @param reserveTempUuids
	 * @param @return   
	 * @return List<AirticketactivityreserveTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-24 下午7:51:26
	 */
	public List<AirticketactivityreserveTemp> getByUuids(List<String> reserveTempUuids) {
		return airticketactivityreserveTempDao.getByUuids(reserveTempUuids);
	}
	
	/**
	 * 批量更新机票切位草稿箱信息
	 * @Description: 
	 * @param @param reserveTemps
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-24 下午8:20:30
	 */
	public boolean batchUpdate(List<AirticketactivityreserveTemp> reserveTemps) {
		boolean flag = false;
		try{
			airticketactivityreserveTempDao.batchUpdate(reserveTemps);
			flag = true;
		} catch(Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}


		/**
		 * 修改file的delFlag
		 */
		@Override
		public void delFile(Long docId,String uuid) {
			airticketactivityreserveTempDao.delFile(docId,uuid);
		}
		/**
		 * 保存file
		 */
		@Override
		public void saveFileTemp(List<AirticketreservefileTemp> list) {
			for(AirticketreservefileTemp airticketreservefileTemp:list){
				super.setOptInfo(airticketreservefileTemp, BaseService.OPERATION_ADD);
				airticketreservefileTempDao.saveObj(airticketreservefileTemp);
			}
		}
	


}
