<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-游客类型管理</title>
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
			window.open( "${ctx}/travelerType/show/" + uuid) ;
		}
		function edit(uuid){
			window.open("${ctx}/travelerType/edit/" + uuid) ;
		}
		function del(uuid){
			$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在删除数据...", 'loading');
					$.post("${ctx}/travelerType/delete", {"uuid":uuid},
						function(data){
							if(data.result=="1"){
								top.$.jBox.tip('删除成功');
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
			//排序校验，排序sort不可重复并且不可为空
			var map = {};
			var msg = "";
			var flag = true;
			$(".sort").each(function(){
				var key = $(this).find(".maintain_sort").val();
				if((key == "") || (key == null) || (key == undefined)) {
					msg = "排序数字不能为空，请重新输入！";
					flag = false;
					$("input[class='maintain_sort']").removeAttr("disabled");
					$(this).find(".maintain_sort").focus();
					return false;
				} else if(map[key] == 1) {
					msg = "排序数字不能重复输入"+ key +"，请重新输入！";
					flag = false;
					$("input[class='maintain_sort']").removeAttr("disabled");
					$(this).find(".maintain_sort").focus();
					return false;
				} else {
					map[key] = 1;
				}
			});
			
			if(!flag) {
				$.jBox.tip(msg);
				$(".ydbz_x").text("提交");
				return false;
			}
			
			if($(".sort").length>0){
				$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
					if (v == 'ok') {
						$.jBox.tip("正在修改排序...", 'loading');
						var uuidAndSorts = [];
						$(".sort").each(function(){
							var uuid = $(this).find("input[name=uuid]").val();
							var sort = $(this).find(".maintain_sort").val();
							uuidAndSorts.push(uuid+"-"+sort);
						});
						$.post("${ctx}/travelerType/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(",")},
							function(data){
								if(data.result=="1"){
									top.$.jBox.tip('修改排序成功!');
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
	<page:applyDecorator name="sys_menu_head" >
	    <page:param name="showType">traveler_type</page:param>
	</page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<form:form method="post" modelAttribute="travelerType" action="${ctx}/travelerType/list" class="form-horizontal" id="searchForm" novalidate="">
			
		</form:form>
		<div class="filter_btn">
			<%--<a class="btn btn-primary" target="_blank" href="${ctx}/travelerType/form">添加游客类型</a>--%>
			<input class="btn btn-primary ydbz_x" onclick="window.open('${ctx}/travelerType/form')" value="添加游客类型" type="button">

		</div>
		<table class="activitylist_bodyer_table mainTable">
			<thead>
				<tr>
					<th width="10%">排序</th>
					<th width="10%">游客类型名称</th>
					<th width="15%">年龄范围</th>
					<th width="10%">对应系统的游客类型</th>
					<th width="20%">产品使用范围</th>
					<th width="10%">描述</th>
					<th width="5%">人员类型</th>
					<th width="10%">状态</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="travelerType" items="${travelerTypes}" varStatus="v">
					<tr>
						<td class="sort">
							<input type="hidden" name="uuid" value="${travelerType.uuid }" />
							<input type="text" value="${travelerType.sort}" maxlength="4" class="maintain_sort" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled" />
						</td>
						<td class="tc">${travelerType.name }</td>
						<td class="tc">${travelerType.rangeFrom }-${travelerType.rangeTo }</td>
						<td class="tc"><trekiz:autoId2Name4Table tableName="sys_traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerType.sysTravelerType}" /></td>
						<td class="tc">${travelerType.applyProductName}</td>
						<td class="tc">${travelerType.description }</td>
						<td class="tc">
							<c:choose> 
							  <c:when test="${travelerType.personType == 0}">   
								   成人  
							  </c:when> 
							  <c:when test="${travelerType.personType == 1}">   
							   	 婴儿  
							  </c:when> 
							  <c:when test="${travelerType.personType == 2}">   
							   	儿童  
							  </c:when> 
							  
							</c:choose> 
						</td>
						<td class="tc">
							<c:choose>
								<c:when test="${travelerType.status=='1' }">启用</c:when>
								<c:when test="${travelerType.status=='0' }">停用</c:when>
							</c:choose>
						</td>
						<td class="tda tc">
							<a href="javascript:void(0);" onclick="edit('${travelerType.uuid}')">修改</a>
							<c:if test="${travelerType.sysTravelerType != '3b23624f1db94deaa32861d642f56f79' }"><a href="javascript:void(0);" onclick="del('${travelerType.uuid}')">删除</a></c:if>
							<a href="javascript:void(0);" onclick="show('${travelerType.uuid}')">详情</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="page">
			<div class="pagination">
				<dd>
					<%--<a onclick="updateOrder(this)" class="ydbz_x">修改排序</a>--%>
					<input class="btn btn-primary ydbz_x" onclick="updateOrder(this)" value="修改排序" type="button" />
				</dd>
				<div style="clear:both;"></div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
<script type="text/javascript">


</script>
