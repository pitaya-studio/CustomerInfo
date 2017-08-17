<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>还签证收据-游客列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaBorrowMoneyReviewList4Receipt.js"></script>

<script type="text/javascript">
	$(function(){
		//产品名称获得焦点显示隐藏
		inputTips();
		g_context_url = "${ctx}";
		
		$("#applyPerson").comboboxSingle();
		$("#printStatus").comboboxSingle();
		$("#order_creator").comboboxSingle();
		
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
		
		if(isSearchConditionNotUsed()) {
			$('.zksx').click();
		}
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
//筛选条件重置
var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
		
    $('#orderCdGroupCdBatchNo').val('');
    $('#applyPerson').val('');
    $('#printStatus').val('');
    $('#reviewStatus').val('');
    $('#order_creator').val('');
    $('#travlerName').val('');
    $('#applyDateFrom').val('');
    $('#applyDateTo').val('');
}

function isSearchConditionNotUsed(){
	//visaType
	//sysCountryId
	//collarZoning
	var startTime = $('#applyDateFrom').val();
	var endTime = $('#applyDateTo').val();
	var applyPerson = $('#applyPerson').val();
	var printStatus = $('#printStatus').val();
	var reviewStatus = $('#reviewStatus').val();
	var order_creator = $('#order_creator').val();
	var travlerName = $('#travlerName').val();
	
	if(""==startTime && ""==endTime 
		&& ""==applyPerson && "-99999"==applyPerson
		&&""==printStatus && "-99999"==printStatus
		&&""==reviewStatus && "-99999"==reviewStatus
		&&""==order_creator && "-99999"==order_creator
		&&""==travlerName){
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
	function  batchCancel(batchno){
		var html = '<div class="add_allactivity"><label>您确认要进行批次撤销操作？！</label>';
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
			          url:g_context_url+"/visa/workflow/returnreceipt/visaReturnReceiptBatchCancelAjax",
			          dataType:"json",
			          data:{'batchno':batchno},
			          success:function(data){
			        	  if(data.result==1){
			        		  $("#searchForm").submit();
			        	  }else{
			        		  $.jBox.tip("撤销失败！"); 
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
			if ($(this).attr("checked")=='checked') {
				chk = chk+1;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='allChk']").attr("checked", true);
		} else {//不全选 
			$("input[name='allChk']").attr("checked", false);
		}
	}
	
	//批量审批按钮 操作
	function multiBatchReviewvisaReturnReceiptbyBatchNo(ctx,orderid,agentid,obj){
	    var str="";
		$('[name=activityId]:checkbox:checked').each(function(){
			str+=$(this).val()+",";
		});
		
		var chosenNum = 0;
		if("" == str){
			$.jBox.tip("请选择需要审批的记录！"); 
			return false;
		}else{
			chosenNum = str.split(",").length-1;
		}
				
		$.jBox("iframe:"+ctx+"/orderPay/returnMoneyConfirm?orderid="+orderid+"&agentid="+agentid+"&chosenNum="+chosenNum,{		
			    title: "批量审批",
				width:830,
		   		height: 300,
		   		buttons:{'取消': 2,'驳回':0,'通过':1},
		   		persistent:true,
		   		loaded: function (h) {},
		   		submit: function(v,h,f){
		   			if(v==1 || v==0){
		   			 var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
				        dataparam={ 
				        		      batchnons:str,
								      remarks:remarks,
								      result:v
								  };
				            
				      $.ajax({ 
				          type:"POST",
				          url:ctx+"/visa/workflow/returnreceipt/multiBatchReviewvisaReturnReceiptbyBatchNo",
				          dataType:"json",
				          data:dataparam,
				          success:function(data){
				               $("#searchForm").submit();
				          }
				      });
		   			}
		   		}
		});
	}
	
	
	function batchreviewlist(){
		  window.location.href = g_context_url+"/visa/workflow/returnreceipt/visaReturnReceiptReviewList4CW?flowType=4";
	}
	
//to批次列表
function toBatchList(){
	$("#isTravellerList").val("batch");
	$("#searchForm").submit();
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
.activitylist_team_co3_text{width:100px;}
</style>
</head>
<body>

	<!--右侧内容部分开始-->
	<form id="searchForm" action="${ctx}/visa/hqx/returnvisareceipt/visaReturnReceiptBatchReviewList/REVIEW" method="post">
                 
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
		<!-- 展示的是否是游客列表 -->
		<input name="isTravellerList" id="isTravellerList" type="hidden" value="${conditionsMap.isTravellerList }">
		
        <div class="activitylist_bodyer_right_team_co">
        	<div class="activitylist_bodyer_right_team_co2 pr wpr20">
            	<label>搜索：</label>
                <input style="width:260px" class="txtPro inputTxt inquiry_left_text" value="${conditionsMap.orderCdGroupCdBatchNo }" flag="istips" name="orderCdGroupCdBatchNo" id="orderCdGroupCdBatchNo"/>
                <span style="display: block;" class="ipt-tips">团号/订单号/批次号</span>
            </div>
            <div class="form_submit">
            	<input class="btn btn-primary ydbz_x" value="搜索" type="submit"/>
                <input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
            </div>
            
            <a class="zksx">筛选</a>
            <div class="ydxbd">
	            <div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">审批发起人：</div>
					<select name="applyPerson" id="applyPerson">
						<option value="-99999" selected="selected">不限</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo"><!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }" <c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select> 
				</div>
                <div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">申请日期：</div>
					<input id="applyDateFrom" class="inputTxt dateinput" name="applyDateFrom" value="${conditionsMap.applyDateFrom}" onfocus="WdatePicker()" readonly="" /> 
					<span> 至 </span> 
					<input id="applyDateTo"  class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}"  onclick="WdatePicker()" readonly="" />
				</div>
					
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">审批状态：</div>
					<select name="reviewStatus" id="reviewStatus">
						<option value="-99999" <c:if test="${conditionsMap.reviewStatus == -99999}">selected="selected"</c:if>>不限</option>
						<option value="0" <c:if test="${conditionsMap.reviewStatus == 0 }">selected="selected"</c:if>>已驳回</option>
						<option value="1" <c:if test="${conditionsMap.reviewStatus == 1 }">selected="selected"</c:if>>审批中</option>
						<option value="2" <c:if test="${conditionsMap.reviewStatus == 2 }">selected="selected"</c:if>>已通过</option>
						<option value="3" <c:if test="${conditionsMap.reviewStatus == 3 }">selected="selected"</c:if>>已取消</option>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">下单人：</div>
					<select class="sel-w1" style='height : 28px' name="order_creator" id="order_creator">
						<option value="-99999" selected="selected">不限</option>
						<!-- 用户类型  ??? 代表下单人 -->
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<option value="${userinfo.id }" <c:if test="${conditionsMap.order_creator==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">游客：</div>
					<input id="travlerName" class="inputTxt" name="travlerName" value="${conditionsMap.travlerName}"  type="text" /> 
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
					<a href="javascript:void(0)" id="all" onClick="statusChoose('0')" <c:if test = "${conditionsMap.statusChoose == null || conditionsMap.statusChoose == 0 || conditionsMap.statusChoose == '0'}">class="select" </c:if>>全部</a>
                    <a id="todo" href="javascript:void(0)" onClick="statusChoose('1')" <c:if test = "${conditionsMap.statusChoose == '1' || conditionsMap.statusChoose == 1}">class="select" </c:if>> 待本人审批</a> 
                    <a id="todo" href="javascript:void(0)" onClick="statusChoose('2')" <c:if test = "${conditionsMap.statusChoose == '2' || conditionsMap.statusChoose == 2}">class="select" </c:if>>本人已审批</a>
               	    <a id="todoing" href="javascript:void(0)" onClick="statusChoose('3')" <c:if test = "${conditionsMap.statusChoose == '3' || conditionsMap.statusChoose == 3}">class="select" </c:if>>非本人审批</a>
					<p class="main-right-topbutt">
						<a style="font-size:14px;padding:10px;margin:-20px;margin-right: 160px;" href="javascript:void(0)" onclick="javascript:toBatchList();" target="blank">批次列表</a>
					</p>
				</div>					
					<!--状态结束-->
                <table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                    
                        <tr>
                            <th width="3%">序号</th>
                            <th width="5%">批次号</th>
                            <th width="5%">申请时间</th>
                            <th width="5%">审批发起人</th>
                            <th width="5%">收据总金额</th>
                            <th width="5%">借款总金额</th>
                            <th width="5%">借款人数</th>
                            <th width="6%">审批状态</th>
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
                        	<td class="tc">
                        	<%=n++%>
                        	<c:if test="${conditionsMap.statusChoose=='1' }"> 
                        	  <input type="checkbox" value="${returnReviewInfo.batchno}" name="activityId" >
                        	</c:if>
                        	</td>
                            <td class="batchNoTD tc">${returnReviewInfo.batchNo}</td>
                            <td class="p0">
                            	<div class="out-date">
                            		<fmt:formatDate value="${returnReviewInfo.createdate}" pattern="yyyy-MM-dd"/>
                            	</div>
                                <div class="close-date time">
                                	<fmt:formatDate value="${returnReviewInfo.createdate}" pattern="HH:mm:ss"/>
                                </div>
                            </td>
                            <td class="tc">${fns:getUserNameById(returnReviewInfo.createby)}</td>
                            <td class="tc">¥&nbsp;<span class="fbold tdorange"><c:if test="${empty returnReviewInfo.totalReceipt}">0.00</c:if>${returnReviewInfo.totalReceipt}</span></td>
                            <td class="tc">¥&nbsp;<span class="fbold tdorange"><c:if test="${empty returnReviewInfo.totalLend}">0.00</c:if>${returnReviewInfo.totalLend}</span></td>
                            <td class="tc">${returnReviewInfo.batchPersonCount}</td>
                            <td class="invoice_yes tc">
								<c:if test="${returnReviewInfo.status eq '0'}">
									已驳回
								</c:if>
								<c:if test="${returnReviewInfo.status eq '1'}">
									待${fns:getUserNameById(returnReviewInfo.currentReviewer) }审批
								</c:if>
								<c:if test="${returnReviewInfo.status eq '2'}">
									已通过
								</c:if>
								<c:if test="${returnReviewInfo.status eq '3'}">
									已取消
								</c:if>
                            </td>
                            <td class="p0">
                                <dl class="handle">
                                    <dt><img  src="${ctxStatic}/images/handle_cz.png"/></dt>
                                    <dd class="">
                                    <p>
                                        <span></span>
                                        <a openFlag="false" href="javascript:void(0)" onClick="getTravelerList(this,'${ctx}','1');">游客列表</a><br/>
                                        <a href="${ctx }/visa/workflow/borrowmoney/exportTravelerInfo?batchNo=${returnReviewInfo.batchno}&busynessType=1">游客明细</a><br/>
                                        
                                         <c:if test="${returnReviewInfo.revLevel == returnReviewInfo.curlevel && returnReviewInfo.revstatus == '1'}">
                                        <a href="${ctx}/visa/workflow/returnreceipt/visaReturnReceiptBatchReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=0&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&nowLevel=${returnReviewInfo.curlevel}&flowType=${conditionsMap.flowType}&batchno=${returnReviewInfo.batchno}&fromflag=CW">审批</a><br />
                                        </c:if>
										<a href="${ctx}/visa/workflow/returnreceipt/visaReturnReceiptBatchReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=1&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&nowLevel=${returnReviewInfo.curlevel}&flowType=${conditionsMap.flowType}&batchno=${returnReviewInfo.batchno}">查看</a>
										
										<!-- 处理是否显示打印按钮 2015-04-19 新增：审批通过或审批通过后的后续操作才显示打印按钮 -->
										<c:if test="${returnReviewInfo.revstatusforprint == '2'||returnReviewInfo.revstatusforprint == '3'}">
                                             <a target="_blank"  href="${ctx}/visa/workflow/returnreceipt/visaReturnMoneyBatchFeePrint?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=1&revid=${returnReviewInfo.revid}&flowType=${returnReviewInfo.flowType}&nowLevel=${returnReviewInfo.curlevel}&flowType=${conditionsMap.flowType}&batchno=${returnReviewInfo.batchno}"">打印</a><br />
                                        </c:if>
                                        
                                        <!-- 处于审批中的才显示   撤销按钮 -->
										<c:if test="${returnReviewInfo.isBackReview==true && returnReviewInfo.status == '1'}">
                                             <a  onclick="batchCancel('${returnReviewInfo.batchno}');" >撤销</a><br />
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
									<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:multiBatchReviewvisaReturnReceiptbyBatchNo('${ctx}',1,1,this);" >批量审批</a>
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
