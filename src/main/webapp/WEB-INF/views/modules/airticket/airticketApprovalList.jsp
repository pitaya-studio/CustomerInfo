<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>机票改签审核</title>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!-- 20150810 批量审核js -->
<script src="${ctxStatic}/modules/batchReview/batchReview.js" type="text/javascript"></script>

<script type="text/javascript">
var root = "${ctx}";
function page(pn,ps){
	   $("#pageNo").val(pn);
	   $("#pageSize").val(ps);
	   $("#searchForm").submit();
	}
	
	
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

$(function(){

var status = $("#status").val();
var userJob = $("#userJob").val();
$("#"+status).attr("class","select");
$("#"+userJob).attr("style","background:none repeat scroll 0% 0% #28B2E6");
	//展开筛选按钮
	launch();
		operateHandler();
		//展开筛选按钮
		//搜索聚焦失焦
		inputTips();
		
	//产品销售和下单人切换
	switchSalerAndPicker();
	$('#salerrefund').comboboxInquiry();
	$('#order-picker').comboboxInquiry();
	$('#agentId').comboboxInquiry();
	$('#jd').comboboxInquiry();
		
	//有查询条件时 自动打开 展开按钮	
	if($("#start").val()!=""||$("#end").val()!=""||$("#agentId").val()!=""||$("#jd").val()!=""||$("#salerrefund").val()!=""||$("#order-picker").val()!=""){
		$('.zksx').click();
	}
	
		
	$('#submitto').click(function(){
		$("#searchForm").submit();
	});
	
	$('#all').click(function(){
	$("#status").val('all');
		$("#searchForm").submit();
	});
	$('#todo').click(function(){
	$("#status").val('todo');
		$("#searchForm").submit();
	});
	$('#todoing').click(function(){
	$("#status").val('todoing');
		$("#searchForm").submit();
	});
	$('#cancel').click(function(){
	$("#status").val('cancel');
		$("#searchForm").submit();
	});
	$('#reject').click(function(){
	$("#status").val('reject');
		$("#searchForm").submit();
	});
	$('#pass').click(function(){
	$("#status").val('pass');
		$("#searchForm").submit();
	});
	
	$("div.message_box div.message_box_li").hover(function(){
        $("div.message_box_li_hover",this).show();
    },function(){
         $("div.message_box_li_hover",this).hide();
    });
	
});


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
 <form id="searchForm" action="${ctx}/order/manage/aireGaiQianAppList" method="post">
    <!--右侧内容部分开始-->
            	<!--右侧内容部分开始-->
               <input id="status" name="status"  type="hidden" value="${conditionsMap.status}"/>
               <input id="userJob" name="userJob"  type="hidden" value="${conditionsMap.userJobCon}"/>
                <input type='hidden' id='pageNo' name='pageNo'/>
                <input type='hidden' id='pageSize' name='pageSize'/>
                <div class="message_box">
                	<c:forEach items="${userJobs}" var="entity">
	                    <div class="message_box_li">
	                    	<a id="${entity.deptId}-${entity.id}" href="${ctx}/order/manage/aireGaiQianAppList?userJob=${entity.deptId}-${entity.id}">
								<div class="message_box_li_a <c:if test='${entity.id == conditionsMap.userJobId}'>curret</c:if>">
									<span>${fns:abbrs(entity.deptName,entity.jobName,40)}</span>
			                        <c:if test="${entity.count>0}"><div class="message_box_li_em" >${entity.count}</div></c:if>
									<c:if test="${fns:getStringLength(entity.deptName,entity.jobName) gt 37}">
										<div class="message_box_li_hover">
											${entity.deptName }_${entity.jobName }
										</div>
									</c:if>
								</div>
							</a>
						</div>
                    </c:forEach>
				</div>
                	<div class="activitylist_bodyer_right_team_co">
                    	<div class="activitylist_bodyer_right_team_co2 pr wpr20">
                        	<label>团号：</label><input style="width:260px" value="${conditionsMap.wholeSalerKey}" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="" type="text" />
                        </div>
                        <div class="form_submit">
                        	<input class="btn btn-primary" value="搜索" id="submitto" type="submit">
                        </div>
                        <a class="zksx">展开筛选</a>
                        <div class="ydxbd">
                            <!--<div class="activitylist_bodyer_right_team_co1">
                            	<div class="activitylist_team_co3_text">审核状态：</div><select>
                                	<option value="">已通过</option>
                                    <option value="">未通过</option>
                                    <option value="" selected="selected">全部</option>
                                    <option value="">待审核</option>
                                </select>
                            </div>-->
                           	<div class="activitylist_bodyer_right_team_co1">
                            	<div class="activitylist_team_co3_text">渠道选择：</div>
                            	<select id="agentId" name="agentId">
                            		<option value=""></option>
                                	<c:forEach items="${fns:getAgentList()}" var="cell">
                                	<option value="${cell.id }"<c:if test="${conditionsMap.agentId==cell.id }">selected="selected"</c:if>>${cell.agentName}</option>
                                </c:forEach>
                                </select>
                            </div>
                           
                            <div class="activitylist_bodyer_right_team_co2">
                            	<label>下单时间：</label><input id="start" class="inputTxt dateinput" name="start" 
                            	value="${conditionsMap.start}" onclick="WdatePicker()" readonly=""/>
                                <span> 至 </span>
                                <input id="end" class="inputTxt dateinput" name="end"
								value="${conditionsMap.end}" onclick="WdatePicker()" readonly="" />
                            </div>
                            
                            <div class="kong"></div>
                            
                            <div class="activitylist_bodyer_right_team_co1">
                           	<div class="activitylist_team_co3_text">销售：</div>
							<select  name="saler" id="salerrefund">
								<option></option>
								<c:forEach items="${userList }" var="user">
									<option value="${user.id }" <c:if test="${conditionsMap.saler == user.id }">selected="selected"</c:if>>${user.name }</option>
								</c:forEach>
							</select>
                            </div>
                            
                            <div class="activitylist_bodyer_right_team_co1">
								<div class="activitylist_team_co3_text">下单人：</div>
								<select name="picker" id="order-picker">
									<option></option>
									<c:forEach items="${userList }" var="user">
										<option value="${user.id }" <c:if test="${conditionsMap.picker==user.id }">selected="selected"</c:if>>${user.name }</option>
									</c:forEach>
								</select>
							</div>
                            
                            <div class="activitylist_bodyer_right_team_co1">
                            	<div class="activitylist_team_co3_text">计调：</div>
                            	<select class="sel-w1" name="jd" id="jd">
                            	<option value=""></option>
                                	 <c:forEach items="${userList }" var="user">
										<option value="${user.id }" <c:if test="${conditionsMap.jd==user.id }">selected="selected"</c:if>>${user.name }</option>
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
					<a  href="javascript:void(0)" id="all"  >全部</a>
					<a  id="todo" href="javascript:void(0)">待本人审核</a>
					<a  id="todoing" href="javascript:void(0)">审核中</a>
					<a id="reject" href="javascript:void(0)">未通过</a>
					<a id="pass" href="javascript:void(0)">已通过</a>
					<a id="cancel" href="javascript:void(0)">已取消</a>
                </div>
                
                <!--状态结束-->
                <table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="10%">订单编号</th>
                            <th width="11%"><span class="tuanhao on">团号</span></th>
                            <th width="6%">计调</th>
                            <th width="7%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
                            <th width="6%">游客</th>
                            <th width="9%">下单时间</th>
                            <th width="9%">报批日期</th>
                            <th width="10%">机票金额</th>
                            <th width="7%">上一环节<br />审批人</th>
                            <th width="9%">原因备注</th>
                            <th width="6%">审核状态</th>
                            <th width="6%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="cell" varStatus="status">
                    	<tr>
                        	<td>
                        	<!-- 20150804 add 多选框 -->
							<c:if test="${conditionsMap.status=='todo' }">
							 	<input type="checkbox" value="${cell.nowLevel}@${cell.id}" name="activityId" >
							</c:if>
                        	${status.index+1}</td>
                            <td>${cell.orderNo}</td>
                            <td>
                            	<div class="tuanhao_cen onshow">${cell.group_code}</div>
                        		<div class="chanpin_cen qtip" title="JHGJLXS5674--切位"><a href="javascript:void(0)" target="_blank">JHGJLXS5674--切位</a></div>
                    		</td>
                            <td>${fns:getUserById(cell.jdCreateBy+"").name}</td>
                            <td><span class="order-saler onshow">${fns:getUserNameById(cell.saler)}</span><span class="order-picker">${fns:getUserNameById(cell.picker)}</span></td>
                            <td>${cell.name} </td>
                            <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cell.create_date}"/></td>
                            <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cell.createDate}"/></td>
                            <td class="tr"><span class="fbold tdorange">${cell.moneyStr}</span></td>
                            <td>
                            <c:if test="${cell.updateBy==''}"></c:if>
                            <c:if test="${cell.updateBy!=''}"> ${fns:getUserById(cell.updateBy+"").name}</c:if>
                            
                            </td>
                            <td>${cell.createReason}</td>
                            <td class="invoice_no">
	                         ${fns:getNextReview(cell.id) }
                            </td>
                            <td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_cz.png" title="操作">
								</dt>
								<dd>
									<p>
