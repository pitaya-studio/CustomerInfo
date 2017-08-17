package com.trekiz.admin.modules.eprice.form;

import java.util.Arrays;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.DateUtils;

/**
 * 接收新增单团询价项目第三表单——机票询价表单
 *  20150902 新增，由计调主管分配计调的询价流程，第三步表单
 * @author lihua.xu
 * @时间 2014年9月23日
 *
 */
public class ProjectThirdForm {
	
	/**
	 * 询价项目id
	 */
	@NotNull
	private Long projectId;
	
	/**
	 * 是否申请机票
	 * true申请，false 不申请
	 */
	private Boolean isAppFlight;

	/**
	 * 票务计调用户id
	 */
	@NotNull
	private Long[] toperatorUserId;
	
	/**
	 * 交通路线类型
	 * 1 往返，2 单程，3 多段
	 */
	private Integer trafficLineType;
	
	/**
	 * 线路地域类型：trafficLineType值为1、2时有效
	 * 1 内陆，2 国际，3 内陆+国际
	 */
	private Integer qAreaType;
	
	/**
	 * 编号
	 * 标识第几段
	 */
	private Integer[] no;
	
	/**
	 * 往返
	 * 出发
	 */
	@NotNull
	private Long[] startCityId;
	@NotNull
	private String[] startCityName;
	
	/**
	 * 终点城市id
	 */
	@NotNull
	private Long[] endCityId;
	
	/**
	 * 终点城市name
	 */
	@NotNull
	private String[] endCityName;
	
	/**
	 * 出发日期（不含时分秒，只标识日期）
	 */
	@NotNull
	private String[] startDate;
	
	/**
	 * 出发时间区间开始时间（不含日期，只标识时间）
	 */
	private String[] startTime1;
	
	/**
	 * 出发时间区间结束时间（不含日期，只标识时间）
	 */
	private String[] startTime2;
	
	/**
	 * 出发时刻：1 早 2中 3 晚
	 */
	private Integer[] startTimeType;
	
	/**
	 * 地域类型
	 * 1 内陆，2 国际，3 内陆+国际
	 */
	private Integer[] areaType;
	
	/**
	 * 舱位等级:1 头等舱；2 公务舱；3 经济舱
	 */
	private Integer[] aircraftSpaceLevel;
	
	
	
	/**
	 * 舱位：Y（Y舱），K（K舱）
	 */
	private String[]  aircraftSpace;
	
	
	/**
	 * 舱位：Y（Y舱），K（K舱）
	 */
	private Integer[]  lineAllPersonSum;
	
	
	/**
    * 交通方式类型
    * 1、汽车；2 火车；3 航班；4 混合交通方式
    */
	private Integer trafficType=3;
	
	/** 总人数
	 */
	@NotNull
	@Min(value=1)
	private Integer allPersonSum;
	
	/**
	 * 成人数
	 */
	
	private Integer adultSum;
	
	/**
	 * 儿童数
	 */
	private Integer childSum;
	
	/**
	 * 特殊人群
	 */
	private Integer specialPersonSum;
	
	/**
	 * 特殊要求
	 */
	private String specialDescn;
	
	/*
	 * 是否是再次询价 1-是 0-否
	 */
	private Integer onceAgain  =0;
	
	public String check(){
		if(!isAppFlight){
			return null;
		}
		
		int size = no.length;
		for(int i =0;i<size;i++){
			if(startCityId[i]==0){
				return "请选择第"+(i+1)+"段路线的出发城市";
			}
			if(endCityId[i]==0){
				return "请选择第"+(i+1)+"段路线的到达城市";
			}
			if(startCityName[i].equals(endCityName[i])){
				return "第"+(i+1)+"段路的出发城市与到达城市不能一致";
			}
			if(startDate==null||startDate.length<=i||StringUtils.isBlank(startDate[i])){
				return "请选择第"+(i+1)+"段路线的出发日期";
			}
			
			
			//判断时间
			if(size>1&&i<size-1){
				int result = DateUtils.compareDate(startDate[i], "yyyy-MM-dd", startDate[i+1], "yyyy-MM-dd");
				if(result>0){
					return "第"+(i+1)+"段路线的出发日期不能大于第"+(i+2)+"段路线的出发日期";
				}
				if(result==0){
					String startStr =startTime2[i];
					if(startStr.equals("0")){
						startStr = "24";
					}
					if(Integer.parseInt(startStr)>Integer.parseInt(startTime1[i+1])){
						return "第"+(i+1)+"段路线的出发时间不能大于第"+(i+2)+"段路线的出发时间";
					}
					
				}
			}
			
		}
		
		//判断城市
		if(trafficLineType==1){
			if(!startCityId[0].equals(endCityId[1])){
				return "去程出发城市应该与返程到达城市一致";
			}
			
			if(!startCityId[1].equals(endCityId[0])){
				return "去程到达城市应该与返程出发城市一致";
			}
		}
		
		
		
		if(allPersonSum==null||allPersonSum==0){
			 return "请输入正确的申请总人数";
		}
		
		
		if(allPersonSum!=adultSum+childSum+specialPersonSum){
			 return "申请总人数应等于成人人数+儿童人数+特殊人群";
		}
		return null;
	}
	

	public Long getProjectId() {
		return projectId;
	}

	public Long[] getToperatorUserId() {
		return toperatorUserId;
	}


