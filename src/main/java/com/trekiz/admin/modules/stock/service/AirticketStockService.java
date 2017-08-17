package com.trekiz.admin.modules.stock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrder;
import com.trekiz.admin.modules.activity.repository.ActivityReserveOrderDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.entity.AirticketReserveFile;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.stock.repository.AirticketReserveFileDao;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.temp.stock.dao.AirticketreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;

/**
 * Created by ZhengZiyu on 2014/11/4.
 */
@Service
public class AirticketStockService extends BaseService {

    @Autowired
    private IAirTicketOrderService iAirTicketOrderService;

    @Autowired
    private AirticketActivityReserveDao airticketActivityReserveDao;
    
	@Autowired
	private IStockDao stockDao;
    @Autowired
	private ActivityReserveOrderDao activityReserveOrderDao;

    @Autowired
    private AirticketReserveFileDao airticketReserveFileDao;

    @Autowired
    private IActivityAirTicketService iActivityAirTicketService;

    @Autowired
    private DocInfoDao docInfoDao;
    
    @Autowired
    private ActivityAirTicketDao activityAirTicketDao;
    
	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private AirticketreservefileTempDao airticketreservefileTempDao;
    /**
     * 查询团期已占位明细或售出占位明细
     * @param id 团期ID
     * @param status 已占位或售出占位
     * @return
     */
    @Transactional(readOnly = false,rollbackFor={ServiceException.class})
    public List<List<Map<String,Object>>> findProductGroupOrders(HttpServletRequest request, HttpServletResponse response, Long id, String status) {

        User userInfo = UserUtils.getUser();
        List<Role> roleList = userInfo.getRoleList();

        Map<String, Object> roleMap = new HashMap<String,Object>();
        for(Role role : roleList){
            roleMap.put(role.getRoleType(), role.getDepartment());
        }
        Map<String, Object> conMap = new HashMap<String, Object>();
        conMap.put("airticket_id", id.toString());
        Page<Map<String,Object>> page = iAirTicketOrderService.queryAirticketOrderListByCond(request,response, UserUtils.getUser(), conMap, roleMap);
        page.setPageSize(9999);
        List<List<Map<String, Object>>> msgList = new ArrayList<List<Map<String, Object>>>();
        List<Map<String, Object>> productListPosition = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> productList = page.getList();  
       
        if(productList != null) {
        	
        	Iterator<Map<String, Object>> it = productList.iterator();
            //如果status为空，则表示售出占位，不为空则表示已占位
            if(StringUtils.isEmpty(status)) {//售出占位
            	
                while(it.hasNext()) {
                    Map<String, Object> p = it.next();                  
                    String placeHolderType = p.get("place_holder_type") != null ? p.get("place_holder_type").toString() : "0";//占位类型：1为切位，0或空表示占位
                   
                    if(placeHolderType == null || "0".equals(placeHolderType)) {//占位
                        String payStatus = p.get("order_state") != null ? p.get("order_state").toString() : null;
                        if("4".equals(payStatus) || "5".equals(payStatus)) {
                            productListPosition.add(p);
                        }
                    }
                }
            } else {//已占位明细
                //把切位订单排除在外
            	
                while(it.hasNext()) {
                    Map<String, Object> p = it.next();
                    String placeHolderType = p.get("place_holder_type") != null ? p.get("place_holder_type").toString() : null;//占位类型：1为切位，0或空表示占位
                    String payStatus = p.get("order_state") != null ? p.get("order_state").toString() : null;
                    if(!"1".equals(placeHolderType) && ("3".equals(payStatus) || "4".equals(payStatus) || "5".equals(payStatus))) {//切位
                    	productListPosition.add(p);
                    }
                }
            }
            
            if(productListPosition.size() > 1) {
                List<Map<String, Object>> arrDataList = new ArrayList<Map<String, Object>>();
                arrDataList.add(productListPosition.get(0));               
                for(int i = 0; i<productListPosition.size()-1; i++) {
                    Map<String, Object> order = productListPosition.get(i + 1);                 
                   
                    if(order.get("place_holder_type") == null || "0".equals(order.get("place_holder_type").toString())) {//占位
                    	if(productListPosition.get(i).get("agentName") != null && productListPosition.get(i).get("agentName").equals(order.get("agentName"))) {
                        	
                        	arrDataList.add(productListPosition.get(i+1));
                        } else {
                        	
                            msgList.add(arrDataList);
                            arrDataList = new ArrayList<Map<String, Object>>();
                            arrDataList.add(productListPosition.get(i+1));
                        }
                    }
                }
                msgList.add(arrDataList);
                
            } else {
                msgList.add(productListPosition);
            }
        }
        return msgList;
    }
    
