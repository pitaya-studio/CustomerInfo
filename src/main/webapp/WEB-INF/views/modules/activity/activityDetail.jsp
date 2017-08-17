<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>产品详情</title>
<meta name="decorator" content="wholesaler" />
<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
#pricePlanTable{
	margin-top:10px; /*bug16435*/
}
/*.divScrollBar{background-color:#ddd;position:absolute;opacity:0.5; filter:Alpha(opacity=50);}*/
/*.divScrollBar:hover{opacity:1; filter:Alpha(opacity=100);}*/
/*.divScrollBar div{background-color:#aaa; position:absolute; left:0px; top:0px;}*/

<%--bug 16327 Start--%>
<%--散拼产品详情页面，修改了目的地的颜色，和鼠标指针的样式--%>
.targetname{
	display:block;
	color: #009535;
	cursor: pointer;
	width:200px;
	text-overflow:ellipsis;
	overflow:hidden;
	white-space:nowrap;
}
<%--bug 16327 End--%>
</style>
	<%--<script type="text/javascript" src="${ctxStatic}/js/jsScroll.js"></script>--%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/jquery.validate.extension.js"
	type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/minified/jquery-ui.min.js"
			type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/modules/activity/groupPriceDetail.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/activity/activity.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/modules/product/discount-setting.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
<script type="text/javascript">

	var delGroupIds ="";
	var delOtherFileIds = "";
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
	function lineOne(state) {
		var onOrOff = "";
		if(state){
			onOrOff="onLineOne";
		}else{
			onOrOff="offLineOne";
		}
		top.$.jBox.confirm('确认修改此产品状态？','系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
					window.location.assign("${ctx}/activity/manager/"+onOrOff+"/${travelActivity.id}");
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
	}

	$(document).ready(function() {

			$.getJSON("${ctx}/activity/manager/loadvisas/"+$("#proId").val(),
					function(result){
						var a = "<c:url value="/static/images/team_word1.png"/>";
						var b = "<c:url value="/static/images/team_word2.png"/>";
						var visas = result.visas;
						var docs = result.docs;
						var html = "";
						/*if(visas.length!=0 && docs.length!=0){
							html = "<div class=\"mod_information_dzhan_d\"><div class=\"mod_information_d8_1\"><label>签证资料：</label> </div></div>";
							$(".team_ins").children("div").last().children().last().after(html);
							$(visas).each(function(i,visa){
								html = "<div style=\"margin-top:8px;\" class=\"mod_information_dzhan_d\">"+
								"<select id=\"country"+i+"\" name=\"country\" style=\"display:none;\">"+$("#template").find("select[name='country']").html()+"</select>"+
								"<div class=\"mod_information_d8_2\"><label class=\"f2"+i+"\"></label><img src=\"${ctxStatic}/images/shangchuanbiaoqian_11.gif\"><select id=\"visaType"+i+"\" name=\"visaType\"  style=\"display:none;\" disabled=\"disabled\">"+$("#template").find("select[name='visaType']").html()+"</select>";
								$(docs).each(function(j,doc){
									if(visa.srcDocId == doc.id){
										html += "<label name=\"signmaterial_file\">"+doc.docName+"</label>";
										html = html + "<a style=\"margin-left:10px;\" href=\"javascript:void(0)\" onClick=\"downloads('"+doc.id+"')\">预览</a></div>";
										return false;
									}else{
										if(j == docs.length-1){
											html = html + "<a href=\"javascript:void(0)\"><img alt=\"下载\" src=\""+b+"\"/></a>";
										}
									}
								});
								html = html + "</div>";
								$(".team_ins").children("div").last().children().last().after(html);
								$("#country"+i+" option[value='"+visa.countryId+"']").attr("selected",true);
								$("#visaType"+i+" option[value='"+visa.visaType+"']").attr("selected",true);
								$(".f2"+i).text($("#country"+i+" option[value='"+visa.countryId+"']").text()+"<"+$("#visaType"+i+" option[value='"+visa.visaType+"']").text()+">：");
							});	
						}*/
						$(".hover-title").niceScroll({
							cursorcolor: "#ccc",//#CC0071 光标颜色
							cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
							touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
							cursorwidth: "5px", //像素光标的宽度
							cursorborder: "0", //     游标边框css定义
							cursorborderradius: "5px",//以像素为光标边界半径
							autohidemode: false //是否隐藏滚动条
						});
						$("#targetAreaName").hover(function(){
							$(".hover-title").show();
						},function(){
							$(".hover-title").hide();
						})
					}
		
			
			);
			
			var ids = "";
			var names = "";
			<c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
			 	ids = ids + "${data.id}"+",";
			 	names = names +"${data.name}"+",";
			</c:forEach>
			//$("#targetAreaName").text(names.toString());
<%--			$("#targetAreaName").attr('title',names.toString().substring(0,names.length-1));--%>


		//可见用户
		var userNames = "";
		<c:forEach var="data" items="${travelActivity.opUserList}" varStatus="d">
			 userNames = userNames +"${data.name}"+",";
		</c:forEach>
		$("#opUserName").text(userNames.toString().substring(0,userNames.length-1));

		//机票产品信息
		<c:if test="${not empty travelActivity.activityAirTicket.id}">
			getAirTicketInfo("${travelActivity.activityAirTicket.productCode}","${ctx}");
		</c:if>
		$(".hover-title").niceScroll({
			cursorcolor: "#ccc",//#CC0071 光标颜色
			cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
			touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
			cursorwidth: "5px", //像素光标的宽度
			cursorborder: "0", //     游标边框css定义
			cursorborderradius: "5px",//以像素为光标边界半径
			autohidemode: false //是否隐藏滚动条
		})
		$("#targetAreaName").hover(function(){
			$(".hover-title").show();
		},function(){
			$(".hover-title").hide();
		})


		});


	</script>
<style type="text/css">
<%--
.main-right {
	min-width: 1650px;
}

--%>
<%--
a.a_none {
	display: inline-block;
	border: 1px solid #ccc;
	border-radius: 4px;
	width: 20px;
	text-align: center;
	vertical-align: top;
	margin-top: 4px;
	background: #fff;
	color: #333;
	text-decoration: none;
	height: 18px;
	line-height: 18px;
}

--%>
<%--
#product_type,#product_level {
	width: 120px;
}

