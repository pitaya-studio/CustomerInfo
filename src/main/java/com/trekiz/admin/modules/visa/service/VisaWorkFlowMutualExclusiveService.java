package com.trekiz.admin.modules.visa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;

@Service
@Transactional(readOnly = true)
public class VisaWorkFlowMutualExclusiveService extends BaseService {
	
	@Autowired
	private ReviewService reviewService;
	
	
	
	/**
	 * 执行流程互斥：
	 * 进行借款、退团... 等操作或申请时弹出流程互斥的信息后，点击确认后执行该方法
	 * 取消相应的审核流程
	 * 
	 * @param travelerId 游客id 
	 * @param exclusiveType  1:撤签，2:退团 ....
	 * @return true:互斥执行成功;false:互斥执行失败
	 */
	public boolean excuteVisaWorkFlowMutualExclusive(long travelerId,int exclusiveType){

		
		Traveler traveler = travelerService.findTravelerById(travelerId);
		boolean flag = false;
		//签证相关的流程类型
		//1.撤签
		Integer[] flowTypes4cq = {1,10,6,7,4,5,13}; 
		List<Integer> flowType4cq = new ArrayList<Integer>();
		for (int i = 0; i < flowTypes4cq.length; i++) {
			flowType4cq.add(flowTypes4cq[i]);
		}
		
		
		//2.退团
		Integer[] flowTypes4tt = {8}; 
		List<Integer> flowType4tt = new ArrayList<Integer>();
		for (int i = 0; i < flowTypes4tt.length; i++) {
			flowType4tt.add(flowTypes4tt[i]);
		}
		
	
		//1.撤签
		if (1==exclusiveType) {
			
			if (null!=traveler) {
				List<Review> reviews = reviewDao.findReview4WithdrawalVisa(6, flowType4cq, traveler.getOrderId(), ""+traveler.getId());
				for (Review review : reviews) {
					reviewDao.removeMyReview(review.getId());
				}
				flag=true;
			}
		}
		//2.退团
		else if(2==exclusiveType) {
			
			if (null!=traveler) {
				List<Review> reviews = reviewDao.findReview4WithdrawalVisa(6, flowType4tt, traveler.getOrderId(), ""+traveler.getId());
				for (Review review : reviews) {
					reviewDao.removeMyReview(review.getId());
				}
				flag=true;
			}
		}
	
		return flag;
	}
	
	
	
	
	
/*	 *//** 审核流程类型：退款1*//*
    public final static Integer REVIEW_FLOWTYPE_REFUND = 1;
    *//** 审核流程类型：退票3*//*
    public final static Integer REVIEW_FLOWTYPE_AIRTICKET_RETURN = 3;
    *//** 审核流程类型：签证押金转担保6*//*
    public final static Integer REVIEW_FLOWTYPE_DEPOSITTOWARRANT = 6;
    *//** 审核流程类型：退签证押金7*//*
    public final static Integer REVIEW_FLOWTYPE_DEPOSITE_REFUND = 7;
    *//** 审核流程类型：退团 8*//*
    public final static Integer REVIEW_FLOWTYPE_EXIT_GROUP = 8;
    *//** 审核流程类型：返佣 9*//*
    public final static Integer REBATES_FLOW_TYPE = 9;
    *//** 审核流程类型：改价10*//*
    public final static Integer REVIEW_FLOWTYPE_CHANGE_PRICE = 10;
    *//** 审核流程类型：转团  11*//*
    public final static Integer REVIEW_FLOWTYPE_TRANSFER_GROUP = 11;
    *//** 审核流程类型：转款  12*//*
    public final static Integer REVIEW_FLOWTYPE_TRANSFER_MONEY = 12;   
    *//** 审核流程类型：还签证收据  4*//*
    public final static Integer REVIEW_FLOWTYPE_VISA_RETURNRECEIPT= 4;
    *//** 审核流程类型：签证借款  5*//*
    public final static Integer REVIEW_FLOWTYPE_VISA_BORROWMONEY = 5;
    *//** 审核流程类型：还签证押金收据 13*//*
    public final static Integer REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT =13;*/
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private TravelerService travelerService;
	
