package com.trekiz.admin.review.borrowing.airticket.formbean;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.Reflections;
/**
 * 
 * @创建人 ruyi.chen
 * @创建时间 2015-5-4下午9:17:08
 * @描述 借款
 * @version
 */
public class NewBorrowingBean {
	
	/**
	 * 借款流程用于多个记录保存时用到的分隔符 
	 */
	public static final String REGEX = "#";
	
	/**
	 * 占位符，保证每个分隔符都不连续
	 * 防止传递空字符串时造成split出的数组结构错误
	 */
	public static final String REGEX_PLACE = "_";
	
	/**
	 * 游客Id
	 */
	private static final String KEY_TRAVELERID = "travelerId";
	/**
	 * 游客名称
	 */
	private static final String KEY_TRAVELERNAME = "travelerName";
	/**
	 * 借款款项
	 */
	private static final String KEY_LENDNAME = "lendName";
	
	/**
	 *借款备注
	 */
	
	private static final String KEY_BORROWREMARK ="borrowRemark";
	
	/**
	 * 申请日期
	 */
	private static final String KEY_APPLYDATE = "createDate";
	/**
	 * 货币类型Id
	 */
	private static final String KEY_CURRENCYID = "currencyId";
	/**
	 * 货币名称
	 */
	private static final String KEY_CURRENCYNAME = "currencyName";
	
	/**
	 * 币种汇率
	 */
	private static final String KEY_CURRENCYRXCHANGERATE ="currencyExchangerate";
	
	/**
	 * 环节待办人ID
	 */
	
	private static final String KEY_CURRENTREVIEWER = "currentReviewer";
	
	/**
	 * 货币标示
	 */
	private static final String KEY_CURRENCYMARK = "currencyMark";
	/**
	 * 结算价
	 */
	private static final String KEY_PAYPRICE = "payPrice";
	/**
	 * 借款
	 */
	private static final String KEY_LENDPRICE = "lendPrice";
	/**
	 * 还款日期
	 */
	private static final String KEY_REFUND_DATE = "refundDate";
	/**
	 * 退款说明(分隔多个)
	 */
	private static final String KEY_REMARK = "remark";
	/**
	 * 申请状态
	 */
	private static final String KEY_STATUS = "status";
	
	private static final String KEY_REVIEWID = "id";
	
	/**
	 * 货币类型Id(分隔多个)
	 */
	private static final String KEY_ALL_CURRENCYID = "currencyIds";
	/**
	 * 货币名称(分隔多个)
	 */
	private static final String KEY_ALL_CURRENCYNAME = "currencyNames";
	/**
	 * 货币标示(分隔多个)
	 */
	private static final String KEY_ALL_CURRENCYMARK = "currencyMarks";
	/**
	 * 金额标示(分隔多个)
	 */
	private static final String KEY_ALL_BORROWPRICE = "borrowPrices";
	/**
	 * 货币汇率(分隔多个)
	 */
	private static final String KEY_ALL_CURRENCYRXCHANGERATE = "currencyExchangerates";
	/**
	 * 转换人民币
	 */
	private static final String KEY_CURRENCYCONVERTER = "currencyConverter";
	/**
	 * 申请人ID
	 */
	private static final String CREATEBY = "createBy";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static Map<String, String> statusMap = new HashMap<String, String>();
	
	private static Map<String, String> statusClassMap = new HashMap<String, String>();
	
	static {
		statusMap.put("0", "已驳回");
		statusMap.put("1", "审批中");
		statusMap.put("2", "已通过");
		statusMap.put("3", "已退款");
		
		statusClassMap.put("0", "invoice_back");
		statusClassMap.put("1", "invoice_no");
		statusClassMap.put("2", "invoice_yes");
		statusClassMap.put("3", "invoice_yes");

	}
	
	
	

	/**
	 * 游客Id(分隔多个)
	 */
	private String travelerId;
	/**
	 * 游客名称(分隔多个)
	 */
	private String travelerName;
	/**
	 * 环节待办人id
	 */
	private String currentReviewer;
	/**
	 * 退款款项(分隔多个)
	 */
	private String lendName;
	/**
	 * 申请日期(分隔多个)
	 */
	private Date applyDate;
	/**
	 * 还款日期
	 */
	private Date refundDate;
	/**
	 * 货币类型Id(分隔多个)
	 */
	private String currencyId;
	/**
	 * 货币名称(分隔多个)
	 */
	private String currencyName;
	/**
	 * 货币汇率(分隔多个)
	 */
	private String currencyExchangerate;
	public String getCurrencyExchangerate() {
		return currencyExchangerate;
	}

