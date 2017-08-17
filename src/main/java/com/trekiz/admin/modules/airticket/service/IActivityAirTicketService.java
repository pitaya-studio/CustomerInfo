package com.trekiz.admin.modules.airticket.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicketCost;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
/**
 * 
 * 
 * @Description:TODO
 * 
 * @author:midas
 * 
 * @time:2014-9-19 上午10:23:11
 */
public interface IActivityAirTicketService {

	public ActivityAirTicket getActivityAirTicketById(Long airTicketId);
	
	/**
	 * 根据产品ID，查询机票产品的部分信息，目前只适用于结算单
	 * @param productId    产品ID
	 * @author shijun.liu
	 * @return
	 * @date 2015.12.25
	 */
	public List<Map<String, Object>> getProductInfoForSettle(Long productId);
	
	/**
	 * 根据产品ID，查询机票产品的部分信息，目前只适用于预报单
	 * @param productId    产品ID
	 * @author shijun.liu
	 * @return
	 * @date 2015.12.25
	 */
	public List<Map<String, Object>> getProductInfoForForcast(Long productId);

	/**
	 * 分页列表查询
	 * 
	 * @Description:TODO
	 * @param page
	 * @param travelActivity
	 * @param settlementAdultPriceStart
	 * @param settlementAdultPriceEnd
	 * @param agentId
	 * @return Page<ActivityAirTicket>
	 * @exception:
	 * @author: midas
	 * @time:2014-9-19 下午12:28:47
	 */
	Page<ActivityAirTicket> findActivityAirTicketPage(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, Long companyId, DepartmentCommon common);
	
	Page<ActivityAirTicket> findAirTicketList(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, Long companyId,String orderBy);
	/*成本录入模块 机票列表*/
	Page<Map<String, Object>> findActivityAirTicketReviewPage(
			Page<Map<String, Object>> page, ActivityAirTicket airTicket, Map<String, Object> params);
	/*成本审核模块 机票列表(过时函数)*/
	Page<ActivityAirTicket> findAirTicketReviewPage(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, String review,Integer nowLevel,Long companyId,String orderBy);
	/*成本审核模块 机票列表*/
	Page<ActivityAirTicketCost> findAirCostReviewPage(
			Page<ActivityAirTicketCost> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, String review,Integer nowLevel, Long companyId,
			Long reviewCompanyId,Integer flowType,String orderBy,String createByName);

	ActivityAirTicket save(ActivityAirTicket activityAirTicket);


	void delActivityAirTicket(ActivityAirTicket activityAirTicket);

	/**
	 * 批量删除产品 创建人：liangjingming 创建时间：2014-3-3 下午2:44:19
	 * 
	 * @throws Exception
	 * 
	 */
	void batchDelActivityAirTicket(List<Long> ids);

