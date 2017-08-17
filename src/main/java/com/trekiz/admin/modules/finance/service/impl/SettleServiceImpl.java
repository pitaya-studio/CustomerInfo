package com.trekiz.admin.modules.finance.service.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordVO;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.finance.FinanceUtils;
import com.trekiz.admin.modules.finance.entity.Settle;
import com.trekiz.admin.modules.finance.entity.SettleIn;
import com.trekiz.admin.modules.finance.entity.SettleOrder;
import com.trekiz.admin.modules.finance.entity.SettleOther;
import com.trekiz.admin.modules.finance.entity.SettleOut;
import com.trekiz.admin.modules.finance.repository.ISettleDao;
import com.trekiz.admin.modules.finance.service.ISettleService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，结算单的Service实现类
 * @author shijun.liu
 * @date 2016年05月05日
 */
@Service
@Transactional(readOnly = true)
public class SettleServiceImpl implements ISettleService{

    @Autowired
    private ISettleDao settleDao;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CostManageService costManageService;

    /**
     * 获取结算单的Map类型的数据。
     */
    public Map<String,Object> getSettle(Integer orderType,Long groupId,String groupUuid){
        Settle settle = getSettleObj(orderType,groupId,groupUuid);
        Map<String,Object> resultMap = castSettle2Map(settle);
        return resultMap;
    }

    /**
     * 获取Settle对象的类型的结算单数据
     */
    public Settle getSettleObj(Integer orderType,Long groupId,String groupUuid){
        String groupIdUuid = ForeCastServiceImpl.getGroupIdUuid(orderType,groupId,groupUuid);
        Settle settle = settleDao.findSettle(orderType,groupIdUuid);
        return settle;
    }

    /**
     * 获取单纯的结算单中的数据，不包含子表的数据。
     */
    @Override
    public Settle getSimpleSettleObj(Integer orderType, String groupIdUuid) {
        return settleDao.findSimpleSettle(orderType,groupIdUuid);
    }

    /**
     * 获取单纯的结算单中的数据，不包含子表的数据。重载方法。
     */
    @Override
    public Settle getSimpleSettleObj(Integer orderType,Long groupId,String groupUuid) {
        String groupIdUuid = ForeCastServiceImpl.getGroupIdUuid(orderType,groupId,groupUuid);
        return settleDao.findSimpleSettle(orderType,groupIdUuid);
    }


    /**
     * 保存传入的Map类型的结算单数据到数据库。
     */
    @Transactional(readOnly = false,rollbackFor ={Exception.class})
    public void saveSettleByMap(Integer orderType,String groupIdUuid,Map<String,Object> resultMap){
        Settle settle = handleHistory(orderType,groupIdUuid);
        settle = castMap2Settle(settle,resultMap);
        if (settle.getId() == null){//表示是新建的记录,不是持久化对象,需要设置一些属性
            settle.setUuid(UuidUtils.generUuid());
            settle.setOrderType(orderType);
            settle.setGroupIdUuid(groupIdUuid);
        }
        settleDao.saveOrUpdateSettle(settle);
    }

    @Transactional(readOnly = false,rollbackFor ={Exception.class})
    public void saveSettleByMap(Integer orderType,Long groupId,String groupUuid,Map<String,Object> resultMap){
        String groupIdUuid = ForeCastServiceImpl.getGroupIdUuid(orderType,groupId,groupUuid);
        saveSettleByMap(orderType,groupIdUuid,resultMap);
    }

