/**
 *
 */
package com.trekiz.admin.modules.traveler.entity;

import java.util.List;

import com.trekiz.admin.modules.sys.entity.Dict;

  /**
 *  文件名: TravelerVisaInfo
 *  功能: 游客自备签信息列表
 *  修改记录:   
 *  
 *  @author taoxiaoyang
 *  @DateTime 2014-11-12
 *  @version 1.0
 */

public class TravelerVisaInfo{
	
    /** 申请国家ID */
    private Long applyCountryId;
    private Long sysCountryId;
    /** 申请国家名称 */
    private String applyCountryName;
    /** 领地 */
    private List<Dict> manorList;
	/** 签证类型 */
    private List<Dict> visaTypeList;
    
	public Long getApplyCountryId() {
		return applyCountryId;
	}

	public void setApplyCountryId(Long applyCountryId) {
		this.applyCountryId = applyCountryId;
	}

	public Long getSysCountryId() {
		return sysCountryId;
	}

	public void setSysCountryId(Long sysCountryId) {
		this.sysCountryId = sysCountryId;
	}

	public String getApplyCountryName() {
		return applyCountryName;
	}

	public void setApplyCountryName(String applyCountryName) {
		this.applyCountryName = applyCountryName;
	}

	public List<Dict> getManorList() {
		return manorList;
	}

	public void setManorList(List<Dict> manorList) {
		this.manorList = manorList;
	}

	public List<Dict> getVisaTypeList() {
		return visaTypeList;
	}

	public void setVisaTypeList(List<Dict> visaTypeList) {
		this.visaTypeList = visaTypeList;
	}

}