	public List<Map<String, Object>> findAirReserveOrder(Long activityGroupId,Long agentId){
		return stockDao.findAirReserveOrder(activityGroupId,agentId);
	}
	
	public List<Map<String, Object>> findAirSoldNopayPosition(Long activityGroupId){
		return stockDao.findAirSoldNopayPosition(activityGroupId.toString());
	}
	
    /*
    @Transactional(readOnly = false,rollbackFor={ServiceException.class})
    public List<List<Map<String,Object>>> findSoldNopayPosition(HttpServletRequest request, HttpServletResponse response, String airticket_id) {

        User userInfo = UserUtils.getUser();
        List<Role> roleList = userInfo.getRoleList();    
       
        Page<Map<String,Object>> page = iAirTicketOrderService.findSoldNopayPosition(request,response, airticket_id);
        page.setPageSize(9999);
        List<List<Map<String, Object>>> msgList = new ArrayList<List<Map<String, Object>>>();
        List<Map<String, Object>> productListPosition = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> productList = page.getList();  
       
        if(productList != null) {
        	
        	Iterator<Map<String, Object>> it = productList.iterator();
            //如果status为空，则表示售出占位，不为空则表示已占位
           
            	System.out.println("SS:"+productList.size());
                while(it.hasNext()) {
                    Map<String, Object> p = it.next();               
                    String payStatus = p.get("order_state") != null ? p.get("order_state").toString() : null;
                   // if(!"1".equals(payStatus) && !"2".equals(payStatus) && !"99".equals(payStatus) && !"111".equals(payStatus)) {
                            productListPosition.add(p);                    
                   // }
                }
           
            
            if(productListPosition.size() > 1) {
                List<Map<String, Object>> arrDataList = new ArrayList<Map<String, Object>>();
                arrDataList.add(productListPosition.get(0));               
                for(int i = 0; i<productListPosition.size()-1; i++) {
                    Map<String, Object> order = productListPosition.get(i + 1);                
                   
                    	if(productListPosition.get(i).get("agentName") != null && productListPosition.get(i).get("agentName").equals(order.get("agentName"))) {
                        	
                        	arrDataList.add(productListPosition.get(i+1));
                        } else {
                        	
                            msgList.add(arrDataList);
                            arrDataList = new ArrayList<Map<String, Object>>();
                            arrDataList.add(productListPosition.get(i+1));
                        }
                   
                }
                msgList.add(arrDataList);
                
            } else {
                msgList.add(productListPosition);
            }
        }
        return msgList;
    } */

    public List<Map<String, Object>> findSoldNopayPosition(Long activityId){
        return airticketActivityReserveDao.findSoldNopayPosition(activityId);
    }

    public List<Map<String, Object>> findSoldNopayPosition(Long activityId, Long agentId){
        return airticketActivityReserveDao.findSoldNopayPosition(activityId, agentId);
    }

    public List<AirticketActivityReserve> findReserve(Long agentId, Long airticketId) {
        return airticketActivityReserveDao.findByAgentIdAndActivityId(agentId, airticketId);
    }

    public List<AirticketActivityReserve> findReserve(Long activityId) {
        return airticketActivityReserveDao.findByActivityId(activityId);
    }

    public List<AirticketReserveFile> findByAgentIdAndSrcActivityId(Long agentId, Long airticketActivityId) {
        return airticketReserveFileDao.findByAgentIdAndAirticketActivityId(agentId, airticketActivityId);
    }
 
