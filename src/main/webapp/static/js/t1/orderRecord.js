/**
 * Created by wanglijun on 2016/10/20.
 * 订单记录页面的相关js
 */
var ctx="";
var ctxStatic="";
$(function(){
    ctxStatic=$("#getCtxStatic").val();
    ctx=$("#getCtx").val();
    getSupplier(ctx);
    getOrderRecord(ctx);
    //为搜索按钮绑定事件
    $("#search_child").bind("click",getOrderRecordInCondition)
    $("#fuzzySearch").next().bind("click",getOrderRecordInCondition)
    downOrUp(getOrderRecordInCondition);
    })
var inputDetail={
    "groupOpenDateBegin": "",
    "groupOpenDateEnd": "",
    "orderTimeBegin": "",
    "orderTimeEnd": "",
    "companyIds": "",
    "moneyStrMin": "",
    "moneyStrMax": "",
    "orderStatus": "0", // 0 全部订单；1 待处理；2 已下单；3 已取消；4 已删除
    "pageNo":"",
    "pageSize":"",
    "activityNameOrGroupCode":"",
    "orderBy":""//以何种形式排序
}

/**
 * 获取供应商的列表
 */
function getSupplier(ctx){
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/getCompany",
        cache: false,
        dataType: "json",
        async: true,
        data: {},
        success: function (data) {
            var orderResult = data.officeInfo;
            var supplierHtml="";
            if(orderResult&&orderResult.length){
                for(var i=0,j=orderResult.length;i<j;i++){
                    var supplier=orderResult[i];
                    supplierHtml+='<li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em>' +
                        '<span data-id="'+supplier.officeId+'" title="'+supplier.officeName+'" class="item_text">'+(supplier.officeName.length>10?supplier.officeName.substring(0,10)+"...":supplier.officeName)+'</span></li>';
                }
            }
            $("#supplierSelect").empty().append(supplierHtml);
        }
    });

}
/**
 * 搜索按钮
 */
function getOrderRecordInCondition (orderCondition){
    /**
     * 条件汇总
     */
    inputDetail.groupOpenDateBegin=$("#groupOpenDate").val();
    inputDetail.groupOpenDateEnd=$("#groupCloseDate").val();
    inputDetail.orderTimeBegin=$("#groupOpenDateTwo").val();
    inputDetail.orderTimeEnd=$("#groupCloseDateTwo").val();
    var supplierId="";
    $("#supStorm").find("span.groupHomeSearch_right_child").each(function(){
        supplierId+=$(this).attr("data-id")+",";
    });
    supplierId=supplierId.substring(0,supplierId.length-1);
    inputDetail.companyIds=supplierId;
    var orderState="";

    if( $(".group_nav_child.group_nav_child_active").attr("data-id")=="0"){
        $("#orderState").find("span.groupHomeSearch_right_child").each(function(){
            orderState+=$(this).attr("data-id")+",";
        })
        orderState=orderState.substring(0,orderState.length-1);
    }else{
        orderState= $(".group_nav_child.group_nav_child_active").attr("data-id");
    }
    inputDetail.orderStatus=orderState;
    inputDetail.moneyStrMin=$("#inputSmall").val();
    inputDetail.moneyStrMax=$("#inputSmall").next().val();
    inputDetail.activityNameOrGroupCode=$("#fuzzySearch").val();
    if(typeof orderCondition=="string"&&orderCondition){
        inputDetail.orderBy=orderCondition;
    }
     getOrderRecord();
}

/**
 * 下单记录的具体文档信息
 * @param ctx
 */
