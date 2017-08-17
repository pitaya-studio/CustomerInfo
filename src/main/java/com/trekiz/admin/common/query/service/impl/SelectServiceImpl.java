package com.trekiz.admin.common.query.service.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.query.entity.SelectJson;
import com.trekiz.admin.common.query.entity.SelectOption;
import com.trekiz.admin.common.query.repository.ISelectDao;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.query.utils.CommonUtils;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.modules.order.entity.ZhifubaoInfo;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.repository.ZhifubaoDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class SelectServiceImpl implements ISelectService{

    private static final Logger log = Logger.getLogger(SelectServiceImpl.class);

	@Autowired
	private ISelectDao selectDao;
	@Autowired
	private UserJobDao userJobDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private ZhifubaoDao zhifubaoDao;

	/**
	 *  @author jinxin.gao
	 *  @return

	@Transactional(readOnly = true)
	public List<SelectOption> findAllAgentinfo() {
		List<SelectOption> agentList = new LinkedList<SelectOption>();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        //如果不是销售经理或管理员，则用户只能查看自己负责的渠道
        if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
        	Long userId = user.getId();
        	boolean isSaleManager = false;
        	boolean isManager = false;
        	boolean isFinance = false;
        	for(Role role : user.getRoleList()) {
        		if(Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())) {
        			isManager = true;
        			break;
        		}
        		if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
        			isSaleManager = true;
        			break;
        		}
        	}
        	List<UserJob> list = userJobDao.getUserJobList(userId);
        	for (UserJob userJob:list) {
        		String jobName = userJob.getJobName();
        		if(jobName.indexOf("财务") != -1){
        			isFinance = true;
        			break;
        		}
			}
        	if(isManager||isFinance||isSaleManager){
        		List<Map<String, Object>> agentInfoList = selectDao.getAgentInfo(userCompanyId);
        		List<SelectOption> selectOptions = CommonUtils.toListSelectOption(agentInfoList);
       		    return selectOptions;
       	    }else {
       	    	return selectDao.findAgentBySalerId(userId);
			}

        }
	    return agentList;
	}
 */
	/**
	 * 获取产品发布人和计调
	 * @param companyId 公司id
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public SelectJson loadGetOperators(Long companyId) {
		SelectJson selectJson=new SelectJson();
		try {
			List<Map<String,Object>> operators = selectDao.getOperators(companyId);
			List<SelectOption> selectOptions = CommonUtils.toListSelectOption(operators);
			selectJson.setData(selectOptions);
		} catch (Exception e) {
			selectJson.setError("系统异常，请联系管理员");
			e.printStackTrace();
		}
		return selectJson;
	}
	
	/*
	 * 根据公司id获取sys_user下单人列表
	 * @param companyId 公司id
	 * @return 
	 * @author xianglei.dong
	 */
	@Override
	public SelectJson loadSysUserPlaceOrderPersons() {
		// TODO Auto-generated method stub
		return loadReviewer();
	}
	
	/*
	 * 根据公司id获取多表联合下单人列表
	 * @param companyId 公司id
	 * @return 
	 * @author xianglei.dong
	 */
	@Override
	public SelectJson loadUnionPlaceOrderPersons() {
		SelectJson json = new SelectJson();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		try{
			List<Map<String, Object>> placePersons = selectDao.getUnionPlaceOrderPersons(currentCompanyId);
			List<SelectOption> list = CommonUtils.toListSelectOption(placePersons);
			json.setData(list);
		}catch(Exception e) {
			log.error(e.getMessage());
			json.setError("数据查询错误，请检查!");
		}
		return json;
	}


    @Override
	public List<Map<String,Object>> loadDepartment(Long companyId) {

		return selectDao.loadDepartment(companyId);
	}


	/**
	 * 获取所有可发布产品的人
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public SelectJson loadAllUsers() {
		SelectJson selectJson=new SelectJson();
		try {
			List<Map<String,Object>> users = selectDao.getAllUsers();
			List<SelectOption> selectOptions = CommonUtils.toListSelectOption(users);
			selectJson.setData(selectOptions);
		} catch (Exception e) {
			selectJson.setError("系统异常，请联系管理员");
			 log.error(e.getMessage());
			e.printStackTrace();
		}
		return selectJson;
	}

	/**
	 * 获取销售
	 * @author jinxin.gao
	 */
	@Override
	public SelectJson loadFindAllAgentinfo() {
		SelectJson selectJson = new SelectJson();
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> list = null;
        try{
            list = selectDao.findInnerSales(companyId);
			List<SelectOption> data = CommonUtils.toListSelectOption(list);
			selectJson.setData(data);
        }catch (Exception e){
            log.error(e.getMessage());
            selectJson.setError("数据查询错误，请检查!");
        }
		return selectJson;
	}

    @Override
    public SelectJson loadReviewer() {
        SelectJson json = new SelectJson();
        Long currentCompanyId = UserUtils.getUser().getCompany().getId();
        List<Map<String, Object>> list = null;
        try{
            list = selectDao.getReviewer(currentCompanyId);
			List<SelectOption> data = CommonUtils.toListSelectOption(list);
			json.setData(data);
        }catch (Exception e){
            log.error(e.getMessage());
            json.setError("数据查询错误，请检查!");
        }
        return json;
    }
	/**
	 * 获取该公司下的渠道商
	 * @return
	 * @author yudong.xu
	 */
	@Override
	public SelectJson loadAgentInfoBySupplyId() {
		SelectJson selectJson=new SelectJson();
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> list = null;
		try{
			list = selectDao.getAgentInfoBySupplyId(companyId);
			List<SelectOption> data = CommonUtils.toListSelectOption(list);
			selectJson.setData(data);
		}catch (Exception e){
			log.error(e.getMessage());
			selectJson.setError("数据查询错误，请检查!");
		}
		return selectJson;
	}

	/**
	 * 获取该员工跟进的渠道商
	 * @return
	 * @author yudong.xu
	 */
	@Override
	public SelectJson loadAgentInfoBySalerId() {
		SelectJson selectJson=new SelectJson();
		Long salerId = UserUtils.getUser().getId();
		List<Map<String, Object>> list = null;
		try{
			list = selectDao.getAgentInfoBySalerId(salerId);
			List<SelectOption> data = CommonUtils.toListSelectOption(list);
			selectJson.setData(data);
		}catch (Exception e){
			log.error(e.getMessage());
			selectJson.setError("数据查询错误，请检查!");
		}
		return selectJson;
	}

	@Override
	public SelectJson loadGetInnerOperator() {
		SelectJson selectJson=new SelectJson();
		try {
			List<Map<String,Object>> operator = selectDao.getInnerOperator();
			List<SelectOption> list = CommonUtils.toListSelectOption(operator);
			selectJson.setData(list);
		} catch (Exception e) {
			selectJson.setError("系统异常，请联系管理员");
			e.printStackTrace();
		}
		return selectJson;
	}

	/*
	 * 获取地接社
	 * @author xianglei.dong
	 */
	@Override
	public SelectJson loadSupplier() {
		SelectJson selectJson = new SelectJson();
		try {
			List<Map<String,Object>> suppliers = selectDao.getSupplier();
			List<SelectOption> list = CommonUtils.toListSelectOption(suppliers);
			selectJson.setData(list);
		} catch (Exception e) {
			selectJson.setError("数据查询错误，请检查!");
			e.printStackTrace();
		}
		return selectJson;
	}


	/**
	 * 获取切位渠道商
	 * @author yudong.xu --2016/4/11--15:51
	 */
	@Override
	public SelectJson loadStockAgentinfo() {
		SelectJson selectJson=new SelectJson();
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> list = null;
		try{
			list = selectDao.getStockAgentinfo(companyId);
			List<SelectOption> data = CommonUtils.toListSelectOption(list);
			selectJson.setData(data);
		}catch (Exception e){
			log.error(e.getMessage());
			selectJson.setError("数据查询错误，请检查!");
		}
		return selectJson;
	}

	/**
	 * 返回渠道商，会根据当前用户的身份来进行区别选择。经理，管理员，财务显示公司所有渠道商。其他返回
	 * 自己跟进的渠道商。
	 * 如果当前用户不是接待社的用户。不返回渠道信息。
	 * @author yudong.xu --2016/4/11--20:35
	 */
	@Override
	public SelectJson loadAgents() {
		User user = UserUtils.getUser();
		if (Context.USER_TYPE_RECEPTION.equalsIgnoreCase(user.getUserType())){
			boolean isShowAll = isShowAllAgentinfo(user);
			if (isShowAll){
				return loadAgentInfoBySupplyId();
			}
			return loadAgentInfoBySalerId();
		}
		return null;
	}


	@Override
	public List<SelectOption> findAllAgentinfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 如果是销售经理，管理员，财务中的任意一个就返回true。否则返回false。
	 * @author yudong.xu --2016/4/11--20:33
	 */
	private boolean isShowAllAgentinfo(User user){
		for (Role role : user.getRoleList()){
			if (Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())){
				return true;
			}
			if (Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())){
				return true;
			}
		}
		List<UserJob> list =userJobDao.getUserJobList(user.getId());
		for (UserJob userJob:list) {
			String jobName = userJob.getJobName();
			if(jobName.indexOf("财务") != -1){
				return true;
			}
		}
		return false;
	}

