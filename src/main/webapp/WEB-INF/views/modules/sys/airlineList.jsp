<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-航空公司</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	inputTips();
	//操作浮框
	operateHandler();

});

//删除航空公司
function updateAirlineStatus(mess,area,airlineCode) {
	$.jBox.confirm(mess, "提示", function(v, h, f) {
		if (v == 'ok') {
			$.post("${ctx}/sys/airline/updateAirlineStatus", {
				"v_area" : area,
				"v_airlineCode_old" : airlineCode
			}, function(data) {
				if (data.ret == "success") {
					top.$.jBox.tip(data.msg, 'success', {
						closed : window.location="${ctx}/sys/airline/list/"+area
					});
				} else {
					top.$.jBox.tip(data.msg, 'warning');
				}
			});
		}
	});
}
</script>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">airline</page:param>
</page:applyDecorator>
<form:form id="selForm" action="${ctx}/sys/airline/saveDispStatus" method="post" >
	<input id="area" name="area" type="hidden" value="${area}"/>
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="airlineIds" type="hidden" name="airlineIds" value="${airlineIds}"/>
			<div class="activitylist_bodyer_right_team_co">
				<!--右侧内容部分开始-->
				<%--modified for UG_V2 调整搜索框及搜索按钮样式 by tlw at 20170302 start--%>
				<div class="activitylist_bodyer_right_team_co2">
					  <input class="searchInput radius4"id="airlineNameKeyword" name="airlineNameKeyword" type="text" value="${airlineNameKeyword}" placeholder="输入航空公司名称、二字码、航班号"/>
				</div>
				<div class="form_submit">
					<input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="searchAirlineInfo()" value="搜索">
				</div>
				<%--modified for UG_V2 搜索框及搜索按钮样式 by tlw at 20170302 end--%>

				<%--modified for UG_V2 改变新增按钮位置 by tlw at 20170302 start--%>
				<p class="main-right-topbutt">
					<a class="primary" href="javascript:void(0);" onclick="forwardFormPage();">新增航空公司</a>
				</p>
					<%--modified for UG_V2 改变新增按钮位置 by tlw at 20170302 end--%>
			</div>
				<!--查询结果筛选条件排序开始-->
					<%--modified for UG_V2 替换页签样式与线上统一 更改查询结果位置 by tlw at 20170302 start--%>
				<div class="supplierLine">
					<a onclick="changeTab('1')" href="javascript:void(0)" id="groupLabel" <c:if test="${area eq '1'}">class="select"</c:if>>国内</a>
					<a onclick="changeTab('2')" href="javascript:void(0)" id="orderLabel" <c:if test="${area eq '2'}">class="select"</c:if>>国外</a>
					<div class="filter_num">查询结果<strong>${page.count}</strong>条</div>
				</div>
					<%--modified for UG_V2 替换页签样式与线上统一 更改查询结果位置 by tlw at 20170302 end--%>
				<!--查询结果筛选条件排序结束-->	
				<table class="activitylist_bodyer_table mainTable" id="contentTable">
					<thead>
						<tr>
							<th width="10%">序号</th>
			                <th width="15%">国家</th>
			                <th width="20%">航空公司</th>
			                <th width="10%">航空公司二字码</th>
			                <th width="15%">航班号/出发到达时间</th>
			                <th width="10%">舱位等级</th>
			                <th width="10%">舱位</th>
			                <th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="keyword" varStatus="s">
							<tr>
								<td class="tc">
									<span class="table_borderLeftN">
										<input type="checkbox" name="activityId" data-group="tq" value="${keyword[6]}" <c:if test="${keyword[5] eq '1'}">checked</c:if> />
					                </span>
					            	${s.count}
					            </td>
								<td class="tc">
									<trekiz:autoId2Name4Table tableName="sys_area" sourceColumnName="id" srcColumnName="name" value="${keyword[0]}" />
								</td>
								<td class="tc">${keyword[1]}</td>
								<td class="tc">${keyword[2]}</td>
								<td class="tc">${keyword[7]}<br/>
								${keyword[8]}
								<c:if test="${keyword[9]!=null && keyword[9]!=''}">---</c:if>
								${keyword[9]}
								</td>
								<td class="tc">
								<c:if test="${keyword[3]!=null && keyword[3]!='' }">
									${fns:getDictLabel(keyword[3],"spaceGrade_Type",keyword[3])}
								</c:if>
								</td>
								<td class="tc">
								<c:if test="${keyword[4]!=null && keyword[4]!='' }">
									${fns:getDictLabel(keyword[4],"airspace_Type",keyword[4])}
								</c:if>
								</td>
								<td class="p0">
									<dl class="handle">
				                  		<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
				                  		<dd class="">
				                    	<p> <span></span> 
					                    	<a href="${ctx}/sys/airline/modify/${keyword[2]}?area=${area}" target="_blank">修改</a> 
					                    	<a href="javascript:void(0)" onclick="updateAirlineStatus('确定要删除该航空公司吗','${area}','${keyword[2]}')">删除</a>
					                    	<a href="${ctx}/sys/airline/showDetail/${keyword[2]}?area=${area}" target="_blank">详情</a> 
				                    	</p>
				                  		</dd>
				                	</dl>
				                </td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<%--modified for UG_V2 调整空信息位置 by tlw at20170302--%>
				<c:if test="${page.count eq 0 }">
					<div class="wtjqw">无航空公司信息</div>
				</c:if>
				<%--modified for UG_V2 调整空信息位置 by tlw at20170302--%>
				<%--modified for UG_V2 调整保存按钮样式 调整html结构 by tlw 20170302--%>
				<div class="page">
					<div class="pagination">
						<dl>
							<dt>
								<input type="checkbox" data-group="tq" class="chkAll" />全选</dt>
							</dt>
							<dd><tags:message content="${message}" />
								<input class="btn ydbz_x" type="button" value="保存" target="_blank" id="piliang_o_" onclick="saveDispStatus()">
							</dd>
						</dl>
						<%--modified for UG_V2 调整分页位置 与全选在同一行 by tlw at 20170302 start --%>
						<c:if test="${page.count ne 0 }">
							${page}
						</c:if>
						<%--modified for UG_V2 调整分页位置 与全选在同一行 by tlw at 20170302 end --%>
					</div>
				</div>
				<%--modified for UG_V2 调整保存按钮样式 调整html结构 by tlw 20170302--%>

	<%--<div class="batch_del2 mar_batch_top15">--%>
                        <%--<dl>--%>
                            <%--<dt><input type="checkbox" data-group="tq" class="chkAll" />全选</dt>--%>
                            <%--<dd>--%>
                                <%--<tags:message content="${message}" />--%>
                                <%--<a onclick="saveDispStatus()">保存</a>--%>
                            <%--</dd>--%>
                        <%--</dl>--%>

                    <%--</div>--%>

				<!--右侧内容部分结束-->
</form:form>
<script type="text/javascript">
$(document).ready(function() {
    $("div[style]").each(function() {
        if ($(this).css("clear") == "both") {
        	$(this).removeAttr("style");
        }
    });
    
    inputTips();
});

function changeTab(area){
	window.location.href = "${ctx}/sys/airline/list/" + area;
}

//产品名称获得焦点显示隐藏
function inputTips(){
	$("#airlineNameKeyword").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	$("#airlineNameKeyword").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}

function forwardFormPage(){
	window.location.href = "${ctx}/sys/airline/form/1";
}

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#selForm").attr("action","${ctx}/sys/airline/list/${area}");
	$("#selForm").submit();
}

function saveDispStatus(){
	$("#selForm").attr("action","${ctx}/sys/airline/saveDispStatus");
	$("#selForm").submit();
}

function searchAirlineInfo(){
	$("#selForm").attr("action","${ctx}/sys/airline/list/${area}");
	$("#selForm").submit();
}
</script>
</body>
</html>
