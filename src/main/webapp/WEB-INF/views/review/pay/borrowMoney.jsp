<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>借款付款</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/cost/finance_common.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/Remark_New.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.nicescroll.min.js"></script>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/Remark_New.css">
<script type="text/javascript">
var ctx = '${ctx}';

Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var inputRequest = false;
	var selectRequest = false;
	
	$("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='groupCode']").each(function(){
		if($(this).val()){
			inputRequest = true;
			return false;
		}
	})
	$("#searchForm").find("select").each(function(){
		var select_val = $(this).children("option:selected").val();
		if(select_val && select_val != 0){
			selectRequest = true;
			return false;
		}
	})
	if (inputRequest||selectRequest) {
		$('.zksx').click();
	}
}
function query() {
	$('#searchForm').submit();
}

// 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22
function exportExcel() {
    $('#searchForm').attr('action', '${ctx}/costNew/payManager/getBorrowMoneyListExcel');
    $('#searchForm').submit();
	$('#searchForm').attr('action', '${ctx}/costNew/payManager/payList/203');
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
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

function cancelOper(ctx,str,orderType,obj){
    $.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&payType=2&orderType="+orderType,{
        title: "付款记录",
		width:830,
   		height: 500,
   		buttons:{'撤销': 1,'关闭':0},
   		persistent:true,
   		loaded: function(h){},
   		submit:function(v,h,f){
   		    if(v==1){
   		         var refundId =  $(h.find("iframe")[0].contentWindow.refundId).val();
   		         var recordId =  $(h.find("iframe")[0].contentWindow.recordId).val();
   		         $.ajax({
   		            type:"GET",
   		            url:ctx+"/refund/manager/undoRefundPayInfo",
   		            dataType:"json",
   		            data:{refundId:refundId,recordId:recordId},
   		            success:function(data){
   		              if(data.flag=='ok'){
   		            	  cancelOptionMoney(obj);
   		              }
   		            }
   		         })
   		    }
   		}
    }).find("#jbox-content").css("overflow", "hidden"); 
}

function cancelOptionMoney(obj){
	var moneyObj = $(obj).parents('.activity_team_top1').prev().find("div.dzje_dd");
    var currencyDzje = moneyObj.text().trim();             //主表中的 已付金额  例如 $256.30
    if(currencyDzje){
    	var operator = currencyDzje.match(/-/g) || [""];
    	var numArray = currencyDzje.replace(new RegExp(/,/g),"").match(/\d+/g);                 //取出金额中的数值 $256.30 --> [256,30]
    	console.log(numArray);
        var dzje = null;
        if(numArray.length == 2){
        	dzje = parseFloat(operator[0] + numArray[0] + '.' + numArray[1]).toFixed(2);
        }
        console.log(dzje);
    	var cny = currencyDzje.replace(dzje,'');                   //获取币种符号 
    	
        var index = $(obj).parent().siblings().first().text().trim();//付款记录里面的索引号
        var id = '#money_' + index;
    	var currencyRecordMoney = $(obj).parent().siblings().filter(id).text().trim(); //付款记录里面的金额 
    	console.log(currencyRecordMoney);
    	var recordMoney = null;
    	if(currencyRecordMoney){
    		var recordOperator = currencyRecordMoney.match(/-/g) || [""];
    		var recordNumArray = currencyRecordMoney.replace(new RegExp(/,/g),"").match(/\d+/g);
    		if(recordNumArray.length==2){
    			recordMoney = parseFloat(recordOperator[0] + recordNumArray[0] + '.' + recordNumArray[1]).toFixed(2);
    		}
    		console.log(recordMoney);
    		var cha = parseFloat(dzje - recordMoney).toFixed(2);
    		console.log(cha);
    		if(cha == 0){
    			moneyObj.html('<span class="fbold"> </span>');
    		}else{
    			moneyObj.html("<span class='fbold'>"+(cny + cha)+"</span>");
    		}
    	}
    }
    $(obj).parent().prev().html('已撤销');//状态改成已撤销
    $(obj).next().remove();             //移除打印按钮 
    $(obj).remove();                    //移除撤销按钮  
}

