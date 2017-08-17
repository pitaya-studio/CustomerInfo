/**
 *
 */
package com.trekiz.admin.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.entity.SysBatchNo;
import com.trekiz.admin.modules.sys.repository.SysBatchNoDao;

/**
 * 批次号Service
 * @author yue.wang
 * @version 2015-03-03
 */
@Service
@Transactional(readOnly = true)
public class SysBatchNoService extends BaseService {

	@Autowired
	private SysBatchNoDao sysBatchNoDao;
	
	/*
	 * 取借护照批次号
	 */
	public synchronized String getBorrowPassPortBatchNo(){
		String curDate = DateUtils.getDate("yyyyMMdd");
		String code = SysBatchNo.CODE_BORROW_PASSPORT;
		Integer value = genBatchNo(curDate,code);
		String result =curDate+"-"+ "0000".substring(0,4-value.toString().length())+value;
		return result;
	}
	
	
	
	
	
	/*
	 * 签证批量借款批次号
	 */
	public synchronized String getVisaBorrowMoneyBatchNo(){
		String curDate = DateUtils.getDate("yyyyMMdd");
		String code = SysBatchNo.CODE_VISA_BORROWMONEY_BACTCH;
		Integer value = genBatchNo(curDate,code);
		String result =curDate+"-"+ "0000".substring(0,4-value.toString().length())+value;
		return result;
	}
	
	/**
	 * 获取批次号的通用方法：批次号的格式为：（yyyyMMdd）+"-"+当日的流水号（1开始，不足4位的用0补齐）
	 * @param code:相当于批次号的类型：统一定义在SysBatchNo类中：
	 * 如：
	 * //批次借护照
	 * public final static String CODE_BORROW_PASSPORT="CODE_BORROW_PASSPORT";
	 * 
	 * @return （yyyyMMdd）+"-"+当日的流水号（1开始，不足4位的用0补齐）
	 */
	public synchronized String getBatchNo(String code){
		String curDate = DateUtils.getDate("yyyyMMdd");
		Integer value = genBatchNo(curDate,code);
		String result =curDate+"-"+ "0000".substring(0,4-value.toString().length())+value;
		return result;
	}
	
	/*
	 * 签证批量还收据批次号
	 * wxw added 2015-03-30
	 */
	public synchronized String getVisaReturnReceiptBatchNo(){
		String curDate = DateUtils.getDate("yyyyMMdd");
		String code = SysBatchNo.CODE_VISA_RETURNRECEIPT_BACTCH;
		Integer value = genBatchNo(curDate,code);
		String result =curDate+"-"+ "0000".substring(0,4-value.toString().length())+value;
		return result;
	}
	
	/*
	 * 签证批量处理面签通知批次号
	 * wxw added 2015-03-30
	 */
	public synchronized String getVisaInterviewNoticeBatchNo(){
		String curDate = DateUtils.getDate("yyyyMMdd");
		String code = SysBatchNo.CODE_VISA_INTERVIEW_NOTICE_BACTCH;
		Integer value = genBatchNo(curDate,code);
		String result =curDate+"-"+ "0000".substring(0,4-value.toString().length())+value;
		return result;
	}

	/*
	 * 签证批量修改担保类型批次号
	 */
	public synchronized String getVisaGuaranteeBatchNo(){
		String curDate = DateUtils.getDate("yyyyMMdd");
		String code = SysBatchNo.CODE_VISA_GUARANTEE_BACTCH;
		Integer value = genBatchNo(curDate,code);
		String result =curDate+"-"+ "0000".substring(0,4-value.toString().length())+value;
		return result;
	}
	
	/*
	 * 生成批次号
	 * @param curDate 日期
	 * @param code 主键
	 */
	public Integer genBatchNo(String curDate,String code){
		Integer value = 1;
		SysBatchNo batchNo = sysBatchNoDao.findByKeyAndDate(curDate,code);
		if(batchNo==null){
			batchNo= new SysBatchNo();
			batchNo.setCurDate(curDate);
			batchNo.setCode(code);
			batchNo.setValue(2);
			this.save(batchNo);
		}else{
			value = batchNo.getValue();
			batchNo.setValue(value+1);
			this.update(batchNo);
		}
		return value;
	}
	

	private void update(SysBatchNo batchNo) {
		// TODO Auto-generated method stub
		sysBatchNoDao.getSession().update(batchNo);
		sysBatchNoDao.getSession().flush();
	}

	private void save(SysBatchNo batchNo) {
		// TODO Auto-generated method stub
		sysBatchNoDao.getSession().save(batchNo);
		sysBatchNoDao.getSession().flush();
	}
}