	public void setCurrencyExchangerate(String currencyExchangerate) {
		this.currencyExchangerate = currencyExchangerate;
	}

	/**
	 * 货币标示(分隔多个)
	 */
	private String currencyMark;
	/**
	 * 应付款(分隔多个)
	 */
	private String payPrice;
	/**
	 * 借款(分隔多个)
	 */
	private String lendPrice;
	/**
	 * 退款说明(分隔多个)
	 */
	private String remark;
	/**
	 * 申请状态
	 */
	private int status;
	
	/**
	 * 借款备注
	 */
	
	private String borrowRemark;
	

	public String getBorrowRemark() {
		return borrowRemark;
	}

	public void setBorrowRemark(String borrowRemark) {
		this.borrowRemark = borrowRemark;
	}

	/**
	 * 评审Id
	 */
	private String reviewId;

	/**
	 * 借款总金额币种id字符串  分隔符分隔
	 */
	private String currencyIds;
	
	/**
	 * 借款总金额币种名称字符串  分隔符分隔
	 */
	private String currencyNames;
	/**
	 * 借款总金额币种符号字符串  分隔符分隔
	 */
	private String currencyMarks;
	/**
	 * 借款总金额币种金额字符串  分隔符分隔
	 */
	private String borrowPrices;
	
	/**
	 * 借款总金额币种汇率字符串  分隔符分隔
	 */
	private String currencyExchangerates;
	public String getCurrencyExchangerates() {
		return currencyExchangerates;
	}

	public void setCurrencyExchangerates(String currencyExchangerates) {
		this.currencyExchangerates = currencyExchangerates;
	}

	/**
	 * 
	 * 转换人民币
	 */
	
	private String currencyConverter;
	
	public String getCurrencyConverter() {
		return currencyConverter;
	}

	public void setCurrencyConverter(String currencyConverter) {
		this.currencyConverter = currencyConverter;
	}

	/**
	 * 借款申请人ID
	 */
	private Long createBy;
	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	
	

	public String getCurrentReviewer() {
		return currentReviewer;
	}

	public void setCurrentReviewer(String currentReviewer) {
		this.currentReviewer = currentReviewer;
	}

	public String getStatusDesc() {
		return statusMap.get(String.valueOf(status));
	}
	
	public String getStatusClassDesc(){
	    return statusClassMap.get(String.valueOf(status));
	}

	public NewBorrowingBean() {

	}
	
	
	
	/**
	 * by sy 2015年11月4日18:19:32
	 * 
	 * @param reviewDetailMap
	 */
	
