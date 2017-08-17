package com.trekiz.admin.modules.mobile.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.mobile.entity.MobileUser;
/**
 * 微信端用户dao
 * @describe:TODO
 * @author:zhanyu.gu
 * @time:2017-1-22 下午2:28:47
 */
public interface MobileUserDao extends MobileUserDaoCustom,CrudRepository<MobileUser, Long>{

}
interface MobileUserDaoCustom extends BaseDao<MobileUser>{
	public List<Map<String ,Object>> getMobileUserList();
} 
@Repository
class MobileUserDaoImpl extends BaseDaoImpl<MobileUser> implements MobileUserDaoCustom{

	@Override
	public List<Map<String ,Object>> getMobileUserList() {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		
		sql.append("select m1.agentName,m1.`name`,m1.telephone,m1.wechatCode,m1.phone from mobile_user");
				
		findBySql(sql.toString(),Map.class);
		
		return null;
	}
	
}