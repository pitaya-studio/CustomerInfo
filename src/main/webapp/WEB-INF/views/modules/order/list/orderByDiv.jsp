<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- 排序 -->
<div class="activitylist_bodyer_right_team_co_paixu">
	<div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
			<ul>
				<%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
				<li class="activitylist_paixu_left_biankuang lipro.id">
				<a onClick="sortby('pro.id',this)">创建时间</a></li>
				<li class="activitylist_paixu_left_biankuang lipro.updateDate">
				<a onClick="sortby('pro.updateDate',this)">更新时间</a></li>
				<li class="activitylist_paixu_left_biankuang ligroupOpenDate">
				<a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
				<li class="activitylist_paixu_left_biankuang ligroupCloseDate">
				<a onClick="sortby('groupCloseDate',this)">截团日期</a></li>
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
