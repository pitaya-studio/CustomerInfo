package com.trekiz.admin.agentToOffice.T1.activity.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.activity.service.ProductBrowseNumService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class T1CalendarActivityService  extends BaseService {
    
	@Autowired
	private TravelActivityDao activityDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
    private CurrencyDao currencyDao;
	@Autowired
	private OfficeDao officeDao;
	@Resource
	private DocInfoService docInfoService;
	@Autowired
    private UserDao userDao;
	@Autowired
	private ProductBrowseNumService productBrowseNumService;
    
	/**
	 * 获取产品下团期信息
	 * @return
	 * @author yakun.bai
	 * @Date 2016-11-22
	 */
	public Object getActivityGroupInfo(HttpServletRequest request) {
		
		JSONObject results = new JSONObject();
		
		String activityId = request.getParameter("activityId");
		String groupId = request.getParameter("groupId");
		
		// 如果产品ID为空，则直接返回错误
		if (StringUtils.isBlank(activityId)) {
			results.put("result", false);
			results.put("msg", "产品ID不能为空");
			return results;
		}
		
		TravelActivity activity = activityDao.findOne(Long.parseLong(activityId));
		//记录商品的浏览次数
		productBrowseNumService.updateBrowseNumByProductId(activity.getId());
		ActivityGroup group = null;
		// 如果团期ID为空，则选择产品中最小出团日期的团期
		if (StringUtils.isBlank(groupId)) {
			Date groupOpendDate = activity.getGroupOpenDate();
			Set<ActivityGroup> groupSet = activity.getActivityGroups();
			for (ActivityGroup activityGroup : groupSet) {
				if (activityGroup.getGroupOpenDate().compareTo(groupOpendDate) == 0) {
					group = activityGroup;
					break;
				}
			}
		} else {
			group = activityGroupDao.findOne(Long.parseLong(groupId));
		}
		
		/** 获取批发商信息 */
		setOfficeInfo(activity, group, results);
		
		/** 产品信息 */
		setActivityInfo(activity, group, results);
		
		/** 获取产品下所有团期信息 */
		setGroupInfo(activity, group, results);
		
		/** 获取销售信息 */
		getCompanySaler(activity, results);
		
		/** 是否有权限下单 */
		results.put("t1SalerRebate", (1 == UserUtils.getUser().getDifferenceRights() ? true : false));
		
		return results;
	}
	
	/**
	 * 获取产品下团期信息
	 * @return
	 * @author yakun.bai
	 * @Date 2016-11-22
	 */
	public Object getGroupInfo(HttpServletRequest request) {
		
		JSONObject results = new JSONObject();
		
		String groupId = request.getParameter("groupId");
		
		// 如果产品ID为空，则直接返回错误
		if (StringUtils.isBlank(groupId)) {
			results.put("result", false);
			results.put("msg", "团期ID不能为空");
			return results;
		}
		
		ActivityGroup group = activityGroupDao.findOne(Long.parseLong(groupId));
		
		/** 团期信息 */
		setGroupInfo(group, results);
		
		return results;
	}
	
	/**
	 * 获取批发商信息
	 * @param activity
	 * @param group
	 * @param results
	 * @author yakun.bai
	 * @Date 2016-10-23
	 */
	private void setOfficeInfo(TravelActivity activity, ActivityGroup group, JSONObject results) {
		
		/** 批发商信息 */
		JSONObject officeInfo = new JSONObject();
		
		Office office = officeDao.findById(activity.getProCompany());
		
		officeInfo.put("officeId", office.getId());
		officeInfo.put("docInfoId", office.getLogo());
		officeInfo.put("name", office.getName());
		officeInfo.put("phone", office.getPhone());
		officeInfo.put("webSite", office.getWebSite());
		
		// 资质证书
		List<DocInfo> businessCertificate = docInfoService.getDocInfoByStringIds(";", office.getBusinessCertificate());
		if (CollectionUtils.isNotEmpty(businessCertificate)) {
			JSONArray certificateArr = new JSONArray();
			for (DocInfo docInfo : businessCertificate) {
				JSONObject certificate = new JSONObject();
				certificate.put("docId", docInfo.getId());
				certificate.put("docName", docInfo.getDocName());
				certificateArr.add(certificate);
			}
			officeInfo.put("businessCertificate", certificateArr);
		} else {
			officeInfo.put("businessCertificate", null);
		}
		
		// 营业执照
		List<DocInfo> businessLicense = docInfoService.getDocInfoByStringIds(";", office.getBusinessLicense());
		if (CollectionUtils.isNotEmpty(businessLicense)) {
			JSONArray licenseArr = new JSONArray();
			for (DocInfo docInfo : businessLicense) {
				JSONObject license = new JSONObject();
				license.put("docId", docInfo.getId());
				license.put("docName", docInfo.getDocName());
				licenseArr.add(license);
			}
			officeInfo.put("businessLicense", licenseArr);
		} else {
			officeInfo.put("businessLicense", null);
		}
		
		// 合作协议
		List<DocInfo> cooperationProtocol = docInfoService.getDocInfoByStringIds(";", office.getCooperationProtocol());
		if (CollectionUtils.isNotEmpty(cooperationProtocol)) {
			JSONArray cooperationProtocolArr = new JSONArray();
			for (DocInfo docInfo : cooperationProtocol) {
				JSONObject cooperationProtocolObj = new JSONObject();
				cooperationProtocolObj.put("docId", docInfo.getId());
				cooperationProtocolObj.put("docName", docInfo.getDocName());
				cooperationProtocolArr.add(cooperationProtocolObj);
			}
			officeInfo.put("cooperationProtocol", cooperationProtocolArr);
		} else {
			officeInfo.put("cooperationProtocol", null);
		}
		
		results.put("officeInfo", officeInfo);
	}
	
	/**
	 * 获取产品信息
	 * @param activity
	 * @param group
	 * @param results
	 * @author yakun.bai
	 * @Date 2016-10-22
	 */
	private void setActivityInfo(TravelActivity activity, ActivityGroup group, JSONObject results) {
		
		/** 产品信息 */
		JSONObject productInfo = new JSONObject();
		// 产品名称
		productInfo.put("productName", activity.getAcitivityName());
		// 团号
		productInfo.put("groupCode", group.getGroupCode());
		// 出发城市
		productInfo.put("fromArea", activity.getFromAreaName() == null ? "" : activity.getFromAreaName());
		// 行程天数
		productInfo.put("activityDuration", activity.getActivityDuration());
		// 交通工具
		productInfo.put("trafficMode", activity.getTrafficModeName() == null ? "" : activity.getTrafficModeName());
		// 备注
		productInfo.put("remark", activity.getRemarks() == null ? "" : activity.getRemarks());
		
		// 行程单附件
		Set<ActivityFile> activityFiles = activity.getActivityFiles();
		if (CollectionUtils.isNotEmpty(activityFiles)) {
			JSONArray activityFilesArr = new JSONArray();
			for (ActivityFile file : activityFiles) {
				JSONObject activityFile = new JSONObject();
				activityFile.put("docId", file.getDocInfo().getId());
				activityFile.put("docName", file.getDocInfo().getDocName());
				activityFilesArr.add(activityFile);
			}
			results.put("activityFiles", activityFilesArr);
		} else {
			results.put("activityFiles", null);
		}
		
		/** 获取团期价格 */
		setGroupInfo(group, results);
		
		results.put("productInfo", productInfo);
	}
	
	/**
	 * 获取团期信息
	 * @param group
	 * @param results
	 * @author yakun.bai
	 * @Date 2016-11-23
	 */
	private void setGroupInfo(ActivityGroup group, JSONObject results) {
		// 团期价格
		JSONObject groupPriceDetail = new JSONObject();
		String currency = group.getCurrencyType();
		String[] currencyArr = currency.split(",");
		Rate rate = RateUtils.getRate(group.getId(), 2, UserUtils.getUser().getAgentId());
		
		Currency adultCurrency = currencyDao.findById(Long.parseLong(currencyArr[0]));
		Currency childCurrency = currencyDao.findById(Long.parseLong(currencyArr[1]));
		Currency specialCurrency = currencyDao.findById(Long.parseLong(currencyArr[2]));
		
		// 实际结算价
		BigDecimal adultPrice = OrderCommonUtil.getRetailPrice(group.getSettlementAdultPrice(), group.getQuauqAdultPrice(), rate, adultCurrency.getId());
		BigDecimal childPrice = OrderCommonUtil.getRetailPrice(group.getSettlementcChildPrice(), group.getQuauqChildPrice(), rate, childCurrency.getId());
		BigDecimal specialPrice = OrderCommonUtil.getRetailPrice(group.getSettlementSpecialPrice(), group.getQuauqSpecialPrice(), rate, specialCurrency.getId());
		
		DecimalFormat d = new DecimalFormat("##0.00");
		
		groupPriceDetail.put("groupId", group.getId());
		
		groupPriceDetail.put("adultPrice", adultPrice == null ? "--" : d.format(adultPrice));
		groupPriceDetail.put("adultCurrencyMark", adultCurrency.getCurrencyMark());
		groupPriceDetail.put("childPrice", childPrice == null ? "--" : d.format(childPrice));
		groupPriceDetail.put("childCurrencyMark", childCurrency.getCurrencyMark());
		groupPriceDetail.put("specialPrice", specialPrice == null ? "--" : d.format(specialPrice));
		groupPriceDetail.put("specialCurrencyMark", specialCurrency.getCurrencyMark());
		
		// 同行价
		BigDecimal settlementAdultPrice = group.getSettlementAdultPrice();
		BigDecimal settlementChildPrice = group.getSettlementcChildPrice();
		BigDecimal settlementSpecialPrice = group.getSettlementSpecialPrice();
		
		groupPriceDetail.put("thAdultPrice", settlementAdultPrice == null ? "--" : d.format(settlementAdultPrice));
		groupPriceDetail.put("thAdultCurrencyMark", adultCurrency.getCurrencyMark());
		groupPriceDetail.put("thChildPrice", settlementChildPrice == null ? "--" : d.format(settlementChildPrice));
		groupPriceDetail.put("thChildCurrencyMark", childCurrency.getCurrencyMark());
		groupPriceDetail.put("thSpecialPrice", settlementSpecialPrice == null ? "--" : d.format(settlementSpecialPrice));
		groupPriceDetail.put("thSpecialCurrencyMark", specialCurrency.getCurrencyMark());
		
		// 直客价
		BigDecimal suggestAdultPrice = group.getSuggestAdultPrice();
		BigDecimal suggestChildPrice = group.getSuggestChildPrice();
		BigDecimal suggestSpecialPrice = group.getSuggestSpecialPrice();
		
		groupPriceDetail.put("zkAdultPrice", suggestAdultPrice == null ? "--" : d.format(suggestAdultPrice));
		groupPriceDetail.put("zkAdultCurrencyMark", adultCurrency.getCurrencyMark());
		groupPriceDetail.put("zkChildPrice", suggestChildPrice == null ? "--" : d.format(suggestChildPrice));
		groupPriceDetail.put("zkChildCurrencyMark", childCurrency.getCurrencyMark());
		groupPriceDetail.put("zkSpecialPrice", suggestSpecialPrice == null ? "--" : d.format(suggestSpecialPrice));
		groupPriceDetail.put("zkSpecialCurrencyMark", specialCurrency.getCurrencyMark());
		
		results.put("transDetail", groupPriceDetail);
	}
	
	/**
	 * 获取团期信息
	 * @param activity
	 * @param group
	 * @param results
	 * @author yakun.bai
	 * @Date 2016-11-22
	 */
	private void setGroupInfo(TravelActivity activity, ActivityGroup group, JSONObject results) {
		
		Office office = officeDao.findById(activity.getProCompany());
		Integer t1FreePosionStatus = office.getT1FreePosionStatus();//0.实时 1.现询
		
		/** 被选中团期信息 */
		JSONObject groupSelected = new JSONObject();
		groupSelected.put("groupId", group.getId());
		groupSelected.put("month", DateUtils.date2String(group.getGroupOpenDate(), DateUtils.DATE_PATTERN_YYYYMMDD));
		results.put("groupSelected", groupSelected);
		
		/** 开始日期和结束日期 */
		JSONObject startToEnd = new JSONObject();
		JSONObject startMonth = new JSONObject(); 
		startMonth.put("year", DateUtils.date2String(activity.getGroupOpenDate(), DateUtils.DATE_PATTERN_YYYYMMDD));
		JSONObject endMonth = new JSONObject(); 
		endMonth.put("year", DateUtils.date2String(activity.getGroupCloseDate(), DateUtils.DATE_PATTERN_YYYYMMDD));
		startToEnd.put("startMonth", startMonth);
		startToEnd.put("endMonth", endMonth);
		results.put("startToEnd", startToEnd);
		
		/** 产品信息 */
		JSONArray groupDate = new JSONArray();
		JSONObject groupMonthObj = new JSONObject();
		JSONArray detail = new JSONArray();
		List<ActivityGroup> groupList = activityGroupDao.findT1GroupByActivityId(activity.getId().intValue());
		ActivityGroup nowGroup = null;
		ActivityGroup tempGroup = null;
		for (int i = 0; i < groupList.size(); i++) {
			nowGroup = groupList.get(i);
			Date nowDate = DateUtils.dateFormat(DateUtils.date2String(nowGroup.getGroupOpenDate(), "yyyy-MM-dd") + " 23:59:59");
			if (new Date().compareTo(nowDate) > 0) {
				continue;
			}
			String currency = nowGroup.getCurrencyType();
			String[] currencyArr = currency.split(",");
			Rate rate = RateUtils.getRate(nowGroup.getId(), 2, UserUtils.getUser().getAgentId());
			Currency adultCurrency = currencyDao.findById(Long.parseLong(currencyArr[0]));
			// 实际结算价
			BigDecimal adultPrice = OrderCommonUtil.getRetailPrice(nowGroup.getSettlementAdultPrice(), nowGroup.getQuauqAdultPrice(), rate, adultCurrency.getId());
			DecimalFormat d = new DecimalFormat(",##0.00");
			
			Integer selectedGroup = group.getId().longValue() == nowGroup.getId().longValue() ? 1 : 0;
			if (i == 0) {
				JSONObject groupObj = new JSONObject();
				groupObj.put("groupId", nowGroup.getId());
				groupObj.put("date", DateUtils.date2String(nowGroup.getGroupOpenDate(), "dd"));
				groupObj.put("groupPrice", adultPrice == null ? "--" : adultCurrency.getCurrencyMark() + d.format(adultPrice));
				groupObj.put("currencyName", adultCurrency.getCurrencyName());
				groupObj.put("freeSeat", nowGroup.getFreePosition());
				groupObj.put("selectedGroup", selectedGroup);
				groupObj.put("t1FreePosionStatus", t1FreePosionStatus);
				detail.add(groupObj);
				groupMonthObj.put("month", DateUtils.date2String(nowGroup.getGroupOpenDate(), "yyyyMM"));
				groupMonthObj.put("detail", detail);
				if (groupList.size() == 1) {
					groupDate.add(groupMonthObj);
				}
			} else {
				tempGroup = groupList.get(i - 1);
				String groupMonth = DateUtils.date2String(nowGroup.getGroupOpenDate(), "yyyyMM");
				String preGroupMonth = DateUtils.date2String(tempGroup.getGroupOpenDate(), "yyyyMM");
				if (groupMonth.equals(preGroupMonth)) {
					JSONObject groupObj = new JSONObject();
					groupObj.put("groupId", nowGroup.getId());
					groupObj.put("date", DateUtils.date2String(nowGroup.getGroupOpenDate(), "dd"));
					groupObj.put("groupPrice", adultPrice == null ? "--" : adultCurrency.getCurrencyMark() + d.format(adultPrice));
					groupObj.put("currencyName", adultCurrency.getCurrencyName());
					groupObj.put("freeSeat", nowGroup.getFreePosition());
					groupObj.put("selectedGroup", selectedGroup);
					groupObj.put("t1FreePosionStatus", t1FreePosionStatus);
					detail.add(groupObj);
					groupMonthObj.put("month", DateUtils.date2String(nowGroup.getGroupOpenDate(), "yyyyMM"));
					groupMonthObj.put("detail", detail);
					if (i == groupList.size() -1) {
						groupDate.add(groupMonthObj);
					}
				} else {
					groupDate.add(groupMonthObj);
					detail = new JSONArray();
					JSONObject groupObj = new JSONObject();
					groupObj.put("groupId", nowGroup.getId());
					groupObj.put("date", DateUtils.date2String(nowGroup.getGroupOpenDate(), "dd"));
					groupObj.put("groupPrice", adultPrice == null ? "--" : adultCurrency.getCurrencyMark() + d.format(adultPrice));
					groupObj.put("currencyName", adultCurrency.getCurrencyName());
					groupObj.put("freeSeat", nowGroup.getFreePosition());
					groupObj.put("selectedGroup", selectedGroup);
					groupObj.put("t1FreePosionStatus", t1FreePosionStatus);
					detail.add(groupObj);
					groupMonthObj.put("month", DateUtils.date2String(nowGroup.getGroupOpenDate(), "yyyyMM"));
					groupMonthObj.put("detail", detail);
					if (i == groupList.size() -1) {
						groupDate.add(groupMonthObj);
					}
				}
			}
		}
		results.put("groupDate", groupDate);
	}
	
	/**
	 * 获取T2有quauq报名权限用户
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-11-23
	 */
	private void getCompanySaler(TravelActivity activity, JSONObject results) {
		
		// 联系人
		List<User> userList = userDao.findCompanyT1User(activity.getProCompany());
		JSONArray userArr = new JSONArray();
		if (CollectionUtils.isNotEmpty(userList)) {
			JSONObject userInfo = null;
			for (User user : userList) {
				userInfo = new JSONObject();
				userInfo.put("salerId", user.getId());
				userInfo.put("salerName", user.getName());
				userInfo.put("salerPhone", user.getPhone());
				userInfo.put("salerCardId", user.getCardId());
				userInfo.put("salerMobile", user.getMobile());
				userInfo.put("salerEmail", user.getEmail());
				userInfo.put("salerWechart", user.getWeixin());
				userInfo.put("salerPhotoId", user.getPhotoId());
				userArr.add(userInfo);
			}
		}
		results.put("contacts", userArr);
	}
}