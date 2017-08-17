<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>订单管理-订单收款</title>
    <meta name="decorator" content="wholesaler"/>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctxStatic}/common/jquery.file.filter.js"></script>

    <script type="text/javascript" src="${ctxStatic}/modules/activity/activity.js"></script>

    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>

    <style>
        div.jbox .jbox-content {
            min-height: 24px;
            line-height: 18px;
            color: #444444;
            -ms-overflow-y: hidden !important;
        }

        .dell {
            display: inline-block;
            width: 12px;
            height: 15px;
            background: url("css/img/glyphicons-halflings.png");
            text-indent: -9999999px;
            background-position: -313px 2px;
            margin-left: 5px;
        }
    </style>
    <script type="text/javascript" type="text/javascript">
        String.prototype.endWith = function (s) {
            if (s == null || s == "" || this.length == 0 || s.length > this.length)
                return false;
            if (this.substring(this.length - s.length) == s)
                return true;
            else
                return false;
            return true;
        };

        function toBankAccountChange() {
            var val = $("#toBankAccount").val();
            if (val != "-1") {
                $("#errorBankAccount").hide();
            }
            if (val == "-1") {
                $("#errorBankAccount").show();
            }
        }
        //表单验证
        $().ready(function () {
            $("#offlineform_1").validate({
                onsubmit: false
            });
            $("#offlineform_2").validate({
                onsubmit: false
            });
            $("#offlineform_3").validate({
                onsubmit: false
            });
            $("#offlineform_4").validate({
                onsubmit: false
            });
            $("#offlineform_5").validate({
                onsubmit: false
            });
            $("#offlineform_9").validate({
                onsubmit: false
            });
            jQuery.extend(jQuery.validator.messages, {
                required: "必填信息",
                number: "输入信息有误",
                digits: "输入信息有误",
                email: "email格式错误",
                min: jQuery.validator.format("请选择收款行！")
            });
        });

        jQuery(function ($) {
            $.fn.datepicker = function (option) {
                var opt = $.extend({}, option);
                $(this).click(function () {
                    WdatePicker(opt);
                });
            };
        });

        var submit_times = 0;
        $(function () {
            $("[name='invoiceDate']").datepicker();

            //  S 89需求-->
            $("[name='bankName']").comboboxInquiry({
                removeIfInvalid: false
            });
            $("[name='bankAccount']").comboboxInquiry({
                removeIfInvalid: false
            });

            $("[name='accountName']").comboboxInquiry();

            $('[name="bankName"]+.custom-combobox').on("autocompletechange", function (event, ui) {
                var value = $(this).children('input').val(),
                        valueLowerCase = value.toLowerCase(),
                        valid = false;
                $(this).prev().children("option").each(function () {
                    if ($(this).text().toLowerCase() === valueLowerCase) {
                        this.selected = valid = true;
                        return false;
                    }
                });
                if (valid) {
                    //this._trigger("afterInvalid", null, value);
                    return;
                }
                if (!valid) {
                    $("#bankAccount").html('<option value=""></option>');
                }
            });

            $('[name="bankName"]').on("comboboxinquiryselect", function () {
                var selectVal = $(this).val();
                var bankName = encodeURI(encodeURI(selectVal));
                var supplierId = $(this).attr('agentId');
                if (bankName != -1) {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/orderPay/getAgentOpenBankById/" + supplierId,
                        cache: false,
                        async: false,
                        data: "bankName=" + bankName,
                        dataType: "json",//返回的数据类型
                        success: function (data) {
                            var options = '';
                            if (data != null) {
                                for (var i = 0; i < data.length; i++) {
                                    options += '<option value="' + data[i][5] + '">'
                                            + data[i][5] + '</option>';
                                }
                            }
                            $("#bankAccount").html('');
                            $("#bankAccount").append(options);
                        },
                        error: function () {
                            alert('返回数据失败');
                        }
                    });
                }
            });
            $('[name="bankName"]+.custom-combobox input').on('keyup', function () {
                var value = this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g, '');
                this.value = value.length > 100 ? value.substr(0, 100) : value;

            });
            $('[name="bankAccount"]+.custom-combobox input').on('keyup', function () {
                var value = this.value.replace(/[^0-9\s]/g, '');
                this.value = value.length > 100 ? value.substr(0, 100) : value;
            });

            //E 89需求-->

            $(".patorder_a1").click(
                    function () {
                        $(this).css({
                            "color": "#3A7851",
                            "backgroundColor": "#FFF"
                        }).siblings().css({
                            "color": "#000",
                            "backgroundColor": ""
                        });
                        $(this).parent().siblings().children('#offlinebox_1').show().siblings().hide();
                    }).click();

            $(".patorder_a2").click(
                    function () {
                        $(this).css({
                            "color": "#3A7851",
                            "backgroundColor": "#FFF"
                        }).siblings().css({
                            "color": "#000",
                            "backgroundColor": ""
                        });
                        $(this).parent().siblings().children('#offlinebox_3').show().siblings().hide();
                    });

            $(".patorder_a3").click(
                    function () {
                        $(this).css({
                            "color": "#3A7851",
                            "backgroundColor": "#FFF"
                        }).siblings().css({
                            "color": "#000",
                            "backgroundColor": ""
                        });
                        $(this).parent().siblings().children('#offlinebox_4').show().siblings().hide();
                    });

            $(".patorder_a4").click(
                    function () {
                        $(this).css({
                            "color": "#3A7851",
                            "backgroundColor": "#FFF"
                        }).siblings().css({
                            "color": "#000",
                            "backgroundColor": ""
                        });
                        $(this).parent().siblings().children('#offlinebox_5').show().siblings().hide();
                    });
            $(".patorder_a9").click(
                    function () {
                        $(this).css({
                            "color": "#3A7851",
                            "backgroundColor": "#FFF"
                        }).siblings().css({
                            "color": "#000",
                            "backgroundColor": ""
                        });
                        $(this).parent().siblings().children('#offlinebox_9').show().siblings().hide();
                    });
            $payforRadio = $('input[name=paymentStatus]');
            $payforRadio.change(function () {
                var payforRadioVal = $(this).val();
                if (payforRadioVal != 1) {
                    /* 				top.$.jBox.confirm('您是否按月结支付？', '系统提示', function(v) {
                     if (v == 'ok') { */
                    $('#payBtn').text('确认');
                    /* 					} else {
                     $('input[name=paymentStatus]')[1].checked = true;
                     $('#payBtn').text('确认支付');
                     }
                     }, {
                     buttonsFocus : 1
                     });
                     top.$('.jbox-body .jbox-icon').css('top', '55px');
                     return false; */
                } else {
                    $('#payBtn').text('确认收款');
                }
            });
        });
        function changeBank(bankId, agentId, type) {
            $.ajax({
                type: "POST",
                url: "${ctx}/orderPay/getAccountByBankId/" + bankId + "/"
                + agentId + "/" + type,
                cache: false,
                async: false,
                dataType: "json",//返回的数据类型
                data: {
                    format: "json"
                },
                success: function (data) {
                    var options = '';
                    if (data != null) {
                        for (var i = 0; i < data.length; i++) {
                            options += '<option value=' + data[i].id + '>'
                                    + data[i].bankAccountCode + '</option>';
                        }
                    }
                    $("#inAccountSelect").html('');
                    $("#inAccountSelect").append(options);
                },
                error: function () {
                    alert('返回数据失败');
                }
            });

        }

        //开户行
        function changeOpenBank(supplierId, bankName, obj) {
            bankName = encodeURI(encodeURI(bankName));
            if (bankName != -1) {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/orderPay/getOpenBankById/" + supplierId,
                    cache: false,
                    async: false,
                    data: "bankName=" + bankName,
                    dataType: "json",//返回的数据类型
                    success: function (data) {
                        var options = '';
                        if (data != null) {
                            for (var i = 0; i < data.length; i++) {
                                options += '<option value="' + data[i][5] + '">'
                                        + data[i][5] + '</option>';
                            }
                        }
                        //$(obj).parent().parent().next().find("select[name='toBankAccount']").html('');
                        //$(obj).parent().parent().next().find("[name='toBankAccount']").append(options);
                        $("select[name=toBankAccount]").html("");
                        $("select[name=toBankAccount]").append(options);
                        toBankAccountChange();
                    },
                    error: function () {
                        alert('返回数据失败');
                    }
                });
            } else {
                var options = '<option value="-1">--请选择--</option>';
                $("#toBankAccount").html('');
                $("#toBankAccount").append(options);
                toBankAccountChange();
            }
        }

        function formSunbmit() {

            var flag = checkMoney();
            if (!flag) {
                return false;
            }

            // 判断当前活动DIV
            var payDivId = ""; // 活动ID
            $(".payDiv").each(function () {
                var block = $(this).css('display');
                if (block == 'block') {
                    payDivId = $(this).attr('id');
                }
            });

            // 保存月结/后付费的状态
            var payforRadio = 1;
            if ($('input:radio[name=paymentStatus]:checked').length > 0) {
                payforRadio = $('input:radio[name=paymentStatus]:checked').val();
            }

            var $paymentStatus = $('#' + payDivId)
                    .find('input[name=paymentStatus]');
            $paymentStatus.val(payforRadio);

            // 立即支付的情况下
            if (payforRadio == 1) {
                //校验支票付款
                if ("offlinebox_1" == payDivId) {

                    var r = $("#offlineform_1").validate({
                        //-------校验开始--------
                        onfocusout: function (element) { //失去焦点就进行校验
                            $(element).valid();
                        }
                        //--------校验结束--------
                    });

                    if (!r.form()) {
                        return false;
                    }

                    if (!checkifuploadZFPZ()) {
                     top.$.jBox.tip("请上传支付凭证！");
                     return false;
                     } 
                    var payerName = $("input[id=payerNameID_1]").val();
                    if ($.trim(payerName).length == 0) {
                        top.$.jBox.tip("请填写来款单位 ！");
                        return false;
                    }
					
                } else if ("offlinebox_5" == payDivId) {
                    var r = $("#offlineform_5").validate({
                        //-------校验开始--------
                        onfocusout: function (element) { //失去焦点就进行校验
                            $(element).valid();
                        }
                        //--------校验结束--------
                    });

                    if (!r.form()) {
                        return false;
                    }

                    if (!checkifuploadZFPZ()) {
                        top.$.jBox.tip("请上传支付凭证！");
                        return false;
                    }

                }

                // 汇款
                else if ("offlinebox_4" == payDivId) {
                    var r = $("#offlineform_4").validate({
                        //-------校验开始--------
                        onfocusout: function (element) { //失去焦点就进行校验
                            $(element).valid();
                        }
                        //--------校验结束--------
                    });

                    var isSHZL = "${isSHZL}";// 127 判断用户是否为奢华之旅

                    if (!r.form()) {
                        return false;
                    }

                    //127 start 未选择收款账户时提示 0330
                    if (isSHZL) {
                        if ($("select[id=toBankAccount]").val() == "-1") {
                            $("label[id=errorBankAccount]").show();
                        }
                    }
                    //127 end 未选择收款账户时提示 0330

                    if (!checkifuploadZFPZ()) {
                        top.$.jBox.tip("请上传支付凭证！");
                        return false;
                    }
                    var payerName = $("input[id=payerNameID_4]").val();
                    if ($.trim(payerName).length == 0) {
                        top.$.jBox.tip("请填写来款单位 ！");
                        return false;
                    }

                    if (isSHZL) {
                        var toBankNnameChoice = $("#toBankNname").val();
                        if (toBankNnameChoice == -1) {
                            top.$.jBox.tip("请选择收款行！");
                            submit_times == 0;
                            return;
                        }
                        var toBankAccountChoice = $("#toBankAccount").val();
                        if (toBankAccountChoice == -1) {
                            top.$.jBox.tip("请选择收款账户！");
                            submit_times == 0;
                            return;
                        }
                    }
                }
                // 现金支付
                else if ("offlinebox_3" == payDivId) {
                    var r = $("#offlineform_3").validate({
                        //-------校验开始--------
                        onfocusout: function (element) { //失去焦点就进行校验
                            $(element).valid();
                        }
                        //--------校验结束--------
                    });
                    if (!r.form()) {
                        return false;
                    }
                    var payerName = $("input[id=payerNameID_3]").val();
                    if ($.trim(payerName).length == 0) {
                        top.$.jBox.tip("请填写来款单位 ！");
                        return false;
                    }
                }
                else if ("offlinebox_9" == payDivId) {
                    var r = $("#offlineform_9").validate({
                        //-------校验开始--------
                        onfocusout: function (element) { //失去焦点就进行校验
                            $(element).valid();
                        }
                        //--------校验结束--------
                    });
                    if (!r.form()) {
                        return false;
                    }
                    if (!checkifuploadZFPZ()) {
                        top.$.jBox.tip("请上传收款凭证！");
                        return false;
                    }
                    var payerName = $("input[id=payerNameID_9 ]").val();
                    if ($.trim(payerName).length == 0) {
                        top.$.jBox.tip("请填写来款单位 ！");
                        return false;
                    }
            }
            }
            if (submit_times == 0) {
		        var bankName = $('[name="bankName"]+.custom-combobox input').val();
                var bankAccount = $('[name="bankAccount"]+.custom-combobox input').val();
                if ($.trim(bankName).length > 100) {
                    top.$.jBox.tip("来款行名称不能超过100个字符");
                    return false;
                }
                if ($.trim(bankAccount).length > 100) {
                    top.$.jBox.tip("来款账号不能超过100个字符");
                    return false;
                }
                $('[name="realBankName"]').val(bankName);
                $('[name="realBankAccount"]').val(bankAccount);
                submit_times++;
                $(".payDiv>form:visible").submit();
            }

        }
        function getPostData(obj) {
            var postData = {
                requestType: "mtour data",
                param: base64encode(JSON.stringify(obj))
            }
            return postData;
        }
        //判断是否上传了支付凭证
        function checkifuploadZFPZ() {
            var pathnum = $(".batch-ol li").length;
            if (pathnum <= 0) {
                return false;
            }
            return true;
        }

        function valdate(flag, obj, chushiValue, bizhong) {
            if (!isNaN(obj.value)) {
                if ("false" == flag) {
                    if (parseInt(obj.value) < parseInt(chushiValue)) {
                        obj.focus();
                        top.$.jBox.tip("当前金额是" + obj.value + "," + bizhong
                                + "金额不能低于" + chushiValue);
                    }
                }
            } else {
                obj.focus();
                top.$.jBox.tip(bizhong + "金额只能输入数字");
            }
        }

        function nownotpay(url) {
            if (window.opener) {
                window.close();
            } else {
                window.location.href = url;
            }
        }

        function checkMoney() {
            var price = $(".payforDiv").find("input[name=dqzfprice].valid").val();
            if (price == "") {
                price = 0;
            }
            var money = parseFloat(price);
            if (money > 100000000) {
                top.$.jBox.tip("金额不能大于100000000");
                return false;
            } else {
                return true;
            }
        }

        function checkrequired(obj) {
            if ($(obj).val().trim() == '') {
                $(obj).next().text("必填信息");
                return true;
            } else {
                $(obj).next().empty();
                return false;
            }

        }

        var $node;
        function relationInvoiceList() {
            var $pop = $.jBox($("#relationInvoice").html(), {
                        title: "发票列表", buttons: {'提交': 1, '关闭': 0}, submit: function (v, h, f) {
                            if (v == "0") {
                            } else if (v == "1") {
                                var checks = $("input[name='invoiceName']:checked");
                                var invoiceIds = "";
                                checks.each(function (i, element) {
                                    invoiceIds += $(element).val();
                                    invoiceIds += ",";
                                })
                                invoiceIds = invoiceIds.slice(0, -1);
                                $("input[name='relationInvoiceIds']").val(invoiceIds);
                                // $.jBox.tip("选定关联发票成功！");
                                if (invoiceIds == "") {
                                    $.jBox.tip("请先选择要关联的发票!");
                                    return false;
                                }
                            }
                        }, loaded: function (h, f) {
                            $node = h.find("#relationInvoiceTable");
                            searchRelationInvoice();
                        }, height: 350, width: 700
                    }
            );
        }

        // ajax进入后台查询，返回json再解析
        function searchRelationInvoice() {
            var orderId = $("input[name=orderId]").val();
            $.ajax({
                type: "POST",
                url: sysCtx + "/orderPay/relationInvoiceList",
                data: {
                    orderId: orderId
                },
                success: function (result) {
                    var html = "";
                    if (result == undefined || result == null) {
                        top.$.jBox.tip("获取关联发票记录失败！");
                    }
                    if (result == '') {
                        $node.empty().append(html);
                    }
                    var json = eval(result);
                    // json数组个数
                    var jsonLength = json.length;
                    // 判断为空
                    if (jsonLength && jsonLength != 0) {
                        // 循环获取html组合
                        for (var i = 0; i < jsonLength; i++) {
                            // 序列值
                            var indexVal = i + 1;
                            html += "<tr>";
                            // 选择
                            html += "<td><input name='invoiceName' type='checkbox' value='" + json[i][0] + "'></td>";
                            // 发票号
                            html += "<td name='operatorName' class='tc'><span>" + json[i][1] + "</span></td>";
                            // 团号
                            html += "<td name='operation' class='tc'><span>" + json[i][2] + "</span></td>";
                            // 申请人
                            html += "<td name='operationTime' class='tc'><span>" + json[i][5] + "</span></td>";
                            // 开票状态
                            html += "<td name='mainContext' class='tc'><span>" + json[i][3] + "</span></td>";
                            // 开票金额
                            html += "<td name='mainContext' class='tr'><span>¥ " + json[i][4] + "</span></td>";
                            html += "</tr>";
                        }
                        $node.empty().append(html);
                    }
                }
            });
        }


        function getAlipayAccount() {
            var name = $("select[name='toAlipayName']").val();
            $.ajax({
                type: "post",
                url: "${ctx}/orderPay/getAlipayAccount",
                data: {
                    "name": name
                },
                success: function (result) {
                    var select = $("select[name='toAlipayAccount']");
                    select.children().remove();
                    var html = "";
                    if (name == "") {
                        html += "<option value=''>请选择</option>";
                    } else {
                        $.each(result, function (i, a) {
                            html += "<option value='" + a.account + "'>" + a.account + "</option>";
                        });
                    }
                    select.append(html);
                }
            });
        }
	$(function(){
		var forms = $(".formTotal");
		for(var j = 0 ; j < forms.length ; j++){
			var form = $(forms[j]); 
			var dqzfprices = form.find("input[name='dqzfprice']");
			var returnPrice = form.find("input[name = 'returnPrice']");
			var c = "";
			for(var i = 0 ; i < dqzfprices.length ; i++){
				var dqzfprice = $(dqzfprices[i]);
				var mark = dqzfprice.parent().prev().text().charAt(dqzfprice.parent().prev().text().length-1);
				var value = dqzfprice.val();
				if(mark == "${differenceCurrencyMark}"){
					value = parseFloat(dqzfprice.val()) + parseFloat(returnPrice.val());
				}
				value = mark+Number(value).toFixed(2);
				if(c == ""){
					c = value;
				}else{
					c =value +"+"+c
				}
			}
			form.find("td[class = 'all_sum']").text(c);
		}	
	});
	function total(obj){
		var table = $(obj).parent().parent().parent().parent();
		var dqzfprices = table.find("input[name = 'dqzfprice']");
		var returnPrice = table.find("input[name = 'returnPrice']");
	   if("${differenceFlag}"=="1"){
			if(returnPrice.attr("name")=="returnPrice"){
				var payedReturn = Number("${returnDifference.returnPrice}");
				var a = "${differenceMoney}";
				if(a==""){
					a = 0
				}
				if(Number(returnPrice.val())+payedReturn>Number(a)){
					returnPrice.parent().find("span").remove();
					var span="<span style = 'color:#f00;'>已超出门店结算价差额返还</span>"
					returnPrice.parent().append(span);
				}else{
					returnPrice.parent().find("span").remove();
				}
			}
		}	
		var c = "";
			for(var i = 0 ; i < dqzfprices.length ; i++){
				var dqzfprice = $(dqzfprices[i]);
				var mark = dqzfprice.parent().prev().text().charAt(dqzfprice.parent().prev().text().length-1);
				var value = dqzfprice.val();
				if(mark == "${differenceCurrencyMark}"){
					value = Number(dqzfprice.val()) + Number(returnPrice.val());
				}
				value = mark + Number(value).toFixed(2);
				if(c == ""){
					c = value;
				}else{
					c =value +"+"+c
				}
			}
			table.find("td[class = 'all_sum']").text(c);
	}
	
	function invoiceInputin(obj){
		var $this=obj;
	    var t = $this.value.charAt(0); 
	    $this.value = $this.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
	    $this.value = $this.value.replace(/^\./g,""); //验证第一个字符是数字而不是
	    $this.value = $this.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
	    $this.value = $this.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	    $this.value = $this.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	}
    </script>
