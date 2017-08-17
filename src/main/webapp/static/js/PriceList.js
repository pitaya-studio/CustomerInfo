/**
 * Created by Administrator on 2016/10/10.
 */
$(function(){
    divScroll();
    ctx = $("#ctx").val();
    $(window).resize(function(){
        divScroll();
    });



    $(".special-info dt i").mouseenter(function(){//悬浮显示特殊人群备注
        $(this).parent().next().show();
    }).mouseout(function(){
        $(this).parent().next().hide();
    });

    var $tbody = $("tr[name='proGroupMainTr']").parent();
    $tbody.each(function(){
        var $dd = $(this).find(".more-op-style dd");
        if($(this).next().length!==1){//如果是最后一条记录则更多选择浮窗出现在上方
            $dd.addClass("last-dd");
            $dd.find("span").appendTo($dd);
        }
    });



    $(".more-op-style").mouseover(function(){
        $(this).find("i").removeClass("fa-angle-down").addClass("fa-angle-up");//悬浮时箭头变为向上
    }).mouseleave(function(){
        $(this).find("i").removeClass("fa-angle-up").addClass("fa-angle-down");
    });
});

$(document).on("mouseover mouseout", ".handle", function (event) {
    if (event.type == "mouseover") {
        //鼠标悬浮
        if (0 != $(this).find('a').length) {
            $(this).addClass('handle-on');
            $(this).find('dd').addClass('block');
        }
    } else if (event.type == "mouseout") {
        //鼠标离开
        $(this).removeClass('handle-on');
        $(this).find('dd').removeClass('block');
    }
});

