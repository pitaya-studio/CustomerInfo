<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-币种信息</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<style>
html{overflow: auto; overflow-x:hidden}
</style>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">currency</page:param>
</page:applyDecorator>
<form:form id="selForm" action="${ctx}/sys/currency/saveDispStatus" method="post" >
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="currencyIds" type="hidden" name="currencyIds" value="${currencyIds}"/>
	<!--右侧内容部分开始-->
		<select name="currencyTemplate" id="currencyTemplate"
			style="display:none;">
			<c:forEach items="${cList}" var="cur" varStatus="b">
				<option value="${cur.id }">${cur.currencyName }</option>
			</c:forEach>
		</select><input type="hidden" id ="srcMyCurrencyMark" value ="${cList[0].currencyMark}"/>
		<div class="formmoney"> 
          <b>当前每</b><input type="text" onkeyup="priceInputin(this)" value="0" maxlength="8" onblur="priceInputin(this)" onafterpaste="priceInputin" class="red srcPrice"/><b>元</b>
          <select name ="srcCurrency">
          <c:forEach items="${page.list}" var="cur" varStatus="b">
			<option value="${cur.id }">${cur.currencyName }</option>
		 </c:forEach>
          </select>
          
          <b>可兑换</b><a class="ydbz_x fn">换算</a>
        </div>
        <!--查询结果筛选条件排序开始-->
        <div class="filter_btn"> <a  class="btn btn-primary" onclick="newCurrency();" href="javascript:void(0)">+新增币种信息</a> </div>
	<div class="tableDiv flight" style="width:100%;padding-left:0px;padding-top: 0px;">
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
                            <tr>
                                <th width="6%">序号</th>
					              <th width="6%">符号</th>
					              <th width="6%">币种</th>
					              <th width="6%">排序</th>
					              <th width="8%">日期</th>
					              <th width="10%"><span class="tdred">现金收款<br/>
					                可兑换</span></th>
					              <th width="10%">现金收款<br/>
					                换汇汇率</th>
					              <th width="10%">对公收款<br/>
					                换汇汇率</th>
					              <th width="10%">中行折算价<br/>
					                基准价</th>
					              <th width="10%">公司最低<br/>
					                汇率标准</th>
					             
                                <th width="5%" class="table_borderLeftN">状态</th>
								<th width="9%">操作</th> 
                            </tr>
                            </thead>
		<tbody>
		<c:forEach items="${page.list}" var="currency" varStatus="s">
			<tr>
				<td>${s.count}</td>
				<td class="tc">${currency.currencyMark}</td>
				<td class="tc">${currency.currencyName}</td>
				<td class="tc">${currency.sort}</td>
				<td><fmt:formatDate value="${currency.updateDate}" pattern="yyyy-MM-dd"/></td>
				<td class="red tr ">
				<div name="myConvert"></div>
				<input name ="currencyId" type="hidden" value="${currency.id}"/>
				</td>
				<td class="tr"><fmt:formatNumber value="${currency.convertCash}" type="NUMBER" maxFractionDigits="4"/></td>
				<td class="tr"><fmt:formatNumber value="${currency.convertForeign}" type="NUMBER" maxFractionDigits="4"/></td>
				<td class="tr"><fmt:formatNumber value="${currency.convertAbc}" type="NUMBER" maxFractionDigits="4"/></td>
				<td class="tr"><fmt:formatNumber value="${currency.convertLowest}" type="NUMBER" maxFractionDigits="4"/></td> 
				<td class="table_borderLeftN">
				<c:if test="${currency.displayFlag eq '1'}">
				<span title="启用"><i class="basic_on"></i></span>
				</c:if>
				<c:if test="${currency.displayFlag eq '0'}">
				<span title="停用"><i class="basic_off"></i></span>
				</c:if>
				</td>       
				<td class="tc"><a href="javascript:void(0)" onclick="modifyCurrency(${s.count},${currency.id});">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${!(fns:isHuaerUser()||fns:isMtourUser())}">
