package com.trekiz.admin.modules.activity.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.GroupControlBoard;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.GroupControlBoardDao;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 团控板操作
 *@author tao.liu
 * 
 */
@Service
@Transactional(readOnly = true)
public class GroupControlBoardService extends BaseService implements IGroupControlBoardService {

	@Autowired
	private GroupControlBoardDao groupControlBoardDao;

	@Autowired
	private ActivityGroupService activityGroupService;

	@Autowired
	private TravelActivityService travelActivityService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	
	@Autowired
	private UserDao userDao;

	/**
	 * 获取团控板首页数据集
	 * 由于部门,角色条件不好拼接sql,此方法暂时没使用
	 */
	@Override
	public List<Map<String, Object>> getGroupControlBoardList(
			String nameOrCode, String groupOpenDateFrom, String groupOpenDateTo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT tra.acitivityName,agp.groupCode,DATE_FORMAT(agp.groupOpenDate, '%Y-%m-%d') groupOpenDate,agp.id groupId, ")
		.append(" (agp.receptAmount+agp.nopayReservePosition+agp.payReservePosition) hasReceptAmount,agp.planPosition,agp.freePosition ")
		.append(" FROM activitygroup agp LEFT JOIN travelactivity tra ON agp.srcActivityId = tra.id ");

		String where = getWhere(nameOrCode, groupOpenDateFrom, groupOpenDateTo);

		sb.append(where);
		
		List<Map<String, Object>> list = groupControlBoardDao.findBySql(
				sb.toString(), Map.class);

		return list;
	}
	
	/**
	 * 获取团控板首页数据集
	 */
	@Override
	public List<Map<String, Object>> getGroupControlBoardListNew(Page<ActivityGroup> page,
			String nameOrCode, String groupOpenDateFrom, String groupOpenDateTo) throws ParseException {
		//按部门展示 暂时注掉 去掉部门角色条件限制
//		DepartmentCommon common = null;
//		if (StringUtils.isNotBlank(listFlag) && "".equals(listFlag)) {
//			common = departmentService.setDepartmentPara("bookOrder", null);
//		} else {
//			common = departmentService.setDepartmentPara("bookOrder", null);
//		}
		
		DetachedCriteria dc = activityGroupDao.createDetachedCriteria();
		
		//筛选没有删除的团期
		dc.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat Fmt_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isNotEmpty(groupOpenDateFrom)) {
			groupOpenDateFrom = groupOpenDateFrom + " 00:00:00";
		} else {
			// 获取前一月的第一天
			Calendar cal_from = Calendar.getInstance();// 获取当前日期
			cal_from.add(Calendar.MONTH, -1);
			cal_from.set(Calendar.DAY_OF_MONTH, 1);// 第一天
			groupOpenDateFrom = format.format(cal_from.getTime()) + " 00:00:00";
		}
		dc.add(Restrictions.ge("groupOpenDate", Fmt_date.parse(groupOpenDateFrom)));
		
		if (StringUtils.isNotBlank(groupOpenDateTo)) {
			groupOpenDateTo = groupOpenDateTo + " 23:59:59";
			dc.add(Restrictions.le("groupOpenDate", Fmt_date.parse(groupOpenDateTo)));
		}
		
		DetachedCriteria productDc = DetachedCriteria.forClass(TravelActivity.class);
		