--%>
<%--
#travelTypeId,#trafficName,#activityLevelId,#activityTypeId,#fromArea,#trafficMode,#payMode
	{
	width: 90px;
	font-size: 12px;
	height: 28px;
}

--%>
#contentTable td {
	padding: 4px 12px;
	text-align: left;
}

#contentTable th {
	padding: 12px;
	text-align: left;
	background: #403738
}

#teamTable {
	margin-bottom: 8px;
}

#travelTypeId,#trafficName,#activityLevelId,#activityTypeId,#fromArea,#trafficMode,#payMode
	{
	width: 90px;
	font-size: 12px;
	height: 28px;
}

#targetAreaName,.hasDatepicker {
	width: 120px;
	height: 18px;
	line-height: 16px;
}

#targetAreaName {
	margin-right: 57px;
}

#opUserName,.hasDatepicker {
	width: 120px;
	height: 18px;
	line-height: 16px;
}

#opUserName {
	margin-right: 57px;
}

#activitySerNum {
	width: 75px;
}

<%--
#activityDuration {
	width: 50px;
	height: 18px;
	line-height: 16px;
}

--%>
#acitivityName {
	height: 26px;
	line-height: 26px;
}

#remainDays {
	width: 80px;
}

#product_type,#product_level {
	width: 120px;
}

#targetAreaButton {
	left: 133px;
	height: 18px;
	position: absolute;
	top: 0;
}

a.a_none {
	display: inline-block;
	border: 1px solid #ccc;
	border-radius: 4px;
	width: 20px;
	text-align: center;
	vertical-align: top;
	margin-top: 4px;
	background: #fff;
	color: #333;
	text-decoration: none;
	height: 18px;
	line-height: 18px;
}

label {
	font-size: 12px;
	font-family: '宋体';
	cursor: text;
}

#targetAreaButton {
	font-size: 12px;
	font-family: '宋体';
}

#btnCancel {
	background-color: #62AFE7;
	width: 72px;
	height: 28px;
}

#btnSubmit {
	width: 72px;
	height: 28px;
}

.team_ins select {
	width: 120px;
	font-family: '宋体';
	font-size: 12px;
	height: 28px;
}

.ui-button {
	width: 90px;
	height: 30px;
}

.ui-button-text-icon-primary .ui-button-icon-primary,.ui-button-text-icons .ui-button-icon-primary,.ui-button-icons-only .ui-button-icon-primary
	{
	left: 1em;
}

.ui-widget {
	font-size: 12px;
	padding: 4px 6px;
}

