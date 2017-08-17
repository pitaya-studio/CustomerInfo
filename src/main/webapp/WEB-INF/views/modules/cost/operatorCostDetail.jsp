<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>产品成本</title>
    <meta name="decorator" content="wholesaler"/>

    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js" type="text/javascript"></script>
    <script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
    <script type="text/javascript">
    $(function(){

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
        
        $('.handle').hover(function() {
	    	if(0 != $(this).find('a').length){
	    		$(this).addClass('handle-on');
				$(this).find('dd').addClass('block');
	    	}
		},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
        
/*         var elemToCal = document.getElementById('operatorBudget');
        if (elemToCal.addEventListener) { // Firefox, Opera and Safari
            elemToCal.addEventListener ('DOMSubtreeModified', function(){eventHandler('operatorBudget', $('#contentTable'));}, false);
        } else if (elemToCal.attachEvent) { // Internet Explorer
            elemToCal.attachEvent('onpropertychange', function(){eventHandler('operatorBudget', $('#contentTable'));});
        } */
        
        getCost('operatorBudget', $('#operatorBudget'));
        getCost('operator', $('#operatorSpecific'));
        var ids = "";
        var names = "";
        <c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
            ids = ids + "${data.id}"+",";
            names = names +"${data.name}"+",";
        </c:forEach>
        $("#targetAreaName").text(names.toString().substring(0,12)+'...');
        $("#targetAreaName").attr('title',names.toString().substring(0,names.length-1));
        eventHandler('operatorBudget', $('#contentTable'));
    });
    
    function orderDetail(orderId){
        window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
    }
    
    var eventHandler = function(mark, $Table){
        var sum = 0;
        var income = 0;
        var gain = 0;
        var datas = $('[price-sum-bind=\'' + mark + '\']');
        
        for(var i = 0; i < datas.length; i++){
            sum += parseInt(datas[i].innerHTML);
        }
        var orders = $Table.find('td:nth-child(8) span');
        for(var i = 0; i < orders.length; i++){
            var orderPrice = orders[i].innerHTML.replace(new RegExp(',', 'gm'), "");
            gain += parseInt(orderPrice);
        }
        income = gain - sum;
        $('[price-sum-all=\'' + mark + '\']').html(sum);
        $('[price-sum-gain=\'' + mark + '\']').html(gain);
        $('[price-sum-income=\'' + mark + '\']').html(income);
    };
    
    /*
    * 按类型获取成本
    */
    var getCost = function(classType, $List){
        var groupId = '${activityGroup.id}';
        $.ajax({
            type: "GET",
            url: "${ctx}/cost/manager/getCosts/" + classType + "/" + groupId,
            dataType:"json",
            async:false,
            cache:false,
            success: function(array){
                var htmldoc = "";
                for(var i = 0; i < array.length; i++){
                    htmldoc += '<tr id=' + classType + "_" + array[i].id + '> <td>' + (i + 1) + '</td><td>'+array[i].name+'</td><td price-sum-bind="' + classType + '">'+array[i].price+'</td><td>'+array[i].comment+'</td></tr>';
                }
                $List.append(htmldoc);
            }
         });
    };
</script>
</head>
<body>
    <%@ include file="head.jsp" %>

    <div class="produceDiv">
        <div class="mod_information">
            <div class="mod_information_d">
                <div class="ydbz_tit">产品基本信息</div>
            </div>
        </div>
        <div class="mod_information_dzhan">
            <div class="mod_information_dzhan_d mod_details2_d">
                <span style="font-size: 16px; font-weight: bold;" class="ydbz_mc">${travelActivity.acitivityName}</span>
                <div class="mod_information_d7"></div>
                <table border="0" width="90%">
                    <tr>
                        <td class="mod_details2_d1">产品编号：</td>
                        <td class="mod_details2_d2">
                            <c:choose> 
                                 <c:when test="${fn:length(travelActivity.activitySerNum) > 20}"> 
                                    <a style="text-decoration: none; color:inherit; cursor:default;" title="${travelActivity.activitySerNum}"><c:out value="${fn:substring(travelActivity.activitySerNum, 0, 20)}..." /></a> 
                                 </c:when> 
                                 <c:otherwise> 
                                    <c:out value="${travelActivity.activitySerNum}" /> 
                                 </c:otherwise>
                            </c:choose>
                        
                        </td>
                        <td class="mod_details2_d1">产品分类：</td>
                        <td class="mod_details2_d2">
                            <c:choose>
                                <c:when test="${travelActivity.overseasFlag eq 1}">国外</c:when>
                                <c:otherwise>国内</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="mod_details2_d1">旅游类型：</td>
                        <td class="mod_details2_d2">${travelActivity.travelTypeName}</td>
                        <td class="mod_details2_d1">出发城市：</td>
                        <td class="mod_details2_d2">${travelActivity.fromAreaName}</td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">目的地：</td>
                        <td class="mod_details2_d2" id="targetAreaName" title=""></td>
                        <td class="mod_details2_d1">交通方式：</td>
                        <td class="mod_details2_d2">
                            ${travelActivity.trafficModeName}
                            <c:if test="${!empty travelActivity.trafficName and fn:indexOf(relevanceFlagId,travelActivity.trafficMode)>=0 }">
                                &nbsp;&nbsp;|&nbsp;&nbsp;${travelActivity.trafficNameDesc}
                            </c:if>
                        </td>
                        <td class="mod_details2_d1">产品系列：</td>
                        <td class="mod_details2_d2">${travelActivity.activityLevelName}</td>
                        <td class="mod_details2_d1">团号：</td>
                        <td class="mod_details2_d2">${activityGroup.groupCode}</td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">产品类型：</td>
                        <td class="mod_details2_d2">${travelActivity.activityTypeName}</td>
                        <td class="mod_details2_d1">行程天数：</td>
                        <td class="mod_details2_d2" id="activityDuration">${travelActivity.activityDuration}</td>
                        <td class="mod_details2_d1">付款方式：</td>
                        <td colspan="3" class="mod_details2_d2">
                            <c:if test="${travelActivity.payMode_deposit eq 1}">
                                                                         订金占位
                                <c:if test="${travelActivity.remainDays_deposit != '' && travelActivity.remainDays_deposit != null}">
                                                                                 （保留${travelActivity.remainDays_deposit}天）&nbsp;&nbsp;
                                </c:if>
                            </c:if>
                            <c:if test="${travelActivity.payMode_advance eq 1}">
                                                                         预占位（保留${travelActivity.remainDays_advance}天）&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${travelActivity.payMode_full eq 1}">
                                                                          全款支付
                            </c:if>
                            <c:if test="${travelActivity.payMode_op eq 1}">
                                                                          计调确认占位
                            </c:if>
                            <c:if test="${travelActivity.payMode_cw eq 1}">
                                                                          财务确认占位
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">计调人员：</td>
                        <td class="mod_details2_d2">${travelActivity.createBy.name}</td>
                        <td class="mod_details2_d1">创建时间：</td>
                        <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${travelActivity.createDate }"/></td>
                        <td class="mod_details2_d1">更新时间：</td>
                        <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${travelActivity.updateDate }"/></td>   
                        <td class="mod_details2_d1">出团日期：</td>
                        <td class="mod_details2_d2"><fmt:formatDate value="${activityGroup.groupOpenDate }" pattern="yyyy-MM-dd"/></td>   
                    </tr>
                </table>
                <div class="kong"></div>
            </div>
        </div>
        <div class="mod_information">
            <div class="mod_information_d">
                <div class="ydbz_tit">订单列表</div>
            </div>
        </div>
        <c:set var="ordersize" value="${fn:length(orderList)}"></c:set>
        <table id="contentTable"
            class="activitylist_bodyer_table mod_information_dzhan"
            style="border-top: 1px solid #dddddd">
            <thead style="background: #403738;">
                <tr>
                    <th width="7%">预定渠道</th>
					<th width="11%">订单号</th>
		            <th width="13%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
					<th width="11%">预订时间</th>
		            <th width="8%">出/截团日期</th>
		            <th width="5%">人数</th>
		            <th width="8%">订单状态</th>
					<th width="8%">订单总额</th>
					<th width="8%">已付金额<br/>到账金额</th> 
		            <th width="6%">操作</th>
                </tr>
            </thead>
            <tbody>
                 <c:choose>
                     <c:when test="${ordersize eq 0}">
                         <tr class="toptr" >
                             <td colspan="10" style="text-align: center;">
                                                                                         暂无搜索结果
                             </td>
                         </tr>
                     </c:when>
                     <c:otherwise>
                        <c:forEach items="${orderList}" var="orders" varStatus="s">
                            <tr class="toptr">
                                <td>${orders.orderCompanyName }</td>
                                <td>${orders.orderNum }</td>
                                <td>
                                    <div class="tuanhao_cen onshow">
                                        <%--<c:choose>
                                            <c:when test="${fn:length(orders.groupCode) > 10}">
                                                <a style="text-decoration: none; color: inherit; cursor: default;" title="${orders.groupCode}">
                                                <c:out value="${fn:substring(orders.groupCode, 0, 10)}......" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${orders.groupCode}" />
                                            </c:otherwise>
                                        </c:choose>
                                    --%>${orders.groupCode}</div>
                                    <div class="chanpin_cen qtip" title="${orders.acitivityName}">
                                        <a href="javascript:void(0)"
                                            onclick="javascript:window.open('${ctx}/activity/manager/detail/${orders.activityId}')">${orders.acitivityName}</a>
                                    </div>
                                </td>
                                <td class="tc"><fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>

                                <td style="padding: 0px;"><div class="out-date">
                                        <fmt:formatDate value="${orders.groupOpenDate}"
                                            pattern="yyyy-MM-dd" />
                                    </div>

                                    <div class="close-date">
                                        <fmt:formatDate value="${orders.groupCloseDate}"
                                            pattern="yyyy-MM-dd" />
                                    </div>
                                </td>

                                <td>${orders.orderPersonNum }</td>
                                <td>${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}<c:if
                                                test="${orders.payStatus==1 or orders.payStatus == 2}">
                                                <br />(未占位)</c:if>
                                </td>

                                <td class="tr"><c:if test="${not empty orders.totalMoney }">¥</c:if>
                                	<span class="tdorange fbold"><fmt:formatNumber value='${orders.totalMoney}' type="currency" pattern="#,##0.00" /></span>
                               	</td>
                               	
                               	<td class="p0 tr">	
		                            <div class="yfje_dd">	
			                            <c:if test="${not empty orders.payedMoney and orders.payedMoney ne 0}">¥<span class="fbold"><fmt:formatNumber value='${orders.payedMoney}' type="currency" pattern="#,##0.00" /></c:if></span>
		                            </div>
		                            <div class="dzje_dd">
		                            	<c:if test="${not empty orders.accountedMoney }">¥<span class="fbold"><fmt:formatNumber value="${orders.accountedMoney }" type="currency" pattern="#,##0.00" /></c:if></span>
		                            </div>
	                            </td>
                                    	<td class="tc">
	                                       <shiro:hasPermission name="looseOrder:operation:view">
	                                           <a href="javascript:void(0)"
	                                               onclick="javascript:orderDetail(${orders.id});">详情 </a>
	                                       </shiro:hasPermission>
               							</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                 </c:choose>
            </tbody>
        </table>
            <div class="mod_information">
                <div class="mod_information_d">
                    <div class="ydbz_tit">成本预算</div>
                </div>
            </div>
            <table id="operatorBudgetTable"
                class="activitylist_bodyer_table mod_information_dzhan"
                style="border-top: 1px solid #dddddd">
                <thead>
                    <tr>
                        <th width="6%">序号</th>
                        <th width="25%">项目名称</th>
                        <th width="19%">金额</th>
                        <th width="25%">备注</th>
                    </tr>
                </thead>
                <tbody id="operatorBudget">
                </tbody>
            </table>
            <table class="mod_information_dzhan" style="line-height: 30px;">
                <tr>
                    <td>预计总成本：<span price-sum-all="operatorBudget"></span> 元</td>
                    <td>预计总毛利：<span price-sum-income="operatorBudget"></span> 元</td>
                    <td>预计总收入：<span price-sum-gain="operatorBudget"></span> 元</td>
                </tr>
            </table>
            <div class="mod_information">
                <div class="mod_information_d">
                    <div class="ydbz_tit">产品成本</div>
                </div>
            </div>
            <table id="operatorSpecificTable"
                class="activitylist_bodyer_table mod_information_dzhan"
                style="border-top: 1px solid #dddddd">
                <thead>
                    <tr>
                        <th width="6%">序号</th>
                        <th width="25%">项目名称</th>
                        <th width="19%">金额</th>
                        <th width="25%">备注</th>
                    </tr>
                </thead>
                <tbody id="operatorSpecific">
                </tbody>
            </table>
            <div class="release_next_add">
                <input class="btn btn-primary" type="button" value="关 闭"
                    onclick="javascript:window.close();" />
            </div>
    </div>
</body>
</html>
