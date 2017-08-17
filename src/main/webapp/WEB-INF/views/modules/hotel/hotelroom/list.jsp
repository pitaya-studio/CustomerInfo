<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>酒店房型维护</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		$(function(){
			//操作浮框
			operateHandler();
		});
		function show(uuid){
			window.open( "${ctx}/hotelRoom/show/" + uuid) ;
		}
		function edit(uuid){
			window.open("${ctx}/hotelRoom/edit/" + uuid) ;
		}
		
		function optFloor(uuid){
			window.open( "${ctx}/hotelFloor/list/${hotelUuid}/" + uuid) ;
		}
		function del(uuid){
			$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在删除数据...", 'loading');
					$.post("${ctx}/hotelRoom/delete", {"uuid":uuid},
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
						$.post("${ctx}/hotelRoom/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(";")},
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
			<form id="searchForm" action="${ctx}/hotelRoom/list/${hotelUuid}" method="post">
				<input id="type" name="type" type="hidden" value="hotel_room" />
			</form>
			<a class="btn btn-primary" href="${ctx}/hotelRoom/form/${hotelUuid}" target="_blank">添加房型</a>
		</div>
		<table class="t-type t-type100 tablemt50">
			<thead>
				<tr>
					<th width="10%">排序</th>
					<th width="">房型</th>
					<th width="">床型</th>
					<th width="">容住率</th>
					<!-- <th width="">加床价</th> -->
					<th width="6%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="hotelRoom" items="${hotelRooms}" varStatus="v">
					<tr>
						<td class="sort">
							<input type="hidden" value="${hotelRoom.uuid}" name="uuid" />
							<input type="text" value="${hotelRoom.sort}" class="maintain_sort" maxlength="4" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');"  disabled="disabled" />
						</td>
						<td class="tc">${hotelRoom.roomName }</td>
						<td class="tc"><trekiz:defineDict name="bed" readonly="true" type="hotel_bed_type" defaultValue="${hotelRoom.bed }" /></td>
						<td class="tc">${hotelRoom.occupancyRate }</td>
						<%-- <td class="tr">${hotelRoom.extraBedCost }</td> --%>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
								</dt>
								<dd class="">
									<p>
										<span></span>
											<a href="javascript:void(0);" onclick="optFloor('${hotelRoom.uuid }')">管理楼层</a> 
											<a href="javascript:void(0);" onclick="edit('${hotelRoom.uuid }')">修改</a> 
											<a href="javascript:void(0);" onclick="del('${hotelRoom.uuid }')">删除</a>
											<a href="javascript:void(0);" onclick="show('${hotelRoom.uuid }')">详情</a>
									</p>
								</dd>
							</dl>
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
