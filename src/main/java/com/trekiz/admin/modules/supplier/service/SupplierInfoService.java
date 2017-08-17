package com.trekiz.admin.modules.supplier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.supplier.entity.Bank;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.entity.SupplierName;
import com.trekiz.admin.modules.supplier.entity.SupplierWebsiteInfo;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.supplier.repository.SupplierNameDao;
import com.trekiz.admin.modules.supplier.repository.SupplierWebsiteInfoDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;

@Service
@Transactional(readOnly = true)
public class SupplierInfoService extends BaseService{
	@Autowired
	private SupplierInfoDao supplierInfoDao;
	@Autowired
	private SupplierNameDao SupplierNameDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private SupplierWebsiteInfoDao supplierWebsiteInfoDao;

	/**
	 * 查询地接社信息
	 * @param request
	 * @param response
	 * @param condMap
	 * @return
	 */
	public Page<Map<String, String>> findSupplierInfo(HttpServletRequest request, HttpServletResponse response, Map<String,Object> condMap, Long companyId){
		Page<Map<String,String>> orderPage = supplierInfoDao.querySupplierOrderListByCond(request, response, condMap, companyId);
		List<Map<String, String>> list = orderPage.getList();
		for(Map<String, String> temp : list){
			// 格式化地接社类型
			if (StringUtils.isNotBlank(temp.get("supplierType"))) {
				String[] supplier = temp.get("supplierType").split(",");
				StringBuffer supplierBuffer = new StringBuffer();
				for (int i = 0; i < supplier.length; i++) {
					//mod start by jiangyang
//					String supplierType = DictUtils.getDict(supplier[i].trim(), "travel_agency_type").getLabel();
					String supplierType = null;
					SysCompanyDictView dictView = DictUtils.getSysCompanyDictView(supplier[i].trim(), Context.BaseInfo.TRAVEL_AGENCY_TYPE);
					if(dictView != null){
						supplierType = dictView.getLabel();
					}
					//mod end   by jiangyang
					supplierBuffer.append(supplierType);
					if(i < supplier.length - 1){
						supplierBuffer.append(" ");
					}
				}
				temp.put("supplierType", supplierBuffer.toString());
			}
			// 根据UUID,对多币种进行拼接
			String money = moneyAmountService.getMoney(temp.get("bussinessUUID"));
			temp.put("bussinessUUID", money);
		}
		return orderPage;
	}

	/**
	 * 取得地接社基本信息
	 * @param id 地接社ID
	 * @return
	 */
	public SupplierInfo findSupplierInfoById(Long supplierId){
		return supplierInfoDao.findOne(supplierId);
	}

	/**
	 * 根据id和类型取得银行账户
	 * @param id 批发商ID/地接社ID/渠道ID
	 * @param type 银行账户类型
	 * @return
	 */
	public List<String> getPlatBankInfo(String id, String type){
		return supplierInfoDao.getPlatBankInfo(id, type);
	}

	/**
	 * 取得银行账户
	 * @param id 地接社ID
	 * @return
	 */
	public List<String> getSupplierPlatBankInfo(Long supplierId){
		return supplierInfoDao.getSupplierPlatBankInfo(supplierId);
	}

	public List<Bank> findBank(Integer platType,Integer belongPlatId){
		return supplierInfoDao.findBank(platType,belongPlatId);
	}
	
	public Bank findBankByNameAccount(String bankName, String bankAccount, Integer belongPlatId) {
		return supplierInfoDao.findBankByNameAccount(bankName, bankAccount, belongPlatId);
	}


	/**
	 * 取得地接社网站信息
	 * @param id 地接社ID
	 * @return
	 */
	public SupplierWebsiteInfo selectSupplierWebsiteInfo(Long supplierId){
		return supplierInfoDao.selectSupplierWebsiteInfo(supplierId);
	}

	/**
	 * 删除地接社信息
	 * @param id 地接社ID
	 * @return
	 */
	public boolean deleteSupplierInfo(Long id){
		return supplierInfoDao.deleteSupplierInfo(id);
	}

	/**
	 * 保存地接社信息
	 * @param supplierInfo 地接社信息
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public SupplierInfo saveSupplierInfo(SupplierInfo supplierInfo) {
		return supplierInfoDao.save(supplierInfo);
	}

	/**
	 * 插入地接社信息
	 * @param type
	 * @param id
	 * @return
	 */
	public boolean insertSupplierMap(Long type, Long id) {
		return supplierInfoDao.insertSupplierMap(type, id);
	}

