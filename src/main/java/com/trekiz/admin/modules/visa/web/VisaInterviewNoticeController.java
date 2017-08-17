package com.trekiz.admin.modules.visa.web;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.BatchRecordDao;
import com.trekiz.admin.modules.sys.repository.BatchTravelerRelationDao;
import com.trekiz.admin.modules.sys.repository.CountryDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNotice;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNoticeAddress;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNoticeTraveler;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaInterviewNoticeAddressDao;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaFlowBatchOprationService;
import com.trekiz.admin.modules.visa.service.VisaInterviewNoticeAddressService;
import com.trekiz.admin.modules.visa.service.VisaInterviewNoticeService;
import com.trekiz.admin.modules.visa.service.VisaInterviewNoticeTravelerService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;


@Controller
@RequestMapping(value="${adminPath}/visa/interviewNotice")
public class VisaInterviewNoticeController extends BaseController {

	@Autowired
	private VisaInterviewNoticeService visaInterviewNoticeService;
	
	@Autowired
	private VisaInterviewNoticeTravelerService visaInterviewNoticeTravelerService;
	
	@Autowired
	private IVisaProductsService visaProductsService;
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private CountryDao countryDao;

	@Autowired
	private VisaInterviewNoticeAddressService visaInterviewNoticeAddressService;
	
	@Autowired
	private VisaInterviewNoticeAddressDao visaInterviewNoticeAddressDao;
	
	@Autowired
	private VisaProductsDao visaProductsDao;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private SysBatchNoService sysBatchNoService;	
	
	@Autowired
	private BatchRecordDao batchRecordDao;
	
	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	
	@Autowired
	private VisaFlowBatchOprationService visaFlowBatchOprationService;
	
	@Autowired
	private BatchTravelerRelationService batchTravelerRelationService;
	
