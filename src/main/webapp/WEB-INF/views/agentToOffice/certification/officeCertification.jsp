<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8,Chrome=1">
    <title>批发商认证</title>
   <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
	
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <style type="text/css">
        .fa-angle-right:before,.fa-angle-left:before{
            content: "";
        }
    </style>
    <script>
    //分页
    function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/wholesalers/certification/getOfficeList");
		$("#searchForm").submit();
	}
    /*//首页
    function goHomePage(){
        location.href = "${ctx}/t1/newHome";
    }*/
    //搜索
    function searchForm(){
    	$("#searchForm").submit();
    }
    $(function () {
        $(".brief_intro").each(function(){
            var maxwidth=141;
            if($(this).text().length>maxwidth){
                $(this).text($(this).text().substring(0,maxwidth));
                $(this).html($(this).html()+'…');
            }
        });
    });
    </script>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/homepage/T1Head.jsp"%>
<div class="sea">
    <div class="main">
        <div class="mainHomePage">
            <div class="contentHomePage">
           		 <form id="searchForm" method="post" action="${ctx}/wholesalers/certification/getOfficeList">
                	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/> 
					<div class="main_head_div" style="display:none">
                        <span class="hedear-logo" onclick="goHomePage('${ctx}')"></span>
                        <span class="float_right font_0 ">
                            <input class="wholesale_search" name="groupCodeOrOfficeNameOrActivityName"  value="${groupCodeOrOfficeNameOrActivityName }" type="text" placeholder="产品名称 / 供应商 / 团号 ">
                            <span class="main_head_search" onclick="searchForm();">搜 索</span>
                        </span>
                    </div>
                </form>
                <div class="bread_new"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('${ctx}')">首页</a> &gt; 批发商认证</div>
               
                <div id="group">
                    <ul class="wholesale_list">
                    	<c:forEach items="${page.list }" var="office">
                    		 <li>
                            <c:choose>
	                            <c:when test="${not empty office.logo}">
	                                <img src="${ctx }/person/info/getLogo?id=${office.logo}" alt="logo"/>
	                            </c:when>
	                            <c:otherwise>
	                                <img src="${ctxStatic}/images/T1T2/no_logo.png" alt="暂无logo"/>
	                            </c:otherwise>
	                        </c:choose>
                            <div class="brief_details">
                                <span>${office.name }</span>
                                <span class="brief_intro">${office.summary }</span>
                                <span class="phone_num"><span>线路：${office.count }</span><span>电话：${office.phone }</span>     <span>网址：${office.web_site }</span></span>
                            </div>
                            <c:choose>
                            	<c:when test="${empty office.business_license && empty office.business_certificate && empty office.cooperation_protocol }">
                            			<span  class="btn_all_detail un_cursor" href="javascript:void(0)"  target="_blank"> 查看详情 <i class="fa fa-angle-double-right"></i></span>
                           		 </c:when>
                           		 <c:otherwise>
                           		 	<a  class="btn_all_detail" href="${ctx }/wholesalers/certification/officeDetail?companyId=${office.id}" target="_blank"> 查看详情 <i class="fa fa-angle-double-right"></i></a>
                           		 </c:otherwise>
                            </c:choose>
                        </li>
                    	</c:forEach>
                    </ul>
                    <div class="page">
                        <div class="pagination other_page_style">
                            <div class="endPage" style="text-align:center;margin:0 auto">
                                	${page }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--main end-->
    </div>
    <!--footer start-->
    <%@ include file="/WEB-INF/views/modules/homepage/t1footer.jsp"%><!--modify by wlj at 2016.11.24 for huiteng-start-->


    <!--footer end-->
</div>
</body>
<script>
    var nt = !1;
    $(window).bind("scroll",
            function () {
                var st = $(document).scrollTop();//往下滚的高度
                nt = nt ? nt : $(".J_m_nav").offset().top;
                var sel = $(".J_m_nav");
                if (nt < st) {
                    sel.addClass("nav_fixed_home");
                } else {
                    sel.removeClass("nav_fixed_home");
                }
            });
</script>
</html>

