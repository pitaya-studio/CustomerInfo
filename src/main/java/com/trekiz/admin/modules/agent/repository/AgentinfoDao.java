package com.trekiz.admin.modules.agent.repository;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 渠道商DAO接口
 * @author liangjingming
 * @version 2014-01-24
 */
public interface AgentinfoDao extends AgentinfoDaoCustom, CrudRepository<Agentinfo, Long>{

	@Modifying
	@Query("update Agentinfo set delFlag = '"+Agentinfo.DEL_FLAG_DELETE+"' where id = ?1")
	public void delAgentinfo(Long agentid);

	// 此方法有问题，故注释掉  update by shijun.liu
	@Query("select id,agentName from Agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and supplyId= ?1 and status = '1' order by agentFirstLetter")
	@Deprecated
	public List<Agentinfo> findAllAgentinfo(Long supplyId);
	
	/*切位渠道商选择，排除非签约渠道 （ID < 0）*/
	@Query("from Agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and (supplyId=0 or supplyId= ?1) and status = '1' and id > 0 order by agentFirstLetter")
	public List<Agentinfo> findStockAgentinfo(Long supplyId);
	
	@Query("select count(*) from Agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and supplyId = ?1 and agentName = ?2 and status = '1'")
	public List<Long> getAgentNameCountBySupply(Long supplyId,String agentName);
	//销售主管，一线销售 ()
	@Query(value="select u.id,u.name from sys_user u where u.delFlag='"+User.DEL_FLAG_NORMAL+"' and u.companyId=?1 and u.id in (select userId from sys_user_role where roleId in (select id from sys_role))",nativeQuery=true)
	public List<Object[]> getInnerSales(Long companyId);
	
	/**
	 * 
	 * 根据批发商ID,获取该批发商下所有拥有计调角色的用户 3-计调, 4-计调主管
	 * @author shijun.liu 2015.03.26
	 * @param  companyId    批发商ID
	 * @return
	 */
	@Query(value="select u.id,u.name from sys_user u where u.delFlag='"+User.DEL_FLAG_NORMAL+"' and u.companyId=?1 and u.id in (select userId from sys_user_role where roleId in (select id from sys_role where roleType in(3,4)))",nativeQuery=true)
	public List<Object[]> getInnerOperator(Long companyId);
	
	@Query(value="from Agentinfo where supplyId=?1 and isSynchronize=" + Agentinfo.IS_SYNCHRONIZED +" and status = '1' ")
	public Agentinfo findSpecialAgentinfo(Long companyId);

	/**
	 * 根据销售查询其所维护的所有渠道，可以使用后面的方法替代{@link com.trekiz.admin.modules.agent.repository.findAgentsThatHasTheSalerId}
	 * 或者{@link com.trekiz.admin.modules.agent.repository.findAgentsHasSalerId}
	 * @param agentSalerId
	 * @return
	 */
	@Deprecated
	@Query(value="from Agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and agentSalerId=?1 and status = '1' order by agentFirstLetter ")
	public List<Agentinfo> findAgentBySalerId(Long agentSalerId);
	
	@Query(value="from Agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and enable_quauq_agent = 1 and supplyId = ?1")
	public List<Agentinfo> findAgentByCompany(Long companyId);
	
	/**
	 * 只有少量字段，如需可以添加或者直接使用后面的方法{@link com.trekiz.admin.modules.agent.repository.findAgentsThatHasTheSalerId}
	 * 字段agentSalerId 存储规则： 逗号连接的诸多id (由long变为了string)
	 * 考虑到模糊查询可能造成的不准确的结果，故而在匹配模型两侧添加了逗号。
	 * 使用时必须严格遵循规则：销售参数在传入之前，必须保证字符串两侧各有一个逗号，字符串不允许出现空格。
	 * @param salerIdString 格式如：  ",366,367,"
	 * @return
	 */
	@Query(value="select id, agentName, agentSalerId from agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and INSTR(CONCAT(',', agentSalerId, ','), '?1') <> 0 and status = '1' order by agentFirstLetter ", nativeQuery=true)
	public List<Object[]> findAgentsHasSalerId(String salerIdString);
	
