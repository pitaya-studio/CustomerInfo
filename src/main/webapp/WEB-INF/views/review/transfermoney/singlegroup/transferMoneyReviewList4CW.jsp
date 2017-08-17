<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转款-列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<!-- 静态资源 -->

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<!-- 20150810 批量审批js -->
<%-- <script src="${ctxStatic}/review/borrowing/airticket/newBatchReview.js" type="text/javascript"></script> --%>
<script type="text/javascript">
	$(function() {
		//产品名称获得焦点显示隐藏
		inputTips();
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
		});
		//如果展开部分有查询条件的话，默认展开，否则收起	
		var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='orderCdGroupCdProductNm']");
		var searchFormselect = $("#searchForm").find("select[name!='productType']");
		var inputRequest = false;
		var selectRequest = false;
		for(var i = 0; i<searchFormInput.length; i++) {
			if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
				inputRequest = true;
			}
		}
		for(var i = 0; i<searchFormselect.length; i++) {
			if($(searchFormselect[i]).children("option:selected").val() != "" && 
					$(searchFormselect[i]).children("option:selected").val() != null &&
					$(searchFormselect[i]).children("option:selected").val() != -99999) {
				selectRequest = true;
			}
		}
		if($("#productType").val() != 0){
			selectRequest = true;
		}
		if(inputRequest||selectRequest) {
			$('.zksx').click();
		}
			
		//团号和产品切换
		switchNumAndPro();
		
		//产品销售和下单人切换
		switchSalerAndPicker();
		//注册可输入下拉框组件
// 		$('#productType').comboboxInquiry();
// 		$('#reviewStatus').comboboxInquiry();
//		renderSelects(selectQuery());
		selectQuery();
		
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
	
	function selectQuery() {
		$('#agentId').comboboxInquiry();
		$('#applyPerson').comboboxInquiry();
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
		
    $('#orderCdGroupCdProductNm').val('');
    $('#productType').val('');
    $('#agentId').val('');
    $('#reviewStatus').val('');
    $('#applyPerson').val('');
    $('#transferMoneyFrom').val('');
    $('#transferMoneyTo').val('');
    $('#applyDateFrom').val('');
    $('#applyDateTo').val('');
}

function orderBySub(orderNum) {
	$("#orderBy").val(orderNum);
	$("#searchForm").submit();
};

//排序
function sort(element){
	var _this = $(element);

	//原先高亮按钮隐藏input值清空
	_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");	
	//原先排序清空
	_this.parent("li").siblings(".activitylist_paixu_moren").children("input[name*='DateSort']").val("");
	//原先高亮的同级元素置灰
	_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");

	//按钮高亮
	_this.parent("li").attr("class","activitylist_paixu_moren");
	//高亮按钮隐藏input赋值
	_this.next().val("activitylist_paixu_moren");

	var sortFlag = _this.children().attr("class");
	//升序
	if(sortFlag == "icon icon-arrow-down"){
		//改变箭头方向
		_this.children().attr("class","icon icon-arrow-up");
		//升序
		_this.prev().val("asc");
	} else {  //降序
		//改变箭头的方向
		_this.children().attr("class","icon icon-arrow-down");
		//降序
		_this.prev().val("desc");
	}

	$("#searchForm").submit();

	return false;
}

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

//查看审批信息
function viewReviewInfo(roleId,orderId,rid){

	window.open("${ctx}/singlegrouporder/transfermoney/transferMoneyDetails/" + rid);
}
//审批明细
function reviewTransferMoney(roleId,orderId,rid){
	var rrid =rid;
	window.open("${ctx}/singlegrouporder/transfermoney/toTransferMoneyReview?orderId=" + orderId + "&rid="+rrid);
}

//同一审批流程切换审批角色查询
function chooseRole(rid){
	$("#rid").val(rid);
	$("#searchForm").submit();
}


//审批撤销
function backOutReview(rid, status){
	
	$.jBox.confirm("确定要撤销审批吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				async: false, 
				url : "${ctx}/singlegrouporder/transfermoney/backReviewTransferAmount",
				data : {
					rid: rid,
					status: status
				},
				success : function(data){
					if(data.flag == "error"){
						$.jBox.tip(data.msg); 
					} else {
						$.jBox.tip("撤销成功"); 
						$("#searchForm").submit();
					}
				}
			});
		}
	});
	
}

