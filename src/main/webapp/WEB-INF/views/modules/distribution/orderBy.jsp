<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	$(function(){
		var orderBy = $("#orderBy").val();
		// 同行价格样式处理
		if (orderBy == "settlementPriceRMB DESC") {
			//按钮高亮
			$("#settlementPriceRMBId").attr("class", "activitylist_paixu_moren");
			//原先高亮的同级元素置灰
			$("#settlementPriceRMBId").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");
			$("#settlementPriceRMBId").children().html("同行价格<i class='icon icon-arrow-down'></i>");
		} else if (orderBy == "settlementPriceRMB ASC") {
			//按钮高亮
			$("#settlementPriceRMBId").attr("class", "activitylist_paixu_moren");
			//原先高亮的同级元素置灰
			$("#settlementPriceRMBId").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");
			$("#settlementPriceRMBId").children().html("同行价格<i class='icon icon-arrow-up'></i>");
		}
		
		// 直客价格样式处理
		if (orderBy == "suggestPriceRMB DESC") {
			//按钮高亮
			$("#suggestPriceRMBId").attr("class", "activitylist_paixu_moren");
			//原先高亮的同级元素置灰
			$("#suggestPriceRMBId").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");
			$("#suggestPriceRMBId").children().html("直客价格<i class='icon icon-arrow-down'></i>");
		} else if (orderBy == "suggestPriceRMB ASC") {
			//按钮高亮
			$("#suggestPriceRMBId").attr("class", "activitylist_paixu_moren");
			//原先高亮的同级元素置灰
			$("#suggestPriceRMBId").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");
			$("#suggestPriceRMBId").children().html("直客价格<i class='icon icon-arrow-up'></i>");
		}
		
		// 团期更新时间样式处理
		if (orderBy == "g.updateDate DESC") {
			//按钮高亮
			$("#groupUpdateDateId").attr("class", "activitylist_paixu_moren");
			//原先高亮的同级元素置灰
			$("#groupUpdateDateId").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");
			$("#groupUpdateDateId").children().html("更新时间<i class='icon icon-arrow-down'></i>");
		} else if (orderBy == "g.updateDate ASC") {
			//按钮高亮
			$("#groupUpdateDateId").attr("class", "activitylist_paixu_moren");
			//原先高亮的同级元素置灰
			$("#groupUpdateDateId").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");
			$("#groupUpdateDateId").children().html("更新时间<i class='icon icon-arrow-up'></i>");
		}
	})

	function sortby(sortBy,obj){
		var temporderBy = $("#orderBy").val();

		if(temporderBy.match(sortBy)){
			sortBy = temporderBy;
			if(sortBy.match(/ASC/g)){
				sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
			}else{
				sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
			}
		}else{
			sortBy = sortBy+" DESC";
		}
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}
</script>
<div class="activitylist_bodyer_right_team_co_paixu">
	<div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
			<ul>
				<%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
				<li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
				
				<c:choose>
					<c:when test="${productOrGroup == 'product' }">
						<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
					</c:when>
					<c:otherwise>
						<li id="groupUpdateDateId" class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('g.updateDate',this)">更新时间</a></li>
					</c:otherwise>
				</c:choose>
				<li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
				<li class="activitylist_paixu_left_biankuang ligroupCloseDate"><a onClick="sortby('groupCloseDate',this)">截团日期</a></li>
				
				<%-- <c:if test="${productOrGroup == 'group'}"> --%>
					<li id="settlementPriceRMBId" class="activitylist_paixu_left_biankuang lisettlementAdultPrice" style="display: list-item;">
	                	<a onclick="sortby('settlementAdultPrice',this)">同行价格</a>
	                </li>
	                <li id="suggestPriceRMBId" class="activitylist_paixu_left_biankuang lisuggestAdultPrice" style="display: list-item;">
	                	<a onclick="sortby('suggestAdultPrice',this)">直客价格</a>
	                </li>
				<%-- </c:if> --%>
				
			</ul>
		</div>
		<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
		<div class="kong"></div>
	</div>
</div>