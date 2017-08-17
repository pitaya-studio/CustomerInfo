<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理-新增第1步-基本信息</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	
	var supplierTypes = $("#supplierTypes").val();
	if (supplierTypes != '') {
		var supplierTypeArr = supplierTypes.split(',');
		for (var i=0;i<supplierTypeArr.length;i++) {
			$(":checkbox[value=" + supplierTypeArr[i] + "]").attr('checked', true);
		}
	}
	
	// 公司地址 国家、省（直辖市）、市区联动下拉框
	$("span.countryCityAddress select").change(function(){
		var parentId = $(this).val();
		var url = "${ctx}"+"/supplier/getAreaInfoById/"+parentId;
		var currentClass = $(this).attr('class');
		if(parentId.length>0){
			$.ajax({
				   type: "POST",
				   url: url,
				   dataType:"json",
				   success: function(msg){
				     	if(currentClass=="country"){
		     		  		$("span.countryCityAddress .province").empty();
		     		  		$("span.countryCityAddress .city").empty();
		     		  		$("span.countryCityAddress .province").append("<option>省(直辖市)</option>");
		     		  		$("span.countryCityAddress .city").append("<option>市(区)</option>");
				     		$.each(msg,function(i, n){
				     		  		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";

				     		  		$("span.countryCityAddress .province").append(optionStr);
				     		});
				     	}
			     		if(currentClass=="province"){
			     			$("span.countryCityAddress .city").empty();
			     			$("span.countryCityAddress .city").append("<option>市(区)</option>");
			     			$.each(msg,function(i, n){
				     		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
			     			$("span.countryCityAddress .city").append(optionStr);
			     			});
			     		}
				     	}
				});
		}else{
	     	if(currentClass=="country"){
 		  		$("span.countryCityAddress .province").empty();
 		  		$("span.countryCityAddress .city").empty();
 		  		$("span.countryCityAddress .province").append("<option>省(直辖市)</option>");
 		  		$("span.countryCityAddress .city").append("<option>市(区)</option>");
	     	}
     		if(currentClass=="province"){
     			$("span.countryCityAddress .city").empty();
     			$("span.countryCityAddress .city").append("<option>市(区)</option>");
     		}
		}
	});

	//默认缓存判断
    var spinnerval=$(".spinner").val();
	if(spinnerval==31){
		$(".spinner").parents("p").find("i").text("重复：每月最后一天");
	}else{
		$(".spinner").parents("p").find("i").text("重复：每月"+spinnerval+"日");
	}
});

function liandong(strType, index, thisobj){
	var parentId = $(thisobj).val();
	var url = "${ctx}"+"/supplier/getAreaInfoById/"+parentId;
	if(parentId.length>0){
		$.ajax({
			   type: "POST",
			   url: url,
			   dataType:"json",
			   success: function(msg){
			     	if(strType=="country"){
			     		var objProvince=document.getElementById("belongsAreaProvince"+index);
			     		var objCity=document.getElementById("belongsAreaCity"+index);
			     		document.getElementById("belongsAreaProvince"+index).options.length = 0;
			     		document.getElementById("belongsAreaCity"+index).options.length = 0;
			     		objProvince.options.add(new Option("省(直辖市)",""));
			     		objCity.options.add(new Option("市(区)",""));
			     		$.each(msg,function(i, n){
			     			objProvince.options.add(new Option(n.name,n.id));
			     		});
			     		}
			     		if(strType=="province"){
				     		var objCity=document.getElementById("belongsAreaCity"+index);
				     		document.getElementById("belongsAreaCity"+index).options.length = 0;
				     		objCity.options.add(new Option("市(区)",""));
			     			$.each(msg,function(i, n){
			     				objCity.options.add(new Option(n.name,n.id));
			     			});
			     		}
			     	}
			});
	}else{
     	if(strType=="country"){
     		var objProvince=document.getElementById("belongsAreaProvince"+index);
     		var objCity=document.getElementById("belongsAreaCity"+index);
     		document.getElementById("belongsAreaProvince"+index).options.length = 0;
     		document.getElementById("belongsAreaCity"+index).options.length = 0;
     		objProvince.options.add(new Option("省(直辖市)",""));
     		objCity.options.add(new Option("市(区)",""));
     		}
 		if(strType=="province"){
     		var objCity=document.getElementById("belongsAreaCity"+index);
     		document.getElementById("belongsAreaCity"+index).options.length = 0;
     		objCity.options.add(new Option("市(区)",""));
 		}
	}
}

