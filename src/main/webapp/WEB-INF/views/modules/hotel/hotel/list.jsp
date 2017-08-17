<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-酒店管理</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
	var $ctx = '${ctx}';
$(function(){
	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	
	$("#position").val("${hotel.position}");
	//设置地理属性默认值
	changePosition($("#position"));
	
	setTimeout(setzksx,10);
	
	//取消一个checkbox 就要联动取消 全选的选中状态
	$("input[name='ids']").click(function(){
		/* if($(this).attr("checked")){
			
		}else{
			$("input[name='allChoose']").removeAttr("checked");
		} */
		var result = 0;//声明一个表示结果的变量
	    $("input[name='ids']").each(function() {//遍历其他的checkbox
	    	if ($(this).attr("checked") == "checked") {//选中的+1
	            result += 1;//结果+1
	        }
	    });
	    if (result == $("input[name='ids']").length) {//选中的==checkbox的个数
	    	$("input[name='allChoose']").attr("checked", "true");//全选的checkbox自动勾选
	    }else {//如果结果不等则不勾选
	        $("input[name='allChoose']").removeAttr("checked");//全选的checkbox不勾选
	    }
	});
	
});

function setzksx(){
	if($("#position").val() == ""&& $("#type").val() == "" && $("#country").val() == "" && $("#star").val() == ""){
			
	}else{
		$('.zksx').click();
	}
}

//境内境外 改变地址展现形式
function changePosition(obj){
	
	$.ajax({
		type: "POST",
	   	url: "${ctx}/geography/getGeoListAjax",
	   	data: {
			"type":$(obj).val()
		},
		async:false,
		dataType: "json",
	   	success: function(data){
	   		$("#country").empty();
	   		$("#country").append("<option value=''>请选择</option>");
	   		if(data){
	   			var val="${hotel.country}";
	   			
	   			$.each(data,function(){
	   				var selected="";
   					if(val==this.uuid)selected="selected=\"selected\"";
   					$("#country").append("<option value='"+this.uuid+"' "+selected+">"+this.nameCn+"</option>");
	   			})
	   			
	   		}
			
	   	}
	});
}
function optMeal(uuid){
	window.open( "${ctx}/hotelMeal/list/" + uuid) ;
}
function optRoom(uuid){
	window.open("${ctx}/hotelRoom/list/" + uuid );
}
function travelerForm(uuid){
	window.open("${ctx}/hotelTravelerTypeRelation/edit/" + uuid );
}
function show(uuid){
	window.open("${ctx}/hotel/show/" + uuid );
}
function edit(uuid){
	window.open( "${ctx}/hotel/edit/" + uuid );
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
			$.post( "${ctx}/hotel/delete", {"ids":ids.join(",")}, 
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
	$("#searchForm").attr("action"," ${ctx}/hotel/list");
	$("#searchForm").submit();
}
</script>	
</head>
<body>
<page:applyDecorator name="hotel_controller" >
    <page:param name="current">online</page:param>
</page:applyDecorator>
<!--右侧内容部分开始-->
   <div class="activitylist_bodyer_right_team_co_bgcolor">
   <form id="searchForm" action=" ${ctx}/hotel/list" method="post">
   		<input id="pageNo" name="pageNo" type="hidden" value="<c:out value="${pageNo}" />"/>
		<input id="pageSize" name="pageSize" type="hidden" value="<c:out value="${pageSize}" />"/>
		<input id="orderBy" name="orderBy" type="hidden" value="<c:out value="${orderBy}" />"/>
        <div class="activitylist_bodyer_right_team_co">
        
            <div class="activitylist_bodyer_right_team_co2 pr">
               <input class="txtPro inputTxt searchInput" id="nameCn" name="nameCn" value="${hotel.nameCn}" type="text" placeholder="请输入酒店名称" />
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit">
			</div>
			<p class="main-right-topbutt">
				<a href="${ctx}/hotel/form" >添加酒店</a>
			</p>
			<div class="ydxbd">
				<span></span>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">位置属性：</label>
					<div class="selectStyle">
						<select id="position" name="position"  onchange="changePosition(this);">
							<option value="">请选择</option>
							<option value="1">境内酒店</option>
							<option value="2">境外酒店</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">酒店类别：</label>
					<div class="selectStyle">
						<trekiz:defineDict name="type" input="select" type="hotel_type" defaultValue="${hotel.type}"/>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">国家：</div>
					<select id="country" name="country">
						<c:forEach items="${countrys }" var="country">
							<option value="${country.uuid }" <c:if test="${country.uuid == hotel.country}">selected</c:if> >${country.nameCn }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">酒店星级：</div>
					<div class="selectStyle">
						<select id="star" name="star">
							<option value="">请选择</option>
							<c:forEach items="${whoStarList }" var="whoStar">
								<option value="${whoStar.uuid}" <c:if test="${whoStar.uuid == hotel.star }">selected="selected"</c:if>>${whoStar.label}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="kong"></div>
		 	</div>
            </form>
            </div>
            <div class="filter_btn">

</div>
<table class="t-type t-type100 mainTable">
                <thead>
                    <tr>
                    	<th width="">序号</th>
						<th width="">酒店名称</th>
						<th width="">位置属性</th>
						<th width="">国家</th>
						<th width="">酒店类型</th>
						<th width="">酒店星级</th>
                        <th width="">操作</th>
                    </tr>
                </thead>
                <tbody>
		            <c:forEach var="entry" items="${page.list}" varStatus="v">
			          <tr>
			            <td align="center"><input name="ids" type="checkbox" value="${entry.uuid}" />&nbsp;${v.index + 1 } </td>
						<td>${entry.nameCn}</td>
						<td>${entry.position==1?"境内酒店":"境外酒店"}</td>
						<td>
							<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${entry.country}"/>
						</td>
						<td>${entry.areaType==1?"内陆":"海岛"}</td>
						<td><trekiz:autoId2Name4Class classzName="HotelStar" sourceProName="uuid" srcProName="label" value="${entry.star}"/></td>
			            <td class="p0">
	                    	<dl class="handle">
	                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
	                    		<dd class="">
	               				<p style="width:75px;">
	               					<span></span>
									<a href="javascript:void(0);" onclick="optMeal('${entry.uuid }')">管理餐型</a>
<!-- 									<br /> -->
<!-- 									<a href="javascript:void(0)" onClick="travelerForm('${entry.uuid}')">游客类型绑定</a> -->
									<br/>
									<a href="javascript:void(0)" onClick="optRoom('${entry.uuid}')">管理房型</a>
									<br/>
									<a href="javascript:void(0)" onClick="edit('${entry.uuid}')">修改</a>
									<br/>
									<a href="javascript:void(0)" onClick="del('${entry.uuid}')">删除</a>
									<br/>
									<a href="javascript:void(0)" onClick="show('${entry.uuid}')">详情</a>
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
				<input class="btn ydbz_x" type="button" value="批量删除" onClick="alldel()">
			</dd>
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
