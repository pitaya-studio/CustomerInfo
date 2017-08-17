package com.trekiz.admin.modules.stock.entity;

import com.trekiz.admin.common.persistence.DataEntity;

import javax.persistence.*;

/**
 * Created by ZhengZiyu on 2014/11/5.
 */
@Entity
@Table(name = "airticketreservefile")
public class AirticketReserveFile extends DataEntity{
    private static final long serialVersionUID = 1L;
    private Long id; 		// 编号

    /** 产品信息表ID外键 */
    private Long airticketActivityId;
    /** 渠道商基本信息表id */
    private Long agentId;
    /** 切位订单 id */
    private Long reserveOrderId;
    /** 附件表id */
    private Long srcDocId;
    /** 文件名 */
    private String fileName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAirticketActivityId() {
        return airticketActivityId;
    }

    public void setAirticketActivityId(Long airticketActivityId) {
        this.airticketActivityId = airticketActivityId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getSrcDocId() {
        return srcDocId;
    }

    public void setSrcDocId(Long srcDocId) {
        this.srcDocId = srcDocId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name="reserveOrderId")
	public Long getReserveOrderId() {
		return reserveOrderId;
	}

	public void setReserveOrderId(Long reserveOrderId) {
		this.reserveOrderId = reserveOrderId;
	}
    
}
