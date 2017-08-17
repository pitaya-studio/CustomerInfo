<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
<ul class="nav-tabs hasNav">
<li <c:if test="${showType==1}">class="active current"</c:if>><a href="${ctx}/activityIsland/islandProductList?status=0&showType=1">团期列表</a></li>
<li <c:if test="${showType==2}">class="active current"</c:if>><a href="${ctx}/activityIsland/islandProductList?status=0&showType=2">产品列表</a></li>
</ul>
	<%--
    <li  class="ernav active current"><a href="javascript:void(0);">海岛游<i></i></a>
        <dl>
           <dt ><a <c:if test="${activityStatus==5}">class="active"</c:if> href="${ctx}/activityIsland/islandProductList?activityStatus=5&showType=${showType}">全部</a><span>丨</span></dt>
           <dt ><a <c:if test="${activityStatus==1}">class="active"</c:if> href="${ctx}/activityIsland/islandProductList?activityStatus=1&showType=${showType}">已上架</a><span>丨</span></dt>
           <dt ><a <c:if test="${activityStatus==2}">class="active"</c:if> href="${ctx}/activityIsland/islandProductList?activityStatus=2&showType=${showType}">已下架</a><span>丨</span></dt>
           <dt ><a <c:if test="${activityStatus==3}">class="active"</c:if> href="${ctx}/activityIsland/islandProductList?activityStatus=3&showType=${showType}">草稿箱</a><span>丨</span></dt>
           <dt ><a <c:if test="${activityStatus==4}">class="active"</c:if> href="${ctx}/activityIsland/islandProductList?activityStatus=4&showType=${showType}">已删除</a></dt>
        </dl>
    </li>
    --%>	
</content>


