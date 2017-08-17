package com.trekiz.admin.modules.finance.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.utils.XmlUtils;
import com.trekiz.admin.modules.finance.entity.ForeCast;
import com.trekiz.admin.modules.finance.entity.ForeCastIn;
import com.trekiz.admin.modules.finance.entity.ForeCastOrder;
import com.trekiz.admin.modules.finance.entity.ForeCastOther;
import com.trekiz.admin.modules.finance.entity.ForeCastOut;
import com.trekiz.admin.modules.finance.repository.IForeCastDao;
import com.trekiz.admin.modules.finance.service.IForeCastService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，预报单的Service实现类
 * @author shijun.liu
 * @date 2016年05月05日
 */
@Service
@Transactional(readOnly = true)
public class ForeCastServiceImpl implements IForeCastService {

    @Autowired
    private IForeCastDao foreCastDao;

    /**
     * 获取预报单的Map类型的数据。
     */
    public Map<String,Object> getForeCast(Integer orderType,Long groupId,String groupUuid){
        ForeCast foreCast = getForeCastObj(orderType,groupId,groupUuid);
        Map<String,Object> resultMap = castForeCast2Map(foreCast);
        return resultMap;
    }

    /**
     * 返回ForeCast对象类型的预报单数据。
     */
    public ForeCast getForeCastObj(Integer orderType,Long groupId,String groupUuid){
        String groupIdUuid = getGroupIdUuid(orderType,groupId,groupUuid);
        ForeCast foreCast = foreCastDao.findForeCast(orderType,groupIdUuid);
        return foreCast;
    }

    /**
     * 返回单纯的预报单基本数据，不包含字表信息。
     */
    @Override
    public ForeCast getSimpleForeCast(Integer orderType, String groupIdUuid) {
        ForeCast foreCast = foreCastDao.findSimpleForeCast(orderType,groupIdUuid);
        return foreCast;
    }

    /**
     * 返回单纯的预报单基本数据，不包含字表信息。
     */
    @Override
    public ForeCast getSimpleForeCast(Integer orderType,Long groupId,String groupUuid) {
        String groupIdUuid = getGroupIdUuid(orderType,groupId,groupUuid);
        ForeCast foreCast = foreCastDao.findSimpleForeCast(orderType,groupIdUuid);
        return foreCast;
    }


    /**
     * 保存传入的Map类型的预报单数据到数据库。
     */
    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    public void saveForeCastByMap(Integer orderType,String groupIdUuid,Map<String,Object> resultMap){
        ForeCast foreCast = handleHistory(orderType,groupIdUuid);
        foreCast = castMap2ForeCast(foreCast,resultMap);
        if (foreCast.getId() == null){//表示是新建的记录,不是持久化对象,需要设置一些属性
            foreCast.setUuid(UuidUtils.generUuid());
            foreCast.setOrderType(orderType);
            foreCast.setGroupIdUuid(groupIdUuid);
        }
        foreCastDao.saveOrUpdateForeCast(foreCast);
    }

    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    public void saveForeCastByMap(Integer orderType,Long groupId,String groupUuid,Map<String,Object> resultMap){
        String groupIdUuid = getGroupIdUuid(orderType,groupId,groupUuid);
        saveForeCastByMap(orderType,groupIdUuid,resultMap);
    }

