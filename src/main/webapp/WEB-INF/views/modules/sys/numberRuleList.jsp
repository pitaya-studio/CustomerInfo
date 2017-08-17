<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>基础信息维护-编号规则设置</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet"	href="${ctxStatic}/css/jquery.validate.min.css" />

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"	src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

</head>
<body>
	<page:applyDecorator name="sys_menu_head">
		<page:param name="showType">numberRule</page:param>
	</page:applyDecorator>

	<!--右侧内容部分开始-->
	<div class="activitylist_bodyer_right_team_co">
		<p class="main-right-topbutt">
			<a href="javascript:void(0)" class="ydbz_x primary" onclick="javascript:window.location.href='${ctx}/sys/numberRule/show'">新增编号规则</a>
		</p>
	</div>
	<div class="tableDiv flight" style="width:100%;padding-left:0px;padding-top: 0px;">
		<table id="contentTable" class="table activitylist_bodyer_table mainTable">
			<thead>
				 <tr>
					<th width="15%">编号类型</th>
					<th width="70%">规则</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="numberRule" >
					<tr>
                       	<td>${numberRule.numberType }</td>
                        <td>${numberRule.numberValue }</td>
                        <td class="tc"><a href="javascript:window.location.href='${ctx}/sys/numberRule/show?id=${numberRule.id }'" target="_self">自定义</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!--右侧内容部分结束-->
	</div>

</body>
</html>
