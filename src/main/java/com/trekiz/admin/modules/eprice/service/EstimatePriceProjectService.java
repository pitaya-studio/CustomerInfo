package com.trekiz.admin.modules.eprice.service;

import java.util.Map;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ListSearchForm;
import com.trekiz.admin.modules.eprice.form.ProjectFirstForm;
import com.trekiz.admin.modules.eprice.form.ProjectSecondForManagerForm;
import com.trekiz.admin.modules.eprice.form.ProjectSecondForm;
import com.trekiz.admin.modules.eprice.form.ProjectThirdForManagerForm;
import com.trekiz.admin.modules.eprice.form.ProjectThirdForm;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 询价项目service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePriceProjectService {

	/**
	 * 询价项目存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epp
	 */
	public void save(EstimatePriceProject epp);
	
	/**
	 * 询价项目更新的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epp
	 */
	public void update(EstimatePriceProject epp);
	
	/**
	 * 通过id获取询价项目对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价项目id
	 * @return EstimatePriceProject
	 */
	public EstimatePriceProject findById(Long id);
	
	/**
	 * 通过id逻辑删除询价项目对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价项目id
	 */
	public void delById(Long id);
	
	/**
	 * 多条件可排序多条件查询分页询价项目列表
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param pageSize 分页大小
	 * @param pageNo 分页页码
	 * @param keyword 搜索关键字（目前搜索计调姓名/国家/客户）
	 * @param salerId 销售id
	 * @param operatorUserId 计调id
	 * @param estimateStatus 询价项目状态 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品
	 * @param type 询价项目类型 1 单团询价  2 机票询价
	 * @param travelCountryId 线路国家id
	 * @param startGroupOutDate 出团时间区间——起始时间
	 * @param endGroupOutDate 出团时间区间——结束始时间
	 * @param epriceStartDate 最近询价日期--起始时间
	 * @param epriceEndDate 最近询价日期--结束时间
	 * @param sort 排序 {Column:orderValue} Column:排序字段，orderValue:排序方式 0 倒序 1正序
	 * @return Page<EstimatePriceProject>
	 */
	public Page<EstimatePriceProject> findByPage(int pageSize,int pageNo,String keyword,Long salerId,Long operatorUserId,
			Integer estimateStatus,Integer type,Long travelCountryId,String startGroupOutDate,String endGroupOutDate,
			String epriceStartDate,String epriceEndDate,
			Map<String,Integer> sort,DepartmentCommon comm,ListSearchForm lsf);
	
	/**
	 * 多条件可排序多条件查询分页询价项目列表
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param lsf 搜索表单对象
	 * @param pageSize 分页大小
	 * @param pageNo 分页页码
	 * @return Page<EstimatePriceProject>
	 */
	public Page<EstimatePriceProject> findByPage(ListSearchForm lsf,int pageSize,int pageNo,DepartmentCommon comm);
	
	/**
	 * 新增询价项目第一步：创建询价项目（草稿），创建询价基本信息记录（草稿）来存储询价基础信息数据
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff 新增询价项目第一步表单数据接收类
	 * @param loginUser 当前登陆的用户
	 * @return Map<String,Object> {"res": 处理结果，"mes":处理描述，"model":EstimatePriceProject 成功创建的询价项目}
	 */
	public Map<String,Object> addProjectFirst(ProjectFirstForm pff,User loginUser);
	
	
	/**
	 * 新增询价项目第二步：创建询价接待要求记录（草稿）来存储询价基础信息数据
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param psf 新增询价项目第二步表单数据接收类
	 * @param loginUser 当前登陆的用户
	 * @return Map<String,Object> {"res": 处理结果，"mes":处理描述，"model":EstimatePriceProject 成功创建的询价项目}
	 */
	public Map<String,Object> addProjectSecond(ProjectSecondForm psf,User loginUser);
	
	/**
	 * 新增询价项目第二步：创建询价接待要求记录（草稿）来存储询价基础信息数据
	 * add 20150906 使用计调主管来分配计调。 
	 * @param psf
	 * @param loginUser
	 * @return
	 */
	public Map<String, Object> addProjectForManagerSecond(ProjectSecondForManagerForm psf,User loginUser);
	
	/**
	 * 新增询价项目第三步：创建机票询价要求记录（草稿）来存储询价基础信息数据
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param ptf 新增询价项目第三步表单数据接收类
	 * @param loginUser 当前登陆的用户
	 * @return Map<String,Object> {"res": 处理结果，"mes":处理描述，"model":EstimatePriceProject 成功创建的询价项目}
	 */
	public Map<String,Object> addProjectThird(ProjectThirdForm ptf,User loginUser);
	
	/**
	 * 新增询价项目第三步：创建机票询价要求记录（草稿）来存储询价基础信息数据
	 add 20150906 使用计调主管来分配计调。 
	 * @param psf
	 * @param loginUser
	 * @return
	 */
	public Map<String,Object> addProjectForManagerThird(ProjectThirdForManagerForm ptf,User loginUser);
	
	/**
	 * 生成询价项目title算法
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param epp 询价项目对象
	 * @return 询价项目title
	 */
	public String createTitle(EstimatePriceProject epp);
	
	/**
	 * 为询价记录rid回复页面服务，提供询价记录和询价项目数据
	 * @author lihua.xu
	 * @时间 2014年9月28日
	 * @param rid 询价项目id
	 * @return Map<String,Object> {"res": 处理结果，"mes":处理描述，"record":EstimatePriceRecord 询价记录，"project":EstimatePriceProject 询价项目,"type"：回复记录分类,1:地接计调，2机票计调}
	 */
	public Map<String,Object> replayopView(Long rid,User loginUser,String types);
	
	
	
	/**
	 * 
	 * @param pid
	 * @return
	 */
	public Map<String,Object> erlistMore(Long pid);

	/**
	 * 多条件可排序多条件查询分页询价项目列表
	 * @author yue.wang
	 * @时间 2014年01月05日
	 * @param lsf 搜索表单对象
	 * @param pageSize 分页大小
	 * @param pageNo 分页页码
	 * @return Page<EstimatePriceProject>
	 */
	public Page<EstimatePriceProject> findListByPage(ListSearchForm lsf,
			Integer pageSize, Integer pageNo, DepartmentCommon common);
	
	/**
	 * 多条件可排序多条件查询分页询价项目列表
	 * @param lsf 搜索表单对象
	 * @param pageSize 分页大小
	 * @param pageNo 分页页码
	 * @return Page<EstimatePriceProject>
	 */
	public Page<EstimatePriceProject> findListByPageForManager(ListSearchForm lsf, Integer pageSize, Integer pageNo);
	
}
