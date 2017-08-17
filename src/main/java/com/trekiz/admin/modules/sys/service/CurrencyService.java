package com.trekiz.admin.modules.sys.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class CurrencyService extends BaseService {

	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private AirticketPreOrderDao airticketDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	
	
	/**
	 * 根据币种ID，获得该币种详情
	 * @param CurrencyID
	 * @return
	 */
	public Currency findCurrency(Long CurrencyID){
		Currency cu = new Currency();
		if(CurrencyID!=null && CurrencyID>0){
		//	cu =  currencyDao.findID(CurrencyID, Currency.DEL_FLAG_DELETE);
			cu = currencyDao.findOne(CurrencyID);
		}
		return cu;
	}
	
	public Currency findById(Long currencyId) {
		if (currencyId == null || currencyId <= 0) {
			return null;
		}
		Currency currency = currencyDao.findOne(currencyId);
		return currency;
	}
	
	/**
	 * 根据币种ID，获得该币种名称
	 * @param CurrencyID
	 * @return
	 */
	public String findCurrencyName(Long CurrencyID){
		if (CurrencyID == null || CurrencyID <= 0) {
			return null;
		}
		Currency cu = new Currency();
		cu = currencyDao.findOne(CurrencyID);
		if (cu == null) {
			return null;
		}
		return cu.getCurrencyName();
	}
	/**
	 * 根据币种ID，获得该币种符号
	 * @param CurrencyID
	 * @return
	 */
	public String findCurrencyMark(String currencyID){
		if (StringUtils.isBlank(currencyID) || Integer.parseInt(currencyID) == 0) {
			return null;
		}
		Currency cu = new Currency();
		cu = currencyDao.findOne(Long.parseLong(currencyID));
		if (cu == null) {
			return null;
		}
		return cu.getCurrencyMark();
	}
	/**
	 * 根据币种状态，获取币种列表
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public List<Currency> findCurrencyList(String status){
		List<Currency> currencyList = new ArrayList<Currency>();
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(StringUtils.isNotEmpty(status) && StringUtils.validateInt(status)){
			currencyList = currencyDao.findListByID(status, companyId);
		}
		return currencyList;
	}
	
	/**
	 * 根据用户所属批发商，获取正在使用的币种列表（有显示状态判断）
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public List<Currency> findCurrencyList(Long companyId){
		List<Currency> currencyList = new ArrayList<Currency>();
		if(companyId != null){
			currencyList = currencyDao.findListByCompanyId(companyId);
		}
		return currencyList;
	}
	
	/**
	 * 根据用户所属批发商，获取正在使用的币种列表(无显示状态判断)
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public List<Currency> findCompanyCurrencyList(Long companyId){
		List<Currency> currencyList = new ArrayList<Currency>();
		if(companyId != null){
			currencyList = currencyDao.findByCompanyId(companyId);
		}
		return currencyList;
	}
	
	/**
	 * 根据用户所属批发商，获取正在使用的人民币币种
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public Currency findRMBCurrency(Long companyId){
		Currency RMB = new Currency();
		if(companyId != null){
			RMB = currencyDao.findRMB(companyId);
		}
		return RMB;
	}
	
	/**
	 * 根据用户所属批发商，获取正在使用的人民币币种
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public Currency findRMBCurrency(){
		Currency RMB = new Currency();
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null){
			RMB = currencyDao.findRMB(companyId);
		}
		return RMB;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2015-09-11
	 * 根据用户所属批发商，获取正在使用的币种列表(sort排序)
	 * @param CurrencyID
	 * @param status
	 * 
	 * @return
	 */
	public List<Currency> findSortCurrencyList(Long companyId){
		List<Currency> currencyList = new ArrayList<Currency>();
		if(companyId != null){
			currencyList = currencyDao.findSortListByCompanyId(companyId);
		}
		return currencyList;
	}
	
	/**
	 * 根据传入的币种对象，添加/修改该币种记录
	 * @param currency
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void createCurrency(Currency currency){
		
		if(currency.getId() == null)
			currency.setCreateCompanyId(UserUtils.getUser().getCompany().getId());
		else {
			Currency oldCurrency = currencyDao.findOne(currency.getId());
			// 币种
			oldCurrency.setCurrencyName(currency.getCurrencyName());
			// 符号
			oldCurrency.setCurrencyMark(currency.getCurrencyMark());
//			oldCurrency.setCurrencyStyle(currency.getCurrencyStyle());
			// 汇率
			
			// 现金收款
			oldCurrency.setConvertCash(currency.getConvertCash());
			// 对公收款
			oldCurrency.setConvertForeign(currency.getConvertForeign());
			// 中行折算价
			oldCurrency.setConvertAbc(currency.getConvertAbc());
			// 最低汇率标准
			
			if ("人民币".equals(currency.getCurrencyName())) {
				// 汇率
				oldCurrency.setCurrencyExchangerate(new BigDecimal(1));
				// 最低汇率
				oldCurrency.setConvertLowest(new BigDecimal(1));
			} else {
				oldCurrency.setCurrencyExchangerate(currency.getCurrencyExchangerate());
				oldCurrency.setConvertLowest(currency.getConvertLowest());
			}
			oldCurrency.setRemark(currency.getRemark());
			oldCurrency.setSort(currency.getSort());
			oldCurrency.setDisplayFlag(currency.getDisplayFlag());
			currency = oldCurrency;
		}
		currencyDao.save(currency);
	}
	
	/**
	 * 根据传入的币种对象，删除该币种记录(物理删除，请注意！)
	 * @param currency
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delCurrency(Currency currency){
		if(currency!=null){
			currencyDao.delete(currency.getId());
		}
	}
	
	/**
	 * 根据传入的币种ID，删除该币种记录(逻辑删除，请注意！)
	 * @param currency
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delCurrency(Long id){
		if(id!=null){
			currencyDao.deleteCurrency(id);
		}
	}
	
	/**
	 * 验证币种名称是否可以使用
	 * @param id
	 * @param currencyName
	 * @return String
	 */
	public String check(String currency_name, Long id) {
		
		if(!currencyDao.checkName(UserUtils.getUser().getCompany().getId(), currency_name, id).isEmpty())
			return "false";
			else
		return "true";	
	}
	
	/**
	 * 验证币种符号是否可以使用
	 * @param id
	 * @param currencyName
	 * @return String
	 */
	public String checkCurrencyMark(String currencyMark, Long id) {
		
		if(!currencyDao.checkCurrencyMark(UserUtils.getUser().getCompany().getId(), currencyMark, id).isEmpty())
			return "false";
			else
		return "true";	
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param companyId 批发商ID
	 * @return
	 */
	public Page<Currency> searchCurrencyPage(Page<Currency> page, Long companyId) {
		String sql = "from Currency where delFlag = '" + Dict.DEL_FLAG_NORMAL
				+ "' and createCompanyId = " + companyId + " order by sort asc,update_date DESC";
		return currencyDao.find(page, sql);
	}
	
	/**
	 * 由对象LIST取得相应对象的ID并拼接
	 * @param list
	 * @return
	 */
	public String getIdFromListObj(List<Currency> list) {
		StringBuffer sbRes = new StringBuffer();
		for (Currency currency : list) {
			sbRes.append(currency.getId());
			sbRes.append(",");
		}

		if(sbRes.length() > 0){
			sbRes.deleteCharAt(sbRes.length() - 1);
		}

		return sbRes.toString();
	}
	
	/**
	 * 保存显示状态
	 * @param currencyIdArr 页面所有的ID数组
	 * @param checkedIdArr 选中的ID数组
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void saveDispStatus(String[] currencyIdArr,String[] checkedIdArr){
		List<String> cancelIdLst = new ArrayList<String>();
		
		for(String currencyId : currencyIdArr){
			boolean existFlg = false;
			for(String checkedId : checkedIdArr){
				if(currencyId.equals(checkedId)){
					existFlg = true;
					break;
				}
			}
			
			if(!existFlg){
				cancelIdLst.add(currencyId);
			}
		}
		
		Currency currency = null;
		for(String checkedId : checkedIdArr) {
			currency = currencyDao.findOne(Long.parseLong(checkedId));
			if(currency != null){
				// 显示（checked状态）
				currency.setDisplayFlag(Currency.SHOW);
				currencyDao.save(currency);
			}
		}
		for(String cancelId : cancelIdLst) {
			currency = currencyDao.findOne(Long.parseLong(cancelId));
			if(currency != null){
				// 不显示（unchecked状态）
				currency.setDisplayFlag(Currency.HIDE);
				currencyDao.save(currency);
			}
		}
	}
	public List<Currency> findCurrcyListBySerium(String originalPayPriceSerialNum) {
		// TODO Auto-generated method stub
		String hql =" select distinct cu from Currency cu , MoneyAmount ma where ma.currencyId = cu.id  and ma.serialNum='"+ originalPayPriceSerialNum+"'";
		List<Currency>  list = currencyDao.find(hql);
		return list;
	}
	
	/**
	 * 取得前台页面显示的货币信息
	 * @return
	 */
	public List<Integer> findDispCurrencyInfo() {
		String sql = "select t.currency_id from currency t where t.create_company_id = ? and t.del_flag = ? and t.display_flag = ?";
		List<Integer> list = currencyDao.findBySql(sql, UserUtils.getUser()
				.getCompany().getId(), Currency.DEL_FLAG_NORMAL, Currency.SHOW);
		return list;
	}

	
	/**
	 * 由币种ID取得相应的换汇汇率-公司最低汇率标准
	 * @param ids
	 * @return
	 */
	public BigDecimal[] findConvertLowestByIds(String ids) {
		String sql = "select t.convert_lowest from currency t where t.currency_id in (?)";
		List<Long> list = currencyDao.findBySql(sql, ids);
		return list.toArray(new BigDecimal[list.size()]);
	}
	
	/**
	 * 获取当前用户人民币币种
	 * @return
	 */
	public Currency getRMBCurrencyId() {
		List<Currency> currencyList = currencyDao.getRMBCurrencyId(UserUtils.getUser().getCompany().getId());
		if (CollectionUtils.isNotEmpty(currencyList)) {
			return currencyList.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 获取指定批发商的人民币币种
	 * @return
	 */
	public Currency getRMBCurrencyByOffice(Long officeId) {
		List<Currency> currencyList = currencyDao.getRMBCurrencyId(officeId);
		if (CollectionUtils.isNotEmpty(currencyList)) {
			return currencyList.get(0);
		} else {
			return null;
		}
	}
    
	/**
	 * 根据用户所属供应商，获取正在使用的币种列表
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public List<Currency> getMyCurrencyList(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> companyIds = new ArrayList<Long>();
		companyIds.add((long)1);
		companyIds.add(companyId);
		return currencyDao.getMyCurrencyList(companyIds);
	}
	
	/**
	 * 根据用户所属供应商，可添加币种列表
	 * @param CurrencyID
	 * @param status
	 * @return
	 */
	public List<Currency> getMyAddCurrencyList(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> cList1 = currencyDao.findListByCompanyId((long)1);
		List<Currency> cList2 = currencyDao.findListByID(0+"",companyId);
		List<Currency> list = Lists.newArrayList();
		
		for(Currency c : cList1){
			boolean flag = true;
			for(Currency cc : cList2){
				if(c.getCurrencyName().equals(cc.getCurrencyName())){
					flag = false;
				}
			}
			if(flag){
				list.add(c);
			}
		}
		
		return list;
	}

	/**
	 * 保存显示状态
	 * @param sort 页面所有的排序数组
	 * @param currencyIds 当前页面所有的ID数组
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void saveSuperCurrencySort(Integer[]sort,Long[]currencyIds){
		if(0 < currencyIds.length){
			for(int i = 0;i < currencyIds.length; i++) {
				Currency currency = currencyDao.findOne(currencyIds[i]);
				if(currency != null){
					// 显示（checked状态）
					currency.setSort(sort[i]);
					currencyDao.save(currency);
				}
			}
		}
		
	}
	/**
	 * 根据传入的币种对象，删除该币种记录(物理删除，请注意！)
	 * @param currency
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void updateDefaultCurrency(){
		currencyDao.updateDefaultCurrency(UserUtils.getUser().getCompany().getId());
	}
	
	/**
	 * 根据币种ID集合获取币种集合
	 * @Description: 
	 * @param @param ids
	 * @param @return   
	 * @return List<Currency>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-7
	 */
	public List<Currency> getCurrencysByIds(String[] ids) {
		if(ids == null || ids.length == 0) {
			return null;
		}
		List<Long> currencyIds = new ArrayList<Long>();
		for(String id : ids) {
			currencyIds.add(Long.parseLong(id));
		}
		return currencyDao.getCurrencysByIds(currencyIds);
	}
	
	/**
	 * 查询activitygroup activity_airticket money_amount中有无引用币种信息
	 * @param id
	 * @return true 引用  false 无引用
	 */
	public Boolean checkCurrency(Integer id){
		List<Integer> airticketIds = airticketDao.findIdByCurrencyId(Long.parseLong(id.toString()));
		List<Integer> montyAmoutIds = moneyAmountDao.findIdsByCurrencyId(id);
		List<Integer> groupIds = activityGroupDao.findIdsByCurrencyType("%"+id.toString()+"%"); 
		// 有一个不为空，则应用了币种信息
		if(airticketIds!=null||montyAmoutIds!=null||groupIds!=null){
			return true;
		}
		return false;
	}
	
	public Currency findCurrencyByCurrencyMark(String currencyMark, Long companyId) {
		if(StringUtils.isNotBlank(currencyMark) && companyId != null){
			List<Currency> currencys = currencyDao.findCurrencyByCurrencyMark(currencyMark, companyId);
			if(CollectionUtils.isNotEmpty(currencys)) {
				return currencys.get(0);
			}
			else {
				return null;
			}
		}else {
			return null;
		}
	}

	public Map<String, String> getCurrencyMarks() {
		return currencyDao.getCurrencyMark();
	}
}
