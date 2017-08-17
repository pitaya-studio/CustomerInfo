package com.trekiz.admin.common.persistence;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import com.trekiz.admin.common.utils.AutoColumnToBean;
import com.trekiz.admin.common.utils.Reflections;
import com.trekiz.admin.common.utils.StringUtils;

/**
 * DAO支持类实现
 * @author zj
 * @version 2013-11-19
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T>{

	/**
	 * 获取实体工厂管理对象
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDaoImpl() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 获取 Session
	 */
	public Session getSession(){  
	  return (Session) getEntityManager().getDelegate();
	}

	/**
	 * 强制与数据库同步
	 */
	public void flush(){
		getSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear(){ 
		getSession().clear();
	}
	
	// -------------- QL Query --------------

	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> Page<E> find(Page<E> page, String qlString, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
//	        page.setCount(Long.valueOf(createQuery(countQlString, parameter).uniqueResult().toString()));
	        Query query = createQuery(countQlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createQuery(ql, parameter); 
    	// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.list());
		return page;
    }
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Object... parameter){
		Query query = createQuery(qlString, parameter);
		return query.list();
	}

	/**
	 * QL 更新
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public int update(String qlString, Object... parameter){
		return createQuery(qlString, parameter).executeUpdate();
	}
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter){
		Query query = getSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}
	
	// -------------- SQL Query --------------

    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Object... parameter){
    	return findBySql(page, sqlString, null, parameter);
    }
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
    	    //sqlString = removeGroup(sqlString);
	        String countSqlString = "select count(*) from (" + sqlString+" ) tmp"; 
//	        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createSqlQuery(countSqlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
		// switch to show query sql statement
		if (Boolean.parseBoolean(System.getProperty("showQuerySql"))) {
			System.out.println("=QuerySQL is:="+sql+"=end=");
		}
        SQLQuery query = createSqlQuery(sql, parameter); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        setResultTransformer(query, resultClass);
        page.setList(query.list());
		return page;
    }
	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Object... parameter){
		return findBySql(sqlString, null, parameter);
	}
	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString, Class<?> resultClass, Object... parameter){
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}
	
	/**
	 * SQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateBySql(String sqlString, Object... parameter){
		return createSqlQuery(sqlString, parameter).executeUpdate();
	}
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString, Object... parameter){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}
	
	// -------------- Query Tools --------------

	/**
	 * 设置查询结果类型
	 * @param query
	 * @param resultClass
	 */
	private void setResultTransformer(SQLQuery query, Class<?> resultClass){
		if (resultClass != null){
			if (resultClass == Map.class){
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (resultClass == List.class){
				query.setResultTransformer(Transformers.TO_LIST);
			}else{
				query.addEntity(resultClass);
			}
		}
	}
	
	/**
	 * 设置查询参数
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Object... parameter){
		if (parameter != null){
			for (int i=0; i<parameter.length; i++){
				query.setParameter(i, parameter[i]);
			}
		}
	}
	
    /** 
     * 去除qlString的select子句。 
     * @param hql 
     * @return 
     */  
    protected String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from ");  
        return qlString.substring(beginPos)+"";  
    }  
      
    /** 
     * 去除hql的orderBy子句。 
     * @param hql 
     * @return 
     */  
    protected String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    } 
    
    /**
	 * 分页查询
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page) {
		return find(page, createDetachedCriteria());
	}
	

	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()){
			page.setCount(count(detachedCriteria));
			if (page.getCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (!page.isDisabled()){
	        criteria.setFirstResult(page.getFirstResult());
	        criteria.setMaxResults(page.getMaxResults()); 
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())){
			for (String order : StringUtils.split(page.getOrderBy(), ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					criteria.addOrder(Order.desc(o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						criteria.addOrder(Order.desc(o[0]));
					}else{
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		page.setList(criteria.list());
		return page;
	}

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list(); 
	}
	
	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List<?> orderEntrys = (List<?>)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList<Object>());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}
	
	/**
	 * 根据SQL获取查询结果，默认为数值型，并且该SQL只查询一个字段
	 * @param sql	需要执行的SQL
	 * @return val  返回的值
	 * @author shijun.liu
	 */
	@Override
	public long getCountBySQL(String sql){
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		return Long.valueOf(sqlQuery.uniqueResult().toString());
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}
	
	// -------------- Hibernate search --------------
	
	/**
	 * 获取全文Session
	 */
	public FullTextSession getFullTextSession(){
		return Search.getFullTextSession(getSession());
	}
	
	/**
	 * 建立索引
	 */
	public void createIndex(){
		try {
			getFullTextSession().createIndexer(entityClass).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**add by ruyi.chen
	 * add date 2014-12-16
	 * 
	 * 补充 SQL 分页查询 方法  整体包装查询语句，针对于处理内部含特殊分组，子查询排序等
	 * 使用此方法时注意order by内容放到page中，语句末尾不要加order by内容
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <E> Page<E> findPageBySql(Page<E> page, String sqlString, Class<?> resultClass, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
    	    //sqlString = removeGroup(sqlString);
	        String countSqlString = "select count(*) from( " + sqlString+")count_all";  
//	        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createSqlQuery(countSqlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        SQLQuery query = createSqlQuery(sql, parameter); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        setResultTransformer(query, resultClass);
        page.setList(query.list());
		return page;
    }
    
    
    public void saveObj (T entity){
		Session session = getSession();
		session.save(entity);
		Session session2 = getSession();
		if(session == session2){
			System.out.println("相同");
		}
		session2.flush();
	}
	
	public void updateObj (T entity){
		getSession().update(entity);
		getSession().flush();
	}
	/**
	 * 数据库ID数据类型不统一导致映射类出现Long和Integer俩种数据类型，此处用反射自动处理相应的数据类型
	 */
	@SuppressWarnings("unchecked")
	public T getById(Object value) {
		T t=null;
		Field[] fields = entityClass.getDeclaredFields();
		for(Field field:fields){
			if(field.getName().equals("id")){
				if(field.getType().toString().indexOf("java.lang.Long")>-1){
					t= (T)getSession().get(entityClass, (Long)value);
				}else if(field.getType().toString().indexOf("java.lang.Integer")>-1){
					t= (T)getSession().get(entityClass, (Integer)value);
				}
				break;
			}
		}
		
		return t;
	}
	
	public void batchSave(List<T> entitys) {
		if(CollectionUtils.isNotEmpty(entitys)) {
			for(T entity : entitys) {
				getSession().save(entity);
			}
			getSession().flush();
		}
	}
	
	public void batchUpdate(List<T> entitys) {
		if(CollectionUtils.isNotEmpty(entitys)) {
			for(T entity : entitys) {
				getSession().update(entity);
			}
			getSession().flush();
		}
	}
	
	public void batchDelete(List<T> entitys) {
		if(CollectionUtils.isNotEmpty(entitys)) {
			for(T entity : entitys) {
				getSession().delete(entity);
			}
			getSession().flush();
		}
	}
	
	/**
	 * 根据SQL语句查询出对应的自定义类集合
	*<p>Title: findCustomObjBySql</p>
	* @return List<? extends Serializable> 返回类型
	* @author majiancheng
	* @date 2015-6-12 下午2:14:55
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Serializable> findCustomObjBySql(String sql, Class<? extends Serializable> clazz, Object... parameter) {
		return this.createSqlQuery(sql, parameter).setResultTransformer((new AutoColumnToBean(clazz))).list();
	}
	
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
	 * @time 2016-08-23
	 */
	@SuppressWarnings("unchecked")
	public <E> Page<E> findCustomPageBySql(Page<E> page, String sqlString, Class<? extends Serializable> clazz, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
    	    //sqlString = removeGroup(sqlString);
	        String countSqlString = "select count(*) from( " + sqlString+")count_all";  
//	        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createSqlQuery(countSqlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        SQLQuery query = (SQLQuery) createSqlQuery(sql, parameter);
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        List<? extends Serializable> list= (List<? extends Serializable>) query.setResultTransformer(new AutoColumnToBean(clazz)).list();
        List array=list;
        page.setList(array);
		return page;
    }
}