<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="wholesaler"/>
	<title>签证成本录入列表</title>

<script type="text/javascript">
	$(function(){
		//搜索条件筛选
		launch();
		//产品名称文本框提示信息
		inputTips();
		//操作浮框
		operateHandler();

		//如果展开部分有查询条件的话，默认展开，否则收起
		var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
		var searchFormselect = $("#searchForm").find("select");
		var inputRequest = false;
		var selectRequest = false;
		for(var i = 0; i < searchFormInput.length; i++) {
			if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
				inputRequest = true;
			}
		}
		for(var i = 0; i < searchFormselect.length; i++) {
			if($(searchFormselect[i]).children("option:selected").val() != "" && 
					$(searchFormselect[i]).children("option:selected").val() != "100" &&
					$(searchFormselect[i]).children("option:selected").val() != null) {
				selectRequest = true;
			}
		}
		if(inputRequest||selectRequest) {
			$('.zksx').click();
		}

		var _$orderBy = $("#orderBy").val();
		if(_$orderBy==""){
			_$orderBy="createDate DESC";
			$("#orderBy").val("createDate DESC");
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
				$(this).addClass("group_h_scroll");
			}
		});
	});

	var orderBy = null;
	function sortby(sortBy,obj){
		var temporderBy = $("#orderBy").val();
		if(temporderBy.match(sortBy)){
			sortBy = temporderBy;
			if(sortBy.match(/ASC/g)){
				sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
			}else{
				sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
			}
		}else{
			sortBy = sortBy+" DESC";
		}
		
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}

	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/cost/visa/list");
		$("#searchForm").submit();
	}
		

