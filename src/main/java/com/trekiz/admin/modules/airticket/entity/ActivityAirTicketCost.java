package com.trekiz.admin.modules.airticket.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.sys.utils.DictUtils;

/**
 * 文件名: airticketproducts 功能: 机票产品Entity 修改记录:
 * 
 * @author xiaojun
 * @DateTime 2014-09-15
 * @version 1.0
 */
@Entity
@Table(name = "activity_airticket_cost_view")
public class ActivityAirTicketCost extends DataEntity {

	private static final long serialVersionUID = 1L;

	private Long id; // 主键
	
	private Long airId;
	
	private String name;
	private String costCreateBy;
	private String currencyMark;
	private BigDecimal price;	
	private Integer quantity;
	private BigDecimal totalPrice;	
	private String supplyName;
    private Long reviewCompanyId;
	private String airType; // 机票类型

	private String ticket_area_type;// 机票区域类型

	private java.util.Date startingDate; // 出发日期

	private java.util.Date returnDate;// 返程日期

	private String departureCity;// 出发城市

	private String arrivedCity;// 到达城市
	
	private Map<String, String> paraMap;
	
	private Long spaceGrade;
	
	private String whetherTrip;
	
	private String airlines;

	private Long currency_id; // 币种ID（关联币种表 currency）

	private Set<ActivityFile> activityFiles; // 附件文件

	private List<FlightInfo> flightInfos; // 多段航班行程
	
    private List<IntermodalStrategy> intermodalStrategies;//联运
    
    /*切位订单 */	
   	private List<AirticketActivityReserve> airticketReserveList;
    
   	private List<CostRecord> costRecordList;
   	
	private Set<AirTicketFile> airTicketFiles;

	private Long proCompany; // 产品机构ID

	private String remark; // 备注

	private java.util.Date depositTime; // 订金时限

	private java.util.Date cancelTimeLimit; // 取消时限

	private String productCode;// 产品编号
	
	private String reservationStutas ; //预定状态
	
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityId", referencedColumnName = "id")	
	public List<CostRecord> getCostRecordList() {
		return costRecordList;
	}
	
	public void setCostRecordList(List<CostRecord> costRecordList) {
		this.costRecordList = costRecordList;
	}
	
	
	@Transient
	public String getReservationStutas() {
		return reservationStutas;
	}

	public void setReservationStutas(String reservationStutas) {
		this.reservationStutas = reservationStutas;
	}

	// 是否含税
	private Integer istax;
	// 税金
	private BigDecimal taxamt;
	// 订金
	private BigDecimal depositamt;
	// 有效期限
	private java.util.Date limitTime;

	private int reservationsNum;// 预收人数
	
	/** 产品状态 */
	private int  productStatus;
	
	private String cost;
	private String income;
	

	/**
	 * 余位人数
	 */
	private int freePosition;

	/** 成人的成本价 */
	private BigDecimal settlementAdultPrice;
	/** 儿童的成本价 */
	private BigDecimal settlementcChildPrice;

	/** 特殊人群 成本价 */
	private BigDecimal settlementSpecialPrice;

	/** 特殊人群备注 */
	private String specialremark;

	/** 目标区域名称列表*/
	private String targetAreaNames;
	/**
	 * 目标区域Id列表
	 */
	private String targetAreaIds;
	/**
	 * 预定数
	 */
	private String reserveNumber;
	
	/**
	 * 订单数
	 */
	private int orderNumber;
	
	/**
	 * 切位数
	 */
	private int payReserveNumber;

	private Integer activityScope;
	
	private Long isRecord;

    /** 各渠道总的占位人数 */
    private int nopayReservePosition;
    /** 各渠道总的切位人数置 */
    private int payReservePosition;
    /** 售出占位 */
    private int soldNopayPosition;
    /** 售出切位 */
    private int soldPayPosition;
    
    /** 付款方式：预占位 0表示没有使用，1表示使用 */
	private Integer payMode_advance;
	/** 付款方式：订金支付 0表示没有使用，1表示使用 */
	private Integer payMode_deposit;
	/** 支付方式：全款支付 0表示没有使用，1表示使用 */
	private Integer payMode_full;
	/** 支付方式：计调确认占位 0表示没有使用，1表示使用 */
//	private Integer payMode_op;
	/** 预占位保留天数 */
	private Integer remainDays_advance;
	/** 订金占位保留天数 */
	private Integer remainDays_deposit;
	
	private java.util.Date outTicketTime;
	
	private Integer intermodalType;

	private String outArea;
	
	private Long recordId;
	/**成本录入审核:0未通过,1待审核,2已通过, 4待录入*/
	private Integer review;
	
	/**成本审核当前层级*/
	private Integer nowLevel;
	
	/**成本录入锁定：0:没锁定，1：锁定*/
	private Integer lockStatus;
	private Integer payNowLevel;
	private Long payReviewCompanyId;	
	private Integer payReview;	
	private Integer budgetType;	
	/**部门ID*/
	private Long deptId;
	