    /**
     * 处理原有保存的预报单数据（查询数据库，返回对应的ForeCast对象，如果没有找到返回null，
     * 如果找到则删除子数据信息,不删除主表ForeCast记录，返回保留基本信息ForeCast对象）
     */
    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    private ForeCast handleHistory(Integer orderType,String groupIdUuid){
        ForeCast foreCast = foreCastDao.findSimpleForeCast(orderType,groupIdUuid);
        if (null == foreCast)
            return null;
        foreCastDao.deleteSub("ForeCastOrder",foreCast.getUuid());
        foreCastDao.deleteSub("ForeCastIn",foreCast.getUuid());
        foreCastDao.deleteSub("ForeCastOut",foreCast.getUuid());
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	 foreCastDao.deleteSub("ForeCastOther", foreCast.getUuid());
        }
        return foreCast;
    }

    //根据订单的不同来生成groupIdUuid
    static String getGroupIdUuid(Integer orderType,Long groupId,String groupUuid){
        if (orderType == Context.ORDER_TYPE_ISLAND || orderType == Context.ORDER_TYPE_HOTEL ){
            if (null == groupUuid)
                throw new IllegalArgumentException();
            return groupUuid;
        }else {
            if (null == groupId)
                throw new IllegalArgumentException();
            return groupId.toString();
        }
    }

    /**
     * 将ForeCast对象解析为Map类型的预报单信息。用于把从数据库查询出来的对象，转换为页面上使用的数据。
     * @param foreCast
     * @return
     */
    private Map<String,Object> castForeCast2Map(ForeCast foreCast){
        if (null == foreCast)
            return null;

        //把传入的ForeCast对象转换成Map.用于页面显示,并且进行字段名称的映射。
        Map<String,Object> resultMap = new HashMap<>(36);
        resultMap.put("createBy",foreCast.getOperator());
        resultMap.put("createByLeader",foreCast.getOperatorManagers());
        resultMap.put("createByLeaderFull",foreCast.getOperatorManagersFull());
        resultMap.put("salers",foreCast.getSalers());
        resultMap.put("productName",foreCast.getProductName());
        resultMap.put("groupCode",foreCast.getGroupCode());
//        resultMap.put("orderPersonNum", Integer.parseInt(foreCast.getPersonNum()));
        resultMap.put("personDay",foreCast.getPersonDay());
        resultMap.put("groupDate",foreCast.getOpenCloseDate());
        resultMap.put("activityDuration",foreCast.getActivityDuration());
        resultMap.put("grouplead",foreCast.getGrouplead());
        resultMap.put("supplier",foreCast.getSupplier());
        resultMap.put("invoiceMoney",foreCast.getInvoiceMoney());
        resultMap.put("profitAfterTax",foreCast.getProfitAfterTax());
        resultMap.put("price",foreCast.getPrice());
        resultMap.put("remark", StringUtils.isNotBlank(foreCast.getRemark())? foreCast.getRemark():"");

        resultMap.put("groupOpenDate", foreCast.getGroupOpenDate());
        resultMap.put("groupCloseDate", foreCast.getGroupCloseDate());
        resultMap.put("settlementAdultPrice", foreCast.getSettlementAdultPrice());
        resultMap.put("orderType", CommonUtils.getOrderTypeName(foreCast.getOrderType()));
        resultMap.put("history", foreCast.getHistory());
        //骡子假期 团队退款备注
        resultMap.put("groupRefundSum", foreCast.getGroupRefundSum());
        List<ForeCastOrder> expectIncome = foreCast.getExpectedIncome();
        List<Map<String,Object>> orderList = new ArrayList<>();
        for (ForeCastOrder order : expectIncome) {
            Map<String,Object> map = new HashMap<>(10);
            map.put("saler",order.getSaler());
            map.put("agentName",order.getAgentName());
            map.put("orderPersonNum",order.getOrderPersonNum());
            map.put("totalMoney",order.getTotalMoney());
            map.put("refundprice",order.getBackMoney());
            map.put("accountedMoney",order.getAccountedMoney());
            map.put("notAccountedMoney",order.getNotAccountedMoney());
            
            map.put("price", order.getPrice());
            map.put("personNum", order.getPersonNum());
            map.put("totalPrice", order.getTotalPrice());
            map.put("remark", order.getRemark());
            orderList.add(map);
        }
        resultMap.put("expectIncome",orderList);
        resultMap.put("totalMoneySum",foreCast.getTotalMoneySum());
        resultMap.put("backMoneySum",foreCast.getBackMoneySum());
        resultMap.put("accountedMoneySum",foreCast.getAccountedMoneySum());
        resultMap.put("notAccountedMoneySum",foreCast.getNotAccountedMoneySum());
        resultMap.put("orderPersonNumSum", Integer.parseInt(foreCast.getPersonNum()));

        List<ForeCastIn> expectedInList = foreCast.getExpectedInList();
        List<Map<String,Object>> inList = new ArrayList<>();
        for (ForeCastIn foreCastIn : expectedInList) {
            Map<String,Object> map = new HashMap<>(8);
            map.put("name",foreCastIn.getCostName());
            map.put("supplyName",foreCastIn.getAgentName());
            map.put("formatPrice",foreCastIn.getPrice());
            map.put("quantity",foreCastIn.getQuantity());
            map.put("formatPriceAfter",foreCastIn.getAmount());
            map.put("comment", foreCastIn.getComment());
            inList.add(map);
        }
        resultMap.put("actualInList",inList);
        resultMap.put("expectedInMoneySum",foreCast.getExpectedInMoneySum());

        List<ForeCastOut> expectedOutList = foreCast.getExpectedOutList();
        
        // bugfree--17742--点击预报单下载页面报错---问题处理--高阳 2017-04-12---start
        // 注释掉的代码
        /*List<Map<String,Object>> outList = new ArrayList<>();
        for (ForeCastOut foreCastOut : expectedOutList) {
            Map<String,Object> map = new HashMap<>(8);
            map.put("supplyName",foreCastOut.getAgentName());
            map.put("name",foreCastOut.getCurrencyName());
            map.put("rate",foreCastOut.getRate());
            map.put("formatPrice",foreCastOut.getPrice());
            map.put("formatPriceAfter",foreCastOut.getAmount());
            map.put("quantity", foreCastOut.getQuantity());
            map.put("comment", foreCastOut.getComment());
            outList.add(map);
        }*/
        // 新追加的代码
        List<CostRecord> outList = new ArrayList<CostRecord>();
        for (ForeCastOut foreCastOut : expectedOutList) {
        	CostRecord record = new CostRecord();
        	record.setSupplyName(foreCastOut.getAgentName());
        	record.setName(foreCastOut.getCurrencyName());
        	record.setRate(new BigDecimal(foreCastOut.getRate()));
        	record.setFormatPrice(foreCastOut.getPrice());
        	record.setFormatPriceAfter(foreCastOut.getAmount());
        	record.setQuantity(foreCastOut.getQuantity());
        	record.setComment(foreCastOut.getComment());
            outList.add(record);
        }
        // bugfree--17742--点击预报单下载页面报错---问题处理--高阳 2017-04-12---end
        
        resultMap.put("actualOutList", outList);
        resultMap.put("expectedOutMoneySum",foreCast.getExpectedOutMoneySum());

        resultMap.put("totalExpenditureMoneySum", foreCast.getTotalExpenditureMoneySum());
        
        resultMap.put("realMoneySum",foreCast.getRealMoneySum());
        resultMap.put("outMoneySum",foreCast.getOutMoneySum());
        resultMap.put("profitSum",foreCast.getProfitSum());
        resultMap.put("profitRate",foreCast.getProfitRate());
        
        //其他收入收款
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	List<Map<String,Object>> otherRecordList = new ArrayList<Map<String,Object>>();
        	List<ForeCastOther> recordList = foreCast.getOtherRecordList();
        	if(recordList != null){
        		for(ForeCastOther other : recordList){
            		Map<String,Object> otherMap = new HashMap<String, Object>();
            		otherMap.put("agentName", other.getAgentName());
            		otherMap.put("comment", other.getComment());
            		otherMap.put("name", other.getName());
            		otherMap.put("totalMoney", other.getTotalMoney());
            		otherRecordList.add(otherMap);
            	}
        	}
        	resultMap.put("otherRecordList", otherRecordList);
        	resultMap.put("otherSum", foreCast.getOtherSum());
        }
        return resultMap;
    }

    /**
     * 将Map类型的预报单数据转换为ForeCast对象。
     * @param resultMap
     * @param foreCast 从数据库查询出来的对象
     * @return
     */
    private ForeCast castMap2ForeCast(ForeCast foreCast,Map<String,Object> resultMap){
        if (null == resultMap)
            return null;
        if (null == foreCast){
            foreCast = new ForeCast();
            foreCast.setCreateBy(UserUtils.getUser().getId());//创建人，创建日期
            foreCast.setCreateDate(new Date());
        }
        foreCast.setUpdateBy(UserUtils.getUser().getId());//更新人，更新日期
        foreCast.setUpdateDate(new Date());

        //把map数据转换成ForeCast对象。
        //团期的基本信息
        String createBy = StringUtils.blankReturnEmpty(resultMap.get("createBy"));
        foreCast.setOperator(createBy);//操作人
        String createByLeader = StringUtils.blankReturnEmpty(resultMap.get("createByLeader"));
        foreCast.setOperatorManagers(createByLeader);//操作负责人简写
        String createByLeaderFull = StringUtils.blankReturnEmpty(resultMap.get("createByLeaderFull"));
        foreCast.setOperatorManagersFull(createByLeaderFull);//操作负责人全写
        String salers = StringUtils.blankReturnEmpty(resultMap.get("salers"));
        foreCast.setSalers(salers);//销售

        String productName = StringUtils.blankReturnEmpty(resultMap.get("productName"));
        foreCast.setProductName(productName);//线路
        String groupCode = StringUtils.blankReturnEmpty(resultMap.get("groupCode"));
        foreCast.setGroupCode(groupCode);//团号
        String orderPersonNum = StringUtils.blankReturnEmpty(resultMap.get("orderPersonNumSum"));
        foreCast.setPersonNum(orderPersonNum);//人数
        String personDay = StringUtils.blankReturnEmpty(resultMap.get("personDay"));
        foreCast.setPersonDay(personDay);//人天数

        String groupDate = StringUtils.blankReturnEmpty(resultMap.get("groupDate"));
        foreCast.setOpenCloseDate(groupDate);//日期(团期的开始时间拼接结束时间的字符串)
        String activityDuration = StringUtils.blankReturnEmpty(resultMap.get("activityDuration"));
        foreCast.setActivityDuration(activityDuration);//天数

        String grouplead = StringUtils.blankReturnEmpty(resultMap.get("grouplead"));
        foreCast.setGrouplead(grouplead);//领队
        String supplier = StringUtils.blankReturnEmpty(resultMap.get("supplier"));
        foreCast.setSupplier(supplier);//地接社
        String price = StringUtils.blankReturnEmpty(resultMap.get("price"));
        foreCast.setPrice(price);//单价(拉美图)

        //懿洋假期,发票税款，及减税后利润
        String invoiceMoney = StringUtils.blankReturnEmpty(resultMap.get("invoiceMoney"));
        foreCast.setInvoiceMoney(invoiceMoney);
        String profitAfterTax = StringUtils.blankReturnEmpty(resultMap.get("profitAfterTax"));
        foreCast.setProfitAfterTax(profitAfterTax);
        
        //0546 骡子假期 增加出/截团日期，同行价-不降价价格字段  modify by yang.wang 2016.11.11
        String groupOpenDate = StringUtils.blankReturnEmpty(resultMap.get("groupOpenDate"));
        foreCast.setGroupOpenDate(groupOpenDate);
        String groupCloseDate = StringUtils.blankReturnEmpty(resultMap.get("groupCloseDate"));
        foreCast.setGroupCloseDate(groupCloseDate);
        String settlementAdultPrice = StringUtils.blankReturnEmpty(resultMap.get("settlementAdultPrice"));
        foreCast.setSettlementAdultPrice(settlementAdultPrice);
        foreCast.setHistory(2);
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	if(resultMap.get("groupRefundSum") != null){
        		foreCast.setGroupRefundSum(resultMap.get("groupRefundSum").toString());
        	}
        	foreCast.setOtherSum(resultMap.get("otherSum").toString());
        }
        
        //预计收款详细信息
        List<Map<String,Object>> expectIncome = (List<Map<String,Object>>)resultMap.get("expectIncome");
        if (null != expectIncome){
            List<ForeCastOrder> orderList = new ArrayList<>();
            for (Map<String, Object> map : expectIncome) {
                ForeCastOrder order = new ForeCastOrder();
                order.setSaler(StringUtils.blankReturnEmpty(map.get("saler")));
                order.setAgentName(StringUtils.blankReturnEmpty(map.get("agentName")));
                
                order.setOrderPersonNum((Integer) map.get("orderPersonNum"));
                order.setTotalMoney(StringUtils.blankReturnEmpty(map.get("totalMoney")));
                order.setBackMoney(StringUtils.blankReturnEmpty(map.get("refundprice")));//为统一起见，使用backMoney作为退款。
                order.setAccountedMoney(StringUtils.blankReturnEmpty(map.get("accountedMoney")));
                order.setNotAccountedMoney(StringUtils.blankReturnEmpty(map.get("notAccountedMoney")));
                
                // 0546 骡子假期 modify by yang.wang  2016.11.11
                order.setPrice(StringUtils.blankReturnEmpty(map.get("price")));
                order.setPersonNum(Integer.parseInt(map.get("personNum") == null || "".equals(map.get("personNum")) ? "0" : map.get("personNum").toString()));
                order.setTotalPrice(StringUtils.blankReturnEmpty(map.get("totalPrice")));
                order.setRemark(StringUtils.blankReturnEmpty(map.get("remark")));
                orderList.add(order);
            }
            foreCast.setExpectedIncome(orderList);
        }
        //预计收款统计信息
        String totalMoneySum = StringUtils.blankReturnEmpty(resultMap.get("totalMoneySum"));
        foreCast.setTotalMoneySum(totalMoneySum);
        String backMoneySum = StringUtils.blankReturnEmpty(resultMap.get("backMoneySum"));
        foreCast.setBackMoneySum(backMoneySum);
        String accountedMoneySum = StringUtils.blankReturnEmpty(resultMap.get("accountedMoneySum"));
        foreCast.setAccountedMoneySum(accountedMoneySum);
        String notAccountedMoneySum = StringUtils.blankReturnEmpty(resultMap.get("notAccountedMoneySum"));
        foreCast.setNotAccountedMoneySum(notAccountedMoneySum);

        //预计境内付款详细信息
        List<CostRecord> actualInList = (List<CostRecord>)resultMap.get("actualInList");
        if (null != actualInList){
            List<ForeCastIn> inList = new ArrayList<>();
            for (CostRecord record : actualInList) {
                //为了见文知意，这里进行字段名称映射。
                ForeCastIn inModel = new ForeCastIn();
                inModel.setCostName(record.getName());
                inModel.setAgentName(record.getSupplyName());
                inModel.setPrice(record.getFormatPrice());
                inModel.setQuantity(record.getQuantity());
                inModel.setAmount(record.getFormatPriceAfter());
                inModel.setComment(record.getComment());
                inList.add(inModel);
            }
            foreCast.setExpectedInList(inList);
        }
        //预计境内付款统计信息
        String expectedInMoneySum = StringUtils.blankReturnEmpty(resultMap.get("expectedInMoneySum"));
        foreCast.setExpectedInMoneySum(expectedInMoneySum);

        //预计境外付款详细信息
        List<CostRecord> actualOutList = (List<CostRecord>)resultMap.get("actualOutList");
        if(null != actualOutList){
            List<ForeCastOut> outList = new ArrayList<>();
            for (CostRecord record : actualOutList) {
                ForeCastOut outModel = new ForeCastOut();
                outModel.setAgentName(record.getSupplyName());
                outModel.setCurrencyName(record.getName());//业务中使用了name字段来保存币种名称
                outModel.setRate(StringUtils.blankReturnEmpty(record.getRate()));
                outModel.setPrice(record.getFormatPrice());//对应页面上的外币字段
                outModel.setAmount(record.getFormatPriceAfter());
                outModel.setQuantity(record.getQuantity());
                outModel.setComment(record.getComment());
                outList.add(outModel);
            }
            foreCast.setExpectedOutList(outList);
        }
        //预计境外付款统计信息
        String expectedOutMoneySum = StringUtils.blankReturnEmpty(resultMap.get("expectedOutMoneySum"));
        foreCast.setExpectedOutMoneySum(expectedOutMoneySum);
        String totalExpenditureMoneySum = StringUtils.blankReturnEmpty(resultMap.get("totalExpenditureMoneySum"));
        foreCast.setTotalExpenditureMoneySum(totalExpenditureMoneySum);

        //下方统计信息
        String realMoneySum = StringUtils.blankReturnEmpty(resultMap.get("realMoneySum"));
        String outMoneySum = StringUtils.blankReturnEmpty(resultMap.get("outMoneySum"));
        String profitSum = StringUtils.blankReturnEmpty(resultMap.get("profitSum"));
        String profitRate = StringUtils.blankReturnEmpty(resultMap.get("profitRate"));
        foreCast.setRealMoneySum(realMoneySum);
        foreCast.setOutMoneySum(outMoneySum);
        foreCast.setProfitSum(profitSum);
        foreCast.setProfitRate(profitRate);
        if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LZJQ)){
        	//其他收入收款
        	List<Map<String,Object>> otherMapList = (List<Map<String,Object>>)resultMap.get("otherRecordList");
        	List<ForeCastOther> otherRecordList = new ArrayList<ForeCastOther>();
        	for(Map<String,Object> otherMap : otherMapList){
        		ForeCastOther other = new ForeCastOther();
        		other.setAgentName(otherMap.get("agentName").toString());
        		other.setName(otherMap.get("name").toString());
        		other.setComment(otherMap.get("comment").toString());
        		other.setTotalMoney(otherMap.get("totalMoney").toString());
        		
        		otherRecordList.add(other);
        	}
        	foreCast.setOtherRecordList(otherRecordList);
        }
        return foreCast;//返回填充完数据的ForeCast对象。
    }

    /**
     * 将拉美图的xml保存的团期预报单数据存储到数据库中。 yudong.xu
     * @return 返回日志字符串。
     */
    @Transactional(readOnly = false,rollbackFor = {Exception.class})
    public String xml2DatabaseLMT(){
        String xmlAddr = XmlUtils.getDiffXmlRoot(0);
        File file = new File(xmlAddr);
        String[] xmls = file.list();
        StringBuilder log = new StringBuilder();
        try {
            log.append("------Here we go! ------\n");
            for (String xmlName : xmls) {//类似 2_123324.xml
                String[] params = xmlName.split("_");
                Integer orderType = Integer.parseInt(params[0]);
                String params1 = params[1];
                String groupIdUuid = params1.substring(0,params1.length()-4);//截取id部分。
                String snapshotAddr = xmlAddr + File.separator + xmlName; //目录名+文件名
                log.append("\n");
                log.append(snapshotAddr).append(" --->");
                Map<String,Object> resultMap = XmlUtils.findSnapshot(snapshotAddr); //解析xml数据
                log.append(" is parsed OK ");
                saveForeCastByMap(orderType,groupIdUuid,resultMap); //保存到数据库
                log.append(" ---> is stored by database Successfully!\n");
            }
            log.append("\nwell done! All xml file is stored in database Successfully,Congratulations!");
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            return log.toString();
        }
    }

	@Override
	@Transactional(readOnly = false,rollbackFor = {Exception.class})
	public void updateForeCast(ForeCast foreCast) {
		// TODO Auto-generated method stub
		foreCastDao.saveOrUpdateForeCast(foreCast);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor = {Exception.class})
	public void saveForeCast(ForeCast foreCast) {
		// TODO Auto-generated method stub
		foreCastDao.saveOrUpdateForeCast(foreCast);
	}

}
