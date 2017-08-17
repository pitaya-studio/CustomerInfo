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
	.main-right{ min-width:1650px;}
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
				$("#searchForm").attr("action","${ctx}/activity/manager/del/" + proId + "/2");
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
	    	$("#searchForm").attr("action","${ctx}/activity/manager/mod/"+proId +"/0");
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
        <div class="team_bill"><span style="padding-left:20px; color:#FFF;">产品基本资料</span></div>
		<table id="orderDetail_span" class="table table-striped table-bordered table-condensed">
                <tr>
                    <td style=" border-right:none;"><span  class="manageorder_label" style="text-align:right">线路名称:</span></td>
                    <td style="text-align:left; border-left:none;">${productorder.orderCompanyName}<input id="orderid" type="hidden" value="${productorder.id}"></td>
                    <td style=" border-right:none;"><span  class="manageorder_label"style="text-align:right">出发地:</span></td>
                    <td  style="text-align:left; border-left:none;">${productorder.orderPersonName}</td>
                    <td style=" border-right:none;"><span  class="manageorder_label"style="text-align:right">航空:</span></td>
                    <td  style="text-align:left; border-left:none;">${productorder.orderPersonPhoneNum}</td>
                </tr>
                <tr>
                    <td style=" border-right:none;"><span  class="manageorder_label"style="text-align:right">旅游类型:</span></td>
                    <td  style="text-align:left; border-left:none;"><fmt:formatDate value="${productorder.orderTime}" pattern="yyyy-MM-dd"/></td>
                    <td style=" border-right:none;"><span  class="manageorder_label"style="text-align:right">产品类型:</span></td>
                    <td  style="text-align:left; border-left:none;"><fmt:formatNumber value='${productorder.totalMoney}' pattern="0" /></td>
                    <td style=" border-right:none;"><span  class="manageorder_label"style="text-align:right">产品系列:</span></td>
                    <td  style="text-align:left; border-left:none;">${productorder.payedMoney}</td>
                </tr>
                <tr>
                    <td style=" border-right:none;"><span  class="manageorder_label">组团类型:</span></td>
                    <td  style="text-align:left; border-left:none;">${product.acitivityName}</td>
                    <td style=" border-right:none;"><span  class="manageorder_label">切位渠道:</span></td>
                    <td  style="text-align:left; border-left:none;">${product.activityLevelName}</td>
                    <td style=" border-right:none;"></td>
                    <td style="text-align:left; border-left:none;"></td>
                </tr>
            </table>
	</form:form>
    
    
	<tags:message content="${message}"/>
	<form id="groupform" name="groupform" action="" method="post">
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
   	<colgroup>
      	<col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="8%">
         <col width="15%">
         <col width="5%">
      </colgroup>
		<thead style="background:#403738">
			<tr>
				<th>出团日期</th>
				<th>预收人数</th>
				<th>已分配人数</th>
                <th>占位人数</th>
				<th>切位人数</th>
				<th>已售出</th>
				<th>售出占位</th>
				<th>售出切位</th>
				<th>余位</th>
				<th>本社留位</th>
				<th>团期剩余天数</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		
			<c:forEach items="${page.list}" var="activity" varStatus="s">
			<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
				<tr id="parent${s.count}">
					<td>2014.2.6</td>
					<td>20</td>
					<td>18<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#savebtn${s.count}','#child${s.count}',this)">明细</a></td>
                    <td>7</td>
					<td>8</td>
					<td>9</td>
					<td>3<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#savebtn${s.count}','#child${s.count}',this)">订单明细</a>
               <img alt="下载" src='<c:url value="/static/images/img/team_san.png"/>'/>
               
<!-- 						<a id="expand${s.count}" href="javascript:void(0)" onclick="expand(${activity.id},'#parent${s.count}','#expand${s.count}','#close${s.count}','child${s.count }')">展开全部团期</a> -->
<!-- 						<a id="close${s.count}" href="javascript:void(0)" onclick="close('#child${s.count}','#expand${s.count}','#close${s.count}')">展开全部团期</a> -->
					</td>
					<td><span style="color:#eb0301">¥<c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}"><fmt:formatNumber pattern="#" value="${activity.settlementAdultPrice}" /></c:if></span>起</td>
					<td><span style="color:#eb0301">¥<c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if test="${activity.suggestAdultPrice>0}"><fmt:formatNumber pattern="#" value="${activity.suggestAdultPrice}" /></span></c:if>起</td>
					<td><c:if test="${!empty visaMap[activity.id]}">
						<c:forEach items="${visaMap[activity.id] }" var="visas">
							<c:forEach items="${visas }" var="map">
							<a href="javascript:void(0)" onClick="downloads(${map.value})">${map.key}</a>
							</c:forEach>
						</c:forEach>
					</c:if></td>
