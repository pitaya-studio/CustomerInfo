/**
 * Created by changying huo on 2016/11/23.
 * 产品和团期详情展示页面相关JS
 */
var ctx, groupId, activityId, ctxStatic, askGroupId;
$(document).ready(function () {
    ctx = $("#ctx").val();
    ctxStatic = $("#ctxStatic").val();
    var searchDetail = JSON.parse(sessionStorage.getItem("searchDetail"));//当前点击的产品或者团期的相关ID
    activityId = searchDetail.activityId;
    groupId = searchDetail.groupId;

    // 订单追踪v2.0.1 modefied by ruiqi.zhang 2017-3-16 16:47:30
    // linkShow();
    getAllData(ctx, activityId, groupId);
    click_lR(".order_bounced_body_left", ".order_bounced_body_right", ".order_bounced_body", ".order_contact", 2,"t_left","t_right");
    click_lR(".link_491_left", ".link_491_right", ".link_491", ".pop_contact", 2,"b_left","b_right");
    Details_header();
    moneyPeople();
    // sellerInfoBtnClick();
});
// 联系供应商展开收起
/*function linkShow() {
    $(".link_phone").bind("click", function () {
        var link_phone_i = $(".link_phone i");
        if (link_phone_i.is(".show")) {
            link_phone_i.removeClass("show").addClass("hide");
            $(".popup-top").hide();
        } else {
            link_phone_i.removeClass("hide").addClass("show");
            $(".popup-top").show();
            askGroupId=$(".border_orange").attr("data-Id");
            addAsk(askGroupId);
        }
    });
}*/

function addAsk(groupId,_salerId) {
	$.ajax({
        type : "post",
        url : ctx + "/orderProgressTracking/manage/save?dom=" + Math.random(),
        data : {
            groupId : groupId,
            orderType : 1,
            salerId : _salerId
        }
    });
}

function Details_header() {
    $(".pop_permission").live("mouseenter", function (event) {
        $(".pop_permission_show").show();
    });

    $(".pop_permission").live("mouseleave", function (event) {
        //当鼠标移到下面详细信息上时，不关闭信息窗口
        $(".pop_permission_show").hover(function () {
            $(".pop_permission_show").show();
        }, function () {
            $(".pop_permission_show").hide();
        });
        $(".pop_permission_show").hide();
    });
}
/**
 * 获取详情页面 初始化的所有数据
 * @param activityId  产品ID
 * @param groupId   团期ID
 */
function getAllData(ctx, activityId, groupId) {
    $.ajax({
        type: "POST",
        url: ctx + "/t1/activity/calendar/getActivityGroupInfo",
        cache: false,
        dataType: "json",
        async: false,
        data: {"activityId": activityId, "groupId": groupId},
        success: function (data) {
            // console.log(data)
            var groupIdUse = groupId;
            getTransDetail(data.transDetail);
            getDistributorLeft(data.officeInfo, ctx);
            getDistributorRight(data.contacts, ctx, ctxStatic);
            getproductInfo(data.activityFiles, data.officeInfo, data.productInfo, ctx, data.contacts);
            //日历
            var startToEnd = data.startToEnd;
            displayProAndGroup(startToEnd.startMonth.year, startToEnd.endMonth.year, data.groupSelected.month, data.groupDate);
            if(groupIdUse==null||groupIdUse==""){
                groupIdUse=$(".border_orange ").attr("data-Id");
            }
            chooseSalerOrder(data.contacts, groupIdUse, ctx, data.t1SalerRebate, ctxStatic);

        }, error: function () {
        }
    })
}
/**
 * 获取除底部销售模块以外的数据 初始化的所有数据 此方法用于解决：点击团期时，不刷新底部的销售信息
 * @param activityId  产品ID
 * @param groupId   团期ID
 * @author created by ruiqi.zhang 2017-3-22 18:43:22
 */
function getExceptRightBottom(ctx, activityId, groupId) {
    $.ajax({
        type: "POST",
        url: ctx + "/t1/activity/calendar/getActivityGroupInfo",
        cache: false,
        dataType: "json",
        async: false,
        data: {"activityId": activityId, "groupId": groupId},
        success: function (data) {
            // console.log(data)
            var groupIdUse = groupId;
            getTransDetail(data.transDetail);
            // getDistributorLeft(data.officeInfo, ctx);
            // getDistributorRight(data.contacts, ctx, ctxStatic);
            getproductInfo(data.activityFiles, data.officeInfo, data.productInfo, ctx, data.contacts);
            //日历
            var startToEnd = data.startToEnd;
            displayProAndGroup(startToEnd.startMonth.year, startToEnd.endMonth.year, data.groupSelected.month, data.groupDate);
            if(groupIdUse==null||groupIdUse==""){
                groupIdUse=$(".border_orange ").attr("data-Id");
            }
            chooseSalerOrder(data.contacts, groupIdUse, ctx, data.t1SalerRebate, ctxStatic);

        }, error: function () {
        }
    })
}
/**
 * 左右标签切换函数
 * @param left          id
 * @param right         id
 * @param body_parent 父容器
 * @param bodyChild  子个数
 * @param length    展示个数
 * left_hover        左侧点击按钮
 * right_hover        右侧点击按钮
 */
