<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler">
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>代收服务费付款</title>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css">
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/order/payOrRefund.js"></script>
    <script type="text/javascript">
        //如果展开部分有查询条件的话，默认展开，否则收起
        function closeOrExpand(){
            var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
            var searchFormselect = $("#searchForm").find("select");
            var inputRequest = false;
            var selectRequest = false;
            for(var i=0;i<searchFormInput.length;i++) {
                var inputValue = $(searchFormInput[i]).val();
                if(inputValue != "" && inputValue != null) {
                    inputRequest = true;
                    break;
                }
            }
            for(var i=0;i<searchFormselect.length;i++) {
                var selectValue = $(searchFormselect[i]).children("option:selected").val();
                if(selectValue != "" && selectValue != null) {
                    selectRequest = true;
                    break;
                }
            }
            if(inputRequest||selectRequest) {
                $('.zksx').click();
            }
        }
        $(function(){
            //展开、收起筛选
            launch();
            //如果展开部分有查询条件的话，默认展开，否则收起
            closeOrExpand();
            //操作浮框
            operateHandler();
            //计调模糊匹配
            switchNumAndPro();
            $("#settleAgentId" ).comboboxInquiry();
            $("#jd" ).comboboxInquiry();
            $("#salerId" ).comboboxInquiry();
            //$("[name=jd]").comboboxSingle();
            $(document).delegate(".downloadzfpz","click",function(){
                window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
            });

            $("#contentTable").delegate("ul.caption > li","click",function(){
                var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
                $(this).addClass("on").siblings().removeClass('on');
                $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
            });

            $("#contentTable").delegate(".tuanhao","click",function(){
                $(this).addClass("on").siblings().removeClass('on');
                $('.chanpin_cen').removeClass('onshow');
                $('.tuanhao_cen').addClass('onshow');
            });

            $("#contentTable").delegate(".chanpin","click",function(){
                $(this).addClass("on").siblings().removeClass('on');
                $('.tuanhao_cen').removeClass('onshow');
                $('.chanpin_cen').addClass('onshow');
            });

            var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="updateTime DESC";
                $("#orderBy").val(_$orderBy);
            }
            var orderBy = _$orderBy.split(" ");
            $(".activitylist_paixu_left li").each(function(){
                if ($(this).hasClass("li"+orderBy[0])){
                    orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                    $(this).attr("class","activitylist_paixu_moren");
                }
            });

            $(document).scrollLeft(0);
            $("#targetAreaId").val("");
            $("#targetAreaName").val("");
        });
        $().ready(function(){
            //产品名称获得焦点显示隐藏
            $("#wholeSalerKey").focusin(function(){
                var obj_this = $(this);
                obj_this.next("span").hide();
            }).focusout(function(){
                var obj_this = $(this);
                if(obj_this.val()!="") {
                    obj_this.next("span").hide();
                }else
                    obj_this.next("span").show();
            });
            if($("#wholeSalerKey").val()!="") {
                $("#wholeSalerKey").next("span").hide();
            }
        });

        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        $(function(){
            $.fn.datepicker=function(option){
                var opt = {}||option;
                this.click(function(){
                    WdatePicker(option);
                });
            }

            $("#groupOpenDate").datepicker({
                dateFormat:"yy-mm-dd",
                dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
                closeText:"关闭",
                prevText:"前一月",
                nextText:"后一月",
                monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
            });

            $("#groupCloseDate").datepicker({
                dateFormat:"yy-mm-dd",
                dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
                closeText:"关闭",
                prevText:"前一月",
                nextText:"后一月",
                monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
            });
            //首次打印提醒
            $(".uiPrint").hover(function(){
                $(this).find("span").show();
            },function(){
                $(this).find("span").hide();
            })

        });

        $(function(){
            $('.nav-tabs li').hover(function(){
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                if($(this).hasClass('ernav'))
                {
                    if(!$(this).hasClass('current')){
                        $(this).addClass('current');
                        $(this).parent().addClass('nav_current');
                    }
                }
            },function(){
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                var _active = $(".totalnav .active").eq(0);
                if(_active.hasClass('ernav')){
                    _active.addClass('current');
                    $(this).parent().addClass('nav_current');
                }
            });
            //展开收起搜索条件
            //操作浮框
            operateHandler();
            //输入金额提示
            inputTips();
        });

        function sortby(sortBy,obj){
            var temporderBy = $("#orderBy").val();
            if(temporderBy.match(sortBy)){
                sortBy = temporderBy;
                if(sortBy.match(/ASC/g)){
                    sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
                }else{
                    sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
                }
            }else{
                sortBy = sortBy+" DESC";
            }

            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }
        function query() {
            $('#searchForm').submit();
        }

        Date.prototype.Format = function(fmt)
        { //author: meizz
            var o = {
                "M+" : this.getMonth()+1,                 //月份
                "d+" : this.getDate(),                    //日
                "h+" : this.getHours(),                   //小时
                "m+" : this.getMinutes(),                 //分
                "s+" : this.getSeconds(),                 //秒
                "q+" : Math.floor((this.getMonth()+3)/3), //季度
                "S"  : this.getMilliseconds()             //毫秒
            };
            if(/(y+)/.test(fmt))
                fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
            for(var k in o)
                if(new RegExp("("+ k +")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            return fmt;
        }
        function expand(child,obj,id,type,index,orderType,orderId,chargeType) {
            $.ajax({
                url:"${ctx}/orderCommon/manage/refundPayInfo/",
                type:"POST",
                data:{id:id,type:type,orderType:orderType},
                success:function(data){
                    var htmlstr=""
                    var num = data.length;
                    if(num>0){
                        for(var i=0;i<num;i++){

                            var payOrder = '';//支付凭证
                            var payOrderArray = data[i].payvoucher.split("|")
                            for(var j=0;j<payOrderArray.length;j++){
                                payOrder+=payOrderArray[j]+"<br/>"
                            }

                            htmlstr+="<tr><td class='tc'>"+data[i].label+"</td><td class='tc'>"+data[i].amount+
                                    "</td><td class='tc'>"+(new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss")
                                    +"</td><td class='tc'>"+data[i].a+
                                    "</td><td class='tc'>"+payOrder+"</td><td class='tc'>"+data[i].opstatus+
                                    "</td><td class='tc'>";
                            if(data[i].opstatus=='已支付'){
                                htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>" +
                                        "&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\" >撤销</a>&nbsp;&nbsp;";
                                <shiro:hasPermission name="charge:operation:print">
                                htmlstr+="<a href=\"${ctx}/finance/serviceCharge/getPrintPaymentInfo/" + orderId + "/" + chargeType + "\" target=\"_blank\">打印</a>";
                                </shiro:hasPermission>
                                htmlstr+="</td></tr>";
                            }else{
                                htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;</td></tr>"
                            }
                        }
                    }else{
                        htmlstr+="<tr><td colspan='7' style='text-align: center;'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td></tr>"
                    }
                    $("#rpi_" + id + "_" + index).html(htmlstr);
                }
            });
            if ($(child).is(":hidden")) {
                $(obj).html("收起");
                $(obj).parents("tr").addClass("tr-hover");
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
            }else{
                if (!$(child).is(":hidden")) {
                    $(obj).parents("tr").removeClass("tr-hover");
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    $(obj).html("支付记录");
                }
            }
        }

        function cancelOper(ctx,str,orderType,obj){
            $.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&orderType="+orderType,{
                title: "付款记录",
                width:830,
                height: 500,
                buttons:{'撤销': 1,'关闭':0},
                persistent:true,
                loaded: function(h){},
                submit:function(v,h,f){
                    if(v==1){
                        var refundId =  $(h.find("iframe")[0].contentWindow.refundId).val();
                        var recordId =  $(h.find("iframe")[0].contentWindow.recordId).val();
                        $.ajax({
                            type:"GET",
                            url:ctx+"/refund/manager/undoRefundPayInfo",
                            dataType:"json",
                            data:{refundId:refundId,recordId:recordId},
                            success:function(data){
                                if(data.flag=='ok'){
                                    var payed = $(obj).parent().parent().parent().parent().parent().parent().prev().find('.dzje_dd .fbold').html();
                                    var temp1 = payed.match(/\d+/g);
                                    var numpayed = temp1[0] + '.' + temp1[1];

                                    var cny = payed.replace(numpayed,'');

                                    var onepayed = $(obj).parent().prev().prev().prev().prev().prev().html();
                                    var temp2 = onepayed.match(/\d+/g);
                                    var numonepayed = temp2[0] + '.' + temp2[1];

                                    var result = (parseFloat(numpayed).toFixed(2)-parseFloat(numonepayed).toFixed(2)).toFixed(2);

                                    if(result == 0) {
                                        $(obj).parent().parent().parent().parent().parent().parent().prev().find('.dzje_dd .fbold').html("");
                                    }else{
                                        $(obj).parent().parent().parent().parent().parent().parent().prev().find('.dzje_dd .fbold').html(cny + result);
                                    }

                                    $(obj).parent().prev().html('已撤销');
                                    $(obj).next().remove();   //移除打印按钮
                                    $(obj).remove();

                                }
                            }

                        })
                    }
                }
            }).find("#jbox-content").css("overflow", "hidden");
        }

        function cancelDetailOper(ctx,refundId,recordId,obj){
            $.ajax({
                type:"GET",
                url:ctx+"/refund/manager/undoRefundPayInfo",
                dataType:"json",
                data:{refundId:refundId,recordId:recordId},
                success:function(data){
                    if(data.flag=='ok'){
                        $(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
                    }
                }});
        }

        /**
         * 查询条件重置
         *
         */
        var resetSearchParams = function(){
            var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
            var selectArray = $('#searchForm').find("select");
            for(var i=0;i<inputArray.length;i++){
                if($(inputArray[i]).val()){
                    $(inputArray[i]).val('');
                }
            }
            for(var i=0;i<selectArray.length;i++){
                var selectOption = $(selectArray[i]).children("option");
                $(selectOption[0]).attr("selected","selected");
            }
        }

    </script>

    <!--//批量付款的处理-->
    <script type="text/javascript">
        $(document).on('change','#contentTable,#contentTable_foot, [type="checkbox"]',function(){
            var $this = $(this);
            var $contentContainer = $("#contentTable,#contentTable_foot");
            var $all =$contentContainer.find('[check-action="All"]');
            var $reverse = $contentContainer.find('[check-action="Reverse"]');
            var $chks=$contentContainer.find('[check-action="Normal"]:enabled');
            if($this.is('[check-action="All"]')){
                if($this.is(':checked')){
                    $chks.attr('checked',true);
                }else{
                    $chks.removeAttr('checked');
                }
            }
            if($this.is('[check-action="Reverse"]')){
                $chks.each(function(){
                    var $chk = $(this);
                    if($chk.is(':checked')){
                        $chk.removeAttr('checked');
                    }else{
                        $chk.attr('checked',true);
                    }
                });
            }
            if($chks.length && ($chks.length ==$chks.filter(':checked').length)){
                $all.attr('checked',true);
            }else{
                $all.removeAttr('checked');
            }
        });

        /**
         * 批量打印按钮处理方法
         * @returns {boolean}
         */
        function batchPrint(){
            var inputs=$(".box:checked");
            if(inputs.length == 0){
                $.jBox.tip('请选择数据','warnning');
                return false;
            }
            var datas = getData(inputs);
            $("#printInfo").val(JSON.stringify(datas));
            $("#printForm").submit();
        }
    </script>
    <style type="text/css">
        .ipt-tips {
            position: absolute;
            left: 0px;
            top: 0px;
            white-space: nowrap;
            color: #b2b2b2;
            z-index: 1;
        }
        a {
            display: inline-block;
        }

        label {
            cursor: inherit;
        }
        form input {
            box-sizing: content-box;
        }
    </style>
</head>
<body>
<c:set var="showType" value="205" />
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    <form id="searchForm" action="${ctx}/finance/serviceCharge/list" method="post">
        <input id="pageNo" name="pageNo" type="hidden" value="" />
        <input id="pageSize" name="pageSize" type="hidden" value="" />
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
        <!--<div class="order_bill"></div>-->
        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
                <input id="groupCode" name="groupCode" class="txtPro inputTxt searchInput" value="${param.groupCode}" placeholder="请输入团号"/>
            </div>
            <div class="zksx">筛选</div>
            <div class="form_submit">
                <input class="btn btn-primary ydbz_x" type="button"	onclick="query()" value="搜索" />
                <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
            </div>
            <div class="ydxbd">
                <span></span>
                <!-- 504 筛选条件 S -->
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">结算方：</div>
                    <select name="settleAgentId" id="settleAgentId">
                        <option value="">全部</option>
                        <option value="-3" <c:if test="${param.settleAgentId==-3}">selected="selected"</c:if>>QUAUQ</option>
                        <c:forEach var="agent" items="${agentList }">
                            <option value="${agent.id }" <c:if test="${param.settleAgentId==agent.id}">selected="selected"</c:if>>${agent.agentName }</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">计调：</label>
                    <select name="jd" id="jd">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach var="item" items="${jdList }">
                            <option value="${item.id }" <c:if test="${param.jd==item.id}">selected="selected"</c:if>>${item.name }</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">付款状态：</div>
                    <div class="selectStyle">
                        <select name="payStatus">
                            <option selected="selected" value="">请选择</option>
                            <option value="0" <c:if test="${param.payStatus eq '0'}">selected="selected"</c:if>>未付款</option>
                            <option value="1" <c:if test="${param.payStatus eq '1'}">selected="selected"</c:if>>已付款</option>
                        </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">付款金额：</div>
                    <input class="inputTxt inputTxtlong" name="payMoneyBegin" value="${param.payMoneyBegin }" oninput="refundInput(this)" type="text">
                    ~
                    <input class="inputTxt inputTxtlong" name="payMoneyEnd" value="${param.payMoneyEnd }"oninput="refundInput(this)" type="text">
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">团队类型：</div>
                    <div class="selectStyle">
                        <select name="orderType" id="orderType">
                            <option selected="selected" value="">全部</option>
                            <option <c:if test="${param.orderType eq 2}">selected="selected"</c:if> value="2">散拼</option>
                            <%--<c:forEach var="type" items="${orderTypes }">--%>
                                <%--<c:if test="${type.value ne 0}">--%>
                                    <%--<option value="${type.value }" <c:if test="${param.orderType==type.value}">selected="selected"</c:if>>${type.label }</option>--%>
                                <%--</c:if>--%>
                            <%--</c:forEach>--%>
                        </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">销售：</label>
                    <select name="salerId" id="salerId">
                        <option value="" selected="selected" id="salerrefundRebate">请选择</option>
                        <c:forEach var="item" items="${salerList }">
                            <option value="${item.id }" <c:if test="${param.salerId==item.id}">selected="selected"</c:if>>${item.name }</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">打印状态：</div>
                    <div class="selectStyle">
                        <select name="printStatus">
                            <option value=''>请选择</option>
                            <option value="0" <c:if test="${param.printStatus eq '0' }">selected="selected"</c:if>>未打印</option>
                            <option value="1" <c:if test="${param.printStatus eq '1' }">selected="selected"</c:if>>已打印</option>
                        </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">出纳确认时间：</div>
                    <input class="inputTxt dateinput" value="${param.cashierConfirmDateBegin }" id="cashierConfirmDateBegin" name="cashierConfirmDateBegin" readonly onclick="WdatePicker()" onchange="isnull()" type="text">
                    至
                    <input class="inputTxt dateinput" value="${param.cashierConfirmDateEnd }" id="cashierConfirmDateEnd"  name="cashierConfirmDateEnd" readonly onclick="WdatePicker()" onchange="isnull()" type="text">
                </div>
                <!-- 504 筛选条件 E -->
            </div>
        </div>
    </form>

    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
            <div class="activitylist_paixu_left">
                <ul>
                    <li class="activitylist_paixu_left_biankuang licreateTime">
                        <a onClick="sortby('createTime',this)">创建时间</a>
                    </li>
                    <li class="activitylist_paixu_left_biankuang liupdateTime">
                        <a onClick="sortby('updateTime',this)">更新时间</a>
                    </li>
                </ul>
            </div>
            <div class="activitylist_paixu_right">
                查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
            </div>
            <div class="kong"></div>
        </div>
    </div>

    <table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
            <tr>
                <th width="3%">序号</th>
                <th width="10%">结算方</th>
                <th width="6%">团队类型</th>
                <th width="10%">团号</th>
                <th width="10%">团队名称</th>
                <th width="8%">计调</th>
                <th width="8%">订单号</th>
                <th width="8%">
                    <span class="tuanhao on">销售</span>/
                    <span class="chanpin">下单人</span>
                </th>
                <th width="10%">付款金额<br>已付金额</th>
                <th width="6%">出纳确认</th>
                <th width="6%">出纳确认时间</th>
                <th width="6%">打印确认</th>
                <th width="6%">操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="item" varStatus="s">
            <tr>
                <td class="tc">
                	<input type="checkbox" check-action="Normal" class="box" value="${item.orderId }" payStatus="${item.payStatus}" <%--<c:if test="${item.payStatus eq '1' }">disabled="disabled"</c:if>--%>/>
                	<input type="hidden" check-action="Normal" value="${item.chargeType }"/>
                	${s.count}
                </td>
                <td class="tc settlement">${item.settleName}</td>
                <td class="tc">${fns:getDictLabel(item.orderType, "order_type", "")}</td>
                <td class="tc groupId">${item.groupCode}</td>
                <td class="tc">${item.groupName}</td>
                <td class="tc">${item.jd}</td>
                <td class="tc orderId">${item.orderNum}</td>
                <td class="tc">
                    <div class="tuanhao_cen onshow" title="${item.saler}">${item.saler}</div>
                    <div class="chanpin_cen qtip" title="${item.creator}">${item.creator}</div>
                </td>
                <td class="p0 tr">
                    <div class="yfje_dd">
                        <span class="fbold">${item.payMoney}</span>
                    </div>
                    <div class="dzje_dd">
                        <span class="fbold">${item.accountedMoney}</span>
                    </div>
                </td>
                <td class="tc">
                    <c:if test="${item.payStatus eq '0' }">
                        <font style="color:red">未付</font>
                    </c:if>
                    <c:if test="${item.payStatus eq '1' }">
                        <font style="color:green">已付</font>
                    </c:if>
                </td>
                <td class="tc">
                	<c:if test="${item.payStatus eq '1' }">
                		<fmt:formatDate value="${item.payTime }" pattern="yyyy-MM-dd"/>
                	</c:if>	
                </td>
                <td class="tc">
                    <c:if test="${empty item.printStatus || item.printStatus == 0 }">
                        <font style="color:green">未打印</font>
                    </c:if>
                    <c:if test="${item.printStatus == 1 }">
                        <p class="uiPrint">
                            <font style="color:green">已打印</font>
                            <span style="display: none;color:purple;">首次打印时间<br>
                            <fmt:formatDate value="${item.printTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                        </p>
                    </c:if>
                </td>
                <td class="p0">
                    <dl class="handle">
                        <dt>
                            <img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png">
                        </dt>
                        <dd>
                            <p>
                                <span></span>
                                <a href="${ctx}/finance/serviceCharge/pay/204?orderType=${item.orderType}&orderId=${item.orderId}&currencyId=${item.currencyIds}&payPrice=${item.amounts}&serviceChargeId=${item.chargeId}&moneyType=${item.chargeType + 14}&chargeType=${item.chargeType}&agentId=${item.agentId}" target="_blank">付款 </a>
                                <c:choose>
                                    <c:when test="${item.orderType < 6 || item.orderType == 10}">
                                        <a href="${ctx }/costReview/activity/activityCostReviewDetail/${item.productId }/${item.groupId }/3?read=1" target="_blank">查看</a>
                                    </c:when>
                                    <c:when test="${item.orderType == 6 }">
                                        <a href="${ctx }/costReview/visa/visaCostReviewDetail/${item.productId }/3?read=1" target="_blank">查看</a>
                                    </c:when>
                                    <c:when test="${item.orderType == 7 }">
                                        <a href="${ctx }/costReview/airticket/airticketCostReviewDetail/${item.productId }/3?read=1" target="_blank">查看</a>
                                    </c:when>
                                </c:choose>
                                <shiro:hasPermission name="chargepay:operation:statement">
                                    <c:if test="${not empty item.groupId}">
                                        <a href="${ctx}/cost/manager/forcastList/${item.groupId}/${item.orderType}" target="_blank">预报单</a>
                                    </c:if>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="chargepay:operation:finalStatement">
                                    <c:if test="${not empty item.groupId}">
                                        <a href="${ctx}/cost/manager/settleList/${item.groupId}/${item.orderType}" target="_blank">结算单</a>
                                    </c:if>
                                </shiro:hasPermission>
                                <a onclick="expand('#child1_${item.chargeId}_${s.count}',this,${item.chargeId},${item.chargeType + 14},${s.count},${item.orderType},${item.orderId},${item.chargeType})" href="javascript:void(0)">支付记录</a>
                                <c:if test="${item.payStatus eq '0' }">
                                	<a onclick="jyPaymentConfirm(this, '${ctx}', '${item.orderId }', '${item.chargeType }')" href="javascript:void(0)">确认付款</a>
                                </c:if>
                                <c:if test="${item.payStatus eq '1' }">
                                	<a onclick="paymentCancel('${ctx}', '${item.orderId }', '${item.chargeType }')" href="javascript:void(0)">撤销付款</a>
                                </c:if>
                                <shiro:hasPermission name="charge:operation:print">
                                    <a href="${ctx}/finance/serviceCharge/getPrintPaymentInfo/${item.orderId}/${item.chargeType}" target="_blank">打印</a>
                                </shiro:hasPermission>
                            </p>
                        </dd>
                    </dl>
                </td>
            </tr>
            <tr id="child1_${item.chargeId}_${s.count}" class="activity_team_top1" style="display:none">
                <td colspan="15" class="team_top">
                    <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                        <thead>
                        <tr>
                            <th class="tc" width="10%">付款方式</th>
                            <th class="tc" width="10%">金额</th>
                            <th class="tc" width="15%">日期</th>
                            <th class="tc" width="10%">支付类型</th>
                            <th class="tc" width="25%">支付凭证</th>
                            <th class="tc" width="10%">状态</th>
                            <th class="tc" width="15%">操作</th>
                        </tr>
                        </thead>
                        <tbody id="rpi_${item.chargeId}_${s.count}">
                        </tbody>
                    </table>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form method="post" action="${ctx}/finance/serviceCharge/getBatchPrintInfo" id="printForm" target="_blank">
        <input id="printInfo" name="printInfo" type="hidden" value=""/>
    </form>

<div class="page" id="contentTable_foot">
    <div class="pagination">
        <dl>
            <dt><input check-action="All" type="checkbox">全选</dt>
            <dt><input check-action="Reverse" type="checkbox">反选</dt>
            <dd>
                <input type="button" class="btn ydbz_x" onclick="jyPaymentConfirm(this,'${ctx}',1,1)"value="批量确认付款">
                <shiro:hasPermission name="charge:operation:print">
                    <input type="button" class="btn ydbz_x" value="批量打印" onclick="batchPrint()" />
                </shiro:hasPermission>
            </dd>
        </dl>
    </div>

    <div class="pagination clearFix">
        ${page}
    </div>
</div>
<!-- 504 确认付款弹窗 S -->
<div id="payment_confirm_pop_o" class="display-none">
    <div class="payment_confirm_pop">
        <table class="table activitylist_bodyer_table " style="margin:0 auto;">
            <thead>
            <tr>
                <th class="tc" width="14%">结算方</th>
                <th class="tc" width="7%">团号</th>
                <th class="tc" width="8%">订单号</th>
                <th class="tc" width="10%">付款金额</th>
                <th class="tc" width="10%">
                	出纳确认时间
                	<div id="div1" class="display-none">
                	<br/>
                	<input name='confirmDate' readonly class='dateinput'   onclick='WdatePicker()'><br/>
                	<button style="padding:0px 10px 0px 10px;" onclick ="copy(this)">复制</button>
                	</div>
                </th>
            </tr>
            </thead>
            <tbody id="paymentConfirmList"></tbody>
        </table>
    </div>
</div>
</div>
</body>
</html>
