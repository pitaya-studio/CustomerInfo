<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>设置费率</title>
	<meta name="decorator" content="wholesaler"/>
    <link href="${ctxStatic}/css/page-T2.css" type="text/css" rel="stylesheet">
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/common.css"/>
	<%-- <link rel="stylesheet" href="${ctxStatic}/css/nav.css" /> --%>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/search.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/input.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/setRate.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/table.css"/>
	<script type="text/javascript" src="${ctxStatic}/lib/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/search.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/setRate.js"></script>
    <script type="text/javascript" src="${ctxStatic}/lib/jsScroll.js"></script>
    
    <%-- <script src="${ctxStatic}/js/jquery-1.9.1.js" type="text/javascript"></script> --%>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"type="text/javascript"></script>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
 	<style>
 		.table_four tbody {
      counter-reset:sectioncounter;
    }
    .SortId:after {
      content:counter(sectioncounter);
      counter-increment:sectioncounter;
    }
 	</style>
   	<script type="text/javascript">
	$(function(){
		inputTips();

			if("${type}"==1){
				$("#li1").attr("class","select-tab");
			}else{
				$("#li2").attr("class","select-tab");
			}
			if("${page.pageNo}"==1){
				$("#i1").attr("disabled","disabled");
				$("#i1").attr("onclick","");
			}else{
				$("#i1").attr("class","fa fa-angle-left i-weight");
			}
			if("${page.pageNo}"=="${pageS}"){
				$("#i2").attr("disabled","disabled");
				$("#i2").attr("class","fa fa-angle-right");
				$("#i2").attr("onclick","");
			}else{
				$("#i2").attr("class","fa fa-angle-right i-weight");
			}
			if("${quauqStrategy.channelType}"!=""){
				$("#wholeSearch").attr("class","");	
			}
		});
		//下属门店
   		function downStore(obj,agentId){
   			$.ajax({
				type:"post",
				url:"${ctx}/group/strategy/getChildrenAgentList",
				data:{
					agentId:agentId
				},
				success:function(result){
					$(obj).next().find("ul").children().remove();
					if(result.length!=0){
						var html="";
						$.each(result,function(i,agent){
							html+="<li>"+agent.agentName+"</li>";
						});
						$(obj).next().find("ul").append(html);
					}else{
						var html = "<li>无下属门店</li>";
						$(obj).next().find("ul").append(html);
					}
				}
   			});
   		}
   		//未设置和已设置标签切换
   		function onType(num,obj){
   			if(num==1){
   				location.href="${ctx }/group/strategy/getNotSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}";
   			}else{
   				location.href="${ctx }/group/strategy/getHasSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}";
   			}
   		}
   		//分页
   		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			if("${type}"==1){
				$("#searchForm").attr("action","${ctx }/group/strategy/getNotSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}");
			}else{
				$("#searchForm").attr("action","${ctx }/group/strategy/getHasSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}");
			}
			$("#searchForm").submit();
	    	return false;
   		}
   		//选择费率类型
   		function choice(num,obj){
   			$(obj).parent().parent().parent().find("input[class='quauqType']").val(num);
   		}
   		//保存
   		function save(){
   			var inputs=$("input[name='indexBox']:checked");
			if(inputs.length==0){
				top.$.jBox.tip("请选择客户");
				return;
			}
   			$.jBox.confirm("您确认替换这些客户的费率吗？", "系统提示", function(v, h, f) {
   				if(v=='ok'){
   	   				var quauq=false;
   	   				var check=false;
   	   				$.each(inputs,function(i,input){
   	   					var tr=$(input).parent().parent();
   	   					if(tr.find("input[name='quauqRate']").val()==""){
   	   						quauq=true;
   	   					}
   	   					if(checkTable(tr.find("input[name='quauqRate']"))||checkTable(tr.find("input[name='quauqOtherRate']")) || checkTable(tr.find("input[name='agentRate']")) || checkTable(tr.find("input[name='agentOtherRate']"))){
   	   	   					check=true;
   	   	   				}
   	   				});
   	   				//百分比时验证所填数在0~100之间
   	   				if(check){
   	   					return;
   	   				}
   	   				//quauq费率必填校验
   	   				 if(quauq){
   	   					top.$.jBox.tip("请填写quauq费率","error");
   	   					return;
   	   				}
   	   				$.ajax({
   	   					type:"post",
   	   					url:"${ctx }/group/strategy/batchSaveStrategy",
   	   					data:{
   	   						datas:JSON.stringify(getData())
   	   					},
   	   					success:function(result){
   	   						if(result=='success'){
   	   							if("${type}"==1){
   	   								$.each(inputs,function(i,a){
   	   		   							$(a).parent().parent().remove();
   	   		   						});
   	   								$("#strong").text($("#strong").text()-inputs.length);
   	   								top.$.jBox.tip("保存成功","success");
   	   							}else{
   	   								top.$.jBox.tip("保存成功","success");
   	   							}	
   	   						}
   	   					}
   	   				});
   				}
   			});
   				
   		}
   		
   		//数组组装
   		function getData(){
   			var datas=[];
   			var inputs=$("input[name='indexBox']:checked");
   			for(var i=0;i<inputs.length;i++){
   				var data={};
   				var tr=$(inputs[i]).parent().parent();
   				data.id=tr.find("input[name='id']").val();
   				data.companyUuid="${quauqStrategy.companyUuid}";
   				data.groupId="${quauqStrategy.groupId}";
   				data.productType="${quauqStrategy.productType}";
   				data.agentId=$(inputs[i]).val();
   				data.quauqRate=tr.find("input[name='quauqRate']").val();
   				data.quauqRateType=tr.find("input[name='quauqRateType']").val();
   				data.quauqOtherRate=tr.find("input[name='quauqOtherRate']").val();
   				data.quauqOtherRateType=tr.find("input[name='quauqOtherRateType']").val();
   				data.agentRate=tr.find("input[name='agentRate']").val();
   				data.agentRateType=tr.find("input[name='agentRateType']").val();
   				data.agentOtherRate=tr.find("input[name='agentOtherRate']").val();
   				data.agentOtherRateType=tr.find("input[name='agentOtherRateType']").val();
   				data.oldQuauqRate=tr.find("input[name='oldQuauqRate']").val();
   				data.oldQuauqRateType=tr.find("input[name='oldQuauqRateType']").val();
   				data.oldQuauqOtherRate=tr.find("input[name='oldQuauqOtherRate']").val();
   				data.oldQuauqOtherRateType=tr.find("input[name='oldQuauqOtherRateType']").val();
   				data.oldAgentRate=tr.find("input[name='oldAgentRate']").val();
   				data.oldAgentRateType=tr.find("input[name='oldAgentRateType']").val();
   				data.oldAgentOtherRate=tr.find("input[name='oldAgentOtherRate']").val();
   				data.oldAgentOtherRateType=tr.find("input[name='oldAgentOtherRateType']").val();
   				datas.push(data);
   			}
   			return datas;
   		}
   		//客户关系筛选
   		function agentType(obj){
   			//type:1 未设置
   			if("${type}"==1){
   				$("#searchForm").attr("action","${ctx }/group/strategy/getNotSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}");
   				$("#agentType").val(obj);
   				$("#searchForm").submit();
   			}else{
   				$("#searchForm").attr("action","${ctx }/group/strategy/getHasSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}");
   				$("#agentType").val(obj);
   				$("#searchForm").submit();
   			}
   		}
   		//搜索按钮
   		function agentName(){
   			if("${type}"==1){
   				$("#searchForm").attr("action","${ctx }/group/strategy/getNotSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}");
   				$("#searchForm").submit();
   			}else{
   				$("#searchForm").attr("action","${ctx }/group/strategy/getHasSetStrategyAgentList?companyUuid=${quauqStrategy.companyUuid}&productType=${quauqStrategy.productType}&groupId=${quauqStrategy.groupId}");
   				$("#searchForm").submit();
   			}
   		}
   	//全选
   	$(document).on('change','[name="indexBox"]',function(){
		if($("input[name='indexBox']:checked").length==$("input[name='indexBox']").length){
			$("#checkAll").prop("checked",true);
		}else{
			$("#checkAll").removeAttr("checked");
		}
   	});
  //批量时验证选择百分比时所填的值在0~100之间，如果数字以"."结尾，自动补上"00"
   	function checkRate(obj){
   		if($("#rateUnit").val()=="百分比"&& ($(obj).val()>100 ||$(obj).val()<0)){
   			top.$.jBox.tip("在百分比情况下只能输入0~100之间的数字","error");
   			return true;
   		}
   		if($("#rateValue").val().charAt($("#rateValue").val().length-1)=='.'){
   			var value=$("#rateValue").val()+"00";
   			$("#rateValue").val(value);
   		}
   	}
   	//验证选择百分比时所填的值在0~100之间,如果数字以"."结尾，自动补上"00"
   	function checkTable(obj){
   		if($(obj).prev().prev().find("input").val()=="百分比" && ($(obj).val()>100 ||$(obj).val()<0)){
   			top.$.jBox.tip("在百分比情况下只能输入0~100之间的数字","error");
   			return true;
   		}
   		if($(obj).val().charAt($(obj).val().length-1)=="."){
   			var value=$(obj).val()+"00";
   			$(obj).val(value)
   		}
   	}
   	</script>