//输入判断
function spinnerInput(obj){
	var ms = obj.value.replace(/\D/g,'');
	if(obj.value>=31){
		$(".spinner").parents("p").find("i").text("重复：每月最后一天");
		if(obj.value > 31){
			obj.value = 31;
		}
	}else{
		$(".spinner").parents("p").find("i").text("重复：每月"+obj.value+"日");
	}
}
function on_hide(obj){
	if ($(obj).attr("checked")) {
		$("#hide").show();
	} else {
		$("#hide").hide();
	}
}

//地接社管理添加地区
function shopAddressAdd2(obj){
		var contactAddressNum = $("p[name=areaspart]").length;
		$('#belongArea').append('<p name="areaspart" style="display:block">'+
			'<label>&nbsp;</label>' +
			'<span class="sysselect_s countryCityArea">' +
			'<select class="country" id="belongArea' + contactAddressNum +'" name="belongArea' + contactAddressNum +'" onchange="liandong(\'country\', ' + contactAddressNum +', this)">' +
			'<option value="">国家</option>'+
			'<c:forEach  items="${areaMap}" var="aa">' +
			'<option value="${aa.key}">${aa.value}</option>' +
	        '</c:forEach>' +
	        '</select>' +
	        '<select class="province" id="belongsAreaProvince' + contactAddressNum +'" name="belongsAreaProvince' + contactAddressNum +'" onchange="liandong(\'province\', ' + contactAddressNum +', this)">' +
	        '<option value="">省(直辖市)</option>' +
	        '</select>'+
	        '<select class="city" id="belongsAreaCity' + contactAddressNum + '" name="belongsAreaCity">' +
	        '<option value="">市(区)</option>' +
	        '</select>' +
	        '</span><a class="ydbz_x gray" onclick="shopAddressDel(this)">删除</a>' +
	        '</p>');
	}

var contactsNum = 2;

// 地接社联系人添加
function shopPeopleAdd2(obj){
	index = $("p[name=shopPeopleP]").length;
	if(contactsNum!=2){
		index = contactsNum;
	}
	var contactPeopleNum = $("p[name=shopPeopleP]").length+1;
	$('#shopPeopleP').append('<p name="shopPeopleP" style="display:block" class="shopPeopleNone"><label>联系人<em>' + contactPeopleNum +'</em>：</label>'+
		'<input type="hidden" name="contactId" value="' + index +'"/>'+
		'<span><input name="contacts.supplierContactses[' + index +'].contactName" type="text" maxlength="20" value="${contacts.contactName }"></span>'+
		'<label>手机：</label>'+
		'<span>'+
		'<input name="contacts.supplierContactses[' + index +'].contactMobile" type="text" maxlength="20" value="${contacts.contactMobile }">'+
		'</span><a class="ydbz_x gray" onclick="shopPeopleDel2(this)">删除</a>'+
		'<span class="kongr20"></span>'+
		'<label>固定电话：</label>'+
		'<span><input name="contacts.supplierContactses[' + index +'].contactPhone" type="text" maxlength="20" value="${contacts.contactPhone }"></span>'+
		'<label>传真：</label>'+
		'<span><input name="contacts.supplierContactses[' + index +'].contactFax" type="text" maxlength="20" value="${contacts.contactFax }"></span>'+
		'<span class="kongr20"></span>'+
		'<label>Email：</label>'+
		'<span><input name="contacts.supplierContactses[' + index +'].contactEmail" type="text" maxlength="50" value="${contacts.contactEmail }"></span>'+
		'<label>QQ：</label>'+
		'<span><input name="contacts.supplierContactses[' + index +'].contactQQ"  type="text" maxlength="20" value="${contacts.contactQQ }"></span>'+
		'</p>');
	if(contactsNum!=2){
		contactsNum += 1;
	}
}
function shopPeopleDel2(obj){
	if(contactsNum == 2){
		contactsNum = $("p[name=shopPeopleP]").length;
	}
	$(obj).parent().remove();
	$('.shopPeopleNone').each(function(index){
	   $(this).find('em').text(index+1);
      });
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
 	var name = obj.name;
	$(obj).parent().find("span").remove();
	if(fileIDList.length>0){
		$("input[name='"+obj.name+"']").after("<span><b>"+fileNameList+"</b><input type='hidden' name='"+obj.name+"' value='"+fileIDList+"'/> <a style='margin-left:10px;' href='javascript:void(0)' onclick='downloads("+fileIDList+",\""+fileNameList+"\",1,true)'>下载</a> <a style='margin-left:10px;' href='javascript:void(0)' onclick='deleteFiles("+fileIDList+",this)'>删除</a></span>");
		}
 	
 }
