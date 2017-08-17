<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>优惠详情</title>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		
		g_context_url = "${ctx}";
		//计算同行价
		var txtotalprice = 0; 
		//计算优惠额度
		var yhtotalprice = 0;
		//计算同行结算价
		var txjstotalprice = 0;
		//申请优惠金额
		var sqyhtotalprice = 0;
		
		//同行价总计
		var jsPriceTotal = new Array();
		$("#contentTable").find("input[name='txPrice']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).val();
			jsPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(jsPriceTotal);
        $("span[name='txtotalprice']").text(tJsPrice);
        
        
		//优惠总计
		var yhPriceTotal = new Array();
		$("#contentTable").find("input[name='yhprice']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).val();
			yhPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(yhPriceTotal);
        $("span[name='yhtotalprice']").text(tJsPrice);
        
		
    	//同行价结算价总计
		var jsPriceTotal = new Array();
		$("#contentTable").find("input[name='txjsPrice']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).val();
			jsPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(jsPriceTotal);
        $("span[name='txjstotalprice']").text(tJsPrice)
        
        
        //申请优惠金额总计
        var jsPriceTotal = new Array();
		$("#contentTable").find("input[name='dicount']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).val();
			jsPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(jsPriceTotal);
	    $("span[name='sqyhtotalprice']").text(tJsPrice);
        
	});

	/**
	 * 多币种拼接显示
	 */
	function getbzStr(jsarray){
	    var bzpjstr="";
	    var json=eval($("#bzJson").val());
	    $.each(json,function(i,element){
	        for(var i=0;i<jsarray.length;i++){
	            if(element.currencyId==jsarray[i].currencyId){
	                element.je=Number(element.je)+Number(jsarray[i].je);
	            }
	        }
	    });
	    $.each(json,function(i,element){
	        if(element.je!=0)
	            bzpjstr+=element.currencyMark+element.je.toString().formatNumberMoney('#,##0.00')+"+";
	    });
	    return bzpjstr.substring(0,bzpjstr.length-1);
	}
	
	function wclose(){
		
		window.close();
	}
	
</script>
</head>
<body>
<!--右侧内容部分开始-->
		<div class="mod_nav">订单 &gt; 散拼</div>
		<%@ include file="/WEB-INF/views/review/borrowing/singlegroup/singlegrouporderBaseInfo.jsp"%>
	
		<!--参团结束-->
		<div class="ydbz_tit">特殊需求</div>
		<div class="orderdetails3">
			<div class="ydbz2_lxr">
				<p>
					<label>特殊需求：</label>
					<span class="tj-hbbz-ys">${productOrder.specialDemand }</span> </p>
			</div>
		</div>
		<div class="ydbz_tit">预订人信息</div>
		<div class="orderdetails4">
			<p class="ydbz_qdmc">预订渠道：${orderCompany} 	&nbsp;&nbsp; ${fns:getUserNameById(userName)}</p>
		</div>
		<div class="ydbz_tit"> <span class="fl">游客优惠</span><span class="fr wpr20">报批日期：2015-06-04 16:53:05 </span> </div>
		<!--S--C109需求优惠审批表格-->
		<form method="post" id="searchForm" >
		<input id="token"        name="token"        value="${token}"         type="hidden" />
		<input id="orderId"      name="orderId"      value="${orderId }" 	  type="hidden">
		<input id="bzJson"       type="hidden"             value='${bzJson}' >
<!-- 	<input type="hidden" value="${reviewId }" name="reviewId" id="reviewId" data-type="${reviewObj.currencyId }"> -->
		<table id="contentTable" class="table activitylist_bodyer_table discount-tourist  apply-for-tourist-fee-pop">
			<thead>
			<tr>
				<th width="8%">姓名</th>
				<th width="5%">游客类型</th>
				<th width="8%">同行价</th>
				<th width="7%">优惠额度</th>
				<th width="6%">同行结算价</th>
				<th width="7%">申请优惠金额</th>
				<th width="50%">备注</th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${reviewDataList }" var="rdlist">
					<tr>
							<td class="tc">${rdlist['travellerName'] }</td>
							<td class="tc">
								<c:choose>
									<c:when test="${rdlist.get('travelerType') eq '1'}">成人</c:when>
									<c:when test="${rdlist.get('travelerType') eq '2'}">儿童</c:when>
									<c:otherwise>特殊人群</c:otherwise>
								</c:choose>
							</td>
							<td class="tr"><input type="hidden" data="${rdlist.get('currencyId') }"  name="txPrice" value="${rdlist['thPrice'] }" > ${fns:getCurrencyNameOrFlag(rdlist.get('currencyId'),'0')}<span class=" "   name="txPrice"><fmt:formatNumber  type="currency" value="${rdlist['thPrice'] }" pattern="#,##0.00"/></span></td>
							<td class="tr"><input type="hidden" data="${rdlist.get('currencyId') }"  name="yhprice" value="${rdlist['inpuryhprice'] }" > ${fns:getCurrencyNameOrFlag(rdlist.get('currencyId'),'0')} <span class="yhPrice"><fmt:formatNumber  type="currency" value="${rdlist['inpuryhprice'] }" pattern="#,##0.00"/></span></td>
							<td class="tr"><input type="hidden" data="${rdlist.get('currencyId') }"  name="txjsPrice" value="${rdlist['thPrice'] - rdlist['inpuryhprice'] }" > ${fns:getCurrencyNameOrFlag(rdlist.get('currencyId'),'0')}<span class=" "   name="txjsPrice"><fmt:formatNumber   type="currency" value="${rdlist['thPrice'] - rdlist['inpuryhprice'] }" pattern="#,##0.00"/></span></td>
							<td class="tr"><input type="hidden" data="${rdlist.get('currencyId') }"  name="dicount" value="${rdlist['sqyhPrice'] }"> ${fns:getCurrencyNameOrFlag(rdlist.get('currencyId'),'0')}<span class=" "><fmt:formatNumber  type="currency" value="${rdlist['sqyhPrice'] }" pattern="#,##0.00"/></span></td>
							<td class="tl">${rdlist['privilegeReason'] }</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="2" class="tl">总价</td>
					<td class="tr"><span class="fbold tdred" name="txtotalprice"></span></td>
					<td class="tr"><span class="fbold tdred" name="yhtotalprice"></span></td>
					<td class="tr"><span class="fbold tdred" name="txjstotalprice"></span></td>
					<td class="tr"><span class="fbold tdred" name="sqyhtotalprice"></span></td>
					<td class="tl"></td>
				</tr>
			</tbody>
		</table>
		<!--E--C109需求优惠审批表格-->
		<!--S--分页-->
		<div class="dbaniu">
					<input type="button" class="ydbz_s gray" onclick="wclose();" value="关闭"> 
		</div>
	</form>
	<!--右侧内容部分结束-->
</body>
</html>