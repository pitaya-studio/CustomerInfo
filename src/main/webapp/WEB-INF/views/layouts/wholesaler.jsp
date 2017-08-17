<%@page import="com.trekiz.admin.modules.sys.utils.UserUtils"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.trekiz.admin.common.config.Global"%>
<%@ page import="com.trekiz.admin.modules.sys.entity.Menu" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><sitemesh:title default="批发商交易平台" /></title>

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
 
<%@include file="/WEB-INF/views/include/head-wholesaler.jsp" %>


<sitemesh:head />
<c:choose>
	<c:when test="${fn:contains(pageContext.request.requestURL,'kaisa.hhqilu.com')}">
		<link href="${ctxStatic}/css/kshj-style.css" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.id eq 97}">
		<link href="${ctxStatic}/css/jh-style-remote-vacation.css" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fn:contains(pageContext.request.requestURL,'diamondforce')}">
		<link href="${ctxStatic}/css/diamondforce.css" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fn:contains(pageContext.request.requestURL,'hqqz.quauqsystem.com.cn')}">
		<link href="${ctxStatic}/css/zsgl.css" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.uuid eq 'dfafad3ebab448bea81ca13b2eb0673e'}">
		<link href="${ctxStatic}/css/tianma.css" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">
		<link href="${ctxStatic}/css/huanqiuxing-travel.css?v=100" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.uuid eq 'eaff3883692644a89427da27ee423b7d'}">
		<link href="${ctxStatic}/css/jinling-travel.css?v=100" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
		<link href="${ctxStatic}/css/lameitu-travel.css?v=100" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.uuid eq 'cb4390e3fed841798f1bb755257334be'}">
		<link href="${ctxStatic}/css/nanyadaziran-travel.css?v=100" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fns:getUser().company.uuid eq '33ab2de5fdc842caba057296b28f5bae'}">
		<link href="${ctxStatic}/css/huiyou-travel.css?v=100" type="text/css" rel="stylesheet" />
	</c:when>
	<c:when test="${fn:contains(pageContext.request.requestURL,'uc-tours')}">
		<link href="${ctxStatic}/css/youchuang-travel.css" type="text/css" rel="stylesheet" />
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>
<%--<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>--%>
<%--<script type="text/javascript" src="${ctxStatic}/js/jquery.placeholder.min.js"></script>--%>
<%--<script>--%>
	<%--$(function() {--%>
		<%--//input内添加placeholder属性hehhe--%>
		<%--$('input').placeholder();--%>
	<%--})--%>
<%--</script>--%>


<!-- 通讯录相关 -->
<link href="${ctxStatic}/css/mail.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css">
<script src="${ctxStatic}/js/mail.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/jsScroll.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/placeholder.js" type="text/javascript"></script>

<%--<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>--%>

<script type="text/javascript" src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"></script>


<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" ></script>


<link rel="stylesheet" href="${ctxStatic}/css/jbox.css">
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>


<%
    String mId = request.getParameter("_m");
    String mcId = request.getParameter("_mc");
    //单团类预定后跳转到指定订单处
    if (mId == null && mcId == null) {
    	mId = request.getAttribute("_m") != null ? request.getAttribute("_m").toString() : null;
        mcId = request.getAttribute("_mc") != null ? request.getAttribute("_mc").toString() : null;
    }
    if(mId != null){
        session.setAttribute("_m" , mId);
        if(mcId != null){
            session.setAttribute("_mc" , mcId);
        }else{
            session.removeAttribute("_mc");
        }

    }
%>
	<%--<script src="${ctxStatic}/js/pullDown.js" type="text/javascript"></script>--%>
<%--<link href="/static/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">--%>



</head>
<%--ie8滚动条样式--%>
<!--[IF IE 8]>
<style>
	.scroll_content{
		width: 432px;
	}
</style>
<![endif]-->
<body>
<shiro:hasPermission name="travel:service:branch">
	<%@ include file="/WEB-INF/views/layouts/branchOfTravelService.jsp"%>