<%-- 					<a href="${ctx}/sys/currency/delete?id=${currency.id}" onClick="return confirmx('您确认删除吗？', this.href)">删除</a>&nbsp;&nbsp;&nbsp;&nbsp; --%>
					<a href="javascript:void(0)" onClick="checkCurrency(${currency.id})">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
					<a href="javascript:void(0)" onclick="viewCurrency(${currency.id});">详情</a>
				</td>
			</tr>
		</c:forEach>	
		</tbody>
	</table>
	<c:if test="${page.count eq 0 }">
		<div class="wtjqw">无币种信息</div>
	</c:if>
	<c:if test="${page.count ne 0 }">
		<div class="page">
			<div class="pagination">
				<div class="endPage">${page}</div>
			</div>
		</div>
		<tags:message content="${message}"/>
	</c:if>
	<!--右侧内容部分结束-->
	</div>
</form:form>
<script type="text/javascript">

function checkCurrency(id){

	$.ajax({
		type: "POST",
		url:  "${ctx}/sys/currency/checkCurrency",
		dataType: "json",
		data : {
				id : id,
		},
		success:function(msg){
			if(msg == '1') {
				$.jBox.confirm("您确认删除吗？", "系统提示", function (v, h, f) {
					if (v == 'ok') {
//						$.jBox.tip("nima");
						window.location.href = "${ctx}/sys/currency/delete?id=${currency.id}";	
					} else if (v == 'cancel') {}
				});
//				return $.jBox.confirm('您确认删除吗？', obj.href);
			} else {
				$.jBox.tip("此币种有引用,您不能删除！"); 
			}
		 }
	});
};

$(document).ready(function() {
	checkSub();
});
var currencyObj = {};

function checkAll(obj){
	if($(obj).attr("checked")){
		$("input[name='allChk']").attr("checked",'true');
		$("input[name='activityId']").attr("checked",'true');
	} else {
		$("input[name='allChk']").removeAttr("checked");
		$("input[name='activityId']").removeAttr("checked");
	}
}

function checkSub(){
	var subBox = $("input[name='activityId']");
	var checkedBoxs = $("input[name='activityId']:checked");
	if(subBox.length == checkedBoxs.length){
		$("input[name='allChk']").attr("checked", 'true');	
	} else {
		$("input[name='allChk']").removeAttr("checked");
	}
}
function viewCurrency(id){
	window.open( "${ctx}/sys/currency/viewCurrencyInfo?id="+id);
}


