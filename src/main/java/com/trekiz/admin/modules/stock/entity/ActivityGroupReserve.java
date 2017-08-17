/**
 *
 */
package com.trekiz.admin.modules.stock.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;

  /**
 *  文件名: Activitygroupreserve
 *  功能:
 *  切位信息Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-02-10
 *  @version 1.0
 */
@Entity
@Table(name = "activitygroupreserve")
public class ActivityGroupReserve extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 产品信息表ID外键 */
    private Long srcActivityId;
    /** 产品团期表ID */
    private Long activityGroupId; 

	/** 渠道商基本信息表id */
    private Long agentId;
    /** 0,订金占位；1,全款占位 */
    private Integer reserveType;
    /** 切位人数 */
    private Integer payReservePosition;
    /** 已售出切位 */
    private Integer soldPayPosition;
    /** 订金金额 */
    private BigDecimal frontMoney;
    /** 剩余的切位人数 */
    private Integer leftpayReservePosition;
    /** 剩余的订金金额 */
    private BigDecimal leftFontMoney;
    /** 切位备注 */
    private String remark;
    /** 还位备注 */
    private String returnRemark;
    /** 预订人 */
    private String reservation;
    /** 支付方式 */
    private Integer payType; 
    //草稿箱记录uuid
    private String reserveTempUuid;
    
	@PrePersist
	public void prePersist(){
		this.leftFontMoney = this.frontMoney;
		this.leftpayReservePosition = this.payReservePosition;
//		super.prePersist();
	}

	public ActivityGroupReserve() {
		super();
	}

	public ActivityGroupReserve(Long id){
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

	    public void setSrcActivityId(Long srcActivityId ){
        this.srcActivityId = srcActivityId ;
    }

    public Long getSrcActivityId(){
        return this.srcActivityId;
    }

    public void setActivityGroupId(Long activityGroupId ){
        this.activityGroupId = activityGroupId ;
    }

    public Long getActivityGroupId(){
        return this.activityGroupId;
    }

    public void setAgentId(Long agentId ){
        this.agentId = agentId ;
    }

    public Long getAgentId(){
        return this.agentId;
    }

    public Integer getReserveType() {
		return reserveType;
	}

	public void setReserveType(Integer reserveType) {
		this.reserveType = reserveType;
	}

	public void setPayReservePosition(Integer payReservePosition ){
        this.payReservePosition = payReservePosition ;
    }

	@NotNull
    public Integer getPayReservePosition(){
        return this.payReservePosition;
    }

    public void setFrontMoney(BigDecimal frontMoney ){
        this.frontMoney = frontMoney ;
    }

    @NotNull
    public BigDecimal getFrontMoney(){
        return this.frontMoney;
    }

    @NotNull
    public Integer getLeftpayReservePosition() {
		return leftpayReservePosition;
	}

	public void setLeftpayReservePosition(Integer leftpayReservePosition) {
		this.leftpayReservePosition = leftpayReservePosition;
	}

	@NotNull
	public BigDecimal getLeftFontMoney() {
		return leftFontMoney;
	}

	public void setLeftFontMoney(BigDecimal leftFontMoney) {
		this.leftFontMoney = leftFontMoney;
	}

	public void setRemark(String remark ){
        this.remark = remark ;
    }

    @Length(min=0, max=200)
    public String getRemark(){
        return this.remark;
    }
    
    @Transient
	public String getAgentName() {
		return AgentInfoUtils.getAgentName(this.agentId);
	}

    @Length(min=0, max=10)
	public String getReservation() {
		return reservation;
	}

	public void setReservation(String reservation) {
		this.reservation = reservation;
	}

	@Transient
	public String getPayTypeLabel() {
		if(payType==null)return "";
		return DictUtils.getDictLabel(String.valueOf(payType), "offlineorder_pay_type", "");
	}

	public Integer getPayType() {
		return payType;
	}
	
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	
	@Transient
	public String getReserveTempUuid() {
		return reserveTempUuid;
	}

	public void setReserveTempUuid(String reserveTempUuid) {
		this.reserveTempUuid = reserveTempUuid;
	}

	public Integer getSoldPayPosition() {
			return soldPayPosition;
		}
	
	public void setSoldPayPosition(Integer soldPayPosition) {
			this.soldPayPosition = soldPayPosition;
		}

	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
	
	
}


