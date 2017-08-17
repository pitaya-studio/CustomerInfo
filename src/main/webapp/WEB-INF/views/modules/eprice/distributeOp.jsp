<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>计调主管分配计调</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script type="text/javascript">

$(function(){
	var opIdArr = eval($("input[name=opIds]").val());
	if (opIdArr && opIdArr.length > 0) {
		for (var i = 0; i < opIdArr.length; i++) {
			$("input[name='opId'][value=" + opIdArr[i] + "]").attr("checked", true).attr("disabled", true);
		}
	}
	
	var airticketOpIdArr = eval($("input[name=airticketOpIds]").val());
	if (airticketOpIdArr && airticketOpIdArr.length > 0) {
		for (var i = 0; i < airticketOpIdArr.length; i++) {
			$("input[name='airticketOpId'][value=" + airticketOpIdArr[i] + "]").attr("checked", true).attr("disabled", true);
		}
	}
});

function submitForm() {
	//if ($("input[name='opId']:checked").length > 0) {
		$("#submitForm").submit();
	//} else {
	//	top.$.jBox.tip('请选择要分配的计调！', 'error', { focusId: 'name' });
	//}
}

</script>

</head>

<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">分配计调</page:param>
</page:applyDecorator>
<div class="bgMainRight">
	<form id="submitForm" method="post" action="${ctx}/eprice/manager/project/distribute">
		<input type="hidden" name="baseInfoId" value="${baseInfoId}">
		<input type="hidden" name="rid" value="${rid}">
		<input type="hidden" name="opIds" value="${opIds}">
		<input type="hidden" name="airticketOpIds" value="${airticketOpIds}">
		<c:if test="${types.contains('0')}">
			<div>
				<div>
					地接计调
				</div>
				<br/>
				<div>
					<c:forEach items="${userList}" var="user" varStatus="s">
						<input type="checkbox" name="opId" value="${user.id}">${user.name }
					</c:forEach>
				</div>
			</div>
			<br/>
		</c:if>
		<c:if test="${types.contains('1')}">
			<div>
			<div>
				机票计调
			</div>
			<br/>
			<div>
				<c:forEach items="${userList}" var="user" varStatus="s">
					<input type="checkbox" name="airticketOpId" value="${user.id}">${user.name }
				</c:forEach>
			</div>
		</div>
		</c:if>
	</form>
	
	<div class="dbaniu cl-both" >
		<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">取消</a>
		<a class="ydbz_s" href="javascript:submitForm()">提交</a>
	</div>
</div>

</body>
</html>
