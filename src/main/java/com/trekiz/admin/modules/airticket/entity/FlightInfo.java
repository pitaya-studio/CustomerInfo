package com.trekiz.admin.modules.airticket.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

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

import com.trekiz.admin.modules.sys.utils.AirlineUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Indexed;

import com.trekiz.admin.modules.sys.utils.DictUtils;

/**
 * 文件名: FlightInfo 功能: 产品Entity 修改记录:
 * 
 * @author liangjingming
 * @DateTime 2014-01-13
 * @version 1.0
 */
@Entity
@Table(name = "activity_flight_info")
public class FlightInfo implements Comparable<FlightInfo>{

	@SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

	// 删除标记（0：正常；1：删除；2：审核；）
	public static final String DEL_FLAG = "delFlag";
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";
	
	private Long id; // 主键

	private Long airticketId; // 机票产品关联ID

	private String leaveAirport; // 出发城市机场

	private String destinationAirpost; // 到达城市机场

	private String airlines;// 航空公司

	private Long currency_id; // 币种ID（关联币种表 currency）

	private String spaceGrade;// 舱位等级

	private String airspace;// 舱位

	private Date startTime;// 出发时间

	private Date arrivalTime;// 到达时间
	
	// 是否含税
	private Integer istax;
	// 税金
	private BigDecimal taxamt;
	
	/** 成人的成本价 */
	private BigDecimal settlementAdultPrice;
	/** 儿童的成本价 */
	private BigDecimal settlementcChildPrice;

	/** 特殊人群成本价 */
	private BigDecimal settlementSpecialPrice;

    private int ticket_area_type;// 机票区域类型
	
	private int number;   //航段顺序数值
	
	private String flightNumber;//航班号
	
	/** 备注 */
	private String remark;//
	
	private Map<String,Object> paraMap;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getTicket_area_type() {
		return ticket_area_type;
	}

	public void setTicket_area_type(int ticketAreaType) {
		ticket_area_type = ticketAreaType;
	}

	public Long getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(Long currencyId) {
		currency_id = currencyId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	private ActivityAirTicket activityAirTicket;

	@ContainedIn
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "airticketId", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	public ActivityAirTicket getActivityAirTicket() {
		return activityAirTicket;
	}

	public void setActivityAirTicket(ActivityAirTicket activityAirTicket) {
		this.activityAirTicket = activityAirTicket;
	}

	public FlightInfo() {
		super();
	}

	public FlightInfo(Long id) {
		super();
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

	@Column(name = "airticketId", unique = false, nullable = false, insertable = false, updatable = false)
	public Long getAirticketId() {
		return airticketId;
	}

	public void setAirticketId(Long airticketId) {
		this.airticketId = airticketId;
	}

	public String getLeaveAirport() {
		return leaveAirport;
	}

	public void setLeaveAirport(String leaveAirport) {
		this.leaveAirport = leaveAirport;
	}

	public String getDestinationAirpost() {
		return destinationAirpost;
	}

	public void setDestinationAirpost(String destinationAirpost) {
		this.destinationAirpost = destinationAirpost;
	}

	public String getAirlines() {
		return airlines;
	}

	public void setAirlines(String airlines) {
		this.airlines = airlines;
	}

	public String getSpaceGrade() {
		return spaceGrade;
	}

	public void setSpaceGrade(String spaceGrade) {
		this.spaceGrade = spaceGrade;
	}

	public String getAirspace() {
		return airspace;
	}

	public void setAirspace(String airspace) {
		this.airspace = airspace;
	}
	
	 @Override  
	    public int compareTo(FlightInfo other) {  
	        if(this.number < other.getNumber()){     
	            return -1;  
	        }else if(this.number > other.getNumber()){
	            return 1;  
	        }
	        return 0;         
	    }

	@Transient
	public Map<String, Object> getParaMap() {
		return paraMap;
	}

//舱位等级名称
	@Transient
	public String spaceGradeLabel() {
		if(spaceGrade==null)return "";
		return DictUtils.getDictLabel(String.valueOf(spaceGrade), "spaceGrade_Type", "");
	}
	
	//航空公司名称
	@Transient
	public String airlinesLabel() {
		Map<String, String> map=DictUtils.getLabelDesMap("traffic_name");
		for(Map.Entry<String, String> entry:map.entrySet()){
			if(entry.getKey().equals(airlines)) return entry.getValue();		        
		}   
        return "不限";	
	 }

	//航空公司名称
	@Transient
	public String airlinesLabel2() {
		String airlineName = AirlineUtils.getAirlineNameByAirlineCode(airlines);
		if(airlineName != null) {
			return airlineName;
		}
		return "不限";
	}
	
	//舱位名称
	@Transient
	public String airspaceLabel() {
		if(airspace==null)return "";
		return DictUtils.getDictLabel(String.valueOf(airspace), "airspace_Type", "");
	}


	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}  

}
