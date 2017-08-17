package com.trekiz.admin.modules.statistics.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statistics.product.bean.ProductCount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.statistics.product.bean.ProductInfo;
import com.trekiz.admin.modules.statistics.product.repository.ProductStatisticsDao;
import com.trekiz.admin.modules.statistics.product.service.ProductStatisticsService;
import com.trekiz.admin.modules.statistics.utils.ExcelUtils;

/**
 * 产品数据统计的实体类
 * @author shijun.liu
 *
 */
@Service
public class ProductStatisticsServiceImpl implements ProductStatisticsService{

	private static Log log = LogFactory.getLog(ProductStatisticsServiceImpl.class);
	
	@Autowired
	private ProductStatisticsDao productStatisticsDao;

	@Override
	public Workbook getAllProductInfo() {
		List<ProductInfo> allList = new ArrayList<ProductInfo>();
		//机票产品有往返或者多段的情况时，数据是重复的，只有航空信息，舱型，出发机场，到达机场，出发时间和到达时间不一致，需要做合并处理
		List<ProductInfo> needDel = new ArrayList<ProductInfo>();
		//多段或者往返的相同机票产品进行合并
		Map<String,ProductInfo> mergeList = new HashMap<String,ProductInfo>();
		List<ProductInfo> list1_1 = productStatisticsDao.getAllProductInfoFor1_1();
		List<ProductInfo> list1_5 = productStatisticsDao.getAllProductInfoFor1_5();
		if(null != list1_5){
			for (ProductInfo p:list1_5) {
				String airType = p.getAirType();
				String orderType = p.getOrderType();
				if("7".equals(orderType) && ("多段".equals(airType) || "往返".equals(airType))){
					needDel.add(p);
					String key = p.getProductId() + p.getCompanyName() +p.getVersion();
					ProductInfo exists = mergeList.get(key);
					if(null == exists){
						mergeList.put(key, p);
					}else{
						//航空公司
						String airCompany = exists.getAirCompany() + "," + p.getAirCompany();
						//舱型
						String airSpace = exists.getAirSpace() + "," + p.getAirSpace();
						//出发机场
						String leaveAirport = exists.getLeaveAirport() + "\n" + p.getLeaveAirport();
						//到达机场
						String arrivedAirport = exists.getArrivedAirport() + "\n" + p.getArrivedAirport();
						//出发时间
						String startTime = exists.getStartTime() + "\n" + p.getStartTime();
						//到达时间
						String arrivalTime = exists.getArrivalTime() + "\n" + p.getArrivalTime();
						exists.setAirCompany(airCompany);
						exists.setAirSpace(airSpace);
						exists.setLeaveAirport(leaveAirport);
						exists.setArrivedAirport(arrivedAirport);
						exists.setStartTime(startTime);
						exists.setArrivalTime(arrivalTime);
					}
				}
			}
			list1_5.removeAll(needDel);
			Set<Entry<String, ProductInfo>> set = mergeList.entrySet();
			Iterator<Entry<String, ProductInfo>> it = set.iterator();
			while(it.hasNext()){
				Entry<String, ProductInfo> entry = it.next();
				list1_5.add(entry.getValue());
			}
		}
		allList.addAll(list1_1);
		allList.addAll(list1_5);
		Workbook workbook = ExcelUtils.createProductExcel(allList);
		return workbook;
	}

	/**
	 * 指定时间区间内，统计所有公司创建产品的数量.
	 */
	public Workbook getProductSumPerOffice(String beginDate, String endDate){
		if(StringUtils.isBlank(beginDate)){
			beginDate = Context.DEFAULT_BEGIN_TIME;
		}else{
			beginDate = beginDate + " 00:00:00";
		}
		if(StringUtils.isBlank(endDate)){
			endDate = Context.DEFAULT_END_TIME;
		}else{
			endDate = endDate + " 23:59:59";
		}
		List<ProductCount> result = productStatisticsDao.getProductSumPerOffice(beginDate,endDate);
		return ExcelUtils.createProductCountWB(result);
	}
	
}
