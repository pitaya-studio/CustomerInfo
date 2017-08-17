package com.trekiz.admin.modules.t1.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.District;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.DistrictService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.t1.service.IT1Service;

/**
 * Created by zzk on 2016/10/9.
 * T1控制器
 */
@Controller
@RequestMapping(value = "${adminPath}/t1")
public class T1Controller {

    @Autowired
    private IT1Service t1Service;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private DictService dictService;
    @Autowired
    private TouristLineService touristLineService;
    @Autowired
    private TravelActivityService travelActivityService;
    @Autowired
    private AgentinfoService agentinfoService;

    /**
     * T1登录之后新首页
     * @return
     */
    @RequestMapping(value={"newHome"})
    public String newHome() {
        return "modules/homepage/newHome";
    }

    /**
     * 获取区域
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value={"getDistricts"})
    public String getDistricts(HttpServletRequest request) {
        String tourOutIn = checkNull(request.getParameter("tourOutIn")); // 出境游/国内游
        String tourDistrictId = checkNull(request.getParameter("tourDistrictId")); // 区域id
        String flag = checkNull(request.getParameter("flag")); // flag：是否使用区域id
        if ("".equals(tourOutIn)) {
            tourOutIn = Context.FREE_TRAVEL_FOREIGN;
        }

        List<Map<String, Object>> list = null;
        list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("id", Context.FREE_TRAVEL_FOREIGN);
        m1.put("name", "出境游");
        list.add(m1);

        Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("id", Context.FREE_TRAVEL_INLAND);
        m2.put("name", "国内游");
        list.add(m2);

        // 组织数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tourOutIn", tourOutIn);
        map.put("tourOutInList", list);
        List<Map<String, Object>> t1LogoList = t1Service.getT1LogoList(tourOutIn, tourDistrictId, flag);
        map.put("tourDistrict", t1LogoList);

        // 返回结果Map
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("results", map);
        // 转换成json
        String json = JSON.toJSONString(results);
        return json;
    }

    /**
     * 获取区域
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value={"getSuppliers"})
    public String getSuppliers(HttpServletRequest request) {
        String tourOutIn = checkNull(request.getParameter("tourOutIn")); // 出境游/国内游
        String tourDistrictId = checkNull(request.getParameter("tourDistrictId")); // 区域id
        String flag = checkNull(request.getParameter("flag")); // flag：是否使用区域id
        if ("".equals(tourOutIn)) {
            tourOutIn = Context.FREE_TRAVEL_FOREIGN;
        }

        // 组织数据
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> t1LogoList = t1Service.getT1LogoList(tourOutIn, tourDistrictId, flag);
        handleSupplier(t1LogoList, tourOutIn);
        map.put("tourDistrict", t1LogoList);

        // 返回结果Map
        Map<String, Object> results = new HashMap<String, Object>();
//        results.put("results", map);
        results = handleSupplier(t1LogoList, tourOutIn);
        // 转换成json
        String json = JSON.toJSONString(results);
        return json;
    }

    private Map<String, Object> handleSupplier(List<Map<String, Object>> t1LogoList, String tourOutIn) {
        for (int i = 0; i < t1LogoList.size();) {
            Map<String, Object> map =  t1LogoList.get(i);
            String tourDistrictId = map.get("tourDistrictId").toString(); // 区域id
            List<Map<String, Object>> suppliers = t1Service.getSuppliers(tourDistrictId, tourOutIn);
            map.put("travelAgency", suppliers);
            return map;
        }
        return null;
    }

    /**
     * 供应商logo读取
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value={"getT1LogoList"})
    public String getT1LogoList(HttpServletRequest request) {
        String tourOutIn = checkNull(request.getParameter("tourOutIn")); // 出境游/国内游
        String tourDistrictId = checkNull(request.getParameter("tourDistrictId")); // 区域id
        String flag = checkNull(request.getParameter("flag")); // flag：是否使用区域id
        if ("".equals(tourOutIn)) {
            tourOutIn = Context.FREE_TRAVEL_FOREIGN;
        }
        // 组织数据
        Map<String, Object> map = new HashMap<String, Object>();
        //从缓存中读取T1首页境外和国内数据
        List<Map<String, Object>> t1LogoList = new ArrayList<Map<String, Object>>();
        if(StringUtils.isNotBlank(tourOutIn)) {
            //境外
            if(tourOutIn.equals(Context.FREE_TRAVEL_FOREIGN)) {
                Map<String, List<Map<String, Object>>> foreighMap = (Map<String, List<Map<String, Object>>> )CacheUtils.get(Context.T1HOMEFOREIGNCACHE);
                if(foreighMap != null) {
                    t1LogoList = foreighMap.get("tourForeignDistrict");
                    map.put("tourOutIn", foreighMap.get("tourOutIn"));
                    map.put("tourOutInList", foreighMap.get("tourOutInList"));
                }
            }else { //国内
                Map<String, List<Map<String, Object>>> inlandMap = (Map<String, List<Map<String, Object>>>)CacheUtils.get(Context.T1HOMEINLANDCACHE);
                if(inlandMap != null){
                    t1LogoList = (List<Map<String, Object>>) inlandMap.get("tourInlandDistrict");
                    map.put("tourOutIn", inlandMap.get("tourOutIn"));
                    map.put("tourOutInList", inlandMap.get("tourOutInList"));
                }
            }
        }
        //服务启动时，如果没有从缓存中拿出首页数据，则将SQL查询结果集合放入缓存中
        if(t1LogoList.size() == 0) {
            if (tourOutIn.equals(Context.FREE_TRAVEL_FOREIGN) && CacheUtils.get(Context.T1HOMEFOREIGNBEFORE) != null) {
                t1LogoList = (List<Map<String, Object>>) CacheUtils.get(Context.T1HOMEFOREIGNBEFORE);
            } else if(tourOutIn.equals(Context.FREE_TRAVEL_INLAND) && CacheUtils.get(Context.T1HOMEINLANDBEFORE) != null) {
                t1LogoList = (List<Map<String, Object>>) CacheUtils.get(Context.T1HOMEINLANDBEFORE);
            } else {
                t1LogoList = t1Service.getT1LogoList(tourOutIn, tourDistrictId, flag);
                handle(t1LogoList, tourOutIn);
                if(tourOutIn.equals(Context.FREE_TRAVEL_FOREIGN)) {
                    CacheUtils.put(Context.T1HOMEFOREIGNBEFORE, t1LogoList);
                }else {
                    CacheUtils.put(Context.T1HOMEINLANDBEFORE, t1LogoList);
                }
            }
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            //境外
            Map<String, Object> m1 = new HashMap<String, Object>();
            m1.put("id", Context.FREE_TRAVEL_FOREIGN);
            m1.put("name", Context.FREE_TRAVEL_FOREIGN_CHINA);
            list.add(m1);
            //国内
            Map<String, Object> m2 = new HashMap<String, Object>();
            m2.put("id", Context.FREE_TRAVEL_INLAND);
            m2.put("name", Context.FREE_TRAVEL_INLAND_CHINA);
            list.add(m2);
            map.put("tourOutIn", tourOutIn);
            map.put("tourOutInList", list);
        }
        map.put("tourDistrict", t1LogoList);
        // 暂时策略，待删除
        map.put("lingxianwangshuai", UserUtils.getUser().getLingxianwangshuai());
        // 返回结果Map
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("results", map);
        // 转换成json
        String json = JSON.toJSONString(results);
        return json;
    }

    /**
     * 添加批发商信息
     * @param t1LogoList
     * @param tourOutIn
     */
    private void handle(List<Map<String, Object>> t1LogoList, String tourOutIn) {
        for (int i = 0; i < t1LogoList.size(); i++) {
            Map<String, Object> map =  t1LogoList.get(i);
            String tourDistrictId = map.get("tourDistrictId").toString(); // 区域id
            List<Map<String, Object>> suppliers = t1Service.getSuppliers(tourDistrictId, tourOutIn);
            map.put("travelAgency", suppliers);
        }
    }