</head>
<body>
<div class="ydbzbox fs">
    <div class="ydbz yd-step4 ydbz-get">&nbsp;</div>
    <div class="payforDiv">
			
        <div class="payforprice">
            您需要收款的最终金额为：
            <c:if test="${empty paramCurrencyId}">
                <span><i>0.00</i></span>
            </c:if>
            <c:if test="${not empty paramCurrencyId}">
                <c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
                    <c:forEach items="${curlist}" var="cur">
                        <c:if test="${cur.id==currencyId}">
                            <em class="gray14" style="vertical-align: baseline;">${cur.currencyMark}</em>
                            <span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                value="${paramCurrencyPrice[status.index]}"/></span>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </c:if>
            <c:if test="${totalCurrencyFlag}">
                <c:if test="${not empty paramTotalCurrencyId}">
                    订单总额为：
                    <c:forEach items="${paramTotalCurrencyId}" var="currencyId" varStatus="status">
                        <c:forEach items="${curlist}" var="cur">
                            <c:if test="${cur.id==currencyId}">
                                <em class="gray14" style="vertical-align: baseline;">${cur.currencyMark}</em>
                                <span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                    value="${paramTotalCurrencyPrice[status.index]}"/></span>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </c:if>
            </c:if>
        </div>

        <div class="payforchose">选择您的收款类型</div>

        <div id="offline_paybox" class="pay_clearfix" style="clear: both;">
            <div id="payorderbgcolor" style="display: block; z-index: 2;height:30px; position:relative;">
                <!-- <div class="patorder_a4" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; line-height: 29px; color: rgb(58, 120, 81); margin-bottom: -1px; float: left; display: block; cursor: pointer; background-color: rgb(255, 255, 255);">快速支付</div> -->
                <div class="patorder_a1"
                     style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: inherit;">
                    支票
                </div>
                <div class="patorder_a2"
                     style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">
                    现金支付
                </div>
                <div class="patorder_a3"
                     style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">
                    汇款
                </div>
                <c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
                    <div class="patorder_a9"
                         style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204); border-radius: 3px 3px 0px 0px; width: 90px; height: 29px; text-align: center; color: rgb(58, 120, 81); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: rgb(255, 255, 255);">
                        因公支付宝
                    </div>
                </c:if>
                <div style="display:none;">
                    <form action="${ctx}/orderPay/paySuccess" method="post" id="formDisPlay">
                        <input name="orderDetailUrl" id="orderDetailUrl" value="${orderDetailUrl }"/>
                        <input name="data" id="data" value=""/>
                        <input name="input" id="input" value=""/>
                    </form>
                </div>

                <c:if test="${!empty paymentStatusFlag && paymentStatusFlag != 0}">

                    <div class="patorder_radio">
                        <em class="xing">*</em>${paymentStatusLblDesc}：
                        <c:choose>
                            <c:when test="${paymentTypeRadioFlag == 0}">
                                <label><input type="radio" name="paymentStatus" value="${paymentStatus}">是</label>
                                <label><input type="radio" name="paymentStatus" value="1" checked="checked">否</label>
                            </c:when>
                            <c:when test="${paymentTypeRadioFlag == 1}">
                                <label><input type="radio" name="paymentStatus" value="${paymentStatus}"
                                              disabled="disabled">是</label>
                                <label><input type="radio" name="paymentStatus" value="1" disabled="disabled"
                                              checked="checked">否</label>
                            </c:when>
                        </c:choose>
                    </div>
                </c:if>
            </div>

            <div class="payORDER"
                 style="clear:both; padding:20px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">

                <!-- 支票 -->
                <div id="offlinebox_1" class="payDiv" >
                    <form id="offlineform_1" method="post" action="${ctx}/orderPay/savePay"
                          style="margin:0px; padding:0px;" class = "formTotal">
                        <span style="color:#f00;">*为确保您的订单能够及时处理，请正确填写以下信息。</span>
                        <c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
                            <c:if test="${hasPreOpeninvoice }">
                                <span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);"
                                                                    onclick="relationInvoiceList()"
                                                                    target="_self">关联发票</a></span>
                            </c:if>
                        </c:if>
                        <%@ include file="/WEB-INF/views/modules/order/payorder/includeField.jsp" %>
                        <input type="hidden" name="payType" value="1">
                        <input type="hidden" name="token" value="${token}">
                        <input type="hidden" name="relationInvoiceIds" value="">
                        <table width="100%" cellpadding="5" cellspacing="0" border="0">
                            <c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
                                <c:forEach items="${curlist}" var="cur">
                                    <c:if test="${cur.id==currencyId}">
                                        <tr>
                                            <td class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
                                            <td>
                                                <input type="text" maxlength="13"
                                                       onblur="valdate('${quankuanValidate}',this,'${paramCurrencyPrice[status.index]}','${cur.currencyName}');total(this)"
                                                       value="${paramCurrencyPrice[status.index]}"
                                                       name="dqzfprice"/>
                                            </td>
                                            <input type="hidden" value="${cur.id}" name="currencyIdPrice"/>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                            <tbody>
                            <c:if test="${differenceFlag == 1 and orderType eq '2' }">
	                         	<tr>
	                         		<td class="trtextaling">门店结算价差额返还：${differenceCurrencyMark }</td>
	                              	<td><input type="text" name="returnPrice" value="0.00" onblur="total(this)" onkeyup="invoiceInputin(this)" maxlength="13"></td>
	                              	<input type="hidden" value="${differenceCurrencyId }" name="differenceCurrencyId" />
	                         	</tr>
	                         	<tr>
	                         		<td class="trtextaling">总计：</td>
	                         		<td class="all_sum"></td>
	                         	</tr>
	                         </c:if>	
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
                                <%-- <td class="trtextalingi"><input type="text" maxlength="50" name="payerName" onblur="checkrequired(this)" value="${payerName}" id="payerNameID_1"/><span style="color:#F1842F;margin-left:8px;" name="spancheck11"></span></td> --%>
                                <td class="trtextalingi"><input type="text" maxlength="50" name="payerName"
                                                                class="required" value="${payerName}"
                                                                id="payerNameID_1"/></td>
                            </tr>
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>支票号：</td>
                                <td class="trtextalingi"><input class="check_char_or_num required" maxlength="10"
                                                                type="text" name="checkNumber" id="checkNumber"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>开票日期：</td>
                                <td class="trtextalingi"><input type="text" name="invoiceDate" class="required"
                                                                readonly id="invoiceDate"/></td>
                            </tr>
                            <tr>
                                <td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>支票图片：</td>
                                <td class="trtextalingi payforFiles">
                                    <input type="button" name="DocInfoIds" class="btn btn-primary" value="上传"
                                           onclick="uploadFiles('${ctx}',null,this);"/>
                                    <ol class="batch-ol">
                                    </ol>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling" style="">备注信息：</td>
                                <td class="trtextalingi" style="vertical-align:top">
                                        <textarea maxlength="120" name="remarks"
                                                  style="width:500px; resize:none;"></textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div id="offlinebox_2" style="display:none;" class="payDiv">
                    <form id="offlineform_2" method="post" action="${ctx}/orderPay/savePay"
                          style="margin:0px; padding:0px;">
                        <span style="color:#F00">* 为确保您的订单能够及时处理，请付款后，正确填写POS单号。</span>
                        <%@ include file="/WEB-INF/views/modules/order/payorder/includeField.jsp" %>
                        <input type="hidden" name="payType" value="2">
                        <input type="hidden" name="token" value="${token}">
                        <table width="100%" cellpadding="5" cellspacing="0" border="0">
                            <tbody>
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>POS单号：</td>
                                <td class="trtextalingi"><input type="text" name="posNo" maxlength="20"
                                                                class="required"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>POS机终端号：</td>
                                <td class="trtextalingi"><input type="text" name="posTagEend" maxlength="20"
                                                                class="required"/></td>
                            </tr>
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>POS机所属银行：</td>
                                <td class="trtextalingi"><input type="text" name="posBank" maxlength="20"
                                                                class="required"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling" style="">备注信息：</td>
                                <td class="trtextalingi" style="vertical-align:top">
                                    <textarea name="remarks" maxlength="120"
                                              style="width:500px;  resize:none;"></textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <!-- 现金支付 -->
                <div id="offlinebox_3" style="display:none;" class="payDiv">
                    <form id="offlineform_3" method="post" action="${ctx}/orderPay/savePay"
                          style="margin:0px; padding:0px;" class = "formTotal">
                        <span style="color:#F00">* 为确保您的订单能够及时处理，请到财务开具证明。</span>
                        <c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
                            <c:if test="${hasPreOpeninvoice }">
                            <span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);"
                                                                onclick="relationInvoiceList()" target="_self">关联发票</a></span>
                            </c:if>
                        </c:if>
                            <%@ include file="/WEB-INF/views/modules/order/payorder/includeField.jsp" %>
                            <input type="hidden" name="payType" value="3">
                            <input type="hidden" name="token" value="${token}">
                            <input type="hidden" name="relationInvoiceIds" value="">
                            <table width="100%" cellpadding="5" cellspacing="0" border="0">
                                <tbody>
                                <c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
                                    <c:forEach items="${curlist}" var="cur">
                                        <c:if test="${cur.id==currencyId}">
                                            <tr>
                                                <td class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
                                                <td><input type="text" maxlength="13"
                                                           onblur="valdate('${quankuanValidate}',this,'${paramCurrencyPrice[status.index]}','${cur.currencyName}');total(this)"
                                                           value="${paramCurrencyPrice[status.index]}"
                                                           name="dqzfprice" />
                                                </td>
                                                <input type="hidden" value="${cur.id}" name="currencyIdPrice"/>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                                <c:if test="${differenceFlag == 1 and orderType eq '2' }">
	                                <tr>
		                         		<td class="trtextaling">门店结算价差额返还：${differenceCurrencyMark }</td>
		                              	<td><input type="text" name="returnPrice" value="0.00" onblur="total(this)" onkeyup="invoiceInputin(this)" maxlength="13"></td>
		                              	<input type="hidden" value="${differenceCurrencyId }" name="differenceCurrencyId" />
		                         	</tr>
		                         	<tr>
		                         		<td class="trtextaling">总计：</td>
		                         		<td class="all_sum"></td>
		                         	</tr>
		                         </c:if>	
                                <tr>
                                    <td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
                                    <td class="trtextalingi"><input type="text" maxlength="50" name="payerName"
                                                                    class="required" value="${payerName}"
                                                                    id="payerNameID_3"/></td>
                                </tr>
                                <tr class="trVoucher_3">
                                    <td class="trtextaling payforFiles-t">收款凭证：</td>
                                    <td class="payforFiles">
                                        <input name="passportfile" type="text" style="display:none;"
                                               disabled="disabled">
                                        <input type="button" name="DocInfoIds" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}',null,this);"/>
                                        <ol class="batch-ol">
                                        </ol>
                                        <label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="trtextaling" style="">备注信息：</td>
                                    <td class="trtextalingi" style="vertical-align:top">
                                    <textarea name="remarks" maxlength="120"
                                              style="width:500px;resize:none;"></textarea>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                    </form>
                </div>
                <!-- 汇款 -->
                <div id="offlinebox_4" style="display:none;" class="payDiv">
                    <form id="offlineform_4" method="post" action="${ctx}/orderPay/savePay"
                          style="margin:0px; padding:0px;" class = "formTotal">
                        <span style="color:#F00">* 为确保您的订单能够及时处理，请到财务开具证明。</span>
                        <c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
                            <c:if test="${hasPreOpeninvoice }">
                        <span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);"
                                                            onclick="relationInvoiceList()"
                                                            target="_self">关联发票</a></span>
                            </c:if>
                        </c:if>
                            <%@ include file="/WEB-INF/views/modules/order/payorder/includeField.jsp" %>
                            <input type="hidden" name="payType" value="4">
                            <input type="hidden" name="token" value="${token}">
                            <input type="hidden" name="agentId" value="${agentId}">
                            <input type="hidden" name="realBankName" value="">
                            <input type="hidden" name="realBankAccount" value="">
                            <input type="hidden" name="relationInvoiceIds" value="">

                            <table width="100%" cellpadding="5" cellspacing="0" border="0">
                                <c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
                                    <c:forEach items="${curlist}" var="cur">
                                        <c:if test="${cur.id==currencyId}">
                                            <tr>
                                                <td class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
                                                <td><input type="text"
                                                           onblur="valdate('${quankuanValidate}',this,'${paramCurrencyPrice[status.index]}','${cur.currencyName}');total(this)"
                                                           maxlength="13" value="${paramCurrencyPrice[status.index]}"
                                                           name="dqzfprice"/></td>
                                                <input type="hidden" value="${cur.id}" name="currencyIdPrice"/>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                                <c:if test="${differenceFlag == 1 and orderType eq '2' }">
	                                <tr>
		                         		<td class="trtextaling">门店结算价差额返还：${differenceCurrencyMark }</td>
		                              	<td><input type="text" name="returnPrice" value="0.00" onblur="total(this)" onkeyup="invoiceInputin(this)" maxlength="13"></td>
		                              	<input type="hidden" value="${differenceCurrencyId }" name="differenceCurrencyId" />
		                         	</tr>
		                         	<tr>
		                         		<td class="trtextaling">总计：</td>
		                         		<td class="all_sum"></td>
		                         	</tr>
		                         </c:if>	
                                <tr>
                                    <td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
                                    <td class="trtextalingi"><input type="text" maxlength="50" name="payerName"
                                                                    class="required"
                                                                    value="${payerName}" id="payerNameID_4"/></td>
                                </tr>

                                <tr>
                                    <td class="trtextaling">来款行名称：</td>
                                    <td class="trtextalingi sel-bank-of-name">
                                        <select id="offlinebox_4_bankName" name="bankName" agentId="${agentId}">
                                            <option value="-1">--请选择--</option>
                                            <c:forEach items="${agentBanks}" var="bankInfo">
                                                <option value="${bankInfo.bankName}">${bankInfo.bankName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="trtextaling">来款账号：</td>
                                    <td class="trtextalingi sel-bank-of-name">
                                        <select id="bankAccount" name="bankAccount">
                                            <option value="-1">--请选择--</option>
                                        </select>
                                    </td>
                                </tr>

                                <!-- 127 奢华之旅 汇款必填  王洋  2016.3.30 ${isSHZL } -->
                                <c:choose>
                                    <c:when test="${isSHZL }">
                                        <tr>
                                            <td class="trtextaling" style="padding:0px;">
                                                <span style="color:#f00;">*</span>收款行名称：
                                            </td>
                                            <td class="trtextalingi"><c:choose>
                                                <c:when test="${fn:length(supplierBanks)>0}">
                                                    <select id="toBankNname" name="toBankNname"
                                                            onchange="changeOpenBank('${supplierId}',[this.options[this.options.selectedIndex].value])"
                                                            min="0" validate="required:true">
                                                        <option value="-1">--请选择--</option>
                                                        <c:forEach items="${supplierBanks}" var="bankInfo"
                                                                   varStatus="status">
                                                            <option value="${supplierBanks[status.index]}">${supplierBanks[status.index]}</option>
                                                        </c:forEach>
                                                    </select>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="text" name="toBankNname" maxlength="20"/>
                                                </c:otherwise>
                                            </c:choose></td>
                                        </tr>
                                        <tr>
                                            <td class="trtextaling" style="padding:0px;">
                                                <span style="color:#f00;">*</span>收款账号：
                                            </td>
                                            <td class="trtextalingi"><c:choose>
                                                <c:when test="${fn:length(supplierBanks)>0}">
                                                    <select id="toBankAccount" name="toBankAccount"
                                                            onfocus="toBankAccountChange()">
                                                        <option value="-1">--请选择--</option>
                                                    </select>
                                                    <label id="errorBankAccount" class="hide"
                                                           style="color: #ea5200;margin-left: 16px;">请选择收款账号！</label>
                                                </c:when>
                                                <c:otherwise>
                                                    <input id="toBankAccount" type="text" name="toBankAccount"
                                                           maxlength="21"/>
                                                </c:otherwise>
                                            </c:choose>

                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td class="trtextaling" style="padding:0px;">
                                                <!-- <span style="color:#f00;">*</span> -->
                                                收款行名称：
                                            </td>
                                            <td class="trtextalingi"><c:choose>
                                                <c:when test="${fn:length(supplierBanks)>0}">
                                                    <select id="toBankNname" name="toBankNname"
                                                            onchange="changeOpenBank('${supplierId}',[this.options[this.options.selectedIndex].value])">
                                                        <option value="-1">--请选择--</option>
                                                        <c:forEach items="${supplierBanks}" var="bankInfo"
                                                                   varStatus="status">
                                                            <option value="${supplierBanks[status.index]}">${supplierBanks[status.index]}</option>
                                                        </c:forEach>
                                                    </select>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="text" name="toBankNname" maxlength="20"/>
                                                </c:otherwise>
                                            </c:choose></td>
                                        </tr>
                                        <tr>
                                            <td class="trtextaling" style="padding:0px;">
                                                <!-- <span style="color:#f00;">*</span> -->收款账号：
                                            </td>
                                            <td class="trtextalingi"><c:choose>
                                                <c:when test="${fn:length(supplierBanks)>0}">
                                                    <select id="toBankAccount" name="toBankAccount">
                                                        <option value="-1">--请选择--</option>
                                                    </select>
                                                </c:when>
                                                <c:otherwise>
                                                    <input id="toBankAccount" type="text" name="toBankAccount"
                                                           maxlength="21"/>
                                                </c:otherwise>
                                            </c:choose></td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>


                                <tr class="trVoucher_4">
                                    <td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>收款凭证：</td>
                                    <td class="payforFiles">
                                        <input type="button" name="DocInfoIds" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}',null,this);"/>
                                        <ol class="batch-ol">
                                        </ol>
                                        <label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="trtextaling" style="">备注信息：</td>
                                    <td class="trtextalingi" style="vertical-align:top">
                                        <textarea name="remarks" maxlength="120"
                                                  style="width:500px; resize:none;"></textarea>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                    </form>
                </div>
                <div id="offlinebox_5" style="display: none;" class="payDiv">
                    <form id="offlineform_5" method="post" action="${ctx}/orderPay/savePay"
                          style="margin:0px; padding:0px;">
                        <%@ include file="/WEB-INF/views/modules/order/payorder/includeField.jsp" %>
                        <input type="hidden" name="payType" value="5">
                        <input type="hidden" name="token" value="${token}">
                        <table width="100%" cellpadding="5" cellspacing="0" border="0">
                            <tbody>
                            <c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
                                <c:forEach items="${curlist}" var="cur">
                                    <c:if test="${cur.id==currencyId}">
                                        <tr>
                                            <td class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
                                            <td><input type="text" maxlength="13"
                                                       onblur="valdate('${quankuanValidate}',this,'${paramCurrencyPrice[status.index]}','${cur.currencyName}');"
                                                       value="${paramCurrencyPrice[status.index]}" name="dqzfprice"/>
                                            </td>
                                            <input type="hidden" value="${cur.id}" name="currencyIdPrice"/>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>收款方式：</td>
                                <td class="trtextalingi">
                                    <input type="text" name="fastPayType" maxlength="20" class="required"
                                           id="fastPayType"/>
                                </td>
                            </tr>
                            <tr class="trVoucher_5">
                                <td class="trtextaling payforFiles-t">
                                    <span style="color:#f00;">*</span>收款凭证：
                                </td>
                                <td class="payforFiles">
                                    <input type="button" name="DocInfoIds" class="btn btn-primary" value="上传"
                                           onclick="uploadFiles('${ctx}',null,this);"/>
                                    <ol class="batch-ol">
                                    </ol>
                                    <label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling" style="">备注信息：</td>
                                <td class="trtextalingi" style="vertical-align:top">
                                    <textarea name="remarks" maxlength="120"
                                              style="width:500px;resize:none;"></textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>

            <%--拉美图增加因公支付宝 --%>
            <c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
                <div id="offlinebox_9" class="payDiv" style="display: none;">
                    <span style="color:#f00;">*为确保您的订单能够及时处理，请正确填写以下信息。</span>
                    <c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
                        <c:if test="${hasPreOpeninvoice }">
                    <span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);"
                                                        onclick="relationInvoiceList()" target="_self">关联发票</a></span>
                        </c:if>
                    </c:if>
                    <form class="offlineform_9 formTotal"  id="offlineform_9" method="post" action="${ctx}/orderPay/savePay"
                          style="margin:0px; padding:0px;">
                        <%@ include file="/WEB-INF/views/modules/order/payorder/includeField.jsp" %>
                        <input type="hidden" name="payType" value="9">
                        <input type="hidden" name="token" value="${token}">
                        <table width="93%" cellpadding="5" cellspacing="0" border="0">
                            <tbody>
                            <tr>
                                <c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
                                <c:forEach items="${curlist}" var="cur">
                                <c:if test="${cur.id==currencyId}">
                            <tr>
                                <td class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
                                <td>
                                    <input type="text" maxlength="13"
                                           onblur="valdate('${quankuanValidate}',this,'${paramCurrencyPrice[status.index]}','${cur.currencyName}');total(this)"
                                           value="${paramCurrencyPrice[status.index]}" name="dqzfprice"/>
                                </td>
                                <input type="hidden" value="${cur.id}" name="currencyIdPrice"/>
                            </tr>
                            </c:if>
                            </c:forEach>
                            </c:forEach>
                            </tr>
                              <c:if test="${differenceFlag == 1 and orderType eq '2' }">
	                                <tr>
		                         		<td class="trtextaling">门店结算价差额返还：${differenceCurrencyMark }</td>
		                              	<td><input type="text" name="returnPrice" value="0.00" onblur="total(this)" onkeyup="invoiceInputin(this)" maxlength="13"></td>
		                              	<input type="hidden" value="${differenceCurrencyId }" name="differenceCurrencyId" />
		                         	</tr>
		                         	<tr>
		                         		<td class="trtextaling">总计：</td>
		                         		<td class="all_sum"></td>
		                         	</tr>
		                        </c:if>	
                            <!--S 20160113 127-->
                            <tr>
                                <td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
                                <td class="trtextalingi" style="width:490px;">
                                    <input type="text" maxlength="50" name="payerName" class="required"
                                           id="payerNameID_9"
                                           value="${payerName}">

                                </td>

                            </tr>
                            <tr>
                                <td class="trtextaling">支付宝名称（来款）：</td>
                                <td class="trtextalingi sel-bank-of-name">
                                    <input type="text" maxlength="50" name="fromAlipayName">


                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling">支付宝账号（来款）：</td>
                                <td class="trtextalingi sel-bank-of-name">
                                    <input type="text" maxlength="50" name="fromAlipayAccount">


                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling">收款单位：</td>
                                <td class="trtextalingi sel-bank-of-name">
                                    <input type="text" maxlength="50" name="comeOfficeName" value="${comOffice }">


                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling">支付宝名称（收款）：</td>
                                <td class="trtextalingi sel-bank-of-name">
                                    <c:if test="${empty alipay }">
                                        <input type="text" maxlength="50" name="toAlipayName1">
                                    </c:if>
                                    <c:if test="${! empty alipay }">
                                        <select name="toAlipayName" onchange="getAlipayAccount()">
                                            <option value="">请选择</option>
                                            <c:forEach items="${alipay }" var="p">
                                                <option value="${p.name }"
                                                        <c:if test="${p.defaultFlag==0 }">selected</c:if>>${p.name }</option>
                                            </c:forEach>
                                        </select>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling">支付宝账号（收款）：</td>
                                <td class="trtextalingi sel-bank-of-name">
                                    <c:if test="${empty alipay }">
                                        <input type="text" maxlength="50" name="toAlipayAccount1">
                                    </c:if>
                                    <c:if test="${! empty alipay }">
                                        <select name="toAlipayAccount">
                                            <c:if test="${! empty account }">
                                                <c:forEach items="${account }" var="p">
                                                    <option value="${p.account }"
                                                            <c:if test="${p.defaultFlag==0 }">selected</c:if>>${p.account }</option>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${empty account }">
                                                <option value="">请选择</option>
                                            </c:if>
                                        </select>
                                    </c:if>
                                </td>
                            </tr>
                            <tr class="trVoucher_4">
                                <td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>收款凭证：</td>
                                <td class="payforFiles">
                                    <input type="button" name="DocInfoIds" class="btn btn-primary" value="上传"
                                           onclick="uploadFiles('${ctx}',null,this);"/>
                                    <ol class="batch-ol">
                                    </ol>
                                    <label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="trtextaling" style="">备注信息：</td>
                                <td class="trtextalingi" style="vertical-align:top">
                                    <textarea name="remarks" maxlength="200"
                                              style="width:200px; resize:none;"></textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
            </c:if>
        </div>
    </div>
