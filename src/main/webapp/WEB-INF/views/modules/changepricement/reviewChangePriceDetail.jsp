<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-改价审核</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>


<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
	$(function() {
		//AA码
		AAHover();
		if ("${reply}" != null && "${reply}" != '') {
			alert("${reply}");
		}
	});
	//驳回
	function jbox_bohui_refund3(){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$("#result").val(0);
				$("#denyReason").val(f.reason);
				$("#searchForm").submit();
			}
		},height:250,width:500});
	}
	
	function review(){
		$("#revSucBtn").attr('disabled',true);
		$("#revFailBtn").attr('disabled',true);
		$("#result").val(1);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="mod_nav">审核 > 改价审核 > 改价审核审批</div>
	<div class="ydbz_tit">订单详情</div>
	<div class="orderdetails1">
		<table border="0" width="98%" style="margin-left: 25px">
			<tbody>
				<tr>
					<td class="mod_details2_d1">下单人：</td>
					<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.ordermaker)}</td>
					<td class="mod_details2_d1">下单时间：</td>
					<td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.orderdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td class="mod_details2_d1">团队类型：</td>
					<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.prdtype,'order_type', '无')}</td>
					<td class="mod_details2_d1">收客人：</td>
					<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.ordermaker)}</td>

				</tr>
				<tr>
					<td class="mod_details2_d1">订单编号：</td>
					<td class="mod_details2_d2">${orderDetail.orderno}</td>
					<td class="mod_details2_d1">订单团号：</td>
					<td class="mod_details2_d2">${orderDetail.groupno}</td>
					<td class="mod_details2_d1">销售：</td>
					<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.ordercreate)}</td>
				</tr>
				<tr>
					<td class="mod_details2_d1">订单总额：</td>
					<td class="mod_details2_d2">
						<!-- ¥ --> <em class="tdred">${orderDetail.totalmoney}</em>
					</td>
					<td class="mod_details2_d1">订单状态：</td>
					<td class="mod_details2_d2">
						${fns:getDictLabel(orderDetail.orderstatus,"order_pay_status" , "无")}
					</td>
					<td class="mod_details2_d1">操作人：</td>
					<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.updateby)}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="ydbz_tit">
		<span class="fl">产品信息</span>
	</div>
	<!--签证-->
	<c:if test="${orderDetail.prdtype==6}">
		<!-- div style=" padding-top:20px; color:#f00; font-size:18px;">“签证”产品信息（开发中不需要此代码）</div -->
		<div class="orderdetails2">
			<p class="ydbz_mc">${orderDetail.prdname}</p>
			<ul class="ydbz_info">
				<!-- <li><span>有效期：</span>15~30天</li> 干掉了-->
				<li><span>签证国家：</span>${country.countryName_cn}</li>
				<li><span>签证类别：</span>${visaType.label}</li>
				<li><span>领区：</span>
					<c:if test="${not empty orderDetail.collarea }">
				                           ${fns:getDictLabel(orderDetail.collarea,'from_area','')}
			         </c:if>
				</li>
				<li><span>应收价格：</span>${fns:getCurrencyInfo(orderDetail.visaCurrency, 0, 'mark')}${orderDetail.visapay == null || orderDetail.visapay == "" ? 0 : orderDetail.visapay}</li>
				<li><span>办签人数：</span>${orderDetail.tnum}人</li>
			</ul>
		</div>
	</c:if>
	<c:if test="${orderDetail.prdtype==7 || orderDetail.prdtype==8}">
		<!--机票-->
		<!-- div style=" padding-top:20px; color:#f00; font-size:18px;">“机票”产品信息（开发中不需要此代码）</div -->
		<div class="orderdetails2">
			<c:if test="${orderDetail.type == 2 }">
				<p class="ydbz_mc">往返</p>
				<!--往返-->
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">
							         ${fns:getDictLabel(orderDetail.departureCity, 'from_area', '无')}
							</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">
								<c:forEach items="${arrivedareas}" var="area">
			                        <c:if test="${area.id eq orderDetail.arrivedCity}">
			                            ${area.name}
			                        </c:if>
			                    </c:forEach>
							</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${orderDetail.reservationsNum}人</td>
						</tr>
					</tbody>
				</table>
				<div class="ydbz_tit ydbz_tit_child">去程：</div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(orderDetail.flightInfoList[0].leaveAirport)}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(orderDetail.flightInfoList[0].destinationAirpost)}
							</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.flightInfoList[0].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${orderDetail.flightInfoList[0].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(orderDetail.flightInfoList[0].airlines)}</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.flightInfoList[0].spaceGrade,
								'spaceGrade_Type', '无')}</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.flightInfoList[0].airspace,
								'airspace_Type', '无')}</td>
						</tr>
					</tbody>
				</table>
				<div class="ydbz_tit ydbz_tit_child">返程：</div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(orderDetail.flightInfoList[1].leaveAirport)}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(orderDetail.flightInfoList[1].destinationAirpost)}</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.flightInfoList[1].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${orderDetail.flightInfoList[1].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(orderDetail.flightInfoList[0].airlines)}</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.flightInfoList[1].spaceGrade,
								'spaceGrade_Type', '无')}</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.flightInfoList[1].airspace,
								'airspace_Type', '无')}</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			<!--单程-->
			<c:if test="${orderDetail.type == 3}">
				<p class="ydbz_mc">单程</p>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">
							    ${fns:getDictLabel(orderDetail.departureCity, 'from_area', '无')}
							</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">
								<c:forEach items="${arrivedareas}" var="area">
			                        <c:if test="${area.id eq orderDetail.arrivedCity}">
			                            ${area.name}
			                        </c:if>
			                    </c:forEach>
							</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${orderDetail.reservationsNum}人</td>
						</tr>
					</tbody>
				</table>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(orderDetail.flightInfoList[0].leaveAirport)}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(orderDetail.flightInfoList[0].destinationAirpost)}</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.flightInfoList[0].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${orderDetail.flightInfoList[0].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">
							${fns:getAirlineNameByAirlineCode(orderDetail.flightInfoList[0].airlines)}
								</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.flightInfoList[0].spaceGrade,
								'spaceGrade_Type', '无')}</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.flightInfoList[0].airspace,
								'airspace_Type', '无')}</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			<c:if test="${orderDetail.type == 1 }">
				<!--多段-->
				<p class="ydbz_mc">多段</p>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">
							    ${fns:getDictLabel(orderDetail.departureCity, 'from_area', '无')}
							</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">
							<c:forEach items="${arrivedareas}" var="area">
			                        <c:if test="${area.id eq orderDetail.arrivedCity}">
			                            ${area.name}
			                        </c:if>
			                    </c:forEach>
							</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${orderDetail.reservationsNum}人</td>
						</tr>
					</tbody>
				</table>
				<c:forEach items="${orderDetail.flightInfoList }" var="flightInfo">
					<div class="ydbz_tit ydbz_tit_child">第${flightInfo.number}段：</div>
					<!-- ${flightInfo.ticket_area_type} -->
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${fns:getAirportName(flightInfo.leaveAirport)}</td>
								<!-- ${flightInfo.leaveAirport} -->
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${fns:getAirportName(flightInfo.destinationAirpost)}</td>
								<!-- ${flightInfo.destinationAirpost} -->
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">航空公司：</td>
								<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(orderDetail.flightInfoList[0].airlines)}</td>
								<td class="mod_details2_d1">舱位等级：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,
									'spaceGrade_Type', '无')}</td>
								<td class="mod_details2_d1">舱位：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,
									'airspace_Type', '无')}</td>
							</tr>
						</tbody>
					</table>
				</c:forEach>
			</c:if>
			<div class="mod_information_d7"></div>
			<ul class="ydbz_dj specialPrice">
				<li style="display: none;"><input type="text" class="required"
					value="0" onafterpaste="this.value=this.value.replace(/\D/g,'')"
					onkeyup="this.value=this.value.replace(/\D/g,'')"
					id="orderPersonelNum">
				</li>
				<li><span class="ydtips">单价</span>
					<p>
						成人：<font color="#FF0000">¥${orderDetail.settlementAdultPrice}</font>
						元
					</p>
					<p>
						儿童：<font color="#FF0000">¥${orderDetail.settlementcChildPrice}</font>
						元
					</p>
					<p>
						特殊人群：<font color="#FF0000">¥${orderDetail.settlementSpecialPrice}</font>
						元
					</p>
				</li>
				<c:if test="${orderDetail.prdtype==7}">
					<li><span class="ydtips"> 出行人数</span>
						<p>
							成人：<span>${orderDetail.adult_num}</span> 人
						</p>
						<p>
							儿童：<span>${orderDetail.child_num}</span> 人
						</p>
						<p>
							特殊人群：<span>${orderDetail.special_num}</span> 人
						</p>
					</li>
				</c:if>
				<li class="ydbz_single"><span class="">税费：</span>${orderDetail.taxamt}元/人
				</li>
			</ul>
		</div>
	</c:if>
	<!--散拼-->
	<c:if test="${orderDetail.prdtype == 2}">
		<!-- <div style=" padding-top:20px; color:#f00; font-size:18px;">“散拼”产品信息（开发中不需要此代码）</div> -->
		<div class="orderdetails2">
			<p class="ydbz_mc">${orderDetail.prdname}</p>
			<ul class="ydbz_info">
				<li><span>团号：</span>${orderDetail.groupno}</li>
				<li><span>出团日期：</span>${orderDetail.opendate}</li>
				<li title="${orderDetail.targetarea}" id="mddtargetAreaNames"><span>目的地：</span>${orderDetail.targetarea}</li>
				<li><span>出发城市：</span>${fns:getDictLabel(orderDetail.fromarea, 'from_area', '无')}</li>
				<li><span>行程天数：</span>${orderDetail.tradays}天</li>
