<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转款-详情</title>
<meta name="decorator" content="wholesaler"/>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
</script>
</head>
<body>
<div id="sea">
	<!--右侧内容部分开始-->
   	<div class="mod_nav">订单 > 单团 > 转款明细</div>
   	<div class="ydbzbox fs">
       	<div class="orderdetails">
        	<%@ include file="/WEB-INF/views/review/transfermoney/singlegroup/transferMoneyDetialsBaseInfo.jsp"%>
       	</div>
       	<div class="ydbz_tit"><span class="fl">审批动态</span></div>
        <%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
        <div class="ydBtn ydBtn2">
        	<a class="ydbz_s gray" onclick="window.close();">关闭</a>
       	</div>
   	</div>
    <!--右侧内容部分结束--> 
</div>
</body>
</html>
