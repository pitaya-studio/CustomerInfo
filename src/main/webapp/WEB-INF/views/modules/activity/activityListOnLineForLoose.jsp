<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>

    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>已上架产品</title>

    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery.placeholder.min.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/activity/dynamic.group.validator.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/activity/activityForLoose.js"></script>
    <script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script><!-- 0258需求校验引入文件 -->
    <script type="application/javascript" src="${ctxStatic}/common/jquery.disabled.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/PriceList.js"></script>
    <%--518--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/priceList.css"/>
    <%--518--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css"/>

    <style type="text/css">
        .sort {
            color: #0663A2;

            cursor: pointer;
        }

        /*0071需求样式 */
        label.myerror {
            color: #ea5200;
            font-weight: bold;
            margin-left: 0px;
            padding-bottom: 2px;
            padding-left: 0px;

        }

        /*bug 16396 S*/
        .withdraw-relative span {
            margin-top: 23%;
            max-width: 132px !important;
            margin-left: 27px !important;
        }

        .withdraw-relative .tableCheckBox {
            position: absolute;
            top: 40%;
            margin-left: 10px !important;
            margin-left: 0 \9 !important; /*bug16433*/
            margin-top: -1px !important;
        }

        .withdraw-relative {
            height: 100%;
            width: 116px !important; /*bug16454*/
            padding-left: 0 !important;
        }

        .withdraw-relative input[type="text"] {
            margin-top: 19% !important;
            margin-left: 10px;
        }

        em.grounding-one, em.unGrounding {
            position: absolute;
        }

        @media screen and (max-width: 1400px) {
            /*屏幕宽度小于1400时调整团号上边距*/
            .withdraw-relative span {
                margin-top: 24%;
            }

            .withdraw-relative input[type="text"] {
                margin-top: 34% !important;

            }

            /*bug16454*/
            .price-list, .hotelAndHouse p {
                width: 100%;
                margin-left: 0;
            }

            span.price-title {
                width: 45%;
            }
        }

        /*bug 16396 E*/


    </style>

    <script type="text/javascript">

        var $ctx = '${ctx}';
        var paramKind = '${activityKind}';
		var requiredStraightPrice = '${requiredStraightPrice}';
		
        var isOnlinePage = "0";

        //  对应需求  c460
        var groupCodeRuleDT = '${groupCodeRuleDT}';

        //备注右边的提示是否显示
        function remarkVisible() {
            $('.groupNoteContent').each(function () {
                if ($(this).html() == '') {
                    $(this).parent().parent().prev().find('.groupNoteTipImg').hide();
                }
                else {
                    $(this).parent().parent().prev().find('.groupNoteTipImg').show()
                }
            })
        }

        //批量操作对应的产品ID
        var activityIds = "";

        //页面加载执行
        $(function () {
            g_context_url = "${ctx}";
            $(document).on('click', '.expandNotes', function () {
                var $this = $(this);
                //判断是否展开备注
                if ($this.parents('tr:first').next().css('display') == 'none') {
                    $this.text('收起备注');
                    $this.parents('tr:first').next().show();
                }
                else {
                    $this.text('展开备注');
                    $this.parents('tr:first').next().hide();
                }
            });
            //设置备注是否显示
            remarkVisible();

            $(".spinner").spinner({
                spin: function (event, ui) {
                    if (ui.value > 365) {
                        $(this).spinner("value", 1);
                        return false;
                    } else if (ui.value <= 0) {
                        $(this).spinner("value", 365);
                        return false;
                    }
                }
            });

            //设置默认排序方式
            var _$orderBy = $("#orderBy").val();
            if (_$orderBy == "") {
                _$orderBy = "groupOpenDate DESC";
            }
            var orderBy = _$orderBy.split(" ");
            $(".activitylist_paixu_left li").each(function () {
                if ($(this).hasClass("li" + orderBy[0])) {
                    orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
                    $(this).attr("class", "activitylist_paixu_moren");
                }
            });

            $('.team_top').find('.table_activity_scroll').each(function (index, element) {
                var _gg = $(this).find('tr').length;
                if (_gg >= 20) {
                    // 	bug 12814
                    $(this).addClass("group_h_scroll_top");
                    $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
                }
            });

            //上架产品被选中
            var activityStatusValue = '${activityStatusValue}';
            if (activityStatusValue == 'offline') {
                $("#offline").attr('class', 'active');
                $("#online").removeAttrs();
            }

            //保存筛选选中的币种信息
            var selectCurrencyType = "${travelActivity.currencyType}";
            if (null != selectCurrencyType || "" != selectCurrencyType) {
                $("#selectCurrencyType option").each(function () {
                    var txt = $(this).val();
                    if (txt == selectCurrencyType) {
                        $(this).attr("selected", "true");
                    }
                });
            }

            activityIds = "${activityIds}";

            //初始化表单校验信息提示语
            jQuery.extend(jQuery.validator.messages, {
                required: "必填信息",
                digits: "请输入正确的数字",
                number: "请输入正确的数字价格"
            });

            //产品名称获得焦点显示隐藏
            $("#wholeSalerKey").focusin(function () {
                var obj_this = $(this);
                obj_this.next("span").hide();
            }).focusout(function () {
                var obj_this = $(this);
                if (obj_this.val() != "") {
                    obj_this.next("span").hide();
                } else
                    obj_this.next("span").show();
            });
            if ($("#wholeSalerKey").val() != "") {
                $("#wholeSalerKey").next("span").hide();
            }


            $("#targetAreaId").val("${travelActivity.targetAreaIds}");
            $("#targetAreaName").val("${travelActivity.targetAreaNamess}");

            $('.handle').hover(function () {
                if (0 != $(this).find('a').length) {
                    $(this).addClass('handle-on');
                    $(this).find('dd').addClass('block');
                }
            }, function () {
                $(this).removeClass('handle-on');
                $(this).find('dd').removeClass('block');
            });

            //展开筛选按钮
            $('.zksx').click(function () {
                if ($('.ydxbd').is(":hidden") == true) {
                    $('.ydxbd').show();
                    //$(this).text('收起筛选');
                    $(this).addClass('zksx-on');
                } else {
                    $('.ydxbd').hide();
                   // $(this).text('展开筛选');
                    $(this).removeClass('zksx-on');
                }
            });

            //如果展开部分有查询条件的话，默认展开，否则收起
            var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
            var searchFormselect = $("#searchForm").find("select").not("#selectCurrencyType");
            var inputRequest = false;
            var selectRequest = false;
            for (var i = 0; i < searchFormInput.length; i++) {
                if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
                    inputRequest = true;
                }
            }
            for (var i = 0; i < searchFormselect.length; i++) {
                if ($(searchFormselect[i]).children("option:selected").val() != "" &&
                        $(searchFormselect[i]).children("option:selected").val() != "100" &&
                        $(searchFormselect[i]).children("option:selected").val() != null) {
                    selectRequest = true;
                }
            }
            if (inputRequest || selectRequest) {
                $('.zksx').click();
            }

            $('.team_a_click').toggle(function () {
                $(this).addClass('team_a_click2');
            }, function () {
                $(this).removeClass('team_a_click2');
            });

        });

        //条件重置
        var resetSearchParams = function () {
            $(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
                    .val('').removeAttr('checked').removeAttr('selected');
            $('#wholeSalerKey').val('');
            $('#estimatePriceRecordUserName').val('');
            $('#fromArea').val('');
            $('#targetAreaId').val('');
            $('#groupOpenDate').val('');
            $('#groupCloseDate').val('');
            $('#createName').val('');
            $('#groupLead').val('');
            $('#trafficName').val('');
            $('#backArea').val('');
            $('#activityLevelId').val('');
            $('#activityDuration').val('');
            $('#selectCurrencyType').val('1');
            $('#settlementAdultPriceStart').val('');
            $('#settlementAdultPriceEnd').val('');
            $('#activityDuration').val('');
            $('#travelTypeId').val('');
            $('#productser').val('');
            $('#productType').val('');
            $('#sousuo').show();
        };

        /*
         *  展开产品团期
         */
        function expand(child, obj, srcActivityId) {
            $(".groupNoteCol").hide();
            if ($(child).css("display") == "none") {
                if ("${userType}" == "1") {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/stock/manager/payReservePosition",
                        data: {
                            srcActivityId: srcActivityId
                        },
                        success: function (msg) {
                            $(obj).html("关闭全部团期");
                            $(child).show();
                            $(obj).parents("td").attr("class", "td-extend");
                            $(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
                            if (msg.length > 0) {
                                $(child + " [class^='soldPayPosition']").show();
                            }
                            $.each(msg, function (keyin, valuein) {
                                $("td .soldPayPosition" + (valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                            });
                        }
                    });
                } else {
                    if ("${shiroType}" == "loose") {
                        //debugger;
                        $.ajax({
                            type: "post",
                            url: "${ctx}/pricingStrategy/manager/findPricingStrategy",
                            data: {
                                srcActivityId: srcActivityId
                            },
                            success: function (msg) {
                                //debugger;
                            }
                        });
                    }
                    $(obj).html("关闭全部团期");
                    //修改bug16272，每次展开全部团期时都显示展开备注
                    $(obj).parents("tr:first").next().find('a.expandNotes').html('展开备注');
                    $(child).show();
                    $(obj).parents("td").attr("class", "td-extend");
                    $(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
                }
            } else {
                $(child).hide();
                $(obj).parents("td").attr("class", "");
                $(obj).html("展开全部团期");
                $(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
            }
        }
        /**
         分页方法
         */
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action", "${ctx}/activity/manager/list/${travelActivity.activityStatus }/${activityKind}?newflag=newflag");
            $("#searchForm").submit();
        }

        // 删除确认对话框
        function confirmxCopy(mess, id, proId, obj, child) {
            top.$.jBox.confirm(mess, '系统提示', function (v) {
                if (v == 'ok') {
                    $.ajax({
                        type: "POST",
                        async: false,
                        url: "${ctx}/activity/manager/hasOrder",
                        data: {"groupId": id},
                        success: function (result) {
                            if (result.data == "true" || result.data == true) {
                                top.$.jBox.info("团期已存在占位，不能删除", "警告");
                                return;
                            } else {
                                $.getJSON("${ctx}/activity/manager/delgroup2/" + $("#activityStatus").val() + "?id=" + id + "&proId=" + proId,
                                        function (result) {
                                            if ("success" == result.flag) {
                                                if (result.settlementAdultPrice)
                                                    $("#settleadultprice" + proId).html(result.settlementAdultPriceCMark + "<span class=\"tdred fbold\">" + result.settlementAdultPrice + "</span>起");
                                                else
                                                    $("#settleadultprice" + proId).html("价格待定");

                                                if (result.suggestAdultPrice)
                                                    $("#suggestadultprice" + proId).html(result.suggestAdultPriceCMark + "<span class=\"tdblue fbold\">" + result.suggestAdultPrice + "</span>起");
                                                else
                                                    $("#suggestadultprice" + proId).html("价格待定");

                                                if (result.groupOpenDate && result.groupCloseDate) {
                                                    if (result.groupOpenDate == result.groupCloseDate)
                                                        $("#groupdate" + proId).find("span").html(result.groupOpenDate);
                                                    else
                                                        $("#groupdate" + proId).find("span").html(result.groupOpenDate + "至" + result.groupCloseDate);
                                                } else
                                                    $("#groupdate" + proId).html("日期待定");

                                                $(obj).parents('tr:first').remove();
                                                if ($("#" + child + " tbody").find("tr").length == 0) {
                                                    $("#" + child).hide();
                                                    $("#groupdate" + proId).removeClass("td-extend");
                                                }
                                            }
                                            else
                                                $.jBox.info("删除失败,请联系管理员", '系统提示');
                                        }
                                );
                            }
                        }
                    });
                }
            }, {buttonsFocus: 1});
            top.$('.jbox-body .jbox-icon').css('top', '55px');
            return false;
        }

        /**
         判断批量操作是否勾选数据
         */
        function confirmBatchIsNull(mess, sta) {
            if ($("#contentTable").find("input[name='activityId']:checked").length != 0) {
                if (sta == 'off') {
                    confirmBatchOff(mess);
                } else if (sta == 'del') {
                    confirmBatchDel(mess);
                }
            } else {
                $.jBox.info('未选择产品', '系统提示');
            }
        }

        // 批量删除确认对话框
        function confirmBatchDel(mess) {
            top.$.jBox.confirm(mess, '系统提示', function (v) {
                if (v == 'ok') {
                    loading('正在提交，请稍等...');
                    $("#searchForm").attr("action", "${ctx}/activity/manager/batchdel/" + activityIds + "/${activityKind}");
                    $("#searchForm").submit();
                }
            }, {buttonsFocus: 1});
            top.$('.jbox-body .jbox-icon').css('top', '55px');
            return false;
        }

        // 批量下架确认对话框
        function confirmBatchOff(mess) {
            top.$.jBox.confirm(mess, '系统提示', function (v) {
                if (v == 'ok') {
                    loading('正在提交，请稍等...');
                    $("#searchForm").attr("action", "${ctx}/activity/manager/batchoff/" + activityIds + "/${activityKind}");
                    $("#searchForm").submit();
                }
            }, {buttonsFocus: 1});
            top.$('.jbox-body .jbox-icon').css('top', '55px');
            return false;
        }

        //删除某团期
        function confirmxDel(mess, proId) {
            top.$.jBox.confirm(mess, '系统提示', function (v) {
                if (v == 'ok') {
                    loading('正在提交，请稍等...');
                    $("#searchForm").attr("action", "${ctx}/activity/manager/del/" + proId + "/1/${activityKind}");
                    $("#searchForm").submit();
                }
            }, {buttonsFocus: 1});
            top.$('.jbox-body .jbox-icon').css('top', '55px');
            return false;
        }
        //设定定价策略
        function setPricingStrategy(obj, groupid, activityid, activityName, isCheck) {
            $.ajax({
                type: "post",
                url: ctx + "/pricingStrategy/manager/checkIsExists",
                data: {groupId: groupid, activityId: activityid},
                success: function (msg) {
                    openPricingStrategy(msg.flag, obj, groupid, activityid, activityName, '/pricingStrategy/manager/editPricingStrategy/' + groupid + "/" + activityid, "/pricingStrategy/manager/addActivityStrategy");
                }
            })

        }

        function openPricingStrategy(isCheck, obj, groupid, activityid, activityName, url, addUrl) {
            var ctx = $('#ctx').val();
            //新建一个隐藏的div，用来保存文件上传后返回的数据
            if ($(obj).parent().find("#hiddenParm").length == 0) {
                $(obj).parent().append('<div  style="display: none" id="hiddenParm"></div>');
            }
            $(obj).addClass("clickBtn");
            $.jBox("iframe:" + ctx + url, {
                title: activityName,
                width: 773,
                height: 400,
                buttons: {'提交': true, '取消': false},
                persistent: true,
                //loaded: function (h) {},
                submit: function (v, h, f) {
                    //获取价格策略值
                    var adultPri = $("#adultPri").val();
                    var childrenPri = $("#childrenPri").val();
                    var specialPri = $("#specialPri").val();
                    $("#hiddenParm").remove();
                    $(".clickBtn", window.parent.document).removeClass("clickBtn");
                    if (!v) {
                        return true;
                    } else if (isCheck) {
                        $.jBox.confirm("已存在QUAUQ价，是否替换", "提示", function (v, h, f) {
                            if (v == 'ok') {
                                loading("正在提交，请稍等...");
                                if (v) {
                                    $.ajax({
                                        type: "POST",
                                        url: ctx + addUrl,
                                        data: {
                                            groupid: groupid,
                                            activityid: activityid,
                                            adultPri: adultPri,
                                            childrenPri: childrenPri,
                                            specialPri: specialPri
                                        },
                                        success: function (data) {

                                            if ('ok' == data.flag) {
                                                if (adultPri == "" && childrenPri == "" && specialPri == "") {
                                                    $(obj).parent().parent().find('.g-w').removeClass('withdraw');
                                                    $(obj).parent().parent().find('.g-w').removeClass('grounding');

                                                    $(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover');
                                                    $(obj).parent().parent().find('.grounding-one').html('');
                                                    $(obj).parent().parent().find('input[type="checkbox"]').css('display', 'none');
                                                    $(obj).parent().parent().parent().find('.stack-btn').hide();
                                                    $(obj).parent().parent().parent().find('.morePrice-parent').css('display', 'none');
                                                    $(".jbox-tip").remove();
                                                } else {
                                                    //if($(obj).parent().parent().find('.g-w').hasClass('withdraw')){
                                                    $.jBox.tip('上架成功！');
                                                    //}
                                                    //loading('正在提交，请稍等...');
                                                    //window.location.reload();
                                                    //var priningStrategyNew = data.priningStrategyNew;
                                                    //var retailPrice = data.retailPrice;
                                                    $(obj).parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
                                                    $(obj).parent().parent().find('.grounding-one').html();
                                                    $(obj).parent().parent().find('.grounding-one').html('已上架旅游交易系统');
                                                    $(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
                                                    $(obj).parent().parent().find('input[type="checkbox"]').css('display', 'inline').attr("disabled", "disabled");
                                                    $(obj).parent().parent().parent().find('.stack-btn').hide();
                                                    $(obj).parent().parent().parent().find('.morePrice-parent').css('display', 'block');
                                                    //$tr.find('.morePrice-parent').show();
                                                    //$(obj).parent().find("td[name='adultPricingStrategy']").html();
                                                    //											$(obj).parent().find("td[name='adultPricingStrategy']").html(priningStrategyNew.adultPricingStrategy);
                                                    //											$(obj).parent().find("td[name='childrenPricingStrategy']").html(priningStrategyNew.childrenPricingStrategy);//priningStrategy.adultPricingStrategy
                                                    //											$(obj).parent().find("td[name='specialPricingStrategy']").html(priningStrategyNew.specialPricingStrategy);
                                                    //											$(obj).parent().find("td[name='adultRetailPrice']").html(new Number(retailPrice.adultRetailPrice).toFixed(2));
                                                    //											$(obj).parent().find("td[name='childRetailPrice']").html(new Number(retailPrice.childRetailPrice).toFixed(2));
                                                    //											$(obj).parent().find("td[name='specialRetailPrice']").html(new Number(retailPrice.specialRetailPrice).toFixed(2));
                                                }
                                                $(".jbox-body").remove();
                                            } else {
                                                $.jBox.confirm("设置失败", "提示", function (v, h, f) {
                                                    if (v == 'ok') {
                                                        return;
                                                    }
                                                    if (v == 'cancel') {
                                                        return;
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                return;
                            }
                            if (v == 'cancel') {
                                flag = true;
                                return true;
                            }
                        });
                        return false;
                    } else {
                        if (v) {
                            loading("正在提交，请稍等...");
                            $.ajax({
                                type: "POST",
                                url: ctx + addUrl,
                                data: {
                                    groupid: groupid,
                                    activityid: activityid,
                                    adultPri: adultPri,
                                    childrenPri: childrenPri,
                                    specialPri: specialPri
                                },
                                success: function (data) {
                                    if ('ok' == data.flag) {
                                        if (adultPri == "" && childrenPri == "" && specialPri == "") {
                                            $(obj).parent().parent().find('.g-w').removeClass('withdraw');
                                            $(obj).parent().parent().find('.g-w').removeClass('grounding');
                                            $(obj).parent().parent().find('.grounding-one').html();
                                            $(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover');
                                            $(obj).parent().parent().find('input[type="checkbox"]').css('display', 'none');
                                            $(obj).parent().parent().parent().find('.stack-btn').hide();
                                            $(obj).parent().parent().parent().find('.morePrice-parent').css('display', 'none');
                                            $(".jbox-tip").remove();
                                        } else {
                                            //if($(obj).parent().parent().find('.g-w').hasClass('withdraw')){
                                            $.jBox.tip('上架成功！');
                                            //}
                                            //var priningStrategy = data.priningStrategyNew;
                                            //var retailPrice = data.retailPrice;
                                            //loading('正在提交，请稍等...');
                                            //window.location.reload();
                                            $(obj).parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
                                            $(obj).parent().parent().find('.grounding-one').html();
                                            $(obj).parent().parent().find('.grounding-one').html('已上架旅游交易系统');
                                            $(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
                                            $(obj).parent().parent().find('input[type="checkbox"]').css('display', 'inline').attr("disabled", "disabled");
                                            $(obj).parent().parent().parent().find('.stack-btn').hide();
                                            $(obj).parent().parent().parent().find('.morePrice-parent').css('display', 'block');
                                            //$tr.find('.morePrice-parent').show();
                                            //$(obj).parent().find("td[name='adultPricingStrategy']").html();
                                            //									$(obj).parent().find("td[name='adultPricingStrategy']").html(priningStrategy.adultPricingStrategy);
                                            //									$(obj).parent().find("td[name='childrenPricingStrategy']").html(priningStrategy.childrenPricingStrategy);//priningStrategy.adultPricingStrategy
                                            //									$(obj).parent().find("td[name='specialPricingStrategy']").html(priningStrategy.specialPricingStrategy);
                                            //									$(obj).parent().find("td[name='adultRetailPrice']").html(new Number(retailPrice.adultRetailPrice).toFixed(2));
                                            //									$(obj).parent().find("td[name='childRetailPrice']").html(new Number(retailPrice.childRetailPrice).toFixed(2));
                                            //									$(obj).parent().find("td[name='specialRetailPrice']").html(new Number(retailPrice.specialRetailPrice).toFixed(2));
                                        }
                                        window.parent.jBox.close();

                                    } else {
                                        $.jBox.confirm("设置失败", "提示", function (v, h, f) {
                                            if (v == 'ok') {
                                                return;
                                            }
                                            if (v == 'cancel') {
                                                return;
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            });
            $("#jbox-content").css("overflow", "hidden");
            $("#jbox-content").css("overflow-y", "hidden");
        }

        /*//518 修改前判断是否上架 如上架则弹出弹框
        function modify(obj) {
            if ($(obj).parent().parent().find('.g-w').hasClass('grounding')) {
                var groupIdsBuffer = groupId;
                $.jBox.info("该团期已上架旅游交易预订系统，如需修改则产品将下架，是否确认修改？", "提示", {
                    buttons: {"关闭": "0", "确认": "1"},
                    submit: function (v, h, f) {
                        if (v == '1') {
                            $(obj).parent().parent().find('.g-w').removeClass('grounding').addClass('withdraw');
                            $(obj).parent().parent().find('.grounding-one').removeClass('grounding-hover').addClass('withdraw-hover');
                            $(obj).parent().parent().find('.grounding-one').html();
                            $(obj).parent().parent().find('.grounding-one').html('已下架旅游交易系统');
                            $(obj).parent().parent().find('input[type="checkbox"]').removeAttr("disabled");
                            $(obj).parent().find('.morePrice').css('display', 'none');
                            //$(obj).parent().find('.stack-btn').show()
                            //$(obj).parent().find('.setting-btn').hide();
                            $.ajax({
                                type: "POST",
                                url: "${ctx}/activity/manager/confimIsT1Off",
                                data: {groupIdsBuffer: groupIdsBuffer},
                                success: function (data) {
                                    if (data.flag) {
                                        $.jBox.tip('操作成功', 'success');
                                        modgroup(obj);
                                    } else {
                                        $.jBox.tip('操作失败,原因:' + data.msg, 'error');
                                        return false;
                                    }
                                }
                            })
                        }
                        if (v == '0') {
                            return true;
                        }
                    }
                })
            } else {
                modgroup(obj);
            }
        }*/

        //修改产品-----------------
        function modgroup(obj) {
            //要修改的元素的团号span
            var $tr = $(obj).parent().parent();
            //获取团号span
            var span4GroupCode = $tr.find('span[name="groupCode"]');
            //要修改的元素的input文本框
            var text4GroupCode = $tr.find('input[name="groupCode"]');
            //批发商的uuid是否为优加
            var uuidTemp = '${uuid4ManualModifyGroupcode}';
            //隐藏所有的span，以便切换到input
            $tr.find("span:not(.houseAndType)").hide();
            //隐藏设定定价策略
            $tr.find('.setting-btn').hide();
            //隐藏平台上架
            $tr.find('.stack-btn').hide();
            //隐藏展开价格方案
            //$tr.find('.expandPriceJson').hide();
            $tr.find('.more-op-style').hide();
            //var text44Modified=$("#"+$(groupid).val());
            //保存按钮出现
            $tr.find('a[name="savebtn"]').show();
            //删除按钮隐藏
            //$tr.find('a[name="delbtn"]').hide();
            //取消按钮出现
            $tr.find('a[name="cancelbtn"]').show();
            //如果为上架状态，则出现价格表
            $tr.find('.morePrice-parent').hide();
            //隐藏修改按钮
            $(obj).hide();
            //展开备注
            //$tr.find('.expandNotes').hide();
            //$tr.find('.groupNoteTipImg').hide();

            //$(obj).parent().parent().find("span").eq(0).show();
            //518 点击取消后checkbox和上下架标志隐藏 S
            $tr.find("input[type='checkbox']").hide();
            //$tr.find(".withdraw-relative .g-w").hide();
            //$tr.find(".mp-floating span").show();
            //518 点击取消后checkbox和上下架标志隐藏 E
            $tr.find("span[class='rm']").show();

            $tr.find("input[type='text']").css("display", "inline-block");
            // 对应需求 c460 添加   groupCodeRuleDT==0
            //if (uuidTemp == 'f5c8969ee6b845bcbeb5c2b40bac3a23' || uuidTemp == '7a81c5d777a811e5bc1e000c29cf2586' || uuidTemp == '5c05dfc65cd24c239cd1528e03965021' || (groupCodeRuleDT == 0 && uuidTemp != '7a8177e377a811e5bc1e000c29cf2586' && uuidTemp != 'ed88f3507ba0422b859e6d7e62161b00' && uuidTemp != 'f5c8969ee6b845bcbeb5c2b40bac3a23' && uuidTemp != '58a27feeab3944378b266aff05b627d2' && uuidTemp != '7a81a26b77a811e5bc1e000c29cf2586' && uuidTemp != '7a45838277a811e5bc1e000c29cf2586')) { //为优加、起航假期 则可手输
            if (uuidTemp == 'f5c8969ee6b845bcbeb5c2b40bac3a23' ||    //懿洋假期
                    uuidTemp == '7a81c5d777a811e5bc1e000c29cf2586' ||   //优加国际
                    uuidTemp == '7a81a26b77a811e5bc1e000c29cf2586' ||   //拉美图
                    uuidTemp == '5c05dfc65cd24c239cd1528e03965021' ||   //起航假期
                    uuidTemp == '58a27feeab3944378b266aff05b627d2' ||   //日信观光
                    uuidTemp == '1d4462b514a84ee2893c551a355a82d2' ||   //非常国际
                    uuidTemp == '7a45838277a811e5bc1e000c29cf2586' ||   //非常国际
                    (groupCodeRuleDT == 0 &&
                    uuidTemp != '7a8177e377a811e5bc1e000c29cf2586' &&
                    uuidTemp != 'ed88f3507ba0422b859e6d7e62161b00' &&
                    uuidTemp != 'f5c8969ee6b845bcbeb5c2b40bac3a23' &&
                    uuidTemp != '58a27feeab3944378b266aff05b627d2' &&
                    uuidTemp != '7a81a26b77a811e5bc1e000c29cf2586')) { //为优加、起航假期 则可手输
                //隐藏团号span,显示团号文本
            } else {
                //隐藏input，显示span
                span4GroupCode.show();
                text4GroupCode.hide();
            }
            //*0258需求,发票税:针对懿洋假期-tgy-s,显示文本框后的%号(如果table结构发生变化,则也需要相应改变),单团,*//
            $(obj).parent().prev().prev().find("input").next().css("display", "inline-block");
            //*258需求,发票税:针对懿洋假期-tgy-e*//
            $tr.find("input[type='text']").attr("disabled", false);
            $tr.find("input[type='checkbox'][flag*='mod']").css("display", "block");
            $tr.find("input[type='checkbox'][flag*='show']").css("display", "none");
            $tr.find("input[type='checkbox'][flag*='mod']").attr("disabled", false);
            var checkedFlag = $(obj).parent().parent().find("input[type='checkbox'][flag*='show']").attr("checked");
            if (checkedFlag == undefined | checkedFlag == null | checkedFlag == '') {
                $tr.find("input[type='checkbox'][flag*='mod']").attr("checked", false);
            } else if (checkedFlag == 'checked') {
                $tr.find("input[type='checkbox'][flag*='mod']").attr("checked", "checked");
            }
            //$tr.find("span[class='houseAndType']").css("display", "block");
            $tr.next().show();
            //设置备注框
            var remark = $tr.next().find('[name="groupNoteContent"]').text();
            $tr.next().find('[name="groupNote"]').val(remark);
            $tr.next().find('div:first').hide().next().show();
            $tr.find('span[class="price-title"]').show();
            $tr.find('span.rm').show();
            $tr.find('span.price-content').show();
        }

        /**
         * 取消修改操作
         */
        function cancelgroup(obj) {
            var $tr = $(obj).parent().parent();
            $tr.find('.more-op-style').show();
            //修改按钮出现
            $tr.find('a[name="modbtn"]').show();
            //删除按钮出现
            //$tr.find('a[name="delbtn"]').show();
            //保存按钮隐藏
            $tr.find('a[name="savebtn"]').hide();
            //取消按钮隐藏
            $(obj).hide();
            //判断该开发商是否具有t1上架权限，如果有上架权限，则显示以下按钮
            if ('${shelfRightsStatus}' == 0) {
                //设定定价策略按钮
                $tr.find('.setting-btn').show();
                //518如果状态为已下架，则显示平台上架按钮
                if ($tr.find('.g-w').hasClass('withdraw')) {
                    //平台上架按钮出现
                    $tr.find('.stack-btn').show();
                    //复选框出现
                    $tr.find("input[type='checkbox']").show();

                }
                if ($tr.find('.g-w').hasClass('grounding')) {
                    //如果为上架状态，则出现价格表
                    $tr.find('.morePrice-parent').show();
                    //价格表出现
                    //$tr.find('a[name="priceTable"]').show();
                    $tr.find("input[type='checkbox']").show();
                }
            }

            $tr.find("span").show();
            $tr.find("input[type='text']").css("display", "none");
            //*0258需求,发票税:针对懿洋假期-tgy-s,隐藏文本框后的%号(如果table结构发生变化,则也需要相应改变),单团,*//
            $(obj).parent().prev().prev().find("input").next().css("display", "none");
            //*258需求,发票税:针对懿洋假期-tgy-e*//
            $tr.find("input[type='text']").attr("disabled", true);
            $tr.find("input[type='checkbox'][flag*='mod']").css("display", "none");
            $tr.find("input[type='checkbox'][flag*='mod']").attr("disabled", true);
            $tr.find("input[type='checkbox'][flag*='show']").css("display", "block");
            //删除错误校验信息
            $(obj).parent().parent().find("label", ".error").remove();
            //设置备注状态
            $tr.find('.expandNotes').text('展开备注');
            $tr.find('.expandNotes').show();
            //展开价格方案
            //$tr.find('.expandPriceJson').show();
            var remark = $tr.next().find('.groupNoteContent').text();
            if (remark == null || remark == 'undefined' || remark == '') {
                $tr.find('.groupNoteTipImg').hide();
            } else {
                $tr.find('.groupNoteTipImg').show();
            }
            $tr.next().hide();
            $tr.next().find('div:first').show().next().hide();
        }

        function withdraw(tr) {
            $(tr).find("input[type='checkbox']").removeAttr("disabled").show();
            $(tr).find(".withdraw-relative .g-w").removeClass("grounding").addClass("withdraw").show();
            $(tr).find(".withdraw-relative .g-w").prev().removeClass("grounding-hover").addClass("withdraw-hover").text("已下架旅游交易系统");
            $(tr).find(".stack-btn").show();
        }

        function stack(tr) {
            $(tr).find("input[type='checkbox']").attr("disabled", "ture").show();
            $(tr).find(".withdraw-relative .g-w").addClass("grounding").removeClass("withdraw").show();
            $(tr).find(".withdraw-relative .g-w").prev().addClass("grounding-hover").removeClass("withdraw-hover").text("已上架旅游交易系统");
            $(tr).find("[id^='modbtn']").addClass("stacked-mod");
            $(tr).find(".stack-btn").hide();
        }


        //下载文件
        function downloads(docids, zipname, acitivityName, iszip) {
            if (iszip) {
                //var zipname = activitySerNum;
                window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + docids + "/" + zipname)));
            }

            else
                window.open("${ctx}/sys/docinfo/download/" + docid);
        }

        //产品修改,t1t2-v2增加修改链接区分
        function productModify(proId) {
            $("#searchForm").attr("action", "${ctx}/activity/manager/mod/" + proId + "/0");
            $("#searchForm").submit();
        }

        //占位
        function occupied(id, srcActivityId, payMode) {
            if (payMode == "1") {
                //dingj zhanwei 
                window.open("${ctx}/orderCommon/manage/showforModify?type=2&productId=" + srcActivityId + "&productGroupId=" + id);
            } else if (payMode == "2") {
                //zanwei
                window.open("${ctx}/orderCommon/manage/showforModify?type=3&productId=" + srcActivityId + "&productGroupId=" + id);
            }
        }

        //预订
        function reserveOrder(id, srcActivityId) {
            window.open("${ctx}/orderCommon/manage/showforModify?type=1&productId=" + srcActivityId + "&productGroupId=" + id);
        }

        function getCurDate() {
            var curDate = new Date();
            return curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + (curDate.getDate() + 1);
        }

        //控制截团时间
        function takeOrderOpenDate(obj) {
            var groupOD = $('#' + obj).parent().parent().find("input[name='groupOpenDate']").eq(0).val();
            return groupOD;
        }
        function takeModVisaDate(obj) {
            var groupOD = $('#' + obj).parent().parent().find("input[name='groupOpenDate']").eq(0).val();
            return groupOD;
        }
        function comparePositionMod(obj) {
            var plan = $(obj).val();
            $(obj).parent().next().find("input").val(plan);
            $(obj).parent().next().find("input").focus();
            $(obj).parent().next().find("input").blur();
        }

        //全选操作
        function checkall(obj) {
            if ($(obj).attr("checked")) {
                $("input[name='allChk']").attr("checked", 'true');
                $("input[name='activityId']").attr("checked", 'true');
                $("input[name='activityId']:checked").each(function (i, a) {
                    var arr = activityIds.split(",");
                    if (arr.indexOf(a.value) < 0) {
                        activityIds = activityIds + a.value + ",";
                    }
                });
            }
            else {
                $("input[name='allChk']").removeAttr("checked");
                $("input[name='activityId']").removeAttr("checked");
                $("input[name='activityId']").each(function (i, a) {
                    var arr = activityIds.split(",");
                    if (arr.indexOf(a.value) >= 0) {
                        activityIds = activityIds.replace(a.value + ",", "");
                    }
                });
            }
            $("#activityIds").val(activityIds);
        }

        function idcheckchg(obj) {
            var value = $(obj).val();
            if ($(obj).attr("checked")) {
                if (activityIds.indexOf($(obj).val()) < 0) {
                    activityIds = activityIds + $(obj).val() + ",";
                }
            }
            else {
                if ($("input[name='allChk']").attr("checked"))
                    $("input[name='allChk']").removeAttr("checked");
                if (activityIds.indexOf($(obj).val()) >= 0) {

                    activityIds = activityIds.replace($(obj).val() + ",", "");
                }
            }
            $("#activityIds").val(activityIds);

        }

        //排序
        function sortby(sortBy, obj) {
            var temporderBy = $("#orderBy").val();

            if (temporderBy.match(sortBy)) {
                sortBy = temporderBy;
                if (sortBy.match(/ASC/g)) {
                    sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
                } else {
                    sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
                }
            } else {
                sortBy = sortBy + " DESC";
            }
            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }

        //下架产品
        function downProduct(activityId) {

            $.jBox.confirm("确定要下架该产品吗？", "提示", function (v, h, f) {
                if (v == 'ok') {
                    $("#searchForm").attr("action", "${ctx}/activity/manager/batchoff/" + activityId + "/${activityKind}");
                    $("#searchForm").submit();
                } else if (v == 'cancel') {

                }
            });
            top.$('.jbox-body .jbox-icon').css('top', '55px');

        }


        //导出团期中关于游客信息
        function exportExcel(groupId, status) {
            var group_id = "#groupId" + groupId;
            var groupCode = $(group_id).html();
            $.ajax({
                type: "POST",
                url: "${ctx}/activity/manager/existExportData",
                dataType: "json",
                cache: false,
                data: {groupId: groupId, status: status},
                success: function (result) {
                    var data = eval(result);
                    if (data && data[0].flag == "true") {
                        $("#groupId").val(groupId);
                        $("#groupCode").val(groupCode);
                        $("#exportForm").submit();
                    } else {
                        var tips = data[0].warning;
                        top.$.jBox.info(tips, "警告", {width: 250, showType: "slide", icon: "info", draggable: "true"});
                        top.$('.jbox-body .jbox-icon').css('top', '55px');
                    }
                }
            });
        }

        function getDepartment(departmentId) {
            $("#departmentId").val(departmentId);
            $("#searchForm").submit();
        }

        function chooseActivityKinds() {
            var _select = $("<select id='activityKinds'></select>")
                    .append($('<option value=\"2"\>散拼</option><option value=\"1"\>单团</option><option value=\"3"\>游学</option><option value=\"4"\>大客户</option><option value=\"5"\>自由行</option>'));

            var $div = $("<div id='chooseQd' class=\"tanchukuang\"></div>")
                    .append($('<div class="add_allactivity choseAgent"></div>')
                            .append($("<label>类型选择：</label>")).append(_select))
                    .append('<div class="ydBtn"><div class="btn btn-primary ydbz_x" onclick="javascript:window.location.href=\'${ctx}/activity/manager/form?kind=\'+$(\'#activityKinds\').val();$(\'.jbox-close\').click();">开始发布</div>');
            var html = $div.html();
            $.jBox(html, {title: "发布-产品类型选择", buttons: {}, height: 220, width: 550});
            $("#activityKinds").children("[value='${activityKind}']").attr("selected", true);

        }

        //--071需求校验签证国家的字符串长度函数
        function checkVisaCountrylen(v1, v2) {
            //0071-进来先清空提示span-防止bug-s
            $("[name='span4visaCountry']").empty();
            //0071-进来先清空提示span-防止bug-e
            var arrVisaCountry = $("#visaCountry" + (v1 + "".concat(v2)));
            if (arrVisaCountry.val().length > 50) {
                $("#visaCountry" + v1 + "".concat(v2)).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
                top.$.jBox.info("请先修改完错误再提交", "警告");
                top.$('.jbox-body .jbox-icon').css('top', '55px');
                return;
            }
        }

        /**
         * 团号超过50字提醒
         * @param {} obj
         */
        var flag = 10;
        function validateLong(obj) {
            replaceStr(obj);
            if ($(obj).val().length <= 49) {
                flag = 10;
            }
            if ($(obj).val().length >= 50) {
                if ($(obj).val().length == 50 && flag == 10) {
                    //$.jBox.tip("团号只能输入50个字符","true");
                    flag++;
                } else {
                    $.jBox.tip("团号超过50个字符，请修改", "error");
                }
                return false;
            }
        }

        //189  优加
        function openGroupLibPage() {
            //debugger;
            //groupcodelibtype = 0 为机票
            $.jBox("iframe:" + g_context_url + "/activity/groupcodelibrary/toGroupcodeLibraryBox?groupcodelibtype=" +${activityKind}, {  //groupcodelibtype =7 为机票
                title: "团号库", buttons: {'关闭': 1}, height: 680, width: 680, persistent: true
            }).find("#jbox-content").css("overflow", "hidden");
        }


        /**
         *
         * @param arr:array operated
         * @param type:expected max,min
         * @returns get max/min value in specified array or cosole log error
         */
        function getMaxMinNum(arr, type) {
            if (type == '' || type == null || type == 'undefined') {
                //console.log("Type is undefined.Please specified!");
                return false;
            }
            if ('max' == type) {
                return Math.max.apply(null, arr);
            }
            if ('min' == type) {
                return Math.min.apply(null, arr);
            }
        }

    </script>

    <style type="text/css">


        #contentTable th {
            height: 40px;
            border-top: 1px solid #CCC;
        }

        #teamTable {
            border: 1px solid #CCC;
        }

        .groupNoteTipImg {
            display: inline-block;
            width: 12px;
            height: 12px;
            background-image: url("${ctxStatic}/images/order_s3.png");
            background-repeat: no-repeat;
            background-position: 0px center;
            margin: 4px 0px 0px 5px;
            line-height: 8px;
            vertical-align: top;
        }
    </style>

    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css"/>
    <script type="text/javascript">
        var $ctx = '${ctx}';
    </script>
    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <%--<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>--%>
    <%--<script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>--%>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/store/billboard.js"></script>


</head>
<div>
<page:applyDecorator name="activity_op_head">
    <page:param name="current">online</page:param>
</page:applyDecorator>

<!-- 签证公告展示 -->
<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp" %>

<!-- 需求223 看板 -->
<div class="activitylist_bodyer_right_team_co_bgcolor">
    <form:form id="searchForm" modelAttribute="travelActivity"
               action="${ctx}/activity/manager/list/${travelActivity.activityStatus }/${activityKind}?newflag=newflag"
               method="post">
        <input id="ctx" type="hidden" value="${ctx}"/>
        <input id="activityStatus" type="hidden" name="activityStatus" value="${travelActivity.activityStatus }"/>
        <input id="activityIds" type="hidden" name="activityIds" value="${activityIds }"/>
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="departmentId" name="departmentId" type="hidden" value="${departmentId }"/>
        <input id="shelfRightsStatus" name="shelfRightsStatus" type="hidden" value="${shelfRightsStatus}">
        <div class="activitylist_bodyer_right_team_co">

            <div class="activitylist_bodyer_right_team_co2 pr">
                <input class="txtPro searchInput inputTxt"
                                         id="wholeSalerKey" name="wholeSalerKey"
                                         value="${travelActivity.acitivityName }"
                                         placeholder="输入产品名称、团号，支持模糊匹配"/>
            </div>
            <a class="zksx">筛选</a>
            <div class="form_submit">
                <input class="btn btn-primary ydbz_x" type="submit" value="搜索"/>
                <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x"/>
                <!--  团期产品  非常国际、优加 、起航假期  添加团号库 -->
                <!--  对应需求号   c460   添加    fns:getUser().company.groupCodeRuleDT eq 1 -->
                <c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleDT == 0}">
                    <c:if test="${fns:getUser().company.isNeedGroupCode eq 1}">
                        <input class="btn btn-primary " type="button" onclick="openGroupLibPage()" value="团号库"/>
                    </c:if>
                </c:if>
            </div>
            <shiro:hasPermission name="${shiroType}Product:operation:form">
                <p class="main-right-topbutt"><a class="primary"
                        onclick="javascript:window.location.href='${ctx}/activity/manager/form?kind=${activityKind}'">发布新产品</a></p>
            </shiro:hasPermission>


            <div class="ydxbd">
                <span></span>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">销售：</div>
                    <input id="estimatePriceRecordUserName" name="estimatePriceRecordUserName" class="inputTxt"
                           value="${travelActivity.estimatePriceRecord.userName }">
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">出发地：</div>
                    <div class="selectStyle">
                        <form:select id="fromArea" path="fromArea" itemValue="key" itemLabel="value">
                            <form:option value="">不限</form:option>
                            <form:options items="${fromAreas}"/>
                        </form:select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">目的地：</div>
                    <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}"
                                     labelName="targetAreaNameList" labelValue="${targetAreaNames}"
                                     title="区域" url="/activity/manager/filterTreeData1?kind=${activityKind}"
                                     checked="true"/>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">出团日期：</label>
                    <input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate"
                                               value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'
                                               onFocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                                               readonly/>
                    <span style="font-size:12px; font-family:'宋体';"> 至</span>
                    <input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate"
                           value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>'
                           onClick="WdatePicker()" readonly/>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">计调：</div>
                    <input type="text" id="createName" name="createName" class="inputTxt" value="${travelActivity.createBy.name }">
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">领队：</div>
                    <input type="text" id="groupLead" name="groupLead" class="inputTxt" value="${travelActivity.groupLead }">
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">航空公司：</div>
                    <div class="selectStyle">
                        <form:select id="trafficName" path="activityAirTicket.airlines">
                            <form:option value="">不限</form:option>
                            <form:options items="${airlines}" itemValue="airlineCode" itemLabel="airlineName"/>
                        </form:select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co4 sCurrency">
                    <label class="activitylist_team_co3_text">同行价格：</label>
                    <div class="selectStyle">
                    <select id="selectCurrencyType" name="currencyType">
                    <c:forEach items="${currencyList}" var="currency" varStatus="s">
                        <c:if test="${currency.id != '1'}">
                            <option value="${currency.id}">${currency.currencyName}</option>
                        </c:if>
                    </c:forEach>
                </select>
                    </div>
                    <input type="text" id="settlementAdultPriceStart" class="inputTxt" name="settlementAdultPriceStart"
                           value="${settlementAdultPriceStart }"/>
                    <span style="font-size:12px;font-family:'宋体';"> 至</span>
                    <input type="text" id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd"
                           value="${settlementAdultPriceEnd }"/>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text" for="spinner" class="fl" style="line-height:28px;">行程天数：
                    </div>
                    <input id="activityDuration" class="spinner" maxlength="3" name="activityDuration"
                           value="${param.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')"
                           onkeyup="this.value=this.value.replace(/\D/g,'')">
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">旅游类型：</div>
                    <div class="selectStyle">
                        <form:select path="travelTypeId" itemValue="key" itemLabel="value" id="travelTypeId">
                            <form:option value="">不限</form:option>
                            <form:options items="${travelTypes}"/>
                        </form:select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">产品系列：</div>
                    <div class="selectStyle">
                        <form:select path="activityLevelId" itemValue="key" itemLabel="value" id="productser">
                            <form:option value="">不限</form:option>
                            <form:options items="${productLevels}"/>
                        </form:select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">产品类型：</label>
                    <div class="selectStyle">
                        <form:select path="activityTypeId" itemValue="key" itemLabel="value" id="productType">
                            <form:option value="">不限</form:option>
                            <form:options items="${productTypes}"/>
                        </form:select>
                    </div>
                </div>
                <!-- 518 添加上架状态筛选条件 S -->
                <c:if test="${shelfRightsStatus eq 0 }"> <!-- 如果供应商是禁用状态 -->
                    <div class="activitylist_bodyer_right_team_co1">
                        <div class="activitylist_team_co3_text">平台上架状态：</div>
                        <div class="selectStyle">
                            <select id="groundingStatus" name="groundingStatus">
                                <option value="" <c:if test="${'' eq groundingStatus }">selected="selected"</c:if>>全部
                                </option>
                                <option value="1" <c:if test="${1 eq groundingStatus }">selected="selected"</c:if>>未上架
                                </option>
                                <option value="2" <c:if test="${2 eq groundingStatus }">selected="selected"</c:if>>已上架
                                </option>
                                <option value="3" <c:if test="${3 eq groundingStatus }">selected="selected"</c:if>>已下架
                                </option>
                            </select>
                        </div>
                        <!-- 518 添加上架状态筛选条件 E -->
                    </div>
                </c:if>
            </div>

        </div>
    </form:form>



    <!-- 部门分区 -->
    <div class="supplierLine"
         <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
        <%--<a <c:if test="${empty departmentId}">class="select"</c:if> href="javascript:void(0)" onclick="getDepartment('');">全部产品</a>--%>
        <c:forEach var="department" items="${showAreaList}" varStatus="status">
            <c:choose>
                <c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
                    <a class="select" href="javascript:void(0)"
                       onclick="getDepartment('${department.id}');">${department.name}</a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>

    <c:if test="${fn:length(page.list) ne 0}">
        <div class="activitylist_bodyer_right_team_co_paixu">
            <div class="activitylist_paixu">
                <div class="activitylist_paixu_left">
                    <ul>
                        <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
                        <li class="activitylist_paixu_left_biankuang liupdateDate"><a
                                onClick="sortby('updateDate',this)">更新时间</a></li>
                        <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a
                                onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
                        <c:if test="${activityKind eq '2' or activityKind eq '10'}">
                            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a
                                    onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
                        </c:if>
                        <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a
                                onClick="sortby('groupOpenDate',this)">出团日期</a></li>
                    </ul>
                </div>
                <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                <div class="kong"></div>
            </div>
        </div>
    </c:if>

    <c:if test="${fn:length(page.list) ne 0}">
    <table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
        <tr>
            <th width="4%">序号</th>
            <th class="table_borderLeftN" width="4%">全选<input name="allChk" type="checkbox" onclick="checkall(this)"/>
            </th>
            <th width="14%">产品名称</th>
            <th width="8%">计调</th>
            <th width="8%">出发城市</th>
            <th width="6%">航空</th>
            <th width="8%">签证</th>
            <th width="16%">最近出团日期</th>
            <th width="10%">成人同行价</th>
            <th width="10%">成人直客价</th>
            <th width="4%">操作</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${page.list}" var="activity" varStatus="s">

            <c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
            <tr id="parent${s.count}">
                <td>${s.count}</td>
                <td class="table_borderLeftN"><input type="checkbox" name="activityId" value="${activity.id }"
                                                     <c:if test="${fn:contains(activityIds,fn:trim(activity.id))}">checked="checked"</c:if>
                                                     onclick="idcheckchg(this)"/><br/><br/></td>

                <td class="activity_name_td">
                    <a href="javascript:void(0)"
                       onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>

                </td>
                <td>${activity.createBy.name}</td>
                <td>
                        ${activity.fromAreaName}
                </td>

                <td align="center">
                    <label class="qtip" title="${activity.trafficNameDesc}">
                        <c:set var="fligthInfoStr" value=""></c:set>
                        <c:forEach items="${activity.activityAirTicket.flightInfos }" var="fligthInfo">
                            <c:set var="fligthInfoStr"
                                   value="${fligthInfoStr }${fn:replace(fligthInfo.airlines,'-1','-')},"></c:set>
                        </c:forEach>
                            ${fligthInfoStr }
                    </label>
                </td>
                <td align="center">
                    <c:if test="${!empty visaMapList}">
                        <c:forEach items="${visaMapList}" var="visas">
                            <c:if test="${activity.id eq visas.srcActivityId}">
                                <a href="javascript:void(0)"
                                   onClick="downloads('${visas.docInfoId}','${fns:getCountryName(visas.countryId)}',null,true)">${fns:getCountryName(visas.countryId)}</a>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </td>

                <td id="groupdate${activity.id }" align="center" class="">
                    <div id="truedate"
                         <c:if test="${groupsize ne 0 }">style="display:block;" </c:if>
                             <c:if test="${groupsize == 0 }">style="display:none;"</c:if>>
							<span>
								<c:choose>
                                    <c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
                                    <c:when test="${empty activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
                                    <c:otherwise>${activity.groupOpenDate}至${activity.groupCloseDate}</c:otherwise>
                                </c:choose>
							</span><br>
                        <a id="close${s.count}" href="javascript:void(0)" class="team_a_click"
                           onClick="expand('#child${s.count}',this,${activity.id });"
                           onMouseenter="if($(this).html()=='全部团期'){$(this).html('展开全部团期')}"
                           onMouseleave="if($(this).html()=='展开全部团期'){$(this).html('全部团期')}">全部团期</a>
                    </div>

                    <div id="falsedate"
                         <c:if test="${groupsize ne 0 }">style="display:none;" </c:if>
                             <c:if test="${groupsize == 0 }">style="display:block;"</c:if>>
                        日期待定
                    </div>
                </td>

                <td id="settleadultprice${activity.id }" class="tr"><c:if
                        test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if
                        test="${activity.settlementAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,0,'mark')}
                    <span class="tdred fbold"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                value="${activity.settlementAdultPrice}"/></span>起</c:if>
                </td>

                <td id="suggestadultprice${activity.id }" class="tr"><c:if
                        test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if
                        test="${activity.suggestAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,1,'mark')}
                    <span class="tdblue fbold"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                 value="${activity.suggestAdultPrice}"/></span>起</c:if>
                </td>

                <td class="p0">
                    <dl class="handle">
                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                        <dd class="">
                            <p>
                                <span></span>
                                <a href="javascript:void(0)"
                                   onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">详情</a>
                                <shiro:hasPermission name="${shiroType}Product:operation:edit"><br/><a
                                        href="javascript:void(0)"
                                        onClick="productModify(${activity.id})">修改</a></shiro:hasPermission>
                                <a href="javascript:void(0)" onClick="downProduct(${activity.id})">下架</a>
                                <shiro:hasPermission name="${shiroType}Product:operation:delete"><br/><a
                                        href="javascript:void(0)"
                                        onClick="return confirmxDel('要删除该产品吗？', ${activity.id})">删除</a></shiro:hasPermission>
                            </p>
                        </dd>
                    </dl>
                </td>

            </tr>
            <tr id="child${s.count}" style="display:none" class="activity_team_top1">

                <td colspan="11" class="team_top" style="background-color:#d1e5f5;">

                    <form id="childform${s.count}">
                        <table id="teamTable" class="table activitylist_bodyer_table table-mod2-group"
                               style="margin:0 auto;">
                            <c:set var="colspanNum" value="3"></c:set>
                            <thead>

                            <tr>
                                <th width="120px">团号</th>

                                <th class="tc" width="100px">出团日期</th>
                                <th class="tc" width="100px">截团日期</th>
                                <th width="100px">签证国家</th>
                                <th width="100px" class="tc p0">资料截止日期</th>
                                <shiro:hasPermission name="price:project">
                                    <th class="tc" width="100px">酒店房型</th>
                                </shiro:hasPermission>

                                <c:set var="priceWidth" value="${12/colspanNum }"></c:set>
                                <th class="t-th2" width="300px">同行价</th>

                                <th class="t-th2" width="300px">直客价</th>

                                <th class="tr" width="75px">儿童最高人数</th>
                                <th class="tr" width="75px">特殊人群最高人数</th>

                                <th class="tc" width="100px">
                                    订金
                                </th>
                                <th class="tc" width="100px">
                                    单房差
                                </th>
                                <th class="tc" width="75px">
                                    预收
                                </th>
                                <th class="tc" width="75px">
                                    余位
                                </th>

                                <!-- 0258需求,发票税:针对懿洋假期-tgy-s-列表:单团,-->
                                <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
                                    <th class="tc" width="100px">发票税</th>
                                </c:if>
                                <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->

                                <th style="display:none" class="soldPayPosition" width="100px">切位</th>
                                <th class="tc" width="150px">操作</th>
                            </tr>

                            </thead>
                            <c:forEach items="${activity.activityGroupList}" var="group" varStatus="s2">
                                <tbody>
                                <tr>
                                    <td rowspan="1" style="padding: 0;">
                                        <!-- 518 添加平台上架标签 S -->
                                        <div class="withdraw-relative" id="groupId932">
                                            <c:if test="${shelfRightsStatus eq 0}">
                                                <c:choose>
                                                    <c:when test="${group.isT1 == 1 }">
                                                        <em class="grounding-hover grounding-one">已上架旅游交易系统</em>
                                                        <em class="g-w grounding"></em>
                                                        <input type="checkbox" name="groupNo" class="tableCheckBox"
                                                               disabled="disabled"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!--团期处于未上架状态时，不显示状态标签-->
                                                        <c:if test="${group.pricingStrategy.alertFlag == 'false'}">
                                                            <em class="grounding-one"></em>
                                                            <em class="g-w unGrounding"></em>
                                                            <input type="checkbox" name="groupNo" class="tableCheckBox"
                                                                   disabled="disabled" style="display: none"/>
                                                        </c:if>
                                                        <c:if test="${group.pricingStrategy.alertFlag == 'true'}">
                                                            <em class="withdraw-hover grounding-one">已下架旅游交易系统</em>
                                                            <em class="g-w withdraw"></em>
                                                            <input type="checkbox" class="tableCheckBox"
                                                                   name="groupNo"/>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <span name="groupCode" value="${group.groupCode}">${group.groupCode}</span>
                                            <!--将团号变成可编辑的模式 -s-tgyTODO:这的正则有点问题-->
                                            <input type="text" id="${group.id}" maxlength="50" name="groupCode"
                                                   value="${group.groupCode}" src="${group.id}"
                                                   onafterpaste="replaceStr(this)" onkeyup="validateLong(this)"
                                                   style="display:none;"/>
                                            <!--将团号变成可编辑的模式 -e-tgy-->
                                        </div>
                                        <!-- 518 添加平台上架标签 E -->
                                    </td>

                                    <td class="tc">
                                        <input type="hidden" class="srcActivityId" name="srcActivityId"
                                               value="${group.srcActivityId}"/>
                                        <span name="groupOpenDate"><fmt:formatDate pattern="yyyy-MM-dd"
                                                              value="${group.groupOpenDate}"/></span>
                                            <%--<input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;" disabled="disabled"/>--%>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="groupOpenDate${s.count}${s2.count}" name="groupOpenDate"
                                               value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>"
                                               onClick="WdatePicker()" class="inputTxt"/>
                                    </td>
                                        <%--<input type="hidden" class="srcActivityId" value="${group.srcActivityId}"/>--%>
                                    <td class="tc">
                                        <span><fmt:formatDate pattern="yyyy-MM-dd"
                                                              value="${group.groupCloseDate }"/></span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="groupCloseDate${s.count}${s2.count}" name="groupCloseDate"
                                               value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}" />"
                                               onClick="WdatePicker({maxDate:takeOrderOpenDate('groupCloseDate${s.count}${s2.count}')})"
                                               class="inputTxt"/>
                                    </td>
                                    <td>
                                        <span>${group.visaCountry }</span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="visaCountry${s.count}${s2.count}" name="visaCountry"
                                               value="${group.visaCountry}"/>
                                    </td>
                                    <td class="tc">
                                        <span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="visaDate${s.count}${s2.count}" name="visaDate"
                                               value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/>"
                                               onClick="WdatePicker({maxDate:takeModVisaDate('visaDate${s.count}${s2.count}')})"/>
                                    </td>
                                    <!-- 299v2 酒店房型 -->
                                    <shiro:hasPermission name="price:project">

                                        <td class="tc hotelAndHouse">
                                            <input type="hidden" name="groupHotel" value="${group.groupHotel}"/>
                                            <input type="hidden" name="groupHouseType" value="${group.groupHouseType}"/>
                                        </td>

                                    </shiro:hasPermission>

                                    <!-- 同行价开始 -->
                                    <!-- 成人价 -->
                                    <td class="tr tdCurrency">
                                        <c:if test="${group.settlementAdultPrice ne 0}">

                                            <%--同行价 成人 开始--%>
                                        <div class="price-list"
                                             <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementAdultPrice }机票价"</c:if>>
                                            <span class="price-title">成人：</span>
                                            <span class="price-content word-break-all">
													<span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span>
													<span class="tdred"><fmt:formatNumber type="currency"
                                                                                          pattern="#,##0.00"
                                                                                          value="${group.settlementAdultPrice }"/>
                                                        </c:if></span>
													<input style="display:none;" disabled="disabled" type="text"
                                                           id="settlementAdultPrice${s.count}${s2.count}"
                                                           name="settlementAdultPrice"
                                                           value="<c:if test="${group.settlementAdultPrice eq 0}"></c:if><c:if test="${group.settlementAdultPrice ne 0}"><fmt:formatNumber value="${group.settlementAdultPrice}" pattern="##0.00" /></c:if>"
                                                           maxlength="14"
                                                           onkeyup="isMoney(this)" onafterpaste="isMoney(this)"
                                                           onblur="isMoney(this)"/>
												</span>
                                        </div>
                                        <%--同行价 成人 结束--%>
                                        <%--同行价 儿童 开始--%>
                                        <div class="price-list"
                                             <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementcChildPrice }机票价"</c:if>>
                                            <span class="price-title">儿童：</span>
                                            <span class="price-content word-break-all">
													<c:if test="${group.settlementcChildPrice ne 0}"><span
                                                    class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span>
													<span class="tdred"><fmt:formatNumber type="currency"
                                                                                          pattern="#,##0.00"
                                                                                          value="${group.settlementcChildPrice }"/></c:if></span>
													<input style="display:none;" disabled="disabled" type="text"
                                                           id="settlementcChildPrice${s.count}${s2.count}"
                                                           name="settlementcChildPrice"
                                                           value="<c:if test="${group.settlementcChildPrice eq 0}"></c:if><c:if test="${group.settlementcChildPrice ne 0}"><fmt:formatNumber value="${group.settlementcChildPrice}" pattern="##0.00" /></c:if>"
                                                           maxlength="14"
                                                           onkeyup="isMoney(this)" onafterpaste="isMoney(this)"
                                                           onblur="isMoney(this)"/>
												</span>

                                        </div>
                                       <%--同行价 儿童 结束--%>
                                        <%--同行价 特殊人群 开始--%>
                                        <div class="price-list"
                                             <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementSpecialPrice }机票价"</c:if>>
                                            <span class="price-title">特殊人群：</span>
                                            <span class="price-content word-break-all">
													<c:if test="${group.settlementSpecialPrice ne 0}">
                                                        <span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span>
                                                        <span class="tdred"
                                                              title="${activity.specialRemark }"><fmt:formatNumber
                                                                type="currency" pattern="#,##0.00"
                                                                value="${group.settlementSpecialPrice }"/></c:if></span>
													<input style="display:none;" disabled="disabled" type="text"
                                                           id="settlementSpecialPrice${s.count}${s2.count}"
                                                           name="settlementSpecialPrice"
                                                           value="<c:if test="${group.settlementSpecialPrice eq 0}"></c:if><c:if test="${group.settlementSpecialPrice ne 0}"><fmt:formatNumber value="${group.settlementSpecialPrice}" pattern="##0.00" /></c:if>"
                                                           maxlength="14"
                                                           onkeyup="isMoney(this)" onafterpaste="isMoney(this)"
                                                           onblur="isMoney(this)"/>
												</span>
                                        </div>
                                        <%--同行价 特殊人群 结束--%>
                                    </td>
                                    <!-- 同行价结束 -->
                                    <!-- 直客价开始 -->
                                    <!-- wxw  added 20160107  处理 币种符号串位问题   -->
                                    <td class="tr tdCurrency">
                                            <%--直客价 成人 开始--%>
                                        <div class="price-list">
                                            <span class="price-title">成人：</span>
                                            <span class="price-content word-break-all">
												<c:if test="${group.suggestAdultPrice ne 0}">
                                                <%--BUG16425 统一样式，币种和金额span标签中间添加换行--%>
                                                    <span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span>
                                                    <span class="tdblue"><fmt:formatNumber type="currency"
                                                                                           pattern="#,##0.00"
                                                                                           value="${group.suggestAdultPrice }"/></c:if></span>
												<input style="display:none;" disabled="disabled" type="text"
                                                       id="suggestAdultPrice${s.count}${s2.count}"
                                                       name="suggestAdultPrice"
                                                       value="<c:if test="${group.suggestAdultPrice eq 0}"></c:if><c:if test="${group.suggestAdultPrice ne 0}"><fmt:formatNumber value="${group.suggestAdultPrice}" pattern="##0.00" /></c:if>"
                                                       maxlength="14" onkeyup="isMoney(this)"
                                                       onafterpaste="isMoney(this)" onblur="isMoney(this)"/>
											</span>
                                        </div>
                                        <%--直客价 成人 结束--%>
                                        <%--直客价 儿童 开始--%>
                                        <div class="price-list">
                                            <span class="price-title">儿童：</span>
                                            <span class="price-content word-break-all">
												<c:if test="${group.suggestChildPrice ne 0}">
                                                    <span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span>
                                                    <span class="tdblue"><fmt:formatNumber type="currency"
                                                                                           pattern="#,##0.00"
                                                                                           value="${group.suggestChildPrice }"/></c:if></span>
												<input style="display:none;" disabled="disabled" type="text"
                                                       id="suggestChildPrice${s.count}${s2.count}"
                                                       name="suggestChildPrice"
                                                       value="<c:if test="${group.suggestChildPrice eq 0}"></c:if><c:if test="${group.suggestChildPrice ne 0}"><fmt:formatNumber value="${group.suggestChildPrice}" pattern="##0.00" /></c:if>"
                                                       maxlength="14" onkeyup="isMoney(this)"
                                                       onafterpaste="isMoney(this)" onblur="isMoney(this)"/>
											</span>
                                        </div>
                                        <%--直客价 儿童 结束--%>
                                        <%--直客价 特殊人群 开始--%>
                                        <div class="price-list">
                                            <span class="price-title">特殊人群：</span>
                                            <span class="price-content word-break-all">
												<c:if test="${group.suggestSpecialPrice ne 0}">
                                                    <span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span>
                                                    <span class="tdblue"
                                                          title="${activity.specialRemark }"><fmt:formatNumber
                                                            type="currency" pattern="#,##0.00"
                                                            value="${group.suggestSpecialPrice }"/></c:if></span>
												<input style="display:none;" disabled="disabled" type="text"
                                                       id="suggestSpecialPrice${s.count}${s2.count}"
                                                       name="suggestSpecialPrice"
                                                       value="<c:if test="${group.suggestSpecialPrice eq 0}"></c:if><c:if test="${group.suggestSpecialPrice ne 0}"><fmt:formatNumber value="${group.suggestSpecialPrice}" pattern="##0.00" /></c:if>"
                                                       maxlength="14" onkeyup="isMoney(this)"
                                                       onafterpaste="isMoney(this)" onblur="isMoney(this)"/>
											</span>
                                        </div>
                                        <%--直客价 特殊人群 结束--%>
                                    </td>
                                    <!-- 直客价结束 -->

                                    <!-- 儿童最高人数 -->
                                    <td class="tr tdCurrency">
                                        <span class="tdorange"><c:if
                                                test="${group.maxChildrenCount ne 0}"> ${group.maxChildrenCount } </c:if></span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="maxChildrenCount${s.count}${s2.count}" name="maxChildrenCount"
                                               value="<c:if test="${group.maxChildrenCount eq 0}"></c:if><c:if test="${group.maxChildrenCount ne 0}">${group.maxChildrenCount}</c:if>"
                                               onafterpaste="this.value.replace(/\D|^0.+/g,'')" maxlength="8"
                                               onKeyUp="this.value.replace(/\D|^0.+/g,'')"/>
                                    </td>
                                    <!-- 特殊人群数 -->
                                    <td class="tr tdCurrency">
                                        <span class="tdorange"><c:if
                                                test="${group.maxPeopleCount ne 0}"> ${group.maxPeopleCount } </c:if></span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="maxPeopleCount${s.count}${s2.count}" name="maxPeopleCount"
                                               value="<c:if test="${group.maxPeopleCount eq 0}"></c:if><c:if test="${group.maxPeopleCount ne 0}">${group.maxPeopleCount}</c:if>"
                                               onafterpaste="this.value.replace(/\D/g,'')" maxlength="8"
                                               onKeyUp="this.value.replace(/\D/g,'')"/>
                                    </td>
                                    <!-- 定金 -->
                                    <c:if test="${fn:length(fn:split(group.currencyType,',')) gt 8 }">
                                        <td class="tr tdCurrency">
                                            <c:if test="${group.payDeposit ne 0}"><span
                                                class="rm">${fns:getCurrencyInfo(group.currencyType,12,'mark')}</span><span
                                                class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                   value="${group.payDeposit }"/></c:if></span>
                                            <input style="display:none;" disabled="disabled" type="text"
                                                   id="payDeposit${s.count}${s2.count}" name="payDeposit"
                                                   value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}"><fmt:formatNumber value="${group.payDeposit}" pattern="#.##" /></c:if>"
                                                   onafterpaste="this.value.replace(/\D/g,'')" maxlength="14"
                                                   onKeyUp="this.value.replace(/\D/g,'')"/>
                                        </td>
                                        <!-- 单房差 -->
                                        <td class="tr tdCurrency">
                                            <c:if test="${group.singleDiff ne 0}"><span
                                                class="rm">${fns:getCurrencyInfo(group.currencyType,13,'mark')}</span><span
                                                class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                value="${group.singleDiff }"/></c:if></span>
                                            <input style="display:none;" disabled="disabled" type="text"
                                                   id="singleDiff${s.count}${s2.count}" name="singleDiff"
                                                   value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}"><fmt:formatNumber value="${group.singleDiff}" pattern="#.##" /></c:if>"
                                                   onafterpaste="this.value.replace(/\D/g,'')" maxlength="14"
                                                   onKeyUp="this.value.replace(/\D/g,'')"/>
                                        </td>
                                    </c:if>
                                    <!-- 定金 -->
                                    <c:if test="${fn:length(fn:split(group.currencyType,',')) le 8 }">
                                        <td class="tr tdCurrency">
                                            <c:if test="${group.payDeposit ne 0}"><span
                                                class="rm">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</span><span
                                                class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                   value="${group.payDeposit }"/></c:if></span>
                                            <input style="display:none;" disabled="disabled" type="text"
                                                   id="payDeposit${s.count}${s2.count}" name="payDeposit"
                                                   value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}"><fmt:formatNumber value="${group.payDeposit}" pattern="#.##" /></c:if>"
                                                   onafterpaste="this.value.replace(/\D/g,'')" maxlength="14"
                                                   onKeyUp="this.value.replace(/\D/g,'')"/>
                                        </td>
                                        <!-- 单房差 -->
                                        <td class="tr tdCurrency">
                                            <c:if test="${group.singleDiff ne 0}"><span
                                                class="rm">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</span><span
                                                class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                value="${group.singleDiff }"/></c:if></span>
                                            <input style="display:none;" disabled="disabled" type="text"
                                                   id="singleDiff${s.count}${s2.count}" name="singleDiff"
                                                   value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}"><fmt:formatNumber value="${group.singleDiff}" pattern="#.##" /></c:if>"
                                                   onafterpaste="this.value.replace(/\D/g,'')" maxlength="14"
                                                   onKeyUp="this.value.replace(/\D/g,'')"/>
                                        </td>
                                    </c:if>
                                    <!-- 预收 -->
                                    <td class="tr">
                                        <span>${group.planPosition }</span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="planPosition${s.count}${s2.count}" name="planPosition"
                                               value="${group.planPosition}"
                                               onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3"
                                               onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
                                    </td>
                                    <!-- 余位 -->
                                    <td class="tr">
                                        <span class="tdred">${group.freePosition }</span>
                                        <input style="display:none;" disabled="disabled" type="text"
                                               id="freePosition${s.count}${s2.count}" name="freePosition"
                                               value="${group.freePosition}"
                                               onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3"
                                               onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
                                    </td>
                                    <!-- 0258需求,发票税:针对懿洋假期-tgy-s-列表: -->
                                    <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
                                        <td class="tr">
                                            <span class="">${group.invoiceTax==null?0:group.invoiceTax}&nbsp;%</span>
                                            <input style="display:none;width:75%;" disabled="disabled" type="text"
                                                   id="invoiceTax${s.count}${s2.count}" name="invoiceTax"
                                                   value="${group.invoiceTax==null?0:group.invoiceTax}"
                                                   onafterpaste="checkValue(this)" onKeyUp="checkValue(this)"
                                                   onfocus="checkValue(this)"/><span style="display:none">%</span>
                                        </td>
                                    </c:if>
                                    <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->
                                    <td style="display:none;" class="soldPayPosition${group.id}">
                                        <span style="color:#eb0205">0</span>
                                    </td>
                                    <td class="tnwrap tc">
                                        <shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
                                            <a href="javascript:void(0)" id="modbtn${s.count}${s2.count}" name="modbtn"
                                               onClick="modgroup(this)">修改</a>
                                        </shiro:hasPermission>
                                        <shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
                                            <a id="savebtn${s.count}${s2.count}" style="display:none;" name="savebtn"
                                               onClick="checkVisaCountrylen(${s.count},${s2.count});savegroupForLoose(this)">保存</a>
                                        </shiro:hasPermission>
                                        <shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
                                            <a style="color:#ec0203;display:none;" id="cancelbtn${s.count}${s2.count}"
                                               href="javascript:void(0)" name="cancelbtn"
                                               onClick="cancelgroup(this)">取消</a>
                                        </shiro:hasPermission>

                                        <c:if test="${shelfRightsStatus eq 0}">
                                            <shiro:hasPermission
                                                    name="${shiroType}Product:operation:groupPricingStrategy">
                                                <a onclick="setPricingStrategy(this,${group.id},${ group.srcActivityId},'${activity.acitivityName }','${group.pricingStrategy.alertFlag}')"
                                                   class="setting-btn">设定定价策略</a>
                                            </shiro:hasPermission>
                                            <c:if test="${group.isT1 == 0 }">
                                                <c:if test="${group.pricingStrategy.alertFlag == 'false'}">
                                                    <a href="javascript:void(0)" style="display:none" class="stack-btn"
                                                       onclick="grounding('${group.id}',this)">平台上架</a>
                                                </c:if>
                                                <c:if test="${group.pricingStrategy.alertFlag == 'true'}">
                                                    <a href="javascript:void(0)" class="stack-btn"
                                                       onclick="grounding('${group.id}',this)">平台上架</a>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${group.isT1 != 0 }">
                                                <a href="javascript:void(0)" style="display:none" class="stack-btn"
                                                   onclick="grounding('${group.id}',this)">平台上架</a>
                                            </c:if>

                                        </c:if>

                                        <!-- 0518 添加价格表 S -->
                                        <c:if test="${shelfRightsStatus eq 0}">
                                            <c:if test="${group.isT1 == 1 }">
                                                <div class="morePrice-parent">
                                                    <shiro:hasPermission
                                                            name="${shiroType}Product:operation:priceTable">
                                                        <a onclick="priceListJbox(this,'${fns:getCurrencyInfo(group.currencyType,0,'mark')}','${fns:getCurrencyInfo(group.currencyType,1,'mark')}','${fns:getCurrencyInfo(group.currencyType,2,'mark')}');"
                                                           href="javascript:void(0)"> 价格表 </a>
                                                    </shiro:hasPermission>
                                                </div>
                                            </c:if>
                                            <c:if test="${group.isT1 != 1 }">
                                                <shiro:hasPermission name="${shiroType}Product:operation:priceTable">
                                                    <div class="morePrice-parent" style="display: none">
                                                        <a onclick="priceListJbox(this,'${fns:getCurrencyInfo(group.currencyType,0,'mark')}','${fns:getCurrencyInfo(group.currencyType,1,'mark')}','${fns:getCurrencyInfo(group.currencyType,2,'mark')}');"
                                                           href="javascript:void(0)"> 价格表 </a>
                                                    </div>
                                                </shiro:hasPermission>
                                            </c:if>
                                        </c:if>
                                        <!-- 0518 添加价格表 E -->


                                        <dl class="handle more-op-style">
                                            <dt style="width:65px;">
                                                <a class="more-op" href="javascript:void(0)"> 更多操作 <i
                                                        class="fa fa-angle-down" aria-hidden="true"></i> </a>
                                            </dt>
                                            <dd class="">
                                                <p>
                                                    <span></span>
                                                    <a class="expandNotes" href="javascript:void(0)">展开备注
                                                        <c:choose>
                                                            <c:when test="${not empty group.groupRemark }">
                                                                <em class="groupNoteTipImg"></em>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <em class="groupNoteTipImg" style="display: none"></em>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </a>
                                                    <shiro:hasPermission name="price:project">
                                                        <%--<c:if test="${not empty group.priceJson }">--%>
                                                        <a class="expandPriceJson" href="javascript:void(0)"
                                                           onclick="expandPriceJson(this, ${activityKind})"
                                                           data='${group.priceJson}'> 展开价格方案 </a>
                                                        <%--</c:if>--%>
                                                    </shiro:hasPermission>
                                                    <shiro:hasPermission
                                                            name="${shiroType}Product:operation:groupDelete">
                                                        <a href="javascript:void(0)" id="delbtn${s.count}${s2.count}"
                                                           onClick="return confirmxCopy('要删除该产品的此团期吗？',${group.id},${activity.id },this,'child${s.count}')">删除</a>
                                                    </shiro:hasPermission>
                                                </p>
                                            </dd>
                                        </dl>
                                    </td>
                                </tr>
                                <tr class="groupNoteCol">
                                    <!-- 懿洋假期多一列发票税 -->
                                    <td colspan="25">
                                        <div>
                                            备注:<span class="groupNoteContent" id="groupNoteContent${s.count}${s2.count}"
                                                     name="groupNoteContent">${group.groupRemark}</span>
                                        </div>
                                        <div class="remarks-containers display-none">备注:<input type="text"
                                                                                               class="groupNotes padding-none"
                                                                                               name="groupNote"/>
                                        </div>
                                    </td>
                                </tr>

                                <!--299-产品模块-单团产品列表-start-->
                                <tr class="pricePlanContainer" style="display:none;">
                                    <td colspan="25">
                                        <table name="pricePlanTable" id="pricePlanTable"
                                               class="table activitylist_bodyer_table border-table-spread"
                                               style="margin: 0 auto">
                                            <thead>
                                            <tr>
                                                <th rowspan="2" class="tc" style="width: 50px;">序号</th>
                                                <th rowspan="2" class="tc" style="width: 500px">价格方案</th>
                                                <th colspan="3" class="tc t-th2">同行价</th>
                                                <th colspan="3" class="tc t-th2">直客价</th>

                                                <th rowspan="2" class="tc">备注</th>
                                            </tr>
                                            <tr>
                                                <th class="tc">成人</th>
                                                <th class="tc">儿童</th>
                                                <th class="tc">特殊人群</th>

                                                <th class="tc">成人</th>
                                                <th class="tc">儿童</th>
                                                <th class="tc">特殊人群</th>
                                            </tr>
                                            </thead>
                                            <tbody>

                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                <!--299-产品模块-单团产品列表-end-->
                                </tbody>
                            </c:forEach>
                        </table>
                    </form>
                    <!-- 518 全选反选批量上架 S -->

                    <div class="selectInTable">
                        <c:if test="${shelfRightsStatus eq 0 }">
                        <span>
                             <input class="table-checkAll" onclick="tableCheckAll(this)" type="checkbox">
                             <label>全选</label>
						</span>
                            <span class="table-checkReverse" onclick="tableCheckReverse(this)">反选</span>
                            <input onclick="batchGrounding(this);" type="button" class="btn btn-primary" value="批量平台上架">
                        </c:if>
                    </div>

                    <!-- 518 全选反选批量上架 E -->
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

<div class="page">
    <div class="pagination">
        <dl>
            <dt><input name="allChk" type="checkbox" onclick="checkall(this)"/>全选</dt>
            <dd>
                <shiro:hasPermission name="${shiroType}Product:operation:edit">
                    <%--<a onClick="confirmBatchIsNull('需要将选择的产品下架吗','off')">批量下架</a>--%>
                    <input type="button" value="批量下架" onClick="confirmBatchIsNull('需要将选择的产品下架吗','off')"class="btn ydbz_x">
                </shiro:hasPermission>
                <shiro:hasPermission name="${shiroType}Product:operation:delete">
                    <%--<a onClick="confirmBatchIsNull('删除所有选择的团期吗','del')">批量删除</a>--%>
                    <input type="button" value="批量删除" onClick="confirmBatchIsNull('删除所有选择的团期吗','del')" class="btn ydbz_x">
                </shiro:hasPermission>
            </dd>
        </dl>
        <div class="endPage">${page}</div>
        <div style="clear:both;"></div>
    </div>
</div>
</c:if>
</div>
</div>
<c:if test="${fn:length(page.list) eq 0}">
    <table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
        <tr>
            <th width="4%">序号</th>
            <th class="table_borderLeftN" width="4%">全选<input name="allChk" type="checkbox" onclick="checkall(this)"/>
            </th>
                <%--			<th width="10%">产品编号</th>--%>
            <th width="14%">产品名称</th>
            <th width="8%">计调</th>
            <th width="8%">出发城市</th>
            <c:if test="${activityKind ne '10' }">
                <th width="6%">航空</th>
            </c:if>
            <th width="8%">签证</th>
            <th width="16%">最近出团日期</th>
            <th width="10%">成人同行价</th>
            <c:if test="${activityKind eq '2' or activityKind eq '10'}">
                <th width="10%">成人直客价</th>
            </c:if>
            <th width="4%">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <c:if test="${activityKind eq '2' or activityKind eq '10'}">
                <td colspan="11" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
            </c:if>
            <c:if test="${activityKind ne '2' and activityKind ne '10'}">
                <td colspan="10" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
            </c:if>
        </tr>
        </tbody>
    </table>
</c:if>
<form id="exportForm" action="${ctx}/activity/manager/exportExcel" method="post">
    <input type="hidden" id="groupId" name="groupId">
    <input type="hidden" id="groupCode" name="groupCode">
</form>

</body>
</html>