package com.trekiz.admin.modules.sys.utils;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;
import com.trekiz.admin.modules.visa.repository.VisaPublicBulletinDao;
/**
 * 签证公告相关UTILS
 * @author xinwei.wang
 */
public class VisaPublicBulletinUtil {
	 private static VisaPublicBulletinDao visaPublicBulletinDao = SpringContextHolder.getBean(VisaPublicBulletinDao.class);
	 
	    /**
		 * 获取签证公告列表
		 * @return
		 */
	 public static List<VisaPublicBulletin> findVisaPublicBulletins(){
			DetachedCriteria dc = visaPublicBulletinDao.createDetachedCriteria();
			dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
			dc.add(Restrictions.eq("companyId", UserUtils.getUser().getCompany().getId()));
			dc.addOrder(Order.asc("id"));
			List<VisaPublicBulletin> visaPublicBulletins = visaPublicBulletinDao.find(dc);
			return  visaPublicBulletins;
		}

}
