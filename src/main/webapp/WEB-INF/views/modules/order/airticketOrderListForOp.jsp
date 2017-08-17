<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<title>订单-机票订单</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		//操作浮框
		operateHandler();
		//展开筛选按钮
		launch();
		//搜索聚焦失焦
		inputTips();
		//展开筛选 或收起筛选功能
		//if(${conditionsMap.showChooseSelect=='1'}){
		//	$("#showChooseOp").click();
		//}
		showSearchPanel();
		
		$("#saler" ).comboboxInquiry();
		$("#salerId" ).comboboxInquiry();
		
		//销售与下单人相互切换
	    $("#contentTable").delegate(".salerId","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.createBy_cen').removeClass('onshow');
	        $('.salerId_cen').addClass('onshow');
	    });
	    
	    $("#contentTable").delegate(".createBy","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.salerId_cen').removeClass('onshow');
	         $('.createBy_cen').addClass('onshow');
	        
	    });
	});
	
	// 	有查询条件的时候，DIV不隐藏
	function showSearchPanel(){
		var ticketType = $("input[name='ticketType']:checked").val();
		var airType = $("input[name='airType']:checked").val();
		var fromAreaId = $("select[name='fromAreaId'] option:selected").val();
		var contact = $("input[name='contact']").val();
		var showType = $("select[name='showType'] option:selected").val();
		var targetAreaNameList = $("#targetAreaName").val();
		var op = $("input[name='op']").val();
		var agentId = $("select[name='agentId'] option:selected").val();
		var startAirTime = $("input[name='startAirTime']").val();
		var endAirTime = $("input[name='endAirTime']").val();
		var returnStartAirTime = $("input[name='returnStartAirTime']").val();
		var returnEndAirTime = $("input[name='returnEndAirTime']").val();
		var saler = $("select[name='saler'] option:selected").val();
		var salerId = $("select[name='salerId'] option:selected").val();
		
		if(isNotEmpty(ticketType) || isNotEmpty(airType)
				||isNotEmpty(fromAreaId) || isNotEmpty(contact)
				||(isNotEmpty(showType) && showType != 0) || isNotEmpty(targetAreaNameList)
				||isNotEmpty(op) || isNotEmpty(agentId) || isNotEmpty(startAirTime) 
				|| isNotEmpty(endAirTime)|| isNotEmpty(returnStartAirTime)|| isNotEmpty(returnEndAirTime) 
				|| isNotEmpty(saler) || isNotEmpty(salerId)){
			$('.zksx').click();
		}
	}
	
	// 判定不为空值
	function isNotEmpty(str){
		if(str != ""){
			return true;
		}
		return false;
	}
	
	function clickChoose(){
		if($("#showChooseOp").html()=="展开筛选"){
			$("#showChooseSelect").val(1);
		}else{
			$("#showChooseSelect").val(0);
		}
	}
	//展开子table
	function expand(child,obj){
		if($(child).css("display")=="none"){
			$(obj).html("收起").parents("tr").addClass("tr-hover");
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开").parents("tr").removeClass("tr-hover");
		}
	}
	
	//分页
	function page(n,s){
	    $("#pageNo").val(n);
	    $("#pageSize").val(s);
	    $("#searchForm").submit();
	    return false;
	}
	
	//查询条件重置
	function resetSearchParams(){
	    $(':input','#searchForm')
	     .not(':button, :submit, :reset, :hidden')
	     .val('')
	     .removeAttr('checked')
	     .removeAttr('selected');
	}
	
	//排序
	function sort(element, sortNum){
		$("#sortNum").val(sortNum);
		var _this = $(element);
		
		var inputObjs = _this.parent("li").parent("ul").find("input");
		for(var i = 0;i < inputObjs.length;i++){
			$(inputObjs[i]).val("");
		}
		
		//按钮高亮
		_this.parent("li").attr("class","activitylist_paixu_moren");
		
		//原先高亮的同级元素置灰
		_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");
		
		//高亮按钮隐藏input赋值
		_this.next().val("activitylist_paixu_moren");
		
		//原先高亮按钮隐藏input值清空
		_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");
		
		var sortFlag = _this.children().attr("class");
		//降序
		if(sortFlag == undefined || sortFlag == "icon icon-arrow-up"){
		
			//改变箭头的方向
			_this.children().attr("class","icon icon-arrow-down");
			
			//降序
			_this.prev().val("desc");
		}
		//降序
		else if(sortFlag == "icon icon-arrow-down"){
			//改变箭头方向
			_this.children().attr("class","icon icon-arrow-up");
			
			//shengx
			_this.prev().val("asc");
		}
		
		$("#searchForm").submit();
		
	    return false;
	}
	
	function downloads(docid){
		if(docid){
	   		window.open("${ctx}/sys/docinfo/download/"+docid);
		}else{
			top.$.jBox.tip("没有确认单");
		}
    }
    
    //订单-机票订单-航班备注
	function jbox_hbbzOp(element, orderId,contentRemark) {
	    var html = '<div style="padding:10px; text-align:center;">';
	    html += '<textarea id="contentRemark" name="contentRemark" cols="" rows="" style="margin:10px auto; width:90%;">'+(contentRemark==null?"":contentRemark)+'</textarea>';
	    html += '</div>';
	    $.jBox(html, { title: "航班备注",buttons:{'确定': true,'取消': false }, submit:function(v, h, f){
	        if (v==true){
	        
	       		var contentRemark = $("#contentRemark").val();
// 	       		if(contentRemark == ""){
// 					top.$.jBox.tip('请输入航班备注信息', 'error', { focusId: 'contentRemark' }); 
// 					return false;
// 	       		}
  		       	contentRemark = contentRemark.replace(/</g, "&lt;").replace(/>/g, "&gt;");
	       		contentRemark = contentRemark.replace("\n\r", "<br>&nbsp;&nbsp;");
				contentRemark = contentRemark.replace("\r\n", "<br>&nbsp;&nbsp;");//这才是正确的！
				contentRemark = contentRemark.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
	       		contentRemark = contentRemark.replace(" ", "&nbsp;");
	        	var jsonRes = {
	        			orderId : orderId,
	        			remark : contentRemark
	    				};
    			$.ajax({
    				type: "POST",
    				url: "${ctx}/order/manage/saveRemark",
    				cache:false,
    				dataType:"json",
    				async:false,
    				data:jsonRes,
    				success: function(data){
    					if(data.flag == 'success'){
        					top.$.jBox.tip('航班备注保存成功','success');
        					$("#btn_search").click();
    					} else {
    						top.$.jBox.tip('航班备注保存失败','error');
    						return false;
    					}
    				},
    				error : function(e){
    					top.$.jBox.tip('请求失败。','error');
    					return false;
    				}
    			 });
	        	
	            return true;
	        }
	    },height:200,width:500});
	
		
	}
    
