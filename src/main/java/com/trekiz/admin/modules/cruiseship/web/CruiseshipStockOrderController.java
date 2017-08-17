/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.web;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.common.ExceptionConstants;
import com.trekiz.admin.common.exception.util.LogMessageUtil;
import com.trekiz.admin.common.exception.util.LogUtil;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockOrderInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockOrderQuery;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipInfoService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockOrderService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipStockOrder")
public class CruiseshipStockOrderController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipstockorder/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipStockOrder/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipstockorder/form";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipstockorder/show";
	private static final Log log = LogFactory.getLog(CruiseshipStockOrderController.class);
	
	@Autowired
	private CruiseshipStockOrderService cruiseshipStockOrderService;
	
	@Autowired
	private CruiseshipStockDetailService cruiseshipStockDetailService;
	
	private CruiseshipStockOrder dataObj;
	
	@Autowired
	private CruiseshipInfoService cruiseshipInfoService;
	
	@Autowired
	private CruiseshipStockService cruiseshipStockService;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=cruiseshipStockOrderService.getByUuid(uuid);
		}
	}
	
	/**
	 * 我的订单
	 * @param cruiseshipStockOrderQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "list")
	public String list(@ModelAttribute CruiseshipStockOrderQuery cruiseshipStockOrderQuery, BaseInput4MT input, HttpServletResponse response, Model model) {
		cruiseshipStockOrderQuery.setDelFlag("0");
		cruiseshipStockOrderQuery.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		String jsontext = input.getParam();
		JSONObject json = JSONObject.parseObject(jsontext);
		if(StringUtils.isNotBlank(json.get("cruiseshipStockUuid").toString())){
			cruiseshipStockOrderQuery.setCruiseshipStockUuid(json.get("cruiseshipStockUuid").toString());
		}
		cruiseshipStockOrderQuery.setCreateBy(UserUtils.getUser().getId().intValue());
       List<CruiseshipStockOrder> list = cruiseshipStockOrderService.find(cruiseshipStockOrderQuery); 
       Map<String,Object> myOrderInfo=new HashMap<String,Object>();
       Set<Map<String,Object>> activitys=new HashSet<Map<String,Object>>();
       Set<Map<String,Object>> cruiseshipCabin=new HashSet<Map<String,Object>>();
       Set<Map<String,Object>> depart=new HashSet<Map<String,Object>>();
       for(CruiseshipStockOrder cruiseshipStockOrder:list){
    	   Map<String,Object> activity=new HashMap<String,Object>();
    	   activity.put("activityId", cruiseshipStockOrder.getActivityId());
    	   activity.put("activityName", cruiseshipStockOrder.getActivityName());
    	   activity.put("activityType", cruiseshipStockOrder.getActivityType());
    	   activitys.add(activity);
    	   Map<String,Object> cabin=new HashMap<String,Object>();
    	   CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getByUuid(cruiseshipStockOrder.getCruiseshipStockDetailUuid());
    	   cabin.put("cruiseshipCabinUuid", cruiseshipStockDetail.getCruiseshipCabinUuid());
    	   cabin.put("cruiseshipCabinName", cruiseshipStockOrder.getCruiseshipCabinName());
    	   cruiseshipCabin.add(cabin);
    	   Map<String,Object> departMap=new HashMap<String,Object>();
    	   departMap.put("departureCityId", cruiseshipStockOrder.getDepartureCityId());
    	   departMap.put("departureCityName", cruiseshipStockOrder.getDepartureCityName());
    	   depart.add(departMap);
       }
       myOrderInfo.put("activity", activitys);
       myOrderInfo.put("cruiseshipCabin", cruiseshipCabin);
       myOrderInfo.put("depart", depart);
       List<Map<String, Object>> stockOrderMaps = cruiseshipStockOrderService.getOrderByGroup(json);
       for(Map<String,Object> map:stockOrderMaps){
    	   List<Map<String,Object>> find = cruiseshipStockOrderService.getStockOrders(map.get("cruiseshipCabinName").toString(), map.get("cruiseshipStockDetailUuid").toString(),json);
    	   map.put("stockOrders", find);
       }
       myOrderInfo.put("stockOrderMaps", stockOrderMaps);
       String jsonString = JSON.toJSONString(myOrderInfo);
        return jsonString;
	}
	/*@ResponseBody
	@RequestMapping(value="shipStockList")
	public String getShipStockList(CruiseshipStockQuery cruiseshipStockQuery){
		List<Map<String,Object>> list = cruiseshipStockService.getShipInfo();
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}*/
	@ResponseBody
	@RequestMapping(value="getShipStock")
	public String getShipStock(CruiseshipStockQuery cruiseshipStockQuery,HttpServletRequest request){
		cruiseshipStockQuery.setDelFlag("0");
		cruiseshipStockQuery.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		cruiseshipStockQuery.setCruiseshipInfoUuid(request.getParameter("cruiseshipStockQuery"));
		List<CruiseshipStock> list = cruiseshipStockService.find(cruiseshipStockQuery);
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}
	@RequestMapping(value = "form")
	public String form(CruiseshipStockOrderInput cruiseshipStockOrderInput, Model model) {
		model.addAttribute("cruiseshipStockOrderInput", cruiseshipStockOrderInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CruiseshipStockOrderInput cruiseshipStockOrderInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			cruiseshipStockOrderService.save(cruiseshipStockOrderInput);
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
		model.addAttribute("cruiseshipStockOrder", cruiseshipStockOrderService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CruiseshipStockOrder cruiseshipStockOrder = cruiseshipStockOrderService.getByUuid(uuid);
		CruiseshipStockOrderInput cruiseshipStockOrderInput = new CruiseshipStockOrderInput(cruiseshipStockOrder);
		model.addAttribute("cruiseshipStockOrderInput", cruiseshipStockOrderInput);
		return FORM_PAGE;
	}
	/**
	 * 修改订单信息 modify by chy 2016-2-4 11:10:18
	 * @param cruiseshipStockOrderInput
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(@ModelAttribute BaseInput4MT cruiseshipStockOrderInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			JSONArray jsonArray = JSONObject.parseArray(cruiseshipStockOrderInput.getParam());
			for(int i=0;i<jsonArray.size();i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				//订单UUID
				String uuid = obj.getString("orderUuid");//getUuid();
				CruiseshipStockOrder cruiseshipStockOrder = cruiseshipStockOrderService.getByUuid(uuid);
				//库存明细UUID
				String cruiseshipStockDetailUuid =cruiseshipStockOrder.getCruiseshipStockDetailUuid();//CruiseshipStockDetailUuid();
				CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getByUuid(cruiseshipStockDetailUuid);
				//修改后的男人数
				String mnum = obj.getString("mNum");
				// 男拼
				String mpiece = obj.getString("mPiece");
				//修改后的女人数
				String fnum = obj.getString("fNum");
				// 女拼
				String fpiece = obj.getString("fPiece");
				//原来的人数
				Integer allNum = cruiseshipStockOrder.getAllNum();
				//修改后的人数
				Integer allNumNew = Integer.parseInt(mnum) + Integer.parseInt(fnum);
				if(allNumNew>cruiseshipStockDetail.getFreePosition()+allNum){
					result=cruiseshipStockOrder.getCruiseshipCabinName();
					throw new RuntimeException("余位不足！");
				}
				cruiseshipStockOrder.setMnum(Integer.parseInt(mnum));
				cruiseshipStockOrder.setMpiece(Integer.parseInt(mpiece));
				cruiseshipStockOrder.setFnum(Integer.parseInt(fnum));
				cruiseshipStockOrder.setFpiece(Integer.parseInt(fpiece));
				cruiseshipStockOrder.setAllNum(allNumNew);
				//修改订单信息 
				cruiseshipStockOrderService.update(cruiseshipStockOrder);
				//记录订单操作日志
				logger.info(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_DINGDAN));
				//更新余位
				cruiseshipStockDetail.setFreePosition(cruiseshipStockDetail.getFreePosition() + allNum - allNumNew);
				cruiseshipStockDetail.setUpdateBy(UserUtils.getUser().getId().intValue());
				cruiseshipStockDetail.setUpdateDate(new Date());
				cruiseshipStockDetailService.save(cruiseshipStockDetail);
				//baseOut4MT.setResponseCode4Success();
			}
		} catch (Exception e) {
			e.printStackTrace();
//			baseOut4MT.setResponseCode4Fail();
//			baseOut4MT.putMsgCode(BaseOut4MT.CODE_0002);
			logger.error(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_DINGDAN));
			
		}
		//map.put("result", result);
		//baseOut4MT.setMsg(map);
		return result;
	}
	
	/**
	 * 库存订单批量删除接口 modify by chy 2016年2月5日11:32:04
	 * @param uuids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public BaseOut4MT delete(String uuids) {
		BaseOut4MT out = new BaseOut4MT();
		Map<String,String> datas = new HashMap<String, String>();
		boolean b = true;
		try {
			
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				
				//归还余位
				CruiseshipStockOrder cruiseshipStockOrder = new CruiseshipStockOrder();
				//CruiseshipStockDetail cruiseshipStockDetail = new CruiseshipStockDetail();
				for(String uuid : uuidArray){
					cruiseshipStockOrder = cruiseshipStockOrderService.getByUuid(uuid);
					CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getByUuid(cruiseshipStockOrder.getCruiseshipStockDetailUuid());
						cruiseshipStockDetail.setFreePosition(cruiseshipStockDetail.getFreePosition() + cruiseshipStockOrder.getAllNum());
						cruiseshipStockDetail.setUpdateDate(new Date());
						cruiseshipStockDetailService.update(cruiseshipStockDetail);
					b = cruiseshipStockOrderService.batchDelete(uuidArray);
					/*cruiseshipStockOrder = null;
					cruiseshipStockDetail = null;*/
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
		out.setMsg(datas);
		return out;
	}
	
	/**
	 * 库存游轮包名接口 生成库存游轮订单 by chy 2016年2月2日17:01:11
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="makeOrder")
//	public String makeCruiseshipStockOrder(HttpServletRequest request){
//		String result = "1";//返回结果 0 标示 异常 1 标示正常
//		try {
//			//组织参数
//			Map<String, Object> params = preparams(request);
//			//调用service方法 包名生成订单
//			cruiseshipStockOrderService.makeOrder(params);
//			logger.info(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
//		} catch (BaseException4Quauq e) {
//			logger.error(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
//			e.printStackTrace(LogUtil.getErrorStream(log));
//			result = "0";
//		}
//		return result;
//	}
	
	
	/**
	 * @author songyang
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="makeOrder")
	public String makeCruiseshipStockOrder(BaseInput4MT input){
		String result = "1";//返回结果 0 标示 异常 1 标示正常
		String jsontext = input.getParam();
//		JSONObject json =JSONObject.parseObject(jsontext.toString());
		JSONArray  json = JSONArray.parseArray(jsontext);
		try {
			//组织参数
			Map<String, Object> res = new HashMap<String,Object>();
			for (int i = 0; i < json.size(); i++) {
				JSONObject jsonObject =  json.getJSONObject(i);
				res.put("activityId", jsonObject.get("activityId"));//"产品表ID"
				res.put("cruiseshipStockUuid", jsonObject.get("cruiseshipStockUuid"));//"库存UUID"
				res.put("cruiseshipStockDetailUuid", jsonObject.get("cruiseshipStockDetailUuid"));//"库存明细UUID"
				res.put("cruiseshipCabinName", jsonObject.get("cruiseshipCabinName"));//"舱位名称"
				res.put("fnum", jsonObject.get("fnum"));//"女人数"
				res.put("mnum", jsonObject.get("mnum"));//"男人数"
				if(StringUtils.isBlank(jsonObject.getString("fpiece"))){
					res.put("fpiece", "1");//"女拼（拼：0；不拼：1；）"
				}else{
					res.put("fpiece", jsonObject.get("fpiece"));//"女拼（拼：0；不拼：1；）"
				}
				if(StringUtils.isBlank(jsonObject.getString("mpiece"))){
					res.put("mpiece", "1");//"男拼（拼：0；不拼：1；）"
				}else{
					res.put("mpiece", jsonObject.get("mpiece"));//"男拼（拼：0；不拼：1；）"
				}
				CruiseshipStockDetail stockDetail = cruiseshipStockDetailService.getByUuid(jsonObject.get("cruiseshipStockDetailUuid").toString());
				if(Integer.parseInt(("").equals(jsonObject.getString("fnum"))?"0":jsonObject.getString("fnum"))+Integer.parseInt(("").equals(jsonObject.getString("mnum"))?"0":jsonObject.getString("mnum"))>stockDetail.getFreePosition()){
					throw new BaseException4Quauq("余位不足，请重新填写");
				}
				res.put("allNum", Integer.parseInt(("").equals(jsonObject.getString("fnum"))?"0":jsonObject.getString("fnum"))+Integer.parseInt(("").equals(jsonObject.getString("mnum"))?"0":jsonObject.getString("mnum")));//"总人数"
				res.put("wholesalerId", UserUtils.getUser().getCompany().getId().intValue());//"批发商id"
			}
			//调用service方法 包名生成订单
			cruiseshipStockOrderService.makeOrder(res);
			logger.info(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
		} catch (BaseException4Quauq e) {
			logger.error(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
			e.printStackTrace(LogUtil.getErrorStream(log));
			result = "0";
		}
		return result;
	}
	

	/**
	 * 组织参数
	 * @param request
	 * @return
	 */
	private Map<String, Object> preparams(HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
//		res.put("activityId", request.getParameter("activityId"));//"产品表ID"
		res.put("activityId", request.getParameter("popActivetySelect"));//"产品表ID"
		res.put("cruiseshipStockUuid", request.getParameter("cruiseshipStockUuid"));//"库存UUID"
		res.put("cruiseshipStockDetailUuid", request.getParameter("cruiseshipStockDetailUuid"));//"库存明细UUID"
		res.put("cruiseshipCabinName", request.getParameter("cruiseshipCabinName"));//"舱位名称"
		res.put("sex", request.getParameter("sex"));//"性别（女：F，男：M）"
		res.put("fnum", request.getParameter("fnum"));//"女人数"
		res.put("mnum", request.getParameter("mnum"));//"男人数"
		if(StringUtils.isBlank(request.getParameter("fpiece"))){
			res.put("fpiece", "1");//"女拼（拼：0；不拼：1；）"
		}else{
			res.put("fpiece", request.getParameter("fpiece"));//"女拼（拼：0；不拼：1；）"
		}
		if(StringUtils.isBlank(request.getParameter("mpiece"))){
			res.put("mpiece", "1");//"男拼（拼：0；不拼：1；）"
		}else{
			res.put("mpiece", request.getParameter("mpiece"));//"男拼（拼：0；不拼：1；）"
		}
		res.put("allNum", Integer.parseInt(request.getParameter("fnum"))+Integer.parseInt(request.getParameter("mnum")));//"总人数"
		res.put("wholesalerId", request.getParameter("wholesalerId"));//"批发商id"
		return res;
	}
}
