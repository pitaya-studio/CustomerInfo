<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>海岛游转团申请</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
	$(function(){
		$('.class_change_btn').click(function() {
			var html = $('.class_change_on').html();
				$.jBox(html, { title: "转团",buttons:{'进入':0,'确认': 1}, submit:function(v, h, f){
				if (v=="1"){
					return true;
				}
			},height:200,width:410});	
		
		});
		$('.class_change_btn_off').click(function() {
			var html = $('.class_change_off').html();
				$.jBox(html, { title: "转团",buttons:{'取消':0,'强行转入': 1}, submit:function(v, h, f){
				if (v=="1"){
					return true;
				}
			},height:200,width:410});	
		
		});
		// 查询团期
		$("#findCode").click(function(){
			valTravels();
		});
		// 转团操作
		$("#onloadDiv").click(function(){
			subForm();
		});
		// 取消转团，关闭转团信息
		$("#closeDiv").click(function(){
			$("#groupInfo").css("display","none");
		});
		// 控房模块
		$("#kfSel").on('click', function() {
			$(this).find("div.pop_inner_outer").show();
			event.stopPropagation();
		}).on('click', "div.pop_inner_outer input.btn_confirm_inner_outer02", function(event) {
			// 选择控票数 确定
			var $div = $("div.pop_inner_outer").has(this);
			var sum = 0;
			$div.find("tr:visible td:last-child").each(function() {
				sum += (parseInt($(this).find("input").val()) || 0);
			});
			$("#kfInput").val(sum);
			$div.hide();
			event.stopPropagation();
			//ticketAmount();
		});
	});
	
	// 判断是否选中了转团游客，否则不允许查询新团期号
	function valTravels(){
		// num为选中游客的数量
		var num = 0;
		$("#groupForm input[name=travelUuid]").each(function(){
			if($.trim($(this).val())){
				num++;
			}
		});
		if(num>0){
			var groupCode = $.trim($("#inGroupCode").val());
			if(groupCode){
				getGroupCode(groupCode);	
			}else{
				$.jBox.tip("请填写新团期号","警告");
				$("#inGroupCode").val("");
			}
		}else{
			$.jBox.tip("请选择需要转团的游客","警告");
			$("#inGroupCode").val("");
		}
	}
	
	//模糊匹配输入团期号 (暂时用与查找新团期号)
	function getGroupCode(groupCode){
		$.ajax({
			type:"GET",
			url : "${ctx}/islandapplications/getAjaxGroupCodeList?groupCode="+groupCode,
			data : "",
			dataType : "text",
			success : function(html){
				var json;
				try{
					json = $.parseJSON(html);
				}catch(e){
					json = {res:"error"};							
				}
				if(json.res=="success"){
					// 清理掉隐藏div中原来的转团信息
					$("#groupInfo span.travel").empty();
					$("#groupInfo span.groupCode").empty();
					$("#groupInfo span.activityIsland").empty();
					$("#groupInfo span.emptySit").empty();
					
					// 将转团游客名写入隐藏div
					$("#contentTable input[name=chk]:checked").each(function(){
						if($.trim($(this).next().next().val())){
							$("#groupInfo span.travel").append($.trim($(this).next().next().val())+"    ");
						}
					});
					// 将团号写入隐藏Div
					$("#groupInfo span.groupCode").append(groupCode);
					// 将产品名称写入隐藏Div
					$("#groupInfo span.activityIsland").append(json.activityIsland.activityName);
					// 将余位数写入隐藏Div
					$("#groupInfo span.emptySit").append(json.remNumber);
					$("#rember").append(json.remNumber);

					// 查询到团期，并展开转团信息div
					if($("#groupInfo").css("display")=="none"){
						$("#groupInfo").css("display","");
					}
				}
			}
		});
	}
	// 全选事件
	function allChk(obj){
		//alert("allchk");
		if(obj.checked){
			$("input[name='chk']").not("input:checked").each(function(){
				$(this).attr("checked","checked");
				var uuid = $(this).next().val();
				$("#groupForm").append("<input type='hidden'  name='travelUuid' value='"+uuid+"'>");
				 // 获取本行中的转团原因
				 var remarkval= $(this).parents("tr").find("input[name=remark]").val();
				 var remarkId = $(this).parents("tr").find("input[name=remark]").attr("id");
				 $("#groupForm").append("<input type='hidden' name='remark' id="+remarkId+" value='"+remarkval+"'>");
			});
		}else{
			$("input[name='chk']:checked").each(function(){
				$(this).removeAttr("checked");
				var uuid = $(this).next().val();
				$("#groupForm input[name=travelUuid][value="+uuid+"]").remove();
				 // 获取本行中的转团原因
				 var remark= $(this).parents("tr").find("input[name=remark]").val();
				 var remarkId = $(this).parents("tr").find("input[name=remark]").attr("id");
				 $("#"+remarkId).remove();
			});
		}
		// 查询到团期，并展开转团信息div
		$("#groupInfo").css("display","none");
	}
	// 单选事件
	function chk(obj){
		// 选中
		 if(obj.checked){
			 // 获取本行中的游客UUid
			 var uuid = $(obj).next().val();
			 $("#groupForm").append("<input type='hidden' name='travelUuid' value='"+uuid+"'>");
			 // 获取本行中的转团原因
			 var remarkval= $(obj).parents("tr").find("input[name=remark]").val();
			 var remarkId = $(obj).parents("tr").find("input[name=remark]").attr("id");
			 $("#groupForm").append("<input type='hidden' name='remark' id="+remarkId+" value='"+remarkval+"'>");
		 }
		// 取消选中
		else{
			 // 获取本行中的游客UUid
			 var uuid = $(obj).next().val();
			 $("#groupForm input[name=travelUuid][value="+uuid+"]").remove();
			 // 获取本行中的转团原因
			 var remark= $(obj).parents("tr").find("input[name=remark]").val();
			 var remarkId = $(obj).parents("tr").find("input[name=remark]").attr("id");
			 $("#"+remarkId).remove();
		}
		// 查询到团期，并展开转团信息div
		 $("#groupInfo").css("display","none");
	}
	// 提交转团
	function subForm(){
		//alert("subfrom");
		// 获取新团号
		var inGroupCode = $.trim($("#inGroupCode").val());
		// 新团预报名间数
		var newRoom = $("#groupInfo input[name=newRoom]").val();
		// 新团预报名票数
		var newTicket = $("#groupInfo input[name=newTicket]").val();
		// 原订单控房数量
		var oldRoomControl = $("#groupInfo input[name=oldRoomControl]").val();
		// 原订单非控房数量
		var oldRoomNoControl = $("#groupInfo input[name=oldRoomNoControl]").val();
		 // 原订单控票数量
		 var oldTicketControl = $("#groupInfo input[name=oldTicketControl]").val();
		 // 原订单非控票数量
		 var oldTicketNoControl = $("#groupInfo input[name=oldTicketNoControl]").val();
		if(inGroupCode){
			// 将新团号写入表单
			$("#groupForm input[name=groupCode]").val(inGroupCode);
			$("#groupForm input[name=newRoom]").val(newRoom);
			$("#groupForm input[name=newTicket]").val(newTicket);
			$("#groupForm input[name=oldRoomControl]").val(oldRoomControl);
			$("#groupForm input[name=oldRoomNoControl]").val(oldRoomNoControl);
			$("#groupForm input[name=oldTicketControl]").val(oldTicketControl);
			$("#groupForm input[name=oldTicketNoControl]").val(oldTicketNoControl);
			// 校验转团
			valForm();
		}else{
			$.jBox.tip("请输入新团期号","警告");
		}
	}
	// 校验转团
	function valForm(){
		var groupCode =$.trim($("#groupForm input[name=groupCode]").val());
		var orderUUid = $.trim($("#groupForm input[name=orderUuid]").val());
		var num = 0;
		$("#groupForm input[name=travelUuid]").each(function(){
			if($.trim($(this).val())){
				num++;
			}
		});
		// 如果新团期号、原订单UUID、转团游客数量合法，则开始ajax提交表单
		if(groupCode && orderUUid && num>0){
			// ajax 提交表单
			ajaxUpload();
		}else{
			$.jBox.tip("请选择转团游客","警告");
		}
	}
	// ajax提交表单
	function ajaxUpload(){
		var theParam = $("#groupForm").serialize();
		$.ajax({
			type : "POST",
			url : "${ctx}/islandapplications/addIsLandReview",
			data : theParam,
			dataType : "text",
			success : function(html){
				var json;
				try{
					json = $.parseJSON(html);
				}catch(e){
					json = {res:"error"};
				}
				
				if(json.res=="success"){
					$.jBox.tip("成功","提示");
					window.location.href="${ctx}/islandapplications/goToIslandOrderList/${order.uuid }";
				}else if(json.res=="error"){
					$.jBox.tip(json.mes,"警告");
				}
			}
			
		});
	}