</head>
<body>
	<page:applyDecorator name="group_rate">
            <page:param name="current">grouprate</page:param>
        </page:applyDecorator>
	<div>
	<form action="" method="post" id="searchForm">   
		<input type="hidden" name="type" value="${type }" id="type">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input name="channelType" type="hidden" id="agentType" value="${quauqStrategy.channelType }">
        <!--顶部tab页-->
        <ul class="tab-switch">
	        <li id="li1" onclick="onType(1,this)"><a href="javascript:void(0)" >未调整</a></li>
	        <li id="li2" onclick="onType(2,this)"><a href="javascript:void(0)" >已调整</a></li>
    	</ul>
    	<!--搜索框-->
    	<div class="search-content">
		    <input type="text" id="searchName" value="${quauqStrategy.agentName }" name="agentName" flag="istips">
			<span class="ipt-tips" style="left: 38px;top: 111px;">请输入客户名称</span>
		    <div class="search-btn" onclick="agentName()"><em class="fa fa-search"></em> 搜索</div>
		    <span class="clear-all"  id="clearAll" onclick="cleanAll()">清空所有条件</span>
		</div>
		</form>
		<!--搜索条件-->
		<div class="search-container">
	        <dl class="search-item">
	            <dt>客户类型</dt>
	            <dd class="last-item">
	                <div class="items-container">
	                    <span class="selected"  id="wholeSearch" onclick="agentType('')">全部</span>
	                    <c:forEach items="${dicts }" var="dicts">
	                    	<span onclick="agentType('${dicts.value}')" <c:if test="${dicts.value==quauqStrategy.channelType }">class="selected"</c:if> >${dicts.name }</span>
	                    </c:forEach>
	                  <!--   <em class="more-item">更多<i class="fa fa-caret-down" aria-hidden="true"></i></em> -->
	                </div>
	            </dd>
	        </dl>
        </div>
        <!--表头筛选-->
        <div class="table-sort">
	        <!--align-left排列在左边的元素-->
	        <span class="align-left"><input type="checkbox" class="table_checkbox"  id="checkAll" name="checkAll">全选</span>
	        <span class="align-left rever-checked"  id="reverseCheck" >反选</span>
	        <span class="align-left" style="margin-right:-10px;">
			    <span  class="dl-select">
			        <input type="text"   id="rateType"    readonly onfocus="this.blur()" class="rate"  value="QUAUQ产品费率"    />
			        <ul class="top-distant" style="top:37px;">
			            <li>QUAUQ产品费率</li>
			            <li>QUAUQ其他费率</li>
			            <li>渠道产品费率</li>
			            <li>渠道其他费率</li>
			        </ul>
			    </span>
			</span>
			<span class="align-left" style="margin-right:0px;">
			    <span  class="dl-select small-select" style="margin-right:0px;">
			        <input type="text" id="rateUnit" readonly onfocus="this.blur()" value="百分比" class="rate">
			        <ul class="top-distant">
			            <li>百分比</li>
			            <li>金额￥</li>
			        </ul>
			    </span>
	        </span>
			<span class="align-left" style="margin-right:10px;">
			    <input type="text"  class="rate-input"  id="rateValue"  onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="5" onblur="checkRate(this)">
			</span>
	        <span class="align-left">
			    <button  id="batchFill" class="batch-fill">批量填写</button>
	        </span>
	        <span>查询结果<strong id="strong">${page.count }</strong>条</span>
	        <span class="simple-page"  style="cursor:pointer;">
	            <i class="fa fa-angle-left" aria-hidden="true" onclick="page('${page.pageNo-1}', '${page.pageSize }')" id="i1"></i>
	            <em>${page.pageNo }</em>/<em>${pageS }</em>
	            <i class="fa fa-angle-right i-weight" aria-hidden="true" onclick="page('${page.pageNo+1}', '${page.pageSize }')" id="i2"></i><!--i-weight加粗显示的样式-->
	        </span>
        </div>
        <!--列表table-->
        <table class="table_four"  style="margin-top:10px;">
			<thead>
			<tr>
				<th class="table_per_7">序号</th>
				<th class="table_per_24">客户名称</th>
				<th class="table_per_10">客户类型</th>
				<th class="table_per_13">QUAUQ产品费率</th>
				<th class="table_per_13">QUAUQ其他费率</th>
				<th class="table_per_13">渠道产品费率</th>
				<th class="table_per_13">渠道其他费率</th>
				<th class="table_per_7">操作</th>
			</tr>
			</thead>
			<tbody id="tb">
			<c:forEach items="${page.list }" var="list" varStatus="status">
			<tr>
				<td class="SortId">
					<input name="indexBox" type="checkbox" class="table_checkbox" value="${list.agentId }"><%--  ${status.count } --%>
					<input name="id" type="hidden" value="${list.groupRateId }">
				</td>
				<td style="word-break: break-all;display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;max-width: 350px;min-width:200px;margin-top: 15px;" title="${list.agentName }">${list.agentName }</td>
				<td>${list.customName}</td>
				<td>
					 <div  class="dl-select small-select">
					 	<c:choose>
					 		<c:when test="${list.quauq_rate_type==0 }"><input type="text" value="百分比" readonly onfocus="this.blur()"></c:when>
					 		<c:when test="${list.quauq_rate_type==1 }"><input type="text" value="金额￥" readonly onfocus="this.blur()"></c:when>
					 		<c:otherwise><input type="text" readonly value="百分比" onfocus="this.blur()"></c:otherwise>
					 	</c:choose>
				        <ul class="select-option">
				           	<li onclick="choice(0,this)">百分比</li>
				            <li onclick="choice(1,this)">金额￥</li>
				        </ul>
				    </div>
					<input type="hidden" name="oldQuauqRate" value="${list.quauq_rate }">
					<c:choose>
				 		<c:when test="${list.quauq_rate_type==0 }"><input type="text"  name="quauqRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" 
				 				value="<fmt:formatNumber type="number"  value="${list.quauq_rate*100 } " pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" maxlength="5" onblur="checkTable(this)">
				 		</c:when>
				 		<c:when test="${list.quauq_rate_type==1 }"><input type="text"  name="quauqRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" 
				 				value="<fmt:formatNumber type="number"  value="${list.quauq_rate } " pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" maxlength="5" onblur="checkTable(this)"></c:when>
				 		<c:otherwise><input type="text" name="quauqRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" value="" maxlength="5" onblur="checkTable(this)"></c:otherwise>
					 </c:choose>
					 <c:choose>
					 	<c:when test="${list.quauq_rate_type!=0 && list.quauq_rate_type!=1 }">
					 		<input type="hidden" name="quauqRateType" class="quauqType" value="0">
					 	</c:when>
					 	<c:otherwise>
					 		<input type="hidden" name="quauqRateType" class="quauqType" value="${list.quauq_rate_type }">
					 	</c:otherwise>
					 </c:choose>
				     <input type="hidden" name="oldQuauqRateType" value="${list.quauq_rate_type }">
			    </td>
				<td>
					<div  class="dl-select small-select">
				        <c:choose>
					 		<c:when test="${list.quauq_other_rate_type==0 }"><input type="text" value="百分比" readonly onfocus="this.blur()"></c:when>
					 		<c:when test="${list.quauq_other_rate_type==1 }"><input type="text" value="金额￥" readonly onfocus="this.blur()"></c:when>
					 		<c:otherwise><input type="text" readonly value="百分比" onfocus="this.blur()"></c:otherwise>
					 	</c:choose>
				        <ul class="select-option">
				            <li onclick="choice(0,this)">百分比</li>
				            <li onclick="choice(1,this)">金额￥</li>
				        </ul>
				    </div>
					<input type="hidden" name="oldQuauqOtherRate" value="${list.quauq_other_rate }">
					 <c:choose>
					 		<c:when test="${list.quauq_other_rate_type==0 }">
					 			<input type="text" name="quauqOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" 
					 				value="<fmt:formatNumber type="number"  value="${list.quauq_other_rate*100 } " pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" onblur="checkTable(this)" maxlength="5">
					 		</c:when>
					 		<c:when test="${list.quauq_other_rate_type==1 }">
					 			<input type="text" name="quauqOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" 
					 				value="<fmt:formatNumber type="number"  value="${list.quauq_other_rate } " pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" onblur="checkTable(this)" maxlength="5">
					 		</c:when>
					 		<c:otherwise>
					 			<input type="text" name="quauqOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" value="" onblur="checkTable(this)" maxlength="5">
					 		</c:otherwise>
					 	</c:choose>
					 	 <c:choose>
						 	<c:when test="${list.quauq_other_rate_type!=0 && list.quauq_other_rate_type!=1 }">
						 		<input type="hidden" name="quauqOtherRateType" class="quauqType" value="0">
						 	</c:when>
						 	<c:otherwise>
						 		<input type="hidden" name="quauqOtherRateType" class="quauqType" value="${list.quauq_other_rate_type }">
						 	</c:otherwise>
					 </c:choose>
					<input type="hidden" name="oldQuauqOtherRateType" value="${list.quauq_other_rate_type }">
				</td>
				<td>
					 <div  class="dl-select small-select">
				        <c:choose>
					 		<c:when test="${list.agent_rate_type==0 }"><input type="text" value="百分比" readonly onfocus="this.blur()"></c:when>
					 		<c:when test="${list.agent_rate_type==1 }"><input type="text" value="金额￥" readonly onfocus="this.blur()"></c:when>
					 		<c:otherwise><input type="text" readonly value="百分比" onfocus="this.blur()"></c:otherwise> 
					 	</c:choose>
				        <ul class="select-option">
				            <li onclick="choice(0,this)">百分比</li>
				            <li onclick="choice(1,this)">金额￥</li>
				        </ul>
				    </div>
					<input type="hidden" name="oldAgentRate" value="${list.agent_rate }">
					<c:choose>
				 		<c:when test="${list.agent_rate_type==0 }">
				 			<input type="text"  name="agentRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" 
				 				value="<fmt:formatNumber type="number"  value="${list.agent_rate*100 } " pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" onblur="checkTable(this)" maxlength="5">
				 		</c:when>
				 		<c:when test="${list.agent_rate_type==1 }">
				 			<input type="text"  name="agentRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" 
				 				value="<fmt:formatNumber type="number"  value="${list.agent_rate }" pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" onblur="checkTable(this)" maxlength="5">
				 		</c:when>
				 		<c:otherwise>
				 			<input type="text"  name="agentRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" value="" onblur="checkTable(this)" maxlength="5">
				 		</c:otherwise>
					 </c:choose>
					  <c:choose>
						 	<c:when test="${list.agent_rate_type!=0 && list.agent_rate_type!=1 }">
						 		<input type="hidden" name="agentRateType" class="quauqType" value="0">
						 	</c:when>
						 	<c:otherwise>
						 		<input type="hidden" name="agentRateType" class="quauqType" value="${list.agent_rate_type }">
						 	</c:otherwise>
					 </c:choose>
					<input type="hidden" name="oldAgentRateType" value="${list.agent_rate_type }">
				</td>
				<td>
					<div  class="dl-select small-select">
				        <c:choose>
					 		<c:when test="${list.agent_other_rate_type==0 }"><input type="text" value="百分比" readonly onfocus="this.blur()"></c:when>
					 		<c:when test="${list.agent_other_rate_type==1 }"><input type="text" value="金额￥" readonly onfocus="this.blur()"></c:when>
					 		<c:otherwise><input type="text" readonly value="百分比" onfocus="this.blur()"></c:otherwise>
					 	</c:choose>
				        <ul class="select-option">
				            <li onclick="choice(0,this)">百分比</li>
				            <li onclick="choice(1,this)">金额￥</li>
				        </ul>
				    </div>
					<input type="hidden" name="oldAgentOtherRate" value="${list.agent_other_rate }">
					<c:choose>
				 		<c:when test="${list.agent_other_rate_type==0 }">
				 			<input type="text" name="agentOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" 
				 			value="<fmt:formatNumber type="number"  value="${list.agent_other_rate*100 }" pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" onblur="checkTable(this)" maxlength="5">
				 		</c:when>
				 		<c:when test="${list.agent_other_rate_type==1 }">
				 			<input type="text" name="agentOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" 
				 				value="<fmt:formatNumber type="number"  value="${list.agent_other_rate }" pattern="##.##" maxFractionDigits="2" minFractionDigits="2"/>" onblur="checkTable(this)" maxlength="5">
				 		</c:when>
				 		<c:otherwise>
				 			<input type="text" name="agentOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" value="" onblur="checkTable(this)" maxlength="5">
				 		</c:otherwise>
				 	</c:choose>
				 	<c:choose>
						 	<c:when test="${list.agent_rate_type!=0 && list.agent_rate_type!=1 }">
						 		<input type="hidden" name="agentOtherRateType" class="quauqType" value="0">
						 	</c:when>
						 	<c:otherwise>
						 		<input type="hidden" name="agentOtherRateType"  class="quauqType" value="${list.agent_other_rate_type }">
						 	</c:otherwise>
					 </c:choose>
					<input type="hidden" name="oldAgentOtherRateType" value="${list.agent_other_rate_type }">
				</td>
				<td class="ro">
					<span  class="sub-store" onmouseover="downStore(this,'${list.agentId}')" style="cursor:pointer;">下属门店</span>
					<div class="tip-container">
					<em class="triangle-img"></em>
						<ul>
		
						</ul>
					</div>
				</td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
		<!--分页-->
		<div class="pagination" style="height:35px;">
	   		${page}
		</div>
		<!--保存按钮-->
		<button class="save-btn" onclick="save()">保存</button><button class="save-btn cancel-btn" onclick="window.close();">返回</button>
		</div>
</body>
</html>