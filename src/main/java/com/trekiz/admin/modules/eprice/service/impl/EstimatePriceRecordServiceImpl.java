package com.trekiz.admin.modules.eprice.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.eprice.entity.EstimateMoneyView;
import com.trekiz.admin.modules.eprice.entity.EstimatePrice;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;
import com.trekiz.admin.modules.eprice.entity.EstimatePricePartReply;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ReplyEPrice4AdmitForm;
import com.trekiz.admin.modules.eprice.repository.EstimateMoneyViewDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitLinesAreaDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitRequirementsDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceBaseInfoDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceFileDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceProjectDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceRecordDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceTrafficRequirementsDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePricerReplyDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceFileService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficLineService;
import com.trekiz.admin.modules.eprice.service.EstimatePricerReplyService;
import com.trekiz.admin.modules.eprice.utils.EstimatePriceUtils;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service("estimatePriceRecordService")
@Transactional(readOnly = true)
public class EstimatePriceRecordServiceImpl implements EstimatePriceRecordService {

	@Autowired
	private EstimatePriceRecordDao estimatePriceRecordDao;
	
	@Autowired
	private EstimatePriceProjectDao estimatePriceDao;
	
	@Resource
	private EstimatePricerReplyService estimatePricerReplyService;
	
	@Resource
	private EstimatePriceTrafficLineService estimatePriceTrafficLineService;
	
	@Resource
	private EstimatePriceFileService  estimatePriceFileService;
	
	@Autowired
	private EstimatePriceProjectDao estimatePriceProjectDao;
	
	@Autowired
	private EstimatePriceAdmitRequirementsDao estimatePriceAdmitRequirementsDao;
	
	@Autowired
	private EstimatePriceTrafficRequirementsDao estimatePriceTrafficRequirementsDao;
	
	@Autowired
	private EstimatePriceBaseInfoDao estimatePriceBaseInfoDao;
	
	@Autowired
	private EstimatePriceFileDao estimatePriceFileDao;
	
	@Autowired
	private EstimatePriceAdmitLinesAreaDao estimatePriceAdmitLinesAreaDao;
	
	@Autowired
	private EstimatePricerReplyDao estimatePricerReplyDao;
	
	@Autowired
	protected LogOperateService logOpeService;
	
	@Autowired
	protected EstimateMoneyViewDao estimateMoneyViewDao;
	
	@Autowired
	private CurrencyService currencyService;
	 
	public void save(EstimatePriceRecord epr) {
		estimatePriceRecordDao.save(epr);
		estimatePriceRecordDao.getSession().flush();
	}

	public EstimatePriceRecord findById(Long id) {
		if(id==null){
			return null;
		}
		return estimatePriceRecordDao.findById(id);
	}

	public void delById(Long id) {
		EstimatePriceRecord epr = new EstimatePriceRecord();
		epr.setId(id);
		epr.setStatus(EstimatePriceRecord.STATUS_DEL);
		epr.setModifyTime(new Date());
		
		this.save(epr);
	}

	/**
	 * 选择计调，保存询价记录
	 */
	public Map<String, Object> addEPriceRecord(EstimatePriceProject epp,User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		EstimatePriceRecord epr = new EstimatePriceRecord();
		epr.setPid(epp.getId());
		epr.setUserId(loginUser.getId());
		epr.setUserName(loginUser.getName());
		epr.setCompanyId(epp.getCompanyId());
		epr.setCompanyName(epp.getCompanyName());
		
		//EstimatePriceBaseInfo info = new EstimatePriceBaseInfo(epp.getLastBaseInfo());
		EstimatePriceBaseInfo info = epp.getLastBaseInfo();
		epr.setBaseInfo(info);
		// 地接计调社 新增
		//EstimatePriceAdmitRequirements admit = new EstimatePriceAdmitRequirements(epp.getLastAdmitRequirements());
		EstimatePriceAdmitRequirements admit = epp.getLastAdmitRequirements();
		epr.setAdmitRequirements(admit);
		
		// 机票计调社，新增
		//EstimatePriceTrafficRequirements traffic = new EstimatePriceTrafficRequirements(epp.getLastTrafficRequirements());
		EstimatePriceTrafficRequirements traffic = epp.getLastTrafficRequirements();
		epr.setTrafficRequirements(traffic);
		
		epr.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_WAITING);
		//TODO 这里为创建询价第二步，但是询价记录为 正常而不是草稿。
		epr.setStatus(EstimatePriceRecord.STATUS_NORMAL);
		epr.setType(epp.getType());
		
		//是否申请机票
		if(epp.getType()==7 || epp.getLastTrafficRequirements()!=null){
			epr.setIsAppFlight(1);
		}else{
			epr.setIsAppFlight(0);
		}
		
