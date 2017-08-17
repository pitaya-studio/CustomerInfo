package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trekiz.admin.modules.sys.entity.User;



@Entity
@Table(name = "hotelgroup_cost_view")
@DynamicInsert
public class HotelGroupCostView implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private Long id; // 编号	
	
	private String name;
	private String currencyMark;
	private Integer currencyId;
	private BigDecimal  price;
	private Integer quantity;
	private BigDecimal  totalPrice;
	private String supplyName;	

	/**团号 */
	private Long productId;
	private String groupCode;
	
	private Long supplyId;
	private Integer supplyType;

	/*产品名*/
	private String activityName;
	private String activityUuid;
	private String productUuid;
	
	/** 产品中一个确定的出团日期 */
	private Date groupOpenDate;
	/** 产品中的一个确定的截团日期 */
	private Date groupEndDate;
	private  Date updateDate; 
	
	private String islandName;
	private String hotelName;
	private String createByName;
	private Integer remNumber;
	private String totalRoom;
	
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
	private Long wholesalerId;
	
	private Integer payUpdateBy;

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

	
	
	@Column(name = "groupCode", unique = false, nullable = true)
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
		
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Column(name = "groupOpendate", unique = false, nullable = true)
	public Date getGroupOpenDate() {
		return groupOpenDate;
	}
	public void setGroupOpenDate(Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}
	
	
	@Column(name = "updateDate", unique = false, nullable = true)
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	} 
	

	public Integer getReview() {
		return review;
	}
	public void setReview(Integer review) {
		this.review = review;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	

	public String getActivityUuid() {
		return activityUuid;
	}

	public void setActivityUuid(String activityUuid) {
		this.activityUuid = activityUuid;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public Date getGroupEndDate() {
		return groupEndDate;
	}

	public void setGroupEndDate(Date groupEndDate) {
		this.groupEndDate = groupEndDate;
	}

	public Long getWholesalerId() {
		return wholesalerId;
	}

	public void setWholesalerId(Long wholesalerId) {
		this.wholesalerId = wholesalerId;
	}

	public String getIslandName() {
		return islandName;
	}

	public void setIslandName(String islandName) {
		this.islandName = islandName;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public Integer getRemNumber() {
		return remNumber;
	}

	public void setRemNumber(Integer remNumber) {
		this.remNumber = remNumber;
	}

	public String getTotalRoom() {
		return totalRoom;
	}

	public void setTotalRoom(String totalRoom) {
		this.totalRoom = totalRoom;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getPayUpdateBy() {
		return payUpdateBy;
	}

	public void setPayUpdateBy(Integer payUpdateBy) {
		this.payUpdateBy = payUpdateBy;
	}
	
	
	
}