//切换审批状态
function statusChoose(status){
	$("#statusChoose").val(status);
	$("#searchForm").submit();
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
    checkIsAll();
}

//检查是否全选
function checkIsAll(){
	var chknum = $("input[name='activityId']").size();
	var chk = 0;
	$("input[name=activityId]").each(function() {
		if ($(this).attr("checked") == "checked") {
			chk++;
		}
	});
	if (chknum == chk) {//全选 
		$("input[name='allChk']").attr("checked", true);
	} else {//不全选 
		$("input[name='allChk']").attr("checked", false);
	}
}

//批量审批
function jbox__shoukuanqueren_chexiao_fab() {
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
				  +"     <td><p>您好，当前您提交了"+num+"个审批项目，是否执行批量操作？</p></td>"
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
						url : "${ctx}/singlegrouporder/transfermoney/transferMoneyBatchReview",
						data:{ 
							"revIds":str,
							"remark" : remark,
							"result" : v
						},
						success : function(result) {
							if(result.flag == 1){
								$.jBox.tip("批量审批成功！", "success");
							}else{
								alert("批量审批结束！存在失败的审批，详情：" + result.message);
// 								$.jBox.tip("批量审批结束！存在失败的审批，详情：" + result.message, "error");
							}
							//刷新页面
							window.location.href="${ctx}/singlegrouporder/transfermoney/transferMoneyReviewList/REVIEW4CW";
						}
		            })
	            }
	        }, height: 250, width: 350
	});
}
</script>

<style type="text/css">
.rolelist_btn .ydbz_x{float:right}
 /**订单切换卡**/
