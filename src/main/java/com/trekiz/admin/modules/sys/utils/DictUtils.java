/**
 *
 */
package com.trekiz.admin.modules.sys.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.TransferMap;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.repository.SysCompanyDictViewDao;
import com.trekiz.admin.modules.sys.repository.SysDefineDictDao;
import com.trekiz.admin.modules.sys.repository.UserDefineDictDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.OfficeService;

/**
 * 字典工具类
 * @author zj
 * @version 2013-11-19
 */
public class DictUtils {
	
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
	private static SysCompanyDictViewDao sysCompanyDictViewDao = SpringContextHolder.getBean(SysCompanyDictViewDao.class);
	/**
	 * 自定义词典Dao
	 */
	private static SysDefineDictDao sysDefineDictDao = SpringContextHolder.getBean(SysDefineDictDao.class);
	
	private static UserDefineDictDao userDefineDictDao = SpringContextHolder.getBean(UserDefineDictDao.class);
	private static SysCompanyDictViewService sysCompanyDictViewService = SpringContextHolder.getBean(SysCompanyDictViewService.class);
	private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
	private static AreaService areaService = SpringContextHolder.getBean("areaService");

	public static final String CACHE_DICT_MAP = "dictMap";
	
	public static final String CACHE_DEFINE_DICT_MAP = "defineDictMap";
	
