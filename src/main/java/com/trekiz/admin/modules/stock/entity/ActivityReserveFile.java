/**
 *
 */
package com.trekiz.admin.modules.stock.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.trekiz.admin.common.persistence.DataEntity;

  /**
 *  文件名: ActivityReserveFile
 *  功能: 记录切位凭证文件
 *  库存Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-03-26
 *  @version 1.0
 */
@Entity
@Table(name = "activityreservefile")
public class ActivityReserveFile extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 产品信息表ID外键 */
    private Long srcActivityId;
    /** 产品团期表ID */
    private Long activityGroupId;
    /** 渠道商基本信息表id */
    private Long agentId;
    
    private Long ReserveOrderId;
    /** 附件表id */
    private Long srcDocId;
    /** 文件名 */
    private String fileName;

	public ActivityReserveFile() {
		super();
	}

	public ActivityReserveFile(Long id){
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

    public void setSrcDocId(Long srcDocId ){
        this.srcDocId = srcDocId ;
    }

    public Long getSrcDocId(){
        return this.srcDocId;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getReserveOrderId() {
		return ReserveOrderId;
	}

	public void setReserveOrderId(Long reserveOrderId) {
		ReserveOrderId = reserveOrderId;
	}
	
	
}


