
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
	<li id="mycreatemsg">
		<a href="${ctx}/message/goToAddMessage">添加公告</a>
	</li>
	<li id="mysavemsglist">
		<a href="${ctx}/message/findMySaveMsgList">草稿箱</a>
	</li>
	<li id="mycreatemsglist">
		<a href="${ctx}/message/findMyCreateMsgList">我发布的公告-列表页</a>
	</li>
</content>