package com.trekiz.admin.modules.eprice.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ListSearchForm;
import com.trekiz.admin.modules.eprice.form.ProjectFirstForm;
import com.trekiz.admin.modules.eprice.form.ProjectSecondForManagerForm;
import com.trekiz.admin.modules.eprice.form.ProjectSecondForm;
import com.trekiz.admin.modules.eprice.form.ProjectThirdForManagerForm;
import com.trekiz.admin.modules.eprice.form.ProjectThirdForm;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitLinesAreaDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitRequirementsDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceBaseInfoDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceFileDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceProjectDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceTrafficRequirementsDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceAdmitRequirementsService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceBaseInfoService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceFileService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceProjectService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficLineService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficRequirementsService;
import com.trekiz.admin.modules.eprice.service.EstimatePricerReplyService;
import com.trekiz.admin.modules.eprice.utils.EstimatePriceUtils;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;
import com.trekiz.admin.modules.epriceDistribution.query.EstimatePriceDistributionQuery;
import com.trekiz.admin.modules.epriceDistribution.service.EstimatePriceDistributionService;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service("estimatePriceProjectService")
@Transactional(readOnly = true)
public class EstimatePriceProjectServiceImpl extends BaseService implements EstimatePriceProjectService{
	
	@Autowired
	private EstimatePriceProjectDao estimatePriceProjectDao;
	@Autowired
	private DocInfoDao docInfoDao;
	
	@Autowired
	private EstimatePriceFileDao estimatePriceFileDao;
	
	@Autowired
	private EstimatePriceAdmitLinesAreaDao estimatePriceAdmitLinesAreaDao;
	
	@Resource
	private SystemService systemService;
	
	@Resource
	private AgentinfoService agentinfoService;
	
	@Resource
	private EstimatePriceBaseInfoService estimatePriceBaseInfoService;
	
	@Resource
	private EstimatePriceAdmitRequirementsService estimatePriceAdmitRequirementsService;
	
	@Resource
	private EstimatePriceFileService  estimatePriceFileService;
	
	@Resource
	private EstimatePriceTrafficRequirementsService estimatePriceTrafficRequirementsService;
	
	@Resource
	private EstimatePriceTrafficLineService estimatePriceTrafficLineService;
	
	@Resource
	private EstimatePriceRecordService estimatePriceRecordService;
	
	@Resource
	private EstimatePricerReplyService estimatePricerReplyService;
	@Autowired
	private EstimatePriceBaseInfoDao estimatePriceBaseInfoDao;
	@Autowired
	private EstimatePriceAdmitRequirementsDao estimatePriceAdmitRequirementsDao;
	@Autowired
	private EstimatePriceTrafficRequirementsDao estimatePriceTrafficRequirementsDao;
	
	@Autowired
	protected LogOperateService logOpeService;
	
	@Autowired
	private EstimatePriceDistributionService estimatePriceDistributionService;
	

	public void save(EstimatePriceProject epp) {
		estimatePriceProjectDao.save(epp);
		estimatePriceProjectDao.getSession().flush();
	}
	
	
	public void update(EstimatePriceProject epp) {
		 
		estimatePriceProjectDao.update(epp);
	}


	public EstimatePriceProject findById(Long id) {
		if(id==null){
			return null;
		}
		return estimatePriceProjectDao.findById(id);
	}

	public void delById(Long id) {
		if(id==null){
			return ;
		}
		EstimatePriceProject epp = new EstimatePriceProject();
		epp.setId(id);
		epp.setStatus(EstimatePriceProject.STATUS_DEL);
		epp.setModifyTime(new Date());
		
		this.save(epp);
		
	}

	
	
	public String createTitle(EstimatePriceProject epp){
		String title= epp.getCompanyName()+"_"+epp.getCompanyId()+"_"+epp;
		
		return title;
	}

	//@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Object> addProjectFirst(ProjectFirstForm pff,User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		User saler = systemService.getUser(pff.getSalerId());
		if(saler==null){
			map.put("res", "data_error");
			map.put("mes", "saler is null");
			return map;
		}
		
		Date date = new Date();
		boolean isadd;
		
		//初始化询价项目（草稿）
		EstimatePriceProject epp = this.findById(pff.getProjectId());
		EstimatePriceBaseInfo epbi;
		if(epp==null ){
			epp = new EstimatePriceProject();

			// 设定询价项目为计调主管专用“待分配”状态
			if(2 == UserUtils.getUser().getCompany().getEstimateModel() || (3 == UserUtils.getUser().getCompany().getEstimateModel() && pff.getEmode() == 2)){
				epp.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_FORMANAGER);
			}else{
				epp.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_WAITING);
			}
			
			epp.setStatus(EstimatePriceProject.STATUS_DRAFT);
			epp.setUserId(loginUser.getId());
			epp.setUserName(loginUser.getName());
			
			epp.setCreateTime(date);
			epp.setLastCreateProductTime(date);
			
			
			isadd = true;
		}else{
			isadd = false;
		}
		
		
		if(isadd||ProjectSecondForm.onceAgainYes==pff.getOnceAgain()){
			//初始化询价基本信息（草稿）
			epbi = new EstimatePriceBaseInfo();
			epbi.setStatus(EstimatePriceBaseInfo.STATUS_DRAFT);
			epbi.setCreateTime(date);
			epp.setLastBaseInfo(epbi);
		}else{
			epbi = epp.getLastBaseInfo();
		}
		
		
		
		epp.setCompanyId(saler.getCompany().getId());
		epp.setCompanyName(saler.getCompany().getName());
		
//		if(EstimatePriceProject.TYPE_ALONE==pff.getType()){
//			epp.setType(EstimatePriceProject.TYPE_ALONE);
//		}else if(EstimatePriceProject.TYPE_FLIGHT==pff.getType()){
//			epp.setType(EstimatePriceProject.TYPE_FLIGHT);
//		}
		
		epp.setType(pff.getType());
		
		
		epp.setLastEstimatePriceTime(date);
		epp.setModifyTime(date);
		
		epbi.setSalerId(saler.getId());
		epbi.setSalerName(saler.getName());
		String phone = saler.getPhone();
		if(phone!=null && !"".equals(phone)){
			epbi.setSalerPhone(phone);
		}else{
			epbi.setSalerPhone(saler.getMobile());
		}		
		epbi.setSalerEmail(saler.getEmail());
		epbi.setTeamType(pff.getTeamType());
		epbi.setBudget(pff.getBudget());
		epbi.setBudgetType(pff.getBudgetType());
		epbi.setBudgetRemark(pff.getBudgetRemark());
		//询价客户类型
		Integer customerType = pff.getCustomerType();
		epbi.setOtherContactWay(pff.getOtherContactWay());
		if(customerType.equals(EstimatePriceBaseInfo.CUSTOMER_TYPE_STRAIGHT)){
		//询价客户类型:直客 1
			epbi.setCustomerName(pff.getCustomerName());
			epbi.setContactPerson(pff.getContactPerson());
			epbi.setContactMobile(pff.getContactMobile());
		}else if(customerType.equals(EstimatePriceBaseInfo.CUSTOMER_TYPE_PEER)){
		//询价客户类型:同行 2
			Long caid = pff.getCustomerAgentId();
			/*
			 * 获取同行客户渠道商数据
			 */
			Agentinfo agentinfo = agentinfoService.findOne(caid);
			if(agentinfo==null){
				map.put("res", "data_error");
				map.put("mes", "找不到对应的客户信息");
				return map;
			}
//			if(caid==-1){
//				epbi.setCustomerAgentId(caid);
//				epbi.setContactPerson(pff.getContactPersontogether());
//				epbi.setContactMobile(pff.getContactMobiletogether());
//			}else{
			epbi.setCustomerAgentId(caid);
			epbi.setCustomerName(agentinfo.getAgentName());
			epbi.setContactPerson(pff.getContactPersontogether());
			epbi.setContactMobile(pff.getContactMobiletogether());
//			}
			
		}else if(customerType.equals(EstimatePriceBaseInfo.CUSTOMER_TYPE_OTHER)){
		//询价客户类型:其他 0
			epbi.setOtherContactWay(pff.getOtherContactWay());
			epbi.setCustomerName(pff.getOtherCustomerName());
			epbi.setCustomerName(pff.getOtherCustomerName());
			epbi.setContactPerson(pff.getOtherContactPerson());
			epbi.setContactMobile(pff.getOtherContactMobile());
			epbi.setBudget(pff.getBudget());
			epbi.setBudgetType(pff.getBudgetType());
			epbi.setBudgetRemark(pff.getBudgetRemark());
			
		}else{
			map.put("res", "data_error");
			map.put("mes", "customerType is error");
			return map;
		}
		
		epbi.setCustomerType(customerType);
		
