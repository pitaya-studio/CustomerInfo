<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>退团审核列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<!-- 静态资源 -->

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<!-- 20150810 批量审核js -->
<script src="${ctxStatic}/review/borrowing/airticket/newBatchReview.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
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
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
			
		//团号和产品切换
		switchNumAndPro();
		
		//产品销售和下单人切换
		switchSalerAndPicker();
		$('#order-saler').comboboxInquiry();
		$('#order-picker').comboboxInquiry();
		$('#channelrefund').comboboxInquiry();
		$('#meterrefund').comboboxInquiry();
		
		$(".bgMainRight").delegate("select[class='selectType']","change",function(){
		   $("#searchForm").submit();
		 })
		var _$orderBy = $("#orderBy").val();
			if(_$orderBy==""){
			    _$orderBy="id DESC";
			}
			var orderBy = _$orderBy.split(" ");
			
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
	});
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
	
function sortby(sortBy,obj) {
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)) {
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)) {
            sortBy = $.trim(sortBy.replace(/ASC/gi,"")) + " DESC";
        } else {
            sortBy = $.trim(sortBy.replace(/DESC/gi,"")) + " ASC";
        }
    } else {
        sortBy = sortBy+" DESC";
    }
    
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}

//查看审核信息
function viewReviewInfo(roleId,orderId,rid){

	window.open("${ctx}/activityOrder/lendmoney/airticketLendMoneyByReviewId?orderId=" + orderId + "&reviewId=" + rid);
}
//审核明细
function reviewBorrowing(roleId,orderId,rid){

	var rrid =rid;
	window.open("${ctx}/activityOrder/lendmoney/reviewPlaneBorrowingInfo?orderId=" + orderId + "&rid="+rrid);
}

//同一审核流程切换审核角色查询
function chooseRole(rid){
	$("#rid").val(rid);
	$("#searchForm").submit();
}


//审核撤销
function backOutReview(rid,userLevel){
	
	$.jBox.confirm("确定要撤销审核吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				async: false, 
				url : "${ctx}/activityOrder/lendmoney/backBorrowAmount",
				data : {
					rid: rid
				},
				success : function(msg) {
					if(msg.result == "1"){
					jBox.tip("撤销成功！");
						$("#searchForm").submit();
					}else{
						jBox.tip(msg.message);
					}
				}
			});
		}
	});
		
}

//全选
function checkall(obj){
    if($(obj).attr("checked")){
        $('#contentTable input[type="checkbox"]').attr("checked",'true');
        $("input[name='allChk']").attr("checked",'true');
    }else{
        $('#contentTable input[type="checkbox"]').removeAttr("checked");
        $("input[name='allChk']").removeAttr("checked");
    }
}

