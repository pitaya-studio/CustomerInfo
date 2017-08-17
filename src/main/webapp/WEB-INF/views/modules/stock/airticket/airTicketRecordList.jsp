<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>库存查询</title>
	<meta name="decorator" content="wholesaler"/>
	<script  type="text/javascript">
    	var contextPath = '${ctx}';
    </script>
<%-- 	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> --%>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>

    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script src="jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>

	<style type="text/css"></style>
	<style type="text/css">
        .pop-channel-container {
            margin-top: 20px;;
        }

        .pop-channel-container .header label {
            width: 100px;
            text-align: right;
        }
        .pop-channel-container .selected-channel{
            padding: 10px;
        }
        .pop-channel-container .selected-channel .channel-text{
            display: inline-block;
            padding: 0 3px;
            margin-right: 6px;
            margin-bottom: 6px;
            line-height: 25px;
            border: 1px solid #DDDDDD;
            background-color: #ededed;
        }
        .pop-channel-container .selected-channel .channel-remove{
            margin-left: 10px;
            cursor: pointer;
        }
        .ui-autocomplete.ui-menu.ui-front {
            z-index: 10000;
        }
    </style>
    
	<script type="text/javascript">
	
		var activityIds = "";
        //操作浮框
	    function operateHandler(){
		    $('.handle').hover(function() {
		        if(0 != $(this).find('a').length){
		            $(this).addClass('handle-on');
		            $(this).find('dd').addClass('block');
		        }
		    },function(){
		        $(this).removeClass('handle-on');
		        $(this).find('dd').removeClass('block');
		    });
		}

		$(function() {
        <%--added for UG_V2 展开/收起筛选 at 20170223 by tlw start.--%>
        launch();
      //操作浮框
      operateHandler();   
      
      //切位渠道改为可输入的select 
      $("#modifyAgentInfo").comboboxInquiry()
  
			$( ".spinner" ).spinner({
				spin: function( event, ui ) {
				if ( ui.value > 365 ) {
					$( this ).spinner( "value", 1 );
					return false;
				} else if ( ui.value < 0 ) {
					$( this ).spinner( "value", 365 );
					return false;
				}
				}
			});
			$(".qtip").tooltip({
				track: true
			});
			
			$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");

			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 						
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}

			if(inputRequest||selectRequest) {       
				$('.zksx').click();
			}
			
			activityIds = "${activityIds}";
		 	$("#groupform").validate({});
		 	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息"
   			});
			
			
            var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="id DESC";
            }
            var orderBy = _$orderBy.split(" ");
            $(".activitylist_paixu_left li").each(function(){
                if ($(this).hasClass("li"+orderBy[0])){
                    orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                    $(this).attr("class","activitylist_paixu_moren");
                }
            });
           $('.team_top').find('.table_activity_scroll').each(function(index, element) {
             var _gg=$(this).find('tr').length;
            if(_gg>=20){
            $(this).addClass("group_h_scroll");}
          });
           
           $("a[name=cost-del-name]").click(function(){
       		var gid = $(this).attr("groupid");
       		deleteCost(gid,this);       		
       		});
		});
		
	    function expandsss(child,obj,srcActivityId){
            if($(child).is(":hidden")){
                
                if("${userType}"=="1") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/manager/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId
                        },
                        success:function(msg) {
                        	$(obj).html("关闭查看");
                        	$(child).show();
                            $(obj).parents("td").attr("class","td-extend");
                        	if(msg.length>0){
                                $(child+" [class^='soldPayPosition']").show();
                        	}
                        	$.each(msg,function(keyin,valuein){
                                $("td .soldPayPosition"+(valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                        	});
                        }
                    });
                }else{
                	$(obj).html("关闭成本");
                	$(child).show();
                    $(obj).parents("td").attr("class","td-extend");
                }
            }else{
                if(!$(child).is(":hidden")){           
                	$(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("查看成本");
                }
            }
        }



        function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_op,payMode_cw,payMode_data,payMode_guarantee,payMode_express) {
    //alert(child);
    if($(child).is(":hidden")){
        var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected") : $("input[name='agentId']");
        var agentId = selectdata.val();
        if(agentId!=null&&agentId!="") {
            $.ajax({
                
            });
        }else{
            $(obj).html("收起");
            $(obj).parents('tr').addClass('tr-hover');
            $(child).show();
            $(obj).parents("td").addClass("td-extend");
        }
    }else{
        if(!$(child).is(":hidden")){
            $(obj).parents('tr').removeClass('tr-hover');
            $(child).hide();
            $(obj).parents("td").removeClass("td-extend");
            $(obj).html("展开");
        }
    }
}
	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
			});	
		 });
	    
	    function getCurDate(){
			var curDate = new Date();
			return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
		}
	  //控制截团时间	
		function takeOrderOpenDate(obj){
			var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
			return groupOD;
		}
		function takeModVisaDate(obj) {
			var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
			return groupOD;
		}
		function comparePositionMod(obj){
    		var plan = $(obj).val();
    		$(obj).parent().next().find("input").val(plan);
    		$(obj).parent().next().find("input").focus();
    		$(obj).parent().next().find("input").blur();
    	}
		
        function sortby(sortBy,obj){
            var temporderBy = $("#orderBy").val();
            if(temporderBy.match(sortBy)){
                sortBy = temporderBy;
                if(sortBy.match(/ASC/g)){
                    sortBy = sortBy.replace(/ASC/g,"");
                }else{
                    sortBy = $.trim(sortBy)+" ASC";
                }
            }
            
            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }

        function groupDetail(url) {
        	window.open(encodeURI(encodeURI(url)));
        }
        
        var deleteCost = function(id,$this){
            $.jBox.confirm("确定要删除所有的成本数据吗？", "提示", function(v, h, f) {
                if (v == 'ok') {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/manager/delByGroup",
                        cache:false,
                        async:false,
                        data:{gid : id},
                        success: function(e){
                            if(e == 'true'){
                               $($this).parent("td[name=cost-manager-name]").find("a[name=cost-add-name]").html("成本录入");
                               $($this).parent("td[name=cost-manager-name]").children(":not(a[name=cost-add-name])").remove();
                               window.location.reload();
                            }else{
                                top.$.jBox.tip('请求失败。','error');
                                return false;
                            }
                            
                        },
                        error : function(e){
                            top.$.jBox.tip('请求失败。','error');
                            return false;
                        }
                     });
                }
            });
            
          }
        
        
      function  changeIsRecord(itemRecord){
    	  $("#isRecord").val(itemRecord);
    	  $(".supplierLine .select").removeClass("select");
    	  $("#isRecord"+itemRecord).addClass("select");
    	  $("#searchForm").submit();
      }


     function returnReserve(mess, airticketId,leftFontMoney,freePosition) {
               
                 requestActivityGroupId=airticketId;              
                 var option = '<div style="margin-top:20px; padding-left:0px;">';
                 option += '<p>当前余位数量：'+ freePosition +'</p><dl style="overflow:hidden; padding-right:5px;"><dd style=" margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">渠道</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>'; 
                 $.ajax({
                        type: "POST",
                        async: false,
                        url: "${ctx}/stock/manager/airticket/getReserveByGroupId?dom=" + Math.random(),
                        data: {
                            airticketId : airticketId
                        },
                        success: function(msg) {                          
                        activityGroupList=msg;
                        for(var i=0;i<msg.length;i++){
                            option=option+'<tr><td><input name="radioid"  id="radioid" type="radio" value="'+msg[i].agentId+"~"+msg[i].payReservePosition+"~"+msg[i].soldPayPosition+'" style="margin:0;" />'+(i+1)+'</td><td> '+msg[i].agentName+'</td><td> '+msg[i].payReservePosition+'</td><td> '+msg[i].soldPayPosition+'</td></tr>';
                         }                   
                        }
                    });
                    option+="</table></div>";           
            
            var mess= '<div class="msg" style="margin-left:10px"><div class="field">'+option+
            '</div>'+
            '<p>归还切位数量：</p><div class="field"><input type="text" style="width:150px" id="reserveBackAmount" name="reserveBackAmount" value="" /></div>  ' +  
            '<div class="errorBlock" style="display: none;color:#FF0000"></div><BR> '+ 
             '<p>请填写您的还位原因：</p><div class="field"><textarea name="inputtext"'+
             ' id="inputtext"  rows="3" cols="20"></textarea></div>  ' +             
            '</div>'
          $.jBox(mess, { title: "切位归还",  buttons:{'提交': 1,'取消':0},submit: submit,width:400,persistent:true});
            
        }

         var submit = function (v, h, f) {
          if(v==1){ 
             var New=document.getElementsByName("radioid");
             //var tt=f.radioid;             
             var  find=false;
             var strNew;
             var maxReserveNum;
             var requestAgentId;
             var soldPayPosition;
             var reserveBackAmount=$("#reserveBackAmount").val();             
             var inputtext=$("#inputtext").val(); 
             for(var i=0;i<New.length;i++){
                 if(New.item(i).checked){
                     strNew=New.item(i).getAttribute("value"); 
                     var strs= new Array(); //定义一数组 
                     strs=strNew.split("~"); //字符分割                       
                     requestAgentId=strs[0]; 
                     maxReserveNum =strs[1];   
                     soldPayPosition =strs[2];                        
                     find=true;
                    break;
               }
             } 
             if(find==false) {
              $(".errorBlock").html("请选择切位记录").show();
              return false;
            }
            var repos = /^[1-9]+[0-9]*]*$/;
            var re = /^[0-9]+[0-9]*]*$/;
            var reserveBackAmount=$("#reserveBackAmount").val();            
            if(!reserveBackAmount.match(repos)||parseInt(reserveBackAmount)>(parseInt(maxReserveNum) -parseInt(soldPayPosition))|| parseInt(reserveBackAmount)<0){             
                $(".errorBlock").html("人数请输入正整数1到"+(parseInt(maxReserveNum) -parseInt(soldPayPosition))+"之间的整数").show();
                return false; 
                /*}else   if(fontMoneyBackAmount==null||fontMoneyBackAmount==''||(!fontMoneyBackAmount.match(re))||
                    parseInt(fontMoneyBackAmount)>parseInt(maxFontMoneyBack) || parseInt(maxFontMoneyBack)<0){
                //alert("金额请输入整数");
                $(".errorBlock").html("金额请输入正整数0到"+maxFontMoneyBack+"之间的整数").show();
                return false; */       
            }else{  
                $(".errorBlock").hide();               
                $.ajax({
                    type: "POST",
                    url: "${ctx}/stock/manager/airticket/returnReserve?dom=" + Math.random(),
                    data: {
                        airticketId : requestActivityGroupId,
                        agentId : requestAgentId,
                        reserveBackAmount:reserveBackAmount,
                        fontMoneyBackAmount:1, //fontMoneyBackAmount
                        returnRemark:inputtext                        
                    },
                    success: function(msg) {
                        if(msg == 'fail') {
                            top.$.jBox.tip('归还失败','warning');
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        } else if(msg == 'success') {
                            top.$.jBox.tip('归还成功','warning');
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                            location.reload();
                        } else {
                            top.$.jBox.tip(msg,'warning');
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        }
                    }
                });
                top.$('.jbox-body .jbox-icon').css('top','55px');
                return false;
              }

            }        
           }

        function formReset(){
      $(':input','#searchForm')
      .not(':button, :submit, :reset, :hidden')
      .val('')
      .removeAttr('checked')
      .removeAttr('selected');
     $('#modifyAgentInfo').val('');
    }

	</script>
	
	<!-- 批量切位操作 -->
	 <script type="text/javascript">
        $(document).ready(function () {
            $('#btnCut').on('click', function () {
                var $pop = $.jBox($('#popCutChannelSelect').html(), {
                    title: 	"选择渠道", buttons: {'添加': 1, '取消': 2}, submit: function (v, h, f) {
                        if(v==1){
                            var channels =[];
                            $pop.find('.selected-channel .channel-text').each(function () {
                                var code = $(this).attr('channel-code');
                                channels.push(code);
                            });
                            
                            if(channels.length == 0) {
                            	return true;
                            }
                            
                            window.location.href="${ctx}/stock/manager/airticket/batchReceiveInfo?channels="+channels.join(",");
                        }
                    }, height: 320, width: 380
                });
                $pop.on('click','.channel-remove', function () {
                    $(this).parent().remove();
                });
                var $popAgentId = $pop.find('#popAgentId').comboboxInquiry();
                $popAgentId.on('comboboxinquiryselect', function () {
                    var channelCode = $popAgentId.val();
                    var channelName = $popAgentId.find('option:selected').text();

                    if (!$pop.find('.selected-channel [channel-code="' + channelCode + '"]').length) {
                        var channelText = '<span class="channel-text" channel-code="' + channelCode + '">' + channelName+
                                '<span class="channel-remove">x</span>' +
                                '</span>';
                        $pop.find('.selected-channel').append(channelText);
                    }
                    $popAgentId.val('');
                    setTimeout(function () {
                        $pop.find('.ui-autocomplete-input').val('');
                    },100);

                });
            });
            $('#btnCutBack').on('click', function () {
                var $pop = $.jBox($('#popBackChannelSelect').html(), {
                    title: "选择归还渠道", buttons: {'添加': 1, '取消': 2}, submit: function (v, h, f) {
                        if(v==1){
                        	var channels =[];
                            $pop.find('.selected-channel .channel-text').each(function () {
                            	var code = $(this).attr('channel-code');
                                channels.push(code);
                            });
                            
                            if(channels.length == 0) {
                            	return true;
                            }
                            
                            window.location.href="${ctx}/stock/manager/airticket/batchReturnReceiveInfo?channels=" + channels.join(",");
                        }
                    }, height: 320, width: 380
                });
                $pop.on('click','.channel-remove', function () {
                    $(this).parent().remove();
                });
                var $popAgentId = $pop.find('#popAgentId').comboboxInquiry();
                $popAgentId.on('comboboxinquiryselect', function () {
                    var channelCode = $popAgentId.val();
                    var channelName = $popAgentId.find('option:selected').text();

                    if (!$pop.find('.selected-channel [channel-code="' + channelCode + '"]').length) {
                        var channelText = '<span class="channel-text" channel-code="' + channelCode + '">' + channelName+
                                '<span class="channel-remove">x</span>' +
                                '</span>';
                        $pop.find('.selected-channel').append(channelText);
                    }
                    $popAgentId.val('');
                    setTimeout(function () {
                        $pop.find('.ui-autocomplete-input').val('');
                    },100);

                });
            });
            $(document).on('click','[name="saveInDraftbox"]',function(){
                window.location.href="${ctx}/airticketactivityreserveTemp/list";
            });
        });
    </script>

