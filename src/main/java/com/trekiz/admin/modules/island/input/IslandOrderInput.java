/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.entity.IslandTravelerPapersType;
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;
import com.trekiz.admin.modules.order.entity.OrderContacts;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class IslandOrderInput  extends BaseInput {
	private static final long serialVersionUID = 1L;
	
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"订单uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityIslandUuid;
	//"海岛游产品团期uuid"
	private java.lang.String activityIslandGroupUuid;
	//"预订单位-即渠道"
	private java.lang.Integer orderCompany;
	//"预订单位名称"
	private java.lang.String orderCompanyName;
	//"预定人数"
	private java.lang.Integer orderPersonNum;
	//"占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位"
	private Integer placeHolderType;
	//"是否是补单产品，0：否，1：是"
	private java.lang.Boolean isAfterSupplement;
	//"结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4"
	private java.lang.Integer paymentType;
	//"预报名间数"
	private java.lang.Integer forecaseReportRoomNum;
	//"预报名票数"
	private java.lang.Integer forecaseReportTicketNum;
	//备注
	private java.lang.String remark;
	//columns END
	private IslandOrder dataObj ;
	
	
	//渠道联系人信息
	//  名称*/
    private String[] orderContacts_contactsName;
    //  电话*/
    private String[] orderContacts_contactsTel;
    //  固定电话*/
    private String[] orderContacts_contactsTixedTel ;
    //  地址*/
    private String[] orderContacts_contactsAddress;
    //  传真*/
    private String[] orderContacts_contactsFax;
    //  QQ*/
    private String[] orderContacts_contactsQQ;
    //  邮箱*/
    private String[] orderContacts_contactsEmail;
    //  邮编*/
    private String[] orderContacts_contactsZipCode;
    //  其他*/
    private String[] orderContacts_remark;
	
    
    //游客类型费用、费用调整 等信息
    //"价格类型：1 团期价格类型（游客类型的价格存储类型）；2 返佣类型；3 优惠类型 ；4 其他类型； 5 退款"
  	private String[] islandOrderPrice_priceType;
  	//"海岛游产品团期价格表UUID" 
  	private String[] islandOrderPrice_activityIslandGroupPriceUuid;//islandOrderPrice_travelerTypeUuid
  	//"币种"
  	private String[] islandOrderPrice_currencyId;
  	//"游客类型"
  	private String[] islandOrderPrice_travelerType;
  	//"舱位等级"
  	private String[] islandOrderPrice_spaceLevel;
  	//"价格"
  	private String[] islandOrderPrice_price;
  	//"个数"  
  	private String[] islandOrderPrice_num; //个数为空时不会进行数据的映射，费用调整的要保存数据请传“0”
  	//"金额名称" 
  	private String[] islandOrderPrice_priceName;
  	//"备注"
  	private String[] islandOrderPrice_remark;
    
  	
  	/**游客信息 主表信息 及 子表信息 Start*/
  	//"主键"
  	//"游客姓名"
  	private String[] islandTraveler_name;
  	//"游客名称拼音"
  	private String[] islandTraveler_nameSpell;
  	//"舱位等级"
  	private String[] islandTraveler_spaceLevel;
  	//"人员类型（游客类型，数据字典）"
  	private String[] islandTraveler_personType;
  	//"性别 1-男 2-女"
  	private String[] islandTraveler_sex;
  	//"备注"
  	private String[] islandTraveler_remark;
  	
  	//"附件信息" 
  	private String[] islandTraveler_files;//拼接规则 docId#docName#docPath 如： [111#222#333；444#555#666] 如有空值用“_”占位
  	
  	//签证信息
  	private String[] islandTraveler_visaInfo;//拼接规则 country#visaTypeId 如： [111#222；444#555] 如有空值用“_”占位
  	
  	//金额信息
  	//订单的金额信息，订单总额、结算总额、应收、已收、未收
  	private String[] islandMoneyAmount_currencyId;
  	private String[] islandMoneyAmount_amount;
  	private String[] islandMoneyAmount_moneyType;
  	//游客的金额信息 
  	private String[] islandTraveler_amount;//拼接规则 currencyId#amount#exchangerate 如： [111#222#333；444#555#666] 如有空值用“_”占位
  	
  	//游客的证件信息
  	private String[] islandTraveler_papersType;//拼接规则 papersType#idCard#validityDate 如： [111#222#333；444#555#666] 如有空值用“_”占位
  	
  	/**游客信息 主表信息 及 子表信息 end*/
  	
	public IslandOrderInput(){
	}
	//数据库映射bean转换成表单提交bean
	public IslandOrderInput(IslandOrder obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public IslandOrder getIslandOrder() throws Exception {
		dataObj = new IslandOrder();
		BeanUtil.copySimpleProperties(dataObj , this , true );
		
		//联系人信息
		dataObj.setOrderContactsList(super.transfer2Object(OrderContacts.class, this));
		//游客类型费用、费用调整 等信息 集合  ----- 个数为空时不会进行数据的映射，费用调整的要保存数据请传“0”
	  	dataObj.setIslandOrderPriceList(super.transfer2Object(IslandOrderPrice.class, this,new String[]{"islandOrderPrice_num"}));
	  	//游客信息 集合
	  	dataObj.setIslandTravelerList(super.transfer2Object(IslandTraveler.class, this));
	  	//订单的金额信息，订单总额、结算总额、应收、已收
	  	dataObj.setIslandMoneyAmountList(super.transfer2Object(IslandMoneyAmount.class, this));
	  	
		return dataObj;
	}
	
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getUuid() {
		return uuid;
	}
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}
	public java.lang.String getActivityIslandUuid() {
		return activityIslandUuid;
	}
	public void setActivityIslandUuid(java.lang.String activityIslandUuid) {
		this.activityIslandUuid = activityIslandUuid;
	}
	public java.lang.String getActivityIslandGroupUuid() {
		return activityIslandGroupUuid;
	}
	public void setActivityIslandGroupUuid(java.lang.String activityIslandGroupUuid) {
		this.activityIslandGroupUuid = activityIslandGroupUuid;
	}
	public java.lang.Integer getOrderCompany() {
		return orderCompany;
	}
	public void setOrderCompany(java.lang.Integer orderCompany) {
		this.orderCompany = orderCompany;
	}
	public java.lang.String getOrderCompanyName() {
		return orderCompanyName;
	}
	public void setOrderCompanyName(java.lang.String orderCompanyName) {
		this.orderCompanyName = orderCompanyName;
	}
	public java.lang.Integer getOrderPersonNum() {
		return orderPersonNum;
	}
	public void setOrderPersonNum(java.lang.Integer orderPersonNum) {
		this.orderPersonNum = orderPersonNum;
	}
	public Integer getPlaceHolderType() {
		return placeHolderType;
	}
	public void setPlaceHolderType(Integer placeHolderType) {
		this.placeHolderType = placeHolderType;
	}
	public java.lang.Boolean getIsAfterSupplement() {
		return isAfterSupplement;
	}
	public void setIsAfterSupplement(java.lang.Boolean isAfterSupplement) {
		this.isAfterSupplement = isAfterSupplement;
	}
	public java.lang.Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(java.lang.Integer paymentType) {
		this.paymentType = paymentType;
	}
	public void setForecaseReportRoomNum(java.lang.Integer value) {
		this.forecaseReportRoomNum = value;
	}
	public java.lang.Integer getForecaseReportRoomNum() {
		return this.forecaseReportRoomNum;
	}
	public void setForecaseReportTicketNum(java.lang.Integer value) {
		this.forecaseReportTicketNum = value;
	}
	public java.lang.Integer getForecaseReportTicketNum() {
		return this.forecaseReportTicketNum;
	}
	public IslandOrder getDataObj() {
		return dataObj;
	}
	public void setDataObj(IslandOrder dataObj) {
		this.dataObj = dataObj;
	}
	public String[] getOrderContacts_contactsName() {
		return orderContacts_contactsName;
	}
	public void setOrderContacts_contactsName(String[] orderContacts_contactsName) {
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
	public void setOrderContacts_contactsEmail(String[] orderContacts_contactsEmail) {
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
	public String[] getIslandOrderPrice_priceType() {
		return islandOrderPrice_priceType;
	}
	public void setIslandOrderPrice_priceType(String[] islandOrderPrice_priceType) {
		this.islandOrderPrice_priceType = islandOrderPrice_priceType;
	}
	public String[] getIslandOrderPrice_activityIslandGroupPriceUuid() {
		return islandOrderPrice_activityIslandGroupPriceUuid;
	}
	public void setIslandOrderPrice_activityIslandGroupPriceUuid(
			String[] islandOrderPrice_activityIslandGroupPriceUuid) {
		this.islandOrderPrice_activityIslandGroupPriceUuid = islandOrderPrice_activityIslandGroupPriceUuid;
	}
	public String[] getIslandOrderPrice_currencyId() {
		return islandOrderPrice_currencyId;
	}
	public void setIslandOrderPrice_currencyId(String[] islandOrderPrice_currencyId) {
		this.islandOrderPrice_currencyId = islandOrderPrice_currencyId;
	}
	public String[] getIslandOrderPrice_travelerType() {
		return islandOrderPrice_travelerType;
	}
	public void setIslandOrderPrice_travelerType(
			String[] islandOrderPrice_travelerType) {
		this.islandOrderPrice_travelerType = islandOrderPrice_travelerType;
	}

	public String[] getIslandOrderPrice_spaceLevel() {
		return islandOrderPrice_spaceLevel;
	}
	public void setIslandOrderPrice_spaceLevel(String[] islandOrderPrice_spaceLevel) {
		this.islandOrderPrice_spaceLevel = islandOrderPrice_spaceLevel;
	}
	public String[] getIslandOrderPrice_price() {
		return islandOrderPrice_price;
	}
	public void setIslandOrderPrice_price(String[] islandOrderPrice_price) {
		this.islandOrderPrice_price = islandOrderPrice_price;
	}
	public String[] getIslandOrderPrice_num() {
		return islandOrderPrice_num;
	}
	public void setIslandOrderPrice_num(String[] islandOrderPrice_num) {
		this.islandOrderPrice_num = islandOrderPrice_num;
	}
	public String[] getIslandOrderPrice_priceName() {
		return islandOrderPrice_priceName;
	}
	public void setIslandOrderPrice_priceName(String[] islandOrderPrice_priceName) {
		this.islandOrderPrice_priceName = islandOrderPrice_priceName;
	}
	public String[] getIslandOrderPrice_remark() {
		return islandOrderPrice_remark;
	}
	public void setIslandOrderPrice_remark(String[] islandOrderPrice_remark) {
		this.islandOrderPrice_remark = islandOrderPrice_remark;
	}
	public String[] getIslandTraveler_name() {
		return islandTraveler_name;
	}
	public void setIslandTraveler_name(String[] islandTraveler_name) {
		this.islandTraveler_name = islandTraveler_name;
	}
	public String[] getIslandTraveler_nameSpell() {
		return islandTraveler_nameSpell;
	}
	public void setIslandTraveler_nameSpell(String[] islandTraveler_nameSpell) {
		this.islandTraveler_nameSpell = islandTraveler_nameSpell;
	}
	public String[] getIslandTraveler_spaceLevel() {
		return islandTraveler_spaceLevel;
	}
	public void setIslandTraveler_spaceLevel(String[] islandTraveler_spaceLevel) {
		this.islandTraveler_spaceLevel = islandTraveler_spaceLevel;
	}
	public String[] getIslandTraveler_personType() {
		return islandTraveler_personType;
	}
	public void setIslandTraveler_personType(String[] islandTraveler_personType) {
		this.islandTraveler_personType = islandTraveler_personType;
	}
	public String[] getIslandTraveler_sex() {
		return islandTraveler_sex;
	}
	public void setIslandTraveler_sex(String[] islandTraveler_sex) {
		this.islandTraveler_sex = islandTraveler_sex;
	}
	public String[] getIslandTraveler_remark() {
		return islandTraveler_remark;
	}
	public void setIslandTraveler_remark(String[] islandTraveler_remark) {
		this.islandTraveler_remark = islandTraveler_remark;
	}
	public String[] getIslandTraveler_files() {
		return islandTraveler_files;
	}
	public void setIslandTraveler_files(String[] islandTraveler_files) {
		this.islandTraveler_files = islandTraveler_files;
	}
	public String[] getIslandTraveler_visaInfo() {
		return islandTraveler_visaInfo;
	}
	public void setIslandTraveler_visaInfo(String[] islandTraveler_visaInfo) {
		this.islandTraveler_visaInfo = islandTraveler_visaInfo;
	}
	
	public String[] getIslandMoneyAmount_currencyId() {
		return islandMoneyAmount_currencyId;
	}
	public void setIslandMoneyAmount_currencyId(
			String[] islandMoneyAmount_currencyId) {
		this.islandMoneyAmount_currencyId = islandMoneyAmount_currencyId;
	}
	public String[] getIslandMoneyAmount_amount() {
		return islandMoneyAmount_amount;
	}
	public void setIslandMoneyAmount_amount(String[] islandMoneyAmount_amount) {
		this.islandMoneyAmount_amount = islandMoneyAmount_amount;
	}
	public String[] getIslandMoneyAmount_moneyType() {
		return islandMoneyAmount_moneyType;
	}
	public void setIslandMoneyAmount_moneyType(
			String[] islandMoneyAmount_moneyType) {
		this.islandMoneyAmount_moneyType = islandMoneyAmount_moneyType;
	}
	public String[] getIslandTraveler_amount() {
		return islandTraveler_amount;
	}
	public void setIslandTraveler_amount(String[] islandTraveler_amount) {
		this.islandTraveler_amount = islandTraveler_amount;
	}
	public String[] getIslandTraveler_papersType() {
		return islandTraveler_papersType;
	}
	public void setIslandTraveler_papersType(String[] islandTraveler_papersType) {
		this.islandTraveler_papersType = islandTraveler_papersType;
	}
	
	public java.lang.String getRemark() {
		return remark;
	}
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	static{
		
		//初始化 input 表单的 拆分规则
		if(!baseInputBeanMap.containsKey("islandTraveler_files")){
			BaseInputBean bean = new BaseInputBean();
			bean.setClassz(HotelAnnex.class);
			bean.setPropertyName(new String[]{"docId","docName","docPath"});
			bean.setChildListName("islandTravelerFilesList");
			baseInputBeanMap.put("islandTraveler_files", bean);
			
			bean = new BaseInputBean();
			bean.setClassz(IslandTravelervisa.class);
			bean.setPropertyName(new String[]{"country","visaTypeId"});
			bean.setChildListName("islandTravelervisaList");
			baseInputBeanMap.put("islandTraveler_visaInfo", bean);
			
			bean = new BaseInputBean();
			bean.setClassz(IslandMoneyAmount.class);
			bean.setPropertyName(new String[]{"currencyId","amount"});
			bean.setChildListName("islandMoneyAmountList");
			baseInputBeanMap.put("islandTraveler_amount", bean);
			
			bean = new BaseInputBean();
			bean.setClassz(IslandTravelerPapersType.class);
			bean.setPropertyName(new String[]{"papersType","idCard","validityDate"});
			bean.setChildListName("islandTravelerPapersTypeList");
			baseInputBeanMap.put("islandTraveler_papersType", bean);
		}
	}

}

