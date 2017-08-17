<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<shiro:hasPermission name="singleOrder:book"><title>预订</title></shiro:hasPermission>
<shiro:hasPermission name="order:list:guest"><title>产品</title></shiro:hasPermission>


<meta name="decorator" content="wholesaler"/>
<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<style type="text/css">
</style>
<script type="text/javascript">

$(document).ready(function() {
    $( ".spinner" ).spinner({
        spin: function( event, ui ) {
            if ( ui.value > 365 ) {
                $( this ).spinner( "value", 1 );
                return false;
            } else if ( ui.value < 0 ) {
                $( this ).spinner( "value", 365 );
                return false;
            }
        }
    });
    $(".qtip").tooltip({
        track: true
    });

    var _$orderBy = $("#orderBy").val();
    if(_$orderBy==""){
        _$orderBy="groupOpenDate DESC";
    }
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if ($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
        }
    });

    var selectdata = $("#agentIdSel").children("option:selected");
    if(selectdata.length!=0 && $("#tempUserName").val() != "lmelsguest"){
        var options = $("#agentIdSel option");
        if(selectdata.val()==null||selectdata.val()==""){
            var _select = $("#agentIdSel").clone();
            _select.attr("id","agentIdIn");
            _select.attr("name","agentIdIn");
            _select.unbind();
            var $div = $("<div id='chooseQd' class=\"tanchukuang\"></div>")
                    .append($('<div class="add_allactivity choseAgent"></div>')
                            .append($('<p style="line-height:60px;text-align:center;">我们目前为您提供'+(options.length-1)+'家渠道的预订，请根据您服务的对象选择渠道名称</p>'))
                            .append($("<label>渠道选择：</label>")).append(_select))
                    .append('<div class="ydBtn"><div class="btn btn-primary ydbz_x" onclick="choseAgent()">开始预订</div>');
            var html = $div.html();
            $.jBox(html, { title: "预订-选择渠道", buttons:{},height:220,width:550});
            letDivCenter("#jbox");
        }
    }
    if( $("#tempUserName").val() == "lmelsguest") {
        $(".lmels-ts").show();
        $("#agentIdSel").attr("disabled",true);

    }

    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
            WdatePicker(option);
        });
    };
    //滚动条
    $('.team_top').find('.table_activity_scroll').each(function(index, element) {
        var _gg=$(this).find('tr').length;
        if(_gg>=20){
            $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
            $(this).addClass("group_h_scroll");}
    });
    //产品名称获得焦点显示隐藏
    $("#wholeSalerKey").focusin(function(){
        var obj_this = $(this);
        obj_this.next("span").hide();
    }).focusout(function(){
        var obj_this = $(this);
        if(obj_this.val()!="") {
            obj_this.next("span").hide();
        }else
            obj_this.next("span").show();
    });
    if($("#wholeSalerKey").val()!="") {
        $("#wholeSalerKey").next("span").hide();
    }
    $("#groupOpenDate").datepicker({
        dateFormat:"yy-mm-dd",
        dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
        closeText:"关闭",
        prevText:"前一月",
        nextText:"后一月",
        monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
    });
    $("#groupCloseDate").datepicker({
        dateFormat:"yy-mm-dd",
        dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
        closeText:"关闭",
        prevText:"前一月",
        nextText:"后一月",
        monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
    });
    $("#targetAreaId").val("${travelActivity.targetAreaIds}");
    $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    $("#agentIdSel").change(function(){
        var selectdata = $("#agentIdSel option:selected");
        $("#agentId").val($(selectdata).val());
        $("#seachbutton").click();
    });

    //展开筛选按钮
    $('.zksx').click(function() {

        if($('.ydxbd').is(":hidden")==true) {
            $('.ydxbd').show();
            //$(this).text('收起筛选');
            $(this).addClass('zksx-on');
        }else{
            $('.ydxbd').hide();
           // $(this).text('展开筛选');
            $(this).removeClass('zksx-on');
        }
    });
    //如果展开部分有查询条件的话，默认展开，否则收起
    var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][id!='wholeSalerKey']");
    var searchFormselect = $("#searchForm").find("select");
    var inputRequest = false;
    var selectRequest = false;
    for(var i = 0; i<searchFormInput.length; i++) {
        if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
            inputRequest = true;
        }
    }
    for(var i = 0; i<searchFormselect.length; i++) {
        if($(searchFormselect[i]).children("option:selected").val() != "" && $(searchFormselect[i]).children("option:selected").val() != null) {
            selectRequest = true;
        }
    }
    if(inputRequest||selectRequest) {
        $('.zksx').click();
    }



});

