<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>担保变更审批列表</title>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

	<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <%--<script type="text/javascript" src="${ctxStatic}/modules/order/single/activityListForOrder.js"></script>--%>
<script type="text/javascript">
	$(function(){
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();

		$('select[class="shenpifaqiren"]').comboboxInquiry();
		$('select[class="qudaoshang"]').comboboxInquiry();

		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function(){
			if($(this).attr("checked")){

			}else{
				$("input[name='allChk']").removeAttr("checked");
			}
		});

		var resetSearchParams = function(){
			$(':input','#searchForm')
					.not(':button, :submit, :reset, :hidden')
					.val('')
					.removeAttr('checked')
					.removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable').on('change','input[type="checkbox"]',function(){
			if( $('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length ){
				$('[name="allChk" ]').attr('checked',true);
			}else{
				$('[name="allChk" ]').removeAttr('checked');
			}
		});

		var _$orderBy = $("#orderBy").val();
		if(_$orderBy==""){
			_$orderBy="updateDate DESC";
			$("#orderBy").val("updateDate DESC");
		}
		var orderBy = _$orderBy.split(" ");
		$(".activitylist_paixu_left li").each(function(){
			if ($(this).hasClass("li"+orderBy[0])){
				orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
				$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				$(this).attr("class","activitylist_paixu_moren");
			}
		});

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
	});

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

	function reviewer(tag) {
		$("#reviewer").val(tag);
		$("#searchForm").submit();
	}

	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
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
					url:"${ctx}/guaranteeMod/batchApproveOrReject",
					data:{reviewUuids:tmp,reason:reason,type:v},
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

	function revoke(reviewUuid){
		$.jBox.confirm("确定要撤销此审批吗？","提示",function(v,h,f){
			if(v=='ok'){
				$.ajax({
					type:"POST",
					url:"${ctx}/guaranteeMod/revoke",
					cache:false,
					async:false,
					data:{reviewUuid : reviewUuid},
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
	<!--66 START-->
	<!--右侧内容部分开始-->
	<form id="searchForm" action="${ctx}/guaranteeMod/guaranteeReviewList" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="reviewer" name="reviewer" type="hidden"  value="${reviewer}"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<input class="txtPro inputTxt searchInput" id="wholeSalerKey" name="wholeSalerKey" value="${wholeSalerKey}" type="text" placeholder="团号/订单号" />
				<%--<span style="display: block;" class="ipt-tips">团号/订单号</span>--%>
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit">
			</div>
			<div class="ydxbd text-more-new">
				<span></span>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">审批发起人：</label>
					<select name="createBy" class="shenpifaqiren">
						<option value="">全部</option>
						<!-- 用户类型  1 代表销售 -->
						<c:forEach items="${fns:getSaleUserList('1')}" var="user">
							<option value="${user.id }" <c:if test="${createBy eq user.id }">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">渠道商：</label>
					<select name="agentInfo" class="qudaoshang">
						<option value="">全部</option>
						<c:forEach items="${agentList }" var="agent">
							<option value="${agent.id }" <c:if test="${agentInfo eq agent.id }">selected="selected"</c:if>>${agent.agentName }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">游客：</label>
					<input name="travelerName" type="text" value="${travelerName}" />
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">审批状态：</label>
					<div class="selectStyle">
						<select name="reviewStatus">
							<option value="" selected="selected">全部</option>
							<option value="1" <c:if test="${reviewStatus eq '1' }">selected="selected"</c:if>>审批中</option>
							<option value="2" <c:if test="${reviewStatus eq '2' }">selected="selected"</c:if>>审批通过</option>
							<option value="0" <c:if test="${reviewStatus eq '0' }">selected="selected"</c:if>>审批驳回</option>
							<option value="3" <c:if test="${reviewStatus eq '3' }">selected="selected"</c:if>>取消申请</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">申请日期：</label>
					<input class="inputTxt dateinput" name="createDateStart" value="${createDateStart}" onclick="WdatePicker()" readonly="readonly" />
					<span> 至 </span>
					<input class="inputTxt dateinput" name="createDateEnd" value="${createDateEnd}" onclick="WdatePicker()" readonly="readonly" />
				</div>
			</div>
			<div class="kong"></div>
		</div>
	</form>

	<!--状态开始-->
	<div class="supplierLine">
		<a id="all" name="reviewer" href="javascript:void(0)" onclick="reviewer(0)" <c:if test="${reviewer eq 0 or empty reviewer}">class="select"</c:if>>全部</a>
		<a id="todo" name="reviewer" href="javascript:void(0)" onclick="reviewer(1)" <c:if test="${reviewer eq 1 }">class="select"</c:if>>待本人审批</a>
		<a id="done" name="reviewer" href="javascript:void(0)" onclick="reviewer(2)" <c:if test="${reviewer eq 2 }">class="select"</c:if>>本人已审批</a>
		<a id="notdo" name="reviewer" href="javascript:void(0)" onclick="reviewer(3)" <c:if test="${reviewer eq 3 }">class="select"</c:if>>非本人审批</a>
	</div>

	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li class="activitylist_paixu_left_biankuang licreateDate"><a onclick="sortby('createDate',this)">创建时间</a></li>
					<li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
				</ul>
			</div>
			<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条</div>
			<div class="kong"></div>
		</div>
	</div>

	<!--状态结束-->
	<table id="contentTable" class="table mainTable activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="4%">序号</th>
				<th width="8%">订单号</th>
				<th rowspan="2" width="8%">
					<span class="tuanhao on">团号</span>
					/
					<span class="chanpin">产品名称</span>
				</th>
				<th width="5%">申请时间</th>
				<th width="7%">审批发起人</th>
				<th width="6%">渠道商</th>
				<th width="6%">游客</th>
				<th width="6%">原担保类型</th>
				<th width="6%">原押金金额</th>
				<th width="6%">申请转担保类型</th>
				<th width="7%">申请交押金金额</th>
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
			<c:forEach items="${page.list}" var="guaranteeReview" varStatus="status">
				<tr>
					<td><c:if test="${reviewer eq 1 }"><input type="checkbox" name="checkbox" value="${guaranteeReview.reviewUuid}" /></c:if>
						${status.count}
					</td>
					<td>${guaranteeReview.visaorderNo}</td>
					<td class="tl">
						<div title="${guaranteeReview.groupCode}" class="tuanhao_cen onshow">${guaranteeReview.groupCode}</div>
						<div title="${guaranteeReview.productName}" class="chanpin_cen qtip">
							<a href="${ctx}/visa/visaProducts/visaProductsDetail/${guaranteeReview.visaProductId}" target="_blank">${guaranteeReview.productName}</a>
						</div>
					</td>
					<td class="tc">
						<div class="yfje_dd">
							<span class="fbold">
									<fmt:formatDate value="${guaranteeReview.createDate}" pattern="yyyy-MM-dd"/>
							</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">
									<fmt:formatDate value="${guaranteeReview.createDate}" pattern="HH:mm:ss"/>
							</span>
						</div>
					</td>
					<td class="tc">${fns:getUserNameByIds(guaranteeReview.createBy)}</td>
					<td class="tc">${fns:getAgentName(guaranteeReview.agentinfoId)}</td>
					<td class="tc">${guaranteeReview.travelerName}</td>
					<td class="tc">
						<c:choose>
							<c:when test="${guaranteeReview.guaranteeStatus eq 1}">担保</c:when>
							<c:when test="${guaranteeReview.guaranteeStatus eq 2}">担保+押金</c:when>
							<c:when test="${guaranteeReview.guaranteeStatus eq 3}">押金</c:when>
							<c:when test="${guaranteeReview.guaranteeStatus eq 4}">无需担保</c:when>
						</c:choose>
					</td>
					<td class="tr">${guaranteeReview.totalDeposit}</td>
					<td class="tr">
						<c:choose>
							<c:when test="${guaranteeReview.newGuaranteeType eq 1}">担保</c:when>
							<c:when test="${guaranteeReview.newGuaranteeType eq 2}">担保+押金</c:when>
							<c:when test="${guaranteeReview.newGuaranteeType eq 3}">押金</c:when>
							<c:when test="${guaranteeReview.newGuaranteeType eq 4}">无需担保</c:when>
						</c:choose>
					</td>
					<td class="tc"><c:if test="${guaranteeReview.newGuaranteeType eq 2 or guaranteeReview.newGuaranteeType eq 3}">${fns:getCurrencyNameOrFlag(guaranteeReview.currencyId, '0') }${guaranteeReview.amount}</c:if></td>
					<td class="tc">${fns:getUserNameById(guaranteeReview.last_reviewer)}</td>
					<td class="tc">${fns:getChineseReviewStatus(guaranteeReview.reviewStatus, guaranteeReview.current_reviewer)}</td>
					<td class="tc">
						<a href="${ctx}/guaranteeMod/guaranteeApply?orderId=${guaranteeReview.visaorderId}&flag=detail&review=1&travelerId=${guaranteeReview.id}&nav=1&title=1" target="_blank">查看</a>
						<c:if test="${reviewer eq 1 }">
							|<a href="${ctx}/guaranteeMod/guaranteeApply?orderId=${guaranteeReview.visaorderId}&flag=approval&travelerId=${guaranteeReview.id}" target="_blank">审批</a>
						</c:if>
						<c:if test="${guaranteeReview.reviewStatus eq 1 and not empty guaranteeReview.last_reviewer and fns:getUser().id eq guaranteeReview.last_reviewer }">|<a href="#" onclick="revoke('${guaranteeReview.reviewUuid }')">撤销</a></c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="page">
		<c:if test="${reviewer eq 1 }">
			<div class="pagination">
				<dl>
					<dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
					<dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
					<dd>
						<input class="btn ydbz_x" type="button" value="批量审批" onclick="batchApproval('${ctx }','checkbox');">
					</dd>
				</dl>
			</div>
		</c:if>
		<div class="pagination">
			<div class="endPage">
				<ul>
					${page}
				</ul>
				<div style="clear:both;"></div>
			</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	<!--右侧内容部分结束-->
	<!--66 END-->

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