function getOrderRecord(){
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/showOrderList",
        cache: false,
        dataType: "json",
        async: true,
        data: inputDetail,
        success: function (data) {
            if($.isEmptyObject(data)){
                return  false;
            }
            $("#table_down").nextAll().remove();
            //订单list
            var orderInfoList = data.orderInfo;
            //分页信息
            var pageInfo=data.pageInfo;
            var count=pageInfo.count;
            var pageNo=pageInfo.pageNo;
            var pageSize=pageInfo.pageSize;
            var last = parseInt(pageInfo.count /pageInfo.pageSize);
            if (count % pageSize != 0 || last == 0) {
                last++;
            }
            $(".totalPageSize").text(count);
            if(pageNo>last){//如果用户输入小于1的数字作为页码，则页码置为1
                pageNo=last;
            }else if(pageNo<=0){//如果用户输入小于1的数字作为页码，则页码置为1
                pageNo=1;
            }
            $(".nowPage").text(pageNo);
            $(".nowPage").text(pageNo);
            $(".totalPage").text(last);
            //初始化时排序按钮的显示
            if(inputDetail.orderBy){//如果不是第一次查询，则不进行按钮处理

            }else{//如果是第一次查询，默认进来，下单时间倒叙
                $("#orderTime em:last").addClass("rank_down_checked");
            }
            var orderRecordHtml="";
            if(orderInfoList&&orderInfoList.length){
                for(var i=0,j=orderInfoList.length;i<j;i++) {
                    var orderInfo = orderInfoList[i];
                    orderRecordHtml += '<div class="table_down">';
                    orderRecordHtml += '<div class="table_down_div">';
                    orderRecordHtml += '<span class="order_number"><span >提交编号：</span><span >' + orderInfo.orderNum + '</span></span>';
                    orderRecordHtml += '<span class="table_down_head"><span >提交时间：</span><span >' + orderInfo.orderTime + '</span></span></div>';
                    orderRecordHtml += '<table class="table_width"><tbody><tr><td width="440px" class="first_t">';
                    if (orderInfoList[i].isT1 == "1") {
                    orderRecordHtml += '<p title="' + orderInfo.acitivityName + '"><a href="javascript:void(0)" onclick="details(' + orderInfo.productId + ',\'' + orderInfo.groupId + '\',\'' + ctx + '\',\'' + ctxStatic + '\')">' + orderInfo.acitivityName + '</a></p>'
                    }else{
                        orderRecordHtml += '<p title="' + orderInfo.acitivityName + '"><a href="javascript:void(0)" onclick="details_jbox()">' + orderInfo.acitivityName + '</a></p>'
                    }
                    // orderRecordHtml+='<p title="'+orderInfo.acitivityName+'"><a href="javascript:void(0)" onclick="details('+orderInfo.productId+',\''+orderInfo.groupCode+'\',\''+ctx+'\',\''+ctxStatic+'\')">'+orderInfo.acitivityName+'</a></p>'
                    orderRecordHtml+='<p><span>供应商：</span><span>'+orderInfo.officeName+'</span><span class="margin_left_30">团号：</span>'+orderInfo.groupCode+'<span></span></p></td>'
                    orderRecordHtml+='<td width="100px"><p>'+orderInfo.groupOpenDate+'</p></td>'
                    orderRecordHtml+='<td width="50px"><p>'+orderInfo.orderPersonNum+'</p></td>'
                    orderRecordHtml+='<td width="110px" class="t_right "><p>'+orderInfo.totalMoney+'</p></td>'
                    orderRecordHtml+='<td width="110px" class="t_right "><p>'+orderInfo.companyMoney+'</p></td>'
                    orderRecordHtml+='<td width="90px" ><p><span style="display: inline-block">'+orderInfo.salerName+'</span><span style="display: block">'+orderInfo.salerPhone+'</span></p></td>'
                    orderRecordHtml+='<td width="100px" class="last_t"><p><span class="btn-block">'+orderInfo.orderStatus+'</span><a href="javascript:void(0)" onclick="orderRecorddetails(\''+orderInfo.id+'\')">详情</a></p></td>'
                    orderRecordHtml+='</tr></tbody></table></div>'
                }
                $("#table_down").hide();
                $("#table_down").after(orderRecordHtml);
                //绑定相关的事件
            }
            else{
                $("#table_down").show();
            }
            //添加分页信息
            var _webVersion="t1";
            var _pageTest={
                count:count,
                pageSize:pageSize,
                pageNo:pageNo
            }
            var _pageHtml= doPage(_pageTest,_webVersion);
            $(".pagination").empty().append(_pageHtml)
        },
        error:function (e) {
            $("#table_down").hide();
        }
    });
}

