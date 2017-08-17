<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出发城市</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(function(){
				//产品名称获得焦点显示隐藏
				$("#wholeSalerKey").focusin(function(){
				var obj_this = $(this);
				obj_this.next("span").hide();
				}).focusout(function(){
				var obj_this = $(this);
				if(obj_this.val()!="") {
				obj_this.next("span").hide();
				}else
				obj_this.next("span").show();
				});
				if($("#wholeSalerKey").val()!="") {
				$("#wholeSalerKey").next("span").hide();
				} 
			if("${message}")
				$.jBox.tip("保存成功","success",{timeout:1000});
		});
			//修改操作
			function update() {
				getIdArr();
				$("#selForm").attr("action","${ctx}/sys/dict/descUpdate");
				$("#selForm").submit();
				}
			//查询
			function searchButton(){
					$("#term").val($("#wholeSalerKey").val());
					getIdArr();
					$("#searchForm").attr("action","${ctx}/sys/dict/descSearch");
					$("#searchForm").submit();
			}
			//数据状态(新增，删除，默认显示)
			function getVal(obj) {
				if($(obj).attr("class")){
					if($(obj).attr("class")=="selected"){
						$(obj).attr("class",'cancel');
					}else if($(obj).attr("class")=="cancel"){
						$(obj).attr("class",'selected');
					}else{
						$(obj).attr("class",'');
					}
				}else{
					$(obj).attr("class",'checked');
				}
			}
			//分页	
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#term1").val($("#wholeSalerKey").val());
			$("#selForm").attr("action","${ctx}/sys/dict/pagingList");
			$("#selForm").submit();
	    }
		//数据状态Id	
		function getIdArr() {
			var checkedIds = "";
			var cancelIds = "";
			var selectedIds = "";
			$("[class='checked']").each(function(index,element){
				checkedIds += $(element).val()+",";
			});
			$("[class='cancel']").each(function(index,element){
				cancelIds += $(element).val()+",";
			});
			$("[class='selected']").each(function(index,element){
				selectedIds += $(element).val()+",";
			});
			
			$("#checkedIds").val(checkedIds);
			$("#cancelIds").val(cancelIds);
			$("#upLoadTypeIds").val(selectedIds);
		}
	</script>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">fromarea</page:param>
</page:applyDecorator>
<!-- 
    <content tag="three_level_menu">
		<li id="travel_type" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=travel_type">旅游类型</a></li>
		<li id="product_level" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=product_level">产品系列</a></li>
		<li id="product_type" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=product_type">产品类型</a></li>
		<li id="traffic_mode" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=traffic_mode">交通方式</a></li>
		<li id="flight_info" class=""><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=flight_info">机场信息</a></li>
		<li id="from_area" class="active"><a href="${ctx}/sys/dict/pagingList?type=fromarea">出发城市</a></li>
		<li id="sys_area" class=""><a href="${ctx}/sys/dict/normalList?type=area">目的地区域</a></li>
		<li id="flight" class=""><a href="${ctx}/sys/dict/pagingList?type=flight">航空公司</a></li>
        <li id="out_area" class="" ><a href="${ctx}/sys/dict/pagingList?type=outarea">离境口岸</a></li>
	</content>
-->
	 <%--<div class="ydbzbox fs">--%>
		<%--<div class="ydbz_tit">出发城市维护</div>--%>
		<%--<div class="aviation"></div>--%>
			<%--<div class="tableDiv flight">--%>
<%--modified for UG_V2 统一代码结构 去掉无用div by tlw at 20170301  start--%>
			<div id="sea">
				<form:form id="searchForm" action="" method="post">
					<div class="activitylist_bodyer_right_team_co">
						<input id="type" type="hidden" name="type" value="fromarea"/>
						<input id="term" type="hidden" name="term" value="${term}"/>
						<div class="activitylist_bodyer_right_team_co2">
							<input value="${seek}" name="wholeSalerKey" class="searchInput radius4"
								   id="wholeSalerKey" type="text" placeholder="请输入城市名称"
								   onkeydown="if (event.keyCode == 13){searchButton();}" />
						</div>
						<div class="form_submit">
							<input id="search" class="inquiry_left_submit btn btn-primary ydbz_x" type="submit" onClick="searchButton()" value="搜 索"/>
						</div>
						<%--<div class="inquiry_right">查询结果：<b> <c:if test="${empty page.list }">${fn:length(dataList)}</c:if><c:if test="${!empty page.list }">${page.count}</c:if></b>条</div>--%>
						<input id="upLoadTypeIds" type="hidden" name="upLoadTypeIds" value="${upLoadTypeIds}"/>
					</div>
				</form:form>

