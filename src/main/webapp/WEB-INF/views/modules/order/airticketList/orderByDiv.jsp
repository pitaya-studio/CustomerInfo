<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 排序 -->
<div class="activitylist_bodyer_right_team_co_paixu">
	<div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
			<ul>
				<li class="activitylist_paixu_left_biankuang liao.id">
				<a onClick="sortby('ao.id',this)">创建时间</a></li>
				<li class="activitylist_paixu_left_biankuang liao.update_date">
				<a onClick="sortby('ao.update_date',this)">更新时间</a></li>
				<li class="activitylist_paixu_left_biankuang listartingDate">
				<a onClick="sortby('startingDate',this)">起飞时间</a></li>
				<li class="activitylist_paixu_left_biankuang lireturnDate">
				<a onClick="sortby('returnDate',this)">到达时间</a></li>
			</ul>
		</div>
		<div class="activitylist_paixu_right">
			<c:if test="${orderOrGroup == 'order' && companyUuid eq '58a27feeab3944378b266aff05b627d2'}">
				<button onClick="exportExcelUserList();" class="btn btn-primary" style="width:auto;">导出游客</button></c:if>
			查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
		</div>
		<div class="kong"></div>
	</div>
</div>
