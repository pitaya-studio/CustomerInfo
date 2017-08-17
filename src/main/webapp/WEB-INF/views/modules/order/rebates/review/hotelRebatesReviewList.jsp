<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>审核-返佣</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/rebates/rebatesReviewList.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript">
	$(function() {
	
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		
		//首次打印提醒
		$(".uiPrint").hover(function(){
			$(this).find("span").show();
		},function(){
			$(this).find("span").hide();
		});
	})
	</script>
	<style type="text/css">
	.message_box{width:100%;padding:0px 0 40px 0;}
	.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
	.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
	.message_box_li .curret{background:#62afe7;}
	.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
	.message_box_li_a span{float:left;}
	.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
	</style>
</head>
<body>
	<page:applyDecorator name="rebates_review_head">
             <page:param name="current"><c:choose><c:when test="${reviewVO.orderType==1}">single</c:when><c:when test="${reviewVO.orderType==2}">loose</c:when><c:when test="${reviewVO.orderType==3}">study</c:when><c:when test="${reviewVO.orderType==5}">free</c:when><c:when test="${reviewVO.orderType==4}">bigCustomer</c:when><c:when test="${orderType==6}">visa</c:when><c:when test="${orderType==7}">airticket</c:when><c:when test="${reviewVO.orderType==10}">cruise</c:when></c:choose></page:param>
       </page:applyDecorator>
	<!--右侧内容部分开始-->
    <form id="searchForm" action="${ctx}/order/rebates/review/showRebatesReviewList?orderType=${reviewVO.orderType}" method="post">
		<div class="message_box">
					<c:forEach items="${userJobs}" var="role">
						<div class="message_box_li">
							<c:choose>
								<c:when test="${reviewVO.rid==role.id}">
						   				<a  onClick="chooseRole('${role.id}');">
						   				<div class="message_box_li_a curret">
								<span>
								${fns:abbrs(role.deptName,role.jobName,40)}
								</span>
								<c:if test ="${role.count gt 0}">
									<div class="message_box_li_em" >
									${role.count}								
									</div>
								</c:if>
								<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
									<div class="message_box_li_hover">
										${role.deptName }_${role.jobName }
									</div>
								</c:if>
								
							</div>
						   				</a>
						   		</c:when>
								<c:otherwise>
						   		<a  onClick="chooseRole('${role.id}');">
						   				<div class="message_box_li_a">
								<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
								<c:if test ="${role.count gt 0}">
									<div class="message_box_li_em" >
									${role.count}					
									</div>
								</c:if>
								<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
									<div class="message_box_li_hover">
										${role.deptName }_${role.jobName }
									</div>
								</c:if>
							</div>
						   				</a>
						   		</c:otherwise>
							</c:choose>
							
						</div>
					</c:forEach>	
				</div>
	    <input id="rid"name="rid" type="hidden" value="${reviewVO.rid}" />
		<input id="userLevel"name="userLevel" type="hidden" value="${reviewVO.userLevel}" />
		<input id="reviewStatus"name="reviewStatus" type="hidden" value="${reviewVO.reviewStatus}" />
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		<input id="revid" name="revid" type="hidden" value="0" />
		<!-- 排序字段 默认为1 -->
		<input id="statusChoose" name="statusChoose" type="hidden"
							value="${reviewVO.reviewStatus}" />
		<!-- 状态过滤  默认为0 全部 -->
		<input name="flowType" type="hidden" value="1" />
		<!-- 流程类型 退款 退票 -->
		<input name="active" type="hidden" value="1" />
		<!-- 有效标志 用于审批表记录查询 默认为1 -->
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>团号：</label><input style="width:260px"
					class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey"
					name="groupCode" value="${reviewVO.groupCode}" type="text" />
			</div>
			<!--  
			<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">团队类型：</div>
					 <select name="orderType" class="selectType">
						<c:forEach items="${productList}" var="productinfo">
							<option value="${productinfo.productType}"<c:if test="${productinfo.productType eq reviewVO.orderType }">selected="selected"</c:if> >${productinfo.productName}</option>											
						</c:forEach>
					</select> 
				</div>
				-->
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" value="搜索" type="submit" />
				<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
			</div>
			<div class="zksx">筛选</div>
			<div class="ydxbd">
				<div class="activitylist_bodyer_right_team_co2">
	                <label class="activitylist_team_co3_text">下单日期：</label>
	                <input id="orderTimeBegin" class="inputTxt dateinput" name="orderTimeBegin"  value='<fmt:formatDate value="${orderType.orderTimeBegin }" pattern="yyyy-MM-dd"/>'  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeEnd').value==''){$dp.$('orderTimeEnd').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                <input id="orderTimeEnd" class="inputTxt dateinput" name="orderTimeEnd" value='<fmt:formatDate value="${orderType.orderTimeEnd }" pattern="yyyy-MM-dd"/>'  onClick="WdatePicker()" readonly/>
	            </div>
				<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">渠道：</div>
					<select name="channel">
						<option value="">不限</option>
						<c:if test="${not empty fns:getAgentList() }">
							<c:forEach items="${fns:getAgentList()}" var="agentinfo">
								<option value="${agentinfo.id }" <c:if test="${agentinfo.id==reviewVO.channel }">selected="selected"</c:if>>${agentinfo.agentName}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">销售：</div>
					<select class="sel-w1" style='height : 28px' name="saler">
						<option value="" selected="selected">不限</option>
						<c:forEach items="${fns:getSaleUserList('1,2')}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }" <c:if test="${userinfo.id==reviewVO.saler }">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">计调：</div>
					<select name='meter'>
						<option value=""  selected="selected">不限</option>
						<c:forEach items="${fns:getSaleUserList('3')}" var="userinfo">
							<!-- 用户类型  3 代表计调 -->
							<option value="${userinfo.id }" <c:if test="${userinfo.id==reviewVO.meter}">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="kong"></div>
		</div>
	</form>
    <div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu_left">
           <ul>
            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
            <li class="activitylist_paixu_left_biankuang liaa.createDate"><a onClick="sortby('aa.createDate',this)">创建时间</a></li>
		    <li class="activitylist_paixu_left_biankuang liaa.updateDate"><a onClick="sortby('aa.updateDate',this)">更新时间</a></li>
           </ul>
		</div>
	</div>
	<!--状态开始-->
	<div class="supplierLine"><c:if test=""></c:if>
						<a  <c:if test="${reviewVO.reviewStatus==0 }">class="select"</c:if> onClick="statusChoose(0);">全部</a> 
						<a <c:if test="${reviewVO.reviewStatus==1 }">class="select"</c:if> onClick="statusChoose(1);">待本人审核</a>
						<a <c:if test="${reviewVO.reviewStatus==4 }">class="select"</c:if> onClick="statusChoose(4);">审核中</a>
						<a  <c:if test="${reviewVO.reviewStatus==2 }">class="select"</c:if> onClick="statusChoose(2);">未通过</a>
						<a <c:if test="${reviewVO.reviewStatus==3 }">class="select"</c:if> onClick="statusChoose(3);">已通过</a>
						<a <c:if test="${reviewVO.reviewStatus==5 }">class="select"</c:if> onClick="statusChoose(5);">已取消</a>
						<div class="activitylist_paixu_right">
								查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
							</div>
							<div class="kong"></div>
					</div>
    <!--状态结束-->
    <table id="contentTable" class="table activitylist_bodyer_table">
        <thead>
            <tr>
                <th width="4%">序号</th>
                <th width="13%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
                <th width="9%">计调</th>
                <th width="6%">团队类型</th>
                <th width="9%">渠道</th>
                <th width="12%">款项</th>
                <th width="5%">销售</th>
                <th width="9%">返佣差额</th>
                <th width="6%">上一环节<br />审批人</th>
                <th width="6%">审核状态</th>
                <th width="5%">打印确认</th>
                <th width="4%">操作</th>
            </tr>
        </thead>
        <tbody>
       		<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
	                 <td colspan="11" style="text-align: center;">
						 暂无搜索结果
	                 </td>
				</tr>
	        </c:if>
	        <c:forEach items="${page.list}" var="reviewInfos" varStatus="s">
				<tr>
	              	<td>${s.count}</td>
	                <td>
	                	<div class="tuanhao_cen onshow">${reviewInfos.groupCode}</div>
	            		<div class="chanpin_cen qtip" title="${reviewInfos.acitivityName}">
	            			<a href="${ctx}/activity/manager/detail/${reviewInfos.productId}" target="_blank">${reviewInfos.acitivityName}</a>
	            		</div>
	        		</td>
	        		<td>${fns:getUserNameById(reviewInfos.meter)}</td>
	                <td>${fns:getStringOrderStatus(reviewInfos.orderStatus)}</td>
	                <td>${reviewInfos.orderCompanyName}</td>
	                <td>${reviewInfos.costname}</td>
	                <td>${fns:getUserNameById(reviewInfos.saler)}</td>
	                <td class="tr"><span class="fbold tdorange">${reviewInfos.currencyName}<fmt:formatNumber type="currency" pattern="#,###.00" value="${reviewInfos.rebatesDiff}" /></span></td>
	                <td>${reviewInfos.beforeReviewName}</td>
	                <td class="invoice_yes">
	                	<!--  	<c:choose>
										<c:when test="${reviewVO.reviewStatus==1 }">
											<c:if test="${reviewInfos.myStatus==1}">待审核</c:if>
											<c:if test="${reviewInfos.myStatus==2}">未通过</c:if>
											<c:if test="${reviewInfos.myStatus==3}">已通过</c:if>
										</c:when>
										<c:otherwise>
											<c:if test="${reviewInfos.reviewStatus==1}">审核中</c:if>
											<c:if test="${reviewInfos.reviewStatus==0}">未通过</c:if>
											<c:if test="${reviewInfos.reviewStatus==2}">已通过</c:if>
											<c:if test="${reviewInfos.reviewStatus==3}">已通过</c:if>
											<c:if test="${reviewInfos.reviewStatus==4}">已取消</c:if>
										</c:otherwise>
									
									</c:choose>-->
									${fns:getNextReview(reviewInfos.rid)}
	                </td>
	                
	                <!-- 处理打印状态2015-04-19 新增：审核通过后显示已打印，鼠标hover后 显示首次打印日期 -->
					<c:choose>
					     <c:when test="${reviewInfos.printFlag == '1'}">
					     		<td class="invoice_yes"><p class="uiPrint">已打印<span style="display: none;">首次打印日期<br><fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${reviewInfos.printTime}"/></span></p></td>
					     </c:when>
					     <c:otherwise>
					            <td>未打印</td>
					     </c:otherwise>
					</c:choose>
	                
	                <td class="p0">
	                    <dl class="handle">
	                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
	                        <dd class="">
		                        <p>
							  		<a href="javascript:rebatesReview.showReviewDetail(${reviewInfos.id})">查看</a><br/>
							  		<a href="${ctx}/cost/manager/forcastList/${reviewInfos.groupid}/${reviewInfos.orderStatus}" target="_blank">预报单</a>
							  		<a href="${ctx}/cost/manager/settleList/${reviewInfos.groupid}/${reviewInfos.orderStatus}" target="_blank">结算单</a>
							  		<!--  <a href="${ctx}/cost/manager/returnMoneyList/${reviewInfos.rid}" target="_blank">返款单</a> -->
									<c:if test="${reviewInfos.myStatus==1 && reviewInfos.reviewStatus==1}">
										<a href="javascript:rebatesReview.showRebatesReview(${reviewInfos.id},${reviewInfos.rid},${reviewVO.userLevel})">审批</a>
									</c:if>

									<!-- 处理是否显示打印按钮 2015-04-19 新增：审核通过或审核通过后的后续操作才显示打印按钮 -->
									<shiro:hasPermission name="rebatesReview:print:down">
										<c:if test="${reviewInfos.reviewStatus == '2'||reviewInfos.reviewStatus == '3'}">
											  <a href="${ctx}/order/rebates/review/rebatesReviewPrint?reviewId=${reviewInfos.rid}&rebatesId=${reviewInfos.id}&groupCode=${reviewInfos.groupCode}" target="_blank">打印</a><br/>
	                                    </c:if>
                                    </shiro:hasPermission>
		                        </p>
	                        </dd>
	                    </dl>
	                </td>
	            </tr>
			</c:forEach>
     	</tbody>
	</table>
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
</body>
</html>