package com.trekiz.admin.review.borrowing.airticket.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class NewAirTicketOrderLendMoneyService {
	
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;
	
	/**
	 * 审批通过后 保存 借款相关信息到MoneyAmount表中.将review_detail中总币种、总借款金额、对应费率进行拆分，
	 * 每一种币种对应的保存到MoneyAmount一条记录，款项类型 为12，多个游客以团队处理保存订单号，一个游客保存的是游客ID 
	 * 
	 * 业务类型全部修改成1,（1：订单，2：游客） add by songyang 2015年11月5日19:37:45
	 * @param reviewId 流程ID
	 * @param orderId 订单ID
	 * @param orderType 产品订单类型 1-7
	 * @return 返回true表示保存成功，false保存失败
	 * 
	 */
	public boolean saveLendMoney2MoneyAmount(String reviewId,String orderId,int orderType){
		boolean flag = true;
		User user =   UserUtils.getUser();
		List<MoneyAmount> malist = new ArrayList<MoneyAmount>(); 
		MoneyAmount ma = null;
		Map<String,Object> processMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		List<Map<String,Object>>  rdlist = new ArrayList<Map<String,Object>>();
		rdlist.add(processMap);
		if(rdlist!=null&&rdlist.size()>0){
			for(int i=0;i<rdlist.size();i++){//先确定生成的MoneyAmount的记录的数目
				if(rdlist.get(i).containsKey("currencyIds")){
					String currencyIds = rdlist.get(i).get("currencyIds").toString();
					if(currencyIds!=null&&!"".equals(currencyIds)){
						String[] currencyIdArray = currencyIds.split(BorrowingBean.REGEX);
						
						for(int j=0;j<currencyIdArray.length;j++){
							if(malist.size()<currencyIdArray.length){
								ma = new MoneyAmount();
								malist.add(ma);
							}
							malist.get(j).setCurrencyId(Integer.valueOf(currencyIdArray[j]));
						}
					}
					break;
				}
				
			}
			for(int i=0;i<rdlist.size();i++){
				if(rdlist.get(i).containsKey("borrowPrices")){
					String borrowPrices = rdlist.get(i).get("borrowPrices").toString();
					if(borrowPrices!=null&&!"".equals(borrowPrices)){
						String[] borrowPriceArray = borrowPrices.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setAmount(new BigDecimal(borrowPriceArray[j]));
						}
					}
				}
				
				if(rdlist.get(i).containsKey("currencyExchangerates")){
					String currencyExchangerates = rdlist.get(i).get("currencyExchangerates").toString();
					if(currencyExchangerates!=null&&!"".equals(currencyExchangerates)){
						String[] currencyExchangerateArray = currencyExchangerates.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setExchangerate(new BigDecimal(currencyExchangerateArray[j]));
						}
					}
				}
				
				if(rdlist.get(i).containsKey("travelerId")){//多个游客的话，就按团队处理
					String travelerId = rdlist.get(i).get("travelerId").toString();
					if(travelerId!=null&&!"".equals(travelerId)){
						String[] travelIdArray = travelerId.split(BorrowingBean.REGEX);
						if(travelIdArray.length==1 && !"0".equals(travelIdArray[0])){
							
							for(int j = 0;j<malist.size();j++){
								malist.get(j).setUid(Long.valueOf(travelerId));
								malist.get(j).setBusindessType(2);
							}
						}else{
							for(int j = 0;j<malist.size();j++){
								malist.get(j).setUid(Long.valueOf(orderId));
								malist.get(j).setBusindessType(1);
							}
						}
					}else{
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setUid(Long.valueOf(orderId));
							malist.get(j).setBusindessType(1);
						}
					}
				}
			}
		}
		
		for(int j=0;j<malist.size();j++){
			MoneyAmount moneyAmount =  malist.get(j);
			moneyAmount.setSerialNum(UUID.randomUUID().toString());
			moneyAmount.setOrderType(orderType);
			moneyAmount.setCreatedBy(user.getId());
			moneyAmount.setCreateTime(new Date());
			moneyAmount.setMoneyType(12);
			moneyAmount.setDelFlag("0");
//			moneyAmount.setReviewId(new Long(1));
			moneyAmount.setReviewUuid(reviewId);
			//业务类型全部修改成1,（1：订单，2：游客）并同时把orderId保存； update by zhanghao 20150522 
			moneyAmount.setBusindessType(1);
			moneyAmount.setUid(Long.valueOf(orderId));
			
			moneyAmountDao.save(moneyAmount);
			
		}
		
		return flag ;
	}
}
