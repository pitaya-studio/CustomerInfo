<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-退款</title>
<!-- 财务-财务 去掉了这几个字 因为审核公用这个页面 -->
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
//table中“销售“、”下单人“切换
function switchSalerAndCreate(){
	//点击团号
	$(".activitylist_bodyer_table").delegate(".creator","click",function(){
        $(this).addClass("on").siblings().removeClass('on');
        $('.salercls_cen').removeClass('onshow');
        $('.creator_cen').addClass('onshow');
    });
    //点击产品
    $(".activitylist_bodyer_table").delegate(".salercls","click",function(){
         $(this).addClass("on").siblings().removeClass('on');
         $('.creator_cen').removeClass('onshow');
         $('.salercls_cen').addClass('onshow');
    });
}
	$(function() {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//取消一个checkbox 就要联动取消 全选的选中状态
        $("input[name='checkboxrevid']").click(function(){
            if($(this).attr("checked")){

            }else{
                $("input[name='allChk']").removeAttr("checked");
            }
        });
		//团号和产品切换
		switchNumAndPro();
		switchSalerAndCreate();
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		$("#channelrefund" ).comboboxInquiry();
		$("#salerrefund" ).comboboxInquiry();
		$("#truesalerrefund" ).comboboxInquiry();
		$("#meterrefund" ).comboboxInquiry();
		
		showSearchPanel();
	});
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
						url : "${ctx}/refundReview/batchrefundReview",
						data:{ 
							"revIds":str,
							"remark" : remark,
							"result" : v
						},
						success : function(msg) {
							$("#searchForm").submit();
						}
		            })
	            }
	        }, height: 250, width: 350
	    });
	    inquiryCheckBOX();
	}
	//全选&反选操作
function checkall(obj){
    if($(obj).attr("checked")){
        $('#contentTable input[type="checkbox"]').attr("checked",'true');
        $("input[name='allChk']").attr("checked",'true');
    }else{
        $('#contentTable input[type="checkbox"]').removeAttr("checked");
        $("input[name='allChk']").removeAttr("checked");
    }
}
function checkreverse(obj){
    var $contentTable = $('#contentTable');
    $contentTable.find('input[type="checkbox"]').each(function(){
        var $checkbox = $(this);
        if($checkbox.is(':checked')){
            $checkbox.removeAttr('checked');
        }else{
            $checkbox.attr('checked',true);
        }
		 $checkbox.trigger('change');
    });
}
	// 	有查询条件的时候，DIV不隐藏
	function showSearchPanel(){
		
		var startTime = $("input[name='startTime']").val();
		var endTime = $("input[name='endTime']").val();
		
		var channel = $("select[name='channel'] option:selected").val();
		var saler = $("select[name='saler'] option:selected").val();
		var meter = $("select[name='meter'] option:selected").val();
		if(isNotEmpty(startTime)
				||isNotEmpty(endTime) || (isNotEmpty(channel)&&channel!=-99999)
				||(isNotEmpty(saler)&&saler!=-99999)|| (isNotEmpty(meter)&&meter!=-99999)){
			$('.zksx').click();
		}
	}
	
	// 判定不为空值
	function isNotEmpty(str){
		if(str != ""){
			return true;
		}
		return false;
	}
	
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};

	//排序
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
	function statusChooses(statusNum) {
		$("#statusChoose").val(statusNum);
		$("#searchForm").submit();
	};
	function cashConfirm(obj,rid) {
		$.ajax({
		        type: "POST",
		        url: "${ctx}/refundReview/cashConfirm",
		        data: {
		        	rId : rid
		        },
		        success: function(msg){
			            location.reload();
		        }
		     });
	};
	function printRefund(obj, flowType, productId, productType, agentId, orderId, reviewId, travelerId, groupCode, refundPrice){
// 	    printCallback(obj);
		$("input[name='flowType']").val(flowType);
		$("input[name='productId']").val(productId);
		$("input[name='productType']").val(productType);
		$("input[name='agentId']").val(agentId);
		$("input[name='orderId']").val(orderId);
		$("input[name='reviewId']").val(reviewId);
		$("input[name='travelerId']").val(travelerId);
		$("input[name='groupCodePrint']").val(groupCode);
		$("input[name='refundPrice']").val(refundPrice);
		$("#refundRevPrintRefundForm").submit();
	};
	function payRefund(orderType, orderId, travelerId, currencyId, payPrice, reviewId){
		$.ajax({
		        type: "POST",
		        url: "${ctx}/refundReview/refundPay",
		        data: {
		        	orderType : orderType,
		        	orderId : orderId,
		        	travelerId : travelerId,
		        	currencyId : currencyId,
		        	payPrice : payPrice,
		        	reviewId : reviewId
		        },
		        success: function(msg){
// 			            location.reload();
		        }
		     });
	}
