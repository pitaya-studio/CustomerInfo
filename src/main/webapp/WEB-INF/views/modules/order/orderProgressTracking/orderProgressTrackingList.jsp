<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>交易实时跟踪系统</title>
    <script type="text/javascript" src="${ctxStatic}/modules/order/orderProgressTracking/ordertrack.js?v=1209"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
	
	<!-- T2页面的样式 -->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/orderTrack.css"/>
	<%--<c:if test="${fns:getUser().isQuauqAgentLoginUser eq 0}">--%>
		<%--<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/orderTrack-T2.css">--%>
	<%--</c:if>--%>
</head>

<body>

	<input id="pageNoForProgress" type="hidden" value="1"/>
	<input id="selectType" type="hidden" value="${selectType}"/>
	<input id="progressCtx" type="hidden" value="${ctx}"/>
	<input id="isQuauqUser" type="hidden" value="${fns:getUser().isQuauqAgentLoginUser}"/>
	<input id="isQuauqAdmin" type="hidden" value="${fns:getUser().id eq 2060}"/>
	
<div class="orderTracking_back" style="display: none">
	<div class="orderTracking_all" id="orderTrackingAll">
		<div class="orderT_top_title">
			<span class="orderText">订单跟踪</span>
			<!--<span class="orderBorder"></span>-->
			<!--<span class="orderText">数据仪表盘</span>-->
		</div>
		<div class="orderT_close"></div>
		<div class="ot_refresh" onclick="reloadAgain('all')"><i class="fa fa-repeat"></i>刷新数据</div>
        <div class="orderTracking_top">
            <div class="dataStatistics" onclick="reloadAgain('add')" lang="add">
                <div class="data_title">今日新增</div>
                <div class="data_number"></div>
            </div>
            <div class="dataStatistics selectData" onclick="reloadAgain('all')" lang="all">
                <div class="data_title">所有订单</div>
                <div class="data_number"></div>
            </div>
            <div class="dataStatistics" onclick="reloadAgain('outTime')" lang="outTime">
                <div class="data_title">超时订单</div>
                <div class="data_number"></div>
            </div>
        </div>
        <div class="orderT_content"><!--内部嵌套一个表格，div分为上下两部分，上面是title下面是内容-->
            <div class="orderT_title"><!--内部嵌套一个表格，div分为上下两部分，上面是title下面是内容-->
                <span style="width: 355px;padding-left: 75px;">门店名称</span>
                <span style="width: 85px;padding-left: 10px;">门店销售</span>
                <span style="width: 235px;padding-left: 30px;">销售</span>
                <span style="width: 245px;padding-left: 20px;">计调</span>
                <span style="width: 70px;padding-left: 15px;">财务</span>
                <span style="width: 78px;padding-left: 10px;">详情</span>
            </div>

			<div class="orderT_info"><!--内部嵌套一个表格，div分为上下两部分，上面是title下面是内容-->
				<ul class="orderT_left">
					<li class="selectedMoveLi">
					</li>
				</ul>
				
				 <!--没有数据时显示div，下面的ul不用展示-->
				<div class="no_data" style="display: none;">
					<div><span></span></div>
				</div>

                <div class="orderT_right">
                    <ul class="orderT_right_ul">
                        <!--该li为了调整右侧样式！不可以删去！-->
                        <li style="display: block;height: 0px;"></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>