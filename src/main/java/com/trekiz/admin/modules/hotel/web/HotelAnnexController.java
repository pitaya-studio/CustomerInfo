/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelAnnex")
public class HotelAnnexController extends BaseController {
	
	//forward paths
	
	@Autowired
	private HotelAnnexService hotelAnnexService;
	
	
	@RequestMapping(value = "list")
	public String list(HotelAnnex hotelAnnex, HttpServletRequest request, HttpServletResponse response, Model model) {
		
        return "";
	}

	@RequestMapping(value = "delete")
	public void delete(String uuid,HttpServletRequest request, HttpServletResponse response) {
		hotelAnnexService.removeByUuid(uuid);
	}
	
}
