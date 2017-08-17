/*

 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.web;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.input.HotelPlInput;
import com.trekiz.admin.modules.hotelPl.input.HotelPlTaxInput;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.modules.hotelPl.query.HotelPlQuery;
import com.trekiz.admin.modules.hotelPl.service.*;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelPl")
public class HotelPlController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotelPl/hotelpl/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelPl/list";
	protected static final String SAVE_HOTEL_PL_PAGE = "modules/hotelPl/hotelpl/saveHotelPl";
	protected static final String SHOW_PAGE = "modules/hotelPl/hotelpl/show";
	
	@Autowired
	private HotelPlService hotelPlService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private SysGeographyService sysGeographyService;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	
	private static final int STATUS_ADD = 1;
	private static final int STATUS_UPDATE = 2;
	
	/**
	 * 酒店价单列表信息
	     * <p>@Description TODO</p>
		 * @Title: list
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-12 上午10:21:17
	 */
	@RequestMapping(value = "list")
	public String list(HotelPlQuery hotelPlQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(hotelPlQuery.getCountry() == null) {
			//初始化国家为马尔代夫
			hotelPlQuery.setCountry("80415d01488c4d789494a67b638f8a37");
		}
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		hotelPlQuery.setDelFlag("0");
		String startCreateDateStr = hotelPlQuery.getStartCreateDateStr();
		String endCreateDateStr = hotelPlQuery.getEndCreateDateStr();
		
		if(StringUtils.isNotEmpty(startCreateDateStr)) {
			hotelPlQuery.setStartCreateDate(DateUtils.string2Date(startCreateDateStr, DateUtils.DATE_PATTERN_YYYY_MM_DD));
		}
		
		if(StringUtils.isNotEmpty(endCreateDateStr)) {
			Date endCreateDate = DateUtils.string2Date(endCreateDateStr, DateUtils.DATE_PATTERN_YYYY_MM_DD);
			Calendar calendar = Calendar.getInstance();  
			calendar.setTime(endCreateDate);  
			calendar.add(Calendar.DATE, 1);
			calendar.add(Calendar.SECOND, -1);
			
			hotelPlQuery.setEndCreateDate(calendar.getTime());
		}
		hotelPlQuery.setWholesalerId(companyId.intValue());
		
        Page<HotelPl> page = hotelPlService.find(new Page<HotelPl>(request, response), hotelPlQuery);
        if(page != null && CollectionUtils.isNotEmpty(page.getList())) {
        	for(HotelPl hotelPl : page.getList()) {
        		hotelPl.setHotelPlRooms(hotelPlService.getDistinctHotelPlRoomsByUuid(hotelPl.getUuid()));
        	}
        }
        
        //读取地接社集合信息
        model.addAttribute("supplierInfos", supplierInfoService.findSupplierInfoByCompanyId(companyId));
        model.addAttribute("page", page);
        model.addAttribute("hotelPlQuery", hotelPlQuery);
        
        if(StringUtils.isNotEmpty(hotelPlQuery.getCountry())) {
            model.addAttribute("countryName", sysGeographyService.getNameCnByUuid(hotelPlQuery.getCountry()));
        }
        
        return LIST_PAGE;
	}
	
	/**
	 * 酒店价单详情展示
	     * <p>@Description TODO</p>
		 * @Title: show
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-12 上午10:21:31
	 */
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		HotelPl hotelPl = hotelPlService.getByUuid(uuid);
		
		if(StringUtils.isEmpty(uuid) || (hotelPl == null)) {
			return RE_LIST_PAGE;
		}
		
		if(StringUtils.isNotEmpty(hotelPl.getGalamealMemo())) {
			hotelPl.setGalamealMemo(hotelPl.getGalamealMemo().replaceAll("\n", "<br/>"));
		}
		
		model.addAttribute("hotelPl", hotelPl);
		
		//加载新增酒店价单基础数据
		hotelPlService.initSaveHotelPlPageData(model);
		
		//加载酒店价单数据
		hotelPlService.initHotelPlData(hotelPl, model);
		//获取已上传资料
		List<HotelAnnex> annexList=hotelAnnexService.getAnnexListByMainUuid(hotelPl.getUuid());
		//加载酒店价单下关联酒店的基础数据
		Map<String, String> datas = new HashMap<String, String>();
		hotelPlService.buildBaseData(hotelPl, datas);
		model.addAllAttributes(datas);
		model.addAttribute("annexList", annexList);
		
		return SHOW_PAGE;
	}
	
	/**
	 * 跳转到保存酒店价单页面
	*<p>Title: toSaveHotelPl</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-7-13 上午10:12:18
	* @throws
	 */
	@RequestMapping(value = "toSaveHotelPl")
	public String toSaveHotelPl(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//加载新增酒店价单基础数据
		hotelPlService.initSaveHotelPlPageData(model);

		model.addAttribute("updateFlag", "1");
		return SAVE_HOTEL_PL_PAGE;
	}
	
	/**
	 * 跳转到更新酒店价单页面
	*<p>Title: toUpdateHotelPl</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-7-13 上午10:13:13
	* @throws
	 */
	@RequestMapping(value = "toUpdateHotelPl/{hotelPlUuid}")
	public String toUpdateHotelPl(@PathVariable String hotelPlUuid, HttpServletRequest request, HttpServletResponse response, Model model) {

		HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
		
		if(StringUtils.isEmpty(hotelPlUuid) || (hotelPl == null)) {
			return RE_LIST_PAGE;
		}
		
		model.addAttribute("hotelPl", hotelPl);
		
		//加载新增酒店价单基础数据
		hotelPlService.initSaveHotelPlPageData(model);
		
		//加载酒店价单数据
		hotelPlService.initHotelPlData(hotelPl, model);
		//获取已上传资料
		List<HotelAnnex> annexList=hotelAnnexService.getAnnexListByMainUuid(hotelPl.getUuid());
		//加载酒店价单下关联酒店的基础数据
		Map<String, String> datas = new HashMap<String, String>();
		hotelPlService.buildBaseData(hotelPl, datas);
		model.addAllAttributes(datas);
		model.addAttribute("annexList", annexList);
		model.addAttribute("updateFlag", "2");
		
		return SAVE_HOTEL_PL_PAGE;
	}
	
	/**
	 * 删除价单信息
		 * @Title: delete
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-23 下午3:10:38
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(String ids){
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(ids)){
				String[] idsArray = ids.split(",");
				b = hotelPlService.batchDelete(idsArray);
				
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "error");
		}
		if(b){
			datas.put("result", "1");
			datas.put("message", "success");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	/**
	 * 保存基本信息
	*<p>Title: saveBaseInfo</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-26 上午11:02:40
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "saveBaseInfo")
	public Object saveBaseInfo(HotelPlInput hotelPlInput, Model model, RedirectAttributes redirectAttributes) {
		Map<String, String> datas = new HashMap<String, String>();
		
		try {
			HotelPl hotelPl = hotelPlInput.getHotelPl();
			
			if(StringUtils.isEmpty(hotelPlInput.getUuid())) {
				hotelPl.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
				//将税金算法默认设置为连乘
				if(hotelPl.getTaxArithmetic() == null) {
					hotelPl.setTaxArithmetic(HotelPl.TAX_ARITHMETIC_CONNECTION);
				}
				hotelPlService.save(hotelPl);
				datas.put("result", "1");
				datas.put("message", "基本信息保存成功！");

				//组装基础数据
				hotelPlService.buildBaseData(hotelPl, datas);
			}
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
		
	}
	
	/**
	 * 修改价单基本信息
	*<p>Title: updatePlBaseInfo</p>
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-7-20 上午10:18:25
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "updatePlBaseInfo")
	public Object updatePlBaseInfo(HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		String name = request.getParameter("name");
		String supplierInfoId = request.getParameter("supplierInfoId");
		String purchaseType = request.getParameter("purchaseType");
		String areaType = request.getParameter("areaType");

		HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
		if(StringUtils.isEmpty(hotelPlUuid) || hotelPl == null) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			
			return datas;
		}
		
		try {
			hotelPl.setName(name);
			if(StringUtils.isNotEmpty(supplierInfoId)) {
				hotelPl.setSupplierInfoId(Integer.parseInt(supplierInfoId));
			}
			hotelPl.setPurchaseType(Integer.parseInt(purchaseType));
			hotelPl.setAreaType(Integer.parseInt(areaType));
			
			hotelPlService.update(hotelPl);
			
			datas.put("result", "2");
			datas.put("message", "价单基本信息修改成功！");
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
		
	}
	
	
	/**
	 * 保存和更新酒店税金接口
	     * <p>@Description TODO</p>
		 * @Title: saveHotelPlTax
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:29:28
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelPlTax")
	public Object saveHotelPlTax(String hotelPlTaxPriceJsonData, String hotelPlTaxExceptionJsonData, String taxArithmetic, Model model, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String status = request.getParameter("status");
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		
		try {
			HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			if(StringUtils.isNotEmpty(taxArithmetic)) {
				hotelPl.setTaxArithmetic(Integer.parseInt(taxArithmetic));
			}
			hotelPlService.update(hotelPl);
			
			if(HotelPlTaxInput.STATUS_ADD == Integer.parseInt(status)) {
				
				datas = hotelPlService.saveHotelPlTaxInfo(hotelPlTaxPriceJsonData, hotelPlTaxExceptionJsonData, hotelPl);
				datas.put("message", "酒店税金保存成功！");
			} else if(HotelPlTaxInput.STATUS_UPDATE == Integer.parseInt(status)) {
				datas = hotelPlService.updateHotelPlTaxInfo(hotelPlTaxPriceJsonData, hotelPlTaxExceptionJsonData, hotelPl);
				datas.put("message", "酒店税金更新成功！");
			}
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
		
	}
	
	/**
	 * 保存和更新酒店房型价格信息
	     * <p>@Description TODO</p>
		 * @Title: saveHotelPlPrice
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:30:26
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelPlPrice")
	public Object saveHotelPlPrice(String hotelPlPriceJsonData, String roomMemoJsonData,  Model model, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String status = request.getParameter("status");
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		String mixliveCurrencyId = request.getParameter("mixliveCurrencyId");
		String mixliveAmount = request.getParameter("mixliveAmount");
		
		try {
			HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			hotelPl.setMixliveCurrencyId(Integer.parseInt(mixliveCurrencyId));
			if(StringUtils.isNotEmpty(mixliveAmount)) {
				hotelPl.setMixliveAmount(Double.parseDouble(mixliveAmount));
			}
			hotelPlService.update(hotelPl);
			
			if(STATUS_ADD == Integer.parseInt(status)) {
				datas = hotelPlService.saveHotelPlPriceInfo(hotelPlPriceJsonData, roomMemoJsonData, hotelPl);
				datas.put("message", "酒店房型价格保存成功！");
			} else if(STATUS_UPDATE == Integer.parseInt(status)) {
				datas = hotelPlService.updateHotelPlPriceInfo(hotelPlPriceJsonData, roomMemoJsonData, hotelPl);
				datas.put("message", "酒店房型价格更新成功！");
			}
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
	}
	
	/**
	 * 保存和更新交通费用
	     * <p>@Description TODO</p>
		 * @Title: saveHotelPlIslandWay
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:30:58
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelPlIslandWay")
	public Object saveHotelPlIslandWay(String hotelPlIslandWayJsonData, String islandWayMemoJsonData,  Model model, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String status = request.getParameter("status");
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		
		try {
			HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			
			if(STATUS_ADD == Integer.parseInt(status)) {
				datas = hotelPlService.saveHotelPlIslandWayInfo(hotelPlIslandWayJsonData, islandWayMemoJsonData, hotelPl);
				datas.put("message", "交通费用保存成功！");
			} else if(STATUS_UPDATE == Integer.parseInt(status)) {
				datas = hotelPlService.updateHotelPlIslandWayInfo(hotelPlIslandWayJsonData, islandWayMemoJsonData, hotelPl);
				datas.put("message", "交通费用更新成功！");
			}
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
	}
	
	/**
	 * 保存和更新升餐费用信息
	     * <p>@Description TODO</p>
		 * @Title: saveHotelPlRiseMeal
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:31:20
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelPlRiseMeal")
	public Object saveHotelPlRiseMeal(String hotelPlRiseMealJsonData, String riseMealMemoJsonData, Model model, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String status = request.getParameter("status");
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		
		try {
			HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			
			if(STATUS_ADD == Integer.parseInt(status)) {
				datas = hotelPlService.saveHotelPlRiseMealInfo(hotelPlRiseMealJsonData, riseMealMemoJsonData, hotelPl);
				datas.put("message", "升餐费用保存成功！");
			} else if(STATUS_UPDATE == Integer.parseInt(status)) {
				datas = hotelPlService.updateHotelPlRiseMealInfo(hotelPlRiseMealJsonData, riseMealMemoJsonData, hotelPl);
				datas.put("message", "升餐费用更新成功！");
			}
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
	}
	
	
	/**
	 * 保存和更新强制性节日餐信息
	     * <p>@Description TODO</p>
		 * @Title: saveHotelPlHolidayMeal
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:31:46
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelPlHolidayMeal")
	public Object saveHotelPlHolidayMeal(String hotelPlHolidayMealJsonData, String galamealMemo, Model model, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String status = request.getParameter("status");
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		
		try {
			//修改酒店价单节日餐备注
			HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			hotelPl.setGalamealMemo(galamealMemo);
			hotelPlService.update(hotelPl);
			
			if(STATUS_ADD == Integer.parseInt(status)) {
				datas = hotelPlService.saveHotelPlHolidayMealInfo(hotelPlHolidayMealJsonData, hotelPl);
				datas.put("message", "升餐费用保存成功！");
			} else if(STATUS_UPDATE == Integer.parseInt(status)) {
				datas = hotelPlService.updateHotelPlHolidayMealInfo(hotelPlHolidayMealJsonData, hotelPl);
				datas.put("message", "升餐费用更新成功！");
			}
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
	}
	
	/**
	 * 保存酒店价单上传资料信息
	     * <p>@Description TODO</p>
		 * @Title: saveHotelAnnex
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:32:06
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelAnnex")
	public Object saveHotelAnnex(HttpServletRequest request){
		Map<String, Object> data= new HashMap<String, Object>();
		String hotelPlUuid=request.getParameter("hotelPlUuid");
		try{
			//HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			List<HotelAnnex> hotelAnnexList=buildAnnexList(request);
			hotelAnnexDao.synDocInfo(hotelPlUuid,HotelAnnex.ANNEX_TYPE_FOR_HOTEL_PL,
					UserUtils.getCompanyIdForData().intValue(),hotelAnnexList);
			//hotelPlService.saveHOtelAnnex(hotelAnnexList,hotelPl);
			data.put("message", "上传文件保存成功");
		}catch (Exception e){
			data.put("message", "系统出现异常，请稍后重试！");
		}
		
		return data;
	}
	
	/**
	 * 保存和更新酒店价单优惠信息
	     * <p>@Description TODO</p>
		 * @Title: saveHotelPlPreferential
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-8 上午10:32:34
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelPlPreferential")
	public Object saveHotelPlPreferential(String hotelPlPreferentialJsonData,  Model model, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String status = request.getParameter("status");
		String hotelPlUuid = request.getParameter("hotelPlUuid");
		
		try {
			HotelPl hotelPl = hotelPlService.getByUuid(hotelPlUuid);
			
			if(STATUS_ADD == Integer.parseInt(status)) {
				datas = hotelPlService.saveHotelPlPreferentialInfo(hotelPlPreferentialJsonData, hotelPl);
				datas.put("message", "优惠信息保存成功！");
			} else if(STATUS_UPDATE == Integer.parseInt(status)) {
				datas = hotelPlService.updateHotelPlPreferentialInfo(hotelPlPreferentialJsonData, hotelPl);
				datas.put("message", "优惠信息更新成功！");
			}
			
			
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
		}
		return datas;
	}
	
	//装载上传文件信息
	private List<HotelAnnex> buildAnnexList(HttpServletRequest request) {
		List<HotelAnnex> list = new ArrayList<HotelAnnex>();
		if(ArrayUtils.isNotEmpty(request.getParameter("docId").split(","))){
			String[] docId = request.getParameter("docId").split(",");
			String[] docOriName = request.getParameter("docName").split(",");
			String[] docPath = request.getParameter("docPath").split(",");
			
			for(int i=0;i<docId.length;i++){
				if(StringUtils.isNotBlank(docId[i])){
					HotelAnnex ha = new HotelAnnex();
					ha.setDocId(Integer.parseInt(docId[i]));
					ha.setDocPath(docPath[i]);
					ha.setDocName(docOriName[i]);
					list.add(ha);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据酒店uuid获取酒店的详细信息
	*<p>Title: getHotelDetailInfo</p>
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-7-8 下午3:37:31
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "getHotelDetailInfo")
	public Object getHotelDetailInfo(String hotelUuid, Model model){
		HotelPl currentHotel = new HotelPl();
		Hotel hotel = hotelService.getByUuid(hotelUuid);
		if(hotel == null) {
			return "";
		}
		
		HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
		currentHotel.setHotelUuid(hotel.getUuid());
		if(hotelStar != null) {
			currentHotel.setHotelStar(hotelStar.getLabel());
		}
		currentHotel.setHotelAddress(hotel.getAddress());
		currentHotel.setContactPhone(hotel.getTelephone());
		
		return JSON.toJSONStringWithDateFormat(currentHotel, "yyyy-MM-dd");
	}
	
	/**
	 * 根据酒店uuid获取酒店房型集合
	*<p>Title: getHotelRooms</p>
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-7-8 下午3:37:49
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "getHouseTypesInfo")
	public Object getHouseTypesInfo(String hotelUuid, Model model){
		Map<String,String> datas = new HashMap<String, String>();
		Map<String,Object> houseTypes = hotelPlService.getHotelRoomsInfoByHotelUuid(hotelUuid);
		datas.put("houseTypes", JSON.toJSONStringWithDateFormat(houseTypes, "yyyy-MM-dd"));
		return datas;
	}
	
	/**
	 * 修改酒店价单备注信息
	*<p>Title: updateHotelPlMemo</p>
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-7-10 下午8:56:01
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "updateHotelPlMemo")
	public Object updateHotelPlMemo(String hotelPlUuid, String hotelPlMemo) {
		Map<String, String> datas = hotelPlService.updateHotelPlMemo(hotelPlUuid, hotelPlMemo);
		return datas;
	}
	
	/**
	 * 酒店价单唯一性校验(根据“酒店名称”“采购类型”“地接供应商”3个条件来判断该价单是否已经存在)
	*<p>Title: findIsExist</p>
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-7-18 下午2:59:42
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "findIsExist")
	public Object findIsExist(String hotelUuid, String purchaseType, String supplierInfoId) {
		Map<String, String> datas = new HashMap<String, String>();
		if(StringUtils.isEmpty(hotelUuid) || StringUtils.isEmpty(purchaseType) || StringUtils.isEmpty(supplierInfoId)) {
			datas.put("result", "1");
			datas.put("message", "酒店名称、采购类型、地接供应商均不能为空！！！");
			return datas;
		}
		
		boolean flag = hotelPlService.findIsExist(hotelUuid, Integer.parseInt(purchaseType), Integer.parseInt(supplierInfoId));
		
		//价单信息不存在
		if(!flag) {
			datas.put("result", "1");
		} else {
			datas.put("result", "0");
			datas.put("message", "您所维护价单已存在，请修改酒店名称、采购类型或地接供应商！！！");
		}
		
		return datas;
	}
	
}
