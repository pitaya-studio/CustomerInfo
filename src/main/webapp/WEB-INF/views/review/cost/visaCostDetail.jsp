<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>签证成本录入详情页</title>
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
			if($('.ydxbds').is(":hidden")==true) {
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
			var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
			$(this).addClass("on").siblings().removeClass('on');
			$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
		});
	
		$("#contentTable").delegate(".tuanhao","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.chanpin_cen').removeClass('onshow');
			$('.tuanhao_cen').addClass('onshow');
		});
		
		$("#contentTable").delegate(".chanpin","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.tuanhao_cen').removeClass('onshow');
			$('.chanpin_cen').addClass('onshow');
		});

		var ids = "";
		var names = "";
		<c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
			ids = ids + "${data.id}"+",";
			names = names +"${data.name}"+",";
		</c:forEach>
		$("#targetAreaName").text(names.toString().substring(0,12)+'...');
		$("#targetAreaName").attr('title',names.toString().substring(0,names.length-1));
	});
	
	var deleteCost = function(id, classType){
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/cost/manager/delete",
					cache:false,
					async:false,
					data:{id : id,
						type : classType,groupId : "", orderType : "",visaId : ""},
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

	function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}

	function addCostHQX(budgetType) {
		var groupId = '${visaProduct.id}';
		var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ budgetType  +"/6/"+ '${deptId}';
		var ht = ($(window).height())*0.7;
		$.jBox(iframe, {
			title: "成本录入",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function addOtherCostHQX(budgetType) {
		var groupId = '${visaProduct.id}';
		var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ budgetType  +"/6/"+ '${deptId}';	 
		var ht = ($(window).height())*0.7;
		$.jBox(iframe, {
			title: "其它收入录入",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function updateCostHQX(costid) {
		var groupId = '${visaProduct.id}';
		var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ costid	+"/6/"+ '${deptId}';	 
		var ht = ($(window).height())*0.7;
		$.jBox(iframe, {
			title: "成本修改",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function updateOtherCostHQX(costid) {
		var groupId = '${visaProduct.id}';
		var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ costid  +"/6/"+ '${deptId}';	 
		var ht = ($(window).height())*0.7;
		$.jBox(iframe, {
			title: "其它收入修改",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function saveCheckBox(id,budgetType){
		var tmp=0;
		var msg = "";
		$("input[name='"+id+"']").each(function(){
			if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
				tmp=tmp +","+$(this).attr('value');
				if(${companyUuid eq DHJQ}) {
					if ($(this).parent().next().html() == "") {
						msg = "请上传附件后重新提交";
					}
				}
			}
		});
		if(tmp=="0"){
			if (budgetType=='0')  alert("请选择预算成本");
			else alert("请选择实际成本");
			return;
		}
		if(msg != ""){
			alert(msg);
			return false;
		}
		$.ajax({
			type: "POST",
			url: "${ctx}/costReview/visa/visaCostApply",
			cache:false,
			//dataType:"json",
			async:false,
			data:{ 
				costList:tmp,visaIds:"",groupId:"",orderType:"", deptId : '${deptId}', activityId : '${visaProduct.id}'},
			success: function(data){
				top.$.jBox.tip(data,'success');
				window.location.reload();
			},
			error : function(e){
				alert('请求失败。');
				return false;
			}
		});
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
			url: "${ctx}/review/visa/payment/apply",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:tmp},
			success: function(data){
				if(data.flag){
					$.jBox.tip('申请付款成功', 'success');
					window.location.reload();
				}else{
					$.jBox.tip('付款申请失败，' + data.msg, 'error');
					return false;
				}
			},
			error : function(e){
				$.jBox.tip('申请付款失败', 'error');
				return false;
			}
		});
	}
	
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
	
	function milliFormat(s){//添加千位符
		if(/[^0-9\.\-]/.test(s)) return "invalid value";
		s=s.replace(/^(\d*)$/,"$1.");
		s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
		s=s.replace(".",",");
		var re=/(\d)(\d{3},)/;
		while(re.test(s)){
			s=s.replace(re,"$1,$2");
		}
		s=s.replace(/,(\d\d)$/,".$1");
		return s.replace(/^\./,"0.");
	}	

	function expand(child,obj,id) {
		$.ajax({
			url:"${ctx}/cost/manager/payedRecord/",
			type:"POST",
			data:{id:id},
			success:function(data){
				var htmlstr="";
				var num = data.length;
				if(num>0){
					var str1='';
					for(var i =0;i<num;i++){
						var str = data[i].payvoucher.split("|");
						var idstr = data[i].ids.split("|");
						var index = str.length;
						if(index>0){
							for(var a=0;a<index;a++){
								str1+="<a class=\"downloadzfpz\" lang=\""+idstr[a]+"\">"+str[a]+"</a><br/>"
							}
						}
						htmlstr+="<tr><td class='tc'>"+data[i].payTypeName+"</td><td class='tc'>"+data[i].currency_mark+milliFormat(parseFloat(data[i].amount).toFixed(2))+"</td><td class='tc'>"+data[i].createDate+"</td><td class='tc'>"+"其它收入"+
						"</td><td class='tc'>";
						if(data[i].isAsAccount == null) {
							htmlstr+="待收款";
						}else if(data[i].isAsAccount == 1) {
							htmlstr+="已达账";
						}else if(data[i].isAsAccount == 101) {
							htmlstr+="已撤销";
						}else if(data[i].isAsAccount == 102) {
							htmlstr+="<div class='pr xuanfudiv'>已驳回";
							if(data[i].rejectReason!=null && data[i].rejectReason!="") {
								htmlstr+="<div class='ycq xuanfu' style='width: 24px;'>备注</div><div class='hover-title team_top hide' id='hoverWindow'>"+data[i].rejectReason+"</div>";
							}
						}
						htmlstr+="</td><td class='tc'>"+str1+"</td>"+"</tr>";
						str1='';
					}
				}
				$("#rpi").html(htmlstr);
			}
		});
		if ($(child).is(":hidden")) {
			$(obj).html("收起收款记录");
			$(obj).parents("tr").addClass("tr-hover");
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
		} else {
			if (!$(child).is(":hidden")) {
				$(obj).parents("tr").removeClass("tr-hover");
				$(child).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).html("收款记录");
			}
		}
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
	<div class="mod_nav">运控 > 产品成本录入 > 签证录入详情页</div>
	<div class="produceDiv">
		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">产品基本信息</div>
			</div>
		</div>
		
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d mod_details2_d">
				<span style="color: #3a7851; font-size: 16px; font-weight: bold;">${visaProduct.productName}</span>
				<div class="mod_information_d7"></div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">产品编号：</td>
							<td class="mod_details2_d2">${visaProduct.productCode}</td>
							<td class="mod_details2_d1">签证国家：</td>
							<td class="mod_details2_d2"><c:forEach items="${visaCountryList}" var="country">
								<c:if test="${country.id eq visaProduct.sysCountryId}">
									${country.countryName_cn}
								</c:if>
							</c:forEach></td>
							<td class="mod_details2_d1">签证类型：</td>
							<td class="mod_details2_d2">
								${visaType}
								</td>
							<td class="mod_details2_d1">签证领区：</td>
							<td class="mod_details2_d2">
							<c:if test="${not empty visaProduct.collarZoning }">
								${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
							</c:if>
							</td>
							<td colspan="2"> </td>
						</tr>
						<tr>
							<td class="mod_details2_d1">应收价格：</td>
							<td class="mod_details2_d2">${currencyMark}&nbsp;<fmt:formatNumber pattern="#.00" value="${visaProduct.visaPay}" /></td>
							<td class="mod_details2_d1">创建时间：</td>
							<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate}"/></td>
							<td class="mod_details2_d1">团号：</td>
							<td class="mod_details2_d2">${visaProduct.groupCode}  </td>
							<td colspan="2"></td>
						</tr>
					</tbody>
				</table>
				<div class="kong"></div>
			</div>
		</div>

		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">订单列表 &nbsp; <a class="zksxs">收起筛选</a></div>
			</div>
		</div>
		<div class="ydxbds">
			<table style="border-top: 1px solid #dddddd" class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
				<thead style="background: #403738;">
					<tr>
						<th width="4%">序号</th>
						<th width="7%">预定渠道</th>
						<th width="11%">订单号</th>
						<th width="11%">产品编号</th>
						<th width="8%">销售</th>
						<th width="8%">下单时间</th>
						<th width="5%">人数</th>
						<th width="8%">应收总额</th>
						<th width="8%">已收金额<br/>到账金额</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orderList}" var="visaOrder" varStatus="s">
						<tr>
							<td class="tc">${s.count}</td>
							<td class="tc">
								<c:choose>
								   	<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' &&visaOrder.agentName eq '非签约渠道' }">
							        直客
							   		</c:when>
							   		<c:otherwise>${visaOrder.agentName}</c:otherwise>
								</c:choose>
							</td>
							<td class="tc">${visaOrder.orderNo}</td>
							<td class="tc">${visaProduct.productCode}</td>
							<td class="tc">${visaOrder.orderUserName}</td>
							<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate }"/></td>
							<td class="tr">${visaOrder.travel_num}</td>
							<td class="tr">
								<c:choose>
									<c:when test="${empty visaOrder.totalMoney}"><span class="fbold"> </span></c:when>
									<c:when test="${fns:getMoneyAmountByUUIDOrderType(visaOrder.totalMoney,6,13,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
									<c:otherwise><span class="tdorange fbold">${fns:getMoneyAmountByUUIDOrderType(visaOrder.totalMoney,6,13,2)}</span></c:otherwise>
								</c:choose>
							</td>
							<td class="p0 tr">
								<div class="yfje_dd">
									<c:choose>
										<c:when test="${empty visaOrder.payedMoney}"><span class="fbold"> </span></c:when>
										<c:when test="${fns:getMoneyAmountByUUIDOrderType(visaOrder.payedMoney,6,5,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
										<c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(visaOrder.payedMoney,6,5,2)}</span></c:otherwise>
									</c:choose>
								</div>
								<div class="dzje_dd">
									<c:choose>
										<c:when test="${empty visaOrder.accountedMoney}"><span class="fbold"> </span></c:when>
										<c:when test="${fns:getMoneyAmountByUUIDOrderType(visaOrder.accountedMoney,6,4,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
										<c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(visaOrder.accountedMoney,6,4,2)}</span></c:otherwise>
									</c:choose>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
			<ul class="cost-ul" data-total="cost">
				<ul class="cost-ul" data-total="income">
					<li>订单总收入：&nbsp;¥ <fmt:formatNumber type="currency"
							pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney') }" /></li>
				</ul>
				<li>订单总人数：&nbsp;${fns:getSum(orderList,'travel_num') }</li>
			</ul>
		</div>

		<iframe id="iframepage" width="100%" height="100%" frameborder="0" src="${ctx}/cost/common/getCostRecordList/${visaProduct.id }/6" onLoad="iFrameHeight()"></iframe>

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
<script type="text/javascript">
function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}
//运控-成本录入-添加项目--小计
function costSums(obj,objshow,ordertype){
	var objMoney = {};
	obj.each(function(index, element) {
		//var currencyName = $(element).find("td[name='tdCurrencyName']").text();
		var thisAccount = $(element).find("td[name='tdAccount']").text();
		if(thisAccount == '') {
			thisAccount = 1;
		}
		var thisPrice = $(element).find("td[name='tdPrice']").text();	
		if(thisPrice.indexOf('-')!=-1) thisPrice = $(element).find("td[name='tdPrice']").next().next().next().text();
		var thisReview = $(element).find("td[name='tdReview']").text();	
		var border=2;
		//去掉两边空格
		thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
		//找到金额中第一个数字位置
		for(var i=0;i<thisPrice.length;i++){
			if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
				border=i;
				break;
			}
		}
		var currencyName =thisPrice.substring(0,border).trim();
		//金额去掉第一个字符(币种)
		thisPrice=thisPrice.substring(border);  
		if(ordertype==2 || (ordertype==0 && trimStr(thisReview) != '已取消' && trimStr(thisReview) != '取消申请')|| (ordertype==1 && trimStr(thisReview) != '已取消' && (trimStr(thisReview) != '已驳回'&&trimStr(thisReview) != '审核失败(驳回)' && trimStr(thisReview) != '取消申请' && trimStr(thisReview) != '审批驳回') )){
			if(typeof objMoney[currencyName] == "undefined"){
				objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(new RegExp(",","gm"),""),10);
			}else{
				objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(new RegExp(",","gm"),""),10);
			}
		}
	});
	
	//输出结果
	var strCurrency = "";
	var sign = " + "; 
	for(var i in objMoney){
		var isNegative = /^\-/.test(objMoney[i]);
		if(isNegative){
			sign = " - ";
		}
		if(strCurrency != '' || (strCurrency == '' && isNegative)){
			strCurrency += sign;
		}
		strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
	}
	if(objshow.length>0) objshow.text("  "+strCurrency);
}

$(function(){
	costSums($('tr.otherCost'),$('#otherCostShow'),2);
	//实际成本录入-境内小计
	costSums($('tr.budgetInCost'),$('#budgetInShow'),0);
	//实际成本录入-境外小计  
	costSums($('tr.budgetOutCost'),$("#budgetOutShow"),0);

	costSums($('tr.actualInCost'),$("#actualInShow"),1);
	
	costSums($('tr.actualOutCost'),$("#actualOutShow"),1);

});
</script>
	
</body>
</html>
