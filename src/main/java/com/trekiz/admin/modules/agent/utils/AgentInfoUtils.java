package com.trekiz.admin.modules.agent.utils;

import com.trekiz.admin.agentToOffice.agentInfo.service.CustomerTypeService;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AgentInfoUtils {
	
	private static AgentinfoDao agentinfoDao = SpringContextHolder.getBean(AgentinfoDao.class);
	private static QuauqAgentInfoService quauqAgentInfoService = SpringContextHolder.getBean(QuauqAgentInfoService.class);
	private static CustomerTypeService customerTypeService = SpringContextHolder.getBean(CustomerTypeService.class);
	public static final String CACHE_AGENT_LIST = "agentList";
	public static final String CACHE_AGENT_MAP = "agentMap";
	public static final String CACHE_AGENT_LIST_MAP = "agentListMap";
	public static final String CACHE_QUAUQ_AGENT_LIST_MAP = "quauqAgentListMap";
	public static final String CACHE_QUAUQ_OWN_AGENT_LIST_MAP = "quauqAndOwnAgentListMap";
	public static final String CACHE_QUAUQ_AGENT_PARENT_LIST = "agentParentList";
	public static final String CACHE_QUAUQ_AGENT_TYPE_LIST = "customerTypeList";

	/**
	 * 从缓存获取批发商列表
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
	@SuppressWarnings("unchecked")
	public static List<Agentinfo> getAgentList() {
		List<Agentinfo> agentinfoList = (List<Agentinfo>) getCache(CACHE_AGENT_LIST);
		if (agentinfoList == null) {
			DetachedCriteria dc = agentinfoDao.createDetachedCriteria();
			dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.desc("id"));
			agentinfoList = agentinfoDao.find(dc);
			putCache(CACHE_AGENT_LIST, agentinfoList);
		}
		return agentinfoList;
	}
	
	/**
	 * 获取渠道列表，根据 agentFirstLetter 排序
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
	public static List<Agentinfo> getAgentListOrderByFirstLetter() {
		DetachedCriteria dc = agentinfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("agentFirstLetter"));
		List<Agentinfo> agentinfoList = agentinfoDao.find(dc);
		putCache(CACHE_AGENT_LIST, agentinfoList);
		return agentinfoList;
	}
	
	/**
	 * 根据批发商id获取对应渠道：如果缓存中无信息则查询数据库
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
    @SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getAgentList(Long companyId) {
        List<Map<String, Object>> agentinfoList = (List<Map<String, Object>>) getCache(CACHE_AGENT_LIST_MAP);
        if (agentinfoList == null) {
            String sql = "SELECT id, agentName FROM agentinfo where delFlag = 0 and supplyId = ? ORDER BY id";
            agentinfoList = agentinfoDao.findBySql(sql, Map.class, companyId);
            putCache(CACHE_AGENT_LIST_MAP, agentinfoList);
        }
        return agentinfoList;
    }

	/**
	 * 获取QUAUQ渠道总社的名称和id
	 * @return
	 * @author yudong.xu 2016.9.18
	 */
	public static List<Map<String,Object>> getQuauqAgent(){
		List<Map<String, Object>> quauqAgentList = (List<Map<String, Object>>) getCache(CACHE_QUAUQ_AGENT_LIST_MAP);
		if (quauqAgentList == null){
			String sql = "SELECT a.id,a.agentName FROM agentinfo a WHERE a.is_quauq_agent=1" +
					" AND a.delFlag='0' AND a.agent_parent=-1";
			quauqAgentList = agentinfoDao.findBySql(sql.toString(),Map.class);
			putCache(CACHE_QUAUQ_AGENT_LIST_MAP, quauqAgentList);
		}
		return quauqAgentList;
	}
    
	/**
	 * 获取渠道（包括该批发商下的自用渠道和所有已启用的quauq渠道）
	 * @return
	 * @author yang.wang 2016.11.2
	 */
	public static List<Map<String, Object>> getQuauqAndOwnAgentList(Long companyId) {
		List<Map<String, Object>> quauqAndOwnAgentList = (List<Map<String, Object>>) getCache(CACHE_QUAUQ_OWN_AGENT_LIST_MAP);
		if (quauqAndOwnAgentList == null) {
			String sql = "SELECT tt.id, tt.agentName FROM " +
						 "( SELECT a.id, a.agentName, a.delFlag FROM agentinfo a WHERE a.supplyId = ? " +
						 "UNION " +
						 "SELECT b.id, b.agentName, b.delFlag FROM agentinfo b " +
						 "WHERE b.is_quauq_agent = 1 AND b.enable_quauq_agent = 1 ) tt " +
						 "WHERE tt.delFlag = 0 ORDER BY tt.id";
			quauqAndOwnAgentList = agentinfoDao.findBySql(sql, Map.class, companyId);
			putCache(CACHE_QUAUQ_OWN_AGENT_LIST_MAP, quauqAndOwnAgentList);
		}
		return quauqAndOwnAgentList;
	}
	
    /**
	 * 根据批发商ID获取渠道信息（经过agentFirstLetter排序且只有id与name）
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
    public static List<Agentinfo> getAgentListAddSort() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		//获取渠道商列表
        List<Agentinfo> agentinfos = agentinfoDao.findStockAgentinfo(companyId);
        //目标渠道商（由于太重，只存id和name）
        List<Agentinfo> targetAgents = new LinkedList<Agentinfo>();
        Agentinfo myAgentinfo = new Agentinfo();
        myAgentinfo.setId(-1L);
        myAgentinfo.setAgentName("非签约渠道");
        targetAgents.add(myAgentinfo);
        if (CollectionUtils.isNotEmpty(agentinfos)) {
            for (Agentinfo agentinfo : agentinfos) {
                Agentinfo myAgentinfo2 = new Agentinfo();
                myAgentinfo2.setId(agentinfo.getId());
                myAgentinfo2.setAgentName(agentinfo.getAgentName());
                targetAgents.add(myAgentinfo2);
            }
        }
        return targetAgents;
	}
    
    /**
     * 根据渠道ID查询渠道名称
     * @author yakun.bai
     * @Date 2016-5-17
     */
	@SuppressWarnings("unchecked")
	public static String getAgentName(Long id) {
		String agentName = null;
		Map<Long, String> agentNameMap = (Map<Long, String>) getCache(CACHE_AGENT_MAP);
		if (agentNameMap == null || agentNameMap.get(id) == null) {
			Agentinfo agentinfo = agentinfoDao.findAgentInfoById(id);
			if (agentinfo != null) {
				agentNameMap = new HashMap<Long, String>();
				agentName = agentinfo.getAgentName();
				agentNameMap.put(agentinfo.getId(), agentName);
				putCache(CACHE_AGENT_MAP, agentNameMap);
			}
		} else {
			agentName = agentNameMap.get(id);
		}
		return agentName;
	}
	
	/**
	 * 根据ID获取渠道
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
	public static Agentinfo getAgent(String id) {
		if (StringUtils.isNotBlank(id) && !"0".equals(id)) {
			 Agentinfo agentinfo = agentinfoDao.findAgentInfoById(Long.valueOf(id));
			 if (agentinfo != null) {
				  return agentinfo;
			 }
		}
		return null;
	}
	
	/**
	 * 获取缓存中信息
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
	private static Object getCache(String cacheAgentList) {
		return UserUtils.getCache(cacheAgentList);
	}
	
	/**
	 * 把信息放到缓存 
	 * @author yakun.bai
	 * @Date 2016-5-17
	 */
	private static void putCache(String cacheAgentList, Object obj) {
		UserUtils.putCache(cacheAgentList, obj);
	}
	
	/**
	 * 依据id获取渠道
	 * @author yakun.bai
	 * @Date 2016-6-8
	 */
	public static Agentinfo getAgentById(Long agentId) {
		Agentinfo returnAgentinfo = new Agentinfo();
		if (agentId == null) {
			return returnAgentinfo;
		}
		returnAgentinfo = agentinfoDao.getById(agentId);
		
		return returnAgentinfo;
	}
	
	/**
	 * 对渠道进行排序
	 * @param brandName  品牌名称
	 * @param list  同一品牌下的渠道商列表
	 * @return
	 */
	public static List<Map<String, Object>> orderByAgentName(final String brandName,List<Map<String, Object>> list){
		Collections.sort(list,new Comparator<Map<String, Object>>(){  
            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {  
            	Map<String, Object> o1 = (Map<String, Object>) arg0;
            	Map<String, Object> o2 = (Map<String, Object>) arg1;
            	String loginStatus1 = o1.get("loginStatus").toString();
            	String loginStatus2 = o2.get("loginStatus").toString();
            	
            	//获取在线状态loginStatus
            	if(loginStatus1.equals(loginStatus2)){
            		//获取str1 的 首字母
            		String first =ChineseToEnglish.getFirstLetter(o1.get("agentName").toString().replace(brandName, ""));
            		//获取str2的首字母
            		String second = ChineseToEnglish.getFirstLetter(o2.get("agentName").toString().replace(brandName, ""));
            		//比较首字母大小
            		int isCract1 = 32; 
            		if(first.length()>0){
            			Integer.valueOf(first.charAt(0));
            		}
            		int isCract2 = 32;
            		if(second.length()>0){
            			Integer.valueOf(second.charAt(0));
            		}
            		
            		if((64<isCract1&&isCract1<91)||(96<isCract1&&isCract1<123)){
            			isCract1 = isCract1-127;
            		}
            		if((64<isCract2&&isCract2<91)||(96<isCract2&&isCract2<123)){
            			isCract2 = isCract2-127;
            		}
            		return isCract1-isCract2;
            	}else{
            		return loginStatus2.compareTo(loginStatus1);
            	}
            }  
        });
		return list;
	}
	/**
	 * 获取上下级的信息，并把他存入cache
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getagentParentListFromCache(){
		
		List<Map<String, String>> agentParentList = (List<Map<String, String>>)getCache(CACHE_QUAUQ_AGENT_PARENT_LIST);
		if( agentParentList == null || agentParentList.isEmpty()){
			agentParentList = quauqAgentInfoService.getAgentParentList((long) -2);
			putCache(CACHE_QUAUQ_AGENT_PARENT_LIST, agentParentList);
		}
		return agentParentList;
	}
	
	/**
	 * 获取渠道类型的信息，并把它存入cache
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getTypePropertyFromCache(){
		List<Map<String, Object>> customerTypeList = (List<Map<String, Object>>)getCache(CACHE_QUAUQ_AGENT_TYPE_LIST);
		if(customerTypeList == null || customerTypeList.isEmpty()){
			customerTypeList = customerTypeService.getCustomerTypeList4Select();
		}
		return customerTypeList;
	}
	
	/**
	 * 更新渠道上下级关系下拉选项缓存（当添加或修改类型为非门店类型的渠道时调用此方法，更新缓存）
	 * @author wangyang 2016.12.20
	 * */
	public static void updateAgentParentListCache() {
		List<Map<String,String>> agentParentList = quauqAgentInfoService.getAgentParentList((long) -2);
		if (agentParentList != null) {
			putCache(CACHE_QUAUQ_AGENT_PARENT_LIST, agentParentList);
		}
	}
}