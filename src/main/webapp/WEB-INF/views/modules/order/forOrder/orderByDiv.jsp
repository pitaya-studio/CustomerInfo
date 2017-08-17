<%@ page contentType="text/html;charset=UTF-8" %>
<div class="activitylist_bodyer_right_team_co_paixu">
	<div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
			<ul>
				<li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
				<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
				<c:if test="${activityKind == 2 || activityKind == 10}">
					<li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
					<!-- C323大洋隐藏 -->  <!-- 284 散拼报名 列表 针对越柬行踪 屏蔽排序条件 -->
					<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
					</c:if>
				</c:if>
				<li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
			</ul>
		</div>
		<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
		<div class="kong"></div>
	</div>
</div>