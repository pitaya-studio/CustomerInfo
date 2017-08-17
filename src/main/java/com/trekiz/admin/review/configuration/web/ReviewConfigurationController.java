package com.trekiz.admin.review.configuration.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewConfigService;
import com.quauq.review.core.engine.ReviewProcessService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewConfig;
import com.quauq.review.core.engine.entity.ReviewProcess;
import com.quauq.review.core.management.ReviewManagementService;
import com.quauq.review.core.utils.TypeConvertUtil;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.SysOfficeProcessType;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.configuration.config.ReviewContext;
import com.trekiz.admin.review.configuration.entity.ReviewCostPaymentConfiguration;
import com.trekiz.admin.review.configuration.model.DepartmentNode;
import com.trekiz.admin.review.configuration.service.ReviewConfigurationService;
import com.trekiz.admin.review.configuration.service.ReviewCostPaymentConfigurationService;
import com.trekiz.admin.review.configuration.util.DepartmentUtil;

/**
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 * <p/>
 * 流程配置页面controller
 *
 * @author zhenxing.yan
 * @date 2015年11月13日
 */
@Controller
@RequestMapping("${adminPath}/sys/review/configuration")
public class ReviewConfigurationController {

    private static Logger logger= LoggerFactory.getLogger(ReviewConfigurationController.class);
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SysOfficeConfigurationService officeConfigurationService;

    @Autowired
    private ReviewProcessService reviewProcessService;

    @Autowired
    private ReviewConfigurationService reviewConfigurationService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ReviewManagementService reviewManagementService;

    @Autowired
    private ReviewConfigService reviewConfigService;

    @Autowired
    private ReviewCostPaymentConfigurationService reviewCostPaymentConfigurationService;

    /**
     * 流程配置列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "index")
    public String index(Model model, @RequestParam(value = "deptId", required = false) String deptId, @RequestParam(value = "productType", required = false) String productType,
    @RequestParam(value = "processType",required = false) String processType) {
        User user = UserUtils.getUser();
        String companyUuid = user.getCompany().getUuid();

        /**
         * 该公司的部门列表、产品类型列表、流程类型列表
         */
        List<Department> departments = departmentService.findByOfficeId(user.getCompany().getId());
        List<SysOfficeProcessType> officeProcessTypes = officeConfigurationService.obtainOfficeProcessTypes(companyUuid);
        List<SysOfficeProductType> officeProductTypes = officeConfigurationService.obtainOfficeProductTypes(companyUuid);

        Map<Long, String> departmentMap = buildDepartmentMap(departments);
        model.addAttribute("departments", departmentMap);
        model.addAttribute("productTypes", buildProductTypeMap(officeProductTypes));
        model.addAttribute("processTypes", buildProcessTypeMap(officeProcessTypes));

        /**
         * 判断是否是搜索
         */
        if (StringUtils.isBlank(deptId)&&StringUtils.isBlank(productType)&&StringUtils.isBlank(processType)){
            //没有搜索
            List<List<ReviewProcess>> reviewProcessList = reviewProcessService.findAllReviewProcessesGroupBySerialNumber(companyUuid);
            model.addAttribute("records", convert2ProcessList(reviewProcessList, departmentMap));
        }else{
            //有搜索
            List<List<ReviewProcess>> reviewProcessList=reviewConfigurationService.searchReviewProcess(companyUuid,deptId,productType,processType);
            model.addAttribute("records", convert2ProcessList(reviewProcessList, departmentMap));
            model.addAttribute("deptId",StringUtils.isNotBlank(deptId)?Long.parseLong(deptId):null);
            model.addAttribute("productTypeId",StringUtils.isNotBlank(productType)?Integer.parseInt(productType):null);
            model.addAttribute("processTypeId",StringUtils.isNotBlank(processType)?Integer.parseInt(processType):null);
        }
        //查找所有的无需审批的序列号
        model.addAttribute("needNoReviewProcessKey", ReviewConstant.REVIEW_PROCESS_KEY_NOTHING);

