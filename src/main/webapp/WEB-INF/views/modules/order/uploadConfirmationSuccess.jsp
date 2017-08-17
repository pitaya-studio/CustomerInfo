<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传完成</title>
	<meta name="decorator" content="wholesaler" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script type="text/javascript">
		$(function(){
			//刷新父页面 
			window.opener.location=window.opener.location;
		});
		
		function orderDetail(orderId){
		    window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
		}
	</script>
</head>
<body>
	<div class="ydbzbox fs">
		<div class="payforDiv">
	    	<div class="payforok">
	        	<div class="payforok-inner">
	            	<h3 class="payforok-title">&nbsp;&nbsp;恭喜！确认单上传成功</h3>
					<p class="payforokbtn">
						<a href="javascript:window.close();">关闭</a>
					</p>
	        	</div>
	    	</div>
	    	<div style="overflow:hidden">
	        	<div class="kongr"></div>
			</div>
		</div>
	</div>
</body>
</html>