    @Transactional(readOnly = false,rollbackFor={ServiceException.class})
    public Long saveAirticketReserveList(List<AirticketActivityReserve> activityGroupReserveList, List<ActivityReserveOrder> activityReserveOrderList, HttpServletRequest request) throws Exception {
        for(AirticketActivityReserve groupreserve : activityGroupReserveList){
            Long id = groupreserve.getId();          
            Long airticketId = groupreserve.getActivityId();
            //要切位的数目 
            int addreserve = groupreserve.getPayReservePosition();
          
            AirticketActivityReserve groupreserveOld = null;
            if(id != null) groupreserveOld = airticketActivityReserveDao.findOne(id);
            if(groupreserveOld!=null){
                int newLeftpayReservePosition=addreserve + groupreserveOld.getLeftpayReservePosition();
                //新的切位总数=要切位的数目 + 原来的切位数目
                int newPayReservePosition=addreserve + groupreserveOld.getPayReservePosition();
                BigDecimal newFrontMoney=groupreserveOld.getFrontMoney().add(groupreserve.getFrontMoney());
               
                groupreserveOld.setLeftpayReservePosition(newLeftpayReservePosition);
                groupreserveOld.setPayReservePosition(newPayReservePosition);
                groupreserveOld.setFrontMoney(newFrontMoney);
            }else{
                groupreserveOld = groupreserve;
            }
            ActivityAirTicket airticket = iActivityAirTicketService.getActivityAirTicketById(airticketId);
            if(airticket==null){
                throw new ServiceException("团期不存在");
            }
            else if(addreserve>airticket.getFreePosition()){
                throw new ServiceException("此团期没有足够的余位");
            }           
            airticket.setFreePosition(airticket.getFreePosition()- addreserve);
            airticket.setPayReservePosition(airticket.getPayReservePosition() + addreserve);
//         
            iActivityAirTicketService.save(airticket);
//            activityGroupService.updatePositionNumByOptLock(airticket, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
            airticketActivityReserveDao.save(groupreserveOld);         
        } 
        ActivityReserveOrder activityReserveOrder  = new ActivityReserveOrder();	
        for(ActivityReserveOrder reserveOrder : activityReserveOrderList){
        	activityReserveOrder=activityReserveOrderDao.save(reserveOrder);			 
		 }
        return activityReserveOrder.getId();
    }
    
    /**
     * 根据渠道Id 和产品Id 获取支付凭证
     * @param agentId 渠道Id
     * @param srcActivityId  产品Id
     *@return list
     * */
    public List<AirticketReserveFile> findFilesByAgentIdAndActivityId(Long agentId,Long srcActivityId) {
        return airticketReserveFileDao.findByAgentIdAndAirticketActivityId(agentId, srcActivityId);
    }
    
    /**
     * 根据渠道Id 和切位订单Id 获取支付凭证
     * @param agentId  渠道Id
     * @param reserveOrderId  切位订单Id
     * @return list
     * 
     * */
    public List<AirticketReserveFile> findFilesByAgentIdAndReserveOrderId(Long agentId,Long reserveOrderId) {
        return airticketReserveFileDao.findByAgentIdAndReserveOrderId(agentId, reserveOrderId);
    }
    
    public String saveOrderPay(DocInfo docInfo,AirticketReserveFile actReserve){
        docInfoDao.save(docInfo);
        actReserve.setSrcDocId(docInfo.getId());
        airticketReserveFileDao.save(actReserve);
        return docInfo.getDocName();
    }
    
    @Transactional(readOnly = false,rollbackFor={ServiceException.class})
    public void saveairticketReserveFile(AirticketReserveFile actReserve){
    	  airticketReserveFileDao.save(actReserve);
    }