</script>
</head>
<body>
	<page:applyDecorator name="cost_input_head">
		<page:param name="current">visa</page:param>
	</page:applyDecorator>
	<%--<%@ include file="/WEB-INF/views/head/costInputHead.jsp"%>--%>
	<!--右侧内容部分开始-->
	<p class="main-right-topbutt"></p>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form id="searchForm" modelAttribute="visaProducts" action="${ctx}/cost/visa/list" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input class="txtPro inputTxt searchInput" id="productName" name="productName" value="${params.productName}"
                           type="text" placeholder="请输入产品名称" />
				</div>
                <a class="zksx">筛选</a>
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" value="搜索" type="submit">
				</div>
				<div class="ydxbd">
                    <span></span>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">领区：</label>
                        <div class="selectStyle">
                            <select name="collarZoning" id="collarZoning">
                                <option value="" >所有</option>
                                <c:forEach items="${visaDistrictList}" var="visaDistrict">
                                    <option value="${visaDistrict.key}" <c:if test="${params.collarZoning eq visaDistrict.key}">selected</c:if>>
                                        <c:out value="${visaDistrict.value}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text" style="width: 100px;">签证国家/地区：</label>
                        <div class="selectStyle">
                            <select  name="sysCountryId" id="sysCountryId">
                                <option value="" >所有</option>
                                <c:forEach items="${visaCountryList}" var="visaCountry">
                                    <option value="${visaCountry.id}" <c:if test="${params.sysCountryId eq visaCountry.id}">selected</c:if>>
                                        <c:out value="${visaCountry.countryName_cn}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">签证类型：</label>
                        <div class="selectStyle">
                            <select  name="visaType" id="visaType">
                                <option value="" >所有</option>
                                <c:forEach items="${visaTypeList}" var="visaType">
                                    <option value="${visaType.key}"  <c:if test="${params.visaType eq visaType.key}">selected</c:if>>
                                        <c:out value="${visaType.value}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
					</div>
					<c:if test="${DHJQ || TMYT}">
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">付款提交：</label>
                            <div class="selectStyle">
                                <select name="commitType">
                                   <option value="">请选择</option>
                                   <option value="1" <c:if test="${params.commitType eq '1'}">selected="selected"</c:if>>未提交</option>
                                   <option value="2" <c:if test="${params.commitType eq '2'}">selected="selected"</c:if>>已提交</option>
                                </select>
                            </div>
						</div>
					</c:if>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">计调：</label>
						<input id="operator" name="operator" class="inputTxt" value="${params.operator }"/>
					</div>
					<%-- 540 增加驳回标识筛选项 王洋 2017.3.22 --%>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">驳回标识：</label>
						<div class="selectStyle">
							<select name="isReject">
								<option value="" <c:if test="${params.isReject eq ''}">selected="selected"</c:if>>全部</option>
								<option value="1" <c:if test="${params.isReject eq '1'}">selected="selected"</c:if>>被驳回</option>
							</select>
						</div>
					</div>
                </div>
                <div class="kong"></div>
            </div>
        </form:form>
        <c:if test="${fn:length(page.list) ne 0}">
            <div class="activitylist_bodyer_right_team_co_paixu">
                <div class="activitylist_paixu">
                    <div class="activitylist_paixu_left">
                        <ul>
                            <li class="activitylist_paixu_left_biankuang licreateDate"><a onclick="sortby('createDate',this)">创建时间</a></li>
                            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
                            <li class="activitylist_paixu_left_biankuang livisaPay"><a onClick="sortby('visaPay',this)">应收价</a></li>
                        </ul>
                    </div>
                    <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                    <div class="kong"></div>
                </div>
            </div>
        </c:if>

        <table id="contentTable" class="table mainTable activitylist_bodyer_table">
            <thead>
                <tr>
                    <th width="3%">序号</th>
                    <th width="6%">团号</th>
                    <th width="12%">产品名称</th>
                    <th width="6%">签证国家</th>
                    <th width="8%">签证类型</th>
                    <th width="6%">签证领区</th>
                    <th width="8%">计调</th>
                    <th width="6%">成本价格</th>
                    <th width="6%">应收价格</th>
                    <th width="9%">发布时间</th>
                    <!--  <th width="6%">审核状态</th> -->
                    <c:if test="${TMYT}">
                        <th width="7%">预计总成本</th>
                        <th width="7%">实际总成本</th>
                        <th width="7%">成本差额</th>
                    </c:if>
                    <th width="12%">操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${page.list}" var="visaProduct" varStatus="s">
                    <tr id="parent1">
                        <td class="tc">${s.count}</td>
                        <c:choose>
                        	<c:when test="${visaProduct.rejectCount > 0 }">
                        		<%-- 540需求，当存在被驳回的审批数据，团号显示驳回标识 王洋 2017.3.22 --%>
                        		<td class="tc reject_style">${visaProduct.groupCode}<span></span></td>
                        	</c:when>
                        	<c:otherwise>
                        		<td class="tc">${visaProduct.groupCode}</td>
                        	</c:otherwise>
                        </c:choose>
                        <td class="activity_name_td" style="text-align: center">
                            <a href="javascript:void(0)"  onclick="javascript:window.open('${ctx}/visa/preorder/visaProductsDetail/${visaProduct.id}')">${visaProduct.productName}</a>
                        </td>
                        <td class="tc">
                            <c:forEach items="${visaCountryList}" var="country">
                                <c:if test="${country.id eq visaProduct.sysCountryId}">
                                    ${country.countryName_cn}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td class="tc">
                            <c:forEach items="${visaTypeList}" var="visas">
                                <c:if test="${visas.key eq visaProduct.visaType}">
                                    ${visas.value}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td class="tc">
                            <c:if test="${not empty visaProduct.collarZoning }">
                                ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                            </c:if>
                        </td>
                        <td class="tc">${visaProduct.operator}</td>
                        <td class="tr">
                            <c:forEach items="${curlist}" var="currency">
                                <c:if test="${currency.id eq visaProduct.currencyId}">
                                    ${currency.currencyMark}
                                </c:if>
                            </c:forEach><font color="red"><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPrice}" /></font> </span>起
                        </td>
                        <td class="tr">
                            <c:forEach items="${curlist}" var="currency">
                                <c:if test="${currency.id eq visaProduct.currencyId}">
                                    ${currency.currencyMark}
                                </c:if>
                            </c:forEach><font color="green"><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPay}" /></font> </span>起
                        </td>
                        <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${visaProduct.updateDate }"/></td>

                        <c:if test="${TMYT}">
                            <td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.budgetTotal}"/></td>
                            <td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.actualTotal}"/></td>
                            <td class="tr">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.actualTotal - visaProduct.budgetTotal}"/></td>
                        </c:if>
                        <td class="p0">
                            <dl class="handle">
                                <a target="_blank"  href="${ctx}/costReview/visa/visaCostDetail/${visaProduct.id}">成本录入</a>
                                <a href="${ctx }/cost/manager/forcastList/${visaProduct.id}/6" target="_blank">预报单</a>
                                <a href="${ctx }/cost/manager/settleList/${visaProduct.id}/6" target="_blank">结算单</a>
                            </dl>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="page">
            <div class="pagination">
                <div class="endPage">
                    ${page}
                </div>
                <div style="clear:both;"></div>
            </div>
        </div>
    </div>
</body>
</html>