	/**
	 * 批量上架或下架产品 创建人：liangjingming 创建时间：2014-3-3 下午2:44:19
	 * 
	 * @throws Exception
	 * 
	 */
	void batchOnOrOffActivityAirTicket(List<Long> ids, Integer product_status);

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
			ActivityAirTicket activityAirTicket, HttpServletRequest request,
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
			ActivityAirTicket activityAirTicket, String groupOpenDateBegin,
			String groupCloseDateEnd, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes);

	/**
	 * 草稿上架操作
	 */
	void batchOnActivityAirTicketTmp(List<Long> ids);
	
	public List<Map<String, Object>> findAreaIds(Long companyId);

	@SuppressWarnings("rawtypes")
    public List findActivityByPorCode(String productCode);
	/**
	 * 查询下订单机票产品分页数据
	 * @param page
	 * @param airTicket 机票产品信息
	 * @param orderBy  排序
	 * @return
	 */
	Page<ActivityAirTicket> findActivityAirTicketPageByOrder(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String orderBy, User user, String haveYw, String haveQw, String groupCodeOrActSer);
	
	/**
	 * 查询下订单机票产品分页数据(使用map封装参数)
	 * @param page
	 * @param airTicket 机票产品信息
	 * @param orderBy  排序
	 * @return
	 */
	Page<ActivityAirTicket> findActivityAirTicketPageByOrder(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			User user, Map<String,String> mapRequest);	
	
	/**
	 * 根据航空公司获取舱位等级
	 * */
	List<Map<String, String>> findAirlSpaceList(Long companyId,
			String airlineCode, String spaceLevel);

	List<Map<String, String>> findSpaceLevelList(Long companyId,
			String airlineCode);
	
	
	List<AirlineInfo> findAirlineByComid(Long companyId);

	void deleteById(Long id);

	void deleteAirlineInfoById(Long id);

	ActivityAirTicket flushSave(ActivityAirTicket activityAirTicket);

	/**
	 * 删除航段、附件、联运相关联
	 * */
	void deleteRelationByAirTicketId(Long id);
	
	/**
	 * 删除航段、附件、联运
	 * */
	void deleteAirlineInfoByAirTicketId(Long id);
	void deleteDocInfosByAirTicketId(Long id);
	void deleteIntermodalStrategiesByAirTicketId(Long id);
	
	/**
	 * 参团订单编号
	 */
	
	ProductOrder getProductById(Long id);
	
	/**
	 * 参团团号
	 */
	
	String getActivitygroupById(Long id);
	
	/**
	 * 获取机票的所有附件
	 */
	List<Map<String, String>> getDocsByAirTicketId(Long id);

	/**
	 * 根据角色ID获取部门ID,部门名称
	 */
	List<Map<String, String>> getDeptList(Long id);

	public void submitReview(Long id, Integer review);

	public void updateReview(Long id, Integer review, Integer nowLevel,CostRecordLog costRecordLog);

	public ActivityAirTicket findById(Long Id);
    /*切位--机票产品列表*/
	Page<ActivityAirTicket> findAirTicketStock(Page<ActivityAirTicket> page,
			ActivityAirTicket airTicket, String departureCity,
			String arrivedCity, BigDecimal minprice, BigDecimal maxprice,
			String airType, String startTime, String endTime, Long companyId,
			Long agentId, String orderBy);
	
	/**
	 * 获取机票订单的可切位产品
	 * @Description: 
	 * @param @param page 分页属性
	 * @param @param airTicket 机票产品信息
	 * @param @param departureCity 出发城市
	 * @param @param arrivedCity 到达城市
	 * @param @param minprice 成人最低价
	 * @param @param maxprice 成人最高价
	 * @param @param airType
	 * @param @param startTime 出团开始时间
	 * @param @param endTime 出团结束时间
	 * @param @param companyId 公司id
	 * @param @param agentId 批发商id
	 * @param @param orderBy 排序字段
	 * @param @param source 标识是切位还是返还切位调用(isReserve表示切位团期列表，isReturn表示返还切位团期列表)
	 * @param @return   
	 * @return Page<ActivityAirTicket>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-22 下午2:49:00
	 */
	public Page<ActivityAirTicket> findAirTicketStock(Page<ActivityAirTicket> page,
			ActivityAirTicket airTicket, String departureCity,
			String arrivedCity, BigDecimal minprice, BigDecimal maxprice,
			String airType, String startTime, String endTime, Long companyId,
			Long agentId, Integer freePositionStart,Integer freePositionEnd,String orderBy, String source);
	

	/**
	 * 机票产品--预报单预计收款和退款数据
	 * @param productId           产品ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
    public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId,Integer orderType);
    
    /**
	 * 机票产品--结算单收款明细和退款数据
	 * @param productId           产品ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
    public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId,Integer orderType);
    
    public List<Map<String, Object>> getRefunifoForCastList(Long productId,Integer orderType);
   
    public List<Map<String, Object>> getCost(Long activityId,Integer orderType,Integer budgetType);
    
    public List<Map<String, Object>> getHotelCost(String activityUuid,Integer budgetType);
    
    public List<Map<String, Object>> getIslandCost(String activityUuid,Integer budgetType);
    
    /**
     * 根据机票产品id获取美途收入单信息
     * @Description: 
     * @param @param airTicketId
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author majiancheng
     * @date 2015-11-11 下午12:25:23
     */
    public Map<String, Object> getMeituIncomeInfoByAirTicketId(Long airTicketId);

	void delActivity(ActivityAirTicket ticket);

	List<Traveler> getAllTraveler(AirticketOrder airticketOrder);

	public void updateEntity(ActivityAirTicket airticket);

	public Map<String, Object> getLastCount(Long id);

	public Map<String, Object> findByGoupid(Long id,String containSelf);

}