	@Query(value="from Agentinfo where delFlag='" + Agentinfo.DEL_FLAG_NORMAL + "' and status = '1' and supplyId = ?1 order by agentFirstLetter ")
	public List<Agentinfo> findAgentsByCompanyId(Long companyId);
	
	@Query(value="from OrderContacts where agentId=?1")
	public List<OrderContacts> findAgentContactsByagentId(Long agentId);
	
	@Query(value="select id,name from Area where parent.id = ?1")
	public List<Object[]> getAreaInfo(Long areaParentId);
	
//	@Query(value="select id,label from Dict where type = ?1 order by  createDate")
//	public List<Object[]> findAllPaymentType(String paymentType);
	
	@Query(value="select id,label from Dict where type = '"+Context.PAY_MENT_TYPE+"' order by  createDate")
	public List<Object[]> findAllPaymentType();
	
	@Query(value="from Agentinfo where id = ?1 ")
	public Agentinfo findAgentInfoById(Long id);
	
	@Modifying
	@Query(value="delete  from SupplyContacts s where s.supplierId = ?1")
	public void deleteSupplyContacts(long id);
	
	@Modifying
	@Query(value="delete from PlatBankInfo b where b.platType = ?1 and b.beLongPlatId = ?2")
	public void delBankByAgentinfoId(Integer platType, Long id);
	
	@Query(value="select id,name from Area where type = 2 and delFlag = 0 ")
	public List<Object[]> getCountryInfo();

	/**
	 * @author wuqiang 2015-03-18
	 * 根据批发商ID,获取该批发商下所有拥有计调角色的用户
	 * 3-计调, 4-计调主管
	 */
	@Query(value="select u.id,u.name from sys_user u where u.delFlag='"+User.DEL_FLAG_NORMAL+"' and u.companyId=?1 and u.id in (select userId from sys_user_role where roleId in (select id from sys_role where roleType=3 or roleType=4))",nativeQuery=true)
	public List<Object[]> getInnerJd(Long companyId);
	
	/**
	 * 获取可发布产品的用户
	 * @param companyId
	 * @return
	 */
	@Query(value="select u.id,u.name from sys_user u where u.delFlag='"+User.DEL_FLAG_NORMAL+"' and u.companyId=?1 ",nativeQuery=true)
	public List<Object[]> getAllUsers(Long companyId);
	
	
	/**
	 * 根据渠道商id获取渠道商名称
		 * @Title: getAgentNameById
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-24 下午5:22:21
	 */
	@Query(value="select agentName from Agentinfo where id = ?1")
	public String getAgentNameById(Long id);
	
	@Modifying
	@Query("update Agentinfo set agentFirstLetter=?1  where agentName=?2")
	public void updateData(String agentFirstLetter, String agentName);

	@Query("from Agentinfo  where agentName=?1")
	public Agentinfo findagentinfoByName(String orderCompanyNameShow);
	
	@Modifying
	@Query(value = "update Agentinfo set t1_list_flag =?2 where id=?1 and delFlag=0")
	public int changeT1ListShowFlag(Long id,Integer t1ListFlag);

}

/**
 * DAO自定义接口
 * @author liangjingming
 */
interface AgentinfoDaoCustom extends BaseDao<Agentinfo> {
	public List<String> findAgentBankInfoByNameAndId(long id, String bankName);
	
	/**
	 * 根据id集合获取渠道信息集合
	 * @Description: 
	 * @param @param idList
	 * @param @return   
	 * @return List<Agentinfo>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-30 下午2:41:21
	 */
	public List<Agentinfo> findAgentinfosByIds(List<String> idList);

