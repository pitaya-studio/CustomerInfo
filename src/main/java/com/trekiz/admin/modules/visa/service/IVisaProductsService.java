package com.trekiz.admin.modules.visa.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.entity.VisaProductsCostView;

public interface IVisaProductsService {
	/**
	 * 分页列表查询
	 * 
	 * @Description:TODO
	 * @param page
	 * @param travelActivity
	 * @param settlementAdultPriceStart
	 * @param settlementAdultPriceEnd
	 * @param agentId
	 * @return Page<VisaProducts>
	 * @exception:
	 * @author: midas
	 * @time:2014-9-19 下午12:28:47
	 */
	Page<VisaProducts> findVisaProductsPage(Page<VisaProducts> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType,
			String orderBy, DepartmentCommon common);

	VisaProducts save(VisaProducts visaProducts);


	void delVisaProducts(VisaProducts visaProducts);

	/**
	 * 批量删除产品 创建人：liangjingming 创建时间：2014-3-3 下午2:44:19
	 * 
	 * @throws Exception
	 * 
	 */
	void batchDelVisaProducts(List<Long> ids);

	/**
	 * 批量上架或下架产品 创建人：liangjingming 创建时间：2014-3-3 下午2:44:19
	 * 
	 * @throws Exception
	 * 
	 */
	void batchOnOrOffVisaProducts(List<Long> ids, Integer product_status);

	/**
	 * 产品修改
	 * 
	 * @param introduction
	 *            产品行程介绍
	 * @param costagreement
	 *            自费补充协议
	 * @param otheragreement
	 *            其他补充协议
	 * @param otherfile
	 *            其他文件
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	String modSave(MultipartFile introduction, MultipartFile costagreement,
			MultipartFile otheragreement, List<MultipartFile> otherfile,
			List<MultipartFile> signmaterial, String groupdata,
			VisaProducts VisaProducts, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes);

	/**
	 * 产品添加
	 * 
	 * @param introduction
	 *            产品行程介绍
	 * @param costagreement
	 *            自费补充协议
	 * @param otheragreement
	 *            其他补充协议
	 * @param otherfile
	 *            其他文件
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	String save(MultipartFile introduction, MultipartFile costagreement,
			MultipartFile otheragreement, List<MultipartFile> otherfile,
			List<MultipartFile> signmaterial,
			VisaProducts VisaProducts, String groupOpenDateBegin,
			String groupCloseDateEnd, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes);

	/**
	 * 草稿上架操作
	 */
	void batchOnVisaProductsTmp(List<Long> ids);
	
	List<Map<String, Object>> findAreaIds(Long companyId);
	
	/**
	 * 显示某个签证详细信息
	 */
	VisaProducts findByVisaProductsId(Long visaProductId);
	
	/**
	 * 根据产品ID 查询签证产品的部分信息，只适用于结算单
	 * @param productId
	 * @author shijun.liu
	 * @return
	 * @date 2015.12.25
	 */
	public List<Map<String, Object>> getProductInfoForSettle(Long productId);
	
	/**
	 * 根据产品ID 查询签证产品的部分信息，只适用于预报单
	 * @param productId
	 * @author shijun.liu
	 * @return
	 * @date 2015.12.25
	 */
	public List<Map<String, Object>> getProductInfoForForcast(Long productId);
	
	/**
	 * 签证产品--预报单预计收款和退款
	 * @param productId           产品ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId,Integer orderType);
	
	/**
	 * 签证产品--结算单收款明细和退款
	 * @param productId           产品ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId,Integer orderType);
	
	List<Object[]> findCountryInfoList();
	
	List<Object[]> findVisaCountryArea(Integer countryId);

	List<Object> findFileListByProId(Long visaProdectsId, boolean isDownLoad);
	
	/**
	 * 同一个国家和领区只能发一种类型的签证产品
	 * @author jiachen
	 * @DateTime 2014-12-5 上午10:52:40
	 * @return List<VisaProducts>
	 */
	String findMoreProduct(Integer sysCountry, String collarZoning, Integer visaType, Long proId, String deptId,String groupCode);
	
	/**
	 * 根据 签证国家，签证类型，签证领区 
	 * 
	 * @param countryId
	 * @param visaType
	 * @param collarZoning
	 * @return
	 */
	public  VisaProducts  findVisaProductsByCountryTypeCollarZonID(Integer countryId, Integer visaType, String collarZoning);
     /*成表录入模块 记录列表*/
	Page<Map<String, Object>> findVisaProductsReviewPage(Page<Map<String, Object>> page,
			VisaProducts visaProducts, Map<String, Object> params);
	 /*成表审核模块 记录列表*/
	Page<VisaProducts> findVisaReviewPage(Page<VisaProducts> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType, String review,
			Integer nowLevel,Long companyId, String orderBy);
	
	Page<VisaProducts> findVisaProductsPage4PreOrder(Page<VisaProducts> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType,
			String orderBy, DepartmentCommon common);
	Page<VisaProductsCostView> findVisaCostViewPage(Page<VisaProductsCostView> page,
			VisaProducts visaProducts, String productName, String collarZoning,
			String sysCountryId, String visaType, String review,Integer nowLevel,Long companyId,
			Long reviewCompanyId,Integer supplyId,Integer agentId,Integer flowType,String orderBy,String createByName) ;

	VisaProducts findById(Long id);

	void delActivity(VisaProducts visaProducts);


    VisaProducts getVisaProductById(Long visaProductId);
}
