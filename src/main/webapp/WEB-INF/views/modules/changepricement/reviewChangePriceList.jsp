<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审核-改价</title>
<!-- 财务-财务 去掉了这几个字 因为审核公用这个页面 -->
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
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

// 	有查询条件的时候，DIV不隐藏
function showSearchPanel(){
	var startTime = $("input[name='startTime']").val();
	var endTime = $("input[name='endTime']").val();
	var channel = $("select[name='channel'] option:selected").val();
	var saler = $("select[name='saler'] option:selected").val();
	var meter = $("select[name='meter'] option:selected").val();
// 	var orderType = $("select[name='orderType'] option:selected").val();
	if(isNotEmpty(startTime)//||isNotEmpty(orderType)
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
	function orderBySub(orderNum) {
		Sorting_add();
		$("#orderBy").val(orderNum);
		$("#searchForm").submit();
		return false;
	};
	function statusChooses(statusNum) {
		//alert(statusNum);
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
	function revokeChangePrice(review_id, my_check_level){
		if(review_id == null || review_id == ""){
			$.jBox.tip("审核ID不能为空", 'error');
			return false;
		}
		if(my_check_level == null || my_check_level == ""){
			$.jBox.tip("您所在的审核层级不能为空，检查是否配置审核层级！", 'error');
			return false;
		}
		$.jBox.confirm("确定要撤销此审核吗？","提示",function(v,h,f){
			if(v=='ok'){
				$.ajax({
					type:"POST",
					url:"${ctx}/changePrice/revokeChangePrice",
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
			      +"     <textarea name='textfield' id='textfield' style='width: 290px;' maxlength='200'></textarea>"
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
						url : "${ctx}/changePrice/batchchangePriceReview",
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
</style>
</head>
<body>
	<page:applyDecorator name="changepricement_review_head">
        <page:param name="current"><c:choose><c:when test="${conditionsMap.headPrd==1}">single</c:when><c:when test="${conditionsMap.headPrd==2}">loose</c:when><c:when test="${conditionsMap.headPrd==3}">study</c:when><c:when test="${conditionsMap.headPrd==5}">free</c:when><c:when test="${conditionsMap.headPrd==4}">bigCustomer</c:when><c:when test="${conditionsMap.headPrd==6}">visa</c:when><c:when test="${conditionsMap.headPrd==7}">airticket</c:when><c:when test="${conditionsMap.headPrd==11}">hotel</c:when><c:when test="${conditionsMap.headPrd==12}">island</c:when><c:when test="${conditionsMap.headPrd==10}">boat</c:when></c:choose></page:param>
    </page:applyDecorator>  
 	 <!--右侧内容部分开始-->
  	<div class="message_box">
   		<c:forEach items="${userJobs}" var="userJob">
   			<div class="message_box_li">
	   			<c:if test="${conditionsMap.userJobId == userJob.id}">
	   				<div class="message_box_li_a curret ydbz_x">
						<a href="${ctx}/changePricement/changePriceReviewList?headPrd=${conditionsMap.headPrd}&userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>
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
						<a style="none" href="${ctx}/changePricement/changePriceReviewList?headPrd=${conditionsMap.headPrd}&userJobId=${userJob.id}"><span><font style="color:white">${fns:abbrs(userJob.deptName,userJob.jobName,40)}</font></span>
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
<!-- <div class="rolelist_btn"> -->
<!--    		<c:forEach items="${userJobs}" var="userJob"> -->
<!--    			<c:if test="${conditionsMap.userJobId == userJob.id}"> -->
<!-- 				<a class="ydbz_x" href="${ctx}/changePricement/changePriceReviewList?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a> -->
<!-- 			</c:if>  -->
<!-- 			<c:if test="${conditionsMap.userJobId != userJob.id}"> -->
<!-- 				<a class="ydbz_x gray" href="${ctx}/changePricement/changePriceReviewList?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a> -->
<!-- 			</c:if>  -->
<!-- 		</c:forEach> -->
<!--     </div> -->
	<form id="searchForm" action="${ctx}/changePricement/changePriceReviewList?headPrd=${conditionsMap.headPrd}"
		method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" /> <input id="userJobId" name="userJobId"
			type="hidden" value="${conditionsMap.userJobId}" />
			<input id="orderBy" name="orderBy"
			type="hidden" value="${conditionsMap.orderBy}" />
			<input type="hidden" value = 10 name="flowType"/><!-- 流程类型10 改价 -->
		<!-- <input id="revid" name="revid" type="hidden" value="0" /> -->
		<!-- 排序字段 默认为1 -->
		<input id="statusChoose" name="statusChoose" type="hidden"
			value="${conditionsMap.statusChoose}" />
		<!-- 状态过滤  默认为全部 -->
		<input name="active" type="hidden" value="1" />
		<!-- 有效标志 用于审批表记录查询 默认为1 -->
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>团号：</label><input style="width:260px"
					class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey"
					name="groupCode" value="${conditionsMap.groupCode}" type="text" />
			</div>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit" />
			</div>
			<a class="zksx">展开筛选</a>
			<div class="ydxbd">
<!-- 			<div class="activitylist_bodyer_right_team_co1"> -->
<!-- 					<div class="activitylist_team_co3_text">团队类型：</div> -->
<!-- 					<select name="orderType" class="selectType"> -->
<!-- 					<c:forEach items="${prds}" var="productinfo"> -->
<!-- 						<option value="${productinfo}"<c:if test="${productinfo == conditionsMap.orderType }">selected="selected"</c:if> > -->
<!-- 							<c:if test="${productinfo == 1}">单团</c:if> -->
<!-- 							<c:if test="${productinfo == 2}">散拼</c:if> -->
<!-- 							<c:if test="${productinfo == 3}">游学</c:if> -->
<!-- 							<c:if test="${productinfo == 4}">大客户</c:if> -->
<!-- 							<c:if test="${productinfo == 5}">自由行</c:if> -->
<!-- 							<c:if test="${productinfo == 6}">签证</c:if> -->
<!-- 							<c:if test="${productinfo == 7}">机票</c:if> -->
<!-- 						</option>											 -->
<!-- 					</c:forEach> -->
<!-- 					</select> -->
<!-- 				</div> -->
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">报批日期：</div><input id="" class="inputTxt dateinput"
						name="startTime" value="${conditionsMap.startTime}"
						onfocus="WdatePicker()" readonly="" /> <span> 至 </span> <input
						id="" class="inputTxt dateinput" name="endTime"
						value="${conditionsMap.endTime}" onclick="WdatePicker()"
						readonly="" />
				</div>
				<div class="kong"></div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">渠道选择：</div>
					<select name="channel" id="channelrefund">
						<option value="">全部</option>
						<c:if test="${not empty fns:getAgentList() }">
							<c:forEach items="${fns:getAgentList()}" var="agentinfo">
								<option value="${agentinfo.id }"
									<c:if test="${conditionsMap.channel==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName
									}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
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
					<select name='meter' id="meterrefund">
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
<!-- 			<div class="activitylist_paixu_left"> -->
<!-- 				<ul> -->
<!-- 					<li -->
<!-- 						style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li> -->
<!-- 					<li class="activitylist_paixu_left_biankuang activitylist_paixu_moren"><a -->
<!-- 						onClick="orderBySub('createtime');">创建时间<i -->
<!-- 							class="icon icon-arrow-down"></i> -->
<!-- 					</a> -->
<!-- 					</li> -->
<!-- 					<li class="activitylist_paixu_left_biankuang liid"><a -->
<!-- 						onClick="orderBySub('updatetime');">更新日期<i -->
<!-- 							class="icon icon-arrow-down"></i> </a> -->
<!-- 					</li> -->
<!-- 				</ul> -->
<!-- 			</div> -->
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
             <!--状态结束-->
             <table id="contentTable" class="table activitylist_bodyer_table">
                 <thead>
                     <tr>
                         <th width="4%">序号</th>
                         <th width="10%"><span class="tuanhao on">团号</span>&nbsp;/&nbsp;
                         <span class="chanpin"> 产品编号
                         </span></th>
                         <th width="5%">
                         	<c:if test = "${conditionsMap.headPrd != 11 && conditionsMap.headPrd != 12}">
								<span class="creator on">销售</span>/<span class="salercls">下单人</span>
							 </c:if>
							 <c:if test = "${conditionsMap.headPrd == 11 || conditionsMap.headPrd == 12}">
								<span>下单人</span>
							 </c:if>
					 	 </th>
                         <th width="5%">计调</th>
                         <th width="9%">订单编号</th>
                         <th width="9%">下单时间</th>
                         <!-- <th width="9%">出/截团日期</th>   -->
                         <th width="8%">款项</th> 
						 <th width="8%">订单总额</th>
                         <th width="7%">原金额</th>
                         <th width="7%">改价后金额</th>
                         <th width="7%">上一环节<br />审批人</th>
                         <th width="7%">审核状态</th>
                         <th width="8%">改价理由</th>
                         <th width="4%">操作</th>
                     </tr>
                 </thead>
                 <tbody>
                 <!-- 遍历显示结果 start -->
					<%
						int n = 1;
					%>
					<c:forEach items="${page.list}" var="changePriceReviewInfo">
                 	<tr>
                     	<td><c:if test="${conditionsMap.statusChoose == '1'}"><input type="checkbox" name="checkboxrevid" value="${changePriceReviewInfo.revid}" id="checkbox<%=n%>" /></c:if><%=n++%></td>
                         <td>
                         	<div class="tuanhao_cen onshow">${changePriceReviewInfo.groupcode}</div>
                     		<div class="chanpin_cen qtip"  title="${changePriceReviewInfo.chanpname}">
                     		
								<c:if test="${changePriceReviewInfo.prdtype ==7}">
									<a href="${ctx}/airTicket/actityAirTickettail/${changePriceReviewInfo.chanpid}" target="_blank">${changePriceReviewInfo.chanpname}</a>
								</c:if>
								<c:if test="${changePriceReviewInfo.prdtype !=7 && changePriceReviewInfo.prdtype !=6}">
									<a href="${ctx}/activity/manager/detail/${changePriceReviewInfo.chanpid}" target="_blank">${changePriceReviewInfo.chanpname}</a>
								</c:if>
								<c:if test="${changePriceReviewInfo.prdtype ==6}">
									<a href="${ctx}/visa/preorder/visaProductsDetail/${changePriceReviewInfo.chanpid}" target="_blank">${changePriceReviewInfo.chanpname}</a>
								</c:if>
						   </div>
                 		</td>
                         <td>
                         	<c:if test = "${conditionsMap.headPrd != 11 && conditionsMap.headPrd != 12}">
								<div class="creator_cen onshow" title="JHGJLXS5674--切位">${changePriceReviewInfo.salerName}</div>
								<div class="salercls_cen qtip">${fns:getUserNameById(changePriceReviewInfo.salecreateby)}</div>
							</c:if>
							<c:if test = "${conditionsMap.headPrd == 11 || conditionsMap.headPrd == 12}">
								${fns:getUserNameById(changePriceReviewInfo.salecreateby)}
							</c:if>
						</td>
                         <td>${fns:getUserNameById(changePriceReviewInfo.jidcreateby)}</td>
                         <td>${changePriceReviewInfo.orderno}</td>
                         <td class="tc"><fmt:formatDate value="${changePriceReviewInfo.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <!--  <td class="p0">
                             <div class="out-date">2014-12-20</div>
                             <div class="close-date">2014-10-24</div>
                         </td> -->
                         <td>${changePriceReviewInfo.changedfund}</td>
						 <td>${fns:getOrderTotal(changePriceReviewInfo.orderid,changePriceReviewInfo.prdtype)}</td>
                         <td class="tr">${fns:getCurrencyInfo(changePriceReviewInfo.currencyid,'0','mark')}<span class="fbold tdorange">${changePriceReviewInfo.curtotalmoney}</span></td>
                         <td class="tr">${fns:getCurrencyInfo(changePriceReviewInfo.currencyid,'0','mark')}<span class="fbold tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${changePriceReviewInfo.changedtotalmoney}" /></span></td>
                         <td>${fns:getUserNameById(changePriceReviewInfo.lastoperator)}</td>
                         <td class="invoice_yes">${fns:getNextReview(changePriceReviewInfo.revid) }</td>
                         <td>${changePriceReviewInfo.remark}</td>
                         <td class="p0">
                             <dl class="handle">
                                 <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                                 <dd class="">
                                 <p>
                                     <span></span>
                                     <c:if test="${changePriceReviewInfo.revLevel == changePriceReviewInfo.curlevel && changePriceReviewInfo.revstatus == '1'}">
										<a href="${ctx}/changePricement/changePriceReviewDetail?orderid=${changePriceReviewInfo.orderid}&revid=${changePriceReviewInfo.revid}&prdType=${changePriceReviewInfo.prdtype}&flag=0&nowlevel=${changePriceReviewInfo.curlevel}">审批</a><br />
									</c:if>
									<c:if test="${changePriceReviewInfo.prdtype == 7 || changePriceReviewInfo.prdtype == 6}">
										<c:if test="${not empty changePriceReviewInfo.chanpid}"><a href="${ctx }/cost/manager/forcastList/${changePriceReviewInfo.chanpid}/${changePriceReviewInfo.prdtype}" target="_blank">预报单</a></c:if>
	                              		<c:if test="${not empty changePriceReviewInfo.chanpid}"><a href="${ctx }/cost/manager/settleList/${changePriceReviewInfo.chanpid}/${changePriceReviewInfo.prdtype}" target="_blank">结算单</a></c:if>
									</c:if>
									<c:if test="${changePriceReviewInfo.prdtype != 7 && changePriceReviewInfo.prdtype != 6&& changePriceReviewInfo.prdtype != 8&& changePriceReviewInfo.prdtype != 9}">
										<c:if test="${not empty changePriceReviewInfo.chanpid}"><a href="${ctx }/cost/manager/forcastList/${changePriceReviewInfo.groupid}/${changePriceReviewInfo.prdtype}" target="_blank">预报单</a></c:if>
	                              		<c:if test="${not empty changePriceReviewInfo.chanpid}"><a href="${ctx }/cost/manager/settleList/${changePriceReviewInfo.groupid}/${changePriceReviewInfo.prdtype}" target="_blank">结算单</a></c:if>
									</c:if>
									<a href="${ctx}/changePricement/changePriceReviewDetail?orderid=${changePriceReviewInfo.orderid}&revid=${changePriceReviewInfo.revid}&prdType=${changePriceReviewInfo.prdtype}&flag=1&nowlevel=${changePriceReviewInfo.curlevel}">查看</a><br />
	                                 <c:if test="${fns:getUser().id == changePriceReviewInfo.lastoperator 
										&& changePriceReviewInfo.revstatus == 1 && changePriceReviewInfo.curlevel != 1}">
										<a href="#" onclick="revokeChangePrice('${changePriceReviewInfo.revid}','${changePriceReviewInfo.revLevel}')">撤销</a>
									</c:if>
                                 </p>
                                 </dd>
                             </dl>
                         </td>
                     </tr>
                     </c:forEach>
                 </tbody>
             </table>
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
         		<!-- 分页部分 -->
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
    <!--右侧内容部分结束-->
</body>
</html>