<!-- 				<li><span style="width:171px;">创建时间（补单时填写）：</span>${orderDetail.tracreatedate}</li> -->
				<li><span>预收人数：</span>${orderDetail.pposition}</li>
				<li><span>余位人数：</span>${orderDetail.fposition}</li>
				<!-- <li><span>预报名数：</span>96</li> -->
			</ul>
			<ul class="ydbz_dj specialPrice">
				<li><span class="ydtips">单价</span>
					<p>成人：${fns:getMoneyAmountBySerialNum(orderDetail.settlementAdultPrice,2)}</p>
					<p>儿童：${fns:getMoneyAmountBySerialNum(orderDetail.settlementcChildPrice,2)}</p>
<!-- 					<p>特殊人群：¥${orderDetail.settlementSpecialPrice}元</p></li> -->
					<p>特殊人群：${fns:getMoneyAmountBySerialNum(orderDetail.settlementSpecialPrice,2)}</p></li>
				<li><span class="ydtips"> 出行人数</span>
					<p>
						成人：<span>${orderDetail.nadult}</span> 人
					</p>
					<p>
						儿童：<span>${orderDetail.nchild}</span> 人
					</p>
					<p>
						特殊人群：<span>${orderDetail.nspecial}</span> 人
					</p>
				</li>
			</ul>
		</div>
	</c:if>
	<!--散拼切位-->
	<c:if test="${orderDetail.prdtype == 9 }">
		<!-- <div style=" padding-top:20px; color:#f00; font-size:18px;">“散拼”产品信息（开发中不需要此代码）</div> -->
		<div class="orderdetails2">
			<p class="ydbz_mc">${orderDetail.prdname}</p>
			<ul class="ydbz_info">
				<li><span>团号：</span>${orderDetail.groupno}</li>
				<li><span>出团日期：</span>${orderDetail.opendate}</li>
				<li title="${orderDetail.targetarea}" id="mddtargetAreaNames"><span>目的地：</span>${orderDetail.targetarea}</li>
				<li><span>出发城市：</span>${fns:getDictLabel(orderDetail.fromarea, 'from_area', '无')}</li>
				<li><span>行程天数：</span>${orderDetail.tradays}天</li>
				<li><span style="width:171px;">创建时间（补单时填写）：</span>${orderDetail.tracreatedate}</li>
				<c:if test="${orderDetail.type == 9 }">	
					<li><span>已切位数：</span>${orderDetail.payreserve}</li>
				</c:if>
				<!-- <li><span>预报名数：</span>96</li> -->
			</ul>
			<ul class="ydbz_dj specialPrice">
				<li><span class="ydtips">单价</span>
					<p>成人：¥${orderDetail.settlementAdultPrice} 元</p>
					<p>儿童：¥${orderDetail.settlementcChildPrice} 元</p>
					<p>特殊人群：¥${orderDetail.settlementSpecialPrice}元</p></li>
			</ul>
		</div>
	</c:if>
	<!--参团-->
	<c:if test="${orderDetail.prdtype == 1 || orderDetail.prdtype == 3 || orderDetail.prdtype == 4 || orderDetail.prdtype == 5 || orderDetail.prdtype == 10}">
	<!-- <div style=" padding-top:20px; color:#f00; font-size:18px;">“单团”产品信息（开发中不需要此代码）</div> -->
	<div class="orderdetails2">
		<p class="ydbz_mc">${orderDetail.prdname}</p>
		<ul class="ydbz_info">
			<li>
				<span>出发城市：</span>
				<c:if test="${orderDetail.modaltype == null || orderDetail.modaltype == 0 }">
					${fns:getDictLabel(orderDetail.fromarea, 'from_area', '无')}
				</c:if>
				<c:if test="${orderDetail.modaltype == 1 }">
					全国联运
				</c:if>
				<c:if test="${orderDetail.modaltype == 2 }">
					分区联运
				</c:if>
			</li>
			<li><span>出团日期：</span>${orderDetail.opendate}</li>
			<li><span>行程天数：</span>${orderDetail.tradays}天</li>
			<c:if test="${orderDetail.prdtype ne '10'}">
				<li><span>离境城市：</span>${orderDetail.outarea}</li>
			</c:if>
			<!-- <li><span>游客出发区域：</span>上海</li> -->
			<li class="orderdetails2_text" title="${orderDetail.targetarea}" id="mddtargetAreaNames"><span>目的地：</span>${orderDetail.targetarea}</li>
		</ul>