function click_lR(left, right, body_parent, bodyChild, length, left_hover, right_hover) {
    if($(bodyChild).length <= length){
        $(left).removeClass(left_hover);
        $(right).removeClass(right_hover);
    }else{
        var click_lRuse = 0;
        $(left).removeClass(left_hover);
        $(left).bind("click", function () {
            if (click_lRuse > 1) {
                $(bodyChild).eq(--click_lRuse).show();
                $(right).addClass(right_hover);
            }else if(click_lRuse == 1){
                $(bodyChild).eq(--click_lRuse).show();
                $(left).removeClass(left_hover);
                $(right).addClass(right_hover);
            }
        });
        $(right).bind("click", function () {
            if (click_lRuse < $(body_parent).children().length - length - 1) {
                $(bodyChild).eq(click_lRuse++).hide();
                $(left).addClass(left_hover);
            }else if(click_lRuse == $(body_parent).children().length - length - 1){
                $(bodyChild).eq(click_lRuse++).hide();
                $(right).removeClass(right_hover);
                $(left).addClass(left_hover);
            }
        });
    }

}
//详情弹窗计算功能
function count() {
    //定义变量flag 为true时 ，可以点击计算按钮
    var flagPeoNo = false;//人数是否通过标志
    var flagPrice = false;//金额是否通过标志
    var flagAll = true;
    var adult_price = $("#adult_price").text().replace(/,/,"");//系统结算价
    var adult_money = $("#adult_money").val();//实际结算价
    var adult = $("#adult").val();//人数
    var child_price = $("#child_price").text().replace(/,/,"");
    var child_money = $("#child_money").val();
    var child = $("#child").val();
    var special_price = $("#special_price").text().replace(/,/,"");
    var special_money = $("#special_money").val();
    var special = $("#special").val();
    /**
     * 538需求与产品协议，进行了判定修改
     * @type {*[]}
     */
    var people = [
        {"whoNo": adult, "whom": "成人", "people_price": adult_price, "people_money": adult_money},
        {"whoNo": child, "whom": "儿童", "people_price": child_price, "people_money": child_money},
        {"whoNo": special, "whom": "特殊人群", "people_price": special_price, "people_money": special_money}
    ];
    for (var _i = 0; _i < people.length; _i++) {
        var peo = people[_i];
        if (Object.prototype.toString.call(peo) != "[object Object]") {
            continue;
        }
        if (peo["people_price"] == "" && peo["people_money"] != "") {
            flagPrice = false;
            $.jBox.tip(peo["whom"] + '无系统结算价，请重新填写', 'error', {focusId: "top"});
            return false;
        }
        if (peo["people_money"] != "" || peo["whoNo"] != "") {
            //价格个人数都没填写
            flagAll = false;
        }
        if (peo["people_money"] != "" && (peo["whoNo"] == "" || peo["whoNo"] == 0)) {
            $.jBox.tip('请填写正确的' + peo["whom"] + '人数', 'error');
            return false;
        }
        if (peo["people_money"] == "" && peo["whoNo"] != "") {
            $.jBox.tip('请填写' + peo["whom"] + '实际结算金额', 'error');
            return false;
        }
    }
    if (flagAll) {
        $.jBox.tip('请输入数值及人数', 'error');
        return false;
    }
    /**
     * end
     * @type {*[]}
     */
    var adult_last = parseFloat(Number((adult_money - adult_price) * adult).toFixed(2));
    var child_last = parseFloat(Number((child_money - child_price) * child).toFixed(2));
    var special_last = parseFloat(Number((special_money - special_price) * special).toFixed(2));
    var money_1 = $(".money_1").text();
    var money_2 = $(".money_2").text();
    var money_3 = $(".money_3").text();
    var money_head1 = $(".money_head1");
    var money_head2 = $(".money_head2");
    var money_head3 = $(".money_head3");
    var all_last1 = $(".all_last1");
    var all_last2 = $(".all_last2");
    var all_last3 = $(".all_last3");
    //为下单准备提交数据
    initInputDetail("1", adult_money, adult, child_money, child, special_money, special);
    //如果金额为空，人数为空，则不允许下单，给出提示即可
    if (!adult_last && !child_last && !special_last) {
        isconfirmToordered = false;
    } else {
        isconfirmToordered = true;
    }


    //最终利润
    var alllast = "";
    $("#adult_last").text(adult_last);//利润显示  成人
    $("#child_last").text(child_last);
    $("#special_last").text(special_last);
    var currencyCode = [money_1, money_2, money_3];
    var currency = [adult_last, child_last, special_last]
    var total = {};
    for (var i = 0; i < currencyCode.length; i++) {
        //如果存在
        if (total[currencyCode[i]]) {
            total[currencyCode[i]] = parseFloat((Number(currency[i]) + Number(total[currencyCode[i]])).toFixed(2));
        } else {
            //如果不存在
            if (!currency[i]) {
            } else {
                total[currencyCode[i]] = Number(currency[i]);
            }
        }
    }
    var _flag = true;
    for (var x in total) {
        if (total[x].toString().length >= 5) {
            _flag = false;
        }
    }
    for (var x in total) {
        alllast += '<span class="money_head1">' + x + '</span>'
//        if(total[x].toString().length>=5){
        if (!_flag) {
            alllast += '<span class="orange font_44 all_last1" id="all_last" style="font-size: 18px;">' + total[x] + '</span>';
        } else {
            alllast += '<span class="orange font_44 all_last1" id="all_last" style="font-size: 26px;">' + total[x] + '</span>';
        }
    }
    if ($(".multi_currency").find("span").length > 0) {
        $(".multi_currency").empty();
        alllast = "利润：" + alllast;
    }
    alllast = alllast.substring(0, alllast.length - 1);
//        alllast.slice(0,-1);
    $(".multi_currency").append(alllast);
    $(".profit_count_one").hide();
    $(".profit_count_two").show();
    if ($(".count_parent").eq(1).children().length == 2) {
        $(".count_again").addClass("count_gray");
    } else {
        $(".count_again").removeClass("count_gray");
    }
}
function return_count() {
    $("#order").hide();
    $(".profit_count_two").show();
}
function count_again() {
    $(".profit_count_one input").val("");
    $(".profit_count_one").show();
    $(".profit_count_two").hide();
}
/**
 * 取自于t1home.js,为了保存原先的输入数据
 */
//首页计算的时候输入的数据
var inputDetail = {
    "adultPrice": "",
    "adultNum": "",
    "childPrice": "",
    "childNum": "",
    "specialPrice": "",
    "specialNum": "",
    "groupId": "",
    "orderPersonNum": "",
    "payId": "",
    "remark": "测试数据"
};
var isconfirmToordered = false;
function initInputDetail(type, adult_money, adult, child_money, child, special_money, special) {
    if (type != "1") {
        inputDetail = {
            "adultPrice": "",
            "adultNum": "",
            "childPrice": "",
            "childNum": "",
            "specialPrice": "",
            "specialNum": "",
            "groupId": "",
            "orderPersonNum": "",
        };
    } else {
        inputDetail.adultPrice = adult_money;
        inputDetail.adultNum = adult;
        inputDetail.childPrice = child_money;
        inputDetail.childNum = child;
        inputDetail.specialPrice = special_money;
        inputDetail.specialNum = special;
        inputDetail.orderPersonNum = parseInt(adult == "" ? 0 : adult) + parseInt(child == "" ? 0 : child) + parseInt(special == "" ? 0 : special);
    }
}
function order_this() {
    $("#order").show();
    $(".profit_count_two").hide();
}
/**
 *详情页头部信息
 */
