<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批发商管理-第2步-银行账户信息</title>
<meta name="decorator" content="wholesaler" />

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
	account_tj();
	$("#domesticAccount .account").css("borderTop","none");
    $(".abroadAndDomestic div:first").addClass("accountActive");
    $(".abroadAndDomestic div:first").on("click",function(){
        $(this).addClass("accountActive");
        $(this).siblings().removeClass("accountActive");
        $("#domesticAccount").show();
        $("#abroadAccount").hide();
    });
    $(".abroadAndDomestic div:last").on("click",function(){
        $(this).addClass("accountActive");
        $(this).siblings().removeClass("accountActive");
        $("#domesticAccount").hide();
        $("#abroadAccount").show();
        $("#abroadAccount .account").css("borderTop","none");
    });
});
var bankNum = 1;

function onDelete(){
	if(bankNum == 1){
		bankNum = $("dl[name=account]").length;
	}

}

// 批发商银行账户删除
function account_tj(){
	$('.account dt em').live('click',function(){
		$(this).parents('.account').remove();
		var accountName = document.getElementsByName('defaultFlag');
		//for (var i = 0; i < accountName.length; i++) {
			//if (accountName[i].checked==true) {
				//return true;
			//}
		//}
		//document.getElementById('defaultFlag').checked=true;});
		//$('.account_tj').click(function(){
		//var ykhtml=$(this).parent().next('.account').html();
		//$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
		
	});
}

var index = $("dl[name=abroadAccount]").length + $("dl[name=account]").length;
// 批发商银行账户添加
function accountAdd(obj){
	$('#account').append('<dl name="account" class="account" style="display:block;">'+
		'<dt><span><input id="defaultFlag" name="defaultFlag" type="radio" value="1" onclick="setDefaultFlagVal(this);"/></span><b>设为默认账户</b>'+
		'<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="1"><em onclick="onDelete(this)">X 删除</em></dt>'+
		'<dd><span><i>*</i>账户名：</span><input id="accountName" name="accountName" type="text" maxlength="100" value="" /></dd>'+
		'<input type="hidden" id="belongType" name="belongType" value="1">'+
		'<dd><span><i>*</i>开户行名称：</span><input id="bankName" name="bankName" type="text" maxlength="100" value="" /></dd>'+
		'<dd><span><i>*</i>开户行地址：</span><input id="bankAddr" name="bankAddr" type="text" maxlength="200" value="" /></dd>'+
		'<dd><span><i>*</i>账户号码：</span><input id="bankAccountCode" name="bankAccountCode" type="text" maxlength="50" value="" /></dd>'+
		'<dd><span>备注：</span><textarea id="remarks" name="remarks" cols="" rows="" maxlength="200"></textarea></dd>'+
		'<input type="hidden" name="accountId" value="' + index + '"/></dl>');
}

function abroadAccountAdd(obj){
	$('#abroadAccountContent').append('<dl class="account" name="abroadAccount">' +
	'<dt><span><input id="abroadDefaultFlag" name="abroadDefaultFlag" type="radio" altattr="radio" value="3" onclick="setAbroadDefaultFlag(this);"/> </span> <b>设为默认账户</b>'+
	'<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="3"><em onclick="onDelete(this)">X 删除</em></dt>'+
	'<input type="hidden" id="belongType" name="belongType" value="2">'+ 
	'<dd><span><i></i>账户名：</span><input id="accountName" name="accountName" type="text" maxlength="100" value="" /></dd>'+
	'<dd><span><i></i>开户行名称：</span><input id="bankName" name="bankName" type="text" maxlength="100" value="" /></dd>'+
	'<dd><span><i></i>开户行地址：</span><input id="bankAddr" name="bankAddr" type="text" maxlength="200" value="" /></dd>' +
	'<dd><span><i></i>账户号码：</span><input id="bankAccountCode" name="bankAccountCode" type="text" maxlength="50" value="" /></dd>'+
	'<dd><span>Rounting：</span><input id="Rounting" name="Rounting" type="text" value="" /></dd>'+
	'<dd><span>Swift Number：</span><input id="swiftNum" name="swiftNum" type="text" value="" /></dd>'+
	'<dd><span>Phone Number：</span><input id="phoneNum" name="phoneNum" type="text" value="" /></dd>'+
	'<dd><span>备注：</span><textarea id="remarks" name="remarks" cols="" rows="" maxlength="200"></textarea></dd>'+
	'<input type="hidden" name="accountId" value="' + index + '" /></dl>');
}

