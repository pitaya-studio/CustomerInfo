<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>CruiseshipStock维护</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
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
	
	//取消一个checkbox 就要联动取消 全选的选中状态
	$("input[name='ids']").click(function(){
		if($(this).attr("checked")){
			
		}else{
			$("input[name='allChoose']").removeAttr("checked");
		}
	});
});


function show(uuid){
	window.open("${ctx}/cruiseshipStock/show/" + uuid );
}
function edit(uuid){
	window.open( "${ctx}/cruiseshipStock/edit/" + uuid );
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
			$.post( "${ctx}/cruiseshipStock/delete", {"uuids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
						top.$.jBox.tip('删除成功!');
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
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action"," ${ctx}/cruiseshipStock/list");
	$("#searchForm").submit();
}
</script>	
</head>
<body>
	<div>
<!--右侧内容部分开始-->
   <div class="activitylist_bodyer_right_team_co_bgcolor">
   <form id="searchForm" modelAttribute="cruiseshipStockQuery" action=" ${ctx}/cruiseshipStock/list" method="post">
   		<input id="pageNo" name="pageNo" type="hidden" value="<c:out value="${pageNo}" />"/>
		<input id="pageSize" name="pageSize" type="hidden" value="<c:out value="${pageSize}" />"/>
		<input id="orderBy" name="orderBy" type="hidden" value="<c:out value="${orderBy}" />"/>
        <div class="activitylist_bodyer_right_team_co">
        
           <div class="activitylist_bodyer_right_team_co2 pr wpr20">
               <label>**名称：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="" type="text" flag="istips" />
                        <span class="ipt-tips">仅支持**名称的搜索</span>
                    </div>
                    <div class="form_submit">
                            <input class="btn btn-primary" value="搜索" type="submit">
                    </div>
                    <a class="zksx">展开筛选</a>
                    <div class="ydxbd">
                        
                        
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
uuid：
</div>
<input value="${cruiseshipStockQuery.uuid}" id="uuid" name="uuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游轮UUID：
</div>
<input value="${cruiseshipStockQuery.cruiseshipInfoUuid}" id="cruiseshipInfoUuid" name="cruiseshipInfoUuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
船期：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipStock.shipDate}"/>" id="shipDate" name="shipDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
批发商id：
</div>
<input value="${cruiseshipStockQuery.wholesalerId}" id="wholesalerId" name="wholesalerId"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
备注：
</div>
<input value="${cruiseshipStockQuery.memo}" id="memo" name="memo"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
创建人：
</div>
<input value="${cruiseshipStockQuery.createBy}" id="createBy" name="createBy"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
创建时间：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipStock.createDate}"/>" id="createDate" name="createDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
修改人：
</div>
<input value="${cruiseshipStockQuery.updateBy}" id="updateBy" name="updateBy"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
修改时间：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipStock.updateDate}"/>" id="updateDate" name="updateDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
删除标识：
</div>
<input value="${cruiseshipStockQuery.delFlag}" id="delFlag" name="delFlag"/>								</div>
                        
                    <div class="kong"></div>            
                 </div> 
                 </div>
            </form>
            </div>
            <div class="filter_btn">
	<a class="btn btn-primary" href=" ${ctx}/cruiseshipStock/form" target="_blank">添加</a>
</div>
<table class="t-type t-type100">
                <thead>
                    <tr>
                    	<th width="">序号</th>
                    	<th width=""><input name="allChoose" type="checkbox" onclick="checkall(this)"/></th>
							<th width="">uuid</th>
							<th width="">游轮UUID</th>
							<th width="">船期</th>
							<th width="">批发商id</th>
							<th width="">备注</th>
							<th width="">创建人</th>
							<th width="">创建时间</th>
							<th width="">修改人</th>
							<th width="">修改时间</th>
							<th width="">删除标识</th>
                        <th width="">操作</th>
                    </tr>
                </thead>
                <tbody>
                	
		            <c:forEach var="entry" items="${page.list}" varStatus="v">
			          <tr>
			            <td>${v.index + 1 }</td>
			            <td align="center"><input name="ids" type="checkbox" value="${entry.uuid}" /></td>
							<td>${entry.uuid}</td>
							<td>${entry.cruiseshipInfoUuid}</td>
							<td>${entry.shipDateString}</td>
							<td>${entry.wholesalerId}</td>
							<td>${entry.memo}</td>
							<td>${entry.createBy}</td>
							<td>${entry.createDateString}</td>
							<td>${entry.updateBy}</td>
							<td>${entry.updateDateString}</td>
							<td>${entry.delFlag}</td>
			            
			            <td class="p0">
	                    	<dl class="handle">
	                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
	                    		<dd class="">
	               				<p>
	               					<span></span>
									<a href="javascript:void(0)" onClick="show('${entry.uuid}')">详情</a>
									<br/>
									<a href="javascript:void(0)" onClick="edit('${entry.uuid}')">修改</a>
									<br/>
									<a href="javascript:void(0)" onClick="del('${entry.uuid}')">删除</a>
								</p>
								</dd>
							</dl>
						</td>
			            
			          </tr>
		           </c:forEach>
		           
		       </tbody>
   </table>
	<div class="page"></div>
	<div class="pagination">
   		<dl>
			<dt><input name="allChoose" type="checkbox" onclick="checkall(this)"/>全选</dt>
			<dd><a onClick="alldel()">批量删除</a></dd>
		</dl>
		<div class="endPage">${page}</div>
	</div>
	<br/>

            <!--右侧内容部分结束-->
</div>
</body>
</html>
<script type="text/javascript">


</script>
