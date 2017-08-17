<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转团转款列表</title>
<meta name="decorator" content="wholesaler"/>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	//成本价滑过显示具体内容
	inquiryPriceCon();
});

//分页
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}

//取消申请

function cancelApp(reviewId){
	if(confirm("确定取消申请?")){
		$.ajax({
			type : "POST",
			url : contextPath+"/orderCommon/transferMoney/transfersMoneyCancel/"+reviewId,
			dataType : "text",
			success : function(html){
				var json;
				try{
					json = $.parseJSON(html);
				}catch(e){
					json = {res:"error"};							
				}
				if(json.res=="success"){
					jBox.tip("操作成功", 'info');
					$("#searchForm").submit();
				}else if(json.res=="data_error"){
					jBox.tip("输入数据不正确", 'error');
				}else{
					jBox.tip(json.res, 'error');
				}
			}
		});
	}
}

</script>
</head>
<body>
<div id="sea">
				<!--右侧内容部分开始-->
                <div class="mod_nav">订单 > 
							<c:choose>
											<c:when test="${order.orderStatus == 1}">单团</c:when>
											<c:when test="${order.orderStatus == 2}">散拼</c:when>
											<c:when test="${order.orderStatus == 3}">游学</c:when>
											<c:when test="${order.orderStatus == 4}">大客户</c:when>
											<c:when test="${order.orderStatus == 5}">自由行</c:when>
								</c:choose>

 						> 转款记录</div>
				<form id="searchForm" action="${ctx}/orderCommon/transferMoney/transfersMoneyHref/${orderId}" method="post" class="formpaixu">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	        </form>
				
		<table class="table activitylist_bodyer_table" id="contentTable">
			<thead>
				<tr>
				             <th width="7%">游客</th>
							 <th width="11%">下单时间</th>
							 <th width="10%">报批日期</th>
							 <th width="10%">应收金额</th>
							 <th width="10%">转款金额</th>
							 <th width="15%">转款原因</th>
							 <th width="15%">状态</th>
							 <th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(page.list) <= 0 }">
	                 <tr class="toptr" >
	                 	<td colspan="12" style="text-align: center;"> 暂无搜索结果</td>
	                 </tr>
        		</c:if>
        		
				<c:forEach items="${page.list }" var="show">
					<tr class="toptr">
						<td>${show.traveler.name}</td>
						<td><fmt:formatDate value="${show.newOrder.createDate}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><fmt:formatDate value="${show.review.createDate}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>${show.orderTotalMoney}</td>
						<td width="10%">${show.transMoney}</td>
						<td>${show.review.createReason}</td>
						<td>
							<!--  	   <c:if test="${show.review.status==0}">已驳回</c:if>
					         	   <c:if test="${show.review.status==1}">审核中</c:if>
					               <c:if test="${show.review.status==2}">审核成功</c:if>
					               <c:if test="${show.review.status==3}">操作完成</c:if>
					               <c:if test="${show.review.status==4}">已取消申请</c:if> -->
							${fns:getNextReview(show.review.id)}
						</td>
						<td>
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_cz.png" title="操作">
								</dt>
								<dd>
									<p>
										<span></span> <a target="_blank"
											href="${ctx}/orderCommon/transferMoney/transferMoneyDetails/${show.review.id}">审核详情
										</a>
										<c:if test="${show.review.status==1}">
											<a target="_blank" onclick="cancelApp(${show.review.id})">
												取消申请 </a>
										</c:if>
									</p>

								</dd>
							</dl>

						</td>
					</tr>
				</c:forEach>
				
			</tbody>
		</table>
	</div>

	<!-- 分页部分 -->
	<div class="pagination">
			<c:if test="${fn:length(page.list) > 0 }">
		  		 ${pageStr}
		   </c:if>
			<div class="ydBtn"><a onclick="window.close();" class="ydbz_s gray">关闭</a></div>
	</div>
	
			
</div>
</body>
</html>
