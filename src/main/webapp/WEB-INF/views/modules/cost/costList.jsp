<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>运控成本录入列表</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
	
		$(function() {
		    launch();
			//加载操作按键
			$('.handle').hover(function() {
				if(0 != $(this).find('a').length){
					$(this).addClass('handle-on');
					$(this).find('dd').addClass('block');
				}
			},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});

			$(".qtip").tooltip({
				track: true
			});

			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i < searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i < searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
				
			jQuery.extend(jQuery.validator.messages, {
				required: "必填信息"
			});
			
			$('.nav-tabs li').hover(function(){
				$('.nav-tabs li').removeClass('current');
				$(this).parent().removeClass('nav_current');
				if($(this).hasClass('ernav')){
					if(!$(this).hasClass('current')){
						$(this).addClass('current');
						$(this).parent().addClass('nav_current');
					}
				}
			},function(){
				$('.nav-tabs li').removeClass('current');
				$(this).parent().removeClass('nav_current');
				var _active = $(".totalnav .active").eq(0);
				if(_active.hasClass('ernav')){
					_active.addClass('current');
					$(this).parent().addClass('nav_current');
				}
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
		});


        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
        }

        function sortby(sortBy,obj){
			var temporderBy = $("#orderBy").val();
			if(temporderBy.match(sortBy)){
				sortBy = temporderBy;
				if(sortBy.match(/ASC/g)){
					sortBy = $.trim(sortBy.replace(/ASC/g,"")) + " DESC";
				}else{
					sortBy = $.trim(sortBy.replace(/DESC/g,"")) + " ASC";
				}
			}else{
				sortBy = sortBy + " DESC";
			}
			
			$("#orderBy").val(sortBy);
			$("#searchForm").submit();
		}

		function getDepartment(departmentId) {
			$("#departmentId").val(departmentId);
			$("#searchForm").submit();
		}

	</script>
