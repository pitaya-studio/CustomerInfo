<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IslandTraveler信息</title>
	<meta name="decorator" content="wholesaler"/>
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
							url: "${ctx}/islandTraveler/check?type="+$('#type').val()+"&uuid="+$('#uuid').val()
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
						url="${ctx}/islandTraveler/save";
					} else {
						url="${ctx}/islandTraveler/update";
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
	<div class="ydbz_tit pl20">IslandTraveler信息</div>
	<form:form method="post" modelAttribute="islandTravelerInput" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<tags:message content=" ${message}"/>
		<div class="maintain_add">
			<p>
				<label><em class="xing"></em> 游客UUID：</label>
				<form:input path="travelerUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em> 订单UUID：</label>
				<form:input path="orderUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>订单id 关联productorder,visa_order,airticket_order等表的id，暂保留兼容以前的设计，以后以订单uuid为关联：</label>
				<form:input path="orderId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>游客姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>游客名称拼音：</label>
				<form:input path="nameSpell" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>房屋类型 1-单人间 2-双人间：</label>
				<form:input path="hotelDemand" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>单房差：</label>
				<form:input path="singleDiff" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>单房差几晚：</label>
				<form:input path="singleDiffNight" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>papersType：</label>
				<form:input path="papersType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>身份证号：</label>
				<form:input path="idCard" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>国籍：</label>
				<form:input path="nationality" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>性别 1-男 2-女：</label>
				<form:input path="sex" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>出生日期：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${islandTraveler.birthDay}" pattern="yyyy-MM-dd" />" name="birthDay" class="inputTxt dateinput" id="birthDay" />
			</p>
			<p>
				<label><em class="xing"></em>issuePlace：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${islandTraveler.issuePlace}" pattern="yyyy-MM-dd" />" name="issuePlace" class="inputTxt dateinput" id="issuePlace" />
			</p>
			<p>
				<label><em class="xing"></em>validityDate：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${islandTraveler.validityDate}" pattern="yyyy-MM-dd" />" name="validityDate" class="inputTxt dateinput" id="validityDate" />
			</p>
			<p>
				<label><em class="xing"></em>手机号：</label>
				<form:input path="telephone" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>备注：</label>
				<form:input path="remark" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>单价（发布产品时的定价）：</label>
				<form:input path="srcPrice" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>人员类型（1-成人 2-儿童 3-特殊人群）：</label>
				<form:input path="personType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>passportCode：</label>
				<form:input path="passportCode" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>护照有效期：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${islandTraveler.passportValidity}" pattern="yyyy-MM-dd" />" name="passportValidity" class="inputTxt dateinput" id="passportValidity" />
			</p>
			<p>
				<label><em class="xing"></em>护照种类 1：因公护照 2：因私护照：</label>
				<form:input path="passportType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>是否需要联运 0：不需要，1：需要：</label>
				<form:input path="intermodalType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>产品联运信息表主键 关联intermodal_strategy表：</label>
				<form:input path="intermodalId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>护照状态 1:借出;2:归还客户;3:未签收;4:已签收：</label>
				<form:input path="passportStatus" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>单价币种：</label>
				<form:input path="srcPriceCurrency" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>单房差币种：</label>
				<form:input path="singleDiffCurrency" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>记录游客属于那种订单（预报名：0 单团类、散拼、签证、机票同产品类型）：</label>
				<form:input path="orderType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>删除标记   0:正常   1：删除    2:退团审核中  3：已退团   4：转团审核中   5：已转团：</label>
				<form:input path="delFlag" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>游客结算价流水号：</label>
				<form:input path="payPriceSerialNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>是否有机票标志 0 标示无机票 或已退票 1标示有机票：</label>
				<form:input path="isAirticketFlag" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>游客原始应收价 一次生成 永不改变：</label>
				<form:input path="originalPayPriceSerialNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>游客已付款流水号：</label>
				<form:input path="payedMoneySerialNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>costPriceSerialNum：</label>
				<form:input path="costPriceSerialNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>rebatesMoneySerialNum：</label>
				<form:input path="rebatesMoneySerialNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>paymentType：</label>
				<form:input path="paymentType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>id：</label>
				<form:input path="mainOrderId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>id：</label>
				<form:input path="mainOrderTravelerid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em> UUID：</label>
				<form:input path="accountedMoney" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>批量借护照批次号：</label>
				<form:input path="borrowPassportBatchNo" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>游客借款UUID：</label>
				<form:input path="jkSerialNum" htmlEscape="false" maxlength="11" class="required" />
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
