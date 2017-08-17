/**
 * Created by zrq quauq on 2017/2/7.
 * 微信sdp-0103
 */
var ctx = $("#ctx").val();
var paraInput = {
    "box": "searchBox",//外部span,id名
    "btn": "searchBtn",//按钮id名
    "inpt": "searchInput",//输入框id名
    "resu": "searchResu",//下拉列表id名
    "active": "searchActive"//鼠标滑上去active状态className
}
//将所有尺寸放入paraInputSize,
var paraInputSize = {
    "input_width": "234px",//输入框宽度
    "input_height": "28px",//输入框高度
    "resu_top": "30px",//下拉列表定位top值
    "resu_maxH": "100px",//下拉列表最大高度
    "resu_liH": "20px",//下拉列表选项li高度
    "btn_width": "20px",//按钮宽度
    "btn_height": "29px",//按钮高度
    "btn_left": "221px"//按钮定位left值
}
var ajax_data = null;
/**
 * 打开弹窗
 */
function linkToChannel(wxId) {
    var html = '<div id="customerLevel">'
        + '<div class="content-container"  style="height:391px">'
        + '</div></div>';
    $pop = $.jBox(html, {
        title: "账号匹配",
        buttons: {'确定': 0, '生成账号': 1},
        submit: function (v, h, f) {
            if (v == 0) {
                //判断是否选中手动搜索
                var isLastinputChecked = $(".jbox_body .chk_box").children("input:last").is(":checked"),
                    wxAccountId = wxId;
                if (!isLastinputChecked) {
                    var channelId = $(".cnl_account .chk_box").children("input:checked").parent().siblings(".channel_info").children("input").val(),
                        channelUserId = $(".cnl_account .chk_box").children("input:checked").parent().siblings(".channel_name").children("input").val();
                    //打开匹配账号页面
                    matchAccount(wxAccountId, channelId, channelUserId);
                }else{
                    var searchAgentId = $(".searchResult_wx").eq(2).siblings("input").val(),
                        searchUserId = $(".searchResult_wx").eq(0).siblings("input").val();
                    if(!searchUserId){
                        return false;
                    }else{
                        //打开匹配账号页面
                        matchAccount(wxAccountId, searchAgentId, searchUserId);
                    }
                }
            }else{
                //打开生成账号页面
                createAccount(wxId);
            }
        },
        width: 530,
        height: 470,
        showScrolling: false,
        persistent: true
    });
    //获取弹窗数据
    getWxData(wxId);
    //判断座机号是否为空
    isPhoneEmpty();
    //获取弹窗手动搜索模块数据
    searchBox(paraInput, paraInputSize);
    // 滚动条优化
    // $('.jbox_body').niceScroll({
    //     cursorcolor: "#ccc",
    //     cursoropacitymax: 1,
    //     touchbehavior: false,
    //     cursorwidth: "5px",
    //     cursorborder: "0",
    //     cursorborderradius: "5px"
    // });
    // $("#jbox-content").css({
    //     "overflow-y":"hidden",
    //     "height":"100%"
    // });
}
/**
 * 弹窗数据加载
 * @param wxId 微信用户ID
 */
