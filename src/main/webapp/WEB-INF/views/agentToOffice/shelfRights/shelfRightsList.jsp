<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>渠道-渠道商查询</title>

<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" /> -->
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet"href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="application/javascript"src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript"src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript"src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="js/trekiz.min.js" type="${ctxStatic}/text/javascript"></script>
<script>
	$(function() {
		$("#invoiceType").comboboxInquiry();
		$("#invoiceType2").comboboxInquiry();
		if('${type}'=='groupCount'){
			$("#li2").attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			$("#li3").attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			if('${big}'==0){
				$("#li1").find("i").attr("class","icon icon-arrow-down");
			}else{
				$("#li1").find("i").attr("class","icon icon-arrow-up");
			}
		}else if('${type}'=='orderCount'){
			$("#li1").attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			$("#li3").attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			$("#li2").attr("class","activitylist_paixu_moren");
			$("#li1").find("i").remove();
			if('${big}'==0){
				$("#li2").find("a").append("<i class='icon icon-arrow-down'></i>");
			}else{
				$("#li2").find("a").append("<i class='icon icon-arrow-up'></i>");
			}
		}else if('${type}'=='personCount'){
			$("#li1").attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			$("#li2").attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			$("#li3").attr("class","activitylist_paixu_moren");
			$("#li1").find("i").remove();
			if('${big}'==0){
				$("#li3").find("a").append("<i class='icon icon-arrow-down'></i>");
			}else{
				$("#li3").find("a").append("<i class='icon icon-arrow-up'></i>");
			}
		}
	});
	function sortby(type,obj){
		var a=$(obj);
		var li=a.parent();
		var big;
		if(type=='groupCount'){
			li.attr("class","activitylist_paixu_moren");
			li.next().attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			li.next().next().attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			li.next().find("i").remove();
			li.next().next().find("i").remove();
			if(a.find("i").size()==0){
				a.append("<i class='icon icon-arrow-down'></i>");
				big=0;
			}else if(a.find("i").attr('class')=='icon icon-arrow-down'){
				a.find("i").attr("class","icon icon-arrow-up");
				big=1;
			}else{
				a.find("i").attr("class","icon icon-arrow-down");
				big=0;
			}	
		}else if(type=='orderCount'){
			li.attr("class","activitylist_paixu_moren");
			li.prev().attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			li.next().attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			li.next().find("i").remove();
			li.prev().find("i").remove();
			if(a.find("i").size()==0){
				a.append("<i class='icon icon-arrow-down'></i>");
				big=0;
			}else if(a.find("i").attr('class')=='icon icon-arrow-down'){
				a.find("i").attr("class","icon icon-arrow-up");
				big=1;
			}else{
				a.find("i").attr("class","icon icon-arrow-down");
				big=0;
			}	
		}else{
			li.prev().attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			li.prev().prev().attr("class","activitylist_paixu_left_biankuang lipro.updateDate");
			li.prev().find("i").remove();
			li.prev().prev().find("i").remove();
			li.attr("class","activitylist_paixu_moren");
			if(a.find("i").size()==0){
				a.append("<i class='icon icon-arrow-down'></i>");
				big=0;
			}else if(a.find("i").attr('class')=='icon icon-arrow-down'){
				a.find("i").attr("class","icon icon-arrow-up");
				big=1;
			}else{
				a.find("i").attr("class","icon icon-arrow-down");
				big=0;
			}	
		}
		$("#type").val(type);
		$("#big").val(big);
		$("#searchForm").attr("action","${ctx}/shelfRights/list");
		$("#searchForm").submit();
	}