</head>
<body>
	<page:applyDecorator name="cost_input_head">
	<page:param name="current"><c:choose><c:when test="${params.orderType==1}">single</c:when><c:when test="${params.orderType==2}">loose</c:when><c:when test="${params.orderType==3}">study</c:when><c:when test="${params.orderType==5}">free</c:when><c:when test="${params.orderType==4}">bigCustomer</c:when><c:when test="${params.orderType==10}">cruise</c:when></c:choose></page:param>
	</page:applyDecorator>
	
	<div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
		<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/cost/manager/list/${params.orderType}/" method="post" >
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
	
			<div class="activitylist_bodyer_right_team_co">
				<%--<input id="review" name="review" type="hidden" value="${review}">--%>
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input class="txtPro inputTxt searchInput" id="wholeSalerKey" name="groupCode" value="${params.groupCode }"placeholder="请输入团号"/>
				</div>
                    <div class="zksx" >筛选</div>
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" type="submit" value="搜索">
				</div>
				<div class="ydxbd">
                    <span></span>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">地接社：</label>
                        <div class="selectStyle">
                            <select name="supplierId">
                                <option value="">不限</option>
                                <c:forEach	items="${supplierList }" var="supplier">
                                    <option value="${supplier.id }" <c:if test="${params.supplierId==supplier.id}">selected="selected"</c:if>>${supplier.supplierName }</option>
                                </c:forEach>
                            </select>
                        </div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label  class="activitylist_team_co3_text">出团日期：</label>
						<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate"
                               value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'
                               onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly/>
						<span style="font-size:12px; font-family:'宋体';"> 至</span>  
						<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate"
                               value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>'
                               onClick="WdatePicker()" readonly/>
					</div>
					<c:if test="${DHJQ || TMYT}">
	           			<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">付款提交：</label>
							<select name="commitType">
			                   <option value="">请选择</option>
			                   <option value="1" <c:if test="${params.commitType eq '1'}">selected="selected"</c:if>>未提交</option>
			                   <option value="2" <c:if test="${params.commitType eq '2'}">selected="selected"</c:if>>已提交</option>
			               </select>
						</div>
					</c:if>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">计调：</label>
						<input id="operator" name="operator" class="inputTxt" value="${params.operator }"/>
					</div>
					<%--增加驳回标识筛选项--540-yudong.xu 2017.3.22--%>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">驳回标识：</label>
						<div class="selectStyle">
							<select name="isReject">
								<option value="" <c:if test="${empty params.isReject}">selected="selected"</c:if>>全部</option>
								<option value="1" <c:if test="${params.isReject eq '1'}">selected="selected"</c:if>>被驳回</option>
							</select>
						</div>
					</div>
         		</div>
     		</div>
 		</form:form>

 		<!-- 部门分区 -->
 		<%@ include file="/WEB-INF/views/common/departmentDiv.jsp"%>
 		
 <!-- 排序部分 -->
 <div class="activitylist_bodyer_right_team_co_paixu">
     <div class="activitylist_paixu">
         <div class="activitylist_paixu_left">
             <ul>
                 <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
                 <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
                 <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
                 <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
                 <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
             </ul>
         </div>
         <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
         <div class="kong"></div>
     </div>
 </div>

 <table id="contentTable" class="table mainTable activitylist_bodyer_table">
     <thead>
         <tr>
             <th width="5%">序号</th>
             <th width="9%">团号</th>
             <th width="9%">产品名称</th>
             <th width="6%">出发地</th>
             <th width="8%">目的地</th>
             <th width="8%">出团日期</th>
             <th width="8%">截团日期</th>
			 <th width="8%">计调</th>
             <c:if test="${params.orderType==2}">
                 <th width="3%">切位数</th>
             </c:if>
             <th width="3%">余位数</th>
             <th width="3%">预收数</th>
             <!--  <th width="7%">审核状态</th> -->
             <c:if test="${TMYT}">
                 <th width="6%">预计总成本</th>
                 <th width="6%">实际总成本</th>
                 <th width="6%">成本差额</th>
             </c:if>
             <th width="14%">操作</th>
         </tr>
     </thead>
     <tbody>
         <c:forEach items="${page.list}" var="activity" varStatus="s">
             <tr id="parent${s.count}">
                 <td class="tc">${s.count}</td>
				 <c:choose>
					 <c:when test="${not empty activity.isReject}">
						 <td class="tc word-wrap reject_style">${activity.groupCode}<span></span></td>
					 </c:when>
					 <c:otherwise>
						 <td class="tc word-wrap">${activity.groupCode}</td>
					 </c:otherwise>
				 </c:choose>
                 <td class="tc">${activity.acitivityName}</td>
                 <td class="tc">${fns:getDictLabel(activity.fromArea,"from_area","")}</td>
                 <td class="tc">
                     <div id="area${s.count}" title="${areaTitle[s.count-1] }">
                         <c:forEach items="${fns:findTargetAreaById(activity.srcActivityId)}" var="targetArea" varStatus="t">
                             <c:if test="${ t.count<3}">
                                 ${targetArea.name}<c:if test="${fns:findTargetAreaById(activity.srcActivityId).size() ge 3 or (fns:findTargetAreaById(activity.srcActivityId).size() eq 2 and t.count ne 2)}">,</c:if>
                             </c:if>
                             <c:if test="${t.count eq 3}">${targetArea.name}&nbsp;</c:if><c:if test="${t.count eq 4}">.......</c:if>
                         </c:forEach>
                     </div>
                 </td>
                 <td class="tc"><fmt:formatDate value="${activity.groupOpenDate}" pattern="yyyy-MM-dd"/></td>
                 <td class="tc"><fmt:formatDate value="${activity.groupCloseDate}" pattern="yyyy-MM-dd" /></td>
				 <td class="tc">${activity.operator}</td>
                 <c:if test="${params.orderType==2}">
                     <td class="tc">${activity.payReservePosition}</td>
                 </c:if>
                 <td class="tc">${activity.freePosition}</td>
                 <td class="tc">${activity.planPosition}</td>
                 <c:if test="${TMYT}">
                     <td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.budgetTotal}"/></td>
                     <td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.actualTotal}"/></td>
                     <td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.actualTotal - activity.budgetTotal}"/></td>
                 </c:if>
                 <td class="p00">
                     <dl class="handle">
                         <a target="_blank" href="${ctx}/costReview/activity/activityCostDetail/${activity.productId}/${activity.id}/${params.orderType}?from=operatorPre">成本录入</a>
                         <a href="${ctx}/cost/manager/forcastList/${activity.id}/${params.orderType}" target="_blank">预报单</a>&nbsp;
                         <a href="${ctx}/cost/manager/settleList/${activity.id}/${params.orderType}" target="_blank">结算单</a>
                     </dl>
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
<script type="text/javascript">
$(document).ready(function(){
 //目的地去掉最后的逗号
 var total=$("#pageSize").val();
 for(var i=1;i<=total;i++){
     if($("#area"+i).length>0){
         var str=$("#area"+i).html();
         $("#area"+i).html(str.substring(0,str.length-2));
     }
 }
});
</script>
</body>
</html>