<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收据记录</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" /> -->
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">
  //撤销操作弹出层脚本
function jbox__shoukuanqueren_chexiao_fab() {
    $.jBox($("#rejected_cancel_confirms").html(), {
        title: "系统提示", buttons: { '取消': 1,'确定': 2 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '597', width: 300
    });
    inquiryCheckBOX();
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
$(function(){
inputTips();
//展开筛选按钮
  		$('.zksx').click(function(){
		if($('.ydxbd').is(":hidden")==true)
		{
			$('.ydxbd').show();
			$(this).text('收起筛选');
			$(this).addClass('zksx-on');
		}else
		{
			$('.ydxbd').hide();
			$(this).text('展开筛选');
			$(this).removeClass('zksx-on');
		}
	});
	//如果展开部分有查询条件的话，默认展开，否则收起	
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
	var searchFormselect = $("#searchForm").find("select[id!='orderShowType']");
	var inputRequest = false;
	var selectRequest = false;
	for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" && 
				$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if($("#orderShowType").children("option:selected").val() != "0") {
		selectRequest = true;
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
	var _$orderBy = $("#orderBy").val();
	if(_$orderBy==""){
	    _$orderBy="createDate DESC";
	    $("#orderBy").val(_$orderBy);
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
	    if ($(this).hasClass("li"+orderBy[0])){
	        orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
	        $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
	        $(this).attr("class","activitylist_paixu_moren");
	    }
	});
		//条件重置
	var resetSearchParams = function(){
    $(':input','#searchForm')
     .not(':button, :submit, :reset, :hidden')
     .val('')
     .removeAttr('checked')
     .removeAttr('selected');
	}
	launch();	
		$('.handle').hover(function() {
				if(0 != $(this).find('a').length){
					$(this).addClass('handle-on');
					$(this).find('dd').addClass('block');
		    	}
			},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});		
	     //产品名称获得焦点显示隐藏
			$("#wholeSalerKey").focusin(function(){
				var obj_this = $(this);
					obj_this.next("span").hide();
			}).focusout(function(){
				var obj_this = $(this);
				if(obj_this.val()!="") {
				obj_this.next("span").hide();
			}else
				obj_this.next("span").show();
			});
			if($("#wholeSalerKey").val()!="") {
				$("#wholeSalerKey").next("span").hide();
			}

});


function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_data,payMode_guarantee,payMode_express,showType) {
    if($(child).is(":hidden")){
        if (payMode_deposit != "") {
        	//暂时取消，币种，订单金额，计调人员的查询
        	var currencyId = "";//$("#c_cny").val();
        	var orderMoneyBegin = "";//$("#m_order_money_begin").val();
        	var orderMoneyEnd = "";//$("#m_order_money_end").val();
        	var operator = "";//$("#p_operator").val();
            $.ajax({
            	type : "post",
				url : "${ctx}/receipt/limit/supplyviewdetailopen/" + payMode_deposit + "?1=1&currencyId="
						              + currencyId+"&orderMoneyBegin=" +orderMoneyBegin + "&orderMoneyEnd=" 
						              + orderMoneyEnd + "&operator=" + operator,
				success : function(msg) {
					if(msg.length > 0){
						var record = '';
						for(var i = 0; i < msg.length; i++) {
							if(msg[i][6] == null)
								msg[i][6] = "";
							if(msg[i][7] == null)
								msg[i][7] = "";
							if(msg[i][8] == null)
								msg[i][8] = "";
							if(msg[i][9] == null)
								msg[i][9] = "";
							record += "<tr>"
							+"<td>" + (i+1) + "</td>"+
							"<td>" + msg[i][0]+ "</td>"
							+"<td>" + msg[i][2] + "</td>"
							+"<td>" + msg[i][1] + "</td>"
							+"<td class='tc'>" + msg[i][3] +"</td>"
							+"<td class='tr'>" + msg[i][19] + "</td>"
							+"<td>" + msg[i][4] + "</td>"
							+"<td>" + msg[i][5] + "</td>"
							+"<td class='tr'>" + msg[i][14] + "</td>"
							+"<td class='tr'>" + msg[i][16]+ "</td>"
							+"<td class='tr'>" + msg[i][17] + "</td>"
							+"<td class='tr'>" + msg[i][18] + "</td>"
							+"<td class='tr'>" + msg[i][11] + "</td></tr>";
						}
						$(child).find("td table tbody").html(record);
                	}else {
                		$(child).find("td table tbody").html('<tr><td colspan="13" class="tc">暂无数据</td></tr>');
                	}
					$(obj).html("关闭订单列表");
					$(child).show();
					$(obj).parents("td").addClass("td-extend");
				}
            });
        }else{
        	$(obj).html("关闭订单列表");
            $(child).show();
            $(obj).parents("td").addClass("td-extend");
        }
    }else{
        if(!$(child).is(":hidden")){
            $(child).hide();
            $(obj).parents("td").removeClass("td-extend");
            $(obj).html("展开订单列表");
        }
    }
}



	$(function(){
		$('.team_a_click').toggle(function(){
			$(this).addClass('team_a_click2');
		},function(){
			$(this).removeClass('team_a_click2');
		});	

	 });
	 //撤销收据
 function revokeToUncheck(invoiceNum){
	var submit = function (v, h, f) {
	    if (v == 'ok') {
	    	$.jBox.tip("正在撤销数据...", 'loading');
	    	$.ajax({ 
		          type:"POST",
		          url:"${ctx}/receipt/limit/revokeToUncheck/" + invoiceNum,
		          dataType:"json",
		          data:{},
		          success:function(data){
		        	if('success' == data.flag){
		        		$.jBox.tip(data.msg, 'success');
		        		$("#searchForm").submit();
		        	}else if('fail' == data.flag){
		        		$.jBox.tip(data.msg, 'fail');
		        	}
		          }
		      });
	    }
	    return true; //close
	};
	$.jBox.confirm("确定要撤销该收据吗？", "提示", submit);
}
//明细页
function viewdetail(invoiceNum,verifyStatus,orderType){
	verifyStatus = '-2';
	window.open("${ctx}/receipt/limit/supplyviewrecorddetail/" + invoiceNum + "/" + verifyStatus + "/" + orderType);
}






