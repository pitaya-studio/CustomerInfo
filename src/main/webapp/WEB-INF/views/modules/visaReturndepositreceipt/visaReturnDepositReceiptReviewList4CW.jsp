<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-还签证押金收据审核列表activity</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
<script type="text/javascript">
	$(function(){
		g_context_url = "${ctx}";
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();
		
		//产品销售和下单人切换
		switchSalerAndPicker();
		
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		
	//	renderSelects(function(){
			$("#meter").comboboxInquery();
			$("#saler").comboboxInquery();
			$("#channel").comboboxInquery();
			$("#orderCreateBy").comboboxInquery();
	//	});
		
	});
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
	
	
	//根据审核ID 取回（审核中某个审核环节的神，使之成为本人的待审）
	function  reviewGoback(revid){
		var html = '<div class="add_allactivity"><label>您确认要进行撤销操作？！</label>';
		html += '</div>';
		$.jBox(html, { 
			title: "撤销确认",
			buttons:{"取消":"0","确认":"1"},
			submit:function(v, h, f){
			if (v=="1"){
				//$("#result").val(0);//代表驳回按钮
				//$("#denyReason").val(f.reason);
				//$("#searchForm").submit();
				//后台进行撤销操作，成功后刷新页面
		        $.ajax({ 
			          type:"POST",
			          url:g_context_url+"/newreviewvisa/returndepositreceipt/returnDepositReceiptCancelAjax",
			          dataType:"json",
			          data:{'revid':revid},
			          success:function(result){
			        	  if(result.flag=='success'){			        		  
			        		  $("#searchForm").submit();
			        	  }else{
			        		  $.jBox.tip("撤销失败！" + result.msg); 
			        	  }
			          }
		        });
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
	
	//
	//批量审批
	function multiReviewVisaDepositReturnReceipt() {
		var num = 0;
		var str="";
		$('[name=activityId]:checkbox:checked').each(function(){
			if(num == 0){
				str+=$(this).val();
			} else {
				str+=","+$(this).val();
			}
			num++;
		});
		if("" == str)
		{
			$.jBox.tip("请选择需要审批的记录！"); 
			return false;
		}
		var html = "<table width='100%' style='padding:10px !important; border-collapse: separate;'>"
				  +"   <tr>"
				  +"     <td> </td>"
				  +"  </tr>"
				  +"   <tr>"
				  +"     <td><p>您好，当前您提交了"+num+"个审核项目，是否执行批量操作？</p></td>"
				  +"  </tr>"
				  +"   <tr>"
				  +"     <td><p>备注：</p></td>"
				  +"  </tr>"
				  +"   <tr>"
			      +" <td><label>"
			      +"     <textarea name='textfield' id='textfield' style='width: 290px;'></textarea>"
			      +"   </label></td>"
			      +" </tr>"
				  +"</table>";
	    $.jBox(html, {
	        title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
	            if (v == "1" || v == "2") {
		            var remark = f.textfield;
		            if(remark.length>200){
		            	$.jBox.tip("字符长度过长，请输入少于200个字符", 'error');
		            	return;
		            }
		            $.ajax({
		            	type : "post",
						url : "${ctx}/newreviewvisa/returndepositreceipt/multiReviewVisaDepositReturnReceipt",
						data:{ 
							"revIds":str,
							"remark" : remark,
							"result" : v
						},
						success : function(result) {
							if(result.flag == 1){
								$.jBox.tip("批量审批成功！", "success");
							}else{
								$.jBox.tip("批量审批完成！结果详情：" + result.message, "info");
							}
							//刷新页面
// 							$("#searchForm").submit();
						}
		            })
	            }
	        }, height: 250, width: 350
	    });
// 	    inquiryCheckBOX();
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

	<%--<div class="message_box">--%>
		    		<%--<c:forEach items="${userJobs}" var="userJob">--%>
			   			<%--<c:if test="${conditionsMap.userJobId == userJob.id}">--%>
			   			<%--<div class="message_box_li">--%>
				           <%----%>
								<%--<a onclick="reviewListByRoleRealedLevel(${userJob.id})" >--%>
								<%--<div class="message_box_li_a curret">--%>
			  		               <%--<span>--%>
							            <%--${fns:abbrs(userJob.deptName,userJob.jobName,40)}--%>
							       <%--</span>--%>
							       <%--<c:if test="${userJob.count!=0}">--%>
								       <%--<div class="message_box_li_em" >--%>
								       		  <%--${userJob.count}--%>
								       <%--</div>--%>
							       <%--</c:if>--%>
							       <%--<c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">--%>
									<%--<div class="message_box_li_hover">--%>
										<%--${userJob.deptName }_${userJob.jobName }--%>
									<%--</div>--%>
								   <%--</c:if>--%>
								  <%--</div>--%>
								<%--</a>--%>
						<%--</div>--%>
						<%--</c:if> --%>
						<%--<c:if test="${conditionsMap.userJobId != userJob.id}">--%>
						<%--<div class="message_box_li">--%>
				             <%--<div class="message_box_li_a">--%>
							     <%--<a onclick="reviewListByRoleRealedLevel(${userJob.id})">--%>
							       <%--<span> ${fns:abbrs(userJob.deptName,userJob.jobName,40)}</span>--%>
							        <%--<c:if test="${userJob.count!=0}">--%>
								       <%--<div class="message_box_li_em" >--%>
								       		 <%--${userJob.count}--%>
								       <%--</div>--%>
							        <%--</c:if>--%>
							        <%--<c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">--%>
									<%--<div class="message_box_li_hover">--%>
										<%--${userJob.deptName }_${userJob.jobName }--%>
									<%--</div>--%>
								   <%--</c:if>--%>
							    <%--</a>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--</c:if> --%>
					 <%--</c:forEach>--%>
                 <%--</div>--%>

		<!--右侧内容部分开始-->
		<form id="searchForm" action="${ctx}/newreviewvisa/returndepositreceipt/visaReturnDepositReceiptReviewList4CW" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}" />
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" /> <!-- ${conditionsMap.orderBy} -->

			<!-- 处理一个流程涉及多个角色审核不同level的情况的情况   -->
			<input id="userJobId" name="userJobId"  type="hidden" value="${conditionsMap.userJobId}" />
			<input id="flowType" name="flowType"  type="hidden" value="${conditionsMap.flowType}" />

			<!-- 排序字段 默认为1 -->
			<input id="statusChoose" name="statusChoose" type="hidden" value="${conditionsMap.statusChoose}" />

			<!-- 状态过滤  默认为全部 -->
			<input name="active" type="hidden" value="1" />
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input  class="txtPro inputTxt searchInput" id="wholeSalerKey" name="groupCode" value="${conditionsMap.groupCode}" type="text" placeholder="请输入团号" />
				</div>
				<a class="zksx">筛选</a>
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" value="搜索" type="submit"/>
				</div>
				<div class="ydxbd">
					<span></span>
					<div class="activitylist_bodyer_right_team_co1" >
						<label class="activitylist_team_co3_text">订单编号：</label>
						<input id="orderNum" class="inputTxt" name="orderNum" value="${conditionsMap.orderNum}"  type="text" />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">押金金额：</label>
						<input id="depositeAmount" class="inputTxt" name="depositeAmount" value="${conditionsMap.depositeAmount}"  type="text" />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">报批日期：</div>
						<input id="" class="inputTxt dateinput" name="startTime" value="${conditionsMap.startTime}" onfocus="WdatePicker()" readonly="" />
						<span> 至 </span>
						<input id=""  class="inputTxt dateinput" name="endTime" value="${conditionsMap.endTime}"  onclick="WdatePicker()" readonly="" />
					</div>
					<div class="activitylist_bodyer_right_team_co1" >
						<label class="activitylist_team_co3_text">渠道选择：</label>
						<div class="selectStyle">
							<select name="channel" id="channel">
								<option value="">全部</option>
								<c:if test="${not empty fns:getAgentList() }">
									<c:forEach items="${fns:getAgentList()}" var="agentinfo">
										<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
										<c:choose>
										   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}">
											   <option value="${agentinfo.id }" <c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if> >
												   直客
												</option>
										   </c:when>
										   <c:otherwise>
											   <option value="${agentinfo.id }" <c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if> >
													${agentinfo.agentName}
												</option>
										   </c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1" >
						<label class="activitylist_team_co3_text">销售：</label>
						<div class="selectStyle">
							<select name="saler" id="saler" >
								<option value="" selected="selected">不限</option>
								<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
									<!-- 用户类型  1 代表销售 -->
									<option value="${userinfo.id }" <c:if test="${conditionsMap.saler==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
					   <div class="activitylist_team_co3_text">下单人：</div>
						<div class="selectStyle">
							<select id="createBy" name="orderCreateBy" >
								<option value="" selected="selected">不限</option>
								<c:forEach items="${createByList}" var="createByIdandName">
									<option value="${createByIdandName.split(',')[0]}" <c:if test="${conditionsMap.orderCreateBy==createByIdandName.split(',')[0]}">selected="selected"</c:if>>
									  ${createByIdandName.split(',')[1]}
									</option>
								</c:forEach>
							 </select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">游客姓名：</label>
						<input id="travlerName" class="inputTxt" name="travlerName" value="${conditionsMap.travlerName}"  type="text" />
					</div>
					<div class="activitylist_bodyer_right_team_co1" >
						<label class="activitylist_team_co3_text">计调：</label>
						<div class="selectStyle">
							<select name='meter' id="meter">
								<option value="" selected="selected">不限</option>
								<c:forEach items="${fns:getSaleUserList('3')}" var="userinfo">
									<!-- 用户类型  3 代表计调 -->
									<option value="${userinfo.id }" <c:if test="${conditionsMap.meter==userinfo.id }">selected="selected"</c:if> >${userinfo.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
			</div>
		</form>
		<!--状态开始-->
		<div class="supplierLine">
			<a href="javascript:void(0)" id="all" onClick="statusChoose('0')" <c:if test = "${conditionsMap.statusChoose == null || conditionsMap.statusChoose == 0 || conditionsMap.statusChoose == '0'}">class="select" </c:if>>全部</a>
			<a id="todo" href="javascript:void(0)" onClick="statusChoose('1')" <c:if test = "${conditionsMap.statusChoose == '1' || conditionsMap.statusChoose == 1}">class="select" </c:if>> 待本人审批</a>
			<a id="todo" href="javascript:void(0)" onClick="statusChoose('2')" <c:if test = "${conditionsMap.statusChoose == '2' || conditionsMap.statusChoose == 2}">class="select" </c:if>>本人已审核</a>
			<a id="todoing" href="javascript:void(0)" onClick="statusChoose('3')" <c:if test = "${conditionsMap.statusChoose == '3' || conditionsMap.statusChoose == 3}">class="select" </c:if>>非本人审批</a>
		</div>
		 <div class="activitylist_bodyer_right_team_co_paixu">
			 <div class="activitylist_paixu">
				 <div class="activitylist_paixu_left">
					 <ul>
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
				 <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
				 <div class="kong"></div>
			 </div>
		 </div>
					<!--状态结束-->
                <table id="contentTable" class="table mainTable activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="6%">订单编号</th>
                            <th width="6%">团号</th>
                            <th width="4%">计调</th>
                            <th width="5%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
                            <th width="4%">游客</th>
                            <th width="9%">下单时间</th>
                            <th width="5%">订单总额</th>
                            <th width="6%">已付总金额<br>到账总金额</th>
                            <th width="9%">报批日期</th>
                            <th width="6%">押金收据金额</th>
                            <th width="6%">上一环节<br />审批人</th>
                            <th width="11%">原因备注</th>
                            <th width="10%">审核状态</th>
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
                        	  <input type="checkbox" value="${returnReviewInfo.revid}" name="activityId" >
                        	</c:if>
                        	<%=n++%>
                        	</td>
                            <td>${returnReviewInfo.orderno}</td>
                            <td>
                            	<div class="tuanhao_cen onshow">${returnReviewInfo.groupcode}</div>
                    		</td>
                            <td>${fns:getUserNameById(returnReviewInfo.jidcreateby)}</td>
                            <td>
                                <span class="order-saler onshow">${fns:getUserNameById(returnReviewInfo.salecreateby)}</span>
	                            <span class="order-picker">${fns:getUserNameById(returnReviewInfo.orderCreateby)}</span>
                            </td>
                            <td>${returnReviewInfo.tname}</td><!-- tid 为游客id -->
                            <td class="tc"><fmt:formatDate value="${returnReviewInfo.orderdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <span class="tdorange fbold">
									${fns:getMoneyAmountBySerialNum(returnReviewInfo.totalmoney,2)}
								</span>
                            </td>
                            <td class="p0 tr">	
									<div class="yfje_dd">	
										<span class="fbold">
											${fns:getMoneyAmountBySerialNum(returnReviewInfo.payedmoney,2)}
										</span>
									</div>
									<div class="dzje_dd">
										<span class="fbold">
											${fns:getMoneyAmountBySerialNum(returnReviewInfo.accountedmoney,2)}
										</span>
									</div>
							</td>	
                            <td class="tc"><fmt:formatDate value="${returnReviewInfo.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td class="tr">¥&nbsp;<span class="fbold tdorange">${returnReviewInfo.myValue}</span></td>
                            <td>${fns:getUserNameById(returnReviewInfo.lastoperator)}</td>
                            <td>${returnReviewInfo.createReason}</td>
                            <%-- <td class="invoice_yes">${fns:getNextReview(returnReviewInfo.revid)}</td> --%>
                            <td>${returnReviewInfo.status }</td>
                            <td class="p0">
                                <dl class="handle">
                                    <dt><img  src="${ctxStatic}/images/handle_cz_rebuild.png"/></dt>
                                    <dd class="">
                                    <p>
                                        <span></span>
                                        <c:if test="${returnReviewInfo.isCurReviewer == true && returnReviewInfo.status == '1'}">
                                        <a href="${ctx}/newreviewvisa/returndepositreceipt/visaReturnDepositeReceiptReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=0&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&flowType=${conditionsMap.flowType}&fromflag=CW">审批</a><br />
                                        </c:if>
										<a href="${ctx}/newreviewvisa/returndepositreceipt/visaReturnDepositeReceiptReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=1&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&flowType=${conditionsMap.flowType}">查看</a>
										
										<!-- 处于审核中的才显示   撤销按钮 -->
										<c:if test="${returnReviewInfo.isBackReview==true && returnReviewInfo.status == '1'}">
                                             <a onclick="reviewGoback('${returnReviewInfo.revid}');" >撤销</a><br />
                                        </c:if>
                                        
                                    </p>
                                    </dd>
                                </dl>
                            </td>
                        </tr>
                        </c:forEach>
                        <%--<c:if test="${conditionsMap.statusChoose=='1' }"> --%>
	                        <%--<tr class="checkalltd">--%>
								<%--<td colspan='19' class="tl">--%>
									<%--<label>--%>
										<%--<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选--%>
									<%--</label>--%>
									<%--<label>--%>
										<%--<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选--%>
									<%--</label>--%>
									<%--&lt;%&ndash; <a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:multiReviewVisaDepositReturnReceipt('${ctx}',1,1,this);" >批量审批</a> &ndash;%&gt;--%>
									<%--<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:multiReviewVisaDepositReturnReceipt();" >批量审批</a>--%>
								<%--</td>--%>
							<%--</tr>--%>
						<%--</c:if>--%>
                    </tbody>
                </table>
            	<!-- 分页部分 -->
					<%--<div class="pagination clearFix">${page}</div>--%>

	<div class="page">
		<c:if test="${conditionsMap.statusChoose=='1' }">
		<div class="pagination">
				<dl>
					<dt>
						<input name="allChk" onclick="t_checkall(this)" type="checkbox">全选
					</dt>
					<dt>
						<input name="reverseChk" onclick="t_checkallNo(this)" type="checkbox">反选
					</dt>
					<dd>
						<input class="btn ydbz_x" type="button" id="piliang_o_${result.orderId}" value="批量审批" onclick="javascript:multiReviewDepositToWarrant('${ctx}',1,1,this);">
					</dd>
				</dl>
			</div>
		</c:if>
		<div class="pagination clearFix">${page}</div>
	</div>
				<!--右侧内容部分结束-->
</body>
</html>
