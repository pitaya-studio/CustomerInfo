<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><c:if test="${budgetType eq 0 }">预算成本-列表</c:if><c:if test="${budgetType eq 1 }">实际成本-列表</c:if></title>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

	<!--[if lte IE 6]>
	<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
	<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
	<![endif]-->
	<link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"/>
<!-- 	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/> -->
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>

	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//产品名称文本框提示信息
	inputTips();

	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	//地接社、渠道商切换
	switchSupplierAndAgent();
	//取消一个checkbox 就要联动取消 全选的选中状态
	$("input[name='ids']").click(function(){
		if($(this).attr("checked")){

		}else{
			$("input[name='allChk']").removeAttr("checked");
		}
	});

	$('#contentTable').on('change','input[type="checkbox"]',function(){
		if( $('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length ){
			$('[name="allChk" ]').attr('checked',true);
		}else{
			$('[name="allChk" ]').removeAttr('checked');
		}
	});

	//renderSelects(selectQuery());
	selectQuery();

	//如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null && $(searchFormInput[i]).val() != "全部") {
			inputRequest = true;
		}
	}
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" &&
			$(searchFormselect[i]).children("option:selected").val() != "0" &&
			$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}

	var _$orderBy = $("#orderBy").val();
	if(_$orderBy==""){
		_$orderBy="createDate DESC";
		$("#orderBy").val("createDate DESC");
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
		if ($(this).hasClass("li"+orderBy[0])){
			orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
			$(this).attr("class","activitylist_paixu_moren");
		}
	});
});

function selectQuery() {
	$("#createBy").comboboxInquiry();
	$("#supplyId").comboboxInquiry();
	$("#agentId").comboboxInquiry();
}

function sortby(sortBy,obj){
	var temporderBy = $("#orderBy").val();
	if(temporderBy.match(sortBy)){
		sortBy = temporderBy;
		if(sortBy.match(/ASC/g)){
			sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
		}else{
			sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
		}
	}else{
		sortBy = sortBy+" DESC";
	}

	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
}

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/costReview/activity/costReviewList/${budgetType}");
	$("#searchForm").submit();
}

function reviewer(tag) {
	$("#reviewer").val(tag);
	$("#searchForm").submit();
}

//展开收起
function expand(child, obj) {
	if($(child).is(":hidden")){
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
		$(obj).parents("tr").addClass("tr-hover");

	}else{
		$(child).hide();
		$(obj).parents("td").removeClass("td-extend");
		$(obj).parents("tr").removeClass("tr-hover");

	}
}
function checkall(obj){
	if($(obj).attr("checked")){
		$('#contentTable input[type="checkbox"]').attr("checked",'true');
		$("input[name='allChk']").attr("checked",'true');
	}else{
		$('#contentTable input[type="checkbox"]').removeAttr("checked");
		$("input[name='allChk']").removeAttr("checked");
	}
}

function checkreverse(obj){
	var $contentTable = $('#contentTable');
	$contentTable.find('input[type="checkbox"]').each(function(){
		var $checkbox = $(this);
		if($checkbox.is(':checked')){
			$checkbox.removeAttr('checked');
		}else{
			$checkbox.attr('checked',true);
		}
	});
}

function batchApproval(ctx,checkBox) {
	var tmp=0;
	var count=0;
	$("input[name='"+checkBox+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp +","+$(this).attr('value');
			count++;
		}
	});
	if(tmp=="0"){
		alert("请选择审批记录");
		return;
	}
	$('#tip').text($('#tip').text().replace('num', count));
	$.jBox($("#batch-verify-list").html(), {
		title: "批量审批", buttons: {'取消': 0,'驳回': 1, '通过': 2}, submit: function (v, h, f) {
			var reason = f.reason;

			$.ajax({
				type:"POST",
				url:"${ctx}/costReview/activity/batchApproveOrReject",
				data:{reviewIds:tmp,reason:reason,type:v},
				success:function(data){
					window.location.reload();
				},
				error:function(){
					alert('返回数据失败');
				}
			});

		}, height: 250, width: 350
	});
	inquiryCheckBOX();
}

