<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>CruiseshipCabin信息</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
	<div class="ydbz_tit pl20">CruiseshipCabin信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${cruiseshipCabin.uuid}
			</span>
		</p>
		<p>
			<label>游轮UUID：</label> 
			<span>
				${cruiseshipCabin.cruiseshipInfoUuid}
			</span>
		</p>
		<p>
			<label>批发商id：</label> 
			<span>
				${cruiseshipCabin.wholesalerId}
			</span>
		</p>
		<p>
			<label>舱型名称：</label> 
			<span>
				${cruiseshipCabin.name}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${cruiseshipCabin.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipCabin.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${cruiseshipCabin.updateBy}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipCabin.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除标识：</label> 
			<span>
				${cruiseshipCabin.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
