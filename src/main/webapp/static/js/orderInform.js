/**
 * Created by changying huo on 2016/10/13.
 */
$(function () {
    ctx = $("#ctx").val();
    getAgentinfos();
    getAgentContacts();
    getOrderStatusInfo();
    HtmlForReturnDetail();
    //展开、收起筛选
    launch();
});
//如果展开部分有查询条件的话，默认展开，否则收起
var ctx = "";
/**
 * 获取渠道名称数据
 */
function getAgentinfos() {
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/getSearchInfo",
        cache: false,
        dataType: "json",
        async: true,
        data: {},

        success: function (data) {
            var _productInfos = data.productInfo;
            var _agentifos=data.agentInfo;
            var htmlstr = $("select[name=channelName]");
            htmlstr.html('');
            var _insertHtml = "";
            _insertHtml += "<option value='' ></option>";
            var num = _agentifos.length;
            $("#channelName").comboboxInquiry();
            if (num > 0) {
                for (var i = 0; i < num; i++) {
                    var _agentifo = _agentifos[i];
                    _insertHtml += '<option value="' + _agentifo.agentId + '" >' + _agentifo.agentName + '</option>';
                }
                htmlstr.append(_insertHtml);
            }

        },
        error: function (e) {
            console.log("渠道名称请求失败");
        }
    })
};
/**
 * 获取登录账号
 */
function getAgentContacts() {
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/getSearchInfo",
        cache: false,
        dataType: "json",
        async: true,
        data: {},

        success: function (data) {
            var userInfos = data.userInfo;
            var htmlstr = $("select[name=loginNumber]");
            htmlstr.html('');
            var _insertHtml = "";
            _insertHtml += "<option value='' ></option>";
            var num = userInfos.length;
            if (num > 0) {
                for (var i = 0; i < num; i++) {
                    var userInfo = userInfos[i];
                    _insertHtml += ' <option value="' + userInfo.userId + '" >' + userInfo.loginName + '</option>';
                }
                htmlstr.append(_insertHtml);
            }
            $("#loginNumber").comboboxInquiry();
        },
        error: function (e) {
            console.log("登录账号请求失败");
        }
    })
};
/**
 * 获取下单状态
 */
function getOrderStatusInfo() {
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/getSearchInfo",
        cache: false,
        dataType: "json",
        async: true,
        data: {},

        success: function (data) {
            var orderStatusInfos = data.orderStatusInfo;
            var htmlstr = $("select[name=orderStatus]");
            htmlstr.html('');
            var _insertHtml = "";
            _insertHtml += "<option value='0' >全部</option>";
            _insertHtml += '<option value="1" >' + orderStatusInfos[1] + '</option>';
            _insertHtml += '<option value="2" >' + orderStatusInfos[2] + '</option>';
            _insertHtml += '<option value="3" >' + orderStatusInfos[3] + '</option>';
            _insertHtml += '<option value="4" >' + orderStatusInfos[4] + '</option>';
            htmlstr.append(_insertHtml);

        },
        error: function (e) {
            console.log("下单状态请求失败");
        }
    })
};
var inputDetail =
{
    "activityNameOrGroupCode": "", //搜索框的值
    "agentId": "",//渠道id
    "userId": "",//渠道联系人id
    "orderTimeBegin": "",//付款状态：0：未付款，1：已付款
    "orderTimeEnd": "",
    "orderStatus": "",
    "pageNo": "",
    "pageSize": ""
};
/**
 *  下单列表
 * @constructor
 */