		//被指定的接待计调成员和票务计调成员数据冗余到询价记录用户展示
		// 地接计调
		JSONArray jaa1 = new  JSONArray();
		if(epp.getType()==EstimatePriceProject.TYPE_ALONE||epp.getType()==EstimatePriceProject.TYPE_YX||
				epp.getType()==EstimatePriceProject.TYPE_DKH||epp.getType()==EstimatePriceProject.TYPE_ZYX){
			JSONArray jaa = JSONArray.fromObject(epp.getLastBaseInfo().getAoperatorUserJson());
//			System.out.println(jaa +"   "+jaa.size()+"  "+ jaa.get(0));
//			System.out.println(jaa.getString(0)=="null");
//			System.out.println(jaa.get(0).equals(null));
//			System.out.println(jaa.get(0).equals("null"));
			//TODO
			if(jaa.size()>0 && !jaa.getString(0).equals("null")){
				for (int i = 0,len = jaa.size(); i < len; i++) {
					Long userId = jaa.getLong(i);
					User u = UserUtils.getUser(userId);
					if(u==null){
						map.put("res", "data_error");
						map.put("mes", "user(userId="+userId+") is error");
						return map;
					}
					JSONObject jt = new JSONObject();
					jt.put("userId", userId);
					jt.put("userName", u.getName());
					
					jaa1.add(jt);
				}
			}
		}
		epr.setAoperatorUserJson(jaa1.toString());
		
		// 票务计调
		JSONArray jat = JSONArray.fromObject(epp.getLastBaseInfo().getToperatorUserJson());
		JSONArray jat1 = new JSONArray();
//		System.out.println(jat.get(0));
//		System.out.println(jat.get(0).equals("null"));
		//TODO
		if(jat.size()>0 && !jat.getString(0).equals("null")){
			for (int i = 0,len = jat.size(); i < len; i++) {
				Long userId = jat.getLong(i);
				User u = UserUtils.getUser(userId);
				if(u==null){
					map.put("res", "data_error");
					map.put("mes", "user(userId="+userId+") is error");
					return map;
				}
				
				JSONObject jt = new JSONObject();
				jt.put("userId", userId);
				jt.put("userName", u.getName());
				
				jat1.add(jt);
			}
		}
		epr.setToperatorUserJson(jat1.toString()); 
		
		Date date = new Date();
		
		epr.setCreateTime(date);
		epr.setModifyTime(date);
		
		if(admit!=null){
			estimatePriceAdmitRequirementsDao.save(admit);
		}
		if(traffic!=null){
			estimatePriceTrafficRequirementsDao.save(traffic);
		}
		estimatePriceBaseInfoDao.save(info);
		this.save(epr);
		
		//发送询价消息
		estimatePricerReplyService.send(epp, epr, null, null, true);
		
		map.put("model", epr);
		
