package com.trekiz.admin.modules.cost.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cost.repository.IPaymanagerDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.query.HotelMoneyAmountQuery;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.query.IslandMoneyAmountQuery;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 付款service
 * 
 * @author Administrator 2015年5月13日20:32:02
 */
@Service
public class PayManagerService extends BaseService {

	@Autowired
	private IPaymanagerDao paymanagerDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RefundDao refundDao;

	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
    private DocInfoService docInfoService;
	@Autowired
	private ReviewDao reviewDao;

	/**
	 * 查询审核通过的支付列表
	 */
	public Page<Map<String, Object>> getReviewPayList(
			Map<String, Object> params) {
		return paymanagerDao.getReviewPayList(params);
	}

	/**
	 * 查询审核通过的返佣支付列表
	 */
	public Page<Map<String, Object>> findRebateList(
			Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		
		Page<Map<String, Object>> page = paymanagerDao.getRebatePayList(map,
				request, response);
		return page;
	}

	/**
	 * 查询借款列表数据
	 * 
	 * @param params
	 * @param page
	 * @return
	 */
	public Page<Map<String, Object>> findBorrowMoneyList(
			Map<String, String> params, Page<Map<String, Object>> page) {
		Long companyId = UserUtils.getUser().getCompany().getId();
//		String orderTypeStr = params.get("orderType");
//		Integer orderType = null;
//		if(StringUtils.isNotBlank(orderTypeStr)){
//			orderType = Integer.parseInt(orderTypeStr);
//		}
		params.put("companyId", String.valueOf(companyId));
		/*注释掉了 这些分类查询 在总的查询中增加了过滤条件产品类型  by chy 2015年6月15日16:03:56*/
//		if("".equals(orderTypeStr) || null == orderTypeStr || orderType == 0){
			return paymanagerDao.findBorrowMoneyListAll(params, page);
//		}else if (Context.ORDER_TYPE_QZ == orderType) {
//			return paymanagerDao.findBorrowMoneyListQZ(params, page);
//		} else if (Context.ORDER_TYPE_JP == orderType) {
//			return paymanagerDao.findBorrowMoneyListJP(params, page);
//		} else {
//			return paymanagerDao.findBorrowMoneyListDT(params, page);
//		}
	}
	
	/**
	 * 专门用于环球行供应商签证借款
	 * @param params	参数
	 * @param companyId 签证国家
	 * @param page      存储数据信息
	 * @return
	 * @author shijun.liu
	 */
	public Page<Map<String, Object>> borrowMoneyForTTSQZ(Map<String, String> params, Long companyId,
			Page<Map<String, Object>> page){
		return paymanagerDao.findBorrowMoneyListTTSQZ(params, companyId, page);
	}

	/**
	 * 根据reviewId查询付款已付数据
	 *  酒店
	 * @param reviewId
	 * @return
	 */
	public List<HotelMoneyAmount> getHotelPayedMoney(Long reviewId, Integer paytype) {
		
		List<HotelMoneyAmount> result = new ArrayList<HotelMoneyAmount>();
		// 根据reviewId查询付款记录
		List<Refund> list = refundDao.findByRecordId(reviewId, paytype);
		List<HotelMoneyAmount> hotelAmounts = new ArrayList<HotelMoneyAmount>();
		for (Refund temp : list) {
				HotelMoneyAmountQuery hotelMoneyAmountQuery = new HotelMoneyAmountQuery();
				hotelMoneyAmountQuery.setSerialNum(temp.getMoneySerialNum());
				hotelAmounts.addAll(hotelMoneyAmountService.find(hotelMoneyAmountQuery));
		}
		Set<Integer> curSet = new HashSet<Integer>();
		for(HotelMoneyAmount temp : hotelAmounts) {
			if(curSet.contains(temp.getCurrencyId())){
				for(HotelMoneyAmount temp2 : result){
					if(temp2.getCurrencyId().intValue() == temp.getCurrencyId().intValue()){
						temp2.setAmount(temp2.getAmount() + temp.getAmount());
					}
				}
				continue;
			}
			curSet.add(temp.getCurrencyId());
			result.add(temp);
		}
		return result;
	}

	/**
	 * 根据reviewId查询付款已付数据
	 * 海岛游
	 * @param reviewId
	 * @return
	 */
	public List<IslandMoneyAmount> getIslandPayedMoney(Long reviewId, Integer paytype) {
		
		List<IslandMoneyAmount> result = new ArrayList<IslandMoneyAmount>();
		// 根据reviewId查询付款记录
		List<Refund> list = refundDao.findByRecordId(reviewId, paytype);
		List<IslandMoneyAmount> islandAmounts = new ArrayList<IslandMoneyAmount>();
		for (Refund temp : list) {
			IslandMoneyAmountQuery islandMoneyAmountQuery = new IslandMoneyAmountQuery();
			islandMoneyAmountQuery.setSerialNum(temp.getMoneySerialNum());
			islandAmounts.addAll(islandMoneyAmountService.find(islandMoneyAmountQuery));
		}
		Set<Integer> curSet = new HashSet<Integer>();
		for(IslandMoneyAmount temp : islandAmounts) {
			if(curSet.contains(temp.getCurrencyId())){
				for(IslandMoneyAmount temp2 : result){
					if(temp2.getCurrencyId().intValue() == temp.getCurrencyId().intValue()){
						temp2.setAmount(temp2.getAmount() + temp.getAmount());
					}
				}
				continue;
			}
			curSet.add(temp.getCurrencyId());
			result.add(temp);
		}
		return result;
	}
	
