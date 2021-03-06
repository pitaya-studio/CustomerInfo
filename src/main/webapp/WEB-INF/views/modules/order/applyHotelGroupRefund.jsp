<!-- 
author:chenry
createDate：2015-06-18
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>酒店申请退款
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){

	inputTips();
	
	currencyObj = {};
	loadCurrency();
    
function loadCurrency(){
   $.ajax({
		  type: "GET",
		  url: "${ctx}/order/refund/currencyJson",
		  dataType: "json",
		  async: false,
		  success:function(msg){
			  if(msg && msg.currencyList){
			      $.each(msg.currencyList, function(i,n){
			         currencyObj[n.id] = n;
			      });
			  }
		  }
		  
		});
}
//产品名称获得焦点显示隐藏
function inputTips(){
	$("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	$("input[flag=istips]").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}


//验证是否是数值
function formatNum(number){
	return Number(number) ? Number(number) : 0;
}

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = thisvalue.replace(/\D/g,"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}else{
		$(dom).val(0);
	}
}
	
});	 
function checkValueSingle(a){
	if(a.length <= 0){
		top.$.jBox.tip('请正确填写游客退款的信息！', 'error', { focusId: 'name' });
		return false;
	}else{
		return true;
	}
	
}

var submit_times = 0;
function submitForm(){
	if(submit_times != 0) {
		return false;
	}
	submit_times++;
	if ($("[name=checkSign]:checked").length > 0) {
		   checkFlag = true;
	   } else {
		   top.$.jBox.tip('请选择要退款的游客','warning');
		   return false;
	   }
	   if(checkFlag){
	   var travelerId=[];
	   var refundAmount=[];
	   var currency_id=[];
	   var refund=[];
	   var remark=[];
	   var orderId;
	   var selects=$("select[class='currency selectrefund']");
	   var checkArray1=[];
	   var checkArray2=[];
	   var objArray=[];
	   var orderTotal=$("#orderTotal").val();
	   selects.each(function(index, element) {
	   		var si = $(selects[index]);
         	var s = si.val();
            var checkinput=si.parents("tr").find("input[type='checkbox']");
	   		if(checkinput.prop("checked")){
				         var obj={};
					     var n = si.parent().parent().find("input[name='refund']").val();					     
					     if(n==""|| n ==undefined){n=0;}else{n=n;}
					     var orderTotalTr=si.parent().parent().find("input[name='orderTotalTr']").val();
					     if(orderTotalTr==""|| orderTotalTr ==undefined){orderTotalTr=0;}else{orderTotalTr=orderTotalTr;}
					     refund.push(n);
					     currency_id.push(s);
					     var remarks = si.parent().parent().find("input[name='remark']").val();
					     remark.push(remarks);
					     var ra=si.parent().parent().find("input[name='refundAmount']").val();
					     refundAmount.push(ra);
					     var tid=si.parent().parent().find("input[name='travelerId']").val();
					     travelerId.push(tid);
					     checkArray1.push(s+ra+tid);
					     
					    obj.travelerId = tid;
			        	obj.travelerName = si.parent().parent().find("input[name='travelerName']").val();
			        	if (ra) {
			        		obj.refundName = ra;
			        	}
			        	obj.currencyId = s;
			        	obj.currencyName = currencyObj[obj.currencyId].currencyName;
			        	obj.currencyMark = currencyObj[obj.currencyId].currencyMark;
			        	obj.payPrice =orderTotalTr;
			        	obj.refundPrice = n;
			        	obj.remark = remarks;
			        	obj.status = 1;
			        	if("" != obj.refundPrice && undefined !=obj.refundPrice && 0 < obj.refundPrice){
			        		objArray.push(obj);	
			        	}
			        					     				      
			}		 			
	   	   
	   });
	   var flag=checkValueSingle(checkArray1);
	   var submitNum = 1;
	   if(flag){
		   if (submitNum == 1) {
			   submitNum++;
			   $.ajax({
	                type: "POST",
	                url: "${ctx}/orderCommon/manage/saveHotelGroupRefundInfo",
	                data: {
	                	refundRecordsStr : JSON.stringify(objArray),
	                    travelerId:travelerId.join(','),
	                    refundAmount :refundAmount.join(','),
	                    currencyId :currency_id.join(','),
	                    refund :refund.join(','),
	                    remark :remark.join(','),
	                    productType:11,flowType:1,orderId:"${orderId}",orderUuid:"${orderUuid}"
	                    
	                },
	                success: function(msg){
		                if(msg.sign == 1){
				            	window.location.href ="${ctx}/orderCommon/manage/viewHotelGroupRefund?productType=11&orderId=${orderId}&orderUuid=${orderUuid}&flowType=1";
				            }else{
				            	submitNum--;
				            	top.$.jBox.tip(msg.result, 'error', { focusId: 'name' });
				            }
		                }
	            });
			}
	   }
	   
	   }	   	   	   	   
	}  
