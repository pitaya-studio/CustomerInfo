<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>酒店管理-游客类型绑定</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ctxStatic}/js/default.validator.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#inputForm")
								.validate(
										{
											rules : {
												label : {
													required : true,
													remote : {
														type : "POST",
														url : "${ctx}/hotelTravelerTypeRelation/check?type="
																+ $('#type')
																		.val()
																+ "&uuid="
																+ $('#uuid')
																		.val()
													}
												},
												sort : {
													required : true,
													digits : true
												},
												dictUuid : {
													required : true
												}
											},
											submitHandler : function(form) {
												// 					var url = "";
												// 					if($("#uuid").val()=='') {
												// 						url="${ctx}/hotelTravelerTypeRelation/save";
												// 					} else {
												// 						url="${ctx}/hotelTravelerTypeRelation/update";
												// 					}	
												var huuid = $("#hotelUuid")
														.val();
												var url = "${ctx}/hotelTravelerTypeRelation/update?hotelUuid="
														+ huuid;
												// 					var url = "${ctx}/hotelTravelerTypeRelation/update";

												var travelerTypeUuids = "";
												var travelerTypeNames = "";
												$(
														"input[name='travelerType']:checkbox")
														.each(
																function() {
																	if ($(this)
																			.attr(
																					"checked")) {
																		travelerTypeUuids += $(
																				this)
																				.val()
																				+ ",";
																		travelerTypeNames += $(
																				this)
																				.attr(
																						"data-value")
																				+ ",";
																	}
																});
												$("#travelerTypeUuids").val(
														travelerTypeUuids);
												$("#travelerTypeNames").val(
														travelerTypeNames);

												$
														.post(
																url,
																$("#inputForm")
																		.serialize(),
																function(data) {
																	if (data == "1") {
																		$(
																				"#searchForm",
																				window.opener.document)
																				.submit();
																		$.jBox
																				.tip("保存成功!");
																		setTimeout(
																				function() {
																					window
																							.close();
																				},
																				500);
																	} else if (data == "2") {
																		$(
																				"#searchForm",
																				window.opener.document)
																				.submit();
																		$.jBox
																				.tip("修改成功!");
																		setTimeout(
																				function() {
																					window
																							.close();
																				},
																				500);
																	} else {
																		$.jBox
																				.tip(
																						'操作异常！',
																						'warning');
																		$(
																				"#btnSubmit")
																				.attr(
																						"disabled",
																						false);
																	}
																});
											},
											messages : {
												label : {
													remote : "名称已存在"
												},
											}
										});

						if ($("#uuid").val() == '') {
							$("#sort").val("50");
						}
					});
</script>
<script>
	function checkall(obj) {
		if ($(obj).attr("checked")) {
			$("input[name='travelerType']").attr("checked", 'true');
		} else {
			$("input[name='travelerType']").removeAttr("checked");
		}
	}
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">游客类型信息</div>
	<form:form method="post"
		modelAttribute="hotelTravelerTypeRelationInput" action=""
		class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
<!-- 		<tags:message content=" ${message}" /> -->
		<div class="maintain_add">
			<input type="hidden" id="hotelUuid" value="${hotelUuid }" /> <input
				type="hidden" id="travelerTypeUuids" name="travelerTypeUuids" /> <input
				type="hidden" id="travelerTypeNames" name="travelerTypeNames" />
			酒店名称：
			<trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid"
				srcColumnName="name_cn" value="${hotelUuid }" />
			<br />
			<!-- 			<label><em class="xing"></em>游客类型：</label><br/> -->
			<!-- 			<table class="activitylist_bodyer_table" style="width: 30%"> -->
			<!-- 				<tr> -->
			<!-- 					<td width="35%"><input type="checkbox" onclick="checkall(this)"/>全选</td> -->
			<!-- 					<td width="65%">游客类型</td> -->
			<!-- 				</tr> -->
			<br />
			<br /> 游客类型:
			<c:forEach items="${travelerTypes }" var="t" varStatus="v">
				<input type="checkbox" name="travelerType" value="${t.uuid }"
					<c:if test="${relationString.contains(t.uuid) }">checked="checked"</c:if>
					data-value="${t.name}">${t.name}</input>
					&nbsp;&nbsp;
				<!-- 					<tr> -->
				<!-- 						<td><input type="checkbox" name="travelerType"  value="${t.uuid }" <c:if test="${relationString.contains(t.uuid) }">checked="checked"</c:if> data-value="${t.name}"></input></td> -->
				<!-- 						<td>${t.name}</td> -->
				<!-- 					</tr> -->
			</c:forEach>
<!-- 			<input type="checkbox" onclick="checkall(this)" />全选 -->
			<!-- 			</table> -->


			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button"
					value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray"
					onclick="window.close();" /> <input type="submit"
					value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
