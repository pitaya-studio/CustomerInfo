package com.trekiz.admin.modules.distribution.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Decoder.BASE64Encoder;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.distribution.service.DistributionService;
import com.trekiz.admin.modules.distribution.service.QrCodeService;
import com.trekiz.admin.modules.distribution.util.QRCodeUtils;
import com.trekiz.admin.modules.distribution.util.ShortUrlUtils;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.service.VisaService;

/**
 * 微信分销模块Controller
 * @date 2017.1.13
 * */
@Controller
@RequestMapping(value = "${adminPath}/weixin/distribution")
public class DistributionController {

	@Autowired
	private DistributionService distributionService;
	@Autowired
	private IActivityAirTicketService iActivityAirTicketService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private QrCodeService qrCodeService;
	@Autowired
	private VisaService visaService;
	
	/**
	 * 初始化，处理所有日期格式参数。格式为 yyyy-MM-dd
	 * @author yang.wang 
	 * @date 2017.1.13
	 * */
	@InitBinder
	public void initBinder(WebDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 生成二维码通过部分团期ID
	 * @author yang.wang
	 * @date 2017.1.12
	 * */
	@ResponseBody
	@RequestMapping(value = "generateQrCode")
	public void generateQrCode(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> activityGroupList = new ArrayList<>();
		// 团期Ids，以","分隔，末尾无","
		String groupIds = request.getParameter("activityIds");
		String isMulti = request.getParameter("isMulti");
		if (StringUtils.isNotBlank(groupIds)) {
			activityGroupList = distributionService.getDistributionActivityGroupList(groupIds);
		}
		String url = qrCodeService.generateQrCodeInfo4Batch(activityGroupList, Boolean.valueOf(isMulti));
		// 长连接转为短连接
		url = ShortUrlUtils.getShortUrlByBaiduApi(url);
		try {
			OutputStream output= response.getOutputStream(); 
			ByteArrayOutputStream out = new ByteArrayOutputStream(); 
			QRCodeUtils.encoderQRCode(url, out, "png", 5);
			BASE64Encoder encoder = new BASE64Encoder();
			byte[] decodedBytes = out.toByteArray();	
			output.write(encoder.encode(decodedBytes).getBytes());
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 生成二维码通过全部团期ID
	 * @author yang.wang
	 * @date 2017.1.12
	 * */
	@ResponseBody
	@RequestMapping(value = "generateQrCodeByAllId")
	public void generateQrCodeByAllId(HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute TravelActivity travelActivity) {
		
		Map<String, String> paramMap = new HashMap<>();
		String params = "wholeSalerKey,createName,settlementAdultPriceStart,settlementAdultPriceEnd,currencyType";
		OrderCommonUtil.handlePara(params, paramMap, request);
		
		Page<Map<String, Object>> page = new Page<>(request, response);
		page.setMaxSize(Integer.MAX_VALUE);	// 设置最大值，取消分页条数限制
		// 获取筛选后的团期
		page = distributionService.findActivityGroupList(page, travelActivity, paramMap);
		String url = qrCodeService.generateQrCodeInfo4Batch(page.getList(), true);
		// 长连接转为短连接
		url = ShortUrlUtils.getShortUrlByBaiduApi(url);
		
		try {
			OutputStream output= response.getOutputStream(); 
			ByteArrayOutputStream out = new ByteArrayOutputStream(); 
			QRCodeUtils.encoderQRCode(url, out, "png", 5);
			BASE64Encoder encoder = new BASE64Encoder();
			byte[] decodedBytes = out.toByteArray();
			output.write(encoder.encode(decodedBytes).getBytes());
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 分销产品列表
	 * @author yang.wang
	 * @date 2017.1.12
	 * */
	@RequestMapping(value = "productList")
	public String productList(@ModelAttribute TravelActivity travelActivity, Model model, 
			HttpServletRequest request, HttpServletResponse response) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 团期列表/产品列表
		String productOrGroup = request.getParameter("productOrGroup");
		if (StringUtils.isBlank(productOrGroup)) {
			productOrGroup = "product";
		}
		model.addAttribute("productOrGroup", productOrGroup);
		
		// 查询条件map
		Map<String, String> paramMap = new HashMap<>();
		String params = "wholeSalerKey,createName,settlementAdultPriceStart,settlementAdultPriceEnd,currencyType";
		OrderCommonUtil.handlePara(params, paramMap, model, request);
		model.addAttribute("travelActivity", travelActivity);
		
		// 产品或团期列表查询
		if ("product".equals(productOrGroup)) {
			Page<TravelActivity> page = new Page<TravelActivity>(request, response);
			// 排序，默认为出团日期排序
			String orderBy = request.getParameter("orderBy");
			if (StringUtils.isBlank(orderBy)) {
				orderBy = "groupOpenDate DESC";
				page.setOrderBy(orderBy);
			}
			page = distributionService.findTravelActivityList(page, travelActivity, paramMap);
			model.addAttribute("page", page);
			
			// 拼接groupIds 用于单个产品分销其下全部团期 modify by yang.wang 2017.1.12
			List<String> groupIdsList = new ArrayList<>(); // 团期idsList
			List<Long> ids = new ArrayList<>();	// 产品ids
			for (TravelActivity activity : page.getList()) {
				List<String> groupIdArr = new ArrayList<>(); 
				for (ActivityGroup group : activity.getActivityGroupList()) {
					groupIdArr.add(group.getId().toString());
				}
				String groupIds = StringUtils.join(groupIdArr, ",");
				groupIdsList.add(groupIds);
				ids.add(activity.getId());
			}
			// 产品列表--产品下团期ids，用于生成单一产品二维码
			model.addAttribute("groupIds", groupIdsList);
			// 产品列表--签证列数据
			List<Map<String,Object>> visaMapList = visaService.findVisasNew(ids);
			model.addAttribute("visaMapList", visaMapList);
		} else {
			Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
			// 排序，默认为出团日期排序
			String orderBy = request.getParameter("orderBy");
			if (StringUtils.isBlank(orderBy)) {
				orderBy = "groupOpenDate DESC";
				page.setOrderBy(orderBy);
			}
			page = distributionService.findActivityGroupList(page, travelActivity, paramMap);
			model.addAttribute("page", page);
		}
		
		// 出发地下拉列表
		model.addAttribute("fromAreas", DictUtils.findUserDict(companyId, "fromarea"));
		// 目的地
		model.addAttribute("targetAreaNames", request.getParameter("targetAreaNameList"));
		// 航空公司
		model.addAttribute("trafficNames", iActivityAirTicketService.findAirlineByComid(companyId));
		// 同行价币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		// 同行价币种Id
//		model.addAttribute("currencyId", request.getParameter("currencyType"));
		// 旅游类型
//		model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type", companyId));
		// 产品系列
//		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level", companyId));
		// 产品类型
//		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type", companyId));
		
		return "modules/distribution/productList";
	}
}
