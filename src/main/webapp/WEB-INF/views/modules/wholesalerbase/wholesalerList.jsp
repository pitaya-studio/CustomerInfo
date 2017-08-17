<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler" />
<title>批发商管理-列表</title>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		launch();
		//产品名称文本框提示信息
		inputTips();
		// 排序方式
		$("#orderCreate").click(function(){		// 按创建时间
			$("input[name='orderby']").val("1");
			$("#searchForm").submit();
		});
		$("#orderUpdate").click(function(){	// 按修正时间
			$("input[name='orderby']").val("2");
			$("#searchForm").submit();
		});
		// 提交查询表单
		$("input[name=subTheForm]").click(function(){
			$("#searchForm").submit();
		});
	});
	//删除产品的提示信息
	function confirmxDel(mess, proId) {
		top.$.jBox.confirm(mess, '系统提示', function(v) {
			if (v == 'ok') {
				//loading('正在提交，请稍等...');
				$("#delForm").append("<input type='hidden' name='rid' value='"+proId+"' />")
				var param = $("#delForm").serialize();
				$.ajax({
					type:"POST",
					url : contextPath+"/manage/saler/delWholeOffice",
					data : param,
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};
						}
						if(json.res=="success"){
							jBox.tip("删除成功", 'info');
							$("input[name=subTheForm]").click();
						}else{
							jBox.tip("系统繁忙，请稍后再试", 'error');
						}
					}
				});
			}
		}, {
			buttonsFocus : 1
		});
		return false;
	}

	//修改产品的提示信息
	function confirmxChange(mess, proId) {
		top.$.jBox.confirm(mess, '系统提示', function(v) {
			if (v == 'ok') {
				// 跳入批发商修改
				location.href= contextPath + "/manage/saler/gotoAddWholeOfficeOne/"+proId;
			}
		}, {
			buttonsFocus : 1
		});
		return false;
	}

	//查看产品的提示信息
	function confirmxInfo(mess, proId) {
		top.$.jBox.confirm(mess, '系统提示', function(v) {
			if (v == 'ok') {
				// 跳入批发商修改
				location.href= contextPath + "/manage/saler/wholeOfficeInfo/"+proId;
			}
		}, {
			buttonsFocus : 1
		});
		return false;
	}

	// 新增批发商
	function addNew(){
		location.href=contextPath + "/manage/saler/gotoAddWholeOfficeOne/0";
	}

	var activityIds;
	//批量删除确认对话框
	function confirmBatchDel(mess, sta) {
		if (activityIds != "") {
			top.$.jBox.confirm(mess, '系统提示', function(v) {
				if (v == 'ok') {
					//loading('正在提交，请稍等...');
					//$("#searchForm").attr("action","/a/activity/manager/batchdel/"+activityIds);
					//$("#searchForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
			return false;
		} else {
			$.jBox.info('未选择产品', '系统提示');
		}
	}
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/manage/saler/salerlist");
		$("#searchForm").submit();
	}
</script>
</head>
<body>

		<!--右侧内容部分开始-->
		<form id="searchForm" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2">
					<input type="text" value="${form.conn }" class="inputTxt inputTxtlong searchInput" name="conn"
						id="" placeholder="输入供应商名称">
				</div>
				<a class="zksx">筛选</a>
				<div class="form_submit">
					<input type="button" value="搜索"  name="subTheForm" class="btn btn-primary ydbz_x">
					<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件">
				</div>
				<p class="main-right-topbutt">
					<a class="primary" href="javascript:void(0)" onclick="addNew()">新增批发商</a>
				</p>
				<div class="ydxbd">
					<span></span>
					<div class="activitylist_bodyer_right_team_co3">
					<label class="activitylist_team_co3_text">公司名称：</label>
						<input type="text"  name="companyName"  value="${form.companyName }"/>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">创建时间：</label>
						<input type="text" readonly="readonly"
							   onclick="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('endDate').value==''){$dp.$('endDate').value=vvv;}},maxDate:'#F{$dp.$D(\'endDate\')}'})"
							   class="inputTxt dateinput" name="startDate" id="startDate" value="${startDate }"/>
						<span>至</span>
						<input type="text" readonly="readonly"
							   onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"
							   value="" name="endDate" class="inputTxt dateinput"
							   id="endDate"  value="${endDate }"/>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">上级节点：</label>
						<input type="text"  name="parentName"  value="${form.parentName }"/>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">批发商类别：</label>
						<div class="selectStyle">
							<trekiz:defineDict name="typevalue" input="select" type="wholesaler_type" defaultValue="${form.typevalue }"/>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">批发商等级：</label>
						<div class="selectStyle">
							<trekiz:defineDict name="levelvalue" input="select" type="wholesaler_level" defaultValue="${form.levelvalue }"/>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">状态：</label>
						<div class="selectStyle">
							<select name="status">
								<option value="1"  <c:if test="${form.status==1 }">selected</c:if>>启用</option>
								<option value="2"  <c:if test="${form.status==2 }">selected</c:if>>停用</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<!--查询结果筛选条件排序开始-->
			<%--由于这个排序和标签切换功能本身就有问题，先恢复原始样式 by tlw 20170314 start--%>
			<div class="filterbox">
				<div class="filter_num">
					查询结果<strong>${count }</strong>条
				</div>
				<div class="filter_check">
					<span>信息筛选：</span>
					<c:if test="${not empty form.frontier }">
						<label><input type="radio" name="frontier"  value="1" <c:if test="${form.frontier==1}">checked="checked"</c:if> />国内</label>
						<label><input type="radio" name="frontier"  value="2" <c:if test="${form.frontier==2}">checked="checked"</c:if>/>国外</label>
					</c:if>
					<c:if test="${empty form.frontier }">
						<label><input type="radio" name="frontier"  value="1" checked="checked" />国内</label>
						<label><input type="radio" name="frontier"  value="2" />国外</label>
					</c:if>
				</div>
				<div class="filter_sort">
					<span>表单排序：</span>
					<a href="javascript:void(0)" id="orderCreate" >创建时间</a>
					<a href="javascript:void(0)" id="orderUpdate" >更新时间</a>
					<input type="hidden"  name="orderby"  value="1"/>
				</div>
			</div>
			<%--由于这个排序和标签切换功能本身就有问题，先恢复原始样式 by tlw 20170314 end--%>
	</form>
		<!--查询结果筛选条件排序结束-->

		<table class="table activitylist_bodyer_table mainTable" id="contentTable">
			<thead>
				<tr>
					<th width="2%">序号</th>
					<th width="10%">批发商编码/名称</th>
					<th width="10%">公司名称</th>
					<th width="10%">供应品牌</th>
					<th width="9%">批发商类型</th>
					<th width="6%">批发商等级</th>
					<th width="5%">状态</th>
					<th width="6%">创建时间</th>
					<th width="6%">更新时间</th>
					<th width="10%">上级节点名称</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${count>0 }">
					<c:forEach items="${page.list }"  var="office" varStatus="offindex">
						<tr>
							<td class="table_borderLeftN">
								<!-- <input type="checkbox" onclick="idcheckchg(this)" name="activityId" value="1"> -->
								${offindex.index+1}
							</td>
							<td>${office.code } / ${office.name }</td>
							<td>${office.companyName }</td>
							<td>${office.supplierBrand }</td>
							<td class="tc">
								<c:if test="${not empty office.supplierTypeNames }">${office.supplierTypeNames }</c:if>
								<c:if test="${empty office.supplierTypeNames }">未选</c:if>
							</td>
							<td class="tc">
								<c:if test="${not empty office.level.label  }">${office.level.label }</c:if>
								<c:if test="${empty office.level.label  }">未选</c:if>
							</td>
							<td class="tc">
								<c:if test="${office.status ==1 }">启动</c:if>
								<c:if test="${office.status ==2 }">停用</c:if>
							</td>
							<td class="tc"><fmt:formatDate value="${office.createDate }" pattern="yyyy-MM-dd"/></td>
							<td class="tc"><fmt:formatDate value="${office.updateDate }" pattern="yyyy-MM-dd"/></td>
							<td class="tc">${office.parent.name }</td>
							<td class="tda tc">
								<a href="javascript:void(0)" onclick="confirmxChange('要修改该批发商信息吗？',${office.id})">修改</a>
								<a href="javascript:void(0)" onclick="confirmxDel('要删除该批发商及所有相关项吗？',${office.id})">删除</a>
								<a href="javascript:void(0)" onclick="confirmxInfo('要查看该批发商信息吗？',${office.id})">详情</a></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${count == 0}">
					<td colspan="11" style="text-align: center;">没有符合条件的记录</td>
				</c:if>

				<!--
				<tr>
					<td class="table_borderLeftN"><input type="checkbox"
						onclick="idcheckchg(this)" name="activityId" value="1">1</td>
					<td>机票供应商1</td>
					<td>散拼、酒店</td>
					<td class="tc">5A级供应商</td>
					<td>zhangsansan</td>
					<td class="p0">
						<div class="out-date">张三三</div>
						<div class="close-date">010-64276556</div>
					</td>
					<td class="tc">2014-03-01</td>
					<td class="tda tc"><a href="供应商管理-新增第1步-基本信息.html"
						target="_blank">修改</a> <a href="javascript:void(0)"
						onclick="return confirmxDel('要删除该供应商及所有子机构项吗？',1)">删除</a> <a
						href="供应商管理-供应商详情页.html" target="_blank">详情</a></td>
				</tr> -->
			</tbody>
		</table>
		<div class="page">
			<div class="pagination">
				<!--
				<dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox">全选
					</dt>
					<dd>
						<a onclick="confirmBatchDel('删除所选择的供应商吗','del')">批量删除</a>
					</dd>
				</dl>
				 -->
				 <!-- 用于删除的表单 -->
				 <form id="delForm"></form>
				 <%--modified for UG_V2 删除没用的div by tlw at 20170302 start--%>
						<div class="endPage">${page}</div>
						<div style="clear:both;"></div>
				 <%--modified for UG_V2 删除没用的div by tlw at 20170302 end--%>
				<div style="clear: both;"></div>
			</div>
		</div>
		<!--右侧内容部分结束-->
</body>
</html>