<!-- 									${fns:getUser().id}--${ cell.updateBy }-- ${ cell.status  } -- ${ cell.nowLevel } -->
										<c:if test="${fns:getUser().id == cell.updateBy && cell.status == 1 && cell.nowLevel != 1}">
											<a href="#" onclick="cancelAirticketReturnreview('${cell.id}','${conditionsMap.revLevel}')">撤销</a>
										</c:if>
										
										<span></span>
										<c:if test="${cell.status == '1' && cell.nowLevel == conditionsMap.revLevel}">
										<a target="_blank" href="${ctx}/order/manage/airticketApprovalDetail?orderId=${cell.orderId}
										&trvalerId=${cell.travelerId}&reviewId=${cell.id}">审批</a>
										</c:if>
										<a target="_blank" href="${ctx}/order/manage/airticketOrderDetail?orderId=${cell.orderId}&reviewId=${cell.id}">详情</a>
									</p>
								</dd>
							</dl></td>
                        </tr>
                    	</c:forEach>
                    </tbody>
					<!-- 20150804 全选反选 -->
					<c:if test="${conditionsMap.status=='todo' }">
						<tr class="checkalltd">
							<td colspan='19' class="tl">
								<label>
									<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
								</label>
								<label>
									<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
								</label>
								<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:payedConfirmNew('${ctx}',1,1,'${conditionsMap.revLevel}','/order/manage/batchPlaneReview');" >批量审批</a>
							</td>
						</tr>
					</c:if>
                </table>
            	 <div class="page">
				        <div class="pagination">
				            <div class="endPage">${page}
				            </div>
				            <div style="clear:both;"></div>
				        </div>
				  </div>
                <!--右侧内容部分结束-->
</form>
</body>
</html>