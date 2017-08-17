package com.trekiz.admin.modules.activity.pojo;

import java.util.Date;
import java.util.List;

import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;

/**
 * @ClassName: GroupCodeLifeBycle
 * @Description: 团号生命周期类 ：记录创建和修改的相关信息
 * @author 
 * @date 2015-2015年11月30日 下午7:37:24
 *
 */
public class GroupCodeLifeBycle {

	private String oldGroupCode;
	private Date oldGroupCodeCreateDate;
	private String oldGroupCodeCreatebyName;
	private List<GroupcodeModifiedRecord> groupcodeModifiedRecords;
	public String getOldGroupCode() {
		return oldGroupCode;
	}
	public void setOldGroupCode(String oldGroupCode) {
		this.oldGroupCode = oldGroupCode;
	}
	public Date getOldGroupCodeCreateDate() {
		return oldGroupCodeCreateDate;
	}
	public void setOldGroupCodeCreateDate(Date oldGroupCodeCreateDate) {
		this.oldGroupCodeCreateDate = oldGroupCodeCreateDate;
	}
	public String getOldGroupCodeCreatebyName() {
		return oldGroupCodeCreatebyName;
	}
	public void setOldGroupCodeCreatebyName(String oldGroupCodeCreatebyName) {
		this.oldGroupCodeCreatebyName = oldGroupCodeCreatebyName;
	}
	public List<GroupcodeModifiedRecord> getGroupcodeModifiedRecords() {
		return groupcodeModifiedRecords;
	}
	public void setGroupcodeModifiedRecords(List<GroupcodeModifiedRecord> groupcodeModifiedRecords) {
		this.groupcodeModifiedRecords = groupcodeModifiedRecords;
	}
	
}
