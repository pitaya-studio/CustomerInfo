<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>
	<c:if test="${form.msgType != 7}">公告-列表页</c:if>
	<c:if test="${form.msgType == 7}">提醒-列表页</c:if>
</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>

<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//产品名称获得焦点显示隐藏
	inputTips();
	slide();
	intoPart();
	$('.filter_check label input[type=radio]').click(function(){
		var  va = $("div.filterbox").find("input[type=radio]:checked").val(); // 获取选中的分类值
		 $("#msgthetype").val(va);
		slide();
		if(va==2){// 部门公告分类，需要选择部门后再去点击“搜索”提交
			
		}else{ // 其他分类，直接提交
			$("#searchForm").submit();
		}
	});
	
	
	// 点击选择部门
	$("div.filterbox-foot input[name='msgNoticeType']").click(function(){
		var che= $(this).attr("checked");
		if(che == "checked"){ // 将选中的部门放入 form（#searchForm）中用于提交
			var val = $(this).val();
			$("#searchForm").append("<input type='hidden' name='msgNoticeType' value='"+val+"'>");
		}else{	// 将去掉选中的部门从 form（#searchForm）中剔除
			var del = $(this).val();
			$("#searchForm").find("input[name=msgNoticeType][value='"+del+"']").remove();
		}
	});

	//处理提醒列表的标题
	handleTitle();

});

/**
 * 270 处理提醒列表的标题
 */
function handleTitle(){
	$("p.remindTitle").each(function () {
		var arr = $(this).html().split("#");
		var type = arr[3];
		var html = "<span>";
		html += arr[0];
		//单团借款 url: .../a/singlegrouporder/lendmoney/airticketLendMoneyByReviewId?orderId=5200&reviewId=ea4aeabc7ec3443a982f05a17cb8ec77
		if(type==1){
			html += "<a href='" + contextPath + "/singlegrouporder/lendmoney/airticketLendMoneyByReviewId?orderId=" + arr[4] + "&reviewId=" + arr[5] + "'>" + arr[1] + "</a>";
		//机票借款 url:.../a/activityOrder/lendmoney/airticketLendMoneyByReviewId?orderId=3625&reviewId=75119e2f79384e8d919ac8c48db991a0
		}else if(type==2){
			html += "<a href='" + contextPath + "/activityOrder/lendmoney/airticketLendMoneyByReviewId?orderId=" + arr[4] + "&reviewId=" + arr[5] +"'>" + arr[1] + "</a>";
		//签证借款(无批次) url:.../a/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?orderId=1107&reviewId=826b29e40bce4ddab08994d5d8980111
		}else if(type==3){
			html += "<a href='" + contextPath + "/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?orderId=" + arr[4] + "&reviewId=" + arr[5] +"'>" + arr[1] + "</a>";
		//签证借款(有批次)	url: .../a/visa/borrowMoney/review/visaBorrowMoneyBatchReviewDetail4Hqx?orderId=&travelerId=&flag=1&revid=f2563276132e4293831bf1a9e7ecad7f&flowType=&batchno=20160331-0004
		}else if(type==4){
			html += "<a href='" + contextPath + "/visa/borrowMoney/review/visaBorrowMoneyBatchReviewDetail4Hqx?orderId=&travelerId=&flag=1&revid=" + arr[5] + "&flowType=&batchno=" + arr[1] +"'>" + arr[1] + "</a>";
		}
		html += arr[2];
		$(this).html(html);
	});
}

// 如果上次查询为部门查询，则需要吧上次选择的部门放入form（#searchForm）
function intoPart(){
	$("div.filterbox-foot").find("input[name=msgNoticeType]:checked").each(function(){
		var val = $(this).val();
		$("#searchForm").append("<input type='hidden' name='msgNoticeType' value='"+val+"'>");
	});
}

