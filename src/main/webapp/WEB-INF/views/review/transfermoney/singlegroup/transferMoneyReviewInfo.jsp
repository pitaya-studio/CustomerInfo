<%@ page contentType="text/html;charset=UTF-8"%>  
<div class="ydbz_tit">
	<span class="fl">审核动态</span>
</div>
	<ul class="spdtai">
		<c:if test="${not empty hashMap.reviewLogInfo}">
			<c:forEach items="${hashMap.reviewLogInfo}" var="log" varStatus="s">
				<li><fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;
					【${fns:getUserNameById(log.createBy)}】&nbsp;&nbsp;${log.result}</li>
			</c:forEach>
	
		</c:if>
		<c:if test="${empty hashMap.reviewLogInfo}">
			<li>暂无审核动态</li>
		</c:if>
	</ul>
	<c:if test="${ hashMap.reviewInfo.status ==0}">
		<div class="ydbz_tit">
			<span class="fl">驳回理由</span>
		</div>
		<ul class="spdtai">
			<li>${ hashMap.reviewInfo.denyReason}</li>
		</ul>
	</c:if>
	
	<div class="ydBtn ydBtn2">
		<a class="ydbz_s gray"
			href="javascript:window.opener=null;window.close();">关闭</a>
	</div>


</ul>