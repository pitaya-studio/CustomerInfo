<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>批发商<shiro:hasPermission name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission></title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var $isShowCancelOrder = $(":hidden[name=isShowCancelOrder]");
			var $isShowDeleteOrder = $(":hidden[name=isShowDeleteOrder]");
			$("#isShowCancelOrder").click(function() {
				var isChecked = this.checked;
				if(isChecked) {
					$isShowCancelOrder.val("1");
				} else {
					$isShowCancelOrder.val("0");
				}
			});

			$("#isShowDeleteOrder").click(function() {
				var isChecked = this.checked;
				if(isChecked) {
					$isShowDeleteOrder.val("1");
				} else {
					$isShowDeleteOrder.val("0");
				}
			});
		});

		$(document).ready(function() {
			if(${is_check_domainName eq '1'})
				$("#domain_name_li").show();

			$("#inputForm").validate({
				rules:{
					supplierType:"required",
					supplierBrand:"required",
					companyName:"required",
					name:"required",
					code:"required",
					address:"required",
					master:"required",
					zipCode:{
						digits:true,
						rangelength:[6,6]
					},
					chargeRate:{
						isRateNumber:true,
						required:true
					},
					/* phone:{
						digits:true,
						rangelength:[11,11]
					}, */
					email:"email"
				},
				messages:{
					supplierType:"必填信息",
					supplierBrand:"必填信息",
					companyName:"必填信息",
					name:"必填信息",
					code:"必填信息",
					address:"必填信息",
					master:"必填信息",
					zipCode:{
						digits:"请输入数字",
						required:"号码长度为6"
					},
					chargeRate:{
						rangelength:"必填信息"
					},
					phone:{
						digits:"请输入数字",
						rangelength:"请输入11位手机号码"
					},
					email: "请输入正确的Email"
				},
				errorPlacement: function(error, element) {
					if ( element.is(":radio") )
						error.appendTo ( element.parent() );
					else if ( element.is(":checkbox") )
						error.appendTo ( element.parent() );
					else if ( element.is("input") )
						error.appendTo ( element.parent() );
					else
						error.insertAfter(element);
				}
			});
			
			jQuery.validator.addMethod("isRateNumber", function(value, element) {   
 			    var tel = /^([0-9]|[1-9][0-9]{1,4})(\.\d{1,4})?$/;
    			return this.optional(element) || (tel.test(value));
			}, "格式有误,例如12345.1234");

			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
				}
			});
		});
		//文件下载
		function downloads(docid,activitySerNum,acitivityName,iszip){
			window.open("${ctx}/sys/docinfo/download/"+docid);
		}
		//删除现有的文件
		function deleteFiles(id,o) {
			top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
				if(v=='ok'){
					var fileIDList = $(o).parent().parent().find("input").last().val();
					if (fileIDList.indexOf(";") < 0) {//单文件处理
						$(o).parent().next().val('');
						$(o).parent().remove();
					} else {//多文件处理
						id = id+";";
						var index = fileIDList.indexOf(id);
						if (index <= 0) {
							fileIDList = fileIDList.replace(id, "");
						} else if (fileIDList.charAt(index - 1) == ';') {
							fileIDList = fileIDList.replace(id, "");
						}
						$(o).parent().parent().find("input").last().val(fileIDList);
						$(o).parent().remove();
					}
				}
			},{buttonsFocus:1});
		}
		//文件上传回调
		/**
		 * 附件上传回调方法
		 * @param {Object} obj button对象
		 * @param {Object} fileIDList  文件表id
		 * @param {Object} fileNameList 文件原名称
		 * @param {Object} filePathList 文件url
		 */
		function commenFunction(obj,fileIDList,fileNameList,filePathList){

			if (fileIDList.length > 0) {
				var bool = fileIDList.indexOf(";");
				if (bool < 0) {//单文件处理
					$(obj).parent().find("span").remove();
					$("input[id='"+obj.id+"']").after("<span class='seach_checkbox_2'><b>"+fileNameList+"</b><input type='hidden' name='"+obj.name+"' value='"+fileIDList+"'/> <a style='margin-left:10px;' href='javascript:void(0)' onclick='downloads("+fileIDList+",\""+fileNameList+"\",1,true)'>下载</a> <a style='margin-left:10px;' href='javascript:void(0)' onclick='deleteFiles("+fileIDList+",this)'>删除</a></span>");
				} else {//多文件处理
					var fileID = new Array();
					var fileName = new Array();
					var filePath = new Array();
					
					fileID = fileIDList.split(";");
					fileName = fileNameList.split(";");
					filePath = filePathList.split(";");
					
					for (var i = 0; i < fileID.length - 1; i++) {
						var html = [];
						html.push("<span class='seach_checkbox_2'>");
						html.push("<br>");
						html.push("<b>" + fileName[i] + "</b>");
						html.push("<a style='margin-left:10px;' href='javascript:void(0)' onclick='downloads(" + fileID[i] + ",\"" + fileName[i] + "\",1,true)'>下载</a>");
						html.push("<a style='margin-left:10px;' href='javascript:void(0)' onclick='deleteFiles(" + fileID[i] + ",this)'>删除</a>");
						html.push("</span>");
						$("input[id='" + obj.id + "']").after(html.join(''));
					}
					var fileIDValues = $("input[name='" + obj.id + "']").val();
					$("input[name='" + obj.id + "']").val(fileIDValues + fileIDList);
				}
			}
		}
		function checkFirstForm(){
			top.$.jBox.tip('请先填写基本信息', 'error');
		}
		
		/** 
		* 校验团号规则不能为手动改自动 
		*/
		function checkGroupCodeRule(obj,values){
			if(values=='0' ){
				$(obj).parent().find("input:first").attr("checked","true");
				$.jBox.confirm("现团号规则为“手输并可修改”，不可改为“自动生成”，若必须修改团号规则请提需求处理！","提示","none", {buttons: { '确认': true, '关闭': false}});
			}else if(values=='1' && $(obj).next().text()!='自动生成'){
				$.jBox.confirm("团号规则配置只能由“自动生成”改为“手输并可修改”，不可改回，确认修改？", "确认", function(v, h, f){
	    	        if (v == 'ok') {
	    	        	flag = false;
	    	        }else{
	    	        	$(obj).parent().find("input").eq(1).attr("checked","true");
	    	        }
	            });
			}
		}
		$(function () {
			$(".input-xlarge").val($(this).val().substr(0, 500));
		})
	    
	</script>
