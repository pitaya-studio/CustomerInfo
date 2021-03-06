<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>借款-列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jquery.validate.min.css" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

<!-- 20150810 批量审批js -->
<script type="text/javascript" src="${ctxStatic}/review/borrowing/common/newBatchReview.js"></script>
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
// 		inputTips();
		//团号和产品切换
		switchNumAndPro();
		//产品名称文本框提示信息
		$("#channel").comboboxInquiry();
		$("#local-travel").comboboxInquiry();                
		$("#order-picker").comboboxInquiry();
		$("#country").comboboxInquiry();
//		renderSelects(selectQuery());
		selectQuery();
		
		inputTips();
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		//首次打印提醒
		$(".uiPrint").hover(function(){
			$(this).find("span").show();
		},function(){
			$(this).find("span").hide();
		});
		//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey']");
			var searchFormselect = $("#searchForm").find("select[name!='orderType']");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
// 			if(inputRequest||selectRequest) {
// 				$('.zksx').click();
// 			}
			
		//团号和产品切换
		switchNumAndPro();
		
		//产品销售和下单人切换
		switchSalerAndPicker();
		
		$(".bgMainRight").delegate("select[class='selectType']","change",function(){
		   $("#searchForm").submit();
		 })
			
		$("#contentTable").delegate(".tuanhao","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.chanpin_cen').removeClass('onshow');
	        $('.tuanhao_cen').addClass('onshow');
	    });
   
	    $("#contentTable").delegate(".chanpin","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.tuanhao_cen').removeClass('onshow');
	         $('.chanpin_cen').addClass('onshow');
	        
	    });
	    
		$(".activitylist_paixu_left li").each(function(){
		    if ($(this).hasClass("li"+orderBy[0])){
		        orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
		        $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
		        $(this).attr("class","activitylist_paixu_moren");
		    }
		});
			
		updateOrderStyle($("#orderBy").val());
	});
	
	function selectQuery() {
		$("#approval ").comboboxInquiry();
		//渠道改为可输入的select
		$("#modifyAgentInfo").comboboxInquiry();
	}
	
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};
	/**
 * 查询条件重置
 * 
 */
 
 $(function(){
    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }        
           
   $("#orderTimeBegin").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
	$("#orderTimeEnd").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
           
           
    
});
var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    
    $('#channelrefund').val('');
    $('#order-picker').val('');
    $('#order-saler').val('');
    $('#meterrefund').val('');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
}

function orderBySub(orderNum) {
	$("#orderBy").val(orderNum);
	$("#searchForm").submit();
};

function statusChoose(statusNum) {
	$("#reviewStatus").val(statusNum);
	$("#searchForm").submit();
};
	
//修改排序显示样式
function updateOrderStyle(orderBy) {
	if(orderBy == "create_date") {
		if($("#ascOrDesc").val() == "asc") {
			$("#createDateLi").find("i").attr("class", "icon icon-arrow-up");
		} else {
			$("#createDateLi").find("i").attr("class", "icon icon-arrow-down");
		} 
		$("#createDateLi").attr("class", "activitylist_paixu_moren");
		$("#updateDateLi").attr("class", "activitylist_paixu_left_biankuang");
		$("#updateDateLi").find("i").attr("class", "icon");
	} else {
		$("#createDateLi").attr("class", "activitylist_paixu_left_biankuang");
		$("#updateDateLi").attr("class", "activitylist_paixu_moren");
		$("#createDateLi").find("i").attr("class", "icon");
		
		if($("#ascOrDesc").val() == "asc") {
			$("#updateDateLi").find("i").attr("class", "icon icon-arrow-up");
		} else {
			$("#updateDateLi").find("i").attr("class", "icon icon-arrow-down");
		} 
	}
}

function orderBy(orderBy){
	if($("#orderBy").val() == orderBy) {
		if($("#ascOrDesc").val() == "asc") {
			$("#ascOrDesc").val("desc");
		} else {
			$("#ascOrDesc").val("asc");
		}
		
	} else {
		$("#orderBy").val(orderBy);
		$("#ascOrDesc").val("desc");
	}
	
	//修改排序样式
	updateOrderStyle(orderBy);
	
	$("#searchForm").submit();
}

