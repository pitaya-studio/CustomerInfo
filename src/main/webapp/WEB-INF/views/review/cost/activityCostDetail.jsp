<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>产品成本录入详情页</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>
	<style type="text/css">
		.td-extend .handle {
			background-image: none;
		}
		.xuanfu {
			position: absolute;
			top: 0px;
			left: 0;
			right:130px;
		}

		#hoverWindow {
			text-align:left;
			width:35em;
			top: 29%;
			left: 0%;
			word-wrap: break-word;
		}
	</style>
	<script type="text/javascript">
	$(function(){
		$('.zksxs').click(function() {
			if($('.ydxbds').is(":hidden")) {
				$('.ydxbds').show();
				$(this).text('收起筛选');
			}else{
				$('.ydxbds').hide();
				$(this).text('展开筛选');
			}
		});

		$(document).delegate(".downloadzfpz","click",function(){
			window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
		});

		//团号产品名称切换
		$("#contentTable").delegate("ul.caption > li","click",function(){
			var iIndex = $(this).index(); //index() 方法返回指定元素相对于其他指定元素的 index 位置。
			$(this).addClass("on").siblings().removeClass('on');
			$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
		});
	
		$("#contentTable").delegate(".tuanhao","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.chanpin_cen').removeClass('onshow');
			$('.tuanhao_cen').addClass('onshow');
			$("div.groupCode").show();
			$("div.acitivityName").hide();
		});
		
		$("#contentTable").delegate(".chanpin","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.tuanhao_cen').removeClass('onshow');
			$('.chanpin_cen').addClass('onshow');
			$("div.groupCode").hide();
			$("div.acitivityName").show();
		});
		
		var names = "";
		<c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
			<c:if test="${d.count eq 1}">
				names = "${data.name}";
			</c:if>
			<c:if test="${d.count gt 1}">
				names = names +","+"${data.name}";
			</c:if>
		</c:forEach>

		$("#targetAreaName div").text(names.toString());
	});
	
	var deleteCost = function(id, classType, groupId,visaId){
		$.jBox.confirm("确定要删除成本吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/cost/manager/delete",
					cache:false,
					async:false,
					data:{id : id,
						type : classType, groupId : groupId, orderType : '${orderType}',visaId : visaId, deptId : '${deptId}'},
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
	};

	var cancelCost = function(id, classType){
		$.jBox.confirm("确定要取消成本审批吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/costReview/activity/cancel",
					cache:false,
					async:false,
					data:{id : id,
						type : classType},
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
	};

	var cancelPayCost = function(reviewId){
		$.jBox.confirm("确定要取消付款审批吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/review/payment/web/cancelReview",
					cache:false,
					async:false,
					data:{reviewId:reviewId},
					success: function(data){
						if(data.flag){
							$.jBox.tip('取消成功', 'success');
							window.location.reload();
						}else{
							$.jBox.tip('取消失败，' + data.msg, 'error');
							return false;
						}
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error');
						return false;
					}
				});
			}
		});
	}
	
	function trimStr(str){
		return str.replace(/(^\s*)|(\s*$)/g,"");
	}
	var ht = $(window).height()*0.7;
	function addCostHQX(budgetType) {
		var groupId = '${activityGroup.id}';
		var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${activityId}' +"/"+ '${groupId}' +"/"+ budgetType  +"/"+ '${typeId}' +"/"+ '${deptId}';

		$.jBox(iframe, {
			title: "成本录入",
			width: 400,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function addOtherCostHQX(budgetType) {
		var groupId = '${activityGroup.id}';
		var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${activityId}' +"/"+ '${groupId}' +"/"+ budgetType  +"/"+ '${typeId}' +"/"+ '${deptId}';

		$.jBox(iframe, {
			title: "其它收入录入",
			width: 400,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function updateCostHQX(costid) {
		var groupId = '${activityGroup.id}';
		var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${activityId}' +"/"+ '${groupId}' +"/"+ costid  +"/"+ '${typeId}' +"/"+ '${deptId}';

		$.jBox(iframe, {
			title: "成本修改",
			width: 400,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function updateOtherCostHQX(costid) {
		var groupId = '${activityGroup.id}';
		var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${activityId}' +"/"+ '${groupId}' +"/"+ costid  +"/"+ '${typeId}' +"/"+ '${deptId}';

		$.jBox(iframe, {
			title: "其它收入修改",
			width: 400,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function payCheckBox(id,budgetType){
		var tmp= '';
	    $("input[name='"+id+"']").each(function(){
		    if ($(this).is(":checked")){
		    	if(!tmp){
		    		tmp = $(this).val();
		    	}else{
		    		tmp = tmp + "," + $(this).val();
		    	}
			}
	    });
	    if(!tmp){
	    	alert("请选择需要付款审批的成本项");
	    	return false;
	    }
	    $.ajax({
			type: "POST",
			url: "${ctx}/review/activity/payment/apply",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:tmp},
			success: function(data){
				if(data.flag){
					$.jBox.tip('申请付款成功', 'success');
					window.location.reload();
				}else{
					$.jBox.tip('付款申请失败，原因如下：' + data.msg, 'error');
					return false;
				}
			},
			error : function(e){
				$.jBox.tip('申请付款失败', 'error');
			    return false;
			}
		});
	}

	function iFrameHeight() {
		var ifm= document.getElementById("iframepage");
		var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument;
		if(ifm != null && subWeb != null) {
			ifm.height = subWeb.body.scrollHeight;
			ifm.width = subWeb.body.scrollWidth;
		}
	}

</script>
</head>
<body>
	<div class="mod_nav">运控 > 产品成本录入 > ${typename}录入详情页</div>
	<div class="produceDiv">
		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">产品基本信息</div>
			</div>
		</div>
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d mod_details2_d">
				<span style="color: #3a7851; font-size: 16px; font-weight: bold;">${travelActivity.acitivityName}</span>
				<div class="mod_information_d7"></div>
				<table border="0" width="90%">
					<tr>
						<td class="mod_details2_d1">产品编号：</td>
						<td class="mod_details2_d2">
							<c:choose> 
								<c:when test="${fn:length(travelActivity.activitySerNum) > 20}">
									<a style="text-decoration: none; color:inherit; cursor:default;" title="${travelActivity.activitySerNum}"><c:out value="${fn:substring(travelActivity.activitySerNum, 0, 20)}..." /></a> 
								</c:when> 
								<c:otherwise> 
									<c:out value="${travelActivity.activitySerNum}" />
								</c:otherwise>
							</c:choose>
						</td>
						<td class="mod_details2_d1">产品分类：</td>
						<td class="mod_details2_d2">
							<c:choose>
								<c:when test="${travelActivity.overseasFlag eq 1}">国外</c:when>
								<c:otherwise>国内</c:otherwise>
							</c:choose>
						</td>
						<c:if test="${typeId ne 10}">
							<td class="mod_details2_d1">旅游类型：</td>
							<td class="mod_details2_d2">${travelActivity.travelTypeName}</td>
						</c:if>
						<td class="mod_details2_d1">出发城市：</td>
						<td class="mod_details2_d2">${travelActivity.fromAreaName}</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">目的地：</td>
						<td class="mod_details2_d2" id="targetAreaName"
							title="123${travelActivity.targetAreaNames}">
							<div style="width:200px; text-overflow:ellipsis;overflow:hidden; white-space:nowrap;max-width:200px;">
								${travelActivity.targetAreaNames}
							</div>
						</td>
						<c:if test="${typeId ne 10}">
							<td class="mod_details2_d1">交通方式：</td>
							<td class="mod_details2_d2">
								${travelActivity.trafficModeName}
								<c:if test="${not empty travelActivity.trafficName and fn:indexOf(relevanceFlagId,travelActivity.trafficMode)>=0 }">
									&nbsp;&nbsp;|&nbsp;&nbsp;${travelActivity.trafficNameDesc}
								</c:if>
							</td>
						</c:if>
						<td class="mod_details2_d1">产品系列：</td>
						<td class="mod_details2_d2">${travelActivity.activityLevelName}</td>
						<td class="mod_details2_d1">团号：</td>
						<td class="mod_details2_d2">${activityGroup.groupCode}</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">行程天数：</td>
						<td class="mod_details2_d2" id="activityDuration">${travelActivity.activityDuration}</td>
						<td class="mod_details2_d1">付款方式：</td>
						<td colspan="5" class="mod_details2_d2">
							<c:if test="${travelActivity.payMode_deposit eq 1}">
								订金占位
								<c:if test="${not empty travelActivity.remainDays_deposit}">
									（保留${travelActivity.remainDays_deposit}天）&nbsp;&nbsp;
								</c:if>
							</c:if>
							<c:if test="${travelActivity.payMode_advance eq 1}">
								预占位（保留${travelActivity.remainDays_advance}天）&nbsp;&nbsp;
							</c:if>
							<c:if test="${travelActivity.payMode_guarantee eq 1}">
								担保占位（保留${travelActivity.remainDays_guarantee}天）&nbsp;&nbsp;
							</c:if>
							<c:if test="${travelActivity.payMode_data eq 1}">
								资料占位（保留${travelActivity.remainDays_data}天）&nbsp;&nbsp;
							</c:if>
							<c:if test="${travelActivity.payMode_express eq 1}">
								确认单占位（保留${travelActivity.remainDays_express}天）&nbsp;&nbsp;
							</c:if>
							<c:if test="${travelActivity.payMode_full eq 1}">
								全款支付
							</c:if>
							<c:if test="${travelActivity.payMode_op eq 1}">
								计调确认占位
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">计调人员：</td>
						<td class="mod_details2_d2">${travelActivity.createBy.name}</td>
						<td class="mod_details2_d1">创建时间：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelActivity.createDate }"/></td>
						<td class="mod_details2_d1">更新时间：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelActivity.updateDate }"/></td>
						<td class="mod_details2_d1">出团日期：</td>
						<td class="mod_details2_d2"><fmt:formatDate value="${activityGroup.groupOpenDate }" pattern="yyyy-MM-dd"/></td>
					</tr>
				</table>
				<div class="kong"></div>
			</div>
		</div>

		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">订单列表 &nbsp; <a class="zksxs">收起筛选</a></div>
			</div>
		</div>

		<div class="ydxbds" styple="display:inherit">
			<table class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
				<thead>
					<tr>
						<th width="7%">预定渠道</th>
						<th width="11%">订单号</th>
						<th width="13%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
						<th width="11%">预订时间</th>
						<th width="8%">出/截团日期</th>
						<th width="5%">人数</th>
						<th width="8%">订单状态</th>
						<th width="8%">订单总额</th>
						<th width="8%">已收金额<br>到账金额</th>
						<th width="6%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orderList}" var="groupOrder">
						<tr>
							<td class="tc">
							<c:choose>
							   	<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'
							   	&& groupOrder.orderCompanyName eq '非签约渠道'}">直客</c:when>
							   	<c:otherwise>${groupOrder.orderCompanyName}</c:otherwise>
							</c:choose>
							</td>
							<td class="tc">${groupOrder.orderNum}</td>
							<td class="tc">
								<div class="groupCode">${groupOrder.groupCode}</div>
								<div style="display:none;" class="acitivityName">${groupOrder.acitivityName}</div>
							</td>
							<td class="tc">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${groupOrder.orderTime }"/>
							</td>
							<td style="padding: 0px;">
								<div class="out-date">${groupOrder.groupOpenDate}</div>
								<div class="close-date">${groupOrder.groupCloseDate}</div>
							</td>
							<td class="tr">${groupOrder.orderPersonNum}</td>
							<td class="tc">${fns:getDictLabel(groupOrder.payStatus, "order_pay_status", "")}</td>
							<td class="tr"><span class="tdorange fbold">${groupOrder.totalMoney}</span></td>
							<td class="p0 tr">
								<div class="yfje_dd">
									<span class="fbold">${groupOrder.payedMoney}</span>
								</div>
								<div class="dzje_dd">
									<span class="fbold">${groupOrder.accountedMoney}</span>
								</div>
							</td>
							<td class="tc"><a target="_blank" href="${ctx}/orderCommon/manage/orderDetail/${groupOrder.id}" onclick="">详情</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	
		<div class="costSum clearfix" style="width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
			<ul class="cost-ul" data-total="cost">
				<ul class="cost-ul" data-total="income">
					<li>订单总收入：&nbsp;¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney') }" /></li>
				</ul>
				<li>订单总人数：&nbsp;${fns:getSum(orderList,'orderPersonNum') }</li>
			</ul>
		</div>

		<iframe id="iframepage" width="100%" height="100%" frameborder="0" src="${ctx}/cost/common/getCostRecordList/${activityGroup.id }/${typeId }" onLoad="iFrameHeight()"></iframe>

		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">审批日志</div>
			</div>
		</div>
		<div style="margin:0 auto; width:98%;">
			<ul class="spdtai">
				<c:forEach items="${costLog}" var="log" varStatus="status">
					<li><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${log.createDate}"/>&#12288;【${log.costName}】<c:if test="${log.result==-1}"><span class="invoice_back">审核已撤销</span></c:if><c:if test="${log.result==0}"><span class="invoice_back">审核未通过</span></c:if><c:if test="${log.result==1}"><span class="invoice_yes">审核通过</span></c:if>&#12288;【${log.name}】&#12288; <c:if test="${!empty log.remark}"><font color="red">批注:</font>&nbsp;${log.remark} </c:if></li>
				</c:forEach>
			</ul>
		</div>
		<%@ include file="/WEB-INF/views/review/common/cost_payment_review_log.jsp"%>
	</div>
</body>
</html>