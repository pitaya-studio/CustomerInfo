<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机票付款审核</title>
	<meta name="decorator" content="wholesaler"/>	
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		$("div.message_box div.message_box_li").hover(function(){
			$("div.message_box_li_hover",this).show();
		},function(){
			$("div.message_box_li_hover",this).hide();
		});
		$("#targetAreaId").val("${travelActivity.targetAreaIds}");
		$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
			
		//展开筛选按钮
    	$('.zksx').click(function(){
			if($('.ydxbd').is(":hidden")==true){
				$('.ydxbd').show();
				$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			}else{
				$('.ydxbd').hide();
				$(this).text('展开筛选');
				$(this).removeClass('zksx-on');
			}
		})
		//如果展开部分有查询条件的话，默认展开，否则收起	
		var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
		var searchFormselect = $("#searchForm").find("select");
		var inputRequest = false;
		var selectRequest = false;
		for(var i = 0; i<searchFormInput.length; i++){
			if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
				inputRequest = true;
			}
		}
		for(var i = 0; i<searchFormselect.length; i++) {
			if($(searchFormselect[i]).children("option:selected").val() != "" && 
					$(searchFormselect[i]).children("option:selected").val() != "100" &&
					$(searchFormselect[i]).children("option:selected").val() != null) {
				selectRequest = true;
			}
		}
		if(inputRequest||selectRequest) {
			$('.zksx').click();
		}
			
		var _$review = $("#review").val();
		if(_$review==""){
			$("#isRecord").addClass("select");
		}else{
			$("#isRecord"+_$review).addClass("select"); 
		}

		var _$orderBy = $("#orderBy").val();
		if(_$orderBy==""){
			_$orderBy="id DESC";
			$("#orderBy").val("id");
		}
		var orderBy = _$orderBy.split(" ");
		$(".activitylist_paixu_left li").each(function(){
			if($(this).hasClass("li"+orderBy[0])){
				orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
				$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				$(this).attr("class","activitylist_paixu_moren");
			}
		});
	});
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/payment/review/airTicketList/${reviewLevel}");
		$("#searchForm").submit();
	}

	function sortby(sortBy,obj){
		var temporderBy = $("#orderBy").val();
		if(temporderBy.match(sortBy)){
			sortBy = temporderBy;
			if(sortBy.match(/ASC/g)){
				sortBy = sortBy.replace(/ASC/g,"");
			}else{
				sortBy = $.trim(sortBy)+" ASC";
			}
		}
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}

	function changeIsRecord(itemRecord){
		$("#review").val(itemRecord);
		$(".supplierLine .select").removeClass("select");
		$("#isRecord"+itemRecord).addClass("select");
		$("#searchForm").submit();
	}

	function revokePayAudit(id, nowLevel, orderType){
		$.jBox.confirm("确定要撤销付款审核吗？", "提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/pay/review/revokePayAudit",
					cache:false,
					async:false,
					data:{id : id, nowLevel : nowLevel, orderType : orderType},
					success: function(e){
						window.location.reload();                     
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error');
						return false;
					}
				});
			}
		});
	}
	
	function jbox__shoukuanqueren_chexiao_fab(){
		var $contentTable = $('#contentTable');
		var items_val = '';
		var count = 0;
		$contentTable.find('input[name="batchItem"]').each(function(){
			if ($(this).is(":checked")){
				var temp = $(this).val();
				if(items_val == ''){
					items_val = temp;
				}else{
					items_val += '&&' + temp;
				}
				count++;
			}
		})
		if(count == 0){
			$.jBox.tip("请选择需要审核的项目", 'error');
			return false;
		}
		$('#batch-verify-list').find('p:first').text('您好，当前您提交了'+count+'个审核项目，是否执行批量操作？');
		$.jBox($("#batch-verify-list").html(),{
			title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
				var token = $('#token').val() || "";
				if (v == "1") {
					var comment = f.comment_val;
					return _denyCost(items_val,comment,token);
				}else if(v == "2"){
					var comment = f.comment_val;
					return _passCost(items_val,comment,token);
				}
			}, height: 250, width: 350
		});
	}
	
	//付款审核-通过某条成本
	function _passCost(items_val,comment,token){
		$.ajax({
			type: "POST",
			url: "${ctx}/pay/review/batchPass",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:items_val,comment:comment,token:token},
			success:function (data){
				if(data.flag){
					$.jBox.tip('审核通过', 'success');
					window.location.reload();
					return true;
				}else{
					$.jBox.tip(data.msg, 'error');
					return false;
				}
			},
			error: function (){
				$.jBox.tip("审核失败", 'error');
				return false;
			}
		});
	}
	
	//付款审核-拒绝某条成本
	function _denyCost(items_val,comment,token){
		$.ajax({
			type: "POST",
			url: "${ctx}/pay/review/batchDeny",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:items_val,comment:comment,token:token},
			success:function (data){
				if(data.flag){
					$.jBox.tip('驳回成功', 'success');
					window.location.reload();
					return true;
				}else{
					$.jBox.tip(data.msg, 'error');
					return false;
				}
			},
			error: function (){
				$.jBox.tip("驳回失败", 'error');
				return false;
			}
		});
	}
	//全选&反选操作
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
			$checkbox.trigger('change');
		});
	}
	</script>
