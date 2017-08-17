<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审批-退签证押金审批-列表</title>
<meta name="decorator" content="wholesaler" />
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();
		$("#channelrefund" ).comboboxInquiry();
		$("#salerrefund" ).comboboxInquiry();
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function() {
			if ($(this).attr("checked")) {

			} else {
				$("input[name='allChk']").removeAttr("checked");
			}
		});

		var resetSearchParams = function() {
			$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
					.val('').removeAttr('checked').removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable')
				.on(
						'change',
						'input[type="checkbox"]',
						function() {
							if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
								$('[name="allChk" ]').attr('checked', true);
							} else {
								$('[name="allChk" ]').removeAttr('checked');
							}
						});
	});

	//展开收起
	function expand(child, obj) {
		if ($(child).is(":hidden")) {
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");

		} else {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");

		}
	}
	function checkall(obj) {
		if ($(obj).attr("checked")) {
			$('#contentTable input[type="checkbox"]').attr("checked", 'true');
			$("input[name='allChk']").attr("checked", 'true');
		} else {
			$('#contentTable input[type="checkbox"]').removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
		}
	}

	function checkreverse(obj) {
		var $contentTable = $('#contentTable');
		$contentTable.find('input[type="checkbox"]').each(function() {
			var $checkbox = $(this);
			if ($checkbox.is(':checked')) {
				$checkbox.removeAttr('checked');
			} else {
				$checkbox.attr('checked', true);
			}
		});
	}
		//排序
	function sort(element){

		var _this = $(element);

		//按钮高亮
		_this.parent("li").attr("class","activitylist_paixu_moren");

		//原先高亮的同级元素置灰
		_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");

		//高亮按钮隐藏input赋值
		_this.next().val("activitylist_paixu_moren");

		//原先高亮按钮隐藏input值清空
		_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");

		var sortFlag = _this.children().attr("class");
		//降序
		if(sortFlag == "icon icon-arrow-up"){

			//改变箭头的方向
			_this.children().attr("class","icon icon-arrow-down");

			//降序
			_this.prev().val("desc");
		}
		//降序
		else if(sortFlag == "icon icon-arrow-down"){
			//改变箭头方向
			_this.children().attr("class","icon icon-arrow-up");

			//shengx
			_this.prev().val("asc");
		}

		$("#searchForm").submit();

		return false;
	}
	function statusChooses(status){
		$("#tabStatus").val(status);
		$("#searchForm").submit();
	}
		function jbox__shoukuanqueren_chexiao_fab() {
		var num = 0;
		 var str="";
		$('[name=checkboxrevid]:checkbox:checked').each(function(){
			if(num == 0){
				str+=$(this).val();
			} else {
				str+=","+$(this).val();
			}
			num++;
		});
		if("" == str)
		{
			$.jBox.tip("请选择需要审批的记录！"); 
			return false;
		}
		var html = "<table width='100%' style='padding:10px !important; border-collapse: separate;'>"
				  +"   <tr>"
				  +"     <td> </td>"
				  +"  </tr>"
				  +"   <tr>"
				  +"     <td><p>您好，当前您提交了"+num+"个审核项目，是否执行批量操作？</p></td>"
				  +"  </tr>"
				  +"   <tr>"
				  +"     <td><p>备注：</p></td>"
				  +"  </tr>"
				  +"   <tr>"
			      +" <td><label>"
			      +"     <textarea name='textfield' id='textfield' style='width: 290px;'></textarea>"
			      +"   </label></td>"
			      +" </tr>"
				  +"</table>";
	    $.jBox(html, {
	        title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
	            if (v == "1" || v == "2") {
		            var remark = f.textfield;
		            if(remark.length>200){
		            	$.jBox.tip("字符长度过长，请输入少于200个字符", 'error');
		            	return;
		            }
		            $.ajax({
		            	type : "post",
						url : "${ctx}/depositenew/batchdepositeReview",
						data:{ 
							"revIds":str,
							"remark" : remark,
							"result" : v
						},
						success : function(msg) {
							$.jBox.tip(msg.msg);
							$("#searchForm").submit();
						}
		            })
	            }
	        }, height: 250, width: 350
	    });
	    inquiryCheckBOX();
	}
	function backReview(reviewId){
		$.ajax({
			type: "post",
			url : "${ctx}/depositenew/backrefundreview/" + reviewId,
			success : function(data){
				if(data.flag == "error"){
					$.jBox.tip(data.msg); 
				} else {
					$.jBox.tip("撤销成功"); 
					$("#searchForm").submit();
				}
			}
		})
	}
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};
</script>
<style type="text/css">
.text-more-new .activitylist_team_co3_text,.text-more-new .activitylist_bodyer_right_team_co2 label
	{
	width: 90px;
	text-align: right;
}

.text-more-new .activitylist_bodyer_right_team_co1,.text-more-new .activitylist_bodyer_right_team_co3
	{
	min-width: 200px;
}