function cancelDetailOper(ctx,refundId,recordId,obj){
	$.ajax({
       type:"GET",
       url:ctx+"/refund/manager/undoRefundPayInfo",
       dataType:"json",
       data:{refundId:refundId,recordId:recordId},
       success:function(data){
         if(data.flag=='ok'){
            $(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
         }
       }});
}

function expand(child,obj,review_id,money_type,index,orderType, revId, isShowPrint, reviewFlag) {
	$.ajax({
		url:"${ctx}/orderCommon/manage/refundPayInfo/",
       	type:"POST",
       	data:{id:review_id,type:money_type,orderType:orderType},
       	success:function(data){
			var htmlstr=""
           	var num = data.length;
           	if(num>0){
				for(var i=0;i<num;i++){
            	   	var payOrder = '';//支付凭证
            	   	var payOrderArray = data[i].payvoucher.split("|")
                   	for(var j=0;j<payOrderArray.length;j++){
                       	payOrder+=payOrderArray[j]+"<br/>"
                   	}
            	  	var payType = data[i].label;	//付款方式
					if (payType == null){
						payType = "因公支付宝"; <%-- 拉美图的因公支付宝结算方式暂未保存到字典表中。后期如做调整可删除 --%>
					}
            	   	var amount = data[i].amount;	//金额
            	   	var date_val = (new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss");//日期
            	   	var type	= data[i].a;	//支付类型
            	   	var status = data[i].opstatus;	//付款状态
            	   	var refund_id = data[i].id;		//refund表Id
            	   	var order_type = data[i].orderType;	//订单类型
                   	htmlstr+="<tr><td class='tc'>"+(i+1)+"</td><td class='tc'>"+payType+
                            "</td><td id='money_"+(i+1)+"' style='text-align:center'>"+amount+
                            "</td><td class='tc'>"+date_val+"</td><td class='tc'>"+type+
                            "</td><td class='tc'>"+payOrder+"</td><td class='tc'>"+status+"</td><td class='tc'>";
					if(status=='已支付'){
                   		if(orderType=='11' || orderType=='12') {
                   			htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','"+refund_id+"',2,cancelDetailOper,"+order_type+",this)\" href='javascript:void'>查看</a>"+
                	      		"&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+refund_id+"','"+order_type+"',this)\" >撤销</a></td></tr>"
                   		}else{
                   			htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','"+refund_id+"',2,cancelDetailOper,"+order_type+",this)\" href='javascript:void'>查看</a>"+
                	      		"&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+refund_id+"','"+order_type+"',this)\" >撤销</a>";
                	      		
                	      	if(isShowPrint=='2' && orderType=='6'){		//签证单独打印
                	      		htmlstr += "&nbsp;&nbsp;<a target=\"_blank\" href=\"${ctx}/visa/workflow/borrowmoney/visaBorrowMoney4XXZFeePrint?revid="+revId+"&payId="+refund_id+"&option=pay"+"\">打印</a><br />";
                	      	}
                	      	if(orderType!='6' && reviewFlag=='1'){		//旧审批数据
                	      		htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/printForm/borrowMoneyForm?reviewId="+revId+"&orderType="+order_type+"&payId="+refund_id+"&option=pay"+"\" target=\"_blank\">打印</a><br/>";
                	      	}
                	      	if(orderType!='6' && reviewFlag=='2'){		//新审批数据
                	      		htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/newOrderReview/manage/showBorrowMoneyForm?reviewId="+revId+"&orderType="+order_type+"&payId="+refund_id+"&option=pay"+"\" target=\"_blank\">打印</a><br/>";
                	      	}
                	      	
                	      	htmlstr += "</td></tr>"
                   		}                     
                   	}else{
                      	htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+refund_id+"',2,cancelDetailOper,"+order_type+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;</td></tr>"
                   	}
               	}
           	}else{
        	   	htmlstr+="<tr><td colspan='8' style='text-align: center;'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td></tr>"
           	}
           	$("#rpi_" + review_id + "_" + index).html(htmlstr);
		}
	});
	if ($(child).is(":hidden")) {
		$(obj).html("收起");
		$(obj).parents("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	}else{
		if (!$(child).is(":hidden")) {
			$(obj).parents("tr").removeClass("tr-hover");
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("支付记录");
		}
	}
}

$(function(){
	//展开、收起筛选
	launch();
	
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	
	//操作浮框
	operateHandler();
	
	//产品名称获得焦点显示隐藏
	inputTips();
	
	var _$orderBy = $.trim($("#orderBy").val() || "updateDate DESC");
	$("#orderBy").val(_$orderBy);
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
         }
    });
    
    $("#contentTable").delegate("ul.caption > li","click",function(){
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
    
  	//首次打印提醒
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	});
});
/**
 * 表单重置，不能改成 $('#searchForm')[0].reset() 
 * 输入条件点击查询之后，再点击条件重置按钮，此方法在Google Chrome 下失效 
 * @author shijun.liu
 */
