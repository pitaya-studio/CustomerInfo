<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>404 - 页面不存在</title>
    <meta name="decorator" content="wholesaler"/>
</head>
<body>
	<div class="container-fluid">
		<div class="site_error_4">
	        <div class="site_error_text">
	            <p>页面不存在.</p> 
	            <div><a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div>
				<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	        </div>
	    </div> 
    </div>
</body>
</html>