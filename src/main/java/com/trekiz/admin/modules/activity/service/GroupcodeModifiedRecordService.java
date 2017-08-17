package com.trekiz.admin.modules.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.pojo.GroupCodeLifeBycle;
import com.trekiz.admin.modules.activity.repository.GroupcodeModifiedRecordDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**  
 * @Title: GroupcodeModifiedRecordService.java
 * @Package com.trekiz.admin.modules.activity.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xinwei.wang   
 * @date 2015-2015年11月25日 下午6:00:33
 * @version V1.0  
 */
@Service
public class GroupcodeModifiedRecordService  extends BaseService implements IGroupcodeModifiedRecordService{
	
	@Autowired
	private GroupcodeModifiedRecordDao groupcodeModifiedRecordDao;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private  ActivityGroupService activityGroupService;
	@Autowired
	private TravelActivityService  travelActivityService;
	@Autowired
	private VisaProductsService  visaProductsService;

    
	/**
	 * 机票团期库列表
	 */
	@Override
	public Page<Map<String, Object>> queryGroupCodeLibrary4Airticket(HttpServletRequest request, HttpServletResponse response) {
		
		Long office_id = UserUtils.getUser().getCompany().getId();
		Long parentDeptId= 0L;
		//String office_uuid = UserUtils.getUser().getCompany().getUuid();
		List<Department> departments = departmentService.findByOfficeId(office_id);
		for (Department department : departments) {
			if (0==department.getParentId()) {
				parentDeptId = department.getId();
				break;
			}
		}
	    
		
		Page<Map<String, Object>> reviewPage = groupcodeModifiedRecordDao.queryGroupCodeLibrary4Airticket(request, response,office_id,parentDeptId);
		
		//团号生命周期处理
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){

			String productId  = tMap.get("productId").toString();
			List<GroupcodeModifiedRecord> groupcodeModifiedRecords = null;
			ActivityAirTicket activityAirTicketold = activityAirTicketService.findById(Long.parseLong(productId));
			groupcodeModifiedRecords  =  groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByProductIdAndType(activityAirTicketold.getId().intValue(), new Integer(7));
			GroupCodeLifeBycle groupCodeLifeBycle = new GroupCodeLifeBycle();
			if (null!=groupcodeModifiedRecords&&groupcodeModifiedRecords.size()>0) {
				
				//处理原始团号记录
				groupCodeLifeBycle.setOldGroupCodeCreateDate(activityAirTicketold.getCreateDate());
				groupCodeLifeBycle.setOldGroupCodeCreatebyName(activityAirTicketold.getCreateBy().getName());
				groupCodeLifeBycle.setOldGroupCode(groupcodeModifiedRecords.get(0).getGroupcodeOld());
				
				//处理修改的团号记录
				groupCodeLifeBycle.setGroupcodeModifiedRecords(groupcodeModifiedRecords);
			}
			tMap.put("groupCodeLifeBycle", groupCodeLifeBycle);
		}
		
		reviewPage.setList(reviewList);
		
