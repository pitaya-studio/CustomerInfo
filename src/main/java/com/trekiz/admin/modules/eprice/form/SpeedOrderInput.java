/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.eprice.form;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerPapersType;
import com.trekiz.admin.modules.hotel.entity.HotelTravelervisa;
import com.trekiz.admin.modules.order.entity.OrderContacts;

/**
 * @author quauq
 * @version 1.0
 * @since 1.0
 */
public class SpeedOrderInput extends BaseInput {
	private static final long serialVersionUID = -7141480892837137152L;

	// 自定义拆分规则定义
	private static Map<String, BaseInputBean> baseInputBeanMap = new HashMap<String, BaseInputBean>();

	// columns START
	// "主键"
	private java.lang.Integer id;
	// "订单uuid"
	private java.lang.String uuid;
	// "酒店产品uuid"
	private java.lang.String activityHotelUuid;
	// "酒店产品团期uuid"
	private java.lang.String activityHotelGroupUuid;
	// "预订单位-即渠道"
	private java.lang.Integer orderCompany;
	// "预订单位名称"
	private java.lang.String orderCompanyName;
	// "预定人数"
	private java.lang.Integer orderPersonNum;
	// "占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位"
	private Integer placeHolderType;
	// "是否是补单产品，0：否，1：是"
	private java.lang.Boolean isAfterSupplement;
	// "结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4"
	private java.lang.Integer paymentType;
	// "预报名间数"
	private java.lang.Integer forecaseReportNum;
	// 备注
	private java.lang.String remark;
	// columns END
	private HotelOrder dataObj;

	// 渠道联系人信息
	// 名称*/
	private String[] orderContacts_contactsName;
	// 电话*/
	private String[] orderContacts_contactsTel;
	// 固定电话*/
	private String[] orderContacts_contactsTixedTel;
	// 地址*/
	private String[] orderContacts_contactsAddress;
	// 传真*/
	private String[] orderContacts_contactsFax;
	// QQ*/
	private String[] orderContacts_contactsQQ;
	// 邮箱*/
	private String[] orderContacts_contactsEmail;
	// 邮编*/
	private String[] orderContacts_contactsZipCode;
	// 其他*/
	private String[] orderContacts_remark;

	// 游客类型费用、费用调整 等信息
	// "价格类型：1 团期价格类型（游客类型的价格存储类型）；2 返佣类型；3 优惠类型 ；4 其他类型； 5 退款"
	private String[] hotelOrderPrice_priceType;
	// "酒店产品团期价格表UUID"
	private String[] hotelOrderPrice_activityHotelGroupPriceUuid;
	//"游客类型"
  	private String[] hotelOrderPrice_travelerType;
	// "币种"
	private String[] hotelOrderPrice_currencyId;
	// "价格"
	private String[] hotelOrderPrice_price;
	// "个数"
	private String[] hotelOrderPrice_num; // 个数为空时不会进行数据的映射，费用调整的要保存数据请传“0”
	// "金额名称"
	private String[] hotelOrderPrice_priceName;
	// "备注"
	private String[] hotelOrderPrice_remark;
	// 游客类型
	private String[] activityHotelGroupPrice_type;

	/** 游客信息 主表信息 及 子表信息 Start */
	// "主键"
	// "游客姓名"
	private String[] hotelTraveler_name;
	// "游客名称拼音"
	private String[] hotelTraveler_nameSpell;
	// "人员类型（游客类型，数据字典）"
	private String[] hotelTraveler_personType;
	// "性别 1-男 2-女"
	private String[] hotelTraveler_sex;
	// "备注"
	private String[] hotelTraveler_remark;
	// "附件信息"
	private String[] hotelTraveler_files;// 拼接规则 docId#docName#docPath 如：
											// [111#222#333；444#555#666]
											// 如有空值用“_”占位
	// 签证信息
	private String[] hotelTraveler_visaInfo;// 拼接规则 country#visaTypeId 如：
											// [111#222；444#555] 如有空值用“_”占位
	// 游客的金额信息
	private String[] hotelTraveler_amount;// 拼接规则 currencyId#amount#exchangerate
											// 如： [111#222#333；444#555#666]
											// 如有空值用“_”占位
	// 游客的证件信息
	private String[] hotelTraveler_papersType;// 拼接规则
												// papersType#idCard#validityDate
												// 如： [111#222#333；444#555#666]
												// 如有空值用“_”占位
	/** 游客信息 主表信息 及 子表信息 end */