function revoke(reviewId){
	$.jBox.confirm("确定要撤销此审批吗？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:"${ctx}/costReview/activity/revoke",
				cache:false,
				async:false,
				data:{reviewId : reviewId},
				success:function(data){
					$.jBox.tip('操作成功', 'success');
					$("#searchForm").submit();
				},
				error : function(e){
					top.$.jBox.tip('请求失败。','error');
					return false;
				}
			});
		}
	});
}
</script>
</head>
<body>

				<!--右侧内容部分开始-->
				<form id="searchForm" action="${ctx}/costReview/activity/costReviewList/${budgetType}" method="post">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
					<input id="reviewer" name="reviewer" type="hidden"  value="${costParam.reviewer}"/>
					<input id="budgetType" name="budgetType" type="hidden" value="${budgetType}"/>
					<div class="activitylist_bodyer_right_team_co">
						<div class="activitylist_bodyer_right_team_co2 pr">
							<%--bug17491 搜索框name和id不对 by tlw at20170308--%>
							<%--<input id="orderNumOrGroupCode" name="commonCode" class="searchInput inputTxt inputTxtlong" value="${costParam.groupCode }" placeholder="输入团号/产品名称" flag="istips" onkeyup="this.value=this.value.replaceColonChars()" onafterpaste="this.value=this.value.replaceColonChars()">--%>
								<input class="txtPro inputTxt searchInput" id="groupCode" name="groupCode" value="${costParam.groupCode }" type="text" placeholder="输入团号/产品名称" />
							<%--							<label><!-- 团号： --></label>
							<input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="groupCode" name="groupCode" value="${costParam.groupCode }" type="text" flag="istips" />
							<span style="display: block;" class="ipt-tips">团号/产品名称</span>--%>
						</div>
						<a class="zksx">筛选</a>
						<div class="form_submit">
							<input class="btn btn-primary" value="搜索" type="submit">
						</div>
						<div class="ydxbd">
							<span></span>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">产品类型：</label>
								<div class="selectStyle">
									<select name="productType">
										<option value="0">全部</option>
										<c:forEach items="${productTypes }" var="productType">
											<option value="${productType.productType }" <c:if test="${costParam.productType eq productType.productType }">selected="selected"</c:if>>${productType.getProductName() }</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="activitylist_bodyer_right_team_co3">
								<label class="activitylist_team_co3_text">地接社：</label>
								<select id="supplyId" name="supplyId" >
									<option value="0">全部</option>
									<c:forEach items="${supplierList }" var="supplierInfo">
										<option value="${supplierInfo.id }" <c:if test="${costParam.supplyId eq supplierInfo.id }">selected="selected"</c:if>>${supplierInfo.supplierName }</option>
									</c:forEach>
								</select>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">申请日期：</label>
								<input id="createDateStart" class="inputTxt dateinput" name="createDateStart" value="${costParam.createDateStart }" onclick="WdatePicker()" readonly="readonly" />
								<span> 至 </span>
								<input id="createDateEnd" class="inputTxt dateinput" name="createDateEnd" value="${costParam.createDateEnd }" onclick="WdatePicker()" readonly="readonly" />
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">审批发起人：</label>
								<select id="createBy" name="createBy" >
									<option value="0">全部</option>
									<!-- 用户类型  1 代表销售 -->
									<c:forEach items="${fns:getSaleUserList('1')}" var="user">
										<option value="${user.id }" <c:if test="${costParam.createBy==user.id }">selected="selected"</c:if>>${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="activitylist_bodyer_right_team_co3">
								<label class="activitylist_team_co3_text">渠道选择：</label>
								<select id="agentId" name="agentId" >
									<option value="">全部</option>
									<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
									<c:choose>
									   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
									       <option value="-1">直客</option>
									   </c:when>
									   <c:otherwise>
									       <option value="-1">非签约渠道</option>
									   </c:otherwise>
									</c:choose>
									<c:forEach items="${agentList }" var="agent">
										<option value="${agent.id }" <c:if test="${costParam.agentId eq agent.id }">selected="selected"</c:if>>${agent.agentName }</option>
									</c:forEach>
								</select>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">出团日期：</label>
								<input id="groupOpenDateStart" class="inputTxt dateinput" name="groupOpenDateStart" value="${costParam.groupOpenDateStart }" onclick="WdatePicker()" readonly="readonly" />
								<span> 至 </span>
								<input id="groupOpenDateEnd" class="inputTxt dateinput" name="groupOpenDateEnd" value="${costParam.groupOpenDateEnd }" onclick="WdatePicker()" readonly="readonly" />
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">审批状态：</label>
								<div class="selectStyle">
									<select name="status">
										<option value="" selected="selected">全部</option>
										<option value="1" <c:if test="${costParam.status eq 1 }">selected="selected"</c:if>>审批中</option>
										<option value="2" <c:if test="${costParam.status eq 2 }">selected="selected"</c:if>>审批通过</option>
										<option value="0" <c:if test="${costParam.status eq 0 }">selected="selected"</c:if>>审批驳回</option>
										<option value="3" <c:if test="${costParam.status eq 3 }">selected="selected"</c:if>>取消申请</option>
									</select>
								</div>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<div class="activitylist_team_co3_text"><c:if test="${budgetType eq 0 }">预算</c:if><c:if test="${budgetType eq 1 }">成本</c:if>金额：</div>
								<input id="priceStart" name="priceStart" class="inputTxt" value="${costParam.priceStart }" />
								<span> 至 </span>
								<input id="priceEnd" name="priceEnd" class="inputTxt" value="${costParam.priceEnd }" />
							</div>
							<%-- <div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
								<select name="paymentType">
									<option value="">不限</option>
									<c:forEach var="pType" items="${fns:findAllPaymentType()}">
										<option value="${pType[0] }"
											<c:if test="${costParam.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
									</c:forEach>
								</select>
							</div> --%>
						</div>
					</div>
				</form>
				<!--状态开始-->
				<div class="supplierLine">
					<a id="all" name="reviewer" href="javascript:void(0)" onclick="reviewer(0)" <c:if test="${costParam.reviewer eq 0 or empty costParam.reviewer}">class="select"</c:if>>全部</a>
					<a id="todo" name="reviewer" href="javascript:void(0)" onclick="reviewer(1)" <c:if test="${costParam.reviewer eq 1 }">class="select"</c:if>>待本人审批</a>
					<a id="done" name="reviewer" href="javascript:void(0)" onclick="reviewer(2)" <c:if test="${costParam.reviewer eq 2 }">class="select"</c:if>>本人已审批</a>
					<a id="notdo" name="reviewer" href="javascript:void(0)" onclick="reviewer(3)" <c:if test="${costParam.reviewer eq 3 }">class="select"</c:if>>非本人审批</a>
				</div>

				<!--状态结束-->
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
						<ul>
							<%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
							<li class="activitylist_paixu_left_biankuang licreateDate"><a onclick="sortby('createDate',this)">创建时间</a></li>
							<li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
						</ul>
						</div>
						<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条</div>
						<div class="kong"></div>
					</div>
				</div>
				<table id="contentTable" class="table mainTable activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="4%">序号</th>
							<th width="10%"> <span class="tuanhao on">团号</span> / <span class="chanpin">产品名称</span></th>
							<th width="5%">产品类型</th>
							<th width="5%">申请时间</th>
							<th width="7%">审批发起人</th>
							<th width="6%"><span class="supplier on">地接社</span> / <span class="agent">渠道商</span></th>
							<th width="6%">出团日期<br />
								截团日期</th>
							<th width="6%">预收<br />余位</th>
							<th width="6%">款项名称</th>
							<th width="6%">
								<c:choose>
									<c:when test="${budgetType eq 0 }">预算金额</c:when>
									<c:when test="${budgetType eq 1 }">成本金额</c:when>
								</c:choose>
							</th>
							<th width="7%">汇率</th>
							<th width="6%">上一环节审批人</th>
							<th width="7%">审批状态</th>
							<th width="6%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(page.list) <= 0 }">
							<tr>
								<td class="tc" colspan="14">暂无审批记录</td>
							</tr>
						</c:if>
						<c:forEach items="${page.list}" var="activity" varStatus="s">
							<tr>
								<td class="tc">
									<c:if test="${costParam.reviewer eq 1 }">
										<input type="checkbox" name="checkBox" id="checkBox" value="${activity.reviewId }" <c:if test="${activity.status eq 2 }">disabled="disabled"</c:if> />
									</c:if>
									${s.count }</td>
								<td class="tc"><div title="${activity.groupCode}" class="tuanhao_cen onshow">${activity.groupCode}</div>
									<div title="${activity.activityName}" class="chanpin_cen qtip"> ${activity.activityName}</div></td>
								<td class="tc">${fns:getStringOrderStatus(activity.productType)}</td>
								<td class="p0"><div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${activity.createDate }" /></div>
									<div class="close-date time"><fmt:formatDate pattern="HH:mm:ss" value="${activity.createDate }" /></div></td>
								<td class="tc">${fns:getUserById(activity.createBy).name }</td>
								<td class="tc">
									<div title="${activity.groupCode}" class="supplier_cen onshow"><c:if test="${activity.supplyType eq 0 }">${activity.supplyName }</c:if></div>
									<div title="${activity.activityName}" class="agent_cen qtip"><c:if test="${activity.supplyType eq 1 }">
										<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
									<c:choose>
									   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && activity.supplyName=='非签约渠道'}">
									       直客
									   </c:when>
									   <c:otherwise>
									      ${activity.supplyName }
									   </c:otherwise>
									</c:choose>
									</c:if></div>
								</td>
								<td class="p0"><div class="out-date">${activity.groupOpenDate }</div>
									<div class="close-date">${activity.groupCloseDate }</div></td>
								<td class="p0"><div class="out-date">${activity.planPosition }</div>
									<div class="close-date">${activity.freePosition }</div></td>
								<td class="tc">${activity.name }</td>
								<td class="tr">${fns:getCurrencyNameOrFlag(activity.currencyId, "0") }<span class="fbold tdred">${activity.totalPrice }</span></td>
								<td class="tc">${activity.rate }</td>
								<td class="tc"><c:if test="${not empty activity.last_reviewer and activity.status ne 3 }">${fns:getUserNameById(activity.last_reviewer)}</c:if></td>
								<td class="<c:if test="${activity.status eq 2 }">invoice_yes</c:if> tc">
