/**
 * Created by shijun.liu on 2016.06.17
 */
//新建联系人
function new_contacts(ctx) {
    var div = '<div class="contact_information">'
        + '<p class="head_background">'
        + '<span class="inline"><font>*</font>联系人：</span><span class="contact_information_name head_hide hide" id="contactName"></span><input type="text" name="contactName" class="contact_information_input" maxlength="9"/>'
        + '<span class="inline"><font>*</font>手机号：</span><span class="head_hide hide inline" id="contactMobile"></span><input type="text" name="contactMobile" class="contact_information_input" maxlength="11" />'
        + '<input type="hidden" name="contactId" value="" />'
        + '<span class="float_right ">'
        + '<span class="slide" onclick="slide_contacts_open_close(this)"><em class="t1_2 slide_open_close"></em></span>'
        + '</span>'
        + '<span class="float_right hide delete_save">'
        + '<span class="delete" onclick="delete_contacts(this, \'' + ctx + '\')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save" onclick="save_contacts(\'' + ctx + '\', this);"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile hide" onclick="editor_contacts(this);"><em class="t1_2 compile_t1_2"></em> 编辑</span>'
        + '</span>'
        + '</p>'
        + '<div class="border_top">'
        + '<table class="channel_information_table">'
        + '<tr>'
        + '<td width="95">固定电话：</td>'
        + '<td width="400">'
        + '<span class="channel_phone hide" id="contactPhone">'
        + '</span>'
        + '<span class="input">'
        + '<input type="text" class="min_input" name="contactPhoneCode" maxlength="4"/>-'
        + '<input type="text" class="min_right_input" name="contactPhone" maxlength="8"/>'
        + '</span>'
        + '</td>'
        + '<td width="95">QQ：</td>'
        + '<td width="360">'
        + '<span class="hide" id="contactQQ"></span>'
        + '<input type="text" name="contactQQ" maxlength="14"/>'
        + '</td>'
        + '</tr>'
        + '<tr>'
        + '<td>传真：</td>'
        + '<td>'
        + '<span class="channel_phone hide" id="contactFax">'
        + '</span>'
        + '<span class="input">'
        + '<input type="text" class="min_input" name="contactFaxCode" maxlength="6"/>-'
        + '<input type="text" class="min_right_input" name="contactFax" maxlength="13"/>'
        + '</span>'
        + '</td>'
        + '<td>电子邮箱：</td>'
        + '<td>'
        + '<span class="hide" id="contactEmail" style="display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 300px;"></span>'
        + '<input type="text" name="contactEmail" maxlength="50"/>'
        + '</td>'
        + '</tr>'
        + '<tr>'
        + '<td>描述：</td>'
        + '<td>'
        + '<span class="hide" id="contactRemark"></span>'
        + '<textarea name="contactRemark" id="" cols="30" rows="10" class="contact_information_text" maxlength="200"></textarea>'
        + '</td>'
        + '<td></td>'
        + '<td></td>'
        + '</tr>'
        + '</table>'
        + '</div>'
        + '</div>';
    var p = $("#contact_information>p");
    p.after(div);
    var partten = /^\d+$/;
    $(document).ready(function () {
        $('#phone1').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
        $('#phone2').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
        $('#fax1').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
        $('#fax2').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
        $('input[name="contactQQ"]').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
        $('input[name="contactMobile"]').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });

        //需求调整，座机号码允许输入20位 ymx Start
        // $('input[name="contactPhoneCode"]').keyup(function () {
        //     if (!partten.test($(this).val())) {
        //         var a = $(this).val();
        //         var b = a.replace(/[^\d]+/gi, '');
        //         $(this).val(b);
        //     }
        // });
        // $('input[name="contactPhone"]').keyup(function () {
        //     if (!partten.test($(this).val())) {
        //         var a = $(this).val();
        //         var b = a.replace(/[^\d]+/gi, '');
        //         $(this).val(b);
        //     }
        // });
        //需求调整，座机号码允许输入20位 ymx End

        $('input[name="contactFaxCode"]').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
        $('input[name="contactFax"]').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
    });
}
/**
 * 保存联系人信息
 * @param ctx
 * @param obj
 * @returns {boolean}
 */
