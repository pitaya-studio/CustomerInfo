<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 查询form -->
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/t1/orderList/manage/showOrderList/${orderStatus}.htm" method="post">
	<input id="ctx" type="hidden" value="${ctx}" />
	<input id="orderStatus" type="hidden" value="${orderStatus}" />
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
	<input id="showType" name="showType" type="hidden" value="${showType}" />
	<input id="orderOrGroup" name="orderOrGroup" type="hidden" value="${orderOrGroup}" />
	<style>
		.input-append{
			margin-bottom:0;
		}
		.groupSearchFirst{
			margin:10px 0 5px 0;
		}
		select{
			width:220px;
			height:32px;
		}
		#targetAreaName{
			width:144px;
		}
		.custom-combobox-input{
			width:200px;
			border-right:0;
			background:#fff;
		}
		.ui-button{
			height:30px;
		}
	</style>
	<div class="groupSearch">
		<div class="groupSearchFirst">
			<span class="groupSearchSpan">
				<span>搜索：</span>
				<input type="text" placeholder="产品名称、团号" name="productNameOrGroupCode" value="${productNameOrGroupCode}"/>
			</span>
			<span class="groupSearchSpan">
				<span>出发城市：</span>
				<span>
					<form:select path="fromArea" itemValue="key" itemLabel="value">
						<form:option value="">不限</form:option>
						<form:options items="${fromAreas}" />
					</form:select>
				</span>
			</span>
			<div class="groupSearchSpan">
				<span>目的地：</span>
					<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList" 
						labelValue="${targetAreaNames}" title="区域" url="/activity/manager/filterTreeData1?kind=${activityKind}" checked="true"/>
			</div>
		</div>
		
		<p>
			<span class="groupSearchSpan">
				<span>订单状态</span>
				<select name="orderStatus" id="orderStatus">
					<option value="" <c:if test="${empty orderStatus}">selected="selected"</c:if>>全部订单</option>
					<option value="1" <c:if test="${orderStatus==1 }">selected="selected"</c:if>>未支付全款</option>
					<option value="2" <c:if test="${orderStatus==2 }">selected="selected"</c:if>>未支付订金</option>
					<option value="5" <c:if test="${orderStatus==5 }">selected="selected"</c:if>>已支付全款</option>
					<option value="4" <c:if test="${orderStatus==4 }">selected="selected"</c:if>>已支付订金</option>
					<option value="3" <c:if test="${orderStatus==3 }">selected="selected"</c:if>>已占位</option>
					<option value="7" <c:if test="${orderStatus==7 }">selected="selected"</c:if>>待计调确认</option>
					<option value="8" <c:if test="${orderStatus==8 }">selected="selected"</c:if>>待财务确认</option>
					<option value="9" <c:if test="${orderStatus==9 }">selected="selected"</c:if>>已撤销占位</option>
					<option value="99" <c:if test="${orderStatus==99 }">selected="selected"</c:if>>已取消</option>
					<option value="111" <c:if test="${orderStatus==111 }">selected="selected"</c:if>>已删除</option>
				</select>
			</span>
			<span class="groupSearchSpan">
				<span>供应商：</span>
				<span class="sysselect_s">
					<select name="companyId" id="companyId">
						<option value="" selected="">全部</option>
						<c:forEach var="office" items="${officeList}">
							<option value="${office.id}" <c:if test="${companyId==office.id}">selected="selected"</c:if>>${office.name}</option>
						</c:forEach>
					</select>
				</span>
			</span>
			<span class="groupSearchSpan">
				<span>下单日期：</span>
				<input readonly="readonly" onclick="WdatePicker()" value="${orderTimeBegin}" name="orderTimeBegin" class="inputTxt dateinput">
				<span>至</span>
				<input readonly="readonly" onclick="WdatePicker()" value="${orderTimeEnd}" name="orderTimeEnd" class="inputTxt dateinput">
			</span>
		</p>

		<p>
			<span class="groupSearchSpan">
				<span>出团日期：</span>
				<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate"
					value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'	 
					onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly />
				<span> 至</span>
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate"
					value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' onClick="WdatePicker()" readonly />
			</span>
		</p>
                            
		<p class="search">
			<span onclick="formSubmit();">搜&nbsp&nbsp&nbsp索</span>
			<span class="reset" onclick="resetSearchParams()">条件重置</span>
		</p>
	</div>
</form:form>

<!-- 下载 -->
<form id="exportForm" action="${ctx}/orderCommon/manage/downloadData" method="post">
	<input type="hidden" id="orderId" name="orderId">
	<input type="hidden" name="orderType" value="${orderStatus}">
	<input type="hidden" id="downloadType" name="downloadType">
	<input type="hidden" value="${orderNum}" id="orderNum" name="orderNum">
</form>
<!-- 导出 -->
<form id="exportTravelesForm" action="${ctx}/activity/manager/exportExcel" method="post">
	<input type="hidden" id="groupId" name="groupId">
	<input type="hidden" name="orderType" value="${orderStatus}">
	<input type="hidden" id="groupCode" name="groupCode">
</form> 