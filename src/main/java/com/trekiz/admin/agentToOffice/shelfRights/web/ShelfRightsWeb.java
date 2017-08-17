package com.trekiz.admin.agentToOffice.shelfRights.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.PricingStrategy.service.PricingStrategyService;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.agentToOffice.shelfRights.service.ShelfRightsService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.t1.utils.T1Utils;
import com.trekiz.admin.modules.t1.web.T1Controller;

@Controller
@RequestMapping(value = "${adminPath}/shelfRights")
public class ShelfRightsWeb {
	@Autowired
	private ShelfRightsService shelfRightsService;
	@Autowired
	private PricingStrategyService pricingStrategyService;
	@Autowired
	private TouristLineService priceStrategyLineService;
	@Autowired
	private AreaService areaService;
	/**
	 * 上架权限列表
	 * @param office
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequestMapping(value="list")
	public String list(Office office,HttpServletRequest request, HttpServletResponse response, Model model,String type,String big){
		try {
			model.addAttribute("type", type);
			model.addAttribute("big",big);
			model.addAttribute("office", office);
			model.addAttribute("offices",shelfRightsService.getAllOffice());
			model.addAttribute("count", shelfRightsService.getCompanyCount(office));
			office.setDelFlag("0");
			Page<Map<String,Object>> page = shelfRightsService.find(new Page<Map<String,Object>>(request, response), office,type,big);
			model.addAttribute("page", page);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "/agentToOffice/shelfRights/shelfRightsList";
	}
	/**
	 * 修改上架权限状态
	 * @param officeId
	 * @param shelfRightsStatus
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="updateStatus")
	public boolean updatStatus(Long officeId,Integer shelfRightsStatus){
		try {
			shelfRightsService.updatShelRightsStatusByCompanyId(officeId, shelfRightsStatus);
			T1Utils.updateT1HomeCache();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//验证两个集合相等
	 public static <T> boolean equals(Collection<T> a,Collection<T> b){  
		    if(a == null){  
		        return false;  
		    }  
		    if(b == null){  
		        return false;  
		    }  
		    if(a.isEmpty() && b.isEmpty()){  
		        return true;  
		    }  
		    if(a.size() != b.size()){  
		        return false;  
		    }  
		    List<T> alist = new ArrayList<T>(a);  
		    List<T> blist = new ArrayList<T>(b);  
		    Collections.sort(alist,new Comparator<T>() {  
		        public int compare(T o1, T o2) {  
		            return o1.hashCode() - o2.hashCode();  
		        }  
		          
		    });  
		      
		    Collections.sort(blist,new Comparator<T>() {  
		        public int compare(T o1, T o2) {  
		            return o1.hashCode() - o2.hashCode();  
		        }  
		          
		    });  
		      
		    return alist.equals(blist);  
		      
		}  
}

