	<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>切位草稿箱</title>
	
	<!-- 页面左边和上边的装饰 -->
	<meta name="decorator" content="wholesaler" />
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>

    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <style type="text/css">
    	.table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
   		 background-color: #ffffff !important;
	}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #f9f9f9 !important;
}
        .sort {
            color: #0663A2;
            cursor: pointer;
        }
        table tbody tr td .edit-input{
            display: none;
            width:80% ;
            text-align: center;
            margin-bottom: auto
        }
     .pay_record_new ul li 
    	.ico-download {
    		display: inline-block;
    		width: 16px;
   			 height: 16px;
    		background: transparent url("${ctxStatic}/images/handle_xz.png") no-repeat scroll 0px 0px;
   		   cursor: pointer;
   		   margin-top: 10px;
           margin-left: 10px;
         } 
         em, i {
    		font-style: normal;
		}
		.pay_record_new ul li .ico-del {
		    display: inline-block;
		    width: 14px;
		    height: 14px;
		    background: transparent url("${ctxStatic}/images/del.png") no-repeat scroll 0px 0px;
		    cursor: pointer;
		    margin-left: 10px;
		    margin-top: 10px;
		}   
		.display-none {
    		display: none;
		}
    </style>


    <script type="text/javascript">

        var activityIds = "";

        $(document).on('change','[name="checkAll"]',function(){
            if($(this).attr('checked')){
                $(this).parents('.toggle').find('input[type="checkbox"]').attr('checked',true);
            }else{
                $(this).parents('.toggle').find('input[type="checkbox"]').attr('checked',false);
            }
        });
		
		$(document).on('change','[name="groupId"]',function(){
			if($("input[name='groupId']:checked").length==$("input[name='groupId']").length){
				$("input[name='checkAll']").attr("checked",true);
			}else{
				$("input[name='checkAll']").attr("checked",false);
			}
		});
		
        //操作浮框
        function operateHandler() {
            $('.handle').hover(function () {
                if (0 != $(this).find('a').length) {
                    $(this).addClass('handle-on');
                    $(this).find('dd').addClass('block');
                }
            }, function () {
                $(this).removeClass('handle-on');
                $(this).find('dd').removeClass('block');
            });
        }

        $(function () {
            //操作浮框
            operateHandler();
			$("#wholeSalerKey").focusin(function(){
	 			var obj_this = $(this);
	 			obj_this.next("span").hide();
	 		}).focusout(function(){var obj_this = $(this);
	 		if(obj_this.val()!="") {
	 			obj_this.next("span").hide();
	 		}else
	 			obj_this.next("span").show();
	 		});
	 		if($("#wholeSalerKey").val()!="") {
	 			$("#wholeSalerKey").next("span").hide();
	 		}; 
            $(".spinner").spinner({
                spin: function (event, ui) {
                    if (ui.value > 365) {
                        $(this).spinner("value", 1);
                        return false;
                    } else if (ui.value < 0) {
                        $(this).spinner("value", 365);
                        return false;
                    }
                }
            });
            $(".qtip").tooltip({
                track: true
            });

            $('.team_top').find('.table_activity_scroll').each(function (index, element) {
                var _gg = $(this).find('tr').length;
                if (_gg >= 20) {
                    $(this).addClass("group_h_scroll");
                    $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
                }

            });

            $("#targetAreaId").val("");
            $("#targetAreaName").val("");

            //展开筛选按钮
            $('.zksx').click(function () {
                if ($('.ydxbd').is(":hidden") == true) {
                    $('.ydxbd').show();
                    $(this).text('收起筛选');
                    $(this).addClass('zksx-on');
                } else {
                    $('.ydxbd').hide();
                    $(this).text('展开筛选');
                    $(this).removeClass('zksx-on');
                }
            });
            //如果展开部分有查询条件的话，默认展开，否则收起
            var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
            var searchFormselect = $("#searchForm").find("select");
            var inputRequest = false;
            var selectRequest = false;
            for (var i = 0; i < searchFormInput.length; i++) {
                if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
                    inputRequest = true;
                }
            }
            for (var i = 0; i < searchFormselect.length; i++) {
                if ($(searchFormselect[i]).children("option:selected").val() != "" &&
                        $(searchFormselect[i]).children("option:selected").val() != null) {
                    selectRequest = true;
                }
            }
            if (inputRequest || selectRequest) {
                $('.zksx').click();
            }

            //隐藏保存按钮
            $('[name="save"]').parent().hide();

        });

        function expand(child, obj, srcActivityId) {
            if ($(child).is(":hidden")) {

                if ("" == "1") {
                    $.ajax({
                        type: "POST",
                        url: "/a/stock/manager/apartGroup/payReservePosition",
                        data: {
                            srcActivityId: srcActivityId
                        },
                        success: function (msg) {
                            $(obj).html("关闭全部团期");
                            $(child).show();
                            $(obj).parents("td").attr("class", "td-extend");
                            if (msg.length > 0) {
                                $(child + " [class^='soldPayPosition']").show();
                            }
                            $.each(msg, function (keyin, valuein) {
                                $("td .soldPayPosition" + (valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                            });
                        }
                    });
                } else {
                    $(obj).html("关闭全部团期");
                    $(child).show();
                    $(obj).parents("td").attr("class", "td-extend");
                }
            } else {
                if (!$(child).is(":hidden")) {
                    $(child).hide();
                    $(obj).parents("td").attr("class", "");
                    $(obj).html("展开全部团期");
                }
            }
        }
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action", "/a/stock/manager/apartGroup");
            $("#searchForm").submit();
        }
        function confirmBatchIsNull(mess, sta) {
            if (activityIds != "") {
                if (sta == 'off') {
                    confirmBatchOff(mess);
                } else if (sta == 'del') {
                    confirmBatchDel(mess);
                }
            } else {
                $.jBox.error('未选择产品', '系统提示');
            }
        }
        function getSoldPayPosition(obj) {

        }
        function modgroup(groupid, savebtn, delbtn, cancelbtn, obj) {
            $(obj).parent().parent().find("span").css("display", "none");
            $(obj).parent().parent().find("input[type='text']").css("display", "inline-block");
            $(obj).parent().parent().find("input[type='text']").attr("disabled", false);
            $(groupid).css("display", "none");
            $(groupid).attr("disabled", false);
            $(savebtn).css("display", "block");
            $(delbtn).css("display", "none");
            $(cancelbtn).css("display", "block");
            $(obj).css("display", "none");
        }
        function cancelgroup(modbtn, savebtn, delbtn, obj) {
            $(modbtn).css("display", "block");
            $(savebtn).css("display", "none");
            $(delbtn).css("display", "block");
            $(obj).css("display", "none");
            $(obj).parent().parent().find("span").css("display", "block");
            $(obj).parent().parent().find("input[type='text']").css("display", "none");
            $(obj).parent().parent().find("input[type='text']").attr("disabled", true);
        }
        function downloads(docid) {
            window.open("${ctx}/sys/docinfo/download/" + docid);
            //window.location.href = "/a/sys/docinfo/download/"+docid;
        }
        $(function () {
            $('.team_a_click').toggle(function () {
                $(this).addClass('team_a_click2');
            }, function () {
                $(this).removeClass('team_a_click2');
            });
        });

        function getCurDate() {
            var curDate = new Date();
            return curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + (curDate.getDate() + 1);
        }
        //控制截团时间
        function takeOrderOpenDate(obj) {
            var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
            return groupOD;
        }
        function takeModVisaDate(obj) {
            var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
            return groupOD;
        }
        function comparePositionMod(obj) {
            var plan = $(obj).val();
            $(obj).parent().next().find("input").val(plan);
            $(obj).parent().next().find("input").focus();
            $(obj).parent().next().find("input").blur();
        }

        function sortby(sortBy, obj) {
            var temporderBy = $("#orderBy").val();
            if (temporderBy.match(sortBy)) {
                sortBy = temporderBy;
                if (sortBy.match(/ASC/g)) {
                    sortBy = sortBy.replace(/ASC/g, "");
                } else {
                    sortBy = $.trim(sortBy) + " ASC";
                }
            }

            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }

        function groupDetail(url) {
            window.open(encodeURI(encodeURI(url)));
        }

        var requestAgentId;
        var requestActivityGroupId;
        var maxReserveNum;
        var activityGroupList;
        var maxFontMoneyBack;

        function returnReserve(mess, activityGroupId, leftFontMoney, freePosition) {

            requestActivityGroupId = activityGroupId;
            var option = '<div style="margin-top:20px; padding-left:0px;">';
            option += '<p>当前余位数量：' + freePosition + '</p><dl style="overflow:hidden; padding-right:5px;"><dd style=" margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">渠道</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>';
            $.ajax({
                type: "POST",
                async: false,
                url: "/a/stock/manager/apartGroup/getReserveByGroupId?dom=" + Math.random(),
                data: {
                    activityGroupId: activityGroupId
                },
                success: function (msg) {
                    activityGroupList = msg;
                    for (var i = 0; i < msg.length; i++) {
                        option = option + '<tr><td><input name="radioid"  id="radioid" type="radio" value="' + msg[i].agentId + "~" + msg[i].payReservePosition + "~" + msg[i].soldPayPosition + '" style="margin:0;" />' + (i + 1) + '</td><td> ' + msg[i].agentName + '</td><td> ' + msg[i].payReservePosition + '</td><td> ' + msg[i].soldPayPosition + '</td></tr>';
                    }
                }
            });
            option += "</table></div>";

            var mess = '<div class="msg" style="margin-left:10px"><div class="field">' + option +
                    '</div>' +
                    '<p>归还切位数量：</p><div class="field"><input type="text" style="width:150px" id="reserveBackAmount" name="reserveBackAmount" value="" /></div>  ' +
                    '<div class="errorBlock" style="display: none;color:#FF0000"></div><BR> ' +
                    '<p>请填写您的还位原因：</p><div class="field"><textarea name="inputtext"' +
                    ' id="inputtext"  rows="3" cols="20"></textarea></div>  ' +
                    '</div>'
            $.jBox(mess, {title: "切位归还", buttons: {'提交': 1, '取消': 0}, submit: submit, width: 400, persistent: true});

        }

        var submit = function (v, h, f) {
            if (v == 1) {
                var New = document.getElementsByName("radioid");
                //var tt=f.radioid;
                var find = false;
                var strNew;
                var maxReserveNum;
                var requestAgentId;
                var soldPayPosition;
                var reserveBackAmount = $("#reserveBackAmount").val();
                var inputtext = $("#inputtext").val();
                for (var i = 0; i < New.length; i++) {
                    if (New.item(i).checked) {
                        strNew = New.item(i).getAttribute("value");
                        var strs = new Array(); //定义一数组
                        strs = strNew.split("~"); //字符分割
                        requestAgentId = strs[0];
                        maxReserveNum = strs[1];
                        soldPayPosition = strs[2];
                        find = true;
                        break;
                    }
                }
                if (find == false) {
                    $(".errorBlock").html("请选择切位记录").show();
                    return false;
                }
                var repos = /^[1-9]+[0-9]*]*$/;
                var re = /^[0-9]+[0-9]*]*$/;
                var reserveBackAmount = $("#reserveBackAmount").val();
                if (!reserveBackAmount.match(repos) || parseInt(reserveBackAmount) > (parseInt(maxReserveNum) - parseInt(soldPayPosition)) || parseInt(reserveBackAmount) < 0) {
                    $(".errorBlock").html("人数请输入正整数1到" + (parseInt(maxReserveNum) - parseInt(soldPayPosition)) + "之间的整数").show();
                    return false;
                    /*}else   if(fontMoneyBackAmount==null||fontMoneyBackAmount==''||(!fontMoneyBackAmount.match(re))||
                     parseInt(fontMoneyBackAmount)>parseInt(maxFontMoneyBack) || parseInt(maxFontMoneyBack)<0){
                     //alert("金额请输入整数");
                     $(".errorBlock").html("金额请输入正整数0到"+maxFontMoneyBack+"之间的整数").show();
                     return false; */
                } else {
                    $(".errorBlock").hide();
                    $.ajax({
                        type: "POST",
                        url: "/a/stock/manager/apartGroup/returnReserve?dom=" + Math.random(),
                        data: {
                            activityGroupId: requestActivityGroupId,
                            agentId: requestAgentId,
                            reserveBackAmount: reserveBackAmount,
                            fontMoneyBackAmount: 1, //fontMoneyBackAmount
                            returnRemark: inputtext
                        },
                        success: function (msg) {
                            if (msg == 'fail') {
                                top.$.jBox.tip('归还失败', 'warning');
                                top.$('.jbox-body .jbox-icon').css('top', '55px');
                            } else if (msg == 'success') {
                                top.$.jBox.tip('归还成功', 'warning');
                                top.$('.jbox-body .jbox-icon').css('top', '55px');
                                location.reload();
                            } else {
                                top.$.jBox.tip(msg, 'warning');
                                top.$('.jbox-body .jbox-icon').css('top', '55px');
                            }
                        }
                    });
                    top.$('.jbox-body .jbox-icon').css('top', '55px');
                    return false;
                }

            }
        }


        function confirmReserve() {
            var New = document.getElementsByName("radioid");
            var find = false;
            var strNew;
            var maxReserveNum;
            var requestAgentId;
            var soldPayPosition;
            var reserveBackAmount = $("#reserveBackAmount").val();
            var inputtext = $("#inputtext").val();
            for (var i = 0; i < New.length; i++) {
                if (New.item(i).checked) {
                    strNew = New.item(i).getAttribute("value");
                    var strs = new Array(); //定义一数组
                    strs = strNew.split("-"); //字符分割
                    requestAgentId = strs[0];
                    maxReserveNum = strs[1];
                    soldPayPosition = strs[2];
                    find = true;
                    break;
                }
            }
            if (find == false) {
                $(".errorBlock").html("请选择切位记录").show();
                return;
            }
            var repos = /^[1-9]+[0-9]*]*$/;
            var re = /^[0-9]+[0-9]*]*$/;
            var reserveBackAmount = $("#reserveBackAmount").val();

            if (!reserveBackAmount.match(repos) || parseInt(reserveBackAmount) > (parseInt(maxReserveNum) - parseInt(soldPayPosition)) || parseInt(reserveBackAmount) < 0) {
                $(".errorBlock").html("人数请输入正整数1到" + (parseInt(maxReserveNum) - parseInt(soldPayPosition)) + "之间的整数").show();
                return false;
                /*}else   if(fontMoneyBackAmount==null||fontMoneyBackAmount==''||(!fontMoneyBackAmount.match(re))||
                 parseInt(fontMoneyBackAmount)>parseInt(maxFontMoneyBack) || parseInt(maxFontMoneyBack)<0){
                 //alert("金额请输入整数");
                 $(".errorBlock").html("金额请输入正整数0到"+maxFontMoneyBack+"之间的整数").show();
                 return false; */
            } else {
                $(".errorBlock").hide();
                $.ajax({
                    type: "POST",
                    url: "/a/stock/manager/apartGroup/returnReserve?dom=" + Math.random(),
                    data: {
                        activityGroupId: requestActivityGroupId,
                        agentId: requestAgentId,
                        reserveBackAmount: reserveBackAmount,
                        fontMoneyBackAmount: 1, //fontMoneyBackAmount
                        inputText: inputtext
                    },
                    success: function (msg) {
                        if (msg == 'fail') {
                            top.$.jBox.tip('归还失败', 'warning');
                            top.$('.jbox-body .jbox-icon').css('top', '55px');
                        } else if (msg == 'success') {
                            top.$.jBox.tip('归还成功', 'warning');
                            top.$('.jbox-body .jbox-icon').css('top', '55px');
                            location.reload();
                        } else {
                            top.$.jBox.tip(msg, 'warning');
                            top.$('.jbox-body .jbox-icon').css('top', '55px');
                        }
                    }
                });
                top.$('.jbox-body .jbox-icon').css('top', '55px');
                return false;
            }
        }


        function agentIdChange() {
            requestAgentId = $("#agentId").val();
            for (var i = 0; i < activityGroupList.length; i++) {
                if (activityGroupList[i].agentId == requestAgentId) {
                    $("#maxReserveNum").html(activityGroupList[i].leftpayReservePosition);
                    maxReserveNum = activityGroupList[i].leftpayReservePosition;
                    $("#maxFontMoneyBack").html(activityGroupList[i].leftFontMoney);
                    maxFontMoneyBack = activityGroupList[i].leftFontMoney;
                }
            }
        }


        function getDepartment(departmentId) {
            $("#departmentId").val(departmentId);
            $("#searchForm").submit();
        }

        function refundAmount(orderNum) {
            alert(orderNum);
        }

        function formReset() {
            $(':input', '#searchForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $('#targetAreaId').val('');
        }

    </script>
    <script type="text/javascript">

        $(document).ready(function () {
        	hideCopyArea();
            $('#contentTable').on('change', 'input[type="checkbox"]', function () {
                if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
                    $('[name="allChk" ]').attr('checked', true);
                } else {
                    $('[name="allChk" ]').removeAttr('checked');
                }
            });

           /*  $(".closeOrExpand").click(function () {
                if ($(this).is(".ydClose")) {
                    $(this).parents('.ydbz_tit:first').next('.toggle:first').show();
                    $(this).addClass("ydExpand");
                    $(this).removeClass("ydClose");
                } else {
                    $(this).parents('.ydbz_tit:first').next('.toggle:first').hide();
                    $(this).addClass("ydClose");
                    $(this).removeClass("ydExpand");
                }
            }); */

            $('[name="back"]').on('click',function(){
                window.location.href="${ctx}/stock/manager/airticket/?agentId";
            });

            $('[name="batchDelete"]').on('click',function(){
                var $selected = $('#contentTable tbody tr input[type="checkbox"]:checked');
                var airticketactivityreserveTemps=getUuids($(this));
                var delBatchData=JSON.stringify(airticketactivityreserveTemps);
                if(!$selected.length) {
                    $.jBox.tip('您未选择团期，请选择！');
                    return;
                }
                $.jBox.confirm('确定删除所有选择的数据吗？','系统提示',function(v,h,f){
                    if(v==='ok'){
                        $selected.each(function(){
                            if($(this).attr('name')=='checkAll'){
                                return;
                            }
                             $.ajax({
				                	type:"POST",
				                	url:"${ctx}/airticketactivityreserveTemp/delete",
				                	data:{
				                		"delBatchData":delBatchData
				                	},
				                	success:function(result){
				                		if(result=="0"||result=="1"|| result=="3"){
				                			$.jBox.tip('系统发生异常,请重新操作！','error');
				                			return;
				                		}
				                		if(result=='2'){
				                			$.jBox.tip('删除成功！','success');
				                				location.href="${ctx}/airticketactivityreserveTemp/list?status=0&showType=2";
				                		}
				                	}
				             });
				            //  $(this).parent().parent().remove();
                        });
                    }
                });
            });

            $('[name="confirm"]').on('click',function(){
                var isValid=true;
                var $selected = $('#contentTable tbody tr input[type="checkbox"]:checked');
                if(!$selected.length) {
                    $.jBox.tip('您未选择团期，请选择！');
                    return;
                }
                debugger;
                $selected.each(function(){
                    var $tds = $(this).parents('tr:first').find('td');
                    var surplusCount = (+$tds.eq(7).text());
                    var cutCount = (+$tds.eq(6).text());
                    if(cutCount>surplusCount){
                        isValid = false;
                    }
                });
                if(!isValid){
                    $.jBox.tip('产品团期过期或者余位不足，请查看','error');
                    return false;
                }
                
                var reserveTemps = [];
                $("#contentTable tbody").find("tr.grouprow").each(function(){
                	if($(this).find("input[name=groupId]").attr("checked") == "checked") {
                		reserveTemps.push($(this).find("input[name=groupUuid]").val());
                	}
                });
                
                $.ajax({
            		type: "POST",
            	   	url: "${ctx}/airticketactivityreserveTemp/confirmReverse",
            	   	async: false,
            	   	data: {
            				"reserveTempJson":reserveTemps.join(",")
            			  },
            		dataType: "json",
            	   	success: function(data){
            	   		if(data.result=="1"){
            	   			$.jBox.tip(data.message, 'success', { closed: function () { location.reload(); } });
            	   		} else if(data.result=="2") {
            	   			$.jBox.tip(data.message, 'fail');
            	   		} else if(data.result=="3") {
            	   			$.jBox.tip(data.message, 'error');
            	   		}
            	   	}
            	});
                
            })

            $('[name="modify"]').on('click', function () {
                var $selected = $('#contentTable tbody tr input[type="checkbox"]:checked');
                if(!$selected.length) {
                    $.jBox.tip('您未选择团期，请选择！');
                    return;
                }

                $selected.each(function(){
                    var $grouprow = $(this).parents('.grouprow:first');
                    var $edits = $grouprow.find('.edit-input');
                    $edits.each(function(){
                        if($(this).is('input')){
                            $(this).css("display","block").val($(this).prev().css("display","none").text());
                        }else{
                            var optionText = $(this).prev().css("display","none").text();
                            $(this).css('display','block').find('option').each(function(){
                                if($(this).text()==optionText){
                                    $(this).attr('selected','true');
                                }
                            });
                        }
                    });
                });
                $(this).parent().hide();
                showCopyArea();
                lockOperate();
                $('[name="save"]').parent().show();
            });
        });
        var sy=false;
         var scan=false;
        $(document).on('click', '[name="save"]',function () {
        	   dute();
                if(scan){
                	$.jBox.tip('切位人数格式不正确！',"error");
                	return false;
                }
                trub();
                if(sy){
                	$.jBox.tip('订金格式不正确！',"error");
                	return false;
                }
            var $selected = $('#contentTable tbody tr input[type="checkbox"]:checked');
            $selected.each(function(){
                var $grouprow = $(this).parents('.grouprow:first');
                var $edits = $grouprow.find('.edit-input');
                var airticketactivityreserveTemps = getSelectedRows($(this));
                 var updateData=JSON.stringify(airticketactivityreserveTemps);
                $edits.each(function(){
                    if($(this).is('input')){
                        $(this).prev().show().text($(this).hide().val());
                    }else{
                        if($(this).hide().find('option:selected').text()=="请选择"){
                    		$(this).prev().show().text("");
                    	}else{
                    		$(this).prev().show().text($(this).hide().find('option:selected').text());
                    	}
                    }
                });
                  $.ajax({
	            	type:"post",
	            	url:"${ctx}/airticketactivityreserveTemp/update",
	            	data:{
	            		"updateData":updateData
	            	},
	            	success:function(result){
	            		if(result==2){
	            			$.jBox.tip('修改成功！');
	            		}else{
	            		}
	            	}
           	 	});
            });
            $(this).parent().hide();
            hideCopyArea();
            unLockOperate();
            $('[name="modify"]').parent().show();
        });
        $(document).on('click','[name="cancel"]',function(){
            var $selected = $('#contentTable tbody tr input[type="checkbox"]:checked');
            $selected.each(function(){
                var $grouprow = $(this).parents('.grouprow:first');
                var $edits = $grouprow.find('.edit-input');
                $edits.each(function(){
                    $(this).hide().prev().show();
                });
            });
            $(this).parent().hide();
            hideCopyArea();
            unLockOperate();
            $('[name="modify"]').parent().show();
        });
          //复选框与批量删除置为不可操作
        function lockOperate(){
            $('input[type="checkbox"]').attr('disabled','disabled');
            $('button[name="batchDelete"]').attr('disabled','disabled');

        }
         //复选框与批量删除置为可操作
        function unLockOperate(){
            $('input[type="checkbox"]').removeAttr('disabled');
            $('button[name="batchDelete"]').removeAttr('disabled');
        }
          //修改时可进行复制操作
			function showCopyArea(){
			    $('#contentTable thead th span:hidden').show();
			    $('#contentTable thead th select:hidden').show();
			}
			function hideCopyArea(){
			    $('#contentTable thead th span').hide();
			    $('#contentTable thead th select:hidden').hide();
			}
			//复制方法
			$(document).on('click', '.visa_copy', function () {
			    var $th = $(this).parents('th:first');
			    var $tr = $(this).parents('tr:first');
			    var name = $(this).attr('name');
			    var value = $(this).siblings('input,select').val()
			    $(this).parents('table:first').find('tbody tr').each(function () {
			        $(this).find('input[name="' + name + '"],select[name="' + name + '"]:visible').val(value);
			    });
			});
		 function getSelectedRows(obj) {
                    var  airticketactivityreserveTemps = [];
                    obj.parents('.toggle:first').find('#contentTable tbody tr input[type="checkbox"]:checked').each(function () {
                        if ($(this).attr('name') == 'groupId') {
                            var $row = $(this).parent().parent();
                            if ($row.attr('name') != "grouprow") {
                                var $tds = $row.find('td');
                                var  airticketactivityreserveTemp = {};
                                var cIndex = 0;
                                //产品Id
                                 airticketactivityreserveTemp.id = $tds.eq(cIndex).children().eq(0).val();
                                //产品名称
                                cIndex++;
                              
                                //出团日期
                                cIndex++;
                                
                                //渠道
                               cIndex++;
                               
                                 //跟进人
                               cIndex++;
                               
                                //订金
                                cIndex++;
                                 airticketactivityreserveTemp.frontMoney = $tds.eq(cIndex).children().eq(1).val();
                                //切位人数
                                cIndex++;
                                 airticketactivityreserveTemp.payReservePosition = $tds.eq(cIndex).children().eq(1).val();
                                //余位
                                cIndex++;
                               
                                //预定人
                                cIndex++;
                                 airticketactivityreserveTemp.reservation =  $tds.eq(cIndex).children().eq(1).val();
                                //成人
                                cIndex++;
                         
                                //儿童
                                cIndex++;
                               
                                //特殊人群
                                cIndex++;
                               
                               //预收
                                cIndex++;
                              
                                //支付时间
                                cIndex++;
                               
                                //支付方式
                                cIndex++;
                                 airticketactivityreserveTemp.payType =  $tds.eq(cIndex).children().eq(1).val();
                                //支付凭证
                                cIndex++;
                                //备注
                                cIndex++;
                                airticketactivityreserveTemp.remark =  $tds.eq(cIndex).children().eq(1).val();
                                airticketactivityreserveTemps.push( airticketactivityreserveTemp);
                              
                            }
                        }
                    });
                    
                    return  airticketactivityreserveTemps;
                }
		
				 function getUuids(obj) {
                    var  airticketactivityreserveTemps = [];
                    obj.parents('.toggle:first').find('#contentTable tbody tr input[type="checkbox"]:checked').each(function () {
                        if ($(this).attr('name') == 'groupId') {
                            var $row = $(this).parent().parent();
                            if ($row.attr('name') != "grouprow") {
                                var $tds = $row.find('td');
                                var  airticketactivityreserveTemp = {};
                                var cIndex = 0;
                                 //产品Id
                                 airticketactivityreserveTemp.id = $tds.eq(cIndex).children().eq(0).val();
                                //产品uuid
                                 airticketactivityreserveTemp.uuid = $tds.eq(cIndex).children().eq(1).val();
                                 airticketactivityreserveTemps.push( airticketactivityreserveTemp);
                            }
                         }        
                     });
                      return  airticketactivityreserveTemps;
                  }             
        function checkall(obj) {
            if ($(obj).attr("checked")) {
                $('#contentTable input[type="checkbox"]').attr("checked", 'true');
                $("input[name='allChk']").attr("checked", 'true');
            } else {
                $('#contentTable input[type="checkbox"]').removeAttr("checked");
                $("input[name='allChk']").removeAttr("checked");
            }
        }
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/airticketactivityreserveTemp/list?status=0&showType=2");
			$("#searchForm").submit();
	    }
        function checkreverse(obj) {
            var $contentTable = $('#contentTable');
            $contentTable.find('input[type="checkbox"]').each(function () {
                var $checkbox = $(this);
                if ($checkbox.is(':checked')) {
                    $checkbox.removeAttr('checked');
                } else {
                    $checkbox.attr('checked', true);
                }
            });
        }
      //用正则验证切位人数格式
     function dute(){	
     	$('#contentTable tbody tr input[type="checkbox"]:checked').each(function () {
     		  if ($(this).attr('name') == 'groupId') {
					var reg=/^\d{0,9}$/     		  		
     		  		var payReservePosition=$(this).parent().next().next().next().next().next().next().children().eq(1).val();
     		  		//var freePosition=$(this).parent().next().next().next().next().next().next().next().text();
     		  		if(!payReservePosition.match(reg)){
     		  			scan=true;
     		  		}
     		  		else{
     		  			scan=false;
     		  		}
     		  }
     	});
     	
     }
     //正则验证订金格式
      function trub(){
     			$('#contentTable tbody tr input[type="checkbox"]:checked').each(function () {
     				  if ($(this).attr('name') == 'groupId') {
     				  		var frontMoney=$(this).parent().next().next().next().next().next().children().eq(1).val();
     				  		var str=/^\d{1,8}\.{0,1}\d{0,2}$/
     				  		if(!frontMoney.match(str)){
     				  			sy=true; 
     				  		}else{
     				  			sy=false;
     				  			if(frontMoney.match(/^\d{0,8}$/)){
     				  				$(this).parent().next().next().next().next().next().children().eq(1).val(frontMoney+".00");
     				  			}
     				  		}
     				  }
     			});
     	}
     		//格式化小数，保留两位
		function checkValue(obj){
		    var money = obj.value;
		    if(money){
		        if(money >= 0){
		            var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		            var txt = ms.split(".");
		            obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
		        }else{
		            obj.value = '0';
		        }
		    }
		}
      	function showPayOrderWin(obj,hasPermission,uuid,createBy){
      			$.ajax({
				type:"POST",
				url:"${ctx}/airticketactivityreserveTemp/query",
				asyn:true,
				data:{
					"uuid":uuid
				},
				success:function(result){
					var list=JSON.parse(result);
					if(list.length>0){
						$("#download").children().remove();
						$.each(list,function(index,doc){
							var li=$("<li></li>").append('<label title="'+doc.fileName+'">'+doc.fileName+'</label>')
														  .append('<em class="ico-download" title="下载" onclick="downloads('+doc.srcDocId +')"></em>')
														  .append('<input type="hidden" name="docId"  value="'+doc.srcDocId+'" />');
							$("#download").append(li);
						});
					} else {
						$("#download").find("li").remove();
					}
					$.jBox($(obj).parents('body').find('[name="downloadWin"]').html(),{
				        title:'凭证管理',
				        width:500,
				        height:400,
				        buttons:{'关闭':true},
				       /*  closed: function () { 
				        	location.reload();
				        },
				     	submit:function(v, h, f){
							if(v){
								location.reload();
							}
						},  */
				        loaded:function(h){
			          		  //消除滚动条
				            $(".jbox-content", top.document).css("overflow-y","auto");
					            if(hasPermission){
					                //var $content = ;
					                if(createBy=='${user.id}'){
						                h.find('li').append('<em class="ico-del" title="删除" onclick="deleteAttachment(this,\''+uuid+'\')"></em>');
						                h.find('ul').append('<input class="btn btn-primary" value="上传支付凭证" style="margin:10px 0px" onclick="uploadFiles(\'${ctx}\',\'\',this,\'false\',\''+uuid+'\')">'); 
					            	}
					            }
				      }
				  });	
				}
			});
		    
		}
		function deleteAttachment(obj,uuid){
		    $.jBox.confirm('确定删除该文件么','系统提示', function (v, h, f){
		        if(v=='ok'){
		            $(obj).parent().remove();
		            var docId=$(obj).parent().find('input[name="docId"]').first().val();
		            $.ajax({
		            	type:"POST",
		            	url:"${ctx}/airticketactivityreserveTemp/deleteFile",
		            	data:{
		            		"docId":docId,
		            		"uuid":uuid
		            	},
		            	success:function(result){}
		            }); 
		        }
		    });
		}
		
		
		function uploadFiles(ctx, inputId, obj, isSimple,uuid) {
		var fls=flashChecker();
		var s="";
		if(fls.f) {
//			alert("您安装了flash,当前flash版本为: "+fls.v+".x");
		} else {
			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
			return;
		}
		
		//新建一个隐藏的div，用来保存文件上传后返回的数据
		if($(obj).parent().find(".uploadPath").length == 0)
			$(obj).parent().append('<div class="uploadPath" style="display: none" id="uploadPathDiv"></div>');
		
		$(obj).addClass("clickBtn");
		
		//默认为多文件上传
		if(isSimple == null) {
			isSimple = "false";
		}
		
		$.jBox("iframe:"+ ctx +"/MulUploadFile/uploadFilesPage?isSimple=" + isSimple, {
		//$.jBox("iframe:"+ ctx, {
		    title: "文件上传",
			width: 340,
	   		height: 365,
	   		buttons: {'完成上传':true},
	   		persistent:true,
	   		loaded: function (h) {},
	   		submit: function (v, h, f) {
				$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
				//这里拼接本次上传文件的原名称
				var fileIDList = "";
				var fileNameList = "";
				var filePathList = "";
					//
				if($(obj).parent().find("[name='docID']").length != 0) {
					$(obj).parent().find("[name='docID']").each(function(index, obj) {
						if(null != isSimple && "false"!=isSimple) {
							fileIDList = $(obj).val();
						}else{
							fileIDList += $(obj).val() + ";";
						}
					});
				}
				if($(obj).parent().find("[name='docOriName']").length != 0) {
					$(obj).parent().find("[name='docOriName']").each(function(index, obj) {
						if(null != isSimple && "false"!=isSimple) {
							fileNameList = $(obj).val();
						}else{
							fileNameList += $(obj).val() + ";";
						}
					});
				}
				if($(obj).parent().find("[name='docPath']").length != 0) {
					$(obj).parent().find("[name='docPath']").each(function(index, obj) {
						if(null != isSimple && "false"!=isSimple) {
							filePathList = $(obj).val();
						}else{
							filePathList += $(obj).val() + ";";
						}
					});
				}
				//在这里将原名称写入到指定id的input中
				//if(inputId)
				//	$("#" + inputId).val(fileNameList);
				//该函数各自业务jsp都写一个，里面的内容根据自身页面要求自我实现
				commenFunction(obj,fileIDList,fileNameList,filePathList,uuid);
				$("#uploadPathDiv").remove();
				$(".clickBtn",window.parent.document).removeClass("clickBtn");
				fileNameList = "";
	   		}
		});
		$(".jbox-close").hide();
		$(".jbox-content").css("overflow-y","auto");
	}
	function flashChecker()
	{
		var hasFlash=0;        //是否安装了flash
		var flashVersion=0;    //flash版本
		
		if(document.all){
			var swf ;
			try{
			   swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
			} catch (e) {
				hasFlash=1;
				alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
			}
			if(swf) { 
				hasFlash=1;
				VSwf=swf.GetVariable("$version");
				flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]); 
			}
		}else{
			if (navigator.plugins && navigator.plugins.length > 0){
				var swf=navigator.plugins["Shockwave Flash"];
				if (swf)  {
				   hasFlash=1;
			       var words = swf.description.split(" ");
			       for (var i = 0; i < words.length; ++i){
			         if (isNaN(parseInt(words[i]))) continue;
			         flashVersion = parseInt(words[i]);
				   }
				}
			}
		}
		return {f:hasFlash,v:flashVersion};
	}
	 function commenFunction(obj,fileIDList,fileNameList,filePathList,uuid) {
	 		var fileIdArr = fileIDList.split(";");
	 		var fileNameArr = fileNameList.split(";");
	 		var filePathArr = filePathList.split(";");
	 		for(var i=0; i<fileIdArr.length-1; i++) {
					//<span><a href="javascript:void(0);" title="点击下载附件">0001.jpg</a></span>
	    			var html = [];
	    			html.push('<li>');
	    			html.push('<label>'+fileNameArr[i]+'</label>');
	    			html.push('<em class="ico-download" title="下载" onclick="downloads('+ fileIdArr[i] +')"></em>');
	    			html.push('<em class="ico-del" title="删除" onclick="deleteAttachment(this,\''+uuid+'\')"></em>')
	    			html.push('<input type="hidden" name="docId"  value="' + fileIdArr[i] + '" />');
	    			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
	    			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
	    			html.push('</li>');
	    			if($(obj).parent().find("li").length>0){
	    				$(obj).parent().find("li").first().before(html.join(''));
	    			}else{
	    				$(obj).before(html.join(''));
	    			}
	 			
	 		}
			$.ajax({
				type:"POST",
				url:"${ctx}/airticketactivityreserveTemp/saveFile",
				data:{
					"uuid":uuid,
					"docIds":fileIDList
				},
				success:function(result){}
			});	 		 
	     }
    </script>
