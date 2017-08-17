package com.trekiz.admin.agentToOffice.personnalInfo.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.agentToOffice.personnalInfo.exception.AgentException;
import com.trekiz.admin.agentToOffice.personnalInfo.service.PersonInfoService;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.AreaDao;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value="${adminPath}/person/info")
public class PersonInfoController {
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaDao areaDao;

	/**
	 * 将Map类型的数据转换成List类型的数据
	 * @param data	Map数据
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.16
     */
	private List<Map<String, Object>> transMapToList(Map<String, String> data){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		if(null == data || 0 == data.size()){
			return list;
		}
		Set<String> keySet = data.keySet();
		for(String key : keySet){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", key);
			map.put("name", data.get(key));
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据传递过来的参数查询其子集信息，此处是指下拉框联动
	 * @param parentId
	 * @param model
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.17
	 */
	@ResponseBody
	@RequestMapping(value="getChildData")
	public List<Map<String,Object>> getChildData(Long parentId){
		Map<String, String> belongsProvinceMap = agentinfoService.findAreaInfo(parentId);
		List<Map<String, Object>> list = transMapToList(belongsProvinceMap);
		return list;
	}

	/**
	 * 获得基础信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getAgentInfo")
	public String getAgentInfo(HttpServletRequest request,Model model){
		Long agentId = null;
		String agentIdParam = request.getParameter("agentId");
		model.addAttribute("ctxs", 1);
		if(StringUtils.isNotEmpty(agentIdParam)){
			//如果agentIdParam不为空则表示是查看渠道信息，则不显示编辑按钮
			model.addAttribute("updateShow","1");  // updateShow = 1 表示查看渠道信息
			agentId = Long.parseLong(agentIdParam);
			model.addAttribute("agentId", agentId); 
		}
		Agentinfo agentinfo = personInfoService.getAgentInfoById(agentId);
		if(null == agentinfo){
			return "";
		}
		//所属地区
		StringBuffer belongArea = new StringBuffer();
		if(agentinfo.getBelongsArea() != null){
			Area area = areaDao.findOne(agentinfo.getBelongsArea());
			belongArea.append(area.getName());
		}
		if(agentinfo.getBelongsAreaProvince() != null){
			Area area = areaDao.findOne(agentinfo.getBelongsAreaProvince());
			if(area!=null){
				belongArea.append(area.getName());
			}
		}
		if(agentinfo.getBelongsAreaCity() != null){
			Area area = areaDao.findOne(agentinfo.getBelongsAreaCity());
			if(area!=null){
				belongArea.append(area.getName());
			}
		}
		// 公司地址
		StringBuffer agentAddress = new StringBuffer();
		if(StringUtils.isNotBlank(agentinfo.getAgentAddress())){
			Area area = areaDao.findOne(Long.valueOf(agentinfo.getAgentAddress()));
			agentAddress.append(area.getName());
		}
		if(agentinfo.getAgentAddressProvince() != null){
			Area area = areaDao.findOne(agentinfo.getAgentAddressProvince());
			if(area!=null){
				agentAddress.append(area.getName());
			}
		}
		if(agentinfo.getAgentAddressCity() != null){
			Area area = areaDao.findOne(agentinfo.getAgentAddressCity());
			if(area!=null){
				agentAddress.append(area.getName());
			}
		}
		agentAddress.append(agentinfo.getAgentAddressStreet());
		agentinfo.setAgentAddress(belongArea.toString());// set belongArea
		agentinfo.setAgentAddressFull(agentAddress.toString());// set agentAddress
		model.addAttribute("agentInfo", agentinfo);

		Map<String, String> countryInfo = agentinfoService.findCountryInfo();
		List<Map<String, Object>> list = transMapToList(countryInfo);
		// 获取所有国家信息
		model.addAttribute("countryList", list);

		//获取联系人
		List<SupplyContacts> supplyContacts = personInfoService.getSupplyContactsByAgentId(agentinfo.getId().intValue(), 0);
		model.addAttribute("supplyContacts",supplyContacts);

		return "/agentToOffice/personInfo/agent-base-info";
	}

	/**
	 * 保存联系人信息
	 * @param request
	 * @return
	 * @author	shijun.liu
	 * @date    2016.06.20
     */
	@ResponseBody
	@RequestMapping(value = "saveContacts")
	public String saveContacts(HttpServletRequest request){
		ResponseJson responseJson = new ResponseJson();
		String contacts = request.getParameter("contacts");
		Long contactsId = personInfoService.saveOrUpdateContacts(contacts);
		responseJson.setMsg(contactsId+"");
		String json = JSON.toJSONString(responseJson);
		return json;
	}

	/**
	 * 删除联系人
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.17
	 */
	@ResponseBody
	@RequestMapping(value="deleteContacts", method = RequestMethod.POST)
	public String deleteContacts(HttpServletRequest request){
		String ids = request.getParameter("id");
		ResponseJson responseJson = new ResponseJson();
		if(StringUtils.isBlank(ids)){
			responseJson.setFlag(false);
			responseJson.setMsg("联系人ID不能为空");
			String json = JSON.toJSONString(responseJson);
			return json;
		}
		try {
			personInfoService.deleteContacts(Long.valueOf(ids));
		} catch (AgentException e) {
			responseJson.setFlag(false);
			responseJson.setMsg(e.getMessage());
		}
		String json = JSON.toJSONString(responseJson);
		return json;
	}


	/**
	 * 查询渠道银行信息
	 * @param request
	 * @param model
     * @return
     */
	@RequestMapping(value="getAgentBank")
	public String getAgentBank(HttpServletRequest request,Model model){
		
		model.addAttribute("ctxs", 1);
		String agentId = request.getParameter("agentId");
		User user = UserUtils.getUser();
		model.addAttribute("user", user);
		if(StringUtils.isNotBlank(agentId)){
			model.addAttribute("agentId",agentId);
			model.addAttribute("updateShow", "1");
		}else{
			agentId = UserUtils.getUser().getAgentId().toString();
		}
		//银行账号信息
		List<PlatBankInfo> platBankInfos = personInfoService.getPlatBankInfoByAgentId(Long.valueOf(agentId));
		model.addAttribute("agentBank", platBankInfos);
		return "/agentToOffice/personInfo/agent-bank-info";
	}

	/**
	 * 修改基础信息
	 * @param agentInfoJson
	 * @param agentPersonJson
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="updateInfo")
	public boolean updateInfoAndperson(Agentinfo agentInfo,Model model){
		try {
			personInfoService.updateInfoById(agentInfo);
			//personInfoService.updatePersonById(agentPersonJson);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 保存修改银行信息
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="saveAgentBank")
	public String saveAgentBank(HttpServletRequest request){
		ResponseJson responseJson = new ResponseJson();
		String bankInfo = request.getParameter("bankJson");
		if(StringUtils.isBlank(bankInfo)){
			responseJson.setFlag(false);
			responseJson.setMsg("所提交银行信息不能为空");
			return JSON.toJSONString(responseJson);
		}
		try {
			PlatBankInfo info = personInfoService.saveOrUpdateAgentBank(bankInfo);
			responseJson.setMsg(info.getId() + "," + info.getDefaultFlag());
		} catch (AgentException e) {
			responseJson.setFlag(false);
			responseJson.setMsg(e.getMessage());
		}
		return JSON.toJSONString(responseJson);
	}

	/**
	 * 删除银行信息
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.17
	 */
	@ResponseBody
	@RequestMapping(value="deleteAgentBank")
	public String deleteAgentBank(HttpServletRequest request){
		ResponseJson responseJson = new ResponseJson();
		String bankIdStr = request.getParameter("id");
		if(StringUtils.isBlank(bankIdStr) || !MoneyNumberFormat.isNumber(bankIdStr)){
			responseJson.setFlag(false);
			responseJson.setMsg("要删除的银行ID不能为空，且为数字");
			String json = JSON.toJSONString(responseJson);
			return json;
		}
		try {
			personInfoService.deleteAgentBank(Long.valueOf(bankIdStr));
		} catch (AgentException e) {
			responseJson.setFlag(false);
			responseJson.setMsg(e.getMessage());
		}
		String json = JSON.toJSONString(responseJson);
		return json;
	}

	/**
	 * 设置当前银行账号为默认账户
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.17
	 */
	@ResponseBody
	@RequestMapping(value="setDefaultAgentBank")
	public String setDefaultAgentBank(HttpServletRequest request){
		ResponseJson responseJson = new ResponseJson();
		String bankIdStr = request.getParameter("id");
		if(StringUtils.isBlank(bankIdStr) || !MoneyNumberFormat.isNumber(bankIdStr)){
			responseJson.setFlag(false);
			responseJson.setMsg("设置为默认的银行账号ID不能为空，且为数字");
			String json = JSON.toJSONString(responseJson);
			return json;
		}
		try {
			personInfoService.setDefaultAgentBank(Long.valueOf(bankIdStr));
		} catch (AgentException e) {
			responseJson.setFlag(false);
			responseJson.setMsg(e.getMessage());
		}
		String json = JSON.toJSONString(responseJson);
		return json;
	}

	/**
	 * 查询渠道的资质信息
	 * @param request
	 * @param model
     * @return
	 * @author shijun.liu
	 * @date   2016.06.20
     */
	@RequestMapping(value="getAgentQualification")
	public String getAgentQualification(HttpServletRequest request, Model model){
		Long agentId = null;
		model.addAttribute("ctxs", 1);
		String agentIdParam = request.getParameter("agentId");
		if(StringUtils.isNotBlank(agentIdParam)){
			//如果agentIdParam不为空则表示是查看渠道信息，则不显示编辑按钮
			model.addAttribute("updateShow","1");  // updateShow = 1 表示查看渠道信息
			agentId = Long.parseLong(agentIdParam);
			model.addAttribute("agentId", agentId);
		}
		Agentinfo agentinfo = personInfoService.getAgentInfoById(agentId);
		if(null == agentinfo){
			return "";
		}
		model.addAttribute("agentInfo", agentinfo);
		//营业执照
		if(null != agentinfo.getBusinessLicense()){
			DocInfo business =  personInfoService.getDocInfoById(agentinfo.getBusinessLicense());
			model.addAttribute("business",business );
		}
		//经营许可证
		if(null != agentinfo.getLicense()){
			DocInfo license = personInfoService.getDocInfoById(agentinfo.getLicense());
			model.addAttribute("license",license );
		}
		//税务登记证
		if(null != agentinfo.getTaxCertificate()){
			DocInfo taxCertificate =  personInfoService.getDocInfoById(agentinfo.getTaxCertificate());
			model.addAttribute("taxCertificate",taxCertificate );
		}
		//组织结构代码
		if(null != agentinfo.getOrganizeCertificate()){
			DocInfo organizeCertificate =  personInfoService.getDocInfoById(agentinfo.getOrganizeCertificate());
			model.addAttribute("organizeCertificate", organizeCertificate);
		}
		//公司法人身份证
		if(null != agentinfo.getIdCard()){
			DocInfo idCard = personInfoService.getDocInfoById(agentinfo.getIdCard());
			model.addAttribute("idCard", idCard);
		}
		//公司银行开户证明
		if(null != agentinfo.getBankOpenLicense()){
			DocInfo bankOpenLicense =  personInfoService.getDocInfoById(agentinfo.getBankOpenLicense());
			model.addAttribute("bankOpenLicense",bankOpenLicense );
		}
		//旅游资质
		if(null != agentinfo.getTravelAptitudes()){
			DocInfo travelAptitudes =  personInfoService.getDocInfoById(agentinfo.getTravelAptitudes());
			model.addAttribute("travelAptitudes",travelAptitudes );
		}
		//其他资质
		if(StringUtils.isNotBlank(agentinfo.getElseFile())){
			String [] elseFile = agentinfo.getElseFile().split(",");
			List<DocInfo> elseFileList = new ArrayList<DocInfo>();
			for (String item : elseFile){
				String idStr = item;
				String nameStr = "";
				DocInfo di = personInfoService.getDocInfoById(Long.parseLong(idStr));
				di.setElseFileName(nameStr);
				elseFileList.add(di);
			}
			model.addAttribute("elseFileList",elseFileList );
		}
		return "/agentToOffice/personInfo/agent-qualification";
	}

	/**
	 * 保存资质信息
	 * @param request
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.20
     */
	@ResponseBody
	@RequestMapping(value="saveAgentQualification")
	public String saveQualifications(HttpServletRequest request){
		ResponseJson responseJson = new ResponseJson();
		String type = request.getParameter("type");
		String docId = request.getParameter("docId");
		if(StringUtils.isBlank(docId) || !MoneyNumberFormat.isNumber(docId)){
			responseJson.setFlag(false);
			responseJson.setMsg("资质ID不能为空，且为数值类型");
			return JSON.toJSONString(responseJson);
		}
		try {
			personInfoService.saveOrUpdateQualication(type, Long.valueOf(docId));
		} catch (Exception e) {
			responseJson.setFlag(false);
			responseJson.setMsg(e.getMessage());
		}
		return JSON.toJSONString(responseJson);
	}

	/**
	 * 删除资质信息
	 * @param request
	 * @return
	 * @author shijun.liu
	 * @date   2016.06.27
	 */
	@ResponseBody
	@RequestMapping(value="deleteQualification")
	public String deleteQualifications(HttpServletRequest request){
		ResponseJson responseJson = new ResponseJson();
		String type = request.getParameter("type");
		String docId = request.getParameter("docId");
		if(StringUtils.isBlank(docId) || !MoneyNumberFormat.isNumber(docId)){
			responseJson.setFlag(false);
			responseJson.setMsg("资质ID不能为空，且为数值类型");
			return JSON.toJSONString(responseJson);
		}
		try {
			personInfoService.deleteQualication(type, Long.valueOf(docId));
		} catch (Exception e) {
			responseJson.setFlag(false);
			responseJson.setMsg(e.getMessage());
		}
		return JSON.toJSONString(responseJson);
	}

	/**
	 * 删除资质
	 * @param id
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="deleteFiles")
	public boolean deleteFiles(Long id){
		try {
			personInfoService.deleteFiles(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 获得logo图片
	 * @param id
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequestMapping(value="getLogo")
	public void getLogo(HttpServletResponse response, Long id){
        InputStream is = null;
        OutputStream out = null;
		if(null == id){
			return;
		}
        try {  
        	DocInfo info = docInfoDao.findOne(id);
        	String filePath = Global.getBasePath() +File.separator + info.getDocPath();
			File file = new File(filePath);
			if(!file.exists()){
				return;
			}
            is = new BufferedInputStream(new FileInputStream(filePath));
            out = response.getOutputStream();
            // size = is.available(); // 得到文件大小  
            byte[] bytes = new byte[10240];  
            int len = 0;  
            // 开始读取图片信息  
            while (-1 != (len = is.read(bytes))) {  
                out.write(bytes, 0, len);  
            }  
            out.flush();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally{
        	if(null != is){
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(null != out){
        		try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}


}
