<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<title>订单-单办机票改签申请</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">

	function closeCurWindow(){
		this.close();
	}
	 var names="";
	 function query(obj){
	 
	 if($("#product_code").val()==""){
	 alert("请输入产品编码");
	 return;
	 }
	 	names="";
	 	$("#names").html(names);
	    $(".introselect").each(function() { 
						if($(this).attr("checked")){
							names+=this.name+",";
						}
		});
	    if (names != "" && names.lastIndexOf(",") == names.length -1) {
			names = names.substring(0, names.length-1)
		}
	 	$("#names").html(names);
	 	//alert($("#product_code").val());
	 	$.ajax({    
	 				type: "POST",                 
					url:"${ctx}/order/manage/ajaxQueryProduct",                 
					data:{product_code:$("#product_code").val()},                
					error: function(request) {                     
					},      
					dataType:"json",           
					success: function(msg) {    
						  $("#content").html(msg.html);
						  $("#freePosition").val(msg.freePosition);
						  $("#newProId").val(msg.newProId);
						  //$("#newProId").html(data.newProId);
					}             
		});
	 	
	 	$(obj).parent().find("div").show();
	 }
	 
	 $(function(){
		$("#selectall").click(function() { 
		$("#names").html("");
			if($(this).attr("checked")){
				$(".introselect").each(function() { 
					if(!$(this).attr("checked")){
						names +=this.name+",";
					}
					$(this).attr("checked", true); 
					});
			}else{
				$(".introselect").each(function() { 
				$(this).attr("checked", false); 
				names ="";
			});
			}
			if (names != "" && names.lastIndexOf(",") == names.length -1) {
				names = names.substring(0, names.length-1)
			}
			$("#names").html(names);
		});
		$("#submit").click(function() {
			if($("#content").html()==""){
			 alert("请查找改签产品");
			 return;
	 		}
			var ids="";
			$(".introselect").each(function() { 
						if($(this).attr("checked")){
							ids+=this.value+",";
						}
			});
			
			if(ids==""){
				alert("请选择改签乘客");
				return;
			}
			if(parseInt($("#freePosition").val())==0){
				alert("产品余位不足");
				return;
			}
			//验证互斥
			$.ajax({
		            type: "POST",
		            url: "${ctx}/refundReview/beforeAddReview",
		            data: {
		            orderId : $("#orderId").val(),
		            travelerids : ids,
		            reviewFlowId : 14//改签	            
		            },
		            async : false,
		            success: function(msg){
		            	if(msg.result == ''){
		            		submitChange();
		            	}else{
		            		top.$.jBox.tip(msg.result);
		            	}
		            }
		        });
		});
		//改签的方法提交
		function submitChange(){
		 if($("#content").html()==""){
			 alert("请查找改签产品");
			 return;
	 		}
			var ids="";
			$(".introselect").each(function() { 
						if($(this).attr("checked")){
							ids+=this.value+",";
						}
			});
			
			if(ids==""){
				alert("请选择改签乘客");
				return;
			}
			if(parseInt($("#freePosition").val())==0){
				alert("产品余位不足");
				return;
			}
			$.ajax({    
					async: false,//配置同步请求
	 				type: "POST",                 
					url:"${ctx}/order/manage/areaGaiQianCheck",                 
					data:{travelerIds:ids,newProId:$("#newProId").val(),orderId:$("#orderId").val()},                
					dataType:"json",           
					success: function(data) { 
						if(data.result=='1'){
							$.ajax({    
				 				type: "POST",                 
								url:"${ctx}/order/manage/areaGaiQian",                 
								data:{travelerIds:ids,newProId:$("#newProId").val(),orderId:$("#orderId").val()},                
								dataType:"json",           
								success: function(data) { 
									alert(data.result);   
									window.opener.location.reload();
									window.close();
								},    
								error: function(request) {                     
								}         
							});
						}else{
							alert(data.msg);
						}  
					},    
					error: function(request) {                     
					}         
			});
		}
		
		$(".introselect").click(function() {
			if ($(this).is(":checked")) {
				// 				names = names.replace(new RegExp(this.name+",","gm"), "") + this.name+",";
				if (names.indexOf('this.name') != -1) {
					return;
				} else {
					if(names==''){
						names=this.name;
					}
					else{
						names = names + ',' + this.name;
					}
					
				}
			} else {
				//names = names.replace(this.name+",", "");
// 				names = names.replace(new RegExp(this.name + ",", "gm"), "");
				names='';
				$('.activitylist_bodyer_table input[name!="selectall"]:checked').each(function(){
					var $this=$(this);
					names+=this.name+',';
				})
			}
			if (names != "" && names.lastIndexOf(",") == names.length - 1) {
				names = names.substring(0, names.length - 1)
			}
			$("#names").html(names);
		});
		
		
		
	});

	