	public static Dict getDict(String value, String type){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict;
				}
			}
		}
		return null;
	}
	
	/**
	 * 由ID值取得相应的Dict对象
	 * @param id
	 * @param type
	 * @return
	 */
	public static Dict getDictById(Long id, String type){
		if (StringUtils.isNotBlank(type) && id != null){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && id.equals(dict.getId())){
					return dict;
				}
			}
		}
		return null;
	}
	
	/**
	 * 由ID取得相应的Label值
	 * @param id
	 * @param type
	 * @return
	 */
	public static String getLabelById(Long id, String type) {
		Dict dict = getDictById(id, type);
		if (dict == null) {
			return "";
		}

		return dict.getLabel();
	}
	
	@SuppressWarnings("all")
	public static String getDepatCity(String id){
		String city = "";
		Map map = new HashMap();
		map.put("id", id);
		List<Map> list = areaService.findAirportCityList2(map);
		if(null!=list&&list.size()>0){
			city = list.get(0).get("name").toString();
		}
		return city;
	}

	public static String getDictLabel(String value, String type, String defaultValue){
		Dict dict = getDict( value,  type);
		if(dict!=null){
			return dict.getLabel();
		}else if("spaceGrade_Type".equals(type) || "airspace_Type".equals(type)){
			return "不限";
		}else {
			return defaultValue;
		}
	}

	public static String getDictLabel2(String value, String type, String defaultValue){
		String sql = "SELECT label FROM sys_dict where value = ? and type = ?";
		List<Map<String, Object>> list = dictDao.findBySql(sql, Map.class, value, type);
		if (list != null && list.size() > 0	) {
			return list.get(0).get("label").toString();
		}
		return defaultValue;

	}
	
	public static String getDictDescription(String value, String type, String defaultValue){
		Dict dict = getDict( value,  type);
		if(dict!=null)return dict.getDescription();
		else return defaultValue;
	}
	
	public static List<String> getFromAreaLabel(List<Dict> dictList) {
		List<String> fromAreaLabelList = null;
		if(dictList!=null) {
			fromAreaLabelList = new ArrayList<String>();
			for(Dict d:dictList)
				fromAreaLabelList.add(d.getLabel());
			
		}
		return fromAreaLabelList;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<Dict> getDictList(String type){
		
		Map<String, List<Dict>> dictMap = Maps.newHashMap();
		List<Dict> dicts = dictDao.findAllListByType(type);
		for (Dict dict : dicts){
			List<Dict> dictList = dictMap.get(dict.getType());
			if (dictList != null){
				dictList.add(dict);
			}else{
				dictMap.put(dict.getType(), Lists.newArrayList(dict));
			}
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	/**
	 * 
	 * @Description: 解决数据量大时慢的问题
	 * @author xinwei.wang
	 * @date 2016年4月9日上午9:58:37
	 * @param type
	 * @return    
	 * @throws
	 */
	public static List<Map<String, Object>>  getDictListValueAndLableByType(String type){
		List<Map<String, Object>>  list = dictDao.findBySql("SELECT sd.`value`,sd.label from sys_dict sd where sd.delFlag='0' and sd.type='"+type+"' order by sd.sort", Map.class);
		return list;
	}
	

	public static List<Map<String, Object>> getDict2List(String type) {
		String sql = "select value, label from sys_dict where type = ? and delFlag = 0 ORDER BY sort";
		return dictDao.findBySql(sql, Map.class, type);
	}
	
	private static Map<String, Map<String,SysDefineDict>> getDefineDictMap(Long companyId){
		Map<Long, Map<String, Map<String,SysDefineDict>>> dictMap = Maps.newHashMap();
		List<SysDefineDict> dicts = sysDefineDictDao.findAllList(companyId);
		for (SysDefineDict dict : dicts){
			Map<String, Map<String,SysDefineDict>> innerMap = dictMap.get(dict.getCompanyId());
			if (innerMap != null){
				Map<String,SysDefineDict> tmpMap = innerMap.get(dict.getType());
				if(tmpMap != null){
					tmpMap.put(dict.getValue(),dict);
				}else{
					tmpMap = Maps.newHashMap();
					tmpMap.put(dict.getValue(),dict);
					innerMap.put(dict.getType(), tmpMap);
				}
			}else{
				innerMap = new HashMap<String, Map<String,SysDefineDict>>();
				Map<String,SysDefineDict> tmpMap = Maps.newHashMap();
				tmpMap.put(dict.getValue(),dict);
				innerMap.put(dict.getType(), tmpMap);
				dictMap.put(dict.getCompanyId(), innerMap);
			}
		}
		return dictMap.get(companyId);
	}
	
	/**
	 * 获取当前用户所属的字典信息(value和label)
	 * */
	public static Map<String,String> getValueAndLabelMap(String type,Long companyId) {
		//判断当前用户的companyId是否定义了默认字典
		Map<String, Map<String,SysDefineDict>> defineMap = getDefineDictMap(companyId);
		if(defineMap==null)return new HashMap<String,String>();
		Map<String,SysDefineDict> dictmap =  defineMap.get(type);
		if(dictmap==null)return new HashMap<String,String>();
		return new TransferMap(dictmap);
	}
	
	/**
	 * 获取指定用户companyId 在自定义字典表中的数据
	 * */
	public static List<SysDefineDict> getDefineDictByCompanyIdAndType(String type, Long companyId) {
		List<SysDefineDict> dicts = sysDefineDictDao.findSysDefineDictByTarvleType(type, companyId);
		if (CollectionUtils.isEmpty(dicts)) {
			dicts = new ArrayList<>();
			dicts.add(new SysDefineDict());
		}
		return dicts;
	}
	
	/**
	 * 获取指定用户companyUuid 在自定义字典表中的数据
	 * */
	public static List<SysDefineDict> getDefineDictByCompanyUuidAndType(String type, String uuid) {
		Office office = officeService.findWholeOfficeByUuid(uuid);
		if (office != null && office.getId() != null) {
			List<SysDefineDict> dicts = sysDefineDictDao.findSysDefineDictByTarvleType(type, office.getId());
			if (CollectionUtils.isEmpty(dicts)) {
				dicts = new ArrayList<>();
				dicts.add(new SysDefineDict());
			}
			return dicts;
		}
		return null;
	}
	
	/**
	 * 获取与航空二字码关联的字典信息 
	 */
	public static StringBuffer getRelevanceFlag(Long companyId) {
		List<SysDefineDict> relevanceFlagList = sysDefineDictDao.findRelevanceFlag(companyId);
		StringBuffer relevanceFlagId = new StringBuffer();
		if(relevanceFlagList.size()!=0){
			for(SysDefineDict s : relevanceFlagList) {
				relevanceFlagId.append(s.getValue()+",");
			}
		}
		if(relevanceFlagId.toString().equals("")){
			return relevanceFlagId;
		}else{
			return new StringBuffer(relevanceFlagId.toString().substring(0, relevanceFlagId.toString().length()-1));
		}
	}
	
	/**
	 * 获取与航空二字码关联的字典信息 
	 */
	public static StringBuffer getRelevanceFlagName(Long companyId) {
		List<SysDefineDict> relevanceFlagList = sysDefineDictDao.findRelevanceFlag(companyId);
		StringBuffer relevanceFlagName = new StringBuffer();
		if(relevanceFlagList.size()!=0){
			for(SysDefineDict s : relevanceFlagList) {
				relevanceFlagName.append(s.getLabel()+",");
			}
		}
		return relevanceFlagName;
	}
	
	
	/**
	 * 获取当前用户所属的字典信息(出发地与航空信息)
	 * */
	public static Map<String,String> findUserDict(Long companyId,String type) {
		Map<String,String> userDictMap = new HashMap<String,String>();
		String sql = "select sa.* from sys_dict sa inner join userdefinedict u  on sa.id = u.dictId where  u.companyId='"+companyId+"' and u.type='"+type+"'";
		List<Dict> userDictList = userDefineDictDao.findBySql(sql,Dict.class);
		if(userDictList.size()!=0){
			for(Dict dict:userDictList) {
				
				userDictMap.put(dict.getValue().toString(),dict.getLabel());
			}
		}
		return userDictMap;
	}
	/**
	 * 获取当前用户字典使用过的编号(value)
	 * 
	 */
	public static StringBuffer getSysdefinedictValueList(String type) {
		List<SysDefineDict> dictList = sysDefineDictDao.findSysDefineDictByTarvleType(type, UserUtils.getUser().getCompany().getId());
		StringBuffer sb = new StringBuffer();
		if(!dictList.isEmpty()) {
			int index = 0;
			int count = 0;
			int[] dictValueArrSort = new int[dictList.size()];
			Map<Integer,String> dictValueMap = new HashMap<Integer,String>();
			for(SysDefineDict dict:dictList) {
				int valueHashCode = dictValueArrSort[index] = dict.getValue().hashCode();
				dictValueMap.put(valueHashCode, dict.getValue());
				++index;
				}
			Arrays.sort(dictValueArrSort);
				for(; count<dictValueMap.size();) {
					String s = dictValueMap.get(dictValueArrSort[count]);
					++count;
					sb.append("<"+s+">&nbsp;");
					if(count%5==0) {
						sb.append("&#10;");	
					}
				}
		}
		return sb;
	}
	
	/**
	 * 根据类型获取字典的key-value映射关系
	 */
	public static Map<String,String> getDicMap(String type){
		Long companyId = UserUtils.getUser().getCompany().getId();
		return getValueAndLabelMap(type,companyId);
	}
		
	/**
	 * 根据类型获取字典的key-value映射关系
	 */
	public static SysDefineDict getDefineDict(String type, String value, Long companyId){
		//判断当前用户的companyId是否定义了默认字典
		Map<String, Map<String,SysDefineDict>> defineMap = getDefineDictMap(companyId);
		if(defineMap==null)return null;
		Map<String,SysDefineDict> dictmap =  defineMap.get(type);
		if(dictmap==null)return null;
		else return dictmap.get(value);
		
	}
	
	/**
	 * @deprecated
	 * 原设计没有defaultflag，所有字典中的记录都是默认值
	 * 新设计中没有了默认值的概念，默认选中用户上次选择的内容
	 * 
	 * 自定义目标区域
	 * 创建人：liangingming   
	 * 创建时间：2014-2-19 下午3:21:22     
	 *
	 */
	public static List<Map<String, Object>> getDefineAreaDatas(){
		
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Long companyId = UserUtils.getUser().getCompany().getId();
		//判断当前用户的companyId是否定义了默认字典
		Map<String, Map<String,SysDefineDict>> defineMap = getDefineDictMap(companyId);
		Map<String,SysDefineDict> defineDictMap = defineMap.get(Context.DEFINE_AREA);
		//目的地
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 1);
		map.put("pId", 0);
		map.put("name", "目的地");
		mapList.add(map);
		if(defineDictMap!=null){
			for(Map.Entry<String,SysDefineDict> item:defineDictMap.entrySet()){
				map = Maps.newHashMap();
				map.put("id", Integer.parseInt(item.getKey()));
				map.put("pId", 1);
				map.put("name", item.getValue().getLabel());
				mapList.add(map);
			}
		}
		return null;
	}
	
	/**
	 * 字典表里value可以转为数字的做Map<Integer, String>处理     
	 * 创建人：liangjingming   
	 * 创建时间：2014-1-27 下午2:50:12     
	 */
	public static Map<Integer, String> getKeyIntMap(String type){
		Map<Integer, String> dicMap = new HashMap<Integer, String>();
		List<Dict> dictList = getDictList(type);
		if(dictList!=null){
			for(Dict dict:dictList){
				dicMap.put(Integer.parseInt(dict.getValue()), dict.getLabel());
			}
		}
		return dicMap;
	}
	
	/**
	 * 自定义字典表里value可以转为数字的做Map<Integer, String>处理     
	 * 创建人：yang.jiang   
	 * 创建时间：2016-3-15 15:18:02     
	 */
	public static Map<Integer, String> getKeyIntMapFromDefinedDict(String type, String companyUuid){
		Map<Integer, String> dicMap = new HashMap<Integer, String>();
		List<SysDefineDict> dictList = DictUtils.getDefineDictByCompanyUuidAndType(type, companyUuid);
		if(dictList != null){
			for(SysDefineDict dict : dictList){
				dicMap.put(Integer.parseInt(dict.getValue()), dict.getLabel());
			}
		}
		return dicMap;
	}
	
	/**
	 *字典表里航空公司二字码对应的航空公司名称 
	 */
	public static Map<String, String> getFlightName() {
		Map<String, String> dicMap = new HashMap<String, String>();
		List<Dict> dictList = getDictList(Context.TRAFFIC_NAME);
		if(dictList!=null){
			for(Dict dict:dictList){
				dicMap.put(dict.getValue(), dict.getDescription());
			}
		}
		return dicMap;
	}
	
	
	/**
	 * 根据字典类型获取系统公共字典缓存
	 * 创建人：liangjingming   
	 * 创建时间：2014-3-20 下午7:59:38     
	 *
	 */
	public static Map<String,String> getSysDicMap(String type){
		Map<String, String> dicMap = new HashMap<String, String>();
		//签证类型时用，按插入顺序显示
		Map<String,String> newMap = new LinkedHashMap<String,String>();  
		String uuid = UserUtils.getUser().getCompany().getUuid();
		
		List<Dict> dictList = getDictList(type);
		if(dictList!=null){
			for(Dict dict:dictList){
				/**
				 *S- 300&301根据不同供应商展示不同的签证类型- S
				 */
				if("new_visa_type".equals(type)){
					if(11>=Integer.parseInt(dict.getValue())){
						newMap.put(dict.getValue(), dict.getLabel());
					}
					//针对越柬行踪、起航假期 供应商的签证类型的不同显示--wenchao.lv
					//起航假期uuid：5c05dfc65cd24c239cd1528e03965021 12---23
					if("5c05dfc65cd24c239cd1528e03965021".equals(uuid)){
						if(12<=Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=23){
							newMap.put(dict.getValue(), dict.getLabel());
						}
					}
					//越柬行踪uuid:7a81b21a77a811e5bc1e000c29cf2586	24-28
					if("7a81b21a77a811e5bc1e000c29cf2586".equals(uuid)){
						if(23<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=28){
							newMap.put(dict.getValue(), dict.getLabel());
						}
					}
					//百乐游uuid：4a39518f8de74baebe6b51efcdd57aa3  29---
					if("4a39518f8de74baebe6b51efcdd57aa3".equals(uuid)){
						if(28<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=38){
							newMap.put(dict.getValue(), dict.getLabel());
						}
					}
					//骡子假期 39-61  0417需求 
					if("980e4c74b7684136afd89df7f89b2bee".equals(uuid)){
						if(38<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=61){
							newMap.put(dict.getValue(), dict.getLabel());
						}
					}
					//鼎鸿假期62-64 0391需求-djw
					if("049984365af44db592d1cd529f3008c3".equals(uuid)){
						if(61<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=64){
							newMap.put(dict.getValue(), dict.getLabel());
						}
					}
				}else{
					dicMap.put(dict.getValue(), dict.getLabel());
				}
			}
		}	
		if("new_visa_type".equals(type)){
			return newMap;
		}else{
			return dicMap;
		}
		
	}
	
	/**
	 * 获取签证类型
	 * @param type
	 * @return
	 */
	public static Map<String,String> getVisaTypeMap(String type){
		Map<String, String> dicMap = new HashMap<String, String>();
		List<Dict> dictList = getDictList(type);
		if(dictList!=null){
			for(Dict dict:dictList){
				dicMap.put(dict.getValue(), dict.getLabel());
			}
		}
		return dicMap;
	}
	
	/**
	 *根据字典类型，获取由label，description组成的字典
	 */
	public static Map<String, String> getLabelDesMap(String type) {
		Map<String, String> dicMap = new HashMap<String, String>();
		List<Dict> dictList = getDictList(type);
		if(dictList!=null){
			for(Dict dict:dictList){
				dicMap.put(dict.getLabel(), dict.getDescription());
			}
		}
		return dicMap;
	}
	/**
	 *从字典里去值
	 *label 
	 *type 
	 *关联用户所在公司
	 */
	@SuppressWarnings("unchecked")
    public static List<Dict> getDictsByType(String label,String type ){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hql= " select dict  from Dict dict ,UserDefineDict udict " +
			  		" where  udict.dictId=dict.id " +
			  		" and  udict.type='"+type+"' " +
			  		" and dict.delFlag='0'" +
			  		" and udict.companyId = " + companyId;
		  if(!StringUtils.isBlank(label)){
			  hql += " and dict.label like '%"+label+"%'";
		  }
		 hql += " ORDER BY dict.sort	";
		 Query query = dictDao.getSession().createQuery(hql);
		return query.list();
	}
	
	/**
	 *从字典视图里取值 add by zhanghao
	 *type 
	 */
    public static Map<String,String> getDictViewsMapByType(String type, String dataScope ){
		Map<String,String> map = new LinkedHashMap<String,String>();
		List<SysCompanyDictView> list = getDictViewsListByType(type, dataScope);
		for(SysCompanyDictView sv : list){
			map.put(sv.getUuid(), sv.getLabel());
		}
		return map;
	}
	
	/**
	 *从字典视图里取值 add by zhanghao
	 *type 
	 */
    public static List<SysCompanyDictView> getDictViewsListByType(String type, String dataScope ){
		SysCompanyDictView sysCompanyDictView = new SysCompanyDictView ();
		sysCompanyDictView.setType(type);
		sysCompanyDictView.setDelFlag("0");
		if("system".equals(dataScope)) {
			sysCompanyDictView.setCompanyId(-1L);
		} else {
			sysCompanyDictView.setCompanyId(getCompanyId());
		}
		List<SysCompanyDictView> list = sysCompanyDictViewService.find(sysCompanyDictView);
		return list;
	}
	private static long getCompanyId(){
		User user = UserUtils.getUser();
		long l = 0;
		if(user==null)return l;
		
		if(user.getId()==1){//判断是否是超级用户
			l =  -1;
		}else{//非超级用户查询本公司的字典列表
			Office company = UserUtils.getUser().getCompany();
        	if(company != null) {
        		l =  company.getId();
        	}
        	
		}
		return l;
    }
	/**
	 * 根据uuid，获取字典表标签
	 * @author gao
	 * @param uuid
	 * @return
	 */
	public static String getDictLabelByUuid(String uuid){
		if(StringUtils.isNotBlank(uuid)){
			Dict dict = dictDao.findLabelByUuid(uuid);
			if(dict!=null && StringUtils.isNotBlank(dict.getLabel())){
				return dict.getLabel();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 从视图sys_company_dict_view中查询
	 * @author jiangyang
	 * @param value
	 * @param type
	 * @return
	 */
	public static SysCompanyDictView getSysCompanyDictView(String value, String type) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (SysCompanyDictView sysCompanyDictView : getSysCompanyDictViewList(type)){
				if (type.equals(sysCompanyDictView.getType()) && value.equals(sysCompanyDictView.getValue())){
					return sysCompanyDictView;
				}
			}
		}
		return null;
	}
	
	/**
	 * @author jiangyang
	 * @param type
	 * @return 从sys_company_dict_view中取
	 */
	public static List<SysCompanyDictView> getSysCompanyDictViewList(String type){
		Map<String, List<SysCompanyDictView>> dictViewMap = Maps.newHashMap();
		List<SysCompanyDictView> dictViews = sysCompanyDictViewDao.findAllDictViewListByType(type);
		for (SysCompanyDictView sysCompanyDictView : dictViews){
			List<SysCompanyDictView> dictList = new ArrayList<>();
			if(sysCompanyDictView != null){
				dictList = dictViewMap.get(sysCompanyDictView.getType());
			}
			if (dictList != null){
				dictList.add(sysCompanyDictView);
			}else{
				dictViewMap.put(sysCompanyDictView.getType(), Lists.newArrayList(sysCompanyDictView));
			}
		}
		List<SysCompanyDictView> dictList = dictViewMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	/**
	 * @author jiangyang
	 * @param type
	 * @return 从sys_company_dict_view中取
	 */
	public static List<SysCompanyDictView> getSysCompanyDictViewListByCmpId(String type, Long companyId){
		Map<String, List<SysCompanyDictView>> dictViewMap = Maps.newHashMap();
		List<SysCompanyDictView> dictViews = sysCompanyDictViewDao.findAllDictViewListByType(type, companyId);
		for (SysCompanyDictView sysCompanyDictView : dictViews){
			List<SysCompanyDictView> dictList = new ArrayList<>();
			if(sysCompanyDictView != null){
				dictList = dictViewMap.get(sysCompanyDictView.getType());
			}
			if (dictList != null){
				dictList.add(sysCompanyDictView);
			}else{
				dictViewMap.put(sysCompanyDictView.getType(), Lists.newArrayList(sysCompanyDictView));
			}
		}
		List<SysCompanyDictView> dictList = dictViewMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	/**
	 * 1.5签证订单收款支付方式查询
	 * @return
	 */
	public static List<Map<String,Object>> getPayType(){
		List<Dict> dictList = dictDao.findDictByPayType();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Dict dict :dictList){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("payType", dict.getValue());
			map.put("payTypeName", dict.getLabel());
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据type查询dict对象
     */
	public static List<Dict> getDictByType(String type){

		if("new_visa_type".equals(type))
		{

			String sql = "";
			if("4a39518f8de74baebe6b51efcdd57aa3".equals(UserUtils.getUser().getCompany().getUuid())) //百乐游
				sql ="SELECT * FROM sys_dict sd WHERE  (sd.value <12  or  (sd.value >28 AND sd.value <=38))  AND sd.type ='new_visa_type' AND sd.delFlag = '0' ORDER BY sd.id";
			if("7a81b21a77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid()))  //越柬行踪
				sql ="SELECT * FROM sys_dict sd WHERE  (sd.value <12  or  (sd.value >23 AND sd.value <=28))  AND sd.type ='new_visa_type' AND sd.delFlag = '0' ORDER BY sd.id";
			if("5c05dfc65cd24c239cd1528e03965021".equals(UserUtils.getUser().getCompany().getUuid()))  	//起航假期
				sql ="SELECT * FROM sys_dict sd WHERE  (sd.value <12  or  (sd.value >=12 AND sd.value <=23))  AND sd.type ='new_visa_type' AND sd.delFlag = '0' ORDER BY sd.id";
			else
				sql="SELECT * FROM sys_dict sd WHERE sd.type ='new_visa_type' AND sd.delFlag = '0' AND sd.value <12 ORDER BY sd.id";
			List<Dict>  list = dictDao.findBySql(sql,Dict.class);
			return list;

		}
		else
		{
			List<Dict> dictList = dictDao.findByType(type);
			return dictList;
		}

	}

	public static boolean isExist(List<String> list,String type) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		for(String str: list){
			if(!str.equals("-1")){
				List<SysDefineDict> li = sysDefineDictDao.findValueIsExist(companyId, type,str);
				if(li == null || li.size()<=0){
					return false;
				}
			}
		}
		return true;
	}

	public static List<Dict> getFromArea4T1(String type) {
		return dictDao.getFromArea4T1(type);
	}
	
	/**
	 * 将jsp中的字符替换(用于解决成本付款的一些小问题)
	 * @param str
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @return
	 * @author chao.zhang
	 */
	public static String getReplace(String str,String arg1,String arg2,String arg3,String arg4){
		String string = str.replace(arg1, arg2).replace(arg3, arg4);
		return string;
	}
	
}