/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(pageNo,pageSize){
    if(!pageNo&&!pageSize){//此处是为了T1列表展示页面 列表上面小分页而作
        var _pageSizeInput=$(".pagination").find("ul>li").last();
        var _pageSize=_pageSizeInput.find("input").val();
        var _pageNo=_pageSizeInput.prev().find("input").val();
        var _last=_pageSizeInput.prev().prev().prev().find("span").text();

        inputDetail.pageSize=parseInt(_pageSize);
        if(arguments[2]&&arguments[2]=='prev'){
            inputDetail.pageNo=parseInt(_pageNo)-1;
        }else if(arguments[2]&&arguments[2]=='next'&&parseInt(_pageNo)<_last){
            inputDetail.pageNo=parseInt(_pageNo)+1;
        }
    }else{
        inputDetail.pageNo=pageNo;
        inputDetail.pageSize=pageSize;
    }
    //去后台请求数据，并刷新列表项
    getOrderRecord(ctx);
}
/**
 * 根据状态的标签页切换
 * @param index
 */
function getOrderRecordByRecord(index){
   var eventSrc=getEventSrc();
    $(eventSrc).parent().find("span").each(function(){
        if($(this).is(".group_nav_child")){
            this.className="group_nav_child";
            $(eventSrc).attr("class","group_nav_child group_nav_child_active");
        }
    });
    var _orderBy=$("#orderTime").attr("data-id")+" DESC";
    inputDetail={
        "groupOpenDateBegin": "",
        "groupOpenDateEnd": "",
        "orderTimeBegin": "",
        "orderTimeEnd": "",
        "companyIds": "",
        "moneyStrMin": "",
        "moneyStrMax": "",
        "orderStatus": "0", // 0 全部订单；1 待处理；2 已下单；3 已取消；4 已删除
        "pageNo":"",
        "pageSize":"",
        "activityNameOrGroupCode":"",
        "orderBy":_orderBy
    }
    inputDetail.orderStatus=index;
    //蛇精病操作，此处竟然让回到最初的排序
    $(".downOrUp").find("em").each(function(){
        $(this).removeClass("rank_down_checked rank_up_checked");
    })
    $("#orderTime").find("em:last").addClass("rank_down_checked");
    //同时清空下面的筛选条件（认为筛选条件低级？）
    $("#fuzzySearch").val("");
    $("#groupSearch input").val("");
    $("#groupSearch p.provider_input.write_space_use_one").empty();
    //蛇精病结束
    if(index>0){
        $("#orderState").hide();
    }else{
        $("#orderState").show();
    }
    getOrderRecord();


}

/**
 * 下单记录 对应的订单详情
 * t1订单详情
 * @param orderId
 */
