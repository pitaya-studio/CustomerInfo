<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>转团/机票改签-列表</title>
	<meta name="decorator" content="wholesaler" />
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/>
	<link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"/>

	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/review/common/batchReview.js"></script>
	<script type="text/javascript" src="${ctxStatic}/review/transferGroup/common/transferGroupReview.js"></script>
	<style type="text/css">
		.text-more-new .activitylist_team_co3_text,.text-more-new .activitylist_bodyer_right_team_co2 label{width:90px;	text-align:right;}
		.text-more-new .activitylist_bodyer_right_team_co1,.text-more-new .activitylist_bodyer_right_team_co3{min-width:230px;}
		.text-more-new .activitylist_bodyer_right_team_co2{min-width:400px;}
	</style>
	<style  type="text/css">
		.tip-content{
		    display: none; 
		    position: absolute;
		    top: 0px;
		  	white-space: nowrap;
		  	left: 0px;
		  	height:100%;
		  	min-width:250px;
		  	max-width:350px;
		  	text-align: left;
		    z-index: 10;
		    border: 1px solid #d9d9d9;
		    background-color: white;
		    padding: 8px;
		    min-width: 100%;
		}
		.inner_cont{
			overflow: hidden;
			text-overflow:ellipsis;
			white-space: nowrap;
		}
	</style>
	<script type="text/javascript">	
		function showContentdiv(divId,num){
          var sp_show = document.getElementById("sp_show"+num);
          var inner_cont = document.getElementById("inner_cont"+num);
          sp_show.innerHTML = inner_cont.innerHTML;
          sp_show.style.display="block";
        }
		function hideContent(obj) {
		  var sp_id_name = $(obj).attr('id');
		  var sp_show = document.getElementById(sp_id_name);
		  sp_show.style.display="none";
		}
	</script>