//反选
function checkreverse(obj){
    var $contentTable = $('#contentTable');
    $contentTable.find('input[type="checkbox"]').each(function(){
        var $checkbox = $(this);
        if($checkbox.is(':checked')){
            $checkbox.removeAttr('checked');
        }else{
            $checkbox.attr('checked',true);
        }
    });
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
</style>

</head>

<body>

	<div id="sea">
	<!--右侧内容部分开始-->	
					
	     	<form id="searchForm" action="${ctx}/newOrderReview/manage/getBorrowingList"  method="post">
					<div class="message_box">
						<c:forEach items="${userJobs}" var="role">
							<div class="message_box_li">
								<c:choose>
									<c:when test="${reviewVO.rid==role.id}">
							   		<a  onClick="chooseRole('${role.id}');">
							   		<div class="message_box_li_a curret">
										<span>
										${fns:abbrs(role.deptName,role.jobName,40)}
										</span>
										<c:if test ="${role.count gt 0}">
											<div class="message_box_li_em" >
											${role.count}								
											</div>
										</c:if>
										<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
											<div class="message_box_li_hover">
												${role.deptName }_${role.jobName }
											</div>
										</c:if>
									</div>
							   		</a>
							   		</c:when>
								<c:otherwise>
							   		<a onClick="chooseRole('${role.id}');">
								   		<div class="message_box_li_a">
											<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
											<c:if test ="${role.count gt 0}">
												<div class="message_box_li_em" >
												${role.count}					
												</div>
											</c:if>
											<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
												<div class="message_box_li_hover">
													${role.deptName }_${role.jobName }
												</div>
											</c:if>
										</div>
							   		</a>
							   	</c:otherwise>
								</c:choose>
								
							</div>
						</c:forEach>	
					</div>
				    <input id="rid"name="rid" type="hidden" value="${reviewVO.rid}" />
					<input id="userLevel"name="userLevel" type="hidden" value="${reviewVO.userLevel}" />
					<input id="reviewStatus"name="reviewStatus" type="hidden" value="${reviewVO.reviewStatus}" />
					<input id="pageNo" name="pageNo" type="hidden"
						value="${page.pageNo}" /> <input id="pageSize" name="pageSize"
						type="hidden" value="${page.pageSize}" /> <input id="orderBy"
						name="orderBy" type="hidden" value="${page.orderBy}" />
						 <input id="revid" name="revid" type="hidden" value="0" />
					<!-- 排序字段 默认为1 -->
					<input id="statusChoose" name="statusChoose" type="hidden"
						value="0" />
					<input id="orderType" name="orderType" type="hidden"
						value="${reviewVO.orderType}" />
					<!-- 流程类型 退款 退票 -->
					<input name="active" type="hidden" value="1" />
                	<div class="activitylist_bodyer_right_team_co">
                    	<div class="activitylist_bodyer_right_team_co2 pr wpr20">
                        	<label>搜索：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="" type="text" />
                       		<span style="display: block;" class="ipt-tips">团号/产品名称/订单号</span>
                        </div>
                      	<div class="form_submit">
								<input class="btn btn-primary ydbz_x" value="搜索" type="submit" />
								<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
						</div>
                        <div class="zksx">筛选</div>
                        <div class="ydxbd text-more-new">
                            <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">产品类型：</div>
                            <select name="productType">
	                            <c:forEach var="order" items="${fns:getDictList('order_type')}">
									<option value="${order.value }"
										<c:if test="${conditionsMap.productType==order.value}">selected="selected"</c:if>>${order.label}
									</option>
								</c:forEach>
                            </select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">渠道商：</div>
                            <select name="channel" id="">
								<option value="" selected="selected">不限</option>
								<c:if test="${not empty fns:getAgentList() }">
									<c:forEach items="${fns:getAgentList()}" var="agentinfo">
										<option value="${agentinfo.id }"
											<c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>
											${agentinfo.agentName}</option>
									</c:forEach>
								</c:if>
							</select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co2">
                                <label>申请日期：</label>
                                <input id="" class="inputTxt dateinput"
								name="applyDateFrom" value="${conditionsMap.applyDateFrom}" onclick="WdatePicker()" readonly="readonly" /> <span>
								至 </span> <input id="" class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}"
								onclick="WdatePicker()" readonly="readonly" />
                            </div>
                            <div class="kong"></div>
                            <div class="activitylist_bodyer_right_team_co3">
                                <div class="activitylist_team_co3_text">审批发起人：</div>
	                            <select name="applyPerson" id="salerrefund">
									<option value="" selected="selected">不限</option>
									<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
										<!-- 用户类型  1 代表销售 -->
										<option value="${userinfo.id }"
											<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name
											}</option>
									</c:forEach>
								</select> 
                            </div>
                             <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">销售：</div>
                                <select name="saler" id="salerrefund">
									<option value="" selected="selected">不限</option>
									<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
										<!-- 用户类型  1 代表销售 -->
										<option value="${userinfo.id }"
											<c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name
											}</option>
									</c:forEach>
								</select> 
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">审批状态：</div>
                            <select name="selectReviewStatus" id="">
								<option value="" selected="selected">全部</option>
								<option value="1"
									<c:if test="${conditionsMap.reviewStatus == 1 }">selected="selected"</c:if>>审批中</option>
								<option value="2"
									<c:if test="${conditionsMap.reviewStatus == 2 }">selected="selected"</c:if>>已通过</option>
								<option value="0"
								<c:if test="${conditionsMap.reviewStatus == 0 }">selected="selected"</c:if>>未通过</option>
							</select>
                            </div>
                           
                            <div class="kong"></div>
                            
                           <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">出纳确认：</div>
	                            <select name="cashConfirm" id="">
							 		<option value="" selected="selected">全部</option>
									<option value="0"
										<c:if test="${conditionsMap.cashConfirm == 0 }">selected="selected"</c:if>>已付</option>
									<option value="1"
										<c:if test="${conditionsMap.cashConfirm == 1 }">selected="selected"</c:if>>未付</option>
							 	</select>
                            </div>
                             <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">打印状态：</div>
	                            <select name="printFlag" id="">
									<option value="" selected="selected">全部</option>
									<option value="0"
										<c:if test="${conditionsMap.printFlag == 0 }">selected="selected"</c:if>>已打印</option>
									<option value="1"
										<c:if test="${conditionsMap.printFlag == 1 }">selected="selected"</c:if>>未打印</option>
								</select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co2">
                                <label>借款金额：</label>
	                                <input id=""  name="lendMoneyFrom" value="${conditionsMap.lendMoneyFrom}" /> 
	                                <span> 至 </span> 
	                                <input id=""  name="lendMoneyTo"   value="${conditionsMap.lendMoneyTo}"   />
                            </div>
                           
                        </div>
                        <div class="kong"></div>
                    </div>
                </form>
					<div class="activitylist_bodyer_right_team_co_paixu">
						<div class="activitylist_paixu_left">
				            <ul>
					            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
					            <li class="activitylist_paixu_left_biankuang liaa.createDate"><a onClick="sortby('r.create_date',this)">创建时间</a></li>
					            <li class="activitylist_paixu_left_biankuang liaa.updateDate"><a onClick="sortby('r.create_date',this)">更新时间</a></li>
				            </ul>
						</div>
						<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
					</div>
					</div>
					<!--状态开始-->