input,textarea,.uneditable-input {
	width: 78px;
	height: 20px;
}
/* 团号一行显示*/
.ellipsis-number-team {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	max-width: 150px;
}
</style>
</head>
<body>

	<page:applyDecorator name="show_head">
		<page:param name="desc">
			<c:choose>
				<c:when test="${travelActivity.activityKind eq 1 }">单团产品详情</c:when>
				<c:when test="${travelActivity.activityKind eq 2 }">散拼产品详情</c:when>
				<c:when test="${travelActivity.activityKind eq 3 }">游学产品详情</c:when>
				<c:when test="${travelActivity.activityKind eq 4 }">大客户产品详情</c:when>
				<c:when test="${travelActivity.activityKind eq 5 }">自由行产品详情</c:when>
				<c:when test="${travelActivity.activityKind eq 10 }">游轮产品详情</c:when>
			</c:choose>
		</page:param>
	</page:applyDecorator>
	<div class="produceDiv">
		<form:form id="modForm" modelAttribute="travelActivity"
			action="${ctx}/activity/manager/modsave?proId=" method="post"
			class="" enctype="multipart/form-data">
			<tags:message content="${message}" />
			<input type="hidden" id="proId" value="${travelActivity.id }" />
			<input type="hidden" id="activityKind" value="${travelActivity.activityKind }" />
			<div class="mod_nav">产品详情</div>
			<div class="mod_information">
				<div class="mod_information_d">
					<div class="ydbz_tit">产品基本信息</div>
				</div>
			</div>
			<div class="mod_information_dzhan">
				<div class="mod_information_dzhan_d mod_details2_d">
					<span style="color:#009535; font-size:16px; font-weight:bold;">${travelActivity.acitivityName}</span>
					<div class="mod_information_d7"></div>
					<table border="0" width="90%">
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">${travelActivity.fromAreaName}</td>
							<td class="mod_details2_d1">行程天数：</td>
							<td class="mod_details2_d2" id="activityDuration">${travelActivity.activityDuration}</td>
							<td class="mod_details2_d1">领队：</td>
							<td class="mod_details2_d2" id="activityDuration">${travelActivity.groupLead}</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">目的地：</td>
							<!-- <td class="mod_details2_d2" id="targetAreaName" title=""></td> -->
							<td class="mod_details2_d2 hover-container" id="targetAreaName">
								<div class="targetname">
									${travelActivity.targetAreaNames}</div>
								<div  class="hover-title hint_show_sty" style=" overflow:hidden;   word-break: break-all;">
									${travelActivity.targetAreaNames}</div>
							</td>
							<td class="mod_details2_d1">可见用户：</td>
							<td class="mod_details2_d2" id="opUserName" title=""></td>

							<td class="mod_details2_d1">部门：</td>
							<td class="mod_details2_d2">${deptName }</td>
						</tr>
						<c:choose>
							<c:when test="${travelActivity.activityKind eq 10  }">
								<td class="mod_details2_d1">返回城市：</td>
								<td class="mod_details2_d2">${travelActivity.backAreaName}</td>
								<td class="mod_details2_d1">产品系列：</td>
								<td class="mod_details2_d2">${travelActivity.activityLevelName}</td>
							</c:when>
							<c:otherwise>
								<tr>
									<td class="mod_details2_d1">交通方式：</td>
									<td class="mod_details2_d2">${travelActivity.trafficModeName}<c:if
											test="${!empty travelActivity.trafficName and fn:indexOf(relevanceFlagId,travelActivity.trafficMode)>=0 }">&nbsp;&nbsp;|&nbsp;&nbsp;${travelActivity.trafficNameDesc}</c:if>
										<c:if test="${not empty travelActivity.activityAirTicket.id}">
											<span class="linkAir"> <span
												onclick="showAirInfo(this)" class="linkAir-spn">展开关联机票产品信息</span>
												<!-- <div class="airInfo-arrow" style="display: block;">
													<i></i>
												</div> -->
											</span>
											<tr class="airInfoTr">
												<td colspan="8">
													<div class="airInfo" style="display: none;">
														<input type="hidden" id="airTicketId" name="airTicketId"
															value="" />
													</div>
												</td>
											</tr>
										</c:if>
									</td>
									<c:if test="${travelActivity.activityKind eq 2  }">
										<td class="mod_details2_d1">游玩线路：</td>
										<c:choose>
											<c:when test="${empty travelActivity.touristLine}">
												<td class="mod_details2_d2"></td>
											</c:when>
											<c:otherwise>
												<td class="mod_details2_d2">${travelActivity.touristLine.lineName}</td>
											</c:otherwise>
										</c:choose>
									</c:if>
								</tr>
								<tr>
									<td class="mod_details2_d1">创建时间：</td>
									<td class="mod_details2_d2"><fmt:formatDate
											pattern="yyyy-MM-dd HH:mm:ss"
											value="${travelActivity.createDate }" /></td>
									<td class="mod_details2_d1">更新时间：</td>
									<td class="mod_details2_d2"><fmt:formatDate
											pattern="yyyy-MM-dd HH:mm:ss"
											value="${travelActivity.updateDate }" /></td>
								</tr>
								<%--        ======================--%>
								<tr>
									<td class="mod_details2_d1">旅游类型：</td>
									<td class="mod_details2_d2">${travelActivity.travelTypeName}</td>
									<td class="mod_details2_d1">产品系列：</td>
									<td class="mod_details2_d2">${travelActivity.activityLevelName}</td>
									<td class="mod_details2_d1">产品类型：</td>
									<td class="mod_details2_d2">${travelActivity.activityTypeName}</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
							<td class="mod_details2_d1">付款方式：</td>
							<td class="mod_details2_d2"><c:if
									test="${travelActivity.payMode_deposit eq 1}">
						订金占位（保留
						<c:if
										test="${travelActivity.remainDays_deposit == '' || travelActivity.remainDays_deposit == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_deposit != '' && travelActivity.remainDays_deposit != null}">${travelActivity.remainDays_deposit}</c:if>
						天
						<c:if
										test="${travelActivity.remainDays_deposit_hour == '' || travelActivity.remainDays_deposit_hour == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_deposit_hour != '' && travelActivity.remainDays_deposit_hour != null}">${travelActivity.remainDays_deposit_hour}</c:if>
						时
						<c:if
										test="${travelActivity.remainDays_deposit_fen == '' || travelActivity.remainDays_deposit_fen == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_deposit_fen != '' && travelActivity.remainDays_deposit_fen != null}">${travelActivity.remainDays_deposit_fen}</c:if>
						分）<br>
								</c:if> <c:if test="${travelActivity.payMode_advance eq 1}">
						预占位（保留
						<c:if
										test="${travelActivity.remainDays_advance == '' || travelActivity.remainDays_advance == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_advance != '' && travelActivity.remainDays_advance != null}">${travelActivity.remainDays_advance}</c:if>
						天
						<c:if
										test="${travelActivity.remainDays_advance_hour == '' || travelActivity.remainDays_advance_hour == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_advance_hour != '' && travelActivity.remainDays_advance_hour != null}">${travelActivity.remainDays_advance_hour}</c:if>
						时
						<c:if
										test="${travelActivity.remainDays_advance_fen == '' || travelActivity.remainDays_advance_fen == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_advance_fen != '' && travelActivity.remainDays_advance_fen != null}">${travelActivity.remainDays_advance_fen}</c:if>
						分）<br>
								</c:if> <c:if test="${travelActivity.payMode_data eq 1}">
						资料占位（保留
						<c:if
										test="${travelActivity.remainDays_data == '' || travelActivity.remainDays_data == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_data != '' && travelActivity.remainDays_data != null}">${travelActivity.remainDays_data}</c:if>
						天
						<c:if
										test="${travelActivity.remainDays_data_hour == '' || travelActivity.remainDays_data_hour == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_data_hour != '' && travelActivity.remainDays_data_hour != null}">${travelActivity.remainDays_data_hour}</c:if>
						时
						<c:if
										test="${travelActivity.remainDays_data_fen == '' || travelActivity.remainDays_data_fen == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_data_fen != '' && travelActivity.remainDays_data_fen != null}">${travelActivity.remainDays_data_fen}</c:if>
						分）<br>
								</c:if> <c:if test="${travelActivity.payMode_guarantee eq 1}">
						担保占位（保留
						<c:if
										test="${travelActivity.remainDays_guarantee == '' || travelActivity.remainDays_guarantee == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_guarantee != '' && travelActivity.remainDays_guarantee != null}">${travelActivity.remainDays_guarantee}</c:if>
						天
						<c:if
										test="${travelActivity.remainDays_guarantee_hour == '' || travelActivity.remainDays_guarantee_hour == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_guarantee_hour != '' && travelActivity.remainDays_guarantee_hour != null}">${travelActivity.remainDays_guarantee_hour}</c:if>
						时
						<c:if
										test="${travelActivity.remainDays_guarantee_fen == '' || travelActivity.remainDays_guarantee_fen == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_guarantee_fen != '' && travelActivity.remainDays_guarantee_fen != null}">${travelActivity.remainDays_guarantee_fen}</c:if>
						分）<br>
								</c:if> <c:if test="${travelActivity.payMode_express eq 1}">
						确认单占位（保留
						<c:if
										test="${travelActivity.remainDays_express == '' || travelActivity.remainDays_express == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_express != '' && travelActivity.remainDays_express != null}">${travelActivity.remainDays_express}</c:if>
						天
						<c:if
										test="${travelActivity.remainDays_express_hour == '' || travelActivity.remainDays_express_hour == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_express_hour != '' && travelActivity.remainDays_express_hour != null}">${travelActivity.remainDays_express_hour}</c:if>
						时
						<c:if
										test="${travelActivity.remainDays_express_fen == '' || travelActivity.remainDays_express_fen == null}">0</c:if>
									<c:if
										test="${travelActivity.remainDays_express_fen != '' && travelActivity.remainDays_express_fen != null}">${travelActivity.remainDays_express_fen}</c:if>
						分）<br>
								</c:if> <c:if test="${travelActivity.payMode_full eq 1}">
						全款支付<br>
								</c:if> <c:if test="${travelActivity.payMode_op eq 1}">
						计调确认占位<br>
								</c:if> <!-- wangxinwei added 20151111,添加，财务确认占位，对应需求号 C362 --> <c:if
									test="${travelActivity.payMode_cw eq 1}">
						财务确认占位
					</c:if></td>

							<td class="mod_details2_d1">特殊人群备注：</td>
							<td class="mod_details2_d2">
								<div style="width:200px; white-space:nowrap; text-overflow:ellipsis; overflow:hidden;">
									<span title="${travelActivity.specialRemark}">${travelActivity.specialRemark}</span>
								</div>
							</td>
							</tr>
							</tbody>
					</table>
					<div class="kong"></div>
				</div>
			</div>
			</td>
							</tr>
							</tbody>
					</table>
			<div class="mod_information">
				<div class="mod_information_d">
					<div class="ydbz_tit">产品团期</div>
				</div>
			</div>
			<c:set var="groupsize"
				value="${fn:length(travelActivity.activityGroups)}"></c:set>
			<c:if test="${groupsize ne 0}">
				<div class="mod_details2_tabletype">
					<table class="table" style="width:100%;">
						<thead>
							<tr>
								<c:if test="${travelActivity.activityKind eq 2 }">
									<shiro:hasPermission name="calendarLoose:book:order">
										<th width="3%">推荐状态</th>
									</shiro:hasPermission>
									</c:if>
									<th width="5%">团号</th>
									<th width="5%">出/截团日期</th>
									<th width="5%">应付账期</th>
									<th width="4%">签证国家</th>
									<th width="5%">资料截止日期</th>
									<c:if test="${travelActivity.activityKind eq 10 }">
										<th width="5%">舱型</th>
									</c:if>
									<shiro:hasPermission name="price:project">
										<c:if test="${travelActivity.activityKind ne 6 and travelActivity.activityKind ne 7 and travelActivity.activityKind ne 10 }">
											<th width="6%">酒店房型</th>
										</c:if>
									</shiro:hasPermission>
									<c:choose>
										<c:when test="${travelActivity.activityKind eq '10' }">
											<th width="5%">1/2同行价</th>
											<th width="5%">3/4同行价</th>
											<th width="5%">1/2直客价</th>
											<th width="5%">3/4直客价</th>
										</c:when>
										<c:otherwise>
											<th width="5%">成人同行价</th>
											<th width="5%">儿童同行价</th>
											<th width="5%">特殊人群同行价</th>
											<th width="3%">儿童最高人数</th>
											<th width="3%">特殊人群最高人数</th>
												<!-- T1新增quauq价-s -->
												<c:if test="${travelActivity.activityKind eq '2' }">
													<th width="5%">成人直客价</th>
													<th width="5%">儿童直客价</th>
													<th width="5%">特殊人群直客价</th>
												<c:if test="${isT1 eq '1'  or isOp eq 1}">
												<c:if test="${empty isOp}">
												    <th width="5%">成人QUAUQ策略</th>
													<th width="5%">儿童QUAUQ策略</th>
													<th width="5%">特殊人群QUAUQ策略</th>
												</c:if>
												<!-- T1新增quauq价-e --> 
												<th width="5%">成人供应价</th>
												<th width="5%">儿童供应价</th>
												<th width="5%">特殊人群供应价</th>
												</c:if>
											</c:if>
										</c:otherwise>
									</c:choose>
									<th width="5%">订金</th>
									<th width="5%">单房差</th>
									<th width="5%">预收</th>
									<th width="5%">余位</th>
									<!-- 0258 -->
									<c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
									<th width="3%">发票税</th>
								    </c:if>
									
									<c:if test="${travelActivity.activityKind eq '2' &&( fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586'|| fns:getUser().company.uuid == '75895555346a4db9a96ba9237eae96a5')}">
									<th width="7%">优惠额度</th>
									</c:if>
								</tr>
							</thead>
							<tbody>
							
							<!-- c463 tgy -start -->
							<c:forEach items="${travelActivity.activityGroupList}" var="group" varStatus="s2">
								<tr>
									<c:if test="${travelActivity.activityKind eq 2 }">
									<shiro:hasPermission name="calendarLoose:book:order">
									<td rowspan="3">
										<c:if test="${empty group.recommend or group.recommend==0 }">无推荐</c:if>
										<c:if test="${not empty group.recommend and group.recommend==1 }">有推荐</c:if>
									</td>
									</shiro:hasPermission>
									</c:if>

									<%-- <td class="ellipsis-number-team" title="${group.groupCode}"> --%>

									 <td rowspan="3">
										<%--<c:choose> 
										     <c:when test="${fn:length(group.groupCode) > 10}"> 
										     	<a style="text-decoration: none; color:inherit; cursor:default;" title="${group.groupCode}"><c:out value="${fn:substring(group.groupCode, 0, 10)}..." /></a> 
										     </c:when> 
										     <c:otherwise> 
										      	<c:out value="${group.groupCode}" /> 
										     </c:otherwise>
										</c:choose>
									--%> <span
										title="${group.groupCode}">${group.groupCode}</span>
									</td>
									<td rowspan="2" name="groupOpenDate" id="groupOpenDateId" class="tc" >
										<div class="out-date">
											<fmt:formatDate pattern="yyyy-MM-dd"
												value="${group.groupOpenDate}" />
										</div>
										<div class="close-date">
											<fmt:formatDate pattern="yyyy-MM-dd"
												value="${group.groupCloseDate}" />
										</div>
									</td>
									<td rowspan="2" id="payableDate" class="tc" ><fmt:formatDate pattern="yyyy-MM-dd" value="${group.payableDate }"/></td>
									<td rowspan="2">${group.visaCountry}</td>
									<td rowspan="2" id="visaDateId" class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></td>
									<c:if test="${travelActivity.activityKind eq 10 }">
										<td rowspan="2">
											${fns:getDictLabel(group.spaceType, "cruise_type", "-") }
										</td>
									</c:if>
									<!-- 团期酒店房型 -->
									<shiro:hasPermission name="price:project">
										<c:if test="${travelActivity.activityKind ne 6 and travelActivity.activityKind ne 7 and travelActivity.activityKind ne 10 }">
										<td name="hotelhouse" rowspan="2">
											<c:forEach var="hotelhouse" items="${group.hotelHouseList }" varStatus="status">
												<div>
													<div>
													<label>酒店：</label>
													<input width="3%" type="text" name="groupHotel" value="${hotelhouse.hotel }" class="valid disabledClass" style="display:none; width:38px; padding-left:2px; padding-right:2px; margin-left:1px;">
													<span class="disabledshowspan">${hotelhouse.hotel }</span>&nbsp;
													</div>
													<div>
													<label>房型：</label>
													<input width="3%" type="text" name="groupHouseType" value="${hotelhouse.houseType }" class="valid disabledClass" style="display:none; width:38px; padding-left:2px; padding-right:2px; margin-left:1px;">
													<span class="disabledshowspan">${hotelhouse.houseType }</span>
													</div>
												</div>
											</c:forEach>
										</td>
										</c:if>
									</shiro:hasPermission>
									<td class="tr" rowspan="2">
										<c:if test="${not empty group.settlementAdultPrice}">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</c:if>
										<span class="tdred" <c:if test="${not empty travelActivity.activityAirTicket }">title="包含${travelActivity.activityAirTicket.settlementAdultPrice }机票价"</c:if> ><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice}" /></span>
									</td>
									<td class="tr" rowspan="2">
										<c:if test="${not empty group.settlementcChildPrice}">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</c:if>
										<span class="tdred" <c:if test="${not empty travelActivity.activityAirTicket }">title="包含${travelActivity.activityAirTicket.settlementcChildPrice }机票价"</c:if> ><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice}" /></span>
									</td>
									<c:if test="${travelActivity.activityKind ne '10' }" >
										<td class="tr" rowspan="2">
											<c:if test="${not empty group.settlementSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</c:if>