	// 金额信息
	// 订单的金额信息，订单总额、结算总额、应收、已收、未收
	private String[] hotelMoneyAmount_currencyId;
	private String[] hotelMoneyAmount_amount;
	private String[] hotelMoneyAmount_moneyType;

	// "团期编号"
	private java.lang.String groupCode;
	// "产品编号,如SG0001"
	private java.lang.String activitySerNum;
	// "酒店产品的名称"
	private java.lang.String activityName;
	// "国家"
	private java.lang.String country;
	// "出团日期"
	private java.util.Date groupOpenDate;
	private String groupOpenDateString;
	// "海岛"
	private java.lang.String islandUuid;
	// "酒店"
	private java.lang.String hotelUuid;
	// "海岛"
	private java.lang.String island;
	// "酒店"
	private java.lang.String hotel;
	// "上岛方式（字典，多个用“；”分隔）"
	private java.lang.String islandWay;
	// "余位数"
	private java.lang.Integer remNumber;
	// 酒店产品团期房型明细
	private List<ActivityHotelGroupRoom> activityHotelGroupRoomList;
	// 酒店产品团期基础餐型明细
	private List<ActivityHotelGroupMeal> activityHotelGroupMealList;
	// 酒店产品团期价格明细
	private List<ActivityHotelGroupPrice> activityHotelGroupPriceList;
	// 酒店产品团期起价明细
	private List<ActivityHotelGroupLowprice> activityHotelGroupLowprices;
	// 酒店产品团期控房明细表
	private List<ActivityHotelGroupControlDetail> activityHotelGroupControlDetail;
	// 酒店产品团期升餐明细
	private List<ActivityHotelGroupMealRise> activityHotelGroupMealRiseList;
	// 酒店产品团期预报名人数
	private Integer orderNum;
	//酒店集团
	private String hotelGroup;
	private String selNo;
	private String singlePrice;
	private String frontMoney;
	
	
	private String[] travelerType;
	
	private ActivityHotel activityHotel;
	private ActivityHotelGroup activityHotelGroup;
	
	private String[] roomtype;
	private String[] night;
	private String[] foodtype;
	
	private String singleCurrency;
	private String frontCurrency;
	
	private Integer orderMan;
	

	public Integer getOrderMan() {
		return orderMan;
	}

	public void setOrderMan(Integer orderMan) {
		this.orderMan = orderMan;
	}

	public String getSingleCurrency() {
		return singleCurrency;
	}

	public void setSingleCurrency(String singleCurrency) {
		this.singleCurrency = singleCurrency;
	}

	public String getFrontCurrency() {
		return frontCurrency;
	}

	public void setFrontCurrency(String frontCurrency) {
		this.frontCurrency = frontCurrency;
	}

	public SpeedOrderInput() {
	}

	

	// 表单提交的bean转换成数据库映射的bean
	public HotelOrder getHotelOrder() throws Exception {
		dataObj = new HotelOrder();
		BeanUtil.copySimpleProperties(dataObj, this, true);

		// 联系人信息
		dataObj.setOrderContactsList(super.transfer2Object(OrderContacts.class,this));
		// 游客类型费用、费用调整 等信息 集合 ----- 个数为空时不会进行数据的映射，费用调整的要保存数据请传“0”
		dataObj.setHotelOrderPriceList(super.transfer2Object(HotelOrderPrice.class, this,new String[] { "hotelOrderPrice_num" }));
		// 游客信息 集合
		dataObj.setHotelTravelerList(super.transfer2Object(HotelTraveler.class,this));
		// 订单的金额信息，订单总额、结算总额、应收、已收
		dataObj.setHotelMoneyAmountList(super.transfer2Object(HotelMoneyAmount.class, this));

		return dataObj;
	}

