package com.trekiz.admin.modules.log.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.log.entity.LogOrder;
import com.trekiz.admin.modules.log.repository.LogOrderDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.repository.TravelerFileDao;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaDao;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;

/**
 * Created by Zong on 2016/7/25.
 */
@Service
@Transactional(readOnly = true)
public class LogOrderService {
    @Autowired
    private LogOrderDao logOrderDao;
    @Autowired
    private VisaOrderDao visaOrderDao;
    @Autowired
    private OrderContactsDao orderContactsDao;
    @Autowired
    private TravelerDao travelerDao;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
    private AgentinfoDao agentInfoDao;
    @Autowired
    private TravelerFileDao travelerFileDao;
    @Autowired
    private TravelerVisaDao travelerVisaDao;
    @Autowired
    private VisaDao visaDao;


    /**
     * 修改订单--渠道联系人
     * @param orderId
     * @param maps
     * @param agentName
     */
    public void saveLogOrder4Agent(String orderId, List<Map<String, String>> maps, String agentName) {
        Long orderIdLong = Long.valueOf(orderId);
        VisaOrder visaOrder = visaOrderDao.findByOrderId(orderIdLong);
        String agentNameById = "";
        if(visaOrder.getAgentinfoId() != -1) {
            agentNameById = agentInfoDao.getAgentNameById(visaOrder.getAgentinfoId());
        } else {
            agentNameById = visaOrder.getAgentinfoName();
        }
        if(!agentName.replaceAll("\r|\n|\t", "").equals(agentNameById)) {
            saveObj("agentinfo_name", orderIdLong, content("预订渠道", agentNameById, agentName), 2);
        }

        List<OrderContacts> orderContactsList = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.valueOf(orderId), 6);
        Set<Map<String, String>> addSet = new HashSet<Map<String, String>>(); //记录添加的渠道联系人
        Set<OrderContacts> delSet = new HashSet<OrderContacts>(); //记录删除的渠道联系人
        for (int i = 0; i < orderContactsList.size(); i++) {
            boolean del = true; //标记是否被删除
            OrderContacts orderContacts = orderContactsList.get(i);
            for (int j = 0; j < maps.size(); j++) {
                Map<String, String> map = maps.get(j);
                String contactsId = map.get("contactsId");
                if (contactsId == null || "".equals(contactsId)) {
                    addSet.add(map);
//                    break;
                }
//				OrderContacts orderContacts2 = contactsDao.getById(contactsId);
                if (contactsId != null && contactsId.equals(orderContacts.getId().toString())) {
                    del = false;
                    modifyAgent(maps, i, orderIdLong, orderContacts);
                }
//                if (contactsId == null) {
//                    del = false;
//                }
            }
            if (del) {
                delSet.add(orderContacts);
            }
        }