        return "review/config/index";
    }

    /**
     * 将流程配置列表转化为页面显示需要的map
     *
     * @param processesList
     * @param departmentMap
     * @return
     */
    private List<Map<String, Object>> convert2ProcessList(List<List<ReviewProcess>> processesList, Map<Long, String> departmentMap) {

//        Assert.notEmpty(departmentMap, "departmentMap should not be empty!");

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (departmentMap==null||departmentMap.size()==0) return result;
        if (processesList != null && processesList.size() > 0) {
            for (List<ReviewProcess> processList : processesList) {
                if (processList != null && processList.size() > 0) {
                    Map<String, Object> recordMap = new HashMap<>();
                    /**
                     * 设置基础数据：序列号、modelId、processKey
                     */
                    recordMap.put("serialNumber", processList.get(0).getSerialNumber());
                    recordMap.put("modelId", processList.get(0).getProcessModelId());
                    recordMap.put("processKey", processList.get(0).getProcessKey());
                    recordMap.put("enable",processList.get(0).getEnable());

                    /**
                     * 设置每个序列的流程列表的部门列表、产品类型列表、流程类型列表
                     */
                    Set<String> departments = new HashSet<>();
                    Set<String> productTypes = new HashSet<>();
                    Set<String> processTypes = new HashSet<>();
                    for (ReviewProcess reviewProcess : processList) {
                        Long deptId = Long.parseLong(reviewProcess.getDeptId());
                        String departmentName = departmentMap.containsKey(deptId) ? departmentMap.get(deptId) : reviewProcess.getDeptId();
                        Integer productType = Integer.parseInt(reviewProcess.getProductType());
                        String productTypeName = ReviewContext.productTypeMap.containsKey(productType) ? ReviewContext.productTypeMap.get(productType) : reviewProcess.getProductType();
                        Integer processType = Integer.parseInt(reviewProcess.getReviewFlow());
                        String processTypeName = ReviewContext.reviewFlowTypeMap.containsKey(processType) ? ReviewContext.reviewFlowTypeMap.get(processType) : reviewProcess.getReviewFlow();

                        departments.add(departmentName);
                        productTypes.add(productTypeName);
                        processTypes.add(processTypeName);
                    }
                    recordMap.put("departments", departments);
                    recordMap.put("productTypes", productTypes);
                    recordMap.put("processTypes", processTypes);

                    result.add(recordMap);
                }

            }
        }

        return result;
    }

    /**
     * 获取当前公司的产品和流程列表
     * @param model
     * @return
     */
    @RequestMapping(value = "getProductAndProcess", method = RequestMethod.GET)
    public String getProductAndProcess(Model model){

        User user= UserUtils.getUser();
        String companyUuid=user.getCompany().getUuid();


        Set<String> products = new HashSet<String>();//流程中所选中的产品列表
        Set<String> processes = new HashSet<String>();//流程中所选中的流程列表

        List<SysOfficeProductType> productTypes=officeConfigurationService.obtainOfficeProductTypes(companyUuid);
        for (SysOfficeProductType sysOfficeProductType:productTypes){
            products.add(sysOfficeProductType.getProductType().toString());
        }
        List<SysOfficeProcessType> processTypes=officeConfigurationService.obtainOfficeProcessTypes(companyUuid);
        for (SysOfficeProcessType sysOfficeProcessType:processTypes){
            processes.add(sysOfficeProcessType.getProcessType().toString());
        }

        model.addAttribute("productTypeMap", ReviewContext.productTypeMap);
        model.addAttribute("reviewFlowTypeMap", ReviewContext.reviewFlowTypeMap);
        model.addAttribute("products", products.toArray(new String[products.size()]));
        model.addAttribute("processes", processes.toArray(new String[processes.size()]));

        return  "review/config/configProductAndProcess";
    }

    /**
     * 保存公司的产品和流程
     * @return
     */
    @RequestMapping(value = "saveProductAndProcess",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveProductAndProcess(@RequestParam(value = "productTypes[]") List<String> productTypes,
                                            @RequestParam(value = "processTypes[]") List<String> processTypes){
        User user= UserUtils.getUser();
        String userId=user.getId().toString();
        String companyId=user.getCompany().getUuid();
        JSONObject result=officeConfigurationService.saveProductTypesAndProcessTypes(buildProductTypes(userId,companyId,productTypes),buildProcessTypes(userId,companyId,processTypes));

        return result;
    }

    private List<SysOfficeProductType> buildProductTypes(String userId, String companyId, List<String> productTypes){
        List<SysOfficeProductType> result=new ArrayList<>();

        if (productTypes!=null&&productTypes.size()>0){
            Date currentDate=new Date();
            for (String productType:productTypes){
                SysOfficeProductType sysOfficeProductType = new SysOfficeProductType();
                sysOfficeProductType.setId(UuidUtils.generUuid());
                sysOfficeProductType.setCompanyId(companyId);
                sysOfficeProductType.setCreateBy(userId);
                sysOfficeProductType.setCreateDate(currentDate);
                sysOfficeProductType.setDelFlag(0);
                sysOfficeProductType.setProductType(Integer.parseInt(productType));
                result.add(sysOfficeProductType);
            }
        }

        return result;
    }

    private List<SysOfficeProcessType> buildProcessTypes(String userId, String companyId, List<String> processTypes){
        List<SysOfficeProcessType> result=new ArrayList<>();

        if (processTypes!=null&&processTypes.size()>0){
            Date currentDate=new Date();
            for (String processType:processTypes){
                SysOfficeProcessType sysOfficeProcessType = new SysOfficeProcessType();
                sysOfficeProcessType.setId(UuidUtils.generUuid());
                sysOfficeProcessType.setCompanyId(companyId);
                sysOfficeProcessType.setCreateBy(userId);
                sysOfficeProcessType.setCreateDate(currentDate);
                sysOfficeProcessType.setDelFlag(0);
                sysOfficeProcessType.setProcessType(Integer.parseInt(processType));
                result.add(sysOfficeProcessType);
            }
        }

        return result;
    }

    @RequestMapping(value = "addProduct", method = RequestMethod.GET)
    public String addProduct() {
        User user = UserUtils.getUser();
        String companyId = user.getCompany().getUuid();
        Date currentDate = new Date();
        List<SysOfficeProductType> officeProductTypes = new ArrayList<SysOfficeProductType>();
        for (Integer productType : ReviewContext.productTypeMap.keySet()) {
            SysOfficeProductType sysOfficeProductType = new SysOfficeProductType();
            sysOfficeProductType.setId(UuidUtils.generUuid());
            sysOfficeProductType.setCompanyId(companyId);
            sysOfficeProductType.setCreateBy(user.getId().toString());
            sysOfficeProductType.setCreateDate(currentDate);
            sysOfficeProductType.setDelFlag(0);
            sysOfficeProductType.setProductType(productType);
            officeProductTypes.add(sysOfficeProductType);
        }
        officeConfigurationService.saveOfficeProductTypes(officeProductTypes);
        return "review/config/index";
    }

    @RequestMapping(value = "addProcess", method = RequestMethod.GET)
    public String addProcess() {
        User user = UserUtils.getUser();
        String companyId = user.getCompany().getUuid();
        Date currentDate = new Date();
        List<SysOfficeProcessType> officeProcessTypes = new ArrayList<SysOfficeProcessType>();
        for (Integer productType : ReviewContext.reviewFlowTypeMap.keySet()) {
            SysOfficeProcessType sysOfficeProductType = new SysOfficeProcessType();
            sysOfficeProductType.setId(UuidUtils.generUuid());
            sysOfficeProductType.setCompanyId(companyId);
            sysOfficeProductType.setCreateBy(user.getId().toString());
            sysOfficeProductType.setCreateDate(currentDate);
            sysOfficeProductType.setDelFlag(0);
            sysOfficeProductType.setProcessType(productType);
            officeProcessTypes.add(sysOfficeProductType);
        }
        officeConfigurationService.saveOfficeProcessTypes(officeProcessTypes);
        return "review/config/index";
    }

    /**
     * 进入流程定义配置页面
     *
     * @param model
     * @return
     * @created_by zhenxing.yan 2015年11月17日
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addConfiguration(Model model) {
        /**
         * 获取部门目录树
         */
        User user = UserUtils.getUser();
        String companyUuid = user.getCompany().getUuid();
        List<Department> departments = departmentService.findByOfficeId(user.getCompany().getId());
        Department root = departmentService.getParent(user.getCompany());
        JSONObject jsonObject=new JSONObject();
        if (root!=null){
            DepartmentNode departmentNode = DepartmentUtil.buildDepartmentTree(0L, root.getId(), departments);
            jsonObject= JSONObject.fromObject(departmentNode);
        }

        List<SysOfficeProcessType> officeProcessTypes = officeConfigurationService.obtainOfficeProcessTypes(companyUuid);
        List<SysOfficeProductType> officeProductTypes = officeConfigurationService.obtainOfficeProductTypes(companyUuid);
        model.addAttribute("departmentJson", jsonObject);
        model.addAttribute("productTypeMap", buildProductTypeMap(officeProductTypes));
        model.addAttribute("reviewFlowTypeMap", buildProcessTypeMap(officeProcessTypes));
        return "review/config/addReview";
    }

    /**
     * 保存流程定义配置，生成流程processKey 1. 首先验证传入的参数是否已经有过配置； 2. 创建流程模板基础信息，生成modelId； 3.
     * 生成processKey； 4. 保存部门、产品类型、流程类型以及processKey的关系。 5. 保存流程权限配置
     *
     * @param depts        部门id列表
     * @param productTypes 产品类型列表
     * @param processTypes 流程类型列表
     * @param needNoReview 是否无需审批
     * @param multiApplyPermit 多次申请受限
     * @return JSONObject
     * @created_by zhenxing.yan 2015年11月17日
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveConfiguration(@RequestParam(value = "depts[]") List<Integer> depts,
                                        @RequestParam(value = "productTypes[]") List<String> productTypes,
                                        @RequestParam(value = "processTypes[]") List<String> processTypes,
                                        @RequestParam(value = "needNoReview") Integer needNoReview,
                                        @RequestParam(value = "multiApplyPermit") Integer multiApplyPermit,
                                        @RequestParam(value = "paymentEqualsCost") Integer paymentEqualsCost) {

        User user = UserUtils.getUser();
        String companyId = user.getCompany().getUuid();//使用UUID

        /**
         * 设置两个默认的属性
         */
        String modelName = "quauq";
        String description = "quauq review model";
        Map<Integer, Integer> specialTypes = new HashMap<Integer, Integer>();
        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_NEED_NO_REVIEW, needNoReview);
