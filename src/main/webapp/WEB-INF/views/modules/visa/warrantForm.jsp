<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>押金转担保申请</title>
<meta name="decorator" content="wholesaler" />
    
<script type="text/javascript">

	function checkall(obj){
		if($(obj).attr("checked")){
			$("input[name='allChk']").attr("checked",'true');
			$("input[addName='travelerIds']").attr("checked",'true');
			$("input[addName='travelerIds']:checked").each(function(i,a){
				setName(a);
			});
		}				
		else{
			$("input[name='allChk']").removeAttr("checked");
			$("input[addName='travelerIds']").removeAttr("checked");
			$("input[addName='travelerIds']").each(function(i,a){
				setName(a);
			});
		}
	}
	
	function setName(element) {
		if($(element).length != 0 && $(element).is(":checkbox")) {
			var addName = $(element).attr("addName");
			if($(element).prop("checked")) {
				$(element).attr("name",addName);
				$(element).parent().parent().find("[class='subVal']").each(function(i, e) {
					var addName = $(e).attr("addName");
					$(e).attr("name",addName);
				});
			}else{
				$(element).attr("name","");
				$(element).parent().parent().find("[class='subVal']").each(function(i, e) {
					$(e).attr("name","");
				});
			}
		}
	}
	
	function subForm() {
		if($(".table_borderLeftN").find("input[name='travelerIds']").length == 0) {
			alert("请选择提交申请的游客！")
		}else{
			$("#addForm").attr("action","${ctx}/order/manager/visa/warrantApply?proId=${proId}");
			$("#addForm").submit();
		}
	}
</script>

</head>
<body>
	<div class="ydbz_tit">订单详情</div>
		<div class="orderdetails1">
             <table border="0" style="margin-left: 25px" width="98%">
                 <tbody>
                     <tr>
                         <td class="mod_details2_d1">下单人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                         <td class="mod_details2_d1">下单时间：</td>
                         <td class="mod_details2_d2">
                         	<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate}"/>
                       	</td> 
                         <td class="mod_details2_d1">团队类型：</td>
                         <td class="mod_details2_d2">单办签</td>	
                         <td class="mod_details2_d1">收客人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                                
                     </tr>
                     <tr> 
                         <td class="mod_details2_d1">订单编号：</td>
                         <td class="mod_details2_d2">${visaOrder.orderNo}</td>
                         <td class="mod_details2_d1">订单团号：</td>
                         <td class="mod_details2_d2">${visaOrder.groupCode}</td>
                         <td class="mod_details2_d1">订单总额：</td>
                         <td class="mod_details2_d2"><em class="tdred">${fns:getMoneyAmountBySerialNum(visaOrder.totalMoney,2)}</em></td>
                         <td class="mod_details2_d1">订单状态：</td>
                         <td class="mod_details2_d2">
                         	<c:if test="${not empty visaOrder.visaOrderStatus}">
								<c:choose>
									<c:when test="${visaOrder.visaOrderStatus eq '0'}">未收款</c:when>
									<c:when test="${visaOrder.visaOrderStatus eq '1'}">已收款</c:when>
									<c:when test="${visaOrder.visaOrderStatus eq '2'}">已取消</c:when>
								</c:choose>
							</c:if>
                         </td>	 
                     </tr>
                     <tr>
                         <td class="mod_details2_d1">操作人：</td>
                         <td class="mod_details2_d2">${visaProdcut.createBy.name}</td>     
                         <td class="mod_details2_d1">销售：</td>
                         <td class="mod_details2_d2">${visaOrder.salerName}</td>
                     </tr>
                 </tbody>
         	</table>	
        </div>
	<div class="ydbz_tit">游客押金转担保</div>
		<form id="addForm" action="" method="post">
			<input type="hidden" name="reviewId" value="${param.reviewId}"/>
			<div class="orderVisaDep">
                 <table class="activitylist_bodyer_table">
                    <thead>
                       <tr>
                          <th width="8%" class="table_borderLeftN">全选<input type="checkbox" onclick="checkall(this)" name="allChk"></th>
                          <th width="15%">游客</th>
                          <th width="17%">币种</th>
                          <th width="10%">押金金额</th>
                          <th width="10%">签证状态</th>
                          <th width="10%">转担保类型</th>
                          <th width="30%">原因</th>
                       </tr>
                    </thead>
                    <tbody>
                       <c:forEach items="${travelerList}" var="traveler" varStatus="s">
                       <tr>
	                       <td class="table_borderLeftN"><input type="checkbox" addName="travelerIds" name="" value="${traveler[0].id}" onclick="setName(this)"></td>
	                       <td>${traveler[0].name}</td><input type="hidden" value="${traveler[0].name}" name="" addName="travelerName" class="subVal"/>
	                       <td>${traveler[1][0]}</td><input type="hidden" value="${traveler[1][0]}" name="" addName="priceCurrency" class="subVal"/>
	                       <td class="tr tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${fn:replace(traveler[1][1],',','')}" /></td><input type="hidden" value="${traveler[1][1]}" name="" addName="price" class="subVal"/>
	                       <td>
                       		  <c:if test="${not empty traveler[2]}">
								 <c:choose>
									<c:when test="${traveler[2] eq '0'}">未送签</c:when>
									<c:when test="${traveler[2] eq '1'}">送签</c:when>
									<c:when test="${traveler[2] eq '2'}">约签</c:when>
									<c:when test="${traveler[2] eq '3'}">出签</c:when>
									<c:when test="${traveler[2] eq '4'}">申请撤签</c:when>
									<c:when test="${traveler[2] eq '5'}">撤签成功</c:when>
									<c:when test="${traveler[2] eq '6'}">撤签失败</c:when>
									<c:when test="${traveler[2] eq '7'}">拒签</c:when>
								 </c:choose>
								 <input type="hidden" value="${traveler[2]}" name="" addName="visaStatus" class="subVal"/>
							  </c:if>
	                       </td>
	                       <td class="tc">
	                          <select addName="warrantType" name="" class="subVal">
	                              <option value="1">担保</option>
	                              <option value="2">担保+押金</option>
	                          </select>
	                       </td>
	                       <td class="tc">
	                          	<input type="text" value="" name="" addName="reasonMark" class="subVal" maxlength="256">
	                   	   </td>
                       </tr>
                       </c:forEach>
                       <c:if test="${empty travelerList}">
                       	   <td colspan="7"><div class="wtjqw">暂无需要申请担保的游客</div></td>
                       </c:if>
                    </tbody>
                 </table>
             </div> 
             <div class="dbaniu" style="width:122px;">
             	 <input class="ydbz_x" type="button" onClick="subForm()" value="提交"/>
                 <input class="ydbz_x gray" type="button" value="返回" onClick="history.go(-1)"/>
             </div>
		</form>
</body>
</html>