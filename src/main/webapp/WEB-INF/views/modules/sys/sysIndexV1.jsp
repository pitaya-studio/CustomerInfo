<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>接待社后台</title>

    <link href="${ctxStatic}/css/jbox.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/css/jquery.validate.min.css" type="text/css" rel="stylesheet"/>

    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

</head>
<body>
<c:choose>
    <c:when test="${fn:contains(pageContext.request.requestURL,'kaisa.hhqilu.com')}">
        <div class="denglu-hy">
            <div class="denglu-hy-top"></div>
            <div class="denglu-hy-center">
                <p>
                    <span></span>
                    <i>
                        <c:if test="${not fn:contains(pageContext.request.requestURL,'kaisa.hhqilu.com')}">欢迎您使用</c:if>
                    </i>
                    <c:if test="${fn:contains(pageContext.request.requestURL,'kaisa.hhqilu.com')}">
                        <link href="${ctxStatic}/css/kshj-style.css" type="text/css" rel="stylesheet"/>
                    </c:if>
                    <i>
                        <c:if test="${not fn:contains(pageContext.request.requestURL,'kaisa.hhqilu.com')}">订单操作系统平台!</c:if>
                    </i>
                </p>
            </div>
            <div class="denglu-hy-foot"></div>
            <div class="denglu-hy-bottom"></div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="wel_bg">
            <div class="wel_msg_bg_bg">
                <div class="wel_msg">
                    <div class="wel_msg_logo"></div>
                </div>
            </div>
        </div>
<%--changying.huo--%>
        <%--数据统计权限控制--%>
		<shiro:hasPermission name="home:statistic">
            <%@ include file="/WEB-INF/views/modules/statisticAnalysis/statisticHome/statisticHome.jsp" %>
        </shiro:hasPermission>
    </c:otherwise>
</c:choose>
</body>
</html>