//        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY, multiApply);
        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY, multiApplyPermit==1?0:1);
        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST,paymentEqualsCost);
        //增加付款金额等
        JSONObject result = null;
        try {
            result = reviewConfigurationService.saveConfiguration(user.getId().toString(), companyId, modelName,
                    description, TypeConvertUtil.convert2StringList(depts), productTypes, processTypes,
                    specialTypes);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("message","只能为成本付款配置该特殊权限！");
        }

        return result;
    }

    /**
     * 更新流程定义配置
     *
     * @param depts        部门id列表
     * @param productTypes 产品类型列表
     * @param processTypes 流程类型列表
     * @param needNoReview 是否无需审批
     * @param multiApplyPermit 多次申请受限
     * @return JSONObject
     * @created_by zhenxing.yan 2015年11月17日
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyConfiguration(@RequestParam(value = "processKey")String processKey,
                                          @RequestParam(value = "serialNumber")String serialNumber,
                                          @RequestParam(value = "modelId")String modelId,
                                          @RequestParam(value = "depts[]") List<Integer> depts,
                                          @RequestParam(value = "productTypes[]") List<String> productTypes,
                                          @RequestParam(value = "processTypes[]") List<String> processTypes,
                                          @RequestParam(value = "needNoReview") Integer needNoReview,
                                          @RequestParam(value = "multiApplyPermit") Integer multiApplyPermit,
                                          @RequestParam(value = "paymentEqualsCost") Integer paymentEqualsCost) {

        User user = UserUtils.getUser();
        String companyId = user.getCompany().getUuid();//使用uuid

        Map<Integer, Integer> specialTypes = new HashMap<Integer, Integer>();
        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_NEED_NO_REVIEW, needNoReview);
//        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY, multiApply);
        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_MULTI_APPLY, multiApplyPermit==1?0:1);
        specialTypes.put(ReviewContext.SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST,paymentEqualsCost);
        JSONObject result = null;
        try {
         result = reviewConfigurationService.modifyProcess(user.getId().toString(), companyId, processKey,
                serialNumber,modelId, TypeConvertUtil.convert2StringList(depts), productTypes, processTypes,
                specialTypes);
    } catch (Exception e) {
        e.printStackTrace();
        result.put("code", -1);
        result.put("message","只能为成本付款配置该特殊权限！");
    }

        return result;
    }




    /**
     * 跳转至流程设计器
     * @param modelId
     * @param processKey
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/model/design", method = RequestMethod.GET)
    public String modelDesing(@RequestParam("modelId") String modelId, @RequestParam("processKey") String processKey,@RequestParam("serialNumber")String serialNumber){

        return "redirect:" + "/process-editor/modeler.jsp?modelId=" + modelId + "&processKey=" + processKey+"&serialNumber="+serialNumber;

    }


    /**
     * 审批详情页面
     *
     * @param serialNumber
     * @return
     * @created_by zhenxing.yan 2015年11月19日
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String configDetail(Model model, @RequestParam("serialNumber") String serialNumber) {
        /**
         * 获取部门目录树
         */
        User user = UserUtils.getUser();
        String companyUuid=user.getCompany().getUuid();
        List<Department> departments = departmentService.findByOfficeId(user.getCompany().getId());
        Department root = departmentService.getParent(user.getCompany());
        DepartmentNode departmentNode = DepartmentUtil.buildDepartmentTree(0L, root.getId(), departments);
        JSONObject jsonObject = JSONObject.fromObject(departmentNode);

        List<SysOfficeProcessType> officeProcessTypes = officeConfigurationService.obtainOfficeProcessTypes(companyUuid);
        List<SysOfficeProductType> officeProductTypes = officeConfigurationService.obtainOfficeProductTypes(companyUuid);


        /**
         * 查找当前processKey下的所有部门、产品、流程记录
         */

        Set<Long> depts = new HashSet<Long>();//该流程所选中的部门列表
        Set<String> products = new HashSet<>();//流程中所选中的产品列表
        Set<String> processes = new HashSet<>();//流程中所选中的流程列表
