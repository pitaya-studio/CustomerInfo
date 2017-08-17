<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证返佣审核</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	
	g_context_url = "${ctx}";
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	switchSalerAndPicker();
	//审核角色切换按钮上显示待审核条数
	$("div.message_box div.message_box_li").hover(function(){
	    $("div.message_box_li_hover",this).show();
	},function(){
        $("div.message_box_li_hover",this).hide();
	});
	
	var _$orderBy = $("#orderBy").val();
    if(_$orderBy==""){
        _$orderBy="r.updateDate DESC";
    }
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if ($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
        }
    });
    
    $("#agent").comboboxInquiry();
    $("#saler").comboboxInquiry();
    $("#op").comboboxInquiry();
	
	//首次打印提醒
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	});
	
	//如果展开部分有查询条件的话，默认展开，否则收起	
	var flag = false;
	if($("#reviewCreateDateStart").val() != "" && $("#reviewCreateDateStart").val() != null) {
		flag = true;
	}
	if($("#reviewCreateDateEnd").val() != "" && $("#reviewCreateDateEnd").val() != null) {
		flag = true;
	}
	if($("#agent").val() != "" && $("#agent").val() != null) {
		flag = true;
	}
	if($("#saler").val() != "" && $("#saler").val() != null) {
		flag = true;
	}
	if($("#op").val() != "" && $("#op").val() != null) {
		flag = true;
	}
	if($("#rebatesAmountStrat").val() != "" && $("#rebatesAmountStrat").val() != null) {
		flag = true;
	}
	if($("#rebatesAmountEnd").val() != "" && $("#rebatesAmountEnd").val() != null) {
		flag = true;
	}
	if($("#printFlag").val() != "" && $("#printFlag").val() != null) {
		flag = true;
	}
	if($("#order_no").val() != "" && $("#order_no").val() != null) {
		flag = true;
	}
	if(flag) {
		$('.zksx').click();
	}
});

function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/DESC/g)){
            sortBy = sortBy.replace(/DESC/g,"");
        }else{
            sortBy = $.trim(sortBy)+" DESC";
        }
    }
    
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
}
function chooseRole(rid){
	$("#jobId").val(rid);
	$("#searchForm").submit();
}
function resetSearchParams(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#agent').val('');
    $('#saler').val('');
    $('#op').val('');
}


//按批次取消签证返佣
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
		          url:g_context_url+"/review/visaRebates/visaRevatesCancelAjax",
		          dataType:"json",
		          data:{'revid':revid},
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


//-----------王新伟added 201808-25 签证批量返佣审核相关  开始----------
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

