<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>批量归还切位</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />

    <script src="${ctxStatic}/js/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery-migrate-1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <style type="text/css">
        .cl-tt-btn{
            display: inline-block;
            background-color: #e5f5ff;
            border: 1px solid #afd5ff;
            box-shadow: none;
            border-radius: 2px;
            color:#00a2ff;
            margin-left:3px;
            height: 20px;
            min-width: 70px;
            padding: 0px;
        }
        .ydtExpand {
            background-position: 0px 0px;
            display: block;
            width: 12px;
            height: 12px;
            position: absolute;
            top: 11px;
            left: 12px;
            cursor: pointer;
            background-image: url("${ctxStatic}/images/yd-close-target.png");
        }
        .ydtClose {
            background-position: 0px -18px;
            display: block;
            width: 12px;
            height: 12px;
            position: absolute;
            top: 11px;
            left: 12px;
            cursor: pointer;
            background-image: url("${ctxStatic}/images/yd-close-target.png");
        }
        .ydBtn {
            PADDING-BOTTOM: 10px;
            MARGIN-TOP: 50px;
            WIDTH: 278px;
            HEIGHT: 28px;
            MARGIN-LEFT: auto;
            MARGIN-RIGHT: auto
        }
        tr.error,
        tr.error input,
        tr.error select
        {
            color: #ff3434;
        }
        .ui-autocomplete.ui-menu.ui-front {
            z-index: 10000;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            //搜索条件筛选
            launch();
            //操作浮框
            operateHandler();
            //团号和产品切换
            switchNumAndPro();
            //展开收起渠道
            closeOrExpand();
        })
    </script>
    <script type="text/javascript">

        function returnMain(){
            window.location.href="${ctx}/stock/manager/apartGroup?_m=147&_mc=243";
        }
        
        $(document).on('click', '[name="addChannel"]', function () {
        	var $channelSelect = $('#popCutChannelSelect').clone();
            var channelCodes = [];
            $(document).find("div.channel-cut").each(function(){
            	channelCodes.push($(this).attr("channel-code"));
            });
             	
            for(var i=0; i<channelCodes.length; i++) {
             	$channelSelect.find("option[value="+channelCodes[i]+"]").remove();
            }
            
        	var $pop = $.jBox($channelSelect.html(), {
                title: "选择渠道", buttons: {'添加': 1, '取消': 2}, submit: function (v, h, f) {
                    if (v == 1) {
                    	var channels =[];
                        var agentinfoIds = [];

                        //渠道选择校验,去重操作
                        var validate = true;
                        var channelName = '';
                        $pop.find('.selected-channel .channel-text').each(function () {
                            var code = $(this).attr('channel-code');
                            channelName = $(this).attr('channel-name');
                            $("#modForm").find(".channel-cut").each(function(){
                            	if($(this).find("span[name=channelCode]").text() == code) {
                            		validate = false;
                            		return false;
                            	}
                            });
                            if(!validate) {
                            	return false;
                            }
                            agentinfoIds.push(code);
                        });

                        if(!validate) {
                        	$.jBox.tip("已经包含该（"+ channelName +"）渠道信息，请重新输入！");
                        	return false;
                        }

                        $.ajax({
                    		type: "POST",
                    	   	url: "${ctx}/agent/manager/getAgentinfoJsonBean",
                    	   	async: false,
                    	   	data: {
                    				"agentinfoIds":agentinfoIds.join(',')
                    			  },
                    		dataType: "json",
                    	   	success: function(data){
                    	   		if(data){
                    	   			channels = data;
                    	   		}
                    	   	}
                    	});
                        
                        for(var i= 0,len=channels.length;i<len;i++){
                        	var salerUsersJson = channels[i].agentSalerUser;
                            salerUsersJson = "'" + salerUsersJson + "'";
		                    var salerUsers = eval(eval(salerUsersJson));
		                 	var salerOptions = new Array();
		                 	for ( var j = 0; j < salerUsers.length; j++) {
								var newOpt = new Option(salerUsers[j].name, salerUsers[j].id);
								salerOptions.push(newOpt);
							}
							channels[i].salerOptions = salerOptions;
                            addCutChannel(channels[i]);
                        }
                    }
                }, height: 320, width: 380
            });
            $pop.on('click', '.channel-remove', function () {
                $(this).parent().remove();
            });
            var $popAgentId = $pop.find('#popAgentId').comboboxInquiry();
            $popAgentId.on('comboboxinquiryselect', function () {
                var channelCode = $popAgentId.val();
                var channelName = $popAgentId.find('option:selected').text();
                
                if (!$pop.find('.selected-channel [channel-code="' + channelCode + '"]').length) {
                    var channelText = '<span class="channel-text" channel-code="' + channelCode + '">' + '<span>' + channelName + '</span>' +
                            '<span class="channel-remove">x</span>' +
                            '</span>';
                    $pop.find('.selected-channel').append(channelText);
                }
                $popAgentId.val('');
                setTimeout(function () {
                    $pop.find('.ui-autocomplete-input').val('');
                }, 100);

            });
        });

        $(document).on('click','[name="removeChannel"]',function(){
            var $this = $(this);
            $.jBox.confirm('确定要删除该渠道吗？','系统提示',function(v,h,f){
                if(v==='ok'){
                    var $channelArea = $this.parents('.channel-cut:first');
                    var hr = $channelArea.next('hr');
                    if(hr == null || hr == undefined || hr.length == 0) {
                    	hr = $channelArea.prev('hr');
                    }
                    hr.remove();
                    
                    var channelCode = $channelArea.attr('channel-code');
                    $channelArea.remove();
                    delete channellist[channelCode];
                }
            });
        })

        $(document).on('click','[name="openGroupList"]',function(){
        	var $this = $(this);
            
            var selectedProductArr = [];
            $(this).parents('.channel-cut:first').find("table tr:visible input[name=groupId]").each(function(){
            	selectedProductArr.push($(this).val());
            });
            var agentId = $(this).parents('.channel-cut:first').attr("channel-code");
            
            var $pop=  $.jBox("iframe:${ctx}/stock/manager/apartGroup/getProductGroupPage?source=isReturn&agentId="+agentId+"&selectedProducts="+selectedProductArr.join(','),{
                title: "产品团期列表",
                width: 1200,
                height: 500,
                buttons: {'提交':1,'取消':0},
                submit: function (v, h, f) {
                    if(v==1){
                        var iframe = $pop.find('iframe').get(0);
                        var $content = $(iframe.contentDocument);
                        var products = [];

                        $content.find('input[type="checkbox"]:checked').each(function(){
                            var $this = $(this);
                            var $row = $this.parents('[name="groupRow"]:first');
                            if($row.length>0 && $this.attr('disabled')!='disabled'){
                                var $tds = $row.find('td');
                                var product = {
                                    groupNo: $($tds.get(1)).text(),
                                    productName: $($tds.get(2)).text(),
                                    startDate: $($tds.get(4)).text(),
                                    cutCount: $($tds.get(7)).text(),
                                    surplusCount: $($tds.get(6)).text(),
                                    advanceCount: $($tds.get(8)).text(),
                                    groupId: $($tds.get(9)).find("input[name=id]").val(),
                                    soldPayPosition: $($tds.get(9)).find("input[name=soldPayPosition]").val()
                                }
                                products.push(product);
                            }
                        });
                        if(products.length==0){
                            $.jBox.tip('请选择团期！', 'error');
                            return false;
                        }
                        cloneRows($this,products);
                        $this.parent().find('.closeOrExpand').addClass("ydtExpand").removeClass("ydtClose");
                    }else{
                        return true;
                    }
                },
                loaded:function(h){
                    var iframe = $pop.find('iframe').get(0);
                    var $content = $(iframe.contentDocument);
                    $content.find('.activitylist_bodyer_right_team_co_paixu').show();
                    $content.find('form').show();
                }
            }).find("#jbox-content").css("overflow","hidden");
        });

        function cloneRows(obj,products){
            var channelCode = obj.parents('.channel-cut:first').attr('channel-code');
            obj.parents('.channel-cut:first').find('.toggle:first').show();
            for(var i= 0,len=products.length;i<len;i++){
                var index = obj.parents('.channel-cut:first').find('tbody tr').length;
                var $tr = obj.parents('.channel-cut:first').find('tbody tr:first').clone();
                $tr.css('display','');
                $tr.attr("name","product");
                var $tds = $tr.find("td");
                var product = products[i];
                var cIndex = 0;
                $tds.eq(cIndex).find("span[name=index]").html(index);
                $tds.eq(cIndex).find("input[name=groupId]").val(product.groupId);
                //团号
                cIndex++;
                $tds.eq(cIndex).html(product.groupNo);
                //产品名称
                cIndex++;
                $tds.eq(cIndex).html(product.productName);
                //当前余位数
                cIndex++;
                $tds.eq(cIndex).html(product.surplusCount);
                //切位数
                cIndex++;
                $tds.eq(cIndex).html(product.cutCount);
                //已售切位
                cIndex++;
                $tds.eq(cIndex).html(product.soldPayPosition);
                
                obj.parents('.channel-cut:first').find('tbody').append($tr.prop("outerHTML"));
                channellist[channelCode].push(product.groupNo);
            }
        }


        function closeOrExpand(){
            $(document).on('click','.closeOrExpand',function(){
                if ($(this).is(".ydtClose")) {
                    $(this).parents('.channel-cut').find('.toggle:first').show();
                    $(this).addClass("ydtExpand");
                    $(this).removeClass("ydtClose");
                } else {
                    $(this).parents('.channel-cut').find('.toggle:first').hide();
                    $(this).addClass("ydtClose");
                    $(this).removeClass("ydtExpand");
                }
            })
        }


        //本界面展示的渠道列表
        var channellist = {};
        /**
         * 创建切位渠道的方法
         */
        function addCutChannel(channel){
            var $channelView = $(".channel-cut").clone().show();
            $channelView.find('span[name="channelName"]').text(channel.channelName);
            $channelView.find('select[name="salerName"]').append(channel.salerOptions);
            $channelView.attr('channel-code',channel.channelCode);
            if($(".channel-cut").length>1){
                $("#modForm").append('<hr style="border-style: solid;border-color: #cccccc">')
            }
            $("#modForm").append($channelView.prop('outerHTML'));
            channellist[channel.channelCode] = [];
        }
        /**
         * 创建切位渠道
         */
        $(function(){
        	var channels = [];
            if('${agentJsonInfos}' != '') {
            	channels = jQuery.parseJSON('${agentJsonInfos}');
            }
            for(var i=0,len=channels.length;i<len;i++){
            	var salerUsersJson = channels[i].agentSalerUser;
                var salerOptions = new Array();
                var salerUsers = eval(salerUsersJson);
                for ( var j = 0; j < salerUsers.length; j++) {
					var newOpt = new Option(salerUsers[j].name, salerUsers[j].id);
					salerOptions.push(newOpt);
				}
				channels[i].salerOptions = salerOptions;
                addCutChannel(channels[i]);
            }
        });

        function showAll() {
            $(".grouprow").each(function () {
                $(this).show();
            });
        }
        $(document).ready(function () {
            $("#btnSubmit").removeAttr("disabled");
            agendChoose();
            var datepicker = $(".groupDate").datepickerRefactor(
                    {
                        dateFormat: "yy-mm-dd",
                        target: "#dateList",
                        numberOfMonths: 3,
                        isChickArr: getChickList(),
                        defaultDate: '2016-05-08'
                    }, "#groupOpenDate", "#groupCloseDate");
            //表单验证
            $("#modForm").validate({
                rules: {
                    agentId: "required"
                },
                messages: {
                    agentId: "请选择一个切位渠道"
                },
                errorPlacement: function (error, element) {
                    if (element.is(":radio"))
                        error.appendTo(element.parent());
                    else if (element.is(":checkbox"))
                        error.appendTo(element.parent());
                    else if (element.is("input"))
                        error.appendTo(element.parent());
                    else
                        error.insertAfter(element);
                }
            });


            $(".selectGroupId").click(function () {
                if ($(this).text() == '选择全部') {
                    $('.cancelGroupId').attr('checked', 'true');
                    $(this).text('取消全部');
                } else {
                    $('.cancelGroupId').removeAttr('checked');
                    $(this).text('选择全部');
                }
            });
        });

        jQuery.extend(jQuery.validator.messages, {
            required: "必填信息",
            digits: "请输入正确的数字",
            number: "请输入正确的数字价格"
        });
        function agentIdChange(selectdata) {
            window.location.href = '/a/stock/manager/apartGroup/reserve?id=9678&agentId=' + selectdata + '&activityGroupId=10619';
        }
        //查询所选择的渠道信息
        function choseAgent(agentId) {

            var selectdata = null;
            if (agentId) {
                selectdata = agentId;
            } else {
                selectdata = $("#agentIdIn option:selected").val();
                agentIdChange(selectdata);
            }
            $.ajax({
                type: "POST",
                url: "/a/stock/manager/apartGroup/agentInfo",
                data: {
                    agentId: selectdata
                },
                success: function (msg) {
                    $(msg).each(function (index1, obj1) {
                        $(".agentInfo").html("渠道商名称：" + obj1.agentName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;接口销售员：" + (obj1.agentSalerName == null ? '暂无' : obj1.agentSalerName) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系电话：" + (obj1.salerMobile == null ? '暂无' : obj1.salerMobile));
                    });
                }
            });
            $(".jbox-close").click();
            $(".agentId").val(selectdata);
        }

        function quitAgent() {
            $(".jbox-close").click();
        }
        function delGroupDate(obj) {
            $(document).delGroup1(obj);

            var divobj = $(obj).parent().parent();
            $(divobj).remove();
        }
        function delgroup(obj) {
            $(obj).parent().parent().remove();
        }
        function maxVali(obj) {
            if ($("#payReservePosition").val() != null && $("#payReservePosition").val() != "") {
                $("#btnSubmit").removeAttr("disabled");
            }
            var old = $(obj).parent().prev().val();
            var newVal = $(obj).val();
            var freePosition = $(obj).parent().next().text();
            if (newVal - old > freePosition) {
                return false;
            } else {
                return true;
            }
        }
        jQuery.validator.addMethod("intChange", function (value, element) {
            return this.optional(element) || maxVali(element);
        }, "余位不足");
        jQuery.validator.addMethod("isZero", function (value, element) {
            return this.optional(element) || !($(element).val() == 0)
        }, "无切位人数");

        function getChickList() {
            var d = 1;
            var dateArr = new Array();
            for (var i = 1; i <= d; i++) {
                var beforDate = $("#grouprow" + i).find(".leftdays").val();
                if (beforDate < 0) {
                    var date = ($("#grouprow" + i + " td").eq(1).text()).split("-");
                    dateArr.push(new Date(date[0], date[1] - 1, date[2]));
                }
            }
            return dateArr;
        }
        function upload(activityGroupId) {
            if ($('#agentId').val() == '') {
                $.jBox.tip('请先选择切位渠道！', 'error');
                return false;
            }
            var iframe = "iframe:/a/stock/manager/apartGroup/uploadform?srcActivityId=9678&agentId=" + $('#agentId').val() + "&activityGroupId=" + activityGroupId;
            $.jBox(iframe, {
                title: "支付凭证",
                width: 580,
                height: 460,
                buttons: {}
            });
            return false;
        }
        function downloads(docid, activitySerNum, groupCode, acitivityName, iszip) {
            if (iszip) {
                var zipname = activitySerNum + '-' + groupCode;
                window.open("/a/sys/docinfo/zipdownload/" + docid + "/" + zipname);
            }

            else
                window.open("/a/sys/docinfo/download/" + docid);
        }

        function cancelGroup() {
            $(".cancelGroupId").each(function () {
                if ($(this).attr("checked")) {
                    $(this).parent().parent().hide();
                    $(this).removeAttr("checked");

                }
            });
            $(".selectGroupId").text('选择全部');
        }

        function agendChoose() {
            if (false) {
                var _select = $("#agentId").clone();
                _select.attr("id", "agentIdIn");
                _select.attr("name", "agentIdIn");
                _select.unbind();
                var $select = $('<p></p>').append(_select);
                var html = '<div id="chooseQd" class="tanchukuang"><div class="add_allactivity choseAgents"><p style="line-height:60px;text-align:center;">共有' + 370 + '家渠道为您服务，请选择渠道为其切位</p><p>' + "<label>渠道选择：</label>" + $select.html() + '</p></div></div>'

                $.jBox(html, {
                    title: "切位-选择渠道",
                    buttons: {'取消': 0, '提交': 1},
                    submit: submit,
                    height: 220,
                    width: 400,
                    persistent: true
                });
                $(".jbox-close").hide();
            } else {
                choseAgent(319);
            }
        }

        var submit = function (v, h, f) {
            if (v == 1) {
                choseAgent();
            } else {
                history.go(-1);
            }
        }

        function formSubmit() {
            var payType = $("#payType").val();
            if (payType == null || payType == "-1") {
                alert("请选择支付方式");
                return;
            }
            var agentId = $("#agentId").val();
            if (agentId != null && agentId != "") {
                var frontMoney = $("#frontMoney").val();
                var reservation = $("#reservation").val();
                var remark = $("#remark").val();
                if (frontMoney.length > 8) {
                    alert("定金不能大于8位数");
                    return false;
                }
                if (reservation.length > 26) {
                    alert("预订人姓名过长");
                    return false;
                }
                if (remark.length > 200) {
                    alert("备注不能超过200个字符");
                    return false;
                }
                $("#modForm").submit();
            } else {
                agendChoose();
            }
            if ($("#payReservePosition").val() != null && $("#payReservePosition").val() != "") {
                $("#btnSubmit").attr({"disabled": "disabled"});
            }
        }


        function checkMoney(v, obj) {
            var a = /^[0-9]*(\.[0-9]{1,2})?$/;
            if (!a.test(v)) {
                alert("金额格式不正确");
                document.getElementById(obj).value = "";
                $("#user_id").val("");
                // $(obj).value="";
                return false;
            }
        }

        function clean(id) {
            $("#frontMoney").val("");
            $("#payReservePosition").val("");
            $("#reservation").val("");
            $("#remark").val("");
            //清空支付方式
            $("#payType option[value='-1']").attr("selected", true);
            $("#btnSubmit").removeAttr("disabled");
            return true;
        }
        function resetBtn() {
            $("#btnSubmit").removeAttr("disabled");
        }

    </script>

    <script type="text/javascript">

    
    	function getReturnReserveList($obj){
    		var returnReserveInfoList = [];
    		var $returnTbody = $obj.find('tbody.returnTbody');
    		var channelCode = $obj.attr("channel-code");
    		
    		$($returnTbody).find("tr[name=product]").each(function(){
   		    	var returnReserveInfo = {};
   				returnReserveInfo.agentId = channelCode;//渠道ID
   				returnReserveInfo.productId = $(this).find("input[name=groupId]").val();//散拼团期ID或者机票产品ID
   				returnReserveInfo.reserveBackAmount = $(this).find("input[name=backCount]").val();//归还切位的数量
   				returnReserveInfo.returnRemark = $(this).find("input[name=returnRemark]").val();//归还切位的备注
   				returnReserveInfoList.push(returnReserveInfo);
    		});
    		return returnReserveInfoList;
    	}
    	
    	function submitReturnReserve(returnReserveJsonData) {
    		var flag = false;
    		$.ajax({
        		type: "POST",
        	   	url: "${ctx}/stock/manager/apartGroup/batchReturnReceive",
        	   	async: false,
        	   	data: {
        				"returnJsonStr":returnReserveJsonData,
        				"returnType":0
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		if(data.result==1){
        	   			$.jBox.tip(data.message, 'success');
        	   			flag = true;
        	   		} else if(data.result == 2) {
        	   			$.jBox.tip(data.message, 'fail');
        	   		}
        	   	}
        	});
    		
    		return flag;
    	}

        $(document).ready(function () {
            $(document).on('change', 'select[name="agentId"]', function () {
                var $this = $(this);
                var channelName = $(this).find('option:selected').text();
                var salerName = $(this).find('option:selected').attr('saler-name');
                $this.parents('.channel-cut:first').find('[name="salerName"]').text(salerName);
                $this.parents('.channel-cut:first').find('[name="agentName"]').text(channelName);
            });
            $(document).on('click','[name="back"]', function () {
                var isValid=true;
                var validInfo='';
                var $channelInfo = $(this).parents('div.channel-cut:first');
                var checkedSize = 0;
                var saler = $(this).parent().find('select[name=salerName]').val();  // 跟进销售
                $channelInfo.find('table tbody.returnTbody tr[name=product]').each(function () {
                    var $tr= $(this);
                    var cutCount = (+$tr.find('td[name=cutCount]').text());
                    var saleCount = (+$tr.find('td[name=saleCount]').text());
                    var backCount  = (+$tr.find('input[name=backCount]').val());
                    
                    checkedSize ++;
                    if($tr.find('input[name=backCount]').val() == '') {
                        isValid=false;
                        validInfo = '归还切位数量为必填项!';
                        $tr.find('input[name=backCount]').focus();
                    	return false;
                    }
                    
                    if((+$tr.find('input[name=backCount]').val()) <= 0) {
                    	isValid=false;
                        validInfo = '归还切位数量必须为正整数!';
                        $tr.find('input[name=backCount]').focus();
                    	return false;
                    }
                    
                    if(backCount>(cutCount-saleCount)){
                        isValid=false;
                        validInfo = '归还数量超出剩余切位数!';
                        $tr.addClass('error');
                    }
                    if(!saler || saler == "0"){
                        isValid = false;
                        validInfo = '请选择渠道跟进人';
                        return false;
                    }
                });
                
                if(checkedSize == 0) {
                    $.jBox.tip('请选择数据!','error');
                	return false;
                }
                
                if(!isValid){
                    $.jBox.tip(validInfo,'error');
                    return false;
                }
                
                //渠道归还切位信息
                var returnReserveInfos = [];
               	var reserveReserveInfoList = getReturnReserveList($channelInfo);
               	for(var i=0; i<reserveReserveInfoList.length; i++) {
               		returnReserveInfos.push(reserveReserveInfoList[i]);
               	}
                var returnReserveJsonData = JSON.stringify(returnReserveInfos);
                //提交返还切位数据
                var flag = submitReturnReserve(returnReserveJsonData);
                if(flag) {
                	$channelInfo.find("tbody.returnTbody tr[name=product]").remove();
                }
            });

            $(document).on('blur','[name="backCount"]',function(){
                $(this).next().remove();
                var $tr = $(this).parent().parent();
                var cutCount = (+$tr.find('[name="cutCount"]').text());
                var saleCount = (+$tr.find('[name="saleCount"]').text());
                var backCount  = (+$(this).val());
                if(backCount>(cutCount-saleCount)){
                    $(this).after('<span style="color:red"><br>请输入1到'+(cutCount-saleCount)+'之间的整数</span>');
                }
                
                if($(this).val() != '') {
                	$(this).val(parseInt($(this).val()));
                }
                
            });

            $(document).on('click','#backAll', function () {
                var isValid=true;
                var validInfo='';
                var checkedSize = 0;
                var hasSaler = true;
				$(this).parent().parent().find('div.channel-cut').each(function(){
					if($(this).attr("channel-code")) {
						var everySaler = $(this).find('select[name=salerName]').val();
						if(!everySaler || everySaler == "0") {
							hasSaler = false;
							return false;
						}
					}
				});
                $('#modForm .channel-cut:visible table tbody.returnTbody tr[name=product]').each(function () {
                    var $tr= $(this);
                    var cutCount = (+$tr.find('[name="cutCount"]').text());
                    var saleCount = (+$tr.find('[name="saleCount"]').text());
                    var backCount  =(+$tr.find('[name="backCount"]').val());
                    
                    checkedSize ++;
                    if($tr.find('input[name=backCount]').val() == '') {
                        isValid=false;
                        validInfo = '归还切位数量为必填项!';
                        $tr.find('input[name=backCount]').focus();
                    	return false;
                    }
                    
                    if((+$tr.find('input[name=backCount]').val()) <= 0) {
                    	isValid=false;
                        validInfo = '归还切位数量必须为正整数!';
                        $tr.find('input[name=backCount]').focus();
                    	return false;
                    }
                    
                    if(backCount>(cutCount-saleCount)){
                        isValid=false;
                        validInfo = '归还数量超出剩余切位数!';
                        $tr.addClass('error');
                    }
                    if(!hasSaler){
                        isValid = false;
                        validInfo = '请选择渠道跟进人';
                        return false;
                    }
                });
                
                if(checkedSize == 0) {
                    $.jBox.tip('请选择数据!','error');
                	return false;
                }
                
                if(!isValid){
                    $.jBox.tip(validInfo,'error');
                    return false;
                }
                
              	//渠道归还切位信息
                var returnReserveInfos = [];
                $("#modForm .channel-cut:visible").each(function(){
                	var reserveReserveInfoList = getReturnReserveList($(this));
                	for(var i=0; i<reserveReserveInfoList.length; i++) {
                		returnReserveInfos.push(reserveReserveInfoList[i]);
                	}
                });
                var returnReserveJsonData = JSON.stringify(returnReserveInfos);
                
                //提交返还切位数据
                var flag = submitReturnReserve(returnReserveJsonData);
                if(flag) {
                	window.setTimeout('window.location.href = "${ctx}/stock/manager/apartGroup/?agentId="',300);
                }
            });
        });
        
        $(function () {
            var activeMark = 'activityStock';
            $('#' + activeMark).addClass('active');
        });

        function jump(href) {
            var _m = '147';
            var _mc = '243';
            href = appendParam(href, {_m: _m, _mc: _mc});
            window.location.href = href;
        }
        
    </script>

