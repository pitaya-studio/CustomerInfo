package com.trekiz.admin.modules.visa.web;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;
import com.trekiz.admin.modules.visa.service.IVisaPublicBulletinService;

@Controller
@RequestMapping(value = "${adminPath}/visa/visaPublicBulletin")
public class VisaPublicBulletinController extends BaseController {


	@Autowired
	private IVisaPublicBulletinService visaPublicBulletinService;
	
    /**
     * 创建签证公告：如果有公告记录则读取，如没有则创建一条公告
     * @param visaPublicBulletin
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "visaPublicBulletinCreate")
	public String visaPublicBulletinCreate(@ModelAttribute VisaPublicBulletin  visaPublicBulletin,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
				if (null==visaPublicBulletin.getId()) {
					Page<VisaPublicBulletin> page =visaPublicBulletinService.findVisaPublicBulletinPage(new Page<VisaPublicBulletin>(request, response), visaPublicBulletin, null, null);
					if (null!=page.getList()&&page.getList().size()>0) {
						VisaPublicBulletin  visaBulletinTemp= page.getList().get(0);
						model.addAttribute("visaPublicBulletin", visaBulletinTemp);
					}else {
						saveVisaPublicBulletin(visaPublicBulletin,model);
					}
						return "modules/visa/visaPublicBulletinCreate";
				}else {
					saveVisaPublicBulletin(visaPublicBulletin,model);
				}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("visaPublicSaveSuccess", "2");
		}
		return "modules/visa/visaPublicBulletinCreate";
	}
	
	private void saveVisaPublicBulletin(VisaPublicBulletin  visaPublicBulletin,Model model){
		if (null!=visaPublicBulletin.getTitle()&&null!=visaPublicBulletin.getContent()) {
			User user = UserUtils.getUser();
			visaPublicBulletin.setCreateBy(user);
			visaPublicBulletin.setCreateDate(new Date());
			visaPublicBulletin.setCompanyId(user.getCompany().getId());
			visaPublicBulletin = visaPublicBulletinService.save(visaPublicBulletin);
			model.addAttribute("visaPublicSaveSuccess", "1");
			model.addAttribute("visaPublicBulletin", visaPublicBulletin);
		}
	}
	
	
	
	

}
