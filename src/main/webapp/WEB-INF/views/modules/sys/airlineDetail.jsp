<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-航空公司信息详情</title>
<meta name="decorator" content="wholesaler"/>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	inputTips();
	//操作浮框
	operateHandler();

});
</script>
<style type="text/css">
.ipt-tips{position:absolute;left:7px; top:-7px;white-space:nowrap;color:#b2b2b2; z-index:1;}
a.newBtn{background:#5F7795;color:#fff;}
a.newBtn:hover {background:#28B2E6;}
</style>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">airline</page:param>
</page:applyDecorator>
<br/>
    <!--右侧内容部分开始-->
    <div class="ydbz_tit">航空公司详情</div>
    <form id="inputForm" class="form-horizontal" action="" method="post">
    <div class="airCompany">
        <div class="seach25 seach100">
        <c:forEach items="${airlineMap }" begin="0" end="0" varStatus="s" var="main">
            <p class="seach_check"><label for="inquiry_radio_flights1">
            	<c:if test="${fn:split(main.key,',')[0]=='1'}">国内航空公司</c:if></label>
            	<c:if test="${fn:split(main.key,',')[0]!='1'}">国外航空公司</c:if></label>
            </p>
        </c:forEach>
        </div>
        <div class="kongr"></div>
        <div class="inquiry_flights1">
            <c:forEach items="${airlineMap }" begin="0" end="0" varStatus="s" var="main">
            <div class="seach25 seach100">
                <p>国家：
                <trekiz:autoId2Name4Table
					tableName="sys_area" sourceColumnName="id"
					srcColumnName="name" value="${fn:split(main.key,',')[1]}" />
                
            </div>
            </c:forEach>
            <div class="kongr20"></div>
            <!--航空公司信息开始-->
            <div class="companyInfo">
            	<c:forEach items="${airlineMap }" begin="0" end="0" varStatus="s" var="main">
                <div class="seach25 seach100">
                    <p>航空公司：</p><label>${fn:split(main.key,',')[2]}</label>
                    <span>二字码：</span><label>${fn:split(main.key,',')[3]}</label>
                  <span class="linkAir">
                  <div class="airInfo-arrow"><i></i></div></span>
                </div>
                </c:forEach>
                <c:forEach items="${airlineMap }" varStatus="s" var="detail">
                <div class="airInfo" style="display:block;">
                    <div class="seach25 seach100">
                        <div class="air_fly_number_add">
                            <p>航班号：</p>${fn:split(detail.key,',')[4]}
                        </div>
                        <div class="air_fly_number_add">
                            <p>出发时间：</p>${fn:split(detail.key,',')[5]}
                        </div>
                        <div class="air_fly_number_add">
                            <p>到达时间：</p>${fn:split(detail.key,',')[6]}
                            <c:if test="${fn:split(detail.key,',')[7]!=null && fn:split(detail.key,',')[7]!=''}">+${fn:split(detail.key,',')[7]}天</c:if>
                            
                        </div>
                        <c:forEach items="${detail.value }" var="spaceLevelDetail" varStatus="spaceLevel">
                        <div class="air_fly_number_add_d">
                            <p>舱位等级<span data="shipGradeOrder">${spaceLevel.index+1 }</span>：</p>
                            <p class="seach_r">
                            <c:if test="${spaceLevelDetail.key!=null && spaceLevelDetail.key!='' }">
                            ${fns:getDictLabel(spaceLevelDetail.key,"spaceGrade_Type",spaceLevelDetail.key)}
                            </c:if>
                            </p>
                            <div class="kong"></div>
                            <ul class="listShip clearfix">
                            <c:forEach items="${spaceLevelDetail.value }" var="spaceDetail" varStatus="space">
                                <li>舱位<span>${space.index+1 }</span>：
                                <c:if test="${spaceDetail.space!=null && spaceDetail.space!='' }">
                                ${fns:getDictLabel(spaceDetail.space,"airspace_Type",spaceDetail.space)}
                                </c:if>
                                </li>
                            </c:forEach>
                            </ul>
                        </div>
                        <div class="airInfo-tit1"></div>
                        </c:forEach>
                    </div>
                </div>
                </c:forEach>
            </div>
        </div>
        <div class="release_next_add">
			<input value="关闭" onclick="window.close();" class="btn btn-primary gray" type="button" /> 
		</div>
    </div>
</form>
<!--右侧内容部分结束-->
<script type="text/javascript">
$(document).ready(function() {
    $("div[style]").each(function() {
        if ($(this).css("clear") == "both") {
        	$(this).removeAttr("style");
        }
    });
    
    inputTips();
});

function changeTab(area){
	window.location.href = "${ctx}/sys/airline/list/" + area;
}

//产品名称获得焦点显示隐藏
function inputTips(){
	$("#airlineNameKeyword").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	$("#airlineNameKeyword").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}

function forwardFormPage(){
	window.location.href = "${ctx}/sys/airline/form/1";
}

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#selForm").attr("action","${ctx}/sys/airline/list/${area}");
	$("#selForm").submit();
}

function saveDispStatus(){
	$("#selForm").attr("action","${ctx}/sys/airline/saveDispStatus");
	$("#selForm").submit();
}

function searchAirlineInfo(){
	$("#selForm").attr("action","${ctx}/sys/airline/list/${area}");
	$("#selForm").submit();
}
</script>
</body>
</html>
