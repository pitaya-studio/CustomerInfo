<%@ page pageEncoding="UTF-8" contentType="text/html; UTF-8" language="java" import="com.trekiz.admin.common.config.Context" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>错误提示</title>
    <meta name="decorator" content="wholesaler"/>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible"content="IE=8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <link rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css" /><!--字体图标-->
    <link rel="stylesheet" href="${ctxStatic}/css/error_page.css" />
</head>
<body>
    <div class="wrong-page">
        <img src="${ctxStatic}/images/wrong-page.jpg" height="340" width="570">
        <p>Sorry！系统发生错误，${error_message_key}，您可以尝试 <a href="javascript:void(0)" onclick="history.go(-1)">返回</a></p>
    </div>
</body>
</html>
