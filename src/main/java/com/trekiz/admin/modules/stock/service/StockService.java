package com.trekiz.admin.modules.stock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrder;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.ActivityReserveOrderDao;
import com.trekiz.admin.modules.activity.service.ActivityReserveOrderService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.entity.ActivityReserveFile;
import com.trekiz.admin.modules.stock.jsonBean.ReturnReserveJsonBean;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.stock.repository.ActivityReserveFileDao;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.temp.stock.dao.ActivityreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;

/**
 * 旅游产品信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class StockService extends BaseService{

	@Autowired
	@Qualifier("activityGroupSyncService")
	private IActivityGroupService activityGroupSyncService;
	@Autowired
	private ActivityGroupReserveDao activityGroupReserveDao;
	
	@Autowired
	private IStockDao stockDao;
	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private ActivityReserveOrderDao activityReserveOrderDao;
	
	
	@Autowired
	private ProductOrderCommonDao productorderDao;
    @Autowired
    private DocInfoDao docInfoDao;
    @Autowired
    private ActivityReserveFileDao activityReserveFileDao;
    @Autowired
    private AgentinfoDao agentinfoDao;
	@Autowired
	@Qualifier("activityReserveOrderService")
	private ActivityReserveOrderService activityReserveOrderService;
    @Autowired
    private AirticketStockService airticketStockService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private ActivityreservefileTempDao activityreservefileTempDao;
	
//	public TravelActivity findById(Long id){
//		return travelActivityDao.findOne(id);
//	}
	
	public ActivityGroupReserve findByGroupreserveId(Long groupreserveId) {
		return activityGroupReserveDao.findOne(groupreserveId);
	}
	
	public ActivityGroup findByActivityGroupId(Long groupreserveId) {
		ActivityGroupReserve groupReserve = this.findByGroupreserveId(groupreserveId);
		if(groupReserve != null){
			return activityGroupSyncService.findById(groupReserve.getActivityGroupId());
		}
		return null;
	}
	
	
	/**
	 *根据主键查找ActivityGroup
	 *@param id      
	 *@return ActivityGroup
	 * */
	public ActivityGroup findActivityGroupById(Long id){
		return activityGroupDao.findOne(id);
	}
	
	public  ActivityReserveOrder findReserveOrderInfoByOrderNum(String orderNum){
		//return reserveOrderInfoViewDao.findReserveOrderInfo(orderNum);
		return (activityReserveOrderDao.findActivityReserveOrder(orderNum)).get(0);
	}
	public ActivityGroupReserve findByActivityGroupIdAndAgentId(String groupId, String agentId) {
		ActivityGroupReserve activityGroupReserve = null;
		if(StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(agentId)) {
			activityGroupReserve = activityGroupReserveDao.findByActivityGroupIdAndAgentId(Long.valueOf(groupId), 
					Long.valueOf(agentId));
			
			
		}
		return activityGroupReserve;
	}
	
	/**
	 * return 0:成功
	 * 		  1：entity为空
	 * 		  2: 切位总数不能大于余位
	 * @throws OptimisticLockHandleException 
	 * @throws PositionOutOfBoundException */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public int saveActivityGroupReserve(ActivityGroupReserve activityGroupReserve, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		if(activityGroupReserve != null){
			Long id = activityGroupReserve.getId();
			ActivityGroupReserve activityGroupReserveOld;
			activityGroupReserveOld = activityGroupReserveDao.findOne(id);
			int payReservePosition = activityGroupReserve.getPayReservePosition();
			int addreserve = payReservePosition;
			if(activityGroupReserveOld==null){
				throw new ServiceException("修改的切团信息不存在");
			}else{
				addreserve -= activityGroupReserveOld.getPayReservePosition();
			}
			ActivityGroup activityGroup = activityGroupSyncService.findById(activityGroupReserveOld.getActivityGroupId());
			if(activityGroup==null){
				throw new ServiceException("团期不存在");
			}
			else if(addreserve>activityGroup.getFreePosition()){
				throw new ServiceException("此团期没有足够的余位");
			}
			activityGroup.setFreePosition(activityGroup.getFreePosition()-addreserve);
			activityGroup.setPayReservePosition(activityGroup.getPayReservePosition()+addreserve);
			///////////////////////////////
			activityGroup.setPlusFreePosition(-addreserve);
			activityGroup.setPlusPayReservePosition(addreserve);
			///////////////////////////////
			activityGroupSyncService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
			
			//activityGroupReserveOld.setFrontMoney(activityGroupReserve.getFrontMoney()+activityGroupReserve.getFrontMoney()*addreserve/activityGroupReserveOld.getPayReservePosition());
			activityGroupReserveOld.setFrontMoney(activityGroupReserve.getFrontMoney());
			activityGroupReserveOld.setPayReservePosition(activityGroupReserve.getPayReservePosition());
			activityGroupReserveOld.setLeftpayReservePosition(activityGroupReserveOld.getLeftpayReservePosition()+addreserve);
			activityGroupReserveOld.setRemark(activityGroupReserve.getRemark());
			activityGroupReserveOld.setUpdateDate(new Date());
		}else{
			return 1;
		}
		return 0;
	}
	public ActivityGroupReserve findOneGroupReserve(Long id) {
		return activityGroupReserveDao.findOne(id);
	}
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public void deleteActivityGroupReserve(Long groupreserveId, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		if(groupreserveId!=null){
			ActivityGroupReserve groupReserve = activityGroupReserveDao.findOne(groupreserveId);
			if(groupReserve!=null){
				int payReservePosition = groupReserve.getPayReservePosition();
				Long activityGroupId = groupReserve.getActivityGroupId();
				ActivityGroup activityGroup = activityGroupSyncService.findById(activityGroupId);
				activityGroupReserveDao.delete(groupReserve);
				activityGroup.setPayReservePosition(activityGroup.getPayReservePosition() - payReservePosition);
				activityGroup.setFreePosition(activityGroup.getFreePosition() + payReservePosition);
				///////////////////////////////
				activityGroup.setPlusPayReservePosition(- payReservePosition);
				activityGroup.setPlusFreePosition(payReservePosition);
				///////////////////////////////
				activityGroupSyncService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
			}
		}
	}
	

	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public Long saveGroupReserveList(List<ActivityGroupReserve> activityGroupReserveList,List<ActivityReserveOrder> activityReserveOrderList, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		 for(ActivityGroupReserve groupreserve : activityGroupReserveList){
			Long id = groupreserve.getId();
			Long activityGroupId = groupreserve.getActivityGroupId();
			//切位数目
			int addreserve  = groupreserve.getPayReservePosition();			
			
			//记录新增切位日志
			//logReserveService.addLog(groupreserve.getAgentId(), 1,groupreserve.getSrcActivityId(), groupreserve.getActivityGroupId(), "新增切位"+addreserve+"个","");
			ActivityGroupReserve groupreserveOld = null;
			if(id != null)groupreserveOld = activityGroupReserveDao.findOne(id);
			if(groupreserveOld!=null){
				int newLeftpayReservePosition=addreserve + groupreserveOld.getLeftpayReservePosition();
				int newPayReservePosition=addreserve + groupreserveOld.getPayReservePosition();
				
				Double newFrontMoney=groupreserveOld.getFrontMoney().doubleValue() + groupreserve.getFrontMoney().doubleValue();
				
				groupreserveOld.setLeftpayReservePosition(newLeftpayReservePosition);
				groupreserveOld.setPayReservePosition(newPayReservePosition);
				BigDecimal money=new BigDecimal(newFrontMoney);
				groupreserveOld.setFrontMoney(money); 
			}else{
				groupreserveOld = groupreserve;
			}
			
			ActivityGroup activityGroup = new ActivityGroup();
			activityGroup = activityGroupSyncService.findById(activityGroupId);
			
			if(activityGroup==null){
				throw new ServiceException("团期不存在");
			}
			else if(addreserve>activityGroup.getFreePosition()){
				throw new ServiceException("此团期没有足够的余位");
			}
			
			activityGroup.setFreePosition(activityGroup.getFreePosition() - addreserve);
			activityGroup.setPayReservePosition(activityGroup.getPayReservePosition() + addreserve);
									
			activityGroupSyncService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
			activityGroupReserveDao.save(groupreserveOld);
		}
		 ActivityReserveOrder activityReserveOrder  = new ActivityReserveOrder();
		 for(ActivityReserveOrder reserveOrder : activityReserveOrderList){
			 activityReserveOrder=activityReserveOrderDao.save(reserveOrder);
		 }		
		 return activityReserveOrder.getId();
			
	}
	
	public List<Map<String, Object>> findReserveOrder(Long activityGroupId,Long agentId){
		return stockDao.findReserveOrder(activityGroupId,agentId);
	}
	 
	
	/*
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public void saveGroupReserve(ActivityGroupReserve groupreserve, ActivityReserveOrder activityReserveOrder, HttpServletRequest request) throws Exception {
		
			Long id = groupreserve.getId();
			Long activityGroupId = groupreserve.getActivityGroupId();
			int payReservePosition = groupreserve.getPayReservePosition();
			int addreserve = payReservePosition;
			ActivityGroupReserve groupreserveOld = null;
			if(id != null)groupreserveOld = activityGroupReserveDao.findOne(id);
			if(groupreserveOld!=null){
				int newLeftpayReservePosition=addreserve+groupreserveOld.getLeftpayReservePosition();
				int newPayReservePosition=addreserve+groupreserveOld.getPayReservePosition();
				
				//int newFrontMoney=groupreserveOld.getFrontMoney()+groupreserve.getFrontMoney();
				Double newFrontMoney=groupreserveOld.getFrontMoney().doubleValue() + groupreserve.getFrontMoney().doubleValue();
				
				//addreserve -= groupreserveOld.getPayReservePosition();
				groupreserveOld.setLeftpayReservePosition(newLeftpayReservePosition);
				groupreserveOld.setPayReservePosition(newPayReservePosition);
				
				BigDecimal money=new BigDecimal(newFrontMoney);
				groupreserveOld.setFrontMoney(money); 
				//groupreserveOld.setFrontMoney(newFrontMoney); 
			}else{
				groupreserveOld = groupreserve;
			}
			ActivityGroup activityGroup = activityGroupSyncService.findById(activityGroupId);
			if(activityGroup==null){
				throw new ServiceException("团期不存在");
			}
			else if(addreserve>activityGroup.getFreePosition()){
				throw new ServiceException("此团期没有足够的余位");
			}
			int newGroupPayReservePosition=activityGroup.getPayReservePosition()+addreserve;
			activityGroup.setFreePosition(activityGroup.getFreePosition()-addreserve);
			activityGroup.setPayReservePosition(newGroupPayReservePosition);
			///////////////////////////////
			activityGroup.setPlusFreePosition(-addreserve);
			activityGroup.setPlusPayReservePosition(addreserve);
			///////////////////////////////
			activityGroupSyncService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
			activityGroupReserveDao.save(groupreserveOld);
			
			activityReserveOrderService.addActivityReserveOrder(activityReserveOrder);		
	}
	*/
	
	

	/**
	 * 归还切位
	 * @param activityGroupId
	 * @param agentId
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public boolean returnReserve(Long activityGroupId,Integer reserveBackAmount,Integer fontMoneyBackAmount ,Long agentId,
			HttpServletRequest request,String remark) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		
		//Long agentId=UserUtils.getUser().getAgent().getId();
		List<ActivityGroupReserve> list = findPayReserveByGroupId(activityGroupId);//查询所有团期切位
		if(CollectionUtils.isNotEmpty(list)) {
			ActivityGroup activityGroup = activityGroupSyncService.findById(activityGroupId);//查询团期
			/*更改团期余位并同步*/
			//int reserveNum = activityGroup.getPayReservePosition() - activityGroup.getSoldPayPosition();//需要归还的余位
			int reserveNum=reserveBackAmount;
			activityGroup.setFreePosition(activityGroup.getFreePosition() + reserveNum);
			//activityGroup.setPayReservePosition(activityGroup.getSoldPayPosition());
			activityGroup.setPayReservePosition(activityGroup.getPayReservePosition()-reserveNum);
			activityGroup.setPlusFreePosition(reserveNum);
			activityGroup.setPlusPayReservePosition(-reserveNum);
			activityGroupSyncService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
			for(ActivityGroupReserve reserve : list) {
				if(reserve.getAgentId().equals(agentId)){
					//记录归还切位日志
					//logReserveService.addLog(agentId, 1,reserve.getSrcActivityId(),reserve.getActivityGroupId(), "归还切位"+reserveNum+"个",remark);
					
					//方法中加一个渠道商选择，如果渠道商一样的话就做以下操作
					int payReservePosition = reserve.getPayReservePosition();//切位人数
					int leftpayReservePosition = reserve.getLeftpayReservePosition();//剩余的切位人数
					//int frontMoney = reserve.getFrontMoney();//定金金额
					//int leftFontMoney = reserve.getLeftFontMoney();//剩余的定金金额
					
					/*修改切位人数、剩余切位人数和订金金额和剩余的订金金额*/
//					reserve.setPayReservePosition(payReservePosition - leftpayReservePosition);
//					reserve.setLeftpayReservePosition(0);
//					reserve.setFrontMoney(frontMoney - leftFontMoney);
//					reserve.setLeftFontMoney(0);					
					reserve.setPayReservePosition(payReservePosition - reserveNum);
					reserve.setLeftpayReservePosition(leftpayReservePosition-reserveNum);
					
					//还位 时 金额 不 做 改变 ,金额 走 退款 流程 
					//reserve.setFrontMoney(frontMoney-fontMoneyBackAmount);
					//reserve.setLeftFontMoney(leftFontMoney-fontMoneyBackAmount);
					
					//如果把所有切位都归还（即没有切位订单），则把团期切位删除，否则保存
					//if(payReservePosition == leftpayReservePosition) {
					if(reserveNum==leftpayReservePosition){
						activityGroupReserveDao.delete(reserve);
					} else {
						activityGroupReserveDao.save(reserve);
					}
				}
			}
		}
		return true;
	}
	
	  @Transactional(readOnly = false,rollbackFor={ServiceException.class})
	    public List<List<Map<String,Object>>> findProductGroupOrders( Long id, String status) {
            /*
	        User userInfo = UserUtils.getUser();
	        List<Role> roleList = userInfo.getRoleList();

	        Map<String, Object> roleMap = new HashMap<String,Object>();
	        for(Role role : roleList){
	            roleMap.put(role.getRoleType(), role.getDepartment());
	        }
	        Map<String, Object> conMap = new HashMap<String, Object>();
	        conMap.put("airticket_id", id.toString());*/
	        List<Map<String, Object>> productList  = stockDao.findProductGroupOrders(id,status);
	      
	        List<List<Map<String, Object>>> msgList = new ArrayList<List<Map<String, Object>>>();
	        List<Map<String, Object>> productListPosition = new ArrayList<Map<String, Object>>();	       
	       
	        if(productList != null) {        	
	        	
	        	for(Map<String, Object> objs:productList){	              
	               
	                if(objs.get("createUserName")== null) objs.put("createUserName"," ");
	            } 	        	
	        	Iterator<Map<String, Object>> it = productList.iterator();
	            //如果status为空，则表示售出占位，不为空则表示已占位
	            if(StringUtils.isEmpty(status)) {//售出占位
	            	
	                while(it.hasNext()) {
	                    Map<String, Object> p = it.next();                  
	                    String placeHolderType = p.get("place_holder_type") != null ? p.get("place_holder_type").toString() : "0";//占位类型：1为切位，0或空表示占位
	                   
	                    if(placeHolderType == null || "0".equals(placeHolderType)) {//占位
	                        String payStatus = p.get("payStatus") != null ? p.get("payStatus").toString() : null;	                       
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
	                    String payStatus = p.get("payStatus") != null ? p.get("payStatus").toString() : null;	
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
	  
	  
	/**
	 * 批量归还余位 add by zhanghao
	 * @param list 批量归还余位的数据对象
	 * @param returnType 散拼0,机票1
	 * @param request 
	 * @return
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public boolean batchReturnReserve(List<ReturnReserveJsonBean> list,Integer returnType,HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		boolean b = true;
		if(returnType==0){//散拼0,机票1
			if(CollectionUtils.isNotEmpty(list)){
				for(ReturnReserveJsonBean bean:list){
					String returnRemark="";
					if(bean.getReturnRemark()!=null){
						returnRemark=bean.getReturnRemark();
					}
					this.returnReserve(bean.getProductId()	, bean.getReserveBackAmount(), null, bean.getAgentId(), request, returnRemark);
				}
			}
			
			
		}else if(returnType==1){//散拼0,机票1
			for(ReturnReserveJsonBean bean:list){
				String returnRemark="";
				if(bean.getReturnRemark()!=null){
					returnRemark=bean.getReturnRemark();
				}
				airticketStockService.returnReserve(bean.getProductId()	, bean.getReserveBackAmount(), 0, bean.getAgentId(), request, returnRemark);
			}
		}
		
		return b;
	}
	  
	/**
	 * 查询团期已占位明细或售出占位明细
	 * @param id 团期ID
	 * @param status 已占位或售出占位
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public List<List<ProductOrderCommon>> findProductGroupOrdersOld(Long id, String status) {
		List<List<ProductOrderCommon>> msgList = new ArrayList<List<ProductOrderCommon>>();//返回值
		List<ProductOrderCommon> productListPosition = new ArrayList<ProductOrderCommon>();
		List<ProductOrderCommon> productList = productorderDao.findByProductGroupIdOrderByCompany(id);
		//选择占位订单，排除切位订单
		if(productList != null) {
			Iterator<ProductOrderCommon> it = productList.iterator();
			//如果status为空，则表示售出占位，不为空则表示已占位
			if(StringUtils.isEmpty(status)) {//售出占位
				while(it.hasNext()) {
					ProductOrderCommon p = it.next();
					Integer placeHolderType = p.getPlaceHolderType();//占位类型：1为切位，0或空表示占位
					if(placeHolderType == null || placeHolderType == 0) {//占位
						Integer payStatus = p.getPayStatus();
						if(payStatus !=null && payStatus !=1 && payStatus !=2 && payStatus != 99 &&payStatus!=111) {
							productListPosition.add(p);
						}
					}
				}
			} else {//已占位明细
				//把切位订单排除在外
				while(it.hasNext()) {
					ProductOrderCommon p = it.next();
					Integer placeHolderType = p.getPlaceHolderType();//占位类型：1为切位，0或空表示占位
					if(placeHolderType == 1) {//切位
						it.remove();
					}
				}
				productListPosition = productList;
			}
		}
		if(productListPosition.size() > 1) {
			List<ProductOrderCommon> arrDataList = new ArrayList<ProductOrderCommon>();
			arrDataList.add(productListPosition.get(0));
			for(int i = 0; i<productListPosition.size()-1; i++) {
				ProductOrderCommon order = productListPosition.get(i + 1);
				if(order.getPlaceHolderType() == null || order.getPlaceHolderType() == 0) {//占位
					if(productListPosition.get(i).getOrderCompanyName().equals(order.getOrderCompanyName())) {
						arrDataList.add(productListPosition.get(i+1));
					} else {
						msgList.add(arrDataList);
						arrDataList = new ArrayList<ProductOrderCommon>();
						arrDataList.add(productListPosition.get(i+1));
					}
				}
			}
			msgList.add(arrDataList);
		} else {
			msgList.add(productListPosition);
		}
		return msgList;
	}
	/**
	 * 查询团期的切位数
	 */
	public List<ActivityGroupReserve> findPayReservePosition(Long srcActivityId,Long agentId) {
		return activityGroupReserveDao.findPayReservePositionByActivityId(srcActivityId,agentId);
	}
	
	/**
	 * 根据团期ID和渠道商ID查询团期切位
	 * @param activityGroupId
	 * @param agentId
	 * @return
	 */
	public List<ActivityGroupReserve> findPayReserveByGroupId(Long activityGroupId) {
		return activityGroupReserveDao.findPayReserveByGroupId(activityGroupId);
	}
	/*
	public List<Object[]> findSoldNopayPosition(Long activityGroupId){
		return activityGroupReserveDao.findSoldNopayPosition(activityGroupId);
	}*/
	
	public List<Map<String, Object>> findSoldNopayPosition(Long activityGroupId){
		return stockDao.findSoldNopayPosition(activityGroupId.toString());
	}
	
	public List<ActivityGroupReserve> findGroupReserve(Long agentId,Long srcActivityId) {
		return activityGroupReserveDao.findByAgentIdAndSrcActivityId(agentId, srcActivityId);
	}
	public Agentinfo findOne(Long id){
		return agentinfoDao.findOne(id);
	}
//	public ActivityGroup findActivityGroup(Long id){
//		return activityGroupSyncService.findOne(id);
//	}
	public ActivityGroupReserve findGroupReserve(Long agentId,Long srcActivityId,Long activityGroupId) {
		return activityGroupReserveDao.findByAgentIdAndSrcActivityIdAndActivityGroupId(agentId, srcActivityId,activityGroupId);
	}
	
	public List<ActivityReserveFile> findByAgentIdAndSrcActivityIdAndActivityGroupId(Long agentId,Long srcActivityId,Long activityGroupId) {
		return activityReserveFileDao.findByAgentIdAndSrcActivityIdAndActivityGroupId(agentId,srcActivityId,activityGroupId);
	}
    /**
     * 根据渠道ID和订单ID获取散拼切位订单支付凭证
     * 
     * @author haiming.zhao
     * @param  agentId   渠道Id
     * @param  id    订单Id
     * @return  list
     * */
	public List<ActivityReserveFile> findByAgentIdAndSrcActivityIdAndActivityGroupId(Long agentId,Long id) {
		return activityReserveFileDao.findByAgentIdAndReserveOrderId(agentId,id);
	}
	
	public List<ActivityReserveFile> findByAgentIdAndSrcActivityId(Long agentId,Long srcActivityId) {
		return activityReserveFileDao.findByAgentIdAndSrcActivityId(agentId,srcActivityId);
	}

	public List<ProductOrderCommon> findProductorderByGroupCode(String groupCode,String srcActivityId) {
		return activityGroupReserveDao.findProductorderByGroupCode(groupCode,Integer.parseInt(srcActivityId));
	}

	public List<TravelActivity> findTravelActivityByGroupCode(String srcActivityId) {
		return activityGroupReserveDao.findTravelActivityByGroupCode(Long.parseLong(srcActivityId));
	}
	
	public List<ActivityGroup> findGroup(String groupCode,String srcActivityId) {
		return activityGroupReserveDao.findActivityGroupByGroupCode(groupCode,Integer.parseInt(srcActivityId));
	}
//	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
//	public void saveActivityGroup(ActivityGroup activityGroup) {
//		activityGroupSyncService.save(activityGroup);
//	}
	public String saveOrderPay(DocInfo docInfo,ActivityReserveFile actReserve){
		docInfoDao.save(docInfo);
		actReserve.setSrcDocId(docInfo.getId());
		activityReserveFileDao.save(actReserve);
		return docInfo.getDocName();
	}
	
	public void saveReserveFile(ActivityReserveFile actReserve){
		activityReserveFileDao.save(actReserve);
	}
	
	/**
	 * 根据json数据批量保存切位信息和上传文件信息
	 * @Description: 
	 * @param @param reserveJsonData
	 * @param @param uploadJsonData
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-2 下午4:11:09
	 */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public Map<String, String> batchReceive(String reserveJsonData, String uploadJsonData, HttpServletRequest request) throws Exception {
		//切位批量信息
		List<ActivityGroupReserve> groupReserves = JSON.parseArray(reserveJsonData, ActivityGroupReserve.class);
		//批量文件信息
		List<ActivityReserveFile> reserveFiles = JSON.parseArray(uploadJsonData, ActivityReserveFile.class);
		return this.batchReceive(groupReserves, reserveFiles, request, "jsonType");
	}
	
	/**
	 * 根据对象集合信息创建切位信息和切位上传文件信息
	 * @Description: 
	 * @param @param groupReserves
	 * @param @param reserveFiles
	 * @param @param request
	 * @param @param saveType 分为jsonType:表示从前台页面直接保存和tempType：表示从草稿箱确认切位方式保存
	 * @param @return
	 * @param @throws Exception   
	 * @return Map<String,String>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午8:18:18
	 */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
	public Map<String, String> batchReceive(List<ActivityGroupReserve> groupReserves, List<ActivityReserveFile> reserveFiles, HttpServletRequest request, String saveType) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		//根据渠道商将切位文件分组
		Map<String, List<ActivityReserveFile>> reserveFileMap = new HashMap<String, List<ActivityReserveFile>>();
		if(CollectionUtils.isNotEmpty(reserveFiles)) {
			for(ActivityReserveFile reserveFile : reserveFiles) {
				if(reserveFileMap.get(reserveFile.getAgentId().toString()) == null) {
					List<ActivityReserveFile> reserveFileList = new ArrayList<ActivityReserveFile>();
					reserveFileList.add(reserveFile);
					reserveFileMap.put(reserveFile.getAgentId().toString(), reserveFileList);
				} else {
					reserveFileMap.get(reserveFile.getAgentId().toString()).add(reserveFile);
				}
			}
		}
		
		//待保存的切位文件集合
		List<ActivityReserveFile> toSaveReserveFiles = new ArrayList<ActivityReserveFile>();
		List<ActivityreservefileTemp> toDeleteReservefileTemps = new ArrayList<ActivityreservefileTemp>();
		
		if(CollectionUtils.isNotEmpty(groupReserves)) {
			for(ActivityGroupReserve groupReserve : groupReserves) {

				//添加支付定金默认金额
				if(groupReserve.getFrontMoney() == null) {
					groupReserve.setFrontMoney(new BigDecimal(0));
				}
				
				//获取页面传递的切位信息
				ActivityGroupReserve formReserveInfo = groupReserve;
				
				//获取改团期历史切位信息
				List<ActivityGroupReserve> aleadyReserves = activityGroupReserveDao.findReserveByActivityGroupId(groupReserve.getActivityGroupId(), groupReserve.getAgentId());

				//获取产品
				ActivityGroup activityGroup = activityGroupDao.getById(groupReserve.getActivityGroupId());

				int addreserve = groupReserve.getPayReservePosition();
				
				//切位信息已经存在
				if(CollectionUtils.isNotEmpty(aleadyReserves)) {
					ActivityGroupReserve aleadyReserve = aleadyReserves.get(0);
					int newLeftpayReservePosition = addreserve + aleadyReserve.getLeftpayReservePosition();//剩余切位
					int newPayReservePosition = addreserve + aleadyReserve.getPayReservePosition();//支付切位
					
					Double newFrontMoney = aleadyReserve.getFrontMoney().doubleValue() + groupReserve.getFrontMoney().doubleValue();//支付定金金额
					
					aleadyReserve.setLeftpayReservePosition(newLeftpayReservePosition);
					aleadyReserve.setPayReservePosition(newPayReservePosition);
					BigDecimal money=new BigDecimal(newFrontMoney);
					aleadyReserve.setFrontMoney(money);
					
					groupReserve = aleadyReserve;
					
					super.setOptInfo(groupReserve, BaseService.OPERATION_UPDATE);
					activityGroupReserveDao.updateObj(aleadyReserve);
				//切位信息不存在时
				} else {
					//切位时已售出切位 soldPayPosition=0
					groupReserve.setSoldPayPosition(0);
					//填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0
					if(groupReserve.getFrontMoney() == null) {
						groupReserve.setFrontMoney(new BigDecimal("0"));
					}
					groupReserve.setSrcActivityId(activityGroup.getSrcActivityId().longValue());//产品id
					groupReserve.setReserveType(0);//占位方式：0,定金占位；1,全款占位
					
					super.setOptInfo(groupReserve, BaseService.OPERATION_ADD);
					activityGroupReserveDao.save(groupReserve);
				}
				
				
				//更新产品信息
				if(activityGroup==null){
					throw new ServiceException("有产品团期不存在");
				}
				else if(addreserve>activityGroup.getFreePosition()){
					throw new ServiceException(activityGroup.getGroupCode() + "此团期没有足够的余位");
				}
				activityGroup.setFreePosition(activityGroup.getFreePosition() - addreserve);
				activityGroup.setPayReservePosition(activityGroup.getPayReservePosition() + addreserve);
				activityGroupSyncService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
				
				
				//--------组装切位订单数据--------------------------
				ActivityReserveOrder activityReserveOrder  = new ActivityReserveOrder();					
				activityReserveOrder.setSrcActivityId(activityGroup.getSrcActivityId().longValue());//产品id
				activityReserveOrder.setActivityGroupId(groupReserve.getActivityGroupId());
				
				User user = UserUtils.getUser();
				String companyName = user.getCompany().getName();
				//生成订单编号
				String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, user.getCompany().getId(), null, Context.ORDER_NUM_TYPE);
				activityReserveOrder.setOrderNum(orderNum);
				
				activityReserveOrder.setAgentId(groupReserve.getAgentId());
				activityReserveOrder.setSaleId(user.getId());
				activityReserveOrder.setRemark(formReserveInfo.getRemark());
				activityReserveOrder.setOrderStatus(1); //订单状态(0:未付定金,1:已付定金)
				activityReserveOrder.setConfirm(0); //0:收款未确认 
				activityReserveOrder.setReserveType(0); //0:散拼，1：机票
				activityReserveOrder.setMoneyType(1);  //切位都用人民币
				activityReserveOrder.setPayReservePosition(formReserveInfo.getPayReservePosition());
				activityReserveOrder.setPayType(formReserveInfo.getPayType());
				activityReserveOrder.setReservation(formReserveInfo.getReservation());
				activityReserveOrder.setOrderMoney(formReserveInfo.getFrontMoney());
				activityReserveOrder.setPayMoney(formReserveInfo.getFrontMoney());
				
				activityReserveOrder.setStartDate(activityGroup.getGroupOpenDate());
				activityReserveOrder.setEndDate(activityGroup.getGroupCloseDate());
				super.setOptInfo(activityReserveOrder, OPERATION_ADD);
				activityReserveOrderDao.save(activityReserveOrder);
				//--------组装切位订单数据--------------------------
				
				//组装切位文件集合
				if("jsonType".equals(saveType)) {
					List<ActivityReserveFile> agentReserveFiles = reserveFileMap.get(groupReserve.getAgentId().toString());
					if(CollectionUtils.isNotEmpty(agentReserveFiles)) {
						for(ActivityReserveFile agentReserveFile : agentReserveFiles) {
							
							ActivityReserveFile actReserve = new ActivityReserveFile();
							actReserve.setSrcActivityId(groupReserve.getSrcActivityId());
				    		actReserve.setActivityGroupId(groupReserve.getActivityGroupId());
				    		actReserve.setReserveOrderId(activityReserveOrder.getId()); //切位订单id
				    		actReserve.setFileName(agentReserveFile.getFileName());
				    		actReserve.setSrcDocId(agentReserveFile.getSrcDocId());
				    		actReserve.setAgentId(agentReserveFile.getAgentId());
				    		super.setOptInfo(actReserve, OPERATION_ADD);
				    		
				    		toSaveReserveFiles.add(actReserve);
						}
					}
				} else if("tempType".equals(saveType)) {
					//临时表uuid
					String reserveTempUuid = groupReserve.getReserveTempUuid();
					List<ActivityreservefileTemp> reserveFileTemps = activityreservefileTempDao.getFilesByReserveTempUuid(reserveTempUuid);
					if(CollectionUtils.isNotEmpty(reserveFileTemps)) {
						for(ActivityreservefileTemp reserveFileTemp : reserveFileTemps) {
							
							ActivityReserveFile actReserve = new ActivityReserveFile();
							actReserve.setSrcActivityId(groupReserve.getSrcActivityId());
				    		actReserve.setActivityGroupId(groupReserve.getActivityGroupId());
				    		actReserve.setReserveOrderId(activityReserveOrder.getId()); //切位订单id
				    		actReserve.setFileName(reserveFileTemp.getFileName());
				    		actReserve.setSrcDocId(reserveFileTemp.getSrcDocId().longValue());
				    		actReserve.setAgentId(reserveFileTemp.getAgentId().longValue());
				    		super.setOptInfo(actReserve, OPERATION_ADD);
				    		
				    		toSaveReserveFiles.add(actReserve);
				    		
				    		reserveFileTemp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
						}
						
						toDeleteReservefileTemps.addAll(reserveFileTemps);
					}
				}
			}
		}
		
		activityReserveFileDao.batchSave(toSaveReserveFiles);
		
		activityreservefileTempDao.batchUpdate(toDeleteReservefileTemps);
		
		datas.put("result", "1");
    	datas.put("message", "批量切位成功");
		return datas;
	}
	
	/**
	 * 根据切位类型和对应的 散拼团期ID/机票产品ID 查询切位订单数据（散拼或者机票）
	 * 
	 * @param sourceId 散拼团期ID/机票产品ID
	 * @param reserveType 散拼0,机票1
	 * @return 
	 */
	public List<Map<String,Object>> queryReserveList4ReserveType(Long sourceId,Integer reserveType) {
		if(sourceId!=null&&reserveType!=null){
			return stockDao.queryReserveList4ReserveType(sourceId, reserveType);
		}
		return null;
	}
	
	/**
	 * 根据团期id集合和批发商id获取切位集合
	 * @Description: 
	 * @param @param activityGroupIds
	 * @param @param agentId
	 * @param @return   
	 * @return List<ActivityGroupReserve>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-9
	 */
	public List<ActivityGroupReserve> getReservesByGroupIds(List<String> activityGroupIds, Long agentId) {
		return stockDao.getReservesByGroupIds(activityGroupIds, agentId);
	}
}
