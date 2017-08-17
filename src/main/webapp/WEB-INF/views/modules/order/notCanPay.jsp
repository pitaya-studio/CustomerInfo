<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单管理</title>
<meta name="decorator" content="wholesaler" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />

    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script>
    
    
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript">


</script>

<style type="text/css">
table{
    border-collapse:collapse;
    width: 100%;
}

#paySuccess_s{ border-radius:10px;background-color:#F5F5F5; width:500px; height:300px;
 text-align:center;}
.main-right { border-top: 3px #D0D7DD solid; background: #EDF0F2; margin-left: 100px;padding:0px 30px 30px 30px;}
</style>

</head>
<body>
<div class="clearFix">
<div id="paySuccess_s" style="margin: 20px 0">

<div style=" margin-top:; width:400px; height:100px;"></div>
<div style=" margin:0px auto; width:400px; height:100px;">
<table style="width:400px; height:100px; border:none;">
<tr>
  <td>
  <span style="color:#090; font-size:20px; font-weight:bold; text-align:center;">${errorMsg }</span>
  </td>
</tr>
<tr>
  <td>
  </td>
</tr>
</table>     
</div>
</div>
</body>
</html>