//查看审批信息
function viewReviewInfo(boproductType,orderId,rid){
	if(boproductType==7){
		window.open("${ctx}/activityOrder/lendmoney/airticketLendMoneyByReviewId?orderId=" + orderId + "&reviewId=" + rid);
	} else if(boproductType==6) {
		window.open("${ctx}/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?orderId=" + orderId + "&reviewId=" + rid);
	} else{
		window.open("${ctx}/singlegrouporder/lendmoney/airticketLendMoneyByReviewId?orderId=" + orderId + "&reviewId=" + rid);
	}	
}
//审批明细
function reviewBorrowing(boproductType,orderId,rid){

// 	var rrid =rid;
	if(boproductType==7){
		window.open("${ctx}/activityOrder/lendmoney/reviewPlaneBorrowingInfo?orderId=" + orderId + "&rid="+rid);
	} else if(boproductType==6){
		//window.open("${ctx}/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?flag=0&orderId=" + orderId + "&reviewId="+rid,"_self");
		window.location.href="${ctx}/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?flag=0&orderId=" + orderId + "&reviewId="+rid;
	} else{
		window.open("${ctx}/singlegrouporder/lendmoney/reviewPlaneBorrowingInfo?orderId=" + orderId + "&rid="+rid);
	}
	
}

//同一审批流程切换审批角色查询
function chooseRole(rid){
	$("#rid").val(rid);
	$("#searchForm").submit();
}


//审批撤销
function backOutReview(rid){
	
	$.jBox.confirm("确定要撤销审批吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				async: false, 
				url : "${ctx}/activityOrder/lendmoney/backBorrowAmount",
				data : {
					rid: rid
				},
				success : function(msg) {
					if(msg == "1"){
						top.$.jBox.tip("撤销成功！");
						$("#searchForm").submit();
					}else{
						jBox.tip(msg.message);
					}
				}
			});
		}
	});
		
}

function jbox__shoukuanqueren_chexiao_fab() {
	var num = 0;
	 var str="";
	$('[name=checkboxrevid]:checkbox:checked').each(function(){
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
					url : "${ctx}/depositenew/batchdepositeReview",
					data:{ 
						"revIds":str,
						"remark" : remark,
						"result" : v
					},
					success : function(msg) {
						$.jBox.tip(msg.msg);
						$("#searchForm").submit();
					}
	            })
            }
        }, height: 250, width: 350
    });
    inquiryCheckBOX();
}
</script>

<style type="text/css">
.rolelist_btn .ydbz_x{float:right}
 /**订单切换卡**/