function submit(){
	$("#inputForm").submit();
}

function check() {
	var checkedFlag = false;
	$("#domesticAccount").find("#account .account").each(function() {
		var accountNameVal = $(this).find("#accountName").val(); 	// 账户名
		var bankNameVal = $(this).find("#bankName").val(); 		// 开户行名称
		var bankAddrVal = $(this).find("#bankAddr").val(); 		// 开户行地址
		var bankAccountCodeVal = $(this).find("#bankAccountCode").val(); // 账户号码
		if($.trim(accountNameVal) == '') {
			checkedFlag = false;
		} else {
			checkedFlag = true;
		}
		if(!checkedFlag){
			top.$.jBox.tip('请填写境内账户名', 'error');
			return checkedFlag;
		}

		if($.trim(bankNameVal) == '') {
			checkedFlag = false;
		} else {
			checkedFlag = true;
		}
		if(!checkedFlag){
			top.$.jBox.tip('请填写境内开户行名称', 'error');
			return checkedFlag;
		}

		if($.trim(bankAddrVal) == '') {
			checkedFlag = false;
		} else {
			checkedFlag = true;
		}

		if(!checkedFlag){
			top.$.jBox.tip('请填写境内开户行地址', 'error');
			return checkedFlag;
		}

		if($.trim(bankAccountCodeVal) == '') {
			checkedFlag = false;
		} else {
			checkedFlag = true;
		}

		if(!checkedFlag){
			top.$.jBox.tip('请填写境内账户号码', 'error');
			return checkedFlag;
		}

	});
	if(!checkedFlag) {
		return checkedFlag;
	}

	if (document.getElementById("id").value != null && document.getElementById("id").value != '') {
		if($("input[type='submit']").val() == '提交'){
			top.$.jBox.tip('银行账户保存成功', 'success');
		}
		return true;
	} else {
		top.$.jBox.tip('请先填写基本信息', 'error');
		return false;
	}
}

function checkFirstForm(){
	top.$.jBox.tip('请先填写基本信息', 'error');
}

function setDefaultFlagVal(obj) {
	// 1为未选中， 2为选中
	//如果当前按钮为选中状态
	if($(obj).attr("checked")) {
		//所有  在  name="defaultFlag" 下的 name="realDefaultFlag" 都赋值为1
		$("[name=account]").each(function(index, obj) {
			$(this).find("[name=realDefaultFlag]").val("1");
		});
		//当前按钮下面的 隐藏域赋值为 0 
		$(obj).parents("[name=account]").find("[name=realDefaultFlag]").val("0");
	}
}

function setAbroadDefaultFlag(obj) {
	// 3 位未选中 4 为选中
	//如果当前按钮为选中状态
	if($(obj).attr("checked")) {
		//所有  在  name="abroadDefaultFlag" 下的 name="realDefaultFlag" 都赋值为3
		$("[name=abroadAccount]").each(function(index, obj) {
			$(this).find("[name=realDefaultFlag]").val("3");
		});
		//当前按钮下面的 隐藏域赋值为 2 
		$(obj).parents("[name=abroadAccount]").find("[name=realDefaultFlag]").val("4");
	}
}

</script>


<style type="text/css">
.abroadAndDomestic {
	margin-top: 30px;
	margin-left: 30px;
}

.abroadAndDomestic div {
	width: 80px;
	display: inline-block;
	height: 40px;
	line-height: 40px;
	text-align: center;
	cursor: pointer;
	border-top: 1px solid #ccc;
	border-bottom: 1px solid #ccc;
	border-right: 1px solid #ccc;
	padding: 0;
}

.abroadAndDomestic div:first-child {
	border-left: 1px solid #ccc;
}

