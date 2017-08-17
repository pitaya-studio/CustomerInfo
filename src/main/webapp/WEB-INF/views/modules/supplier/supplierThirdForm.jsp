<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地接社管理-新增第3步-银行账户信息</title>
<meta name="decorator" content="wholesaler"/>
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
	account_tj();
});
var bankNum = 1;

function onDelete(){
	if(bankNum == 1){
		bankNum = $("dl[name=account]").length;
	}

}

// 地接社银行账户删除
function account_tj(){
	$('.account dt em').live('click',function(){$(this).parents('.account').remove();
	var accountName = document.getElementsByName('defaultFlag');
	for (var i = 0; i < accountName.length; i++) {
		if (accountName[i].checked==true) {
			return true;
		}
	}
	document.getElementById('defaultFlag').checked=true;});
	$('.account_tj').click(function(){
		var ykhtml=$(this).parent().next('.account').html();
		$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
		
	});
}
// 地接社银行账户添加
function accountAdd(obj){
	var index = $("dl[name=account]").length;
	if(bankNum!=1){
		index = bankNum;
	}
	$('#account').append('<dl name="account" class="account" style="display:block;">'+
		'<dt><span><input id="defaultFlag" name="defaultFlag" type="radio" value="' + index + '"/></span><b>设为默认账户</b><em onclick="onDelete(this)">X 删除</em></dt>'+
		'<dd><span>账户名：</span><input id="banks[' + index + '].accountName" name="banks[' + index + '].accountName" type="text" maxlength="100" value="" /></dd>'+
		'<dd><span>开户行名称：</span><input id="banks[' + index + '].bankName" name="banks[' + index + '].bankName" type="text" maxlength="100" value="" /></dd>'+
		'<dd><span>开户行地址：</span><input id="banks[' + index + '].bankAddr" name="banks[' + index + '].bankAddr" type="text" maxlength="200" value="" /></dd>'+
		'<dd><span>账户号码：</span><input id="banks[' + index + '].bankAccountCode" name="banks[' + index + '].bankAccountCode" type="text" maxlength="50" value="" /></dd>'+
		'<dd><span>备注：</span><textarea id="banks[' + index + '].remarks" name="banks[' + index + '].remarks" cols="" rows="" maxlength="200"></textarea></dd>'+
		'<input type="hidden" name="accountId" value="' + index + '"/></dl>');
	if(bankNum!=1){
		bankNum += 1;
	}
}

function submit(){
	$("#inputForm").submit();
}

function check(supplierId){
	if (document.getElementById("supplierId").value == null || document.getElementById("supplierId").value == "") {
		top.$.jBox.tip('请先填写基本信息', 'error');
		return false;
	}
	/* if (document.getElementById("supplierId").value != null && document.getElementById("supplierId").value != "") {
		top.$.jBox.tip('第三步保存成功', 'success');
		return true;
	} */
	if (supplierId != null &&　supplierId !="") {
		$("#inputForm").submit();
		top.$.jBox.tip('第三步保存成功', 'success');
		//return true;
	}
}

function checkFirstForm(){
	top.$.jBox.tip('请先填写基本信息', 'error');
}
</script>
</head>
<body>
	<page:applyDecorator name="supplier_op_head">
		<page:param name="current">supplierAdd</page:param>
	</page:applyDecorator>
