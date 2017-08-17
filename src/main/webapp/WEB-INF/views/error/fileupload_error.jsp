<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>file upload error - 上传文件失败</title>
    <meta name="decorator" content="wholesaler"/>
</head>
<body>
    <div class="container-fluid">
        <div class="page-header"><h1>上传文件失败</h1></div>
        <div><a href="javascript:" onclick="history.go(-1);" class="btn">文件大小不能超过30M,请返回上一页重新上传</a></div>
        <script>try{top.$.jBox.closeTip();}catch(e){}</script>
    </div>
</body>
</html>