	/**
	 * 字段agentSalerId 存储规则： 逗号连接的诸多id (由long变为了string)
	 * 考虑到模糊查询可能造成的不准确的结果，故而在匹配模型两侧添加了逗号。
	 * 使用时必须严格遵循规则：销售参数在传入之前，必须保证字符串两侧各有一个逗号，字符串不允许出现空格。
	 * @param salerIdString 格式如：  ",366,367,"
	 * @return
	 */
	public List<Agentinfo> findAgentsThatHasTheSalerId(String salerIdString);
	
	/**
	 * 获取通讯录
	 * @return
	 */
	public List<Map<String, String>> getAddressList();
}

/**
 * DAO自定义接口实现
 * @author liangjingming
 */
@Repository
class AgentinfoDaoImpl extends BaseDaoImpl<Agentinfo> implements AgentinfoDaoCustom {
	
	public List<String> findAgentBankInfoByNameAndId(long id, String bankName) {
		String sqlStr = "select id,defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks from plat_bank_info where beLongPlatId = ? and bankName=? and platType = 2 order by defaultFlag";
		List<String > list = this.findBySql(sqlStr, new Object[]{id,bankName});
		return list;
	}
	
	/**
	 * 根据id集合获取渠道信息集合
	 * @Description: 
	 * @param @param idList
	 * @param @return   
	 * @return List<Agentinfo>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-30 下午2:41:21
	 */
	public List<Agentinfo> findAgentinfosByIds(List<String> idList) {
		if(CollectionUtils.isEmpty(idList)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
//		islandList = super.find("from Island island where island.delFlag="+BaseEntity.DEL_FLAG_NORMAL+" and island.wholesalerId=?",companyId);
		sb.append("from Agentinfo agentinfo where agentinfo.delFlag="+BaseEntity.DEL_FLAG_NORMAL+" and agentinfo.id in ");
		sb.append("(");
		for(String id : idList) {
			if(StringUtils.isEmpty(id)) {
				continue;
			}
			sb.append(id.trim());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return super.find(sb.toString());
	}

	@Override
	public List<Agentinfo> findAgentsThatHasTheSalerId(String salerId) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("from Agentinfo where delFlag = " + Context.DEL_FLAG_NORMAL);
		stringBuffer.append(" and status = '1' ");
		stringBuffer.append(" and INSTR(CONCAT(',', agentSalerId, ','), '" + salerId + "') <> 0 ");
		stringBuffer.append(" order by agentFirstLetter ");
		List<Agentinfo> agentinfos = super.find(stringBuffer.toString());
		return agentinfos;
	}

	/**
	 * ORDER BY loginStatus DESC
	 */
	@Override
	public List<Map<String, String>> getAddressList() {
		String sql = "SELECT tt.`name`, tt.loginName, tt.userId, tt.agentId, tt.login_status as loginStatus, tt.agentName,tt.agentFirstLetter, tt.agentBrand, tt.isQuauqAgent, tt.enableQuauqAgent, tt.delFlag, tt.agentType, tt.agentParent, tt.supplyId, tt.agent_type FROM ( SELECT u.`name`, u.loginName, u.id AS userId, u.login_status, a.id AS agentId, a.agentName,a.agentFirstLetter, a.agentBrand, a.`is_quauq_agent` AS isQuauqAgent, a.enable_quauq_agent AS enableQuauqAgent, a.delFlag, a.agent_type, ( SELECT sct.`name` FROM sys_customer_type sct WHERE sct.del_flag = '0' AND sct.`value` = a.agent_type AND sct.`value` != 3 ) AS agentType, ( CASE a.agent_parent WHEN '-1' THEN '-1' ELSE ( SELECT b.agentName FROM agentinfo b WHERE b.id = a.agent_parent AND b.delFlag = 0 ) END ) AS agentParent, a.supplyId supplyId FROM agentinfo a LEFT JOIN sys_user u ON a.id = u.agentId WHERE 1 = 1 ) tt WHERE tt.isQuauqAgent = 1 AND tt.enableQuauqAgent = 1 AND tt.delFlag = 0 ";
		return super.findBySql(sql, Map.class);
	}

}