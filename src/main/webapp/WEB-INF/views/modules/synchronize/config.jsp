<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>邮件发送设置</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/modules/synchronize/mailConfig.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			$('#config').attr('class', 'active');
		});
	</script>
</head>
<body>
	<!-- <ul class="nav nav-tabs">
		<li><a href="${ctx}/synchronizeExceptionLog/manage/notSolved">待解决异常团期同步</a></li>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeExceptionLog/manage/solved">已解决异常团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeSuccessLog/manage/success">成功团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeUnPriceLog/manage/unPrice">成人价格不存在异常</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeUnLog/manage/un">正反向产品ID未存在关联日志</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:config"><li class="active"><a href="${ctx}/mailConfig/manage/config">邮件发送设置</a></li></shiro:hasPermission>
	</ul> -->
	<%@ include file="header.jsp" %>
	<form id="mailConfigForm" action="${ctx}/mailConfig/manage/save">
		<table width="550">
			<tr>
				<td>
					<input type="hidden" id="sendEmailStateHidden" value="${sendEmailState}">
					<input type="hidden" id="addressHidden" value="${mailAddress}">
				</td>
				<td>
					<dl class="xtyjfs-ys-top"><dt>邮件发送：</dt><dd><input type="radio" checked="checked" name="sendEmailState" value="1"><span>打开</span></dd><dd><input name="sendEmailState" type="radio" value="0"><span>关闭</span></dd></dl>
				</td>
			</tr>
			<tr>
				<td>
					邮件接收人：
					<div class="xtyjfs-ys">
						<input name="mailAddress" type="text"></input><input type="button" value="复制" class="xtyjfs-ys-add" onclick="copyNode(this);"><input type="button" value="删除" class="xtyjfs-ys-add" onclick="deleteNode(this);">
					</div>
				</td>
				<td align="center">
					<div class="xtyjfs-ys"><input type="button" onclick="saveMileConfig();" class="xtyjfs-ys-add" value="保存"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>