	public NewBorrowingBean(Map<String, Object> reviewDetailMap) {
		//edit by majiancheng 2015-12-05 游客id添加非空校验
		if(reviewDetailMap.get(KEY_TRAVELERID) != null) {
			this.travelerId = reviewDetailMap.get(KEY_TRAVELERID).toString();
		}
		if(reviewDetailMap.get(KEY_TRAVELERNAME) != null){
			this.travelerName = reviewDetailMap.get(KEY_TRAVELERNAME).toString();
		}
		if(reviewDetailMap.get(KEY_CURRENTREVIEWER) != null){
			this.currentReviewer = reviewDetailMap.get(KEY_CURRENTREVIEWER).toString();
		}
		if(reviewDetailMap.get(KEY_LENDNAME) != null){
			this.lendName = reviewDetailMap.get(KEY_LENDNAME).toString();
		}
		if(reviewDetailMap.get(KEY_APPLYDATE) != null){
			this.applyDate = reviewDetailMap.get(KEY_APPLYDATE) == null ? null
					: DateUtils.dateFormat(reviewDetailMap.get(KEY_APPLYDATE).toString());
		}
		if(reviewDetailMap.get(KEY_CURRENCYID) != null){
			this.currencyId = reviewDetailMap.get(KEY_CURRENCYID).toString();
		}
		if(reviewDetailMap.get(KEY_CURRENCYNAME) != null){
			this.currencyName = reviewDetailMap.get(KEY_CURRENCYNAME).toString();
		}
		if(reviewDetailMap.get(KEY_CURRENCYMARK) != null){
			this.currencyMark = reviewDetailMap.get(KEY_CURRENCYMARK).toString();
		}
		if(reviewDetailMap.get(KEY_PAYPRICE) != null){
			this.payPrice = reviewDetailMap.get(KEY_PAYPRICE).toString();
		}
		if(reviewDetailMap.get(KEY_LENDPRICE) != null){
			this.lendPrice = reviewDetailMap.get(KEY_LENDPRICE).toString();
		}
		if(reviewDetailMap.get(KEY_REFUND_DATE) != null){
			this.refundDate = reviewDetailMap.get(KEY_REFUND_DATE) == null ? null
					: DateUtils.dateFormat(reviewDetailMap.get(KEY_REFUND_DATE).toString());
		}
		if(reviewDetailMap.get(KEY_CURRENCYRXCHANGERATE) != null){
			this.currencyExchangerate = reviewDetailMap.get(KEY_CURRENCYRXCHANGERATE).toString();
		}
		if(reviewDetailMap.get(KEY_REMARK) != null){
			this.remark = reviewDetailMap.get(KEY_REMARK).toString();
		}
		if(reviewDetailMap.get(KEY_STATUS) != null){
			this.status = Integer.valueOf(reviewDetailMap.get(KEY_STATUS).toString());
		}
		if(reviewDetailMap.get(KEY_REVIEWID) != null){
			this.reviewId = reviewDetailMap.get(KEY_REVIEWID).toString();
		}
		if(reviewDetailMap.get(KEY_ALL_CURRENCYID) != null){
			this.currencyIds=reviewDetailMap.get(KEY_ALL_CURRENCYID).toString();
		}
		if(reviewDetailMap.get(KEY_ALL_CURRENCYNAME) != null){
			this.currencyNames = reviewDetailMap.get(KEY_ALL_CURRENCYNAME).toString();
		}
		if(reviewDetailMap.get(KEY_ALL_CURRENCYMARK) != null){
			this.currencyMarks = reviewDetailMap.get(KEY_ALL_CURRENCYMARK).toString();
		}
		if(reviewDetailMap.get(KEY_ALL_BORROWPRICE) != null){
			this.borrowPrices = reviewDetailMap.get(KEY_ALL_BORROWPRICE).toString();
		}
		if(reviewDetailMap.get(CREATEBY) != null){
			this.createBy = Long.parseLong(reviewDetailMap.get(CREATEBY).toString());
		}
		if(reviewDetailMap.get(KEY_CURRENCYCONVERTER) != null){
			this.currencyConverter = reviewDetailMap.get(KEY_CURRENCYCONVERTER).toString();
		}
		if(reviewDetailMap.get(KEY_ALL_CURRENCYRXCHANGERATE) != null){
			this.currencyExchangerates = reviewDetailMap.get(KEY_ALL_CURRENCYRXCHANGERATE).toString();
		}
		if(reviewDetailMap.get(KEY_BORROWREMARK) != null){
			this.borrowRemark = reviewDetailMap.get(KEY_BORROWREMARK).toString();
		}
		
	}
	
	

	public Map<String, String> getReviewDetailMap() {
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put(KEY_TRAVELERID, travelerId);
		reviewDetailMap.put(KEY_TRAVELERNAME, travelerName);
		reviewDetailMap.put(KEY_LENDNAME, lendName);
		reviewDetailMap.put(KEY_APPLYDATE, sdf.format(getApplyDate()));
		reviewDetailMap.put(KEY_CURRENCYID, currencyId);
		reviewDetailMap.put(KEY_CURRENCYNAME, currencyName);
		reviewDetailMap.put(KEY_CURRENCYRXCHANGERATE, currencyExchangerate);
		reviewDetailMap.put(KEY_CURRENCYMARK, currencyMark);
		reviewDetailMap.put(KEY_PAYPRICE, payPrice);
		reviewDetailMap.put(KEY_CURRENCYCONVERTER, currencyConverter);
		reviewDetailMap.put(KEY_LENDPRICE, lendPrice);
		reviewDetailMap.put(KEY_REMARK, remark);
		reviewDetailMap.put(KEY_ALL_CURRENCYID, currencyIds);
		reviewDetailMap.put(KEY_ALL_CURRENCYNAME, currencyNames);
		reviewDetailMap.put(KEY_ALL_CURRENCYMARK, currencyMarks);
		reviewDetailMap.put(KEY_ALL_BORROWPRICE, borrowPrices);
		reviewDetailMap.put(CREATEBY, createBy.toString());
		reviewDetailMap.put("KEY_ALL_CURRENCYRXCHANGERATE", currencyExchangerates);
		reviewDetailMap.put(KEY_BORROWREMARK, borrowRemark);
		return reviewDetailMap;
	}