function save_contacts(ctx, obj) {
    var $this = $(obj);
    var div = $this.parent().parent().next();
    var parent = $this.parent().parent().parent().parent();
    var contacts = {};  //联系人对象
    //联系人ID
    var contactId = $this.parent().parent().find('input[name="contactId"]').val();
    //联系人名称
    var contactName = $this.parent().parent().find('input[name="contactName"]').val();
    if (!contactName) {
        alert("请输入姓名");
        return false;
    }
    $this.parent().parent().find('#contactName').html(contactName);
    //联系人手机
    var contactMobile = $this.parent().parent().find('input[name="contactMobile"]').val();
    if (!contactMobile) {
        alert("请输入手机号");
        return false;
    }
    $this.parent().parent().find('#contactMobile').html(contactMobile);
    //联系人电话
    var contactPhone = div.find('input[name="contactPhoneCode"]').val() + '-' + div.find('input[name="contactPhone"]').val();
    if (contactPhone != "-") {
        div.find('#contactPhone').html(contactPhone);
    } else {
        div.find('#contactPhone').html("");
    }
    //联系人传真
    var contactFax = div.find('input[name="contactFaxCode"]').val() + '-' + div.find('input[name="contactFax"]').val();
    if (contactFax != "-") {
        div.find('#contactFax').html(contactFax);
    } else {
        div.find('#contactFax').html("");
    }
    //联系人Email
    var contactEmail = div.find('input[name="contactEmail"]').val();
    div.find('#contactEmail').html(contactEmail);
    div.find('#contactEmail').attr('title', contactEmail);
    //联系人QQ
    var contactQQ = div.find('input[name="contactQQ"]').val();
    div.find('#contactQQ').html(contactQQ);
    //联系人微信
    var wechatCode = div.find('input[name="wechatCode"]').val();
    div.find('#wechatCode').html(wechatCode);
    //联系人描述
    var contactRemark = div.find('textarea[name="contactRemark"]').val();
    div.find('#contactRemark').html(contactRemark);
    contacts.id = contactId;
    contacts.contactName = contactName;
    contacts.contactMobile = contactMobile;
    contacts.contactPhone = contactPhone;
    contacts.contactFax = contactFax;
    contacts.contactEmail = contactEmail;
    contacts.contactQQ = contactQQ;
    contacts.wechatCode = wechatCode;
    contacts.remarks = contactRemark;

    var contactsJson = JSON.stringify(contacts);
    $.ajax({
        type: "POST",
        url: ctx + "/person/info/saveContacts",
        data: {
            contacts: contactsJson
        },
        dataType: "json",
        success: function (result) {
            if (!result.flag) {
                alert("保存联系人信息失败，" + result.msg);
            } else {
                $this.parent().parent().find('input[name="contactId"]').val(result.msg);
                top.$.jBox.tip("保存成功", "success", {top: '0'});
                $(".jbox").css("position", "fixed");
                $(".jbox").css("top", "0");
            }
        }
    });
    div.find("span").show()
    div.find(".input").hide();
    div.find("input,textarea").hide();
    $this.parent().parent().find(".head_hide").css("display", "inline-block");
    $this.parent().parent().find("input").hide();
    $this.hide();
    $this.siblings().show();
    if (contactPhone == "-") {
        div.find(".gang1").hide();
    }
    if (contactFax == "-") {
        div.find(".gang2").hide();
    }
    var p = parent.find(".head_background");
    if (p.size() > 1) {
        var deleteSpan = p.find(".delete");
        for (var i = 0; i < deleteSpan.length; i++) {
            $(deleteSpan[i]).show();
        }
    } else {
        var deleteSpan = $this.parent().find(".delete");
        deleteSpan.hide();
    }
}
/**
 * 删除联系人
 * @param Obj
 * @param ctx
 * @param contactsId
 * @returns {boolean}
 */
function delete_contacts(Obj, ctx) {
    var $this = $(Obj);
    var contactsId = $this.parent().parent().find('input[name="contactId"]').val();
    if (!contactsId) {
        var $contact = $this.parent().parent().parent();
        $contact.remove();
        return false;
    } else {
        $.jBox.confirm("您确认删除此联系人么？", "系统提示", function (v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: ctx + "/person/info/deleteContacts",
                    data: {id: contactsId},
                    dataType: "json",
                    success: function (result) {
                        if (!result.flag) {
                            alert("删除联系人信息失败，" + result.msg);
                        } else {
                            var $contact = $this.parent().parent().parent();
                            var parent = $contact.parent();
                            $contact.remove();
                            var p = parent.find(".head_background");
                            if (p.size() == 1) {
                                var deleteSpan = p.find(".delete");
                                deleteSpan.hide();
                            }
                            top.$.jBox.tip("删除成功", "success", {top: '0'})
                            $(".jbox").css("position", "fixed");
                            $(".jbox").css("top", "0");
                        }
                    }
                });
            }
        });
    }
}
/**
 * 编辑联系人
 * @param obj
 */
function editor_contacts(obj) {
    var $this = $(obj);
    $this.hide();
    $this.siblings().show();
    $this.parent().siblings().find(".slide_open_close").css("background-position", "-145px -48px");
    var div = $this.parent().parent().next();
    div.slideDown();
    div.find("span").hide();
    div.find(".input").css("display", "inline-block");
    div.find("input,textarea").show();
    div.find(".gang1").show();
    div.find(".gang2").show();
    $this.parent().parent().find(".head_hide").hide();
    $this.parent().parent().find("input").show();
    //给input输入框赋值:
    var contactId = $this.parent().parent().find('input[name="contactId"]').val();
    $this.parent().parent().find('input[name="contactId"]').val(contactId);
    //联系人名称
    var contactName = $this.parent().parent().find('#contactName').text();
    $this.parent().parent().find('input[name="contactName"]').val(contactName);
    //联系人手机
    var contactMobile = $this.parent().parent().find('#contactMobile').text();
    $this.parent().parent().find('input[name="contactMobile"]').val(contactMobile);
    //联系人电话
    var contactPhone = div.find('#contactPhone').text();
    div.find('input[name="contactPhoneCode"]').val(contactPhone.split('-')[0]);
    div.find('input[name="contactPhone"]').val(contactPhone.split('-')[1]);
    //联系人传真
    var contactFax = div.find('#contactFax').text();
    div.find('input[name="contactFaxCode"]').val(contactFax.split('-')[0])
    div.find('input[name="contactFax"]').val(contactFax.split('-')[1]);
    //联系人Email
    var contactEmail = div.find('#contactEmail').text();
    div.find('input[name="contactEmail"]').val(contactEmail);
    //联系人QQ
    var contactQQ = div.find('#contactQQ').text();
    div.find('input[name="contactQQ"]').val(contactQQ);
    //联系人描述
    var contactRemark = div.find('#contactRemark').text();
    div.find('textarea[name="contactRemark"]').val(contactRemark);
    var parent = $this.parent().parent().parent().parent();
    var p = parent.find(".head_background");
    if (p.size() <= 1) {
        var deleteSpan = $this.parent().find(".delete");
        deleteSpan.hide();
    }
}

/**
 * 展开联系人
 * @param obj
 */
