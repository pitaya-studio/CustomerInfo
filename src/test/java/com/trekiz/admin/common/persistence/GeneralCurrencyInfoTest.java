package com.trekiz.admin.common.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.modules.sys.model.FractionalCurrency;
import com.trekiz.admin.modules.sys.model.GeneralCurrencyInfo;

public class GeneralCurrencyInfoTest extends TestCase {
	public void testGeneralCurrencyInfo() {
    	List<GeneralCurrencyInfo> generalCurrencyInfos = new ArrayList<GeneralCurrencyInfo>();
    	
    	//人民币
    	GeneralCurrencyInfo currencyInfo1 = new GeneralCurrencyInfo();
    	currencyInfo1.setCurrencyName("人民币");
    	currencyInfo1.setCountry("中国");
    	currencyInfo1.setCurrencyCapital("元");
    	currencyInfo1.setFractionalCurrency("角、分");
    	List<FractionalCurrency> fractionalCurrencys1 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency11 = new FractionalCurrency();
    	fractionalCurrency11.setCurrencyMark("角");
    	fractionalCurrency11.setCractionalCount(10);
    	fractionalCurrencys1.add(fractionalCurrency11);
    	FractionalCurrency fractionalCurrency12 = new FractionalCurrency();
    	fractionalCurrency12.setCurrencyMark("分");
    	fractionalCurrency12.setCractionalCount(100);
    	fractionalCurrencys1.add(fractionalCurrency12);
    	currencyInfo1.setFractionalCurrencys(fractionalCurrencys1);
    	generalCurrencyInfos.add(currencyInfo1);

    	//美元 
    	GeneralCurrencyInfo currencyInfo2 = new GeneralCurrencyInfo();
    	currencyInfo2.setCurrencyName("美元");
    	currencyInfo2.setCountry("美国");
    	currencyInfo2.setCurrencyCapital("元");
    	currencyInfo2.setFractionalCurrency("分");
    	List<FractionalCurrency> fractionalCurrencys2 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency21 = new FractionalCurrency();
    	fractionalCurrency21.setCurrencyMark("分");
    	fractionalCurrency21.setCractionalCount(100);
    	fractionalCurrencys2.add(fractionalCurrency21);
    	currencyInfo2.setFractionalCurrencys(fractionalCurrencys2);
    	generalCurrencyInfos.add(currencyInfo2);

    	//欧元
    	GeneralCurrencyInfo currencyInfo3 = new GeneralCurrencyInfo();
    	currencyInfo3.setCurrencyName("欧元");
    	currencyInfo3.setCountry("欧洲");
    	currencyInfo3.setCurrencyCapital("元");
    	currencyInfo3.setFractionalCurrency("分");
    	List<FractionalCurrency> fractionalCurrencys3 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency31 = new FractionalCurrency();
    	fractionalCurrency31.setCurrencyMark("分");
    	fractionalCurrency31.setCractionalCount(100);
    	fractionalCurrencys3.add(fractionalCurrency31);
    	currencyInfo3.setFractionalCurrencys(fractionalCurrencys3);
    	generalCurrencyInfos.add(currencyInfo3);

    	//澳元
    	GeneralCurrencyInfo currencyInfo4 = new GeneralCurrencyInfo();
    	currencyInfo4.setCurrencyName("澳元");
    	currencyInfo4.setCountry("澳大利亚");
    	currencyInfo4.setCurrencyCapital("元");
    	currencyInfo4.setFractionalCurrency("分");
    	List<FractionalCurrency> fractionalCurrencys4 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency41 = new FractionalCurrency();
    	fractionalCurrency41.setCurrencyMark("分");
    	fractionalCurrency41.setCractionalCount(100);
    	fractionalCurrencys4.add(fractionalCurrency41);
    	currencyInfo4.setFractionalCurrencys(fractionalCurrencys4);
    	generalCurrencyInfos.add(currencyInfo4);

    	//卢布
    	GeneralCurrencyInfo currencyInfo5 = new GeneralCurrencyInfo();
    	currencyInfo5.setCurrencyName("卢布");
    	currencyInfo5.setCountry("俄罗斯");
    	currencyInfo5.setCurrencyCapital("卢布");
    	currencyInfo5.setFractionalCurrency("戈比");
    	List<FractionalCurrency> fractionalCurrencys5 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency51 = new FractionalCurrency();
    	fractionalCurrency51.setCurrencyMark("戈比");
    	fractionalCurrency51.setCractionalCount(100);
    	fractionalCurrencys5.add(fractionalCurrency51);
    	currencyInfo5.setFractionalCurrencys(fractionalCurrencys5);
    	generalCurrencyInfos.add(currencyInfo5);

    	//加元
    	GeneralCurrencyInfo currencyInfo6 = new GeneralCurrencyInfo();
    	currencyInfo6.setCurrencyName("加元");
    	currencyInfo6.setCountry("加拿大");
    	currencyInfo6.setCurrencyCapital("元");
    	currencyInfo6.setFractionalCurrency("分");
    	List<FractionalCurrency> fractionalCurrencys6 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency61 = new FractionalCurrency();
    	fractionalCurrency61.setCurrencyMark("分");
    	fractionalCurrency61.setCractionalCount(100);
    	fractionalCurrencys6.add(fractionalCurrency61);
    	currencyInfo6.setFractionalCurrencys(fractionalCurrencys6);
    	generalCurrencyInfos.add(currencyInfo6);

    	//英镑
    	GeneralCurrencyInfo currencyInfo7 = new GeneralCurrencyInfo();
    	currencyInfo7.setCurrencyName("英镑");
    	currencyInfo7.setCountry("英国");
    	currencyInfo7.setCurrencyCapital("镑");
    	currencyInfo7.setFractionalCurrency("便士");
    	List<FractionalCurrency> fractionalCurrencys7 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency71 = new FractionalCurrency();
    	fractionalCurrency71.setCurrencyMark("便士");
    	fractionalCurrency71.setCractionalCount(100);
    	fractionalCurrencys7.add(fractionalCurrency71);
    	currencyInfo7.setFractionalCurrencys(fractionalCurrencys7);
    	generalCurrencyInfos.add(currencyInfo7);

    	//泰铢
    	GeneralCurrencyInfo currencyInfo8 = new GeneralCurrencyInfo();
    	currencyInfo8.setCurrencyName("泰铢");
    	currencyInfo8.setCountry("泰国");
    	currencyInfo8.setCurrencyCapital("铢");
    	currencyInfo8.setFractionalCurrency("萨当");
    	List<FractionalCurrency> fractionalCurrencys8 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency81 = new FractionalCurrency();
    	fractionalCurrency81.setCurrencyMark("萨当");
    	fractionalCurrency81.setCractionalCount(100);
    	fractionalCurrencys8.add(fractionalCurrency81);
    	currencyInfo8.setFractionalCurrencys(fractionalCurrencys8);
    	generalCurrencyInfos.add(currencyInfo8);

    	//里拉
    	GeneralCurrencyInfo currencyInfo9 = new GeneralCurrencyInfo();
    	currencyInfo9.setCurrencyName("里拉");
    	currencyInfo9.setCountry("意大利");
    	currencyInfo9.setCurrencyCapital("里拉");
    	currencyInfo9.setFractionalCurrency("分");
    	List<FractionalCurrency> fractionalCurrencys9 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency91 = new FractionalCurrency();
    	fractionalCurrency91.setCurrencyMark("分");
    	fractionalCurrency91.setCractionalCount(100);
    	fractionalCurrencys9.add(fractionalCurrency91);
    	currencyInfo9.setFractionalCurrencys(fractionalCurrencys9);
    	generalCurrencyInfos.add(currencyInfo9);

    	//越南盾
    	GeneralCurrencyInfo currencyInfo10 = new GeneralCurrencyInfo();
    	currencyInfo10.setCurrencyName("越南盾");
    	currencyInfo10.setCountry("越南");
    	currencyInfo10.setCurrencyCapital("盾");
    	currencyInfo10.setFractionalCurrency("角、分");
    	List<FractionalCurrency> fractionalCurrencys10 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency101 = new FractionalCurrency();
    	fractionalCurrency101.setCurrencyMark("角");
    	fractionalCurrency101.setCractionalCount(10);
    	fractionalCurrencys10.add(fractionalCurrency101);
    	
    	FractionalCurrency fractionalCurrency102 = new FractionalCurrency();
    	fractionalCurrency102.setCurrencyMark("分");
    	fractionalCurrency102.setCractionalCount(100);
    	fractionalCurrencys10.add(fractionalCurrency102);
    	
    	currencyInfo10.setFractionalCurrencys(fractionalCurrencys10);
    	generalCurrencyInfos.add(currencyInfo10);

    	//瑞士法郎
    	GeneralCurrencyInfo currencyInfo11 = new GeneralCurrencyInfo();
    	currencyInfo11.setCurrencyName("瑞士法郎");
    	currencyInfo11.setCountry("瑞士");
    	currencyInfo11.setCurrencyCapital("法郎");
    	currencyInfo11.setFractionalCurrency("生丁");
    	List<FractionalCurrency> fractionalCurrencys11 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency111 = new FractionalCurrency();
    	fractionalCurrency111.setCurrencyMark("生丁");
    	fractionalCurrency111.setCractionalCount(100);
    	fractionalCurrencys11.add(fractionalCurrency111);
    	currencyInfo11.setFractionalCurrencys(fractionalCurrencys11);
    	generalCurrencyInfos.add(currencyInfo11);

    	//瑞尔
    	GeneralCurrencyInfo currencyInfo12 = new GeneralCurrencyInfo();
    	currencyInfo12.setCurrencyName("瑞尔");
    	currencyInfo12.setCountry("柬埔寨");
    	currencyInfo12.setCurrencyCapital("瑞尔");
    	currencyInfo12.setFractionalCurrency("仙");
    	List<FractionalCurrency> fractionalCurrencys12 = new ArrayList<FractionalCurrency>();
    	FractionalCurrency fractionalCurrency121 = new FractionalCurrency();
    	fractionalCurrency121.setCurrencyMark("仙");
    	fractionalCurrency121.setCractionalCount(100);
    	fractionalCurrencys12.add(fractionalCurrency121);
    	currencyInfo12.setFractionalCurrencys(fractionalCurrencys12);
    	generalCurrencyInfos.add(currencyInfo12);
    	
    	System.out.println(JSON.toJSONStringWithDateFormat(generalCurrencyInfos, "yyyy-MM-dd"));
    }
	
	public void testGetCurrencyInfoByName(){
		
    	InputStream is = GeneralCurrencyInfoTest.class.getResourceAsStream("/generalJson/currency.js");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {      
                sb.append(line);      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {       
           is.close();      
            } catch (IOException e) {      
                e.printStackTrace();      
            }   
        }
        
    	
        System.out.println(sb.toString());
    }
}
