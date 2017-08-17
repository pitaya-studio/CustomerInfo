<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>审核-返佣审核</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
	function jbox_bohui(rid,userLevel){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/order/rebates/review/reviewRebates",
		            data: {
			            rid: rid,
			            result: 0,
			            userLevel: userLevel,
			            denyReason: $("#denyReason").val()		            
		            },
		            success: function(msg){
		            	if("success" == msg){
		            		$("input[name='review']").attr('disabled',"true");
	           				$("input[name='bohui']").attr('disabled',"true");
	           				window.opener.location.href = window.opener.location.href;
		            		window.close();
		            	}else{
		            		jBox.tip("操作失败");
		            	}
		            }
		        });
			}
		},height:250,width:500});
	}
	function reviewPass(rid,userLevel){
		$.ajax({
           type: "POST",
           url: "${ctx}/order/rebates/review/reviewRebates",
           data: {
	           rid: rid,
	           result: 1,
	           userLevel: userLevel,
	           denyReason: ""		            
           },
           success: function(msg){
	           	if("success" == msg){
	           		$("input[name='review']").attr('disabled',"true");
	           		$("input[name='bohui']").attr('disabled',"true");
	           		jBox.tip("操作成功");
	           		window.opener.location.href = window.opener.location.href;
	           		window.close();
	           	}else{
	           		jBox.tip("操作失败");
	           	}
           }
       });
	}
	</script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
	    <page:param name="desc">返佣审批</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
    <div class="mod_nav">审核 > 返佣申请 > 返佣审批</div>
	<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
	<div class="ydbz_tit"><span class="fl"><c:if test="${not empty rebates.traveler}">个人</c:if><c:if test="${empty rebates.traveler}">团队</c:if>返佣</span></div>
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
			 <th width="12%">预计返佣金额</th>
			 <th width="12%">原返佣金额</th>
			 <th width="13%">返佣差额</th>
			 <th width="20%">备注</th>
<!-- 	         <th width="13%">改后返佣金额</th> -->
		  </tr>
	  	</thead>
	   	<tbody>
		  <tr>
			 <td rowspan="2"><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if></td>
             <td>${rebates.currency.currencyName}</td>
			 <td class="tr">${rebates.costname}</td>
			 <td class="tr">
			 	<c:if test="${not empty rebatesStr}">${rebatesStr}</c:if>
			 	<c:if test="${empty rebatesStr}">-</c:if>
			 </td>
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.oldRebates,2)}</td>
             <td class="tr">${rebates.currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></td>
			 <td>${rebates.remark}</td>
<!-- 			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.newRebates,2)}</td> -->
		  </tr>
	   </tbody>
	</table>
	<div class="allzj tr f18">
      	原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      	返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
<!-- 		<div class="all-money">改后返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.newRebates)}</div> -->
	</div>
	<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">关闭</a>
		<input type="button" name="bohui" value="驳回" class="btn btn-primary" onclick="jbox_bohui(${rebates.review.id},${userLevel});">
		<input type="button" name="review" value="审核通过" class="btn btn-primary" onclick="reviewPass(${rebates.review.id},${userLevel});">
	</div>
	<!--右侧内容部分结束-->
</body>
</html>