//		//接待计调人员id
//		epbi.setAoperatorUserJson(JSONArray.fromObject(pff.getAoperatorUserId()).toString());
//		//票务计调人员id
//		epbi.setToperatorUserJson(JSONArray.fromObject(pff.getToperatorUserId()).toString());
		
		//各种人数
		epbi.setAllPersonSum(pff.getAllPersonSum());
		epbi.setAdultSum(pff.getAdultSum());
		epbi.setChildSum(pff.getChildSum());
		epbi.setSpecialPersonSum(pff.getSpecialPersonSum());
		epbi.setSpecialRemark(pff.getSpecialRemark());
		
		epbi.setModifyTime(date);
		
		estimatePriceBaseInfoService.save(epbi);
		
		this.save(epp);
		
		if(isadd|| ProjectSecondForm.onceAgainYes == pff.getOnceAgain()){
			Long id = epp.getId();
			EstimatePriceBaseInfo epbi2 = estimatePriceBaseInfoService.findById(epbi.getId());
			epbi2.setPid(id);
			estimatePriceProjectDao.getSession().saveOrUpdate(epbi2);
			estimatePriceProjectDao.getSession().flush();
		}
		epp = this.findById(epp.getId());
		map.put("model", epp);
		return map;
	}

	
	public Map<String, Object> addProjectSecond(ProjectSecondForm psf,User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		Long pid = psf.getProjectId();
		EstimatePriceProject epp = this.findById(pid);
		if(epp==null){
			map.put("res", "data_error");
			map.put("mes", "pid is error");
			return map;
		}
		
		Date date = new Date();
		boolean isadd;
		// 询价基本信息
		EstimatePriceBaseInfo epbi = epp.getLastBaseInfo();
		// 地接计调
		epbi.setAoperatorUserJson(JSONArray.fromObject(psf.getAoperatorUserId()).toString());
		
		// 接待社询价内容
		EstimatePriceAdmitRequirements epar = epp.getLastAdmitRequirements();
		
		if(epar == null || ProjectSecondForm.onceAgainYes==psf.getOnceAgain() ){
			epar = new EstimatePriceAdmitRequirements();
			epar.setPid(pid);
			epar.setStatus(EstimatePriceAdmitRequirements.STATUS_DRAFT);
			epar.setCreateTime(date);
			
			isadd = true;
		}else{
			isadd = false;
		}
		
		epar.setDgroupOutDate(DateUtils.dateFormat(psf.getDgroupOutDate(), "yyyy-MM-dd"));
		if(psf.getOutAreaId()!=null&&psf.getOutAreaId()>0){
			epar.setOutAreaId(psf.getOutAreaId());
			epar.setOutAreaName(psf.getOutAreaName());
		}else{
			epar.setOutAreaId(null);
			epar.setOutAreaName(null);
		}
		if(psf.getTravelCountryId()!=null && psf.getTravelCountryId().length>0){
			epar.setTravelCountryId(JSONArray.fromObject(psf.getTravelCountryId()).toString());
		}
		if(psf.getTravelCountry()!=null && psf.getTravelCountry().length>0){
			epar.setTravelCountry(JSONArray.fromObject(psf.getTravelCountry()).toString());
		}
		epar.setOutsideDaySum(psf.getOutsideDaySum());
		epar.setOutsideNightSum(psf.getOutsideNightSum());
		
		JSONObject json = JSONObject.fromObject(replaceEnter(psf.getTravelRequirements()));
		JSONObject travelTeam = json.getJSONObject("travelTeamType");
		
		for(int num = 0;num<travelTeam.size();num++){
			String optName = "option_"+num;
			// 循环判断对应组是否被选中
			if(optName!=null){
				JSONObject option = travelTeam.getJSONObject(optName);
				
				if(option!=null && option.get("type")!=null){
					epar.setTravelTeamType(option.getInt("type"));
				}
				
				try {
					epar.setTravelTeamTypeName(epar.getTravelTeamTypeName()+","+URLDecoder.decode(option.getString("title"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					map.put("res", "data_error");
					map.put("mes", "travelTeamTypeName decode  error");
					return map;
				}
			}
		}
		
		
		epar.setTravelRequirements(json.toString());
		
		epar.setModifyTime(date);
		
		if(isadd){
			estimatePriceAdmitRequirementsService.save(epar);
		}else{
			estimatePriceAdmitRequirementsService.update(epar);
			//删除原来admit_lines_area记录
			estimatePriceAdmitLinesAreaDao.deleteByAdmitId(epar.getId());
		}
		
		//新增admit_lines_area记录
		if(psf.getTravelCountryId()!=null && psf.getTravelCountryId().length>0){
			Long[] countryId = psf.getTravelCountryId();
			for(int i=0;i<countryId.length;i++){
				estimatePriceAdmitLinesAreaDao.save(epar.getId(),countryId[i]);
			}
		}
		
		// 上传文件变为了数组
		Long[] epfid = psf.getSalerTripFileId();
		String[] epfName = psf.getSalerTripFileName();
		String[] epfPath = psf.getSalerTipFilePath();
		if(epfid!=null&& epfid.length>0 && epfName!=null && epfName.length>0 && epfPath!=null && epfPath.length>0){
			for(int n=0;n<epfid.length;n++){
				DocInfo docInfo = docInfoDao.findOne(epfid[n]);
				EstimatePriceFile epf = new EstimatePriceFile();
				epf.setPid(epar.getId());
				epf.setDocInfoId(docInfo.getId());
				epf.setPtype(1); //type=1 表示为销售附件
				epf.setType(1);
				epf.setPath(docInfo.getDocPath());
				epf.setFileName(docInfo.getDocName());
				epf.setUserId(epp.getUserId());
				epf.setUserName(epp.getUserName());
				// 获取上传文件的扩展名
				String name  = docInfo.getDocName();
				if(name!=null && name.indexOf(".")>0){
					Integer m = name.indexOf(".");
					String ext = name.substring(m, name.length());
					epf.setExt(ext);
				}
				epf.setStatus(EstimatePriceFile.STATUS_NORMAL);
				epf.setCreateTime(new Date());
				epf.setModifyTime(new Date());
				// EstimatePriceFile文件类和EstimatePriceAdmitRequirements类由一对一改为多对一
				estimatePriceFileService.save(epf);
			}
		}
		epp.setLastAdmitRequirements(epar);
		epp.setModifyTime(date);
		
		EstimatePriceBaseInfo info = new EstimatePriceBaseInfo(epbi);
		estimatePriceBaseInfoService.update(epbi);
		epp.setLastBaseInfo(info);
		this.save(epp);//修改
		map.put("model", epp);
		return map;
	}
	/**
	 * 20150902 新增，由计调主管分配计调的询价流程
	 * 第二步表单提交
	 * 
	 */
	public Map<String, Object> addProjectForManagerSecond(ProjectSecondForManagerForm psf,User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		Long pid = psf.getProjectId();
		EstimatePriceProject epp = this.findById(pid);
		if(epp==null){
			map.put("res", "data_error");
			map.put("mes", "pid is error");
			return map;
		}
		
		Date date = new Date();
		boolean isadd;
		// 询价基本信息
		EstimatePriceBaseInfo epbi = epp.getLastBaseInfo();
		epbi.setFormanager(Context.FOR_MANAGER_YES);// 设置为计调主管分配计调
		estimatePriceBaseInfoService.update(epbi);
		// 添加计调主管
		EstimatePriceDistributionQuery query = new EstimatePriceDistributionQuery();
		query.setEstimateBaseId(Integer.valueOf(epbi.getId().toString()));
		query.setType(Context.TYPE_OP_DAN_TUAN);// 分类为地接计调
		query.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<EstimatePriceDistribution> butList = estimatePriceDistributionService.find(query);
		if(butList!=null && !butList.isEmpty()){
			EstimatePriceDistribution but = butList.get(0);
			but.setOpManagerId(Integer.valueOf(psf.getFormanager()));
			but.setCreateDate(new Date());
			but.setUpdateDate(new Date());
			but.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			but.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			estimatePriceDistributionService.update(but);
		}else{
			EstimatePriceDistribution but = new EstimatePriceDistribution();
			but.setEstimateBaseId(Integer.valueOf(epbi.getId().toString()));
			but.setOpManagerId(Integer.valueOf(psf.getFormanager()));
			but.setType(Context.TYPE_OP_DAN_TUAN); // 分类为地接计调
			but.setDelFlag(Context.DEL_FLAG_NORMAL);
			but.setCreateDate(new Date());
			but.setUpdateDate(new Date());
			but.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			but.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			estimatePriceDistributionService.save(but);
		}
		
		// 接待社询价内容
		EstimatePriceAdmitRequirements epar = epp.getLastAdmitRequirements();
		
		if(epar == null || ProjectSecondForm.onceAgainYes==psf.getOnceAgain() ){
			epar = new EstimatePriceAdmitRequirements();
			epar.setPid(pid);
			epar.setStatus(EstimatePriceAdmitRequirements.STATUS_DRAFT);
			epar.setCreateTime(date);
			
			isadd = true;
		}else{
			isadd = false;
		}
		
		epar.setDgroupOutDate(DateUtils.dateFormat(psf.getDgroupOutDate(), "yyyy-MM-dd"));
		if(psf.getOutAreaId()!=null&&psf.getOutAreaId()>0){
			epar.setOutAreaId(psf.getOutAreaId());
			epar.setOutAreaName(psf.getOutAreaName());
		}else{
			epar.setOutAreaId(null);
			epar.setOutAreaName(null);
		}
		if(psf.getTravelCountryId()!=null && psf.getTravelCountryId().length>0){
			epar.setTravelCountryId(JSONArray.fromObject(psf.getTravelCountryId()).toString());
		}
		if(psf.getTravelCountry()!=null && psf.getTravelCountry().length>0){
			epar.setTravelCountry(JSONArray.fromObject(psf.getTravelCountry()).toString());
		}
		epar.setOutsideDaySum(psf.getOutsideDaySum());
		epar.setOutsideNightSum(psf.getOutsideNightSum());
		
		JSONObject json = JSONObject.fromObject(replaceEnter(psf.getTravelRequirements()));
		JSONObject travelTeam = json.getJSONObject("travelTeamType");
		
		for(int num = 0;num<travelTeam.size();num++){
			String optName = "option_"+num;
			// 循环判断对应组是否被选中
			if(optName!=null){
				JSONObject option = travelTeam.getJSONObject(optName);
				
				if(option!=null && option.get("type")!=null){
					epar.setTravelTeamType(option.getInt("type"));
				}
				
				try {
					epar.setTravelTeamTypeName(epar.getTravelTeamTypeName()+","+URLDecoder.decode(option.getString("title"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					map.put("res", "data_error");
					map.put("mes", "travelTeamTypeName decode  error");
					return map;
				}
			}
		}
		
		
		epar.setTravelRequirements(json.toString());
		
		epar.setModifyTime(date);
		
		if(isadd){
			estimatePriceAdmitRequirementsService.save(epar);
		}else{
			estimatePriceAdmitRequirementsService.update(epar);
			//删除原来admit_lines_area记录
			estimatePriceAdmitLinesAreaDao.deleteByAdmitId(epar.getId());
		}
		
		//新增admit_lines_area记录
		if(psf.getTravelCountryId()!=null && psf.getTravelCountryId().length>0){
			Long[] countryId = psf.getTravelCountryId();
			for(int i=0;i<countryId.length;i++){
				estimatePriceAdmitLinesAreaDao.save(epar.getId(),countryId[i]);
			}
		}
		
		// 上传文件变为了数组
		Long[] epfid = psf.getSalerTripFileId();
		String[] epfName = psf.getSalerTripFileName();
		String[] epfPath = psf.getSalerTipFilePath();
		if(epfid!=null&& epfid.length>0 && epfName!=null && epfName.length>0 && epfPath!=null && epfPath.length>0){
			for(int n=0;n<epfid.length;n++){
				DocInfo docInfo = docInfoDao.findOne(epfid[n]);
				EstimatePriceFile epf = new EstimatePriceFile();
				epf.setPid(epar.getId());
				epf.setDocInfoId(docInfo.getId());
				epf.setPtype(1); //type=1 表示为销售附件
				epf.setType(1);
				epf.setPath(docInfo.getDocPath());
				epf.setFileName(docInfo.getDocName());
				epf.setUserId(epp.getUserId());
				epf.setUserName(epp.getUserName());
				// 获取上传文件的扩展名
				String name  = docInfo.getDocName();
				if(name!=null && name.indexOf(".")>0){
					Integer m = name.indexOf(".");
					String ext = name.substring(m, name.length());
					epf.setExt(ext);
				}
				epf.setStatus(EstimatePriceFile.STATUS_NORMAL);
				epf.setCreateTime(new Date());
				epf.setModifyTime(new Date());
				estimatePriceFileService.save(epf);
			}
		}
		epp.setLastAdmitRequirements(epar);
		epp.setModifyTime(date);
		
		EstimatePriceBaseInfo info = new EstimatePriceBaseInfo(epbi);
		estimatePriceBaseInfoService.update(epbi);
		epp.setLastBaseInfo(info);
		this.save(epp);//修改
		map.put("model", epp);
		return map;
	}
	
	private String replaceEnter(String srcStr)
	{
	    if(null == srcStr)
	    {
	        return null;
	    }
	    return srcStr.replaceAll("\n", "<br>");
	}


	
	public Map<String, Object> addProjectThird(ProjectThirdForm ptf, User loginUser) {

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		boolean isAppFlight = ptf.getIsAppFlight();
		
		Long pid = ptf.getProjectId();
		EstimatePriceProject epp = this.findById(pid);
		if(epp==null){
			map.put("res", "data_error");
			map.put("mes", "pid is error");
			return map;
		}
		
		Date date = new Date();
		String startCity = null;
		String endCity = null;
		Date lastToperatorStartOutTime = null;
		//申请机票询价
		if(isAppFlight){

			startCity =  ptf.getStartCityName()[0];		// 机票计调出发城市
			endCity = new String();
			if(ptf.getEndCityName()!=null && ptf.getEndCityName().length>0){
				String end = ptf.getEndCityName()[ptf.getEndCityName().length-1];
				// 如果多段中首段出发地和末段抵达地相同，则目的地改为末段出发地。
				endCity=end.equals(startCity)?ptf.getStartCityName()[ptf.getStartCityName().length-1]:end;
			}
			
			//  机票计调目的地城市
			lastToperatorStartOutTime = DateUtils.dateFormat(ptf.getStartDate()[0],"yyyy-MM-dd");		// 机票计调出发时间
			
			//EstimatePriceTrafficRequirements eptr = new EstimatePriceTrafficRequirements();
			EstimatePriceTrafficRequirements eptr = epp.getLastTrafficRequirements();
			if(eptr==null||ProjectSecondForm.onceAgainYes==ptf.getOnceAgain()){
				eptr = new EstimatePriceTrafficRequirements();
			} 
			
			eptr.setPid(pid);
			eptr.setTrafficType(ptf.getTrafficType());
			
			Integer[] nos =  ptf.getNo();
			Integer trafficLineType = ptf.getTrafficLineType();
			eptr.setTrafficLineType(trafficLineType);
			if(trafficLineType==EstimatePriceTrafficRequirements.TRAFFIC_LINE_TYPE_RETURN){//往返
				eptr.setSectionsSum(2);
			}else if(trafficLineType==EstimatePriceTrafficRequirements.TRAFFIC_LINE_TYPE_MULTISTAGE){//多段
				eptr.setSectionsSum(nos.length);
			}else{//单程
				eptr.setSectionsSum(1);
			}
			
			
			
			
			//人数数据
			eptr.setAllPersonSum(ptf.getAllPersonSum());
			eptr.setAdultSum(ptf.getAdultSum());
			eptr.setChildSum(ptf.getChildSum());
			eptr.setSpecialPersonSum(ptf.getSpecialPersonSum());
			
			eptr.setSpecialDescn(ptf.getSpecialDescn());
			
			eptr.setStatus(EstimatePriceTrafficRequirements.STATUS_NORMAL);
			
			eptr.setCreateTime(date);
			eptr.setModifyTime(date);
			estimatePriceTrafficRequirementsService.save(eptr);
			
			//线路入库
			List<EstimatePriceTrafficLine> eptlList = new ArrayList<EstimatePriceTrafficLine>();
			EstimatePriceTrafficLine t;
			Long[] startCityId = ptf.getStartCityId();
			String[] startCityName = ptf.getStartCityName();
			Long[] endCityId = ptf.getEndCityId();
			String[] endCityName = ptf.getEndCityName();
			String[] startDate = ptf.getStartDate();
			String[] startTime1 = ptf.getStartTime1();
			String[] startTime2 = ptf.getStartTime2();
			Integer[] startTimeType = ptf.getStartTimeType();
			Integer[] areaType = ptf.getAreaType();
			Integer[] aircraftSpaceLevel = ptf.getAircraftSpaceLevel();
			String[]  aircraftSpace = ptf.getAircraftSpace();
			for (int i = 0; i < nos.length; i++) {
				t =  new EstimatePriceTrafficLine();
				t.setPfid(eptr.getId());
				t.setNo(nos[i]);
				t.setStartCityId(startCityId[i]);
				if(startCityName!=null){
					t.setStartCityName(startCityName[i]);
				}
				
				t.setEndCityId(endCityId[i]);
				if(endCityName!=null){
					t.setEndCityName(endCityName[i]);
				}
				
				String sd = startDate[i];
				if(sd!=null && !"".equals(sd)){
					t.setStartDate(DateUtils.dateFormat(sd,"yyyy-MM-dd"));
				}
				
				t.setStartTime1(startTime1[i]);
				t.setStartTime2(startTime2[i]);				
				t.setStartTimeType(startTimeType[i]);
				
				t.setAreaType(areaType[i]);
				t.setTrafficType(ptf.getTrafficType());
				t.setTrafficLineType(trafficLineType);
				
				t.setStatus(EstimatePriceTrafficLine.STATUS_NORMAL);
				
				//人数数据
				t.setAllPersonSum(ptf.getAllPersonSum());
				t.setAdultSum(ptf.getAdultSum());
				t.setChildSum(ptf.getChildSum());
				t.setSpecialPersonSum(ptf.getSpecialPersonSum());
				
				t.setAircraftSpace(aircraftSpace[i]);
				t.setAircraftSpaceLevel(aircraftSpaceLevel[i]);
				
				t.setCreateTime(date);
				t.setModifyTime(date);
				
				eptlList.add(t);
			}
			
			estimatePriceTrafficLineService.save(eptlList);
			
			epp.setLastTrafficRequirements(eptr);
		}else{
			epp.setLastTrafficRequirements(null);
		}
		
		
		
		epp.setStatus(EstimatePriceProject.STATUS_NORMAL);
		epp.getLastBaseInfo().setStatus(EstimatePriceBaseInfo.STATUS_NORMAL);
		if(EstimatePriceProject.TYPE_FLIGHT!=epp.getType()){
			epp.getLastAdmitRequirements().setStatus(EstimatePriceAdmitRequirements.STATUS_NORMAL);
		}
		
		EstimatePriceBaseInfo info = epp.getLastBaseInfo();
		info.setToperatorUserJson(JSONArray.fromObject(ptf.getToperatorUserId()).toString());
		epp.setLastBaseInfo(info);
		this.save(epp);//修改
		
		map = estimatePriceRecordService.addEPriceRecord(epp, loginUser);
		if("success".equals(map.get("res"))){
			EstimatePriceRecord epr = (EstimatePriceRecord)map.get("model");
			// 将页面需要的 “出发城市，抵达城市，机票出发时间”这三项放入EstimatePriceRecord 对象
			epr.setLastToperatorStartOutTime(lastToperatorStartOutTime);
			epr.setStartCity(startCity);
			epr.setEndCity(endCity);
			
			epp.setLastRecordId(epr.getId());
			int sum = epp.getEstimatePriceSum()==null?0:epp.getEstimatePriceSum();
			// 询价次数加一
			epp.setEstimatePriceSum(sum+1);
			
			this.save(epp);
			
			map.put("model", epp);
			map.put("epriceRecord", epr);
			
			
			//询价记录日志
			StringBuffer content =new StringBuffer("销售员:(ID "+epp.getUserId()+")"+epp.getUserName());
			if(sum!=0){
				content.append(" 再次询价");
			}else{
				content.append(" 新增询价");
			}

			content.append("项目编号(estimate_eprice_record.id)：" +epr.getId());
			//content +="  (estimate_eprice_record.id:"	+epr.getId()+")";
			content.append(", 询价客户为 "+epp.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(epp.getType())+"询价。");
			content.append(" 客户预算："+EstimatePriceUtils.backBudgetType(epp.getLastBaseInfo().getBudgetType())+", ");
			content.append("预算金额："+epp.getLastBaseInfo().getBudget()+"， ");
			content.append("预算币种：人民币， ");
			content.append("备注："+epp.getLastBaseInfo().getBudgetRemark()+"， ");
			content.append("申请总人数："+epp.getLastBaseInfo().getAllPersonSum()+"， ");
			content.append("申请成人数："+epp.getLastBaseInfo().getAdultSum()+"， ");
			content.append("申请儿童数："+epp.getLastBaseInfo().getChildSum()+"， ");
			content.append("申请特殊人群数："+epp.getLastBaseInfo().getSpecialPersonSum()+"， ");
			content.append("特殊人群备注："+epp.getLastBaseInfo().getSpecialRemark()+"。 ");		
			content.append(" 操作人："+UserUtils.getUser().getName());
			content.append(", 操作时间："+ new Date());		
			
			logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name, 
					content.toString(), Context.log_state_add, null, null);
		}
		return map;
	}
	
	/**
	 * 20150902 新增，由计调主管分配计调的询价流程
	 * 第三步表单提交
	 * 
	 */
	public Map<String, Object> addProjectForManagerThird(ProjectThirdForManagerForm ptf, User loginUser) {

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		boolean isAppFlight = ptf.getIsAppFlight();
		
		Long pid = ptf.getProjectId();
		EstimatePriceProject epp = this.findById(pid);
		if(epp==null){
			map.put("res", "data_error");
			map.put("mes", "pid is error");
			return map;
		}
		
		Date date = new Date();
		String startCity = null;
		String endCity = null;
		Date lastToperatorStartOutTime = null;

		// 询价基本信息
		EstimatePriceBaseInfo epbi = epp.getLastBaseInfo();
		//申请机票询价
		if(isAppFlight){
			epbi.setFormanager(Context.FOR_MANAGER_YES);// 设置为计调主管分配计调
			estimatePriceBaseInfoService.update(epbi);
			// 添加机票计调主管
			EstimatePriceDistributionQuery query = new EstimatePriceDistributionQuery();
			query.setEstimateBaseId(Integer.valueOf(epbi.getId().toString()));
			query.setType(Context.TYPE_OP_AIR);// 分类为机票计调
			query.setDelFlag(Context.DEL_FLAG_NORMAL);
			List<EstimatePriceDistribution> butList = estimatePriceDistributionService.find(query);
			if(butList!=null && !butList.isEmpty()){
				EstimatePriceDistribution but = butList.get(0);
				but.setOpManagerId(Integer.valueOf(ptf.getFormanager()));
				but.setCreateDate(new Date());
				but.setUpdateDate(new Date());
				but.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
				but.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
				estimatePriceDistributionService.update(but);
			}else{
				EstimatePriceDistribution but = new EstimatePriceDistribution();
				but.setEstimateBaseId(Integer.valueOf(epbi.getId().toString()));
				but.setOpManagerId(Integer.valueOf(ptf.getFormanager()));
				but.setType(Context.TYPE_OP_AIR); // 分类为机票计调
				but.setDelFlag(Context.DEL_FLAG_NORMAL);
				but.setCreateDate(new Date());
				but.setUpdateDate(new Date());
				but.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
				but.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
				estimatePriceDistributionService.save(but);
			}
			
			startCity =  ptf.getStartCityName()[0];		// 机票计调出发城市
			endCity = new String();
			if(ptf.getEndCityName()!=null && ptf.getEndCityName().length>0){
				String end = ptf.getEndCityName()[ptf.getEndCityName().length-1];
				// 如果多段中首段出发地和末段抵达地相同，则目的地改为末段出发地。
				endCity=end.equals(startCity)?ptf.getStartCityName()[ptf.getStartCityName().length-1]:end;
			}
			
			//  机票计调目的地城市
			lastToperatorStartOutTime = DateUtils.dateFormat(ptf.getStartDate()[0],"yyyy-MM-dd");		// 机票计调出发时间
			
			//EstimatePriceTrafficRequirements eptr = new EstimatePriceTrafficRequirements();
			EstimatePriceTrafficRequirements eptr = epp.getLastTrafficRequirements();
			if(eptr==null||ProjectSecondForm.onceAgainYes==ptf.getOnceAgain()){
				eptr = new EstimatePriceTrafficRequirements();
			} 
			
			eptr.setPid(pid);
			eptr.setTrafficType(ptf.getTrafficType());
			
			Integer[] nos =  ptf.getNo();
			Integer trafficLineType = ptf.getTrafficLineType();
			eptr.setTrafficLineType(trafficLineType);
			if(trafficLineType==EstimatePriceTrafficRequirements.TRAFFIC_LINE_TYPE_RETURN){//往返
				eptr.setSectionsSum(2);
			}else if(trafficLineType==EstimatePriceTrafficRequirements.TRAFFIC_LINE_TYPE_MULTISTAGE){//多段
				eptr.setSectionsSum(nos.length);
			}else{//单程
				eptr.setSectionsSum(1);
			}
			
			//人数数据
			eptr.setAllPersonSum(ptf.getAllPersonSum());
			eptr.setAdultSum(ptf.getAdultSum());
			eptr.setChildSum(ptf.getChildSum());
			eptr.setSpecialPersonSum(ptf.getSpecialPersonSum());
			
			eptr.setSpecialDescn(ptf.getSpecialDescn());
			
			eptr.setStatus(EstimatePriceTrafficRequirements.STATUS_NORMAL);
			
			eptr.setCreateTime(date);
			eptr.setModifyTime(date);
			estimatePriceTrafficRequirementsService.save(eptr);
			
			//线路入库
			List<EstimatePriceTrafficLine> eptlList = new ArrayList<EstimatePriceTrafficLine>();
			EstimatePriceTrafficLine t;
			Long[] startCityId = ptf.getStartCityId();
			String[] startCityName = ptf.getStartCityName();
			Long[] endCityId = ptf.getEndCityId();
			String[] endCityName = ptf.getEndCityName();
			String[] startDate = ptf.getStartDate();
			String[] startTime1 = ptf.getStartTime1();
			String[] startTime2 = ptf.getStartTime2();
			Integer[] startTimeType = ptf.getStartTimeType();
			Integer[] areaType = ptf.getAreaType();
			Integer[] aircraftSpaceLevel = ptf.getAircraftSpaceLevel();
			String[]  aircraftSpace = ptf.getAircraftSpace();
			for (int i = 0; i < nos.length; i++) {
				t =  new EstimatePriceTrafficLine();
				t.setPfid(eptr.getId());
				t.setNo(nos[i]);
				t.setStartCityId(startCityId[i]);
				if(startCityName!=null){
					t.setStartCityName(startCityName[i]);
				}
				
				t.setEndCityId(endCityId[i]);
				if(endCityName!=null){
					t.setEndCityName(endCityName[i]);
				}
				
				String sd = startDate[i];
				if(sd!=null && !"".equals(sd)){
					t.setStartDate(DateUtils.dateFormat(sd,"yyyy-MM-dd"));
				}
				
				t.setStartTime1(startTime1[i]);
				t.setStartTime2(startTime2[i]);				
				t.setStartTimeType(startTimeType[i]);
				
				t.setAreaType(areaType[i]);
				t.setTrafficType(ptf.getTrafficType());
				t.setTrafficLineType(trafficLineType);
				
				t.setStatus(EstimatePriceTrafficLine.STATUS_NORMAL);
				
				//人数数据
				t.setAllPersonSum(ptf.getAllPersonSum());
				t.setAdultSum(ptf.getAdultSum());
				t.setChildSum(ptf.getChildSum());
				t.setSpecialPersonSum(ptf.getSpecialPersonSum());
				
				t.setAircraftSpace(aircraftSpace[i]);
				t.setAircraftSpaceLevel(aircraftSpaceLevel[i]);
				
				t.setCreateTime(date);
				t.setModifyTime(date);
				
				eptlList.add(t);
			}
			
			estimatePriceTrafficLineService.save(eptlList);
			
			epp.setLastTrafficRequirements(eptr);
		}else{
			epp.setLastTrafficRequirements(null);
		}
		
		epp.setStatus(EstimatePriceProject.STATUS_NORMAL);
		epp.getLastBaseInfo().setStatus(EstimatePriceBaseInfo.STATUS_NORMAL);
		if(EstimatePriceProject.TYPE_FLIGHT!=epp.getType()){
			epp.getLastAdmitRequirements().setStatus(EstimatePriceAdmitRequirements.STATUS_NORMAL);
		}
		
		// 切换为计调主管分配计调
		map = estimatePriceRecordService.addEPriceForManagerRecord(epp, loginUser);
		if("success".equals(map.get("res"))){
			EstimatePriceRecord epr = (EstimatePriceRecord)map.get("model");
			// 将页面需要的 “出发城市，抵达城市，机票出发时间”这三项放入EstimatePriceRecord 对象
			epr.setLastToperatorStartOutTime(lastToperatorStartOutTime);
			epr.setStartCity(startCity);
			epr.setEndCity(endCity);
			
			epp.setLastRecordId(epr.getId());
			int sum = epp.getEstimatePriceSum()==null?0:epp.getEstimatePriceSum();
			// 询价次数加一
			epp.setEstimatePriceSum(sum+1);
			this.save(epp);
			
			map.put("model", epp);
			map.put("epriceRecord", epr);
			
			
			//询价记录日志
			StringBuffer content =new StringBuffer("销售员:(ID "+epp.getUserId()+")"+epp.getUserName());
			if(sum!=0){
				content.append(" 再次询价");
			}else{
				content.append(" 新增询价");
			}

			content.append("项目编号(estimate_eprice_record.id)：" +epr.getId());
			//content +="  (estimate_eprice_record.id:"	+epr.getId()+")";
			content.append(", 询价客户为 "+epp.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(epp.getType())+"询价。");
			content.append(" 客户预算："+EstimatePriceUtils.backBudgetType(epp.getLastBaseInfo().getBudgetType())+", ");
			content.append("预算金额："+epp.getLastBaseInfo().getBudget()+"， ");
			content.append("预算币种：人民币， ");
			content.append("备注："+epp.getLastBaseInfo().getBudgetRemark()+"， ");
			content.append("申请总人数："+epp.getLastBaseInfo().getAllPersonSum()+"， ");
			content.append("申请成人数："+epp.getLastBaseInfo().getAdultSum()+"， ");
			content.append("申请儿童数："+epp.getLastBaseInfo().getChildSum()+"， ");
			content.append("申请特殊人群数："+epp.getLastBaseInfo().getSpecialPersonSum()+"， ");
			content.append("特殊人群备注："+epp.getLastBaseInfo().getSpecialRemark()+"。 ");		
			content.append(" 操作人："+UserUtils.getUser().getName());
			content.append(", 操作时间："+ new Date());		
			
			logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name, 
					content.toString(), Context.log_state_add, null, null);
		}
		return map;
	}
	
	public Page<EstimatePriceProject> findByPage(int pageSize, int pageNo,String keyword, Long salerId, Long operatorUserId,
			Integer estimateStatus, Integer type, Long travelCountryId,String startGroupOutDate, String endGroupOutDate,
			String ePriceStartDate,String ePriceEndDate,
			Map<String,Integer> map, DepartmentCommon common,ListSearchForm lsf) {
		String format = "yyy-MM-dd";
		Date datestart = null;
		Date dateend = null;
		Date ePriceDateStart = null;
		Date ePriceDateEnd = null;
		format =  "yyy-MM-dd HH:mm:ss";
		if(!StringUtils.isBlank(startGroupOutDate)){
			startGroupOutDate += " 00:00:00";
			datestart=DateUtils.dateFormat(startGroupOutDate, format);
		}
		if(!StringUtils.isBlank(startGroupOutDate)){
			endGroupOutDate += " 23:59:59";
			dateend = DateUtils.dateFormat(endGroupOutDate, format);
		}
		if(!StringUtils.isBlank(ePriceStartDate)){
			ePriceStartDate += " 00:00:00";
			ePriceDateStart=DateUtils.dateFormat(ePriceStartDate, format);
		}
		if(!StringUtils.isBlank(ePriceEndDate)){
			ePriceEndDate += " 23:59:59";
			ePriceDateEnd = DateUtils.dateFormat(ePriceEndDate, format);
		}
		
		String deptHql = getSalerUsers(UserUtils.getUser().getCompany().getId(), common);
		
		// 判断 单团 type=1  type=7：机票
		if("1".equals(lsf.getReplayType())){ // 单团
			return estimatePriceProjectDao.findByPage(pageSize, pageNo, keyword, salerId, operatorUserId, estimateStatus, type, travelCountryId, 
					datestart,dateend ,ePriceDateStart,ePriceDateEnd, map, deptHql);
		}else if("7".equals(lsf.getReplayType())){	// 机票
			return estimatePriceProjectDao.findByPage(pageSize, pageNo, keyword, salerId, operatorUserId, estimateStatus,  travelCountryId, 
					datestart,dateend ,ePriceDateStart,ePriceDateEnd, map, deptHql,lsf);
		}else{
			return estimatePriceProjectDao.findByPage(pageSize, pageNo, keyword, salerId, operatorUserId, estimateStatus, type, travelCountryId, 
					datestart,dateend , ePriceDateStart, ePriceDateEnd, map, deptHql);
		}
	}

	/**
	 * 获取当前用户所在部门下所有子部门销售用户
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	private String getSalerUsers(Long companyId, DepartmentCommon common) {
		StringBuffer sqlWhere = new StringBuffer("");
		//只判断销售询价模块
		if("eprice:list:eprices".equals(common.getPermission())) {
			String departmentId = common.getDepartmentId();
			List<User> userList = Lists.newArrayList();
			List<Role> roleList = UserUtils.getUser().getRoleList();
			//获取当前用户所在部门下所有人员包括自己部门下人员
			if(StringUtils.isNotBlank(departmentId)) {
				for(Role role : roleList) {
		        	String type = role.getRoleType();
		        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
		        		if(common.getIsParentsAndChildren() || 
		        				(role.getDepartment() != null && role.getDepartment().getId().toString().equals(departmentId))) {
		        			userList = systemService.getAllUserByDepartment(companyId, Long.parseLong(departmentId), "%," + departmentId + ",%");
		        			break;
		        		} 
		        	} else {
		        		boolean isContains = false;
		        		//因为一个销售用户可能同属于两个部门
		        		if(CollectionUtils.isNotEmpty(userList)) {
		        			for(User user : userList) {
		        				if(user.getId() == UserUtils.getUser().getId()) {
		        					isContains = true;
		        					break;
		        				}
		        			}
		        		}
		        		if(!isContains) {
		        			userList.add(UserUtils.getUser());
		        		}
		        	}
				}
			} else {
				//获取部门下销售人员：自己部门和子级部门销售
		    	for(Role role : roleList) {
		        	String type = role.getRoleType();
		        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
		        		userList = systemService.getUserByCompany(UserUtils.getUser().getCompany());
		        		break;
		        	} else {
		        		boolean isContains = false;
		        		//因为一个销售用户可能同属于两个部门
		        		if(CollectionUtils.isNotEmpty(userList)) {
		        			for(User user : userList) {
		        				if(user.getId() == UserUtils.getUser().getId()) {
		        					isContains = true;
		        					break;
		        				}
		        			}
		        		}
		        		if(!isContains) {
		        			userList.add(UserUtils.getUser());
		        		}
		        	}
				}
			}
			//获取部门下销售人员：自己部门和子级部门销售
			if(CollectionUtils.isNotEmpty(userList)) {
				Iterator<User> it = userList.iterator();
				while(it.hasNext()) {
					User user = it.next();
					List<Role> roleLists = user.getRoleList();
					boolean isSaler = false;
					for(Role role : roleLists) {
						if(Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
							isSaler = true;
							break;
						}
					}
					if(!isSaler) {
						it.remove();
					}
				}
			} 
			
			if(CollectionUtils.isNotEmpty(userList)) {
				String createByIds = "";
				Iterator<User> it = userList.iterator();
				int i = 0;
				while(it.hasNext()) {
					if(i == userList.size() - 1) {
						createByIds += it.next().getId();
					} else {
						createByIds += it.next().getId() + ",";
					}
					i++;
				}
				if(StringUtils.isNotBlank(createByIds)) {
					createByIds +=","+UserUtils.getUser().getId();
					sqlWhere.append(" AND p.userId in (" + createByIds  + ")");
				}
			} else {
					sqlWhere.append(" AND 1=2 ");
			}
		}
		return sqlWhere.toString();
	}
	
	/**
	 * 获取当前用户所在部门下所有子部门销售用户
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	private String getSalerUsersSql(Long companyId, DepartmentCommon common) {
		StringBuffer sqlWhere = new StringBuffer("");
		//只判断销售询价模块
		if("eprice:list:eprices".equals(common.getPermission())) {
			String departmentId = common.getDepartmentId();
			List<User> userList = Lists.newArrayList();
			List<Role> roleList = UserUtils.getUser().getRoleList();
			//获取当前用户所在部门下所有人员包括自己部门下人员
			if(StringUtils.isNotBlank(departmentId)) {
				for(Role role : roleList) {
		        	String type = role.getRoleType();
		        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
		        		if(common.getIsParentsAndChildren() || 
		        				(role.getDepartment() != null && role.getDepartment().getId().toString().equals(departmentId))) {
		        			userList = systemService.getAllUserByDepartment(companyId, Long.parseLong(departmentId), "%," + departmentId + ",%");
		        			break;
		        		} 
		        	} else {
		        		boolean isContains = false;
		        		//因为一个销售用户可能同属于两个部门
		        		if(CollectionUtils.isNotEmpty(userList)) {
		        			for(User user : userList) {
		        				if(user.getId() == UserUtils.getUser().getId()) {
		        					isContains = true;
		        					break;
		        				}
		        			}
		        		}
		        		if(!isContains) {
		        			userList.add(UserUtils.getUser());
		        		}
		        	}
				}
			} else {
				//获取部门下销售人员：自己部门和子级部门销售
		    	for(Role role : roleList) {
		        	String type = role.getRoleType();
		        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
		        		userList = systemService.getUserByCompany(UserUtils.getUser().getCompany());
		        		break;
		        	} else {
		        		boolean isContains = false;
		        		//因为一个销售用户可能同属于两个部门
		        		if(CollectionUtils.isNotEmpty(userList)) {
		        			for(User user : userList) {
		        				if(user.getId() == UserUtils.getUser().getId()) {
		        					isContains = true;
		        					break;
		        				}
		        			}
		        		}
		        		if(!isContains) {
		        			userList.add(UserUtils.getUser());
		        		}
		        	}
				}
			}
			//获取部门下销售人员：自己部门和子级部门销售
			if(CollectionUtils.isNotEmpty(userList)) {
				Iterator<User> it = userList.iterator();
				while(it.hasNext()) {
					User user = it.next();
					List<Role> roleLists = user.getRoleList();
					boolean isSaler = false;
					for(Role role : roleLists) {
						if(Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
							isSaler = true;
							break;
						}
					}
					if(!isSaler) {
						it.remove();
					}
				}
			} 
			
			if(CollectionUtils.isNotEmpty(userList)) {
				String createByIds = "";
				Iterator<User> it = userList.iterator();
				int i = 0;
				while(it.hasNext()) {
					if(i == userList.size() - 1) {
						createByIds += it.next().getId();
					} else {
						createByIds += it.next().getId() + ",";
					}
					i++;
				}
				if(StringUtils.isNotBlank(createByIds)) {
					createByIds +=","+UserUtils.getUser().getId();
					sqlWhere.append(" AND u.user_id in (" + createByIds  + ")");
				}
			} else {
					sqlWhere.append(" AND 1=2 ");
			}
		}
		return sqlWhere.toString();
	}
	
	public Page<EstimatePriceProject> findByPage(ListSearchForm lsf,int pageSize, int pageNo, DepartmentCommon common) {
		return findByPage(pageSize, pageNo, lsf.getKeyword(), lsf.getSalerId(), lsf.getOperatorUid(), lsf.getEstimateStatus(), lsf.getType(), 
				lsf.getTravelCountryId(), lsf.getGroupOpenDate(), lsf.getGroupCloseDate(),lsf.getEpriceStartDate(),lsf.getEpriceEndDate(), lsf.getSorts(), common,lsf);
	}


	
	public Map<String, Object> replayopView(Long rid,User loginUser,String types) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		EstimatePriceRecord record = estimatePriceRecordService.findById(rid);
		
		if(record==null){
			map.put("res", "noexist");
			return map;
		}else if(!record.getStatus().equals(EstimatePriceRecord.STATUS_NORMAL)){
			map.put("res", "statuserror");
			return map;
			
		}
		
		//目前暂不使用，如果使用，可能和权限管理起冲突：例如：系统管理员直接操作数据
		//当前登陆用户是否和询价记录归属同一个公司，
		if(!record.getCompanyId().equals(loginUser.getCompany().getId())){
			map.put("res", "noauth");
			return map;
		}
		
		EstimatePriceProject project = this.findById(record.getPid());
		if(project==null){
			map.put("res", "EstimatePriceProject is null  error");
			return map;
		}
		//判断rid对应的询价记录是否有机票询价
		if(record.getIsAppFlight().equals(EstimatePriceRecord.IS_APP_FLIGHT_YES) 
				|| record.getType().equals(EstimatePriceRecord.TYPE_FLIGHT)){
			List<EstimatePriceTrafficLine> lineList = estimatePriceTrafficLineService.findByPfid(record.getTrafficRequirements().getId());
			map.put("lineList", lineList);
		}
		
		EstimatePricerReply epr = estimatePricerReplyService.findReplyByRidAndOperatorUserId(rid, loginUser.getId(), types);
		//没有当前登录用户对于的计调待回复消息处理
		if(epr==null){
			map.put("res", "estimatePricerReply is null error");
			return map;
		}
		
		
		
//		接待社询价上传文件
				List<EstimatePriceFile> upFileList = null;
				String upFileDocIds ="";
				if(EstimatePriceRecord.TYPE_FLIGHT!=record.getType()){
					EstimatePriceFile example = new EstimatePriceFile();
					example.setPid(record.getAdmitRequirements().getId());
					example.setType(EstimatePriceFile.TYPE_TRIP);
					example.setPtype(EstimatePriceFile.PTYPE_ADMIT);
					upFileList = estimatePriceFileDao.findByExample(example);
					for(EstimatePriceFile file:upFileList){
						upFileDocIds+=","+file.getDocInfoId();
					}
					if(upFileDocIds.length()>0){
						upFileDocIds = upFileDocIds.replaceFirst(",", "");
					}
				}
				
				//接待社回复上传文件
				List<EstimatePriceFile> upRelayFileList = null;
				String upRelayFileDocIds ="";
				if(EstimatePriceRecord.TYPE_FLIGHT!=record.getType()){
					EstimatePriceFile example = new EstimatePriceFile();
					example.setPid(epr.getId());
					example.setType(EstimatePriceFile.TYPE_REPLY);
					example.setPtype(EstimatePriceFile.PTYPE_REPLY);
					upRelayFileList = estimatePriceFileDao.findByExample(example);
					for(EstimatePriceFile file:upRelayFileList){
						upRelayFileDocIds+=","+file.getDocInfoId();
					}
					if(upRelayFileDocIds.length()>0){
						upRelayFileDocIds = upRelayFileDocIds.replaceFirst(",", "");
					}
				}
					
				
		// 指定询价项目的路线列表
//		List<EstimatePriceTrafficLine> eptlList = estimatePriceTrafficLineService.findByPfid(record.getTrafficRequirements().getId());
//		if(eptlList!=null && eptlList.size()>0 && eptlList.get(0)!=null){
//			map.put("res", "List<EstimatePriceTrafficLine> is null,  error");
//			return map;
//		}
		map.put("upFileList", upFileList);
		map.put("upFileDocIds", upFileDocIds);	
		map.put("upRelayFileList", upRelayFileList);
		map.put("upRelayFileDocIds", upRelayFileDocIds);	
		map.put("reply", epr);
		map.put("record", record);
		map.put("project", project);
		//map.put("eptlList", eptlList);
		
		return map;
	}


	
	@SuppressWarnings("unused")
    public Map<String, Object> erlistMore(Long pid) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		EstimatePriceProject p = this.findById(pid);
		
		if(p==null){
			map.put("res", "noexist");
			return map;
		}
		
		//检查是否被逻辑删除
		if(p.getStatus().equals(EstimatePriceProject.STATUS_DEL)){
			map.put("res", "noexist");
			return map;
		}
		
		EstimatePriceBaseInfo epbi =  p.getLastBaseInfo();
		JSONArray jaa = new JSONArray();
		if(epbi.getAoperatorUserJson()!=null&&epbi.getAoperatorUserJson().equals("")&&!epbi.getAoperatorUserJson().trim().equals("null")){
			jaa = JSONArray.fromObject(epbi.getAoperatorUserJson());
		}
		
		// 机票相关，暂时去掉
		JSONArray jat = JSONArray.fromObject(epbi.getToperatorUserJson());
		
		List<User> lista = new ArrayList<User>();
		// 机票相关，暂时去掉
		//List<User> listt = new ArrayList<User>();
		
		//分别获取接待计调用户列表和机票计调用户列表
		//这里暂时使用假数据，等新的权限组织架构好了后，再使用真实数据
		for(int i=0,len=jaa.size();i<len;i++){
			User u = new User();
			u.setId(jaa.getLong(i));
			u.setName("测试数据"+i);
			lista.add(u);
		}
		// 机票相关，暂时去掉
//		for(int i=0,len=jat.size();i<len;i++){
//			User u = new User();
//			u.setId(jat.getLong(i));
//			u.setName("测试数据"+i);
//			listt.add(u);
//		}
		map.put("project", p);
		map.put("lista", lista);
		// 机票相关，暂时去掉
//		map.put("listt", listt);
		return map;
	}


	@Override
	public Page<EstimatePriceProject> findListByPage(ListSearchForm lsf,
			Integer pageSize, Integer pageNo, DepartmentCommon common) {
		// TODO Auto-generated method stub
		return findListByPage(lsf,pageSize, pageNo, lsf.getKeyword(), lsf.getSalerId(), lsf.getOperatorUid(), lsf.getEstimateStatus(), lsf.getType(), 
				lsf.getTravelCountryId(), lsf.getSorts(), common);
	}
	
	@Override
	public Page<EstimatePriceProject> findListByPageForManager(ListSearchForm lsf, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		
		return findListByPageForManager(lsf,pageSize, pageNo, lsf.getKeyword(), lsf.getSalerId(), lsf.getOperatorUid(), lsf.getEstimateStatus(), lsf.getType(), 
				lsf.getTravelCountryId(), lsf.getSorts());
	}


	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
    private Page<EstimatePriceProject> findListByPage(ListSearchForm lsf,int pageSize, int pageNo,String keyword, Long salerId, Long operatorUserId,
			Integer estimateStatus, Integer type, Long travelCountryId,Map<String,Integer> map, DepartmentCommon common) {
		// TODO Auto-generated method stub
		String format = "yyy-MM-dd";
		Date datestart = null;
		Date dateend = null;
		//根据选择的条件查询询价信息
		String deptHql = getSalerUsersSql(UserUtils.getUser().getCompany().getId(), common);
		Page page = estimatePriceProjectDao.findListByPage(lsf,pageSize, pageNo, keyword, salerId, operatorUserId, estimateStatus, type, travelCountryId, 
				 map, deptHql);
			
		 List<Object[]> list = page.getResult()	;
	     List<EstimatePriceProject> listProject = new ArrayList<EstimatePriceProject>();
	     for(Object[] obj:list){
	        	EstimatePriceProject project = new EstimatePriceProject();
	        	project.setId(Long.parseLong(obj[0].toString()));
	        	project.setTitle(obj[1]==null?"":obj[1].toString());
	        	project.setUserId(Long.parseLong(obj[2]==null||obj[2].equals("")?"0":obj[2].toString()));
	        	project.setUserName(obj[3]==null?"":obj[3].toString());
	        	project.setCompanyId(Long.parseLong(obj[4]==null||obj[4].equals("")?"0":obj[4].toString()));
	        	project.setCompanyName(obj[5]==null?"":obj[5].toString());
	        	project.setLastRecordId(Long.parseLong(obj[6]==null||obj[6].equals("")?"0":obj[6].toString()));
	        	project.setLastBaseInfo(estimatePriceBaseInfoDao.findById(Long.parseLong(obj[7]==null||obj[7].equals("")?"0":obj[7].toString())));
	        	project.setLastAdmitRequirements(estimatePriceAdmitRequirementsDao.findById(Long.parseLong(obj[8]==null||obj[8].equals("")?"0":obj[8].toString())));
	        	project.setLastTrafficRequirements(estimatePriceTrafficRequirementsDao.findById(Long.parseLong(obj[9]==null||obj[9].equals("")?"0":obj[9].toString())));
	        	project.setEstimateStatus(Integer.parseInt(obj[10]==null||obj[10].equals("")?null:obj[10].toString()));
	        	project.setStatus(Integer.parseInt(obj[11]==null||obj[11].equals("")?null:obj[11].toString()));
	        	project.setEstimatePriceSum(Integer.parseInt(obj[12]==null||obj[12].equals("")?null:obj[12].toString()));
	        	project.setType(Integer.parseInt(obj[13]==null||obj[13].equals("")?null:obj[13].toString()));
	        	project.setLastEstimatePriceTime((Date)obj[14]);
	        	project.setLastOperatorGivenTime((Date)obj[15]);
	        	project.setLastCreateProductTime((Date)obj[16]);
	        	project.setLastCancelTime((Date)obj[18]);
	        	project.setCreateTime((Date)obj[19]);
	        	project.setModifyTime((Date)obj[20]);
	        	project.setRemark((String)obj[21]);
//			     "u.last_create_order_time as last_create_order_time," +//17
//			     "u.create_time as create_time ," +//19
//			     "u.modify_time as modify_time ," +//20
//			     "u.remark as remark        " +//21
	        	
	        	listProject.add(project);
	     }
	     page.setResult(listProject);
	     return page;
	
	}
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
    private Page<EstimatePriceProject> findListByPageForManager(ListSearchForm lsf, int pageSize, int pageNo, String keyword, Long salerId,
    		Long operatorUserId, Integer estimateStatus, Integer type, Long travelCountryId,Map<String,Integer> map) {
		// TODO Auto-generated method stub
		
		String format = "yyy-MM-dd";
		Date datestart = null;
		Date dateend = null;
		 
		Page page = estimatePriceProjectDao.findListByPageForManager(lsf,pageSize, pageNo, keyword, salerId, operatorUserId, 
				estimateStatus, type, travelCountryId, map);
			
		 List<Object[]> list = page.getResult()	;
	     List<EstimatePriceProject> listProject = Lists.newArrayList();
	     for(Object[] obj:list){
	        	EstimatePriceProject project = new EstimatePriceProject();
	        	project.setId(Long.parseLong(obj[0].toString()));
	        	project.setTitle(obj[1]==null?"":obj[1].toString());
	        	project.setUserId(Long.parseLong(obj[2]==null||obj[2].equals("")?"0":obj[2].toString()));
	        	project.setUserName(obj[3]==null?"":obj[3].toString());
	        	project.setCompanyId(Long.parseLong(obj[4]==null||obj[4].equals("")?"0":obj[4].toString()));
	        	project.setCompanyName(obj[5]==null?"":obj[5].toString());
	        	project.setLastRecordId(Long.parseLong(obj[6]==null||obj[6].equals("")?"0":obj[6].toString()));
	        	project.setLastBaseInfo(estimatePriceBaseInfoDao.findById(Long.parseLong(obj[7]==null||obj[7].equals("")?"0":obj[7].toString())));
	        	project.setLastAdmitRequirements(estimatePriceAdmitRequirementsDao.findById(Long.parseLong(obj[8]==null||obj[8].equals("")?"0":obj[8].toString())));
	        	project.setLastTrafficRequirements(estimatePriceTrafficRequirementsDao.findById(Long.parseLong(obj[9]==null||obj[9].equals("")?"0":obj[9].toString())));
	        	project.setEstimateStatus(Integer.parseInt(obj[10]==null||obj[10].equals("")?null:obj[10].toString()));
	        	project.setStatus(Integer.parseInt(obj[11]==null||obj[11].equals("")?null:obj[11].toString()));
	        	project.setEstimatePriceSum(Integer.parseInt(obj[12]==null||obj[12].equals("")?null:obj[12].toString()));
	        	project.setType(Integer.parseInt(obj[13]==null||obj[13].equals("")?null:obj[13].toString()));
	        	project.setLastEstimatePriceTime((Date)obj[14]);
	        	project.setLastOperatorGivenTime((Date)obj[15]);
	        	project.setLastCreateProductTime((Date)obj[16]);
	        	project.setLastCancelTime((Date)obj[18]);
	        	project.setCreateTime((Date)obj[19]);
	        	project.setModifyTime((Date)obj[20]);
	        	project.setRemark((String)obj[21]);
	        	
//	        	if (project.getEstimateStatus() == 1 && obj[22] == null) {
//	        		project.setEstimateStatus(6);
//	        	}
	        	
	        	listProject.add(project);
	     }
	     page.setResult(listProject);
	     return page;
	}
	
}
