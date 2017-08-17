<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预定-签证</title>

<link href="${ctxStatic}/forTTS/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/forTTS/css/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/forTTS/css/jh-style.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/forTTS/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/forTTS/css/jquery.validate.min.css" />
	
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/forTTS/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/forTTS/js/common.js"></script>
<script type="text/javascript">
	$(function() {
		//展开筛选
		launch();
		//文本框提示信息显示隐藏
		inputTips();

		//公告
		notice();
		$(".list_lh").myScroll({
			speed : 80, //数值越大，速度越慢
			rowHeight : 20
		//li的高度
		});
		//预定第二部签证类别
		ydbz2CardSel();
		// 页面加载时获取排列方式
		var by = $("#orderby").val();
		orderByCss(by);
	});
	// 选择排列方式，重新排列产品列表
	function orderByParam(orderby){
		$("#orderby").val(orderby);
		$("#searchForm").submit();
	}
	// 判断排列方式，并选择样式
	function orderByCss(orderby){
		$("#"+orderby).attr("class","activitylist_paixu_moren");
		$("#"+orderby).append('<i class="icon icon-arrow-down"></i>');
	}
	// 预定签证订单
	function visaProductToOrder(productId){
		
	}
	
</script>
</head>
<body>
	<div class="main-right">
		<ul class="nav nav-tabs"></ul>
		<div class="bgMainRight">
			<!--右侧内容部分开始-->
			<dl class="notice">
				<dt class="tdred tr">公告：</dt>
				<dd class="list_lh">
					<ul>
						<li><a href="" target="_blank">写最近约签时间等信息</a></li>
						<li><a href="" target="_blank">你好</a></li>
						<li><a href="" target="_blank">很好</a></li>
						<li><a href="" target="_blank">太好</a></li>
					</ul>
				</dd>
			</dl>
			<form method="post" action="${ctx}/visa/preorder/list" id="searchForm">
				<div class="activitylist_bodyer_right_team_co">
					<!-- 
							<div class="activitylist_bodyer_right_team_co2 pr wpr20">
								<label>产品名称：</label>
								<input value="" name="wholeSalerKey"
									id="wholeSalerKey" class="txtPro inputTxt" style="width: 260px" flag="istips" /> 
								<span style="display: block;" class="ipt-tips">销售姓名或线路国家</span>
							</div>

							

							<div class="zksx zksx-on">收起筛选</div>
							 -->

					
						<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">签证国家：</div>
							<select id="sysCountryId" name="sysCountryId">
								<option value="">不限</option>
								<option value="2481">美国</option>
								<option value="401">加拿大</option>
							</select>
						</div>
						
						<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">领区：</div>
							<select id="collarZoning" name="collarZoning">
								<option value="">不限</option>
								<option value="1">北京</option>
								<option value="2">上海</option>
								<option value="3">沈阳</option>
							</select>
						</div>
						<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">签证类别：</div>
							<select id="visaType" name="visaType">
								<option value="">不限</option>
								<option value="2">个签</option>
								<option value="8">探亲</option>
								<option value="9">照会</option>
								<option value="10">邀请</option>
								<option value="11">照会+邀请</option>
								<option value="1">团签</option>
								<option value="100">其他</option>
							</select>
						</div>
						<div class="form_submit">
							<input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x"/>
						</div>
						<div class="kongr"></div>
					<div class="kong"></div>
				</div>
				<!-- 默认排序方式 -->
				<input type="hidden" id="orderby" name="orderby" value="${orderby }"/>
			</form>
			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul id="orderByUl">
							<li style="width: 50px; border: none; background: none; height: 28px; line-height: 28px;">排序</li>
							<li id="forecast_working_time" class="activitylist_paixu_left_biankuang"><a onclick="orderByParam('forecast_working_time')">预计工作日</a></li>
							<li id="enter_num" class="activitylist_paixu_left_biankuang"><a onclick="orderByParam('enter_num')">入境次数</a></li>
							<li id="stay_time" class="activitylist_paixu_left_biankuang"><a onclick="orderByParam('stay_time')">最多停留</a></li>
							<li id="visa_pay" class="activitylist_paixu_left_biankuang"><a onclick="orderByParam('visa_pay')">价格</a></li>
						</ul>
					</div>
					<div class="activitylist_paixu_right">
						查询结果&nbsp;&nbsp;<strong>261</strong>&nbsp;条
					</div>
					<div class="kong"></div>
				</div>
			</div>
			<form id="groupform" name="groupform" action="" method="post">
				<table id="contentTable" class="table activitylist_bodyer_table">
					<thead style="background: #403738">
						<tr>
							<th width="5%">序号</th>
							<th width="10%">签证国家</th>
							<th width="10%">签证类别</th>
							<th width="10%">领区</th>
							<th width="10%">是否面试</th>
							<th width="10%">签证有效期</th>
							<th width="10%">预计工作日</th>
							<th width="10%">入境次数</th>
							<th width="10%">最多停留</th>
							<th width="10%">应收价格/人</th>
							<th width="5%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list }" var="visaProducts">
							<tr>
								<td>${visaProducts.id }</td>
								<td>${visaProducts.sysCountryId }</td>
								<td>${visaProducts.visaType }</td>
								<td>${visaProducts.collarZoning }</td>
								<td>
									<c:choose>
										<c:when test="${visaProducts.needSpotAudition=='1' }">需要面试</c:when>
										<c:otherwise>不需面试</c:otherwise>
									</c:choose>
								</td>
								<td>${visaProducts.valid_period } ${visaProducts.valid_period_unit }</td>
								<td>${visaProducts.forecastWorkingTime }</td>
								<td>${visaProducts.enterNum }</td>
								<td>${visaProducts.stayTime } ${visaProducts.stayTimeUnit }</td>
								<td>${visaProducts.visaPay }起</td>
								<td>
									<dl class="handle">
									<!-- 
										<dt>
											<img title="操作" src="images/handle_cz.png"/>
										</dt>
										<dd>
											<p>
												<a href="javascript:void(0)" onclick="visaProductInfo(${visaProducts.id})">详情</a><br/>
												<a href="javascript:void(0)" onclick="javascript:window.open('${ctx}/visa/preorder/getOrder/${visaProducts.id }')">办签</a>
											</p>
										</dd> -->
										<p>
											<a href="javascript:void(0)" onclick="javascript:window.open()">详情</a><br/>
											<a href="javascript:void(0)" onclick="javascript:window.open('${ctx}/visa/preorder/getOrder/${visaProducts.id }/-2')">办签</a>
										</p>
									</dl>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<input type="hidden" id="tempUserName" value="admin" />
			</form>
			<div class="pagination clearFix">${page}</div>
			<!--右侧内容部分结束-->
			
		</div>
	</div>
</body>
</html>