</shiro:hasPermission>
<%@ include file="/WEB-INF/views/modules/order/orderProgressTracking/orderProgressTrackingList.jsp"%>

    <div class="panel_content">
    <shiro:hasPermission name="travel:service:branchNew">
        <div id="mailId"  class="mail-btn"  onclick="showPanel(this,'${ctx}/agent/manager');">
           	 门市名录
        </div>
    </shiro:hasPermission>
    <shiro:hasPermission name="loose:book:groupControlBoard">
        <div class="mail-btn panel_control"  onclick="showControlPanel(this);">
            <span class="add_top_border">团控板</span>
        </div>
    </shiro:hasPermission>
    <!-- 团控版收客权限 -->
    <shiro:hasPermission name="groupControlBoard:getOperator">
    	<input id="shiroUse" type="hidden" value="1"/>
    </shiro:hasPermission>
    
    </div>
	<!--通讯录浮窗开始-->
	<shiro:hasPermission name="travel:service:branchNew">
    <div  class="main-mail"  id="mail"></div>
    </shiro:hasPermission>
    <shiro:hasPermission name="loose:book:groupControlBoard">
	<div  id="team">
		<div  class="team_control" style="display: none">
			<div class="top-search">
				<div class="top-container">
					<div class="search-input"><i class="fa fa-search" aria-hidden="true"></i><input id="groupSearchInput" type="text" placeholder="产品名称、团号..." onkeyup="searchProduct();" onblur="searchProduct();"></div>
					<span class="clear_search" onclick="clearPanelSearch();">清除搜索</span>
					<i class="fa fa-times-circle" onclick="closePanel(this)"></i>
				</div>
				<div class="letter-nav team_topcolor">
					<label class="out_date">出团日期：</label>
					<input type="text" class="panel_input_date" onblur="searchProduct();" id="panelTimeBegin" name="panelTimeBegin" value='${panelTimeBegin}' onFocus="var panelTimeEnd=$dp.$('panelTimeEnd');WdatePicker({onpicked:function(){panelTimeEnd.focus();},maxDate:'#F{$dp.$D(\'panelTimeEnd\',{d:-1});}'})" readonly/>
					<span>至</span>
					<input type="text" class="panel_input_date" onblur="searchProduct();" id="panelTimeEnd" name="panelTimeEnd" value='${panelTimeEnd}' onFocus="WdatePicker({minDate:'#F{$dp.$D(\'panelTimeBegin\',{d:0});}'});panelTimeEnd.blur();" readonly/>
					<span class="all_record"><a href="${ctx}/activity/controlBoard/getWholeOpeRecordPage" target="_blank" class="jumptoAll"><i class="fa fa-history"></i> 全部操作记录</a></span>
				</div>
			</div>
			<div class="content-list simulate-bottom"></div>
			<!--列表开始-->
			<div class="content-list simulate-top not_scroll">
				<div class="fix_top_title">
					<span class="fir_title">产品信息</span>
					<span class="sec_title"><span>已收</span> / 预收</span>
					<span class="thir_title">余位</span>
					<span class="four_title">操作</span>
				</div>
				<div class="scroll_content" id="scrollContent">
					<input id="ctxSS" style="display: none;" value="${ctx}">
					<ul class="scroll_content_ul" id="scrollContentUl">
					</ul>
				</div>
			</div>
		</div>
			<%--通讯录jbox弹窗样式--%>
		<div id="insetJboxCont">
			<div class='inset_jbo'>
				<div class='title_pop_all'>产品名称 ：<span class='name_title'></span><br>
					<span class='same_info'></span>
				</div>
				<div class='under_pop_out'>
					<div class='under_pop_left'>
						<div class='same_div'>
							<div class='common_title'>操作项</div>
							<div class='change_input'></div>
						</div>
					</div>
					<div class='under_pop_right'> <div class='same_div'>
						<div class='common_title'>同行价</div>
						<div class='peers_price'></div>
					</div>
						<div class='same_div'>
							<div class='common_title'>直客价</div>
							<div class='guest_price'></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</shiro:hasPermission>