	public void setToperatorUserId(Long[] toperatorUserId) {
		this.toperatorUserId = toperatorUserId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Boolean getIsAppFlight() {
		return isAppFlight;
	}

	public void setIsAppFlight(Boolean isAppFlight) {
		this.isAppFlight = isAppFlight;
	}

	public Integer getTrafficLineType() {
		return trafficLineType;
	}

	public void setTrafficLineType(Integer trafficLineType) {
		this.trafficLineType = trafficLineType;
	}

	public Integer getqAreaType() {
		return qAreaType;
	}

	public void setqAreaType(Integer qAreaType) {
		this.qAreaType = qAreaType;
	}

	public Long[] getStartCityId() {
		return startCityId;
	}

	public void setStartCityId(Long[] startCityId) {
		this.startCityId = startCityId;
	}

	public String[] getStartCityName() {
		return startCityName;
	}

	public void setStartCityName(String[] startCityName) {
		this.startCityName = startCityName;
	}

	public Long[] getEndCityId() {
		return endCityId;
	}

	public void setEndCityId(Long[] endCityId) {
		this.endCityId = endCityId;
	}

	public String[] getEndCityName() {
		return endCityName;
	}

	public void setEndCityName(String[] endCityName) {
		this.endCityName = endCityName;
	}

	public String[] getStartDate() {
		return startDate;
	}

	public void setStartDate(String[] startDate) {
		this.startDate = startDate;
	}

	public String[] getStartTime1() {
		return startTime1;
	}

	public void setStartTime1(String[] startTime1) {
		this.startTime1 = startTime1;
	}

	public String[] getStartTime2() {
		return startTime2;
	}

	public void setStartTime2(String[] startTime2) {
		this.startTime2 = startTime2;
	}

	public Integer[] getStartTimeType() {
		return startTimeType;
	}

	public void setStartTimeType(Integer[] startTimeType) {
		this.startTimeType = startTimeType;
	}

	public Integer[] getAreaType() {
		return areaType;
	}

	public void setAreaType(Integer[] areaType) {
		this.areaType = areaType;
	}

	public Integer[] getAircraftSpaceLevel() {
		return aircraftSpaceLevel;
	}

	public void setAircraftSpaceLevel(Integer[] aircraftSpaceLevel) {
		this.aircraftSpaceLevel = aircraftSpaceLevel;
	}

	public String[] getAircraftSpace() {
		return aircraftSpace;
	}

	public void setAircraftSpace(String[] aircraftSpace) {
		this.aircraftSpace = aircraftSpace;
	}

	public Integer getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(Integer trafficType) {
		this.trafficType = trafficType;
	}

	public Integer getAllPersonSum() {
		return allPersonSum;
	}

	public void setAllPersonSum(Integer allPersonSum) {
		this.allPersonSum = allPersonSum;
	}

	public Integer getAdultSum() {
		return adultSum;
	}

	public void setAdultSum(Integer adultSum) {
		this.adultSum = adultSum;
	}

	public Integer getChildSum() {
		return childSum;
	}

	public void setChildSum(Integer childSum) {
		this.childSum = childSum;
	}

	public Integer getSpecialPersonSum() {
		return specialPersonSum;
	}

	public void setSpecialPersonSum(Integer specialPersonSum) {
		this.specialPersonSum = specialPersonSum;
	}

	public String getSpecialDescn() {
		return specialDescn;
	}

	public void setSpecialDescn(String specialDescn) {
		this.specialDescn = specialDescn;
	}

	

	public Integer[] getNo() {
		return no;
	}


	public void setNo(Integer[] no) {
		this.no = no;
	}


	public Integer[] getLineAllPersonSum() {
		return lineAllPersonSum;
	}


	public void setLineAllPersonSum(Integer[] lineAllPersonSum) {
		this.lineAllPersonSum = lineAllPersonSum;
	}


	@Override
	public String toString() {
		return "ProjectThirdForm [projectId=" + projectId + ", toperatorUserId="+toperatorUserId+", isAppFlight="
				+ isAppFlight + ", trafficLineType=" + trafficLineType
				+ ", qAreaType=" + qAreaType + ", no=" + Arrays.toString(no)
				+ ", startCityId=" + Arrays.toString(startCityId)
				+ ", startCityName=" + Arrays.toString(startCityName)
				+ ", endCityId=" + Arrays.toString(endCityId)
				+ ", endCityName=" + Arrays.toString(endCityName)
				+ ", startDate=" + Arrays.toString(startDate) + ", startTime1="
				+ Arrays.toString(startTime1) + ", startTime2="
				+ Arrays.toString(startTime2) + ", startTimeType="
				+ Arrays.toString(startTimeType) + ", areaType="
				+ Arrays.toString(areaType) + ", aircraftSpaceLevel="
				+ Arrays.toString(aircraftSpaceLevel) + ", aircraftSpace="
				+ Arrays.toString(aircraftSpace) + ", lineAllPersonSum="
				+ Arrays.toString(lineAllPersonSum) + ", trafficType="
				+ trafficType + ", allPersonSum=" + allPersonSum
				+ ", adultSum=" + adultSum + ", childSum=" + childSum
				+ ", specialPersonSum=" + specialPersonSum + ", specialDescn="
				+ specialDescn + "]";
	}


	public Integer getOnceAgain() {
		return onceAgain;
	}


	public void setOnceAgain(Integer onceAgain) {
		this.onceAgain = onceAgain;
	}
	
	
	
	
	
	
}
