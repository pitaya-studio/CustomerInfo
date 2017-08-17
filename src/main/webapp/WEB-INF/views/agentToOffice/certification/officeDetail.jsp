<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>批发商认证-详情</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic }/css/imgView/normalize.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic }/css/imgView/default.css">
    <%-- <link rel="stylesheet" href="${ctxStatic }/css/imgView/bootstrap.min.css"> --%>
    <link rel="stylesheet" href="${ctxStatic }/css/imgView/viewer.css">
    <link rel="stylesheet" href="${ctxStatic }/css/imgView/main.css">
    <link rel="stylesheet" href="${ctxStatic }/css/imgView/style.css">
    <link rel="stylesheet" href="${ctxStatic }/css/imgView/animate.min.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->


    <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
    <link href="${ctxStatic }/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <!--[if lte IE 6]>
    <%-- <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/> --%>
    <%-- <script type="text/javascript" src="js/bootstrap-ie.min.js"></script> --%>
    <![endif]-->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic }/css/t1t2.css"/>
    <link rel="stylesheet" href="${ctxStatic }/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <!-- Scripts -->
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<!--<script type="text/javascript" src="assets/js/jquery-1.8.3.min.js"></script>-->
	<script src="${ctxStatic }/js/picView/jPages.js"></script>
	<script src="${ctxStatic }/js/picView/bootstrap.min.js"></script>
	<script src="${ctxStatic }/js/picView/viewer.js"></script>
	<script src="${ctxStatic }/js/picView/main.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
	<script>
	function goCertification(){
		location.href = "${ctx}/wholesalers/certification/getOfficeList";
	}
	</script>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/homepage/T1Head.jsp"%>
<div class="sea">
    <!--main start-->
    <div class="main">
        <div class="mainHomePage">
            <div class="contentHomePage">
                <div class="bread_new"><i></i>您的位置：<a href="javascript:goHomePage('${ctx}')">首页</a> &gt; <a href="javascript:goCertification()">批发商认证</a> &gt; 批发商认证详情</div>
                <style type="text/css">

                </style>
                <div class="content_auth_back">
                    <div class="authentication_bc1"></div>
                    <div class="authentication_bc2">
                        <div class="name_travel">
                            <label>企业名称：</label><span>${office.name }</span><br>
                            <label>许可证号：</label><span style="word-break: break-all;max-width: 680px;word-break: break-all;">${office.licenseNumber }</span><br>
                            <label>所在地：</label><span style="word-break: break-all;max-width: 680px;word-break: break-all;">${office.address }</span><br>
                            <label>投诉电话：</label><span>${office.phone }</span><br>
                            <label>网址：</label><span  style="word-break: break-all;max-width: 680px;word-break: break-all;">${office.webSite }</span>
                            <label>业务简介：</label><span style="word-break: break-all;max-width: 680px;word-break: break-all;">${office.summary }</span><br>
                        </div>
                        <p>企业资质</p>
                        <div class="htmleaf-container">
                            <div class="docs-galley">
                                <ul class="docs-pictures clearfix">
                                	<c:forEach items="${fns:getDocInfosByIds(office.businessLicense)}" var="doc">
                                		 <li><div><img data-original="${ctx }/person/info/getLogo?id=${doc.id}" title="${doc.docName}" src="${ctx }/person/info/getLogo?id=${doc.id}" alt="${doc.docName}"></div></li>
                                	</c:forEach>
                                	<c:forEach items="${fns:getDocInfosByIds(office.businessCertificate)}" var="doc">
                                    	<li><div><img data-original="${ctx }/person/info/getLogo?id=${doc.id}" title="${doc.docName}" src="${ctx }/person/info/getLogo?id=${doc.id}" alt="${doc.docName}"></div></li>
                                    </c:forEach>
                                    <c:forEach items="${fns:getDocInfosByIds(office.cooperationProtocol)}" var="doc">
                                    	<li><div><img data-original="${ctx }/person/info/getLogo?id=${doc.id}" title="${doc.docName}" src="${ctx }/person/info/getLogo?id=${doc.id}" alt="${doc.docName}"></div></li>
                                	</c:forEach>
                                </ul>
                            </div>
                        </div>
                        <p>销售名片</p>
                        <div class="htmleaf-container">
                                <div class="docs-galley">
                                    <ul class="docs-pictures clearfix">
                                    	<c:forEach items="${docIds}" var="doc">
                                    		<c:forEach items="${fns:getDocInfosByIds(doc.cardId)}" var="d">
                                    			<li><div><img data-original="${ctx }/person/info/getLogo?id=${d.id}" title="${d.docName }" src="${ctx }/person/info/getLogo?id=${d.id}" alt="${d.docName }"></div></li>
                                    		</c:forEach>
                                   		</c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="authentication_bc3"></div>
                </div>
            </div>
        </div>
        <!--main end-->
    </div>

    <!--footer start-->
<%@ include file="/WEB-INF/views/modules/homepage/t1footer.jsp"%><!--modify by wlj at 2016.11.24 for huiteng-start-->
    <!--footer end-->
</div>
</body>
</html>
