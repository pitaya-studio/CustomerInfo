/**
 * 
 */
package com.trekiz.admin.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.repository.AirportInfoDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author 李鑫朝
 * 
 */
@Service
public class AirportService extends BaseService {

	@Autowired
	private AirportInfoDao airportInfoDao;

	/**
	 * 根据机场Id查询机场信息，返回Map对象， key：机场Id， value：机场信息对象
	 * 
	 * @param airportIdList
	 * @return
	 */
	public Map<Long, AirportInfo> queryAirportInfos(List<Long> airportIdList) {
		Map<Long, AirportInfo> airportMap = new HashMap<Long, AirportInfo>();
		List<AirportInfo> airportInfoList = (List<AirportInfo>) this.airportInfoDao
				.findAll(airportIdList);
		if (null == airportInfoList || airportInfoList.size() == 0) {
			return airportMap;
		}

		for (AirportInfo aInfo : airportInfoList) {
			airportMap.put(aInfo.getId(), aInfo);
		}

		return airportMap;
	}

	public List<AirportInfo> queryAirportInfos() {
		return (List<AirportInfo>) this.airportInfoDao.findAll();
	}
	
	public List<AirportInfo> queryAirport(Long companyId) {
		return (List<AirportInfo>) this.airportInfoDao.queryAirport(companyId);
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param companyId 批发商ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public Page searchAirportPage(Page<AirportInfo> page,
			Long companyId, String area) {
		
		String sql = "select t2.name as country_name,t3.name as city_name,t1.airport_name,t1.airport_code,t1.user_mode,t1.id from sys_airport_info t1,sys_area t2,sys_area t3 where (t1.country_id = t2.id and t2.type = '2' and t2.delFlag = '0') and (t1.city_id = t3.id and t3.type = '4' and t3.delFlag = '0') and (t1.del_flag = '0' and t1.area = ? and company_id = ?)";
		return airportInfoDao.findBySql(page, sql, List.class,area, companyId);
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
				sbRes.append(airportInfo.get(5));
				sbRes.append(",");
			}

			if(sbRes.length() > 0){
				sbRes.deleteCharAt(sbRes.length() - 1);
			}
		}

		return sbRes.toString();
	}
	
	public void save(AirportInfo airportInfo){
		airportInfoDao.save(airportInfo);
	}
	
	/**
	 * 保存显示状态
	 * @param currencyIdArr 页面所有的ID数组
	 * @param checkedIdArr 选中的ID数组
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void saveDispStatus(String[] airportIdArr,String[] checkedIdArr){
		List<String> cancelIdLst = new ArrayList<String>();
		
		for(String airportId : airportIdArr){
			boolean existFlg = false;
			for(String checkedId : checkedIdArr){
				if(airportId.equals(checkedId)){
					existFlg = true;
					break;
				}
			}
			
			if(!existFlg){
				cancelIdLst.add(airportId);
			}
		}
		
		AirportInfo airportInfo = null;
		for(String checkedId : checkedIdArr) {
			airportInfo = airportInfoDao.findOne(Long.parseLong(checkedId));
			if(airportInfo != null){
				// 显示（checked状态）
				airportInfo.setUserMode(new Integer(AirportInfo.SHOW));
				airportInfoDao.save(airportInfo);
			}
		}
		for(String cancelId : cancelIdLst) {
			airportInfo = airportInfoDao.findOne(Long.parseLong(cancelId));
			if(airportInfo != null){
				// 不显示（unchecked状态）
				airportInfo.setUserMode(new Integer(AirportInfo.HIDE));
				airportInfoDao.save(airportInfo);
			}
		}
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
	public List<AirportInfo> checkSameName(long companyId, int areaId, Long countryId, Long cityId, String airportName){
		return airportInfoDao.checkSameName(companyId, areaId, countryId, airportName);
	}
	
	/**
	 * 检查相同区域下的同名机场三字码
	 * @param companyId
	 * @param areaId
	 * @param countryId
	 * @param cityId
	 * @param airportName
	 * @return
	 */
	public List<AirportInfo> checkSameName(long companyId, String airportCode, Long airportId){
		if(airportId == null || airportId == -1){
			return airportInfoDao.checkSameAirportCode(companyId, airportCode);	
		}
		return airportInfoDao.checkSameAirportCode(companyId, airportCode, airportId);
	}
	
	/**
	 * 取得前台页面显示的机场信息
	 * @return
	 */
	public List<Integer> findDispAirportInfo(String area) {
		String sql = "select t1.id from sys_airport_info t1,sys_area t2,sys_area t3 where (t1.country_id = t2.id and t2.type = '2' and t2.delFlag = '0') and (t1.city_id = t3.id and t3.type = '4' and t3.delFlag = '0') and (t1.del_flag = '0' and t1.area = ? and company_id = ?) and t1.user_mode = ?";
		List<Integer> list = airportInfoDao.findBySql(sql, area, UserUtils
				.getUser().getCompany().getId(), AirportInfo.SHOW);
		return list;
	}
	
	/**
	 * 删除机场信息
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void deleteAirport(Long id) {
		airportInfoDao.deleteById(id);
	}
	
	/**
	 * 由机场ID取得机场信息
	 * @param id
	 * @return
	 */
    public AirportInfo getAirportInfo(Long id){
    	return airportInfoDao.findOne(id);
    }
}