</head>

<body>
	<page:applyDecorator name="stock_op_head">
        <page:param name="current">activityStock</page:param>
    </page:applyDecorator>
	<div>
		<div style="margin-bottom: 10px;">
			<input href="#" class="btn btn-primary" type="button" value="返回" onclick="returnMain()" /> 
			<input href="#" class="btn btn-primary" type="button" id="backAll" value="一键归还" />
			<input class="btn btn-primary" type="button" value="添加渠道" name="addChannel" />
		</div>

		<div class="ydbzbox fs">
			<form id="modForm" class="form-search" action="/a/stock/manager/apartGroup/doreserve" method="post">
				<div class="channel-cut" style="display: none">
					<div class="" style="margin: 10px 0px">
						<span>渠道名称：</span> 
						<span name="channelName"></span> 
						<span style="margin-left: 20px">渠道跟进人：</span> 
						<select name="salerName">
							<option value="">请选择</option>
						</select>
						<input name="openGroupList" type="button" value="添加团期" class="cl-tt-btn"> 
						<input name="back" type="button" value="归还" class="cl-tt-btn"> 
						<input name="removeChannel" type="button" value="删除渠道" class="cl-tt-btn"> 
						<span style="height:40px; width:1px; border-left:1px #868686 solid;margin-left: 15px"></span>
						<span style="position: relative; bottom: 9px; left: 5px;">
							<span class="closeOrExpand ydtClose"></span>
						</span>
					</div>

					<div class="toggle" style="display: none;margin-top: 10px;">
						<table class="table table-striped table-bordered table-condensed table-mod2-group">
							<thead>
								<tr>
									<th width="4%">序号</th>
									<th width="4%">团号</th>
									<th width="7%">产品名称</th>
									<th width="6%">当前余位数</th>
									<th width="5%">切位数</th>
									<th width="5%">已售切位</th>
									<th width="4%"><span class="xing">*</span>归还切位数量</th>
									<th width="6%">归还原因</th>
								</tr>
							</thead>
							<tbody class="returnTbody">
								<tr class="grouprow" style="display:none" name="groupList">
									<td class="tc">
										<span name="index"></span>
										<input type="hidden" name="groupId" />
									</td>
									<td class="tc"></td>
									<td class="tc"></td>
									<td class="tc"></td>
									<td class="tc" name="cutCount"></td>
									<td class="tc" name="saleCount">0</td>
									<td class="tc">
										<input name="backCount" type="text" data-type="number" maxlength="10" />
									</td>
									<td class="tc"><input type="text" name="returnRemark" value="" />
									</td>
								</tr>
							</tbody>
						</table>
						<div class="release_next_add">
						</div>
					</div>
				</div>
			</form>
		</div>

	</div>


	<div id="popCutChannelSelect" style="display: none">
		<div class="pop-channel-container">
			<div class="header">
				<label>切位渠道：</label> 
				<select id="popAgentId">
					<option value="-1">不限</option>
                    <c:forEach var="agentinfo" items="${agentinfoList }">
                    	<option value="${agentinfo.id }" <c:if test="${param.agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                    </c:forEach>
				</select>
			</div>
			<div class="selected-channel"></div>
		</div>
	</div>

	</div>
</body>
</html>