	private Integer payUpdateBy;

    public int getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(int productStatus) {
		this.productStatus = productStatus;
	}


	@Transient
	public int getPayReserveNumber() {
		return payReserveNumber;
	}

	public void setPayReserveNumber(int payReserveNumber) {
		this.payReserveNumber = payReserveNumber;
	}

	@Transient
	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Transient
	public String getReserveNumber() {
		return reserveNumber;
	}

	public void setReserveNumber(String reserveNumber) {
		this.reserveNumber = reserveNumber;
	}

	@Transient
	public String getTargetAreaNames() {
		return targetAreaNames;
	}

	public void setTargetAreaNames(String targetAreaNames) {
		this.targetAreaNames = targetAreaNames;
	}
	@Transient
	public String getTargetAreaIds() {
		return targetAreaIds;
	}

	public void setTargetAreaIds(String targetAreaIds) {
		this.targetAreaIds = targetAreaIds;
	}
	
	public int getReservationsNum() {
		return reservationsNum;
	}

	public void setReservationsNum(int reservationsNum) {
		this.reservationsNum = reservationsNum;
	}

	@Column(name = "activity_scope")
	public Integer getActivityScope() {
		return activityScope;
	}

	public void setActivityScope(Integer activityScope) {
		this.activityScope = activityScope;
	}

	public String getSpecialremark() {
		return specialremark;
	}

	public void setSpecialremark(String specialremark) {
		this.specialremark = specialremark;
	}

	// 是否分段计价
	private Integer isSection;

	public Integer getIsSection() {
		return isSection;
	}

	public void setIsSection(Integer isSection) {
		this.isSection = isSection;
	}

	public BigDecimal getSettlementAdultPrice() {
		return settlementAdultPrice;
	}

	public void setSettlementAdultPrice(BigDecimal settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}

	public BigDecimal getSettlementcChildPrice() {
		return settlementcChildPrice;
	}

	public void setSettlementcChildPrice(BigDecimal settlementcChildPrice) {
		this.settlementcChildPrice = settlementcChildPrice;
	}

	public BigDecimal getSettlementSpecialPrice() {
		return settlementSpecialPrice;
	}

	public void setSettlementSpecialPrice(BigDecimal settlementSpecialPrice) {
		this.settlementSpecialPrice = settlementSpecialPrice;
	}

	public int getFreePosition() {
		return freePosition;
	}

	public void setFreePosition(int freePosition) {
		this.freePosition = freePosition;
	}

	public Integer getIstax() {
		return istax;
	}

	public void setIstax(Integer istax) {
		this.istax = istax;
	}

	public BigDecimal getTaxamt() {
		return taxamt;
	}

	public void setTaxamt(BigDecimal taxamt) {
		this.taxamt = taxamt;
	}

	public BigDecimal getDepositamt() {
		return depositamt;
	}

	public void setDepositamt(BigDecimal depositamt) {
		this.depositamt = depositamt;
	}

	public java.util.Date getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(java.util.Date limitTime) {
		this.limitTime = limitTime;
	}

	@Column(name = "product_code")
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/** 产品类型 id **/

	private Integer product_type_id;

	public Integer getProduct_type_id() {
		return product_type_id;
	}