<%--modified for UG_V2 改变排序位置 by tlw at 20170301  start--%>
<div class="activitylist_bodyer_right_team_co_paixu">
	<div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
		</div>
		<div class="activitylist_paixu_right">
			查询结果&nbsp;&nbsp;<strong> <c:if test="${empty page.list }">${fn:length(dataList)}</c:if><c:if test="${!empty page.list }">${page.count}</c:if></strong>&nbsp;条
		</div>
		<div class="kong"></div>
	</div>
</div>
<%--modified for UG_V2 改变排序位置 by tlw at 20170301 end--%>

				<form:form id="selForm" modelAttribute="dataList" action="" method="post">
					<input id="activityStatus" type="hidden" name="activityStatus" value="${travelActivity.activityStatus }"/>
					<input id="activityIds" type="hidden" name="activityIds" value="${activityIds }"/>
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="term1" type="hidden" name="term1" value="${term}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
					<input id="fromareaIds" type="hidden" name="typeIds" value="${fromareaIds}"/>
					<input id="checkedIds" type="hidden" name="checkedIds" value="${checkedIds}"/>
					<input id="cancelIds" type="hidden" name="cancelIds" value="${cancelIds}"/>
					<input id="type" type="hidden" name="type" value="fromarea"/>
						<table class="t-type mainTable">
						<colgroup>
							<col width="15%">
							<col width="60%">
							<col width="15%">
					   </colgroup>
					   <thead>
							<tr>
								<th width="">编号</th>
								<th>出发城市</th>
								<th>是否显示</th>
							</tr>
					   </thead>
					   <tbody>
					   <c:forEach items="${page.list}" var="fromarea">
							<tr>
							<td>${fromarea.value}</td>
							<td>${fromarea.label}</td>
							<td><input type="checkbox" id="selected" class="<c:if test="${fromarea.selected eq '1'}">selected</c:if>" value="${fromarea.id}" <c:if test="${fromarea.selected eq '1'}">checked</c:if> onClick="getVal(this)"/></td>
							</tr>
					   </c:forEach>
<%--					   <c:if test="${empty page.list }">--%>
<%--						   <c:forEach items="${dataList}" var="fromarea">--%>
<%--						   <c:set var="arealistsize" value="${fn:length(dataList)}"></c:set>--%>
<%--								<tr>--%>
<%--								<td>${fromarea.value}</td>--%>
<%--								<td>${fromarea.label}</td>--%>
<%--								<td><input type="checkbox" id="selected" class="<c:if test="${fromarea.selected eq '1'}">selected</c:if>" value="${fromarea.id}" <c:if test="${fromarea.selected eq '1'}">checked</c:if> onClick="getVal(this)"/></td>--%>
<%--								</tr>--%>
<%--						   </c:forEach>--%>
<%--					   </c:if>--%>
					   </tbody>
				</table>
			    <c:if test="${page.count eq 0 }">
			       	<td><div class="wtjqw">无该出发城市的相关信息</div></td>
			       </c:if>
			       <c:if test="${page.count ne 0 }">
			 <div class="page">
				<div class="pagination" style="overflow:hidden;">
					<div class="endPage">${page}</div>
				</div>
			</div>

				</c:if>
                    <div class="t-type-add">
                        <input class="inquiry_left_submit btn btn-primary ydbz_x" type="button" onclick="update()" value="保存">
                    </div>
			</form:form>
			<%--</div>--%>
		<%--</div>--%>
		</div>
	</body>
</html>