function getproductInfo(activityFiles, officeInfo, productInfo, ctx, contact) {
    var businessCertificates = officeInfo.businessCertificate;
    var businessLicenses = officeInfo.businessLicense;
    var cooperationProtocols = officeInfo.cooperationProtocol;
    var activityFilesIds = '';
    if (activityFiles && activityFiles.length > 0) {
        for (var n = 0; n < activityFiles.length; n++) {
            activityFilesIds += activityFiles[n].docId + ',';
        }
    }
    var htmlstr = $(".Details_header");
    htmlstr.html('');
    var _insertHtml = '<div class="big_head">';
    _insertHtml += '<h2 class="h2_491" title="' + productInfo.productName + '">' + productInfo.productName + '</h2>';
    _insertHtml += '<div class="relative download_float_right fontsize_14" onclick="showDownLoad(\'' + activityFilesIds + '\',this);">';
    _insertHtml += '<i class="fa fa-paperclip" aria-hidden="true" style="margin-left:9px;"></i> 行程单附件<em class="trangle-top"></em>';

    if (activityFiles && activityFiles.length > 0) {
        _insertHtml += '<div class="travel-attach"><a class="batch-attach" onclick="downloads(\'' + activityFilesIds + '\',\'' + ctx + '\')">';
        _insertHtml += '<i class="fa fa-download" aria-hidden="true"></i>下载全部</a><ul>';
        for (var k = 0; k < activityFiles.length; k++) {
            var activityFilesdocIds = activityFiles[k].docId;
            var activityFilesdocName = activityFiles[k].docName;
            _insertHtml += '<li><span>' + activityFilesdocName + '<a  onclick="downloadFile(\'' + activityFilesdocIds + '\',\'' + ctx + '\')">下载</a></span></li>';
        }
        _insertHtml += '</ul></div>';
    } else {
        _insertHtml += '<div class="travel-attach">暂无数据</div>';
    }
    _insertHtml += '</div></div>';
    _insertHtml += '<div class="relative">';
    _insertHtml += '<div class="pop_link">';
    _insertHtml += '<dl><dt>团号：</dt>';
    _insertHtml += '<dd title="' + productInfo.groupCode + '">' + productInfo.groupCode + '</dd></dl>';
    _insertHtml += '<dl><dt>出发城市：</dt>';
    _insertHtml += '<dd>' + productInfo.fromArea + '</dd></dl>';
    _insertHtml += '<dl><dt>行程天数：</dt>';
    if (productInfo.activityDuration == "") {
        _insertHtml += '<dd></dl></dd>';
    } else {
        _insertHtml += '<dd>' + productInfo.activityDuration + '天</dl></dd>';

    }
    _insertHtml += '<dl><dt>交通工具：</dt>';
    _insertHtml += '<dd>' + productInfo.trafficMode + '</dd></dl>';

    var domain=window.location.host;
    if(domain.indexOf("huitengguoji.com")!=-1||domain.indexOf("travel.jsjbt")!=-1) {

    }else{
        //认证详情开始的地方
        _insertHtml += '<dl><dt><a class="pop_permission" href="' + ctx + '/wholesalers/certification/officeDetail?companyId=' + officeInfo.officeId + '" target="_blank"><em></em>认证详情</a>';
        _insertHtml += '<div class="pop_permission_show">';
        _insertHtml += '<em class="pop_per_angle"></em>';
        _insertHtml += '<div class="pop_per_content">';
        _insertHtml += '<span>该批发商已经通过认证</span>';
        _insertHtml += '<a href="' + ctx + '/wholesalers/certification/officeDetail?companyId=' + officeInfo.officeId + '" target="_blank">认证详情 <iclass="fa fa-angle-double-right"></i></a>';
        _insertHtml += '<ul class="auth_icon">';
        if (businessCertificates && businessCertificates.length > 0) {
            _insertHtml += '<li onclick="showName(\'certificate\')"><a><em class="real_name"></em><br>资质证书</a></li>';
        } else {
            _insertHtml += '<li><a class="not_click_a"><em class="real_name_none"></em><br>资质证书</a></li>';
        }
        if (businessLicenses && businessLicenses.length > 0) {
            _insertHtml += '<li onclick="showName(\'license\')"><a ><em class="business_licen"></em><br>营业执照</a></li>';
        } else {
            _insertHtml += '<li><a class="not_click_a"><em class="business_licen_none"></em><br>营业执照</a></li>';
        }
        if (cooperationProtocols && cooperationProtocols.length > 0) {
            _insertHtml += '<li onclick="showName(\'protocol\')" class="auth_icon_lastli"><a><em class="coop_deal"></em><br>合作协议</a></li>';
        } else {
            _insertHtml += '<li class="auth_icon_lastli"> <a class="not_click_a"><em class="coop_deal_none"></em><br>合作协议</a></li>';
        }
        _insertHtml += '</ul></div></div>';
    }
    //认证详情结束的地方


    _insertHtml += '<div class="htmleaf-container">';
    _insertHtml += '<div class="docs-galley">';
    _insertHtml += '<ul class="docs-pictures certificate clearfix" style="display:none">';
    // 资质证书图片

    if (businessCertificates && businessCertificates.length > 0) {
        for (var b = 0; b < businessCertificates.length; b++) {
            var businessCertificatesdocId = businessCertificates[b].docId;
            var businessCertificatesdocName = businessCertificates[b].docName;
            _insertHtml += '<li><div ><img data-original="' + ctx + '/person/info/getLogo?id=' + businessCertificatesdocId + '" alt="' + businessCertificatesdocName + '" src="' + ctx + '/person/info/getLogo?id=' + businessCertificatesdocId + '" class="viewer-toggle"></div></li>';
        }
    }
    _insertHtml += '</ul><ul class="docs-pictures license clearfix" style="display:none">';
    // 营业执照图片
    if (businessLicenses && businessLicenses.length > 0) {
        for (var l = 0; l < businessLicenses.length; l++) {
            var businessLicensesdocId = businessLicenses[l].docId;
            var businessLicensesdocName = businessLicenses[l].docName;
            _insertHtml += '<li><div ><img data-original="' + ctx + '/person/info/getLogo?id=' + businessLicensesdocId + '" alt="' + businessLicensesdocName + '" src="' + ctx + '/person/info/getLogo?id=' + businessLicensesdocId + '" class="viewer-toggle"></div></li>';
        }
    }
    _insertHtml += '</ul>';
    _insertHtml += '<ul class="docs-pictures protocol clearfix" style="display:none">';
    // 合作协议图片
    if (cooperationProtocols && cooperationProtocols.length > 0) {
        for (var m = 0; m < cooperationProtocols.length; m++) {
            var cooperationProtocolsdocId = cooperationProtocols[m].docId;
            var cooperationProtocolsdocName = cooperationProtocols[m].docName;
            _insertHtml += '<li><div><img data-original="' + ctx + '/person/info/getLogo?id=' + cooperationProtocolsdocId + '" alt="' + cooperationProtocolsdocName + '" src="' + ctx + '/person/info/getLogo?id=' + cooperationProtocolsdocId + '" class="viewer-toggle"></div></li>';
        }
    }
    _insertHtml += '</ul></div></div></dt></dl></div></div>';
    _insertHtml += '<div class="htmleaf-container" id="link_491">';
    _insertHtml += '<div class="docs-galley">';
    _insertHtml += '<ul class="docs-pictures  clearfix forSaleOrder"  style="display:none">';
    for (var j = 0; j < contact.length; j++) {
        if (contact[j].salerCardId) {
            _insertHtml += '<li><div><img data-imgId="' + contact[j].salerCardId + '" data-original="' + ctx + '/person/info/getLogo?id=' + contact[j].salerCardId + '" alt="' + contact[j].salerName + '" src="' + ctx + '/person/info/getLogo?id=' + contact[j].salerCardId + '"></div></li>';
        }
    }
    _insertHtml += '</ul></div></div>';
    htmlstr.append(_insertHtml);
    if ($(".certificate").children().length == 0 && $(".license").children().length == 0 && $(".protocol").children().length == 0) {
        $(".pop_permission").hide();
    } else {
        $(".pop_permission").show();
    }
    /*动态引入js*/
    $("body #use_img_491").empty();
    var contextPath=$("#contextPath").val();
    $("#use_img_491").append('<script type="text/javascript" src="' + contextPath + '/static/js/picView/main.js"></script>');
    $("#use_img_491").append('<script type="text/javascript" src="' + contextPath +'/static/js/picView/viewer.js"></script>');
}
/**
 * 右侧价格列表
 */
