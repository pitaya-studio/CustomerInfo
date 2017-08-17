<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/> 
<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript">
	function deleteFile(orderId,docId,delUrl){
		var ctx = $("#hostPath").val();
		$.ajax({
			type : "POST",
			url : ctx +"/" +delUrl,
			data : {
				orderId : orderId,
				docId : docId
			},
			success : function (msg) {
				if (msg.success == 'ok') {
					$("#doc"+docId).css('display','none'); 
					top.$.jBox.tip("删除成功", "warning");
				} else {
					top.$.jBox.tip("删除失败", "warning");
				}

			}
		});
	}
	
	function downloadFile(docId,orderId,downloadUrl){
		var ctx = $("#hostPath").val();
		window.open(encodeURI(encodeURI(ctx+"/"+downloadUrl+"/"+orderId+"/"+docId)));
	}
</script>

<div style="margin-top:26px;margin-left:58px">
	<input type="hidden" id="hostPath" value="${ctx }">
	<c:forEach items="${docList}" var="doc">
		<p id="doc${doc.id }">${doc.docName }
		<c:if test="${fns:getUser().id == doc.createBy.id && delUrl ne null}">
			<a style="cursor:pointer; margin-left: 6px"  onclick="deleteFile('${orderId }','${doc.id }','${delUrl }')">删除</a>
		</c:if>
		<a style="cursor:pointer; margin-left: 6px" onclick="downloadFile(${doc.id },${orderId },'${downloadUrl }')">下载</a></p>
	</c:forEach>
</div>
