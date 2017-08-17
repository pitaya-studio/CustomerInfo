<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>操作日志详情页（新增）</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
	
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
	
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/dynamic.group.validator.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
</head>
<body>
                <div class="mod_nav"><!-- 操作日志 &gt; 操作日志详情 --></div>
                <div class="ydbz_tit">操作日志基本信息</div>
                <div class="orderdetails1">
                	<!--如果是批发商操作 则隐藏 这个div log-title-->
                	<p class="log-title">${logope.ope_comname}</p>

                    <table class="log-table">
                        <tbody>
                           <tr>
                                <td width="50%">模块：${logope.modular_name}</td>
                                <td width="50%">操作人：${logope.ope_name}</td>
                                   
                            </tr>
                            <tr> 
                            	<td width="50%">操作时间：${logope.create_date}</td>
                                <td width="50%">操作项：${logope.ope_type}</td>
                                
                            </tr>
                            <tr>
                            	<td colspan="2">操作内容：${logope.content}</td>
                            </tr>
                        </tbody>
                    </table>
                
                </div>
                <!--新增信息结束-->
                <div class="ydbz_sxb ydbz_button"><a href="javascript:void"  onclick ="javascript:history.go(-1);">关闭</a></div>
</body>
</html>
