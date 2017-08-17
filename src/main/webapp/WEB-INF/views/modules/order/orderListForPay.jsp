<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>成本付款</title>
    <meta name="decorator" content="wholesaler"/>
    <link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/css/Remark_New.css" type="text/css" rel="stylesheet"/>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/jquery.nicescroll.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        //如果展开部分有查询条件的话，默认展开，否则收起
        function closeOrExpand() {
            var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
            var searchFormselect = $("#searchForm").find("select");
            var inputRequest = false;
            var selectRequest = false;
            for (var i = 0; i < searchFormInput.length; i++) {
                var inputValue = $(searchFormInput[i]).val();
                if (inputValue != "" && inputValue != null) {
                    inputRequest = true;
                    break;
                }
            }
            for (var i = 0; i < searchFormselect.length; i++) {
                var selectValue = $(searchFormselect[i]).children("option:selected").val();
                if (selectValue != "" && selectValue != null && selectValue != 0) {
                    selectRequest = true;
                    break;
                }
            }
            var bankNamee = '${bankNamee}';
            if (bankNamee != "") {
                inputRequest = true;
            }
            if (inputRequest || selectRequest) {
                $('.zksx').click();
            }
        }
        $(function () {
            $.ajax({
                url: "${ctx}/query/select/getFromBanks",
                type: "POST",
                data: null,
                success: function (data) {
                    var htmlstr = $("select[name=bank]");
                    htmlstr.html('');
                    htmlstr.append("<option value='' >请选择</option>");
                    var num = data.length;
                    if (num > 0) {
                        for (var i = 0; i < num; i++) {
                            if (data[i].bankName == '${bankNamee}') {
                                htmlstr.append("<option value='" + data[i].bankName + "' selected='selected'>" + data[i].bankName + "</option>");
                            } else {
                                htmlstr.append("<option value='" + data[i].bankName + "'>" + data[i].bankName + "</option>");
                            }
                        }
                    }

                }
            });
            //展开、收起筛选
            launch();
            //如果展开部分有查询条件的话，默认展开，否则收起
            closeOrExpand();
            //操作浮框
            operateHandler();
            //
            switchSupplierAndAgent();
            //计调模糊匹配
            $("[name=jd]").comboboxSingle();
            $("[name=bank]").comboboxSingle();
            $("[name=agentId]").comboboxSingle();
            $("[name=supplierInfo]").comboboxSingle();

            $(document).delegate(".downloadzfpz", "click", function () {
                window.open("${ctx}/sys/docinfo/download/" + $(this).attr("lang"));
            });

            $("#contentTable").delegate("ul.caption > li", "click", function () {
                var iIndex = $(this).index();
                /*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
                $(this).addClass("on").siblings().removeClass('on');
                $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
            });

            $("#contentTable").delegate(".tuanhao", "click", function () {
                $(this).addClass("on").siblings().removeClass('on');
                $('.chanpin_cen').removeClass('onshow');
                $('.tuanhao_cen').addClass('onshow');
            });

            $("#contentTable").delegate(".chanpin", "click", function () {
                $(this).addClass("on").siblings().removeClass('on');
                $('.tuanhao_cen').removeClass('onshow');
                $('.chanpin_cen').addClass('onshow');
            });

            var _$orderBy = $("#orderBy").val();
            if (_$orderBy == "") {
                _$orderBy = "updateDate DESC";
            }
            var orderBy = _$orderBy.split(" ");
            $(".activitylist_paixu_left li").each(function () {
                if ($(this).hasClass("li" + orderBy[0])) {
                    orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
                    $(this).attr("class", "activitylist_paixu_moren");
                }
            });

            $(document).scrollLeft(0);
            $("#targetAreaId").val("${travelActivity.targetAreaIds}");
            $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
        });

        $().ready(function () {
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
        });
        // 548,549需求
        $(function () {
            $('.remark_div_child').niceScroll({
                cursorcolor: "#ccc",//#CC0071 光标颜色
                cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                cursorwidth: "5px", //像素光标的宽度
                cursorborder: "0", //     游标边框css定义
                cursorborderradius: "5px",//以像素为光标边界半径
                autohidemode: false //是否隐藏滚动条
            });
            $(".remark_548").mouseover(function () {
                $(this).children().show();
                $(".remark_div_child").getNiceScroll().resize();
            });
            $(".remark_548").mouseleave( function () {
                $(this).children().hide();
                if ($(this).find("input").is(':checked')) {
                   var reviewUuid = $(this).find("input")[0].value;
                    $(this).remove();
                    $.ajax({
                        type:"post",
                        url:"${ctx}/review/common/web/cancelShowRemark",
                        data:{reviewUuid:reviewUuid}
                    });
                }
            });
        });


        /*
         * 付款确认页面
         */
        function paymentConfirm(id) {
            location.href = "${ctx}/cost/manager/paymentConfirm/" + id;
        }

        function orderDetail(orderId) {
            window.open("${ctx}/orderCommon/manage/orderDetail/" + orderId, "_blank");
        }

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        $(function () {
            $.fn.datepicker = function (option) {
                var opt = {} || option;
                this.click(function () {
                    WdatePicker(option);
                });
            }

            //首次打印提醒
            $(".uiPrint").hover(function () {
                $(this).find("span").show();
            }, function () {
                $(this).find("span").hide();
            });
        });

        $(function () {
            $('.nav-tabs li').hover(function () {
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                if ($(this).hasClass('ernav')) {
                    if (!$(this).hasClass('current')) {
                        $(this).addClass('current');
                        $(this).parent().addClass('nav_current');
                    }
                }
            }, function () {
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                var _active = $(".totalnav .active").eq(0);
                if (_active.hasClass('ernav')) {
                    _active.addClass('current');
                    $(this).parent().addClass('nav_current');
                }
            });
            //展开收起搜索条件
            //操作浮框
            operateHandler();
            //输入金额提示
            inputTips();
        });

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

        function query() {
            $('#searchForm').submit();
        }
        
        // 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22
        function exportExcel() {
            $('#searchForm').attr('action', '${ctx}/finance/manage/getCostPaymentListExcel')
            $('#searchForm').submit();
            $('#searchForm').attr('action', '${ctx}/finance/manage/showFinanceList/${showType}/${orderS}.htm?option=${param.option }')
        }

        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1,                 //月份
                "d+": this.getDate(),                    //日
                "h+": this.getHours(),                   //小时
                "m+": this.getMinutes(),                 //分
                "s+": this.getSeconds(),                 //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()             //毫秒
            };

            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }


        function expand(child, obj, id, orderType, type, isNew) {
            $.ajax({
                url: "${ctx}/orderCommon/manage/refundPayInfo/",
                type: "POST",
                data: {id: id, type: type, orderType: orderType},
                success: function (data) {
                    var htmlstr = "";
                    var num = data.length;
                    if (num > 0) {
                        var str1 = '';
                        for (var i = 0; i < num; i++) {
                            var str = data[i].payvoucher.split("|");
                            var index = str.length;
                            if (index > 0) {
                                for (var a = 0; a < index; a++) {
                                    str1 += str[a] + "<br/>";
                                }
                            }
                            if (data[i].label == null) { <%-- 拉美图的因公支付宝结算方式暂未保存到字典表中。后期如做调整可删除 --%>
                                data[i].label = "因公支付宝";
                            }
                            htmlstr += "<tr><td class='tc'>" + data[i].label + "</td><td class='tc'>" + data[i].amount + "</td><td class='tc'>" + (new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss") + "</td><td class='tc'>" + data[i].a +
                                    "</td><td class='tc'>" + str1 + "</td><td class='tc'>" + data[i].opstatus + "</td><td class='tc'>";

                            if (data[i].opstatus == '已支付') {
                                if (orderType == '11' || orderType == '12') {
                                    htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','" + data[i].id + "',2,cancelDetailOper," + data[i].orderType + ",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','" + data[i].id + "','" + data[i].orderType + "',this)\" >撤销</a></td></tr>";
                                } else {
                                    htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','" + data[i].id + "',2,cancelDetailOper," + data[i].orderType + ",this)\" href='javascript:void'>查看</a>" +
                                            "&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','" + data[i].id + "','" + data[i].orderType + "',this)\">撤销</a>" +
                                            "&nbsp;&nbsp;<a href=\"${ctx}/cost/manager/payment/" + id + "/" + orderType + "/" + isNew + "?option=pay&payId=" + data[i].id + "\"" + " target=\"_blank\">打印</a></td></tr>";
                                }
                            } else {
                                htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','" + data[i].id + "',2,cancelDetailOper," + data[i].orderType + ",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;</td></tr>";
                            }
                            str1 = '';
                        }
                    }
                    $("#rpi_" + id).html(htmlstr);
                }
            });
            if ($(child).is(":hidden")) {
                $(obj).html("收起");
                $(obj).parents("tr").addClass("tr-hover");
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
            } else {
                if (!$(child).is(":hidden")) {
                    $(obj).parents("tr").removeClass("tr-hover");
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    $(obj).html("支付记录");
                }
            }
        }

        function cancelOper(ctx, str, orderType, obj) {
            $.jBox("iframe:" + ctx + "/refund/manager/cancelPayInfo?refundId=" + str + "&flag=edit&payType=2&orderType=" + orderType, {
                title: "付款记录",
                width: 830,
                height: 500,
                buttons: {'撤销': 1, '关闭': 0},
                persistent: true,
                loaded: function (h) {
                },
                submit: function (v, h, f) {
                    if (v == 1) {
                        var refundId = $(h.find("iframe")[0].contentWindow.refundId).val();
                        var recordId = $(h.find("iframe")[0].contentWindow.recordId).val();
                        $.ajax({
                            type: "GET",
                            url: ctx + "/refund/manager/undoRefundPayInfo",
                            dataType: "json",
                            data: {refundId: refundId, recordId: recordId},
                            success: function (data) {
                                if (data.flag == 'ok') {
                                    $('#searchForm').submit();
                                }
                            }
                        });
                    }
                }
            }).find("#jbox-content").css("overflow", "hidden");
        }

        function cancelDetailOper(ctx, refundId, recordId, obj) {
            $.ajax({
                type: "GET",
                url: ctx + "/refund/manager/undoRefundPayInfo",
                dataType: "json",
                data: {refundId: refundId, recordId: recordId},
                success: function (data) {
                    if (data.flag == 'ok') {
                        $(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
                    }
                }
            });
        }

        /**
         * 查询条件重置
         *
         */

        function resetForm() {
            var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
            var selectArray = $('#searchForm').find("select");
            for (var i = 0; i < inputArray.length; i++) {
                if ($(inputArray[i]).val()) {
                    $(inputArray[i]).val('');
                }
            }
            for (var i = 0; i < selectArray.length; i++) {
                var selectOption = $(selectArray[i]).children("option");
                $(selectOption[0]).attr("selected", "selected");
            }
        }

        //批量付款的处理
        $(document).on('change', '#contentTable,#contentTable_foot, [type="checkbox"]', function () {
            var $this = $(this);
            var $contentContainer = $("#contentTable,#contentTable_foot");
            var $all = $contentContainer.find('[check-action="All"]');
            var $reverse = $contentContainer.find('[check-action="Reverse"]');
            var $chks = $contentContainer.find('[check-action="Normal"]:enabled');
            if ($this.is('[check-action="All"]')) {
                if ($this.is(':checked')) {
                    $chks.attr('checked', true);
                } else {
                    $chks.removeAttr('checked');
                }
            }
            if ($this.is('[check-action="Reverse"]')) {
                $chks.each(function () {
                    var $chk = $(this);
                    if ($chk.is(':checked')) {
                        $chk.removeAttr('checked');
                    } else {
                        $chk.attr('checked', true);
                    }
                });
            }
            if ($chks.length && ($chks.length == $chks.filter(':checked').length)) {
                $all.attr('checked', true);
            } else {
                $all.removeAttr('checked');
            }
        });

	function batchPrint(){
		var boxes = $("input[name = 'checkBox']");
		var flag = true;
		var datas = [];
		boxes.each(function(){
			 if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
				 flag = false;
				 var values = $(this).val().split("_");
				 var data = {};
				 data.id = values[0];
				 data.orderType = values[1];
				 data.isNew = $(this).next().val();
				 datas.push(data);
			 }
		});
		if(flag){
			top.$.jBox.tip('请选择数据！');
			return;
		}
		$("#params").val(JSON.stringify(datas))
		$("#printForm").submit();
		/* window.open("${ctx}/cost/manager/batchPayment?params="+JSON.stringify(datas)); */
	}

    </script>

    <style type="text/css">
        a {
            display: inline-block;
        }

        label {
            cursor: inherit;
        }
    </style>

</head>
<body>
<!-- 批量打印post提交 -->
 <form action="${ctx}/cost/manager/batchPayment" id="printForm" method="post" target="_blank">
    <input type="hidden" name = "params" value="" id="params">
</form>
<%--<page:applyDecorator name="finance_op_head" >--%>
<%--<page:param name="current"><c:choose><c:when test="${param.option eq 'detail' or empty param.option}">dealList</c:when><c:when test="${param.option eq 'account'}">agingList</c:when><c:when test="${param.option eq 'pay'}">payList</c:when></c:choose></page:param>--%>
<%--</page:applyDecorator>--%>
<c:choose>
	<c:when test="${param.option eq 'detail' or empty param.option}">
		<c:set var="current" value="dealList"/>
	</c:when>
	<c:when test="${param.option eq 'account'}"><c:set var="current" value="agingList"/></c:when>
	<c:when test="${param.option eq 'pay'}"><c:set var="current" value="payList"/></c:when>
</c:choose>
<%@ include file="/WEB-INF/views/head/financeHead.jsp" %>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    <form:form id="searchForm" modelAttribute="travelActivity"
               action="${ctx}/finance/manage/showFinanceList/${showType}/${orderS}.htm?option=${param.option }"
               method="post">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    <!--<div class="order_bill"></div>-->
    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2">
            <input id="groupCode" name="groupCode" class="txtPro inputTxt searchInput" value="${groupCode }"  placeholder="请输入团号"/>
        </div>
        <div class="zksx">筛选</div>
        <div class="form_submit">
            <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
            <input class="btn ydbz_x" type="button" onclick="resetForm()" value="清空所有条件"/>
            <!-- 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22 -->
            <input class="btn ydbz_x" type="button" onclick="exportExcel()" value="导出Excel"/>
        </div>
        <div class="ydxbd">
            <span></span>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">团队类型：</div>
                <div class="selectStyle">
                    <select name="orderS">
                        <c:forEach var="order" items="${orderTypes }">
                            <c:choose>
                                <c:when test="${isEFX}">
                                    <c:if test="${order.value eq '11' or order.value eq '12' or order.value eq '0'}">
                                        <option value="${order.value }"
                                                <c:if test="${orderS==order.value}">selected="selected"</c:if>>${order.label }</option>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <option value="${order.value }"
                                            <c:if test="${orderS==order.value}">selected="selected"</c:if>>${order.label }</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">地接社：</label>
                <select name="supplierInfo" style="width:278px;">
                    <option value="">请选择</option>
                    <c:forEach var="s" items="${supplierList }">
                        <option value="${s.id}"
                                <c:if test="${supplierId==s.id}">selected="selected"</c:if>>${s.supplierName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">渠道选择：</label>
                <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
                <select name="agentId">
                    <option value="">全部</option>
                    <c:choose>
                        <c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">
                            <option value="-1">未签</option>
                        </c:when>
                        <c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
                            <option value="-1">直客</option>
                        </c:when>
                        <c:otherwise>
                            <option value="-1">非签约渠道</option>
                        </c:otherwise>
                    </c:choose>
                    <c:forEach var="agentinfo" items="${agentinfoList }">
                        <%-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 --%>
                        <c:choose>
                            <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.agentName eq '非签约渠道'}">
                                <option value="${agentinfo.id }"
                                        <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>直客
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${agentinfo.id }"
                                        <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">申请日期：</label>
                <input id="createTimeMin" name="createTimeMin" class="inputTxt dateinput" value="${createTimeMin }"
                       readonly onClick="WdatePicker()"/> 至
                <input id="createTimeMax" name="createTimeMax" value="${createTimeMax }" readonly
                       onClick="WdatePicker()" class="inputTxt dateinput"/>
            </div>
            <c:choose>
                <c:when test="${confirmPay ne '1' }">
                    <div class="activitylist_bodyer_right_team_co1">
                        <div class="activitylist_team_co3_text">已付金额：</div>
                        <div class="selectStyle">
                            <select name="moneyNum">
                                <option value="" <c:if test="${moneyNum eq ''}">selected="selected"</c:if>>全部</option>
                                <option value="no" <c:if test="${moneyNum eq 'no'}">selected="selected"</c:if>>未付</option>
                            </select>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="activitylist_bodyer_right_team_co1">
                        <div class="activitylist_team_co3_text">付款状态：</div>
                        <div class="selectStyle">
                            <select name="pay">
                                <option value="">请选择</option>
                                <option value="N" <c:if test="${pay eq 'N'}">selected="selected"</c:if>>未付款</option>
                                <option value="Y" <c:if test="${pay eq 'Y'}">selected="selected"</c:if>>已付款</option>
                            </select>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

            <c:if test="${fns:getUser().userType ==1}">
                <input type="hidden" name="agentId" value="${fns:getUser().company.id}"/>
            </c:if>
            <div class="activitylist_bodyer_right_team_co1" id="jd">
                <div class="activitylist_team_co3_text">计调：</div>
                <select name="jd">
                    <option value="">请选择</option>
                    <c:forEach var="jd" items="${agentJd }">
                        <option value="${jd.id }"
                                <c:if test="${jds==jd.id}">selected="selected"</c:if>>${jd.name }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">支付方式：</div>
                <div class="selectStyle">
                    <select name="payType">
                        <option value="">请选择</option>
                        <option value="1" <c:if test="${payType eq '1'}">selected="selected"</c:if>>支票</option>
                        <option value="3" <c:if test="${payType eq '3'}">selected="selected"</c:if>>现金支付</option>
                        <option value="4" <c:if test="${payType eq '4'}">selected="selected"</c:if>>汇款</option>
                        <option value="6" <c:if test="${payType eq '6'}">selected="selected"</c:if>>银行转账</option>
                        <option value="7" <c:if test="${payType eq '7'}">selected="selected"</c:if>>汇票</option>
                        <option value="8" <c:if test="${payType eq '8'}">selected="selected"</c:if>>POS机刷卡</option>
                        <c:if test="${isLMT }">
                            <option value="9" <c:if test="${payType eq '9'}">selected="selected"</c:if>>因公支付宝</option>
                        </c:if>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">出纳确认时间：</label>
                <input id="confirmCashierDateBegin" name="confirmCashierDateBegin" class="inputTxt dateinput"
                       value="${confirmCashierDateBegin }" readonly onClick="WdatePicker()"/> 至
                <input id="confirmCashierDateEnd" name="confirmCashierDateEnd" value="${confirmCashierDateEnd }"
                       readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1" id="bank">
                <label class="activitylist_team_co3_text">来款银行：</label>
                <select name="bank">
                    <c:if test="${!isSelect}">
                        <option value=''>请选择</option>
                    </c:if>
                    <c:if test="${isSelect}">
                        <option value='${bankNamee}'>${bankNamee}</option>
                    </c:if>
                </select>
            </div>

            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">打印状态：</label>
                <div class="selectStyle">
                    <select name="printFlag">
                        <option value="">请选择</option>
                        <option value="0" <c:if test="${printFlag eq '0' }">selected="selected"</c:if>>未打印</option>
                        <option value="1" <c:if test="${printFlag eq '1' }">selected="selected"</c:if>>已打印</option>
                    </select>
                </div>
            </div>
            <%--bug17478 标签闭合错误 由于长度问题将币种选择改为币种 调整筛选项样式 by tlw at 20170307--%>
            <div class="activitylist_bodyer_right_team_co4">
                <label class="activitylist_team_co3_text">付款金额：</label>
                <div class="selectStyle">
                    <select id="currency" name="currency">
                        <option value="">币种</option>
                        <c:forEach items="${currencyList}" var="c">
                            <option value="${c.id}"
                                    <c:if test="${currencyId==c.id}">selected="selected"</c:if>>${c.currencyName}</option>
                        </c:forEach>
                    </select>
                </div>
                	<input type="text" value="${startMoney }" name="startMoney" id="startMoney" class="inputTxt"
                    	onkeyup="validNum(this)" onafterpaste="validNum(this))"/>
                 <span>~</span>
                	<input type="text" value="${endMoney }" name="endMoney" id="endMoney" class="inputTxt"
                    	onkeyup="validNum(this)" onafterpaste="validNum(this)"/>
            </div>
        </div>
    </div>
    </div>
</form:form>

<div class="activitylist_bodyer_right_team_co_paixu">
    <div class="activitylist_paixu">
        <div class="activitylist_paixu_left">
            <ul>
                <li class="activitylist_paixu_left_biankuang licreateDate">
                	<a onClick="sortby('createDate',this)">创建时间</a></li>
                <li class="activitylist_paixu_left_biankuang liupdateDate">
                	<a onClick="sortby('updateDate',this)">更新时间</a></li>
            </ul>
        </div>
        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        <div class="kong"></div>
    </div>
</div>
<table id="contentTable" class="table mainTable activitylist_bodyer_table" style="margin-left:0px;">
    <thead style="background:#403738;">
    <tr>
        <th colspan="2" width="6%">序号</th>
        <th width="9%">申请日期</th>
        <th width="9%">团号</th>
        <th width="6%">团队类型</th>
        <th width="9%">团队名称</th>
        <th width="9%"><span class="supplier on">地接社</span> / <span class="agent">渠道商</span></th>
        <%--<th width="9%">地接社/渠道商</th>--%>
        <th width="7%">款项</th>
        <th width="9%">付款金额<br>已付金额</th>
        <th width="6%">计调</th>
        <th width="6%">付款审核状态</th>
        <c:if test="${confirmPay eq '1' }">
            <th width="6%">出纳确认</th>
        </c:if>
        <th width="6%">出纳确认时间</th>
        <th width="6%">打印确认</th>
        <c:if test="${companyUUID eq '75895555346a4db9a96ba9237eae96a5' }"> <%-- 奢华之旅 --%>
            <th>发票状态</th>
        </c:if>
        <th width="4%">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${fn:length(page.list) <= 0 }">
        <tr><%-- 标题最大列为16，15改为16 modify by wangyang 2016.12.26 --%>
            <td colspan="16" style="text-align: center;">
                暂无搜索结果
            </td>
        </tr>
    </c:if>
    <c:forEach items="${page.list }" var="orders" varStatus="s">
        <tr>
            <td class="tc">
            	<%-- 修改jsp因版本等原因导致String.replace()方法报错 modify by wangyang 2016.12.26 --%>
                <input type="checkbox" name="checkBox"
                       value="${orders.id }_${orders.orderType }_1_${orders.rate }_${orders.amount }_${orders.currencyId }_${fns:getReplace(orders.groupCode,',','','_','')}_${orders.name }_${orders.payCreateDate }_${orders.payFlag}"
                       check-action="Normal"/>
                <input type="hidden" name="isNew" value="${orders.isNew }"/>
            </td>
            <td class="tc">${s.count }</td>
            <td class="tc"><fmt:formatDate value="${orders.payCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td class="tc">${orders.groupCode }</td>
            <td class="tc">${fns:getDictLabel(orders.orderType, "order_type", "")}</td>
            <td class="tc">${orders.acitivityName }</td>
            <td class="tc">
                <%--bug 17641  区分地接社和渠道商 by ruiqi.zhang  2017-3-27 11:02:43--%>
                <div title="${orders.groupCode}" class="supplier_cen onshow"><c:if test="${orders.supplyType eq 0 }">${orders.supplyName }</c:if></div>
                <div title="${orders.activityName}" class="agent_cen qtip"><c:if test="${orders.supplyType eq 1 }">
                <%-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 --%>
                <c:choose>
                    <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && orders.supplyName eq '非签约渠道'}">
                        直客
                    </c:when>
                    <c:otherwise>
                        ${orders.supplyName }
                    </c:otherwise>
                </c:choose>
                </c:if></div>
            </td>
            <td>${orders.name }</td>
            <td class="tc">
                <div class="yfje_dd">
                    <span class="fbold">
                    	<font id="moneyFlag${s.count }">${fns:getCurrencyNameOrFlag(orders.currencyId,'0')}</font>
                    	<fmt:formatNumber type="currency" value="${orders.amount}" pattern="#,##0.00" maxFractionDigits="2"/>
                    </span>
                    <span style="color: black;">/ <fmt:formatNumber
                        value="${orders.rate}" pattern="##.##" minFractionDigits="4"></fmt:formatNumber>    
                    </span>
                </div>
                <div class="dzje_dd">
                    <span class="fbold">${fns:getRefundPayedMoney(orders.id,"1",orders.orderType)}</span>
                </div>
            </td>
            <td class="tc">${fns:getUserNameById(orders.createBy) }</td>
            <td class="tc"><font style="color:green">已通过</font><%-- 列表页默认查询的就是付款审核通过的数据，所以此处直接写已通过 --%>
            </td>
            <c:if test="${confirmPay eq '1'}">
                <td class="tc z_548">
                    <c:if test="${orders.payFlag eq '0' }">
                        <font style="color:red" class="noPay">未付</font>
                    </c:if>
                    <c:if test="${orders.payFlag eq '1' }">
                        <font style="color:green" class="noPay">已付</font>
                    </c:if>

                    <!--548&&549-->
                    <c:if test="${not empty orders.payReviewUuid}">
                        <c:set var="reviewLogs" value="${fns:getHavingRemarkReviewLogs(orders.payReviewUuid)}"/>
                        <c:if test="${not empty reviewLogs}">
                        <c:set var="review" value="${fns:getReviewNewByUuid(orders.payReviewUuid)}" />
                        <c:if test="${empty review.extend4 or review.extend4 eq '0'}">
                        <div class="relative div_548">
                            <div class="remark_548">备注
                                <div class="remark_div">
                                    <em class="arrows_548"></em>
                                    <div class="remark_div_head">
                                        <div class="remark_head_left">审批备注</div>
                                        <div class="remark_head_right">
                                            <input type="checkbox" value="${orders.payReviewUuid}"/>
                                            <div>不再显示</div>
                                        </div>
                                    </div>
                                    <div class="remark_div_body">
                                        <div class="remark_div_child">
                                            <c:forEach var="reviewLog" items="${reviewLogs}">
                                                <!-- 审批通过/撤销  -->
                                                <c:if test="${reviewLog.operation eq 1 or reviewLog.operation eq 4}">
                                                    <ul>
                                                        <li class="left_548">
                                                            <div class="color_black">
                                                                <fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                            </div>
                                                            <div class="green_548">
                                                                <c:if test="${reviewLog.operation eq '1'}">审批通过</c:if>
                                                                <c:if test="${reviewLog.operation eq '4'}">撤销</c:if>
                                                            </div>
                                                        </li>
                                                        <li class="center_548"><em class="point-time step_yes"></em></li>
                                                        <li class="right_548">
                                                            <div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
                                                            ( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
                                                            <div class="right_body">
                                                                <div class="remarks">备注：</div>${reviewLog.remark}
                                                            </div>
                                                        </li>
                                                    </ul>
                                                </c:if>

                                                <!-- 审批驳回 -->
                                                <c:if test="${reviewLog.operation eq 2 }">
                                                    <ul>
                                                        <li class="left_548">
                                                            <div class="color_black">
                                                                <fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                            </div>
                                                            <div class="green_548">审批驳回</div>
                                                        </li>
                                                        <li class="center_548"><em class="point-time stop"></em></li>
                                                        <li class="right_548">
                                                            <div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
                                                            ( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
                                                            <div class="right_body">
                                                                <div class="remarks">备注：</div>${reviewLog.remark}
                                                            </div>
                                                        </li>
                                                    </ul>
                                                </c:if>

                                                <!-- 审批驳回  结束标志 -->
                                                <c:if test="${review.status eq 0 and reviewLog.sequenceNumber eq fn:length(reviewLogs)}">
                                                    <ul>
                                                        <li class="left_548">
                                                            <div class="color_black"></div>
                                                            <div class="green_548">审批驳回</div>
                                                        </li>
                                                        <li class="center_548"><em class="point-time end"></em></li>
                                                        <li class="right_548"><div class="right_head"></div></li>
                                                    </ul>
                                                </c:if>

                                                <!-- 审批通过 结束标志 -->
                                                <c:if test="${review.status eq 2 and reviewLog.sequenceNumber eq fn:length(reviewLogs)}">
                                                    <ul>
                                                        <li class="left_548">
                                                            <div class="color_black"></div>
                                                            <div class="green_548">审批通过</div>
                                                        </li>
                                                        <li class="center_548"><em class="point-time yes"></em></li>
                                                        <li class="right_548"><div class="right_head"></div></li>
                                                    </ul>
                                                </c:if>

                                                <!-- 审批取消,审批中 结束标志 -->
                                                <c:if test="${review.status ne 0 and review.status ne 2
                                                                and reviewLog.sequenceNumber eq fn:length(reviewLogs)
                                                                and reviewLog.sequenceNumber ne 1}">
                                                    <ul>
                                                        <li class="left_548"><div class="color_black"></div><div class="green_548"></div></li>
                                                        <li class="center_548"><em class="point-time next"></em></li>
                                                        <li class="right_548"><div class="right_head"></div></li>
                                                    </ul>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:if>
                        </c:if>
                    </c:if>
                    <!--548&&549end-->
                </td>
            </c:if>
            <td class="tc"><fmt:formatDate value="${orders.confirmCashierDate}" pattern="yyyy-MM-dd"/></td>
            <td class="invoice_yes" id="print_${orders.id}_${orders.orderType}">
                <c:if test="${empty orders.printFlag || orders.printFlag == 0 }">未打印</c:if>
                <c:if test="${orders.printFlag == 1 }">
                	<p class="uiPrint">已打印<span style="display: none;">首次打印时间<br>  
                		<fmt:formatDate value="${orders.printTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
                </c:if>
            </td>
            <c:if test="${companyUUID eq '75895555346a4db9a96ba9237eae96a5' }">
                <td class="invoice_yes">
                    <c:if test="${orders.invoiceStatus == 0 }">未收</c:if>
                    <c:if test="${orders.invoiceStatus == 1 }">已收</c:if>
                </td>
            </c:if>
            <td class="p0">
                <dl class="handle">
                    <dt><img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png"></dt>
                    <dd><p>
                        <span></span>
                        <a href="${ctx}/cost/manager/paymentConfirm/${orders.id}/${orders.orderType}"
                           target="_blank">付款</a>
                        <a onclick="expand('#child1_${orders.id}',this,${orders.id},${orders.orderType },'1', ${orders.isNew})"
                           href="javascript:void(0)">支付记录</a>
                        <c:if test="${orders.orderType eq '1' || orders.orderType eq '2' || orders.orderType eq '3' || orders.orderType eq '4' || orders.orderType eq '5' || orders.orderType eq '10'}">
                            <a href="${ctx}/costReview/activity/activityCostReviewDetail/${orders.proid}/${orders.groupid}/2?read=1&head=1"
                               target="_blank">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType eq '6' }">
                            <a href="${ctx}/costReview/visa/visaCostReviewDetail/${orders.proid}/2?read=1&head=1"
                               target="_blank">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType eq '7' }">
                            <a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${orders.proid}/2?read=1&head=1"
                               target="_blank">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType eq '11' }">
                            <a target="_blank"
                               href="${ctx}/cost/review/hotelRead/${orders.proid}/${orders.groupid}/1?from=operatorPre&menuid=1&isEFX=${isEFX}">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType eq '12' }">
                            <a target="_blank"
                               href="${ctx}/cost/review/islandRead/${orders.proid}/${orders.groupid}/1?from=operatorPre&menuid=1&isEFX=${isEFX}">查看</a>
                        </c:if>
                        <c:if test="${confirmPay eq '1' }">
                            <c:if test="${orders.payFlag eq '0' || orders.payFlag eq ''}">
                                <a href="javascript:void(0)"
                                   onclick="jbox_paymentconfirmLx(${orders.currencyId },${s.count },${orders.amount},${orders.rate},'${ctx }',${orders.id },'1','1',${orders.payFlag },${orders.orderType })">付款确认</a>
                            </c:if>
                            <c:if test="${orders.payFlag eq '1'}">
                                <a href="javascript:void(0)"
                                   onclick="confirmOrCannePay('${ctx }',${orders.id },'1','0',${orders.payFlag },${orders.orderType })">撤销付款</a>
                            </c:if>
                        </c:if>
                        <c:if test="${companyUUID eq '75895555346a4db9a96ba9237eae96a5' }">
                            <c:if test="${orders.invoiceStatus eq '1' }">
                                <a onclick="confirmOrCannelInvoice('${ctx }','${orders.id}','1','1')"
                                   href="javascript:void(0)">撤销发票</a>
                            </c:if>
                            <c:if test="${orders.invoiceStatus ne '1'}">
                                <a onclick="confirmOrCannelInvoice('${ctx }','${orders.id}','0','0')"
                                   href="javascript:void(0)">确认发票</a>
                            </c:if>
                        </c:if>
                        <a onclick="showDownloadWin('${ctx}','${orders.costVoucher }',false)">下载附件</a>
                        <!-- 11为酒店，12为海岛游 -->
                        <c:choose>
                            <c:when test="${orders.orderType eq '11' || orders.orderType eq '12'}">
                                <a href="${ctx }/cost/manager/paymentForHotelAndIslandPrint/${orders.id}/${orders.orderType}"
                                   target="_blank">打印</a>
                            </c:when>
                            <c:otherwise>
                                <shiro:hasPermission name="costpay:operation:print">
                                    <a href="${ctx }/cost/manager/payment/${orders.id}/${orders.orderType}/${orders.isNew}?option=order"
                                       target="_blank">打印</a>
                                </shiro:hasPermission>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${not empty orders.activityId}">
                            <shiro:hasPermission name="costpay:operation:statement">
                                <a href="${ctx }/cost/manager/forcastList/${orders.activityId}/${orders.orderType}"
                                   target="_blank">预报单</a>
                            </shiro:hasPermission>
                        </c:if>
                        <c:if test="${not empty orders.activityId}">
                            <shiro:hasPermission name="costpay:operation:finalStatement">
                                <a href="${ctx }/cost/manager/settleList/${orders.activityId}/${orders.orderType}"
                                   target="_blank">结算单</a>
                            </shiro:hasPermission>
                        </c:if>
                    </p></dd>
                </dl>
            </td>
        </tr>

        <tr id="child1_${orders.id}" class="activity_team_top1" style="display:none">
            <td colspan="15" class="team_top">
                <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                    <thead>
                    <tr>
                        <th class="tc" width="10%">付款方式</th>
                        <th class="tc" width="10%">金额</th>
                        <th class="tc" width="15%">日期</th>
                        <th class="tc" width="10%">支付类型</th>
                        <th class="tc" width="25%">支付凭证</th>
                        <th class="tc" width="10%">状态</th>
                        <th class="tc" width="15%">操作</th>
                    </tr>
                    </thead>
                    <tbody id='rpi_${orders.id}'>

                    </tbody>
                </table>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<div id="contentTable_foot">--%>
    <%--<label><input check-action="All" type="checkbox"> 全选</label>--%>
    <%--<label><input check-action="Reverse" type="checkbox"> 反选</label>--%>
    <%--<c:if test="${confirmPay eq '1'}">--%>
        <%--<input type="button" class="btn-primary" value="批量确认付款"--%>
               <%--onclick="paymentconfirmallLx('${ctx }','checkBox','1',1)"/>--%>
        <%--<c:if test="${companyUUID eq '75895555346a4db9a96ba9237eae96a5'}">--%>
            <%--<input type="button" class="btn-primary" value="批量确认发票"--%>
                   <%--onclick="confirmOrCannelInvoiceValues('${ctx }','checkBox','2',1)"/>--%>
        <%--</c:if>--%>
    <%--</c:if>--%>
    <%--<shiro:hasPermission name="costpay:operation:print">--%>
        <%--&nbsp;<input type="button" class="btn-primary" value="批量打印" onclick="batchPrint()" />--%>
    <%--</shiro:hasPermission>--%>