</head>
<body>
	<page:applyDecorator name="stock_op_head">
    	<page:param name="current">flightStock</page:param>
    </page:applyDecorator>
	<script type="text/javascript">
                        $(function () {
                            var activeMark = 'flightStock';
                            $('#' + activeMark).addClass('active');
                        });
						   
                        function jump(href) {
                            var _m = '147';
                            var _mc = '243';
                            href = appendParam(href, {_m: _m, _mc: _mc});
                            window.location.href = href;
                        }
                    </script>

                    <div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">

                        <!--   <div class="activitylist_bodyer_right_team_co_bgcolor">-->
                        <form id="searchForm" action="${ctx }/airticketactivityreserveTemp/list?status=0&showType=2" method="post">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
							<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                            <div class="activitylist_bodyer_right_team_co">

                                <div class="activitylist_bodyer_right_team_co2 pr wpr20">
                                    <label>搜索：</label>
                                    <input style="width:260px;" class="txtPro inputTxt inquiry_left_text"
                                           id="wholeSalerKey" name="productCode" value="${productCode }"/>
                                    <span class="ipt-tips" style="display: block;">输入产品编号，支持模糊匹配</span>
                                </div>
                                <div class="form_submit">
                                    <input class="btn btn-primary" type="submit" value="搜索">
                                    &nbsp;
                                    <input class="btn btn-primary" type="button" value="重置查询" onclick="formReset();">
                                </div>
                                <div class="zksx">筛选</div>

                                <div class="ydxbd">
                                    <div class="activitylist_bodyer_right_team_co1">
                                       <div class="activitylist_team_co3_text">团号：</div>
                                        <input value="" type="text" name="groupCode" value="${groupCode }">
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co3">
                                        <div class="activitylist_team_co3_text">渠道：</div>
                                        <select name="agentName">
                                            <option value="">不限</option>
											<c:forEach items="${agentinfoList}" var="agentInfo">
							                      <option value="${agentInfo.id}" ${agentInfo.id==agentName?"selected":''}>${agentInfo.agentName}</option>
							                  </c:forEach>
                                        </select>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co2">
                                        <label>出发日期：</label>
                                        <input id="groupOpenDate" class="inputTxt dateinput" name="startingDateFront"
                                               value='<f:formatDate value="${startingDateFront }" pattern="yyyy-MM-dd"/>' style="margin-left: -3px; width: 122px;"
                                               onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                                               readonly/>
                                        <span style="font-size:12px; font-family:'宋体';"> 至</span>
                                        <input id="groupCloseDate" class="inputTxt dateinput" name="startingDateAfter"
                                               value='<f:formatDate value="${startingDateAfter }" pattern="yyyy-MM-dd"/>' style="width: 122px;" onClick="WdatePicker()" readonly/>
                                    </div>

                                </div>
                            </div>
                        </form>

                        <div id="secondStepTitle" style="margin:10px 0 0 0%;" class=" ydbz_tit">
                            <span><span class="ydExpand closeOrExpand"></span>切位草稿箱</span>
                        </div>
                        <div class="toggle"  style="margin-top: 10px" >
                            <form id="groupform" name="groupform" action="" method="post">

                                <table id="contentTable" class="table table-striped table-bordered table-condensed table-mod2-group">
                                    <thead>
                                        <tr>
                                            <th width="8%" rowspan="2"><input name="checkAll" value="" type="checkbox" style="width:auto">团号</th>
                                            <th width="4%" rowspan="2">产品编号</th>
                                            <th width="4%" rowspan="2">出发日期</th>
                                            <th width="7%" rowspan="2">渠道</th>
                                            <th width="4%" rowspan="2">跟进人</th>
                                            <th width="4%" rowspan="2">订金
		                                         <span class="tc display-none">
		                                         <input name="reservation2" id="reservation2" maxlength="10" type="text"
		                                                       onkeyup="checkValue(this)"
		                                                       onafterpaste="checkValue(this)"
		                                                       onblur="checkValue(this)">
		                                         <input value="复制" name="deposit" class="visa_copy"
		                                                       type="button">
		                                         </span>
		                                   </th>
                                           <th width="4%" rowspan="2">
                                                <span class="xing">*</span>
                                                切位人数
                                                <span class="tc display-none">
                                                    <input name="reservation3" id="reservation3" maxlength="10"
                                                       data-type="number" type="text"><br>
                                                    <input value="复制" name="cutCount" class="visa_copy" type="button">
                                                </span>
                                            </th>
                                           <th width="4%" rowspan="2">余位</th>
                                           <th width="4%" rowspan="2">预订人
                                                <span class="tc display-none">
                                                    <input name="reservation4" id="reservation4" type="text">
                                                    <br>
                                                    <input value="复制" class="visa_copy" name="reservationer" type="button">
                                                </span>
                                            </th>
                                            <th width="12%" colspan="3" class="t-th2">同行价</th>
                                            <th width="6%" rowspan="2">预收</th>
                                            <th width="6%" rowspan="2">支付时间</th>
                                            <th width="4%" rowspan="2">支付方式
                                                <span class="tc display-none">
                                                    <select name="payMethod" class="edit-input" style="width: 90%" >
														 <option value="">请选择</option>
					                                     <c:forEach items="${list}" var="y">
					                                            	<option value="${y.value }" >${y.label }</option>
					                                      </c:forEach>
												</select>
                                                    <br>
                                                    <input value="复制" class="visa_copy" name="payMethod" type="button">
                                                </span>
                                            </th>
                                           <th width="4%" rowspan="2">支付凭证</th>
                                            <th width="6%" rowspan="2">备注
                                                <span class="tc display-none">
                                                    <input name="reservation5" id="reservation5" type="text">
                                                    <br>
                                                    <input value="复制" class="visa_copy" name="memo" type="button">
                                                </span>
                                            </th>
                                        </tr>
                                        <tr>
                                            <th width="4%">成人</th>
                                            <th width="4%">儿童</th>
                                            <th width="4%">特殊人群</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<c:forEach items="${page.list }" var="p">
                                        <tr class="grouprow">
                                            <!--<td ></td>-->
                                            <td class="tc">
                                                <input name="groupId" type="checkbox" value="${p.id }" style="width:auto">${p.groupCode }
                                                <input name="groupUuid" type="hidden" value="${p.uuid }">
                                            </td>
                                            <td class="tc">${p.productCode }</td>
                                            <td class="tc">
                                               ${p.startingDate }
                                            </td>
                                            <td class="tc">${p.agentName }</td>
                                            <td class="tc">${p.name }</td>
                                            <td class="tc">
                                                <span>${p.frontMoney }</span>
                                                <input name="deposit" maxlength="10" onkeyup=" value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')" type="text" class="edit-input" onblur="trub()">
                                            </td>
                                            <td class="tc">
                                                <span>${p.payReservePosition }</span>
                                                <input class="valid edit-input" name="cutCount" onblur="dute()" onkeyup=" value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')" value="" maxlength="10" type="text" >
                                            </td>
                                            <td class="tc" >${p.freePosition }</td>
                                            <td class="tc">
                                                <span>${p.reservation }</span>
                                                <input name="reservationer" type="text" class="edit-input" maxlength="10">
                                            </td>
                                            <td class="tc">
                                            	<%-- <c:if test="${p.settlementAdultPrice!=null }">
                                            		￥
                                            	</c:if> --%>
                                            	${p.mark }
                                            	${p.settlementAdultPrice }
                                            </td>

                                            <td class="tc">
                                           <%--  <c:if test="${p.settlementAdultPrice!=null }">
                                            		￥
                                            </c:if> --%>
                                            ${p.mark}
                                            ${p.settlementcChildPrice }</td>
                                            <td class="tc">
                                            <%-- <c:if test="${p.settlementAdultPrice!=null }">
                                            		￥
                                            	</c:if> --%>
                                            ${p.mark}
                                            ${p.settlementSpecialPrice }</td>
                                            <td class="tc">${p.reservationsNum }</td>
                                            <td class="tc">
                                                ${p.createDate }
                                            </td>
                                            <td class="tc">
                                                <span>
                                                <c:forEach items="${list}" var="y">
                                                	<c:if test="${y.value==p.payType }">
                                                		${y.label }
                                                	</c:if>
                                                </c:forEach>
                                                </span>
                                                <select name="payMethod" class="edit-input" style="width: 90%">
                                                    <option value="">请选择</option>
                                                    <c:forEach items="${list}" var="y">
                                                    	<c:if test="${p.payType==y.value}">
                                                   		 <option value="${y.value }" selected>${y.label }</option>
                                                   		</c:if>
                                                   		<c:if test="${p.payType!=y.value }">
                                                   			 <option value="${y.value }" >${y.label }</option>
                                                   		</c:if>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                             <td class="tc"><a onclick="showPayOrderWin(this,true,'${p.uuid}','${p.createBy }')">凭证管理</a></td>
                                            <td class="tl">
                                                <span>${p.remark }</span>
                                                <input name="memo" maxlength="200" type="text" class="edit-input">
                                            </td>
                                        </tr>
                                     </c:forEach>
                                     	<tr>
                                            <td colspan="17">
                                                <button class="btn btn-default" style="height: 26px;" type="button" name="batchDelete">
                                                    批量删除
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>


                            </form>
                            <div class="page">
                                <div class="pagination">
                                    <div class="endPage">
                                        ${page }
                                    </div>
                                </div>
                                <div class="release_next_add">
                                    <input class="btn btn-primary" type="button" name="modify" value="修改"/>
                                    &nbsp;
                                    <input class="btn btn-primary" type="button" name="confirm"  value="确认切位"/>
                                    <input class="btn btn-primary" type="button"  value="返回" name="back"/>
                                </div>
                                <div class="release_next_add" >
                                    <input class="btn btn-primary" type="button" name="save" value="保存"/>
                                     <input class="btn btn-primary" type="button" name="cancel" value="取消"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <br/>

                    <script type="text/javascript">
                        $(document).ready(function () {
                            //目的地去掉最后的逗号
                            var total = $("#pageSize").val();
                            for (var i = 1; i <= total; i++) {
                                if ($("#area" + i).length > 0) {
                                    var str = $("#area" + i).html();
                                    $("#area" + i).html(str.substring(0, str.length - 2));
                                }
                            }
                        });
                    </script>
              		 <!-- 下载浮框开始-->
    <div name="downloadWin" class="display-none" >
        <div class="pay_record_new">
            <ul id="download">
            </ul>
        </div>
    </div>
    <!--下载浮框结束-->                  
</body>
</html>
	