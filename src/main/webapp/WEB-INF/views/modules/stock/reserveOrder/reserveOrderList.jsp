<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>散拼切位订单</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">
	.sort{color:#0663A2;cursor:pointer;}
	.out-date,.close-date{width:100%; height:25px; line-height:25px; text-align:center;}
	.p0{ padding:0;}
	.table .tr,.activitylist_bodyer_table .tr{ text-align:right;}
	.dzje_dd span{color:#009535;padding-right:5px;}
	.fbold{ font-weight:bold;}/**字号加粗**/
	#searchForm .activitylist_bodyer_right_team_co .pr input#wholeSalerKey:-ms-input-placeholder{
		color: #999;
	}
	</style>
<%-- 	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> --%>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
   <style type="text/css">
   </style>
	<script type="text/javascript">
	
	var activityIds = "";
	function selectQuery(){
	$("#modifyAgentInfo").comboboxInquiry();
	}
	 
	$(function() {
	//展开筛选按钮
		launch();
    //加载操作按键
    $('.handle').hover(function() {
        if(0 != $(this).find('a').length){
            $(this).addClass('handle-on');
            $(this).find('dd').addClass('block');
        }
    },function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  
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
			
			$('.team_top').find('.table_activity_scroll').each(function(index, element) {
				var _gg=$(this).find('tr').length;
	            if(_gg>=20){
	            $(this).addClass("group_h_scroll");
	            $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
	            }

		  });

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

            //渠道改为可输入的select
           
            selectQuery();
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
		});
	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
			});	
		 });

	function expand(child,obj,srcActivityId){
		if($(child).is(":hidden")){

			if("${userType}"=="1") {
				$.ajax({
					type:"POST",
					url:"${ctx}/stock/manager/apartGroup/payReservePosition",
					data:{
						srcActivityId:srcActivityId
					},
					success:function(msg) {
						$(obj).html("关闭全部团期");
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
				$(obj).html("关闭全部团期");
				$(child).show();
				$(obj).parents("td").attr("class","td-extend");
			}
		}else{
			if(!$(child).is(":hidden")){
				$(child).hide();
				$(obj).parents("td").attr("class","");
				$(obj).html("展开全部团期");
			}
		}
	}
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/stock/manager/apartGroup/getReserveOrderList");
		$("#searchForm").submit();
	}
	function confirmBatchIsNull(mess,sta) {
		if(activityIds != ""){
			if(sta=='off'){
				confirmBatchOff(mess);
			}else if(sta=='del'){
				confirmBatchDel(mess);
			}
		}else{
			$.jBox.error('未选择产品','系统提示');
		}
	}
	function getSoldPayPosition(obj) {

	}
	function modgroup(groupid,savebtn,delbtn,cancelbtn,obj){
		$(obj).parent().parent().find("span").css("display","none");
		$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
		$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
		$(groupid).css("display","none");
		$(groupid).attr("disabled",false);
		$(savebtn).css("display","block");
		$(delbtn).css("display","none");
		$(cancelbtn).css("display","block");
		$(obj).css("display","none");
	}
	function cancelgroup(modbtn,savebtn,delbtn,obj){
		$(modbtn).css("display","block");
		$(savebtn).css("display","none");
		$(delbtn).css("display","block");
		$(obj).css("display","none");
		$(obj).parent().parent().find("span").css("display","block");
		$(obj).parent().parent().find("input[type='text']").css("display","none");
		$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	}
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
		//window.location.href = "${ctx}/sys/docinfo/download/"+docid;
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
//			alert(groupOD);
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
        var requestAgentId;
        var requestActivityGroupId;
        var maxReserveNum;
        var activityGroupList;
        var maxFontMoneyBack;

        function returnReserve(mess, activityGroupId,leftFontMoney,freePosition) {
     			
                 requestActivityGroupId=activityGroupId;              
                 var option = '<div style="margin-top:20px; padding-left:0px;">';
                 option += '<p>当前余位数量：'+ freePosition +'</p><dl style="overflow:hidden; padding-right:5px;"><dd style=" margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">经销商</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>'; 
       			 $.ajax({
    	                type: "POST",
    	                async: false,
    	                url: "${ctx}/stock/manager/apartGroup/getReserveByGroupId?dom=" + Math.random(),
    	                data: {
    	                	activityGroupId : activityGroupId
    	                },
    	                success: function(msg) {                          
    	                activityGroupList=msg;
    	                for(var i=0;i<msg.length;i++){
    	                	option=option+'<tr><td><input name="radioid"  id="radioid" type="radio" value="'+msg[i].agentId+"~"+msg[i].leftpayReservePosition+"~"+msg[i].soldPayPosition+'" style="margin:0;" />'+(i+1)+'</td><td> '+msg[i].agentName+'</td><td> '+msg[i].leftpayReservePosition+'</td><td> '+msg[i].soldPayPosition+'</td></tr>';
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
                    url: "${ctx}/stock/manager/apartGroup/returnReserve?dom=" + Math.random(),
                    data: {
                        activityGroupId : requestActivityGroupId,
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
        var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
}

    	function viewRefund(aid){
    		window.open("${ctx}/stock/manager/apartGroup/viewGroupRefund?orderId="+aid);
    	}
	</script>
</head>
<body>
	<!-- <div class="mod_nav">
切位订单&nbsp;&nbsp;>散拼库存切位列表</div>   -->
   <div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
<!--   <div class="activitylist_bodyer_right_team_co_bgcolor">-->
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/stock/manager/apartGroup/getReserveOrderList" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
    	 <div class="activitylist_bodyer_right_team_co">
    	 
    	 <div class="activitylist_bodyer_right_team_co2 pr">
         	<input class="txtPro inputTxt searchInput" id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }"
				   placeholder="请输入产品名称"/>
         	</div>
			 <a class="zksx" >筛选</a>
	      <div class="form_submit">
			<input class="btn btn-primary ydbz_x" type="submit" value="搜索">
			<input class="btn ydbz_x " type="button" onclick="resetSearchParams()" value="清空所有条件"/>
		</div>
		<div class="ydxbd">
			<span></span>
    	 <div class="activitylist_bodyer_right_team_co3">
			<label class="activitylist_team_co3_text">渠道选择：</label>
				 <select id="modifyAgentInfo" name="agentId" >
                     <option value="">全部</option>
                     <c:forEach var="agentinfo" items="${agentinfoList }">
                     <option value="${agentinfo.id }" <c:if test="${param.agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                     </c:forEach>
                 </select>
			</div>
      		<div class="activitylist_bodyer_right_team_co1" >
				<label class="activitylist_team_co3_text">出团日期：</label>
				<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>' onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly/>
				<span style="font-size:12px; font-family:'宋体';"> 至</span>  
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate" value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>'onClick="WdatePicker()" readonly/>
        	</div>
        	<%-- <div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
				<select name="paymentType" id="paymentType" >
					<option value="">不限</option>
					<c:forEach items="${fns:findAllPaymentType()}" var="pType">
						<!-- 用户类型  1 代表销售 -->
						<option value="${pType[0]}" <c:if test="${pType[0] eq paymentType}">selected="selected"</c:if>>${pType[1] }</option>
					</c:forEach> 
				</select>
			</div> --%>
    		<div class="activitylist_bodyer_right_team_co1">
                  <label class="activitylist_team_co3_text">目的地：</label>
                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds }" labelName="targetAreaNameList"  labelValue="${ targetAreaName}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
         	</div>
         	<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">价格范围：</label><input id="settlementAdultPriceStart" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
				<span style="font-size:12px;font-family:'宋体';"> 至</span> 
				<input id="settlementAdultPriceEnd" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
         	</div>
		</div>
	      <div class="kong"></div>
         </div>
	</form:form>
<div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
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
			  		<th width="4%">序号</th>
					<th width="8%">订单号</th>
					<th width="8%">团号</th>
					<th width="8%">产品名称</th>
					<th width="8%">渠道</th>
					<th width="9%">切位时间</th>
					<th width="8%">出发地</th>
					<th width="8%">目的地</th>
					<th width="8%">出/截团日期</th>
					<th width="5%">切位数</th>
					<th width="5%">余位数</th>
					<th width="7%">切位订金</th>
					<th width="7%">已收金额<br />到账金额</th>
					<th width="4%">操作</th>
            </tr>
		</thead>
		<tbody>		
			<c:forEach items="${page.list}" var="activity" varStatus="s">
			 	<tr id="parent${s.count}">
					<td class="tc">${s.count} </td>
					<td class="tc">${activity.orderNum}</td>
					<td class="tc">${activity.groupCode}</td>
              		<td class="tc">${activity.acitivityName}</td>
              		<td class="tc">${activity.agentName}</td>
              		<td><fmt:formatDate value="${activity.createDate}" pattern="yyyy-MM-dd"/></td>
                 	<td>${activity.getFromAreaName()}</td>
					<td >
                        ${activity.areaName}
					</td>

                    <td class="tc p0">
                                     <div class="out-date"><fmt:formatDate value="${activity.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
                                     <div class="close-date"><fmt:formatDate value="${activity.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
                     </td>
                    <td>${activity.payReservePosition}</td>
                    <td>${activity.freePosition}</td>
                    <td>${activity.orderMoney}${activity.currencyName}</td>
                    <td class="p0 tr">	
									<div class="yfje_dd"><span class="fbold">${activity.orderMoney}${activity.currencyName}</span></div>
									<div class="dzje_dd"><span class="fbold"><c:if test="${activity.confirm == 1 }">${activity.payMoney}${activity.currencyName}</c:if></span></div>
								</td>
                    <td class="p00">
                            <dl class="handle">
                                <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                                <dd class="">
                                    <p>
                                        <span></span>                                       

<a href="${ctx}/stock/manager/apartGroup/reserveOrderInfo?orderNum=${activity.orderNum} " onclick="">查看详情</a>
     
<a href="javascript:void(0)" onclick="viewRefund('${activity.aid }');">切位退款</a>                                      
                                    </p>
                                </dd>
                            </dl>
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
	</body>
</html>