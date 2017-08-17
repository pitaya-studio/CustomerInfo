package com.trekiz.admin.modules.cost.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.cost.entity.CostRecord;

/**
 * 解析预报单，结算单xml数据 2016/4/28.
 */
public class XmlUtils {
    private static String getXmlPath(){
        //文件绝对路径根目录
        String basePath = Global.getBasePath();
        String pathPrefix = basePath + File.separator + "snapshot";
        File file = new File(pathPrefix);
        if(!file.exists()){
            file.mkdirs();
        }
        return pathPrefix;
    }
    /**
     * 返回存储预报单或结算单的数据的路径
     * 默认是预报单; 1是结算单
     * @author yudong.xu --2016/4/28--16:37
     */
    public static String getDiffXmlRoot(Integer type){
        String danzi = "forcast";
        if (type == 1){
            danzi = "settleList";
        }
        String xmlRootPath = getXmlPath();
        String diffXmlRoot = xmlRootPath + File.separator + danzi;
        File file = new File(diffXmlRoot);
        if(!file.exists()){
            file.mkdirs();
        }
        return diffXmlRoot;
    }

    /**
     * 根据订单类型和团期号生成预报单或结算单xml的文件路径。type参数判断是结算单还是预报单。
     * @author yudong.xu --2016/4/28--16:58
     */
    public static String getSnapshotPath(Integer type,Integer orderType,Long groupId,String groupUuid){
        String  danziBase =  getDiffXmlRoot(type);
        StringBuilder fileName = new StringBuilder();
        fileName.append(danziBase).append(File.separator).append(orderType).append("_");
        if (orderType == Context.ORDER_TYPE_ISLAND || orderType == Context.ORDER_TYPE_HOTEL){
            fileName.append(groupUuid).append(".xml");
        }else {
            fileName.append(groupId).append(".xml");
        }
        return fileName.toString();
    }