function slide_contacts_open_close(obj) {
    var $this = $(obj);
    var div = $this.parent().parent().next();
    if (div.is(":hidden")) {
        div.slideDown();
        $this.children().css("background-position", "-145px -48px");
    } else {
        div.slideUp();
        $this.children().css("background-position", " -121px -47px");
    }
}


/*********************银行账户******************************************/
/**
 * 新建银行账户
 */
function new_agent_bank(ctx) {
    // 没有权限添加的DIV
	    var oldDiv='<div class="contact_information">'
	        +'<p class="head_background">'
	        +'<span class="inline"><font>*</font>账户名称：</span><span class="contact_information_name head_hide hide" id="accountName"></span><input type="text" name="accountName" class="contact_information_input" maxlength="50"/>'
	        +'<span class="inline"><font>*</font>账户号码：</span><span class="contact_information_body head_hide hide" id="bankAccountCode"></span><input type="text" name="bankAccountCode" class="contact_information_input" maxlength="37"/> '
	        +'<input type="hidden" name="bankId" value=""><input type="hidden" name="defaultFlag" value="">'
	        +'<span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, \''+ctx+'\')">设为默认</span>'
	        +'<span class="default hide" id="defaultAccount">默认账户</span>'
	        +'<span class="float_right ">'
	        +'<span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>'
	        +'</span>'
	        +'<span class="float_right hide delete_save">'
	        +'<span class="delete" onclick="delete_agent_bank(this, \''+ctx+'\')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save" onclick="save_agent_bank(this, \''+ctx+'\');"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile hide" onclick="editor_agent_bank(this);"><em class="t1_2 compile_t1_2"></em>编辑</span>'
	        +'</span>'
	        +'</p>'
	        +'<div class="">'
	        +'<table class="channel_information_table">'
	        +'<tr>'
	        +'<td width="98"><font>*</font>开户行名称：</td>'
	        +'<td width="400">'
	        +'<span class="hide" id="bankName">'
	        +'</span>'
	        +'<input type="text" name="bankName" maxlength="50"/>'
	        +'</td>'
	        +'<td width="95"></td>'
	        +'<td width="360">'
	        +'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'<td>开户行地址：</td>'
	        +'<td>'
	        +'<span class="hide" id="bankAddr" style="display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 500px;">'
	        +'</span>'
	        +'<input type="text" name="bankAddr" />'
	        +'</td>'
	        +'<td></td>'
	        +'<td>'
	        +'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'<td>描述：</td>'
	        +'<td colspan="3">'
	        +'<span class="hide" id="bankRemark" style="display: inline-block;word-break: break-all"></span>'
	        +'<textarea name="bankRemark" cols="30" rows="10" class="contact_information_text "></textarea>'
	        +'</td>'
	        +'</tr>'
	        +'</table>'
	        +'</div>'
	        +'</div>';
    // 到此为止
    var div = '<div class="contact_information">'
        + '<div class="dl-select small-dl new_absolute type_select" style="display: block">'
        + '<input style="width: 90px;" value="银行卡" type="text" name="accountType">'
        + '<ul style="width: 102px; display: none;" class="select-option">'
        + '<li type="123">银行卡</li>'
        + '<li type="456">支付宝</li>'
        + '<li type="789">微信</li>'
        + '</ul>'
        + '</div>'
        + '<p class="head_background">'
        + '<span class="inline"><font>*</font>账户类型：</span><span class="contact_information_name head_hide hide" id="accountType" ></span><span class="accountTyleClass" style="display:inline-block;"></span>'
        + '<span class="bank_account"><span class="inline accountHeadSpan"><font>*</font>账户名称：</span><span class="contact_information_name head_hide hide" id="accountName" ></span><input type="text" name="accountName" class="contact_information_input" maxlength="50"/></span>'
        + '<span class="alipay_account hide"><span class="inline accountHeadSpan"><font>*</font><span class="accountChange">支付宝</span>账户：</span><span class="contact_information_name head_hide hide" id="alipayNumber" ></span><input type="text" name="alipayNumber" class="contact_information_input" maxlength="50"/></span>'
        + '<input type="hidden" name="bankId" value="">'
        // +'<input type="hidden" name="defaultFlag" value="">'
        // + '<span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, \'' + ctx + '\')">设为默认</span>'
        // + '<span class="default hide" id="defaultAccount">默认账户</span>'
        + '<span class="float_right ">'
        + '<span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>'
        + '</span>'
        + '<span class="float_right hide delete_save">'
        + '<span class="delete" onclick="delete_agent_bank(this, \'' + ctx + '\')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save" onclick="save_agent_bank(this, \'' + ctx + '\');"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile hide" onclick="editor_agent_bank(this);"><em class="t1_2 compile_t1_2"></em>编辑</span>'
        + '</span>'
        + '</p>'
        + '<div class="">'
        + '<table class="channel_information_table bank_table">'
        + '<tr>'
        + '<td width="165" class="overflow"><font>*</font>账户号码：</td>'
        + '<td width="400">'
        + '<span id="bankAccountCode" class="overflow hide"></span>'
        + '<input name="bankAccountCode" type="text" maxlength="50"/>'
        + '</td>'
        + '<td  width="165"></td>'
        + '<td  width="360">'
        + '</td>'
        + '</tr>'
        + '<tr>'
        + '<td width="98"><font>*</font>开户行名称：</td>'
        + '<td width="400">'
        + '<span class="hide" id="bankName">'
        + '</span>'
        + '<input type="text" name="bankName" maxlength="50"/>'
        + '</td>'
        + '<td width="95"></td>'
        + '<td width="360">'
        + '</td>'
        + '</tr>'
        + '<tr>'
        + '<td>开户行地址：</td>'
        + '<td>'
        + '<span class="hide" id="bankAddr" style="display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 500px;">'
        + '</span>'
        + '<input type="text" name="bankAddr" />'
        + '</td>'
        + '<td></td>'
        + '<td>'
        + '</td>'
        + '</tr>'
        + '<tr>'
        + '<td>描述：</td>'
        + '<td colspan="3">'
        + '<span class="hide" id="bankRemark" style="display: inline-block;word-break: break-all;"></span>'
        + '<textarea name="bankRemark" cols="30" rows="10" class="contact_information_text "></textarea>'
        + '</td>'
        + '</tr>'
        + '</table>'
        + '<table class="channel_information_table alipay_table hide">'
        + '<tr>'
        + '<td width="165" class="overflow"><font>*</font><span class="accountChange">支付宝</span>姓名：</td>'
        + '<td width="400">'
        + '<span id="alipay" class="overflow hide"></span>'
        + '<input name="alipay" type="text" maxlength="50"/>'
        + '</td>'
        + '<td width="165"></td>'
        + '<td width="360"></td>'
        + '</tr>'
        + '<tr>'
        + '<td>描述：</td>'
        + '<td colspan="3">'
        + '<span id="alipayRemark" class="hide" style="display: inline-block;word-break: break-all"></span>'
        + '<textarea name="alipayRemark" cols="30" rows="10" class="contact_information_text"></textarea>'
        + '</td>'
        + '</tr>'
        + '</table>'
        + '</div>'
        + '</div>';
    var p = $("#contact_information>p");

    	p.after(div);

    // modfiy by huochangying at 2016.10.14 for 538
    var _newOption = $(".dl-select input").eq(0);
    _newOption.bind("click", function (event) {
        var event = event || window.event;
        event.stopPropagation();
        $(this).parent().children("ul").toggle();
    });
    _newOption.next().find("li").each(function () {
        $(this).bind("click", function () {
            var value = $(this).text();
            $(this).parent().hide();
            $(this).parent().parent().children("input").val(value);
            var useDiv = $(this).parent().parent().parent();
            if (value == "银行卡") {
                useDiv.find(".bank_table").show();
                useDiv.find(".bank_account").show();
                useDiv.find(".alipay_table").hide();
                useDiv.find(".alipay_account").hide();
            } else {
                useDiv.find(".bank_table").hide();
                useDiv.find(".bank_account").hide();
                useDiv.find(".alipay_table").show();
                useDiv.find(".alipay_account").show();
                if (value == "支付宝") {
                    useDiv.find(".accountChange").eq(0).text("支付宝");
                    useDiv.find(".accountChange").eq(1).text("支付宝");
                } else {
                    useDiv.find(".accountChange").eq(0).text("微信");
                    useDiv.find(".accountChange").eq(1).text("微信");
                }
            }
        });
    });
    // modfiy by huochangying at 2016.10.14 for 538
    var partten = /^[\u4e00-\u9fa5][A-Za-z0-9]+$/;
    $(document).ready(function () {
        $('input[name=bankName]').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^a-z0-9\u4e00-\u9fa5]+/gi, '');
                $(this).val(b)
            }
        });

    });
    var partten = /^\d+$/;
    $(document).ready(function () {
        $('input[name=bankAccountCode]').keyup(function () {
            if (!partten.test($(this).val())) {
                var a = $(this).val();
                var b = a.replace(/[^\d]+/gi, '');
                $(this).val(b);
            }
        });
    });

}

