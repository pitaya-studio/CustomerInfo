<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- 订单标题 -->
<thead style="background:#403738;" id="orderOrGroup_order_thead">
	<tr>
	    <th width="4%">序号</th>			
		<th width="9%">预定渠道</th>
		<!-- 200 针对优加，订单列表加入"渠道联系人" -->
		<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
			<th width="8%">渠道联系人</th>
		</c:if>
		<th width="7%">订单号</th>
		<th width="6%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
		<th width="5%">计调</th>
		<th width="5%"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
		<th width="8%">预订<c:if test="${(showType ne 89) && (showType ne 99)}">/剩余</c:if>时间</th>
		<th width="7%">出/截团日期</th>
		<th width="4%">人数</th>
		<th width="7%">游客</th>
		<!-- 20151102 C322 针对大洋需求，屏蔽发票和收据明细 -->
		<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
			<th width="6%">发票/收据</th>
		</c:if>
		<th width="6%">订单状态</th>
		<th width="7%">
			<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">订单总额</c:if>
			<%-- C360 大唐国旅 显示未收余额 --%>
			<c:if test="${companyUuid eq '7a45838277a811e5bc1e000c29cf2586' }"><span class="total on">订单总额</span>/<span class="remainder">未收余额</span></c:if>
		</th>
		<c:choose>
			<c:when test="${showType==99}"><th width="7%">取消原因</th></c:when>
               <c:otherwise>                     
				<th width="7%">已收金额<br/>到账金额</th> 
			</c:otherwise>
		</c:choose>
		<c:if test="${showType==89}">
			<th width="7%">退团原因</th>
		</c:if>
		<th width="4%">操作</th>
		<c:if test="${showType != 7}">
			<th width="4%">下载</th>
			<th width="4%">财务</th>
		</c:if>
	</tr>
</thead>