	/**
	 * 根据reviewId查询付款已付数据
	 * 
	 * @param reviewId
	 * @return
	 */
	public List<MoneyAmount> getPayedMoney(Long reviewId, Integer paytype) {
		
		List<MoneyAmount> result = new ArrayList<MoneyAmount>();
		// 根据reviewId查询付款记录
		List<Refund> list = refundDao.findByRecordId(reviewId, paytype);
		List<MoneyAmount> amounts = new ArrayList<MoneyAmount>();
		for (Refund temp : list) {
			amounts.addAll(moneyAmountService.findAmountBySerialNum(temp.getMoneySerialNum()));
		}
		Set<Integer> curSet = new HashSet<Integer>();
		for(MoneyAmount temp : amounts) {
			if(curSet.contains(temp.getCurrencyId())){
				for(MoneyAmount temp2 : result){
					if(temp2.getCurrencyId().intValue() == temp.getCurrencyId().intValue()){
						temp2.setAmount(temp2.getAmount().add(temp.getAmount()));
					}
				}
				continue;
			}
			curSet.add(temp.getCurrencyId());
			result.add(temp);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserList(String hql) {
		return userDao.getSession().createSQLQuery(hql).list();
	}
	
	/**
	 * 环球行签证借款支付记录
	 * @param batchId
	 * @param orderType
	 * @author  shijun.liu
	 */
	@Deprecated
	public String getTTSQZPayRecord(Integer batchId, Integer orderType){
		String json = null;
		List<Map<String, Object>> list = paymanagerDao.getTTSQZPayRecord(batchId, orderType);
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map : list){
				String payVoucher = (String)map.get("payVoucher");
				StringBuffer name = new StringBuffer();
				if(StringUtils.isNotBlank(payVoucher)){
					List<DocInfo> docInfoList = docInfoService.getDocInfoBydocids(payVoucher);
					for(DocInfo docInfo:docInfoList){
						name.append(docInfo.getDocName()).append("|");
					}
					int len = name.toString().length();
					if(len != 0){
						name.delete(len-1, len);
					}
				}
				map.put("payVoucher", name.toString());
			}
			json = JSONObject.toJSONString(list);
		}
		return json;
	}
	
	/**
	 * 查询借款付款还未付款的数目
	 * @return
	 */
	public long getBorrowMoneyNotPayedCount(){
		long count = 0;
		Long companyId = UserUtils.getUser().getCompany().getId();
		count = paymanagerDao.getBorrowMoneyNotPayedCount(companyId);
		return count;
	}

	/**
	 * 查询签证批量借款付款未付款的数目
	 * @return
	 */
	public long getBatchBorrowMoneyNotPayedCount(){
		long count = 0;
		Long companyId = UserUtils.getUser().getCompany().getId();
		count = paymanagerDao.getBorrowMoneyNotPayedCountForTTS(companyId);
		return count;
	}
	/**
	 * 退款付款，返佣付款，借款付款(除环球行)付款成功之后更新review表的updateDate字段信息
	 * @param orderPayForm
	 * @author shijun.liu
	 */
	public void updateReviewUpdateDate(OrderPayForm orderPayForm){
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long reviewId = orderPayDetail.getProjectId();
		if (reviewId != null) {
			Review review = reviewDao.findOne(reviewId);
			review.setUpdateBy(UserUtils.getUser().getId());
			review.setUpdateDate(new Date());
			//新增 更新出纳确认状态 根据用户配置 start add by chy 2015-10-29 17:40:39
//			if(0 == UserUtils.getUser().getCompany().getConfirmPay()){
//				review.setPayStatus(1);
//			}
			//新增 更新出纳确认状态 根据用户配置 end
			reviewDao.save(review);
		}
	}
	
	/**
	 * 借款付款(环球行)付款成功之后更新visa_flow_batch_opration表的update_time字段信息
	 * @param orderPayForm
	 * @author shijun.liu
	 */
	public void updateBatchBorrowMoneyUpdateTime(OrderPayForm orderPayForm){
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long batchId = orderPayDetail.getProjectId();
		if (batchId != null) {
			String sql = "update visa_flow_batch_opration set update_time=?, updateBy=? where id=?";
			reviewDao.updateBySql(sql, new Date(), UserUtils.getUser().getId(), batchId);
		}
	}
}