function lockOrder(orderId){
   doOrderLockStatus(orderId, "lockOrder", "锁定成功");
}


function unLockOrder(orderId){
   doOrderLockStatus(orderId, "unLockOrder", "解锁成功");
}

function doOrderLockStatus(orderId, actionName, tipMsg){
    $.ajax({
        type: "POST",
        url: "${ctx}/order/manage/" + actionName,
        data: {
            "orderId":orderId
        },
        success: function(msg){
           if(msg){
	           if(msg.success){
	                top.$.jBox.tip(tipMsg,'warning');
	                $("#btn_search").click();
	            }else{
	                top.$.jBox.tip(msg.error,'warning');
	            }
           }
            
        }
     });
}

/**
 * 确认占位
 *
 * param orderId
 */
function confirmOrder(orderId) {
	$.ajax({
		type: "POST",
		url: "${ctx}/order/manage/confirmOrder?dom=" + Math.random(),
		data: {
			orderId : orderId
		},
		success: function(msg) {
			if(msg == 'fail') {
				top.$.jBox.tip('占位失败','warning');
				top.$('.jbox-body .jbox-icon').css('top','55px');
			} else if(msg == 'success') {
				top.$.jBox.tip('占位成功','warning');
				top.$('.jbox-body .jbox-icon').css('top','55px');
				$("#btn_search").click();
			} else {
				top.$.jBox.tip(msg,'warning');
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}
		}
	});
}

function formatRemark(remark){
	if(remark == null || remark == ""){
		return "";
	}
	var content=remark.replace(/\n/g,"<br/>");
	return content;
}