function getTransDetail(transDetail) {
    var tbody = $("#tbody_491");
    var tr_de = transDetail;
    var people = [['zkAdult', "thAdult", "adult"], ["zkChild", "thChild", "child"], ["zkSpecial", "thSpecial", "special"]];
    tbody.empty();
    for (var i = 0; i < people.length; i++) {
        var peo = people[i];
        var _priceHtml = '';
        _priceHtml += '<tr class="firsttr">';
        _priceHtml += '<td class="td_first">' + (peo[2] == "adult" ? '成人' : (peo[2] == "child" ? "儿童" : "特殊人群")) + '</td>';
        _priceHtml += TransDetailUse1(tr_de[peo[0] + "CurrencyMark"], tr_de[peo[0] + "Price"]);
        _priceHtml += TransDetailUse1(tr_de[peo[1] + "CurrencyMark"], tr_de[peo[1] + "Price"]);
        _priceHtml += '<td class="tr">';
        _priceHtml += TransDetailUse2(tr_de[peo[2] + "CurrencyMark"], tr_de[peo[2] + "Price"], peo[2] + "_price", "money_" + (i + 1));
        tbody.append(_priceHtml);
    }
}
function TransDetailUse1(currency, money) {
    var html = "";
    if (money == "--") {
        html += '<td class="tr"></td>';
    } else {
        html += '<td class="tr">' + currency + money + '/人</td>';
    }
    return html;
}
function TransDetailUse2(currency, money, iduse, classuse) {
    var html = "";
    if (money == "--") {
        html += '<span class="' + classuse + '"></span><span class="orange orange-price money_use" id="' + iduse + '" style="font-size: 18px;"></span></td></tr>';
    } else {
        html += '<span class="' + classuse + '">' + currency + '</span><span class="orange orange-price money_use" id="' + iduse + '" style="font-size: 18px;">' + money + '</span>/人 </td></tr>';
    }
    return html;
}
/**
 * 底部供应商左侧信息
 */
function getDistributorLeft(officeInfo, ctx) {
    var businessCertificates = officeInfo.businessCertificate;
    var businessLicenses = officeInfo.businessLicense;
    var cooperationProtocols = officeInfo.cooperationProtocol;
    var bodyuse = $(".new_top_pop");
    bodyuse.empty();
    var dtbHtml = '';
    dtbHtml += '<div class="po_img_title">';
    if (officeInfo.docInfoId == null || officeInfo.docInfoId == "") {
        dtbHtml += '<img src="' + ctxStatic + '/images/T1T2/no_logo.png" alt="暂无logo"/>';
    } else {
        dtbHtml += '<img src="' + ctx + '/person/info/getLogo?id=' + officeInfo.docInfoId + '" alt="logo">';
    }
    dtbHtml += '<span>' + officeInfo.name + '</span>';
    dtbHtml += '</div>';
    dtbHtml += '<p title="' + officeInfo.phone + '">投诉电话：' + officeInfo.phone + '</p>';
    if (officeInfo.webSite) {
        dtbHtml += '<p title="' + officeInfo.webSite + '">网址：&nbsp;' + officeInfo.webSite + '</p>';
    } else {
        dtbHtml += '<p title="">网址：&nbsp;</p>';
    }
    dtbHtml += '<ul class="auth_icon auth_icon_new">';
    if (businessCertificates && businessCertificates.length > 0) {
        dtbHtml += '<li onclick="showName(\'certificate\')"><a><em class="real_name"></em><br>资质证书</a></li>';
    } else {
        dtbHtml += '<li><a class="not_click_a"><em class="real_name_none"></em><br>资质证书</a></li>';
    }
    if (businessLicenses && businessLicenses.length > 0) {
        dtbHtml += '<li onclick="showName(\'license\')"><a><em class="business_licen"></em><br>营业执照</a></li>';
    } else {
        dtbHtml += '<li><a class="not_click_a"><em class="business_licen_none"></em><br>营业执照</a></li>';
    }
    if (cooperationProtocols && cooperationProtocols.length > 0) {
        dtbHtml += '<li onclick="showName(\'protocol\')" class="auth_icon_lastli"><a><em class="coop_deal"></em><br>合作协议</a></li>';
    } else {
        dtbHtml += '<li class="auth_icon_lastli"> <a class="not_click_a"><em class="coop_deal_none"></em><br>合作协议</a></li>';
    }
    dtbHtml += '</ul>';
    bodyuse.append(dtbHtml);

}
/*-----^_^-----订单追踪v2.0.1 modefied by ruiqi.zhang -----^_^-----start----^_^-----*/

/**
 * 底部供应商右侧信息
 * 订单追踪v2.0.1  2017-3-16 15:54:06
 * modefied by ruiqi.zhang
 */
