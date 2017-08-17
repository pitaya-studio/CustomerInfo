/**
 * Created by xcihao sun on 2016/01/15.
 */

//选中项的团号写入隐藏域中

//$(document).on('change','[name="ids"]',function(){
//    if($(this).get(0).checked){
//        $('.first-part ul').append('<li><input type="text" value="'+$(this).parents('tr:first').find('[name="groupCode"]').val()+'"></li>')
//    }else{
//        var groupNo = $(this).parents('tr:first').find('[name="groupCode"]').val();
//        $('.first-part ul').find('input').each(function(){
//            var $this = $(this);
//            if($this.val()==groupNo){
//                $this.parent().remove();
//            }
//        });
//    }
//})


//设置优惠额度

function jbox__discount_setting_pop_fab() {
    var $checks = $('[name="ids"]:checked');
    if ($checks.length < 1) {
        $.jBox.tip('请选择团期!');
        return;
    }

    var settlementAdultPriceFlag="";
    var settlementcChildPriceFlag="";
    var settlementSpecialPriceFlag="";
    var returnFlag = true;
    $('[name="ids"]:checked').each(function (index,obj) {
        //判断是否所选币种一致
        if(index == 0)
        {
            settlementAdultPriceFlag=$(obj).parent().parent().find("input[name=settlementAdultPrice]").parent().find("span").html();
            settlementcChildPriceFlag=$(obj).parent().parent().find("input[name=settlementcChildPrice]").parent().find("span").html();
            settlementSpecialPriceFlag=$(obj).parent().parent().find("input[name=settlementSpecialPrice]").parent().find("span").html();
        }
        else
        {
            if(
            settlementAdultPriceFlag  !=$(obj).parent().parent().find("input[name=settlementAdultPrice]").parent().find("span").html() ||
            settlementcChildPriceFlag !=$(obj).parent().parent().find("input[name=settlementcChildPrice]").parent().find("span").html()||
            settlementSpecialPriceFlag !=$(obj).parent().parent().find("input[name=settlementSpecialPrice]").parent().find("span").html())
            {
                returnFlag = false;
                return false;
            }
        }
    });

if(returnFlag == false)
{
    $.jBox.tip("币种类型不统一!");
    return;
}




    $('[name="ids"]:checked').each(function (index,obj) {
        $("#discount-setting-pop").find('.first-part ul').append('<li><input type="text" value="' + $(this).parents("tr").find("[name=groupCode]").val() + '"></li>');
        ////拼接表体
        if(index ==0)
        {
            var tid= $(this).parents("tr").find("input[name=groupCode]").val();
            var appendTr="";
            appendTr = appendTr +"<tr id='"+tid+"'><td class=\"tc\">";
            appendTr = appendTr +$(this).parents("tr").find("input[name=settlementAdultPrice]").prev().html();
            appendTr = appendTr +"<input type=\"text\"  ";
            appendTr = appendTr +' onkeyup="validNum_new(this)" onafterpaste="validNum_new(this)"';
            appendTr = appendTr +" name=\"adultDiscountPrice\"/></td>";
            appendTr = appendTr +"<td class=\"tc\">";
            appendTr = appendTr +$(this).parents("tr").find("input[name=settlementcChildPrice]").prev().html();
            appendTr = appendTr +" <input type=\"text\"  ";
            appendTr = appendTr  +	" onkeyup=\"validNum_new(this) \"   ";
            appendTr = appendTr  + " onafterpaste=\"validNum_new(this)\" ";
            appendTr = appendTr   +" name=\"childDiscountPrice\"/></td>";
            appendTr = appendTr +"<td class=\"tc\">";
            appendTr = appendTr +$(this).parents("tr").find("input[name=settlementSpecialPrice]").prev().html();
            appendTr = appendTr +"<input type=\"text\"  name=\"specialDiscountPrice\" onkeyup=\"validNum_new(this) \"  onafterpaste=\"validNum_new(this)\"  /></td>";
            appendTr = appendTr +"</tr>";
            $("#discount-setting-pop").find("tbody").append(appendTr)
        }
    });


    var $pop = $.jBox($("#discount-setting-pop").html(), {
        title: "查看优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if (v == "1") {
                var tip_Flag =true;
                //填充优惠数据
                    //成人优惠
                    var adultDiscountPrice_value = $(".mod_details2_tabletype").find("table>tbody").last().find("tr>td>input[name=adultDiscountPrice]").val();
                    //儿童优惠
                    var childDiscountPrice_value = $(".mod_details2_tabletype").find("table>tbody").last().find("tr>td>input[name=childDiscountPrice]").val()
                    //特殊人群优惠
                    var specialDiscountPrice_value = $(".mod_details2_tabletype").find("table>tbody").last().find("tr>td>input[name=specialDiscountPrice]").val()
                    //循环给父窗口元素赋值
                    $('[name="ids"]:checked').each(function (index,obj) {
                        // 成人同行价
                     var validate_settlementAdultPrice   =   $(obj).parent().parent().find("input[name=settlementAdultPrice]").val(); // 成人
                     var validate_settlementcChildPrice  =   $(obj).parent().parent().find("input[name=settlementcChildPrice]").val(); // 儿童
                     var validate_settlementSpecialPrice =   $(obj).parent().parent().find("input[name=settlementSpecialPrice]").val(); // 特殊人群
                     //获取团号
                     var tip_goupCode =    $(obj).parent().parent().find("input[name=groupCode]").val();

                     if(parseInt(adultDiscountPrice_value) >= parseInt(validate_settlementAdultPrice))
                     {
                         tip_Flag = false;
                         $.jBox.tip(tip_goupCode +'团成人优惠价格大于等于同行价!');
                         return false;
                     }
                    if(parseInt(childDiscountPrice_value) >= parseInt(validate_settlementcChildPrice))
                    {
                        tip_Flag = false;
                        $.jBox.tip(tip_goupCode +'团儿童优惠价格大于等于同行价!');
                        return false;
                    }
                    if(parseInt(specialDiscountPrice_value) >= parseInt(validate_settlementSpecialPrice))
                    {
                        tip_Flag = false;
                        $.jBox.tip(tip_goupCode +'团特殊人群优惠价格大于等于同行价!');
                        return false;
                    }

                        if(tip_Flag == false)
                        return false;
                        //给优惠价格赋值
                        $(obj).parent().parent().find("input[name=adultDiscountPrice]").val(adultDiscountPrice_value);
                        $(obj).parent().parent().find("input[name=childDiscountPrice]").val(childDiscountPrice_value);
                        $(obj).parent().parent().find("input[name=specialDiscountPrice]").val(specialDiscountPrice_value);

                    });

                if(tip_Flag == true)
                return true;
                else
                return false;
            }
            else
            {
                $("#discount-setting-pop").find('.first-part ul').html("");
                $("#discount-setting-pop").find("tbody").html("");

            }
        }, height: 300, width: 500,closed:function(){
            $("#discount-setting-pop").find('.first-part ul').html("");
            $("#discount-setting-pop").find("tbody").html("");
        },
        persistent: true
    });

    //inquiryCheckBOX();
}


