<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>返佣申请</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/rebates/islandRebatesList.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){  
			//input获得失去焦点提示信息显示隐藏  
			inputTips();
			rebates.init();
		});
	</script>
</head>
<body>
	
<div id="sea">

	<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
               <c:forEach items="${currencyList}" var="cur" varStatus="b">
                    <option value="${cur.id }" data-text="${cur.currencyMark }">${cur.currencyName }</option>
                    </c:forEach>
                </select>
 <!--右侧内容部分开始-->
 <input type="hidden" value="${productOrder.id}" id="orderId"/>
  <input type="hidden" value="${productOrder.uuid}" id="orderUuid"/>
 	<div class="bgMainRight_right">
 	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostAndNumInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostInfo.jsp"%>
         <div class="ydbz_tit pl20"><span class="fl">返佣</span></div>
                    <div class="jbox_batch_change_price fl" style="width:100%; height:30px; line-height:30px;margin:20px 0px 5px 0px; padding-top:5px; background-color:#FEF9E6;">
                        <ul class="ydbz_info">
                            <li style="width:80px; font-size:14px; color:#996c00;" class="batch"> 批量返佣</li>
                            <li style="width:190px;">
                                <span>币种：</span>
                                <select class="w80" id="currency">
                                    <c:forEach items="${currencyList}" var="cur" varStatus="b">
									 	<option data-text="${cur.currencyMark }" value="${cur.id }">${cur.currencyName }</option>
									 </c:forEach>
                                </select>
                            </li>
                            <li style="width:190px;"><span>返佣金额：</span><input id="batchMoney" class="w80" type="text" data-type="float" /></li>
                            <li style="width:190px;"><span>款项：</span><input id="batchFund" class="w80" type="text" /></li>
                            <li style="width:120px;"><span>人数：<label id="peopleCount">0</label>人</span></li>
                            <li style="width:150px;"><span>返佣总额：<label id="totalMoney">0.00</label></span></li>
                            <li style="width:200px;"><a class="ydbz_s" id="okLink">确认</a></li>
                        </ul>
                    </div>
                    <table id="commissionTable" class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new activitylist_bodyer_table_new">
                        <thead>
                            <tr>
                                <th width="7%">
                                    全选
                                    <input style="display: block; margin: 0px auto;" data-group="commission" name="allChk" id="allChk" type="checkbox">
                                </th>
                                <th width="7%">姓名</th>
                                <th width="7%">游客类型</th>
                                <th width="10%">应收金额</th>
                                <th width="10%">累计返佣金额</th>
                                <th width="15%">返佣款项</th>
                                <th width="8%">币种</th>
                                <th width="10%">本次返佣金额</th>
                                <th width="20%">备注</th>
                            </tr>
                        </thead>
                        <tbody>
                        
                        <c:forEach items="${travelerRebatesList}" var="travelerRebates" varStatus="s">
		  					<tr group="travler${s.count}">
		  					<td class="table_borderLeftN"><input type="checkbox" data-group="commission" name="checkSign" /></td>
								<td>${travelerRebates.traveler.name}
								<input type="hidden" name="travelerId" value="${travelerRebates.traveler.id}">
								<input type="hidden" name="travelerUuid" value="${travelerRebates.traveler.uuid}">
								</td>
								<td class="tc"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerRebates.traveler.personType}"/></td>
								<td class="tr">${fns:getIslandMoneyAmountBySerialNum(travelerRebates.traveler.payPriceSerialNum,2)}</td>
								<td class="tr">${fns:getTravelRebatesByOrderType(travelerRebates.traveler.uuid,12)}<input type="hidden" name="oldRebates" value="${travelerRebates.oldRebates}"></td>
								<td class="tc"><input class="inputTxt fund" name="costname"
									type="text" />
								</td>
								<td class="tc" width="8%"><select name="gaijiaCurency" class="currency selectrefund" style="width:90%;"
									nowvalue="2">
										<c:forEach items="${currencyList}" var="currency">
											<option value="${currency.id}"
												data-text="${currency.currencyMark}">${currency.currencyName}</option>
										</c:forEach>
								</select></td>
								<td width="10%">
									<input type="text" maxlength="9" name="refund" class="money" data-type="float"  onkeyup="refundInput(this)" onafterpaste="refundInput(this))" />
								</td>
								<td width="20%"><input name="remark" type="text" />
								</td>
							</tr>
		  				</c:forEach>                        
                            <tr>
                                <td class="tc">总计</td>
                                <td colspan="9" class="tc">
                                <span class="f14 all-money mar_ri20">应收总金额：<span class="red20">${totalTravelerPrice}</span></span> <span class="f14 all-money mar_l20">累计返佣总金额：<span class="red20">${totalRebatesMoney }</span></span> <span class="f14 all-money mar_l20">本次返佣总金额：<span id="totalAmount"><span class="red20">0.00</span></span></span></td>
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
                        <input type="button" name="apply" value="提交" class="btn btn-primary">
                    </div>
                <div class="ydbz_tit pl20 cl-both"><span class="fl">返佣记录</span></div>
	                <table id="contentTable" class="activitylist_bodyer_table_new">
						<thead>
							<tr>
								<th width="7%">申请日期</th>
								<th width="7%">姓名</th>
								<th width="7%">舱位等级</th>
								<th width="7%">游客类型</th>
								<th width="7%">款项</th>
								<th width="9%">应收金额</th>
								<th width="9%">累计返佣金额</th>
								<th width="7%">币种</th>
								<th width="9%">本次返佣金额</th>
								<th width="12%">备注</th>
								<th width="6%">审批状态</th>
							</tr>
						</thead>	
							<!-- 无查询结果 -->
							<c:if test="${fn:length(rebatesList) <= 0 }">
								<tr class="toptr">
									<td colspan="12" style="text-align: center;">暂无返佣记录</td>
								</tr>
							</c:if>
							<c:forEach items="${rebatesList}" var="rebates">
								<tr>
									<td class="tc">
										<fmt:formatDate pattern="yyyy-MM-dd" value="${rebates.createDate}" />
									</td>
									<td class="tc">
										<c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if>
									</td>
									<td class="tc">${fns:getDictLabel(rebates.traveler.spaceLevel,"spaceGrade_Type" , "无")}</td>
									<td class="tc">
										<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" 
											value="${rebates.traveler.personType}"/>
									</td>
									<td>${rebates.costname}</td>
									<td class="tr">${fns:getIslandMoneyAmountBySerialNum(rebates.totalMoney,1)}</td>
									<td class="tr">${rebates.oldRebates}</td>
									<td>${rebates.currency.currencyName}</td>
				
									<td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
									<td>${rebates.remark}</td>
									<td class="tc tdgreen" title="">审核通过</td>
								</tr>
							</c:forEach>
				</table>
          </div>  
    </div>
    <!--右侧内容部分结束--> 
    