.text-more-new .activitylist_bodyer_right_team_co2 {
	min-width: 400px;
}
</style>
</head>
<body>
	<!--右侧内容部分开始-->
	<form id="searchForm" action="${ctx}/depositenew/depositeRefundReviewList" method="post">
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>搜索：</label>
				<input style="width:260px" id="wholeSalerKey" name="groupCode"
                           class="inputTxt inputTxtlong" value="${conditionsMap.groupCode}"
                           flag="istips" onKeyUp="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()"/>
                <span class="ipt-tips" style="display: block;">团号/订单号</span>
			</div>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit">
			</div>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" /> <input id="orderBy" name="orderBy"
				type="hidden" value="${page.orderBy}" /> <a class="zksx">展开筛选</a>
			<div class="ydxbd text-more-new">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">渠道选择：</div>
					<select name="agentId" id="channelrefund">
						<option value="">全部</option>
						<c:if test="${not empty fns:getAgentList() }">
							<c:forEach items="${fns:getAgentList()}" var="agentinfo">
								<option value="${agentinfo.id }"
									<c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName
									}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<!-- 				<div class="activitylist_bodyer_right_team_co1"> -->
				<!-- 					<div class="activitylist_team_co3_text">下单人：</div> -->
				<!-- 					<select> -->
				<!-- 						<option value="">张三</option> -->

				<!-- 					</select> -->
				<!-- 				</div> -->
				<div class="activitylist_bodyer_right_team_co2">
					<label>申请日期：</label> <input id="" class="inputTxt dateinput"
						name="applyDateFrom" value="${conditionsMap.applyDateFrom}"
						onclick="WdatePicker()" readonly="readonly" /> <span> 至 </span> <input
						id="" class="inputTxt dateinput" name="applyDateTo"
						value="${conditionsMap.applyDateTo}" onclick="WdatePicker()"
						readonly="readonly" />
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">审批发起人：</div>
					<select name="applyPerson" id="salerrefund">
						<option value="-99999" selected="selected">不限</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }"
								<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name
								}</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">游客：</div>
					<input type="text" value="${conditionsMap.travelerId}"
						name="travelerId"></input>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">审批状态：</div>
					<select name="reviewStatus" id="">
						<option value="" selected="selected">全部</option>
						<option value="1"
							<c:if test="${conditionsMap.reviewStatus == 1 }">selected="selected"</c:if>>审批中</option>
						<option value="2"
							<c:if test="${conditionsMap.reviewStatus == 2 }">selected="selected"</c:if>>已通过</option>
						<option value="0"
							<c:if test="${conditionsMap.reviewStatus == 0 }">selected="selected"</c:if>>未通过</option>
					</select>
				</div>
				<div class="kong"></div>

			</div>
			<div class="kong"></div>
		</div>
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li
							style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
						<c:choose>
							<c:when
								test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
								<li class="activitylist_paixu_moren">
							</c:when>
							<c:otherwise>
								<li class="activitylist_paixu_left_biankuang">
							</c:otherwise>
						</c:choose>
						<input type="hidden" value="" name="orderCreateDateSort">
						<a onclick="sort(this)">创建日期 <c:choose>
								<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
									<i class="icon icon-arrow-down"></i>
								</c:when>
								<c:otherwise>
									<i class="icon icon-arrow-up"></i>
								</c:otherwise>
							</c:choose> </a>
						<input type="hidden" value="" name="orderCreateDateCss">
						</li>
						<c:choose>
							<c:when
								test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
								<li class="activitylist_paixu_moren">
							</c:when>
							<c:otherwise>
								<li class="activitylist_paixu_left_biankuang">
							</c:otherwise>
						</c:choose>
						<input type="hidden" value="" name="orderUpdateDateSort">
						<a onclick="sort(this)">更新日期 <c:choose>
								<c:when test="${conditionsMap.orderUpdateDateSort == 'desc' }">
									<i class="icon icon-arrow-down"></i>
								</c:when>
								<c:otherwise>
									<i class="icon icon-arrow-up"></i>
								</c:otherwise>
							</c:choose> </a>
						<input type="hidden" value="" name="orderUpdateDateCss">
						</li>
					</ul>
				</div>
				<input type="hidden" id="tabStatus" name="tabStatus"
					value="${conditionsMap.tabStatus}" />
	</form>
	<div class="activitylist_paixu_right">
		查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
	</div>
	<div class="kong"></div>
	</div>
	</form>
	</div>
	<!--状态开始-->
	<div class="supplierLine">
		<a href="javascript:void(0)" id="all" onClick="statusChooses('0')"
			<c:if test = "${conditionsMap.tabStatus == null || conditionsMap.tabStatus == 0 || conditionsMap.tabStatus == '0'}">class="select" </c:if>>全部</a>
		<a id="todo" href="javascript:void(0)" onClick="statusChooses('1')"
			<c:if test = "${conditionsMap.tabStatus == '1' || conditionsMap.tabStatus == 1}">class="select" </c:if>>
			待本人审批</a> <a id="todo" href="javascript:void(0)"
			onClick="statusChooses('2')"
			<c:if test = "${conditionsMap.tabStatus == '2' || conditionsMap.tabStatus == 2}">class="select" </c:if>>本人已审批</a>
		<a id="todoing" href="javascript:void(0)" onClick="statusChooses('3')"
			<c:if test = "${conditionsMap.tabStatus == '3' || conditionsMap.tabStatus == 3}">class="select" </c:if>>非本人审批</a>
	</div>

	<!--状态结束-->
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="4%">序号</th>
				<th width="8%">订单号</th>
				<th width="8%"><span class="tuanhao on">团号</span> / <span
					class="chanpin">产品名称</span>
				</th>
				<th width="5%">申请时间</th>
				<th width="7%">审批发起人</th>
				<th width="6%">渠道商</th>
				<!-- 				<th width="6%">下单人</th> -->
				<th width="6%" class="tc">游客</th>
				<th width="7%">押金金额</th>
				<th width="6%">上一环节审批人</th>
				<th width="7%">审批状态</th>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr>
					<td colspan="14" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>
			<%
				int n = 1;
			%>
			<c:forEach items="${page.list}" var="depositeRefundReviewInfo">
				<tr>
					<td>
						<c:if test = "${conditionsMap.tabStatus == '1'}">
							<input type="checkbox" name="checkboxrevid" value="${depositeRefundReviewInfo.reviewid}" /> 
						</c:if><%=n++%>
					</td>
					<td>${depositeRefundReviewInfo.orderno}</td>
					<td><div title="${depositeRefundReviewInfo.groupcode}"
							class="tuanhao_cen onshow">${depositeRefundReviewInfo.groupcode}</div>
						<div title="${depositeRefundReviewInfo.productname}"
							class="chanpin_cen qtip">
							<a
								href="${ctx}/visa/preorder/visaProductsDetail/${depositeRefundReviewInfo.productid}"
								target="_blank">${depositeRefundReviewInfo.productname}</a>
						</div></td>
					<td class="p0"><div class="out-date">
							<fmt:formatDate value="${depositeRefundReviewInfo.createdate}"
								pattern="yyyy-MM-dd" />
						</div>
						<div class="close-date time">
							<fmt:formatDate value="${depositeRefundReviewInfo.createdate}"
								pattern="HH:mm:ss" />
						</div>
					</td>
					<td class="tc">${fns:getUserNameById(depositeRefundReviewInfo.createby)}</td>
					<td class="tc">${fns:getAgentName(depositeRefundReviewInfo.agent)}</td>
					<!-- 				<td class="tc">系统管理员</td> -->
					<td class="tc">${depositeRefundReviewInfo.travelername}</td>
					<td class="tr">¥<span class="fbold tdred">
							${fns:getCurrencyNameOrFlag(depositeRefundReviewInfo.currencyid,
							0)}${depositeRefundReviewInfo.amount} </span></td>
					<td class="tc">${fns:getUserNameById(depositeRefundReviewInfo.lastreviewer)}</td>
					<td class="invoice_yes tc">${depositeRefundReviewInfo.statusdesc}</td>
					<td class="p0"><dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
							</dt>
							<dd class="">
								<p>
									<span></span>
									<c:if test="${depositeRefundReviewInfo.isCurReviewer == true}">
										<a href="${ctx}/depositenew/depositeRefundReviewDetail?orderid=${depositeRefundReviewInfo.orderid}&revid=${depositeRefundReviewInfo.reviewid}&prdType=${depositeRefundReviewInfo.producttype}&flag=0"
											target="_blank">审批</a>
									</c:if>
									<a href="${ctx}/depositenew/depositeRefundReviewDetail?orderid=${depositeRefundReviewInfo.orderid}&revid=${depositeRefundReviewInfo.reviewid}&prdType=${depositeRefundReviewInfo.producttype}&flag=1"
										target="_blank">查看</a> 
										<a href="javascript:void(0)">预报单</a> 
										<a href="javascript:void(0)">结算单</a>
									<c:if test="${depositeRefundReviewInfo.isBackReview == true}">
										<a href="javascript:void(0)" onClick="backReview('${depositeRefundReviewInfo.reviewid}')">撤销</a>
									</c:if>
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
			<c:if test="${conditionsMap.tabStatus == '1'}">
				<div class="pagination">
					<dl>
						<dt>
							<input name="allChk" onclick="checkall(this)" type="checkbox">
							全选
						</dt>
						<dt>
							<input name="reverseChk" onclick="checkreverse(this)"
								type="checkbox"> 反选
						</dt>
						<dd>
							<a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a>
						</dd>
					</dl>
				</div>
			</c:if>
		</div>
		<div class="pagination clearFix">${page}</div>
	</div>
	<!--右侧内容部分结束-->
	<!--S批量审核操作弹出层-->

<!-- 	<div class="batch-verify-list" id="batch-verify-list" -->
<!-- 		style="padding:20px;"> -->
<!-- 		<table width="100%" -->
<!-- 			style="padding:10px !important; border-collapse: separate;"> -->
<!-- 			<tr> -->
<!-- 				<td></td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 				<td><p>您好，当前您提交了23个审核项目，是否执行批量操作？</p> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 				<td><p>备注：</p> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 				<td><label> <textarea name="textfield" id="textfield" -->
<!-- 							style="width: 290px;"></textarea> </label> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
<!-- 	</div> -->

	<!--S批量审核操作弹出层-
</body>
</html>