/**
 * 修改优惠额度
 */
function jbox__modify_discount_setting_pop_fab(obj)
{
    var html = "<tr>";
    html= html+ "<td class='tc'>" ;
    html= html+  $(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").parent().find("span").html() ;
    html= html+ "<input type='text' name='discount'   value='" ;
    html= html+   $(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").val() ;
    html= html+    "' " ;
    html= html+     'onkeyup="validNum_new(this)" onafterpaste="validNum_new(this)"';
    html= html+     "/></td>";
    html= html+ "<td class='tc'>" ;
    html= html+    $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").parent().find("span").html() ;
    html= html+    "<input type='text' name='discount'    value='" ;
    html= html+    $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").val() ;
    html= html+    "' " ;
    html= html+    'onkeyup="validNum(this)" onafterpaste="validNum(this)"';
    html= html+    "/></td>"
    html= html+  "<td class='tc'>" ;
    html= html+     $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").parent().find("span").html() ;
    html= html+     "<input type='text' name='discount'   value='" ;
    html= html+    $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").val() ;
    html= html+     "' " ;
    html= html+    'onkeyup="validNum(this)" onafterpaste="validNum(this)"';
    html= html+     "/></td>"
    html= html+  "</tr>"
    $("#view-discount-setting-pop").find("tbody").html(html);
    $.jBox($("#view-discount-setting-pop").html(), {
        title: "修改优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if(v =="1")
            {
                //更新优惠价格
                var adultDiscount_Price = $($("#jbox-content").find("tbody").find("input")[0]).val();
                var childDiscount_Price = $($("#jbox-content").find("tbody").find("input")[1]).val();
                var specialDiscount_Price = $($("#jbox-content").find("tbody").find("input")[2]).val();


                var tip_goupCode =     $(obj).parent().parent().parent().parent().parent().find("input[name=groupCode]").val();
                var adultDiscount_Price_p = $(obj).parent().parent().parent().parent().parent().find("input[name=settlementAdultPrice]").val();
                var childDiscount_Price_p =$(obj).parent().parent().parent().parent().parent().find("input[name=settlementcChildPrice]").val();
                var specialDiscount_Price_p =$(obj).parent().parent().parent().parent().parent().find("input[name=settlementSpecialPrice]").val();

                if(parseInt(adultDiscount_Price) > parseInt(adultDiscount_Price_p))
                {
                    $.jBox.tip(tip_goupCode +'团成人优惠价格大于同行价!');
                    return false;
                }
                if(parseInt(childDiscount_Price) > parseInt(childDiscount_Price_p))
                {
                    $.jBox.tip(tip_goupCode +'团儿童优惠价格大于同行价!');
                    return false;
                }
                if(parseInt(specialDiscount_Price) > parseInt(specialDiscount_Price_p))
                {
                    $.jBox.tip(tip_goupCode +'团特殊人群优惠价格大于同行价!');
                    return false;
                }
                //获取团号
                $(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").val(adultDiscount_Price);
                $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").val(childDiscount_Price);
                $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").val(specialDiscount_Price);
            }
            $("#view-discount-setting-pop").find("tbody").html("");
        },close:function() {
            $("#view-discount-setting-pop").find("tbody").html("");
        },
        height: 300, width: 500,
        persistent: true
    });


}

//批量修改优惠额度
function jbox__batch_modify_discount_setting_pop_fab() {
    var $checks = $('[name="ids"]:checked');
    if ($checks.length < 1) {
        $.jBox.tip('请选择团期!');
        return;
    }
    var $pop = $.jBox($("#batch-modify-discount-setting-pop").html(), {
        title: "修改优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if (v == '1') {
                $.jBox.confirm("XXXX团已有优惠额度是否确认替换？", "提示", function (v) {
                    if (v == 'ok') {

                    }
                });
                //return true;
            }
        }, height: 300, width: 500,
        persistent: true
    });
    $('[name="ids"]:checked').each(function () {
        $pop.find('.first-part ul').append('<li><input type="text" value="' + $(this).parents('tr:first').find('[name="groupCode"]').val() + '"></li>')
    });
    inquiryCheckBOX();
}
//单独修改优惠额度
function jbox__single_modify_discount_setting_pop_fab() {
    var $pop = $.jBox($("#modify-discount-setting-pop").html(), {
        title: "修改优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 300, width: 500,
        persistent: true
    });
    $('[name="ids"]:checked').each(function () {
        $pop.find('.first-part ul').append('<li><input type="text" value="' + $(this).parents('tr:first').find('[name="groupCode"]').val() + '"></li>')
    });
    inquiryCheckBOX();
}
function jbox__nosel_group_discount_setting_pop_pop_fab() {
    $.jBox($("#nosel-group-discount-setting-pop").html(), {
        title: "提示", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 150, width: 300,
        persistent: true
    });
    inquiryCheckBOX();
}


