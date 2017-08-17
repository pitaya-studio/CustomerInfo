/**
 * 538需求相关js
 */
/**
 * 取自于t1home.js,为了保存原先的输入数据
 */
//首页计算的时候输入的数据
var inputDetail={
    "adultPrice":"",
    "adultNum":"",
    "childPrice":"",
    "childNum":"",
    "specialPrice":"",
    "specialNum":"",
    "groupId":"",
    "orderPersonNum":"",
    "payId": "",
    "remark": "测试数据"
};
var isconfirmToordered=false;
function  initInputDetail (type,adult_money,adult,child_money,child,special_money,special){
    if(type!="1"){
        inputDetail={
            "adultPrice":"",
            "adultNum":"",
            "childPrice":"",
            "childNum":"",
            "specialPrice":"",
            "specialNum":"",
            "groupId":"",
            "orderPersonNum":"",
        };
    }else{
        inputDetail.adultPrice=adult_money;
        inputDetail.adultNum=adult;
        inputDetail.childPrice=child_money;
        inputDetail.childNum=child;
        inputDetail.specialPrice=special_money;
        inputDetail.specialNum=special;
        inputDetail.orderPersonNum=parseInt(adult==""?0:adult)+parseInt(child==""?0:child)+parseInt(special==""?0:special);
    }
}
/**
 * 产品详情弹窗内的下单操作
 * ${ctx}:接口路径
 * groupId：团期Id
 * salerUserId:被选中的销售下单人员Id
 */
function  placeOrder(groupId,salerUserId,ctx){
    window.parent.$pop.find(".jbox-title").html("下单信息");
    $(".pop_content").hide();
    fillOrderForm(groupId,salerUserId,ctx);
    $("#ord").show();
    // $(".order_bounced_body").getNiceScroll().resize();
    /* $.jBox.defaults.title = "下单详情";*///修改title值
    barUse();
}
/**
 *解决chrome浏览器jbox点击title滚动条消失的问题
 */
function barUse(){
    window.parent.$pop.find(".jbox-title-panel").click(function(){
        if($("#ord").css("height")=="659px") {
            $("#ord").css("height", "inherit");
        }else{
            $("#ord").css("height", "");
        }
    })
}
/**
 * 产品详情弹窗内的下单中  上一步 操作
 */
function modifyOrder(){
    $("#ord").hide();
    $(".pop_content").show();
    $("#order").hide();
    // count_again();//返回到计算首页
    order_this();//返回到上一步的操作
    window.parent.$pop.find(".jbox-title").html(window.parent.productDetailTitle)
};

/**
 * 填充下单信息
 */
function fillOrderForm(groupId,salerUserId,ctx){
    var _createOrderHtml="";
    inputDetail.groupId=groupId;
    inputDetail.salerId=salerUserId;
    //正式时放开
      $.ajax({
     type: "POST",
     url: ctx+"/t1/preOrder/manage/t1OrderDetail",
     cache:false,
     dataType:"json",
     async:false,
     data:inputDetail,
     success: function(data){
     var orderResult=data;
    // var orderResult=orderOBJ;

    //产品相关信息
    var _productInfo= orderResult.productInfo;
         for(var _key in _productInfo){
             if(!_productInfo[_key]){
                 _productInfo[_key]="";
             }
         }
    _createOrderHtml+='<div class="product-info" title="'+_productInfo.productName+'">'+_productInfo.productName+'</div>';
    _createOrderHtml+='<div class="product-item">';
    _createOrderHtml+=' <span class="item-detail" title="'+_productInfo.groupCode+'">团号：'+_productInfo.groupCode+'</span>';
    _createOrderHtml+=' <span class="item-detail">出团日期：'+_productInfo.groupOpenDate+'</span>';
    _createOrderHtml+=' <span class="item-detail">出发城市：'+_productInfo.fromArea+'</span>';
    _createOrderHtml+=' <span class="item-detail">行程天数：'+_productInfo.activityDuration+'天</span>';
    _createOrderHtml+=' <span class="item-detail">交通工具：'+_productInfo.trafficMode+'</span>';
    _createOrderHtml+='</div>';
    //批发商相关信息
    var _contacts=orderResult.contacts;
    _createOrderHtml+='<label>联系人</label>';
    _createOrderHtml+='<div  class="contacts">';
    _createOrderHtml+='批发商：<span class="saler-name" title="'+_contacts.companyName+'">'+_contacts.companyName+'</span>';
    _createOrderHtml+='联系人：<div  class="dl-select small-dl" title="'+_contacts.salerName+'">'+_contacts.salerName+'</div>';
    _createOrderHtml+='电话：<span class="saler-name">'+_contacts.salerPhone+'</span></div>';
    //交易明细的相关部分
    var _transDetail=orderResult.transDetail;
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
    _createOrderHtml+='<td  colspan="4"  class="summary">门店结算价差额返还总计：'+_transDetail.specialCurrencyMark+'<span>'+_transDetail.profitsSum+'</span></td></tr>';
    _createOrderHtml+='</tbody></table>'

    //备注
    var _remarks=orderResult.remarks
    _createOrderHtml+='<label >备注</label>';
    _createOrderHtml+='<textarea  name="" id="orderDetailRemark"  maxlength="150" class="remark-order">'+_remarks+'</textarea>'
    _createOrderHtml+='<div  class="buttons">'
    _createOrderHtml+='<span  class="unable"  onclick="previousStep(1)">上一步</span><span  onclick="getPayMethod();">下一步</span></div>'
    $("#ord").empty().append(_createOrderHtml);
    //给下拉框绑定事件
    bindClick();
    //收款方式页面的拼接，本应该放在下一步操作中，但整体定义了一个接口，数据一起带过来了
        createPayHtml(orderResult.payInfo,ctx);
     },
     error : function(e){
     top.$.jBox.tip('请求失败。','error');
     return false;
     }
     });
}
/**
 * 给下单页面的下拉框绑定下拉事件
 */