<!-- 									<c:if test="${activity.status eq 0 }">已驳回</c:if> -->
<!-- 									<c:if test="${activity.status eq 1 }">处理中</c:if> -->
<!-- 									<c:if test="${activity.status eq 2 }">已通过</c:if> -->
<!-- 									<c:if test="${activity.status eq 3 }">已取消</c:if> -->
										${fns:getChineseReviewStatus(activity.status, activity.current_reviewer)}
								</td>
								<td class="p0"><dl class="handle">
									<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png" /></dt>
									<dd class="">
										<p> <span></span>
											<c:if test="${activity.productType < 6 or activity.productType eq 10 }">
												<a href="${ctx}/costReview/activity/activityCostReviewDetail/${activity.productId}/${activity.groupId}/${activity.budgetType }?costId=${activity.costId }&read=1" target="_blank">查看</a>
												<c:if test="${costParam.reviewer eq 1 }">
													<a href="${ctx}/costReview/activity/activityCostReviewDetail/${activity.productId}/${activity.groupId}/${activity.budgetType }?costId=${activity.costId }" target="_blank">审批</a>
												</c:if>
											</c:if>
											<c:if test="${activity.productType eq 6 }">
												<a href="${ctx}/costReview/visa/visaCostReviewDetail/${activity.productId}/${activity.budgetType }?costId=${activity.costId }&read=1" target="_blank">查看</a>
												<c:if test="${costParam.reviewer eq 1 }">
													<a href="${ctx}/costReview/visa/visaCostReviewDetail/${activity.productId}/${activity.budgetType }?costId=${activity.costId }" target="_blank">审批</a>
												</c:if>
											</c:if>
											<c:if test="${activity.productType eq 7 }">
												<a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${activity.productId}/${activity.budgetType }?costId=${activity.costId }&read=1" target="_blank">查看</a>
												<c:if test="${costParam.reviewer eq 1 }">
													<a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${activity.productId}/${activity.budgetType }?costId=${activity.costId }" target="_blank">审批</a>
												</c:if>
											</c:if>
											<c:choose>
												<c:when test="${activity.productType < 6 or activity.productType eq 10 }">
													<a href="${ctx }/cost/manager/forcastList/${activity.groupId}/${activity.productType}" target="_blank">预报单</a>
													<a href="${ctx }/cost/manager/settleList/${activity.groupId}/${activity.productType}" target="_blank">结算单</a>
												</c:when>
												<c:when test="${activity.productType eq 6 or activity.productType eq 7 }">
													<a href="${ctx }/cost/manager/forcastList/${activity.productId}/${activity.productType}" target="_blank">预报单</a>
													<a href="${ctx }/cost/manager/settleList/${activity.productId}/${activity.productType}" target="_blank">结算单</a>
												</c:when>
											</c:choose>
											<c:if test="${activity.status eq 1 and not empty activity.last_reviewer and fns:getUser().id eq activity.last_reviewer }"><a href="#" onclick="revoke('${activity.reviewId }')">撤销</a></c:if>
										</p>
									</dd>
								</dl></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="page">
					<c:if test="${costParam.reviewer eq 1 }">
						<div class="pagination">
							<dl>
								<dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
								<dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
								<dd>
									<input class="btn ydbz_x" value="批量审批" type="button" onclick="batchApproval('${ctx }','checkBox');">
								</dd>
							</dl>
						</div>
					</c:if>
					<div class="pagination">
						<c:if test="${fn:length(page.list) ne 0}">
						</c:if>
						<div class="endPage">${page}</div>
						<div style="clear:both;"></div>
					</div>
				</div>
				<!--右侧内容部分结束-->

<!--S批量审核操作弹出层-->

<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
	<table width="100%" style="padding:10px !important; border-collapse: separate;">
		<tr>
			<td> </td>
		</tr>
		<tr>
			<td><p id="tip">您好，当前您提交了num个审批项目，是否执行批量操作？</p></td>
		</tr>
		<tr>
			<td><p>备注：</p></td>
		</tr>
		<tr>
			<td><label>
				<textarea name="reason" id="reason" style="width: 290px;"></textarea>
			</label></td>
		</tr>
	</table>
</div>

<!--S批量审核操作弹出层-->
</body>
</html>
