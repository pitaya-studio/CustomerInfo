package com.trekiz.admin.agentToOffice.T2.service;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.common.persistence.Page;
/**
 * 获取QUAUQ服务费统计
 * @author ayong.bao
 *
 */
public interface QuauqServiceFeeService {

	/**
	 * 获取QUAUQ服务费统计
	 * @param page 分页
	 * @param priceType 报名时选择的价格类型0:同行价 1:直客价 2:quauq价
	 * @param groupBy 是否分供应商查询  还是统计全部 true是分供应商查询
	 * @return
	 */
	public Page<Map<String,Object>> getQuauqServiceFeeStatistics(Page<?> page, boolean groupBy, String officeId);
	
	/**
	 * quauq交易明细
	 * @param page
	 * @param mapRequest
	 * @return
	 */
	public Page<Map<Object, Object>> quauqTradeDetail(Page<Map<Object, Object>> page, Map<String, String> mapRequest);
	
	/**
	 * 单个批发商交易明细下的总计统计
	 * @param mapRequest
	 * @return
	 */
	public Map<String, Object> findSum2Office(Map<String, String> mapRequest);
	
	/**
	 * 批发商QUAUQ服务费的总计
	 * @return
	 */
	public String findSum2QuauqCharge(String officeId);
	
	/**
	 * 批发商渠道服务费的总计
	 * @return
	 */
	public String findSum2AgentCharge(String officeId);
	
	/**
	 * 批发商抽成服务费的总计
	 * @return
	 */
	public String findSum2CutCharge(String officeId);
	
	/**
	 * 批发商订单数和人数总计
	 * @return
	 */
	public Map<String, Object> findSum2OrderAndPersonCount();
	
	/**
	 * 设置改变订单缴费状态
	 * @param orderIds
	 * @param changeType
	 * @return
	 */
	public void changeOrderIsPayedCharge(String orderIds,Integer changeType);

	/**
	 * 获取订单结算价操作日志json数据
	 * @param orderType
	 * @param orderId
	 * @return
	 */
	public JSONArray getPayPriceLogData(String orderType, String orderId);

	/**
	 * 查询批发商的默认费率值。
	 * @author yudong.xu 2016.8.10
	 */
	public void getOfficeRate(Page<Map<Object, Object>> page, Map<String, String> mapRequest);

	/**
	 * 保存用户设置的批发商默认费率值。
	 * @param itemStr json格式的用户输入数组数据
	 * @param uuidStr json格式的用户选择的公司uuid数组数据
	 * @author yudong.xu 2016.8.15
     */
	public void saveDefaultRate(String itemStr,String uuidStr);

	/**
	 * 查询对应批发商的Quauq费率和渠道费率
	 * @param companyUuid		批发商uuid
	 * @param agentType			渠道类型(门店、总社、集团客户)
	 * @return
	 * @author	shijun.liu
	 * @date	2016.08.11
     */
	public Rate getCompanyRate(String companyUuid, Integer agentType);


	/**
	 * 根据公司(渠道商)的uuid来新增所有类型的费率。将quauq的默认费率设置为1%,其它不进行设置。
	 * @param companyUuid
	 * @author yudong.xu 2016.8.29
	 */
	public void saveAllTypeCompanyRate(String companyUuid);
	
	/**
	 * excel导出 查询列表
	 * @param mapRequest
	 * @return
	 */
	public List<Map<Object,Object>> getOfficeRateList( Map<String,String> mapRequest);
	
	/**
	 * 生成Excel
	 * @param map
	 */
	public  HSSFWorkbook createRateExcel(List<Map<Object,Object>> list);
	
	/**
	 * 自定义表头
	 * @param sheet
	 * @param titleList 表头中字段的名字
	 * @param list 为（int[]:为每一个CellRangeAddress的参数 数组的长度为4）的集合
	 * @param cellStyle
	 * @return
	 */
	public int createTitleRow(HSSFSheet sheet,List<String[]> titleList, int rowNum,HSSFCellStyle cellStyle,List<int[]> list);
	
	/**
	 * 创建表的body
	 * @param sheet
	 * @param bodyList 表所需数据 （String[] 中的一个元素为每一行中的一个单元格的值）
	 * @param rowNum 表的body从第几行开始
	 * @param cellStyle 样式
	 * @author chao.zhang
	 */
	public void createBodyRow(HSSFSheet sheet,List<String[]> bodyList,int rowNum,HSSFCellStyle cellStyle);

}