function applyInvoice(orderNum,orderId,orderType){
    $.ajax({
    	type : "post",
		url : "${ctx}/orderInvoice/manage/validateOrder",
		data:{
				orderNum : orderNum,
				orderId: orderId,
				orderType:orderType
		},
		success : function(msg) 
		{
			if("success"== msg.msg)
				window.location.href ="${ctx}/orderReceipt/manage/applyReceipt?orderNum=${orderNum}&orderId=${orderId}&orderType=${orderType}";
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开收据！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！','success'); 
		}
    });
	
}






</script>
</head>
<body>
<div class="mod_nav">订单 > 
<c:if test="${orderType==1 }">单团</c:if>
<c:if test="${orderType==2 }">散拼</c:if>
<c:if test="${orderType==3 }">游学</c:if>
<c:if test="${orderType==4 }">大客户</c:if>
<c:if test="${orderType==5 }">自由行</c:if>
<c:if test="${orderType==6 }">签证</c:if>
<c:if test="${orderType==7 }">机票</c:if>
<c:if test="${orderType==10 }">游轮</c:if>
<c:if test="${orderType==11 }">海岛</c:if>
<c:if test="${orderType==12 }">酒店</c:if>
 > 收据记录</div>

<div class="ydbz_tit orderdetails_titpr" id="invoiceApply">收据记录<c:if test="${applySign ==1 }"> </c:if>
<c:if test="${lockStatus != 1}">
	<a class="ydbz_x" href="javascript:applyInvoice('${orderNum}',${orderId},${orderType})">申请收据</a>
