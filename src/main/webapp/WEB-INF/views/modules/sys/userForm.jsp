<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>账号<c:if test="${param.id==null or fn:length(param.id)==0}">添加</c:if><c:if test="${param.id!=null and fn:length(param.id)!=0}">修改</c:if></title>
    <meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
	<link rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/user/userForm.js" type="text/javascript"></script>
	<script type="text/javascript">
		function downloads(docid){
			window.open("${ctx}/sys/docinfo/download/"+docid);
		}
		$(function(){
			likePlaceHolder();
		});
		/**
		 * 上传头像
		 */
		function uploadHeadPicFunc(obj){

			//新建一个隐藏的div，用来保存文件上传后返回的数据
			if($(obj).parent().find(".uploadPath").length == 0) {
				$(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
				$(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');
			}
			$(obj).addClass("clickBtn");

			/*移除产品行程校验提示信息label标签*/
			$("#modIntroduction").remove();
			$.jBox("iframe:${ctx}/sys/user/uploadPicturePage", {
				title: "请上传头像",
				buttons:{"关闭":true},
				width:650,
				height:550,
				persistent:true,
				submit : function(v, h, f) {
					$(h.find("iframe")[0].contentWindow.successUpload).click();
					if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
						$(obj).next(".batch-ol").children().remove();
						$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
							$(obj).next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:110px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span>' +
									'<a class="batchDl" href="javascript:void(0)" onClick="downloads(\''+$(obj1).prev().val()+'\')">下载</a>' +
									'<a class="batchDel"  style="margin-left: 2px" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><li>');
						});
						if($(obj).parent().find("#currentFiles").children().length != 0) {
							$(obj).parent().find("#currentFiles").children().remove();
						}
					}
					$(".clickBtn",window.parent.document).removeClass("clickBtn");
				}
			});
			$("#jbox-content").css("overflow-y","hidden");
		}
		/**
		 * 删除上传的文件显示，
		 * ps:具体是否删除，需要后台操作
		 * @param obj
		 */
		function removeHeadPic(obj){
			$(obj).parent().empty();
		}
		/**
		 * 密码提示问题
		 */
		function likePlaceHolder(){
			var obj = $(obj);
			var ua = navigator.userAgent.toLowerCase();
			var isIE = /msie/.test(ua);
			var node = document.getElementById('newPassword');
			var nodeTip = document.getElementById('ipt-tips');
			var func = function(){
				//TODO
				if(node.value.length==0){
					$(nodeTip).show();
				}else{
					$(nodeTip).hide();
				}
			}
			if(isIE){
				node.attachEvent('onpropertychange', function(){
					if(window.event.propertyName == 'value'){
						//func();
						if(node.value.length==0){
							$(nodeTip).show();
						}else{
							$(nodeTip).hide();
						}
					}
				});
			} else{
				node.addEventListener('input',func, false);
			}

		}
	</script>
	<style type="text/css">
		.batch-ol li {
			width: 170px;
		}
		.upload_f span {
			max-width: 110px;
		}
	</style>