	/**
	 * 预约表信息列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){
		String orderId=request.getParameter("orderId");
		if(orderId==null){
			return null;
		}
		List<VisaInterviewNotice> list=visaInterviewNoticeService.list(StringUtils.toLong(orderId));
		for(VisaInterviewNotice bean:list){
			//兼容历史数据
			try{
				JSONArray json = JSONArray.fromObject(bean.getArea());
				String str = "";
				for(int i=0;i<json.size();i++){
					if(i==json.size()-1){
						str+=((Map<String,String>)(json.get(i))).get("areaName");
					}else{
						str+=((Map<String,String>)(json.get(i))).get("areaName")+",";
					}
				}
				bean.setArea(str);
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
		
		//--0214--新增面签附件功能-s//
		   //根据orderId查询表visa_order_file表,获得该订单已关联的文档id(真正查询的时候,有带上删除标记delFlag=0这一条件)
		 List<Map<String,Object>> docInfoList=visaInterviewNoticeService.findDocInfoListByOrderId(orderId);
		//--0214--新增面签附件功能-e//
		model.addAttribute("list", list);
		//--0214--新增面签附件功能-s//
		model.addAttribute("visaOrderId", orderId);
		model.addAttribute("docInfoList", docInfoList);
		//--0214--新增面签附件功能-e//
        return "modules/visa/visaInterviewNoticeList";
	}
	
	/**
	 * 约签人信息列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="travelerList")
	public String travelerList(HttpServletRequest request,HttpServletResponse response,Model model){
		String interviewId=request.getParameter("interviewId");
		if(interviewId==null){
			return null;
		}
		List<Map<String, Object>> travelerList=visaInterviewNoticeService.getTravelerInfos(StringUtils.toLong(interviewId));
		JSONArray json=JSONArray.fromObject(travelerList);
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 删除预约表记录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="delete")
	public String delete(HttpServletRequest request,HttpServletResponse response,Model model){
		String id=request.getParameter("id");
		if(id==null){
			return null;
		}
		visaInterviewNoticeService.delete(StringUtils.toLong(id));
		JSONObject json=JSONObject.fromObject("{msg:'ok'}");
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 跳转至新建页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toCreate")
	public String toCreate(HttpServletRequest request,HttpServletResponse response,Model model){
		String orderId=request.getParameter("orderId");
		if(orderId==null){
			return null;
		}
		List<Object> travelers=visaInterviewNoticeService.getUnvisaTravelers(StringUtils.toLong(orderId));
		model.addAttribute("travelers", travelers);
		
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		model.addAttribute("countryInfoList", countryInfoList);
		
		List<Map<String, Object>> areas=visaInterviewNoticeService.getAreaInfoByOrderId(StringUtils.toLong(orderId));
		model.addAttribute("areas", areas);
		VisaOrder order  = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(order.getVisaProductId());
		model.addAttribute("contractPersion", visaProducts.getContactPerson());
		//需求198-版本0419-将国家.领区由下拉形式改为文本形式-djw--start
		Country country = CountryUtils.getCountry(Long.parseLong(visaProducts.getSysCountryId().toString()));
		model.addAttribute("country", country);
		model.addAttribute("visaProducts", visaProducts);
		//需求198-版本0419-将国家.领区由下拉形式改为文本形式-djw--end
        return "modules/visa/visaInterviewNoticeCreate";
	}
	
	@RequestMapping(value="areaInfo")
	public String areaInfo(HttpServletRequest request,HttpServletResponse response,Model model){
		String orderId=request.getParameter("orderId");
		if(orderId==null){
			return null;
		}
		List<Map<String, Object>> areas=visaInterviewNoticeService.getAreaInfoByOrderId(StringUtils.toLong(orderId));
		JSONArray json=JSONArray.fromObject(areas);
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 新建保存预约表信息
	 * @param orderId
	 * @param country
	 * @param area
	 * @param address
	 * @param interviewTime
	 * @param explainationTime
	 * @param contactMan
	 * @param contactWay
	 * @param travelers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="doCreate")
	public String doCreate(
			Long orderId,
			String country,
			String area,
			String address,
			String interviewTime,
			String explainationTime,
			String contactMan,
			String contactWay,
			String travelers,
			String areaIds,
			String areaNames,
			HttpServletRequest request,HttpServletResponse response,Model model){
		
		VisaInterviewNotice o=new VisaInterviewNotice();
		o.setOrderId(orderId);
		String[] areaIdArray = areaIds.split(",");
		String[] areaNameArray = areaNames.split(",");
		JSONArray areaJson =new JSONArray();
		for(int i = 0 ;i<areaIdArray.length;i++){
			if(StringUtils.isBlank(areaIdArray[i])){
				continue;
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("areaId", areaIdArray[i]);
			map.put("areaName", areaNameArray[i]);
			areaJson.add(map);
		}
		
		Country contry = countryDao.getCountryById(Long.parseLong(country));
		o.setCountry(contry.getCountryName_cn());
		o.setArea(areaJson.toString());
		o.setAddress(address);
		o.setInterviewTime(DateUtils.dateFormat(interviewTime));
		o.setExplainationTime(DateUtils.dateFormat(explainationTime));
		o.setCreateTime(new Date());
		o.setContactMan(contactMan);
		o.setContactWay(contactWay);
		int result=visaInterviewNoticeService.add(o);
		
		if(result==0){
			return null;
		}
		
		String[] ts=travelers.split("&");
		List<VisaInterviewNoticeTraveler> tList=null;
		if(ts.length>0){
			tList=new ArrayList<VisaInterviewNoticeTraveler>();
			VisaInterviewNoticeTraveler traveler;
			String[] map;
			for (String t : ts) {
				if(StringUtils.isBlank(t)){
					continue;
				}
				traveler=new VisaInterviewNoticeTraveler();
				map=t.split(":");
				traveler.setInterviewId(o.getId());
				traveler.setTravalerId(StringUtils.toLong(map[0]));
				traveler.setTravalerName(map[1]);
				tList.add(traveler);
			}
			visaInterviewNoticeTravelerService.add(tList);
		}
		
		JSONObject json=JSONObject.fromObject("{msg:'ok'}");
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 跳转至编辑页面
	 * @param request 
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value="toUpdate")
	public String toUpdate(HttpServletRequest request,HttpServletResponse response,Model model){
		String interviewId=request.getParameter("interviewId");
		if(interviewId==null){
			return null;
		}
		String orderId=request.getParameter("orderId");
		if(orderId==null){
			return null;
		}
		
		VisaInterviewNotice o=visaInterviewNoticeService.getById(StringUtils.toLong(interviewId));
		try{
			JSONArray json = JSONArray.fromObject(o.getArea());
			String str = "";
			String strId ="";
			for(int i=0;i<json.size();i++){
				if(i==json.size()-1){
					str+=((Map<String,String>)(json.get(i))).get("areaName");
					strId+=((Map<String,String>)(json.get(i))).get("areaId");
					 
				}else{
					str+=((Map<String,String>)(json.get(i))).get("areaName")+",";
					strId+=((Map<String,String>)(json.get(i))).get("areaId")+",";
				}
			}
			String[] arr=str.split(",");
			String[] arrId=strId.split(",");
			model.addAttribute("arr",arr);
			model.addAttribute("arrId",arrId);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		model.addAttribute("interview", o);
		
		List<Object> travelers=visaInterviewNoticeService.getUnvisaTravelers(StringUtils.toLong(orderId));
		model.addAttribute("travelers", travelers);
		
		List<Object> myTravelers=visaInterviewNoticeService.getTravelersBySId(StringUtils.toLong(interviewId));
		model.addAttribute("myTravelers", myTravelers);
		
		
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		model.addAttribute("countryInfoList", countryInfoList);
		
		List<Map<String, Object>> areas=visaInterviewNoticeService.getAreaInfoByOrderId(StringUtils.toLong(orderId));
		model.addAttribute("areas", areas);
        return "modules/visa/visaInterviewNoticeUpdate";
	}
	
	/**
	 * 编辑保存预约表信息
	 * @param interviewId
	 * @param country
	 * @param area
	 * @param address
	 * @param interviewTime
	 * @param explainationTime
	 * @param contactMan
	 * @param contactWay
	 * @param travelers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="doUpdate")
	public String doUpdate(
			Long interviewId,
			String country,
			String area,
			String address,
			String interviewTime,
			String explainationTime,
			String contactMan,
			String contactWay,
			String travelers,
			String areaIds,
			String areaNames,
			HttpServletRequest request,HttpServletResponse response,Model model){
		
		VisaInterviewNotice o=new VisaInterviewNotice();
		o.setId(interviewId);
		
		String[] areaIdArray = areaIds.split(",");
		String[] areaNameArray = areaNames.split(",");
		JSONArray areaJson =new JSONArray();
		for(int i = 0 ;i<areaIdArray.length;i++){
			if(StringUtils.isBlank(areaIdArray[i])){
				continue;
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("areaId", areaIdArray[i]);
			map.put("areaName", areaNameArray[i]);
			areaJson.add(map);
		}
		
		Country contry = countryDao.getCountryById(Long.parseLong(country));
		o.setCountry(contry.getCountryName_cn());
		o.setArea(areaJson.toString());
		o.setAddress(address);
		o.setInterviewTime(DateUtils.dateFormat(interviewTime));
		o.setExplainationTime(DateUtils.dateFormat(explainationTime));
		o.setCreateTime(new Date());
		o.setContactMan(contactMan);
		o.setContactWay(contactWay);
		
		int result=visaInterviewNoticeService.update(o);
		if(result==0){
			return null;
		}
		
		visaInterviewNoticeTravelerService.delete(interviewId);
		
		String[] ts=travelers.split("&");
		List<VisaInterviewNoticeTraveler> tList=null;
		if(ts.length>0){
			tList=new ArrayList<VisaInterviewNoticeTraveler>();
			VisaInterviewNoticeTraveler traveler;
			String[] map;
			for (String t : ts) {
				traveler=new VisaInterviewNoticeTraveler();
				map=t.split(":");
				traveler.setInterviewId(interviewId);
				traveler.setTravalerId(StringUtils.toLong(map[0]));
				traveler.setTravalerName(map[1]);
				tList.add(traveler);
			}
			visaInterviewNoticeTravelerService.add(tList);
		}
		
		JSONObject json=JSONObject.fromObject("{msg:'ok'}");
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 预览预约表信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value="preview")
	public String preview(HttpServletRequest request,HttpServletResponse response,Model model){
		String interviewId=request.getParameter("interviewId");
		if(interviewId==null){
			return null;
		}
		
		VisaInterviewNotice o=visaInterviewNoticeService.getById(StringUtils.toLong(interviewId));
		try{
			JSONArray json = JSONArray.fromObject(o.getArea());
			String str = "";
			for(int i=0;i<json.size();i++){
				if(i==json.size()-1){
					str+=((Map<String,String>)(json.get(i))).get("areaName");
				}else{
					str+=((Map<String,String>)(json.get(i))).get("areaName")+",";
				}
			}
			o.setArea(str);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		model.addAttribute("interview", o);
		
		List<Object> myTravelers=visaInterviewNoticeService.getTravelersBySId(StringUtils.toLong(interviewId));
		model.addAttribute("myTravelers", myTravelers);
		
        return "modules/visa/visaInterviewNoticePreview";
	}
	
	public String encode(String target){
		try {
			return new String(target.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询签证国家领区
	 * @author nan
	 * @date 2015年8月25日 上午11:09:01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="visaInterviewNoticeAddress")
	public String visaInterviewNoticeAddress(HttpServletRequest request,HttpServletResponse response,Model model){
		//公司ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		//所属公司的签证国家及领区
		List<Map<Object, Object>> visaInterviewNoticeAddressList = visaInterviewNoticeAddressService.list(companyId);
		model.addAttribute("visaInterviewNoticeAddressList", visaInterviewNoticeAddressList);
		//国家
		List<Country> countryList = countryDao.getCountrys();
		model.addAttribute("countryList",countryList);
		//领区
		model.addAttribute("areaList", DictUtils.getDictList("from_area"));
		return "modules/visa/visaInterviewNoticeAddressList";
	}
	
	/**
	 * 添加签证国家领区
	 * @author nan
	 * @date 2015年8月25日 上午11:09:01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveVisaAddress")
	public Map<String, Object> saveVisaAddress(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		//国家ID
		String countryId = request.getParameter("country");
		//领区ID
		String area = request.getParameter("area");
		//备注
		String remark = request.getParameter("remark");
		//修改ID
		String id = request.getParameter("id");
		//修改前领区ID
		String oldArea = request.getParameter("oldArea");
		//公司ID
		Long companyId = UserUtils.getUser().getCompany().getId();

		//查看此签证国家领区是否已存在
		List<VisaInterviewNoticeAddress> visaInterviewNoticeAddressList = null;
		//id不为空说明是修改，旧的领区和新的领区不一致时验证
		if(!area.equals(oldArea)){
			visaInterviewNoticeAddressList = visaInterviewNoticeAddressService.findByCountryIdAndArea(Long.parseLong(countryId), area, companyId);
		}
		//重复
		if(visaInterviewNoticeAddressList != null && visaInterviewNoticeAddressList.size() > 0){
			map.put("result", "0");
		}else{
			//修改
			if(StringUtils.isNotBlank(id)){
				VisaInterviewNoticeAddress visaInterviewNoticeAddress = visaInterviewNoticeAddressDao.findOne(Long.parseLong(id));
				List<VisaProducts> visaProductsList = visaProductsDao.findVisaProductsByCountryAndArea(Integer.parseInt(countryId), oldArea, companyId);
				if(visaProductsList != null && visaProductsList.size() > 0 && !area.equals(oldArea)){
					map.put("result", "2");
				}else{
					visaInterviewNoticeAddress.setArea(area);
					visaInterviewNoticeAddress.setRemark(remark);
					visaInterviewNoticeAddressDao.save(visaInterviewNoticeAddress);
					map.put("result", "1");
				}
			}else{
				//新增
				VisaInterviewNoticeAddress visaInterviewNoticeAddress = new VisaInterviewNoticeAddress();
				visaInterviewNoticeAddress.setCountryId(Long.parseLong(countryId));
				visaInterviewNoticeAddress.setArea(area);
				visaInterviewNoticeAddress.setRemark(remark);
				visaInterviewNoticeAddress.setCompanyId(companyId);
				visaInterviewNoticeAddressDao.save(visaInterviewNoticeAddress);
				map.put("result", "1");
			}
		}
		return map;
	}
	
	/**
	 * 删除签证国家领区
	 * @author nan
	 * @date 2015年8月25日 上午11:09:01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteVisaAddress")
	public Map<String, Object> deleteVisaAddress(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		//国家ID
		String countryId = request.getParameter("countryId");
		//领区ID
		String area = request.getParameter("area");
		//公司ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		List<VisaProducts> visaProductsList = visaProductsDao.findVisaProductsByCountryAndArea(Integer.parseInt(countryId), area, companyId);
		if(visaProductsList != null && visaProductsList.size() > 0){
			map.put("result", "0");
		}else{
			//删除签证国家及领区关系
			int flag = visaInterviewNoticeAddressService.deleteByCountryIdAndArea(Long.parseLong(countryId), area, companyId);
			if(flag==1){
				map.put("result", "1");
			}else{
				map.put("result", "0");
			}
		}
		return map;
	}
	
	/**
	 * 批量删除签证国家领区
	 * @author nan
	 * @date 2015年8月25日 上午11:09:01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchDelete")
	public Map<String, Object> batchDelete(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		//关系ID
		String listIds = request.getParameter("listIds");
		//公司ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		//关系ID数组
		String[] listIdsArray = listIds.split(",");
		//循环校验签证国家及领区是否发不过产品
		for(int i = 0;i<listIdsArray.length;i++){
			VisaInterviewNoticeAddress visaInterviewNoticeAddress = visaInterviewNoticeAddressDao.findOne(Long.parseLong(listIdsArray[i]));
			List<VisaProducts> visaProductsList = visaProductsDao.findVisaProductsByCountryAndArea(visaInterviewNoticeAddress.getCountryId().intValue(), visaInterviewNoticeAddress.getArea(), companyId);
			if(visaProductsList != null && visaProductsList.size() > 0){
				String countryName = CountryUtils.getCountryName(visaInterviewNoticeAddress.getCountryId());
				String area = DictUtils.getDictLabel(visaInterviewNoticeAddress.getArea(), "from_area", "");
				map.put("result", "2");
				map.put("msg", "国家："+countryName+"<br/>领区："+area+"<br/>");
				return map;
			}
		}
		
		//批量删除签证国家及领区
		if(visaInterviewNoticeAddressService.batchDelete(listIds)){
			map.put("result", "1");
		}else{
			map.put("result", "0");
		}
		return map;
	}
   /**
    * 0214需求,面签通知页面新增附件上传	
    * @param request
    * @return
    */
   @RequestMapping("saveAttachedFile")
   @ResponseBody
   public Boolean saveAttachedFile(HttpServletRequest request){
	   //System.out.println("-----"+request.getParameter("visaOrderId")+"---"+request.getParameter("docinfoids"));
	   //获取订单id
	   String visaOrderId=request.getParameter("visaOrderId");
	   //获取附件id的字符串
	   String docInfoIds=request.getParameter("docinfoids");
	   //假设每次进行保存操作,都是生成了新的记录,旧的记录都是的删除标记都设置为1,表示删除了
	   visaInterviewNoticeService.updateDelFlagByVisaOrderId(visaOrderId);
	   try {
		visaInterviewNoticeService.save(visaOrderId,docInfoIds);
		//面签通知页面的附件和和订单成功建立关系,则返回true
		return true;
	} catch (Exception e) {
		//面签通知页面的附件和和订单未成功建立关系,则返回false
		e.printStackTrace();
		return false;
	}
   }
   
   
   /**
	 * 批量设置面签通知
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value="batchDoCreate")
	public Map<String, String> batchDoCreate(HttpServletRequest request,HttpServletResponse response,Model model){
		
    	Map<String,String> mapMsg = new HashMap<String, String>();
    	mapMsg.put("msg", null);
		//Long orderId = Long.parseLong(request.getParameter("orderId"));
//		String country = request.getParameter("country");
//		String area = request.getParameter("area");
//		String address = request.getParameter("address");
//		String interviewTime = request.getParameter("interviewTime");
//		String explainationTime = request.getParameter("explainationTime");
//		String contactMan = request.getParameter("contactMan");
//		String contactWay = request.getParameter("contactWay");
//		String travelers = request.getParameter("travelers");
//		String areaIds = request.getParameter("areaIds");
//		String areaNames = request.getParameter("areaNames");
    	String visaIds = request.getParameter("visaIds") + "0";
    	String travelerIds = request.getParameter("travelerIds") + "0";
		String orderIds = request.getParameter("orderIds") + "0";
		String visaCountryIds = request.getParameter("visaCountryIds") + "0";
		String collarZonings = request.getParameter("collarZonings") + "0";
		String salerNames = request.getParameter("salerNames") + "0";
		String interviewPlaces = request.getParameter("interviewPlaces") + "0";
		String interviewTimes = request.getParameter("interviewTimes") + "0";
		String explainationTimes = request.getParameter("explainationTimes") + "0";
		String contactMans = request.getParameter("contactMans") + "0";
		String contactWays = request.getParameter("contactWays") + "0";
		String travelerNums = request.getParameter("travelerNums") + "0";
		
		String[] visaIDS = visaIds.split(",");
		String[] travelerIDS = travelerIds.split(",");
		String[] orderIDS = orderIds.split(",");
		String[] visaCountryIDS = visaCountryIds.split(",");
		String[] collarZoningS = collarZonings.split(",");
		String[] salerNameS = salerNames.split(",");
		String[] interviewPlaceS = interviewPlaces.split(",");
		String[] interviewTimeS = interviewTimes.split(",");
		String[] explainationTimeS = explainationTimes.split(",");
		String[] contactManS = contactMans.split(",");
		String[] contactWayS = contactWays.split(",");
		String[] travelerNumS = travelerNums.split(",");
		String[] count = travelerIds.split(",");  //用于下面的计数
		//VisaFlowBatchOpration record = new VisaFlowBatchOpration();
		// 生成批次号
		String batchNo = sysBatchNoService.getVisaInterviewNoticeBatchNo();
		// 批次总人数
		int batchPersonCount = travelerIDS.length - 1;
		//生成uuid
		String batchUuid = UUID.randomUUID().toString();
		
		try {
			visaFlowBatchOprationService.updateBySql(batchNo, batchPersonCount, batchUuid);
			//visaFlowBatchOprationDao.getSession().save(record);
		} catch (Exception e) {
			e.printStackTrace();
			mapMsg.put("msg", "批量设置失败，请重试！");
			return mapMsg;
		}

		List<VisaInterviewNoticeTraveler> tList = new ArrayList<VisaInterviewNoticeTraveler>();
		for (int i = 0; i < count.length-1; i++) {
			if(count[i] != null){
				VisaInterviewNotice o=new VisaInterviewNotice();
				//Area area = areaService.findById(Long.parseLong(collarZoningS[i]));
				Dict dict = DictUtils.getDict(collarZoningS[i], "from_area");
				o.setOrderId(Long.parseLong(orderIDS[i]));
				JSONArray areaJson =new JSONArray();
				Map<String,String> map = new HashMap<String,String>();
				map.put("areaId", collarZoningS[i]);
				map.put("areaName", dict.getLabel());
				areaJson.add(map);
				
				Country contry = countryDao.getCountryById(Long.parseLong(visaCountryIDS[i]));
				o.setBatchNo(batchNo);
				o.setCountry(contry.getCountryName_cn());
				o.setArea(areaJson.toString());
				o.setAddress(interviewPlaceS[i]);
				o.setInterviewTime(DateUtils.dateFormat(interviewTimeS[i]));
				o.setExplainationTime(DateUtils.dateFormat(explainationTimeS[i]));
				o.setCreateTime(new Date());
				o.setContactMan(contactManS[i]);
				o.setContactWay(contactWayS[i]);
				o.setNum(Integer.parseInt(travelerNumS[i]));
				
				try {
					visaInterviewNoticeService.add(o);
				} catch (Exception e) {
					e.printStackTrace();
					mapMsg.put("msg", "批量设置失败，请重试！");
				}
				VisaInterviewNoticeTraveler traveler=new VisaInterviewNoticeTraveler();
				
				traveler.setInterviewId(o.getId());
				traveler.setTravalerId(Long.parseLong(travelerIDS[i]));
				traveler.setTravalerName(travelerService.findTravelerById(Long.parseLong(travelerIDS[i])).getName());
				tList.add(traveler);
				try {
					visaInterviewNoticeTravelerService.add(tList);
				} catch (Exception e) {
					e.printStackTrace();
					mapMsg.put("msg", "批量设置失败，请重试！");
				}
				
				for(int j = i+1; j < count.length-1; j++){
					if(count[j] != null){
						if(orderIDS[i].equals(orderIDS[j]) & interviewPlaceS[i].equals(interviewPlaceS[j]) & interviewTimeS[i].equals(interviewTimeS[j])
								& explainationTimeS[i].equals(explainationTimeS[j]) & contactManS[i].equals(contactManS[j])
								& contactWayS[i].equals(contactWayS[j])){
							
							traveler=new VisaInterviewNoticeTraveler();
							
							traveler.setInterviewId(o.getId());
							traveler.setTravalerId(Long.parseLong(travelerIDS[j]));
							traveler.setTravalerName(travelerService.findTravelerById(Long.parseLong(travelerIDS[j])).getName());
							tList.add(traveler);
							
							count[j] = null;
						}
					}
				}
				
				try {
					visaInterviewNoticeTravelerService.add(tList);
				} catch (Exception e) {
					e.printStackTrace();
					mapMsg.put("msg", "批量设置失败，请重试！");
				}
			}
				
		}
			
			//保存游客与批次的关系
			for(int i = 0; i < travelerIDS.length - 1; i++){
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					String uuid = UUID.randomUUID().toString();
					BatchTravelerRelation relation = new BatchTravelerRelation();
					relation.setUuid(uuid);
					relation.setBatchUuid(batchUuid);
					relation.setBatchRecordNo(batchNo);
					relation.setTravelerId(Long.parseLong(travelerIDS[i]));
					relation.setVisaId(Long.parseLong(visaIDS[i]));
					relation.setOrderId(Long.parseLong(orderIDS[i]));
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					relation.setTravelerName(traveler.getName());
					relation.setBusinessType(5);//业务类型 1:借款 2：还收据  3：借护照 4：还护照 5:面签通知
					relation.setCreatebyId(UserUtils.getUser().getId());
					relation.setCreatebyName(UserUtils.getUser().getName());
					relation.setSaveTime(new Date());
					try {
						
						batchTravelerRelationService.save(relation);
					} catch (Exception e) {
						e.printStackTrace();
						mapMsg.put("msg", "批量设置失败，请重试！");
					}
				}
			}
       return mapMsg;
	}
   
   
}
