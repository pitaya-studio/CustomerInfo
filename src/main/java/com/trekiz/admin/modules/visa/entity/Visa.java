package com.trekiz.admin.modules.visa.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.trekiz.admin.common.persistence.DataEntityTTS;
import com.trekiz.admin.modules.sys.entity.DocInfo;
/**
 * 签证类（办理的签证对象）
 * @author Administrator
 *
 */
@Entity
@Table(name = "visa")
public class Visa extends DataEntityTTS {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 担保类型-担保
	 */
	public static final String GUARANTEE_NOMEL_NORMAL = "1";
	/**
	 * 担保类型-高担保
	 */
	public static final String GUARANTEE_NOMEL_HEIGHT = "2";
	/**
	 * 担保类型-交押金
	 */
	public static final String GUARANTEE_NOMEL_NONE = "3";
	
	private Long id;
	/** 关联游客ID */
	
	private Long travelerId;
	/** 
	 * 签证状态 
	 *  0未送签，1送签，2约签，3出签，4申请撤签，5撤签成功，6撤签失败，7拒签
	 * */
	private int visaStauts; 
	/** 预计出发时间 */
	private Date forecastStartOut;
	
	/** 预计回团时间 */
	private Date forecastBackDate;
	
	/** 预计签约时间 */
	private Date forecastContract;

	/** 实际出发时间 */
	private Date startOut;
	/** 实际签约时间 */
	private Date contract;
	/** 实际送签时间*/
	private Date deliveryTime;
	/** AA码 */
	private String AACode;
	/** 制表人 */
	private String makeTable;
	/** 护照首页ID */
	private Long passportPhotoId;
	private DocInfo passportPhoto;
	
	/** 身份证正面ID */
	private Long identityFrontPhotoId;
	private DocInfo identityFrontPhoto;
	
	/** 身份证背面ID */
	private Long identityBackPhotoId;
	private DocInfo identityBackPhoto;
	
	/** 报名表(申请表格)ID */
	private Long tablePhotoId;
	private DocInfo tablePhoto;
	
	/** 照片(电子照片)ID */
	private Long personPhotoId;
	private DocInfo personPhoto;
	
	/** 其他图片附件ID（多个附件ID，用&分离） */
	private Long otherPhotoId;
	private DocInfo otherPhoto;
	
	/** 户口本ID */
	private Long familyRegisterPhotoId;
	private DocInfo familyRegisterPhoto;
	
	/** 房产证ID */
	private Long houseEvidencePhotoId;
	private DocInfo houseEvidencePhoto;
	
	/** 备注 */
	private String remark;
	/** 游客类型 （在职，退休，学生） */
	private Integer travelerType;
	/** 结算价格 */
	private BigDecimal footUp; 
	//--------------------------
    
	/**
	 * 表示是否签收资料原件:0:未签收  1：签收
	 */
	private  String  signOriginalProjectType;
	
	/**
	 * 表示是否签收其他原件 :0:未签收  1：签收
	 */
	private  String  signOriginalProjectName;
	
	/**
	 * 表示是否签收资料复印件 :0:未签收  1：签收
	 */
	private  String signCopyProjectType;
	
	/**
	 * 表示是否签收其他资料复印件 :0:未签收  1：签收
	 */
	private  String  signCopyProjectName;
	
	/**
	 * 担保状态：1:担保；2:高担保；3:押金；
	 */
	private  Integer  guaranteeStatus;
	
	/**
	 * 应收押金 UUID
	 */
	private String  totalDeposit;
	
	/**
	 * 已收押金 UUID
	 */
	private String  payedDeposit;
	
	/**
	 * 达账押金 UUID
	 */
	private String  accountedDeposit;
	
	/**
	 * 财务已退押金 UUID
	 */
	private String  returnedDeposit;
	
	/**
	 * 押金是否达账：0:未达账；1:达账
	 */
	private  Integer  isAccounted;
	 
