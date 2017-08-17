<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
   <%--<meta http-equiv="X-UA-Compatible" content="Chrome=1,IE=edge" />--%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta charset='utf-8' />
    <title>QUAUQ（夸克）旅游交易预订系统</title>
    <%--<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>--%>
    <!--<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet"/>-->
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/json/json2.js"></script>
    <![endif]-->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <style>
        .inline {
            display: inline-block;
            float: left;
        }
        <%--解决firfoxbug- start--%>
        .box{
            display: flex;
            flex-direction:row;
            flex-wrap:wrap;
            justify-content: flex-start;
        }
        .boxLine{
            display: inline-flex;
            flex-direction:row;
            flex-wrap:wrap;
            justify-content: flex-start;
        }
        <%--解决firfoxbug- start--%>

        .phcolor{
            color: #999;
        }

    </style>

    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <!--<script type="application/javascript" src="jqueryUI/ui/jquery-ui.js"></script>-->
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1home.js"></script>
    <script>

        $(function () {
            var orderBy = $("#orderBy").val();
            var orderByArr = orderBy.split(" ");
            var orderByType = orderByArr[0];
            var orderByStatus = orderByArr[1];
            if (orderByType == "activityDuration") {
                if (orderByStatus == "ASC") {
                    $("#activityDuration i em").eq(0).removeClass("rank_up").addClass("rank_up_checked");
                } else {
                    $("#activityDuration i em").eq(1).removeClass("rank_down").addClass("rank_down_checked");
                }
            } else if (orderByType == "groupOpenDate") {
                if (orderByStatus == "ASC") {
                    $("#groupOpenDate i em").eq(0).removeClass("rank_up").addClass("rank_up_checked");
                } else {
                    $("#groupOpenDate i em").eq(1).removeClass("rank_down").addClass("rank_down_checked");
                }
            } else if (orderByType == "quauqPrice") {
                if (orderByStatus == "ASC") {
                    $("#quauqPrice i em").eq(0).removeClass("rank_up").addClass("rank_up_checked");
                } else {
                    $("#quauqPrice i em").eq(1).removeClass("rank_down").addClass("rank_down_checked");
                }
            } else {
                if (orderByStatus == "ASC") {
                    $("#defaultSort i em").eq(0).removeClass("rank_up").addClass("rank_up_checked");
                } else {
                    $("#defaultSort i em").eq(1).removeClass("rank_down").addClass("rank_down_checked");
                }
            }

            var type = "${type}";
            if (100000 == type || "" == type) {
                $("#home").removeClass("active");
                $("#out").addClass("active");
            }else{
                $("#out").removeClass("active");
                $("#home").addClass("active");
            }

            //判断浏览器是否支持placeholder属性
            var  supportPlaceholder='placeholder'in document.createElement('input'),
                    placeholder=function(input){
                        var text = input.attr('placeholder'),
                                defaultValue = input.defaultValue;
                        if('${keywordHidden}'!=""&&'${keywordHidden}'!="产品名称 / 供应商 / 团号 / 目的地") {
                            input.val('${keywordHidden}').removeClass("phcolor");
                        }else if(!defaultValue){
                            input.val(text).addClass("phcolor");
                        }

                        input.focus(function(){
                            if(input.val() == text){
                                $(this).val("");
                            }
                        });


                        input.blur(function(){
                            if(input.val() == ""){
                                $(this).val(text).addClass("phcolor");
                            }
                        });

                        //输入的字符不为灰色
                        input.keydown(function(){
                            $(this).removeClass("phcolor");
                        });
                    };

            //当浏览器不支持placeholder属性时，调用placeholder函数
            if(!supportPlaceholder){
                $('#keyword').each(function(){
                    text = $(this).attr("placeholder");
                    if($(this).attr("type") == "text"){
                        placeholder($(this));
                    }
                });
            }
        });

        /**
         * 解析后台额条件格式，将前台的条件按钮选中展示
         */
        $(document).ready(function(){
            var jsonStr='${json4Search}';
//            var jsonObj=jQuery.parseJSON(jsonStr);
            var jsonObj=JSON.parse(jsonStr);
            if(jsonObj&&jsonObj.length>0)
                for(var i=0,j=jsonObj.length;i<j;i++){
                    for (var param in jsonObj[i]){
                        switch (param) {
                            case "出发城市":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_sc", "startCity", _startCity);
                                break;
                            case "目的地":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_cy", "country", _startCity);
                                break;
                            case "抵达城市":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_ec", "endCity", _startCity);
                                break;
                            case "供应商":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_su", "groupHomeSearch_down_border", _startCity);
                                break;
                            case "出团日期":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_date", "startDate", _startCity);
                                break;
                            case "行程天数":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_day", "tourDays", _startCity);
                                break;
                            case "价格区间":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_price", "priceRange", _startCity);
                                break;
                            case "余位":
                                var _startCity=jsonObj[i][param];
                                setChecked("limit_container_seat", "remainSeat", _startCity);
                                break;
                        }
                    }
                }

        })

        function setChecked(spanId,divId,Obj){
            //首先 将该div的全部标签去除选中，并将上面的span显示
            $("#"+divId).find("p>span").first().attr("class","search_spare");
            var _upHtml="";
            $("#"+spanId).append();
            //先获得div下面的常用标签，判断需要设定额标签是否在此之内，如果在，设为选定；若不在，添加新标签，并设为选中
            Obj.name;
            var objName=[];
            var objId=[];
            for(var i=0,j=Obj.length;i<j;i++){
                objName.push(Obj[i].name);
                objId.push(Obj[i].id);
            }
            var _html="";
            var exitText=[];
            $("#"+divId).find("p>span").each(function(){
                exitText.push($(this).text());
            })
            //需要新加的
            var newCondition=[];
            var nowCondition=[];
            if(objName.length>0){
                for(var i=0;i<objName.length;i++){
                    if(exitText.indexOf(objName[i])>-1){
                        nowCondition.push(objName[i]);
                    }else{
                        newCondition.push(objName[i]);
                    }
                    _upHtml+='<span class="groupHomeSearch_right_child" id="'+objId[i]+'">'+objName[i]+'<em class="t1_2"></em></span>';
                }
            }
            for(var i=0;i<nowCondition.length;i++){
                $("#"+divId).find("p>span").each(function(){
                    if($(this).text()==nowCondition[i]){
//                        $(this).attr("class","groupHomeSearch_right_child");
                        $(this).attr("class","groupHomeSearch_down_active");
                    }
                })
            }
            for(var i=0;i<newCondition.length;i++){
                _html+='<span class="groupHomeSearch_right_child" style="display:none">'+newCondition[i]+'<em class="t1_2"></em></span>';
                //将更多中的多选框选中
                $("#"+divId).find(".city_list").children().each(function(){
                    if(newCondition[i]==$(this).text()){
                        $(this).find("em").each(function(){
                            $(this).attr("class","item_icon  selected_box");
                        })
                    }
                })
            }
            if(["startCity","endCity","groupHomeSearch_down_border","country"].indexOf(divId)>-1){
                $("#"+divId).find("p").append(_html);
            }
            $("#"+spanId).append(_upHtml);
            dealDom(spanId);

        }

        //验证只能输入数字
        //        var partten = /^[0-9]+$/;
        //        $(document).ready(function(){
        //            $('input[class=homeInput]').keyup(function(){
        //                if(!partten.test($(this).val())){
        //                    var a= $(this).val();
        //                    var b=a.replace(/[^0-9]+/gi,'');
        //                    $(this).val(b)
        //                }
        //            })
        //        });
        var partten = /^([1-9]\d*|0)(\.\d*[1-9])?$/;
        $(document).ready(function(){
            $('input[class=homeInput]').keyup(function(){
                if(!partten.test($(this).val())){
                    var a= $(this).val();
                    var b=a.replace(/([^1-9]\d*|0)(\.\d*[1-9])?$/gi,'');
                    $(this).val(b)
                }
            })
        });



        function searchConditions() {
            // 如果没有默认的页签值，则赋值为出境游
            var type = $("#type").val();
            if (!type || type == "") {
                $("#type").val("100000");
            }
            //出发城市
            var startCity = "";
            var startCity4Show = "";
            $("#limit_container_sc").children().each(function(e, item) {
                if($(item).attr("id") != undefined) {
                    startCity += $(item).attr("id") + ",";
                }
                startCity4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
            });
            $("#startCityPara").val(startCity);
            $("#startCityPara4Show").val(startCity4Show);

            //国家
            var country = "";
            var country4Show = "";
            $("#limit_container_cy").children().each(function(e, item) {
                if($(item).attr("id") != undefined) {
                    country += $(item).attr("id") + ",";
                }
                country4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
            });
            $("#countryPara").val(country);
            $("#countryPara4Show").val(country4Show);

            //目的地
            var endCity = "";
            var endCity4Show = "";
            $("#limit_container_ec").children().each(function(e, item) {
                if($(item).attr("id") != undefined) {
                    endCity += $(item).attr("id") + ",";
                }
                endCity4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
            });
            $("#endCityPara").val(endCity);
            $("#endCityPara4Show").val(endCity4Show);

            //批发商
            var supplier = "";
            var supplier4Show = "";
            $("#limit_container_su").children().each(function(e, item) {
                if($(item).attr("id") != undefined) {
                    supplier += $(item).attr("id") + ",";
                }
                supplier4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
            });
            $("#supplierPara").val(supplier);
            $("#supplierPara4Show").val(supplier4Show);

            //出团日期
            var groupDate = "";
//            var groupDate4Show = "";
            $("#limit_container_date").children().each(function(e, item) {
                groupDate += $(item).html().replace(/<[^>]+>/g,"");
//                groupDate4Show += $(item).attr("id") + ":" + $(item).val() + ",";
            });
            $("#groupDatePara").val(groupDate.replace("全部",""));
            $("#groupDatePara4Show").val(groupDate);

            //行程天数
            var days = "";
            var days4Show = "";
            $("#limit_container_day").children().each(function(e, item) {

                if($(item).attr("id") == undefined && $(item).html().replace(/<[^>]+>/g,"") != "全部") {
                    days = $(item).html().replace(/<[^>]+>/g,"");
                    days4Show = $(item).html().replace(/<[^>]+>/g,"");
                } else if ($(item).attr("id") != undefined && $(item).attr("id").indexOf("-") != -1){
                    days = $(item).attr("id") + "";
                    days4Show = $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
                } else if ($(item).attr("id") != undefined){
                    days += $(item).attr("id").replace("全部","") + ",";
                    days4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
                } else {
                    days4Show = "全部";
                }
            });
            $("#dayPara").val(days.substring(0, days.length-1));
            $("#dayPara4Show").val(days4Show);

            //价格区间
            var prices = "";
            var prices4Show = "";
            $("#limit_container_price").children().each(function(e, item) {
                if($(item).attr("id") == undefined) {
                    prices = $(item).html().replace(/<[^>]+>/g,"");
                    prices4Show = $(item).html().replace(/<[^>]+>/g,"");
                } else if ($(item).attr("id") != undefined && $(item).attr("id").indexOf("-") != -1){
                    var t = $(item).attr("id") + "";
                    prices = t.substring(0, t.length-1);
                    prices4Show = $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
                } else {
                    prices += $(item).attr("id") + "";
                    prices4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + "";
                }
            });
            $("#pricePara").val(prices.replace("全部",""));
            $("#pricePara4Show").val(prices4Show);

            //余位
            var frees = "";
            var frees4Show = "";
            $("#limit_container_seat").children().each(function(e, item) {
                if($(item).attr("id") == undefined) {
                    frees = $(item).html().replace(/<[^>]+>/g,"");
                    frees4Show = $(item).html().replace(/<[^>]+>/g,"");
                } else if ($(item).attr("id") != undefined && $(item).attr("id").indexOf("-") != -1){
                    frees = $(item).attr("id") + "";
                    frees4Show = $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + ",";
                } else {
                    frees += $(item).attr("id") + "";
                    frees4Show += $(item).attr("id") + ":" + $(item).html().replace(/<[^>]+>/g,"") + "";
                }
            });
            $("#freePara").val(frees.replace("全部",""));
            $("#freePara4Show").val(frees4Show);
        }

        function searchForm(){
            $("#keywordHidden").val($("#keyword").val());
            var type = $("#type").val();
            if (!type || type == "") {
                $("#type").val("100000");
            }
            $("#homeSearchingForm").submit();
        }
        /* function  addLimit(flag){
         var swd = $("#selected_condition").width();
         $(".groupHomeSearch_ml").each(function(){
         var This = this;
         var wid = $(This).width()+47;
         if(wid < swd){
         $(this).children().last().css('white-space','nowrap');
         }else{
         $(this).children().last().css('white-space','nowrap');
         //并且与上一个节点保持距离  即 添加一个<br/>标签
         if(flag==2){
         $(".groupHomeSearch_ml:eq(0)").after("<br/>");
         }else if(flag==3){
         $(".groupHomeSearch_ml:eq(1)").after("<br/>");
         }
         }
         });
         }*/
        function search() {
            searchConditions();
            $("#keyword").val($("#keywordHidden").val());
            $("#homeSearchingForm").submit();
        }

        function page(n,s){
            searchConditions();
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#homeSearchingForm").submit();
        }

        function sortby(sortBy,obj){
            searchConditions();
            var temporderBy = $("#orderBy").val();
            if(temporderBy.match(sortBy)){
                sortBy = temporderBy;
                if(sortBy.match(/ASC/g)){
                    sortBy = $.trim(sortBy.replace(/ASC/g,"")) + " DESC";
                }else{
                    sortBy = $.trim(sortBy.replace(/DESC/g,""))+" ASC";
                }
            }else{
                sortBy = sortBy+" ASC";
            }

            $("#orderBy").val(sortBy);
            $("#homeSearchingForm").submit();
        }

        function reload() {
            window.location.href = "${ctx}/activity/manager/homepagelist";
        }
    </script>