// 判断，如果选择部门公告，则弹出部门列表
function slide(){
	var type = $("#msgthetype").val();
	if(type==2){
		$('.filterbox-foot').slideDown(300);
	}else{
		// 清空 选中的部门
		$("#searchForm input[name=msgNoticeType]").each(function(){
			$(this).remove();
		});
		$('.filterbox-foot').slideUp(300);
		
	}
}
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/message/findMsgList/0");
	$("#searchForm").submit();
}
//展开收起
function expand(obj,id){
	var tr = $(obj).parents("tr").next();
	//console.log(tr);
	if($(tr).css("display")=="none"){
		// ajax 方式，查找公告预览
		ajaxShow(id);
		$(obj).html("收起");
		$(tr).show();
		$(obj).addClass('team_a_click2');
		$(obj).parents("td").parent("tr").addClass("tr-hover");
	}else{
		$(tr).hide();
		$(obj).removeClass('team_a_click2');
		$(obj).parents("td").parent("tr").removeClass("tr-hover");
		$(obj).html("预览");
	}
}
// ajax 方法获取消息/公告预览
function ajaxShow(id){
	$.ajax({
		type : "POST",
		url : contextPath + "/message/findMsgAjax/"+id,
		data : "",
		dataType : "text",
		success : function(html){
			var json;
			try{
				json = $.parseJSON(html);
			}catch(e){
				json = {res:"error"};
				jBox.tip("系统繁忙，请稍后再试", 'error');
			}
			console.log(json);
			if(json.res == "success"){
				successAjax(json,id);
			}else{
				//jBox.tip("ssss", 'error');
			}
		}
	});
}

// ajax成功后，将ajax返回信息填入弹出层
function successAjax(json,id){
	var text = $("#"+id).find("span.show_name").text();
	if(!text){
		$("#"+id).find("div.content").append(json.msg.contentinfo+'<a href="javascript:void(0)"  onclick="goToMessageInfo('+id+')">[查看详情]</a>');
		$("#"+id).find("span.show_name").text(json.user.name);
		// 添加附件
		//console.log(json.docList);
		var jsonobj = json.docList;
		var num = new Number(jsonobj.length);
		var i = new Number(0);
		if(json.docList){
			while(i<num){
				$("#"+id+" ul.doc_show").append("<li><span>"+jsonobj[i].docName+"</span><a href='${ctx}/sys/docinfo/zipdownload/"+jsonobj[i].id+"/"+jsonobj[i].docName+"'>[下载]</a></li>");
				i++;
			}
		}
	}
}
// 删除单个消息
function jbox_isdel(id){
	$("#jbox_isdel_form").append("<input type='hidden' name='ids' value='"+id+"' />");
	//$("#jbox_isdel_form").submit();
	del_message();
}
// 批量删除消息
function jbox_isdelAll(){
	del_message();
}

// ajax 操作删除消息
function del_message(){
	var the_param = $("#jbox_isdel_form").serialize();
	the_param+="&status=2"; // 修正状态为“删除”
	$.ajax({
		type:"POST",
		url:contextPath + "/message/changeMsgStatusListAjax",
		data : the_param,
		dataType : "text",
		success : function(html){
			var json;
			try{
				json = $.parseJSON(html);
			}catch(e){
				json = {res:"error"};
				jBox.tip("系统繁忙，请稍后再试", 'error');
			}
			
			if(json.res == "success"){
				//jBox.tip("操作成功，共删除"+json.changeNum+"条记录。",'success');
				location.href = contextPath + "/message/findMsgList/0";	// 重新刷新列表页
			}
		}
	});
}


//单个消息已读   0:设为已读， 1:提醒列表的设为已还  2:提醒列表的设为已读
function jbox_isRead(id, type){
	if(type == 0 || type == 2){
		$("#jbox_isRead_form").append("<input type='hidden' name='ids' value='"+id+"' />");
	}else if(type == 1){
		$("#jbox_isShow_form").append("<input type='hidden' name='ids' value='"+id+"' />");
	}
	read_message(type);
}
// 批量消息已读  0:设为已读， 1:设为已还
function jbox_isReadAll(type){
	read_message(type);
}
//ajax 操作已读消息  0:设为已读， 1:设为已还
function read_message(type){
	if(type==0){
		var the_param = $("#jbox_isRead_form").serialize();
		var url = contextPath + "/message/changMsgListAjax/0";
		var success_url = contextPath + "/message/findMsgList/0";
	}else if(type==1){
		var the_param = $("#jbox_isShow_form").serialize();
		var url = contextPath + "/message/changMsgListAjax/1";
		var success_url = contextPath + "/message/findMsgList/7";
	}else if(type==2){
		var the_param = $("#jbox_isRead_form").serialize();
		var url = contextPath + "/message/changMsgListAjax/0";
		var success_url = contextPath + "/message/findMsgList/7";
	}

	$.ajax({
		type:"POST",
		url: url,
		data: the_param,
		dataType : "text",
		success : function(html){
			var json;
			try{
				json = $.parseJSON(html);
			}catch(e){
				json = {res:"error"};
				jBox.tip("系统繁忙，请稍后再试", 'error');
			}
			if(json.res == "success"){
				//jBox.tip("操作成功，共"+json.changeNum+"条记录设为已读。",'success');
				location.href = success_url;	// 重新刷新列表页
			}
		}
	});
}

