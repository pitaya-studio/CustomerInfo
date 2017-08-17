<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
            <ul class="nav nav-tabs">
				<%-- <li><a href="">产品信息</a></li>
				<li><a href="">交通信息</a></li> --%>
				<li class="active"><a href="${ctx}/sysGeoCity/cityList">城市</a></li>
				<li ><a href="${ctx}/sysGeoCity/destinationList">目的地</a></li>
				<%-- <li><a href="">币种信息</a></li> --%>
			</ul>
</content>