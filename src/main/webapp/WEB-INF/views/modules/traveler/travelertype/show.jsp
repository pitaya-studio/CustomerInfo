<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>游客类型信息</title>
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
	<div class="ydbz_tit pl20">游客类型信息</div>
	<div class="maintain_add">
		<p>
			<label>排序：</label> 
			<span>
				${travelerType.sort}
			</span>
		</p>
		<p>
			<label>游客类型名称：</label> 
			<span>
				${travelerType.name}
			</span>
		</p>
		<p>
			<label>游客类型简称：</label> 
			<span>
				${travelerType.shortName}
			</span>
		</p>
		<p>
			<label>年龄范围：</label> 
			<span>
				${travelerType.rangeFrom }-${travelerType.rangeTo }
			</span>
		</p>
		<p>
			<label>系统游客类型：</label> 
			<span>
				<trekiz:autoId2Name4Table tableName="sys_traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerType.sysTravelerType }" />
			</span>
		</p>
		<p>
			<label>启用状态：</label> 
			<span>
				<c:choose>
					<c:when test="${travelerType.status=='1' }">启用</c:when>
					<c:when test="${travelerType.status=='0' }">停用</c:when>
				</c:choose>
			</span>
		</p>
		<p>
			<label>人员类型：</label> 
			<span>
				<c:choose> 
				  <c:when test="${travelerType.personType == 0}">   
					   成人  
				  </c:when> 
				  <c:when test="${travelerType.personType == 1}">   
				   	 婴儿  
				  </c:when> 
				  <c:otherwise>   
				            儿童  
				  </c:otherwise> 
				</c:choose> 
			</span>
		</p>
		<p>
			<label>适用产品：</label> 
			<span>
			  <c:forTokens var="str" items="${travelerType.applyProduct}" delims="," varStatus="status">  
			    <c:if test="${status.current=='1'}">单团</c:if>
				<c:if test="${status.current=='2'}">散拼</c:if>
				<c:if test="${status.current=='3'}">游学</c:if>
				<c:if test="${status.current=='4'}">大客户</c:if>
				<c:if test="${status.current=='5'}">自由行</c:if>
				<c:if test="${status.current=='6'}">签证</c:if>
				<c:if test="${status.current=='7'}">机票</c:if>
				<c:if test="${status.current=='10'}">游轮</c:if>
				<c:if test="${status.current=='11'}">酒店</c:if>
				<c:if test="${status.current=='12'}">海岛游</c:if>
			   </c:forTokens>  
			</span>
		</p>
		<p>
			<label>描述：</label> 
			<span>
				${travelerType.description}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