    /**
     * 处理原有保存的结算单数据（查询数据库，返回对应的Settle对象，如果没有找到返回null，
     * 如果找到则删除子数据信息,不删除主表Settle记录，返回保留基本信息Settle对象）
     */
    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    private Settle handleHistory(Integer orderType,String groupIdUuid){
        Settle settle = settleDao.findSimpleSettle(orderType,groupIdUuid);
        if (null == settle)
            return null;
        settleDao.deleteSub("SettleOrder",settle.getUuid());
        settleDao.deleteSub("SettleIn",settle.getUuid());
        settleDao.deleteSub("SettleOut",settle.getUuid());
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	 settleDao.deleteSub("SettleOther", settle.getUuid());
        }
        return settle;
    }

    /**
     * 解析Settle对象中的数据到Map中。
     * @param settle
     * @return Map类型的结算单数据。
     */
    public Map<String,Object> castSettle2Map(Settle settle){
        if (null == settle) return null;
        //把传入的Settle对象转换成Map.用于页面显示,并且进行字段名称的映射。
        Map<String,Object> resultMap = new HashMap<>(32);
        resultMap.put("createBy",settle.getOperator());
        resultMap.put("createByLeader",settle.getOperatorManagers());
        resultMap.put("createByLeaderFull",settle.getOperatorManagersFull());
        resultMap.put("salers",settle.getSalers());
        resultMap.put("productName",settle.getProductName());
        resultMap.put("groupCode",settle.getGroupCode());
        resultMap.put("orderPersonNum",settle.getPersonNum());
        resultMap.put("personDay",settle.getPersonDay());
        resultMap.put("groupDate",settle.getOpenCloseDate());
        resultMap.put("activityDuration",settle.getActivityDuration());
        resultMap.put("grouplead",settle.getGrouplead());
        resultMap.put("supplier",settle.getSupplier());
        resultMap.put("invoiceMoney",settle.getInvoiceMoney());
        resultMap.put("profitAfterTax",settle.getProfitAfterTax());
        resultMap.put("remark", StringUtils.isNotBlank(settle.getRemark())? settle.getRemark():"");

        resultMap.put("totalMoneySum",settle.getTotalMoneySum());
        resultMap.put("backMoneySum",settle.getBackMoneySum());
        resultMap.put("accountedMoneySum",settle.getAccountedMoneySum());
        resultMap.put("notAccountedMoneySum",settle.getNotAccountedMoneySum());
        resultMap.put("orderPersonNumSum",settle.getPersonNum());

        resultMap.put("expectedInMoneySum",settle.getActualInMoneySum());
        resultMap.put("expectedOutMoneySum",settle.getActualOutMoneySum());

        resultMap.put("realMoneySum",settle.getRealMoneySum());
        resultMap.put("outMoneySum",settle.getOutMoneySum());
        resultMap.put("profitSum",settle.getProfitSum());
        resultMap.put("profitRate",settle.getProfitRate());

        /* 对骡子假期结算单的新锁定，采用新的值和页面。而在改动之前已经锁定的旧的结算单，则展示以前版本的页面和值。
         添加了版本号字段来区分，利于以后的修改。*/
        Integer version = settle.getVersion();
        resultMap.put("version",version);
        //骡子假期 团队退款备注
        resultMap.put("groupRefundSum", settle.getGroupRefundSum());
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid) && version == 2){
            Integer orderPersonNum = Integer.parseInt(settle.getPersonNum());
            resultMap.put("orderPersonNumSum",orderPersonNum); // 需要Integer类型的
            resultMap.put("groupOpenDate",settle.getPersonDay()); // 使用PersonDay存开团日期
            resultMap.put("groupCloseDate",settle.getOpenCloseDate()); // 原有的日期字段，存放截团日期
            resultMap.put("orderType",settle.getActivityDuration()); // 使用天数字段，存放团队类型
            resultMap.put("settlementAdultPrice",settle.getInvoiceMoney()); // 使用发票税票字段，存放同行价(不降价价格)
            resultMap.put("orderMoneySum",settle.getBackMoneySum()); // 使用退款总计字段，存放订单总计(不包括返佣)
            resultMap.put("recordMoneySum",settle.getProfitAfterTax()); // 使用ProfitAfterTax字段，保存结算支出总计
            resultMap.put("otherSum", settle.getOtherSum());
            castSettleIncome2MapForLZJQ(settle,resultMap);
            //其他收入收款
            castSettleOther2MapForLZJQ(settle,resultMap);
        }else {
            castSettleIncome2Map(settle,resultMap);
        }
        castSettleRecordIn2Map(settle,resultMap);
        castSettleRecordOut2Map(settle,resultMap);

        return resultMap;
    }

    /**
     * 结算单订单转化成map数据。
     * @param settle
     * @param resultMap
     */
    private void castSettleIncome2Map(Settle settle,Map<String,Object> resultMap){
        List<SettleOrder> actualIncome = settle.getActualIncome();
        List<Map<String,Object>> orderList = new ArrayList<>();
        for (SettleOrder order : actualIncome) {
            Map<String,Object> map = new HashMap<>(8);
            map.put("saler",order.getSaler());
            map.put("agentName",order.getAgentName());
            map.put("orderPersonNum",order.getOrderPersonNum());
            map.put("totalMoney",order.getTotalMoney());
            map.put("refundprice",order.getBackMoney());
            map.put("accountedMoney",order.getAccountedMoney());
            map.put("notAccountedMoney",order.getNotAccountedMoney());
            orderList.add(map);
        }
        resultMap.put("expectIncome",orderList);
    }

    /**
     * 骡子假期的结算单订单转化，不再区分成人，儿童，特殊人群，统一成一条记录，保存的时候已经区分保存了。
     * @param settle
     * @param resultMap
     */
    private void castSettleIncome2MapForLZJQ(Settle settle,Map<String,Object> resultMap){
        List<SettleOrder> actualIncome = settle.getActualIncome();
        List<Map<String,Object>> orderList = new ArrayList<>();
        for (SettleOrder order : actualIncome) {
            Map<String,Object> map = new HashMap<>();
            map.put("agentName",order.getAgentName());
            map.put("adultPrice",order.getBackMoney()); // BackMoney存放单价price
            map.put("adultNum",order.getOrderPersonNum()); // 人数存放对应人数
            map.put("adultMoney",order.getTotalMoney()); // totalMoney存放对应的总金额
            map.put("totalMoney",order.getTotalMoney()); // 其他收入收款需要使用totalMoney名称
            map.put("supplyName",order.getAccountedMoney()); // AccountedMoney存放其他收入收款需要使用supplyName名称
            map.put("adultRemark",order.getNotAccountedMoney()); // 备注
            map.put("saler",order.getSaler());
            map.put("adultPersonCount", order.getPersonCount());
            map.put("adultZGPersonCount", order.getzGPersonCount());
            orderList.add(map);
        }
        resultMap.put("expectIncome",orderList);
    }

    private void castSettleRecordIn2Map(Settle settle,Map<String,Object> resultMap){
        List<SettleIn> actualInList = settle.getActualInList();
        List<CostRecord> inList = new ArrayList<>();
        for (SettleIn settleIn : actualInList) {
            CostRecord record = new CostRecord();
            record.setName(settleIn.getCostName());
            record.setSupplyName(settleIn.getAgentName());
            record.setFormatPrice(settleIn.getPrice());
            record.setQuantity(settleIn.getQuantity());
            record.setFormatPriceAfter(settleIn.getAmount());
            record.setComment(settleIn.getComment());
            inList.add(record);
        }
        resultMap.put("actualInList",inList);
    }

    private void castSettleRecordOut2Map(Settle settle,Map<String,Object> resultMap){
        List<SettleOut> actualOutList = settle.getActualOutList();
        List<CostRecord> outList = new ArrayList<>();
        for (SettleOut settleOut : actualOutList) {
            CostRecord record = new CostRecord();
            record.setSupplyName(settleOut.getAgentName());
            record.setName(settleOut.getCurrencyName());
            record.setRate(new BigDecimal(settleOut.getRate().replace(",","")));
            record.setFormatPrice(settleOut.getPrice());
            record.setFormatPriceAfter(settleOut.getAmount());
//            record.setPayStatus(settleOut.getPayStatus());
            outList.add(record);
        }
        resultMap.put("actualOutList",outList);
    }

    /**
     * 将Map类型的结算单数据转换成Settle对象类型的结算单数据。
     * @param resultMap
     * @param settle 从数据库中查询出来的对象
     * @return
     */
    public Settle castMap2Settle(Settle settle,Map<String,Object> resultMap){
        if (null == resultMap)
            return null;
        if (null == settle){
            settle = new Settle();
            settle.setCreateBy(UserUtils.getUser().getId());//创建人，创建日期
            settle.setCreateDate(new Date());
        }

        settle.setUpdateBy(UserUtils.getUser().getId()); //更新人，更新日期
        settle.setUpdateDate(new Date());

        //把map数据转换成Settle对象。
        //团期的基本信息
        String createBy = FinanceUtils.blankReturnEmpty(resultMap.get("createBy"));
        settle.setOperator(createBy);//操作人
        String createByLeader = FinanceUtils.blankReturnEmpty(resultMap.get("createByLeader"));
        settle.setOperatorManagers(createByLeader);//操作负责人简写
        String createByLeaderFull = FinanceUtils.blankReturnEmpty(resultMap.get("createByLeaderFull"));
        settle.setOperatorManagersFull(createByLeaderFull);//操作负责人全写
        String salers = FinanceUtils.blankReturnEmpty(resultMap.get("salers"));
        settle.setSalers(salers);//销售

        String productName = FinanceUtils.blankReturnEmpty(resultMap.get("productName"));
        settle.setProductName(productName);//线路
        String groupCode = FinanceUtils.blankReturnEmpty(resultMap.get("groupCode"));
        settle.setGroupCode(groupCode);//团号
        String orderPersonNum = FinanceUtils.blankReturnEmpty(resultMap.get("orderPersonNumSum"));
        settle.setPersonNum(orderPersonNum);//人数
        String personDay = FinanceUtils.blankReturnEmpty(resultMap.get("personDay"));
        settle.setPersonDay(personDay);//人天数

        String groupDate = FinanceUtils.blankReturnEmpty(resultMap.get("groupDate"));
        settle.setOpenCloseDate(groupDate);//日期(团期的开始时间拼接结束时间的字符串)
        String activityDuration = FinanceUtils.blankReturnEmpty(resultMap.get("activityDuration"));
        settle.setActivityDuration(activityDuration);//天数

        String grouplead = FinanceUtils.blankReturnEmpty(resultMap.get("grouplead"));
        settle.setGrouplead(grouplead);//领队
        String supplier = FinanceUtils.blankReturnEmpty(resultMap.get("supplier"));
        settle.setSupplier(supplier);//地接社

        //懿洋假期,发票税款，及减税后利润
        String invoiceMoney = FinanceUtils.blankReturnEmpty(resultMap.get("invoiceMoney"));
        settle.setInvoiceMoney(invoiceMoney);
        String profitAfterTax = FinanceUtils.blankReturnEmpty(resultMap.get("profitAfterTax"));
        settle.setProfitAfterTax(profitAfterTax);

        //收款统计信息
        String totalMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("totalMoneySum"));
        settle.setTotalMoneySum(totalMoneySum);
        String backMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("backMoneySum"));
        settle.setBackMoneySum(backMoneySum);
        String accountedMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("accountedMoneySum"));
        settle.setAccountedMoneySum(accountedMoneySum);
        String notAccountedMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("notAccountedMoneySum"));
        settle.setNotAccountedMoneySum(notAccountedMoneySum);

        //境内付款统计信息
        String expectedInMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("expectedInMoneySum"));
        settle.setActualInMoneySum(expectedInMoneySum);//规范名称，使用ActualInMoneySum作为名称，map实际存入的是expectedInMoneySum。

        //境外付款统计信息
        String expectedOutMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("expectedOutMoneySum"));
        settle.setActualOutMoneySum(expectedOutMoneySum);//为了统一名称，进行了字段映射。

        // 对骡子假期进行了单独处理，把订单，境内，境外的for循环提取为方法。
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
            String groupOpenDate = FinanceUtils.blankReturnEmpty(resultMap.get("groupOpenDate"));
            settle.setPersonDay(groupOpenDate); // 骡子假期没有PersonDay字段，使用PersonDay存开团日期

            String groupCloseDate = FinanceUtils.blankReturnEmpty(resultMap.get("groupCloseDate"));
            settle.setOpenCloseDate(groupCloseDate); // 原有的日期字段，存放截团日期

            String orderType = FinanceUtils.blankReturnEmpty(resultMap.get("orderType"));
            settle.setActivityDuration(orderType); // 使用天数字段，存放团队类型

            String settlementAdultPrice = FinanceUtils.blankReturnEmpty(resultMap.get("settlementAdultPrice"));
            settle.setInvoiceMoney(settlementAdultPrice); // 使用发票税票字段，存放同行价(不降价价格)

            String orderMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("orderMoneySum"));
            settle.setBackMoneySum(orderMoneySum); // 使用退款总计字段，存放订单总计(不包括返佣)

            String recordMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("recordMoneySum"));
            settle.setProfitAfterTax(recordMoneySum); // 使用ProfitAfterTax字段，保存结算支出总计
            
            settle.setVersion(2); // 骡子假期较以前的版本来说，这次新的改动版本为 2
            
            if(resultMap.get("groupRefundSum") != null){
            	settle.setGroupRefundSum(resultMap.get("groupRefundSum").toString());
            }
            settle.setOtherSum(resultMap.get("otherSum").toString());
            castMap2SettleIncomeForLZJQ(settle,resultMap);
            castMap2SettleRecordInForLZJQ(settle,resultMap);
            castMap2SettleOtherRecordListForLZJQ(settle,resultMap);
            
        }else {
            castMap2SettleIncome(settle,resultMap); //收款详细信息
            castMap2SettleRecordIn(settle,resultMap); //境内付款详细信息
            castMap2SettleRecordOut(settle,resultMap); //境外付款详细信息
            settle.setVersion(1);
        }

        //下方统计信息
        String realMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("realMoneySum"));
        String outMoneySum = FinanceUtils.blankReturnEmpty(resultMap.get("outMoneySum"));
        String profitSum = FinanceUtils.blankReturnEmpty(resultMap.get("profitSum"));
        String profitRate = FinanceUtils.blankReturnEmpty(resultMap.get("profitRate"));
        settle.setRealMoneySum(realMoneySum);
        settle.setOutMoneySum(outMoneySum);
        settle.setProfitSum(profitSum);
        settle.setProfitRate(profitRate);

        return settle;//返回填充完数据的settle对象。
    }

    /**
     * 保存结算单的订单收入信息。把map里面相应的数据转换成Settle里面的数据。
     * @param settle
     * @param resultMap
     */
    private void  castMap2SettleIncome(Settle settle,Map<String,Object> resultMap){
        List<Map<String,Object>> expectIncome = (List<Map<String,Object>>)resultMap.get("expectIncome");
        if (null != expectIncome) {
            List<SettleOrder> orderList = new ArrayList<>();
            for (Map<String, Object> map : expectIncome) {
                SettleOrder order = new SettleOrder();
                order.setSaler(FinanceUtils.blankReturnEmpty(map.get("saler")));
                order.setAgentName(FinanceUtils.blankReturnEmpty(map.get("agentName")));
                order.setOrderPersonNum(Integer.parseInt(map.get("orderPersonNum").toString()));
                order.setTotalMoney(FinanceUtils.blankReturnEmpty(map.get("totalMoney")));
                order.setBackMoney(FinanceUtils.blankReturnEmpty(map.get("refundprice")));//为统一起见，使用backMoney作为退款。
                order.setAccountedMoney(FinanceUtils.blankReturnEmpty(map.get("accountedMoney")));
                order.setNotAccountedMoney(FinanceUtils.blankReturnEmpty(map.get("notAccountedMoney")));
                orderList.add(order);
            }
            settle.setActualIncome(orderList);
        }
    }

    /**
     * 保存骡子假期的订单收款详情Item。由于骡子假期的结算单字段和原有的不同，这里对字段名称进行了映射，表中字段名字和实际保存的
     * 值会不一致，取出时需要再进行转化。
     * @param settle
     * @param resultMap
     * @author yudong.xu 2016.11.11
     */
    private void  castMap2SettleIncomeForLZJQ(Settle settle,Map<String,Object> resultMap){
        List<Map<String,Object>> expectIncome = (List<Map<String,Object>>)resultMap.get("expectIncome");
        if (null != expectIncome) {
            List<SettleOrder> orderList = new ArrayList<>();
            // 按照页面上字段的顺序排序
            String[] adultKeys = {"agentName","adultPrice","adultNum","adultMoney","saler","adultRemark","adultPersonCount","adultZGPersonCount"};
            String[] childKeys = {"agentName","childrenPrice","childrenNum","childrenMoney","saler","childrenRemark","childrenPersonCount","childrenZGPersonCount"};
            String[] specialKeys = {"agentName","specialPrice","specialNum","specialMoney","saler","specialRemark","specialPersonCount","specialZGPersonCount"};
            for (Map<String, Object> map : expectIncome) {
                if ("其他".equals(map.get("agentName"))){
                    SettleOrder order = new SettleOrder();
                    order.setAgentName(FinanceUtils.blankReturnEmpty(map.get("agentName")));
                    order.setTotalMoney(FinanceUtils.blankReturnEmpty(map.get("totalMoney")));
                    order.setSaler(FinanceUtils.blankReturnEmpty(map.get("saler")));
                    // 使用这个字段存放其他收入的supplyName
                    order.setAccountedMoney(FinanceUtils.blankReturnEmpty(map.get("supplyName")));
                    orderList.add(order);
                    continue;
                }
                settleIncomeSetterForLZJQ(orderList, map, adultKeys); // 成人
                settleIncomeSetterForLZJQ(orderList, map, childKeys); // 儿童
                settleIncomeSetterForLZJQ(orderList, map, specialKeys); // 特殊人群
            }
            settle.setActualIncome(orderList);
        }
    }

    /**
     * 骡子假期结算单锁定时，用于设置订单部分的数据，供上面的方法进行调用。
     * @param orderList 外部传入的结果List
     * @param map   存放数据的map
     * @param keys 存放在map里面的需要保存的字段的名称。
     * @author yudong.xu 2016.11.11
     */
    private void  settleIncomeSetterForLZJQ(List<SettleOrder> orderList,Map<String, Object> map,String[] keys){
        String numStr = FinanceUtils.blankReturnEmpty(map.get(keys[2]));
        if (StringUtils.isBlank(numStr)){
            return;
        }
        Integer num = Integer.parseInt(numStr); // 成人,儿童,特殊人群对应的人数
        if (num > 0){
            SettleOrder order = new SettleOrder();
            order.setAgentName(FinanceUtils.blankReturnEmpty(map.get(keys[0])));
            // 使用BackMoney存放单价price
            order.setBackMoney(FinanceUtils.blankReturnEmpty(map.get(keys[1])));
            // 使用人数存放对应人数
            order.setOrderPersonNum(num);
            // 使用totalMoney存放对应的总金额
            order.setTotalMoney(FinanceUtils.blankReturnEmpty(map.get(keys[3])));
            order.setSaler(FinanceUtils.blankReturnEmpty(map.get(keys[4])));
            // 使用notAccountedMoney存放remark备注
            order.setNotAccountedMoney(FinanceUtils.blankReturnEmpty(map.get(keys[5])));
            orderList.add(order);
        }else{
        	if(Integer.parseInt(map.get(keys[7]).toString()) != 0 || Integer.parseInt(map.get(keys[6]).toString()) != 0){
        		SettleOrder order = new SettleOrder();
                order.setAgentName(FinanceUtils.blankReturnEmpty(map.get(keys[0])));
                // 使用BackMoney存放单价price
                order.setBackMoney(FinanceUtils.blankReturnEmpty(map.get(keys[1])));
                // 使用人数存放对应人数
                order.setOrderPersonNum(0);
                // 使用totalMoney存放对应的总金额
                order.setTotalMoney("0.00");
                order.setSaler(FinanceUtils.blankReturnEmpty(map.get(keys[4])));
                // 使用notAccountedMoney存放remark备注
                order.setNotAccountedMoney(FinanceUtils.blankReturnEmpty(map.get(keys[5])));
                //骡子假期转团人数
                order.setzGPersonCount(Integer.parseInt(map.get(keys[7]).toString()));
                //骡子假期退团人数
                order.setPersonCount(Integer.parseInt(map.get(keys[6]).toString()));
                orderList.add(order);
        	}
        }
    }

    /**
     * 转化map数据到结算单的境内支出。
     * @param settle
     * @param resultMap
     */
    private void  castMap2SettleRecordIn(Settle settle,Map<String,Object> resultMap){
        List<CostRecord> actualInList = (List<CostRecord>)resultMap.get("actualInList");
        if (null != actualInList){
            List<SettleIn> inList = new ArrayList<>();
            recordInSetter(inList,actualInList);
            settle.setActualInList(inList);
        }
    }

    /**
     * 结算单的境内支出设置器，把CostRecord里面的数据转换成SettleIn里面的数据。
     * @param inList
     * @param recordList
     */
    private void recordInSetter(List<SettleIn> inList,List<CostRecord> recordList){
        if (null != recordList && null != inList){
            for (CostRecord record : recordList) {
                SettleIn inModel = new SettleIn();
                inModel.setCostName(record.getName());
                inModel.setAgentName(record.getSupplyName());
                inModel.setPrice(record.getFormatPrice());
                inModel.setQuantity(record.getQuantity());
                inModel.setAmount(record.getFormatPriceAfter());
                inModel.setComment(record.getComment());
                inList.add(inModel);
            }
        }
    }

    /**
     * 骡子假期结算单中的境内境外付款统一为结算支出，新结算支出对应的列字段和原有的结算单境内支出的相同，所以境内境外的都放入到
     * 结算单的境内支出表里面进行保存。
     * @param settle
     * @param resultMap
     */
    private void  castMap2SettleRecordInForLZJQ(Settle settle,Map<String,Object> resultMap){
        List<CostRecord> actualInList = (List<CostRecord>)resultMap.get("actualInList");
        List<CostRecord> actualOutList = (List<CostRecord>)resultMap.get("actualOutList");
        if (null != actualInList || null != actualOutList){
            List<SettleIn> inList = new ArrayList<>();
            recordInSetter(inList,actualInList);
            recordInSetter(inList,actualOutList);
            settle.setActualInList(inList);
        }
    }

    /**
     * 转化map数据到结算单的境外支出。
     * @param settle
     * @param resultMap
     */
    private void  castMap2SettleRecordOut(Settle settle,Map<String,Object> resultMap){
        List<CostRecord> actualOutList = (List<CostRecord>)resultMap.get("actualOutList");
        if(null != actualOutList){
            List<SettleOut> outList = new ArrayList<>();
            for (CostRecord record : actualOutList) {
                SettleOut outModel = new SettleOut();
                outModel.setAgentName(record.getSupplyName());
                outModel.setCurrencyName(record.getName());//业务中使用了name字段来保存币种名称
                outModel.setRate(FinanceUtils.blankReturnEmpty(record.getRate()));
                outModel.setPrice(record.getFormatPrice());//对应页面上的外币字段
                outModel.setAmount(record.getFormatPriceAfter());
//                outModel.setPayStatus(record.getPayStatus());
                outList.add(outModel);
            }
            settle.setActualOutList(outList);
        }
    }
    
    @Override
    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    public void updateSettle(Settle settle) {
    	settleDao.saveOrUpdateSettle(settle);
    }
    
    @Override
    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    public void saveSettle(Settle settle) {
    	settleDao.saveOrUpdateSettle(settle);
    }

    @Override
    public Map<String, Object> getSettleMap(Integer orderType, Long groupId, String groupUuid) {
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        Map<String,Object> resultMap = null;
        CostRecordVO cRecordVO = null;
        try {
            cRecordVO = costManageService.getSettleDataInfo(groupId, orderType, groupUuid);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Object lockStatus = cRecordVO.getBaseinfo().get("lockStatus");
        if("".equals(FinanceUtils.blankReturnEmpty(lockStatus))){
            lockStatus = 0;
        }
        if ("1".equals(lockStatus.toString())){
            resultMap = this.getSettle(orderType, groupId, groupUuid);
            if (null == resultMap){
                resultMap = castCostRecordVO2Map(cRecordVO, orderType, 1);
                resultMap.put("remark", "");
                this.saveSettleByMap(orderType, groupId, groupUuid, resultMap);
            }
        }else {
            resultMap = castCostRecordVO2Map(cRecordVO, orderType, 1);
            //获取备注,仅针对拉美途
            if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
                Settle simpleSettle = this.getSimpleSettleObj(orderType, groupId, groupUuid);
                if(null != simpleSettle) {
                    resultMap.put("remark", FinanceUtils.blankReturnEmpty(simpleSettle.getRemark()));
                }else {
                    resultMap.put("remark", "");
                }
            }
        }
        resultMap.put("lockStatus", lockStatus);
        resultMap.put("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
        return resultMap;
    }

    @Override
    public Map<String, Object> castCostRecordVO2Map(CostRecordVO costRecordVO, Integer orderType, Integer type) {
        Map<String, Object> map = new HashMap<String, Object>();
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        //产品信息
        getProductData(map, costRecordVO, orderType);
        //预报单-->预计收款;结算单-->收款明细
        payDetailData(map, costRecordVO, orderType);
        //预计收款-合计
        map.put("totalMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("totalSum").toString(), 2));
        //预计退款-合计
        map.put("backMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("backSum").toString(), 2));
        //实际收款-合计
        map.put("accountedMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("accountedSum").toString(),2));
        //未收款项-合计
        map.put("notAccountedMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("notAcccountedSum").toString(),2));
        // 纯净的订单总额,未减去差额
        map.put("orderMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("orderMoneySum").toString(), 2));
        
        //骡子假期 团队退款总额
		if (Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
			if(costRecordVO.getGroupRefundSum() !=  null && costRecordVO.getGroupRefundSum() != new BigDecimal("0.00")){
				map.put("groupRefundSum", "团队退款：￥"+costRecordVO.getGroupRefundSum() );
			}else{
				map.put("groupRefundSum", "" );
			}
		}
        // 预计境内付款/境内支出
        BigDecimal inMoneySum = actualInData(map, costRecordVO, orderType);
        // 预计境外付款/境外支出
        BigDecimal outMoneySum = actualOutData(map, costRecordVO);
        // 境内外支出统计，不包含返佣
        BigDecimal recordMoneySum = inMoneySum.add(outMoneySum);
        // 返佣数据处理
        if (type == 0 && ActivityGroupService.isLMTTuanQi(orderType)){
            // 如果是拉美图的预报单，不需要进行返佣处理。针对拉美图0396需求。
        }else {
            inMoneySum = commissionData(map, costRecordVO, inMoneySum);
        }
        // 预计境内付款/境内支出 --> 合计
        map.put("expectedInMoneySum", MoneyNumberFormat.getThousandsByRegex(inMoneySum.toString(), 2));
        // 预计境外付款/境外支出 --> 合计
        map.put("expectedOutMoneySum", MoneyNumberFormat.getThousandsByRegex(outMoneySum.toString(), 2));
        // 境内外支出统计，不包含返佣
        map.put("recordMoneySum", MoneyNumberFormat.getThousandsByRegex(recordMoneySum.toString(), 2));
        // 支出合计
        BigDecimal outSumMoney = outMoneySum.add(inMoneySum);
        // 预计实际收入
        BigDecimal realMoney = new BigDecimal(map.get("totalSum").toString())
                .subtract(new BigDecimal(map.get("backSum").toString()));
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	realMoney = realMoney.add(new BigDecimal(map.get("otherSum").toString()));
        }
        if (type == 0 && ActivityGroupService.isLMTTuanQi(orderType)){
            //如果是拉美图的团期。
            String adultPriceOfLMTStr = map.get("adultPrice").toString().replace(",","");
            String preIncomeOfLMTStr = map.get("receiveMoney").toString().replace(",","");//预计收入
            String planPositionStr = map.get("planPosition").toString();//预收人数
            map.put("price", MoneyNumberFormat.getThousandsByRegex(adultPriceOfLMTStr, 2));//拉美图单价
            map.put("orderPersonNum", planPositionStr);//拉美图人数取预收人数
            //拉美图团期：预计收款=预计收入合计=(单价*预收人数)
            realMoney = new BigDecimal(preIncomeOfLMTStr);
        }
        //预计实际收入或者预计收款
        map.put("realMoneySum", MoneyNumberFormat.getThousandsByRegex(realMoney.toString(), 2));
        //0258 懿洋假期 税款=实际收入X发票税  保留两位小数  王洋   2016.3.31
        if(Context.SUPPLIER_UUID_YYJQ.equals(companyUuid)){
            BigDecimal taxRate = new BigDecimal(map.get("invoiceTax").toString()).divide(new BigDecimal(100));
            BigDecimal invoiceMoney = new BigDecimal(MoneyNumberFormat.getRoundMoney(
                    realMoney.multiply(taxRate), 2, BigDecimal.ROUND_HALF_UP));

            map.put("invoiceMoney", MoneyNumberFormat.getThousandsByRegex(invoiceMoney.toString(), 2));

            BigDecimal profitAfterTax = realMoney.subtract(outSumMoney).subtract(
                    new BigDecimal(MoneyNumberFormat.getRoundMoney(invoiceMoney, 2, BigDecimal.ROUND_HALF_UP)));
            map.put("profitAfterTax", MoneyNumberFormat.getThousandsByRegex(profitAfterTax.toString(), 2));
        }

        //预计毛利
        BigDecimal profitSum = realMoney.subtract(outSumMoney);
        //预计支出合计
        map.put("outMoneySum", MoneyNumberFormat.getThousandsByRegex(outSumMoney.toString(), 2));
        //预计毛利合计
        map.put("profitSum", MoneyNumberFormat.getThousandsByRegex(profitSum.toString(), 2));

        /**
         * 拉美图，结算单需求，需求号：256 by jinxin.gao
         * 	删除收入合计与退款合计两项；
         * 		实际收入：原取值为实际收入=收入合计—退款合计，
         * 	                                 现改为：实际收入＝实际收款；
         * 		支出合计=所有支出+退款合计
         * 		毛利：原取值为毛利=实际收入—支出合计；现改为，毛利＝实际收入-支出合计（包含退款合计）
         */
        if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && type == 1){
            //实际收入
            realMoney = new BigDecimal(map.get("accountedSum").toString());
            map.put("realMoneySum", MoneyNumberFormat.getThousandsByRegex(realMoney.toString(), 2));
            //支出合计
            outSumMoney = outMoneySum.add(inMoneySum).add(new BigDecimal(map.get("backSum").toString()));
            map.put("outMoneySum", MoneyNumberFormat.getThousandsByRegex(outSumMoney.toString(), 2));
            //毛利
            profitSum = realMoney.subtract(outSumMoney);
            map.put("profitSum", MoneyNumberFormat.getThousandsByRegex(profitSum.toString(), 2));
        }
        BigDecimal profitRate = new BigDecimal("0");
        if(Double.valueOf(realMoney.toString()) != 0){
            BigDecimal tempValue = new BigDecimal(MoneyNumberFormat.getRoundMoney(Double.valueOf(realMoney.toString()),
                    MoneyNumberFormat.POINT_TWO));
            if (tempValue.compareTo(new BigDecimal(0)) != 0){
                profitRate = profitSum.divide(tempValue, 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//毛利率
            }
        }
        String rate  = profitRate.toString();
        if(rate.toString().equals("0")){
            rate = "";
        }
        if(rate.startsWith(".")){
            rate += "0";
        }
        if(!"".equals(rate)){
            rate = MoneyNumberFormat.getFormatRate(Double.valueOf(rate.toString()),
                    MoneyNumberFormat.POINT_THREE);
            rate = rate + "%";
        }
        map.put("profitRate", rate);//预计毛利率
        //其他收入
       if(Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
    	   map.put("otherRecordList", costRecordVO.getOtherRecordList());
       }
        return map;
    }

    /**
     * 处理返佣数据
     * @param map
     * @param costRecordVO
     * @param inMoneySum
     * @author shijun.liu
     */
    @SuppressWarnings("unchecked")
    private BigDecimal commissionData(Map<String, Object> map, CostRecordVO costRecordVO, BigDecimal inMoneySum){
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        List<Map<String, Object>> commission = costRecordVO.getfYlistList();
        if(null != commission){

            ArrayList<CostRecord> actualIn;
            if(null != map.get("actualInList")){
                actualIn = (ArrayList<CostRecord>)map.get("actualInList");
            }else{
                actualIn = new ArrayList<>();
                map.put("actualInList", actualIn);//预计境内付款
            }

            for (Map<String, Object> fyMap : commission) {
                CostRecord record = new CostRecord();
                record.setName("其他");
                String supplyName = FinanceUtils.blankReturnEmpty(fyMap.get("supplyName"));
                record.setSupplyName(FreeMarkerUtil.StringFilter(supplyName));
                record.setQuantity(Integer.parseInt(fyMap.get("count").toString()));
                String formatAfterPrice = MoneyNumberFormat.getThousandsByRegex(fyMap.get("price").toString(), 2);
                //单价
                record.setFormatPrice("-");
                //金额
                record.setFormatPriceAfter(formatAfterPrice);
                if (! Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
                	inMoneySum = inMoneySum.add(new BigDecimal(fyMap.get("price").toString()));
                }
                if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
                    continue; // 546骡子假期，预报结算单从新处理，返佣条目不计入支出列表，但是返佣金额进入统计。
                }
                actualIn.add(record);
            }
        }
        return inMoneySum;
    }

    /**
     * 预报单->预计境外付款/结算单->境外支出
     * @param map
     * @param costRecordVO
     * @return
     * @author shijun.liu
     */
    private BigDecimal actualOutData(Map<String, Object> map, CostRecordVO costRecordVO){
        List<CostRecord> actualOutList = costRecordVO.getActualoutList();
        BigDecimal outMoneySum = new BigDecimal("0");
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        for (CostRecord record:actualOutList) {
            Currency currency = currencyService.findCurrency(Long.valueOf(record.getCurrencyId()));
            record.setRate(record.getRate()==null ? new BigDecimal("1") : record.getRate());
            String recordName = record.getName();
            //币种，境外付款使用CostRecord对象里面的name属性作为币种名称属性
            if(null != currency){
                record.setName(currency.getCurrencyName());
            }else{
                record.setName("");
            }
            //汇率
            record.setFormatRate(MoneyNumberFormat.getFormatRate(record.getRate().doubleValue(),MoneyNumberFormat.POINT_THREE));

            //外币
            BigDecimal unitPrice = record.getPrice() == null ? new BigDecimal("0") : record.getPrice();
            Integer count = record.getQuantity() == null ? 0 : record.getQuantity();
            BigDecimal tempUnitPrice = unitPrice.multiply(new BigDecimal(count));
            String formatUnitPrice = currency.getCurrencyMark()+MoneyNumberFormat.getThousandsByRegex(tempUnitPrice.toString(), 2);
            record.setFormatPrice(formatUnitPrice);
            // 骡子假期需求，不区分境内境外，支出样式和原先的境内支出数据一样。
            if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
                tempUnitPrice = unitPrice.multiply(record.getRate());
                formatUnitPrice = MoneyNumberFormat.getThousandsByRegex(tempUnitPrice.toString(), 2);
                record.setFormatPrice(formatUnitPrice);
                record.setName(recordName);
            }

            //金额
            BigDecimal priceAfter = record.getPriceAfter()==null ? new BigDecimal("0") : record.getPriceAfter();
            record.setFormatPriceAfter(MoneyNumberFormat.getThousandsByRegex(priceAfter.toString(),2));

            //地接社
            String supplyName = FinanceUtils.blankReturnEmpty(record.getSupplyName());
            if(Context.SUPPLIER_UUID_DYGL.equals(companyUuid) && FinanceUtils.AGENT_NAME.equals(supplyName)){
                supplyName = FinanceUtils.AGENT_NAME_WEIQIAN;
            }
            if (Context.SUPPLIER_UUID_YJXZ.equals(companyUuid) && FinanceUtils.AGENT_NAME.equals(supplyName)){
                supplyName = FinanceUtils.AGENT_NAME_ZHIKE;//直客，越谏行踪，将非签约渠道改为直客。 add by yudong.xu 2016.5.30
            }
            record.setSupplyName(FreeMarkerUtil.StringFilter(supplyName));

            outMoneySum = outMoneySum.add(priceAfter);
        }
        //骡子假期 返佣
  		if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LZJQ)){
  			List<Map<String, Object>> fyList = costRecordVO.getfYlistList();
  			List<CostRecord> list = new ArrayList<CostRecord>();
  			for(Map<String,Object> fyMap : fyList){
  				CostRecord  record = new CostRecord();
  				record.setName("其他");
  				record.setFormatPriceAfter(fyMap.get("price").toString());
  				record.setSupplyName(fyMap.get("supplyName").toString());
  				list.add(record);
  			}
  			actualOutList.addAll(list);
  		}
        map.put("actualOutList", actualOutList);//预计境外付款/境外支出
        return outMoneySum;
    }


    /**
     * 预报单->预计境内付款/结算单->境内支出
     * @param map
     * @param costRecordVO
     * @return
     * @author shijun.liu
     */
    private BigDecimal actualInData(Map<String, Object> map, CostRecordVO costRecordVO, Integer orderType){
        List<CostRecord> actualInList = costRecordVO.getActualInList();
        BigDecimal inMoneySum = new BigDecimal("0");
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        for (CostRecord record:actualInList) {
            BigDecimal unitPrice = record.getPrice() == null ? new BigDecimal("0") : record.getPrice();
            BigDecimal priceAfter = record.getPriceAfter()==null ? new BigDecimal("0") : record.getPriceAfter();
            BigDecimal rate = record.getRate() == null ? new BigDecimal("1") : record.getRate();
            BigDecimal tempUnitPrice = unitPrice.multiply(rate);
            String formatUnitPrice = MoneyNumberFormat.getThousandsByRegex(tempUnitPrice.toString(), 2);
            //单价
            record.setFormatPrice(formatUnitPrice);
            //金额
            record.setFormatPriceAfter(MoneyNumberFormat.getThousandsByRegex(priceAfter.toString(),2));
            //地接社
            String supplyName = FinanceUtils.blankReturnEmpty(record.getSupplyName());

            //大洋国旅，将非签约渠道改成未签，2015.11.25 C413
            if(Context.SUPPLIER_UUID_DYGL.equals(companyUuid) && FinanceUtils.AGENT_NAME.equals(supplyName)){
                supplyName = FinanceUtils.AGENT_NAME_WEIQIAN;
            }
            if (Context.SUPPLIER_UUID_YJXZ.equals(companyUuid) && FinanceUtils.AGENT_NAME.equals(supplyName)){
                supplyName = FinanceUtils.AGENT_NAME_ZHIKE; //越谏行踪，将非签约渠道改成直客。 add by yudong.xu 2016.5.30
            }
            record.setSupplyName(FreeMarkerUtil.StringFilter(supplyName));

            inMoneySum = inMoneySum.add(priceAfter);
        }
        //骡子假期返佣
  		if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
  			List<Map<String, Object>> fyList = costRecordVO.getfYlistList();
  			if(fyList != null){
  				for(Map<String,Object> fyMap : fyList){
  					inMoneySum = inMoneySum.add(new BigDecimal(fyMap.get("price").toString()));
  				}
  			}
  		}
        map.put("actualInList", actualInList);//预计境内付款
        return inMoneySum;
    }


    /**
     * 处理产品信息
     * @param map
     * @param costRecordVO
     * @param orderType
     * @author shijun.liu
     */
    private void getProductData(Map<String, Object> map, CostRecordVO costRecordVO, Integer orderType){
        Map<String, Object> baseData = costRecordVO.getBaseinfo();
        String createBy = FinanceUtils.blankReturnEmpty(baseData.get("createBy"));//操作
        String productName = FinanceUtils.blankReturnEmpty(baseData.get("productName"));//线路
        String groupCode = FinanceUtils.blankReturnEmpty(baseData.get("groupCode"));//团期
        String orderPersonNum = FinanceUtils.blankReturnEmpty(baseData.get("orderPersonNum"));//人数
        String groupOpenDate = FinanceUtils.blankReturnEmpty(baseData.get("groupOpenDate"));//日期
        String groupCloseDate = FinanceUtils.blankReturnEmpty(baseData.get("groupCloseDate"));//日期
        String realGroupCloseDate = FinanceUtils.blankReturnEmpty(baseData.get("realGroupCloseDate"));//real截团日期
        String activityDuration = FinanceUtils.blankReturnEmpty(baseData.get("activityDuration"));//天数
        String grouplead = FinanceUtils.blankReturnEmpty(baseData.get("grouplead"));//领队
        if(Context.SUPPLIER_UUID_YYJQ.equals(UserUtils.getUser().getCompany().getUuid())){
            String invoiceTax = baseData.get("invoiceTax")==null ? "0":baseData.get("invoiceTax").toString();//发票税
            map.put("invoiceTax", invoiceTax);
        }
        //针对拉美图团期进行操作，获取同行成人价作为单价，和预计收款。需求3690. yudong.xu
        if (ActivityGroupService.isLMTTuanQi(orderType)){
            String adultPrice = FinanceUtils.blankReturnEmpty(baseData.get("adultPrice"));//单价(成人价)
            String preIncomeOfLMT = FinanceUtils.blankReturnEmpty(baseData.get("preIncome"));//预计收款
            String planPositionOfLMT = FinanceUtils.blankReturnEmpty(baseData.get("planPosition"));//预收人数
            map.put("adultPrice", adultPrice);
            map.put("receiveMoney", preIncomeOfLMT);
            map.put("planPosition", planPositionOfLMT);
        }
        map.put("createBy", FreeMarkerUtil.StringFilter(createBy));
        map.put("createByLeader", baseData.get("createByLeader"));
        map.put("createByLeaderFull", baseData.get("createByLeaderFull"));
        map.put("productName", FreeMarkerUtil.StringFilter(productName));
        map.put("groupCode", FreeMarkerUtil.StringFilter(groupCode));
        map.put("orderPersonNum", orderPersonNum);

        //成人同行价
        String settlementAdultPrice = FinanceUtils.blankReturnEmpty(baseData.get("settlementAdultPrice"));
        if (StringUtils.isBlank(settlementAdultPrice)){
            map.put("settlementAdultPrice","");
        }else {
            String currencyType = FinanceUtils.blankReturnEmpty(baseData.get("currencyType"));//对应的币种id数组
            String currencyMark = currencyService.findCurrencyMark(currencyType.split(",")[0]);
            map.put("settlementAdultPrice",currencyMark + settlementAdultPrice);
        }

        String groupOpenDateStr = DateUtils.date2String(DateUtils.string2Date(groupOpenDate)); // 开团日期
        String groupCloseDateStr = DateUtils.date2String(DateUtils.string2Date(groupCloseDate)); // 开团日期 + 天数(散拼)
        String realGroupCloseDateStr = DateUtils.date2String(DateUtils.string2Date(realGroupCloseDate)); // 真正的截团日期
         /*注意：只有骡子假期使用groupOpenDate,groupCloseDate这两个名称;其他只使用groupDate，由于名字不同，这里没加批发商判断。
         机票使用groupCloseDateStr，签证无，散拼使用realGroupCloseDateStr*/
        map.put("groupOpenDate",groupOpenDateStr);
        map.put("groupCloseDate",orderType == 7 ? groupCloseDateStr : realGroupCloseDateStr);
        if(orderType == Context.ORDER_TYPE_QZ){
            map.put("groupDate", "");
        }else if(orderType == Context.ORDER_TYPE_JP || orderType == Context.ORDER_TYPE_HOTEL
                || orderType == Context.ORDER_TYPE_ISLAND){
            map.put("groupDate", groupOpenDateStr);
        }else{
            map.put("groupDate", groupOpenDateStr + " - " + groupCloseDateStr);
        }
        String days = FinanceUtils.getDays(orderType,groupCloseDate,groupOpenDate,activityDuration);
        map.put("activityDuration", days);
        map.put("grouplead", FreeMarkerUtil.StringFilter(grouplead));
        map.put("orderType", CommonUtils.getOrderTypeName(orderType));
        map.put("personDay", "");//人天数
    }

    /**
     * 处理付款明细数据
     * @param map
     * @param costRecordVO
     * @param orderType
     * @author shijun.liu
     */
    private void payDetailData(Map<String, Object> map, CostRecordVO costRecordVO, Integer orderType){

        String companyId = UserUtils.getUser().getCompany().getUuid();//用户uuid modify by wangyang 2016.3.17
        StringBuilder salers = new StringBuilder();//销售
        BigDecimal totalSum = new BigDecimal("0");//预计收款-预计收款-合计
        BigDecimal backSum = new BigDecimal("0");//预计收款-预计退款-合计
        BigDecimal accountedSum = new BigDecimal("0");//预计收款-实际收款-合计
        BigDecimal notAcccountedSum = new BigDecimal("0");//预计收款-未收款项-合计
        BigDecimal orderMoneySum = new BigDecimal(0); // 团队的订单总额，未减去差额的原始值，骡子假期需求
        int orderPersonNumSum = 0;
        //预计收款、收款明细
        List<Map<String, Object>> refundInfoList = costRecordVO.getRefundInfo();
        for (Map<String, Object> refund:refundInfoList) {
            String totalMoneyStr = refund.get("totalMoney")==null?"0":refund.get("totalMoney").toString();
            String refundPriceStr = refund.get("refundprice")==null?"0":refund.get("refundprice").toString();
            String accountedMoneyStr = refund.get("accountedMoney")==null?"0":refund.get("accountedMoney").toString();
            String orderPersonNumStr = refund.get("orderPersonNum")==null?"0":refund.get("orderPersonNum").toString();
            BigDecimal totalMoney = new BigDecimal(totalMoneyStr);
            BigDecimal backMoney = new BigDecimal(refundPriceStr);
            BigDecimal accountedMoney = new BigDecimal(accountedMoneyStr);
            orderPersonNumSum += Integer.parseInt(orderPersonNumStr);
            refund.put("orderPersonNum", Integer.parseInt(orderPersonNumStr));
            refund.put("totalMoney", MoneyNumberFormat.getThousandsByRegex(totalMoneyStr, 2));
            refund.put("refundprice", MoneyNumberFormat.getThousandsByRegex(refundPriceStr, 2));
            refund.put("accountedMoney", MoneyNumberFormat.getThousandsByRegex(accountedMoneyStr, 2));
            //实际收款 = 收款 - 退款  只适用于辉煌齐鲁 2016.03.17 by jinxin.gao
            BigDecimal notAccountedMoney = new BigDecimal("0");
            BigDecimal newAccountedMoney = new BigDecimal("0");
            // 需求 457 预报单   未收款项 = 预计收款 - 实际收款 + 预计退款
            //		   结算单	   未收团款 = 收款 - 实际收款 + 退款
            notAccountedMoney = totalMoney.subtract(accountedMoney).add(backMoney);
            /**if(Context.SUPPLIER_UUID_QDKS.equals(UserUtils.getUser().getCompany().getUuid())){
             newAccountedMoney = totalMoney.subtract(backMoney);
             refund.put("accountedMoney", MoneyNumberFormat.getThousandsByRegex(newAccountedMoney.toString(), 2));
             //未收款项 = 预计收款- 实际收款  2015.05.19
             notAccountedMoney = totalMoney.subtract(newAccountedMoney);
             }else{
             newAccountedMoney = totalMoney.subtract(backMoney);
             refund.put("accountedMoney", MoneyNumberFormat.getThousandsByRegex(accountedMoney.toString(), 2));
             //未收款项 = 预计收款- 实际收款  2015.05.19
             notAccountedMoney = totalMoney.subtract(accountedMoney);
             }*/
            
            //0546需求 实收合计=实收总额
//            if (Context.SUPPLIER_UUID_LZJQ.equals(companyId)){
//                String adultMoneyStr = refund.get("adultMoney") == null ? "0":refund.get("adultMoney").toString();
//                String childMoneyStr = refund.get("childMoney") == null ? "0":refund.get("childMoney").toString();
//                String specialMoneyStr = refund.get("specialMoney") == null ? "0":refund.get("specialMoney").toString();
//
//                refund.put("adultMoney", MoneyNumberFormat.getThousandsByRegex(adultMoneyStr, 2));
//                refund.put("childMoney", MoneyNumberFormat.getThousandsByRegex(childMoneyStr, 2));
//                refund.put("specialMoney", MoneyNumberFormat.getThousandsByRegex(specialMoneyStr, 2));
//                // 只有是其他收入的时候才使用其他收入的totalMoney值，不然记为0.
//                BigDecimal otherMoney = "其他".equals(refund.get("agentName")) ? totalMoney : BigDecimal.ZERO;
//                BigDecimal adultMoney = new BigDecimal(adultMoneyStr);
//                BigDecimal childMoney = new BigDecimal(childMoneyStr);
//                BigDecimal specialMoney = new BigDecimal(specialMoneyStr);
//                orderMoneySum = orderMoneySum.add(adultMoney).add(childMoney).add(specialMoney).add(otherMoney);
//            }

            //当未收款小于0时，未收款值置0 (只适用于拉美图) modify by 王洋 2016.3.17
            if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyId)){
                notAccountedMoney = notAccountedMoney.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : notAccountedMoney;
            }
            /**
             * 	环球行用户不适用于 457需求，即保持原需求不变
             * 	预报单   未收款项 = 预计收款 - 实际收款
             * 	结算单	未收团款 = 收款 - 实际收款
             */
            if(Context.SUPPLIER_UUID_HQX.equals(companyId)){
                notAccountedMoney = totalMoney.subtract(accountedMoney);
            }
            refund.put("notAccountedMoney", MoneyNumberFormat.getThousandsByRegex(notAccountedMoney.toString(), 2));
            totalSum = totalSum.add(totalMoney);
            backSum = backSum.add(backMoney);
            if(Context.SUPPLIER_UUID_QDKS.equals(companyId)){
                accountedSum = accountedSum.add(newAccountedMoney);
            }else{
                accountedSum = accountedSum.add(accountedMoney);
            }
            notAcccountedSum = notAcccountedSum.add(notAccountedMoney);
            String saler = FinanceUtils.blankReturnEmpty(refund.get("saler"));
            if(StringUtils.isNotEmpty(saler) && !"其他".equals(refund.get("agentName"))){
                if(salers.indexOf(saler) == -1){
                    salers.append(saler).append(",");
                }
            }
            if(Context.SUPPLIER_UUID_DYGL.equals(companyId)
                    && "非签约渠道".equals(refund.get("agentName"))){
                refund.put("agentName", "未签");
            }
        }
        //其他收入收款
		List<Map<String,Object>> otherRecordList = costRecordVO.getOtherRecordList();
		BigDecimal otherSum = new BigDecimal("0");
		if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LZJQ)){
			if(otherRecordList.size()>0){
				for(Map<String,Object> otherMap : otherRecordList){
					otherSum = otherSum.add(new BigDecimal(otherMap.get("totalMoney").toString()));
				}
			}
		}
		map.put("otherSum", otherSum);
        map.put("orderPersonNumSum", orderPersonNumSum);
        map.put("totalSum", totalSum);
        map.put("backSum", backSum);
        map.put("accountedSum", accountedSum);
        map.put("notAcccountedSum", notAcccountedSum);
        map.put("orderMoneySum", orderMoneySum);
        if(salers.length()>0){
            salers.delete(salers.length()-1, salers.length());
        }
        map.put("expectIncome", refundInfoList);//预计收款
        if(Context.ORDER_TYPE_SP == orderType || Context.ORDER_TYPE_JP == orderType
                || orderType == Context.ORDER_TYPE_QZ){//散拼,机票，签证销售为空
            map.put("salers", "");//销售
        }else{
            map.put("salers", FreeMarkerUtil.StringFilter(salers.toString()));//销售
        }
    }
    
    /**
     * 其他收入存入settle
     * @param settle
     * @param resultMap
     * @author chao.zhang
     */
    private void  castMap2SettleOtherRecordListForLZJQ(Settle settle,Map<String,Object> resultMap){
    	List<Map<String,Object>> others = (List<Map<String,Object>>)resultMap.get("otherRecordList");
    	List<SettleOther> otherRecordList = new ArrayList<SettleOther>();
    	for(Map<String,Object> map : others){
    		SettleOther other = new SettleOther();
    		other.setAgentName(map.get("supplyName").toString());
    		other.setComment(map.get("comment").toString());
    		other.setTotalMoney(map.get("totalMoney").toString());
    		other.setName(map.get("name").toString());
    		otherRecordList.add(other);
    	}
    	settle.setOtherRecordList(otherRecordList);
    }
    
    private void castSettleOther2MapForLZJQ(Settle settle,Map<String,Object> resultMap){
    	List<Map<String,Object>> otherRecordList = new ArrayList<Map<String,Object>>();
    	List<SettleOther> recordList = settle.getOtherRecordList();
    	if(recordList != null){
    		for(SettleOther other : recordList){
    			Map<String,Object> map = new HashMap<String, Object>();
    			map.put("name", other.getName());
    			map.put("supplyName", other.getAgentName());
    			map.put("comment", other.getComment());
    			map.put("totalMoney", other.getTotalMoney());
    			otherRecordList.add(map);
    		}
    		resultMap.put("otherRecordList", otherRecordList);
    	}
    }
}
