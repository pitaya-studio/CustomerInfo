package com.trekiz.admin.modules.airticket.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
/**
 * 改价的HTML生成器帮助工具类
 * @author HPT
 *
 */
public final class UPPricesGenerateHTML { 
	private UPPricesGenerateHTML(){}
	private static String defCurrenyName = "人民币"; // 默认币种
	private static String defMoneny ="0.00"; // 默认金额
	
	/**
	 * 生成展示游客列表的HTML元素
	 * @param travelers 游客列表
	 * @return HTML元素
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public static String generateTravelers(List<Map<String,Object>> travelers) throws RuntimeException{
		StringBuilder sbuilder = new StringBuilder();
		for(int i =0;i<travelers.size();i++){
			Map rawMap = travelers.get(i); // 游客行级内容
			List<Map<String,Object>> moneys = (List<Map<String, Object>>) rawMap.get("travelers"); // 游客币种
			sbuilder.append("<!-- 生成展示游客列表的HTML元素 开始-->");
			StringBuilder firstSbuilder = new StringBuilder();
			StringBuilder htmlSbuilder = new StringBuilder();
			// moneys.size() --2
			if(moneys.size()>0){
				sbuilder.append("<tr group='travler"+i+"'> <td rowspan='"+moneys.size()+"'> "+rawMap.get("travelername")+" </td> ");
				
				
				
				for(int j=0;j<moneys.size();j++){
					Map<String,Object> temp = moneys.get(j); // 游客币种内容
					if(j == 0){ // 追加第一行的币种
 						firstSbuilder.append("");
 						firstSbuilder.append("<td><input type='hidden' name='travelerids' value='"+rawMap.get("travelerid")+"'/><input type='hidden' name='gaijiaCurency' value='"+temp.get("currency_id")+"'/><span name='gaijiaCurency'>"+temp.get("currency_name")+"</span></td>");
 						firstSbuilder.append("<td class='tr'>"+CurrencyUtils.getCurrencyInfo(temp.get("currency_id").toString(), 0, "mark") + "<span class='tdorange fbold'> "+temp.get("oldtotalmoney")+"</span></td>");
 						firstSbuilder.append("<td class='tr'>"+CurrencyUtils.getCurrencyInfo(temp.get("currency_id").toString(), 0, "mark") + "<span class='tdorange fbold' flag='beforeys'> "+temp.get("amount")+"</span></td>");
 						firstSbuilder.append("<td class='tc'><dl class='huanjia'><dt><input name='plusys' maxlength='15' type='text' class='' value='0.00' defaultValue='0.00' " +
								" onkeyup='validNum(this)' onafterpaste='validNum(this))' /></dt><dd><div class='ydbz_x'" +
								" flag='appAll'>应用全部</div><div class='ydbz_x gray' flag='reset'>还原</div></dd></dl></td>");
 						//firstSbuilder.append("<td class='tc'><input name='travelerremark' type='text' value='' /></td>");
 						firstSbuilder.append("<td class='tc'><textarea name='travelerremark' cols='180' rows='1' onclick=" + '"' + "this.innerHTML=''" + '"' + ">备注</textarea></td>");
 						firstSbuilder.append("<td class='tr'>"+CurrencyUtils.getCurrencyInfo(temp.get("currency_id").toString(), 0, "mark") + "<span class='tdorange fbold' flag='afterys'> "+temp.get("amount")+"</span></td>");
 						firstSbuilder.append("</tr>");
 						// 第一行 币种追加完毕
					}else{
						// 追加下行的币种信息
						htmlSbuilder.append("<tr group='travler"+i+"'>");
						
						htmlSbuilder.append("<td><input type='hidden' name='travelerids' value='"+rawMap.get("travelerid")+"'/><input type='hidden' name='gaijiaCurency' value='"+temp.get("currency_id")+"'/><span name='gaijiaCurency'>"+temp.get("currency_name")+"</span><div class='pr'></div></td>");
						htmlSbuilder.append("<td class='tr'>"+CurrencyUtils.getCurrencyInfo(temp.get("currency_id").toString(), 0, "mark") + " <span class='tdorange fbold'>"+temp.get("oldtotalmoney")+"</span></td>");
						htmlSbuilder.append("<td class='tr'>"+CurrencyUtils.getCurrencyInfo(temp.get("currency_id").toString(), 0, "mark") + " <span class='tdorange fbold' flag='beforeys'> "+temp.get("amount")+"</span></td>");
						htmlSbuilder.append("<td class='tc'><dl class='huanjia'><dt><input name='plusys' maxlength='15' type='text' class='' value='0.00' defaultValue='0.00' " +
								" onkeyup='validNum(this)' onafterpaste='validNum(this))' /></dt><dd><div class='ydbz_x'" +
								" flag='appAll'>应用全部</div><div class='ydbz_x gray' flag='reset'>还原</div></dd></dl></td>");
						//htmlSbuilder.append("<td class='tc'><input name='travelerremark' type='text' value='' /></td>");
						htmlSbuilder.append("<td class='tc'><textarea name='travelerremark' cols='180' rows='1' onclick=" + '"' + "this.innerHTML=''" + '"' + ">备注</textarea></td>");
						htmlSbuilder.append("<td class='tr' >"+CurrencyUtils.getCurrencyInfo(temp.get("currency_id").toString(), 0, "mark") + "<span class='tdorange fbold' flag='afterys'> "+temp.get("amount")+"</span></td>");
						
						htmlSbuilder.append("</tr>");
					}
					
				}
				sbuilder.append(firstSbuilder);
				sbuilder.append(htmlSbuilder);
				// 多币种情况 追加完毕...
				
			}else{
				// 没有币种的情况...   使用默认值
				
				sbuilder.append("<tr group='travler"+i+"'> <td rowspan='"+(moneys.size()+1)+"'> "+rawMap.get("travelername")+" </td> ");
				firstSbuilder.append("<td><input type='hidden' name='travelerids' value='"+rawMap.get("travelerid")+"'/><input type='hidden' name='gaijiaCurency' value='1'/><span name='gaijiaCurency'>"+defCurrenyName+"</span></td>");
				firstSbuilder.append("<td class='tr'>"+defMoneny+"</td>");
				firstSbuilder.append("<td class='tr' flag='beforeys'>"+defMoneny+"</td>");
				firstSbuilder.append("<td class='tc'><dl class='huanjia'><dt><input name='plusys' maxlength='15' type='text' class='' value='0.00' defaultValue='0.00' " +
						" onkeyup='validNum(this)' onafterpaste='validNum(this))' /></dt><dd><div class='ydbz_x'" +
						" flag='appAll'>应用全部</div><div class='ydbz_x gray' flag='reset'>还原</div></dd></dl></td>");
				//firstSbuilder.append("<td class='tc'><input name='travelerremark' type='text' value='' /></td>");
				firstSbuilder.append("<td class='tc'><textarea name='travelerremark' cols='180' rows='1' onclick=" + '"' + "this.innerHTML=''" + '"' + ">备注</textarea></td>");
				firstSbuilder.append("<td class='tr' flag='afterys'>"+defMoneny+"</td>");
				sbuilder.append(firstSbuilder);
				sbuilder.append("</tr>");
				
				
			}
			
		}
		return sbuilder.toString();
	}

	/**
	 * 获取当前登陆用户的币种
	 * @param currencyService
	 * @return
	 */
    public static Map<String, String> getCurrencyParam(CurrencyService currencyService){
		Map<String, String> currencyTypes = Maps.newHashMap(); 
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		for(Currency currency : currencylist){
			currencyTypes.put(currency.getId().toString(),currency.getCurrencyName() );
		}
		return currencyTypes;
	}
	
}