<div id="sea" class="remarks">
<header>
	<input type="hidden" id="sysIsShow" value="<%=UserUtils.getUser().getCompany().getIsNeedAttention()%>" />
	<input type="hidden" id="sysCtx" value="${ctx}" />
	<input type="hidden" id="ctxStatic" value="${ctxStatic }">
	<input type="hidden" id="sysCompanyUuid" value="<%=UserUtils.getUser().getCompany().getUuid()%>" />
	<input type="hidden" id="isRemind" value="${isRemind}" />
	<input type="hidden" id="remindTitleSession" value="${remindTitle}" />
	<%--<input type="hidden" id="isRemindSession" value='<%=request.getSession().getAttribute("isRemind")%>' />--%>
	<div class="hedear">
		<div class="hedear-left">
			<c:choose>
				<c:when test="${fns:getUser().company.id eq 97}">
					<div class="remote-vacation-hedear-logo"></div>
				</c:when>
				<c:when test="${fn:contains(pageContext.request.requestURL,'diamondforce')}">
					<div class="diamondforce-hedear-logo"></div>
				</c:when>
				<c:otherwise>
					<div class="hedear-logo"></div>
				</c:otherwise>
			</c:choose>
			<div class="clear"></div>
		</div>
		<div class="hedear-right">
			<ul class="hedear-nav">
				<!-- 约签公告 -->
				<%--<li class="head-contract">--%>
					<%--<dl>--%>
						<%--<dt><i></i><span id="yueqian_id"></span></dt>--%>
						<%--<dd><em></em><span>约签</span></dd>--%>
					<%--</dl>--%>
				<%--</li>--%>

				<li class="head-contract" >
					<dl>
						<dd onclick="showNoticeList()"><em></em><span>通知中心</span>
							<%--<b><i class="iNumb9"></i><i class="iNumb9"></i><i class="iNum2"></i></b>--%>
						</dd>
						<dd class="noticeList" style="height:0;">
							<p>
								<a class="announce" href="${ctx}/message/findMsgList/0" ><em></em>公告</a>
								<a class="message"  href="${ctx}/message/findMsgList/5" ><em></em>消息</a>
								<a class="remind"   href="${ctx}/message/findMsgList/7" ><em></em>提醒</a>
							</p>
						</dd>
					</dl>
				</li>

				<%--<li class="head-notice">--%>
					<%--<a href="${ctx}/message/findMsgList"><i></i><strong style="display:none;" id="msg_show"></strong>--%>
					<%--<span>通知中心</span></a>--%>
				<%--</li>--%>
				<li class="head-home"><a href="${ctx }">后台首页</a></li>
				<shiro:hasPermission name="order:progress:tracking">
					<li><em class="order-track-btn"></em><a id="orderTracking_btn" href="javascript:void(0)">订单跟踪</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="travel:service:branch">
					<li class="head-popup"  onclick="openMsml();"><a class="popup_first"></a><a href="#">门市名录</a><a class="popup_sec"></a></li>
				</shiro:hasPermission>
				<li class="head-logout"><a href="${ctx }/logout">退出</a></li>
				<div class="clear"></div>
			</ul> 
	        <c:set var="userinfo" value="${fns:getUser()}"/>
	        <p class="header-user"><em>${userinfo.name }</em>，您好<c:if test="${userinfo.loginDate!=null }">，上次登录时间 <em><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${userinfo.loginDate }"/></em></c:if>
	        	<c:if test="${userinfo.agentId!=null && fns:getAgent(userinfo.agentId).agentSalerUser!=null}"><span class="header-userspan">接口销售：${fns:getAgent(userinfo.agentId).agentSalerUser.name }</span><span>销售人电话：${userinfo.agent.agentSalerUser.mobile }</span></c:if>
	        </p>
		</div>
		<div class="clear"></div>
	</div>
  </header>
  <div class="main">
    <div class="main-left">
      <%--<ul class="main-nav">--%>
		<h2 id="menuToggle"><p></p></h2>
			<%
				List<Menu> menuList = (List<Menu>)UserUtils.getCache(UserUtils.CACHE_MENU_LIST);
			%>
		  <c:set var="menuList" value="<%=menuList%>"></c:set>
		  <c:set var="countForOrderListDZ1" value="${fns:getCountForOrderListDZ(1) }"></c:set>
		  <c:set var="countForOrderListDZ2" value="${fns:getCountForOrderListDZ(2) }"></c:set>
		  <c:set var="countForOrderListDZ3" value="${fns:getCountForOrderListDZ(3) }"></c:set>
		  <c:set var="countForOrderListDZ4" value="${fns:getCountForOrderListDZ(4) }"></c:set>
		  
		<c:forEach items="${menuList}" var="menus">



			<c:set var="menuIds" value="${menus.id},${menuIds}"></c:set>



		</c:forEach>
		<c:set var="menuIds" value=",${menuIds},"></c:set>
		<input id="tesetsetwe" value="${menuIds }" type="hidden">
		<c:set var="waitNumMap" value="${fns:getAllReviewCount() }"></c:set>
		<c:set var="isShow" value="${fns:getUser().company.isNeedAttention}"></c:set>
		<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
			<%--modify by wlj  at 20170213 for 542 menu hidden -start --%>
          <c:if test="${menu.parent.id eq 1 and menu.isShow eq 1 and not fn:contains(menu.name, '功能权限') and not fn:contains(menu.name, '新版门市名录') and not fn:contains(menu.name, '首页数据展示')}">
			  <%--modify by wlj  at 20170213 for 542 menu-end --%>
               	<c:set var="waitNumCount" value="0"></c:set>
               	<c:forEach items="${waitNumMap }" var="waitNum">
               		<c:set var="waitNumCount" value="${waitNumCount+waitNum.value }"></c:set>
               	</c:forEach>
                <c:choose>
                    <c:when test="${menu.href eq ''}">
                        <h2 id="menu_${menu.id}" class="mainMenu<c:if test="${sessionScope._m eq menu.id}"> mainMenu-on</c:if><c:if test="${idxStatus.last}"> mainMenu-last</c:if><c:if test="${fn:length(menu.childList) eq 0}"> mainMenu-noChild</c:if>">
                            <span class="iconMenu ${menu.icon}"></span>${menu.name }
                            <c:if test="${menu.name eq '审批' and waitNumCount gt 0}"><span class="untreated_verify"></span></c:if>
                            <c:if test="${menu.name eq '财务审批' and waitNumCount gt 0}"><span class="untreated_verify" style="margin-left: 28px;"></span></c:if>
							<c:if test="${menu.name eq '定价策略' and fns:getChangedCount()}"><span class="untreated_verify" style="margin-left: 28px;"></span></c:if>
                            <c:if test="${fn:length(menu.childList) ne 0}"><i class="iconMenu-arrow"></i></c:if>
                        </h2>
                    </c:when>
                    <c:otherwise>
                        <h2 id="menu_${menu.id}" class="mainMenu<c:if test="${sessionScope._m eq menu.id}"> mainMenu-on</c:if><c:if test="${idxStatus.last}"> mainMenu-last</c:if><c:if test="${fn:length(menu.childList) eq 0}"> mainMenu-noChild</c:if>">
                            <span class="iconMenu ${menu.icon}"></span><a onclick="selectMenu('${ctx}${menu.href}', this);" href="javascript:void(0);" >${menu.name }</a><c:if test="${fn:length(menu.childList) ne 0}"><i class="iconMenu-arrow"></i></c:if>
                        </h2>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${fn:length(menu.childList) ne 0}">
                        <ul class="mainMenu-ul" <c:if test="${sessionScope._m eq menu.id}">style="display: block;"</c:if>>
                            <c:forEach items="${menu.childList}" var="childMenu">
                            	<c:set var="tempChildStr" value=",${childMenu.id},"></c:set>
                                <c:if test="${fn:contains(menuIds, tempChildStr) and childMenu.isShow eq 1 and childMenu.name != '功能权限' and not empty childMenu.href}">
                                    <li id="childMenu_${childMenu.id}" <c:if test="${sessionScope._mc eq childMenu.id}">class="mainMenu-ul-on"</c:if>>
                                    	<a onclick="selectChildMenu('${ctx}${childMenu.href}', this);" href="javascript:void(0);" >${childMenu.name }
	                                    	<c:if test="${childMenu.parent.name eq '审批' || childMenu.parent.name eq '财务审批' }">
	                                    		<c:set var="waitNum" value="0"></c:set>
	                                    		<c:choose>
		                                    		<c:when test="${childMenu.name eq '预算成本审批'}">
		                                    			<c:set var="waitNum" value="${waitNumMap['15']}"></c:set>
		                                    		</c:when>
													<c:when test="${childMenu.name eq '实际成本审批'}">
														<c:set var="waitNum" value="${waitNumMap['17']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '成本付款审批'}">
		                                    			<c:set var="waitNum" value="${waitNumMap['18']}"></c:set>
		                                    		</c:when>
													<c:when test="${childMenu.name eq '返佣审批'}">
														<c:set var="waitNum" value="${waitNumMap['9']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '宣传费审批' }">
														<c:set var="waitNum" value="${waitNumMap['9']}"></c:set>
													</c:when>
		                                    		<c:when test="${childMenu.name eq '退款审批'}">
		                                    			<c:set var="waitNum" value="${waitNumMap['1']+waitNumMap['16']}"></c:set>
		                                    		</c:when>
													<c:when test="${childMenu.name eq '借款审批'}">
														<c:set var="waitNum" value="${waitNumMap['19']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '转款审批'}">
														<c:set var="waitNum" value="${waitNumMap['12']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '转团|机票改签'}">
														<c:set var="waitNum" value="${waitNumMap['11']+waitNumMap['14']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '退团|退票审批'}">
														<c:set var="waitNum" value="${waitNumMap['3']+waitNumMap['8']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '改价审批'}">
														<c:set var="waitNum" value="${waitNumMap['10']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '签证押金转担保'}">
														<c:set var="waitNum" value="${waitNumMap['6']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '还签证押金收据'}">
														<c:set var="waitNum" value="${waitNumMap['13']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '退签证押金'}">
														<c:set var="waitNum" value="${waitNumMap['7']}"></c:set>
													</c:when>
		                                    		<c:when test="${childMenu.name eq '还签证收据'}">
		                                    			<c:set var="waitNum" value="${waitNumMap['4']}"></c:set>
		                                    		</c:when>
		                                    		<c:when test="${childMenu.name eq '签证借款审批'}">
		                                    			<c:set var="waitNum" value="${waitNumMap['5']}"></c:set>
		                                    		</c:when>
		                                    		<c:when test="${childMenu.name eq '优惠审批'}">
														<c:set var="waitNum" value="${waitNumMap['21']}"></c:set>
													</c:when>
													<c:when test="${childMenu.name eq '担保变更审批'}">
														<c:set var="waitNum" value="${waitNumMap['22']}"></c:set>
													</c:when>
	                                    		</c:choose>
												<c:if test="${waitNum gt 0 }">
		                                    		<em>
														<span>
															${waitNum }
														</span>
														<p></p>
													</em>
												</c:if>
	                                    	</c:if>
                                    	</a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </c:when>
                </c:choose>
          </c:if>
      </c:forEach>
      	<%
			menuList = null;
		%>
    </div>
    <div class="main-right">
        <ul class="nav-tabs">
            <sitemesh:getProperty property="page.three_level_menu" default="" />
        </ul>
        <div class="bgMainRight">
          <sitemesh:body />
        </div>
		<div class="bs-footer">
			<p>Copyright © ${fns:getConfig('copyrightYear')} 旅游交易预订系统  Powered  by  QUAUQ  Technology  技术支持：010-85718666</p>
			<%--<div class="footer-by">Powered By Trekiz Technology</div>--%>
		</div>
    </div>
 </div>

