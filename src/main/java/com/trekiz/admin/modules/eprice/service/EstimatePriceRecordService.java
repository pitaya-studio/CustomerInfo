package com.trekiz.admin.modules.eprice.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ReplyEPrice4AdmitForm;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 询价记录service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePriceRecordService {
	
	/**
	 * 询价记录存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epr
	 */
	public void save(EstimatePriceRecord epr);
	
	/**
	 * 通过id获取询价记录对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价记录id
	 * @return EstimatePriceRecord
	 */
	public EstimatePriceRecord findById(Long id);
	
	/**
	 * 通过id逻辑删除询价记录对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价项目id
	 */
	public void delById(Long id);
	
	/**
	 * 通过询价项目生成一个新的询价记录，同时发送询价“消息”给各个计调
	 * @author lihua.xu
	 * @时间 2014年9月25日
	 * @param epp
	 * @param loginUser
	 * @return
	 */
	public Map<String,Object> addEPriceRecord(EstimatePriceProject epp,User loginUser);
	
	/**
	 * 通过询价项目生成一个新的询价记录，同时发送询价“消息”给各个计调
	 * 通过选择计调主管生成新询价记录
	 * @author jianning.gao
	 * @时间 2015年9月6日
	 * @param epp
	 * @param loginUser
	 * @return
	 */
	public Map<String,Object> addEPriceForManagerRecord(EstimatePriceProject epp,User loginUser);
	
	/**
	 * 通过询价项目id查询获取可多条件排序的分页询价记录列表
	 * @author lihua.xu
	 * @时间 2014年9月26日
	 * @param pid 询价项目id
	 * @param type 询价类型 1、单团；7、机票
	 * @param pageSize 分页大小
	 * @param pageNo 分页页码
	 * @param sort 排序条件 {Column:orderValue} Column:排序字段，orderValue:排序方式 0 倒序 1正序
	 * @return Page<EstimatePriceRecord>
	 */
	public Page<EstimatePriceRecord> findByPid(Long pid, Integer type, int pageSize, int pageNo ,Map<String,Integer> sort, boolean isOpManager);
	
	/**
	 * 接待询价内容被回复(20150515 废弃)
	 * @author lihua.xu
	 * @时间 2014年9月25日
	 * @param reaf
	 * @param loginUser
	 * @return {"res":处理结果，success 成功,"model":EstimatePriceRecord}
	 */
	public Map<String,Object> reply4admit(ReplyEPrice4AdmitForm reaf,User loginUser);
	/**
	 *  接待询价内容被回复  20150515 新增，多币种报价
	 * @author gao
	 * @param replyId
	 * @param content
	 * @param salerTripFileId
	 * @param salerTripFileName
	 * @param salerTipFilePath
	 */
	public void  replyPay4admit(Long replyId,String content,String[] salerTripFileId,String[] salerTripFileName,String[] salerTipFilePath,String operatorPrice);
	/**
	 * 获取展示询价详情的页面数据接口
	 * @author lihua.xu
	 * @时间 2014年9月29日
	 * @param rid 询价记录id
	 * @param loginUser 登陆用户
	 * @return
	 */
	public Map<String,Object> recordInfoShow(Long rid,User loginUser);
	
	/**
	 * 产品发布成功后，调用该方法，通知询价项目状态改为“已发布产品”
	 * @param Pid
	 */
	public void successProject(Long Pid);

	/**
	 * 异步提交修改外报价
	 * @author yue.wang
	 * @时间 2014年12月12日
	 * @param result
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public Map updateEpriceOutPrice(Map conditionMap);

	/**
	 * 查询行程附件id
	 * @author yue.wang
	 * @时间 2014年12月15日
	 * @param result
	 * @return
	 */
	public List<String> getXcFilesIdsByResultPage(Page<EstimatePriceRecord> page);

	/**
	 * 查询报价附件id
	 * @author yue.wang
	 * @时间 2014年12月15日
	 * @param result
	 * @return
	 */
	public List<String> getbjFilesIdsByResultPage(Page<EstimatePriceRecord> page);
	
	
	/**
	 * 询价发布产品
	 * @author yue.wang
	 * @时间 2014年12月15日
	 * @param recordId  type 1地接报价  7机票报价
	 * @return str.equals("suc") 说明成功 否则返回错误提示信息
	 */
	public String releaseProduct(Long recordId,String type);
	
	/**
	 * 询价生成订单
	 * @author yue.wang
	 * @时间 2014年12月15日
	 * @param recordId 
	 * @return str.equals("suc") 说明成功 否则返回错误提示信息
	 */
	public String releaseOrder(Long recordId);
	
	/**
	 * 根据pid查询询价记录
	 * @param pid
	 * @return
	 */
	public List<EstimatePriceRecord> getAoperatorUserIdByPid(Long pid);
}
