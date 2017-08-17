/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.order.entity.ZhifubaoInfo;
import com.trekiz.admin.modules.sys.entity.Office;

/**
 * 支付宝DAO接口
 * @version 
 */
public interface ZhifubaoDao extends ZhifubaoDaoCustom, CrudRepository<ZhifubaoInfo, Long> {

	@Query("from ZhifubaoInfo where id= ? and delflag = 0")
	public ZhifubaoInfo findZhifubaoInfoById(Long id);
}

/**
 * DAO自定义接口
 * @author 
 */
interface ZhifubaoDaoCustom extends BaseDao<Office> {
	
	/**
	 * 查询支付宝账户
	 */
	List<Map<String, Object>> getOfficeZhifubaoInfo(Long id);
	
	/**
	 * 删除支付宝账户
	 */
	boolean deleteOfficeZhifubaoInfo(Long id);
	
	/**
	 * 插入支付宝账户
	 */
	boolean insertOfficeZhifubaoInfo(String defaultFlag, String name, String account, String remark, Long companyId);
}

/**
 * DAO自定义接口实现
 * @author 
 */
@Repository
class ZhifubaoDaoImpl extends BaseDaoImpl<Office> implements ZhifubaoDaoCustom {
	
	/**
	 * 根据批发商ID查询支付宝账户
	 */
	public List<Map<String, Object>> getOfficeZhifubaoInfo(Long id) {
		String getOfficeContactsSql = "SELECT id, account, name, remarks, defaultFlag FROM zhifubao_info WHERE companyId = ? AND plat_type = 0 AND delflag = 0";
		List<Map<String, Object>> zhibubaoInfo = findBySql(getOfficeContactsSql,Map.class, id);
		return zhibubaoInfo;
	}
	
	/**
	 * 根据批发商ID删除支付宝账户
	 */
	public boolean deleteOfficeZhifubaoInfo(Long id){
		String deleteOfficeZhifubaoInfo = "UPDATE zhifubao_info set delFlag = 1 where companyId = ? and plat_type = 0";
		int count = updateBySql(deleteOfficeZhifubaoInfo, id);
		return count == 0?false:true;
	}
	
	/**
	 * 根据批发商ID插入支付宝账户
	 */
	public boolean insertOfficeZhifubaoInfo(String defaultFlag, String name, String account, String remark, Long companyId) {
		String insertOfficePlatBankInfoSql = "INSERT INTO zhifubao_info (" +
				" defaultFlag," +
				" name," +
				" account," +
				" remarks," +
				" plat_type," +
				" companyId," +
				" uuid) " +
				" VALUES ( ?, ?, ?, ?, 0, ?, ?)";
		int count = updateBySql(insertOfficePlatBankInfoSql, defaultFlag, name,
				account, remark, companyId, UuidUtils.generUuid());
		return count == 0?false:true;
	}
}
