<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>已发布产品</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
   <style type="text/css">
   #contentTable td{padding:4px 12px;text-align:left;}
	#contentTable td.v_middle{vertical-align:middle;text-align:center;}
   #contentTable th{padding:12px;text-align:left;}
	#teamTable{ margin-bottom:8px;}
   </style>
	<script type="text/javascript">
		$(document).ready(function() {
		
		 	$("input").validate({
            	 });
            	 
			$("#groupOpenDate").datepicker({
				dateFormat:"yy-mm-dd",
			   dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
		       closeText:"关闭", 
		       prevText:"前一月", 
		       nextText:"后一月",
		       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
		       });
			$("#groupCloseDate").datepicker({
				dateFormat:"yy-mm-dd",
				   dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
			       closeText:"关闭", 
			       prevText:"前一月", 
			       nextText:"后一月",
			       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
			       
			       });
			//表单验证
	    	$("#searchForm").validate({
	    		rules:{
	    			settlementAdultPriceStart : "number",
	    			settlementAdultPriceEnd : "number"
	    		},
	    		messages:{
	    			settlementAdultPriceStart : "请输入数字价格",
	    			settlementAdultPriceEnd : "请输入数字价格"
	    		}
	    		
	    	});
	    	$("#groupform").validate({
	    	});
	    	//var ids = "";
			//var names = "";
			//<c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
			 	//ids = ids + "${data.id}"+",";
			 	//names = names +"${data.name}"+",";
			//</c:forEach>
			//$("#targetAreaId").val(ids.toString().substring(0,ids.length-1));
			//$("#targetAreaName").val(names.toString().substring(0,names.length-1));
			$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
		});
// 		function expand(srcActivityId,parent,expand,close,child){
			
// 			$.ajax({
// 				type:"POST",
// 				url:"${ctx}/activity/manager/groups?",
// 				dataType:"json",
//				contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
// 				data:{date:new Date(),srcActivityId:srcActivityId},
// 				success:function(result){
// 					var con = JSON.stringify(result);
// 					$("#content").html(con);
// 				},
// 				error:function(){
					
// 				}
// 			});
			
// 			var content = "<tr id=\"child1\" style=\"display:table-row\"><td colspan=\"10\"><table class=\"table table-striped table-bordered table-condensed\"><thead><tr><th>出团日期</th></tr></thead><tbody><tr><td>fafd</td></tr></tbody></table></td></tr>";
// 			$(parent).after(content);
// 			$(expand).css("display","none");
// 			$(close).css("display","block");	
// 		}
// 		function close(child,expand,close){
// 			$(child).remove();
// 			$(expand).css("display","block");
// 			$(close).css("display","none");
// 		}
		function expand(savebtn,child,obj){
			
			$(obj).parent().parent().next().find("span").css("display","block");
	    	$(obj).parent().parent().next().find("input[type='text']").css("display","none");
	    	$(obj).parent().parent().next().find("input[type='text']").attr("disabled",true);
	    	$(savebtn).css("disabled","none");
	    	//$(savebtn).attr("disabled",true);
			if($(child).css("display")=="none"){				
				$(obj).html("关闭全部团期");
				$(child).css("display","table-row");
				
			}else{
				
				if($(child).css("display")=="table-row"){			
					$(child).css("display","none");
					$(obj).html("展开全部团期");
				}
			}
				
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/activity/manager/list");
			$("#searchForm").submit();
	    }
	    function delgroup(id,srcActivityId){
	    	$("#groupform").attr("action","${ctx}/activity/manager/delgroup?id="+id+"&srcActivityId="+srcActivityId);
			$("#groupform").submit();
	    }
	    // 删除确认对话框
		function confirmxCopy(mess, href,id ,srcActivityId){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				$("#groupform").attr("action","${ctx}/activity/manager/delgroup?id="+id+"&srcActivityId="+srcActivityId);
				$("#groupform").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		function confirmxDel(mess, href,proId){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				$("#searchForm").attr("action","${ctx}/activity/manager/del/"+proId);
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
	    function savegroup(savebtn,srcActivityId){
	    	$("#groupform").attr("action","${ctx}/activity/manager/savegroup?srcActivityId="+srcActivityId);
	    	$("#groupform").submit();
	    	//$(savebtn).attr("disabled",true); 
	    }
	    function modgroup(groupid,savebtn,obj){    	
	    	$(obj).parent().parent().find("span").css("display","none");
	    	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	    	$(groupid).css("display","none");
	    	$(groupid).attr("disabled",false);
	    	$(savebtn).css("display","block");
	    }
	    function downloads(docid){
	    	window.location.href = "${ctx}/sys/docinfo/download/"+docid;
	    }
	    //产品修改
	    function productModify(proId){
	    	$("#searchForm").attr("action","${ctx}/activity/manager/mod/"+proId+"/0");
			$("#searchForm").submit();
	    }
	    function productDel(proId){
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f){
			if (v == 'ok') {
		        $.ajax({
		        type: "POST",
		        url: "${ctx}/activity/manager/del/" + proId + "/2",
		        
		        success: function(msg){
		            top.$.jBox.tip('删除成功','warning');
		            location.reload();
		        }
		     });
			}else if (v == 'cancel'){
				
					}
			});
		}
	    
	    //占位
	    function occupied(id,srcActivityId){
	    	window.open("${ctx}/orderCommon/manage/showforModify?type=3&productId="+srcActivityId+"&productGroupId="+id);
        }
	    //预订
	    function reserveOrder(id,srcActivityId){
	    	window.open("${ctx}/orderCommon/manage/showforModify?type=1&productId="+srcActivityId+"&productGroupId="+id);
        }
	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2')
			},function(){
				$(this).removeClass('team_a_click2')
			})	
		 })
	</script>