/**
 * 保存银行账户信息
 * @param obj   当前对象
 * @param ctx   系统路径
 */
function save_agent_bank(obj, ctx) {
    var $this = $(obj);
    var p = $this.parent().parent();   //账户名词所在的区块
    var pd = $this.parent().parent().next();   //开户行名词所在的区块
    var pp = $this.parent().parent().prev();   //账户类型选择区块
    var accountType = pp.find('input[name="accountType"]').val();        //银行账户类型
    var defaultFlag = p.find('input[name="defaultFlag"]').val() || "1"; //银行ID
    var bank = {};  //银行信息
    var bankId = p.find('input[name="bankId"]').val() || '';  //是否是默认账户
    var bankJson;
    var accountName;
    var bankAccountCode;
    var bankName;
    var bankAddr;
    var bankRemark;

    if (pp.find("input").val() == "银行卡") {
        accountName = p.find('input[name="accountName"]').val();        //银行账户名称
       bankAccountCode = pd.find('input[name=bankAccountCode]').val();  //银行账户号码
        bankName = pd.find('input[name="bankName"]').val();             //开户行名称
        bankAddr = pd.find('input[name="bankAddr"]').val();             //开户行地址
         bankRemark = pd.find('textarea[name="bankRemark"]').val();      //描述
        //校验
        if (!accountType) {
            alert('请选择账户类型');
            return false;
        }
        if (!accountName) {
            alert('请填写账户名称');
            return false;
        }
        if (!bankAccountCode) {
            alert('请填写账户号码');
            return false;
        }
        if (!bankName) {
            alert('请填写开户行名称');
            return false;
        }
        if (accountName.length > 51) {
            alert('账户名称最多只能输入51个字符');
            return false;
        }
        if (bankAccountCode.length > 38) {
            alert('账户号码最多只能输入38个字符');
            return false;
        }
        if (bankName.length > 51) {
            alert('开户行名称最多只能输入51个字符');
            return false;
        }

        //对象赋值
        bank.accountPayType=3;
        bank.id = bankId;
        bank.accountType = accountType;
        bank.accountName = accountName;
        bank.defaultFlag = defaultFlag;
        bank.bankAccountCode = bankAccountCode;
        bank.bankName = bankName;
        bank.bankAddr = bankAddr;
        bank.remarks = bankRemark;

        //显示区域赋值
        p.find('input[name="bankId"]').val(bankId);
        p.find('input[name="defaultFlag"]').val(defaultFlag);
        p.find('#accountType').text(accountType);
        p.find('#accountType').attr("title", accountType);
        p.find('#accountName').text(accountName);
        p.find('#accountName').attr("title", accountName);
        pd.find('#bankAccountCode').text(bankAccountCode);
        pd.find('#bankAccountCode').attr("title", bankAccountCode);
        pd.find('#bankName').text(bankName);
        pd.find('#bankAddr').text(bankAddr);
        pd.find('#bankRemark').text(bankRemark);
        pd.find("#bankAddr").attr('title', bankAddr);
        // pd.find("#bankRemark").attr("title", bankRemark);

        bankJson = JSON.stringify(bank);
        $.ajax({
            type: "POST",
            url: ctx + "/person/info/saveAgentBank",
            data: {bankJson: bankJson},
            dataType: "json",
            success: function (result) {
                if (!result.flag) {
                    alert("保存银行账户信息失败，" + result.msg);
                } else {
                    var msg = result.msg;
                    var bankId = msg.split(",")[0];
                    var defaultFlag = msg.split(",")[1] || "";
                    p.find('input[name="bankId"]').val(bankId);
                    p.find('input[name="defaultFlag"]').val(defaultFlag);
                    top.$.jBox.tip("保存成功", "success", {top: '0'});
                    $(".jbox").css("position", "fixed");
                    $(".jbox").css("top", "0");
                }
            },
            error:function(){
                top.$.jBox.tip("请求失败", "error", {top: '0'});
            }
        });
    } else if(pp.find("input").val() == "支付宝"|| pp.find("input").val() == "微信"){
        var alipayNumber = p.find('input[name="alipayNumber"]').val();        //支付宝微信账户
        var alipayName = pd.find('input[name="alipay"]').val();  //支付宝微信姓名
        var alipayRemark = pd.find('textarea[name="alipayRemark"]').val();      //描述
        //校验
        if (!accountType) {
            alert('请选择账户类型');
            return false;
        }
        if (!alipayNumber) {
            alert('请填写账户');
            return false;
        }
        if (!alipayName) {
            alert('请填写姓名');
            return false;
        }
        if (alipayNumber.length > 20) {
            alert('账户最多只能输入20个字符');
            return false;
        }
        if (alipayName.length > 20) {
            alert('姓名最多只能输入20个字符');
            return false;
        }

        //对象赋值
        if (pp.find("input").val() == "支付宝") {
            bank.accountPayType = 2;
        } else {
            bank.accountPayType = 1;
        }
        bank.id = bankId;
        bank.defaultFlag = defaultFlag;
        bank.accountType = accountType;
        bank.bankAccountCode = alipayNumber;
        bank.accountName = alipayName;
        bank.remarks = alipayRemark;

        //显示区域赋值
        p.find('input[name="bankId"]').val(bankId);
        p.find('input[name="defaultFlag"]').val(defaultFlag);
        p.find('#accountType').text(accountType);
        p.find('#accountType').attr("title", accountType);
        p.find('#alipayNumber').text(alipayNumber);
        p.find('#alipayNumber').attr("title", alipayNumber);
        pd.find('#alipay').text(alipayName);
        pd.find('#alipay').attr("title", alipayName);
        pd.find('#alipayRemark').text(alipayRemark);
        pd.find("#alipayRemark").attr('title', alipayRemark);

        bankJson = JSON.stringify(bank);
        $.ajax({
            type: "POST",
            url: ctx + "/person/info/saveAgentBank",
            data: {bankJson: bankJson},
            dataType: "json",
            success: function (result) {
                if (!result.flag) {
                    alert("保存账户信息失败，" + result.msg);
                } else {
                    var msg = result.msg;
                    var bankId = msg.split(",")[0];
                    var defaultFlag = msg.split(",")[1] || "";
                    p.find('input[name="bankId"]').val(bankId);
                    p.find('input[name="defaultFlag"]').val(defaultFlag);
                    top.$.jBox.tip("保存成功", "success", {top: '0'});
                    $(".jbox").css("position", "fixed");
                    $(".jbox").css("top", "0");
                }
            },
            error:function(){
                top.$.jBox.tip("请求失败", "error", {top: '0'});
            }
        });
    }else{

         accountName = p.find('input[name="accountName"]').val();        //银行账户名称
         bankAccountCode = p.find('input[name=bankAccountCode]').val();  //银行账户号码
         bankName = pd.find('input[name="bankName"]').val();             //开户行名称
         bankAddr = pd.find('input[name="bankAddr"]').val();             //开户行地址
         bankRemark = pd.find('textarea[name="bankRemark"]').val();      //描述
        //校验
        if(!accountName){
            alert('请填写账户名称');
            return false;
        }
        if(!bankAccountCode){
            alert('请填写账户号码');
            return false;
        }
        if(!bankName){
            alert('请填写开户行名称');
            return false;
        }
        if(accountName.length > 51){
            alert('账户名称最多只能输入51个字符');
            return false;
        }
        if(bankAccountCode.length > 38){
            alert('账户号码最多只能输入38个字符');
            return false;
        }
        if(bankName.length > 51){
            alert('开户行名称最多只能输入51个字符');
            return false;
        }

        //对象赋值
        bank.id = bankId;
        bank.accountName = accountName;
        bank.defaultFlag = defaultFlag;
        bank.bankAccountCode = bankAccountCode;
        bank.bankName = bankName;
        bank.bankAddr = bankAddr;
        bank.remarks = bankRemark;

        //显示区域赋值
        p.find('input[name="bankId"]').val(bankId);
        p.find('input[name="defaultFlag"]').val(defaultFlag);
        p.find('#accountName').text(accountName);
        p.find('#accountName').attr("title",accountName);
        p.find('#bankAccountCode').text(bankAccountCode);
        p.find('#bankAccountCode').attr("title",bankAccountCode);
        pd.find('#bankName').text(bankName);
        pd.find('#bankAddr').text(bankAddr);
        pd.find('#bankRemark').text(bankRemark);
        pd.find("#bankAddr").attr('title',bankAddr);
        pd.find("#bankRemark").attr("title",bankRemark);

        bankJson = JSON.stringify(bank);
        $.ajax({
            type:"POST",
            url:ctx + "/person/info/saveAgentBank",
            data:{bankJson : bankJson},
            dataType:"json",
            success:function(result){
                if(!result.flag){
                    alert("保存银行账户信息失败，" + result.msg);
                }else{
                    var msg = result.msg;
                    var bankId = msg.split(",")[0];
                    var defaultFlag = msg.split(",")[1] || "";
                    p.find('input[name="bankId"]').val(bankId);
                    p.find('input[name="defaultFlag"]').val(defaultFlag);
                    top.$.jBox.tip("保存成功","success",{top:'0'});
                    $(".jbox").css("position", "fixed");
                    $(".jbox").css("top", "0");
                }
            }
        });
        p.find(".head_hide").show().css("display","inline-block");
        if(defaultFlag == "1"){
            p.find("#setDefaultAccount").show();
        }else if(defaultFlag == "0"){
            p.find("#defaultAccount").show();
        }
    }
    p.find(".head_hide").show().css("display", "inline-block");
    if (defaultFlag == "1") {
        p.find("#setDefaultAccount").show();
    } else if (defaultFlag == "0") {
        p.find("#defaultAccount").show();
    }
    p.find("input").hide();
    pd.find("input,textarea").hide();
    pd.find("span").show();
    p.find(".accountTyleClass").hide();
    pp.hide();
    $this.hide();
    $this.siblings().show();
}

