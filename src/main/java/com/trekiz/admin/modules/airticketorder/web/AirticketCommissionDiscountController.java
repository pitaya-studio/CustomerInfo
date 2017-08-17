package com.trekiz.admin.modules.airticketorder.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.service.CurrencyService;

@Controller
@RequestMapping(value = "${adminPath}/order/comdiscount")
public class AirticketCommissionDiscountController {

	@Autowired
	private RebatesService rebatesService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
    private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private ActivityGroupService activityGroupService;
	
	

	/**
	 * 跳转到返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showComdiscountList")
	public String showComdiscountList(Model model, HttpServletRequest request){
		Long orderId = Long.parseLong(request.getParameter("orderId"));
		int orderType = Integer.parseInt(request.getParameter("productType"));
	
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", orderType);
		List<Rebates> rebatesList = rebatesService.findRebatesListAir(orderId, orderType);
		//Map<Long,List<Rebates>> groupMap1 = new TreeMap<Long,List<Rebates>>();
		//指定排序器  
        /* 
         * int compare(Object o1, Object o2) 返回一个基本类型的整型， 
         * 返回负数表示：o1 小于o2， 
         * 返回0 表示：o1和o2相等， 
         * 返回正数表示：o1大于o2。 
         */  
        TreeMap<Long,List<Rebates>> groupMap1 = new TreeMap<Long,List<Rebates>>(new Comparator<Long>(){  
            public int compare(Long o1, Long o2) {
            	//指定排序器按照降序排列  
                return o2.compareTo(o1);  
            }     
        });
		
		for(Rebates rb:rebatesList){
			if(groupMap1.containsKey(rb.getRid())){
				groupMap1.get(rb.getRid()).add(rb);
			}else{
				List<Rebates> list = new ArrayList<Rebates>();
				list.add(rb);
				groupMap1.put(rb.getRid(), list);
			}
		}
		List<Rebates> result = new ArrayList<Rebates>();
		for(Map.Entry<Long, List<Rebates>> superEntry : groupMap1.entrySet()){
			String currencyName = "";
			String rebatesDiff = "";			
			
			//如果多条游客或团队记录，则默认是团队类型 
			if(superEntry.getValue().size()>1){
				superEntry.getValue().get(0).setTraveler(null);
			}
			result.add(superEntry.getValue().get(0));
			Map<Long, List<Rebates>> groupMap = new HashMap<Long, List<Rebates>>();
			for(Rebates rebate:superEntry.getValue()){
 				if(groupMap.containsKey(rebate.getCurrencyId())) {
	                groupMap.get(rebate.getCurrencyId()).add(rebate);
	            } else {
 	                List<Rebates> glist = new ArrayList<Rebates>();
	                glist.add(rebate);
	                groupMap.put(rebate.getCurrencyId(), glist);
	            }
			}
			List<String> molist = Lists.newArrayList();
			List<String> newMolist = Lists.newArrayList();
			for (Map.Entry<Long, List<Rebates>> childEntry : groupMap.entrySet()) {
	            BigDecimal amount = new BigDecimal("0");
	            currencyName += childEntry.getKey()+ ",";
	            List<Rebates> ls=childEntry.getValue(); 
	            for(Rebates reb : ls ){
	                amount = amount.add(reb.getRebatesDiff());
	                newMolist.add(reb.getOldRebates());
	                molist.add(reb.getTotalMoney());
	            }
	            rebatesDiff +=amount +",";
	            
	        }
			
			String rcn = currencyName.substring(0, currencyName.length() - 1).toString();
			String rbs = rebatesDiff.substring(0, rebatesDiff.length() - 1).toString();
			
			superEntry.getValue().get(0).setRebatesdiffCurrName(rcn);
			superEntry.getValue().get(0).setRebatesdiffString(rbs);
			
			String newMonry = moneyAmountService.getMoneyStr(newMolist);
			String totalMoney = moneyAmountService.getMoneyStr(molist);
			//结算价总价
			superEntry.getValue().get(0).setRebatesdiffString2(totalMoney);
			superEntry.getValue().get(0).setCostname("机票返佣");
			//累计返佣金额
			superEntry.getValue().get(0).setRebatesdiffString1(newMonry);
//			Currency currency = null;
			String allCumulative =superEntry.getValue().get(0).getAllCumulative();
			if(null!=allCumulative && !("").equals(allCumulative)){
				String[] arry = allCumulative.split("\\+");
		        Map<String, Double> map = new HashMap<String, Double>();
		        Map<String, String> map2 = new HashMap<String, String>();
		        for (String str : arry)
		        {
		            String[] s = str.split("\\:");
		            if (map.containsKey(s[0]))
		            {
//		            	currency=currencyService.findCurrency(Long.parseLong(s[0].toString()));
		                map.put(s[0], map.get(s[0]) + Double.valueOf(s[1]));
		            }
		            else
		            {
//		            	currency=currencyService.findCurrency(Long.parseLong(s[0].toString()));
		                map.put(s[0], Double.valueOf(s[1]));
		            }
			    }
		        Double val;
		        String name;
		        Set<String> keySet = map.keySet();
		        DecimalFormat myformat = new DecimalFormat();
				myformat.applyPattern("##,###.00");
		        
		        for (String  key : keySet) {
					val = map.get(key);
					name = currencyService.findCurrency(Long.parseLong(key)).getCurrencyName();
					map2.put(name, myformat.format(val));
				}
			    String mt = map2.toString();
			    mt=mt.replaceAll("\\=", " ");
			    mt=mt.substring(1, mt.length()-1);
			    superEntry.getValue().get(0).setAllCumulative(mt);
			}else{
				superEntry.getValue().get(0).getAllCumulative();
			}
		}
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId.toString());
		String totalmoney =  orderDetail.get("total_money").toString();
		String tuantotalmoney = moneyAmountService.getMoneyCurrenName(totalmoney);
		model.addAttribute("tuantotalmoney", tuantotalmoney);
		
		//当前订单所有游客的返佣最新记录 
		List<Rebates> teamList = rebatesService.getRebates(orderId);
		Map<Long,Rebates> rebatesMap = new HashMap<Long, Rebates>();
		for(Rebates rb :teamList){
			rebatesMap.put(rb.getTravelerId(), rb);
		}
		
		model.addAttribute("rebatesMap", rebatesMap);
		model.addAttribute("rebatesList", result);
		return "modules/order/comdiscount/airticketcomborrowAmountList";
	}

	/**
	 * 取消申请
	 * @param response
	 * @param request
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value="cancleRebates")
	public String cancleRebates(HttpServletResponse response, HttpServletRequest request){
		try{
			Long id = Long.parseLong(request.getParameter("rid"));
			Review r = reviewService.findReviewInfo(id);
			if (null != r && 1 == r.getStatus().intValue()) {
				reviewService.removeReview(id);
			}
			
		}catch(Exception e){
			e.printStackTrace();  
			return "error";
		}
		return "success";
	}
	
	
	/**
	 * 跳转到返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showcomdiscountDetail/{rebatesId}")
	public String showcomdiscountDetail(@PathVariable Long rebatesId, Model model, HttpServletRequest request){
		Rebates rebates = rebatesService.findRebatesById(rebatesId);
		ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(rebates.getOrderId().toString());
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rebates", rebates);
		List<ReviewLog> reviewLogList =  reviewService.findReviewLog(rebates.getReview().getId());
		model.addAttribute("reviewLogList", reviewLogList);
		return "modules/order/comdiscount/airticketComdisCountDetail";
	}
	

}
