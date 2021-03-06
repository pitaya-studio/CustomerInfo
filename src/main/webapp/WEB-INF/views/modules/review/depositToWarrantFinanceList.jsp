<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证押金转担保</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

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
    
//    renderSelects(selectQuery());
    selectQuery();
    
    $("#op").comboboxInquiry();
    $("#createByName").comboboxInquiry();
	
});

function selectQuery() {
	$("#agent").comboboxInquiry();
    $("#saler").comboboxInquiry();
}

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
	$("#searchForm").attr("action","${ctx}/review/deposit/financeList");
	$("#searchForm").submit();
}
function chooseStatus(reviewStatus) {
	$("#reviewStatus").val(reviewStatus);
	$("#searchForm").submit();
};
function chooseRole(rid){
	$("#jobId").val(rid);
	$("#searchForm").submit();
}

//某审核环节还没进行审核的可被上一环节审核人取回
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
		          url:g_context_url+"/review/deposit/depositToWarrantReviewCancelAjax",
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
function multiReviewDepositToWarrant(ctx,orderid,agentid,obj){
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
			          url:ctx+"/review/deposit/multiReviewDepositToWarrant",
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
	<form id="searchForm" method="post" action="${ctx}/review/deposit/financeList">
		<%--<div class="message_box">--%>
	   		<%--<c:forEach items="${userJobList}" var="job">--%>
	   			<%--<div class="message_box_li">--%>
		   			<%--<c:choose>--%>
		   				<%--<c:when test="${jobId eq job.id}">--%>
		   					<%--<a>--%>
			   					<%--<div class="message_box_li_a curret">--%>
									<%--<span>--%>
										<%--${fns:abbrs(job.deptName,job.jobName,40)}--%>
									<%--</span>--%>
									<%--<c:if test ="${job.count gt 0}">--%>
										<%--<div class="message_box_li_em" >--%>
										<%--${job.count}								--%>
										<%--</div>--%>
									<%--</c:if>--%>
									<%--<c:if test="${fns:getStringLength(job.deptName,job.jobName) gt 37}">--%>
										<%--<div class="message_box_li_hover">--%>
											<%--${job.deptName }_${job.jobName }--%>
										<%--</div>--%>
									<%--</c:if>--%>
								<%--</div>--%>
		   					<%--</a>--%>
		   				<%--</c:when>--%>
		   				<%--<c:otherwise>--%>
			   				<%--<a onClick="chooseRole('${job.id}');">--%>
			   					<%--<div class="message_box_li_a">--%>
									<%--<span>${fns:abbrs(job.deptName,job.jobName,40)}</span>--%>
									<%--<c:if test ="${job.count gt 0}">--%>
										<%--<div class="message_box_li_em" >--%>
											<%--${job.count}					--%>
										<%--</div>--%>
									<%--</c:if>--%>
									<%--<c:if test="${fns:getStringLength(job.deptName,job.jobName) gt 37}">--%>
										<%--<div class="message_box_li_hover">--%>
											<%--${job.deptName }_${job.jobName }--%>
										<%--</div>--%>
									<%--</c:if>--%>
								<%--</div>--%>
			   				<%--</a>--%>
		   				<%--</c:otherwise>				   			--%>
		   			<%--</c:choose>--%>
				<%--</div>--%>
			<%--</c:forEach>--%>
	    <%--</div>--%>
		<input id="reviewStatus" name="reviewStatus" type="hidden" value="${paramMap.reviewStatus}" />
		<input type="hidden" value="${jobId}" name="jobId" id="jobId"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<input id="groupCode" class="txtPro inputTxt searchInput" type="text"
					   value="${paramMap['p.groupCode']}" name="groupCode" placeholder="请输入团号">
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" type="submit" value="搜索">
			</div>
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
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">订单号：</label>
					<input value="${paramMap['o.order_no']}" class="inputTxt" name="visaOrderNo" id="visaOrderNo"> 
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">游客：</label>
					<input value="${paramMap['t.name']}" class="inputTxt inputTxtlong" name="travelerName" id="travelerName">
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">报批时间：</label>
					<input id="reviewCreateDateStart" class="inputTxt dateinput" name="reviewCreateDateStart" value="${paramMap['reviewCreateDateStart']}" onClick="WdatePicker()" readonly/> 
					<span style="font-size:12px; font-family:'宋体';"> 至</span>
					<input id="reviewCreateDateEnd" class="inputTxt dateinput" name="reviewCreateDateEnd" value="${paramMap['reviewCreateDateEnd']}" onClick="WdatePicker()" readonly/>
				</div>
				<%-- <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
						<select name="paymentType">
							<option value="">不限</option>
							<c:forEach var="pType" items="${fns:findAllPaymentType()}">
								<option value="${pType[0] }"
									<c:if test="${paramMap.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
							</c:forEach>
						</select>
					</div> --%>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">渠道选择：</label>
					<select name="agent" id="agent" >
						<option value="">全部</option>
						<c:if test="${not empty fns:getAgentList() }">
							<c:forEach items="${fns:getAgentList()}" var="agentinfo">
								<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
								<c:choose>
								   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}"> 
								      <option value="${agentinfo.id}" <c:if test="${agentinfo.id eq paramMap['o.agentinfo_id'] }">selected="selected"</c:if> >直客</option>
								   </c:when>
								   <c:otherwise>
								       <option value="${agentinfo.id}" <c:if test="${agentinfo.id eq paramMap['o.agentinfo_id'] }">selected="selected"</c:if> >${agentinfo.agentName}</option>
								   </c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">销售：</label>
					<select name="saler" id="saler" >
						<option value="">不限</option>
						<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
							<!-- 用户类型  1 代表销售 -->
							<option value="${userinfo.id }" <c:if test="${userinfo.id eq paramMap['o.create_by']}">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
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
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">下单人：</div>
					<select id="createByName" name="createByName">
					<option value="">不限</option>
						<c:forEach items="${createByList}" var="createByIdandName1">
							<option value="${createByIdandName1.id}" <c:if test="${paramMap['createByName'] eq createByIdandName1.id}">selected="selected"</c:if>>${createByIdandName1.name}</option>
						</c:forEach>
					</select> 
			</div>
			</div>
			
			<div class="kong"></div>
		</div>
	</form>
	<div class="supplierLine">
		<a href="${ctx}/review/deposit/financeList?reviewStatus=8&jobId=${jobId}" <c:if test="${param.reviewStatus eq 8}">class="select" </c:if>" >全部</a>
		<a href="${ctx}/review/deposit/financeList?reviewStatus=1&jobId=${jobId}" <c:if test="${param.reviewStatus eq 1 or empty param.reviewStatus}">class="select" </c:if> >待本人审核</a>
		<a href="${ctx}/review/deposit/financeList?reviewStatus=0&jobId=${jobId}" <c:if test="${param.reviewStatus eq 0}">class="select" </c:if> >未通过</a>
		<a href="${ctx}/review/deposit/financeList?reviewStatus=666&jobId=${jobId}" <c:if test="${param.reviewStatus eq 666}">class="select" </c:if> >审核中</a>
		<a href="${ctx}/review/deposit/financeList?reviewStatus=2&jobId=${jobId}" <c:if test="${param.reviewStatus eq 2}">class="select" </c:if> >已通过</a>
		<a href="${ctx}/review/deposit/financeList?reviewStatus=4&jobId=${jobId}" <c:if test="${param.reviewStatus eq 4}">class="select" </c:if> >已取消</a>
	</div>
	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li class="activitylist_paixu_left_biankuang lir.id"><a onClick="sortby('r.id',this)">创建时间</a></li>
					<li class="activitylist_paixu_left_biankuang lir.updateDate"><a onClick="sortby('r.updateDate',this)">更新时间</a></li>
				</ul>
			</div>
			<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
			<div class="kong"></div>
		</div>
	</div>

	<table id="contentTable" class="table activitylist_bodyer_table mainTable">
		<thead>
			<th width="4%">序号</th>
			<th width="7%">报批日期</th>
			<th width="7%">订单号</th>
			<th width="9%">
				<span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span>
			</th>
			<th width="5%">计调</th>
			<th width="6%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
			<th width="5%">游客</th>
			<th width="7%">下单时间</th>
			<th width="7%">订单总额</th>
			<th width="7%">已付金额<br>到账金额</th>
			<th width="10%">担保金额</th>
			<th width="7%">
				上一环节<br>审批人
			</th>
			<th width="9%">原因备注</th>
			<th width="6%">审核状态</th>
			<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="page" varStatus="s">
				<tr>
					<td>
					    <!-- 签证押金转担保  选择多项同时审核  开始  wxw added -->
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
					<td>${fns:getUserById(page.OPId).name}</td>
					<td><span class="order-saler onshow">${page.salerName}</span><span class="order-picker">${fns:getUserById(page.salerId).name}</span></td>
					<td>${page.travelerName}</td>
					<td class="tc">
						${fn:substring(page.create_date,0,10)}
					</td>
					<td class="tr-payable">
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
					<td class="tr">
						<span class="fbold tdorange">
							${page.priceCurrency}${page.price}
						</span>
					</td>
					<td>${page.reviewPersonName}</td>
					<td>${page.createReason}</td>
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
									<a href="${ctx}/review/deposit/reviewForm?reviewId=${page.reviewId}">查看</a>
									<c:if test="${page.result eq 1 and page.jobLevel eq page.nowLevel}">
										<br>
										<a href="${ctx}/review/deposit/reviewForm?reviewId=${page.reviewId}&nowLevel=${page.nowLevel}&shenfen=caiwu">审批</a>
									</c:if>
									
									 <!-- 处于审核中的才显示   撤销按钮 -->
									<c:if test="${page.nowLevel-page.jobLevel==1 && page.result == '1'}">
									<br />
                                           <a onclick="reviewGoback('${page.reviewId}');" >撤销</a>
                                    </c:if>
                                    
								</p>
							</dd>
						</dl>
					</td>
				</tr>
			</c:forEach>
			<!-- 签证押金转担保  选择多项同时审核  开始  wxw added -->
		</tbody>
	</table>


	<div class="page">
		<c:if test="${param.reviewStatus eq 1 or empty param.reviewStatus}">
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
</body>
</html>