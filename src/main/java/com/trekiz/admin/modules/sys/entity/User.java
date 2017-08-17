package com.trekiz.admin.modules.sys.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.excel.annotation.ExcelField;
import com.trekiz.admin.common.utils.excel.fieldtype.RoleListType;
import com.trekiz.admin.modules.mobile.entity.MobileUser;

/**
 * 用户Entity
 * @author zj
 * @version 2013-11-19
 */
@Entity
@Table(name = "sys_user")
@DynamicInsert @DynamicUpdate
public class User extends DataEntity {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Office company;	 // 归属供货商，渠道商的用户也会记录对应的归属供货商
	private Long agentId; // 渠道商信息，只有渠道商的用户才有此信息
	private String loginName;// 登录名
	private String password; // 密码
	private String two_psw;  // 密码
	private String no;		 // 工号
	private String name;	 // 姓名
	private String email;	 // 邮箱
	private String phone;	 // 电话
	private String mobile;	 // 手机
	private String userType; // 用户类型
	private String loginIp;	 // 最后登陆IP
	private Date loginDate;	 // 最后登陆日期
	private String remarks;  // 备注
	private Integer loginStatus;	// 登录状态 0：未登录，1：已登陆
	private String groupeSurname;
	private Long deptId;     //所属部门Id
	private String uuid;	//用户 uuid
	private String substituteOrder;  // 可代替自己下单的人
	private String isQuauqAgentLoginUser;  // 是否是quauq渠道的登陆账号  0 不是 1 是
	private String quauqBookOrderPermission;  // 是否可以用quauq渠道报名
	private int hasPricingStrategyPermission;  //设置定价策略权限   0：无权限   1：有权限

	private String cardId; // 名片id,对应docinfo表的主键
	private String photoId; // 个人照片id,对应docinfo表的主键
	private DocInfo cardDocInfo; // 名片的DocInfo对象
	private DocInfo photoDocInfo; // 个人照片的DocInfo对象
	private Integer differenceRights;//下单权限 1：有 0：没有
	/** 临时策略（待删除） */
	private Integer lingxianwangshuai;//是否要看领航李帅产品
	
	private String weixin;	//微信行号
	
	private List<Map<String,Object>> deptJobRelation;//部门-职务关系集合
	private MobileUser mobileUser;//关联微信用户
	private Integer accountFrom;	//渠道账号来源	0:内部  1:微信
	
	private String userName;	//仅针对530
	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupeSurname()
	{
		return groupeSurname;
	}

	public void setGroupeSurname(String groupeSurname)
	{
		this.groupeSurname = groupeSurname;
	}
	public Integer getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(Integer accountFrom) {
		this.accountFrom = accountFrom;
	}


	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	//private List<Department> userDeptList = Lists.newArrayList(); // 拥有部门列表

	public User() {
		super();
	}
	
	public User(Long id) {
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="companyId")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="供应商不能为空")
	@ExcelField(title="供应商", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	@Length(min=1, max=100)
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="weixin")
	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	@Length(max=100)
	public String getTwo_psw() {
		return two_psw;
	}
	
	public void setTwo_psw(String two_psw) {
		this.two_psw = two_psw;
	}

	@Length(min=1, max=100)
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	@Length(min=1, max=100)
	@ExcelField(title="工号", align=2, sort=45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email @Length(min=0, max=200)
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200)
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=1)
	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	

	public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	@Column(name = "login_status")
	public Integer getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "roleId") })
	@Where(clause="delFlag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	@JsonIgnore
	public List<Long> getRoleIdList() {
		List<Long> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}
	
	@Transient
	public void setRoleIdList(List<Long> roleIdList) {
		roleList = Lists.newArrayList();
		for (Long roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	@Transient
	public String getRoleIds() {
		return Collections3.extractToString(roleList, "id", ", ");
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}
	
	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	@Transient
	public static boolean isAdmin(Long id){
		return id != null && id.equals(1L);
	}
	
	/**
	 * 根据用户角色关联部门等级排序，如果等级相同则根据id排序
	 * @return
	 */
	@Transient
	@JsonIgnore
	public List<Role> getRoleListOrderByDept() {
		List<Role> roleList = Lists.newArrayList();
		roleList.addAll(this.roleList);
        Collections.sort(roleList, new Comparator<Role>() {  
            public int compare(Role o1, Role o2) {  
                int result = o1.getDepartment().getLevel() - o2.getDepartment().getLevel();
                if (result == 0) {  
                    result = o1.getDepartment().getId().compareTo(o2.getDepartment().getId());  
                }  
                return result;  
            }  
        });  
        return roleList;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "substitute_order")
	public String getSubstituteOrder() {
		return substituteOrder;
	}

	public void setSubstituteOrder(String substituteOrder) {
		this.substituteOrder = substituteOrder;
	}
	
	@Transient
	public List<Map<String,Object>> getDeptJobRelation() {
		return deptJobRelation;
	}
	
	public void setDeptJobRelation(List<Map<String,Object>> deptJobRelation) {
		this.deptJobRelation = deptJobRelation;
	}

	@Column(name = "is_quauq_agent_login_user")
	public String getIsQuauqAgentLoginUser() {
		return isQuauqAgentLoginUser;
	}

	public void setIsQuauqAgentLoginUser(String isQuauqAgentLoginUser) {
		this.isQuauqAgentLoginUser = isQuauqAgentLoginUser;
	}

	public String getQuauqBookOrderPermission() {
		return quauqBookOrderPermission;
	}

	public void setQuauqBookOrderPermission(String quauqBookOrderPermission) {
		this.quauqBookOrderPermission = quauqBookOrderPermission;
	}

	@Column(name = "hasPricingStrategyPermission")
	public int getHasPricingStrategyPermission() {
		return hasPricingStrategyPermission;
	}

	public void setHasPricingStrategyPermission(int hasPricingStrategyPermission) {
		this.hasPricingStrategyPermission = hasPricingStrategyPermission;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	@Transient
	@JsonIgnore
	public DocInfo getCardDocInfo() {
		return cardDocInfo;
	}

	public void setCardDocInfo(DocInfo cardDocInfo) {
		this.cardDocInfo = cardDocInfo;
	}

	@Transient
	@JsonIgnore
	public DocInfo getPhotoDocInfo() {
		return photoDocInfo;
	}

	public void setPhotoDocInfo(DocInfo photoDocInfo) {
		this.photoDocInfo = photoDocInfo;
	}
	
	@Column(name="differenceRights")
	public Integer getDifferenceRights() {
		return differenceRights;
	}

	public void setDifferenceRights(Integer differenceRights) {
		this.differenceRights = differenceRights;
	}

	/** 临时策略（待删除） */
	@Column(name="lingxianwangshuai")
	public Integer getLingxianwangshuai() {
		return lingxianwangshuai;
	}

	public void setLingxianwangshuai(Integer lingxianwangshuai) {
		this.lingxianwangshuai = lingxianwangshuai;
	}

	@ManyToOne
	@JoinColumn(name="mobileUserId")
	@NotFound(action = NotFoundAction.IGNORE)
	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}
	
	
}