function resetForm(){
	$('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']").each(function(){
		if($(this).val()){
			$(this).val('');
		}
	})
	$('#searchForm').find("select").each(function(){
		var select_val = $(this).children("option");
		$(select_val[0]).attr("selected","selected");
	})
}

//批量付款的处理
$(document).on('change','#contentTable,#contentTable_foot, [type="checkbox"]',function(){
    var $this = $(this);
    var $contentContainer = $("#contentTable,#contentTable_foot");
    var $all =$contentContainer.find('[check-action="All"]');
    var $reverse = $contentContainer.find('[check-action="Reverse"]');
    var $chks=$contentContainer.find('[check-action="Normal"]:enabled');
    if($this.is('[check-action="All"]')){
        if($this.is(':checked')){
            $chks.attr('checked',true);
        }else{
            $chks.removeAttr('checked');
        }
    }
    if($this.is('[check-action="Reverse"]')){
        $chks.each(function(){
            var $chk = $(this);
            if($chk.is(':checked')){
                $chk.removeAttr('checked');
            }else{
                $chk.attr('checked',true);
            }
        });
    }
    if($chks.length && ($chks.length ==$chks.filter(':checked').length)){
        $all.attr('checked',true);
    }else{
        $all.removeAttr('checked');
    }
});


function batchPrint(){
	var tmp = '';
	$("input[name='checkBox']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
		}
	});
	if(tmp==""){
		$.jBox.tip('请选择数据！','warnning');
		return;
	}
	$("#printInfo").val(tmp);
	$("#printForm").submit();
}

function batchConfirmOrCannePay(ctx,id,type,status){
	var tmp = '';
	var havePayed = false; // 用户是否有选择已确认付款的条目
	$("input[name='"+id+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
			if($(this).attr('payStatus') == '1'){ // 表示有选择确认付款的
				havePayed = true;
			}
		}
	});
	if(tmp==""){
		$.jBox.tip('请选择数据','warnning');
		return;
	}
	if(havePayed){
		$.jBox.tip('已有数据付款，请重新选择','warnning');
		return;
	}
	var msg="确认付款吗？";
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/orderCommon/manage/batchConfirmPay",
				data:{id:tmp,type:type,status:status},
				success:function(data){
					if('ok' == data.flag){
						$("#searchForm").submit();
					}else{

					}
				}
			})
		}
		if(v=='cancel'){

		}
	})
}
</script>

</head>

