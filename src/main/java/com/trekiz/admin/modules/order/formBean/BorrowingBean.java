package com.trekiz.admin.modules.order.formBean;

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

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.Reflections;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
/**
 * 
 * @创建人 ruyi.chen
 * @创建时间 2015-5-4下午9:17:08
 * @描述 借款
 * @version
 */
public class BorrowingBean {
	
	/**
	 * 借款流程用于多个记录保存时用到的分隔符 
	 */
	public static final String REGEX = "#";
	
	/**
	 * 占位符，保证每个分隔符都不连续
	 * 防止传递空字符串时造成split出的数组结构错误
	 */
	private static final String REGEX_PLACE = "_";
	
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
		statusMap.put("1", "审核中");
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
	 * 退款款项(分隔多个)
	 */
	private String lendName;
	/**
	 * 申请日期(分隔多个)
	 */
	private Date applyDate;
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

	public String getStatusDesc() {
		return statusMap.get(String.valueOf(status));
	}
	
	public String getStatusClassDesc(){
	    return statusClassMap.get(String.valueOf(status));
	}

	public BorrowingBean() {

	}

	public BorrowingBean(Map<String, String> reviewDetailMap) {
		this.travelerId = reviewDetailMap.get(KEY_TRAVELERID);
		this.travelerName = reviewDetailMap.get(KEY_TRAVELERNAME);
		this.lendName = reviewDetailMap.get(KEY_LENDNAME);
		this.applyDate = reviewDetailMap.get(KEY_APPLYDATE) == null ? null
				: DateUtils.dateFormat(reviewDetailMap.get(KEY_APPLYDATE));
		this.currencyId = reviewDetailMap.get(KEY_CURRENCYID);
		this.currencyName = reviewDetailMap.get(KEY_CURRENCYNAME);
		this.currencyMark = reviewDetailMap.get(KEY_CURRENCYMARK);
		this.payPrice = reviewDetailMap.get(KEY_PAYPRICE);
		this.lendPrice = reviewDetailMap.get(KEY_LENDPRICE);
		this.currencyExchangerate = reviewDetailMap.get(KEY_CURRENCYRXCHANGERATE);
		this.remark = reviewDetailMap.get(KEY_REMARK);
		this.status = Integer.valueOf(reviewDetailMap.get(KEY_STATUS));
		this.reviewId = reviewDetailMap.get(KEY_REVIEWID);
		this.currencyIds=reviewDetailMap.get(KEY_ALL_CURRENCYID);
		this.currencyNames = reviewDetailMap.get(KEY_ALL_CURRENCYNAME);
		this.currencyMarks = reviewDetailMap.get(KEY_ALL_CURRENCYMARK);
		this.borrowPrices = reviewDetailMap.get(KEY_ALL_BORROWPRICE);
		this.createBy = Long.parseLong(reviewDetailMap.get(CREATEBY));
		this.currencyConverter = reviewDetailMap.get(KEY_CURRENCYCONVERTER);
		this.currencyExchangerates = reviewDetailMap.get(KEY_ALL_CURRENCYRXCHANGERATE);
		this.borrowRemark = reviewDetailMap.get(KEY_BORROWREMARK);
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
	 * 根据List<ReviewDetail> rdlist 中的对象属性转换成  BorrowingBean对象，rdlist是根据review_id查询出的记录 
	 * add by zhanghao 20150505
	 * @return List<BorrowingBean>
	 * @param rdlist
	 * @return
	 */
	public static List<BorrowingBean> transferReviewDetail2BorrowingBean(List<ReviewDetail> rdlist){
		List<BorrowingBean> result = new ArrayList<BorrowingBean>();
		if(rdlist!=null){
			for(ReviewDetail rd:rdlist){
				
				//得到所有 BorrowingBean 对象 的 属性信息 
				Field[] fields = BorrowingBean.class.getDeclaredFields();
				if(fields!=null){
					for(Field field:fields){
						//当前对象的属性名称如果和 List<ReviewDetail> rdlist 中的每个detail 的 KEY 做比较
						//如果相等则会进行循环赋值操作
						if(field !=null && field.getName().equals(rd.getMykey())){
							if(StringUtils.isNotBlank(rd.getMyvalue())){
								String[] valueArray = rd.getMyvalue().split(BorrowingBean.REGEX);
								if(valueArray!=null ){
									for(int i=0;i<valueArray.length;i++){
										//String value = valueArray[i]
										if(result.size()<valueArray.length){
											BorrowingBean bean = new BorrowingBean();
											result.add(bean);
										}
										Object obj = setfieldValue(field, valueArray[i]);
										Reflections.setFieldValue(result.get(i), rd.getMykey(), obj);
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
		
		if(StringUtils.isNotBlank(value)&&value.indexOf(BorrowingBean.REGEX_PLACE)>-1){
			value=value.replaceAll(BorrowingBean.REGEX_PLACE, "");
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
	 * 把request中的属性值组装之后 放入 ReviewDetail对象并返回List
	 * 此方法只能用于 流程申请时的数据封装，所以reqeust中必须有reviewId,否则不进行转换
	 * add by zhanghao 20150505
	 * @param request
	 * @param paramNames null值时会把所有的request属性都进行转换，否则只会转换KEY值是paramNames中的属性
	 * @param keyParam 转换时的 验证表示，如果为空则不会进行属性添加
	 * @return
	 */
	private static List<ReviewDetail> exportReviewDetail4Request(HttpServletRequest request,List<String> paramNames,String keyParam){
		List<ReviewDetail> result = new ArrayList<ReviewDetail>();
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
							sb.append(BorrowingBean.REGEX+BorrowingBean.REGEX_PLACE);
						}
					}
					if(sb.length()>0){
						sb.delete(sb.lastIndexOf(BorrowingBean.REGEX+BorrowingBean.REGEX_PLACE),sb.length());
						ReviewDetail rd = new ReviewDetail();
						if(StringUtils.isNotBlank(reviewId)){
							rd.setReviewId(Long.parseLong(reviewId));
						}
						rd.setMykey(name);
						rd.setMyvalue(sb.toString());
						result.add(rd);
					}
				}
			}
		}else{
			//得到所有 BorrowingBean 对象 的 属性信息 
			Field[] fields = BorrowingBean.class.getDeclaredFields();
			if(fields!=null){
				for(Field field:fields){
					
					//当前对象的属性名称如果和 List<ReviewDetail> rdlist 中的每个detail 的 KEY 做比较
					//如果相等则会进行循环赋值操作
					if(field !=null ){
						paramArray = request.getParameterValues(field.getName());
						
						if(ArrayUtils.isNotEmpty(paramArray)){
							StringBuilder sb = new StringBuilder();
							for(String param:paramArray){
								sb.append(param);
								sb.append(BorrowingBean.REGEX+BorrowingBean.REGEX_PLACE);
							}
							if(sb.length()>0){
								sb.delete(sb.lastIndexOf(BorrowingBean.REGEX+BorrowingBean.REGEX_PLACE),sb.length());
								ReviewDetail rd = new ReviewDetail();
								rd.setReviewId(Long.parseLong(reviewId));
								rd.setMykey(field.getName());
								rd.setMyvalue(sb.toString());
								result.add(rd);
							}
							
						}
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * exportReviewDetail4Request方法的扩展，此方法返回List<Detail>
	 * @param request
	 * @param paramNames request中的数组
	 * @param param request中的唯一值
	 * @param keyParamName 非空判断标示的属性
	 * @return
	 */
	public static List<Detail> exportDetail4Request(HttpServletRequest request,List<String> paramNames,List<String> params,String keyParamName){
		List<ReviewDetail> sourceList = exportReviewDetail4Request(request, paramNames,keyParamName);
		if(CollectionUtils.isEmpty(sourceList)){
			return null;
		}
		List<Detail> targetList = new ArrayList<Detail>();
		for(ReviewDetail source:sourceList){
			Detail target = new Detail();
			target.setKey(source.getMykey());
			target.setValue(source.getMyvalue());
			targetList.add(target);
		}
		if(CollectionUtils.isNotEmpty(params)){
			for(String param:params){
				Detail target = new Detail();
				target.setKey(param);
				if(request.getParameter(param).indexOf(",")>-1){
					target.setValue(request.getParameter(param).replaceAll(",", ""));
				}else{
					target.setValue(request.getParameter(param));
				}
				targetList.add(target);
			}
		}
		return targetList;
	}
	
}