//基础信息维护-币种信息
function newCurrency(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<dl class="bzxx">';
//	html += '<dt><span><font color="red">*&nbsp;</font>币种：</span><select id ="currencyId" onchange="changeCurrency(this);" name ="currencyId">{0}<select>&nbsp;&nbsp;&nbsp;&nbsp;<span></span><input type="checkbox" name="defaultCurrencyId" id="defaultCurrencyId" style="width:20px;"/>设为默认币种</dt>'.replace("{0}",$("#currencyTemplate").html());
	html += '<dt><span><font color="red">*&nbsp;</font>币种：</span><select id ="currencyId" onchange="changeCurrency(this);" name ="currencyId">{0}</select></dt>'.replace("{0}",$("#currencyTemplate").html());
	html += '<dt><span><font color="red">*&nbsp;</font>符号：</span><span id="currencyMarkxinz" style="width:130px;text-align:left;padding-left:6px;">{1}</span><span><font color="red">*&nbsp;</font>汇率：</span><input type="text" id="currencyExchangerate" name="currencyExchangerate" maxlength="12"/></dt>'.replace("{1}",$("#srcMyCurrencyMark").val());
	html += '<dt><span>换汇汇率：</span><span id="sort" style="width:130px;text-align:left;padding-left:6px;"></span><span><font color="red">*&nbsp;</font>排序：</span><input type="text" id="sort" name="sort" maxlength="5"/></dt>';//保持和superadmin一样的规则,最大输入长度为5位数
	html += '<dt><span>现金收款：</span><input type="text" id="convertCash" name="convertCash" maxlength="12"/><span>对公收款：</span><input type="text" id="convertForeign" name="convertForeign" maxlength="12"/></dt>';
	html += '<dt><span>中行折算价：</span><input type="text" id="convertAbc" name="convertAbc" maxlength="12"/></dt>';
	html += '<dt><span><font color="red">*&nbsp;</font>最低汇率标准：</span><input type="text" id="convertLowest" name="convertLowest" maxlength="12"/><span>状态：</span><input type="checkbox" name="enableId" id="enableId" checked="checked" style="width:20px;" /><span style="width:30px;">启用</span></dt>';
	html += '</dl>';
	html += '</div>';
	$.jBox(html, { title: "新增币种信息",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
			// 币种
			if(undefined == f.currencyId || "" == f.currencyId){
				top.$.jBox.tip('当前没有可选币种', 'error', { focusId: 'currencyName' }); 
				return false;
			}
			var currencyName = $.trim(currencyObj[f.currencyId].currencyName);
			if(currencyName == ""){
				top.$.jBox.tip('币种不能为空', 'error', { focusId: 'currencyName' }); 
				return false;
			}
			if(!isValidName(currencyName)){
				top.$.jBox.tip('币种只能为中文字符', 'error', { focusId: 'currencyName' }); 
				return false;
			}
			
			var checkSameFlg = false;
			$.ajax({
				type: "POST",
				url: "${ctx}/sys/currency/check",
				cache:false,
				dataType:"json",
				async:false,
				data:{id : null,
					currencyName : currencyName},
				success: function(data){
					if(data.flag == "error"){
						top.$.jBox.tip('币种重复', 'error', { focusId: 'currencyName' });
						checkSameFlg = true;
						return false;
					}
				},
				error : function(e){
					top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
					return false;
				}
			 });
			
			if(checkSameFlg){
				return false;
			}
			// 符号
			var currencyMark = $.trim(currencyObj[f.currencyId].currencyMark);
			if(currencyMark == ""){
				top.$.jBox.tip('符号不能为空', 'error', { focusId: 'currencyMark' }); 
				return false;
			}
			
			if(!isNotContainNum(currencyMark)){
				top.$.jBox.tip('符号中不能包含数字', 'error', { focusId: 'currencyMark' }); 
				return false;
			}
			
			checkSameFlg = false;
			$.ajax({
				type: "POST",
				url: "${ctx}/sys/currency/checkCurrencyMark",
				cache:false,
				dataType:"json",
				async:false,
				data:{id : null,
					currencyMark : currencyMark},
				success: function(data){
					if(data.flag == "error"){
						top.$.jBox.tip('币种符号重复', 'error', { focusId: 'currencyMark' });
						checkSameFlg = true;
						return false;
					}
				},
				error : function(e){
					top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyMark' });
					return false;
				}
			 });
			
			if(checkSameFlg){
				return false;
			}

			// 汇率
			var currencyExchangerate = $.trim(f.currencyExchangerate);
			if(currencyExchangerate == ""){
				top.$.jBox.tip('汇率不能为空', 'error', { focusId: 'currencyExchangerate' }); 
				return false;
			}
			if(currencyExchangerate.length > 0 && !isNumber(currencyExchangerate)){
				top.$.jBox.tip('汇率必须为数字', 'error', { focusId: 'currencyExchangerate' }); 
				return false;
			}

			// 现金收款
			var convertCash = $.trim(f.convertCash);
			if(convertCash.length > 0 && !isNumber(convertCash)){
				top.$.jBox.tip('现金收款必须为数字', 'error', { focusId: 'convertCash'}); 
				return false;
			}

			// 对公收款
			var convertForeign = $.trim(f.convertForeign);
			if(convertForeign.length > 0 && !isNumber(convertForeign)){
				top.$.jBox.tip('对公收款必须为数字', 'error', { focusId: 'convertForeign' }); 
				return false;
			}

			// 中行折算价
			var convertAbc = $.trim(f.convertAbc);
			if(convertAbc.length > 0 && !isNumber(convertAbc)){
				top.$.jBox.tip('中行折算价必须为数字', 'error', { focusId: 'convertAbc' }); 
				return false;
			}
            //0365-qyl-begin---排序校验
            var sort1=$.trim(f.sort);
            if(sort1 ==""){
            	top.$.jBox.tip('排序不能为空', 'error', { focusId: 'sort' }); 
				return false;
            }
            if(!isNotContainZheng(sort1)){
				top.$.jBox.tip('输入格式不正确，请输入正整数', 'error', { focusId: 'sort' }); 
				return false;
			}
          //0365-qyl-end---排序
			// 最低汇率标准
			var convertLowest = $.trim(f.convertLowest);
			if(convertLowest == ""){
				top.$.jBox.tip('最低汇率标准不能为空', 'error', { focusId: 'convertLowest' }); 
				return false;
			}
			if(convertLowest.length > 0 && !isNumber(convertLowest)){
				top.$.jBox.tip('最低汇率标准必须为数字', 'error', { focusId: 'convertLowest' }); 
				return false;
			}
			
			if(!checkNumRange(currencyExchangerate)){
				top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'currencyExchangerate' }); 
				return false;
			}
			if(!checkNumRange(convertCash)){
				top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertCash' }); 
				return false;
			}
			if(!checkNumRange(convertForeign)){
				top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertForeign'}); 
				return false;
			}
			if(!checkNumRange(convertAbc)){
				top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertAbc' }); 
				return false;
			}
			if(!checkNumRange(convertLowest)){
				top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertLowest' }); 
				return false;
			}
			var display_flag= 0;
			if($("#enableId").attr("checked")){
				display_flag = 1;
			}
			var sort = currencyObj[f.currencyId].sort;
			if(currencyObj[f.currencyId].currencyName == "人民币"){
				sort = 0;
				if(display_flag == 0){
					top.$.jBox.tip('默认币种人民币不能被停用！', 'error', { focusId: 'convertLowest' });
					return false;
				}
			}
//			if($("#defaultCurrencyId").attr("checked")){
//				sort = 0;
//			}
			var jsonRes = {
				id : null,
				currencyName : currencyName, 
				currencyMark : currencyMark, 
				currencyExchangerate : currencyExchangerate,
				convertCash : convertCash,
				convertForeign : convertForeign,
				convertAbc : convertAbc,
				convertLowest : convertLowest,
				sort : sort1,
				displayFlag:display_flag
				};
				
					$.ajax({
								type: "POST",
								url: "${ctx}/sys/currency/save",
								cache:false,
								dataType:"json",
								async:false,
								data:jsonRes,
								success: function(data){
									top.$.jBox.tip('添加成功！','success');
									window.location.href = "${ctx}/sys/currency/list";
								},
								error : function(e){
									top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
									return false;
								}
							 });

		}
	},height:320,width:530});
}

