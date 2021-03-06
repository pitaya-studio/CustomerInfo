/**
 *
 */
package com.trekiz.admin.modules.sys.entity;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.DataEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 区域Entity
 * @author zj
 * @version 2013-11-19
 */
@Entity
@Table(name = "sys_area")
@DynamicInsert @DynamicUpdate
public class Area extends DataEntity {

	private static final long serialVersionUID = 1L;
	private Long id;		// 编号
	private Area parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
//	private Long districtId;// 旅游区域id
	private String name; 	// 区域名称
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	private String selected; //是否为渠道选中项(0或者null：未选中、默认；1：选中项)
	private List<Area> childList = Lists.newArrayList();	// 拥有子区域列表
	private Long sysDistrictId;

	public Area(){
		super();
	}
	
	public Area(Long id){
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentId")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="parent")
	@Where(clause="delFlag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code")
	@NotFound(action = NotFoundAction.IGNORE)
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Area> getChildList() {
		return childList;
	}

	public void setChildList(List<Area> childList) {
		this.childList = childList;
	}

    @Column(name = "sys_district_id")
	public Long getSysDistrictId() {
		return sysDistrictId;
	}

	public void setSysDistrictId(Long sysDistrictId) {
		this.sysDistrictId = sysDistrictId;
	}

	@Transient
	public static void sortList(List<Area> list, List<Area> sourcelist, Long parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Area e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Area childe = sourcelist.get(j);
					if (childe.getParent()!=null && childe.getParent().getId()!=null
							&& childe.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	@Transient
	public static boolean isAdmin(Long id){
		return id != null && id.equals(1L);
	}
	
	public void setSelected(String selected) {
		this.selected = selected;
	}
	@Transient
	public String getSelected() {
		return selected;
	}

//	@Column(name = "sys_district_id")
//	public Long getDistrictId() {
//		return districtId;
//	}
//
//	public void setDistrictId(Long districtId) {
//		this.districtId = districtId;
//	}
}