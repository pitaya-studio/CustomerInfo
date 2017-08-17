package com.trekiz.admin.modules.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.T2.service.QuauqServiceFeeService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.repository.SupplyContactsDao;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.message.service.MessageMsgService;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.ZhifubaoDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.wholesalerbase.form.OfficeSelectForm;

/**
 * 批发商Service
 * @author zj
 * @version 2013-11-19
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	private static final Logger logger =LoggerFactory.getLogger(MessageMsgService.class);
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private QuauqServiceFeeService quauqServiceFeeService;
	

	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	@Autowired
	private SupplyContactsDao supplyContactsDao;
	@Autowired
	private ZhifubaoDao zhifubaoDao;
	
	public Office get(Long id) {
		return officeDao.findOne(id);
	}
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList(false, null, null);
	}
	
	public List<Office> findSyncOffice(){
		return officeDao.findSyncOffice();
	}
	@Transactional(readOnly = false)
	public void save(Office office) {
		office.setParent(this.get(office.getParent().getId()));
		Long requestOfficeId = office.getId();
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		officeDao.save(list);
		if(requestOfficeId==null) {
			
			Long saveOfficeId = office.getId();
			String[] sql = new String[10];
			sql[0] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('产品编号',1000,1000,NULL,4,"+saveOfficeId+",1);";
			sql[1] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('订单编号',1,1,'2014-04-21',3,"+saveOfficeId+",2);";
			sql[2] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('团号',1,1,NULL,5,"+saveOfficeId+",3);";
			sql[3] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('A',1,1,NULL,3,"+saveOfficeId+",4);";
			sql[4] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('B',1,1,NULL,3,"+saveOfficeId+",4);";
			sql[5] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('C',1,1,NULL,3,"+saveOfficeId+",4);";
			sql[6] = "insert into agentinfo (agentName, supplyId, supplyName, is_synchronize) values ('思锐创途'," + office.getId() + ",'" + office.getName() + "',1);";
			// 订单跟踪
			sql[7] = "INSERT INTO order_tracking_setting VALUES(null, " + saveOfficeId + ", 1, 3, 0, 10, 3, 10, 20, 3, 20, null, 1, now(), 1, now(), 0);";
			sql[8] = "INSERT INTO order_tracking_setting VALUES(null, " + saveOfficeId + ", 2, 3, 0, 10, 3, 10, 20, 3, 20, null, 1, now(), 1, now(), 0);";
			sql[9] = "INSERT INTO order_tracking_setting VALUES(null, " + saveOfficeId + ", 3, 3, 0, 10, 3, 10, 20, 3, 20, null, 1, now(), 1, now(), 0);";
			this.getJdbcTemplate().batchUpdate(sql);

			//如果新添加批发商，则需要同时添加批发商顶级部门
			departmentService.addParent(office);
			// 如果新添加批发商，则添加该批发商的quauq的默认费率记录 yudong.xu 2016.8.30
			quauqServiceFeeService.saveAllTypeCompanyRate(office.getUuid());
			officeDao.save(office);
		}
	}
	
	@Transactional(readOnly = false)
	public Office saveOffice(Office office) {
		office.setParent(this.get(office.getParent().getId()));
		Long requestOfficeId = office.getId();
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		officeDao.save(list);
		Office back = new Office();
		if(requestOfficeId==null) {
			
			Long saveOfficeId = office.getId();
			String[] sql = new String[7];
			sql[0] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('产品编号',1000,1000,NULL,4,"+saveOfficeId+",1);";
			sql[1] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('订单编号',1,1,'2014-04-21',3,"+saveOfficeId+",2);";
			sql[2] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('团号',1,1,NULL,5,"+saveOfficeId+",3);";
			sql[3] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('A',1,1,NULL,3,"+saveOfficeId+",4);";
			sql[4] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('B',1,1,NULL,3,"+saveOfficeId+",4);";
			sql[5] = "insert into sysincrease (codeName,codeNum,version,dateMark,numLen,proCompanyId,codeType) values ('C',1,1,NULL,3,"+saveOfficeId+",4);";
			sql[6] = "insert into agentinfo (agentName, supplyId, supplyName, is_synchronize) values ('思锐创途'," + office.getId() + ",'" + office.getName() + "',1);";
			this.getJdbcTemplate().batchUpdate(sql);
			
			//如果新添加供应商，则需要同时添加供应商顶级部门
			departmentService.addParent(office);
			// 如果新添加批发商，则添加该批发商的quauq的默认费率记录 yudong.xu 2016.8.30
			quauqServiceFeeService.saveAllTypeCompanyRate(office.getUuid());
			back = officeDao.save(office);
			logger.info("批发商信息已更新。操作人："+UserUtils.getUser().getId()+"; 批发商ID："+back.getId()+"; 操作时间："+new Date());
		}
		return back;
	}
	
	/**
	 * 删除批发商绑定，只删除产品关系表
	 * @param id
	 */
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void unbound(Long id){
		officeDao.unboundOffice(id);
	}
	
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void delete(Long id) throws Exception {
		List<Office> list = findRelatedOffices(id);
		if(list.size() != 1){
			throw new IllegalArgumentException("删除节点失败，有子节点未删除，或无此节点");
		}else{
			Office office = list.get(0);
			officeDao.deleteById(office.getId());
		}
	}
	public List<Office> findRelatedOffices(Long id){
		List<Office> list = officeDao.findRelatedOffice(id, "%," + id + ",%");
		return list;
	}
	public List<Office> findLikeDomainName(String domainName) {
		return officeDao.findDomainName("%"+domainName+"%");
	}

	/**
	 * 根据批发商ID取得银行账户
	 * @param id
	 * @return
	 */
	public List<String> getOfficePlatBankInfo(Long Id){
		return officeDao.getOfficePlatBankInfo(Id);
	}
	/**
	 * 根据批发商ID取得支付宝账户
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getOfficeZhifubaoInfo(Long Id){
		return zhifubaoDao.getOfficeZhifubaoInfo(Id);
	}
	public List<String> getOfficePlatBankInfoById(Long id){
		return officeDao.getOfficePlatBankInfoById(id);
	}
	/**
	 * 根据批发商ID删除银行账户
	 * @param id
	 * @return
	 */
	public boolean deleteOfficePlatBankInfo(Long id) {
		return officeDao.deleteOfficePlatBankInfo(id);
	}
	/**
	 * 根据批发商ID删除支付宝账户
	 * @param id
	 * @return
	 */
	public boolean deleteOfficeZhifubaoInfo(Long id) {
		return zhifubaoDao.deleteOfficeZhifubaoInfo(id);
	}

	public List<String> getOfficePlatBankInfo(long id, String bankName) {
		// TODO Auto-generated method stub
		return officeDao.getOfficePlatBankInfo(id,bankName);
	}
	/**
	 * 保存
	 * @author gao
	 * @param view
	 * @return
	 */
	public SysCompanyDictView addSysCompanyDictView(SysCompanyDictView view) {
		sysCompanyDictViewDao.saveObj(view);
		return null;
	}

	/**
	 * 保存联系人
	 * @author gao
	 * @param contacts
	 * @return
	 */
	public SupplyContacts addSupplyContacts(SupplyContacts contacts) {
		SupplyContacts supplyContacts = supplyContactsDao.save(contacts);
		return supplyContacts;
	}

	/**
	 * 按照平台商ID查询
	 * @author gao
	 * @param id
	 * @return
	 */
	public Office findWholeOfficeById(Long id) {
		Office back = officeDao.findOne(id);
		if(back!=null){
			return back;
		}
		return null;
	}
	
	/**
	 * 按照平台商uuid查询
	 * @param id
	 * @return
	 */
	public Office findWholeOfficeByUuid(String uuid) {
		if (StringUtils.isNotBlank(uuid)) {
			Office back = officeDao.findByUuid(uuid);
			if(back != null){
				return back;
			}
		}
		return null;
	}

	
