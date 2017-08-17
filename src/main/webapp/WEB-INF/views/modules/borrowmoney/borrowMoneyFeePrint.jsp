<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${printFormBean.printFormName }</title>
<!-- 页面左边和上边的装饰 -->

<meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta  charset='utf-8' />
<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">
	function closePrintSheet(url){
		if(window.opener){
			window.close();
		}else{
			window.location.href=window.opener;
		}
	}
	
	function downloadJKD(url){
		if("${printFormBean.printDate}" == '') {
			$.post("${ctx}/printForm/updateReviewPrintInfo", {"reviewId":"${printFormBean.reviewId}", "payId":"${payId}"},
				function(data){
					if(data != '') {
						$("#printDate").text(data);
					}
				}
			);
		}
		window.location.href=url;
	}
</script>
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
						<th class="fr f4 paddr" colspan="8">首次打印日期：<span id="printDate"><fmt:formatDate pattern="yyyy/ MM /dd HH:mm" value="${printFormBean.printDate}"/></span></th>
					</tr>
					<tr>
						<th class="f1" colspan="8">${printFormBean.printFormName }</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${printFormBean.revCreateDate}"/></th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				</table>
			<table  class="dayinzy  dayInIe">
				<tbody>
					<tr>
						<td class="f2">借款单位</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">&nbsp;${printFormBean.borrowDept}</div>
						</td>
						<td class="f2">经办人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320">&nbsp;${printFormBean.operatorName}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">借款理由</td>
						<td class="f3" colspan="7">
							<div class="dayinzy_w698">&nbsp;${printFormBean.revBorrowRemark}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">借款金额</td>
						<td class="f3" colspan="4">
							<div class="dayinzy_w373" style="text-align: left;">人民币${printFormBean.revBorrowAmountDx}</div>
						</td>
						<td class="f2">￥</td>
						<td class="f3" colspan="2">
							<div class="dayinzy_w193" style="text-align: right;">&nbsp;${printFormBean.revBorrowAmount}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">领款人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">&nbsp;${printFormBean.operatorName}</div>
						</td>
						<td class="f2">总经理</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320">&nbsp;${printFormBean.majorCheckPerson}</div>
						</td>
					</tr>
					<tr>
						<td class="f2" width="70">财务主管</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">
							   <c:choose>
							   		<c:when test="${companyid==68}">
										&nbsp;
							   		</c:when>
							   		<c:otherwise>
										&nbsp;${printFormBean.cwmanager}
							   		</c:otherwise>
							   </c:choose>
							</div>
						</td>
						<td class="f2" width="40">出纳</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">
							  <c:choose>
							   		<c:when test="${companyid==68}">
										&nbsp;
							   		</c:when>
							   		<c:otherwise>
										&nbsp;${printFormBean.cashier}
							   		</c:otherwise>
							   </c:choose>
							</div>
						</td>
						<td class="f2" width="70">财务</td>
						<td class="f3" width="110">
<!-- 						   by sy 2015.8.6 -->
<!-- 							<div class="dayinzy_w130">&nbsp;${printFormBean.cw}</div> -->
							<div style="text-align: center;">&nbsp;${printFormBean.cw}</div>
						</td>
						<td class="f2" width="40">审批</td>
						<td class="f3">
<!-- 							<div class="dayinzy_w152">&nbsp;${printFormBean.deptmanager}</div> -->
							<div style="text-align: center;">&nbsp;${printFormBean.deptmanager}</div>
						</td>
					</tr>
					</tbody>
				</table>
				<table  class="dayinzy  TopHeight">
					<tr>
						<!-- 为再改回来备用：现在只留年月日汉字，具体日期手动填写  -->
						<!-- 
					    <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${payDate}"/>
					     -->
						<c:if test="${payStatus == 0 }">
					        <td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
					     </c:if>
					     <c:if test="${payStatus == 1 }">
					        <td class="fr f3 noborder paddr" colspan="8">确认付款日期：<fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${printFormBean.payDate}"/></td>
					     </c:if>
					</tr>
			</table>
		</div>
		<!--S打印&下载按钮-->
		<div class="dbaniu">
			<shiro:hasPermission name="review:print:down">
				<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure();">
				<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downloadJKD('${ctx}/printForm/downloadBorrowMoneySheet?reviewId=${printFormBean.reviewId}&orderType=${orderType}&option=${option}');">
			</shiro:hasPermission>
		</div>
		<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript">
		
			function printTure() {
				if("${printFormBean.printDate}" == '') {
					$.post("${ctx}/printForm/updateReviewPrintInfo", {"reviewId":"${printFormBean.reviewId}"},
						function(data){
							window.opener.$("#searchForm").submit();
							if(data != '') {
								$("#printDate").text(data);
								printPage(document.getElementById("printDiv"));
							} else {
								printPage(document.getElementById("printDiv"));
							}
						}
					);
				} else {
					window.opener.$("#searchForm").submit();
					printPage(document.getElementById("printDiv"));
				}
			}
		</script>
	</body>

		
</html>