</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}

.gaijia-add,.gaijia-delete{
	display:block; width:16px; height:16px;
	position:absolute; top:-16px; right:5px;
	cursor:pointer;
	background:url('${ctxStatic}/images/button_add.gif') no-repeat;
}
.gaijia-delete{
	top:-35px;
	background:url('${ctxStatic}/images/chazi_07.gif') -2px -2px no-repeat;
}
.refundtd input[type="text"]{width:70%}
.refundtd .gaijia-delete,.refundtd .gaijia-add{top:-24px;}

</style>

</head>

<body>
<div id="sea">

	<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    
 <!--右侧内容部分开始-->
 	<div class="bgMainRight_right">
 	<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderHotelBaseinfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderCostAndNumInfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderCostInfo.jsp"%>
         <div class="ydbz_tit pl20"><span class="fl">退款</span></div>
                    <div class="jbox_batch_change_price fl" style="width:100%; height:30px; line-height:30px;margin:20px 0px 5px 0px; padding-top:5px; background-color:#FEF9E6;">
                        <ul class="ydbz_info">
                            <li style="width:80px; font-size:14px; color:#996c00;" class="batch"> 批量退款</li>
                            <li style="width:190px;">
                                <span>币种：</span>
                                <select class="w80" id="currency">
                                    <c:forEach items="${currencyList}" var="cur" varStatus="b">
									 	<option data-text="${cur.currencyMark }" value="${cur.id }">${cur.currencyName }</option>
									 </c:forEach>
                                </select>
                            </li>
                            <li style="width:190px;"><span>退款金额：</span><input id="batchMoney" class="w80" type="text" data-type="float" /></li>
                            <li style="width:190px;"><span>款项：</span><input id="batchFund" class="w80" type="text" /></li>
                            <li style="width:120px;"><span>人数：<label id="peopleCount">0</label>人</span></li>
                            <li style="width:150px;"><span>退款总额：<label id="totalMoney">0.00</label></span></li>
                            <li style="width:200px;"><a class="ydbz_s" id="okLink">确认</a></li>
                        </ul>
                    </div>
                    <table id="contentTable" class="table activitylist_bodyer_table activitylist_bodyer_table_new">
                        <thead>
                            <tr>
                                <th width="5%" class="table_borderLeftN">
                                    全选
                                    <input type="checkbox"class="chkAll" name="allChk" id="allChk"/>
                                </th>
                                <th width="7%">姓名</th>
                                <th width="7%">游客类型</th>
                                <th width="10%">应收金额</th>
                                <th width="10%">累计退款金额</th>
                                <th width="15%">退款款项</th>
                                <th width="8%">币种</th>
                                <th width="10%">本次退款金额</th>
                                <th width="20%">备注</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${travelerList}" var="travelerRebates" varStatus="s">
		  					<tr group="travler${s.count}">
		  					<td class="table_borderLeftN"><input type="checkbox" data-group="refund" name="checkSign" /></td>
								<td>${travelerRebates.name}<input type="hidden" name="travelerId" value="${travelerRebates.id}"></td>
								<td class="tc"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerRebates.personType}"/></td>
								<td class="tr">${fns:getHotelMoneyAmountBySerialNum(travelerRebates.payPriceSerialNum,2)}</td>
								<td class="tr">${fns:getRefundPayMoneyTravelByOrderType(orderUuid,travelerRebates.uuid,11)}</td>
								
								<td colspan="6" class="p0 details_border_none">
                                    <table class="refundTable details_border_bot_none" width="100%">
                                        <tr>
                                            <td width="15%" class="refundtd">
                                                <input class="inputTxt fund" name="refundAmount" type="text"/>
                                                <div class="pr"><i title="添加款项" class="gaijia-add"></i>
                                                	<input name="travelerName" value="${travelerRebates.name}" type ="hidden"/>
                                                	<input name="travelerId" value="${travelerRebates.id}" type ="hidden"/>
                                                </div>
                                            </td>
                                            <td class="tc" width="8%">
                                                <select name="gaijiaCurency" class="currency selectrefund" style="width:90%;">
													<c:forEach items="${currencyList}" var="currency">
														<option value="${currency.id}" data-text="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select>
                                            </td>
                                            <td width="10%"><input type="text" maxlength="9" name="refund" class="money" data-type="float"  onkeyup="refundInput(this)" onafterpaste="refundInput(this))" /></td>
                                            <td width="20%"><input name="remark" type="text"/></td>
                                        </tr>
                                    </table>
                                </td>
							</tr>
		  				</c:forEach>                        
                            <tr>
                                <td class="tc">总计</td>
                                <td colspan="9" class="tc"><span class="f14 all-money mar_ri20">应收总金额：<span class="red20">${totalTravelerPrice}</span></span> <span class="f14 all-money mar_l20">累计退款总金额：<span class="red20">${ refundTotal}</span></span> <span class="f14 all-money mar_l20">本次退款总金额：<span id="totalAmount"><span class="red20">0.00</span></span></span></td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="jbox_batch_change_price fl" style="width:1100px;margin:20px auto;">
                        <ul class="ydbz_info">
                            <li style="width:110px;" class="batch">
                                <input id="clearBtn" class="mod_infoinformation3_file maring_bottom0 btn_sign_up_sea" value="全部清空" type="button" />
                            </li>
                        </ul>
                    </div>
                    <div class="dbaniu cl-both">
                        <a class="ydbz_s gray" onclick="window.close();">取消</a>
                        <a class="ydbz_s" href="javascript:submitForm()">提交</a>
                    </div>
                <div class="ydbz_tit pl20 cl-both"><span class="fl">退款记录</span></div>
                <table id="contentTable" class="activitylist_bodyer_table_new">
		<thead>
			<tr>
				<th width="7%">申请日期</th>
				<th width="7%">姓名</th>
				<th width="7%">游客类型</th>
				<th width="7%">款项</th>
				<th width="9%">应收金额</th>
				<th width="9%">累计退款金额</th>
				<th width="7%">币种</th>
				<th width="9%">本次退款金额</th>
				<th width="12%">备注</th>
				<th width="6%">审批状态</th>
			</tr>
		</thead>
		<tbody>
			<!-- 无查询结果 -->
			<c:if test="${fn:length(bAList) <= 0 }">
				<tr class="toptr">
					<td colspan="12" style="text-align: center;">暂无退款记录</td>
				</tr>
			</c:if>
			<c:forEach items="${bAList}" var="rebates">
				<tr>
					<td class="tc"><fmt:formatDate value="${rebates.applyDate }" pattern="yyyy-MM-dd" /></td>
					<td class="tc">${rebates.travelerName}</td>
					<td class="tc"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${rebates.personType}"/></td>
					<td>${rebates.refundName}</td>
					<td class="tr">${fns:getHotelMoneyAmountBySerialNum(rebates.totalMoney,2)}</td>
					<td class="tr">${rebates.sumRefundPrice}</td>
					<td>${rebates.currencyName}</td>

					<td class="tr">${rebates.currencyMark}${rebates.refundPrice}</td>
					<td>${rebates.remark}</td>
					<td class="tc tdgreen" title="">审核通过</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
          </div>  
    </div>
    <!--右侧内容部分结束--> 
    
