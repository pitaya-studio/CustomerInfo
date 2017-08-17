<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批发商管理-第3步-支付宝信息</title>
<meta name="decorator" content="wholesaler" />

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
    $(".abroadAndDomestic div:eq(0)").addClass("accountActive");
    $(".abroadAndDomestic div:eq(0)").on("click",function(){
        $(this).addClass("accountActive");
        $(this).siblings().removeClass("accountActive");
        $("#domesticAccount").show();
        $("#abroadAccount").hide();
        $("#zhifubaoAccount").hide();
    });
    $(".abroadAndDomestic div:eq(1)").on("click",function(){
        $(this).addClass("accountActive");
        $(this).siblings().removeClass("accountActive");
        $("#domesticAccount").hide();
        $("#abroadAccount").show();
        $("#zhifubaoAccount").hide();
        $("#abroadAccount .account").css("borderTop","none");
    });
    $(".abroadAndDomestic div:eq(2)").on("click",function(){
        $(this).addClass("accountActive");
        $(this).siblings().removeClass("accountActive");
        $("#domesticAccount").hide();
        $("#abroadAccount").hide();
        $("#zhifubaoAccount").show();
        $("#zhifubaoAccount .account").css("borderTop","none");
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
	});
}

var index = $("dl[name=abroadAccount]").length + $("dl[name=account]").length;
// 批发商银行账户添加

function zhifubaoAccountAdd(obj){
	$('#zhifubaoAccountContent').append('<dl class="account" name="zhifubaoAccount">' +
	'<dt><span><input id="zhifubaoDefaultFlag" name="zhifubaoDefaultFlag" type="radio" altattr="radio" value="0" onclick="setZhifubaoDefaultFlag(this);"/> </span> <b>设为默认</b>'+
	'<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="1"><em onclick="onDelete(this)">X 删除</em></dt>'+
	'<dd><span><i></i>支付宝名称：</span><input id="name" name="name" type="text" maxlength="50" value="" /></dd>'+
	'<dd><span><i></i>支付宝账号：</span><input id="account" name="account" type="text" maxlength="50" value="" /></dd>'+
	'<dd><span>备注：</span><textarea id="remarks" name="remarks" cols="" rows="" maxlength="200"></textarea></dd>'+
	'<input type="hidden" name="accountId" value="' + index + '" /></dl>');
}

function submit(){
	$("#inputForm").submit();
}

function check() {
	var checkedFlag = false;
	var nameAndAccounts = new Array();
	var account = $("#domesticAccount").find("#zhifubaoAccountContent .account");
	/* account.each(function() {
		var nameVal = $(this).find("#name").val(); 	// 支付宝名称
		var accountVal = $(this).find("#account").val(); // 支付宝账号
		
		nameAndAccounts.push(nameVal+","+accountVal);
		
		if($.trim(nameVal) == '') {
			checkedFlag = false;
		} else {
			checkedFlag = true;
		}
		if(!checkedFlag){
			top.$.jBox.tip('请填写支付宝名称', 'error');
			return checkedFlag;
		}

		if($.trim(accountVal) == '') {
			checkedFlag = false;
		} else {
			checkedFlag = true;
		}

		if(!checkedFlag){
			top.$.jBox.tip('请填写支付宝账号', 'error');
			return checkedFlag;
		}

	}); */
	if(account.length == 0 ){ // 未添加支付宝账号
		return true;
	}else if(account.length == 1){  // 添加的第一个支付宝账户设为默认
		account.find("input[name='realDefaultFlag']").val('0'); 
	}else if(account.length > 1){
		for(var x = 0 ; x < nameAndAccounts.length; x++){
			for(var y = 0; y < x; y++){
				if(nameAndAccounts[x]==nameAndAccounts[y]){
					top.$.jBox.tip('同一支付宝名称，支付账号不可以重复!', 'error');
					return false;
				}
			}
		}
	}
	
	/* if(!checkedFlag) {
		return checkedFlag;
	} */

	if (document.getElementById("id").value != null && document.getElementById("id").value != '') {
		top.$.jBox.tip('支付宝账户保存成功', 'success');
		return true;
	} else {
		top.$.jBox.tip('请先填写基本信息', 'error');
		return false;
	}
}


function checkFirstForm(){
	top.$.jBox.tip('请先填写基本信息', 'error');
}

