<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-签证国家</title>

<link href="${ctxStatic }/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link href="${ctxStatic }/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${ctxStatic }/css/jbox.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/jquery.validate.min.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic }/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<style type='text/css'>
	.ui-front {
		z-index: 2100;
	}
</style>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
//基础信息维护-新增签证国家及领区
function addVisaCountry(type,id,countryId,countryName,oldArea,oldRemark) {
	var html = '<div id="abc" style="position:absolute; z-index:999;margin-top:20px; padding-left:50px;">';
	html += '<dl style="overflow:hidden; padding-right:5px;">';
	html += '<dd class="jbox-margin0 fl">';
	html += '<label><font color="red">*&nbsp;</font>国家：</label>';
	if(type=='new'){
		html += '<select id="country" name="country" class="jbox-width100">';
		html += '<option value="-1">请选择</option>';
		html += '<c:forEach items="${countryList}" var="country">';
		html += '<option value="${country.id}">${country.countryName_cn}</option>';
		html += '</c:forEach>';
		html += '</select>';
	}else if(type=='edit'){
		html += '<label>'+countryName+'</label>';
	}
	html += '<br/>';html += '<br/>';
	html += '<label><font color="red">*&nbsp;</font>领区：</label>';
	html += '<select id="area" name="area" class="jbox-width100">';
	html += '<option value="-1">请选择</option>';
	html += '<c:forEach items="${areaList}" var="area">';
	if(type=='new'){
		html += '<option value="${area.value}">${area.label}</option>';
	}else if(type=='edit'){
		if(oldArea=="${area.value}"){
			html += '<option value="${area.value}" selected="selected">${area.label}</option>';
		}else{
			html += '<option value="${area.value}">${area.label}</option>';
		}
	}
	html += '</c:forEach>';
	html += '</select>';
	html += '<br/>';html += '<br/>';
	html += '<div class="jbox-margin0 fl">';
	if(type=='new'){
		html += '<label>备注：</label><textarea id="remark" name="remark"></textarea>';
	}else if(type=='edit'){
		html += '<label>备注：</label><textarea id="remark" name="remark">'+oldRemark+'</textarea>';
	}
	html += '</div>';
	html += '</dd>';
	html += '</dl>';
	html += '</div>';
	
	if(type=='new'){
		var title = "新增关联信息";
	}else if(type=='edit'){
		var title = "修改关联信息";
	}
	
	$.jBox(html, { title: title,buttons:{'取消': 0,'提交':1}, 
		submit:function(v, h, f){
		if (v=="1"){
			//国家
			if(type=='new'){
				var country = $("#country").val();
			}else if(type=='edit'){
				var country = countryId;
			}
			if(country == "" || country == -1){
				top.$.jBox.tip("签证国家不能为空");
				return false;
			}
			//领区
			var area = $("#area").val();
			var select=document.getElementById('area');
			var areavalue=select.options[select.selectedIndex].text;//获取文本
			if(area == "" || area == -1 || areavalue==""){
				top.$.jBox.tip("领区不能为空");
				return false;
			}
			//备注
			var remark = $("#remark").val();
			
			//没有任何改动点击提交
			if(type=='edit'){
				if(area==oldArea && remark==oldRemark){
					return true;
				}
			}
			
			$.ajax({
				type: "POST",
				url: "${ctx}/visa/interviewNotice/saveVisaAddress",
				cache:false,
				async:false,
				data:{
					"id" : id,
					"country" : country,
					"area" : area,
					"remark" : remark,
					"oldArea" : oldArea
				},
				success: function(data){
					if(data.result=="2"){
						top.$.jBox.tip("此签证国家及领区正在使用中，修改失败！", 'warnning');
						flag = true;
					}else if(data.result=="0"){
						top.$.jBox.tip("已关联此领区信息，请重新选择！", 'warnning');
						flag = false;
					}else{
						flag = true;
						if(type=='new'){
							top.$.jBox.tip("添加成功！", 'warnning');
						}else if(type=='edit'){
							top.$.jBox.tip("修改成功!", 'warnning');
						}
						window.location.href = "${ctx}/visa/interviewNotice/visaInterviewNoticeAddress";
					}
				},
			});
			return flag;
		}else{
			return true;
		}
	},height:320,width:520});
	$("#country").comboboxSingle();
	$("#area").comboboxSingle();
}

