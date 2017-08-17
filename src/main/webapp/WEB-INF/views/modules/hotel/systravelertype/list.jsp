<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统游客类型列表</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>  --%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
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
	$("div.ydxbd").find("input").each(function(){
		if($(this).val()){
			$("a.zksx").click();
			return false;
		}
	})
});
function show(uuid){
	window.open("${ctx}/sysTravelerType/show/" + uuid );
}
function edit(uuid){
	window.open( "${ctx}/sysTravelerType/edit/" + uuid );
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
			$.post( "${ctx}/sysTravelerType/delete", {"uuids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
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
	$("#searchForm").attr("action"," ${ctx}/sysTravelerType/list");
	$("#searchForm").submit();
}
</script>	
</head>
<body>
	<div>
<!--右侧内容部分开始-->
   <div class="activitylist_bodyer_right_team_co_bgcolor">
   <form id="searchForm" modelAttribute="sysTravelerTypeQuery" action=" ${ctx}/sysTravelerType/list" method="post">
   		<input id="pageNo" name="pageNo" type="hidden" value="<c:out value="${pageNo}" />"/>
		<input id="pageSize" name="pageSize" type="hidden" value="<c:out value="${pageSize}" />"/>
		<input id="orderBy" name="orderBy" type="hidden" value="<c:out value="${orderBy}" />"/>
        <div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<input class="txtPro inputTxt searchInput" id="name" name="name" value="${sysTravelerTypeQuery.name}"
					   type="text" placeholder="请输入游客类型名称" />
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				 <input class="btn btn-primary" value="搜索" type="submit">
			</div>
			<p class="main-right-topbutt">
				<a class="primary" href="javascript:void(0)" onclick="jbox_xzxj()">新增询价</a>
			</p>
			<div class="ydxbd">
				<span></span>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					uuid：
					</div>
					<input value="${sysTravelerTypeQuery.uuid}" id="uuid" name="uuid"/>
				</div> --%>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					排序：
					</div>
					<input value="${sysTravelerTypeQuery.sort}" id="sort" name="sort"/>
				</div> --%>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					游客类型名称：
					</div>
					<input value="${sysTravelerTypeQuery.name}" id="name" name="name"/>
				</div> --%>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					游客类型名称：
					</div>
					<input value="${sysTravelerTypeQuery.shortName}" id="shortName" name="shortName"/>
				</div> --%>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					创建人：
					</div>
					<input value="${sysTravelerTypeQuery.createBy}" id="createBy" name="createBy"/>
				</div> --%>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">创建时间：</label>
					<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${sysTravelerTypeQuery.createDate}"/>" id="createDate" name="createDate"  class="inputTxt dateinput"/>
				</div>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					修改人：
					</div>
					<input value="${sysTravelerTypeQuery.updateBy}" id="updateBy" name="updateBy"/>
				</div> --%>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">修改时间：</label>
					<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${sysTravelerTypeQuery.updateDate}"/>" id="updateDate" name="updateDate" class="inputTxt dateinput"/>
				</div>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">
					删除状态：
					</div>
					<input value="${sysTravelerTypeQuery.delFlag}" id="delFlag" name="delFlag"/>
				</div> --%>
			<div class="kong"></div>
		 </div>
         </div>
    </form>
    </div>
<table class="t-type t-type100 mainTable">
                <thead>
                    <tr>
                    	<th width="">序号</th>
                   	    <!-- <th width=""><input name="allChoose" type="checkbox" onclick="checkall(this)"/></th> -->
						<!-- <th width="">排序</th> -->
						<th width="">游客类型名称</th>
						<th width="">游客类型简写</th>
						<th width="">人员类型</th>
						<!-- <th width="">创建人</th> -->
						<th width="">创建时间</th>
					    <!--<th width="">修改人</th> -->
						<th width="">修改时间</th>
						<!--<th width="">删除状态</th> -->
                        <th width="">操作</th>
                    </tr>
                </thead>
                <tbody>
                	
		            <c:forEach var="entry" items="${page.list}" varStatus="v">
			          <tr>
			            <td align="center"><input name="ids" type="checkbox" value="${entry.uuid}" />&nbsp;${v.index + 1 }</td>
			            <%-- <td align="center"><input name="ids" type="checkbox" value="${entry.uuid}" /></td> --%>
						<%-- <td>${entry.sort}</td> --%>
						<td>${entry.name}</td>
						<td>${entry.shortName}</td>
						<td>
							<c:choose> 
							  <c:when test="${entry.personType == 0}">   
								 成人  
							  </c:when> 
							  <c:when test="${entry.personType == 1}">   
							   	 婴儿  
							  </c:when> 
							   <c:when test="${entry.personType == 2}">   
							   	 儿童  
							  </c:when> 
							  <c:otherwise>   
							            
							  </c:otherwise> 
							</c:choose> 
						</td>
						<%-- <td>${entry.createBy}</td> --%>
						<td>${entry.createDateString}</td>
						<%-- <td>${entry.updateBy}</td> --%>
						<td>${entry.updateDateString}</td>
						<%-- <td>${entry.delFlag}</td> --%>
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
			<dd>
                <input class="btn ydbz_x" value="批量删除" type="button" onClick="alldel()">
            </dd>
		</dl>
		<div class="endPage">${page}</div>
	</div>
	<br/>
            <!--右侧内容部分结束-->
</div>
</body>
</html>