<style type="text/css">
	.message_box{width:100%;padding:0px 0 40px 0;}
	.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
	.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
	.message_box_li .curret{background:#62afe7;}
	.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
	.message_box_li_a span{float:left;}
	.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
</style>

</head>
<body>
<page:applyDecorator name="payment_review_head">
	<page:param name="current">airticket</page:param>
</page:applyDecorator>
<div class="message_box">
<c:forEach items="${userJobs}" var="role">
	<div class="message_box_li">
	<c:choose>
		<c:when test="${userJobId==role.id}">
		<a href="${ctx}/payment/review/airTicketList/${reviewLevel}?userJobId=${role.id}">
			<div class="message_box_li_a curret">
			<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
			<c:if test ="${role.count gt 0}">
			<div class="message_box_li_em" >${role.count}</div>
			</c:if>
			<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
			<div class="message_box_li_hover">${role.deptName}_${role.jobName}</div>
			</c:if>
			</div>
		</a>
		</c:when>
		<c:otherwise>
		<a href="${ctx}/payment/review/airTicketList/${reviewLevel}?userJobId=${role.id}">
			<div class="message_box_li_a">
			<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
			<c:if test ="${role.count gt 0}">
			<div class="message_box_li_em">${role.count}</div>
			</c:if>
			<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
			<div class="message_box_li_hover">${role.deptName}_${role.jobName}</div>
			</c:if>
			</div>
		</a>
		</c:otherwise>
	</c:choose>
	</div>
</c:forEach>  
</div>

<div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
	<form:form id="searchForm" modelAttribute="activityAirTicket" action="${ctx}/payment/review/airTicketList/${reviewLevel}" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="review" name="review" type="hidden" value="${review}">
		<input id="reviewLevel" type="hidden" name="reviewLevel"  value="${reviewLevel}">
		<input id="userJobId" name="userJobId" type="hidden" value="${userJobId}">
		<input id="token" name="token" type="hidden" value="${token}">
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>出发城市：</label>
				<input name="departureCity" type="text" class="inputTxt" value="${departureCity}" style="width:130px;">
			</div>
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>到达城市：</label>
				<input  value="${arrivedCity}" name="arrivedCity" class="inputTxt" style="width:130px;">
			</div>
			<div class="form_submit">
				<input type="submit" value="搜索" class="btn btn-primary">
			</div>
			<a class="zksx">展开筛选</a>
			<div class="ydxbd" style="display: hidden;">
				<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">机票类型：</div>
					<select name="airType">
						<option value="">不限</option>
						<option value="3" ${activityAirTicket.airType=='3'?"selected":''}>单程</option>
						<option value="1" ${activityAirTicket.airType=='1'?"selected":''}>多段</option>
						<option value="2" ${activityAirTicket.airType=='2'?"selected":''}>往返</option>
					</select>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co2">
					<label>起飞时间：</label>
					<input readonly="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" style="margin-left: -3px;" value="${startingDate}" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate">
					<span> 至 </span>
					<input readonly="" onclick="WdatePicker()" value="${returnDate}" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co4_text" >审核发起人：</div>
					<input id="createByName" name="createByName" class="inputTxt inputTxtlong" value="${createByName }"/> 
				</div>
			</div>
			<div class="kong"></div>
		</div>
	</form:form>

	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li style="width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
					<li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
					<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
					<li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li> 
				</ul>
			</div>
			<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong id="total">${page.count}</strong>&nbsp;条</div>
			<div class="kong"></div>
		</div>
	</div>
	<div class="supplierLine">
		<a href="javascript:void(0)" onclick="changeIsRecord(11)" id="isRecord11">全部</a>
		<a href="javascript:void(0)" onclick="changeIsRecord('')" id="isRecord">待本人审核</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(22)" id="isRecord22">本人审核通过</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(1)" id="isRecord1">审核中</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(2)" id="isRecord2">已通过</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(0)" id="isRecord0">未通过</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(5)" id="isRecord5">已取消</a>
	</div>
	<table id="contentTable" class="table activitylist_bodyer_table" >
		<thead>
			<tr>
				<th width="3%">序号</th>
				<th width="7%">成本名称</th>   
				<th width="7%">渠道商/地接社</th>  
				<th width="6%">机票类型</th>
				<th width="6%">出发城市</th>
				<th width="6%">到达城市</th>
				<th width="6%">起飞日期</th>
				<th width="6%">到达日期</th>
				<th width="6%">应收价格</th>
				<th width="6%">金额</th>
				<th width="7%">已付金额</th>
				<th width="6%">审核发起<br>时间</th>
				<th width="6%">审核发起<br>人</th>
				<th width="6%">审核状态</th>
				<th width="4%">审核</th>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="airticket" varStatus="s">
			<c:if test="${empty review}">
			<tr id="review${airticket.id}">
			</c:if>
			<c:if test="${not empty review }">
			<tr id="review${airticket.id}_${review}">
			</c:if>
				<td>
					<c:if test="${empty review}">
						<input type="checkbox" name="batchItem" value="${airticket.id},${reviewLevel},${reviewCompanyId},7" />
					</c:if>${s.count}
				</td>
				<td class="tc">${airticket.name}</td>
				<td class="tc">${airticket.supplyName}</td>
				<td>
				<c:choose>
					<c:when test="${airticket.airType == 3}">单程</c:when>
					<c:when test="${airticket.airType == 2}">往返</c:when>
					<c:otherwise>多段</c:otherwise>
				</c:choose>
				</td>
                <td>${airticket.departureCityLabel()}</td>
				<td>
				<c:forEach items="${areas}" var="arrivedcity">
					<c:if test="${arrivedcity.id eq airticket.arrivedCity}">${arrivedcity.name}</c:if>
				</c:forEach>
				</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.startingDate }"/></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.returnDate }"/></td>
				<td>${airticket.currencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.settlementAdultPrice}"/></td>
				<td class="tc">${airticket.currencyMark} ${airticket.totalPrice}</td>
				<td class="tc">${fns:getPayCost(activity.id,typeId,1)}</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.updateDate}"/></td>
				<td>${fns:getUserNameById(airticket.costCreateBy)}</td>
				<td id="result${airticket.id}">${fns:getNextPayReview(airticket.id)}</td>
				<td class="tc" id="change${airticket.id}">
				<c:if test="${airticket.payNowLevel == reviewLevel && airticket.payReview==1}"> 
					<a href="${ctx}/payment/review/airTicketPreRecord/${airticket.airId}/${reviewLevel}/${reviewCompanyId}" target="_blank">审核</a>
				</c:if>
				</td>
				<td>
					<a href="${ctx}/payment/review/airTicketRead/${airticket.airId}/${reviewLevel}" target="_blank">查看</a>
					<br><a href="${ctx}/cost/manager/forcastList/${airticket.airId}/7" target="_blank">预报单</a><br>
					<br><a href="${ctx}/cost/manager/settleList/${airticket.airId}/7" target="_blank">结算单</a>
					<br>
					<c:if test="${airticket.payUpdateBy eq fns:getUser().id and airticket.payNowLevel ne 1 and airticket.payNowLevel-1 ne reviewCompany.topLevel and airticket.payReview ne 0 }">
					<a href="javascript:void(0)" onClick="revokePayAudit('${airticket.id}','${airticket.payNowLevel}','${airticket.product_type_id }')">撤销</a></c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>

	<div class="page">
		<c:if test="${empty review}">
			<div class="pagination">
				<dl>
					<dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
					<dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
					<dd><a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a></dd>
				</dl>
			</div>
		</c:if>
		<div class="pagination">
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	
	<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
		<table width="100%" style="padding:10px !important; border-collapse: separate;">
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td><p> </p></td>
			</tr>
			<tr>
				<td><p>备注：</p></td>
			</tr>
			<tr>
				<td>
					<label>
						<textarea name="comment_val" id="comment_val" style="width: 290px;"></textarea>
					</label>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>