/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.modules.mtourCommon.jsonbean.AgentInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.BankInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.CityInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.FileInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SupplierInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SysCompanyDictViewJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.TopLevelMenuInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.UserInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.service.MtourCommonService;
import com.trekiz.admin.modules.mtourCommon.transfer.FileInfoTransfer;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 美图国际通用数据接口
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/mtour/common")
public class MtourCommonController extends MtourBaseController {
	
	@Autowired
	private MtourCommonService mtourCommonService;

	@Autowired
	private DocInfoService docInfoService;
	
	@RequestMapping(value = "mtourIndex")
	public String mtourIndex() {
        return "modules/mtour/index";
	}
	/**
	 * 美途国际上传组件封装
	     * <p>@Description TODO</p>
		 * @Title: uploadFile
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-14 上午10:57:37
	 */
	@ResponseBody
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String uploadFile(BaseInput4MT input, MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		BaseOut4MT out = new BaseOut4MT(input);
		Map<String, MultipartFile> fileMap = request.getFileMap();
		// 上传完成后，需要返回上传文件对象
		List<FileInfoJsonBean> fileInfoJsonBeans = new ArrayList<FileInfoJsonBean>();

		try {
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile file = entity.getValue();
				String originalName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(originalName).toLowerCase(Locale.ENGLISH);
				int limitSize = Integer.parseInt(Global.getConfig("web.maxUploadSize"));
				 if(file.getSize()>limitSize){
					 throw new Exception("上传失败：文件大小不能超过10M");
				 }
				// 使用唯一标识码生成文件名
				String newName = UUID.randomUUID().toString() + "." + (ext != null ? ext : "");
	
				File uploadFile = null;
				StringBuilder sb = new StringBuilder();
	
				sb.append(FileUtils.getUploadFilePath().get(1));
				uploadFile = new File(sb.toString());
				if (!uploadFile.exists()) {
					uploadFile.mkdirs();
				}
				uploadFile = new File(sb.toString(), newName);
				FileCopyUtils.copy(file.getBytes(), uploadFile);
	
				// 保存到DocInfo
				DocInfo doc = new DocInfo();
				doc.setDocName(originalName);
				doc.setDocPath(FileUtils.getUploadFilePath().get(0) + newName);
				Long docId = docInfoService.saveDocInfo(doc).getId();
				doc.setId(docId);
				fileInfoJsonBeans.add(FileInfoTransfer.docInfo2FileInfoJsonBean(doc));
			}
			out.setData(fileInfoJsonBeans);
			out.setResponseCode4Success();
		} catch (Exception e) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("上传文件出错，请重新上传！");
			
