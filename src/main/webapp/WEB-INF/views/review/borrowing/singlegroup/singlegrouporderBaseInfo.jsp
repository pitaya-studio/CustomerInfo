<%@ page contentType="text/html;charset=UTF-8" %>
<div class="ydbz_tit">订单信息</div>
	<input type="hidden" value="${productOrder.id}" id="orderId"/>
	<input type="hidden" value="${productOrder.orderStatus}" id="orderType"/>
    <div class="orderdetails1">
	   	<table border="0" style="margin-left: 25px" width="98%">
			<tbody>
				<tr>
					<td class="mod_details2_d1">下单人：</td>
			        <td class="mod_details2_d2">${productOrder.createBy.name}</td>
			        <td class="mod_details2_d1">销售：</td>
			        <c:set value="${productOrder.salerId}" var="saler"></c:set>
			        <td class="mod_details2_d2">${fns:getUserNameById(saler)}</td>
					<td class="mod_details2_d1">下单时间：</td>
					<td class="mod_details2_d2"><fmt:formatDate value="${productOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
		            <td class="mod_details2_d1">团队类型：</td>
			        <td class="mod_details2_d2">${orderStatusStr}</td>	
				</tr>
				<tr> 
		            <td class="mod_details2_d1">订单编号：</td>
					<td class="mod_details2_d2">${productOrder.orderNum}</td>
		            <td class="mod_details2_d1">订单团号：</td>
					<td class="mod_details2_d2">${productGroup.groupCode}</td>
		            <td class="mod_details2_d1">订单总额：</td>
					<td class="mod_details2_d2">${fns:getMoneyAmountBySerialNum(productOrder.totalMoney,2)}</td>
		            <td class="mod_details2_d1">订单状态：</td>
					<td class="mod_details2_d2">${payModeStr}</td>	 
		        </tr>
				<tr>
					<td class="mod_details2_d1">操作人：</td>
			        <td class="mod_details2_d2">${product.createBy.name}</td>
			        
				</tr>
			</tbody>
		</table>
    </div>
<div class="ydbz_tit">产品信息</div>
<div class="orderdetails2">
    <p class="ydbz_mc">${product.acitivityName }</p>
	<ul class="ydbz_info">
		<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
		<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
		<li><span>行程天数：</span>${product.activityDuration}天</li>
		<li id="mddtargetAreaNames" title="${product.targetAreaNames}" style="display:block; width:200px; text-overflow:ellipsis; overflow:hidden; white-space:nowrap;" class="orderdetails2_text"><span>目的地：</span>${product.targetAreaNames}</li>
	</ul>
	<ul class="ydbz_dj specialPrice">
		<c:choose>
			<c:when test="${productOrder.orderStatus == '10'}">
				<li><span class="ydtips">舱型&nbsp;${fns:getDictLabel(productGroup.spaceType, 'cruise_type', '-')}</span>
					<p>1/2人同行价：${fns:getMoneyAmountBySerialNum(productOrder.settlementAdultPrice,2)}</p>
					<p>&nbsp;</p>
					<p>3/4人同行价：${fns:getMoneyAmountBySerialNum(productOrder.settlementcChildPrice,2)}</p>
				</li>
				<li><span class="ydtips"> 出行人数</span>
					<p>1/2人出行人数：<span>${productOrder.orderPersonNumAdult}</span> 人</p>
					<p>&nbsp;</p>
					<p>3/4人出行人数：<span>${productOrder.orderPersonNumChild}</span> 人</p>
				</li>
				<li style="background:none;">
			       <p>&nbsp;</p>
			       <p>总计：<span>${productOrder.roomNumber}</span> 间  </p>
			       <p>&nbsp;</p>
			    </li>
			</c:when>
			<c:otherwise>
				<li><span class="ydtips">单价</span>
					<p>成人：${fns:getMoneyAmountBySerialNum(productOrder.settlementAdultPrice,2)}</p>
					<p>儿童：${fns:getMoneyAmountBySerialNum(productOrder.settlementcChildPrice,2)}</p>
					<p>特殊人群：${fns:getMoneyAmountBySerialNum(productOrder.settlementSpecialPrice,2)}</p>
				</li>
				<li><span class="ydtips"> 出行人数</span>
					<p>成人：<span>${productOrder.orderPersonNumAdult}</span> 人</p>
					<p>儿童：<span>${productOrder.orderPersonNumChild}</span> 人</p>
					<p>特殊人群：<span>${productOrder.orderPersonNumSpecial}</span> 人</p>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>