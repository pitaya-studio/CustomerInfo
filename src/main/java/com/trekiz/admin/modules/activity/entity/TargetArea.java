/**
 *
 */
package com.trekiz.admin.modules.activity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



  /**
 *  文件名: TargetArea.java 
 *  @author Wang Duo
 *  @DateTime 2014-12-18
  视图定义如下：
  CREATE VIEW activitytargetarea_view AS 
  SELECT t1.id,t1.srcActivityId,t1.targetAreaId,s1.name
  from activitytargetarea t1
    left join sys_area s1 on t1.targetAreaId=s1.id 
 */
@Entity
@Table(name = "activitytargetarea_view")
public class TargetArea {
	
 private Long id; // 编号
 private long srcActivityId;
 private long targetAreaId;
 private String name;
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "srcActivityId", unique = true, nullable =false, insertable = false, updatable = false)
	public long getSrcActivityId() {
		return srcActivityId;
	}
	public void setSrcActivityId(long srcActivityId) {
		this.srcActivityId = srcActivityId;
	}
	public long getTargetAreaId() {
		return targetAreaId;
	}
	public void setTargetAreaId(long targetAreaId) {
		this.targetAreaId = targetAreaId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
 

 
}
