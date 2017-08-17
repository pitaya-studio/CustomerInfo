<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HotelOrder信息</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				rules:{
					label:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/hotelOrder/check?type="+$('#type').val()+"&uuid="+$('#uuid').val()
								}
						},
					sort:{
						required:true,
						digits:true
					},
					dictUuid:{
						required:true
					} 
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/hotelOrder/save";
					} else {
						url="${ctx}/hotelOrder/update";
					}
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else{
							$.jBox.tip('操作异常！','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
				},
				messages:{
					label:{remote:"名称已存在"},
				}
			});
			
			
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
		});
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">HotelOrder信息</div>
	<form:form method="post" modelAttribute="hotelOrderInput" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<tags:message content=" ${message}"/>
		<div class="maintain_add">
			<p>
				<label><em class="xing"></em>订单uuid：</label>
				<form:input path="uuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>酒店产品uuid：</label>
				<form:input path="activityHotelUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>酒店产品团期uuid：</label>
				<form:input path="activityHotelGroupUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>订单单号：</label>
				<form:input path="orderNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>订单状态：0 全部订单；1 待确认报名；2 已确认报名；3 已取消；：</label>
				<form:input path="orderStatus" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>预订单位-即渠道：</label>
				<form:input path="orderCompany" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>预订单位名称：</label>
				<form:input path="orderCompanyName" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>跟进销售员id：</label>
				<form:input path="orderSalerId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>预订人名称：</label>
				<form:input path="orderPersonName" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>预订人联系电话：</label>
				<form:input path="orderPersonPhoneNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>预订日期：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${hotelOrder.orderTime}" pattern="yyyy-MM-dd" />" name="orderTime" class="inputTxt dateinput" id="orderTime" />
			</p>
			<p>
				<label><em class="xing"></em>预定人数：</label>
				<form:input path="orderPersonNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>订金金额UUID：</label>
				<form:input path="frontMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消：</label>
				<form:input path="payStatus" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>已付金额UUID：</label>
				<form:input path="payedMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>支付方式1-支票 2-POS机付款 3-现金支付 4-汇款 5-快速支付：</label>
				<form:input path="payType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>创建者：</label>
				<form:input path="createBy" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>创建日期：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${hotelOrder.createDate}" pattern="yyyy-MM-dd" />" name="createDate" class="inputTxt dateinput" id="createDate" />
			</p>
			<p>
				<label><em class="xing"></em>更新者：</label>
				<form:input path="updateBy" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>更新日期：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${hotelOrder.updateDate}" pattern="yyyy-MM-dd" />" name="updateDate" class="inputTxt dateinput" id="updateDate" />
			</p>
			<p>
				<label><em class="xing"></em>删除标记：</label>
				<form:input path="delFlag" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>当前退换记录Id：</label>
				<form:input path="changeGroupId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>退换类型：</label>
				<form:input path="groupChangeType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>订单成本金额：</label>
				<form:input path="costMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>达账状态：</label>
				<form:input path="asAcountType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>达账金额UUID：</label>
				<form:input path="accountedMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>下订单时产品的预收定金：</label>
				<form:input path="payDeposit" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位：</label>
				<form:input path="placeHolderType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>下订单时的单房差：</label>
				<form:input path="singleDiff" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>取消原因：</label>
				<form:input path="cancelDescription" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>0 未付款 1 已付首款 2 已付尾款（全款）3 首款已达账 4 尾款（全款）已达账 5 开发票申请 6 已开发票：</label>
				<form:input path="isPayment" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>付款方式：</label>
				<form:input path="payMode" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>保留天数：</label>
				<form:input path="remainDays" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>激活时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${hotelOrder.activationDate}" pattern="yyyy-MM-dd" />" name="activationDate" class="inputTxt dateinput" id="activationDate" />
			</p>
			<p>
				<label><em class="xing"></em>订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)：</label>
				<form:input path="lockStatus" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>特殊需求：</label>
				<form:input path="specialDemand" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>订单总价UUID：</label>
				<form:input path="totalMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>文件ids：</label>
				<form:input path="fileIds" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>原始应收价 一次生成永不改变：</label>
				<form:input path="originalTotalMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>是否是补单产品，0：否，1：是：</label>
				<form:input path="isAfterSupplement" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>原始订金金额（乘人数后金额）：</label>
				<form:input path="originalFrontMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4：</label>
				<form:input path="paymentType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>预报名间数：</label>
				<form:input path="forecaseReportNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>酒店扣减控房间数：</label>
				<form:input path="subControlNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>酒店扣减非控房间数：</label>
				<form:input path="subUnControlNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>备注：</label>
				<form:input path="remark" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>报名状态（1、预报名；2、报名）：</label>
				<form:input path="bookingStatus" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="submit" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
