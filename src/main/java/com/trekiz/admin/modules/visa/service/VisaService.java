package com.trekiz.admin.modules.visa.service;

import java.util.*;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.log.service.LogOrderService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.visa.entity.Activityvisafile;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.Visabasics;
import com.trekiz.admin.modules.visa.entity.Visafile;
import com.trekiz.admin.modules.visa.entity.Visapersonneltype;
import com.trekiz.admin.modules.visa.repository.ActivityvisafileDao;
import com.trekiz.admin.modules.visa.repository.VisaBasicsDao;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaPersonnelDao;
import com.trekiz.admin.modules.visa.repository.VisafileDao;


@Service
@Transactional(readOnly = true)
public class VisaService  extends BaseService {
    
    @Autowired
   private VisaBasicsDao visaBasicsDao;
    
    @Autowired
    private VisaPersonnelDao visaPersonnelDao;
    
    @Autowired
    private DocInfoDao docInfoDao;
    
    @Autowired
    private ActivityvisafileDao activityvisafileDao;
    
    @Autowired
	private DocInfoService docInfoService;

	@Autowired
	private VisafileDao visafileDao;
	
	@Autowired
	private VisaDao visaDao;

	@Autowired
	private LogOrderService logOrderService;

    public Visabasics getVisaBasicsByCountryId(int visaCountryId){
       List<Visabasics> list = visaBasicsDao.findByVisaCountry(visaCountryId);
       if(list!=null&&list.size()>0){
           return list.get(0);
       }
       return null;
   }
    
    public List<Visapersonneltype> getVisaPersonelByBasicsId(long basicId){
        return visaPersonnelDao.findByBasicId(basicId);
    }
    
    public void saveVisaBasic(Visabasics visabasics){
        visaBasicsDao.save(visabasics);
    }
    public void saveVisapersonneltype(Visapersonneltype visapersonneltype){
        visaPersonnelDao.save(visapersonneltype);
    }
    
    public Set<Country> getAllCountry(){
        List<Activityvisafile> activityVisaFiles= activityvisafileDao.findAllList();
        Set<Country> set = new HashSet<Country>();
        for(Activityvisafile file :activityVisaFiles){
            Country country = CountryUtils.getCountry(file.getCountryId());
            set.add(country);
        }
        return set;        
    }
    
    public List<Activityvisafile> findByVisaFileByCountry(Long countryId){
       return  activityvisafileDao.findByVisaFileByCountry(countryId);
    }
    
    public List<DocInfo> findByVisaFileByDocInfoId(Long docInfoId){
        return  activityvisafileDao.findByDocInfoByDocInfoId(docInfoId);
     }
    
    public List<Activityvisafile> findByVisaFileByCountry(String countryIds){
        List<Long> list = new ArrayList<Long>();
        
        if(StringUtils.isNotBlank(countryIds)){
            String[] cs = countryIds.split(";") ;
            for(String csone : cs){
                if(StringUtils.isNotBlank(csone)){
                    list.add(Long.parseLong(csone));
                }
            }
        }
        if(list.size()<=0){
            return null;
        }
        return  activityvisafileDao.findByVisaFileByCountrys(list);
    }
    
    
     /**
     *  功能:
     *          保存上传信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 下午8:29:30
     */
    @Transactional
    public void saveUploadData(String selectcountry,String radiovisaType,String fileName,String path){
        DocInfo docInfo = new DocInfo();
        docInfo.setDocName(fileName);
        docInfo.setDocPath(path);
        docInfoDao.save(docInfo);
        Activityvisafile avisaFile = new Activityvisafile();
        avisaFile.setSrcDocId(docInfo.getId());
        avisaFile.setCountryId(Long.parseLong(selectcountry));
        avisaFile.setVisaType(Integer.parseInt(radiovisaType));
        avisaFile.setCountryName(CountryUtils.getCountry(Long.parseLong(selectcountry)).getCountryName_cn());
        activityvisafileDao.save(avisaFile);
    }
    
