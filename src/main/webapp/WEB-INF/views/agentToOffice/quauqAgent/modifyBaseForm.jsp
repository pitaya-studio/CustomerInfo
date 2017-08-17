<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<title>渠道管理-基本信息
	<c:if test="${isDetail}">详情</c:if>
	<c:if test="${!isDetail }">修改</c:if>
</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
	// 是否是详情页面
	var isDetail = '${isDetail}';
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
		// 修改页面没有“关闭”按钮
		$("#btnClose").hide();
		// 给联系人改input的序号name
		reIndexContact();
		// 登录名不允许修改
		$("#quauqAgentUserLoginName").attr("readonly", true);
		if(isDetail && isDetail == "true"){
			$("input").attr("readonly", true);
			$("#btnClose").attr("readonly", false);
			$("select").attr("disabled", true);
			$("#btnClose").show();  //修改页面有“关闭”按钮
			$("#btnSubmit").hide();  //修改页面没有“保存”按钮
			$("#btnCancel").hide();  //修改页面没有“返回”按钮
			$("#btnClose").addClass("blue");
		}
	
	$("#agentParent").comboboxInquiry();
	lockTheSelect();
	});	
	
	/**
	 * 提交
	 */
	function submitForm(){
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
		var agentType = $('#agentType').val();
		if(agentType == "" || agentType == null) {
			b = false;
			top.$.jBox.info("渠道类型为必选项", "警告");
			return ;
		}
		$("#inputForm").submit();
		
	}
	
	/**
	 * 增删联系人
	 */
	 $(document).on('click', '.add_post_department_s em[class=add]', function () {
	 	if(isDetail && isDetail == "true"){
	 		return;
	 	}
         var $newOne = $(this).parent().parent().clone();
         $newOne.find('input').val('');
         $newOne.find('i').show();
         $newOne.find('textarea').val('');
         $('.add_post_department_s_container').append($newOne);
         reIndexContact();
     });
     $(document).on('click', '.add_post_department_s i', function () {
     	if(isDetail && isDetail == "true"){
	 		return;
	 	}
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
			$(element).find("input[name*=contactId]").attr("name", "contacts.supplyContactses[" + index + "].id");
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
	 * 锁定上级关系下拉框
	 */
	function lockSelect() {
		
		var value = $('#agentType').val();

		if (value == "1") {
			$('.custom-combobox-input').removeAttr("disabled");
		} else {
			$('.custom-combobox-input').val("请选择");
			$('.custom-combobox-input').attr("disabled", "true") ;
		}
	}
	
	function lockTheSelect() {
		
		var value = $('#agentType').val();

		if (value == "1") {
			$('.custom-combobox-input').removeAttr("disabled");
		} else {
			$('.custom-combobox-input').attr("disabled","true") ;
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
	
	function openAllParams(obj) {
		
		var $this = $(obj);
		var div = $this.parent().parent();
		var phone = (div.find("#contactPhone").val() == '') ? '-' : div.find("#contactPhone").val();
		var fax = (div.find("#contactFax").val() == '') ? '-' : div.find("#contactFax").val();
		
		var array = new Array();
		array = phone.split('-');
		div.find("input[name*=contactCodePhone]").val(array[0]);
		div.find("input[name*=contactNumberPhone]").val(array[1]);
		
		array = fax.split('-');
		div.find("input[name*=contactCodeFax]").val(array[0]);
		div.find("input[name*=contactNumberFax]").val(array[1]);
		
		boxCloseOnAdd(obj, '', '2');
	}
</script>
</head>
<body>
<style type="text/css">
label { 
	cursor:inherit;
}
.ydbz_qd .ydbz_qd_close {
	height:auto;
	margin-top:-13px;
}

.ydbz_qd .ydbz_qd_close li {
	margin:0;
	width: 34%;
}
.sys_adduser li {
	min-width: 300px;
}
#inputForm .label-Uprelation {
	text-align: left;
	overflow: inherit;
	width: 60%;
	word-break:break-all;
}

</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
	&nbsp;&nbsp;QUAUQ后台&nbsp;&nbsp;>&nbsp;&nbsp;基本信息<c:if test="${isDetail eq true}">详情</c:if><c:if test="${isDetail ne true }">修改</c:if><br/><br/>
	
	<div class="sysdiv">
    	<form:form  method="post" action="${ctx}/quauqAgent/manage/updateBase" class="form-horizontal" id="inputForm" name="inputForm" >

			<input type="hidden" id="isDetail" name="isDetail" value="${isDetail}">
			<ul class="sys_adduser sys_adduserL  ydbz_qd">
				<li>
					<label for="agentName"><em class="xing">*</em> 渠道名称：</label>
                	<span>
		            	<input id="agentName" name="agentinfo.agentName" type="text" maxlength="50" value="${agentinfo.agentName }"/>
		             		<!-- onkeyup="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
		             	 	onkeydown="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
		              	 	onafterpast="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"> -->
		        	</span>
		        	<input id="id" name="agentinfo.id" type="hidden" value="${agentinfo.id }">
		        	<!-- 首英文名（js获取、隐藏） -->
		        	<input id="agentNameShort" name="agentinfo.agentNameShort" type="hidden" maxlength="50">
		        	<!-- 首字母（js获取、隐藏） -->
		        	<input id="agentFirstLetter" name="agentinfo.agentFirstLetter" type="hidden" maxlength="1" value="${agentinfo.agentFirstLetter}">
				</li>
				<li><label for="abbreviation">简称 ：</label>
					<input id="abbreviation" name="agentinfo.abbreviation" type="text" maxlength="30" value="${agentinfo.abbreviation}"/> 
				</li>
                <li>
                	<label for="agentBrand"><em class="xing">*</em>渠道品牌：</label>
                	<input id="agentBrand" name="agentinfo.agentBrand" type="text" maxlength="50" value="${agentinfo.agentBrand }">
                </li>
                <li class="clear"></li>
                <li>
                	<label for="quauqAgentUserLoginName"><em class="xing">*</em>登录名：</label>
                	<input id="quauqAgentUserLoginName" name="quauqAgentUserLoginName" maxlength="50" type="text" value="${agentinfo.loginName }">
                	<input id="loginId" name="loginId" type="hidden" value="${agentinfo.loginId}" />
                </li>
                
                
                <li>
                	<label for="agentType"><em class="xing">*</em>类型：</label>
                	<select id="agentType" name="agentinfo.agentType" onchange="lockSelect()">
                		<option value="" <c:if test="${agentinfo.agentType eq '-1' }">selected</c:if>>请选择</option>
                		<c:forEach items="${customerTypeList }" var="customerType">
                			<option value="${customerType.value }" <c:if test="${agentinfo.agentType eq customerType.value }">selected</c:if>>${customerType.name }</option>
                		</c:forEach>
                	</select>
                </li>
                <li style = "min-width: 336px">
                	<label for="agentParent">上级关系：</label>
                	<%-- <select id="agentParent" name="agentinfo.agentParent">
                		<option value="-1">请选择</option>
                		<c:forEach items="${agentParentList }" var="agentParent">
                			<option value="${agentParent.id }" <c:if test="${agentinfo.agentParent eq agentParent.id }">selected</c:if>>${agentParent.agentName }</option>
                		</c:forEach>
                	</select> --%>
                	<label class="label-Uprelation">${agentParent }</label>
                </li>
                <li class="clear"></li>

                <div class="add_post_department_s_container">
                	<c:forEach var="contact" items="${supplyContactsList}" varStatus="varStatus">
					<div class="add_post_department_s">
                        <li class="">
                            <label><em class="xing">*</em>联系人<span name="contactIndex"></span>：</label>
                            <div class="input-append">
                            	<input style="border-radius: 4px" name="contactName" type="text" maxlength="9" value="${contact.contactName }">
                            	<input type="hidden" name="contactId" id="contactId" value="${contact.id }" >
                            </div>
                        </li>
                        <li class="">
                            <label><em class="xing">*</em>联系人电话：</label>
                            <input name="contactMobile" type="text" maxlength="20" value="${contact.contactMobile }" >
                        </li>
                        <li class="" style="width: 230px;">
                        	<div class="zksx modify-order boxCloseOnAdd" onclick="openAllParams(this);" style="display: inline-block;">展开全部</div>
                        </li>
                        <span>
                        	<em class="add" style="display: none"></em>
                        	<c:if test="${varStatus.count eq 1 }">
	                        	<i style="display: none;"></i>
                        	</c:if>
                        	<c:if test="${varStatus.count ne 1 }">
	                        	<i style="display: inline;"></i>
                        	</c:if>
                        </span>
                        <li flag="messageDiv" class="ydbz_qd_close" style="display: none;background-color: white;">
			           		<ul class="view" style="width: 84%;">
				                <li style="background-color: white;">
				                	<label>固定电话：</label>
				                	<span>
				                		<input name="contactCodePhone" type="text" maxlength="20" style="width: 40px;" onblur="spellContactPhone(this);"/>
				                		<span>-</span>
										<%--需求调整，座机号码允许输入20位 ymx Start--%>
				                		<input name="contactNumberPhone" type="text" maxlength="20" onblur="spellContactPhone(this);" style="width: 91px;"/>
										<%--需求调整，座机号码允许输入20位 ymx End--%>
				                	</span>
				                	<input id="contactPhone" name="contactPhone" value="${contact.contactPhone }" type="hidden"/>
				                </li>
				                <li style="background-color: white;">
				                	<label style="width: 90px;">QQ：</label>
				                	<input name="contactQQ" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" 
				                		onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="14" value="${contact.contactQQ }"/>
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
				                	<input id="contactFax" name="contactFax" value="${contact.contactFax }" type="hidden"/>
				                </li>
				                <li style="background-color: white;">
				                	<label style="width: 90px;">Email：</label>
				                	<input name="contactEmail" type="text" maxlength="50" value="${contact.contactEmail }"/>
				                </li>
								<li style="background-color: white;">
									<label>微信：</label>
									<input name="wechatCode" type="text" maxlength="20" value="${contact.wechatCode }"/>
								</li>
				                <li style="background-color: white;display: table;height: auto;">
				                	<label style="display: table-cell;vertical-align: top;width: 90px;">描述：</label>
				                	<textarea rows="3" cols="30" name="remarks" maxlength="200" style="margin-left: 9px;">${contact.remarks }</textarea>
				                </li>
			            	</ul>
			        	</li>
					</div>
					</c:forEach>
                </div>
                <li class="clear"></li>
                
                <li class="liWidth countryCityArea">
                	<label><em class="xing">*</em>所属地区：</label>
                    <select class="country" name="agentinfo.belongsArea">
	                	<option value="">国家</option>
	                	<c:forEach items="${areaMap}" var="area">
	                		<option value="${area.key}" <c:if test="${agentinfo.belongsArea eq area.key }">selected='selected'</c:if> >${area.value}</option>
	                	</c:forEach>
	                </select>
                    <select class="province" name="agentinfo.belongsAreaProvince">
                		<option value="">省(直辖市)</option>
                    	<c:forEach items="${belongsProvinceMap}" var="prvc">
                    		<option value="${prvc.key}" <c:if test="${agentinfo.belongsAreaProvince eq prvc.key }">selected='selected'</c:if> >${prvc.value}</option>
	                	</c:forEach>
	                </select>
	                <select class="city" name="agentinfo.belongsAreaCity">
	                 	<option value="">市(区)</option>
	                 	<c:forEach items="${belongsCityMap}" var="city">
                    		<option value="${city.key}" <c:if test="${agentinfo.belongsAreaCity eq city.key }">selected='selected'</c:if> >${city.value}</option>
	                	</c:forEach>
	                </select>
				</li>
                <li class="clear"></li>
                <li class="liWidth countryCityAddress">
                	<label><em class="xing">*</em>公司地址：</label>
                    <select class="country" name="agentinfo.agentAddress" value="${agentinfo.agentAddress }">
	                	<option value="">国家</option>
	                	<c:forEach  items="${areaMap}" var="area">
	                		<option value="${area.key}" <c:if test="${agentinfo.agentAddress eq area.key }">selected='selected'</c:if> >${area.value}</option>
	                	</c:forEach>
	                </select>
                    <select class="province" name="agentinfo.agentAddressProvince">
	                	<option value="">省(直辖市)</option>
	                	<c:forEach items="${addressProvinceMap}" var="prvc">
                    		<option value="${prvc.key}" <c:if test="${agentinfo.agentAddressProvince eq prvc.key }">selected='selected'</c:if> >${prvc.value}</option>
	                	</c:forEach>
	                </select>
	                <select class="city" name="agentinfo.agentAddressCity">
	                 	<option value="">市(区)</option>
	                 	<c:forEach items="${addressCityMap}" var="city">
                    		<option value="${city.key}" <c:if test="${agentinfo.agentAddressCity eq city.key }">selected='selected'</c:if> >${city.value}</option>
	                	</c:forEach>
	                </select>
                    <input type="text" maxlength="100" name="agentinfo.agentAddressStreet" value="${agentinfo.agentAddressStreet }">
            	</li>
            	<li class="clear"></li>
            	<li><input type="checkbox" name="differenceRights" value = "1" <c:if test="${differenceRights==1 }">checked</c:if>/>开启下单权限</li>
				<!-- 临时策略（待删除）-->
            	<li><input type="checkbox" name="lingxianwangshuai" value = "1" <c:if test="${lingxianwangshuai==1 }">checked onclick='return false;'</c:if>/>领先（王帅）</li>
			</ul>
			
			<div class="sys_adduser_btn">
				<input id="btnClose" class="btn btn-primary" value="关&nbsp;&nbsp;&nbsp;闭" onclick="window.close();" type="button">
				<input id="btnCancel" class="btn btn-primary" value="返&nbsp;&nbsp;&nbsp;回" onclick="history.go(-1);" type="button">
				<input id="btnSubmit" onclick="submitForm();" class="btn btn-primary" value="保&nbsp;&nbsp;&nbsp;存" type="button">&nbsp;
			</div>
			
		</form:form>
	</div>
</body>
</html>
