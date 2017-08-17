<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="ydbz_tit"><span class="ydExpand" data-target="#costTable"></span>费用及人数</div>
<div id="costTable">
	<table id="moneyAndPeopleTab" class="table activitylist_bodyer_table_new contentTable_preventive">
		<thead>
			<tr>
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
						<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.travelerType}"/>
					</td>
					<td class="tc">
						<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/>${groupPrice.price }</span>
					</td>
					<td class="tc"><input type="hidden" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum" value="${groupPrice.num }"/>${groupPrice.num }</td>
					<td class="tc"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/><span>${groupPrice.subTotal }</span></td>
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
						<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.currencyId }"/>&nbsp;${activityHotelGroup.singlePrice }
					</span>
					<span class="price_sale_houser_25" style="margin-left:200px;"><label>需交定金：</label>
						<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.frontMoneyCurrencyId }"/>&nbsp;${activityHotelGroup.frontMoney }
					</span>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="activitylist_bodyer_right_team_co1" style="width: 150px;">
		 <label>预报名间数：</label>${hotelOrder.forecaseReportNum }&nbsp;间
	</div>
	<div class="activitylist_bodyer_right_team_co1" style="width: 320px;">
		<div class="activitylist_team_co3_text" style="width:130px; font-weight:normal;">酒店扣减间数：控房&nbsp;${hotelOrder.subControlNum }间</div> 
		<input id="subControlNum" name="subControlNum" type="hidden" data-type="number" class="inputTxt" readonly="readonly" value="${hotelOrder.subControlNum }"/>
	</div>
	<div class="activitylist_bodyer_right_team_co1"	style="width: 320px;">
		<div class="activitylist_team_co3_text" style="width: 100px; font-weight: normal;">非控房：${hotelOrder.subUnControlNum }间</div>
		<input type="hidden" id="subUnControlNum" name="subUnControlNum"  value="${hotelOrder.subUnControlNum }" data-type="number"  class="inputTxt" /> 
	</div>
	<ul class="ydbz_qd_02"><li><label>合计：</label> <span id="hotelRoomTotalNumber"></span>间</li></ul>
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
        if (cost[k].price) {
            if (cost[k].price > 0 && str.length) {
                str.push(" + ");
            } else if (cost[k].price < 0) {
                str.push(" - ");
            }
            str.push(cost[k].code + Math.abs(cost[k].price));
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
        var count = parseInt($("#" + traveller.type).find("td:eq(2) input").val());
        if (count) {
            var cost = {};
            for (var k in traveller.cost) {
                totalCost[k] || (totalCost[k] = { code: traveller.cost[k].code, price: 0 });
                cost[k] = { code: traveller.cost[k].code, price: traveller.cost[k].price * count };
                totalCost[k].price += cost[k].price;
            }
            totalCount += count;
        }
    }
    orderInfo.totalCost = totalCost;
    orderInfo.totalCount = totalCount;
    $("#totalPeopleCount").text(totalCount);//合计人数
    $("#totalPeopleMoney").text(formatCost(totalCost));	//合计金额
  	//酒店间数合计
    $("#hotelRoomTotalNumber").text((+($("#subControlNum").val()) || 0) + (+($("#subUnControlNum").val()) || 0));
}
peopleCountChange();
</script>