		return map;
	}
	/**
	 * 20150906 新增
	 * 选择计调主管，保存询价记录
	 */
	public Map<String, Object> addEPriceForManagerRecord(EstimatePriceProject epp,User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		EstimatePriceRecord epr = new EstimatePriceRecord();
		epr.setPid(epp.getId());
		epr.setUserId(loginUser.getId());
		epr.setUserName(loginUser.getName());
		epr.setCompanyId(epp.getCompanyId());
		epr.setCompanyName(epp.getCompanyName());
		
		EstimatePriceBaseInfo info = epp.getLastBaseInfo();
		epr.setBaseInfo(info);
		// 地接计调社 新增
		EstimatePriceAdmitRequirements admit = epp.getLastAdmitRequirements();
		epr.setAdmitRequirements(admit);
		
		// 机票计调社，新增
		EstimatePriceTrafficRequirements traffic = epp.getLastTrafficRequirements();
		epr.setTrafficRequirements(traffic);
		// 设定询价记录为计调主管专用“待分配”状态
		epr.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_FORMANAGER);
		//TODO 这里为创建询价第二步，但是询价记录为 正常而不是草稿。
		epr.setStatus(EstimatePriceRecord.STATUS_NORMAL);
		epr.setType(epp.getType());
		
		//是否申请机票
		if(epp.getType()==7 || epp.getLastTrafficRequirements()!=null){
			epr.setIsAppFlight(1);
		}else{
			epr.setIsAppFlight(0);
		}
		
		Date date = new Date();
		
		epr.setCreateTime(date);
		epr.setModifyTime(date);
		
		if(admit!=null){
			estimatePriceAdmitRequirementsDao.save(admit);
		}
		if(traffic!=null){
			estimatePriceTrafficRequirementsDao.save(traffic);
		}
		estimatePriceBaseInfoDao.save(info);
		this.save(epr);
		
		
		map.put("model", epr);
		
		return map;
	}

	
	public Page<EstimatePriceRecord> findByPid(Long pid, Integer type,int pageSize,
			int pageNo, Map<String, Integer> sort, boolean isOpManager) {
		if(type!=7){
			return estimatePriceRecordDao.findByPid(pid, type,pageSize, pageNo, sort, isOpManager);	// 查询全部的单团询价记录
		}else if(type==7){
			return estimatePriceRecordDao.findByPid(pid, pageSize, pageNo, sort);			// 查询全部的机票询价记录（条件：toperatorUserJson不为空）
		}else{
			return estimatePriceRecordDao.findByPid(pid, type,pageSize, pageNo, sort, isOpManager);	// 查询全部的单团询价记录
		}
	}
	
	/**
	 * 20150515 新增，多币种报价
	 * @author gao
	 * @param replyId  EstimatePricerReply 类 ID
	 */
	public void replyPay4admit(Long replyId,String content,String[] salerTripFileId,String[] salerTripFileName,String[] salerTipFilePath,String operatorPrice){
		// 获取报价类
		EstimatePricerReply reply = estimatePricerReplyService.findById(replyId);
		if(reply==null){
			return;
		}
		reply.setStatus(EstimatePricerReply.STATUS_REPLYED);
		reply.setContent(content); // 计调回复
		reply.setOperatorPriceTime(new Date()); // 计调报价时间
		reply.setPriceDetail(operatorPrice); // 计调总计价格
		reply.setModifyTime(new Date()); // 更新时间
		// 询价回复记录保存
		estimatePricerReplyService.save(reply);
				
		if(salerTripFileId!=null && StringUtils.isNotBlank(salerTripFileId[0])){
			// 上传文件类保存询价记录id
			for(int i =0;i<salerTripFileId.length;i++){
				EstimatePriceFile epf = estimatePriceFileService.findById(Long.valueOf(salerTripFileId[i]));
				if(epf==null){
					epf = new EstimatePriceFile();
				}
				epf.setPid(Long.valueOf(replyId));
				epf.setDocInfoId(Long.valueOf(salerTripFileId[i]));
				epf.setFileName(salerTripFileName[i]);
				epf.setPath(salerTipFilePath[i]);
				epf.setPtype(EstimatePriceFile.PTYPE_REPLY);
				epf.setType(EstimatePriceFile.TYPE_REPLY);
				epf.setStatus(EstimatePriceFile.STATUS_NORMAL);
				estimatePriceFileService.save(epf);
			}
		}
		// 修改询价记录
		EstimatePriceRecord record = estimatePriceRecordDao.findById(reply.getRid());
		record.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_GIVEN); // 询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品，5 已生成订单
		record.setType(reply.getType());//'询价类型:1 单团询价  2 机票询价'
		record.setLastAoperatorPriceTime(new Date());//'最新接待计调报价时间  operator_price_time
		this.save(record);
		
		// 修改项目记录
		EstimatePriceProject project = estimatePriceDao.findById(reply.getPid());
		project.setLastOperatorGivenTime(new Date()); // 最新计调报价时间'
		if(EstimatePriceProject.ESTIMATE_STATUS_WAITING==project.getEstimateStatus()
				&&EstimatePriceRecord.IS_APP_FLIGHT_YES==record.getIsAppFlight()){
			//判断机票是否已报价
			List<EstimatePricerReply> list = estimatePriceTrafficRequirementsDao.find(" from EstimatePricerReply where pid = '"+project.getId()+
					"' and rid = "+record.getId()+" and type ='"+EstimatePricerReply.TYPE_FLIGHT+"' and status >1");
			if(list!=null&&list.size()>0){
				if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>project.getEstimateStatus()){
					project.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
				}
			}
		
			
		}else{
			if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>project.getEstimateStatus()){
				project.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
			}
		}
		 
		estimatePriceDao.save(project);
		estimatePriceDao.getSession().flush();
		
		//询价记录日志
		StringBuffer cont =new StringBuffer("地接社计调:(ID "+UserUtils.getUser().getId()+")"+UserUtils.getUser().getName());
		cont.append(", 回复销售员：(ID "+project.getUserId()+")"+project.getUserName());
		cont.append(", 询价客户为 "+project.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(project.getType())+"询价。");
		cont.append(" 客户预算："+EstimatePriceUtils.backBudgetType(project.getLastBaseInfo().getBudgetType())+", ");
		cont.append("预算金额："+project.getLastBaseInfo().getBudget()+"， ");
		cont.append("预算币种：人民币， ");
		cont.append("备注："+project.getLastBaseInfo().getBudgetRemark()+"， ");
		cont.append("申请总人数："+project.getLastBaseInfo().getAllPersonSum()+"， ");
		cont.append("申请成人数："+project.getLastBaseInfo().getAdultSum()+"， ");
		cont.append("申请儿童数："+project.getLastBaseInfo().getChildSum()+"， ");
		cont.append("申请特殊人群数："+project.getLastBaseInfo().getSpecialPersonSum()+"， ");
		cont.append("特殊人群备注："+project.getLastBaseInfo().getSpecialRemark()+"。 ");		
		logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name,
				content.toString(), Context.log_state_add, null, null);
	}
	
	// 20150515废弃
	public Map<String, Object> reply4admit(ReplyEPrice4AdmitForm reaf,User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		EstimatePriceRecord epr = this.findById(reaf.getRid());
		if(epr==null){
			map.put("res", "record_noexist");
			return map;
		}
		
		//询价项目类型end
		
		EstimatePricerReply epreply = estimatePricerReplyService.findReplyByRidAndOperatorUserId(reaf.getRid(), loginUser.getId(),String.valueOf(epr.getType()));
		if(epreply==null){
			map.put("res", "reply_noexist");
			return map;
		}
		
		epreply.setStatus(EstimatePricerReply.STATUS_REPLYED);
		epreply.setContent(reaf.getContent());
		//计调回复总价
		JSONObject json = JSONObject.fromObject(reaf.getPriceDetail());
		JSONObject adult = json.getJSONObject("adult");
		JSONObject child = json.getJSONObject("child");
		JSONObject specialPerson = json.getJSONObject("specialPerson");
		
		adult.put("sum", epr.getBaseInfo().getAdultSum());
		child.put("sum", epr.getBaseInfo().getChildSum());
		specialPerson.put("sum", epr.getBaseInfo().getSpecialPersonSum());
		
		Double adultprice =  adult.getDouble("price") ;
		Double childprice =  child.getDouble("price");
		Double specialprice =  specialPerson.getDouble("price");
		//计调回复总价计算
		//计调回复固定项总价计算
		Double operatorTotalPrice = adultprice*adult.getInt("sum")+childprice*child.getInt("sum")+specialprice*specialPerson.getInt("sum");
		// 成人/儿童/特殊人群数量
		epreply.setAdultSum(adult.getInt("sum"));
		epreply.setChildSum(child.getInt("sum"));
		epreply.setSpecialPersonSum(specialPerson.getInt("sum"));
		//计调回复固定项总价计算+其他项报价总价计算
		JSONArray ja = json.getJSONArray("other");
		JSONObject jt;
		for (int i = 0,len = ja.size(); i <len; i++) {
			jt = ja.getJSONObject(i);
			operatorTotalPrice += jt.getDouble("price")*jt.getInt("sum");
		}
		
		epreply.setOperatorTotalPrice(new BigDecimal(operatorTotalPrice));
		
		epreply.setPriceDetail(json.toString());
		epreply.setOperatorPriceTime(new Date());
		epreply.setModifyTime(new Date());
		
		
		// 询价回复记录保存
		estimatePricerReplyService.save(epreply);
		// 上传文件类保存询价记录id
		Long[] fileIds = reaf.getSalerTripFileId();
		String[] fileNames = reaf.getSalerTripFileName();
		if(fileIds!=null){
			for(int i =0;i<fileIds.length;i++){
				EstimatePriceFile epf = estimatePriceFileService.findById(fileIds[i]);
				if(epf==null){
					epf = new EstimatePriceFile();
				}
				epf.setPid(epreply.getId());
				epf.setDocInfoId(fileIds[i]);
				epf.setFileName(fileNames[i]);
				epf.setPtype(EstimatePriceFile.PTYPE_REPLY);
				epf.setType(EstimatePriceFile.TYPE_REPLY);
				epf.setStatus(EstimatePriceFile.STATUS_NORMAL);
				estimatePriceFileService.save(epf);
			}
		}
		
		// 修改询价记录
		EstimatePriceRecord record = estimatePriceRecordDao.findById(epreply.getRid());
		record.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_GIVEN); // 询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品，5 已生成订单
		record.setType(epreply.getType());//'询价类型:1 单团询价  2 机票询价'
		record.setLastAoperatorPriceTime(new Date());//'最新接待计调报价时间
		//record.setModifyTime(new Date());//'更新时间'
		this.save(record);
		
		// 修改项目记录
		EstimatePriceProject project = estimatePriceDao.findById(epreply.getPid());
		project.setLastOperatorGivenTime(new Date()); // 最新计调报价时间'
		if(EstimatePriceProject.ESTIMATE_STATUS_WAITING==project.getEstimateStatus()
				&&EstimatePriceRecord.IS_APP_FLIGHT_YES==record.getIsAppFlight()){
			//判断机票是否已报价
			List<EstimatePricerReply> list = estimatePriceTrafficRequirementsDao.find(" from EstimatePricerReply where pid = '"+project.getId()+
					"' and rid = "+record.getId()+" and type ='"+EstimatePricerReply.TYPE_FLIGHT+"' and status >1");
			if(list!=null&&list.size()>0){
				if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>project.getEstimateStatus()){
					project.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
				}
			}
		
			
		}else{
			if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>project.getEstimateStatus()){
				project.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
			}
		}
		 
		estimatePriceDao.save(project);
		estimatePriceDao.getSession().flush();
		map.put("model", epreply);
		