function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full, payMode_op, payMode_cw,payMode_data,payMode_guarantee,payMode_express) {

    if($(child).is(":hidden")){
        var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected") : $("input[name='agentId']");

        var agentId = selectdata.val();
        if(agentId!=null&&agentId!="") {
            $.ajax({
                type:"POST",
                url:"${ctx}/stock/manager/payReservePosition",
                data:{
                    srcActivityId:srcActivityId,
                    agentId:agentId
                },
                success:function(msg) {
                    $(obj).html("关闭团期预定");
                    $(obj).next().hide();
                    $(child).css("display","table-row");
                    $(obj).parents("td").attr("class","td-extend");
                    if(msg.length>0){
                        $(child+" [class^='soldPayPosition']").show();
                    }
                    $.each(msg,function(keyin,valuein){
                        $("td .soldPayPosition"+(valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                        //如果切位余位数大于0
                        if(valuein.leftpayReservePosition>0) {
                            //三种支付方式
                            $("td .aPayforModePrice"+(valuein.activityGroupId)).each(function(index, obj) {
                                if(!$(this).hasClass("canClick")) {
                                    $(this).css("color","").unbind().click(function() {
                                        if($.trim($(this).text()) == '订金占位' && payMode_deposit == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,1,this);
                                        } else if($.trim($(this).text()) == '预占位' && payMode_advance == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,2,this);
                                        } else if($.trim($(this).text()) == '付全款' && payMode_full == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,3,this);
                                        } else if($.trim($(this).text()) == '计调确认占位' && payMode_op == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,7,this);
                                        } else if($.trim($(this).text()) == '财务确认占位' && payMode_cw == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,8,this);
                                        } else if($.trim($(this).text()) == '资料占位' && payMode_data == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,4,this);
                                        } else if($.trim($(this).text()) == '担保占位' && payMode_guarantee == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,5,this);
                                        } else if($.trim($(this).text()) == '确认单占位' && payMode_express == 1) {
                                            occupied(valuein.activityGroupId,srcActivityId,6,this);
                                        }
                                    });
                                    $("td .aPayforModePrice" + (valuein.activityGroupId) + ":last").next("input").removeClass("gray").addClass("btn-primary");
                                    $("td .aPayforModePrice" + (valuein.activityGroupId) + ":last").next("input").unbind().click(function() {
                                        orderType(this)
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }else{
            $(obj).html("关闭团期预定");
            $(child).show();
            $(obj).parents("td").addClass("td-extend");
            //$(obj).next().hide();
        }
    }else{
        if(!$(child).is(":hidden")){
            $(child).hide();
            $(obj).parents("td").removeClass("td-extend");
            $(obj).html("展开团期预定");
        }
    }
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").attr("action","${ctx}/activity/managerforOrder/listTemp");
    $("#searchForm").submit();
}

function downloads(docid){
    window.location.href = "${ctx}/sys/docinfo/download/"+docid;
}

//占位
function occupied(id,srcActivityId,payMode,_this){
    showQwOrYw("预订",srcActivityId,id,payMode,_this);
    return false;
}
//预订
function reserveOrder(id,srcActivityId,_this){

    showQwOrYw("预订",srcActivityId,id,payMode,_this);

    var selectdata = $("#agentIdSel option:selected");
    if(selectdata.length==0){
        selectdata= $(".inputagentId");
    }
    if(selectdata.val()==null||selectdata.val()==""){
        top.$.jBox.tip('请选择预订的渠道','error');
        return false;
    }
    var param = "&agentId="+selectdata.val();
    //渠道id
    window.open("${ctx}/orderCommon/manage/showforModify?payMode=" + payMode + "&type=1&productId=" + srcActivityId + "&productGroupId=" + id + param);
}

$(function(){
    $('.team_a_click').toggle(function(){
        $(this).addClass('team_a_click2')
    },function(){
        $(this).removeClass('team_a_click2')
    });
});

function takeOpenDate(obj){

    var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
    return groupOD;
}

function takeVisaDate(obj) {
    var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
    return groupOD;
}

function getOpenDate(obj){
    var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
    var strArrOD = groupOD.split("-");
    var thisMouth = parseInt(strArrOD[1])+1;
    var thisYear = parseInt(strArrOD[0]);
    if(thisMouth==13){
        thisYear+=1;
        thisMouth=1;
    }
    var thisDate = thisYear+"-"+thisMouth+"-"+strArrOD[2];
    return thisDate;
}

function doClick(productId,productGroupId,payMode){
    var selectType = $("input[name='flytype']:checked").val();
    var selectdata = $("#agentIdSel option:selected");
    if(selectdata.length==0){
        selectdata= $(".inputagentId");
    }

    if(selectdata.val()==null||selectdata.val()==""){
        top.$.jBox.tip('请选择预订的渠道','error');
        return false;
    }
    var selectIntermodalValue = $('#intermodalAreaSelect').children('select').val();
    var param = "&agentId="+selectdata.val()+"&placeHolderType="+selectType;
    if(selectIntermodalValue){
        param += "&intermodalType=" + selectIntermodalValue;
    }
    if(payMode=="1"){
        //dingj zhanwei
        window.open("${ctx}/orderCommon/manage/showforModify?payMode=1&type=2&productId="+productId+"&productGroupId="+productGroupId+param);
    }else if(payMode=="2"){
        //zanwei
        window.open("${ctx}/orderCommon/manage/showforModify?payMode=2&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
    }else if(payMode=="3"){
        window.open("${ctx}/orderCommon/manage/showforModify?payMode=3&type=1&productId="+productId+"&productGroupId="+productGroupId+param);
    }else if(payMode=="4"){
        window.open("${ctx}/orderCommon/manage/showforModify?payMode=4&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
    }else if(payMode=="5"){
        window.open("${ctx}/orderCommon/manage/showforModify?payMode=5&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
    }else if(payMode=="6"){
        window.open("${ctx}/orderCommon/manage/showforModify?payMode=6&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
    }
}
function orderType(obj) {
    var _selectType = $(obj).next().clone();
    var intermodalType = $(obj).next().next().clone();
    var intermodalFlag = false;
    if(intermodalType.length > 0){
        intermodalFlag = true;
        intermodalType.show();
    }
    $(obj).addClass("sign");
    $(_selectType).addClass("typeSelected");
    $(_selectType).show();
    var $div = $("<div class=\"tanchukuang\"></div>")
            .append($('<div class="add_allactivity"><label>付款方式：</label>').append(_selectType));
    if(intermodalFlag){
        $div.append($('<div id="intermodalAreaSelect" class="add_intermodalType"><label>离境口岸：</label>').append(intermodalType));
    }
    $div.append("<br><div class='ydBtn'><input class='btn btn-primary' type='button' onClick='orderPay()' value='预 定'/></div>");
    var html = $div.html();
    $.jBox(html, { title: "预订-选择付款方式",buttons:{},height:230});
    letDivCenter("#jbox");

}
function orderPay() {
    var selectType = $(".typeSelected option:selected").val();
    if (selectType == 3) {
        $(".sign").prevAll(".normalPayType").click();
    } else if (selectType == 7) {
		$(".sign").prevAll(".opPayType").click();
	} else if (selectType == 8) {
		$(".sign").prevAll(".cwPayType").click();
	} else if(selectType == 1) {
        $(".sign").prevAll(".dingjin_PayType").click();
    } else if(selectType == 2) {
        $(".sign").prevAll(".yuzhan_PayType").click();
    } else if(selectType == 4) {
        $(".sign").prevAll(".data_PayType").click();
    } else if(selectType == 5) {
        $(".sign").prevAll(".guarantee_PayType").click();
    } else if(selectType == 6) {
        $(".sign").prevAll(".express_PayType").click();
    }

    $(".sign").removeClass("sign");
}
function showQwOrYw(title,productId,productGroupId,type,_this){
    var selectdata = $("#agentIdSel option:selected");
    if(selectdata.length==0){
        selectdata= $(".inputagentId");
    }
    if(selectdata.val()==null||selectdata.val()==""){
        top.$.jBox.tip('请选择预订的渠道','error');
        return false;
    }
    var _td = $(_this).closest("tr").find("td[class^='soldPayPosition']");
    var qws=Number(_td.text());
    var yws=Number(_td.prev().text());
    var flag = false;
    var selectType = 0;
    if(yws<=0&&qws<=0){
        top.$.jBox.tip('余位数不足，不能预订','error');
        return false;
    }else if(yws<=0){
        //直接走切位
        selectType=1;
        flag = true;
    }else if(qws<=0){
        //直接走余位
        flag = true;
        selectType=0;
    }
    if(flag){
        var selectIntermodalValue = $('#intermodalAreaSelect').children('select').val();
        var param = "&agentId="+selectdata.val()+"&placeHolderType="+selectType;
        if(selectIntermodalValue){
            param += "&intermodalType=" + selectIntermodalValue;
        }

        if(type=="1"){
            //dingj zhanwei
            window.open("${ctx}/orderCommon/manage/showforModify?payMode=1&type=2&productId="+productId+"&productGroupId="+productGroupId+param);
        }else if(type=="2"){
            //zanwei
            window.open("${ctx}/orderCommon/manage/showforModify?payMode=2&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
        }else if(type=="3"){
            window.open("${ctx}/orderCommon/manage/showforModify?payMode=3&type=1&productId="+productId+"&productGroupId="+productGroupId+param);
        }else if(type=="4"){
            window.open("${ctx}/orderCommon/manage/showforModify?payMode=4&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
        }else if(type=="5"){
            window.open("${ctx}/orderCommon/manage/showforModify?payMode=5&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
        }else if(type=="6"){
            window.open("${ctx}/orderCommon/manage/showforModify?payMode=6&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
        }
        $(".jbox-close").click();
        return false;
    }
    var $div = $("<div class=\"tanchukuang\"></div>")
            .append('<label>订单类型：</label><p><span><input type="radio" name="flytype" checked="checked" value="1" class="radio">切位订单，剩余' + qws + '</span><span><input type="radio" name="flytype" value="0" class="radio">余位订单，剩余' + yws + '</span></p>');
    $(".add_allactivity").after('<div class="ydBtn"><div class="ydbz_x" style="width: 57px;" onclick="doClick(' + productId+',' + productGroupId+',' + type+')">' + title + '</div>');
    var html = $div.html();
    $(".add_allactivity").find("label").text("付款方式：" + $(".typeSelected option:selected").text());
    $(".typeSelected").nextAll().remove();
    $(".typeSelected").parent().append($div);
    $(".tanchukuang").prev().remove();
    $(".add_allactivity").next().next().next().next().remove();
    $("#intermodalAreaSelect").hide();
}

function choseAgent(){
    var selectdata = $("#agentIdIn option:selected");
    $("#agentIdSel").val(selectdata.val());
    $("#agentId").val(selectdata.val());
    $("#seachbutton").click();
    //$("#agentIdSel").change();
    $(".jbox-close").click();
}


function letDivCenter(divName){
    var top = ($(window).height() - $(divName).height())/2;
    $(divName).css( { 'top' : top } ).show();
}

function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = sortBy.replace(/ASC/g,"");
        }else{
            sortBy = $.trim(sortBy)+" ASC";
        }
    }

    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}

function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#searchForm").submit();
}

</script>
</head>
<body>
<content tag="three_level_menu">
    <shiro:hasPermission name="singleOrder:book">
        <shiro:hasPermission name="singleOrder:book"><li class='active'><a href="${ctx}/activity/managerforOrder/list/1">预订</a></li></shiro:hasPermission>
        <shiro:hasPermission name="order:list:guest"><li class='active'><a href="${ctx}/activity/managerforOrder/list/1">产品</a></li></shiro:hasPermission>
    </shiro:hasPermission>

    <div class="xt-activitylist">

        <c:if test="${fns:getUser().userType ==3}">
            <label class="activitylist_team_co3_text">渠道：</label>
            <select name="agentId" id="agentIdSel">
                <option value="">不限</option>
                <option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>><c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when><c:otherwise>非签约渠道</c:otherwise></c:choose></option>
                <c:forEach var="agentinfo" items="${agentinfoList }">
                    <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                </c:forEach>
            </select>
        </c:if>
    </div>
</content>

<div class="activitylist_bodyer_right_team_co_bgcolor" >
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/managerforOrder/listTemp" method="post">

    <div class="lmels-ts" style="display: none;"><img src="${ctxStatic}/logo/lmels-ts.png" />如需预定，请与浪漫俄罗斯相关销售人员联系  010-52877517；010-52899377；010-52906039；13581525134</div>

    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    <input id="agentId" name="agentId" type="hidden" value="${agentId }" />
    <input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />

    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2 pr wpr20" >
            <label>产品名称：</label><input style="width:260px" class="txtPro inputTxt" id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }"/>
            <span class="ipt-tips" style="display: block;">支持产品名称搜索</span>
        </div>

        <div class="form_submit">
            <input class="btn btn-primary ydbz_x" id="seachbutton" type="submit" value="搜索"/>
        </div>

        <div class="zksx">筛选</div>
        <c:if test="${fns:getUser().userType ==1}">
            <input type="hidden" name="agentId" class="inputagentId" value="${fns:getUser().agentId}" />
        </c:if>
        <div class="ydxbd" style="display: none;">
            <div class="activitylist_bodyer_right_team_co3">
                <div class="activitylist_team_co3_text">旅游类型：</div><form:select path="travelTypeId" itemValue="key" itemLabel="value">
                <form:option value="">不限</form:option>
                <form:options items="${travelTypes}" />
            </form:select>
            </div>

            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">出发地：</div>
                <form:select path="fromArea" itemValue="key"
                             itemLabel="value">
                    <form:option value="">不限</form:option>
                    <form:options items="${fromAreas}" />
                </form:select>

            </div>

            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">目的地：</div>
                <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                                 title="区域" url="/activity/manager/filterTreeData" checked="true"/>
            </div>

            <div class="activitylist_bodyer_right_team_co2">
                <label>出团日期：</label><input id="groupOpenDate"
                                           class="inputTxt dateinput" name="groupOpenDate"
                                           value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'
                                           style=" width: 122px;" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly />
						<span
                                style="font-size:12px; font-family:'宋体';"> 至</span><input
                    id="groupCloseDate" class="inputTxt dateinput"
                    name="groupCloseDate"
                    value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>'
                    style="width: 122px;" onClick="WdatePicker()" readonly />
            </div>
            <div class="kong"></div>

            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">产品类型：</div><form:select path="activityTypeId" itemValue="key" itemLabel="value">
                <form:option value="">不限</form:option>
                <form:options items="${productTypes}" />
            </form:select>
            </div>

            <div class="activitylist_bodyer_right_team_co3">
                <div class="activitylist_team_co3_text">产品系列：</div><form:select path="activityLevelId" itemValue="key" itemLabel="value">
                <form:option value="">不限</form:option>
                <form:options items="${productLevels}" />
            </form:select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">航空公司：</div><form:select id="trafficName" path="trafficName" itemValue="key" itemLabel="value" >
                <form:option value="" >不限</form:option>
                <form:options items="${trafficNames}"/>
            </form:select>
            </div>

            <div class="activitylist_bodyer_right_team_co2">
                <label>同行价格：</label><input id="settlementAdultPriceStart" maxlength="8" class="rmb inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                <span style="font-size:12px;font-family:'宋体';"> 至</span>
                <input id="settlementAdultPriceEnd" class="rmb inputTxt" name="settlementAdultPriceEnd" maxlength="8" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
            </div>
            <div class="kong"></div>

            <div class="activitylist_bodyer_right_team_co3">
                <div class="activitylist_team_co3_text">行程天数：</div>
                <input id="activityDuration" class="spinner" maxlength="5" name="activityDuration" value="${activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
            </div>

            <div class="activitylist_bodyer_right_team_co3">
                <div class="activitylist_team_co3_text">产品编号：</div>
                <input id="activitySerNum" name="activitySerNum" class="inputTxt"  value="${travelActivity.activitySerNum }">
            </div>
            <div class="kong"></div>
        </div>
        <div class="kong"></div>
    </div>

</form:form>

<div class="activitylist_bodyer_right_team_co_paixu">
    <div class="activitylist_paixu">
        <div class="activitylist_paixu_left">
            <ul>
                <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
                <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
                <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
                <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
                <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
                <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
            </ul>
        </div>
        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        <div class="kong"></div>
    </div>
</div>

<!-- 部门分区 -->
<div class="supplierLine" <c:if test="${empty showAreaList}">style="display: none;"</c:if>>
	<%--<a <c:if test="${empty departmentId}">class="select"</c:if> href="javascript:void(0)" onclick="getDepartment('');">全部产品</a>--%>
	<c:forEach var="department" items="${showAreaList}" varStatus="status">
		<c:choose>
			<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
				<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</div>

<form id="groupform" name="groupform" action="" method="post" >
<table id="contentTable" class="table activitylist_bodyer_table" >
<thead style="background:#403738">
<tr>
    <th width="5%">序号</th>
    <th width="10%">产品编号</th>
    <th width="10%">产品系列</th>
    <th width="10%">出发地</th>
    <!--
    <th>目的地</th>
     -->
    <th width="5%">航空</th>
    <th width="23%">产品名称</th>
    <th width="10%">成人同行价</th>
    <th width="10%">成人直客价</th>
    <th width="16%">最近出团日期</th>
</tr>
</thead>
<tbody>

<c:if test="${fn:length(page.list) <= 0 }">
    <tr class="toptr" >
        <td colspan="9" style="text-align: center;">
            暂无搜索结果
        </td>
    </tr>
</c:if>


<c:forEach items="${page.list}" var="activity" varStatus="s">
<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
<c:set var="freePositions" value="0"></c:set>
<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
    <c:if test="${activity.groupOpenDate eq group.groupOpenDate}">
        <c:set var="freePositions" value="${freePositions + group.freePosition }"></c:set>
    </c:if>
</c:forEach>

<tr id="parent${s.count}">
    <td>${s.count}<br/><br/></td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.activitySerNum) > 20}">
                <a style="text-decoration: none; color:inherit; cursor:default;" title="${activity.activitySerNum}"><c:out value="${fn:substring(activity.activitySerNum, 0, 20)}......" /></a>
            </c:when>
            <c:otherwise>
                <c:out value="${activity.activitySerNum}" />
            </c:otherwise>
        </c:choose>
    </td>
    <td>${activity.activityLevelName}</td>
    <td>
        <c:choose>
            <c:when test="${activity.intermodalStrategies[0].type == 1}">
                <span class="lianyun_name">全国联运</span>
            </c:when>
            <c:when test="${activity.intermodalStrategies[0].type == 2}">
                <span class="lianyun_name">分区联运</span>
            </c:when>
            <c:otherwise>
                ${activity.fromAreaName}
            </c:otherwise>
        </c:choose>
    </td>
    <td>
        <label class="qtip" title="${activity.trafficNameDesc}">${activity.trafficNameLabel}</label>
    </td>
    <td class="activity_name_td" >
        <a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>
        <c:choose>
            <c:when test="${activity.intermodalStrategies[0].type == 1}">
                <span class="lianyun_name">全国联运</span>
            </c:when>
            <c:when test="${activity.intermodalStrategies[0].type == 2}">
                <span class="lianyun_name">分区联运</span>
            </c:when>
            <c:otherwise>
                <span class="lianyun_name">无联运</span>
            </c:otherwise>
        </c:choose>
    </td>
    <td class="tr"><c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}">¥<span class="tdred fbold"><fmt:formatNumber value="${activity.settlementAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起</c:if></td>
    <td class="tr"><c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if test="${activity.suggestAdultPrice>0}">¥<span class="tdblue fbold"><fmt:formatNumber value="${activity.suggestAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起</c:if><br/><br/></td>
    <td>
        <c:if test="${groupsize ne 0 }">
            <c:choose>
                <c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
                <c:otherwise>${activity.groupOpenDate}至${activity.groupCloseDate}</c:otherwise>
            </c:choose>
            <br/>
            <a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id },${activity.payMode_deposit},${activity.payMode_advance},${activity.payMode_full},${activity.payMode_op},${activity.payMode_cw})" onMouseenter="if($(this).html()=='团期预定'){$(this).html('展开团期预定')}" onMouseleave="if($(this).html()=='展开团期预定'){$(this).html('团期预定')}">团期预定</a>
        </c:if>
        <c:if test="${groupsize == 0 }">
            日期待定
        </c:if>
    </td>
</tr>
<tr id="child${s.count}" style="display:none" class="activity_team_top1">
    <td colspan="9" class="team_top" style="background-color:#d1e5f5;">
        <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
            <thead>
            <tr>
                <th rowspan="2" width="10%">团号</th>
                <th rowspan="2" width="8%">出/截团日期</th>
                <th rowspan="2" width="7%">签证<br/>国家</th>
                <th rowspan="2" width="8%">资料截止日期</th>
                <th colspan="3" class="t-th2" width="18%">同行价</th>
                <th colspan="3" class="t-th2" width="18%">直客价</th>
                <th rowspan="2" width="6%">订金</th>
                <th rowspan="2" width="6%">单房差</th>
                <th rowspan="2" width="5%">预收</th>
                <th rowspan="2" width="5%">余位</th>
                <th rowspan="2" style="display:none" class="soldPayPosition"  width="4%">切位</th>
                <shiro:hasPermission name="looseOrder:operation:topay">
                    <th rowspan="2" width="5%">操作</th>
                </shiro:hasPermission>
            </tr>
            <tr>
                <th width="6%">成人</th>
                <th width="6%">儿童</th>
                <th width="6%">特殊人群</th>
                <th width="6%">成人</th>
                <th width="6%">儿童</th>
                <th width="6%">特殊人群</th>
            </tr>
            </thead></table>
        <div class="table_activity_scroll">
            <table class="table activitylist_bodyer_table table-mod2-group">
                <tbody>
                <c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">

                    <tr>
                        <td width="10%">
                                <%--<c:choose>
                                    <c:when test="${fn:length(group.groupCode) > 10}">
                                        <a style="text-decoration: none; color:inherit; cursor:default;" title="${group.groupCode}"><c:out value="${fn:substring(group.groupCode, 0, 10)}......" /></a>
                                    </c:when>
                                    <c:otherwise>
                                         <c:out value="${group.groupCode}" />
                                    </c:otherwise>
                               </c:choose>
                           --%>
                                ${group.groupCode}
                        </td>
                        <td class="p0" width="8%">
                            <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></div>
                            <div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></div>
                        </td>
                        <td width="7%">
                                ${group.visaCountry}
                        </td>
                        <td width="8%" class="tc">
                            <span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.settlementAdultPrice}">¥</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.settlementcChildPrice}">¥</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.settlementSpecialPrice}">¥</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.suggestAdultPrice}">¥</c:if><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.suggestChildPrice}">¥</c:if><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.suggestSpecialPrice}">¥</c:if><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.payDeposit}">¥</c:if><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></span>
                        </td>
                        <td width="6%" class="tr">
                            <c:if test="${not empty group.singleDiff}">¥</c:if><span  class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></span>
                        </td>
                        <td width="5%" class="tr">
                            <span>${group.planPosition }</span>
                        </td>
                        <td width="5%" class="tr tdred">
                            <span>${group.freePosition }</span>
                        </td>

                        <td style="display:none;" class="soldPayPosition${group.id}" width="4%">
                            <span class="tdred" >0</span>
                        </td>
                        <shiro:hasPermission name="looseOrder:operation:topay">
                            <td width="5%" class="tc">
                                <c:if test="${activity.payMode_full=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="normalPayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},3,this)' </c:if>>
                                        付全款
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_op=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="opPayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},7,this)' </c:if>>
                                        计调确认占位
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_cw=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="cwPayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},8,this)' </c:if>>
                                       	 财务确认占位
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_deposit=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="dingjin_PayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},1,this)' </c:if>>
                                        订金占位
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_advance=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="yuzhan_PayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},2,this)' </c:if>>
                                        预占位
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_data=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="data_PayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},4,this)' </c:if>>
                                        资料占位
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_guarantee=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="guarantee_PayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},5,this)' </c:if>>
                                        担保占位
                                    </a>
                                </c:if>
                                <c:if test="${activity.payMode_express=='1'}">
                                    <a style="display:none;" href="javascript:void(0)"  class="express_PayType aPayforModePrice${group.id}
                                        <c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}"> canClick </c:if>"
                                       style='padding-right:5px;
                                       <c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0)}">color:gray;</c:if>'
                                            <c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0)}">
                                                onClick='occupied(${group.id},${activity.id},6,this)' </c:if>>
                                        确认单占位
                                    </a>
                                </c:if>
                                <c:if test="${showType=='1'}">
                                <c:if test="${group.freePosition > 0 &&  (group.leftdays <= 0 || activity.isAfterSupplement == 1) && (group.settlementAdultPrice>0 || group.settlementcChildPrice>0)}">
                                    <input class="btn btn-primary" type="button" value="预 定" onClick="orderType(this)"/>
                                </c:if>
                                <c:if test="${group.freePosition <= 0 || (group.leftdays > 0 && activity.isAfterSupplement == 0) || (group.settlementAdultPrice <= 0 && group.settlementcChildPrice <= 0)}">
                                    <input class="btn gray" type="button" value="预 定"/>
                                </c:if>
                                <select style="display:none;">
                                    <c:if test="${activity.payMode_full=='1'}">
                                        <option value='3'>全款支付</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_op=='1'}">
                                        <option value='7'>计调确认占位</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_cw=='1'}">
                                        <option value='8'>财务确认占位</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_deposit=='1'}">
                                        <option value='1'>订金占位</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_advance=='1'}">
                                        <option value='2'>预占位</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_data=='1'}">
                                        <option value='4'>资料占位</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_guarantee=='1'}">
                                        <option value='5'>担保占位</option>
                                    </c:if>
                                    <c:if test="${activity.payMode_express=='1'}">
                                        <option value='6'>确认单占位</option>
                                    </c:if>
                                </select>
                                <c:if test="${activity.intermodalStrategies[0].type == 1 || activity.intermodalStrategies[0].type == 2}">
                                    <select style="display:none;">
                                        <option value='0'>${fns:getDictLabel(activity.outArea, "out_area", "-")}</option>
                                        <c:forEach items="${activity.intermodalStrategies}" var="is">
                                            <option value='${is.id}'>${is.groupPart}</option>
                                        </c:forEach>
                                    </select>
                                </c:if>
                            </td>
                        </shiro:hasPermission>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </td>
</tr>
</c:forEach>

</tbody>
</table>
<c:set var="userinfo" value="${fns:getUser()}"/>
<input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
</form>
</div>
<div class="pagination clearFix">${page}</div>

<div class="page"> </div>
</body>
</html>