<!-- 											<span class="tdred" title="${travelActivity.specialRemark }<c:if test="${not empty travelActivity.activityAirTicket }">包含${travelActivity.activityAirTicket.settlementcChildPrice }机票价</c:if> "> -->
												<span class="tdred" <c:if test="${not empty travelActivity.activityAirTicket }">title="${travelActivity.specialRemark }包含${travelActivity.activityAirTicket.settlementcChildPrice }机票价"</c:if>>
												<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice}" />
											</span>
										</td>
									</c:if>
									<c:if test="${travelActivity.activityKind ne '10' }">
									<td class="tr" rowspan="2">
											<c:if test="${not empty group.maxChildrenCount}">${group.maxChildrenCount}</c:if>
										</td>
										<td class="tr" rowspan="2">
											<c:if test="${not empty group.maxPeopleCount}">${group.maxPeopleCount}</c:if>
										</td>
									</c:if>
									<c:if test="${travelActivity.activityKind eq '2' or travelActivity.activityKind eq '10'}">
									
									    <!-- wxw  added 20160107  处理 币种符号串位问题   -->
									    <c:choose>
									        <c:when test="${travelActivity.activityKind eq '10'}">
												<td class="tr" rowspan="2">
												   <c:if test="${not empty group.suggestAdultPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</c:if>
												   <span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice}" /></span>
												</td>
												<td class="tr" rowspan="2">
												   <c:if test="${not empty group.suggestChildPrice}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
												   <span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice}" /></span>
												</td>
									        </c:when>
									        <c:when test="${travelActivity.activityKind eq '2'}">
												<td class="tr" rowspan="2">
												   <c:if test="${not empty group.suggestAdultPrice}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
												   <span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice}" /></span>
												</td>
												<td class="tr" rowspan="2">
												   <c:if test="${not empty group.suggestChildPrice}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if>
												   <span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice}" /></span>
												</td>
												
												<td class="tr" rowspan="2">
													<c:if test="${not empty group.suggestSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</c:if>
													<span class="tdblue" title="${travelActivity.specialRemark }">												
														<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice}" />
													</span>
												</td>
									        </c:when>
									    
									    </c:choose>
									    
									
										
											<c:if test="${travelActivity.activityKind eq '2' }">
											<c:if test="${isT1 eq '1'  or isOp eq 1}">
											<!-- 增加quauq策略 -->
											<c:if test="${empty isOp}">
												<td class="tr" rowspan="2">
													<c:choose>
														<c:when test="${group.isT1 !=1 || empty group.pricingStrategy.adultPricingStrategy }">-</c:when>
														<c:otherwise>${group.pricingStrategy.adultPricingStrategy }</c:otherwise>
													</c:choose>
												</td> 
												<td class="tr" rowspan="2">
													<c:choose>
														<c:when test="${group.isT1 !=1 || empty group.pricingStrategy.childrenPricingStrategy}">-</c:when>
														<c:otherwise>${group.pricingStrategy.childrenPricingStrategy }</c:otherwise>
													</c:choose>
												</td> 
												<td class="tr" rowspan="2">
													<c:choose>
														<c:when test="${group.isT1 !=1 || empty group.pricingStrategy.specialPricingStrategy }">-</c:when>
														<c:otherwise>${group.pricingStrategy.specialPricingStrategy }</c:otherwise>
													</c:choose>
												</td>
											</c:if>
											<!-- t1新增供应价-s -->
											<td class="tr" rowspan="2">
												<c:choose>
													<c:when test="${group.isT1 ==1 and not empty group.quauqAdultPrice}">${fns:getCurrencyInfo(group.currencyType,0,'mark')}
													<span class="tdblue">
														<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.adultRetailPrice }" />
													</span></c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td> 
											<td class="tr" rowspan="2">
												<c:choose>
													<c:when test="${group.isT1 ==1 and not empty group.quauqChildPrice}">${fns:getCurrencyInfo(group.currencyType,1,'mark')}
													<span class="tdblue">
														<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.childRetailPrice }" />
													</span></c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td> 
											<td class="tr" rowspan="2">
												<c:choose>
													<c:when test="${group.isT1 ==1 and not empty group.quauqSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}
													<span class="tdblue">
														<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.specialRetailPrice }" />
													</span></c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>  
											</c:if>
											<!-- t1新增供应价-e -->
											<c:if test="${fn:length(fn:split(group.currencyType,',')) gt 8 }">
											<td class="tr" rowspan="2">
											<c:if   test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,12,'mark')}</c:if><span
												class="tdorange"><fmt:formatNumber type="currency"
														pattern="#,##0.00" value="${group.payDeposit}" /></span></td>
											<td class="tr" rowspan="2"><c:if
													test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,13,'mark')}</c:if>
												<fmt:formatNumber type="currency" pattern="#,##0.00"
													value="${group.singleDiff}" /></td>
											</c:if>
											<c:if test="${fn:length(fn:split(group.currencyType,',')) le 8 }">
											<td class="tr" rowspan="2">
											<c:if   test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</c:if><span
												class="tdorange"><fmt:formatNumber type="currency"
														pattern="#,##0.00" value="${group.payDeposit}" /></span></td>
											<td class="tr" rowspan="2"><c:if
													test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</c:if>
												<fmt:formatNumber type="currency" pattern="#,##0.00"
													value="${group.singleDiff}" /></td>
											</c:if> 
										</c:if>
										<c:if test="${travelActivity.activityKind eq '10' }" >
											<td class="tr" rowspan="2"><c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit}" /></span></td>
											<td class="tr" rowspan="2"><c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff}" /></td>
										</c:if>
									</c:if>
									<c:if test="${travelActivity.activityKind ne '2' and travelActivity.activityKind ne '10'}">
										<td class="tr" rowspan="2"><c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit}" /></span></td>
										<td class="tr" rowspan="2"><c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff}" /></td>
									</c:if>

									<td class="tr" rowspan="2"> ${group.planPosition} </td>
									<td class="tr" rowspan="2"> ${group.freePosition} </td>
									<!--0258-qyl-begin  -->
									<c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
									<td class="tr" rowspan="2"><c:if test="${group.invoiceTax ==''||group.invoiceTax==null}">0%</c:if>
									<c:if test="${group.invoiceTax !=''&&group.invoiceTax!=null}">${group.invoiceTax}&nbsp;%</c:if></td>
									</c:if>	
 									<c:if test="${travelActivity.activityKind eq '2'}">
									
									<c:if test="${fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '75895555346a4db9a96ba9237eae96a5'  }">
										<td class="tr" rowspan="2">
										<a class="a1" href="javascript:void(0)" onclick="jbox__view_discount_setting_pop_fab_view(this);">查看优惠额度</a>
										 </td>
								   	</c:if>	 
								   	
								   	<%--161009 bug16281 将hidden换成display:none 兼容ie--%>
										 <td style="display:none" class="tr" rowspan="2" id="adultDiscountPrice">
										 <c:if test="${not empty group.adultDiscountPrice}">${fns:getCurrencyInfo(group.currencyType,8,'mark')}</c:if>
												<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.adultDiscountPrice}" />
										</td>
										<td style="display:none" class="tr" rowspan="2" id="childDiscountPrice">
										<c:if test="${not empty group.childDiscountPrice}">${fns:getCurrencyInfo(group.currencyType,9,'mark')}</c:if>
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.childDiscountPrice}" />
										</td>
										<td style="display:none" class="tr" rowspan="2" id="specialDiscountPrice" >
										<c:if test="${not empty group.specialDiscountPrice}">${fns:getCurrencyInfo(group.currencyType,10,'mark')}</c:if>
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.specialDiscountPrice}" />
										</td>
									</c:if>
								</tr>
								<tr></tr><!-- 该标签仅仅是起换行的作用 -->
								<tr>
                                <td colspan="23" class="groupdetailNote">备注：${group.groupRemark}</td><!-- 由于t1新增了quauq价,所以colspan由16变成了19 -->
									<td style="display:none"><input type='hidden' name='priceJson' value='${group.priceJson}' /></td><%--bug16435--%>
                                </tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
			<!-- 上传文件 -->
			<div class="team_ins" style="margin-top:32px;">
				<div class="mod_information">
					<div class="mod_information_d">
						<div class="ydbz_tit">产品相关资料</div>
					</div>
				</div>
				<div class="mod_information_dzhan">
					<c:set var="flag" value="0"></c:set>
					<div class="batch">
						<label class="batch-label">产品行程介绍：</label>
						<ol class="batch-ol">
							<c:forEach items="${travelActivity.activityFiles}" var="file"
								varStatus="s1">
								<c:if test="${file.fileType==1}">
									<li><span>${file.docInfo.docName}</span> <a  class="batchDl"
										style="margin-left:10px;" href="javascript:void(0)"
										onClick="downloads(${file.docInfo.id})">下载</a></li>
								</c:if>
							</c:forEach>
						</ol>
					</div>
					<div class="batch">
						<label class="batch-label">自费补充协议：</label>
						<ol class="batch-ol">
							<c:forEach items="${travelActivity.activityFiles}" var="file"
								varStatus="s1">
								<c:if test="${file.fileType==2}">
									<li><span>${file.docInfo.docName}</span> <a class="batchDl"
										style="margin-left:10px;" href="javascript:void(0)"
										onClick="downloads(${file.docInfo.id})">下载</a></li>
								</c:if>
							</c:forEach>
						</ol>
					</div>
					<div class="batch">
						<label class="batch-label">其他补充协议：</label>
						<ol class="batch-ol">
							<c:forEach items="${travelActivity.activityFiles}" var="file"
								varStatus="s1">
								<c:if test="${file.fileType==3}">
									<li><span>${file.docInfo.docName}</span> <a class="batchDl"
										style="margin-left:10px;" href="javascript:void(0)"
										onClick="downloads(${file.docInfo.id})">下载</a></li>
								</c:if>
							</c:forEach>
						</ol>
					</div>
					<div class="batch">
						<label class="batch-label">其他文件：</label>
						<ol class="batch-ol">
							<c:forEach items="${travelActivity.activityFiles}" var="file"
								varStatus="s1">
								<c:if test="${file.fileType==5}">
									<li><span>${file.docInfo.docName}</span> <a class="batchDl"
										style="margin-left:10px;" href="javascript:void(0)"
										onClick="downloads(${file.docInfo.id})">下载</a></li>
								</c:if>
							</c:forEach>
						</ol>
					</div>

					<div class="batch">
						<label class="batch-label">签证资料：</label> <br>
						<c:forEach items="${visaMap[travelActivity.id] }" var="visaList"
							varStatus="ms">
							<c:forEach items="${visaList }" var="visaMaps">
								<c:set var="countryId" value="${fn:split(visaMaps.key,'/')[0]}"></c:set>
								<div class="mod_information_d8_2" style="margin-top:5px;">
									<table border="0" style="vertical-align:middle;"
										name="company_logo">
										<tbody>
											<tr>
												<td>${fns:getCountryName(countryId) }
													<${fn:split(visaMaps.key,'/')[1] }>:</td>
											</tr>
										</tbody>
									</table>
									<ol class="batch-ol" style="margin-left:38px;">
										<c:forEach items="${visaMaps.value }" var="docInfo">
											<li><span>${docInfo.docName }</span><a class="batchDl"
												href="javascript:void(0)" onClick="downloads(${docInfo.id})">下载</a>
											</li>
										</c:forEach>
									</ol>
								</div>
							</c:forEach>
						</c:forEach>
					</div>
					
					<div class="batch">
						<label class="batch-label">微信分销广告图片：</label>
						<ol class="batch-ol">
							<c:forEach items="${travelActivity.activityFiles}" var="file"
								varStatus="s1">
								<c:if test="${file.fileType==6}">
									<li><span>${file.docInfo.docName}</span> <a class="batchDl"
										style="margin-left:10px;" href="javascript:void(0)"
										onClick="downloads(${file.docInfo.id})">下载</a></li>
								</c:if>
							</c:forEach>
						</ol>
					</div>

				</div>


			</div>
      
            <c:if test="${is_need_groupCode eq '1' }"><!-- 当批发商具有团号库权限时,才进行修改记录的展示  -->
			<!-- 大洋87、非常国际、优加、起航假期  机票产品团号修改记录，对应需求号 c451,c453 -->
			<!-- 对应需求号 c460   ||fns:getUser().company.groupCodeRuleDT eq 0  -->
			<div class="team_ins" style="margin-top:32px;">
				<c:if
					test="${fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' ||fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021'||fns:getUser().company.groupCodeRuleDT == 0}">
					<div class="mod_information">
						<div class="mod_information_d">
							<div class="ydbz_tit">修改记录</div>
						</div>
					</div>
					<c:forEach items="${groupcodeModifiedRecordmap}"
						var="groupcodeModified" varStatus="listIndex">
						<div class="mod_information_dzhan">
							<span class="modifyTime"
								style="margin-left: 30px;font-weight:bold;">当前团号【<font
								color="red">${groupcodeModified.key}</font>】修改过程记录如下:
							</span>
						</div>

						<c:forEach items="${groupcodeModified.value}" var="modifiedRecord"
							varStatus="listIndex">
							<div class="mod_information_dzhan">
								<span class="modifyTime" style="margin-left: 30px"><fmt:formatDate
										value="${modifiedRecord.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></span> 【<span class="modifyType">团号</span>
								】 由【<span class="exGroupNo">${modifiedRecord.groupcodeOld}</span>
								】修改成【<span class="groupNo">${modifiedRecord.groupcodeNew}</span>】
								by【<span class="modifyUser">${modifiedRecord.updateByName}</span>】
							</div>
						</c:forEach>
					</c:forEach>
				</c:if>
			</div>
            </c:if>


		</form:form>


		<div class="ydbz_sxb ydbz_button">
			<a class="ydbz_x" href="javascript:void(0)"
				onClick="javascript:window.close();">关闭</a>

			<c:if
				test="${travelActivity.activityStatus==3||travelActivity.activityStatus==1}">
				<a class="ydbz_x" href="javascript:void(0)" onclick="lineOne('on')">上架</a>
			</c:if>

			<c:if test="${travelActivity.activityStatus==2}">
				<shiro:hasPermission name="product:manager:tooffline">
					<a class="ydbz_x" href="javascript:void(0)" onclick="lineOne()">下架</a>
				</shiro:hasPermission>
			</c:if>
		</div>
	</div>

	<style type="text/css">