<!-- 兼容游轮 注释掉了这里 替换为下面的模块
		<ul class="ydbz_dj specialPrice">
			<li><span class="ydtips">单价</span>
				<p>成人：¥${orderDetail.settlementAdultPrice} 元</p>
				<p>儿童：¥${orderDetail.settlementcChildPrice} 元</p>
				<p>特殊人群：¥${orderDetail.settlementSpecialPrice} 元</p></li>
			<li><span class="ydtips"> 出行人数</span>
				<p>
					成人：<span>${orderDetail.nadult}</span> 人
				</p>
				<p>
					儿童：<span>${orderDetail.nchild}</span> 人
				</p>
				<p>
					特殊人群：<span>${orderDetail.nspecial}</span> 人
				</p></li>
		</ul> -->
		<ul class="ydbz_dj specialPrice">
		<c:choose>
			<c:when test="${orderDetail.prdtype == '10'}">
				<li><span class="ydtips">单价</span>
					<p>1/2人同行价：${fns:getMoneyAmountBySerialNum(orderDetail.settlementAdultPrice,2)}</p>
					<p>&nbsp;</p>
					<p>3/4人同行价：${fns:getMoneyAmountBySerialNum(orderDetail.settlementcChildPrice,2)}</p>
				</li>
				<li><span class="ydtips"> 出行人数</span>
					<p>1/2人出行人数：<span>${orderDetail.nadult}</span> 人</p>
					<p>&nbsp;</p>
					<p>3/4人出行人数：<span>${orderDetail.nchild}</span> 人</p>
				</li>
				<li style="background:none;">
			       <p>&nbsp;</p>
			       <p>总计：<span>${orderDetail.roomNumber}</span> 间  </p>
			       <p>&nbsp;</p>
			    </li>
			</c:when>
			<c:otherwise>
				<li><span class="ydtips">单价</span>
					<p>成人：${fns:getMoneyAmountBySerialNum(orderDetail.settlementAdultPrice,2)}</p>
					<p>儿童：${fns:getMoneyAmountBySerialNum(orderDetail.settlementcChildPrice,2)}</p>
					<p>特殊人群：${fns:getMoneyAmountBySerialNum(orderDetail.settlementSpecialPrice,2)}</p>
				</li>
				<li><span class="ydtips"> 出行人数</span>
					<p>成人：<span>${orderDetail.nadult}</span> 人</p>
					<p>儿童：<span>${orderDetail.nchild}</span> 人</p>
					<p>特殊人群：<span>${orderDetail.nspecial}</span> 人</p>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
	</div>
	</c:if>
	<!--参团结束-->
	<div class="ydbz_tit">特殊需求</div>
	<div class="orderdetails3">
		<div class="ydbz2_lxr">
			<p>
				<label>特殊需求：</label> <span class="tj-hbbz-ys">${orderDetail.comments}</span>
			</p>
		</div>
	</div>
	<div class="ydbz_tit">预订人信息</div>
	<div class="orderdetails4">
		<p class="ydbz_qdmc">预订渠道：${fns:getAgentName(orderDetail.qdid)}</p>
		<c:forEach items="${orderContacts}" var="orderContact">
			<ul class="ydbz_qd">
				<li><label>渠道联系人<font>1</font>：</label>${orderContact.contactsName}</li>
				<li class="ydbz_qd_lilong"><label>渠道联系人电话：</label>${orderContact.contactsTel}
					<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
				</li>
				<li flag="messageDiv" class="ydbz_qd_close">
					<ul>
						<li><label>固定电话：</label>${orderContact.contactsTixedTel}</li>
						<li><label>渠道地址：</label>${orderContact.contactsAddress}</li>
						<li><label>传真：</label>${orderContact.contactsFax}</li>
						<li><label>QQ：</label>${orderContact.contactsQQ}</li>
						<li><label>Email：</label>${orderContact.contactsEmail}</li>
						<li><label>渠道邮编：</label>${orderContact.contactsZipCode}</li>
						<li><label>其他：</label>${orderContact.remark}</li>
					</ul>
				</li>
			</ul>
		</c:forEach>
	</div>
	<c:if test="${reviewdetail.travelerid != 0 && reviewdetail.travelerid != -1}">
	<div class="ydbz_tit">
		<span class="fl">游客改价</span><span class="fr wpr20">报批日期：<fmt:formatDate value="${reviewdetail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</span>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="7%">姓名</th>
				<th width="13%">币种</th>
				<th width="15%">原始应收价</th>
				<th width="15%">当前应收价</th>
				<th width="15%">改价差额</th>
				<th width="25%">备注</th>
				<th width="15%">改后应收价</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td >${reviewdetail.travelername}</td>
				<td>${reviewdetail.currencyname}</td>
				<td class="tr">${reviewdetail.oldtotalmoney}</td>
				<td class="tr">${reviewdetail.curtotalmoney}</td>
				<td class="tr">
				<fmt:formatNumber type="currency" pattern="#0.00" value="${reviewdetail.changedprice}" />
				</td>
				<td>${reviewdetail.remark}</td>
				<td class="tr"><fmt:formatNumber type="currency" pattern="#0.00" value="${reviewdetail.changedtotalmoney}" /></td>
			</tr>
		</tbody>
	</table>
	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
		<div class="fr f14 all-money">
			游客改价差额：<span id="totalTravelerPlus">
