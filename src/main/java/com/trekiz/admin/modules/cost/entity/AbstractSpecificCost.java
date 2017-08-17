package com.trekiz.admin.modules.cost.entity;

import java.math.BigDecimal;

import com.trekiz.admin.common.persistence.DataEntity;
import javax.persistence.*;

@MappedSuperclass
public class AbstractSpecificCost extends DataEntity{

	private static final long serialVersionUID = 1L;

	private Long id;
	
    //团期,签证,机票 id
    private Long activityId;

    //订单类型 2:散拼;6签证;7机票
    private Integer orderType; 
    
    private Integer quantity;
    
    private Integer currencyId;
    
    private Integer overseas;
    
    private String name;
    
    private BigDecimal price;
    
    private String comment;    
    //渠道商类型 
    private Integer supplyType;
    //渠道商 Id
    private Integer supplyId;  
      
    private String supplyName;
    
    private Integer budgetType;
    
    private Integer review;
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	@Column(name="name",unique=false,nullable=true)
	public String getName() {
		return name;
	}

	@Column(name="price",unique=false,nullable=true)
	public BigDecimal getPrice() {
		return price;
	}

	@Column(name="comment",unique=false,nullable=true)
	public String getComment() {
		return comment;
	}

	@Column(name="activityId",unique=false,nullable=true)
	public Long getActivityId() {
		return activityId;
	}

	

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}


	@Column(name="quantity",unique=false,nullable=true)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	
	@Column(name="supplyId",unique=false,nullable=true)
	public Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}

	@Column(name="supplyName",unique=false,nullable=true)
	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	@Column(name="currencyId",unique=false,nullable=true)
	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	@Column(name="overseas",unique=false,nullable=true)
	public Integer getOverseas() {
		return overseas;
	}

	public void setOverseas(Integer overseas) {
		this.overseas = overseas;
	}
	@Column(name="supplyType",unique=false,nullable=true)
	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}
	@Column(name="budgetType",unique=false,nullable=true)
	public Integer getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(Integer budgetType) {
		this.budgetType = budgetType;
	}
	
	@Column(name="orderType",unique=false,nullable=true)
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}
	
}