function cancelDeposite(review_id, my_check_level){
	if(review_id == null || review_id == ""){
		$.jBox.tip("审核ID不能为空", 'error');
		return false;
	}
	if(my_check_level == null || my_check_level == ""){
		$.jBox.tip("您所在的审核层级不能为空", 'error');
		return false;
	}
	$.jBox.confirm("确定要撤销此审核吗？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:"${ctx}/deposite/cancelDeposite",
				data:{reviewId:review_id,myCheckLevel:my_check_level},
				success:function(data){
					if('success' == data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip(data.msg, 'error');
					}
				}
			})
		}
	});
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
	.creator,.salercls{cursor:pointer; color:#0c85d8;}
	.creator.on,.salercls.on{ color:#717171; cursor:default;}
	.creator_cen,.salercls_cen{display:none;}
	.onshow{ display:block;}
	#batch-verify-list{ width:400px !important; height:300px !important; display:none;}
</style>
</head>
<body>
       <page:applyDecorator name="refund_review_head">
             <page:param name="current"><c:choose><c:when test="${conditionsMap.headPrd==1}">single</c:when><c:when test="${conditionsMap.headPrd==2}">loose</c:when><c:when test="${conditionsMap.headPrd==3}">study</c:when><c:when test="${conditionsMap.headPrd==5}">free</c:when><c:when test="${conditionsMap.headPrd==4}">bigCustomer</c:when><c:when test="${conditionsMap.headPrd==6}">visa</c:when><c:when test="${conditionsMap.headPrd==7}">airticket</c:when><c:when test="${conditionsMap.headPrd==11}">hotel</c:when><c:when test="${conditionsMap.headPrd==12}">island</c:when><c:when test="${conditionsMap.headPrd==10}">boat</c:when></c:choose></page:param>
       </page:applyDecorator>  
	<!--右侧内容部分开始-->
  	<div class="message_box">
   		<c:forEach items="${userJobs}" var="userJob">
   			<div class="message_box_li">
	   			<c:if test="${conditionsMap.userJobId == userJob.id}">
	   				<div class="message_box_li_a curret ydbz_x">
						<a href="${ctx}/refundReview/refundReviewList?headPrd=${conditionsMap.headPrd}&userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>
						<c:if test ="${userJob.count gt 0}">	
							<div class="message_box_li_em" >${userJob.count}</div>
						</c:if>
						<c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">
							<div class="message_box_li_hover">
								${userJob.deptName }_${userJob.jobName }
							</div>
						</c:if>
						</a>
					</div>
				</c:if> 
				<c:if test="${conditionsMap.userJobId != userJob.id}">
					<div class="message_box_li_a ydbz_x gray">
						<a style="none" href="${ctx}/refundReview/refundReviewList?headPrd=${conditionsMap.headPrd}&userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>
						<c:if test ="${userJob.count gt 0}">
							<div class="message_box_li_em" >${userJob.count}</div>
						</c:if>
						<c:if test="${fns:getStringLength(userJob.deptName,userJob.jobName) gt 37}">
							<div class="message_box_li_hover">
								${userJob.deptName }_${userJob.jobName }
							</div>
						</c:if>
						</a>
					</div>
				</c:if> 
			</div>
		</c:forEach>
    </div>
<!--     <div class="rolelist_btn"> -->
<!--    		<c:forEach items="${userJobs}" var="userJob"> -->
<!--    			<c:if test="${conditionsMap.userJobId == userJob.id}"> -->
<!-- 				<a class="ydbz_x" href="${ctx}/refundReview/refundReviewList?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a> -->
<!-- 			</c:if>  -->
<!-- 			<c:if test="${conditionsMap.userJobId != userJob.id}"> -->
<!-- 				<a class="ydbz_x gray" href="${ctx}/refundReview/refundReviewList?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a> -->
<!-- 			</c:if>  -->
<!-- 		</c:forEach> -->
<!--     </div> -->
	<form id="searchForm" action="${ctx}/refundReview/refundReviewList?headPrd=${conditionsMap.headPrd}"
		method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" /> <input id="orderBy" name="orderBy"
			type="hidden" value="${page.orderBy}" />
			<input type="hidden" value = "${conditionsMap.userJobId}" name="userJobId"/><!-- 职位id -->
			<input type="hidden" value = "${conditionsMap.headPrd}" name="headPrd"/>
			<input type="hidden" value = 1 name="flowType"/><!-- 流程类型 1 退款 -->
		<!-- <input id="revid" name="revid" type="hidden" value="0" /> -->
		<!-- 排序字段 默认为1 -->
		<input id="statusChoose" name="statusChoose" type="hidden" 	value="${conditionsMap.statusChoose}" />
		<!-- 状态过滤  默认为全部 -->
		<input name="active" type="hidden" value="1" />
		<!-- 有效标志 用于审批表记录查询 默认为1 -->
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label style="width:100px">团号/订单编号：</label><input style="width:260px"
					class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey"
					name="groupCode" value="${conditionsMap.groupCode}" type="text" />
			</div>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit" />
			</div>
			<a class="zksx">展开筛选</a>
			<div class="ydxbd">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">渠道选择：</div>
					<select name="channel" id="channelrefund">
						<option value="" >全部</option>
						<c:if test="${not empty fns:getAgentListOrderByFirstLetter() }">
							<c:forEach items="${fns:getAgentListOrderByFirstLetter()}" var="agentinfo">
								<c:choose>
									<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and agentinfo.agentName eq '非签约渠道' }">
										<option value="${agentinfo.id }" <c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>未签</option>
									</c:when>
									<c:otherwise><option value="${agentinfo.id }" <c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option></c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
					</select>
				</div>
				
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">报批日期：</div><input id="" class="inputTxt dateinput"
						name="startTime" value="${conditionsMap.startTime}"
						onfocus="WdatePicker()" readonly="" /> <span> 至 </span> <input
						id="" class="inputTxt dateinput" name="endTime"
						value="${conditionsMap.endTime}" onclick="WdatePicker()"
						readonly="" />
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">下单人：</div>
					<select class="sel-w1" style='height : 28px' name="saler" id="salerrefund">
						<option value="-99999" selected="selected">不限</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }"
								<c:if test="${conditionsMap.saler==userinfo.id }">selected="selected"</c:if>>${userinfo.name
								}</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">计调：</div>
					<select name='meter' id = "meterrefund">
						<option value="-99999" selected="selected">不限</option>
						<c:forEach items="${fns:getSaleUserList('3')}" var="userinfo">
							<!-- 用户类型  3 代表计调 -->
							<option value="${userinfo.id }"
								<c:if test="${conditionsMap.meter==userinfo.id }">selected="selected"</c:if>>${userinfo.name
								}</option>
						</c:forEach>
					</select>
				</div>
				<c:if
					test="${conditionsMap.headPrd != 11 && conditionsMap.headPrd != 12}">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">销售：</div>
						<select class="sel-w1" style='height : 28px' name="truesaler"
							id="truesalerrefund">
							<option value="-99999" selected="selected">不限</option>
							<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
								<!-- 用户类型  1 代表销售 -->
								<option value="${userinfo.id }"
									<c:if test="${conditionsMap.truesaler==userinfo.id }">selected="selected"</c:if>>${userinfo.name
									}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
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
			<div class="activitylist_paixu_right">
				查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
			</div>
			<div class="kong"></div>
		</div>
	</div>
	<!--状态开始-->
	<div class="supplierLine">
		<c:if test="${conditionsMap.statusChoose == null || conditionsMap.statusChoose == ''}">
			<a class="select" onClick="statusChooses('');">全部</a> 
			<a  onClick="statusChooses(1);">待本人审核</a> 
			<a  onClick="statusChooses(5);">审核中</a> 
			<a onClick="statusChooses(0);">未通过</a>
			<a onClick="statusChooses(2);">已通过</a>
			<a onClick="statusChooses(4);">已取消</a>
		</c:if>
		<c:if test="${conditionsMap.statusChoose == '1'}">
			<a onClick="statusChooses('');">全部</a> 
			<a  class="select" onClick="statusChooses(1);">待本人审核</a> 
			<a  onClick="statusChooses(5);">审核中</a> 
			<a onClick="statusChooses(0);">未通过</a>
			<a onClick="statusChooses(2);">已通过</a>
			<a onClick="statusChooses(4);">已取消</a>
		</c:if>
		<c:if test="${conditionsMap.statusChoose == '5'}">
			<a onClick="statusChooses('');">全部</a> 
			<a onClick="statusChooses(1);">待本人审核</a> 
			<a  class="select" onClick="statusChooses(5);">审核中</a> 
			<a onClick="statusChooses(0);">未通过</a>
			<a onClick="statusChooses(2);">已通过</a>
			<a onClick="statusChooses(4);">已取消</a>
		</c:if>
		<c:if test="${conditionsMap.statusChoose == '0'}">
			<a onClick="statusChooses('');">全部</a>
			<a onClick="statusChooses(1);">待本人审核</a> 
			<a  onClick="statusChooses(5);">审核中</a> 
			<a class="select" onClick="statusChooses(0);">未通过</a>
			<a onClick="statusChooses(2);">已通过</a>
			<a onClick="statusChooses(4);">已取消</a>
		</c:if>
		<c:if test="${conditionsMap.statusChoose == '2'}">
			<a onClick="statusChooses('');">全部</a> 
			<a onClick="statusChooses(1);">待本人审核</a> 
			<a  onClick="statusChooses(5);">审核中</a> 
			<a onClick="statusChooses(0);">未通过</a>
			<a class="select" onClick="statusChooses(2);">已通过</a>
			<a onClick="statusChooses(4);">已取消</a>
		</c:if>
		<c:if test="${conditionsMap.statusChoose == '4'}">
			<a onClick="statusChooses('');">全部</a> 
			<a onClick="statusChooses(1);">待本人审核</a> 
			<a  onClick="statusChooses(5);">审核中</a> 
			<a onClick="statusChooses(0);">未通过</a>
			<a onClick="statusChooses(2);">已通过</a>
			<a class="select" onClick="statusChooses(4);">已取消</a>
		</c:if>
	</div>
	<!--状态结束-->
	</form>
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="4%">序号</th>
				<th width="7%">订单编号</th>
				<th width="9%">报批日期</th>
				<!-- <th width="10%">付款账号</th> -->
				<th width="5%">计调</th>
				<th width="6%">团队类型</th>
				<th width="6%"><span class="tuanhao on">团号</span>/<span
					class="chanpin">产品编号</span>
				</th>
				<th width="9%">批发商</th>
				<th width="5%">
					<c:if test = "${conditionsMap.headPrd != 11 && conditionsMap.headPrd != 12}">
						<span class="creator on">销售</span>/<span class="salercls">下单人</span>
					</c:if>
					<c:if test = "${conditionsMap.headPrd == 11 || conditionsMap.headPrd == 12}">
						<span>下单人</span>
					</c:if>
					</th>
				<th width="6%">款项</th>
				<th width="4%">退款金额</th>
				<th width="5%">订单总额</th>
				<th width="6%">已收金额<br>到账金额</th>
				<th width="4%">上一环节<br />审批人</th>
				<th width="4%">审核状态</th>
				<th width="6%">打印确认</th>
