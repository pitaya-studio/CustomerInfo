<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传订金凭证</title>
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
function inFileName(obj){
    var res = $(obj).val();
    var dest = $(obj).parent().find("input[name='fileLogo']")[0];
    $(dest).val(res);
}
function removeFileUpload(obj){
    $(obj).parent().parent().remove();
}
function addFileUpload(){
	if($("#filediv p"))$("#filediv p").remove();
	$("#filediv").append($("#hiddendiv").html());
}
function check(){
	if($("#inputForm input[name='fileLogo']").length==0){
		alert('请先添加要上传的支付凭证后再提交！');
		return false;
	}
	return true;
}
$(function()
{
    $('.upload_cen dl:last').css({'border-bottom':'0px'}); 
});
</script>
</head>
<body>
<div class="jbox-content"  style="width:560px; background:#fff; padding:10px 0px; margin:20px auto;">
<!--上传支付凭证开始-->
    <div class="upload">
        上传支付凭证：<input type="button" value="+添加" onclick="javascript:addFileUpload();" > 
    </div>
    <div id="hiddendiv" style="display:none"><dl><dt><b>凭证：</b><span><input name="fileLogo" type="text" value="" /></span><input type="file" onchange="javascript:inFileName(this)" hidefocus="" size="1" name="payVoucher"><input type="button"  value="选择文件"></dt><dd><input type="button" class="btn btn-primary gray" value="删除" class="btn btn-primary" onclick="javascript:removeFileUpload(this);")></dd></dl></div>
    <form:form id="inputForm" action="${ctx}/stock/manager/apartGroup/upload" enctype="multipart/form-data" method="post" onsubmit="return check();">
    <tags:message content="${message}"/>
    <div id="filediv" class="upload_cen">
    <c:if test="${empty activityReserveFileList }"><p>未添加支付凭证，点击添加按钮上传。</p></c:if>
    <c:forEach items="${activityReserveFileList}" var="file">
    <b>凭证：</b><span>${file.fileName }</span><br/>
    </c:forEach> 
    </div>
    <div class="release_next_add">
            <input type="hidden" name="activityGroupId" value="${param.activityGroupId }"/><input type="hidden" name="srcActivityId" value="${param.srcActivityId }"/><input type="hidden" name="agentId" value="${param.agentId }"/>
             <input type="button" value="关闭" onClick="window.parent.window.jBox.close()" class="btn btn-primary gray">
             <input type="submit" value="提交" class="btn btn-primary"/>
    </div>
    </form:form>
<!--上传支付凭结束-->
</div>
</body>
</html>