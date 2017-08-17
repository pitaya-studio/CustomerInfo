/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.util.LogUtil;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockLog;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipAnnexQuery;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipInfoQuery;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockDetailQuery;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockGroupRelQuery;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockLogQuery;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipAnnexService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipCabinService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipInfoService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockGroupRelService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockLogService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockService;
import com.trekiz.admin.modules.mtourCommon.utils.ThreadVariable;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipStock")
public class CruiseshipStockController extends BaseController {
	
	private static final Log log = LogFactory.getLog(CruiseshipStockController.class);
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipstock/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipStock/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipstock/form";
	protected static final String ADD_PAGE = "modules/cruiseship/cruiseshipstock/cruiseShipStockAdd";
	protected static final String EDIT_PAGE = "modules/cruiseship/cruiseshipstock/cruiseShipStockEdit";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipstock/show";
	protected static final String STOCK_LIST_PAGE = "modules/cruiseship/cruiseshipstock/stockList";			//游轮库存列表页面
	protected static final String CREISESHIP_BOARD_PAGE = "modules/cruiseship/cruiseshipboard/cruiseshipboard";//奢华之旅看板页面
	protected static final String SHOW_PAGE_STOCK="modules/cruiseship/cruiseshipstock/cruiseShipStockShow";
	@Autowired
	private CruiseshipStockService cruiseshipStockService;
	@Autowired
	private CruiseshipInfoService cruiseshipInfoService;
	@Autowired
	private CruiseshipStockDetailService cruiseshipStockDetailService;
	@Autowired
	private CruiseshipCabinService cruiseshipCabinService;
	@Autowired
	private CruiseshipAnnexService cruiseshipAnnexService;
	@Autowired
	private CruiseshipStockGroupRelService cruiseshipStockGroupRelService;
	@Autowired
	private CruiseshipStockLogService cruiseshipStockLogService;
	