function getDistributorRight(contacts, ctx, ctxStatic) {
    var contact = contacts;
    var bodyUse = $(".link_491");
    bodyUse.empty();
    var ctHtml = '';
    if (contact.length > 0) {
        for (var i = 0; i < contact.length; i++) {
            ctHtml += '<div class="pop_contact">';
            if (contact[i].salerPhotoId == null || contact[i].salerPhotoId == "") {
                ctHtml += '<div class="divOld"><img src="' + ctxStatic + '/images/T1T2/photo.png" alt="默认头像"/></div>';
            } else {
                ctHtml += '<div class="divOld"><img src="' + ctx + '/person/info/getLogo?id=' + contact[i].salerPhotoId + '" alt="' + contact[i].salerName + '"></div>';
            }
            ctHtml += '<ul>';
            if (contact[i].salerCardId == null || contact[i].salerCardId == "") {
                ctHtml += '<li class="pop_contact_name">' + contact[i].salerName + '</li>';
            } else {
                ctHtml += '<li class="pop_contact_name">' + contact[i].salerName + '<a style = "display: none;" onclick="showName(' + contact[i].salerCardId + ',\'forSaleOrder\',\'link_491\')" title="名片" ></a></li>';
            }
            ctHtml += '<li><label>职位：</label>销售</li>';
            ctHtml += '<div class = "seller_info"><li class="pop_phone_num"><label>手机：</label>' + contact[i].salerMobile  + '</li>';
            ctHtml += '<li><label>座机：</label>' + contact[i].salerPhone+ '</li>';
            if(contact[i].salerWechart==null||contact[i].salerWechart==""){
                ctHtml += '<li><label>微信：</label><span class="pop_e_mail"></span></li>';
            }else{
                ctHtml += '<li><label>微信：</label><span class="pop_e_mail white-space">' + contact[i].salerWechart + '</span></li>';
            }
            ctHtml += '<li><label>邮箱：</label><span class="pop_e_mail">' + contact[i].salerEmail + '</span></li>';
            ctHtml += '</div></ul><input type="hidden" value="' + contact[i].salerId + '"><div class = "contact_info_btn">联系此销售</div></div>';
        }
        bodyUse.append(ctHtml);
    }
    sellerInfoBtnClick();
}
/**
 * 联系销售按钮点击事件
 */
function sellerInfoBtnClick(){
    // $(".seller_info").off("click");
    //调用时隐藏销售信息
    $(".seller_info").hide();
    $(".contact_info_btn").on("click",function(){
        var _sellId = $(this).siblings("input").val();
        $(this).hide();
        // bug 17633 modefied by ruiqi.zhang 2017-3-23 18:05:20
        $(this).siblings("ul").children(".pop_contact_name").children("a").removeAttr("style");
        $(this).siblings("ul").children(".seller_info").show();
        addAsk(groupId,_sellId);
    })
}

/*function getDistributorRight(contacts, ctx, ctxStatic) {
    var contact = contacts;
    var bodyUse = $(".link_491");
    bodyUse.empty();
    var ctHtml = '';
    if (contact.length > 0) {
        for (var i = 0; i < contact.length; i++) {
            ctHtml += '<div class="pop_contact">';
            if (contact[i].salerPhotoId == null || contact[i].salerPhotoId == "") {
                ctHtml += '<div class="divOld"><img src="' + ctxStatic + '/images/T1T2/photo.png" alt="默认头像"/></div>';
            } else {
                ctHtml += '<div class="divOld"><img src="' + ctx + '/person/info/getLogo?id=' + contact[i].salerPhotoId + '" alt="' + contact[i].salerName + '"></div>';
            }
            ctHtml += '<ul>';
            if (contact[i].salerCardId == null || contact[i].salerCardId == "") {
                ctHtml += '<li class="pop_contact_name">' + contact[i].salerName + '</li>';
            } else {
                ctHtml += '<li class="pop_contact_name">' + contact[i].salerName + '<a onclick="showName(' + contact[i].salerCardId + ',\'forSaleOrder\',\'link_491\')" title="名片" ></a></li>';
            }
            ctHtml += '<li><label>职位：</label>销售</li>';
            ctHtml += '<li class="pop_phone_num"><label>手机：</label>' + contact[i].salerMobile  + '</li>';
            ctHtml += '<li><label>座机：</label>' + contact[i].salerPhone+ '</li>';
            if(contact[i].salerWechart==null||contact[i].salerWechart==""){
                ctHtml += '<li><label>微信：</label><span class="pop_e_mail"></span></li>';
            }else{
                ctHtml += '<li><label>微信：</label><span class="pop_e_mail white-space">' + contact[i].salerWechart + '</span></li>';
            }
            ctHtml += '<li><label>邮箱：</label><span class="pop_e_mail">' + contact[i].salerEmail + '</span></li>';
            ctHtml += '</ul></div>';
        }
        bodyUse.append(ctHtml);
    }
}*/

/*-----^_^-----订单追踪v2.0.1 modefied by ruiqi.zhang ------^_^----end----^_^-----*/
/**
 * 选择销售下单 视图拼接
 */
function chooseSalerOrder(contacts, groupId, ctx, t1SalerRebate, ctxStatic) {
    var groupIdUse = groupId;
    var bodyUse = $(".order_bounced_body");
    var contact = contacts;
    bodyUse.empty();
    var csoHtml = '';
    for (var i = 0; i < contact.length; i++) {
        csoHtml += '<div class="order_contact">';
        csoHtml += '<div class="ordering" onclick="placeOrder(\'' + groupIdUse + '\',\'' + contact[i].salerId + '\',\' ' + ctx + '\')">下单</div>';

        if (contact[i].salerPhotoId == null || contact[i].salerPhotoId == "") {
            csoHtml += '<img class="img_491" src="' + ctxStatic + '/images/T1T2/photo.png" alt="默认头像"/>';
        } else {
            csoHtml += '<img class="img_491" src="' + ctx + '/person/info/getLogo?id=' + contact[i].salerPhotoId + '" alt="' + contact[i].salerName + '">';
        }
        csoHtml += '<ul class="font_12 ul_491">';
        csoHtml += '<li class="order_contact_name"><span title="' + contact[i].salerName + '" class="order_contact_name_width">' + contact[i].salerName + '</span>';

        if (contact[i].salerCardId == null || contact[i].salerCardId == "") {
            csoHtml += '</li>';
        } else {
            csoHtml += '<a onclick="showName(\'' + contact[i].salerCardId + '\',\'forSaleOrder\',\'link_491\')"  title="名片"></a></li>';
        }
        csoHtml += ' <li><label>职位：</label>销售</li>';
        csoHtml += '<li class=""><label>手机：</label>' + contact[i].salerMobile+ '</li>';
        csoHtml += '<li><label>座机：</label>' + contact[i].salerPhone + '</li>';
        if(contact[i].salerWechart == null || contact[i].salerWechart == ""){
            csoHtml += '<li><label>微信：</label><span class="inline_flex_491"></span></li>';
        }else{
            csoHtml += '<li><label>微信：</label><span class="inline_flex_491">' + contact[i].salerWechart + '</span></li>';
        }
        csoHtml += '<li><label>邮箱：</label><span class="inline_flex_491">' + contact[i].salerEmail + '</span>';
        csoHtml += '</li></ul></div>';
    }
    bodyUse.append(csoHtml);
    if (t1SalerRebate == false) {
        $(".order").hide();
    } else if(t1SalerRebate == true){
        $(".order").show();
    }
}
/**
 * 产品详情弹窗内的下单操作
 * ${ctx}:接口路径
 * groupId：团期Id
 * salerUserId:被选中的销售下单人员Id
 */