</head>
<body>
    <content tag="three_level_menu">
        <li><a href="${ctx}/sys/profile/info">个人信息</a></li>
        <li><a href="${ctx}/sys/profile/modifyPwd">修改密码</a></li>
        <shiro:hasPermission name="sys:user:view"><li><a href="${ctx}/sys/user/">账号管理</a></li></shiro:hasPermission>
        <li class="active"><a href="${ctx}/sys/user/form<c:if test="${not empty user.id}">?id=${user.id}</c:if>">账号<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
        <shiro:hasPermission name="transfer:leave:account"><li><a href="${ctx}/sys/user/transferLeaveAccountForm">离职账户转移</a></li></shiro:hasPermission>
    </content>
    
	<div class="sysdiv">
		<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save?companyId=${user.company.id}" method="post" class="form-horizontal">
			<input type="hidden" value="${departmentJson}" id="departmentJson">
			<input type="hidden" value="${user.roleIds}" id="roleIds">
			<input type="hidden" value="${fns:getUser().id}" id="userId">
			<input type="hidden" value="${deptId}" id="deptId">
			<input type="hidden" value="${selectIds}" id="selectIds">
			<input type="hidden" value="${user.loginName}" id="hiddenLoginName">
			<input type="hidden" value="${user.groupeSurname}" id="hiddenGroupeSurname">
			
			<form:hidden path="id"/>
			<tags:message content="${message}"/>
			<ul class="sys_adduser">
				<li class="title-account">
					<em></em>
					<label>账号类型</label>
				</li>
				<li class="sys_adduser_select">
					<label><font style="color:#ff0000; padding-right:5px;">*</font>账号类型：</label>
					<c:if test="${empty user.id }">
						<form:select path="userType" class="required" >
							<form:option value="" label="请选择"/>
							<c:if test="${ fns:getUser().id==1  }">
								<form:option value="3">接待社内部用户</form:option>
							</c:if>
							<c:if test="${ fns:getUser().id!=1  }">
								<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</c:if>
						</form:select>
					</c:if>
                   
					<c:if test="${not empty user.id }">
						${fns:getDictLabel(user.userType, 'sys_user_type', '')}
						<input type="hidden" value="${user.userType }" id="userType"/>
					</c:if>
				</li>
                    
				<c:if test="${ fns:getUser().id==1}">
					<li class="sys_adduser_select" id="companyList" style="display: none;">
						<label><font style="color:#ff0000; padding-right:5px;">*</font>批发商：</label>
						<form:select path="company.id" class="companySelect">
							<form:option value="" label="请选择"/>
							<c:forEach items="${fns:getOfficeList(false,'','')}" var="company" varStatus="idxStatus">
								<c:if test="${company.id != 1}">
									<form:option value="${company.id}" label="${company.name}"/>
								</c:if>
							</c:forEach>
						</form:select>
					</li>
				</c:if>                
                    <li id="agentdiv" class="sys_adduser_select sys_adduser_colspan" style="display:none" >
                      <font style="color:#ff0000; padding-right:5px;">*</font>
                      <label >所属渠道商：</label>
                      <form:select path="agentId" >
                        	<form:option value="" label="请选择"/>
                        	<c:set var="companyId" value="${fns:getUser().company.id}"/>
                      		<c:forEach items="${fns:getAgentList()}" var="agent" varStatus="idxStatus">
                      			<c:if test="${agent.supplyId eq companyId}">
                      				<form:option value="${agent.id}" label="${agent.agentName}"/>
                      			</c:if>
                      		</c:forEach>
                      </form:select>
                      <a class="btn btn-primary" href="${ctx }/agent/manager/firstForm">+增加渠道商</a>
                     </li>                     
				<li class="title-account">
					<em></em>
					<label>角色权限</label><input id="companyId" type="hidden" value = "${user.company.id}"/>
				</li>
				<li class="sys_adduser_check">
					<label id="roleTitle" class="old">用户角色：</label>
					<span id="control_xon" class="s1">
						<c:forEach items="${allRoles}" var="role" varStatus="idxStatus">
							<span userType="${role.userType }"><input type="checkbox" value="${role.id }" name="roleIdList" id="roleIdList${idxStatus.count }"/><label for="roleIdList${idxStatus.count }">${role.name } </label></span>
							<c:if test="${idxStatus.count eq '10'}"><br></c:if>
						</c:forEach>  
					</span>
				</li>
				<li class="clear"></li>
				
				<li class="title-account">
					<em></em>
					<label>用户信息</label>
				</li>
				<li>
					<label ><font style="color:#ff0000; padding-right:5px;">*</font>姓名：</label>
					<form:input path="name" htmlEscape="false" maxlength="50" />
				</li>
  				<li>
					<label >邮箱：</label>
					<form:input path="email" htmlEscape="false" maxlength="50" value="${user.email }"/>
				</li>
				<li class="pr">
					<label ><c:if test="${empty param.id}"><font style="color:#ff0000; padding-right:5px;">*</font></c:if>密码：</label>
					<input id="newPassword" name="newPassword" type="password" value="${randomPwd}" maxlength="50" minlength="3" class="${empty user.id?'required':''}" />
					<c:if test="${not empty user.id}"><span class="ipt-tips" style="display: block;">若不修改密码，请留空。</span></c:if>
				</li>
				<li class="clear"></li>
				
				<li>
					<label ><font style="color:#ff0000; padding-right:5px;">*</font>帐号：</label>
					<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
					<form:input path="loginName" htmlEscape="false" maxlength="50"/>
				</li>
				<li>
					<label >电话：</label>
					<form:input path="phone" htmlEscape="false" maxlength="15"/>
				</li>
				<li>
					<label><c:if test="${param.id==null}"><font style="color:#ff0000; padding-right:5px;">*</font></c:if>确认密码：</label>
					<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="${randomPwd}" maxlength="50" minlength="3" equalTo="#newPassword" class="<c:if test='${param.id==null or fn:length(param.id)==0}'>required</c:if>"/>
				</li>
				<li class="clear"></li>
				
				<li>
					<label ><font style="color:#ff0000; padding-right:5px;">*</font>工号：</label>
					<input id="oldNo" name="oldNo" type="hidden" value="${user.no}">
					<form:input path="no" htmlEscape="false" maxlength="50" /> 
				</li>
				<li>
					<label ><font style="color:#ff0000; padding-right:5px;">*</font>手机：</label>
					<form:input path="mobile" htmlEscape="false" maxlength="14"/>
				</li>
				<li>
					<label >备注：</label>
					<form:input path="remarks" htmlEscape="false" maxlength="200"/>
				</li>
				<!-- 517 批发商认证 开始 -->
				<li class="clear"></li>
				<li>
					<label>上传名片：</label>
					<input type="button" name="cardId" value="选择文件" class="license_file" onclick="uploadSingleFile('${ctx}',null,this)">
					<ol class="batch-ol upload_f">
						<c:if test="${not empty user.cardDocInfo}">
							<li>
								<span title="${user.cardDocInfo.docName}">${user.cardDocInfo.docName}</span><a class="batchDl" href="javascript:void(0)" onClick="downloads(${user.cardDocInfo.id})">下载</a>
								<input type="hidden" name="cardId" value="${user.cardDocInfo.id}"/>
								<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'cardId',this)">删除</a>
							</li>
						</c:if>
					</ol>
				</li>

				<li>
					<label>上传头像：</label>
					<input id="uploadHeadPic" name="photoId" type="button" value="选择文件"  class="license_file" onclick="uploadHeadPicFunc(this)">
					<ol class="batch-ol upload_f">
						<c:if test="${not empty user.photoDocInfo}">
							<li>
								<span title="${user.photoDocInfo.docName}">${user.photoDocInfo.docName}</span><a class="batchDl" href="javascript:void(0)" onClick="downloads(${user.photoDocInfo.id})">下载</a>
								<input type="hidden" name="photoId" value="${user.photoDocInfo.id}"/>
								<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'photoId',this)">删除</a>
							</li>
						</c:if>
					</ol>
				</li>
				<li>
					<label >微信号：</label>
					<form:input path="weixin" htmlEscape="false" maxlength="20"/>																																																																																																															
				</li>
				<!-- 517 批发商认证 结束 -->
				<c:if test="${fns:getUser().company.id eq '72' }">
					<li class="clear"></li>
					<li style="margin-left: 912px;">
						<label style="width: 100px;" ><font style="color:#ff0000; padding-right:5px;">*</font>团号姓氏缩写：</label>
						<input id="oldGroupeSurname" name="oldGroupeSurname" type="hidden" value="${user.groupeSurname}">
						<form:input path="groupeSurname" htmlEscape="false" maxlength="200"/>
					</li>
				</c:if>
				<li class="clear"></li>
				<c:if test="${user.isQuauqAgentLoginUser ne '1'}">
				<c:choose>
					<c:when test="${isNewUser eq 'N' and !empty deptJobRecord}"><!-- 修改用户带出部门-职务记录 -->
						<div class="add_post_department_s_container">
							<c:forEach items="${deptJobRecord}" var="map" varStatus="status">
								<div class="add_post_department_s">
									<li class="">
										<label>
											<font style="color:#ff0000; padding-right:5px;">*</font>部门：
										</label>
										<input id="departmentId" name="departmentId" type="hidden" value="${map.dept_id }">
										<input id="departmentName" name="departmentName" readonly="readonly" type="text" value="${map.deptName }" style="">
										<a id="departmentButton" href="javascript:" class="btn departmentButton" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</li>
									<li class="">
										<label>
											<font style="color:#ff0000; padding-right:5px;">*</font>职务：
										</label>
										<select name="salerId" id="salerId">
											<option value="">请选择</option>
											<c:forEach items="${jobList}" var="jobList">
												<option value="${jobList.id }" <c:if test="${jobList.id eq map.job_id}">selected="selected"</c:if>>${jobList.name }</option>
											</c:forEach>
										</select>
									</li>
									<c:if test="${status.index==0}"><span><em></em><i style="display: none"></i></span></c:if>
									<c:if test="${status.index!=0}"><span><em></em><i ></i></span></c:if>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div class="add_post_department_s_container">
							<div class="add_post_department_s">
								<li class="">
									<label>
										<font style="color:#ff0000; padding-right:5px;">*</font>部门：
									</label>
									<input id="departmentId" name="departmentId" type="hidden" value="">
									<input id="departmentName" name="departmentName" readonly="readonly" type="text" value="" style="">
									<a id="departmentButton"  href="javascript:" class="btn departmentButton" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</li>
								<li class="">
									<label>
										<font style="color:#ff0000; padding-right:5px;">*</font>职务：
									</label>
									<select name="salerId" id="salerId">
										<option value="">请选择</option>
										<c:forEach items="${jobList}" var="jobList">
											<option value="${jobList.id }" >${jobList.name }</option>
										</c:forEach>
									</select>
								</li>
								<span><em></em><i style="display: none"></i></span>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			   </c:if>
				<input id="relationValue" name="relationValue" type="hidden" value="">
				<input id="isSave" name="isSave" type="hidden" value=""><!-- 是否点击审批监督弹出框 -->
				<li class="title-account">
					<em></em>
					<label>账号权限</label>
				</li>
				<div class="approve-optimization-container">   
					<div class="top">
						<ul id="reviewLisenceUL">
							<li id="reviewLisence">
								审批模块权限：
							</li>
							<li>
								<input lisenceValue="1"  name="is_jump_task_permit" type="checkbox" <c:if test="${not empty is_applier_auto_approve and is_applier_auto_approve}"> checked="checked"</c:if>>
								<label for="roleIdList1">自审默认通过</label>
							</li>               				
							<li>
								<input id="approSupervision"  value="" name="approSupervision" type="checkbox">
								<span style="color:#ff0000; padding-right:5px;" class="display-none">*</span>
								<label for="roleIdList1">审批督查</label>
								<input name="lisenceValue"  id="lisenceValue" type="hidden" value="">
							</li>
							<li>
								<label class="error display-none" name="approvalExaminationError">需同时选中部门、产品类型、流程名称!</label>
							</li>
						</ul> 
					</div> 
					
					<div class="bottom display-none">
						<div class="pop-department-pro-flow">
							<table>
								<thead>
									<tr>
										<th class="td-width-first">
											<label>
												<img src="${ctxStatic}/images/main-ico_approve-1_07.png"/>
													部门
											</label>
										</th>
										<th class="td-width-second">
											<label>
												<img src="${ctxStatic}/images/main-ico_approve-1_07.png"/>
													产品类型
											</label>
										</th>
										<th class="td-width-third">
											<label>
												<img src="${ctxStatic}/images/main-ico_approve-1_07.png"/>
													流程名称
											</label>
										</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="vertical-align-top-td">
											<div class="controls">
												<ul id="departTree" class="ztree" style="margin-top:3px;"></ul>
												<input name = "saveDepartTree" id="saveDepartTree" type="hidden" value=""/>
											</div>
						
										</td>
										<td class="productName vertical-align-top-td" id="productType">
											<c:forEach items="${productTypeMap}" var="productType">
												<p
													<c:forEach items="${productIdList}" var="pid">
														<c:if test="${pid==productType.key}"> class="on"</c:if>
													</c:forEach>
												>
													<input name="productType" type="checkbox" value="${productType.key}" 
														<c:forEach items="${productIdList}" var="pid">
															<c:if test="${pid==productType.key}"> checked="checked"</c:if>
														</c:forEach>
													/>
													<label code="${productType.key}">${productType.value}</label>
												</p>								
											</c:forEach>
											<input name = "saveProductType" id="saveProductType" type="hidden" value=""/>
										</td>
										<td class="workflowName vertical-align-top-td" id="processType">
											<c:forEach items="${reviewFlowTypeMap}" var="reviewFlowType">
												<p
													<c:forEach items="${reviewFlowIdList}" var="rid">
														<c:if test="${rid==reviewFlowType.key}"> class="on"</c:if>
													</c:forEach>
												>
													<input name="reviewFlowType" type="checkbox" value="${reviewFlowType.key}" 
														<c:forEach items="${reviewFlowIdList}" var="rid">
															<c:if test="${rid==reviewFlowType.key}"> checked="checked"</c:if>
														</c:forEach>
													/>
													<label code="${reviewFlowType.key}">${reviewFlowType.value}</label>
												</p>									
											</c:forEach>
											<input name = "saveReviewFlowType" id="saveReviewFlowType" type="hidden" value=""/>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
				<div>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;代替下单：</label>
					<input  style="width: 77px;" onclick="jbox_accountSelect();" class="btn btn-primary" value="选择账号" type="button">
					<input type="hidden" name="substituteOrder" id="substituteOrder" value="${user.substituteOrder }">
					<span class="seach_checkbox" name="vendorOperatorShow">
						<c:forEach var="substituteOne" items="${chosedSubstitute }">						
							<a value="${substituteOne.id }">${substituteOne.name }</a>
						</c:forEach>
					</span>
				</div>
				
				<div>
					<label>实时连通渠道权限：</label>
					<input id="quauqBookOrderPermission" name="quauqBookOrderPermission" type="checkbox" <c:if test="${not empty user.quauqBookOrderPermission and user.quauqBookOrderPermission eq 1}">checked="checked"</c:if>>
					<label >报名</label>
					<%--暂时注释518需求--%>
					<%--<input id="hasPricingStrategyPermission" name="hasPricingStrategyPermission" type="checkbox" <c:if test="${not empty user.hasPricingStrategyPermission and user.hasPricingStrategyPermission eq 1}">checked="checked"</c:if>>
					<label >设定定价策略</label>--%>
				</div>
              
				<li class="clear"></li>      

				<c:if test="${not empty user.id}">
					<li style="clear: both;">
						<label>创建时间：</label>
						<fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/>
					</li>
					<li style="clear: both; width:100%">
						<label>最后登录：</label>
						IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/>
					</li>
				</c:if>
			</ul>
			<div class="sys_adduser_btn">
				<%--<input id="btnCancel" class="btn btn-primary" type="button" value="返&nbsp;&nbsp;&nbsp;回" onClick="history.go(-1)"/>--%>
				<input class="btn" type="button" value="返&nbsp;&nbsp;&nbsp;回" onClick="history.go(-1)"/>
				<shiro:hasPermission name="sys:user:edit">
					<input id="btnSubmit" onclick="saveUser();" class="btn btn-primary" type="button" value="保&nbsp;&nbsp;&nbsp;存"/>&nbsp;
				</shiro:hasPermission>
			</div>
		</form:form>
	</div>
        
        
