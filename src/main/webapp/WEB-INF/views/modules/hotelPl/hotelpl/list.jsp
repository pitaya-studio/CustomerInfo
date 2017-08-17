<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>酒店价单列表</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--订单模块的脚本-->
	<script type="text/javascript">
		$(function() {
			//搜索条件筛选
			launch();
			//产品名称文本框提示信息
			inputTips();
			//操作浮框
			operateHandler();
			
			//国家联想输入
			initSuggest({});
			
			$("#checkAll").click(function(){
				if($(this).attr("checked")) {
					$("[name='ids']").attr("checked",true);//全选
				} else {
					$("[name='ids']").removeAttr("checked");
				}
			});
			
			$("[name='ids']").click(function(){
				var allChecked = true;
				$("[name='ids']").each(function(){
					if(! $(this).attr("checked")) {
						allChecked = false;
						return false;
					}
				});
				
				$("#checkAll").attr("checked",allChecked);
			});
			
			var selectRequest = false;
			if(("${hotelPlQuery.country}" !="") || ("${hotelPlQuery.islandUuid}" != "") || ("${hotelPlQuery.hotelGroup}" != "") || ("${hotelPlQuery.supplierInfoId}" != "") || ("${hotelPlQuery.purchaseType}" != "") || ("${hotelPlQuery.startCreateDate}" != "") || ("${hotelPlQuery.endCreateDate}" != "")) {
				selectRequest = true;
			}
			
			if(selectRequest) {
				$('.zksx').click();
			}
			
			//加载岛屿信息
			if('${hotelPlQuery.country}' != '') {
			    $("#islandUuid").empty();
			    $("#countrynouse").val('${hotelPlQuery.country}');
				getAjaxSelect("island", $("#countrynouse"));
			}
			if('${hotelPlQuery.islandUuid}' != '') {
			    $("#islandnouse").val('${hotelPlQuery.islandUuid}');
				getAjaxSelect("hotel", $("#islandnouse"));
			}
			
			//加载排序样式
			updateOrderStyle($("#orderBy").val());
			
		});
			
		//级联查询
        function getAjaxSelect(type,obj){
        	$.ajax({
        		type: "POST",
        	   	url: "${ctx}/hotelControl/ajaxCheck",
        	   	async: false,
        	   	data: {
        				"type":type,
        				"uuid":$(obj).val()
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		if(type == "island"){
        	   			$("#islandUuid").empty();
        	   			$("#islandUuid").append("<option value=''>不限</option>");
        	   			$("#hotelUuid").empty();
        		   		$("#hotelUuid").append("<option value=''>不限</option>");
        	   		} else if(type == "hotel"){
        	   			$("#hotelUuid").empty();
        		   		$("#hotelUuid").append("<option value=''>不限</option>");
        	   		}else{
        		   		$("#"+type).empty();
        		   		$("#"+type).append("<option value=''></option>");
        	   		}
        	   		if(data){
        	   			if(type=="hotel"){ 
        		   			$.each(data.hotelList,function(i,n){
        		   			     $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
        		   			});
        		   			
        		   			if('${hotelPlQuery.hotelUuid}') {
        	   					$("#hotelUuid").val("${hotelPlQuery.hotelUuid}");
        	   				}
        		   		
        	   			}else if(type=="island"){
        	   				$.each(data.islandList,function(i,n){
        	   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
        	   				});
        	   				
        	   				if('${hotelPlQuery.islandUuid}') {
        	   					$("#islandUuid").val("${hotelPlQuery.islandUuid}");
        	   				}
        	   				
        	   			}
        	   		}
	   				$("#islandUuid").attr("title",$("#islandUuid").find("option:selected").text());
	   				$("#hotelUuid").attr("title",$("#hotelUuid").find("option:selected").text());
        	   	}
        	});
        }
        function changeHoteltitle(){
        	$("#hotelUuid").attr("title",$("#hotelUuid").find("option:selected").text());
        	$("#supplierInfoId").attr("title",$("#supplierInfoId").find("option:selected").text());
        }
        function hotelPlOrderBy(orderBy){
        	if($("#orderBy").val() == orderBy) {
        		if($("#ascOrDesc").val() == "asc") {
        			$("#ascOrDesc").val("desc");
        		} else {
        			$("#ascOrDesc").val("asc");
        		}
        		
        	} else {
        		$("#orderBy").val(orderBy);
        		$("#ascOrDesc").val("desc");
        	}
        	
        	//修改排序样式
        	//updateOrderStyle(orderBy);
        	
        	$("#searchForm").submit();
        }
        
        //修改排序显示样式
        //UG_V2 统一排序样式
        function updateOrderStyle(orderBy) {

            if ($("#orderBy").length > 0) {
                $(".activitylist_paixu_left li").each(function () {
                    if (!$(this).hasClass(orderBy)) {
                    } else {
                        console.log(orderBy.toUpperCase())
                        var _$orderBy = $("#ascOrDesc").val().toUpperCase() == "ASC" && $("#ascOrDesc").val() ? "up" : "down";
                        var arrow = "<i class=\"icon icon-arrow-" + _$orderBy + "\"></i>"
                        $(this).find("a").eq(0).html($(this).find("a").eq(0).html() + arrow);
                        $(this).addClass("activitylist_paixu_moren").removeClass("activitylist_paixu_left_biankuang");
                    }
                });
            }

        	
//        	if(orderBy == "createDate") {
//        		if($("#ascOrDesc").val() == "asc") {
//        			$("#createDate_i_sort").attr("class", "i_sort i_sort_up");
//        		} else {
//        			$("#createDate_i_sort").attr("class", "i_sort i_sort_down");
//        		}
//        		$("#updateDate_i_sort").attr("class", "i_sort");
//        	} else {
//        		$("#createDate_i_sort").attr("class", "i_sort");
//
//        		if($("#ascOrDesc").val() == "asc") {
//        			$("#updateDate_i_sort").attr("class", "i_sort i_sort_up");
//        		} else {
//        			$("#updateDate_i_sort").attr("class", "i_sort i_sort_down");
//        		}
//        	}
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/hotelPl/list");
			$("#searchForm").submit();
		}

	    function del(uuid){
			var ids = [];
			ids.push(uuid);
			v_deleteItems(ids);
		}
		function alldel(){
			if($("[name=ids]:checkbox:checked").length>0){
				var ids = [];
				$("[name=ids]:checkbox:checked").each(function(){ids.push($(this).val())});
				v_deleteItems(ids);
			}else{
				top.$.jBox.tip('请选择后进行删除操作','warning');
			}
		}

