<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>批量切位具体操作页面</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />

	<script src="${ctxStatic}/js/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery-migrate-1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <style type="text/css">
            .cl-tt-btn {
                display: inline-block;
                background-color: #e5f5ff;
                border: 1px solid #afd5ff;
                box-shadow: none;
                border-radius: 2px;
                color: #00a2ff;
                margin-left: 3px;
                height: 20px;
                min-width: 70px;
                padding: 0px;
            }

            .ydBtn {
                PADDING-BOTTOM: 10px;
                MARGIN-TOP: 50px;
                WIDTH: 278px;
                HEIGHT: 28px;
                MARGIN-LEFT: auto;
                MARGIN-RIGHT: auto
            }

            tr.error,
            tr.error input,
            tr.error select {
                color: #ff3434;
            }
        </style>
        <style type="text/css">
            .draftbox-disable {
                background-color: #cccccc;
            }

            .ydtExpand {
                background-position: 0px 0px;
                display: block;
                width: 12px;
                height: 12px;
                position: absolute;
                top: 11px;
                left: 12px;
                cursor: pointer;
                background-image: url("${ctxStatic}/images/yd-close-target.png");
            }

            .ydtClose {
                background-position: 0px -18px;
                display: block;
                width: 12px;
                height: 12px;
                position: absolute;
                top: 11px;
                left: 12px;
                cursor: pointer;
                background-image: url("${ctxStatic}/images/yd-close-target.png");
            }

            .pop-channel-container {
                margin-top: 20px;;
            }

            .pop-channel-container .header label {
                width: 100px;
                text-align: right;
            }

            .pop-channel-container .selected-channel {
                padding: 10px;
            }

            .pop-channel-container .selected-channel .channel-text {
                display: inline-block;
                padding: 0 3px;
                margin-right: 6px;
                margin-bottom: 6px;
                line-height: 25px;
                border: 1px solid #DDDDDD;
                background-color: #ededed;
            }

            .pop-channel-container .selected-channel .channel-remove {
                margin-left: 10px;
                cursor: pointer;
            }

            .ui-autocomplete.ui-menu.ui-front {
                z-index: 10000;
            }

            .file-upload {
                width: 55px;
                height: 25px;
                border: 1px solid #D9D9D9;
                color: #403938;
                font-size: 12px;
                background: transparent -moz-linear-gradient(center top, #FFF, #F9F9F9) repeat scroll 0% 0%;
            }
        </style>
        <script type="text/javascript">
            $(function () {
                //搜索条件筛选
//            launch();
                //操作浮框
                operateHandler();
                //展开收起渠道
                closeOrExpand()
                //添加上传支付凭证
                appendFileUpload();
                //存入草稿按钮状态控制
                saveInDraftboxState();
                //附件删除
                inquiryCheckBOXLocal();
            })
        </script>
        <script type="text/javascript">


            function saveInDraftboxState() {
                $('[name="saveInDraftbox"]').attr('disabled', 'true').css('background-color', '#cccccc');
                $(document).on('change', 'input[type="checkbox"]', function () {
                    var checkboxLength = $('input[type="checkbox"][name="groupCheck"]:checked').length;
                    $('input[type="checkbox"][name="groupCheck"]:checked').each(function(){
                        var isHidden = $(this).parents("[name='reserveInfoTr']").is(":hidden");
                        if(isHidden) {
                            checkboxLength = checkboxLength - 1;
                        }
                    });
                    if (checkboxLength > 0) {
                        $('[name="saveInDraftbox"]').removeAttr('disabled').css('background-color', '#5f7795');
                    } else {
                        $('[name="saveInDraftbox"]').attr('disabled', 'false').css('background-color', '#cccccc');
                    }
                    
                    $('div.channel-cut').each(function(){
                    	var checkLength = $(this).find("tr[name=product] input[name='groupCheck']:checked").length;
                    	var allLength = $(this).find("tr[name=product] input[name='groupCheck']").length;
                    	if((allLength != 0) && checkLength == allLength){
							$(this).find("input[name=checkAll]").attr("checked",true);
                    	} else {
							$(this).find("input[name=checkAll]").attr("checked",false);
                    	}
                    });
                    
                })
            }

            $(document).on('change', '[name="checkAll"]', function () {
                if ($(this).attr('checked')) {
                    $(this).parents('.toggle').find('input[type="checkbox"]').attr('checked', true);
                } else {
                    $(this).parents('.toggle').find('input[type="checkbox"]').attr('checked', false);
                }
            });

            function closeOrExpand() {
                $(document).on('click', '.closeOrExpand', function () {
                    if ($(this).is(".ydtClose")) {
                        $(this).parents('.channel-cut').find('.toggle:first').show();
                        $(this).addClass("ydtExpand");
                        $(this).removeClass("ydtClose");
                    } else {
                        $(this).parents('.channel-cut').find('.toggle:first').hide();
                        $(this).addClass("ydtClose");
                        $(this).removeClass("ydtExpand");
                    }
                })
            }
            function appendFileUpload() {
                $(document).on('click', '[name="onFileUpload"]', function () {
                    var $toogle = $(this).parent().parent();
                    var $filediv = $toogle.find("#filediv");
                    var $hiddendiv = $toogle.find("#hiddendiv");
                    if ($filediv.find('p')) {
                        $filediv.find('p').remove();
                        $filediv.append($hiddendiv.html());
                    }
                });
            }

            function inFileName(obj) {
                var res = $(obj).val();
                var dest = $(obj).parent().find("input[name='fileLogo']")[0];
                $(dest).val(res);
            }
            function removeFileUpload(obj) {
                $(obj).parent().parent().remove();
            }
            function check() {
                if ($("#inputForm input[name='fileLogo']").length == 0) {
                    alert('请先添加要上传的支付凭证后再提交！');
                    return false;
                }
                return true;
            }

            function showAll() {
                $(".grouprow").each(function () {
                    $(this).show();
                });
            }

            //本界面展示的渠道列表
            var channellist = {};
            /**
             * 创建切位渠道的方法
             */
            function addCutChannel(channel) {
                var $channelView = $(".channel-cut-copy").clone();
                $channelView.find('span[name="channelName"]').text(channel.channelName);
                $channelView.find('select[name="salerName"]').append(channel.salerOptions);
                $channelView.find('span[name="channelCode"]').text(channel.channelCode);
                $channelView.find('.channel-cut').attr('channel-code', channel.channelCode);
                if ($(".channel-cut").length > 1) {
                    $("#modForm").append('<hr style="border-style: solid;border-color: #cccccc">')
                }
                $("#modForm").append($channelView.html());
                channellist[channel.channelCode] = [];
            }
            /**
             * 创建切位渠道
             */
             $(function(){
                 var channels = [];
                 if('${agentJsonInfos}' != '') {
                 	channels = jQuery.parseJSON('${agentJsonInfos}');
                 }
                 for(var i=0,len=channels.length;i<len;i++){
                 	var salerUsersJson = channels[i].agentSalerUser;
                 	var salerOptions = new Array();
                    var salerUsers = eval(salerUsersJson);
                 	for ( var j = 0; j < salerUsers.length; j++) {
						var newOpt = new Option(salerUsers[j].name, salerUsers[j].id);
						salerOptions.push(newOpt);
					}
					channels[i].salerOptions = salerOptions;
                    addCutChannel(channels[i]);
                 }
             });

            $(document).ready(function () {
                $("#btnSubmit").removeAttr("disabled");
                agendChoose();
                var datepicker = $(".groupDate").datepickerRefactor(
                        {
                            dateFormat: "yy-mm-dd",
                            target: "#dateList",
                            numberOfMonths: 3,
                            isChickArr: getChickList(),
                            defaultDate: '2016-05-08'
                        }, "#groupOpenDate", "#groupCloseDate");
                //表单验证
                $("#modForm").validate({
                    rules: {
                        agentId: "required"
                    },
                    messages: {
                        agentId: "请选择一个切位渠道"
                    },
                    errorPlacement: function (error, element) {
                        if (element.is(":radio"))
                            error.appendTo(element.parent());
                        else if (element.is(":checkbox"))
                            error.appendTo(element.parent());
                        else if (element.is("input"))
                            error.appendTo(element.parent());
                        else
                            error.insertAfter(element);
                    }
                });


                $(".selectGroupId").click(function () {
                    if ($(this).text() == '选择全部') {
                        $('.cancelGroupId').attr('checked', 'true');
                        $(this).text('取消全部');
                    } else {
                        $('.cancelGroupId').removeAttr('checked');
                        $(this).text('选择全部');
                    }
                });
            });

            jQuery.extend(jQuery.validator.messages, {
                required: "必填信息",
                digits: "请输入正确的数字",
                number: "请输入正确的数字价格"
            });
            function agentIdChange(selectdata) {
                window.location.href = '/a/stock/manager/apartGroup/reserve?id=9678&agentId=' + selectdata + '&activityGroupId=10619';
            }
            //查询所选择的渠道信息
            function choseAgent(agentId) {
                var selectdata = null;
                if (agentId) {
                    selectdata = agentId;
                } else {
                    selectdata = $("#agentIdIn option:selected").val();
                    agentIdChange(selectdata);
                }
                $.ajax({
                    type: "POST",
                    url: "/a/stock/manager/apartGroup/agentInfo",
                    data: {
                        agentId: selectdata
                    },
                    success: function (msg) {
                        $(msg).each(function (index1, obj1) {
                            $(".agentInfo").html("渠道商名称：" + obj1.agentName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;接口销售员：" + (obj1.agentSalerName == null ? '暂无' : obj1.agentSalerName) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系电话：" + (obj1.salerMobile == null ? '暂无' : obj1.salerMobile));
                        });
                    }
                });
                $(".jbox-close").click();
                $(".agentId").val(selectdata);
            }

            function quitAgent() {
                $(".jbox-close").click();
            }
            function delGroupDate(obj) {
                $(document).delGroup1(obj);

                var divobj = $(obj).parent().parent();
                $(divobj).remove();
            }
            function delgroup(obj) {
                $(obj).parent().parent().remove();
            }
            function maxVali(obj) {
                if ($("#payReservePosition").val() != null && $("#payReservePosition").val() != "") {
                    $("#btnSubmit").removeAttr("disabled");
                }
                var old = $(obj).parent().prev().val();
                var newVal = $(obj).val();
                var freePosition = $(obj).parent().next().text();
                if (newVal - old > freePosition) {
                    return false;
                } else {
                    return true;
                }
            }
            jQuery.validator.addMethod("intChange", function (value, element) {
                return this.optional(element) || maxVali(element);
            }, "余位不足");
            jQuery.validator.addMethod("isZero", function (value, element) {
                return this.optional(element) || !($(element).val() == 0)
            }, "无切位人数");

            function getChickList() {
                var d = 1;
                var dateArr = new Array();
                for (var i = 1; i <= d; i++) {
                    var beforDate = $("#grouprow" + i).find(".leftdays").val();
                    if (beforDate < 0) {
                        var date = ($("#grouprow" + i + " td").eq(1).text()).split("-");
                        dateArr.push(new Date(date[0], date[1] - 1, date[2]));
                    }
                }
                return dateArr;
            }
            function upload(activityGroupId) {
                if ($('#agentId').val() == '') {
                    $.jBox.tip('请先选择切位渠道！', 'error');
                    return false;
                }
                var iframe = "iframe:/a/stock/manager/apartGroup/uploadform?srcActivityId=9678&agentId=" + $('#agentId').val() + "&activityGroupId=" + activityGroupId;
                $.jBox(iframe, {
                    title: "支付凭证",
                    width: 580,
                    height: 460,
                    buttons: {}
                });
                return false;
            }
            function downloads(docid, activitySerNum, groupCode, acitivityName, iszip) {
                if (iszip) {
                    var zipname = activitySerNum + '-' + groupCode;
                    window.open("/a/sys/docinfo/zipdownload/" + docid + "/" + zipname);
                }

                else
                    window.open("/a/sys/docinfo/download/" + docid);
            }

            function cancelGroup() {
                $(".cancelGroupId").each(function () {
                    if ($(this).attr("checked")) {
                        $(this).parent().parent().hide();
                        $(this).removeAttr("checked");

                    }
                });
                $(".selectGroupId").text('选择全部');
            }

            function agendChoose() {
                if (false) {
                    var _select = $("#agentId").clone();
                    _select.attr("id", "agentIdIn");
                    _select.attr("name", "agentIdIn");
                    _select.unbind();
                    var $select = $('<p></p>').append(_select);
                    var html = '<div id="chooseQd" class="tanchukuang"><div class="add_allactivity choseAgents"><p style="line-height:60px;text-align:center;">共有' + 370 + '家渠道为您服务，请选择渠道为其切位</p><p>' + "<label>渠道选择：</label>" + $select.html() + '</p></div></div>'

                    $.jBox(html, {
                        title: "切位-选择渠道",
                        buttons: {'取消': 0, '提交': 1},
                        submit: submit,
                        height: 220,
                        width: 400,
                        persistent: true
                    });
                    $(".jbox-close").hide();
                } else {
                    choseAgent(319);
                }
            }

            var submit = function (v, h, f) {
                if (v == 1) {
                    choseAgent();
                } else {
                    history.go(-1);
                }
            }

            function formSubmit() {
                var payType = $("#payType").val();
                if (payType == null || payType == "-1") {
                    alert("请选择支付方式");
                    return;
                }
                var agentId = $("#agentId").val();
                if (agentId != null && agentId != "") {
                    var frontMoney = $("#frontMoney").val();
                    var reservation = $("#reservation").val();
                    var remark = $("#remark").val();
                    if (frontMoney.length > 8) {
                        alert("定金不能大于8位数");
                        return false;
                    }
                    if (reservation.length > 26) {
                        alert("预订人姓名过长");
                        return false;
                    }
                    if (remark.length > 200) {
                        alert("备注不能超过200个字符");
                        return false;
                    }
                    $("#modForm").submit();
                } else {
                    agendChoose();
                }
                if ($("#payReservePosition").val() != null && $("#payReservePosition").val() != "") {
                    $("#btnSubmit").attr({"disabled": "disabled"});
                }
            }


            function checkMoney(v, obj) {
                var a = /^[0-9]*(\.[0-9]{1,2})?$/;
                if (!a.test(v)) {
                    alert("金额格式不正确");
                    document.getElementById(obj).value = "";
                    $("#user_id").val("");
                    // $(obj).value="";
                    return false;
                }
            }

            function clean(id) {
                $("#frontMoney").val("");
                $("#payReservePosition").val("");
                $("#reservation").val("");
                $("#remark").val("");
                //清空支付方式
                $("#payType option[value='-1']").attr("selected", true);
                $("#btnSubmit").removeAttr("disabled");
                return true;
            }
            function resetBtn() {
                $("#btnSubmit").removeAttr("disabled");
            }

        </script>

        <script type="text/javascript">
            function clearCut(el) {
                var $tr = $(el).parents('tr:first');
                $tr.removeClass('error');
                $tr.find('input[type="text"],select').val('');
                $tr.find('input[name=cutCount]').next().remove();
            }

            function getCutChannelList() {

            }

            $(document).ready(function () {
                $(document).on('change', 'select[name="agentId"]', function () {
                    var $this = $(this);
                    var channelName = $(this).find('option:selected').text();
                    var salerName = $(this).find('option:selected').attr('saler-name');
                    $this.parents('.channel-cut:first').find('[name="salerName"]').text(salerName);
                    $this.parents('.channel-cut:first').find('[name="agentName"]').text(channelName);
                });

                $(document).on('click', '.visa_copy', function () {
                    var $th = $(this).parents('th:first');
                    var $tr = $(this).parents('tr:first');
                    var name = $(this).attr('name');
                    var value = $(this).siblings('input,select').val()
                    $(this).parents('table:first').find('tbody tr').each(function () {
                        $(this).find('input[name="' + name + '"],select[name="' + name + '"]').val(value);
                        $(this).find('input[name="' + name + '"],select[name="' + name + '"]').parent().parent().removeClass('error');
                        if(name =='cutCount') {
	                        if( +value <= (+$(this).find('td[name=surplusCount]').text())){
	                        	$(this).find('input[name="' + name + '"]').next().html("");
	                        }
	 						else{
	 							$(this).find('input[name="' + name + '"]').next().remove();
	                        	$(this).find('td[name=surplusCount]').prev().append('<span style="color:red"><br>余位不足</span>')
	                        }	
                        }
                    });
                });

                $(document).on('click', '[name=clear]', function () {
                    var _this = this;

                    $.jBox.confirm('确定要清空数据吗？', '系统提示', function (v, h, f) {
                        if (v === 'ok') {
                            clearCut(_this);
                        }
                    });
                });
                $(document).on('click', '[name=clearAll]', function () {
                    var $this = $(this);
                    var $checks = $this.parents('.channel-cut:first').find('table input[name="groupCheck"]:checked:visible');
                    if (!$checks.length ) {
                        $.jBox.tip('您未选择团期,请选择！', 'error');
                        return;
                    }
                    $.jBox.confirm('确定清空所有选择的数据吗？', '系统提示', function (v, h, f) {
                        if (v === 'ok') {
                            $checks.each(function () {
                                clearCut(this);
                            });
                        }
                    });
                });

                $(document).on('click', '[name="batchDelete"]', function () {
                    var $this = $(this);
                    var $temp = $(this);
                    var $area = $this.parents('.channel-cut:first');
                    var $checks = $area.find('table input[name="groupCheck"]:checked:visible')
                    if (!$checks.length) {
                        $.jBox.tip('您未选择团期,请选择！', 'error');
                        return;
                    }
                    $.jBox.confirm('确定删除所有选择的数据吗？', '系统提示', function (v, h, f) {
                        if (v === 'ok') {
                            $checks.each(function () {
                                $this = $(this);
                                if ($this.attr('name') == 'checkAll' || !$this.is(':visible')) {
                                    return;
                                }
                                var $checkRow = $this.parent().parent();
                                var $detail = $checkRow.next('[name="detail"]');
                                removeItem(channellist[$area.attr('channel-code')], decodeURIComponent($checkRow.attr('group-no')));
                                $checkRow.remove();
                                $detail.remove();
                            });
                            resetIndex($area);
                            $temp.parents("form#modForm").find("[name='saveInDraftbox']").attr("disabled", "true").css('background-color', '#cccccc');
                        }
                    });
                })

                $(document).on('click', '[name="batchCopy"]', function () {
                    var $this = $(this);
                    if (!$this.parents('.channel-cut:first').find('table input[name="groupCheck"]:checked:visible').length) {
                        $.jBox.tip('您未选择团期,请选择！', 'error');
                        return;
                    }
                    var products = getSelectedRows($(this));
                    var channelCode = $(this).parents('.channel-cut:first').find('[name="channelCode"]');
                    var $channelSelect = $('#popCutChannelSelect').clone();
                    $channelSelect.find('option').remove();
                    debugger;
                    var $channels = $('.channel-cut').each(function () {
                        var channelCodeValue = $(this).find('[name="channelCode"]').text();
                        var channelName = $(this).find('[name="channelName"]').text();
                        if ((channelCodeValue != '') && (channelCodeValue != channelCode.text())) {
                            $('<option>', {
                                value: channelCodeValue,
                                text: channelName
                            }).appendTo($channelSelect.find("#popAgentId"));
                        }
                    });
                    var $pop = $.jBox($channelSelect.html(), {
                        title: "选择渠道", buttons: {'添加': 1, '取消': 2}, submit: function (v, h, f) {
                            if (v == 1) {
                                var channels = [];
                                $pop.find('.selected-channel .channel-text').each(function () {
                                    var code = $(this).attr('channel-code');
                                    var name = $(this).text();
                                    channels.push({
                                        channelCode: code,
                                        channelName: name
                                    });
                                });
                                copyGroups(channels, products);
                            }
                        }, height: 320, width: 380
                    });
                    $pop.on('click', '.channel-remove', function () {
                        $(this).parent().remove();
                    });
                    var $popAgentId = $pop.find('#popAgentId').comboboxInquiry();
                    $popAgentId.on('comboboxinquiryselect', function () {
                        var channelCode = $popAgentId.val();
                        var channelName = $popAgentId.find('option:selected').text();

                        if (!$pop.find('.selected-channel [channel-code="' + channelCode + '"]').length) {
                            var channelText = '<span class="channel-text" channel-code="' + channelCode + '">' + channelName +
                                    '<span class="channel-remove">x</span>' +
                                    '</span>';
                            $pop.find('.selected-channel').append(channelText);
                        }
                        $popAgentId.val('');
                        setTimeout(function () {
                            $pop.find('.ui-autocomplete-input').val('');
                        }, 100);
                    });
                })


                //获取选中行的数据
                function getSelectedRows(obj) {
                    var products = [];
                    obj.parents('.channel-cut:first').find('table tbody tr:visible input[type="checkbox"]:checked').each(function () {
                        if ($(this).attr('name') == 'groupCheck') {
                            var $row = $(this).parent().parent();
                            if ($row.attr('name') != "groupList") {
                                var $tds = $row.find('td');
                                var product = {};
                                var cIndex = 0;
                                //产品id
                                product.id = $tds.eq(cIndex).find("input[name=groupCheck]").val();
                                //产品名称
                                cIndex++;
                                product.productName = $tds.eq(cIndex).html();
                                //团号
                                cIndex++;
                                product.groupNo = $tds.eq(cIndex).html();
                                //出团日期
                                cIndex++;
                                product.startDate = $tds.eq(cIndex).html();
                                //订金
                                cIndex++;
                                product.deposit = $tds.eq(cIndex).children().eq(0).val();
                                //切位人数
                                cIndex++;
                                product.cutCount = $tds.eq(cIndex).children().eq(0).val();
                                //余位
                                cIndex++;
                                product.surplusCount = $tds.eq(cIndex).html();
                                //预定人
                                cIndex++;
                                product.reservation = $tds.eq(cIndex).children(':first').val();
                                //成人
                                cIndex++;
                                product.settlementAdultPrice = $tds.eq(cIndex).html();
                                //儿童
                                cIndex++;
                                product.settlementcChildPrice = $tds.eq(cIndex).html();
                                //特殊人群
                                cIndex++;
                                product.settlementSpecialPrice = $tds.eq(cIndex).html();
                                //单房差
                                cIndex++;
                                product.singleDiff = $tds.eq(cIndex).html();
                                //预收
                                cIndex++;
                                product.advanceCount = $tds.eq(cIndex).text();
                                //支付时间
                                cIndex++;
                                product.payTime = $tds.eq(cIndex).html();
                                //支付方式
                                cIndex++;
                                product.payType = $tds.eq(cIndex).children(':first').val();
                                //备注
                                cIndex++;
                                product.memo = $tds.eq(cIndex).children(':first').val();
                                products.push(product);
                            }
                        }
                    });
                    return products;
                }

                function copyGroups(channels, products) {

                    if (isGroupExist(channels, products)) {
                        $.jBox.confirm('目标渠道已包含所选团期,复制后可能覆盖已录入的信息,确定要复制么?', '系统提示', function (v, h, f) {
                            if (v == 'ok') {
                                batchCopy(channels, products);
                            }
                        });
                    } else {
                        batchCopy(channels, products);
                    }
                }


                function batchCopy(channels, products) {
                    for (var i = 0, len = channels.length; i < len; i++) {
                        $('span[name="channelCode"]').each(function () {
                            if ($(this).text() == channels[i].channelCode) {
                                cloneRows($(this).parents('.channel-cut:first').find('[name="openGroupList"]'), products);
                            }
                        });
                    }
                }

                //校验批量复制的团期是否已在目标渠道中存在
                function isGroupExist(channels, products) {
                    for (var i = 0, len = channels.length; i < len; i++) {
                        var selectedGroup = channellist[channels[i].channelCode];
                        if (selectedGroup) {
                            for (var m = 0, len = selectedGroup.length; m < len; m++) {
                                console.log(selectedGroup[m]);
                                for (var n = 0, len = products.length; n < len; n++) {
                                    console.log(products[n].groupNo);
                                    if (selectedGroup[m] == products[n].groupNo) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }

                $(document).on('click', '[name="confirm"]', function () {
                	//校验信息
					var isValid=true;
                    var validInfo = '';
                    var $channelInfo = $(this).parents('div.channel-cut');
                    var checkedSize = 0;
                    var saler = $(this).parent().find('select[name=salerName]').val();  // 跟进销售
                    $channelInfo.find('table tbody tr[name=product]').each(function(){
                        var $tr= $(this);
                        if(($tr.find("input[name=groupCheck]").attr("checked") == "checked")){
                            var cutCount = $tr.find('input[name=cutCount]').val();
                            var surplusCount = $tr.find('td[name=surplusCount]').text();
                            checkedSize ++;
                            
                            if(cutCount == '') {
                                isValid = false;
                                validInfo = '切位人数为必填项';
                                $tr.find('input[name=cutCount]').focus();
                            	return false;
                            }
                            if(parseInt(cutCount) > parseInt(surplusCount)){
                                isValid = false;
                                validInfo = '产品团期余位不足，请查看';
                                $tr.addClass('error');
                                return false;
                            }
                            if(!saler || saler == "0"){
                                isValid = false;
                                validInfo = '请选择渠道跟进人';
                                return false;
                            }
                        }
                    });
                    
                    if(checkedSize == 0) {
                        $.jBox.tip('请选择产品!','error');
                    	return false;
                    }
                    
                    if(!isValid){
                        $.jBox.tip(validInfo,'error');
                        return false;
                    }
                    
                    //渠道切位信息
                    var reserveInfos = [];
                    var reserveInfoList = getReserveList($channelInfo, true);
                   	for(var i=0; i<reserveInfoList.length; i++) {
                      	reserveInfos.push(reserveInfoList[i]);
                   	}
                    var reserveJsonData = JSON.stringify(reserveInfos);
                    
                    //渠道上传文件信息
                    var uploadInfos = [];
                    var uploadInfoArr = getUploadInfos($channelInfo);
                    for(var i=0; i<uploadInfoArr.length; i++) {
                        uploadInfos.push(uploadInfoArr[i]);
                    }
                    var uploadJsonData = JSON.stringify(uploadInfos);
                    
                    $.ajax({
                		type: "POST",
                	   	url: "${ctx}/stock/manager/apartGroup/batchReceive",
                	   	async: false,
                	   	data: {
                				"reserveJsonData":reserveJsonData,
                				"uploadJsonData":uploadJsonData
                			  },
                		dataType: "json",
                	   	success: function(data){
                	   		if(data.result=="1"){
                	   			$.jBox.tip(data.message, 'success');
                	   			var closeFlag = true;
                	   			
                	   			//确认切位后删除行记录，并收起列表
                	   			$channelInfo.find('table tbody tr[name=product]').each(function(){
                	   				if(($(this).find("input[name=groupCheck]").attr("checked") == "checked")){
                	   					$(this).next('tr[name=detail]').remove();
                	   					$(this).remove();
                	   				} else {
                	   					closeFlag = false;
                	   				}
                	   			});
                	   			
                	   			if(closeFlag) {
                	   				$channelInfo.find('span.closeOrExpand').click();
                	   			}
                	   			resetIndex($channelInfo);
                	   		} else if(data.result=="2") {
                	   			$.jBox.tip(data.message, 'fail');
                	   		} else if(data.result=="3") {
                	   			$.jBox.tip(data.message, 'error');
                	   		}
                	   	}
                	});

                });

                $(document).on('blur', '[name="cutCount"]', function () {
                    $(this).next().remove();
                    var $tr = $(this).parent().parent();
                    $tr.removeClass('error');
                    var cutCount = (+$tr.find('[name="cutCount"]').val());
                    var surplusCount = (+$tr.find('[name="surplusCount"]').text());
                    if (cutCount > surplusCount) {
                        $(this).after('<span style="color:red"><br>余位不足</span>');
                    }
                });

              //根据渠道切位信息组装切位json数据,checkFlag为true时多选框需要进行校验，checkFlag为false时多选框不需要进行校验
                function getReserveList($obj, checkFlag){
                	var reserveInfoList = [];
            		var $reserveTbody = $obj.find('tbody.reserveTbody');
            		var channelCode = $obj.attr("channel-code");
            		
            		$($reserveTbody).find("tr[name=product]").each(function(){
            			if(checkFlag) {
	            			if($(this).find("input[name=groupCheck]").attr("checked") == 'checked') {
	            		    	var reserveInfo = {};
	            				reserveInfo.activityGroupId = $(this).find("input[name=groupCheck]").val();//团期id
	            				reserveInfo.agentId = channelCode;//渠道商基本信息表id'
	            				reserveInfo.payReservePosition = $(this).find("input[name=cutCount]").val();//切位人数
	            				reserveInfo.frontMoney = $(this).find("input[name=cutAmout]").val();//定金金额
	            				reserveInfo.reservation = $(this).find("input[name=reservationer]").val();//预订人
	            				reserveInfo.payType = $(this).find("select[name=payMethod]").val();//支付方式
	            				reserveInfo.remark = $(this).find("input[name=memo]").val();//切位备注
	            				reserveInfoList.push(reserveInfo);
	            			}
            			} else {
            				var reserveInfo = {};
            				reserveInfo.activityGroupId = $(this).find("input[name=groupCheck]").val();//团期id
            				reserveInfo.agentId = channelCode;//渠道商基本信息表id'
            				reserveInfo.payReservePosition = $(this).find("input[name=cutCount]").val();//切位人数
            				reserveInfo.frontMoney = $(this).find("input[name=cutAmout]").val();//定金金额
            				reserveInfo.reservation = $(this).find("input[name=reservationer]").val();//预订人
            				reserveInfo.payType = $(this).find("select[name=payMethod]").val();//支付方式
            				reserveInfo.remark = $(this).find("input[name=memo]").val();//切位备注
            				reserveInfoList.push(reserveInfo);
            			}
            		});
            		
            		return reserveInfoList;
                }
                
                function getUploadInfos($obj) {
                	var uploadInfos = [];
                	var channelCode = $obj.attr("channel-code");
                	$obj.find("span.seach_checkbox_user span").each(function(){
                		var uploadInfo = {};
                		uploadInfo.agentId = channelCode;
                		uploadInfo.srcDocId = $(this).find("input[name=docId]").val();
                		uploadInfo.fileName = $(this).find("input[name=docName]").val();
                		uploadInfos.push(uploadInfo);
                	});
                	return uploadInfos;
                }
                
                $('[name="return"]').on('click', function () {
                    window.location.href = "${ctx}/stock/manager/apartGroup?_m=147&_mc=243";
                });

                $(document).on('click','[name="confirmAll"]',function(){
                	//校验信息
					var isValid=true;
                    var validInfo = '';
                    var checkedSize = 0;
					var hasSaler = true;
					$(this).parent().parent().find('div.channel-cut').each(function(){
						if($(this).attr("channel-code")) {
							var everySaler = $(this).find('select[name=salerName]').val();
							if(!everySaler || everySaler == "0") {
								hasSaler = false;
								return false;
							}
						}
					});
                    $('#modForm').find('div.channel-cut').find('table tbody tr[name=product]').each(function(){
                        var $tr= $(this);
                            var cutCount = $tr.find('input[name=cutCount]').val();
                            var surplusCount = $tr.find('td[name=surplusCount]').text();
                            
                            checkedSize ++;
                            if(cutCount == '') {
                                isValid = false;
                                validInfo = '切位人数为必填项';
                                $tr.find('input[name=cutCount]').focus();
                            	return false;
                            }
                            if(parseInt(cutCount) > parseInt(surplusCount)){
                                isValid = false;
                                validInfo = '产品团期余位不足，请查看';
                                $tr.addClass('error');
                            }
                            if(!hasSaler){
                                isValid = false;
                                validInfo = '请选择渠道跟进人';
                                return false;
                            }
                    });
                    
                    if(checkedSize == 0) {
                        $.jBox.tip('没有数据!','error');
                    	return false;
                    }
                    
                    if(!isValid){
                        $.jBox.tip(validInfo,'error');
                        return false;
                    }
                    
                    //渠道切位信息
                    var reserveInfos = [];
                    $("#modForm .channel-cut").each(function(){
                    	var reserveInfoList = getReserveList($(this), false);
                    	for(var i=0; i<reserveInfoList.length; i++) {
                    		reserveInfos.push(reserveInfoList[i]);
                    	}
                    });
                    var reserveJsonData = JSON.stringify(reserveInfos);
                    
                    //渠道上传文件信息
                    var uploadInfos = [];
                    $("#modForm .channel-cut").each(function(){
                    	var uploadInfoArr = getUploadInfos($(this));
                        for(var i=0; i<uploadInfoArr.length; i++) {
                            uploadInfos.push(uploadInfoArr[i]);
                        }
                    });
                    var uploadJsonData = JSON.stringify(uploadInfos);
                    
                    $.ajax({
                		type: "POST",
                	   	url: "${ctx}/stock/manager/apartGroup/batchReceive",
                	   	async: false,
                	   	data: {
                				"reserveJsonData":reserveJsonData,
                				"uploadJsonData":uploadJsonData
                			  },
                		dataType: "json",
                	   	success: function(data){
                	   		if(data.result=="1"){
                	   			$.jBox.tip(data.message, 'success');
                	   			window.setTimeout('window.location.href = "${ctx}/stock/manager/apartGroup?_m=147&_mc=243"', 300);
                	   		} else if(data.result=="2") {
                	   			$.jBox.tip(data.message, 'fail');
                	   		} else if(data.result=="3") {
                	   			$.jBox.tip(data.message, 'error');
                	   		}
                	   	}
                	});
                });

                var submitTimes = 0;
                // 存入草稿
                $(document).on('click', '[name="saveInDraftbox"]', function () {
                    if(submitTimes > 0) {
                        $.jBox.tip("请不要重复提交!", "warning");
                        return;
                    }
                    submitTimes++;
                    // 跟进销售
                    var hasSaler = true;
					$(this).parent().parent().find('div.channel-cut').each(function(){
						if($(this).attr("channel-code")) {
							var everySaler = $(this).find('select[name=salerName]').val();
							if(!everySaler || everySaler == "0") {
								hasSaler = false;
								return false;
							}
						}
					});
					if(!hasSaler){
                        $.jBox.tip("请选择渠道跟进人", 'error');
                        submitTimes--;
                        return false;
                    }
                    //渠道切位信息
                    var reserveInfos = [];
                    $("#modForm .channel-cut").each(function(){
                        var reserveInfoList = getReserveList($(this), true);
                        for(var i=0; i<reserveInfoList.length; i++) {
                            reserveInfos.push(reserveInfoList[i]);
                        }
                    });
                    var reserveJsonData = JSON.stringify(reserveInfos);

                    //渠道上传文件信息
                    var uploadInfos = [];
                    $("#modForm .channel-cut").each(function(){
                        var uploadInfoArr = getUploadInfos($(this));
                        for(var i=0; i<uploadInfoArr.length; i++) {
                            uploadInfos.push(uploadInfoArr[i]);
                        }
                    });
                    var uploadJsonData = JSON.stringify(uploadInfos);

                    $.ajax({
                        type: "POST",
                        url: "${ctx}/activitygroupreserveTemp/batchSave2Draftbox",
                        async: false,
                        data: {
                            "reserveJsonData":reserveJsonData,
                            "uploadJsonData":uploadJsonData
                        },
                        dataType: "json",
                        success: function(data){
                            if(data.result=="success"){
                                $.jBox.tip(data.message, 'success');
                                window.setTimeout('window.location.href = "${ctx}/activitygroupreserveTemp/list?showType=1"',300);
                            } else if(data.result=="failed") {
                                $.jBox.tip(data.message, 'fail');
                                submitTimes--;
                            } else {
                                $.jBox.tip("系统异常!", 'error');
                                submitTimes--;
                            }
                        }
                    });
                });


                $(document).on('click', '[name="removeChannel"]', function () {
                    var $this = $(this);
                    $.jBox.confirm('确定要删除该渠道吗？', '系统提示', function (v, h, f) {
                        if (v === 'ok') {
                            var $channelArea = $this.parents('.channel-cut');
                            var hr = $channelArea.next('hr');
                            if(hr == null || hr == undefined || hr.length == 0) {
                            	hr = $channelArea.prev('hr');
                            }
                            hr.remove();
                            var channelCode = $channelArea.attr('channel-code');
                            $channelArea.remove();
                            delete channellist[channelCode];

                            var checkboxLength = $('input[type="checkbox"][name="groupCheck"]:checked').length;
                            $('input[type="checkbox"][name="groupCheck"]:checked').each(function(){
                                var isHidden = $(this).parents("[name='reserveInfoTr']").is(":hidden");
                                if(isHidden) {
                                    checkboxLength = checkboxLength - 1;
                                }
                            });
                            if (checkboxLength > 0) {
                                $('[name="saveInDraftbox"]').removeAttr('disabled').css('background-color', '#5f7795');
                            } else {
                                $('[name="saveInDraftbox"]').attr('disabled', 'false').css('background-color', '#cccccc');
                            }
                        }
                    });
                })

                $(document).on('click', '[name="addChannel"]', function () {
                	var $channelSelect = $('#popCutChannelSelect').clone();
                	var channelCodes = [];
                	$(document).find("div.channel-cut").each(function(){
                		channelCodes.push($(this).attr("channel-code"));
                	});
                	
                	for(var i=0; i<channelCodes.length; i++) {
                		$channelSelect.find("option[value="+channelCodes[i]+"]").remove();
                	}
                	
                    var $pop = $.jBox($channelSelect.html(), {
                        title: "选择渠道", buttons: {'添加': 1, '取消': 2}, submit: function (v, h, f) {
                            if (v == 1) {
                            	var channels =[];
                                var agentinfoIds = [];

                                //渠道选择校验,去重操作
                                var validate = true;
                                var channelName = '';
                                $pop.find('.selected-channel .channel-text').each(function () {
                                    var code = $(this).attr('channel-code');
                                    channelName = $(this).attr('channel-name');
                                    $("#modForm").find(".channel-cut").each(function(){
                                    	if($(this).find("span[name=channelCode]").text() == code) {
                                    		validate = false;
                                    		return false;
                                    	}
                                    });
                                    if(!validate) {
                                    	return false;
                                    }
                                    agentinfoIds.push(code);
                                });

                                if(!validate) {
                                	$.jBox.tip("已经包含该（"+ channelName +"）渠道信息，请重新输入！");
                                	return false;
                                }

                                $.ajax({
                            		type: "POST",
                            	   	url: "${ctx}/agent/manager/getAgentinfoJsonBean",
                            	   	async: false,
                            	   	data: {
                            				"agentinfoIds":agentinfoIds.join(',')
                            			  },
                            		dataType: "json",
                            	   	success: function(data){
                            	   		if(data){
                            	   			channels = data;
                            	   		}
                            	   	}
                            	});
                                
                                for(var i= 0,len=channels.length;i<len;i++){
                                	var salerUsersJson = channels[i].agentSalerUser;
                                	salerUsersJson = "'" + salerUsersJson + "'";
				                    var salerUsers = eval(eval(salerUsersJson));
				                 	var salerOptions = new Array();
				                 	for ( var j = 0; j < salerUsers.length; j++) {
										var newOpt = new Option(salerUsers[j].name, salerUsers[j].id);
										salerOptions.push(newOpt);
									}
									channels[i].salerOptions = salerOptions;
                                    addCutChannel(channels[i]);
                                }
                                
                            }
                        }, height: 320, width: 380
                    });
                    $pop.on('click', '.channel-remove', function () {
                        $(this).parent().remove();
                    });
                    var $popAgentId = $pop.find('#popAgentId').comboboxInquiry();
                    $popAgentId.on('comboboxinquiryselect', function () {
                        var channelCode = $popAgentId.val();
                        var channelName = $popAgentId.find('option:selected').text();
                        if (channellist[channelCode]) {
                            $.jBox.tip('此渠道已被选择！', 'info');
                            return;
                        }
                        if (!$pop.find('.selected-channel [channel-code="' + channelCode + '"]').length) {
                            var channelText = '<span class="channel-text" channel-code="' + channelCode + '">' + '<span>' + channelName + '</span>' +
                                    '<span class="channel-remove">x</span>' +
                                    '</span>';
                            $pop.find('.selected-channel').append(channelText);
                        }
                        $popAgentId.val('');
                        setTimeout(function () {
                            $pop.find('.ui-autocomplete-input').val('');
                        }, 100);

                    });
                });

                $(document).on('click', '[name="delete"]', function () {
                    var $this = $(this);
                    var $area = $this.parents('.channel-cut:first');
                    var $tr = $this.parents('tr:first');
                    var $detail = $tr.next('[name="detail"]');
                    $.jBox.confirm('确定要删除数据吗？', '系统提示', function (v, h, f) {
                        if (v === 'ok') {
                            $tr.remove();
                            removeItem(channellist[$area.attr('channel-code')], decodeURIComponent($tr.attr('group-no')));
                            $detail.remove();
                            resetIndex($area);

                            var checkboxLength = $('input[type="checkbox"][name="groupCheck"]:checked').length;
                            $('input[type="checkbox"][name="groupCheck"]:checked').each(function(){
                                var isHidden = $(this).parents("[name='reserveInfoTr']").is(":hidden");
                                if(isHidden) {
                                    checkboxLength = checkboxLength - 1;
                                }
                            });
                            if (checkboxLength > 0) {
                                $('[name="saveInDraftbox"]').removeAttr('disabled').css('background-color', '#5f7795');
                            } else {
                                $('[name="saveInDraftbox"]').attr('disabled', 'false').css('background-color', '#cccccc');
                            }
                        }
                    });
                });

                $(document).on('click', '[name="showDetail"]', function () {
                    var $tr = $(this).parents('tr:first');
                    var $toggleTr = $tr.next();
                    $toggleTr.toggle();
                    if ($toggleTr.is(':visible')) {
                        $(this).text($(this).text().replace('查看', '收起'));
                    } else {
                        $(this).text($(this).text().replace('收起', '查看'));
                    }
                    var sourceId = $tr.find("input[name=groupCheck]").val();
                    
                    $.ajax({
	                    type: "POST",
	                    url: "${ctx}/stock/manager/apartGroup/queryReserveList",
	                    async: false,
	                    data: {
	                    	sourceId : sourceId,
	                        reserveType: 0
	                    },
	                    success: function (data) {
	                    	data = jQuery.parseJSON(data);
	                    	if(data.code == '0000') {
			                    var html = [];
	                    		$.each(data.list, function(idx, obj) {
			                    	html.push('<tr>');
			                    	html.push('<td class="tc">' + obj.createDate + '</td>');
			                    	html.push('<td class="tc">' + obj.agentName + '</td>');
			                    	html.push('<td class="tc">' + obj.payReservePosition + '</td>');
			                    	html.push('<td class="tc">' + obj.reservation + '</td>');
			                    	html.push('<td class="tc">' + obj.orderMoney + '</td>');
			                    	html.push('<td class="tc">' + getTextByAttribute(obj.label) + '</td>');
			                    	html.push('</tr>');
								});
								$toggleTr.find("tbody").html(html.join(''));
	                    	}
	                    	
	                    }
	                });
                });

                $(document).on('click', '[name="openGroupList"]', function () {
                	var $this = $(this);
                    
                    var selectedProductArr = [];
                    $(this).parents('.channel-cut:first').find("table tr:visible input[name=groupCheck]").each(function(){
                    	selectedProductArr.push($(this).val());
                    });
                    
                    var $pop= $.jBox("iframe:${ctx}/stock/manager/apartGroup/getProductGroupPage?source=isReserve&selectedProducts=" + selectedProductArr.join(','),{
                        title: "产品团期列表",
                        width: 1200,
                        height: 500,
                        buttons: {'提交':1,'取消':0},
                        submit: function (v, h, f) {
                            if(v==1){
                                var iframe = $pop.find('iframe').get(0);
                                var $content = $(iframe.contentDocument);
                                var products = [];
                                $content.find('input[type="checkbox"]:checked').each(function(){
                                    var $row = $(this).parents('[name="groupRow"]:first');
                                    
                                    if($row.length>0 && $(this).attr('disabled') != 'disabled'){
                                        var $tds = $row.find('td');
                                        var product = {
                                            groupNo: $($tds.get(1)).text(),
                                            productName: $($tds.get(2)).text(),
                                            startDate: $($tds.get(4)).text(),
                                            surplusCount: $($tds.get(6)).text(),
                                            advanceCount: $($tds.get(8)).text(),
                                            id: $($tds.get(9)).find("input[name=id]").val(),
                                            settlementAdultPrice: $($tds.get(9)).find("input[name=agpsettlementAdultPrice]").val(),
                                            settlementcChildPrice: $($tds.get(9)).find("input[name=agpsettlementcChildPrice]").val(),
                                            settlementSpecialPrice: $($tds.get(9)).find("input[name=agpsettlementSpecialPrice]").val(),
                                            singleDiff: $($tds.get(9)).find("input[name=singleDiff]").val()
                                        }
                                        products.push(product);
                                    }
                                });
                                if(products.length==0){
                                    $.jBox.tip('没有选择团期', 'error');
                                    return false;
                                }
                                cloneRows($this,products);
                                $this.parent().find('.closeOrExpand').addClass("ydtExpand").removeClass("ydtClose");
                            }


                        }
                    }).find("#jbox-content").css("overflow","hidden");

                });

                function cloneRows(obj, products) {
                    //展开
                    obj.parents('.channel-cut').find('.toggle:first').show();
                    obj.parents('.channel-cut').find('.closeOrExpand').addClass("ydtExpand").removeClass("ydtClose");

                    var channelCode = obj.parents('.channel-cut').attr('channel-code');
                    for (var i = 0, len = products.length; i < len; i++) {
                        var product = products[i];
                        //根据团号获取列,若存在则直接删除,批量复制时会重新写入
                        var $selectedGroup = obj.parents('.channel-cut').find('tr[group-no="' + encodeURIComponent(product.groupNo) + '"]');
                        if ($selectedGroup) {
                        	$selectedGroup.next('[name="detail"]').remove();
                            $selectedGroup.remove();
                            removeItem(channellist[channelCode], product.groupNo);
                        }
                        var $tr = obj.parents('.channel-cut:first').find('tbody tr:first').clone();
                        var $detailtr = obj.parents('.channel-cut:first').find('tbody tr:first').next().clone();
                        $tr.show();
                        $tr.attr("group-no", encodeURIComponent(product.groupNo));
                        $tr.attr("name", "product");
                        var $tds = $tr.find("td");
                        var cIndex = 0;
                      //初始化团期id信息
                        $tds.eq(cIndex).find("input[name=groupCheck]").val(product.id);
                        //产品名称
                        cIndex++;
                        $tds.eq(cIndex).html(product.productName);
                        //团号
                        cIndex++;
                        $tds.eq(cIndex).html(product.groupNo);
                        //出团日期
                        cIndex++;
                        $tds.eq(cIndex).html(product.startDate);
                        //订金
                        cIndex++;
                        $('<input>', {
                            type: 'text',
                            name: 'cutAmout',
                            value: product.deposit,
                            onkeyup: "value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')",
                            onafterpaste: "value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')",
                            onblur: "value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')",
                            maxlength: 12
                        }).appendTo($tds.eq(cIndex));
                        //切位人数
                        cIndex++;
                        $tds.eq(cIndex).children().remove();
                        $('<input>', {
                            type: 'text',
                            name: 'cutCount',
                            value: product.cutCount,
                            maxlength: 12
                        //}).attr('data-type', 'number').appendTo($tds.eq(cIndex));
                        }).attr('data-type', 'number').attr('limit-zero','true').appendTo($tds.eq(cIndex));
                        //余位
                        cIndex++;
                        $tds.eq(cIndex).html(product.surplusCount);
                        //预定人
                        cIndex++;
                        $tds.eq(cIndex).children(':first').attr('value', product.reservation);
                        //成人
                        cIndex++;
                        $tds.eq(cIndex).html(product.settlementAdultPrice);
                        //儿童
                        cIndex++;
                        $tds.eq(cIndex).html(product.settlementcChildPrice);
                        //特殊人群
                        cIndex++;
                        $tds.eq(cIndex).html(product.settlementSpecialPrice);
                        //单房差
                        cIndex++;
                        $tds.eq(cIndex).html(product.singleDiff);
                        //预收
                        cIndex++;
                        $tds.eq(cIndex).text(product.advanceCount);
                        //支付时间
                        cIndex++;
                        //支付方式
                        cIndex++;
                        //备注
                        cIndex++;
                        $tds.eq(cIndex).children(':first').attr('value', product.memo);
                        obj.parents('.channel-cut:first').find('tr[name="btnGroup"]').before($tr.prop("outerHTML"));
                        //详情
                        obj.parents('.channel-cut:first').find('tr[name="btnGroup"]').before($detailtr.prop("outerHTML"));
                        channellist[channelCode].push(product.groupNo);
                        obj.parents('.channel-cut').find('tr[group-no="' + encodeURIComponent(product.groupNo) + '"]').find('select[name="payMethod"]').val(product.payType);
                    }
                    resetIndex(obj.parents('.channel-cut:first'));
                }

            });
            
            $(function () {
                var activeMark = 'activityStock';
                $('#' + activeMark).addClass('active');
            });

            function jump(href) {
                var _m = '147';
                var _mc = '243';
                href = appendParam(href, {_m: _m, _mc: _mc});
                window.location.href = href;
            }
            
          //下载文件
        	function downloads(docid){
        		window.open("${ctx}/sys/docinfo/download/"+docid);
            }
            //上传文件后的名称回显
            function commenFunction(obj,fileIDList,fileNameList,filePathList) {
        		var fileIdArr = fileIDList.split(";");
        		var fileNameArr = fileNameList.split(";");
        		var filePathArr = filePathList.split(";");
        		for(var i=0; i<fileIdArr.length-1; i++) {
       				//<span><a href="javascript:void(0);" title="点击下载附件">0001.jpg</a></span>
           			var html = [];
           			html.push('<span>');
           			html.push('<a href="javascript:downloads('+ fileIdArr[i] +');" title="点击下载附件">'+ fileNameArr[i] +'</a>');
           			html.push('<input type="hidden" name="hotelTraveler_files" />');
           			html.push('<input type="hidden" name="docId"  value="' + fileIdArr[i] + '" />');
           			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
           			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
           			html.push('</span>');
        			$(obj).parent().find("span.seach_checkbox_user").append(html.join(''));
        		}
            }
        </script>
</head>

<body>
	<page:applyDecorator name="stock_op_head">
        <page:param name="current">activityStock</page:param>
    </page:applyDecorator>
	<div>
		<div class="ydbzbox fs">
			<form id="modForm" class="form-search" action="/a/stock/manager/apartGroup/doreserve" method="post">
				<div style="padding-bottom: 6px;">
					<input class="btn btn-primary" type="button" value="返回" name="return" /> &nbsp; 
					<input class="btn btn-primary" type="button" value="存入草稿" name="saveInDraftbox" /> &nbsp; 
					<input class="btn btn-primary" type="button" value="全部切位" name="confirmAll" /> &nbsp; 
					<input class="btn btn-primary" type="button" value="添加渠道" name="addChannel" />
				</div>
				<div class="channel-cut-copy" style="display: none">
					<div class="channel-cut">
						<div class="" style="margin: 10px 0px">
							<span>渠道名称：</span> 
							<span name="channelName"></span> 
							<span name="channelCode" style="display: none"></span> 
							<span style="margin-left: 20px">渠道跟进人：</span> 
							<select name="salerName">
								<option value="">请选择</option>
							</select>
							<input name="openGroupList" type="button" value="添加团期" class="cl-tt-btn"> 
							<input name="confirm" type="button" value="确认切位" class="cl-tt-btn"> 
							<input name="removeChannel" type="button" value="删除渠道" class="cl-tt-btn"> 
							<span style="height:40px; width:1px; border-left:1px #868686 solid;margin-left: 15px"></span>
							<span style="position: relative; bottom: 9px; left: 5px;">
								<span class="closeOrExpand ydtClose"></span>
							</span>
						</div>

						<div class="toggle" style="display: none;margin-top: 10px;">
							<table
								class="table table-striped table-bordered table-condensed table-mod2-group ">
								<thead>
									<tr>
										<th rowspan="2" width="4%"><input name="checkAll"
											value="" type="checkbox" style="width: auto"> 序号 
										</th>
										<th rowspan="2" width="10%">产品名称</th>
										<th rowspan="2" width="6%">团号</th>
										<th rowspan="2" width="6%">出团日期</th>
										<th rowspan="2" width="5%">订金(元)
											<span class="tc">
												<input name="reservation2" id="reservation2" maxlength="12" type="text" onkeyup="value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')" onafterpaste="value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')" onblur="value=value.replace(/[\a-zA-Z\u4E00-\u9FA5]/g,'')">
												<input value="复制" name="cutAmout" class="visa_copy" type="button">
											</span>
										</th>
										<th rowspan="2" width="5%">
											<span class="xing">*</span>
											切位人数
											<span class="tc"> 
												<input name="reservation3" id="reservation3" maxlength="10" data-type='number' limit-zero="true" type="text"><br> 
												<input value="复制" name="cutCount" class="visa_copy" type="button">
											</span>
										</th>
										<th rowspan="2" width="4%">余位</th>
										<th rowspan="2" width="6%">预订人
											<span class="tc"> 
												<input name="reservation4" id="reservation4" type="text" maxlength="50"> <br> 
												<input value="复制" class="visa_copy" name="reservationer" type="button">
											</span>
										</th>
										<th colspan="3" class="t-th2" width="18%">同行价</th>
										<th rowspan="2" width="6%">单房差</th>
										<th rowspan="2" width="5%">预收</th>
										<th rowspan="2" width="8%">支付时间</th>
										<th rowspan="2" width="4%">支付方式
											<span class="tc"> 
												<select id="selectCurrency" name="selectCurrency">
													<option value="-1">请选择</option>
													<c:forEach items="${payTypes }" var="payType">
														<option value="${payType.value }">${payType.label }</option>
													</c:forEach>
												</select> <br> 
												<input value="复制" class="visa_copy" name="payMethod" type="button">
											</span>
										</th>
										<th rowspan="2" width="6%">备注
											<span class="tc"> 
												<input name="reservation5" id="reservation5" maxlength="10" type="text"> <br> 
												<input value="复制" class="visa_copy" name="memo" type="button">
											</span>
										</th>
										<th rowspan="2" width="5%">操作</th>
									</tr>
									<tr>
										<th width="6%">成人</th>
										<th width="6%">儿童</th>
										<th width="6%">特殊人群</th>
									</tr>
								</thead>
								<tbody class="reserveTbody">
									<tr group-no="21231" style="display:none" name="reserveInfoTr">
										<td class="tc">
											<input name="groupCheck" value="" type="checkbox" style="width: auto"> <span></span>
										</td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc" name="surplusCount"></td>
										<td class="tc">
											<input name="reservationer" type="text" maxlength="50">
										</td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"><fmt:formatDate value="${currDate }" pattern="yyyy-MM-dd" /></td>
										<td class="tc">
											<select name="payMethod">
												<option value="-1">请选择</option>
												<c:forEach items="${payTypes }" var="payType">
													<option value="${payType.value }">${payType.label }</option>
												</c:forEach>
											</select>
										</td>
										<td class="tc">
											<input name="memo" maxlength="10" type="text">
										</td>
										<td class="p0">
											<dl class="handle">
												<dt>
													<img title="操作" src="${ctxStatic}/images/handle_cz.png">
												</dt>
												<dd class="">
													<p>
														<span></span> 
														<a href="javascript:void(0)" name="clear">清空</a>
														<a href="javascript:void(0)" name="showDetail">查看已切位明细 </a> 
														<a href="javascript:void(0)" name="delete">删除</a>
													</p>
												</dd>
											</dl></td>
									</tr>
									<tr style="display:none" name="detail">
										<td colspan="18">
											<table class="table activitylist_bodyer_table">
												<thead>
													<tr>
														<th>切位时间</th>
														<th>渠道</th>
														<th>切位数</th>
														<th>预订人</th>
														<th>订金</th>
														<th>支付方式</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table></td>
									</tr>
									<tr name="btnGroup">
										<td colspan="17">
											<button class="btn btn-default" style="height: 26px;" type="button" name="batchDelete">批量删除</button>
											<button class="btn btn-default" style="height: 26px;" type="button" name="clearAll">批量清空</button>
											<button class="btn btn-default" style="height: 26px;" type="button" name="batchCopy">批量复制</button>
										</td>
									</tr>
								</tbody>
							</table>

							<div class="orderdetails_tit">&nbsp;&nbsp;上传切位支付凭证&nbsp;&nbsp;

							</div>
							<div style="text-align: left">
								<strong>上传切位支付凭证：</strong> 
								<input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','',this,'false')">
								<span class="fileLogo"></span> 
								<span class="seach_checkbox_user"> 
									
								</span>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div id="popCutChannelSelect" style="display: none">
		<div class="pop-channel-container">
			<div class="header">
				<label>切位渠道：</label> 
				<select id="popAgentId">
					<option value="-1">不限</option>
                    <c:forEach var="agentinfo" items="${agentinfoList }">
                    	<option value="${agentinfo.id }" <c:if test="${param.agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                    </c:forEach>
				</select>
			</div>
			<div class="selected-channel"></div>
		</div>
	</div>
</body>
</html>