<c:set var="showType" value="${payType }" />
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/costNew/payManager/payList/203" method="post">
   <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
   <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
   <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
   <div class="activitylist_bodyer_right_team_co">
       <div class="activitylist_bodyer_right_team_co2">
		   <%--bug17457 添加class inputTxt--%>
	       <input id="groupCode" name="groupCode" class="txtPro inputTxt searchInput" value="${groupCode}" placeholder="请输入团号"/>
       </div>
	   <div class="zksx">筛选</div>
       <div class="form_submit">
           <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
           <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
           <!-- 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22 -->
           <input class="btn ydbz_x" type="button" onclick="exportExcel();" value="导出Excel" />
       </div>
       <div class="ydxbd">
			   <span></span>
               <div class="activitylist_bodyer_right_team_co1">
       		       <div class="activitylist_team_co3_text">团队类型：</div>
				   <div class="selectStyle">
					   <select name="orderType" id="orderS">
							<!-- 首先判断是否是俄风行批发商  -->
							<c:choose>
								<c:when test="${fns:getUser().company.uuid eq '79c8cf8377a811e5bc1e000c29cf2586'}">
									<option value="0" <c:if test="${orderTypeValue eq '0'}">selected="selected"</c:if>>全部</option>
									<option value="11" <c:if test="${orderTypeValue eq '11'}">selected="selected"</c:if>>酒店</option>
									<option value="12" <c:if test="${orderTypeValue eq '12'}">selected="selected"</c:if>>海岛游</option>
								</c:when>
								<c:otherwise>
									<c:forEach var="order" items="${orderTypeCombox }">
										<c:choose>
											<c:when test="${isXXZ}">
												<option value="${order.value }" <c:if test="${orderTypeValue==order.value}">selected="selected"</c:if>>${order.label }</option>
											</c:when>
											<c:otherwise>
												<c:if test="${order.value ne 6 }">
													<option value="${order.value }" <c:if test="${orderTypeValue==order.value}">selected="selected"</c:if>>${order.label }</option>
												</c:if>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:otherwise>
							</c:choose>
					   </select>
				   </div>
               </div> 
               <div class="activitylist_bodyer_right_team_co1">
       			   <div class="activitylist_team_co3_text">付款状态：</div>
				   <div class="selectStyle">
					   <select name="payStatus">
							<option value="">全部</option>
							<option value="0" <c:if test="${payStatus eq '0'}">selected="selected"</c:if>>未付</option>
							<option value="1" <c:if test="${payStatus eq '1'}">selected="selected"</c:if>>已付</option>
					   </select>
				   </div>
               </div>
               <div class="activitylist_bodyer_right_team_co4">
               	   <div class="activitylist_team_co3_text">付款金额：</div>
				   <div class="selectStyle">
					   <select id="currency" name="currencyId">
						   <option value="">币种选择</option>
						   <c:forEach items="${currencyList}" var="c">
								<option value="${c.id}" <c:if test="${currencyId==c.id}">selected="selected"</c:if>>${c.currencyName}</option>
						   </c:forEach>
					   </select>
				   </div>
                   		<input type="text" value="${moneyBegin }" name="moneyBegin" id="startMoney" class="inputTxt"  onkeyup="validNum(this)"
							   onafterpaste="validNum(this))" placeholder="输入金额"/>
                   <span>~</span>
               	   		<input type="text" value="${moneyEnd }" name="moneyEnd" id="endMoney" class="inputTxt"  onkeyup="validNum(this)"
							   onafterpaste="validNum(this))" placeholder="输入金额"/>
               </div>
         	   <div class="activitylist_bodyer_right_team_co1" >
	               <div class="activitylist_team_co3_text">计调：</div>
				   <div class="selectStyle">
                       <select name="operatorId">
                           <option value="">全部</option>
                           <c:forEach var="operator" items="${operator }">
                           <option value="${operator.id }" <c:if test="${operatorId==operator.id}">selected="selected"</c:if>>${operator.name }</option>
                           </c:forEach>
                       </select>
				   </div>
	           </div>
	           <div class="activitylist_bodyer_right_team_co1">
       			   <div class="activitylist_team_co3_text">打印状态：</div>
				   <div class="selectStyle">
					   <select name="printStatus">
							<option value="">全部</option>
							<option value="0" <c:if test="${printStatus eq '0'}">selected="selected"</c:if>>未打印</option>
							<option value="1" <c:if test="${printStatus eq '1'}">selected="selected"</c:if>>已打印</option>
					   </select>
				   </div>
               </div>
			   <div class="activitylist_bodyer_right_team_co1" >
				   <div class="activitylist_team_co3_text">申请日期：</div>
				   <input class="inputTxt dateinput" value="${createTimeMin}" name="createTimeMin" readonly="" onclick="WdatePicker()">
				   至
				   <input class="inputTxt dateinput" value="${createTimeMax}" name="createTimeMax" readonly="" onclick="WdatePicker()">
			   </div>
		</div>
   </div>
