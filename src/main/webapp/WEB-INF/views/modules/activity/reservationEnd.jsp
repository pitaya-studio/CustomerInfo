<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>


<html>
<head>
<meta/>
<title>预定-单团-收款成功</title>

<link href="${ctxStatic}/forTTS/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="${ctxStatic}/forTTS/js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/forTTS/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/forTTS/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/forTTS/js/common.js"></script>
</head>
<body>
<div id="sea">
	<header>
		<div class="hedear">
      		<div class="hedear-left">
                <div class="hedear-logo"></div>
                <div class="clear"></div>
      		</div>
            <div class="hedear-right">
                <ul class="hedear-nav">
                    <li class="head-home"><a href="#">后台首页</a></li>
                    <li class="head-logout"><a href="#">退出</a></li>
                    <div class="clear"></div>
                </ul>
				<p class="header-user"><em>王涛</em>，您好，登录时间 <em>2014-5-12 12:36:52</em><span class="header-userspan">
			接口销售：郭晓勤</span><span>销售人电话：13666984589</span></p>
            </div>
      		<div class="clear"></div>
		</div>
    </header>
	<div class="main">
		<div class="main-left">
            <h2 class="mainMenu"><span class="iconMenu iconMenu-10"></span>询价<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">询价记录</a></li>
            </ul>
            <h2 class="mainMenu mainMenu-on"><span class="iconMenu iconMenu-1"></span>预定<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul" style="display:block;">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团</a></li>
                <li><a href="#" target="_self">散拼</a></li>
                <li><a href="#" target="_self">游学</a></li>
                <li><a href="#" target="_self">大客户</a></li>
                <li><a href="#" target="_self">自由行</a></li>
                <li><a href="#" target="_self">签证</a></li>
                <li><a href="#" target="_self">机票</a></li>
            </ul>
            <!--订单开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-2"></span>订单<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团订单</a></li>
                <li><a href="#" target="_self">销售订单</a></li>
                <li><a href="#" target="_self">计调订单</a></li>
                <li><a href="#" target="_self">签证订单</a></li>
                <li><a href="#" target="_self">机票订单</a></li>
            </ul>
            <!--订单结束-->
            <!--渠道商开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-3"></span>渠道商<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">渠道商查询</a></li>
                <li><a href="#" target="_self">渠道商添加</a></li>
                <li><a href="#" target="_self">定价策略查询</a></li>
                <li><a href="#" target="_self">定价策略添加</a></li>
            </ul>
            <!--渠道商结束-->
            <!--财务(渠)开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-4"></span>财务(渠)<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self"><!--渠道商-->发票申请</a></li>
                <li><a href="#" target="_self"><!--渠道商-->发票审核记录查询</a></li>
            </ul>
            <!--财务(渠)结束-->
            <!--财务(供)开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-4"></span>财务(供)<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self"><!--渠道商-->发票申请</a></li>
                <li><a href="#" target="_self"><!--渠道商-->结算管理</a></li>
                <li><a href="#" target="_self"><!--渠道商-->成本管理</a></li>
            </ul>
            <!--财务(供)结束-->
            <!--产品开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-5"></span>产品<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团</a></li>
                <li><a href="#" target="_self">散拼</a></li>
                <li><a href="#" target="_self">游学</a></li>
                <li><a href="#" target="_self">大客户</a></li>
                <li><a href="#" target="_self">自由行</a></li>
                <li><a href="#" target="_self">签证</a></li>
                <li><a href="#" target="_self">机票</a></li>
            </ul>
            <!--产品结束-->
            <!--运控开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-6"></span>运控<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">库存切位</a></li>
                <li><a href="#" target="_self">产品成本录入</a></li>
            </ul>
            <!--运控结束-->
            <!--系统设置开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-7"></span>系统设置<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">个人信息</a></li>
                <li><a href="#" target="_self">修改密码</a></li>
                <li><a href="#" target="_self">账号管理</a></li>
                <li><a href="#" target="_self">账号添加</a></li>
            </ul>
            <!--系统设置结束-->
            <!--基础信息开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-8"></span>基础信息<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">旅游类型</a></li>
                <li><a href="#" target="_self">产品系列</a></li>
                <li><a href="#" target="_self">产品类型</a></li>
                <li><a href="#" target="_self">交通方式</a></li>
                <li><a href="#" target="_self">出发城市</a></li>
                <li><a href="#" target="_self">目的地区域</a></li>
                <li><a href="#" target="_self">航空公司</a></li>
            </ul>
            <!--基础信息结束-->
            <!--模块说明开始-->
            <h2 class="mainMenu mainMenu-last"><span class="iconMenu iconMenu-9"></span>模块说明<!--<i class="iconMenu-arrow"></i>--></h2>
            <!--模块说明结束-->
		</div>
        <div class="main-right">
            <content tag="three_level_menu">
                <li class="active"><a href="#">个人信息</a></li>
                <li><a href="#">修改密码</a></li>
                <li><a href="#">账号管理</a></li>
                <li><a href="#">账号添加</a></li>
                <li><a href="#">资讯公告列表</a></li>
                <li><a href="#">资讯公告添加</a></li>
            </content>
            <div class="bgMainRight">
            	<!--右侧内容部分开始-->
                <div class="ydbzbox fs">
                    <div class="ydbz yd-step5">&nbsp;</div>
                    <div class="payforDiv">
                        <div class="payforok">
                            <div class="payforok-inner">
                                <h3 class="payforok-title">恭喜！您已成功下单</h3>
                                <table>
                                    <tbody><tr><th>订单号</th><th>付款方式</th><th>总金额</th></tr>
                                    <tr><td>61567</td> <td>现金支金支付</td>  <td><span class="gray14">￥</span><span class="tdred f20">6,800</span></td></tr>
                                </tbody></table>
                                <p class="payforokbtn"><a href="#">下载确认单</a><a href="#">打印确认单</a><a class="payforokbtn3" href="#">回到首页</a></p>
                            </div>
                        </div>
                        <p class="payforoktip"><span>提示：</span>尊敬的客户，恭喜您付款成功，请登录“<a href="#">我的订单</a>”补充您的游客相关信息，感谢您的配合！</p>
                        <div style="overflow:hidden">
                           <div class="kongr"></div>
                        </div>
                     </div>
                 </div>
                <!--右侧内容部分结束-->
            </div>
        </div>
	</div>
    <!--footer-->
    <div class="bs-footer">
        <p>客服电话：010-85718666  <br/>Copyright © 2012-${fns:getConfig('copyrightYear')} 接待社交易管理后台</p>
        <div class="footer-by">Powered By Trekiz Technology</div>
    </div>
    <!--footer***end-->
</div>
</body>
</html>