</script>
</head>
<body>

		<!--右侧内容部分开始-->
		<%--added for tab标签 by tlw at 20170301 start--%>
		<content tag="three_level_menu">
			<li class="active"><a href="${ctx}/sys/dict/">批发商上架权限</a></li>
		</content>
		<%--added for tab标签 by tlw at 20170301 end--%>

		<form id="searchForm" method="post" action="${ctx}/shelfRights/list">
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 wpr20">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/> 
					<input id="type" name="type" type="hidden" value="${type }"/>
					<input id="big" name="big" type="hidden" value="${big }"/>
					<label class="activitylist_team_co2_text">批发商名称：</label>
					<select name="name" id="invoiceType">
						<option value="-1" >全部</option>
						<c:forEach items="${offices }" var="list">
							<option <c:if test="${office.name==list.name }">selected="selected"</c:if>  value="${list.name }">${list.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co2_text">状态：</div>
					<select name="shelfRightsStatus" id="invoiceType2">
						<option value="-1" selected="">全部</option>
						<option value="0" <c:if test="${office.shelfRightsStatus==0 }">selected="selected"</c:if>>已启用</option>
						<option value="1" <c:if test="${office.shelfRightsStatus==1 }">selected="selected"</c:if>>已禁用</option>
					</select>
				</div>

				<div class="form_submit">
					<input value="搜索" id="seachbutton" class="btn btn-primary ydbz_x" type="submit">
				</div>
			</div>

		</form>
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li class="activitylist_paixu_moren" id="li1">
							<a onclick="sortby('groupCount',this)">有效团期总数<i class="icon icon-arrow-up"></i></a></li>
						<li class="activitylist_paixu_left_biankuang lipro.updateDate" id="li2">
							<a onclick="sortby('orderCount',this)">T1订单总数</a></li>
						<li class="activitylist_paixu_left_biankuang lipro.updateDate" id="li3">
							<a onclick="sortby('personCount',this)">T1预定总人数</a></li>
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong>${count }</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>

		<!-- 产品线路分区 -->

		<table class="activitylist_bodyer_table mainTable" id="contentTable_quauq">
			<thead>
				<tr>
					<th width="4%">序号</th>
                        <th width="8%">批发商名称</th>
                        <th width="8%">有效团期总数</th>
                        <th width="8%">T1订单总数</th>
                        <th width="8%">T1预定总人数</th>
                        <th width="8%">状态</th>
                        <th width="8%">操作</th>
				</tr>
			</thead>
			<tbody class="orderOrGroup_group_tbody">
				<c:forEach items="${page.list }" var="list" varStatus="status">
					<tr>
						<td class="tc">${status.count }</td>
						<td class="tc">${list.name }</td>
						<td class="tc">${list.groupCount }</td>
						<td class="tc">${list.T1OrderCount }</td>
						<td class="tc">${list.quauqPersonCount }</td>
						<td class="tc"><span class="started"> <c:choose>
									<c:when test="${list.shelfRightsStatus==0 }">已启用</c:when>
									<c:otherwise>已禁用</c:otherwise>
								</c:choose>
						</span></td>
						<td class="tc">
							<c:choose>
								<c:when test="${list.shelfRightsStatus==1 }">
									<a class="forbidden" onclick="forbidden(this,'${list.id}',0)">启用</a>
									<a class="display-none forbidden" onclick="forbidden(this,'${list.id}',1)">禁用</a>
								</c:when>
								<c:otherwise>
									<a class="display-none forbidden" onclick="forbidden(this,'${list.id}',0)">启用</a>
									<a class="forbidden" onclick="forbidden(this,'${list.id}',1)">禁用</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<script>
				function forbidden(obj,id,value) {
					if(value==0){
						$.jBox.confirm("是否启用该批发商？", "系统提示", function(v, h, f) {
							if(v=='ok'){
								$.ajax({
									type:"post",
									url:"${ctx}/shelfRights/updateStatus",
									data:{
										officeId:id,
										shelfRightsStatus:value
									},
									success:function(result){
										if(result){
											$(obj).hide();
											$(obj).next().show();
											$(obj).parent().prev().find("span").text("已启用");
											top.$.jBox.tip("启用成功");
										}else{
											top.$.jBox.tip("启用失败");
										}
									}
								});
							}else if(v=='cancle'){
							
							}
						});		
					}else{
						$.jBox.confirm("是否禁用该批发商？", "系统提示", function(v, h, f) {
							if(v=='ok'){
								$.ajax({
									type:"post",
									url:"${ctx}/shelfRights/updateStatus",
									data:{
										officeId:id,
										shelfRightsStatus:value
									},
									success:function(result){
										if(result){
											$(obj).hide();
											$(obj).prev().show();
											$(obj).parent().prev().find("span").text("已禁用");
											top.$.jBox.tip("禁用成功");
										}else{
											top.$.jBox.tip("禁用失败");
										}
									}
								});
							}else if(v=='cancle'){
							
							}
						});		
					}
					
				}
				function page(n,s){
					$("#pageNo").val(n);
					$("#pageSize").val(s);
					$("#searchForm").attr("action","${ctx}/shelfRights/list");
					$("#searchForm").submit();
				}
			</script>
		</table>
		<!--quauq渠道列表-->
		<div class="page">
				<div class="pagination">
						<div class="endPage">${page }</div>
				</div>
		</div>	
		<!--右侧内容部分结束-->
</body>
</html>