        for (Map<String, String> m : addSet) {
            saveObj("", orderIdLong, "添加渠道联系人：" + m.get("contactsName"), 1);
        }
        for (OrderContacts orderContacts : delSet) {
            saveObj("", orderIdLong, "删除渠道联系人：" + orderContacts.getContactsName(), 3);
        }
    }

    private void modifyAgent(List<Map<String, String>> maps, int i, Long orderIdLong, OrderContacts orderContacts) {
        if(!getString(maps.get(i).get("contactsAddress")).equals(getString(orderContacts.getContactsAddress()))) {
            saveObj("contactsAddress", orderIdLong, content("渠道地址", getString(orderContacts.getContactsAddress()), maps.get(i).get("contactsAddress")), 2);
        }
        if(!getString(maps.get(i).get("contactsEmail")).equals(getString(orderContacts.getContactsEmail()))) {
            saveObj("contactsEmail", orderIdLong, content("Email", getString(orderContacts.getContactsEmail()), maps.get(i).get("contactsEmail")), 2);
        }
        if(!getString(maps.get(i).get("contactsFax")).equals(getString(orderContacts.getContactsFax()))) {
            saveObj("contactsFax", orderIdLong, content("传真", getString(orderContacts.getContactsFax()), maps.get(i).get("contactsFax")), 2);
        }
        if(!getString(maps.get(i).get("contactsName")).equals(getString(orderContacts.getContactsName()))) {
            saveObj("contactsName", orderIdLong, content("渠道联系人", getString(orderContacts.getContactsName()), maps.get(i).get("contactsName")), 2);
        }
        if(!getString(maps.get(i).get("contactsQQ")).equals(getString(orderContacts.getContactsQQ()))) {
            saveObj("contactsQQ", orderIdLong, content("QQ", getString(orderContacts.getContactsQQ()), maps.get(i).get("contactsQQ")), 2);
        }
        if(!getString(maps.get(i).get("contactsTel")).equals(getString(orderContacts.getContactsTel()))) {
            saveObj("contactsTel", orderIdLong, content("渠道联系人电话", getString(orderContacts.getContactsTel()), maps.get(i).get("contactsTel")), 2);
        }
        if(!getString(maps.get(i).get("contactsTixedTel")).equals(getString(orderContacts.getContactsTixedTel()))) {
            saveObj("contactsTixedTel", orderIdLong, content("固定电话", getString(orderContacts.getContactsTixedTel()), maps.get(i).get("contactsTixedTel")), 2);
        }
        if(!getString(maps.get(i).get("contactsZipCode")).equals(getString(orderContacts.getContactsZipCode()))) {
            saveObj("contactsZipCode", orderIdLong, content("渠道邮编", getString(orderContacts.getContactsZipCode()), maps.get(i).get("contactsZipCode")), 2);
        }
        if(!getString(maps.get(i).get("remark")).equals(getString(orderContacts.getRemark()))) {
            saveObj("remark", orderIdLong, content("其他", getString(orderContacts.getRemark()), maps.get(i).get("remark")), 2);
        }
    }

    /**
     * 修改订单--游客信息
     * @param traveler
     */
    public void saveLogOrder4Traveler(Traveler traveler, String orderId, Visa visa, String inputClearPrice) {
        Long orderIdLong = Long.valueOf(orderId);
        Traveler oldTraveler = travelerDao.findOne(traveler.getId());
        if(!traveler.getName().equals(oldTraveler.getName())) {
            saveObj("name", orderIdLong, content("姓名", oldTraveler.getName(), traveler.getName()), 2);
        }
        if(!traveler.getNameSpell().equals(oldTraveler.getNameSpell())) {
            saveObj("nameSpell", orderIdLong, content("英文/拼音", oldTraveler.getNameSpell(), traveler.getNameSpell()), 2);
        }
        if(!traveler.getSex().equals(oldTraveler.getSex())) {
            String oldSexStr = "";
            Integer oldSex = oldTraveler.getSex();
            if(1 == oldSex) {
                oldSexStr = "男";
            } else if (2 == oldSex) {
                oldSexStr = "女";
            }
            String newSexStr = "";
            Integer newSex = traveler.getSex();
            if(1 == newSex) {
                newSexStr = "男";
            } else if (2 == newSex) {
                newSexStr = "女";
            }
            saveObj("sex", orderIdLong, content("性别", oldSexStr, newSexStr), 2);
        }
        String birthDay = getString(traveler.getBirthDay());
        String oldBirthDay = getString(oldTraveler.getBirthDay());
        if(!birthDay.equals(oldBirthDay)) {
            saveObj("birthDay", orderIdLong, content("出生日期", oldBirthDay, birthDay), 2);
        }
        if(!traveler.getTelephone().equals(oldTraveler.getTelephone())) {
            saveObj("telephone", orderIdLong, content("联系电话", oldTraveler.getTelephone(), traveler.getTelephone()), 2);
        }
        if(!traveler.getPassportCode().equals(oldTraveler.getPassportCode())) {
            saveObj("passportCode", orderIdLong, content("护照号", oldTraveler.getPassportCode(), traveler.getPassportCode()), 2);
        }
        String issuePlace = getString(traveler.getIssuePlace());
        String oldIssuePlace = getString(oldTraveler.getIssuePlace());
        if(!issuePlace.equals(oldIssuePlace)) {
            saveObj("issuePlace", orderIdLong, content("护照签发日期", oldIssuePlace, issuePlace), 2);
        }
        String passportValidity = getString(traveler.getPassportValidity());
        String oldPassportValidity = getString(oldTraveler.getPassportValidity());
        if(!passportValidity.equals(oldPassportValidity)) {
            saveObj("passportValidity", orderIdLong, content("护照有效期", oldPassportValidity, passportValidity), 2);
        }
        if(!getString(traveler.getIssuePlace1()).equals(getString(oldTraveler.getIssuePlace1()))){
        	saveObj("issuePlace1", orderIdLong, content("签发地", getString(oldTraveler.getIssuePlace1()), getString(traveler.getIssuePlace1())), 2);
	    }
	    if(!getString(traveler.getIssueDate()).equals(getString(oldTraveler.getIssueDate()))){
	    	saveObj("issueDate", orderIdLong, content("签发日期", getString(oldTraveler.getIssueDate()), getString(traveler.getIssueDate())), 2);
	    }//544end
        if(!traveler.getPassportType().equals(oldTraveler.getPassportType())) {
            String oldPassportTypeStr = "";
            String oldPassportType = oldTraveler.getPassportType();
            if("1".equals(oldPassportType)) {
                oldPassportTypeStr = "因公护照";
            } else if ("2".equals(oldPassportType)) {
                oldPassportTypeStr = "因私护照";
            }
            String newPassportTypeStr = "";
            String newPassportType = traveler.getPassportType();
            if("1".equals(newPassportType)) {
                newPassportTypeStr = "因公护照";
            } else if ("2".equals(newPassportType)) {
                newPassportTypeStr = "因私护照";
            }
            saveObj("passportType", orderIdLong, content("护照类型", oldPassportTypeStr, newPassportTypeStr), 2);
        }
        if(!traveler.getRemark().equals(oldTraveler.getRemark())) {
            saveObj("remark", orderIdLong, content("备注", oldTraveler.getRemark(), traveler.getRemark()), 2);
        }

        Visa oldVisa = visaDao.findByTravelerId(traveler.getId());
        if (!getString(visa.getPassportPhotoId()).equals(getString(oldVisa.getPassportPhotoId()))) {
            saveObj("passport_photo_id", orderIdLong, content("护照首页", getDocName(oldVisa.getPassportPhotoId()), getDocName(visa.getPassportPhotoId())), 2);
        }
        if (!getString(visa.getIdentityFrontPhotoId()).equals(getString(oldVisa.getIdentityFrontPhotoId()))) {
            saveObj("identity_front_photo_id", orderIdLong, content("身份证正面", getDocName(oldVisa.getIdentityFrontPhotoId()), getDocName(visa.getIdentityFrontPhotoId())), 2);
        }
        if (!getString(visa.getTablePhotoId()).equals(getString(oldVisa.getTablePhotoId()))) {
            saveObj("table_photo_id", orderIdLong, content("申请表格", getDocName(oldVisa.getTablePhotoId()), getDocName(visa.getTablePhotoId())), 2);
        }
        if (!getString(visa.getPersonPhotoId()).equals(getString(oldVisa.getPersonPhotoId()))) {
            saveObj("person_photo_id", orderIdLong, content("电子照片", getDocName(oldVisa.getPersonPhotoId()), getDocName(visa.getPersonPhotoId())), 2);
        }
        if (!getString(visa.getIdentityBackPhotoId()).equals(getString(oldVisa.getIdentityBackPhotoId()))) {
            saveObj("identity_back_photo_id", orderIdLong, content("身份证反面", getDocName(oldVisa.getIdentityBackPhotoId()), getDocName(visa.getIdentityBackPhotoId())), 2);
        }
        if (!getString(visa.getDocIds()).equals(getString(oldVisa.getDocIds()))) {
            saveObj("visa_doc_id", orderIdLong, content("签证附件", getDocsName(oldVisa.getDocIds()), getDocsName(visa.getDocIds())), 2);
        }
        if (!getString(visa.getFamilyRegisterPhotoId()).equals(getString(oldVisa.getFamilyRegisterPhotoId()))) {
            saveObj("family_register_photo_id", orderIdLong, content("户口本", getDocName(oldVisa.getFamilyRegisterPhotoId()), getDocName(visa.getFamilyRegisterPhotoId())), 2);
        }
        if (!getString(visa.getHouseEvidencePhotoId()).equals(getString(oldVisa.getHouseEvidencePhotoId()))) {
            saveObj("house_evidence_photo_id", orderIdLong, content("房产证", getDocName(oldVisa.getHouseEvidencePhotoId()), getDocName(visa.getHouseEvidencePhotoId())), 2);
        }
        if (!getString(visa.getOtherPhotoId()).equals(getString(oldVisa.getOtherPhotoId()))) {
            saveObj("other_photo_id", orderIdLong, content("其它", getDocName(oldVisa.getOtherPhotoId()), getDocName(visa.getOtherPhotoId())), 2);
        }
//        if (!getString(visa.getRemark()).equals(getString(oldVisa.getRemark()))) {
//            saveObj("remark", orderIdLong, content("备注", oldVisa.getRemark(), visa.getRemark()), 2);
//        }
//        MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(traveler.getRebatesMoneySerialNum());
//        if(!getString(traveler.getRebatesCurrencyID()).equals(getString(moneyAmount.getCurrencyId()))) {
//            saveObj("currencyId", orderIdLong, content("预计个人返佣币种", CurrencyUtils.getCurrencyNameOrFlag(moneyAmount.getCurrencyId().longValue(), "1"), CurrencyUtils.getCurrencyNameOrFlag(Long.valueOf(traveler.getRebatesCurrencyID()), "1")), 2);
//        }
//        if(!getString(traveler.getRebatesAmount()).equals(getString(moneyAmount.getAmount()))) {
//            saveObj("amount", orderIdLong, content("预计个人返佣金额", moneyAmount.getAmount().toString(), traveler.getRebatesAmount()), 2);
//        }
//        //结算价
//        MoneyAmount moneyAmount2 = moneyAmountService.findOneAmountBySerialNum(traveler.getPayPriceSerialNum());
//        if(!inputClearPrice.equals(moneyAmount2.getAmount().toString())) {
//            DecimalFormat df = new DecimalFormat("#.00");
//            String format = df.format(Double.valueOf(inputClearPrice));
//            saveObj("amount", orderIdLong, content("结算价", moneyAmount2.getAmount().toString(), format), 2);
//        }
    }

    public void saveLogOrder4Visa(Visa visa, String orderId) {
        Long orderIdLong = Long.valueOf(orderId);
        Visa oldVisa = visaDao.findOne(visa.getId());
        if(visa.getVisaStauts() != oldVisa.getVisaStauts()) {
            saveObj("visa_stauts", orderIdLong, content("签证状态", getVisaStatusName(oldVisa.getVisaStauts()), getVisaStatusName(visa.getVisaStauts())), 2);
        }
        if(!getString(visa.getMakeTable()).equals(getString(oldVisa.getMakeTable()))) {
            saveObj("make_table", orderIdLong, content("制表人姓名", oldVisa.getMakeTable(), visa.getMakeTable()), 2);
        }
        if(!getString(visa.getStartOut()).equals(getString(oldVisa.getStartOut()))) {
            saveObj("start_out", orderIdLong, content("实际出团时间", getString(oldVisa.getStartOut()), getString(visa.getStartOut())), 2);
        }
        if(!getString(visa.getContract()).equals(getString(oldVisa.getContract()))) {
            saveObj("contract", orderIdLong, content("实际约签时间", getString(oldVisa.getContract()), getString(visa.getContract())), 2);
        }
        if(!getString(visa.getAACode()).equals(getString(oldVisa.getAACode()))) {
            saveObj("AA_code", orderIdLong, content("AA码", getString(oldVisa.getAACode()), visa.getAACode()), 2);
        }
        if(!getString(visa.getUIDCode()).equals(getString(oldVisa.getUIDCode()))) {
            saveObj("UIDCode", orderIdLong, content("UID编号", getString(oldVisa.getUIDCode()), visa.getUIDCode()), 2);
        }
        if(!getString(visa.getPassportOperateRemark()).equals(getString(oldVisa.getPassportOperateRemark()))) {
            saveObj("passport_operate_remark", orderIdLong, content("备注", getString(oldVisa.getPassportOperateRemark()), visa.getPassportOperateRemark()), 2);
        }

    }

    private String getVisaStatusName(int visaStatus) {
        String visaStatusName = "";
        switch (visaStatus) {
            case 0:
                visaStatusName = "未送签";
                break;
            case 1:
                visaStatusName = "送签";
                break;
            case 2:
                visaStatusName = "已约签";
                break;
            case 3:
                visaStatusName = "通过";
                break;
            case 4:
                visaStatusName = "未约签";
                break;
            case 5:
                visaStatusName = "已撤签";
                break;
            case 6:
                visaStatusName = "";
                break;
            case 7:
                visaStatusName = "拒签";
                break;
            case 8:
                visaStatusName = "调查";
                break;
            case 9:
                visaStatusName = "续补资料";
                break;
            default:
                visaStatusName = "";
                break;
        }
        return visaStatusName;
    }

    private String getDocName(Long id) {
        if (id == null) {
            return "";
        } else {
            DocInfo docInfo = docInfoService.getDocInfo(id);
            return docInfo.getDocName();
        }
    }

    private String getDocsName(String ids) {
        if (ids == null) {
            return "";
        } else {
            String[] idArr = ids.split(",");
            String docsName = "";
            for (int i = 0; i < idArr.length; i++) {
                String id = idArr[i];
                DocInfo docInfo = docInfoService.getDocInfo(Long.valueOf(id));
                docsName += docInfo.getDocName() + " ";
            }
            return docsName;
        }
    }

    private String getString(Object obj) {
        if (obj == null) {
            return "";
        }else if (obj instanceof Date) {
            return new DateTime(obj).toString("yyyy-MM-dd");
        }else {
           return obj.toString();
        }
    }

    public String content(String fieldName, String oldValue, String newValue) {
        return "【" + fieldName + "】由【" + oldValue + "】修改成了【" + newValue + "】";
    }

    public void saveObj(String fieldName, Long orderId, String content, Integer opType) {
        User user = UserUtils.getUser();
        LogOrder logOrder = new LogOrder();
        logOrder.setUuid(UuidUtils.generUuid());
        logOrder.setOrderType(6);
        logOrder.setBussinessType(1);
        logOrder.setBussinessId(orderId);
        logOrder.setOpType(opType);
        logOrder.setFieldName(fieldName);
        logOrder.setContent(content);
        logOrder.setCreateBy(user.getId());
        logOrder.setCreateDate(new Date());
        logOrder.setCompanyId(user.getCompany().getId());
        logOrderDao.saveObj(logOrder);
    }

    /**
     * 修改记录列表
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getLogOrderList(Long orderId) {
        String sql = "SELECT " +
                "lo.create_by, " +
                "lo.create_date, " +
                "GROUP_CONCAT(lo.content SEPARATOR '，') content " +
                "FROM " +
                " log_order lo " +
                "WHERE " +
                " lo.order_type = 6 " +
                "AND lo.bussiness_type = 1 " +
                "AND lo.bussiness_id = ? " +
                "GROUP BY " +
                " lo.create_date" +
                " ORDER BY lo.create_date DESC ";
        List<Map<String, Object>> list = logOrderDao.findBySql(sql, Map.class, orderId);
        return list;
    }

    public List<Map<String, Object>> getLogOrderListByCreateDate(Long orderId, String createDate) {
        String sql = "SELECT " +
                " lo.content " +
                "FROM " +
                "log_order lo " +
                "WHERE " +
                " lo.order_type = 6 " +
                "AND lo.bussiness_type = 1 " +
                "AND lo.bussiness_id = ? " +
                "AND lo.create_date = ?";
        List<Map<String, Object>> list = logOrderDao.findBySql(sql, Map.class, orderId, createDate);
        return list;
    }
    
    
    /**
    * 保存订单人数、渠道联系人 
    * 若orderContact的id为空，则该联系人为新添加的
    * @param modifyMap
    * @param preContactsList  修改之前的orderContactsList
    * @param request
    * @throws JSONException
    */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveLogSingleGroupOrder4Agent(Map<String, String> modifyMap, List<OrderContacts> preContactsList, HttpServletRequest request) throws Exception {
    	// 每次记录日志，产生不同的extend
    	String extend = UuidUtils.generUuid();
//    	String extend = SerialUtils.getNextSerial();
    	String afterOrderCompanyName = modifyMap.get("afterOrderCompanyName");
	    String afterOrderPersonNumChild = request.getParameter("orderPersonNumChild");
	    String afterOrderPersonNumAdult = request.getParameter("orderPersonNumAdult");
	    String afterOrderPersonNumSpecial = request.getParameter("orderPersonNumSpecial");
	    String afterSpecialDemand = request.getParameter("specialDemand").trim();
	    String afterSpecialDemandFileIds = request.getParameter("specialDemandFileIds");
	    Long orderId = Long.parseLong(modifyMap.get("orderId"));
	    if("10".equals(getString(modifyMap.get("orderStatus")))){  // 游轮订单
	    	if(!afterOrderPersonNumChild.equals(modifyMap.get("orderPersonNumChild"))) {
	    		saveSingleOrderLog("orderPersonNumChild", orderId, content("1/2人出行人数", modifyMap.get("orderPersonNumChild"), afterOrderPersonNumChild), 2, extend);
	    	}
	    	if(!afterOrderPersonNumAdult.equals(modifyMap.get("orderPersonNumAdult"))) {
	    		saveSingleOrderLog("orderPersonNumAdult", orderId, content("3/4人出行人数", modifyMap.get("orderPersonNumAdult"), afterOrderPersonNumAdult), 2, extend);
	    	}
	    }else{
	    	if(!afterOrderPersonNumChild.equals(modifyMap.get("orderPersonNumChild"))) {
	    		saveSingleOrderLog("orderPersonNumChild", orderId, content("儿童人数", modifyMap.get("orderPersonNumChild"), afterOrderPersonNumChild), 2, extend);
	    	}
	    	if(!afterOrderPersonNumAdult.equals(modifyMap.get("orderPersonNumAdult"))) {
	    		saveSingleOrderLog("orderPersonNumAdult", orderId, content("成人人数", modifyMap.get("orderPersonNumAdult"), afterOrderPersonNumAdult), 2, extend);
	    	}
	    	if(!afterOrderPersonNumSpecial.equals(modifyMap.get("orderPersonNumSpecial"))) {
	    		saveSingleOrderLog("orderPersonNumSpecial", orderId, content("特殊人群人数", modifyMap.get("orderPersonNumSpecial"), afterOrderPersonNumSpecial), 2, extend);
	    	}
	    }
	    if(!getString(modifyMap.get("preNewRoomNumber")).equals(getString(modifyMap.get("afterNewRoomNumber")))) {
	    	saveSingleOrderLog("specialDemandFileIds", orderId, content("房间总计", getString(modifyMap.get("preNewRoomNumber")), getString(modifyMap.get("afterNewRoomNumber"))), 2, extend);
	    }
	    if(!getString(afterSpecialDemand).equals(getString(modifyMap.get("preSpecialDemand")))) {
	    	saveSingleOrderLog("specialDemand", orderId, content("特殊需求", getString(modifyMap.get("preSpecialDemand")), getString(afterSpecialDemand)), 2, extend);
	    }
	    if(!getString(afterSpecialDemandFileIds).equals(getString(modifyMap.get("preSpecialDamandFileIds")))) {
	    	saveSingleOrderLog("specialDemandFileIds", orderId, content("特殊需求附件名称", getDocNames(modifyMap.get("preSpecialDamandFileIds")), getDocNames(afterSpecialDemandFileIds)), 2, extend);
	    }
	    if(!afterOrderCompanyName.equals(modifyMap.get("orderCompanyName"))) {
	    	saveSingleOrderLog("orderCompanyName", orderId, content("预订渠道", modifyMap.get("orderCompanyName"), afterOrderCompanyName), 2, extend);
	    }
	    // 从前台取
	    List<OrderContacts> afterContactsList = OrderUtil.getContactsListForLog(request.getParameter("orderContactsJSON"));
	    Set<OrderContacts> delSet = new HashSet<OrderContacts>(); //记录删除的渠道联系人
	    // 添加渠道联系人  若orderContact的id为空，则该联系人为新添加的
	    for (int i = 0; i < afterContactsList.size(); i++) {
	    		if(afterContactsList.get(i).getId() == null){
	    			saveSingleOrderLog("", orderId, "添加渠道联系人：" + afterContactsList.get(i).getContactsName(), 1, extend);
	    			afterContactsList.remove(i);
	    		}
	    	}
	    for (int i = 0; i < preContactsList.size(); i++) {
	        boolean del = true; //标记是否被删除
	        OrderContacts preContact = preContactsList.get(i);
	        for (int j = 0; j < afterContactsList.size(); j++) {
	    	OrderContacts afterContacts = afterContactsList.get(j);
	    	String contactsId = afterContacts.getId().toString();
	    	if (contactsId.equals(preContact.getId().toString())) {
	    	    del = false;
	    	    if(!getString(afterContacts.getContactsAddress()).equals(getString(preContact.getContactsAddress()))) {
	    		saveSingleOrderLog("contactsAddress", orderId, content("渠道地址", getString(preContact.getContactsAddress()), getString(afterContacts.getContactsAddress())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsEmail()).equals(getString(preContact.getContactsEmail()))) {
	    		saveSingleOrderLog("contactsEmail", orderId, content("Email", getString(preContact.getContactsEmail()), getString(afterContacts.getContactsEmail())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsFax()).equals(getString(preContact.getContactsFax()))) {
	    		saveSingleOrderLog("contactsFax", orderId, content("传真", getString(preContact.getContactsFax()), getString(afterContacts.getContactsFax())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsName()).equals(getString(preContact.getContactsName()))) {
	    		saveSingleOrderLog("contactsName", orderId, content("渠道联系人", getString(preContact.getContactsName()), getString(afterContacts.getContactsName())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsQQ()).equals(getString(preContact.getContactsQQ()))) {
	    		saveSingleOrderLog("contactsQQ", orderId, content("QQ", getString(preContact.getContactsQQ()), getString(afterContacts.getContactsQQ())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsTel()).equals(getString(preContact.getContactsTel()))) {
	    		saveSingleOrderLog("contactsTel", orderId, content("渠道联系人电话", getString(preContact.getContactsTel()), getString(afterContacts.getContactsTel())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsTixedTel()).equals(getString(preContact.getContactsTixedTel()))) {
	    		saveSingleOrderLog("contactsTixedTel", orderId, content("固定电话", getString(preContact.getContactsTixedTel()), getString(afterContacts.getContactsTixedTel())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getContactsZipCode()).equals(getString(preContact.getContactsZipCode()))) {
	    		saveSingleOrderLog("contactsZipCode", orderId, content("渠道邮编", getString(preContact.getContactsZipCode()), getString(afterContacts.getContactsZipCode())), 2, extend);
	    	    }
	    	    if(!getString(afterContacts.getRemark()).equals(getString(preContact.getRemark()))) {
	    		saveSingleOrderLog("remark", orderId, content("其他", getString(preContact.getRemark()), getString(afterContacts.getRemark())), 2, extend);
	    	    }
	    	}
        }
        if (del) {
        	delSet.add(preContact);
        }
	}

	    for (OrderContacts orderContacts : delSet) {
	    	saveSingleOrderLog("", orderId, "删除渠道联系人：" + orderContacts.getContactsName(), 3, extend);
	    }
    }

    /**
     * 游客修改日志保存
     * @param preTraveler
     * @param visasArr
     * @param preTravelerFileList
     * @param map
     */
    public void saveLogSingleGroupOrder4Traveler(Traveler preTraveler,JSONArray visasArr,List<TravelerFile>  preTravelerFileList,Map<String, Object> map) {
    	// 每次记录日志，产生不同的extend
    	String extend = UuidUtils.generUuid();
//    	String extend = SerialUtils.getNextSerial();
    	Long orderIdLong = preTraveler.getOrderId();
	    Traveler afterTraveler = travelerDao.findTravelerById(preTraveler.getId());
	    if(!getString(afterTraveler.getName()).equals(getString(preTraveler.getName()))) {
	    	saveSingleOrderLog("name", orderIdLong, content("姓名", getString(preTraveler.getName()), getString(afterTraveler.getName())), 2, extend);
	    }
	    if(!getString(afterTraveler.getNameSpell()).equals(getString(preTraveler.getNameSpell()))) {
	        saveSingleOrderLog("nameSpell", orderIdLong, content("英文/拼音", getString(preTraveler.getNameSpell()), getString(afterTraveler.getNameSpell())), 2, extend);
	    } 
	    if(!getString(afterTraveler.getSex()).equals(getString(preTraveler.getSex()))) {
	        String oldSexStr = "";
	        Integer oldSex = preTraveler.getSex();
	        if(1 == oldSex) {
	    	oldSexStr = "男";
	        } else if (2 == oldSex) {
	    	oldSexStr = "女";
	        }
	        String newSexStr = "";
	        Integer newSex = afterTraveler.getSex();
	        if(1 == newSex) {
	    	newSexStr = "男";
	        } else if (2 == newSex) {
	    	newSexStr = "女";
	        }
	        saveSingleOrderLog("sex", orderIdLong, content("性别", oldSexStr, newSexStr), 2, extend);
	    }
	    String preCountry = "";
	    String afterCountry = "";
	    if(preTraveler.getNationality() != null){
	    	preCountry = CountryUtils.getCountryName(Long.parseLong(preTraveler.getNationality().toString()));
	    }
	    if(afterTraveler.getNationality() != null){
	    	afterCountry = CountryUtils.getCountryName(Long.parseLong(afterTraveler.getNationality().toString()));
	    }
	    if(!preCountry.equals(afterCountry)) {
	    	saveSingleOrderLog("nationality", orderIdLong, content("国籍", preCountry, afterCountry), 2, extend);
	    }
	    String birthDay = getString(afterTraveler.getBirthDay());
	    String oldBirthDay = getString(preTraveler.getBirthDay());
	    if(!birthDay.equals(oldBirthDay)) {
	        saveSingleOrderLog("birthDay", orderIdLong, content("出生日期", oldBirthDay, birthDay), 2, extend);
	    }
	    if(!getString(afterTraveler.getTelephone()).equals(getString(preTraveler.getTelephone()))) {
	        saveSingleOrderLog("telephone", orderIdLong, content("联系电话", getString(preTraveler.getTelephone()), getString(afterTraveler.getTelephone())), 2, extend);
	    }
	    if(!getString(afterTraveler.getPassportCode()).equals(getString(preTraveler.getPassportCode()))) {
	        saveSingleOrderLog("passportCode", orderIdLong, content("护照号", getString(preTraveler.getPassportCode()), getString(afterTraveler.getPassportCode())), 2, extend);
	    }
	    //544 添加游客修改记录
	    if(!getString(afterTraveler.getIssuePlace1()).equals(getString(preTraveler.getIssuePlace1()))){
	    	 saveSingleOrderLog("issuePlace1", orderIdLong, content("签发地", getString(preTraveler.getIssuePlace1()), getString(afterTraveler.getIssuePlace1())), 2, extend);
	    }
	    if(!getString(afterTraveler.getIssueDate()).equals(getString(preTraveler.getIssueDate()))){
	    	 saveSingleOrderLog("issueDate", orderIdLong, content("签发日期", getString(preTraveler.getIssueDate()), getString(afterTraveler.getIssueDate())), 2, extend);
	    }//544end
	    String issuePlace = getString(afterTraveler.getIssuePlace());
	    String oldIssuePlace = getString(preTraveler.getIssuePlace());
	    if(!issuePlace.equals(oldIssuePlace)) {
	        saveSingleOrderLog("issuePlace", orderIdLong, content("发证日期", oldIssuePlace, issuePlace), 2, extend);
	    }
	    String passportValidity = getString(afterTraveler.getPassportValidity());
	    String oldPassportValidity = getString(preTraveler.getPassportValidity());
	    if(!passportValidity.equals(oldPassportValidity)) {
	        saveSingleOrderLog("passportValidity", orderIdLong, content("护照有效期", oldPassportValidity, passportValidity), 2, extend);
	    }
	    if(!getString(afterTraveler.getIdCard()).equals(getString(preTraveler.getIdCard()))) {
	    	saveSingleOrderLog("idCard", orderIdLong, content("身份证号", getString(preTraveler.getIdCard()), getString(afterTraveler.getIdCard())), 2, extend);
	    }
	    if(!getString(afterTraveler.getPassportType()).equals(getString(preTraveler.getPassportType()))) {
	        String oldPassportTypeStr = "";
	        String oldPassportType = getString(preTraveler.getPassportType());
	        if("1".equals(oldPassportType)) {
	    	oldPassportTypeStr = "因公护照";
	        } else if ("2".equals(oldPassportType)) {
	    	oldPassportTypeStr = "因私护照";
	        }
	        String newPassportTypeStr = "";
	        String newPassportType = getString(afterTraveler.getPassportType());
	        if("1".equals(newPassportType)) {
	    	newPassportTypeStr = "因公护照";
	        } else if ("2".equals(newPassportType)) {
	    	newPassportTypeStr = "因私护照";
	        }
	        saveSingleOrderLog("passportType", orderIdLong, content("护照类型", oldPassportTypeStr, newPassportTypeStr), 2, extend);
	    }
	
    	List<TravelerFile> afterTravelerFileList = travelerFileDao.findFileListByPid(preTraveler.getId());
    	// 根据前台filetype定义对应的数组
    	String[] fileNameArray = {"其它", "护照首页", "电子照片", "身份证正面", "身份证反面", "申请表格", "房产证", "户口本", "签证附件"};
	    for(int i = 0; i < afterTravelerFileList.size(); i++){
	    	Integer afterFileType = afterTravelerFileList.get(i).getFileType();
	    	String afterFileName = getString(afterTravelerFileList.get(i).getFileName());
	    	Boolean flag = true;
	    	for (int j = 0; j < preTravelerFileList.size(); j++) {
	    		// filetype相同
    			if(getString(preTravelerFileList.get(j).getFileType()).equals(afterFileType.toString())){
    				// fileName不等
    				flag = false;
    				if(!getString(preTravelerFileList.get(j).getFileName()).equals(afterFileName)){
    					saveSingleOrderLog("", orderIdLong, content(fileNameArray[afterFileType], getString(preTravelerFileList.get(j).getFileName()), afterFileName), 2, extend);
    				}
    			}
	    	}
	    	if(flag){
	    		// 新添加的文件
	    		saveSingleOrderLog("", orderIdLong, fileNameArray[afterFileType] + "(上传文件): " + afterFileName, 1, extend);
	    	}
	    }
	    
//	    // 返佣
//	    MoneyAmount mao1 = moneyAmountService.findOneAmountBySerialNum(getString(preTraveler.getRebatesMoneySerialNum()));
//	    String preRebatesMoney = "";
//	    if(mao1 == null){
//	    	preRebatesMoney = "";
//	    }else{
//	    	preRebatesMoney = mao1.getAmount().toString();
//	    	if(!getString(map.get("afterRebatesCurrencyId")).equals(mao1.getCurrencyId().toString())) {
//	    		saveSingleOrderLog("currencyId", orderIdLong, content("预计个人返佣币种", CurrencyUtils.getCurrencyNameOrFlag(Long.valueOf(mao1.getCurrencyId().toString()), "1"), CurrencyUtils.getCurrencyNameOrFlag(Long.valueOf(getString(map.get("afterRebatesCurrencyId"))), "1")), 2);
//	    	}
//	    }
//	    if(!getString(map.get("afterRebatesMoney")).equals(preRebatesMoney)) {
//	    	saveSingleOrderLog("amount", orderIdLong, content("预计个人返佣金额", preRebatesMoney , getString(map.get("afterRebatesMoney"))), 2);
//	    }
//	    //结算价
//	    if(!getString(map.get("afterInputClearPrice")).equals(getString(map.get("preInputClearPrice")))) {
//	    	//DecimalFormat df = new DecimalFormat("#.00");
//	    	//String format = df.format(Double.valueOf(getString(map.get("afterInputClearPrice"))));
//	        saveSingleOrderLog("amount", orderIdLong, content("结算价", getString(map.get("preInputClearPrice")), getString(map.get("afterInputClearPrice"))), 2);
//	    }
	    
	    // 游客自备签 & 签证
	    @SuppressWarnings("unchecked")
	    List<TravelerVisa> preTravelerVisaList = (List<TravelerVisa>) map.get("preTravelerVisa");
	    if(visasArr!=null && visasArr.size()>0 && preTravelerVisaList!=null && preTravelerVisaList.size()>0){
	    	for(int i = 0; i < visasArr.size(); i++){
	    		JSONObject afterJsonVisa = visasArr.getJSONObject(i);
	    		TravelerVisa preTravelerVisa = preTravelerVisaList.get(i);
	    		if(!"1".equals(getString(preTravelerVisa.getZbqType())) && "1".equals(getString(afterJsonVisa.getString("zbqType")))){
		    		// 之前不是自备签  勾选了自备签 
		    		saveSingleOrderLog("zbqType", orderIdLong, " 【自备签】由【】修改为了【勾选】 ", 2, extend);
		    	}else if("1".equals(getString(preTravelerVisa.getZbqType())) && !"1".equals(getString(afterJsonVisa.getString("zbqType")))){ 
		    		// 之前是自备签  去掉了自备签的勾选 
		    		saveSingleOrderLog("zbqType", orderIdLong, " 【自备签】由【勾选】修改为了【】 ", 2, extend);
		    	}else if("1".equals(getString(preTravelerVisa.getZbqType())) && "1".equals(getString(afterJsonVisa.getString("zbqType")))){
		    		// 前后都是自备签
		    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    		String preVisaDate = preTravelerVisa.getVisaDate() == null ? "" : formatter.format(preTravelerVisa.getVisaDate());
		    		String afterVisaDate = afterJsonVisa.getString("visaDate") == null ? "" : afterJsonVisa.getString("visaDate");
		    		if(!preVisaDate.equals(afterVisaDate)){
		    			saveSingleOrderLog("visaDate", orderIdLong, content("自备签有效期", preVisaDate, afterVisaDate), 2, extend);
		    		}
		    	}else{
		    		String preAreaName = getVisaTypeOrAreaName(getString(preTravelerVisa.getManorId()), "from_area");
		    		String afterAreaName = getVisaTypeOrAreaName(getString(afterJsonVisa.getString("manorId")), "from_area");
		    		if(!preAreaName.equals(afterAreaName)){
		    			saveSingleOrderLog("manorId", orderIdLong, content("领区", preAreaName, afterAreaName), 2, extend);
		    		}
		    		String preVisaType = getVisaTypeOrAreaName(getString(preTravelerVisa.getVisaTypeId()), "new_visa_type");
		    		String afterVisaType = getVisaTypeOrAreaName(getString(afterJsonVisa.getString("visaTypeId")), "new_visa_type");
		    		if(!preVisaType.equals(afterVisaType)){
		    			saveSingleOrderLog("visaTypeId", orderIdLong, content("签证类别", preVisaType, afterVisaType), 2, extend);
		    		}
		    		if(preTravelerVisa.getGroupOpenDate() != null && !getString(preTravelerVisa.getGroupOpenDate()).equals(getString(afterJsonVisa.getString("groupOpenDate")))){
		    			saveSingleOrderLog("groupOpenDate", orderIdLong, content("预计出团时间", getString(preTravelerVisa.getGroupOpenDate()), getString(afterJsonVisa.getString("groupOpenDate"))), 2, extend);
		    		}
		    		if(preTravelerVisa.getContractDate() != null && !getString(preTravelerVisa.getContractDate()).equals(getString(afterJsonVisa.getString("contractDate")))){
		    			saveSingleOrderLog("contractDate", orderIdLong, content("预计签约时间", getString(preTravelerVisa.getContractDate()), getString(afterJsonVisa.getString("contractDate"))), 2, extend);
		    		}
		    	}
	    	}
	    	
	    }
	    
	    if(!getString(afterTraveler.getRemark()).equals(getString(preTraveler.getRemark()))) {
	    	saveSingleOrderLog("remark", orderIdLong, content("备注", getString(preTraveler.getRemark()), getString(afterTraveler.getRemark())), 2, extend);
	    }

    }
    
    /**
     * 游客修改日志保存
     * @param preTraveler
     * @param visasArr
     * @param preTravelerFileList
     * @param map
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveYouJiaLogOrder4Traveler(Traveler preTraveler,JSONArray visasArr,List<Map<String,Object>> preTravelerFileList,List<Map<String, Object>> preVisalist) {
    	// 每次记录日志，产生不同的extend
    	String extend = UuidUtils.generUuid();
    	Long orderIdLong = preTraveler.getOrderId();
    	Traveler afterTraveler = travelerDao.findTravelerById(preTraveler.getId());
    	if(!getString(afterTraveler.getName()).equals(getString(preTraveler.getName()))) {
    		saveSingleOrderLog("name", orderIdLong, content("姓名", getString(preTraveler.getName()), getString(afterTraveler.getName())), 2, extend);
    	}
    	if(!getString(afterTraveler.getNameSpell()).equals(getString(preTraveler.getNameSpell()))) {
    		saveSingleOrderLog("nameSpell", orderIdLong, content("英文/拼音", getString(preTraveler.getNameSpell()), getString(afterTraveler.getNameSpell())), 2, extend);
    	} 
    	if(!getString(afterTraveler.getSex()).equals(getString(preTraveler.getSex()))) {
    		String oldSexStr = "";
    		Integer oldSex = preTraveler.getSex();
    		if(1 == oldSex) {
    			oldSexStr = "男";
    		} else if (2 == oldSex) {
    			oldSexStr = "女";
    		}
    		String newSexStr = "";
    		Integer newSex = afterTraveler.getSex();
    		if(1 == newSex) {
    			newSexStr = "男";
    		} else if (2 == newSex) {
    			newSexStr = "女";
    		}
    		saveSingleOrderLog("sex", orderIdLong, content("性别", oldSexStr, newSexStr), 2, extend);
    	}
    	String birthDay = getString(afterTraveler.getBirthDay());
    	String oldBirthDay = getString(preTraveler.getBirthDay());
    	if(!birthDay.equals(oldBirthDay)) {
    		saveSingleOrderLog("birthDay", orderIdLong, content("出生日期", oldBirthDay, birthDay), 2, extend);
    	}
    	if(!getString(afterTraveler.getTelephone()).equals(getString(preTraveler.getTelephone()))) {
    		saveSingleOrderLog("telephone", orderIdLong, content("联系电话", getString(preTraveler.getTelephone()), getString(afterTraveler.getTelephone())), 2, extend);
    	}
    	if(!getString(afterTraveler.getPassportCode()).equals(getString(preTraveler.getPassportCode()))) {
    		saveSingleOrderLog("passportCode", orderIdLong, content("护照号", getString(preTraveler.getPassportCode()), getString(afterTraveler.getPassportCode())), 2, extend);
    	}
    	String issuePlace = getString(afterTraveler.getIssuePlace());
    	String oldIssuePlace = getString(preTraveler.getIssuePlace());
    	if(!issuePlace.equals(oldIssuePlace)) {
    		saveSingleOrderLog("issuePlace", orderIdLong, content("发证日期", oldIssuePlace, issuePlace), 2, extend);
    	}
    	String passportValidity = getString(afterTraveler.getPassportValidity());
    	String oldPassportValidity = getString(preTraveler.getPassportValidity());
    	if(!passportValidity.equals(oldPassportValidity)) {
    		saveSingleOrderLog("passportValidity", orderIdLong, content("护照有效期", oldPassportValidity, passportValidity), 2, extend);
    	}
    	if(!getString(afterTraveler.getIdCard()).equals(getString(preTraveler.getIdCard()))) {
    		saveSingleOrderLog("idCard", orderIdLong, content("身份证号", getString(preTraveler.getIdCard()), getString(afterTraveler.getIdCard())), 2, extend);
    	}
    	if(!getString(afterTraveler.getPassportType()).equals(getString(preTraveler.getPassportType()))) {
    		String oldPassportTypeStr = "";
    		String oldPassportType = getString(preTraveler.getPassportType());
    		if("1".equals(oldPassportType)) {
    			oldPassportTypeStr = "因公护照";
    		} else if ("2".equals(oldPassportType)) {
    			oldPassportTypeStr = "因私护照";
    		}
    		String newPassportTypeStr = "";
    		String newPassportType = getString(afterTraveler.getPassportType());
    		if("1".equals(newPassportType)) {
    			newPassportTypeStr = "因公护照";
    		} else if ("2".equals(newPassportType)) {
    			newPassportTypeStr = "因私护照";
    		}
    		saveSingleOrderLog("passportType", orderIdLong, content("护照类型", oldPassportTypeStr, newPassportTypeStr), 2, extend);
    	}
    	if(!getString(afterTraveler.getPassportPlace()).equals(getString(preTraveler.getPassportPlace()))) {
    		saveSingleOrderLog("passportPlace", orderIdLong, content("护照签发地", getString(preTraveler.getPassportPlace()), getString(afterTraveler.getPassportPlace())), 2, extend);
    	}
    	if(!getString(afterTraveler.getPositionCn()).equals(getString(preTraveler.getPositionCn()))) {
    		saveSingleOrderLog("positionCn", orderIdLong, content("职务中文", getString(preTraveler.getPositionCn()), getString(afterTraveler.getPositionCn())), 2, extend);
    	}
    	if(!getString(afterTraveler.getPositionEn()).equals(getString(preTraveler.getPositionEn()))) {
    		saveSingleOrderLog("positionEn", orderIdLong, content("职务英文", getString(preTraveler.getPositionEn()), getString(afterTraveler.getPositionEn())), 2, extend);
    	}
    	
    	List<Map<String, Object>> afterTravelerFileList = travelerFileDao.findFilesByPid(preTraveler.getId());
    	// 根据前台filetype定义对应的数组
    	String[] fileNameArray = {"其它", "护照首页", "参团报告书", "个人资料表", "健康承诺书", "担保书", "", "", "签证附件"};
    	// TODO 多个文件的情况
    	for(int i = 0; i < afterTravelerFileList.size(); i++){
    		Integer afterFileType = Integer.parseInt(afterTravelerFileList.get(i).get("fileType").toString());
    		String afterFileName = getString(afterTravelerFileList.get(i).get("fileName"));
    		Boolean flag = true;
    		for (int j = 0; j < preTravelerFileList.size(); j++) {
    			// filetype相同
    			if(getString(preTravelerFileList.get(j).get("fileType")).equals(afterFileType.toString())){
    				// fileName不等
    				flag = false;
    				if(!getString(preTravelerFileList.get(j).get("fileName")).equals(afterFileName)){
    					if(afterFileType <= fileNameArray.length){
    						saveSingleOrderLog("", orderIdLong, content(fileNameArray[afterFileType], getString(preTravelerFileList.get(j).get("fileName")), afterFileName), 2, extend);
    					}
    				}
    				// 便于之后统计被删除的文件
    				preTravelerFileList.remove(j);
    			}
    		}
    		if(flag){
    			// 新添加的文件
    			if(afterFileType <= fileNameArray.length){
    				saveSingleOrderLog("", orderIdLong, fileNameArray[afterFileType] + "(上传文件): " + afterFileName, 1, extend);
    			}
    		}
    	}
    	// 剩余preTravelerFileList中的是被用户删除了的
    	if(preTravelerFileList.size() > 0){
    		for (int i = 0; i < preTravelerFileList.size(); i++) {
    			Integer fileType = Integer.parseInt(preTravelerFileList.get(i).get("fileType").toString());
        		String fileName = getString(preTravelerFileList.get(i).get("fileName"));
    			// 删除的文件
        		if(fileType <= fileNameArray.length){
        			saveSingleOrderLog("", orderIdLong, fileNameArray[fileType] + "(删除文件): " + fileName, 3, extend);
        		}
			}
    	}
    	
    	// 申请办签
    	if(visasArr!=null && visasArr.size()>0 && preVisalist!=null && preVisalist.size()>0){
    		for(int i = 0; i < visasArr.size(); i++){
    			JSONObject afterJsonVisa = visasArr.getJSONObject(i);
    			if(StringUtils.isBlank(afterJsonVisa.getString("orgVisaId"))){  // 新添加的
    				saveSingleOrderLog("", orderIdLong, "  新添加了申请办签国家： " + CountryUtils.getCountryName(Long.parseLong(afterJsonVisa.getString("applyCountryId"))), 1, extend);
    			}else{
    				for (int j = 0; j < preVisalist.size(); j++) {  // 修改了的
    					if(afterJsonVisa.getString("orgVisaId").equals(getString(preVisalist.get(j).get("id")))){
    						String afterCountryName = CountryUtils.getCountryName(Long.parseLong(afterJsonVisa.getString("applyCountryId")));
    						if(!getString(preVisalist.get(j).get("visa_country_name")).equals(getString(afterCountryName))){
    							saveSingleOrderLog("visa_country_name", orderIdLong, content("申请国家", getString(preVisalist.get(j).get("visa_country_name")), afterCountryName), 2, extend);
    						}
    						String preAreaName = getString(preVisalist.get(j).get("visa_consulardistric_name"));
    						String afterAreaName = getVisaTypeOrAreaName(getString(afterJsonVisa.getString("manorId")), "from_area");
    						if(!preAreaName.equals(afterAreaName)){
    							saveSingleOrderLog("visa_consulardistric_name", orderIdLong, content("领区", preAreaName, afterAreaName), 2, extend);
    						}
    						String preVisaType = getString(preVisalist.get(j).get("visa_type_name"));
    						String afterVisaType = getVisaTypeOrAreaName(getString(afterJsonVisa.getString("visaTypeId")), "new_visa_type");
    						if(!preVisaType.equals(afterVisaType)){
    							saveSingleOrderLog("visa_type_name", orderIdLong, content("签证类型", preVisaType, afterVisaType), 2, extend);
    						}
    						if(!getString(preVisalist.get(j).get("about_signing_time")).equals(getString(afterJsonVisa.getString("contractDate")))){
    							saveSingleOrderLog("contractDate", orderIdLong, content("预计签约时间", getString(preVisalist.get(j).get("about_signing_time")), getString(afterJsonVisa.getString("contractDate"))), 2, extend);
    						}
    						preVisalist.remove(j);
    					}
					}
    			}
    		}
    		if(preVisalist.size() > 0){
    			for (int i = 0; i <  preVisalist.size(); i++) {
    				// 删除了的
    				saveSingleOrderLog("", orderIdLong, "   删除了申请办签国家："+preVisalist.get(i).get("visa_country_name"), 3, extend);
				}
    		}
    	}
    	if(visasArr!=null && visasArr.size()<0 && preVisalist!=null && preVisalist.size()>0){  // 删除
    		for (int i = 0; i < preVisalist.size(); i++) {
    			saveSingleOrderLog("", orderIdLong, "   删除了申请办签国家："+preVisalist.get(i).get("visa_country_name"), 3, extend);
			}
    	}
    	if(visasArr!=null && visasArr.size()>0 && preVisalist!=null && preVisalist.size()<0){  // 添加
    		for (int i = 0; i < visasArr.size(); i++) {
    			saveSingleOrderLog("", orderIdLong, "  新添加了申请办签国家： " + CountryUtils.getCountryName(Long.parseLong(visasArr.getJSONObject(i).getString("applyCountryId"))), 1, extend);
			}
    	}
    	
    	if(!getString(afterTraveler.getRemark()).equals(getString(preTraveler.getRemark()))) {
    		saveSingleOrderLog("remark", orderIdLong, content("备注", getString(preTraveler.getRemark()), getString(afterTraveler.getRemark())), 2, extend);
    	}
    }
    
    /**
     * 获取办签领域名称或签证类别名称
     * @param type  领域  from_area  签证类别  new_visa_type
     * @param value
     * @return
     */
    public String getVisaTypeOrAreaName(String value, String type){
    	if(StringUtils.isBlank(type) || StringUtils.isBlank(value)){
    		return "";
    	}
    	List<Dict> dictList = DictUtils.getDictByType(type);
    	for (Dict dict : dictList) {
			if(dict.getValue().equals(value)){
				return dict.getLabel();
			}
		}
    	return "";
    }
    
//    public String getAreaName(String index){
//    	if("1".equals(index)){
//    		return "北京";
//    	}else if("3".equals(index)){
//    		return "上海";
//    	}else if("4".equals(index)){
//    		return "广州";
//    	}else{
//    		return "";
//    	}
//    }
//    
//    public String getVisaType(String index){
//    	if("1".equals(index)){
//    		return "个签";
//    	}else if("4".equals(index)){
//    		return "照会";
//    	}else if("6".equals(index)){
//    		return "照会+邀请";
//    	}else if("7".equals(index)){
//    		return "探亲";
//    	}else if("8".equals(index)){
//    		return "续签";
//    	}else if("9".equals(index)){
//    		return "个签+探亲+邀请";
//    	}else if("11".equals(index)){
//    		return "商务";
//    	}else{
//    		return "";
//    	}
//    }

    private String getDocNames(String id) {
    	if(StringUtils.isNotBlank(id)){
    		StringBuffer names = new StringBuffer();
    		String[] ids = id.split(",");
    		for (int i = 0; i < ids.length; i++) {
    			DocInfo docInfo = docInfoService.getDocInfo(Long.parseLong(ids[i]));
    			names.append(docInfo.getDocName()).append(" , ");
    		}
    		return names.toString().substring(0, names.length() - 2);
    	}
    	return "";
    }

    /**
    * 保存单团类订单修改日志
    * @param fieldName
    * @param orderId
    * @param content
    * @param opType
    */
    public void saveSingleOrderLog(String fieldName, Long orderId, String content, Integer opType, String extend1) {
    	User user = UserUtils.getUser();
    	LogOrder logOrder = new LogOrder();
    	logOrder.setUuid(UuidUtils.generUuid());
    	logOrder.setOrderType(1);
    	logOrder.setBussinessType(1);
    	logOrder.setBussinessId(orderId);
    	logOrder.setOpType(opType);
    	logOrder.setFieldName(fieldName);
    	logOrder.setContent(content);
    	logOrder.setCreateBy(user.getId());
    	logOrder.setCreateDate(new Date());
    	logOrder.setCompanyId(user.getCompany().getId());
    	logOrder.setExpand1(extend1);
    	logOrderDao.saveObj(logOrder);
    }
    
    /**
     * 单团类订单修改记录列表
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getLogSingleGroupOrderList(Long orderId) {
        String sql = "SELECT " +
                "u.`name` create_by, " +
                "lo.create_date, " +
                "GROUP_CONCAT(lo.content SEPARATOR ' ') content, " +
                "lo.expand_1 expend " +
                "FROM " +
                " log_order lo LEFT JOIN sys_user u ON lo.create_by = u.id " +
                "WHERE " +
                " lo.order_type = 1 " +
                "AND lo.bussiness_type = 1 " +
                "AND lo.bussiness_id = ? " +
                "GROUP BY " +
                " lo.expand_1 " +
        		" ORDER BY " +
        		" lo.create_date DESC ";
        List<Map<String, Object>> list = logOrderDao.findBySql(sql, Map.class, orderId);
        return list;
    }

    public List<Map<String, Object>> getLogSingleGroupOrderListByExpand(Long orderId, String expend) {
        String sql = "SELECT " +
                " lo.content " +
                "FROM " +
                "log_order lo " +
                "WHERE " +
                " lo.order_type = 1 " +
                "AND lo.bussiness_type = 1 " +
                "AND lo.bussiness_id = ? " +
                "AND lo.expand_1 = ?";
        List<Map<String, Object>> list = logOrderDao.findBySql(sql, Map.class, orderId, expend);
        return list;
    }
}