.accountActive {
	background-color: #62afe7;
	color: white;
}
</style>
</head>
<body>
	<content tag="three_level_menu">
	<li><a href="${ctx}/sys/office/">批发商列表</a>
	</li>
	<li class="active"><a
		href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">批发商<shiro:hasPermission
				name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission>
			<shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission>
	</a>
	</li>
	</content>
	<!--右侧内容部分开始-->
	<div class="supplierLine">
		<c:choose>
			<c:when test="${id!=null && id!='' }">
				<a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">基本信息填写</a>
				<a href="" class="select">银行账户</a>
				<c:if test="${fns:getUser().company.uuid == '7a81a26b77a811e5bc1e000c29cf2586' }">
					<a href="${ctx}/sys/office/officeZhifubaoInfoForm?id=${office.id}">支付宝账户</a>
				</c:if>
			</c:when>
			<c:otherwise>
				<a href="${ctx}/sys/office/form">基本信息填写</a>
				<a href="" class="select">银行账户</a>
				<c:if test="${fns:getUser().company.uuid == '7a81a26b77a811e5bc1e000c29cf2586' }">
					<a href="${ctx}/sys/office/officeZhifubaoInfoForm?id=${office.id}">支付宝账户</a>
				</c:if>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="abroadAndDomestic">
		<div>境内账户</div>
		<div>境外账户</div>
	</div>
	
	<form method="post" action="${ctx}/sys/office/saveOfficePlatBankInfoForm" class="form-horizontal" id="inputForm" onsubmit="return check()">
		<input type="hidden" id="id" name="id" <c:if test="${id!=null && id != '' }">value="${id }"</c:if>
			<c:if test="${id==null || id == '' }">value=""</c:if> />
		<div class="qdgl-cen" id="domesticAccount" style="margin-top: 10px">
			<div id="account">
				<c:choose>
					<c:when test="${banks != null }">
						<c:forEach items="${banks }" var="bank" varStatus="status">
							<!-- 境内账户回显 -->
							<c:if test="${bank[1] == '1' }">
								<dl name="account" class="account">
								<input type="hidden" name="id" value="${bank[0]}" />
								<dt>
									<span><input id="defaultFlag" name="defaultFlag" type="radio" altattr="radio"
										<c:if test="${bank[2] == '0' }">checked="checked"</c:if> value="${bank[2] }" onclick="setDefaultFlagVal(this);"/>
									</span> <b>设为默认账户</b>
									<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="${bank[2] }">
									<c:if test="${status.index > 0}">
										<em onclick="onDelete(this)">X 删除</em>
									</c:if>
								</dt>
								<input type="hidden" id="belongType" name="belongType" value="1">
								<dd>
									<span><i>*</i>账户名：</span><input id="accountName"
										name="accountName" type="text" maxlength="100" value="${bank[3]}" />
								</dd>
								<dd>
									<span><i>*</i>开户行名称：</span><input id="bankName"
										name="bankName" type="text" maxlength="100" value="${bank[4]}" />
								</dd>
								<dd>
									<span><i>*</i>开户行地址：</span><input id="bankAddr"
										name="bankAddr" type="text" maxlength="200" value="${bank[5]}" />
								</dd>
								<dd>
									<span><i>*</i>账户号码：</span><input id="bankAccountCode"
										name="bankAccountCode" type="text" maxlength="50"
										value="${bank[6]}" />
								</dd>
								<dd>
									<span>备注：</span>
									<textarea id="remarks" name="remarks" cols=""
										rows="" maxlength="200">${bank[7]}</textarea>
								</dd>
								<input type="hidden" name="accountId" value="${status.index}" />
							</dl>
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<dl name="account" class="account">
							<dt>
								<span><input id="defaultFlag" name="defaultFlag" type="radio" altattr="radio" checked="checked" onclick="setDefaultFlagVal(this)"/>
								</span> <b>设为默认账户</b>
								<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="0">
							</dt>
							<input type="hidden" id="belongType" name="belongType" value="1">
							<dd>
								<span><i>*</i>账户名：</span><input id="accountName" name="accountName"
									type="text" maxlength="100" value="" />
							</dd>
							<dd>
								<span><i>*</i>开户行名称：</span><input id="bankName" name="bankName"
									type="text" maxlength="100" value="" />
							</dd>
							<dd>
								<span><i>*</i>开户行地址：</span><input id="bankAddr" name="bankAddr"
									type="text" maxlength="200" value="" />
							</dd>
							<dd>
								<span><i>*</i>账户号码：</span><input id="bankAccountCode"
									name="bankAccountCode" type="text" maxlength="50" value="" />
							</dd>
							<dd>
								<span>备注：</span>
								<textarea id="remarks" name="remarks" cols="" rows="" maxlength="200"></textarea>
							</dd>
							<input type="hidden" name="accountId" value="0" />

						</dl>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="yh-account">
				<div class="ydbz_s" onclick="accountAdd(this)">继续添加账户+</div>
			</div>
		</div>
		
		<div class="qdgl-cen" id="abroadAccount" style="margin-top: 10px;display: none">
			<div id="abroadAccountContent">
			<c:choose>
				<c:when test="${banks != null }">
					<c:forEach items="${banks }" var="bank2" varStatus="status2">
					<!-- 境外账户回显 -->
					<c:if test="${bank2[1] == '2' }">
					<dl class="account" name="abroadAccount">
						<dt>
							<span><input id="abroadDefaultFlag" name="abroadDefaultFlag" type="radio" altattr="radio" <c:if test="${bank2[2] == '4' }">checked="checked"</c:if> value="${bank2[2] }" onclick="setAbroadDefaultFlag(this);"/> </span> <b>设为默认账户</b>
							<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="${bank2[2] }">
							<c:if test="${status2.index > 0}">
								<em onclick="onDelete(this)">X 删除</em>
							</c:if>
						</dt>
						<input type="hidden" id="belongType" name="belongType" value="2">
						<dd>
							<span><i></i>账户名：</span><input id="accountName" name="accountName" type="text" maxlength="100" value="${bank2[3]}" />
						</dd>
						<dd>
							<span><i></i>开户行名称：</span><input id="bankName" name="bankName" type="text" maxlength="100" value="${bank2[4]}" />
						</dd>
						<dd>
							<span><i></i>开户行地址：</span><input id="bankAddr" name="bankAddr" type="text" maxlength="200" value="${bank2[5]}" />
						</dd>
						<dd>
							<span><i></i>账户号码：</span><input id="bankAccountCode" name="bankAccountCode" type="text" maxlength="50" value="${bank2[6]}" />
						</dd>
						
						<dd>
							<span>Rounting：</span><input id="Rounting" name="Rounting" type="text" value="${bank2[8]}" />
						</dd>
						<dd>
							<span>Swift Number：</span><input id="swiftNum" name="swiftNum" type="text" value="${bank2[9]}" />
						</dd>
						<dd>
							<span>Phone Number：</span><input id="phoneNum" name="phoneNum" type="text" value="${bank2[10]}" />
						</dd>
						<dd>
							<span>备注：</span>
							<textarea id="remarks" name="remarks" cols="" rows="" maxlength="200">${bank2[6]}</textarea>
						</dd>
						<input type="hidden" name="accountId" value="0" />
					</dl>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<dl class="account" name="abroadAccount">
					<dt>
						<span><input id="abroadDefaultFlag" name="abroadDefaultFlag" type="radio" altattr="radio" checked="checked" value="4" onclick="setAbroadDefaultFlag(this);"/> </span> <b>设为默认账户</b>
						<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="4">
					</dt>
					<input type="hidden" id="belongType" name="belongType" value="2">
					<dd>
						<span><i></i>账户名：</span><input id="accountName" name="accountName" type="text" maxlength="100" value="" />
					</dd>
					<dd>
						<span><i></i>开户行名称：</span><input id="bankName" name="bankName" type="text" maxlength="100" value="" />
					</dd>
					<dd>
						<span><i></i>开户行地址：</span><input id="bankAddr" name="bankAddr" type="text" maxlength="200" value="" />
					</dd>
					<dd>
						<span><i></i>账户号码：</span><input id="bankAccountCode" name="bankAccountCode" type="text" maxlength="50" value="" />
					</dd>
					
					<dd>
						<span>Rounting：</span><input id="Rounting" name="Rounting" type="text" value="" />
					</dd>
					<dd>
						<span>Swift Number：</span><input id="swiftNum" name="swiftNum" type="text" value="" />
					</dd>
					<dd>
						<span>Phone Number：</span><input id="phoneNum" name="phoneNum" type="text" value="" />
					</dd>
					<dd>
						<span>备注：</span>
						<textarea id="remarks" name="remarks" cols="" rows="" maxlength="200"></textarea>
					</dd>
					<input type="hidden" name="accountId" value="0" />
				</dl>
			</c:otherwise>
			</c:choose>
			</div>
			<div class="yh-account">
				<div class="ydbz_s" onclick="abroadAccountAdd(this)">继续添加账户+</div>
			</div>
		</div>
		
		<div class="dbaniu " style=" margin-left:100px;">
			<span><a class="ydbz_x gray" href="${ctx}/sys/office">返&nbsp;&nbsp;&nbsp;回</a> 
			<a class="ydbz_x" href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">上一步</a>
			<c:choose>
				<c:when test="${fns:getUser().company.uuid == '7a81a26b77a811e5bc1e000c29cf2586' }">
					<input type="submit" name="submit" value="下一步" class="ydbz_x submit"></span>
				</c:when>
				<c:otherwise>
					<input type="submit" name="submit" value="提交" class="ydbz_x submit"></span>
				</c:otherwise>
			</c:choose>
		</div>
	</form>
	<!--右侧内容部分结束-->
</body>
</html>