//	@Override
//	public List<String[]> getOfficePlatBankInfoForSelect() {
//		
//		Long id = UserUtils.getUser().getCompany().getId();
//		
//		List<String[]> banks = officeDao.getOfficePlatBankInfoForSelect(id);
//		return banks;
//	}
	
	@Override
	@Deprecated
	public List<String[]> getOfficePlatBankInfoForSelect() {
		
		Long id = UserUtils.getUser().getCompany().getId();
		
		List<String[]> banks = officeDao.getOfficePlatBankInfoForSelect(id);
		return banks;
	}

	@Override
	public SelectJson getOfficePlatBankInfoForSelectJson() {
		
		Long id = UserUtils.getUser().getCompany().getId();
		List<String[]> banks = officeDao.getOfficePlatBankInfoForSelect(id);
		SelectJson selectjson = new SelectJson();
		List<SelectOption> solist = new ArrayList<SelectOption>();
		
		if(CollectionUtils.isNotEmpty(banks) && banks.size() > 0){
			for(Object[] bank : banks){
				SelectOption so = new SelectOption();
				so.setId(bank[0].toString());
				so.setText(bank[1].toString());
				solist.add(so);
			}
		}
		selectjson.setData(solist);
		
		return selectjson;
	}

	@Override
	public List<Map<String, String>> getFromBanks(Integer companyId) {
		List<Map<String, String>> list = selectDao.getFromBanks(companyId);
		return list;
	}

	@Override
	public List<ZhifubaoInfo> getAlipay(Long companyId){
		if(null == companyId){
			throw new RuntimeException("公司ID不能为空");
		}
		List<ZhifubaoInfo> alipayList = new ArrayList<ZhifubaoInfo>();
		List<Map<String, Object>> list = zhifubaoDao.getOfficeZhifubaoInfo(companyId);
		if(Collections3.isEmpty(list)){
			return alipayList;
		}
		for (Map<String, Object> map : list){
			ZhifubaoInfo zhifubaoInfo = new ZhifubaoInfo();
			Object objId = map.get("id");
			Object objName = map.get("name");
			Object objAccount = map.get("account");
			Object objDefaultFlag = map.get("defaultFlag");
			if(null != objId){
				zhifubaoInfo.setId(Long.valueOf(String.valueOf(objId)));
			}
			if(null != objName){
				zhifubaoInfo.setName(objName.toString());
			}
			if(null != objAccount){
				zhifubaoInfo.setAccount(objAccount.toString());
			}
			zhifubaoInfo.setDefaultFlag(objDefaultFlag == null ? "" : objDefaultFlag.toString());
			alipayList.add(zhifubaoInfo);
		}
		return alipayList;
	}

	@Override
	public List<ZhifubaoInfo> getDistinctAlipay(Long companyId){
		List<ZhifubaoInfo> list = getAlipay(companyId);
		Map<String, ZhifubaoInfo> map = new HashMap<>();
		for (ZhifubaoInfo item : list){
			String alipayName = item.getName();
			ZhifubaoInfo exist = map.get(alipayName);
			if(null == exist){
				map.put(alipayName, item);
			}else{
				if("0".equals(item.getDefaultFlag())){
					map.put(alipayName, item);
				}
			}
		}
		return new ArrayList<ZhifubaoInfo>(map.values());
	}


	@Override
	public ZhifubaoInfo getAlipayById(Long id) {
		ZhifubaoInfo info = zhifubaoDao.findZhifubaoInfoById(id);
		return info;
	}

	@Override
	public List<ZhifubaoInfo> getAlipayByName(String name) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM zhifubao_info WHERE name=? AND delFlag=? AND companyId=? ");
		List<ZhifubaoInfo> list=selectDao.findBySql(sbf.toString(),ZhifubaoInfo.class,name,0,UserUtils.getUser().getCompany().getId().intValue());
		return list;
	}

	@Override
	public List<Map<String,Object>> getT1Agentinfos() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM agentinfo a WHERE a.is_quauq_agent =1 AND a.enable_quauq_agent = 1 ");
		List<Map<String,Object>> list = selectDao.findBySql(sbf.toString(),Map.class);
		return list;
	}

	@Override
	public List<Map<String,Object>> getT1User() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT su.* FROM agentinfo a LEFT JOIN supplier_contacts su ON su.supplierId = a.id WHERE a.is_quauq_agent =1 " +
				"AND a.enable_quauq_agent = 1  AND su.delFlag = 0 group by su.contactName");
		List<Map<String,Object>> users = selectDao.findBySql(sbf.toString(),Map.class);
		return users;
	}
}
