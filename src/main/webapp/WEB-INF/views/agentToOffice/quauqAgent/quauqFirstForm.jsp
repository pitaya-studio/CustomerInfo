<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>实时连通渠道账号添加</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">

$(function(){
	$("#agentSalerUser").comboboxInquiry();
	//输入渠道公司名称后，自动输出其首字母 ， 且第一个字符不能为特殊字符 
	$("#agentName").on('blur',function(){
		$.ajax({
			type:"POST",
			url: "${ctx}/agent/manager/firstLetter",
			data: {"agentName":$('#agentName').val()},
			datatype:"json",
			success: function(data){
				$("#agentFirstLetter").val(data);
			}
		});
	 });
	
	//渠道首字母统一为大写, 且只能为数字或者英文字符 
	$("#agentFirstLetter").on('blur',function(){
		var agentFirstLetterUpper = $("#agentFirstLetter").val().toUpperCase();
		$("#agentFirstLetter").val(agentFirstLetterUpper);
	});
	
	//所属地区 国家、省（直辖市）、市区联动下拉框
	$("li.countryCityArea select").change(function(){
			var parentId = $(this).val();
			var url = "${ctx}"+"/agent/manager/getAreaInfoById/"+parentId;
			var currentClass = $(this).attr('class');
			if(parentId.length>0){
				$.ajax({
					   type: "POST",
					   url: url,
					   dataType:"json",
					   success: function(msg){
					     	if(currentClass=="country"){
			     		  		$("li.countryCityArea .province").empty();
			     		  		$("li.countryCityArea .city").empty();
			     		  		$("li.countryCityArea .province").append("<option value=''>省(直辖市)</option>");
			     		  		$("li.countryCityArea .city").append("<option value=''>市(区)</option>");
					     		$.each(msg,function(i, n){
					     		  		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";

					     		  		$("li.countryCityArea .province").append(optionStr);
					     		});
					     	}
				     		if(currentClass=="province"){
				     			$("li.countryCityArea .city").empty();
				     			$("li.countryCityArea .city").append("<option value=''>市(区)</option>");
				     			$.each(msg,function(i, n){
						     		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
					     			$("li.countryCityArea .city").append(optionStr);
				     			});
				     		}
					   }
					});
			}else{
		     	if(currentClass=="country"){
	 		  		$("li.countryCityArea .province").empty();
	 		  		$("li.countryCityArea .city").empty();
	 		  		$("li.countryCityArea .province").append("<option value=''>省(直辖市)</option>");
	 		  		$("li.countryCityArea .city").append("<option value=''>市(区)</option>");
		     	}
	     		if(currentClass=="province"){
	     			$("li.countryCityArea .city").empty();
	     			$("li.countryCityArea .city").append("<option value=''>市(区)</option>");
	     		}
			}
	});
	// 公司地址 国家、省（直辖市）、市区联动下拉框
	$("li.countryCityAddress select").change(function(){
		var parentId = $(this).val();
		var url = "${ctx}"+"/agent/manager/getAreaInfoById/"+parentId;
		var currentClass = $(this).attr('class');
		if(parentId.length>0){
			$.ajax({
				   type: "POST",
				   url: url,
				   dataType:"json",
				   success: function(msg){
				     	if(currentClass=="country"){
		     		  		$("li.countryCityAddress .province").empty();
		     		  		$("li.countryCityAddress .city").empty();
		     		  		$("li.countryCityAddress .province").append("<option value=''>省(直辖市)</option>");
		     		  		$("li.countryCityAddress .city").append("<option value=''>市(区)</option>");
				     		$.each(msg,function(i, n){
				     		  		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
				     		  		$("li.countryCityAddress .province").append(optionStr);
				     		});
				     	}
			     		if(currentClass=="province"){
			     			$("li.countryCityAddress .city").empty();
			     			$("li.countryCityAddress .city").append("<option value=''>市(区)</option>");
			     			$.each(msg,function(i, n){
					     		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
				     			$("li.countryCityAddress .city").append(optionStr);
			     			});
			     		}
					}
				});
		}else{
	     	if(currentClass=="country"){
 		  		$("li.countryCityAddress .province").empty();
 		  		$("li.countryCityAddress .city").empty();
 		  		$("li.countryCityAddress .province").append("<option value=''>省(直辖市)</option>");
 		  		$("li.countryCityAddress .city").append("<option value=''>市(区)</option>");
	     	}
     		if(currentClass=="province"){
     			$("li.countryCityAddress .city").empty();
     			$("li.countryCityAddress .city").append("<option value=''>市(区)</option>");
     		}
		}
	});
	
	// 初始需要更改第一联系人 的input name
	reIndexContact();
	
	$("#agentParent").comboboxInquiry();
	lockSelect(); 
});	
	
	/**
	 * 增删联系人
	 */
	 $(document).on('click', '.add_post_department_s em[class=add]', function () {
         var $newOne = $(this).parent().parent().clone();
         $newOne.find('input').val('');
         $newOne.find('i').show();
         $newOne.find('textarea').val('');
         $('.add_post_department_s_container').append($newOne);
         reIndexContact();
     })
     $(document).on('click', '.add_post_department_s i', function () {
         $(this).parent().parent().remove();
         reIndexContact();
     })
     
     /**
	 * 变更 联系人的 input name 序号（初始化、增加、删除时调用）
	 */
	function reIndexContact(){
		$(".add_post_department_s_container").find(".add_post_department_s").each(function(index, element){
			$(element).find("input[name*=contactName]").parent().parent().find("span[name=contactIndex]").html(Number(index + 1));
			$(element).find("input[name*=contactName]").attr("name", "contacts.supplyContactses[" + index + "].contactName");
			$(element).find("input[name*=contactMobile]").attr("name", "contacts.supplyContactses[" + index + "].contactMobile");
			$(element).find("input[name*=contactPhone]").attr("name", "contacts.supplyContactses[" + index + "].contactPhone");
			$(element).find("input[name*=contactFax]").attr("name", "contacts.supplyContactses[" + index + "].contactFax");
			$(element).find("input[name*=contactQQ]").attr("name", "contacts.supplyContactses[" + index + "].contactQQ");
			$(element).find("input[name*=contactEmail]").attr("name", "contacts.supplyContactses[" + index + "].contactEmail");
			$(element).find("input[name*=wechatCode]").attr("name", "contacts.supplyContactses[" + index + "].wechatCode");
			$(element).find("textarea[name*=remarks]").attr("name", "contacts.supplyContactses[" + index + "].remarks");
		});
	}

	/**
	 * 提交
	 */
	function submitForm(){
		// 必填校验
		var agentName = $("#agentName").val();
		var brandVal = $("#agentBrand").val();
		var loginName = $("#quauqAgentUserLoginName").val();
		var isContactFilled = true;
		var contactIndex = 0;
		$("input[name*=contactName]").each(function(index, element){
			if($(element).val() == undefined || $(element).val() == null || $(element).val() == "") {
				isContactFilled = false;
				contactIndex = Number(index + 1);
				return false;
			}
		});
		var isContactMobileFilled = true;
		if (isContactFilled) {
			$("input[name*=contactMobile]").each(function(index, element){
				if($(element).val() == undefined || $(element).val() == null || $(element).val() == "") {
					isContactMobileFilled = false;
					contactIndex = Number(index + 1);
					return false;
				}
			});
		}
		var belongArea = $("select[name='agentinfo.belongsArea']").val();
		var agentAddress = $("select[name='agentinfo.agentAddress']").val();
		var agentAddressStreet = $("input[name='agentinfo.agentAddressStreet']").val();
		
		var b = true;
		if(agentName==""||agentName==null){
			b = false;
			top.$.jBox.info("渠道名称为必填项", "警告");
			return;
		}
		if(brandVal==""||brandVal==null){
			b = false;
			top.$.jBox.info("渠道品牌为必填项", "警告");
			return;
		}
		if(loginName=="" || loginName==null){
			b = false;
			top.$.jBox.info("登录名为必填项", "警告");
			return;
		}
		if(!isContactFilled){
			b = false;
			top.$.jBox.info("联系人" + contactIndex + "姓名为必填项", "警告");
			return ;
		}
		if(!isContactMobileFilled){
			b = false;
			top.$.jBox.info("联系人" + contactIndex + "电话为必填项", "警告");
			return ;
		}
		if(!belongArea){
			b = false;
			top.$.jBox.info("所属地区为必填项", "警告");
			return ;
		}
		if(!agentAddress && !agentAddressStreet){
			b = false;
			top.$.jBox.info("公司地址为必填项", "警告");
			return ;
		}
		// 类型必填
		var agentType = $('#agentType').val();
		if(agentType=="" || agentType==null){
			b = false;
			top.$.jBox.info("类型为必选项", "警告");
			return ;
		}
		//禁用按钮 防止反复提交
		$("#btnSubmit").attr({"disabled":"disabled"});
		// 提交
		var data = $("#inputForm").serialize();
		var ctx = $("#ctx").val();
		$.ajax({
			type: "POST",
			async:false,
			url: ctx + "/quauqAgent/manage/saveFirstForm",
			data : data,
			success : function(msg){
				if (msg.result == true) {
					top.$.jBox.tip('账号生成并关联成功', 'info');
				}
				window.location.href = ctx + msg.url;
			}
		});
	}
	
	/**
	 * 校验登录是否重复
	 */
	function validLoginName(obj) {
		var nowLoginName = $(obj).val();
		if(nowLoginName == undefined || nowLoginName == null || nowLoginName == ""){
			return;
		}
		var validURL = "${ctx}/sys/user/checkQuauqAgentLoginName?loginName=" + encodeURIComponent(nowLoginName) + "&dom=" + Math.random();
		$.ajax({
		   type: "POST",
		   url: validURL,
		   success: function(msg){
		     	if (!msg || msg == "false") {
		     		top.$.jBox.info("登录名已存在！", "警告");
					$(obj).val("");
				}
			}
		});
	}
	
	//锁定上级关系下拉框
	function lockSelect() {
		
		var value = $('#agentType').val();

		if (value == "1") {
			$('.custom-combobox-input').removeAttr("disabled");
		} else {
			$('.custom-combobox-input').val("请选择");
			$('.custom-combobox-input').attr("disabled","true");
		}
	}
		
	function spellContactPhone(obj) {
		var $this = $(obj);
		var phone = $this.parent().parent().find("input[name*=contactPhone]");
		var val = phone.val();

//		if (val.indexOf('-') == -1) {
//			val = '-';
//		}

		var arr = new Array();
		arr = val.split('-');
		if ($this.attr("name") == 'contactCodePhone') {
			phone.val($this.val() + '-' + arr[1]);
		} else if ($this.attr("name") == 'contactNumberPhone') {
			phone.val(arr[0] + '-' + $this.val());
		}
	}
	
	function spellContactFax(obj) {
		var $this = $(obj);
		var fax = $this.parent().parent().find("input[name*=contactFax]");
		var val = fax.val();
		if (val.indexOf('-') == -1) {
			val = '-';
		}
		var arr = new Array();
		arr = val.split('-');
		if ($this.attr("name") == 'contactCodeFax') {
			fax.val($this.val() + '-' + arr[1]);
		} else if ($this.attr("name") == 'contactNumberFax') {
			fax.val(arr[0] + '-' + $this.val());
		}
	}
