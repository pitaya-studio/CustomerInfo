<!-- 预算成本审批日志，实际成本审批日志，付款审批日志 add by shijun.liu  -->
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 预算成本审批日志 -->
<c:set var="budgetReviewLog" value="${budgetReviewLog}" />
<!-- 实际成本审批日志 -->
<c:set var="actualReviewLog" value="${actualReviewLog}" />
<!-- 成本付款审批日志 -->
<c:set var="paymentReviewLog" value="${paymentReviewLog}" />
<!-- 预算成本审批日志 -->
<c:forEach var="budgetLog" items="${budgetReviewLog}" varStatus="status" >
	<div class="content">
	    <article>
	    <h3>${budgetLog.costName}-预算成本审批<c:if test="${budgetLog.deleteFlag eq '1'}">【已删除】</c:if></h3>
	    <c:forEach var="reviewLog" items="${budgetLog.logs}" varStatus="status">
	    	<c:set var="review" value="${fns:getReviewNewByUuid(reviewLog.reviewId)}" />
	    	<!-- 审批通过/撤销  -->
	    	<c:if test="${reviewLog.operation eq '1' or reviewLog.operation eq '4'}">
		        <section>
		            <span class="point-time step_yes"></span>
		            <time>
		                <span class="text-green"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		            </time>
		            <aside>
		            	<p class="things text-green"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserNameById(reviewLog.createBy)}</span>
		            	<span>
		            		<c:if test="${reviewLog.operation eq '1'}">审批通过</c:if>
		            		<c:if test="${reviewLog.operation eq '4'}">撤销</c:if>
		            	</span>
		            	<span>备注：${reviewLog.remark}</span></p>
		            </aside>
		        </section>
	        </c:if>
	        <!-- 审批驳回 -->
	        <c:if test="${reviewLog.operation eq '2' }">
		        <section>
		            <span class="point-time stop"></span>
		            <time>
		                <span class="text-red"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		            </time>
		            <aside>
		            	<p class="things text-red"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserNameById(reviewLog.createBy)}</span><span>审批驳回</span><span>备注：${reviewLog.remark}</span></p>
		            </aside>
		        </section>
	        </c:if>
	        <!-- 审批驳回  结束标志-->
	        <c:if test="${review.status eq '0' and reviewLog.sequenceNumber eq fn:length(budgetLog.logs)}">
	        	<section>
	            	<span class="point-time end"></span>
	            	<aside>
	            		<p class="things text-red"></p>
	            		<p class="things text-red">审批驳回</p>
	            	</aside>
	        	</section>
	        </c:if>
	        <!-- 审批通过 结束标志-->
	        <c:if test="${review.status eq '2' and reviewLog.sequenceNumber eq fn:length(budgetLog.logs)}">
	        	<section>
	            	<span class="point-time yes"></span>
	            	<aside>
	            		<p class="things text-green"></p>
	            		<p class="things text-green">审批通过</p>
	            	</aside>
	        	</section>
	        </c:if>
	        <!-- 审批取消,审批中 结束标志-->
	        <c:if test="${review.status ne '0' and review.status ne '2' 
	        			and reviewLog.sequenceNumber eq fn:length(budgetLog.logs) and reviewLog.sequenceNumber ne 1}">
	        	<section>
	            	<span class="point-time next"></span>
	            	<time> </time>
	            	<aside> </aside>
	        	</section>
	        </c:if>
	    </c:forEach>    
		</article>
	</div>
</c:forEach>


