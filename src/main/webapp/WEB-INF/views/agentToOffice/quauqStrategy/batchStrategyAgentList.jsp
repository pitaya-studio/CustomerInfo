<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>批量设置费率</title>
	<meta name="decorator" content="wholesaler"/>
	<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet">
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
			console.log("${quauqStrategy.channelType}")
		});
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
						$(obj).next().find("ul").append("<li>无下属门店</li>");
					}
				}
   			});
   		}
   		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
   		}
   		function choice(num,obj){
   			$(obj).parent().parent().prev().prev().val(num);
   		}
   		function save(){
   				var inputs=$("input[name='indexBox']:checked");
   				if(inputs.length==0){
   					top.$.jBox.tip("请选择客户");
   					return;
   				}
   				var quauq=false;
   				var quauqOther=false;
   				var agent=false;
   				var agentOther=false;
   				$.each(inputs,function(i,input){
   					var tr=$(input).parent().parent();
   					if(tr.find("input[name='quauqRate']").val()!="" && tr.find("input[name='quauqRateType']").val()==""){
   						quauq=true;
   					}
   					if(tr.find("input[name='quauqOtherRate']").val()!="" && tr.find("input[name='quauqOtherRateType']").val()==""){
   						quauqOther=true;
   					}
   					if(tr.find("input[name='agentRate']").val()!="" && tr.find("input[name='agentRateType']").val()==""){
   						agent=true;
   					}
   					if(tr.find("input[name='agentOtherRate']").val()!="" && tr.find("input[name='agentOtherRateType']").val()==""){
   						agentOther=true;
   					}
   				});
   				if(quauq){
   					top.$.jBox.tip("请选择quauq费率类型");
   					return;
   				}
   				if(quauqOther){
   					top.$.jBox.tip("请选择quauq其他费率类型");
   					return;
   				}
   				if(agent){
   					top.$.jBox.tip("请选择渠道费率类型");
   					return;
   				}
   				if(agentOther){
   					top.$.jBox.tip("请选择渠道其他费率类型");
   					return;
   				}
   				$.ajax({
   					type:"post",
   					url:"${ctx }/group/strategy/batchSaveGroupRate",
   					data:{
   						datas:JSON.stringify(getData()),
   						companyUuid:'${quauqStrategy.companyUuid}',
   						productType:'${quauqStrategy.productType}',
   						groupIds:'${groupIds}'
   					},
   					success:function(result){
   						if(result=='success'){
   							top.$.jBox.tip("保存成功");
   						}
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
   				data.agentId=$(inputs[i]).val();
   				data.agentType=tr.find("input[name='agentType']").val();
   				data.quauqRate=tr.find("input[name='quauqRate']").val();
   				data.quauqRateType=tr.find("input[name='quauqRateType']").val()=="百分比"?0:tr.find("input[name='quauqRateType']").val()=="金额"?1:null;
   				data.quauqOtherRate=tr.find("input[name='quauqOtherRate']").val();
   				data.quauqOtherRateType=tr.find("input[name='quauqOtherRateType']").val()=="百分比"?0:tr.find("input[name='quauqOtherRateType']").val()=="金额"?1:null;
   				data.agentRate=tr.find("input[name='agentRate']").val();
   				data.agentRateType=tr.find("input[name='agentRateType']").val()=="百分比"?0:tr.find("input[name='agentRateType']").val()=="金额"?1:null;
   				data.agentOtherRate=tr.find("input[name='agentOtherRate']").val();
   				data.agentOtherRateType=tr.find("input[name='agentOtherRateType']").val()=="百分比"?0:tr.find("input[name='agentOtherRateType']").val()=="金额"?1:null;
   				datas.push(data);
   			}
   			return datas;
   		}
   		//客户关系筛选
   		function agentType(obj){
   				$("#agentType").val(obj);
   				$("#searchForm").submit();
   		}
   		function agentName(){
   				$("#searchForm").submit();
   		}
   		
	function checkRate(obj){
   		if($("#rateUnit").val()=="百分比"&& ($(obj).val()>100 ||$(obj).val()<0)){
   			top.$.jBox.tip("在百分比情况下只能输入0~100之间的数字","error");
   			return true;
   		}
   	}
   	
   	$(document).on('change','[name="indexBox"]',function(){
		if($("input[name='indexBox']:checked").length==$("input[name='indexBox']").length){
			$("#checkAll").prop("checked",true);
		}else{
			$("#checkAll").removeAttr("checked");
		}
   	});
   	</script>
</head>
<body> 
	<content tag="three_level_menu">
         <li id="grouprate"><font >设置费率 <strong>——</strong> ${company.name }</font></li>
	</content>
	<form action="" method="post" id="searchForm">   
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input name="channelType" type="hidden" id="agentType" value="${quauqStrategy.channelType }">
        <!--顶部tab页-->
    	<!--搜索框-->
    	<div class="search-content">
		    <input type="text" placeholder="请输入客户名称"  id="searchName" value="${quauqStrategy.agentName }" name="agentName">
		    <div class="search-btn" onclick="agentName()"><em class="fa fa-search"></em> 搜索</div>
		    <span class="clear-all"  id="clearAll">清空所有条件</span>
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
			        <input type="text" id="rateType" readonly onfocus="this.blur()"  value="QUAUQ产品费率"  class="rate">
			        <ul class="top-distant">
			            <li>QUAUQ产品费率</li>
			            <li>QUAUQ其他费率</li>
			            <li>渠道产品费率</li>
			            <li>渠道其他费率</li>
			        </ul>
			    </span>
			</span>
			<span class="align-left"  style="margin-right:0;">
				    <span  class="dl-select small-select"  style="margin-right:0;">
				        <input type="text" id="rateUnit"  value="百分比" class="rate"/>
				        <ul class="top-distant" readonly onfocus="this.blur()" value="百分比">
				            <li onclick="cleanInputVal(this)">百分比</li>
				            <li onclick="cleanInputVal(this)">金额</li>
				        </ul>
				    </span>
		        </span>
				<span class="align-left" style="margin-right:10px;">
				    <input type="text"  class="rate-input"  id="rateValue"  onkeyup="value=value.replace(/[^\d.]/g,'')" onblur="checkRateItm(this)">
				</span>
	        <span class="align-left">
			    <button  id="batchFill" class="batch-fill">批量填写</button>
	        </span>
	        <span>查询结果<strong>${page.count }</strong>条</span>
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
					<input name="agentType" type="hidden" value="${list.agentType }">
				</td>
				<td style="word-break: break-all;display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;max-width: 350px;min-width:200px;margin-top: 15px;" title="${list.agentName }">${list.agentName }</td>
				<td>${list.customName }</td>
				<td>
					<div  class="dl-select small-select">
					        <input name="quauqRateType" type="text" value="百分比"/>
					        <ul class="select-option">
					            <li onclick="cleanInputVal(this)">百分比</li>
					            <li onclick="cleanInputVal(this)">金额</li>
					        </ul>
					    </div>
				 	<input type="text" name="quauqRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" value=""  onblur="checkRateItm(this)">
			    </td>
				<td>
					 <div  class="dl-select small-select">
					        <input name="quauqOtherRateType" type="text" value="百分比"/>
					        <ul class="select-option">
					            <li onclick="cleanInputVal(this)">百分比</li>
					            <li onclick="cleanInputVal(this)">金额</li>
					        </ul>
					    </div>
					<input type="text" name="quauqOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" value="" onblur="checkRateItm(this)">
				</td>
				<td>
					<div  class="dl-select small-select">
					        <input name="agentRateType" type="text" value="百分比"/>
					        <ul class="select-option">
					            <li onclick="cleanInputVal(this)">百分比</li>
					            <li onclick="cleanInputVal(this)">金额</li>
					        </ul>
					    </div>
				 			<input type="text"  name="agentRate" class="rate-input  move-right"   onkeyup="value=value.replace(/[^\d.]/g,'')" value="" onblur="checkRateItm(this)">
				</td>
				<td>
					<div  class="dl-select small-select">
					        <input name="agentOtherRateType" type="text" value="百分比"/>
					        <ul class="select-option">
					            <li onclick="cleanInputVal(this)">百分比</li>
					            <li onclick="cleanInputVal(this)">金额</li>
					        </ul>
					    </div>
				 			<input type="text" name="agentOtherRate"  class="rate-input  move-right"  onkeyup="value=value.replace(/[^\d.]/g,'')" value="" onblur="checkRateItm(this)">
				</td>
				<td>
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