//删除现有的文件
function deleteFiles(id , object) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			$(object).closest("span").remove();
		}
		},{buttonsFocus:1});
}
//文件下载
function downloads(docid){
	window.open("${ctx}/sys/docinfo/download/"+docid);
}

function firstSub(companyId){
	if (check(companyId)) {
		$("#inputForm").submit();
	}
}

//验证输入框是否全是空格
var parten = /^\s*$/ ;

function check(companyId){
	//是否选择了地接社
	var checkedFlag = false;
	if ($(":checkbox:checked").size() > 0) {
		checkedFlag = true;
	}
	if(!checkedFlag){
		top.$.jBox.tip('请选择地接社类型', 'error');
		return checkedFlag;
	}
	
	if(document.getElementById("supplierLevel").value == ""
			|| parten.test(document.getElementById("supplierLevel").value)){
	   	top.$.jBox.tip('请填写地接社等级', 'error');
		document.getElementById("supplierLevel").focus(); 
		return false; 
	}else if(document.getElementById("supplierInfo.supplierBrand").value == ""
			|| parten.test(document.getElementById("supplierInfo.supplierBrand").value)) {
		top.$.jBox.tip('请填写地接社品牌', 'error');
		document.getElementById("supplierInfo.supplierBrand").focus(); 
		return false; 
	}else if((document.getElementById("supplierInfo.supplierName").value == ""
			|| parten.test(document.getElementById("supplierInfo.supplierName").value))
			//&& companyId == 71
			){
		top.$.jBox.tip('请填写公司名称', 'error');
		document.getElementById("supplierInfo.supplierName").focus(); 
		return false; 
	}else {
	$("#inputForm").submit();
	top.$.jBox.tip('第一步保存成功', 'success');
	//return true;
	}
}