//全选
function checkall(obj){
	if(obj.checked){ 
		$("input[name='activityId']").not("input:checked").each(function(){
				var id = $(this).val();
				$("#jbox_isdel_form").append("<input type='hidden' name='ids' value='"+id+"' />");
				$("#jbox_isRead_form").append("<input type='hidden' name='ids' value='"+id+"' />");
				this.checked=true;
			}); 
		$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
		// 隐藏单独操作（删除、已读）
		$("a.control").hide();
	}else{ 
		$("input[name='activityId']:checked").each(function(){
				$("#jbox_isdel_form").empty();
				$("#jbox_isRead_form").empty();
				this.checked=false;
			}); 
		$("input[name='allChk']:checked").each(function(){this.checked=false;});	
		// 还原单独操作（删除、已读）
		$("a.control").show();
	} 
}
//每行中的复选框
function idcheckchg(obj){
	var id = $(obj).val();
	var inputIds = $("input[name='ids'][value='"+id+"']").val();
	if(obj.checked){
		if(!inputIds){
			$("#jbox_isdel_form"). append("<input type='hidden' name='ids' value='"+id+"' />");
			$("#jbox_isRead_form").append("<input type='hidden' name='ids' value='"+id+"' />");
			$("#jbox_isShow_form").append("<input type='hidden' name='ids' value='"+id+"' />");
		}
		if($("input[name='activityId']").not("input:checked").length == 0){
			$("input[name='allChk']").not("input:checked").each(function(){
					this.checked=true;
				});
		}
	}else{
		if(inputIds){
			var inputs = $("input[name='ids'][value='"+id+"']");
			$(inputs).remove();
		}
		$("input[name='allChk']:checked").each(function(){
			this.checked=false;	
		});
	}
	// 判断被选中的项若大于一，则隐藏单独选项
	var num = 0;
	$("input[name='activityId']:checked").each(function(){
		num++;
	});
	if(num>1){
		$("a.control").hide();
	}else{
		$("a.control").show();
	}
}
/**
 * 修改
 */
function jbox_idUpdate(msgId){
	window.open(contextPath + "/message/goToEditMessage/"+msgId);	// 跳转到修正页
	location.href = contextPath + "/message/findMsgList/0";	// 重新刷新列表页
}

/**
 * 跳转到消息、公告详情
 */
