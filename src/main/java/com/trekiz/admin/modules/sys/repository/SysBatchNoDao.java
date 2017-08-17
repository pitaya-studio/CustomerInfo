/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysBatchNo;

/**
 * 系统批次号DAO接口
 * @author yue.wang
 * @version 2015-03-03
 */
@Component("sysBatchNoDao")
public class SysBatchNoDao extends   BaseDaoImpl<SysBatchNo> {
	private static final Logger log = LoggerFactory.getLogger(SysBatchNoDao.class);
	
	
	/*
	 * 获取sysBatchNo对象
	 * @param curDate 日期
	 * @param code 类型
	 */
	@SuppressWarnings({ "unchecked"})
	public SysBatchNo findByKeyAndDate(String curDate, String code){
		log.debug("getting SysBatchNo instance with curDate: " + curDate+"  code: "+code);
		try {
			
			String queryString = "from SysBatchNo as model where model.curDate='"+curDate+"'" +
					" and model.code='"+code+"'";
			List<SysBatchNo> list = this.getSession().createQuery(queryString).list();
			if(list==null||list.size()==0){
				return null;
			}
			return list.get(0);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

