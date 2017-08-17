package com.trekiz.admin.modules.airticketorder.utils;


import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunpeng.zhang on 2016/1/13.
 */
public class AirticketOrderUtil {
    private static MoneyAmountDao moneyAmountDao = SpringContextHolder.getBean(MoneyAmountDao.class);

    /**
     * 组装订单联系人数据,将Json数组转换为List. 由于统一变更联系人模块，使用了统一的name
     */
    public static List<OrderContacts> getContactsListNew(String orderContactsStr) throws JSONException {
        List<OrderContacts> contactsList = new ArrayList<>();
        JSONArray orderContactsJsonArray = new JSONArray(orderContactsStr);
        int len = orderContactsJsonArray.length();
        if(len > 0){
            for(int i=0;i<len;i++){
                JSONObject orderContacts = orderContactsJsonArray.getJSONObject(i);
                //处理联系人
                OrderContacts contacts = new OrderContacts();
                {
                    //渠道id
                    Object agentId = orderContacts.get("agentId");
                    if(agentId!=null&& StringUtils.isNotBlank(agentId.toString())){
                        contacts.setAgentId(Long.parseLong(agentId.toString()));
                    }
                    //联系人名称
                    Object contactsName = orderContacts.get("contactsName");
                    if(contactsName!=null&& StringUtils.isNotBlank(contactsName.toString())){
                        contacts.setContactsName(contactsName.toString());
                    }
                    //联系人电话
                    Object contactsTel = orderContacts.get("contactsTel");
                    if(contactsTel!=null&&StringUtils.isNotBlank(contactsTel.toString())){
                        contacts.setContactsTel(contactsTel.toString());
                    }
                    //联系人固定电话
                    Object contactsTixedTel = orderContacts.get("contactsTixedTel");
                    if(contactsTixedTel!=null&&StringUtils.isNotBlank(contactsTixedTel.toString())){
                        contacts.setContactsTixedTel(contactsTixedTel.toString());
                    }
                    //联系人地址
                    Object contactsAddress = orderContacts.get("contactsAddress");
                    if(contactsAddress!=null&&StringUtils.isNotBlank(contactsAddress.toString())){
                        contacts.setContactsAddress(contactsAddress.toString());
                    }
                    //联系人传真
                    Object contactsFax = orderContacts.get("contactsFax");
                    if(contactsFax!=null&&StringUtils.isNotBlank(contactsFax.toString())){
                        contacts.setContactsFax(contactsFax.toString());
                    }
                    //联系人QQ
                    Object contactsQQ = orderContacts.get("contactsQQ");
                    if(contactsQQ!=null&&StringUtils.isNotBlank(contactsQQ.toString())){
                        contacts.setContactsQQ(contactsQQ.toString());
                    }
                    //联系人邮箱
                    Object contactsEmail = orderContacts.get("contactsEmail");
                    if(contactsEmail!=null&&StringUtils.isNotBlank(contactsEmail.toString())){
                        contacts.setContactsEmail(contactsEmail.toString());
                    }
                    //备注
                    Object remark = orderContacts.get("remark");
                    if(remark!=null&&StringUtils.isNotBlank(remark.toString())){
                        contacts.setRemark(remark.toString());
                    }
                }
                contactsList.add(contacts);
            }
        }
        return contactsList;

    }

    /**
     * 组织订单总结算价,将Json数组转换为List
     */
    public static List<MoneyAmount> getOrderTotalClearPriceList(String orderTotalClearPriceStr) throws JSONException {
        List<MoneyAmount> orderTotalClearPriceList = new ArrayList<>();
        JSONArray orderTotalClearPriceJsonArray = new JSONArray(orderTotalClearPriceStr);
        int len = orderTotalClearPriceJsonArray.length();
        if(len > 0) {
            for(int i = 0; i < len; i++) {
                JSONObject orderTotalClearPrice = orderTotalClearPriceJsonArray.getJSONObject(i);
                MoneyAmount moneyAmount = new MoneyAmount();
                //币种
                Object currencyId = orderTotalClearPrice.get("currencyId");
                if(currencyId!=null&& StringUtils.isNotBlank(currencyId.toString())){
                    moneyAmount.setCurrencyId(Integer.parseInt(currencyId.toString()));
                }
                //金额
                Object amount = orderTotalClearPrice.get("je");
                if(amount!=null&& StringUtils.isNotBlank(amount.toString())){
                    moneyAmount.setAmount(new BigDecimal(amount.toString()));
                }
                orderTotalClearPriceList.add(moneyAmount);
            }

        }
        Map<Integer, BigDecimal> orderTotalClearPriceMap = new HashMap<>();
        if(orderTotalClearPriceList.size() > 0) {
            for(MoneyAmount moneyAmount : orderTotalClearPriceList) {
                Integer currencyId = moneyAmount.getCurrencyId();
                BigDecimal amount = moneyAmount.getAmount();
                boolean isExist = orderTotalClearPriceMap.containsKey(currencyId);
                if(isExist) {
                    BigDecimal tempAmount = orderTotalClearPriceMap.get(currencyId);
                    orderTotalClearPriceMap.put(currencyId, amount.add(tempAmount));
                } else {
                    orderTotalClearPriceMap.put(currencyId, amount);
                }
            }
        }
        orderTotalClearPriceList.clear();
        if(orderTotalClearPriceMap.size() > 0) {
            for(Map.Entry<Integer, BigDecimal> entry : orderTotalClearPriceMap.entrySet()) {
                MoneyAmount moneyAmount = new MoneyAmount();
                moneyAmount.setCurrencyId(entry.getKey());
                moneyAmount.setAmount(entry.getValue());
                orderTotalClearPriceList.add(moneyAmount);
            }
        }

        return orderTotalClearPriceList;
    }

}