function checkFirstForm(){
	top.$.jBox.tip('请先填写基本信息', 'error');
}
</script>
</head>
<body>
	<content tag="three_level_menu">
		<shiro:hasPermission name="supplier:manager:view">
			<li><a href="${ctx}/supplier/supplierInfoList">地接社列表</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="supplier:manager:add">
			<li class="active"><a href="${ctx}/supplier/supplierFirstForm">地接社<shiro:hasPermission name="supplier:manager:add">${not empty supplierId?'修改':'添加'}</shiro:hasPermission></a></li>
		</shiro:hasPermission>
	</content>

	<!--右侧内容部分开始-->
	<div class="supplierLine">
		<c:choose>
			<c:when test="${supplierId!=null && supplierId!='' }">
				<a href="${ctx}/supplier/supplierFirstForm?supplierId=${supplierId}" class="select">基本信息填写</a>
				<a href="${ctx}/supplier/supplierSecondForm?supplierId=${supplierId}">网站信息</a>
				<a href="${ctx}/supplier/supplierThirdForm?supplierId=${supplierId}">银行账户</a>
				<a href="${ctx}/supplier/supplierFourthForm?supplierId=${supplierId}">资质上传</a>
			</c:when>
			<c:otherwise>
				<a href="${ctx}/supplier/supplierFirstForm" class="select">基本信息填写</a>
				<a href="javascript:void(0)" onclick="checkFirstForm()">网站信息</a>
				<a href="javascript:void(0)" onclick="checkFirstForm()">银行账户</a>
				<a href="javascript:void(0)" onclick="checkFirstForm()">资质上传</a>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="sysdiv sysdiv_coupon">
		<form method="post" action="${ctx}/supplier/saveFirstForm" class="form-horizontal" id="inputForm">
			<input type="hidden" name="supplierId" <c:if test="${supplierId!=null }">value="${supplierId }"</c:if><c:if test="${supplierId==null }">value=""</c:if>/>
            <input type="hidden" id="supplierTypes" value="${supplierInfo.supplierType}">
            <p>
              <label><em class="xing">*</em>${supplierTypeName}：</label>
			<span class="checkboxdiv">
				<c:forEach var="supplierType" items="${supplierTypeList}" varStatus="status">
				<c:if test="${not empty supplierType.id}">
					<c:choose>
						<c:when test="${supplierType.label == '散拼' || supplierType.label == '签证' || supplierType.label == '领队'}">
							<c:if test="${companyId != '68'}">
								<input type="checkbox" name="supplierType" value="${supplierType.value}">
								<label for="${status.count}">${supplierType.label}</label>
							</c:if>
						</c:when>
						<c:when test="${supplierType.label == '餐厅'}">
							<c:if test="${companyId == '68'}">
								<input type="checkbox" name="supplierType" value="${supplierType.value}">
								<label for="${status.count}">${supplierType.label}</label>
							</c:if>
						</c:when>
						<c:when test="${supplierType.value == '其他'}">
							<input type="checkbox" name="supplierType" value="${supplierType.value}" onclick="on_hide(this);">
							<label for="${status.count}">${supplierType.label}</label>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="supplierType" value="${supplierType.value}">
							<label for="${status.count}">${supplierType.label}</label>
						</c:otherwise>
					</c:choose>
					</c:if>
				</c:forEach>
			</span>
			<input type="text" id="hide" name="supplierInfo.supplierOtherType" <c:if test="${fn:contains(supplierInfo.supplierType,'10')}">value="${supplierInfo.supplierOtherType}" style="display:block;"</c:if> <c:if test="${not fn:contains(supplierInfo.supplierType,'10')}">value="" style="display:none;"</c:if> maxlength="50">
			</p>
			<p>
				<label><em class="xing">*</em>${supplierLevelName }：</label>
	        	<span>
	        	<select class="" id="supplierLevel" name="supplierLevel">
                <option value="">请选择</option>
                	<c:forEach var="levelList" items="${supplierLevelList }">
                		<option value="${levelList.value}" <c:if test="${levelList.value == supplierInfo.supplierLevel}">selected="selected"</c:if> >${levelList.label}</option>
                	</c:forEach>
                </select>
	        	</span>
			</p>
            <p>
              <label><em class="xing">*</em>地接社品牌：</label>
              <span>
              <input type="text" id="supplierInfo.supplierBrand" name="supplierInfo.supplierBrand" value="${supplierInfo.supplierBrand}" maxlength="50" onblur="checktxt();">
              </span>
			</p>
            <p>
            <!-- 
              <label><c:if test="${fns:getUser().company.id eq '71' }"><em class="xing">*</em></c:if>公司名称：</label> -->
              <label><em class="xing">*</em>公司名称：</label>
              <span>
              <input type="text" id="supplierInfo.supplierName" name="supplierInfo.supplierName" value="${supplierInfo.supplierName}" maxlength="50">
              </span>
			</p>
			<p>
              <label>英文名称：</label>
              <span>
              <input type="text" name="supplierInfo.companyEnName" value="${supplierInfo.companyEnName}" maxlength="20">
              </span>
			</p>
			<div id="belongArea">
			<c:choose>
			<c:when test="${belongAreaList != null}">
            <c:forEach items="${belongAreaList}" var="dd" varStatus="status" step="4">
			<p name="areaspart">
              <c:choose>
	               	<c:when test="${status.index==0 }"><label>所属地区：</label></c:when>
	               	<c:otherwise><label>&nbsp;</label></c:otherwise>
	          </c:choose>
              <span class="sysselect_s countryCityArea">
                <select class="country" id="belongArea<fmt:formatNumber value="${status.index/4}" pattern="0"/>" name="belongArea<fmt:formatNumber value="${status.index/4}" pattern="0"/>" onchange="liandong('country',<fmt:formatNumber value="${status.index/4}" pattern="0"/>,this)">
                <option value="">国家</option>
                	<c:forEach items="${belongAreaList[status.index] }" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == belongAreaList[status.index+3].belongArea}">selected="selected"</c:if>>${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" id="belongsAreaProvince<fmt:formatNumber value="${status.index/4}" pattern="0"/>" name="belongsAreaProvince<fmt:formatNumber value="${status.index/4}" pattern="0"/>" onchange="liandong('province',<fmt:formatNumber value="${status.index/4}" pattern="0"/>,this)">
                	<option value="">省(直辖市)</option>
                	<c:forEach items="${belongAreaList[status.index+1] }" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == belongAreaList[status.index+3].belongsAreaProvince}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="city" id="belongsAreaCity<fmt:formatNumber value="${status.index/4}" pattern="0"/>" name="belongsAreaCity" pattern="0"/>">
                 	<option value="">市(区)</option>
                 	<c:forEach items="${belongAreaList[status.index+2] }" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == belongAreaList[status.index+3].belongsAreaCity}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
              </span>
	               	<c:choose>
	               	<c:when test="${status.index==0 }"><a class="ydbz_x" onclick="shopAddressAdd2(this)">添加</a></c:when>
	               	<c:otherwise><a class="ydbz_x gray" onclick="shopAddressDel(this)">删除</a></c:otherwise>
	               	</c:choose>
			</p>
            </c:forEach>
            </c:when>
            <c:otherwise>
            <p name="areaspart">
              <label>所属地区：</label>
              <span class="sysselect_s countryCityArea">
                <select class="country" id="belongArea0" name="belongArea0" onchange="liandong('country',0,this)">
                <option value="">国家</option>
                	<c:forEach  items="${areaMap}" var="aa">
                		<option value="${aa.key}">${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" id="belongsAreaProvince0" name="belongsAreaProvince0" onchange="liandong('province',0,this)">
                	<option value="">省(直辖市)</option>
                </select>
                <select class="city" id="belongsAreaCity0" name="belongsAreaCity">
                 	<option value="">市(区)</option>
                </select>
              </span><a class="ydbz_x" onclick="shopAddressAdd2(this)">添加</a>
			</p>
            </c:otherwise>
            </c:choose>
			</div>
            <p>
              <label>公司地址：</label>
              <span class="sysselect_s countryCityAddress">
                 <select class="country" name="supplierInfo.companyAddr">
                	<option value="">国家</option>
                	<c:forEach  items="${areaMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == supplierInfo.companyAddr}">selected="selected"</c:if>>${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" name="supplierInfo.companyAddrProvince">
                	<option value="">省(直辖市)</option>
                	<c:forEach  items="${addressProvinceMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == supplierInfo.companyAddrProvince}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="city" name="supplierInfo.companyAddrCity">
                 	<option value="">市(区)</option>
                 	<c:forEach  items="${addressCityMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == supplierInfo.companyAddrCity}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <input name="supplierInfo.detailAddr" value="${supplierInfo.detailAddr}" type="text" class="" maxlength="100">
              </span>
			</p>
            <p>
              <label>电话：</label>
              <span><input type="text" class="sysinput_s" name="supplierInfo.phoneCode" maxlength="8" value="${supplierInfo.phoneCode}"><span class="sysinput_span">-</span><input type="text" class="inputTxt" name="supplierInfo.phone" maxlength="20" value="${supplierInfo.phone}">例如：010-87475943</span>
			</p>
            <p>
              <label>传真：</label>
             <span><input type="text" class="sysinput_s" name="supplierInfo.faxCode" maxlength="8" value="${supplierInfo.faxCode}"><span class="sysinput_span">-</span><input type="text" class="inputTxt" name="supplierInfo.fax" maxlength="20" value="${supplierInfo.fax}">例如：010-87475943</span>
			</p>
            <div id="shopPeopleP">
            <c:choose>
			<c:when test="${supplierContactsList != null}">
            <c:forEach items="${supplierContactsList }" var="contacts" varStatus="status">
             <p name="shopPeopleP" class="shopPeopleNone">
	              	<label>联系人<em>${status.index +1}</em>：</label>
	              	<input type="hidden" name="contactId" value="${status.index}"/>
	               <span><input name="contacts.supplierContactses[${status.index}].contactName" type="text" maxlength="20" value="${contacts.contactName }"></span>
	               <label>手机：</label>
	               <span>
	               		<input name="contacts.supplierContactses[${status.index}].contactMobile" type="text" maxlength="20" value="${contacts.contactMobile }">
	               </span>
	               <c:if test="${status.index!=1 }">
	               		<c:choose>
	               			<c:when test="${status.index==0 }"><a class="ydbz_x" onclick="shopPeopleAdd2(this)">添加</a></c:when>
	               			<c:otherwise><a class="ydbz_x gray" onclick="shopPeopleDel2(this)">删除</a></c:otherwise>
	               		</c:choose>
	               </c:if>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input name="contacts.supplierContactses[${status.index}].contactPhone" type="text" maxlength="20" value="${contacts.contactPhone }"></span>
	               <label>传真：</label>
	               <span><input name="contacts.supplierContactses[${status.index}].contactFax" type="text" maxlength="20" value="${contacts.contactFax }"></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input name="contacts.supplierContactses[${status.index}].contactEmail" type="text" maxlength="50" value="${contacts.contactEmail }"></span>
	               <label>QQ：</label>
	               <span><input name="contacts.supplierContactses[${status.index}].contactQQ"  type="text" maxlength="20" value="${contacts.contactQQ }"></span>
	             </p>
             </c:forEach>
             </c:when>
             <c:otherwise>
             <p name="shopPeopleP" class="shopPeopleNone">
	              	<label>联系人<em>1</em>：</label>
	              	<input type="hidden" name="contactId" value="0"/>
	               <span><input name="contacts.supplierContactses[0].contactName" type="text" maxlength="20" value="${contacts.contactName }"></span>
	               <label>手机：</label>
	               <span>
	               		<input name="contacts.supplierContactses[0].contactMobile" type="text" maxlength="20" value="${contacts.contactMobile }">
	               </span>
	               <a class="ydbz_x" onclick="shopPeopleAdd2(this)">添加</a>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input name="contacts.supplierContactses[0].contactPhone" type="text" maxlength="20" value="${contacts.contactPhone }"></span>
	               <label>传真：</label>
	               <span><input name="contacts.supplierContactses[0].contactFax" type="text" maxlength="20" value="${contacts.contactFax }"></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input name="contacts.supplierContactses[0].contactEmail" type="text" maxlength="50" value="${contacts.contactEmail }"></span>
	               <label>QQ：</label>
	               <span><input name="contacts.supplierContactses[0].contactQQ"  type="text" maxlength="20" value="${contacts.contactQQ }"></span>
	             </p>
             <p name="shopPeopleP" class="shopPeopleNone">
	              	<label>联系人<em>2</em>：</label>
	              	<input type="hidden" name="contactId" value="1"/>
	               <span><input name="contacts.supplierContactses[1].contactName" type="text" maxlength="20" value="${contacts.contactName }"></span>
	               <label>手机：</label>
	               <span>
	               		<input name="contacts.supplierContactses[1].contactMobile" type="text" maxlength="20" value="${contacts.contactMobile }">
	               </span>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input name="contacts.supplierContactses[1].contactPhone" type="text" maxlength="20" value="${contacts.contactPhone }"></span>
	               <label>传真：</label>
	               <span><input name="contacts.supplierContactses[1].contactFax" type="text" maxlength="20" value="${contacts.contactFax }"></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input name="contacts.supplierContactses[1].contactEmail" type="text" maxlength="50" value="${contacts.contactEmail }"></span>
	               <label>QQ：</label>
	               <span><input name="contacts.supplierContactses[1].contactQQ"  type="text" maxlength="20" value="${contacts.contactQQ }"></span>
	             </p>
             </c:otherwise>
             </c:choose>
            </div>
			<p>
              <label>描述：</label>
              <span>
              <textarea name="supplierInfo.description" value="" rows="3" class="input-xlarge" maxlength="200">${supplierInfo.description }</textarea>
              </span>
			</p>
			<p>
			<label>公司LOGO：</label>
			<span>
			<input type="button" name="logo"  id="uploadMoreFile" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','passportfile',this,1);"/>
			<span id="upfileShow" class="seach_checkbox">
			<c:choose>
			<c:when test="${docInfo != null}">
				<a class="docname">
				<b>${docInfo.docName}</b>
				<input type="hidden" name="supplierInfo.logo" value="${docInfo.id}">
				</a>
				<a class="download" href="javascript:void(0);" id="${docInfo.id }" onclick="downloads('${docInfo.id}','${docInfo.docName}',1,true);">下载</a>
				<a class="delete" href="javascript:void(0);" id="${docInfo.id }" onclick="deleteFiles('${docInfo.id}', this);">删除</a>
			</c:when>
			</c:choose>
			</span>
			<span class="fileLogo"></span>
			</span>
			</p>
            <p>
              <label>&nbsp;</label>
              <span>
                <%--<a class="ydbz_s gray" href="${ctx}/supplier/supplierInfoList">返&nbsp;&nbsp;&nbsp;回</a>--%>
                <input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="ydbz_x btn " onclick="location.href='${ctx}/supplier/supplierInfoList'">
                <input type="button" value="下一步" class="ydbz_x btn btn-primary" onclick="check(${companyId});">
              </span>
			</p>
          </form>
        </div>
        <!--右侧内容部分结束--> 
</body>
</html>