			e.printStackTrace();
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 删除已经上传的文件
	     * <p>@Description TODO</p>
		 * @Title: delFile
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-15 上午9:54:01
	 */
	@ResponseBody
	@RequestMapping(value = "delFile", method = RequestMethod.POST)
	public String delFile(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String attachmentUuid = input.getParamValue("attachmentUuid");
		if(StringUtils.isEmpty(attachmentUuid)) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数错误，请重新操作！");
		} else {
			docInfoService.delDocInfoById(Long.parseLong(attachmentUuid));
			out.setResponseCode4Success();
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 特定的文件下载
		 * @Title: download
	     * @return ResponseEntity<byte[]>
	     * @author majiancheng       
	     * @date 2015-10-29 下午2:17:54
	 */
	@RequestMapping(value="download/{docid}")
    public ResponseEntity<byte[]> download(@PathVariable("docid") Long docid,HttpServletResponse response) {
    	File downFile;
    	OutputStream os = null;
    	try {
    		DocInfo docInfo = docInfoService.getDocInfo(docid);
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
		return null;
    }
    /**
	 * 根据字典类型获取相应字典数据接口
	     * <p>@Description TODO</p>
		 * @Title: getTourOperatorTypeList
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:35:09
	 */
	@ResponseBody
	@RequestMapping(value = "getDictListByType")
	public String getDictListByType(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String dictType = input.getParamValue("dictType");
		if(StringUtils.isEmpty(dictType)) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数错误，请输入相应字典类型！");
		} else {
			try{
				//update by WangXK 根据公司id分开进行查询
				List<SysCompanyDictViewJsonBean> list = mtourCommonService.getDictListByType(dictType);
				out.setResponseCode4Success();
				if(CollectionUtils.isNotEmpty(list)){
					out.setData(list);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
 		return toAndCacheJSONString(out);
	}
	/**
	 * 根据字典类型获取相应字典数据接口
	     * <p>@Description TODO</p>
		 * @Title: getTourOperatorTypeList
	     * @return String
	     * @author WangXK   
	     * @date 2015-11-06 下午2:35:09
	 */
	@ResponseBody
	@RequestMapping(value = "getDictListByTypeAndCompanyId")
	public String getDictListByTypeAndCompanyId(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String dictType = input.getParamValue("dictType");
		if(StringUtils.isEmpty(dictType)) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数错误，请输入相应字典类型！");
		} else {
			//update by WangXK 根据视图进行查询
			List<SysCompanyDictViewJsonBean> list = mtourCommonService.getDictListByTypeAndCompanyId(getCompanyId(),dictType);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				out.setData(list);
			}
		}
		
 		return toAndCacheJSONString(out);
	}
	/**
	 * 根据用户类型、用户姓名、返回个数，返回用户数据
	 * @param roleType,userName,count
	 * @return
	 */ 
	@ResponseBody
	@RequestMapping(value = "getUserByRoleType")
	public String getUserByRoleType(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		//获得传递的参数值
		String roleType = input.getParamValue("roleType");
		String userName = input.getParamValue("userName");
		String count = input.getParamValue("count");
		if(StringUtils.isNotBlank(count)){
			List<UserInfoJsonBean> list = mtourCommonService.getUserByRoleType(roleType,userName,count);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				out.setData(list);
			}
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 根据渠道类型,输出该类型下的渠道
	 * @author ang.gao 
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAgentinfoByTypeCode")
	public String getAgentinfoByTypeCode(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		
		String channelTypeCode = input.getParamValue("channelTypeCode");//渠道类型代码（
		String channelName = input.getParamValue("channelName");//渠道名称
		String count = input.getParamValue("count");//返回个数（ 必填）
		
		List<AgentInfoJsonBean> jsonBeanList = new ArrayList<AgentInfoJsonBean>();
		if(StringUtils.isNotBlank(count)){
			List<Map<String,Object>> list = mtourCommonService.getAgentinfoByTypeCode(channelTypeCode, channelName, count);
			for(Map<String,Object> map : list){
				AgentInfoJsonBean bean = new AgentInfoJsonBean();
				bean.setChannelUuid(map.get("channelUuid")!=null ? map.get("channelUuid").toString() : "");//渠道id
				bean.setChannelName(map.get("channelName")!=null ? map.get("channelName").toString() : "");//渠道名称
				bean.setContactName(map.get("contactName")!=null ? map.get("contactName").toString() : "");//渠道联系人姓名
				bean.setContactPhone(map.get("contactPhone")!=null ? map.get("contactPhone").toString() : "");//渠道联系人电话
				if(map.get("channelTel")!=null && StringUtils.isNotBlank(map.get("channelTel").toString())){//渠道固定电话
					StringBuffer channelTel = new StringBuffer();
					if(map.get("agentTelAreaCode")!=null && StringUtils.isNotBlank(map.get("agentTelAreaCode").toString())){//区号
						channelTel.append(map.get("agentTelAreaCode").toString()).append("-");
					}
					channelTel.append(map.get("channelTel").toString());
					bean.setChannelTel(channelTel.toString());
				}else{
					bean.setChannelTel("");
				}
				bean.setChannelZipCode(map.get("channelZipCode")!=null ? map.get("channelZipCode").toString() : "");//渠道邮编
				bean.setChannelAddress(map.get("channelAddress")!=null ? map.get("channelAddress").toString() : "");//渠道地址,只读取了街道地址
				if(map.get("channelTax")!=null && StringUtils.isNotEmpty(map.get("channelTax").toString())){//渠道传真
					StringBuffer channelTax = new StringBuffer();
					if(map.get("agentFaxAreaCode")!=null && StringUtils.isNotEmpty(map.get("agentFaxAreaCode").toString())){//传真区号
						channelTax.append(map.get("agentFaxAreaCode").toString()).append("-");
					}
					channelTax.append(map.get("channelTax").toString());
					bean.setChannelTax(channelTax.toString());
				}else{
					bean.setChannelTax("");
				}
				jsonBeanList.add(bean);
			}
			
			out.setData(jsonBeanList);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
		
		return toAndCacheJSONString(out);
	}

	
	

	/**
	 * 根据地接社类型uuid,地接社类型名称，返回数量,输出对应该类型的地接社列表
	 * @author gao
	 * 2015年10月14日
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSupplierList")
	public String getSupplierList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String tourOperatorTypeUuid = input.getParamValue("tourOperatorTypeCode");//地接社类型值
		String tourOperatorName = input.getParamValue("tourOperatorName");//地接社类型名称
		String count = input.getParamValue("count"); // 返回数量
		try{
			if(StringUtils.isNotEmpty(count)){
				List<SupplierInfoJsonBean> list = mtourCommonService.getSupplierList(tourOperatorTypeUuid, tourOperatorName, Integer.parseInt(count));
				out.setResponseCode4Success();
				if(CollectionUtils.isNotEmpty(list)){
					out.setData(list);
				}
			}else{
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常，返回数量为空！");
			}
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("接口处理异常.");
		}
 		return toAndCacheJSONString(out);
	}
	

	/**
	 * 获取渠道银行账户信息
	 * <p>@Description TODO</p>
	 * @Title: getBankInfo
     * @return String
     * @author 赵海明       
     * @date 2015-10-14 下午3:35:09
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value="getBankInfo")
	public String getBankInfo(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		//update by zhanghao 前端输入参数类型 变成 数字型
		Long id=null;
		if(StringUtils.isNotEmpty(input.getParamValue("uuid"))){
			id = input.getParamValue4Object("uuid", Long.class);//渠道Id,批发商Id
		}
		String type = input.getParamValue("type");//0表示批发商,1表示地接社,2表示渠道
		List<BankInfoJsonBean> list = null;
		if(id!=null && StringUtils.isNotBlank(type)){
			list = mtourCommonService.getBankInfo(id, type);
		}
		out.setResponseCode4Success();
		if(CollectionUtils.isNotEmpty(list)){
			out.setData(list);
		}		
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 查询国家
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCountryVaguely")
	public String getCountryVaguely(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			if(StringUtils.isEmpty(input.getParamValue("lineType")) || input.getParamValue("lineType").equals("1")){
				list = mtourCommonService.getCountryVaguely(input);
			}else{
				list = mtourCommonService.getEU(input);
			}
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				out.setData(list);
			}
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 查询航空公司
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAirlineVaguely")
	public String getAirlineVaguely(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String,String>> list = mtourCommonService.getAirlineVaguely(input);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				out.setData(list);
			}
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 查询机场
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAirportVaguely")
	public String getAirportVaguely(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String,String>> list = mtourCommonService.getAirportVaguely(input);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				out.setData(list);
			}
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 查询币种
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCurrency")
	public String getCurrency(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String,String>> list = mtourCommonService.getCurrency(input);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				out.setData(list);
			}
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 根据查询类型,输入文本统计订单数量
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "countOrders")
	public String countOrders(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			Map<String,Object> map = mtourCommonService.countOrders(input);
			out.setData(map);
			out.setResponseCode4Success();
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return toAndCacheJSONString(out);
	}

	/**
	 * 获取城市
	 * @author gao
	 * 2015年10月20日
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCity")
	public String getCity(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		List<CityInfoJsonBean> cityList = new ArrayList<CityInfoJsonBean>();
		try{
			String cityKey = input.getParamValue("cityKey");// 城市名称或者快捷搜索码(可模糊搜索，中文或拼音)
			String count = input.getParamValue("count"); // 返回个数，-1表示返回全部,必填‘
			if(StringUtils.isNotBlank(count)){
				int countInt = Integer.parseInt(count);
				cityList = mtourCommonService.getCityInfo(cityKey, countInt);
				out.setResponseCode4Success();
				if(CollectionUtils.isNotEmpty(cityList)){
					out.setData(cityList);
				}
			}else{
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常，快捷搜索码或返回数量为空！");
			}
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取当前用户对应的menu
	 * @author ning.zhang@quauq.com
	 * @date 2015年10月22日17:42:10
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="getMenuByUser")
	public String getMenuByUser(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			Object[] objs = mtourCommonService.getMenuInfoByCurrentUser();
			if(objs[0].equals(true)){
				out.putMsgCode(BaseOut4MT.CODE_0003);
				out.putMsgDescription("发现有无父节点的Menu " + objs[1].toString());
			}
			List<TopLevelMenuInfoJsonBean>  menus = (List<TopLevelMenuInfoJsonBean>) objs[2];
			if(CollectionUtils.isNotEmpty(menus)){
				out.setData(menus);
			}
			out.setResponseCode4Success();   
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return this.toAndCacheJSONString(out);
	}
	
	/**
	 * 全站统计信息--付款信息数量
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getPaymentCount")
	public String getPaymentCount(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			JSONObject jsonObject = JSONObject.parseObject(input.getParam());
			JSONObject jsonObj = jsonObject.getJSONObject("searchParam");
			Integer searchType = jsonObj.getInteger("searchType");
			String searchKey = jsonObj.getString("searchKey");
			Map<String, Object> map = mtourCommonService.getPaymentCount(searchType, searchKey);
			out.setData(map);
			out.setResponseCode4Success();
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 全站统计信息--收款信息数量
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getReceiptCount")
	public String getReceiptCount(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			JSONObject jsonObject = JSONObject.parseObject(input.getParam());
			JSONObject jsonObj = jsonObject.getJSONObject("searchParam");
			Integer searchType = jsonObj.getInteger("searchType");
			String searchKey = jsonObj.getString("searchKey");
			Map<String, Object> map = mtourCommonService.getReceiptCount(searchType, searchKey);
			out.setData(map);
			out.setResponseCode4Success();
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 全站统计信息--全部信息数量
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="countAllInfo")
	public String countAllInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String, Object>> list = mtourCommonService.countAllInfo(input);
			out.setData(list);
			out.setResponseCode4Success();
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取用户信息(获取当前用户的个人信息)
		 * @Title: getCurrentUserInfo
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-26 下午2:00:28
	 */
	@ResponseBody
	@RequestMapping(value="getCurrentUserInfo")
	public String getCurrentUserInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			UserInfoJsonBean data = mtourCommonService.getCurrentUserInfo();
			out.setData(data);
			out.setResponseCode4Success();
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return JSON.toJSONStringWithDateFormat(out, DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM);
	}
	
	/**
	 * 获取当前用户权限内订单的统计信息
	 * @author wangxv
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="orderStatisticsInfo")
	public String orderStatisticsInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		Long currentUserCompanyId = UserUtils.getCompanyIdForData();
		Map<String, Object>  result = mtourCommonService.orderStatisticsInfo(currentUserCompanyId);
		out.setData(result);
		out.setResponseCode4Success();
		return toAndCacheJSONString(out);
	}
}
