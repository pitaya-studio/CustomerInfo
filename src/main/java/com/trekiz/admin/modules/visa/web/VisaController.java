package com.trekiz.admin.modules.visa.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.visa.entity.Activityvisafile;
import com.trekiz.admin.modules.visa.entity.Visabasics;
import com.trekiz.admin.modules.visa.entity.Visapersonneltype;
import com.trekiz.admin.modules.visa.service.VisaService;


 /**
 *  文件名: VisaController.java
 *  功能:
 *      签证控制器
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-13 下午2:36:17
 *  @version 1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/visa/instruction")
public class VisaController extends BaseController {

	@Autowired
	private VisaService visaService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 70;
	}
	
	@RequestMapping(value = {"show",""})
	public String showInstruction(Model model){
	    model.addAttribute("validCountry", visaService.getAllCountry());
//	    model.addAttribute("allCountry", CountryUtils.getCountryList().values());
	    model.addAttribute("visaType", DictUtils.getDictList(Context.DICT_TYPE_VISATYPE));
        return "modules/visa/visaInstructions";
	}
	
	@RequestMapping(value = "visaInstructions/{basicsCountryId}")
	public String getVisaInstruction(Model model,@PathVariable("basicsCountryId")Integer basicsCountryId){
	    Visabasics visaBasics = visaService.getVisaBasicsByCountryId(basicsCountryId);
	    if(visaBasics!=null){
	        List<Visapersonneltype> list = visaService.getVisaPersonelByBasicsId(visaBasics.getId());
	        model.addAttribute("personalList", list);
	    }
	    model.addAttribute("visaBasics", visaBasics);
	    model.addAttribute("validCountry", visaService.getAllCountry());
	    return "modules/visa/visaInstructions";
	}
	
	
    @RequestMapping(value = "form")
    public String form(Visabasics visabasics, Model model) {
        model.addAttribute("yesorno",DictUtils.getDictList(Context.DICT_TYPE_YESNO));
        return "modules/visa/visaBasicFrom";
    }
    
    @RequestMapping(value = "formPersonal/{basicid}")
    public String formPersonal(@PathVariable("basicid") Long basicid,Visapersonneltype visapersonnelType , Model model) {
        model.addAttribute("visa_personnaltype",DictUtils.getDictList(Context.DICT_TYPE_VISAPERSONALTYPE));
        visapersonnelType.setVisabasicsId(basicid);
        return "modules/visa/formPersonal";
    }
    
    @RequestMapping(value = "save")
    public String save(Visabasics visabasics, Model model) {
        if (!beanValidator(model, visabasics)){
            return form(visabasics, model);
        }
        visaService.saveVisaBasic(visabasics);
//        addMessage(redirectAttributes, "保存区域'" + visabasics.getName() + "'成功");
        return "redirect:"+Global.getAdminPath()+"/visa/instruction/visaInstructions/"+visabasics.getVisaCountry();
        //return "modules/visa/visaBasicFrom";
    }
    
    @RequestMapping(value = "saveVisaPersonnal")
    public String saveVisaPersonnal(Visapersonneltype Visapersonneltype, Model model) {
        if (!beanValidator(model, Visapersonneltype)){
            return formPersonal(Visapersonneltype.getVisabasicsId(),Visapersonneltype, model);
        }
        visaService.saveVisapersonneltype(Visapersonneltype);
        return "redirect:"+Global.getAdminPath()+"/visa/instruction/formPersonal/"+Visapersonneltype.getVisabasicsId();
    }
	
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		visaInfoService.delete(id);
		addMessage(redirectAttributes, "删除签证信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/visa/visaInfo/?repage";
	}
	
	@ResponseBody
	@RequestMapping(value = "showFileByCountrys")
    public Object showFilesByCountrys(@RequestParam("countrys") String countrys) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    List<Activityvisafile> list =  new ArrayList<Activityvisafile>();
	    list = visaService.findByVisaFileByCountry(countrys);
	    map.put("data", list);
	    map.put("visaType", DictUtils.getDictList(Context.DICT_TYPE_VISATYPE));
        return map;
    }
	
	
	@RequestMapping(value = "upload")
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {  
        String selectcountry = request.getParameter("selectcountry");
        String radiovisaType = request.getParameter("radiovisaType");
        String fileName = file.getOriginalFilename();  
        //保存  
        try {
            String path = FileUtils.uploadFile(file.getInputStream(), fileName);
            visaService.saveUploadData(selectcountry,radiovisaType,fileName,path);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return "redirect:"+Global.getAdminPath()+"/visa/instruction/";
    } 
	
	/**
	 * 
	* @Title: saveAsAcount 
	* @Description: TODO(财务确认押金收款) 
	* @param @param request
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws
	 */
	@RequestMapping(value ="saveAsAcount")
	@ResponseBody
	public Object saveAsAcount(HttpServletRequest request){
	    String visaId = request.getParameter("id");
	    String serialNum = request.getParameter("serialNum");
	    moneyAmountService.updateAccountedDeposit(Long.parseLong(visaId),serialNum);
	    return null;
	}
	
}
