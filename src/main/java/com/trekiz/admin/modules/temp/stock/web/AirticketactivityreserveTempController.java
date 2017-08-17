/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.service.AirticketStockService;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.temp.stock.entity.AirticketactivityreserveTemp;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.AirticketactivityreserveTempInput;
import com.trekiz.admin.modules.temp.stock.service.AirticketactivityreserveTempService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/airticketactivityreserveTemp")
public class AirticketactivityreserveTempController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/temp/stock/airticketactivityreservetemp/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/airticketactivityreserveTemp/list";
	protected static final String FORM_PAGE = "modules/temp/stock/airticketactivityreservetemp/form";
	protected static final String SHOW_PAGE = "modules/temp/stock/airticketactivityreservetemp/show";
	
	@Autowired
	private AirticketactivityreserveTempService airticketactivityreserveTempService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private DocInfoService docInfoService;
	
	@Autowired
	private AirticketStockService airticketStockService;
	private AirticketactivityreserveTemp dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=airticketactivityreserveTempService.getByUuid(uuid);
		}
	}
	/**
	 * 切位草稿箱查询接口
	 * @param productCode
	 * @param groupCode
	 * @param agentName
	 * @param startingDateFront
	 * @param startingDateAfter
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author chao.zhang
	 */
    @RequiresPermissions("stock:airticket:batchreserve")
	@RequestMapping(value = "list")
	public String list(String productCode,String groupCode,String agentName,Date startingDateFront,Date startingDateAfter, HttpServletRequest request, HttpServletResponse response, Model model) {
		
       // Page<AirticketactivityreserveTemp> page = airticketactivityreserveTempService.find(new Page<AirticketactivityreserveTemp>(request, response), airticketactivityreserveTempQuery); 
      //  model.addAttribute("page", page);
     //   model.addAttribute("airticketactivityreserveTempQuery", airticketactivityreserveTempQuery);
		Page<Map<Object,Object>> page = airticketactivityreserveTempService.findByPage(new Page<Map<Object,Object>>(request,response), productCode, groupCode, agentName, startingDateFront, startingDateAfter);
		List<Agentinfo> agentinfoList = agentinfoService.findStockAgentinfo();;
		List<Dict> list = DictUtils.getDictList(Context.ORDER_PAYTYPE);
		model.addAttribute("list",list);
		model.addAttribute("productCode",productCode);
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("agentName", agentName);
		model.addAttribute("startingDateFront", startingDateFront);
		model.addAttribute("startingDateAfter",startingDateAfter);
        model.addAttribute("page", page);
        model.addAttribute("agentinfoList",agentinfoList);
        //
        model.addAttribute("showType",request.getParameter("showType"));
        model.addAttribute("user",UserUtils.getUser());
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(AirticketactivityreserveTempInput airticketactivityreserveTempInput, Model model) {
		model.addAttribute("airticketactivityreserveTempInput", airticketactivityreserveTempInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(AirticketactivityreserveTempInput airticketactivityreserveTempInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			airticketactivityreserveTempService.save(airticketactivityreserveTempInput);
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
		model.addAttribute("airticketactivityreserveTemp", airticketactivityreserveTempService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		AirticketactivityreserveTemp airticketactivityreserveTemp = airticketactivityreserveTempService.getByUuid(uuid);
		AirticketactivityreserveTempInput airticketactivityreserveTempInput = new AirticketactivityreserveTempInput(airticketactivityreserveTemp);
		model.addAttribute("airticketactivityreserveTempInput", airticketactivityreserveTempInput);
		return FORM_PAGE;
	}
	/**
	 * 切位草稿箱修改接口
	 * @param model
	 * @param resp
	 * @param request
	 * @return
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(Model model, HttpServletResponse resp,HttpServletRequest request) {
		String string = request.getParameter("updateData");
		List<AirticketactivityreserveTemp> list = JSON.parseArray(string,AirticketactivityreserveTemp.class);
		String result="2";
		try {
			for(AirticketactivityreserveTemp airticketactivityreserveTemp:list){
				AirticketactivityreserveTemp temp = airticketactivityreserveTempService.getById(airticketactivityreserveTemp.getId());
				temp.setFrontMoney(airticketactivityreserveTemp.getFrontMoney());
				temp.setReservation(airticketactivityreserveTemp.getReservation());
				temp.setPayReservePosition(airticketactivityreserveTemp.getPayReservePosition());
				temp.setPayType(airticketactivityreserveTemp.getPayType());
				temp.setRemark(airticketactivityreserveTemp.getRemark());
				airticketactivityreserveTempService.update(temp);	
			}
//			BeanUtil.copySimpleProperties(dataObj, airticketactivityreserveTempInput,true);
//			airticketactivityreserveTempService.update(dataObj);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	/**
	 * 批删接口
	 * @param delBatchData
	 * @return
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public void delete(String delBatchData,HttpServletResponse response) {
		//Map<String,Object> datas = new HashMap<String, Object>();
		String str="";
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(delBatchData)){
				List<AirticketactivityreserveTemp> temps = JSON.parseArray(delBatchData,AirticketactivityreserveTemp.class);
				StringBuffer sbf=new StringBuffer();
				for(AirticketactivityreserveTemp temp:temps){
					sbf.append(temp.getUuid()+",");
				}
				String[] uuids = sbf.toString().split(",");
				b = airticketactivityreserveTempService.batchDelete(uuids);
			}else{
				b=false;
				//datas.put("message", "fail");
				str="0";
			}
		} catch (Exception e) {
			b = false;
			//datas.put("message", "系统发生异常，请重新操作!");
			str="1";
		}
		if(b){
//			datas.put("result", "1");
//			datas.put("message", "success");
			str="2";
		}else{
//			datas.put("result", "0");
			str="3";
		}
		try {
			response.getWriter().print(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切位草稿箱下载查询接口
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping("/query")
	public String query(String uuid,Model model,HttpServletResponse resp){
		List<AirticketreservefileTemp> list = airticketactivityreserveTempService.down(uuid);
		model.addAttribute("docList",list);
		String result = JSON.toJSONString(list);
		return result;
	}
	/**
	 *切位草稿箱 下载接口
	 * @author chao.zhang
	 */
		@RequestMapping(value="/downLoad")
	  public void download(Long docId,HttpServletResponse response){
		  File downFile;
		  OutputStream os = null;
		  try {
			  DocInfo docInfo = docInfoService.getDocInfo(docId);
	    		if(docInfo!=null){
	    			downFile = new File(Global.getBasePath() +File.separator+docInfo.getDocPath());
	    			if(downFile.exists()){
	    				response.reset();
	    				response.setHeader("Content-Disposition", "attachment; filename="+new String(docInfo.getDocName().getBytes("gb2312"), "ISO-8859-1"));
	    				response.setContentType("application/octet-stream; charset=utf-8");
	    		    	os = response.getOutputStream();
	    				os.write(FileUtils.readFileToByteArray(downFile));
	    	            os.flush();
	    			}       		
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(os!=null)
					try {
						os.close();
					} catch (Exception e) {
					}
			}
	  }


	/**
	 * 批量存入草稿箱
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月19日12:01:59
	 */
	@ResponseBody
	@RequestMapping(value = "batchSave2Draftbox")
	public Map<String, Object> batchSave2Draftbox(Model model,HttpServletRequest request,HttpServletResponse response) {
		String reserveJsonData = request.getParameter("reserveJsonData");
		String uploadJsonData = request.getParameter("uploadJsonData");
		Map<String, Object> data = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(reserveJsonData)) {
			try {
				data = airticketactivityreserveTempService.batchSave2Draftbox(reserveJsonData, uploadJsonData, request);
			} catch(Exception e) {
//				System.out.println(e);
				data.put("result", "failed");
				data.put("message", "存入草稿箱失败!");
			}
		}

		return data;
	}

	/**
	 * 修改file
	 * @param docId
	 * @param uuid
	 */
	@RequestMapping(value="deleteFile")
	public void deleteFile(Long docId,String uuid){
		airticketactivityreserveTempService.delFile(docId,uuid);
	}
	/**
	 * 将新上传的凭证保存到airticketreservefile_temp中
	 * @param response
	 * @param uuid
	 * @param docIds
	 */
	@RequestMapping(value="saveFile")
	public void saveFile(String uuid,String docIds,HttpServletResponse response){
		String[] split = docIds.split(";");
		AirticketactivityreserveTemp temp = airticketactivityreserveTempService.getByUuid(uuid);
		List<AirticketreservefileTemp> list=new ArrayList<AirticketreservefileTemp>();
		for(String docId:split){
			DocInfo docInfo = docInfoService.getDocInfo(Long.parseLong(docId));
			AirticketreservefileTemp airticketreservefileTemp=new AirticketreservefileTemp();
			airticketreservefileTemp.setSrcDocId(docInfo.getId().intValue());
			airticketreservefileTemp.setFileName(docInfo.getDocName());
			airticketreservefileTemp.setAirticketActivityId(temp.getActivityId());
			airticketreservefileTemp.setAgentId(temp.getAgentId());
			airticketreservefileTemp.setReserveTempUuid(uuid);
			list.add(airticketreservefileTemp);
		}
		airticketactivityreserveTempService.saveFileTemp(list);
		try {
			response.getWriter().print(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	
	/**
	 * 草稿箱列表确认切位接口操作
	 * @Description: 
	 * @param @param model
	 * @param @param request
	 * @param @param response
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-24 下午7:50:06
	 */
	@ResponseBody
    @RequestMapping(value = "confirmReverse")
	public String confirmReverse(Model model,HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		String reserveTempJson = request.getParameter("reserveTempJson");
		if(StringUtils.isEmpty(reserveTempJson)) {
			data.put("result", "3");
			data.put("message", "请选择机票产品信息！");
			return JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd");
		}
		
		String[] reserveTempUuidArr = reserveTempJson.split(",");
		List<String> reserveTempUuids = Arrays.asList(reserveTempUuidArr);
		
		//机票草稿箱集合
		List<AirticketactivityreserveTemp> temps = airticketactivityreserveTempService.getByUuids(reserveTempUuids);
		
		//机票切位表信息集合
		List<AirticketActivityReserve> groupReserves = new ArrayList<AirticketActivityReserve>();
		
		if(CollectionUtils.isNotEmpty(temps)) {
			for(AirticketactivityreserveTemp temp : temps) {
				AirticketActivityReserve groupReserve = new AirticketActivityReserve();
				groupReserve.setActivityId(temp.getActivityId().longValue());
				groupReserve.setAgentId(temp.getAgentId().longValue());
				groupReserve.setFrontMoney(new BigDecimal(temp.getFrontMoney()));
				groupReserve.setLeftFontMoney(new BigDecimal(temp.getLeftFontMoney()));
				groupReserve.setLeftpayReservePosition(temp.getLeftpayReservePosition());
				if(temp.getPayReservePosition() != null) {
					groupReserve.setPayReservePosition(temp.getPayReservePosition());
				} else {
					groupReserve.setPayReservePosition(0);
				}
				groupReserve.setPayType(temp.getPayType());
				groupReserve.setRemark(temp.getRemark());
				groupReserve.setReservation(temp.getReservation());
				groupReserve.setReserveType(temp.getReserveType());
				groupReserve.setReturnRemark(temp.getReturnRemark());
				groupReserve.setSoldPayPosition(temp.getSoldPayPosition());
				groupReserve.setReserveTempUuid(temp.getUuid());
				groupReserves.add(groupReserve);
				temp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
		}
		
		try{
			data = airticketStockService.batchReceive(groupReserves, null, "tempType");

			airticketactivityreserveTempService.batchUpdate(temps);
			
		} catch(Exception e) {
			
			if((e.getMessage().endsWith("此团期没有足够的余位")) || (e.getMessage().endsWith("有产品团期不存在"))) {
				data.put("result", "2");
				data.put("message", e.getMessage());
			} else {
				data.put("result", "3");
				data.put("message", "系统出现异常，请重试！");
			}
			
			e.printStackTrace();
		}
		
		return JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd");
	}

}