		return reviewPage;
	}
	
	
	
	/**
	 * 签证团期库列表
	 * 对应需求   c460  可能要暂停
	 */
	@Override
	public Page<Map<String, Object>> queryGroupCodeLibrary4Visa(HttpServletRequest request, HttpServletResponse response) {
		
		/**
		 * 获取登录人所在部门的顶级部门父部门
		 */
		Long office_id = UserUtils.getUser().getCompany().getId();
		Long parentDeptId= 0L;
		//String office_uuid = UserUtils.getUser().getCompany().getUuid();
		List<Department> departments = departmentService.findByOfficeId(office_id);
		for (Department department : departments) {
			if (0==department.getParentId()) {
				parentDeptId = department.getId();
				break;
			}
		}
	    
		
		Page<Map<String, Object>> reviewPage = groupcodeModifiedRecordDao.queryGroupCodeLibrary4Visa(request, response,office_id,parentDeptId);
		
		//签证团号生命周期处理
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){

			String productId  = tMap.get("productId").toString();
			List<GroupcodeModifiedRecord> groupcodeModifiedRecords = null;
			VisaProducts visaProductsold = visaProductsService.findById(Long.parseLong(productId));
			groupcodeModifiedRecords  =  groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByProductIdAndType(visaProductsold.getId().intValue(), new Integer(6));
			GroupCodeLifeBycle groupCodeLifeBycle = new GroupCodeLifeBycle();
			if (null!=groupcodeModifiedRecords&&groupcodeModifiedRecords.size()>0) {
				
				//处理原始团号记录
				groupCodeLifeBycle.setOldGroupCodeCreateDate(visaProductsold.getCreateDate());
				groupCodeLifeBycle.setOldGroupCodeCreatebyName(visaProductsold.getCreateBy().getName());
				groupCodeLifeBycle.setOldGroupCode(groupcodeModifiedRecords.get(0).getGroupcodeOld());
				
				//处理修改的团号记录
				groupCodeLifeBycle.setGroupcodeModifiedRecords(groupcodeModifiedRecords);
			}else{
				//处理原始团号记录
				groupCodeLifeBycle.setOldGroupCodeCreateDate(visaProductsold.getCreateDate());
				groupCodeLifeBycle.setOldGroupCodeCreatebyName(visaProductsold.getCreateBy().getName());
				groupCodeLifeBycle.setOldGroupCode(visaProductsold.getGroupCode());
			}
			tMap.put("groupCodeLifeBycle", groupCodeLifeBycle);
		}
		
		reviewPage.setList(reviewList);
		
		return reviewPage;
	}
	
	
    /**
     * 团期类团号库
     */
	@Override
	public Page<Map<String, Object>> queryGroupCodeLibrary4Tuanqi(HttpServletRequest request,
			HttpServletResponse response) {
		
		String productType = request.getParameter("groupcodelibtype");
	
		Long office_id = UserUtils.getUser().getCompany().getId();
		Long parentDeptId= 0L;
		//String office_uuid = UserUtils.getUser().getCompany().getUuid();
		List<Department> departments = departmentService.findByOfficeId(office_id);
		for (Department department : departments) {
			if (0==department.getParentId()) {
				parentDeptId = department.getId();
				break;
			}
		}
		
		Page<Map<String, Object>> reviewPage = groupcodeModifiedRecordDao.queryGroupCodeLibrary4Tuanqi(request, response,office_id,parentDeptId,productType);
		
		//团号生命周期处理
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){

			String activity_group_id  = null;
			List<GroupcodeModifiedRecord> groupcodeModifiedRecords = null;
			ActivityGroup activityGroup = null;
			if (null!=tMap.get("activity_group_id")) {
				activity_group_id= tMap.get("activity_group_id").toString();
				activityGroup = activityGroupService.findById(Long.parseLong(activity_group_id));
				groupcodeModifiedRecords  =  groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByActivityGroupIdAndType(activityGroup.getId().intValue(), Integer.parseInt(productType));
			}
			
			GroupCodeLifeBycle groupCodeLifeBycle = new GroupCodeLifeBycle();
			if (null!=groupcodeModifiedRecords&&groupcodeModifiedRecords.size()>0) {
				
				//处理原始团号记录
				groupCodeLifeBycle.setOldGroupCodeCreateDate(activityGroup.getCreateDate());
				groupCodeLifeBycle.setOldGroupCodeCreatebyName(activityGroup.getCreateBy().getName());
				groupCodeLifeBycle.setOldGroupCode(groupcodeModifiedRecords.get(0).getGroupcodeOld());
				
				//处理修改的团号记录
				groupCodeLifeBycle.setGroupcodeModifiedRecords(groupcodeModifiedRecords);
			}
			tMap.put("groupCodeLifeBycle", groupCodeLifeBycle);
		}
		
		reviewPage.setList(reviewList);
		
		return reviewPage;
	}
	
	/**
	 * @Description: 团期类产品   根据  产品id  获取各个团期  的 修改信息
	 * @author xinwei.wang
	 * @date 2015年12月2日上午9:33:36
	 * @param productId
	 * @param productType
	 * @return    
	 * @throws
	 */
	public Map<String, List<GroupcodeModifiedRecord>> getGroupCodeByProductId(Long productId){
		
		Map<String, List<GroupcodeModifiedRecord>> groupcodeModifiedRecordMap = new HashMap<String, List<GroupcodeModifiedRecord>>();
		
		TravelActivity travelActivity = travelActivityService.findById(productId);
		Set<ActivityGroup> activityGroups = travelActivity.getActivityGroups();
		List<GroupcodeModifiedRecord> groupcodeModifiedRecords = null;
		for (ActivityGroup activityGroup : activityGroups) {
			groupcodeModifiedRecords  =  groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByActivityGroupId(activityGroup.getId().intValue());
			if (null!=groupcodeModifiedRecords&&groupcodeModifiedRecords.size()>0) {
				groupcodeModifiedRecordMap.put(activityGroup.getGroupCode(), groupcodeModifiedRecords);
			}
		}
		return groupcodeModifiedRecordMap;
	}
	

}
