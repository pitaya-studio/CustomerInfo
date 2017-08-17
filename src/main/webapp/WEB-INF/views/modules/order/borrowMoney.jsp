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
<script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
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
	} else {
		$('.zksx').text('展开筛选');
	}
}
function query() {
	$('#searchForm').submit();
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
    $.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&orderType="+orderType,{
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
    }) 
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
        $(obj).parent().prev().html('已撤销');//状态改成已撤销 
        $(obj).remove();                    //移除撤销按钮 
    }else{
    	$(obj).parent().prev().html('已撤销');//状态改成已撤销 
        $(obj).remove();                    //移除撤销按钮 
    }
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

function expand(child,obj,id,type,index,orderType) {
    $.ajax({
       url:"${ctx}/orderCommon/manage/refundPayInfo/",
       type:"POST",
       data:{id:id,type:type,orderType:orderType},
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
                   htmlstr+="<tr><td class='tc'>"+(i+1)+"</td><td class='tc'>"+data[i].label+
                            "</td><td id='money_"+(i+1)+"' style='text-align:center'>"+data[i].amount+"</td><td class='tc'>"+
                            (new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss")+
                            "</td><td class='tc'>"+data[i].a+"</td><td class='tc'>"+payOrder+
                            "</td><td class='tc'>"+data[i].opstatus+"</td><td class='tc'>";
                   if(data[i].opstatus=='已支付'){
                      htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\" >撤销</a></td></tr>"
                   }else{
                      htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;</td></tr>"
                   }
               }
           }else{
        	   htmlstr+="<tr><td colspan='8' style='text-align: center;'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td></tr>"
           }
           $("#rpi_" + id + "_" + index).html(htmlstr);
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
	
	var _$orderBy = $("#orderBy").val() || "updateDate DESC";
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
$(document).on('change','#contentTable, [type="checkbox"]',function(){
    var $this = $(this);
    var $contentTable = $("#contentTable");
    var $all =$contentTable.find('[check-action="All"]');
    var $reverse = $contentTable.find('[check-action="Reverse"]');
    var $chks=$contentTable.find('[check-action="Normal"]:enabled');
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
})
</script>

<style type="text/css">
	a{display: inline-block;}
	label{ cursor:inherit;}
</style>
</head>
<body>
<page:applyDecorator name="finance_op_head" >
    <page:param name="showType">${payType }</page:param>
</page:applyDecorator>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/cost/payManager/payList/203" method="post" >
   <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
   <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
   <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
   <div class="activitylist_bodyer_right_team_co">
       <div class="activitylist_bodyer_right_team_co2 wpr20">
	       <label>团号：</label><input id="groupCode"  name="groupCode" class="txtPro inputTxt" value="${groupCode}"/> 
       </div>
       <div class="form_submit">
           <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
           <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
       </div>
       <div class="zksx">筛选</div>
           <div class="ydxbd">
               <div class="activitylist_bodyer_right_team_co3">
       		       <div class="activitylist_team_co3_text">团队类型：</div>
                   <select name="orderType" id="orderS">
                   		<c:choose>
                   			<c:when test="${fn:contains(fns:getUser().company.name,'俄风行') or fn:contains(fns:getUser().company.name,'九州风行')}">
                   				<option value="0" <c:if test="${orderTypeValue eq '0'}">selected="selected"</c:if>>全部</option>
                   				<option value="11" <c:if test="${orderTypeValue eq '11'}">selected="selected"</c:if>>酒店</option>
                   				<option value="12" <c:if test="${orderTypeValue eq '12'}">selected="selected"</c:if>>海岛游</option>
                   			</c:when>
                   			<c:otherwise>
                   				<c:forEach var="order" items="${orderTypeCombox }">
			                       <option value="${order.value }" <c:if test="${orderTypeValue==order.value}">selected="selected"</c:if>>${order.label }</option>
			                    </c:forEach>
                   			</c:otherwise>
                   		</c:choose>
                   </select>
               </div>
               <c:if test="${confirmPay eq '1'}">
	               <div class="activitylist_bodyer_right_team_co1">
	       			   <div class="activitylist_team_co3_text">付款状态：</div>
	                   <select name="payStatus">
	                        <option value="">全部</option>
					    	<option value="0" <c:if test="${payStatus eq '0'}">selected="selected"</c:if>>未付</option>
					    	<option value="1" <c:if test="${payStatus eq '1'}">selected="selected"</c:if>>已付</option>
	                   </select>
	               </div>
               </c:if>
               <div class="activitylist_bodyer_right_team_co2">
               	   <div class="activitylist_team_co3_text">付款金额：</div>
                   <select id="currency" name="currencyId" style="width:90px;margin-bottom:0">
	                   <option value="">币种选择</option>
					   <c:forEach items="${currencyList}" var="c">
							<option value="${c.id}" <c:if test="${currencyId==c.id}">selected="selected"</c:if>>${c.currencyName}</option>
					   </c:forEach>
				   </select>
                   <span class="pr" style="display:inline-block;">
                   		<input type="text" value="${moneyBegin }" name="moneyBegin" id="startMoney" class="inputTxt" flag="istips" style="width:70px;" onkeyup="validNum(this)" onafterpaste="validNum(this))"/>
                   		<span class="ipt-tips ipt-tips2">输入金额</span>
                   </span> ~
               	   <span class="pr" style="display:inline-block;">
               	   		<input type="text" value="${moneyEnd }" name="moneyEnd" id="endMoney" class="inputTxt" flag="istips" style="width:70px;" onkeyup="validNum(this)" onafterpaste="validNum(this))"/>
               	   		<span class="ipt-tips ipt-tips2">输入金额</span>
               	   </span>
               </div>
               <c:if test="${confirmPay eq '0'}">
	               <div class="activitylist_bodyer_right_team_co1">
	       			   <div class="activitylist_team_co3_text">已付金额：</div>
	                   <select name="payedMoneyStatus">
	                        <option value="Y">全部</option>
					    	<option value="N" <c:if test="${payedMoneyStatus eq 'N'}">selected="selected"</c:if>>未付</option>
	                   </select>
	               </div>
               </c:if>
               <div class="kong"></div>
         	   <div class="activitylist_bodyer_right_team_co1" >
	               <div class="activitylist_team_co3_text">计调：</div>
                       <select name="operatorId">
                           <option value="">全部</option>
                           <c:forEach var="operator" items="${operator }">
                           <option value="${operator.key }" <c:if test="${operatorId==operator.key}">selected="selected"</c:if>>${operator.value }</option>
                           </c:forEach>
                       </select>
	           </div>
	           <div class="activitylist_bodyer_right_team_co1">
       			   <div class="activitylist_team_co3_text">打印状态：</div>
                   <select name="printStatus">
                        <option value="">全部</option>
				    	<option value="0" <c:if test="${printStatus eq '0'}">selected="selected"</c:if>>未打印</option>
				    	<option value="1" <c:if test="${printStatus eq '1'}">selected="selected"</c:if>>已打印</option>
                   </select>
               </div> 
		</div>
   </div>
</form:form>
</div>
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
            <ul>
            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
            <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

<table id="contentTable" class="table activitylist_bodyer_table">
        <thead>
	        <tr>
	        	<c:if test="${confirmPay eq '1'}">
	            	<th colspan="2" width="6%">序号</th>
	            </c:if>
	            <c:if test="${confirmPay ne '1'}">
	            	<th width="6%">序号</th>
	            </c:if>
	            <th width="8%">申请日期</th>
	            <th width="8%">团队类型</th>
	            <th width="10%">团号</th>
	            <th width="15%">团队名称</th>	
	            <th width="6%">计调</th>
	            <th width="10%">款项</th>
	            <th width="10%">付款金额<br />已付金额</th>
	            <c:if test="${confirmPay eq '1'}">
	            	<th width="6%">出纳确认</th>
	            </c:if>
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
        <c:forEach items="${page.list }" var="pay" varStatus="s">
            <tr>
            	<c:if test="${confirmPay eq '1'}">
            	<td class="tc"><input type="checkbox" name="checkBox" value="${pay.reviewId }" check-action="Normal" <c:if test="${pay.payStatus eq '1' }">disabled="disabled"</c:if>/> </td>
            	</c:if>
                <td class="tc">${s.count }</td>
                <td class="tc"><fmt:formatDate value="${pay.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td class="tc">${fns:getStringOrderStatus(pay.orderType)}</td>
				<td class="tc">${pay.groupCode }</td>
				<c:if test="${pay.orderType !=11 && pay.orderType !=12}">
					<td class="tc">${fns:getProductName(pay.productId, pay.orderType)}</td>
				</c:if>
				<c:if test="${pay.orderType ==11 || pay.orderType ==12}">
					<td>${pay.chanpname}</td>
				</c:if>
                <td class="tc">
                	<c:if test="${pay.operatorId != null && pay.operatorId != ''}">
                		${fns:getUserById(pay.operatorId).name}
                	</c:if>
                </td>
                <td class="tc">${fns:getStringOrderStatus(pay.orderType)}借款</td>
                <td class="tr">
                	<div class="yfje_dd">
                	<span class="fbold">${fns:getMoneyByReviewId(pay.reviewId,'0')}</span> </div>
                	<div class="dzje_dd"><span class="fbold">${fns:getRefundPayedMoney(pay.reviewId,'4',pay.orderType)}</span></div>
                </td>
                <c:if test="${confirmPay eq '1'}">
	                <td class="tc">
	               		<c:if test="${pay.payStatus eq '0' }">
	               			<font style="color:red">未付</font>
	               		</c:if>
	               		<c:if test="${pay.payStatus eq '1' }">
	               			<font style="color:green">已付</font>
	               		</c:if>
	                </td>
                </c:if>
                <c:choose>
				     <c:when test="${pay.printFlag == '1'}">
				     	 <td class="tc"><p class="uiPrint" style="color:green;">已打印<span style="display: none;color:purple;">首次打印日期<br><fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${pay.printTime}"/></span></p></td>
				     </c:when>
				     <c:otherwise>
				     	<td class="tc" style="color:red;">未打印</td>
				     </c:otherwise>
				</c:choose>
                <td class="p0">
                    <dl class="handle">
                       	<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
	                   	<dd><p>
                           	<span></span>
                            <a href="${ctx}/cost/payManager/pay/203?orderType=${pay.orderType}&orderId=${pay.orderId}&travelerId=${pay.travelerId}&reviewId=${pay.reviewId}&agentId=${pay.agentId}" target="_blank">付款 </a>
                            <c:if test="${pay.orderType == 6}">
                            	<a href="${ctx }/cost/review/visaRead/${pay.productId}/0?menuid=4" target="_blank">查看</a>
                            	<a href="${ctx }/cost/manager/forcastList/${pay.productId}/${pay.orderType}" target="_blank">预报单</a>
                              	<a href="${ctx }/cost/manager/settleList/${pay.productId}/${pay.orderType}" target="_blank">结算单</a>
                            </c:if>
                            <c:if test="${pay.orderType == 7}">
                            	<a target="_blank" href="${ctx}/cost/review/airTicketRead/${pay.productId}/0?menuid=4">查看</a>
                                <a href="${ctx }/cost/manager/forcastList/${pay.productId}/${pay.orderType}" target="_blank">预报单</a>
                              	<a href="${ctx }/cost/manager/settleList/${pay.productId}/${pay.orderType}" target="_blank">结算单</a>
                            </c:if>
                            <c:if test="${pay.orderType < 6 || pay.orderType == 10}">
                            	<a target="_blank" href="${ctx}/cost/review/read/${pay.productId}/${pay.groupId}/0?from=operatorPre&menuid=4">查看</a>
                                <a href="${ctx }/cost/manager/forcastList/${pay.groupId}/${pay.orderType}" target="_blank">预报单</a>
                              	<a href="${ctx }/cost/manager/settleList/${pay.groupId}/${pay.orderType}" target="_blank">结算单</a>
                            </c:if>
                            <c:if test="${pay.orderType == 11}">
                            	<a target="_blank" href="${ctx}/cost/review/hotelRead/${pay.productId}/${pay.groupId}/1?menuid=4">查看</a>
                                <a href="${ctx }/cost/manager/forcastList/${pay.groupId}/${pay.orderType}" target="_blank">预报单</a>
                              	<a href="${ctx }/cost/manager/settleList/${pay.groupId}/${pay.orderType}" target="_blank">结算单</a>
                              	<a href="${ctx }/cost/manager/returnMoneyList4JK/${pay.reviewId}/${pay.orderType}"
												target="_blank">打印</a>
                            </c:if>
                            <c:if test="${pay.orderType == 12}">
                            	<a target="_blank" href="${ctx}/cost/review/islandRead/${pay.productId}/${pay.groupId}/1?&menuid=4">查看</a>
                                <a href="${ctx }/cost/manager/forcastList/${pay.groupId}/${pay.orderType}" target="_blank">预报单</a>
                              	<a href="${ctx }/cost/manager/settleList/${pay.groupId}/${pay.orderType}" target="_blank">结算单</a>
                              	<a href="${ctx }/cost/manager/returnMoneyList4JK/${pay.reviewId}/${pay.orderType}"
												target="_blank">打印</a>
                            </c:if>
                            <a onclick="expand('#child1_${pay.reviewId}_${s.count}',this,${pay.reviewId},'4',${s.count},${pay.orderType})"  href="javascript:void(0)">支付记录</a>
                            <c:if test="${confirmPay eq '1'}">
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
                            <c:if test="${pay.isShowPrint == '2' && pay.orderType == 6}">
                            	<a target="_blank"  href="${ctx}/visa/workflow/borrowmoney/visaBorrowMoney4XXZFeePrint?revid=${pay.reviewId}">打印</a><br />
                            </c:if>
                            <!-- 其他产品打印，暂不支持酒店和海岛游 -->
                            <shiro:hasPermission name="review:print:down">
                            	<c:if test="${pay.orderType != 6 && pay.orderType != 11 && pay.orderType != 12}">         
                            		<a href="${ctx}/printForm/borrowMoneyForm?reviewId=${pay.reviewId}&orderType=${pay.orderType}" target="_blank">打印</a><br/>
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
						<tbody id='rpi_${pay.reviewId}_${s.count}'>
						
						</tbody>
					</table>
				</td>
			</tr>	
        </c:forEach>
        </tbody> 
    </table>
    <c:if test="${confirmPay eq '1'}">
	    <div id="contentTable_foot">
	        <label><input check-action="All" type="checkbox"> 全选</label>
	        <label><input check-action="Reverse" type="checkbox"> 反选</label>
	        <input type="button" class="btn-primary" value="批量确认付款" onclick="batchConfirmOrCannePay('${ctx }','checkBox','4',1)" />
	    </div>
    </c:if>
</div>
<div class="pagination clearFix">${page}</div>
</body>
</html>