    /**
     * 解析预报单,结算单的xml快照，返回解析后的map对象。该方法的境内境外付款对象是CostRecord类型的。另一个是Map类型的。
     * @author yudong.xu --2016/4/28--21:00
     */
    public static Map<String,Object> findSnapshot(String addr) throws DocumentException {
        boolean isExists = new File(addr).exists();
        if (!isExists){
            return null;
        }

        Map<String,Object> resultMap = new HashMap<>(32);//因知道该Map的大小，故指定了初始化大小。
        SAXReader reader = new SAXReader();
        Document document = reader.read(addr);
        Element rootEle = document.getRootElement();

        resultMap.put("createBy",rootEle.element("operator").getText());//操作人
        resultMap.put("createByLeader",rootEle.element("operatorLeader").getText());//操作负责人简写
        resultMap.put("createByLeaderFull",rootEle.element("operatorLeaderFull").getText());//操作负责人全写
        resultMap.put("salers",rootEle.element("salers").getText());//销售

        resultMap.put("productName",rootEle.element("productName").getText());//线路
        resultMap.put("groupCode",rootEle.element("groupCode").getText());//团号
        resultMap.put("orderPersonNum",rootEle.element("orderPersonNum").getText());//人数
        resultMap.put("personDay",rootEle.element("personDay").getText());//人天数
        resultMap.put("groupDate",rootEle.element("groupDate").getText());//日期
        resultMap.put("activityDuration",rootEle.element("activityDuration").getText());//天数
        resultMap.put("grouplead",rootEle.element("grouplead").getText());//领队
        resultMap.put("supplier",rootEle.element("supplier").getText());//地接社

//        resultMap.put("totalMoneySum",rootEle.element("price").getText());//报价
//        resultMap.put("adultPrice",rootEle.element("adultPrice").getText());//成人单价(拉美图需求)
//        resultMap.put("planPosition",rootEle.element("planPosition").getText());//预收人数(拉美图需求)

        //原先拉美图使用字段"adultPrice",作为显示单价。现在统一使用"price"作为单价。把保存的xml标签解析为"price"字段。
        resultMap.put("price",rootEle.element("adultPrice").getText());//成人单价(拉美图需求)，用于数据同步，yudong.xu
        //原先拉美图人数使用"planPosition"字段，现在原有的字段"orderPersonNum"作为显示。把xml保存的人数转换为"orderPersonNum"。
        resultMap.put("orderPersonNum",rootEle.element("planPosition").getText());//预收人数(拉美图需求)用于数据同步，yudong.xu
        //原先拉美图的预计收款等于预计收入合计。所以只需解析预计收入合计totalMoneySum，即可获取预计收款的值。
        resultMap.put("receiveMoney",rootEle.element("receiveMoney").getText());//预计收款

        //预计收款明细
        Element  expectIncomeEle = rootEle.element("expectIncome");
        //获取预计收款每一项的数据。
        List<Element> receiveItems = expectIncomeEle.elements("item");
        List<Map<String,String>> expectIncome = new ArrayList<>();
        for (Element item : receiveItems) {
            Map<String,String> map = new HashMap<>();
            map.put("saler",item.element("saler").getText());
            map.put("agentName",item.element("agentName").getText());
            map.put("totalMoney",item.element("totalMoney").getText());
            map.put("refundprice",item.element("refundprice").getText());
            map.put("accountedMoney",item.element("accountedMoney").getText());
            map.put("notAccountedMoney",item.element("notAccountedMoney").getText());
            expectIncome.add(map);
        }
        resultMap.put("expectIncome",expectIncome);
        //获取预计收款明细统计数据。
        resultMap.put("totalMoneySum",expectIncomeEle.attributeValue("receiveSum","0.00"));
        resultMap.put("backMoneySum",expectIncomeEle.attributeValue("refundSum","0.00"));
        resultMap.put("accountedMoneySum",expectIncomeEle.attributeValue("realSum","0.00"));
        resultMap.put("notAccountedMoneySum",expectIncomeEle.attributeValue("notReceive","0.00"));

        //境内付款解析
        Element  actualInListEle = rootEle.element("actualInList");
        //获取境内付款每一项数据
        List<Element> inItems = actualInListEle.elements("item");
        List<CostRecord> actualInList = new ArrayList<>();
        for (Element item : inItems) {
            CostRecord record = new CostRecord();
            record.setName(item.element("name").getText());
            record.setSupplyName(item.element("supplyName").getText());
            record.setFormatPrice(item.element("formatPrice").getText());
            record.setQuantity(Integer.parseInt(item.element("quantity").getText()));
            record.setFormatPriceAfter(item.element("formatPriceAfter").getText());
            actualInList.add(record);
        }
        resultMap.put("actualInList",actualInList);
        resultMap.put("expectedInMoneySum",actualInListEle.attributeValue("sum","0.00"));

        //境外付款解析
        Element  actualOutListEle = rootEle.element("actualOutList");
        //获取境外付款每一项数据
        List<Element> outItems = actualOutListEle.elements("item");
        List<CostRecord> actualOutList = new ArrayList<>();
        for (Element item : outItems) {
            CostRecord record = new CostRecord();
            record.setSupplyName(item.element("supplyName").getText());
            record.setName(item.element("currencyMark").getText());
            String rateStr = item.element("rate").getText();
            record.setRate(new BigDecimal(rateStr));
            record.setFormatPrice(item.element("formatPrice").getText());
            record.setFormatPriceAfter(item.element("formatPriceAfter").getText());
            actualOutList.add(record);
        }
        resultMap.put("actualOutList",actualOutList);
        resultMap.put("expectedOutMoneySum",actualOutListEle.attributeValue("sum","0.00"));

        resultMap.put("totalMoneySum",rootEle.element("inSum").getText());//预计收入合计
        resultMap.put("backMoneySum",rootEle.element("refundSum").getText());//预计退款合计
        resultMap.put("realMoneySum",rootEle.element("realSum").getText());//实际收入合计
        resultMap.put("outMoneySum",rootEle.element("outSum").getText());//预计支出合计
        resultMap.put("profitSum",rootEle.element("profit").getText());//预计毛利
        resultMap.put("profitRate",rootEle.element("profitRate").getText());//预计毛利率

        return resultMap;
    }

    /**
     * 对传入的预报单,结算单的map对象生成xml类型的快照数据。保存到指定地址
     * @author yudong.xu --2016/4/28--21:05
     */
    public static void createSnapshot(String savePath,Map<String, Object> resultMap) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element rootEle = document.addElement("rootEle");
        rootEle.addElement("operator").setText(resultMap.get("createBy")==null ? "":resultMap.get("createBy").toString());
        rootEle.addElement("operatorLeader").setText(resultMap.get("createByLeader")==null ? "":resultMap.get("createByLeader").toString());
        rootEle.addElement("operatorLeaderFull").setText(resultMap.get("createByLeaderFull")==null ? "":resultMap.get("createByLeaderFull").toString());
        rootEle.addElement("salers").setText(resultMap.get("salers")==null ? "":resultMap.get("salers").toString());
        rootEle.addElement("productName").setText(resultMap.get("productName")==null ? "":resultMap.get("productName").toString());
        rootEle.addElement("groupCode").setText(resultMap.get("groupCode")==null ? "":resultMap.get("groupCode").toString());
        rootEle.addElement("orderPersonNum").setText(resultMap.get("orderPersonNum")==null ? "":resultMap.get("orderPersonNum").toString());
        rootEle.addElement("personDay").setText(resultMap.get("personDay")==null ? "":resultMap.get("personDay").toString());
        rootEle.addElement("groupDate").setText(resultMap.get("groupDate")==null ? "":resultMap.get("groupDate").toString());
        rootEle.addElement("activityDuration").setText(resultMap.get("activityDuration")==null ? "":resultMap.get("activityDuration").toString());
        rootEle.addElement("grouplead").setText(resultMap.get("grouplead")==null ? "":resultMap.get("grouplead").toString());
        rootEle.addElement("supplier").setText(resultMap.get("supplier")==null ? "":resultMap.get("supplier").toString());