    @ResponseBody
    @RequestMapping(value={"homePageList"})
    public String homePageList(HttpServletRequest request, HttpServletResponse response) {
        
    	Map<String, Object> paramsMap = getConditionMap(request); // 获取条件

        // 列表数据
        Page<Map<String, Object>> page = t1Service.queryList(request, new Page<Map<String, Object>>(request, response), paramsMap);
        handlePage(page); // 处理返回结果

        // 结果数据组装成Map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", page.getPageNo()); // 页码
        map.put("pageSize", page.getPageSize()); // 每页显示条数
        map.put("count", page.getCount()); // 共计多少条数据
        map.put("first", page.getFirst()); // 第一页
        int last = Double.valueOf(Math.ceil((double)page.getCount() / page.getPageSize())).intValue();
        map.put("last", last); // 最后一页
        map.put("firstPage", page.isFirstPage()); // 是否是首页
        map.put("lastPage", page.isLastPage()); // 是否是尾页
        map.put("list", page.getList()); // 列表数据

        // 排序数据
        String orderBy = page.getOrderBy();
        String[] orderByArr = orderBy.split(" ");
        String direction = orderByArr[1];
        String property = orderByArr[0];
        Map<String, Object> orderByMap = new HashMap<String, Object>();
        orderByMap.put("direction", direction);
        orderByMap.put("property", property);

        List<Map<String, Object>> orderByList = Lists.newArrayList();
        orderByList.add(orderByMap);
        map.put("sort", orderByList);

        // 返回结果Map
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("page", map);
        // 转换成json
        String json = JSON.toJSONStringWithDateFormat(results, "yyyy-MM-dd");
        return json;
    }
    
    @ResponseBody
    @RequestMapping(value={"homePageActivityList"})
    public String homePageActivityList(HttpServletRequest request, HttpServletResponse response) {
    	
        Map<String, Object> paramsMap = getConditionMap(request); // 获取条件

        // 列表数据
        Page<Map<String,Object>> page = t1Service.queryActivityList(request, new Page<Map<String, Object>>(request, response), paramsMap);
        handleActivityPage(page); // 处理返回结果
        
        // 结果数据组装成Map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", page.getPageNo()); // 页码
        map.put("pageSize", page.getPageSize()); // 每页显示条数
        map.put("count", page.getCount()); // 共计多少条数据
        map.put("list", page.getList()); // 列表数据
        int last = Double.valueOf(Math.ceil((double)page.getCount() / page.getPageSize())).intValue();
        map.put("last", last); // 最后一页

        // 排序数据
        String orderBy = page.getOrderBy().replace("defaultSort IS NULL,", "");
        String[] orderByArr = orderBy.split(" ");
        String direction = orderByArr[1];
        String property = orderByArr[0];
        Map<String, Object> orderByMap = new HashMap<String, Object>();
        orderByMap.put("direction", direction);
        orderByMap.put("property", property);

        List<Map<String, Object>> orderByList = Lists.newArrayList();
        orderByList.add(orderByMap);
        map.put("sort", orderByList);

        // 返回结果Map
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("page", map);
        // 转换成json
        String json = JSON.toJSONStringWithDateFormat(results, "yyyy-MM-dd");
        return json;
    }
    
