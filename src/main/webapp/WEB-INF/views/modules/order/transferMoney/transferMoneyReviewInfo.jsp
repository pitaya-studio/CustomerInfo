<%@ page contentType="text/html;charset=UTF-8"%>  
<div class="ydbz_tit">
					<span class="fl">审核动态</span>
        		</div>
				<ul class="spdtai">
				<c:if test="${not empty rLog}">
					<c:forEach items="${rLog}" var="log" varStatus="s">
					<li><fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;&nbsp;
					 【${fns:getUserNameById(log.createBy)}】&nbsp;&nbsp;${log.result}
					 <!-- 解决审核通过备注信息无法显示的问题 -->
             				<c:if test="${not empty log.remark}">
	             		   &nbsp;,备注： ${ log.remark}
	             		</c:if>
					 </li>
					</c:forEach>
				
				</c:if>
				<c:if test="${empty rLog}">
					<li>暂无审核动态</li>
				</c:if>
				</ul>	
				<c:if test="${review.denyReason!=null&&review.denyReason!=''}">
	                <div class="ydbz_tit">
						<span class="fl">驳回理由</span>
	        		</div>
	                <ul class="spdtai">
				    <li>${review.denyReason}</li>
			    </c:if>
</ul>