//        Map<String,String> products = new HashMap<>();//流程中所选中的产品列表
//        Map<String,String> processes = new HashMap<>();//流程中所选中的流程列表
        List<ReviewProcess> reviewProcesses = reviewProcessService.findReviewProcessBySerialNumber(serialNumber);
        String modelId = ReviewContext.PROCESS_MODEL_ID_NEED_NO_REVIEW;
        String processKey="";
        if (reviewProcesses != null && reviewProcesses.size() > 0) {
            for (ReviewProcess reviewProcess : reviewProcesses) {
                depts.add(Long.parseLong(reviewProcess.getDeptId()));
                products.add(reviewProcess.getProductType());
                processes.add(reviewProcess.getReviewFlow());
//                products.put(reviewProcess.getProductType(),reviewProcess.getProductType());
//                processes.put(reviewProcess.getReviewFlow(),reviewProcess.getReviewFlow());
            }
            modelId = reviewProcesses.get(0).getProcessModelId();
            processKey=reviewProcesses.get(0).getProcessKey();
        }

        Date createDate = repositoryService.getModel(modelId).getCreateTime();

        ReviewConfig reviewConfig=reviewConfigService.findBySerialNumber(serialNumber);

        /**
         * 判断是否有成本审批，如果有的话
         */
        if (processes.contains(Context.REVIEW_FLOWTYPE_STOCK.toString())){
            ReviewCostPaymentConfiguration reviewCostPaymentConfiguration=reviewCostPaymentConfigurationService.getConfiguration((depts.toArray(new Long[depts.size()]))[0].toString(),(products.toArray(new String[products.size()]))[0].toString(), Context.REVIEW_FLOWTYPE_STOCK.toString());
            model.addAttribute("reviewCostPaymentConfiguration", reviewCostPaymentConfiguration);
        }

        model.addAttribute("departmentJson", jsonObject);
        model.addAttribute("productTypeMap", buildProductTypeMap(officeProductTypes));
        model.addAttribute("reviewFlowTypeMap", buildProcessTypeMap(officeProcessTypes));
        model.addAttribute("depts", depts);
        model.addAttribute("products", products.toArray(new String[products.size()]));
        model.addAttribute("processes", processes.toArray(new String[processes.size()]));
        model.addAttribute("modelId", modelId);
        model.addAttribute("reviewConfig", reviewConfig);
        model.addAttribute("processKey", processKey);
        model.addAttribute("serialNumber", serialNumber);
        model.addAttribute("createDate", createDate);
        model.addAttribute("needNoReviewModelId", ReviewContext.PROCESS_MODEL_ID_NEED_NO_REVIEW);
        model.addAttribute("needNoReviewProcessKey", ReviewConstant.REVIEW_PROCESS_KEY_NOTHING);
        return "review/config/detail";
    }

    /**
     * 审批修改页面
     *
     * @param serialNumber
     * @return
     * @created_by zhenxing.yan 2015年11月19日
     */
    @RequestMapping(value = "modifyInfo", method = RequestMethod.GET)
    public String configModify(Model model, @RequestParam("serialNumber") String serialNumber) {
        /**
         * 获取部门目录树
         */
        User user = UserUtils.getUser();
        String companyId = user.getCompany().getId().toString();
        String companyUuid=user.getCompany().getUuid();
        List<Department> departments = departmentService.findByOfficeId(user.getCompany().getId());
        Department root = departmentService.getParent(user.getCompany());
        DepartmentNode departmentNode = DepartmentUtil.buildDepartmentTree(0L, root.getId(), departments);
        JSONObject jsonObject = JSONObject.fromObject(departmentNode);

        List<SysOfficeProcessType> officeProcessTypes = officeConfigurationService.obtainOfficeProcessTypes(companyUuid);
        List<SysOfficeProductType> officeProductTypes = officeConfigurationService.obtainOfficeProductTypes(companyUuid);


        /**
         * 查找当前processKey下的所有部门、产品、流程记录
         */
        Set<Long> depts = new HashSet<Long>();//该流程所选中的部门列表
        Set<String> products = new HashSet<String>();//流程中所选中的产品列表
        Set<String> processes = new HashSet<String>();//流程中所选中的流程列表
//        List<ReviewProcess> reviewProcesses = reviewProcessService.findReviewProcessByProcessKey(processKey);
        List<ReviewProcess> reviewProcesses = reviewProcessService.findReviewProcessBySerialNumber(serialNumber);
        String modelId = ReviewContext.PROCESS_MODEL_ID_NEED_NO_REVIEW;
        String processKey="";
        if (reviewProcesses != null && reviewProcesses.size() > 0) {
            for (ReviewProcess reviewProcess : reviewProcesses) {
                depts.add(Long.parseLong(reviewProcess.getDeptId()));
                products.add(reviewProcess.getProductType());
                processes.add(reviewProcess.getReviewFlow());
            }
            modelId = reviewProcesses.get(0).getProcessModelId();
            processKey=reviewProcesses.get(0).getProcessKey();
        }

        /**
         * 判断是否有成本审批，如果有的话
         */
        if (processes.contains(Context.REVIEW_FLOWTYPE_PAYMENT.toString())){
            ReviewCostPaymentConfiguration reviewCostPaymentConfiguration=reviewCostPaymentConfigurationService.getConfiguration((depts.toArray(new Long[depts.size()]))[0].toString(),(products.toArray(new String[products.size()]))[0].toString(), Context.REVIEW_FLOWTYPE_PAYMENT.toString());
            model.addAttribute("reviewCostPaymentConfiguration", reviewCostPaymentConfiguration);
        }

        /**
         * 查找历史部署版本信息
         */
        List<Map<String,Object>> deploymentMaps=new ArrayList<>();
        Map<String,Object> firstDeploymentMap=new HashMap<>();
        List<Deployment> deployments=repositoryService.createDeploymentQuery().processDefinitionKey(processKey).orderByDeploymenTime().desc().list();
        if (deployments!=null&&deployments.size()>0){
            for (Deployment deployment:deployments){
                Map<String,Object> deploymentMap=new HashMap<>();
                deploymentMap.put("deployId",deployment.getId());
                deploymentMap.put("deployDate",deployment.getDeploymentTime());
                deploymentMaps.add(deploymentMap);
            }
        }

//        //历史版本去掉最新的一个
//        if(deploymentMaps.size()>0){
//            deploymentMaps.remove(0);
//        }
        //第一个显示model的图片
        Date deployDate = repositoryService.getModel(modelId).getLastUpdateTime();
        firstDeploymentMap.put("modelId",modelId);
        firstDeploymentMap.put("deployDate",deployDate);

        ReviewConfig reviewConfig=reviewConfigService.findBySerialNumber(serialNumber);

        /**
         * 判断是否有成本审批，如果有的话
         */
        if (processes.contains(Context.REVIEW_FLOWTYPE_PAYMENT.toString())){
            ReviewCostPaymentConfiguration reviewCostPaymentConfiguration=reviewCostPaymentConfigurationService.getConfiguration((depts.toArray(new Long[depts.size()]))[0].toString(),(products.toArray(new String[products.size()]))[0].toString(), Context.REVIEW_FLOWTYPE_PAYMENT.toString());
            model.addAttribute("reviewCostPaymentConfiguration", reviewCostPaymentConfiguration);
        }

        model.addAttribute("departmentJson", jsonObject);
        model.addAttribute("productTypeMap", buildProductTypeMap(officeProductTypes));
        model.addAttribute("reviewFlowTypeMap", buildProcessTypeMap(officeProcessTypes));
        model.addAttribute("depts", depts);
        model.addAttribute("products", products.toArray(new String[products.size()]));
        model.addAttribute("processes", processes.toArray(new String[processes.size()]));
        model.addAttribute("modelId", modelId);
        model.addAttribute("processKey", processKey);
        model.addAttribute("reviewConfig", reviewConfig);
        model.addAttribute("serialNumber", serialNumber);
        model.addAttribute("deploymentMaps", deploymentMaps);
        model.addAttribute("firstDeploymentMap", firstDeploymentMap);
        model.addAttribute("needNoReviewModelId", ReviewContext.PROCESS_MODEL_ID_NEED_NO_REVIEW);
        model.addAttribute("needNoReviewProcessKey", ReviewConstant.REVIEW_PROCESS_KEY_NOTHING);
        return "review/config/modify";
    }

    /**
     * 显示model的png图片
     */
    @RequestMapping(value = "model/diagram/{modelId}", method = RequestMethod.GET)
    public void showDiagram(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        try {
            byte[] bpmnBytes = repositoryService.getModelEditorSourceExtra(modelId);
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            response.setContentType("image/png");
            IOUtils.copy(in, response.getOutputStream());
//			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看指定部署的图片（用于显示历史版本）
     * @param deploymentId
     * @param response
     */
    @RequestMapping(value = "process/diagram/{deploymentId}")
    public void showDiagramWithVersion(@PathVariable("deploymentId")String deploymentId,HttpServletResponse response){
        try {
            List<String> resourceNames=repositoryService.getDeploymentResourceNames(deploymentId);
            if (resourceNames!=null&&resourceNames.size()>0){
                String imageName="";
                for (String resourceName:resourceNames){
                    if (resourceName.indexOf(".png")>0){
                        imageName=resourceName;
                        break;
                    }
                }
                if (StringUtils.isNotBlank(imageName)){
                    InputStream inputStream=repositoryService.getResourceAsStream(deploymentId,imageName);
                    response.setContentType("image/png");
                    IOUtils.copy(inputStream, response.getOutputStream());
                    response.flushBuffer();
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 启用审批配置（部署审批流程）
     * @param modelId
     * @return
     */
    @RequestMapping(value="deploy",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deploy(@RequestParam("modelId")String modelId,@RequestParam("serialNumber")String serialNumber,@RequestParam("processKey")String processKey){
        JSONObject result=new JSONObject();
        result.put("code",0);
        try {
            reviewManagementService.deployFromProcessModel(serialNumber,processKey,modelId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("msg","流程启用失败，请检查流程图是否配置正确或与管理员联系！");
        }
        return result;
    }

    /**
     * 停用审批配置
     * @param serialNumber
     * @param processKey
     * @return
     */
    @RequestMapping(value="undeploy",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject undeploy(@RequestParam("serialNumber") String serialNumber,@RequestParam("processKey") String processKey){
        JSONObject result=new JSONObject();
        result.put("code",0);
        try {
            reviewManagementService.undeployFromProcessKey(serialNumber,processKey);
        } catch (Throwable e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("msg","流程停用失败！");
        }
        return result;
    }

    /**
     * 删除流程配置
     * @param serialNumber
     * @param processKey
     * @return
     */
    @RequestMapping(value="delete",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteReviewProcess(@RequestParam("serialNumber") String serialNumber,@RequestParam("processKey") String processKey){
        JSONObject result=reviewConfigurationService.deleteReviewProcess(serialNumber,processKey);
        return result;
    }

    /**
     * 删除流程验证，如果存在正在审批的
     * @param serialNumber
     * @param processKey
     * @return
     */
    @RequestMapping(value="delete/validation",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteValidation(@RequestParam("serialNumber") String serialNumber,@RequestParam("processKey") String processKey){
        JSONObject result=reviewConfigurationService.deleteValidation(serialNumber,processKey);
        return result;
    }

    /**
     * 构建公司部门映射关系
     *
     * @param departments
     * @return
     */
    private Map<Long, String> buildDepartmentMap(List<Department> departments) {
        Map<Long, String> result = new HashMap<Long, String>();
        if (departments != null && departments.size() > 0) {
            for (Department department : departments) {
                result.put(department.getId(), department.getName());
            }
        }
        return result;
    }

    /**
     * 构建公司产品类型映射关系
     *
     * @param officeProductTypes
     * @return
     * @created_by zhenxing.yan 2015年11月16日
     */
    private Map<Integer, String> buildProductTypeMap(List<SysOfficeProductType> officeProductTypes) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        if (officeProductTypes != null && officeProductTypes.size() > 0) {
            for (SysOfficeProductType sysOfficeProductType : officeProductTypes) {
                if (ReviewContext.productTypeMap.containsKey(sysOfficeProductType.getProductType())) {
                    result.put(sysOfficeProductType.getProductType(),
                            ReviewContext.productTypeMap.get(sysOfficeProductType.getProductType()));
                }
            }
        }
        return result;
    }

    /**
     * 构建公司流程类型映射关系
     *
     * @param officeProcessTypes
     * @return
     * @created_by zhenxing.yan 2015年11月16日
     */
    private Map<Integer, String> buildProcessTypeMap(List<SysOfficeProcessType> officeProcessTypes) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        if (officeProcessTypes != null && officeProcessTypes.size() > 0) {
            for (SysOfficeProcessType sysOfficeProcessType : officeProcessTypes) {
                if (ReviewContext.reviewFlowTypeMap.containsKey(sysOfficeProcessType.getProcessType())) {
                    result.put(sysOfficeProcessType.getProcessType(),
                            ReviewContext.reviewFlowTypeMap.get(sysOfficeProcessType.getProcessType()));
                }
            }
        }
        return result;
    }
}