	/**
	 * 处理签证相关流程互斥：
	 * 提供借款、退团... 等操作或申请时流程互斥的提示信息
	 * 
	 * @param travelerId 游客id 
	 * @param exclusiveType  1:撤签，2:退团 ....
	 * @return 返回null
	 */
	public String getVisaWorkFlowMutualExclusiveInfo(long travelerId,int exclusiveType){
		
		Traveler traveler = travelerService.findTravelerById(travelerId);
		//签证相关的流程类型
		//1.撤签
		Integer[] flowTypes4cq = {1,10,6,7,4,5,13}; 
		List<Integer> flowType4cq = new ArrayList<Integer>();
		for (int i = 0; i < flowTypes4cq.length; i++) {
			flowType4cq.add(flowTypes4cq[i]);
		}
		
		
		//2.退团
		Integer[] flowTypes4tt = {8}; 
		List<Integer> flowType4tt = new ArrayList<Integer>();
		for (int i = 0; i < flowTypes4tt.length; i++) {
			flowType4tt.add(flowTypes4tt[i]);
		}
		
		String result = null;
		StringBuilder sb = null;
		//1.撤签
		if (1==exclusiveType) {
			sb = new StringBuilder();
			if (null!=traveler) {
				List<Review> reviews = reviewDao.findReview4WithdrawalVisa(6, flowType4cq, traveler.getOrderId(), ""+traveler.getId());
				for (Review review : reviews) {
					/*
					 * 1:退款   10:改价  6:签证押金转担保7:退签证押金
					 * 4:还签证收据 5:签证借款   13:还签证押金收据
					 */
					if (1==review.getFlowType()) {
						sb.append("退款,");
					}else if (10==review.getFlowType()) {
						sb.append("改价,");
					}else if (6==review.getFlowType()) {
						sb.append("签证押金转担保,");
					}else if (7==review.getFlowType()) {
						sb.append("退签证押金,");
					}else if (4==review.getFlowType()) {
						sb.append("还签证收据,");
					}else if (5==review.getFlowType()) {
						sb.append("签证借款,");
					}else if (13==review.getFlowType()) {
						sb.append("还签证押金收据,");
					}
				}
				result =removeSameStringINItsself(sb.toString());
			}
		}
		//2.退团
		else if(2==exclusiveType) {
			sb = new StringBuilder();
			if (null!=traveler) {
				List<Review> reviews = reviewDao.findReview4WithdrawalVisa(6, flowType4tt, traveler.getOrderId(), ""+traveler.getId());
				for (Review review : reviews) {
					/*
					 * 8:退团
					 */
					if (10==review.getFlowType()) {
						sb.append("改价,");
					}
				}
				result =removeSameStringINItsself(sb.toString());
			}
		}
		return result;
	}
	
	/**
	 * 去掉重复的流程类型名称
	 * @param rawString
	 * @return
	 */
	private String  removeSameStringINItsself(String rawString){
		String rs = null;
		if (null!=rawString) {
			List<String> flNames = new ArrayList<String>();
			String[] flowtypeNames = rawString.split(",");
			for (String string : flowtypeNames) {
				if (!flNames.contains(string)) {
					flNames.add(string);
				}
			}
			StringBuilder sb  =  new StringBuilder();
			for (String flName : flNames) {
				sb.append(flName).append(",");
			}
			rs = sb.toString();
		}
//		System.out.println(rs);
		return rs;
	}
	
	
	
	
	public static void main(String[] args) {
		VisaWorkFlowMutualExclusiveService vs = new VisaWorkFlowMutualExclusiveService();
//		vs.removeSameStringINItsself(null);
	}
	
}