</head>
<body>
<content tag="three_level_menu">
	<li><a href="${ctx}/sys/office/">批发商列表</a></li>
	<li class="active"><a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">批发商<shiro:hasPermission name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li>
</content>
<div class="supplierLine">
	<c:choose>
		<c:when test="${id!=null && id!='' }">
			<a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}" class="select">基本信息填写</a>
			<a href="${ctx}/sys/office/officePlatBankInfoForm?id=${office.id}&parent.id=${office.parent.id}">银行账户</a>
			<c:if test="${fns:getUser().company.uuid == '7a81a26b77a811e5bc1e000c29cf2586' }">
				<a href="${ctx}/sys/office/officeZhifubaoInfoForm?id=${office.id}&parent.id=${office.parent.id}">支付宝账户</a>
			</c:if>
		</c:when>
		<c:otherwise>
			<a href="${ctx}/sys/office/form" class="select">基本信息填写</a>
			<a href="javascript:void(0)" onclick="checkFirstForm()">银行账户</a>
			<c:if test="${fns:getUser().company.uuid == '7a81a26b77a811e5bc1e000c29cf2586' }">
				<a href="javascript:void(0)" onclick="checkFirstForm()">支付宝账户</a>
			</c:if>
		</c:otherwise>
	</c:choose>
</div><br/>
<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/saveOffice" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<tags:message content="${message}"/>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;上级节点:</label>
		<div class="controls">
			<tags:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
							 title="批发商" url="/sys/office/treeData" extId="${office.id}" cssClass="required"/>
		</div>
	</div>
	<input type="hidden" name="id" <c:if test="${id!=null }">value="${id }"</c:if><c:if test="${id==null }">value=""</c:if>/>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;批发商类型：</label>
		<div class="controls">
			<input type="checkbox" name="supplierType" id="supplierType1" value="1" <c:if test="${supplierType1=='1'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="1">散拼</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType2" value="2" <c:if test="${supplierType2=='2'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="2">游学</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType3" value="3" <c:if test="${supplierType3=='3'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="3">自由行</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType4" value="4" <c:if test="${supplierType4=='4'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="4">海岛游</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType5" value="5" <c:if test="${supplierType5=='5'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="5">签证</label><br />
			<input type="checkbox" name="supplierType" id="supplierType6" value="6" <c:if test="${supplierType6=='6'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="6">保险</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType7" value="7" <c:if test="${supplierType7=='7'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="7">单团</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType8" value="8" <c:if test="${supplierType8=='8'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="8">酒店</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType9" value="9" <c:if test="${supplierType9=='9'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="9">门票/景点</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="supplierType" id="supplierType10" value="10" <c:if test="${supplierType10=='10'}">checked="checked"</c:if>>&nbsp;&nbsp;&nbsp;<label for="10">机票</label>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;批发商品牌：</label>
		<div class="controls">
			<form:input path="supplierBrand" htmlEscape="false" maxlength="100" class="required"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;公司名称：</label>
		<div class="controls">
			<form:input path="companyName" htmlEscape="false" maxlength="100" class="required"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;英文名称：</label>
		<div class="controls">
			<form:input path="enname" htmlEscape="false" maxlength="100" class="required"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;批发商名称:</label>
		<div class="controls">
			<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;批发商编码:</label>
		<div class="controls">
			<form:input path="code" htmlEscape="false" maxlength="50"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;联系地址:</label>
		<div class="controls">
			<form:input path="address" htmlEscape="false" maxlength="50"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;"><!-- * --></span>&nbsp;英文地址:</label>
		<div class="controls">
			<form:input path="enAddress" htmlEscape="false" maxlength="250"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">邮政编码:</label>
		<div class="controls">
			<form:input path="zipCode" htmlEscape="false" maxlength="50"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span style="color:#f00;">*</span>&nbsp;负责人:</label>
		<div class="controls">
			<form:input path="master" htmlEscape="false" maxlength="50"/>
		</div>
	</div>