	public void setProduct_type_id(Integer productTypeId) {
		product_type_id = productTypeId;
	}
    /*机票列表 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "activityAirTicket", targetEntity = FlightInfo.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@OrderBy("number") 
	public List<FlightInfo> getFlightInfos() {
		return flightInfos;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "activityAirTicket", targetEntity = ActivityFile.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<ActivityFile> getActivityFiles() {
		return activityFiles;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "activityAirTicket", targetEntity = AirTicketFile.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<AirTicketFile> getAirTicketFiles() {
		return airTicketFiles;
	}

	public void setAirTicketFiles(Set<AirTicketFile> airTicketFiles) {
		this.airTicketFiles = airTicketFiles;
	}
	
	public void setFlightInfos(List<FlightInfo> flightInfos) {
		this.flightInfos = flightInfos;
	}

	public void setActivityFiles(Set<ActivityFile> activityFiles) {
		this.activityFiles = activityFiles;
	}

	public Long getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(Long currencyId) {
		currency_id = currencyId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAirType() {
		return airType;
	}

	public void setAirType(String airType) {
		this.airType = airType;
	}

	public java.util.Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(java.util.Date startingDate) {
		this.startingDate = startingDate;
	}

	public java.util.Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(java.util.Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getArrivedCity() {
		return arrivedCity;
	}

	public void setArrivedCity(String arrivedCity) {
		this.arrivedCity = arrivedCity;
	}

	public Long getProCompany() {
		return proCompany;
	}

	public void setProCompany(Long proCompany) {
		this.proCompany = proCompany;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public java.util.Date getDepositTime() {
		return depositTime;
	}

	public void setDepositTime(java.util.Date depositTime) {
		this.depositTime = depositTime;
	}

	public java.util.Date getCancelTimeLimit() {
		return cancelTimeLimit;
	}

	public void setCancelTimeLimit(java.util.Date cancelTimeLimit) {
		this.cancelTimeLimit = cancelTimeLimit;
	}

	public String getTicket_area_type() {
		return ticket_area_type;
	}

	public void setTicket_area_type(String ticketAreaType) {
		ticket_area_type = ticketAreaType;
	}


	@Transient
	public Long getSpaceGrade() {
		return spaceGrade;
	}

	public void setSpaceGrade(Long spaceGrade) {
		this.spaceGrade = spaceGrade;
	}

	public String getAirlines() {
		return airlines;
	}

	public void setAirlines(String airlines) {
		this.airlines = airlines;
	}

	public String getWhetherTrip() {
		return whetherTrip;
	}

	public void setWhetherTrip(String whetherTrip) {
		this.whetherTrip = whetherTrip;
	}
	
	@Transient
	public Map<String, String> getParaMap() {
		return paraMap;
	}

	public void setParaMap(Map<String, String> paraMap) {
		this.paraMap = paraMap;
	}  


    public int getNopayReservePosition() {
        return nopayReservePosition;
    }

    public void setNopayReservePosition(int nopayReservePosition) {
        this.nopayReservePosition = nopayReservePosition;
    }

    public int getPayReservePosition() {
        return payReservePosition;
    }

    public void setPayReservePosition(int payReservePosition) {
        this.payReservePosition = payReservePosition;
    }

    public int getSoldNopayPosition() {
        return soldNopayPosition;
    }

    public void setSoldNopayPosition(int soldNopayPosition) {
        this.soldNopayPosition = soldNopayPosition;
    }

    public int getSoldPayPosition() {
        return soldPayPosition;
    }

    public void setSoldPayPosition(int soldPayPosition) {
        this.soldPayPosition = soldPayPosition;
    }

	public Long getIsRecord() {
		return isRecord;
	}

	public void setIsRecord(Long isRecord) {
		this.isRecord = isRecord;
	}

	public Integer getPayMode_advance() {
		return payMode_advance;
	}

	public void setPayMode_advance(Integer payMode_advance) {
		this.payMode_advance = payMode_advance;
	}

	public Integer getPayMode_deposit() {
		return payMode_deposit;
	}

	public void setPayMode_deposit(Integer payMode_deposit) {
		this.payMode_deposit = payMode_deposit;
	}

	public Integer getPayMode_full() {
		return payMode_full;
	}

	public void setPayMode_full(Integer payMode_full) {
		this.payMode_full = payMode_full;
	}
	
//	public Integer getPayMode_op() {
//		return payMode_op;
//	}
//
//	public void setPayMode_op(Integer payMode_op) {
//		this.payMode_op = payMode_op;
//	}

	public Integer getRemainDays_advance() {
		return remainDays_advance;
	}

	public void setRemainDays_advance(Integer remainDays_advance) {
		this.remainDays_advance = remainDays_advance;
	}

	public Integer getRemainDays_deposit() {
		return remainDays_deposit;
	}

	public void setRemainDays_deposit(Integer remainDays_deposit) {
		this.remainDays_deposit = remainDays_deposit;
	}

	public java.util.Date getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(java.util.Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityAirTicket", targetEntity = IntermodalStrategy.class, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public List<IntermodalStrategy> getIntermodalStrategies() {
        return intermodalStrategies;
    }

    public void setIntermodalStrategies(List<IntermodalStrategy> intermodalStrategies) {
        this.intermodalStrategies = intermodalStrategies;
    }

	public Integer getIntermodalType() {
		return intermodalType;
	}

	public void setIntermodalType(Integer intermodalType) {
		this.intermodalType = intermodalType;
	}

	public String getOutArea() {
		return outArea;
	}

	public void setOutArea(String outArea) {
		this.outArea = outArea;
	}

	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityId", referencedColumnName = "id")
	public List<AirticketActivityReserve> getAirticketReserveList() {
		return airticketReserveList;
	}
  


	public void setAirticketReserveList(List<AirticketActivityReserve> airticketReserveList) {
		this.airticketReserveList = airticketReserveList;
	}
	
	//出发城市
	@Transient
	public String departureCityLabel() {
		if(departureCity==null)return "";
		return DictUtils.getDictLabel(String.valueOf(departureCity), "from_area", "");
	}
	
	
	//到达城市
		@Transient
		public String arrivedCityLabel() {
			if(arrivedCity==null)return "";
			return DictUtils.getDictLabel(String.valueOf(arrivedCity), "from_area", "");
		}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Integer getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getAirId() {
		return airId;
	}

	public void setAirId(Long airId) {
		this.airId = airId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrencyMark() {
		return currencyMark;
	}

	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public Long getReviewCompanyId() {
		return reviewCompanyId;
	}

	public void setReviewCompanyId(Long reviewCompanyId) {
		this.reviewCompanyId = reviewCompanyId;
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