        rootEle.addElement("price").setText(resultMap.get("totalMoneySum")==null ? "":resultMap.get("totalMoneySum").toString());//报价
        rootEle.addElement("adultPrice").setText(resultMap.get("adultPrice")==null ? "":resultMap.get("adultPrice").toString());//成人单价(拉美图需求)
        rootEle.addElement("planPosition").setText(resultMap.get("planPosition")==null ? "":resultMap.get("planPosition").toString());//预收人数(拉美图需求)

        rootEle.addElement("receiveMoney").setText(resultMap.get("receiveMoney")==null ? "":resultMap.get("receiveMoney").toString());

        //预计收款明细
        Element expectIncomeEle = rootEle.addElement("expectIncome");
        expectIncomeEle.addAttribute("receiveSum",resultMap.get("totalMoneySum")==null ? "":resultMap.get("totalMoneySum").toString());
        expectIncomeEle.addAttribute("refundSum",resultMap.get("backMoneySum")==null ? "":resultMap.get("backMoneySum").toString());
        expectIncomeEle.addAttribute("realSum",resultMap.get("accountedMoneySum")==null ? "":resultMap.get("accountedMoneySum").toString());
        expectIncomeEle.addAttribute("notReceive",resultMap.get("notAccountedMoneySum")==null ? "":resultMap.get("notAccountedMoneySum").toString());

        List<Map<String,Object>> expectIncome = (List<Map<String,Object>>)resultMap.get("expectIncome");
        for (Map<String, Object> map:expectIncome){
            Element item = expectIncomeEle.addElement("item");
            item.addElement("saler").setText(map.get("saler")==null ? "":map.get("saler").toString());
            item.addElement("agentName").setText(map.get("agentName")==null ? "":map.get("agentName").toString());
            item.addElement("totalMoney").setText(map.get("totalMoney")==null ? "":map.get("totalMoney").toString());
            item.addElement("refundprice").setText(map.get("refundprice")==null ? "":map.get("refundprice").toString());
            item.addElement("accountedMoney").setText(map.get("accountedMoney")==null ? "":map.get("accountedMoney").toString());
            item.addElement("notAccountedMoney").setText(map.get("notAccountedMoney")==null ? "":map.get("notAccountedMoney").toString());
        }

        //境内付款
        Element actualInListEle = rootEle.addElement("actualInList");
        actualInListEle.addAttribute("sum",resultMap.get("expectedInMoneySum")==null ? "":resultMap.get("expectedInMoneySum").toString());

        List<CostRecord> actualInList = (List<CostRecord>)resultMap.get("actualInList");
        for (CostRecord record:actualInList){
            Element item = actualInListEle.addElement("item");
            item.addElement("name").setText(record.getName());
            item.addElement("supplyName").setText(record.getSupplyName());
            item.addElement("formatPrice").setText(record.getFormatPrice());
            item.addElement("quantity").setText(record.getQuantity()==null ? "":record.getQuantity().toString());
            item.addElement("formatPriceAfter").setText(record.getFormatPriceAfter());
        }

        //境外付款
        Element actualOutListEle = rootEle.addElement("actualOutList");
        actualOutListEle.addAttribute("sum",resultMap.get("expectedOutMoneySum")==null ? "":resultMap.get("expectedOutMoneySum").toString());

        List<CostRecord> actualOutList = (List<CostRecord>)resultMap.get("actualOutList");
        for (CostRecord record:actualOutList){
            Element item = actualOutListEle.addElement("item");
            item.addElement("supplyName").setText(record.getSupplyName());
            item.addElement("currencyMark").setText(record.getName());
            item.addElement("rate").setText(record.getRate()==null ? "":record.getRate().toString());
            item.addElement("formatPrice").setText(record.getFormatPrice());
            item.addElement("formatPriceAfter").setText(record.getFormatPriceAfter());
        }

        rootEle.addElement("inSum").setText(resultMap.get("totalMoneySum")==null ? "":resultMap.get("totalMoneySum").toString());
        rootEle.addElement("refundSum").setText(resultMap.get("backMoneySum")==null ? "":resultMap.get("backMoneySum").toString());
        rootEle.addElement("realSum").setText(resultMap.get("realMoneySum")==null ? "":resultMap.get("realMoneySum").toString());
        rootEle.addElement("outSum").setText(resultMap.get("outMoneySum")==null ? "":resultMap.get("outMoneySum").toString());
        rootEle.addElement("profit").setText(resultMap.get("profitSum")==null ? "":resultMap.get("profitSum").toString());
        rootEle.addElement("profitRate").setText(resultMap.get("profitRate")==null ? "":resultMap.get("profitRate").toString());

        OutputFormat format = new OutputFormat();
        format.setEncoding("utf-8");
        FileOutputStream fos = new FileOutputStream(savePath);
        XMLWriter writer = new XMLWriter(fos,format);
        writer.write(document);
        writer.close();
        fos.close();
    }
}
