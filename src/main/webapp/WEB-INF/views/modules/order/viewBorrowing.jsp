<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>借款记录</title>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	g_context_url="${ctx}";
	//操作浮框
	operateHandler();
	yd_dt_Handler();
});
//撤销借款
function  cancelBorrowAmount(status,id){
	if (status!=1){
		top.$.jBox.tip('当前状态不能撤销');
		return false;
	}
	$.ajax({                 
		cache: true,                 
		type: "POST",                 
		url:g_context_url+ "/order/lendmoney/cancelBorrowAmount",                 
		data:{id:id},                
		async: false,                 
		error: function(request) {                     
			top.$.jBox.tip('操作失败');
		},                 
		success: function(data) { 
			 if(data!=""){
				 top.$.jBox.tip(data,'warning');
				 return false;}
			top.$.jBox.tip('操作成功');
			window.location.href=g_context_url+"/order/lendmoney/borrowAmountList";
		}             
	});
	}
	function cancleReview(rid){
$.jBox.confirm("确定要取消审核吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
	                type: "POST",
	                url: "${ctx}/orderCommon/manage/cancleAudit",
	                data: {
	                    id:rid
	                },
	                success: function(msg){
	                    location.reload();
	                }
	            });
			}
		});
			
}
</script>
</head>
<body>
				<!--右侧内容部分开始-->
                <div class="mod_nav">订单&nbsp;&nbsp;> 
                <c:if test="${productType==1 }">单团</c:if>
				<c:if test="${productType==2 }">散拼</c:if>
				<c:if test="${productType==3 }">游学</c:if>
				<c:if test="${productType==4 }">大客户</c:if>
				<c:if test="${productType==5 }">自由行</c:if>
				<c:if test="${productType==10 }">游轮</c:if>&nbsp;&nbsp;>借款记录</div>
<!--                 <div class="filter_btn"><a href="${ctx}/orderCommon/manage/applyBorrowing?orderId=${orderId }&productType=${productType}" class="btn btn-primary" target="_blank">申请借款</a> </div> -->
                 <input type="hidden" name="orderId" value="${orderId }"></input> 
				<table id="contentTable" class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="10%">报批日期</th>
							<th width="10%">游客/团队</th>
							<%-- <th width="10%">款项</th> 
							     <th width="10%">币种</th> --%>
							<th width="12%">借款金额</th>
							<th width="12%">申请人</th>
							<th width="19%">状态</th>
							<th width="6%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${fn:length(bAList) <= 0 }">
	                 <tr class="toptr" >
	                 	<td colspan="15" style="text-align: center;"> 暂无搜索结果</td>
	                 </tr>
        		    </c:if>
					 <c:forEach var="entry" items="${bAList}" >
					<tr>
					    <input type="hidden" name="id" value="${entry.reviewId }"></input>
					    <input type="hidden" name="status" value="${entry.status }"></input>					   
						<td class="tc"><fmt:formatDate value="${entry.applyDate }" pattern="yyyy-MM-dd" /></td>
						<td class="tc">${entry.travelerName }</td>
						<%-- <td class="tc">${entry.createReason }</td> 
						<td class="tc">${entry.currencyIds }</td>--%>
						<td class="tc">${entry.currencyIds}</td>
						<td>${fns:getUserById(entry.createBy).name}</td>
						<td class="tc">
						        ${fns:getNextReview(entry.reviewId) }
						</td>
						<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
								<dd class="">
									<p>
										<span></span> 
										<a href="${ctx}/orderCommon/manage/viewBorrowingInfo?orderId=${orderId }&reviewId=${entry.reviewId }" >借款详情</a>
										<c:if test="${entry.status ==1 }">
										<a href="javascript:void(0)" onclick="cancleReview(${entry.reviewId })">撤销</a>
										</c:if>
										
									</p>
								</dd>
							</dl>
						</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
				<!--右侧内容部分结束-->
</body>
</html>
