<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>机票成本录入列表</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

	<script type="text/javascript">
	
		$(function() {
			$(".qtip").tooltip({
				track: true
			});
			
			//added for 展开收起筛选 by tlw at20170224
			launch();
			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != "100" &&
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
			
			$("#groupform").validate({});
			jQuery.extend(jQuery.validator.messages, {
				required: "必填信息"
			});
			
			var _$orderBy = $("#orderBy").val();
			if(_$orderBy==""){
				_$orderBy="createDate DESC";
			}
			var orderBy = _$orderBy.split(" ");
			$(".activitylist_paixu_left li").each(function(){
				if ($(this).hasClass("li"+orderBy[0])){
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
					$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
					$(this).attr("class","activitylist_paixu_moren");
				}
			});
			$('.team_top').find('.table_activity_scroll').each(function(index, element) {
				var _gg=$(this).find('tr').length;
				if(_gg>=20){
					$(this).addClass("group_h_scroll");
				}
			});

		});

		$(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
			});	
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/cost/manager/airTicketList");
			$("#searchForm").submit();
		}
		
		function sortby(sortBy,obj){
			var temporderBy = $("#orderBy").val();
			if(temporderBy.match(sortBy)){
				sortBy = temporderBy;
				if(sortBy.match(/ASC/g)){
					sortBy = $.trim(sortBy.replace(/ASC/g,"")) + " DESC";
				}else{
					sortBy = $.trim(sortBy.replace(/DESC/g,""))+" ASC";
				}
			}else{
				sortBy = sortBy+" DESC";
			}
			
			$("#orderBy").val(sortBy);
			$("#searchForm").submit();
		}
		
	</script>

