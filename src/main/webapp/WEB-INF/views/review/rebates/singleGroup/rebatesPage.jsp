<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	    <meta name="decorator" content="wholesaler"/>
	    <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				<title>宣传费申请</title>
			</c:when>
         <c:otherwise>
              <title>返佣申请</title>
          </c:otherwise>
     </c:choose>   
	    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/review/rebates/singleGroup/rebatesList.js"></script>
		<script type="text/javascript">
			$(function(){  
				//input获得失去焦点提示信息显示隐藏  
				inputTips();
				rebates.init();

				//返佣对象的选择 如果为供应商则带出供应商信息---C475--start
				$('#rebateObject').comboboxInquiry();
				//返佣对象类型联动
				$(document).on('change', '.rebateTarget', function () {
					if ($(this).find('option:selected').val() == '2') {//选择返佣供应商
						$('#rebateObject').comboboxInquiry('resetEmpty');
						$('#rebateObject').find("option[value='0']").attr('selected','selected');
						$('.supplyNameSpan').show();
						$('#default').show();
						$('#domesticAccount').hide();
						$('#overseasAccount').hide();
					}
					else {
						$('.supplyNameSpan').hide();
						$('.supplyInfoSpan').hide();
					}
				});
				//返佣对象类型联动，选择境内境外账号联动时
				$(document).on('change', '#accountType', function () {
					if ($(this).find('option:selected').val() == '1') {//选择境内账号
						$('#default').hide();
						$('#domesticAccount').show();
						$('#overseasAccount').hide();
					}
					else if($(this).find('option:selected').val() == '2'){
						$('#default').hide();
						$('#domesticAccount').hide();
						$('#overseasAccount').show();
					}else{
						$('#default').show();
						$('#domesticAccount').hide();
						$('#overseasAccount').hide();
					}
				});
				//返佣对象类型联动，选择开户行时关联账号号码
				$(document).on('change', 'select[name="bankName"]', function () {
					var bankAccountCode=$(this).find("option:selected").attr("bankAccountCode");
					if(bankAccountCode == undefined){
						$(this).parent().find("select[name='bankAccount']").empty();
						$(this).parent().find("select[name='bankAccount']").append("<option value='0' selected='selected'>请选择</option>");
					}else{
						/*$(this).parent().find("select[name='bankAccount']").text(bankAccountCode);*/
						$(this).parent().find("select[name='bankAccount']").empty();
						$(this).parent().find("select[name='bankAccount']").append("<option value='"+bankAccountCode+"' selected='selected'>"+bankAccountCode+"</option>");
					}

				});
				//返佣供应商联动,选择供应商名称时
				$(document).on('comboboxinquiryselect', '.supplyName', function () {
					var selectedSupply = $(this).find('option:selected').val();
					if (selectedSupply != '0') {//有供应商选择
						var url = "${ctx}"+"/rebatesupplier/manager/bankinfo/"+selectedSupply;
						$.ajax({
							type: "POST",
							url: url,
							dataType:"json",
							success: function(data){
								//渲染境内账号
								$("#domesticAccount select[name='bankName']").empty();
								var optionStr="<option value='0'>请选择</option>";
								$("#domesticAccount select[name='bankName']").append(optionStr);
								if(data.domestic.length>0){
									$.each(data.domestic,function(i, n){
										var optionStr = "<option value='"+n.id+"' bankAccountCode='"+ n.bankAccountCode+"'>"+n.bankName+"</option>";
										$("#domesticAccount select[name='bankName']").append(optionStr);
									});
								}

								//渲染境外账号
								$("#overseasAccount select[name='bankName']").empty();
								var optionStr="<option value='0'>请选择</option>";
								$("#overseasAccount select[name='bankName']").append(optionStr);
								if(data.overseas.length>0){
									$.each(data.overseas,function(i, n){
										var optionStr = "<option value='"+n.id+"' bankAccountCode='"+ n.bankAccountCode+"'>"+n.bankName+"</option>";
										$("#overseasAccount select[name='bankName']").append(optionStr);
									});
								}
							}
						});
						$('.supplyInfoSpan').show();
						$('#default').show();
						$('#domesticAccount').hide();
						$('#overseasAccount').hide();
						//清除之前的选项
						$("#accountType").find("option[value='0']").attr("selected","selected");
						$("select[name='bankName']").find("option[value='0']").attr("selected","selected");
						$("select[name='bankAccount']").find("option[value='0']").attr("selected","selected");
						/*$("label[name='bankAccount']").text("");*/
					}
					else {
						$('.supplyInfoSpan').hide();
					}
				});
				//返佣对象的选择 如果为供应商则带出供应商信息---C475--end
			});



		</script>
	</head>
