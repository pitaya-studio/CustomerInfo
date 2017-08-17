<%@ page contentType="text/html;charset=UTF-8" %>
<c:if test="${activity.payMode_full=='1'}">
	<a style="display:none;" href="javascript:void(0)"  class="normalPayType aPayforModePrice${group.id}  
		<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
		<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
		<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},3,${group.freePosition})' </c:if>>
		付全款
	</a>
</c:if>
<c:if test="${activity.payMode_op=='1'}">
	<a style="display:none;" href="javascript:void(0)"  class="opPayType aPayforModePrice${group.id}  
		<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
		<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
		<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},7,${group.freePosition})' </c:if>>
		计调确认占位
	</a>
</c:if>
<c:if test="${activity.payMode_cw=='1'}">
	<a style="display:none;" href="javascript:void(0)"  class="cwPayType aPayforModePrice${group.id}  
		<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
		<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
		<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},8,${group.freePosition})' </c:if>>
		计调确认占位
	</a>
</c:if>
<c:if test="${activity.payMode_deposit=='1'}">
	<a style="display:none;" href="javascript:void(0)"  class="dingjin_PayType aPayforModePrice${group.id}  
		<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
		<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
		<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},1,${group.freePosition})' </c:if>>
		订金占位
	</a>
</c:if>
<c:if test="${activity.payMode_advance=='1'}">
	<a style="display:none;" href="javascript:void(0)"  class="yuzhan_PayType aPayforModePrice${group.id}  
		<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
		<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
		<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},2,${group.freePosition})' </c:if>>
		预占位
	</a>
</c:if>

<input hidden="hidden" name="bookOrder" value="${group.freePosition},${group.leftdays},${group.settlementAdultPrice},${group.settlementcChildPrice},${group.settlementSpecialPrice},${group.suggestAdultPrice},${group.suggestChildPrice},${group.suggestSpecialPrice}"/>
                          
<select style="display:none;">
	<c:if test="${activity.payMode_full=='1'}">
		<option value='3'>全款支付</option>
	</c:if>
	<c:if test="${activity.payMode_op=='1'}">
		<option value='7'>计调确认占位</option>
	</c:if>
	<c:if test="${activity.payMode_cw=='1'}">
		<option value='8'>财务确认占位</option>
	</c:if>
	<c:if test="${activity.payMode_deposit=='1'}">
		<option value='1'>订金占位</option>
	</c:if>
	<c:if test="${activity.payMode_advance=='1'}">
		<option value='2'>预占位</option>
	</c:if>
</select>
<br>
<div class="kong"></div>
<c:if test="${queryCommonOrderList=='1' && activityKind == 1}">
	<input class="btn btn-primary" value="已收明细"  onclick="javascript:showOrderPay(1234,this);" type="button"/>
	<div class="kong"></div>
</c:if>

<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->

<!-- 285 越柬行踪 单团报名 产品列表默认展开备注 -->
<!--<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
	<a class="expandNotes" href="javascript:void(0)"> 收起备注 </a>
</c:if>
<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
	<a class="expandNotes" href="javascript:void(0)"> 展开备注 </a>
</c:if> -->

<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' || companyUuid eq 'cb19b61e52fb4b6ab422aedac38acdfa'}">
	<a class="expandNotes" href="javascript:void(0)"> 收起备注 </a>
</c:if>
<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">
	<a class="expandNotes" href="javascript:void(0)"> 展开备注 </a>
</c:if>

<!-- 576需求-报名团期备注默认展开***end*** -->

<em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em>

<shiro:hasPermission name="price:project">
	<c:if test="${not empty group.priceJson}">
		<br/>
		<a href="javascript:void(0);" onclick="expandPriceJson(this, ${activityKind})" data='${group.priceJson}'>展开价格方案</a>
	</c:if>
</shiro:hasPermission>