function replaceNull(val){
	if(val == null){
		return "";
	}
	
	return val;
}


//基础信息维护-币种信息
function modifyCurrency(lineNo,currencyId){
	$.ajax({
		type:"GET",
		async : false,
		cache : false,
		url:"${ctx}/sys/currency/findCurrencyById?id=" + currencyId,
		success:function(result){
			var checkStr1 = '' ;
			 if(result.currency.displayFlag == 1){
			  checkStr1 =' checked="checked" ';
			 }
		   	var checkStr2 = '';
		   	if(result.currency.sort == 0){
		   		checkStr2 =' checked="checked" ';
		   	}
			var html = '<div style=" padding:20px; overflow:hidden;">';
			html += '<dl class="bzxx">';
//			html += '<dt><span><font color="red">*&nbsp;</font>币种：</span><input type="text" id="currencyName" readonly ="readonly" name="currencyName" value="'+result.currency.currencyName+'" maxlength="20"/><span></span><input type="checkbox" name="defaultCurrencyId" '+checkStr2+' id="defaultCurrencyId" style="width:20px;"/>设为默认币种</dt>';
			html += '<dt><span><font color="red">*&nbsp;</font>币种：</span><input type="text" id="currencyName" readonly ="readonly" name="currencyName" value="'+result.currency.currencyName+'" maxlength="20"/></dt>';
			html += '<dt><span><font color="red">*&nbsp;</font>符号：</span><input type="text" id="currencyMark" readonly ="readonly" name="currencyMark" value="'+result.currency.currencyMark+'" maxlength="10"/><span><font color="red">*&nbsp;</font>汇率：</span><input type="text" id="currencyExchangerate" class="inputTxt" name="currencyExchangerate" value="'+replaceNull(result.currency.currencyExchangerate)+'" maxlength="12"/></dt>';
			html += '<dt><span>换汇汇率：</span> <span id="sort" style="width:130px;text-align:left;padding-left:6px;"></span><span><font color="red">*&nbsp;</font>排序：</span><input type="text" id="sort" name="sort" value="'+replaceNull(result.currency.sort)+'" maxlength="5"/></dt>';//保持和superadmin一样的规则,排序最大长度为5位
			html += '<dt><span>现金收款：</span><input type="text" id="convertCash" name="convertCash" value="'+replaceNull(result.currency.convertCash)+'" maxlength="12"/><span>对公收款：</span><input type="text" id="convertForeign" name="convertForeign" value="'+replaceNull(result.currency.convertForeign)+'" maxlength="12"/></dt>';
			html += '<dt><span>中行折算价：</span><input type="text" id="convertAbc" name="convertAbc" value="'+replaceNull(result.currency.convertAbc)+'" maxlength="12"/></dt>';
			html += '<dt><span><font color="red">*&nbsp;</font>最低汇率标准：</span><input type="text" id="convertLowest" name="convertLowest" value="'+replaceNull(result.currency.convertLowest)+'" maxlength="12"/><span>状态：</span><input type="checkbox" name="enableId" id="enableId"  '+checkStr1+' style="width:20px;" /><span style="width:30px;">启用</span></dt>';
			html += '</dl>';
			html += '</div>';

			$.jBox(html, { title: "修改币种信息",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
			if (v=="1"){
				// 币种
				var currencyName = $.trim(f.currencyName);
				if(currencyName == ""){
					top.$.jBox.tip('币种不能为空', 'error', { focusId: 'currencyName' }); 
					return false;
				}
				if(!isValidName(currencyName)){
					top.$.jBox.tip('币种只能为中文字符', 'error', { focusId: 'currencyName' }); 
					return false;
				}
				
				var checkSameFlg = false;
				$.ajax({
					type: "POST",
					url: "${ctx}/sys/currency/check",
					cache:false,
					dataType:"json",
					async:false,
					data:{id : currencyId,
						currencyName : currencyName},
					success: function(data){
						if(data.flag == "error"){
							top.$.jBox.tip('币种重复', 'error', { focusId: 'currencyName' });
							checkSameFlg = true;
							return false;
						}
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
						return false;
					}
				 });
				
				if(checkSameFlg){
					return false;
				}

				// 符号
				var currencyMark = $.trim(f.currencyMark);
				if(currencyMark == ""){
					top.$.jBox.tip('符号不能为空', 'error', { focusId: 'currencyMark' }); 
					return false;
				}
				
				if(!isNotContainNum(currencyMark)){
					top.$.jBox.tip('符号中不能包含数字', 'error', { focusId: 'currencyMark' }); 
					return false;
				}
				
				checkSameFlg = false;
				$.ajax({
					type: "POST",
					url: "${ctx}/sys/currency/checkCurrencyMark",
					cache:false,
					dataType:"json",
					async:false,
					data:{id : currencyId,
						currencyMark : currencyMark},
					success: function(data){
						if(data.flag == "error"){
							top.$.jBox.tip('币种符号重复', 'error', { focusId: 'currencyMark' });
							checkSameFlg = true;
							return false;
						}
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyMark' });
						return false;
					}
				 });
				
				if(checkSameFlg){
					return false;
				}
				// 汇率
				var currencyExchangerate = $.trim(f.currencyExchangerate);
				if(currencyExchangerate == ""){
					top.$.jBox.tip('汇率不能为空', 'error', { focusId: 'currencyExchangerate' }); 
					return false;
				}
				if(currencyExchangerate.length > 0 && !isNumber(currencyExchangerate)){
					top.$.jBox.tip('汇率必须为数字', 'error', { focusId: 'currencyExchangerate' }); 
					return false;
				}

				// 现金收款
				var convertCash = $.trim(f.convertCash);
				if(convertCash.length > 0 && !isNumber(convertCash)){
					top.$.jBox.tip('现金收款必须为数字', 'error', { focusId: 'convertCash'}); 
					return false;
				}

				// 对公收款
				var convertForeign = $.trim(f.convertForeign);
				if(convertForeign.length > 0 && !isNumber(convertForeign)){
					top.$.jBox.tip('对公收款必须为数字', 'error', { focusId: 'convertForeign' }); 
					return false;
				}
				 //0365-qyl-begin---排序校验
	            var sortModified=$.trim(f.sort);
	            if(sortModified ==""){
	            	top.$.jBox.tip('排序不能为空', 'error', { focusId: 'sort' }); 
					return false;
	            }
	            if(!isNotContainZheng(sortModified)){
					top.$.jBox.tip('输入格式不正确，请输入正整数', 'error', { focusId: 'sort' }); 
					return false;
				}
	          //0365-qyl-end---排序
				// 中行折算价
				var convertAbc = $.trim(f.convertAbc);
				if(convertAbc.length > 0 && !isNumber(convertAbc)){
					top.$.jBox.tip('中行折算价必须为数字', 'error', { focusId: 'convertAbc' }); 
					return false;
				}

				// 最低汇率标准
				var convertLowest = $.trim(f.convertLowest);
				if(convertLowest == ""){
					top.$.jBox.tip('最低汇率标准不能为空', 'error', { focusId: 'convertLowest' }); 
					return false;
				}
				if(convertLowest.length > 0 && !isNumber(convertLowest)){
					top.$.jBox.tip('最低汇率标准必须为数字', 'error', { focusId: 'convertLowest' }); 
					return false;
				}
				
				if(!checkNumRange(currencyExchangerate)){
					top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'currencyExchangerate' }); 
					return false;
				}
				if(!checkNumRange(convertCash)){
					top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertCash' }); 
					return false;
				}
				if(!checkNumRange(convertForeign)){
					top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertForeign'}); 
					return false;
				}
				if(!checkNumRange(convertAbc)){
					top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertAbc' }); 
					return false;
				}
				if(!checkNumRange(convertLowest)){
					top.$.jBox.tip('数值不能大于99999999.999', 'error', { focusId: 'convertLowest' }); 
					return false;
				}
				var display_flag= 0;
			if($("#enableId").attr("checked")){
				display_flag = 1;
			}
			var sort = currencyObj[currencyId].sort;
			if(currencyObj[currencyId].currencyName == "人民币"){
				sort = 0;
				if(display_flag == 0){
					top.$.jBox.tip('默认币种人民币不能被停用！', 'error', { focusId: 'convertLowest' });
					return false;
				}
			}
//			if($("#defaultCurrencyId").attr("checked")){
//				sort = 0;
//			}
			var jsonRes = {
				id : currencyId,
				currencyName : currencyName, 
				currencyMark : currencyMark, 
				currencyExchangerate : currencyExchangerate,
				convertCash : convertCash,
				convertForeign : convertForeign,
				convertAbc : convertAbc,
				convertLowest : convertLowest,
				sort : sortModified,
				displayFlag:display_flag
				};
				
					$.ajax({
								type: "POST",
								url: "${ctx}/sys/currency/save",
								cache:false,
								dataType:"json",
								async:false,
								data:jsonRes,
								success: function(data){
									top.$.jBox.tip('修改成功','success');
									window.location.href = "${ctx}/sys/currency/list";
								},
								error : function(e){
									top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
									return false;
								}
							 });
			}
			},height:320,width:530});
		},
		error : function(e){
			top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
			return false;
		}
	});
}

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#selForm").attr("action","${ctx}/sys/currency/list");
	$("#selForm").submit();
}

