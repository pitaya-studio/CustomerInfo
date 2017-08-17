<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<title>审批优化-审批-返佣审批-列表</title>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
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
    $('#contentTable').on('change','input[type="checkbox"]',function(){
        if( $('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length ){
            $('[name="allChk" ]').attr('checked',true);
        }else{
            $('[name="allChk" ]').removeAttr('checked');
        }
    });
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
    });
}
//分页
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}
//排序
function cantuansortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
        }else{
            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
        }
    }else{
        sortBy = sortBy+" DESC";
    }
    $("#searchForm").attr("action","${ctx}/order/newcomdiscountApprove/getComdiscountList");
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
//
function statusChoose(statusNum) {
	$("#tabStatus").val(statusNum);
	$("#searchForm").submit();
};
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
            	<!--右侧内容部分开始-->
                <form id="searchForm" action="" method="post">
                <input id="orderBy" name="orderBy" value="${formInput.orderBy}" type="hidden">
                <input id="pageNo" name="pageNo" value="${pageNo}" type="hidden"> 
				<input id="pageSize" name="pageSize" value="${pageSize}" type="hidden">
				<input id="tabStatus" name="tabStatus" value="${formInput.tabStatus}" type="hidden"> 
                	<div class="activitylist_bodyer_right_team_co">
                    	<div class="activitylist_bodyer_right_team_co2 pr wpr20">
                        	<label>搜索：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="${formInput.wholeSalerKey}" type="text" />
                        </div>
                        <div class="form_submit">
                        	<input class="btn btn-primary" value="搜索" type="submit">
                        </div>
                        <a class="zksx">展开筛选</a>
                        <div class="ydxbd text-more-new">
                            <div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">产品类型：</div>
                              <select name="productType" id="productType">
                                 <c:forEach var="order" items="${fns:getDictList('order_type')}">
									<option value="${order.value }"
										<c:if test="${formInput.productType==order.value}">selected="selected"</c:if>>${order.label}
									</option>
								</c:forEach>
                              </select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">渠道商：</div>
                             <select name="agent" id="agent">
                               <option value="" selected="selected">不限</option>
								<c:if test="${not empty fns:getAgentList() }">
									<c:forEach items="${fns:getAgentList()}" var="agentinfo">
										<option value="${agentinfo.id }"
											<c:if test="${formInput.agent==agentinfo.id }">selected="selected"</c:if>>
											${agentinfo.agentName}</option>
									</c:forEach>
								</c:if>
                            </select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co2">
                                <label>申请日期：</label>
                                <input id="approveDateFrom" class="inputTxt dateinput" name="approveDateFrom" value="" onclick="WdatePicker()" readonly="readonly" />
                                <span> 至 </span>
                                <input id="approveDateTo" class="inputTxt dateinput" name="approveDateTo" value="" onclick="WdatePicker()" readonly="readonly" />
                            </div>
                            <div class="kong"></div>
                            <div class="activitylist_bodyer_right_team_co3">
                               <div class="activitylist_team_co3_text">审批发起人：</div>
                               <select name="createBy" id="createBy">
	                                <option value="" selected="selected">不限</option>
									<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
										<!-- 用户类型  1 代表销售 -->
										<option value="${userinfo.id }"
											<c:if test="${formInput.createBy==userinfo.id }">selected="selected"</c:if>>${userinfo.name}
										</option>
									</c:forEach>
                                </select>
                            </div>
                             <div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">计调：</div>
	                              <select name="operator" id="operator">
	                                    <option value="" selected="selected">不限</option>
										<c:forEach items="${fns:getSaleUserList('3')}" var="userinfo">
											<!-- 用户类型3 代表计调 -->
											<option value="${userinfo.id }"
												<c:if test="${formInput.operator==userinfo.id }">selected="selected"</c:if>>${userinfo.name}
											</option>
										</c:forEach>
	                              </select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <div class="activitylist_team_co3_text">审批状态：</div>
	                              <select name="status" id="status">
	                                <option value="" selected="selected">全部</option>
									<option value="1"
										<c:if test="${formInput.status == 1 }">selected="selected"</c:if>>审批中</option>
									<option value="2"
										<c:if test="${formInput.status == 2 }">selected="selected"</c:if>>已通过</option>
									<option value="0"
									<c:if test="${formInput.status == 0 }">selected="selected"</c:if>>未通过</option>
	                              </select>
                            </div>
                            <div class="kong"></div>
                           <div class="activitylist_bodyer_right_team_co1">
	                              <div class="activitylist_team_co3_text">出纳确认：</div>
	                              <select name="payStatus" id="payStatus">
	                               <option value="" selected="selected">全部</option>
									<option value="0"
										<c:if test="${formInput.payStatus == 0 }">selected="selected"</c:if>>已付</option>
									<option value="1"
										<c:if test="${formInput.payStatus == 1 }">selected="selected"</c:if>>未付</option>
	                              </select>
                            </div>
                             <div class="activitylist_bodyer_right_team_co1">
                               <div class="activitylist_team_co3_text">打印状态：</div>
                               <select name="isPrinted" id="isPrinted">
	                               <option value="" selected="selected">全部</option>
									<option value="0"
										<c:if test="${formInput.isPrinted == 0 }">selected="selected"</c:if>>已打印</option>
									<option value="1"
										<c:if test="${formInput.isPrinted == 1 }">selected="selected"</c:if>>未打印</option>
                                </select>
                            </div>
                            <div class="activitylist_bodyer_right_team_co2">
                                <label>返佣金额：</label>
                                <input id="rebateAmountFrom" class="inputTxt" name="rebateAmountFrom" value=""  
                                onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');"  onafterpaste="this.value=this.value.replace(/\D/g,'');"/>
                                <span>至</span>
                                <input id="rebateAmountTo" class="inputTxt" name="rebateAmountTo" value="" 
                                onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');"  onafterpaste="this.value=this.value.replace(/\D/g,'');"/>
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
                            <!-- <li class="activitylist_paixu_left_biankuang liid"><a onclick="">创建时间</a></li>
                            <li class="activitylist_paixu_moren"><a onclick="">更新时间<i class="icon icon-arrow-down"></i></a></li> -->
                            <c:choose>
								<c:when test="${formInput.orderBy=='' || formInput.orderBy==null}">
									<li class="activitylist_paixu_left_biankuang liid">
									<a onclick="cantuansortby('r.create_date',this)">创建时间<i
										class=""></i></a></li>
									<li class="activitylist_paixu_moren">
									<a onclick="cantuansortby('r.update_date',this)">更新时间<i
										class=""></i></a></li>			
								</c:when>
								<c:when test="${formInput.orderBy=='r.create_date DESC'}">
									<li class="activitylist_paixu_left_biankuang liid">
									<a onclick="cantuansortby('r.create_date',this)">创建时间<i
										class="icon icon-arrow-down"></i></a></li>
									<li class="activitylist_paixu_moren">
									<a onclick="cantuansortby('r.update_date',this)">更新时间<i
										class=""></i></a></li>	
								</c:when>
								<c:when test="${formInput.orderBy=='r.create_date ASC'}">
									<li class="activitylist_paixu_left_biankuang liid">
									<a onclick="cantuansortby('r.create_date',this)">创建时间<i
										class="icon icon-arrow-up"></i></a></li>
									<li class="activitylist_paixu_moren">
									<a onclick="cantuansortby('r.update_date',this)">更新时间<i
										class=""></i></a></li>					
								</c:when>
								<c:when test="${formInput.orderBy=='r.update_date DESC'}">
									<li class="activitylist_paixu_left_biankuang liid">
									<a onclick="cantuansortby('r.create_date',this)">创建时间<i
										class=""></i></a></li>
									<li class="activitylist_paixu_moren">
									<a onclick="cantuansortby('r.update_date',this)">更新时间<i
										class="icon icon-arrow-down"></i></a></li>						
								</c:when>
								<c:when test="${formInput.orderBy=='r.update_date ASC'}">
									<li class="activitylist_paixu_left_biankuang liid">
									<a onclick="cantuansortby('r.create_date',this)">创建时间<i
										class=""></i></a></li>
									<li class="activitylist_paixu_moren">
									<a onclick="cantuansortby('r.update_date',this)">更新时间<i
										class="icon icon-arrow-up"></i></a></li>						
								</c:when>					
							</c:choose>
                        </ul>
                        </div>
                        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${count}</strong>&nbsp;条</div>
                        <div class="kong"></div>
                    </div>
                </div>
                <!--状态开始-->
                <div class="supplierLine">
                    <a <c:if test="${formInput.tabStatus==0 }">class="select"</c:if> onClick="statusChoose(0);">全部</a>
                    <a <c:if test="${formInput.tabStatus==1 }">class="select"</c:if> onClick="statusChoose(1);">待本人审核</a>
                    <a <c:if test="${formInput.tabStatus==2 }">class="select"</c:if> onClick="statusChoose(1);">本人已审核</a>
                    <a <c:if test="${formInput.tabStatus==3 }">class="select"</c:if> onClick="statusChoose(1);">非本人审核</a>
                </div>
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
                            <th width="6%">计调</th>
                            <th width="6%">订单金额</th>
                            <th width="6%">已付金额<br />到账金额</th>
                            <th width="6%">款项名称</th>
                            <th width="7%">返佣金额</th>
                            <th width="6%">上一环节审批人</th>
                            <th width="7%">审批状态</th>
                            <th width="7%">出纳确认</th>
                            <th width="6%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty page.list}">
							<tr class="toptr">
								<td colspan="16" style="text-align: center;">暂无相关信息</td>
							</tr>
				       	</c:if>
				     <c:forEach items="${page.list }" var="record" varStatus="v">  	
                        <tr>
                            <td><input type="checkbox" name="ids" value="${record.id}" />
                                ${v.index+1}
                            </td>
                            <td>
                            	${record.orderno}
                            </td>
                            <td>
                                <div title="${record.groupcode}" class="tuanhao_cen onshow">${record.groupcode}</div>
                                <div title="${record.productname}" class="chanpin_cen qtip"> <a href="#" target="_blank">${record.productname}</a></div>
                            </td>
                            <td class="tc">${record.producttype}</td>
                            <td class="p0"><div class="out-date">${record.createdate}</div>
                                <div class="close-date time">${record.createtime}</div></td>
                            <td class="tc">张三${record.rreateby}</td>
                            <td class="tc">渠道商${record.agent}</td>
                            <td class="tc">计调${record.operator}</td>
                            <td class="tr">¥<span class="fbold tdred">900</span>起${record.totalmoney}</td>
                            <td class="tr">
				        		<div class="yfje_dd">
									<span class="fbold">¥1,000.00${record.payedmoney}</span>
								</div>
				        		<div class="dzje_dd">
									<span class="fbold">¥1,000.00${record.accountedmoney}</span>
								</div>
			        		</td>
                            <td class="tr">${record.costname}</td>
                            <td class="tr"><span class="fbold tdorange">人民币1.00$通过标签选择</span></td>
                            <td class="tc">孙喜超${record.lastreviewer}</td>
                            <td class="invoice_yes tc">已通过${record.status}</td>
                            <td class="invoice_no tc">未付款${record.paystatus}</td>
                            <td class="p0">
                                <dl class="handle">
	                                <dt><img title="操作" src="${ctxStatic }/images/handle_cz.png" /></dt>
	                                <dd class="">
	                                    <p> 
	                                        <span></span> 
	                                        <!--不同产品类型，不同的链接-->
	                                        <c:choose>
	                                        	<c:when test="${record.producttype==7}">
	                                        		  <a href="${ctx}/order/newcomdiscount/newShowcomdiscountDetail?reviewId=${record.id}">查看</a> 
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		<a href="javascript:void(0)">查看1</a> 
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		<a href="javascript:void(0)">查看2</a> 
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		<a href="javascript:void(0)">查看3</a> 
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		<a href="javascript:void(0)">查看4</a> 
	                                        	</c:when>
	                                        </c:choose>
		                                    <a href="${ctx }/cost/manager/forcastList/${record.productid }/${record.producttype}">预报单</a> 
		                                    <a href="${ctx }/cost/manager/settleList/${record.productid }/${record.producttype}">结算单</a> 
		                                    <c:if test="${record.status==1}">
			                                    <a href="${ctx}/order/newcomdiscountApprove/comdiscountCancel?reviewId=${record.id}">撤销</a>
		                                    </c:if>
		                                  
		                                     <c:choose>
	                                        	<c:when test="${record.producttype==7}">
	                                        		  <a href="${ctx}/newcomdiscountApprove/newShowcomdiscountDetail?reviewId=${record.id}">审批</a> 
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		  <a href="javascript:void(0)">审批1</a>  
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		  <a href="javascript:void(0)">审批2</a>  
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		  <a href="javascript:void(0)">审批3</a>  
	                                        	</c:when>
	                                        	<c:when test="">
	                                        		  <a href="javascript:void(0)">审批4</a>  
	                                        	</c:when>
	                                        </c:choose>
	                                    </p>
	                                </dd>
                         	    </dl>
                            </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                </table>
                <div class="page">
	                <div class="pagination">
	                    <dl>
	                        <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
	                        <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
	                        <dd>
	                            <a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a>
	                        </dd>
	                    </dl>
	                </div>
                    <div class="pagination">
                       ${pageStr}
                    </div>	
				</div>
                <!--右侧内容部分结束-->
<!--S批量审核操作弹出层-->
<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
  <table width="100%" style="padding:10px !important; border-collapse: separate;">
    <tr>
      <td></td>
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