</head>
<body>
	<input type="hidden" id="ctx" value="${ctx}"/>
	
	<c:set var="companyUuid" value="${fns:getUser().company.uuid}"></c:set>
	<form id="searchForm" action="${ctx}/newTransferGroupReview/list" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		<input id="tabStatus" name="tabStatus" type="hidden" value="${conditionsMap.tabStatus}" />
		<input id="ctx" type="hidden" value="${ctx}" />
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<%--<label></label>--%>
				<input id="groupCode" name="groupCode"
					   class="inputTxt inputTxtlong searchInput" value="${conditionsMap.groupCode}"
					   onkeyup="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()" placeholder="团号/产品名称/订单号"/>
				<%--<span class="ipt-tips" style="display: block;">团号/产品名称/订单号</span>--%>
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" value="搜索" type="submit">
			</div>
			<div class="ydxbd"><%--text-more-new--%>
				<span></span>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">产品类型：</label>
					<div class="selectStyle">
						<select name="productType">
							<c:forEach var="order" items="${fns:getDictList('order_type')}">
								<c:if test="${order.label ne '签证'}">
									<c:if test="${not (order.label eq '游轮' && companyUuid eq '7a816f5077a811e5bc1e000c29cf2586')}">
										<option value="${order.value }"
											<c:if test="${conditionsMap.productType==order.value}">selected="selected"</c:if>>${order.label}
										</option>
									</c:if>
								</c:if>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="activitylist_bodyer_right_team_co3">
					<label class="activitylist_team_co3_text">渠道选择：</label>
					<select name="agentId" id="agentId" class="width-select-channel" >
						<option value="" selected="selected">全部</option>
						<c:if test="${not empty fns:getAgentListAddSort()}">
							<c:forEach items="${fns:getAgentListAddSort()}" var="agentinfo">
								<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
									<c:choose>
									   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}"> 
									       <option value="${agentinfo.id }"<c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>直客</option>
									   </c:when>
									   <c:otherwise>
									       <option value="${agentinfo.id }"<c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option>
									   </c:otherwise>
									</c:choose>
							</c:forEach>
						</c:if>
					</select>
				</div>
				
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">游客：</label>
					<input id="travelerName" class="inputTxt " name="travelerName"  value="${conditionsMap.travelerName }"/>
				</div>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
					<select name="paymentType">
						<option value="">不限</option>
						<c:forEach var="pType" items="${fns:findAllPaymentType()}">
							<option value="${pType[0] }"
								<c:if test="${paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
						</c:forEach>
					</select>
				</div> --%>

				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">审批发起人：</label>
					<select name="applyPerson" id="saler" >
						<option value="" selected="selected">全部</option>
						<!-- 用户类型  1 代表销售 -->
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<option value="${userinfo.id }"
								<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}
							</option>
						</c:forEach>
					</select> 
				</div>
				
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">申请日期：</label>
					<input class="inputTxt dateinput" name="applyDateFrom" value="${conditionsMap.applyDateFrom}" onclick="WdatePicker()" readonly="readonly" />
					<span>至 </span>
					<input class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}" onclick="WdatePicker()" readonly="readonly" />
				</div>
					
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">审批状态：</label>
					<div class="selectStyle">
						<select name="reviewStatus" id="reviewStatus">
							<option value="" selected="selected">全部</option>
							<option value="1" <c:if test="${conditionsMap.reviewStatus == '1' }"> selected="selected" </c:if>>审批中</option>
							<option value="2" <c:if test="${conditionsMap.reviewStatus == '2' }"> selected="selected" </c:if>>审批通过</option>
							<option value="0" <c:if test="${conditionsMap.reviewStatus == '0' }"> selected="selected" </c:if>>审批驳回</option>
							<option value="3" <c:if test="${conditionsMap.reviewStatus == '3' }"> selected="selected" </c:if>>取消申请</option>
						</select>
					</div>
				</div>
			</div>
			<div class="kong"></div>
		</div>
	</form>
    <div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu_left">
           <ul>
            <%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
            <li class="activitylist_paixu_left_biankuang lir.create_date"><a onClick="sortby('r.create_date',this)">创建时间</a></li>
		    <li class="activitylist_paixu_left_biankuang lir.update_date"><a onClick="sortby('r.update_date',this)">更新时间</a></li>
           </ul>
		</div>
	</div>
	
	<!--状态开始-->
	<div class="supplierLine">
		<a href="javascript:void(0)" id="all" onClick="statusChooses('0')" <c:if test = "${conditionsMap.tabStatus == null || conditionsMap.tabStatus == 0 || conditionsMap.tabStatus == '0'}">class="select" </c:if>>全部</a> 
		<a id="todo" href="javascript:void(0)" onClick="statusChooses('1')" <c:if test = "${conditionsMap.tabStatus == '1' || conditionsMap.tabStatus == 1}">class="select" </c:if>> 待本人审批</a> 
		<a id="todo" href="javascript:void(0)" onClick="statusChooses('2')" <c:if test = "${conditionsMap.tabStatus == '2' || conditionsMap.tabStatus == 2}">class="select" </c:if>>本人已审批</a> 
		<a id="todoing" href="javascript:void(0)" onClick="statusChooses('3')" <c:if test = "${conditionsMap.tabStatus == '3' || conditionsMap.tabStatus == 3}">class="select" </c:if>>非本人审批</a>
	</div>
    <!--状态结束-->
    
    <table id="contentTable" class="table activitylist_bodyer_table mainTable" style="width:100%; table-layout:fixed;">
        <thead>
            <tr>
                <th width="5%">序号</th>
                <th width="8%">订单号</th>
                <th width="9%">
                	<span class="tuanhao on">转出团号</span>/<span class="chanpin">转入团号</span>
                </th>
                <th width="6%">产品类型</th>
                <th width="6%">申请时间</th>
                <th width="6%">审批发起人</th>
                <th width="5%">渠道商</th>
                <th width="5%">游客</th>
                <th width="8%">原应收金额</th>
                <th width="8%">转团后应收</th>
                <th width="6%">上一环节<br />审批人</th>
                <th width="6%">审批状态</th>
                <th width="4%">操作</th>
            </tr>
        </thead>
        <tbody>
       		<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
	                 <td colspan="13" style="text-align: center;">
						 暂无搜索结果
	                 </td>
				</tr>
	        </c:if>
	        <c:forEach items="${page.list}" var="reviewInfos" varStatus="s">
				<tr>
	              	<td>
						<c:if test="${conditionsMap.tabStatus == '1'}">
							<input type="checkbox" value="${reviewInfos.id}@${reviewInfos.productType}" name="activityId" >
						</c:if>
						${s.count}
	              	</td>
	              	<td>${reviewInfos.orderNum}</td>
	                <td>
	                	<div title="${reviewInfos.groupCode}" class="tuanhao_cen onshow">${reviewInfos.groupCode}</div>
						<div title="${reviewInfos.newGroupCode}" class="chanpin_cen qtip">${reviewInfos.newGroupCode}</div>
	        		</td>
	        		<td>${fns:getStringOrderStatus(reviewInfos.productType)}</td>
	        		<td class="p0">
	        			<div class="out-date"><fmt:formatDate value="${reviewInfos.createDate}" pattern="yyyy-MM-dd"/></div>
						<div class="close-date time"><fmt:formatDate value="${reviewInfos.createDate}" pattern="HH:mm:ss"/></div>
					</td>
	        		<td class="tc">${fns:getUserNameById(reviewInfos.createBy)}</td>
	        		<td class="tc">
	        			<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && reviewInfos.agent==-1}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						       ${fns:getAgentName(reviewInfos.agent)}
						   </c:otherwise>
						</c:choose>
	        		</td>
	        		<td class="tc inner_cont" title="${reviewInfos.travellerName}">${reviewInfos.travellerName}</td>
	        		<td class="tc inner_cont tdorange fbold" title="${reviewInfos.payPriceSumString}">${reviewInfos.payPriceSumString}</td>
					<td class="tc inner_cont tdorange fbold" title="${reviewInfos.subtractSumString}">${reviewInfos.subtractSumString}</td>
	        		<td class="tc">${fns:getUserNameById(reviewInfos.lastReviewer)}</td>
	                <c:choose>
                		<c:when test="${reviewInfos.status == 0}">
                			<td class="invoice_back tc">${reviewInfos.statusdesc}</td>
                		</c:when>
                		<c:otherwise>
                			<td class="invoice_yes tc">${reviewInfos.statusdesc}</td>
                		</c:otherwise>
                	</c:choose>
	                
	                <td class="p0">
	                    <dl class="handle">
	                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
	                        <dd class="">
		                        <p>
		                        	<c:choose>
		                        		<c:when test="${reviewInfos.productType == 7}">
		                        			<a href="${ctx}/airticketChange/airChange/airticketApprovalDetail?orderId=${reviewInfos.orderId}&trvalerId=${reviewInfos.travellerId}&reviewId=${reviewInfos.id}" target="_blank">查看</a>
		                        			<c:if test="${not empty reviewInfos.productId}"><a href="${ctx }/cost/manager/forcastList/${reviewInfos.productId}/${reviewInfos.productType}" target="_blank">预报单</a></c:if>
											<c:if test="${not empty reviewInfos.productId}"><a href="${ctx }/cost/manager/settleList/${reviewInfos.productId}/${reviewInfos.productType}" target="_blank">结算单</a></c:if>
		                        		</c:when>
		                        		<c:otherwise>
		                        			<a href="${ctx}/newTransferGroup/transferGroupInfo?reviewId=${reviewInfos.id}" target="_blank">查看</a>
		                        			<a href="${ctx}/cost/manager/forcastList/${reviewInfos.groupId}/${reviewInfos.productType}" target="_blank">预报单</a>
											<a href="${ctx}/cost/manager/settleList/${reviewInfos.groupId}/${reviewInfos.productType}" target="_blank">结算单</a>
		                        		</c:otherwise>
		                        	</c:choose>
									<c:if test="${reviewInfos.isCurReviewer == true}">
										<c:choose>
			                        		<c:when test="${reviewInfos.productType == 7}">
			                        			<a href="${ctx}/airticketChange/airChange/airticketApprovalDetail?orderId=${reviewInfos.orderId}&trvalerId=${reviewInfos.travellerId}&reviewId=${reviewInfos.id}&isReview=true" target="_blank">审批</a>
			                        		</c:when>
			                        		<c:otherwise>
			                        			<a href="${ctx}/newTransferGroup/transferGroupInfo?reviewId=${reviewInfos.id}&isReview=true" target = "_blank">审批</a>
			                        		</c:otherwise>
			                        	</c:choose>
									</c:if>
									<c:if test="${reviewInfos.isBackReview == true}">
										<a href="javascript:void(0)" onClick = "backReview('${reviewInfos.reviewId}')">撤销</a> 
									</c:if>
		                        </p>
	                        </dd>
	                    </dl>
	                </td>
	            </tr>
			</c:forEach>
     	</tbody>
	</table>
	<c:if test="${conditionsMap.tabStatus == '1'}">
		<div class="page">
			<div class="pagination">
				<dl>
					<dt>
						<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
					</dt>
					<dt>
						<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
					</dt>
					<dd>
						<%--<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:payedConfirmNew('${ctx}','/newTransferGroupReview/batchReview');" >批量审批</a>--%>
						<input type="button" value="批量审批" id="piliang_o_${result.orderId}" class="btn" onclick="javascript:payedConfirmNew('${ctx}','/newTransferGroupReview/batchReview');">

					</dd>
				</dl>
			</div>
		</div>
	</c:if>
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
</body>


</html>