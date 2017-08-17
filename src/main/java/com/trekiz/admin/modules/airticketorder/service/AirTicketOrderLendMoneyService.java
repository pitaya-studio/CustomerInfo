package com.trekiz.admin.modules.airticketorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class AirTicketOrderLendMoneyService {
	
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;
	public List<Byte> validateReviewStatus(int id) {
		String sql ="  select r.status AS status FROM review r  where  r.active = '1' and r.id='"+id+"'";
		return reviewDao.findBySql(sql);
	}
	
	public int cancelBorrowReview(int id) {
		String sql ="update  review r  set  r.active = '0',r.status = 4 where r.id='"+id+"'";
		return reviewDao.updateBySql(sql);
	}
	/**
	 * 流程互斥判断 返回值如果大于零，就是存在互斥
	 */
	public int validateProcess(int id) {
		String sql ="SELECT COUNT(id) as total FROM review r WHERE r.active = 1 AND r.status =1 AND  r.flowType =19 AND r.productType =7 AND r.orderId ='"+id+"'";
       //FIXME 限制条件需要更新？
		int total = 0;
		if (CollectionUtils.isNotEmpty(reviewDao.findBySql(sql))){
			total = Integer.parseInt(reviewDao.findBySql(sql).get(0).toString());
		}
		return total;
	}
	
	/**
	 * 审核通过后 保存 借款相关信息到MoneyAmount表中.将review_detail中总币种、总借款金额、对应费率进行拆分，
	 * 每一种币种对应的保存到MoneyAmount一条记录，款项类型 为12，多个游客以团队处理保存订单号，一个游客保存的是游客ID 
	 * 
	 * 业务类型全部修改成1,（1：订单，2：游客） update by zhanghao 20150522
	 * @param reviewId 流程ID
	 * @param orderId 订单ID
	 * @param orderType 产品订单类型 1-7
	 * @return 返回true表示保存成功，false保存失败
	 * 
	 */
	public boolean saveLendMoney2MoneyAmount(Long reviewId,String orderId,int orderType){
		boolean flag = true;
		User user =   UserUtils.getUser();
		List<MoneyAmount> malist = new ArrayList<MoneyAmount>(); 
		MoneyAmount ma = null;
		List<ReviewDetail> rdlist = reviewDetailDao.findReviewDetail(reviewId);
		if(rdlist!=null&&rdlist.size()>0){
			for(int i=0;i<rdlist.size();i++){//先确定生成的MoneyAmount的记录的数目
				if("currencyIds".equals(rdlist.get(i).getMykey())){
					String currencyIds = rdlist.get(i).getMyvalue();
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
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					String borrowPrices = rdlist.get(i).getMyvalue();
					if(borrowPrices!=null&&!"".equals(borrowPrices)){
						String[] borrowPriceArray = borrowPrices.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setAmount(new BigDecimal(borrowPriceArray[j]));
						}
					}
				}
				
				if("currencyExchangerates".equals(rdlist.get(i).getMykey())){
					String currencyExchangerates = rdlist.get(i).getMyvalue();
					if(currencyExchangerates!=null&&!"".equals(currencyExchangerates)){
						String[] currencyExchangerateArray = currencyExchangerates.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setExchangerate(new BigDecimal(currencyExchangerateArray[j]));
						}
					}
				}
				
				if("travelerId".equals(rdlist.get(i).getMykey())){//多个游客的话，就按团队处理
					String travelerId = rdlist.get(i).getMyvalue();
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
			moneyAmount.setReviewId(reviewId);
			//业务类型全部修改成1,（1：订单，2：游客）并同时把orderId保存； update by zhanghao 20150522 
			moneyAmount.setBusindessType(1);
			moneyAmount.setUid(Long.valueOf(orderId));
			
			moneyAmountDao.save(moneyAmount);
			
		}
		
		return flag ;
	}
	public boolean saveHotelLendMoney2MoneyAmount(Long reviewId,String orderId,int orderType,String orderUuid){
		boolean flag = true;
		User user =   UserUtils.getUser();
		List<HotelMoneyAmount> malist = new ArrayList<HotelMoneyAmount>(); 
		HotelMoneyAmount ma = null;
		List<ReviewDetail> rdlist = reviewDetailDao.findReviewDetail(reviewId);
		if(rdlist!=null&&rdlist.size()>0){
			for(int i=0;i<rdlist.size();i++){//先确定生成的MoneyAmount的记录的数目
				if("currencyIds".equals(rdlist.get(i).getMykey())){
					String currencyIds = rdlist.get(i).getMyvalue();
					if(currencyIds!=null&&!"".equals(currencyIds)){
						String[] currencyIdArray = currencyIds.split(BorrowingBean.REGEX);
						
						for(int j=0;j<currencyIdArray.length;j++){
							if(malist.size()<currencyIdArray.length){
								ma = new HotelMoneyAmount();
								malist.add(ma);
							}
							malist.get(j).setCurrencyId(Integer.valueOf(currencyIdArray[j]));
						}
					}
					break;
				}
				
			}
			for(int i=0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					String borrowPrices = rdlist.get(i).getMyvalue();
					if(borrowPrices!=null&&!"".equals(borrowPrices)){
						String[] borrowPriceArray = borrowPrices.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setAmount(Double.valueOf(borrowPriceArray[j]));
						}
					}
				}
				
				if("currencyExchangerates".equals(rdlist.get(i).getMykey())){
					String currencyExchangerates = rdlist.get(i).getMyvalue();
					if(currencyExchangerates!=null&&!"".equals(currencyExchangerates)){
						String[] currencyExchangerateArray = currencyExchangerates.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setExchangerate(Double.valueOf(currencyExchangerateArray[j]));
						}
					}
				}
				
//				if("travelerId".equals(rdlist.get(i).getMykey())){//多个游客的话，就按团队处理
//					String travelerId = rdlist.get(i).getMyvalue();
//					if(travelerId!=null&&!"".equals(travelerId)){
//						String[] travelIdArray = travelerId.split(BorrowingBean.REGEX);
//						if(travelIdArray.length==1 && !"0".equals(travelIdArray[0])){
//							
//							for(int j = 0;j<malist.size();j++){
//								malist.get(j).setUid(Long.valueOf(travelerId));
//								malist.get(j).setBusindessType(2);
//							}
//						}else{
//							for(int j = 0;j<malist.size();j++){
//								malist.get(j).setUid(Long.valueOf(orderId));
//								malist.get(j).setBusindessType(1);
//							}
//						}
//					}else{
//						for(int j = 0;j<malist.size();j++){
//							malist.get(j).setUid(Long.valueOf(orderId));
//							malist.get(j).setBusindessType(1);
//						}
//					}
//				}
			}
		}
		
		for(int j=0;j<malist.size();j++){
			HotelMoneyAmount moneyAmount =  malist.get(j);
			moneyAmount.setSerialNum(UUID.randomUUID().toString());
			moneyAmount.setCreateBy(user.getId().intValue());
			moneyAmount.setCreateDate(new Date());
			moneyAmount.setMoneyType(12);
			moneyAmount.setDelFlag("0");
			moneyAmount.setReviewId(reviewId.intValue());
			//业务类型全部修改成1,（1：订单，2：游客）并同时把orderId保存； update by zhanghao 20150522 
			moneyAmount.setBusinessType(1);
			moneyAmount.setBusinessUuid(orderUuid);
			
			hotelMoneyAmountDao.saveObj(moneyAmount);
			
		}
		
		return flag ;
	}
	
	public boolean saveIslandLendMoney2MoneyAmount(Long reviewId,String orderId,int orderType,String orderUuid){
		boolean flag = true;
		User user =   UserUtils.getUser();
		List<IslandMoneyAmount> malist = new ArrayList<IslandMoneyAmount>(); 
		IslandMoneyAmount ma = null;
		List<ReviewDetail> rdlist = reviewDetailDao.findReviewDetail(reviewId);
		if(rdlist!=null&&rdlist.size()>0){
			for(int i=0;i<rdlist.size();i++){//先确定生成的MoneyAmount的记录的数目
				if("currencyIds".equals(rdlist.get(i).getMykey())){
					String currencyIds = rdlist.get(i).getMyvalue();
					if(currencyIds!=null&&!"".equals(currencyIds)){
						String[] currencyIdArray = currencyIds.split(BorrowingBean.REGEX);
						
						for(int j=0;j<currencyIdArray.length;j++){
							if(malist.size()<currencyIdArray.length){
								ma = new IslandMoneyAmount();
								malist.add(ma);
							}
							malist.get(j).setCurrencyId(Integer.valueOf(currencyIdArray[j]));
						}
					}
					break;
				}
				
			}
			for(int i=0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					String borrowPrices = rdlist.get(i).getMyvalue();
					if(borrowPrices!=null&&!"".equals(borrowPrices)){
						String[] borrowPriceArray = borrowPrices.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setAmount(Double.valueOf(borrowPriceArray[j]));
						}
					}
				}
				
				if("currencyExchangerates".equals(rdlist.get(i).getMykey())){
					String currencyExchangerates = rdlist.get(i).getMyvalue();
					if(currencyExchangerates!=null&&!"".equals(currencyExchangerates)){
						String[] currencyExchangerateArray = currencyExchangerates.split(BorrowingBean.REGEX);
						for(int j = 0;j<malist.size();j++){
							malist.get(j).setExchangerate(Double.valueOf(currencyExchangerateArray[j]));
						}
					}
				}
				
//				if("travelerId".equals(rdlist.get(i).getMykey())){//多个游客的话，就按团队处理
//					String travelerId = rdlist.get(i).getMyvalue();
//					if(travelerId!=null&&!"".equals(travelerId)){
//						String[] travelIdArray = travelerId.split(BorrowingBean.REGEX);
//						if(travelIdArray.length==1 && !"0".equals(travelIdArray[0])){
//							
//							for(int j = 0;j<malist.size();j++){
//								malist.get(j).setUid(Long.valueOf(travelerId));
//								malist.get(j).setBusindessType(2);
//							}
//						}else{
//							for(int j = 0;j<malist.size();j++){
//								malist.get(j).setUid(Long.valueOf(orderId));
//								malist.get(j).setBusindessType(1);
//							}
//						}
//					}else{
//						for(int j = 0;j<malist.size();j++){
//							malist.get(j).setUid(Long.valueOf(orderId));
//							malist.get(j).setBusindessType(1);
//						}
//					}
//				}
			}
		}
		
		for(int j=0;j<malist.size();j++){
			IslandMoneyAmount moneyAmount =  malist.get(j);
			moneyAmount.setSerialNum(UUID.randomUUID().toString());
			moneyAmount.setCreateBy(user.getId().intValue());
			moneyAmount.setCreateDate(new Date());
			moneyAmount.setMoneyType(12);
			moneyAmount.setDelFlag("0");
			moneyAmount.setReviewId(reviewId.intValue());
			//业务类型全部修改成1,（1：订单，2：游客）并同时把orderId保存； update by zhanghao 20150522 
			moneyAmount.setBusinessType(1);
			moneyAmount.setBusinessUuid(orderUuid);
			
			islandMoneyAmountDao.saveObj(moneyAmount);
			
		}
		
		return flag ;
	}
	
}
