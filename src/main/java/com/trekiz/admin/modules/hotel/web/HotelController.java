/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelContact;
import com.trekiz.admin.modules.hotel.entity.HotelFeature;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerTypeRelation;
import com.trekiz.admin.modules.hotel.query.HotelTravelerTypeRelationQuery;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelContactService;
import com.trekiz.admin.modules.hotel.service.HotelFeatureService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotel")
public class HotelController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotel/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotel/list";
	protected static final String FORM_PAGE = "modules/hotel/hotel/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotel/show";
	protected static final String FILE_PAGE = "modules/hotel/hotel/mulUploadFile";
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelContactService hotelContactService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private HotelFeatureService hotelFeatureService;
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private SysGeographyService sysGeographyService;
	
	private List<HotelContact> hcList;
	
	private List<HotelAnnex> annexList;
	
	public void setHcList(List<HotelContact> hcList) {
		this.hcList = hcList;
	}

	public void setAnnexList(List<HotelAnnex> annexList) {
		this.annexList = annexList;
	}

	@ModelAttribute  
    public void populateModel( HttpServletRequest request, Model model) { 
		/**表单提交后的联系人处理*/
		if(ArrayUtils.isNotEmpty(request.getParameterValues("names"))){
			String[] names = request.getParameterValues("names");
			String[] mobiles = request.getParameterValues("mobiles");
			String[] telephones = request.getParameterValues("telephones");
			String[] faxs = request.getParameterValues("faxs");
			String[] emails = request.getParameterValues("emails");
			
			List<HotelContact> list = new ArrayList<HotelContact>();
			for(int i=0;i<names.length;i++){
				if(StringUtils.isNotBlank(names[i])){
					HotelContact hc = new HotelContact();
					
					hc.setName(names[i]);
					hc.setMobile(mobiles[i]);
					hc.setTelephone(telephones[i]);
					hc.setFax(faxs[i]);
					hc.setEmail(emails[i]);
					list.add(hc);
				}
			}
			
			this.setHcList(list);
		}
		if(ArrayUtils.isNotEmpty(request.getParameterValues("hotelAnnexDocId"))){
			String[] hotelAnnexDocId = request.getParameterValues("hotelAnnexDocId");
			String[] docOriName = request.getParameterValues("docOriName");
			String[] docPath = request.getParameterValues("docPath");
			
			List<HotelAnnex> list = new ArrayList<HotelAnnex>();
			for(int i=0;i<hotelAnnexDocId.length;i++){
				if(StringUtils.isNotBlank(hotelAnnexDocId[i])){
					HotelAnnex ha = new HotelAnnex();
					ha.setDocId(Integer.parseInt(hotelAnnexDocId[i]));
					ha.setDocPath(docPath[i]);
					ha.setDocName(docOriName[i]);
					list.add(ha);
				}
			}
			this.setAnnexList(list);
		}
		
		model.addAttribute("type", Context.BaseInfo.HOTEL_INFO);  
	}

	@RequestMapping(value = "list")
	public String list(Hotel hotel, HttpServletRequest request, HttpServletResponse response, Model model) {
    	if(UserUtils.getCompanyIdForData() != null) {
    		hotel.setDelFlag("0");
    		hotel.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData()+""));
            Page<Hotel> page = hotelService.find(new Page<Hotel>(request, response), hotel); 
            model.addAttribute("page", page);
    	}
    	hotelService.getByUuid("c9d1c71dfa8344a285f1766dccf95dfe");
    	model.addAttribute("whoStarList", buildWhoStarList());
    	
    	//位置（1：境内，2：境外）
    	Integer position = hotel.getPosition();
		List<SysGeography> list = null;
    	if(position != null) {
    		SysGeography sysGeography = new SysGeography();
    		if(position == 1) {
    			sysGeography.setDelFlag("0");
    			sysGeography.setNameCn("中国");
    			sysGeography.setLevel(1);
    			list = sysGeographyService.find(sysGeography);
    		} else if(position == 2) {
    			sysGeography.setDelFlag("0");
    			sysGeography.setLevel(1);
    			list = removeChinaList(sysGeographyService.find(sysGeography));
    		}
    	}
		model.addAttribute("countrys", list);
    	
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(Hotel hotel, Model model) {
		
    	if(UserUtils.getCompanyIdForData() != null) {
    		
    		model.addAttribute("whoStarList", buildWhoStarList());
    		
    		HotelFeature hf = new HotelFeature();
    		hf.setDelFlag("0");
    		hf.setWholesalerId(UserUtils.getCompanyIdForData());
    		List<HotelFeature> hfList = hotelFeatureService.find(hf);
    		model.addAttribute("hfList", hfList);
    		
    		Island island = new Island();
    		island.setDelFlag("0");
    		island.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData()+""));
    		List<Island> islandList = islandService.find(island);
    		//add by WangXK 20150825
    		model.addAttribute("travelerTypes", getTravelTypes());
    		model.addAttribute("islandList", islandList);
    	}
		
		model.addAttribute("hotel", hotel);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(Hotel hotel, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		
    	if(UserUtils.getCompanyIdForData() != null) {
    		String position= request.getParameter("position");
    		String[] shortAddressArr = request.getParameterValues("shortAddress");
			if("2".equals(position)){
    			hotel.setCountry(request.getParameter("overseas_state"));
    			hotel.setProvince(request.getParameter("overseas_province"));
    			hotel.setCity(request.getParameter("overseas_city"));
    			hotel.setShortAddress(shortAddressArr[1]);
    		}else if("1".equals(position)){
    			hotel.setShortAddress(shortAddressArr[0]);
    		}
    		String[] travelerTypeUuids = request.getParameter("travelerTypeUuids").trim().split(",");
    		String[] travelerTypeNames = request.getParameter("travelerTypeNames").trim().split(",");
    		hotel.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData()+""));
    		hotelService.save(hotel, this.hcList,this.annexList);
    		//保存 travelerType表的UUid 保存到hotelTravelerTypeRelationService
			for(int i=0;i<travelerTypeUuids.length;i++){
				String s = travelerTypeUuids[i];
				if(s!=null&&!"".equals(s)){
					HotelTravelerTypeRelation typeRelation = new HotelTravelerTypeRelation();
					typeRelation.setTravelerTypeUuid(travelerTypeUuids[i]);
					typeRelation.setTravelerTypeName(travelerTypeNames[i]);
					typeRelation.setHotelUuid(hotel.getUuid());
					hotelTravelerTypeRelationService.save(typeRelation);
				}
			}
			
    		model.addAttribute("message","1");
    	}else{
    		//add by WangXK 20150825
    		model.addAttribute("travelerTypes", getTravelTypes());
    		return "3";
    	}
		
 		return "1";
	}
	
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		if(StringUtils.isNotBlank(uuid)){
			Hotel hotel = hotelService.getByUuid(uuid);
			//update by WangXK 20151020 添加空指针的判断
			if(hotel!=null && hotel.getWholesalerId()!=null && (hotel.getWholesalerId().intValue() != UserUtils.getCompanyIdForData())){
				return SHOW_PAGE;
			}
			//update by WangXK 20151020 添加空指针的判断
	    	if(UserUtils.getCompanyIdForData() != null && hotel.getWholesalerId()!=null &&hotel.getWholesalerId()==Integer.parseInt(""+UserUtils.getCompanyIdForData())) {
	    		if(hotel!=null){
					HotelContact hc = new HotelContact();
					hc.setDelFlag("0");
					hc.setHotelUuid(hotel.getUuid());
					List<HotelContact> list = hotelContactService.find(hc);
					
					model.addAttribute("hcList", list);
				}
				model.addAttribute("hotel", hotel);
	    	}
	    	//add by WangXK 20150825
	    	HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery = new HotelTravelerTypeRelationQuery();
	    	if(hotel!=null){
	    		hotelTravelerTypeRelationQuery.setHotelUuid(hotel.getUuid());
	    	}
    		hotelTravelerTypeRelationQuery.setDelFlag(Context.DEL_FLAG_NORMAL);
	    	List<HotelTravelerTypeRelation> httrList = hotelTravelerTypeRelationService.find(hotelTravelerTypeRelationQuery);
	    	model.addAttribute("httrList", httrList);
		}
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		Map<String,String> positionMap = new LinkedHashMap<String,String>();
		positionMap.put("1", "境内岛屿");
		positionMap.put("2", "境外岛屿");
		model.addAttribute("positionMap", positionMap);
		
		if(StringUtils.isNotBlank(uuid)){
			Hotel hotel = hotelService.getByUuid(uuid);
			//update by WangXK 20151020 添加空指针的判断
			if(hotel!=null && hotel.getWholesalerId()!=null && (hotel.getWholesalerId().intValue() != UserUtils.getCompanyIdForData())){
				return FORM_PAGE;
			}
	    	if(UserUtils.getCompanyIdForData() != null && hotel.getWholesalerId()!=null && hotel.getWholesalerId()==Integer.parseInt(""+UserUtils.getCompanyIdForData())) {
	    		if(hotel!=null){
					HotelContact hc = new HotelContact();
					hc.setDelFlag("0");
					hc.setHotelUuid(hotel.getUuid());
					List<HotelContact> list = hotelContactService.find(hc);
					
					model.addAttribute("hcList", list);
					
					HotelAnnex ha = new HotelAnnex();
					ha.setDelFlag("0");
					ha.setMainUuid(hotel.getUuid());
					ha.setType(1);
					List<HotelAnnex> haList = hotelAnnexService.find(ha);
					model.addAttribute("haList", haList);
					
				}
	    		
	    		model.addAttribute("whoStarList", buildWhoStarList());
	    		
	    		HotelFeature hf = new HotelFeature();
	    		hf.setDelFlag("0");
	    		hf.setWholesalerId(UserUtils.getCompanyIdForData());
	    		List<HotelFeature> hfList = hotelFeatureService.find(hf);
	    		model.addAttribute("hfList", hfList);
	    		
	    		Island island = new Island();
	    		island.setDelFlag("0");
	    		island.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData()+""));
	    		List<Island> islandList = islandService.find(island);
	    		if(hotel.getPosition()==1){
					model.addAttribute("countrys",getGeoListFromPosition(hotel.getPosition().toString(),""));
					model.addAttribute("provinces", getGeoListFromPosition("",hotel.getCountry()));
					model.addAttribute("citys", getGeoListFromPosition("",hotel.getProvince()));
					model.addAttribute("districts", getGeoListFromPosition("",hotel.getCity()));
				}else if(hotel.getPosition()==2){
					model.addAttribute("countrys",getGeoListFromPosition(hotel.getPosition().toString(),""));
					model.addAttribute("provinces", getGeoListFromPosition("",hotel.getCountry()));
					model.addAttribute("citys", getGeoListFromPosition("",hotel.getProvince()));
				}
	    		
	    		model.addAttribute("islandList", islandList);
	    		HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery = new HotelTravelerTypeRelationQuery();
	    		hotelTravelerTypeRelationQuery.setHotelUuid(hotel.getUuid());
	    		hotelTravelerTypeRelationQuery.setDelFlag(Context.DEL_FLAG_NORMAL);
	    		List<HotelTravelerTypeRelation> list = hotelTravelerTypeRelationService.find(hotelTravelerTypeRelationQuery);
	    		String travelerTypeString = "";
	    		if(list != null && list.size()>0){
	    			for(HotelTravelerTypeRelation hotelTravelerTypeRelation: list){
	    				travelerTypeString = travelerTypeString + hotelTravelerTypeRelation.getTravelerTypeUuid() + ",";
	    			}
	    		}
	    		
	    		//add by WangXK 20150825
	    		model.addAttribute("travelerTypeString", travelerTypeString);
	    		model.addAttribute("travelerTypes", getTravelTypes());
				model.addAttribute("hotel", hotel);
	    	}
			
		}
		
		return "modules/hotel/hotel/edit";
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(Hotel hotel, Model model, HttpServletRequest request) {
		String position = request.getParameter("position");
		String[] travelerTypeUuids = request.getParameter("travelerTypeUuids").trim().split(",");
		String[] travelerTypeNames = request.getParameter("travelerTypeNames").trim().split(",");
		//update by WangXK 20151020 添加空指针的判断
		if(hotel!=null && hotel.getWholesalerId()!=null && (hotel.getWholesalerId().intValue() != UserUtils.getCompanyIdForData())){
			return "3";
		}
		String[] shortAddress = request.getParameterValues("shortAddress");
		if("1".equals(position)){
			hotel.setShortAddress(shortAddress[0]);
		}else if("2".equals(position)){
			hotel.setCountry(request.getParameter("overseas_state"));
			hotel.setProvince(request.getParameter("overseas_province"));
			hotel.setCity(request.getParameter("overseas_city"));
			hotel.setShortAddress(shortAddress[1]); 
		}
		// add by WangXK 20150825 先删除后添加
		//update by WangXK 20151020 添加空指针的判断
		if(hotel!=null){
			String uuid = hotel.getUuid();
			if(uuid!=null && !"".equals(uuid.trim())){
				hotelTravelerTypeRelationService.removeByHotelUuid(uuid);
			}
		}
		for(int i=0;i<travelerTypeUuids.length;i++){
			String s = travelerTypeUuids[i];
			if(s!=null&&!"".equals(s)){
				HotelTravelerTypeRelation typeRelation = new HotelTravelerTypeRelation();
				typeRelation.setTravelerTypeUuid(travelerTypeUuids[i]);
				typeRelation.setTravelerTypeName(travelerTypeNames[i]);
				typeRelation.setHotelUuid(hotel.getUuid());
				hotelTravelerTypeRelationService.save(typeRelation);
			}
		}
		hotelService.update(hotel, this.hcList,this.annexList);
		
 		return "2";

	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String ids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(ids)){
				String[] idsArray = ids.split(",");
				if(idsArray!=null && idsArray.length>0){
					for(String uuid : idsArray){
						if(StringUtils.isNotBlank(uuid)){
							hotelService.removeByUuid(uuid);
						}
					}
				}
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "error");
		}
		if(b){
			datas.put("result", "1");
			datas.put("message", "success");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	/**
	 * 多文件上传
	 */
	@RequestMapping("uploadFilesPage")
	public String getUploadFilesPage() {
		return FILE_PAGE;
	}
	
	private List<HotelStar> buildWhoStarList() {
		HotelStar hotelStar = new HotelStar();
		hotelStar.setDelFlag("0");
		hotelStar.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData()+""));
		List<HotelStar> whoStarList = hotelStarService.find(hotelStar);
		return whoStarList;
	}
	private List<SysGeography> getGeoListFromPosition(String type,String parentId) {
		List<SysGeography> allList = new ArrayList<SysGeography>();
		SysGeography sysGeography = new SysGeography();
		sysGeography.setUuid("");
        sysGeography.setNameCn("请选择");
        allList.add(sysGeography);
        
		sysGeography = new SysGeography();
        List<SysGeography> list =new ArrayList<SysGeography>();
        if(StringUtils.isNotBlank(type)&&type.equals("1")){//境内
			sysGeography.setDelFlag("0");
			sysGeography.setNameCn("中国");
			sysGeography.setLevel(1);
			list = sysGeographyService.find(sysGeography);
		}else if(StringUtils.isNotBlank(type)&&type.equals("2")){//境外
			sysGeography.setDelFlag("0");
			sysGeography.setLevel(1);
			list = removeChinaList(sysGeographyService.find(sysGeography));
		}else if(StringUtils.isNotBlank(parentId)){//查询子节点集合
			sysGeography.setDelFlag("0");
			sysGeography.setParentUuid(parentId);
			list = sysGeographyService.find(sysGeography);
		}
        allList.addAll(list);
		return allList;
	}
	/**
     * 境外国家剔除 中国
     * @param list
     * @return
     */
    private List<SysGeography> removeChinaList(List<SysGeography> list){
    	List<SysGeography> nlist = new ArrayList<SysGeography>();
    	for(SysGeography sg:list){
    		if(StringUtils.isNotBlank(sg.getNameCn())&&sg.getNameCn().equals("中国")){
    			
    		}else{
    			nlist.add(sg);
    		}
    	}
    	return nlist;
    }
	/**
	 * 获取酒店关联游客类型的信息
	 * @return
	 */
	private List<TravelerType> getTravelTypes(){
		TravelerType travelerType = new TravelerType();
		travelerType.setStatus("1");//启用
		travelerType.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		travelerType.setDelFlag(Context.DEL_FLAG_NORMAL);
		
		List<TravelerType> list = travelerTypeService.find(travelerType);
		return list;
	}
	
}