#product_level {
	top: 137px !important;
	left: 875px !important;
	width: 78px;
}

#product_type {
	top: 175px !important;
	left: 235px !important;
	width: 88px;
}
</style>

<!--S--C109--设置优惠额度弹窗-->
<!--S--C109--查看优惠额度弹窗-->

<div id="view-discount-setting-pop"  class="display-none">
    <div class="discount-setting-pop">
        <div class="mod_details2_tabletype">
            <table class="table" style="width:100%;">
                <thead>
                <tr>
                    <th colspan="3">同行价优惠额度</th>
                </tr>
                <tr>
                    <th class="tc" width="12%">成人</th>
                    <th class="tc" width="9%">儿童</th>
                    <th class="tc" width="9%">特殊人群</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<!--S--C109--查看优惠额度弹窗-->
<!--S--C109--修改优惠额度弹窗-->

<div id="modify-discount-setting-pop"  class="display-none">
    <div class="discount-setting-pop">
        <div class="mod_details2_tabletype">
            <table class="table" style="width:100%;">
                <thead>
                <tr>
                    <th colspan="3">同行价优惠额度</th>
                </tr>
                <tr>
                    <th class="tc" width="12%">成人</th>
                    <th class="tc" width="9%">儿童</th>
                    <th class="tc" width="9%">特殊人群</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                </tr>
                <tr>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

