<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>预定-签证填写下单信息</title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css"
	type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><link href="/trekiz_wholesaler/static/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="/trekiz_wholesaler/static/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/css/jh-style.css?ver=1" type="text/css"
	rel="stylesheet" />
<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css"
	rel="stylesheet" />

	
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<link
	href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"
	type="text/css" rel="stylesheet" />
	

<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/vendor.service_mode1.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"
	type="text/javascript"></script>
<!-- 
<script
	src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js"
	type="text/javascript"></script>
 -->
<script src="${ctxStatic}/json/json2.js" type="text/javascript"></script>
<!-- 
<script src="${ctxStatic}/modules/activity/dynamic.validator.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<!-- 
<script src="${ctxStatic}/modules/order/manageorder.js"
	type="text/javascript"></script>
-->
<script src="${ctxStatic}/common/jquery.disabled.js"
	type="text/javascript"></script>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus,select[readonly]:focus,textarea[readonly]:focus
	{
	cursor: auto;
	background: transparent;
	border: 0px;
	box-shadow: inset 0 0px 0px rgba(0, 0, 0, 0.075)
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		// 表单验证
		$("#firstStepForm").validate({
			rules : {
				travelNum : "required",
			},
			messages : {
				travelNum : "游客数量不能为空",
			}
		});
		$("div[class='noContract']").hide();
		$("div[class='contract']").hide();
		// 将渠道商切到页面默认值
		changeAgentInfo();
		$("#agentinfoId").change(function() {
			changeAgentInfo();
		});

		$(".ydbz_x").bind("click", function() {
			submitNext();
		});

	});
	// 切换渠道商方法
	function changeAgentInfo() {
		var agentInfoID = $("#agentinfoId").val();
		// 判断，如果agentInfoID=-1，表示为非签约渠道商，需要展示可修改的渠道商信息
		if (agentInfoID == -1) {
			$("div[class='noContract']").show();
			$("div[class='contract']").hide();
		} else {
			$("div[class='contract']").show();
			$("div[class='noContract']").hide();
			var jsonRes = new Array();
			if (agentInfoID) {
				jsonRes.push({
					id : agentInfoID,
					value : agentInfoID
				});
				$.ajax({
					type : "POST",
					url : "${ctx}/visa/preorder/getAgentInfo",
					dataType : "json",
					async : false,
					data : {
						jsonresult : JSON.stringify(jsonRes)
					},
					success : function(result) {
						$(result).each(function() {
							$("#agentContact").empty();
							$("#agentContact").append(this.agentContact);
							$("#agentTel").empty();
							$("#agentTel").append(this.agentTel);
							$("#agentFixedLine").empty();
							$("#agentFixedLine").append(this.agentFixedLine);
							$("#agentAddress").empty();
							$("#agentAddress").append(this.agentAddress);
							$("#agentEmail").empty();
							$("#agentEmail").append(this.agentEmail);
							$("#agentQQ").empty();
							$("#agentQQ").append(this.agentQQ);
						});
					}
				});
			}
		}

	};

	// 提交到下一步
	function submitNext() {
		// 获取特殊需求
		var rem = $("#need").text();
		if (rem) {
			$("input[name='remark']").val(rem);
		}
		$("#firstStepForm").submit();
	};
</script>

</head>

