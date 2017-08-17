package com.trekiz.admin.modules.eprice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Indexed;

/**
 * 询价相关文件
 *  @author lihua.xu
 */
@Entity
@Table(name = "estimate_price_file")
@DynamicInsert @DynamicUpdate
public class EstimatePriceFile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	
	//询价相关文件父级类型start
	
	/**
	 * 询价相关文件父级类型:接待询价行程文档  1
	 */
	public static final int PTYPE_ADMIT = 1;
	
	/**
	 * 询价相关文件父级类型:接待计调回复行程文档  2
	 */
	public static final int PTYPE_REPLY = 2;
	//询价相关文件父级类型end
	
	//询价相关文件数据状态start
	
	/**
	 * 询价相关文件数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 * 询价相关文件数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	
	/**
	 *数据状态:草稿  -1
	 */
	public static final int STATUS_DRAFT = -1;
	//询价相关文件数据状态end
	
	//询价相关type类型start
	
	/**
	 * 询价相关type类型:询价内容行程文档  1
	 */
	public static final int TYPE_TRIP = 1;
	
	/**
	 * 询价相关type类型:询价内容回复文档  2
	 */
	public static final int TYPE_REPLY = 2;
	
	
	//询价相关type类型end
	
	/**
	 * ID 主键ID
	 */
	private Long id;
	
	/**
	 * 父级——询价项目id
	 */
	private Long pid;
	
	/**
	 * 父级类型
	 * 1 接待询价行程文档 ；2  接待计调回复行程文档
	 * 父级id（pid）根据类型不同，关联的表不同，
	 * 当type为1时，pid代表接待询价内容id；
	 * 当type为2时，pid代表询价回复id
	 */
	private Integer ptype;
	
	/**
	 * 上传文件的用户id
	 */
	private Long userId;
	
	/**
	 * 上传文件的用户name
	 */
	private String userName;
	
	/**
	 * 类型：1 行程文档
	 */
	private Integer type;
	
	/**
	 * 文件名：含扩展名
	 */
	private String fileName;
	
	/**
	 * 文件扩展名
	 */
	private String ext;
	
	/**
	 * 状态：
	 * 1 正常，0 被删除
	 */
	private Integer status;
	
	/**
	 * 询价相关文件
	 */
	private String path;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date modifyTime;
	
	private String remark;
	
	private Long docInfoId;

	// Constructors

	/** default constructor */
	public EstimatePriceFile() {
	}

	/** full constructor */
	public EstimatePriceFile(Long pid, Integer ptype, Long userId,
			String userName, Integer type, String fileName,String ext, Integer status,
			String path, Date createTime, Date modifyTime,String remark) {
		this.pid = pid;
		this.ptype = ptype;
		this.userId = userId;
		this.userName = userName;
		this.type = type;
		this.fileName = fileName;
		this.ext = ext;
		this.status = status;
		this.path = path;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
	}

	// Property accessors
	//@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "pid")
	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Column(name = "ptype")
	public Integer getPtype() {
		return this.ptype;
	}

	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}

	@Column(name = "user_id")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "user_name")//, length = 64
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "file_name")//, length = 512
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "ext")//, length = 32
	public String getExt() {
		return this.ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "path")//, length = 1024
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")//, length = 10
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time")//, length = 10
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Column(name = "remark")//, length = 1024
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "doc_info_id")
	public Long getDocInfoId() {
		return docInfoId;
	}

	public void setDocInfoId(Long docInfoId) {
		this.docInfoId = docInfoId;
	}

}