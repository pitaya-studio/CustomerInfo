package com.trekiz.admin.modules.mobile.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.trekiz.admin.modules.sys.entity.User;

/**
 * 手机用户数据模型类
 * @author 李鑫
 *
 */
@Entity
@Table(name = "mobile_user")
@DynamicInsert @DynamicUpdate
public class MobileUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;//用户编号
	private String accessToken;
	private String refreshToken;
	private String openId;
	private String nickName;//昵称
	private String sex;//性别
	private String headImageUrl;//微信头像地址
	private String telePhone;//手机号
	private String areaCode;//电话区号
	private String phone;//座机
	private String wechatCode;//微信账号
	private String agentName;//旅行社名称
	private String name;//用户名称
	private String isRegist;//用户是否注册 0未注册1已注册
	private String unbundleReason;//账号解绑原因
	protected Date createDate;// 创建日期
	protected Date updateDate;// 更新日期
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	
	private String isMatch;//该用户是否匹配T1账号
	private User t1User;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getHeadImageUrl() {
		return headImageUrl;
	}
	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWechatCode() {
		return wechatCode;
	}
	public void setWechatCode(String wechatCode) {
		this.wechatCode = wechatCode;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsRegist() {
		return isRegist;
	}
	public void setIsRegist(String isRegist) {
		this.isRegist = isRegist;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	@PrePersist
	public void prePersist() {
		if (createDate == null) {
			this.createDate = new Date();
		}
		this.updateDate = this.createDate;
	}

	@PreUpdate
	public void preUpdate(){
		this.updateDate = new Date();
	}
	@Transient
	public String getIsMatch() {
		return isMatch;
	}
	public void setIsMatch(String isMatch) {
		this.isMatch = isMatch;
	}
	public String getUnbundleReason() {
		return unbundleReason;
	}
	public void setUnbundleReason(String unbundleReason) {
		this.unbundleReason = unbundleReason;
	}
	
	@ManyToOne
	@JoinColumn(name="t1UserId")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getT1User() {
		return t1User;
	}
	public void setT1User(User t1User) {
		this.t1User = t1User;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}
