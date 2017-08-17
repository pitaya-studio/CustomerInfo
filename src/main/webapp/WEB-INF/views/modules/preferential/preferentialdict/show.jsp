<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>PreferentialDict信息</title>
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
	<div class="ydbz_tit pl20">PreferentialDict信息</div>
	<div class="maintain_add">
		
		<p>
			<label>字典名称：</label> 
			<span>
				${preferentialDict.name}
			</span>
		</p>
		<p class="maintain_kong"></p>
		<p>
			<label>数据类型：</label> 
			<span>
				${preferentialDict.dataType==0?"数值":"日期"}
			</span>
		</p>
		<p class="maintain_kong"></p>
		<p>
			<label>因果类型：</label> 
			<span>
				${preferentialDict.type==0?"因":"果"}
			</span>
		</p>
		<p class="maintain_kong"></p>
		<p>
			<label>关系运算符：</label> 
			<span>
				${preferentialDict.relationalOperatorString}
			</span>
		</p>
		<p class="maintain_kong"></p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
