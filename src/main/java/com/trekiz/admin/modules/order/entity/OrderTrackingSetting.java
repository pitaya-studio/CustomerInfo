package com.trekiz.admin.modules.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 订单跟踪设置实体类
 * @author yakun.bai
 * @Date 2016-8-12
 */
@Entity
@Table(name = "order_tracking_setting")
public class OrderTrackingSetting extends DataEntity {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	/** 批发商ID */
	private Long companyId;
	/** 销售处理中 1；上传确认单 2；计调处理中 3；财务收款 4 */
	private Integer settingType;
	/** 绿灯时间单位：天 1；小时 2；分 3 */
	private Integer greenLightTimeType;
	/** 绿灯时间：多少时间内显示 */
	private Integer greenLightTimeStart;
	/** 绿灯时间：多少时间内显示 */
	private Integer greenLightTimeEnd;
	/** 黄灯时间单位：天 1；小时 2；分 3 */
	private Integer yellowLightTimeType;
	/** 黄灯时间：多少时间内显示 */
	private Integer yellowLightTimeStart;
	/** 黄灯时间：多少时间内显示 */
	private Integer yellowLightTimeEnd;
	/** 红灯时间单位：天 1；小时 2；分 3 */
	private Integer redLightTimeType;
	/** 红灯时间：多少时间内显示 */
	private Integer redLightTimeStart;
	/** 红灯时间：多少时间内显示 */
	private Integer redLightTimeEnd;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="company_id")
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	@Column(name="setting_type")
	public Integer getSettingType() {
		return settingType;
	}
	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}
	@Column(name="green_light_time_type")
	public Integer getGreenLightTimeType() {
		return greenLightTimeType;
	}
	public void setGreenLightTimeType(Integer greenLightTimeType) {
		this.greenLightTimeType = greenLightTimeType;
	}
	@Column(name="green_light_time_start")
	public Integer getGreenLightTimeStart() {
		return greenLightTimeStart;
	}
	public void setGreenLightTimeStart(Integer greenLightTimeStart) {
		this.greenLightTimeStart = greenLightTimeStart;
	}
	@Column(name="green_light_time_end")
	public Integer getGreenLightTimeEnd() {
		return greenLightTimeEnd;
	}
	public void setGreenLightTimeEnd(Integer greenLightTimeEnd) {
		this.greenLightTimeEnd = greenLightTimeEnd;
	}
	@Column(name="yellow_light_time_type")
	public Integer getYellowLightTimeType() {
		return yellowLightTimeType;
	}
	public void setYellowLightTimeType(Integer yellowLightTimeType) {
		this.yellowLightTimeType = yellowLightTimeType;
	}
	@Column(name="yellow_light_time_start")
	public Integer getYellowLightTimeStart() {
		return yellowLightTimeStart;
	}
	public void setYellowLightTimeStart(Integer yellowLightTimeStart) {
		this.yellowLightTimeStart = yellowLightTimeStart;
	}
	@Column(name="yellow_light_time_end")
	public Integer getYellowLightTimeEnd() {
		return yellowLightTimeEnd;
	}
	public void setYellowLightTimeEnd(Integer yellowLightTimeEnd) {
		this.yellowLightTimeEnd = yellowLightTimeEnd;
	}
	@Column(name="red_light_time_type")
	public Integer getRedLightTimeType() {
		return redLightTimeType;
	}
	public void setRedLightTimeType(Integer redLightTimeType) {
		this.redLightTimeType = redLightTimeType;
	}
	@Column(name="red_light_time_start")
	public Integer getRedLightTimeStart() {
		return redLightTimeStart;
	}
	public void setRedLightTimeStart(Integer redLightTimeStart) {
		this.redLightTimeStart = redLightTimeStart;
	}
	@Column(name="red_light_time_end")
	public Integer getRedLightTimeEnd() {
		return redLightTimeEnd;
	}
	public void setRedLightTimeEnd(Integer redLightTimeEnd) {
		this.redLightTimeEnd = redLightTimeEnd;
	}
}