/**
 * 删除渠道银行账号信息
 * @param obj
 * @returns {boolean}
 */
function delete_agent_bank(obj, ctx) {
    var $this = $(obj);
    var bankId = $this.parent().parent().find('input[name="bankId"]').val();
    if (!bankId) {
        var $bank = $this.parent().parent().parent();
        $bank.remove();
        return false;
    } else {
        $.jBox.confirm("您确认删除此账户信息吗？", "系统提示", function (v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: ctx + "/person/info/deleteAgentBank",
                    data: {id: bankId},
                    dataType: "json",
                    success: function (result) {
                        if (!result.flag) {
                            alert("删除账户信息失败，" + result.msg);
                        } else {
                            var $bank = $this.parent().parent().parent();
                            $bank.remove();
                            return false;
                        }
                    }
                });
            }
        });
    }
}

/**
 * 设置此银行账号为该渠道的默认账户
 * @param obj
 */
function agent_bank_default(obj, ctx) {
    var $this = $(obj);
    var bankId = $this.siblings('input[name="bankId"]').val();
    if (!bankId) {
        alert("当前银行账户信息还未保存，请先保存");
        return false;
    }
    $.ajax({
        type: "POST",
        url: ctx + "/person/info/setDefaultAgentBank",
        data: {id: bankId},
        dataType: "json",
        success: function (result) {
            if (!result.flag) {
                alert("设置默认账户失败")
            } else {
                $this.siblings('input[name="defaultFlag"]').val("0");
                $this.parents('.contact_information').siblings('.contact_information').each(function () {
                    var $defaultObj = $(this).children('p').children('#defaultAccount');
                    if (!$defaultObj.is(':hidden')) {
                        $defaultObj.replaceWith('<span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, \'' + ctx + '\')">设为默认</span>');
                    }
                });
                //解决新增时，点击设为默认，然后又点击其他设为默认时，新增的银行，显示两个设为默认
                $this.next('.hide').replaceWith('<span class="setDefault" id="setDefaultAccount" onclick="agent_bank_default(this, \'' + ctx + '\')">设为默认</span>');
                $this.replaceWith('<span class="default" id="defaultAccount">默认账户</span>');
                top.$.jBox.tip("设置成功", "success", {top: '0'});
                $(".jbox").css("position", "fixed");
                $(".jbox").css("top", "0");
            }
        }
    });
}