<!-- 			<font class="gray14" flag="bz">美元</font> -->
			<span class="f20 tdred">${fns:getCurrencyInfo(orderDetail.visaCurrency, 0, 'mark')}
			<fmt:formatNumber type="currency" pattern="#0.00" value="${reviewdetail.changedprice}" />
			</span>
			</span>
		</div>
	</div>
	</c:if>
	<c:if test="${reviewdetail.travelerid == -1 }">
	<div class="ydbz_tit">
		<span class="fl">订金改价</span>
	</div>
	<table class="activitylist_bodyer_table modifyPrice-table">
		<thead>
			<tr>
				<th width="7%">款项</th>
				<th width="13%">币种</th>
				<th width="15%">原始订金</th>
				<th width="15%">当前订金</th>
				<th width="15%">改价差额</th>
				<th width="25%">备注</th>
				<th width="15%">改后订金</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>${reviewdetail.changedfund}</td>
				<td>${reviewdetail.currencyname}</td>
				<td class="tr">${reviewdetail.oldtotalmoney}</td>
				<td class="tr">${reviewdetail.curtotalmoney}</td>
				<td class="tr">
				<fmt:formatNumber type="currency" pattern="#0.00" value="${reviewdetail.changedprice}" />
				</td>
				<td>${reviewdetail.remark}</td>
				<td class="tr">${reviewdetail.changedtotalmoney}</td>
			</tr>
		</tbody>
	</table>
	</c:if>
	<c:if test="${reviewdetail.travelerid == 0 }">
	<div style="margin-top:20px;"></div>
	<div class="ydbz_tit">
		<span class="fl">团队改价</span>
	</div>
	<div class="groupModify-detail">
		<ol class="gai-price-ol">
			<li><i>${reviewdetail.changedfund}</i> 
			<i>${reviewdetail.currencyname}${reviewdetail.changedprice}</i> 
			<i>${reviewdetail.remark}</i></li>
		</ol>
	</div>

