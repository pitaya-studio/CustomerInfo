package com.trekiz.admin.review.returnvisareceipt.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.review.returnvisareceipt.service.IVisaReturnReceiptReviewService;

/**
 * 还签证收据审核控制器 Created by yunpeng.zhang on 2015/12/9.
 */
@Controller
@RequestMapping("${adminPath}/visa/hqx/returnvisareceipt/review")
public class VisaReturnReceiptReviewController {
	@Autowired
	private IVisaReturnReceiptReviewService visaReturnReceiptReviewService;


	@ResponseBody
	@RequestMapping("/backReview")
	public Map<String, Object> backReview(String batchNo) {
        /*声明返回对象*/
		Map<String, Object> result = new HashMap<String, Object>();
		// 如果批次号不为空，则进行审核撤销
		if(StringUtil.isNotBlank(batchNo)) {
			try {
				result = visaReturnReceiptReviewService.backReivew(batchNo);
			} catch(Exception e) {
				result.put("result", "fail");
				result.put("msg", "撤销失败！");
			}
		} else {
			result.put("result", "fail");
			result.put("msg", "批次号不能为空！");
		}
		return result;
	}


	/**
	 * 批量签证收据审核
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年12月14日14:22:00
	 * @param result
	 * @param remark
	 * @param batchNos
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/batchReviewVisaReturnReceipt", method = RequestMethod.POST)
	public Map<String, Object> batchReviewVisaReturnReceipt(String result, String remark, String batchNos) {
		Map<String, Object> resultMap = new HashMap<>();
		String[] batchNoStrs = batchNos.split(",");
		if (batchNoStrs != null && batchNoStrs.length > 0) {
			try {
				visaReturnReceiptReviewService.batchReturnVisaReceiptReview(result, remark, batchNoStrs);
			} catch (Exception e) {
				resultMap.put("result", "fail");
				resultMap.put("msg", "批量审核失败");
			}
		}
		resultMap.put("result", "success");
		resultMap.put("msg", "批量审批成功!");
		return resultMap;
	}

	/**
	 * 还签证收据审核
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年12月10日16:56:01
	 * @param result
	 *            1 审核通过 0 驳回
	 * @param denyReason
	 * @param batchNo
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/returnVisaReceiptReview4Hqx")
	public Map<String, Object> returnVisaReceiptReview4Hqx(String result, String denyReason, String batchNo,
			Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtil.isNotBlank(result) && StringUtil.isNotBlank(batchNo)) {
			try {
				resultMap = visaReturnReceiptReviewService.returnVisaReceiptReview(result, batchNo, denyReason);
			} catch (Exception e) {
				resultMap.put("result", "fail");
				resultMap.put("msg", "操作失败！");
				e.printStackTrace();
			}
		} else {
			resultMap.put("msg", "批次号不存在！");
			resultMap.put("result", "fail");
		}

		return resultMap;
	}

}