</script>
</head>
<body>
<style type="text/css">label{ cursor:inherit;}
.ydbz_qd .ydbz_qd_close {
	height:auto;
	margin-top:-13px;
}

.ydbz_qd .ydbz_qd_close li{
	margin:0;
	width: 34%;
}
.custom-combobox input{
	background: #fff;
}
.custom-combobox a{
	background: #fff;
}
</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
	&nbsp;&nbsp;QUAUQ后台&nbsp;&nbsp;>&nbsp;&nbsp;渠道账号添加<br/><br/>
	
	<div class="sysdiv">
    	<form:form  method="post" action="${ctx}/quauqAgent/manage/saveFirstForm" class="form-horizontal" id="inputForm" name="inputForm" >
			<input type="hidden" name="token" value="${token }">
			<input type="hidden" name="mobileUserId" value="${mobileUser.id}"/>
			<input type="hidden" id="ctx" name="ctx" value="${ctx}"/>
			<ul class="sys_adduser sys_adduserL  ydbz_qd">
				<li>
					<label for="agentName"><em class="xing">*</em> 渠道名称：</label>
                	<span>
		            	<input id="agentName" name="agentinfo.agentName" type="text" maxlength="50" value="${mobileUser.agentName}" <c:if test="${not empty mobileUser}">readonly="readonly"</c:if>/>
		               <!-- onkeyup="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
		             	 	onkeydown="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
		              	 	onafterpast="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"  -->
		        	</span>
		        	<!-- 首英文名（js获取、隐藏） -->
		        	<input id="agentNameShort" name="agentinfo.agentNameShort" type="hidden" maxlength="50">
		        	<!-- 首字母（js获取、隐藏） -->
		        	<input id="agentFirstLetter" name="agentinfo.agentFirstLetter" type="hidden" maxlength="1" value="${agentinfo.agentFirstLetter}">
				</li>
				<li>
                	<label for="abbreviation">简称：</label>
                	<input id="abbreviation" name="agentinfo.abbreviation" type="text" maxlength="30">
                </li>
               
                <li>
                	<label for="agentBrand"><em class="xing">*</em>渠道品牌：</label>
                	<input id="agentBrand" name="agentinfo.agentBrand" type="text" maxlength="50">
                </li>
                 <li class="clear"></li>
                <li>
                	<label for="quauqAgentUserLoginName"><em class="xing">*</em>登录名：</label>
                	<input id="quauqAgentUserLoginName" name="quauqAgentUserLoginName" value="" maxlength="50" type="text"  onblur="validLoginName(this);">
                	<input id="newPassword" name="newPassword" type="hidden" value="0000" maxlength="50" minlength="3" />
                </li>
                
                
                <li>
                	<label for="agentType"><em class="xing">*</em>类型：</label>
                	<select id="agentType" name="agentinfo.agentType" onchange="lockSelect()">
                		<option value="">请选择</option>
                		<c:forEach items="${customerTypeList }" var="customerType">
                			<option value="${customerType.value }">${customerType.name }</option>
                		</c:forEach>
                	</select>
                </li>
                <li>
                	<label for="agentParent">上级关系：</label>
                	<select id="agentParent" name="agentinfo.agentParent">
                		<option value="-1">请选择</option>
                		<c:forEach items="${agentParentList }" var="agentParent">
                			<option value="${agentParent.id }">${agentParent.agentName }</option>
                		</c:forEach>
                	</select>
                </li>
                <li class="clear"></li>
                
                <div class="add_post_department_s_container">
					<div class="add_post_department_s">
                        <li class="">
                            <label><em class="xing">*</em>联系人<span name="contactIndex"></span>：</label>
                            <div class="input-append">
                            	<input style="border-radius: 4px" name="contactName" type="text" maxlength="9" value="${mobileUser.name}" <c:if test="${not empty mobileUser}">readonly="readonly"</c:if>>
                            </div>
                        </li>
                        <li class="">
                            <label><em class="xing">*</em>联系人电话：</label>
                            <input name="contactMobile" type="text" maxlength="20" value="${mobileUser.telePhone}" <c:if test="${not empty mobileUser}">readonly="readonly"</c:if>/>
                        </li>
                        <li class="" style="width: 230px;">
                        	<div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')" style="display: inline-block;">展开全部</div>
                        </li>
                        <span><em class="add" style="display: none"></em><i style="display: none"></i></span>
                        <li flag="messageDiv" class="ydbz_qd_close" style="display: none;background-color: white;">
			           		<ul class="view" style="width: 84%;">
				                <li style="background-color: white;">
				                	<label>固定电话：</label>
				                	<span>

				                		<input name="contactCodePhone" type="text" maxlength="20" style="width: 40px;" value="${mobileUser.areaCode}" onblur="spellContactPhone(this);"<c:if test="${not empty mobileUser}">readonly="readonly"</c:if>/>
				                		<span>-</span>
										<%--需求调整，座机号码允许输入20位 ymx Start--%>
				                		<input name="contactNumberPhone" type="text"  maxlength="20"
				                			onblur="spellContactPhone(this);" style="width: 91px;" value="${mobileUser.phone}" <c:if test="${not empty mobileUser}">readonly="readonly"</c:if>/>
				                		<%--需求调整，座机号码允许输入20位 ymx End--%>
									</span>
				                	<input id="contactPhone" name="contactPhone" value="-" type="hidden"/>
				                </li>
				                <li style="background-color: white;">
				                	<label style="width: 90px;">QQ：</label>
				                	<input name="contactQQ" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" 
				                		onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="14"/>
				                </li>
				                <li style="background-color: white;">
				                	<label>传真：</label>
				                	<span>
				                		<input name="contactCodeFax" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" 
				                			onafterpaste="this.value=this.value.replace(/\D/g,'')" 
				                			onblur="spellContactFax(this);" maxlength="6" style="width: 40px;"/>
				                		<span>-</span>
				                		<input name="contactNumberFax" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" 
				                			onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="13" 
				                			onblur="spellContactFax(this);" style="width: 91px;"/>
				                	</span>
				                	<input id="contactFax" name="contactFax" value="-" type="hidden"/>
				                </li>
				                <li style="background-color: white;">
				                	<label style="width: 90px;">Email：</label>
				                	<input name="contactEmail" type="text" maxlength="50"/>
				                </li>
								<li style="background-color: white;">
									<label >微信：</label>
									<input name="wechatCode" type="text" maxlength="20" value="${mobileUser.wechatCode}" <c:if test="${not empty mobileUser}">readonly="readonly"</c:if>/>
								</li>
								<li style="background-color: white;display: table;height: auto;">
									<label style="display: table-cell;vertical-align: top;width: 90px;">描述：</label>
									<textarea rows="3" cols="30" name="remarks" maxlength="200" style="margin-left: 9px;"></textarea>
								</li>
			            	</ul>
			        	</li>
					</div>
                </div>
                <li class="clear"></li>
                
                <li class="liWidth countryCityArea">
                	<label><em class="xing">*</em>所属地区：</label>
                    <select class="country" name="agentinfo.belongsArea">
	                	<option value="">国家</option>
	                	<option value="${countryId }">中国</option>
	                	<c:forEach  items="${areaMap}" var="aa">
	                		<option value="${aa.key}">${aa.value}</option>
	                	</c:forEach>
	                </select>
                    <select class="province" name="agentinfo.belongsAreaProvince">
	                	<option value="">省(直辖市)</option>
	                </select>
	                <select class="city" name="agentinfo.belongsAreaCity">
	                 	<option value="">市(区)</option>
	                </select>
				</li>
                <li class="clear"></li>
                <li class="liWidth countryCityAddress">
                	<label><em class="xing">*</em>公司地址：</label>
                    <select class="country" name="agentinfo.agentAddress">
	                	<option value="">国家</option>
	                	<option value="${countryId }">中国</option>
	                	<c:forEach  items="${areaMap}" var="aa">
	                		<option value="${aa.key}">${aa.value}</option>
	                	</c:forEach>
	                </select>
                    <select class="province" name="agentinfo.agentAddressProvince">
	                	<option value="">省(直辖市)</option>
	                </select>
	                <select class="city" name="agentinfo.agentAddressCity">
	                 	<option value="">市(区)</option>
	                </select>
                    <input type="text" maxlength="100" name="agentinfo.agentAddressStreet" >
            	</li>
            	<li class="clear"></li>
            	<li><input type="checkbox" name="differenceRights" value = "1"/>开启下单权限</li>
            	<!--  -->
            	<!-- 临时策略（待删除）-->
            	<li><input type="checkbox" name="lingxianwangshuai" value = "1"/>领先（王帅）</li>
            	<li class="clear"></li>
			</ul>
			
			<div class="sys_adduser_btn">
				<input id="btnCancel" class="btn btn-primary" value="返&nbsp;&nbsp;&nbsp;回" onclick="history.go(-1)" type="button">
				<input id="btnSubmit" onclick="submitForm();" class="btn btn-primary" value="保&nbsp;&nbsp;&nbsp;存" type="button">&nbsp;
			</div>
			
		</form:form>
	</div>

</body>
</html>