</head>
<body>
	 <ul class="nav nav-tabs">
		<shiro:hasPermission name="product:manager:view"><li class="active"><a href="${ctx}/activity/manager/list">库存查询</a></li></shiro:hasPermission>
		<shiro:hasPermission name="product:manager:add"><li><a href="${ctx}/activity/manager/form">产品详情</a></li></shiro:hasPermission>
        <shiro:hasPermission name="inventory:warning:view">
        <li><a href="${ctx}/warning/showwarningList.htm">产品申请切位</a></li>
        <li><a href="${ctx}/warning/showAddForm">修改切位</a></li>
        </shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div style="margin-top:8px;" class="team_con row  show-grid">
      	<div class="span7">
            <label>旅游类型：</label><form:select path="travelTypeId" itemValue="key" itemLabel="value">
               <form:option value="">请选择</form:option>
               <form:options items="${travelTypes}" />
            </form:select>
         	
         </div>
         <div class="span7">   
				<label>目的地：</label>
                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
         </div>  
		</div>
		<div style="margin-top:8px;" class="team_con row show-grid">
      	<div class="span7">
				<label>航空公司：</label><form:select id="trafficName" path="trafficName" itemValue="key" itemLabel="value" >      
               <form:option value="">请选择</form:option>
               <form:options items="${trafficNames}"/>
            </form:select>
         	
         </div>
         <div class="span7">
            <label>切位渠道:</label><form:select path="activityLevelId" itemValue="key" itemLabel="value">
               <form:option value="">请选择</form:option>
               <form:options items="${productLevels}" />
            </form:select>
            
         </div>
		</div>
      <div style="margin-top:8px;" class="team_con row show-grid">
      	<div class="span7"><label>产品系列：</label><form:select path="activityTypeId" itemValue="key" itemLabel="value">
               <form:option value="">请选择</form:option>
               <form:options items="${productTypes}" />
            </form:select>
         	
         </div>
		 <div class="span7"><label>产品类型：</label><form:select path="activityTypeId" itemValue="key" itemLabel="value">
               <form:option value="">请选择</form:option>
               <form:options items="${productTypes}" />
            </form:select>
         </div>
      </div>
	  <div style="margin-top:8px;" class="team_con row show-grid">
	     <div class="span12"><label>天数：</label><form:select path="activityDuration">
               <form:option value="">请选择</form:option>
               <form:option value="10">10</form:option>
               <form:option value="9">9</form:option>
               <form:option value="8">8</form:option>
               <form:option value="7">7</form:option>
               <form:option value="6">6</form:option>
               <form:option value="5">5</form:option>
               <form:option value="4">4</form:option>
               <form:option value="3">3</form:option>
               <form:option value="2">2</form:option>
               <form:option value="1">1</form:option>
            </form:select>
         </div>
	</div>
	<div style="margin-top:8px;" class="team_con row show-grid">
      	<div class="span8">
				<label>出团日期：</label><input id="groupOpenDate" name="groupOpenDate" value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>' readonly/> 至 
				<input id="groupCloseDate" name="groupCloseDate" value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' readonly/>
         	
         </div>
    </div>
    <div style="margin-top:8px;" class="team_con row">
         <div class="span8">
				<label>价格范围：</label><input id="settlementAdultPriceStart" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }"/> 至 
				<input id="settlementAdultPriceEnd" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }"/>
         </div>
            
	</div>
	<div style="margin-top:8px;" class="team_con row">
      	<div class="span7">
				<label>关键词：</label><input id="wholeSalerKey" name="wholeSalerKey"/>
				&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="搜索" onClick=""/>
		</div>
    </div>
	<div style="margin-top:8px;" class="team_con row show-grid">
      	<div class="span7" style="padding-left:20px;">
				<label>搜索结果：&nbsp;&nbsp;&nbsp;共100条</label>
		</div>
		<div class="span7" style="padding-left:;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="+添加渠道" onClick=""/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="+添加产品" onClick=""/>
		</div>
    </div>
	</form:form>
	<tags:message content="${message}"/>
	<form id="groupform" name="groupform" action="" method="post">
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
   	<colgroup>
      	<col width="5%">
         <col width="10%">
         <col width="10%">
         <col width="10%">
         <col width="35%">
         <col width="20%">
         <col width="10%">
        
      </colgroup>
		<thead style="background:#403738">
			<tr>
				<th>序号</th>
				<th>产品系列</th>
				<th>出发地</th>
				<th>航空</th>
				<th>产品名称</th>
				<th>最近出团日期</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		
			<c:forEach items="${page.list}" var="activity" varStatus="s">
			<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
				<tr id="parent${s.count}">
					<td>${s.count}</td>
					<td>${activity.activityLevelId}</td>
					<td>${activity.fromArea}</td>
                    <td>${activity.targetAreaNames}</td>
					<td>${activity.trafficName}</td>
					<td>${activity.groupOpenDate}至${activity.groupCloseDate}<br>
					<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#savebtn${s.count}','#child${s.count}',this)">展开全部团期</a>
               <img alt="下载" src='<c:url value="/static/images/img/team_san.png"/>'/>
               
<!-- 						<a id="expand${s.count}" href="javascript:void(0)" onclick="expand(${activity.id},'#parent${s.count}','#expand${s.count}','#close${s.count}','child${s.count }')">展开全部团期</a> -->
<!-- 						<a id="close${s.count}" href="javascript:void(0)" onclick="close('#child${s.count}','#expand${s.count}','#close${s.count}')">展开全部团期</a> -->
					</td>
					
					<td>
						<shiro:hasPermission name="product:manager:edit"><a href="javascript:void(0)" onClick="productModify(${activity.id})">切位</a></shiro:hasPermission>&nbsp;&nbsp;&nbsp;
						<shiro:hasPermission name="product:manager:delete"><a href="javascript:void(0)" onClick="return confirmxDel('要删除该产品吗？', this.href ,${activity.id})">库存详情</a></shiro:hasPermission>
					</td>
										
				</tr>
				<tr id="child${s.count}" style="display:none"></tr>
			</c:forEach>
		
		

				
		</tbody>
	</table>
	</form>
	<div class="pagination">${page}</div>
   <div class="page">
   	
   </div>	 
</body>
</html>