</c:if>
</div>
      <!--右侧内容部分开始-->
    
        <table class="activitylist_bodyer_table" id="contentTable">
          <thead>
            <tr>
              <th width="4%">序号</th>
              <th width="7%">收据类型</th>
              <th width="7%">开收据客户</th>
              <th width="8%">申请日期</th>
              <th width="8%">开收据日期</th>
              <th width="6%">申请人</th>
              <th width="7%">收据金额</th>
              <th width="6%">收据状态</th>
              <th width="6%">审核状态</th>
              <th width="6%">备注</th>
              <th width="10%">操作</th>
            </tr>
          </thead>
          <tbody>
          <%
				int n = 1;
			%>
          <c:forEach items="${page}" var="receiptInfo" varStatus="s">
            <tr>
              <td><%=n++%></td>
              <td><c:forEach var="invoice" items="${invoiceTypes }">
	          	<c:if test="${invoice.value==receiptInfo.invoiceType}">${invoice.label }</c:if>
	          </c:forEach></td>
              <td>${receiptInfo.invoiceCustomer}</td>
              <td class="tc"><fmt:formatDate value="${receiptInfo.createDate}" pattern="yyyy-MM-dd HH:mm"/></td>
              <c:if test="${receiptInfo['verifyStatus'] eq  '1'}">
              <td class="tc"><fmt:formatDate value="${receiptInfo.updateDate}" pattern="yyyy-MM-dd HH:mm"/></td>
              </c:if>
              
              <c:if test="${receiptInfo['verifyStatus'] ne  '1'}">
              <td class="tc"> </td>
              </c:if>
              
              
              <td class="tc">${receiptInfo.createName}</td>
              <td class="tr"><span class="fbold tdorange">${receiptInfo.invoiceAmount}</span></td>
              <c:if test="${receiptInfo['createStatus'] eq '0'}">
					<td class="invoice_no">待开</td>
			  </c:if>
			  <c:if test="${receiptInfo['createStatus'] eq  '1'}">
					<td class="invoice_yes">已开</td>
			  </c:if>
			  <c:if test="${receiptInfo['createStatus'] eq  '2'}">
					<td class="invoice_yes">已还</td>
			  </c:if>
			  
			  <!--开收据状态-->
              <c:if test="${receiptInfo['verifyStatus'] eq '0'}">
					<td class="invoice_no">未审核</td>
				</c:if>
				<c:if test="${receiptInfo['verifyStatus'] eq  '1'}">
					<td class="invoice_yes">审核通过</td>
				</c:if>
				<c:if test="${receiptInfo['verifyStatus'] eq  '2'}">
					<td class="invoice_back">被驳回</td>
				</c:if>
				<!--审核状态-->
         
              <td class="tc">${receiptInfo.remarks}</td>
              <td class="tc tda">
              <a href="javascript:void(0)" onClick="viewdetail('${receiptInfo['uuid']}','${param.verifyStatus}','${receiptInfo['orderType']}')">详情</a> <br />
			<a class="team_a_click" href="javascript:void(0)" onclick="expand('#child${s.count}',this,'${s.count}','${receiptInfo['uuid']}')">展开订单列表</a></td>
            </tr>
             <tr id="child${s.count}" style="display: none;" class="activity_team_top1_old">
              <td colspan="15" class="team_top">
              <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                  <thead>
                    <tr>
                      <th width="3%">序号</th>
                      <th width="7%">订单号</th>
                      <th width="7%">开收据客户</th>
                      <th width="6%"><c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">订单团号</c:if><c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}">团号</c:if></th>
                      <th width="7%">销售</th>
                      <th width="7%">达账金额</th>
                      <th width="7%">下单时间</th>
                      <th width="3%">人数</th>

                      <th width="7%">应收金额</th>
                      <th width="7%">已开发票金额</th>
                      <th width="7%">退款金额</th>
                      <th width="7%">已开收据金额</th>
                      <th width="12%">收据金额</th>
                    </tr>
                  </thead>
                  <tbody>
<!--                     <tr> -->
<!--                       <td>1</td> -->
<!--                       <td>dt090404001</td> -->
<!--                       <td>mj001223</td> -->
<!--                       <td class="tc">mj001223</td> -->
<!--                       <td class="tc">张珊</td> -->
<!--                       <td class="tc">张珊</td> -->
<!--                       <td class="tc">2014-10-23</td> -->
<!--                       <td>5</td> -->
<!--                       <td style="padding: 0px;" class="tc"><div class="out-date"> 2014-11-16 </div> -->
<!--                         <div class="close-date"> 2014-11-13 </div></td> -->
<!--                       <td class="tr">¥<span class="tdred">1,000</span></td> -->
<!--                       <td class="tr">¥<span class="tdred">1,000</span></td> -->
<!--                       <td class="tr">¥<span class="tdred">1,000</span></td> -->
<!--                       <td class="tr">¥<span class="tdred">1,000</span></td> -->
<!--                       <td class="tr">¥<span class="tdred">1,000</span></td> -->
<!--                       <td class="tc">已开</td> -->
<!--                     </tr> -->
                  </tbody>
                </table>
                </td>
            </tr>
            </c:forEach>
                </tbody>
              </table>
       
      <!--右侧内容部分结束--> 
  <!--撤销弹出层开始-->
  <!-- <div  id="rejected_cancel_confirms">
    <div style="margin:10px;min-height:30px;height:auto;padding-left:40px;text-align:left;"><span class="jbox-icon jbox-icon-question" style="position:absolute; top:55px;left:15px; width:32px; height:32px;"></span>确认撤销吗？</div>
  </div> -->

</body>
</html>
