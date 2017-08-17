var ctx = $('#ctx').val();
$(function () {
    use_message();
    check_number();
    checkout_use();
    adopt();
    area();
});
/**
 * 采用是否显示
 */
function adopt() {
    $('.adopt_use').each(function () {
        var children = $(this).parent().next().children();
        $(this).text() == '' ? (children.hide(), $(this).text('-')) : children.show();
    })
}
/**
 * 采用信息
 */
function use_message() {
    $('.use_btn').click(function () {
        var left, right, text, eq_use;
        text = $(this).parent().prev().children().eq(1).text();
        eq_use = $(this).attr('data-id');
        if (eq_use == 4) {
            if (text.indexOf('-') != -1) {
                left = text.split('-')[0];
                right = text.split('-')[1];
            } else {
                left = text.split('-')[1];
                right = text.split('-')[0];
            }
            $('.input_use_left').val(left);
            $('.input_use_right').val(right);
        } else {
            $('.checkout_use').eq(eq_use).val(text);
        }
    })
}
/**
 * 验证只能输入数字
 */
function check_number() {
    $('input.number').on('input', function () {
        check_use(this);
    })
}
/**
 * 确认合并再次验证只能输入数字
 */
function check_number_use() {
    $('input.number').each(function () {
        check_use(this);
    })
}
/**
 * 验证只能输入数字复用函数
 */
function check_use(obj) {
    var partten = /^[0-9]+$/;
    if (!partten.test($(obj).val())) {
        var a = $(obj).val();
        var b = a.replace(/[^0-9]+/gi, '');
        $(obj).val(b);
    }
}
/**
 * 确认合并信息校验
 */
function checkout_use() {
    var _checkout_use_select = $('.checkout_use_select');
    $('#checkout').click(function () {
        check_number_use();
        var _num = 0;
        $('.checkout_use').each(function () {
            if ($(this).val() == '') {
                ++_num;
                $(this).addClass('border_red');
                $(this).parent().find('.checkout_text').show();
            } else {
                $(this).removeClass('border_red');
                $(this).parent().find('.checkout_text').hide();
            }
        });
        if ($('.checkout_use_select option:selected').text() == "国家") {
            ++_num;
            _checkout_use_select.addClass('border_red');
            _checkout_use_select.parent().find('.checkout_text').show();
        } else {
            _checkout_use_select.removeClass('border_red');
            _checkout_use_select.parent().find('.checkout_text').hide();
        }
        if (_num == 0) { // 提交
            var data = $("#form").serialize();
            $.ajax({
                type: "POST",
                async: false,
                url: ctx+"/mobileUser/confirmCorrelation",
                data: data,
                success: function (res) {
                    if (res.result == true) {
                        top.$.jBox.tip(res.msg, 'info');
                        window.location.href = ctx + res.url;
                    } else {
                        top.$.jBox.tip(res.msg, 'error');
                    }
                }
            });
        }
    })
}
/**
 * 取消按钮
 */
$('#cancel').click(function () {
    window.location.href = ctx + '/mobileUser/cancelCorrelation';
});
/**
 * 国家、省、市三级联动
 */
function area() {
    $("#area_use select").change(function () {
        var _parentId = $(this).val();
        var _url = "/a" + "/agent/manager/getAreaInfoById/" + _parentId;
        var _currentId = $(this).attr('id');
        if (_parentId.length > 0) {
            $.ajax({
                type: "POST",
                url: _url,
                dataType: "json",
                success: function (msg) {
                    if (_currentId == "country") {
                        $("#province").empty().append("<option value=''>省(直辖市)</option>");
                        $("#city").empty().append("<option value=''>市(区)</option>");
                        $.each(msg, function (i, n) {
                            var _optionStr = "<option value='" + n.id + "'>" + n.name + "</option>";
                            $("#province").append(_optionStr);
                        });
                    }
                    if (_currentId == "province") {
                        $("#city").empty().append("<option value=''>市(区)</option>");
                        $.each(msg, function (i, n) {
                            var _optionStr = "<option value='" + n.id + "'>" + n.name + "</option>";
                            $("#city").append(_optionStr);
                        });
                    }
                }
            });
        } else {
            if (_currentId == "country") {
                $("#province").empty().append("<option value=''>省(直辖市)</option>");
                $("#city").empty().append("<option value=''>市(区)</option>");
            }
            if (_currentId == "province") {
                $("#city").empty().append("<option value=''>市(区)</option>");
            }
        }
    });
}

