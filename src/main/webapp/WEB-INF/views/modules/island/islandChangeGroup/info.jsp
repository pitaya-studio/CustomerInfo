<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-海岛游-转团详情</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--订单模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.orderform.js"></script>
<script type="text/javascript">
$(function(){
	//input获得失去焦点提示信息显示隐藏
	inputTips();
	ic_refunds();
	gaijia('visa');
	$('.ic_formoney_add').click(function() {
		var html = '<li><i><input type="checkbox" /></i> <i><input type="text" name="" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select name="teamCurrency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i>本次转团金额：</i> <i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" onkeyup="validNum(this)" onafterpast="validNum(this)" maxlength="20" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		$(this).parents('.gai-price-ol').find("li:last").find("select[name='teamCurrency'] option:first").prop("selected",true);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTipsDynamic($(this).parents('.gai-price-ol').find("li:last"));
	});
});
//团队转团
function ic_refunds(){
	$('.refund-price-btn').click(function() {
		var html = '<li><i><input type="text" name="" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" class="gai-price-ipt1" name="refund" data-type="rmb" flag="istips" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this)"/><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
	});
	//删除团队转团一项
	$('.gai-price-ol').on("click",".clear-btn",function(){
		$(this).parents('li').remove();
		//totalRefund();
	});
	//游客转团-删除转团
	$('#contentTable').on("click",'.gaijia-delete',function(){
		$(this).parent().parent().parent().remove();
		//totalRefund();
	})
	//游客转团-新增转团
	$('#contentTable').on("click",'.gaijia-add',function(){
		//$(this).parent().parent().parent().remove();
		var html='<tr>';
		html+='<td class="refundtd"><input type="text"><div class="pr"><i class="gaijia-delete" title="删除款项"></i></div></td>';
		html+='<td class="tr" width="10%">￥<span class="tdgreen">11,900</span></td>';
		html+='<td class="tr" width="10%">$<span class="tdgreen">11,900</span></td>';
		html+='<td class="tc"><select style="width:90%;" class="selectrefund">'+$("#currencyTemplate").html()+'</select></td>';
		html+='<td><input type="text" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this)" name="refund" data-type="eur"></td>';
		html+='<td><input type="text"></td></tr>';
		$(this).parents('.refundTable').append(html);
		//$(this).parents('tbody').find('td[rowspan]')
		//totalRefund();
	})		
}

</script>
</head>
<body>
	<div class="bgMainRight">
		<!--右侧内容部分开始-->
		
               <page:applyDecorator name="activity_island_change_info"></page:applyDecorator>
              <!--币种模板开始-->
              <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
                  <option value="1">人民币</option>
                  <option value="2">美元</option>
                  <option value="3">欧元</option>
                  <option value="4">日元</option>
              </select>
              <!--币种模板结束-->
              <div class="mod_nav">订单 > 海岛游 > 转团详情</div>
              <!-- 订单信息 -->
				<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>
				<!-- 费用及人数 -->
				<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostAndNumInfo.jsp"%>
				<!-- 费用结算 -->
				<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostInfo.jsp"%>
				
              <div class="ydbz_tit pl20"><span class="fl">申请转团</span></div>
				<table id="contentTable" class="table activitylist_bodyer_table activitylist_bodyer_table_new">
                    <thead>
                        <tr>
						    <th width="9%" class="table_borderLeftN">序号</th>
                            <th width="14%">客人姓名</th>
							 <th width="11%">游客类型</th>
							 <th width="11%">舱位等级</th>
                            <th width="14%">签证国家及类型</th>
                            <th width="21%">证件类型/证件号/有效期</th>
                            <th width="20%">备注</th>
                        </tr>
                    </thead>
                    <tbody>
						<c:forEach items="${travelList }" var="travel">
	                   	 <tr>
						       <td class="table_borderLeftN">
						       		<input type="checkbox"  name="chk"/>
						       		<input type="hidden" name="travelUuid" value="${travel.uuid }"/>
						       	</td>
	                           <td class="tc">${travel.name }</td>
	                           <td class="tc">
	                           		<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" value="${travel.personType }" srcColumnName="name" />
	                           </td>
	                           <td class="tc">${travel. }</td>
	                           <td class="tc">
	                           		<c:forEach items="${travel.islandTravelervisaList }" var="visa">
	                           			<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" value="${visa.country }" srcColumnName="name_cn" />
	                           			<trekiz:defineDict id="hotelTheme" name="hotelTheme" type="new_visa_type" defaultValue="${visa.visaTypeId }" />
	                           			<br/>
	                           		</c:forEach>
	                           </td>
						       <td class="tc">
						       		<c:forEach items="${travel.islandTravelerPapersTypeList }" var="papers">
						       			<c:if test="${papers.papersType eq 1 }">身份证</c:if>
						       			<c:if test="${papers.papersType eq 2 }">护照</c:if>
						       			<c:if test="${papers.papersType eq 3 }">警官证</c:if>
						       			<c:if test="${papers.papersType eq 4 }">军官证</c:if>
						       			<c:if test="${papers.papersType eq 5 }">其他</c:if>
						       			/${papers.idCard }/
						       			<fmt:formatDate value="${papers.validityDate }" pattern="yyyy-MM-dd"/>
						       			<br/>
						       		</c:forEach>
						       </td>
	                           <td class="tc"><input type="text" name="remark" value=""/></td>
	                       </tr>
	                   </c:forEach>
                    </tbody>
                </table>
				<!--<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
			<div class="ydbz_x fl ">全部清空</div>
		</div>-->
              
	  <div class="ydbz_tit pl20"><span class="fl">转团记录</span></div>
		<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new">
		   <thead>
				  <tr>
					 <th width="10%">游客姓名</th>
					 <th width="7%">游客类型</th>
					 <th width="7%">舱位等级</th>
					 <th width="10%">申请时间</th>
					 <th width="10%">签证国家及类型</th>
					 <th width="20%">证件类型/证件号/有效期</th>
					 <th width="10%">备注</th>
					 <th width="10%">转入团</th>
					 <th width="7%">申请人</th>
					 <th width="7%">审批状态</th>
				  </tr>
			   </thead>
			   <tbody>
			   		<c:forEach items="${reviewList }" var ="review">
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
			   			</tr>
			   		</c:forEach>
			   </tbody>
		</table>
		<div class="dbaniu" >
			<a class="ydbz_s " onclick="window.close();">关闭</a>
		</div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