<%--					<td>${activity. }</td>--%>
					<c:set var="flag" value="0"></c:set>
					<td class="v_middle">
						<c:forEach items="${activity.activityFiles}" var="file" varStatus="s1">							
							<c:if test="${file.fileType==1}">
								<c:set var="flag" value="1"></c:set>								
								<a href="javascript:void(0)" onClick="downloads(${file.docInfo.id})"><img alt="下载" src='<c:url value="/static/images/img/team_word1.png"/>'/></a>
							</c:if>													
						</c:forEach>
						<c:if test="${flag==0 }">
								<a href="javascript:void(0)"><img alt="下载" src='<c:url value="/static/images/img/team_word2.png"/>'/></a>
						</c:if>
					</td>
					<c:set var="flag1" value="0"></c:set>
					<td class="v_middle">
						<c:forEach items="${activity.activityFiles}" var="file" varStatus="s1">
							<c:if test="${file.fileType==2}">
								<c:set var="flag1" value="1"></c:set>
								<a href="javascript:void(0)" onClick="downloads(${file.docInfo.id})"><img alt="下载" src='<c:url value="/static/images/img/team_word1.png"/>'/></a>
							</c:if>							
						</c:forEach>
						<c:if test="${flag1==0 }">
								<a href="javascript:void(0)"><img alt="下载" src='<c:url value="/static/images/img/team_word2.png"/>'/></a>
						</c:if>
					</td>
					
					<td>
						<shiro:hasPermission name="product:manager:edit"><a href="javascript:void(0)" onClick="productModify(${activity.id})">修改</a></shiro:hasPermission>&nbsp;&nbsp;&nbsp;
						<shiro:hasPermission name="product:manager:delete"><a href="javascript:void(0)" onClick="return confirmxDel('要删除该产品吗？', this.href ,${activity.id})">删除</a></shiro:hasPermission>
					</td>
										
				</tr>
				<tr id="child${s.count}" style="display:none">
					<td colspan="13">
               <div class="team_top_mian">
               <div class="team_top_jian"></div>
               <div class="team_top">
						<table id="teamTable" class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th>出团日期</th>
									<th>截团日期</th>
									<th>成人同行价(元)</th>
									<th>儿童同行价(元)</th>
									<th>成人建议零售价(元)</th>
									<th>儿童建议零售价(元)</th>
									<th>订金金额(元)</th>
									<th>预收人数</th>
									<th>余位</th>
									<th>资料截止日期</th>
									<th>签证/签证类型</th>
									<th colspan="2">操作</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
								<tr>
									<td><span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></span>
									<input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;margin-bottom:0;" disabled="disabled"></input>
									<input style="display:none;width:85px;margin-bottom:0" disabled="disabled" type="text" name="groupOpenDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker()" class="required"/> </td>
									<td><span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></span><input style="display:none;margin-bottom:0;width: 85px;" disabled="disabled" type="text" name="groupCloseDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}" />" onClick="WdatePicker()" class="required"/></td>
									<td><span style="color:#0989e2">¥<fmt:formatNumber pattern="#" value="${group.settlementAdultPrice }" /></span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="settlementAdultPrice" value="${group.settlementAdultPrice}" class="required number"/></td>
									<td><span style="color:#0989e2">¥<fmt:formatNumber pattern="#" value="${group.settlementcChildPrice }" /></span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="settlementcChildPrice" value="${group.settlementcChildPrice}" class="required number"/></td>
									<td><span style="color:#ee7605">¥<fmt:formatNumber pattern="#" value="${group.suggestAdultPrice }" /></span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="suggestAdultPrice" value="${group.suggestAdultPrice}" class="required number"/></td>
									<td><span style="color:#ee7605">¥<fmt:formatNumber pattern="#" value="${group.suggestChildPrice }" /></span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="suggestChildPrice" value="${group.suggestChildPrice}" class="required number"/></td>
									<td><span style="color:#eb0205">¥<fmt:formatNumber pattern="#" value="${group.payDeposit }" /></span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="payDeposit" value="${group.payDeposit}" class="required number"/></td>
									<td><span>${group.planPosition }</span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="planPosition" value="${group.planPosition}" class="required digits"/></td>
									<td><span style="color:#eb0205">${group.freePosition }</span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="freePosition" value="${group.freePosition}" class="required digits"/></td>
									<td><span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="visaDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/>" onClick="WdatePicker()" class="required"/></td>
									<td><span>${group.visaCountry }</span><input style="display:none;width: 85px;margin-bottom:0;" disabled="disabled" type="text" name="visaCountry" value="${group.visaCountry}" class="required"/></td>
									
									<td>
                           				<shiro:hasPermission name="group:manager:book"><a href="javascript:void(0)" onClick="reserveOrder(${group.id},${activity.id})">付全款</a></shiro:hasPermission>
										<shiro:hasPermission name="group:manager:sit">
												<a href="javascript:void(0)" onClick="occupied(${group.id},${activity.id})">${payTypes[activity.payMode]}</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="group:manager:edit"><a href="javascript:void(0)" onClick="modgroup('#groupid${s.count}${s2.count}','#savebtn${s.count}',this)">修改</a></shiro:hasPermission>
										<%-- <a href="javascript:void(0)" onClick="delgroup(${group.id},${activity.id})">删除</a> --%>
										
										<shiro:hasPermission name="group:manager:delete"><a style="color:#ec0203" href="javascript:void(0)" onClick="return confirmxCopy('要删除该产品的此条记录吗？', this.href ,${group.id},${activity.id})">删除</a></shiro:hasPermission>
										
									</td>
									<c:if test="${s2.count==1 }">
										<td rowspan="${groupsize}">
											<input id="savebtn${s.count}" type="button" value="确认" onClick="savegroup('#savebtn${s.count}',${activity.id})" style="display: none;"/>
										</td>
									</c:if>
								</tr>
							</c:forEach>
							</tbody>
						</table>
                  </div>
                  
					</td>
			</tr>
			</c:forEach>
		
		

				
		</tbody>
	</table>
	</form>
	<div class="pagination">${page}</div>
   <div class="page">
   	
   </div>	 
</body>
</html>