<!--右侧内容部分开始-->
	<div class="supplierLine">
		<c:choose>
			<c:when test="${supplierId!=null && supplierId!='' }">
				<a href="${ctx}/supplier/supplierFirstForm?supplierId=${supplierId}">基本信息填写</a>
				<a href="${ctx}/supplier/supplierSecondForm?supplierId=${supplierId}">网站信息</a>
				<a href="${ctx}/supplier/supplierThirdForm?supplierId=${supplierId}" class="select">银行账户</a>
				<a href="${ctx}/supplier/supplierFourthForm?supplierId=${supplierId}">资质上传</a>
			</c:when>
			<c:otherwise>
				<a href="${ctx}/supplier/supplierFirstForm">基本信息填写</a>
				<a href="javascript:void(0)" onclick="checkFirstForm()">网站信息</a>
				<a href="${ctx}/supplier/supplierThirdForm" class="select">银行账户</a>
				<a href="javascript:void(0)" onclick="checkFirstForm()">资质上传</a>
			</c:otherwise>
		</c:choose>
	</div>
	<form method="post" action="${ctx}/supplier/saveThirdForm" class="form-horizontal" id="inputForm">
	<input type="hidden" id="supplierId" name="supplierId" <c:if test="${supplierId!=null && supplierId!='' }">value="${supplierId }"</c:if><c:if test="${supplierId==null || supplierId=='' }">value=""</c:if>/>
	<div class="qdgl-cen">
	<div id="account">
		<c:choose>
		<c:when test="${banks != null }">
		<c:forEach items="${banks }" var="bank" varStatus="status">
		<dl name="account" class="account"> 
			<input type="hidden" name="banks[${status.index}].id" value="${bank[0]}"/>
			<dt><span><input id="defaultFlag" name="defaultFlag" type="radio" altattr="radio" <c:if test="${bank[1] == '0' }">checked="checked"</c:if> value="${status.index}" /></span>
			<b>设为默认账户</b><c:if test="${status.index > 0}"><em onclick="onDelete(this)">X 删除</em></c:if>
			</dt>
			<dd><span>账户名：</span><input id="banks[${status.index}].accountName" name="banks[${status.index}].accountName" type="text" maxlength="100" value="${bank[2]}" /></dd>
			<dd><span>开户行名称：</span><input id="banks[${status.index}].bankName" name="banks[${status.index}].bankName" type="text" maxlength="100" value="${bank[3]}" /></dd>
			<dd><span>开户行地址：</span><input id="banks[${status.index}].bankAddr" name="banks[${status.index}].bankAddr" type="text" maxlength="200" value="${bank[4]}" /></dd>
			<dd><span>账户号码：</span><input id="banks[${status.index}].bankAccountCode" name="banks[${status.index}].bankAccountCode" type="text" maxlength="50" value="${bank[5]}" /></dd>
			<dd><span>备注：</span><textarea id="banks[${status.index}].remarks" name="banks[${status.index}].remarks" cols="" rows="" maxlength="200">${bank[6]}</textarea></dd>
			<input type="hidden" name="accountId" value="${status.index}"/>
		</dl>
		</c:forEach>
		</c:when>
		<c:otherwise>
			<dl name="account" class="account"> 
			<dt><span><input id="defaultFlag" name="defaultFlag" type="radio" altattr="radio" value="0" checked="checked"/></span>
			<b>设为默认账户</b>
			</dt>
			<dd><span>账户名：</span><input id="banks[0].accountName" name="banks[0].accountName" type="text" maxlength="100" value="" /></dd>
			<dd><span>开户行名称：</span><input id="banks[0].bankName" name="banks[0].bankName" type="text" maxlength="100" value="" /></dd>
			<dd><span>开户行地址：</span><input id="banks[0].bankAddr" name="banks[0].bankAddr" type="text" maxlength="200" value="" /></dd>
			<dd><span>账户号码：</span><input id="banks[0].bankAccountCode" name="banks[0].bankAccountCode" type="text" maxlength="50" value="" /></dd>
			<dd><span>备注：</span><textarea id="banks[0].remarks" name="banks[0].remarks" cols="" rows="" maxlength="200"></textarea></dd>
			<input type="hidden" name="accountId" value="0"/>
			
		</dl>
		</c:otherwise>
		</c:choose>
	</div>
		<div class="yh-account">
		<%--<div class="ydbz_s" onclick="accountAdd(this)">继续添加账户+</div>--%>
		<input type="button" value="继续添加账户" class="ydbz_x btn btn-primary" onclick="accountAdd(this)">
		</div>
	</div>
	<div class="dbaniu " style=" margin-left:100px;">
	<span>
		<%--<a class="ydbz_x gray" href="${ctx}/supplier/supplierInfoList">返&nbsp;&nbsp;&nbsp;回</a>--%>
		<%--<a class="ydbz_x" href="${ctx}/supplier/supplierSecondForm?supplierId=${supplierId}">上一步</a>--%>
		<%--<input type="submit" value="下一步" class="ydbz_x submit" onclick="check(${supplierId});">--%>
		<input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="ydbz_x btn" onclick="location.href='${ctx}/supplier/supplierInfoList'">
		<input type="button" value="上一步" class="ydbz_x btn" onclick="location.href='${ctx}/supplier/supplierSecondForm?supplierId=${supplierId}'">
		<input type="submit" value="下一步" class="ydbz_x btn btn-primary submit" onclick="check(${supplierId});">

	</span>
	</div>
	</form>
<!--右侧内容部分结束-->
</body>
</html>
