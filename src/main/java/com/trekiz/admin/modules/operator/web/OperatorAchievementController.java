package com.trekiz.admin.modules.operator.web;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.utils.excel.CommonExcel;
import com.trekiz.admin.common.utils.excel.CommonExcelUtils;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.finance.service.ISettleService;
import com.trekiz.admin.modules.operator.service.OperatorAchievementService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
/**
 * 操作人员业绩
 * @author chao.zhang@quauq.com
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/operator/achievement")
public class OperatorAchievementController {
	
	@Autowired
	private OperatorAchievementService operatorAchievementService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ISettleService settleService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ISelectService selectService;
	/**
	 * 操作人员业绩总列表查询
	 * @return
	 */
	@RequestMapping(value="getList")
	public String getList(HttpServletRequest request, HttpServletResponse response, Model model){
		//团号
		String groupCode = request.getParameter("groupCode");
		model.addAttribute("groupCode",groupCode);
		//部门id
		String departmentId = request.getParameter("departmentId");
		model.addAttribute("departmentId",departmentId);
		//销售id
		String salerId = request.getParameter("salerId");
		model.addAttribute("salerId",salerId);
		//产品名称
		String productName = request.getParameter("productName");
		model.addAttribute("productName",productName);
		//汇总月份
		String groupOpenDate = request.getParameter("groupOpenDate");
		model.addAttribute("groupOpenDate",groupOpenDate);

		Page<Map<String,Object>> page = operatorAchievementService.getOperatorAchievementList(new Page<Map<String,Object>>(request, response), request);
		for(Map<String,Object> map:page.getList()){
			Long activityId=Long.parseLong(map.get("activityId").toString());
			Integer orderType=Integer.parseInt(map.get("kind").toString());
			Map<String, Object> settle=null;
			if(orderType!=6 && orderType!=7){
				Long groupId=Long.parseLong(map.get("groupId").toString());
				settle = settleService.getSettleMap(orderType, groupId, "");
			}else{
				settle = settleService.getSettleMap(orderType, activityId, "");
			}
			map.put("profitSum", settle.get("profitSum"));
		}
		model.addAttribute("page", page);
		//查询所有员工操作员
		model.addAttribute("users", operatorAchievementService.getUserList());
		//查询部门
		model.addAttribute("departments", selectService.loadDepartment(UserUtils.getUser().getCompany().getId().longValue()));
		return "modules/operator/operatorAchievement";
	}
	
	/**
	 * 查询总毛利和总人数（现未使用）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getZongJi")
	public String getZongJi(HttpServletRequest request){
		//获得总人数和总毛利
		List<Map<String,Object>> list = operatorAchievementService.getPersonSum(request);
		Integer personNum=0;
		BigDecimal big=new BigDecimal("0.00");
		for(Map<String,Object> map:list){
			//总人数
			personNum+=Integer.parseInt(map.get("personNum").toString());
			Long activityId=Long.parseLong(map.get("activityId").toString());
			Integer orderType=Integer.parseInt(map.get("kind").toString());
			Map<String, Object> settle1=null;
			if(!orderType.toString() .equals(Context.ORDER_STATUS_VISA)  && !orderType.toString().equals(Context.ORDER_STATUS_AIR_TICKET) ){
				Long groupId=Long.parseLong(map.get("groupId").toString());
				settle1 = settleService.getSettleMap(orderType, groupId, "");
			}else{
				settle1 = settleService.getSettleMap(orderType, activityId, "");
			}
			//总毛利
			big=big.add(new BigDecimal(settle1.get("profitSum").toString().replace(",", "")));
		}
		//转换千分位
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		//总人数
		String str1 = numberFormat.format(personNum);
		//总毛利
		DecimalFormat df = new DecimalFormat("###,##0.00");
		String str2 = df.format(big);
		String str=str1+"-"+str2;
		return str;
	}
	
	/**
	 * 操作人员业绩表 导出EXCEL
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="exportToExcel")
	public void exportToExcel(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> data = operatorAchievementService.getPersonSum(request);
		List<List<Object>> list=new ArrayList<List<Object>>();
		int i=1;
		for(Map<String,Object> map:data){
			List<Object> obj=new ArrayList<Object>();
			Long activityId=Long.parseLong(map.get("activityId").toString());
			Integer orderType=Integer.parseInt(map.get("kind").toString());
			Map<String, Object> settle=null;
			if(orderType!=6 && orderType!=7){
				Long groupId=Long.parseLong(map.get("groupId").toString());
				settle = settleService.getSettleMap(orderType, groupId, "");
			}else{
				settle = settleService.getSettleMap(orderType, activityId, "");
			}
			obj.add(i);
			i++;
			obj.add(map.get("departmentName"));
			if (map.get("createBy")!=null && !map.get("createBy").toString().equals("")) {
				User user = userDao.getById(Long.parseLong(map.get("createBy")
						.toString()));
				obj.add(user.getName());
			}else{
				obj.add("");
			}
			obj.add(map.get("acitivityName"));
			obj.add(map.get("groupCode"));
			obj.add(map.get("personNum"));
			if(settle.get("profitSum").equals("0.00")){
				obj.add("");
			}else{
				obj.add("￥"+settle.get("profitSum"));
			}
			obj.add("");
			list.add(obj);
		}
		String[] headers={"序号","部门","操作人员姓名","产品名称","团号","人数（不含领队）","毛利","备注"};
		HSSFWorkbook workBook=new HSSFWorkbook();
		HSSFCellStyle style=workBook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		String dateNow=request.getParameter("groupOpenDate");
		if(dateNow.equals("")){
			dateNow="__月";
		}
		CommonExcel excel = CommonExcelUtils.createExcelWithEnclosure("操作人员"+dateNow+"业绩汇总表",headers , null, list,workBook );
		excel.setHeaderStyle(style);
		Integer[] s={5,6};
		excel.addSumRow(4, "合计：", s);
		String fileName="操作业绩汇总表.xls";
		response.setContentType("octets/stream");
		OutputStream os=null;
		try {
			response.setHeader("Content-Disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
			os=response.getOutputStream();
			excel.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