//	public void updateWholeOffice(Office Office) {
//		officeDao.updateObj(Office);
//	}

	/**
	 * 按照平台商ID删除
	 * @author gao
	 * @param id
	 */
	public void delWholeOffice(Long id) {
		officeDao.delOffice(id);
		logger.info("平台商信息已删除。操作人："+UserUtils.getUser().getId()+"; 平台商ID："+id+"; 操作时间："+new Date());
	}
	
	/**
	 *	修改批发商下载excel的时间
	 * @param date
	 * @param id
	 */
	public void updateOffice(Date date,Long id){
		officeDao.addExportTime(date, id);
		logger.info("批发商修改下载时间："+UserUtils.getUser().getId()+"; 平台商ID："+id+"; 操作时间："+new Date());
	}
	/**
	 * 按照 批发商编码/名称、公司、上级节点、等级、状态、创建时间、类型  查询 分页
	 * 和类型表关联
	 * @author gao
	 * @param form
	 * @param page
	 * @return
	 */
	public Page<Office> findOfficePage(OfficeSelectForm form, Page<Office> page) {
		String hql = findOfficePageHql(form);
		Page<Office> backpage = officeDao.find(page, hql);
		return backpage;
	}
	/**
	 * 按照 批发商编码/名称、公司、上级节点、等级、状态、创建时间、类型  查询 总量
	 * 和类型表关联
	 * @author gao
	 * @param form
	 * @return
	 */
	public Integer findOfficePageNum(OfficeSelectForm form){
		String hql = findOfficePageHql(form);
		List<Office> back = officeDao.find(hql);
		if(back!=null && !back.isEmpty()){
			return back.size();
		}else{
			return 0;
		}
	}
	
	/**
	 * 按照 批发商编码/名称、公司、上级节点、等级、状态、创建时间、类型  查询 分页
	 * 没有类型查询，不和类型表关联
	 * @author gao
	 * @param form
	 * @param page
	 * @return
	 */
	public Page<Office> findOfficePageWithoutTypePage(OfficeSelectForm form, Page<Office> page) {
		String hql = findOfficePageWithoutTypeHql(form);
		Page<Office> backpage = officeDao.find(page, hql);
		return backpage;
	}

	/**
	 * 按照 批发商编码/名称、公司、上级节点、等级、状态、创建时间查询 的总量
	 * 没有类型查询，不和类型表关联
	 * @author gao
	 * @param form
	 * @return
	 */
	public Integer findOfficePageWithoutTypeNum(OfficeSelectForm form){
		String hql = findOfficePageWithoutTypeHql(form);
		List<Office> back = officeDao.find(hql);
		if(back!=null && !back.isEmpty()){
			return back.size();
		}else{
			return 0;
		}
	}
	
	/**
	 * 按照 批发商编码/名称、公司、上级节点、等级、状态、创建时间、类型  查询 的hql语句
	 * 和类型表关联
	 * @author gao
	 * @param form
	 * @return
	 */
	private String findOfficePageHql(OfficeSelectForm form){
		StringBuffer hql = new StringBuffer("from Office c,WholeOfficeType w where w.companyID = c.id ");
		// 正常状态
		hql.append(" and c.delFlag = 0");
		// 批发商类型匹配
		hql.append(" and  w.sysdefinedictUUID = '"+form.getTypevalue()+"'");
		// 模糊查询匹配
		if(StringUtils.isNotBlank(form.getConn())){
			hql.append(" and (c.name like '%"+form.getConn()+"%' or c.code like '%"+form.getConn()+"%')");
		}
		// 公司名称匹配
		if(StringUtils.isNotBlank(form.getCompanyName())){
			hql.append(" and c.companyName = '"+form.getCompanyName()+"'");
		}
		// 上级节点匹配
		if(StringUtils.isNotBlank(form.getParentName())){
			hql.append(" and c.parent.id = "+form.getParentName());
		}
		// 批发商等级匹配
		if(StringUtils.isNotBlank(form.getLevelvalue())){
			hql.append(" and c.level.uuid ='"+form.getLevelvalue()+"'");
		}
		// 状态匹配
		if(form.getStatus()!=null){
			hql.append(" and c.status = "+form.getStatus());
		}
		// 创建时间匹配
		if(StringUtils.isNotBlank(form.getStartDate())){
			hql.append(" and c.createDate >= '"+form.getStartDate()+"'");
		}
		// 创建时间匹配
		if(StringUtils.isNotBlank(form.getEndDate())){
			hql.append(" and c.createDate <= '"+form.getEndDate()+"'");
		}
		// 国内国外
		if(form.getFrontier()!=null){
			hql.append(" and c.frontier= "+form.getFrontier());
		}
		// 排序方式
		if(form.getOrderby()==OfficeSelectForm.ORDER_CREATE){ // 创建时间
			hql.append(" order by c.createDate desc ");
		}else if(form.getOrderby()==OfficeSelectForm.ORDER_UPDATE){ // 修改时间
			hql.append(" order by c.updateDate desc ");
		}
		return hql.toString();
	}
	
	/**
	 * 按照 批发商编码/名称、公司、上级节点、等级、状态、创建时间查询 的hql语句
	 * 没有类型查询，不和类型表关联
	 * 	@author gao
	 * @param form
	 * @return
	 */
	private String findOfficePageWithoutTypeHql(OfficeSelectForm form){
		StringBuffer hql = new StringBuffer("from Office c where 1=1");
		// 正常状态
		hql.append(" and c.delFlag = 0");
		// 模糊查询匹配
		if(StringUtils.isNotBlank(form.getConn())){
			hql.append(" and (c.name like '%"+form.getConn()+"%' or c.code like '%"+form.getConn()+"%')");
		}
		// 公司名称匹配
		if(StringUtils.isNotBlank(form.getCompanyName())){
			hql.append(" and c.companyName = '"+form.getCompanyName()+"'");
		}
		// 上级节点匹配
		if(StringUtils.isNotBlank(form.getParentName())){
			hql.append(" and c.parent.id = "+form.getParentName());
		}
		// 批发商等级匹配
		if(StringUtils.isNotBlank(form.getLevelvalue())){
			hql.append(" and c.level.uuid ='"+form.getLevelvalue()+"'");
		}
		// 状态匹配
		if(form.getStatus()!=null){
			hql.append(" and c.status = "+form.getStatus());
		}
		// 创建时间匹配
		if(StringUtils.isNotBlank(form.getStartDate())){
			hql.append(" and c.createDate >= '"+form.getStartDate()+"'");
		}
		// 创建时间匹配
		if(StringUtils.isNotBlank(form.getEndDate())){
			hql.append(" and c.createDate <= '"+form.getEndDate()+"'");
		}
		// 国内国外
		if(form.getFrontier()!=null){
			hql.append(" and c.frontier= "+form.getFrontier());
		}
		// 排序方式
		if(form.getOrderby()==OfficeSelectForm.ORDER_CREATE){ // 创建时间
			hql.append(" order by c.createDate desc ");
		}else if(form.getOrderby()==OfficeSelectForm.ORDER_UPDATE){ // 修改时间
			hql.append(" order by c.updateDate desc ");
		}
		return hql.toString();
	}
	
	/**
	 * 插入境外银行账户
	 * 
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 * @param string6
	 * @param parseLong
	 * @param string7
	 * @param string8
	 * @param string9
	 */
	@Transactional(readOnly = false)
	public boolean insertOfficePlatBankInfo(String defaultFlag, String accountName, String bankName, String bankAddr,String bankAccountCode, 
			String remarks, long beLongPlatId, String rounting, String swiftNum, String phoneNum, String belongType) {
		return officeDao.insertOfficePlatBankInfo(defaultFlag, accountName, bankName, bankAddr, bankAccountCode,
				remarks, beLongPlatId, rounting, swiftNum, phoneNum, belongType);
	}


	/**
	 * 插入境内银行账户
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public boolean insertOfficePlatBankInfo(String defaultFlag, String accountName, String bankName, String bankAddr,
				String bankAccountCode, String remarks, Long beLongPlatId, String belongType) {
			return officeDao.insertOfficePlatBankInfo(defaultFlag, accountName, bankName, bankAddr, bankAccountCode,
					remarks, beLongPlatId, belongType);
	}
	
	/**
	 * 插入支付宝账户
	 * @param defaultFlag
	 * @param accountName
	 * @param bankAccountCode
	 * @param remarks
	 * @param beLongPlatId
	 * @param belongType
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean insertOfficeZhifubaoInfo(String defaultFlag, String name, String account, String remark, Long companyId) {
		return zhifubaoDao.insertOfficeZhifubaoInfo(defaultFlag, name, account, remark, companyId);
	}
	
	/**
	 * 获取被启用的批发商
	 * @param shelfRightsStatus
	 * @return
	 */
	public List<Office> findByShelfRightsStatus(Integer shelfRightsStatus) {
		return officeDao.findByShelfRightsStatus(shelfRightsStatus);
	}

	/**
	 * 获取被启用的批发商
	 * 顺序为：T1上架产品最多的排在最前面，降序排列；
	 * @return
	 */
	public List<Office> getOffice4T1(String type) {
		return officeDao.getOffice4T1(type);
	}
	
	
}