<%--</div>--%>

        <div class="page" id="contentTable_foot">
            <div class="pagination">
                <dl>
                    <dt><input check-action="All" type="checkbox">全选</dt>
                    <dt><input check-action="Reverse" type="checkbox">反选</dt>
                    <dd>
                        <c:if test="${confirmPay eq '1'}">
                            <input type="button" class="btn ydbz_x" value="批量确认付款"
                                   onclick="paymentconfirmallLx('${ctx }','checkBox','1',1)"/>
                            <c:if test="${companyUUID eq '75895555346a4db9a96ba9237eae96a5'}">
                                <input type="button" class="btn ydbz_x" value="批量确认发票"
                                       onclick="confirmOrCannelInvoiceValues('${ctx }','checkBox','2',1)"/>
                            </c:if>
                        </c:if>
                        <shiro:hasPermission name="costpay:operation:print">
                            <input type="button" class="btn ydbz_x " onclick="batchPrint();" value="批量打印">
                        </shiro:hasPermission>
                    </dd>
                </dl>
            </div>

            <div class="pagination clearFix">
                ${page}
            </div>
        </div>

</div>

<div id="payment_confirm_pop_o" class="display-none">
    <div class="payment_confirm_pop">
        <table class="table activitylist_bodyer_table orderlist-list" style="margin:0 auto;">
            <thead>
            <tr>
                <th class="tc" width="7%">付款金额</th>
                <th class="tc" width="4%"></th>
                <th class="tc" width="5%">汇率</th>
                <th class="tc" width="4%"></th>
                <th class="tc" width="15%">转换后金额</th>
                <th class="tc" width="15%">出纳确认时间</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="tc payment"><font id="moneyFlag"></font><span name="payment"></span></td>
                <td class="tc">x</td>
                <td class="tc"><input class="rate inputTxt inputTxtlong" onkeyup='checkNum(this)'
                                      onafterpaste='checkNum(this)' name="rate" id="singleRate"></td>
                <td class="tc">=</td>
                <td class="afterexchange tc">¥<span name="afterexchange"></span></td>
                <td class="tc"><input id="confirmCashierDate" name="confirmCashierDate" class="inputTxt dateinput"
                                      value="<%=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())%>"
                                      readonly onClick="WdatePicker()"/></td>
            </tr>
            <tbody>
        </table>
    </div>