	static {
		// 初始化 input 表单的 拆分规则
		if (!baseInputBeanMap.containsKey("hotelTraveler_files")) {
			BaseInputBean bean = new BaseInputBean();
			bean.setClassz(HotelAnnex.class);
			bean.setPropertyName(new String[] { "docId", "docName", "docPath" });
			bean.setChildListName("hotelTravelerFilesList");
			baseInputBeanMap.put("hotelTraveler_files", bean);

			bean = new BaseInputBean();
			bean.setClassz(HotelTravelervisa.class);
			bean.setPropertyName(new String[] { "country", "visaTypeId" });
			bean.setChildListName("hotelTravelervisaList");
			baseInputBeanMap.put("hotelTraveler_visaInfo", bean);

			bean = new BaseInputBean();
			bean.setClassz(HotelMoneyAmount.class);
			bean.setPropertyName(new String[] { "currencyId", "amount" });
			bean.setChildListName("hotelMoneyAmountList");
			baseInputBeanMap.put("hotelTraveler_amount", bean);

			bean = new BaseInputBean();
			bean.setClassz(HotelTravelerPapersType.class);
			bean.setPropertyName(new String[] { "papersType", "idCard",
					"validityDate" });
			bean.setChildListName("hotelTravelerPapersTypeList");
			baseInputBeanMap.put("hotelTraveler_papersType", bean);
		}
	}

	public void setId(java.lang.Integer value) {
		this.id = value;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}

