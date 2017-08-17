/**
 *
 */
package com.trekiz.admin.modules.visa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;

  /**
 *  文件名: Visabasics
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-01-13
 *  @version 1.0
 */
@Entity
@Table(name = "visabasics")
public class Visabasics extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 签证类型 */
    private Integer visaType;
    /** 是否需要面试 */
    private Integer isNeedAudition;
    /** 需机票订单 */
    private Integer isneedFlightOrder;
    /** 签证国家 */
    private Integer visaCountry;
    /** 抽查面试 */
    private Integer isNeedSpotAudition;
    /** 酒店订单 */
    private Integer isNeedHotelOrder;
    /** 办理所需时间 */
    private String handleTime;
    /** 是否需要预约 */
    private Integer isNeedBespeak;
    /** 是否需要保险 */
    private Integer isNeedInsurance;
    /** 销签所需时间 */
    private String pinCheckTime;
    /** 是否需要销签 */
    private Integer isNeedPinCheck;
    /** 自备邀请 */
    private Integer selfInvited;
    /** 领区划分 */
    private String collarZoning;
    /** 贴示 */
    private String stickshow;
    /** 注意事项 */
    private String attention;
    /** 特别提示 */
    private String specialTips;
    /** 其他说明 */
    private String otherDescription;
    /** 备注 */
    private String remarks;
    /** 附件id */
    private Integer fileTableId;

	public Visabasics() {
		super();
	}

	public Visabasics(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVisaType(Integer visaType ){
        this.visaType = visaType ;
    }

    public Integer getVisaType(){
        return this.visaType;
    }

    public void setIsNeedAudition(Integer isNeedAudition ){
        this.isNeedAudition = isNeedAudition ;
    }

    public Integer getIsNeedAudition(){
        return this.isNeedAudition;
    }

    public void setIsneedFlightOrder(Integer isneedFlightOrder ){
        this.isneedFlightOrder = isneedFlightOrder ;
    }

    public Integer getIsneedFlightOrder(){
        return this.isneedFlightOrder;
    }

    public void setVisaCountry(Integer visaCountry ){
        this.visaCountry = visaCountry ;
    }

    public Integer getVisaCountry(){
        return this.visaCountry;
    }

    public void setIsNeedSpotAudition(Integer isNeedSpotAudition ){
        this.isNeedSpotAudition = isNeedSpotAudition ;
    }

    public Integer getIsNeedSpotAudition(){
        return this.isNeedSpotAudition;
    }

    public void setIsNeedHotelOrder(Integer isNeedHotelOrder ){
        this.isNeedHotelOrder = isNeedHotelOrder ;
    }

    public Integer getIsNeedHotelOrder(){
        return this.isNeedHotelOrder;
    }

    public void setHandleTime(String handleTime ){
        this.handleTime = handleTime ;
    }

    @Length(min=0, max=50)
    public String getHandleTime(){
        return this.handleTime;
    }

    public void setIsNeedBespeak(Integer isNeedBespeak ){
        this.isNeedBespeak = isNeedBespeak ;
    }

    public Integer getIsNeedBespeak(){
        return this.isNeedBespeak;
    }

    public void setIsNeedInsurance(Integer isNeedInsurance ){
        this.isNeedInsurance = isNeedInsurance ;
    }

    public Integer getIsNeedInsurance(){
        return this.isNeedInsurance;
    }

    public void setPinCheckTime(String pinCheckTime ){
        this.pinCheckTime = pinCheckTime ;
    }

    @Length(min=0, max=50)
    public String getPinCheckTime(){
        return this.pinCheckTime;
    }

    public void setIsNeedPinCheck(Integer isNeedPinCheck ){
        this.isNeedPinCheck = isNeedPinCheck ;
    }

    public Integer getIsNeedPinCheck(){
        return this.isNeedPinCheck;
    }

    public void setSelfInvited(Integer selfInvited ){
        this.selfInvited = selfInvited ;
    }

    public Integer getSelfInvited(){
        return this.selfInvited;
    }

    public void setCollarZoning(String collarZoning ){
        this.collarZoning = collarZoning ;
    }

    @Length(min=0, max=65535)
    public String getCollarZoning(){
        return this.collarZoning;
    }

    public void setStickshow(String stickshow ){
        this.stickshow = stickshow ;
    }

    @Length(min=0, max=65535)
    public String getStickshow(){
        return this.stickshow;
    }

    public void setAttention(String attention ){
        this.attention = attention ;
    }

    @Length(min=0, max=65535)
    public String getAttention(){
        return this.attention;
    }

    public void setSpecialTips(String specialTips ){
        this.specialTips = specialTips ;
    }

    @Length(min=0, max=65535)
    public String getSpecialTips(){
        return this.specialTips;
    }

    public void setOtherDescription(String otherDescription ){
        this.otherDescription = otherDescription ;
    }

    @Length(min=0, max=65535)
    public String getOtherDescription(){
        return this.otherDescription;
    }

    public void setRemarks(String remarks ){
        this.remarks = remarks ;
    }

    @Length(min=0, max=65535)
    public String getRemarks(){
        return this.remarks;
    }

    public void setFileTableId(Integer fileTableId ){
        this.fileTableId = fileTableId ;
    }

    public Integer getFileTableId(){
        return this.fileTableId;
    }
}


