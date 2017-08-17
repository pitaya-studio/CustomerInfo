package com.trekiz.admin.modules.sys.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Currency;

public interface CurrencyDao extends CurrencyDaoCustom, CrudRepository<Currency, Long>{

	/**
	 * 查找所有可用的货币
	 * @return
	 */
	@Query("from Currency where  delFlag = 0")
	public Iterable<Currency> findAllAvailableCurrencies();
	
	/**
	 * 根据id查找可用的货币
	 * @param currencyId
	 * @return
	 */
	@Query("from Currency where id = ?1 and delFlag = 0")
	public Currency findById(Long currencyId);
	
	@Query("from Currency where id = ?1 and delFlag != ?2")
	public Currency findID(Long currencyId,String status);
	
	@Query("from Currency where id = ?1 and delFlag =0 and createCompanyId = ?2")
	public Currency findID(Long currencyId,long companyId);
	
	/*@Query("from Currency where delFlag =?1 and createCompanyId = ?2 order by id asc")
	public List<Currency> findListByID(String status, Long companyId);*/
	@Query("from Currency where delFlag =?1 and createCompanyId = ?2 order by id asc")
	public List<Currency> findListByID(String status, Long companyId);
	 
	@Query("from Currency where delFlag ='" + Currency.DEL_FLAG_NORMAL + "' and displayFlag = '" + Currency.SHOW  + "' and createCompanyId = ?1 order by sort asc,update_date DESC")
	public List<Currency> findListByCompanyId(Long companyId);
	
	@Query("from Currency where delFlag ='" + Currency.DEL_FLAG_NORMAL  + "' and createCompanyId = ?1 order by sort asc,update_date DESC")
	public List<Currency> findByCompanyId(Long companyId);
	
	@Modifying
	@Query("update Currency set currency_name=?2,currency_mark=?3,currency_exchangerate=?4,createCompany_id=?5,remark=?6,update_by=?7,update_date=?8,del_flag=?9 where id=?1")
	public void updateCurrency(Long currencyId,String currencyName,String currencyMark,BigDecimal currencyExchangerate,Long createCompanyId,
			String remark,Long updateBy,Date updateDate,String delFlag);
	
	@Modifying
	@Query("update Currency set delFlag = '"+Currency.DEL_FLAG_DELETE+"' where id = ?1 ")
	public void deleteCurrency(Long currencyId);
	
	@Query("from Currency where createCompanyId = ?1 and currencyName = ?2 and id != ?3 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<Currency> checkName(Long companyId, String currencyName, Long id);
	
	@Query("from Currency where createCompanyId = ?1 and currency_mark = ?2 and id != ?3 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<Currency> checkCurrencyMark(Long companyId, String currencyMark, Long id);
	
	public Currency findCurrencyMarkById(Long currencyId);
	
	@Query("from Currency where createCompanyId = ?1 and currencyName = '人民币' and delFlag = '" + Currency.DEL_FLAG_NORMAL + "'")
	public List<Currency> getRMBCurrencyId(Long companyId);
	
	@Query("from Currency where createCompanyId in (?1) and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<Currency> getMyCurrencyList(List<Long> companyIds);
	@Modifying
	@Query("update Currency set sort = 50 where sort =0 and  createCompanyId = ?1 ")
	public void updateDefaultCurrency(Long companyId);
	
	@Query("select convertLowest from Currency where id = ?1 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public BigDecimal findConvertLowestById(Long currencyId);
	
	@Query("select currencyExchangerate from Currency where id = ?1 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public BigDecimal findExchangerageById(Long currencyId);
	
	@Query("from Currency where delFlag ='" + Currency.DEL_FLAG_NORMAL + "' and displayFlag = '" + Currency.SHOW  + "' and createCompanyId = ?1 order by sort,id asc")
	public List<Currency> findSortListByCompanyId(Long companyId);
	
	@Query("from Currency where id in (?1) and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<Currency> getCurrencysByIds(List<Long> currencyIds);
	
	/**
	 * 获取人民币
	 * @param currencyId
	 * @param companyId
	 * @return
	 */
	@Query("from Currency where createCompanyId = ?1 and currencyName = '人民币' and delFlag = " + Currency.DEL_FLAG_NORMAL)
	public Currency findRMB(long companyId);
	
	@Query("from Currency where currencyMark = ?1 and createCompanyId = ?2 and delFlag = " + Currency.DEL_FLAG_NORMAL)
	public List<Currency> findCurrencyByCurrencyMark(String currencyMark, Long companyId);
}

interface CurrencyDaoCustom extends BaseDao<Currency>{
	Map<String,String> getCurrencyMark();
}

@Repository
class CurrencyDaoImpl extends BaseDaoImpl<Currency> implements CurrencyDaoCustom {

	@Override
	public Map<String, String> getCurrencyMark() {
		 javax.persistence.Query  query = super.getEntityManager().createNativeQuery("select currency_id, currency_mark from currency");  
		 List rows = query.getResultList();  
		 Map<String,String> currencyMarkMap = new HashMap<String,String>();
	    for (Object row : rows) {  
	    	Object[] cells = (Object[]) row;  
	    	currencyMarkMap.put(cells[0].toString(), cells[1].toString());
	    }  
		return currencyMarkMap;
	}
	
}