//		//询价记录日志
//		String content ="地接报价:询价项目编号："+project.getId()+",recordId:"+record.getId();
//		 
//
//		content +=";报价内容:总价:"+operatorTotalPrice;
//		content += "成人:"+adultprice+"x"+adult.getInt("sum")+",";
//		content += "儿童:"+childprice+"x"+child.getInt("sum")+",";
//		content += "特殊人群:"+specialprice+"x"+specialPerson.getInt("sum")+"";
//		content+=content1;
//		logOpeService.saveLogOperate(Context.log_type_price,Context.log_type_price_name,content,Context.log_state_add);

		//询价记录日志
		StringBuffer content =new StringBuffer("地接社计调:(ID "+UserUtils.getUser().getId()+")"+UserUtils.getUser().getName());
		content.append(", 回复销售员：(ID "+project.getUserId()+")"+project.getUserName());
		//content.append("项目编号(estimate_eprice_record.id)：" +record.getId());
		content.append(", 询价客户为 "+project.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(project.getType())+"询价。");
		content.append(" 客户预算："+EstimatePriceUtils.backBudgetType(project.getLastBaseInfo().getBudgetType())+", ");
		content.append("预算金额："+project.getLastBaseInfo().getBudget()+"， ");
		content.append("预算币种：人民币， ");
		content.append("备注："+project.getLastBaseInfo().getBudgetRemark()+"， ");
		content.append("申请总人数："+project.getLastBaseInfo().getAllPersonSum()+"， ");
		content.append("申请成人数："+project.getLastBaseInfo().getAdultSum()+"， ");
		content.append("申请儿童数："+project.getLastBaseInfo().getChildSum()+"， ");
		content.append("申请特殊人群数："+project.getLastBaseInfo().getSpecialPersonSum()+"， ");
		content.append("特殊人群备注："+project.getLastBaseInfo().getSpecialRemark()+"。 ");			
		
		logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name,
				content.toString(), Context.log_state_add, null, null);
		
		return map;
	}
	
	
	public Map<String, Object> recordInfoShow(Long rid, User loginUser) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		EstimatePriceRecord record = this.findById(rid);
		List<EstimatePricerReply> alist  = null;
		if(record==null){
			map.put("res", "noexist");
			return map;
		}else if(!record.getStatus().equals(EstimatePriceRecord.STATUS_NORMAL)){
			map.put("res", "statuserror");
			return map;
		}
		
		if(record.getIsAppFlight().equals(EstimatePriceRecord.IS_APP_FLIGHT_YES)){
			List<EstimatePriceTrafficLine> lineList = estimatePriceTrafficLineService.findByPfid(record.getTrafficRequirements().getId());
			map.put("lineList", lineList);
		}
		