</head>
<body>
      <page:applyDecorator name="stock_op_head">
      <page:param name="current">flightStock</page:param>
      </page:applyDecorator>
    <div class="activitylist_bodyer_right_team_co_bgcolor">
	 <form:form id="searchForm" modelAttribute="activityAirTicket" action="${ctx}/stock/manager/airticket" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="isRecord" name="isRecord" type="hidden" value="${activityAirTicket.isRecord}">
        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
                <input name="departureCity" type="text" class="inputTxt radius4" value="${departureCity}"placeholder="出发城市">
            —
                <input value="${arrivedCity}" name="arrivedCity" class="inputTxt radius4"placeholder="到达城市">
            </div>
            <div class="zksx filterButton_solo">筛选</div>
            <div class="form_submit">
                <input class="btn btn-primary ydbz_x" type="submit" value="搜索">
                 <input class="btn ydbz_x" type="button" value="清空所有条件" onclick="formReset();">
            </div>
            <shiro:hasPermission name="stock:airticket:batchreserve">
            <div class=" pull-right">
                <input class="btn btn-primary" id='btnCut' type="button" value="批量切位">
                <input class="btn btn-primary" id="btnCutBack" type="button" value="批量归还切位">
                <input class="btn btn-primary" type="button" value="切位草稿箱" name="saveInDraftbox">
            </div>
            </shiro:hasPermission>
            <div class="ydxbd">
                <span></span>
            <div class="activitylist_bodyer_right_team_co3">
                <label class="activitylist_team_co3_text">团号：</label>
                    <input value="${groupCode}" name="groupCode" class="inputTxt">
                </div> 
              <div class="activitylist_bodyer_right_team_co3">
                <label class="activitylist_team_co3_text">渠道选择：</label>
                    <select id="modifyAgentInfo" name="agentId">
                        <option value="">全部</option>
                        <c:forEach items="${agentinfoList}" var="agentInfo">
                            <option value="${agentInfo.id}" ${agentInfo.id==agentId?"selected":''}>${agentInfo.agentName}</option>
                        </c:forEach>
                    </select>
                </div>
               <!-- <div class="activitylist_bodyer_right_team_co3">
                    <div class="activitylist_team_co3_text">切位部门：</div>
                    <select name="department">
                        <option value="">不限</option>
                        <c:forEach items="${departmentList}" var="department">

                            <option value="${department.id}" ${departmentId==department.id?"selected":''}>${department.name}</option>
                        </c:forEach>

                    </select>
                </div>  -->
                <div class="activitylist_bodyer_right_team_co3">
                    <label class="activitylist_team_co3_text">航空公司：</label>
                    <div class="selectStyle">
                        <select name="airlines">
                            <option value="">不限</option>
                            <c:forEach items="${traffic_namelist}" var="trafficName">
                                <option value="${trafficName.value}" ${trafficName.value==activityAirTicket.airlines?"selected":''}>${trafficName.label} ${trafficName.description}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">机票类型：</label>
                    <div class="selectStyle">
                        <select name="airType">
                            <option value="">不限</option>
                            <option value="3" ${activityAirTicket.airType=='3'?"selected":''}>单程</option>
                            <option value="1" ${activityAirTicket.airType=='1'?"selected":''}>多段</option>
                            <option value="2" ${activityAirTicket.airType=='2'?"selected":''}>往返</option>
                        </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">时间区间：</label>
                    <input readonly="" onfocus="WdatePicker({minDate:CurentTime(),onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" style="margin-left: -3px;" value="${startingDate}" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate">
                    <span> 至 </span>
                    <input readonly="" onclick="WdatePicker({minDate:CurentTime()})" value="${returnDate}" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                     <label class="activitylist_team_co3_text">价格范围：</label>
                     <input onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" value="${settlementAdultPriceStart}" name="settlementAdultPriceStart" class="inputTxt" maxlength="7" id="settlementAdultPriceStart">
                     <span> 至 </span><!-- class去掉了 rmb -->
                     <input onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" value="${settlementAdultPriceEnd}" name="settlementAdultPriceEnd" class="inputTxt" maxlength="7" id="settlementAdultPriceEnd">
                </div>
            </div>
            <div class="kong"></div>
        </div>
	</form:form>
	
	
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
            <!--  
            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
            -->
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
    </div>    
    	<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table activitylist_bodyer_table mainTable" >
		<thead >
			<tr>
				<th width="5%">序号</th>	
				<th width="9%">团号</th>		
				<th width="7%">机票类型</th>
				<th width="6%">产品编号</th> 
				<th width="7%">出发城市</th>
				<th width="7%">到达城市</th>				
				<th width="6%">应收价格</th>
        <th width="9%">机位余位</th>   
        <th width="9%">切位数</th>			       
        <th width="9%">已分配数</th>
				<th width="8%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="airticket" varStatus="s">
				<tr id="parent${s.count}">
					<td class="tc">${s.count} </td>
					<td class="tc">${airticket.groupCode} </td>	
					<td class="tc"><c:choose>
                    <c:when test="${airticket.airType == 3}">
                        单程
                    </c:when>
                    <c:when test="${airticket.airType == 2}">
                        往返
                    </c:when>
                    <c:otherwise>
                        多段
                    </c:otherwise>
                </c:choose>   
               </td>
               <td class="tc">
                ${airticket.productCode}
               </td>
              <!-- 出发城市 -->
              <td  class="tc">
                ${airticket.departureCityLabel() }             
                     </td>
                     <td  class="tc">
                     <!-- 到达城市 -->                     
					  <c:forEach items="${areas}" var="arrivedcity">
                    <c:if test="${arrivedcity.id eq airticket.arrivedCity}">
                     ${arrivedcity.name}
                    </c:if>
                   </c:forEach>
                </td>  
                    <td  class="tc"><c:forEach items="${curlist}" var="curlist">
                    <c:if test="${curlist.id==airticket.currency_id}">
                        ${curlist.currencyMark}
                    </c:if>
                </c:forEach>
                ${airticket.settlementAdultPrice} 起</td>                 
                 <td class="tc">${airticket.freePosition}</td> 
                 <td class="tc">${airticket.payReservePosition}</td>              
                 <td class="tc">${airticket.nopayReservePosition + airticket.payReservePosition} </td>
                 <td>                 
                 <dl class="handle">
                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                        <dd>
                            <p>
                                <span></span>
                                <%--onclick="jbox_qw();"--%> 
                                 <a href="javascript:void(0)" onclick="expand('#child${s.count}',this,1203)">展开</a>
                                 <c:if test="${airticket.freePosition > 0}">
                                   <a href="${ctx}/stock/manager/airticket/reserve?id=${airticket.id}&agentId=" >切位</a>
                                 </c:if>
                               <c:if test="${(airticket.payReservePosition - airticket.soldPayPosition)> 0 }">
                                    <a href="javascript:void(0)" onclick="return returnReserve('确认归还切位？', '${airticket.id }','${airticket.payReservePosition - airticket.soldPayPosition}','${airticket.freePosition}');">归还切位</a>
                                </c:if>
                                <a target="_blank" href="${ctx}/stock/manager/airticket/detail/${airticket.id}" >库存详情</a> 
                                 </p>
                        </dd>
                    </dl>            
			        </td>					
				</tr>





                 <tr id="child${s.count}" style="display:none" class="activity_team_top1">
                    <td class="team_top" colspan="11">
                        <table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable">
                                    <thead>
                                        <tr>
                                            <th width="5%">序号</th>
                                            <th width="10%">航空公司</th>
                                            <th width="7%">舱位</th>
                                            <th width="11%">出发机场</th>
                                            <th width="11%">到达机场</th>
                                            <th width="12%">起飞时间</th>
                                            <th width="12%">到达时间</th>
                                            <th width="9%">成人同行价</th>
                                            <th width="9%">儿童同行价</th>
                                            <th width="10%">特殊人群同行价</th>                                            
                                        </tr>
                                    </thead>                            
                            <tbody>
                         <c:forEach items="${airticket.flightInfos}" var="group" varStatus="s2">
                                <tr>
                                     <td class="tc">${group.number}</td>
                                     <td>${group.airlinesLabel()}</td>
                                     <td>${group.airspaceLabel()}</td>
                                     <td>
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq group.leaveAirport}">
                            ${airportlist.airportName}
                            </c:if>
                             </c:forEach> 
                                     </td>
                                   <td>
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq group.destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach> 
                             </td>
                             <td><fmt:formatDate value="${group.startTime}" pattern="yyyy-MM-dd  HH:mm"/>
                            </td>
                             <td><fmt:formatDate value="${group.arrivalTime}" pattern="yyyy-MM-dd  HH:mm"/></td>
                             
                             
                             <c:choose>
                                <c:when test="${airticket.airType == 2}">
                                    <td >-</td>
                                    <td >-</td>
                                    <td >-</td>
                                </c:when>
                                <c:when test="${airticket.airType == 3 }">
                                  <td >
                                        ${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="tdred fbold">${airticket.settlementAdultPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="tdred fbold">${airticket.settlementcChildPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="tdred fbold">${airticket.settlementSpecialPrice}</span>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td >
                                        ${fns:getCurrencyInfo(group.currency_id, 0, "mark")}<span class="tdred fbold">${group.settlementAdultPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(group.currency_id, 0, "mark")}<span class="tdred fbold">${group.settlementcChildPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(group.currency_id, 0, "mark")}<span class="tdred fbold">${group.settlementSpecialPrice}</span>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                             
                    
                 </tr>
                   </c:forEach>
                            </tbody>
                       </table>     
                       
                </td>
            </tr>    





			</c:forEach>
		</tbody>
	</table>
	</form>
	</div>
	   <div class="page">
			<div class="pagination">
				<div class="endPage">${page}</div>
				<div style="clear:both;"></div>
			</div>	
		</div>
		<br/>
	
	<div id="popCutChannelSelect" style="display:none;">
	    <div class="pop-channel-container">
	        <div class="header">
	            <label>切位渠道：</label>
	            <select id="popAgentId">
	                <option></option>
	            	<c:forEach items="${agentinfoList}" var="agentInfo">
                        <option value="${agentInfo.id}">${agentInfo.agentName}</option>
                    </c:forEach>
	            </select>
	        </div>
	        <div class="selected-channel">
	        </div>
	    </div>
	</div>
	<div id="popBackChannelSelect" style="display:none;">
	    <div class="pop-channel-container">
	        <div class="header">
	            <label>切位渠道：</label>
	            <select id="popAgentId">
	                <option></option>
	            	<c:forEach items="${agentinfoList}" var="agentInfo">
                        <option value="${agentInfo.id}">${agentInfo.agentName}</option>
                    </c:forEach>
	            </select>
	        </div>
	        <div class="selected-channel">
	        </div>
	    </div>
	</div>
	</body>
</html>