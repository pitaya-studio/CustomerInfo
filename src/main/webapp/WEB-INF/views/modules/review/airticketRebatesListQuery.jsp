<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>审核-机票返佣</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/airticket/airticketRebatesReviewList.js" type="text/javascript"></script>
	<!-- 20150810 批量审核js -->
	<script src="${ctxStatic}/modules/batchReview/batchReview.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function() {
		//产品销售和下单人切换
		switchSalerAndPicker();
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		 $("#channel").comboboxInquiry();
		 $("#saler").comboboxInquiry();
		 $("#meter").comboboxInquiry();
		 $("#picker").comboboxInquiry();
		 
		//首次打印提醒
		$(".uiPrint").hover(function(){
			$(this).find("span").show();
		},function(){
			$(this).find("span").hide();
		});
		
		function sortby(sortBy,obj){
		    var temporderBy = $("#orderBy").val();
		    if(temporderBy.match(sortBy)){
		        sortBy = temporderBy;
		        if(sortBy.match(/DESC/g)){
		            sortBy = sortBy.replace(/DESC/g,"");
		        }else{
		            sortBy = $.trim(sortBy)+" DESC";
		        }
		    }
		    
		    $("#orderBy").val(sortBy);
		    $("#searchForm").submit();
		}
		
		function statusChoose(statusNum) {
			$("#reviewStatus").val(statusNum);
			$("#searchForm").submit();
		};
		
		//contextPath = $("#ctx").val();
		var _$orderBy = $("#orderBy").val();
	    if(_$orderBy==""){
	        _$orderBy="r.updateDate DESC";
	    }
	    var orderBy = _$orderBy.split(" ");
	    $(".activitylist_paixu_left li").each(function(){
	        if ($(this).hasClass("li"+orderBy[0])){
	            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
	            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
	            $(this).attr("class","activitylist_paixu_moren");
	        }
	    });
	})
		function backOutReview(rid,userLevel){
	
	$.jBox.confirm("确定要取消审核吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				async: false, 
				url : "${ctx}/orderReview/manage/backOutReview",
				data : {
					rid: rid,
					userLevel : userLevel
				},
				success : function(msg) {
					if(msg.result == "1"){
					jBox.tip("撤销成功！");
						$("#searchForm").submit();
					}else{
						jBox.tip(msg.message);
					}
				}
			});
		}
	});
		
}

