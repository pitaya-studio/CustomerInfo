/**
 *
 */
package com.trekiz.admin.modules.order.entity;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.sys.entity.Currency;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


 /**
 *  文件名: Costchange.java
 *  功能:  费用变更pojo
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-16 上午10:51:59
 *  @version 1.0
 */
@Entity
@Table(name = "costchange")
public class Costchange extends BaseEntity implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 费用名称 */
    private String costName;
    /** 单价 */
    private BigDecimal costPrice;
    /** 数量 */
    private BigDecimal costNum;
    /** 费用金额 */
    private BigDecimal costSum;
    /** 游客ID */
    private Long travelerId;
    /** 费用币种 */
    private Currency priceCurrency;

    /** 审批状态 */
    private Integer status = 2;

    /** 业务类型 */
    private Integer businessType = 0;

    /**审批uuid*/
    private String reviewUuid;

    
	public Costchange() {
		super();
	}

	public Costchange(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCostName(String costName ){
        this.costName = costName ;
    }

    @Length(min=0, max=50)
    public String getCostName(){
        return this.costName;
    }

    public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getCostNum() {
		return costNum;
	}

	public void setCostNum(BigDecimal costNum) {
		this.costNum = costNum;
	}

	public void setCostSum(BigDecimal costSum ){
        this.costSum = costSum ;
    }

    public BigDecimal getCostSum(){
        return this.costSum;
    }

    public void setTravelerId(Long travelerId ){
        this.travelerId = travelerId ;
    }

    public Long getTravelerId(){
        return this.travelerId;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_currency", referencedColumnName = "currency_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public Currency getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(Currency priceCurrency) {
		this.priceCurrency = priceCurrency;
	}

     public Integer getStatus() {
         return status;
     }

     public Integer getBusinessType() {
         return businessType;
     }

     public void setStatus(Integer status) {
         this.status = status;
     }

     public void setBusinessType(Integer businessType) {
         this.businessType = businessType;
     }

     @Column(name = "review_uuid")
     public String getReviewUuid() {
         return reviewUuid;
     }

     public void setReviewUuid(String reviewUuid) {
         this.reviewUuid = reviewUuid;
     }
 }


