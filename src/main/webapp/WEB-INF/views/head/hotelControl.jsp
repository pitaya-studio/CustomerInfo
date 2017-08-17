<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
                <li <c:if test="${activityStatus=='3'}">class="active"</c:if>>
                     <a href="${ctx}/hotelControl/hotelControlList?showType=${showType}&activityStatus=3" target="_self">全部</a></li>
			    <li <c:if test="${activityStatus=='0'}">class="active"</c:if>>
			         <a href="${ctx}/hotelControl/hotelControlList?showType=${showType}&activityStatus=0" target="_self">已生效</a></li>
			    <li <c:if test="${activityStatus=='4'}">class="active"</c:if>>
			         <a href="${ctx}/hotelControl/hotelControlList?showType=${showType}&activityStatus=4" target="_self">已停用</a></li>
                <li <c:if test="${activityStatus=='1'}">class="active"</c:if>>
                	<a href="${ctx}/hotelControl/hotelControlList?showType=${showType}&activityStatus=1" target="_self">草稿</a></li>
                <li <c:if test="${activityStatus=='2'}">class="active"</c:if>>
                    <a href="${ctx}/hotelControl/hotelControlList?showType=${showType}&activityStatus=2" target="_self">已删除</a></li>
</content>