<!-- 				<th width="6%">出纳确认</th> -->
				<c:if test="${isConfirm eq '1' }">
					<th width="5%">付款状态</th>
				</c:if>
				<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<!-- 遍历显示结果 start -->
			<%
				int n = 1;
			%>
			<c:forEach items="${page.list}" var="refundReviewInfo">
				<tr>
					<td><c:if test="${conditionsMap.statusChoose == '1'}"><input type="checkbox" name="checkboxrevid" value="${refundReviewInfo.revid}" id="checkbox<%=n%>" /></c:if> <%=n++%></td>
					<td>${refundReviewInfo.orderno}</td>
					<td class="tc"><fmt:formatDate value="${refundReviewInfo.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<!--<td>${refundReviewInfo.bankAccount}</td> -->
					<td>${fns:getUserNameById(refundReviewInfo.jidcreateby)}</td>
					<c:if test="${refundReviewInfo.prdtype == 8}">
						<td>机票切位</td>
					</c:if>
					<c:if test="${refundReviewInfo.prdtype == 9}">
						<td>散拼切位</td>
					</c:if>
					<c:if test="${refundReviewInfo.prdtype != 8 && refundReviewInfo.prdtype != 9}">
					<td>${fns:getDictLabel(refundReviewInfo.prdtype, 'order_type',
						refundReviewInfo.prdtype)}</td>
					</c:if>
					<td>
						<div class="tuanhao_cen onshow">${refundReviewInfo.groupcode}</div>
						<div class="chanpin_cen qtip" title="JHGJLXS5674--切位">
							<c:if test="${refundReviewInfo.prdtype ==7}"><!--  -->
								<a href="${ctx}/airTicket/actityAirTickettail/${refundReviewInfo.chanpid}" target="_blank">${refundReviewInfo.chanpname}</a>
							</c:if>
							<c:if test="${refundReviewInfo.prdtype !=7 && refundReviewInfo.prdtype !=6}">
								<a href="${ctx}/activity/manager/detail/${refundReviewInfo.chanpid}" target="_blank">${refundReviewInfo.chanpname}</a>
							</c:if>
							<c:if test="${refundReviewInfo.prdtype ==6}">
								<a href="${ctx}/visa/preorder/visaProductsDetail/${refundReviewInfo.chanpid}" target="_blank">${refundReviewInfo.chanpname}</a>
							</c:if>
						</div></td>
					<td>${fns:getOfficeById(refundReviewInfo.supplycom)}</td>
					<td>
						<c:if test = "${conditionsMap.headPrd != 11 && conditionsMap.headPrd != 12}">
							<div class="creator_cen onshow" title="JHGJLXS5674--切位">${refundReviewInfo.salerName}</div>
							<div class="salercls_cen qtip">${fns:getUserNameById(refundReviewInfo.salecreateby)}</div>
						</c:if>
						<c:if test = "${conditionsMap.headPrd == 11 || conditionsMap.headPrd == 12}">
							${fns:getUserNameById(refundReviewInfo.salecreateby)}
						</c:if>
					</td>
					<td>${refundReviewInfo.refundName}</td>
					<td class="tr"><span class="fbold tdorange">${refundReviewInfo.currencyMark}${refundReviewInfo.refundPrice}</span>
					</td>
					<td class="tr"><span class="tdorange fbold">${fns:getOrderMoney(refundReviewInfo.prdtype + '', refundReviewInfo.orderid, '1')}</span></td>
					<td class="p0 tr">
						<div class="yfje_dd">
							<span class="fbold">${fns:getOrderMoney(refundReviewInfo.prdtype + '', refundReviewInfo.orderid, '2')}</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">${fns:getOrderMoney(refundReviewInfo.prdtype + '', refundReviewInfo.orderid, '3')}</span>
						</div>
					</td>
					<td>${fns:getUserNameById(refundReviewInfo.lastoperator)}</td>
					<td class="invoice_yes">${fns:getNextReview(refundReviewInfo.revid) }</td>
					<td>
						<c:if test="${refundReviewInfo.printFlag==0 || refundReviewInfo.printFlag == null}"><span >未打印</span></c:if>
						<c:if test="${refundReviewInfo.printFlag==1}"><span title="<fmt:formatDate value="${refundReviewInfo.printTime}" pattern="yyyy-MM-dd HH:mm:ss"/>">已打印</span></c:if>
					</td>
					<c:if test="${isConfirm eq '1' }">
						<td>
							<c:if test="${refundReviewInfo.payStatus == null || refundReviewInfo.payStatus==0}"><span >未付款</span></c:if>
							<c:if test="${refundReviewInfo.payStatus==1}">已付款</span></c:if>
						</td>
					</c:if>
					<td class="p0">
						<dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
							</dt>
							<dd class="">
								<p>
									<span></span> 
									<c:if test="${ refundReviewInfo.revLevel == refundReviewInfo.curlevel && refundReviewInfo.revstatus == '1'}">
