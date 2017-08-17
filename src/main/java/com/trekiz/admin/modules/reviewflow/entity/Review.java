package com.trekiz.admin.modules.reviewflow.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "review")
public class Review  {
   
   private Long id;

   private Long reviewCompanyId;
   
   private Integer topLevel;
   private Integer nowLevel;
   //产品类型 (单团,机票...)
   private Integer productType;  
    //流程类型 (退款,改签...)
   private Integer flowType;
   //订单 Id
   private String orderId;
   //公司Id
   private Long companyId;
   //游客 Id
   private Long travelerId;
   private String createReason;
   private String denyReason;   
   private Long createBy;
   private Long updateBy;
   private Date createDate;
   private Date updateDate;
   private String updateByName;
   private Integer status;
   private Integer active;
   private Long deptId;
   
   private Date printTime;
   private Integer printFlag;
   private Integer payStatus = 0;
   private Integer invoice_status = 0;
   private Date payConfirmDate;

	public Review(){}
   public Review(long reviewCompanyId,int topLevel,int nowLevel, int productType,String orderId,int flowType,long companyId,Long travelerId,String createReason,String denyReason,long createBy, Date createDate,Date updateDate,String updateByName,int status,int active,Long deptId){
	   this.reviewCompanyId=reviewCompanyId;
	   this.topLevel=topLevel;
	   this.nowLevel= nowLevel;
	   this.productType=productType;
	   this.orderId=orderId;
	   this.flowType=flowType;
	   if(travelerId!=null && travelerId>0) {
		   this.travelerId=travelerId;	
	    }
	   this.createReason= createReason;
	   this.denyReason=denyReason;
	   this.createBy=createBy;
	   this.companyId=companyId;
	   //this.updateBy=updateBy;
	   this.createDate=createDate;
	   this.updateDate=updateDate;
	   this.updateByName=updateByName;
	   this.status=status;
	   this.active=active;	
	   this.deptId=deptId;
   }
   
   

	public Review(Long id, Long reviewCompanyId, Integer topLevel,
		Integer nowLevel, Integer productType, Integer flowType,
		String orderId, Long companyId, Long travelerId, String createReason,
		String denyReason, Long createBy, Long updateBy, Date createDate,
		Date updateDate, String updateByName, Integer status, Integer active,
		Long deptId, Date printTime, Integer printFlag, Integer payStatus,Integer invoice_status) {
		super();
		this.id = id;
		this.reviewCompanyId = reviewCompanyId;
		this.topLevel = topLevel;
		this.nowLevel = nowLevel;
		this.productType = productType;
		this.flowType = flowType;
		this.orderId = orderId;
		this.companyId = companyId;
		this.travelerId = travelerId;
		this.createReason = createReason;
		this.denyReason = denyReason;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.updateByName = updateByName;
		this.status = status;
		this.active = active;
		this.deptId = deptId;
		this.printTime = printTime;
		this.printFlag = printFlag;
		this.payStatus = payStatus;
		this.invoice_status = invoice_status;
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
	
	@Column(name = "topLevel", unique = false, nullable = false)
	public Integer getTopLevel() {
		return topLevel;
	}
	@Column(name = "nowLevel", unique = false, nullable = false)
	public Integer getNowLevel() {
		return nowLevel;
	}
	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}
	@Column(name = "productType", unique = false, nullable = false)
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	
	@Column(name = "flowType", unique = false, nullable = false)
	public Integer getFlowType() {
		return flowType;
	}
	
	@Column(name = "orderId", unique = false, nullable = false)
	public String getOrderId() {
		return orderId;
	}
	
	@Column(name = "travelerId", unique = false, nullable = false)
	public Long getTravelerId() {
		return travelerId;
	}	
	
	@Column(name = "createBy", unique = false, nullable = false)
	public Long getCreateBy() {
		return createBy;
	}

	@Column(name = "updateBy", unique = false, nullable = true)
	public Long getUpdateBy() {
		return updateBy;
	}

	@Column(name = "createDate", unique = false, nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	@Column(name = "updateDate", unique = false, nullable = true)
	public Date getUpdateDate() {
		return updateDate;
	}

	@Column(name = "updateByName", unique = false, nullable =true)
	public String getUpdateByName() {
		return updateByName;
	}	
	
	@Column(name = "createReason", unique = false, nullable = true)
	public String getCreateReason() {
		return createReason;
	}
	
	@Column(name = "denyReason", unique = false, nullable = true)
	public String getDenyReason() {
		return denyReason;
	}
	
	@Column(name = "status", unique = false, nullable = false)
	public Integer getStatus() {
		return status;
	}
		@Column(name = "active", unique = false, nullable = false)
	public Integer getActive() {
		return active;
	}
	@Column(name = "companyId", unique = false, nullable = false)
		public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}	
		
	
	public void setCreateReason(String createReason) {
		this.createReason = createReason;
	}
	
	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}


	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}
	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}
	
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate =createDate;
	}	

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate =updateDate;
	}
	
	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}		

	public void setTopLevel(Integer topLevel) {
		this.topLevel = topLevel;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	@Column(name = "printTime", unique = false, nullable = false)
	public Date getPrintTime() {
		return printTime;
	}
	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}
	@Column(name = "printFlag", unique = false, nullable = false)
	public Integer getPrintFlag() {
		return printFlag;
	}
	public void setPrintFlag(Integer printFlag) {
		this.printFlag = printFlag;
	}
	

	 @Column(name = "review_company_id", unique = false, nullable = false)
	public Long getReviewCompanyId() {
		return reviewCompanyId;
	}
	public void setReviewCompanyId(Long reviewCompanyId) {
		this.reviewCompanyId = reviewCompanyId;
	}
	@Column(name = "payStatus", unique = false, nullable = false)
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	@Column(name = "invoice_status", unique = false, nullable = false)
	public Integer getinvoice_status() {
		return invoice_status;
	}
	public void setinvoice_status(Integer invoice_status) {
		this.invoice_status = invoice_status;
	}

	public Integer getInvoice_status() {
		return invoice_status;
	}

	public void setInvoice_status(Integer invoice_status) {
		this.invoice_status = invoice_status;
	}
	@Column(name = "Pay_confirm_date")
	public Date getPayConfirmDate() {
		return payConfirmDate;
	}

	public void setPayConfirmDate(Date payConfirmDate) {
		this.payConfirmDate = payConfirmDate;
	}
}