function bindClick(){
    /*点击下拉款*/
    $(".dl-select input").click(function(){
        $(this).parent().children("ul").toggle();
        var event = getE();
        if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
            event.cancelBubble = true;
        }else{
            event.stopPropagation();
        }
    });
    $(".dl-select ul li").each(function(){
        $(this).click(function(){
            var value = $(this).text();
            $(this).parent().hide();
            $(this).parent().parent().children("input").val(value);
        });
    });
}
/**
 * 按此价格下单 按钮
 */
function order_this(){
    $("#order").show();
    $(".profit_count_two").hide();
    $(".order_contact:first").css("border-top","none")
    // $('.order_bounced_body').niceScroll({
    //     cursorcolor: "#ccc",//#CC0071 光标颜色
    //     cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
    //     touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
    //     cursorwidth: "5px", //像素光标的宽度
    //     cursorborder: "0", //     游标边框css定义
    //     cursorborderradius: "5px",//以像素为光标边界半径
    //     autohidemode: false //是否隐藏滚动条
    // });
}


//详情弹窗计算功能
function count() {
    //定义变量flag 为true时 ，可以点击计算按钮
    var flagPeoNo=false;//人数是否通过标志
    var flagPrice=false;//金额是否通过标志
    var flagAll=true;
    var adult_price = $("#adult_price").text();//系统结算价
    var adult_money = $("#adult_money").val();//实际结算价
    var adult = $("#adult").val();//人数
    var child_price = $("#child_price").text();
    var child_money = $("#child_money").val();
    var child = $("#child").val();
    var special_price = $("#special_price").text();
    var special_money = $("#special_money").val();
    var special = $("#special").val();
    /**
     * 538需求与产品协议，进行了判定修改
     * @type {*[]}
     */
    var people=[
        {"whoNo":adult,"whom":"成人","people_price":adult_price,"people_money":adult_money},
        {"whoNo":child,"whom":"儿童","people_price":child_price,"people_money":child_money},
        {"whoNo":special,"whom":"特殊人群","people_price":special_price,"people_money":special_money}
    ];
    for(var _i=0;_i<people.length;_i++ ){
        var peo=people[_i];
        if(Object.prototype.toString.call(peo)!="[object Object]"){
            continue;
        }
        if(peo["people_price"]==""&&peo["people_money"]!=""){
            flagPrice=false;
            $.jBox.tip(peo["whom"]+'无系统结算价，请重新填写','error',{focusId:"top"});
            return false;
        }
        if(peo["people_money"]!=""||peo["whoNo"]!=""){
            //价格个人数都没填写
            flagAll=false;
        }
        if(peo["people_money"]!=""&&(peo["whoNo"]==""||peo["whoNo"]==0)){
            $.jBox.tip('请填写正确的'+peo["whom"]+'人数','error');
            return false;
        }
        if(peo["people_money"]==""&&peo["whoNo"]!=""){
            $.jBox.tip('请填写'+peo["whom"]+'实际结算金额','error');
            return false;
        }
    }
    if(flagAll){
        $.jBox.tip('请输入数值及人数','error');
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
    var money_head1=$(".money_head1");
    var money_head2=$(".money_head2");
    var money_head3=$(".money_head3");
    var all_last1=$(".all_last1");
    var all_last2=$(".all_last2");
    var all_last3=$(".all_last3");
    //为下单准备提交数据
    initInputDetail("1",adult_money,adult,child_money,child,special_money,special);
    //如果金额为空，人数为空，则不允许下单，给出提示即可
    if(!adult_last&&!child_last&&!special_last){
        isconfirmToordered=false;
    }else{
        isconfirmToordered=true;
    }


    //最终利润
    var alllast="";
    $("#adult_last").text(adult_last);//利润显示  成人
    $("#child_last").text(child_last);
    $("#special_last").text(special_last);
    var currencyCode=[money_1,money_2,money_3];
    var currency=[adult_last,child_last,special_last]
    var total={};
    for(var i=0;i<currencyCode.length;i++){
        //如果存在
        if(total[currencyCode[i]]){
            total[currencyCode[i]]=parseFloat((Number(currency[i])+Number(total[currencyCode[i]])).toFixed(2));
        }else{
            //如果不存在
            if(!currency[i]){
            }else{
                total[currencyCode[i]]=Number(currency[i]);
            }
        }
    }
    var _flag=true;
    for(var x in total){
        if(total[x].toString().length>=5){
            _flag=false;
        }
    }
    for(var x in total){
        alllast +='<span class="money_head1">'+x+'</span>'
//        if(total[x].toString().length>=5){
        if(!_flag){
            alllast+='<span class="orange font_44 all_last1" id="all_last" style="font-size: 18px;">'+total[x]+'</span>+';
        }else{
            alllast+='<span class="orange font_44 all_last1" id="all_last" style="font-size: 26px;">'+total[x]+'</span>+';
        }
    }
    if($(".multi_currency").find("span").length>0){
        $(".multi_currency").empty();
        alllast="利润："+alllast;
    }
    alllast=alllast.substring(0,alllast.length-1);
//        alllast.slice(0,-1);
    $(".multi_currency").append(alllast);
    $(".profit_count_one").hide();
    $(".profit_count_two").show();
    if($(".count_parent").eq(1).children().length ==2){
        $(".count_again").addClass("count_gray");
    }else{
        $(".count_again").removeClass("count_gray");
    }
}
/**
 * 下单详情页面的 下一步 按钮
 */
function  getPayMethod(){
    inputDetail.remark=$("#orderDetailRemark").val();
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
function  previousStep(which){
    if(which==1){//下单页面的上一步操作
        $("#ord").hide();
        $(".pop_content").show();
        $("#order").hide();
        // count_again();//返回到计算首页
        order_this();//返回到上一步的操作
        window.parent.$pop.find(".jbox-title").html(window.parent.productDetailTitle)
    }else if(which==2){//收款页面的上一步操作
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
function payTabChange(obj,which){
  $(obj).removeClass("active").addClass("active");
  $(obj).siblings().removeClass("active");
    $(obj).prev().addClass("borderRN");
    $(obj).prev().prev().removeClass("borderRN");
    $(obj).next().addClass("borderLN");
    $(obj).next().next().removeClass("borderLN");
    $(obj).removeClass("borderLN").removeClass("borderRN");
    if(which==1){
        cleanChecked("#bank","#alipay","#weChat");
    }else if(which==2){
        cleanChecked("#alipay","#bank","#weChat");
    }else if(which==3){
        cleanChecked("#weChat","#alipay","#bank");
    }
}

function cleanChecked(first,second,third){
    $(first).show().find("input").first().attr("checked","checked");
    $(second).hide().find("input").removeAttr("checked");
    $(third).hide().find("input").removeAttr("checked");

}

/**
 * 每个支付对象，支持行选定
 */
function paymentChecked (){
    $("#pay-child").find("ul>li").bind("click",function(e){
        e=getE();
        $target=$(e.target||e.srcElement);
        var _checkedLi=$(e.target||e.srcElement);
        if(_checkedLi[0].nodeName=="LI"){
            _checkedLi.children().first().attr("checked","checked");
        }else{
            _checkedLi.attr("checked","checked");
            _checkedLi.siblings().attr("checked","false");
        }

    })
}

/**
 * 进入收款页面时初始化
 * 默认在第一个标签页，默认选中第一个账户
 */
function initChecked(){
    $("#pay ul.tabs").children().eq("0").addClass("active").nextAll().removeClass("active");
    $("#bank").show();
    $("#alipay").hide();
    $("#weChat").hide();
    $("#pay-child").find("li input[type='radio']").removeAttr("checked");
    $("#bank").find("input").first().attr("checked","checked");
}

var _btncount=1;
/**
 * 收款方式 提交
 */
function submitOrderDetail(ctx,event){
   var abc=$("#pay-child").find("li input[type='radio']:checked").nextAll();
    abc.first().text();
   var payId= abc.first().attr("data-id");
    inputDetail.payId=payId;
    if(_btncount==1){
        _btncount++;
       $.ajax({
           type: "POST",
           url: ctx+"/t1/preOrder/manage/saveOrder",
           cache:false,
           async:false,
           data:inputDetail,
           success: function(data) {

               if (data.result == "success") {
            		$.ajax({
            	        type : "post",
            	        url : ctx + "/orderProgressTracking/manage/save?dom=" + Math.random(),
            	        data : {
            	        	groupId : data.groupId,
                            orderType : 2,
                            preOrderId : data.preOrderId
            	        }
            	    });
                   top.$.jBox.tip('保存成功', 'success',{top:'0',timeout: 1500,
                       closed: function () {
                           _btncount=1;
                           window.parent.location.href = ctx + "/t1/preOrder/manage/showT1OrderList";
                           window.parent.jBox.close();
                       } /* 提示关闭后执行的函数 */
                   });
               } else {
                   top.$.jBox.tip('保存失败', 'error',{top:'0'});
                   _btncount=1;
               }
           },
           error:function(){
               top.$.jBox.tip('提交失败。', 'error',{top:'0'});
               _btncount=1;
           }
       })
   }else if(_btncount>1){
       top.$.jBox.tip('请勿重复提交', 'error',{top:'0'});
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
function createPayHtml(payInfo,ctx){
    var _payHtml='';
    var _bankInfoList=payInfo.bankInfo;
    var _zfbInfoList=payInfo.zfbInfo;
    var _wxInfoList=payInfo.wxInfo;
    _payHtml+='<div  class="pay-tip">请选择您要收款的方式：</div>';
    _payHtml+='<div class="tab-container"><ul class="tabs">';
    _payHtml+='<li onclick="payTabChange(this,1)" class="active"><a href="#bank">银行卡</a></li>'+
               '<li onclick="payTabChange(this,2)" class=""><a href="#alipay">支付宝</a></li>'+
               '<li onclick="payTabChange(this,3)" class=""><a href="#weChat">微信</a></li></ul>';
    _payHtml+=' <div id="pay-child" class="tab_containers"  style="clear: both;">'
            +'<div id="bank" class="tab_content" style="display: block;">'
            +' <ul  class="bank-list">';
    //收款方式可以不选，我屮艸芔茻
    _payHtml+='<li><input name="bankNo" type="radio"><span data-id=""  class="bank-info">无</span></li>'
    for(var i=0;i<_bankInfoList.length;i++){
        var _bankInfo=_bankInfoList[i];
        _payHtml+='<li><input name="bankNo" type="radio"><span data-id="'+_bankInfo.payId+'"  class="bank-info" data-type="'+_bankInfo.accountPayType+'"  title="'+_bankInfo.bankName+'">'+_bankInfo.bankName+'</span><span  class="card-info">尾号'+_bankInfo.bankCodeEnd.substring(_bankInfo.bankCodeEnd.length-4,_bankInfo.bankCodeEnd.length)+'</span><span>'+_bankInfo.userName+'</span></li>'
    }
    _payHtml+='</ul></div>'
    //支付宝模块开始
    _payHtml+='<div id="alipay" class="tab_content" style="display: none;"><ul class="bank-list">';
    _payHtml+='<li><input name="alipayNo" type="radio"><span data-id=""  >无</span></li>'
    for(var i=0;i<_zfbInfoList.length;i++){
        var _zfbInfo=_zfbInfoList[i];
        _payHtml+='<li><input name="alipayNo" type="radio"><span data-id="'+_zfbInfo.payId+'" class="alipy_img"></span><span  class="card-info" title="'+_zfbInfo.accountCode+'" data-type="'+_zfbInfo.accountPayType+'">'+_zfbInfo.accountCode+'</span><span>'+_zfbInfo.userName+'</span></li>'
    }
    _payHtml+='</ul></div>'
    //微信模块开始
    _payHtml+='<div id="weChat" class="tab_content" style="display: none;"><ul class="bank-list">';
    _payHtml+='<li><input name="wechatNo" type="radio"><span data-id=""  >无</span></li>'
    for(var i=0;i<_wxInfoList.length;i++){
        var _wxInfo=_wxInfoList[i];
        _payHtml+='<li><input name="wechatNo" type="radio"><span data-id="'+_wxInfo.payId+'" class="wechat_img"></span><span  class="card-info" title="'+_wxInfo.accountCode+'" data-type="'+_wxInfo.accountPayType+'">'+_wxInfo.accountCode+'</span><span>'+_wxInfo.userName+'</span></li>'
    }
    _payHtml+='</ul></div>'
    _payHtml+='</div></div>';
    _payHtml+='<div  class="buttons">';
    _payHtml+='<span class="unable" onclick="previousStep(2)">上一步</span><span  onclick="submitOrderDetail(\''+ctx+'\')">提交</span></div>';
    $("#pay").empty().append(_payHtml);
    paymentChecked();
}