<!--S--C109--修改优惠额度弹窗-->
<!--S--C109--未选择团期设置优惠提示-->
<div id="nosel-group-discount-setting-pop" class="display-none">
    <div class="nosel-group-discount-setting-pop">
        请选择团！
    </div>
</div>
<
<!--E--C109--未选择团期设置优惠提示-->
</div>

<div id="pricingTableTem" class="display-none">
		<table id="pricePlanTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son">
			<thead>
				<tr>
					<th width="5%" rowspan="2">序号</th>
					<th width="30%" class="tc" rowspan="2">价格方案</th>
					<th width="15%" class="tc" colspan="3">同行价</th>
					<c:if test="${travelActivity.activityKind eq '2'}">
						<th width="15%" class="tc" colspan="3">直客价</th>
					</c:if>
					<th width="25%" rowspan="2">备注</th>
				</tr>
				<tr>
					<th>成人</th>
					<th>儿童</th>
					<th>特殊人群</th>
					<c:if test="${travelActivity.activityKind eq '2'}">
						<th>成人</th>
						<th>儿童</th>
						<th>特殊人群</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td name="index">1</td>
					<td>
						<p>
							<span>
								<label>酒店：</label>
							</span>
							<input width="4%" type="text" name="hotel" class="pricing-scheme" data-type="amount" maxlength="50">
							<span>
								<label>房型：</label>
							</span>
							<input width="4%" type="text" name="houseType" class="pricing-scheme" data-type="amount" maxlength="50">
							
							<span class="addAndRemove"><em class="add-select" name="addPricing"></em>
							<em class="remove-selected" name="deletePricing"></em></span>
						</p>
					</td>
					<td class="tr tdCurrency">
						<span>$</span>
						<input width="4%" type="text" name="thcr" class="ipt-currency" data-type="amount" maxlength="8">
					</td>
					<td class="tr tdCurrency">
						<span>$</span>
						<input width="4%" type="text" name="thet" class="ipt-currency" data-type="amount" maxlength="8">
					</td>
					<td class="tr tdCurrency">
						<span>$</span>
						<input width="4%" type="text" name="thts" class="ipt-currency" data-type="amount" maxlength="8">
					</td>
					<c:if test="${travelActivity.activityKind eq '2'}">
						<td class="tr tdCurrency">
							<span>$</span>
							<input width="4%" type="text" name="zkcr" class="ipt-currency" data-type="amount" maxlength="8">
						</td>
						<td class="tr tdCurrency">
							<span>$</span>
							<input width="4%" type="text" name="zket" class="ipt-currency" data-type="amount" maxlength="8">
						</td>
						<td class="tr tdCurrency">
							<span>$</span>
							<input width="4%" type="text" name="zkts" class="ipt-currency" data-type="amount" maxlength="8">
						</td>
					</c:if>
					<td class="tc">
						<input type="text" name="remark" class="nopadding" maxlength="50"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

</body>

</html>
