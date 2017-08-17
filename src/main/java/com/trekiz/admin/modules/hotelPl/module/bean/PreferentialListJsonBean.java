/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatter;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRelHotel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequire;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;



/**
 * 可选择的优惠信息 
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class PreferentialListJsonBean  {

	private HotelPlPreferential hotelPlPreferential;//优惠名称
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<HotelPlPreferentialRoom> preferentialRoomList = new ArrayList<HotelPlPreferentialRoom>();//房型信息集合，包括关联酒店的房型 如果是关联酒店的房型则会把关联酒店名称带出
		HotelPlPreferentialRoom hotelPlPreferentialRoom = new HotelPlPreferentialRoom();
		hotelPlPreferentialRoom.setUuid("关联酒店房型uuid");
		hotelPlPreferentialRoom.setHotelUuid("酒店uuid");
		hotelPlPreferentialRoom.setHotelRoomUuid("酒店房型uuid");
		hotelPlPreferentialRoom.setHotelRoomText("酒店房型显示文本");
		hotelPlPreferentialRoom.setNights(2);
		
		List<HotelMeal> hotelMealList = new ArrayList<HotelMeal> ();
		HotelMeal hm1 = new HotelMeal();
		hm1.setMealName("BB");
		hm1.setUuid("餐型的UUID");
		HotelMeal hm2 = new HotelMeal();
		hm2.setMealName("HB");
		hm2.setUuid("餐型的UUID");
		HotelMeal hm3 = new HotelMeal();
		hm3.setMealName("FB");
		hm3.setUuid("餐型的UUID");
		HotelMeal hm4 = new HotelMeal();
		hm4.setMealName("AL");
		hm4.setUuid("餐型的UUID");
		hotelMealList.add(hm1);
		hotelMealList.add(hm2);
		hotelMealList.add(hm3);
		hotelMealList.add(hm4);
		hotelPlPreferentialRoom.setHotelMealList(hotelMealList);
		preferentialRoomList.add(hotelPlPreferentialRoom);
		
		
		
		HotelPlPreferentialRoom hotelPlPreferentialRoom1 = new HotelPlPreferentialRoom();
		hotelPlPreferentialRoom1.setUuid("关联酒店房型uuid");
		hotelPlPreferentialRoom1.setHotelUuid("关联的酒店uuid");
		hotelPlPreferentialRoom1.setHotelRoomUuid("酒店房型uuid");
		hotelPlPreferentialRoom1.setHotelRoomText("酒店房型显示文本");
		hotelPlPreferentialRoom1.setNights(3);
		
		List<HotelMeal> hotelMealList1 = new ArrayList<HotelMeal> ();
		HotelMeal hm21 = new HotelMeal();
		hm21.setMealName("BB");
		hm21.setUuid("餐型的UUID");
		HotelMeal hm22 = new HotelMeal();
		hm22.setMealName("HB");
		hm22.setUuid("餐型的UUID");
		HotelMeal hm23 = new HotelMeal();
		hm23.setMealName("FB");
		hm23.setUuid("餐型的UUID");
		HotelMeal hm24 = new HotelMeal();
		hm24.setMealName("AL");
		hm24.setUuid("餐型的UUID");
		hotelMealList1.add(hm21);
		hotelMealList1.add(hm22);
		hotelMealList1.add(hm23);
		hotelMealList1.add(hm24);
		hotelPlPreferentialRoom1.setHotelMealList(hotelMealList1);
		
		preferentialRoomList.add(hotelPlPreferentialRoom1);
		
		List<SysCompanyDictView> islandWayList = new ArrayList<SysCompanyDictView>();//上岛方式集合
		SysCompanyDictView sdv1 = new SysCompanyDictView();
		sdv1.setUuid("上岛方式uuid");
		sdv1.setLabel("水飞");
		SysCompanyDictView sdv2 = new SysCompanyDictView();
		sdv2.setUuid("上岛方式uuid");
		sdv2.setLabel("内飞");
		islandWayList.add(sdv1);
		islandWayList.add(sdv2);
		
		
		List<HotelPlPreferentialRel> hotelPlPreferentialRels = new ArrayList<HotelPlPreferentialRel>();//酒店价单优惠关联集合
		HotelPlPreferentialRel hotelPlPreferentialRel1 = new HotelPlPreferentialRel();
		hotelPlPreferentialRel1.setRelHotelPlPreferentialUuid("关联的优惠信息UUID1");
		hotelPlPreferentialRel1.setRelHotelPlPreferentialName("关联的优惠信息名称");
		HotelPlPreferentialRel hotelPlPreferentialRel2 = new HotelPlPreferentialRel();
		hotelPlPreferentialRel2.setRelHotelPlPreferentialUuid("关联的优惠信息UUID2");
		hotelPlPreferentialRel2.setRelHotelPlPreferentialName("关联的优惠信息名称");
		HotelPlPreferentialRel hotelPlPreferentialRel3 = new HotelPlPreferentialRel();
		hotelPlPreferentialRel3.setRelHotelPlPreferentialUuid("关联的优惠信息UUID3");
		hotelPlPreferentialRel3.setRelHotelPlPreferentialName("关联的优惠信息名称");
		HotelPlPreferentialRel hotelPlPreferentialRel4 = new HotelPlPreferentialRel();
		hotelPlPreferentialRel4.setRelHotelPlPreferentialUuid("关联的优惠信息UUID4");
		hotelPlPreferentialRel4.setRelHotelPlPreferentialName("关联的优惠信息名称");
		hotelPlPreferentialRels.add(hotelPlPreferentialRel1);
		hotelPlPreferentialRels.add(hotelPlPreferentialRel2);
		hotelPlPreferentialRels.add(hotelPlPreferentialRel3);
		hotelPlPreferentialRels.add(hotelPlPreferentialRel4);
		
		HotelPlPreferentialMatter matter = new HotelPlPreferentialMatter();//事项
		
		matter.setPreferentialTemplatesText("住宿优惠");
		matter.setPreferentialTemplatesDetailText("住： 5  晚  免：1  晚");
		matter.setPreferentialTemplatesUuid("优惠模板uuid");
		matter.setMemo("XXXXXXXXXXXXXXXXXXX");
		
		Map<String,List<HotelPlPreferentialTax>> preferentialTaxMap = new HashMap<String,List<HotelPlPreferentialTax>>();
		List<HotelPlPreferentialTax> preferentialTax1List = new ArrayList<HotelPlPreferentialTax>();
		preferentialTaxMap.put("1", preferentialTax1List);
		
		List<HotelPlPreferentialTax> preferentialTax2List = new ArrayList<HotelPlPreferentialTax>();
		preferentialTaxMap.put("2", preferentialTax2List);
		
		List<HotelPlPreferentialTax> preferentialTax3List = new ArrayList<HotelPlPreferentialTax>();
		preferentialTaxMap.put("3", preferentialTax3List);
		
		
		HotelPlPreferentialTax hpt111 = new HotelPlPreferentialTax();
		hpt111.setTravelerTypeUuid("游客类型uuid");
		hpt111.setTravelerTypeText("成人");
		hpt111.setPreferentialType(1);
		hpt111.setPreferentialTypeText("打折");
		hpt111.setPreferentialAmount(50d);
		hpt111.setChargeType(1);
		hpt111.setChargeTypeText("%");
		hpt111.setIslandWayUuids("上岛方式uuids");
		hpt111.setIslandWayText("");
		hpt111.setHotelMealUuids("酒店餐型uuids");
		hpt111.setHotelMealText("");
		hpt111.setIstax("1");
		hpt111.setIstaxText("政府税，服务税，床税");
		hpt111.setType(1);
		hpt111.setUuid("税金uuid");
		
		HotelPlPreferentialTax hpt222 = new HotelPlPreferentialTax();
		hpt222.setTravelerTypeUuid("游客类型uuid");
		hpt222.setTravelerTypeText("儿童");
		hpt222.setPreferentialType(1);
		hpt222.setPreferentialTypeText("减金额");
		hpt222.setPreferentialAmount(200d);
		hpt222.setChargeType(1);
		hpt222.setChargeTypeText("$");
		hpt222.setIslandWayUuids("上岛方式uuids");
		hpt222.setIslandWayText("");
		hpt222.setHotelMealUuids("酒店餐型uuids");
		hpt222.setHotelMealText("");
		hpt222.setIstax("1");
		hpt222.setIstaxText("服务税，床税");
		hpt222.setType(1);
		hpt222.setUuid("税金uuid");
		
		HotelPlPreferentialTax hpt333 = new HotelPlPreferentialTax();
		hpt333.setTravelerTypeUuid("游客类型uuid");
		hpt333.setTravelerTypeText("婴儿");
		hpt333.setPreferentialType(1);
		hpt333.setPreferentialTypeText("减最低");
		hpt333.setPreferentialAmount(20d);
		hpt333.setChargeType(1);
		hpt333.setChargeTypeText("$");
		hpt333.setIslandWayUuids("上岛方式uuids");
		hpt333.setIslandWayText("");
		hpt333.setHotelMealUuids("酒店餐型uuids");
		hpt333.setHotelMealText("");
		hpt333.setIstax("1");
		hpt333.setIstaxText("床税");
		hpt333.setType(1);
		hpt333.setUuid("税金uuid");
		
		preferentialTax1List.add(hpt111);
		preferentialTax1List.add(hpt222);
		preferentialTax1List.add(hpt333);
		
		HotelPlPreferentialTax hpt211 = new HotelPlPreferentialTax();
		hpt211.setTravelerTypeUuid("游客类型uuid");
		hpt211.setTravelerTypeText("成人");
		hpt211.setPreferentialType(1);
		hpt211.setPreferentialTypeText("打折");
		hpt211.setPreferentialAmount(50d);
		hpt211.setChargeType(1);
		hpt211.setChargeTypeText("%");
		hpt211.setIslandWayUuids("上岛方式uuids");
		hpt211.setIslandWayText("水飞,内飞");
		hpt211.setHotelMealUuids("酒店餐型uuids");
		hpt211.setHotelMealText("");
		hpt211.setIstax("1");
		hpt211.setIstaxText("政府税，服务税，床税");
		hpt211.setType(2);
		hpt211.setUuid("税金uuid");
		
		HotelPlPreferentialTax hpt212 = new HotelPlPreferentialTax();
		hpt212.setTravelerTypeUuid("游客类型uuid");
		hpt212.setTravelerTypeText("儿童");
		hpt212.setPreferentialType(1);
		hpt212.setPreferentialTypeText("减金额");
		hpt212.setPreferentialAmount(200d);
		hpt212.setChargeType(1);
		hpt212.setChargeTypeText("$");
		hpt212.setIslandWayUuids("上岛方式uuids");
		hpt212.setIslandWayText("水飞");
		hpt212.setHotelMealUuids("酒店餐型uuids");
		hpt212.setHotelMealText("");
		hpt212.setIstax("1");
		hpt212.setIstaxText("服务税，床税");
		hpt212.setType(2);
		hpt212.setUuid("税金uuid");
		
		HotelPlPreferentialTax hpt213 = new HotelPlPreferentialTax();
		hpt213.setTravelerTypeUuid("游客类型uuid");
		hpt213.setTravelerTypeText("婴儿");
		hpt213.setPreferentialType(1);
		hpt213.setPreferentialTypeText("减最低");
		hpt213.setPreferentialAmount(20d);
		hpt213.setChargeType(1);
		hpt213.setChargeTypeText("$");
		hpt213.setIslandWayUuids("上岛方式uuids");
		hpt213.setIslandWayText("内飞");
		hpt213.setHotelMealUuids("酒店餐型uuids");
		hpt213.setHotelMealText("");
		hpt213.setIstax("1");
		hpt213.setIstaxText("床税");
		hpt213.setType(2);
		hpt213.setUuid("税金uuid");
		
		preferentialTax2List.add(hpt211);
		preferentialTax2List.add(hpt212);
		preferentialTax2List.add(hpt213);
		

		HotelPlPreferentialTax hpt311 = new HotelPlPreferentialTax();
		hpt311.setTravelerTypeUuid("游客类型uuid");
		hpt311.setTravelerTypeText("成人");
		hpt311.setPreferentialType(1);
		hpt311.setPreferentialTypeText("打折");
		hpt311.setPreferentialAmount(50d);
		hpt311.setChargeType(1);
		hpt311.setChargeTypeText("%");
		hpt311.setIslandWayUuids("上岛方式uuids");
		hpt311.setIslandWayText("水飞,内飞");
		hpt311.setHotelMealUuids("酒店餐型uuids");
		hpt311.setHotelMealText("");
		hpt311.setIstax("1");
		hpt311.setIstaxText("政府税，服务税，床税");
		hpt311.setType(2);
		hpt311.setUuid("税金uuid");
		
		HotelPlPreferentialTax hpt312 = new HotelPlPreferentialTax();
		hpt312.setTravelerTypeUuid("游客类型uuid");
		hpt312.setTravelerTypeText("儿童");
		hpt312.setPreferentialType(1);
		hpt312.setPreferentialTypeText("减金额");
		hpt312.setPreferentialAmount(200d);
		hpt312.setChargeType(1);
		hpt312.setChargeTypeText("$");
		hpt312.setIslandWayUuids("上岛方式uuids");
		hpt312.setIslandWayText("水飞");
		hpt312.setHotelMealUuids("酒店餐型uuids");
		hpt312.setHotelMealText("");
		hpt312.setIstax("1");
		hpt312.setIstaxText("服务税，床税");
		hpt312.setType(2);
		hpt312.setUuid("税金uuid");
		
		HotelPlPreferentialTax hpt313 = new HotelPlPreferentialTax();
		hpt313.setTravelerTypeUuid("游客类型uuid");
		hpt313.setTravelerTypeText("婴儿");
		hpt313.setPreferentialType(1);
		hpt313.setPreferentialTypeText("减最低");
		hpt313.setPreferentialAmount(20d);
		hpt313.setChargeType(1);
		hpt313.setChargeTypeText("$");
		hpt313.setIslandWayUuids("上岛方式uuids");
		hpt313.setIslandWayText("内飞");
		hpt313.setHotelMealUuids("酒店餐型uuids");
		hpt313.setHotelMealText("");
		hpt313.setIstax("1");
		hpt313.setIstaxText("床税");
		hpt313.setType(2);
		hpt313.setUuid("税金uuid");
		
		preferentialTax3List.add(hpt311);
		preferentialTax3List.add(hpt312);
		preferentialTax3List.add(hpt313);
		
		matter.setPreferentialTaxMap(preferentialTaxMap);
		
		
		HotelPlPreferentialRequire require = new HotelPlPreferentialRequire();//要求
		require.setUuid("优惠信息模板要求uuid");
		require.setBookingNights(5);
		require.setBookingNumbers(10);
		require.setNotApplicableDate("2015.01.01");
		require.setNotApplicableRoom("不适用房型uuids");
		require.setNotApplicableRoomName("不适用房型名称");
		require.setApplicableThirdPerson(1);
		require.setApplicableThirdPersonText("是");
		require.setIsSuperposition(1);
		require.setIsSuperpositionText("允许");
		require.setMemo("XXXXXXXXXXXXXXXXXXXXXXXX");
		
		HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel = new HotelPlPreferentialRelHotel();//关联酒店
		hotelPlPreferentialRelHotel.setUuid("关联酒店信息uuid");
		hotelPlPreferentialRelHotel.setHotelUuid("关联的酒店UUID");
		hotelPlPreferentialRelHotel.setHotelText("关联酒店的显示文本");
		List<SysCompanyDictView> islandWayList1 = new ArrayList<SysCompanyDictView>();//上岛方式集合
		SysCompanyDictView sdv11 = new SysCompanyDictView();
		sdv11.setUuid("上岛方式uuid");
		sdv11.setLabel("水飞");
		SysCompanyDictView sdv12 = new SysCompanyDictView();
		sdv12.setUuid("上岛方式uuid");
		sdv12.setLabel("内飞");
		islandWayList1.add(sdv11);
		islandWayList1.add(sdv12);
		hotelPlPreferentialRelHotel.setIslandWayList(islandWayList1);
		
		HotelPlPreferential hotelPlPreferential = new HotelPlPreferential();
		
		hotelPlPreferential.setUuid("酒店优惠模板uuid");
		hotelPlPreferential.setPreferentialName("优惠信息名称");
		hotelPlPreferential.setBookingCode("下单代码");
		
		hotelPlPreferential.setHotelUuid("酒店uuid");
		hotelPlPreferential.setUuid("酒店价单优惠模板uuid");
		hotelPlPreferential.setHotelPlUuid("酒店价单uuid");
		hotelPlPreferential.setInDate(new Date());
		hotelPlPreferential.setOutDate(new Date());
		hotelPlPreferential.setBookingStartDate(new Date());
		hotelPlPreferential.setBookingEndDate(new Date());
		
		hotelPlPreferential.setIsRelation(1);
		
		hotelPlPreferential.setPreferentialRoomList(preferentialRoomList);
		
		hotelPlPreferential.setIslandWayList(islandWayList);
		hotelPlPreferential.setHotelPlPreferentialRels(hotelPlPreferentialRels);
//		hotelPlPreferential.setHotelPlPreferentialTaxs(hotelPlPreferentialTaxs);
		hotelPlPreferential.setMatter(matter);
		hotelPlPreferential.setRequire(require);
		hotelPlPreferential.setHotelPlPreferentialRelHotel(hotelPlPreferentialRelHotel);
		
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlPreferential, "yyyy-MM-dd"));
	}
	
	public HotelPlPreferential getHotelPlPreferential() {
		return hotelPlPreferential;
	}
	public void setHotelPlPreferential(HotelPlPreferential hotelPlPreferential) {
		this.hotelPlPreferential = hotelPlPreferential;
	}
	
}

