<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>酒店餐型列表</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
	function show(uuid){
		window.open( "${ctx}/hotelMeal/show/" + uuid) ;
	}
	function edit(uuid){
		window.open("${ctx}/hotelMeal/edit/" + uuid) ;
	}
	function del(uuid){
		$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在删除数据...", 'loading');
				$.post("${ctx}/hotelMeal/delete", {"uuid":uuid},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip("删除成功!");
							$("#searchForm").submit();
						}else{
							top.$.jBox.tip(data.message,'warning');
						}
					}
				);
			}
		});
	}
	
	function orderSubmitCallBack(){
		if($(".sort").length>0){
			$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在修改排序...", 'loading');
					var uuidAndSorts = [];
					$(".sort").each(function(){
						var uuid = $(this).find("input[name=uuid]").val();
						var sort = $(this).find(".maintain_sort").val();
						uuidAndSorts.push(uuid+","+sort);
					});
					$.post("${ctx}/hotelMeal/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(";")},
						function(data){
							if(data.result=="1"){
								top.$.jBox.tip("修改排序成功!");
								$("#searchForm").submit();
							}else{
								top.$.jBox.tip(data.message,'warning');
							}
						}
					);
					
				} else if (v == 'cancel') {
		             $("#searchForm").submit();
				}
			});
		}
	}
</script>	
</head>
<body>
	<div>
		<!--右侧内容部分开始-->
		<div class="filter_btn">
			<form id="searchForm" action="${ctx}/hotelMeal/list/${hotelUuid}" method="post">
				
			</form>
			<a class="btn btn-primary" target="_blank" href="${ctx}/hotelMeal/form/${hotelUuid}">添加餐型</a>
		</div>
		<table class="t-type t-type100 tablemt50">
			<thead>
				<tr>
					<th width="10%">排序</th>
					<th width="">餐型名称</th>
					<th width="">类型</th>
					<th width="">适用人数</th>
					<th width="15%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="hotelMeal" items="${hotelMeals}" varStatus="v">
					<tr>
						<td class="sort">
							<input type="hidden" value="${hotelMeal.uuid}" name="uuid" />
							<input type="text" value="${hotelMeal.sort}" class="maintain_sort" maxlength="4" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');"  disabled="disabled" />
						</td>
						<td class="tc">${hotelMeal.mealName}</td>
						<td class="tc"><trekiz:defineDict name="mealType" type="hotel_meal_type" defaultValue="${hotelMeal.mealType }" readonly="true"/></td>
						<td class="tc">${hotelMeal.suitableNum}</td>
						<td class="tda tc">
							<a href="javascript:void(0);" onclick="edit('${hotelMeal.uuid }')">修改</a> 
							<a href="javascript:void(0);" onclick="del('${hotelMeal.uuid }')">删除</a> 
							<a href="javascript:void(0);" onclick="show('${hotelMeal.uuid }')">详情</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="page">
			<div class="pagination">
				<dl>
					<a onclick="updateOrder(this)" class="ydbz_x">修改排序</a>
				</dl>
				<div style="clear:both;"></div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
<script type="text/javascript">


</script>
