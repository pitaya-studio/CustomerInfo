<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 预报名订单：1000 -->
<table id="contentTable" class="table table-striped table-condensed activitylist_bodyer_table">
	<thead style="background:#403738;">
		<tr>
			<th width="8%">预定渠道</th>
			<th width="10%">订单号</th>
            <th width="13%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
			<th width="7%"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
			<th width="11%">报名时间</th>
            <th width="8%">出/截团日期</th>
            <th width="5%">人数</th>
			<th width="7%">订单总额</th>
			<th width="5%">订单状态</th>
			<th width="6%">操作</th>
        </tr>
	</thead>
	<tbody>
		<c:forEach items="${page.list }" var="orders" varStatus="s">
		<tr class="toptr"> 
			<td>${orders.orderCompanyName }</td>
			<td>
				${orders.orderNum }
			</td>
			<td>
				<div class="tuanhao_cen onshow">
					${orders.groupCode}</div>
				<div class="chanpin_cen qtip" title="${orders.acitivityName}"><a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.activityId}')">${orders.acitivityName}</a></div>
			</td>
			<td>
				<div class="salerId_cen onshow">
					${orders.salerName}
				</div>
				<div class="createBy_cen qtip">
					${orders.carateUserName}
				</div>
			</td>
			<td class="tc">
				<fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
			<td class="p0">
				<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
				<div class="close-date"><fmt:formatDate value="${orders.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
			</td>
			<td class="tr">
				${orders.orderPersonNum}
			</td>
			<td class="tr">
				<span class="tdorange fbold">
					${fns:getMoneyAmountBySerialNum(orders.totalMoney,2)}
				</span>
			</td>
			<td class="tr">
				<c:if test="${orders.orderType==0}">
					已报名
				</c:if>
				<c:if test="${orders.orderType==1}">
					转正 
				</c:if>
				<c:if test="${orders.orderType==2}">
	                                         已取消
	            </c:if>
			</td>
			<td class="tr">
				<input class="btn btn-primary" type="button"  name="positive" value="转正" onClick="applyOrderInfo.changeToOrder(${orders.gruopId}, ${orders.id}, ${orders.freePosition},${orders.orderPersonNum})" <c:if test="${orders.orderType==2||orders.orderType==1}">style="display:none;"</c:if>/>
              	<input class="btn btn-primary" type="button" name="cancle" value="取消" onClick="applyOrderInfo.changeOrderType(${orders.id}, this, 2)" <c:if test="${orders.orderType==2||orders.orderType==1}">style="display:none;"</c:if>/>
                <input class="btn btn-primary" type="button" name="recover" value="恢复" onClick="applyOrderInfo.changeOrderType(${orders.id}, this, 0)" <c:if test="${orders.orderType==0||orders.orderType==1}">style="display:none;"</c:if>/>
				<c:if test="${orders.orderType == 1}">
					<input class="btn gray" type="button" value="转正"/>
				</c:if>
			</td>
		</tr>
	</c:forEach>
</tbody>
</table>
