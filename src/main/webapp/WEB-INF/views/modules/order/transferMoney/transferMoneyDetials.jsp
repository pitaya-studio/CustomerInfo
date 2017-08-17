<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转团转款明细</title>
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
               <%@ include file="/WEB-INF/views/modules/order/transferMoney/transferMoneyDetialsBaseInfo.jsp"%>
               <%@ include file="/WEB-INF/views/modules/order/transferMoney/transferMoneyReviewInfo.jsp"%>
              
            </div>
           <div class="ydBtn ydBtn2"><a class="ydbz_s gray" onclick="window.location.href='${ctx}/orderCommon/transferMoney/transfersMoneyHref/${oldBean.productOrderCommon.id}'">返回</a></div>
         </div>
    <!--右侧内容部分结束--> 
	</div>
</div>
</body>
</html>
