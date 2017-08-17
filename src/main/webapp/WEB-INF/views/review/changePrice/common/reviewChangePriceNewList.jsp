<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>改价-列表</title>
<!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" /> -->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();
		showSearchPanel();
		inputTips();
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function() {
			if ($(this).attr("checked")) {

			} else {
				$("input[name='allChk']").removeAttr("checked");
			}
		});
// 		$("#prdtypeidtag" ).comboboxInquiry();
//		renderSelects(selectQuery());
		selectQuery();
		
		$("#meterrefund" ).comboboxInquiry();
		var resetSearchParams = function() {
			$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
					.val('').removeAttr('checked').removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable')
				.on(
						'change',
						'input[type="checkbox"]',
						function() {
							if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
								$('[name="allChk" ]').attr('checked', true);
							} else {
								$('[name="allChk" ]').removeAttr('checked');
							}
						});
	});
	
	function selectQuery() {
		$("#channelrefund" ).comboboxInquiry();
		$("#salerrefund" ).comboboxInquiry();
	}
	
		// 	有查询条件的时候，DIV不隐藏
	function showSearchPanel(){
		//产品类型
		var productType = $("select[name='productType'] option:selected").val();
		//渠道商
		var agentId = $("select[name='agentId'] option:selected").val();
		//申请日期
		var applyDateFrom = $("input[name='applyDateFrom']").val();
		var applyDateTo = $("input[name='applyDateTo']").val();
		//审批发起人
		var applyPerson = $("select[name='applyPerson'] option:selected").val();
		//审批状态
		var reviewStatus = $("select[name='reviewStatus'] option:selected").val();
		
		if( isNotEmpty(productType) 
		 || isNotEmpty(agentId) 
		 || isNotEmpty(applyDateFrom)
		 || isNotEmpty(applyDateTo)
		 || isNotEmpty(applyPerson)
		 || isNotEmpty(reviewStatus) 
		 ){
			$('.zksx').click();
		}
	}
	
		// 判定不为空值
	function isNotEmpty(str){
		if(str != "" && str != "0" && str != "null"){
			return true;
		}
		return false;
	}

	//展开收起
	function expand(child, obj) {
		if ($(child).is(":hidden")) {
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");

		} else {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");

		}
	}
	function checkall(obj) {
		if ($(obj).attr("checked")) {
			$('#contentTable input[type="checkbox"]').attr("checked", 'true');
			$("input[name='allChk']").attr("checked", 'true');
			$("input[name='reverseChk']").removeAttr("checked");
		} else {
			$('#contentTable input[type="checkbox"]').removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
		}
	}

	function checkreverse(obj) {
		var $contentTable = $('#contentTable');
		$contentTable.find('input[type="checkbox"]').each(function() {
			var $checkbox = $(this);
			if ($checkbox.is(':checked')) {
				$checkbox.removeAttr('checked');
				$("input[name='allChk']").removeAttr("checked");
			} else {
				$checkbox.attr('checked', true);
			}
		});
	}
	function jbox__shoukuanqueren_chexiao_fab() {
		var num = 0;
		 var str="";
		var currencyIdString = "";
		var amountString = "";
		$('[name=checkboxrevid]:checkbox:checked').each(function(){
			if(num == 0){
				str+=$(this).val();
				currencyIdString+=$(this).parent().prev().prev().html();
				amountString+=$(this).parent().prev().html();
			} else {
				str+=","+$(this).val();
				currencyIdString+=","+$(this).parent().prev().prev().html();
				amountString+=","+$(this).parent().prev().html();
			}
			num++;
		});
		if("" == str)
		{
			$.jBox.tip("请选择需要审批的记录！"); 
			return false;
		}
		var html = "<table width='100%' style='padding:10px !important; border-collapse: separate;'>"
				  +"   <tr>"
				  +"     <td> </td>"
				  +"  </tr>"
				  +"   <tr>"
				  +"     <td><p>您好，当前您提交了"+num+"个审核项目，是否执行批量操作？</p></td>"
				  +"  </tr>"
				  +"   <tr>"
				  +"     <td><p>备注：</p></td>"
				  +"  </tr>"
				  +"   <tr>"
			      +" <td><label>"
			      +"     <textarea name='textfield' id='textfield' style='width: 290px;'></textarea>"
			      +"   </label></td>"
			      +" </tr>"
				  +"</table>";
	    $.jBox(html, {
	        title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
	            if (v == "1" || v == "2") {
		            var remark = f.textfield;
		            if(remark.length>200){
		            	$.jBox.tip("字符长度过长，请输入少于200个字符", 'error');
		            	return;
		            }
		            $.ajax({
		            	type : "post",
						url : "${ctx}/changepricenew/batchchangepricenewreview",
						data:{ 
							"revIds":str,
							"remark" : remark,
							"result" : v,
							"currencyIdString":currencyIdString,
							"amountString":amountString
						},
						success : function(msg) {
							$("#searchForm").submit();
						}
		            })
	            }
	        }, height: 250, width: 350
	    });
	    inquiryCheckBOX();
	}
		function backReview(reviewId){
		top.$.jBox.confirm('确定要撤销审批吗','系统提示',function(v){
		if(v=='ok'){
			$.ajax({
			type: "post",
			url : "${ctx}/changepricenew/backchangepricenewreview/" + reviewId,
			success : function(data){
				if(data.flag == "error"){
					$.jBox.tip(data.msg); 
				} else {
// 					$.jBox.tip("撤销成功", 'info'); 
					top.$.jBox.tip("撤销成功！");
					$("#searchForm").submit();
				}
			}
		})
		}
		},{buttonsFocus:1});
	}
	function statusChooses(status){
		$("#tabStatus").val(status);
		$("#searchForm").submit();
	}
		//排序
	function sort(element){

		var _this = $(element);

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
		if(sortFlag == "icon icon-arrow-up"){

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
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};
//	$(function() {
//		if (!Object.minusObj) {
//			Object.defineProperty(Object, "minusObj", {
//				enumerable: false,
//				configurable: true,
//				writable: true,
//				value: function(target, firstSource) {
//					"use strict";
//					if (target === undefined || target === null)
//						throw new TypeError("Cannot convert first argument to object");
//					var to = Object(target);
//					for (var i = 1; i < arguments.length; i++) {
//						var nextSource = arguments[i];
//						if (nextSource === undefined || nextSource === null) continue;
//						var keysArray = Object.keys(Object(nextSource));
//						for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
//							var nextKey = keysArray[nextIndex];
//							var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
//							var flag=to.hasOwnProperty(nextKey);
//							if (desc !== undefined && desc.enumerable)
//								if(flag){
//									to[nextKey] -= nextSource[nextKey];
//									if(!to[nextKey]){
//										var x=nextKey;
//										var x= delete to[nextKey.toString()];
//									}
//								}else{
//									to[nextKey] = -nextSource[nextKey];
//								}
//						}
//					}
//					return to;
//				}
//			});
//		}
//
//		function getObj($this){
//			var tempObj={};
//			if($this.find("span").length>0){
//				var tempAttr=[];
//				var tempValue=[];
//				$this.find("span:even").each(function () {
//					tempAttr.push($(this).text());
//				});
//				$this.find("span:odd").each(function () {
//					tempValue.push($(this).text().trim())
//				});
//				$.each(tempAttr,function(i,v){
//					tempObj[tempAttr[i]]=Number(tempValue[i].replace(",", ""));
//				})
//			}
//			return tempObj;
//		}
//		(function countToatl(){
//			var returnObj={};
//			$("tbody").find("tr").each(function(){
//				var innerHtml1='';
//				var oldPrice=$(this).find("td").eq("8");
//				var nowPrice=$(this).find("td").eq("9");
//				var tempObj={};
//				var oldPriceObj=getObj(oldPrice);
//				var nowPriceObj=getObj(nowPrice);
//				returnObj=Object.minusObj(nowPriceObj,oldPriceObj);
//				for(var _a in returnObj){
//					innerHtml1+='<span>'+_a+'</span> <span class="tdorange fbold">'+Number(returnObj[_a]).toFixed(2)+'</span>+';
//				}
//				innerHtml1=innerHtml1.slice(0,-1);
//				$(this).find("td").eq("10").empty().html(innerHtml1);
//			})
//		})();
//	});
</script>
</head>
<body>
	<%--<div id="sea">--%>
		<!--右侧内容部分开始-->
		<form id="searchForm" action="${ctx}/changepricenew/changpricereviewnewlist" method="post">
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input id="wholeSalerKey" name="groupCode"
						   class="inputTxt inputTxtlong"
						   value="${conditionsMap.groupCode}"
						   onKeyUp="this.value=this.value.replaceColonChars()"
						   onafterpaste="this.value=this.value.replaceColonChars()"
						   placeholder="请输入团号/产品名称/订单号"/>
				</div>
				<a class="zksx">筛选</a>
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" /> 
				<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
				<input type="hidden" id="tabStatus" name = "tabStatus" value = "${conditionsMap.tabStatus}"/><!--bug17500 将#tabStatus移入form内 by tlw at20170309-->
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" value="搜索" type="submit">
				</div>

				<div class="ydxbd min-screen">
					<span></span>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">产品类型：</label>
						<div class="selectStyle">
							<select name="productType" id="prdtypeidtag">
							<option value="" selected="selected">全部</option>
								<c:forEach var="order" items="${processTypes}">
									<option value="${order.productType }"
										<c:if test="${conditionsMap.productType==order.productType}">selected="selected"</c:if>>${order.getProductName()}
									</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">渠道选择：</label>
						<select name="agentId" id="channelrefund" >
						<option value="">全部</option>
						<c:if test="${not empty fns:getAgentListAddSort()}">
							<c:forEach items="${fns:getAgentListAddSort()}" var="agentinfo">
								<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
									<c:choose>
									   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}"> 
									       <option value="${agentinfo.id }"<c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>直客</option>
									   </c:when>
									   <c:otherwise>
									       <option value="${agentinfo.id }"<c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option>
									   </c:otherwise>
									</c:choose>
							</c:forEach>
						</c:if>
					</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">申请日期：</label> <input id="" class="inputTxt dateinput"
						name="applyDateFrom" value="${conditionsMap.applyDateFrom}" onclick="WdatePicker()" readonly="readonly" /> <span>
						至 </span> <input id="" class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}"
						onclick="WdatePicker()" readonly="readonly" />
					</div>
					<div class="activitylist_bodyer_right_team_co3">
						<label class="activitylist_team_co3_text">审批发起人：</label>
						<select name="applyPerson" id="salerrefund" >
						<option value="" selected="selected">全部</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }"
								<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name
								}</option>
						</c:forEach>
					</select> 
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">审批状态：</label>
						<div class="selectStyle">
							<select name="reviewStatus" id="">
								<option value="" selected="selected">全部</option>
								<option value="1"
								<c:if test="${conditionsMap.reviewStatus == '1' }"> selected="selected" </c:if>>审批中</option>
								<option value="2"
								<c:if test="${conditionsMap.reviewStatus == '2' }"> selected="selected" </c:if>>审批通过</option>
								<option value="0"
								<c:if test="${conditionsMap.reviewStatus == '0' }"> selected="selected" </c:if>>审批驳回</option>
								<option value="3"
								<c:if test="${conditionsMap.reviewStatus == '3' }"> selected="selected" </c:if>>取消申请</option>
							</select>
						</div>
					</div>
					<%-- <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
						<select name="paymentType">
							<option value="">不限</option>
							<c:forEach var="pType" items="${fns:findAllPaymentType()}">
								<option value="${pType[0] }"
									<c:if test="${conditionsMap.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
							</c:forEach>
						</select>
					</div> --%>
					<div class="kong"></div>

				</div>
				<div class="kong"></div>
			</div>

			<!--状态开始-->
			<div class="supplierLine">
				<a href="javascript:void(0)" id="all" onClick="statusChooses('0')" <c:if test = "${conditionsMap.tabStatus == null || conditionsMap.tabStatus == 0 || conditionsMap.tabStatus == '0'}">class="select" </c:if>>全部</a>
				<a id="todo" href="javascript:void(0)" onClick="statusChooses('1')" <c:if test = "${conditionsMap.tabStatus == '1' || conditionsMap.tabStatus == 1}">class="select" </c:if>> 待本人审批</a>
				<a id="todo" href="javascript:void(0)" onClick="statusChooses('2')" <c:if test = "${conditionsMap.tabStatus == '2' || conditionsMap.tabStatus == 2}">class="select" </c:if>>本人已审批</a>
				<a id="todoing" href="javascript:void(0)" onClick="statusChooses('3')" <c:if test = "${conditionsMap.tabStatus == '3' || conditionsMap.tabStatus == 3}">class="select" </c:if>>非本人审批</a>
			</div>

			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>
							<c:choose>
							<c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
							<li class="activitylist_paixu_moren">
								</c:when>
								<c:otherwise>
							<li class="activitylist_paixu_left_biankuang">
								</c:otherwise>
								</c:choose>
								<input type="hidden" value="" name="orderCreateDateSort">
								<a onclick="sort(this)">创建日期
									<c:choose>
										<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
											<i class="icon icon-arrow-down"></i>
										</c:when>
										<c:otherwise>
											<i class="icon icon-arrow-up"></i>
										</c:otherwise>
									</c:choose>
								</a>
								<input type="hidden" value="" name="orderCreateDateCss">
							</li>

							<c:choose>
							<c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
							<li class="activitylist_paixu_moren">
								</c:when>
								<c:otherwise>
							<li class="activitylist_paixu_left_biankuang">
								</c:otherwise>
								</c:choose>
								<input type="hidden" value="" name="orderUpdateDateSort">
								<a onclick="sort(this)">更新日期
									<c:choose>
										<c:when test="${conditionsMap.orderUpdateDateSort == 'desc' }">
											<i class="icon icon-arrow-down"></i>
										</c:when>
										<c:otherwise>
											<i class="icon icon-arrow-up"></i>
										</c:otherwise>
									</c:choose>
								</a>
								<input type="hidden" value="" name="orderUpdateDateCss">
							</li>
							<!-- <li class="activitylist_paixu_left_biankuang orderCreateDateSort"><a onclick="sortby('ordercreateDateSort',this)">创建时间</a></li>
                            <li class="activitylist_paixu_left_biankuang orderUpdateDateSort"><a onclick="sortby('orderUpdateDateSort',this)">更新时间</a></li> -->
						</ul>
					</div>

					<div class="activitylist_paixu_right">
						查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
					</div>
					<div class="kong"></div>
				</div>
			</div>
			<!--状态结束-->
		</form>

		<table id="contentTable" class="table mainTable activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="4%">序号</th>
					<th width="8%">订单号</th>
					<th width="8%"><span class="tuanhao on">团号</span> / <span
						class="chanpin">产品名称</span>
					</th>
					<th width="5%">产品类型</th>
					<th width="5%">申请时间</th>
					<th width="7%">审批发起人</th>
					<th width="6%">渠道商</th>
					<th width="7%">原始应收价</th>
					<th width="6%" style="display: none">改前金额</th>
					<th width="6%" style="display: none">改后金额</th>
					<th width="6%">改价差额</th>
					<th width="6%">上一环节审批人</th>
					<th width="7%">审批状态</th>
					<th width="6%">操作</th>
				</tr>
			</thead>
			<tbody>
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr>
					<td colspan="14" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>
			<%
				int n = 1;
			%>
			<c:forEach items="${page.list}" var="returnReviewInfo">
				<tr>
                    <c:choose>
                        <c:when test="${returnReviewInfo.producttype ne 7 && returnReviewInfo.producttype ne 6}">
                            <td id="currencyIdStr" style="display: none"><c:forEach items="${fns:getCosts(returnReviewInfo.travellerid, 1)}" var="money" varStatus="count"><c:choose><c:when test="${not count.last}">${money.currencyId}/</c:when><c:otherwise>${money.currencyId}</c:otherwise></c:choose></c:forEach></td>
                            <td id="amountStr" style="display: none"><c:forEach items="${fns:getCosts(returnReviewInfo.travellerid, 1)}" var="money" varStatus="count"><c:choose><c:when test="${not count.last}">${money.sum}/</c:when><c:otherwise>${money.sum}</c:otherwise></c:choose></c:forEach></td>
                        </c:when>
                        <c:otherwise>
                            <td id="currencyIdStr" style="display: none">${returnReviewInfo.currencyid}</td>
                            <td id="amountStr" style="display: none">${returnReviewInfo.changedprice}</td>
                        </c:otherwise>
                    </c:choose>
					<td><c:if test="${conditionsMap.tabStatus == '1'}"><input type="checkbox" name="checkboxrevid" value="${returnReviewInfo.reviewid}" /></c:if><%=n++%></td>
					<td>${returnReviewInfo.orderno}</td>
					<td><div title="4535DFDSF" class="tuanhao_cen onshow">${returnReviewInfo.groupcode}</div>
						<div title="${returnReviewInfo.productname}" class="chanpin_cen qtip">
							<c:if test="${returnReviewInfo.producttype ==7}">
								<a href="${ctx}/airTicket/actityAirTickettail/${returnReviewInfo.productid}" target="_blank">${returnReviewInfo.productname}</a>
							</c:if>
							<c:if test="${refundReviewInfo.producttype !=7 && returnReviewInfo.producttype !=6}">
								<a href="${ctx}/activity/manager/detail/${returnReviewInfo.productid}" target="_blank">${returnReviewInfo.productname}</a>
							</c:if>
							<c:if test="${returnReviewInfo.producttype ==6}">
								<a href="${ctx}/visa/preorder/visaProductsDetail/${returnReviewInfo.productid}" target="_blank">${returnReviewInfo.productname}</a>
							</c:if>
						</div>
					</td>
					<td class="tc">${fns:getDictLabel(returnReviewInfo.producttype, 'order_type', returnReviewInfo.producttype)}</td>
					<td class="p0"><div class="out-date"><fmt:formatDate value="${returnReviewInfo.createdate}" pattern="yyyy-MM-dd"/></div>
						<div class="close-date time"><fmt:formatDate value="${returnReviewInfo.createdate}" pattern="HH:mm:ss"/></div>
					</td>
					<td class="tc">${fns:getUserNameById(returnReviewInfo.createby)}</td>
					<td class="tc">
						<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && returnReviewInfo.agent==-1}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						       ${fns:getAgentName(returnReviewInfo.agent)}
						   </c:otherwise>
						</c:choose>
					</td>
					<td class="tr">${fns:getMoneyAmountBySerialNum(returnReviewInfo.payPriceSerialNum,2)}</td>
					<td class="tc" style="display: none">
						<c:forEach items="${returnReviewInfo.originalMoneyList}" var="originalMoney" varStatus="c">
							<c:choose>
								<c:when test="${!c.last}">
									<span>${fns:getCurrencyInfo(originalMoney.currencyId,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${originalMoney.amount}" /></span> +
								</c:when>
								<c:otherwise>
									<span>${fns:getCurrencyInfo(originalMoney.currencyId,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${originalMoney.amount}" /></span>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</td>
					<td class="tc" style="display: none">
						<c:forEach items="${returnReviewInfo.moneyList}" var="money" varStatus="cc">
							<c:choose>
								<c:when test="${!cc.last}">
									<span>${fns:getCurrencyInfo(money.key,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${money.value}" /></span> +
								</c:when>
								<c:otherwise>
									<span>${fns:getCurrencyInfo(money.key,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${money.value}" /></span>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</td>
					<td class="tc">
                        <c:choose>
                            <c:when test="${returnReviewInfo.producttype ne 7 && returnReviewInfo.producttype ne 6}">
                                <c:forEach items="${fns:getCosts4Review(returnReviewInfo.reviewid)}" var="money" varStatus="count">
                                    <c:choose>
                                        <c:when test="${not count.last}">
                                            <span>${fns:getCurrencyNameOrFlag(money.currencyId, "0")}</span> <span class='tdorange fbold' flag='original'><fmt:formatNumber type="currency" pattern="#,###.00" value="${money.sum}" /></span> +
                                        </c:when>
                                        <c:otherwise>
                                            <span>${fns:getCurrencyNameOrFlag(money.currencyId, "0")}</span> <span class='tdorange fbold' flag='original'><fmt:formatNumber type="currency" pattern="#,###.00" value="${money.sum}" /></span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                ${fns:getCurrencyNameOrFlag(returnReviewInfo.currencyid, 0)}<span class="fbold tdred"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${returnReviewInfo.changedprice}"/>
                            </c:otherwise>
                        </c:choose>
					</td>
					<td class="tc">${fns:getUserNameById(returnReviewInfo.lastreviewer)}</td>
					<td class="invoice_yes tc">${returnReviewInfo.statusdesc}</td>
					<td class="p0"><dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png" />
							</dt>
							<dd class="">
								<p>
									<span></span> 
									<a href="${ctx}/changepricenew/changpricereviewnewdetail?orderid=${returnReviewInfo.orderid}&revid=${returnReviewInfo.reviewid}&prdType=${returnReviewInfo.producttype}&flag=1" target = "_blank">查看</a> 
									<c:if test="${returnReviewInfo.isBackReview == true}">
										<a href="javascript:void(0)" onClick = "backReview('${returnReviewInfo.reviewid}')">撤销</a> 
									</c:if>
									<c:if test="${returnReviewInfo.producttype == 7 || returnReviewInfo.producttype == 6}">
										<c:if test="${not empty returnReviewInfo.productid}"><a href="${ctx }/cost/manager/forcastList/${returnReviewInfo.productid}/${returnReviewInfo.producttype}" target="_blank">预报单</a></c:if>
	                              		<c:if test="${not empty returnReviewInfo.productid}"><a href="${ctx }/cost/manager/settleList/${returnReviewInfo.productid}/${returnReviewInfo.producttype}" target="_blank">结算单</a></c:if>
									</c:if>
									<c:if test="${returnReviewInfo.producttype != 7 && returnReviewInfo.producttype != 6&& returnReviewInfo.producttype != 8&& returnReviewInfo.producttype != 9}">
										<c:if test="${not empty returnReviewInfo.groupid}"><a href="${ctx }/cost/manager/forcastList/${returnReviewInfo.groupid}/${returnReviewInfo.producttype}" target="_blank">预报单</a></c:if>
	                              		<c:if test="${not empty returnReviewInfo.groupid}"><a href="${ctx }/cost/manager/settleList/${returnReviewInfo.groupid}/${returnReviewInfo.producttype}" target="_blank">结算单</a></c:if>
									</c:if>
									<c:if test="${returnReviewInfo.isCurReviewer == true}">
										<a href="${ctx}/changepricenew/changpricereviewnewdetail?orderid=${returnReviewInfo.orderid}&revid=${returnReviewInfo.reviewid}&prdType=${returnReviewInfo.producttype}&flag=0" target = "_blank">审批</a>
									</c:if>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="page">
			<c:if test="${conditionsMap.tabStatus == '1'}">
			<div class="pagination">
				<dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox">全选
					</dt>
					<dt>
						<input name="reverseChk" onclick="checkreverse(this)"
							type="checkbox">反选
					</dt>
					<dd>
						<input class="btn ydbz_x" type="button" value="批量审批"  onclick="jbox__shoukuanqueren_chexiao_fab();">
					</dd>
				</dl>
			</div>
			</c:if>
			<div class="pagination clearFix">${page}</div>
		</div>
		<!--右侧内容部分结束-->

	<!--S批量审核操作弹出层-->
	<div class="batch-verify-list" id="batch-verify-list"
		style="padding:20px;">
		<table width="100%"
			style="padding:10px !important; border-collapse: separate;">
			<tr>
				<td></td>
			</tr>
			<tr>
				<td><p>您好，当前您提交了23个审核项目，是否执行批量操作？</p>
				</td>
			</tr>
			<tr>
				<td><p>备注：</p>
				</td>
			</tr>
			<tr>
				<td><label> <textarea name="textfield" id="textfield"
							style="width: 290px;"></textarea> </label>
				</td>
			</tr>
		</table>
	</div>

	<!--S批量审核操作弹出层-->
</body>
</html>