function refundInputs(obj){
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
    var txt = ms.split(".");
    obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
	   
}
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
	<!-- 顶部参数 -->
 <%--    <page:applyDecorator name="show_head">
	    <page:param name="desc">返佣列表</page:param>
	</page:applyDecorator> --%>

		<page:applyDecorator name="rebates_review_head2">
            	<page:param name="current"><c:choose><c:when test="${reviewVO.orderType==1}">single</c:when><c:when test="${reviewVO.orderType==2}">loose</c:when><c:when test="${reviewVO.orderType==3}">study</c:when><c:when test="${reviewVO.orderType==5}">free</c:when><c:when test="${reviewVO.orderType==4}">bigCustomer</c:when><c:when test="${orderType==6}">visa</c:when><c:when test="${reviewVO.orderType==7}">airticket</c:when><c:when test="${reviewVO.orderType==10}">cruise</c:when></c:choose></page:param>
   		</page:applyDecorator>

	<!--右侧内容部分开始-->
    <form id="searchForm" action="${ctx}/airticketRebates/queryAirticketRebatesList?flag=1" method="post">
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
		<input id="orderType"name="orderType" type="hidden" value="${reviewVO.orderType}" />
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
				<label>团号：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey"
					name="groupCode" value="${reviewVO.groupCode}" type="text" />
			</div>
			
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" value="搜索" type="submit" />
				<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
			</div>
			<div class="zksx">筛选</div>
			<div class="ydxbd">
				
	            <div class="activitylist_bodyer_right_team_co2 pr wpr20">
					<label>订单号：</label>
					<input style="width:130px" id="orderNo" name="orderNo" value="${reviewVO.orderNo}" type="text" />
				</div>				
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">打印状态：</div>
					<select name='printFlag'>
						<option value=""  selected="selected">不限</option>
						<c:forEach items="${printMap}" var="printflag">
							<option value="${printflag.key }" <c:if test="${printflag.key==reviewVO.printFlag}">selected="selected"</c:if>>${printflag.value}</option>
						</c:forEach>
					</select>
				</div>				
				<div class="activitylist_bodyer_right_team_co2">
	                <label class="activitylist_team_co3_text">报批时间：</label>
	                <input id="createTimeBegin" class="inputTxt dateinput" name="createTimeBegin"  value='${reviewVO.createTimeBegin }'  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('createTimeBegin').value==''){$dp.$('createTimeEnd').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>                          
	                <input id="createTimeEnd" class="inputTxt dateinput" name="createTimeEnd" value='${reviewVO.createTimeEnd }'  onClick="WdatePicker()" readonly/>
	            </div>
	            <div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co2 wpr20">
					<div class="activitylist_team_co3_text">渠道：</div>
					<select name="channel" id="channel" >
						<option value=""></option>
						<c:if test="${not empty fns:getAgentList() }">
							<c:forEach items="${fns:getAgentList()}" var="agentinfo">
								<option value="${agentinfo.id }" <c:if test="${agentinfo.id==reviewVO.channel }">selected="selected"</c:if>>${agentinfo.agentName}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">销售：</div>
					<select name="saler" id="saler">
						<option value=""></option>
						<c:forEach items="${users}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }" <c:if test="${userinfo.id==reviewVO.saler }">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co2">
	                <label class="activitylist_team_co3_text">返佣金额：</label>
	                <input id="rebatesDiffBegin" class="inputTxt "  name="rebatesDiffBegin"  value="${reviewVO.rebatesDiffBegin }"  onblur="refundInputs(this)" /> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>                          
	                <input id="rebatesDiffEnd" class="inputTxt "  name="rebatesDiffEnd" value="${reviewVO.rebatesDiffEnd }"  onblur="refundInputs(this)" />
	            </div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co2 wpr20">
					<div class="activitylist_team_co3_text">计调：</div>
					<select name="meter" id ="meter">
						<option value=""></option>
						<c:forEach items="${users}" var="userinfo">
							<option value="${userinfo.id }" <c:if test="${userinfo.id==reviewVO.meter}">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">下单人：</div>
					<select name="picker" id ="picker">
						<option value=""></option>
						<c:forEach items="${users}" var="userinfo">
							<option value="${userinfo.id }" <c:if test="${userinfo.id==reviewVO.picker }">selected="selected"</c:if>>${userinfo.name }</option>
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
                <th width="9%">报批日期</th>
                <th width="9%">订单号</th>
                <th width="9%">团号</th>
                <th width="6%">团队类型</th>
                <th width="6%">渠道商</th>
                <th width="8%">订单总额</th>
                <th width="8%">已付金额<br/>到账金额</th>
                <th width="6%">款项</th>
                <th width="7%">
	            	<span class="order-saler-title on">销售</span>/
	            	<span class="order-picker-title">下单人</span>
            	</th>
                <th width="9%">返佣金额</th>
                <th width="6%">上一环节<br />审批人</th>
                <th width="6%">审核状态</th>
                <th width="5%">打印确认</th>
                <th width="4%">操作</th>
            </tr>
        </thead>
        <tbody>
       		<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
	                 <td colspan="14" style="text-align: center;">
						 暂无搜索结果
	                 </td>
				</tr>
	        </c:if>
	        <c:forEach items="${page.list}" var="reviewInfos" varStatus="s">
				<tr>
	              	<td>
	              	<c:if test="${reviewVO.reviewStatus==1 }">
						 	<input type="checkbox" value="${reviewInfos.nowLevel}@${reviewInfos.rid}" name="activityId" >
						</c:if>
	              	${s.count}</td>
	              	<td>${reviewInfos.createDate}</td>
	              	<td>${reviewInfos.orderNo}</td>
	              	<td><div class="tuanhao_cen onshow">${reviewInfos.groupCode}</div></td>
	              	<td>${fns:getStringOrderStatus(reviewInfos.orderStatus)}</td>
	              	<td>${reviewInfos.orderCompanyName} </td>
	             <!-- 	<td>${fns:getMoneyAmountBySerialNum(reviewInfos.totalMoney, 1)}</td>-->
	              	<!-- add by ruyi.chen 添加预计返佣展示 -->
	              	<td class="tr-payable pr">
	        		<c:if test="${reviewInfos.rebatesSign == 1 }">
	                        	<span class="icon-rebate">
									<span>预计返佣${reviewInfos.rebatesStr }</span>
	                        	</span>
                        	</c:if>
	        		<span class="tdorange fbold">
						${fns:getMoneyAmountBySerialNum(reviewInfos.totalMoney, 1)}
					</span></td>
					
	              	<td>
	              		<div class="yfje_dd">	
							<span class="fbold">${fns:getMoneyAmountBySerialNum(reviewInfos.payedMoney, 1)}</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">${fns:getMoneyAmountBySerialNum(reviewInfos.accountedMoney, 1)}</span>
						</div>
					</td>
	              	<td>${paymentProject}</td>
<!-- 	              	<td>${fns:getUserNameById(reviewInfos.saler)}</td> -->
	              	<td>
                    	<span class="order-saler onshow"><c:if test="${empty reviewInfos.saler or reviewInfos.saler == 0}">——</c:if>${fns:getUserById(reviewInfos.saler).name}</span>
                    	<span class="order-picker"><c:if test="${empty reviewInfos.picker or reviewInfos.picker==0}">——</c:if>${fns:getUserById(reviewInfos.picker).name}</span>
                    </td>	              	
	              	<!-- 
	                <td>
	                	<div class="tuanhao_cen onshow">${reviewInfos.groupCode}</div>
	            		<div class="chanpin_cen qtip" title="${reviewInfos.acitivityName}">
	            			<a href="${ctx}/airTicket/actityAirTickettail/${reviewInfos.productId}" target="_blank">${reviewInfos.acitivityName}</a>
	            		</div>
	        		</td>
	        		<td>${fns:getUserNameById(reviewInfos.meter)}</td>
	        		 -->
	        		
	                <td class="tr">
	                   <c:set value="${reviewInfos.currencyName}" var="currencyNames" />
                       <c:set value=" ${reviewInfos.rebatesDiff}" var="rebatesDiffs" />
                       <c:forEach items="${fn:split(rebatesDiffs, ',')}" var="rebatesDiff" varStatus="i">
                          <p><span class="fbold tdorange">
                          ${fns:getCurrencyNameOrFlag(fn:split(currencyNames, ',')[i.index],"1")}
                          ${rebatesDiff }</p>
                          <%-- <fmt:formatNumber type="currency" pattern="#,#00.00" value="rebatesDiff" />--%>
                          </span> 
	                   </c:forEach>
		                <%--  <span class="fbold tdorange">${reviewInfos.currencyName}
		               <fmt:formatNumber type="currency" pattern="#,###.00" value="${reviewInfos.rebatesDiff}" /> --%>
		                
	                </td>
	                <td>${reviewInfos.beforeReviewName}</td>
	                <td class="invoice_yes">
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
							  		<a href="javascript:rebatesReview.showReviewDetail(${reviewInfos.rid})">查看</a><br/>
							  		<a href="${ctx}/cost/manager/forcastList/${reviewInfos.groupid}/${reviewInfos.orderStatus}" target="_blank">预报单</a>
							  		<a href="${ctx}/cost/manager/settleList/${reviewInfos.groupid}/${reviewInfos.orderStatus}" target="_blank">结算单</a>
									<c:if test="${reviewInfos.myStatus==1 && reviewInfos.reviewStatus==1}">
										<a href="javascript:rebatesReview.showRebatesReview(${reviewInfos.rid},${reviewVO.userLevel})">审批</a>
									</c:if>
									<c:if test="${fns:getUser().id ==reviewInfos.updateBy && reviewVO.userLevel +1 == reviewInfos.nowLevel && reviewInfos.reviewStatus== 1 }">
										<a href="javascript:backOutReview(${reviewInfos.rid},${reviewVO.userLevel})">撤销</a>
									</c:if>
									<!-- 处理是否显示打印按钮 2015-04-19 新增：审核通过或审核通过后的后续操作才显示打印按钮 -->
									<shiro:hasPermission name="rebatesReview:print:down">
										<c:if test="${reviewInfos.reviewStatus == '2'||reviewInfos.reviewStatus == '3'}">  
											  <a href="${ctx}/airticketRebates/airticketRebatesReviewPrint?reviewId=${reviewInfos.rid}&rebatesId=${reviewInfos.rid}&groupCode=${reviewInfos.groupCode}" target="_blank">打印</a><br/>
	                                  </c:if>
                                    </shiro:hasPermission>
		                        </p>
	                        </dd>
	                    </dl>
	                </td>
	            </tr>
			</c:forEach>
            <!-- 20150804 全选反选 -->
			<c:if test="${reviewVO.reviewStatus==1 }">
				<tr class="checkalltd">
					<td colspan='19' class="tl">
						<label>
							<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
						</label>
						<label>
							<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
						</label>
						<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:payedConfirmNew('${ctx}',1,1,'${reviewVO.userLevel }','/order/rebates/review/batchAirticketReviewRebates');" >批量审批</a>
					</td>
				</tr>
			</c:if>
     	</tbody>
	</table>
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
</body>
</html>