</div>
<script type="text/javascript">
$(window).load(function(){
	//使用ajax请求第一条约签消息
    //ajaxEngage();
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function(index, element) {
        if($(this).attr("menuid")==leftmenuid){
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
});
$(function(){
    $('.closeNotice').click(function(){
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function(){
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});

$(function(){
	// 270 通知中心 提醒框 如果点击忽略，则以后都不再显示（更改）
	var $remind = $('.head-contract dd').parent().parent();
	var $isRemind = $("#isRemind").val();
	if($isRemind){	//如果忽略此提醒，则以后都不再显示（值在session中）
		var html = '<dt style="display: block; width: 320px;"> ';
		html +=	'<span><img src="';
		html += '${ctxStatic}';
		html += '/images/alram.gif" /> ';
		html +=	'<a name="remindTitle" style="width: 245px;" href="';
		html += '${ctx}';
		html += '/message/findMsgList/7"></a> ';
		html += '<a onclick="ignoreNotice(this);" style="float:right;margin-right: -234px;font-weight: normal;color: #aaa;margin-top: -27px;" >忽略</a> ';
		html +=	'</span> </dt>';

 		//如果session中有数据，就不用发ajax请求
		var remindTitleSession = $("#remindTitleSession").val();
		if(remindTitleSession){
			$remind.find("dl").prepend(html);
			$remind.addClass('head-contract-on');
			formatRemindTitle(remindTitleSession);
		}else{
			$.ajax({
				type:"POST",
				url:  "${ctx}/message/findRemindTitle",
				success: function (data){
					if (data) {
						$remind.find("dl").prepend(html);
						$remind.addClass('head-contract-on');
					}
					formatRemindTitle(data);
				}
			});
		}
	}

	$('.main-nav li').click(function(){
		$(this).addClass('select').siblings().removeClass('select');
	});
});

/**
 * 提醒title有好几段组成，将其拆分，取出需要的部分
 * @date 2016年4月11日
 * @param data
 */
function formatRemindTitle(data){
	if(data){
		var arr = data.split("#");
		var type = arr[3];
		if(type == 4){
			data = "还款提醒：批次"+ arr[1] + "需尽快还款";
		}else{
			data = "还款提醒：订单"+ arr[1] + "需尽快还款";
		}
		$("a[name=remindTitle]").html(data);
	}else{
		var $remind = $('.head-contract dd').parent().parent();
		$remind.find("dt").css("display", "none");
		$remind.removeClass('head-contract-on');
	}
}

String.prototype.formatNumberMoney= function(pattern){
	  var strarr = this?this.toString().split('.'):['0'];
	  var fmtarr = pattern?pattern.split('.'):[''];
	  var retstr='';
	  var str = strarr[0];
	  var fmt = fmtarr[0];
	  var i = str.length-1;
	  var comma = false;
	  for(var f=fmt.length-1;f>=0;f--){
	    switch(fmt.substr(f,1)){
	      case '#':
	        if(i>=0 ) retstr = str.substr(i--,1) + retstr;
	        break;
	      case '0':
	        if(i>=0) retstr = str.substr(i--,1) + retstr;
	        else retstr = '0' + retstr;
	        break;
	      case ',':
	         comma = true;
	         retstr=','+retstr;
	        break;
	     }
	   }
	  if(i>=0){
	    if(comma){
	      var l = str.length;
	      for(;i>=0;i--){
	         retstr = str.substr(i,1) + retstr;
	        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;
	       }
	     }
	    else retstr = str.substr(0,i+1) + retstr;
	   }

	   retstr = retstr+'.';

	   str=strarr.length>1?strarr[1]:'';
	   fmt=fmtarr.length>1?fmtarr[1]:'';
	   i=0;
	  for(var f=0;f<fmt.length;f++){
	    switch(fmt.substr(f,1)){
	      case '#':
	        if(i<str.length) retstr+=str.substr(i++,1);
	        break;
	      case '0':
	        if(i<str.length) retstr+= str.substr(i++,1);
	        else retstr+='0';
	        break;
	     }
	   }
	  return retstr.replace(/^,+/,'').replace(/\.$/,'');
}

String.prototype.replaceSpecialChars=function(regEx){
	if(!regEx){
		regEx = /[\`\"\~\!\@\#\$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@\#\￥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？\\]/g;
	}
	return this.replace(regEx,'');

};



</script>
<script type="application/javascript">
<%request.setAttribute("remote_im_url", Global.getMessageConfig("msg.im.baseUrl"));%>

  	//创建对应的websocket，对应的地址是以ws开头的协议，ws://xxxxx/ws.
	var socket_base_url = "${remote_im_url}/ws"; //需要根据情况指定
	var userId = "${userinfo.id}"; //指定用户的ID
	var topic_base_path = "/topic/user_msg/" + userId; //订阅地址
		//alert(socket_base_url);
	//以下处理websocket订阅处理
    //var socket = new SockJS(socket_base_url);
	//var stompClient = Stomp.over(socket);

	//msg 处理
	function renderMsg(frame) {
		var result = JSON.parse(frame.body);
		console.info("结果："+result);
		result= eval('(' + result + ')');

		 //TODO 处理约签,如果数量不为空则用ajax查询数据库，否则删除
		 if(result.yueqianCount>0){
		    //使用ajax请求第一条约签消息
		    ajaxEngage();
		 }else{
		   //$('#yueqian_id').html('');
		 }

		if(result.announceCount>0){
			// 将数值写入通知中心
			$("#msg_show").empty();
			$("#msg_show").append(result.announceCount);
		    // 将通知数字写入红色图片中
			//因为交接问题，开发C326时经讨论决定隐藏本通知数字（edit by yang.jiang 2015年12月17日 15:07:53）
		    //noticeNum();
		}else{
		   //设置公告或者消息的数量
	      $("#msg_show").html("");
		}

	}

	//通知信息数字
	function  noticeNum(){
		var iNumb=$('.head-notice strong').text();
		if(iNumb==''){
		}else{
			var str_html = '';
			if(iNumb.length > 2){
				str_html +='<i class="iNumb9"></i><i class="iNumb9"></i><i class="iNumbAdd"></i>';
			}else{
				for(var i=0;i<iNumb.length; i++){
					str_html += '<i class="iNumb'+ iNumb[i] +'"></i>';
				}
			}

			if($('.head-notice b').length==0){
				$('.head-notice strong').before('<b>'+str_html+'</b>');
			}else{
				$('.head-notice b').html(str_html);
			}
		}
	}
	// ajax约签信息
	function ajaxEngage(){
		$.ajax({
			type:"POST",
			url:  "${ctx}/message/findVisaAjax",
			dataType:"text",
			success : function(data){
				var json;
				json = $.parseJSON(data);
				if(json.msgId){
					// 判断是否已读
					if(json.ifRead==0){
						$('#yueqian_id').html('<img src="${ctxStatic}/images/alram.gif" /><a href="javascript:void(0)" onclick="gotoInfo('+json.msgId+')">约签通知：'+json.msgTitle+'</a>');
						// 未读约签消息如果已经弹出，则不做操作
						if($('.head-contract').find('dl').hasClass('head-contract-on')){
						}else{
							// 未读约签消息自动弹出
							$('.head-contract dd').click();
						}
					}else{
						$('#yueqian_id').html('<a href="javascript:void(0)" onclick="gotoInfo('+json.msgId+')">约签通知：'+json.msgTitle+'</a>');
					}
				}
			}
		});
	}

	function gotoInfo(msgId){
		window.open("${ctx}/message/goToMessageInfo/"+msgId);
	}

	//订阅地址
	var connectCallback = function() {
		stompClient.subscribe(topic_base_path, renderMsg);
	};

	//处理订阅错误
	var errorCallback = function(error) {

	    if(error && error.headers){
	      alert(error.headers.message);
	    }else{
	       console.info('网络出错：'+error);
	    }

	};

	/*$(function() {
   	   // 通过 websocket链接到服务器
        stompClient.connect("guest", "guest", connectCallback,
	    errorCallback);

	 });*/

</script>
<script src="${ctxStatic}/js/placeholder.js" type="text/javascript"></script>
</body>
</html>
