package com.trekiz.admin.modules.cost.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cost_record_log")
public class CostRecordLog  {

   private Long id; 
   private Long rid;
   private Long costId;
   private String activityUuid;
   private String costName;
   private Integer orderType;
   private Integer nowLevel;
   private Long createBy;
   private Date createDate;
   private Integer result;
   private String remark; 
   private Integer logType;// 0 成本审核, 1 付款审核
   
   public CostRecordLog(){}
   public CostRecordLog(Long rid,int orderType,int nowLevel,Long createBy,Date createDate,Integer result,String remark){
	   this.rid=rid;
	   this.orderType=orderType;
	   this.nowLevel=nowLevel;
	   this.createBy=createBy;
	   this.createDate=createDate;
	   this.result=result;
	   this.remark=remark; 
   }   

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	@Column(name = "rid", unique = false, nullable = false)
    public Long getRid(){
        return this.rid;
    }
	
	@Column(name = "nowLevel", unique = false, nullable = false)
    public Integer getNowLevel(){
        return this.nowLevel;
	}
	
	@Column(name = "createBy", unique = false, nullable = false)
    public Long getCreateBy(){
        return this.createBy;
    }
	
	@Column(name = "createDate", unique = false, nullable = false)
    public Date getCreateDate(){
        return this.createDate;
	}
	
	@Column(name = "result", unique = false, nullable = false)
    public Integer getResult(){
        return this.result;
    }
	
	@Column(name = "remark", unique = false, nullable = true)
    public String getRemark(){
        return this.remark;
	}	
	
	public void setRid(Long rid) {
		this.rid = rid;
	}
	
	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setResult(Integer result) {
		this.result = result;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public Long getCostId() {
		return costId;
	}
	public void setCostId(Long costId) {
		this.costId = costId;
	}
	
	@Column(name = "activity_uuid", unique = false, nullable = true)
	public String getActivityUuid() {
		return activityUuid;
	}
	
	public void setActivityUuid(String activityUuid) {
		this.activityUuid = activityUuid;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	@Column(name = "logType", unique = false, nullable = true)
	public Integer getLogType() {
		return logType;
	}
}