function placeOrder(groupId, salerUserId, ctx) {
    orderJbox(groupId, salerUserId, ctx);
    fillOrderForm(groupId, salerUserId, ctx);
    barUse();
}
/**
 *解决chrome浏览器jbox点击title滚动条消失的问题
 */
function barUse() {
    window.parent.$pop.find(".jbox-title-panel").click(function () {
        if ($("#ord").css("height") == "659px") {
            $("#ord").css("height", "inherit");
        } else {
            $("#ord").css("height", "");
        }
    })
}
/**
 * 填充下单信息
 */
function fillOrderForm(groupId, salerUserId, ctx) {
    var _createOrderHtml = "";
    inputDetail.groupId = groupId;
    inputDetail.salerId = salerUserId;
    //正式时放开
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/t1OrderDetail",
        cache: false,
        dataType: "json",
        async: false,
        data: inputDetail,
        success: function (data) {
            var orderResult = data;
            // var orderResult=orderOBJ;

            //产品相关信息
            var _productInfo = orderResult.productInfo;
            for (var _key in _productInfo) {
                if (!_productInfo[_key]) {
                    _productInfo[_key] = "";
                }
            }
            _createOrderHtml += '<div class="product-info" title="' + _productInfo.productName + '">' + _productInfo.productName + '</div>';
            _createOrderHtml += '<div class="product-item">';
            _createOrderHtml += ' <span class="item-detail" title="' + _productInfo.groupCode + '">团号：' + _productInfo.groupCode + '</span>';
            _createOrderHtml += ' <span class="item-detail">出团日期：' + _productInfo.groupOpenDate + '</span>';
            _createOrderHtml += ' <span class="item-detail">出发城市：' + _productInfo.fromArea + '</span>';
            _createOrderHtml += ' <span class="item-detail">行程天数：' + _productInfo.activityDuration + '天</span>';
            _createOrderHtml += ' <span class="item-detail">交通工具：' + _productInfo.trafficMode + '</span>';
            _createOrderHtml += '</div>';
            //批发商相关信息
            var _contacts = orderResult.contacts;
            _createOrderHtml += '<label>联系人</label>';
            _createOrderHtml += '<div  class="contacts">';
            _createOrderHtml += '批发商：<span class="saler-name" title="' + _contacts.companyName + '">' + _contacts.companyName + '</span>';
            _createOrderHtml += '联系人：<div  class="dl-select small-dl" title="' + _contacts.salerName + '">' + _contacts.salerName + '</div>';
            _createOrderHtml += '电话：<span class="saler-name">' + _contacts.salerPhone + '</span></div>';
            //交易明细的相关部分
            var _transDetail = orderResult.transDetail;
            _createOrderHtml += '<label for="">交易明细</label>';
            _createOrderHtml += '<table class="sus-table"><thead>';
            _createOrderHtml += '<tr><th width="200"></th><th width="170" class="tr">成人</th><th width="170" class="tr">儿童</th><th width="170" class="tr">特殊人群</th></tr></thead>';
            _createOrderHtml += '<tbody>';
            _createOrderHtml += '<tr><td>实际结算价</td>';
            var _tempHtml = _transDetail.adultPrice == '--' ? '--' : _transDetail.adultCurrencyMark + _transDetail.adultPrice + '/人';
            _createOrderHtml += '<td>' + _tempHtml + '</td>';
            _tempHtml = _transDetail.childPrice == '--' ? '--' : _transDetail.childCurrencyMark + _transDetail.childPrice + '/人';
            _createOrderHtml += '<td>' + _tempHtml + '</td>';
            _tempHtml = _transDetail.specialPrice == '--' ? '--' : _transDetail.specialCurrencyMark + _transDetail.specialPrice + '/人';
            _createOrderHtml += '<td>' + _tempHtml + '</td>';
            _createOrderHtml += '</tr><tr>';
            _createOrderHtml += '<td>系统结算价</td>';
            var _tempHtml = _transDetail.companyAdultPrice == '--' ? '--' : _transDetail.companyAdultCurrencyMark + _transDetail.companyAdultPrice + '/人';
            _createOrderHtml += '<td>' + _tempHtml + '</td>';
            var _tempHtml = _transDetail.companyChildPrice == '--' ? '--' : _transDetail.companyChildCurrencyMark + _transDetail.companyChildPrice + '/人';
            _createOrderHtml += '<td>' + _tempHtml + '</td>';
            var _tempHtml = _transDetail.companySpecialPrice == '--' ? '--' : _transDetail.companySpecialCurrencyMark + _transDetail.companySpecialPrice + '/人';
            _createOrderHtml += '<td>' + _tempHtml + '</td>';
            _createOrderHtml += '</tr><tr>';
            _createOrderHtml += '<td>人数</td>';
            var _tempNum = _transDetail.adultNum == '--' ? '--' : (_transDetail.adultNum + '人')
            _createOrderHtml += '<td>' + _tempNum + '</td>';
            var _tempNum = _transDetail.childNum == '--' ? '--' : (_transDetail.childNum + '人')
            _createOrderHtml += '<td>' + _tempNum + '</td>';
            var _tempNum = _transDetail.specialNum == '--' ? '--' : (_transDetail.specialNum + '人')
            _createOrderHtml += '<td>' + _tempNum + '</td>';
            _createOrderHtml += '</tr><tr>';
            _createOrderHtml += '<td><div>小计</div><div class="susTdGray">(实际结算价×人数)</div></td>';
            var _tempMark = [_transDetail.adultCurrencyMark, _transDetail.childCurrencyMark, _transDetail.specialCurrencyMark];
            var _tempSum = [_transDetail.adultSum, _transDetail.childSum, _transDetail.specialSum];
            for (var i = 0; i < _tempMark.length; i++) {
                if (_tempSum[i] == '--') {
                    _createOrderHtml += '<td><span class="orange">--</span></td>';
                } else {
                    _createOrderHtml += '<td>' + _tempMark[i] + '<span class="orange">' + _tempSum[i] + '</span></td>';
                }

            }
            _createOrderHtml += '</tr><tr>';
            //这里的采用的是特殊人群的币种符号，具体需要看业务，待协商。
            _createOrderHtml += '<td  colspan="4"  class="summary">门店结算价差额返还总计：' + _transDetail.specialCurrencyMark + '<span>' + _transDetail.profitsSum + '</span></td></tr>';
            _createOrderHtml += '</tbody></table>'

            //备注
            var _remarks = orderResult.remarks;
            _createOrderHtml += '<label >备注</label>';
            _createOrderHtml += '<textarea  name="" id="orderDetailRemark"  maxlength="150" class="remark-order">' + _remarks + '</textarea>'
            _createOrderHtml += '<div  class="buttons">'
            _createOrderHtml += '<span  class="unable"  onclick="previousStep(1)">上一步</span><span  onclick="getPayMethod();">下一步</span></div>'
            $("#ord").empty().append(_createOrderHtml);
            //给下拉框绑定事件
            bindClick();
            //收款方式页面的拼接，本应该放在下一步操作中，但整体定义了一个接口，数据一起带过来了
            createPayHtml(orderResult.payInfo, ctx, salerUserId);
        },
        error: function (data) {
        }
    });
}
/**
 * 给下单页面的下拉框绑定下拉事件
 */
