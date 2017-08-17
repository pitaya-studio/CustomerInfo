<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品切位</title>
<meta name="decorator" content="wholesaler"/>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>  
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
 {       $('.upload_cen dl:last').css({'border-bottom':'0px'});


/* var html=' ';
var _agentSelect = $("#content").clone();
var $select=$('<p></p>').append(_agentSelect);
      $.jBox($select.html(), {
                    title: "支付凭证",
                    width: 580,
                    height: 460,
                    showClose:false,
                    persistent:true,
                    buttons: {}
          }); */

 
});


function jumpto(){
    window.location.href="${ctx}/stock/manager/airticket/detail/${srcActivityId }";
}



</script>
</head>
<body>
    <page:applyDecorator name="stock_op_head">
       <page:param name="current">flightStock</page:param>
    </page:applyDecorator>
 <div class="orderdetails_tit">&nbsp;&nbsp;上传切位支付凭证</div>

 <div class="jbox-content"  id="content" style="width:560px; background:#fff; padding:10px 0px; margin:20px auto;">
   
<!--上传支付凭证开始-->

    <div class="upload">
        <strong>上传切位支付凭证：</strong><input type="button" value="+添加" onclick="javascript:addFileUpload();" > 
    </div>
    <div id="hiddendiv" style="display:none"><dl><dt><b>凭证：</b><span><input name="fileLogo" type="text" value="" /></span><input type="file" onchange="javascript:inFileName(this)" hidefocus="" size="1" name="payVoucher"><input type="button"  value="选择文件"></dt><dd><input type="button" class="btn btn-primary gray" value="删除" class="btn btn-primary" onclick="javascript:removeFileUpload(this);")></dd></dl></div>
<%--     <form:form id="inputForm" action="${ctx}/stock/manager/airticket/upload" enctype="multipart/form-data" method="post" onsubmit="return check();"> --%>
	<form:form id="inputForm" action="${ctx}/stock/manager/airticket/upload" enctype="multipart/form-data" method="post">
    <tags:message content="${message}"/>
    <div id="filediv" class="upload_cen">
    <c:if test="${empty activityReserveFileList }"><p>未添加支付凭证，点击添加按钮上传。</p></c:if>
    <c:forEach items="${activityReserveFileList}" var="file">
    <b>凭证：</b><span>${file.fileName }</span><br/>
    </c:forEach> 
    </div>
    <div class="release_next_add">
            <input  type="hidden" name="srcActivityId" value="${srcActivityId }"/>
            <input  type="hidden" name="agentId" value="${agentId }"/>
            <input  type="hidden" name="reserveOrderId" value="${reserveOrderId }"/>
             <input type="button" value="取消上传" onClick="jumpto()" class="btn btn-primary">
             <input type="submit" value="提 交" class="btn btn-primary"/>
    </div>
    </form:form>
<!--上传支付凭结束-->
</div>
</div>

<script type="text/javascript">

</script>

</body>
</html>