<!-- 实际成本审批日志 -->
<c:forEach var="actualLog" items="${actualReviewLog}" varStatus="status">
	<div class="content">
	    <article>
	    <h3>${actualLog.costName}-实际成本审批<c:if test="${actualLog.deleteFlag eq '1'}">【已删除】</c:if></h3>
	    <c:forEach var="reviewLog" items="${actualLog.logs}" varStatus="status">
	    	<c:set var="review" value="${fns:getReviewNewByUuid(reviewLog.reviewId)}" />
	    	<!-- 审批通过/撤销  -->
	    	<c:if test="${reviewLog.operation eq '1' or reviewLog.operation eq '4' }">
		        <section>
		            <span class="point-time step_yes"></span>
		            <time>
		                <span class="text-green"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		            </time>
		            <aside>
		            	<p class="things text-green"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserNameById(reviewLog.createBy)}</span>
		            	<span>
		            		<c:if test="${reviewLog.operation eq '1'}">审批通过</c:if>
		            		<c:if test="${reviewLog.operation eq '4'}">撤销</c:if>
		            	</span>
		            	<span>备注：${reviewLog.remark}</span></p>
		            </aside>
		        </section>
	        </c:if>
	        <!-- 审批驳回 -->
	        <c:if test="${reviewLog.operation eq '2' }">
		        <section>
		            <span class="point-time stop"></span>
		            <time>
		                <span class="text-red"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		            </time>
		            <aside>
		            	<p class="things text-red"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserNameById(reviewLog.createBy)}</span><span>审批驳回</span><span>备注：${reviewLog.remark}</span></p>
		            </aside>
		        </section>
	        </c:if>
	        <!-- 审批驳回  结束标志-->
	        <c:if test="${review.status eq '0' and reviewLog.sequenceNumber eq fn:length(actualLog.logs)}">
	        	<section>
	            	<span class="point-time end"></span>
	            	<aside>
	            		<p class="things text-red"></p>
	            		<p class="things text-red">审批驳回</p>
	            	</aside>
	        	</section>
	        </c:if>
	        <!-- 审批通过 结束标志-->
	        <c:if test="${review.status eq '2' and reviewLog.sequenceNumber eq fn:length(actualLog.logs)}">
	        	<section>
	            	<span class="point-time yes"></span>
	            	<aside>
	            		<p class="things text-green"></p>
	            		<p class="things text-green">审批通过</p>
	            	</aside>
	        	</section>
	        </c:if>
	        <!-- 审批取消,审批中 结束标志-->
	        <c:if test="${review.status ne '0' and review.status ne '2' and reviewLog.sequenceNumber eq fn:length(actualLog.logs)
	        						and reviewLog.sequenceNumber ne 1}">
	        	<section>
	            	<span class="point-time next"></span>
	            	<time> </time>
	            	<aside> </aside>
	        	</section>
	        </c:if>
	    </c:forEach>    
		</article>
	</div>
</c:forEach>


<!-- 成本付款审批日志 -->
<c:forEach var="paymentLog" items="${paymentReviewLog}" varStatus="status" >
	<div class="content">
	    <article>
	    <h3>${paymentLog.costName}-成本付款审批<c:if test="${paymentLog.deleteFlag eq '1'}">【已删除】</c:if></h3>
	    <c:forEach var="reviewLog" items="${paymentLog.logs}" varStatus="status">
	    	<c:set var="review" value="${fns:getReviewNewByUuid(reviewLog.reviewId)}" />
	    	<!-- 审批通过/撤销  -->
	    	<c:if test="${reviewLog.operation eq '1' or reviewLog.operation eq '4'}">
		        <section>
		            <span class="point-time step_yes"></span>
		            <time>
		                <span class="text-green"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		            </time>
		            <aside>
		            	<p class="things text-green"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserNameById(reviewLog.createBy)}</span>
		            	<span>
		            		<c:if test="${reviewLog.operation eq '1'}">审批通过</c:if>
		            		<c:if test="${reviewLog.operation eq '4'}">撤销</c:if>
		            	</span>
		            	<span>备注：${reviewLog.remark}</span></p>
		            </aside>
		        </section>
	        </c:if>
	        <!-- 审批驳回 -->
	        <c:if test="${reviewLog.operation eq '2' }">
		        <section>
		            <span class="point-time stop"></span>
		            <time>
		                <span class="text-red"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		            </time>
		            <aside>
		            	<p class="things text-red"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserNameById(reviewLog.createBy)}</span><span>审批驳回</span><span>备注：${reviewLog.remark}</span></p>
		            </aside>
		        </section>
	        </c:if>
	        <!-- 审批驳回  结束标志-->
	        <c:if test="${review.status eq '0' and reviewLog.sequenceNumber eq fn:length(paymentLog.logs)}">
	        	<section>
	            	<span class="point-time end"></span>
	            	<aside>
	            		<p class="things text-red"></p>
	            		<p class="things text-red">审批驳回</p>
	            	</aside>
	        	</section>
	        </c:if>
	        <!-- 审批通过 结束标志-->
	        <c:if test="${review.status eq '2' and reviewLog.sequenceNumber eq fn:length(paymentLog.logs)}">
	        	<section>
	            	<span class="point-time yes"></span>
	            	<aside>
	            		<p class="things text-green"></p>
	            		<p class="things text-green">审批通过</p>
	            	</aside>
	        	</section>
	        </c:if>
	        <!-- 审批取消,审批中 结束标志-->
	        <c:if test="${review.status ne '0' and review.status ne '2' 
	        				and reviewLog.sequenceNumber eq fn:length(paymentLog.logs)
	        				and reviewLog.sequenceNumber ne 1}">
	        	<section>
	            	<span class="point-time next"></span>
	            	<time> </time>
	            	<aside> </aside>
	        	</section>
	        </c:if>
	    </c:forEach>    
		</article>
	</div>
</c:forEach>