    /**
     * 归还切位
     * @param airticketId
     * @param reserveBackAmount
     * @param fontMoneyBackAmount
     * @param agentId
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false,rollbackFor={ServiceException.class})
    public boolean returnReserve(Long airticketId,Integer reserveBackAmount,Integer fontMoneyBackAmount ,Long agentId,
                                 HttpServletRequest request,String returnRemark) throws Exception {
        List<AirticketActivityReserve> list = findReserve(airticketId);//查询所有团期切位
        if(CollectionUtils.isNotEmpty(list)) {
            ActivityAirTicket airticket = iActivityAirTicketService.getActivityAirTicketById(airticketId);//查询团期
			/*更改团期余位并同步*/
            //int reserveNum = airticket.getPayReservePosition() - airticket.getSoldPayPosition();//需要归还的余位
            int reserveNum=reserveBackAmount;
            airticket.setFreePosition(airticket.getFreePosition() + reserveNum);
            //airticket.setPayReservePosition(airticket.getSoldPayPosition());
           
            airticket.setPayReservePosition(airticket.getPayReservePosition()-reserveNum);            
            iActivityAirTicketService.save(airticket);
            for(AirticketActivityReserve reserve : list) {
                if(reserve.getAgentId().equals(agentId)){
                    //方法中加一个渠道商选择，如果渠道商一样的话就做以下操作
                    int payReservePosition = reserve.getPayReservePosition();//切位人数
                    int leftpayReservePosition = reserve.getLeftpayReservePosition();//剩余的切位人数
                    //int frontMoney = reserve.getFrontMoney();//定金金额
                    BigDecimal leftFontMoney = reserve.getLeftFontMoney();//剩余的定金金额
					/*修改切位人数、剩余切位人数和订金金额和剩余的订金金额*/ 
     				/*reserve.setPayReservePosition(payReservePosition - leftpayReservePosition);
 		         	reserve.setLeftpayReservePosition(0);
 					reserve.setFrontMoney(frontMoney - leftFontMoney);
 					reserve.setLeftFontMoney(0);
                    reserve.setFrontMoney(frontMoney-fontMoneyBackAmount); */
                    reserve.setPayReservePosition(payReservePosition - reserveNum);
                    reserve.setLeftpayReservePosition(leftpayReservePosition-reserveNum);                    
                    reserve.setLeftFontMoney(leftFontMoney.subtract(new BigDecimal(fontMoneyBackAmount)));
                    
                    reserve.setReturnRemark(returnRemark);
                    //如果把所有切位都归还（即没有切位订单），则把团期切位删除，否则保存
                    //if(payReservePosition == leftpayReservePosition) {
                    if(reserveNum==leftpayReservePosition){
                        airticketActivityReserveDao.delete(reserve);
                    } else {
                        airticketActivityReserveDao.save(reserve);
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * 机票批量切位接口调整
     * @Description: 
     * @param @param reserveJsonData
     * @param @param uploadJsonData
     * @param @return   
     * @return Map<String,String>  
     * @throws
     * @author majiancheng
     * @date 2015-12-15 下午2:06:44
     */
    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public Map<String, String> batchReceive(String reserveJsonData, String uploadJsonData) throws Exception {
    	//机票切位信息集合
    	List<AirticketActivityReserve> airticketReserveList = JSON.parseArray(reserveJsonData, AirticketActivityReserve.class);
    	//文件集合信息
    	List<AirticketReserveFile> reserveFileList = JSON.parseArray(uploadJsonData, AirticketReserveFile.class);
    	
    	return this.batchReceive(airticketReserveList, reserveFileList, "jsonType");
    }
    
    /**
     * 根据机票切位信息和上传附件保存切位
     * @Description: 
     * @param @param airticketReserveList
     * @param @param reserveFileList
     * @param @param saveType : jsonType表示根据前端表单json数据保存，tempType表示根据草稿箱保存
     * @param @return   
     * @return Map<String,String>  
     * @throws
     * @author majiancheng
     * @date 2015-12-24 下午8:04:30
     */
    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public Map<String, String> batchReceive(List<AirticketActivityReserve> airticketReserveList, List<AirticketReserveFile> reserveFileList, String saveType) {
    	
    	Map<String, String> data = new HashMap<String, String>();
    	
    	List<AirticketReserveFile> toSaveReserveFiles = new ArrayList<AirticketReserveFile>();
    	List<AirticketreservefileTemp> toDeleteFileTemps = new ArrayList<AirticketreservefileTemp>();
    	
    	//根据渠道商将切位文件分组
		Map<String, List<AirticketReserveFile>> reserveFileMap = new HashMap<String, List<AirticketReserveFile>>();
		if(CollectionUtils.isNotEmpty(reserveFileList)) {
			for(AirticketReserveFile reserveFile : reserveFileList) {
				if(reserveFileMap.get(reserveFile.getAgentId().toString()) == null) {
					List<AirticketReserveFile> reserveFiles = new ArrayList<AirticketReserveFile>();
					reserveFiles.add(reserveFile);
					reserveFileMap.put(reserveFile.getAgentId().toString(), reserveFiles);
				} else {
					reserveFileMap.get(reserveFile.getAgentId().toString()).add(reserveFile);
				}
			}
		}
		
		
    	if(CollectionUtils.isNotEmpty(airticketReserveList)) {
    		for(AirticketActivityReserve airticketReserve : airticketReserveList) {
    			//将订金默认值设置为0
    			if(airticketReserve.getFrontMoney() == null) {
    				airticketReserve.setFrontMoney(new BigDecimal(0));//订金金额
    			}
    			
    			//获取页面传递的切位信息
    			AirticketActivityReserve formReserveInfo = airticketReserve;
    			
    			
				//获取改团期历史切位信息
				List<AirticketActivityReserve> aleadyReserves = airticketActivityReserveDao.findPayReservePositionByActivityId(airticketReserve.getActivityId(), airticketReserve.getAgentId());

	            //要切位的数目 
	            int addreserve = airticketReserve.getPayReservePosition();
				//获取产品
				ActivityAirTicket airticket = activityAirTicketDao.getById(airticketReserve.getActivityId());
				//此团期存在切位记录
				if(CollectionUtils.isNotEmpty(aleadyReserves)) {
					AirticketActivityReserve groupreserveOld = aleadyReserves.get(0);
		          
	                int newLeftpayReservePosition = addreserve + groupreserveOld.getLeftpayReservePosition();
	                //新的切位总数=要切位的数目 + 原来的切位数目
	                int newPayReservePosition = addreserve + groupreserveOld.getPayReservePosition();
	                BigDecimal newFrontMoney=groupreserveOld.getFrontMoney().add(airticketReserve.getFrontMoney());
	                
	                groupreserveOld.setLeftpayReservePosition(newLeftpayReservePosition);
	                groupreserveOld.setPayReservePosition(newPayReservePosition);
	                groupreserveOld.setFrontMoney(newFrontMoney);
					super.setOptInfo(groupreserveOld, BaseService.OPERATION_UPDATE);
					
					airticketReserve = groupreserveOld;
		            airticketActivityReserveDao.updateObj(groupreserveOld);
				} else {
					airticketReserve.setReserveType(0);//占位方式：0,定金占位；1,全款占位
					airticketReserve.setSoldPayPosition(0);//售出切位人数
					super.setOptInfo(airticketReserve, BaseService.OPERATION_ADD);
					
					airticketActivityReserveDao.saveObj(airticketReserve);
				}

				//更新机票产品信息
	            if(airticket==null){
	                throw new ServiceException("有产品团期不存在");
	            }
	            else if(addreserve>airticket.getFreePosition()){
	                throw new ServiceException(airticket.getGroupCode() + "此团期没有足够的余位");
	            }
	            airticket.setFreePosition(airticket.getFreePosition()- addreserve);
	            airticket.setPayReservePosition(airticket.getPayReservePosition() + addreserve);
	            activityAirTicketDao.updateObj(airticket);
	            
	            //新建机票团期切位订单
	            ActivityReserveOrder activityReserveOrder  = new ActivityReserveOrder();					
				activityReserveOrder.setSrcActivityId(airticket.getId());
				activityReserveOrder.setActivityGroupId(airticket.getId());
				activityReserveOrder.setPayType(formReserveInfo.getPayType());
				activityReserveOrder.setReservation(formReserveInfo.getReservation());
				Office company=UserUtils.getUser().getCompany();
				String orderNum = sysIncreaseService.updateSysIncrease(company.getName().length() > 3 ? company.getName().substring(0, 3) : company.getName(), company.getId(), null, Context.ORDER_NUM_TYPE);
				activityReserveOrder.setOrderNum(orderNum);
				activityReserveOrder.setAgentId(airticketReserve.getAgentId());
				activityReserveOrder.setSaleId(UserUtils.getUser().getId());
				
				activityReserveOrder.setOrderStatus(1); //订单状态(0:未付定金,1:已付定金)
				activityReserveOrder.setConfirm(0); //收款确认(0:未确认,1:已确认)
				activityReserveOrder.setReserveType(1); //散拼0,机票1
				activityReserveOrder.setMoneyType(1); //人民币
				activityReserveOrder.setRemark(formReserveInfo.getRemark());
				
				//填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0，20150910
				activityReserveOrder.setOrderMoney(formReserveInfo.getFrontMoney());
				activityReserveOrder.setPayMoney(formReserveInfo.getFrontMoney());
				activityReserveOrder.setPayReservePosition(formReserveInfo.getPayReservePosition());
				super.setOptInfo(activityReserveOrder, OPERATION_ADD);
				
				activityReserveOrderDao.save(activityReserveOrder);
				
				if("jsonType".equals(saveType)) {
					//组装切位文件集合
					List<AirticketReserveFile> agentReserveFiles = reserveFileMap.get(airticketReserve.getAgentId().toString());
					if(CollectionUtils.isNotEmpty(agentReserveFiles)) {
						for(AirticketReserveFile agentReserveFile : agentReserveFiles) {
							AirticketReserveFile actReserve = new AirticketReserveFile();
							actReserve.setAirticketActivityId(airticketReserve.getActivityId());
				    		actReserve.setReserveOrderId(activityReserveOrder.getId()); //切位订单id
				    		actReserve.setFileName(agentReserveFile.getFileName());
				    		actReserve.setSrcDocId(agentReserveFile.getSrcDocId());
				    		actReserve.setAgentId(agentReserveFile.getAgentId());
				    		super.setOptInfo(actReserve, OPERATION_ADD);
				    		toSaveReserveFiles.add(actReserve);
						}
					}
				} else if("tempType".equals(saveType)){
					List<AirticketreservefileTemp> fileTemps = airticketreservefileTempDao.getByReserveTempUuid(airticketReserve.getReserveTempUuid());
					if(CollectionUtils.isNotEmpty(fileTemps)) {
						for(AirticketreservefileTemp fileTemp : fileTemps) {
							AirticketReserveFile actReserve = new AirticketReserveFile();
							actReserve.setAirticketActivityId(airticketReserve.getActivityId());
				    		actReserve.setReserveOrderId(activityReserveOrder.getId()); //切位订单id
				    		actReserve.setFileName(fileTemp.getFileName());
				    		actReserve.setSrcDocId(fileTemp.getSrcDocId().longValue());
				    		actReserve.setAgentId(fileTemp.getAgentId().longValue());
				    		super.setOptInfo(actReserve, OPERATION_ADD);
				    		toSaveReserveFiles.add(actReserve);
				    		
				    		fileTemp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
						}
						
						toDeleteFileTemps.addAll(fileTemps);
					}
				}
    		}
    		airticketReserveFileDao.batchSave(toSaveReserveFiles);
    		
    		airticketreservefileTempDao.batchUpdate(toDeleteFileTemps);
    	}

    	data.put("result", "1");
    	data.put("message", "批量切位成功");
    	return data;
    }
    
    /**
     * 根据机票id集合和渠道id信息
     * @Description: 
     * @param @param airTicketIds
     * @param @param agentId
     * @param @return   
     * @return List<AirticketActivityReserve>  
     * @throws
     * @author majiancheng
     * @date 2016-1-9
     */
    public List<AirticketActivityReserve> getReservesByAirTicketIds(List<Long> airTicketIds, Long agentId) {
    	return airticketActivityReserveDao.getReservesByAirTicketIds(airTicketIds, agentId);
    }
}
