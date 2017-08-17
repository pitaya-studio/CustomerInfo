<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>优惠申请</title>
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
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function(){
			if($(this).attr("checked")){

			}else{
				$("input[name='allChk']").removeAttr("checked");
			}
		});
		
		//同行价总计
		var jsPriceTotal = new Array();
		$("#contentTable").find("span[name='txPrice']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).text();
			jsPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(jsPriceTotal);
        $("span[name='txtotalprice']").text(tJsPrice);
        $("input[name='inputtxtotalprice']").val(tJsPrice);
		
    	//同行价结算价总计
		var jsPriceTotal = new Array();
		$("#contentTable").find("span[name='txjsPrice']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).text();
			jsPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(jsPriceTotal);
		$("input[name='inputtxjstotalprice']").val(tJsPrice)
        $("span[name='txjstotalprice']").text(tJsPrice)
        
        
        //优惠价总计
		var yhtotalprice = new Array();
		$("#contentTable").find("span[name='yhPrice']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).text();
			yhtotalprice.push(obj);
		});
		var yhPrice=getbzStr(yhtotalprice);
		$("input[name='inputyhtotalprice']").val(yhPrice)
        $("span[name='yhtotalprice']").text(yhPrice)
        
        
	});

	//申请优惠金额总计
	function sjyhTotalPrice(){
		var jsPriceTotal = new Array();
		$("#contentTable").find("input[name='dicount']").each(function(){
			var obj = new Object();
			obj.currencyId = $(this).attr("data");
			obj.je = $(this).val();
			jsPriceTotal.push(obj);
		});
		var tJsPrice=getbzStr(jsPriceTotal);
		var noMark = getbzStrNoMark(jsPriceTotal);
		if(tJsPrice.indexOf("NaN")!=-1){
	       $("span[name='sqyhtotalprice']").text("");
	       $("input[name='inputsqyhtotalprice']").val("");
	       $("input[name='inputsqyhtotalpriceNoMark']").val("");
		}else{
	       $("span[name='sqyhtotalprice']").text(tJsPrice);
	       $("input[name='inputsqyhtotalprice']").val(tJsPrice);
	       $("input[name='inputsqyhtotalpriceNoMark']").val(noMark);
		}
 
	}
	
	//全选&反选操作
	function checkall(obj){
		if($(obj).attr("checked")){
			$('#contentTable input[type="checkbox"]').attr("checked",'true');
			$("input[name='allChk']").attr("checked",'true');
			$("input[name='dicount']").attr("readonly",false);
		    $("input[name='privilegereason']").attr("readonly",false);
		}else{
			$('#contentTable input[type="checkbox"]').removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
			$("input[name='dicount']").attr("readonly",true);
		    $("input[name='privilegereason']").attr("readonly",true);
		    $("input[name='dicount']").val("");
		    $("input[name='privilegereason']").val("");
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
	
	//判断只读
	function inputRead(){
		$("#contentTable > tbody > tr ").each(function(i,n){
		    if($(this).find("[name='ids']",n).attr("checked")){
		       $(this).find("input[name='dicount']",n).attr("readonly",false);
		       $(this).find("input[name='privilegereason']",n).attr("readonly",false);
		    }else{
		       $(this).find("input[name='dicount']",n).attr("readonly",true);
		       $(this).find("input[name='privilegereason']",n).attr("readonly",true);
		       $(this).find("input[name='dicount']",n).val("");
		       $(this).find("input[name='privilegereason']",n).val("");
		    }
		})
		sjyhTotalPrice();
	}
	
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
	
	
	/**
	 * 多币种拼接显示
	 */
	function getbzStrNoMark(jsarray){
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
	            bzpjstr+=element.je.toString().formatNumberMoney('#,##0.00')+"+";
	    });
	    return bzpjstr.substring(0,bzpjstr.length-1);
	}
	
	
	
	//申请优惠
	function submitPrivelege(){
		
		var orderId = $("#orderId").val();
		var str = "";
		var checkFlag = false;
		var checkPrice = true;
		var traStr = new Array();
		var n = 0;
		var num = 0;
		$("input[name='ids']").each(function() {
			if (this.checked == true || this.checked == "checked") {
				str += "1" + ",";
				traStr[n] = $("input[name='ids']")[num].value;
				n = n + 1;
				checkFlag = true;
			} else {
				str += "0,";
			}
			num = num + 1;
		});
		if (!checkFlag) {
			$.jBox.tip("请选择优惠游客!", "warning");
			return;
		}
		$("#contentTable > tbody > tr ").each(function(i,n){
		    if($(this).find("[name='ids']",n).attr("checked")){
				if($(this).find("input[name='dicount']",n).val()==""){
					checkPrice = false;
				}
		    }
		})
		
		if(!checkPrice){
			$.jBox.tip("请填写价格!", "warning");
			return;	
		}
		$.ajax({
			type : "POST",
			url  : g_context_url + "/singlegroup/privilege/privilegeApply",
			dataType :"json",
			data : $.param({'ids':str,'token':$("#token").val()}) + '&' + $('#searchForm').serialize(),
			async: false,
			success:function(data){
				$.jBox.tip(data.result, "warning");
				document.location = "${ctx}/singlegroup/privilege/privilegeApplyList?orderId="+orderId;
			}
		});
		
	}
	
	//全部还原
	function clearAll(){
		$("[name=ids]").attr("checked",false);
		$("[name=allChk]").attr("checked",false);
		$("input[name='dicount']").val("");
		$("input[name='privilegereason']").val("");
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
		<div class="ydbz_tit"> <span class="fl">游客优惠</span><span class="fr wpr20"> </span> </div>
		<!--S--C109需求优惠审批表格-->
		<form method="post" id="searchForm" >
		<input id="token"        name="token"        value="${token}"         type="hidden" />
		<input id="orderId"      name="orderId"      value="${orderId }" 	  type="hidden">
		<input id="bzJson"       type="hidden"             value='${bzJson}' >
<!-- 	<input type="hidden" value="${reviewId }" name="reviewId" id="reviewId" data-type="${reviewObj.currencyId }"> -->
		<table id="contentTable" class="table activitylist_bodyer_table discount-tourist  apply-for-tourist-fee-pop">
			<thead>
			<tr>
				<th class="tc" width="4%">
					全选<br>
					<input name="allChk" onclick="checkall(this)" type="checkbox">
				</th>
				<th width="8%">姓名</th>
				<th width="5%">游客类型</th>
				<th width="8%">同行价</th>
				<th width="7%">优惠额度</th>
				<th width="6%">同行结算价</th>
				<th width="7%">申请优惠金额</th>
				<th width="40%">备注</th>
			</tr>
			</thead>
			<tbody>
				
				<c:forEach items="${travelerList }" var="travelerList">
						<tr>
							<td class="tc">
								<input type="checkbox" name="ids" value=""  onclick="inputRead();"/>
								<input type="hidden" name="travelerId"  value="${travelerList.get('id') }">
							</td>
							<td class="tc">${travelerList.get('name') } <input type="hidden"  name="travelerName" value="${travelerList.get('name') }"> </td>
							<td class="tc">
								<input type="hidden" name="travelerType" value="${travelerList.get('personType')}">
								<c:choose>
									<c:when test="${travelerList.get('personType') eq '1'}">成人</c:when>
									<c:when test="${travelerList.get('personType') eq '2'}">儿童</c:when>
									<c:otherwise>特殊人群</c:otherwise>
								</c:choose>
							</td>
							<td class="tr">${fns:getCurrencyNameOrFlag(travelerList.get('srcPriceCurrency'),'0')}  <input name="currencyIds" type="hidden" value="${travelerList.get('srcPriceCurrency') }"> <input name="thPrice" type="hidden" value="${travelerList.get('srcPrice') }"> <span class="fbold tdred" data="${travelerList.get('srcPriceCurrency') }" name = "txPrice">${travelerList.get('srcPrice') }</span></td>
							<td class="tr">${fns:getCurrencyNameOrFlag(travelerList.get('srcPriceCurrency'),'0')}  <span class="fbold tdred" data="${travelerList.get('srcPriceCurrency') }" name = "yhPrice"> ${travelerList.get('org_discount_price') }   </span><input name="inpuryhprice"  value="${travelerList.get('org_discount_price') }" type="hidden"> </td>
							<td class="tr">${fns:getCurrencyNameOrFlag(travelerList.get('srcPriceCurrency'),'0')}  <span class="fbold tdred" data="${travelerList.get('srcPriceCurrency') }" name = "txjsPrice">${travelerList.get('srcPrice')-travelerList.get('reviewed_discount_price') }</span><input name="thjsjinputprice" type="hidden" value="${travelerList.get('srcPrice')-travelerList.get('reviewed_discount_price') }"> </td>
							<td class="">
								${fns:getCurrencyNameOrFlag(travelerList.get('srcPriceCurrency'),'0')}
								<input type="text" data="${travelerList.get('srcPriceCurrency') }" name="dicount" readonly="readonly" value=""  maxlength="9"  onblur="sjyhTotalPrice();"  onkeyup="sjyhTotalPrice();this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
							</td>
							<td class="tl remark">
								<input name="privilegereason" maxlength="200" readonly="readonly" type="text"/>
	<!-- 							<textarea rows="20" cols="" style="width: 657px; height: 17px;" name="privilegereason"></textarea> -->
							</td>
						</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="3" class="tl">总价</td>
					<td class="tr"><input type="hidden" name="inputtxtotalprice" value=""> <span class="fbold tdred" name="txtotalprice"></span></td>
					<td class="tr"><input type="hidden" name="inputyhtotalprice" value=""><span class="fbold tdred"  name="yhtotalprice"></span></td>
					<td class="tr"><input type="hidden" name="inputtxjstotalprice" value=""><span class="fbold tdred" name="txjstotalprice"></span></td>
					<td class="tr">
						<input type="hidden" name="inputsqyhtotalprice" value="">
						<input type="hidden" name="inputsqyhtotalpriceNoMark" value="">
						<span class="fbold tdred" name="sqyhtotalprice"></span>
					</td>
					
					<td class="tl"></td>
				</tr>
			</tfoot>
		</table>
		<!--E--C109需求优惠审批表格-->
		<!--S--分页-->
		<div class="page">
			<div class="pagination">
				<dl>
					<dd>
						<a onclick="clearAll();">全部还原</a>
					</dd>
				</dl>
			</div>
		</div>
		<div class="dbaniu">
					<a class="ydbz_s gray" href="/a/changePrice/changePriceReviewList">取消</a>
					<input class="ydbz_s" type="button" id="revFailBtn" onclick="submitPrivelege()" value="申请优惠">
		</div>
	</form>
	<!--右侧内容部分结束-->
</body>
</html>