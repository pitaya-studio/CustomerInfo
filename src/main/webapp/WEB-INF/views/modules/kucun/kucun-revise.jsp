<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>修改切位</title>
<meta name="decorator" content="wholesaler"/>
<link href="${ctxStatic}/common/Pager.css" rel="stylesheet" />
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.pager.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/pageHandler/jquery.pagerhandler.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
   $('.attend_up').click(function() {
	var vals = parseInt($(this).siblings('.duration').val());
	if (vals>=0) {
		$(this).siblings('.duration').val(vals + 1);
	} else {
		$(this).siblings('.duration').val(0)
	}
})
$('.attend_down').click(function(){	
	var vals = parseInt($(this).siblings('.duration').val());
	if (vals>0) {
		$(this).siblings('.duration').val(vals - 1);
	} else {
		$(this).siblings('.duration').val(0)
	}

});
 })
 $().ready(function() {
 	$("#reviseForm").validate({
 		errorPlacement: function(error, element) { 
    if ( element.is(":radio") ) 
        error.appendTo ( element.parent() ); 
    else if ( element.is(":checkbox") ) 
        error.appendTo ( element.parent() ); 
    else if ( element.is("input") ) 
        error.appendTo ( element.parent() ); 
    else 
        error.insertAfter(element); 
	} 
 	});
 	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		digits:"请输入正确的数字",
		  		number : "请输入正确的数字价格"
   			});
   	jQuery.validator.addMethod("intCheck", function(value, element) {       
    	var befor = ${groupReserve.payReservePosition};
  		var after = $("input.duration").val();
  		var cha = after-befor;
  		var freePosition = ${activitygroup.freePosition };
  		if(cha>freePosition){
  			return false;
  		}
  		return true;
 	}, "余位数不足");   
	jQuery.validator.addMethod("isZero", function(value, element) {   
	    return this.optional(element) || !($(element).val() == 0)
	}, "若无切位人数,请"+" <a href=\"${ctx}/stock/manager/delete?id=${param.id}&agentId=${param.agentId }&groupreserveId=${param.groupreserveId}\">删除</a>");  
})
   </script>

<style type="text/css">
#store td{padding:4px 12px;text-align:left;font:12px Arial,'微软雅黑,宋体';}
#store th{padding:12px;text-align:left;color:#fff;font-size:12px;font-weight:bold;}
#teamTable{ margin-bottom:8px;}
a.attend_down,a.attend_up{display:inline-block;border-radius:4px;width:20px;text-align:center;background:#62AFE7;color:#fff;text-decoration:none;height: 26px;line-height: 26px;}
.attend_down:hover,.attend_up:hover{text-decoration:none; color:#fff;}
.stock,.input_textarea,.duration{background:#FFF;border:1px solid #CCC;border-radius:4px;box-shadow:0 1px 1px rgba(0, 0, 0, 0.075) inset;color:#333;cursor:text;height:26px;line-height:26px;padding:0 5px;width:50px;}
#btnSubmit,#btnCancel{background:#62AFE7;border:none;box-shadow:none;font:12px Arial,'宋体'; padding:4px 16px;text-shadow:none;}
#btnCancel{background:#CCC;}
.sub_actions{text-align:center;}
.line-name{background-color: #F5F5F5;border-radius: 4px;list-style: none outside none;margin: 0 0 20px;padding:8px 15px 5px 10px;}
.line-shop{ width:80%;}
.line-shop li{ display:inline;font:12px '微软雅黑'; padding-right:20px;}
.line-shop label{display:inline-block;}
</style>
</head>
<body>
	
  <ul class="nav nav-tabs">
            <shiro:hasPermission name="inventory:warning:view">
            <li><a href="${ctx}/warning/showwarningList.htm">产品库存警告设置</a></li>
            <li><a href="${ctx}/warning/showAddForm">添加产品库存警告</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="stock:manager:view">
            <li><a href="${ctx}/stock/manager/">库存查询</a></li>
            <li><a href="${ctx}/stock/manager/detail?id=${param.id}&agentId=${param.agentId }">库存详情</a></li>
            </shiro:hasPermission>
    		 <li class="active"><a href="${ctx}/stock/manager/revise?id=${param.id }&groupreserveId=${groupReserve.id }&agentId=${param.agentId }">修改切位</a></li>
	</ul>
	<form:form id="Form" modelAttribute="travelActivity"
			action="${ctx}/activity/manager/save" method="post"
			class="breadcrumb form-search" enctype="multipart/form-data">
			<tags:message content="${message}" />
   <div class="line-name">
   	<ul class="line-shop">
   		<li><label>线路名称：</label><span><strong style="font-weight:500">${travelActivity.acitivityName }</strong></span></li>
   		
         <li><label>占位渠道：</label>
         			<span><c:forEach items="${fns:getAgentList()}" var="agent" varStatus="idxStatus">
                         <c:if test="${agent.id==param.agentId }">${agent.agentName }</c:if>
                 	</c:forEach></span></li>
   	</li>
   </div>
   </form:form>
  
   <form id="reviseForm" action="${ctx}/stock/manager/update" method="post" >
	<table id="store" class="table table-striped table-bordered table-condensed">
      <thead style="background:#403738">
         <tr>
            <th>出团日期</th>
            <th>订单</th>
            <th>申请人数</th>
            <th>余位</th>
            <th>成人同行价</th>
            <th>儿童同行价</th>
            <th>单房差</th>
            <th>预收</th>
            <th>备注</th>
         </tr>
      </thead>
     
      <tbody>
         <tr>
            <td><span><fmt:formatDate pattern="yyyy-MM-dd"  value="${activitygroup.groupOpenDate}"/></span></td>
            <td><input type="text" class="stock required" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" style="margin-bottom:0;" name="frontMoney" value="${groupReserve.frontMoney}" ><span style=" padding-left:5px;">元/人</span></td>
            <td><a href="javascript:void(0)" class="attend_down">‐</a>
					<input class="duration required intCheck isZero" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" name="payReservePosition" name="payReservePosition" type="text"  value="${groupReserve.payReservePosition}" style="margin-bottom:0;" />
					<a href="javascript:void(0)" class="attend_up">+</a><input type="hidden" name="id" value="${groupReserve.id }" />
					<input type="hidden" name="srcActivityId" value="${groupReserve.srcActivityId }" />
					<input type="hidden" name="agentId" value="${groupReserve.agentId }" />
					</td>
            <td><span>${activitygroup.freePosition }</span></td>
            <td><span>${activitygroup.payDeposit }</span></td>
            <td><span>${activitygroup.settlementAdultPrice }</span></td>
            <td><span>${activitygroup.settlementcChildPrice }</span></td>
            <td><span>${activitygroup.planPosition }</span></td>
            <td><textarea  rows="1" class="input_textarea" maxlength="100" style="width:200px;margin-bottom:0" name="remark" style="margin-bottom:0;" value="${groupReserve.remark}">${groupReserve.remark}</textarea></td>
         </tr>
        
      </tbody>
   </table>
    
   <div class="sub_actions">
		<input type="submit" value="保存修改" class="btn btn-primary" id="btnSubmit">&nbsp; <input type="button" onClick="history.go(-1)" value="返 回" class="btn" id="btnCancel">
	</div> 
   </form>
</body>
</html>