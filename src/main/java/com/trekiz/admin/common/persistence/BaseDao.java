/**

 *

 */
package com.trekiz.admin.common.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.search.FullTextSession;
import org.hibernate.transform.ResultTransformer;

/**
 * DAO支持接口
 * @author zj
 * @version 2013-11-19
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager();
	
	/**
	 * 获取 Session
	 */
	public Session getSession();
	
	/**
	 * 强制与数据库同步
	 */
	public void flush();

	/**
	 * 清除缓存数据
	 */
	public void clear();
	
	// -------------- QL Query --------------

	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    public <E> Page<E> find(Page<E> page, String qlString, Object... parameter);
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> find(String qlString, Object... parameter);
    
	/**
	 * QL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int update(String qlString, Object... parameter);
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter);
	
	// -------------- SQL Query --------------

    /**
	 * SQL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Object... parameter);
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param qlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass, Object... parameter);

	/**
	 * SQL 查询
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Object... parameter);
	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Class<?> resultClass, Object... parameter);
	
	/**
	 * SQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateBySql(String sqlString, Object... parameter);
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public Query createSqlQuery(String sqlString, Object... parameter);
	
	// -------------- Criteria --------------
	
	/**
	 * 分页查询
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page);
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria);
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer);

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria);
	
	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer);
	
	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	public long count(DetachedCriteria detachedCriteria);
	
	/**
	 * 根据SQL获取查询结果，默认为数值型，并且该SQL只查询一个字段
	 * @param sql	需要执行的SQL
	 * @return val  返回的值
	 * @author shijun.liu
	 */
	public long getCountBySQL(String sql);

	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions);
	
	// -------------- Hibernate search --------------
	
	/**
	 * 获取全文Session
	 */
	public FullTextSession getFullTextSession();
	
	/**
	 * 建立索引
	 */
	public void createIndex();
	
	/**add by ruyi.chen
	 * add date 2014-12-16
	 * 补充 SQL 分页查询 方法  整体包装查询语句，针对于处理内部含特殊分组，子查询排序等
	 * 使用此方法时注意order by内容放到page中，语句末尾不要加order by内容
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> findPageBySql(Page<E> page, String sqlString, Class<?> resultClass, Object... parameter);
	
	
	public void saveObj (T entity);
	
	public void updateObj (T entity);
	
	public T getById(Object value);
	
	/**
	 * 批量保存操作
	 * @param entitys
	 */
	public void batchSave(List<T> entitys);
	
	/**
	 * 批量更新操作
	 * @param entitys
	 */
	public void batchUpdate(List<T> entitys);
	
	/**
	 * 批量删除操作
	 * @param entitys
	 */
	public void batchDelete(List<T> entitys);
	
	/**
	 * 根据SQL语句查询出对应的自定义类集合
	*<p>Title: findCustomObjBySql</p>
	* @return List<? extends Serializable> 返回类型
	* @author majiancheng
	* @date 2015-6-12 下午2:14:55
	* @throws
	 */
	public List<? extends Serializable> findCustomObjBySql(String sql, Class<? extends Serializable> clazz, Object... parameter);
	
	/**
	 * 封装成任意对象
	 * 补充 SQL 分页查询 方法  整体包装查询语句，针对于处理内部含特殊分组，子查询排序等
	 * 使用此方法时注意order by内容放到page中，语句末尾不要加order by内容
	 * @param page
	 * @param sqlString
	 * @param clazz
	 * @param parameter
	 * @return
	 * @author chao.zhang@quauq.com
	 * add time 2016-08-23
	 */
	public <E> Page<E> findCustomPageBySql(Page<E> page, String sqlString, Class<? extends Serializable> clazz, Object... parameter);
	
	
}