    /**
     * 获取前台查询条件集合
     * @param request
     * @return
     */
	private Map<String, Object> getConditionMap(HttpServletRequest request) {
    	String params = request.getParameter("params");
        JSONObject m = JSON.parseObject(params);
        String keyword = checkNull(m.get("keywordQ")); //搜索关键字
        String tourOutIn = checkNull(m.get("tourOutInQ")); // 出境游/国内游
        String travelAreaId = checkNull(m.get("travelAreaIdQ")); // 区域id
        String pageNo = checkNull(m.get("pageNo")); // 当前页码（点击的页码）
        String pageSize = checkNull(m.get("pageSize")); // 每页显示记录数
        String orderBy = checkNull(m.get("orderBy")); // 排序

        // 出发城市
        String startCityPara = checkNull(m.get("startCityParaQ"));
        // 目的地
        String endCityPara = checkNull(m.get("endCityParaQ"));
        // 抵达城市
        String targetCity = checkNull(m.get("targetCity"));
        // 线路玩法
        String linePlay = checkNull(m.get("linePlay"));
        // 供应商
        String supplierPara = checkNull(m.get("supplierParaQ"));
        // 出团日期
        String groupDatePara = checkNull(m.get("groupDateParaQ"));
        // 行程天数
        String dayPara = checkNull(m.get("dayParaQ"));
        // 价格
        String pricePara = checkNull(m.get("priceParaQ"));
        // 余位
        String freePara = checkNull(m.get("freeParaQ"));

        // 封装参数
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("keyword", keyword);
        paramsMap.put("tourOutIn", tourOutIn);
        paramsMap.put("travelAreaId", travelAreaId);
        paramsMap.put("startCityParas", startCityPara);
        paramsMap.put("endCityParas", endCityPara);
        paramsMap.put("targetCitys", targetCity);
        paramsMap.put("linePlays", linePlay);
        paramsMap.put("supplierParas", supplierPara);
        paramsMap.put("groupDateParas", groupDatePara);
        paramsMap.put("dayParas", dayPara);
        paramsMap.put("priceParas", pricePara);
        paramsMap.put("freeParas", freePara);
        paramsMap.put("pageNo", pageNo);
        paramsMap.put("pageSize", pageSize);
        paramsMap.put("orderBy", orderBy);
        
        return paramsMap;
    }
    
    /**
     * 处理产品列表返回结果
     * @param page
     */
	private void handleActivityPage(Page<Map<String, Object>> page) {
    	for (Map<String, Object> m : page.getList()) {
    		BigDecimal quauqSettlePrice = null; // 供应价
    		BigDecimal settlementPrice = null;  // 同行价
    		
			if (m.get("quauqPrice") != null) {
				quauqSettlePrice = new BigDecimal(m.get("quauqPrice").toString());
    		}
			if (m.get("settleAdultPrice") != null) {
				settlementPrice = new BigDecimal(m.get("settleAdultPrice").toString());
			}

            // 同行价
//			if (settlementPrice != null) {
//				m.put("settlementPrice", m.get("quauqCurrencyMark").toString() + settlementPrice);
//			} else { // 供应价为null时,可能会导致同行价为null,此时取产品所有团期中最低同行价
//				if (m.get("minSettlementAdultPrice") != null) {
//					m.put("settlementPrice", m.get("quauqCurrencyMark").toString() + m.get("minSettlementAdultPrice"));
//				} else {
//					m.put("settlementPrice", "");
//				}
//			}
			
			// 供应价
			if (quauqSettlePrice != null) {
				if (quauqSettlePrice.compareTo(new BigDecimal(0)) < 1) { // 供应价不大于0时,对应的供应价,同行价设为空
				    m.put("quauqSettlePrice", "");
				    m.put("quauqCurrencyMark", "");
				    m.put("settlementPrice", "");
				}else{
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					m.put("quauqSettlePrice", decimalFormat.format(quauqSettlePrice));// 供应价
					m.put("settlementPrice", m.get("quauqCurrencyMark").toString() + decimalFormat.format(settlementPrice)); // 同行价
				}
			} else {
				m.put("quauqSettlePrice", "");
				m.put("quauqCurrencyMark", "");
				m.put("settlementPrice", ""); // 同行价
			}
            
            // 产品的出团日期超过4个只显示4个
            String[] dateArray = {};
            StringBuffer sb = new StringBuffer();
            if(m.get("groupOpenDate") != null){
            	dateArray = m.get("groupOpenDate").toString().split(",");
            }
            if(dateArray.length > 4){
            	for (int i = 0; i < 4; i++) {
            		sb.append(dateArray[i]).append(" , ");
				}
            	String groupOpenDate = sb.toString().substring(0, sb.length() - 3);
            	m.put("groupOpenDate", groupOpenDate);
            	m.put("dateSizeFlag", 1);
            }
            
            // 产品中最近出团日期的团期id
            String[] groupIdArray = {};
			if (m.get("nearestGroupIds") != null) {
            	groupIdArray = m.get("nearestGroupIds").toString().split(",");
				if (groupIdArray.length > 0) {
					m.put("groupId", groupIdArray[0]);
				} else {
					m.put("groupId", "");
				}
            }
            
            // 批发商认证详情
			if (m.get("officeId") != null) {
            	long officeId = Long.parseLong(m.get("officeId").toString());
            	Office office = officeService.get(officeId);
				if (office != null) {
            		// 资质证书
            		List<DocInfo> businessCertificate = docInfoService.getDocInfoByStringIds(";",office.getBusinessCertificate());
            		if(businessCertificate != null && businessCertificate.size() >0){
            			List<Map<String ,Object>> list = new ArrayList<>();
            			for (DocInfo docInfo : businessCertificate) {
            				Map<String, Object> map = new HashMap<>();
							map.put("id", docInfo.getId());
							map.put("name", docInfo.getDocName());
							list.add(map);
						}
            			m.put("businessCertificate", list);
            		}
            		// 营业执照
            		List<DocInfo> businessLicense = docInfoService.getDocInfoByStringIds(";",office.getBusinessLicense());
            		if(businessLicense != null && businessLicense.size() >0){
            			List<Map<String ,Object>> list = new ArrayList<>();
            			for (DocInfo docInfo : businessLicense) {
            				Map<String, Object> map = new HashMap<>();
							map.put("id", docInfo.getId());
							map.put("name", docInfo.getDocName());
							list.add(map);
						}
            			m.put("businessLicense", list);
            		}
            		// 合作协议
            		List<DocInfo> cooperationProtocol = docInfoService.getDocInfoByStringIds(";",office.getCooperationProtocol());
            		if(cooperationProtocol != null && cooperationProtocol.size() >0){
            			List<Map<String ,Object>> list = new ArrayList<>();
            			for (DocInfo docInfo : cooperationProtocol) {
            				Map<String, Object> map = new HashMap<>();
							map.put("id", docInfo.getId());
							map.put("name", docInfo.getDocName());
							list.add(map);
						}
            			m.put("cooperationProtocol", list);
            		}
            	}
            }
            
    	}
    }