//基础信息维护-删除签证国家及领区
function deleteVisaCountry(message,countryId,area) {
	top.$.jBox.confirm(message,'系统提示',function(v){
		if(v=='ok'){
			$.ajax({
				type: "POST",
				url: "${ctx}/visa/interviewNotice/deleteVisaAddress",
				cache:false,
				async:false,
				data:{
					"countryId" : countryId,
					"area" : area
				},
				success: function(data){
					if(data.result=="0"){
						top.$.jBox.tip("此签证国家及领区正在使用中，删除失败!", 'warnning');
					}else{
						top.$.jBox.tip("删除成功!", 'warnning');
						window.location.href = "${ctx}/visa/interviewNotice/visaInterviewNoticeAddress";
					}
				},
			});
			return true;
		}else{
			return true;
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}

//批量删除签证国家及领区
function batchDelete(){
	var listIds ="";
	$("input[type=checkbox][name='id']").each(function(){
		if($(this).attr("checked")){
			var listId = $(this).attr("listIds");
			listIds += listId+",";
		}
	});
	if(listIds==""){
		top.$.jBox.tip('请选择领区！');
		return;
	}else{
		//删除最后一个逗号
		listIds = listIds.substring(0,listIds.length-1);
	}
	checkVisaCountry(listIds);//检查游客是否满足删除条件
}

//检查签证国家及领区是否发布过产品
function checkVisaCountry(listIds){
	top.$.jBox.confirm('您确定要删除所选签证国家领区信息吗？','系统提示',function(v){
		if(v=='ok'){
			$.ajax({
				type: "POST",
				url: "${ctx}/visa/interviewNotice/batchDelete",
				cache:false,
				async:false,
				data:{
					"listIds" : listIds
				},
				success: function(data){
					if(data.result=="2"){
						top.$.jBox.tip(data.msg+"正在使用中，删除失败!", '删除失败');
					}else if(data.result=="0"){
						top.$.jBox.tip("删除失败，请联系管理员!", 'warnning');
					}else{
						top.$.jBox.tip("删除成功!", 'warnning');
						window.location.href = "${ctx}/visa/interviewNotice/visaInterviewNoticeAddress";
					}
				},
			});
		}else{
			return true;
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}

//列表全选
function checkall(obj) {
	if (obj.checked) {
		$("input[name='id']").not("input:checked").each(function() {
			this.checked = true;
		});
		$("input[name='allChk']").not("input:checked").each(function() {
			this.checked = true;
		});
	} else {
		$("input[name='id']:checked").each(function() {
			this.checked = false;
		});
		$("input[name='allChk']:checked").each(function() {
			this.checked = false;
		});
	}
}
//列表反选
function checkallNo(obj) {
	$("input[name='id']").each(function() {
		$(this).attr("checked", !$(this).attr("checked"));
	});
	allchk();
}
function allchk() {
	var chknum = $("input[name='id']").size();
	var chk = 0;
	$("input[name='id']").each(function() {
		if ($(this).attr("checked") == 'checked') {
			chk=chk+1;
		}
	});
	if (chknum == chk) {//全选 
		$("input[name='allChk']").attr("checked", true);
	} else {//不全选 
		$("input[name='allChk']").attr("checked", false);
	}
}
</script>
</head>
<body>


<form id="selForm" action="/a/sys/currency/saveDispStatus" method="post">
	<!--查询结果筛选条件排序开始-->
	<div class="filter_btn">
		<%--<a class="btn btn-primary" onclick="addVisaCountry('new','','','','','');" href="javascript:void(0)">+新增签证国家</a>--%>
		<input class="btn btn-primary ydbz_x"  onclick="addVisaCountry('new','','','','','');" value="新增签证国家" type="button">
	</div>
	<!--查询结果筛选条件排序结束-->
	<div  class="tableDiv flight" style="width:100%;padding-left:0px;padding-top: 0px;">
		<c:if test="${fn:length(visaInterviewNoticeAddressList) > 0 }">
			<tr class="checkalltd">
				<td colspan='19' class="tl">
					<label>
						<input type="checkbox" name="allChk" onclick="checkall(this)">
						全选
					</label>
					<label>
						<input type="checkbox" name="allChkNo" onclick="checkallNo(this)">
						反选
					</label>
					&nbsp;&nbsp;
					<input class="btn-primary" type="button" onclick="batchDelete()" value="批量删除">
				</td>
			</tr>
		</c:if>
		<table id="contentTable" class="table mainTable activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="6%">序号</th>
					<th width="6%">签证国家</th>
					<th width="6%">领区</th>
					<th width="10%">备注</th>
					<th width="8%">操作</th>
				</tr>
			</thead>
			<tbody>
			<c:if test="${fn:length(visaInterviewNoticeAddressList) > 0 }">
				<c:forEach items="${visaInterviewNoticeAddressList }" var="list" varStatus="s">
					<c:set var="country" value="${list.countryId }"/>
					<c:if test="${country!=oldCountry }">
						<c:set var="index" value="1"/>
					</c:if>
					<tr>
						<td class="tc"><input type="checkbox" listIds="${list.id }" onclick="allchk();" value="${list.id }" name="id" style="margin-top:0px;">&nbsp;&nbsp;${s.count}</td>
						<c:if test="${index==1 }">
							<td class="tc" rowspan="${list.ct }">${fns:getCountryName(list.countryId) }</td>
						</c:if>
						<td class="tc">${fns:getDictLabel(list.area,'from_area','') }</td>
						<td class="tc">${list.remark }</td>
						<td class="tc tda">
							<a href="javascript:void(0)" onClick="addVisaCountry('edit','${list.id }','${list.countryId }','${fns:getCountryName(list.countryId) }','${list.area }','${list.remark }');" name="editVisa">修改</a>
							<a href="javascript:void(0)" onClick="deleteVisaCountry('您确定要删除所选签证国家领区信息吗？','${list.countryId}','${list.area }');">删除</a>
						</td>
					</tr>
					<c:set var="oldCountry" value="${list.countryId }"/>
					<c:set var="index" value="${index+1 }"/>
				</c:forEach>
				</c:if>
				<c:if test="${fn:length(visaInterviewNoticeAddressList) <= 0 }">
					<tr class="toptr" >
						<td colspan="15" style="text-align: center;">暂无记录</td>
					</tr>
				</c:if>
				<c:if test="${fn:length(visaInterviewNoticeAddressList) > 0 }">
					<tr class="checkalltd">
						<td colspan='19' class="tl">
							<label>
								<input type="checkbox" name="allChk" onclick="checkall(this)">
								全选
							</label>
							<label>
								<input type="checkbox" name="allChkNo" onclick="checkallNo(this)">
								反选
							</label>
							<input class="btn-primary" type="button" onclick="batchDelete()" value="批量删除">
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</form>
</body>
</html>