		//没有删除的数据
		productDc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		if (StringUtils.isNotBlank(nameOrCode)) {
			dc.add(Restrictions.or(Restrictions.like("groupCode", "%" + nameOrCode + "%"),
					Restrictions.sqlRestriction("{alias}.srcActivityId in (select id from travelactivity where acitivityName like '%" + nameOrCode + "%')")));
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (companyId != null) {
			productDc.add(Restrictions.eq("proCompany", companyId));
		}
		productDc.add(Restrictions.eq("activityKind", 2)); // 散拼产品
		
		productDc.add(Restrictions.eq("activityStatus", 2)); // 产品上架状态
		
//		systemService.getDepartmentSql("activity", productDc, null, common, 2);
		
		productDc.setProjection(Property.forName("id"));
		dc.add(Property.forName("srcActivityId").in(productDc));
		
		int pageNo = page.getPageNo(); // 页面请求的第几页
		
		Page<ActivityGroup> find = activityGroupDao.find(page, dc);

		int permitSize = (int) Math.ceil((double)page.getCount()/(double)page.getPageSize());
		List<Map<String, Object>> result = new ArrayList<>();
		// 由于分页方法中,当请求参数查出的条数小于每页显示条数 pageSize >= count时,pageNo自动设为1,
		// 或者查询的第一条记录大于查出的总条数时 firstResult >= getCount(),firstResult设为0,这两种情况导致请求的页数增大始终能查出第一页的数据给前台,故加此限制
		if (pageNo <= permitSize) { 
			List<ActivityGroup> groupList = find.getList();
			
			// 处理数据 将需要的数据存入map集合
			for (ActivityGroup agp : groupList) {
				String acitivityName = agp.getTravelActivity().getAcitivityName();
				String groupCode = agp.getGroupCode();
				String groupOpenDate = format.format(agp.getGroupOpenDate());
				Long id = agp.getId();
				Integer hasReceptAmount= agp.getReceptAmount() + agp.getNopayReservePosition() + agp.getPayReservePosition();
				Integer planPosition = agp.getPlanPosition();
				Integer freePosition = agp.getFreePosition();
				HashMap<String,Object> map = new HashMap<>();
				map.put("acitivityName", acitivityName);
				map.put("groupCode", groupCode);
				map.put("groupOpenDate", groupOpenDate);
				map.put("groupId", id);
				map.put("hasReceptAmount", hasReceptAmount);
				map.put("planPosition", planPosition);
				map.put("freePosition", freePosition);
				result.add(map);
			}
		}
		return result;
	}
	
