<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>交通方式信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">交通方式信息</div>
	<div class="maintain_add">
		<p>
			<label>名称：</label> <span> ${sysdefinedict.label}</span>
		</p>
		<p class="maintain_kong"></p>
		<p>
			<label>排序：</label> <span> ${sysdefinedict.sort}</span>
		</p>
		<p class="maintain_kong"></p>
		<p class="maintain_pfull">
			<label>描述：</label> <span> ${sysdefinedict.description}</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
