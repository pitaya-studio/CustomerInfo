<%@ page contentType="text/html;charset=UTF-8"%>
<%--<div class="filterbox">--%>
	<%--<div class="filter_num">查询结果<strong>${page.count}</strong>条</div>--%>
	<%--<div class="filter_check">--%>
		<%--<span>信息筛选：</span>--%>
		<%--<label><input type="radio" id="orderLabel" name="filterTable1" value="true" onclick="orderOrGroupList('order');"/>订单列表</label>--%>
		<%--<label><input type="radio" id="groupLabel" name="filterTable1" value="false" onclick="orderOrGroupList('group');"/>团号列表</label>--%>
	<%--</div>--%>
	<%--<div class="filter_sort"><span>表单排序：</span>--%>
		<%--<a onClick="sortby('orderById',this)" id="orderById">创建时间<i class="i_sort"></i></a>--%>
		<%--<a onClick="sortby('orderByUpdateDate',this)" id="orderByUpdateDate">更新时间<i class="i_sort"></i></a>--%>
		<%--<a onClick="sortby('groupOpenDate',this)" id="groupOpenDate">出团日期<i class="i_sort"></i></a>--%>
		<%--<a onClick="sortby('groupEndDate',this)" id="groupEndDate">截团日期<i class="i_sort"></i></a>--%>
	<%--</div>--%>
<%--</div>--%>

<div class="supplierLine">
	<a onclick="orderOrGroupList('group');" href="javascript:void(0)" id="groupLabel">团号列表</a>
	<a onclick="orderOrGroupList('order');" href="javascript:void(0)" id="orderLabel">订单列表</a>
</div>

<div class="activitylist_bodyer_right_team_co_paixu">
	<div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
			<ul>

				<li class="activitylist_paixu_left_biankuang orderById">
					<a onclick="sortby('orderById',this)">创建时间</a></li>
				<li class="activitylist_paixu_left_biankuang orderByUpdateDate">
					<a onclick="sortby('orderByUpdateDate',this)">更新时间</a></li>
				<li class="activitylist_paixu_left_biankuang groupOpenDate">
					<a onclick="sortby('groupOpenDate',this)">出团日期</a></li>
				<li class="activitylist_paixu_left_biankuang groupEndDate">
					<a onclick="sortby('groupEndDate',this)">截团日期</a></li>
			</ul>
		</div>
		<div class="activitylist_paixu_right">

			查询结果&nbsp;<strong>${page.count}</strong>&nbsp;条
		</div>
		<div class="kong"></div>
	</div>
</div>