	private String getWhere(String nameOrCode, String groupOpenDateFrom,
			String groupOpenDateTo) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer();
		Long officeId = UserUtils.getUser().getCompany().getId();
		sb.append(" WHERE tra.proCompany = " + officeId) // 供应商id
		  .append(" AND tra.activity_kind = 2 "); // 散拼产品
		if (StringUtils.isNotBlank(nameOrCode)) { // 产品名称或团号
			nameOrCode = nameOrCode.trim();
			sb.append(" AND (agp.groupCode like '%" + nameOrCode + "%' OR tra.acitivityName like '%" + nameOrCode + "%' ) ");
		}
		// 出团日期
		if (StringUtils.isNotBlank(groupOpenDateFrom)) {
			sb.append(" AND agp.groupOpenDate >= '" + groupOpenDateFrom + " 00:00:00" + "'");
		} else {
			// 获取前一月的第一天
			Calendar cal_from = Calendar.getInstance();// 获取当前日期
			cal_from.add(Calendar.MONTH, -1);
			cal_from.set(Calendar.DAY_OF_MONTH, 1);// 第一天
			String firstDay = format.format(cal_from.getTime());
			sb.append(" AND agp.groupOpenDate >= '" + firstDay + " 00:00:00" + "'");
		}
		if (StringUtils.isNotBlank(groupOpenDateTo)) {
			sb.append(" AND agp.groupOpenDate <= '" + groupOpenDateTo + " 23:59:59" + "'");
		} else {
			// 获取后两月的最后一天
			Calendar cal_to = Calendar.getInstance();
			cal_to.add(Calendar.MONTH, 2);
			cal_to.set(Calendar.DAY_OF_MONTH,cal_to.getActualMaximum(Calendar.DAY_OF_MONTH));// 最后一天
			String lastDay = format.format(cal_to.getTime());
			sb.append(" AND agp.groupOpenDate <= '" + lastDay + " 23:59:59" + "'");
		}
		return sb.toString();
	}

	/**
	 * 团控板操作页
	 */
	@Override
	public Map<String, Object> groupControlBoardOpePage(long groupId,
			String opeType) {
		Map<String, Object> result = new HashMap<>();
		// 产品和团期信息
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT tra.acitivityName, tra.fromArea, tra.activityDuration, agp.groupCode, agp.groupOpenDate, ")
			.append(" agp.freePosition freePosition, agp.settlementAdultPrice, agp.settlementcChildPrice, agp.settlementSpecialPrice, ")
			.append(" agp.suggestAdultPrice, agp.suggestChildPrice, agp.suggestSpecialPrice, agp.currency_type currencyType ")
			.append(" FROM activitygroup agp  LEFT JOIN travelactivity tra ON agp.srcActivityId = tra.id WHERE agp.id = " + groupId);

		List<Map<String, Object>> list = groupControlBoardDao.findBySql(sb.toString(),Map.class);
		Map<String, Object> map = null;
		if (list.size() > 0) {
			map = list.get(0);
		}

		if (map.get("currencyType") != null) {
			String currencyType = map.get("currencyType").toString(); // 币种
			// 对同行价和直客价进行处理
			handMapDate(map, currencyType, "settlementAdultPrice", 0);
			handMapDate(map, currencyType, "settlementcChildPrice", 1);
			handMapDate(map, currencyType, "settlementSpecialPrice", 2);
			handMapDate(map, currencyType, "suggestAdultPrice", 3);
			handMapDate(map, currencyType, "suggestChildPrice", 4);
			handMapDate(map, currencyType, "suggestSpecialPrice", 5);
		}
		
		// 出发城市
		if(map.get("fromArea") == null){
			map.put("fromArea", "");
		}else{
			String fromArea = DictUtils.getDictLabel(map.get("fromArea").toString(), "from_area", "");
			map.put("fromArea", fromArea);
		}

		map.put("groupId", groupId);
		map.put("opeType", opeType); // 操作类型 目前团控版只有 1收客

		result.put("traAndGroupDetail", map);
		return result;
	}

	/**
	 * 参数处理 若Map中价格为空或null,则价格置为"-",对应的币种置为空串""
	 * 
	 * @param map
	 * @param currencyType
	 * @param moneykey
	 * @param currencyIndex
	 */
	private void handMapDate(Map<String, Object> map, String currencyType,
			String moneykey, Integer currencyIndex) {
		String amont = map.get(moneykey) == null ? "" : map.get(moneykey).toString();
		if (StringUtils.isEmpty(amont)) {
			map.put(moneykey, "-");
			map.put(moneykey + "Currency", "");
		} else {
			map.put(moneykey, amont);
			map.put(moneykey + "Currency", CurrencyUtils.getCurrencyInfo(currencyType, currencyIndex, "mark"));
		}
	}

	@Override
	public Map<String, Object> groupContralBoardOpeRecord(HttpServletRequest request) {
		String groupId = request.getParameter("groupId");
		Integer pageNo = StringNumFormat.getIntegerValue(request.getParameter("pageNo")); // 第几页
		Integer pageSize = StringNumFormat.getIntegerValue(request.getParameter("pageSize")); // 每页条数
		Integer firstPage = (pageNo - 1) * pageSize; // 要显示的第一条记录
		BigInteger count = new BigInteger("0"); // 总条数
		
		String where = getOpeRecordWhere(request); // 日期查询条件

		StringBuffer sql = new StringBuffer(); // 操作记录列表sql
		sql.append(" SELECT DATE_FORMAT(gcb.create_date, '%Y-%m-%d %T') opeTime, gcb.operate_name opeName, gcb.operate_type opetype, ")
				.append(" gcb.amount, gcb.remarks FROM group_control_board gcb WHERE gcb.group_id = " + groupId )
				.append(" AND gcb.officeId = " + UserUtils.getUser().getCompany().getId());

		if (StringUtils.isNotEmpty(where)) {
			sql.append(where);
		}
		
		String countSqlString = "select count(*) from (" + sql.toString() +" ) tmp"; 
		List<BigInteger> counts = groupControlBoardDao.findBySql(countSqlString);
		
		if(counts.size() > 0){
			count = counts.get(0);
		}
		
		sql.append(" ORDER BY gcb.create_date DESC ") // 根操作时间排序
		   .append(" limit " + firstPage + "," + pageSize);
		
		List<Map<String, Object>> list = groupControlBoardDao.findBySql(
				sql.toString(), Map.class); // 单个团号的操作记录

		// 查询产品,团号, 出团日期
		String groupSql = "SELECT tral.acitivityName, agp.groupCode, DATE_FORMAT(agp.groupOpenDate, '%Y-%m-%d') groupOpenDate FROM activitygroup agp"
				+ " LEFT JOIN travelactivity tral ON agp.srcActivityId = tral.id WHERE agp.id = " + groupId;
		List<Map<String, Object>> list2 = groupControlBoardDao.findBySql(groupSql, Map.class);
		Map<String, Object> map = null;
		if (list2.size() > 0) {
			map = list2.get(0);
		}
		
		// 页面的信息
		Map<String, String> pageMap = new HashMap<>();
		pageMap.put("pageNo", pageNo.toString());
		pageMap.put("pageSize", pageSize.toString());
		pageMap.put("count", count.toString());
		pageMap.put("groupId", groupId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("singleList", list);
		result.put("groupMessage", map);
		result.put("pageInfo", pageMap);
		return result;
	}

	@Override
	public Map<String, Object> groupContralBoardOpeRecordAll(HttpServletRequest request) {
		Integer pageNo = StringNumFormat.getIntegerValue(request.getParameter("pageNo")); // 第几页
		Integer pageSize = StringNumFormat.getIntegerValue(request.getParameter("pageSize")); // 每页条数
		Integer firstPage = (pageNo - 1) * pageSize; // 要显示的第一条记录
		BigInteger count = new BigInteger("0"); // 总记录条数
		String where = getOpeRecordWhere(request); // 日期查询条件

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DATE_FORMAT( gcb.create_date, '%Y-%m-%d %T' ) opeTime, gcb.operate_name opeName, gcb.travelactivity_name tralActivityName, ")
				.append(" gcb.group_code groupCode, gcb.operate_type opetype, gcb.amount, gcb.remarks FROM group_control_board gcb ")
				.append(" where gcb.officeId = " + UserUtils.getUser().getCompany().getId());

		if (StringUtils.isNotBlank(where)) {
			sql.append(where);
		}
		
		String countSqlString = "select count(*) from (" + sql.toString() +" ) tmp"; 
		List<BigInteger> counts = groupControlBoardDao.findBySql(countSqlString);
		
		if(counts.size() > 0){
			count = counts.get(0);
		}
		
		sql.append(" ORDER BY gcb.create_date DESC ") // 根操作时间排序
		   .append(" limit " + firstPage + "," + pageSize);

		List<Map<String, Object>> list = groupControlBoardDao.findBySql(sql.toString(), Map.class);
		
		// 页面的信息
		Map<String, String> pageMap = new HashMap<>();
		pageMap.put("pageNo", pageNo.toString());
		pageMap.put("pageSize", pageSize.toString());
		pageMap.put("count", count.toString());

		Map<String, Object> result = new HashMap<>();
		result.put("wholeList", list);
		result.put("pageInfo", pageMap);
		
		return result;
	}

	private String getOpeRecordWhere(HttpServletRequest request) {
		String nameOrCode = request.getParameter("nameOrCode"); // 产品名称或团号
		String dateType = request.getParameter("selectDateType"); // 查询日期类型：1全部、 2按单日(某个日期)、3 自定义日期区间)
		StringBuffer where = new StringBuffer();
		where.append(" AND gcb.operate_type > 0 ");
		if (StringUtils.isNotBlank(nameOrCode)) {
			nameOrCode= nameOrCode.trim();
			where.append(" AND (gcb.group_code like '%" + nameOrCode + "%' OR gcb.travelactivity_name like '%" + nameOrCode + "%' ) ");
		}
		if ("2".equals(dateType)) {
			String date = request.getParameter("selectDate");
			if (StringUtils.isNotEmpty(date)) {
				where.append(" AND gcb.create_date like '" + date + "%'");
			}
		} else if ("3".equals(dateType)) {
			String dateFrom = request.getParameter("dateFrom");
			String dateTo = request.getParameter("dateTo");
			if (StringUtils.isNotEmpty(dateFrom)) {
				where.append(" AND gcb.create_date >= '" + dateFrom + " 00:00:00'");
			}
			if (StringUtils.isNotBlank(dateTo)) {
				where.append(" AND gcb.create_date <= '" + dateTo + " 23:59:59'");
			}
		}
		return where.toString();
	}

	/**
	 * 插入团控板数据
	 * 
	 * @param opeType
	 *            操作项 1收客 2报名 3余位调整 4订单修改 5退团 6转团(转出团) 7转团(转入团) 8订单取消(包括操作取消和11系统自动取消的订单) 9订单删除 10财务驳回取消占位 
	 * @param amount
	 *            数量
	 * @param remarks
	 *            备注
	 * @param groupId
	 *            团期id
	 * @param createBy
	 *            操作者id(针对转团、退团操作、系统自动取消的订单传入,其他操作可传入-1)
	 * @return
	 */
	@Override
	public Map<String, String> insertGroupControlBoard(Integer opeType,
			Integer amount, String remarks, long groupId, long createBy) {
		Map<String, String> result = new HashMap<String, String>();
		User user = UserUtils.getUser();
		ActivityGroup group = activityGroupService.findById(groupId);
		TravelActivity travelActivity = travelActivityService.findById(Long.parseLong(group.getSrcActivityId().toString()));
		GroupControlBoard groupControlBoard = new GroupControlBoard();
		if (travelActivity.getActivityKind() == 2) { // 散拼产品
			if (1 == opeType) { // 收客	
				if(amount <= 0 || amount > 99999){
					result.put("message", "输入收客的数量不合法!");
					result.put("result", "fail");
					return result;
				}
				if (amount > group.getFreePosition()) {
					result.put("message", "不能大于余位!");
					result.put("result", "fail");
					return result;
				}
				if(remarks.trim().length() > 30){
					result.put("message", "备注的数量不能超过30字!");
					result.put("result", "fail");
					return result;
				}
				group.setReceptAmount(group.getReceptAmount() + amount); // 增加收客
				group.setFreePosition(group.getFreePosition() - amount); // 团期减余位
				// 保存团期余位、已收人数
				activityGroupService.updateObj(group);
			} else if (5 == opeType || 6 == opeType || 7 == opeType || 11 == opeType) { // 针对转团、退团操作 、订单取消中的系统自动取消的订单
				if (userDao.findById(createBy) != null) {
					user = userDao.findById(createBy);
				}
				if (11 == opeType) { 
					opeType = 8; // 订单取消
				}
			} 
			
			groupControlBoard.setAmount(amount);	
			groupControlBoard.setGroupCode(group.getGroupCode());
			groupControlBoard.setGroupId(group.getId());
			groupControlBoard.setOpeDate(new Date());
			groupControlBoard.setOpeId(user.getId());
			groupControlBoard.setOpeLoginName(user.getLoginName());
			groupControlBoard.setOpeName(user.getName());
			groupControlBoard.setOfficeId(user.getCompany().getId());
			groupControlBoard.setOperateType(opeType);
			groupControlBoard.setRemarks(remarks);
			groupControlBoard.setTravelActivityId(travelActivity.getId());
			groupControlBoard.setTravelActivityName(travelActivity.getAcitivityName());
			groupControlBoard.setUuid(UuidUtils.generUuid());
			groupControlBoardDao.save(groupControlBoard);
		}
		result.put("result", "success");
		return result;
	}

}
