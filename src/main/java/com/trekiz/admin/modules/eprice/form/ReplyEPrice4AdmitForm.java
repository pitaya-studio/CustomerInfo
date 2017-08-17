package com.trekiz.admin.modules.eprice.form;

import org.apache.commons.lang3.StringUtils;

/**
 * 接待询价内容被回复表单类
 * @author lihua.xu
 * @时间 2014年9月28日
 *
 */
public class ReplyEPrice4AdmitForm {
	
	private Long rid;
	
	private String content;
	
	/**
	 * 报价明细
	 * {"adult":{"price":1000},"child":{"price":200},"specialPerson":{"price":300},"other":[{"title":"优惠价1","price":200,"sum":200}]}
	 */
	private String priceDetail;
	
	
	/**
	 * 行程文件记录id
	 */
	private Long[] salerTripFileId;
	
	/**
	 * 行程文件记录Name
	 */
	private String[] salerTripFileName;
	
	/**
	 * 行程文件记录Path
	 */
	private String[] salerTipFilePath;
	
	
	public boolean check(){
		
		return true;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getContent() {
		if(StringUtils.isNotBlank(this.content)){
			this.content = this.content.trim();
		}else{
			this.content = null;
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPriceDetail() {
		return priceDetail;
	}

	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}

	public Long[] getSalerTripFileId() {
		return salerTripFileId;
	}

	public void setSalerTripFileId(Long[] salerTripFileId) {
		this.salerTripFileId = salerTripFileId;
	}

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

	 
	
	

}