	public java.lang.String getUuid() {
		return this.uuid;
	}

	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}

	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
	}

	public void setActivityHotelGroupUuid(java.lang.String value) {
		this.activityHotelGroupUuid = value;
	}

	public java.lang.String getActivityHotelGroupUuid() {
		return this.activityHotelGroupUuid;
	}

	public void setOrderCompany(java.lang.Integer value) {
		this.orderCompany = value;
	}

	public java.lang.Integer getOrderCompany() {
		return this.orderCompany;
	}

	public void setOrderCompanyName(java.lang.String value) {
		this.orderCompanyName = value;
	}

	public java.lang.String getOrderCompanyName() {
		return this.orderCompanyName;
	}

	public void setOrderPersonNum(java.lang.Integer value) {
		this.orderPersonNum = value;
	}

	public java.lang.Integer getOrderPersonNum() {
		return this.orderPersonNum;
	}

	public void setPlaceHolderType(Integer value) {
		this.placeHolderType = value;
	}

	public Integer getPlaceHolderType() {
		return this.placeHolderType;
	}

	public void setIsAfterSupplement(java.lang.Boolean value) {
		this.isAfterSupplement = value;
	}

	public java.lang.Boolean getIsAfterSupplement() {
		return this.isAfterSupplement;
	}

	public void setPaymentType(java.lang.Integer value) {
		this.paymentType = value;
	}

	public java.lang.Integer getPaymentType() {
		return this.paymentType;
	}

	public void setForecaseReportNum(java.lang.Integer value) {
		this.forecaseReportNum = value;
	}

	public java.lang.Integer getForecaseReportNum() {
		return this.forecaseReportNum;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	public HotelOrder getDataObj() {
		return dataObj;
	}

	public void setDataObj(HotelOrder dataObj) {
		this.dataObj = dataObj;
	}

	public String[] getOrderContacts_contactsName() {
		return orderContacts_contactsName;
	}

	public void setOrderContacts_contactsName(
			String[] orderContacts_contactsName) {
		this.orderContacts_contactsName = orderContacts_contactsName;
	}

	public String[] getOrderContacts_contactsTel() {
		return orderContacts_contactsTel;
	}

	public void setOrderContacts_contactsTel(String[] orderContacts_contactsTel) {
		this.orderContacts_contactsTel = orderContacts_contactsTel;
	}

	public String[] getOrderContacts_contactsTixedTel() {
		return orderContacts_contactsTixedTel;
	}

	public void setOrderContacts_contactsTixedTel(
			String[] orderContacts_contactsTixedTel) {
		this.orderContacts_contactsTixedTel = orderContacts_contactsTixedTel;
	}

	public String[] getOrderContacts_contactsAddress() {
		return orderContacts_contactsAddress;
	}

	public void setOrderContacts_contactsAddress(
			String[] orderContacts_contactsAddress) {
		this.orderContacts_contactsAddress = orderContacts_contactsAddress;
	}

	public String[] getOrderContacts_contactsFax() {
		return orderContacts_contactsFax;
	}

	public void setOrderContacts_contactsFax(String[] orderContacts_contactsFax) {
		this.orderContacts_contactsFax = orderContacts_contactsFax;
	}

	public String[] getOrderContacts_contactsQQ() {
		return orderContacts_contactsQQ;
	}

	public void setOrderContacts_contactsQQ(String[] orderContacts_contactsQQ) {
		this.orderContacts_contactsQQ = orderContacts_contactsQQ;
	}

	public String[] getOrderContacts_contactsEmail() {
		return orderContacts_contactsEmail;
	}

	public void setOrderContacts_contactsEmail(
			String[] orderContacts_contactsEmail) {
		this.orderContacts_contactsEmail = orderContacts_contactsEmail;
	}

	public String[] getOrderContacts_contactsZipCode() {
		return orderContacts_contactsZipCode;
	}

	public void setOrderContacts_contactsZipCode(
			String[] orderContacts_contactsZipCode) {
		this.orderContacts_contactsZipCode = orderContacts_contactsZipCode;
	}

	public String[] getOrderContacts_remark() {
		return orderContacts_remark;
	}

	public void setOrderContacts_remark(String[] orderContacts_remark) {
		this.orderContacts_remark = orderContacts_remark;
	}

	public String[] getHotelOrderPrice_priceType() {
		return hotelOrderPrice_priceType;
	}

	public void setHotelOrderPrice_priceType(String[] hotelOrderPrice_priceType) {
		this.hotelOrderPrice_priceType = hotelOrderPrice_priceType;
	}

	public String[] getHotelOrderPrice_activityHotelGroupPriceUuid() {
		return hotelOrderPrice_activityHotelGroupPriceUuid;
	}

	public void setHotelOrderPrice_activityHotelGroupPriceUuid(
			String[] hotelOrderPrice_activityHotelGroupPriceUuid) {
		this.hotelOrderPrice_activityHotelGroupPriceUuid = hotelOrderPrice_activityHotelGroupPriceUuid;
	}

	public String[] getHotelOrderPrice_currencyId() {
		return hotelOrderPrice_currencyId;
	}

	public void setHotelOrderPrice_currencyId(
			String[] hotelOrderPrice_currencyId) {
		this.hotelOrderPrice_currencyId = hotelOrderPrice_currencyId;
	}

	public String[] getHotelOrderPrice_price() {
		return hotelOrderPrice_price;
	}

	public void setHotelOrderPrice_price(String[] hotelOrderPrice_price) {
		this.hotelOrderPrice_price = hotelOrderPrice_price;
	}

	public String[] getHotelOrderPrice_num() {
		return hotelOrderPrice_num;
	}

	public void setHotelOrderPrice_num(String[] hotelOrderPrice_num) {
		this.hotelOrderPrice_num = hotelOrderPrice_num;
	}

	public String[] getHotelOrderPrice_priceName() {
		return hotelOrderPrice_priceName;
	}

	public void setHotelOrderPrice_priceName(String[] hotelOrderPrice_priceName) {
		this.hotelOrderPrice_priceName = hotelOrderPrice_priceName;
	}

	public String[] getHotelOrderPrice_remark() {
		return hotelOrderPrice_remark;
	}

	public void setHotelOrderPrice_remark(String[] hotelOrderPrice_remark) {
		this.hotelOrderPrice_remark = hotelOrderPrice_remark;
	}

	public String[] getHotelTraveler_name() {
		return hotelTraveler_name;
	}

	public void setHotelTraveler_name(String[] hotelTraveler_name) {
		this.hotelTraveler_name = hotelTraveler_name;
	}

	public String[] getHotelTraveler_nameSpell() {
		return hotelTraveler_nameSpell;
	}

	public void setHotelTraveler_nameSpell(String[] hotelTraveler_nameSpell) {
		this.hotelTraveler_nameSpell = hotelTraveler_nameSpell;
	}

	public String[] getHotelTraveler_personType() {
		return hotelTraveler_personType;
	}

	public void setHotelTraveler_personType(String[] hotelTraveler_personType) {
		this.hotelTraveler_personType = hotelTraveler_personType;
	}

	public String[] getHotelTraveler_sex() {
		return hotelTraveler_sex;
	}

	public void setHotelTraveler_sex(String[] hotelTraveler_sex) {
		this.hotelTraveler_sex = hotelTraveler_sex;
	}

	public String[] getHotelTraveler_remark() {
		return hotelTraveler_remark;
	}

	public void setHotelTraveler_remark(String[] hotelTraveler_remark) {
		this.hotelTraveler_remark = hotelTraveler_remark;
	}

	public String[] getHotelTraveler_files() {
		return hotelTraveler_files;
	}

	public void setHotelTraveler_files(String[] hotelTraveler_files) {
		this.hotelTraveler_files = hotelTraveler_files;
	}

	public String[] getHotelTraveler_visaInfo() {
		return hotelTraveler_visaInfo;
	}

	public void setHotelTraveler_visaInfo(String[] hotelTraveler_visaInfo) {
		this.hotelTraveler_visaInfo = hotelTraveler_visaInfo;
	}

	public String[] getHotelTraveler_amount() {
		return hotelTraveler_amount;
	}

	public void setHotelTraveler_amount(String[] hotelTraveler_amount) {
		this.hotelTraveler_amount = hotelTraveler_amount;
	}

	public String[] getHotelTraveler_papersType() {
		return hotelTraveler_papersType;
	}

	public void setHotelTraveler_papersType(String[] hotelTraveler_papersType) {
		this.hotelTraveler_papersType = hotelTraveler_papersType;
	}

	public String[] getHotelMoneyAmount_currencyId() {
		return hotelMoneyAmount_currencyId;
	}

	public void setHotelMoneyAmount_currencyId(
			String[] hotelMoneyAmount_currencyId) {
		this.hotelMoneyAmount_currencyId = hotelMoneyAmount_currencyId;
	}

	public String[] getHotelMoneyAmount_amount() {
		return hotelMoneyAmount_amount;
	}

	public void setHotelMoneyAmount_amount(String[] hotelMoneyAmount_amount) {
		this.hotelMoneyAmount_amount = hotelMoneyAmount_amount;
	}

	public String[] getHotelMoneyAmount_moneyType() {
		return hotelMoneyAmount_moneyType;
	}

	public void setHotelMoneyAmount_moneyType(
			String[] hotelMoneyAmount_moneyType) {
		this.hotelMoneyAmount_moneyType = hotelMoneyAmount_moneyType;
	}

	public java.lang.String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(java.lang.String groupCode) {
		this.groupCode = groupCode;
	}

	public java.lang.String getActivitySerNum() {
		return activitySerNum;
	}

	public void setActivitySerNum(java.lang.String activitySerNum) {
		this.activitySerNum = activitySerNum;
	}

	public java.lang.String getActivityName() {
		return activityName;
	}

	public void setActivityName(java.lang.String activityName) {
		this.activityName = activityName;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.util.Date getGroupOpenDate() {
		return groupOpenDate;
	}

	public void setGroupOpenDate(java.util.Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}

	public java.lang.String getIslandUuid() {
		return islandUuid;
	}

	public void setIslandUuid(java.lang.String islandUuid) {
		this.islandUuid = islandUuid;
	}

	public java.lang.String getHotelUuid() {
		return hotelUuid;
	}

	public void setHotelUuid(java.lang.String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}

	public java.lang.String getIslandWay() {
		return islandWay;
	}

	public void setIslandWay(java.lang.String islandWay) {
		this.islandWay = islandWay;
	}

	public java.lang.Integer getRemNumber() {
		return remNumber;
	}

	public void setRemNumber(java.lang.Integer remNumber) {
		this.remNumber = remNumber;
	}

	public List<ActivityHotelGroupRoom> getActivityHotelGroupRoomList() {
		return activityHotelGroupRoomList;
	}

	public void setActivityHotelGroupRoomList(
			List<ActivityHotelGroupRoom> activityHotelGroupRoomList) {
		this.activityHotelGroupRoomList = activityHotelGroupRoomList;
	}

	public List<ActivityHotelGroupMeal> getActivityHotelGroupMealList() {
		return activityHotelGroupMealList;
	}

	public void setActivityHotelGroupMealList(
			List<ActivityHotelGroupMeal> activityHotelGroupMealList) {
		this.activityHotelGroupMealList = activityHotelGroupMealList;
	}

	public List<ActivityHotelGroupPrice> getActivityHotelGroupPriceList() {
		return activityHotelGroupPriceList;
	}

	public void setActivityHotelGroupPriceList(
			List<ActivityHotelGroupPrice> activityHotelGroupPriceList) {
		this.activityHotelGroupPriceList = activityHotelGroupPriceList;
	}

	public List<ActivityHotelGroupLowprice> getActivityHotelGroupLowprices() {
		return activityHotelGroupLowprices;
	}

	public void setActivityHotelGroupLowprices(
			List<ActivityHotelGroupLowprice> activityHotelGroupLowprices) {
		this.activityHotelGroupLowprices = activityHotelGroupLowprices;
	}

	public List<ActivityHotelGroupControlDetail> getActivityHotelGroupControlDetail() {
		return activityHotelGroupControlDetail;
	}

	public void setActivityHotelGroupControlDetail(
			List<ActivityHotelGroupControlDetail> activityHotelGroupControlDetail) {
		this.activityHotelGroupControlDetail = activityHotelGroupControlDetail;
	}

	public List<ActivityHotelGroupMealRise> getActivityHotelGroupMealRiseList() {
		return activityHotelGroupMealRiseList;
	}

	public void setActivityHotelGroupMealRiseList(
			List<ActivityHotelGroupMealRise> activityHotelGroupMealRiseList) {
		this.activityHotelGroupMealRiseList = activityHotelGroupMealRiseList;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public ActivityHotel getActivityHotel() {
		if(activityHotel==null){
			return new ActivityHotel();
		}
		return activityHotel;
	}

	public void setActivityHotel(ActivityHotel activityHotel) {
		this.activityHotel = activityHotel;
	}

	public ActivityHotelGroup getActivityHotelGroup() {
		if(activityHotelGroup==null){
			return new ActivityHotelGroup();
		}
		return activityHotelGroup;
	}

	public void setActivityHotelGroup(ActivityHotelGroup activityHotelGroup) {
		this.activityHotelGroup = activityHotelGroup;
	}

	public java.lang.String getIsland() {
		return island;
	}

	public void setIsland(java.lang.String island) {
		this.island = island;
	}

	public java.lang.String getHotel() {
		return hotel;
	}

	public void setHotel(java.lang.String hotel) {
		this.hotel = hotel;
	}

	public String getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public String getSelNo() {
		return selNo;
	}

	public void setSelNo(String selNo) {
		this.selNo = selNo;
	}

	public String getGroupOpenDateString() {
		return groupOpenDateString;
	}

	public void setGroupOpenDateString(String groupOpenDateString) {
		this.groupOpenDateString = groupOpenDateString;
	}

	public String getSinglePrice() {
		return singlePrice;
	}

	public void setSinglePrice(String singlePrice) {
		this.singlePrice = singlePrice;
	}

	public String getFrontMoney() {
		return frontMoney;
	}

	public void setFrontMoney(String frontMoney) {
		this.frontMoney = frontMoney;
	}

	public String[] getTravelerType() {
		return travelerType;
	}

	public void setTravelerType(String[] travelerType) {
		this.travelerType = travelerType;
	}

	public String[] getActivityHotelGroupPrice_type() {
		return activityHotelGroupPrice_type;
	}

	public void setActivityHotelGroupPrice_type(
			String[] activityHotelGroupPrice_type) {
		this.activityHotelGroupPrice_type = activityHotelGroupPrice_type;
	}

	public String[] getHotelOrderPrice_travelerType() {
		return hotelOrderPrice_travelerType;
	}

	public void setHotelOrderPrice_travelerType(
			String[] hotelOrderPrice_travelerType) {
		this.hotelOrderPrice_travelerType = hotelOrderPrice_travelerType;
	}

	public String[] getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String[] roomtype) {
		this.roomtype = roomtype;
	}

	public String[] getNight() {
		return night;
	}

	public void setNight(String[] night) {
		this.night = night;
	}

	public String[] getFoodtype() {
		return foodtype;
	}

	public void setFoodtype(String[] foodtype) {
		this.foodtype = foodtype;
	}
}