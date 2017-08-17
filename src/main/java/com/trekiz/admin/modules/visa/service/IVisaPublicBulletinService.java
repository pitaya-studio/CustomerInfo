package com.trekiz.admin.modules.visa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;

@Service
public interface IVisaPublicBulletinService {
	/**
	 * 分页列表查询
	 * @param page
	 * @param visaPublicBulletin
	 * @param visaPublicBulletinName
	 * @param orderBy
	 * @return
	 * @exception:
	 * @author: xinwei.wang
	 * @time:2015-1-4 下午20:28:47
	 */
	Page<VisaPublicBulletin> findVisaPublicBulletinPage(Page<VisaPublicBulletin> page,
			VisaPublicBulletin visaPublicBulletin, String visaPublicBulletinName, String orderBy);
	
	/**
	 * 	公告保存
	 * @param visaPublicBulletin
	 * @return
	 */
	VisaPublicBulletin save(VisaPublicBulletin visaPublicBulletin);

	/**
	 * 公告删除
	 * @param visaPublicBulletin
	 */
	void delVisaPublicBulletin(VisaPublicBulletin visaPublicBulletin);

	/**
	 * 批量删除公告
	 * 创建人：xinwei.wang 
	 * 创建时间：2015-1-4 下午20:28:47
	 * @throws Exception
	 * 
	 */
	void batchDelVisaPublicBulletin(List<Long> ids);

    /**
     * 公告添加
     * @param VisaProducts
     * @param request
     * @param response
     * @param model
     * @param redirectAttributes
     * @return
     */
	String save(VisaPublicBulletin visaPublicBulletin,  HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes);

	/**
	 * 根基公告ID 查找公告
	 * @return
	 */
	VisaPublicBulletin findByVisaPublicBulletinId(Long visaPublicBulletinID);
	
	/**
	 * 获取签证资料的第一条记录
	 * @return
	 */
	VisaPublicBulletin findByVisaPublicBulletinForOne();
	
}
