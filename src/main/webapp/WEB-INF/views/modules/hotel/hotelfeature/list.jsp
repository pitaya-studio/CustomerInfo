<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-酒店特色管理</title>
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
					var id = $(this).find("input[name=id]").val();
					var sort = $(this).find(".maintain_sort").val();
					var wholesalerId = $(this).find("input[name=wholesalerId]").val();
					idAndSorts.push(id+","+sort+","+wholesalerId);
				});
				$.post( "${ctx}/hotelFeature/updateOrder", {"idAndSorts":idAndSorts.join(";"),"wholesalerId":"${wholesalerId}"}, 
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
	window.open(  "${ctx}/hotelFeature/show/" + uuid +"") ;
}
function edit(uuid,wholesalerId){
	window.open( "${ctx}/hotelFeature/edit/" + uuid +"?wholesalerId="+wholesalerId) ;
}
function del(uuid,wholesalerId){

	
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			$.post( "${ctx}/hotelFeature/delete", {"uuid":uuid,"wholesalerId":wholesalerId}, 
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
	$("#searchForm").attr("action"," ${ctx}/hotelFeature/list");
	$("#searchForm").submit();
}
</script>	
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">${type}</page:param>
</page:applyDecorator>
	<div>
<!--右侧内容部分开始-->
   	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form id="searchForm" action=" ${ctx}/hotelFeature/list" method="post">

		</form>
	</div>
	<div class="filter_btn">
		<%--<a class="btn btn-primary" href=" ${ctx}/hotelFeature/form" target="_blank">添加酒店特色</a>--%>
		<input class="btn btn-primary ydbz_x" onclick="window.open('${ctx}/hotelFeature/form')" value="添加酒店特色" type="button">
	</div>
<table class="activitylist_bodyer_table">
                <thead>
                    <tr>
                    	
                    	<th width="10%">排序</th>
						<th width="">酒店特色</th>
						<th width="">描述</th>
                        <th width="15%">操作</th>
                    </tr>
                </thead>
                <tbody>
                	
		            <c:forEach var="entry" items="${list}" varStatus="v">
			          <tr>
						<td class="tc sort">
						<input name="id" type="hidden" value="<c:out value="${entry.id}" />"/>
						<input name="wholesalerId" type="hidden" value="<c:out value="${entry.wholesalerId}" />"/>
						<input type="text" value="${entry.sort}" class="maintain_sort" maxlength="4"  onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
						</td>
						<td>${entry.name}</td>
						<td>${entry.description}</td>
						<td class="tda tc">
                            <c:if test="${entry.wholesalerId==wholesalerId}">
	                            <a href="javascript:void(0)" onclick="edit('${entry.uuid}','${entry.wholesalerId}')">修改</a>
	                            <a href="javascript:void(0)" onclick="del('${entry.uuid}','${entry.wholesalerId}')">删除</a>
                            </c:if>
                            <a href="javascript:void(0)" onclick="show('${entry.uuid}')">详情</a>
                        </td>
			            
			          </tr>
		           </c:forEach>
		           
		       </tbody>
   </table>
	<div class="page"></div>
	<div class="pagination">
   		 <dd>
             <%--<a onclick="updateOrder(this)" class="ydbz_x">修改排序</a>--%>
			 <input class="btn btn-primary ydbz_x" onclick="updateOrder(this)" value="修改排序" type="button">

		 </dd>
		
	</div>
	<br/>

            <!--右侧内容部分结束-->
</div>
</body>
</html>
<script type="text/javascript">


</script>
