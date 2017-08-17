<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审批优化-审批-借款审批-列表</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//团号和产品切换
		switchNumAndPro();
		//产品名称文本框提示信息
		inputTips();
		
	 	//取消一个checkbox 就要联动取消 全选的选中状态
	    $("input[name='ids']").click(function(){
	        if($(this).attr("checked")){
	
	        }else{
	            $("input[name='allChk']").removeAttr("checked");
	        }
	    });
	
		  var resetSearchParams = function(){
		            $(':input','#searchForm')
		                    .not(':button, :submit, :reset, :hidden')
		                    .val('')
		                    .removeAttr('checked')
		                    .removeAttr('selected');
		            $('#country').val("");
		        }
		    $('#contentTable').on('change','input[name="ids"]',function(){
		        if( $('#contentTable input[name="ids"]').length == $('#contentTable input[name="ids"]:checked').length ){
		            $('input[name="allChk" ]').attr('checked',true);
		        }else{
		            $('input[name="allChk" ]').removeAttr('checked');
		        }
		    });
		    
		    //判断是否展开筛选
		    var openFlag = false;

			if($("select[name=agent]").val() != '' || $("input[name=applyDateFrom]").val() != '' || $("input[name=applyDateTo]").val() != '' || $("select[name=applyPerson]").val() != '' || $("select[name=saler]").val() != '') {
				openFlag = true;
			}
			if($("select[name=reviewStatus]").val() != '' || $("select[name=cashConfirm]").val() != '' || $("select[name=printStatus]").val() != '' || $("input[name=minBorrowMoney]").val() != '' || $("input[name=maxBorrowMoney]").val() != '') {
				openFlag = true;
			}
			if(openFlag) {
				$("a.zksx").click();
			}
			
			updateOrderStyle($("#orderBy").val());
		});

	//展开收起
	function expand(child, obj) {
		if($(child).is(":hidden")){
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");
	
		}else{
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
	
		}
	}
	
	function checkall(obj){
	    if($(obj).attr("checked")){
	        $('#contentTable input[name="ids"]').attr("checked",'true');
	        $("input[name='allChk']").attr("checked",'true');
	    }else{
	        $('#contentTable input[name="ids"]').removeAttr("checked");
	        $("input[name='allChk']").removeAttr("checked");
	    }
	}

	function checkreverse(obj){
	    var $contentTable = $('#contentTable');
	    $contentTable.find('input[name="ids"]').each(function(){
	        var $checkbox = $(this);
	        if($checkbox.is(':checked')){
	            $checkbox.removeAttr('checked');
	        }else{
	            $checkbox.attr('checked',true);
	        }
	    });
	}
	
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
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
    
    //选择审核状态
    function selectTabStatus(obj, tabStatus) {
    	$("a[name=tabStatus]").attr("class", "");
    	$(obj).attr("class", "selected");
    	$("#tabStatus").val(tabStatus);
    	$("#searchForm").submit();
    }
    
  //按批次取消审核
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
			          url:g_context_url+"/visa/xxz/borrowmoney/visaBorrowMoneyCancelAjaxNew",
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
    
</script>
<style type="text/css">
	.text-more-new .activitylist_team_co3_text,.text-more-new .activitylist_bodyer_right_team_co2 label{
		width:90px;	
		text-align:right;	
		}
	.text-more-new .activitylist_bodyer_right_team_co1,.text-more-new .activitylist_bodyer_right_team_co3{
		min-width:200px;		
		}
	.text-more-new .activitylist_bodyer_right_team_co2{
		min-width:400px;		
		}
