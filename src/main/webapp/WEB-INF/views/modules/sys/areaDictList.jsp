<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>目的地管理</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		$().ready(function(){
				//产品名称获得焦点显示隐藏
				$("#wholeSalerKey").focusin(function(){
				var obj_this = $(this);
				obj_this.next("span").hide();
				}).focusout(function(){
				var obj_this = $(this);
				if(obj_this.val()!="") {
				obj_this.next("span").hide();
				}else
				obj_this.next("span").show();
				});
			if($("#wholeSalerKey").val()!="") {
				$("#wholeSalerKey").next("span").hide();
				} 
			$('.destination dl').find('dd:last').css({'border-bottom':'#ddd 1px solid'});
			if("${message}")
				$.jBox.tip("保存成功","success",{timeout:1000});
		});
		//修改操作
		function update() {
				getIdArr();
				$("#selForm").attr("action","${ctx}/sys/dict/descUpdate");
				$("#selForm").submit();
		}
		//搜索
		function searchButton() {
				$("#term").val($("#wholeSalerKey").val());
				getIdArr();
				$("#searchForm").attr("action","${ctx}/sys/dict/descSearch");
				$("#searchForm").submit();
		}
		//数据状态(新增，删除，默认显示)
		function getVal(obj) {
			checkOpt(obj);
			checkChildOpt(obj);
			setCheckboxClass(obj);
		}
		
		function setCheckboxClass(obj) {
			if ($(obj).attr("class")) {
				if($(obj).attr("class")=="selected"){
					$(obj).attr("class",'cancel');
				}else if($(obj).attr("class")=="cancel"){
					$(obj).attr("class",'selected');
				}else{
					$(obj).attr("class",'');
				}
			} else {
				$(obj).attr("class",'checked');
			}
		}
		
		//选择目的地后默认 联动选择父节点，如果子节点都没有选中则默认把父节点的选中去掉 add by zhanghao 2015-03-17
		function checkOpt(obj) {
			var parentId = $(obj).attr("parId");
			if ($(obj).attr("checked")=="checked") {
				
				//选中操作时做递归的父节点选中操作
				$("input[type=checkbox][value="+parentId+"]").each(function(){
					//父节点的选择后的class改变 如果已经是选择状态的不改变class
					if($(this).attr("checked")=="checked"){
						
					}else{
						setCheckboxClass(this);
					}
					$(this).attr("checked",true);//父节点选中
					checkOpt($(this));
					return false;
				});
			} else {
				//取消选中操作时如果子节点都不选中则父节点取消选中
				var b = true;
				$("input[type=checkbox][parId=" + parentId + "]").each(function(){
					if($(this).attr("checked") == "checked"){
						b = false;
						return false;
					}
				});
				
				if (b) {//父节点下的所有子节点都取消选中时 该父节点取消选中
					$("input[type=checkbox][value=" + parentId + "]").each(function(){
						
						//父节点的选择后的class改变 如果已经是取消状态的不改变class
						if ($(this).attr("checked")=="checked") {
							setCheckboxClass(this);
						} else {
							
						}
						$(this).attr("checked",false);//父节点取消选中
						checkOpt($(this));
						return false;
					});
				}
				// forbug 若有未勾选的城市，则将全选勾选去掉
				$("#allChk").attr("checked",false);
			}
		}
		
		
		//选择目的地后默认 联动选择父节点，如果子节点都没有选中则默认把父节点的选中去掉 add by zhanghao 2015-03-17
		function checkChildOpt(obj) {
			var parentId = $(obj).attr("parId");
			var objValue = $(obj).attr("value");
			if ($(obj).attr("checked") == "checked") {
				//如果父节点被选中，则下面所有子孙节点都要被选中
				$("input[type=checkbox][parId=" + $(obj).val() + "]").each(function(){
					//父节点的选择后的class改变 如果已经是选择状态的不改变class
					if($(this).attr("checked") != "checked"){
						$(this).attr("checked",true);//子节点被选中
						setCheckboxClass(this);
						checkChildOpt($(this));
					}
				});
				
			} else {
				//取消选中操作时如果子节点都不选中则父节点取消选中
				var b = true;
				$("input[type=checkbox][parId=" + parentId + "]").each(function(){
					if($(this).attr("checked") == "checked"){
						b = false;
						return false;
					}
				});
				
				//取消的时候应取消所有子孙节点选中状态
				$("input[type=checkbox][parId=" + $(obj).val() + "]").each(function(){
					//父节点的选择后的class改变 如果已经是选择状态的不改变class
					if($(this).attr("checked") == "checked"){
						setCheckboxClass(this);
						$(this).attr("checked",false);//父节点取消选中
						checkChildOpt($(this));
					}
				});
			}
		}
		
		//数据状态Id	
		function getIdArr() {
			var checkedIds = "";
			var cancelIds = "";
			var selectedIds = "";
			$("[class='checked']").each(function(index,element){
				checkedIds += $(element).val()+",";
			});
			$("[class='cancel']").each(function(index,element){
				cancelIds += $(element).val()+",";
			});
			$("[class='selected']").each(function(index,element){
				selectedIds += $(element).val()+",";
			});
			
			$("#checkedIds").val(checkedIds);
			$("#cancelIds").val(cancelIds);
			$("#upLoadTypeIds").val(selectedIds);
		}
		
		$(function(){
			//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
			var $tmp=$("form :checkbox");
			//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
			$('#allChk').attr('checked',$tmp.length==$tmp.filter(':checked').length);
			//全选
			$("#allChk").click(function() {
				if ($(this).attr("checked")) {
					$("form :checkbox:not(:checked)").each(function(index, obj) {
						setCheckboxClass(this);
						$(this).prop("checked", true);
					});
				} else {
					$("form :checkbox:checked").each(function(index, obj) {
						setCheckboxClass(this);
						$(this).prop("checked", false);
					});
				}
			});
			//反选
			$("#reverseChk").click(function() {
				$("form :checkbox").each(function(index, obj) {
					setCheckboxClass(this);
					if ($(this).prop("checked")) {
						$(this).prop("checked", false);
					} else {
						$(this).prop("checked", true);
					}
				});
				$("form :checkbox:checked").each(function(index, obj) {
					var parentId = $(this).attr("parId");
					var parentObj = $("input[type=checkbox][value=" + parentId + "]");
					if ($(parentObj).length > 0 && !$(parentObj).prop("checked")) {
						setCheckboxClass(parentObj);
						$(parentObj).prop("checked", true);
					}
				});
				//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
				var $tmp=$("form :checkbox");
				//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
				$('#allChk').attr('checked',$tmp.length==$tmp.filter(':checked').length);
			});
			
			var explorer = window.navigator.userAgent;
			if(explorer.indexOf("MSIE") >= 0){
				$("input[type='checkbox']").attr('ondblclick', 'this.click()');
			}
		});
	</script>
	<%--该页面比较特殊，单独添加css文件--%>
	<link rel="stylesheet" href="${ctxStatic }/css/areaDictLst.css">
	
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">area</page:param>
</page:applyDecorator>
	<%--<div class="ydbzbox fs">--%>
	<%--<div class="tableDiv flight">--%>
		<div class="inquiry">
			<form:form id="searchForm" action="" method="post">
				<input id="type" type="hidden" name="type" value="area"/>
				<input id="term" type="hidden" name="term" value="${term}"/>
                <div class="activitylist_bodyer_right_team_co">
                    <div class="activitylist_bodyer_right_team_co2 pr">
				        <%--<div class="inquiry_left">--%>
                        <input value="${seek}" name="wholeSalerKey"  id="wholeSalerKey"
							   class="searchInput inputTxt radius4" type="text" placeholder="请输入搜索区域或国家"
							   onkeydown="if (event.keyCode == 13){searchButton();}"/>
                        <%--<span class="ipt-tips">请输入搜索区域或国家</span>--%>
                    </div>
                    <div class="form_submit">
                        <input id="search" type="submit" class="inquiry_left_submit btn btn-primary"  onClick="searchButton()" value="搜 索"/>
                    </div>
                    <%--</div>--%>
                    <div class="inquiry_right">查询结果：<b>${fn:length(dataList)}</b>条</div>
				    <input id="upLoadTypeIds" type="hidden" name="upLoadTypeIds" value="${upLoadTypeIds}"/>
                </div>
			</form:form>
		</div>
		<div id="base-info-manage-header">
			<label><input check-action="All" type="checkbox" id="allChk"> 全选</label>
			<label><input check-action="Reverse" type="checkbox" id="reverseChk"> 反选</label>
		</div>
		<div class="destination">
		<form:form id="selForm" modelAttribute="dataList" action="" method="post">
			<input id="checkedIds" type="hidden" name="checkedIds" value="${checkedIds}"/>
			<input id="cancelIds" type="hidden" name="cancelIds" value="${cancelIds}"/>
			<input id="type" type="hidden" name="type" value="area"/>
			<div class="destination_title">
				<div class="destination_a">编号</div>
				<div class="destination_b">区域</div>
				<div class="destination_c">国家/大区</div>
                <div class="destination_d">二级区域</div>
                <div class="destination_e">省市/市</div>
				<div class="destination_f">是否显示</div>
			</div>
			<dl>
			<c:forEach items="${dataList}" var="area">
				<c:set var="arealistsize" value="${fn:length(arealist)}"></c:set>
				<c:if test="${area.type eq 1}">
					<dt>
						<div class="destination_a">${area.id}</div>
						<div class="destination_b">${area.name}</div>
						<div class="destination_c"></div>
                        <div class="destination_d"></div>
                        <div class="destination_e"></div>
						<div class=""></div>
					</dt>
				</c:if>
				<c:if test="${area.type eq 2}">
					<dd>
						<div class="destination_a">${area.id}</div>
						<div class="destination_b"></div>
						<div class="destination_c">${area.name}</div>
                        <div class="destination_d"></div>
                        <div class="destination_e"></div>
						<div class=""><input type="checkbox" parId="${area.getParent().getId()}" id="selected${s.count }" class="<c:if test="${area.selected eq '1'}">selected</c:if>" value="${area.id}" <c:if test="${area.selected eq '1'}">checked</c:if> onClick="getVal(this)"/></div>
					</dd>
				</c:if>
                <c:if test="${area.type eq 3}">
                    <dd>
                        <div class="destination_a">${area.id}</div>
                        <div class="destination_b"></div>
                        <div class="destination_c"></div>
                        <div class="destination_d">${area.name}</div>
                        <div class="destination_e"></div>
                        <div class=""><input type="checkbox" parId="${area.getParent().getId()}"  id="selected${s.count }" class="<c:if test="${area.selected eq '1'}">selected</c:if>" value="${area.id}" <c:if test="${area.selected eq '1'}">checked</c:if> onClick="getVal(this)"/></div>
                    </dd>
                </c:if>
                <c:if test="${area.type eq 4}">
                    <dd>
                        <div class="destination_a">${area.id}</div>
                        <div class="destination_b"></div>
                        <div class="destination_c"></div>
                        <div class="destination_d"></div>
                        <div class="destination_e">${area.name}</div>
                        <div class=""><input type="checkbox" parId="${area.getParent().getId()}"  id="selected${s.count }" class="<c:if test="${area.selected eq '1'}">selected</c:if>" value="${area.id}" <c:if test="${area.selected eq '1'}">checked</c:if> onClick="getVal(this)"/></div>
                    </dd>
                </c:if>
			</c:forEach>
			</dl>
		</form:form>
		</div>
			<c:if test="${empty dataList}"><div class="wtjqw">没有关于该区域的信息</div></c:if>
			<c:if test="${not empty dataList}">
				<%--<a class="ydbz_s normalList_save" onclick="update()" >保存</a>--%>
				<input type="button" class="inquiry_left_submit btn btn-primary normalList_save" onclick="update()" value="保存">
			</c:if>
		<%--</div>--%>
	<%--</div>--%>
</body>
</html>