</div>
<script type="text/javascript">
         $(document).ready(function () {
        
        $("#allChk").click(function(){
				if($(this).attr('checked') == 'checked'){
					$("input[name='checkSign']").attr('checked','checked');
				}else{
					$("input[name='checkSign']:checked").removeAttr('checked');
				}
			});
			
            $("#okLink").on('click', function () {
                var currency = $("#currency").val();
                var money = (+($("#batchMoney").val()) || 0);
                var fund = $("#batchFund").val();
                var $trs = $("#commissionTable tbody tr").has("td:first input:checkbox:checked");
                $trs.find("select.currency").val(currency);
                $trs.find("input.money").val(money);
                $trs.find("input.fund").val(fund);

                totalAmount();
            });

            $("#batchMoney, #currency").on('change', function () {
                var count = $("#commissionTable tbody input:checkbox:checked").length;
                $("#peopleCount").text(count);
                var money = (+($("#batchMoney").val()) || 0);
                $("#totalMoney").text($("#currency option:selected").attr("data-text") + (money * count));
            });

            $("#clearBtn").on('click', function () {
                $("#commissionTable select.currency").val("");
                $("#commissionTable input.money").val("");
                $("#commissionTable input.fund").val("");
                totalAmount();
            });

            $("#commissionTable").on('change', "input.money, select.currency", function () {
                totalAmount();
            }).on('change', "input:checkbox", function () {
                setTimeout(function () { $("#batchMoney").change() }, 10);
            });

            function totalAmount() {
                var amount = {};
                $("#commissionTable tbody tr:not(:last)").each(function () {
                    var currency = $(this).find("select.currency option:selected").attr("data-text");
                    var money = +($(this).find("input.money").val());
                    amount[currency] = (amount[currency] || 0) + (money || 0);
                });
                var text = [];
                for (var key in amount) {
                    if (amount[key]) {
                        var join = (text.length && amount[key] > 0) ? " + " : (amount[key] < 0 ? " - " : "");
                        var money = Math.abs(amount[key]);
                        text.push(join, '<span class="red20">', key, money, '</span>');
                    }
                }
                $("#totalAmount").html(text.join(''));

                //清空
                $("#currency").val('');
                $("#batchMoney").val('');
                $("#batchFund").val('');
                //$("#commissionTable input:checkbox").prop("checked", false);
                $("#batchMoney").change();
            }
        });
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
		 var ms = obj.value.replace(/[^\d\.\-]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
         var txt = ms.split(".");
         obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
   }
    </script>
</body>
</html>