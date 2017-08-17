<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传出团通知</title>
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css" />
<!--[if lte IE 6]><link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<!--<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<!--<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />-->
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
function inFileName(obj){
    var res = $(obj).val();
    var dest = $(obj).parent().parent().find("input[name='fileLogo']")[0];
    $(dest).val(res);
    $("#submit").show();
}
function removeFileUpload(obj){
    $(obj).parent().prev().find("input[name='fileLogo']").val('');
    $(obj).parent().prev().find("input[name='groupFile']").val('');
    $('.clickA',window.parent.document).prev('[name=openDateFiles]').val('');
    $('.clickA',window.parent.document).attr('id','');
}
function addFileUpload(){
	if($("#filediv p"))$("#filediv p").remove();
	$("#filediv").append($("#hiddendiv").html());
}
function check(){
	if($("#inputForm input[name='fileLogo']").length==0){
		alert('请先添加要上传的出团通知后再提交！');
		return false;
	}
	return true;
}
$(function()
{
    $('.upload_cen dl:last').css({'border-bottom':'0px'}); 
    if(${docInfo.id ne null}) {
    	$(".clickA",window.parent.document).attr("id","${docInfo.id}");
    }
    
});
</script>
</head>
<body>
<!--上传支付凭证开始-->
    <form:form id="inputForm" action="${ctx}/activity/manager/uploadGroupFile" enctype="multipart/form-data" method="post" onsubmit="return check();">
    <tags:message content="${message}"/>
	    <div id="hiddendiv">
		    <dl class="teamNotice">
		    	<dt>出团通知：</dt>
			    <dd>
				    <input name="fileLogo" type="text" value="${docInfo.docName}" readonly="readonly" style="width:auto;" />
				    <span class="pr" style="vertical-align:top;">
					    <input type="file" onchange="javascript:inFileName(this)" hidefocus="" size="1" name="groupFile" value="${docInfo.docName}" class="inputFile"/>
					    <input type="button"  value="选择文件" class="btninput_file" />
				    </span>
			    </dd>
		    </dl>
	    </div>
	    <div class="release_next_add" style="margin-top:40px;">
		   
		        <input id="submit" type="submit" value="提交" class="btn btn-primary" style="
		         <c:if test="${docInfo.docName ne null and docInfo.docName ne ''}">
		         	display:none
		    	 </c:if>
		        " />
    		<input type="button" class="btn btn-primary gray" value="删除" class="btn btn-primary" onclick="javascript:removeFileUpload(this);")/>
	        <input type="button" value="关闭" onClick="window.parent.window.jBox.close(); $('.clickA',window.parent.document).prev('input').val($('.clickA',window.parent.document).attr('id')); $('.clickA',window.parent.document).removeClass('clickA');" class="btn btn-primary gray">
    	</div>
    </form:form>
<!--上传支付凭结束-->
</body>
</html>