.tuanhao,.chanpin{cursor:pointer; color:#0c85d8;}
.tuanhao.on,.chanpin.on{ color:#717171; cursor:default;}
.tuanhao_cen,.chanpin_cen{display:none;}
.onshow{ display:block;}
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
	     	<form id="searchForm" action="${ctx}/singlegrouporder/transfermoney/transferMoneyReviewList/REVIEW4CW"  method="post">
	     		<!-- 角色 -->
					<%--<div class="message_box">--%>
						<%--<c:forEach items="${userJobs}" var="role">--%>
							<%--<div class="message_box_li">--%>
								<%--<c:choose>--%>
									<%--<c:when test="${conditionsMap.rid==role.id}">--%>
							   		<%--<a  onClick="chooseRole('${role.id}');">--%>
							   		<%--<div class="message_box_li_a curret">--%>
										<%--<span>--%>
										<%--${fns:abbrs(role.deptName,role.jobName,40)}--%>
										<%--</span>--%>
										<%--<c:if test ="${role.count gt 0}">--%>
											<%--<div class="message_box_li_em" >--%>
											<%--${role.count}								--%>
											<%--</div>--%>
										<%--</c:if>--%>
										<%--<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">--%>
											<%--<div class="message_box_li_hover">--%>
												<%--${role.deptName }_${role.jobName }--%>
											<%--</div>--%>
										<%--</c:if>--%>
									<%--</div>--%>
							   		<%--</a>--%>
							   		<%--</c:when>--%>
								<%--<c:otherwise>--%>
							   		<%--<a onClick="chooseRole('${role.id}');">--%>
								   		<%--<div class="message_box_li_a">--%>
											<%--<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>--%>
											<%--<c:if test ="${role.count gt 0}">--%>
												<%--<div class="message_box_li_em" >--%>
												<%--${role.count}					--%>
												<%--</div>--%>
											<%--</c:if>--%>
											<%--<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">--%>
												<%--<div class="message_box_li_hover">--%>
													<%--${role.deptName }_${role.jobName }--%>
												<%--</div>--%>
											<%--</c:if>--%>
										<%--</div>--%>
							   		<%--</a>--%>
							   	<%--</c:otherwise>--%>
								<%--</c:choose>--%>
								<%----%>
							<%--</div>--%>
						<%--</c:forEach>	--%>
					<%--</div>--%>
				    <!-- 搜索 -->
				    	<!-- 隐藏input -->
					    <input id="rid"name="rid" type="hidden" value="${conditionsMap.rid}" />
						<input id="userLevel"name="userLevel" type="hidden" value="${conditionsMap.userLevel}" />
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
						<input id="revid" name="revid" type="hidden" value="${conditionsMap.rid}" />
						<!-- 排序字段 默认为1 -->
						<input id="statusChoose" name="statusChoose" type="hidden" value="${conditionsMap.statusChoose}" />
						<input id="orderType" name="orderType" type="hidden" value="${conditionsMap.orderType}" />
						<!-- 流程类型 退款 退票 -->
						<input name="active" type="hidden" value="1" />
						<!-- 搜索条件 -->
	                	<div class="activitylist_bodyer_right_team_co">
							<div class="activitylist_bodyer_right_team_co2 pr">
								<input class="txtPro inputTxt searchInput" value="${conditionsMap.orderCdGroupCdProductNm }"
									   name="orderCdGroupCdProductNm" id="orderCdGroupCdProductNm" placeholder="请输入团号/产品名称/订单号"/>
							</div>
							<a class="zksx">筛选</a>
							<div class="form_submit">
								<input class="btn btn-primary ydbz_x" value="搜索" type="submit">
								<%-- <input class="btn btn-primary " type="button" onclick="resetSearchParams()" value="条件重置"/> --%>
							</div>
							<div class="ydxbd min-screen">
								<span></span>
								<!-- 筛选条件第1行 -->
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">产品类型：</label>
									<div class="selectStyle">
										<select name="productType" id="productType">
											<!--
											<c:forEach var="order" items="${fns:getDictList('order_type')}">
												<option value="${order.value }" <c:if test="${conditionsMap.productType==order.value}">selected="selected"</c:if>>${order.label }</option>
											</c:forEach>
											-->
											<option value="" selected="selected">全部</option>
											<c:forEach var="order" items="${processTypes}">
												<option value="${order.productType }"
													<c:if test="${conditionsMap.productType==order.productType}">selected="selected"</c:if>>${order.getProductName()}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co3">
									<label class="activitylist_team_co3_text">渠道选择：</label>
									<select name="agentId" id="agentId" >
										<option value="">全部</option>

											<c:if test="${not empty fns:getAgentListAddSort()}">

												<c:forEach items="${fns:getAgentListAddSort()}" var="agentinfo">
													<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
													<c:choose>
													   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}"> 
													       <option value="${agentinfo.id }"<c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>直客</option>
													   </c:when>
													   <c:otherwise>
													       <option value="${agentinfo.id }"<c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option>
													   </c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>

									</select>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">转款金额：</label>
									<input id="transferMoneyFrom" class="inputTxt" name="transferMoneyFrom" value="${conditionsMap.transferMoneyFrom}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
									<span>至</span>
									<input id="transferMoneyTo" class="inputTxt" name="transferMoneyTo" value="${conditionsMap.transferMoneyTo}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
								</div>
								<%-- <div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
									<select name="paymentType">
										<option value="">不限</option>
										<c:forEach var="pType" items="${fns:findAllPaymentType()}">
											<option value="${pType[0] }"
												<c:if test="${conditionsMap.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
										</c:forEach>
									</select>
								</div> --%>
								<!-- 筛选条件第2行 -->
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">审批状态：</label>
									<div class="selectStyle">
										<select name="reviewStatus" id="reviewStatus">
											<option value="-99999" <c:if test="${conditionsMap.reviewStatus == -99999}">selected="selected"</c:if>>全部</option>
											<option value="1" <c:if test="${conditionsMap.reviewStatus == 1 }">selected="selected"</c:if>>审批中</option>
											<option value="2" <c:if test="${conditionsMap.reviewStatus == 2 }">selected="selected"</c:if>>审批通过</option>
											<option value="0" <c:if test="${conditionsMap.reviewStatus == 0 }">selected="selected"</c:if>>审批驳回</option>
											<option value="3" <c:if test="${conditionsMap.reviewStatus == 3 }">selected="selected"</c:if>>取消申请</option>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co3">
									<div class="activitylist_team_co3_text">审批发起人：</div>
									<select name="applyPerson" id="applyPerson" >
										<option value="-99999" selected="selected">全部</option>
										<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
											<!-- 用户类型  1 代表销售 -->
											<option value="${userinfo.id }" <c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}</option>
										</c:forEach>
									</select> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label  class="activitylist_team_co3_text">申请日期：</label>
									<input id="" class="inputTxt dateinput"
										   name="applyDateFrom" value="${conditionsMap.applyDateFrom}"
										   onclick="WdatePicker()" readonly="readonly" />
									<span> 至 </span>
									<input id="" class="inputTxt dateinput" name="applyDateTo"
										   value="${conditionsMap.applyDateTo}" onclick="WdatePicker()"
										   readonly="readonly" />
								</div>
							</div>
						</div>

					<!-- 审批标签 -->
				<div class="supplierLine">
                    <a href="javascript:void(0)" id="all" onClick="statusChoose('0')" <c:if test = "${conditionsMap.statusChoose == null || conditionsMap.statusChoose == 0 || conditionsMap.statusChoose == '0'}">class="select" </c:if>>全部</a>
                    <a id="todo" href="javascript:void(0)" onClick="statusChoose('1')" <c:if test = "${conditionsMap.statusChoose == '1' || conditionsMap.statusChoose == 1}">class="select" </c:if>> 待本人审批</a> 
                    <a id="todo" href="javascript:void(0)" onClick="statusChoose('2')" <c:if test = "${conditionsMap.statusChoose == '2' || conditionsMap.statusChoose == 2}">class="select" </c:if>>本人已审批</a>
               	    <a id="todoing" href="javascript:void(0)" onClick="statusChoose('3')" <c:if test = "${conditionsMap.statusChoose == '3' || conditionsMap.statusChoose == 3}">class="select" </c:if>>非本人审批</a>
                </div>

				<!-- 排序 -->
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">

						<div class="activitylist_paixu_left">
							<ul>
								<c:choose>
								<c:when
										test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
								<li class="activitylist_paixu_moren">
									</c:when>
									<c:otherwise>
								<li class="activitylist_paixu_left_biankuang">
									</c:otherwise>
									</c:choose>
									<input type="hidden" value="${conditionsMap.orderCreateDateSort }" name="orderCreateDateSort">
									<a onclick="sort(this)">
										创建日期
										<c:choose>
											<c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' and conditionsMap.orderCreateDateSort == 'desc' }">
												<i class="icon icon-arrow-down"></i>
											</c:when>
											<c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' and conditionsMap.orderCreateDateSort == 'asc' }">
												<i class="icon icon-arrow-up"></i>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</a>
									<input type="hidden" value="${conditionsMap.orderCreateDateCss }" name="orderCreateDateCss">
								</li>

								<c:choose>
								<c:when
										test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
								<li class="activitylist_paixu_moren">
									</c:when>
									<c:otherwise>
								<li class="activitylist_paixu_left_biankuang">
									</c:otherwise>
									</c:choose>
									<input type="hidden" value="${conditionsMap.orderUpdateDateSort }" name="orderUpdateDateSort">
									<a onclick="sort(this)">
										更新日期
										<c:choose>
											<c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' and conditionsMap.orderUpdateDateSort == 'desc' }">
												<i class="icon icon-arrow-down"></i>
											</c:when>
											<c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' and conditionsMap.orderUpdateDateSort == 'asc' }">
												<i class="icon icon-arrow-up"></i>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</a>
									<input type="hidden" value="${conditionsMap.orderUpdateDateCss }" name="orderUpdateDateCss">
								</li>
							</ul>
						</div>
						<div class="activitylist_paixu_right">
							查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
						</div>
						<div class="kong"></div>
					</div>
				</div>
					
                </form>
					<!--状态结束-->
					
					 <!--状态结束-->
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
                            <th width="6%"  class="tr">订单金额</th>
                            <th width="6%"  class="tr">已收金额<br />到账金额</th>
                            <th width="7%">转款金额</th>
                            <th width="6%">上一环节审批人</th>
                            <th width="7%">审批状态</th>
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
                 	<%int n = 1;%>
					<c:forEach items="${page.list}" var="reviewInfos">
                        <tr>
                         	<td class="tc">
                         		<%=n++%>
								<!-- 20150804 add 多选框 -->
								<c:if test="${conditionsMap.statusChoose==1 }">
									<input type="checkbox" value="${reviewInfos.id}" name="activityId" onclick="checkIsAll()">
								</c:if>
							</td>
                            <td class="tc">
                            	${reviewInfos.orderno}
                            </td>
                            <td class="tc">
                            	<div title="4535DFDSF" class="tuanhao_cen onshow">
                            		 ${reviewInfos.groupcode}
                            	</div>
                                <div title="${reviewInfos.acitivityName}" class="chanpin_cen qtip"> 
                               		 <a href="${ctx}/activity/manager/detail/${reviewInfos.productId}" target="_blank">${reviewInfos.acitivityName}</a>
                                </div>
                            </td>
                            <td class="tc">
                            	<c:if test="${reviewInfos.producttype eq '7'}">机票</c:if>
                            	<c:if test="${reviewInfos.producttype eq '1'}">单团</c:if>
                            	<c:if test="${reviewInfos.producttype eq '2'}">散拼</c:if>
                            	<c:if test="${reviewInfos.producttype eq '3'}">游学</c:if>
                            	<c:if test="${reviewInfos.producttype eq '4'}">大客户</c:if>
                            	<c:if test="${reviewInfos.producttype eq '5'}">自由行</c:if>
                            	<c:if test="${reviewInfos.producttype eq '10'}">游轮</c:if>
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
                            <td class="tc">
                            	<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
									<c:choose>
									   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && reviewInfos.agent==-1}"> 
									       直客
									   </c:when>
									   <c:otherwise>
									      ${fns:getAgentName(reviewInfos.agent)}
									   </c:otherwise>
									</c:choose>
                            </td>
                            <td class="tr">
                            	<span class="fbold">${fns:getOrderMoney(reviewInfos.producttype, reviewInfos.orderid, '1')}</span>
                            </td>
                            <td class="tr">
					        	<div class="yfje_dd">
									<span class="fbold">${fns:getOrderMoney(reviewInfos.producttype, reviewInfos.orderid, '2')}</span>
								</div>
								<div class="dzje_dd">
									<span class="fbold">${fns:getOrderMoney(reviewInfos.producttype, reviewInfos.orderid, '3')}</span>
								</div>
			        		</td>
                            <td class="tr">
                            	<span class="fbold tdorange">${fns:getReviewMoneyStrByUUID(reviewInfos.transferMoneyUuid, 'mark')}</span>
                            </td>
                            <td class="tc">
                         	   <c:if test="${empty fns:getUserNameById(reviewInfos.lastreviewer)}">无</c:if>${fns:getUserNameById(reviewInfos.lastreviewer)}
                            </td>
                            <td class="invoice_yes tc">
								<c:if test="${reviewInfos.status eq '0'}">审批驳回</c:if>
								<c:if test="${reviewInfos.status eq '1'}">待${fns:getUserNameByIds(reviewInfos.currentReviewer) }审批</c:if>
								<c:if test="${reviewInfos.status eq '2'}">审批通过</c:if>
								<c:if test="${reviewInfos.status eq '3'}">取消申请</c:if>
                            </td>
                            <td class="p0">
	                            <dl class="handle">
	                                <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png" /></dt>
	                                <dd class="">
                               			<p>
	                                    	<span></span>
											<a href="javascript:viewReviewInfo('','${reviewInfos.orderid}','${reviewInfos.reviewid}')">查看</a><br/>
											<c:if test="${reviewInfos.isCurReviewer == true}">
												<a href="javascript:reviewTransferMoney('','${reviewInfos.orderid}','${reviewInfos.reviewid}')">审批</a>
											</c:if>      
											<!-- 审批撤销 -->
											<c:if test="${reviewInfos.isBackReview == true}">
												<a href="javascript:backOutReview('${reviewInfos.reviewid}', '${reviewInfos.status }')">撤销</a>
											</c:if>      
	                                	</p>
	                                </dd>
	                            </dl>
                            </td>
                        </tr>
                   </c:forEach>
                </tbody>
                </table>
				<c:if test="${conditionsMap.statusChoose==1 }">	
				    <div class="pagination">
	                    <dl>
	                        <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
	                        <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
	                        <dd>
								<input class="btn ydbz_x" type="button" value="批量审批"  onclick="jbox__shoukuanqueren_chexiao_fab();">

							</dd>
	                    </dl>
	                </div>
                </c:if>
					<!-- 分页部分 -->
					<div class="pagination clearFix">${page}</div>
					<!--右侧内容部分结束-->
					</div>
</body>
</html>
