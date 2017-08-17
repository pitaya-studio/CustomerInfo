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


/**
 * 文件名: ActivitygroupView 视图,运控产品切位列表使用
 * 
 * @author WangDuo
 * @DateTime 2014-11-15
 * @version 1.0
 * 视图定义如下：
  CREATE VIEW activitygroup_view AS 
SELECT
  `agp`.`id`                        AS `id`,
  `agp`.`groupCode`                 AS `groupCode`,
  `agp`.`srcActivityId`             AS `srcActivityId`,
  `activity`.`acitivityName`        AS `acitivityName`,
  `activity`.`activitySerNum`       AS `activitySerNum`,
  `activity`.`fromArea`             AS `fromArea`,
  `activity`.`targetArea`           AS `targetArea`,
  `agp`.`groupOpenDate`             AS `groupOpenDate`,
  `agp`.`groupCloseDate`            AS `groupCloseDate`,
  `agp`.`payReservePosition`        AS `payReservePosition`,
  `agp`.`nopayReservePosition`      AS `nopayReservePosition`,
  `agp`.`soldPayPosition`           AS `soldPayPosition`,
  `agp`.`freePosition`              AS `freePosition`,
  `agp`.`planPosition`              AS `planPosition`,
  `agp`.`review`                    AS `review`,
  `agp`.`nowLevel`                  AS `nowLevel`,
  `activity`.`settlementAdultPrice` AS `settlementAdultPrice`,
  `activity`.`id`                   AS `productId`,
  `activity`.`activity_kind`        AS `activityKind`,
  `activity`.`proCompany`           AS `proCompany`,
  `activity`.`createDate`           AS `createDate`,
  `activity`.`updateDate`           AS `updateDate`,
  `activity`.`suggestAdultPrice`    AS `suggestAdultPrice`,
  `activity`.`createBy`             AS `createBy`,
  `activity`.`opUserId`             AS `opUserId`
FROM (`activitygroup` `agp`
   LEFT JOIN `travelactivity` `activity`
     ON ((`agp`.`srcActivityId` = `activity`.`id`)))
WHERE (`agp`.`delFlag` = '0') 
 */

@Entity
@Table(name = "activitygroup_view")
@DynamicInsert
public class ActivityGroupView implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private Long id; // 编号	
	/**团号 */
	private Long productId;
	private String groupCode;
	private Long srcActivityId;

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
	private BigDecimal settlementAdultPrice;
	
	
	private BigDecimal agpsettlementAdultPrice;
	private BigDecimal agpsettlementcChildPrice;
	private BigDecimal agpsettlementSpecialPrice;
	private BigDecimal singleDiff;

	private Integer activityKind;

	/** 建议成人最低零售价 */
	private BigDecimal suggestAdultPrice;
	
	private  Date updateDate; 
    /**产品上架状态*/
    private String activityStatus;
	private List<String> targetAreaNameList;
	
	private User createBy;
	
	private Integer opUserId;
	
	/*成本录入审核状态*/
	private Integer review;
	
	/*当前审核层级*/
	private Integer nowLevel;
	
	private  Date createDate; 
	
	//币种信息
	private String currencyType;
	
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
	public BigDecimal getSettlementAdultPrice() {
		return settlementAdultPrice;
	}
	public void setSettlementAdultPrice(BigDecimal settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}
	
	@Column(name = "agpsettlementAdultPrice", unique = false, nullable = true)
	public BigDecimal getAgpsettlementAdultPrice() {
		return agpsettlementAdultPrice;
	}

	public void setAgpsettlementAdultPrice(BigDecimal agpsettlementAdultPrice) {
		this.agpsettlementAdultPrice = agpsettlementAdultPrice;
	}

	@Column(name = "agpsettlementcChildPrice", unique = false, nullable = true)
	public BigDecimal getAgpsettlementcChildPrice() {
		return agpsettlementcChildPrice;
	}

	public void setAgpsettlementcChildPrice(BigDecimal agpsettlementcChildPrice) {
		this.agpsettlementcChildPrice = agpsettlementcChildPrice;
	}

	@Column(name = "agpsettlementSpecialPrice", unique = false, nullable = true)
	public BigDecimal getAgpsettlementSpecialPrice() {
		return agpsettlementSpecialPrice;
	}

	public void setAgpsettlementSpecialPrice(BigDecimal agpsettlementSpecialPrice) {
		this.agpsettlementSpecialPrice = agpsettlementSpecialPrice;
	}
	
	@Column(name = "singleDiff", unique = false, nullable = true)
	public BigDecimal getSingleDiff() {
		return singleDiff;
	}

	public void setSingleDiff(BigDecimal singleDiff) {
		this.singleDiff = singleDiff;
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

	public Integer getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(Integer opUserId) {
		this.opUserId = opUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	

}