function getWxData(wxId) {
    // $(".content-container").empty();
    $.ajax({
        type: "GET",
        url: ctx + "/quauqAgent/manage/getMatchedAccount",
        cache: false,
        dataType: "json",
        async: false,
        data: {
            "mobileUserId": wxId
        },
        success: function (data) {
            if (data) {
                var wx_channelName = data.mobileUserMsg.agentName ||"",
                    wx_wxNum = data.mobileUserMsg.wechatCode ||"",
                    wx_mobile = data.mobileUserMsg.telephone || "",
                    wx_phone = data.mobileUserMsg.phone || "",
                    wx_name = data.mobileUserMsg.name || "",
                    wx_mobileUser_Id = data.mobileUserMsg.mobileUserId,
                    wx_areaCode = data.mobileUserMsg.areaCode || "";

                var _length, qudao_channelName, qudao_wxNum, qudao_mobile, qudao_phone, qudao_name, qudao_agent_Id, qudao_sysUser_Id;
                var channel_html = "", wx_html = "";
                //微信记录信息
                wx_html += "<div class = 'jbox_ta_head wx_info'><ul class='channel_name'>" +
                    "<li class = jbox_ta_header><b>微信记录信息</b></li><li class = jbox_ta_header>渠道名称 ：<span>" + wx_channelName + "</span></li>" +
                    "</ul><ul class='channel_info'>" +
                    "<li class = jbox_ta_header><div><em>微信号 ：</em><span>" + wx_wxNum + "</span></div></li>" +
                    "<li class = jbox_ta_header><div><em>手机号 ：</em><span>" + wx_mobile + "</span></div></li>" +
                    "<li class = jbox_ta_header><div><em>姓名 ：</em><span>" + wx_name + "</span></div></li>" +
                    "<li class = jbox_ta_header><div><em>座机号 ：</em><span>" + wx_areaCode+''+ wx_phone + "</span></div></li>" +
                    "</ul><input type='hidden' value='" + wx_mobileUser_Id + "'></div>";
                channel_html += "<div class = 'jbox_body'>";

                if (data.t1UserList) {
                    _length = data.t1UserList.length;
                    for (i = 0; i < _length; i++) {
                        qudao_channelName = data.t1UserList[i].agentName || "",
                            qudao_wxNum = data.t1UserList[i].wechatCode || "",
                            qudao_mobile = data.t1UserList[i].contactMobile || "",
                            qudao_phone = data.t1UserList[i].contactPhone || "",
                            qudao_name = data.t1UserList[i].contactName || "",
                            qudao_agent_Id = data.t1UserList[i].agentId,
                            qudao_sysUser_Id = data.t1UserList[i].sysUserId;

                        //渠道账号匹配信息
                        channel_html += "<div class = 'jbox_ta_head cnl_account'>" +
                            "<div class='chk_box'><input name='selectRadio' type='radio'></div>" +
                            "<ul class='channel_name'><li class = jbox_ta_header><b>渠道账号信息</b></li>" +
                            "<li class = jbox_ta_header>渠道名称 ：<span>" + qudao_channelName + "</span></li><input type='hidden' value='" + qudao_sysUser_Id + "'></ul>" +
                            "<ul class='channel_info'><li class = jbox_ta_header><div><em>微信号 ：</em><span>" + qudao_wxNum + "</span></div></li>" +
                            "<li class = jbox_ta_header><div><em>手机号 ：</em><span>" + qudao_mobile + "</span></div></li>" +
                            "<li class = jbox_ta_header><div><em class='info_name'>姓名 ：</em><span>" + qudao_name + "</span></div></li>" +
                            "<li class = jbox_ta_header><div><em>座机号 ：</em><span>" + qudao_phone + "</span></div></li>" +
                            "<input type='hidden' value='" + qudao_agent_Id + "'></ul></div>";
                    };
                };

                //手动搜索模块代码拼接
                channel_html += "<div class = 'jbox_ta_head cnl_account'>" +
                    "<div class='chk_box'><input name='selectRadio' type='radio' ></div>" +
                    "<ul class='channel_name'><li class = jbox_ta_header><b>手动搜索账号</b></li>" +
                    "<li class = jbox_ta_header><span></span><span id='searchBox'><input type='text' name='searchInput' id='searchInput'placeholder='请输入渠道名称'/><a id='searchBtn'/></a><span class='ui-button-icon-primary ui-icon ui-icon-triangle-1-s'></span><ul id='searchResu'></ul></span>" +
                    // "<option value='渠道1'></option><option value='渠道2'></option><option value='渠道3'></option>"+
                    "</li></ul><ul class='channel_info'>" +
                    "<li class = jbox_ta_header><div><em>微信号 ：</em><span class='searchResult_wx'></span><input type='hidden' value=''></div></li>" +
                    "<li class = jbox_ta_header><div><em>手机号 ：</em><span class='searchResult_wx'></span></div></li>" +
                    "<li class = jbox_ta_header><div><em class='info_name'>姓名 ：</em><span class='searchResult_wx'></span><input type='hidden' value=''></div></li>" +
                    "<li class = jbox_ta_header><div><em>座机号 ：</em><span class='searchResult_wx'></span></div></li>" +
                    "</ul></div></div>";
                $(".content-container").append(wx_html).append(channel_html);
                //默认选中第一条数据
                $(".jbox-content .jbox_body input:first").attr("checked", true);
            }

        },
        error: function () {
            alert("error");
        }
    });
}
/**
 * 判断手机号是否为空
 */