	public String getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(String travelerId) {
		this.travelerId = travelerId;
	}

	public String getTravelerName() {
		return travelerName;
	}

	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}



	public String getLendName() {
		return lendName;
	}

	public void setLendName(String lendName) {
		this.lendName = lendName;
	}

	public Date getApplyDate() {
		if (null == applyDate) {
			applyDate = new Date();
		}
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyMark() {
		return currencyMark;
	}

	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	

	public String getLendPrice() {
		return lendPrice;
	}

	public void setLendPrice(String lendPrice) {
		this.lendPrice = lendPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCurrencyIds() {
		return currencyIds;
	}

	public void setCurrencyIds(String currencyIds) {
		this.currencyIds = currencyIds;
	}

	public String getCurrencyNames() {
		return currencyNames;
	}

	public void setCurrencyNames(String currencyNames) {
		this.currencyNames = currencyNames;
	}

	public String getCurrencyMarks() {
		return currencyMarks;
	}

	public void setCurrencyMarks(String currencyMarks) {
		this.currencyMarks = currencyMarks;
	}

	
	public String getBorrowPrices() {
		return borrowPrices;
	}

	public void setBorrowPrices(String borrowPrices) {
		this.borrowPrices = borrowPrices;
	}

	public Long getCreateBy() {
		return createBy;
	}
	
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	
	/**
	 * add by sy 2015年11月4日18:20:17
	 * @return List<NewBorrowingBean>
	 * @param rdlist
	 * @return
	 */
	public static List<NewBorrowingBean> transferReviewDetail2BorrowingBeanMap(List<Map<String,Object>> rdlist){
		List<NewBorrowingBean> result = new ArrayList<NewBorrowingBean>();
		if(rdlist!=null){
			
			for(Map<String,Object> rd:rdlist){
				rd.putAll(rdlist.get(0));
				//得到所有 NewBorrowingBean 对象 的 属性信息 
				Field[] fields = NewBorrowingBean.class.getDeclaredFields();
				if(fields!=null){
					for(Field field:fields){
						//当前对象的属性名称如果和 List<ReviewDetail> rdlist 中的每个detail 的 KEY 做比较
						//如果相等则会进行循环赋值操作
						if(field !=null && rd.containsKey(field.getName())){
							if(rd.size()>0){
								String[] valueArray = rd.get(field.getName()).toString().split(NewBorrowingBean.REGEX);
								if(valueArray!=null ){
									for(int i=0;i<valueArray.length;i++){
										//String value = valueArray[i]
										if(result.size()<valueArray.length){
											NewBorrowingBean bean = new NewBorrowingBean();
											result.add(bean);
										}
										Object obj = setfieldValue(field, valueArray[i]);
										Reflections.setFieldValue(result.get(i), field.getName(), obj);
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	
	
	
	
	/**
	 * 特殊类型的转型
	 * @param field
	 * @param value
	 * @return
	 */
	private static Object setfieldValue(Field field ,String value){
		
		if(StringUtils.isNotBlank(value)&&value.indexOf(NewBorrowingBean.REGEX_PLACE)>-1){
			value=value.replaceAll(NewBorrowingBean.REGEX_PLACE, "");
		}
		
		String type = field.getType().toString();
		if(type.indexOf("java.lang.Long")>-1||type.indexOf("long")>-1){
			return Long.parseLong(value);
		}else if(type.indexOf("java.lang.Integer")>-1||type.indexOf("int")>-1){
			return Integer.parseInt(value);
		}else if(type.indexOf("java.lang.Float")>-1||type.indexOf("float")>-1){
			return Float.parseFloat(value);
		}else if(type.indexOf("java.lang.Double")>-1||type.indexOf("double")>-1){
			return Double.parseDouble(value);
		}else if(type.indexOf("java.util.Date")>-1){
			return DateUtils.string2Date(value);
		}
		return value;
	}
	
	
	
	/**
	 * exportReviewDetail4Request方法的扩展，此方法返回Map<Detail>
	 * @author songyang 2015年11月2日17:49:44
	 * @param request
	 * @param paramNames request中的数组
	 * @param param request中的唯一值
	 * @param keyParamName 非空判断标示的属性
	 * @return
	 */
	public static Map<String,String> exportDetail4RequestMap(HttpServletRequest request,List<String> paramNames,List<String> params,String keyParamName){
		Map<String,String>   sourceMap = exportReviewDetail4Request(request, paramNames,keyParamName);
		sourceMap.put(Context.REVIEW_VARIABLE_KEY_REFUND_DATE, request.getParameter("refundDate"));
		if(sourceMap.size()<0){
			return null;
		}
		//组装params参数到MAP
		Map<String,String>   paramsMap = new HashMap<String,String>();
		if(CollectionUtils.isNotEmpty(params)){
			for(String param:params){
				if(null!=request.getParameter(param)){
					if(request.getParameter(param).indexOf(",")>-1){
						paramsMap.put(param, request.getParameter(param).replaceAll(",", ""));
					}else{
						paramsMap.put(param,request.getParameter(param));
					}
				}
				sourceMap.putAll(paramsMap);
			}
		}
		return sourceMap;
	}
	
	
	
	
	
	/**
	 * 把request中的属性值组装之后 放入 ReviewDetail对象并返回List
	 * 此方法只能用于 流程申请时的数据封装，所以reqeust中必须有reviewId,否则不进行转换
	 * add by sy 2015年11月4日18:20:08
	 * @param request
	 * @param paramNames null值时会把所有的request属性都进行转换，否则只会转换KEY值是paramNames中的属性
	 * @param keyParam 转换时的 验证表示，如果为空则不会进行属性添加
	 * @return
	 */
	public static Map<String,String> exportReviewDetail4Request(HttpServletRequest request,List<String> paramNames,String keyParam){
		Map<String,String> mapResult = new HashMap<String,String>();
		String reviewId = request.getParameter("reviewId");
		
		String[] paramArray =null;
		//如果集合不为空则会只取出 request中的指定属性进行对象转换否则会进行request中的所有属性转换
		if(CollectionUtils.isNotEmpty(paramNames)&&StringUtils.isNotBlank(keyParam)){
			String[] keyParamArray = request.getParameterValues(keyParam);
			for(String name:paramNames){
				paramArray = request.getParameterValues(name);
				
				if(ArrayUtils.isNotEmpty(paramArray)){
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<paramArray.length;i++){
						//验证标示 非空时才会进行属性添加
						if(StringUtils.isNotBlank(keyParamArray[i])){
							if(paramArray[i].indexOf(",")>-1){
								sb.append(paramArray[i].replaceAll(",", ""));
							}else{
								sb.append(paramArray[i]);
							}
							sb.append(NewBorrowingBean.REGEX+NewBorrowingBean.REGEX_PLACE);
						}
					}
					if(sb.length()>0){
						sb.delete(sb.lastIndexOf(NewBorrowingBean.REGEX+NewBorrowingBean.REGEX_PLACE),sb.length());
//						ReviewDetail rd = new ReviewDetail();
//						if(StringUtils.isNotBlank(reviewId)){
//							rd.setReviewId(Long.parseLong(reviewId));
//						}
						mapResult.put(name, sb.toString());
					}
				}
			}
		}else{
			//得到所有 NewBorrowingBean 对象 的 属性信息 
			Field[] fields = NewBorrowingBean.class.getDeclaredFields();
			if(fields!=null){
				for(Field field:fields){
					
					if(field !=null ){
						paramArray = request.getParameterValues(field.getName());
						
						if(ArrayUtils.isNotEmpty(paramArray)){
							StringBuilder sb = new StringBuilder();
							for(String param:paramArray){
								sb.append(param);
								sb.append(NewBorrowingBean.REGEX+NewBorrowingBean.REGEX_PLACE);
							}
							if(sb.length()>0){
								sb.delete(sb.lastIndexOf(NewBorrowingBean.REGEX+NewBorrowingBean.REGEX_PLACE),sb.length());
								mapResult.put(field.getName(), sb.toString());
							}
							
						}
					}
				}
			}
		}
		
		return mapResult;
	}
	
}
