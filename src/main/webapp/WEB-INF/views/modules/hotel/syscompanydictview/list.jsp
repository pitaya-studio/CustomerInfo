<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${titleName}维护</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	
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

//修改排序的操作函数
function orderSubmitCallBack(){
	if($(".sort").length>0){
		$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在修改排序...", 'loading');
				
				var idAndSorts = [];
				
				$(".sort").each(function(){
					if($(this).find(".maintain_sort").val() != undefined) {
						var id = $(this).find("input[name=id]").val();
						var sort = $(this).find(".maintain_sort").val();
						var companyId = $(this).find("input[name=companyId]").val();
						idAndSorts.push(id+","+sort+","+companyId);
					}
				});
				$.post( "${ctx}/sysCompanyDictView/updateOrder", {"idAndSorts":idAndSorts.join(";"),"companyId":"${companyId}"}, 
					function(data){
						if(data.result=="1"){
							if(data.sortException!=null&&data.sortException!=""){
								top.$.jBox.tip("修改排序成功!序号"+data.sortException+"不在修改权限内。");
								//$("#searchForm").submit();
							}else{
								top.$.jBox.tip("修改排序成功!");
								$("#searchForm").submit();
							}
						}else{
							top.$.jBox.tip(data.message,'warning');
						}
					}
				);
				
			} else if (v == 'cancel') {
	             $("#searchForm").submit();
			}
		});
	}
}


function show(uuid){
	window.open(  "${ctx}/sysCompanyDictView/show/" + uuid +"?type=${type}") ;
}
function edit(uuid,companyId){
	window.open( "${ctx}/sysCompanyDictView/edit/" + uuid +"?type=${type}&companyId="+companyId) ;
}
function del(uuid,companyId){

	
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			$.post( "${ctx}/sysCompanyDictView/delete", {"uuid":uuid,"companyId":companyId}, 
				function(data){
					if(data.result=="1"){
						top.$.jBox.tip('删除成功!');
						$("#searchForm").submit();
					}else{
						top.$.jBox.tip(data.message,'warning');
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
	$("#searchForm").attr("action"," ${ctx}/sysCompanyDictView/list");
	$("#searchForm").submit();
}

//bug17517 由于UG_V2将按钮统一为input[type=button],所以将text()改为val(),为不影响其他页面功能，先在该jsp添加方法
function updateOrder(obj){
	var txt=$(obj).val();
	if(txt=="修改排序"){
		$(obj).val("提交");
		$("input[class='maintain_sort']").removeAttr("disabled");
	}else{
		$(obj).val("修改排序");
		$("input[class='maintain_sort']").attr("disabled","disabled");
		//提交修改排序后的回调函数
		orderSubmitCallBack();
	}
}
</script>
<style type="text/css">
	input[type="text"][disabled].readonly_sort{width:30px; border-color:#fff; background:none; box-shadow:none; cursor:text}
</style> 
</head>
<body>
<c:if test="${ empty source }">
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">${param.type}</page:param>
</page:applyDecorator>
</c:if>

	<div>
<!--右侧内容部分开始-->
   <div class="activitylist_bodyer_right_team_co_bgcolor">
	   <form id="searchForm" action=" ${ctx}/sysCompanyDictView/list" method="post">
			<input id="type" name="type" type="hidden" value="<c:out value="${type}" />"/>
	   </form>
   </div>
   <div class="filter_btn">
	<%--<a class="btn btn-primary" href=" ${ctx}/sysCompanyDictView/form?type=${type}" target="_blank">添加${titleName}</a>--%>
		<input class="btn btn-primary ydbz_x" value="添加${titleName}" type="submit"
			   onclick='window.open("${ctx}/sysCompanyDictView/form?type=${type}")'
			   style="margin-bottom: 10px;">
	</div>
<table class="activitylist_bodyer_table mainTable">
                <thead>
                    <tr>
                    	
                    	<th width="10%">排序</th>
						<th width="">${titleName}</th>
						<th width="">描述</th>
                        <th width="15%">操作</th>
                    </tr>
                </thead>
                <tbody>
                	
		            <c:forEach var="entry" items="${list}" varStatus="v">
			          <tr>
			          	<c:if test="${not empty entry.id}">
							<td class="tc sort">
							<input name="id" type="hidden" value="<c:out value="${entry.id}" />"/>
							<input name="companyId" type="hidden" value="<c:out value="${entry.companyId}" />"/>
							<input type="text" value="${entry.sort}" <c:choose><c:when test="${entry.companyId==companyId}">class="maintain_sort" maxlength="4" style="width:30px;" </c:when><c:otherwise>class="maintain_sort"</c:otherwise></c:choose> onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
							</td>
							<td>${entry.label}</td>
							<td>${entry.description}</td>
							<td class="tda tc">
	                            <c:if test="${entry.companyId==companyId}">
		                            <a href="javascript:void(0)" onclick="edit('${entry.uuid}','${entry.companyId}')">修改</a>
		                            <a href="javascript:void(0)" onclick="del('${entry.uuid}','${entry.companyId}')">删除</a>
	                            </c:if>
	                            <a href="javascript:void(0)" onclick="show('${entry.uuid}')">详情</a>
	                        </td>
			           	</c:if>
			          </tr>
		           </c:forEach>
		           
		       </tbody>
   </table>
	<div class="page"></div>
	<div class="pagination">
   		 <dd>
			 <input class="btn btn-primary ydbz_x" value="修改排序" type="submit" onclick="updateOrder(this)">


		 </dd>
		
	</div>
	<br/>

            <!--右侧内容部分结束-->
</div>
</body>
</html>
<script type="text/javascript">


</script>
