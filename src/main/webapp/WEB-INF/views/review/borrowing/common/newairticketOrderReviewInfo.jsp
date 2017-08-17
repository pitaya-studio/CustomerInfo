<%@ page contentType="text/html;charset=UTF-8"%>  
<div class="ydbz_tit">
					<span class="fl">审批动态</span>
        		</div>
				<ul class="spdtai">
				<c:if test="${not empty rLog}">
					<c:forEach items="${rLog}" var="log" varStatus="s">
					<li><fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;&nbsp;
					 【${fns:getUserNameById(log.createBy)}】&nbsp;&nbsp;
					 
						 ${log.operationDescription}
					 
<!-- 					<c:if  test="${log.operation eq '1'}"> -->
<!-- 						通过 -->
<!-- 					</c:if> -->
<!-- 					<c:if  test="${log.operation eq '2'}"> -->
<!-- 						驳回 -->
<!-- 					</c:if> -->
<!-- 					<c:if  test="${log.operation eq '3'}"> -->
<!-- 						撤销 -->
<!-- 					</c:if> -->
<!-- 					<c:if  test="${log.operation eq '4'}"> -->
<!-- 						取消 -->
<!-- 					</c:if> -->
					 &nbsp;&nbsp;
<!-- 					 	审批备注：${log.remark } -->
					 </li>
					</c:forEach>
				
				</c:if>
				<c:if test="${empty rLog}">
					<li>暂无审批动态</li>
				</c:if>
				</ul>	
<!-- 				<c:if test="${review.denyReason!=null&&review.denyReason!=''}"> -->
<!-- 	                <div class="ydbz_tit"> -->
<!-- 						<span class="fl">驳回理由</span> -->
<!-- 	        		</div> -->
<!-- 	                <ul class="spdtai"> -->
<!-- 				    <li>${review.denyReason}</li> -->
<!-- 			    </c:if> -->
</ul>