</head>
<body>
	<!--header start-->
    <%@ include file="T1Head.jsp"%>
    <!--header end-->
<div class="sea">
    <form id="homeSearchingForm" action="${ctx}/activity/manager/homepagelist" method="post">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="startCityPara" name="startCityPara" type="hidden" value=""/>
        <input id="countryPara" name="countryPara" type="hidden" value=""/>
        <input id="endCityPara" name="endCityPara" type="hidden" value=""/>
        <input id="supplierPara" name="supplierPara" type="hidden" value=""/>
        <input id="groupDatePara" name="groupDatePara" type="hidden" value=""/>
        <input id="dayPara" name="dayPara" type="hidden" value=""/>
        <input id="pricePara" name="pricePara" type="hidden" value=""/>
        <input id="freePara" name="freePara" type="hidden" value=""/>
        <input id="type" name="type" type="hidden" value="${type}"/>
        <input id="startCityPara4Show" name="startCityPara4Show" type="hidden" value=""/>
        <input id="countryPara4Show" name="countryPara4Show" type="hidden" value=""/>
        <input id="endCityPara4Show" name="endCityPara4Show" type="hidden" value=""/>
        <input id="supplierPara4Show" name="supplierPara4Show" type="hidden" value=""/>
        <input id="groupDatePara4Show" name="groupDatePara4Show" type="hidden" value=""/>
        <input id="dayPara4Show" name="dayPara4Show" type="hidden" value=""/>
        <input id="pricePara4Show" name="pricePara4Show" type="hidden" value=""/>
        <input id="freePara4Show" name="freePara4Show" type="hidden" value=""/>
        <input id="keywordHidden" name="keywordHidden" type="hidden" value="${keywordHidden}" />
        <!--main start-->
        <div class="main">
            <div class="mainHomePage">
                <div class="contentHomePage">
                    <div class="main_head_div">
                        <span class="hedear-logo" onclick="reload()"></span>
                        <span class="float_right font_0 ">
                            <input id="keyword" name="keyword" value="${keywordHidden}" type="text" placeholder="产品名称 / 供应商 / 团号 / 目的地"/>
                            <span class="main_head_search" onclick="searchForm();">搜 索</span>
                        </span>
                    </div>
                    <div id="group">
                        <div class="search-container">
                            <span id="out"  class="active"  onclick="changeStatus(this,100000);">出境游</span>
                            <span id="home"  onclick="changeStatus(this,200000);">国内游</span>
                        </div>
                        <div class="groupHomeSearch">
                            <div class="groupHomeSearch_one" id="groupHomeSearch_one"  style="overflow: hidden;">
                                <span class="groupHomeSearch_head groupHomeSearch_head_height">您已选择：</span>
                                <%--解决firfoxbug- start--%>
                                <div id="selected_condition" class="box" >
                                    <%--解决firfoxbug- end--%>
                                    <span class="groupHomeSearch_ml">
                                        <span class="groupHomeSearch_left">出发城市</span>
                                        <span class="groupHomeSearch_right">
                                            <span  class="limit_container boxLine" id="limit_container_sc">
                                                <%--<span class="groupHomeSearch_right_child" id="">北京<em class="t1_2"></em></span>--%>
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>
                                    <c:if test="${empty type or type eq 100000 }">
                                        <span class="groupHomeSearch_ml">
                                            <span class="groupHomeSearch_left">目的地</span>
                                            <span class="groupHomeSearch_right">
                                                 <span  class="limit_container boxLine" id="limit_container_cy">


                                                </span>
                                                <em class="t1_2 dif"></em>
                                            </span>
                                        </span>
                                    </c:if>
                                    <span class="groupHomeSearch_ml">
                                        <span class="groupHomeSearch_left">抵达城市</span>
                                        <span class="groupHomeSearch_right">
                                            <span  class="limit_container boxLine" id="limit_container_ec">
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>
                                    <span class="groupHomeSearch_ml">
                                	    <span class="groupHomeSearch_left">供应商</span>
                                        <span class="groupHomeSearch_right">
                                    	    <span class="limit_container boxLine" id="limit_container_su">
                                        	    <!--<span class="">杭州<em class="t1_2"></em></span>-->
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>
                                    <span class="groupHomeSearch_ml">
                                	    <span class="groupHomeSearch_left">出团日期</span>
                                        <span class="groupHomeSearch_right">
                                    	    <span class="limit_container" id="limit_container_date">
                                        	    <!--<span class="">杭州<em class="t1_2"></em></span>-->
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>
                                    <span class="groupHomeSearch_ml">
                                        <span class="groupHomeSearch_left">行程天数</span>
                                        <span class="groupHomeSearch_right">
                                            <span class="limit_container" id="limit_container_day">
                                                <!--<span class="">杭州<em class="t1_2"></em></span>-->
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>
                                    <span class="groupHomeSearch_ml">
                                        <span class="groupHomeSearch_left">价格区间</span>
                                        <span class="groupHomeSearch_right">
                                            <span class="limit_container" id="limit_container_price">
                                                <!--<span class="">杭州<em class="t1_2"></em></span>-->
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>
                                    <span class="groupHomeSearch_ml">
                                        <span class="groupHomeSearch_left">余位</span>
                                        <span class="groupHomeSearch_right">
                                            <span class="limit_container" id="limit_container_seat">
                                                <!--<span class="">杭州<em class="t1_2"></em></span>-->
                                            </span>
                                            <em class="t1_2 dif"></em>
                                        </span>
                                    </span>

                                    <span class="inline">
                                        <span class="groupHomeSearch_ml_sure" onclick="search()">确 定</span>
                                        <span class="cleared_condition" onclick="searchForm()">清除筛选条件</span>
                                    </span>
                                    <%--2016.08.15  清除浮动-start --%>
                                    <span style="clear: both;width: 2px;height: 34px;"></span>
                                </div>
                            </div>
                            <div class="groupHomeSearch_down" id="startCity">
                                <span class="groupHomeSearch_head">出发城市：</span>
                                <p class="groupHomeSearch_down_p">
                                    <span class="groupHomeSearch_down_active search_spare">全部</span>
                                    <c:forEach items="${fromAreaList}" var="fromArea" ><%--begin="0" end="13"--%>
                                        <span class="search_spare" id="${fromArea.value}">${fromArea.label}</span>
                                        <%--<span class="search_spare">上海</span>--%>
                                        <%--<span class="search_spare">南京</span>--%>
                                        <%--<span class="search_spare">广州</span>--%>
                                    </c:forEach>
                                </p>
                                <%--<a class="search_more"  onclick="switchTip(this);">更多<em class="more_icon"></em></a>--%>
                                <%--<div class="main_container">--%>
                                <%--<em class="main_icon"></em>--%>
                                <%--<span class="main_header">--%>
                                <%--<em class="city_icon"></em>--%>
                                <%--<span class="city_text">出发城市</span>--%>
                                <%--<em class="close_icon"  onclick="closeTip(this);"></em>--%>
                                <%--</span>--%>
                                <%--<div class="main_content">--%>
                                <%--<div class="input_container">--%>
                                <%--<input type="text"  class="se_input">--%>
                                <%--<em class="input_icon"  onclick="fuzzySearch(this);"></em>--%>
                                <%--</div>--%>
                                <%--<ul class="city_list">--%>
                                <%--<c:forEach items="${fromAreaList}" var="fromArea" begin="14">--%>
                                <%--<li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span class="item_text" id="${fromArea.value}">${fromArea.label}</span></li>--%>
                                <%--</c:forEach>--%>
                                <%--</ul>--%>
                                <%--</div>--%>
                                <%--<span class="main_footer">--%>
                                <%--<span class="butn_sure"  onclick="getEle(this,1);">确定</span>--%>
                                <%--</span>--%>
                                <%--</div>--%>
                            </div>
                            <c:if test="${empty type or type eq 100000 }">
                                <div class="groupHomeSearch_down" id="country">
                                    <span class="groupHomeSearch_head">目的地：</span>
                                    <p class="groupHomeSearch_down_p">
                                        <span class="groupHomeSearch_down_active search_spare">全部</span>
                                        <c:forEach items="${countrys}" var="country" begin="0" end="12">
                                            <span class="search_spare" id="${country.id}">${country.name}</span>
                                        </c:forEach>
                                    </p>
                                    <c:if test="${fn:length(countrys) > 13}">
                                        <a class="search_more"  onclick="switchTip(this);">更多<em class="more_icon"></em></a>
                                        <div class="main_container">
                                            <em class="main_icon"></em>
                                            <span class="main_header">
                                            <em class="city_icon"></em>
                                            <span class="city_text">目的地</span>
                                            <em class="close_icon"  onclick="closeTip(this);"></em>
                                        </span>
                                            <div class="main_content">
                                                <div class="input_container">
                                                    <input type="text"  class="se_input">
                                                    <em class="input_icon"  onclick="fuzzySearch(this);"></em>
                                                </div>
                                                <ul class="city_list">
                                                    <c:forEach items="${countrys}" var="country" begin="13">
                                                        <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span class="item_text" id="${country.id}">${country.name}</span></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                            <span class="main_footer">
                                            <span class="butn_sure"  onclick="getEle(this,4);">确定</span>
                                        </span>
                                        </div>
                                    </c:if>
                                </div>
                            </c:if>
                            <div class="groupHomeSearch_down" id="endCity">
                                <span class="groupHomeSearch_head">抵达城市：</span>
                                <p class="groupHomeSearch_down_p">
                                    <span class="groupHomeSearch_down_active search_spare">全部</span>
                                    <c:forEach items="${targetAreas}" var="targetArea" begin="0" end="11">
                                        <span class="search_spare" id="${targetArea.id}">${targetArea.name}</span>
                                    </c:forEach>
                                </p>
                                <c:if test="${fn:length(targetAreas) > 13}">
                                    <a class="search_more"  onclick="switchTip(this);">更多<em class="more_icon"></em></a>
                                    <div class="main_container">
                                        <em class="main_icon"></em>
                                        <span class="main_header">
                                        <em class="city_icon"></em>
                                        <span class="city_text">抵达城市</span>
                                        <em class="close_icon"  onclick="closeTip(this);"></em>
                                    </span>
                                        <div class="main_content">
                                            <div class="input_container">
                                                <input type="text"  class="se_input">
                                                <em class="input_icon"  onclick="fuzzySearch(this);"></em>
                                            </div>
                                            <ul class="city_list">
                                                <c:forEach items="${targetAreas}" var="targetArea" begin="12">
                                                    <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span class="item_text" id="${targetArea.id}">${targetArea.name}</span></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <span class="main_footer">
                                        <span class="butn_sure"  onclick="getEle(this,2);">确定</span>
                                    </span>
                                    </div>
                                </c:if>
                            </div>
                            <div class="groupHomeSearch_down " id="groupHomeSearch_down_border">
                                <span class="groupHomeSearch_head">供应商：</span>
                                <p class="groupHomeSearch_down_p">
                                    <span class="groupHomeSearch_down_active search_spare">全部</span>
                                    <%--<span class="search_spare"></span>--%>
                                    <c:forEach items="${supplierInfos}" var="supplierInfo" begin="0" end="7">
                                        <span class="search_spare" id="${supplierInfo.id}">${supplierInfo.name}</span>
                                    </c:forEach>
                                </p>
                                <c:if test="${fn:length(supplierInfos) > 8}">
                                    <a class="search_more"  onclick="switchTip(this);">更多<em class="more_icon"></em></a>
                                    <div class="main_container">
                                        <em class="main_icon"></em>
                                        <span class="main_header">
                                        <em class="city_icon"></em>
                                        <span class="city_text">供应商</span>
                                        <em class="close_icon"  onclick="closeTip(this);"></em>
                                    </span>
                                        <div class="main_content">
                                            <div class="input_container">
                                                <input type="text"  class="se_input">
                                                <em class="input_icon"  onclick="fuzzySearch(this);"></em>
                                            </div>
                                            <ul class="city_list">
                                                <c:forEach items="${supplierInfos}" var="supplierInfo" begin="8">
                                                    <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span class="item_text" id="${supplierInfo.id}">${supplierInfo.name}</span></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <span class="main_footer">
                                        <span class="butn_sure"  onclick="getEle(this,3);">确定</span>
                                    </span>
                                    </div>
                                </c:if>
                            </div>
                            <div class="more_less_div">
                                <div class="groupHomeSearch_down" id="startDate">
                                    <span class="groupHomeSearch_head">出团日期：</span>
                                    <p class="groupHomeSearch_down_p">
                                        <span class="groupHomeSearch_down_active">全部</span>
                                        <span >
                                            <input id="groupOpenDate" class="inputTxt dateinput homeInput" name="groupOpenDateBegin" value="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" > —
                                            <input id="groupCloseDate" class="inputTxt dateinput homeInput" name="groupOpenDateEnd" value="" onclick="WdatePicker()" >
                                            <span class="ascertain">确定</span>
                                        </span>
                                    </p>
                                </div>

                                <div class="groupHomeSearch_down" id="tourDays">
                                    <span class="groupHomeSearch_head">行程天数：</span>
                                    <p class="groupHomeSearch_down_p">
                                        <span class="groupHomeSearch_down_active search_spare">全部</span>
                                        <span class="search_spare" id="d1">1天</span>
                                        <%--<span class="search_spare" id="d2">2天</span>--%>
                                        <span class="search_spare" id="d3">3天</span>
                                        <span class="search_spare" id="d4">4天</span>
                                        <span class="search_spare" id="d5">5天</span>
                                        <span class="search_spare" id="d6">6天</span>
                                        <span class="search_spare" id="d7">7天</span>
                                        <span class="search_spare" id="d8">8天</span>
                                        <span class="search_spare" id="d9">9天</span>
                                        <span class="search_spare" id="d0">10天以上</span>
                                        <span>
                                        <input type="text" class="homeInput"/> —
                                        <input type="text" class="homeInput"/>
                                        <span class="ascertain" >确定</span>
                                    </span>
                                    </p>
                                </div>
                                <div class="groupHomeSearch_down" id="priceRange">
                                    <span class="groupHomeSearch_head">价格区间：</span>
                                    <p class="groupHomeSearch_down_p groupHomeSearch_down_p_long">
                                        <span class="groupHomeSearch_down_active search_spare">全部</span>
                                        <span class="search_spare" id="p0">3000元以下</span>
                                        <span class="search_spare" id="p1">3000-4999</span>
                                        <span class="search_spare" id="p2">5000-7999</span>
                                        <span class="search_spare" id="p3">8000-9999</span>
                                        <span class="search_spare" id="p4">10000元以上</span>
                                        <span>
                                        <input type="text" class="homeInput"/> —
                                        <input type="text" class="homeInput"/>
                                        <span class="ascertain">确定</span>
                                    </span>
                                    </p>
                                </div>
                                <div class="groupHomeSearch_down" id="remainSeat">
                                    <span class="groupHomeSearch_head">余位：</span>
                                    <p class="groupHomeSearch_down_p">
                                        <span class="groupHomeSearch_down_active search_spare">全部</span>
                                        <span class="search_spare" id="f0">10以下</span>
                                        <span class="search_spare" id="f1">10-19</span>
                                        <span class="search_spare" id="f2">20-29</span>
                                        <span class="search_spare" id="f3">30以上</span>
                                        <span>
                                        <input type="text" class="homeInput"/> —
                                        <input type="text" class="homeInput"/>
                                        <span class="ascertain">确定</span>
                                    </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="more_less" onclick="more_less()">
                        <span class="show_more">显示更多筛选条件<em class="t1_2"></em></span>
                        <span class="show_less">隐藏更多筛选条件<em class="t1_2"></em></span>
                    </div>
                    <div class="rank">
                        <span class="float_left ">排序：</span>
                        <span id="defaultSort" class="float_left rank_child_left rank_child_left_two" onClick="sortby('defaultSort',this)">默认 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
                        <span id="activityDuration" class="float_left rank_child_left rank_child_left_two" onClick="sortby('activityDuration',this)">行程天数 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
                        <span id="groupOpenDate" class="float_left rank_child_left" onClick="sortby('groupOpenDate',this)">出团日期 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
                        <span id="quauqPrice" class="float_left rank_child_left" onClick="sortby('quauqPrice',this)">结算价 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
                        <span id="page2" class="float_right">
                            <%--<span class="rank_product_right">共 <span class="orange font_14">${page.count}</span> 条产品</span>--%>
                            <%--<span>--%>
                                <%--<em class="orange_left t1_2"></em>--%>
                                <%--<span class="orange">1</span>--%>
                                <%--<span>/</span>--%>
                                <%--<span>100</span>--%>
                                <%--<em class="orange_right t1_2"></em>--%>
                            <%--</span>--%>
                            ${page2}
                        </span>
                    </div>
                    <div >
                        <table class="table_width table_width_hover table_width_collapse">
                            <thead class="groupOrder J_m_nav groupHomerOrder " id="J_m_nav">
                            <tr style="background-color: #fffbf8">
                                <th width="35px" class="first_t">序号</th>
                                <th width="400px">产品信息</th>
                                <th width="130px">团号</th><%--bug16444 将团号宽度从90px改为130px--%>
                                <th width="90px">出团日期</th>
                                <th width="100px">出发城市</th><%--bug16444 将团号宽度从140px改为100px--%>
                                <th width="50px">余位</th>
                                <th width="80px" class="t_right">建议直客价</th>
                                <th width="125px" class=" last_t t_right">结算价</th>
                            </tr>
                            </thead>
                            <tbody id="content">
                            <c:forEach items="${page.list}" var="activity" varStatus="v">
                                <tr>
                                    <td width="35px" class="first_t"><p>
                                        <c:choose>
                                            <c:when test="${v.count < 10}">0${v.count}</c:when>
                                            <c:otherwise>${v.count}</c:otherwise>
                                        </c:choose>
                                    </p></td>
                                    <td width="400px">
                                        <p title="${activity.activityName}"><a href="javascript:void(0)" onclick="details(${activity.travelactivity_id},'${activity.groupCode}','${ctx}')">${activity.activityName}</a></p>
                                        <p>
                                            <span>行程天数：</span><span>${activity.activityDuration}天</span>
                                            <span class="margin_left_20">供应商：</span><span>${activity.supplierName}</span>
                                        </p>
                                    </td>
                                    <td width="130px" class="td_bottom"><%--bug16444--%>
                                        <p>${activity.groupCode}</p>
                                    </td>
                                    <td width="90px">
                                        <p>${activity.groupOpenDate}</p>
                                    </td>
                                    <td width="100px"><%--bug16444--%>
                                        <p>
                                            <c:forEach items="${fromAreas}" var="fromArea">
                                                <c:if test="${fromArea.value eq activity.fromArea }">
                                                    ${fromArea.label}
                                                </c:if>
                                            </c:forEach>
                                        </p>
                                    </td>
                                    <td width="50px" class="relative">
                                        <p class="surplus">${activity.freePosition}</p>
                                        <!--<em class="hot"></em>-->
                                    </td>
                                    <td width="70px" class="t_right ">
                                        <c:if test="${not empty activity.suggestAdultPrice}">
                                            <p>${fns:getCurrencyInfo(activity.currencyids,3,'mark')}<fmt:formatNumber  type="currency" pattern="###0.##" value="${activity.suggestAdultPrice}" /></p>
                                        </c:if>
                                    </td>
                                    <td width="120px" class="last_t t_right">
                                        <c:if test="${not empty activity.quauqPrice}">
                                            <p>${fns:getCurrencyInfo(activity.currencyids,6,'mark')}<span class="money_color">
                                                <c:if test="${not empty fns:getCurrencyInfo(activity.currencyids,6,'id')}">
                                                        <fmt:formatNumber  type="currency" pattern="###0.##" value="${fns:getRetailPrice(activity.activitygroup_id, 2, activity.settlementAdultPrice, activity.quauqPrice, fns:getCurrencyInfo(activity.currencyids,6,'id'))}" />
                                                </c:if>
                                                    </span></p>
                                        </c:if>
                                        <p class="trade_price">同行价：<span title=""><s>
                                                <c:if test="${not empty activity.settlementAdultPrice}">
                                                    ${fns:getCurrencyInfo(activity.currencyids,3,'mark')}<fmt:formatNumber  type="currency" pattern="###0.##" value="${activity.settlementAdultPrice}" />
                                                </c:if>
                                            </s></span></p>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="page">
                        <div class="pagination">
                            <div id="page" class="endPage">
                                ${page}
                                
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--main end-->

    </form>

    <!--footer start-->
    <%@ include file="t1footer.jsp"%>
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