    /**
     *  功能:
     *          保存产品签证信息
     *  @author xuziqian
     *  @DateTime 2014-1-14 下午8:29:30
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void saveActivityVisaData(String selectcountry,String radiovisaType,String fileName,String path,Long activityId){
        DocInfo docInfo = new DocInfo();
        docInfo.setDocName(fileName);
        docInfo.setDocPath(path+fileName);
        docInfoDao.save(docInfo);
        Activityvisafile avisaFile = new Activityvisafile();
        avisaFile.setSrcActivityId(activityId);
        avisaFile.setSrcDocId(docInfo.getId());
        avisaFile.setCountryId(Long.parseLong(selectcountry));
        avisaFile.setVisaType(Integer.parseInt(radiovisaType));
        avisaFile.setCountryName(CountryUtils.getCountry(Long.parseLong(selectcountry)).getCountryName_cn());
        activityvisafileDao.save(avisaFile);
    }
    
    /**
     * 上传签证
     *     
     * 创建人：Administrator   
     * 创建时间：2014-1-27 下午4:30:13   
     * 修改人：Administrator   
     * 修改时间：2014-1-27 下午4:30:13   
     * @version    
     *
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void uploadActivityVisaData(String selectcountry,String radiovisaType,String fileName,String path,Long activityId){
    	DocInfo docInfo = new DocInfo();
        docInfo.setDocName(fileName);
        docInfo.setDocPath(path);
        docInfoDao.save(docInfo);
        Activityvisafile avisaFile = new Activityvisafile();
        avisaFile.setSrcActivityId(activityId);
        avisaFile.setSrcDocId(docInfo.getId());
        avisaFile.setCountryId(Long.parseLong(selectcountry));
        avisaFile.setVisaType(Integer.parseInt(radiovisaType));
        avisaFile.setCountryName(CountryUtils.getCountry(Long.parseLong(selectcountry)).getCountryName_cn());
        activityvisafileDao.save(avisaFile);
    }
    
    /**
     * 操作产品签证文件
     * @author jiachen
     * @DateTime 2015年1月9日 下午5:53:30
     * @return void
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void uploadActivityVisaData(String selectcountry,String radiovisaType,String[] docInfoIdArr,Long activityId){
    	
    	//如果是修改操作，则需要先获取旧文件列表
    	List<Activityvisafile> oldFileList = findVisaFileByProid(activityId);
    	List<String> visaOldFlies = new ArrayList<String>();
    	if(!oldFileList.isEmpty()) {
    		for(Activityvisafile file : oldFileList) {
    			visaOldFlies.add(file.getSrcDocId().toString());
    		}
    	}
    	
    	//将前台提交的文件id封装到list中
    	List<String> docInfoIdList = new ArrayList<String>();
    	if(null != docInfoIdArr) {
    		docInfoIdList = Arrays.asList(docInfoIdArr);
    	}
    	
		//如果新组中有不包含旧组的值，说明是删除的
		for(String oldFiles : visaOldFlies) {
			if(!docInfoIdList.contains(oldFiles)) {
				activityvisafileDao.deleteById(StringUtils.toLong(oldFiles));
			}
		}
		//如果旧组中有不包含新组的值，说明是新增的
		for(String newFiles : docInfoIdList) {
			if(!oldFileList.contains(newFiles)) {
				Activityvisafile avisaFile = new Activityvisafile();
				avisaFile.setSrcActivityId(activityId);
    			avisaFile.setSrcDocId(StringUtils.toLong(newFiles));
    			avisaFile.setCountryId(Long.parseLong(selectcountry));
    			avisaFile.setVisaType(Integer.parseInt(radiovisaType));
    			avisaFile.setCountryName(CountryUtils.getCountry(Long.parseLong(selectcountry)).getCountryName_cn());
    			activityvisafileDao.save(avisaFile);
			}
		}
    }
    
    /**
     * 操作产品签证文件
     * @author jiachen
     * @DateTime 2015年1月9日 下午5:53:30
     * @return void
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void uploadActivityVisaData4Save(String selectcountry,String radiovisaType,String docId,String[] docInfoIdArr,Long activityId){
    	
    	//如果是修改操作，则需要先获取旧文件列表
    	List<Activityvisafile> oldFileList = findVisaFileByProid(activityId);
    	List<String> visaOldFlies = new ArrayList<String>();
    	if(!oldFileList.isEmpty()) {
    		for(Activityvisafile file : oldFileList) {
    			visaOldFlies.add(file.getSrcDocId().toString());
    		}
    	}
    	
    	//将前台提交的文件id封装到list中
    	List<String> docInfoIdList = new ArrayList<String>();
    	if(null != docInfoIdArr) {
    		docInfoIdList = Arrays.asList(docInfoIdArr);
    	}
    	
		//如果新组中有不包含旧组的值，说明是删除的
		for(String oldFiles : visaOldFlies) {
			if(!docInfoIdList.contains(oldFiles)) {
				activityvisafileDao.deleteById(StringUtils.toLong(oldFiles));
			}
		}
		//如果旧组中有不包含新组的值，说明是新增的
//		for(String newFiles : docInfoIdList) {
//			if(!oldFileList.contains(newFiles)) {
				Activityvisafile avisaFile = new Activityvisafile();
				avisaFile.setSrcActivityId(activityId);
    			avisaFile.setSrcDocId(StringUtils.toLong(docId));
    			avisaFile.setCountryId(Long.parseLong(selectcountry));
    			avisaFile.setVisaType(Integer.parseInt(radiovisaType));
    			avisaFile.setCountryName(CountryUtils.getCountry(Long.parseLong(selectcountry)).getCountryName_cn());
    			activityvisafileDao.save(avisaFile);
//			}
//		}
    }
    
    /**
     * 查询产品的签证信息
     * @param proId
     * @return
     */
    public List<Activityvisafile> findVisaFileByProid(Long proId) {
    	return activityvisafileDao.findVisaFileByProid(proId);
    }
    
