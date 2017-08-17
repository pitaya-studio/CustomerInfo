<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 订单标题 -->
<thead>
	<tr>
		<th width="6%">序号</th>
		<th width="6%" style="min-width: 75px;">预定渠道</th>
		<!-- 200 针对优加，机票订单 订单列表加入"渠道联系人" -->
		<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
			<th width="8%">渠道联系人</th>
		</c:if>
		
		<th width="6%">订单编号</th>
		<th width="6%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品编号</span></th>
		<th width="6%">参团类型</th>
		<th width="6%">参团订单号<br> 参团团号</th>
		<th width="6%"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
		<th width="8%">预订/剩余时间</th>
		<th width="8%">出/截团日期</th>
		<th width="6%">机票类型</th>
		<th width="4%">人数</th>
		<th width="6%">游客</th>
		<c:if test="${queryType eq 1 && companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
			<th width="6%">发票/收据</th>
		</c:if>
		<th width="6%">订单状态</th>
		<th width="6%" class="tr">
			<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">订单总额</c:if>
			<%-- C360 大唐国旅 显示未收余额 --%>
			<c:if test="${companyUuid eq '7a45838277a811e5bc1e000c29cf2586' }"><span class="total on">订单总额</span>/<span class="remainder">未收余额</span></c:if>
		</th>
		<th width="6%">已收金额<br>到账金额</th>
		<th width="4%">操作</th>
		<th width="4%">下载</th>
		<c:if test="${queryType eq 1}">
			<th width="4%">财务</th>
		</c:if>
	</tr>
</thead>