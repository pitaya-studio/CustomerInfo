package com.quauq.activiti.rest.process.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quauq.review.core.engine.ProcessTagService;
import com.quauq.review.core.engine.ReviewConditionService;
import com.quauq.review.core.engine.ReviewProcessService;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ProcessTag;
import com.quauq.review.core.engine.entity.ReviewProcess;
import com.quauq.review.core.extend.condition.ConditionAssigneeConfiguration;
import com.quauq.review.core.management.ReviewManagementService;
import com.quauq.review.core.utils.SetUtil;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.SysJobNew;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserDeptJobNew;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.configuration.config.ReviewContext;

@Controller
@RequestMapping(value = "${adminPath}/activitytest")
public class ProcessController implements ModelDataJsonConstants {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private SystemService systemService;

	@Autowired
	private ProcessTagService processTagService;
	
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ConditionAssigneeConfiguration conditionAssigneeConfiguration;

	@Autowired
	private ReviewConditionService reviewConditionService;

	@Autowired
	private ReviewProcessService reviewProcessService;

	@Autowired
	private ReviewManagementService reviewManagementService;

	@Autowired
	private DepartmentService departmentService;

	/**
	 * 根据供应商获取用户列表
	 * @author  songyang 2015年10月27日16:29:23  
	 * @return
	 */
	@RequestMapping(value = "/getUser")
	@ResponseBody
	public Map<String,List<Map<String, String>>> getUser() {
		Map<String,List<Map<String, String>>> result=new HashMap<>();
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String companyUuid=user.getCompany().getUuid();
		MultiValueMap<Long,UserDeptJobNew> userDeptJobNewMultiValueMap=systemService.findUserDeptJobByCompanyUuid(companyUuid);
		Map<Long,Department> departmentMap=departmentService.getDepartmentMapByOfficeId(companyId);
		Map<Long,SysJobNew> sysJobNewMap=systemService.findSysJobMapByCompanyUuid(companyUuid);
		// 某一供应商下的所有用户
		List<User> comListUser = systemService.getAllUserByCompanyId(companyId);
		for (int i = 0; i < comListUser.size(); i++) {
			Long userId=comListUser.get(i).getId();
			if (userDeptJobNewMultiValueMap.containsKey(userId)){
				List<UserDeptJobNew> userDeptJobNews=userDeptJobNewMultiValueMap.get(userId);
				for (UserDeptJobNew userDeptJobNew:userDeptJobNews){
					Map<String, String> userMap = new HashMap<String, String>();
					//格式 “userId-deptId-jobId”
					userMap.put("id", comListUser.get(i).getId().toString()+"-"+userDeptJobNew.getDept_id()+"-"+userDeptJobNew.getJob_id());
					String deptName=departmentMap.containsKey(userDeptJobNew.getDept_id())?departmentMap.get(userDeptJobNew.getDept_id()).getName()+"-":"";
					String jobName=sysJobNewMap.containsKey(userDeptJobNew.getJob_id())?sysJobNewMap.get(userDeptJobNew.getJob_id()).getName()+"-":"";
					userMap.put("name",deptName+jobName+comListUser.get(i).getName());
					userList.add(userMap);
				}
			}else{
				Map<String, String> userMap = new HashMap<String, String>();
				userMap.put("id", comListUser.get(i).getId().toString());
				userMap.put("name",comListUser.get(i).getName());
				userList.add(userMap);
			}
		}
		result.put("assigneeList",userList);

		/**
		 * 获取部门职务组
		 */
		List<Map<String, String>> groups=new ArrayList<Map<String, String>>();
		List<String> deptJobs=systemService.findDeptJobByCompanyUuid(companyUuid);
		if (deptJobs!=null&&deptJobs.size()>0){
			for (String deptJob:deptJobs){
				String[] values=deptJob.split(",");
				//格式：部门id-职务id
				if (values.length==2){
					Long deptId=Long.parseLong(values[0]);
					Long jobId=Long.parseLong(values[1]);
					Map<String, String> groupMap=new HashMap<>();
					groupMap.put("id",values[0]+"-"+values[1]);
					if (departmentMap.containsKey(deptId)&&sysJobNewMap.containsKey(jobId)){
						groupMap.put("name",departmentMap.get(deptId).getName()+"-"+sysJobNewMap.get(jobId).getName());
					}
					groups.add(groupMap);
				}
			}
		}
		result.put("groupList",groups);

		/**
		 * 获取审批条件
		 */
		List<Map<String, String>> condition=new ArrayList<Map<String, String>>();
		Map<String, String> configs=conditionAssigneeConfiguration.getDisplayLabels();
		for (String key: configs.keySet()) {
			Map<String,String> conditionMap=new HashMap<String,String>();
			conditionMap.put("id",key);
			conditionMap.put("name",configs.get(key));
			condition.add(conditionMap);
		}
		result.put("conditionList",condition);
		return result;
	}