<script>
	function add_onemore_money() {
		var aa = $('#add_onemore_moneybj').val();
        $('#add_onemore_money').parent().after('<div class="kongr" id="del_onemore_moneyk' + aa + '"></div><div class="mod_information_d2" id="del_onemore_money1' + aa + '"><label>&nbsp;</label> <select><option>请选择</option><option>单团</option></select></div><div class="mod_information_d2" id="del_onemore_money2' + aa + '"><label>优惠金额: ￥</label> <input name="name" type="text" value="" maxlength="50"/> <a href="javascript:;" onclick="del_onemore_money(' + aa + ')"><img src="./images/button_del.gif" /></a></div>');
        var bb = Number(aa) + Number(1);
        $('#add_onemore_moneybj').val(bb);
	}
    function del_onemore_money(t) {
        $('#del_onemore_money1' + t).remove();
        $('#del_onemore_money2' + t).remove();
        $('#del_onemore_moneyk' + t).remove();
    }
    var $node;
    // S 201 选择账户弹窗
	function jbox_accountSelect() {
    	var $pop = $.jBox($("#select_account_pop_o").html(), {
        	title    : "选择账号", buttons: {'保存': 1,'关闭':2}, submit: function (v, h, f) {
            	if (v == "1") {
					$pop.find("input[name='beselected']:checked").each(function(){
						var $checkedTr = $(this).parent().parent();
						var userid = $checkedTr.find("input[name=beselected]").attr("travelerid");
						var name=$checkedTr.find("td[name=userName]").text();
						var same ='';
						$("span[name=vendorOperatorShow]").find("a").each(function(){
           					if($(this).text() == name){
								same=1;
							}
						});
          				if(same==''){
				            $("span[name=vendorOperatorShow]").append('<a value="' + userid + '">' + name + '</a>');
				        }
					});
			        // 给代替人字段赋值
			        setSubstitute();
				}
			},loaded:function(h, f){
				$node = h.find("#substituteTable");
				searchUserInfo(null);
			}, height: '500', width: 900}
		);
    	$pop.find('input[id="select1"]').on("click",function(){
            if($pop.find('input[id="select1"]').attr('checked')){
	            $pop.find('input[id="select1"]').parent().parent().parent().parent().find("input[name='beselected']").attr("checked",true);
	        }else{
	             $pop.find('input[id="select1"]').parent().parent().parent().parent().find("input[name='beselected']").attr("checked",false);
	        }
        });
	    if($pop.find("input[name='allChk']").attr("checked")){
	        $pop.find('input[name="beselected"]').each(function(){
	            $(this). attr("checked", 'true');
	        });
	    }else{
	        $pop.find('input[type="checkbox"]').removeAttr("checked");
	    }
    	inquiryCheckBOX();
	}
	
	function setSubstitute() {
		var substituteStr = "";
		$("span[name=vendorOperatorShow]").children().each(function(index, element){
			if(index > 0){
				substituteStr += "," + $(element).attr("value");
			} else {
				substituteStr += $(element).attr("value");
			}
		});
		$("#substituteOrder").val(substituteStr);
	}
	
	function inquiryCheckBOX() {
		$(".seach_checkbox").on("click", "em", function () {
			$(this).parent().remove();
			// 给代替人字段赋值
			setSubstitute();
		})
		$(".seach_checkbox").on("hover", "a", function () {
			$(this).append("<em></em>");
		})
		$(".seach_checkbox").on("mouseleave", "a", function () {
			$(this).parent().find('em').remove();
		})
	}	
	
	//点击“选择账号”、“搜索”，发送ajax进入后台查询，返回json再解析
	function searchUserInfo(obj){
		var $baseTds, subLoginName, subUserName, subRole;
		if(obj != undefined && obj != null){
			$baseTds = $(obj).parent().siblings();
			subLoginName = $baseTds.find("#subLoginName").val();
			subUserName = $baseTds.find("#subUserName").val();
			subRole = $baseTds.find("#subRole").val();
		} else {
			subLoginName = '';
			subUserName = '';
			subRole = null;
		}
		$.ajax({
			type: "POST",
            url: "${ctx}/sys/user/getSubstituteList",
            data: {
              subLoginName : subLoginName,
              subUserName : subUserName,
              subRole : subRole
            },
			success: function(resultMap){
				if(resultMap && resultMap.flag == 'success'){
					if(!parseSubstituteJson2Tbody(resultMap.data)){
						top.$.jBox.tip("获取账号列表失败！");
					};
				} else {
					top.$.jBox.tip("获取账号列表失败！");
				}
			}
		});
	}
	
	// 依据json解析成tbody内容展示
	function parseSubstituteJson2Tbody(subJson){
		var html = "";
		if(subJson == undefined || subJson == null){
			return false;
		}
		if(subJson == ''){
			$node.empty().append(html);
			return true;
		}
		var json = eval(subJson);
		// json数组个数
		var jsonLength = json.length;
		// 获取已经选择的账号
		var substituteStr = $("#substituteOrder").val();
		var subStituteArray = new Array();
		if (substituteStr != undefined && substituteStr != null && substituteStr != "") {
			subStituteArray = substituteStr.split(",");
		}
		// 判断为空
		if (jsonLength && jsonLength != 0) {
			// 循环获取html组合
			for (var i = 0; i < jsonLength; i++) {
			    var isChecked = false;
				for ( var j = 0; j < subStituteArray.length; j++) {
					if (json[i].id == Number(subStituteArray[j])) {
						isChecked =  true;
						// 移除元素 TODO
						break;
					}
				}
				// 序列值
				var indexVal = i + 1;
				html += "<tr>";
				html += "<td><input name='beselected' type='checkbox' class='tdCheckBox tc' travelerid='" + json[i].id + "' name='index' onclick='' style='margin-top: 5px;'";
				if (isChecked) {				
					html += " checked='checked' disabled='disabled' ";
				}
				html += ">" + indexVal + "</td>";
				// 登录名
				html += "<td name='loginName' class='tc'><span>" + json[i].subLoginName + "</span></td>";
				// 姓名
				html += "<td name='userName' class='tc'><span>" + json[i].subUserName + "</span></td>";
				// 角色
				html += "<td name='role' class='tc'><span>" + json[i].subRole + "</span></td>";
				html += "</tr>";
				
			}
			$node.empty().append(html);
		}
        
		return true;
	}
	
	// E 201 选择账户弹窗
