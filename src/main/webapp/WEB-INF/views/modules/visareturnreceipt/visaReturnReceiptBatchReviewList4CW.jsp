<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批-还签证收据审批列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />


<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script src="${ctxStatic}/modules/visa/visaBorrowMoneyBatchReviewList.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function(){
		
		g_context_url = "${ctx}";
		
		//销售模糊匹配搜索
		$("[name=saler]").comboboxSingle();
		
		//计调模糊匹配
		$("[name=meter]").comboboxSingle();
		
		//计调模糊匹配
		//$("[name=channel]").comboboxSingle();
		//$("[name=channel]").comboboxInquiry();
		
		
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		//搜索条件筛选
		launch();
		
		show('${flag}');
		
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();
		
		//首次打印提醒：
		$(".uiPrint").hover(function(){
			$(this).find("span").show();
		},function(){
			$(this).find("span").hide();
		});
		
	});
	
	//-------处理查询条件的收起与展开-------
	function show(flag){
	//alert(isSearchConditionNotUsed());
 	if("zhankai"== flag&&!isSearchConditionNotUsed()){
		document.getElementById("showFlag").value="zhankai";
		if($('.ydxbd').is(":hidden")==true) {
			document.getElementById("showFlag").value="zhankai";
			$('.ydxbd').show();
			$('.zksx').text('收起筛选');
			$('.zksx').addClass('zksx-on');
		}
	} else{
		document.getElementById("showFlag").value="shouqi";
		$('.ydxbd').hide();
		$('.zksx').text('展开筛选');
		$('.zksx').removeClass('zksx-on');
	}
}	
//展开筛选按钮
function launch(){
	$('.zksx').click(function() {
		if($('.ydxbd').is(":hidden")==true) {
			document.getElementById("showFlag").value="zhankai";
			$('.ydxbd').show();
			$('.zksx').text('收起筛选');
			$('.zksx').addClass('zksx-on');
		}else{
			document.getElementById("showFlag").value="shouqi";
			$('.ydxbd').hide();
			$('.zksx').text('展开筛选');
			$('.zksx').removeClass('zksx-on');
		}
	});
}

