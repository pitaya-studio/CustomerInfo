package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.search.annotations.Indexed;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;


/**
 * 文件名: ActivityReserveOrderView 视图,散拼产品切位列表使用
 * 
 * @author ruyi.chen
 * @DateTime 2014-12-18
 * @version 1.0
 */

@Entity
@Table(name = "searchreserveorder_view")
@DynamicInsert
public class ActivityReserveOrderView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id; // 编号	
	/**团号 */
	private Long productId;
	private String groupCode;
	private Long srcActivityId;
	private Long activityTypeId;
	/*产品名*/
	private String acitivityName;
	
	private String fromArea;
	/*团名*/
	private String targetArea;	
	/** 产品中一个确定的出团日期 */
	private Date groupOpenDate;
	/** 产品中的一个确定的截团日期 */
	private Date groupCloseDate;
	
	private Integer soldPayPosition;
	private Integer payReservePosition;
	private Integer freePosition;
	private Integer planPosition;
	
	/** 成人的产品最低结算价 */
	private Integer settlementAdultPrice;

	/** 建议成人最低零售价 */
	private BigDecimal suggestAdultPrice;
	
	private  Date updateDate;// 更新日期

	/** 加载散拼切位订单信息 */
	private Integer aid;//散拼切位订单唯一标识
	private String orderNum;//散拼切位订单 订单号
	private Date createDate;//散拼切位订单 创建时间
	private Integer orderStatus;//散拼切位订单状态  0:未付定金,1:已付定金
	private BigDecimal orderMoney;//散拼切位订单已付金额
	private BigDecimal payMoney;//散拼切位订单应付金额
	private Integer confirm;//散拼切位订单 (财务)收款确认(0:未确认,1:已确认)
	private Integer reserveType;//散拼0,机票1
	private Integer moneyType;//币种类型(美元,人民币..)
	private String currencyName;//币种名称
	private String agentName;//渠道商名称
	
	private String areaName;//产品目的地
	private String areaId;//产品目的地标识
	private Integer proCompany;//产品所有公司
	private Integer agentId;//渠道商ID
	private Long createBy;//创建切位订单人
	private Integer companyId;//创建切位订单人所在公司Id
	
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public Integer getProCompany() {
		return proCompany;
	}
	public void setProCompany(Integer proCompany) {
		this.proCompany = proCompany;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	@Id
	@Column(name = "aid", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public BigDecimal getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public Integer getConfirm() {
		return confirm;
	}
	public void setConfirm(Integer confirm) {
		this.confirm = confirm;
	}
	public Integer getReserveType() {
		return reserveType;
	}
	public void setReserveType(Integer reserveType) {
		this.reserveType = reserveType;
	}
	public Integer getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	
	@Column(name = "srcActivityId", unique = true, nullable =false)	
	public Long getSrcActivityId() {
		return srcActivityId;
	}
	public void setSrcActivityId(Long srcActivityId) {
		this.srcActivityId = srcActivityId;
	}	

	
	@Column(name = "groupCode", unique = false, nullable = true)
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@Column(name = "acitivityName", unique = false, nullable = true)
	public String getAcitivityName() {
		return acitivityName;
	}
	public void setAcitivityName(String acitivityName) {
		this.acitivityName = acitivityName;
	}
	
	@Column(name = "fromArea", unique = false, nullable = true)
	public String getFromArea() {
		return fromArea;
	}
	public void setFromArea(String fromArea) {
		this.fromArea = fromArea;
	}
	
	@Column(name = "targetArea", unique = false, nullable = true)
	public String getTargetArea() {
		return targetArea;
	}
	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}
	
	@Column(name = "groupOpendate", unique = false, nullable = true)
	public Date getGroupOpenDate() {
		return groupOpenDate;
	}
	public void setGroupOpenDate(Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}
	
	@Column(name = "groupCloseDate", unique = false, nullable = true)
	public Date getGroupCloseDate() {
		return groupCloseDate;
	}
	public void setGroupCloseDate(Date groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}
	
	@Column(name = "soldPayPosition", unique = false, nullable = true)
	public Integer getSoldPayPosition() {
		return soldPayPosition;
	}
	public void setSoldPayPosition(Integer soldPayPosition) {
		this.soldPayPosition = soldPayPosition;
	}
	
	@Column(name = "freePosition", unique = false, nullable = true)
	public Integer getFreePosition() {
		return freePosition;
	}
	public void setFreePosition(Integer freePosition) {
		this.freePosition = freePosition;
	}
	@Column(name = "planPosition", unique = false, nullable = true)
	public Integer getPlanPosition() {
		return planPosition;
	}
	public void setPlanPosition(Integer planPosition) {
		this.planPosition = planPosition;
	}

	@Column(name = "productId", unique = false, nullable = true)
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Column(name = "settlementAdultPrice", unique = false, nullable = true)
	public Integer getSettlementAdultPrice() {
		return settlementAdultPrice;
	}
	public void setSettlementAdultPrice(Integer settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}
	

	@Transient
	public String getFromAreaName() {
		if (getFromArea() == null)
			return "";
		return DictUtils
				.getDictLabel(getFromArea().toString(), "from_area", "");
	}	
	
	@Column(name = "suggestAdultPrice", unique = false, nullable = true)
	public BigDecimal getSuggestAdultPrice() {
		return suggestAdultPrice;
	}
	
	public void setSuggestAdultPrice(BigDecimal suggestAdultPrice) {
		this.suggestAdultPrice = suggestAdultPrice;
	}
	
	@Column(name = "updateDate", unique = false, nullable = true)
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	} 
	
	@Column(name = "payReservePosition", unique = false, nullable = true)
	public Integer getPayReservePosition() {
		return payReservePosition;
	}
	public void setPayReservePosition(Integer payReservePosition) {
		this.payReservePosition = payReservePosition;
	}
	
	@Transient
	public int getLeftdays() {
		if(getGroupOpenDate()==null)return 0;
    	long openDateTime = getGroupOpenDate().getTime();
    	long nowDateTime = DateUtils.parseDate(DateUtils.getDate("yyyy-MM-dd")).getTime();
    	Long diffDate = (nowDateTime-openDateTime)/(24*60*60*1000);
        return diffDate.intValue() ;
	}
	
	@Column(name = "activityTypeId", unique = false, nullable = true)
	public Long getActivityTypeId() {
		return activityTypeId;
	}
	public void setActivityTypeId(Long activityTypeId) {
		this.activityTypeId = activityTypeId;
	}
}