<body>

	<div class="main-right">
		<form id="firstStepForm" action="${ctx}/visa/preorder/getOrderFirst"
			method="post">
			<!-- step 判断下单进行到第几步 -->
			<input type="hidden" id="step" name="step" value="1" />
			<!-- 签证订单类型，在本界面下达的订单均为单签订单 -->
			<input type="hidden" name="visaOrderType" id="visaOrderType"
				value="0" />
			<!-- 签证产品ID -->
			<input type="hidden" name="visaProductId" id="visaProductId"
				value="${productId }" />
			<ul class="nav nav-tabs">
				<li class="active"><a href="#">预定</a></li>
			</ul>

			<div class="bgMainRight">
				<div class="ydbzbox fs">
					<div class="ydbz yd-step1 " id="stepbar">&nbsp;</div>
					<div class="ydbz_tit">订单基本信息</div>

					<!--<p class="ydbz_mc">JHGJLXS5535--预报名--001</p>-->
					<ul class="ydbz_info">
						<li><span>有效期：</span> <em class="fArial"></em>${product.valid_period }
								${product.valid_period_unit }</li>
						<li><span>签证国家：</span>${country.countryName_cn }</li>
						<li><span>签证类别：</span> ${visaType }</li>
						<li><span>是否面试：</span>${needSpotAudition }</li>
						<li><span>预计工作日：</span>${forecastWorkingTime } 天</li>
						<li><span>入境次数：</span> ${enterNum } 次</li>
						<li><span>最多停留：</span> <em class="fArial"></em> ${stayTime }
								${stayTimeUnit } </li>
						<li><span>应收价格：</span>${currency.currencyMark} ${visaPay } ${currency.currencyName}</li>
						<li><span>办签人数：</span><input type="text" id="travelNum"
							name="travelNum" class="inputTxt"></li>
					</ul>

					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>填写预订人信息
					</div>
					<div flag="messageDiv">
						<p class="ydbz_qdmc">
							预订渠道： <select id="agentinfoId" name="agentinfoId">
								<option value="">不限</option>
								<option value="-1">非签约渠道</option>
								<c:forEach items="${agentinfoList }" var="agentinfo">
									<option value="${agentinfo.id }">${agentinfo.agentName}</option>
								</c:forEach>
							</select>
						</p>
						<div class="contract">
							<ul class="ydbz_qd">
								<li><label><span class="xing">*</span>渠道联系人：</label><span
										id="agentContact"></span></li>
								<li><label><span class="xing">*</span>渠道联系人电话：</label><span
										id="agentTel"></span></li>
								<li><label>固定电话：</label> <span id="agentFixedLine"></span><!-- <span class="ydbz_x yd1AddPeople" onclick="yd1AddPeople(this)">展开</span> -->
								</li>
							</ul>
							<ul class="ydbz_qd">
								<li><label>通讯地址：</label><span id="agentAddress"></span></li>
								<li><label>传真号码：</label><span id="agentFax"></span></li>
								<li><label>网络邮箱：</label><span id="agentEmail"></span></li>
							</ul>

							<ul class="ydbz_qd">
								<li><label>QQ号码：</label><span id="agentQQ"></span></li>
							</ul>
						</div>
						<div class="noContract">
							<!--<ul class="ydbz_qd" id="orderpersonMes"> -->
							<ul class="ydbz_qd">
								<li><label><span class="xing">*</span>渠道联系人：</label><span><input
											type="text" name="agentContact" /></span></li>
								<li><label><span class="xing">*</span>渠道联系人电话：</label><span><input
											type="text" name="agentTel" /></span></li>
								<li><label>固定电话：</label><span><input type="text"
											name="agentFixedLine" /></span> <!-- <span class="ydbz_x yd1AddPeople" onclick="yd1AddPeople(this)">展开</span> -->
								</li>
							</ul>
							<ul class="ydbz_qd">
								<li><label>通讯地址：</label><span><input type="text"
											name="agentAddress" /></span></li>
								<li><label>传真号码：</label><span><input type="text"
											name="agentFixedLine" /></span></li>
								<li><label>网络邮箱：</label><span><input type="text"
											name="agentEmail" /></span></li>
							</ul>

							<ul class="ydbz_qd">
								<li><label>QQ号码：</label><span><input type="text"
											name="agentQQ" /></span></li>
							</ul>
						</div>
					</div>
					<div id="manageOrder_m">
						<div id="contact">
							<div class="ydbz_tit">
								<span class="ydExpand closeOrExpand"></span> 特殊需求
							</div>
							<div class="ydbz2_lxr" flag="messageDiv">

								<p>
									<label style="vertical-align: top">特殊需求：</label>
									<!-- 特殊需求（备注） -->
									<textarea id="remark" name="remark"></textarea>
								</p>

							</div>
						</div>
					</div>
					<div class="ydbz_sxb" id="oneToSecondOutStepDiv">
						<div class="ydBtn" id="oneToSecondStepDiv">
							<span class="ydbz_x">下一步</span>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
