package com.trekiz.admin.modules.review.visaRebates.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import freemarker.template.TemplateException;

public interface IVisaRebatesService {

	/**
	 * 签证返佣审核通过操作
	 * @author jiachen
	 * @DateTime 2015年6月3日 下午1:56:37
	 * @param reviewMap
	 * @return void
	 */
	public void reviewSuccess(Map<String,String> reviewMap);
	
	/**
	 * 签证返佣审核通过操作(供审核公共接口调用)
	 * @author jiachen
	 * @DateTime 2015年6月3日 下午1:56:12
	 * @param reviewId
	 * @return void
	 */
	public void reviewSuccess(Long reviewId);
	
	//--------返佣打印开始--------
	
	public void updateReviewPrintInfoById (Long revid,Date printdate) throws Exception ;
	
	public File createRebatesReviewSheetDownloadFile(Long revid, String payId) throws IOException, TemplateException;
	
	//--------返佣打印结束--------
	
	public File createRebatesReviewSheetDownloadFile(String revid) throws IOException, TemplateException;
}