<!-- 					<div class="supplierLine"><c:if test=""></c:if> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==0 }">class="select"</c:if> onClick="statusChoose(0);">全部</a>  -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==1 }">class="select"</c:if> onClick="statusChoose(1);">待本人审核</a> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==4 }">class="select"</c:if> onClick="statusChoose(4);">审核中</a> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==2 }">class="select"</c:if> onClick="statusChoose(2);">未通过</a> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==3 }">class="select"</c:if> onClick="statusChoose(3);">已通过</a> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==5 }">class="select"</c:if> onClick="statusChoose(5);">已取消</a> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==6 }">class="select"</c:if> onClick="statusChoose(5);">本人审批通过</a> -->
<!-- 						<a <c:if test="${reviewVO.reviewStatus==7 }">class="select"</c:if> onClick="statusChoose(5);">非本人审批</a> -->
<!-- 						<div class="activitylist_paixu_right"> -->
<!-- 								查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条 -->
<!-- 							</div> -->
<!-- 							<div class="kong"></div> -->
<!-- 					</div> -->
					
					
				<div class="supplierLine">
                    <a <c:if test="${reviewVO.reviewStatus==0 }">class="select"</c:if> onClick="statusChoose(0);">全部</a>
                    <a <c:if test="${reviewVO.reviewStatus==1 }">class="select"</c:if> onClick="statusChoose(1);">待本人审核</a>
                    <a id="todo" href="javascript:void(0)">本人已审核</a>
                    <a <c:if test="${reviewVO.reviewStatus==7 }">class="select"</c:if> onClick="statusChoose(5);">非本人审批</a>
                </div>
					
					<!--状态结束-->
					
					 <!--状态结束-->
                <table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="8%">订单号</th>
                            <th width="8%"> <span class="tuanhao on">团号</span> / <span class="chanpin">产品名称</span></th>
                            <th width="5%">产品类型</th>
                            <th width="5%">申请时间</th>
                            <th width="7%">审批发起人</th>
                            <th width="6%">渠道商</th>
                            <th width="6%">销售</th>
                            <th width="6%"  class="tr">订单金额</th>
                            <th width="6%"  class="tr">已付金额<br />到账金额</th>
                            <th width="6%">款项名称</th>
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
                 	<%
						int n = 1;
					%>
					<c:forEach items="${page.list}" var="reviewInfos">
                        <tr>
                         	<td>
								<!-- 20150804 add 多选框 -->
								<c:if test="${reviewVO.reviewStatus==1 }">
								 	<input type="checkbox" value="${reviewInfos.id}" name="activityId" >
								</c:if>
								<%=n++%>
							</td>
                            <td>
                            	${reviewInfos.orderno}
                            </td>
                            <td>
                            	<div title="4535DFDSF" class="tuanhao_cen onshow">
                            		 ${reviewInfos.groupcode}
                            	</div>
                                <div title="${reviewInfos.acitivityName}" class="chanpin_cen qtip"> 
                               		 <a href="${ctx}/airTicket/actityAirTickettail/${reviewInfos.productId}" target="_blank">${reviewInfos.acitivityName}</a>
                                </div>
                            </td>
                            <td class="tc">
                            	<c:if test="${reviewInfos.producttype eq '7'}">
                            		机票
                            	</c:if>
                            	<c:if test="${reviewInfos.producttype eq '1'}">
                            		单团
                            	</c:if>
                            	<c:if test="${reviewInfos.producttype eq '2'}">
                            		散拼
                            	</c:if>
                            	<c:if test="${reviewInfos.producttype eq '3'}">
                            		游学
                            	</c:if>
                            	<c:if test="${reviewInfos.producttype eq '4'}">
                            		大客户
                            	</c:if>
                            	<c:if test="${reviewInfos.producttype eq '5'}">
                            		自由行
                            	</c:if>
                            	<c:if test="${reviewInfos.producttype eq '10'}">
                            		游轮
                            	</c:if>
                            </td>
                            <td class="p0">
                            	<div class="out-date">
                            		<fmt:formatDate value="${reviewInfos.createdate}" pattern="yyyy-MM-dd"/>
                            	</div>
                                <div class="close-date time">
                                	<fmt:formatDate value="${reviewInfos.createdate}" pattern="HH:mm:ss"/>
                                </div>
                            </td>
                            <td class="tc">${fns:getUserNameById(reviewInfos.createby)}</td>
                            <td class="tc">${reviewInfos.agentName}</td>
                            <td class="tc">${fns:getUserNameById(reviewInfos.saler)}	</td>
                            <td class="tr">
                            	<span class="fbold tdred">${reviewInfos.payPrice}</span>起
                            </td>
                            <td class="tr">
					        	<div class="yfje_dd">
									<span class="fbold">${fns:getOrderMoney(refundReviewInfo.producttype, refundReviewInfo.orderid, '2')}</span>
								</div>
								<div class="dzje_dd">
									<span class="fbold">${fns:getOrderMoney(refundReviewInfo.producttype, refundReviewInfo.orderid, '3')}</span>
								</div>
			        		</td>
                            <td class="tr">
                            	款项名称
                            </td>
                            <td class="tr"><span class="fbold tdorange">${reviewInfos.payPrice}</span></td>
                            <td class="tc">
                         	   ${fns:getUserNameById(reviewInfos.last_reviewer)}
                            </td>
                            <td class="invoice_yes tc">
								<c:if test="${reviewInfos.status eq '0'}">
											已驳回
										</c:if>
										<c:if test="${reviewInfos.status eq '2'}">
											已通过
										</c:if>
										<c:if test="${reviewInfos.status eq '1'}">
											审核中
								</c:if>
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
	                                <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png" /></dt>
	                                <dd class="">
                               		  <p>
	                                        <span></span>
											<a href="javascript:viewReviewInfo('','${reviewInfos.orderid}','${reviewInfos.reviewid}')">查看</a><br/>
											<a href="javascript:reviewBorrowing('','${reviewInfos.orderid}','${reviewInfos.reviewid}')">审批</a>
											<!-- 审核撤销 -->
											<c:if test="${refundReviewInfo.isBackReview == true}">
												<a href="javascript:backOutReview('${reviewInfos.reviewid}')">撤销</a>
											</c:if>      
											<c:if test="${reviewInfos.status==2}">
												<shiro:hasPermission name="review:print:down">         
                                                      <a href="${ctx}/printForm/borrowMoneyForm?reviewId=${reviewInfos.reviewid}&orderType=${reviewVO.orderType}" target="_blank">打印</a><br/>
                                                   </shiro:hasPermission>
											</c:if>
	                                    </p>
	                                </dd>
	                            </dl>
                            </td>
                        </tr>
                   </c:forEach>
                </tbody>
                </table>
					
			    <div class="pagination">
                    <dl>
                        <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                        <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
                        <dd>
                            <a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a>
                        </dd>
                    </dl>
                </div>
					<!-- 分页部分 -->
					<div class="pagination clearFix">${page}</div>
					<!--右侧内容部分结束-->		
		<!--footer-->
		<!--footer***end-->
</body>
</html>
