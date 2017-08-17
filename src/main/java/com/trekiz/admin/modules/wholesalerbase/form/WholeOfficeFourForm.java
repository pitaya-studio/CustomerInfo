package com.trekiz.admin.modules.wholesalerbase.form;

import javax.validation.constraints.NotNull;

/**
 * 新增批发商第四步 资质上传
 * @author gao
 *  2015年4月13日
 */
public class WholeOfficeFourForm {
	// 资质附件类别名称
	@NotNull(message="请填写资质附件名称")
	private String[] title;
	/**
	 * 资质附件类别代码 
	 * 1:营业执照
	 * 2：经营许可证
	 * 3：税务登记证
	 * 4：组织机构代码证
	 * 5：公司法人身份证（正反面一起）
	 * 6：公司银行开户许可证
	 * 7：旅游业资质
	 * 8：其他文件
	 */
	private Integer[] titleType;
	// 对应批发商ID
	@NotNull(message="指定批发商不存在")
	private Long companyId;
	@NotNull(message="请上传附件")
	private Long[] salerTripFileId; // 附件ID
	private String[] salerTripFileName;// 附件文件名
	private String[] salerTipFilePath; // 附件文件地址
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	public Integer[] getTitleType() {
		return titleType;
	}
	public void setTitleType(Integer[] titleType) {
		this.titleType = titleType;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
