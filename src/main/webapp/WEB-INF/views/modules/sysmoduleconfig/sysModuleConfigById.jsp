<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>后台维护-后台添加</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
</head>
<body>

            <div class="bgMainRight">
            	<!--右侧内容部分开始-->
                <div class="sysdiv sysdiv_coupon">
					<p>
						<label>批发商：</label>
						<span>${office.name}</span>
					</p>
					<p>
						<label>模块：</label>
						<span>${sysModule.fmodulename }</span>
					</p>
					<p>
						<label>子模块：</label>
						<span>${sysModule.modulename }</span>
					</p>
					<p>
						<label>页面名称：</label>
						<span>${sysModule.pageName }</span>
					</p>
					<p>
						<label>页面路径：</label>
						<span>${sysModule.path }</span>
					</p>
					<p>
						<label>预览路径：</label>
						<span>${sysModule.prePath }</span>
					</p>
					<p>
						<label>&nbsp;</label>
						<span>
							<input type="button" onclick ="javascript:history.go(-1);" id="btn_search" value="关闭"  class="btn btn-primary ydbz_x">
						</span>
					</p>
				</div>
                <!--右侧内容部分结束-->
			</div>
    
</body>
</html>
