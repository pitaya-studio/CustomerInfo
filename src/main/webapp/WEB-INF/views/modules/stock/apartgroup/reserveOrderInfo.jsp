<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>散拼产品切位订单信息</title>
<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
<style type="text/css">
.ydBtn {
	PADDING-BOTTOM: 10px; MARGIN-TOP: 50px; WIDTH: 278px; HEIGHT: 28px; MARGIN-LEFT: auto; MARGIN-RIGHT: auto
}
</style>
	<script type="text/javascript">
	   function downloads(docid,activitySerNum,groupCode,acitivityName,iszip){
            if(iszip){
                var zipname = activitySerNum+'-'+groupCode;
                window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
            }
                
            else
                window.open("${ctx}/sys/docinfo/download/"+docid);
        }
	</script>
</head>
<body>
        <!--<page:applyDecorator name="stock_op_head">
            <page:param name="current">stock</page:param>
        </page:applyDecorator>
        -->
        <div class="ydbzbox fs">
	
    <div class="orderdetails_tit">
			<span>1</span>
			产品信息  
		</div>
            <p class="ydbz_mc">${activityGroup.travelActivity.acitivityName }</p>
            <div class="orderdetails1">
                   <span>旅游类型：${activityGroup.travelActivity.travelTypeId }</span>
                   <span>出发地：${activityGroup.travelActivity.fromAreaName }</span>
                   <span>产品编号：${activityGroup.travelActivity.activitySerNum }</span>
                   <span>行程天数：${activityGroup.travelActivity.activityDuration }</span>
                   <span>渠道名称：
                   <c:forEach var="agentinfo" items="${agentinfoList }">
                  		<c:if test="${activityReserveOrder.agentId==agentinfo.id}">${agentinfo.agentName }</c:if>
                   </c:forEach></span>
                   <span>特殊人群备注：</span>${activityGroup.travelActivity.specialRemark}
            <span class="agentInfo"></span>
            
            
            </div>
       <div class="orderdetails_tit">
			<span>2</span>
			切位订单信息
			</div>
            
       
        <div class="orderdetails1_bt">
			<div class="add2_line_text"></div>
		</div>
 		<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>
		<table class="table table-striped table-bordered table-condensed table-mod2-group">
		<thead>
			<tr>
				<th width="7%" rowspan="2">出团日期</th>
					<th width="6%" rowspan="2">订金(元)</th>
					<th width="6%" rowspan="2">切位人数</th>
					<th width="4%" rowspan="2">余位</th>
					<th width="6%" rowspan="2">预订人</th>
					<th width="18%" colspan="3" class="t-th2">同行价</th>
					<th width="6%" rowspan="2">单房差</th>
					<th width="6%" rowspan="2">预收</th>					
					<th width="8%" rowspan="2">收款时间</th>
					<th width="6%" rowspan="2">收款方式</th>
				  <th width="5%" rowspan="2">收款凭证</th>
					<th width="6%" rowspan="2">备注</th>
				</tr>
				<tr>
					<th width="6%">成人</th>
					<th width="6%">儿童</th>
					<th width="6%">特殊人群</th>
			</tr>
		</thead>
      <tbody><fmt:formatNumber type="currency" pattern="#,##0.00" value="" />
     
           
            <tr>
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${activityReserveOrder.startDate}"/></td>
                <td class="tc"><c:if test="${not empty activityReserveOrder.orderMoney}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityReserveOrder.orderMoney}" /></td>
                <td class="tc">${activityReserveOrder.payReservePosition }</td>
                <td class="tc">${activityGroup.freePosition}</td>
                <td class="tc">${activityReserveOrder.reservation}</td>
                <td class="tc"><c:if test="${not empty activityGroup.settlementAdultPrice}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.settlementAdultPrice}" /></td>
                <td class="tc"><c:if test="${not empty activityGroup.settlementcChildPrice}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.settlementcChildPrice}" /></td>
                <td class="tc"><c:if test="${not empty activityGroup.settlementSpecialPrice}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.settlementSpecialPrice}" /></td>
                <td class="tc"><c:if test="${not empty aactivityGroup.singleDiff}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##00.00" value="${activityGroup.singleDiff}" /></td>
                <td class="tc">${activityGroup.planPosition}</td>               
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${activityReserveOrder.createDate}"/></td>
                <td class="tc">${activityReserveOrder.payTypeLabel}</td>
                <td class="tc"><c:if test="${payVoucherIds!='' }">
                <a href="javascript:void(0)" onClick="downloads('${payVoucherIds }','${activityGroup.travelActivity.activitySerNum }','${activityGroup.groupCode }','${activityGroup.travelActivity.acitivityName }',true)">下载</a>
                </c:if>
                <c:if test="${group.payVoucherIds=='' }">-</c:if>
                </td>
                <td class="tc">${activityReserveOrder.remark}</td>
            </tr>
       
       
        </tbody>
	</table>
	

	</div>
</body>
</html>
