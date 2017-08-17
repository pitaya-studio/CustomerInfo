<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 签证公告展示 -->
<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
<div class="xt-activitylist" style="display:none;">
    <c:if test="${fns:getUser().userType ==3}">
        <select name="agentId" id="agentIdSel" class="typeSelected" onchange="getSalerByAgentId(this);">
        	<c:if test="${fns:getUser().company.id ne 68 }">
	            <option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>><c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when><c:otherwise>非签约渠道</c:otherwise></c:choose></option>
        	</c:if>
            <c:forEach var="agentinfo" items="${agentinfoList }">
                <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if> >${agentinfo.agentName }</option>
            </c:forEach>
        </select>
    </c:if>
</div>
<input id="companyUuid" type="hidden" value="${companyUuid}" />
<input id="showType" name="showType" type="hidden" value="${showType}" />
<input id="orderTypeStr" name="orderTypeStr" type="hidden" value="${orderTypeStr}" />
<shiro:hasPermission name="${orderTypeStr}:book:addAgent">
	<input id="isAddAgent" name="isAddAgent" type="hidden" value="1" />
</shiro:hasPermission>
<input id="activityKind" name="activityKind" type="hidden" value="${activityKind}" />
<div class="activitylist_bodyer_right_team_co_bgcolor" >
	<!-- 搜索查询 -->
	<%@ include file="/WEB-INF/views/modules/order/forOrder/form.jsp"%>
	<!-- 产品系列 -->
	<%@ include file="/WEB-INF/views/modules/order/forOrder/productType.jsp"%>
	<!-- 部门分区 -->
	<%@ include file="/WEB-INF/views/common/departmentDiv.jsp"%>
	<!-- 产品列表、团期列表 -->
	<%@ include file="/WEB-INF/views/modules/order/forOrder/swith.jsp"%>
	<!-- 排序 -->
	<%@ include file="/WEB-INF/views/modules/order/forOrder/orderByDiv.jsp"%>
	
	<c:choose>
		<c:when test="${productOrGroup == 'product'}">
			<!-- 产品列表 -->
			<%@ include file="/WEB-INF/views/modules/order/forOrder/productForm.jsp"%>
		</c:when>
		<c:otherwise> 
			<!-- 团期列表 -->
			<%@ include file="/WEB-INF/views/modules/order/forOrder/groupForm.jsp"%>
		</c:otherwise>
	</c:choose>
</div>
<div class="pagination clearFix">${page}</div>
<div class="page"></div>