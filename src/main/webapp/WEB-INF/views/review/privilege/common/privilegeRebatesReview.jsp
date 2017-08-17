<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>审批>优惠审批 >审批</title>
<meta name="decorator" content="wholesaler"/>
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
</script>
<script type="text/javascript">
	function jbox_bohui(rid){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/privilegeReview/review",
		            data: {
			            reviewId: rid,
			            result: 0,
			            denyReason: $("#denyReason").val()		            
		            },
		            success: function(data){
		            	if ("success" == data.result) {
		            		$("input[name='review']").attr('disabled',"true");
	           				$("input[name='bohui']").attr('disabled',"true");
	           				window.opener.location.href = window.opener.location.href;
		            		window.close();
		            	} else {
		            		jBox.tip("操作失败");
		            	}
		            }
		        });
			}
		},height:250,width:500});
	}
	function reviewPass(rid){
		var html = '<div class="add_allactivity"><label>请填写您的备注!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/privilegeReview/review",
		            data: {
			            reviewId: rid,
			            result: 1,
			            denyReason: $("#denyReason").val()		            
		            },
		            success: function(data){
		            	if ("success" == data.result) {
		            		$("input[name='review']").attr('disabled',"true");
	           				$("input[name='bohui']").attr('disabled',"true");
	           				window.opener.location.href = window.opener.location.href;
		            		window.close();
		            	} else {
		            		jBox.tip("操作失败");
		            	}
		            }
		        });
			}
		},height:250,width:500});
	}
</script>
</head>
<body>
<!--右侧内容部分开始-->
	<input id="bzJson"  type="hidden"  value='${bzJson}'>
	<div class="mod_nav">审批  &gt;优惠审批 </div>
		<%@ include file="/WEB-INF/views/review/borrowing/singlegroup/singlegrouporderBaseInfo.jsp"%>
	<div class="ydbz_tit">特殊需求</div>
	<div class="orderdetails3">
		<div class="ydbz2_lxr">
			<p><label>特殊需求：</label><span class="tj-hbbz-ys">${productOrder.specialDemand }</span></p>
		</div>
	</div>
	<div class="ydbz_tit">预订人信息</div>
	<div class="orderdetails4">
		<p class="ydbz_qdmc">预订渠道：${orderCompany} 	&nbsp;&nbsp; ${fns:getUserNameById(userName)}</p>
	</div>
	<div class="ydbz_tit"> 
		 <span class="fl">游客优惠</span><span class="fr wpr20">报批日期：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${rebates.createDate}"/></span>
	</div>
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
					<td class="tr"><input type="hidden" data="${rdlist.get('currencyId') }"  name="txjsPrice" value="${rdlist['thjsjinputprice'] }" > ${fns:getCurrencyNameOrFlag(rdlist.get('currencyId'),'0')}<span class=" "   name="txjsPrice"><fmt:formatNumber   type="currency" value="${rdlist['thjsjinputprice'] }" pattern="#,##0.00"/></span></td>
					<td class="tr"><input type="hidden" data="${rdlist.get('currencyId') }"  name="dicount" value="${rdlist['sqyhPrice'] }"> ${fns:getCurrencyNameOrFlag(rdlist.get('currencyId'),'0')}<span class=" "><fmt:formatNumber  type="currency" value="${rdlist['sqyhPrice'] }" pattern="#,##0.00"/></span></td>
					<td class="tl">${rdlist['privilegeReason'] }</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2" class="tl">总价</td>
				<%--同行价--%>
				<td class="tr"><span class="fbold tdred" name="txtotalprice"></span></td>
				<%--优惠额度--%>
				<td class="tr"><span class="fbold tdred" name="yhtotalprice"></span></td>
				<%--同行结算价--%>
				<td class="tr"><span class="fbold tdred" name="txjstotalprice"></span></td>
				<%--申请优惠金额--%>
				<td class="tr"><span class="fbold tdred" name="sqyhtotalprice"></span></td>
				<%--空--%>
				<td class="tl"></td>
			</tr>
		</tbody>
	</table>
	<div class="ydbz_tit">
		<span class="fl">审批动态</span>
	</div>
	<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">关闭</a>
		<input type="button" name="bohui" value="驳回" class="btn btn-primary" onclick="jbox_bohui('${rebates.id}');">
		<input type="button" name="review" value="审批通过" class="btn btn-primary" onclick="reviewPass('${rebates.id}');">
	</div>
<!--右侧内容部分结束-->
</body>
</html>