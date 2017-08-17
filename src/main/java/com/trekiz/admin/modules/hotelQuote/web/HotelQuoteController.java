/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.service.HotelGuestTypeService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.module.bean.GuestPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.PreferentialJsonBean;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuote;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteCondition;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionDetailPersonNum;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResult;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetail;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionDetailPersonNumService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionPreferentialRelService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultDetailService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteService;
import com.trekiz.admin.modules.hotelQuotePreferential.copyJson.ConditionDetail;
import com.trekiz.admin.modules.hotelQuotePreferential.copyJson.CopyJson;
import com.trekiz.admin.modules.hotelQuotePreferential.copyJson.GuestPrice;
import com.trekiz.admin.modules.hotelQuotePreferential.copyJson.RoomNight;
import com.trekiz.admin.modules.hotelQuotePreferential.copyJson.TotalPrice;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialRequireService;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialRoomService;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelQuote")
public class HotelQuoteController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules//hotelquote/hotelQuoteList";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelQuote/list";
	protected static final String FORM_PAGE = "modules//hotelquote/form";
	protected static final String SHOW_PAGE = "modules//hotelquote/show";
	protected static final String SHOW_DETAIL = "modules/hotelquote/showDetail";
	
	@Autowired
	private HotelQuoteService hotelQuoteService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private HotelQuoteConditionService hotelQuoteConditionService;
	@Autowired
	private HotelQuoteResultService hotelQuoteResultService;
	@Autowired
	private HotelQuoteResultDetailService hotelQuoteResultDetailService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private HotelQuoteConditionDetailPersonNumService hotelQuoteConditionDetailPersonNumService;
	@Autowired
	private HotelQuotePreferentialService hotelQuotePreferentialService;
	@Autowired
	private HotelQuoteConditionPreferentialRelService hotelQuoteConditionPreferentialRelService;
	@Autowired
	private HotelQuotePreferentialRoomService hotelQuotePreferentialRoomService;
	@Autowired
	private HotelQuotePreferentialRequireService hotelQuotePreferentialRequireService;
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private HotelRoomService roomService;
	@Autowired
	private HotelMealService mealService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private HotelGuestTypeService hotelGuestTypeService;
	private HotelQuote dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=hotelQuoteService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(HotelQuoteQuery hotelQuoteQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelQuoteQuery.setDelFlag("0");
        Page<HotelQuote> page = hotelQuoteService.find(new Page<HotelQuote>(request, response), hotelQuoteQuery); 
        model.addAttribute("page", page);
        model.addAttribute("hotelQuoteQuery", hotelQuoteQuery);
        return "modules//hotelquote/list";
	}
	
	/**
	 * 酒店报价列表方法
	 * @param hotelQuoteQuery  封装了查询条件
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author wangxv
	 */
	@RequestMapping(value = "hotelQuoteList")
	public String hotelQuoteList(HotelQuoteQuery hotelQuoteQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Map<String, Object>> page = hotelQuoteService.hotelQuoteList(new Page<Map<String, Object>>(request, response), hotelQuoteQuery); 
        List<List<Map<String,String>>> subList = hotelQuoteService.getQuoteRoomList(page.getList());
        //装载分页数据
        model.addAttribute("page", page);
        model.addAttribute("pageStr", page.toString());
        model.addAttribute("count", page.getCount());
        model.addAttribute("subList", subList);
        //查询条件封装
        model.addAttribute("hotelQuoteQuery", hotelQuoteQuery);
        //初始化信息
        List<Department> departmentList = departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId());
        model.addAttribute("departmentList", departmentList);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(HotelQuoteInput hotelQuoteInput, Model model) {
		model.addAttribute("hotelQuoteInput", hotelQuoteInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(HotelQuoteInput hotelQuoteInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			hotelQuoteService.save(hotelQuoteInput);
		} catch (Exception e) {
			result="0";
		}
		return result;
		
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("hotelQuote", hotelQuoteService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		HotelQuote hotelQuote = hotelQuoteService.getByUuid(uuid);
		HotelQuoteInput hotelQuoteInput = new HotelQuoteInput(hotelQuote);
		model.addAttribute("hotelQuoteInput", hotelQuoteInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(HotelQuoteInput hotelQuoteInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, hotelQuoteInput,true);
			hotelQuoteService.update(dataObj);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					hotelQuoteService.removeByUuid(uuid);
				}
				
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常，请重新操作!");
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
	 * 报价详情页面 
	 * @param uuid  报价条件表的uuid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "showDetail/{uuid}")
	public ModelAndView showDetail(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		List<HotelQuoteCondition> conditionList = hotelQuoteConditionService.findByQuoteConditionUuid(uuid);
		//基本信息 
		HotelQuote hotelQuote = hotelQuoteService.getByUuid(conditionList.get(0).getHotelQuoteUuid());
		mav.addObject("hotelQuote", hotelQuote);
		User user=new User();
		if(hotelQuote!=null){
			user = UserUtils.getUser(hotelQuote.getUserId()+0L);
		}
		mav.addObject("user", user);
		//添加复制功能信息
		CopyJson copyJson = new CopyJson();
		copyJson.setIslandText(islandService.getByUuid(conditionList.get(0).getIslandUuid()).getIslandName());
		//酒店报价条件
		for(HotelQuoteCondition hqc:conditionList){
			List<HotelQuoteConditionDetailPersonNum> conditionDetailPersonNum = hotelQuoteConditionDetailPersonNumService.getByHotelQuoteConditionUuid(hqc.getUuid());
			//入住日期 	房型（容住率） 餐型 游客类型 + 优惠合计
			List<HotelQuoteResult> hotelQuoteResult = hotelQuoteResultService.findByHotelQuoteConditionUuid(hqc.getUuid());
			List<HotelQuoteResultDetail> hotelQuoteResultDetail = hotelQuoteResultDetailService.findByHotelQuoteConditionUuid(hqc.getUuid());
			Map<String,List<HotelQuoteResultDetail>> detailMap = new TreeMap<String,List<HotelQuoteResultDetail>>();
			for(HotelQuoteResultDetail detail:hotelQuoteResultDetail){
				String key = detail.getInDate()+";"+detail.getHotelRoomUuid()+";"+detail.getHotelMealUuid();
				if(!detailMap.containsKey(key)){
					detailMap.put(key, new ArrayList<HotelQuoteResultDetail>());
				}
				detailMap.get(key).add(detail);
			}
			if(hqc.getSupplierInfoId()!=null){
				SupplierInfo supplierInfo= supplierInfoService.findSupplierInfoById(hqc.getSupplierInfoId().longValue());
				hqc.setSupplierInfo(supplierInfo);
			}
			hqc.setHotelQuoteResult(hotelQuoteResult);
			hqc.setRelMap(detailMap);
			hqc.setConditionDetailPersonNum(conditionDetailPersonNum);
			
			//添加复制功能信息
			List<ConditionDetail> conditionDetails = new ArrayList<ConditionDetail>();
			List<RoomNight> roomNights = new ArrayList<RoomNight>();
			List<String> meals = new ArrayList<String>();
			List<String> rooms = new ArrayList<String>();
			TotalPrice totalPrice = new TotalPrice();
			List<GuestPrice> guesttotalPrices = new ArrayList<GuestPrice>();
			TotalPrice preferentialTotalPrice = new TotalPrice();
			List<GuestPrice> guestpreferentialTotalPrices = new ArrayList<GuestPrice>();
			String currencyText = "￥";//报价的结果现在只有人民币
			
			if(!detailMap.isEmpty()){
				 for (Map.Entry<String,List<HotelQuoteResultDetail>> entry : detailMap.entrySet()){
					 ConditionDetail conditionDetail = new ConditionDetail();
					 String[] key = entry.getKey().split(";");
					 conditionDetail.setInDate(key[0].substring(0, 10));//只取年月日
					 String roomText = roomService.getByUuid(key[1]).getRoomName();
					 conditionDetail.setRoomType(roomText);
					 rooms.add(roomText);
					 if(key[2]!=null&&!"null".equals(key[2])){
						 String meal = mealService.getByUuid(key[2]).getMealName();
						 conditionDetail.setMealType(meal);
						 meals.add(meal);
					 }
					 conditionDetail.setIslandWay(sysCompanyDictViewService.findByUuid(conditionList.get(0).getArrivalIslandWay()).getLabel());
					 List<GuestPrice> guestPrices = new ArrayList<GuestPrice>();
					 for(HotelQuoteResultDetail detail:entry.getValue()){
						 if(2==detail.getPriceType().intValue()){
                             if(hotelGuestTypeService.getByUuid(detail.getTypeUuid())!=null){
                            	 GuestPrice  guestPrice = new GuestPrice();
								 guestPrice.setAmount(detail.getPrice());
								 guestPrice.setCurrencyText(currencyText);
								 guestPrice.setTravelerTypeText(hotelGuestTypeService.getByUuid(detail.getTypeUuid()).getName());
								 guestPrices.add(guestPrice);
                             }
						 }else{
							 if(travelerTypeService.getTravelerName(detail.getTypeUuid())!=null){
								 GuestPrice  guestPrice = new GuestPrice();
								 guestPrice.setAmount(detail.getPrice());
								 guestPrice.setCurrencyText(currencyText);
								 guestPrice.setTravelerTypeText(travelerTypeService.getTravelerName(detail.getTypeUuid()).getName());
								 guestPrices.add(guestPrice);
							 }
						 }
					 }
					 conditionDetail.setGuestPrices(guestPrices);
					 conditionDetails.add(conditionDetail);
				 }
			}
			copyJson.setConditionDetails(conditionDetails);
			Object[] dates = (Object[])hotelQuoteResultService.getDates(hqc.getUuid());
			copyJson.setBeginDate((Date)dates[0]);
			copyJson.setEndDate((Date)dates[1]);
			for(int k=0;k<rooms.size();k++){
				String roomtype = rooms.get(k);
				String preRoomtype = "";
				if(k>0){
					preRoomtype = rooms.get(k-1);
				}
				if(preRoomtype.length()>0&&preRoomtype.equals(roomtype)){
					roomNights.get(roomNights.size()-1).setNight(roomNights.get(roomNights.size()-1).getNight()+1);
				}else{
					RoomNight roomNight = new RoomNight();
					roomNight.setRoomText(roomtype);
					roomNight.setNight(1);
					roomNights.add(roomNight);
				}
			}
			copyJson.setRoomNights(roomNights);
			String mealTexts = Arrays.toString(meals.toArray());
			if(mealTexts.length()>2){
			   copyJson.setMeals(mealTexts.substring(1, mealTexts.length()-1));
			}
			copyJson.setIslandWays(sysCompanyDictViewService.findByUuid(conditionList.get(0).getArrivalIslandWay()).getLabel()+","
			+sysCompanyDictViewService.findByUuid(conditionList.get(0).getDepartureIslandWay()).getLabel());
			for(HotelQuoteResult result:hotelQuoteResult){
				if(1==result.getPriceType().intValue()){
					if(travelerTypeService.getByUuid(result.getTypeUuid())!=null){
						GuestPrice price = new GuestPrice();
						GuestPrice preferentialPrice = new GuestPrice();
						price.setAmount(result.getPrice());
						price.setCurrencyText(currencyText);
						price.setTravelerTypeText(travelerTypeService.getByUuid(result.getTypeUuid()).getName());
						preferentialPrice.setAmount(result.getPreferentialPrice());
						preferentialPrice.setCurrencyText(currencyText);
						preferentialPrice.setTravelerTypeText(travelerTypeService.getByUuid(result.getTypeUuid()).getName());
						guesttotalPrices.add(price);
						guestpreferentialTotalPrices.add(preferentialPrice);
					}
				}else if(2==result.getPriceType().intValue()){
					if(hotelGuestTypeService.getByUuid(result.getTypeUuid())!=null){
						GuestPrice price = new GuestPrice();
						GuestPrice preferentialPrice = new GuestPrice();
						price.setAmount(result.getPrice());
						price.setCurrencyText(currencyText);
						price.setTravelerTypeText(hotelGuestTypeService.getByUuid(result.getTypeUuid()).getName());
						preferentialPrice.setAmount(result.getPreferentialPrice());
						preferentialPrice.setCurrencyText(currencyText);
						preferentialPrice.setTravelerTypeText(hotelGuestTypeService.getByUuid(result.getTypeUuid()).getName());
						guesttotalPrices.add(price);
						guestpreferentialTotalPrices.add(preferentialPrice);
					}
				}else if(3==result.getPriceType().intValue()){
					totalPrice.setMixlivePrice(result.getPrice());
					totalPrice.setMixlivePriceCurrencyText(currencyText);
					preferentialTotalPrice.setMixlivePrice(result.getPreferentialPrice());
					preferentialTotalPrice.setMixlivePriceCurrencyText(currencyText);
				}else{//打包价格
					totalPrice.setTotalPrice(result.getPrice());
					totalPrice.setTotalPriceCurrencyText(currencyText);
					preferentialTotalPrice.setTotalPrice(result.getPreferentialPrice());
					preferentialTotalPrice.setTotalPriceCurrencyText(currencyText);
				}
			}
			totalPrice.setGuestPrices(guesttotalPrices);
			preferentialTotalPrice.setGuestPrices(guestpreferentialTotalPrices);
			copyJson.setTotalPrice(totalPrice);
			copyJson.setPreferentialTotalPrice(preferentialTotalPrice);
		}
		String json = JSON.toJSONStringWithDateFormat(copyJson, "yyyy-MM-dd");//生成复制功能的数据
		mav.addObject("resultCopy", json);
		mav.addObject("conditionList", conditionList);
		//封装已使用优惠的json数据
		StringBuffer sbjson = new StringBuffer();
		if(CollectionUtils.isNotEmpty(conditionList)){
			for(HotelQuoteCondition quotecondition :conditionList){	
				PreferentialJsonBean preferentialJsonBean = new PreferentialJsonBean();
				List<HotelPlPreferential> preferentialList = hotelQuotePreferentialService.findQuotePreferentialsByHotelQuoteUuid(quotecondition.getUuid());//hotel_quote_condition_uuid
				if(CollectionUtils.isNotEmpty(preferentialList)){
					preferentialJsonBean.setPreferentialList(preferentialList);
					quotecondition.setSort(-1);//原为排序字段，暂时作为有无优惠标识：-1是有优惠
				}
				List<Map<String,Object>> guestpriceList = hotelQuoteResultService.getByQuoteUuid(quotecondition.getUuid());//hotel_quote_condition_uuid
				List<GuestPriceJsonBean> guestpriceJsonList = new ArrayList<GuestPriceJsonBean>();
				for (Map<String,Object> jsonbean : guestpriceList) {//循环中的key要与sql中的别名对应
					if ("混住费用".equals(jsonbean.get("travelerTypeText"))) {
						if(jsonbean.get("amount")!=null){
						preferentialJsonBean.setMixlivePrice(Double.parseDouble(jsonbean.get("amount").toString()));
						}
						if(jsonbean.get("currencyText")!=null){
							preferentialJsonBean.setMixlivePriceCurrencyText((String)jsonbean.get("currencyText"));
						}
						if(jsonbean.get("currencyId")!=null){
							preferentialJsonBean.setMixlivePriceCurrencyId(jsonbean.get("currencyId").toString());
						}
						continue;
					}
					if ("打包价格".equals(jsonbean.get("travelerTypeText"))) {
						if(jsonbean.get("amount")!=null){
						preferentialJsonBean.setTotalPrice(Double.parseDouble(jsonbean.get("amount").toString()));
						}
						if(jsonbean.get("currencyText")!=null){
							preferentialJsonBean.setTotalPriceCurrencyText((String)jsonbean.get("currencyText"));
						}
						if(jsonbean.get("currencyId")!=null){
							preferentialJsonBean.setTotalPriceCurrencyId(jsonbean.get("currencyId").toString());
						}
						continue;
					}
					GuestPriceJsonBean guestJson = new GuestPriceJsonBean();
					if(jsonbean.get("amount")!=null){
						guestJson.setAmount(Double.parseDouble(jsonbean.get("amount").toString()));
					}
					if(jsonbean.get("currencyId")!=null){
						guestJson.setCurrencyId(jsonbean.get("currencyId").toString());
					}
					if(jsonbean.get("currencyText")!=null){
						guestJson.setCurrencyText((String)jsonbean.get("currencyText"));
					}
					if(jsonbean.get("travelerType")!=null){
						guestJson.setTravelerType((String)jsonbean.get("travelerType"));
					}
					if(jsonbean.get("travelerTypeText")!=null){
						guestJson.setTravelerTypeText((String)jsonbean.get("travelerTypeText"));
					}
					if(jsonbean.get("preferAmount")!=null){//优惠的金额
						guestJson.setPreferAmount(Double.parseDouble(jsonbean.get("preferAmount").toString()));
					}
					guestpriceJsonList.add(guestJson);
				}
				preferentialJsonBean.setGuestPriceList(guestpriceJsonList);
				String jsondate = JSON.toJSONStringWithDateFormat(preferentialJsonBean, "yyyy-MM-dd");
				StringBuffer sb = new StringBuffer(jsondate);
				sb.insert(0, "\""+quotecondition.getUuid()+"\":");
				sbjson.append(sb.toString()+",");
				sb.setLength(0);
			}
		}
		sbjson.deleteCharAt(sbjson.length()-1).insert(0, "{").insert(sbjson.length()-1, "}");
		mav.addObject("jsondate", sbjson.toString());
		//封装已使用优惠的json数据
		mav.setViewName(SHOW_DETAIL);
		return mav;
	}
	
	/**
	 * 保存报价信息
	*<p>Title: saveQuotedPriceInfo</p>
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-7-17 下午4:19:22
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "saveQuotedPriceInfo")
	public Object saveQuotedPriceInfo(String preferentialJsonData, String quotedPriceJsonData, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		
		String hotelQuoteJsonStr = request.getParameter("hotelQuoteJsonStr");
		
//		System.out.println(hotelQuoteJsonStr);
		
		try {
			HotelQuoteInput hotelQuoteInput = JSON.parseObject(hotelQuoteJsonStr,HotelQuoteInput.class);
			hotelQuoteService.saveQuotedPriceInfo(hotelQuoteInput);
			datas.put("result", "1");
			datas.put("message", "报价信息保存成功！！！");
		} catch (Exception e) {
			logger.error("保存报价记录异常！",e);
			e.printStackTrace();
			datas.put("result", "2");
			datas.put("message", "报价信息保存成功！！！");
		}
		
		return datas;
	}
}