var notSeenIds = "";
function changeSeenFlagByMouse(orderId, obj) {
	if ($("[name=seenFlag]", obj).val() == 0) {
		notSeenIds += "," + orderId;
	}
	changeOrderSeenFlag();
}

function changeOrderSeenFlag() {
	if (notSeenIds != "") {
		$.ajax({
			type :"POST",
			url : "${ctx}/order/manage/changeNotSeenOrderFlag?dom=" + Math.random(),
			cache : false,
			data : {notSeenOrderIds : notSeenIds},
			success:function(data) {
				if (data.result = "success") {
					//更改没查看订单状态，改为已查看
					var idArr = notSeenIds.split(",");
					for (var i=0;i<idArr.length;i++) {
						$("#order_" + idArr[i]).val("1");
						$("#order_" + idArr[i]).parent().find(".new-tips").remove();
					}
					
					//更新左边菜单栏没查看订单数字
					var href = "order/manage/airticketOrderListForOp";
					$("li[id^=childMenu]").each(function(index, obj) {
						if ($(this).html().indexOf(href) != -1) {
							var leftNum = $("span", this).text() - data.changeSum;
							if (leftNum == 0) {
								$("span", this).parent().remove();
								if ($("li[id^=childMenu] a em").length == 0) {
									$("h2:contains('订单')").children("span:last").remove();
								}
							} else {
								$("span", this).text(leftNum);
							}
							notSeenIds = "";
							return false;
						}
					});
				}
			}
		});
	}
}

//setInterval("changeOrderSeenFlag()", 1000);
</script>
</head>

