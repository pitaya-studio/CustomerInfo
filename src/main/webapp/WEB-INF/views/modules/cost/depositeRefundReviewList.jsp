<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>财务-财务审核-退签证押金</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	
	inputTips();
	
	//计调模糊匹配
	$("[name=operatorId]").comboboxSingle();
	//渠道模糊匹配
	$("[name=channel]").comboboxSingle();
	//销售模糊匹配
	$("[name=salerId]").comboboxSingle();
	//产品销售和下单人切换
    switchSalerAndPicker();
    //下单恩模糊匹配
    $("[name=orderPersonId]").comboboxSingle();
	
	$("div.message_box div.message_box_li").hover(function(){
	    $("div.message_box_li_hover",this).show();
	},function(){
        $("div.message_box_li_hover",this).hide();
	});
	
	//排序代码
	var _$orderBy = $("#orderBy").val() || 'createDate DESC';//如果取值为空(null/"")则默认为 "createDate DESC"
	$("#orderBy").val(_$orderBy);
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if ($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
        }
    });
    
    var statusChooseVal = $('#statusChoose').val() || '1';//如果取值为空则默认为 1
    $('.supplierLine a').each(function(index){
    	$(this).removeClass('select');
    	if(index == statusChooseVal){
    		$(this).addClass('select');
    	}
	})
});

//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var inputRequest = false;
	var selectRequest = false;
	$("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey']").each(function(){
		if($(this).val()){
			inputRequest = true;
			return false;
		}
	})
	$("#searchForm").find("select").each(function(){
		if($(this).children("option:selected").val()){
			selectRequest = true;
			return false;
		}
	})
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}else{
		$('.zksx').text('展开筛选');
	}
}

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
};
function statusChooses(statusNum) {
	$("#statusChoose").val(statusNum);
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
function cancelDeposite(review_id, my_check_level){
	if(!review_id){
		$.jBox.tip("审核ID不能为空", 'error');
		return false;
	}
	if(!my_check_level){
		$.jBox.tip("您所在的审核层级不能为空", 'error');
		return false;
	}
	$.jBox.confirm("确定要撤销此审核吗？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:"${ctx}/deposite/cancelDeposite",
				data:{reviewId:review_id,myCheckLevel:my_check_level},
				success:function(data){
					if('success' == data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip(data.msg, 'error');
					}
				}
			})
		}
	});
}
function jbox__shoukuanqueren_chexiao_fab(){
	var $contentTable = $('#contentTable');
	var items_val = '';
	var count = 0;
	$contentTable.find('input[name="batchItem"]').each(function(){
		if ($(this).attr("checked") != null && $(this).attr("checked")=="checked"){
			var temp = $(this).val();
			if(items_val == ''){
				items_val = temp;
			}else{
				items_val += ',' + temp;
			}
			count++;
		}
	})
	if(!count){
		$.jBox.tip("请选择需要审批的项目", 'error');
		return false;
	}
	$('#batch-verify-list').find('p:first').text('您好，当前您提交了'+count+'个审核项目，是否执行批量操作？');
	$.jBox($("#batch-verify-list").html(),{
		title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
			if (v == "1" || v == "2") {
				var comment = f.comment_val;
				return _passOrDenyCost(items_val, comment, v);
			}
		}, height: 250, width: 350
	});
}