/**
 * 编辑渠道银行账户
 * @param obj
 */
function editor_agent_bank(obj) {
    var $this = $(obj);
    $this.hide();
    $this.siblings().show();
    var p = $this.parent().parent();
    p.find('.head_hide').hide();
    p.find('input').show();
    p.find('.accountTyleClass').css("display", "inline-block");
    p.find('#defaultAccount').hide();
    p.find('#setDefaultAccount').show();
    var div = p.next();
    div.slideDown();
    div.find("span").hide();
    div.find(".accountChange").show();
    div.find("input,textarea").show();

    if(div.parent().children().length==3){
        var prev = p.prev();
    prev.show();
    //给input输入框赋值
    var bankId = p.find('input[name="bankId"]').val();
    p.find('input[name="bankId"]').val(bankId);
    //默认银行账户
    var defaultFlag = p.find('input[name="defaultFlag"]').val();
    p.find('input[name="defaultFlag"]').val(defaultFlag);
    //银行账户类型
    var accountType = prev.find('#accountType').text();
    p.find('input[name="accountType"]').val(accountType);
    if (prev.find("input").val() == "银行卡") {
        //银行账户
        var accountNumber = p.find('#accountName').text();
        p.find('input[name="accountName"]').val(accountNumber);
        //银行账号
        var bankAccountCode = div.find('#bankAccountCode').text();
        div.find('input[name="bankAccountCode"]').val(bankAccountCode);
        //开户行名称
        var bankName = div.find('#bankName').text();
        div.find('input[name="bankName"]').val(bankName);
        //开户行地址
        var bankAddress = div.find('#bankAddr').text();
        div.find('input[name="bankAddr"]').val(bankAddress);
        //联系人描述
        var bankRemark = div.find('#bankRemark').text();
        div.find('textarea[name="bankRemark"]').val(bankRemark);
    }else{
    	//支付宝微信姓名
        var bankAddress = p.find('#alipayNumber').text();
        p.find('input[name="alipayNumber"]').val(bankAddress);
        //支付宝微信姓名
        var bankAddress = div.find('#alipay').text();
        div.find('input[name="alipay"]').val(bankAddress);
        //支付宝微信描述
        var bankRemark = div.find('#alipayRemark').text();
        div.find('textarea[name="alipayRemark"]').val(bankRemark);
    }
    }else{
        //给input输入框赋值
        var bankId = p.find('input[name="bankId"]').val();
        p.find('input[name="bankId"]').val(bankId);
        //默认银行账户
        var defaultFlag = p.find('input[name="defaultFlag"]').val();
        p.find('input[name="defaultFlag"]').val(defaultFlag);
        //银行账户
        var accountName = p.find('#accountName').text();
        p.find('input[name="accountName"]').val(accountName);
        //银行账号
        var bankAccountCode = p.find('#bankAccountCode').text();
        p.find('input[name="bankAccountCode"]').val(bankAccountCode);
        //开户行名称
        var bankName = div.find('#bankName').text();
        div.find('input[name="bankName"]').val(bankName);
        //开户行地址
        var bankAddress = div.find('#bankAddr').text();
        div.find('input[name="bankAddr"]').val(bankAddress);
        //联系人描述
        var bankRemark = div.find('#bankRemark').text();
        div.find('textarea[name="bankRemark"]').val(bankRemark);
    }
}

