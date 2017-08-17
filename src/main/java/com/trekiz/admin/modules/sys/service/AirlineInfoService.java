package com.trekiz.admin.modules.sys.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.AirlineInfoDao;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class AirlineInfoService extends BaseService {

	@Autowired
	private AirlineInfoDao airlineInfoDao;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private DictDao dictDao;

	public List<AirlineInfo> getAirlineInfoList(Long companyId) {
		return this.airlineInfoDao.findAirlineInfoList(companyId);
	}

	@SuppressWarnings("rawtypes")
    public List<Map> getAirlineList(Long companyId) {
		return this.airlineInfoDao.getAirlineList(companyId);
	}

	
	/**
	 * 获取仓位
	 * @param companyId
	 * @param airlineCode
	 * @param spaceLevel
	 * @return
	 */
	public Map<String, String> findAirlineInfo_spaceList(Long companyId, String airlineCode,
			String spaceLevel) {
		
		Map<String, String> map = new HashMap<String, String>();

		List<AirlineInfo> list = this.airlineInfoDao.findAirlineInfo_spaceList(
				companyId, airlineCode, spaceLevel);
		for (AirlineInfo airline : list) {
			map.put(airline.getSpace(), DictUtils.getDict(airline.getSpace(),"airspace_Type")!=null?DictUtils.getDict(airline.getSpace(),"airspace_Type").getLabel():"");
		}
		return map;
	}
	
	/**
	 * 获取仓位等级
	 * @param companyId
	 * @param airlineCode
	 * @param spaceLevel
	 * @return
	 */
	public Map<String, String> findAirlineInfo_spaceLevelList(Long companyId, String airlineCode) {
	
		Map<String, String> map = new HashMap<String, String>();
		List<AirlineInfo> list;
		if (StringUtils.isNotBlank(airlineCode)) {
			list = this.airlineInfoDao.findAirlineInfo_spaceLevelList(companyId, airlineCode);
		} else {
			list = this.airlineInfoDao.findAirlineInfoList(companyId);
		}
		for (AirlineInfo airline : list) {
			map.put(airline.getSpaceLevel(), DictUtils.getDict(airline.getSpaceLevel(),"spaceGrade_Type")!=null?DictUtils.getDict(airline.getSpaceLevel(),"spaceGrade_Type").getLabel():"");
			
		}
		return map;
	}
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param companyId 批发商ID
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public Page searchAirlinePage(Page page,
			Long companyId, String area, String airlineNameKeyword) {
		/*String sql = "select t2.name as country_name,t1.airline_name,t1.airline_code,t3.label as space_level,t4.label as space," +
				     "t1.user_mode,t1.id,t1.flight_number,substr(t1.departure_time,12,5),substr(t1.arrival_time,12,5), " +
				     "t1.company_id,t1.area "+
				     "from sys_airline_info t1,sys_area t2,sys_dict t3,sys_dict t4 " +
				     "where t1.space_level = t3.value and t3.type = 'spaceGrade_Type' " +
				     "and t3.delFlag = '0' and t1.space = t4.value and t4.type = 'airspace_Type' " +
				     "and t4.delFlag = '0' and t1.country_id = t2.id and t2.delFlag = 0 " +
				     "and t2.type = '2' and t1.del_flag = 0 and t1.area = "+area+" and t1.company_id = "+companyId;*/
		//modify by hhx 2015-8-10
		StringBuffer sb = new StringBuffer("select t1.country_id,t1.airline_name,t1.airline_code,t1.space_level,t1.space," +
									     "t1.user_mode,t1.id,t1.flight_number,substr(t1.departure_time,12,5),substr(t1.arrival_time,12,5), " +
									     "t1.company_id,t1.area "+
									     "from sys_airline_info t1 where t1.del_flag = 0 and t1.area = "+area+" and t1.company_id = "+companyId);
		if(StringUtils.isNotBlank(airlineNameKeyword)){
			sb.append(" and (t1.airline_name like '%" +airlineNameKeyword +"%' or " +
					" t1.airline_code like '%"+airlineNameKeyword+"%' or" +
					" t1.flight_number like '%"+airlineNameKeyword+"%')");
		}
		return airlineInfoDao.findBySql(page, sb.toString(), List.class);
	}
	
	/**
	 * 取得LIST对象中的ID，并拼接成字符串 
	 * @param list
	 * @return
	 */
	public String getIdFromListObj(List<List<Object>> list) {
		StringBuffer sbRes = new StringBuffer();
		if (list != null && list.size() != 0) {
			for (List<Object> airportInfo : list) {
				sbRes.append(airportInfo.get(6));
				sbRes.append(",");
			}

			if(sbRes.length() > 0){
				sbRes.deleteCharAt(sbRes.length() - 1);
			}
		}

		return sbRes.toString();
	}
	
	/**
	 * 保存显示状态
	 * @param currencyIdArr 页面所有的ID数组
	 * @param checkedIdArr 选中的ID数组
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void saveDispStatus(String[] airlineIdArr,String[] checkedIdArr){
		List<String> cancelIdLst = new ArrayList<String>();
		
		for(String airlineId : airlineIdArr){
			boolean existFlg = false;
			for(String checkedId : checkedIdArr){
				if(airlineId.equals(checkedId)){
					existFlg = true;
					break;
				}
			}
			
			if(!existFlg){
				cancelIdLst.add(airlineId);
			}
		}
		
		AirlineInfo airlineInfo = null;
		for(String checkedId : checkedIdArr) {
			airlineInfo = airlineInfoDao.findOne(Long.parseLong(checkedId));
			if(airlineInfo != null){
				// 显示（checked状态）
				airlineInfo.setUserMode(new Integer(AirportInfo.SHOW));
				airlineInfoDao.save(airlineInfo);
			}
		}
		for(String cancelId : cancelIdLst) {
			airlineInfo = airlineInfoDao.findOne(Long.parseLong(cancelId));
			if(airlineInfo != null){
				// 不显示（unchecked状态）
				airlineInfo.setUserMode(new Integer(AirportInfo.HIDE));
				airlineInfoDao.save(airlineInfo);
			}
		}
	}
	
	public void save(AirlineInfo airlineInfo){
		airlineInfoDao.save(airlineInfo);
	}
	
	/**
	 * 检查相同区域下的同名机场名称
	 * @param companyId
	 * @param areaId
	 * @param countryId
	 * @param cityId
	 * @param airportName
	 * @return
	 */
	public List<AirlineInfo> checkSameName(long companyId, int areaId, Long countryId, String airportName){
		return airlineInfoDao.checkSameName(companyId, areaId, countryId, airportName);
	}
	
	/**
	 * 检查相同区域下的同名航空公司二字码
	 * @param companyId
	 * @param areaId
	 * @param countryId
	 * @param cityId
	 * @param airportName
	 * @return
	 */
	public List<AirlineInfo> checkSameAirlineCode(long companyId, int areaId, Long countryId, String airlineName, String airlineCode){
		return airlineInfoDao.checkSameAirlineCode(companyId, areaId, countryId, airlineCode,airlineName);
	}
	
	/**
	 * 取得前台页面显示的信息
	 * @return
	 */
	public List<Integer> findDispAirlineInfoInfo(Long companyId, String area, String airlineNameKeyword) {
		/*String sql = "select t1.id from sys_airline_info t1," +
				     "sys_area t2,sys_dict t3,sys_dict t4 " +
				     "where t1.space_level = t3.value and t3.type = 'spaceGrade_Type' and t3.delFlag = '0' " +
				     "and t1.space = t4.value and t4.type = 'airspace_Type' " +
				     "and t4.delFlag = '0' and t1.country_id = t2.id " +
				     "and t2.delFlag = 0 and t2.type = '2' and t1.del_flag = 0 " +
				     "and t1.area = "+area+" and t1.company_id = "+companyId;*/
		String sql = "select t1.id from sys_airline_info t1 where t1.area = ? and t1.company_id = ? and t1.del_flag = ?";
		List<Integer> list = airlineInfoDao.findBySql(sql,area,companyId,BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}
	
	/**
	 * 保存航空的舱位等级
	 * 
	 * @param airportName
	 * @param spaceLevelArr
	 */
	public List<String> saveAirlineSpaceLevel(Long companyId,
			Integer airlineArea, Long countryId, String airlineName,
			String airlineCode, String[] spaceLevelArr) {

		List<String> spaceLvlList = new ArrayList<String>();
		
		int maxSpaceLvl = this.getMaxDictVal("spaceGrade_Type");

		for (int i = 0; i < spaceLevelArr.length; i++) {
			String spaceLevel = spaceLevelArr[i];
			
			String spaceLevelId = this.getSpaceLevelId(companyId, airlineArea,
					countryId, airlineName, airlineCode, spaceLevel);
			if (StringUtils.isNotBlank(spaceLevelId)) {
				spaceLvlList.add(spaceLevelId);
				continue;
			}

			Dict dict = new Dict();
			// 标签名
			dict.setLabel(spaceLevel);
			// 数据值
			String spaceLvlId = (maxSpaceLvl + i + 1) + "";
			dict.setValue(spaceLvlId);
			spaceLvlList.add(spaceLvlId);
			// type
			dict.setType("spaceGrade_Type");
			// 描述
			dict.setDescription(airlineName + "的舱位等级");
			// 排序
			dict.setSort(new Integer(spaceLvlId));
			// 保存
			dictService.save(dict);
		}

		return spaceLvlList;
	}
	
	/**
	 * 保存航空的舱位 add by hhx
	 * @param space
	 * @param airlineName
	 */
	public String saveAirlineSpace(String space,String airlineName,Integer area,String airlineCode) {
		String space_value = getSpaceValue(area,airlineCode,space);
		if(StringUtils.isNotBlank(space_value)){
			return space_value;
		}
		int maxSpace = getMaxDictVal("airspace_Type");
		Dict dict = new Dict();
		// 标签名
		dict.setLabel(space.toUpperCase());
		// 数据值
		String shipGradeId = (maxSpace + 1) + "";
		dict.setValue(shipGradeId);
		// type
		dict.setType("airspace_Type");
		// 描述
		dict.setDescription(airlineName + "的舱位");
		// 排序
		dict.setSort(new Integer(shipGradeId));
		// 保存
		dictService.save(dict);
		return shipGradeId;
	}
	
	public int getMaxDictVal(String type) {
		String sql = "select t.value from sys_dict t where t.type = '" + type
				+ "'";
		List<String> list = dictDao.findBySql(sql);

		List<Integer> intLst = new ArrayList<Integer>();
		for (String str : list) {
			try {
				intLst.add(new Integer(str));
			} catch (Exception e) {
				intLst.add(0);
			}
		}
		Collections.sort(intLst);

		if (intLst.size() > 0) {
			return intLst.get(intLst.size() - 1);
		}

		return 0;
	}
	
	public String getSpaceLevelId(Long companyId, Integer airlineArea,
			Long countryId, String airlineName, String airlineCode,
			String spaceLevel) {
		String sql = "select DISTINCT t1.space_level from sys_airline_info t1,sys_dict t2 "
				+ " where t1.space_level = t2.value and t2.type= 'spaceGrade_Type'"
				+ " and t1.company_id = '"
				+ companyId
				+ "'"
				+ " and t1.area = '"
				+ airlineArea
				+ "'"
				+ " and t1.country_id = '"
				+ countryId
				+ "'"
				+ " and t1.airline_name = '"
				+ airlineName
				+ "'"
				+ " and t1.airline_code = '"
				+ airlineCode
				+ "' "
				+ " and t2.label = '" + spaceLevel + "'";
		List<String> list = dictDao.findBySql(sql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return "";
	}
	
	/**
	 * 查找当前仓位是否已存在数据字典返回字典value值   add by hhx
	 * @param area
	 * @param airlineCode
	 * @param space
	 * @return
	 */
	public String getSpaceValue(Integer area,String airlineCode,String space) {
		String sql = "select DISTINCT t1.space from sys_airline_info t1,sys_dict t2 "
				+ " where t1.space = t2.value and t2.type= 'airspace_Type'"
				+ " and t1.company_id = '" + UserUtils.getUser().getCompany().getId() + "'"
				+ " and t1.area = '" + area + "'"
				+ " and t1.airline_code = '" + airlineCode + "' "
				+ " and t2.label = '" + space + "'";
		List<String> list = dictDao.findBySql(sql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return "";
	}
	
	public boolean checkSameSpace(Long companyId, Integer airlineArea,
			Long countryId, String airlineName,
			String airlineCode, String spaceLevel, String space) {
		String sql = "select t1.airline_name from sys_airline_info t1,sys_dict t2,sys_dict t3 "
				+ " where t1.space_level = t2.value and t2.type= 'spaceGrade_Type'"
				+ " and t1.space = t3.value and t3.type = 'airspace_Type'"
				+ " and t1.company_id = '"
				+ companyId
				+ "'"
				+ " and t1.area = '"
				+ airlineArea
				+ "'"
				+ " and t1.country_id = '"
				+ countryId
				+ "'"
				+ " and t1.airline_name = '"
				+ airlineName
				+ "'"
				+ " and t1.airline_code = '"
				+ airlineCode
				+ "'"
				+ " and t2.label = '"
				+ spaceLevel
				+ "'"
				+ " and t3.label = '"
				+ space + "'";
		List<String> list = dictDao.findBySql(sql);
		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}
	
	/**
	 * 根据航班号获取仓位等级 add by wangXk
	 * @param companyId
	 * @param flightnumber
	 * @param spaceLevel
	 * @return
	 */
	public Map<String, String> findSpaceLevelByFlightNumber(Long companyId, String flightnumber) {
	
		Map<String, String> map = new HashMap<String, String>();
		List<AirlineInfo> list = this.airlineInfoDao.findSpaceLevelByFlightNumber(companyId, flightnumber);
		for (AirlineInfo airline : list) {
			map.put(airline.getSpaceLevel(), DictUtils.getDict(airline.getSpaceLevel(),"spaceGrade_Type")!=null?DictUtils.getDict(airline.getSpaceLevel(),"spaceGrade_Type").getLabel():"");
			
		}
		return map;
	}
	/**
	 * 根据航空公司的airlindeCode获取航班号 add by wangXk
	 * @param companyId
	 * @param flightnumber
	 * @param spaceLevel
	 * @return
	 */
	public Map<String, String> findFlightNumberById(Long companyId, String airlindeCode) {
	
		Map<String, String> map = new HashMap<String, String>();
		List<AirlineInfo> list = this.airlineInfoDao.findAirlineInfo_spaceLevelList(companyId, airlindeCode);
		for (AirlineInfo airline : list) {
			map.put(airline.getAirlineCode(), airline.getFlightnumber());
			
		}
		return map;
	}
	/**
	 * 根据供货商id,返回唯一的航空公司列表add by hhx
	 * @param companyId
	 * @param airlineCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<AirlineInfo> getDistinctAirline(Long companyId){
		List<Map> oldList = airlineInfoDao.getAirlineList(companyId);
		List<AirlineInfo> newList = new ArrayList<AirlineInfo>();
		for(Object o:oldList){
			AirlineInfo airline = new AirlineInfo();
			airline.setAirlineCode((String) ((Object[]) o)[0]);
			airline.setAirlineName((String) ((Object[]) o)[1]);
			newList.add(airline);
		}
		return newList;
	}
	/**
	 * 根据供货商id,航空公司简码返回唯一的航班号列表add by hhx
	 * @param companyId
	 * @param airlineCode
	 * @return
	 */
	public List<AirlineInfo> getDistinctFlightNum(Long companyId, String airlineCode){
		List<Object> oldList = airlineInfoDao.getDistinctFlightNum(companyId,airlineCode);
		List<AirlineInfo> newList = new ArrayList<AirlineInfo>();
		for(Object o:oldList){
			AirlineInfo airline = new AirlineInfo();
			airline.setAirlineCode((String) ((Object[]) o)[0]);
			airline.setAirlineName((String) ((Object[]) o)[1]);
			airline.setFlightnumber((String) ((Object[]) o)[2]);
			airline.setDeparturetime((Date) ((Object[]) o)[3]);
			airline.setArrivaltime((Date) ((Object[]) o)[4]);
			airline.setDayNum((Integer) ((Object[]) o)[5]);
			newList.add(airline);
		}
		return newList;
	}
	/**
	 * 根据供货商id,航空公司简码,航班号返回唯一的舱位等级列表add by hhx
	 * @param companyId
	 * @param airlineCode
	 * @param flightnum
	 * @return
	 */
	public List<AirlineInfo> getDistinctSpaceLevel(Long companyId, String airlineCode,String flightnum){
		List<Object> oldList = airlineInfoDao.getDistinctSpaceLevel(companyId, airlineCode, flightnum);
		List<AirlineInfo> newList = new ArrayList<AirlineInfo>();
		for(Object o:oldList){
			AirlineInfo airline = new AirlineInfo();
			airline.setAirlineCode((String) ((Object[]) o)[0]);  //二字码 
			airline.setAirlineName((String) ((Object[]) o)[1]);  //航空公司名字
			airline.setFlightnumber((String) ((Object[]) o)[2]); //航班号
			airline.setSpaceLevel((String) ((Object[]) o)[3]);   //舱位等级
			Dict dict = DictUtils.getDict((String) ((Object[]) o)[3], "spaceGrade_Type");
			airline.setSpace(dict==null?"":dict.getLabel());//舱位等级对用的中文名
			newList.add(airline);
		}
		return newList;
	}

	public List<AirlineInfo> getByAirlineCode(Long id, int area,
			String airlineCode) {
		
		//return airlineInfoDao.getByAirlineCode(id,area,airlineCode);
		return airlineInfoDao.findAirlineInfo_spaceLevelList(id,airlineCode);
	}
	
	/**
	 * 新增航空公司信息保存
	 * @param req
	 */
	public void saveAirlineInfo(HttpServletRequest req) {
		Integer area = Integer.parseInt(req.getParameter("v_area"));
		Long country = StringUtils.isBlank(req.getParameter("v_country"))?null:Long.parseLong(req.getParameter("v_country"));
		String airlineName = req.getParameter("v_airlineName");
		String airlineCode = req.getParameter("v_airlineCode");
		String flightnumber = req.getParameter("v_flightnumber");
		String departuretime = req.getParameter("v_departuretime");
		String arrivaltime = req.getParameter("v_arrivaltime");
		String dayNum = req.getParameter("v_dayNum");
		String shipName = req.getParameter("v_shipName");
		String shipGrade = req.getParameter("v_shipGrade");
		Long companyId = UserUtils.getUser().getCompany().getId();
		String shipCompany[] = shipGrade.split(";");
		int shipCompanyLen = shipCompany.length;
		for(int i=0;i<shipCompanyLen;i++){
			String shipFlight[] = shipCompany[i].split(",");
			int shipFlightLen = shipFlight.length;
			String vAirlineName = airlineName.split(";")[i];
			String vAirlineCode = airlineCode.split(";")[i];
			for(int j=0;j<shipFlightLen;j++){
				String spaceLevel[] = shipFlight[j].split("_");
				int levelLen = spaceLevel.length;
				String vFlightnumber = flightnumber.split(";")[i].split(",")[j];
				String vDeparturetime = departuretime.split(";")[i].split(",")[j];
				String vArrivaltime = arrivaltime.split(";")[i].split(",")[j];
				String vDayNum = dayNum.split(";")[i].split(",")[j];
				for(int k= 0;k<levelLen;k++){
					String space[] = spaceLevel[k].split("!");
					int spaceLen = space.length;
					//在这里将仓位等级保存到字典
					String vShipName = shipName.split(";")[i].split(",")[j].split("_")[k];
					List<String> spaceLevelDict = new ArrayList<String>();
					if(StringUtils.isNotBlank(vShipName) && !"$".equals(vShipName)){
						spaceLevelDict = saveAirlineSpaceLevel(companyId, area,country, vAirlineName, vAirlineCode,new String[] { vShipName });
					}
					for(int l=0;l<spaceLen;l++){
						AirlineInfo airInfo = new AirlineInfo();
						airInfo.setArea(area);
						airInfo.setCountryId(country);
						airInfo.setUserMode(1);
						airInfo.setCompanyId(companyId);
						airInfo.setAirlineName("$".equals(vAirlineName)?null:vAirlineName);
						airInfo.setAirlineCode("$".equals(vAirlineCode)?null:vAirlineCode.toUpperCase());
						airInfo.setFlightnumber("$".equals(vFlightnumber)?null:vFlightnumber );
						airInfo.setDeparturetime(DateUtils.string2Date("$".equals(vDeparturetime)?null:vDeparturetime,"HH:mm"));
						airInfo.setArrivaltime(DateUtils.string2Date("$".equals(vArrivaltime)?null:vArrivaltime,"HH:mm"));
						airInfo.setDayNum("$".equals(vDayNum)?null:Integer.parseInt(vDayNum));
						airInfo.setSpaceLevel(CollectionUtils.isEmpty(spaceLevelDict)?null:spaceLevelDict.get(0));
						//在这里将仓位保存到字典
						String spaceDict = "";
						if(StringUtils.isNotBlank(space[l]) && !"$".equals(space[l])){
							spaceDict = saveAirlineSpace(space[l],vAirlineName,area,vAirlineCode);
						}
						airInfo.setSpace(spaceDict);
						save(airInfo);
					}
				}
			}
		}
	}
	
	/**
	 * 批量删除航空公司信息
	 * @param req
	 */
	public void deleteAirline(HttpServletRequest req){
		Integer area = Integer.parseInt(req.getParameter("v_area"));
		String airlineCode = req.getParameter("v_airlineCode_old");
		List<AirlineInfo> airlinelist = airlineInfoDao.getByAirlineCode(
						UserUtils.getUser().getCompany().getId(),area, airlineCode);
		for(AirlineInfo info:airlinelist){
			info.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		}
		airlineInfoDao.batchUpdate(airlinelist);
    }
	
	/**
	 * 显示航空公司详情 add by hhx
	 * @param airlineCode
	 * @param companyId
	 * @param area
	 * @return
	 */
	public Map<String, Map<String,List<Map<String,String>>>> getAirlineListGroupBySpaceLevel(String airlineCode,Long companyId,String area) {
		try {
			airlineCode = new String(airlineCode.getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String sb = "SELECT concat_ws(',',sai.area,sai.country_id,sai.airline_name,sai.airline_code,sai.flight_number," +
				    "SUBSTR(sai.departure_time,12,5),SUBSTR(sai.arrival_time,12,5),sai.day_num) as flightNumber," +
			        "sai.space_level as spaceLevel," +
			        "sai.space as space FROM sys_airline_info sai " +
	                "WHERE airline_code = ? and company_id = ? and area = ? and del_flag = ? ";
		List<Map<String, String>> list = airlineInfoDao.findBySql(sb.toString(), Map.class,airlineCode,companyId,area,BaseEntity.DEL_FLAG_NORMAL);
		Map<String, Map<String,List<Map<String,String>>>> flight = new TreeMap<String, Map<String,List<Map<String,String>>>>();
		for(Map<String, String> map:list){
			String flightValue = map.get("flightNumber")==null?"":map.get("flightNumber");
			String spaceLevelValue = map.get("spaceLevel")==null?"":map.get("spaceLevel");
			if(!flight.containsKey(flightValue)){
				flight.put(flightValue, new TreeMap<String,List<Map<String,String>>>());
			}
			if(!flight.get(flightValue).containsKey(spaceLevelValue)){
				flight.get(flightValue).put(spaceLevelValue, new ArrayList<Map<String,String>>());
			}
			flight.get(flightValue).get(spaceLevelValue).add(map);
		}
		return flight;
	}
}