	/**
	 * 删除地接社信息
	 * @param id 地接社ID
	 * @return
	 */
	public boolean deleteSupplierMap(Long id) {
		return supplierInfoDao.deleteSupplierMap(id);
	}

	/**
	 * 公司所属地接社
	 * @param companyId 所属公司ID
	 * @return
	 */
	public List<SupplierInfo> findSupplierInfoByCompanyId(Long companyId){
		return supplierInfoDao.findSupplierInfoByCompanyId(companyId);
	}

	/**
	 * 所属地国家信息
	 * @return
	 */
	public Map<String,String> findCountryInfo() {
		Map<String,String> map = new HashMap<String,String>();
		List<Object[]> areaInfoList = supplierInfoDao.getCountryInfo();
		for (Iterator<Object[]> iterator = areaInfoList.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			map.put(objects[0].toString(), objects[1].toString());
		}
		return map;
	}

	/**
	 * 根据sys_area的ParentId查当前父节点下的当前子节点，不做递归查询
	 * @param areaParentId 父节点ID
	 * @return  map<地区ID,地区名称>
	 */
	public Map<String,String> findAreaInfo(Long areaParentId){
		Map<String,String> map = new HashMap<String,String>();
		List<Object[]> areaInfoList = supplierInfoDao.getAreaInfo(areaParentId);
		for (Iterator<Object[]> iterator = areaInfoList.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			map.put(objects[0].toString(), objects[1].toString());
		}
		return map;
	}

	/**
	 * 根据国家获得地区
	 * @param areaParentId 父节点ID
	 * @return  map<地区ID,地区名称>
	 */
	public Map<String,String> findArea(String areaParentId){
		Map<String,String> map = new HashMap<String,String>();
		// 数据库“所有父级编号”的格式
		areaParentId = "%,%,%,"+areaParentId+",%";
		List<Object[]> areaInfoList = supplierInfoDao.getArea(areaParentId);
		for (Iterator<Object[]> iterator = areaInfoList.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			map.put(objects[0].toString(), objects[1].toString());
		}
		return map;
	}

	/**
	 * 根据sys_area的编号查父级编号
	 * @param areaId 编号
	 * @return  map<地区ID,地区名称>
	 */
	public Long findAreaParentInfo(Long areaId){
		return supplierInfoDao.getAreaParentInfo(areaId);
	}

	/**
	 * 根据地接社类型，所属公司ID查询地接社名称
	 * @param supplyType 地接社类型
	 * @param companyId 所属公司ID
	 * @return listMap 地接社名称List
	 */
	public List<SupplierName> findSupplierName(Integer supplyType,long companyId){
		List<SupplierName> listMap = new ArrayList<SupplierName>();	
		listMap=SupplierNameDao.findSupplierName(supplyType,companyId);
		return listMap;
	}	

	/**
	 * 根据地接社ID查找网站信息ID
	 * @param supplierId 编号
	 * @return  map<地区ID,地区名称>
	 */
	public SupplierWebsiteInfo findWebsiteInfo(Long supplierId){
		return supplierWebsiteInfoDao.getWebsiteInfoId(supplierId);
	}

	/**
	 * 保存地接社网站信息
	 * @param supplierWebsiteInfo 地接社网站信息
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public SupplierWebsiteInfo saveWebsite(SupplierWebsiteInfo supplierWebsiteInfo) {
		return supplierWebsiteInfoDao.save(supplierWebsiteInfo);
	}

	/**
	 * 根据地接社ID删除银行账户
	 * @param id 地接社ID
	 * @return
	 */
	public boolean deleteSupplierPlatBankInfo(Long id) {
		return supplierInfoDao.deleteSupplierPlatBankInfo(id);
	}

	/**
	 * 插入银行账户
	 * @param defaultFlag 设为默认账户标志(0 是 1 否)
	 * @param accountName 账户名
	 * @param bankName 开户行名称
	 * @param bankAddr 开户行地址
	 * @param bankAccountCode 银行账号
	 * @param remarks 备注
	 * @param beLongPlatId 所属平台ID（0批发商 1地接社 2渠道）
	 * @return
	 */
	public boolean insertSupplierPlatBankInfo(String defaultFlag, String accountName, String bankName,
			String bankAddr, String bankAccountCode, String remarks, Long beLongPlatId) {
		return supplierInfoDao.insertSupplierPlatBankInfo(defaultFlag, accountName, bankName,
				bankAddr, bankAccountCode, remarks, beLongPlatId);
	}

}