function setZhifubaoDefaultFlag(obj) {
	// 1为未选中 0为选中
	//如果当前按钮为选中状态
	if($(obj).attr("checked")) {
		//所有  在  name="abroadDefaultFlag" 下的 name="realDefaultFlag" 都赋值为1
		$("[name=zhifubaoAccount]").each(function(index, obj) {
			$(this).find("[name=realDefaultFlag]").val("1");
		});
		//当前按钮下面的 隐藏域赋值为 0 
		$(obj).parents("[name=zhifubaoAccount]").find("[name=realDefaultFlag]").val("0");
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
				<a href="${ctx}/sys/office/officePlatBankInfoForm?id=${office.id}&parent.id=${office.parent.id}">银行账户</a>
				<a href="${ctx}/sys/office/officeZhifubaoInfoForm?id=${id}" class="select">支付宝账户</a>
			</c:when>
			<c:otherwise>
				<a href="${ctx}/sys/office/form">基本信息填写</a>
				<a href="${ctx}/sys/office/officePlatBankInfoForm?id=${office.id}&parent.id=${office.parent.id}">银行账户</a>
				<a href="" class="select">支付宝账户</a>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="abroadAndDomestic">
	</div>
	
	<form method="post" action="${ctx}/sys/office/saveOfficeZhifubaoInfoForm" class="form-horizontal" id="inputForm" onsubmit="return check()">
		<input type="hidden" id="id" name="id" <c:if test="${id!=null && id != '' }">value="${id }"</c:if>
			<c:if test="${id==null || id == '' }">value=""</c:if> />
		<div class="qdgl-cen" id="domesticAccount" style="margin-top: 10px">
			<div id=zhifubaoAccountContent>
				<c:choose>
					<c:when test="${zfbs != null }">
						<c:forEach items="${zfbs }" var="zfb" varStatus="status3">
							<dl class="account" name="zhifubaoAccount">
								<dt>
									<span><input id="zhifubaoDefaultFlag" name="zhifubaoDefaultFlag" type="radio" altattr="radio" <c:if test="${zfb.defaultFlag == '0' }">checked="checked"</c:if> value="${zfb.defaultFlag }" onclick="setZhifubaoDefaultFlag(this);"/> </span> <b>设为默认</b>
									<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="${zfb.defaultFlag }">
									<c:if test="${status3.index > 0}">
										<em onclick="onDelete(this)">X 删除</em>
									</c:if>
								</dt>
								<dd>
									<span><i></i>支付宝名称：</span><input id="name" name="name" type="text" maxlength="50" value="${zfb.name}"/>
								</dd>
								<dd>
									<span><i></i>支付宝账户：</span><input id="account" name="account" type="text" maxlength="50" value="${zfb.account}" />
								</dd>
								
								<dd>
									<span>备注：</span>
									<textarea id="remarks" name="remarks" cols="" rows="" maxlength="200">${zfb.remarks}</textarea>
								</dd>
								<input type="hidden" name="accountId" value="0" />
							</dl>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<dl class="account" name="zhifubaoAccount">
							<dt>
								<span><input id="abroadDefaultFlag" name="abroadDefaultFlag" type="radio" altattr="radio" checked="checked" value="4" onclick="setZhifubaoDefaultFlag(this);"/> </span> <b>设为默认</b>
								<input type="hidden"  class="realDefaultFlag" name="realDefaultFlag" value="1">
								<em onclick="onDelete(this)">X 删除</em>
							</dt>
							<dd>
								<span><i></i>支付宝名称：</span><input id="name" name="name" type="text" maxlength="50" value="" />
							</dd>
							<dd>
								<span><i></i>支付宝账户：</span><input id="account" name="account" type="text" maxlength="50" value="" />
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
				<div class="ydbz_s" onclick="zhifubaoAccountAdd(this)">继续添加账户+</div>
			</div>
		</div>
		
		<div class="dbaniu " style=" margin-left:100px;">
			<span><a class="ydbz_x gray" href="${ctx}/sys/office">返&nbsp;&nbsp;&nbsp;回</a> 
			<a class="ydbz_x" href="${ctx}/sys/office/officePlatBankInfoForm?id=${office.id}&parent.id=${office.parent.id}">上一步</a>
			<input type="submit" value="提交" class="ydbz_x submit"></span>
		</div>
	</form>
</body>
</html>
