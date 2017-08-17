package com.trekiz.admin.modules.visa.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNotice;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaOrderFile;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaInterviewNoticeDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderFileDao;

import freemarker.template.TemplateException;


@Service
@Transactional(readOnly = true)
public class VisaInterviewNoticeService  extends BaseService {
    
    @Autowired
    private VisaInterviewNoticeDao visaInterviewNoticeDao;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private VisaOrderService visaOrderService;
    @Autowired
	private DictDao dictDao;
    @Autowired
	private VisaOrderDao visaOrderDao;

    //0214--需求--新增面前通知附件-s//
    @Autowired
    private VisaOrderFileDao visaOrderFileDao;
    //0214--需求--新增面前通知附件-e//
    
	public String getMoney(String serialNum,String flag){
		if(StringUtils.isBlank(serialNum)){
			return null;
		}
		String sql = "SELECT m.currencyId,c.currency_name,sum(m.amount),c.currency_mark,m.amount from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum +"' GROUP BY m.currencyId ORDER BY m.currencyId";
		@SuppressWarnings("unchecked")
        List<Object[]> results = visaOrderDao.getSession().createSQLQuery(sql).list();
		
		String money = "";
		
		if(results!=null && results.size()>0){
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				
				DecimalFormat d= new DecimalFormat(",##0.00");
				if(null == flag){
					if(i==0){
						money = money + amount[3] + " " + d.format(new BigDecimal(amount[2].toString()));
					}else{
						money = money +"</br>"+ amount[3] + " " + d.format(new BigDecimal(amount[2].toString()));
					}
				}else{
					money = money + amount[3] + " " + d.format(new BigDecimal(amount[2].toString()));
				}
			}
		}else{
			if(null == flag){
				return null;
			}
			money = "0";
		}
		