</script>

</head>

<body>
	<!-- tab 
	
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>-->
	
	<!--右侧内容部分开始-->
	<div class="ydbzbox fs">
		<div class="orderdetails">
			<div class="orderdetails_tit">
				<span></span>订单信息
				<input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
			</div>
			<div class="orderdetails1">
				<table border="0" width="90%" style="margin-left:0;">
					<tbody>
						<tr>
							<td class="mod_details2_d1">下单人：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.userName}</td>
							<td class="mod_details2_d1">销售：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.salerName }</td>
							<td class="mod_details2_d1">下单时间：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">操作人：</td>
							<td class="mod_details2_d2">
							<!-- ${orderDetailInfoMap.userName} //以前用这个-->
							${orderDetailInfoMap.activityCreateName}
							</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">订单编号：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.orderNo }</td>
							<td class="mod_details2_d1">订单团号：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.orderGroupCode }</td>
							<td class="mod_details2_d1">团队类型：</td>
							<td class="mod_details2_d2">
								<c:choose>
									<c:when test="${orderDetailInfoMap.type == 1 }">单办</c:when>
									<c:when test="${orderDetailInfoMap.type == 2 }">参团</c:when>
								</c:choose>
							</td>
						</tr>
						<c:if test="${orderDetailInfoMap.type == 2 }">
							<tr>
								<td class="mod_details2_d1">参团订单编号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td>
								<td class="mod_details2_d1">参团团号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
			
			
			
			
			<div class="mod_information_dzhan" style="overflow:hidden;">
				<div class="mod_information_dzhan_d mod_details2_d">
						<c:choose>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 2 }">
								<!--往返-->
								<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（往返）</span>
								<div class="mod_information_d7"></div>
								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(orderDetailInfoMap.departureCity,"from_area" , "")}</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">${fns:findAreaNameById(orderDetailInfoMap.arrivedCity)}</td>
											<td class="mod_details2_d1">预收人数：</td>
											<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
										 </tr>
									 </tbody>
								 </table>
								 
								<c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
									<div class="title_samil">
										<c:choose>
											<c:when test="${flightInfo.orderNumber==1 }">去程：</c:when>
											<c:when test="${flightInfo.orderNumber==2 }">回程：</c:when>
										</c:choose>
									</div>
									
									<table width="90%" border="0">
									<tbody><tr>
										<td class="mod_details2_d1">出发机场：</td>
										<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
										<td class="mod_details2_d1">到达机场：</td>
										<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
									
								</c:forEach> 
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 1 }">
								<!--单程-->
								<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（单程）</span>
								<div class="mod_information_d7"></div>
								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
											 	${fns:getDictLabel(orderDetailInfoMap.departureCity,"from_area" , "")} 
												<!--${fromArea}-->
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">${fns:findAreaNameById(orderDetailInfoMap.arrivedCity)}</td>
											<td class="mod_details2_d1">预收人数：</td>
											<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
										 </tr>
									 </tbody>
								 </table>
								 
								 <c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
									<table width="90%" border="0">
									<tbody><tr>
										<td class="mod_details2_d1">出发机场：</td>
										<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
										<td class="mod_details2_d1">到达机场：</td>
										<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2"> ${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
								</c:forEach>
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) >= 3 }">
								<!--多段-->
								<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（多段）</span>
								<div class="mod_information_d7"></div>
								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(orderDetailInfoMap.departureCity,"from_area" , "")}</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">${fns:findAreaNameById(orderDetailInfoMap.arrivedCity)}</td>
											<td class="mod_details2_d1">预收人数：</td>
											<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
										 </tr>
									 </tbody>
								 </table>
								 <c:forEach items="${orderDetailInfoMap.flightInfoList }" var="flightInfo">
									 <div class="title_samil">第${flightInfo.orderNumber }段：
									 	<c:choose>
									 		<c:when test="${flightInfo.ticketAreaType == 1 }">内陆</c:when>
									 		<c:when test="${flightInfo.ticketAreaType == 2 }">国际</c:when>
									 		<c:when test="${flightInfo.ticketAreaType == 3 }">内陆+国际</c:when>
									 	</c:choose>
									 </div>
									 <table width="90%" border="0">
									<tbody><tr>
										<td class="mod_details2_d1">出发机场：</td>
										<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
										<td class="mod_details2_d1">到达机场：</td>
										<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
								</c:forEach>
							</c:when>
						</c:choose>
					<div class="mod_information_d7"></div>
					
					<ul class="ydbz_dj specialPrice">
						<li style="display: none;">
                  			<input type="text" class="required" value="${orderDetailInfoMap.personNum}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" id="orderPersonelNum">
                       </li>
						<li>
							<span class="ydtips">单价</span>
							<input id="aPrice" type="hidden" value="${orderDetailInfoMap.adultPrice }">
							<input id="cPrice" type="hidden" value="${orderDetailInfoMap.childPrice }">
							<input id="sPrice" type="hidden" value="${orderDetailInfoMap.specialPrice }">
							<p>成人：<font color="#FF0000">${orderDetailInfoMap.adultPrice }</font> </p>
							<p>儿童：<font color="#FF0000">${orderDetailInfoMap.childPrice }</font> </p>
							<p>特殊人群：<font color="#FF0000">${orderDetailInfoMap.specialPrice }</font> </p>
						</li>
						 <li><span class="ydtips"> 出行人数</span>
							<p>成人：<span>${orderDetailInfoMap.adultNum }</span> 人</p>
							<p>儿童：<span>${orderDetailInfoMap.childNum }</span> 人</p>
							<p>特殊人群：<span>${orderDetailInfoMap.specialNum }</span> 人</p>
						 </li>
						 <li class="ydbz_single">
						 <span class="">税费：</span>${orderDetailInfoMap.taxamt }人
						 </li>
					</ul>
				</div>
			</div>
			
			<div class="orderdetails_tit">
				<span>2</span>机票
			</div>
			<div flag="messageDiv" class="ydbz2_lxr">
				<form class="contactTable">
					<p>
						<label style="vertical-align:top;width:300px;text-align:center;">航班备注：<c:if test="${ not empty orderDetailInfoMap.remark }">${orderDetailInfoMap.remark }</c:if>无</label>
					</p>
				</form>
			</div>
			
			
			<div class="ydbz_tit">游客改签</div>
                        <table class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						<th width="5%" class="table_borderLeftN">全选
						<input  id="selectall" type="checkbox" name="selectall" ></th>
						 <th width="15%">姓名</th>
						 <th width="15%">是否联运</th>
						 <th width="15%">联运类型</th>
						 <th width="15%">联运价格</th>
						 <th width="25%">身份证号</th>
					  </tr>
				   </thead>
				   <tbody>
				    <c:forEach items="${orderDetailInfoMap.travelerStrategy}" var="travelerStrategy">
					  <tr>
						 <td class="table_borderLeftN">
						 <input type="checkbox" id="traId${travelerStrategy.id}" class="introselect" name="${travelerStrategy.name}" value="${travelerStrategy.id}"></td>
						 <td>${travelerStrategy.name}</td>
						 <td>
						 <c:if  test="${travelerStrategy.intermodalId eq null}">
						 否
						 </c:if>
						 <c:if  test="${travelerStrategy.intermodalId ne null}">
						 是
						 </c:if>
						</td>
						 <td> <c:if  test="${travelerStrategy.intermodalId eq null}">
						无联运
						 </c:if>
						 <c:if  test="${travelerStrategy.type==1}">
						 全国
						 </c:if>
						 <c:if  test="${travelerStrategy.type==2}">
						 分区
						 </c:if>
						 </td>
						 <td class="tr"><font color="#FF0000">${travelerStrategy.price}</font></td>
						 <td>${travelerStrategy.idCard}</td>
					  </tr>
					  </c:forEach>
				   </tbody>
				</table>
             <div class="changeGroup">改签机票产品编号：<input type="text" id="product_code"/>
             <input type="button" value="查找" onclick="query(this)" class="btn btn-primary ydbz_x">
              <div style="display:none">
              <p class="ydbz_tit">机票信息</p>
               <table border="0" style="margin-bottom:5px;">
						<tbody>
							<tr>
								<td class="mod_details2_d1">改签人：</td>
						        <td class="mod_details2_d2"><span id="names">张三、李四</span></td>
								<td class="mod_details2_d1">操作人：</td>
								<td class="mod_details2_d2">${operate}</td>   		        
							</tr>
						</tbody>
					</table>
                            <div id="content"></div>
               </div>   
             </div></div>
           <input type='hidden' id="travelerIds"/>
           <input type='hidden' id="newProId"/>
           <input type='hidden' id="freePosition" name="freePosition"/>
               <div class="ydBtn ydBtn2">
               <a class="ydbz_s" onClick="closeCurWindow();">关闭</a><a class="ydbz_s" id="submit">申请改签</a>
            </div>
	</div>
	<!--右侧内容部分结束-->

</body>


</html>
