package com.trekiz.admin.modules.traveler.entity;

public enum TravelerFileEnum {
	
	OTHER(0, "other", "护照"),
	PASSPORT(1, "passport", "护照"),
	E_PHOTO(2, "photo", "电子相片"),
	IDCARD_FRONT(3, "idcardfront", "身份证正面"),
	IDCARD_BACK(4, "idcardback", "身份证反面"),
	ENTRY_FORM(5, "entryform", "申请表格"),
	HOUSE(6, "house", "房产证"),
	RESIDENCE(7, "residence", "户口本"),
	VISA(8, "visaannex", "签证附件"),
	ZBQ_PASSPORT_HOMEPAGE(9, "zbqpassport", "自备签护照首页"),
	ZBQ_VISAPAGE(10, "zbqvisaannex", "自备签签证页"),
	ZBQ_OTHER(11, "zbqother", "自备签其他");

	/** 枚举索引 */
	private Integer index;
	/** 枚举内容字符串 */
	private String key;
	/** 枚举内容描述 */
	private String description;
	
	private TravelerFileEnum(Integer index, String key, String description) {
		this.index = index;
		this.key = key;
		this.description = description;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
}