	/**
	 * 保存设计模型
	 * @author songyang 2015年10月27日16:29:23
	 * @param modelId
	 * @param values
	 */
	@RequestMapping(value = "/{modelId}/save", method = RequestMethod.PUT)
//	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public void saveModel(@PathVariable String modelId,@RequestParam("processKey") String processKey,@RequestParam("serialNumber")String serialNumber,
			@RequestBody MultiValueMap<String, String> values,HttpServletResponse response) {
		try {
			String userId=UserUtils.getUser().getId().toString();
			String companyId=UserUtils.getUser().getCompany().getUuid();

			// 解析多层json串
			JSONObject jsonObject = new JSONObject(values.getFirst("json_xml"));
			JSONArray jsonArray = jsonObject.getJSONArray("childShapes");

			JSONObject propertiesJson=jsonObject.getJSONObject("properties");
			/**
			 * 设置processKey
			 */
			String process_id =processKey;
			if (!propertiesJson.has("process_id")){
				propertiesJson.put("process_id",process_id);
			}
			List<ProcessTag> processTags=new ArrayList<>();
			if (null != jsonArray && jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject childJson = jsonArray.getJSONObject(i);
					/**
					 * 判断是否是用户流程
					 */
					if(childJson.has("stencil")) {
						JSONObject stencilJson = childJson.getJSONObject("stencil");
						//判断是否是用户流程
						if (stencilJson!=null&&stencilJson.has("id")&&"UserTask".equals(stencilJson.getString("id"))) {
							/**
							 * 通过属性对象获取用户的职务id
							 * 如果没有环节id的产生uuid并赋值
							 * 同时回写解析后的用户id、组id等到原来的json对象中
							 */
							JSONObject properties = childJson.getJSONObject("properties");
							//该环节id
							String activityId = properties.getString("overrideid");
							if (StringUtils.isBlank(activityId)) {
								activityId = "key"+UuidUtils.generUuid();
								//环节id回写
								properties.put("overrideid",activityId);
							}
							Set<String> receiptTagSet = new HashSet<>();//该环节标签集合
							JSONObject usertaskassignmentJson = properties.getJSONObject("usertaskassignment");
							if (usertaskassignmentJson != null && usertaskassignmentJson.length() > 0) {
								if (usertaskassignmentJson.has("assignment")) {
									JSONObject assignmentJson = usertaskassignmentJson.getJSONObject("assignment");

									/**
									 * 判断指定审批人
									 */
									if (assignmentJson.has("assignee")&&StringUtils.isNotBlank(assignmentJson.getString("assignee"))&&!"null".equals(assignmentJson.getString("assignee"))) {
										String assignee = assignmentJson.getString("assignee");
										if (StringUtils.isNotBlank(assignee)) {
											/**
											 * 计算单据条件，同时还原用户id
											 */
											String[] idValues = assignee.split("-");//规则  userId-部门id-职务id
											if (idValues.length == 3) {
												receiptTagSet.add(idValues[2]);
											}
//											assignmentJson.put("assignee",idValues[0]);
										}
									}else{//如果指定了审批人，其他设置失效
										/**
										 * 判断候选审批人
										 */
										if (assignmentJson.has("candidateUsers")) {
											JSONArray candidateUsers=assignmentJson.getJSONArray("candidateUsers");
											if (candidateUsers!=null&&candidateUsers.length()>0){
												for (int j=0;j<candidateUsers.length();j++){
													JSONObject candidateUser=candidateUsers.getJSONObject(j);
													if (candidateUser.has("value")){
														String candidateUserStr=candidateUser.getString("value");

														if (StringUtils.isNotBlank(candidateUserStr)) {
															/**
															 * 计算单据条件，同时还原用户id
															 */
															String[] idValues = candidateUserStr.split("-");//规则  userId-部门id-职务id
															if (idValues.length == 3) {
																receiptTagSet.add(idValues[2]);
															}
//															candidateUser.put("value",idValues[0]);
														}
													}
												}
											}
										}

										/**
										 * 判断候选审批组
										 */
										if (assignmentJson.has("candidateGroups")) {
											JSONArray candidateGroups=assignmentJson.getJSONArray("candidateGroups");
											if (candidateGroups!=null&&candidateGroups.length()>0){
												for (int j=0;j<candidateGroups.length();j++){
													JSONObject candidateGroup=candidateGroups.getJSONObject(j);
													if (candidateGroup.has("value")){
														String candidateGroupStr=candidateGroup.getString("value");

														if (StringUtils.isNotBlank(candidateGroupStr)) {
															/**
															 * 计算单据条件
															 */
															String[] idValues = candidateGroupStr.split("-");//规则  部门id-职务id
															if (idValues.length == 2) {
																receiptTagSet.add(idValues[1]);
															}
														}
													}
												}
											}
										}

										/**
										 * 判断候选条件
										 */
										if(assignmentJson.has("condition")){
											String condition=assignmentJson.getString("condition");
											if(StringUtils.isNotBlank(condition)){
												receiptTagSet.add(ReviewContext.DEFAULT_CONDITION_JOB_ID);
												//保存审批条件
												reviewConditionService.save(userId,companyId,processKey,activityId,condition);
											}
										}
									}
									if (receiptTagSet.size()>0){
										ProcessTag processTag = new ProcessTag();
										processTag.setProcessKey(process_id);
										processTag.setCompanyId(companyId);
										processTag.setActivityId(activityId);
										processTag.setTagId(SetUtil.convertSet2Str(receiptTagSet,","));
										processTag.setCreateBy(UserUtils.getUser().getId().toString());
										processTag.setCreateDate(new Date());

										processTags.add(processTag);
									}
								}
							}
						}
					}
				}
			}
			//回写json数据
			values.put("json_xml",Arrays.asList(jsonObject.toString()));
			reviewService.saveModel(UserUtils.getUser().getId().toString(), modelId, values);

			//保存标签
			processTagService.save(userId,processKey,processTags);
			/**
			 * 如果当前流程是启用状态，则自动部署新版本
			 */
			List<ReviewProcess> reviewprocesses=reviewProcessService.findReviewProcessBySerialNumber(serialNumber);
			if (reviewprocesses!=null&&reviewprocesses.size()>0){
				if(reviewprocesses.get(0).getEnable()==1){//已经是启用状态，重新部署
					reviewManagementService.deployFromProcessModel(serialNumber,processKey,modelId);
				}
			}

			response.setStatus(200);
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(200);
		}
	}


	/**
	 * 导出model的xml文件
	 */
	
	
	@RequestMapping(value = "/export" ,method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		try {
			String modelId = request.getParameter("modelId");
			org.activiti.engine.repository.Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
//			IOUtils.copy(in, response.getOutputStream());
			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";

			//流程部署
			Deployment deployment = repositoryService.createDeployment().addInputStream(filename, in).deploy();
			List<Deployment> list = repositoryService.createDeploymentQuery().processDefinitionKey("refund").list();
			
			//流程导出
//			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
//			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("导出model的xml文件失败：modelId={}", modelId, e);
		}
	}

	
	/**
	 * 部署流程
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/deploy")
	public String deploy(@RequestParam(value = "file", required = false) MultipartFile file) {
		// String exportDir="";
		String fileName = file.getOriginalFilename();

		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;

			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			}

			List<Deployment> list = repositoryService.createDeploymentQuery().processDefinitionKey("process1").list();
//			System.out.println(list.size());
			// List<ProcessDefinition> list =
			// repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			// System.out.println(list.size());
			// for (ProcessDefinition processDefinition : list) {
			// WorkflowUtils.exportDiagramToFile(repositoryService,
			// processDefinition, exportDir);
			// }

		} catch (Exception e) {
			e.printStackTrace();
//			log.error("error on deploy process, because of file input stream", e);
		}

		return "redirect:/workflow/deploylist";
	}

}