/**
 * 判定输入项为汉字
 */
function isValidName(strName){
    var str = strName;   
	  var reg = /[^\u4e00-\u9fa5]/;  
    if(reg.test(str)){
     return false;
    }
    return true;
}

/**
 * 不大于decimal(11,3)的数字
 */
function checkNumRange(oNum){
	if(parseFloat(oNum) > 99999999.999){
		return false;
	}
	
	return true;
}

/**
 * 必须为数字或小数点的校验
 */
function isNumber(oNum) {
	if(!oNum) return false;
	var strP=/^\d+(\.\d+)?$/;
	if(!strP.test(oNum)) return false;
	try{
		if(parseFloat(oNum)!=oNum) return false;
	}catch(ex){
		return false;
	}
	return true;
}

function isNotContainNum(str){
	var strP=/\d/gi;
	if(strP.test(str)) {
		return false;
	}
	return true;
}
/**
 * 只能输入大于0的整数-0365-qyl
 */
 function isNotContainZheng(str){  
	var reg =/^[1-9]+[0-9]*]*$/;
   if(!reg.test(str)){
    return false;
   }
   return true;
	
} 

function saveDispStatus(){
	$("#selForm").attr("action","${ctx}/sys/currency/saveDispStatus");
	$("#selForm").submit();
}
function priceInputin(obj){
		 var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
         var txt = ms.split(".");
         obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
   }
   function loadCurrency(){
   $.ajax({
		  type: "GET",
		  url: "${ctx}/sys/currency/currencyJson",
		  dataType: "json",
		  async: false,
		  success:function(msg){
			  if(msg && msg.currencyList){
			      $.each(msg.currencyList, function(i,n){
			         currencyObj[n.id] = n;
			      });
			  }
		  }
		  
		});
	}
	
   $(function(){
	   loadCurrency();
	   init();
   });
   function init(){
	$(".ydbz_x").click(function() {
	   		var selects=$("div[name='myConvert']");
	   		var priceSrc = $(".srcPrice").val();
	   		if(undefined == priceSrc || "" == priceSrc){
	   		priceSrc = 0;
	   		}
	   		var currencySrc = $("select[name='srcCurrency']").val();
	   		selects.each(function(index, element) {
	   			var si = $(selects[index]);
	   			var currencyId = si.parent().find("input[name='currencyId']").val();
	   			var nowCurrency = currencyObj[currencyId].convertLowest;
	   			var srcCurrency = currencyObj[currencySrc].convertLowest;
	   			if(undefined == nowCurrency || "" == nowCurrency){
		   		nowCurrency = 0;
		   		}
		   		if(undefined == srcCurrency || "" == srcCurrency){
		   		srcCurrency = 0;
		   		}
	   			if(nowCurrency == srcCurrency && srcCurrency != 0 && nowCurrency != 0){
	   				si.html(milliFormat(priceSrc,'1'));
	   			}else{
	   				if(priceSrc !=0 && srcCurrency != 0 && nowCurrency != 0){
	   				var src=priceSrc/nowCurrency*srcCurrency;
	   				si.html(milliFormat(src,'1'));
	   				}else{
	   				si.html('0.00');
	   				}
	   				
	   			}
	   			
	   		});
	   });
	};
	function changeCurrency(obj){
//		$("#currencyMark").val(currencyObj[obj.value].currencyMark);
		$('#currencyMarkxinz').html(currencyObj[obj.value].currencyMark);
	};
	
</script>
</body>
</html>
