<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="ydbz_tit"><span class="ydExpand" data-target="#costTable"></span>费用及人数</div>
<div id="costTable">
	<table id="moneyAndPeopleTab" class="table activitylist_bodyer_table_new contentTable_preventive">
		<thead>
			<tr>
				<th width="12%">舱位等级</th>
				<th width="13%">游客类型</th>
				<th width="25%">同行价/人</th>
				<th width="25%"><span class="xing">*</span>人数</th>
				<th width="25%">小计</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${groupPrices }" var="groupPrice">
				<tr id="${groupPrice.uuid}" class="groupPrices_tr">
					<td class="tc">
						${groupPrice.spaceLevel}
					</td>
					<td class="tc">
						<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.travelerType}"/>
					</td>
					<td class="tc">
						<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/>${groupPrice.priceStr }</span>
					</td>
					<td class="tc"><input type="hidden" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum" value="${groupPrice.num }"/>${groupPrice.num }</td>
					<td class="tc"><span>${groupPrice.subTotal }</span></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="5" class="tr">
					<span class="price_sale_houser_25"><label>合计人数：</label>
						<em> <span id="totalPeopleCount"></span> 人</em>
					</span> 
					<span class="price_sale_houser_25"><label>合计金额：</label>
						<em><i><span class="totalCost" id="totalPeopleMoney"></span></i></em>
					</span>
				</td>
			</tr>
			<tr>
				<td colspan="5" class="tl">
					<span class="price_sale_houser_25" style="margin-left:89px;"><label>单房差：</label>
						<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityIslandGroup.currencyId }"/>&nbsp;
						<fmt:formatNumber type="currency" value="${activityIslandGroup.singlePrice }" pattern="#,##0.00" />
					</span>
					<span class="price_sale_houser_25" style="margin-left:200px;"><label>需交定金：</label>
						<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityIslandGroup.frontMoneyCurrencyId }"/>&nbsp;
						<fmt:formatNumber type="currency" value="${activityIslandGroup.frontMoney }" pattern="#,##0.00" />
					</span>
				</td>
			</tr>
		</tbody>
	</table>

<!-- 预报名间数 -->
<ul class="ydbz_qd_02" style="background-color:#FBFBFB;">
	<c:set var="forecaseReportRoomNum" value="${not empty islandOrder.forecaseReportRoomNum ? islandOrder.forecaseReportRoomNum : 0}"></c:set>
 	<c:set var="subControlNum" value="${not empty islandOrder.subControlNum ? islandOrder.subControlNum : 0}"></c:set>
 	<c:set var="subUnControlNum" value="${not empty islandOrder.subUnControlNum ? islandOrder.subUnControlNum : 0}"></c:set>
 	<c:set var="sumNum" value="${subControlNum + subUnControlNum}"></c:set>
	<li style="width:400px;">
		<label>预报名间数：${forecaseReportRoomNum }间</label>
		<label>酒店扣减间数：控房 ${subControlNum}间</label>
		<label>非控房 ${subUnControlNum}间</label>
	</li>
	<li>
		<label style=" margin-left:60px">合计：${sumNum}间</label>
	</li>
 </ul>
 
 <!-- 预报名票数 -->
 <ul class="ydbz_qd_02" style="background-color:#FBFBFB;">
 	<c:set var="forecaseReportTicketNum" value="${not empty islandOrder.forecaseReportTicketNum ? islandOrder.forecaseReportTicketNum : 0}"></c:set>
 	<c:set var="subControlTicketNum" value="${not empty islandOrder.subControlTicketNum ? islandOrder.subControlTicketNum : 0}"></c:set>
 	<c:set var="subUnControlTicketNum" value="${not empty islandOrder.subUnControlTicketNum ? islandOrder.subUnControlTicketNum : 0}"></c:set>
 	<c:set var="sumTicketNum" value="${subControlTicketNum + subUnControlTicketNum}"></c:set>
	<li style="width:400px;">
		<label>预报名间数：${forecaseReportTicketNum }间</label>
		<label>机票扣减间数：控票 ${subControlTicketNum}间</label>
		<label>非控票 ${subUnControlTicketNum}间</label>
	</li>
	<li>
		<label style=" margin-left:60px">合计：${sumTicketNum}间</label>
	</li>
 </ul>
 
 </div>

<script type="text/javascript">
var orderInfo = {
	    traveller: 
	    [
			<c:forEach items="${groupPrices }" var="groupPrice" varStatus="status">
			{ type: "${groupPrice.uuid}", 
			  name: "<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.travelerType}'/>",
			  cost: { 
				  "${groupPrice.currencyId}":{
					  	  code:'<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId}"/>',
					  	  price:"${groupPrice.price}",
					  }
				  }
			},
			</c:forEach>
	    ],
	    // 总人数
	    totalCount: 0
	};
function formatCost(cost) {
	var str = [];
    for (var k in cost) {
        if (cost[k].price!=null || cost[k].price!="") {
            if (cost[k].price > 0 && str.length) {
                str.push(" + ");
            } else if (cost[k].price < 0) {
                str.push(" - ");
            }
            str.push(cost[k].code + (Math.abs(cost[k].price)).toFixed(2));
        }
    }
    return str.join('');
}
//计算合计人数和合计金额
function peopleCountChange() {
	 // 总人数
    var totalCount = 0;
    // 总费用
    var totalCost = {};
    for (var l = orderInfo.traveller.length; l--;) {
        var traveller = orderInfo.traveller[l];
        var count = parseInt($("#" + traveller.type).find("td:eq(3) input").val());
        if (count>=0) {
            var cost = {};
            for (var k in traveller.cost) {
                totalCost[k] || (totalCost[k] = { code: traveller.cost[k].code, price: 0 });
                cost[k] = { code: traveller.cost[k].code, price: traveller.cost[k].price * count };
                totalCost[k].price += cost[k].price;
            }
            $("#" + traveller.type).find("td:eq(4)").text(formatCost(cost));
            totalCount += count;
        }
    }
    orderInfo.totalCost = totalCost;
    orderInfo.totalCount = totalCount;
    $("#totalPeopleCount").text(totalCount);//合计人数
    $("#totalPeopleMoney").text(formatCost(totalCost));	//合计金额
  	//酒店间数合计
    $("#hotelRoomTotalNumber").text((+($("#subControlNum").val()) || 0) + (+($("#subUnControlNum").val()) || 0));
  	//机票合计
    $("#ticketTotalNumber").text((+($("#subControlTicketNum").val()) || 0) + (+($("#subUnControlTicketNum").val()) || 0));
}
peopleCountChange();
</script>