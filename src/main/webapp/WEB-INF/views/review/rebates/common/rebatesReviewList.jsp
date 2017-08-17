<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		<title>宣传费-列表</title>
	</c:when>
	<c:otherwise>
		<title>返佣-列表</title>
	</c:otherwise>
</c:choose>   
	
	<meta name="decorator" content="wholesaler" />
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/>
	<link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"/>
	
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/review/common/batchReview.js"></script>
	<script type="text/javascript" src="${ctxStatic}/review/rebates/common/rebatesReview.js"></script>
	<script type="text/javascript">
		function payedConfirmNew(ctx, url) {
			var str = "";
			var num = 0; // 审批项目数量
			$('[name=activityId]:checkbox:checked').each(function () {
				str += $(this).val() + ",";
				num++;
			});

			if ("" == str) {
				$.jBox.tip("请选择需要审批的记录！");
				return false;
			}

			var theurl = "iframe:" + ctx + "/orderPay/returnMoneyConfirm?type=1&chosenNum=" + num;
			$.jBox(theurl, {
				title: "批量审批",
				width: 830,
				height: 300,
				iframeScrolling: 'no', // iframe 不使用滚动条
				buttons: {
					'取消': 2,
					'驳回': 0,
					'通过': 1
				},
				persistent: true,
				loaded: function (h) {
				},
				submit: function (v, h, f) {
					if (v == 1 || v == 0) {
						var remark = $(h.find("iframe")[0].contentWindow.remarks).val();
						if (remark.length > 100) {
							$.jBox.tip("字符长度过长，请输入少于100个字符", 'error');
							return;
						}
						dataparam = {
							revids: str,
							remark: remark,
							result: v
						};
						console.log(ctx + url);
						$.ajax({
							type: "POST",
							url: ctx + url,
							dataType: "json",
							data: dataparam,
							success: function (data) {
								if (data.result == "success") {
									$("#searchForm").submit();
								} else {
									$.jBox.tip(data.msg);
								}
							}
						});
					}

				}
			});
		}
	</script>