function HtmlForReturnDetail(obj) {
    var orderStatusUse = obj;
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/showOrderList",
        cache: false,
        dataType: "json",
        async: true,
        data: inputDetail,
        beforeSend: function () {
            // $("#loading").show();
        },
        success: function (data) {
            var _listResult = data.orderInfo;
            var tbody = $("#tbody");
            //tbody.empty();
            var _htmlstr = $("#noResult");
             _htmlstr.nextAll().remove();
            if (_listResult.length > 0) {
                var _insertHtml = "";
                var _count = data.pageInfo.count;
                var _pageSize = data.pageInfo.pageSize;
                var _pageNo = data.pageInfo.pageNo;
                for (var i = 0, j = _listResult.length; i < j; i++) {
                    var _orderDetail = _listResult[i];
                    _insertHtml += '<tr data-id="' + _orderDetail.id + '">';
                    _insertHtml += '<td class="tc relative abcTd">';
                    if (_orderDetail.newOrderFlag === "1") {
                        _insertHtml += '<div class="arrow_new"></div>';
                    }
                    _insertHtml += '</td>';
                    _insertHtml += '<td><div class="pro-name">' + _orderDetail.orderNum + '</div></td>';
                    _insertHtml += '<td>';
                    _insertHtml += '<div class="pro-name">';
                    _insertHtml += '<div>' + _orderDetail.acitivityName + '</div>';
                    _insertHtml += '<div><span>团号:</span>' + _orderDetail.groupCode + '</div>';
                    _insertHtml += '<div><span>提交时间:</span>' + _orderDetail.orderTime + '</div>';
                    _insertHtml += '</div>';
                    _insertHtml += '</td>';
                    _insertHtml += '<td><div class="pro-name">' + _orderDetail.orderPersonNum + '</div></td>';
                    _insertHtml += '<td>';
                    _insertHtml += '<div class="pro-name">';
                    _insertHtml += '<div>' + _orderDetail.agentName + '</div>';
                    _insertHtml += '</div>';
                    _insertHtml += '</td>';
                    _insertHtml += '<td><div class="pro-name">' + _orderDetail.loginName + '</div></td>';
                    _insertHtml += '<td>';
                    _insertHtml += '<div class="pro-name">';
                    _insertHtml += '<div><span class="order-title">系统结算价:</span><span class="order-right">' + _orderDetail.companyMoney + '</span></div>';
                    _insertHtml += '<div><span class="order-title">价格总额:</span><span class="order-right">' + _orderDetail.totalMoney + '</span></div>';
                    _insertHtml += '<div><span class="order-title">门店结算价差额返还:</span><span class="order-right">' + _orderDetail.differenceMoney + '</span></div>';
                    _insertHtml += '</div>';
                    _insertHtml += '</td>';
                    _insertHtml += '<td class="tc">';
                    _insertHtml += '<div class="activeOrder">' + _orderDetail.orderStatus + '</div>';
                    _insertHtml += '<div class="processingOperations">';
                    switch (_orderDetail.orderStatus) {
                        case "待处理":

                            // 支付方式值
                            _insertHtml += '<input type="hidden" name="payMode" value="' + _orderDetail.payMode + '">';
                            _insertHtml += '<a href="javascript:void(0);" class="orderTo" onclick="validatePrice(this, ' + _orderDetail.salerId + ', ' + _orderDetail.id + ')">下单</a>';
                            _insertHtml += '<a href="javascript:void(0);" class="orderUndo" onclick="orderUndo(this)">取消</a>';
                            _insertHtml += '<a href="javascript:void(0);" class="orderDetails" onclick="orderDetails(this)">详情</a>';
                            break;
                        case "已下单":
                            _insertHtml += '<a href="javascript:void(0);" class="orderLook" onclick="orderLook(this)">查看订单</a>';
                            _insertHtml += '<a href="javascript:void(0);" class="orderDetails" onclick="orderDetails(this)">详情</a>';
                            break;
                        case "已取消":
                            _insertHtml += '<a href="javascript:void(0);" class="orderDel" onclick="orderDel(this)">删除</a>';
                            _insertHtml += '<a href="javascript:void(0);" class="orderDetails" onclick="orderDetails(this)">详情</a>';
                            break;
                        case "已删除":
                            _insertHtml += '<a href="javascript:void(0);" class="orderDetails" onclick="orderDetails(this)">详情</a>';
                            break;
                    }
                    _insertHtml += '</div>';
                    _insertHtml += '</td>';
                    _insertHtml += '</tr>';
                    _insertHtml += '<tr class="tr_child height_30">';
                    _insertHtml += '<td class="paddingL order_remark" colspan="8">';
                    _insertHtml += '<div class="overflowE"><span style="white-space:nowrap">备注：</span>' + _orderDetail.remark + '</div>';
                    _insertHtml += '</td>';
                    _insertHtml += '</tr>';
                }
                $("#noResult").hide();
                 _htmlstr.after(_insertHtml);
                // tbody.append(_insertHtml);
                var tlength = tbody.children().length - 1;
                var child_this = tbody.children().eq(tlength);
                var orderStatusUseChild = child_this.prev().find(".activeOrder").text();
                if (orderStatusUse != 4 && orderStatusUseChild == "已删除") {
                    child_this.hide();
                    child_this.prev().hide();
                }
                var _webVersion = "t2";
                var _pageTest = {
                    count: _count,
                    pageSize: _pageSize,
                    pageNo: _pageNo
                };

                var _pageHtml = doPage(_pageTest, _webVersion);
                $(".pagination").empty().append(_pageHtml);
                remarkEffect();
            } else {
                $(".pagination").empty();
                $("#noResult").show();
            }
        },
        error: function (e) {
            console.log("请求失败");
            $("#noResult").show();
        },
        complete: function () {
            // $("#loading").hide();
        }
    })
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
        $(selectOption[0]).nextAll().removeAttr("selected")
    }
}