//付款审核-通过某条成本
function _passOrDenyCost(items_val, comment, result){
	$.ajax({
		type: "POST",
		url: "${ctx}/deposite/batchdepositeReview",
		data:{"revIds":items_val,"remark":comment,"result":result},
		success:function (data){
			window.location.reload();
			return true;
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

function cashConfirm(traid){
	$.ajax({
			type:"POST",
			url:"${ctx}/deposite/cashConfirm",
			data:{traid:traid},
			success:function(data){
				if('success' == data.flag){
					$.jBox.tip('操作成功', 'success');
					$("#searchForm").submit();
				}else{
					$.jBox.tip(data.msg, 'error');
				}
			}
		})
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
	<!--右侧内容部分开始-->
	<%--<div class="message_box">--%>
		<%--<c:forEach items="${userJobs}" var="userJob">--%>
			<%--<div class="message_box_li">--%>
			<%--<c:if test="${conditionsMap.userJobId == userJob.id}">--%>
			<%--<div class="message_box_li_a curret">--%>
				<%--<a href="${ctx}/deposite/costDepositeRefundReviewList?userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>--%>
				<%--<c:if test ="${userJob.count gt 0}">--%>
					<%--<div class="message_box_li_em" >${userJob.count}</div>--%>
				<%--</c:if>--%>
				<%--<c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">--%>
					<%--<div class="message_box_li_hover">${userJob.deptName}_${userJob.jobName}</div>--%>
				<%--</c:if>--%>
				<%--</a>--%>
			<%--</div>--%>
			<%--</c:if> --%>
			<%--<c:if test="${conditionsMap.userJobId != userJob.id}">--%>
			<%--<div class="message_box_li_a">--%>
				<%--<a href="${ctx}/deposite/costDepositeRefundReviewList?userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>--%>
				<%--<c:if test ="${userJob.count gt 0}">--%>
				<%--<div class="message_box_li_em" >${userJob.count}</div>--%>
				<%--</c:if>--%>
				<%--<c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">--%>
				<%--<div class="message_box_li_hover">${userJob.deptName}_${userJob.jobName}</div>--%>
				<%--</c:if>--%>
				<%--</a>--%>
			<%--</div>--%>
			<%--</c:if> --%>
		<%--</div>--%>
	<%--</c:forEach>--%>
<%--</div>--%>
        <form id="searchForm" action="${ctx}/deposite/costDepositeRefundReviewList" method="post">
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
			<input type="hidden" value ="${conditionsMap.userJobId}" name="userJobId"/><!-- 角色id -->
			<input type="hidden" value=7 name="flowType"/><!-- 流程类型 7 退款 -->
			<!-- 默认为待本人审核 -->
			<input id="statusChoose" name="statusChoose" type="hidden" value="${conditionsMap.statusChoose}" />
	        <div class="activitylist_bodyer_right_team_co">
	            <div class="activitylist_bodyer_right_team_co2 pr wpr20">
					<label>团号：</label>
					<input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="groupCode" value="${conditionsMap.groupCode}" type="text" />
				</div>
	            <div class="form_submit">
	                <input class="btn btn-primary" value="搜索" type="submit">
	            </div>
	            <a class="zksx">展开筛选</a>
	                <div class="ydxbd">
	                   	<div class="activitylist_bodyer_right_team_co1">
					   	 	<div class="activitylist_team_co3_text">订单编号：</div>
							<input class="inputTxt" name="orderNum" value="${conditionsMap.orderNum}" />
					 	</div>
					    <div class="activitylist_bodyer_right_team_co1">
				   	 	 	<div class="activitylist_team_co3_text">计调：</div>
							<select name='operatorId'>
								<option value="" selected="selected">全部</option>
								<c:forEach items="${fns:getSaleUserList('3')}" var="userinfo">
									<!-- 用户类型  3 代表计调 -->
									<option value="${userinfo.id }"
										<c:if test="${conditionsMap.operatorId==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
								</c:forEach>
							</select>
					 	</div>
					 	<div class="activitylist_bodyer_right_team_co2">
						 	<label>报批日期：</label><input class="inputTxt dateinput" name="startTime" value="${conditionsMap.startTime}" onfocus="WdatePicker()" readonly="readonly" /> 
						 	<span> 至 </span> 
						 	<input class="inputTxt dateinput" name="endTime" value="${conditionsMap.endTime}" onclick="WdatePicker()" readonly="readonly" />
						</div>
	              		<div class="kong"></div>
	              		<div class="activitylist_bodyer_right_team_co1">
				   			<div class="activitylist_team_co3_text">渠道选择：</div>
							 	<select name="channel">
									<option value="">全部</option>
									<c:if test="${not empty fns:getAgentList() }">
										<c:forEach items="${fns:getAgentList()}" var="agentinfo">
											<option value="${agentinfo.id }"
											<c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}
											</option>
										</c:forEach>
									</c:if>
							 	</select>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">销售：</div>
								<select name="salerId">
								<option value="" selected="selected">全部</option>
								<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
									<!-- 用户类型  1 代表销售 -->
									<option value="${userinfo.id }"
										<c:if test="${conditionsMap.salerId==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
								</c:forEach>
								</select>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">押金金额：</div>
		                    <span class="pr" style="display:inline-block;">
		                   		<input type="text" value="${conditionsMap.moneyBegin }" name="moneyBegin" id="startMoney" class="inputTxt" flag="istips" style="width:70px;" onkeyup="validNum(this)" onafterpaste="validNum(this))"/>
		                   		<span class="ipt-tips ipt-tips2">输入金额</span>
		                    </span> ~
		               	    <span class="pr" style="display:inline-block;">
		               	   		<input type="text" value="${conditionsMap.moneyEnd}" name="moneyEnd" id="endMoney" class="inputTxt" flag="istips" style="width:70px;" onkeyup="validNum(this)" onafterpaste="validNum(this))"/>
		               	   		<span class="ipt-tips ipt-tips2">输入金额</span>
		               	    </span>
						</div>
						<div class="kong"></div>
						<div class="activitylist_bodyer_right_team_co1">
					   	 	<div class="activitylist_team_co3_text">游客：</div>
							<input class="inputTxt" name="travelerName" value="${conditionsMap.travelerName}" />
					 	</div>
					 	<div class="activitylist_bodyer_right_team_co1" >
		                    <div class="activitylist_team_co3_text">下单人：</div>
		                    <select name="orderPersonId">
		                        <option value="">全部</option>
		                        <c:forEach var="order_person" items="${orderPersonList}">
		                            <option value="${order_person.id }" <c:if test="${conditionsMap.orderPersonId==order_person.id}">selected="selected"</c:if>>${order_person.name }</option>
		                        </c:forEach>
		                    </select>
		                </div>
	                </div>
	        </div>
            <div class="activitylist_bodyer_right_team_co_paixu">
             	<div class="activitylist_paixu">
                 	<div class="activitylist_paixu_left">
                    	<ul>
                   			<li style="width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
            				<li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            				<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
                 		</ul>
                    </div>
                    <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                    <div class="kong"></div>
                </div>
            </div>
        </form>
<!--状态开始-->
<div class="supplierLine">
	<!-- 0:全部 1:待本人审核 2:本人审核通过 3:审核中 4:已通过 5:未通过 6:已取消 -->
	<a href="javascript:void(0)" onClick="statusChooses(0);">全部</a> 
	<a href="javascript:void(0)" onClick="statusChooses(1);">待本人审核</a> 
	<a href="javascript:void(0)" onClick="statusChooses(2);">本人审核通过</a>
	<a href="javascript:void(0)" onClick="statusChooses(3);">审核中</a> 
	<a href="javascript:void(0)" onClick="statusChooses(4);">已通过</a>
	<a href="javascript:void(0)" onClick="statusChooses(5);">未通过</a>
	<a href="javascript:void(0)" onClick="statusChooses(6);">已取消</a>
</div>
<!--状态结束-->
<table id="contentTable" class="table activitylist_bodyer_table">
   <thead>
   	   <tr>
           <th width="4%">序号</th>
           <th width="7%">报批日期</th>
           <th width="8%">订单编号</th>
           <th width="6%">渠道</th>
           <th width="5%">游客</th>
           <th width="10%">
           		<span class="tuanhao on">团号</span>
              	/<span class="chanpin">产品名称</span>
           </th>
           <th width="5%">计调</th>
           <th width="5%">
               <span class="order-saler-title on">销售</span>/
               <span class="order-picker-title">下单人</span>
           </th>
           <th width="7%">下单时间</th>
           <th width="7%">订单总额</th>
           <th width="7%">已付金额<br/>到账金额</th>
           <th width="7%">押金金额</th>
           <th width="7%">上一环节<br />审批人</th>
           <th width="6%">审核状态</th>
           <th width="6%">出纳确认</th>
           <th width="8%">操作</th>
       </tr>
   </thead>
   <tbody>
   <c:if test="${fn:length(page.list) <= 0 }">
       <tr class="toptr" >
       		<td colspan="16" style="text-align: center;">暂无搜索结果</td>
       </tr>
   </c:if>
	<c:forEach items="${page.list}" var="visaRefund" varStatus="s">
      	<tr>
          	<td>
          		<c:if test="${conditionsMap.statusChoose eq '1'}">
					<input type="checkbox" name="batchItem" value="${visaRefund.revid}" />
				</c:if>${s.count}
          	</td>
          	<td class="tc"><fmt:formatDate value="${visaRefund.createDate}" pattern="yyyy-MM-dd"/></td>
            <td>${visaRefund.ordercode}</td>
            <td>${fns:getAgentName(visaRefund.agentid)}</td>
            <td>${visaRefund.travelerName}</td>
            <td>
              	<div class="tuanhao_cen onshow">${visaRefund.groupcode}</div>
          		<div class="chanpin_cen qtip" title="JHGJLXS5674--切位">
          		<a href="${ctx}/visa/preorder/visaProductsDetail/${visaRefund.chanpid}" target="_blank">${visaRefund.chanpname}</a>
          		</div>
      		</td>
            <td>
            	<c:if test="${visaRefund.jidcreateby != null && visaRefund.jidcreateby != ''}">
           			${fns:getUserNameById(visaRefund.jidcreateby)}
           		</c:if>
            </td>
            <td>
            	<span class="order-saler onshow">
               		<c:if test="${visaRefund.salerId != null && visaRefund.salerId != ''}">
               			${fns:getUserNameById(visaRefund.salerId)}
               		</c:if>
                </span>
      			<span class="order-picker">
      				<c:if test="${visaRefund.orderPersonId != null && visaRefund.orderPersonId != ''}">
      					${fns:getUserNameById(visaRefund.orderPersonId)}
      				</c:if>
      			</span>
            </td>
            <td class="tc"><fmt:formatDate value="${visaRefund.ordercreatedate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td class="tr-payable">
            	<c:choose>
                    <c:when test="${fns:getMoneyAmountByUUIDOrderType(visaRefund.total_money,6,13,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
                    <c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(visaRefund.total_money,6,13,2)}</span></c:otherwise>
                </c:choose>
            </td>
            <td class="p0 tr">
                <div class="yfje_dd">
                    <c:choose>
                        <c:when test="${fns:getMoneyAmountByUUIDOrderType(visaRefund.payed_money,6,5,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
                        <c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(visaRefund.payed_money,6,5,2)}</span></c:otherwise>
                   	</c:choose>
                </div>
                <div class="dzje_dd">
                    <c:choose>
                        <c:when test="${fns:getMoneyAmountByUUIDOrderType(visaRefund.accounted_money,6,4,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
                        <c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(visaRefund.accounted_money,6,4,2)}</span></c:otherwise>
                   	</c:choose>
                </div>
            </td>
            <td class="tr"><span class="fbold tdorange">${fns:getCurrencyInfo(visaRefund.depositPriceCurrency,0,'mark')}${visaRefund.refundPrice}</span></td>
            <td>${fns:getUserNameById(visaRefund.lastoperator)}</td>
            <td class="invoice_yes">${fns:getNextReview(visaRefund.revid) }</td>
            <td class="invoice_yes">
            	<c:if test="${visaRefund.returnstatus == true}">已付款</c:if>
            	<c:if test="${visaRefund.returnstatus == false || visaRefund.returnstatus == null}">未付款</c:if></td>
            <td class="p0">
                <dl class="handle">
                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                    <dd class="">
                    	<p> <span></span> 
	                    <c:if test="${visaRefund.revLevel == visaRefund.curlevel && visaRefund.revstatus == '1'}">
							<a href="${ctx}/deposite/costDepositeRefundReviewDetail?orderid=${visaRefund.orderid}&revid=${visaRefund.revid}&flag=0&nowlevel=${visaRefund.curlevel}" target="_blank">审批</a><br />
						</c:if>
						<c:if test="${(visaRefund.toplevel==visaRefund.curlevel)&&(visaRefund.returnstatus eq '0' || empty visaRefund.returnstatus)}">
							<a onclick="cashConfirm('${visaRefund.traid}')">出纳确认</a><br />
						</c:if>
						<a href="${ctx}/deposite/depositeRefundReviewDetail?orderid=${visaRefund.orderid}&revid=${visaRefund.revid}&flag=1&nowlevel=${visaRefund.curlevel}" target="_blank">查看</a>
						<c:if test="${fns:getUser().id == visaRefund.lastoperator 
							&& visaRefund.revstatus == 1 && visaRefund.curlevel != 1}">
							<a href="javascript:void(0)" onclick="cancelDeposite('${visaRefund.revid}','${visaRefund.revLevel}')">撤销</a>
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
	<c:if test="${conditionsMap.statusChoose eq '1'}">
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
