var rebates = {
    init: function () {
        initRebates("employee");
    },
    showDetail: function (rebatesId) {
        window.open(contextPath + "/rebatesNew/rebatesInfo/" + rebatesId);
    },
    addRebates: function (orderId, orderType) {
        rebates.ajax.getRebatesStatusAjax(orderId, orderType);
    },
    saveRebates: function () {
        var rebatesList = [];
        var orderId = $("#orderId").val();
        var orderType = $("#orderType").val();
        var validFlag = true;
        /**
         * 获取返佣对象信息
         */
        var rebatesObjectType = "1";//返佣对象类型：渠道、供应商,默认为渠道
        var rebatesObjectId = "";//返佣对象id
        var rebatesObjectName = "";//返佣对象名称
        var rebatesObjectAccountId = "";//返佣对象账号id
        var rebatesObjectAccountType = "";//返佣对象账号类型：境内、境外
        var rebatesObjectAccountBank = "";//返佣对象账号开户行名称
        var rebatesObjectAccountCode = "";//返佣对象账号号码

        /**
         * 获取返佣对象信息，并且进行校验
         */
        if ($("#rebateObjectTitle").length > 0) {//需要选择返佣类型
            //判断是否选择返佣类型
            if ($(".rebateTarget").find('option:selected').val() == '0') {
                if ($("#inputId").val() == '049984365af44db592d1cd529f3008c3') {
                    jBox.tip("请选择宣传费对象信息！");
                } else {
                    jBox.tip("请选择返佣对象信息！");
                }
                return;
            }

            //如果是返佣类型是供应商
            if ($(".rebateTarget").find('option:selected').val() == '2') {
                rebatesObjectType = "2";//设置返佣对象类型值

                //判断是否选择供应商
                if ($("#rebateObject").find('option:selected').val() == '0') {
                    if ($("#inputId").val() == '049984365af44db592d1cd529f3008c3') {
                        jBox.tip("请选择宣传费供应商！");
                    } else {
                        jBox.tip("请选择返佣供应商！");
                    }
                    return;
                } else {
                    //设置返佣对象信息
                    rebatesObjectId = $("#rebateObject").find('option:selected').val();
                    rebatesObjectName = $("#rebateObject").find('option:selected').text();

                    //判断是否选择账户类型（境内境外）
                    if ($("#accountType").find('option:selected').val() == '0') {
                        jBox.tip("请选择账户类型！");
                        return;
                    } else {
                        //设置返佣对象账户类型
                        rebatesObjectAccountType = $("#accountType").find('option:selected').val();

                        //判断是否选择了开户行名称
                        $option = $("select[name='bankName']:visible").find('option:selected');
                        if ($option.val() == '0') {
                            jBox.tip("请选择开户行！");
                            return;
                        } else {
                            rebatesObjectAccountId = $option.val();//账户id
                            rebatesObjectAccountBank = $option.text();//账户开户行
                            rebatesObjectAccountCode = $option.attr("bankAccountCode");
                            if (rebatesObjectAccountCode == undefined || rebatesObjectAccountCode == '') {
                                jBox.tip("请确认当前开户行账号号码是否存在！");
                                return;
                            }
                        }
                    }
                }
            }
        }


        $("tr[group^='travler']").each(function (index, element) {
            var costname = $(element).find("input[name='costname']").val();
            var rebatesDiff = $(element).find(".huanjia input").val();
            if (rebatesDiff != "" && isFloat(rebatesDiff) > 0 && costname == "") {
                if ($("#inputId").val() == '049984365af44db592d1cd529f3008c3') {
                    jBox.tip("个人宣传费差额大于0，请输入款项！");
                } else {
                    jBox.tip("个人返佣差额大于0，请输入款项！");
                }
                validFlag = false;
                return;
            } else if (rebatesDiff != "" && isFloat(rebatesDiff) > 0 && costname != "") {
                var travelerId = $(element).find("input[name='travelerId']").val();
                var currencyId = $(element).find("select[name='gaijiaCurency'] option:selected").val();
                var oldRebates = $(element).find("input[name='oldRebates']").val();
                var remark = $(element).find("input[name='remark']").val();
                var newRebates = [];
                $(element).find("[name='gaijiaCurencyNew']").each(function (index, element) {
                    var currencyId = $(element).attr("currencyId");
                    var amount = isFloat($(element).attr("data").replace(/[ ]/g, "").replace(/,/g, ""));
                    newRebates.push(rebates._getMoneyAmount(currencyId, amount));
                });
                rebatesList.push(rebates._getRebates(orderId, travelerId, currencyId, isFloat(rebatesDiff), oldRebates, newRebates, costname, remark, orderType, rebatesObjectType, rebatesObjectId, rebatesObjectName, rebatesObjectAccountId, rebatesObjectAccountType, rebatesObjectAccountBank, rebatesObjectAccountCode));
            }
        });
        $(".gai-price-ol li").each(function (index, element) {
            var rebatesDiff = $(element).find("input[name='teamMoney']").val();
            var costname = $(element).find("input[name='costname']").val();
            if (rebatesDiff != "" && isFloat(rebatesDiff) > 0 && costname == "") {
                if ($("#inputId").val() == '049984365af44db592d1cd529f3008c3') {
                    jBox.tip("团队宣传费差额大于0，请输入款项！");
                } else {
                    jBox.tip("团队返佣差额大于0，请输入款项！");
                }
                validFlag = false;
                return;
            } else if (rebatesDiff != "" && isFloat(rebatesDiff) > 0 && costname != "") {
                var currencyId = $(element).find("select[name='teamCurrency'] option:selected").val();
                var remark = $(element).find("input[name='remark']").val();
                var newRebates = [];
                newRebates.push(rebates._getMoneyAmount(currencyId, isFloat(rebatesDiff)));
                rebatesList.push(rebates._getRebates(orderId, "", currencyId, rebatesDiff, "", newRebates, costname, remark, orderType, rebatesObjectType, rebatesObjectId, rebatesObjectName, rebatesObjectAccountId, rebatesObjectAccountType, rebatesObjectAccountBank, rebatesObjectAccountCode));
            }
            ;
        });
        if (validFlag && rebatesList.length > 0) {
            $("input[name='apply']").attr("disabled", "disabled");
            rebates.ajax.saveRebatesAjax(rebatesList, orderId, orderType);
        } else {
            if (validFlag) {
                if ($("#inputId").val() == '049984365af44db592d1cd529f3008c3') {
                    jBox.tip("请输入宣传费信息！");
                } else {
                    jBox.tip("请输入返佣信息！");
                }
            }
        }
    },
    cancleRebates: function (rid) {
        $.jBox.confirm("确定要取消申请吗？", "提示", function (v, h, f) {
            if (v == 'ok') {
                if (rid != null) {
                    rebates.ajax.cancleRebatesAjax(rid);
                } else {
                    window.close();
                }
            }
        });
    },
    _getRebates: function (orderId, travelerId, currencyId, rebatesDiff, oldRebates, newRebates, costname, remark, orderType, rebatesObjectType, rebatesObjectId, rebatesObjectName, rebatesObjectAccountId, rebatesObjectAccountType, rebatesObjectAccountBank, rebatesObjectAccountCode) {
        var rebates = new Object();
        rebates.orderId = orderId;
        rebates.travelerId = travelerId;
        rebates.currencyId = currencyId;
        rebates.rebatesDiff = rebatesDiff;
        rebates.oldRebates = oldRebates;
        rebates.newRebates = newRebates;
        rebates.costname = costname;
        rebates.remark = remark;
        rebates.orderType = orderType;
        //增加返佣对象信息
        rebates.rebatesObjectType = rebatesObjectType;//返佣对象类型
        rebates.rebatesObjectId = rebatesObjectId;//返佣对象id
        rebates.rebatesObjectName = rebatesObjectName;//返佣对象名称
        rebates.rebatesObjectAccountId = rebatesObjectAccountId;//返佣对象账号
        rebates.rebatesObjectAccountType = rebatesObjectAccountType;//
        rebates.rebatesObjectAccountBank = rebatesObjectAccountBank;//
        rebates.rebatesObjectAccountCode = rebatesObjectAccountCode;//
        return rebates;
    },
    _getMoneyAmount: function (currencyId, amount) {
        var moneyAmount = new Object();
        moneyAmount.currencyId = currencyId;
        moneyAmount.amount = amount;
        return moneyAmount;
    },
    ajax: {
        saveRebatesAjax: function (rebatesList, orderId, orderType) {
            $.ajax({
                type: "POST",
                url: contextPath + "/rebatesNew/applyRebate",
                data: {
                    rebatesList: JSON.stringify(rebatesList)
                },
                success: function (msg) {
                    if (msg == "success") {
                        top.$.jBox.tip("保存成功", "success");
                        window.location.href = contextPath + "/rebatesNew/list?orderId=" + orderId + "&orderType=" + orderType;
                    } else {
                        $("input[name='apply']").attr("disabled", false);
                        top.$.jBox.tip(msg, "warning");
                    }
                }
            });
        },
        cancleRebatesAjax: function (rid) {
            $.ajax({
                type: "POST",
                url: contextPath + "/rebatesNew/cancelRebates",
                data: {
                    rid: rid
                },
                success: function (msg) {
                    if (msg == "success") {
                        top.$.jBox.tip("保存成功", "success");
                        location.reload();
                    } else {
                        top.$.jBox.tip("保存失败", "warning");
                    }
                }
            });
        },
        getRebatesStatusAjax: function (orderId, orderType) {
            $.ajax({
                type: "POST",
                async: false,
                url: contextPath + "/order/rebates/validRebates",
                data: {
                    orderId: orderId,
                    orderType: orderType
                },
                success: function (msg) {
                    if (msg == "true") {
                        window.open(contextPath + "/order/rebates/addRebates/" + orderId + "/" + orderType);
                    } else {
                        jBox.tip("上次返佣申请流程未结束，不能再次申请！");
                    }
                }
            });
        }
    }
};
//定义分币种金额对象
function CurrencyMoney(ObjCurrency) {
    var obj = {};
    ObjCurrency.each(function (index, element) {
        var currencyNameShow = $(element).text().replace(/[ ]/g, "");
        obj[currencyNameShow] = 0.00;
    });
    return obj;
}
//验证是否是数值
function isFloat(str) {
    return parseFloat(str) ? parseFloat(str) : 0.00;
}