function priceListJbox(obj,rmAdult,rmChild,rmSpecial) {//价格表弹窗
    debugger;
    $.ajax({
         type : "POST",
         url  : ctx + "/activity/manager/findAllAgentName",
         async: true,
        dataType:"json",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success:function(data){
            var agentNameData = data;//假数据测试，这行删掉
            if (agentNameData) {

                var $tr = $(obj).parents("tr:first");
                var pName = $tr.parents("tr").prev().find(".activity_name_td a").text();//获取产品名称
                if ($tr.parent().parent().parent().hasClass("divScroll")) {//散拼报名获取出团日期和团号和团期ID
                    var pOutDate = $tr.find(".word-break-all").next().find(".out-date").text();
                    var pNum = $tr.find(".group-num").text();
                    var groupId = $tr.find(".group-num input").attr("value");
                } else {//散拼产品获取出团日期和团号
                    var pOutDate = $tr.find("input[name=groupOpenDate]").prev().text();
                    var pNum = $tr.find("span[name=groupCode]").text();
                    var groupId = $tr.find("input[name=groupCode]").attr("id");
                }
                //modify '
                var html1 = '<div id="priceList">' +
                    '<div id="pl-productName"><span>产品名称：</span><span class="ellipsis">' + pName +
                    '</span></div><div id="pl-num-outdate"><span>团号：</span><span>' + pNum +
                    '</span>&nbsp;&nbsp;&nbsp;<span>出团日期：</span><span>' + pOutDate +
                    '</span></div><div id="pl-search"><span>渠道：</span>' +
                    '<select id="pl-search-select" data-placeholder="可输入查找渠道名称"><option></option>' ;
                var html2= '</select> ' +
                    // '<span id="pl-tips" class="ipt-tips" style="display: block;">可输入查找渠道名称</span>' +
                    '<input class="btn btn-primary" id="pl-searchbutton" type="button" value="查询"></div>' +
                    '<table id="pl-table-head"><thead class="table-lists-header"><tr><th width="60%">渠道名称</th><th width="40%">供应价</th></tr></thead></table>' +
                    '<div id="pl-result"><table><tbody><tr id="pl-no-results"><td colspan="2">无查询结果，请选择渠道进行查询</td></tr></tbody></table></div>';
                var _length=agentNameData.length;
                var tempHtml=[];
                for (var i = 0; i < _length; i++) {//写入下拉框选项
                    var option = '<option>' + agentNameData[i] + '</option>';
                    tempHtml.push(option);
                }
                var html=html1+tempHtml.join(" ")+html2;
                $pop = $.jBox(html, {
                    title: "价格表",
                    buttons: {},
                    width: 600,
                    height: 400,
                    showScrolling: false,
                    persistent: true
                });
                $pop.find("#pl-search-select").comboboxInquiry();//生成可输入可下拉框
                $pop.find('#pl-result').niceScroll({//生成滚动条
                    cursorcolor: "#ccc",//#CC0071 光标颜色
                    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                    cursorwidth: "8px", //像素光标的宽度
                    cursorborder: "0", //     游标边框css定义
                    cursorborderradius: "8px",//以像素为光标边界半径
                    autohidemode: false //是否隐藏滚动条
                });
                var $input = $pop.find(".custom-combobox-input");

                /*var html = '<div id="priceList">' +
                    '<div id="pl-productName"><span>产品名称：</span><span class="ellipsis">' + pName +
                    '</span></div><div id="pl-num-outdate"><span>团号：</span><span>' + pNum +
                    '</span>&nbsp;&nbsp;&nbsp;<span>出团日期：</span><span>' + pOutDate +
                    '</span></div><div id="pl-search"><span>渠道：</span>' +
                    '<select id="pl-search-select"><option></option></select> ' +
                    // '<span id="pl-tips" class="ipt-tips" style="display: block;">可输入查找渠道名称</span>' +
                    '<input class="btn btn-primary" id="pl-searchbutton" type="button" value="查询"></div>' +
                    '<table id="pl-table-head"><thead class="table-lists-header"><tr><th width="60%">渠道名称</th><th width="40%">供应价</th></tr></thead></table>' +
                    '<div id="pl-result"><table><tbody><tr id="pl-no-results"><td colspan="2">无查询结果，请选择渠道进行查询</td></tr></tbody></table></div>';


                $pop = $.jBox(html, {
                        title: "价格表",
                        buttons: {},
                        width: 600,
                        height: 400,
                        showScrolling: false,
                        persistent: true
                });
                $pop.find("#pl-search-select").comboboxInquiry();//生成可输入可下拉框

                for (var i = 0; i < agentNameData.length; i++) {//写入下拉框选项
                    var name = agentNameData[i];
                    var option = '<option>' + name + '</option>';
                    $pop.find("#pl-search-select").append(option);
                }

                $pop.find('#pl-result').niceScroll({//生成滚动条
                        cursorcolor: "#ccc",//#CC0071 光标颜色
                        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                        cursorwidth: "8px", //像素光标的宽度
                        cursorborder: "0", //     游标边框css定义
                        cursorborderradius: "8px",//以像素为光标边界半径
                        autohidemode: false //是否隐藏滚动条
                });
                //bug16440 使用placeholder插件使placeholder兼容ie8
                var $input = $pop.find(".custom-combobox-input");
                $input.attr("placeholder","可输入查找渠道名称");
                $('input').placeholder();*/


                //
                // $input.on("focus", function () {//获得焦点时提示文字隐藏
                //     $pop.find("#pl-tips").hide();
                //
                // }).on("blur", function () {
                //     if ($input.val() == "") {//失去焦点时input为空则提示文字出现
                //         $pop.find("#pl-tips").show();
                //     }
                // });

                $("#pl-searchbutton").on("click",function(){
                    var searchInputVal = $input.val();
                    $.ajax({
                         type : "POST",
                         url	 : ctx + "/activity/manager/findPriceTable",
                         async: true,
                         dataType:"json",
                         contentType: "application/x-www-form-urlencoded;charset=utf-8",
                         data:{groupId:groupId, searchInputVal:searchInputVal},
                         success:function(result){
                              var agentDataList = result;//假数据测试
                              if(agentDataList) {
                                  var agent="";
                                  var adultPrice = null;
                                  var childPrice = null;
                                  var specialPrice = null;
                                  if (agentDataList.length == 0){
                                      var agent='<tr id="pl-no-results"><td colspan="2">无查询数据</td></tr>';
                                  }else {
                                      for(var j = 0;j < agentDataList.length;j++){
                                          var agentData = agentDataList[j];
                                          if (agentData.rateAdlut != ""){
                                              adultPrice = formatMoney(agentData.rateAdlut, 2);
                                          }else {
                                              adultPrice = "-";
                                          }
                                          if(agentData.rateChild != ""){
                                              childPrice = formatMoney(agentData.rateChild, 2);
                                          }else {
                                              childPrice = "-";
                                          }
                                          if (agentData.rateSpecil != ""){
                                              specialPrice = formatMoney(agentData.rateSpecil, 2);
                                          }else {
                                              specialPrice = "-" ;
                                          }

                                          agent +=
                                              '<tr>' +
                                              '<td class="pl-qdName word-break-all">'+agentData.agentName+'</td>' +
                                              '<td class="pl-content">' +
                                              '<div class="price-list">' +
                                              '<span class="price-title">成人：</span>' +
                                              '<span class="price-content word-break-all"><span class="rm">'+rmAdult+'</span>'+adultPrice+'</span>' +
                                              '</div>' +
                                              '<div class="price-list">' +
                                              '<span class="price-title">儿童：</span>' +
                                              '<span class="price-content word-break-all"><span class="rm">'+rmChild+'</span>'+childPrice+'</span>' +
                                              '</div>' +
                                              '<div class="price-list">' +
                                              '<span class="price-title">特殊人群：</span>' +
                                              '<span class="price-content word-break-all"><span class="rm">'+rmSpecial+'</span>'+specialPrice+'</span>' +
                                              '</div></td></tr>';
                                      }
                                  }
                                    //$pop.find("#pl-result tbody").html(agent);//将数据放进tbody
                // }
                              }else{//如果没有结果则显示提示
                                   var agent='<tr id="pl-no-results"><td colspan="2">请输入渠道名称查找数据</td></tr>';
                                    //$pop.find("#pl-result tbody").html(agent);
                              }
                             $pop.find("#pl-result tbody").html(agent);
                             $("#pl-result").getNiceScroll().resize();
                         }
                    });
                });
            }
        }
    });
}





function formatMoney(s, type) {//金额格式化-s:原始金额 type:小数点后位数
    if (/[^0-9\.]/.test(s))
        return "0";
    if (s == null || s == "")
        return "0";
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;
    while (re.test(s))
        s = s.replace(re, "$1,$2");
    s = s.replace(/,(\d\d)$/, ".$1");
    if (type == 0) {// 不带小数位(默认是有小数位)
        var a = s.split(".");
        if (a[1] == "00") {
            s = a[0];
        }
    }
    return s;
}





function divScroll(){//ie9 overflow:auto bug
    $(".divScroll").each(function(){
        var divWidth = $(this).width();
        var tableWidth = $(this).find("table").width();
        if(divWidth<tableWidth){
            $(this).css("overflow-x","scroll");
        }else{
            $(this).css("overflow-x","hidden");
        }
    });
}



