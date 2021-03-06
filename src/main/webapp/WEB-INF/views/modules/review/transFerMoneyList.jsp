<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转团转款审核</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<!-- 20150810 批量审核js -->
<script src="${ctxStatic}/modules/batchReview/batchReview.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
		$(".selectinput" ).comboboxInquiry();
		launch();
		//操作浮框
		operateHandler();
		$("div.message_box div.message_box_li").hover(function(){
		    $("div.message_box_li_hover",this).show();
		},function(){
	        $("div.message_box_li_hover",this).hide();
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
		$(".bgMainRight").delegate("select[class='selectType']","change",function(){
		   $("#searchForm").submit();
		});
		//产品销售和下单人切换
		switchSalerAndPicker();
		$('#order-saler').comboboxInquiry();
		$('#order-picker').comboboxInquiry();
		$('#channelrefund').comboboxInquiry();
		$('#meterrefund').comboboxInquiry();
		
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
           
   $("#createTimeBegin").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
	$("#createTimeEnd").datepicker({
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
    
    $('#order-saler').val('');
	$('#order-picker').val('');
	$('#channelrefund').val('');
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

window.open("${ctx}/orderReview/manage/viewTransFerMoneyReviewInfo?orderId=" + orderId + "&rid="+rid+"&roleId="+roleId+"&userLevel=${reviewVO.userLevel}");
}
//审核转团转款申请
function reviewExitGroup(roleId,orderId,rid){
window.open("${ctx}/orderReview/manage/reviewTransFerMoney?orderId=" + orderId + "&rid="+rid+"&roleId="+roleId+"&userLevel=${reviewVO.userLevel}");
}
//同一审核流程切换审核角色查询
function chooseRole(rid){
	$("#rid").val(rid);
	$("#searchForm").submit();
}
function backOutReview(rid,userLevel){
	
	$.jBox.confirm("确定要撤销审核吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				async: false, 
				url : "${ctx}/orderReview/manage/backOutReview",
				data : {
					rid: rid,
					userLevel : userLevel
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
<page:applyDecorator name="transFerMoney_review_head">
             <page:param name="current"><c:choose><c:when test="${reviewVO.orderType==1}">single</c:when><c:when test="${reviewVO.orderType==2}">loose</c:when><c:when test="${reviewVO.orderType==3}">study</c:when><c:when test="${reviewVO.orderType==5}">free</c:when><c:when test="${reviewVO.orderType==4}">bigCustomer</c:when><c:when test="${flowType==20}">visa</c:when><c:when test="${reviewVO.orderType==7}">airticket</c:when><c:when test="${reviewVO.orderType==10}">cruise</c:when><c:when test="${reviewVO.orderType==11}">hotel</c:when><c:when test="${reviewVO.orderType==12}">island</c:when></c:choose></page:param>
       </page:applyDecorator>
	<div id="sea">
	<!--右侧内容部分开始-->	
					
					<form id="searchForm" action="${ctx}/orderReview/manage/getTransFerMoneyReviewList"
						method="post">
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
						   		<a  onClick="chooseRole('${role.id}');">
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
						<input id="orderType"name="orderType" type="hidden" value="${reviewVO.orderType}" />
						<input id="pageNo" name="pageNo" type="hidden"
							value="${page.pageNo}" /> <input id="pageSize" name="pageSize"
							type="hidden" value="${page.pageSize}" /> <input id="orderBy"
							name="orderBy" type="hidden" value="${page.orderBy}" />
							 <input id="revid" name="revid" type="hidden" value="0" />
						<!-- 排序字段 默认为1 -->
						<input id="statusChoose" name="statusChoose" type="hidden"
							value="${reviewVO.reviewStatus}" />
						<!-- 状态过滤  默认为0 全部 -->
						<input name="flowType" type="hidden" value="1" />
						<!-- 流程类型 退款 退票 -->
						<input name="active" type="hidden" value="1" />
						<!-- 有效标志 用于审批表记录查询 默认为1 -->
						<div class="activitylist_bodyer_right_team_co">
							<div class="activitylist_bodyer_right_team_co2 pr wpr20">
								<label>团号：</label><input style="width:260px"
									class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey"
									name="groupCode" value="${reviewVO.groupCode}" type="text" />
							</div>
					<!--  		<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">团队类型：</div>
									<select name="orderType" class="selectType">
									<c:forEach items="${productList}"
										var="productinfo">
										<option value="${productinfo.productType}"<c:if test="${productinfo.productType eq reviewVO.orderType }">selected="selected"</c:if> >${productinfo.productName}</option>											
									</c:forEach>
									</select>
								</div>  -->
							<div class="form_submit">
								<input class="btn btn-primary ydbz_x" value="" type="submit" />
								<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
							</div>
							<div class="zksx">筛选</div>
							<div class="ydxbd">
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">订单号：</div>
									<input style="width:130px"
									class="txtPro inputTxt inquiry_left_text" 
									name="orderNum" value="${reviewVO.orderNum}" type="text" />
								</div>
								<div class="activitylist_bodyer_right_team_co2">
					                <label class="activitylist_team_co3_text">报批日期：</label>
					                <input id="createTimeBegin" class="inputTxt dateinput" name="createTimeBegin"  value="${reviewVO.createTimeBegin }"  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('createTimeEnd').value==''){$dp.$('createTimeEnd').value=vvv;}}})" readonly/> 
					                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
					                <input id="createTimeEnd" class="inputTxt dateinput" name="createTimeEnd" value="${reviewVO.createTimeEnd }"  onClick="WdatePicker()" readonly/>
				            	</div>
								
								<div class="kong"></div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">渠道：</div>
									<select name="channel" id="channelrefund">
										<option value=""></option>
										<c:if test="${not empty fns:getAgentList() }">
											<c:forEach items="${fns:getAgentList()}" var="agentinfo">
												<c:choose>
													<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and agentinfo.agentName eq '非签约渠道' }">
														<option value="${agentinfo.id }" <c:if test="${agentinfo.id==reviewVO.channel }">selected="selected"</c:if>>未签</option>
													</c:when>
													<c:otherwise><option value="${agentinfo.id }" <c:if test="${agentinfo.id==reviewVO.channel }">selected="selected"</c:if>>${agentinfo.agentName}</option></c:otherwise>
												</c:choose>
											</c:forEach>
										</c:if>
									</select>
								</div>
								
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">销售：</div>
									<select name="saler" id="order-saler" class="selectinput"><!--  id="salerrefund" -->
							            <option value=""></option>
							            <c:forEach items="${users }" var="user">
							            	<option value="${user.id }" <c:if test="${user.id==reviewVO.saler }">selected="selected"</c:if>>${user.name }</option>
							            </c:forEach>
							        </select>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">计调：</div>
									<select name="meter" id="meterrefund" class="selectinput">
							            <option value=""></option>
							            <c:forEach items="${users }" var="user">
							            	<option value="${user.id }" <c:if test="${user.id==reviewVO.meter }">selected="selected"</c:if>>${user.name }</option>
							            </c:forEach>
							        </select>
								</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">游客：</div>
									<input style="width:130px"
									class="txtPro inputTxt inquiry_left_text" 
									name="travelerName" value="${reviewVO.travelerName}" type="text" />
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<div class="activitylist_team_co3_text">下单人：</div>
								<select name="picker" id="order-picker">
									<option></option>
									<c:forEach items="${users }" var="user">
						            	<option value="${user.id }" <c:if test="${user.id==reviewVO.picker }">selected="selected"</c:if>>${user.name }</option>
						            </c:forEach>
								</select>
							</div>
							<div class="kong"></div>
							</div>
						</div>
					</form>
					<div class="activitylist_bodyer_right_team_co_paixu">
						<div class="activitylist_paixu_left">
			            <ul>
				            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
				            <li class="activitylist_paixu_left_biankuang liaa.createDate"><a onClick="sortby('aa.createDate',this)">创建时间</a></li>
				            <li class="activitylist_paixu_left_biankuang liaa.updateDate"><a onClick="sortby('aa.updateDate',this)">更新时间</a></li>
			            </ul>
					</div>
							
						</div>
					</div>
					<!--状态开始-->
					<div class="supplierLine"><c:if test=""></c:if>
						<a  <c:if test="${reviewVO.reviewStatus==0 }">class="select"</c:if> onClick="statusChoose(0);">全部</a> 
						<a <c:if test="${reviewVO.reviewStatus==1 }">class="select"</c:if> onClick="statusChoose(1);">待本人审核</a>
						<a <c:if test="${reviewVO.reviewStatus==4 }">class="select"</c:if> onClick="statusChoose(4);">审核中</a>
						<a  <c:if test="${reviewVO.reviewStatus==2 }">class="select"</c:if> onClick="statusChoose(2);">未通过</a>
						<a <c:if test="${reviewVO.reviewStatus==3 }">class="select"</c:if> onClick="statusChoose(3);">已通过</a>
						<a <c:if test="${reviewVO.reviewStatus==5 }">class="select"</c:if> onClick="statusChoose(5);">已取消</a>
						<div class="activitylist_paixu_right">
								查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
							</div>
							<div class="kong"></div>
					</div>
					<!--状态结束-->
					<table id="contentTable" class="table activitylist_bodyer_table">
						<thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="8%">报批日期</th>
                            <th width="8%">订单号</th>
                            <th width="9%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
                            <th width="6%">渠道</th>
                            <th width="6%">计调</th>
                            <th width="7%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
                            <th width="6%">游客</th>
                            <th width="8%">订单总额</th>
                			<th width="8%">已收金额<br/>到账金额</th>
                            
                            <th width="8%">转款金额</th>
                            <th width="5%">上一环节<br />审批人</th>
                            <th width="8%">原因备注</th>
                            <th width="6%">审核状态</th>
                            <th width="4%">操作</th>
                        </tr>
						</thead>
						<tbody>
						<!-- 无查询结果 --> 
							<c:if test="${fn:length(page.list) <= 0 }">
								<tr class="toptr" >
					                 <td colspan="15" style="text-align: center;">
										 暂无搜索结果
					                 </td>
								</tr>
					        </c:if>
							<!-- 遍历显示结果 start -->
							<%
								int n = 1;
							%>
							<c:forEach items="${page.list}" var="reviewInfos">
								<tr>
									<td>
										<!-- 20150804 add 多选框 -->
										<c:if test="${reviewVO.reviewStatus==1 }">
										 	<input type="checkbox" value="${reviewInfos.nowLevel}@${reviewInfos.rid}" name="activityId" >
										</c:if>
										<%=n++%>
									</td>
									<td><fmt:formatDate value="${reviewInfos.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td class="tc">${reviewInfos.orderNum}</td>
									<td>
									<div class="tuanhao_cen onshow">
										${reviewInfos.groupCode}
									</div>
									<div class="chanpin_cen qtip" title="${reviewInfos.acitivityName}">
									<a href="${ctx}/activity/manager/detail/${reviewInfos.productId}" target="_blank">${reviewInfos.acitivityName}</a>

									</div>									
									<c:choose>
										<c:when test="${reviewInfos.intermodalType == 1}">
											<span class="lianyun_name">全国联运</span>
										</c:when>
										<c:when test="${reviewInfos.intermodalType == 2}">
											<span class="lianyun_name">分区联运</span>
										</c:when>
										<c:otherwise>
											<span class="lianyun_name">无联运</span>
										</c:otherwise>
									</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and reviewInfos.orderCompanyName eq '非签约渠道' }">未签</c:when>
											<c:otherwise>${reviewInfos.orderCompanyName}</c:otherwise>
										</c:choose>
									</td>
									<td>${fns:getUserNameById(reviewInfos.meter)}</td>
									<td><span class="order-saler onshow">${fns:getUserNameById(reviewInfos.saler)}</span><span class="order-picker">${fns:getUserNameById(reviewInfos.picker)}</span></td>
									<td>${reviewInfos.travelerName}</td>
									<td><span class="tdorange fbold">
										${reviewInfos.totalMoney}
									</span></td>
					        		<td>
					        		<div class="yfje_dd">
										<span class="fbold">${reviewInfos.payedMoney}</span>
									</div>
					        		<div class="dzje_dd">
										<span class="fbold">${reviewInfos.accountedMoney}</span>
									</div>
					        		</td>
									
									<td>${reviewInfos.travelerMoney}</td>
									
									<td>${reviewInfos.beforeReviewName}</td>
									<td>${reviewInfos.createReason}</td>
									<td>
									<!--  	<c:choose>
										<c:when test="${reviewVO.reviewStatus==1 }">
											<c:if test="${reviewInfos.myStatus==1}">待审核</c:if>
											<c:if test="${reviewInfos.myStatus==2}">未通过</c:if>
											<c:if test="${reviewInfos.myStatus==3}">已通过</c:if>
										</c:when>
										<c:otherwise>
											<c:if test="${reviewInfos.reviewStatus==1}">审核中</c:if>
											<c:if test="${reviewInfos.reviewStatus==0}">未通过</c:if>
											<c:if test="${reviewInfos.reviewStatus==2}">已通过</c:if>
											<c:if test="${reviewInfos.reviewStatus==3}">已通过</c:if>
											<c:if test="${reviewInfos.reviewStatus==4}">已取消</c:if>
										</c:otherwise>
									
									</c:choose>-->
									${fns:getNextReview(reviewInfos.rid)}
									</td>									
									<td class="p0">
										<dl class="handle">
											<dt>
												<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
											</dt>
											<dd class="">
			                                    <p>
			                                        <span></span>
													<a href="javascript:viewReviewInfo('${reviewVO.rid}','${reviewInfos.orderId}','${reviewInfos.rid}')">查看</a><br/>
													<c:if test="${reviewInfos.myStatus==1 && reviewInfos.reviewStatus==1}">
													<a href="javascript:reviewExitGroup('${reviewVO.rid}','${reviewInfos.orderId}','${reviewInfos.rid}')">审批</a>
													</c:if>
													<!-- 审核撤销 -->
										 			<c:if test="${fns:getUser().id ==reviewInfos.updateBy && reviewVO.userLevel +1 == reviewInfos.nowLevel && reviewInfos.reviewStatus== 1 }">
														<a href="javascript:backOutReview(${reviewInfos.rid},${reviewVO.userLevel})">撤销</a>
													</c:if>   
			                                    </p>
		                                    </dd>
										</dl>
									</td>
								</tr>
							</c:forEach>
							<!-- 遍历显示结果 end -->
							<!-- 20150804 全选反选 -->
							<c:if test="${reviewVO.reviewStatus==1 }">
								<tr class="checkalltd">
									<td colspan='19' class="tl">
										<label>
											<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
										</label>
										<label>
											<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
										</label>
										<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:payedConfirmNew('${ctx}',1,1,'${reviewVO.userLevel}','/orderReview/manage/batchReviewTransFerMoneyInfo');" >批量审批</a>
									</td>
								</tr>
							</c:if>
						</tbody>
					</table>
					<!-- 分页部分 -->
					<div class="pagination clearFix">${page}</div>
					<!--右侧内容部分结束-->		
		<!--footer-->
		<!--footer***end-->
</body>
</html>