    /**
     * 处理团期列表返回结果
     * @param page
     */
    private void handlePage(Page<Map<String, Object>> page) {
        for (Map<String, Object> m : page.getList()) {
            Long groupId = Long.parseLong(m.get("activitygroup_id").toString());
            BigDecimal quauqPrice = null;
            BigDecimal price = null;
            if (m.get("quauqAdultPrice") != null) {
                quauqPrice = new BigDecimal(m.get("quauqAdultPrice").toString());
            }
            if (m.get("settlementPrice") != null) {
            	price = new BigDecimal(m.get("settlementPrice").toString());
            }
            String currencyId = m.get("currencyIdStr").toString().split(",")[6];

            // 已经考虑过大于同行价的情况
            BigDecimal retailPrice = OrderCommonUtil.getRetailPrice(groupId, 2,price, quauqPrice, Long.parseLong(currencyId));
            if (retailPrice != null) {
            	DecimalFormat decimalFormat = new DecimalFormat("#.00");
                m.put("quauqPrice", decimalFormat.format(retailPrice));
				if (retailPrice.compareTo(new BigDecimal(0)) < 1) {
					 m.put("settlementAdultPrice", ""); // 供应价不大于0时,对应的同行价设为空
				}
            } else {
                m.put("quauqPrice", "");
                m.put("settlementAdultPrice", ""); // 供应价为空时,对应的同行价设为空
            }
        }
    }

