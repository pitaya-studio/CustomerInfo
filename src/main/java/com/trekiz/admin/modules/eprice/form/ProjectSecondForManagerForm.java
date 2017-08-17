package com.trekiz.admin.modules.eprice.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 20150902 新增，由计调主管分配计调的询价流程，第二步表单
 * 
 * @author gao
 *
 */
public class ProjectSecondForManagerForm {
	
	/**
	 * 询价项目id
	 */
	@NotNull
	private Long projectId;
	
	/**
	 * 预计出团日期
	 */
	private String dgroupOutDate;
	
	/**
	 * 出境口岸id
	 */
	private Long outAreaId;
	
	/**
	 * 接待计调用户id
	 */
//	private Long[] aoperatorUserId;
	
	
	/**
	 * 冗余出境口岸名字
	 */
	private String outAreaName;
	
	/**
	 * 线路国家id
	 *
	 */
	private Long[] travelCountryId;
	
	/**
	 * 线路国家名字
	 * 
	 */
	private String[] travelCountry;
	
	/**
	 * 境外停留白天天数
	 */
	@Min(value=0)
	private Integer outsideDaySum;
	
	/**
	 * 境外停留夜晚天数
	 */
	@Min(value=0)
	private Integer outsideNightSum;
	
	
	public static Integer onceAgainYes = 1;
	public static Integer onceAgainNo = 0;
	
	/*
	 * 是否是再次询价 1-是 0-否
	 */
	private Integer onceAgain  =0;
	
	/**
	 * 旅游要求
	 * json： {"hotelType":{"type":1,"title":"三星","name":"酒店类型","info":"说明"},"hotelPosition":{"type":1,"title":"三星",","name":"酒店类型"}……,“otherRequirements”:[]}
	 * travelTeamType:团队类型 0 其他，1 单独成团，2 参加拼团
	 * visaType:护照种类, 0其他，1 因公护照，2 因公护照
	 * hotelType : 酒店类型  0 其他， 1 三星，  2 四星， 3 五星
	 * hotelPosition:酒店位置  0 其他， 1  郊区，2 市郊，3 市区，4 市中心
	 * roomType:房间要求 0 其他，1 单人间，2 双人间，3 三人间
	 * carType:用车要求 0 其他，1 “9座车”，2 中巴，3 大车 {“type”:3,"name":"大车","sum":18} (type为3时，sum属性起作用)
	 * guideType:导游要求 0 其他，1 司机兼导游，2 一司一导，
	 * leaderType:领队要求 0 其他，1 需要领队，2 不需要领队
	 * attractionType:景点要求 0 其他，1 门票含讲解，2 其他门票
	 * publicWordType:公务要求 0 其他，1 无公务活动，2 有公务活动
	 * breakfastType:早餐要求 0 其他， 1 欧陆自助，2 美式自助
	 * dinnerType:正餐要求 0 其他，1 5菜一汤，特色餐
	 * visaNeedType:签证要求， 1 要求办理签证，2 不要办理签证，descn 签证描述
	 * otherRequirements:其他要求，列表，value:[{"name":"新增要求title","value":"新增要求value"}]
	 */
	private String travelRequirements;
	
	/**
	 * 行程文件记录id
	 */
	private Long[] salerTripFileId;
	
	/**
	 * 行程文件记录Name
	 */
	private String[] salerTripFileName;
	
	/**
	 * 选中的地接计调主管ID
	 */
	@NotNull
	private String formanager;
	
	public String[] getSalerTripFileName() {
		return salerTripFileName;
	}


	public void setSalerTripFileName(String[] salerTripFileName) {
		this.salerTripFileName = salerTripFileName;
	}


	public String[] getSalerTipFilePath() {
		return salerTipFilePath;
	}


	public void setSalerTipFilePath(String[] salerTipFilePath) {
		this.salerTipFilePath = salerTipFilePath;
	}

	/**
	 * 行程文件记录paths
	 */
	private String[] salerTipFilePath;
	
	/**
	 * 额外的验证检查
	 * 20150902 去除已选计调验证，因为计调由计调主管分配，此时可以为空
	 * @author lihua.xu
	 * @时间 2014年9月23日
	 * @return boolean
	 */
	public String check(){
		if(StringUtils.isBlank(formanager) || "请搜索计调主管".equals(formanager)){
			return "请选择地接计调主管";
		}
		
		if(StringUtils.isBlank(dgroupOutDate)){
			return "请选择预计出团日期";
		}
		
		if(travelCountryId==null||travelCountryId.length==0){
			return "请选择线路国家";
		}
		if (Context.SUPPLIER_UUID_DHJQ.equals(UserUtils.getUser().getCompany().getUuid())) {			
			if(salerTripFileId == null || salerTripFileId.length == 0){
				return "请上传行程文件";
			}
		}
		
		return null;
	}
	

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getDgroupOutDate() {
		return dgroupOutDate;
	}

	public void setDgroupOutDate(String dgroupOutDate) {
		this.dgroupOutDate = dgroupOutDate;
	}

	public Long getOutAreaId() {
		return outAreaId;
	}

	public void setOutAreaId(Long outAreaId) {
		this.outAreaId = outAreaId;
	}

	public String getOutAreaName() {
		return outAreaName;
	}

	public void setOutAreaName(String outAreaName) {
		this.outAreaName = outAreaName;
	}

//	public Long[] getAoperatorUserId() {
//		return aoperatorUserId;
//	}
//
//
//	public void setAoperatorUserId(Long[] aoperatorUserId) {
//		this.aoperatorUserId = aoperatorUserId;
//	}

	public Long[] getTravelCountryId() {
		return travelCountryId;
	}


	public void setTravelCountryId(Long[] travelCountryId) {
		this.travelCountryId = travelCountryId;
	}


	public String[] getTravelCountry() {
		return travelCountry;
	}


	public void setTravelCountry(String[] travelCountry) {
		this.travelCountry = travelCountry;
	}


	public Integer getOutsideDaySum() {
		return outsideDaySum;
	}

	public void setOutsideDaySum(Integer outsideDaySum) {
		this.outsideDaySum = outsideDaySum;
	}

	public Integer getOutsideNightSum() {
		return outsideNightSum;
	}

	public void setOutsideNightSum(Integer outsideNightSum) {
		this.outsideNightSum = outsideNightSum;
	}

	public String getTravelRequirements() {
		return travelRequirements;
	}

	public void setTravelRequirements(String travelRequirements) {
		this.travelRequirements = travelRequirements;
	}

	public Long[] getSalerTripFileId() {
		return salerTripFileId;
	}

	public void setSalerTripFileId(Long[] salerTripFileId) {
		this.salerTripFileId = salerTripFileId;
	}


	public Integer getOnceAgain() {
		return onceAgain;
	}


	public void setOnceAgain(Integer onceAgain) {
		this.onceAgain = onceAgain;
	}


	public String getFormanager() {
		return formanager;
	}


	public void setFormanager(String formanager) {
		this.formanager = formanager;
	}


 
	
	
	
}