<body>

	<!-- 列表 content -->
	<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
        
        <c:if test="${not empty conditionsMap }">
			<form id="searchForm" action="${ctx}/order/manage/airticketOrderListForOp" method="post" class="formpaixu">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		        <input id="showChooseSelect" name="showChooseSelect" type="hidden" value="${conditionsMap.showChooseSelect}"/>
		        <input id="isNeedNoticeOrder" type="hidden" value="${isNeedNoticeOrder}" />
				<div class="activitylist_bodyer_right_team_co2 wpr20 pr">
					<div class="activitylist_team_co3_text">搜索：</div>
					<input value="${conditionsMap.orderNumOrOrderGroupCode }" class="inputTxt inputTxtlong" name="orderNumOrOrderGroupCode" id="orderNum" flag="istips"> 
					<span class="ipt-tips">输入团号、订单号</span>
				</div>
				<div class="form_submit">
					 <input type="submit" id="btn_search" value="搜索"  class="btn btn-primary ydbz_x">
					 <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
				</div>
				<a class="zksx" id="showChooseOp" onclick="clickChoose()">筛选</a>
				<div class="ydxbd">
					<div class="messageDiv">
						<div class="seach25 seach100">
							<span class="seach_check"><label for="inquiry_radio_flights0"><input type="radio" name="ticketType" id="inquiry_radio_flights0" value="" checked="checked"> 不限</label></span>
							<span class="seach_check"><label for="inquiry_radio_flights1"><input type="radio" name="ticketType" id="inquiry_radio_flights1" value="2" <c:if test="${conditionsMap.ticketType==2 }"> checked="checked"</c:if> > 国际机票</label></span>
							<span class="seach_check"><label for="inquiry_radio_flights2"><input type="radio" name="ticketType" id="inquiry_radio_flights2" value="1" <c:if test="${conditionsMap.ticketType==1 }"> checked="checked"</c:if> > 内陆机票</label></span>
							<span class="seach_check"><label for="inquiry_radio_flights3"><input type="radio" name="ticketType" id="inquiry_radio_flights3" value="3" <c:if test="${conditionsMap.ticketType==3 }"> checked="checked"</c:if> > 国际+内陆机票</label></span>
						</div>
						<div class="seach25 seach100">
						  <span class="seach_check"><label for="radio10"><input type="radio" name="airType" id="radio10" value="" checked="checked"> 不限</label>　　</span>
						  <span class="seach_check"><label for="radio11"><input type="radio" name="airType" id="radio11" value="3" <c:if test="${conditionsMap.airType==3 }"> checked="checked"</c:if> > 单程</label>　　</span>
						  <span class="seach_check"><label for="radio12"><input type="radio" name="airType" id="radio12" value="2" <c:if test="${conditionsMap.airType==2 }"> checked="checked"</c:if> > 往返</label>　　</span>
						  <span class="seach_check"><label for="radio13"><input type="radio" name="airType" id="radio13" value="1" <c:if test="${conditionsMap.airType==1 }"> checked="checked"</c:if> > 多段</label>	</span>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">出发地：</div>
                              <select name="fromAreaId" >
								<option value="">不限</option>
								<c:forEach items="${fromAreasMap}" var="entry">
									<option value="${entry.key}"  <c:if test="${conditionsMap.fromAreaId==entry.key }">selected="selected"</c:if> >${entry.value}</option>
								</c:forEach>
							</select>
						</div>
						  
						<div class="activitylist_bodyer_right_team_co1">
                                 <div class="activitylist_team_co3_text">联系人：</div><input type="text" class="" name="contact" value="${conditionsMap.contact }">
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">订单状态：</div>
                              <select id="trafficName" name="showType">      
                                 <option value="0"  <c:if test="${conditionsMap.showType == 0 }">selected="selected"</c:if> >全部订单</option>
                                 <option value="1"  <c:if test="${conditionsMap.showType == 1 }">selected="selected"</c:if> >未收全款</option>
                                 <option value="2"  <c:if test="${conditionsMap.showType == 2 }">selected="selected"</c:if> >未收订金</option>
                                 <option value="5"  <c:if test="${conditionsMap.showType == 5 }">selected="selected"</c:if> >已收全款</option>
                                 <option value="4"  <c:if test="${conditionsMap.showType == 4 }">selected="selected"</c:if> >已收订金</option>
                                 <option value="3"  <c:if test="${conditionsMap.showType == 3 }">selected="selected"</c:if> >已占位</option>
                                 <option value="7"  <c:if test="${conditionsMap.showType == 7 }">selected="selected"</c:if> >待计调确认</option>
                                 <option value="99"  <c:if test="${conditionsMap.showType == 99 }">selected="selected"</c:if> >已取消</option>
                                 <option value="111"  <c:if test="${conditionsMap.showType == 111 }">selected="selected"</c:if> >已删除</option>
							  </select>
						</div>
						<div class="activitylist_bodyer_right_team_co2">
							<label>出发日期：</label><input id="" class="inputTxt dateinput" name="startAirTime" value="${conditionsMap.startAirTime}" onfocus="WdatePicker()" readonly="" />
							 <span> 至 </span> 
							 <input id="" class="inputTxt dateinput" name="endAirTime" value="${conditionsMap.endAirTime}" onclick="WdatePicker()" readonly="" />
						</div>
						<div class="kong"></div>
						<div class="activitylist_bodyer_right_team_co1">  
							<div class="activitylist_team_co3_text">目的地：</div>
                            <tags:treeselect id="targetArea" name="targetAreaIdList" value=""  labelName="targetAreaNameList" labelValue="" title="区域" url="/sys/area/treeData" cssClass="required targetArea_no_input" checked="true"/>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">计调：</div><input type="text" class="" name="op" value="${conditionsMap.op }">
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">渠道选择：</div>
                              <select id="" name="agentId">
                              	<option value="">不限</option>     
                                <c:if test="${not empty agentinfoList }">
									<c:forEach items="${agentinfoList}" var="agentinfo">
										<option value="${agentinfo.id }"  <c:if test="${agentinfo.id == conditionsMap.agentId}">selected="selected"</c:if> >${agentinfo.agentName }</option>
									</c:forEach>
								</c:if>
                              </select>
						</div>
						<div class="activitylist_bodyer_right_team_co2">
							<label>返回日期：</label><input id="" class="inputTxt dateinput" name="returnStartAirTime" value="${conditionsMap.returnStartAirTime}" onfocus="WdatePicker()" readonly="" />
							 <span> 至 </span> 
							 <input id="" class="inputTxt dateinput" name="returnEndAirTime" value="${conditionsMap.returnEndAirTime}" onclick="WdatePicker()" readonly="" />
						</div>						
						<div class="kong"></div>
						<div class="activitylist_bodyer_right_team_co1">
		                      	<div class="activitylist_team_co3_text">销售：</div>
		                      	<span style="position:absolute">
								<select id="salerId" name="salerId"">
									<option value="" >不限</option>
									<c:forEach items="${users}" var="userinfo">
										<option value="${userinfo.id }" <c:if test="${userinfo.id eq conditionsMap.saler}">selected="selected"</c:if>>${userinfo.name }</option>
									</c:forEach>
		                       	</select>
		                   	</span>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
		                      	<div class="activitylist_team_co3_text">下单人：</div>
		                      	<span style="position:absolute">
								<select id="saler" name="picker"">
									<option value="" >不限</option>
									<c:forEach items="${users}" var="userinfo">
										<option value="${userinfo.id }" <c:if test="${userinfo.id eq conditionsMap.picker}">selected="selected"</c:if>>${userinfo.name }</option>
									</c:forEach>
		                       	</select>
		                   	</span>
						</div>
						<div class="kong"></div>
					</div>
				</div>
				<div class="kong"></div>
				
		<!-- 产品线路分区 -->