    /**
     * 获取查询参数
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value={"getFilterCondition"})
    public String getFilterCondition(HttpServletRequest request, HttpServletResponse response) {
        String params = request.getParameter("params");
        JSONObject m = JSON.parseObject(params);
        String keyword = checkNull(m.get("keywordQ")); //搜索关键字
        String tourOutIn = checkNull(m.get("tourOutInQ")); // 出境游/国内游
        String travelAreaId = checkNull(m.get("travelAreaIdQ")); // 区域id
//        String pageNo = checkNull(m.get("pageNo")); // 当前页码（点击的页码）
//        String pageSize = checkNull(m.get("pageSize")); // 每页显示记录数
//        String orderBy = checkNull(m.get("orderBy")); // 排序

        // 出发城市
        String startCityPara = checkNull(m.get("startCityParaQ"));
        // 目的地
        String endCityPara = checkNull(m.get("endCityParaQ"));
        // 抵达城市
        String targetCity = checkNull(m.get("targetCity"));
        // 线路玩法
        String linePlay = checkNull(m.get("linePlay"));
        // 供应商
        String supplierPara = checkNull(m.get("supplierParaQ"));
        // 出团日期
        String groupDatePara = checkNull(m.get("groupDateParaQ"));
        // 行程天数
        String dayPara = checkNull(m.get("dayParaQ"));
        // 价格
        String pricePara = checkNull(m.get("priceParaQ"));
        // 余位
        String freePara = checkNull(m.get("freeParaQ"));

        // 封装参数
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("keyword", keyword);
        paramsMap.put("tourOutIn", tourOutIn);
        paramsMap.put("travelAreaId", travelAreaId);
        paramsMap.put("startCityParas", startCityPara);
        paramsMap.put("endCityParas", endCityPara);
        paramsMap.put("targetCitys", targetCity);
        paramsMap.put("linePlays", linePlay);
        paramsMap.put("supplierParas", supplierPara);
        paramsMap.put("groupDateParas", groupDatePara);
        paramsMap.put("dayParas", dayPara);
        paramsMap.put("priceParas", pricePara);
        paramsMap.put("freeParas", freePara);
        paramsMap.put("pageNo", 0);
        paramsMap.put("pageSize", Integer.MAX_VALUE);
        paramsMap.put("orderBy", "getConditionOrder");

        // 列表数据
        Page<Map<String, Object>> mapPage = new Page<>(request, response);
        Page<Map<String, Object>> page = t1Service.queryList(request, mapPage, paramsMap);
//        handlePage(page);

        // 出发城市
        List<Dict> fromAreaList = DictUtils.getFromArea4T1(tourOutIn);
        // 目的地
        List<Map<String, Object>> countrys = areaService.getCountry4T1();
        // 抵达城市
        List<Map<String, Object>> targetAreas = areaService.getTargetArea4T1(tourOutIn);
        // 供应商
        List<Office> supplierInfos = officeService.getOffice4T1(tourOutIn);

        // 团期列表数据
        List<Map<String, Object>> groupList = page.getList();

        // 筛选查询条件
        Set<Map<String, Object>> supplierInfoSet = new LinkedHashSet<Map<String, Object>>(); // 供应商
        Set<Map<String, Object>> fromAreaSet = new LinkedHashSet<Map<String, Object>>(); // 出发城市
        Set<Map<String, Object>> targetCountrySet = new LinkedHashSet<Map<String, Object>>(); // 目的地
        Set<Map<String, Object>> targetCitySet = new LinkedHashSet<Map<String, Object>>(); // 抵达城市
        Set<Map<String, Object>> linePlaySet = new LinkedHashSet<Map<String, Object>>(); // 线路
//        Set<Map<String, Object>> travelDaySet = new LinkedHashSet<Map<String, Object>>(); // 行程天数
//        Set<Map<String, Object>> priceRangeSet = new LinkedHashSet<Map<String, Object>>(); // 价格区间
        Set<Map<String, Object>> startDateSet = new LinkedHashSet<Map<String, Object>>(); // 出团日期
//        Set<Map<String, Object>> remainingSeatSet = new LinkedHashSet<Map<String, Object>>(); // 余位

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        Map<String, Object> isChooseMap = new LinkedHashMap<String, Object>(); // 已选择
        Map<String, Object> notChooseMap = new LinkedHashMap<String, Object>(); // 未选择

        // 供应商
        for (int i = 0; i < supplierInfos.size(); i++) {
            Office office = supplierInfos.get(i);
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j);
                if (office.getName().equals(groupMap.get("supplierName"))) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", office.getId());
                    map.put("name", office.getName());
                    Long logo = office.getLogo();
                    map.put("logoUrl", logo);
                    String path = "";
                    if (logo != null) {
                        path = checkNull(docInfoService.getDocInfo(logo).getDocPath());
                    }
                    map.put("path", path);
                    supplierInfoSet.add(map);
                    break;
                }
            }
        }
        if ("[]".equals(supplierPara)) {
            notChooseMap.put("supplierInfos", supplierInfoSet);
        } else {
            // 无产品时显示搜索条件
            if (supplierInfoSet.isEmpty()) {
                String[] strings = supplierPara.replace("[", "").replace("]", "").replace("\"", "").split(",");
                for (int i = 0; i < strings.length; i++) {
                    String string = strings[i];
                    Office office = officeService.get(Long.parseLong(string));
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", office.getId());
                    map.put("name", office.getName());
                    Long logo = office.getLogo();
                    map.put("logoUrl", logo);
                    String path = "";
                    if (logo != null) {
                        path = checkNull(docInfoService.getDocInfo(logo).getDocPath());
                    }
                    map.put("path", path);
                    supplierInfoSet.add(map);
                }
                isChooseMap.put("supplierInfos", supplierInfoSet);
            } else {
                isChooseMap.put("supplierInfos", supplierInfoSet);
            }

        }
        // 出发城市
        for (int i = 0; i < fromAreaList.size(); i++) {
            Dict dict = fromAreaList.get(i);
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j);
                if (dict.getLabel().equals(groupMap.get("fromArea").toString())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", dict.getValue());
                    map.put("name", dict.getLabel());
                    fromAreaSet.add(map);
                    break;
                }
            }
        }
        if ("[]".equals(startCityPara)) {
            notChooseMap.put("fromAreaList", fromAreaSet);
        } else {
            // 无产品时显示搜索条件
            if (fromAreaSet.isEmpty()) {
                String[] strings = startCityPara.replace("[", "").replace("]", "").replace("\"", "").split(",");
                for (int i = 0; i < strings.length; i++) {
                    String string = strings[i];
                    Dict dict = dictService.findByValueAndType(string, "from_area");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", dict.getValue());
                    map.put("name", dict.getLabel());
                    fromAreaSet.add(map);
                }
                isChooseMap.put("fromAreaList", fromAreaSet);
            } else {
                isChooseMap.put("fromAreaList", fromAreaSet);
            }
        }
        // 线路玩法
        if (!"[]".equals(linePlay)) {
            linePlay = handleString(linePlay);
            String[] linePlayArr = linePlay.split(",");
            for (int i = 0; i < linePlayArr.length; i++) {
                String lineId = linePlayArr[i];
                if ("0".equals(lineId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "0");
                    map.put("name", "其他");
                    linePlaySet.add(map);
                } else {
                    TouristLine touristLine = touristLineService.getById(Long.parseLong(lineId));
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", touristLine.getId());
                    map.put("name", touristLine.getLineName());
                    linePlaySet.add(map);
                }
            }

        } else {
            boolean qita = false;
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", groupMap.get("lineId"));
                if (groupMap.get("lineName") == null) {
                    qita = true;
                }
                map.put("name", groupMap.get("lineName") == null ? "其他" : groupMap.get("lineName"));
                linePlaySet.add(map);
            }
            // 如果包含“其他”，“其他”放最后
            if (qita) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("name", "其他");
                linePlaySet.remove(map);
                linePlaySet.add(map);
            }
        }
        if ("[]".equals(linePlay)) {
            notChooseMap.put("linePlay", linePlaySet);
        } else {
            isChooseMap.put("linePlay", linePlaySet);
        }
        // 抵达城市
        for (int i = 0; i < targetAreas.size(); i++) {
            Map<String, Object> arrivedCity = targetAreas.get(i);
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j); // 区域(一个)与抵达城市串数量相同重复拼串
                if (isContain(groupMap.get("targetArea").toString(), arrivedCity.get("id").toString(),
                        groupMap.get("districtIds").toString(), travelAreaId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", arrivedCity.get("id"));
                    map.put("name", arrivedCity.get("name"));
                    targetCitySet.add(map);
                    break;
                }
            }
        }
        // 目的地
        Set<Map<String, Object>> countrySet = new HashSet<Map<String, Object>>();
        for (Map<String, Object> tcity : targetCitySet) {
            String tcityId = tcity.get("id").toString();
            Map<String, Object> area = areaService.getCountryByCityId(tcityId); // 根据城市查国家(抵达城市查目的地)
            countrySet.add(area);
        }
        for (int i = 0; i < countrys.size(); i++) {
            Map<String, Object> country = countrys.get(i);
            for (Map<String, Object> c : countrySet) {
                if (c.get("id").toString().equals(country.get("id").toString())) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", country.get("id"));
                        map.put("name", country.get("name"));
                        targetCountrySet.add(map);
                    }
            }
        }
        if ("[]".equals(endCityPara)) {
            notChooseMap.put("targetCountry", targetCountrySet);
        } else {
            // 无产品时显示搜索条件
            if (targetCountrySet.isEmpty()) {
                String[] strings = endCityPara.replace("[", "").replace("]", "").replace("\"", "").split(",");
                for (int i = 0; i < strings.length; i++) {
                    String string = strings[i];
                    Area a = areaService.get(Long.parseLong(string));
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", a.getId());
                    map.put("name", a.getName());
                    targetCountrySet.add(map);
                }
                isChooseMap.put("targetCountry", targetCountrySet);
            } else {
                isChooseMap.put("targetCountry", targetCountrySet);
            }
        }
        // 抵达城市
        if ("[]".equals(targetCity)) {
            notChooseMap.put("targetCity", targetCitySet);
        } else {
            // 无产品时显示搜索条件
            if (targetCitySet.isEmpty()) {
                String[] strings = targetCity.replace("[", "").replace("]", "").replace("\"", "").split(",");
                for (int i = 0; i < strings.length; i++) {
                    String string = strings[i];
                    Area a = areaService.get(Long.parseLong(string));
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", a.getId());
                    map.put("name", a.getName());
                    targetCitySet.add(map);
                }
                isChooseMap.put("targetCity", targetCitySet);
            } else {
                isChooseMap.put("targetCity", targetCitySet);
            }
        }

        // 导航
        Set<String> parentsSet = new HashSet<String>();
        Set<Long> districtSet = new HashSet<Long>();
        for (Map<String, Object> toCity : targetCitySet) {
            String id = toCity.get("id").toString();
            Area area = areaService.get(Long.parseLong(id));
            parentsSet.add(area.getParentIds());
            if (area.getSysDistrictId() != null) {
                districtSet.add(area.getSysDistrictId());
            }
        }

        boolean in = false; // 国内游
        boolean out = false; // 出境游
        for (String s : parentsSet) {
            if (s.contains(Context.FREE_TRAVEL_FOREIGN)) {
                out = true;
            } else if (s.contains(Context.FREE_TRAVEL_INLAND)) {
                in = true;
            }
        }
//        Map<String, Object> result = new HashMap<String, Object>();
        if (in && out) {
            resultMap.put("nav", "导航");
        } else if (out) {
            resultMap.put("nav", "出境游");
        } else if (in) {
            resultMap.put("nav", "国内游");
        } else {
            resultMap.put("nav", "");
        }

        if (districtSet.size() == 1) {
            Long id = districtSet.iterator().next();
            District district = districtService.getDistrictById(id);
			if (district != null) {
            	resultMap.put("nav", resultMap.get("nav") + "/" + district.getName());
            }
        }

        // 行程天数
        Set<Map<String, Object>> daySet = getDaySet();
        Set<Map<String, Object>> daySetTemp = new LinkedHashSet<Map<String, Object>>();
        if(!"[]".equals(dayPara)) {
            dayPara = handleString(dayPara);
            String[] dayArr = dayPara.split(",");
            for (int i = 0; i < dayArr.length; i++) {
                String dayId = dayArr[i];
                if ("d1".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d1");
                    map.put("name", "1天");
                    daySetTemp.add(map);
                } else if ("d2".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d2");
                    map.put("name", "2天");
                    daySetTemp.add(map);
                } else if ("d3".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d3");
                    map.put("name", "3天");
                    daySetTemp.add(map);
                } else if ("d4".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d4");
                    map.put("name", "4天");
                    daySetTemp.add(map);
                } else if ("d5".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d5");
                    map.put("name", "5天");
                    daySetTemp.add(map);
                } else if ("d6".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d6");
                    map.put("name", "6天");
                    daySetTemp.add(map);
                } else if ("d7".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d7");
                    map.put("name", "7天");
                    daySetTemp.add(map);
                } else if ("d8".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d8");
                    map.put("name", "8天");
                    daySetTemp.add(map);
                } else if ("d9".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d9");
                    map.put("name", "9天");
                    daySetTemp.add(map);
                } else if ("d0".equals(dayId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d0");
                    map.put("name", "10天以上");
                    daySetTemp.add(map);
                }
            }
        } else {
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j);
                int activityDuration = Integer.parseInt(groupMap.get("activityDuration").toString());
                if (activityDuration == 1) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d1");
                    map.put("name", "1天");
                    daySetTemp.add(map);
                } else if (activityDuration == 2) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d2");
                    map.put("name", "2天");
                    daySetTemp.add(map);
                }else if (activityDuration == 3) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d3");
                    map.put("name", "3天");
                    daySetTemp.add(map);
                } else if (activityDuration == 4) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d4");
                    map.put("name", "4天");
                    daySetTemp.add(map);
                } else if (activityDuration == 5) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d5");
                    map.put("name", "5天");
                    daySetTemp.add(map);
                } else if (activityDuration == 6) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d6");
                    map.put("name", "6天");
                    daySetTemp.add(map);
                } else if (activityDuration == 7) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d7");
                    map.put("name", "7天");
                    daySetTemp.add(map);
                } else if (activityDuration == 8) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d8");
                    map.put("name", "8天");
                    daySetTemp.add(map);
                } else if (activityDuration == 9) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d9");
                    map.put("name", "9天");
                    daySetTemp.add(map);
                } else if (activityDuration >= 10) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "d0");
                    map.put("name", "10天以上");
                    daySetTemp.add(map);
                }
            }
        }
        Sets.SetView<Map<String, Object>> travelDays = Sets.difference(daySet, Sets.difference(daySet, daySetTemp));
        commonFun(dayPara, isChooseMap, notChooseMap, travelDays, "travelDays");

        // 价格区间
        Set<Map<String, Object>> priceSet = getPriceSet();
        Set<Map<String, Object>> priceSetTemp = new LinkedHashSet<Map<String, Object>>();
        if (!"[]".equals(pricePara)) {
            pricePara = handleString(pricePara);
            String[] priceArr = pricePara.split(",");
            for (int i = 0; i < priceArr.length; i++) {
                String priceId = priceArr[i];
                if ("p0".equals(priceId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "p0");
                    map.put("name", "3000元以下");
                    priceSetTemp.add(map);
                } else if ("p1".equals(priceId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "p1");
                    map.put("name", "3000-4999");
                    priceSetTemp.add(map);
                } else if ("p2".equals(priceId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "p2");
                    map.put("name", "5000-7999");
                    priceSetTemp.add(map);
                } else if ("p3".equals(priceId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "p3");
                    map.put("name", "8000-9999");
                    priceSetTemp.add(map);
                } else if ("p4".equals(priceId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "p4");
                    map.put("name", "10000以上");
                    priceSetTemp.add(map);
                }
            }
        } else {
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j);
                if (groupMap.get("quauqPrice") != null && !"".equals(groupMap.get("quauqPrice").toString())) {
                    Double quauqPrice = Double.parseDouble(groupMap.get("quauqPrice").toString());
                    if (quauqPrice < 3000) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", "p0");
                        map.put("name", "3000元以下");
                        priceSetTemp.add(map);
                    } else if (quauqPrice >= 3000 && quauqPrice <= 4999) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", "p1");
                        map.put("name", "3000-4999");
                        priceSetTemp.add(map);
                    } else if (quauqPrice >= 5000 && quauqPrice <= 7999) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", "p2");
                        map.put("name", "5000-7999");
                        priceSetTemp.add(map);
                    } else if (quauqPrice >= 8000 && quauqPrice <= 9999) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", "p3");
                        map.put("name", "8000-9999");
                        priceSetTemp.add(map);
                    } else if (quauqPrice >= 10000) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", "p4");
                        map.put("name", "10000以上");
                        priceSetTemp.add(map);
                    }
                }
            }
        }
        Sets.SetView<Map<String, Object>> priceRange = Sets.difference(priceSet, Sets.difference(priceSet, priceSetTemp));
        commonFun(pricePara, isChooseMap, notChooseMap, priceRange, "priceRange");

        // 出团日期
        Map<String, Object> dateMap = new HashMap<String, Object>();
        dateMap.put("id", groupDatePara.replace("[", "").replace("]", "").replace("\"", ""));
        dateMap.put("name", groupDatePara.replace("[", "").replace("]", "").replace("\"", ""));
        if (StringUtils.isNotBlank(dateMap.get("id").toString()) && StringUtils.isNotBlank(dateMap.get("name").toString())) {
            startDateSet.add(dateMap);
        }
        if ("[]".equals(groupDatePara)) {
            notChooseMap.put("startDate", startDateSet);
        } else {
            isChooseMap.put("startDate", startDateSet);
        }
        // 余位
        Set<Map<String, Object>> freeSet = getFreeSet();
        Set<Map<String, Object>> freeSetTemp = new LinkedHashSet<Map<String, Object>>();
        if (!"[]".equals(freePara)) {
            freePara = handleString(freePara);
            String[] freeArr = freePara.split(",");
            for (int i = 0; i < freeArr.length; i++) {
                String freeId = freeArr[i];
                if ("f0".equals(freeId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f0");
                    map.put("name", "10以下");
                    freeSetTemp.add(map);
                } else if ("f1".equals(freeId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f1");
                    map.put("name", "10-19");
                    freeSetTemp.add(map);
                } else if ("f2".equals(freeId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f2");
                    map.put("name", "20-29");
                    freeSetTemp.add(map);
                } else if ("f3".equals(freeId)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f3");
                    map.put("name", "30以上");
                    freeSetTemp.add(map);
                }
            }
        } else {
            for (int j = 0; j < groupList.size(); j++) {
                Map<String, Object> groupMap =  groupList.get(j);
                Integer freePosition = Integer.parseInt(groupMap.get("freePosition").toString());
                if (freePosition <= 9) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f0");
                    map.put("name", "10以下");
                    freeSetTemp.add(map);
                } else if (freePosition >= 10 && freePosition <= 19) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f1");
                    map.put("name", "10-19");
                    freeSetTemp.add(map);
                } else if (freePosition >= 20 && freePosition <= 29) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f2");
                    map.put("name", "20-29");
                    freeSetTemp.add(map);
                } else if (freePosition >= 30) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", "f3");
                    map.put("name", "30以上");
                    freeSetTemp.add(map);
                }
            }
        }
        Sets.SetView<Map<String, Object>> remainingSeat = Sets.difference(freeSet, Sets.difference(freeSet, freeSetTemp));
        commonFun(freePara, isChooseMap, notChooseMap, remainingSeat, "remainingSeat");

        // 封装数据
        resultMap.put("isChoose", isChooseMap);
        resultMap.put("notChoose", notChooseMap);
        // 返回结果Map
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("results", resultMap);
        // 转换成json
        String json = JSON.toJSONString(results);
        return json;
    }

    private String handleString(String str) {
        return str.replace("[", "").replace("]", "").replace("\"", "");
    }

    /**
     * 提取公共方法
     * @param para
     * @param isChooseMap
     * @param notChooseMap
     * @param setView
     * @param key
     */
    public void commonFun(String para, Map<String, Object> isChooseMap, Map<String, Object> notChooseMap, Sets.SetView<Map<String, Object>> setView, String key) {
        if (para.contains("-")) {
            para = para.replace("[", "").replace("]", "").replace("\"", "");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", para);
            map.put("name", para);
            Set<Map<String, Object>> s = Sets.newHashSet();
            s.add(map);
            isChooseMap.put(key, s);
        } else {
            if ("[]".equals(para)) {
                notChooseMap.put(key, new LinkedHashSet(setView));
            } else {
                isChooseMap.put(key, new LinkedHashSet(setView));
            }
        }
    }