</div>

<!--S 0137 批量确认付款弹窗-->
<div id="payment_confirmall_pop_o" class="display-none">
    <div class="payment_confirmall_pop">
        <table class="table activitylist_bodyer_table orderlist-list" style="margin:0 auto;">
            <thead>
            <tr>
                <th class="tc" width="6%">申请日期</th>
                <th class="tc" width="6%">团号</th>
                <th class="tc" width="6%">款项</th>
                <th class="tc" width="6%">付款金额</th>
                <th class="tc" width="6%">汇率<br>
                	<input id="rateCopy" class="inputTxt inputTxtlong" value="" onkeyup='checkNum(this)' 
                		onafterpaste='checkNum(this)'><br>
                	<input id="visaCopyBtn" type="button" value="复制" class="rateCopy visa_copy">
                </th>
                <th class="tc" width="6%">转换后金额</th>
                <th class="tc" width="6%">出纳确认时间<br>
                	<input id="confirmCashierDatee" class="inputTxt dateinput" onClick="WdatePicker()"><br>
                	<input id="dateCopyBtn" type="button" value="复制" class="dateCopy visa_copy">
                </th>
            </tr>
            </thead>
            <tbody id="confirmallList"></tbody>
        </table>
    </div>
</div>
<!--E 0137 批量确认付款弹窗-->
<style type="text/css">
    /*.jbox-title-panel{background: #3a7850 !important;}
   div.jbox .jbox-button{background: #3a7850 !important; color:#FFF; font-size:12px;}
   .jbox-title{}*/
    .orderList_dosome {
        text-align: left;
        margin-left: 11px;
    }

    .orderList_line {
        height: 100%;
        width: 50px;
        float: left;
    }
</style>
</body>
</html>