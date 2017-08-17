<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>类型字典维护</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var active = ${param.type};
		$(active).attr("class","active");
		var title = $(${param.type}).text();
		var splitIndex = title.indexOf("丨");
		if(splitIndex != -1){
			title = title.substring(0,splitIndex);
		}
		$(".ydbz_tit").text(title);
	});
	
	function addType(typeId) {
			var id = typeId;
			var type = "${param.type}";
			var iframe = "iframe:${ctx}/sys/CompanyDict/companyDictForm?type="+type;
			if(id!=null){
				iframe += "&id="+typeId;		
			}
			$.jBox(iframe, {
				    title: "请输入"+$(${param.type}).text()+"信息",
				    width: 340,
   					height: 365,
				    buttons: {}
			});
			
	}	
		
		
	</script>
    <style type="text/css">
    
	input[type="file"], input[type="image"], input[type="submit"], input[type="reset"], input[type="button"], input[type="radio"], input[type="checkbox"]{ width:72px; height:28px; font-size:14px;}
    </style>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">${param.type}</page:param>
</page:applyDecorator>
<br/>
<!-- 
    <content tag="three_level_menu">
		<li id="travel_type" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=travel_type">旅游类型</a></li>
		<li id="product_level" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=product_level">产品系列</a></li>
		<li id="product_type" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=product_type">产品类型</a></li>
		<li id="traffic_mode" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=traffic_mode">交通方式</a></li>
		<li id="flight_info" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=flight_info">机场信息</a></li>
		<li id="from_area" class=""><a href="${ctx}/sys/dict/pagingList?type=fromarea">出发城市</a></li>
		<li id="sys_area" class=""><a href="${ctx}/sys/dict/normalList?type=area">目的地区域</a></li>
		<li id="flight" class=""><a href="${ctx}/sys/dict/pagingList?type=flight">航空公司</a></li>
        <li id="out_area" class=""><a href="${ctx}/sys/dict/pagingList?type=outarea">离境口岸</a></li>
        <li id="productLine" class=""><a href="${ctx}/sys/productLine/list">产品线路</a></li>
        <li id="currency" class=""><a href="${ctx}/sys/currency/list">币种管理</a></li>
	</content>
 -->	
	<tags:message content="${message}"/>
	<div class="ydbzbox fs">
	<div class="ydbz_tit"></div>
	<div class="tableDiv flight">
	<table id="contentTable" class="mainTable t-type">
		<thead class="destination_title">
			<tr><th width="15%">编号</th>
				<th width="25%">
					<c:choose>
	            		<c:when test="${param.type eq 'flight_info'}">机场三字码</c:when>
	            		<c:otherwise>名称 </c:otherwise>
	            	</c:choose>
	            </th>
	            <th width="10%">顺序</th>
	            <th width="25%">
					<c:choose>
	            		<c:when test="${param.type eq 'flight_info'}">机场名称</c:when>
	            		<c:otherwise>描述 </c:otherwise>
	            	</c:choose>
				</th>
				<c:if test="${param.type eq 'traffic_mode'}">
					<th width="15%">已关联航空二字码</th>
				</c:if>
			<th width="10%">操作</th></tr>
		</thead>
		<tbody>
		<c:forEach items="${companyDictList}" var="companyDictList" varStatus="status">
			<tr>
				
				<td>${status.count}</td>
				<td>${companyDictList.label}</td>
				<td>${companyDictList.sort}</td>
				<td>${companyDictList.description}</td>
				<c:if test="${param.type eq 'traffic_mode'}">
					<td>
						<c:if test="${companyDictList.defaultFlag eq '1'}">√</c:if>
						<c:if test="${companyDictList.defaultFlag eq '0'}">ⅹ</c:if>
					</td>
				</c:if>
				<td class="tc">
    				<a href="javascript:void(0)" onClick="addType(${companyDictList.id})">修改</a>&nbsp&nbsp&nbsp&nbsp
					<a href="${ctx}/sys/CompanyDict/delCompanyDict?id=${companyDictList.id}" onClick="return confirmx('您确认删除&quot${companyDictList.label}&quot吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
			<c:if test="${empty companyDictList}">
				<div class="wtjqw">没有该类型的相关数据</div>
			</c:if>
	<div class="t-type-add">
		<%--<a class="ydbz_s" href="#" onClick="addType()">添&nbsp;&nbsp;&nbsp;加</a>--%>
		<input id="btn_search" class="btn btn-primary ydbz_x" type="button" onClick="addType()" value="添加">
	</div>
	</div>
	</div>
</body>
</html>