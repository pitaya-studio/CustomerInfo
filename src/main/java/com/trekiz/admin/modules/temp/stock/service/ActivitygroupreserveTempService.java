/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.temp.stock.entity.ActivitygroupreserveTemp;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.ActivitygroupreserveTempInput;
import com.trekiz.admin.modules.temp.stock.query.ActivitygroupreserveTempQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivitygroupreserveTempService{
	
	public void save (ActivitygroupreserveTemp activitygroupreserveTemp);
	
	public void save (ActivitygroupreserveTempInput activitygroupreserveTempInput);
	
	public void update (ActivitygroupreserveTemp activitygroupreserveTemp);
	
	public ActivitygroupreserveTemp getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivitygroupreserveTemp> find(Page<ActivitygroupreserveTemp> page, ActivitygroupreserveTempQuery activitygroupreserveTempQuery);
	
	public List<ActivitygroupreserveTemp> find( ActivitygroupreserveTempQuery activitygroupreserveTempQuery);
	
	public ActivitygroupreserveTemp getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 分页查询接口
	 * @param page
	 * @param acitivityName
	 * @param groupCode
	 * @param agentName
	 * @param groupOpenDatefront
	 * @param groupOpenDateAfter
	 * @return
	 */
	public Page<Map<Object, Object>> findByPage(Page<Map<Object, Object>> page,String acitivityName,String groupCode,String agentName,Date groupOpenDatefront,Date groupOpenDateAfter);

	/**
	 * 批量存入草稿
	 * @param reserveJsonData 切位批量信息
	 * @param uploadJsonData  批量文件信息
	 * @param request
     * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月18日19:39:20
     */
	Map<String, Object> batchSave2Draftbox(String reserveJsonData, String uploadJsonData, HttpServletRequest request) throws Exception;
	/**
	 * 下载
	 * @param agentId 渠道Id
	 * @param activityGroupId 团期Id
	 */
	public List<ActivityreservefileTemp> down(String uuid);
	
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
	public List<ActivitygroupreserveTemp> getByUuids(List<String> reserveTempUuids);

	/**
	 * 插入ActivityreservefileTemp
	 * @param list
	 * @param chao.zhang
	 */
	public void saveFileTemp(List<ActivityreservefileTemp> list);
	/**
	 * 修改file的delflag为1
	 * @param docId
	 */
	public void delFile(Long docId,String uuid);

	
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
	public boolean batchUpdate(List<ActivitygroupreserveTemp> reserveTemps);

}
