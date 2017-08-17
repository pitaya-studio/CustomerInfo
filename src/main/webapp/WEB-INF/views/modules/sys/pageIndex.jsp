<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/publicInclude.jsp"%>
<html>
<!-- 
 DEVELOP_DEMO
 -->
<style type="text/css">
body {
	font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
		sans-serif;
	color: #4f6b72;
	background: #E6EAE9;
}

.table {
	width: 700px;
	padding: 0;
	margin: 0;
}

.table tr {
	width: 100%;
}

.table td {
	width: 100%;
}

.table th {
	font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
	color: #4f6b72;
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
	letter-spacing: 2px;
	text-transform: uppercase;
	text-align: left;
	padding: 6px 6px 6px 12px;
	background: #CAE8EA no-repeat;
}

.table td {
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	background: #fff;
	font-size: 11px;
	padding: 6px 6px 6px 12px;
	color: #4f6b72;
}

/*---------for IE 5.x bug*/
html>body td {
	font-size: 11px;
}

body,td,th {
	font-family: 宋体, Arial;
	font-size: 12px;
}

.player {
        width: 500px;
        height: 300px;
        border: 2px groove gray;
        background: rgb(200, 200, 200);
        text-align: center;
        line-height: 300px;
    }
    .ui-tooltip {
        border: 1px solid white;
        background: rgba(20, 20, 20, 1);
        color: white;
    }
    .set {
        display: inline-block;
    }
    .notification {
        display: inline-block;
        padding: 0px 5px 0px 5px;
    }


</style>

<head>
<title>jQuery.pager.js Test</title>
<link rel="stylesheet" href="${ctxStatic}/jqueryUI/demos.css" />

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.widget.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.button.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.mouse.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.draggable.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.resizable.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.dialog.js"></script>

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.menu.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.tooltip.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.effect.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.effect-blind.js"></script>
    
    
<script type="text/javascript" language="javascript">

//日期控件  设置为中文
jQuery(function($){
    $.datepicker.regional['zh-TW'] = {
        closeText: '关闭',
        prevText: '&#x3C;上月',
        nextText: '下月&#x3E;',
        currentText: '今天',
        monthNames: ['一月','二月','三月','四月','五月','六月',
        '七月','八月','九月','十月','十一月','十二月'],
        monthNamesShort: ['一月','二月','三月','四月','五月','六月',
        '七月','八月','九月','十月','十一月','十二月'],
        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
        dayNamesMin: ['日','一','二','三','四','五','六'],
        weekHeader: '周',
        dateFormat: 'yy/mm/dd',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: '年'};
    $.datepicker.setDefaults($.datepicker.regional['zh-TW']);
});


	$(function() {
		$("#pager").pager({
			pagenumber : 1,//当前页
			pageRecordNum : 33,//每页记录数
			url : "../a/getData",
			userData : {//支持对userData参数处理   传入后台
				username : "userName",
				password : "password"
			},
			buttonClickCallback : pressData
		});
	});
    //解析数据   第一个参数为数据  第二个为是否显示表头
	function pressData(msg) {
		$("#result").html($(document).JsonToHtmlHanlder(msg,true));
	}
	//显示导航
	$(function() {
        $( "#menu" ).menu();
    });
	
	
	//日期控件
	$(function() {
        $( "#datepicker" ).datepicker();
    });
	
	$(function(){
		$("#button").click(function(){
			var value = $("#datepicker").val();
			TrekizUtils.showTips.info(value);
			
		});
	});
	
</script>
</head>
<body>
	<div style="width: 200px;">
		<ul id="menu">
			<li class="ui-state-disabled"><a href="#">Aberdeen</a>
			</li>
			<li><a href="#">Ada</a>
			</li>
			<li><a href="#">Adamsville</a>
			</li>
			<li><a href="#">Addyston</a>
			</li>
			<li><a href="#">Delphi</a>
				<ul>
					<li class="ui-state-disabled"><a href="#">Ada</a>
					</li>
					<li><a href="#">Saarland</a>
					</li>
					<li><a href="#">Salzburg</a>
					</li>
				</ul></li>
			<li><a href="#">Saarland</a>
			</li>
			<li><a href="#">Salzburg</a>
				<ul>
					<li><a href="#">Delphi</a>
						<ul>
							<li><a href="#">Ada</a>
							</li>
							<li><a href="#">Saarland</a>
							</li>
							<li><a href="#">Salzburg</a>
							</li>
						</ul></li>
					<li><a href="#">Delphi</a>
						<ul>
							<li><a href="#">Ada</a>
							</li>
							<li><a href="#">Saarland</a>
							</li>
							<li><a href="#">Salzburg</a>
							</li>
						</ul></li>
					<li><a href="#">Perch</a>
					</li>
				</ul></li>
			<li class="ui-state-disabled"><a href="#">Amesville</a>
			</li>
		</ul>
	</div>
	
	

<%--
    中文显示日期控件
 --%>
<div  style="width: 100px;">
	<p>Date: <input type="text" id="datepicker"/>&nbsp;
    </p>
</div>

<div class="demo-description">
<p>
    中文日历控件
    <button id="button" name="">得到日期值</button>
</p>
</div>
	
	<h1 id="result">Click the pager below.</h1>
    <div id="pager"></div>
	
</body>
</html>
