<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
<title>基础信息维护-币种信息</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

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
<div id="sea">
<form:form id="selForm" action="${ctx}/sys/currency/saveDispStatus" method="post" >
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="currencyIds" type="hidden" name="currencyIds" value="${currencyIds}"/>
	<!--右侧内容部分开始-->
	<div class="filter_btn">
		<%--<a  class="btn btn-primary" onclick="newCurrency();" href="javascript:void(0)">+新增币种信息</a>--%>
		<input class="btn btn-primary ydbz_x" onclick="newCurrency();" value="新增币种信息" type="button">
	</div>
	<div class="tableDiv flight" style="width:100%;padding-left:0px;padding-top: 0px;">
	<table id="contentTable" class="table mainTable activitylist_bodyer_table">
				<thead>
					<tr>
						<th width="6%">排序</th>
						<th width="6%">符号</th>
						<th width="6%">币种</th>
						<th width="8%">日期</th>
						<th width="6%" class="table_borderLeftN">状态</th>
						<th width="8%">操作</th>
					</tr>
				</thead>
				<tbody>
		<c:forEach items="${page.list}" var="currency" varStatus="s">
			<tr>
				<td class="tc">
				<!-- 	<div  name="sortDiv">${currency.sort}</div>
					<input name ="sort" value ="${currency.sort}" style="display:none"/>  -->
					<input type="hidden" name="myCurrency" value ="${currency.id}">
					<input type="text" value="${currency.sort}" name="mySort" maxlength="4" class="maintain_sort" onkeyup="this.value=this.value.replace(/[^\d\+]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+]/g,'')" disabled="disabled"/>
				</td>
				<td class="tc">${currency.currencyMark}</td>
				<td class="tc">${currency.currencyName}</td>
				<td><fmt:formatDate value="${currency.updateDate}" pattern="yyyy-MM-dd"/></td>
				<td class="tc">
				<c:if test="${currency.displayFlag eq '1'}">
				<!--  	<a href="${ctx}/sys/currency/delete?id=${currency.id}" onClick="return confirmx('您确定关闭吗？', this.href)">
				-->	<span title="启用"><i class="basic_on"></i></span>
				</c:if>
				<c:if test="${currency.displayFlag eq '0'}">
				<!-- <a href="${ctx}/sys/currency/delete?id=${currency.id}" onClick="return confirmx('您确定启用吗？', this.href)">
				-->	<span title="停用"><i class="basic_off"></i></span>
				</c:if>
				</td>        
				<td class="tc"><a href="javascript:void(0)" onclick="modifyCurrency(${s.count},${currency.id});">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="${ctx}/sys/currency/delete?id=${currency.id}" onClick="return confirmx('您确认删除吗？', this.href)">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
			<dd>
				<!--<a href="javascript:void(0)" onclick="updateOrder(this);" class="ydbz_x">修改排序</a>-->
				<input class="btn btn-primary ydbz_x" onclick="updateOrder(this);" value="修改排序" type="button">


			</dd>
				<div class="endPage">${page}</div>
			</div>
		</div>
		<tags:message content="${message}"/>
	</c:if>
	<!--右侧内容部分结束-->
	</div>
</form:form>
</div>
<script type="text/javascript">

$(document).ready(function() {
	checkSub();
	
});
function viewCurrency(id){
	window.open( "${ctx}/sys/currency/viewCurrencyInfo?id="+id);
}
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

