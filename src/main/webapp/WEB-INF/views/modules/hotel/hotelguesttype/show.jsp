<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>住客类型信息详情</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
	<div class="ydbz_tit pl20">住客类型</div>
	<div class="maintain_add">
		<p>
			<label>住客类型名称：</label> 
			<span>
				${hotelGuestType.name}
			</span>
		</p>
		<p>
			<label>描述：</label> 
			<span>
				${hotelGuestType.description}
			</span>
		</p>
		<p>
			<label>状态：</label> 
			<span>
				<c:choose>
					<c:when test="${hotelGuestType.status == 0}">不启用</c:when>
					<c:when test="${hotelGuestType.status == 1}">启用</c:when>
				</c:choose>
			</span>
		</p>
		<!-- <label>适用范围：</label> 
		<p>
			<label>适用范围：</label> 
			<span>
				<c:choose>
					<c:when test="${hotelGuestType.useRange == 1}">酒店房型</c:when>
					<c:when test="${hotelGuestType.useRange == 2}">酒店餐型</c:when>
				</c:choose>
			</span>
		</p>
		-->
		<p>
			<label>排序：</label> 
			<span>
				${hotelGuestType.sort}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
