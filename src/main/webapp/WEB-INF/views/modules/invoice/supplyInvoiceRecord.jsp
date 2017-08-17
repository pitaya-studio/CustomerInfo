<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title><c:if test="${param.verifyStatus eq ''}">发票记录</c:if>
		<c:if test="${param.verifyStatus eq 'ne0'}">已审核发票</c:if>
		<c:if test="${param.verifyStatus eq '1'}">开票</c:if></title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/finance/invoice.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function () {
		//输入金额提示
		inputTips();
        //展开筛选按钮
        launch();
		$(document).scrollLeft(0);
		$("#targetAreaId").val("${travelActivity.targetAreaIds}");
		$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
	
		$("#contentTable").delegate("ul.caption > li", "click", function () {
			var iIndex = $(this).index(); /*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
			$(this).addClass("on").siblings().removeClass('on');
			$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
		});
	
		$('.handle').hover(function () {
			if (0 != $(this).find('a').length) {
				$(this).addClass('handle-on');
				$(this).find('dd').addClass('block');
			}
		}, function () {
			$(this).removeClass('handle-on');
			$(this).find('dd').removeClass('block');
		});
		//C434备注和审核备注切换
		$('.thApproveNotes').click(function () {
			$(this).removeClass('active');
			$('.thNotes').addClass('active');
			$('.thInvoiceNotes').addClass('active');
			$('.approveNotes').show();
			$('.notes').hide();
			$('.invoiceNotes').hide();
		});
		$('.thNotes').click(function () {
			$(this).removeClass('active');
			$('.thApproveNotes').addClass('active');
			$('.thInvoiceNotes').addClass('active');
			$('.notes').show();
			$('.approveNotes').hide();
			$('.invoiceNotes').hide();
		});
		$('.thInvoiceNotes').click(function () {
			$(this).removeClass('active');
			$('.thNotes').addClass('active');
			$('.thApproveNotes').addClass('active');
			$('.invoiceNotes').show();
			$('.notes').hide();
			$('.approveNotes').hide();
		});

		//如果展开部分有查询条件的话，默认展开，否则收起
		var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
		var searchFormselect = $("#searchForm").find("select[id!='orderShowType']");
		var inputRequest = false;
		var selectRequest = false;
		for (var i = 0; i < searchFormInput.length; i++) {
			if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
				inputRequest = true;
			}
		}
		for (var i = 0; i < searchFormselect.length; i++) {
			if ($(searchFormselect[i]).children("option:selected").val() != "" &&
				$(searchFormselect[i]).children("option:selected").val() != null) {
				selectRequest = true;
			}
		}
	
		if (inputRequest || selectRequest) {
			$('.zksx').click();
		}
	
		$("#contentTable").delegate(".tuanhao", "click", function () {
			$(this).addClass("on").siblings().removeClass('on');
			$('.chanpin_cen').removeClass('onshow');
			$('.tuanhao_cen').addClass('onshow');
		});
	
		$("#contentTable").delegate(".chanpin", "click", function () {
			$(this).addClass("on").siblings().removeClass('on');
			$('.tuanhao_cen').removeClass('onshow');
			$('.chanpin_cen').addClass('onshow');
	
		});
	
		var _$orderBy = $("#orderBy").val();
		if (_$orderBy == "") {
			_$orderBy = "createDate DESC";
			$("#orderBy").val(_$orderBy);
		}
		var orderBy = _$orderBy.split(" ");
		$(".activitylist_paixu_left li").each(function () {
			if ($(this).hasClass("li" + orderBy[0])) {
				orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
				$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
				$(this).attr("class", "activitylist_paixu_moren");
			}
		});
	
	});
	
	$().ready(function () {
		$("#wholeSalerKey").focusin(function () {
			var obj_this = $(this);
			obj_this.next("span").hide();
		}).focusout(function () {
			var obj_this = $(this);
			if (obj_this.val() != "") {
				obj_this.next("span").hide();
			} else
				obj_this.next("span").show();
		});
		if ($("#wholeSalerKey").val() != "") {
			$("#wholeSalerKey").next("span").hide();
		}
	});
	
	//刷新
	function refresh() {
		setTimeout(location.reload(true), 10000);
	}
	
	function page(n, s) {
		var url = "${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=" + "${param.verifyStatus }";
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action", url);
		$("#searchForm").submit();
		return false;
	}
	
	$(function () {
		$.fn.datepicker = function (option) {
			var opt = {}
			 || option;
			this.click(function () {
				WdatePicker(option);
			});
		}
	
		$("#groupOpenDate").datepicker({
			dateFormat : "yy-mm-dd",
			dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
			closeText : "关闭",
			prevText : "前一月",
			nextText : "后一月",
			monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
		});
	
		$("#groupCloseDate").datepicker({
			dateFormat : "yy-mm-dd",
			dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
			closeText : "关闭",
			prevText : "前一月",
			nextText : "后一月",
			monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
		});
	
		$("#orderTimeBegin").datepicker({
			dateFormat : "yy-mm-dd",
			dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
			closeText : "关闭",
			prevText : "前一月",
			nextText : "后一月",
			monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
		});
		$("#orderTimeEnd").datepicker({
			dateFormat : "yy-mm-dd",
			dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
			closeText : "关闭",
			prevText : "前一月",
			nextText : "后一月",
			monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
		});
	
	});
	
	function sortby(sortBy, obj) {
		var temporderBy = $("#orderBy").val();
		if (temporderBy.match(sortBy)) {
			sortBy = temporderBy;
			if (sortBy.match(/ASC/g)) {
				sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
			} else {
				sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
			}
		} else {
			sortBy = sortBy + " DESC";
		}
	
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}
	
	/**
	 * 查询条件重置
	 *
	 */
	var resetSearchParams = function () {
		$(':input', '#searchForm')
			.not(':button, :submit, :reset, :hidden')
			.val('')
			.removeAttr('checked')
			.removeAttr('selected');
		$('#targetAreaId').val('');
		$('#orderShowType').val('${showType}');
	}
	
	/**
	 * 查询
	 */
	function query() {
		$('#searchForm').submit();
	}
	
	function expand(child, obj, srcActivityId, uuid, payMode_advance, payMode_full, payMode_op, 
		payMode_cw, payMode_data, payMode_guarantee, payMode_express, showType) {
		if ($(child).is(":hidden")) {
			if (uuid != "") {
				//暂时取消，币种，订单金额，计调人员的查询
				var currencyId = ""; //$("#c_cny").val();
				var orderMoneyBegin = ""; //$("#m_order_money_begin").val();
				var orderMoneyEnd = ""; //$("#m_order_money_end").val();
				var operator = ""; //$("#p_operator").val();
				$.ajax({
					type : "post",
					url : "${ctx}/invoice/limit/supplyviewdetailopen/" + uuid + "?1=1&currencyId="
					 + currencyId + "&orderMoneyBegin=" + orderMoneyBegin + "&orderMoneyEnd="
					 + orderMoneyEnd + "&operator=" + operator,
					success : function (msg) {
						if (msg.length > 0) {
							var record = '';
							for (var i = 0; i < msg.length; i++) {
								if (msg[i][6] == null)
									msg[i][6] = "";
								if (msg[i][7] == null)
									msg[i][7] = "";
								if (msg[i][8] == null)
									msg[i][8] = "";
								if (msg[i][9] == null)
									msg[i][9] = "";
	
								record += "<tr><td class='tc'>" + (i + 1) + "</td><td class='tc'>" + msg[i][12] + "</td><td class='tc'>" + msg[i][0]
								 + "</td><td class='tc'>" + msg[i][2] + "</td><td class='tc'>" + msg[i][1] + "</td><td class='tc'>" + msg[i][3] + "</td><td class='tc'>" + msg[i][15]
								 + "</td><td class='tc'>" + msg[i][4] + "</td><td class='tc'>" + msg[i][5] + "</td><td style='padding: 0px;' class='tc'><div class='out-date'>" + msg[i][6]
								 + "</div><div class='close-date'>" + msg[i][7] + "</div></td><td class='tr'>" + msg[i][14] + "</td><td class='tr'>" + msg[i][9]
								 + "</td><td class='tr'>" + msg[i][10] + "</td><td class='tr'>" + msg[i][11] + "</td></tr>";
							}
							$(child).find("td table tbody").html(record);
						} else {
							$(child).find("td table tbody").html('<tr><td colspan="14" class="tc">暂无数据</td></tr>');
						}
						$(obj).html("关闭订单列表");
						$(child).show();
						$(obj).parents("td").addClass("td-extend");
					}
				});
			} else {
				$(obj).html("关闭订单列表");
				$(child).show();
				$(obj).parents("td").addClass("td-extend");
			}
		} else {
			if (!$(child).is(":hidden")) {
				$(child).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).html("展开订单列表");
			}
		}
	}
	
	function viewdetail(uuid, verifyStatus) {
		verifyStatus = '-2';
		window.open("${ctx}/invoice/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus);
	}
	
	function makinvoice(uuid, verifyStatus) {
		window.location.href = "${ctx}/invoice/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus;
	}
	
	function receive(uuid) {
		$("#receiveInvoiceHid").val("true");
		$("#searchForm").attr("action", "${ctx}/invoice/limit/verifyinvoice/" + uuid);
		$("#searchForm").submit();
	}
	
	function revokeToUncheck(uuid) {
		var submit = function (v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在撤销数据...", 'loading');
				$.ajax({
					type : "POST",
					url : "${ctx}/invoice/limit/revokeToUncheck/" + uuid,
					dataType : "json",
					data : {},
					success : function (data) {
						if ('success' == data.flag) {
							$.jBox.tip(data.msg, 'success');
							$("#searchForm").submit();
						} else if ('fail' == data.flag) {
							$.jBox.tip(data.msg, 'fail');
						}
					}
				});
			}
			return true; //close
		};
		$.jBox.confirm("确定要撤销该发票吗？", "提示", submit);
	}
	
	function revokeToUninvioce(uuid) {
		var submit = function (v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在撤销数据...", 'loading');
				$.ajax({
					type : "POST",
					url : "${ctx}/invoice/limit/revokeToUninvioce/" + uuid,
					dataType : "json",
					data : {},
					success : function (data) {
						if ('success' == data.flag) {
							$.jBox.tip(data.msg, 'success');
							$("#searchForm").submit();
						} else if ('fail' == data.flag) {
							$.jBox.tip(data.msg, 'error');
						}
					}
				});
			}
			return true; //close
		};
		$.jBox.confirm("确定要撤销该发票吗？", "提示", submit);
	}
	
	$(function () {
		$(".yukai").hover(function () {
			$(this).children().show();
		}, function () {
			$(this).children().hide();
		})
	});
</script>

<style type="text/css">
a{ display: inline-block;}
*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}
.relative{ position:relative;}
.active { color: #08c;}
.thApproveNotes, .thNotes, .thInvoiceNotes { cursor: pointer;} 
.yukai{ background:#28b2e6; color:#fff; position:absolute; top:2px; right:2px; padding:0px 2px; border-radius:2px;}
.yukai span{ position:absolute; display:none; width:100px; background:#fff; border:1px solid #ddd; border-radius:2px; left:30px; top:0px; color:#333; padding:2px 6px;}
</style>

</head>

<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="order_op_head" >
        <page:param name="showType">${showType}</page:param>
        <page:param name="orderStatus">1</page:param>
    </page:applyDecorator>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
	<!-- 查询form -->
	<page:applyDecorator name="supplier_invoice_op_head" >
        <page:param name="current"><c:choose><c:when test="${verifyStatus eq ''}">invoiceTrace</c:when><c:when test="${verifyStatus eq 'ne0'}">invoiceApplied</c:when><c:when test="${verifyStatus eq '1'}">invoiceOuted</c:when><c:otherwise>invoiceApply</c:otherwise></c:choose></page:param>
    </page:applyDecorator>
	<form:form id="searchForm" modelAttribute="travelActivity" class="form-search" action="${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=${param.verifyStatus }" method="post" >
		<input id="receiveInvoiceHid" name="receiveInvoiceHid" value="" type="hidden"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<%@ include file="/WEB-INF/views/modules/invoice/supplyInvoiceSearchParams.jsp" %>
	</form:form>

	<!-- 排序 -->
	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
	            	<li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
	            	<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
	            </ul>
      		</div>
          	<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        </div>
	</div>
	<c:if test="${fn:length(page.list) ne 0}">	
  <table class="activitylist_bodyer_table mainTable table" id="contentTable">
          <thead>
            <tr>
              <th width="3%">序号</th>
              <c:if test="${param.verifyStatus ne '0'}"><th width="10%">发票号</th></c:if>
              <th width="6%">开票方式</th>
              <c:if test="${param.verifyStatus ne ''}"><th width="6%">发票抬头</th></c:if>
              <!-- 0453需求,开票类型替换成开票项目-->
        	<c:choose>
	            <c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}"> 
	            	<th width="6%">开票项目</th>
	            </c:when>
		       	<c:otherwise>
		       		<th width="6%">开票类型</th>
	            </c:otherwise>
	        </c:choose>
	        <!-- 0453需求,开票类型替换成开票项目-->
			  <c:if test="${!isYJXZ and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}"><%--越谏行踪隐藏开票项目，需求0411，yudong.xu  0453起航假期也需隐藏--%>
              	<th width="6%">开票项目</th>
			  </c:if>
			  <th width="6%">开票客户</th>
			  <th width="6%">来款单位</th>
              <th width="8%">申请日期</th>
              <c:if test="${param.verifyStatus ne '0'}">
              <th width="8%">开票日期</th>
              </c:if>
              <th width="5%">申请人</th>
              <th width="7%">开票金额</th>
              <c:if test="${param.verifyStatus eq '1' }">
              <th width="4%">开票状态</th>
              </c:if>
              <c:if test="${param.verifyStatus eq '' || param.verifyStatus eq '0' || param.verifyStatus eq 'ne0'}">
              <th width="5%">审核状态</th>
              <%--<c:if test="${param.verifyStatus ne '0'}"><th width="7%">开票人</th></c:if>--%>
              </c:if>
              <c:if test="${param.verifyStatus eq '1'}">
              			<th width="13%"> 
              				<span href="javascript:void(0)" class="thNotes">申请</span>
                              /
                            <span href="javascript:void(0)" class="thApproveNotes active">审核备注</span>
							  /
                            <span href="javascript:void(0)" class="thInvoiceNotes active">开票备注</span>
                        </th>
              </c:if>
              <c:if test="${param.verifyStatus ne '1'}">
              			<th width="13%">备注</th>
              </c:if>
              <th width="10%">操作</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${page.list}" var="orderinvoice" varStatus="s">
            <tr>
              <td>
              <c:if test="${param.verifyStatus eq '1'}">
              	<input name="ids" value="${orderinvoice['uuid']}" type="checkbox">
              </c:if>
              	${s.index+1}
              </td>
              <c:if test="${param.verifyStatus ne '0'}"><td><c:if test="${orderinvoice['createStatus'] eq  '1'}">${orderinvoice['invoiceNum']}</c:if></td></c:if>
              <td><c:forEach var="invoicem" items="${invoiceModes }">
	          	<c:if test="${invoicem.value==orderinvoice.invoiceMode}">${invoicem.label }</c:if>
	          </c:forEach></td>
	           <c:if test="${param.verifyStatus ne ''}"><td>${orderinvoice['invoiceHead'] }</td></c:if>
        	 <!-- 0453需求,开票类型替换成开票项目-->
        	<c:choose>
	            <c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}">
	            	 <td>
		                  <c:forEach var="ins" items="${invoiceSubject_qhjq }">
		                  <c:if test="${ins.value==orderinvoice.invoiceSubject}">${ins.label }</c:if>
		                  </c:forEach>
		                  <c:if test="${orderinvoice.receiveStatus == 1 }"><font style="color:red">(已领取)</font></c:if>
		                  <c:if test="${orderinvoice.createStatus==1 and orderinvoice.receiveStatus == 0 }"><font style="color:red">(已开票)</font></c:if>
		                  <c:if test="${orderinvoice.createStatus==0 and orderinvoice.receiveStatus == 0 }"><font style="color:red">(未开票)</font></c:if>
	                </td>
             	 </c:when>
             	 
       			 <c:otherwise>
       			 	 <td class="relative">
       			 	 <c:forEach var="invoice" items="${invoiceTypes }">
	                 <c:if test="${invoice.value==orderinvoice.invoiceType}">${invoice.label }</c:if>
	                 </c:forEach>
	                 <c:if test="${orderinvoice.receiveStatus == 1 }"><font style="color:red">（已领取）</font></c:if>
	                 <!-- 0444需求 -->
	              	  <c:if test="${orderinvoice.applyInvoiceWay == '1' }">
	              	  	<div class="yukai relative">预开
	              	  	<span >
	              	  	<c:choose>
	              	  		<c:when test="${orderinvoice.receivedPayStatus == '1' }">回款状态：已回款</c:when>
	              	  		<c:otherwise>回款状态：未回款</c:otherwise>
	              	  	</c:choose>
	              	  	</span>
	              	  	</div>
	              	  </c:if>
	              	  <!-- 0444需求 -->
	                 </td>
       			 </c:otherwise>
        	</c:choose>
        	<!-- 0453需求,开票类型替换成开票项目-->
			  <c:if test="${!isYJXZ and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}"><%--越谏行踪隐藏开票项目，需求0411，yudong.xu  0453起航假期也需隐藏--%>
				  <td><c:forEach var="invoices" items="${invoiceSubjects }">
					<c:if test="${invoices.value==orderinvoice.invoiceSubject}">${invoices.label }</c:if>
				  </c:forEach></td>
			  </c:if>
              <td>${orderinvoice['invoiceCustomer']}</td>
              <td>${orderinvoice['invoiceComingUnit']}</td>   <!-- 来款单位 -->
              <td class="tc"><fmt:formatDate value="${orderinvoice['createDate']}" pattern="yyyy-MM-dd HH:mm"/></td>
              <c:if test="${param.verifyStatus ne '0'}">
              <td class="tc">
              		<c:choose>
              			<c:when test="${orderinvoice['createStatus'] eq '0' }"></c:when>
              			<c:when test="${orderinvoice['createStatus'] eq '1' }">
              				<fmt:formatDate value="${orderinvoice['updateDate']}" pattern="yyyy-MM-dd"/></td>
              			</c:when>
              			<c:otherwise></c:otherwise>
              		</c:choose>
              </c:if>
              <td>${orderinvoice['createName']}</td>
              <td class="tr">¥<span class="fbold tdorange">${orderinvoice['invoiceAmount']}</span></td>
              <c:if test="${param.verifyStatus eq '1' }">
              <c:if test="${orderinvoice['createStatus'] eq '0'}">
						<td class="invoice_no">待开票</td>
					</c:if>
					<c:if test="${orderinvoice['createStatus'] eq  '1'}">
						<td class="invoice_yes">已开票</td>
					</c:if>
			</c:if>
			<c:if test="${param.verifyStatus eq '' || param.verifyStatus eq '0' || param.verifyStatus eq 'ne0'}">
              <c:if test="${orderinvoice['verifyStatus'] eq '0'}">
						<td class="invoice_no">未审核</td>
					</c:if>
					<c:if test="${orderinvoice['verifyStatus'] eq  '1'}">
						<td class="invoice_yes">审核通过</td>
					</c:if>
					<c:if test="${orderinvoice['verifyStatus'] eq  '2'}">
						<td class="invoice_back">被驳回</td>
					</c:if>
              <!--<c:if test="${param.verifyStatus ne '0'}"><td><c:if test="${orderinvoice['createStatus'] eq  '1'}">${orderinvoice['updateName']}</c:if></td></c:if>-->
              </c:if>
              <td>
              <c:if test="${param.verifyStatus eq '1'}">
              	<span class="notes">${orderinvoice['remarks']}</span>
              	<span class="approveNotes" style="display: none">${orderinvoice['reviewRemark']}</span>
				<span class="invoiceNotes" style="display:none">${orderinvoice['invoiceRemark']}</span>
              </c:if>
              <c:if test="${param.verifyStatus ne '1'}">
              ${orderinvoice['remarks']}
              </c:if>
              </td>
              
              <td>
              <c:choose>
	              <c:when test="${param.verifyStatus eq '' }">
	              	<a href="javascript:void(0)" onClick="viewdetail('${orderinvoice['uuid']}','${param.verifyStatus}')">详情</a>
	              </c:when>
	              <c:when test="${param.verifyStatus eq '0'}">
	              	<a href="javascript:void(0)" onClick="makinvoice('${orderinvoice['uuid']}','${param.verifyStatus}')">审核</a>
	              	<a href="javascript:void(0)" onClick="viewdetail('${orderinvoice['uuid']}','${param.verifyStatus}')">明细</a>
	              </c:when>
	              <c:when test="${param.verifyStatus eq 'ne0'}">
	              	<c:if test="${orderinvoice.createStatus ne '1'}">
	              		<a href="javascript:void(0)" onClick="revokeToUncheck('${orderinvoice['uuid']}')">撤销</a>
	              	</c:if>
	              	<a href="javascript:void(0)" onClick="viewdetail('${orderinvoice['uuid']}','${param.verifyStatus}')">明细</a>
	              </c:when>
	              <c:when test="${param.verifyStatus eq '1'}">
	              	<c:if test="${orderinvoice.createStatus ne '0'}">
	              		<a href="javascript:void(0)" onClick="revokeToUninvioce('${orderinvoice['uuid']}')">撤销</a>
	              	</c:if>
	              	<a href="javascript:void(0)" onClick="viewdetail('${orderinvoice['uuid']}','${param.verifyStatus}')">明细</a>
	              	<c:if test="${orderinvoice.createStatus eq '0'}">
	              		<a href="javascript:void(0)" onClick="makinvoice('${orderinvoice['uuid']}','-1')">开票</a>
	              	</c:if>
	              	<c:if test="${orderinvoice.createStatus eq '1' && orderinvoice.receiveStatus != 1}">
	              		<a href="javascript:void(0)" onClick="receive('${orderinvoice.uuid}')">领取</a>
	              	</c:if>
	              </c:when>
              </c:choose> 
              <a class="team_a_click" href="javascript:void(0)" onclick="expand('#child${s.count}',this,'${s.count}','${orderinvoice['uuid']}')" >展开订单列表</a>
              </td>
            </tr>
            <tr id="child${s.count}" style="display: none;" class="activity_team_top1">
              <td colspan="<c:if test="${param.verifyStatus eq ''}">14</c:if>
              			   <c:if test="${param.verifyStatus eq 'ne0'}">15</c:if>
              			   <c:if test="${param.verifyStatus eq '0'}">13</c:if>
              			   <c:if test="${param.verifyStatus eq '1'}">15</c:if>" class="team_top">
              <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                  <thead>
                    <tr>
						<th class="tc" width="4%">序号</th>
						<th class="tc" width="6%">订单类型</th>
						<th class="tc" width="9%">订单号</th>
						<th class="tc" width="9%">开票客户</th>
						<c:choose>
							<c:when test="${isHQX}">
								<th class="tc" width="10%">订单团号</th>
							</c:when>
							<c:otherwise>
								<th class="tc" width="10%">团号</th>
							</c:otherwise>
						</c:choose>
						<th class="tc" width="5%">销售</th>
						<th class="tc" width="5%">计调</th>
						<th class="tc" width="7%">下单时间</th>
						<th class="tc" width="5%">人数</th>
						<th class="tc" width="7%">出/截团日期</th>
						<th class="tr" width="8%">应收金额</th>
						<th class="tr" width="8%">财务到账</th>
						<th class="tr" width="8%">已开票金额</th>
						<th class="tr" width="8%">开票金额</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
                </td>
            </tr>
           </c:forEach>
          </tbody>
        </table>
        </c:if>
        <c:if test="${fn:length(page.list) eq 0}">
			<table id="contentTable" class="table mainTable activitylist_bodyer_table" >
				<thead>
					<tr>
                		<th width="3%">序号</th>
		              	<c:if test="${param.verifyStatus ne '0'}"><th width="10%">发票号</th></c:if>
		              	<th width="6%">开票方式</th>
		              	<th width="6%">开票类型</th>
					  	<c:if test="${!isYJXZ}"><%--越谏行踪隐藏开票项目，需求0411，yudong.xu --%>
					  		<th width="6%">开票项目</th>
					  	</c:if>
		              	<th width="6%">开票客户</th>
		              	<th width="6%">来款单位</th>
		              	<c:if test="${param.verifyStatus ne '0'}">
		            	  	<th width="8%">申请日期</th>
		            	  	<th width="8%">开票日期</th>
		              	</c:if>
		              <th width="5%">申请人</th>
		              <th width="7%">开票金额</th>
		              <c:if test="${param.verifyStatus eq '1' }">
		              <th width="4%">开票状态</th>
		              </c:if>
		              <c:if test="${param.verifyStatus eq '' || param.verifyStatus eq '0' || param.verifyStatus eq 'ne0'}">
		              <th width="5%">审核状态</th>
		              <c:if test="${param.verifyStatus ne '0'}"><th width="7%">开票人</th></c:if>
		              </c:if>
		              <c:if test="${param.verifyStatus eq '1'}">
		              		<th width="13%"> 
		              			<span href="javascript:void(0)" class="thNotes">申请</span>
                                /
                                <span href="javascript:void(0)" class="thApproveNotes active">审核备注</span>
								/
								<span href="javascript:void(0)" class="thInvoiceNotes active">开票备注</span>
                            </th>
		              </c:if>
		              <c:if test="${param.verifyStatus ne '1'}">
		              		<th width="13%">备注</th>
		              </c:if>
		              <th width="10%">操作</th>
                    </tr>
				</thead>
				<tbody>
					<tr>
						<c:set var="colspan" value="15"></c:set>
						<c:choose>
							<c:when test="${param.verifyStatus eq '1' }">
								<c:set var="colspan" value="11"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="colspan" value="14"></c:set>
							</c:otherwise>
						</c:choose>
						<td colspan="${colspan }" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>						
					</tr>
				</tbody>
			</table>
		</c:if>
<div class="page">
	<c:if test="${param.verifyStatus eq '1'}">
		 <div class="pagination">
            <dl>
                <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
                <dd>
                    <input class="btn ydbz_x" type="button" onclick="jbox__batch_invoice_op('${ctx}');" value="批量开票">
                    <input class="btn ydbz_x" type="button"  onclick="batchReceive('${ctx}');" value="批量领取">
                </dd>
            </dl>
         </div>
	</c:if>
	<div class="pagination clearFix">
	   ${page}
	</div>
</div>
    </div>
</body>
</html>