/***********************渠道资质信息**********************************************/
/**
 * 保存渠道资质信息
 * @param obj
 * @param ctx
 * @param type A:营业执照，B:经营许可证，C:税务登记证，D:组织结构代码，E：公司法人身份证，F:公司银行开户许可证，G:旅游业资质，H:其他资质
 * @author shijun.liu
 * @date 2016.06.20
 */
function save_agent_qualication(obj, ctx, type) {
    var $this = $(obj);
    var docId = $this.siblings('.agent_qual_id').val();
    var docName = $this.siblings('.agent_qual_name').val();
    $.ajax({
        type: "POST",
        url: ctx + "/person/info/saveAgentQualification",
        data: {docId: docId, type: type},
        dataType: "json",
        success: function (result) {
            var par = $this.parent().parent();
            if (!result.flag) {
                //进度条变红
                par.find('.progress-bar').css('width', '190px')
                    .removeClass('background_green')
                    .addClass('background_red');
                //小图标有绿色变为红色
                par.find('.up_success_false').children()
                    .removeClass('up_success')
                    .addClass('up_false');
                alert("设置资质信息失败，" + result.msg)
            } else {
                $this.parents('.channel_brand').find(".qual_show").text(docName);
                $this.parents('.channel_brand').children('p').children("em").show();
                $this.parents('.channel_brand').find("img").first().attr('src', ctx + '/person/info/getLogo?id=' + docId);
                par.next().show().find('span').text(docName);
                par.next().find('input').val(docId);
                par.hide();
                top.$.jBox.tip("保存成功", "success", {top: '0'});
                $(".jbox").css("position", "fixed");
                $(".jbox").css("top", "0");
            }
        }
    });
}

/**
 * 下载资质信息
 * @param obj
 * @param ctx
 */
function download_qualication(obj, ctx) {
    var $this = $(obj);
    var docId = $this.parents('.channel_brand').find('.upload_success_show').children('input').val();
    if (docId) {
        window.open(ctx + "/sys/docinfo/download/" + docId);
    }
}

/**
 * 删除资质信息
 * @param obj
 * @param ctx
 */
function delete_qualication(obj, ctx, type) {
    var $this = $(obj);
    var docId = $this.parents('.channel_brand').find('.upload_success_show').children('input').val();
    if (docId) {
        $.jBox.confirm("确认要删除吗？", "系统提示", function (v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: ctx + "/person/info/deleteQualification",
                    data: {docId: docId, type: type},
                    dataType: "json",
                    success: function (result) {
                        var par = $this.parent().parent();
                        if (!result.flag) {
                            alert('删除失败');
                            return false;
                        } else {
                            $this.parents('.channel_brand').find('.qual_show').html('&nbsp;')
                            $this.parents('.channel_brand').children('p').children('em').hide();
                            $this.parent().hide();
                            top.$.jBox.tip("删除成功", "success", {top: '0'});
                            $(".jbox").css("position", "fixed");
                            $(".jbox").css("top", "0");
                        }
                    }
                });
            }
        });
    }
}

/**
 * 删除图片上传框
 * @param obj
 */
function delete_upload_img(obj) {
    var $this = $(obj);
    $this.parent().parent().hide();
}

/**
 * 公司地址，所属地区国家，省，市联动
 * @param obj
 * @param ctx
 */