    /**
     *
     * @param targetAreas 抵达城市ids
     * @param arriveCity 抵达城市id
     * @param districtIds 区域ids
     * @param districtId 区域id
     * @return
     */
    private boolean isContain(String targetAreas, String arriveCity, String districtIds, String districtId) {
        boolean flag = false;
        if (targetAreas.indexOf(arriveCity) == -1) {
            return false;
        } else {
            // id完全匹配
            String[] targetAreaArr = targetAreas.split(",");
            for (int i = 0; i < targetAreaArr.length; i++) {
                String s = targetAreaArr[i];
                if (arriveCity.equals(s)) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            String[] targetAreaArr = targetAreas.split(",");
            Integer pos = null;
            for (int i = 0; i < targetAreaArr.length; i++) {
                String s = targetAreaArr[i];
                if (arriveCity.equals(s)) {
                    pos = i;
                    break;
                }
            }
            String[] districtIdArr = districtIds.split(",");
            // 最后一个城市没有对应区域或pos为null时抛出异常
            try {
                if (districtId.equals("") || districtId.equals(districtIdArr[pos])) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // 行程天数基础数据
    private Set<Map<String,Object>> getDaySet() {
        Set<Map<String,Object>> set = new LinkedHashSet<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "d1");
        map1.put("name", "1天");
        set.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", "d2");
        map2.put("name", "2天");
        set.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("id", "d3");
        map3.put("name", "3天");
        set.add(map3);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("id", "d4");
        map4.put("name", "4天");
        set.add(map4);
        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("id", "d5");
        map5.put("name", "5天");
        set.add(map5);
        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("id", "d6");
        map6.put("name", "6天");
        set.add(map6);
        Map<String, Object> map7 = new HashMap<String, Object>();
        map7.put("id", "d7");
        map7.put("name", "7天");
        set.add(map7);
        Map<String, Object> map8 = new HashMap<String, Object>();
        map8.put("id", "d8");
        map8.put("name", "8天");
        set.add(map8);
        Map<String, Object> map9 = new HashMap<String, Object>();
        map9.put("id", "d9");
        map9.put("name", "9天");
        set.add(map9);
        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("id", "d0");
        map0.put("name", "10天以上");
        set.add(map0);
        return set;
    }

    // 价格基础数据
    private Set<Map<String,Object>> getPriceSet() {
        Set<Map<String,Object>> set = new LinkedHashSet<Map<String, Object>>();
        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("id", "p0");
        map0.put("name", "3000元以下");
        set.add(map0);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "p1");
        map1.put("name", "3000-4999");
        set.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", "p2");
        map2.put("name", "5000-7999");
        set.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("id", "p3");
        map3.put("name", "8000-9999");
        set.add(map3);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("id", "p4");
        map4.put("name", "10000以上");
        set.add(map4);
        return set;
    }

    // 余位基础数据
    private Set<Map<String,Object>> getFreeSet() {
        Set<Map<String,Object>> set = new LinkedHashSet<Map<String, Object>>();
        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("id", "f0");
        map0.put("name", "10以下");
        set.add(map0);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "f1");
        map1.put("name", "10-19");
        set.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", "f2");
        map2.put("name", "20-29");
        set.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("id", "f3");
        map3.put("name", "30以上");
        set.add(map3);
        return set;
    }

    @RequestMapping(value="jumpParam")
    public String jumpParam(HttpServletRequest request, Model model) {

     /*   HttpSession session = request.getSession();
        String pageDisplay=session.getAttribute("page_Display").toString();
        model.addAttribute("pageDisplay",pageDisplay);*/
    	Agentinfo agent = agentinfoService.findOne(UserUtils.getUser().getAgentId());
		if (agent != null) {
    		model.addAttribute("listFlag", agent.getT1ListFlag());
    	}
        return "modules/homepage/jumpParam";
    }
    @RequestMapping(value="proGroupDetail")
    public String proGroupDetail(HttpServletRequest request, Model model) {
 /*       HttpSession session = request.getSession();
        String pageDisplay=session.getAttribute("page_Display").toString();
        model.addAttribute("pageDisplay",pageDisplay);*/
//        model.addAttribute("ctxs",1);
        return "modules/homepage/proGroupDetail";
    }
    @ResponseBody
    @RequestMapping(value="jumpParamMiddle")
    public String jumpParamMiddle(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String pageDisplay=request.getParameter("pageDisplay")==""?"":request.getParameter("pageDisplay");
        session.setAttribute("page_Display", pageDisplay);
        return "success";
    }

    /**
     * 关联T1区域和目的地，从excel中读取数据
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "readData")
    public String readData() throws IOException {
        DocInfo docInfo = docInfoService.getDocInfoByName();
        File file = new File(Global.getBasePath() + File.separator + docInfo.getDocPath());
        InputStream is = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            // 读取Sheet中数据
            String tempDistrictName = "";
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    // 区域
                    XSSFCell districtName = xssfRow.getCell(0);
                    if (districtName != null) {
                        tempDistrictName = districtName.toString();
                    }
                    District district = districtService.getDistrictByName(tempDistrictName);
                    // 城市
                    XSSFCell cityName = xssfRow.getCell(1);
                    if (cityName != null) {
                        List<Area> areaList = areaService.findAreaByName(cityName.toString());
                        if (areaList == null || areaList.size() == 0) {
                            System.out.println("没有这个城市" + cityName.toString());
                        } else {
                            Area area = areaList.get(0);
                            areaService.updateDistrictById(district.getId(), area.getId());
                        }
                    }

                }
            }
        }
        return "数据关联成功";
    }

    /**
     * 接受的参数如果对象为空，返回空字符串""，否则返回其字符串
     * @param obj
     * @return
     */
    private String checkNull(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 根据产品获取导航信息
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getNav")
    public String getNav(Long productId) {
        List<Integer> targetAreaIds = travelActivityService.getTargetAreaByActivityId(productId);
        // 导航
        Set<String> parentsSet = new HashSet<String>();
        Set<Long> districtSet = new HashSet<Long>();
        for (Integer targetAreaId : targetAreaIds) {
            Area area = areaService.get(targetAreaId.longValue());
            parentsSet.add(area.getParentIds());
            districtSet.add(area.getSysDistrictId());
        }

        boolean in = false; // 国内游
        boolean out = false; // 出境游
        for (String s : parentsSet) {
            if (s.contains(Context.FREE_TRAVEL_FOREIGN)) {
                out = true;
            } else if (s.contains(Context.FREE_TRAVEL_INLAND)) {
                in = true;
            }
        }
        String nav = "";
        if (in && out) {
            nav = "导航";
        } else if (out) {
            nav = "出境游";
        } else if (in) {
            nav = "国内游";
        }

        if (districtSet.size() == 1) {
            Long id = districtSet.iterator().next();
            District district = districtService.getDistrictById(id);
            if(district != null){
            	nav = nav +  "/" + district.getName();
            }
        }
        return nav;
    }
    
    @ResponseBody
    @RequestMapping(value = "changeT1ListShowFlag")
    public Object changeT1ListShowFlag(String t1ListFlag) {
    	Map<String, String> result = agentinfoService.changeT1ListShowFlag(UserUtils.getUser().getAgentId(), Integer.parseInt(t1ListFlag));
    	return result;
    }

}
