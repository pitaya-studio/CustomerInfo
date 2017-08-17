<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>类型字典维护</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	
	function addType(typeId) {
		
			var id = typeId;
			var iframe = "iframe:${ctx}/sys/currency/form";
			if(id!=null){
				iframe += "?id="+typeId;		
			}
			$.jBox(iframe, {
				    title: typeId==null?"新增":"修改"+"币种信息",
				    width: 500,
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
	
	<ul class="nav nav-tabs">
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
        <li id="currency" class="active"><a href="${ctx}/sys/currency/list">币种管理</a></li>
	</ul>
	<tags:message content="${message}"/>
	<div class="ydbzbox fs">
	<div class="ydbz_tit">币种管理</div>
	<div class="tableDiv flight">
	<table id="contentTable" class="t-type">
		<thead class="destination_title">
			<tr>
				<th>币种名称</th>
				<th>币种标识</th>
				<th>汇率</th>
				<th>备注</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${currencyList}" var="currency">
			<tr>
				<td>${currency.currencyName}</td>
				<td>${currency.currencyMark}</td>
				<td>${currency.currencyExchangerate}</td>
				<td>${currency.remark}</td>
				<td class="tc">
    				<a href="javascript:void(0)" onClick="addType(${currency.id})">修改</a>&nbsp&nbsp&nbsp&nbsp
					<a href="${ctx}/sys/currency/delete?id=${currency.id}" onClick="return confirmx('您确认删除&quot${currency.currencyName}&quot吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
			<c:if test="${empty currencyList}">
				<div class="wtjqw">没有该类型的相关数据</div>
			</c:if>
	<div class="t-type-add">
		<a class="ydbz_s" href="#" onClick="addType()">添&nbsp;&nbsp;&nbsp;加</a>
	</div>
	</div>
	</div>
</body>
</html>