function v_deleteItems(ids){
	
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			$.post( "${ctx}/hotelPl/delete", {"ids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
						top.$.jBox.tip('删除成功','success');
						$("#searchForm").submit();
					}else{
						top.$.jBox.tip('删除失败','warning');
					}
				}
			);
			
		} else if (v == 'cancel') {
                
		}
	});
	
}
	</script>
</head>
<body>
	<div>
<!--右侧内容部分开始2-->
		<%--<div class="mod_nav">产品 &gt; 酒店产品 &gt; 酒店价单列表</div>--%>
		<div class="activitylist_bodyer_right_team_co_bgcolor">
			<form:form method="post" modelAttribute="hotelPlQuery" action="${ctx}/hotelPl/list" class="form-horizontal" id="searchForm" novalidate="">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
				<form:hidden path="orderBy" />
				<form:hidden path="ascOrDesc" />
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co2 pr">
						<form:input path="name" class="inputTxt searchInput inputTxtlong" id="orderNum" placeholder="请输入价单名称" value="${hotelPlQuery.name }"/>
					</div>
					<a class="zksx" id="zksx">筛选</a>
					<div class="form_submit">
						<input type="submit" value="搜索" class="btn btn-primary ydbz_x" />
					</div>
					<p class="main-right-topbutt">
						<a class="primary" href="${ctx}/hotelPl/toSaveHotelPl" target="_blank">新增酒店价单</a>
					</p>
					<div class="ydxbd" id="ydxbd2" style="display:none;">
						<span></span>
						<div class="activitylist_bodyer_right_team_co1" >
							<label class="activitylist_team_co3_text">国家：</label>
				      		<trekiz:suggest name="country" defaultValue="${hotelPlQuery.country}" callback="getAjaxSelect('island',$('#country'))"  displayValue="${countryName }" ajaxUrl="${ctx}/geography/getAllConListAjax" />
				      		<input id="countrynouse" type="hidden" />
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">岛屿名称：</div>
							<div class="selectStyle">
								<select name="islandUuid" id="islandUuid" onchange="getAjaxSelect('hotel',this);">
								</select>
							</div>
						 	<input id="islandnouse" type="hidden" />
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">酒店名称：</label>
							<div class="selectStyle">
								<select name="hotelUuid" id="hotelUuid" onchange="changeHoteltitle();">
									<option value="">不限</option>
								</select>
							</div>
							<input id="hotelnouse" type="hidden" />
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text jbox-width100">酒店集团：</label>
							<div class="selectStyle">
								<trekiz:defineDict name="hotelGroup" input="select" type="hotel_group" defaultValue="${hotelPlQuery.hotelGroup}"  />
							</div>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">地接供应商：</label>
							<div class="selectStyle">
								<form:select path="supplierInfoId" onchange="changeHoteltitle();">
									<option value="">不限</option>
									<c:forEach items="${supplierInfos }" var="supplier">
										<option value="${supplier.id }" <c:if test="${hotelPlQuery.supplierInfoId == supplier.id}">selected="selected"</c:if>>${supplier.supplierName }</option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">采购类型：</label>
							<div class="selectStyle">
								<form:select path="purchaseType">
									<option value="">不限</option>
									<form:option value="0">内采</form:option>
									<form:option value="1">外采</form:option>
								</form:select>
							</div>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">价单创建日期：</label>
							<input name="startCreateDateStr" id="startCreateDate" value='<fmt:formatDate value="${hotelPlQuery.startCreateDate }" pattern="yyyy-MM-dd" />' onclick="WdatePicker()" class="dateinput required " type="text"> 
							<span>至</span>
							<input name="endCreateDateStr" id="endCreateDate" value="<fmt:formatDate value="${hotelPlQuery.endCreateDate }" pattern="yyyy-MM-dd" />" onclick="WdatePicker()" class="dateinput required " type="text">
						</div>
					</div>
					<div class="kong"></div>
				</div>
			</form:form>
			<!--查询结果筛选条件排序开始-->
			<%--<div class="filterbox">--%>

				<%--<div class="filter_sort">--%>
					<%--<a href="javascript:void(0);" onclick="hotelPlOrderBy('createDate')">价单创建时间<i class="i_sort i_sort_down" id="createDate_i_sort"></i></a>--%>
					<%--<a href="javascript:void(0);" onclick="hotelPlOrderBy('updateDate')">价单修改时间<i class="i_sort" id="updateDate_i_sort"></i></a>--%>
				<%--</div>--%>
			<%--</div>--%>

			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>

							<li class="activitylist_paixu_left_biankuang createDate">
								<a onclick="hotelPlOrderBy('createDate')">价单创建时间</a>
							</li>
							<li class="activitylist_paixu_left_biankuang updateDate">
								<a onclick="hotelPlOrderBy('updateDate')">价单修改时间</a>
							</li>
						</ul>
					</div>
					<div class="activitylist_paixu_right">

						查询结果&nbsp;${page.count }&nbsp;条
					</div>
					<div class="kong"></div>
				</div>
			</div>
			<!--查询结果筛选条件排序结束-->
			<table id="contentTable"
				class="table activitylist_bodyer_table sea_rua_table mainTable">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="12%">价单创建时间</th>
						<th width="11%">价单名称</th>
						<th width="12%">国家</th>
						<th width="10%">岛屿名称</th>
						<th width="10%">酒店集团</th>
						<th width="10%">酒店名称</th>
						<th width="10%">地接供应商</th>
						<th width="8%">采购类型</th>
                        <th width="7%">税金算法</th>
						<th width="5%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list }" var="hotelPl" varStatus="status">
						<tr>
							<td class="tc"  >
								<span class="table_borderLeftN"><input type="checkbox" value="${hotelPl.uuid }" name="ids"/>${status.count}</span>
							</td>
							<td class="tc"><fmt:formatDate value="${hotelPl.createDate }" pattern="yyyy-MM-dd HH:mm" /></td>
							<td class="tc">${hotelPl.name }</td>
							<td class="tc"><trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${hotelPl.country }'/></td>
							<td class="tc"><trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${hotelPl.islandUuid }"/></td>
							<td class="tc">${hotelPl.hotelGroup }</td>
							<td class="tc"><trekiz:autoId2Name4Class classzName="Hotel" sourceProName="uuid" srcProName="nameCn" value="${hotelPl.hotelUuid }"/></td>
							<td class="tc"><trekiz:autoId2Name4Table tableName='supplier_info' sourceColumnName='id' srcColumnName='supplierName' value='${hotelPl.supplierInfoId }'/></td>
							<td class="tr"><c:choose><c:when test="${hotelPl.purchaseType == 0}">内采</c:when><c:when test="${hotelPl.purchaseType == 1}">外采</c:when></c:choose></td>
							<td class="tc"><c:choose><c:when test="${hotelPl.taxArithmetic == 1}">连乘</c:when><c:when test="${hotelPl.taxArithmetic == 2}">分乘</c:when></c:choose></td>
							<td class="p0">
								<dl class="handle">
									<dt>
										<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
									</dt>
									<dd>
										<p>
											<span></span> 
											<a href="${ctx}/hotelPl/toUpdateHotelPl/${hotelPl.uuid}" target="_blank">修改</a>
											<a href="${ctx}/hotelPl/show/${hotelPl.uuid}" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="del('${hotelPl.uuid}')">删除</a>
										</p>
									</dd>
								</dl>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="batch_del2 mar_batch_top15">
				<dl>
					<dt>
						<input type="checkbox" data-group="tq" class="chkAll" id="checkAll"/>全选
					</dt>
					<dd>
						<%--<a onclick="alldel()">批量删除</a>--%>
                        <input onclick="alldel()" type="button" value="批量删除" class="btn ydbz_x">
					</dd>
				</dl>

			</div>
			<!--分页部分开始-->
			<div class="page_ou">
				<div class="paginations">
					${page }
					<div style="clear:both;"></div>
				</div>
			</div>
			<!--分页部分结束-->
			<!--团号列表-->
		</div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
