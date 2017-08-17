package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.DictUtils;


@Entity
@Table(name = "activitygroup_cost_view")
@DynamicInsert
public class ActivityGroupCostView implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private Long id; // 编号	
	private Long agpId;
	private String name;
	private String costCreateBy;
	private String currencyMark;
	private BigDecimal  price;
	private Integer quantity;
	private BigDecimal  totalPrice;
	private String supplyName;	
    private Integer updateBy;
	/**团号 */
	private Long productId;
	private String groupCode;
	private Long srcActivityId;
	private Long supplyId;
	private Integer supplyType;  
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
	private Integer nopayReservePosition;
	private Integer freePosition;
	private Integer planPosition;
	private Long proCompany;
	
	/** 成人的产品最低结算价 */
	private Integer settlementAdultPrice;
	
 
	private Integer activityKind;

	/** 建议成人最低零售价 */
	private BigDecimal suggestAdultPrice;
	
	private  Date updateDate; 
    /**产品上架状态*/
    private String activityStatus;
	private List<String> targetAreaNameList;
	
	private User createBy;
	private Long reviewCompanyId;
	/*成本录入审核状态*/
	private Integer review;	
	/*当前审核层级*/
	private Integer nowLevel;
	private Integer payNowLevel;
	private Long payReviewCompanyId;	
	private Integer payReview;	
	private Integer budgetType;	
	

	private Integer payUpdateBy;
	
	@Column(name="activityStatus",unique=false,nullable=false)
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	

	public Long getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Long supplyId) {
		this.supplyId = supplyId;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	@Column(name = "srcActivityId", unique = false, nullable =false)
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
	
	@Column(name="updateBy", unique=false, nullable=false)
	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
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


	@Transient
	public String getTargetAreaNamess() {
		return Collections3.convertToString(getTargetAreaNameList(), ",");
	}

	@Transient
	public List<String> getTargetAreaNameList() {
		return targetAreaNameList;
	}

	@Transient
	public void setTargetAreaNameList(List<String> targetAreaNameList) {
		this.targetAreaNameList = targetAreaNameList;
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
    	long nowDateTime = DateUtils.parseDate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).getTime();
    	Long diffDate = (nowDateTime-openDateTime)/(24*60*60*1000);
        return diffDate.intValue();
	}	

	public Integer getReview() {
		return review;
	}
	public void setReview(Integer review) {
		this.review = review;
	}
	

	public Integer getActivityKind() {
		return activityKind;
	}

	public void setActivityKind(Integer activityKind) {
		this.activityKind = activityKind;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createBy")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public Integer getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}

	public Long getProCompany() {
		return proCompany;
	}

	public void setProCompany(Long proCompany) {
		this.proCompany = proCompany;
	}

	public Integer getNopayReservePosition() {
		return nopayReservePosition;
	}

	public void setNopayReservePosition(Integer nopayReservePosition) {
		this.nopayReservePosition = nopayReservePosition;
	}
	@Column(name = "agpId", unique = false, nullable = true)
	public Long getAgpId() {
		return agpId;
	}

	public void setAgpId(Long agpId) {
		this.agpId = agpId;
	}
	@Column(name = "name", unique = false, nullable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "price", unique = false, nullable = true)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getReviewCompanyId() {
		return reviewCompanyId;
	}

	public void setReviewCompanyId(Long reviewCompanyId) {
		this.reviewCompanyId = reviewCompanyId;
	}

	public String getCurrencyMark() {
		return currencyMark;
	}

	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getPayNowLevel() {
		return payNowLevel;
	}

	public void setPayNowLevel(Integer payNowLevel) {
		this.payNowLevel = payNowLevel;
	}

	public Long getPayReviewCompanyId() {
		return payReviewCompanyId;
	}

	public void setPayReviewCompanyId(Long payReviewCompanyId) {
		this.payReviewCompanyId = payReviewCompanyId;
	}

	public Integer getPayReview() {
		return payReview;
	}

	public void setPayReview(Integer payReview) {
		this.payReview = payReview;
	}

	public Integer getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(Integer budgetType) {
		this.budgetType = budgetType;
	}

	public String getCostCreateBy() {
		return costCreateBy;
	}

	public void setCostCreateBy(String costCreateBy) {
		this.costCreateBy = costCreateBy;
	}

	public Integer getPayUpdateBy() {
		return payUpdateBy;
	}

	public void setPayUpdateBy(Integer payUpdateBy) {
		this.payUpdateBy = payUpdateBy;
	}
	
	
}
