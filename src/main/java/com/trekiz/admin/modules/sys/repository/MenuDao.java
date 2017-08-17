/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.util.List;
import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 菜单DAO接口
 * @author zj
 * @version 2013-11-19
 */
public interface MenuDao extends MenuDaoCustom, CrudRepository<Menu, Long> {

	@Modifying
	@Query("update Menu set delFlag='" + Menu.DEL_FLAG_DELETE + "' where id = ?1 or parentIds like ?2")
	public int deleteById(Long id, String likeParentIds);
	
	@Query("from Menu where (name = '预订' or name = '订单' or name = '产品' or name = '询价' or name = '运控') and delFlag='" + Menu.DEL_FLAG_NORMAL + "'")
	public List<Menu> findByNames();
	
	@Query("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' and href = ?1 order by id desc")
	public List<Menu> findByHref(String href);
	
	@Query("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' and parentIds like ?1")
	public List<Menu> findByParentIdsLike(String parentIds);
	
	public List<Menu> findByPermission(String permission);
	
	@Query("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' and id in ?1")
	public List<Menu> findByIds(List<Long> ids);

//	@Query("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' order by sort")
//	public List<Menu> findAllList();

//	@Query("select distinct m from Menu m, Role r, User u where m.isShow=1 and m in elements (r.menuList) and r in elements (u.roleList)" +
//			" and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL +
//			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 " + // or (m.user.id=?1  and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "')" +
//			" order by m.sort")
//	public List<Menu> findByUserId(Long userId);
	
	@Query(value="select m.* from sys_menu m where m.id in (select menuId from sys_role_menu where roleId = ?1) order by m.id",nativeQuery=true)
	public List<Object[]> findMenuByRoleId(Long roleId);
	
}
	
/**
 * DAO自定义接口
 * @author zj
 */
interface MenuDaoCustom extends BaseDao<Menu> {
	/**
	 * 根据菜单id获取父菜单id
		 * @Title: findParentIdById
	     * @return Long
	     * @author majiancheng       
	     * @date 2015-10-28 上午10:40:40
	 */
	public Long findParentIdById(Long id);
	
	/**
	 * 根据菜单的名称和链接获取菜单集合
		 * @Title: findByNameAndHref
	     * @return List<Menu>
	     * @author majiancheng       
	     * @date 2015-10-28 上午11:50:48
	 */
	public List<Menu> findByNameAndHref(String name, String href);

	public List<Menu> findByUserId(Long userId);

	public List<Menu> findAllList();
}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDaoCustom {
	
	/**
	 * 根据菜单id获取父菜单id
		 * @Title: findParentIdById
	     * @return Long
	     * @author majiancheng       
	     * @date 2015-10-28 上午10:40:40
	 */
	public Long findParentIdById(Long id) {
		Object obj = super.createSqlQuery("select parentId from sys_menu where delFlag=? and id=?", Menu.DEL_FLAG_NORMAL, id).uniqueResult();
		if(obj != null) {
			return ((BigInteger)obj).longValue();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Menu> findByUserId(Long userId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct m from Menu m, Role r, User u ")
		   .append("where m.isShow = 1 and m in elements (r.menuList) and r in elements (u.roleList) ")
		   .append("and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "' ")
		   .append("and r.delFlag='" + Role.DEL_FLAG_NORMAL + "' ")
		   .append("and u.delFlag='" + User.DEL_FLAG_NORMAL + "' ")
		   .append("and u.id=? order by m.sort");
		
		return (List<Menu>)super.createQuery(sql.toString(), userId).list();
	}

	@SuppressWarnings("unchecked")
	public List<Menu> findAllList() {
		return (List<Menu>)super.createQuery("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' order by sort").list();
	}

	
	/**
	 * 根据菜单的名称和链接获取菜单集合
		 * @Title: findByNameAndHref
	     * @return List<Menu>
	     * @author majiancheng       
	     * @date 2015-10-28 上午11:50:48
	 */
	public List<Menu> findByNameAndHref(String name, String href) {
		StringBuffer sql = new StringBuffer();
		sql.append("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' ");
		if(StringUtils.isEmpty(name)) {
			sql.append(" and (name is null or name = '') ");
		} else {
			sql.append(" and name='"+name+"' ");
		}
		
		if(StringUtils.isEmpty(href)) {
			sql.append(" and (href is null or href = '') ");
		} else {
			sql.append(" and href='"+href+"' ");
		}
		
		List<Menu> menus = super.find(sql.toString());
		return menus;
	}
}
