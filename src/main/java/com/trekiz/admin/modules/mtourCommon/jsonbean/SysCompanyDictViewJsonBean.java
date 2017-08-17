package com.trekiz.admin.modules.mtourCommon.jsonbean;

public class SysCompanyDictViewJsonBean {
	private String id;//id
	private String code;//字典信息value
	private String name;//字典信息名称
	private String type;//字典类型
	private String uuid;//uuid

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUuid() {
		return this.uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
