<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>游客列表</title>
<meta name="decorator" content="wholesaler" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script> 
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">游客列表</page:param>
</page:applyDecorator>
<!--右侧内容部分开始-->
<div class="mod_nav">订单 > ${orderTypeStr} > 游客列表</div>
<div class="ydbz_tit orderdetails_titpr">游客列表</div>
<table id="contentTable" class="activitylist_bodyer_table">
	<thead>
		<tr>
			<th width="25%">游客</th>
			<th width="25%">护照号</th>
			<th width="25%">未上传数量</th>
			<th width="25%">操作</th>
		</tr>
	</thead>
	
	<tbody>
	<tags:message content="${message}"/>
	<c:forEach items="${travelers}" var="tra" varStatus="s">
		<tr> 
			<td class="tc">${tra.traveler.name}</td>
			<td class="tc">${tra.traveler.passportCode}</td>
			<td class="tc">${9-tra.fileSize}</td>
			<td class="tc">
				<a href="${ctx}/orderCommon/manage/uploadTravelerFilesHref/${tra.traveler.id}/${orderType}">上传资料</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.close();">关闭</a></div>
</body>
</html>