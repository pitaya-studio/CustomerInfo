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
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.temp.stock.dao.ActivitygroupreserveTempDao;
import com.trekiz.admin.modules.temp.stock.dao.ActivityreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.ActivitygroupreserveTemp;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.ActivitygroupreserveTempInput;
import com.trekiz.admin.modules.temp.stock.query.ActivitygroupreserveTempQuery;
import com.trekiz.admin.modules.temp.stock.service.ActivitygroupreserveTempService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivitygroupreserveTempServiceImpl  extends BaseService implements ActivitygroupreserveTempService{
	@Autowired
	private ActivitygroupreserveTempDao activitygroupreserveTempDao;
	@Autowired
	private ActivityGroupDao activitygroupDao;
	@Autowired
	private ActivityreservefileTempDao activityreservefileTempDao;
	@Autowired
	private AgentinfoService agentinfoService;

	public void save (ActivitygroupreserveTemp activitygroupreserveTemp){
		super.setOptInfo(activitygroupreserveTemp, BaseService.OPERATION_ADD);
		activitygroupreserveTempDao.saveObj(activitygroupreserveTemp);
	}
	
	public void save (ActivitygroupreserveTempInput activitygroupreserveTempInput){
		ActivitygroupreserveTemp activitygroupreserveTemp = activitygroupreserveTempInput.getActivitygroupreserveTemp();
		super.setOptInfo(activitygroupreserveTemp, BaseService.OPERATION_ADD);
		activitygroupreserveTempDao.saveObj(activitygroupreserveTemp);
	}
	
	public void update (ActivitygroupreserveTemp activitygroupreserveTemp){
		super.setOptInfo(activitygroupreserveTemp, BaseService.OPERATION_UPDATE);
		activitygroupreserveTempDao.updateObj(activitygroupreserveTemp);
	}
	
	public ActivitygroupreserveTemp getById(java.lang.Integer value) {
		return activitygroupreserveTempDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivitygroupreserveTemp obj = activitygroupreserveTempDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivitygroupreserveTemp> find(Page<ActivitygroupreserveTemp> page, ActivitygroupreserveTempQuery activitygroupreserveTempQuery) {
		DetachedCriteria dc = activitygroupreserveTempDao.createDetachedCriteria();
		
	   	if(activitygroupreserveTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activitygroupreserveTempQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activitygroupreserveTempQuery.getUuid()));
		}
	   	if(activitygroupreserveTempQuery.getSrcActivityId()!=null){
	   		dc.add(Restrictions.eq("srcActivityId", activitygroupreserveTempQuery.getSrcActivityId()));
	   	}
	   	if(activitygroupreserveTempQuery.getActivityGroupId()!=null){
	   		dc.add(Restrictions.eq("activityGroupId", activitygroupreserveTempQuery.getActivityGroupId()));
	   	}
	   	if(activitygroupreserveTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", activitygroupreserveTempQuery.getAgentId()));
	   	}
	   	if(activitygroupreserveTempQuery.getReserveType()!=null){
	   		dc.add(Restrictions.eq("reserveType", activitygroupreserveTempQuery.getReserveType()));
	   	}
	   	if(activitygroupreserveTempQuery.getPayReservePosition()!=null){
	   		dc.add(Restrictions.eq("payReservePosition", activitygroupreserveTempQuery.getPayReservePosition()));
	   	}
	   	if(activitygroupreserveTempQuery.getSoldPayPosition()!=null){
	   		dc.add(Restrictions.eq("soldPayPosition", activitygroupreserveTempQuery.getSoldPayPosition()));
	   	}
	   	if(activitygroupreserveTempQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", activitygroupreserveTempQuery.getFrontMoney()));
	   	}
	   	if(activitygroupreserveTempQuery.getLeftpayReservePosition()!=null){
	   		dc.add(Restrictions.eq("leftpayReservePosition", activitygroupreserveTempQuery.getLeftpayReservePosition()));
	   	}
	   	if(activitygroupreserveTempQuery.getLeftFontMoney()!=null){
	   		dc.add(Restrictions.eq("leftFontMoney", activitygroupreserveTempQuery.getLeftFontMoney()));
	   	}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getReservation())){
			dc.add(Restrictions.eq("reservation", activitygroupreserveTempQuery.getReservation()));
		}
	   	if(activitygroupreserveTempQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", activitygroupreserveTempQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getRemark())){
			dc.add(Restrictions.eq("remark", activitygroupreserveTempQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getReturnRemark())){
			dc.add(Restrictions.eq("returnRemark", activitygroupreserveTempQuery.getReturnRemark()));
		}
	   	if(activitygroupreserveTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activitygroupreserveTempQuery.getCreateBy()));
	   	}
		if(activitygroupreserveTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activitygroupreserveTempQuery.getCreateDate()));
		}
	   	if(activitygroupreserveTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activitygroupreserveTempQuery.getUpdateBy()));
	   	}
		if(activitygroupreserveTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activitygroupreserveTempQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activitygroupreserveTempQuery.getDelFlag()));
		}
		//dc.addOrder(Order.desc("id"));
		Page<ActivitygroupreserveTemp> page2 = activitygroupreserveTempDao.find(page, dc);
		return page2;
	}
	
	public List<ActivitygroupreserveTemp> find( ActivitygroupreserveTempQuery activitygroupreserveTempQuery) {
		DetachedCriteria dc = activitygroupreserveTempDao.createDetachedCriteria();
		
	   	if(activitygroupreserveTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activitygroupreserveTempQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activitygroupreserveTempQuery.getUuid()));
		}
	   	if(activitygroupreserveTempQuery.getSrcActivityId()!=null){
	   		dc.add(Restrictions.eq("srcActivityId", activitygroupreserveTempQuery.getSrcActivityId()));
	   	}
	   	if(activitygroupreserveTempQuery.getActivityGroupId()!=null){
	   		dc.add(Restrictions.eq("activityGroupId", activitygroupreserveTempQuery.getActivityGroupId()));
	   	}
	   	if(activitygroupreserveTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", activitygroupreserveTempQuery.getAgentId()));
	   	}
	   	if(activitygroupreserveTempQuery.getReserveType()!=null){
	   		dc.add(Restrictions.eq("reserveType", activitygroupreserveTempQuery.getReserveType()));
	   	}
	   	if(activitygroupreserveTempQuery.getPayReservePosition()!=null){
	   		dc.add(Restrictions.eq("payReservePosition", activitygroupreserveTempQuery.getPayReservePosition()));
	   	}
	   	if(activitygroupreserveTempQuery.getSoldPayPosition()!=null){
	   		dc.add(Restrictions.eq("soldPayPosition", activitygroupreserveTempQuery.getSoldPayPosition()));
	   	}
	   	if(activitygroupreserveTempQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", activitygroupreserveTempQuery.getFrontMoney()));
	   	}
	   	if(activitygroupreserveTempQuery.getLeftpayReservePosition()!=null){
	   		dc.add(Restrictions.eq("leftpayReservePosition", activitygroupreserveTempQuery.getLeftpayReservePosition()));
	   	}
	   	if(activitygroupreserveTempQuery.getLeftFontMoney()!=null){
	   		dc.add(Restrictions.eq("leftFontMoney", activitygroupreserveTempQuery.getLeftFontMoney()));
	   	}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getReservation())){
			dc.add(Restrictions.eq("reservation", activitygroupreserveTempQuery.getReservation()));
		}
	   	if(activitygroupreserveTempQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", activitygroupreserveTempQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getRemark())){
			dc.add(Restrictions.eq("remark", activitygroupreserveTempQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getReturnRemark())){
			dc.add(Restrictions.eq("returnRemark", activitygroupreserveTempQuery.getReturnRemark()));
		}
	   	if(activitygroupreserveTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activitygroupreserveTempQuery.getCreateBy()));
	   	}
		if(activitygroupreserveTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activitygroupreserveTempQuery.getCreateDate()));
		}
	   	if(activitygroupreserveTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activitygroupreserveTempQuery.getUpdateBy()));
	   	}
		if(activitygroupreserveTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activitygroupreserveTempQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activitygroupreserveTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activitygroupreserveTempQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activitygroupreserveTempDao.find(dc);
	}
	
	public ActivitygroupreserveTemp getByUuid(String uuid) {
		return activitygroupreserveTempDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivitygroupreserveTemp activitygroupreserveTemp = getByUuid(uuid);
		activitygroupreserveTemp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activitygroupreserveTemp);
	}
	
	public boolean batchDelete(String[] uuids) {
		return activitygroupreserveTempDao.batchDelete(uuids);
	}
	
	/**
	 * @author chao.zhang
	 * @param groupOpenDatefront 出团时间起
	 * @param groupOpenDateAfter 出团时间止
	 */
	@Override
	public Page<Map<Object, Object>> findByPage(Page<Map<Object, Object>> page,
			String acitivityName,String groupCode,String agentName,Date groupOpenDatefront,Date groupOpenDateAfter) {
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" a.id DESC ");
		}
		List<Object> paramList=new ArrayList<Object>();
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT " +
				"a.id," +
				"a.uuid," +
				"a.srcActivityId," +
				"a.activityGroupId," +
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
				"b.groupCode," +
				"b.groupOpenDate," +
				"b.settlementAdultPrice," +
				"b.settlementcChildPrice," +
				"b.settlementSpecialPrice," +
				"b.freePosition,"+
				"b.planPosition," +
				"b.singleDiff,"+
				"c.agentName," +
				"e.name,"+
				"d.acitivityName," +
				"b.currency_Type as currencyType "
				)
				.append(" FROM activitygroupreserve_temp a ")
				.append("left join activitygroup b on a.activityGroupId = b.id ")
				.append("left join agentinfo c on a.agentId=c.id ")
				.append("left join travelactivity d on a.srcActivityId = d.id ")
				.append("left join sys_user e on c.agentSalerId=e.id ")
				.append(" where a.delFlag=0 ");
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
		sbf.append("and a.agentId in ("+sb.toString()+")");
		
		//添加页面条件查询
		SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotEmpty(acitivityName)){
			sbf.append(" and d.acitivityName like '%"+acitivityName+"%'");
		}
		if(StringUtils.isNotEmpty(groupCode)){
			sbf.append(" and b.groupCode  =?");
			paramList.add(groupCode);
		}
		if(StringUtils.isNotEmpty(agentName)){
			sbf.append(" and c.id=?");
			paramList.add(agentName);
		}
		if(groupOpenDatefront!=null){
			sbf.append(" and b.groupOpenDate >=?");
			String str1 = date.format(groupOpenDatefront);
			paramList.add(str1+" 00:00:00");
			
		}
		if(groupOpenDateAfter!=null){
			sbf.append(" and b.groupOpenDate <= ?");
			String str2=date.format(groupOpenDateAfter);
			paramList.add(str2+" 23:59:59");
		}
		Page<Map<Object,Object>> page2 =null;
		if(paramList.size()>0){
			page2 = activitygroupreserveTempDao.findBySql(page, sbf.toString(), Map.class,paramList.toArray());
		}else{
			page2 = activitygroupreserveTempDao.findBySql(page, sbf.toString(), Map.class);
		}
		return page2;
	}

	/**
	 * 批量存入草稿
	 *
	 * @param reserveJsonData 切位批量信息
	 * @param uploadJsonData  批量文件信息
	 * @param request
	 * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月18日19:39:20
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> batchSave2Draftbox(String reserveJsonData, String uploadJsonData, HttpServletRequest request) throws
			Exception {
		Map<String, Object> datas = new HashMap<String, Object>();
		//切位批量信息
		List<ActivitygroupreserveTemp> groupReserves = JSON.parseArray(reserveJsonData, ActivitygroupreserveTemp.class);
		//批量文件信息
		List<ActivityreservefileTemp> reserveFiles = JSON.parseArray(uploadJsonData, ActivityreservefileTemp.class);
		//待保存的切位文件集合
		List<ActivityreservefileTemp> toSaveReserveFiles = new ArrayList<ActivityreservefileTemp>();
		//根据渠道商将切位文件分组
		Map<String, List<ActivityreservefileTemp>> reserveFileMap = new HashMap<String, List<ActivityreservefileTemp>>();
		if(CollectionUtils.isNotEmpty(reserveFiles)) {
			for(ActivityreservefileTemp reserveFile : reserveFiles) {
				if(reserveFileMap.get(reserveFile.getAgentId().toString()) == null) {
					List<ActivityreservefileTemp> reserveFileList = new ArrayList<ActivityreservefileTemp>();
					reserveFileList.add(reserveFile);
					reserveFileMap.put(reserveFile.getAgentId().toString(), reserveFileList);
				} else {
					reserveFileMap.get(reserveFile.getAgentId().toString()).add(reserveFile);
				}
			}
		}

		if(CollectionUtils.isNotEmpty(groupReserves)) {
			try {
				for(ActivitygroupreserveTemp groupReserve : groupReserves) {
                    //获取产品
                    ActivityGroup activityGroup = activitygroupDao.getById(groupReserve.getActivityGroupId().longValue());
                    //切位时已售出切位 soldPayPosition=0
                    groupReserve.setSoldPayPosition(0);
                    //填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0
                    if(groupReserve.getFrontMoney() == null) {
                        groupReserve.setFrontMoney(0.00);
                    }
                    groupReserve.setSrcActivityId(activityGroup.getSrcActivityId()); // 产品id
                    groupReserve.setReserveType(0);//占位方式：0,定金占位；1,全款占位
                    groupReserve.setLeftpayReservePosition(0);
                    groupReserve.setLeftFontMoney(0.00);
					if(groupReserve.getPayReservePosition() == null) {
						groupReserve.setPayReservePosition(0);
					}
                    super.setOptInfo(groupReserve, BaseService.OPERATION_ADD);
                    activitygroupreserveTempDao.saveObj(groupReserve);

                    //组装切位文件集合
                    List<ActivityreservefileTemp> agentReserveFiles = reserveFileMap.get(groupReserve.getAgentId().toString());
                    if(CollectionUtils.isNotEmpty(agentReserveFiles)) {
                        for(ActivityreservefileTemp agentReserveFile : agentReserveFiles) {
                            ActivityreservefileTemp actReserve = new ActivityreservefileTemp();
                            actReserve.setSrcActivityId(groupReserve.getSrcActivityId());
                            actReserve.setActivityGroupId(groupReserve.getActivityGroupId());
                            actReserve.setFileName(agentReserveFile.getFileName());
                            actReserve.setSrcDocId(agentReserveFile.getSrcDocId());
                            actReserve.setAgentId(agentReserveFile.getAgentId());
                            actReserve.setReserveTempUuid(groupReserve.getUuid());
                            super.setOptInfo(actReserve, OPERATION_ADD);
                            toSaveReserveFiles.add(actReserve);
                        }
                    }
                }
				activityreservefileTempDao.batchSave(toSaveReserveFiles);
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
	 * @author chao.zhang
	 */
	public List<ActivityreservefileTemp> down(String uuid){
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT id," +
							"srcActivityId,"+
							"activityGroupId," +
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
							"	FROM activityreservefile_temp" +
							"	WHERE reserve_temp_uuid=? and delFlag=0");
		List<ActivityreservefileTemp> list = activitygroupreserveTempDao.findBySql(sbf.toString(),ActivityreservefileTemp.class,uuid);
		return list;
	}
	
	/**
	 * 根据散拼切位临时表获取散拼切位临时集合
	 * @Description: 
	 * @param @param reserveTempUuids
	 * @param @return   
	 * @return List<ActivitygroupreserveTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午8:09:50
	 */
	public List<ActivitygroupreserveTemp> getByUuids(List<String> reserveTempUuids) {
		return activitygroupreserveTempDao.getByUuids(reserveTempUuids);
	}

	/**
	 * 插入ActivityreservefileTemp
	 * @param list 
	 * @author chao.zhang
	 */
	@Override
	public void saveFileTemp(List<ActivityreservefileTemp> list) {
		for(ActivityreservefileTemp activityreservefileTemp:list){
			super.setOptInfo(activityreservefileTemp, BaseService.OPERATION_ADD);
			activityreservefileTempDao.saveObj(activityreservefileTemp);
		}
	}
	/**
	 * 修改file的delFlag
	 */
	@Override
	public void delFile(Long docId,String uuid) {
		activitygroupreserveTempDao.delFile(docId,uuid);
	}

	
	/**
	 * 批量更新散拼切位草稿箱信息
	 * @Description: 
	 * @param @param reserveTemps
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-24 下午8:16:01
	 */
	public boolean batchUpdate(List<ActivitygroupreserveTemp> reserveTemps) {
		boolean flag = false;
		try{
			activitygroupreserveTempDao.batchUpdate(reserveTemps);
			flag = true;
		} catch(Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

}