function refundInput(obj) {
    var ms = obj.value.replace(/[^\d\.]/g, "").replace(/(\.\d{2}).+$/, "$1").replace(/^0+([1-9])/, "$1").replace(/^0+$/, "0");
    // 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
    if (ms != obj.value) {
        var txt = ms.split(".");
        obj.value = txt[0] + (txt.length > 1 ? "." + txt[1] : "");
    }
}
function initRebates(productType) {
    $("input[name='apply']").click(function () {
        rebates.saveRebates();
    });
    //改价产品类型
    var theProductType = productType ? productType : "";
    var $optionCurrency = $("#currencyTemplate option");
    //总价--当前金额=游客改价中的当前应收列相加
    var oldSum = new CurrencyMoney($optionCurrency);
    $("span[name^='gaijiaCurencyOld']").each(function (index, element) {
        var currencyName = $(element).text().replace(/[ ]/g, "");
        oldSum[currencyName] += isFloat($(element).attr("data").replace(/[ ]/g, "").replace(/,/g, ""));
    });
    //总价--当前金额输出
    var html_oldSum = '';
    $("#currencyTemplate option").each(function (index, element) {
        var currencyNameShow = $(element).text();
        html_oldSum = sumToStr(html_oldSum, currencyNameShow, oldSum[currencyNameShow]);
    });
    if (html_oldSum == '') {
        html_oldSum = '<font class="f14" flag="bz" value="0">人民币</font><span class="f20">0</span>';
    }
    $("#totalBefore").html(html_oldSum);

    //总价--差额、改后初始化
    calculateSum(1);

    //增加团队改价
    $('.gai-price-btn').click(function () {
        var html = '<li><i><input type="text" name="costname" maxlength="50" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
        html += '<i><select name="teamCurrency">{0}<select></i>&nbsp;'.replace("{0}", $("#currencyTemplate").html());
        html += '<i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" maxlength="8" onkeyup="refundInput(this)" onafterpast="refundInput(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
        html += '<i><input type="text" name="remark" maxlength="50" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
        //<%--UG_V2 按钮调整 20170315 by tlw--%>
        // html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
        html += '<i><input class="btn ydbz_x clear-btn" type="button" value="删除"></i>';
        $(this).parents('.gai-price-ol').append(html);
        $(this).parents('.gai-price-ol').find("li:last").find("select[name='teamCurrency'] option:first").prop("selected", true);
        //新增改价项目中，input获得失去焦点提示信息显示隐藏
        inputTipsDynamic($(this).parents('.gai-price-ol').find("li:last"));
    });

    //团队改价操作
    $('.gai-price-ol').on("click", ".clear-btn", function () {//删除团队改价一项
        $(this).parents('li').remove();
        calculateSum(0);
    }).on("focusout", "input[name='teamMoney']", function () {//修改款项金额
        //验证是否是数字
        validNumFinally(this);
        $(this).next(".ipt-tips2").hide();
        newNumTrTotal();
        calculateSum(1);
    }).on("change", "select[name='teamCurrency']", function () {//修改款项币种
        newNumTrTotal();
        calculateSum(1);
    });

    //操作显示隐藏
    $('.modifyPrice-table').on("mouseover", ".huanjia", function () {
        //显示操作按钮
        $(this).addClass("huanjia-hover").find('dt input').attr('defaultValue');
        $(this).find('dd').show();
    }).on("mouseout", ".huanjia", function () {
        //隐藏操作按钮
        $(this).removeClass("huanjia-hover").find('dd').hide();
    }).on("change", "select[name='gaijiaCurency']", function () {
        //重新计算改后金额
        newNumTrTotal();
        //重新计算总价
        calculateSum(1);
    }).on("focusout", "input[name=plusys],input[name=plusdj]", function () {
        //验证是否是数字
        validNumFinally(this);
        //重新计算改后金额
        newNumTrTotal();
        //重新计算总价
        if (($(this).attr("name") == "plusys") || ("visa" == theProductType)) {
            calculateSum(1);
        }
    }).on("click", "[flag=appAll]", function () {
        //对应的input框的name值
        var inputName = $(this).parents("td").find("input").attr("name");
        //应用全部
        var inputValue = $(this).parents("td").find("input[name='" + inputName + "']").val();
        var $thisCurrency = $(this).parents("tr").find("[name='gaijiaCurency']");
        var thisCurrencyName = $thisCurrency[0].tagName == "SPAN" ? $thisCurrency.text() : $thisCurrency.find("option:selected").text();
        var groupName = $(this).parents("tr").attr("group");
        //获取游客的姓名数组
        var array_groupName = new Array();
        $(".modifyPrice-table tr[group]").each(function (index, element) {
            if ($.inArray($(element).attr("group"), array_groupName) < 0) {
                array_groupName.push($(element).attr("group"));
            } else {
                return;
            }
        });
        $.each(array_groupName, function (index, element) {
            if (element != groupName) {
                var $thisTr = $(".modifyPrice-table tr[group='" + element + "']");
                $thisTr.each(function () {
                    $(this).find("input[name='" + inputName + "']").val(inputValue).trigger("focusout");
                    return;
                })
            }
        });
    }).on("click", "[flag=reset]", function () {
        //还原
        //UG_V2 统一按钮为input 所以清空时添加限定条件 只清空dt中的input 20170315 tlw
        var $inputLink = $(this).parents(".huanjia").find("dt input");
        $inputLink.val($inputLink.attr("defaultvalue")).trigger("focusout");
    });

    //全部还原
    $(".re-storeall").click(function () {
        $(".huanjia dt input").each(function (index, element) {
            $(element).val($(element).attr("defaultvalue")).trigger("focusout");
        });
    });


    //总价html拼接
    function sumToStr(str, currencyName, thenumber) {
        var html = str;
        if (thenumber && thenumber != 0) {
            var theMoney = milliFormat(thenumber, 1);
            var sign = ' <span class="tdgreen">+</span> ';
            var isNegative = /^\-/.test(theMoney);
            if (isNegative) {
                sign = ' <span class="tdred fbold">-</span> ';
                theMoney = theMoney.replace(/^\-/g, '');
            }
            if (html != '' || (html == '' && isNegative)) {
                html += sign;
            }
            html += '<font class="f14">' + currencyName + '</font><span class="f20">' + theMoney + '</span>';
        }
        return html;
    }


    //游客改价差额计算
    function calculateSum(calculateType) {
        /*calculateType计算类型说明 0:只计算团队改价；1:计算游客改价和团队改价*/
        var thiscalculateType = ("0" == calculateType) ? calculateType : 1;
        //游客改价差额
        var plusSum_traveler = new CurrencyMoney($optionCurrency);
        //团队改价差额
        var plusSum_team = new CurrencyMoney($optionCurrency);
        //团队签证改价差额
        var plusSum_visa = new CurrencyMoney($optionCurrency);
        //改价差额=游客改价差额 + 团队改价差额( + 团队签证改价--签证)
        var plusSum = new CurrencyMoney($optionCurrency);
        //改后总额
        var newSum = new CurrencyMoney($optionCurrency);
        //游客改价金额统计的html
        var sumHTML_traveler = '';
        //改价差额html
        var html_plusSum = '';
        //改后总额html
        var html_newSum = '';
        //计算团队改价
        $(".gai-price-ol li").each(function (index, element) {
            var currencyName = $(element).find("select[name='teamCurrency'] option:selected").text();
            plusSum_team[currencyName] += isFloat($(element).find("input[name='teamMoney']").val());
        });
        //通过html取得游客改价差额
        if ("0" == thiscalculateType) {
            $("#totalTravelerPlus [flag='bz']").each(function (index, element) {
                var money = isFloat($(element).next().text().replace(/[ ]/g, "").replace(/,/g, ""));
                var currencyName = $(element).text();
                plusSum_traveler[currencyName] = money;
            });
        }
        //计算游客改价
        if ("1" == thiscalculateType) {
            $("tr[group^='travler']").each(function (index, element) {
                var currencyName = $(element).find("span[name='gaijiaCurency']").length ? $(element).find("span[name='gaijiaCurency']").text() : $(element).find("select[name='gaijiaCurency'] option:selected").text();
                plusSum_traveler[currencyName] += isFloat($(element).find("[name='plusys']").val());
            });
        }

        //计算总额
        for (var p in newSum) {
            //游客改价差额计算
            if ("1" == thiscalculateType) {
                if (0 != plusSum_traveler[p]) {
                    var theMoney = milliFormat(plusSum_traveler[p], 1);
                    var sign = ' <span class="tdgreen">+</span> ';
                    var isNegative = /^\-/.test(theMoney);
                    if (isNegative) {
                        sign = ' <span class="tdred fbold">-</span> ';
                        theMoney = theMoney.replace(/^\-/g, '');
                    }
                    if (sumHTML_traveler != '' || (sumHTML_traveler == '' && isNegative)) {
                        sumHTML_traveler += sign;
                    }
                    sumHTML_traveler += '<font class="gray14" flag="bz">' + p + '</font><span class="f20 tdred">' + theMoney + '</span>';
                }
            }
            //改价差额计算=游客改价差额 + 团队改价差额
            plusSum[p] = plusSum_traveler[p] + plusSum_team[p];

            //改后总额计算
            newSum[p] = plusSum[p] + oldSum[p];
            //改价差额html
            html_plusSum = sumToStr(html_plusSum, p, plusSum[p]);
            //改后总额html
            html_newSum = sumToStr(html_newSum, p, newSum[p]);


        }

        //游客改价差额输出
        if ("1" == thiscalculateType) {
            $("#totalTravelerPlus").html(sumHTML_traveler);
        }
        //改价差额输出
        $("#totalPlus").html(html_plusSum);
        //改后总额输出
        $("#totalAfter").html(html_newSum);
    }

    //每行游客返佣价
    function newNumTrTotal() {
        $("tr[group^='travler']").each(function (index, element) {
            var thisInput = $(element).find(".huanjia input");
            var newSumName = thisInput.attr("name").replace("plus", "after");
            var oldSumName = thisInput.attr("name").replace("plus", "before");
            var oldMoney = new CurrencyMoney($optionCurrency);
            var $olds = $(element).find("[name='gaijiaCurencyOld']").each(function (index, element) {
                oldMoney[$(element).text()] = isFloat($(element).attr("data").replace(/[ ]/g, "").replace(/,/g, ""));
                ;
            });
            var moneyName = $(element).find("select[name='gaijiaCurency'] option:selected").text();
            var rebatesDiff = thisInput.val();
            if (rebatesDiff == null || rebatesDiff == "") {
                rebatesDiff = 0;
            }
            oldMoney[moneyName] += parseFloat(rebatesDiff);
            var newStr = "";
            for (var p in oldMoney) {
                if (oldMoney[p] != 0) {
                    var theMoney = milliFormat(oldMoney[p], 1);
                    var sign = '';
                    var isNegative = /^\-/.test(theMoney);
                    if (isNegative) {
                        sign = '-';
                        theMoney = theMoney.replace(/^\-/g, '');
                    }
                    if (newStr != '' || (newStr == '' && isNegative)) {
                        newStr += sign;
                    }
                    var currencyId = "";
                    $("#currencyTemplate option").each(function (index, element) {
                        var currencyNameShow = $(element).text();
                        if (p == currencyNameShow) {
                            currencyId = $(element).val();
                        }
                    });
                    newStr += '<span name="gaijiaCurencyNew" data="' + sign + theMoney + '" currencyId="' + currencyId + '">' + p + '</span>' + theMoney + '<br/>';

                }
            }
            $(element).find("[flag='" + newSumName + "']").html(newStr);
        });
    }
}