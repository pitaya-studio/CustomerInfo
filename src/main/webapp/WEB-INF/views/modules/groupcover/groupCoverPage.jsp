<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<% String path = request.getContextPath();%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	    <title>报名-申请补位</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<!-- 页面左边和上边的装饰 -->
		<meta name="decorator" content="wholesaler" />
		<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
		<script type="text/javascript" src="${ctxStatic}/modules/groupcover/groupCoverPage.js"></script>
	</head>
  
	<body>
		<!-- 产品与团期信息 -->
		<%@ include file="/WEB-INF/views/modules/groupcover/groupInfo.jsp"%>
		<div class="ydbz_tit"><span class="fl">补位申请</span></div>
		<form:form action="${ctx}/groupCover/applyGroupCover" method="post" id="form1">
			<table style="margin-left:40px">
				<tbody>
					<tr>
						<td>补位人数：</td>
						<td>
							<input type="text" class="buwei" style="width:100px;" name="coverPosition" id="coverPosition" maxlength="9"
							onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
							onafterpaste="this.value=this.value.replace(/[^\d]/g,'')">
						</td>
						<td width="100px" class="tr">备注：</td>
						<td><input type="text" class="beizhu" style="width:700px;" name="remarks" id="remarks" maxlength="500"/></td>
					</tr>
				</tbody>
			</table>
			<div class="dbaniu" style="width:150px;">
				<a class="ydbz_s gray"onClick="window.close();">关闭</a>
				<input type="button" value="提交" class="btn btn-primary" onclick="submitForm();"> 
			</div>
			<input type="hidden" id="groupId" name="groupId" value="${productGroup.id}"/>
			<input type="hidden" id="freePosition" name="freePosition" value="${productGroup.freePosition}"/>
			<input type="hidden" id="groupcoverNum" name="groupcoverNum" value="${productGroup.groupcoverNum}"/>
		</form:form>
	</body>
</html>