<body>
	<!-- 顶部参数 -->
	<input value="${fns:getUser().company.uuid}" type="hidden" id="inputId">
    <page:applyDecorator name="show_head">
	     <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					  <page:param name="desc">宣传费申请</page:param>
				</c:when>
	         <c:otherwise>
	                <page:param name="desc">返佣申请</page:param>
	          </c:otherwise>
	     </c:choose>   
	</page:applyDecorator>
	
    <!--币种模板-->
    <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
        <c:forEach items="${currencyList}" var="currency">
			<option value="${currency.id}">${currency.currencyName}</option>
		</c:forEach>
    </select>
       <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 <div class="mod_nav">订单 > ${orderTypeStr} > 宣传费记录 > 宣传费申请</div>
			</c:when>
         <c:otherwise>
              <div class="mod_nav">订单 > ${orderTypeStr} > 返佣记录 > 返佣申请</div>
          </c:otherwise>
     </c:choose>   
    <%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>

	<c:if test="${not empty multiRebateObject and multiRebateObject eq true}">
		<div class="ydbz_tit" id="rebateObjectTitle">
			<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 <span class="fl">宣传费对象</span>
			</c:when>
         <c:otherwise>
              <span class="fl">返佣对象</span>
          </c:otherwise>
     </c:choose>   
		</div>
		<div class="rebateObj">
		<span>
			<label>
				<em class="xing">*</em>
				对象类型：
			</label>
			<select class="rebateTarget">
				<option value="0">请选择</option>
				<option value="1">渠道</option>
				<option value="2">供应商</option>
			</select>
		</span>
		<span class="supplyNameSpan">
			<label>
				<em class="xing">*</em>
				供应商名称：
			</label>
			<select class="supplyName" id="rebateObject">
				<option value="0">请选择</option>
				<c:if test="${not empty suppliers}">
					<c:forEach items="${suppliers}" var="supplier">
						<option value="${supplier.id}">${supplier.name}</option>
					</c:forEach>
				</c:if>
			</select>
		</span>
		<span class="supplyInfoSpan">
			<label>
				<em class="xing">*</em>
				账户类型：
			</label>
			<select name="accountType" id="accountType">
				<option value="0">请选择</option>
				<option value="1">境内</option>
				<option value="2">境外</option>
			</select>
			<span id="default">
				<label>
					<em class="xing">*</em>
					开户行名称：
				</label>
				<select name="bankName">
					<option value="0">请选择</option>
				</select>
				<label>
					<em class="xing">*</em>
					账户号码：
				</label>
				<select name="bankAccount" >
					<option value="0">请选择</option>
				</select>
				<%--<label name="bankAccount"></label>--%>
			</span>
			<span id="domesticAccount">
				<label>
					<em class="xing">*</em>
					开户行名称：
				</label>
				<select name="bankName" >
					<option value="0">请选择</option>
				</select>
				<label>
					<em class="xing">*</em>
					账户号码：
				</label>
				<select name="bankAccount" >
					<option value="0">请选择</option>
				</select>
				<%--<label name="bankAccount"></label>--%>
			</span>
			<span id="overseasAccount">
				<label>
					<em class="xing">*</em>
					开户行名称：
				</label>
				<select name="bankName" >
					<option value="0">请选择</option>
				</select>
				<label>
					<em class="xing">*</em>
					账户号码：
				</label>
				<select name="bankAccount" >
					<option value="0">请选择</option>
				</select>
				<%--<label name="bankAccount"></label>--%>
			</span>
		</span>
		</div>
	</c:if>
	<div class="ydbz_tit">
		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				<span class="fl">个人宣传费</span>
			</c:when>
         <c:otherwise>
            <span class="fl">个人返佣</span>
          </c:otherwise>
     </c:choose>   
   	</div>
	<table class="activitylist_bodyer_table modifyPrice-table">
		   <thead>
			  <tr>
				 <th width="8%">姓名</th>
		         <th width="8%">币种</th>
		         <th width="11%">款项</th>
				 <th width="12%">应收金额</th>
				 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					<th width="12%">预计个人宣传费</th>
				 <!-- <th width="12%">原返佣金额</th> -->
				 	<th width="13%">宣传费差额</th>
				</c:when>
		         <c:otherwise>
		            <th width="12%">预计个人返佣</th>
				 <!-- <th width="12%">原返佣金额</th> -->
				 	<th width="13%">返佣差额</th>
		          </c:otherwise>
		     </c:choose>   
				 <th width="20%">备注</th>
				  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			       <c:choose>
			      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						 <th width="13%" style="display:none;">改后宣传费金额</th>
					</c:when>
			         <c:otherwise>
			           <th width="13%" style="display:none;">改后返佣金额</th>
			          </c:otherwise>
			     </c:choose>   
			  </tr>
		  	</thead>
  			<tbody>
  				<c:forEach items="${travelerRebatesList}" var="travelerRebates" varStatus="s">
  					<tr group="travler${s.count}">
						<td>${travelerRebates.traveler.name}<input type="hidden" name="travelerId" value="${travelerRebates.traveler.id}"></td>
						<td>
							<select name="gaijiaCurency" nowvalue="2">
								<c:forEach items="${currencyList}" var="currency">
									<option value="${currency.id}">${currency.currencyName}</option>
								</c:forEach>
							</select>
						</td>
						<td class="tc"><input name="costname" type="text" maxlength="50"/></td>
						<td class="tr">${fns:getMoneyAmountBySerialNum(travelerRebates.traveler.payPriceSerialNum,1)}</td>
						<!-- 20150812 预订个人返佣  start-->
						<td class="tr">${fns:getScheduleByUUID(travelerRebates.traveler.rebatesMoneySerialNum) }</td>
						<!-- 20150812 预订个人返佣  end -->
						<!-- 
						<td class="tr">${fns:getOldRebatesMoneyAmountBySerialNum(travelerRebates.oldRebates)}<input type="hidden" name="oldRebates" value="${travelerRebates.oldRebates}"></td> -->
						<td class="tr" style="display: none;">${fns:getMoneyAmountBySerialNum(travelerRebates.oldRebates,2)}<input type="hidden" name="oldRebates" value="${travelerRebates.oldRebates}"></td>
						<td class="tc">
							<dl class="huanjia">
								<dt><input name="plusys" type="text" maxlength="8" onkeyup="refundInput(this)" onafterpaste="refundInput(this))" /></dt>
								<dd>
									<%--UG_V2 按钮调整 20170315 by tlw--%>
									<%--<div class="ydbz_x" flag="appAll">应用全部</div>--%>
									<%--<div class="ydbz_x gray" flag="reset">清空</div>--%>
									<input class="btn btn-primary ydbz_x" type="button" flag="appAll" value="应用全部">
									<input class="btn ydbz_x" type="button" flag="reset" value="清空">
								</dd>
							</dl>
						</td>
		            	<td class="tc"><input name="remark" type="text" maxlength="50"/></td>
						<td class="tr" flag="afterys" style="display:none;">0</td>
					</tr>
  				</c:forEach>
			</tbody>
	</table>
	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
		<%--UG_V2 按钮调整 20170315 by tlw--%>
	<%--<div class="ydbz_x fl re-storeall">全部还原</div>--%>
		<input class="btn btn-primary ydbz_x fl re-storeall" type="button" value="全部还原">
			<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 <div class="fr f14 all-money">游客宣传费差额：<span id="totalTravelerPlus"></span></div>
			</c:when>
	         <c:otherwise>
	         	<div class="fr f14 all-money">游客返佣差额：<span id="totalTravelerPlus"></span></div>
	          </c:otherwise>
	     </c:choose>   
	</div>
	<div class="ydbz_tit">
		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 <span class="fl">团队宣传费</span>
			</c:when>
	         <c:otherwise>
	         	<span class="fl">团队返佣</span>
	          </c:otherwise>
	     </c:choose>   
		<!-- 20150812 预订团队返佣  start-->
		<c:if test="${not empty amount }">
			<c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 <label class="ydLable2 ydColor1" style="width: 100px;">预计团队宣传费：</label>
			</c:when>
	         <c:otherwise>
	         	<label class="ydLable2 ydColor1" style="width: 100px;">预计团队返佣：</label>
	          </c:otherwise>
	     </c:choose>   
			<span class="disabledshowspan">${currencyMark}</span>
			<span class="disabledshowspan">${amount }</span>
		</c:if>
		<!-- 20150812 预订团队返佣  end-->
	</div>
	<div>
		<ol class="gai-price-ol">
		   	<li>
				<i><input type="text" name="costname" maxlength="50" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>
				<i>
					<select name="teamCurrency">
						<c:forEach items="${currencyList}" var="currency">
							<option value="${currency.id}">${currency.currencyName}</option>
						</c:forEach>
					</select>
				</i>
				<i><input type="text" name="teamMoney" maxlength="8" class="gai-price-ipt1" flag="istips" onkeyup="refundInput(this)" onafterpast="refundInput(this)" value="" /><span class="ipt-tips ipt-tips2">费用</span></i>
				<i><input type="text" name="remark" maxlength="50" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>
				<%--UG_V2 按钮调整 20170315 by tlw--%>
				<%--<i><a class="ydbz_s gai-price-btn">+增加</a></i>--%>
				<i><input class="btn btn-primary ydbz_x gai-price-btn" type="button" value="增加"></i>
		    </li>
		</ol>
	</div>
	<div class="allzj tr f18">
		<c:forEach items="${teamList}" var="team">
			<span name='gaijiaCurencyOld' data='${team[2]}' style="display:none;">${team[1]}</span>
		</c:forEach>
			<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 原宣传费金额：<span id="totalBefore"></span><br />
					宣传费差额：<span id="totalPlus"></span><br />
					<div class="all-money" style="display:none;">改后宣传费金额：<span id="totalAfter"></span></div>
			</c:when>
	         <c:otherwise>
	         		原返佣金额：<span id="totalBefore"></span><br />
					返佣差额：<span id="totalPlus"></span><br />
					<div class="all-money" style="display:none;">改后返佣金额：<span id="totalAfter"></span></div>
	          </c:otherwise>
	     </c:choose>   
	</div>
	<div class="dbaniu" style="width:156px;">
	<%--UG_V2 按钮调整 20170315 by tlw--%>
	<%--<a class="ydbz_s" href="javascript:rebates.cancleRebates();">取消</a>--%>
		<input type="button" onclick="javascript:rebates.cancleRebates();" value="取消" class="btn">
		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	       <c:choose>
	      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				<input type="button" name="apply" value="申请宣传费" class="btn btn-primary">
			</c:when>
	         <c:otherwise>
	         	<input type="button" name="apply" value="申请返佣" class="btn btn-primary">
	          </c:otherwise>
	     </c:choose>   
		
	</div>
	<!--右侧内容部分结束-->
</body>
</html>