.tuanhao,.chanpin{cursor:pointer; color:#0c85d8;}
.tuanhao.on,.chanpin.on{ color:#717171; cursor:default;}
.tuanhao_cen,.chanpin_cen{display:none;}
.onshow{ display:block;}.lianyun_name{color:#FF6600; display:block}
.message_box{width:100%;padding:0px 0 40px 0;}
.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
.message_box_li .curret{background:#62afe7;}
.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
.message_box_li_a span{float:left;}
.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
.activitylist_team_co3_text{width:80px;}
/*.activitylist_paixu_right {margin-top: -20px;}*/
/*UI-V2改版 added by zrq*/
.custom-combobox .ui-corner-left {
	border-top-right-radius: 0px;
	border-bottom-right-radius: 0px;
}
</style>

</head>

<body>

	<div id="sea">
	<!--右侧内容部分开始-->	
					
	   <form id="searchForm" action="${ctx}/newOrderReview/manage/getBorrowingList"  method="post">
	   				<input id="rid" name="rid" type="hidden" value="${reviewVO.rid}" />
					<input id="userLevel" name="userLevel" type="hidden" value="${reviewVO.userLevel}" />
					<input id="reviewStatus" name="reviewStatus" type="hidden" value="${reviewVO.reviewStatus}" />
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" /> 
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" /> 
					<input id="orderBy" name="orderBy" type="hidden" value="${reviewVO.orderBy}" />
					<input type="hidden" name="ascOrDesc" id="ascOrDesc" value="${reviewVO.ascOrDesc }" />
					<input id="revid" name="revid" type="hidden" value="0" />
					<!-- 排序字段 默认为1 -->
					<input id="statusChoose" name="statusChoose" type="hidden" value="0" />
					<input id="orderType" name="orderType" type="hidden" value="${reviewVO.orderType}" />
					<!-- 流程类型 退款 退票 -->
					<input name="active" type="hidden" value="1" />
                	<div class="activitylist_bodyer_right_team_co">
                    	<div class="activitylist_bodyer_right_team_co2 pr">
                       		<input class="inputTxt searchInput inputTxtlong" id="wholeSalerKey" name="searchGOP" placeholder="团号/产品名称/订单号" value="${conditionsMap.searchGOP}" flag="istips" type="text"/>
                        </div>
						<a class="zksx">筛选</a>
                        <div class="form_submit">
                        	<input class="btn btn-primary ydbz_x" value="搜索" type="submit">
                        </div>
                        <div class="ydxbd">
							<span></span>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">产品类型：</label>
								<div class="selectStyle">
									<select name="productType">
										<option value=""   >全部</option>
										<option value="1"  <c:if test="${conditionsMap.productType==1}">selected="selected"</c:if>>单团</option>
										<option value="2"  <c:if test="${conditionsMap.productType==2}">selected="selected"</c:if>>散拼</option>
										<option value="3"  <c:if test="${conditionsMap.productType==3}">selected="selected"</c:if>>游学</option>
										<option value="4"  <c:if test="${conditionsMap.productType==4}">selected="selected"</c:if>>大客户</option>
										<option value="5"  <c:if test="${conditionsMap.productType==5}">selected="selected"</c:if>>自由行</option>
										<c:if test="${companyId ne '68' }">
											<option value="6"  <c:if test="${conditionsMap.productType==6}">selected="selected"</c:if>>签证</option>
										</c:if>
										<option value="7"  <c:if test="${conditionsMap.productType==7}">selected="selected"</c:if>>机票</option>
									</select>
								</div>
                            </div>
                            <div class="activitylist_bodyer_right_team_co3">
                                <label class="activitylist_team_co3_text">渠道选择：</label>
                          		<select class="width-select-channel"  name="channel" id="modifyAgentInfo" >
									<option value="">全部</option>
									<c:if test="${not empty fns:getAgentListAddSort()}">
										<c:forEach items="${fns:getAgentListAddSort()}" var="agentinfo">
											<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
											<c:choose>
											   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}"> 
											       <option value="${agentinfo.id }"<c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>直客</option>
											   </c:when>
											   <c:otherwise>
											       <option value="${agentinfo.id }"<c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option>
											   </c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
								</select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">出纳确认：</label>
								<div class="selectStyle">
									<select name="cashConfirm">
										<option value="">全部</option>
										<option value="1"
											<c:if test="${conditionsMap.cashConfirm eq '1' }">selected="selected"</c:if>>已付</option>
										<option value="0"
											<c:if test="${conditionsMap.cashConfirm eq '0' }">selected="selected"</c:if>>未付</option>
									</select>
								</div>
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">审批发起人：</label>
                                <select name="applyPerson" id="approval">
									<option value="" selected="">全部</option>
									<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
										<option value="${userinfo.id }"
											<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}</option>
									</c:forEach>
								</select> 
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">借款金额：</label>
                              	<input type="text" name="lendMoneyFrom" value="${conditionsMap.lendMoneyFrom}" /> 
	                            <span> 至 </span> 
	                            <input type="text" name="lendMoneyTo"   value="${conditionsMap.lendMoneyTo}"   />
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">审批状态：</label>
								<div class="selectStyle">
									<select name="selectReviewStatus">
										<option value="">全部</option>
										<option value="1" <c:if test="${conditionsMap.selectReviewStatus eq '1' }">selected="selected"</c:if>>审批中</option>
										<option value="2" <c:if test="${conditionsMap.selectReviewStatus eq '2' }">selected="selected"</c:if>>审批通过</option>
										<option value="0" <c:if test="${conditionsMap.selectReviewStatus eq '0' }">selected="selected"</c:if>>审批驳回</option>
										<option value="3" <c:if test="${conditionsMap.selectReviewStatus eq '3' }">selected="selected"</c:if>>取消申请</option>
									</select>
								</div>
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">申请日期：</label>
                                 <input id="" class="inputTxt dateinput"
								 name="applyDateFrom" value="${conditionsMap.applyDateFrom}" onclick="WdatePicker()" readonly="readonly" /> <span>
								   至 </span> <input id="" class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}"
								 onclick="WdatePicker()" readonly="readonly" />
                            </div>   
                           <%--  <div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
								<select name="paymentType">
									<option value="">不限</option>
									<c:forEach var="pType" items="${fns:findAllPaymentType()}">
										<option value="${pType[0] }"
											<c:if test="${conditionsMap.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
									</c:forEach>
								</select>
							</div>  --%>                                              
                        </div>
                        <div class="kong"></div>
                    </div>
                </form>
		<div class="supplierLine">
			<a <c:if test="${reviewVO.reviewStatus==0 }">class="select"</c:if> onClick="statusChoose(0);">全部</a>
			<a <c:if test="${reviewVO.reviewStatus==1 }">class="select"</c:if> onClick="statusChoose(1);">待本人审批</a>
			<a <c:if test="${reviewVO.reviewStatus==2 }">class="select"</c:if> onClick="statusChoose(2);">本人已审批</a>
			<a <c:if test="${reviewVO.reviewStatus==3 }">class="select"</c:if> onClick="statusChoose(3);">非本人审批</a>
		</div>

		<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
							<ul>
								<li class="activitylist_paixu_moren" id="createDateLi">
									<a href="javascript:void(0);" onclick="orderBy('create_date')">创建时间<i class="icon"></i></a>
								</li>
								<li class="activitylist_paixu_left_biankuang" id="updateDateLi">
									<a href="javascript:void(0);" onclick="orderBy('update_date')">更新时间<i class="icon"></i></a>
								</li>
							</ul>
						</div>
						<div class="activitylist_paixu_right">
							查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
						</div>
						<div class="kong"></div>
					</div>
				</div>
                <table id="contentTable" class="table mainTable activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="8%">订单号</th>
                            <th width="8%"> <span class="tuanhao on">团号</span> / <span class="chanpin">产品名称</span></th>
                            <th width="5%">产品类型</th>
                            <th width="5%">申请时间</th>
                            <th width="7%">审批发起人</th>
                            <th width="6%">渠道商</th>
<!--                             <th width="6%">销售</th> -->
                            <th width="6%"  class="tr">订单金额</th>
                            <th width="6%"  class="tr">已收金额<br />到账金额</th>
<!--                             <th width="6%">款项名称</th> -->
                            <th width="7%">借款金额</th>
                            <th width="6%">上一环节审批人</th>
                            <th width="7%">审批状态</th>
                            <th width="7%">出纳确认</th>
                            <th width="6%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:if test="${fn:length(page.list) <= 0 }">
						<tr class="toptr" >
			                 <td colspan="16" style="text-align: center;">
								 暂无搜索结果
			                 </td>
						</tr>
					</c:if>
					<c:forEach items="${page.list}" var="reviewInfos" varStatus="status">
                        <tr>
                         	<td>
								<!-- 20150804 add 多选框 -->
								<c:if test="${reviewVO.reviewStatus==1 }">
								 	<input type="checkbox" value="${reviewInfos.reviewId}" name="activityId" >
								</c:if>
								${status.count }
							</td>
                            <td>
                            	${reviewInfos.order_no}
                            </td>
                            <td>
                            	<div title="4535DFDSF" class="tuanhao_cen onshow">
                            		 ${reviewInfos.groupCode}
                            	</div>
                                <div title="${reviewInfos.product_name}" class="chanpin_cen qtip"> 
                                	<c:if test="${reviewInfos.product_type eq '7'}">
	                               		<a href="${ctx}/airTicket/actityAirTickettail/${reviewInfos.product_id}" target="_blank">${reviewInfos.product_name}</a>
                                	</c:if>
                                	<c:if test="${reviewInfos.product_type ne '7'}">
	                               		<a href="${ctx}/activity/manager/detail/${reviewInfos.product_id}" target="_blank">${reviewInfos.product_name}</a>
                                	</c:if>
                                </div>
                            </td>
                            <td class="tc">
                            	<c:if test="${reviewInfos.product_type eq '7'}">
                            		机票
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '1'}">
                            		单团
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '2'}">
                            		散拼
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '3'}">
                            		游学
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '4'}">
                            		大客户
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '5'}">
                            		自由行
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '6'}">
                            		签证
                            	</c:if>
                            	<c:if test="${reviewInfos.product_type eq '10'}">
                            		游轮
                            	</c:if>
                            </td>
                            <td class="p0">
                            	<div class="out-date">
                            		<fmt:formatDate value="${reviewInfos.create_date}" pattern="yyyy-MM-dd"/>
                            	</div>
                                <div class="close-date time">
                                	<fmt:formatDate value="${reviewInfos.create_date}" pattern="HH:mm:ss"/>
                                </div>
                            </td>
                            <td class="tc">${fns:getUserNameById(reviewInfos.create_by)}</td>
                            <td class="tc">
                            	<c:if test="${reviewInfos.agentName ne '' && reviewInfos.agentName ne '-1'}">
                            			${reviewInfos.agent_name}
                            	</c:if>
                            	<c:if test="${reviewInfos.agentName eq '-1'}">
									      ${reviewInfos.nagentName}
                            	</c:if>
                            </td>