	/**
	 * 护照操作人姓名
	 */
	private String  passportOperator;
	
	/**
	 * 护照操作时间
	 */
	private Date  passportOperateTime;
	
	/**
	 * 护照操作类型 1:借出  2:还护照
	 */
	private Integer  passportOperateType;
	
	/**
	 * 操作时的备注
	 */
	private String passportOperateRemark;
	
	/**
	 * 应收押金（多币种）
	 */
	private String totalDepositMoney;
	
	/**
	 * 附件
	 */
	private List<DocInfo> docs;
	
	/**
	 * 附件IDs
	 */
	private String docIds;
	
	/*出纳确认状态*/
	private Integer returnedDepositStatus;  
	
	/**
	 * UID编号
	 */
	private String UIDCode;
	
	@Column(name="returned_deposit_status")
	public Integer getReturnedDepositStatus() {
		return returnedDepositStatus;
	}

	public void setReturnedDepositStatus(Integer returnedDepositStatus) {
		this.returnedDepositStatus = returnedDepositStatus;
	}

	public Visa() {
		super();
	}
	
	public Visa(Long id){
		this();
		this.id = id;
	}
	/** 主键ID */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="traveler_id")
	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}
	@Column(name="visa_stauts")
	public int getVisaStauts() {
		return visaStauts;
	}

	public void setVisaStauts(int visaStauts) {
		this.visaStauts = visaStauts;
	}

	@Column(name="forecast_start_out")
	public Date getForecastStartOut() {
		return forecastStartOut;
	}

	public void setForecastStartOut(Date forecastStartOut) {
		this.forecastStartOut = forecastStartOut;
	}
	
	@Column(name="forecast_back_date")
	public Date getForecastBackDate() {
		return forecastBackDate;
	}

	public void setForecastBackDate(Date forecastBackDate) {
		this.forecastBackDate = forecastBackDate;
	}

	@Column(name="forecast_contract")
	public Date getForecastContract() {
		return forecastContract;
	}

	public void setForecastContract(Date forecastContract) {
		this.forecastContract = forecastContract;
	}

	/*@Column(name="visa_deposit")
	public Integer getDepositValue() {
		return depositValue;
	}

	public void setDepositValue(Integer depositValue) {
		this.depositValue = depositValue;
	}

	@Column(name="visa_datum")
	public Integer getDatumValue() {
		return datumValue;
	}

	public void setDatumValue(Integer datumValue) {
		this.datumValue = datumValue;
	}*/

	@Column(name="start_out")
	public Date getStartOut() {
		return startOut;
	}

	public void setStartOut(Date startOut) {
		this.startOut = startOut;
	}

	@Column(name="contract")
	public Date getContract() {
		return contract;
	}

	public void setContract(Date contract) {
		this.contract = contract;
	}

	@Column(name="AA_Code")
	public String getAACode() {
		return AACode;
	}

	public void setAACode(String aACode) {
		AACode = aACode;
	}

	@Column(name="make_table")
	public String getMakeTable() {
		return makeTable;
	}

	public void setMakeTable(String makeTable) {
		this.makeTable = makeTable;
	}

	@Column(name="passport_photo_Id")
	public Long getPassportPhotoId() {
		return passportPhotoId;
	}

	public void setPassportPhotoId(Long passportPhotoId) {
		this.passportPhotoId = passportPhotoId;
	}

	@Column(name="identity_front_photo_id")
	public Long getIdentityFrontPhotoId() {
		return identityFrontPhotoId;
	}

	public void setIdentityFrontPhotoId(Long identityFrontPhotoId) {
		this.identityFrontPhotoId = identityFrontPhotoId;
	}

	@Column(name="identity_back_photo_id")
	public Long getIdentityBackPhotoId() {
		return identityBackPhotoId;
	}

	public void setIdentityBackPhotoId(Long identityBackPhotoId) {
		this.identityBackPhotoId = identityBackPhotoId;
	}

	@Column(name="table_photo_id")
	public Long getTablePhotoId() {
		return tablePhotoId;
	}

	public void setTablePhotoId(Long tablePhotoId) {
		this.tablePhotoId = tablePhotoId;
	}

	@Column(name="person_photo_id")
	public Long getPersonPhotoId() {
		return personPhotoId;
	}

	public void setPersonPhotoId(Long personPhotoId) {
		this.personPhotoId = personPhotoId;
	}

	@Column(name="other_photo_id")
	public Long getOtherPhotoId() {
		return otherPhotoId;
	}

	public void setOtherPhotoId(Long otherPhotoId) {
		this.otherPhotoId = otherPhotoId;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="traveler_type")
	public Integer getTravelerType() {
		return travelerType;
	}

	public void setTravelerType(Integer travelerType) {
		this.travelerType = travelerType;
	}

	@Column(name="foot_up")
	public BigDecimal getFootUp() {
		return footUp;
	}

	public void setFootUp(BigDecimal footUp) {
		this.footUp = footUp;
	}
	
	//-----------------------
	
	@Column(name="sign_original_project_type")
	public String getSignOriginalProjectType() {
		return signOriginalProjectType;
	}

	public void setSignOriginalProjectType(String signOriginalProjectType) {
		this.signOriginalProjectType = signOriginalProjectType;
	}

	@Column(name="sign_original_project_name")
	public String getSignOriginalProjectName() {
		return signOriginalProjectName;
	}

	public void setSignOriginalProjectName(String signOriginalProjectName) {
		this.signOriginalProjectName = signOriginalProjectName;
	}

	@Column(name="sign_copy_project_type")
	public String getSignCopyProjectType() {
		return signCopyProjectType;
	}

	public void setSignCopyProjectType(String signCopyProjectType) {
		this.signCopyProjectType = signCopyProjectType;
	}

	@Column(name="sign_copy_project_name")
	public String getSignCopyProjectName() {
		return signCopyProjectName;
	}

	public void setSignCopyProjectName(String signCopyProjectName) {
		this.signCopyProjectName = signCopyProjectName;
	}

	@Column(name="guarantee_status")
	public Integer getGuaranteeStatus() {
		return guaranteeStatus;
	}

	public void setGuaranteeStatus(Integer guaranteeStatus) {
		this.guaranteeStatus = guaranteeStatus;
	}

	@Column(name="total_deposit")
	public String getTotalDeposit() {
		return totalDeposit;
	}

	public void setTotalDeposit(String totalDeposit) {
		this.totalDeposit = totalDeposit;
	}

	@Column(name="payed_deposit")
	public String getPayedDeposit() {
		return payedDeposit;
	}

	public void setPayedDeposit(String payedDeposit) {
		this.payedDeposit = payedDeposit;
	}

	@Column(name="accounted_deposit")
	public String getAccountedDeposit() {
		return accountedDeposit;
	}

	public void setAccountedDeposit(String accountedDeposit) {
		this.accountedDeposit = accountedDeposit;
	}

	@Column(name="returned_deposit")
	public String getReturnedDeposit() {
		return returnedDeposit;
	}

	public void setReturnedDeposit(String returnedDeposit) {
		this.returnedDeposit = returnedDeposit;
	}

	@Column(name="is_accounted")
	public Integer getIsAccounted() {
		return isAccounted;
	}

	public void setIsAccounted(Integer isAccounted) {
		this.isAccounted = isAccounted;
	}

	@Column(name="passport_operator")
	public String getPassportOperator() {
		return passportOperator;
	}

	public void setPassportOperator(String passportOperator) {
		this.passportOperator = passportOperator;
	}

	@Column(name="passport_operate_time")
	public Date getPassportOperateTime() {
		return passportOperateTime;
	}

	public void setPassportOperateTime(Date passportOperateTime) {
		this.passportOperateTime = passportOperateTime;
	}

	@Column(name="passport_operate_type")
	public Integer getPassportOperateType() {
		return passportOperateType;
	}

	public void setPassportOperateType(Integer passportOperateType) {
		this.passportOperateType = passportOperateType;
	}

	@Column(name="passport_operate_remark")
	public String getPassportOperateRemark() {
		return passportOperateRemark;
	}

	public void setPassportOperateRemark(String passportOperateRemark) {
		this.passportOperateRemark = passportOperateRemark;
	}

	@Transient
	public String getTotalDepositMoney() {
		return totalDepositMoney;
	}

	public void setTotalDepositMoney(String totalDepositMoney) {
		this.totalDepositMoney = totalDepositMoney;
	}

	@Transient
	public List<DocInfo> getDocs() {
		return docs;
	}

	public void setDocs(List<DocInfo> docs) {
		this.docs = docs;
	}

	@Column(name="visa_doc_id")
	public String getDocIds() {
		return docIds;
	}

	public void setDocIds(String docIds) {
		this.docIds = docIds;
	}

	@Transient
	public DocInfo getPassportPhoto() {
		return passportPhoto;
	}

	public void setPassportPhoto(DocInfo passportPhoto) {
		this.passportPhoto = passportPhoto;
	}

	@Transient
	public DocInfo getIdentityFrontPhoto() {
		return identityFrontPhoto;
	}

	public void setIdentityFrontPhoto(DocInfo identityFrontPhoto) {
		this.identityFrontPhoto = identityFrontPhoto;
	}

	@Transient
	public DocInfo getIdentityBackPhoto() {
		return identityBackPhoto;
	}

	public void setIdentityBackPhoto(DocInfo identityBackPhoto) {
		this.identityBackPhoto = identityBackPhoto;
	}

	@Transient
	public DocInfo getTablePhoto() {
		return tablePhoto;
	}

	public void setTablePhoto(DocInfo tablePhoto) {
		this.tablePhoto = tablePhoto;
	}

	@Transient
	public DocInfo getPersonPhoto() {
		return personPhoto;
	}

	public void setPersonPhoto(DocInfo personPhoto) {
		this.personPhoto = personPhoto;
	}

	@Transient
	public DocInfo getOtherPhoto() {
		return otherPhoto;
	}

	public void setOtherPhoto(DocInfo otherPhoto) {
		this.otherPhoto = otherPhoto;
	}

	@Column(name="actual_delivery_time")
	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	@Column(name="family_register_photo_id")
	public Long getFamilyRegisterPhotoId() {
		return familyRegisterPhotoId;
	}

	public void setFamilyRegisterPhotoId(Long familyRegisterPhotoId) {
		this.familyRegisterPhotoId = familyRegisterPhotoId;
	}
	
	@Transient
	public DocInfo getFamilyRegisterPhoto() {
		return familyRegisterPhoto;
	}

	public void setFamilyRegisterPhoto(DocInfo familyRegisterPhoto) {
		this.familyRegisterPhoto = familyRegisterPhoto;
	}
	
	@Column(name="house_evidence_photo_id")
	public Long getHouseEvidencePhotoId() {
		return houseEvidencePhotoId;
	}

	public void setHouseEvidencePhotoId(Long houseEvidencePhotoId) {
		this.houseEvidencePhotoId = houseEvidencePhotoId;
	}
	
	@Transient
	public DocInfo getHouseEvidencePhoto() {
		return houseEvidencePhoto;
	}

	public void setHouseEvidencePhoto(DocInfo houseEvidencePhoto) {
		this.houseEvidencePhoto = houseEvidencePhoto;
	}
	
	public String getUIDCode() {
		return UIDCode;
	}

	public void setUIDCode(String uIDCode) {
		UIDCode = uIDCode;
	}
	
	
	
	
}
