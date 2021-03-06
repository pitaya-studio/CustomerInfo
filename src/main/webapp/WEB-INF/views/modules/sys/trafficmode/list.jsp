<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-交通方式</title>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
	$(function(){
		
	});

	function orderSubmitCallBack(){
		if($(".sort").length>0){
			$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在修改排序...", 'loading');
					var idAndSorts = [];
					$(".sort").each(function(){
						var id = $(this).find("input[name=id]").val();
						var sort = $(this).find(".maintain_sort").val();
						idAndSorts.push(id+"-"+sort);
					});
					$.post("${ctx}/sys/CompanyDict/updateOrder", {"idAndSortsStr":idAndSorts.join(",")},
						function(data){
							if(data.result=="1"){
								$.jBox.prompt("修改排序成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
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
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">${param.type}</page:param>
</page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<div class="activitylist_bodyer_right_team_co_bgcolor">
			<form id="searchForm" action="${ctx}/sys/CompanyDict/CompanyDictList" method="post">
				<input id="type" name="type" type="hidden" value="<c:out value="${param.type}" />" />

			</form>
		</div>
		<div class="filter_btn">
			<%--<a class="btn btn-primary" href="${ctx}/sys/CompanyDict/companyDictForm?type=${param.type}" target="_blank">添加交通方式</a>--%>
            <input class="btn btn-primary ydbz_x" type="button"
                   onclick="window.open('${ctx}/sys/CompanyDict/companyDictForm?type=${param.type}')" value="添加交通方式">

		</div>
		<table class="t-type t-type100 tablemt50 mainTable">
			<thead>
				<tr>
					<th width="10%">排序</th>
					<th width="">交通方式</th>
					<th width="">描述</th>
					<th width="15%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entity" items="${companyDictList}" varStatus="v">
					<tr>
						<td class="tc sort">
							<input name="id" type="hidden" value="<c:out value="${entity.id}" />" /> 
							<input type="text" value="${entity.sort}" class="maintain_sort" maxlength="4" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
						</td>
						<td>${entity.label}</td>
						<td>${entity.description}</td>
						<td class="tc tda">
							<a href="${ctx}/sys/CompanyDict/companyDictForm?type=${param.type}&id=${entity.id}" target="_blank">修改</a>
							<a href="${ctx}/sys/CompanyDict/delCompanyDict?id=${entity.id}" onclick="return confirmx('您确认删除&quot${entity.label}&quot吗？', this.href)">删除</a>
							<a href="${ctx}/sys/CompanyDict/show/${entity.id}?type=${param.type}" target="_blank">详情</a>
						</td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
		<div class="page"></div>
		<div class="pagination">
			<dd>
				<%--<a onclick="updateOrder(this)" class="ydbz_x">修改排序</a>--%>
                <input class="btn btn-primary ydbz_x" type="button"
                       onclick="updateOrder(this)" value="修改排序">

			</dd>
		</div>
		<br />
		<!--右侧内容部分结束-->
	</div>
</body>
</html>