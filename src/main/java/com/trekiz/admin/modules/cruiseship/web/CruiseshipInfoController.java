/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.web;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.util.LogUtil;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipCabinInput;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipInfoInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipInfoQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipAnnexService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipCabinService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipInfoService;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipInfo")
public class CruiseshipInfoController extends BaseController {
	private final static Log logger = LogFactory.getLog(BaseController.class);
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipinfo/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipInfo/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipinfo/form";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipinfo/show";
	protected static final String UPDATE_PAGE = "modules/cruiseship/cruiseshipinfo/update";
	
	@Autowired
	private CruiseshipInfoService cruiseshipInfoService;
	
	@Autowired
	private CruiseshipCabinService cruiseshipCabinService;
	@Autowired	
	private DocInfoService docInfoService;
	@Autowired
	private CruiseshipAnnexService cruiseshipAnnexService;
	private CruiseshipInfo dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		try {
			String uuid = request.getParameter("uuid");
			if(StringUtils.isNotBlank(uuid)){
				dataObj=cruiseshipInfoService.getByUuid(uuid);
			}
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
	}
	/**
	 * 列表
	 * @param cruiseshipInfoQuery 
	 * @param request
	 * @param response
	 * @param model
	 * @return 
	 * @author zhangchao
	 * 2016-2-2
	 */
	@RequiresPermissions("sys:dict:ship")
	@RequestMapping(value = "list")
	public String list(CruiseshipInfoQuery cruiseshipInfoQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			cruiseshipInfoQuery.setDelFlag("0");
			cruiseshipInfoQuery.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
			Page<CruiseshipInfo> page = cruiseshipInfoService.find(new Page<CruiseshipInfo>(request, response), cruiseshipInfoQuery,cruiseshipInfoQuery.getUuid()); 
			model.addAttribute("name",cruiseshipInfoQuery.getName());
			model.addAttribute("page", page);
			model.addAttribute("cruiseshipInfoQuery", cruiseshipInfoQuery);
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
        return LIST_PAGE;
	}
	/**
	 * 新增按钮跳转页面
	 * @param cruiseshipInfoInput
	 * @param model
	 * @return
	 * @author zhangchao 
	 * 2016-2-2
	 */
	@RequiresPermissions("sys:dict:ship")
	@RequestMapping(value = "form")
	public String form(CruiseshipInfoInput cruiseshipInfoInput, Model model) {
		model.addAttribute("cruiseshipInfoInput", cruiseshipInfoInput);
		return FORM_PAGE;
	}
	/**
	 * 保存接口
	 * @param cruiseshipInfoInput
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @author zhangchao
	 * 2016-2-2
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(String acitivityName,String cabinTypeNames,String memo ,String docIds,Model model, RedirectAttributes redirectAttributes) {
		String result="1";
		try {
			//保存游轮基本信息
			/*CruiseshipInfoInput cruiseshipInfoInput=new CruiseshipInfoInput();
			System.out.println(input.getParamValue("memo"));
			cruiseshipInfoInput.setMemo(input.getParamValue("memo"));
			System.out.println(input.getParamValue("acitivityName"));
			cruiseshipInfoInput.setWholesalerId(Integer.parseInt(UserUtils.getUser().getCompany().getId()+""));
			cruiseshipInfoInput.setMemo(input.getParamValue("acitivityName"));
			cruiseshipInfoService.save(cruiseshipInfoInput);
			//保存上传附件信息
			System.out.println(cruiseshipInfoInput.getUuid());
			saveFile(cruiseshipInfoInput.getUuid(),input.getParamValue("docIds"));
			//保存舱型信息
			String[] cabinTypeNames=input.getParamValue("cabinTypeName").split(",");*/
			CruiseshipInfoInput cruiseshipInfoInput=new CruiseshipInfoInput();
			cruiseshipInfoInput.setMemo(memo);
			cruiseshipInfoInput.setWholesalerId(Integer.parseInt(UserUtils.getUser().getCompany().getId()+""));
			cruiseshipInfoInput.setName(acitivityName);
			CruiseshipInfo cruiseshipInfo = cruiseshipInfoService.save(cruiseshipInfoInput);
			//保存上传附件信息
			if(!docIds.isEmpty()){
				saveFile(cruiseshipInfo.getUuid(),docIds);
			}
			//保存舱型信息
			String[] split = cabinTypeNames.split(",");
			for(String name:split){
				CruiseshipCabin cruiseshipCabin=new CruiseshipCabin();
				cruiseshipCabin.setName(name);
				cruiseshipCabin.setCruiseshipInfoUuid(cruiseshipInfo.getUuid());
				cruiseshipCabin.setWholesalerId(Integer.parseInt(UserUtils.getUser().getCompany().getId()+""));
				CruiseshipCabinInput cruiseshipCabinInput=new CruiseshipCabinInput(cruiseshipCabin);
				cruiseshipCabinService.save(cruiseshipCabinInput);
			}
		} catch (BaseException4Quauq e) {
			result="0";
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return result;
		
	}
	/**
	 * 修改查询接口
	 * @param uuid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author zhangchao
	 * 2016-2-2
	 */
	@RequiresPermissions("sys:dict:ship")
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			if(StringUtils.isEmpty(uuid)) {
				return RE_LIST_PAGE;
			}
			model.addAttribute("cruiseshipInfo", cruiseshipInfoService.getByUuid(uuid));
			List<CruiseshipCabin> cruiseshipCabins = cruiseshipInfoService.getCruiseshipCabinBycruiseshipInfoUuid(uuid);
			model.addAttribute("cruiseshipCabins",cruiseshipCabins);
			List<CruiseshipAnnex> list = getDoc(uuid);
			model.addAttribute("list",list);
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return UPDATE_PAGE;
	}
	/**
	 * 详情查询接口
	 * @param uuid
	 * @param model
	 * @return
	 * @author zhangchao
	 * 2016-2-2
	 */
	@RequiresPermissions("sys:dict:ship")
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		try {
			if(StringUtils.isEmpty(uuid)) {
				return RE_LIST_PAGE;
			}
			CruiseshipInfo cruiseshipInfo = cruiseshipInfoService.getByUuid(uuid);
			CruiseshipInfoInput cruiseshipInfoInput = new CruiseshipInfoInput(cruiseshipInfo);
			List<CruiseshipCabin> cruiseshipCabins = cruiseshipInfoService.getCruiseshipCabinBycruiseshipInfoUuid(uuid);
			List<CruiseshipAnnex> list = getDoc(uuid);
			model.addAttribute("cabinCruiseshipCabins",cruiseshipCabins);
			model.addAttribute("cruiseshipInfoInput", cruiseshipInfoInput);
			model.addAttribute("list",list);
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return SHOW_PAGE;
	}
	/**
	 * 修改接口
	 * @param cruiseshipInfoInput
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @author zhangchao
	 * 2016-2-2
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(Integer id,String acitivityName,String cabinTypeNames,String memo ,String docIds,String deluuids,String deldocId, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			CruiseshipInfo cruiseshipInfo = cruiseshipInfoService.getById(id);
			cruiseshipInfo.setMemo(memo);
			cruiseshipInfo.setName(acitivityName);
			cruiseshipInfo.setUpdateDate(new Date());
			cruiseshipInfoService.update(cruiseshipInfo);
//			List<CruiseshipCabin> cabin = cruiseshipInfoService.getCruiseshipCabinBycruiseshipInfoUuid(cruiseshipInfo.getUuid());
			String[] split = cabinTypeNames.split(";");
			for(String str:split){
				String[] strings = str.split(",");
				if(!strings[0].isEmpty()){
					CruiseshipCabin cruiseshipCabin = cruiseshipCabinService.getByUuid(strings[0]);
					cruiseshipCabin.setName(strings[1]);
					cruiseshipCabin.setUpdateDate(new Date());
					cruiseshipCabinService.update(cruiseshipCabin);
				}else{
					CruiseshipCabin cruiseshipCabin1=new CruiseshipCabin();
					cruiseshipCabin1.setName(strings[1]);
					cruiseshipCabin1.setCruiseshipInfoUuid(cruiseshipInfo.getUuid());
					cruiseshipCabin1.setWholesalerId(Integer.parseInt(UserUtils.getUser().getCompany().getId()+""));
					CruiseshipCabinInput cruiseshipCabinInput=new CruiseshipCabinInput(cruiseshipCabin1);
					cruiseshipCabinService.save(cruiseshipCabinInput);
				}
			}
			if(StringUtils.isNotEmpty(deluuids)){
				String[] deluuid = deluuids.split(",");
				for(String uuid:deluuid){
					CruiseshipCabin cruiseshipCabin = cruiseshipCabinService.getByUuid(uuid);
					cruiseshipCabin.setDelFlag("1");
					cruiseshipCabinService.update(cruiseshipCabin);
				}
			}
			
		/*		cruiseshipCabinService.removeById(cruiseshipCabin.getId());*/
			/*for(String name:split){
				CruiseshipCabin cruiseshipCabin=new CruiseshipCabin();
				cruiseshipCabin.setName(name);
				cruiseshipCabin.setCruiseshipInfoUuid(cruiseshipInfo.getUuid());
				cruiseshipCabin.setWholesalerId(Integer.parseInt(UserUtils.getUser().getCompany().getId()+""));
				CruiseshipCabinInput cruiseshipCabinInput=new CruiseshipCabinInput(cruiseshipCabin);
				cruiseshipCabinService.save(cruiseshipCabinInput);
			}*/
			if(!docIds.isEmpty()){
				saveFile(cruiseshipInfo.getUuid(),docIds);
			}
			if(!deldocId.isEmpty()){
				String[] uuids = deldocId.split(",");
				for(String uuid:uuids){
					CruiseshipAnnex cruiseshipAnnex = cruiseshipAnnexService.getByUuid(uuid);
					cruiseshipAnnex.setDelFlag("1");
					cruiseshipAnnexService.update(cruiseshipAnnex);
				}
				
			}
			
		} catch (BaseException4Quauq e) {
			result="0";
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return result;
	}
	/**
	 * 批量删除接口
	 * @param uuids
	 * @return
	 * @author zhangchao
	 * 2016-2-2
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		boolean b = true;
		Map<String,Object> datas = new HashMap<String, Object>();
		try {	
			boolean check = deleteCheck(uuids);
			if(check){
				return "2";
			}
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				b = cruiseshipInfoService.batchDelete(uuidArray);
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (BaseException4Quauq e) {
			b = false;
			e.printStackTrace(LogUtil.getErrorStream(logger));
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
	 * 点击修改时的修改验证接口
	 * @author zhangchao
	 * @param id 游轮的id
	 * 2016-2-2
	 */
	@ResponseBody
	@RequestMapping(value="updateCheck")
	public boolean updateCheck(String uuid){
		boolean check=false;
		try {
			check = cruiseshipInfoService.updateCheck(uuid);
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return check;
	}
	/**
	 * 删除检查接口
	 * @param uuid
	 * @return
	 * @author zhangchao
	 * 2016-2-2
	 * @throws BaseException4Quauq 
	 */
	public boolean deleteCheck(String uuids) throws BaseException4Quauq{
		boolean check=false;
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					check = cruiseshipInfoService.updateCheck(uuid);
					if(check){
						throw new BaseException4Quauq("数据已被使用，不可删除");
					}
				}
			}
		return check;
	}
	/**
	 * 下载接口
	 * @author zhangchao
	 * 2016-2-3
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
	 * 将上传的凭证保存到cruiseship_annex
	 * @author zhangchao
	 * @param uuid 游轮的uuid
	 * @param docIds docInfo 的id
	 * @param response
	 * 2016-2-2
	 * @throws BaseException4Quauq 
	 */
	public void saveFile(String uuid,String docIds) throws BaseException4Quauq{
			try {
				String[] split = docIds.split(",");	 
				CruiseshipInfo cruiseshipInfo = cruiseshipInfoService.getByUuid(uuid);
				List<CruiseshipAnnex> list=new ArrayList<CruiseshipAnnex>();
				for(String docId:split){
					DocInfo docInfo = docInfoService.getDocInfo(Long.parseLong(docId));
					CruiseshipAnnex cruiseshipAnnex=new CruiseshipAnnex();
					cruiseshipAnnex.setDocId(docInfo.getId().intValue());
					cruiseshipAnnex.setDocName(docInfo.getDocName());
					cruiseshipAnnex.setMainUuid(cruiseshipInfo.getUuid());
					cruiseshipAnnex.setWholesalerId(cruiseshipInfo.getWholesalerId());
					cruiseshipAnnex.setUuid(UuidUtils.generUuid());
					cruiseshipAnnex.setType(1);
					cruiseshipAnnex.setDocPath(docInfo.getDocPath());
					cruiseshipAnnex.setDocType(docInfo.getDocType());
					list.add(cruiseshipAnnex);
				}
				for(CruiseshipAnnex cruiseshipAnnex:list){
					cruiseshipAnnexService.save(cruiseshipAnnex);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BaseException4Quauq(e.getMessage());
			}
				
	
	}
	/**
	 * 根据游轮的uuid获得CruiseshipAnnex，主要用于修改页面凭证的查询
	 * @param uuid 游轮的uuid
	 * @return
	 * @author zhangchao
	 * @throws BaseException4Quauq 
	 */
	public List<CruiseshipAnnex> getDoc(String uuid) throws BaseException4Quauq{
		List<CruiseshipAnnex> cruiseshipAnnexs = new ArrayList<CruiseshipAnnex>();
		
		cruiseshipAnnexs = cruiseshipAnnexService.getCruiseshipAnnexByMainUuid(uuid, 1);
		
		return cruiseshipAnnexs;
	}
	
	@ResponseBody
	@RequestMapping(value="check")
	public boolean check(String name){
		List<CruiseshipInfo> list = cruiseshipInfoService.getCruiseshipInfoByName(name);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	@ResponseBody
	@RequestMapping(value="checks")
	public boolean checks(String name,String uuid){
		List<CruiseshipInfo> list = cruiseshipInfoService.getCruiseshipInfoByName(name);
		for(CruiseshipInfo cruiseshipInfo:list){
			if(!cruiseshipInfo.getUuid().equals(uuid)){
				return true;
			}
		}
		return false;
	}
}