function isSearchConditionNotUsed(){
	//visaType
	//sysCountryId
	//collarZoning
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	var meter = $('#meter').val();
	var channnel = $('#channnel').val();
	var saler = $('#saler').val();
	var printstatus = $('#printstatus').val();
	
	
	if(""==startTime&&""==endTime&&""==meter&&""==channnel&&""==saler&&""==printstatus){
		return true;
	}else{
		return false;
	}
}
	
	
	
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};
	function orderBySub(sortBy, obj) {
		  var temporderBy = $("#orderBy").val();
		   if(temporderBy.match(sortBy)){
			   sortBy = temporderBy;
			   if(sortBy.match(/ASC/g)){
				   sortBy = sortBy.replace(/ASC/g,"");
			   }else{
				   sortBy = $.trim(sortBy)+" ASC";
			   }
		   }
		 
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
		return false;
	};
	function statusChoose(statusNum) {
		$("#statusChoose").val(statusNum);
		$("#searchForm").submit();
	};
	
	function reviewListByRoleRealedLevel(roleChoose) {
		//alert(roleChoose);
		$("#userJobId").val(roleChoose);
		$("#searchForm").submit();
	};
	
	function sort(element){

		var _this = $(element);

		//按钮高亮
		_this.parent("li").attr("class","activitylist_paixu_moren");

		//原先高亮的同级元素置灰
		_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");

		//高亮按钮隐藏input赋值
		_this.next().val("activitylist_paixu_moren");

		//原先高亮按钮隐藏input值清空
		_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");

		var sortFlag = _this.children().attr("class");
		//降序
		if(sortFlag == "icon icon-arrow-up"){

			//改变箭头的方向
			_this.children().attr("class","icon icon-arrow-down");

			//降序
			_this.prev().val("desc");
		}
		//降序
		else if(sortFlag == "icon icon-arrow-down"){
			//改变箭头方向
			_this.children().attr("class","icon icon-arrow-up");

			//shengx
			_this.prev().val("asc");
		}

		$("#searchForm").submit();

		return false;
	}
	
	
	//按批次取消还签证收据
	function  batchCancel(batchNo){
		var html = '<div class="add_allactivity"><label>您确认要进行批次撤销操作？！</label>';
		html += '</div>';
		$.jBox(html, {
			title: "撤销确认",
			buttons:{"取消":"0","确认":"1"},
			submit:function(v, h, f){
				if (v=="1"){
					$.ajax({
						type : "post",
						url : "${ctx}/visa/hqx/returnvisareceipt/review/backReview?batchNo=" + batchNo,
						success : function(data) {
							console.log(data);
							if (data.result == "fail") {
								$.jBox.tip(data.msg, 'error');
							} else if (data.result == "success"){
								$.jBox.tip(data.msg, 'success');
								window.href = "${ctx}/visa/hqx/returnvisareceipt/visaReturnReceiptBatchReviewList/REVIEW4CW";
							} else {
								$.jBox.tip("程序内部错误!", 'error');
							}
						}
					})
				}
			},height:200,width:400});
	}
	
	
	//多选审批  选择处理  全选，反选
	function t_checkall(obj) {
		if (obj.checked) {
			$("input[name='activityId']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='allChk']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='activityId']:checked").each(function() {
				this.checked = false;
			});
			$("input[name='allChk']:checked").each(function() {
				this.checked = false;
			});
		}
	}
	
	function t_checkallNo(obj) {
		$("input[name='activityId']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
		t_allchk();
	}
	
	function t_allchk() {
		var chknum = $("input[name='activityId']").size();
		var chk = 0;
		$("input[name='activityId']").each(function() {
			//alert($(this).attr("checked")=='checked');
			if ($(this).attr("checked")=='checked') {
				chk = chk+1;
			}
		});
		//alert("chknum="+chknum+";"+"chk="+chk);
		if (chknum == chk) {//全选 
			$("input[name='allChk']").attr("checked", true);
		} else {//不全选 
			$("input[name='allChk']").attr("checked", false);
		}
	}

	//批量还签证收据
	function batchReviewvisaReturnReceipt() {
		var num = 0;
		var str = "";
		$('[name=activityId]:checkbox:checked').each(function() {
			if (num == 0) {
				str += $(this).val();
			} else {
				str += "," + $(this).val();
			}
			num++;
		});
		if ("" == str) {
			$.jBox.tip("请选择需要审批的记录！");
			return false;
		}
		var html = "<table width='100%' style='padding:10px !important; border-collapse: separate;'>"
				+ "   <tr>"
				+ "     <td> </td>"
				+ "  </tr>"
				+ "   <tr>"
				+ "     <td><p>您好，当前您提交了"
				+ num
				+ "个审批项目，是否执行批量操作？</p></td>"
				+ "  </tr>"
				+ "   <tr>"
				+ "     <td><p>备注：</p></td>"
				+ "  </tr>"
				+ "   <tr>"
				+ " <td><label>"
				+ "     <textarea name='textfield' id='textfield' style='width: 290px;'></textarea>"
				+ "   </label></td>" + " </tr>" + "</table>";
		$.jBox(html, {
			title : "批量审批",
			buttons : {
				'取消' : -1,
				'驳回' : 0,
				'通过' : 1
			},
			submit : function(v, h, f) {
				if (v == "0" || v == "1") {
					var remark = f.textfield;
					if (remark.length > 200) {
						$.jBox.tip("字符长度过长，请输入少于200个字符", 'error');
						return;
					}
					$.ajax({
						type : "post",
						url : $("#sysCtx").val() + "/visa/hqx/returnvisareceipt/review/batchReviewVisaReturnReceipt",
						data : {
							"batchNos" : str,
							"remark" : remark,
							"result" : v
						},
						success : function(data) {
							if(data.result == 'success') {
								$.jBox.tip("批量审批成功！", "info");
							} else if(data.result == 'fail'){
								$.jBox.tip("批量审批失败！", "error");
							} else {
								$.jBox.tip("程序内部出错!", "error");
							}
							window.location.href = "${ctx}/visa/hqx/returnvisareceipt/visaReturnReceiptBatchReviewList/REVIEW4CW"
						}
					})
				}
			},
			height : 250,
			width : 350
		});
	}
	
	
	function batchreviewlist(){
		
		//window.location.href = g_context_url+"/visa/workflow/borrowmoney/visaBorrowMoneyBatchReviewList?flowType=5";
		  window.location.href = g_context_url+"/visa/workflow/returnreceipt/visaReturnReceiptReviewList4CW?flowType=4";
	}
	
	
	
</script>
<style type="text/css">
.message_box{width:100%;padding:0px 0 40px 0;}
.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
.message_box_li .curret{background:#62afe7;}
.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
.message_box_li_a span{float:left;}
.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
</style>
</head>
<body>

			     <div class="message_box">
		    		<c:forEach items="${userJobs}" var="userJob">
			   			<c:if test="${conditionsMap.userJobId == userJob.id}">
			   			<div class="message_box_li">
				           
								<a onclick="reviewListByRoleRealedLevel(${userJob.id})" >
								<div class="message_box_li_a curret">
							       <span>
							            ${fns:abbrs(userJob.deptName,userJob.jobName,40)}
							       </span>
							       <c:if test="${userJob.count!=0}">
								       <div class="message_box_li_em" >
								       		  ${userJob.count}
								       </div>
							       </c:if>
							       <c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">
									<div class="message_box_li_hover">
										${userJob.deptName }_${userJob.jobName }
									</div>
								   </c:if>
								  </div>
								</a>
						  
						</div>
						</c:if> 
						<c:if test="${conditionsMap.userJobId != userJob.id}">
						<div class="message_box_li">
				             <div class="message_box_li_a">
							     <a onclick="reviewListByRoleRealedLevel(${userJob.id})">
							       <span> ${fns:abbrs(userJob.deptName,userJob.jobName,40)}</span>
							        <c:if test="${userJob.count!=0}">
								       <div class="message_box_li_em" >
								       		 ${userJob.count}
								       </div>
							        </c:if>
							        <c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">
									<div class="message_box_li_hover">
										${userJob.deptName }_${userJob.jobName }
									</div>
								   </c:if>
							    </a>
							</div>
						</div>
						</c:if> 
					 </c:forEach>
                 </div>
                 
            	 <!--右侧内容部分开始-->
                 <form id="searchForm" action="${ctx}/visa/workflow/returnreceipt/visaReturnReceiptBatchReviewList4CW" method="post">
                 
                 <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
                 <input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}" /> 
                 <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" /> <!-- ${conditionsMap.orderBy} -->
                 
                 <!-- 处理一个流程涉及多个角色审批不同level的情况的情况   -->
                 <input id="userJobId" name="userJobId"  type="hidden" value="${conditionsMap.userJobId}" />
                 <input id="flowType" name="flowType"  type="hidden" value="${conditionsMap.flowType}" />
                 
				 <!-- 排序字段 默认为1 -->
				 <input id="statusChoose" name="statusChoose" type="hidden" value="${conditionsMap.statusChoose}" />
				 
				 <!-- 处理搜索条件的展开与收起 -->
				 <input value=""  type="hidden" id="showFlag" name="showFlag"/>
            
				 <!-- 状态过滤  默认为全部 -->
				<input name="active" type="hidden" value="1" />
                	<div class="activitylist_bodyer_right_team_co">
                    	<div class="activitylist_bodyer_right_team_co2 pr wpr20">
                        	<label>团号：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="groupCode" value="${conditionsMap.groupCode}" type="text" />
                        </div>
                        <div class="form_submit">
                        	<input class="btn btn-primary" value="搜索" type="submit"/>
                        </div>
                        <a class="zksx">展开筛选</a>
                        <div class="ydxbd">
                            <div class="activitylist_bodyer_right_team_co2">
									<div class="activitylist_team_co3_text">报批日期：</div>
									<input id="startTime" class="inputTxt dateinput" name="startTime" value="${conditionsMap.startTime}" onfocus="WdatePicker()" readonly=""  /> 
									<span> 至 </span> 
									<input id="endTime"  class="inputTxt dateinput" name="endTime" value="${conditionsMap.endTime}"  onclick="WdatePicker()" readonly="" />
							</div>
							
                            <div class="activitylist_bodyer_right_team_co3">
								<div class="activitylist_team_co3_text">渠道：</div>
								<select name="channel" id="channnel">
									<option value="" selected="selected">不限</option>
									<c:if test="${not empty fns:getAgentList() }">
										<c:forEach items="${fns:getAgentList()}" var="agentinfo">
											<option value="${agentinfo.id }" <c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if> >
											    ${agentinfo.agentName}
											</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
							
                            <div class="activitylist_bodyer_right_team_co2">
									<div class="activitylist_team_co3_text">销售：</div>
									<select class="sel-w1" style='height : 28px' name="saler" id="saler">
										<option value="" selected="selected">不限</option>
										<c:forEach items="${fns:getVisaSaleUserList('1')}" var="userinfo">
											<!-- 用户类型  1 代表销售 -->
											<option value="${userinfo.id }" <c:if test="${conditionsMap.saler==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
										</c:forEach>
									</select>
							</div>
							
							 <div class="kong"></div>
							 
                            <div class="activitylist_bodyer_right_team_co1"  style="margin-top: 15px;">
									<div class="activitylist_team_co3_text">计调：</div>
									<select name="meter" id="meter">
										<option value="" selected="selected">不限</option>
										<c:forEach items="${fns:getVisaSaleUserList('3')}" var="userinfo">
											<!-- 用户类型  3 代表计调 -->
											<option value="${userinfo.id }" <c:if test="${conditionsMap.meter==userinfo.id }">selected="selected"</c:if> >${userinfo.name }</option>
										</c:forEach>
									</select>
							</div>
							 <div class="activitylist_bodyer_right_team_co1"  style="margin-top: 15px;">
								    <div class="activitylist_team_co3_text">打印状态：</div>
									<select class="sel-w1" style='height:28px' name="printstatus" id="printstatus">
										<option value="" selected="selected">不限</option>
										<option value="0" <c:if test="${conditionsMap.printstatus==0}">selected="selected"</c:if>>未打印 </option>
										<option value="1" <c:if test="${conditionsMap.printstatus==1}">selected="selected"</c:if>>已打印 </option>
									</select>
							</div>
                        </div>
                        <div class="kong"></div>
                    </div>
   
                <div class="activitylist_bodyer_right_team_co_paixu">
                	<div class="activitylist_paixu">
                    				<div class="activitylist_paixu_left">
               <ul>
                   <li>排序</li>
                   <c:choose>
			               <c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
			               <li class="activitylist_paixu_moren">
			               </c:when>
			               <c:otherwise>
			               <li class="activitylist_paixu_left_biankuang">
			               </c:otherwise>
			               </c:choose>
			               	<input type="hidden" value="" name="orderCreateDateSort">
			               	<a onclick="sort(this)">创建日期
			               		<c:choose>
			               			<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
			               			<i class="icon icon-arrow-down"></i>
			               			</c:when>
			               			<c:otherwise>
			               			<i class="icon icon-arrow-up"></i>
			               			</c:otherwise>
			               		</c:choose> 
			               	</a>
			               	<input type="hidden" value="" name="orderCreateDateCss">
			               </li>
			               
			               <c:choose>
			               <c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
			               <li class="activitylist_paixu_moren">
			               </c:when>
			               <c:otherwise>
			               <li class="activitylist_paixu_left_biankuang">
			               </c:otherwise>
			               </c:choose>
			               	<input type="hidden" value="" name="orderUpdateDateSort">
			               	<a onclick="sort(this)">更新日期
			               		<c:choose>
			               			<c:when test="${conditionsMap.orderUpdateDateSort == 'desc' }">
			               			<i class="icon icon-arrow-down"></i>
			               			</c:when>
			               			<c:otherwise>
			               			<i class="icon icon-arrow-up"></i>
			               			</c:otherwise>
			               		</c:choose>
			               	</a>
			               	<input type="hidden" value="" name="orderUpdateDateCss">
			               </li>
                           <!-- <li class="activitylist_paixu_left_biankuang orderCreateDateSort"><a onclick="sortby('ordercreateDateSort',this)">创建时间</a></li>
               			<li class="activitylist_paixu_left_biankuang orderUpdateDateSort"><a onclick="sortby('orderUpdateDateSort',this)">更新时间</a></li> -->
                 </ul>
            </div>
             </form>
                        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                        <div class="kong"></div>
                    </div>
                </div>
                <!--状态开始-->
					<div class="supplierLine">
						<a  <c:if test="${conditionsMap.statusChoose== null||conditionsMap.statusChoose== '' }">class="select"</c:if>  onClick="statusChoose('');">全部</a> 
						<a  <c:if test="${conditionsMap.statusChoose=='1' }">class="select"</c:if>  onClick="statusChoose(1);">待本人审批</a> 
						<a  <c:if test="${conditionsMap.statusChoose=='5' }">class="select"</c:if>  onClick="statusChoose(5);">审批中</a>
						<a  <c:if test="${conditionsMap.statusChoose=='0' }">class="select"</c:if>  onClick="statusChoose(0);">未通过</a>
						<a  <c:if test="${conditionsMap.statusChoose=='2' }">class="select"</c:if>  onClick="statusChoose(2);">已通过</a>
						<a  <c:if test="${conditionsMap.statusChoose=='4' }">class="select"</c:if>  onClick="statusChoose(4);">已取消</a>
						
						
							<p class="main-right-topbutt">
							   <a style="font-size:14px;padding:10px;margin:-20px;margin-right: 10px;" onclick="batchreviewlist();">游客审批列表</a>
							</p>
						
					</div>
					<!--状态结束-->
                <table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                    
                        <tr>
                            <th width="3%">序号</th>
                            <th width="5%">批次号</th>
                            <th width="5%">申请人</th>
                            <th width="5%">报批日期</th>
                            <th width="5%">借款金额</th>
                            <th width="5%">借款人数</th>
                            <th width="6%">审批状态</th>
                            <th width="6%">打印状态</th>
                            <th width="4%">操作</th>
                        </tr>
							
                    </thead>
                    <tbody>
                    <!-- 遍历显示结果 start -->
					<%
						int n = 1;
					%>
					<c:forEach items="${page.list}" var="returnReviewInfo">
                    	<tr>
                        	<td>
                        	<c:if test="${conditionsMap.statusChoose=='1' }"> 
                        	  <input type="checkbox" value="${returnReviewInfo.batchno}" name="activityId" onclick="t_allchk();" />
                        	</c:if>
                        	<%=n++%>
                        	</td>
                            <td class="batchNoTD">${returnReviewInfo.batchNo}</td>
                            <td>${returnReviewInfo.batchcreateusername}</td>
                            <td><fmt:formatDate value="${returnReviewInfo.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${returnReviewInfo.batchtotalmoney}</td>
                            <td>${returnReviewInfo.batchpersoncount}</td>
                            <td class="invoice_yes">${fns:getNextReview(returnReviewInfo.revid)}</td>
                            <!-- 处理打印状态 -->
							<c:choose>
							     <c:when test="${returnReviewInfo.isPrintFlag == '1'}">
							     		<td class="invoice_yes"><p class="uiPrint">已打印<span style="display: none;">首次打印日期<br><fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${returnReviewInfo.printTime}"/></span></p></td>
							     </c:when>
							     <c:otherwise>
							                <td>未打印</td>
							     </c:otherwise>
							</c:choose>
                      
							
                            <td class="p0">
                                <dl class="handle">
                                    <dt><img  src="${ctxStatic}/images/handle_cz.png"/></dt>
                                    <dd class="">
                                    <p>
                                        <span></span>
                                        <a openFlag="false" href="javascript:void(0)" onClick="getTravelerList(this,'${ctx}','1');">游客列表</a><br/>
                                        <a href="${ctx }/visa/workflow/borrowmoney/exportTravelerInfo?batchNo=${returnReviewInfo.batchno}&busynessType=1">游客明细</a><br/>
                                        
                                         <c:if test="${returnReviewInfo.revLevel == returnReviewInfo.curlevel && returnReviewInfo.revstatus == '1'}">
                                        <a href="${ctx}/visa/hqx/returnvisareceipt/visaReturnReceipt4HQXBatchReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=0&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&nowLevel=${returnReviewInfo.curlevel}&flowType=${conditionsMap.flowType}&batchno=${returnReviewInfo.batchNo}&fromflag=CW">审批</a><br />
                                        </c:if>
										<a href="${ctx}/visa/workflow/returnreceipt/visaReturnReceiptBatchReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=1&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&nowLevel=${returnReviewInfo.curlevel}&flowType=${conditionsMap.flowType}&batchno=${returnReviewInfo.batchno}">查看</a>
										
										<!-- 处理是否显示打印按钮 2015-04-19 新增：审批通过或审批通过后的后续操作才显示打印按钮 -->
										<c:if test="${returnReviewInfo.revstatusforprint == '2'||returnReviewInfo.revstatusforprint == '3'}">
                                             <a target="_blank"  href="${ctx}/visa/workflow/returnreceipt/visaReturnMoneyBatchFeePrint?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=1&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&nowLevel=${returnReviewInfo.curlevel}&flowType=${conditionsMap.flowType}&batchno=${returnReviewInfo.batchno}">打印</a><br />
                                        </c:if>
                                        
                                        <!-- 处于审批中的才显示   撤销按钮 -->
										<c:if test="${returnReviewInfo.isBackReview==true && returnReviewInfo.status == '1'}">
											<a  onclick="batchCancel('${returnReviewInfo.batchNo}');" >撤销</a><br />
										</c:if>

                                    </p>
                                    </dd>
                                </dl>
                            </td>
                        </tr>
                        </c:forEach>
                        <c:if test="${conditionsMap.statusChoose=='1' }"> 
	                        <tr class="checkalltd">
								<td colspan='19' class="tl">
									<label>
										<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
									</label>
									<label>
										<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
									</label>
									<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:batchReviewvisaReturnReceipt();" >批量审批</a>
								</td>
							</tr>
						</c:if>                        
                    </tbody>
                </table>
            	<!-- 分页部分 -->
				<div class="pagination clearFix">${page}</div>
				<!--右侧内容部分结束-->
</body>
</html>