</head>
<body>
	<input type="hidden" id="ctx" value="${ctx}"/>
	
	<form id="searchForm" action="${ctx}/newRebatesReview/list" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		<input id="tabStatus" name="tabStatus" type="hidden" value="${conditionsMap.tabStatus}" />
		<input id="ctx" type="hidden" value="${ctx}" />
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<input id="groupCode" name="groupCode" class="inputTxt searchInput inputTxtlong" value="${conditionsMap.groupCode}" placeholder="团号/产品名称/订单号"
				onkeyup="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()"/>
				<%--<span class="ipt-tips" style="display: block;">团号/产品名称/订单号</span>--%>
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" value="搜索" type="submit">
			</div>
			<div class="ydxbd">
				<span></span>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">产品类型：</label>
					<div class="selectStyle">
						<select name="productType">
							<c:forEach var="order" items="${fns:getDictList('order_type')}">
								<option value="${order.value }"
									<c:if test="${conditionsMap.productType==order.value}">selected="selected"</c:if>>${order.label}
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co3">
					<label class="activitylist_team_co3_text">渠道选择：</label>
					<select name="agentId" id="agentId" class="width-select-channel" >
						<option value="">全部</option>
						<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:if test="${not empty fns:getAgentListAddSort()}">
							<c:forEach items="${fns:getAgentListAddSort()}" var="agentinfo">
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
					<label class="activitylist_team_co3_text">出纳确认：</label>
					<div class="selectStyle">
						<select id="payStatus" name="payStatus">
							<option value="">全部</option>
							<option value="0" <c:if test="${conditionsMap.payStatus == '0' }"> selected="selected" </c:if>>未付</option>
							<option value="1" <c:if test="${conditionsMap.payStatus == '1' }"> selected="selected" </c:if>>已付</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">审批发起人：</div>
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
					 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
					<c:choose>
						<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							  <label class="activitylist_team_co3_text">宣传费金额：</label>
						</c:when>
						<c:otherwise>
							  <label class="activitylist_team_co3_text">返佣金额：</label>
						</c:otherwise>
					</c:choose>   
	              
	                <input id="rebatesDiffBegin" class="inputTxt " name="rebatesDiffBegin"  value='${conditionsMap.rebatesDiffBegin }' onblur="refundInputs(this)"/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>                          
	                <input id="rebatesDiffEnd" class="inputTxt " name="rebatesDiffEnd" value='${conditionsMap.rebatesDiffEnd }' onblur="refundInputs(this)" />
	            </div>
				<c:if test="${not empty multiRebateObject and multiRebateObject eq true}">
				<div class="activitylist_bodyer_right_team_co1">
					 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
					<c:choose>
						<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							 <label class="activitylist_team_co3_text">宣传费对象：</label>
						</c:when>
						<c:otherwise>
							  <label class="activitylist_team_co3_text">返佣对象：</label>
						</c:otherwise>
					</c:choose>
					<div class="selectStyle">
						<select name="supplierId">
							<option value="">请选择</option>
							<c:if test="${not empty suppliers}">
								 <c:forEach items="${suppliers}" var="supplier">
									<option value="${supplier.id}" <c:if test="${supplier.id eq supplierId}">selected</c:if> >${supplier.name}</option>
								</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				</c:if>
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
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">申请日期：</label>
					<input id="" class="inputTxt dateinput" name="applyDateFrom" value="${conditionsMap.applyDateFrom}" onclick="WdatePicker()" readonly="readonly" />
					<span>至 </span>
					<input id="" class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}" onclick="WdatePicker()" readonly="readonly" />
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

				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">打印状态：</div>
					<select name='printFlag'>
						<option value=""  selected="selected">全部</option>
						<c:forEach items="${printMap}" var="printflag">
							<option value="${printflag.key }" <c:if test="${printflag.key==reviewVO.printFlag}">selected="selected"</c:if>>${printflag.value}</option>
						</c:forEach>
					</select>
				</div> --%>
			</div>
			<div class="kong"></div>
		</div>
	</form>

	<!--状态开始-->
	<div class="supplierLine">
		<a href="javascript:void(0)" id="all" onClick="statusChooses('0')" <c:if test = "${conditionsMap.tabStatus == null || conditionsMap.tabStatus == 0 || conditionsMap.tabStatus == '0'}">class="select" </c:if>>全部</a>
		<a id="todo" href="javascript:void(0)" onClick="statusChooses('1')" <c:if test = "${conditionsMap.tabStatus == '1' || conditionsMap.tabStatus == 1}">class="select" </c:if>> 待本人审批</a>
		<a id="todo" href="javascript:void(0)" onClick="statusChooses('2')" <c:if test = "${conditionsMap.tabStatus == '2' || conditionsMap.tabStatus == 2}">class="select" </c:if>>本人已审批</a>
		<a id="todoing" href="javascript:void(0)" onClick="statusChooses('3')" <c:if test = "${conditionsMap.tabStatus == '3' || conditionsMap.tabStatus == 3}">class="select" </c:if>>非本人审批</a>
	</div>
	<!--状态结束-->
    <div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu_left">
           <ul>
            <li class="activitylist_paixu_left_biankuang lir.create_date"><a onClick="sortby('r.create_date',this)">创建时间</a></li>
		    <li class="activitylist_paixu_left_biankuang lir.update_date"><a onClick="sortby('r.update_date',this)">更新时间</a></li>
           </ul>
		</div>
		<div class="activitylist_paixu_right">
			查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
		</div>
	</div>
    
    <table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
            <tr>
                <th width="5%">序号</th>
                <th width="8%">订单号</th>
                <th width="9%">
                	
                	<span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span>
                </th>
                <th width="4%">产品类型</th>
                <th width="6%">申请时间</th>
                <th width="6%">审批发起人</th>
                <th width="7%">
					<c:choose>
						<c:when test="${not empty multiRebateObject and multiRebateObject eq true} && ${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'} ">
							<span class="rebate_channelTh <c:if test='${empty conditionsMap.agentId and not empty supplierId}'>unactive</c:if>">渠道</span>
							/
							<span class="rebate_supplyTh <c:if test='${not empty conditionsMap.agentId or empty supplierId}'>unactive</c:if>">宣传费对象</span>
						</c:when>
						<c:when test="${not empty multiRebateObject and multiRebateObject eq true} && ${fns:getUser().company.uuid ne  '049984365af44db592d1cd529f3008c3'} ">
							<span class="rebate_channelTh <c:if test='${empty conditionsMap.agentId and not empty supplierId}'>unactive</c:if>">渠道</span>
							/
							<span class="rebate_supplyTh <c:if test='${not empty conditionsMap.agentId or empty supplierId}'>unactive</c:if>">返佣对象</span>
						</c:when>
						<c:otherwise>渠道商</c:otherwise>
					</c:choose>
				</th>
                <th width="8%">订单金额</th>
                <th width="8%">已收金额<br/>到账金额</th>
                <th width="8%">款项名称</th>
                <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
				<c:choose>
					<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					    <th width="8%">宣传费金额</th>
					</c:when>
					<c:otherwise>
					    <th width="8%">返佣金额</th>
					</c:otherwise>
				</c:choose> 
                <th width="6%">上一环节<br />审批人</th>
                <th width="6%">审批状态</th>
                <th width="5%">出纳确认</th>
                <th width="4%">操作</th>
            </tr>
        </thead>
        <tbody>
       		<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
	                 <td colspan="15" style="text-align: center;">
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
	                	<div title="4535DFDSF" class="tuanhao_cen onshow">${reviewInfos.groupCode}</div>
						<div title="${reviewInfos.productName}" class="chanpin_cen qtip">
							<c:if test="${reviewInfos.productType ==7}">
								<a href="${ctx}/airTicket/actityAirTickettail/${reviewInfos.productId}" target="_blank">${reviewInfos.productName}</a>
							</c:if>
							<c:if test="${reviewInfos.productType !=7 && reviewInfos.productType !=6}">
								<a href="${ctx}/activity/manager/detail/${reviewInfos.productId}?isOp=0" target="_blank">${reviewInfos.productName}</a>
							</c:if>
							<c:if test="${reviewInfos.productType ==6}">
								<a href="${ctx}/visa/preorder/visaProductsDetail/${reviewInfos.productId}" target="_blank">${reviewInfos.productName}</a>
							</c:if>
						</div>
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
							<c:when test="${not empty multiRebateObject and multiRebateObject eq true}">
							    <c:choose>
								   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && reviewInfos.agent==-1}"> 
								       <span class="rebate_channel">直客</span>
										<span class="rebate_supply">
											<c:if test="${reviewInfos.relatedObjectType eq 2}">
												直客
											</c:if> 
										</span>
								   </c:when>
								   <c:otherwise>
								       <span class="rebate_channel">${fns:getAgentName(reviewInfos.agent)}</span>
										<span class="rebate_supply">
											<c:if test="${reviewInfos.relatedObjectType eq 2}">
												${reviewInfos.relatedObjectName}
											</c:if>
										</span>
								   </c:otherwise>
								 </c:choose>
							</c:when>
							<c:otherwise>
							  <c:choose>
								 <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && reviewInfos.agent==-1}"> 
								       直客
								   </c:when>
								   <c:otherwise>
								       ${fns:getAgentName(reviewInfos.agent)}
								   </c:otherwise>
							   </c:choose>
							</c:otherwise>
						</c:choose>
					</td>
	        		<td class="tr-payable pr">
						<c:if test="${reviewInfos.rebatesSign == 1 }">
							<span class="icon-rebate">
							  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
							<c:choose>
								<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
								     <span>预计宣传费${reviewInfos.rebatesStr }</span>
								</c:when>
								<c:otherwise>
								   	<span>预计返佣${reviewInfos.rebatesStr }</span>
								</c:otherwise>
							</c:choose> 
							</span>
						</c:if>
						<!-- 修复bug15017，将币种符号与金额分开 -->
						<span class="tdorange fbold"><c:if test="${reviewInfos.totalMoney != '' }">${reviewInfos.totalMoney}</c:if></span>
					</td>
	        		<td>
		        		<div class="yfje_dd">
		        			<!-- 修复bug15017，将币种符号与金额分开 -->
							<span class="fbold"><c:if test="${reviewInfos.payedMoney != '' }">${reviewInfos.payedMoney}</c:if></span>
						</div>
		        		<div class="dzje_dd">
		        			<!-- 修复bug15017，将币种符号与金额分开 -->
							<span class="fbold"><c:if test="${reviewInfos.accountedMoney != '' }">${reviewInfos.accountedMoney}</c:if></span>
						</div>
	        		</td>
	        		<td>
	        			<c:choose>
                       		<c:when test="${reviewInfos.productType == 6} && ${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}">
                       			签证宣传费
                       		</c:when>
                       		<c:when test="${reviewInfos.productType == 6} && ${fns:getUser().company.uuid ne '049984365af44db592d1cd529f3008c3'}">
                       			签证返佣
                       		</c:when>
                       		<c:when test="${reviewInfos.productType == 7}">
                       			${reviewInfos.costName}
                       		</c:when>
                       		<c:otherwise>
                       			${reviewInfos.costName}
                       		</c:otherwise>
                       	</c:choose>
	        		</td>
	        		<td class="tc">
	        			<span class="fbold tdred">${reviewInfos.markTotalMoney}</span>
	        		</td>
	        		<td class="tc">${fns:getUserNameById(reviewInfos.lastReviewer)}</td>
                	<c:choose>
                		<c:when test="${reviewInfos.status == 0}">
                			<td class="invoice_back tc">${reviewInfos.statusdesc}</td>
                		</c:when>
                		<c:otherwise>
                			<td class="invoice_yes tc">${reviewInfos.statusdesc}</td>
                		</c:otherwise>
                	</c:choose>
	                <td class="invoice_no tc">
	                	<c:if test = "${reviewInfos.paystatus == 'true'}">已付</c:if>
	                	<c:if test = "${reviewInfos.paystatus == 'false'}">未付</c:if>
	                </td>
					<%-- <c:choose>
					     <c:when test="${reviewInfos.printFlag == '1'}">
					     		<td class="invoice_yes"><p class="uiPrint">已打印<span style="display: none;">首次打印日期<br><fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${reviewInfos.printTime}"/></span></p></td>
					     </c:when>
					     <c:otherwise>
					            <td>未打印</td>
					     </c:otherwise>
					</c:choose> --%>
	                
	                <td class="p0">
	                    <dl class="handle">
	                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
	                        <dd class="">
		                        <p>
		                        	<c:choose>
		                        		
		                        		<c:when test="${reviewInfos.productType == 6}">
		                        			<a href="${ctx}/visa/rebate/visaRebateDetail?orderId=${reviewInfos.orderId}&reviewId=${reviewInfos.id}" target="_blank">详情</a>
		                                    <shiro:hasPermission name="rebatesReview:print:down">
			                                     <c:if test="${reviewInfos.status == '2'}">  
			                                     	<a href="${ctx}/newRebatesReview/visaRebatesPrintforReview?reviewId=${reviewInfos.reviewId}&rebatesId=${reviewInfos.id}&groupCode=${reviewInfos.groupCode}" target="_blank">打印</a>
			                        		     </c:if> 
		                        		    </shiro:hasPermission>
		                        		</c:when>
		                        	                		
		                        		<c:when test="${reviewInfos.productType == 7}">
		                        			<a href="${ctx}/order/newAirticketRebate/airticketRebatesDetail?reviewId=${reviewInfos.id}" target="_blank">查看</a>
		                        			<shiro:hasPermission name="rebatesReview:print:down">
												<c:if test="${reviewInfos.status == '2'}">
													  <a href="${ctx}/order/newAirticketRebate/airticketRebatesPrintforReview?reviewId=${reviewInfos.reviewId}&rebatesId=${reviewInfos.id}&groupCode=${reviewInfos.groupCode}" target="_blank">打印</a><br/>
			                                    </c:if>
		                                    </shiro:hasPermission> 
		                        		</c:when>
		                        		<c:otherwise>
		                        			<a href="${ctx}/rebatesNew/rebatesInfo?rebatesId=${reviewInfos.rebatesId}" target="_blank">查看</a>
		                        			<shiro:hasPermission name="rebatesReview:print:down">
												<c:if test="${reviewInfos.status == '2'}">
													  <a href="${ctx}/newRebatesReview/groupRebatesPrintforReview?reviewId=${reviewInfos.reviewId}&rebatesId=${reviewInfos.id}&groupCode=${reviewInfos.groupCode}" target="_blank">打印</a><br/>
			                                    </c:if>
		                                    </shiro:hasPermission>
		                        		</c:otherwise>
		                        	</c:choose>
							  		<!-- 单团，散拼，自由行，大客户，游学，游轮 -->
									<c:if test="${reviewInfos.productType < 6 or reviewInfos.productType == 10}">
										<a href="${ctx}/cost/manager/forcastList/${reviewInfos.groupId}/${reviewInfos.productType}" target="_blank">预报单</a>
										<a href="${ctx}/cost/manager/settleList/${reviewInfos.groupId}/${reviewInfos.productType}" target="_blank">结算单</a>
									</c:if>
									<!-- 机票和签证 -->
									<c:if test="${reviewInfos.productType == 6 or reviewInfos.productType == 7}">
										<c:if test="${reviewInfos.productType == 6}">
											<a href="${ctx}/cost/manager/forcastList/${reviewInfos.productId}/${reviewInfos.productType}" target="_blank">预报单</a>
											<a href="${ctx}/cost/manager/settleList/${reviewInfos.productId}/${reviewInfos.productType}" target="_blank">结算单</a>
										</c:if>
										<c:if test="${reviewInfos.productType == 7}">
											<a href="${ctx}/cost/manager/forcastList/${reviewInfos.productId}/${reviewInfos.productType}" target="_blank">预报单</a>
											<a href="${ctx}/cost/manager/settleList/${reviewInfos.productId}/${reviewInfos.productType}" target="_blank">结算单</a>
										</c:if>
									</c:if>
									<c:if test="${reviewInfos.isCurReviewer == true}">
										<c:choose>
			                        		<c:when test="${reviewInfos.productType == 6}">
			                        			<a href="${ctx}/visa/rebate/visaRebateDetail?orderId=${reviewInfos.orderId}&reviewId=${reviewInfos.id}&isReview=true" target="_blank">审批</a>
			                        		</c:when>
			                        		<c:when test="${reviewInfos.productType == 7}">
			                        			<a href='${ctx}/order/newAirticketRebate/airticketRebatesApproval?reviewId=${reviewInfos.id}' target = "_blank">审批</a>
			                        		</c:when>
			                        		<c:otherwise>
			                        			<a href="${ctx}/newRebatesReview/rebatesDetail?rebatesId=${reviewInfos.rebatesId}&productType=${reviewInfos.productType}" target = "_blank">审批</a>
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
						<input class="btn ydbz_x" type="button" value="批量审批" target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:payedConfirmNew('${ctx}','/newRebatesReview/batchReview');" >
					</dd>
				</dl>
			</div>
		</div>
	</c:if>
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
</body>
</html>