<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证借款-列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaBorrowMoneyReviewList.js"></script>

<script type="text/javascript">
    var companeyUUID = '${fns:getUser().company.uuid}';
	
	$(function() {
		//搜索条件筛选
		launch();
		inputTips();
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();
		showSearchPanel();

		$("#sysCountryId").comboboxInquiry();
		var resetSearchParams = function() {
			$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
					.val('').removeAttr('checked').removeAttr('selected');
			$('#country').val("");
		}
		$("#applyPerson").comboboxInquiry();
		$("#createBy").comboboxInquiry();

	});
	
	function changeTwoDecimal(v) {
	    if (isNaN(v)) {//参数为非数字
	        return 0;
	    }
	    var fv = parseFloat(v);
	    fv = Math.round(fv * 100) / 100; //四舍五入，保留两位小数
	    var fs = fv.toString();
	    var fp = fs.indexOf('.');
	    if (fp < 0) {
	        fp = fs.length;
	        fs += '.';
	    }
	    while (fs.length <= fp + 2) { //小数位小于两位，则补0
	        fs += '0';
	    }
	    return fs;
	}

	// 	有查询条件的时候，DIV不隐藏
	function showSearchPanel() {
		//签证国家
		var sysCountryId = $("select[name='sysCountryId'] option:selected").val();
		//签证类型
		var visaType = $("select[name='visaType'] option:selected").val();
		//下单人
		var createBy = $("select[name='createBy'] option:selected").val();
		//审批发起人
		var applyPerson = $("select[name='applyPerson'] option:selected").val();
		// 游客
		var travellerName = $("input[name='travellerName']").val();	
		//申请日期
		var applyDateFrom = $("input[name='applyDateFrom']").val();
		var applyDateTo = $("input[name='applyDateTo']").val();
		//审批状态
		var reviewStatus = $("select[name='reviewStatus'] option:selected").val();
		//出纳确认
		var cashConfirm = $("select[name='cashConfirm'] option:selected").val();

		if (isNotEmpty(sysCountryId) || isNotEmpty(visaType)
				|| isNotEmpty(createBy) || isNotEmpty(applyPerson)
				|| isNotEmpty(travellerName) || isNotEmpty(applyDateFrom)
				|| isNotEmpty(applyDateTo) || isNotEmpty(reviewStatus)
				|| isNotEmpty(cashConfirm) ) {
			$('.zksx').click();
		}
	}

	// 判定不为空值
	function isNotEmpty(str) {
		if (str != "" && str != "-1" && str != "null") {
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
	//多选审批  选择处理  全选，反选
	function t_checkall(obj) {
		if (obj.checked) {
			$("input[name='batchNo']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='allChk']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='batchNo']:checked").each(function() {
				this.checked = false;
			});
			$("input[name='allChk']:checked").each(function() {
				this.checked = false;
			});
		}
	}

	function t_checkallNo(obj) {
		$("input[name='batchNo']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
		t_allchk();
	}

	function t_allchk() {
		var chknum = $("input[name='batchNo']").size();
		var chk = 0;
		$("input[name='batchNo']").each(function() {
			if ($(this).attr("checked")=='checked') {
				chk = chk+1;
			}
		});
		if (chknum == chk) {//全选
			$("input[name='allChkNo']").attr("checked", true);
		} else {//不全选
			$("input[name='allChkNo']").attr("checked", false);
		}
	}

	//排序
	function sort(element) {

		var _this = $(element);

		//按钮高亮
		_this.parent("li").attr("class", "activitylist_paixu_moren");

		//原先高亮的同级元素置灰
		_this.parent("li").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");

		//高亮按钮隐藏input赋值
		_this.next().val("activitylist_paixu_moren");

		//原先高亮按钮隐藏input值清空
		_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");

		var sortFlag = _this.children().attr("class");
		//降序
		if (sortFlag == "icon icon-arrow-up") {

			//改变箭头的方向
			_this.children().attr("class", "icon icon-arrow-down");

			//降序
			_this.prev().val("desc");
		}
		//降序
		else if (sortFlag == "icon icon-arrow-down") {
			//改变箭头方向
			_this.children().attr("class", "icon icon-arrow-up");

			//shengx
			_this.prev().val("asc");
		}

		$("#searchForm").submit();

		return false;
	}
	function statusChooses(status) {
		$("#tabStatus").val(status);
		$("#searchForm").submit();
	}

	function batchReviewVisaBorrowMoney() {
		var num = 0;
		var str = "";
		$('[name=batchNo]:checkbox:checked').each(function() {
			if (num == 0) {
				str += $(this).val();
			} else {
				str += "," + $(this).val();
			}
			num++;
		});
		if ("" == str) {
			$.jBox.tip("请选择需要审批的记录！");
			return false;
		}
		var html = "<table width='100%' style='padding:10px !important; border-collapse: separate;'>"
				+ "  <tr>"
				+ "     <td> </td>"
				+ "  </tr>"
				+ "  <tr>"
				+ "     <td><p>您好，当前您提交了" + num + "个审核项目，是否执行批量操作？</p></td>"
				+ "  </tr>"
				+ "  <tr>"
				+ "     <td><p>备注：</p></td>"
				+ "  </tr>"
				+ "  <tr>"
				+ " 	<td><label>"
				+ "     	<textarea name='textfield' id='textfield' style='width: 290px;' maxlength='100' oninput='this.value=this.value.substring(0,100)'></textarea>"
				+ "   	</label></td>" 
				+ "  </tr>" 
				+ "</table>";
		$.jBox(html, {
			title : "批量审批",
			buttons : {
				'取消' : -1,
				'驳回' : 0,
				'通过' : 1
			},
			height : 250,
			width : 350,
			submit : function(v, h, f) {
				if (v == "0" || v == "1") {
					var remark = f.textfield;
					if (remark.length > 200) {
						$.jBox.tip("字符长度过长，请输入少于200个字符", 'error');
						return;
					}
					$.ajax({
						type : "post",
						url : "${ctx}/visa/borrowMoney/review/batchVisaBorrowingMoneyReview",
						data : {
							"batchNos" : str,
							"remark" : remark,
							"result" : v
						},
						success : function(data) {
							if(data.result == 'success') {
								$.jBox.tip("批量审批成功！", "success");
							} else if(data.result == 'fail'){
								$.jBox.tip("批量批核失败！", "error");
							} else {
								$.jBox.tip("程序内部出错!", "error");
							}
							window.location.href = "${ctx}/visa/borrowMoney/review/visaBorrowMoneyReviewList";
						}
					});
				}
			}
		});
		inquiryCheckBOX();
	}
	function backReview(batchNo) {
		$.ajax({
			type : "post",
			url : "${ctx}/visa/borrowMoney/review/backReview?batchNo=" + batchNo,
			success : function(data) {
				console.log(data);
				if (data.result == "fail") {
					$.jBox.tip(data.msg, 'warning');
				} else if (data.result == "success"){
					$.jBox.tip(data.msg, 'success');
					location.reload();
				} else {
					$.jBox.tip("程序内部错误!", 'error');
				}
			}
		});
	}
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<form id="searchForm" action="${ctx}/visa/borrowMoney/review/visaBorrowMoneyReviewList"	method="post">
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<input
					class="txtPro inputTxt searchInput inputTxtlong" id="wholeSalerKey"
					name="groupCode" value="${conditionsMap.groupCode}" type="text" placeholder="团号/订单号/批次号"/>
			</div>
			<a class="zksx">筛选</a>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" /> 
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
			<input type="hidden" id="tabStatus" name = "tabStatus" value = "${conditionsMap.tabStatus}"/><!--bug17500 将#tabStatus移入form内 by tlw at20170309-->
			<%--删除多余筛选按钮 bug17510 by tlw at20170307--%>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit">
			</div>


			<div class="ydxbd">
				<span></span>
				<span></span>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">签证国家：</div>
					<select class="sel-w1"  name="sysCountryId" id="sysCountryId">
                    	<option value="" selected="selected">全部</option>
                        <c:forEach items="${countryInfoList}" var="visaCountry">
                        	<option value="${visaCountry[0]}" <c:if test="${conditionsMap.sysCountryId eq visaCountry[0]}"> selected="selected" </c:if>  >${visaCountry[1]}</option>
                        </c:forEach>
                    </select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">签证类型：</label>
					<div class="selectStyle">
						<div class="selectStyle">
							<select name='visaType' id="visaType">
								<option value="" selected="selected">全部</option>
								<c:forEach items="${visaTypeList}" var="visaTypes">
									<option value="${visaTypes.key}" <c:if test="${conditionsMap.visaType eq visaTypes.key}"> selected="selected"</c:if> >
										${visaTypes.value}
									</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">游客：</label>
					<input type="text" name="travellerName" value="${conditionsMap.travellerName }">
				</div>

				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">审批发起人：</label>
					<select name="applyPerson" id="applyPerson" >
						<!-- 用户类型  1 代表销售 -->
						<option value="" selected="selected">全部</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<option value="${userinfo.id }"
								<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">下单人：</label>
					<select name="createBy" id="createBy" >
						<!-- 用户类型  1 代表销售 -->
						<option value="" selected="selected">全部</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<option value="${userinfo.id }" <c:if test="${conditionsMap.createBy==userinfo.id }">selected="selected"</c:if>>${userinfo.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">申请日期：</label>
					<input id="applyDateFrom" class="inputTxt dateinput" name="applyDateFrom" value="${conditionsMap.applyDateFrom}"
						onclick="WdatePicker()" readonly="readonly" /> 
					<span> 至 </span> 
					<input id="applyDateTo" class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}" 
						onclick="WdatePicker()" readonly="readonly" />
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">审批状态：</label>
					<div class="selectStyle">
						<select name="reviewStatus" id="reviewStatus">
							<option value="-1" selected="selected">全部</option>
							<option value="1"
								<c:if test="${conditionsMap.reviewStatus == 1 }">selected="selected"</c:if>>审批中</option>
							<option value="2"
								<c:if test="${conditionsMap.reviewStatus == 2 }">selected="selected"</c:if>>审批通过</option>
							<option value="0"
								<c:if test="${conditionsMap.reviewStatus == 0 }">selected="selected"</c:if>>审批驳回</option>
							<option value="3"
								<c:if test="${conditionsMap.reviewStatus == 3 }">selected="selected"</c:if>>取消申请</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">出纳确认：</label>
					<div class="selectStyle">
						<select name="cashConfirm" id="cashConfirm">
							<option value="-1" selected="selected">全部</option>
							<option value="1"
								<c:if test="${conditionsMap.cashConfirm == 1 }">selected="selected"</c:if>>已付</option>
							<option value="0"
								<c:if test="${conditionsMap.cashConfirm == 0 }">selected="selected"</c:if>>未付</option>
						</select>
					</div>
				</div>
			</div>
		</div>
	</form>

	<!--状态开始-->
	<div class="supplierLine">
		<a href="javascript:void(0)" id="all" onClick="statusChooses('0')"
		   <c:if test = "${conditionsMap.tabStatus == null || conditionsMap.tabStatus == 0 || conditionsMap.tabStatus == '0'}">class="select" </c:if>>全部</a>
		<a id="" href="javascript:void(0)" onClick="statusChooses('1')"
		   <c:if test = "${conditionsMap.tabStatus == '1' || conditionsMap.tabStatus == 1}">class="select" </c:if>>待本人审批</a>
		<a href="javascript:void(0)" onClick="statusChooses('2')"
		   <c:if test = "${conditionsMap.tabStatus == '2' || conditionsMap.tabStatus == 2}">class="select" </c:if>>本人已审批</a>
		<a href="javascript:void(0)" onClick="statusChooses('3')"
		   <c:if test = "${conditionsMap.tabStatus == '3' || conditionsMap.tabStatus == 3}">class="select" </c:if>>非本人审批</a>
	</div>
	<!--状态结束-->
	<div class="activitylist_paixu_right">
		查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
	</div>
	<!--状态结束-->
	<table id="contentTable" class="table mainTable activitylist_bodyer_table">

		<thead>

			<tr>
				<th width="7%">序号</th>
				<th width="7%">批次号</th>
				<th width="6%">申请时间</th>
				<th width="8%">审批发起人</th>
				<th width="7%">借款金额</th>
				<th width="7%">借款单价</th>
				<th width="7%">借款人数</th>
				<th width="6%">上一环节审批人</th>
				<th width="7%">审批状态</th>
				<th width="7%">出纳确认</th>
				<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr>
					<td colspan="16" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>
			<c:forEach items="${page.list}" var="borrowMoneyReview" varStatus="b">
				<tr>
					<td>
						<c:if test="${conditionsMap.tabStatus eq '1'}">
							<input type="checkbox" name="batchNo" value="${borrowMoneyReview.batchNo}"/>
						</c:if>
						${b.count }
					</td>
					<td class="batchNoTD">${borrowMoneyReview.batchNo }</td>
					<td class="p0">
						<div class="out-date">
							<fmt:formatDate value="${borrowMoneyReview.createdate}" pattern="yyyy-MM-dd" />
						</div>
						<div class="close-date time">
							<fmt:formatDate value="${borrowMoneyReview.createdate}" pattern="HH:mm:ss" />
						</div>
					</td>
					<td class="tc">${fns:getUserNameById(borrowMoneyReview.createby)}</td>
					<td class="tc">${fns:getCurrencyNameOrFlag(borrowMoneyReview.currencyId,0)}${borrowMoneyReview.totalBorrowMoney}</td>
					<td class="tc">${fns:getCurrencyNameOrFlag(borrowMoneyReview.currencyId,0)}<span>${borrowMoneyReview.amount}</span></td>
					<td class="tr"><span class="fbold">${borrowMoneyReview.batchPersonCount}</span></td>
					<td class="tr">${fns:getUserNameById(borrowMoneyReview.lastreviewer)}</td>
					<td>
						${fns:getChineseReviewStatus(borrowMoneyReview.status,borrowMoneyReview.currentReviewer)}
					</td>
					<td class="invoice_no tc">
						<c:if test="${borrowMoneyReview.paystatus == 'true'}">已付</c:if> 
						<c:if test="${borrowMoneyReview.paystatus == 'false'}">未付</c:if>
					</td>
					<td class="p0">
						<dl class="handle">
						<dt>
							<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
						</dt>
						<dd class="">
							<p>
								<span></span>
								<a openFlag="false" href="javascript:void(0)" onclick="getTravelerList(this,'${ctx}','2');">游客列表</a><br>
								<a href="${ctx }/visa/borrowMoney/review/exportTravelerInfo?batchNo=${borrowMoneyReview.batchNo }&busynessType=2">游客明细</a><br/>
								<c:if test="${borrowMoneyReview.status eq '1' and borrowMoneyReview.isCurReviewer}">
									<a href="${ctx}/visa/borrowMoney/review/visaBorrowMoneyBatchReviewDetail4Hqx?orderId=${borrowMoneyReview.order_id}&travelerId=${borrowMoneyReview.traveler_id}&flag=0&revid=${borrowMoneyReview.reviewId}&flowType=${borrowMoneyReview.flowType}&batchno=${borrowMoneyReview.batchNo}">审批</a><br />
								</c:if>
								<a href="${ctx}/visa/borrowMoney/review/visaBorrowMoneyBatchReviewDetail4Hqx?orderId=${borrowMoneyReview.order_id}&travelerId=${borrowMoneyReview.traveler_id}&flag=1&revid=${borrowMoneyReview.reviewId}&flowType=${borrowMoneyReview.flowType}&batchno=${borrowMoneyReview.batchNo}">查看</a>
								<!-- 处理是否显示打印按钮 -->
								<c:if test="${borrowMoneyReview.status eq '2'}">
									<a target="_blank" href="${ctx}/visa/hqx/borrowmoney/visaBorrowMoney4HQXBatchFeePrint?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&revid=${borrowMoneyReview.reviewId}&flowType=${returnReviewInfo.flowType}&batchno=${borrowMoneyReview.batchNo}&option=order&isPrintFlag=${borrowMoneyReview.printstatus?'1':'0'}">打印</a><br/>
								</c:if>
								<!-- 处于审核中的才显示   撤销按钮 -->
								<c:if test="${borrowMoneyReview.isBackReview == true}">
									<a onclick="backReview('${borrowMoneyReview.batchNo}');" >撤销</a><br />
								</c:if>
							</p>
						</dd>
						</dl></td>
				</tr>
			</c:forEach>
			<c:if test="${conditionsMap.tabStatus eq '1' and page.list.size() > 0}">
				<tr class="checkalltd">
					<td colspan='19' class="tl">
						<label>
							<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
						</label>
						<label>
							<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
						</label>
						<a onclick="batchReviewVisaBorrowMoney();">批量审批</a>
					</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<div class="page">
		<div class="pagination clearFix">${page}</div>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
