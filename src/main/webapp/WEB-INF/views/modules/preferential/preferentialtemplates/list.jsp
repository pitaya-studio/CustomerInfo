<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>酒店优惠模板维护</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
	$(function(){
		//搜索条件筛选
		launch();
		//产品名称文本框提示信息
		inputTips();
		//操作浮框
		operateHandler();
	});
	
	
	function show(uuid){
		window.open("${ctx}/preferentialTemplates/show/" + uuid );
	}
	function edit(uuid){
		window.open( "${ctx}/preferentialTemplates/edit/" + uuid );
	}
	function del(uuid){
		var ids = [];
		ids.push(uuid);
		v_deleteItems(ids);
	}
	function checkall(obj){
		if($(obj).attr("checked")){
			$("input[name='ids']").attr("checked",'true');
			$("input[name='allChoose']").attr("checked",'true');
		}else{
			$("input[name='ids']").removeAttr("checked");
			$("input[name='allChoose']").removeAttr("checked");
		}
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
				$.post( "${ctx}/preferentialTemplates/delete", {"uuids":ids.join(",")}, 
					function(data){
						if(data.result=="1"){
							$.jBox.prompt("删除成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
						}else{
							top.$.jBox.tip("删除失败",'warning');
						}
					}
				);
				
			} else if (v == 'cancel') {
	                
			}
		});
	}
	
	function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/preferentialTemplates/list");
			$("#searchForm").submit();
	    }
</script>
</head>
<body>
	<div>
		<!--右侧内容部分开始-->
		<div class="activitylist_bodyer_right_team_co_bgcolor">
			<form:form method="post" modelAttribute="preferentialTemplates" action="${ctx}/preferentialTemplates/list" class="form-horizontal" id="searchForm" novalidate="">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co2 pr wpr20">
						<label>模板名称：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="name" name="name" value="${preferentialTemplates.name }" type="text" flag="istips" />
						<span class="ipt-tips">仅支持模板名称的搜索</span>
					</div>
					<div class="form_submit">
						<input class="btn btn-primary" value="搜索" type="submit" />
					</div>
				</div>
			</form:form>
		</div>
		<div class="filter_btn">
			<a class="btn btn-primary" href="${ctx}/preferentialTemplates/form" target="_blank">添加</a>
		</div>
		<table class="t-type t-type100 tablemt50">
			<thead>
				<tr>
					<th width="">序号</th>
					<th width="">模板名称</th>
					<th width="">描述</th>
					<th width="">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${page.list}" varStatus="v">
					<tr>
						<td>${v.index + 1 }</td>
						<td>${entry.name}</td>
						<td>${entry.outHtml}</td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
								</dt>
								<dd class="">
									<p>
										<span></span> 
										<a href="javascript:void(0)" onclick="edit('${entry.uuid}')">修改</a> <br /> 
										<a href="javascript:void(0)" onclick="del('${entry.uuid}')">删除</a>
									</p>
								</dd>
							</dl>
						</td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
		<div class="page">
			<div class="pagination">
				<span class="activitylist_page_num"></span>
				<div class="endPage">${page}</div>
				<div style="clear:both;"></div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
