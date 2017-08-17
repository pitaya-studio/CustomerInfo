<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-酒店住客类型管理</title>
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
			window.open("${ctx}/hotelGuestType/show/" + uuid) ;
		}
		function edit(uuid){
			window.open("${ctx}/hotelGuestType/edit/" + uuid) ;
		}
		function del(uuid){
			$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在删除数据...", 'loading');
					$.post("${ctx}/hotelGuestType/delete", {"uuid":uuid},
						function(data){
							if(data.result=="1"){
								$.jBox.tip('删除成功!');
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
						$.post("${ctx}/hotelGuestType/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(",")},
							function(data){
								if(data.result=="1"){
									$.jBox.tip('修改排序成功!');
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
	    	<page:param name="showType">hotel_guest_type</page:param>
		</page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<form:form method="post" modelAttribute="hotelGuestType" action="${ctx}/hotelGuestType/list" class="form-horizontal" id="searchForm" novalidate="">
			
		</form:form>
		<div class="filter_btn">
			<%--<a class="btn btn-primary" target="_blank" href="${ctx}/hotelGuestType/form">添加酒店住客类型</a>--%>
			<input class="btn btn-primary ydbz_x" onclick="window.open('${ctx}/hotelGuestType/form')" value="添加酒店住客类型" type="button">

		</div>
		<table class="activitylist_bodyer_table mainTable">
			<thead>
				<tr>
					<th width="10%">排序</th>
					<th width="">住客类型名称</th>
					<th width="">对应系统的住客类型</th>
					<th width="">描述</th>
					<th width="">状态</th>
					<th width="">创建时间</th>
					<th width="">修改时间</th>
					<th width="15%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="hotelGuestType" items="${hotelGuestTypes}"
					varStatus="index">
					<tr>
						<td class="tc sort">
							<input type="hidden" name="uuid" value="${hotelGuestType.uuid }" /> 
							<input type="text" value="${hotelGuestType.sort}" class="maintain_sort" maxlength="4"  onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled" />
						</td>
						<td class="tc">${hotelGuestType.name }</td>
						<td class="tc"><trekiz:autoId2Name4Class classzName="SysGuestType" sourceProName="uuid" srcProName="name" value="${hotelGuestType.sysGuestType }"/></td>
						<td class="tc">${hotelGuestType.description }</td>
						<td class="tc">
							<c:choose>
								<c:when test="${hotelGuestType.status == 0}">不启用</c:when>
								<c:when test="${hotelGuestType.status == 1}">启用</c:when>
							</c:choose>
						</td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${hotelGuestType.createDate }" />
						</td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${hotelGuestType.updateDate }" />
						</td>
						<td class="tda tc">
							<a href="javascript:void(0);" onclick="edit('${hotelGuestType.uuid}')">修改</a> 
							<a href="javascript:void(0);" onclick="del('${hotelGuestType.uuid}')">删除</a> 
							<a href="javascript:void(0);" onclick="show('${hotelGuestType.uuid}')">详情</a>
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