//S--C109--未选择团期--设置优惠额度提示-弹窗

function jbox__fail_tips_create_order_pop_fab() {
    $.jBox($("#fail-discount-setting-pop").html(), {
        title: "提示", buttons: {'关闭': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 150, width: 300,
        persistent: true
    });
    inquiryCheckBOX();
}

//查看优惠额度

function jbox__view_discount_setting_pop_fab(obj) {
    //拼接表体

    var html = "<tr><td class=\"tc\">"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").parent().find("span").html()
        +"<span>"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").val();
        +"</span></td>";
    html =html + "<td class=\"tc\">"
        + $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").parent().find("span").html()
        +"<span>"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").val();
        +"</span></td>";
    html =html + "<td class=\"tc\">"
        + $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").parent().find("span").html()
        +"<span>"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").val();
        +"</span></td> </tr>";
    $("#view-discount-setting-pop").find("tbody").html(html);

    $.jBox($("#view-discount-setting-pop").html(), {
        title: "查看优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            $("#view-discount-setting-pop").find("tbody").html("");
        },close:function() {
            $("#view-discount-setting-pop").find("tbody").html("");
        },
        height: 300, width: 500,
        persistent: true
    });

}

//展示所选择的团
$(function display_Group_all_sel() {
    $(".control-display").on("click", function () {
        $(".first-part").addClass("open");
    });
});