<!-- 										<c:if test = "${refundReviewInfo.curlevel == refundReviewInfo.topLevel}"> -->
<!-- 											<a href="${ctx}/refundReview/refundReviewDetail?orderid=${refundReviewInfo.orderid}&revid=${refundReviewInfo.revid}&prdType=${refundReviewInfo.prdtype}&flag=0&nowlevel=${refundReviewInfo.curlevel}">审批</a><br /> -->
<!-- 										</c:if> -->
										<a href="${ctx}/refundReview/refundReviewDetail?orderid=${refundReviewInfo.orderid}&revid=${refundReviewInfo.revid}&prdType=${refundReviewInfo.prdtype}&flag=0&nowlevel=${refundReviewInfo.curlevel}" target="_blank">审批</a><br />
									</c:if>
									<c:if test="${refundReviewInfo.prdtype == 7 || refundReviewInfo.prdtype == 6}">
										<c:if test="${not empty refundReviewInfo.chanpid}"><a href="${ctx }/cost/manager/forcastList/${refundReviewInfo.chanpid}/${refundReviewInfo.prdtype}" target="_blank">预报单</a></c:if>
	                              		<c:if test="${not empty refundReviewInfo.chanpid}"><a href="${ctx }/cost/manager/settleList/${refundReviewInfo.chanpid}/${refundReviewInfo.prdtype}" target="_blank">结算单</a></c:if>
									</c:if>
									<c:if test="${(refundReviewInfo.revstatus=='2' || refundReviewInfo.revstatus=='3') && refundReviewInfo.prdtype != 8 && refundReviewInfo.prdtype != 9 && refundReviewInfo.prdtype != 11 && refundReviewInfo.prdtype != 12}"><a onClick="printRefund(this, '${refundReviewInfo.flowType}', '${refundReviewInfo.chanpid}', '${refundReviewInfo.productType}', '${refundReviewInfo.agentid}', '${refundReviewInfo.orderId}', '${refundReviewInfo.revid}', '${refundReviewInfo.travelerId}', '${refundReviewInfo.groupcode}', '${refundReviewInfo.currencyMark}${refundReviewInfo.refundPrice}');">打印</a></c:if>
									<c:if test="${refundReviewInfo.prdtype != 7 && refundReviewInfo.prdtype != 6&& refundReviewInfo.prdtype != 8&& refundReviewInfo.prdtype != 9}">
										<c:if test="${not empty refundReviewInfo.chanpid}"><a href="${ctx }/cost/manager/forcastList/${refundReviewInfo.groupid}/${refundReviewInfo.prdtype}" target="_blank">预报单</a></c:if>
	                              		<c:if test="${not empty refundReviewInfo.chanpid}"><a href="${ctx }/cost/manager/settleList/${refundReviewInfo.groupid}/${refundReviewInfo.prdtype}" target="_blank">结算单</a></c:if>
									</c:if>
										<a href="${ctx}/refundReview/refundReviewDetail?orderid=${refundReviewInfo.orderid}&revid=${refundReviewInfo.revid}&prdType=${refundReviewInfo.prdtype}&flag=1&nowlevel=${refundReviewInfo.curlevel}" target="_blank">查看</a><br />
									<c:if test="${refundReviewInfo.prdtype != 8 && refundReviewInfo.prdtype != 9}">