</head>
<body>
	<page:applyDecorator name="cost_input_head">
		<page:param name="current">airticket</page:param>
	</page:applyDecorator>
	<%--<%@ include file="/WEB-INF/views/head/costInputHead.jsp"%>--%>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form id="searchForm" modelAttribute="activityAirTicket" action="${ctx}/cost/manager/airTicketList" method="post" >
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="orderBy" name="orderBy"  type="hidden" value="${page.orderBy}"/>
			<%--<input id="review" name="review" type="hidden" value="${review}">--%>
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2">
					<input name="departureCity" type="text" class="inputTxt" value="${params.departureCity}"placeholder="出发城市">
					—
					<input  value="${params.arrivedCity}" name="arrivedCity" class="inputTxt"placeholder="到达城市">
				</div>
				<a class="zksx filterButton_solo">筛选</a>
				<div class="form_submit">
					<input type="submit" value="搜索" class="btn btn-primary">
				</div>
				<div class="ydxbd">
					<span></span>
					<div class="activitylist_bodyer_right_team_co3">
						<label class="activitylist_team_co3_text">航空公司：</label>
						<div class="selectStyle">
							<select name="airlines">
								<option value="">不限</option>
								<c:forEach items="${traffic_namelist}" var="trafficName">
									<option value="${trafficName.value}" ${trafficName.value==activityAirTicket.airlines?"selected":''}>${trafficName.label} ${trafficName.description}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">起飞时间：</label>
						<input readonly="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" style="margin-left: -3px;" value="${params.groupOpenDate}" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate">
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker()" value="${params.groupCloseDate}" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">机票类型：</label>
						<div class="selectStyle">
							<select name="airType">
								<option value="">不限</option>
								<option value="3" ${params.airType=='3'?"selected":''}>单程</option>
								<option value="1" ${params.airType=='1'?"selected":''}>多段</option>
								<option value="2" ${params.airType=='2'?"selected":''}>往返</option>
							</select>
						</div>
					</div>
					<c:if test="${DHJQ || TMYT}">
	           			<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">付款提交：</div>
							<div class="selectStyle">
								<select name="commitType">
								   <option value="">请选择</option>
								   <option value="1" <c:if test="${params.commitType eq '1'}">selected="selected"</c:if>>未提交</option>
								   <option value="2" <c:if test="${params.commitType eq '2'}">selected="selected"</c:if>>已提交</option>
								</select>
							</div>
						</div>
			        </c:if>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">计调：</label>
						<input id="operator" name="operator" class="inputTxt" value="${params.operator }"/>
					</div>
					<%-- 540 增加驳回标识筛选项 王洋 2017.3.22 --%>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">驳回标识：</div>
						<div class="selectStyle">
							<select name="isReject">
								<option value="" <c:if test="${params.isReject eq ''}">selected="selected"</c:if>>全部</option>
								<option value="1" <c:if test="${params.isReject eq '1'}">selected="selected"</c:if>>被驳回</option>
							</select>
						</div>
					</div>
				</div>
				<div class="kong"></div>
			</div>
		</form:form>

		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
						<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
						<li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li> 
					</ul>
				</div>
				<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
				<div class="kong"></div>
			</div>
		</div>

		<table id="contentTable" class="table activitylist_bodyer_table mainTable" >
			<thead>
				<tr>
					<th width="2%">序号</th>
					<th width="8%">航空公司</th>
					<th width="6%">机票类型</th>
					<th width="5%">舱位</th>
					<th width="7%">出发机场</th>
					<th width="7%">到达机场</th>
					<th width="8%">起飞时间</th>
					<th width="8%">到达时间</th>
					<th width="5%">计调</th>
					<th width="6%">应收价格</th>
					<th width="4%">预收</th>
					<th width="4%">机位余位</th>
					<th width="4%">切位数</th>
					<!--  <th width="6%">审核状态</th> -->
					<c:if test="${TMYT}">
						<th width="6%">预计总成本</th>
						<th width="7%">实际总成本</th>
						<th width="7%">成本差额</th>
					</c:if>
					<th width="13%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="airticket" varStatus="s">
					<tr id="parent${s.count}">
						<td class="tc">${s.count}</td>
						<c:choose>
							<c:when test="${airticket.rejectCount > 0 }">
								<%-- 540需求，当存在被驳回的审批数据，团号显示驳回标识 王洋 2017.3.22 --%>
								<td class="tc reject_style">${fns:getAirLineName(airticket.id)}<span></span></td>
							</c:when>
							<c:otherwise>
								<td class="tc">${fns:getAirLineName(airticket.id)}</td>
							</c:otherwise>
						</c:choose>
						<!-- 机票类型 -->
						<td class="tc">
							<c:choose>
								<c:when test="${airticket.airType eq '3'}">单程</c:when>
								<c:when test="${airticket.airType eq '2'}">往返</c:when>
								<c:otherwise>多段</c:otherwise>
							</c:choose>
						</td>
						<td class="tc">${fns:getFlightAirspace(airticket.id)}</td>
						<!-- 出发机场 -->
						<td class="tc">${fns:getFlightLeaveAirPort(airticket.id)}</td>
						<!-- 到达机场 -->
						<td class="tc">${fns:getFlightArrivalAirPort(airticket.id)}</td>
						<td class="tc">${fns:getFlightInfoStartTime(airticket.id)}</td>
						<td class="tc">${fns:getFlightInfoArrivalTime(airticket.id)}</td>
						<td class="tc">${airticket.operator}</td>
						<td class="tr">
							<c:forEach items="${curlist}" var="curlist" >
								<c:if test="${curlist.id eq airticket.currencyId}">
									${curlist.currencyMark}
								</c:if>
							</c:forEach><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticket.settlementAdultPrice }"/>
						</td>
						<td class="tc">${airticket.reservationsNum}</td>
						<td class="tc">${airticket.freePosition}</td>
						<td class="tc">${airticket.payReservePosition}</td>
						<c:if test="${TMYT}">
							<td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.budgetTotal}"/></td>
							<td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.actualTotal}"/></td>
							<td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.actualTotal - airticket.budgetTotal}"/></td>
						</c:if>
						<td style="vertical-align: middle;text-align: center">
							<a target="_blank" href="${ctx}/costReview/airticket/airticketCostDetail/${airticket.id}">成本录入</a><br />
							<a href="${ctx }/cost/manager/forcastList/${airticket.id}/7" target="_blank">预报单</a>
							<a href="${ctx }/cost/manager/settleList/${airticket.id}/7" target="_blank">结算单</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="page">
		<div class="pagination">
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	<br/>
</body>
</html>