</script>
        
	<!--S 201 选择账户弹窗-->
	<div id="select_account_pop_o" class="display-none">
		<div class="select_account_pop" style="padding:20px">
			<table style="width:100%">
				<tr>
					<td>登录名：</td>
					<td><input type="text" value="" name="subLoginName" id="subLoginName" class="jboxinput"></td>
					<td>姓名：</td>
					<td><input type="text" value="" name="subUserName" id="subUserName" class="jboxinput"></td>
					<td>角色：</td>
					<td>
						<select name="subRole" id="subRole" class="jboxinput">
							<option selected="selected" value="">请选择</option>
							<c:forEach var="role" items="${allRoles }">
								<option value="${role.id }">${role.name }</option>
							</c:forEach>
 						</select>
					</td>
					<td> <input id="btnSubmit" onclick="searchUserInfo(this);" class="btn btn-primary" value="搜索" type="button"></td>
				</tr>
			</table>
			<div style="height:358px;overflow-y:auto;">
				<table class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th><input id="select1" name="allChk" type="checkbox">选择</th>
							<th>登录名</th>
							<th>姓名</th>
							<th>角色</th>
						</tr>
					</thead>
					<tbody id="substituteTable">
						<!-- 由js解析json生成 -->
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!--E 201 选择账户弹窗-->
    
</body>
</html>