function chg(obj, ctx) {
    var select = $(obj);
    var parentId = $(obj).val();
    $.ajax({
        type: "post",
        url: ctx + "/person/info/getChildData",
        data: {
            parentId: parentId
        },
        success: function (result) {
            if (select.attr("name") == "belongsArea") {
                $("#belongsAreaProvince").html("");
                var c = $('<option value="-1">省（直辖市）</option>');
                $("#belongsAreaProvince").append(c);
                $.each(result, function (i, a) {
                    var option = $('<option value="' + a.id + '">' + a.name + '</option>');
                    $("#belongsAreaProvince").append(option);
                });
            } else if (select.attr("name") == "belongsAreaProvince") {
                $("#belongsAreaCity").html("");
                var c = $('<option value="-1">市（区）</option>');
                $("#belongsAreaCity").append(c);
                $.each(result, function (i, a) {
                    var option = $('<option value="' + a.id + '">' + a.name + '</option>');
                    $("#belongsAreaCity").append(option);
                });
            } else if (select.attr("name") == "agentAddress") {
                $("#agentAddressProvince").html("");
                var c = $('<option value="-1">省（直辖市）</option>');
                $("#agentAddressProvince").append(c);
                $.each(result, function (i, a) {
                    var option = $('<option value="' + a.id + '">' + a.name + '</option>');
                    $("#agentAddressProvince").append(option);
                });
            } else {
                $("#agentAddressCity").html("");
                var c = $('<option value="-1">市（区）</option>');
                $("#agentAddressCity").append(c);
                $.each(result, function (i, a) {
                    var option = $('<option value="' + a.id + '">' + a.name + '</option>');
                    $("#agentAddressCity").append(option);
                });
            }
        }
    })
}

function saveInfo(name, obj, ctx) {
    if (name == 'agentNameEn') {
        var en_name = $(obj).prev().val();
        if (!en_name || !$.trim(en_name)) {
            top.$.jBox.tip("请填写英文名称", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
    }
    if (name == 'belongsArea') {
        if ($("#belongsArea").val() == -1) {
            top.$.jBox.tip("请选择国家", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
    }
    if (name == 'agentAddress') {
        if ($("#agentAddress").val() == -1) {
            top.$.jBox.tip("请选择国家", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
    }
    if (name == "phone") {
        if ($("#phone1").val() == "") {
            top.$.jBox.tip("请填写区号", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
        if ($("#phone2").val() == "") {
            top.$.jBox.tip("请填写电话号码", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
    }
    if (name == "fax") {
        if ($("#fax1").val() == "") {
            top.$.jBox.tip("请填写区号", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
        if ($("#fax2").val() == "") {
            top.$.jBox.tip("请填写传真号码", "info", {top: '0'});
            $(".jbox").css("position", "fixed");
            $(".jbox").css("top", "0");
            return;
        }
    }
    $.ajax({
        type: "post",
        url: ctx + "/person/info/updateInfo",
        data: $(obj).parent().serialize(),
        success: function (result) {
            if (result) {
                top.$.jBox.tip("保存成功", "success", {top: '0'});
                $(".jbox").css("position", "fixed");
                $(".jbox").css("top", "0");
                $(obj).parent().parent().hide();
                $(obj).parent().parent().prev().find("a").last().hide();
                $(obj).parent().parent().prev().find("a").first().show();
                $(".channel_brand").css("background", "none");
                $(obj).parent().parent().prev().find("a").find(".copyReader").show();
                $(obj).parent().parent().prev().find("span").last().attr("class", "redact redact_use");

            } else {
                top.$.jBox.tip("保存失败", "error", {top: '0'});
                $(".jbox").css("position", "fixed");
                $(".jbox").css("top", "0");
            }
        }
    })
}
//基本信息电话，传真，qq等校验只能输入数字
var partten = /^\d+$/;
$(document).ready(function () {
    $("body").click(function(){
        var target = getEventSrc();
        if(target.parentNode.className != "select-option"){
            $(".select-option").hide();
        }
    });
    $('#phone1').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
    $('#phone2').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
    $('#fax1').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
    $('#fax2').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
    $('input[name="contactQQ"]').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
//    解除手机号输入数字限制
//    $('input[name="contactMobile"]').keyup(function () {
//        if (!partten.test($(this).val())) {
//            var a = $(this).val();
//            var b = a.replace(/[^\d]+/gi, '');
//            $(this).val(b);
//        }
//    });
    //需求调整，座机号码允许输入20位,解除输入数字限制 ymx Start
//     $('input[name="contactPhoneCode"]').keyup(function () {
//         if (!partten.test($(this).val())) {
//             var a = $(this).val();
//             var b = a.replace(/[^\d]+/gi, '');
//             $(this).val(b);
//         }
//     });
//     $('input[name="contactPhone"]').keyup(function () {
//         if (!partten.test($(this).val())) {
//             var a = $(this).val();
//             var b = a.replace(/[^\d]+/gi, '');
//             $(this).val(b);
//         }
//     });
    //需求调整，座机号码允许输入20位,解除输入数字限制 ymx End

    $('input[name="contactFaxCode"]').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
    $('input[name="contactFax"]').keyup(function () {
        if (!partten.test($(this).val())) {
            var a = $(this).val();
            var b = a.replace(/[^\d]+/gi, '');
            $(this).val(b);
        }
    });
    // modfiy by huochangying at 2016.10.17 for 538

    // modfiy by huochangying at 2016.10.17 for 538
});
function goHomePage(ctx) {
    //location.href = ctx + "/activity/manager/homepagelist";
    //modify by wlj at 2016.11.24 for huiteng-start
    var domain=window.location.host;
    if(domain.indexOf("huitengguoji.com")!=-1||domain.indexOf("travel.jsjbt")!=-1) {
        location.href = ctx + "/t1/jumpParam";
    }else{
        location.href = ctx + "/t1/newHome";
    }
    //modify by wlj at 2016.11.24 for huiteng-end
}

