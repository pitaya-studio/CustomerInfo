<!-- 特别说明：引入此页面时请注意 review reviewLogs 已使用，请务必不要重复！后台务必传审核唯一标识rid!-->
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 审核动态 -->
<c:set var="review" value="${fns:getReviewNewByUuid(rid)}" />
<c:set var="reviewLogs" value="${fns:obtainReviewLogs(rid)}" />

<div class="content">
	<article>
	<c:if test="${not empty reviewLogs}">
		<c:forEach var="reviewLog" items="${reviewLogs}" varStatus="status" >
			<c:choose>
				<%-- 驳回状态--%>
				<c:when test="${reviewLog.operation eq 2}">
					<section>
						<%--<span class="point-time end"></span>--%>
						<span class="point-time stop"></span>
						<time>
							<span class="text-red"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
						</time>
						<aside>
							<p class="things text-red"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserById(reviewLog.createBy).name}</span><span> ${reviewLog.operationDescription}</span><span>备注：${reviewLog.remark}</span></p>
						</aside>
					</section>
					<section>
						<%--<span class="point-time end"></span>--%>
						<span class="point-time end"></span>
						<aside>
							<p class="things text-red"><span>审批驳回</span></p>
						</aside>
					</section>
				</c:when>
				<%--其他状态--%>
				<c:otherwise>
						<c:choose>
							<c:when test="${(review.status eq 2) and status.last}">
								<section>
									<span class="point-time step_yes"></span>
									<time>
										<span class="text-green"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
									</time>
									<aside>
										<p class="things text-green"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserById(reviewLog.createBy).name}</span><span> ${reviewLog.operationDescription}</span><span>备注：${reviewLog.remark}</span></p>
									</aside>
								</section>
								<section>
									<span class="point-time yes"></span>
									<aside>
										<p class="things text-green"><span>审批通过</span></p>
									</aside>
								</section>
							</c:when>
							<c:when test="${(review.status eq 1) and status.last}">
								<section>
									<span class="point-time step_yes"></span>
									<time>
										<span class="text-green"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
									</time>
									<aside>
										<p class="things text-green"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserById(reviewLog.createBy).name}</span><span> ${reviewLog.operationDescription}</span><span>备注：${reviewLog.remark}</span></p>
									</aside>
								</section>
								<section>
									<span class="point-time next"></span>
									<time></time>
									<aside></aside>
								</section>
							</c:when>
							<c:otherwise>
								<section>
									<span class="point-time step_yes"></span>
									<time>
										<span class="text-green"><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
									</time>
									<aside>
										<p class="things text-green"><span>${fns:getJobName(reviewLog.tagkey)}</span><span>${fns:getUserById(reviewLog.createBy).name}</span><span> ${reviewLog.operationDescription}</span><span>备注：${reviewLog.remark}</span></p>
									</aside>
								</section>
							</c:otherwise>
						</c:choose>

				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:if>
	</article>
</div>