		return money;
	}
	
    /**
	 * 查询字典表中的数据
	 * @param type 字典表中的 type 字段
	 */
	public List<Dict> findDictByType(String type){
		List<Dict> dictList = dictDao.findByType(type);
		return dictList;
	}
    
    /**
     * 根据订单ID获取预约表信息列表
     * @param orderId
     * @return
     */
    public List<VisaInterviewNotice> list(Long orderId){
    	String sql="select id,country,area,address,interview_time,explanation_time,contact_man,contact_way,create_time,(select count(id) from visa_interview_notice_traveler where interview_id=a.id) num from visa_interview_notice a where order_id=? and del_flag=?";
    	List<Object[]> resultList=visaInterviewNoticeDao.findBySql(sql, orderId,Dict.DEL_FLAG_NORMAL);
    	List<VisaInterviewNotice> list=new ArrayList<VisaInterviewNotice>();
    	VisaInterviewNotice obj;
    	for (Object[] props : resultList) {
			obj=new VisaInterviewNotice();
			obj.setId(StringUtils.toLong(props[0]));
			obj.setOrderId(orderId);
			obj.setCountry((String)props[1]);
			obj.setArea((String)props[2]);
			obj.setAddress((String)props[3]);
			if(props[4]!=null){
				obj.setInterviewTime(DateUtils.dateFormat(props[4].toString()));
			}
			if(props[5]!=null){
				obj.setExplainationTime(DateUtils.dateFormat(props[5].toString()));
			}
			obj.setContactMan((String)props[6]);
			obj.setContactWay((String)props[7]);
			if(props[8]!=null){
				obj.setCreateTime(DateUtils.dateFormat(props[8].toString()));
			}
			obj.setNum(StringUtils.toInteger(props[9]));
			list.add(obj);
		}
    	return list;
    }
    
    /**
     * 根据id删除预约表记录
     * @param id
     * @return
     */
    public int delete(Long id){
    	return visaInterviewNoticeDao.deleteById(id);
    }
    
    /**
     * 添加预约表
     * @param interview
     * @return
     */
    public int add(VisaInterviewNotice interview){
    	visaInterviewNoticeDao.save(interview);
    	if(interview.getId()==null){
    		return 0;
    	}
    	return 1;
    }
    
    /**
     * 更新预约表信息
     * @param interview
     * @return
     */
    public int update(VisaInterviewNotice interview){
    	String sql="update visa_interview_notice set country=?,area=?,address=?,interview_time=?,explanation_time=?,contact_man=?,contact_way=? where id=?";
    	return visaInterviewNoticeDao.updateBySql(sql, interview.getCountry(), interview.getArea(), interview.getAddress(), interview.getInterviewTime(), interview.getExplainationTime(), interview.getContactMan(), interview.getContactWay(), interview.getId());
    }
    
    /**
     * 根据订单ID获取绑定的游客
     * @param orderId
     * @return
     */
    public List<Object> getTravelers(Long orderId){
    	String sql="select id,name from traveler where orderId=? ";
    	List<Object> resultList=visaInterviewNoticeDao.findBySql(sql, orderId);
    	return resultList;
    }
    
    
    /**
     * 根据订单ID获取可面签的客户
     * @param orderId
     * @return
     */
    public List<Object> getUnvisaTravelers(Long orderId){
    	String sql="select t.id,t.name from traveler t, visa visa  where  t.id =visa.traveler_id and t.delFlag=0 and  visa.visa_stauts !=2 and t.orderId=?  ";
    	List<Object> resultList=visaInterviewNoticeDao.findBySql(sql, orderId);
    	return resultList;
    }
    
    /**
     * 根据预约表信息ID获取约签用户
     * @param interviewId
     * @return
     */
    public List<Object> getTravelersBySId(Long interviewId){
    	String sql="select travaler_id,travaler_name from visa_interview_notice_traveler where interview_id=?";
    	List<Object> resultList=visaInterviewNoticeDao.findBySql(sql, interviewId);
    	return resultList;
    }
    
    /**
     * 根据ID获取预约表信息
     * @param id
     * @return
     */
    public VisaInterviewNotice getById(Long id){
    	return visaInterviewNoticeDao.findOne(id);
    }
    
    /**
     * 根据订单ID获取领区地点信息
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getAreaInfoByOrderId(Long orderId){
    	String sql="select (select countryName_cn from sys_country where id=a.country_id) country,area,address from visa_interview_notice_address a where country_id=(select sysCountryId from visa_products where id=(select visa_product_id from visa_order where id=?))";
    	List<Object> resultList=visaInterviewNoticeDao.findBySql(sql, orderId);
    	if(resultList==null || resultList.size()==0){
    		return null;
    	}
    	List<Map<String, Object>> areaList=new ArrayList<Map<String,Object>>();
    	for (Object o : resultList) {
    		Map<String, Object> map=new HashMap<String, Object>();
    		Object[] area=(Object[])o;
    		map.put("country", area[0]);
			map.put("area", area[1]);
			map.put("address", area[2]);
			areaList.add(map);
		}
    	return areaList;
    }
    
    /**
     * 根据预约表ID获取约签人信息列表
     * @param interviewId
     * @return
     */
    public List<Map<String, Object>> getTravelerInfos(Long interviewId){
    	String sql="select a.id,a.name,a.passportCode,a.passportStatus,a.payPriceSerialNum,(select AA_code from visa where traveler_id=a.id) aaCode,(select visaType from visa_products where id=b.visa_product_id) visaType,(select country from visa_interview_notice where id=c.interview_id) country,"
    			+ "v.total_deposit,v.accounted_deposit,v.guarantee_status,b.id as visaOrderId from traveler a inner join visa_order b on a.orderId=b.id inner join visa_interview_notice_traveler c on c.travaler_id=a.id inner join visa v on v.traveler_id = a.id  where c.interview_id=?";
    	List<Object> resultList=visaInterviewNoticeDao.findBySql(sql, interviewId);
    	if(resultList==null){
    		return null;
    	}
    	List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    	for (Object obj : resultList) {
    		Object[] o=(Object[])obj;
    		Map<String, Object> map=new HashMap<String, Object>();
    		//游客id
    		map.put("id", o[0]);
    		//游客名称
    		map.put("name", o[1]);
    		//护照编号
    		map.put("passportCode", o[2]);
    		
    		//护照状态
    		if(null != o[3] && !"".equals(o[3].toString())){
    			if(String.valueOf(o[3].toString()).equals("1")){
    				map.put("passportStatus", "借出");
    			}else if(String.valueOf(o[3].toString()).equals("2")){
    				map.put("passportStatus", "销售已领取");
    			}
//    			else if(String.valueOf(o[3].toString()).equals("3")){
//    				map.put("passportStatus", "未签收");
//    			}else if(String.valueOf(o[3].toString()).equals("4")){
//    				map.put("passportStatus", "已签收");
//    			}
    			else if(String.valueOf(o[3].toString()).equals("4")){
    				map.put("passportStatus", "已还");
    			}
    			else if(String.valueOf(o[3].toString()).equals("5")){
    				map.put("passportStatus", "已取出");
    			}else if(String.valueOf(o[3].toString()).equals("6")){
    				map.put("passportStatus", "未取出");
    			}
//    			else if(String.valueOf(o[3].toString()).equals("7")){
//    				map.put("passportStatus", "走团");
//    			}
    			else if(String.valueOf(o[3].toString()).equals("8")){
    				map.put("passportStatus", "计调领取");
    			}else{
    				map.put("passportStatus", "");
    			}
    		}else{
    			map.put("passportStatus", "");
    		}
    		//应收金额
    		if(null != o[4] && !"".equals(o[4].toString())){
    			map.put("payPrice", getMoney(o[4]+"", null));
    		}else{
    			map.put("payPrice", "");
    		}
    		//AA码
    		map.put("aaCode", o[5]);
    		//签证类型
    		if(null != o[6] && !"".equals(String.valueOf(o[6]))){
    			List<Dict> dictList = findDictByType("new_visa_type");
    			if(null != dictList && dictList.size()>0){
	    			for(int i=0;i<dictList.size();i++){
	    				Dict dict =dictList.get(i);
	    				if(String.valueOf(o[6]).equals(dict.getValue())){
	    					map.put("visaType", dict.getLabel());
	    				}
	    			}
    			}
    		}else{
    			map.put("visaType", "");
    		}
    		//国家
    		map.put("country", o[7]);
    		
    		//应收押金
    		if(null != o[8] && !"".equals(o[8].toString())){
    			map.put("totalDeposit", getMoney(o[8]+"", null));
    		}else{
    			map.put("totalDeposit", "");
    		}
    		//达账押金
    		if(null != o[9] && !"".equals(o[9].toString())){
    			map.put("accountedDeposit", getMoney(o[9]+"", null));
    		}else{
    			map.put("accountedDeposit", "");
    		}
    		//担保状态
    		if(null != o[10] && !"".equals(o[10].toString())){
    			if("0".equals(o[10].toString())){
    				map.put("guaranteeStatus","无");
    			}else if("1".equals(o[10].toString())){
    				map.put("guaranteeStatus","担保");
    			}else if("2".equals(o[10].toString())){
    				map.put("guaranteeStatus","担保+押金");
    			}else if("3".equals(o[10].toString())){
    				map.put("guaranteeStatus","押金");
    			}
    		}else{
    			map.put("guaranteeStatus", "无");
    		}
    		map.put("orderId",o[11].toString());
    		list.add(map);
		}
    	return list;
    }
    /**
     * 根据多个订单id获得面签通知
     * 
     * 
     * */
    @SuppressWarnings("rawtypes")
    public List<File> mianqiantongzhiByOrderId(Long orderId) throws IOException, TemplateException{
    	List<File> fileList = new ArrayList<File>();
    	List<VisaInterviewNotice> results = findByOrderIdAndTravelerId(orderId, -1L);
    	List<VisaInterviewNotice> resultsList = null;
    	if(results != null && !results.isEmpty()){
    		for (int i = 0; i < results.size(); i++) {
    			if (results.get(i) != null) {
					
    				Map<String, Object> root = new HashMap<String, Object>();
    				VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
    				VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
    				VisaInterviewNotice notice_i = results.get(i);
    				VisaInterviewNotice notice = null;
    				int travelerNum =0;
    				String travelerNames = null;
    				//VisaInterviewNotice notice1 = results.get(i);
    				String sql = "select travaler_name from visa_interview_notice_traveler WHERE interview_id=?";
    				List list = visaInterviewNoticeDao.getSession().createSQLQuery(sql)
    						.setParameter(0, notice_i.getId())
    						.list();
    				if(list != null && !list.isEmpty()){
    					travelerNum = list.size();
    					travelerNames = list.toString().substring(1,list.toString().length()-1);
    				}
    				for (int j = i+1; j < results.size(); j++) {
    					VisaInterviewNotice notice_j = results.get(j);
    					if (notice_i.getInterviewTime() != null & notice_i.getExplainationTime() != null & notice_i.getAddress() != null & notice_i.getContactMan() != null & notice_i.getContactWay() != null &
    							notice_j.getInterviewTime() != null & notice_j.getExplainationTime() != null & notice_j.getAddress() != null & notice_j.getContactMan() != null & notice_j.getContactWay() != null
    							) {
    						
    						if ( notice_i.getInterviewTime().compareTo(notice_j.getInterviewTime()) == 0 & 
    								notice_i.getExplainationTime().compareTo(notice_j.getExplainationTime()) == 0 &	
    								notice_i.getAddress().equals(notice_j.getAddress()) &
    								notice_i.getContactMan().equals(notice_j.getContactMan()) &
    								notice_i.getContactWay().equals(notice_j.getContactWay()) ) {
    							
    							/*String sql_i = "select travaler_name from visa_interview_notice_traveler WHERE interview_id=?";
			        	List list_i = visaInterviewNoticeDao.getSession().createSQLQuery(sql_i)
			        		.setParameter(0, notice_i.getId())
			        		.list();*/
    							String sql_j = "select travaler_name from visa_interview_notice_traveler WHERE interview_id=?";
    							List list_j = visaInterviewNoticeDao.getSession().createSQLQuery(sql_j)
    									.setParameter(0, notice_j.getId())
    									.list();
    							if(list_j != null && !list_j.isEmpty()){
    								travelerNum = travelerNum + list_j.size();
    								travelerNames = travelerNames + "  " + list_j.toString().substring(1,list_j.toString().length()-1);
    							}
    							results.set(j, notice);
    						}
    					}
//					else {
//						String sql = "select travaler_name from visa_interview_notice_traveler WHERE interview_id=?";
//			        	List list = visaInterviewNoticeDao.getSession().createSQLQuery(sql)
//			        		.setParameter(0, notice_i.getId())
//			        		.list();
//			        	if(list != null && !list.isEmpty()){
//			        		travelerNum = list.size();
//			        		travelerNames = list.toString().substring(1,list.toString().length()-1);
//			        	}
//					}
    				}
    				root.put("travelerNum", travelerNum);
    				root.put("travelerNames", travelerNames);
    				String interviewTime = "";
    				if (notice_i.getInterviewTime() != null) {
    					interviewTime = changeTime(notice_i.getInterviewTime());
    				}
    				String explainationTime = "";
    				if (notice_i.getExplainationTime() != null) {
    					explainationTime = changeTime(notice_i.getExplainationTime());
    				}
    				
//	    		VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
//	    		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
    				Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
    				
    				root.put("collarZoning", visaProduct.getCollarZoning());
    				root.put("countryNameCn", country.getCountryName_cn());
    				root.put("notice", notice_i);
    				root.put("interviewTime", interviewTime);
    				root.put("explainationTime", explainationTime);
    				
    				/*-需求198—版本0419-根据客户的模板下载面签通知—djw-start-*/
    				if (UserUtils.getUser().getCompany().getUuid().equals("f5c8969ee6b845bcbeb5c2b40bac3a23") && country.getId() == 2481) {
    					if (visaProduct.getCollarZoning().equals("1")) {
    						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_beijing.ftl", "北京面签通知（安家楼路）"+i+".doc", root));
    					}else if(visaProduct.getCollarZoning().equals("5")){
    						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_chengdu.ftl", "成都面签通知"+i+".doc", root));
    					}else if(visaProduct.getCollarZoning().equals("3")){
    						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_shanghai.ftl", "上海面签通知"+i+".doc", root));
    					}else if(visaProduct.getCollarZoning().equals("4")){
    						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_guangzhou.ftl", "广州面签通知"+i+".doc", root));
    					}else if (visaProduct.getCollarZoning().equals("18")){
    						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_shenyang.ftl", "沈阳面谈通知"+i+".doc", root));
    					} else {
    						fileList.add(FreeMarkerUtil.generateFile("interviewNotice.ftl", "面签通知"+i+".doc", root));
    					}
    				} else {
    					fileList.add(FreeMarkerUtil.generateFile("interviewNotice.ftl", "面签通知"+i+".doc", root));
    				}
    				/*-需求198—版本0419-根据客户的模板下载面签通知—djw-end-*/
    			}
    		}
    			
//    		for(int i=0;i<results.size();i++){
//	    		VisaInterviewNotice notice = results.get(i);
//	    		Map<String, Object> root = new HashMap<String, Object>();
    			/*-需求198—版本0419-根据客户的模板下载面签通知—djw-start-*/
    			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 ahh:mm");
    			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    			//String interviewTime = sdf.format(notice.getInterviewTime());
//	    		String interviewTime = "";
//	    		if (notice.getInterviewTime() != null) {
//	    			interviewTime = changeTime(notice.getInterviewTime());
//				}
//	    		String explainationTime = "";
//	    		if (notice.getExplainationTime() != null) {
//	    			explainationTime = changeTime(notice.getExplainationTime());
//				}
    			//String interviewTime = sdf.format(notice.getInterviewTime());
    			//String explainationTime = sdf.format(notice.getExplainationTime());
    			/*-需求198—版本0419-根据客户的模板下载面签通知—djw-end-*/
    			
//	    		VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
//	    		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
//	    		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
    			
//	        	root.put("collarZoning", visaProduct.getCollarZoning());
//	        	root.put("countryNameCn", country.getCountryName_cn());
//	        	root.put("notice", notice);
//	        	root.put("interviewTime", interviewTime);
//	        	root.put("explainationTime", explainationTime);
    			
//	        	String sql = "select travaler_name from visa_interview_notice_traveler WHERE interview_id=?";
//	        	List list = visaInterviewNoticeDao.getSession().createSQLQuery(sql)
//	        		.setParameter(0, notice.getId())
//	        		.list();
//	        	if(list != null && !list.isEmpty()){
//	        		root.put("travelerNum", list.size());
//	        		root.put("travelerNames", list.toString().substring(1,list.toString().length()-1));
//	        	}
    			
//	        	/*-需求198—版本0419-根据客户的模板下载面签通知—djw-start-*/
//	        	if (UserUtils.getUser().getCompany().getUuid().equals("f5c8969ee6b845bcbeb5c2b40bac3a23") && country.getId() == 2481) {
//					if (visaProduct.getCollarZoning().equals("1")) {
//						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_beijing.ftl", "北京面签通知（安家楼路）"+i+".doc", root));
//					}else if(visaProduct.getCollarZoning().equals("5")){
//						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_chengdu.ftl", "成都面签通知"+i+".doc", root));
//					}else if(visaProduct.getCollarZoning().equals("3")){
//						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_shanghai.ftl", "上海面签通知"+i+".doc", root));
//					}else if(visaProduct.getCollarZoning().equals("4")){
//						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_guangzhou.ftl", "广州面签通知"+i+".doc", root));
//					}else if (visaProduct.getCollarZoning().equals("18")){
//						fileList.add(FreeMarkerUtil.generateFile("interviewNoticeAmerica_shenyang.ftl", "沈阳面谈通知"+i+".doc", root));
//					} else {
//						fileList.add(FreeMarkerUtil.generateFile("interviewNotice.ftl", "面签通知"+i+".doc", root));
//					}
//				} else {
//					fileList.add(FreeMarkerUtil.generateFile("interviewNotice.ftl", "面签通知"+i+".doc", root));
//				}
//	        	/*-需求198—版本0419-根据客户的模板下载面签通知—djw-end-*/
//    		}
    		return fileList;
    	}else{
    		return null;
    	}
    }

    /**
     * 根据orderId，travelerId查询一个游客的面签通知
     * 当travelerId= -1 的时候,是按照订单id去查询面签通知的
     * @param orderId
     * @param travelerId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<VisaInterviewNotice> findByOrderIdAndTravelerId(Long orderId, Long travelerId){
    	List<VisaInterviewNotice> results = null;
    	String hql ="";
    	//按照订单或是团号查的时候
    	if (travelerId == -1){
    		hql = "from VisaInterviewNotice where orderId=? and delFlag=? order by createTime desc";//查询条件加上删除标识
    		results = visaInterviewNoticeDao.getSession().createQuery(hql)
    	    		.setParameter(0, orderId)
    	    		.setParameter(1, "0")
    	    		.list();
	    }else if(orderId != null && travelerId !=null){
	    	hql = "from VisaInterviewNotice where orderId=? and id in (select interviewId from VisaInterviewNoticeTraveler where travalerId=? ) order by createTime desc";
	    	results = visaInterviewNoticeDao.getSession().createQuery(hql)
		    		.setParameter(0, orderId)
		    		.setParameter(1, travelerId)
		    		.list();
	    }
    	return results;
    }
    
    @SuppressWarnings("rawtypes")
    public File createInterviewNoticeFile(Long orderId, Long travelerId) throws IOException, TemplateException{
    	List<VisaInterviewNotice> results = findByOrderIdAndTravelerId(orderId, travelerId);
    	if(results != null && !results.isEmpty()){
    		VisaInterviewNotice notice = results.get(0);
    		Map<String, Object> root = new HashMap<String, Object>();
    		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    		//String interviewTime = sdf.format(notice.getInterviewTime());
    		//String explainationTime = sdf.format(notice.getExplainationTime());
    		String interviewTime = changeTime(notice.getInterviewTime());
    		String explainationTime = changeTime(notice.getExplainationTime());
    		
    		VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
    		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
    		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
    		
        	root.put("notice", notice);
        	root.put("interviewTime", interviewTime);
        	root.put("explainationTime", explainationTime);
        	root.put("collarZoning", visaProduct.getCollarZoning());
        	root.put("countryNameCn", country.getCountryName_cn());
        	
        	String sql = "select travaler_name from visa_interview_notice_traveler WHERE interview_id=?";
        	List list = visaInterviewNoticeDao.getSession().createSQLQuery(sql)
        		.setParameter(0, notice.getId())
        		.list();
        	if(list != null && !list.isEmpty()){
        		root.put("travelerNum", list.size());
        		root.put("travelerNames", list.toString().substring(1,list.toString().length()-1));
        	}
        	
        	/*-需求198-根据客户的模板下载面签通知—djw-start-*/
        	if (UserUtils.getUser().getCompany().getUuid().equals("f5c8969ee6b845bcbeb5c2b40bac3a23") && country.getId() == 2481) {
				if (visaProduct.getCollarZoning().equals("1")) {
					return FreeMarkerUtil.generateFile("interviewNoticeAmerica_beijing.ftl", "北京面签通知（安家楼路）.doc", root);
				}else if(visaProduct.getCollarZoning().equals("5")){
					return FreeMarkerUtil.generateFile("interviewNoticeAmerica_chengdu.ftl", "成都面签通知.doc", root);
				}else if(visaProduct.getCollarZoning().equals("3")){
					return FreeMarkerUtil.generateFile("interviewNoticeAmerica_shanghai.ftl", "上海面签通知.doc", root);
				}else if(visaProduct.getCollarZoning().equals("4")){
					return FreeMarkerUtil.generateFile("interviewNoticeAmerica_guangzhou.ftl", "广州面签通知.doc", root);
				}else if (visaProduct.getCollarZoning().equals("18")){
					return FreeMarkerUtil.generateFile("interviewNoticeAmerica_shenyang.ftl", "沈阳面谈通知.doc", root);
				} else {
					return FreeMarkerUtil.generateFile("interviewNotice.ftl", "面签通知.doc", root);
				}
			} else {
				return FreeMarkerUtil.generateFile("interviewNotice.ftl", "interviewNotice.doc", root);
				
			}
        	/*-需求198-根据客户的模板下载面签通知—djw-end-*/
        	
    	}else{
    		return null;
    	}
    	
    }
    /**
     * 0214需求,面签资料附件上传
     * 保存订单和上传附件关系的记录
     * @param visaOrderId
     * @param docInfoIds
     */
	public void save(String visaOrderId, String docInfoIds) {
		String []arrdocInfoId=null;
		List<VisaOrderFile> list=new ArrayList<VisaOrderFile>();
		if(StringUtils.isNotEmpty(docInfoIds)){
			arrdocInfoId=docInfoIds.split(",");
			for(int i=0;i<arrdocInfoId.length;i++){
				VisaOrderFile vof=new VisaOrderFile();
				vof.setVisaOrderId(Long.parseLong(visaOrderId));
				vof.setDocInfoId(Long.parseLong(arrdocInfoId[i]));
				list.add(vof);//将对象保存在list集合中
			}
		 for(VisaOrderFile vofObj : list) {
			visaOrderFileDao.save(vofObj);
			visaOrderDao.clear();
		 }
		}
	}
    /**
     * 204需求-新增面签资料上传附件
     * 根据订单id更新删除标记来达到删除就的记录的目的
     * @param visaOrderId
     */
	public void updateDelFlagByVisaOrderId(String visaOrderId) {
		StringBuffer sb=new StringBuffer();
		sb.append(" UPDATE visa_order_file vof SET vof.del_flag=1 WHERE vof.visa_order_id='"+visaOrderId+"'");
		visaOrderFileDao.updateBySql(sb.toString());
	}
    /**
     * 204需求-新增面签资料上传附件
     * 根据订单id查询该订单已上传附件的信息
     * @param orderId
     * @return
     */
	public List<Map<String, Object>> findDocInfoListByOrderId(String orderId) {
		StringBuffer sql=new StringBuffer(); 
		sql.append(" SELECT vof.docinfo_id AS id,d.docName AS docName  ")
		   .append(" FROM visa_order_file vof  ")
		   .append(" LEFT JOIN docinfo d ")
		   .append(" ON vof.docinfo_id=d.id ")
		   .append(" WHERE vof.del_flag=0 AND vof.visa_order_id='"+orderId+"' ");
		return visaOrderFileDao.findBySql(sql.toString(),Map.class);
	}
	
	/**
	 * 转换时间格式****年**月**日 上（下）午 **时**分
	 * @param date
	 * @return
	 */
	public String changeTime(Date date){
		Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        //Calendar.YEAR:日期中的年  
        int year = c.get(Calendar.YEAR);  
        //Calendar.MONTH:日期中的月，需要加1  
        int mon = c.get(Calendar.MONTH) + 1;  
        String month = Integer.toString(mon);
        if (mon < 10) {
        	month = "0"+month;
		}
        //Calendar.DATE:日期中的日  
        int d = c.get(Calendar.DATE); 
        String day = Integer.toString(d);
        if (d < 10) {
        	day = "0"+day;
		}
        //Calendar.HOUR_OF_DAY：24小时制  
        int hour = c.get(Calendar.HOUR_OF_DAY); 
        String HOUR_OF_DAY = Integer.toString(hour);
        if (hour < 10) {
        	HOUR_OF_DAY = "0"+HOUR_OF_DAY;
		}
        String periodTime = "上午";
        if (hour > 12) {
        	periodTime = "下午";
		}
        //Calendar.MINUTE:日期中的分钟  
        int min = c.get(Calendar.MINUTE);
        String minute = Integer.toString(min);
        if (min < 10) {
        	minute = "0"+minute;
		}
        String time = year+"年"+month+"月"+day+"日  "+periodTime+" "+HOUR_OF_DAY+"时"+minute+"分";
        //System.out.println(time);
        return time;
	}
}
