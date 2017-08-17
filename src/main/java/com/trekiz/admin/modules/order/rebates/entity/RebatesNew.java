package com.trekiz.admin.modules.order.rebates.entity;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.traveler.entity.Traveler;

/**
 * 新返佣记录表
 * @author yakun.bai
 * @Date 2015-11-26
 */
@Entity
@Table(name = "rebates")
public class RebatesNew extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long orderId;
	private Long travelerId;
	private Traveler traveler;
	private Long currencyId;
	/** 汇率 */
	private BigDecimal currencyExchangerate;
	private Currency currency;
	private String totalMoney;
	private String costname;
	private String rid;
    // 原返佣金额(多币种流水号)
	private String oldRebates;
	private String newRebates;
	private BigDecimal rebatesDiff;
	private String remark;
	private Integer orderType;
	private ReviewNew review;
	private String nowCumulative;
	private String allCumulative;
	
	private String rebatesdiffCurrName;//返佣差额币种
	private String rebatesdiffString;//返佣差额
	private String rebatesdiffString1;//累计差额
	private String rebatesdiffString2;//结算价总额
	private int rebatesSign = 0;//有无预计返佣金额标识
	private String rebatesStr;//预计返佣金额字符串
	public RebatesNew() {
		super();
	}
	
	public RebatesNew(Long orderId, Traveler traveler, Long currencyId, String totalMoney,
			String costname, String oldRebates, 
			BigDecimal rebatesDiff,String newRebates, String remark,Integer orderType,String nowCumulative) {
		super();
		this.orderId = orderId;
		this.traveler = traveler;
		this.currencyId = currencyId;
		this.currencyId = currencyId;
		this.totalMoney = totalMoney;
		this.costname = costname;
		this.oldRebates = oldRebates;
		this.rebatesDiff = rebatesDiff;
		this.newRebates = newRebates;
		this.remark = remark;
		this.orderType = orderType;
		this.nowCumulative = nowCumulative;
	}
	
	
	public RebatesNew(Long orderId, Traveler traveler, Long currencyId, String totalMoney,
			String costname, String oldRebates, 
			BigDecimal rebatesDiff,String newRebates, String remark,Integer orderType) {
		super();
		this.orderId = orderId;
		this.traveler = traveler;
		this.currencyId = currencyId;
		this.currencyId = currencyId;
		this.totalMoney = totalMoney;
		this.costname = costname;
		this.oldRebates = oldRebates;
		this.rebatesDiff = rebatesDiff;
		this.newRebates = newRebates;
		this.remark = remark;
		this.orderType = orderType;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="travelerId",unique=false,nullable=true,insertable=false,updatable=false)
	public Long getTravelerId() {
		return travelerId;
	}
	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelerId", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}
	
	@Column(name="currencyId",unique=false,nullable=false,insertable=false,updatable=false)
	public Long getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}
	
	@Column(name="currencyExchangerate")
	public BigDecimal getCurrencyExchangerate() {
		return currencyExchangerate;
	}

	public void setCurrencyExchangerate(BigDecimal currencyExchangerate) {
		this.currencyExchangerate = currencyExchangerate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", referencedColumnName = "currency_id")
    @NotFound(action = NotFoundAction.IGNORE)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Column(name="totalMoney")
	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMondy) {
		this.totalMoney = totalMondy;
	}

	public BigDecimal getRebatesDiff() {
		return rebatesDiff;
	}
	public void setRebatesDiff(BigDecimal rebatesDiff) {
		this.rebatesDiff = rebatesDiff;
	}
	public String getCostname() {
		return costname;
	}
	public void setCostname(String costname) {
		this.costname = costname;
	}
	
	@Column(name="nowCumulative")
	public String getNowCumulative() {
		return nowCumulative;
	}

	public void setNowCumulative(String nowCumulative) {
		this.nowCumulative = nowCumulative;
	}
	@Column(name="allCumulative")
	public String getAllCumulative() {
		return allCumulative;
	}

	public void setAllCumulative(String allCumulative) {
		this.allCumulative = allCumulative;
	}

	@Column(name="oldRebates")
	public String getOldRebates() {
		return oldRebates;
	}
	public void setOldRebates(String oldRebates) {
		this.oldRebates = oldRebates;
	}
	
	@Column(name="newRebates")
	public String getNewRebates() {
		return newRebates;
	}
	public void setNewRebates(String newRebates) {
		this.newRebates = newRebates;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToOne(cascade=CascadeType.ALL,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="rid",referencedColumnName="id")
	public ReviewNew getReview() {
		return review;
	}

	public void setReview(ReviewNew review) {
		this.review = review;
	}
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	@Column(name="rid",unique=false,nullable=true,insertable=false,updatable=false)
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}
	
	@Transient
	public String getRebatesdiffString() {
		return rebatesdiffString;
	}

	public void setRebatesdiffString(String rebatesdiffString) {
		this.rebatesdiffString = rebatesdiffString;
	}
	@Transient
	public String getRebatesdiffString1() {
		return rebatesdiffString1;
	}

	public void setRebatesdiffString1(String rebatesdiffString1) {
		this.rebatesdiffString1 = rebatesdiffString1;
	}
	@Transient
	public String getRebatesdiffString2() {
		return rebatesdiffString2;
	}

	public void setRebatesdiffString2(String rebatesdiffString2) {
		this.rebatesdiffString2 = rebatesdiffString2;
	}
	@Transient
	public String getRebatesdiffCurrName() {
		return rebatesdiffCurrName;
	}

	public void setRebatesdiffCurrName(String rebatesdiffCurrName) {
		this.rebatesdiffCurrName = rebatesdiffCurrName;
	}
	@Transient
	public int getRebatesSign() {
		return rebatesSign;
	}

	public void setRebatesSign(int rebatesSign) {
		this.rebatesSign = rebatesSign;
	}
	@Transient
	public String getRebatesStr() {
		return rebatesStr;
	}

	public void setRebatesStr(String rebatesStr) {
		this.rebatesStr = rebatesStr;
	}
}