<!-- 	<div class="ydbz_tit"> -->
<!-- 		<span class="fl">备注</span> -->
<!-- 	</div> -->
<!-- 	<dl class="gai-price-tex"> -->
<!-- 		<dd>${reviewdetail.remark}</dd> -->
<!-- 	</dl> -->
	</c:if>
	<div class="allzj tr f18">
		改前金额：<span class="f20">${fns:getCurrencyInfo(reviewdetail.currencyid, 0, 'mark')}${reviewdetail.curtotalmoney}</span><br />
		改价差额：<span class="f20">${fns:getCurrencyInfo(reviewdetail.currencyid, 0, 'mark')}
			<fmt:formatNumber type="currency" pattern="#0.00" value="${reviewdetail.changedprice}" />
		</span><br /><!-- orderDetail.visaCurrency -->
		<div class="all-money">
			改后总额：<span class="f20">${fns:getCurrencyInfo(reviewdetail.currencyid, 0, 'mark')}<fmt:formatNumber type="currency" pattern="#0.00" value="${reviewdetail.changedtotalmoney}" /></span>
		</div>
	</div>
	<c:if test="${flag == 0}">
		<div class="dbaniu">
		<!-- 添加提交请求所需数据 -->
		<form id="searchForm" action="${ctx}/changePricement/changePriceReview" method="post">
				<input type = "hidden" id = "revId" name="revId" value = "${reviewdetail.id}"/>
				<input type = "hidden" id = "nowlevel" name="nowlevel" value = "${nowlevel}"/>
				<input type = "hidden" id = "result" name="result"/>
				<input type = "hidden" id = "denyReason" name="denyReason"/>
				<input type = "hidden" id = "orderId" name="orderId" value = "${orderDetail.orderid}"/>
				<input type = "hidden" id = "orderTypeSub" name="orderTypeSub" value = "${orderDetail.prdtype}"/>
				<input type = "hidden" id = "travelerId" name="travelerId" value = "${reviewdetail.travelerid}"/>
				<input type = "hidden" id = "moneyAmount" name="moneyAmount" value = "${reviewdetail.changedprice}"/>
				<input type = "hidden" id = "currencyId" name="currencyId" value = "${reviewdetail.currencyid}"/>
				<input type = "hidden" id = "flag" name="flag" value = "${flag}"/>
			<a class="ydbz_s gray" href = "${ctx}/changePricement/changePriceReviewList">返回</a> 
			<a class="ydbz_s" id="revFailBtn" onclick="jbox_bohui_refund3();">驳回</a> 
			<input type="button" id="revSucBtn" value="审核通过" onclick="review()" class="btn btn-primary">
		</form>
		</div>
	</c:if>
	<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
	<!--右侧内容部分结束-->
</body>
</html>