//查看产品-散拼-详情-无优惠

function jbox__none_discount_setting_pop_fab() {
    $.jBox($("#none-discount-setting-pop").html(), {
        title: "提示", buttons: {'关闭': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 150, width: 200,
        persistent: true
    });
    inquiryCheckBOX();
}

//团号&订单切换

function orderOrGroupList(type, elem) {
    if (type == "group") {
        $('#contentTable_order').hide();
        $('#contentTable_group').show();
        $(elem).addClass('select');
        $(elem).siblings().removeClass('select');
    }
    if (type == "order") {
        $('#contentTable_order').show();
        $('#contentTable_group').hide();
        $(elem).addClass('select');
        $(elem).siblings().removeClass('select');
    }
}
//table中“团号“、”产品“切换
function switchNumAndPro() {
    //点击团号
    $(".activitylist_bodyer_table").delegate(".tuanhao", "click", function () {
        $(this).addClass("on").siblings().removeClass('on');
        $('.chanpin_cen').removeClass('onshow');
        $('.tuanhao_cen').addClass('onshow');
    });
    //点击产品
    $(".activitylist_bodyer_table").delegate(".chanpin", "click", function () {
        $(this).addClass("on").siblings().removeClass('on');
        $('.tuanhao_cen').removeClass('onshow');
        $('.chanpin_cen').addClass('onshow');
    });
}
function switchSalerAndPicker() {
    //点击销售
    $(".activitylist_bodyer_table").on("click", ".order-saler-title", function () {
        $(this).addClass("on").siblings().removeClass('on');
        var $table = $(this).parents('table:first');
        $table.find('.order-saler').addClass('onshow');
        $table.find('.order-picker').removeClass('onshow');
    });
    //点击下单人
    $(".activitylist_bodyer_table").on("click", ".order-picker-title", function () {
        $(this).addClass("on").siblings().removeClass('on');
        var $table = $(this).parents('table:first');
        $table.find('.order-saler').removeClass('onshow');
        $table.find('.order-picker').addClass('onshow');
    });
}


//S--C109--订单散拼订单-生成订单-弹窗

function jbox__tips_create_order_pop_fab() {
    $.jBox($("#tips-create-order-pop").html(), {
        title: "提示", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if (v == "1") {
                $.jBox.tip('订单生成成功!', 'success');
                //$.jBox.success('订单生成成功！', 'jBox');
                return true;
            }
        }, height: 150, width: 300,
        persistent: true
    });
    inquiryCheckBOX();
}

//S--C109--订单散拼订单-生成订单成功-弹窗

function jbox__success_tips_create_order_pop_fab() {
    $.jBox.tip('订单生成成功!', 'success');
    persistent: true
}


//S--C109--订单散拼订单-生成订单失败-弹窗