</div>

<div style="overflow:hidden">
    <div class="kongr"></div>
</div>
<div class="ydbz_sxb ydbz_button">
    <c:if test="${not empty orderDetailUrl}">
        <a target="_blank" class="ydbz_x" href="${orderDetailUrl}">查看订单</a>
    </c:if>
    <a id="payBtn" class="ydbz_x" onclick="formSunbmit()">确认收款</a>
    <c:choose>
        <c:when test="${orderType==1 or orderType==2 or orderType==3 or orderType==4 or orderType==5 or orderType==10}">
            <a id="nopayBtn" class="ydbz_x"
               href="${ctx}/orderList/manage/showOrderList/0/${orderType}?orderNumOrGroupCode=${orderNum}">暂不收款</a>
        </c:when>
        <c:when test="${orderType==7}">
            <a id="nopayBtn" class="ydbz_x"
               href="${ctx}/airticketOrderList/manage/airticketOrderList/1?orderNumOrOrderGroupCode=${orderNum}">暂不收款</a>
        </c:when>
        <c:otherwise></c:otherwise>
    </c:choose>
</div>
</div>
<div id="relationInvoice" class="display-none">
    <div class="select_account_pop" style="padding:20px">
        <div>
            <table class="activitylist_bodyer_table">
                <thead>
                <tr>
                    <th>选择</th>
                    <th>发票号</th>
                    <th>团号</th>
                    <th>申请人</th>
                    <th>开票状态</th>
                    <th>开票金额</th>
                </tr>
                </thead>
                <tbody id="relationInvoiceTable">
                <tr>
                    <td name='logId' class='tc' value=''><input id="" type="checkbox" value=""></td>
                    <td name='operation' class='tc'><span></span></td>
                    <td name='operation' class='tc'><span></span></td>
                    <td name='operationTime' class='tc'><span></span></td>
                    <td name='mainContext' class='tl'><span></span></td>
                    <td name='mainContext' class='tl'><span></span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    $().ready(function () {
        $(".offlineform_1").validate({
            onsubmit: false
        });
        $("#offlineform_2").validate({
            onsubmit: false
        });
        $(".offlineform_3").validate({
            onsubmit: false
        });
        $(".offlineform_4").validate({

            onsubmit: false
        });


        $("#offlineform_5").validate({
            onsubmit: false
        });//224 支付宝

        jQuery.extend(jQuery.validator.messages, {
            required: "必填信息",
            number: "输入信息有误",
            digits: "输入信息有误",
            email: "email格式错误",
            min: jQuery.validator.format("请选择收款行！"),
            max: jQuery.validator.format("请选择收款账户！")

        });
    });
</script>
</body>
</html>