	private CruiseshipStock dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=cruiseshipStockService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(CruiseshipStockQuery cruiseshipStockQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		cruiseshipStockQuery.setDelFlag("0");
        Page<CruiseshipStock> page = cruiseshipStockService.find(new Page<CruiseshipStock>(request, response), cruiseshipStockQuery); 
        model.addAttribute("page", page);
        model.addAttribute("cruiseshipStockQuery", cruiseshipStockQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(CruiseshipStockInput cruiseshipStockInput, Model model) {
		model.addAttribute("cruiseshipStockInput", cruiseshipStockInput);
		return FORM_PAGE;
	}

	/**
	 * @Description 游轮库存查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author Bin
	 * @date 2016年2月3日
	 */
	@RequestMapping(value="cruiseshipStockList")
	public String cruiseshipStockList(HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String, String> parameters = getParameter(request);
		Page<Map<Object, Object>> page = cruiseshipStockService.findStockList(parameters, new Page<Map<Object, Object>>(request, response));

		/**
		 * 3月17日 更改
		 * page中不再查询status(关联状态)，而是根据"stock_uuid"再去"cruiseship_stock_group_rel"表查询status
		 * status若有一个为0(已关联)，则是已关联，status若全为1(未关联)，则是未关联
		 */
		List<Map<Object, Object>> pageList = page.getList();
		for(int i=0; i<pageList.size(); i++){
			String stockUuid = pageList.get(i).get("uuid").toString();
			int targetStatus = cruiseshipStockService.getStatusByStockUuid(stockUuid);
			pageList.get(i).put("status", targetStatus);
		}

		model.addAttribute("page", page);
		model.addAttribute("parameters", parameters);
		return STOCK_LIST_PAGE;
	}

	/**
	 * songyang
	 * 库存关联记录查询 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="cruiseshipStockGroupRelList")
	public void  cruiseshipStockGroupRelList(HttpServletRequest request,HttpServletResponse response,Model model){
		String stockUuid = request.getParameter("stockUuid");
		List<Map<String, Object>>  list = cruiseshipStockService.cruiseshipStockGroupRelList(stockUuid);
		String jsonList = JSONArray.toJSONString(list);
		ServletUtil.print(response,jsonList);
	}
	
	
	
	/**
	 * 游轮库存详情
	 * by songyang
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="cruiseshipStockDetail")
	public String cruiseshipStockDetail(String detailUuid,Model model){
		CruiseshipStock  stock = cruiseshipStockService.getByUuid(detailUuid);
		CruiseshipStockDetailQuery detailQuery = new CruiseshipStockDetailQuery();
		detailQuery.setCruiseshipStockUuid(detailUuid);
		detailQuery.setDelFlag("0");
		List<CruiseshipStockDetail> details = cruiseshipStockDetailService.find(detailQuery);
		if(CollectionUtils.isNotEmpty(details)){
			for(CruiseshipStockDetail detail : details){
				CruiseshipCabin	cruiseshipCabin = cruiseshipCabinService.getByUuid(detail.getCruiseshipCabinUuid());
				if(cruiseshipCabin!=null){
					detail.setCruiseshipCabinName(cruiseshipCabin.getName());
				}
			}
		}
		CruiseshipInfo info = null;
		try {
			info = cruiseshipInfoService.getByUuid(stock.getCruiseshipInfoUuid());
		} catch (BaseException4Quauq e) {
			e.printStackTrace();
		}
		int wholesalerId = UserUtils.getUser().getCompany().getId().intValue();
		//附件
		CruiseshipAnnexQuery cruiseshipAnnexQuery = new CruiseshipAnnexQuery();
		cruiseshipAnnexQuery.setMainUuid(detailUuid);
		List<CruiseshipAnnex> annexList = cruiseshipAnnexService.find(cruiseshipAnnexQuery);
		stock.setCruiseshipInfoName(info.getName());
		//修改记录
		CruiseshipStockLogQuery cruiseshipStockLogQuery = new CruiseshipStockLogQuery();
		cruiseshipStockLogQuery.setWholesalerId(wholesalerId);
		cruiseshipStockLogQuery.setDelFlag("0");
		cruiseshipStockLogQuery.setCruiseshipStockUuid(detailUuid);
		List<CruiseshipStockLog> stockLogs = cruiseshipStockLogService.find(cruiseshipStockLogQuery);
		//是否关联产品
		CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery = new CruiseshipStockGroupRelQuery();
		cruiseshipStockGroupRelQuery.setDelFlag("0");
		cruiseshipStockGroupRelQuery.setWholesalerId(wholesalerId);
		cruiseshipStockGroupRelQuery.setStatus(0);
		cruiseshipStockGroupRelQuery.setCruiseshipStockUuid(detailUuid);
		List<CruiseshipStockGroupRel> rels = cruiseshipStockGroupRelService.find(cruiseshipStockGroupRelQuery);
		boolean isOk = cruiseshipStockService.checkStockActivity(detailUuid);
		boolean relFlag = rels.size()>0?true:false;
		model.addAttribute("details", details);
		model.addAttribute("stock", stock);
		model.addAttribute("stockLogs", stockLogs);
		model.addAttribute("relFlag", relFlag);
		model.addAttribute("annexList", annexList);
		model.addAttribute("isOk",isOk);

		return  SHOW_PAGE_STOCK;
	}
	
	
	
	/**
	 * songyang
	 * 库存修改记录查询
	 */
	@RequestMapping(value="stockUpdateList")
	public void stockUpdateList(HttpServletRequest request,HttpServletResponse response){
		String uuid = request.getParameter("uuid"); //库存UUID
		String stockUuid=request.getParameter("stockUuid");
		String cruiseshipInfoUuid=request.getParameter("cruiseshipInfoUuid");
		String shipDate=request.getParameter("shipDate");
		List<Map<String, Object>>  list = cruiseshipStockService.stockUpdateList(uuid,stockUuid,cruiseshipInfoUuid,shipDate);
		String jsonList = JSONArray.toJSONString(list);
		ServletUtil.print(response,jsonList);
	}
	
	
	/**
	 * songyang
	 * 库存修改
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="cruiseshipStockGroupRelUpdate")
	public void  cruiseshipStockGroupRelUpdate(HttpServletRequest request,HttpServletResponse response,Model model){
		String stockUuid = request.getParameter("stockUuid"); //库存明细UUID
		//是否有关联产品记录 true 有 false 没有
		boolean isOk =  cruiseshipStockService.checkStockActivity(stockUuid);
		try {
			cruiseshipStockService.cruiseshipStockGroupRelUpdate(request, isOk);
		} catch (Exception e) {
			// TODO: handle exception
//			logger.error(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
			e.printStackTrace(LogUtil.getErrorStream(log));
//			result = "0";
		}
	}
	
	
	/**
	 * 库存删除
	 * by songyang
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="cruiseshipStockGroupRelDelete")
	public String  cruiseshipStockGroupRelDelete(HttpServletRequest request,HttpServletResponse response){
		String stockUuid = request.getParameter("stockUuid"); //库存
		String result = "";
		//是否有关联产品记录 true 有 false 没有
		boolean isOk =  cruiseshipStockService.checkStockActivity(stockUuid);
		if(isOk){
			result = "已关联产品，无法删除!";
		}else{
			try {
				cruiseshipStockService.delRelProduct(stockUuid);
				result = "删除成功!";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	
	
	/**
	 * 验证产品是否已经关联团期
	 * by songyang
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="cruiseshipStockGroupRelByActivityId")
	public String  cruiseshipStockGroupRelByActivityId(HttpServletRequest request,HttpServletResponse response){
		String activityId = request.getParameter("activityid"); //产品IT
		String stockUuid = request.getParameter("stockUuid");//stockUuid 
		//返回0未关联
		String result = "0";
		//是否有关联产品记录 true 有 false 没有
		boolean isOk =  cruiseshipStockService.checkStockActivityId(activityId,stockUuid);
		if(isOk){
			result = "1";
		}
		return result;
	}
	
	
	
	
	/**
	 * 库存批量删除
	 * by songyang
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="cruiseshipStockGroupRelBatchDelete")
	public String  cruiseshipStockGroupRelBatchDelete(HttpServletRequest request,HttpServletResponse response){
		String string = request.getParameter("uuids");
		String[] stockUuid = string.split(",");
		String result = "";
		for(String uuid : stockUuid){
			boolean isOk =  cruiseshipStockService.checkStockActivity(uuid);
			if(isOk){
				return "库存已关联，删除失败";
			}
		}
		try {
			cruiseshipStockService.batchDelete(stockUuid);
			result = "删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	

	
	/**
	 * 封装请求参数，并进行校验
     */
	private Map<String,String> getParameter(HttpServletRequest request) {
		Map<String, String> parameters = new HashMap<>();
		String curiseshipName = request.getParameter("curiseshipName"); //游轮名称
		String shipDateBegin = request.getParameter("shipDateBegin");	//查询起始的船期
		String shipDateEnd = request.getParameter("shipDateEnd");		//查询终止的船期

		parameters.put("curiseshipName", curiseshipName);
		parameters.put("shipDateBegin", shipDateBegin);
		parameters.put("shipDateEnd", shipDateEnd);
		return parameters;
	}

	/**
	 * 跳转到游轮库存添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cruiseshipStockform")
	public String cruiseshipStockform(Model model) {
		CruiseshipInfoQuery query = new CruiseshipInfoQuery();
		query.setDelFlag("0");
		query.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		List<CruiseshipInfo> cruiseshipList = cruiseshipInfoService.find(query);
		model.addAttribute("cruiseshipList", cruiseshipList);
		return ADD_PAGE;
	}
	
	/**
	 * 跳转到游轮库存修改页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("cruiseshipStockList:stock:edit")
	@RequestMapping(value = "cruiseshipStockedit")
	public String cruiseshipStockedit(@RequestParam String detailUuid ,Model model) {
		CruiseshipStock  stock = cruiseshipStockService.getByUuid(detailUuid);
		CruiseshipStockDetailQuery detailQuery = new CruiseshipStockDetailQuery();
		detailQuery.setCruiseshipStockUuid(detailUuid);
		detailQuery.setDelFlag("0");
		List<CruiseshipStockDetail> details = cruiseshipStockDetailService.find(detailQuery);
		if(CollectionUtils.isNotEmpty(details)){
			for(CruiseshipStockDetail detail : details){
				CruiseshipCabin	cruiseshipCabin = cruiseshipCabinService.getByUuid(detail.getCruiseshipCabinUuid());
				if(cruiseshipCabin!=null){
					detail.setCruiseshipCabinName(cruiseshipCabin.getName());
				}
			}
		}
		CruiseshipInfo info = null;
		try {
			info = cruiseshipInfoService.getByUuid(stock.getCruiseshipInfoUuid());
		} catch (BaseException4Quauq e) {
			e.printStackTrace();
		}
		int wholesalerId = UserUtils.getUser().getCompany().getId().intValue();
		//附件
		CruiseshipAnnexQuery cruiseshipAnnexQuery = new CruiseshipAnnexQuery();
		cruiseshipAnnexQuery.setMainUuid(detailUuid);
		cruiseshipAnnexQuery.setDelFlag("0");
		List<CruiseshipAnnex> annexList = cruiseshipAnnexService.find(cruiseshipAnnexQuery);
		stock.setCruiseshipInfoName(info.getName());
		//修改记录
		CruiseshipStockLogQuery cruiseshipStockLogQuery = new CruiseshipStockLogQuery();
		cruiseshipStockLogQuery.setWholesalerId(wholesalerId);
		cruiseshipStockLogQuery.setDelFlag("0");
		cruiseshipStockLogQuery.setCruiseshipStockUuid(detailUuid);
		List<CruiseshipStockLog> stockLogs = cruiseshipStockLogService.find(cruiseshipStockLogQuery);
		//是否关联产品
		CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery = new CruiseshipStockGroupRelQuery();
		cruiseshipStockGroupRelQuery.setDelFlag("0");
		cruiseshipStockGroupRelQuery.setWholesalerId(wholesalerId);
		cruiseshipStockGroupRelQuery.setStatus(0);
		cruiseshipStockGroupRelQuery.setCruiseshipStockUuid(detailUuid);
		List<CruiseshipStockGroupRel> rels = cruiseshipStockGroupRelService.find(cruiseshipStockGroupRelQuery);
		boolean isOk = cruiseshipStockService.checkStockActivity(detailUuid);
		boolean relFlag = rels.size()>0?true:false;
		model.addAttribute("details", details);
		model.addAttribute("stock", stock);
		model.addAttribute("stockLogs", stockLogs);
		model.addAttribute("relFlag", relFlag);
		model.addAttribute("annexList", annexList);
		model.addAttribute("isOk",isOk);
		return EDIT_PAGE;
	}
	
	/**
	 * 获取当前用户公司的游轮及船期信息
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCabinAndShipDate")
	public String getCabinAndShipDate( BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String cruiseshipUuid = input.getParamValue("cruiseshipInfoUuid");
		Map<String,Object> inifoMap = new TreeMap<String,Object>();//舱型和船期
		List<Map<String,Object>> cabinList = null;//舱型
		List<Object> shipDateList = null;//船期
		try{
			cabinList = cruiseshipStockService.getCabinList(cruiseshipUuid);
			shipDateList = cruiseshipStockService.getShipDate(cruiseshipUuid);
			if(CollectionUtils.isNotEmpty(shipDateList)){
				inifoMap.put("cruiseshipDate", shipDateList);
			}
			if(CollectionUtils.isNotEmpty(cabinList)){
				inifoMap.put("cruiseshipCabin", cabinList);
			}
			out.setResponseCode4Success();
			out.setData(inifoMap);
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		String json = JSON.toJSONStringWithDateFormat(out, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect);
		ThreadVariable.setMtourAjaxResponse(json);
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "saveCruiseshipStock")
	public String saveCruiseshipStock( BaseInput4MT input) {
		String jsontext = input.getParam();
		String result="1";
		try {
			cruiseshipStockService.saveCruiseshipStock(jsontext);
//			logger.info(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(log));
			result = "0";
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteStockDetailByUuid")
	public String deleteStockDetailByUuid( HttpServletRequest request) {
		String stockdetailuuid = request.getParameter("stockdetailuuid");
		String result="1";
		try {
			cruiseshipStockDetailService.removeByUuid(stockdetailuuid);
//			logger.info(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
		} catch (Exception e) {
			e.printStackTrace();
			result = "0";
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "editStockDetail")
	public String editStockDetail( HttpServletRequest request) {
		String memo = request.getParameter("memo");
		String stockUuid = request.getParameter("stockUuid");
		List<CruiseshipAnnex> annexList = cruiseshipAnnexService.getFileList(request);
		String result="1";
		try {
			cruiseshipStockService.editStockDetail(memo, stockUuid, annexList);
//			logger.info(LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING));
		} catch (Exception e) {
			e.printStackTrace();
			result = "0";
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CruiseshipStockInput cruiseshipStockInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			cruiseshipStockService.save(cruiseshipStockInput);
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
		model.addAttribute("cruiseshipStock", cruiseshipStockService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CruiseshipStock cruiseshipStock = cruiseshipStockService.getByUuid(uuid);
		CruiseshipStockInput cruiseshipStockInput = new CruiseshipStockInput(cruiseshipStock);
		model.addAttribute("cruiseshipStockInput", cruiseshipStockInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(CruiseshipStockInput cruiseshipStockInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, cruiseshipStockInput,true);
			cruiseshipStockService.update(dataObj);
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
				b = cruiseshipStockService.batchDelete(uuidArray);
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
	 * 展示游轮库存看板信息
	 * @Description: 
	 * @param @param request
	 * @param @param response
	 * @param @param model
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-23
	 */
	@RequestMapping(value = "showCruiseshipBoardInfo")
	public String showCruiseshipBoardInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return CREISESHIP_BOARD_PAGE;
	}
	
	/**
	 * 库存查询接口
	 * @Description: 
	 * @param @param request
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-23
	 */
	@ResponseBody
	@RequestMapping("queryCruiseshipInfos")
	public Object queryCruiseshipInfos(HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		
		try {
			Long companyId = UserUtils.getUser().getCompany().getId();
			List<CruiseshipInfo> cruiseshipInfos = cruiseshipInfoService.findByWholesalerId(companyId);
			datas.put("data", JSON.toJSONStringWithDateFormat(cruiseshipInfos, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect));
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
	}
	
	/**
	 * 根据游轮信息uuid获取该游轮下所有的船期
	 * @Description: 
	 * @param @param shipInfoUuid
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@ResponseBody
	@RequestMapping("getShipDatesByShipInfoUuid")
	public Object getShipDatesByShipInfoUuid(HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String cruiseshipInfoUuid = request.getParameter("cruiseshipInfoUuid");
		try {
			if(StringUtils.isEmpty(cruiseshipInfoUuid)) {
				datas.put("result", "3");
				datas.put("message", "请选择游轮信息！");
				return datas;
			} else {
				List<CruiseshipStock> shipDates = cruiseshipStockService.getStocksByShipInfoUuid(cruiseshipInfoUuid);
				datas.put("data", JSON.toJSONStringWithDateFormat(shipDates, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect));
			}
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
	}
	
	
	/**
	 * 库存关联产品查询接口
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @param queryStatus
	 * @param @param request
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@ResponseBody
	@RequestMapping("queryRelProducts")
	public Object queryRelProducts(String cruiseshipStockUuid, String queryStatus, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		
		try {
			if(StringUtils.isEmpty(cruiseshipStockUuid) || StringUtils.isEmpty(queryStatus)) {
				datas.put("result", "3");
				datas.put("message", "请选择筛选信息！");
				return datas;
			} else {
				List<CruiseshipStockGroupRel> stockGroupRels = cruiseshipStockService.queryRelProducts(cruiseshipStockUuid, queryStatus);
				datas.put("data", JSON.toJSONStringWithDateFormat(stockGroupRels, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect));
			}
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
	}
	
	/**
	 * 库存关联产品删除
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @param queryStatus
	 * @param @param request
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-16
	 */
	@ResponseBody
	@RequestMapping("delRelProduct")
	public Object delRelProduct(String relUuid, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		
		try {
			if(StringUtils.isEmpty(relUuid)) {
				datas.put("result", "3");
				datas.put("message", "传入参数异常，请稍后重试！");
				return datas;
			} else {
				cruiseshipStockService.delRelProduct(relUuid);
				datas.put("result", "1");
				datas.put("message", "删除成功");
			}
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
	}
	
	/**
	 * 通过库存uuid获取创建用户信息
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @param request
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@ResponseBody
	@RequestMapping("queryCreateUsersByStockUuid")
	public Object queryCreateUsersByStockUuid(String cruiseshipStockUuid, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		
		try {
			if(StringUtils.isEmpty(cruiseshipStockUuid)) {
				datas.put("result", "3");
				datas.put("message", "请选择库存信息！");
				return datas;
			} else {
				List<Map<String,Object>> stockOrders = cruiseshipStockService.queryCreateUsersByStockUuid(cruiseshipStockUuid);
				datas.put("data", JSON.toJSONStringWithDateFormat(stockOrders, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect));
			}
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
	}
	
	
	/**
	 * 库存订单库存明细查询接口
	 * @Description: 
	 * @param @param queryStockOrderInfo
	 * @param @param request
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@ResponseBody
	@RequestMapping("queryStockOrderInfos")
	public Object queryStockOrderInfos(BaseInput4MT input4mt, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		String string="";
		try {
			if(StringUtils.isEmpty(input4mt.getParam())) {
				datas.put("result", "3");
				datas.put("message", "请选择库存信息！");
				return datas;
			} else {
				Map<String, List<Map<String, List<Map<String,Object>>>>> stockOrderInfos = cruiseshipStockService.queryStockOrderInfos(input4mt.getParam());
				string= JSON.toJSONStringWithDateFormat(stockOrderInfos, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect);
			}
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * 根据游轮信息uuid和船期获取切位信息集合
	 * @Description: 
	 * @param @param shipInfoUuid
	 * @param @param skipDate
	 * @param @param request
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@ResponseBody
	@RequestMapping("getStockInfos")
	public Object getStockInfos(String shipInfoUuid, @DateTimeFormat(pattern = "yyyy-MM-dd") Date skipDate, HttpServletRequest request) {
		Map<String, String> datas = new HashMap<String, String>();
		try {
			if(StringUtils.isEmpty(shipInfoUuid)) {
				datas.put("result", "3");
				datas.put("message", "请选择游轮信息！");
				return datas;
			} else {
				List<CruiseshipStockDetail> cruiseshipStocks = cruiseshipStockService.getStockDetailInfos(shipInfoUuid, skipDate);
				datas.put("data", JSON.toJSONStringWithDateFormat(cruiseshipStocks, DateUtils.DATE_PATTERN_YYYY_MM_DD, SerializerFeature.DisableCircularReferenceDetect));
			}
		} catch (Exception e) {
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请稍后重试！");
			e.printStackTrace();
		}
		return datas;
	}
	
}