</script>
<script type="text/javascript">
	$(document).ready(function() {
		$("select[name='class_num']").combobox();
	});
</script>
<script type="text/javascript">
	
	//$("#fkfInput").on("blur", ticketAmount);
	$(document).click(function(event) {
		if (!$("#kfSel").has(event.target).length && !$("#kfSel").is(event.target)) {
			$("div.pop_inner_outer").hide();
		}
	});
	function ticketAmount() {
		//$("#totalNumber").text((+($("#kfInput").val()) || 0) + (+($("#fkfInput").val()) || 0));
	}
</script>
<style type="text/css">
	.custom-combobox-toggle {
		height: 26px;
		margin: 0 0 0 -1px;
		padding: 0;
		/* support: IE7 */
		
		*height: 1.7em;
		*top: 0.1em;
	}
	.custom-combobox-input {
		margin: 0;
		padding: 0.3em;
		width: 166px;
	}
	.ui-autocomplete {
		height: 200px;
		overflow: auto;
	}
	.sort {
		color: #0663A2;
		cursor: pointer;
	}
	.custom-combobox input[type="text"] {
		height: 26px;
		width: 166px;
	}
	.pop_inner_outer_new{ width: 850px;position: absolute;z-index: 9999;top: 20px;left: -200px;border: 1px solid #EFEFEF;padding: 10px;background-color: #FFF;Wbox-shadow: 0px 1px 2px rgba(0, 0, 0, 0.2);}
    .ydbz_qd_new li{ float:left;width:25%;margin:13px 0 0;}
    .ydbz_qd_new li label{display:inline-block;width:130px; text-align:right;margin: 0;}
    .ydbz_qd_new li label font{ font-size:12px;}
</style>
</head>
<body>
		<div class="bgMainRight">
			<!--右侧内容部分开始-->
               <!--币种模板开始-->
               <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
                   <option value="1">人民币</option>
                   <option value="2">美元</option>
                   <option value="3">欧元</option>
                   <option value="4">日元</option>
               </select>
               <!--币种模板结束-->
               <page:applyDecorator name="activity_island_change_applications"></page:applyDecorator>
               <div class="mod_nav">订单 > 海岛游 > 转团</div>
             <!-- 订单信息 -->
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>
			<!-- 费用及人数 -->
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostAndNumInfo.jsp"%>
			<!-- 费用结算 -->
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostInfo.jsp"%>
			<div class="ydbz_tit pl20"><span class="fl">转团</span></div>
			
			<!-- 提交转团form -->
			<form id="groupForm" >
				<!-- 新团期 -->
				<input type="hidden" name="groupCode" value=""/>
				<!-- 原订单UUID -->
				<input  type="hidden" name="orderUuid" value="${order.uuid }"/>
				<input  type="hidden" name="newRoom"/>
				<input  type="hidden" name="newTicket"/>
				<input  type="hidden" name="oldRoomControl"/>
				<input  type="hidden" name="oldRoomNoControl"/>
				<input  type="hidden" name="oldTicketControl"/>
				<input  type="hidden" name="oldTicketNoControl"/>
			</form>
			<table id="contentTable" class="table activitylist_bodyer_table">
                   <thead>
                       <tr>
					       <th width="5%" class="table_borderLeftN">全选<input type="checkbox" name="allChk" onclick="allChk(this)"/></th>
                           <th width="10%">游客姓名</th>
                           <th width="5%">游客类型</th>
                           <th width="5%">舱位等级</th>
                           <th width="15%">签证国家及类型</th>
					       <th width="20%">证件类型/证件号/有效期</th>
                           <th width="20%">转团原因</th>
                       </tr>
                   </thead>
                   <tbody>
                   <c:if test="${travelList != null }">
	                   <c:forEach items="${travelList }" var="travel">
	                   	 <tr>
						       <td class="table_borderLeftN">
						       		<input type="checkbox"  name="chk" onclick="chk(this)"/>
						       		<input type="hidden" name="travelUuid" value="${travel.uuid }"/>
						       		<input type="hidden" name="travelName" value="${travel.name }"/>
						       	</td>
	                           <td class="tc">${travel.name }</td>
	                           <td class="tc">
	                           		<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" value="${travel.personType }" srcColumnName="name" />
	                           </td>
	                          <td class="tc">
	                          		${fns:getDictLabel(travel.spaceLevel,"spaceGrade_Type" , "无")}
	                          </td>
	                           <td class="tc">
	                           		<c:forEach items="${travel.islandTravelervisaList }" var="visa">
	                           			<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" value="${visa.country }" srcColumnName="name_cn" />
	                           			<!--<trekiz:defineDict id="hotelTheme" name="hotelTheme" type="new_visa_type" defaultValue="${visa.visaTypeId }" />-->
	                           			<c:if test="${visa.visaTypeId eq 1268 }">个签</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1269 }">团签</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1270 }">商务签</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1271 }">照会</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1272 }">邀请</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1273 }">照会+邀请</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1274 }">探亲</c:if>
	                           			<c:if test="${visa.visaTypeId eq 1473 }">续签</c:if>
	                           			<br/>
	                           		</c:forEach>
	                           </td>
						       <td class="tc">
						       		<c:forEach items="${travel.islandTravelerPapersTypeList }" var="papers">
						       			<c:if test="${papers.papersType eq 1 }">身份证</c:if>
						       			<c:if test="${papers.papersType eq 2 }">护照</c:if>
						       			<c:if test="${papers.papersType eq 3 }">警官证</c:if>
						       			<c:if test="${papers.papersType eq 4 }">军官证</c:if>
						       			<c:if test="${papers.papersType eq 5 }">其他</c:if>
						       			/${papers.idCard }/
						       			<fmt:formatDate value="${papers.validityDate }" pattern="yyyy-MM-dd"/>
						       			<br/>
						       		</c:forEach>
						       </td>
	                           <td class="tc"><input type="text" name="remark" id="rem${travel.uuid }" value=""/></td>
	                       </tr>
	                   </c:forEach>
                   </c:if>
                   </tbody>
               </table>
			<div class="ydbz_foot" style="width:1200px;margin-top:10px; padding-bottom:10px;">
				<div class="maintain_add">
					<p>
						<label>转入团号：</label>
						<input type="text" id="inGroupCode" />
						<input type="button" id="findCode" value="查找" class="ydbz_x fl  "/>
					</p>
					<p class="maintain_kong"></p>
				</div>
			</div>
			 <div class="ydbz_tit pl20 cl-both"><span class="fl">转团信息</span></div>
                <div id="groupInfo" style="display:none">
                    <ul class="ydbz_qd_new" style="overflow: visible">
                      <li><label>转团人：</label><span  class="travel"></span></li>
                      <li> <label>团号：</label> <span  class="groupCode"></span></li>
                      <li><label>操作人：</label> <span  class="createby">${fns:getUser().name}</span> </li>
                      <li><label>产品名称：</label> <span  class="activityIsland"></span></li>
                      <li><label>余位：</label><span id="rember"></span></li>
                        <div class="kong"></div>
                        <li><label>新订单预报名票数：</label><input class="w50_30" name="newTicket" type="text" /> 张</li>
                        <li><label>归还机票：控票</label><input class="w50_30" name="oldTicketControl" type="text" /> 张</li>
                        <li><label>非控票</label><input class="w50_30" name=oldTicketNoControl"" type="text" /> 张</li>  
                        <div class="kong"></div>                        
                        <li> <label>新订单预报名间数：</label><input class="w50_30" name="newRoom" type="text" />  </li>
                        <li><label>归还酒店：控房</label>
	                          <input  style="ime-mode: disabled;" id="kfInput" data-type="number" class="inputTxt w50_30" readonly="readonly" type="text" name="oldRoomControl"/> 间  
	                          <a id="kfSel" style="position:relative;">选择
		                         <div style="top: 30px; left: -20px; display: none;" class="pop_inner_outer">
	                                 <table class="table  activitylist_bodyer_table_new">
	                                     <thead>
	                                         <tr>
	                                             <th width="15%">入住日期</th>
	                                             <th width="9%">房型&amp;晚数</th>
	                                             <th width="10%">餐型</th>
	                                             <th width="10%">上岛方式</th>
	                                             <th width="10%">余位/库存</th>
	                                             <th width="10%">地接供应商</th>
	                                             <th width="10%">采购类型</th>
	                                             <th width="10%">使用库存数</th>
	                                         </tr>
	                                     </thead>
	                                     <tbody>
	                                     	<c:if test="${not empty hotelControlDetailList }">
	                                     		<c:forEach items="${hotelControlDetailList }" var="detail">
	                                     			<tr>
	                                     				 <td class="tc font_c66 new_hotel_p_table2_tdf">${detail.inDate }</td>
	                                     				 <td class="tc font_c66 ">
																<c:forEach items="${hotelControlDetail.rooms }" var="room">
																	<p><trekiz:autoId2Name4Class  classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid}"/>*${room.night }</p>
																</c:forEach>
														 </td>
	                                     				 <td class="tc font_c66 "><trekiz:defineDict name="hotelMeals" type="hotel_meal_type" defaultValue="${detail.hotelMeal}" readonly="true" /></td>
	                                     				 <td class="tc font_c66 "><trekiz:defineDict name="islands_way" type="islands_way" defaultValue="${detail.islandWay}" readonly="true" /></td>
	                                     				 <td class="tc font_c66 ">${detail.stock - detail.sellStock }/${detail.stock }</td>
	                                     				 <td class="tc font_c66 ">${detail.groundSupplier }</td>
	                                     				 <td class="tc font_c66 ">
	                                     				 	<c:if test="${detail.purchaseType eq 0 }">内采</c:if>
	                                     				 	<c:if test="${detail.purchaseType eq 1 }">外采</c:if>
	                                     				 </td>
	                                     				 <td class="tc font_c66 ">
	                                     				 	<input style="ime-mode: disabled;" data-type="number" data-min="0" class="inputTxt w50_30 spread " type="text" value="${detail.islandOrderControlDetailNumber }"/>
	                                     				 </td>
	                                     			</tr>
	                                     		</c:forEach>
	                                     	</c:if>
	                                     	<c:if test="${empty hotelControlDetailList }">
	                                     		<tr><td colspan="8">暂无数据</td></tr>
	                                     	</c:if>
	                                     </tbody>
	                                 </table>
	                                 <div class="btn_confirm_inner_outer"><input class="btn_confirm_inner_outer02 maring_bottom10 up_load_visa_info_btn_del" value="确定" type="button"/></div>
	                             </div>
	                         </a>
                         </li>
                          <li><label>非控票</label><input class="w50_30" name="oldRoomNoControl" type="text" /> 间</li>
                    </ul>
                  </div>
                  
                  <div class="dbaniu cl-both" >
                  	<!-- 
					<a class="ydbz_x fl class_change_btn" onclick="window.close();">关闭</a>
					<a class="ydbz_x fl class_change_btn" onclick="class_change_on()" >提交</a>
					 -->
					<input type="button" id="closeDiv" value="关闭" class="ydbz_x fl"  onclick="window.close();"/>
					<input type="button" id="onloadDiv" value="提交" class="ydbz_x fl"/>
				</div>
			
             <!-- 
			<div id="groupInfo" class="" style="display:none">
					<table>
						<tr>
							<td><label>转团人：</label></td><td><span  class="travel"></span></td>
							<td></td>
							<td><label>团号：</label></td><td><span  class="groupCode"></span></td>
							<td></td>
							<td><label>产品名称：</label></td><td><span  class="activityIsland"></span></td>
						</tr>
						<tr>
							<td><label>操作人：</label></td><td><span  class="createby">${fns:getUser().name}</span></td>
							<td></td>
							<td><label>余位：</label></td><td><span  class="emptySit"></span></td>
							<td></td>
							<td></td><td></td>
						</tr>
						<tr>
							<td><label>新团预报名间数：</label></td>
							<td><span  class="newRoom"><input type="text" id="newRoom" value="0" /></span></td>
							<td></td>
							<td><label>新团预报名票数：</label></td>
							<td><span  class="newTicket"><input type="text" id="newTicket" value="0"/></span></td>
							<td></td>
							<td></td><td></td>
						</tr>
						<tr>
							<td><label>原订单房间返还数：</label></td><td><span  class="oldRoom">${fns:getUser().name}</span></td>
							<td></td>
							<td><label>原订单机票返还数：</label></td><td><span  class="oldTicket"></span></td>
							<td></td>
							<td></td><td></td>
						</tr>
						<tr>
							<td></td><td></td>
							<td><input type="button" id="closeDiv" value="关闭" class="ydbz_x fl"/></td>
							<td></td><td></td>
							<td><input type="button" id="onloadDiv" value="提交" class="ydbz_x fl"/></td>
							<td></td><td></td>
						</tr>
					</table>
			</div>-->
			 
			<div class="ydbz_tit pl20"><span class="fl">转团记录</span></div>
			<table class="activitylist_bodyer_table modifyPrice-table">
			   <thead>
				  <tr>
					 <th width="10%">游客姓名</th>
					 <th width="7%">游客类型</th>
					 <th width="7%">舱位等级</th>
					 <th width="10%">申请时间</th>
					 <th width="10%">签证国家及类型</th>
					 <th width="20%">证件类型/证件号/有效期</th>
					 <th width="10%">备注</th>
					 <th width="10%">转入团</th>
					 <th width="7%">申请人</th>
					 <th width="7%">审批状态</th>
				  </tr>
			   </thead>
			   <tbody>
			   		<c:forEach items="${reviewList }" var ="review">
			   			<tr>
			   				<td  class="tc">${review.KEY_TRAVELERNAME }</td>
			   				<td  class="tc">${fns:getTravelerTypeCn(review.KEY_TRAVELERTYPE)}</td>
			   				<td  class="tc">
			   					<c:if test="${review.KEY_TRAVELERLEVEL eq '1'}">头等舱</c:if>
			   					<c:if test="${review.KEY_TRAVELERLEVEL eq '2'}">公务舱</c:if>
			   					<c:if test="${review.KEY_TRAVELERLEVEL eq '3'}">经济舱</c:if>
			   					<c:if test="${review.KEY_TRAVELERLEVEL ne '1' and review.KEY_TRAVELERLEVEL ne '2' and review.KEY_TRAVELERLEVEL ne '3'}">待选</c:if>
			   				</td>
			   				<td  class="tc">${review.KEY_APPLYDATE }</td>
			   				<td  class="tc">${review.KEY_VISACOUNTRYTYPE }</td>
			   				<td  class="tc">${review.KEY_PAPERSTYPECODEDATE }</td>
			   				<td  class="tc">${review.KEY_REMARK }</td>
			   				<td  class="tc">${review.KEY_NEWGROUPCODE }</td>
			   				<td  class="tc">${fns:getUserById(review.createBy).name}</td>
			   				<td  class="tc">
			   					<c:if test="${review.status eq 0 }">驳回</c:if>
			   					<c:if test="${review.status eq 1 }">待审核</c:if>
			   					<c:if test="${review.status eq 2 }">审核成功</c:if>
			   					<c:if test="${review.status eq 3 }">操作完成</c:if>
			   					<c:if test="${review.status eq 4 }">取消审核</c:if>
			   				</td>
			   			</tr>
			   		</c:forEach>
			   </tbody>
			   
			</table>
		</div>
</body>
</html>