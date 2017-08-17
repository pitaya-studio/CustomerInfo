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
import org.springframework.http.ResponseEntity;
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
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.service.StockService;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.temp.stock.entity.ActivitygroupreserveTemp;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.ActivitygroupreserveTempInput;
import com.trekiz.admin.modules.temp.stock.service.ActivitygroupreserveTempService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/activitygroupreserveTemp")
public class ActivitygroupreserveTempController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/temp/stock/activitygroupreservetemp/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/activitygroupreserveTemp/list";
	protected static final String FORM_PAGE = "modules/temp/stock/activitygroupreservetemp/form";
	protected static final String SHOW_PAGE = "modules/temp/stock/activitygroupreservetemp/show";
	protected static final String PAY_ORDER = "modules/temp/stock/activitygroupreservetemp/payOrder";
	@Autowired
	private ActivitygroupreserveTempService activitygroupreserveTempService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private DocInfoService docInfoService;
	private ActivitygroupreserveTemp dataObj;
	@Autowired
	private StockService stockService;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=activitygroupreserveTempService.getByUuid(uuid);
		}
	}
	/**
	 * 批量切位草稿箱查询接口
	 * @author zhangchao
	 * @param acitivityName
	 * @param groupCode
	 * @param agentName
	 * @param groupOpenDatefront 出团时间起
	 * @param groupOpenDateAfter 出团时间止
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author chao.zhang
	 */
	@RequiresPermissions("stock:loose:batchreserve")
	@RequestMapping(value = "list")
	public String list(String acitivityName,String groupCode,String agentName,Date groupOpenDatefront,Date groupOpenDateAfter, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Map<Object, Object>> page = activitygroupreserveTempService.findByPage(new Page<Map<Object,Object>>(request,response), acitivityName, groupCode, agentName, groupOpenDatefront, groupOpenDateAfter);
        List<Agentinfo> agentinfoList = agentinfoService.findStockAgentinfo();
        List<Dict> list = DictUtils.getDictList(Context.ORDER_PAYTYPE);
        model.addAttribute("list",list);
        model.addAttribute("acitivityName",acitivityName);
        model.addAttribute("groupCode",groupCode);
        model.addAttribute("agentName", agentName);
        model.addAttribute("groupOpenDatefront", groupOpenDatefront);
        model.addAttribute("groupOpenDateAfter", groupOpenDateAfter);
        model.addAttribute("page", page);
        model.addAttribute("agentinfoList",agentinfoList);
        model.addAttribute("showType",request.getParameter("showType"));
        model.addAttribute("user",UserUtils.getUser());
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(ActivitygroupreserveTempInput activitygroupreserveTempInput, Model model) {
		model.addAttribute("activitygroupreserveTempInput", activitygroupreserveTempInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(ActivitygroupreserveTempInput activitygroupreserveTempInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			activitygroupreserveTempService.save(activitygroupreserveTempInput);
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
		model.addAttribute("activitygroupreserveTemp", activitygroupreserveTempService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		ActivitygroupreserveTemp activitygroupreserveTemp = activitygroupreserveTempService.getByUuid(uuid);
		ActivitygroupreserveTempInput activitygroupreserveTempInput = new ActivitygroupreserveTempInput(activitygroupreserveTemp);
		model.addAttribute("activitygroupreserveTempInput", activitygroupreserveTempInput);
		return FORM_PAGE;
	}
	/**
	 * 修改接口
	 * @param model
	 * @param resp
	 * @param request
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public void update(Model model, HttpServletResponse resp,HttpServletRequest request) {
		String string = request.getParameter("updateData");
		List<ActivitygroupreserveTemp> list = JSON.parseArray(string, ActivitygroupreserveTemp.class);
		String result="2";
		try {
		for(ActivitygroupreserveTemp activitygroupreserveTemp:list){	
				//BeanUtil.copySimpleProperties(dataObj, activitygroupreserveTemp2,true);
				ActivitygroupreserveTemp temp = activitygroupreserveTempService.getById(activitygroupreserveTemp.getId());
				
				temp.setFrontMoney(activitygroupreserveTemp.getFrontMoney());
				temp.setReservation(activitygroupreserveTemp.getReservation());
				temp.setPayReservePosition(activitygroupreserveTemp.getPayReservePosition());
				temp.setPayType(activitygroupreserveTemp.getPayType());
				temp.setRemark(activitygroupreserveTemp.getRemark());
				activitygroupreserveTempService.update(temp);
			}
		} catch (Exception e) {
			result="0";
			e.printStackTrace();
		}
		try {
			resp.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				List<ActivitygroupreserveTemp> temps = JSON.parseArray(delBatchData,ActivitygroupreserveTemp.class);
				StringBuffer sbf=new StringBuffer();
				for(ActivitygroupreserveTemp temp:temps){
					sbf.append(temp.getUuid()+",");
				}
				String[] uuids = sbf.toString().split(",");
				b = activitygroupreserveTempService.batchDelete(uuids);
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
				data = activitygroupreserveTempService.batchSave2Draftbox(reserveJsonData, uploadJsonData, request);
			} catch(Exception e) {
//				System.out.println(e);
				data.put("result", "failed");
				data.put("message", "存入草稿箱失败!");
			}
		}

		return data;
	}
	/**
	 * 批量切位草稿箱下载查询接口
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping("/query")
	public String query(String uuid,Model model,HttpServletResponse resp){
		List<ActivityreservefileTemp> list = activitygroupreserveTempService.down(uuid);
		model.addAttribute("docList",list);
		String result = JSON.toJSONString(list);
		return result;
	}
	/**
	 * 批量草稿箱下载接口
	 * @author chao.zhang
	 */
		@RequestMapping(value="downLoad")
	  public ResponseEntity<byte[]> download(Long docId,HttpServletResponse response){
		  File downFile;
		  OutputStream os = null;
		  try {
			  DocInfo docInfo = docInfoService.getDocInfo(docId);
	    		if(docInfo!=null){
	    			downFile = new File(Global.getBasePath() +File.separator+docInfo.getDocPath());
//	    			System.out.println(Global.getBasePath() +File.separator+docInfo.getDocPath());
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
		  return null;
	  }
		
	/**
	 * 草稿箱列表确认切位接口操作
	 * @Description: 
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午7:57:27
	 */
	@ResponseBody
    @RequestMapping(value = "confirmReverse")
	public String confirmReverse(Model model,HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> data = new HashMap<String, String>();
		String reserveTempJson = request.getParameter("reserveTempJson");
		if(StringUtils.isEmpty(reserveTempJson)) {
			data.put("result", "3");
			data.put("message", "请选择团期信息！");
			return JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd");
		}
		
		String[] reserveTempUuidArr = reserveTempJson.split(",");
		List<String> reserveTempUuids = Arrays.asList(reserveTempUuidArr);
		
		//散拼切位临时表集合
		List<ActivitygroupreserveTemp> temps = activitygroupreserveTempService.getByUuids(reserveTempUuids);
		
		//散拼切位表集合
		List<ActivityGroupReserve> groupReserves = new ArrayList<ActivityGroupReserve>();
		
		if(CollectionUtils.isNotEmpty(temps)) {
			for(ActivitygroupreserveTemp temp : temps) {
				ActivityGroupReserve groupReserve = new ActivityGroupReserve();
				groupReserve.setActivityGroupId(temp.getActivityGroupId().longValue());
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
				groupReserve.setSrcActivityId(temp.getSrcActivityId().longValue());
				groupReserve.setReserveTempUuid(temp.getUuid());
				groupReserves.add(groupReserve);
				temp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
		}
		
		try{
			data = stockService.batchReceive(groupReserves, null, request, "tempType");
			
			activitygroupreserveTempService.batchUpdate(temps);
			
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
	/**
	 * 将新上传的凭证保存到activityreservefile_temp中
	 * @param response
	 * @param uuid
	 * @param docIds
	 */
	@RequestMapping(value="saveFile")
	public void saveFile(String uuid,String docIds,HttpServletResponse response){
		String[] split = docIds.split(";");
		ActivitygroupreserveTemp temp = activitygroupreserveTempService.getByUuid(uuid);
		List<ActivityreservefileTemp> list=new ArrayList<ActivityreservefileTemp>();
		for(String docId:split){
			DocInfo docInfo = docInfoService.getDocInfo(Long.parseLong(docId));
			ActivityreservefileTemp activityreservefileTemp=new ActivityreservefileTemp();
			activityreservefileTemp.setSrcDocId(docInfo.getId().intValue());
			activityreservefileTemp.setFileName(docInfo.getDocName());
			activityreservefileTemp.setActivityGroupId(temp.getActivityGroupId());
			activityreservefileTemp.setAgentId(temp.getAgentId());
			activityreservefileTemp.setSrcActivityId(temp.getSrcActivityId());
			activityreservefileTemp.setReserveTempUuid(uuid);
			list.add(activityreservefileTemp);
		}
		activitygroupreserveTempService.saveFileTemp(list);
		try {
			response.getWriter().print(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value="deleteFile")
	public void deleteFile(Long docId,String uuid){
		activitygroupreserveTempService.delFile(docId,uuid);
	}
}
