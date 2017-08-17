package com.trekiz.admin.review.pay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.quauq.review.core.engine.ReviewService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cost.utils.CostExcelUtils;
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
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.CountryDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.review.pay.bean.TravelerBean;
import com.trekiz.admin.review.pay.dao.IPaymanagerNewDao;

/**
 * 付款service
 * 
 * @author Administrator 2015年5月13日20:32:02
 */
@Service
public class PayManagerNewService extends BaseService {

	@Autowired
	private IPaymanagerNewDao paymanagerDao;

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
	private ReviewService reviewService;
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	
	@Autowired
	private CountryDao countryDao;

	/**
	 * 查询审核通过的支付列表
	 */
	public Page<Map<String, Object>> getReviewPayList(
			Map<String, Object> params) {
		Page<Map<String, Object>> page = paymanagerDao.getReviewPayList(params);
		/*填充变量数据*/
		List<Map<String, Object>> list = page.getList();
		Object reviewid;
		Object reviewflag;
		for(Map<String, Object> map : list){
			reviewid = map.get("revid");
			reviewflag = map.get("reviewflag");
			if(reviewid == null || "".equals(reviewid.toString()) || reviewflag == null || "1".equals(reviewflag)){
				continue;
			}
			List<ReviewLogNew> reviewLogs = ReviewUtils.getHavingRemarkReviewLogs(reviewid.toString());
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewid.toString());
			map.putAll(reviewMap);
			map.put("reviewLogs",reviewLogs);
		}
		page.setList(list);
		return page;
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
		params.put("companyId", String.valueOf(companyId));
		return paymanagerDao.findBorrowMoneyListAll(params, page);
	}
	
	/**
	 * 专门用于环球行供应商签证借款
	 * @param params	参数
	 * @param page      存储数据信息
	 * @return
	 * @author shijun.liu
	 */
	public Page<Map<String, Object>> borrowMoneyForTTSQZ(Map<String, String> params, Page<Map<String, Object>> page){
		return paymanagerDao.findBorrowMoneyListTTSQZ(params, page);
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
	 * 环球行供应商签证借款-->确认或者取消付款
	 * @param batchId	   批次ID
	 * @param payStatus	    是否付款
	 * @author shijun.liu
	 */
	public String confimOrCancelTTSQZPay(Long batchId, Integer payStatus){
		String flag = "success";
		try {
			if(null != batchId){
				Long currentUserId = UserUtils.getUser().getId();
				VisaFlowBatchOpration visaBatch = visaFlowBatchOprationDao.findOne(batchId);
				if(null != visaBatch){
					if(2 == visaBatch.getIsNewReview()){
						reviewService.batchUpdatePayStatus(visaBatch.getBatchNo(), String.valueOf(currentUserId), 
								new Date(), payStatus);
					}else{
						paymanagerDao.confimOrCancelTTSQZPay(visaBatch.getBatchNo(), payStatus);
					}
				}
			}
		} catch (RuntimeException e) {
			flag = "fail";
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 查询借款付款还未付款的数目
	 * @return
	 */
	public long getBorrowMoneyNotPayedCount(){
		long count = 0;
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId == 68){
			count = paymanagerDao.getBorrowMoneyNotPayedCountForTTS(companyId);
		}else{
			count = paymanagerDao.getBorrowMoneyNotPayedCount(companyId);
		}
		return count;
	}
	
	/**
	 * 退款付款，返佣付款，借款付款(除环球行)付款成功之后更新review表的updateDate字段信息
	 * @param orderPayForm
	 * @author shijun.liu
	 */
	public void updateReviewUpdateDate(OrderPayForm orderPayForm){
/*		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long reviewId = orderPayDetail.getProjectId();
		if (reviewId != null) {
			ReviewNew review = reviewDao.findByIdLongAndDelFlag(reviewId, 0);
			review.setUpdateBy(UserUtils.getUser().getId().toString());
			review.setUpdateDate(new Date());
			reviewDao.save(review);
		}*/
	}
	
	/**
	 * 借款付款(环球行)付款成功之后更新review_new表的update_date字段信息
	 * @param orderPayForm
	 * @author shijun.liu
	 */
	public void updateBatchBorrowMoneyUpdateTime(OrderPayForm orderPayForm){
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long batchId = orderPayDetail.getProjectId();
		Long currentUserId = UserUtils.getUser().getId();
		if (batchId != null) {
			VisaFlowBatchOpration batchBean = visaFlowBatchOprationDao.getById(batchId);
			String batchNo = batchBean.getBatchNo();
			int isNew = batchBean.getIsNewReview();
			if(2 == isNew){
				reviewService.batchUpdate(batchNo, String.valueOf(currentUserId), new Date());
			}else{
				StringBuffer str = new StringBuffer();
				str.append(" update review r set r.updateBy = ?, r.updateDate = ? where ")
				   .append(" r.id in (select d.review_id from review_detail d where d.myKey = 'visaBorrowMoneyBatchNo' ")
				   .append(" and d.myValue = ? )");
				refundDao.updateBySql(str.toString(), currentUserId, new Date(), batchNo); 
			}
		}
	}
	
	/**
	 * 借款付款导出游客列表数据
	 * @param params		参数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.18
	 */
	public Workbook downloadTraveler(Map<String, String> params){
		List<Map<String, Object>> list = paymanagerDao.queryTraveler(params);
		List<TravelerBean> travelerList = new ArrayList<TravelerBean>();
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				TravelerBean traveler = new TravelerBean();
				Map<String, Object> map = list.get(i);
				String createDate = map.get("createDate") == null ?"":String.valueOf(map.get("createDate"));
				String groupCode = map.get("groupCode") == null ? "" : String.valueOf(map.get("groupCode"));
				String travelerName = map.get("traveler") == null ? "" : String.valueOf(map.get("traveler"));
				String salerName = map.get("salerName") == null ? "" : String.valueOf(map.get("salerName"));
				String price = map.get("amount") == null ? "" : String.valueOf(map.get("amount"));
				String review = String.valueOf(map.get("reviewType"));
				String sysCountry = map.get("sysCountryId") == null ? "" : String.valueOf(map.get("sysCountryId"));
				String reviewId = map.get("reviewId") == null ? "-1" : String.valueOf(map.get("reviewId"));
				traveler.setPersonNum("1");
				traveler.setCreateDate(createDate);
				traveler.setGroupCode(groupCode);
				traveler.setTraveler(travelerName);
				traveler.setSalerName(salerName);
				traveler.setPersonCount(list.size());
				if("2".equals(review)){
					//新审批借款金额直接取值即可
					String formatPrice = Context.CURRENCY_MARK_RMB + MoneyNumberFormat.getThousandsByRegex(price, 2);
					traveler.setPrice(formatPrice);
				}else{
					//查询旧审批的借款金额(SQL查询不出来，需单独查询)
					List<ReviewDetail> listDetail = reviewDetailDao.findReviewDetail(Long.valueOf(reviewId));
					if (listDetail.size() > 0) {
						for (ReviewDetail detail : listDetail) {
							if("borrowAmount".equals(detail.getMykey())){
								String formatPrice = Context.CURRENCY_MARK_RMB + MoneyNumberFormat.getThousandsByRegex(detail.getMyvalue(), 2);
								traveler.setPrice(formatPrice);
								break;
							}
						}
					}
				}
				if(StringUtils.isNotBlank(sysCountry)){
					//查询签证国家
					Country country = countryDao.getCountryById(Long.valueOf(sysCountry));
					traveler.setVisaCountry(country.getCountryName_cn());
				}else{
					traveler.setVisaCountry("未知国家");
				}
				travelerList.add(traveler);
			}
		}
		Map<String, List<TravelerBean>> map = groupByVisaCountry(travelerList);
		Workbook workbook = CostExcelUtils.createTravelerExcel(map);
		return workbook;
	}
	
	/**
	 * 根据游客所在的签证国家进行分组
	 * @param list			游客列表数据
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.21
	 */
	private Map<String, List<TravelerBean>> groupByVisaCountry(List<TravelerBean> list){
		Map<String, List<TravelerBean>> map = new HashMap<String, List<TravelerBean>>();
		if(null != list && list.size() > 0){
			for (TravelerBean traveler:list) {
				//是否存在某个签证国家的游客
				List<TravelerBean> existsTravelerCountry = map.get(traveler.getVisaCountry());
				if(null == existsTravelerCountry){
					List<TravelerBean> travelerCountry = new ArrayList<TravelerBean>();
					travelerCountry.add(traveler);
					map.put(traveler.getVisaCountry(), travelerCountry);
				}else{
					existsTravelerCountry.add(traveler);
				}
			}
		}
		return map;
	}

	/**
	 * 获取返佣数据
	 * @param reviewUuid
	 * @param prdtype
	 * @return
	 */
	public Map<String, Object> getRebateInfo(String reviewUuid, Integer prdtype) {
		String sql = "";
		if(prdtype == 6 || prdtype == 7) {
			sql = "SELECT rn.id,r.id rebatesId,r.orderType,date_format(rn.create_date,'%Y-%m-%d %H:%i:%s') createDate, rn.group_code groupCode, r.costname, SUM(r.rebatesDiff) rebatesDiff, r.currencyId, r.currencyExchangerate rate, " +
					"(select c.currency_mark from currency c where c.currency_id = r.currencyId) currencyMark " +
					"FROM rebates r, review_new rn where r.rid = rn.id and rn.id = ? group by r.currencyId";
		}else{
			sql = "SELECT rn.id,r.id rebatesId,r.orderType,date_format(rn.create_date,'%Y-%m-%d %H:%i:%s') createDate, rn.group_code groupCode, r.costname, r.rebatesDiff, r.currencyId, r.currencyExchangerate rate, " +
					"(select c.currency_mark from currency c where c.currency_id = r.currencyId) currencyMark " +
					"FROM rebates r, review_new rn where r.rid = rn.id and rn.id = ?";
		}
		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取返佣数据列表
	 * @param reviewUuid
	 * @param prdtype
	 * @return
	 */
	public List<Map<String, Object>> getRebateList(String reviewUuid, Integer prdtype) {
		String sql = "";
		if(prdtype == 6 || prdtype == 7) {
			sql = "SELECT rn.id,r.id rebatesId,r.orderType,date_format(rn.create_date,'%Y-%m-%d %H:%i:%s') createDate, rn.group_code groupCode, r.costname, SUM(r.rebatesDiff) rebatesDiff, r.currencyId, r.currencyExchangerate rate, " +
					"(select c.currency_mark from currency c where c.currency_id = r.currencyId) currencyMark, 2 AS reviewflag " +
					"FROM rebates r, review_new rn where r.rid = rn.id and rn.id = ? group by r.currencyId";
		}else{
			sql = "SELECT rn.id,r.id rebatesId,r.orderType,date_format(rn.create_date,'%Y-%m-%d %H:%i:%s') createDate, rn.group_code groupCode, r.costname, r.rebatesDiff, r.currencyId, r.currencyExchangerate rate, " +
					"(select c.currency_mark from currency c where c.currency_id = r.currencyId) currencyMark,2 AS reviewflag " +
					"FROM rebates r, review_new rn where r.rid = rn.id and rn.id = ?";
		}
		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);
		return list;
	}

	/**
	 * 获取返佣数据
	 * @param reviewUuid
	 * @param prdtype
	 * @return
	 */
	public List<Map<String, Object>> getRebateInfo2(String reviewUuid, Integer prdtype) {
		String sql = "";

		sql = "SELECT rn.id,group_concat(r.id) rebatesId,r.orderType,date_format(rn.create_date,'%Y-%m-%d %H:%i:%s') createDate," +
				" rn.group_code groupCode, r.costname, SUM(r.rebatesDiff) rebatesDiff, r.currencyId, r.currencyExchangerate rate, " +
				"(select c.currency_mark from currency c where c.currency_id = r.currencyId) currencyMark " +
				"FROM rebates r, review_new rn where r.rid = rn.id and rn.id = ? group by r.currencyId";
		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);

		return list;
	}
	
	public Map<String, Object> getOldRebateInfo(String reviewUuid, String prdtype) {
		String sql =  "";
		if("6".equals(prdtype) || "7".equals(prdtype)) {
			sql =  "select r.id, m.orderType orderType, case m.orderType when 6 then '签证返佣' when 7 then '机票返佣' end costname, date_format(r.createDate,'%Y-%m-%d %H:%i:%s') createDate, "
					+ "'' groupCode, m.currencyId, (select c.currency_mark from currency c where c.currency_id = m.currencyId) currencyMark, " +
					"m.amount rebatesDiff, m.exchangerate rate, 1 reviewflag " +
					" from money_amount m, review r where r.id = m.reviewId and r.id = ?";
		}else{
			sql =  "select r.id, o.order_type orderType, o.costname costname, date_format(r.createDate,'%Y-%m-%d %H:%i:%s') createDate, " +
					"g.groupCode, o.currencyId, (select c.currency_mark from currency c where c.currency_id = o.currencyId) currencyMark," +
					"o.rebatesDiff rebatesDiff, o.rate, 1 reviewflag " +
					"from order_rebates o, review r, productorder p, activitygroup g where r.id = o.rid and p.productGroupId = g.id and r.orderId = p.id and r.id = ?";
		}

		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取退款数据
	 * @param reviewUuid
	 * @return
	 */
	public Map<String, Object> getRefundInfo(String reviewUuid) {
		String sql = "select rn.id, m.orderType, m.currencyId, date_format(rn.create_date,'%Y-%m-%d %H:%i:%s') createDate1,"
				+ "(select c.currency_mark from currency c where c.currency_id = m.currencyId) currencyMark, m.amount rebatesDiff, m.exchangerate rate, 2 reviewflag " +
					"from money_amount m, review_new rn where rn.id = m.review_uuid and m.review_uuid = ?";

		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);
		if(list.size() > 0) {
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewUuid);
			list.get(0).putAll(reviewMap);
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取退款数据
	 * @param reviewUuid
	 * @return
	 */
	public Map<String, Object> getOldRefundInfo(String reviewUuid) {
		String sql = "select r.id, m.orderType, rd.myValue refundName, m.currencyId, "
				+ "(select c.currency_mark from currency c where c.currency_id = m.currencyId) currencyMark, m.amount rebatesDiff, m.exchangerate rate, 1 reviewflag" +
					" from money_amount m, review r, review_detail rd where r.id = m.reviewId and r.id = rd.review_id and rd.myKey = 'refundName' and r.id = ?";

		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取退款数据
	 * @param reviewUuid
	 * @return
	 */
	public Map<String, Object> getOldRefundInfo(String reviewUuid, String prdtype) {
		String sql =  "";
		if("6".equals(prdtype) || "7".equals(prdtype)) {
			sql = "select r.id, m.orderType, rd.myValue refundName, date_format(r.createDate,'%Y-%m-%d %H:%i:%s') createDate1, '' groupCode, m.currencyId, "
					+ "(select c.currency_mark from currency c where c.currency_id = m.currencyId) currencyMark, " +
					"m.amount rebatesDiff, m.exchangerate rate, 1 reviewflag " +
					" from money_amount m, review r, review_detail rd where r.id = m.reviewId and r.id = rd.review_id and rd.myKey = 'refundName' and r.id = ?";
		}else{
			sql =  "select r.id, m.orderType, rd.myValue refundName, date_format(r.createDate,'%Y-%m-%d %H:%i:%s') createDate1, g.groupCode, m.currencyId, "
					+ "(select c.currency_mark from currency c where c.currency_id = m.currencyId) currencyMark, " +
					"m.amount rebatesDiff, m.exchangerate rate,1 reviewflag " +
					" from money_amount m, review r, review_detail rd, productorder p, activitygroup g where r.id = m.reviewId and r.id = rd.review_id and p.productGroupId = g.id "
					+ "and r.orderId = p.id and rd.myKey = 'refundName' and r.id = ?";
		}

		List<Map<String, Object>> list = refundDao.findBySql(sql, Map.class, reviewUuid);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