//		estimate_price_file
		
		//接待社上传文件
		List<EstimatePriceFile> upFileList = null;
		String upFileDocIds ="";
		if(EstimatePriceRecord.TYPE_FLIGHT!=record.getType()){
			//线路国际取值修改
			
			if(record.getAdmitRequirements()!=null){
				record.getAdmitRequirements().setTravelCountry(estimatePriceAdmitLinesAreaDao.getLinesNames(record.getAdmitRequirements().getId().toString()));
			}
			
			
			upFileList = estimatePriceFileDao.findByPid(record.getAdmitRequirements().getId());
			for(EstimatePriceFile file:upFileList){
				upFileDocIds+=","+file.getDocInfoId();
			}
			if(upFileDocIds.length()>0){
				upFileDocIds = upFileDocIds.replaceFirst(",", "");
			}
			
			
			
			//接待询价回复列表
			 alist = estimatePricerReplyService.findReplyByAdmit(rid);
			 List<List<EstimatePriceFile>> repalyFilelist = new ArrayList<List<EstimatePriceFile>>();
			 List<String> reUpFileIdsList = new ArrayList<String>();
			for(EstimatePricerReply relay:alist){
				EstimatePriceFile example = new EstimatePriceFile();
				example.setPid(relay.getId());
				example.setType(EstimatePriceFile.TYPE_REPLY);
				example.setPtype(EstimatePriceFile.PTYPE_REPLY);
				List<EstimatePriceFile> fileList = estimatePriceFileDao.findByExample(example);
				String upReplyFileDocIds = "";
				// 20150515 计算地接计调回复总价
				List<EstimateMoneyView> moneyViewList =  estimateMoneyViewDao.findByReplyId(relay.getId());
				List<EstimatePrice> adult = new ArrayList<EstimatePrice>();
				List<EstimatePrice> child = new ArrayList<EstimatePrice>();
				List<EstimatePrice> special = new ArrayList<EstimatePrice>();
				if(moneyViewList!=null && !moneyViewList.isEmpty()){

					for(EstimateMoneyView view : moneyViewList){ 
						if(EstimatePricePartReply.PRICE_TYPE_ALL.equals(view.getPriceType().toString())){// 总体报价
							BigDecimal amount =view.getAmount(); // 单价
							Integer personNum = view.getPersonNum();// 人数
							BigDecimal sumPrice = amount.multiply(new BigDecimal(personNum)); // 单价*人数
							EstimatePrice est = new EstimatePrice();
							est.setAmount(amount.toString());
							est.setPersonNum(personNum);
							est.setSumPrice(sumPrice.toString());
							est.setCurrencyId(Long.valueOf(view.getCurrencyId()));
							est.setMoneyAmountId(view.getMoneyAmountID()); // 流水ID
							est.setReplyId(view.getEstimatePriceReplyId()); // 报价ID
							if(EstimatePricePartReply.PERSON_TYPE_ADULT.equals(view.getPersonType().toString())){ // 成人
								adult.add(est); // 将本项价格记录送入list
							}else if(EstimatePricePartReply.PERSON_TYPE_CHILD.equals(view.getPersonType().toString())){ // 儿童
								child.add(est); // 将本项价格记录送入list
							}else if(EstimatePricePartReply.PERSON_TYPE_SPECIAL.equals(view.getPersonType().toString())){ // 特殊人群
								special.add(est); // 将本项价格记录送入list
							}
						}else if(EstimatePricePartReply.PRICE_TYPE_PART.equals(view.getPriceType().toString())){// 细分报价
							BigDecimal amount =view.getAmount(); // 单价
							Integer personNum = view.getPersonNum();// 人数
							BigDecimal sumPrice = amount.multiply(new BigDecimal(personNum)); // 单价*人数
							EstimatePrice est = new EstimatePrice();
							est.setAmount(amount.toString());
							est.setPersonNum(personNum);
							est.setSumPrice(sumPrice.toString());
							est.setCurrencyId(Long.valueOf(view.getCurrencyId()));
							est.setMoneyAmountId(view.getMoneyAmountID()); // 流水ID
							est.setReplyId(view.getEstimatePriceReplyId()); // 报价ID
							if(EstimatePricePartReply.PERSON_TYPE_ADULT.equals(view.getPersonType().toString())){ // 成人
								adult.add(est); // 将本项价格记录送入list
							}else if(EstimatePricePartReply.PERSON_TYPE_CHILD.equals(view.getPersonType().toString())){ // 儿童
								child.add(est); // 将本项价格记录送入list
							}else if(EstimatePricePartReply.PERSON_TYPE_SPECIAL.equals(view.getPersonType().toString())){ // 特殊人群
								special.add(est); // 将本项价格记录送入list
							}
						}
					}
					relay.setAdult(adult);
					relay.setChild(child);
					relay.setSpecial(special);
				}
				
				for(EstimatePriceFile file:fileList){
					upReplyFileDocIds+=","+file.getDocInfoId();
				}
				if(upReplyFileDocIds.length()>0){
					upReplyFileDocIds = upReplyFileDocIds.replaceFirst(",", "");
				}
				repalyFilelist.add(fileList);
				reUpFileIdsList.add(upReplyFileDocIds);
			}
			map.put("repalyFilelist", repalyFilelist); 
			map.put("reUpFileIdsList", reUpFileIdsList);
		}
		
		
		//机票询价回复列表 	
		List<EstimatePricerReply> flist = estimatePricerReplyService.findReply(rid, String.valueOf(EstimatePricerReply.TYPE_FLIGHT));
		for(EstimatePricerReply rep:flist){
			// 2015-05-18 机票计调 回复总价
			List<EstimateMoneyView> moneyViewTopList =  estimateMoneyViewDao.findByTopReplyId(rep.getId());
			List<EstimatePrice> adultTop = new ArrayList<EstimatePrice>();
			List<EstimatePrice> childTop = new ArrayList<EstimatePrice>();
			List<EstimatePrice> specialTop = new ArrayList<EstimatePrice>();
			if(moneyViewTopList!=null && !moneyViewTopList.isEmpty()){
				for(EstimateMoneyView view : moneyViewTopList){ 
					if(EstimatePricePartReply.PRICE_TYPE_AOP.equals(view.getPriceType().toString())){// 机票报价
						BigDecimal amount =view.getAmount(); // 单价
						Integer personNum = view.getPersonNum();// 人数
						BigDecimal sumPrice = amount.multiply(new BigDecimal(personNum)); // 单价*人数
						EstimatePrice est = new EstimatePrice();
						est.setAmount(amount.toString());
						est.setPersonNum(personNum);
						est.setSumPrice(sumPrice.toString());
						est.setCurrencyId(Long.valueOf(view.getCurrencyId()));
						est.setMoneyAmountId(view.getMoneyAmountID()); // 流水ID
						est.setReplyId(view.getEstimatePriceReplyId()); // 报价ID
						if(EstimatePricePartReply.PERSON_TYPE_ADULT.equals(view.getPersonType().toString())){ // 成人
							adultTop.add(est); // 将本项价格记录送入list
						}else if(EstimatePricePartReply.PERSON_TYPE_CHILD.equals(view.getPersonType().toString())){ // 儿童
							childTop.add(est); // 将本项价格记录送入list
						}else if(EstimatePricePartReply.PERSON_TYPE_SPECIAL.equals(view.getPersonType().toString())){ // 特殊人群
							specialTop.add(est); // 将本项价格记录送入list
						}
					}
				}
				rep.setAdultTop(adultTop);
				rep.setChildTop(childTop);
				rep.setSpecialTop(specialTop);
			}
		}
		
		map.put("alist", alist);
		map.put("record", record);
		map.put("upFileList", upFileList);
		map.put("upFileDocIds", upFileDocIds);
		//机票询价回复列表 
		map.put("flist", flist);
		return map;
	}

	@Override
	public void successProject(Long rid) {
		if(rid!=null && rid>0){
			// 获取EstimatePriceRecord 询价项目,并将该询价项目设为"已发布产品"
			EstimatePriceRecord record = this.findById(rid);
			if(record!=null){
				record.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_PUBLISH);
				this.save(record);
				// 获取EstimatePriceProject 产品项目,并将该产品项目设为"已发布产品"
				EstimatePriceProject pro = estimatePriceProjectDao.findById(record.getPid());
				if(pro!=null){
					// 将产品项目设为"已发布产品"
					pro.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_PUBLISH);
					estimatePriceProjectDao.save(pro);
				}
			}
		}
	}
	/**
	 * 异步提交修改外报价
	 * @author yue.wang
	 * @时间 2014年12月12日
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public Map updateEpriceOutPrice(Map conditionMap) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("res", "success");
		String recordId = (String)conditionMap.get("recordId");
		String outPrice1 = (String)conditionMap.get("outPrice");
		BigDecimal outPrice = null;
		if(!StringUtils.isBlank(outPrice1)){
			outPrice = new BigDecimal(outPrice1);
		}
		EstimatePriceRecord record = estimatePriceRecordDao.findById(Long.parseLong(recordId));
		if(record==null){
			map.put("res", "找不到对应的record实例，请重试");
			return map;
		}
		record.setOutPrice(outPrice);
		estimatePriceRecordDao.getSession().update(record);
		estimatePriceRecordDao.getSession().flush();
		return map;
	}

	@Override
	public List<String> getXcFilesIdsByResultPage(Page<EstimatePriceRecord> page) {
		// TODO Auto-generated method stub
		List<EstimatePriceRecord> recordList = page.getResult();
		List<String> xcList = new ArrayList<String>();
		for(EstimatePriceRecord record:recordList){
			if(record.getAdmitRequirements()==null){
				xcList.add("");
				continue;
			}
			EstimatePriceFile example = new EstimatePriceFile();
			example.setPid(record.getAdmitRequirements().getId());
			example.setType(EstimatePriceFile.TYPE_TRIP);
			example.setPtype(EstimatePriceFile.PTYPE_ADMIT);
			List<EstimatePriceFile> fileList = estimatePriceFileDao.findByExample(example);
			String upReplyFileDocIds = "";
			for(EstimatePriceFile file:fileList){
				upReplyFileDocIds+=","+file.getDocInfoId();
			}
			if(upReplyFileDocIds.length()>0){
				upReplyFileDocIds = upReplyFileDocIds.replaceFirst(",", "");
			}
			xcList.add(upReplyFileDocIds);
		}
		return xcList;
	}

	@Override
	public List<String> getbjFilesIdsByResultPage(Page<EstimatePriceRecord> page) {
		// TODO Auto-generated method stub
		List<EstimatePriceRecord> recordList = page.getResult();
		List<String> bjList = new ArrayList<String>();
		for(EstimatePriceRecord record:recordList){
			if(record.getAcceptAoperatorReply()==null){
				bjList.add("");
				continue;
			}
			EstimatePriceFile example = new EstimatePriceFile();
			example.setPid(record.getAcceptAoperatorReply().getId());
			example.setType(EstimatePriceFile.TYPE_REPLY);
			example.setPtype(EstimatePriceFile.PTYPE_REPLY);
			List<EstimatePriceFile> fileList = estimatePriceFileDao.findByExample(example);
			String upReplyFileDocIds = "";
			for(EstimatePriceFile file:fileList){
				upReplyFileDocIds+=","+file.getDocInfoId();
			}
			if(upReplyFileDocIds.length()>0){
				upReplyFileDocIds = upReplyFileDocIds.replaceFirst(",", "");
			}
			bjList.add(upReplyFileDocIds);
		}
		return bjList;
	}

	@Override
	public String releaseProduct(Long recordId,String type) {
		// TODO Auto-generated method stub
		EstimatePriceRecord record = estimatePriceRecordDao.findById(recordId);
		if(record==null){
			return "找不到对应的record记录";
		}
		if(EstimatePriceRecord.ESTIMATE_STATUS_PUBLISH==record.getStatus()){
			return "该记录已经发布产品";
		}
		boolean t = false;//判断是否修改询价项目状态
		
		if(type.equals("7")){
			EstimatePricerReply topReplay = record.getAcceptToperatorReply();
			if(topReplay!=null){
				topReplay.setStatus(EstimatePricerReply.STATUS_PRODUCTED);
				estimatePricerReplyDao.getSession().update(topReplay);
			}
			if(record.getType()==EstimatePriceRecord.TYPE_FLIGHT){
				t=true;
			}
		}else{
			t=true;
			EstimatePricerReply aopReplay = record.getAcceptAoperatorReply();
			if(aopReplay!=null){
				aopReplay.setStatus(EstimatePricerReply.STATUS_PRODUCTED);
				estimatePricerReplyDao.getSession().update(aopReplay);
			}
		}
		if(t){
			record.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_PUBLISH);
			estimatePriceRecordDao.getSession().update(record);
			EstimatePriceProject project = estimatePriceProjectDao.findById(record.getPid());
			if(project.getEstimateStatus()<EstimatePriceProject.ESTIMATE_STATUS_PUBLISH){
				project.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_PUBLISH);
				estimatePriceProjectDao.update(project);
			}
		}
		estimatePriceRecordDao.getSession().flush();
		
		return "suc";
	}

	
	@Override
	public String releaseOrder(Long recordId) {
		// TODO Auto-generated method stub
		EstimatePriceRecord record = estimatePriceRecordDao.findById(recordId);
		if(record==null){
			return "找不到对应的record记录";
		}
		if(EstimatePriceRecord.ESTIMATE_STATUS_ORDER_PUBLISH==record.getStatus()){
			return "该记录已经生成订单";
		}
		record.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_ORDER_PUBLISH);
		estimatePriceRecordDao.getSession().update(record);
		EstimatePricerReply topReplay = record.getAcceptToperatorReply();
		if(topReplay!=null){
			topReplay.setStatus(EstimatePricerReply.STATUS_ORDERED);
			estimatePricerReplyDao.getSession().update(topReplay);
		}
		EstimatePricerReply aopReplay = record.getAcceptAoperatorReply();
		if(aopReplay!=null){
			aopReplay.setStatus(EstimatePricerReply.STATUS_ORDERED);
			estimatePricerReplyDao.getSession().update(aopReplay);
		}
		EstimatePriceProject project = estimatePriceProjectDao.findById(record.getPid());
		if(project.getEstimateStatus()<EstimatePriceProject.ESTIMATE_STATUS_ORDER_PUBLISH){
			project.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_ORDER_PUBLISH);
			estimatePriceProjectDao.update(project);
		}
		estimatePriceRecordDao.getSession().flush();
		return "suc";
	}

	@Override
	public List<EstimatePriceRecord> getAoperatorUserIdByPid(Long pid) {
		// TODO Auto-generated method stub
		List<EstimatePriceRecord> findByPid = estimatePriceRecordDao.findByPid(pid);
		return findByPid;
	}

}