//批量审核按钮 操作
function multiReviewVisaRebates(ctx,orderid,agentid,obj){
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
      		 				  revids:str,
							      remarks:remarks,
							      result:v
							  };
			            
			      $.ajax({ 
			          type:"POST",
			          url:ctx+"/review/visaRebates/multiReviewVisaRebates",
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
//-----------王新伟added 201808-25 签证批量返佣审核相关  开始----------


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
	<page:applyDecorator name="rebates_review_head2">
		<c:choose>
			<c:when test="${reviewVO.orderType==1}"><page:param name="current">single</page:param></c:when>
			<c:when test="${reviewVO.orderType==2}"><page:param name="current">loose</page:param></c:when>
			<c:when test="${reviewVO.orderType==3}"><page:param name="current">study</page:param></c:when>
			<c:when test="${reviewVO.orderType==5}"><page:param name="current">free</page:param></c:when>
			<c:when test="${reviewVO.orderType==4}"><page:param name="current">bigCustomer</page:param></c:when>
			<c:when test="${orderType==6}"><page:param name="current">visa</page:param></c:when>
			<c:when test="${reviewVO.orderType==7}"><page:param name="current">airticket</page:param></c:when>
			<c:when test="${reviewVO.orderType==10}"><page:param name="current">cruise</page:param></c:when>
		</c:choose>
	</page:applyDecorator>
	<form id="searchForm" method="post" action="${ctx}/review/visaRebates/financeList?reviewStatus=${param.reviewStatus}">
		<div class="message_box">
	   		<c:forEach items="${userJobList}" var="job">
	   			<div class="message_box_li">
		   			<c:choose>
		   				<c:when test="${jobId eq job.id}">
		   					<a>
			   					<div class="message_box_li_a curret">
									<span>
										${fns:abbrs(job.deptName,job.jobName,40)}
									</span>
									<c:if test ="${job.count gt 0}">
										<div class="message_box_li_em" >
										${job.count}								
										</div>
									</c:if>
									<c:if test="${fns:getStringLength(job.deptName,job.jobName) gt 37}">
										<div class="message_box_li_hover">
											${job.deptName }_${job.jobName }
										</div>
									</c:if>
								</div>
		   					</a>
		   				</c:when>
		   				<c:otherwise>
			   				<a onClick="chooseRole('${job.id}');">
			   					<div class="message_box_li_a">
									<span>${fns:abbrs(job.deptName,job.jobName,40)}</span>
									<c:if test ="${job.count gt 0}">
										<div class="message_box_li_em" >
											${job.count}					
										</div>
									</c:if>
									<c:if test="${fns:getStringLength(job.deptName,job.jobName) gt 37}">
										<div class="message_box_li_hover">
											${job.deptName }_${job.jobName }
										</div>
									</c:if>
								</div>
			   				</a>
		   				</c:otherwise>				   			
		   			</c:choose>
				</div>
			</c:forEach>
	    </div>
		<input type="hidden" value="${jobId}" name="jobId" id="jobId"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>团号：</label>
				<input id="groupCode" class="txtPro inputTxt inquiry_left_text" type="text" value="${paramMap['p.groupCode']}" name="groupCode" style="width:260px">
			</div>
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" type="submit" value="搜索">
				<input class="btn ydbz_x" type="button" value="清空所有条件" onclick="resetSearchParams()">
			</div>
			<a class="zksx zksx-on">筛选</a>
			<div class="ydxbd" style="display: none;">
<%--				<div class="activitylist_bodyer_right_team_co1">--%>
<%--					<div class="activitylist_team_co3_text">团队类型：</div>--%>
<%--					<select name="activityKind">--%>
<%--						<option value="">全部产品</option>--%>
<%--						<option value="1">单团</option>--%>
<%--						<option value="2">散拼</option>--%>
<%--						<option value="3">游学</option>--%>
<%--						<option value="4">大客户</option>--%>
<%--						<option value="5">自由行</option>--%>
<%--						<option value="6">签证</option>--%>
<%--						<option value="7">机票</option>--%>
<%--					</select>--%>
<%--				</div>--%>
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">申请时间：</div>
					<input id="reviewCreateDateStart" class="inputTxt dateinput" name="reviewCreateDateStart" value="${paramMap['reviewCreateDateStart']}" onClick="WdatePicker()" readonly/> 
					<span style="font-size:12px; font-family:'宋体';"> 至</span>
					<input id="reviewCreateDateEnd" class="inputTxt dateinput" name="reviewCreateDateEnd" value="${paramMap['reviewCreateDateEnd']}" onClick="WdatePicker()" readonly/>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">渠道：</div>
					<select name="agent" id="agent">
						<option value="">不限</option>
						<c:if test="${not empty fns:getAgentList() }">
							<c:forEach items="${fns:getAgentList()}" var="agentinfo">
								<option value="${agentinfo.id }" <c:if test="${agentinfo.id eq paramMap['o.agentinfo_id'] }">selected="selected"</c:if> >${agentinfo.agentName}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">销售：</div>
					<select name="saler" id="saler">
						<option value="">不限</option>
						<c:forEach items="${fns:getVisaSaleUserList('1')}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }" <c:if test="${userinfo.id eq paramMap['o.create_by']}">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co2">
					<label class="activitylist_team_co3_text">返佣差额：</label>
					<input id="rebatesAmountStrat" name="rebatesAmountStrat" style="width: 113px;" value="${paramMap['rebatesAmountStrat']}" onkeyup="validNum(this)" onafterpaste="validNum(this)" /> 至 
					<input id="rebatesAmountEnd"   name="rebatesAmountEnd"   style="width: 113px;" value="${paramMap['rebatesAmountEnd']}"   onkeyup="validNum(this)" onafterpaste="validNum(this)" />
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">计调：</div>
					<select name="op" id="op">
						<option value="">不限</option>
						<c:forEach items="${fns:getVisaSaleUserList('3')}" var="userinfo">
							<!-- 用户类型  3 代表计调 -->
							<option value="${userinfo.id }" <c:if test="${userinfo.id eq paramMap['p.createBy']}">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">打印状态：</label>
					<select id="printFlag" name="printFlag">
						<option value="">全部</option>
						<option value="1" <c:if test="${paramMap['r.printFlag'] eq '1'}">selected="selected"</c:if>>已打印</option>
						<option value="0" <c:if test="${paramMap['r.printFlag'] eq '0'}">selected="selected"</c:if>>未打印</option>
					</select>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">订单编号：</div>
					<input style="width:100px" id="order_no" name="order_no" value="${paramMap['o.order_no']}" type="text" />
				</div>
				
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">下单人：</label>
					<select id="createByName" name=createByName  >
				   			<option value="">不限</option>
								<c:forEach items="${createByList}" var="createByIdandName1">
									<option value="${createByIdandName1.id}"  <c:if test="${paramMap['createByName'] eq createByIdandName1.id}"> selected="selected" </c:if>  >
									${createByIdandName1.name}</option>
								</c:forEach>
					</select> 
				</div>
				
				
			</div>
			<div class="kong"></div>
		</div>
		
	</form>
	<div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            	<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
            	<li class="activitylist_paixu_left_biankuang lir.id"><a onClick="sortby('r.id',this)">创建时间</a></li>
            	<li class="activitylist_paixu_left_biankuang lir.updateDate"><a onClick="sortby('r.updateDate',this)">更新时间</a></li>
             </ul>
      	</div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
    </div>
	<div class="supplierLine">
		<a href="${ctx}/review/visaRebates/financeList?reviewStatus=8&jobId=${jobId}" <c:if test="${param.reviewStatus eq 8}">class="select" </c:if>" >全部</a>
		<a href="${ctx}/review/visaRebates/financeList?reviewStatus=1&jobId=${jobId}" <c:if test="${param.reviewStatus eq 1 or empty param.reviewStatus}">class="select" </c:if> >待本人审核</a>
		<a href="${ctx}/review/visaRebates/financeList?reviewStatus=0&jobId=${jobId}" <c:if test="${param.reviewStatus eq '0'}">class="select" </c:if> >未通过</a>
		<a href="${ctx}/review/visaRebates/financeList?reviewStatus=666&jobId=${jobId}" <c:if test="${param.reviewStatus eq 666}">class="select" </c:if> >审核中</a>
		<a href="${ctx}/review/visaRebates/financeList?reviewStatus=2&jobId=${jobId}" <c:if test="${param.reviewStatus eq 2}">class="select" </c:if> >已通过</a>
		<a href="${ctx}/review/visaRebates/financeList?reviewStatus=4&jobId=${jobId}" <c:if test="${param.reviewStatus eq 4}">class="select" </c:if> >已取消</a>
	</div>
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<th width="4%">序号</th>
			<th width="5%">申请日期</th>
			<th width="6%">订单号</th>
			<th width="11%">
				<span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span>
			</th>
			<th width="5%">渠道</th>
			<th width="7%">订单总额</th>
			<th width="7%">已付金额<br/>到账金额</th>
			<th width="5%">款项</th>
			<th width="4%">计调</th>
			<th width="6%">
			<span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span>
			</th>
			<th width="7%">返佣差额</th>
			<th width="5%">
				上一环节<br>审批人
			</th>
			<th width="5%">打印状态</th>
			<th width="7%">审核状态</th>
			<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="page" varStatus="s">
				<tr>
					<td>
					<!-- 王新伟added 201808-25 签证批量返佣审核相关  结束 -->
					<c:if test="${param.reviewStatus eq 1 or empty param.reviewStatus}"> 
                    	 <input type="checkbox" value="${page.nowLevel}@${page.reviewId}" name="activityId" onclick="t_allchk();" />
                    </c:if>
					${s.count}
					</td>
					<td class="tc">
						${fn:substring(page.reviewDate,0,10)}
					</td>
					<td>${page.order_no}</td>
					<td>
						<div class="tuanhao_cen onshow">${page.groupCode}</div>
							<div class="chanpin_cen qtip" title="${page.productName}">
							<a target="_blank" href="${ctx}/visa/visaProducts/visaProductsDetail/${page.productId}">${page.productName}</a>
						</div>
					</td>
					<td>${page.agentName }</td>
					<td class="tr pr">
						<c:if test="${page.yujiRebates != '¥ 0'}">
							<span class="icon-rebate">
								<span>
									预计返佣 ${page.yujiRebates }<br>
								</span>
							</span>
						</c:if>
						<span class="fbold">
							${fns:getMoneyAmountBySerialNum(page.total_money,2)}
						</span>
					</td>
					<td class="p0 tr">
						<div class="yfje_dd">
							<span class="fbold">
								${fns:getMoneyAmountBySerialNum(page.payed_money,2)}
							</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">
								${fns:getMoneyAmountBySerialNum(page.accounted_money,2)}
							</span>
						</div>
					</td>
					<td>签证返佣</td>
					<td>${fns:getUserById(page.OPId).name}</td>
					
					<td>
							<span class="order-saler onshow">${page.salerName}</span><span class="order-picker">${fns:getUserById(page.salerId).name}</span>
					</td>
					
					
					
					
					<td class="tr">
						<span class="fbold tdorange">${page.totalrebatesamount}</span>
					</td>
					<td>${page.reviewPersonName}</td>
					<c:choose>
						<c:when test="${page.isPrintFlag == '1'}">
							<td class="invoice_yes"><p class="uiPrint">
									已打印<span style="display: none;">首次打印日期<br>
									<fmt:formatDate pattern="yyyy/MM/dd HH:mm"
											value="${page.printTime}" /></span>
								</p></td>
						</c:when>
						<c:otherwise>
							<td>未打印</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${page.result eq 0}">
							<td>已驳回</td>
						</c:when>
						<c:when test="${page.result eq 1}">
							<td>${fns:getNextReview(page.reviewId) }</td>
						</c:when>
						<c:when test="${page.result eq 2 or page.result eq 3}">
							<td>已通过</td>
						</c:when>
						<c:when test="${page.result eq 4}">
							<td>已取消</td>
						</c:when>
						<c:otherwise>
							<td>-</td>
						</c:otherwise>
					</c:choose>
					<td>
						<dl class="handle">
							<dt>
								<img src="${ctxStatic}/images/handle_cz.png" title="操作">
							</dt>
							<dd class="">
								<p>
									<span></span>
									<a  target="_blank" href="${ctx}/visa/order/visaRebatesDetail?reviewId=${page.reviewId}&orderId=${page.orderid}">查看</a>
									<c:if test="${page.result eq 1 and page.jobLevel eq page.nowLevel}">
										<br>
										<a href="${ctx}/visa/order/visaRebatesDetail?reviewId=${page.reviewId}&orderId=${page.orderid}&reviewFlag=1&nowLevel=${page.nowLevel}&shenfen=caiwu">审批</a>
									</c:if>
									<a href="${ctx}/cost/manager/forcastList/${page.productId}/6" target="_blank">预报单</a>
									<a href="${ctx}/cost/manager/settleList/${page.productId}/6" target="_blank">结算单</a>
									<!-- 审核通过后，配置有打印权限的才可以进行打印操作  -->
									<c:if test="${page.result eq 2}">
										<shiro:hasPermission name="rebatesReview:print:down">
											<br>
											<a  target="_blank"  href="${ctx}/review/visaRebates/visaRebatesReviewPrint?reviewId=${page.reviewId}&orderId=${page.orderid}">打印</a>
										</shiro:hasPermission>	
									</c:if>
								
								    <!-- 处于审核中的才显示   撤销按钮 -->
									<c:if test="${page.nowLevel-page.jobLevel==1 && page.result == '1'}">
                                           <a onclick="reviewGoback('${page.reviewId}');" >撤销</a><br />
                                    </c:if>
									
								</p>
							</dd>
						</dl>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(page.list) eq 0 }">
				<tr class="toptr">
					<td style="text-align: center;" colspan="15"> 暂无搜索结果 </td>
				</tr>
			</c:if>
			<c:if test="${param.reviewStatus eq 1 or empty param.reviewStatus}"> 
                 <tr class="checkalltd">
					<td colspan='15' class="tl">
						<label>
							<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
						</label>
						<label>
							<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
						</label>
						<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:multiReviewVisaRebates('${ctx}',1,1,this);" >批量审批</a>
					</td>
				</tr>
			</c:if> 
		</tbody>
	</table>
	<div class="page">
		<div class="pagination">
		<div class="endPage">${page}</div>
		<div style="clear:both;"></div>
		</div>	
	</div>
</body>
</html>