    /**
     * 删除产品的签证信息
     * @param proId
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void delVisaFileByProid(Long proId){
    	activityvisafileDao.delVisaFileByProid(proId);
    }
    
    /**
     * 查询产品的签证
     * @param proId
     * @return
     */
    public List<Object[]> findVisas(List<Long> proIds){
		return activityvisafileDao.findVisas(proIds);
	}
    
    /**
     * 根据产品的签证查询查询文件
     * @param proId
     * @return
     */
    public List<DocInfo> findVisaFiles(String activityId, String visaType, String countryId){
    	if(StringUtils.isNotBlank(activityId) && StringUtils.isNotBlank(visaType) && StringUtils.isNotBlank(countryId)) {
    		return activityvisafileDao.findFileList(StringUtils.toLong(activityId), StringUtils.toInteger(visaType), StringUtils.toLong(countryId));
    	}else{
    		return new ArrayList<DocInfo>();	
    	}
	}
    
    /**
     * 根据签证ID集合删除产品的签证信息
     * @param proId
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void delVisaFileByIds(Set<Long> ids){
    	activityvisafileDao.delVisaFileByIds(ids);
    }
    
    /**
     * 根据签证ID删除产品的签证信息
     * @param proId
     */
    @Transactional(readOnly = false,rollbackFor={Exception.class})
    public void delVisaFileById(Long id){
    	activityvisafileDao.deleteById(id);
    }

	
	/**
	 * 签证附件保存
	 * @param files
	 */
	public Iterable<Visafile> visaFile(MultipartFile[] files,Long visaId){
		ArrayList<DocInfo> infoList = new ArrayList<DocInfo>();
		Iterable<Visafile> backVisaFile = new ArrayList<Visafile>();
		if(files!=null && files.length>0){
			for(MultipartFile file:files){
				if(file!=null){
					DocInfo info = new DocInfo();
					try{
						String fileName = file.getOriginalFilename();
						if(StringUtils.isNotBlank(fileName)){
							String path = FileUtils.uploadFile(file.getInputStream(), fileName);
							info.setDocName(fileName);
							info.setDocPath(path);
							infoList.add(info);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			// 批量保存附件类
			Iterable<DocInfo> saveDocInfoList = new ArrayList<DocInfo>();
			saveDocInfoList = docInfoService.saveDocInfoList(infoList);
			if(saveDocInfoList!=null && saveDocInfoList.iterator().hasNext()){
				// 将附件类中的数据和签证附件类关联
				ArrayList<Visafile> visafileList = new ArrayList<Visafile>();
				for(DocInfo info:saveDocInfoList){
					Visafile file = new Visafile();
					file.setFileName(info.getDocName());// 附件名称放入签证附件表中
					file.setFileTableId(info.getId());// 附件ID放入签证附件表
					file.setVisaId(visaId);// 签证ID放入签证附件表
					visafileList.add(file);
				}
				// 批量保存签证附件类
				if(visafileList!=null && visafileList.size()>0){
					try{
						backVisaFile = visafileDao.save(visafileList);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}
		return backVisaFile;
	}
	/**
	 * 保存签证类
	 * @param visa
	 * @return
	 */
	public Visa saveVisa(Visa visa){
		Visa backVisa = null;
		if(visa!=null){
			backVisa = visaDao.save(visa);
		}
		return backVisa;
	}
	
	@SuppressWarnings("unchecked")
	public Visa findVisaByTravlerId(Long travelerId){
		Visa visa = null;
		if(travelerId!=null){
			String hql  = "from Visa where travelerId = ?";
			List<Visa> list = visaDao.getSession().createQuery(hql)
				.setParameter(0, travelerId)
				.list();
			if(list!=null && list.size()>0){
				visa = list.get(0);
			}
		}
		return visa;

	}
	
	/**
	 * 
	* @Title: saveAsAcount 
	* @Description: TODO(财务押金确认收款功能) 
	* @param @param id
	* @return Visa    返回类型 
	* @throws
	 */
//	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
//    public Visa saveAsAcount(Long id) {
//		Visa visa = visaDao.findOne(id);
//		visa.setIsAccounted(Integer.parseInt(Context.DICT_TYPE_YES));
//		visaDao.save(visa);
//        return visa;
//	}
	
	/**
	 * 查找可以申请押金转担保的游客
	 * @author jiachen
	 * @DateTime 2014-12-8 上午11:27:52
	 * @return List<Object[]>([游客id,押金金额])
	 */
	public List<Object[]> findTravelerVisaByOrderId(String orderId) {
		List<Object[]> list = new ArrayList<Object[]>();
		if(StringUtils.isNotBlank(orderId)) {
			list = visaDao.findTravelerVisaByOrderId(StringUtils.toLong(orderId));
		}
		return list;
	}
	
	/**
	 * 通过交押金游客id查找游客信息
	 * @author jiachen
	 * @DateTime 2014-12-9 下午09:19:41
	 * @return List<Object[]>
	 */
	public List<Object[]> findTravelerVisaByTravelerId(String travelerId) {
		List<Object[]> list = new ArrayList<Object[]>();
		if(StringUtils.isNotBlank(travelerId)) {
			list = visaDao.findTravelerVisaByTravelerId(StringUtils.toLong(travelerId));
		}
		return list;
	}
	
	/**
	 * 查找可以申请退签证押金的游客列表
	 * @author jiachen
	 * @DateTime 2014-12-8 上午11:27:52
	 * @return List<Object[]>([游客id,应收金额,达帐金额])
	 */
	public List<Object[]> findTravelerDepositRefundVisaByOrderId(String orderId) {
		List<Object[]> list = new ArrayList<Object[]>();
		if(StringUtils.isNotBlank(orderId)) {
			list = visaDao.findTravelerDepositRefundVisaByOrderId(StringUtils.toLong(orderId));
		}
		return list;
	}
	
	/**
	 * 查找可以申请退签证押金的游客列表
	 * @author jiachen
	 * @DateTime 2015-3-2 上午11:12:52
	 * @return List<Object[]>([游客id,应收金额,达帐金额,订单编号])
	 */
	public List<Object[]> findTravelerDepositRefundVisaByTravelerIds(String[] travelerIdArr) {
		List<Object[]> list = new ArrayList<Object[]>();
		List<Long> travelerIdList = new ArrayList<Long>();
		for(String travelerId : travelerIdArr) {
			travelerIdList.add(StringUtils.toLong(travelerId));
		}
		if(!travelerIdList.isEmpty()) {
			list = visaDao.findTravelerDepositRefundVisaByTravelerIds(travelerIdList);
		}
		return list;
	}
	
	/**
	 * 查找可以申请签证退款的游客列表
	 * @author jiachen
	 * @DateTime 2014-12-8 上午11:27:52
	 * @return List<Object[]>([游客id,应收金额,达帐金额])
	 */
	public List<Object[]> findTravelerRefundVisaByOrderId(String orderId) {
		List<Object[]> list = new ArrayList<Object[]>();
		if(StringUtils.isNotBlank(orderId)) {
			list = visaDao.findTravelerRefundVisaByOrderId(StringUtils.toLong(orderId));
		}
		return list;
	}
	
	/**
	 * 签证订单修改页面，修改签证信息
	 * @param visa
	 */
	public void updateVisaOrder(Visa visa, String orderId){
		if(visa!=null && visa.getId()!=null){
			logOrderService.saveLogOrder4Visa(visa, orderId);

			String hql = "update Visa set visaStauts = ?,startOut = ?,contract = ?, AACode=?, makeTable=?,signOriginalProjectType=?,signCopyProjectType=?,guaranteeStatus=?,passportOperateRemark=?,UIDCode=?  where id = ?";
			visaDao.getSession().createQuery(hql)
				.setParameter(0, visa.getVisaStauts())
				.setParameter(1, visa.getStartOut())
				.setParameter(2, visa.getContract())
				.setParameter(3, visa.getAACode())
				.setParameter(4, visa.getMakeTable())
				.setParameter(5, visa.getSignOriginalProjectType())
				.setParameter(6, visa.getSignCopyProjectType())
				.setParameter(7, visa.getGuaranteeStatus())
				.setParameter(8, visa.getPassportOperateRemark())
				.setParameter(9, visa.getUIDCode())
				.setParameter(10, visa.getId())
				.executeUpdate();
		}else{
			visaDao.getSession().save(visa);
//			System.out.println(visaDao.getSession().save(visa));
		}
	}
	
	/**
	 * 查找参团所需的游客信息
	 * @author jiachen
	 * @DateTime 2014-12-12 下午03:25:13
	 * @return List<Object[]>
	 */
	public List<Object[]> findJoinGroupTravelerVisa(String orderId) {
		List<Object[]> list = null;
		if(StringUtils.isNotBlank(orderId)) {
			list = visaDao.findJoinGroupTravelerVisa(StringUtils.toLong(orderId), Context.ORDER_TYPE_QZ);
		}
		return list;
	}
	
	/**
	 * 查找单个游客的参团信息
	 * @author jiachen
	 * @DateTime 2015年3月13日 下午3:19:40
	 * @return List<Object[]>
	 */
	public List<Object[]> findJoinGroupByTravelerId(String travelerId) {
		List<Object[]> list = null;
		if(StringUtils.isNotBlank(travelerId)) {
			list = visaDao.findJoinGroupByTravelerId(StringUtils.toLong(travelerId));
		}
		return list;
	}
	
	/**
	 * 查找参团单个游客信息
	 * @author wuqiang
	 * @DateTime 2014-12-12 下午03:25:13
	 * @return List<Object[]>
	 */
	public List<Object[]> findJoinGroupTravelerVisaForOne(String orderId,Long travelerId) {
		List<Object[]> list = null;
		if(StringUtils.isNotBlank(orderId)) {
			list = visaDao.findJoinGroupTravelerVisaForOne(StringUtils.toLong(orderId), Context.ORDER_TYPE_QZ,travelerId);
		}
		return list;
	}
	
	/**
	 * 根据游客ID删除VISA
	 * @param travelerId
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void delVisaByTravelerId(Long travelerId){
		visaDao.delVisaByTravelerId(travelerId);
	}

	/**
	 * 签证押金转担保列表页
	 * @return
     */
	public Page<Map<Object, Object>> getReviewList(Page<Map<Object, Object>> page,Map<String,String> mapRequest)
	{
        Long userId = UserUtils.getUser().getId();
        //全部
		StringBuffer stringBuffer0 = new StringBuffer();
		//待本人审核
		StringBuffer stringBuffer1 = new StringBuffer();
		//本人已审批
		StringBuffer stringBuffer2 = new StringBuffer();
		//非本人审核
		StringBuffer stringBuffer3 = new StringBuffer();
		//查询条件
		StringBuffer stringBufferCondition = new StringBuffer();
	
        //待本人审核开始
    	stringBuffer1.append("SELECT rn.order_no,rn.group_code,rn.product_name,rn.create_date,rn.create_by,rn.agent, ");
    	stringBuffer1.append("rn.order_creator_name,rn.traveller_name,rn.extend_3,rn.extend_4,rn.status,rn.last_reviewer ");
    	stringBuffer1.append("FROM review_new rn WHERE FIND_IN_SET('");
    	stringBuffer1.append(userId);
    	stringBuffer1.append("',rn.current_reviewer ) AND rn.product_type='6' ");
    	stringBuffer1.append("AND rn.process_type='6' AND rn.del_flag=0 AND rn.status=1 ");
    	 //待本人审核结束
    	
    	 //本人已审批开始
    	stringBuffer2.append("SELECT rn.order_no,rn.group_code,rn.product_name,rn.create_date,rn.create_by,rn.agent, ");
    	stringBuffer2.append("rn.order_creator_name,rn.traveller_name,rn.extend_3,rn.extend_4,rn.status,rn.last_reviewer ");
    	stringBuffer2.append("FROM review_new rn  LEFT JOIN review_log_new rln ON rn.id= rln.review_id  ");
    	stringBuffer2.append("WHERE rln.create_by ='");
    	stringBuffer2.append(userId);
    	stringBuffer2.append("' AND rln.active_flag=1 AND rln.operation!=4 AND rln.operation!=3  AND rn.process_type='6' ");
    	 //本人已审批结束
    	
    	 //非本人审核开始
    	stringBuffer3.append("SELECT tmp.order_no,tmp.group_code,tmp.product_name,tmp.create_date,tmp.create_by,tmp.agent, ");
    	stringBuffer3.append("tmp.order_creator_name,tmp.traveller_name,tmp.extend_3,tmp.extend_4,tmp.status,tmp.last_reviewer ");
    	stringBuffer3.append("FROM sys_user_review_process_permission surpp  LEFT JOIN ");
    	stringBuffer3.append("( SELECT * FROM review_new rn WHERE rn.id NOT IN ");
    	stringBuffer3.append("(SELECT rn.id FROM review_new rn WHERE FIND_IN_SET('321',rn.all_reviewer AND  rn.process_type='6' AND rn.product_type='6')) ");
    	stringBuffer3.append("AND rn.process_type='6' AND rn.product_type='6')tmp ");
    	stringBuffer3.append("ON surpp.dept_id=tmp.dept_id AND surpp.product_type=tmp.product_type AND surpp.review_flow=tmp.process_type ");
    	stringBuffer3.append("WHERE surpp.product_type=6  AND surpp.review_flow=6 ");
    	 //非本人审核结束
    	//全部开始
    	stringBuffer0.append(stringBuffer1);
    	stringBuffer0.append(" UNION ALL ");
    	stringBuffer0.append(stringBuffer2);
    	stringBuffer0.append(" UNION ALL ");
    	stringBuffer0.append(stringBuffer3);
    	//全部开始结束
    	
    	//处理查询条件
    	serarchCondtion(mapRequest, stringBufferCondition,mapRequest.get("appliType"));
        if("1".equals(mapRequest.get("appliType")))
        	return visaDao.findBySql(page,stringBuffer1.append(stringBufferCondition).toString(),Map.class);
        else if("2".equals(mapRequest.get("appliType")))
        	return visaDao.findBySql(page,stringBuffer2.append(stringBufferCondition).toString(),Map.class);
        else if("3".equals(mapRequest.get("appliType")))
        	return visaDao.findBySql(page,stringBuffer3.append(stringBufferCondition).toString(),Map.class);
        else 
        {
        	StringBuffer buffer = new StringBuffer();
        	buffer.append("SELECT * FROM  ( ");
        	buffer.append(stringBuffer0);
        	buffer.append(") tt   ");
        	buffer.append(stringBufferCondition);
        	return visaDao.findBySql(page,buffer.toString(),Map.class);
        }
        	
	}

/**
 * 游客列表
 * @return
 */
    public List<Map<String,Object>> getTravellerList() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT   rn.traveller_name,rn.traveller_id   ");
        stringBuffer.append("FROM review_new rn WHERE rn.process_type='6' AND rn.product_type='6' and rn.company_id='");
        stringBuffer.append(UserUtils.getUser().getCompany().getUuid());
        stringBuffer.append("' GROUP BY rn.traveller_id");
        return  visaDao.findBySql(stringBuffer.toString(),Map.class);

    }
    
    
    /**
     * 审批发起人列表
     * @return
     */
    public List<Map<String,Object>> getAppStarterList()
    {
    	StringBuffer stringBuffer = new StringBuffer();
    	stringBuffer.append("SELECT su.name appName,tmp.create_by appId FROM sys_user su   JOIN ");
    	stringBuffer.append("(SELECT DISTINCT rn.create_by FROM review_new rn  WHERE rn.process_type='6' AND ");
    	stringBuffer.append("rn.product_type='6' AND rn.company_id='");
    	stringBuffer.append(UserUtils.getUser().getCompany().getId());
    	stringBuffer.append( "') tmp ON su.id =tmp.create_by");
    	return  visaDao.findBySql(stringBuffer.toString(),Map.class);
    }
    
    
    
    /**
	 * 拼接查询条件
	 * @param map
	 * @param sqlBuffer
	 */
	private void serarchCondtion(Map<String, String> map, StringBuffer sqlBuffer,String appliType) {
		
		if(null ==appliType ||appliType.equals("0") || "".equals(appliType))
		sqlBuffer.append(" where 1=1 ");
		if(StringUtils.isNotBlank(map.get("orderNum")))
		{
			sqlBuffer.append(" and  order_no like'%");
			sqlBuffer.append(map.get("orderNum").trim());
			sqlBuffer.append( "%' ");
		}
		if(StringUtils.isNotBlank(map.get("agent")))
		{
			sqlBuffer.append(" and  agent = '");
			sqlBuffer.append(map.get("agent").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("createByName")))  //下单人
		{
			sqlBuffer.append(" and  order_creator_name = '");
			sqlBuffer.append(map.get("createByName").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("timeBegin")))
		{
			sqlBuffer.append(" and create_date" +">='");
			sqlBuffer.append(map.get("timeBegin").trim()+" 00:00:00");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("timeEnd")))
		{
			sqlBuffer.append(" and create_date" +"<='");
			sqlBuffer.append(map.get("timeEnd").trim()+" 23:59:59");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("applyPromoter")))
		{
			sqlBuffer.append(" and  create_by like '");
			sqlBuffer.append(map.get("applyPromoter").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("travellerName")))
		{
			sqlBuffer.append(" and  traveller_name like '");
			sqlBuffer.append(map.get("travellerName").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("status")))
		{
			sqlBuffer.append(" and  status = '");
			sqlBuffer.append(map.get("status").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("paymentType")))
		{
			sqlBuffer.append(" and agent in (select id from agentinfo where paymentType = "+map.get("paymentType")+")");
		}
	}

	public List<Map<String,Object>> findVisasNew(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}
		StringBuffer  stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT t1.srcActivityId,t1.visaType,t1.countryName,t1.countryId,t2.id as docInfoId FROM activityvisafile t1 ,docinfo t2 WHERE t1.srcDocId = t2.id  ");
		stringBuffer.append(" AND t1.delFlag =0 AND t1.srcActivityId IN (");
		String tmp = "";
		for(int i=0;i<ids.size();i++)
		{
			if(i == ids.size()-1)
				tmp = tmp +ids.get(i);
			else
				tmp = tmp +ids.get(i)+",";
		}
		stringBuffer.append(tmp);
		stringBuffer.append(")");
		return visaDao.findBySql(stringBuffer.toString(), Map.class);
	}
     /**
      * 0211需求:
      * 根据visa表的id更新visa表中的字段:forecast_back_date
      * @param travelerVisaId
      */
	public String updateForecastBackDateById(String travelerVisaId,String forecastBackDate) {
		StringBuffer sb=new StringBuffer();
		sb.append(" UPDATE visa v ")
		  .append(" SET v.forecast_back_date = '"+forecastBackDate+"' ")
		  .append(" WHERE v.id ='"+travelerVisaId+"' ");
		try {
			visaDao.updateBySql(sb.toString());
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}

	public void updateGuaranteeStatus(String visaId, String guarantee) {
		String sql = "UPDATE visa set guarantee_status = " + guarantee + " where id = " + visaId;
		visaDao.updateBySql(sql.toString());
	}

	public void updateVisa(Visa visa) {
		visaDao.updateObj(visa);
	}
}