<!--                             <td class="tc">${fns:getUserNameById(reviewInfos.saler)}	</td> -->
                            <td class="tr">
                            	<span class="fbold tdred">${fns:getOrderMoney(reviewInfos.product_type, reviewInfos.order_id, '1')}</span>
                            </td>
                            <td class="tr">
					        	<div class="yfje_dd">
									<span class="fbold">${fns:getOrderMoney(reviewInfos.product_type, reviewInfos.order_id, '2')}</span>
								</div>
								<div class="dzje_dd">
									<span class="fbold">${fns:getOrderMoney(reviewInfos.product_type, reviewInfos.order_id, '3')}</span>
								</div>
			        		</td>
<!--                             <td class="tr"> -->
<!--                             	${reviewInfos.lendName } -->
<!--                             </td> -->
                            <td class="tr"><span class="fbold tdorange">${reviewInfos.payPrice}</span></td>
                            <td class="tc">
                         	   ${fns:getUserNameById(reviewInfos.last_reviewer)}
                            </td>
                            <td class="invoice_yes tc">
									${fns:getChineseReviewStatus(reviewInfos.status,reviewInfos.current_reviewer)}
                            </td>
                            <td class="invoice_no tc">
                            	<c:if  test="${reviewInfos.pay_status eq 'true'}">
                            			已付款
                            	</c:if>
                            	<c:if  test="${reviewInfos.pay_status eq 'false'}">
                            			未付款
                            	</c:if>
                            </td>
                            <td class="p0">
	                            <dl class="handle">
	                                <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png" /></dt>
	                                <dd class="">
                               		  <p>
	                                        <span></span>
											<a href="javascript:viewReviewInfo('${reviewInfos.product_type }','${reviewInfos.order_id}','${reviewInfos.id}')">查看</a><br/>
											<c:if test="${reviewInfos.isCurReviewer == true}">
												<a href="javascript:reviewBorrowing('${reviewInfos.product_type }','${reviewInfos.order_id}','${reviewInfos.id}')">审批</a>
											</c:if>
											<!-- 审批撤销 -->
											<c:if test="${reviewInfos.isBackReview == true}">
												<a href="javascript:backOutReview('${reviewInfos.id}')">撤销</a>
											</c:if>      
											<c:if test="${reviewInfos.status==2}">

													<c:if test="${reviewInfos.product_type eq '6'}">
														<a href="${ctx}/visa/xxz/borrowmoney/visaBorrowMoney4XXZFeePrintNew?reviewId=${reviewInfos.id}&orderType=${reviewInfos.product_type}" target="_blank">打印</a><br/>
													</c:if>
													<c:if test="${reviewInfos.product_type ne '6'}">
														<a href="${ctx}/newOrderReview/manage/showBorrowMoneyForm?reviewId=${reviewInfos.id}&orderType=${reviewInfos.product_type}" target="_blank">打印</a><br/>
													</c:if>

											</c:if>
	                                    </p>
	                                </dd>
	                            </dl>
                            </td>
                        </tr>
                   </c:forEach>
                </tbody>
                </table>
				<!-- 分页部分 -->
		<div class="page">
			<div class="pagination">
				<c:if test="${reviewVO.reviewStatus==1 }">
					<div class="pagination">
			            <dl>
			              <dt>
			                <input name="allChk" onclick="t_checkall(this)" type="checkbox">
			                全选</dt>
			              <dt>
			                <input name="reverseChk" onclick="t_checkallNo(this)" type="checkbox">
			                反选</dt>
			              <dd>
			              	 <input class="btn ydbz_x" type="button" value="批量审批" target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:payedConfirmNew('${ctx}',1,1,'${reviewVO.userLevel}','/newOrderReview/manage/batchReviewBorrowing');">
			              </dd>
			            </dl>
			   		 </div>
			   	 </c:if>
			</div>
			<div class="pagination clearFix">${page}</div>
		</div>
				<!--右侧内容部分结束-->		
		<!--footer-->
		<!--footer***end-->
</body>
</html>
