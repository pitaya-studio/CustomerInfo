<!-- 特别说明：引入此页面时请注意 ReviewInfo ReviewInfoLog 已使用，请务必不要重复！后台务必传审批唯一标识rid!-->
<%@ page contentType="text/html;charset=UTF-8" %>
	<!-- 审批动态 -->
	<c:set var="ReviewInfo" value="${fns:getReviewInfo(rid)}"/>
	<c:set var="ReviewInfoLog" value="${fns:getReviewLog(rid)}"/>
             <div class="orderdetails_tit">审批动态</div>
             
             <div class="orderdetails2">
             	<ul>
             		<c:if test="${empty  ReviewInfoLog}"><li>暂无审批动态</li></c:if>
             		<c:forEach items="${ReviewInfoLog }" var="ReviewLog">
             			<li><span><fmt:formatDate value="${ReviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>  [ ${fns:getUserById(ReviewLog.createBy).name} ]  ${ ReviewLog.result}
             				<!-- 解决审批通过备注信息无法显示的问题 -->
             				<c:if test="${not empty ReviewLog.remark}">
	             		   &nbsp;,备注： ${ ReviewLog.remark}
	             		</c:if>	
             			
             			</span></li>
             		</c:forEach>
             	</ul>
             	<c:if test="${ ReviewInfo.status ==0}">	             		
						<div class="ydbz_tit">
							<span class="fl">驳回理由</span>
		        		</div>
		                <ul class="spdtai">
							<li style="height: auto;">${ ReviewInfo.denyReason}</li>
						</ul>
				 </c:if>
             </div>