</style>
</head>
<body>
	<page:applyDecorator name="borrowing_review_head">
          <page:param name="current"><c:choose><c:when test="${orderType==1}">single</c:when><c:when test="${orderType==2}">loose</c:when><c:when test="${orderType==3}">study</c:when><c:when test="${orderType==5}">free</c:when><c:when test="${orderType==4}">bigCustomer</c:when><c:when test="${orderType==6}">visa</c:when><c:when test="${orderType==7}">airticket</c:when><c:when test="${orderType==10}">cruise</c:when></c:choose></page:param>
    </page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<form id="searchForm" action="${ctx}/visa/xxz/borrowmoney/visaXXZBorrowMoneyNewReviewList" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" /> 
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<input type="hidden" name="orderBy" id="orderBy" value="${formBean.orderBy }" />
			<input type="hidden" name="ascOrDesc" id="ascOrDesc" value="${formBean.ascOrDesc }" />
			<input type="hidden" name="tabStatus" id="tabStatus" value="${formBean.tabStatus }">
			
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr wpr20">
					<label>搜索：</label><input style="width:260px" class="inputTxt inputTxtlong" flag="istips" id="searchLike" name="searchLike" value="${formBean.searchLike }" type="text" /> 
					<span class="ipt-tips">团号/产品名称/订单号</span>
				</div>
				<div class="form_submit">
					<input class="btn btn-primary" value="搜索" type="submit">
				</div>
				<a class="zksx">展开筛选</a>
				<div class="ydxbd text-more-new">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">产品类型：</div>
						<select name="productName">
							<option value="">单团</option>
							<option value="">散拼</option>
							<option value="">游学</option>
							<option value="">大客户</option>
							<option value="">自由行</option>
							<option value="" selected="selected">签证</option>
							<option value="">机票</option>
							<option value="">全部产品</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">渠道商：</div>
						<select name="agent">
							<option value="">不限</option>
							<c:if test="${not empty fns:getAgentList() }">
								<c:forEach items="${fns:getAgentList()}" var="agentinfo">
									<option value="${agentinfo.id }" <c:if test="${formBean.agent==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option>
								</c:forEach>
							</c:if>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co2">
						<label>申请日期：</label> <input id="" class="inputTxt dateinput" name="applyDateFrom" value="${formBean.applyDateFrom }" onclick="WdatePicker()" readonly="readonly" />
						<span> 至 </span> <input id="" class="inputTxt dateinput" name="applyDateTo" value="${formBean.applyDateTo }" onclick="WdatePicker()" readonly="readonly" />
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co3">
						<div class="activitylist_team_co3_text">审批发起人：</div>
						<select name="applyPerson">
							<option value="">不限</option>
							<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
								<!-- 用户类型  1 代表销售 -->
								<option value="${userinfo.id }"
									<c:if test="${formBean.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">销售：</div>
						<select name="saler">
							<option value="">不限</option>
							<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
								<!-- 用户类型  1 代表销售 -->
								<option value="${userinfo.id }" <c:if test="${formBean.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">审批状态：</div>
						<select name="reviewStatus">
							<option value="">全部</option>
							<option value="1" <c:if test="${formBean.reviewStatus == 1 }">selected="selected"</c:if>>审批中</option>
							<option value="2" <c:if test="${formBean.reviewStatus == 2 }">selected="selected"</c:if>>已通过</option>
							<option value="0" <c:if test="${formBean.reviewStatus == 0 }">selected="selected"</c:if>>未通过</option>
						</select>
					</div>

					<div class="kong"></div>

					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">出纳确认：</div>
						<select name="cashConfirm">
							<option value="">全部</option>
							<option value="0" <c:if test="${formBean.cashConfirm == 0 }">selected="selected"</c:if>>已付</option>
							<option value="1" <c:if test="${formBean.cashConfirm == 1 }">selected="selected"</c:if>>未付</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">打印状态：</div>
						<select name="printStatus">
							<option value="">全部</option>
							<option value="0" <c:if test="${formBean.printStatus == 0 }">selected="selected"</c:if>>已打印</option>
							<option value="1" <c:if test="${formBean.printStatus == 1 }">selected="selected"</c:if>>未打印</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co2">
						<label>借款金额：</label> <input type="text" name="minBorrowMoney" value="${formBean.minBorrowMoney }"/>
						<span> 至 </span> <input type="text" name="maxBorrowMoney" value="${formBean.maxBorrowMoney }"/>
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
		<!--状态开始-->
		<div class="supplierLine">
			<a name="tabStatus" href="javascript:void(0)" <c:if test="${formBean.tabStatus == 0 }">class="select"</c:if> onclick="selectTabStatus(this,0);">全部</a> 
			<a name="tabStatus" href="javascript:void(0)" <c:if test="${formBean.tabStatus == 1 }">class="select"</c:if> onclick="selectTabStatus(this,1);">待本人审核</a> 
			<a name="tabStatus" href="javascript:void(0)" <c:if test="${formBean.tabStatus == 2 }">class="select"</c:if> onclick="selectTabStatus(this,2);">本人已审核</a> 
			<a name="tabStatus" href="javascript:void(0)" <c:if test="${formBean.tabStatus == 3 }">class="select"</c:if> onclick="selectTabStatus(this,3);">非本人审核</a>
		</div>

		<!--状态结束-->
		<table id="contentTable" class="table activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="4%">序号</th>
					<th width="8%">订单号</th>
					<th width="8%">
						<span class="tuanhao on">团号</span> / <span class="chanpin">产品名称</span>
					</th>
					<th width="5%">产品类型</th>
					<th width="5%">申请时间</th>
					<th width="7%">审批发起人</th>
					<th width="6%">渠道商</th>
					<th width="6%">销售</th>
					<th width="6%" class="tr">订单金额</th>
					<th width="6%" class="tr">已付金额<br />到账金额</th>
					<th width="6%">款项名称</th>
					<th width="7%">借款金额</th>
					<th width="6%">上一环节审批人</th>
					<th width="7%">审批状态</th>
					<th width="7%">出纳确认</th>
					<th width="6%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list }" var="reviewInfo" varStatus="status">
				<tr>
					<td><input type="checkbox" name="ids" value="${reviewInfo.reviewid }" /> ${status.count }</td>
					<td>${reviewInfo.orderno}</td>
					<td>
						<div title="4535DFDSF" class="tuanhao_cen onshow"> ${reviewInfo.groupcode}</div>
						<div title="${reviewInfo.productname}" class="chanpin_cen qtip">
							<a href="#" target="_blank">${reviewInfo.productname}</a>
						</div>
					</td>
					<td class="tc">签证</td>
					<td class="p0">
						<div class="out-date"><fmt:formatDate value="${reviewInfo.createdate}" pattern="yyyy-MM-dd"/></div>
						<div class="close-date time"><fmt:formatDate value="${reviewInfo.createdate}" pattern="HH:mm:ss"/></div>
					</td>
					<td class="tc">${fns:getUserNameById(reviewInfo.createby)}</td>
					<td class="tc">${reviewInfo.agentName}</td>
					<td class="tc">${fns:getUserNameById(reviewInfo.saler)}</td>
					<td class="tr"><span class="fbold tdred">${fns:getMoneyAmountBySerialNum(reviewInfo.totalMoney,2)}</span>起</td>
					<td class="tr">
						<div class="yfje_dd">
							<span class="fbold">${reviewInfo.payedMoneyStr }</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">${reviewInfo.accountedMoneyStr }</span>
						</div>
					</td>
					<td class="tr">签证借款</td>
					<td class="tr"><span class="fbold tdorange">人民币${reviewInfo.amount}</span>
					</td>
					<td class="tc">${fns:getUserNameById(reviewInfo.lastreviewer)}</td>
					<td class="invoice_yes tc">${fns:getChineseReviewStatus(reviewInfo.status,reviewInfo.currentReviewer)}</td>
					<td class="invoice_no tc">
						<c:if test="${reviewInfo.paystatus == true}">
                          	已付款
                        </c:if>
                        <c:if test="${reviewInfo.paystatus == false}">
                         	未付款
                        </c:if>
                    </td>
					<td class="p0"><dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
							</dt>
							<dd class="">
								<p>
									<span></span> 
									<a href="${ctx }/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?orderId=${reviewInfo.orderid }&reviewId=${reviewInfo.reviewid}&flag=1" target="_blank">查看</a>
									<c:if test="${reviewInfo.status eq '1'}">
										<a href="${ctx }/visa/xxz/borrowmoney/visaBorrowMoney4XXZReviewDetail?orderId=${reviewInfo.orderid }&reviewId=${reviewInfo.reviewid}&flag=0">审批</a> 
									</c:if>
									<a href="javascript:void(0)">预报单</a> 
									<a href="javascript:void(0)">结算单</a>
									<!-- 处于审核中的才显示   撤销按钮 -->
									<c:if test="${reviewInfo.isBackReview == true}">
                                        <a onclick="reviewGoback('${reviewInfo.reviewid}');" >撤销</a><br />
                                    </c:if>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
				</c:forEach>
				<tr class="checkalltd">
					<td colspan='17' class="tl">
						<label>
							<input type="checkbox" name="allChk" onclick="checkall(this)">全选
						</label>
						<label>
							<input type="checkbox" name="reverseChk" onclick="checkreverse(this)">反选
						</label>
						<a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="pagination clearFix">${page}</div>
		<!--右侧内容部分结束-->
	</div>
	<!--S批量审核操作弹出层-->

<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
  <table width="100%" style="padding:10px !important; border-collapse: separate;">
    <tr>
      <td> </td>
    </tr>
    <tr>
      <td><p>您好，当前您提交了23个审核项目，是否执行批量操作？</p></td>
    </tr>
    <tr>
      <td><p>备注：</p></td>
    </tr>
    <tr>
      <td><label>
          <textarea name="textfield" id="textfield" style="width: 290px;"></textarea>
        </label></td>
    </tr>
  </table>
</div>

<!--S批量审核操作弹出层-->
</body>
</html>
