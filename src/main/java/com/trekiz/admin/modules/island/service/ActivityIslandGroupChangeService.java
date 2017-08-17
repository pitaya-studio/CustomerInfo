package com.trekiz.admin.modules.island.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandTraveler;

/**
 * 海岛游转团申请Service
 * @author gao
 *  2015年6月15日
 */
public interface ActivityIslandGroupChangeService {

	/**
	 * 转团申请操作，为一个或多个游客进行转团申请，如果申请失败，需要整体回滚。
	 * @author gao
	 * @param travelList 全部要转团的游客
	 * @param oldOrder 原订单实体
	 * @param  String[] remark 转团理由
	 * @param  String groupCode
	 * @param  TravelActivity ta 原产品实体
	 * @param  String remainDays 保留天数
	 * @param listDetail
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String,String> addReviewList(List<IslandTraveler> travelList ,IslandOrder oldOrder,String[] remark,ActivityIsland ta,
			String groupCode,String newRoom, String newTicket,String oldRoomControl,String oldRoomNoControl,String oldTicketControl,String oldTicketNoControl,StringBuffer reply, 
			Map<String,String> map) throws Exception;
}
