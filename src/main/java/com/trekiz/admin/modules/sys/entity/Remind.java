package com.trekiz.admin.modules.sys.entity;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.Collections3;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * 系统提醒
 */
@Entity
@Table(name = "sys_remind")
@DynamicInsert
@DynamicUpdate
public class Remind extends DataEntity {
    private Integer id;                         // 编号
    private String remindName;                  // 提醒名称
    private Integer remindType;                     // 提醒类型, 1 还款提醒 2 收款提醒
    private Integer selectedRemindType;             // 选择类型， 1 全部团期 2 指定团期
    private String selectedRemindOrderType;     // 选择提醒团期类性

    private Integer startRemindStatus;              // 提醒起始时间状态 -1 前 0 等于 1 后
    private Integer startRemindDays;                // 提醒起始天数
    
    private Integer endRemindStatus;                // 提醒过期时间状态 -1 前 0 等于 1 后
    private Integer endRemindDays;                  // 提醒过期天数

    private String activityGroupIds;            // 团期ids,用','号分割
    private String productIds;            // 产品ids,用','号分割

    private String companyUuid;                   // 所属批发商id
    private String userStr;                // 可见用户Id Str
    private List<User> userList;                // 可见用户
    
    /**
     * 是否对审批人可见
     */
    private Integer isVisible4Reviewer = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "remind_name")
    public String getRemindName() {
        return remindName;
    }

    public void setRemindName(String remindName) {
        this.remindName = remindName;
    }

    @Column(name = "remind_type")
    public Integer getRemindType() {
        return remindType;
    }

    public void setRemindType(Integer remindType) {
        this.remindType = remindType;
    }

    @Column(name = "selected_remind_type")
    public Integer getSelectedRemindType() {
        return selectedRemindType;
    }

    public void setSelectedRemindType(Integer selectedRemindType) {
        this.selectedRemindType = selectedRemindType;
    }

    @Column(name = "selected_remind_order_type")
    public String getSelectedRemindOrderType() {
        return selectedRemindOrderType;
    }

    public void setSelectedRemindOrderType(String selectedRemindOrderType) {
        this.selectedRemindOrderType = selectedRemindOrderType;
    }

    @Column(name = "start_remind_status")
    public Integer getStartRemindStatus() {
        return startRemindStatus;
    }

    public void setStartRemindStatus(Integer startRemindStatus) {
        this.startRemindStatus = startRemindStatus;
    }

    @Column(name = "start_remind_days")
    public Integer getStartRemindDays() {
        return startRemindDays;
    }

    public void setStartRemindDays(Integer startRemindDays) {
        this.startRemindDays = startRemindDays;
    }

    @Column(name = "end_remind_status")
    public Integer getEndRemindStatus() {
        return endRemindStatus;
    }

    public void setEndRemindStatus(Integer endRemindStatus) {
        this.endRemindStatus = endRemindStatus;
    }

    @Column(name = "end_remind_days")
    public Integer getEndRemindDays() {
        return endRemindDays;
    }

    public void setEndRemindDays(Integer endRemindDays) {
        this.endRemindDays = endRemindDays;
    }

    @Column(name = "company_uuid")
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    @Column(name = "activityGroupIds")
    public String getActivityGroupIds() {
        return activityGroupIds;
    }

    public void setActivityGroupIds(String activityGroupIds) {
        this.activityGroupIds = activityGroupIds;
    }
    
    @Column(name = "productIds")
    public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}
	
	@Transient
	public String getUserStr() {
		return userStr;
	}

	public void setUserStr(String userStr) {
		this.userStr = userStr;
	}

	@Transient
    public String getUserNames() {
        if (getUserList() != null) {
            return Collections3.extractToString(getUserList(), "name", ",");
        } else {
            return "";
        }
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_remind_user", joinColumns = { @JoinColumn(name = "remind_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
    @Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Transient
    public String getUserIds() {
        return Collections3.convertToString(getUserIdList(), ",");
    }

    @Transient
    public List<Long> getUserIdList() {
        List<Long> userIdList = Lists.newArrayList();
        if (userList != null) {
            for (User user : userList) {
                userIdList.add(user.getId());
            }
        }
        return userIdList;
    }

    @Transient
    public void setUserIdList(List<Long> userIdList) {
        userList = Lists.newArrayList();
        if (userIdList != null) {
            for (Long userId : userIdList) {
                User user = new User();
                boolean flag = true;
                user.setId(userId);
                for(int i=0;i<userList.size();i++)
                {
                    if(userList.get(i).getId().toString().equals(userId.toString())  )
                    {
                        flag =false;
                        continue;
                    }
                }
                if(flag == true)
                    userList.add(user);
            }

        }
    }

    @Column(name = "is_visible_4_reviewer")
	public Integer getIsVisible4Reviewer() {
		return isVisible4Reviewer;
	}

	public void setIsVisible4Reviewer(Integer isVisible4Reviewer) {
		this.isVisible4Reviewer = isVisible4Reviewer;
	}
    
    

}