</div>
<script type="text/javascript">
        $(document).ready(function () {
            $("#okLink").on('click', function () {
                var currency = $("#currency").val();
                var money = (+($("#batchMoney").val()) || 0);
                var fund = $("#batchFund").val();
                var $trs = $("#contentTable tbody tr").has("td:first input:checkbox:checked");
                $trs.find("select.currency").val(currency);
                $trs.find("input.money").val(money);
                $trs.find("input.fund").val(fund);

                totalAmount();
            });

            $("#batchMoney, #currency").on('change', function () {
                var $tr = $("#contentTable tbody tr").has("input:checkbox:checked");
                var count = $tr.find("table tbody tr").length;
                $("#peopleCount").text($tr.length);
                var money = (+($("#batchMoney").val()) || 0);
                $("#totalMoney").text("" + $("#currency option:selected").attr("data-text") + (milliFormat(money * count,'1')));
            });

            $("#clearBtn").on('click', function () {
                $("#contentTable select.currency").val("");
                $("#contentTable input.money").val("");
                $("#contentTable input.fund").val("");
                totalAmount();
            });

            $("#contentTable").on('change', "input.money, select.currency", function () {
                totalAmount();
            }).on('change', "input:checkbox", function () {
                setTimeout(function () { $("#batchMoney").change(); }, 10);
            }).on('click', 'i.gaijia-add', function () {
                var $tempTr = $("table.refundTable tr").has(this).clone();
                $tempTr.find("i.gaijia-add").replaceWith('<i class="gaijia-delete" title="删除款项"></i>');
                $tempTr.find("input").val('');
                $("table.refundTable tbody").has(this).append($tempTr);
                $("#batchMoney").change();
            }).on("click", 'i.gaijia-delete', function () {
                $(this).parentsUntil("tr").parent().remove();
                totalAmount();
            });

            function totalAmount() {
                var amount = {};
                $("table.refundTable tbody tr").each(function () {
                    var currency = $(this).find("select.currency option:selected").attr("data-text");
                    var money = +($(this).find("input.money").val());
                    amount[currency] = (amount[currency] || 0) + (money || 0);
                });
                var text = [];
                for (var key in amount) {
                    if (amount[key]) {
                        var join = (text.length && amount[key] > 0) ? " + " : (amount[key] < 0 ? " - " : "");
                        var money = Math.abs(amount[key]);
                        text.push(join, '<span class="red20">', key, milliFormat(money,'1'), '</span>');
                    }
                }
                $("#totalAmount").html(text.join(''));

                //清空
                $("#currency").val('');
                $("#batchMoney").val('');
                $("#batchFund").val('');
                //$("#contentTable input:checkbox").prop("checked", false);
                $("#batchMoney").change();
            }
            $("#allChk").click(function(){
				if($(this).attr('checked') == 'checked'){
					$("input[name='checkSign']").attr('checked','checked');
				}else{
					$("input[name='checkSign']:checked").removeAttr('checked');
				}
			});
        });

        //批量退款 弹窗
        function jbox_batch_change_price() {
            $.jBox($(".jbox_batch_change_price_bg").html(), {
                title: "批量退款", buttons: { '确定': 1 }, submit: function (v, h, f) {
                }, height: '560', width: 200
            });
        }
        //数字添加千位符
function milliFormat(s,isFloat){
	var minusSign = false;
	if((typeof s) != String){
		s = s.toString();
	}
	if(/^\-/.test(s)){
		minusSign = true;
		s = s.substring(1);
	}
	if(/[^0-9\.]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	if(isFloat){
		s=s.replace(/,(\d\d)$/,".$1");
	}else{
		s=s.replace(/,(\d\d)$/,"");
	}
	if(minusSign){
		s= '-' + s;
	}
	return s.replace(/^\./,"0.");
}
//订单团队退款
    function refundInput(obj){
		 var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
         var txt = ms.split(".");
         obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
	    totalRefund();
   }
</script>
</body>
</html>