</form:form>

    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
            <ul>
            <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

	<table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
	        <tr>
	            <th colspan="2" width="6%">序号</th>
	            <th width="8%">申请日期</th>
	            <th width="8%">团队类型</th>
				<c:if test="${isHQX}">
					<th width="10%">订单团号</th>
				</c:if>
				<c:if test="${!isHQX}">
					<th width="10%">团号</th>
				</c:if>
	            <th width="15%">团队名称</th>
	            <th width="6%">计调</th>
	            <th width="10%">款项</th>
	            <th width="10%">付款金额<br />已付金额</th>
	            <th width="6%">出纳确认</th>
	            <th width="6%">打印状态</th>
	            <th width="5%">操作</th>
	        </tr>
        </thead>
        <tbody>
        <c:if test="${fn:length(page.list) <= 0 }">
            <tr>
	            <td colspan="20" style="text-align: center;">暂无搜索结果</td>
            </tr>
        </c:if>
        <c:forEach items="${page.list}" var="pay" varStatus="s">
            <tr>
            	<td class="tc" ><input type="checkbox" name="checkBox" value="${pay.reviewId }_${pay.orderType }_${pay.reviewflag}_${pay.isShowPrint}" check-action="Normal" payStatus="${pay.payStatus}"<%--<c:if test="${pay.payStatus eq '1' }">disabled="disabled"</c:if>/>--%> </td>
                <td class="tc">${s.count }</td>
                <td class="tc"><fmt:formatDate value="${pay.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td class="tc">${fns:getStringOrderStatus(pay.orderType)}</td>
				<td class="tc">${pay.groupCode }</td>
				<c:if test="${pay.orderType ne 11 and pay.orderType ne 12}">
					<td class="tc">${fns:getProductName(pay.productId, pay.orderType)}</td>
				</c:if>
				<c:if test="${pay.orderType eq 11 or pay.orderType eq 12}">
					<td>${pay.chanpname}</td>
				</c:if>
                <td class="tc">
                	<c:if test="${not empty pay.operatorId}">
                		${fns:getUserById(pay.operatorId).name}
                	</c:if>
                </td>
                <td class="tc">${fns:getStringOrderStatus(pay.orderType)}借款</td>
                <td class="tr">
                	<div class="yfje_dd">
                	<!-- 新审批的借款付款 -->
                	<c:if test="${pay.reviewflag eq 2}">
                		<span class="fbold">${fns:getMoneyByReviewUuid(pay.reviewId,'0')}</span> </div>
                		<div class="dzje_dd"><span class="fbold">${fns:getRefundPayedMoney(pay.id_long,'10',pay.orderType)}</span></div>
                	</c:if>
                	<!-- 旧审批的借款付款 -->
                	<c:if test="${pay.reviewflag eq 1}">
                		<span class="fbold">${fns:getMoneyByReviewId(pay.reviewId,'0')}</span> </div>
                		<div class="dzje_dd"><span class="fbold">${fns:getRefundPayedMoney(pay.reviewId,'4',pay.orderType)}</span></div>
                	</c:if>
                </td>
                <td class="tc">
               		<c:if test="${pay.payStatus eq '0' }">
               			<font style="color:red">未付</font>
               		</c:if>
               		<c:if test="${pay.payStatus eq '1' }">
               			<font style="color:green">已付</font>
               		</c:if>
               		<!--548&&549-->
               		<c:if test="${pay.hasRemarks and pay.isShowRemark eq 1 }">
                    	<div class="div_548">
                    		<div class="remark_548">备注
                        		<div class="remark_div">
                            		<em class="arrows_548"></em>
                                	<div class="remark_div_head">
                                		<div class="remark_head_left">审批备注</div>
                                    	<div class="remark_head_right">
                                    		<input type="checkbox"/>
                                    		<input type="hidden" value="${pay.reviewId }" />
                                        	<div>不再显示</div>
                                    	</div>
                                	</div>
                                	<div class="remark_div_body">
                                		<div class="remark_div_child">
                                    		<c:forEach items="${pay.logs}" var="reviewLog" varStatus="status">
                                        		<ul>
                                        			<li class="left_548">
                                        				<div class="color_black">
                                        					<fmt:formatDate value="${reviewLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        				</div>
                                        				<div class="green_548">审批通过</div>
                                        			</li>
                                        			<li class="center_548"><em class="point-time step_yes"></em></li>
                                        			<li class="right_548">
                                        				<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
                                        				（<span class="people_name_548">${fns:getUserById(reviewLog.createBy).name}</span>）
                                        				<div class="right_body"><div class="remarks">备注：</div>${reviewLog.remark}</div>
                                        			</li>
                                        		</ul>
                                       	 	</c:forEach>
                                        	<ul>
                                        		<li class="left_548"><div class="color_black"></div><div class="green_548">审批通过</div></li>
                                            	<li class="center_548"><em class="point-time yes"></em></li>
                                            	<li class="right_548"><div class="right_head"></div></li>
                                        	</ul>	
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:if>
               	 	<!--548&&549end--> 
                </td>
                <c:choose>
				     <c:when test="${pay.printFlag eq '1'}">
				     	 <td class="tc"><p class="uiPrint" style="color:green;">已打印<span style="display: none;color:purple;">首次打印日期<br><fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${pay.printTime}"/></span></p></td>
				     </c:when>
				     <c:otherwise>
				     	<td class="tc" style="color:green;">未打印</td>
				     </c:otherwise>
				</c:choose>
                <td class="p0">
                    <dl class="handle">
						<%--修改按钮图片  bug 17642 by ruiqi.zhang 2017-3-24 10:44:20--%>
                       	<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
	                   	<dd><p>
                           	<span></span>
                           	<!-- 新审批付款 -->
                           	<c:if test="${pay.reviewflag eq 2}">
                           		<a href="${ctx}/costNew/payManager/pay/203?orderType=${pay.orderType}&orderId=${pay.orderId}&travelerId=${pay.travelerId}&reviewId=${pay.reviewId}&agentId=${pay.agentId}" target="_blank">付款 </a>
                           	</c:if>
                           	<!-- 旧审批付款 -->
                           	<c:if test="${pay.reviewflag eq 1}">
                           		<a href="${ctx}/cost/payManager/pay/203?orderType=${pay.orderType}&orderId=${pay.orderId}&travelerId=${pay.travelerId}&reviewId=${pay.reviewId}&agentId=${pay.agentId}" target="_blank">付款 </a>
                           	</c:if>
                           	
                            <c:if test="${pay.orderType eq 6}">
                            	<a href="${ctx}/costReview/visa/visaCostReviewDetail/${pay.productId}/2?read=1" target="_blank">查看</a>
                            	<shiro:hasPermission name="globleloanpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${pay.productId}/${pay.orderType}" target="_blank">预报单</a></shiro:hasPermission>
                              	<shiro:hasPermission name="globleloanpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${pay.productId}/${pay.orderType}" target="_blank">结算单</a></shiro:hasPermission>
                            </c:if>
                            <c:if test="${pay.orderType eq 7}">
                            	<a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${pay.productId}/2?read=1" target="_blank">查看</a>
                                <shiro:hasPermission name="globleloanpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${pay.productId}/${pay.orderType}" target="_blank">预报单</a></shiro:hasPermission>
                              	<shiro:hasPermission name="globleloanpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${pay.productId}/${pay.orderType}" target="_blank">结算单</a></shiro:hasPermission>
                            </c:if>
                            <c:if test="${pay.orderType lt 6 || pay.orderType eq 10}">
                            	<a href="${ctx}/costReview/activity/activityCostReviewDetail/${pay.productId}/${pay.groupId}/2?read=1" target="_blank">查看</a>
                                <shiro:hasPermission name="globleloanpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${pay.groupId}/${pay.orderType}" target="_blank">预报单</a></shiro:hasPermission>
                              	<shiro:hasPermission name="globleloanpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${pay.groupId}/${pay.orderType}" target="_blank">结算单</a></shiro:hasPermission>
                            </c:if>
                            <c:if test="${pay.orderType eq 11}">
                            	<a target="_blank" href="${ctx}/cost/review/hotelRead/${pay.productId}/${pay.groupId}/3?menuid=4">查看</a>
                                <shiro:hasPermission name="globleloanpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${pay.groupId}/${pay.orderType}" target="_blank">预报单</a></shiro:hasPermission>
                              	<shiro:hasPermission name="globleloanpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${pay.groupId}/${pay.orderType}" target="_blank">结算单</a></shiro:hasPermission>
                              	<a href="${ctx }/cost/manager/returnMoneyList4JK/${pay.reviewId}/${pay.orderType}"
												target="_blank">打印</a>
                            </c:if>
                            <c:if test="${pay.orderType eq 12}">
                            	<a target="_blank" href="${ctx}/cost/review/islandRead/${pay.productId}/${pay.groupId}/3?&menuid=4">查看</a>
                                <shiro:hasPermission name="globleloanpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${pay.groupId}/${pay.orderType}" target="_blank">预报单</a></shiro:hasPermission>
                              	<shiro:hasPermission name="globleloanpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${pay.groupId}/${pay.orderType}" target="_blank">结算单</a></shiro:hasPermission>
                              	<a href="${ctx }/cost/manager/returnMoneyList4JK/${pay.reviewId}/${pay.orderType}"
												target="_blank">打印</a>
                            </c:if>
                            <!-- 新审批支付记录  -->
                            <c:if test="${pay.reviewflag eq 2}">
                            	<a onclick="expand('#child1_${pay.reviewId}_${s.count}',this,${pay.id_long},'10',${s.count},${pay.orderType}, '${pay.reviewId}', ${pay.isShowPrint}, ${pay.reviewflag})" href="javascript:void(0)">支付记录</a>
                            </c:if>
                            <!-- 旧审批支付记录  -->
                            <c:if test="${pay.reviewflag eq 1}">
                        		<a onclick="expand('#child1_${pay.reviewId}_${s.count}',this,${pay.reviewId},'4',${s.count},${pay.orderType}, '${pay.reviewId}', ${pay.isShowPrint}, ${pay.reviewflag})" href="javascript:void(0)">支付记录</a>    
                            </c:if>
                            <c:if test="${pay.reviewflag eq 2}">
                            	<c:choose>
	                            	<c:when test="${pay.payStatus eq '0'}">
	                            		<a href="javascript:void(0)" onclick="confirmOrCannelPay('${ctx}', '${pay.reviewId}', '1', 0)">确认付款</a>
	                            	</c:when>
	                            	<c:otherwise>
	                            		<a href="javascript:void(0)" onclick="confirmOrCannelPay('${ctx}', '${pay.reviewId}', '0', 1)">取消付款</a>
	                            	</c:otherwise>
                            	</c:choose>
                            </c:if>
                            <c:if test="${pay.reviewflag eq 1}">
                            	<c:choose>
	                            	<c:when test="${pay.payStatus eq '0' }">
	                            		<a href="javascript:void(0)" onclick="confirmOrCannePay('${ctx}',${pay.reviewId},'4','1',${pay.payStatus})">确认付款</a>
	                            	</c:when>
	                            	<c:otherwise>
	                            		<a href="javascript:void(0)" onclick="confirmOrCannePay('${ctx}',${pay.reviewId},'4','0',${pay.payStatus})">取消付款</a>
	                            	</c:otherwise>
	                            </c:choose>
                            </c:if>
                            <!-- 签证打印单独显示 -->
                            <shiro:hasPermission name="globleloanpay:operation:print">
                            	<c:if test="${pay.isShowPrint eq '2' and pay.orderType eq 6}">
                            		<a target="_blank" href="${ctx}/visa/workflow/borrowmoney/visaBorrowMoney4XXZFeePrint?revid=${pay.reviewId}&option=order">打印</a><br />
                            	</c:if>

	                        	<c:if test="${pay.orderType ne 6 and  pay.orderType ne 11 and pay.orderType ne 12 and pay.reviewflag eq 1}">
	                         		<a href="${ctx}/printForm/borrowMoneyForm?reviewId=${pay.reviewId}&orderType=${pay.orderType}&option=order" target="_blank">打印</a><br/>
	                         	</c:if>
								<c:if test="${pay.orderType ne 6 and pay.orderType ne 11 and pay.orderType ne 12 and pay.reviewflag eq 2}">
									<a href="${ctx}/newOrderReview/manage/showBorrowMoneyForm?reviewId=${pay.reviewId}&orderType=${pay.orderType}&option=order" target="_blank">打印</a><br/>
								</c:if>
							</shiro:hasPermission>
                           </p>
                        </dd>
                    </dl>
                </td>
            </tr>
            <tr id="child1_${pay.reviewId}_${s.count}" class="activity_team_top1" style="display:none"> 
				<td colspan="15" class="team_top">
					<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
						<thead>
							<tr>
								<th class="tc" width="5%">序号</th>
								<th class="tc" width="15%">付款方式</th>
								<th class="tc" width="10%">金额</th>
								<th class="tc" width="10%">日期</th>
								<th class="tc" width="10%">支付类型</th>
								<th class="tc" width="15%">支付凭证</th>
								<th class="tc" width="10%">状态</th>
								<th class="tc" width="10%">操作</th>
							</tr>
						</thead>
						<c:if test="${pay.reviewflag eq 2}">
							<tbody id='rpi_${pay.id_long}_${s.count}'>
							</tbody>
						</c:if>
						<c:if test="${pay.reviewflag eq 1}">
							<tbody id='rpi_${pay.reviewId}_${s.count}'>
							</tbody>
						</c:if>
					</table>
				</td>
			</tr>	
        </c:forEach>
        </tbody> 
    </table>
	<form method="post" action="${ctx}/printForm/borrowMoneyBatchPrint" id="printForm" target="_blank">
		<input id="printInfo" name="printInfo" type="hidden" value=""/>
	</form>
	<div class="page" id="contentTable_foot">
		<div class="pagination">
			<dl>
				<dt><input check-action="All" type="checkbox">全选</dt>
				<dt><input check-action="Reverse" type="checkbox">反选</dt>
				<dd>
					<%--bug17485 按钮为次级按钮，去掉class btn-primary --%>
					<input type="button" class="btn" value="批量确认付款" onclick="batchConfirmOrCannePay('${ctx }','checkBox','4',1)" />
					<shiro:hasPermission name="globleloanpay:operation:print">
						<input type="button" class="btn" value="批量打印" onclick="batchPrint()" />
					</shiro:hasPermission>
				</dd>
			</dl>
		</div>

		<div class="pagination clearFix">
			${page}
		</div>
	</div>

</div>
</body>
</html>