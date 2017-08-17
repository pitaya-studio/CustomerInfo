<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>机票支出凭单</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"  href="${ctxStatic}/css/dayinbdzy.css" />
<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
<script type="text/javascript" src="${ctxStatic}js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<style type="text/css">
	.dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
	.ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
    .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
    .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
</style>
</head>
	<body>
		<div id="printDiv">
			<table class="dayinzy">
				<thead>
					<tr>
						<th class="fr f4 paddr" colspan="8">首次打印日期：
							<c:if test="${map.firstPrintTime != null && map.firstPrintTime != '' }">
								<fmt:formatDate value="${map.firstPrintTime }" type="both" dateStyle="default" pattern="yyyy/MM/dd HH:mm"/>
							</c:if>
						</th>
					</tr>
					<tr>
						<th class="f1" colspan="8">支出凭单</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${map.createDate}"/></th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="f2">${map.groupCodeName}</td>
						<td class="fc f3" colspan="2">
							${map.groupCode}
						</td>
						<td class="fc f2">款项</td>
						<td class="f3" >${map.costname }</td>
						<td class="fc f2">经办人</td>
						<td class="f3" colspan="2">
							${map.operatorName}
						</td>
					</tr>
					<tr>
						<td class="f2">摘要</td>
						<td class="f3" colspan="7">
							<div class="dayinzy_w698">${map.remark}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">收款单位</td>
						<td class="fl f3" colspan="7" >&nbsp;
						<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
							<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && map.orderCompanyName=='非签约渠道'}"> 
			       				直客
			   				</c:when>
							<c:otherwise> 
			       				${map.orderCompanyName}
			   				</c:otherwise>
			   			</c:choose>	
						&nbsp;&nbsp;${map.accountName}
						</td>
					</tr>
					<tr>
						<td class="f2">${map.currencyName }</td>
						<td class="f3" colspan="5" style="text-align:right;">${map.money }</td>
						<td class="f2" >汇率</td>
						<td class="f3">${map.currencyExchangerate }</td>
					</tr>
					<tr>
						<td class="fc f2">合计人民币</td>
						<td class="fl f3" colspan="4">
							${map.totalRMBMoneyName }
						</td>
						<td class="fc f3">¥</td>
						<td class="fl f3" colspan="2" style="text-align:right;">${map.totalRMBMoney}</td>
					</tr>
					<tr>
						<td class="fc f2"><c:if test="${map.isYJXZ eq 'YJXZ'}">实际</c:if>领款人</td>
						<td class="fc f3">
							${map.payee}
						</td>
						<td class="fc f2">主管审批</td>
						<td class="fc f3" colspan="5">
							${map.deptmanager }
						</td>
					</tr>
					<tr>
						<td class="fc f2">总经理</td>
						<td class="fc f3">${map.majorCheckPerson}</td>
						<td class="fc f2">财务主管</td>
						<td class="fc f2">&nbsp;${map.financeManage }</td>
						<td class="fc f2">出纳</td>
						<td class="fc f3">&nbsp;${map.cashier }</td>
						<td class="fc f2">审核</td>
						<td class="fc f3">${map.auditor}</td>
					</tr>
					
					<tr>
						 <%-- 为再改回来备用：现在只留年月日汉字，具体日期手动填写 --%>
					     <%--<fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${payDate}"/> --%>
						<c:if test="${empty map.confirmPayDate }">
							<td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
					     </c:if>
					     <c:if test="${!empty map.confirmPayDate }">
							<td class="fr f3 noborder paddr" colspan="8">确认付款日期：${map.confirmPayDate }</td>
					     </c:if>
					</tr>
				</tbody>
			</table>
		</div>
		<%--S打印&下载按钮--%>
		<div class="dbaniu">
			<shiro:hasPermission name="rebatesReview:print:down">
				<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure('${map.reviewId}');">
				<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downloadJKD('${ctx}/order/newAirticketRebate/airticketRebatesNewDownload?reviewId=${map.reviewId}&payId=${map.payId}&groupCode=${map.groupCode}','${map.reviewId}');">
			</shiro:hasPermission>
		</div>
		<script type="text/javascript">
			function downloadJKD(url,reviewId){
				updateTime(reviewId);	//更新打印时间
				window.location.href=url;
			}
		
			function printTure(reviewId) {
				updateTime(reviewId);	//更新打印时间
				window.opener.$("#searchForm").submit();
				printPage(document.getElementById("printDiv"));
			}
			
			function updateTime(reviewId){
				$.ajax({
			        type: "POST",
			        url: "${ctx}/order/newAirticketRebate/updatePrintTime?reviewId=" + reviewId,
			        dataType:"json",
			        async:false,
			        cache:false,
			        data:{			        	
			        },
			        success: function(data){			        	
			        }
			     });
			}
		</script>
	</body>
</html>