function queryMarginInCondition() {
    inputDetail.activityNameOrGroupCode = $("#groupCode").val();
    inputDetail.agentId = $("#channelName").val();
    inputDetail.userId = $("#loginNumber").val();
    inputDetail.orderTimeBegin = $("#orderTimeBegin").val();
    inputDetail.orderTimeEnd = $("#orderTimeEnd").val();
    inputDetail.orderStatus = $("#orderStatus").val();
    var orderStatusUse = $("#orderStatus").val();
    HtmlForReturnDetail(orderStatusUse);
}
/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(page, pageSize) {
    inputDetail.pageNo = page;
    inputDetail.pageSize = pageSize;
    HtmlForReturnDetail();
}
// 备注效果
function remarkEffect() {
    $("#ProTab tbody tr").hover(function () {
        if ($(this).next().hasClass("tr_child")) {
            $(this).next().addClass("tr_hover");
        }
    }, function () {
        $(this).next().removeClass("tr_hover");
    });
    $("#ProTab tbody tr.tr_child").hover(function () {
        $(this).prev().addClass("tr_hover");
    }, function () {
        $(this).prev().removeClass("tr_hover");
    });
    $("#ProTab tbody tr.tr_child").prev().css("border-bottom", "none");
}

// 下单
function orderTo() {

}
// 取消
var orderIdSend = {
    "orderId": ""
};
function orderUndo(obj) {
    var tr = $(obj).parent().parent().parent();
    orderIdSend.orderId = tr.attr("data-id");
    changeSeenFlag(obj, orderIdSend.orderId);
    var submit = function (v, h, f) {
        if (v == 'ok') {
            $.ajax({
                type: "POST",
                url: ctx + "/t1/preOrder/manage/cancleOrder",
                cache: false,
                dataType: "json",
                async: true,
                data: {"orderId":orderIdSend.orderId},
                success: function (data) {
                    var result = data.result;
                    var msg = "取消成功";

                    if (result == "success") {
                        jBox.tip(msg, 'success');
                        // var _insertHtml="";
                        // _insertHtml += '<a href="javascript:void(0);" class="orderDel" onclick="orderDel(this)">删除</a>';
                        // _insertHtml += '<a href="javascript:void(0);" class="orderDetails" onclick="orderDetails(this)">详情</a>';
                        // $(obj).parent().prev().text("已取消");
                        // $(obj).parent().empty().append(_insertHtml);
                        HtmlForReturnDetail();
                    }
                    else {
                        jBox.tip(msg, 'error');
                    }
                    return true;


                },
                error: function (e) {
                    console.log("请求失败");
                }
            })
        }else if (v == 'cancel') {
            // 取消
        }

        return true; //close
    }
    $.jBox.confirm("确定要取消吗？", "提示", submit);
}
// 查看订单
function orderLook(obj) {
	var tr = $(obj).parent().parent().parent();
    var preOrderId = tr.attr("data-id");
    
	$.ajax({
		async : false,
		type : "POST",
		data: {"preOrderId" : preOrderId},
		url : ctx + "/orderCommon/manage/getOrderIdByPreOrderId",
		success : function (msg) {
			if (msg.orderId) {
				window.open(ctx + "/orderCommon/manage/orderDetail/" + msg.orderId, "_blank");
			}
		}
	});
}
// 删除
function orderDel(obj) {
    var tr = $(obj).parent().parent().parent();
    orderIdSend.orderId = tr.attr("data-id");
    changeSeenFlag(obj, orderIdSend.orderId);
    var submit = function (v, h, f) {
        if (v == 'ok') {
        $.ajax({
            type: "POST",
            url: ctx + "/t1/preOrder/manage/deleteOrder",
            cache: false,
            dataType: "json",
            async: true,
            data: {"orderId":orderIdSend.orderId},
            success: function (data) {
                var result = data.result;
                var msg = "删除成功";

                if (result == "success") {
                    jBox.tip(msg, 'success');
                    // tr.remove();
                    // tr.next().remove();
                    HtmlForReturnDetail();
                }
                else {
                    jBox.tip(msg, 'error');
                }
                return true;
            },
            error: function (e) {
                console.log("请求失败");
            }
        });
        } else if (v == 'cancel') {
            // 取消
        }

        return true; //close
    };
    $.jBox.confirm("确定要删除吗？", "提示", submit);
}
// 详情
function orderDetails(obj) {
    var tr = $(obj).parent().parent().parent();
    orderIdSend.orderId = tr.attr("data-id");
    changeSeenFlag(obj, orderIdSend.orderId);
    window.location.href = ctx + "/t1/preOrder/manage/showT2OrderDetail";
    sessionStorage.setItem("orderId", $(obj).parent().parent().parent().attr("data-id"));
}

// 日期格式化

function getdate() {
    var mydate = new Date();
    var str = "" + mydate.getFullYear() + "-";
    str += (mydate.getMonth() + 1) + "-";
    str += mydate.getDate();
    return str;
}