function isPhoneEmpty(){
    var phone=$(".channel_info .jbox_ta_header").eq(3).children().children("span");
    if(phone.text() == "-"){
        phone.text("");
    }
}
/**
 * 确认匹配页面
 * @param mobileUser_id 微信用户ID
 * @param agent_id 匹配到的T1用所在渠道ID
 * @param mobileUser_id 匹配到的T1用户 ID
 */
function matchAccount(mobileUser_id, agent_id, sysUser_id) {
    window.location.href = ctx + "/mobileUser/confirmMatchingPage?" + "mobileUserId=" + mobileUser_id + "&agentId=" + agent_id + "&sysUserId" + sysUser_id + "";
}
/**
 * 生成账号页面
 * @param mobileUser_id 微信用户ID
 */
function createAccount(mobileUser_id) {
    window.location.href = ctx + "/quauqAgent/manage/firstForm?mobileUserId="+ mobileUser_id +"";
}
/**
 * 手动搜索框封装函数   created by zhaoli.liu，2017/2/16
 * @param paraInput 需要外部传入的搜索框相关的class名与id名组成的对象
 * @param paraInputSize 需要外部传入的搜索框相关的元素的相关尺寸组成的对象
 */
function searchBox(paraInput,paraInputSize){
    var _searchBox = $("#"+paraInput.box);
    var _searchBtn = $("#"+paraInput.btn);
    var _searchResult = $("#"+paraInput.resu);
    var _searchInput = $("#"+paraInput.inpt);
    var _searchLi = null;
    //设置css样式
    _searchBox.css({"position":"relative","display":"block"});    //  Modified by ruiqi.zhang   bug17370
    _searchBtn.css({
        "position":"absolute",
        "top":"0",
        "z-index":"2",
        "border": "none",
        "border-left": "1px solid #d9d9d9",
        "border-bottom-right-radius": "4px",
        "border-top-right-radius": "4px",
        "border-bottom-left-radius": "0",
        "border-top-left-radius": "0",
        "cursor":"pointer",
        "background": "none",
        "width":paraInputSize.btn_width,
        "height":paraInputSize.btn_height,
        "left":paraInputSize.btn_left,
        "line-height":paraInputSize.input_height
    });
    _searchResult.css({
        "background-color": "#fff",
        "border": "1px solid #d9d9d9",
        "border-radius": "4px",
        "left": "0",
        "display":"none",
        "width": "238px",
        "top":paraInputSize.resu_top,
        "max-height":paraInputSize.resu_maxH
    })
    _searchInput.css({
        "top": "0px",
        "outline": "none",
        "border": "0px",
        "position": "absolute",
        "left": "0px",
        "border": "1px solid #d9d9d9",
        "padding": "0 0 0 4px",
        "border-radius": "4px",
        "width":paraInputSize.input_width,
        "line-height":paraInputSize.input_height,
        "height":paraInputSize.input_height
    })
    _searchInput.siblings("span").css({
        "position": "absolute",
        "left": "223px",
        "top": "7px",
        "background-color": "#ffffff",
    })
    var _data = '';//向后端发送ajax请求时发送的参数，也即input框中输入的value值
    var activeIndex = null;//data对象中数据的条数以及当前active的下标
    var isReturn = false;//ajax函数执行完后更改为true
    //键盘上下箭头和enter键选择下拉选项
    var KEY_ENTER = 13,KEY_UP = 38,KEY_DOWN = 40;
    /**
     * 按钮点击事件，显示或隐藏下拉框  created by zhaoli.liu，2017/2/16
     */
    //另PS：逻辑有问题，注意修改-wlj 20170221,,已经修改-lzl,20170221,
    _searchBtn.bind('click',function(e){
        var evt = e || window.event;
        stopBubble(evt);
        searchAjax(_data)
        if(isReturn){
            _searchResult.slideToggle();
            _searchResult.scrollTop(0);
        }
        scrollBarResizeDelay();
    });
    /**
     * 重置手动搜索滚动条   created by ruiqi.zhang  2017-2-21 18:20:09
     */
        function scrollBarResize(){
            var notVisible = _searchResult.css("display")=='none';
            if (notVisible){
                $(".jbox_body .jbox_ta_head:last-child").removeClass("last_search");
            }else {
                $(".jbox_body .jbox_ta_head:last-child").addClass("last_search");
            }
            $('.jbox_body').getNiceScroll().resize();
        }
    /**
     * 将scrollBarResizeDelay（）函数设置延迟封装成函数，        //为避免搜索下拉列表闪现，加入延时     --by zrq  2017-2-23 18:00:51
     *   created by zhaoli.liu，2017/2/24
     */
        function scrollBarResizeDelay(){
            setTimeout(function () {
                scrollBarResize();
            },500);
        }

    /**
     * input输入框点击聚焦，隐藏下拉框  created by zhaoli.liu，2017/2/16
     */
    _searchInput.bind('click',function(){
        $(this).focus();
        _searchResult.css('display', 'none');
        $('.jbox_body').getNiceScroll().resize();
    })
    /**
     * 点击空白时，下拉框消失  created by zhaoli.liu，2017/2/16
     */
    $(document).bind('click', function(e) {
        var e = e || window.event; //浏览器兼容性
        var elem = e.target || e.srcElement;
        while(elem) { //循环判断至根节点，防止点击的是span子元素
            if(elem.id && (elem.id == paraInput.inpt || elem.id == paraInput.btn|| elem.id == paraInput.resu)) {
                return;
            }
            elem = elem.parentNode;
        }
        _searchResult.css('display', 'none'); //点击的不是span或其子元素
        //点击空白移动下边框
        $(".jbox_body .jbox_ta_head:last-child").removeClass("last_search")
    });
    /**
     * 当点击选中某个li时，呈现在input框中,同时将相应微信号手机号等展示在右侧,
     *   created by zhaoli.liu，2017/2/16
     */
    _searchResult.bind('mouseup',function(evt){
        var event = evt || window.event;
        if(!event.target.id){
            var checkedLi=$(event.target||event.srcElement);
            _searchInput.val(event.target.innerHTML);
            addClass(checkedLi);
            var data_wechatarray=checkedLi.attr("data_wechatarray");
            data_wechatarray = data_wechatarray.split(",");
            var  selfResult=$(".searchResult_wx");
            for (var i=0,j=selfResult.length;i<j;i++){
                selfResult.eq(i).text(data_wechatarray[i]);
            }
            selfResult.eq(0).siblings('input').attr('value',data_wechatarray[4]);
            selfResult.eq(2).siblings('input').attr('value',data_wechatarray[5]);
            _searchResult.css('display', 'none'); //同时让下拉框消失
            $(".jbox_body .jbox_ta_head:last-child").removeClass("last_search");
            $('.jbox_body').getNiceScroll().resize();

        }

    });
    /**
     * 当鼠标在在下拉框中滑动时，下拉选项随鼠标移动而改变背景色
     *   created by zhaoli.liu，2017/2/16
     */
    _searchResult.on('mouseover','li',function(evt){
        stopBubble(evt);
        addClass($(this));
        addFocus($(this));
        scrollBarResizeDelay();
    })
    /**
     * 当input框中value值发生变化时，实时向后端发送数据,
     * 在此做下判断，加入input输入框获取焦点，向后台发送数据为了兼容IE10。modefied by lzl,2017/2/27
     *   created by zhaoli.liu，2017/2/16
     */
        _searchInput.bind('propertychange input',function(evt){
            _data = _searchInput.val();
            var isFocus=$(this).is(":focus");
            if(true==isFocus){
                delayAjax(searchAjax,_data);
            }
        });

    /**
     * 将向后端发送的ajax请求及在页面的展示封装成函数，方便调用
     * @param _data 向后端发送ajax请求时发送的参数，也即input框中输入的value值
     *   created by zhaoli.liu，2017/2/16
     */
   /* function searchAjax(_data){
        var _url =ctx + '/quauqAgent/manage/notBoundList?agentName='+encodeURI(encodeURI(_data));
        $.ajax({
            type:'get',
            url:_url,
            async:true,
            success:function(data){
                if(data){
                    if(data.result==true){
                        if(data.list&&data.list.length !=0){
                            var agentNameArr = [],wechatArr = [],mobileArr = [],contactNameArr = [],contactPhoneArr = [],t1UserIdArr = [],agentIdArr = [];
                            function pushArr(sele,arr){
                                if(sele){
                                    arr.push(sele)
                                }else{
                                    arr.push("");
                                }
                            }
                            for(var i = 0;i<data.list.length;i++){
                                var sele_agentName = data.list[i].agentName;
                                pushArr(sele_agentName,agentNameArr);
                                var sele_wechatCode = data.list[i].wechatCode;
                                pushArr(sele_wechatCode,wechatArr);
                                var sele_contactMobile = data.list[i].contactMobile;
                                pushArr(sele_contactMobile,mobileArr);
                                var sele_contactName = data.list[i].contactName;
                                pushArr(sele_contactName,contactNameArr);
                                var sele_contactPhone = data.list[i].contactPhone;
                                pushArr(sele_contactPhone,contactPhoneArr);
                                var sele_t1UserId = data.list[i].t1UserId;
                                pushArr(sele_t1UserId,t1UserIdArr);
                                var sele_agentId = data.list[i].agentId;
                                pushArr(sele_agentId,agentIdArr);
                            }
                            ajax_data = {
                                "agentNameArr":agentNameArr,
                                "wechatArr":wechatArr,
                                "mobileArr":mobileArr,
                                "contactNameArr":contactNameArr,
                                "contactPhoneArr":contactPhoneArr,
                                "t1UserIdArr":t1UserIdArr,
                                "agentIdArr":agentIdArr
                            }
                            _searchResult.html('');
                            _searchResult.css({
                                "display": ""
                            });
                            var liHtml="";
                            for(var key=0; key<ajax_data.agentNameArr.length;key++){
                                var tempArray=[wechatArr[key],mobileArr[key],contactNameArr[key],contactPhoneArr[key],t1UserIdArr[key],agentIdArr[key]];
                                liHtml+='<li tabindex="0" data_weChatArray="'+tempArray+'">'+ajax_data.agentNameArr[key]+'</li>';
                            }
                            _searchResult.append(liHtml);
                            _searchLi = $("#"+paraInput.resu+" li");
                            _searchLi.css({
                                "height":paraInputSize.resu_liH,
                                "overflow":"hidden",
                                "white-space":"nowrap",
                                "ext-overflow":"ellipsis"
                            });
                        }else{
                            _searchResult.css('display', 'none'); }
                    }else if(data.result==false){
                        alert('请求数据失败，请稍后重试')
                    }
                }
            },
            error:function(xhr,status,statusText){
                alert(xhr.status);
            }
        });
    }*/
//modify by wlj for code optimization at 20170221  -start
    function searchAjax(_data) {
        var liHtml = "";
        var tempArray = [];
        var tempAgent = "";
        var _url = ctx + '/quauqAgent/manage/notBoundList?agentName=' + encodeURI(encodeURI(_data));
        $.ajax({
            type: 'get',
            url: _url,
            success: function (data) {
                if (data && data.result) {
                    var _length = data.list && data.list.length;
                    if (_length) {
                        for (var i = 0; i < _length; i++) {
                            tempAgent = data.list[i]
                            var tempAgentArray = [tempAgent.wechatCode, tempAgent.contactMobile, tempAgent.contactName, tempAgent.contactPhone, tempAgent.t1UserId, tempAgent.agentId];
                            tempArray.push('<li tabindex="0" data_weChatArray="' + tempAgentArray + '">' + tempAgent.agentName + '</li>');
                        }
                        _searchResult.html('');
                        _searchResult.css({
                            "display": ""
                        });
                        _searchResult.append(tempArray.join(" "));
                        _searchLi = $("#" + paraInput.resu + " li");
                        _searchLi.css({
                            "height": paraInputSize.resu_liH,
                            "overflow": "hidden",
                            "white-space": "nowrap",
                            "ext-overflow": "ellipsis"
                        });
                    } else {
                        _searchResult.css('display', 'none');
                    }
                    isReturn = true;
                } else if (!data.result) {
                    _searchResult.css('display', 'none');
                }
            },
            error: function (xhr, status, statusText) {
                alert(xhr.status);
            }
            ,
        })
    }
    //modify by wlj for code optimization at 20170221  -end
    /**
     * 下拉框中列表项的键盘上下键及enter键对选项的选择监听函数e
     * @param e键盘监听事件
     *   created by zhaoli.liu，2017/2/16
     */
    document.onkeydown=function(e){
        if(_searchLi){
            for(var i = 0;i<_searchLi.length;i++){
                if(_searchLi.eq(i).hasClass(paraInput.active)){
                    activeIndex = i;
                    break;
                }
            }
            var _liLast = _searchResult.children().last().index();
            var which = e.which || e.keyCode;
            if (which === KEY_DOWN) {
                if (activeIndex != _liLast) {
                    addFocus(_searchLi.eq(activeIndex+1));
                    addClass(_searchLi.eq(activeIndex+1));
                    activeIndex++;
                } else{
                    activeIndex == _liLast;
                }
                stopDefault(e);
            } else if (which === KEY_UP) {
                if(activeIndex == 0){
                    addFocus(_searchLi.eq(0));
                    addClass(_searchLi.eq(0));
                }else{
                    addFocus(_searchLi.eq(activeIndex-1));
                    addClass(_searchLi.eq(activeIndex-1));
                    activeIndex--;
                }
                stopBubble(e);
                stopDefault(e);
            }else if (which === KEY_ENTER) {
                if(activeIndex != null){
                    var notVisible = _searchResult.css("display")=='none';
                    if (notVisible){
                        activeIndex = null;
                    }else{
                        var current_val = _searchLi.eq(activeIndex).text();
                        var checkedLi = _searchLi.eq(activeIndex);
                        _searchInput.val(current_val);
                        addClass(checkedLi);
                        var data_wechatarray=checkedLi.attr("data_wechatarray");
                        data_wechatarray = data_wechatarray.split(",");
                        var  selfResult=$(".searchResult_wx");
                        for (var i=0,j=selfResult.length;i<j;i++){
                            selfResult.eq(i).text(data_wechatarray[i]);
                        }
                        selfResult.eq(0).siblings('input').attr('value',data_wechatarray[4]);
                        selfResult.eq(2).siblings('input').attr('value',data_wechatarray[5]);
                    }
                    _searchResult.css('display', 'none');

                }
                stopDefault(e);
            }
        }
        scrollBarResizeDelay();
    }
    /**
     * 防抖动延迟执行ajax函数，减少ajax向后端请求次数
     * @param method 要执行的函数
     * @param data 要携带的参数
     *   created by zhaoli.liu，2017/2/16
     */
    function delayAjax(method,data){
        (function () {
            if(method.tId) {
                clearTimeout(method.tId)
            }
            method.tId = setTimeout(function(){
                method(data);
            },350);
        })()
    }
    /**
     * 添加activeclass复用，对选中项添加activeclass，同时兄弟元素去除activeclass
     * @param _domElement 要添加activeclass的dom元素
     *   created by zhaoli.liu，2017/2/16
     */
    function addClass(_domElement){
        _domElement.addClass(paraInput.active).siblings().removeClass(paraInput.active);
    }
    /**
     * 添加焦点复用，对选中项聚焦，同时兄弟元素失去焦点
     * @param _domElement 要聚焦的dom元素
     *   created by zhaoli.liu，2017/2/16
     */
    function addFocus(_domElement){
        _domElement.focus();
        _domElement.siblings().blur();
    }
}