function jbox__fail_tips_create_order_pop_fab() {
    $.jBox($("#fail-discount-setting-pop").html(), {
        title: "提示", buttons: {'关闭': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 150, width: 300,
        persistent: true
    });
    inquiryCheckBOX();
}


//S--C109--保存并提交审批-报名游客中有低于优惠的范围-弹窗

function jbox__tips_tourist_fee_less_pop_fab() {
    $.jBox($("#tips-tourist-fee-less-pop").html(), {
        title: "提示", buttons: {'关闭': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 150, width: 300,
        persistent: true
    });
    inquiryCheckBOX();
}

//S--C109--报名游客中申请优惠-弹窗

$(document).on('blur', '[name="discountValue"]', function () {
    //300为优惠额度，此处仅为展示效果
    if (+$(this).val() > 300) {
        $.jBox.tip('优惠金额大于优惠额度');
    }
});

function jbox__apply_for_tourist_fee__pop_fab() {
    var $pop = $.jBox($("#apply-for-tourist-fee-pop").html(), {
        title: "申请优惠", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
            if (v == "1") {
                var msg;
                $pop.find('input[type="checkbox"]:checked').each(function () {
                    if (!$(this).parents('tr:first').find('input[name="discount"]').val()) {
                        msg = "申请优惠金额必须填写！";
                        return;
                    }
                });
                if (msg) {
                    $.jBox.tip(msg);
                    return false;
                }
                $pop.find('input[name="discount"]').each(function () {
                    if ($(this).val() && (!$(this).parents('tr:first').find('input[type="checkbox"]').attr('checked'))) {
                        msg = "未勾选游客！";
                        return;
                    }
                });
                if (msg) {
                    $.jBox.tip(msg);
                    return false;
                }
                return true;
            }
        }, height: 400, width: 600,
        persistent: true
    });
    inquiryCheckBOX();
}

//优惠金额≥0正整数
$(document).on("keypress", "input[name='discount']", function (event) {
    var retValue = false;
    var keyCode = event.charCode == undefined ? event.keyCode : event.charCode;
    if ((keyCode >= 48 && keyCode <= 57) || keyCode == 99 || keyCode == 118) {
        retValue = true;
    } else if (keyCode == 45) {
        retValue = $(this).val().indexOf(String.fromCharCode(keyCode)) < 0;
    }
    if (retValue) {
        $(this).data("oldvalue", $(this).val());
    }
    return keyCode == 0 || retValue;
});
$(document).on("keyup", "input[name='discount']", function () {
    var isNegative = $(this).attr('isNegative');
    var numberReg = new RegExp('^\\d{0,}$');
    if ($(this).attr('isNegative')) {
        numberReg = new RegExp('^-?\\d{0,}$');
    }
    if ($(this).val() && !numberReg.test($(this).val())) {
        $(this).val($(this).data("oldvalue"));
    } else {
        $(this).data("oldvalue", $(this).val());
    }
})




//驳回/审批通过按钮对应的弹出框
function jbox_bohui() {
    var html = '<div class="add_allactivity"><label>请填写您的审批备注!</label>';
    html += '<textarea style="width:80%; margin:10px auto;" name="" cols="" rows="" ></textarea>';
    html += '</div>';
    $.jBox(html, {
        title: "备注", buttons: {"提 交": "1"}, submit: function (v, h, f) {
            if (v == "1") {
            }
        }, height: 250, width: 500
    });

}

//当优惠金额大于优惠额度时，系统需要提醒
function discountSettingMoneyTips() {
    if ($("#apply-for-tourist-fee-pop input[name='discount']").val() == "") {
    }
    else {

    }
    $("#apply-for-tourist-fee-pop input[name='discount']").val();
}


/**
 *
 *散拼修改页--查看优惠额度
 */

function jbox__view_discount_setting_pop_fab_mod(obj) {
    //拼接表体

    var html = "<tr><td class=\"tc\">"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").parent().find("span").html()
        +"<span>"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").val();
    +"</span></td>";
    html =html + "<td class=\"tc\">"
        + $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").parent().find("span").html()
        +"<span>"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").val();
    +"</span></td>";
    html =html + "<td class=\"tc\">"
        + $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").parent().find("span").html()
        +"<span>"
        +$(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").val();
    +"</span></td> </tr>";
    $("#view-discount-setting-pop").find("tbody").html(html);

    $.jBox($("#view-discount-setting-pop").html(), {
        title: "查看优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            $("#view-discount-setting-pop").find("tbody").html("");
        },close:function() {
            $("#view-discount-setting-pop").find("tbody").html("");
        },
        height: 300, width: 500,
        persistent: true
    });

}


/**
 * 散拼修改页---修改优惠额度
 */
function jbox__modify_discount_setting_pop_fab_mod(obj)
{
    var html = "<tr>";
    html= html+ "<td class='tc'>" +
        $(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").parent().find("span").html() +
        "<input type='text' name='discount' value='" +
        $(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").val().trim() +
        "' " +
        'onkeyup="validNum(this)" onafterpaste="validNum(this)"'+
        "/></td>"
    html= html+ "<td class='tc'>" +
        $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").parent().find("span").html() +
        "<input type='text' name='discount' value='" +
        $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").val().trim() +
        "' " +
        'onkeyup="validNum(this)" onafterpaste="validNum(this)"'+"/></td>"
    html= html+  "<td class='tc'>" +
        $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").parent().find("span").html() +
        "<input type='text' name='discount' value='" +
        $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").val().trim() +
        "' " +
        'onkeyup="validNum(this)" onafterpaste="validNum(this)"'+
        "/></td>"
    html= html+  "</tr>"
    $("#view-discount-setting-pop").find("tbody").html(html);
    $.jBox($("#view-discount-setting-pop").html(), {
        title: "修改优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if(v =="1")
            {
                //更新优惠价格
                $(obj).parent().parent().parent().parent().parent().find("input[name=adultDiscountPrice]").val($($("#jbox-content").find("tbody").find("input")[0]).val());
                $(obj).parent().parent().parent().parent().parent().find("input[name=childDiscountPrice]").val($($("#jbox-content").find("tbody").find("input")[1]).val());
                $(obj).parent().parent().parent().parent().parent().find("input[name=specialDiscountPrice]").val($($("#jbox-content").find("tbody").find("input")[2]).val());

            }
            $("#view-discount-setting-pop").find("tbody").html("");
        },close:function() {
            $("#view-discount-setting-pop").find("tbody").html("");
        },
        height: 300, width: 500,
        persistent: true
    });


}

function  jbox__discount_setting_pop_fab_mod ()
{
	
    var $checks = $('[name="idss"]:checked');
    if ($checks.length < 1) {
        $.jBox.tip('请选择团期!');
        return;
    }

    var settlementAdultPriceFlag="";
    var settlementcChildPriceFlag="";
    var settlementSpecialPriceFlag="";
    var returnFlag = true;
    $('[name="idss"]:checked').each(function (index,obj) {
        //判断是否所选币种一致
        if(index == 0)
        {
            settlementAdultPriceFlag=$(obj).parent().parent().find("input[name=settlementAdultPrice]").parent().find("span").html();
            settlementcChildPriceFlag=$(obj).parent().parent().find("input[name=settlementcChildPrice]").parent().find("span").html();
            settlementSpecialPriceFlag=$(obj).parent().parent().find("input[name=settlementSpecialPrice]").parent().find("span").html();
        }
        else
        {
            if(
            settlementAdultPriceFlag  !=$(obj).parent().parent().find("input[name=settlementAdultPrice]").parent().find("span").html() ||
            settlementcChildPriceFlag !=$(obj).parent().parent().find("input[name=settlementcChildPrice]").parent().find("span").html()||
            settlementSpecialPriceFlag !=$(obj).parent().parent().find("input[name=settlementSpecialPrice]").parent().find("span").html())
            {
                returnFlag = false;
                return false;
            }
        }
    });

if(returnFlag == false)
{
    $.jBox.tip("币种类型不统一!");
    return;
}




    $('[name="idss"]:checked').each(function (index,obj) {
        $("#discount-setting-pop").find('.first-part ul').append('<li><input type="text" value="' + $(this).parents("tr").find("[name=groupCode]").val() + '"></li>');
        ////拼接表体
        if(index ==0)
        {
            var tid= $(this).parents("tr").find("input[name=groupCode]").val();
            var appendTr="";
            appendTr = appendTr +"<tr id='"+tid+"'><td class=\"tc\">";
			if($(this).parents("tr").find("input[name=settlementAdultPrice]").prev().html() != undefined)
            appendTr = appendTr +$(this).parents("tr").find("input[name=settlementAdultPrice]").prev().html();
            appendTr = appendTr +"<input type=\"text\" "
                +'onkeyup="validNum(this)" onafterpaste="validNum(this)"'
                +"name=\"adultDiscountPrice\" /></td>";
            appendTr = appendTr +"<td class=\"tc\">";
            if($(this).parents("tr").find("input[name=settlementcChildPrice]").prev().html() != undefined)
            appendTr = appendTr +$(this).parents("tr").find("input[name=settlementcChildPrice]").prev().html();
            appendTr = appendTr +"<input type=\"text\""
                +	'onkeyup="validNum(this)" onafterpaste="validNum(this)"'
                +" name=\"childDiscountPrice\"  /></td>";
            appendTr = appendTr +"<td class=\"tc\">";
             if($(this).parents("tr").find("input[name=settlementSpecialPrice]").prev().html() != undefined)
            appendTr = appendTr +$(this).parents("tr").find("input[name=settlementSpecialPrice]").prev().html();
            appendTr = appendTr +"<input type=\"text\" name=\"specialDiscountPrice\"  /></td>";
            appendTr = appendTr +"</tr>";
            $("#discount-setting-pop").find("tbody").append(appendTr)
        }
    });

//当设置的金额一致的时间系统自动为各值填充值，否则置为空
    var adultDiscountPrice_="";
    var childDiscountPrice_="";
    var specialDiscountPrice_="";
    $('[name="idss"]:checked').each(function (index,obj)
    {
        if(index == 0)
        {
            adultDiscountPrice_ =$(obj).parent().parent().find("input[name=adultDiscountPrice]").val().trim();
            childDiscountPrice_ = $(obj).parent().parent().find("input[name=childDiscountPrice]").val().trim();
            specialDiscountPrice_=$(obj).parent().parent().find("input[name=specialDiscountPrice]").val().trim();
        }
        else
        {
            if(adultDiscountPrice_ != $(obj).parent().parent().find("input[name=adultDiscountPrice]").val().trim())
                adultDiscountPrice_="";
            if(childDiscountPrice_ != $(obj).parent().parent().find("input[name=childDiscountPrice]").val().trim())
                childDiscountPrice_="";
            if(specialDiscountPrice_ != $(obj).parent().parent().find("input[name=specialDiscountPrice]").val().trim())
                specialDiscountPrice_="";
        };
    });
    //给弹出框设置默认值
    $("#discount-setting-pop").find("tbody>tr>td").eq(0).find("input").attr("value",adultDiscountPrice_);
    $("#discount-setting-pop").find("tbody>tr>td").eq(1).find("input").attr("value",childDiscountPrice_);
    $("#discount-setting-pop").find("tbody>tr>td").eq(2).find("input").attr("value",specialDiscountPrice_);


    var $pop = $.jBox($("#discount-setting-pop").html(), {
        title: "查看优惠额度", buttons: {'关闭': 0, '确认': 1}, submit: function (v, h, f) {
            if (v == "1") {
                //填充优惠数据
                    //成人优惠
                    var adultDiscountPrice_value = $(".mod_details2_tabletype").find("table>tbody").last().find("tr>td>input[name=adultDiscountPrice]").val();
                    //儿童优惠
                    var childDiscountPrice_value = $(".mod_details2_tabletype").find("table>tbody").last().find("tr>td>input[name=childDiscountPrice]").val()
                    //特殊人群优惠
                    var specialDiscountPrice_value = $(".mod_details2_tabletype").find("table>tbody").last().find("tr>td>input[name=specialDiscountPrice]").val()
                    //循环给父窗口元素赋值
                var submit = function (v, h, f) {
                    if (v == true)
                    {
                        //修改父窗口数据
                        $('[name="idss"]:checked').each(function (index,obj) {
                            $(obj).parent().parent().find("input[name=adultDiscountPrice]").val(adultDiscountPrice_value);
                            $(obj).parent().parent().find("input[name=childDiscountPrice]").val(childDiscountPrice_value);
                            $(obj).parent().parent().find("input[name=specialDiscountPrice]").val(specialDiscountPrice_value);
                        });
                        jBox.tip("修改成功！", 'success');
                    }
                    else {
                    }

                    return true;
                };
                var modifyGroupNo = '';
                $('[name="idss"]:checked').each(function (index,obj) {
                    var currentAdultDiscountPrice = $(obj).parent().parent().find("input[name=adultDiscountPrice]").val();
                    var currentChildDiscountPrice = $(obj).parent().parent().find("input[name=childDiscountPrice]").val();
                    var currentSpecialDiscountPrice = $(obj).parent().parent().find("input[name=specialDiscountPrice]").val();
                    if(currentAdultDiscountPrice!=currentAdultDiscountPrice||currentChildDiscountPrice!=childDiscountPrice_value||currentSpecialDiscountPrice!=specialDiscountPrice_value){
                        if(modifyGroupNo!=''){
                            modifyGroupNo+=',';
                        }
                        modifyGroupNo += $(obj).parent().parent().find('[name="groupCode"]').val();
                    }
                });
                jBox.confirm(modifyGroupNo+"团已有优惠额度是否确认替换", "修改提示提示", submit, { id:'update', showScrolling: false, buttons: { '确认': true, '关闭': false } });
                return true;
            }
            else
            {
                $("#discount-setting-pop").find('.first-part ul').html("");
                $("#discount-setting-pop").find("tbody").html("");

            }
        }, height: 300, width: 500,closed:function(){
            $("#discount-setting-pop").find('.first-part ul').html("");
            $("#discount-setting-pop").find("tbody").html("");
        },
        persistent: true
    });

    //inquiryCheckBOX();

}
/**
 * 详情页查看优惠额度
 * @param {} obj
 */
function jbox__view_discount_setting_pop_fab_view(obj)
{
	
    //拼接表体

    var html = "<tr><td class=\"tc\">"
        +$(obj).parent().parent().find("td[id=adultDiscountPrice]").html().replace(/[\r\n]/g,"").replace(/\s/g,"")
        +"</td>";
    html =html + "<td class=\"tc\">"
        +$(obj).parent().parent().find("td[id=childDiscountPrice]").html().replace(/[\r\n]/g,"").replace(/\s/g,"")
    +"</td>";
    html =html + "<td class=\"tc\">"
         +$(obj).parent().parent().find("td[id=specialDiscountPrice]").html().replace(/[\r\n]/g,"").replace(/\s/g,"")
    +"</td> </tr>";
    $("#view-discount-setting-pop").find("tbody").html(html);
    

    $.jBox($("#view-discount-setting-pop").html(), {
        title: "查看优惠额度", buttons: {'关闭': 0}, submit: function (v, h, f) {
            $("#view-discount-setting-pop").find("tbody").html("");
        },close:function() {
            $("#view-discount-setting-pop").find("tbody").html("");
        },
        height: 300, width: 500,
        persistent: true
    });


}
function validNum_new(dom){
    var thisvalue = $(dom).val();
    var minusSign = false;
    if(thisvalue){
        if(/^\-/.test(thisvalue)){
            minusSign = true;
            thisvalue = thisvalue.substring(1);
        }
        //thisvalue = thisvalue.replace(/\D/g,"");
        thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
        var txt = thisvalue.split(".");
        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
        if(minusSign){
           // thisvalue = '-' + thisvalue;
        }
        $(dom).val(thisvalue);
    }
}

$("#apply-for-tourist-fee-pop input[type='checkbox']")