<%-- 		<div class="supplierLine">
			<a class="select" href="javascript:void(0)" onclick="">订单列表</a>
			<c:if test="${order.orderType == 2 }">
				<a href="javascript:void(0)" onclick="">团号列表</a>
			</c:if>
		</div> --%>
<input type ="hidden" id="sortNum" name = "sortNum" value = "${conditionsMap.sortNum}"/>
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
	                    <li>排序</li>
	                    <c:choose>
	                    <c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
	                    	<input type="hidden" value="${conditionsMap.orderCreateDateSort}" name="orderCreateDateSort">
	                    	<a onclick="sort(this, 0)">创建日期
	                    		<c:choose>
	                    			<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.orderCreateDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose> 
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.orderCreateDateCss}" name="orderCreateDateCss">
	                    </li>
	                    
	                    <c:choose>
	                    <c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
	                    	<input type="hidden" value="${conditionsMap.orderUpdateDateSort}" name="orderUpdateDateSort">
	                    	<a onclick="sort(this, 1)">更新日期
	                    		<c:choose>
	                    			<c:when test="${conditionsMap.orderUpdateDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.orderUpdateDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose>
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.orderUpdateDateCss}" name="orderUpdateDateCss">
	                    </li>
	                    
	                    <c:choose>
	                    <c:when test="${conditionsMap.startFlightDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
							<input type="hidden" value="${conditionsMap.startFlightDateSort}" name="startFlightDateSort">
							<a onclick="sort(this, 2)">起飞日期
								<c:choose>
	                    			<c:when test="${conditionsMap.startFlightDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.startFlightDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose>
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.startFlightDateCss}" name="startFlightDateCss">
						</li>
						
						<c:choose>
	                    <c:when test="${conditionsMap.arrivalFlightDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
	                    	<input type="hidden" value="${conditionsMap.arrivalFlightDateSort}" name="arrivalFlightDateSort">
	                    	<a onclick="sort(this, 3)">到达日期
	                    		<c:choose>
	                    			<c:when test="${conditionsMap.arrivalFlightDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.arrivalFlightDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose>
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.arrivalFlightDateCss}" name="arrivalFlightDateCss">
	                    </li>
                	</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>
     </form>
		</c:if>
		<table class="table activitylist_bodyer_table" id="contentTable">
			<thead>
				<tr>
					<th width="6%">预定渠道</th>
					<th width="6%">订单编号</th>
					<th width="6%">订单团号</th>
					<th width="6%">参团类型</th>
					<th width="7%"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
					<th width="11%">预订/剩余时间</th>
					<th width="8%">出/截团日期</th>
					<th width="6%">机票类型</th>
					<th width="5%">人数</th>
					<th width="6%">订单状态</th>
					<th width="6%">订单总额</th>
					<th width="6%">已付金额<br>到账金额</th>
					<th width="6%">航班备注</th><!-- 原为备注 经和产品硕宇确定改为航班备注  by chy2015年1月7日15:42:08  -->
					<th width="4%">操作</th>
					<th width="4%">下载</th>
				</tr>
			</thead>
			<tbody>
				
				<c:if test="${fn:length(page.list) <= 0 }">
	                 <tr class="toptr" >
	                 	<td colspan="15" style="text-align: center;"> 暂无搜索结果</td>
	                 </tr>
        		</c:if>
        		
				<c:forEach items="${page.list }" var="order">
				
					<tr class="toptr" <c:if test="${isNeedNoticeOrder == 1}">onclick="changeSeenFlagByMouse('${order.id}', this);"</c:if>>
						<input type="hidden" name="seenFlag" value="${order.seenFlag}" id="order_${order.id}"/>
						<input type="hidden" value="${order.id }"/>
						<td>
							<!-- 
								<c:if test="${not empty order.agentName }">${order.agentName }</c:if>
							 -->
							 <c:if test="${not empty order.agentName }">
								<c:if test="${not empty order.paymentStatus }">
										<c:choose>
											<c:when test="${order.paymentStatus == 2 }"><div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 按月结算 </div></c:when>
											<c:when test="${order.paymentStatus == 3 }"><div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 担保结算 </div></c:when>
											<c:when test="${order.paymentStatus == 4 }"><div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 后付费 </div></c:when>
											<c:otherwise>
												<div class="ycq"> 签约渠道 </div>
												${order.nagentName }
											</c:otherwise>
										</c:choose>
								</c:if>
								<c:if test="${order.paymentStatus eq null }"><div class="ycq"> 非签约渠道 </div></c:if>
							${order.agentName }
							</c:if>
							${order.nagentName }
						</td>
						<td>
							<c:if test="${not empty order.orderNo }">${order.orderNo }</c:if>
							<c:if test="${isNeedNoticeOrder == 1 && order.seenFlag == '0' && fns:getUser().id == order.activityCreateBy}">
								<span class="new-tips" style="inline-block;"></span>
							</c:if>
						</td>
						<td>${order.orderGroupCode }</td>
						<td>
							<c:choose>
								<c:when test="${order.orderType == 1 }">单办</c:when>
								<c:when test="${order.orderType == 2 }">参团</c:when>
							</c:choose>
						</td>
						<td>
					    	<div class="salerId_cen onshow">
								${order.salerName}
							</div>
							<div class="createBy_cen qtip">
								${order.createUserName}
							</div>
						</td>
						<td class="p0">
							<div class="out-date">
								<fmt:formatDate value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<div class="close-date">
								${order.leftDays}
							</div>
						</td>
						
						<td class="p0">
							<div class="out-date">
								<c:if test ="${not empty order.groupOpenDate }"><fmt:formatDate value="${order.groupOpenDate }" pattern="yyyy-MM-dd"/></c:if>
							</div>
							<div class="close-date">
								<c:if test="${not empty order.groupCloseDate }"><fmt:formatDate value="${order.groupCloseDate }" pattern="yyyy-MM-dd"/></c:if>
							</div>
						</td>
						
						<td>
							<c:choose>
								<c:when test="${order.airType==1 }">多段</c:when>
								<c:when test="${order.airType==2 }">往返</c:when>
								<c:when test="${order.airType==3 }">单程</c:when>
							</c:choose>
						</td>
						<td><c:if test="${not empty order.personNum }">${order.personNum }</c:if></td>
						<td>${fns:getDictLabel(order.order_state,"order_pay_status" , "无")}</td>
						<td class="tr"><span class="tdorange fbold">${order.totalMoney}</span></td>
						<td class="p0 tr">
							<div class="yfje_dd">
								<span class="fbold">${order.payedMoney }</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">${order.accountedMoney }</span>
							</div></td>
						<td><c:if test="${not empty order.remark }">${order.remark }</c:if></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_cz.png" title="操作">
								</dt>
								<dd>
									<p>
										<span></span>
										<a target="_blank" onclick="" href="${ctx}/order/manage/airticketOrderDetail?orderId=${order.id}">详情 </a>
										<c:if test="${order.activityLockStatus != 1 && order.lockStatus != 1 && order.order_state == 7}">
											<a href="javascript:void(0)" onClick="confirmOrder(${order.id})">确认占位</a>
						                </c:if>  
										<c:if test="${order.orderType == 1 && order.order_state != 7}">
											<shiro:hasPermission name="airticketOrderForOp:operation:lock">
												<!-- 如果结算单被锁死，则订单不能解锁和锁死 -->
												<c:if test="${order.activityLockStatus != 1 }">
													<c:if test="${order.lockStatus == 1 }">
												        <a href="javascript:unLockOrder(${order.id});">解锁</a>
												    </c:if>
												    <c:if test="${order.lockStatus == 0 || order.lockStatus == null }">
												        <a href="javascript:lockOrder(${order.id});">锁死</a>
												    </c:if>
												</c:if>
										    </shiro:hasPermission>
										    <!-- 如果结算单或订单锁死，则订单不能备注 -->
										    <c:if test="${order.activityLockStatus != 1 && order.lockStatus != 1}">
										    	<a onclick="jbox_hbbzOp(this, '${order.id}','${order.remark}');" href="javascript:void(0)"> 备注</a>
										    </c:if>
										</c:if>
										
										<a onclick="expand('#child${order.id}',this)">展开</a>
										<!-- 如果结算单或订单锁死，则订单不能备注 -->
										<c:if test="${order.activityLockStatus != 1 && order.lockStatus != 1 && order.order_state != 7}">
											<a href="${ctx}/order/lendmoney/airticketLendMoneyApply?orderId=${order.id}"  target="_blank">借款</a>
											<a href="${ctx}/order/lendmoney/borrowAmountList?flowType=19&productType=7&orderId=${order.id}"  target="_blank">借款记录</a>
										</c:if>
										</p>
								</dd>
							</dl></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_xz.png" title="下载">
								</dt>
								<dd>
									<p>
										<span></span> 
										<c:if test="${order.order_state != 7 }">
											<a onclick="" href="${ctx}/order/manage/airticketOrderTravelExport?orderId=${order.id}">游客资料</a>
											<a target="" href="${ctx}/order/manage/airticketOrderNameList?orderId=${order.id}">出票名单</a>
											<a target="" href="#" onclick="downloads(${order.airticketOrderAttachmentId})">确认单</a>
										</c:if>
									</p>
								</dd>
							</dl></td>
					 </tr>
					 	
						 <tr id="child${order.id}" style="display:none;" class="activity_team_top1">
							<td colspan="15" class="team_top" style="background-color:#d1e5f5;">
								<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
									<thead>
										<tr>
											<th width="5%">行程段</th>
											<th width="14%">航空公司</th>
											<th width="8%">航班号</th>
											<th width="14%">出发地机场</th>
											<th width="11%">起飞时间</th>
											<!-- <th width="10%">转机点</th> -->
											<th width="14%">到达城市/机场</th>
											<th width="11%">到达时间</th>
											<th width="7%">舱位</th>
											<th width="10%">价格/人</th>
										</tr>
									</thead>
										<tbody>
										<c:set value="true" var="flag" />
										<c:set value="true" var="flag2" />
										 <c:forEach items="${order.airticketOrderFlights }" var="airticketOrderFlight">
											<tr>
												<td class="tc">${airticketOrderFlight.orderNumber }</td>
												<td class="tc">${fns:getAirlineNameByAirlineCode(airticketOrderFlight.airlines)} </td>
												<td class="tc">${airticketOrderFlight.flight_number}</td>
												<td class="tc">${airticketOrderFlight.startAirportName }</td>
												<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airticketOrderFlight.startTime }" /></td>
												<!-- <td class="tc"></td> -->
												<td class="tc">${airticketOrderFlight.endAirportName }</td>
												<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airticketOrderFlight.arrivalTime }" /></td>
												<td class="tc" style="border-right:1px solid #DDD">${fns:getDictLabel(airticketOrderFlight.spaceGrade,"spaceGrade_Type" , "无")}</td>
												<c:choose>
													<c:when test="${order.airType==1}">
														<c:if test="${order.isSection == 1 }">
														<td class="tc">${airticketOrderFlight.formatedSubAdultPrice}</td>
														</c:if>
														<c:if test="${order.isSection != 1 && flag2 == true}">
														<td class="td" rowspan="${order.airticketOrderFlights.size()}" style="text-align:center; vertical-align:middle;">${order.formatedAdultPrice }</td>
														<c:set value="false" var="flag2" />
														</c:if>
													</c:when>
													<c:when test="${order.airType==3}">
														<td class="tc">${order.formatedAdultPrice }</td>
													</c:when>
													<c:when test="${order.airType==2 && flag == true}">
														<td class="td" rowspan="2" style="text-align:center; vertical-align:middle;">${order.formatedAdultPrice }</td>
														<c:set value="false" var="flag" />
													</c:when>
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
	</div>

	<!-- 分页部分 -->
	<div class="pagination clearFix">
		${page}
	</div>
	
</body>
</html>