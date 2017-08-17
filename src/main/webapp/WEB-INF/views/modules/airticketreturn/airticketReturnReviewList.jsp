<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-退票</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
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
</style>
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
	$(function(){
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
						url : "${ctx}/airticketreturn/batchreturnReview",
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
	// 有查询条件的时候，DIV不隐藏
	function showSearchPanel(){
		var startTime = $("input[name='startTime']").val();
		var endTime = $("input[name='endTime']").val();
		var channel = $("select[name='channel'] option:selected").val();
		var saler = $("select[name='saler'] option:selected").val();
		var meter = $("select[name='meter'] option:selected").val();
		
		if(isNotEmpty(startTime)
				||isNotEmpty(endTime) || isNotEmpty(channel)
				||isNotEmpty(saler) || isNotEmpty(meter)){
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
	function statusChooses(statusNum) {
		$("#statusChoose").val(statusNum);
		$("#searchForm").submit();
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
	function cancelAirticketReturnreview(review_id, my_check_level){
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
</head>
<body>
            	<!--右侧内容部分开始-->
	           <div class="message_box">
			   		<c:forEach items="${userJobs}" var="userJob">
			   			<div class="message_box_li">
				   			<c:if test="${conditionsMap.userJobId == userJob.id}">
								<div class="message_box_li_a curret">
									<a href="${ctx}/airticketreturn/airticketReturnReviewList?userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>
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
								<div class="message_box_li_a">
									<a href="${ctx}/airticketreturn/airticketReturnReviewList?userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>
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
<!-- 	           <div class="rolelist_btn"> -->
<!-- 			   		<c:forEach items="${userJobs}" var="userJob"> -->
<!-- 			   			<c:if test="${conditionsMap.userJobId == userJob.id}"> -->
<!-- 							<a class="ydbz_x" href="${ctx}/airticketreturn/airticketReturnReviewList?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a> -->
<!-- 						</c:if>  -->
<!-- 						<c:if test="${conditionsMap.userJobId != userJob.id}"> -->
<!-- 							<a class="ydbz_x gray" href="${ctx}/airticketreturn/airticketReturnReviewList?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a> -->
<!-- 						</c:if>  -->
<!-- 					</c:forEach> -->
<!-- 			    </div> -->
                <form id="searchForm" action="${ctx}/airticketreturn/airticketReturnReviewList" method="post">
                <input id="pageNo" name="pageNo" type="hidden"
							value="${page.pageNo}" /> <input id="pageSize" name="pageSize"
							type="hidden" value="${page.pageSize}" /> <input id="orderBy"
							name="orderBy" type="hidden" value="${page.orderBy}" /> <!-- ${conditionsMap.orderBy} -->
							<input type="hidden" value = "${conditionsMap.userJobId}" name="userJobId"/><!-- 职位id -->
							<input type="hidden" value = 7 name="orderType"/><!-- 产品类型 7 机票 -->
							<input type="hidden" value = 3 name="flowType"/><!-- 流程类型 3 退票 -->
						<!-- 排序字段 默认为1 -->
						<input id="statusChoose" name="statusChoose" type="hidden"
							value="${conditionsMap.statusChoose}" />
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
                         <div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">渠道选择：</div>
									<select name="channel" id="channelrefund">
										<option value="">全部</option>
										<c:if test="${not empty fns:getAgentList() }">
											<c:forEach items="${fns:getAgentList()}" var="agentinfo">
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
										onfocus="WdatePicker()"
										readonly="" /> <span> 至 </span> <input id=""
										class="inputTxt dateinput" name="endTime" value="${conditionsMap.endTime}"
										onclick="WdatePicker()" readonly="" />
								</div>
                            <div class="kong"></div>
                            <div class="kong"></div>
                            <div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">下单人：</div>
									<select class="sel-w1" style='height : 28px' name="saler" id ="salerrefund">
										<option value="" selected="selected">不限</option>
										<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
											<!-- 用户类型  1 代表销售 -->
											<option value="${userinfo.id }" <c:if test="${conditionsMap.saler==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
										</c:forEach>
									</select>
								</div>
                            <div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">计调：</div>
									<select name='meter' id = "meterrefund">
										<option value="" selected="selected">不限</option>
										<c:forEach items="${fns:getSaleUserList('3')}" var="userinfo">
											<!-- 用户类型  3 代表计调 -->
											<option value="${userinfo.id }" <c:if test="${conditionsMap.meter==userinfo.id }">selected="selected"</c:if>>${userinfo.name }</option>
										</c:forEach>
									</select>
								</div>
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
                        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                        <div class="kong"></div>
                    </div>
                </div>
                </form>
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
                <table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="10%">订单编号</th>
                            <th width="13%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品编号</span></th>
                            <th width="5%">计调</th>
                            <th width="5%"><span class="creator on">销售</span>/<span
					class="salercls">下单人</span></th>
                            <th width="5%">游客</th>
                            <th width="9%">下单时间</th>
                            <th width="9%">报批日期</th>
                            <th width="10%">退票金额</th>
                            <th width="7%">上一环节<br />审批人</th>
                            <th width="11%">原因备注</th>
                            <th width="6%">审核状态</th>
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
                        	<td><c:if test="${conditionsMap.statusChoose == '1'}"><input type="checkbox" name="checkboxrevid" value="${returnReviewInfo.revid}" id="checkbox<%=n%>" /></c:if><%=n++%></td>
                            <td>${returnReviewInfo.orderno}</td>
                            <td>
                            	<div class="tuanhao_cen onshow">${returnReviewInfo.groupcode}</div>
                        		<div class="chanpin_cen qtip" title="JHGJLXS5674--切位">${returnReviewInfo.productcode}</div>
                    		</td>
                            <td>${fns:getUserNameById(returnReviewInfo.jidcreateby)}</td>
                            <td><div class="creator_cen onshow">${returnReviewInfo.salerName}</div>
								<div class="salercls_cen qtip" title="JHGJLXS5674--切位">${fns:getUserNameById(returnReviewInfo.salecreateby)}</div>
							</td>
                            <td>${returnReviewInfo.tname}</td><!-- tid 为游客id -->
                            <td class="tc"><fmt:formatDate value="${returnReviewInfo.orderdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td class="tc"><fmt:formatDate value="${returnReviewInfo.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td class="tr"><span class="fbold tdorange">${returnReviewInfo.payprice}</span></td>
                            <td>${fns:getUserNameById(returnReviewInfo.lastoperator)}</td>
                            <td>${returnReviewInfo.createReason}</td>
                            <td class="invoice_yes">${fns:getNextReview(returnReviewInfo.revid) }</td>
                            <td class="p0">
                                <dl class="handle">
                                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"/></dt>
                                    <dd class="">
                                    <p>
                                    	<c:if test="${fns:getUser().id == returnReviewInfo.lastoperator 
										&& returnReviewInfo.revstatus == 1 && returnReviewInfo.curlevel != 1}">
											<a href="#" onclick="cancelAirticketReturnreview('${returnReviewInfo.revid}','${returnReviewInfo.revLevel}')">撤销</a>
										</c:if>
                                        <span></span>
                                        <c:if test="${returnReviewInfo.revLevel == returnReviewInfo.curlevel && returnReviewInfo.revstatus == '1'}">
                                        <a href="${ctx}/airticketreturn/airticketReturnReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=0&revid=${returnReviewInfo.revid}&userJobId=${conditionsMap.userJobId}">审批</a><br />
                                        </c:if>
										<a href="${ctx}/airticketreturn/airticketReturnReviewDetail?orderId=${returnReviewInfo.orderid}&travelerId=${returnReviewInfo.tid}&flag=1&revid=${returnReviewInfo.revid}&userJobId=${conditionsMap.userJobId}">查看</a>
                                    </p>
                                    </dd>
                                </dl>
                            </td>
                        </tr>
                        </c:forEach>
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
                <!--右侧内容部分结束-->
           
</body>
</html>