<!-- 										<c:if test="${(refundReviewInfo.cashconfirm == null || refundReviewInfo.cashconfirm == '0') && refundReviewInfo.payStatus == 1}"> -->
<!-- 											<a onClick="cashConfirm(this, ${refundReviewInfo.revid});">出纳确认 </a> -->
<!-- 										</c:if> -->
<!--  										审核通过的才能付款  
										<c:if test="${(refundReviewInfo.revstatus=='2' || refundReviewInfo.revstatus=='3') && userJob.jobType == 6}">
											<a href="${ctx}/refundReview/refundPay?orderType=${refundReviewInfo.prdtype}&orderId=${refundReviewInfo.orderId}&travelerId=${refundReviewInfo.travelerId}&currencyId=${refundReviewInfo.currencyId}&payPrice=${refundReviewInfo.refundPrice}&reviewId=${refundReviewInfo.revid}&agentId=${refundReviewInfo.agentid}">付款 </a>
										</c:if>-->
									</c:if>


									<c:if test="${fns:getUser().id == refundReviewInfo.lastoperator 
										&& refundReviewInfo.revstatus == 1 && refundReviewInfo.curlevel != 1}">
										<a href="#" onclick="cancelDeposite('${refundReviewInfo.revid}','${refundReviewInfo.revLevel}')">撤销</a>
									</c:if>
									<c:if test="${(refundReviewInfo.revstatus=='2' || refundReviewInfo.revstatus=='3') && (refundReviewInfo.prdtype == 11 || refundReviewInfo.prdtype == 12)}">
										<a href="${ctx }/refundReview/refundTable?reviewId=${refundReviewInfo.revid }">打印</a>
									</c:if>

								</p>
							</dd>
						</dl></td>
				</tr>
			</c:forEach>
			<!-- 遍历显示结果 end -->
		</tbody>
	</table>
	<!-- 分页部分 -->
	<c:if test="${conditionsMap.statusChoose == '1'}">
		<div class="pagination">
            <dl>
              <dt>
                <input name="allChk" onclick="checkall(this)" type="checkbox">
                全选</dt>
              <dt>
                <input name="reverseChk" onclick="checkreverse(this)" type="checkbox">
                反选</dt>
              <dd> <a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a> </dd>
            </dl>
   		 </div>
   	 </c:if>
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
	<form id="refundRevPrintRefundForm" action="${ctx}/refundReview/refundReviewInfo" method = "post" target="_blank">
		<input type="hidden" value="" name="flowType"/>
		<input type="hidden" value="" name="productId"/>
		<input type="hidden" value="" name="productType"/>
		<input type="hidden" value="" name="agentId"/>
		<input type="hidden" value="" name="orderId"/>
		<input type="hidden" value="" name="reviewId"/>
		<input type="hidden" value="" name="travelerId"/>
		<input type="hidden" value="" name="groupCodePrint"/>
		<input type="hidden" value="" name="refundPrice"/>
	</form>

</body>
</html>