function bindClick() {
    /*点击下拉款*/
    $(".dl-select input").click(function () {
        $(this).parent().children("ul").toggle();
        var event = getE();
        if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
            event.cancelBubble = true;
        } else {
            event.stopPropagation();
        }
    });
    $(".dl-select ul li").each(function () {
        $(this).click(function () {
            var value = $(this).text();
            $(this).parent().hide();
            $(this).parent().parent().children("input").val(value);
        });
    });
}

/**
 * 每个支付对象，支持行选定
 */
function paymentChecked() {
    $("#pay-child").find("ul>li").bind("click", function (e) {
        var $target = getEventSrc();
        var _checkedLi = $($target);
        if (_checkedLi[0].nodeName == "LI") {
            _checkedLi.children().first().attr("checked", "checked");
        } else {
            _checkedLi.attr("checked", "checked");
            _checkedLi.siblings().attr("checked", "false");
        }

    })
}
/**
 * 下单弹窗
 * */
function orderJbox() {
    var html = "<div id='ord'></div><div id='pay' style='display: none;'></div>";
    $pop = $.jBox(html, {
        title: "下单信息",
        width: 880,
        height: 630,
        persistent: true,
        buttons: false,
        loaded: function () {
            if ($(window).height() < 800) {
                $("#jbox", window.parent.document).css("top", "0");
            }
        }
    });
}
/**
 * 下单详情页面的 下一步 按钮
 */
