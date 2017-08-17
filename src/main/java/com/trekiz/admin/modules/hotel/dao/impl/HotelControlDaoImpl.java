/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelControlDao;
import com.trekiz.admin.modules.hotel.entity.HotelControl;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Repository
public class HotelControlDaoImpl extends BaseDaoImpl<HotelControl>  implements HotelControlDao{
	@Override
	public HotelControl getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelControl hotelControl where hotelControl.uuid=? and hotelControl.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelControl)entity;
		}
		return null;
	}
		
	@SuppressWarnings("unchecked")
	public <E> Page<E> findBySql(Page<E> page, String sqlString,
			String oldsqlString, Class<?> resultClass, Object... parameter) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			// sqlString = removeGroup(sqlString);
			String countSqlString = "select count(*) "
					+ removeSelect(removeOrders(sqlString));
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			org.hibernate.Query query = super.createSqlQuery(countSqlString,
					parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
//		String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())) {
			oldsqlString += " order by " + page.getOrderBy();
		}
		SQLQuery query = createSqlQuery(oldsqlString, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		setResultTransformer(query, resultClass);
		page.setList(query.list());
		return page;
	}

	/**
	 * 设置查询结果类型
	 * 
	 * @param query
	 * @param resultClass
	 */
	private void setResultTransformer(SQLQuery query, Class<?> resultClass) {
		if (resultClass != null) {
			if (resultClass == Map.class) {
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			} else if (resultClass == List.class) {
				query.setResultTransformer(Transformers.TO_LIST);
			} else {
				query.addEntity(resultClass);
			}
		}
	}

	/**
	 * 去除qlString的select子句。
	 * 
	 * @param hql
	 * @return
	 */
	protected String removeSelect(String qlString) {
		int beginPos = qlString.toLowerCase().indexOf("from ");
		return qlString.substring(beginPos) + "";
	}

	/**
	 * 去除hql的orderBy子句。
	 * 
	 * @param hql
	 * @return
	 */
	protected String removeOrders(String qlString) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(qlString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