<%--	<c:if test="${fns:getUser().id eq 1}"> 注释by yudong.xu 2016.8.30
		<div class="control-group">
			<label class="control-label"><span style="color:#f00;">*</span>&nbsp;服务费率：</label>
			<div class="controls">
				<form:input path="chargeRate" htmlEscape="false" maxlength="10" class="required"/>
			</div>
		</div>
	</c:if>--%>
	<div class="control-group">
		<label class="control-label">电话:</label>
		<div class="controls"><!-- 手机号修改为最大输入长度30，去掉数字验证，可以输入特殊字符等，（侯涛）产品，修改chao.zhang 2016-09-22 -->
			<form:input path="phone" htmlEscape="false" maxlength="30"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">传真:</label>
		<div class="controls">
			<form:input path="fax" htmlEscape="false" maxlength="50"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">邮箱:</label>
		<div class="controls">
			<form:input path="email" htmlEscape="false" maxlength="50"/>
		</div>
	</div>
	
	<!--S 0517 -->
	<div class="control-group">
    	<label class="control-label">许可证号:</label>
        <div class="controls">
            <input type="text" value="${office.licenseNumber }" maxlength="50" name="licenseNumber" id="licenseNumber"/>
        </div>
    </div>
    <div class="control-group">
    	<label class="control-label">简介:</label>
        <div class="controls">
        	<textarea style="width: 270px;" rows="3" name="summary" maxlength="500">${office.summary }</textarea>
        	<%-- <form:textarea path="summary" cssClass="input-xlarge" rows="3" htmlEscape="false" />  --%>
        </div>
    </div>
    <div class="control-group">
    	<label class="control-label">网址:</label>
        <div class="controls">
        	<input type="text" id="webSite" name="webSite" value="${office.webSite }" maxlength="50">
        </div>
    </div>
    <div class="control-group">
    	<label class="control-label">营业执照:</label>
        <div class="controls">
        	<input type="button" value="选择文件" onclick="uploadFilesAddFlag('${ctx}','office.businessLicense',this,null);" class="license_file" id="businessLicense">
			<c:if test="${businessLicenseDocInfos != null }">
				<c:forEach items="${businessLicenseDocInfos }" var="licenseDocInfo">
					<span class="seach_checkbox_2">
						<br><b>${licenseDocInfo.docName }</b>
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${licenseDocInfo.id}','${licenseDocInfo.docName}',1,true)">下载</a> 
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${licenseDocInfo.id}',this)">删除</a>
					</span>
				</c:forEach>
			</c:if>
        	<input type="hidden" name="businessLicense" value="${businessLicense }">
        </div>
    </div>
    <div class="control-group">
    	<label class="control-label">业务资质证书:</label>
        <div class="controls">
        	<input type="button" value="选择文件" onclick="uploadFilesAddFlag('${ctx}','office.businessCertificate',this,null);" class="license_file" id="businessCertificate">
        	<c:if test="${businessCertificateDocInfos != null }">
				<c:forEach items="${businessCertificateDocInfos }" var="certificateDocInfo">
					<span class="seach_checkbox_2">
						<br><b>${certificateDocInfo.docName }</b>
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${certificateDocInfo.id}','${certificateDocInfo.docName}',1,true)">下载</a> 
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${certificateDocInfo.id}',this)">删除</a>
					</span>
				</c:forEach>
			</c:if>
        	<input type="hidden" name="businessCertificate" value="${businessCertificate }">
        </div>
    </div>
    <div class="control-group">
    	<label class="control-label">合作协议:</label>
        <div class="controls">
        	<input type="button" value="选择文件" onclick="uploadFilesAddFlag('${ctx}','office.cooperationProtocol',this,null);" class="license_file" id="cooperationProtocol">
        	<c:if test="${cooperationProtocolDocInfos != null }">
				<c:forEach items="${cooperationProtocolDocInfos }" var="protocolDocInfo">
					<span class="seach_checkbox_2">
						<br><b>${protocolDocInfo.docName }</b>
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${protocolDocInfo.id}','${protocolDocInfo.docName}',1,true)">下载</a> 
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${protocolDocInfo.id}',this)">删除</a>
					</span>
				</c:forEach>
			</c:if>
        	<input type="hidden" name="cooperationProtocol" value="${cooperationProtocol }">
        </div>
    </div>
	<!--E 0517 -->
	 <!--S C460-->
		<div style="margin-left: 56px">（温馨提示：团号规则配置只能由自动生成改为手动输入！）</div>

		<div class="control-group">
			<label class="control-label">团号规则（单团/散拼/大客户/自由行/游学/游轮）:</label>
			<div class="controls">
				<input type="radio" name="groupCodeRuleDT" value="0" onchange="checkGroupCodeRule(this,'${groupCodeRuleDT}');" <c:if test="${groupCodeRuleDT eq '0' or empty groupCodeRuleDT}"> checked="true" </c:if>><label>手输并可修改</label><br> 
				<input type="radio" name="groupCodeRuleDT" value="1" onchange="checkGroupCodeRule(this,'${groupCodeRuleDT}');" <c:if test="${groupCodeRuleDT eq '1'}"> checked="true" </c:if>><label>自动生成</label>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">团号规则（机票）:</label>
			<div class="controls">
				<input type="radio" name="groupCodeRuleJP" value="0" onchange="checkGroupCodeRule(this,'${groupCodeRuleJP}');" <c:if test="${groupCodeRuleJP eq '0' or empty groupCodeRuleJP}"> checked="true" </c:if>><label>手输并可修改</label><br>
				<input type="radio" name="groupCodeRuleJP" value="1" onchange="checkGroupCodeRule(this,'${groupCodeRuleJP}');" <c:if test="${groupCodeRuleJP eq '1'}"> checked="true" </c:if>><label>自动生成</label>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">团号规则（签证）:</label>
			<div class="controls">
				<input type="radio" name="groupCodeRuleQZ" value="0" onchange="checkGroupCodeRule(this,'${groupCodeRuleQZ}');" <c:if test="${groupCodeRuleQZ eq '0' or empty groupCodeRuleQZ}">checked="true"</c:if>><label>手输并可修改</label><br> 
				<input type="radio" name="groupCodeRuleQZ" value="1" onchange="checkGroupCodeRule(this,'${groupCodeRuleQZ}');" <c:if test="${groupCodeRuleQZ eq '1'}"> checked="true" </c:if>><label>自动生成</label>
			</div>
		</div>
		<!--E C460-->
	
	<div class="control-group" style="height:30px" >
		<label class="control-label">是否验证域名标识:</label>
		<div class="controls">
			<input type="radio" name="isValidateDoma" value="0" <c:if test="${is_check_domainName eq '0' or empty is_check_domainName}"> checked="true" </c:if>  onClick="$('#domain_name_li').hide();$('#domainName').val('')"/><label>不需要验证</label>
			<input type="radio" name="isValidateDoma" value="1" <c:if test="${is_check_domainName eq '1'}"> checked="true" </c:if>  onClick="$('#domain_name_li').show()"/><label>需要验证</label>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">系统自动取消订单:</label>
		<div class="controls">
			<input type="radio" name="isCancleOrder" value="1" <c:if test="${is_cancle_order eq '1' or empty is_cancle_order}"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="isCancleOrder" value="0" <c:if test="${is_cancle_order eq '0'}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">是否允许补单:</label>
		<div class="controls">
			<input type="radio" name="isAllowSupplement" value="1" <c:if test="${is_allow_supplement eq '1' or empty is_allow_supplement}"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="isAllowSupplement" value="0" <c:if test="${is_allow_supplement eq '0'}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">询价模式:</label>
		<div class="controls">
			<input type="radio" name="estimateModel" value="1" <c:if test="${estimate_model eq '1' or empty estimate_model}"> checked="true" </c:if> /><label>询问计调</label>
			<input type="radio" name="estimateModel" value="2" <c:if test="${estimate_model eq '2'}"> checked="true" </c:if>  /><label>询问计调主管</label>
			<input type="radio" name="estimateModel" value="3" <c:if test="${estimate_model eq '3'}"> checked="true" </c:if>  /><label>询价对象可选</label>
		</div>
	</div>

	<!-- 添加渠道商下单提醒配置功能 -->
	<div class="control-group" style="height:30px">
		<label class="control-label">是否下单提醒:</label>
		<div class="controls">
			<input type="radio" name="isNeedAttention" value="0"
					<c:if test="${is_need_attention eq '0'}"> checked="true" </c:if>
				   onClick="" /><label>不提醒</label> <input type="radio"
														  name="isNeedAttention" value="1"
				<c:if test="${is_need_attention eq '1' or empty is_need_attention}"> checked="true" </c:if>
														  onClick="" /><label>提醒</label>
		</div>
	</div>

	<!-- 批发商添加渠道是否可修改配置 -->
	<div class="control-group" style="height:30px; display:none;">
		<label class="control-label">是否允许订单预定渠道修改:</label>
		<div class="controls">
			<input type="radio" name="isAllowModify" value="0"
					<c:if test="${is_allow_modify eq '0'}"> checked="true" </c:if> /><label>否</label>
			<input type="radio" name="isAllowModify" value="1"
					<c:if test="${is_allow_modify eq '1' or empty is_allow_modify}"> checked="true" </c:if> /><label>是</label>
		</div>
	</div>

	<c:set var="companyUuid" value="${fns:getUser().company.uuid}"></c:set>
	<c:set var="userId" value="${fns:getUser().id}"></c:set>
	<c:if test="${userId == 1  || (not fn:contains(mtourCompanyUuid, companyUuid))}">
		<!-- 是否允许订单渠道联系人信息输入修改 -->
		<div class="control-group" style="height:30px; display:none;">
			<label class="control-label">允许订单渠道联系人信息输入修改:</label>
			<div class="controls">
				<input type="radio" name="isAllowModifyAgentInfo" value="0"
						<c:if test="${isAllowModifyAgentInfo eq '0'}"> checked="true" </c:if> /><label>否</label>
				<input type="radio" name="isAllowModifyAgentInfo" value="1"
						<c:if test="${isAllowModifyAgentInfo eq '1' or empty isAllowModifyAgentInfo}"> checked="true" </c:if> /><label>是</label>
			</div>
		</div>
		<!-- 是否允许添加订单渠道联系人信息 -->
		<div class="control-group" style="height:30px; display:none;">
			<label class="control-label">允许添加订单渠道联系人信息:</label>
			<div class="controls">
				<input type="radio" name="isAllowAddAgentInfo" value="0"
						<c:if test="${isAllowAddAgentInfo eq '0'}"> checked="true" </c:if> /><label>否</label>
				<input type="radio" name="isAllowAddAgentInfo" value="1"
						<c:if test="${isAllowAddAgentInfo eq '1' or empty isAllowAddAgentInfo}"> checked="true" </c:if> /><label>是</label>
			</div>
		</div>
	</c:if>

	<!-- 是否有预算成本审核流程: 1为否， 0为是 -->
	<div class="control-group" style="height:30px;">
		<label class="control-label">是否有预算成本审核流程:</label>
		<div class="controls">
			<input type="radio" name="budgetCostAutoPass" value="1"
					<c:if test="${budgetCostAutoPass eq '1'}"> checked="true" </c:if> /><label>否</label>
			<input type="radio" name="budgetCostAutoPass" value="0"
					<c:if test="${budgetCostAutoPass eq '0' or empty budgetCostAutoPass}"> checked="true" </c:if> /><label>是</label>
		</div>
	</div>

	<!-- 是否有实际成本审核流程: 1为否， 0为是 -->
	<div class="control-group" style="height:30px;">
		<label class="control-label">是否有实际成本审核流程:</label>
		<div class="controls">
			<input type="radio" name="costAutoPass" value="1"
					<c:if test="${costAutoPass eq '1'}"> checked="true" </c:if> /><label>否</label>
			<input type="radio" name="costAutoPass" value="0"
					<c:if test="${costAutoPass eq '0' or empty costAutoPass}"> checked="true" </c:if> /><label>是</label>
		</div>
	</div>

	<!-- 是否需要团号库配置  1需要， 0不需要-->
	<div class="control-group" style="height:30px;">
		<label class="control-label">是否需要团号库:</label>
		<div class="controls">
			<input type="radio" name="isNeedGroupCode" value="1"
					<c:if test="${is_need_groupCode eq '1'}"> checked="true" </c:if> /><label>是</label>
			<input type="radio" name="isNeedGroupCode" value="0"
					<c:if test="${is_need_groupCode eq '0' or empty is_need_groupCode}"> checked="true" </c:if> /><label>否</label>
		</div>
		
	</div>
	<!-- 是否需要游轮团控  1需要， 0不需要-->
		<div class="control-group" style="height:30px;">
			<label class="control-label">是否需要游轮团控:</label>
			<div class="controls">
				<input type="radio" name="isNeedCruiseshipControll" value="1"
					<c:if test="${is_need_cruiseshipControll eq '1'}"> checked="true" </c:if> /><label>是</label>
				<input type="radio" name="isNeedCruiseshipControll" value="0"
					<c:if test="${is_need_cruiseshipControll eq '0' or empty is_need_cruiseshipControll}"> checked="true" </c:if> /><label>否</label>
			</div>
		</div>

	<!-- 预算成本何时写入预报单，0：保存时；1：提交审批时 -->
	<div class="control-group" style="height:30px;">
		<label class="control-label">预算成本何时写入预报单:</label>
		<div class="controls">
			<input type="radio" name="budgetCostWhenUpdate" value="0"
					<c:if test="${budgetCostWhenUpdate eq '0'  or empty budgetCostWhenUpdate}"> checked="checked" </c:if> /><label>保存时</label>
			<input type="radio" name="budgetCostWhenUpdate" value="1"
					<c:if test="${budgetCostWhenUpdate eq '1'}"> checked="checked" </c:if> /><label>提交审批时</label>
		</div>
	</div>

	<!-- 实际成本何时写入结算单，0：保存时；1：提交审批时；2：提交付款申请(仅针对拉美途) -->
	<div class="control-group" style="height:30px;">
		<label class="control-label">实际成本何时写入结算单:</label>
		<div class="controls">
			<input type="radio" name="actualCostWhenUpdate" value="0"
					<c:if test="${actualCostWhenUpdate eq '0'  or empty actualCostWhenUpdate}"> checked="checked" </c:if> /><label>保存时</label>
			<input type="radio" name="actualCostWhenUpdate" value="1"
					<c:if test="${actualCostWhenUpdate eq '1'}"> checked="checked" </c:if> /><label>提交审批时</label>
				<%--<c:if test="${companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">--%>
			<input type="radio" name="actualCostWhenUpdate" value="2"
				   <c:if test="${actualCostWhenUpdate eq '2'}">checked="checked"</c:if> /><label>提交付款申请</label>
				<%--</c:if>--%>
		</div>
	</div>

	<!-- 预算成本、实际成本何时写入预报单、结算单，0：保存时；1：提交审核时 -->
	<%--<div class="control-group" style="height:30px;">--%>
	<%--<label class="control-label">预算成本、实际成本何时写入预报单、结算单:</label>--%>
	<%--<div class="controls">--%>
	<%--<input type="radio" name="whenToSheet" value="0"--%>
	<%--<c:if test="${whenToSheet eq '0'  or empty whenToSheet}"> checked="true" </c:if> /><label>保存时</label>--%>
	<%--<input type="radio" name="whenToSheet" value="1"--%>
	<%--<c:if test="${whenToSheet eq '1'}"> checked="true" </c:if> /><label>提交审核时</label>--%>
	<%--</div>--%>
	<%--</div>--%>

	<!-- 是否需要确认付款: 1需要， 0不需要 -->
	<div class="control-group" style="height:30px;">
		<label class="control-label">是否需要确认付款:</label>
		<div class="controls">
			<input type="radio" name="confirmPay" value="1"
					<c:if test="${confirmPay eq '1'}"> checked="true" </c:if> /><label>需要</label>
			<input type="radio" name="confirmPay" value="0"
					<c:if test="${confirmPay eq '0' or empty confirmPay}"> checked="true" </c:if> /><label>不需要</label>
		</div>
	</div>

	<!-- 报名模块、订单模块是否显示签证成本价格  1显示， 0不显示-->
	<div class="control-group" style="height:30px;">
		<label class="control-label">报名模块、订单模块是否显示签证成本价格:</label>
		<div class="controls">
			<input type="radio" name="visaCostPrice" value="0"
					<c:if test="${visaCostPrice eq '0' or empty visaCostPrice}"> checked="true" </c:if> /><label>不显示</label>
			<input type="radio" name="visaCostPrice" value="1"
					<c:if test="${visaCostPrice eq '1'}"> checked="true" </c:if> /><label>显示</label>
		</div>
	</div>


	<div class="control-group" style="height:30px" >
		<label class="control-label">生成子订单:</label>
		<div class="controls">
			<input type="checkbox" name="createSubOrder" value="1" <c:if test="${fn:contains(create_sub_order,'1')}"> checked="true" </c:if> /><label>机票</label>
			<input type="checkbox" name="createSubOrder" value="2" <c:if test="${fn:contains(create_sub_order,'2')}"> checked="true" </c:if> /><label>签证</label>
			<input type="checkbox" name="createSubOrder" value=" " style="display: none;" checked="checked"/>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">财务确认占位:</label>
		<div class="controls">
			<input type="radio" name="orderPayMode" value="1" <c:if test="${order_pay_mode eq '1' or empty is_allow_supplement}"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="orderPayMode" value="0" <c:if test="${order_pay_mode eq '0'}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">是否查看已收明细（除机票产品）:</label>
		<div class="controls">
			<input type="radio" name="queryCommonOrderList" value="1" <c:if test="${query_common_order_list eq '1' or empty query_common_order_list}"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="queryCommonOrderList" value="0" <c:if test="${query_common_order_list eq '0'}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">可见字段:</label>
		<div class="controls">
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields1" value="1" <c:if test="${fn:contains(query_common_fields,'1')}"> checked="true" </c:if> /><label>订单号</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields2" value="2" <c:if test="${fn:contains(query_common_fields,'2')}"> checked="true" </c:if> /><label>渠道</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields3" value="3" <c:if test="${fn:contains(query_common_fields,'3')}"> checked="true" </c:if> /><label>销售</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields4" value="4" <c:if test="${fn:contains(query_common_fields,'4')}"> checked="true" </c:if> /><label>下单人</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields5" value="5" <c:if test="${fn:contains(query_common_fields,'5')}"> checked="true" </c:if> /><label>预定时间</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields6" value="6" <c:if test="${fn:contains(query_common_fields,'6')}"> checked="true" </c:if> /><label>人数</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields7" value="7" <c:if test="${fn:contains(query_common_fields,'7')}"> checked="true" </c:if> /><label>订单状态</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields8" value="8" <c:if test="${fn:contains(query_common_fields,'8')}"> checked="true" </c:if> /><label>订单总额</label>
			<input type="checkbox" name="queryCommonFields" id="queryCommonFields9" value="9" <c:if test="${fn:contains(query_common_fields,'9')}"> checked="true" </c:if> /><label>已付金额/到账金额</label>
			<input type="checkbox" name="queryCommonFields" value="" style="display: none;" checked="checked"/>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">是否查看已收明细（机票产品）:</label>
		<div class="controls">
			<input type="radio" name="queryAirticketOrderList" value="1" <c:if test="${query_airticket_order_list eq '1' or empty query_airticket_order_list}"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="queryAirticketOrderList" value="0" <c:if test="${query_airticket_order_list eq '0'}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>

	<div class="control-group" style="height:30px" >
		<label class="control-label">可见字段:</label>
		<div class="controls">
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields1" value="1" <c:if test="${fn:contains(query_airticket_fields,'1')}"> checked="true" </c:if> /><label>订单号</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields2" value="2" <c:if test="${fn:contains(query_airticket_fields,'2')}"> checked="true" </c:if> /><label>渠道</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields3" value="3" <c:if test="${fn:contains(query_airticket_fields,'3')}"> checked="true" </c:if> /><label>销售</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields4" value="4" <c:if test="${fn:contains(query_airticket_fields,'4')}"> checked="true" </c:if> /><label>下单人</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields5" value="5" <c:if test="${fn:contains(query_airticket_fields,'5')}"> checked="true" </c:if> /><label>预定时间</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields6" value="6" <c:if test="${fn:contains(query_airticket_fields,'6')}"> checked="true" </c:if> /><label>人数</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields7" value="7" <c:if test="${fn:contains(query_airticket_fields,'7')}"> checked="true" </c:if> /><label>订单状态</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields8" value="8" <c:if test="${fn:contains(query_airticket_fields,'8')}"> checked="true" </c:if> /><label>订单总额</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields9" value="9" <c:if test="${fn:contains(query_airticket_fields,'9')}"> checked="true" </c:if> /><label>已付金额/到账金额</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields10" value="10" <c:if test="${fn:contains(query_airticket_fields,'10')}"> checked="true" </c:if> /><label>参团类型</label>
			<input type="checkbox" name="queryAirticketFields" id="queryAirticketFields11" value="11" <c:if test="${fn:contains(query_airticket_fields,'11')}"> checked="true" </c:if> /><label>机票类型</label>
			<input type="checkbox" name="queryAirticketFields" value="" style="display: none;" checked="true"/>
		</div>
	</div>
	<%--是否多对象返佣--%>
	<div class="control-group" style="height:30px" >
		<label class="control-label">是否多对象返佣:</label>
		<div class="controls">
			<input type="radio" name="isAllowMultiRebateObject" value="1" <c:if test="${not empty isAllowMultiRebateObject and isAllowMultiRebateObject eq 1 }"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="isAllowMultiRebateObject" value="0" <c:if test="${empty isAllowMultiRebateObject or isAllowMultiRebateObject eq 0}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>

	<%--不可见订单状态--%>
	<div class="control-group" style="height:30px" >
		<label class="control-label">不可见订单状态:</label>
		<div class="controls">
			<input type="checkbox" name="" id="isShowCancelOrder" <c:if test="${isShowCancelOrder eq 1 }"> checked="true" </c:if>  /><label>已取消订单</label>
			<input type="hidden" name="isShowCancelOrder" value="${isShowCancelOrder}" defaultValue="0"/>
			<input type="checkbox" name="" id="isShowDeleteOrder" <c:if test="${isShowDeleteOrder eq 1 }"> checked="true" </c:if> /><label>已删除订单</label>
			<input type="hidden" name="isShowDeleteOrder" value="${isShowDeleteOrder}" defaultValue="0"/>
		</div>
	</div>

	<%-- 270 还款日期是否必填 --%>
	<div class="control-group" style="height:30px" >
		<label class="control-label">还款日期是否必填:</label>
		<div class="controls">
			<input type="radio" name="isMustRefundDate" value="1" <c:if test="${isMustRefundDate eq '1'}"> checked="true" </c:if>/><label>是</label>
			<input type="radio" name="isMustRefundDate" value="0" <c:if test="${isMustRefundDate eq '0' or empty isMustRefundDate}"> checked="true" </c:if>/><label>否</label>
		</div>
	</div>

	<%-- 575 功能修改为订单权限可配置 wangyang 2017.1.4 --%>
	<%--是否使用客户确认占位功能--%>
	<%-- <div class="control-group" style="height:30px" >
		<label class="control-label">是否使用客户确认占位功能:</label>
		<div class="controls">
			<input type="radio" name="isSeizedConfirmation" value="1" <c:if test="${not empty isSeizedConfirmation and isSeizedConfirmation eq 1 }"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="isSeizedConfirmation" value="0" <c:if test="${empty isSeizedConfirmation or isSeizedConfirmation eq 0}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div> --%>
    
    <!-- 0444需求 -->
    <div class="control-group" style="height:30px">
		<label class="control-label">预开发票:</label>
		<div class="controls">
			<input type="radio" name="preOpenInvoice" value="1" <c:if test="${not empty preOpenInvoice and preOpenInvoice eq '1' }"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="preOpenInvoice" value="0" <c:if test="${empty preOpenInvoice or preOpenInvoice eq '0'}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>
	<!-- 0444需求 -->
	
    <!-- 0318新增是否允许修改销售签证订单下的游客信息--s-对于批发商环球行而言,比较特殊,它不显示该配置(为了保持系统原有规则) -->
      <div class="control-group" style="height:30px" <c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">hidden</c:if>>
		<label class="control-label">修改销售签证订单:</label>
		<div class="controls">
			<input type="radio" name="isAllowModifyXSVisaOrder" value="1" <c:if test="${not empty isAllowModifyXSVisaOrder and isAllowModifyXSVisaOrder eq 1 }"> checked="true" </c:if>  /><label>是</label>
			<input type="radio" name="isAllowModifyXSVisaOrder" value="0" <c:if test="${empty isAllowModifyXSVisaOrder or isAllowModifyXSVisaOrder eq 0}"> checked="true" </c:if> /><label>否</label>
		</div>
	</div>
    <!-- 0318新增是否允许修改销售签证订单下的游客信息--e -->
    
    <!-- 0474 新增是否解除发票申请限制     默认为否，鼎鸿假期配置为是 -->
    <div class="control-group" style="height:30px">
		<label class="control-label">是否解除发票申请限制:</label>
		<c:choose>
			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}">
				<div class="controls">
					<input type="radio" name="isRemoveApplyInvoiceLimit" value="1" <c:if test="${empty isRemoveApplyInvoiceLimit or isRemoveApplyInvoiceLimit eq 1 }"> checked="true" </c:if>  /><label>是</label>
					<input type="radio" name="isRemoveApplyInvoiceLimit" value="0" <c:if test="${not empty isRemoveApplyInvoiceLimit and isRemoveApplyInvoiceLimit eq 0}"> checked="true" </c:if> /><label>否</label>
				</div>
			</c:when>
			<c:otherwise>
				<div class="controls">
					<input type="radio" name="isRemoveApplyInvoiceLimit" value="1" <c:if test="${not empty isRemoveApplyInvoiceLimit and isRemoveApplyInvoiceLimit eq 1 }"> checked="true" </c:if>  /><label>是</label>
					<input type="radio" name="isRemoveApplyInvoiceLimit" value="0" <c:if test="${empty isRemoveApplyInvoiceLimit or isRemoveApplyInvoiceLimit eq 0}"> checked="true" </c:if> /><label>否</label>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
    <!-- 0474 新增是否解除发票申请限制     默认为否，鼎鸿假期配置为是  -->
    
    <!-- T1平台余位状态：0.实时 1.现询 -->
    <div class="control-group" style="height:30px">
		<label class="control-label">T1平台余位状态:</label>
		<div class="controls">
			<input type="radio" name="t1FreePosionStatus" value="0" <c:if test="${empty t1FreePosionStatus or t1FreePosionStatus eq 0 }"> checked="true" </c:if>  /><label>实时</label>
			<input type="radio" name="t1FreePosionStatus" value="1" <c:if test="${not empty t1FreePosionStatus and t1FreePosionStatus eq 1}"> checked="true" </c:if> /><label>现询</label>
		</div>
	</div>
    <!-- T1平台余位状态：0.实时 1.现询  -->
    
    <%--签证"全部订单"页不显示的订单: 已取消等 --%>
    <div class="control-group" style="height:30px" >
		<label class="control-label">签证"全部订单"页不显示的订单:</label>
		<div class="controls">
			<input type="checkbox" name="banedVisaOrderOfAllTab" id="banedVisaOrderOfAllTab1" value="99" <c:if test="${fn:contains(banedVisaOrderOfAllTab,'99')}"> checked="true" </c:if> /><label>已取消订单</label>
			<input type="checkbox" name="banedVisaOrderOfAllTab" value="" style="display: none;" checked="true"/>
		</div>
	</div>
	
	<%--是否开通邮件提醒--%>
	<%--<div class="control-group" style="height:30px" >
		<label class="control-label">是否开通邮件提醒:</label>
		<div class="controls">
			<input type="radio" name="isMailRemind" value="1" <c:if test="${isMailRemind eq '1'}"> checked="true" </c:if> /><label>是</label>
			<input type="radio" name="isMailRemind" value="0" <c:if test="${isMailRemind eq '0' or empty isMailRemind}"> checked="true" </c:if>/><label>否</label>
		</div>
	</div>--%>

	<div class="control-group"  id="domain_name_li" style="display: none;">
		<label class="control-label"><font style="color:#ff0000; padding-right:5px;">*</font>域名:</label>
		<div class="controls">
			<form:input path="domainName" maxlength="199" cssClass="required"/>
			<input type="button" class="btn btn-primary" onClick="$('#domainName').val('${pageContext.request.serverName}')" value="使用当前域名">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">备注:</label>
		<div class="controls">
			<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">公司LOGO：</label>
		<div class="controls">
			<input type="button" name="logo" id="logo"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','office.logo',this,1);"/>&nbsp;&nbsp;&nbsp;
			<c:choose>
				<c:when test="${docInfo != null}">
				<span class="seach_checkbox_2">
					<b>${docInfo.docName}</b>
					<input type="hidden" name="taxCertificate" value="${docInfo.id}">
					<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${docInfo.id}','${docInfo.docName}',1,true)">下载</a> 
					<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${docInfo.id}',this)">删除</a>
				</span>
				</c:when>
			</c:choose>
			<input type="hidden" name="logo" value="${logo }"/>
		</div>
	</div>
	<div class="form-actions1">
		<a class="ydbz_x gray" href="${ctx}/sys/office">返&nbsp;&nbsp;&nbsp;回</a>&nbsp;&nbsp;&nbsp;
		<shiro:hasPermission name="sys:office:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="下一步"/>&nbsp;</shiro:hasPermission>
	</div>
</form:form>
</body>
</html>