function getPayMethod() {
    inputDetail.remark = $("#orderDetailRemark").val();
    $("#ord").hide();
    /* "payId": "",
     "remark": "测试数据"*/
    //修改titl为收款方式
    window.parent.$pop.find(".jbox-title").html("收款方式");
    $('#bank').niceScroll({
        cursorcolor: "#ccc",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "5px", //像素光标的宽度
        cursorborder: "0", //     游标边框css定义
        cursorborderradius: "5px",//以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });

    $("#pay").show();
    initChecked();

}
function previousStep(which) {
    if (which == 1) {//下单页面的上一步操作
        // $("#ord").hide();
        // $("#order").hide();
        // count_again();//返回到计算首页
        order_this();//返回到上一步的操作
        window.parent.jBox.close();
    } else if (which == 2) {//收款页面的上一步操作
        $("#pay").hide();
        $("#ord").show();
        window.parent.$pop.find(".jbox-title").html("下单信息");
        $("#bank").getNiceScroll().resize();
    }
}
/**
 * 支付方式页切换标签
 * @param obj
 * @param which
 */
function payTabChange(obj, which) {
    $(obj).removeClass("active").addClass("active");
    $(obj).siblings().removeClass("active");
    $(obj).prev().addClass("borderRN");
    $(obj).prev().prev().removeClass("borderRN");
    $(obj).next().addClass("borderLN");
    $(obj).next().next().removeClass("borderLN");
    $(obj).removeClass("borderLN").removeClass("borderRN");
    if (which == 1) {
        cleanChecked("#bank", "#alipay", "#weChat");
    } else if (which == 2) {
        cleanChecked("#alipay", "#bank", "#weChat");
    } else if (which == 3) {
        cleanChecked("#weChat", "#alipay", "#bank");
    }
}

function cleanChecked(first, second, third) {
    $(first).show().find("input").first().attr("checked", "checked");
    $(second).hide().find("input").removeAttr("checked");
    $(third).hide().find("input").removeAttr("checked");

}
/**
 * 进入收款页面时初始化
 * 默认在第一个标签页，默认选中第一个账户
 */
function initChecked() {
    $("#pay ul.tabs").children().eq("0").addClass("active").nextAll().removeClass("active");
    $("#bank").show();
    $("#alipay").hide();
    $("#weChat").hide();
    $("#pay-child").find("li input[type='radio']").removeAttr("checked");
    $("#bank").find("input").first().attr("checked", "checked");
}
var _btncount = 1;
/**
 * 收款方式 提交
 */
function submitOrderDetail(ctx, salerId) {
    var abc = $("#pay-child").find("li input[type='radio']:checked").nextAll();
    abc.first().text();
    var payId = abc.first().attr("data-id");
    inputDetail.payId = payId;
    if (_btncount == 1) {
        _btncount++;
        $.ajax({
            type: "POST",
            url: ctx + "/t1/preOrder/manage/saveOrder",
            cache: false,
            async: false,
            data: inputDetail,
            success: function (data) {

                if (data.result == "success") {
                	$.ajax({
                        type : "post",
                        url : ctx + "/orderProgressTracking/manage/save?dom=" + Math.random(),
                        data : {
                            groupId: data.groupId,
                            orderType: 2,
                            preOrderId: data.preOrderId,
                            salerId: salerId
                        }
                    });
                    top.$.jBox.tip('保存成功', 'success', {
                        top: '0', timeout: 1500,
                        closed: function () {
                            _btncount = 1;
                            window.parent.location.href = ctx + "/t1/preOrder/manage/showT1OrderList";
                            window.parent.jBox.close();
                        } /* 提示关闭后执行的函数 */
                    });
                } else {
                    top.$.jBox.tip('保存失败', 'error', {top: '0'});
                    _btncount = 1;
                }
            },
            error: function () {
                top.$.jBox.tip('提交失败。', 'error', {top: '0'});
                _btncount = 1;
            }
        })
    } else if (_btncount > 1) {
        top.$.jBox.tip('请勿重复提交', 'error', {top: '0'});
    }
    /* event=getCommonEvent();
     if(event&&event.stopPropagation()){
     event.stopPropagation();
     }else{
     window.event.cancelBubble=true;//IE
     }*/
    // stopDefault();
}
/**
 * 拼接收款方式页面的html,并绑定相关事件
 */
function createPayHtml(payInfo, ctx, salerId) {
    var _payHtml = '';
    var _bankInfoList = payInfo.bankInfo;
    var _zfbInfoList = payInfo.zfbInfo;
    var _wxInfoList = payInfo.wxInfo;
    _payHtml += '<div  class="pay-tip">请选择您要收款的方式：</div>';
    _payHtml += '<div class="tab-container"><ul class="tabs">';
    _payHtml += '<li onclick="payTabChange(this,1)" class="active"><a href="#bank">银行卡</a></li>' +
        '<li onclick="payTabChange(this,2)" class=""><a href="#alipay">支付宝</a></li>' +
        '<li onclick="payTabChange(this,3)" class=""><a href="#weChat">微信</a></li></ul>';
    _payHtml += ' <div id="pay-child" class="tab_containers"  style="clear: both;">'
        + '<div id="bank" class="tab_content" style="display: block;">'
        + ' <ul  class="bank-list">';
    //收款方式可以不选，我屮艸芔茻
    _payHtml += '<li><input name="bankNo" type="radio"><span data-id=""  class="bank-info">无</span></li>'
    for (var i = 0; i < _bankInfoList.length; i++) {
        var _bankInfo = _bankInfoList[i];
        _payHtml += '<li><input name="bankNo" type="radio"><span data-id="' + _bankInfo.payId + '"  class="bank-info" data-type="' + _bankInfo.accountPayType + '"  title="' + _bankInfo.bankName + '">' + _bankInfo.bankName + '</span><span  class="card-info">尾号' + _bankInfo.bankCodeEnd.substring(_bankInfo.bankCodeEnd.length - 4, _bankInfo.bankCodeEnd.length) + '</span><span>' + _bankInfo.userName + '</span></li>'
    }
    _payHtml += '</ul></div>'
    //支付宝模块开始
    _payHtml += '<div id="alipay" class="tab_content" style="display: none;"><ul class="bank-list">';
    _payHtml += '<li><input name="alipayNo" type="radio"><span data-id=""  >无</span></li>'
    for (var i = 0; i < _zfbInfoList.length; i++) {
        var _zfbInfo = _zfbInfoList[i];
        _payHtml += '<li><input name="alipayNo" type="radio"><span data-id="' + _zfbInfo.payId + '" class="alipy_img"></span><span  class="card-info" title="' + _zfbInfo.accountCode + '" data-type="' + _zfbInfo.accountPayType + '">' + _zfbInfo.accountCode + '</span><span>' + _zfbInfo.userName + '</span></li>'
    }
    _payHtml += '</ul></div>'
    //微信模块开始
    _payHtml += '<div id="weChat" class="tab_content" style="display: none;"><ul class="bank-list">';
    _payHtml += '<li><input name="wechatNo" type="radio"><span data-id=""  >无</span></li>'
    for (var i = 0; i < _wxInfoList.length; i++) {
        var _wxInfo = _wxInfoList[i];
        _payHtml += '<li><input name="wechatNo" type="radio"><span data-id="' + _wxInfo.payId + '" class="wechat_img"></span><span  class="card-info" title="' + _wxInfo.accountCode + '" data-type="' + _wxInfo.accountPayType + '">' + _wxInfo.accountCode + '</span><span>' + _wxInfo.userName + '</span></li>'
    }
    _payHtml += '</ul></div>'
    _payHtml += '</div></div>';
    _payHtml += '<div  class="buttons">';
    _payHtml += '<span class="unable" onclick="previousStep(2)">上一步</span><span onclick="submitOrderDetail(\''+ ctx + '\','+ salerId +')">提交</span></div>';
    $("#pay").empty().append(_payHtml);
    paymentChecked();
}
/**
 * 显示名片 图集
 *
 * imgId 该函数的第一个参数，别被名字误解了
 * modify by wlj at 2016/11/3 for 气人
 */
function showName(imgId) {
    if (arguments[1] && arguments[1] == "forSaleOrder") {
        var parentDivId = arguments[2];
        $("#" + parentDivId + " .forSaleOrder").find("img").each(function () {
            if ($(this).attr("data-imgId") && $(this).attr("data-imgId") == imgId) {
                $(this).trigger("click");
            }
        })
    } else {
        $("." + imgId).eq(0).find("img").eq(0).trigger("click");
    }
}
/*显示现在行程单*/
var fadeFlag = true;
function showDownLoad(docIds, obj, event) {
    event = getCommonEvent();
    var target=getEventSrc();
    if (target != obj) {
        fadeFlag = true;
    }
    if (fadeFlag || fadeFlag == true) {
        if (null == docIds || '' == docIds || 'undefined' == docIds) { //判断文档id是否为空
            $(".travel-attach").html("暂无行程单附件！");
        }
        $(".trangle-top,.travel-attach").fadeIn();
        // $(".pos-popup").fadeOut();
        fadeFlag = false;
        flag = true;
    } else {
        $(".trangle-top,.travel-attach").fadeOut();
        fadeFlag = true;
    }
    if (window.event) {
        event.cancelBubble = true;//阻止冒泡
    } else {
        event.stopPropagation();
    }

}
// 行程单下载
function downloads(docIds, ctx) {
    if (null == docIds || '' == docIds || 'undefined' == docIds) { //判断文档id是否为空
        top.$.jBox.tip("没有行程单可供下载!");
        return false;
    }
    //将产品行程介绍打包下载
    window.open(ctx + "/sys/docinfo/zipdownload/" + docIds + "/introduction");
}

function downloadFile(docId, ctx) {
    window.open(ctx + "/sys/docinfo/download/" + docId);
}
// 日历点击事件
function dateSwitch(monthGroupId) {
    $(".date_top_center").empty();
    $(".dc_2").remove();
    // getAllData(ctx, activityId, monthGroupId);
    // added by ruiqi.zhang 刷新除了销售详细信息模块以外的页面数据
    getExceptRightBottom(ctx, activityId, monthGroupId)
    $(".profit_count_one").show();
    $(".profit_count_one").find("input").val("");
    $(".profit_count_two").hide();
    $("#order").hide();
}
/*点击空白处隐藏悬浮窗*/
$(document).click(function () {
    $(".trangle-top,.travel-attach").fadeOut();
    fadeFlag = true;
    flag = true;
});
// 金额人数
function moneyPeople() {
    if ($(".money_use").text().length > 5) {
        $(".money_use").css("font-size", "18px")
    }
    $('input[class=input_100]').keyup(function () {
        var $this = this;
        $this.value = $this.value.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
        $this.value = $this.value.replace(/^\./g, ""); //验证第一个字符是数字而不是
        $this.value = $this.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
        $this.value = $this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        $this.value = $this.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
    });
    $('input[class=input_45]').keyup(function () {
        var _value = $(this).val();
        _value = _value.replace(/[^\d]/g, "");
        _value = _value.replace(/([0-9]{4})[0-9]*/, "$1");
        $(this).val(_value)
    });
}