function orderRecorddetails(orderId){
    $.ajax({
        type: "POST",
        url: ctx + "/t1/preOrder/manage/t1OrderDetail",
        cache: false,
        dataType: "json",
        async: false,
        data: {"orderId":orderId},
        success: function (data) {
            var _productInfo=data.productInfo;
            var _contacts=data.contacts;
            var _transDetail=data.transDetail;
            var _remarks=data.remarks;
            var _payInfo=data.payInfo;
            var _orderStatus=data.orderStatus;
            var _createOrderHtml="";
            _createOrderHtml+='<div class="orderRecord">';
            //订单的状态进度
            $(".orderRecordStatus").addClass("hide");
            switch (_orderStatus){
                case "待处理":
                    $(".orderStatus1").removeClass("hide");
                    $(".orderStatus1 div.dateGray:first").text(data.orderSubmitDate?(data.orderSubmitDate.substring(0,10)):"");
                    break;
                case "已取消":
                    $(".orderStatus2").removeClass("hide");
                    $(".orderStatus2 div.dateGray:first").text(data.orderSubmitDate?(data.orderSubmitDate.substring(0,10)):"");
                    $(".orderStatus2 div.dateGray:last").text(data.orderTimeForT2?(data.orderTimeForT2.substring(0,10)):"");
                    break;
                case "已删除":
                    $(".orderStatus3").removeClass("hide");
                    $(".orderStatus3 div.dateGray:first").text(data.orderSubmitDate?(data.orderSubmitDate.substring(0,10)):"");
                    $(".orderStatus3 div.dateGray:last").text(data.orderTimeForT2?(data.orderTimeForT2.substring(0,10)):"");
                    break;
                case "已下单":
                    $(".orderStatus4").removeClass("hide");
                    $(".orderStatus4 div.dateGray:first").text(data.orderSubmitDate?(data.orderSubmitDate.substring(0,10)):"");
                    $(".orderStatus4 div.dateGray:last").text(data.orderTimeForT2?(data.orderTimeForT2.substring(0,10)):"");
                    break;
                default:
                    break;
            }

            //产品相关信息
            for(var _key in _productInfo){
                if(!_productInfo[_key]){
                    _productInfo[_key]="";
                }
            }
            _createOrderHtml+='<div class="product-info" style="height:20px;" title="'+_productInfo.productName+'">'+_productInfo.productName+'</div>';
            _createOrderHtml+='<div class="product-item">';
            _createOrderHtml+=' <span class="item-detail" title="'+_productInfo.groupCode+'">团号：'+_productInfo.groupCode+'</span>';
            _createOrderHtml+=' <span class="item-detail">出团日期：'+_productInfo.groupOpenDate+'</span>';
            _createOrderHtml+=' <span class="item-detail">出发城市：'+_productInfo.fromArea+'</span>';
            _createOrderHtml+=' <span class="item-detail">行程天数：'+_productInfo.activityDuration+'天</span>';
            _createOrderHtml+=' <span class="item-detail">交通工具：'+_productInfo.trafficMode+'</span>';
            _createOrderHtml+='</div>';
            //批发商相关信息
            _createOrderHtml+='<label>联系人</label>';
            _createOrderHtml+='<div  class="contacts">';
            _createOrderHtml+='批发商：<span class="saler-name">'+_contacts.companyName+'</span>';
            _createOrderHtml+='联系人：<div  class="saler-name">'+_contacts.salerName+'</div>';
            _createOrderHtml+='电话：<span class="saler-name">'+_contacts.salerPhone+'</span></div>';
            //交易明细的相关部分
            _createOrderHtml+='<label for="">交易明细</label>';
            _createOrderHtml+='<table class="sus-table"><thead>';
            _createOrderHtml+='<tr><th width="200"></th><th width="170" class="tr">成人</th><th width="170" class="tr">儿童</th><th width="170" class="tr">特殊人群</th></tr></thead>';
            _createOrderHtml+='<tbody>';
            _createOrderHtml+='<tr><td>实际结算价</td>';
            var _tempHtml=_transDetail.adultPrice=='--'?'--':_transDetail.adultCurrencyMark+_transDetail.adultPrice+'/人';
            _createOrderHtml+='<td>'+_tempHtml+'</td>';
            _tempHtml=_transDetail.childPrice=='--'?'--':_transDetail.childCurrencyMark+_transDetail.childPrice+'/人';
            _createOrderHtml+='<td>'+_tempHtml+'</td>';
            _tempHtml=_transDetail.specialPrice=='--'?'--':_transDetail.specialCurrencyMark+_transDetail.specialPrice+'/人';
            _createOrderHtml+='<td>'+_tempHtml+'</td>';
            _createOrderHtml+='</tr><tr>';
            _createOrderHtml+='<td>系统结算价</td>';
            var _tempHtml=_transDetail.companyAdultPrice=='--'?'--':_transDetail.companyAdultCurrencyMark+_transDetail.companyAdultPrice+'/人';
            _createOrderHtml+='<td>'+_tempHtml+'</td>';
            var _tempHtml=_transDetail.companyChildPrice=='--'?'--':_transDetail.companyChildCurrencyMark+_transDetail.companyChildPrice+'/人';
            _createOrderHtml+='<td>'+_tempHtml+'</td>';
            var _tempHtml=_transDetail.companySpecialPrice=='--'?'--':_transDetail.companySpecialCurrencyMark+_transDetail.companySpecialPrice+'/人';
            _createOrderHtml+='<td>'+_tempHtml+'</td>';
            _createOrderHtml+='</tr><tr>';
            _createOrderHtml+='<td>人数</td>';
            var _tempNum=_transDetail.adultNum=='--'?'--':(_transDetail.adultNum +'人')
            _createOrderHtml+='<td>'+_tempNum+'</td>';
            var _tempNum=_transDetail.childNum=='--'?'--':(_transDetail.childNum +'人')
            _createOrderHtml+='<td>'+_tempNum+'</td>';
            var _tempNum=_transDetail.specialNum=='--'?'--':(_transDetail.specialNum +'人')
            _createOrderHtml+='<td>'+_tempNum+'</td>';
            _createOrderHtml+='</tr><tr>';
            _createOrderHtml+='<td><div>小计</div><div class="susTdGray">(实际结算价×人数)</div></td>';

            var _tempMark=[_transDetail.adultCurrencyMark,_transDetail.childCurrencyMark,_transDetail.specialCurrencyMark];
            var _tempSum=[_transDetail.adultSum,_transDetail.childSum,_transDetail.specialSum];
            for(var i=0;i<_tempMark.length;i++){
                if(_tempSum[i]=='--'){
                    _createOrderHtml+='<td><span class="orange">--</span></td>';
                }else{
                    _createOrderHtml+='<td>'+_tempMark[i]+'<span class="orange">'+_tempSum[i]+'</span></td>';
                }
            }
            _createOrderHtml+='</tr><tr>';
            //这里的采用的是特殊人群的币种符号，具体需要看业务，待协商。
            _createOrderHtml+='<td  colspan="4"  class="summary">门店结算价差额返还总计：'+_transDetail.specialCurrencyMark+'<span>'+(_transDetail.profitsSum)+'</span></td></tr>';
            _createOrderHtml+='</tbody></table>'
            //收款方式
            var _bankInfo=_payInfo.bankInfo;
            var _zfbInfo=_payInfo.zfbInfo;
            var _wxInfo=_payInfo.wxInfo;
            var _payId=data.payId;

            _createOrderHtml+='<label>收款方式</label><div  class="contacts">';
            //收款方式是不存在的情况
            var _payInfoNoExit=false;
            if(_bankInfo.length>0){//说明选择的是银行卡支付
                for(var _temp=0;_temp<_bankInfo.length;_temp++){
                    if(_payId==_bankInfo[_temp].payId){
                        _createOrderHtml+='收款类型：<span class="saler-name">银行卡</span>';
                        // _createOrderHtml+='银行名称：<span class="saler-name">'+_bankInfo[0].bankName+'</span>';
                        _createOrderHtml+='账户号码：<span class="saler-name">尾号'+_bankInfo[_temp].bankCodeEnd.substring(_bankInfo[_temp].bankCodeEnd.length-4,_bankInfo[_temp].bankCodeEnd.length)+'</span>';
                        _payInfoNoExit=true;
                    }
                }

            }
            if(_zfbInfo.length>0){//说明选择的是支付宝支付
                for(var _temp=0;_temp<_zfbInfo.length;_temp++){
                    if(_payId==_zfbInfo[_temp].payId){
                        _createOrderHtml+='收款类型：<span class="saler-name">支付宝</span>';
                        _createOrderHtml+='账户号码：<span class="saler-name">'+_zfbInfo[_temp].accountCode+'</span>';
                        _payInfoNoExit=true;
                    }
                }

            }
            if(_wxInfo.length>0){//说明选择的是微信支付
                for(var _temp=0;_temp<_wxInfo.length;_temp++){
                    if(_payId==_wxInfo[_temp].payId){
                        _createOrderHtml+='收款类型：<span class="saler-name">微信</span>';
                        _createOrderHtml+='账户号码：<span class="saler-name">'+_wxInfo[_temp].accountCode+'</span>';
                        _payInfoNoExit=true;
                    }
                }

            }
            if(!_payInfoNoExit){
                _createOrderHtml+='无';
            }
            _createOrderHtml+='</div>';
            _createOrderHtml+='<label >备注</label>';
            _createOrderHtml+='<div class="remark_use">'+_remarks+'</div>';
            _createOrderHtml+='<div  id="closeOrderDetail" class="buttons"><span  class="unable" onclick="window.parent.window.jBox.close();" >关闭</span></div>';
            _createOrderHtml+='</div>';
            $(".orderStatusDiv").nextAll().remove();
             $("#orderRecord").append(_createOrderHtml);
            $pop=$.jBox($("#orderRecord").html(), {
                title: "订单信息",
                width: 880,
                height: 600,
                persistent: true,
                buttons:false,
                loaded:function(){
                    window.productDetailTitle=$pop.find(".jbox-title").html()
                    if($(window).height()<800){
                        $("#jbox", window.parent.document).css("top","0");
                    }
                }
            });
        }
    })

}
