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
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.temp.stock.entity.AirticketactivityreserveTemp;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.AirticketactivityreserveTempInput;
import com.trekiz.admin.modules.temp.stock.query.AirticketactivityreserveTempQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketactivityreserveTempService{
	
	public void save (AirticketactivityreserveTemp airticketactivityreserveTemp);
	
	public void save (AirticketactivityreserveTempInput airticketactivityreserveTempInput);
	
	public void update (AirticketactivityreserveTemp airticketactivityreserveTemp);
	
	public AirticketactivityreserveTemp getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketactivityreserveTemp> find(Page<AirticketactivityreserveTemp> page, AirticketactivityreserveTempQuery airticketactivityreserveTempQuery);
	
	public List<AirticketactivityreserveTemp> find( AirticketactivityreserveTempQuery airticketactivityreserveTempQuery);
	
	public AirticketactivityreserveTemp getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	/**
	 * 分页查询接口
	 * @param page
	 * @param productCode
	 * @param groupCode
	 * @param agentName
	 * @param startingDateFront
	 * @param startingDateAfter
	 * @return
	 * @author chao.zhang
	 */
	
	public Page<Map<Object, Object>> findByPage(Page<Map<Object, Object>> page,String productCode,String groupCode,String agentName,Date startingDateFront,Date startingDateAfter);

	
	/**
	 * 下载
	 * @param agentId 渠道Id
	 * @param activityGroupId 团期Id
	 */
	public List<DocInfo> down(Integer agentId,Integer activityGroupId);


	/**
	 * 批量存入草稿
	 * @param reserveJsonData 切位批量信息
	 * @param uploadJsonData  批量文件信息
	 * @param request
	 * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月19日12:12:36
	 */
	Map<String,Object> batchSave2Draftbox(String reserveJsonData, String uploadJsonData, HttpServletRequest request) throws Exception;

	/**
	 * @author Administrator chao.zhang
	 * @param uuid
	 * @return
	 */
	public List<AirticketreservefileTemp> down(String uuid);
	/**
	 * @author Administrator  chao.zhang
	 * @param docId
	 * @param uuid
	 */
	public void delFile(Long docId, String uuid);
	/**
	 * @author Administrator chao.zhang
	 * @param list
	 *   保存file
	 */
	public void saveFileTemp(List<AirticketreservefileTemp> list);

	
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
	public List<AirticketactivityreserveTemp> getByUuids(List<String> reserveTempUuids);
	
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
	public boolean batchUpdate(List<AirticketactivityreserveTemp> reserveTemps);


}
