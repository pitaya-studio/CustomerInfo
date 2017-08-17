<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-海岛游-转团记录</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
});
</script>
</head>
<body>
	<div class="bgMainRight">
		<!--右侧内容部分开始-->
        <page:applyDecorator name="activity_island_change_list"></page:applyDecorator>
         <div class="mod_nav">订单 > 海岛游 > 转团记录</div>
		<div class="filter_btn"><a class="btn btn-primary" href="${ctx}/islandapplications/goToIslandOrderUuid/${order.uuid }" target="_blank">申请转团</a> </div>
		<table id="contentTable" class="activitylist_bodyer_table">
		   <thead>
			  <tr>
                 <th width="7%">游客姓名</th>
				 <th width="5%">游客类型</th>
				 <th width="7%">舱位等级</th>
				 <th width="7%">申请时间</th>
				 <th width="5%">签证国家及类型</th>
				 <th width="15%">证件类型/证件号/有效期</th>
				 <th width="9%">备注</th>
				 <th width="9%">转入团</th>
				 <th width="6%">申请人</th>
				 <th width="6%">审批状态</th>
				 <th width="6%">操作</th>
			  </tr>
		   </thead>
		   <tbody>
		   		<c:forEach items="${reviewList }" var="review">
		   			<tr>
		   				<td  class="tc">${review.KEY_TRAVELERNAME }</td>
			   				<td  class="tc">${fns:getTravelerTypeCn(review.KEY_TRAVELERTYPE)}</td>
			   				<td  class="tc">
			   					<c:if test="${review.KEY_TRAVELERLEVEL eq '1'}">头等舱</c:if>
			   					<c:if test="${review.KEY_TRAVELERLEVEL eq '2'}">公务舱</c:if>
			   					<c:if test="${review.KEY_TRAVELERLEVEL eq '3'}">经济舱</c:if>
			   					<c:if test="${review.KEY_TRAVELERLEVEL ne '1' and review.KEY_TRAVELERLEVEL ne '2' and review.KEY_TRAVELERLEVEL ne '3'}">待选</c:if>
			   				</td>
			   				<td  class="tc">${review.KEY_APPLYDATE }</td>
			   				<td  class="tc">${review.KEY_VISACOUNTRYTYPE }</td>
			   				<td  class="tc">${review.KEY_PAPERSTYPECODEDATE }</td>
			   				<td  class="tc">${review.KEY_REMARK }</td>
			   				<td  class="tc">${review.KEY_NEWGROUPCODE }</td>
			   				<td  class="tc">${fns:getUserById(review.createBy).name}</td>
		   				<td  class="tc">
		   					<c:if test="${review.status eq 0 }">驳回</c:if>
		   					<c:if test="${review.status eq 1 }">待审核</c:if>
		   					<c:if test="${review.status eq 2 }">审核成功</c:if>
		   					<c:if test="${review.status eq 3 }">操作完成</c:if>
		   					<c:if test="${review.status eq 4 }">取消审核</c:if>
		   				</td>
		   				<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
								<dd class="">
									<p>
										<span></span> 
										<a href="${ctx}/islandapplications/goToIslandOrderInfo/${order.uuid }/${review.KEY_TRAVELERUUID}">查看详情</a>
								   </p>
								</dd>
							</dl>
						</td>
		   			</tr>
		   		</c:forEach>
		   </tbody>
		   
		</table>
              <div class="ydBtn"><a class="ydbz_s" onclick="window.close();">关闭</a></div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