function goToMessageInfo(msgId){
	window.open(contextPath + "/message/goToMessageInfo/"+msgId);	// 跳转到详情页
	location.href = contextPath + "/message/findMsgList/0";	// 重新刷新列表页
}
function goToContentUrl(contentUrl){
	window.open(contextPath +"/"+contentUrl);	// 跳转到详情页
}
</script>
<style type="text/css">
	/*子table样式*/
	.activitylist_bodyer_table tr td{vertical-align:middle}
	.activity_team_top1 .team_top{background-color:#f5f5f5}
	.activity_team_top1 .team_top{border-top:1px solid #b5c8db}
	.activity_team_top1 .team_top table{border:none;background:none}
	.activity_team_top1 .team_top table thead tr th,.team_top table tr td{border:none;background:none; background-color:#f5f5f5;}
	.activitylist_bodyer_table .team_top thead tr{background:none;}
	.activity_team_top1 table thead tr th{color:#717171}
	#contentTable, #contentTable th, #contentTable td{ vertical-align:middle;}
	#contentTable th,#contentTable td{height:40px;}
	#contentTable .team_top td{height:auto}
	.activitylist_bodyer_table .tr-hover td{}
	#control_xon span{width:19%; float:left;}
	.activitylist_bodyer_table .tr-hover td{background-color:#c7dbec;background-image:url(../images/tr-hoverbg.gif); background-repeat: repeat-x;color:#394b61;border-bottom:1px solid #b5c8db;border-left: 1px solid #b5c8db;}
	.activitylist_bodyer_table .tr-hover td:first-child{border-left:1px solid #93a5bb}
	.activitylist_bodyer_table .tr-hover td:last-child{border-right:1px solid #93a5bb}
	.activitylist_bodyer_table .tr-hover:hover td{background-color:#C7DBEC}
	.activitylist_bodyer_table tr:hover td.team_top{background-color:#e1e8ed}
	.activitylist_bodyer_table tr:hover td{background-color:#f5f5f5}
	.activity_team_top1 .team_top{border:1px solid #93a5bb;padding:0 0 5px 0;margin-bottom:20px;background-color:#e1e8ed}
	.activity_team_top1 .team_top table thead tr th.t-th2{border-bottom:1px solid #DDDDDD}
</style>
</head>
<body>
	    <page:applyDecorator name="message_list" >
	    	<page:param name="current">msglist</page:param>
	    </page:applyDecorator>
    	<!--右侧内容部分开始-->
        <div class="noticeDetails">
            <form id="searchForm"  method="post">
            	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <div class="activitylist_bodyer_right_team_co" style="padding-bottom:8px;">
                    <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="margin-left:0;">
                        <label style="width:auto;">搜索：</label><input type="text" class="txtPro inputTxt" id="conn" name="conn" value="${form.conn}" flag="istips" /><span class="ipt-tips" style="top:4px; left:47px;">标题/内容 关键字</span>
                    </div>
					<!-- 270 提醒列表页 加入提醒类型 -->
					<c:if test="${form.msgType == 7}">
						<div class="activitylist_bodyer_right_team_co2 wpr20">
							<label>提醒类型：</label>
							<select name="remindType">
								<option value="">还款提醒</option>
								<%--<option value="">不限</option>--%>
								<%--<option value="1" <c:if test="${form.remindType==1}">selected="selected"</c:if>>还款提醒</option>--%>
								<%--<option value="2" <c:if test="${form.remindType==2}">selected="selected"</c:if>>收款提醒</option>--%>
							</select>
						</div>
					</c:if>
					<div class="activitylist_bodyer_right_team_co2 wpr20" style="width:auto;margin-left:0;">
						<label class="activitylist_team_co3_text">
							<c:if test="${form.msgType == 7}">提醒时间：</c:if>
							<c:if test="${form.msgType != 7}">时间范围：</c:if>
						</label>
						<input type="text" id="orderOpenDate" class="inputTxt dateinput" name="startDate" value="${form.startDate}" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderCloseDate').value==''){$dp.$('orderCloseDate').value=vvv;}},maxDate:'#F{$dp.$D(\'orderCloseDate\')}'})" readonly="readonly" />
						<span> 至 </span>
						<input type="text" id="orderCloseDate" class="inputTxt dateinput" name="endDate" value="${form.endDate}" onclick="WdatePicker({minDate:'#F{$dp.$D(\'orderOpenDate\')}'})" readonly="readonly" />
					</div>
                    <div class="activitylist_bodyer_right_team_co3 wpr20 " >
                        <label style="width:auto;">是否已读：</label>
                        <select name="ifRead">
                        	<option value="" >不限</option>
                        	<c:if test="${form.ifRead==0 }"><option value="0"  selected>未读</option></c:if>
                        	<c:if test="${form.ifRead!=0 }"><option value="0" >未读</option></c:if>
                        	<c:if test="${form.ifRead==1 }"><option value="1"  selected>已读</option></c:if>
                        	<c:if test="${form.ifRead!=1 }"><option value="1" >已读</option></c:if>
                        </select>
                    </div>
                    <div class="form_submit">
                      <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
                    </div>
                </div>
                <!--查询结果筛选条件排序开始-->
	            <div class="filterbox">
	                <!--<div class="filter_num">查询结果<strong>1392</strong>条</div>-->
	                <input type="hidden" id="msgthetype" value="${form.msgType}"/>
	                <div class="filter_check" style="border-right:0;">
	                	<label><input type="radio" name="msgType"  value="0"  <c:if test="${form.msgType == 0}"> checked='checked'</c:if>/>全部消息和公告<strong class="lightorange">(<c:if test="${not empty messageNum}">${messageNum }</c:if>)</strong></label>
	                    <label><input type="radio" name="msgType"  value="1"  <c:if test="${form.msgType == 1}"> checked='checked'</c:if>/>全站公告<strong class="lightorange">(<c:if test="${not empty activeNum}">${activeNum }</c:if>)</strong></label>
	                    <label><input type="radio" name="msgType"  value="2"  <c:if test="${form.msgType == 2}"> checked='checked'</c:if> class="departmentNotice"/>部门公告<strong class="lightorange">(<c:if test="${not empty partActiveNum}">${partActiveNum }</c:if>)</strong></label>
	                    <label><input type="radio" name="msgType"  value="3"  <c:if test="${form.msgType == 3}"> checked='checked'</c:if> />渠道公告<strong class="lightorange">(<c:if test="${not empty agentActiveNum}">${agentActiveNum }</c:if>)</strong></label>
	                    <label><input type="radio" name="msgType"  value="4"  <c:if test="${form.msgType == 4}"> checked='checked'</c:if> />约签公告<strong class="lightorange">(<c:if test="${not empty engageActiveNum}">${engageActiveNum }</c:if>)</strong></label>
	                    <label><input type="radio" name="msgType"  value="5"  <c:if test="${form.msgType == 5}"> checked='checked'</c:if> />消息<strong class="lightorange">(<c:if test="${not empty messActiveNum}">${messActiveNum }</c:if>)</strong></label>
	                    <label><input type="radio" name="msgType"  value="6"  <c:if test="${form.msgType == 6}"> checked='checked'</c:if> />财务消息<strong class="lightorange">(<c:if test="${not empty financeActiveNum}">${financeActiveNum }</c:if>)</strong></label>
						<label><input type="radio" name="msgType"  value="7"  <c:if test="${form.msgType == 7}"> checked='checked'</c:if> />提醒<strong class="lightorange">(<c:if test="${not empty remindNum}">${remindNum }</c:if>)</strong></label>
	                </div>
	                <div class="clear"></div>    
                </div>
                <!-- 
                <div class="filterbox-foot">
	            	<c:forEach items="${departmentSet }" var="depart">
	            		<label><input type="checkbox"  name="msgNoticeType"  value="${depart.id }" checked=""/>${depart.name }</label>
	            	</c:forEach>
	            </div>
	             -->
            </form>
            <div class="filterbox-foot">
            	<c:forEach items="${departmentSet }" var="depart">
            		<label>
           				<input type="checkbox"  name="msgNoticeType"  value="${depart.id }"  
           					<c:forEach items="${depidList }" var="dep">
           						<c:if test="${dep == depart.id }">checked="checked"</c:if>
           					</c:forEach>
           				/>${depart.name }
            		</label>
            	</c:forEach>
            </div>
            <!--查询结果筛选条件排序结束-->
            <!--table数据开始-->
            <table class="activitylist_bodyer_table tableNotice" id="contentTable">
                <thead >
                    <tr>
                        <th width="40%">标题</th>
						<!-- 270 提醒时 显示提醒时间 -->
						<c:if test="${form.msgType == 7}">
							<th width="13%">提醒时间</th>
						</c:if>
						<c:if test="${form.msgType != 7}">
							<th width="13%">发布时间</th>
						</c:if>
                        <th width="10%">类型</th>
						<!-- 270 提醒 不显示部门 -->
						<c:if test="${form.msgType != 7}">
							<th width="10%">部门</th>
						</c:if>
                        <th width="5%">全选 <input type="checkbox" value="" style="margin:0;" name="allChk" onclick="checkall(this)" /></th>
                        <th width="17%">操作　　
							<c:if test="${form.msgType != 7}">
								<a href="#" onclick="jbox_isReadAll(0)">批量已读</a>
							</c:if>
							<c:if test="${form.msgType == 7}">
								<a href="#" onclick="jbox_isReadAll(2)">批量已读</a>
								&nbsp;&nbsp;<a href="#" onclick="jbox_isReadAll(1)">批量不再提醒</a>
							</c:if>
						</th>
                    </tr>
                </thead>
                <tbody>
                	<c:if test="${fn:length(page.list) <= 0 }">
	                 <tr class="toptr" >
	                 	<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
	                 </tr>
        			</c:if>
                	<c:forEach items="${page.list }"  var="msg">
	                	<tr>
	                		<td class="tableNotice-t">
								<c:if test="${msg.msgType != 7}">

									<i class="hasicon"></i>
									<c:if test="${msg.msgType==5 }"><span class="lightorange" >[系统]</span></c:if>
									<!--  标题加粗加亮 -->

									<c:if test="${msg.titleVulgarCss == 1 && msg.titleLightCss == 1}">
										<c:if test="${msg.msgType==6 }">
											<c:forEach items="${msg4CW }" var="msg4CW">
												<c:if test="${msg4CW.id==msg.id }">
													<span class="tableNotice-t"  style="color:#cc0000; font-weight:bold;">${msg4CW.title4CW[0]}<a target="_blank" href="${ctx}${msg4CW.title4CW[1]}">${msg4CW.title4CW[2]}</a>${msg4CW.title4CW[3]}</span>
												</c:if>
											</c:forEach>
										</c:if>
										<c:if test="${msg.msgType!=6 }">
											<span class="tableNotice-t"  style="color:#cc0000; font-weight:bold;">${msg.title }</span>
										</c:if>
									</c:if>
									<!--  标题加粗 -->
									<c:if test="${msg.titleVulgarCss == 1 && msg.titleLightCss == 0}">
										<c:if test="${msg.msgType==6 }">
											<c:forEach items="${msg4CW }" var="msg4CW">
												<c:if test="${msg4CW.id==msg.id }">
													<span class="tableNotice-t"  style="font-weight:bold;">${msg4CW.title4CW[0]}<a target="_blank" href="${ctx}${msg4CW.title4CW[1]}">${msg4CW.title4CW[2]}</a>${msg4CW.title4CW[3]}</span>
												</c:if>
											</c:forEach>
										</c:if>
										<c:if test="${msg.msgType!=6 }">
											<span class="tableNotice-t"  style="font-weight:bold;">${msg.title }</span>
										</c:if>
									</c:if>
									<!--  标题加亮 -->
									<c:if test="${msg.titleVulgarCss == 0 && msg.titleLightCss == 1}">
										<c:if test="${msg.msgType==6}">
										<c:forEach items="${msg4CW }" var="msg4CW">
												<c:if test="${msg4CW.id==msg.id }">
											<span class="tableNotice-t"  style="color:#cc0000;">${msg4CW.title4CW[0]}<a target="_blank" href="${ctx}${msg4CW.title4CW[1]}">${msg4CW.title4CW[2]}</a>${msg4CW.title4CW[3]}</span>
												</c:if></c:forEach>
										</c:if>
										<c:if test="${msg.msgType!=6 }">
											<span class="tableNotice-t"  style="color:#cc0000;">${msg.title }</span>
										</c:if>
									</c:if>
									<!--  标题不加粗不加亮 -->
									<c:if test="${msg.titleVulgarCss == 0 && msg.titleLightCss == 0}">
										<c:if test="${msg.msgType==6}">
										<c:forEach items="${msg4CW }" var="msg4CW">
												<c:if test="${msg4CW.id==msg.id }">
											<span class="tableNotice-t" >${msg4CW.title4CW[0]}<a target="_blank" href="${ctx}${msg4CW.title4CW[1]}">${msg4CW.title4CW[2]}</a>${msg4CW.title4CW[3]}</span>
												</c:if></c:forEach>
										</c:if>
										<c:if test="${msg.msgType!=6 }">
											<span class="tableNotice-t" >${msg.title }</span>
										</c:if>
									</c:if>

									<c:if test="${msg.ifRead == 0}"><span class="tdred">&nbsp;New</span></c:if>
									<c:if test="${msg.msgType != 6}">
										<span class="noticeLi"><a onclick="expand(this,${msg.id})" class="team_a_click" href="javascript:void(0)">预览</a></span>
									</c:if>
									<c:if test="${ not empty msg.contentUrl }">
										<span><a href="javascript:void(0)" class="team_a_click" onclick="goToContentUrl(${msg.contentUrl })">[查看]</a></span>
									</c:if>

								</c:if>
								<!-- 270 标题 -->
								<c:if test="${msg.msgType == 7}">
									<p class="remindTitle">
										<c:if test="${msg.ifRead == 0 and msg.isShow == 0}"><span class="tdred">&nbsp;New</span></c:if>
									${msg.title}</p>
								</c:if>

	                		</td>
								<td class="tc"><fmt:formatDate value="${msg.createDate }" type="date" dateStyle="long"/></td>
	                        <td>
	                        	<c:if test="${msg.msgType ==4 }">/</c:if>
								<c:if test="${msg.msgType == 7}">还款提醒
									<%--<c:if test="${msg.remindType == 1}">还款提醒</c:if>--%>
									<%--<c:if test="${msg.remindType == 2}">收款提醒</c:if>--%>
								</c:if>
	                        	<c:if test="${msg.msgType !=4 and msg.msgType != 7}">
	                        		<c:if test="${msg.msgNoticeType == 1}">单团</c:if>
		                        	<c:if test="${msg.msgNoticeType == 2}">散拼</c:if>
		                        	<c:if test="${msg.msgNoticeType == 3}">游学</c:if>
		                        	<c:if test="${msg.msgNoticeType == 4}">大客户</c:if>
		                        	<c:if test="${msg.msgNoticeType == 5}">自由行</c:if>
		                        	<c:if test="${msg.msgNoticeType == 6}">签证</c:if>
		                        	<c:if test="${msg.msgNoticeType == 7}">机票</c:if>
		                        	<c:if test="${msg.msgNoticeType == 8}">套餐</c:if>
		                        	<c:if test="${msg.msgNoticeType == 9}">其他</c:if>
		                        	<c:if test="${msg.msgNoticeType == 10}">游轮</c:if>
		                        	<c:if test="${msg.msgNoticeType == 11}">海岛游</c:if>
		                        	<c:if test="${msg.msgNoticeType == 12}">酒店</c:if>
	                        	</c:if>
	                        </td>
							<!-- 消息/公告 类型 -->  <!-- 270 提醒 不显示部门 -->
							<c:if test="${form.msgType != 7}">
								<td>
									<c:if test="${msg.msgType ==1 }">全站公告</c:if>
									<c:if test="${msg.msgType ==2 }">部门公告</c:if>
									<c:if test="${msg.msgType ==3 }">渠道公告</c:if>
									<c:if test="${msg.msgType ==4 }">约签公告</c:if>
									<c:if test="${msg.msgType ==5 }">消息</c:if>
									<c:if test="${msg.msgType ==6 }">财务消息</c:if>
								</td>
							</c:if>
	                        <td class="tc"><input type="checkbox" value="${msg.id }" name="activityId" onclick="idcheckchg(this);" /></td>
	                        <td class="tc">
								<c:if test="${form.msgType != 7}">
	                        		<a href="javascript:void(0);" onclick="jbox_isRead(${msg.id },0);" class="control">设为已读</a>
								</c:if>
								<c:if test="${form.msgType == 7}">
									<a href="javascript:void(0);" onclick="jbox_isRead(${msg.id },2);" class="control">设为已读</a>
									&nbsp;&nbsp;<a href="javascript:void(0);" class="control" onclick="jbox_isRead(${msg.id},1);">不再提醒</a>
								</c:if>
							</td>
	                	</tr>
	                	<tr  id="${msg.id }"  class="activity_team_top1 " style="display:none">
	                        <td colspan="10" class="team_top">
								<div class="content"></div>
								<!-- <div><a href="javascript:void(0)"  onclick="goToMessageInfo(${msg.id})">[查看详情]</a></div>-->
	                            <div class="noticeDetails-foot">
	                                <ul class="doc_show">
	                                <li></li>
	                                </ul>
	                                <p >发布者：<span class="show_name"></span></p>
	                                <div class="kongr"></div>
	                            </div>
	                        </td>
	                    </tr>
                	</c:forEach>
                 </tbody>
            </table>
            <!--table数据结束-->
            <!-- 删除记录 -->
            <form id="jbox_isdel_form" ></form>
            <!-- 已读记录 -->
			<form id="jbox_isRead_form"></form>
			<!-- 已还记录 -->
			<form id="jbox_isShow_form"></form>
            <div class="page">
				<div class="pagination">
					<div class="endPage">${page}</div>
					<div style="clear:both;"></div>
				</div>	
			</div>
        </div>
        <!--右侧内容部分结束-->
</body>
</html>