//基础信息维护-币种信息
function newCurrency(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
                html += '<dl class="bzxx">';
                html += '<dt><span><font color="red">*&nbsp;</font>币种：</span><input type="text" id="currencyName" name="currencyName" maxlength="20"/><span>状态：</span><input type="checkbox" name="enableId" id="enableId" value="1" checked="checked" style="width:20px;" /><span style="width:30px;">启用</span></dt>';
                html += '<dt><span><font color="red">*&nbsp;</font>符号：</span><input type="text" id="currencyMark" name="currencyMark" maxlength="10"/></dt>';
                html += '<dt><span>排序：</span><input type="text" id="sort" name="sort" maxlength="5" onkeyup="refundInputs(this)"  onafterpaste="refundInputs(this)" /></dt>';
                html += '<dt><span>描述：</span><textarea id="remark" name="remark" maxlength="10" rows="3"></textarea></dt>';
                html += '</dl>';
                html += '</div>';
	$.jBox(html, { title: "新增币种信息",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
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
			var currencyMark = $.trim(f.currencyMark);
			if(currencyMark == ""){
				top.$.jBox.tip('符号不能为空', 'error', { focusId: 'currencyMark' }); 
				return false;
			}
			
			if(!isNotContainNum(currencyMark)){
				top.$.jBox.tip('符号中不能包含数字', 'error', { focusId: 'currencyMark' }); 
				return false;
			}
			// 排序
			var sort = $.trim(f.sort);
			if(sort == ""){
				top.$.jBox.tip('排序不能为空', 'error', { focusId: 'sort' }); 
				return false;
			}
			
			if(!checkSortNum(sort)){
				top.$.jBox.tip('排序为1-99999之间整数！', 'error', { focusId: 'sort' }); 
				return false;
			}
			var remark=$.trim(f.remark);
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
			var display_flag= 0;
			if($("#enableId").attr("checked")){
				display_flag = 1;
			}		
			var jsonRes = {
				id : null,
				currencyName : currencyName, 
				currencyMark : currencyMark, 
				sort:sort,
				displayFlag:display_flag,
				remark:remark
				};
			$.ajax({
				type: "POST",
				url: "${ctx}/sys/currency/saveSuperCurrency",
				cache:false,
				dataType:"json",
				async:false,
				data:jsonRes,
				success: function(array){
					top.$.jBox.tip('增加成功','success');
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
//查看币种详情
function viewCurrencyInfo(){

}

//基础信息维护-币种信息
function modifyCurrency(lineNo,currencyId){
	$.ajax({
		type:"GET",
		async : false,
		url:"${ctx}/sys/currency/findCurrencyById?id=" + currencyId,
		success:function(result){
		   var checkStr='' ;
		   if(result.currency.displayFlag == 1){
		   	checkStr=' checked="checked" ';
		   }
			var html = '<div style=" padding:20px; overflow:hidden;">';
                html += '<dl class="bzxx">';
                html += '<dt><span><font color="red">*&nbsp;</font>币种：</span><input type="text" id="currencyName" name="currencyName" value="'+result.currency.currencyName+'" maxlength="20"/><span>状态：</span><input type="checkbox" name="enableId" id="enableId" value="1" '+checkStr+' style="width:20px;" /><span style="width:30px;">启用</span></dt>';
                html += '<dt><span><font color="red">*&nbsp;</font>符号：</span><input type="text" id="currencyMark" name="currencyMark" value="'+result.currency.currencyMark+'" maxlength="10"/></dt>';
                html += '<dt><span>排序：</span><input type="text" id="sort" name="sort" value="'+result.currency.sort+'" maxlength="5" onkeyup="refundInputs(this)"  onafterpaste="refundInputs(this)" /></dt>';
                html += '<dt><span>描述：</span><textarea id="remark" name="remark" maxlength="10" rows="3">'+result.currency.remark+'</textarea></dt>';
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
				// 排序
			var sort = $.trim(f.sort);
			if(sort == ""){
				top.$.jBox.tip('排序不能为空', 'error', { focusId: 'sort' }); 
				return false;
			}
			
			if(!checkSortNum(sort)){
				top.$.jBox.tip('排序为1-99999之间整数！', 'error', { focusId: 'sort' }); 
				return false;
			}
			var remark=$.trim(f.remark);
				if(checkSameFlg){
					return false;
				}
			var display_flag= 0;
			if($("#enableId").attr("checked")){
				display_flag = 1;
			}
				var jsonRes = {id : currencyId,
					currencyName : currencyName, 
					currencyMark : currencyMark, 
					sort:sort,
					displayFlag:display_flag,
					remark:remark
					};
				$.ajax({
					type: "POST",
					url: "${ctx}/sys/currency/saveSuperCurrency",
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
function checkSortNum(oNum){
	if(parseInt(oNum) > 100000 || 0 >= parseInt(oNum)){
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

function saveDispStatus(){
	$("#selForm").attr("action","${ctx}/sys/currency/saveDispStatus");
	$("#selForm").submit();
}
function updateOrder(obj){
	var txt=$(obj).text();
	if(txt=="修改排序"){
		$(obj).text("提交");
		$("input[class='maintain_sort']").removeAttr("disabled");
	}else{
		if(updateSort()){
			$(obj).text("修改排序");
			$("input[class='maintain_sort']").attr("disabled","disabled");
		}
		
	}
}
function refundInputs(obj){
	   objs=obj.value;
	   objs=objs.replace(/[^\d\+]/g,'');
	   $(obj).val(objs);	   
}
function updateSort(){
	var sortInput = $("input[name='mySort']");
	var checkSign = 0;
	var currencyIds =[];
	var mySort = [];
	sortInput.each(function(index, element) {
	var si = $(sortInput[index]);
    var s = si.val();
    var currencyId = si.parent().find("input[name='myCurrency']").val();
    currencyIds.push(currencyId);
    mySort.push(s);
    if("" == s || undefined == s || s <= 0 ){
    	checkSign = 1;
    }
	});
	if(checkSign == 1){
		top.$.jBox.tip('排序错误!', 'error', { focusId: 'currencyMark' });
		return false;
	}else{
	
		$.ajax({
					type: "POST",
					url: "${ctx}/sys/currency/saveSuperCurrencySort",
					cache:false,
					dataType:"json",
					async:false,
					data:{
					currencyIds:currencyIds.join(','),
					sort : mySort.join(',')
					},
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
	return true;
}
</script>

</body>
</html>
