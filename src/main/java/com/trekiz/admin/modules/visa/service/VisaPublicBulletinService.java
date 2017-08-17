package com.trekiz.admin.modules.visa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;
import com.trekiz.admin.modules.visa.repository.VisaPublicBulletinDao;

@Service
@Transactional(readOnly = true)
public class VisaPublicBulletinService extends BaseService implements IVisaPublicBulletinService{
	
	@Autowired
	private VisaPublicBulletinDao visaPublicBulletinDao;

	@Override
	public Page<VisaPublicBulletin> findVisaPublicBulletinPage(
			Page<VisaPublicBulletin> page,
			VisaPublicBulletin visaPublicBulletin,
			String visaPublicBulletinName, String orderBy) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = visaPublicBulletinDao.createDetachedCriteria();

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("companyId", UserUtils.getUser().getCompany().getId()));


/*		if (StringUtils.isNotBlank(visaPublicBulletinName)) {
			dc.add(Restrictions.like("title", "%" + visaPublicBulletinName + "%"));
		}*/

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.asc("id"));
		}
		return visaPublicBulletinDao.find(page, dc);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public VisaPublicBulletin save(VisaPublicBulletin visaPublicBulletin) {
		// TODO Auto-generated method stub
		return this.visaPublicBulletinDao.save(visaPublicBulletin);
	}

	@Override
	public void delVisaPublicBulletin(VisaPublicBulletin visaPublicBulletin) {
		this.visaPublicBulletinDao.delete(visaPublicBulletin);
	}

	@Override
	public void batchDelVisaPublicBulletin(List<Long> ids) {
		this.visaPublicBulletinDao.batchDelVisaProducts(ids);
		
	}

	@Override
	public String save(VisaPublicBulletin visaPublicBulletin,
			HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisaPublicBulletin findByVisaPublicBulletinId(
			Long visaPublicBulletinID) {
		return visaPublicBulletinDao.findOne(visaPublicBulletinID);
	}

	
	/**
	 * 获取签证资料的第一条记录
	 * @return
	 */
	public VisaPublicBulletin findByVisaPublicBulletinForOne(){
		DetachedCriteria dc = visaPublicBulletinDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("id"));
		List<VisaPublicBulletin> visaPublicBulletins = 	 visaPublicBulletinDao.find(dc);
	    if (null!=visaPublicBulletins&&visaPublicBulletins.size()>0) {
			return  visaPublicBulletins.get(0);
		}
		return  null;
	}
	

}
