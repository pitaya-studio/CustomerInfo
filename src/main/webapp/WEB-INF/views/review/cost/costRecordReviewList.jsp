<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head-wholesaler.jsp"%>

<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>
<script type="text/javascript">
  $(function(){
    $(document).delegate(".downloadzfpz","click",function(){
      window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
    });
  });

  function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}

  function saveCheckBox(id){
    var tmp=0;
    $("input[name='"+id+"']").each(function(){
      if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
        tmp=tmp +","+$(this).attr('value');
      }
    });
    if(tmp=="0"){
      alert("请选择成本");
      return;
    }
      parent.$.jBox($("#batch-verify-list").html(), {
        title: "备注：", buttons: {'取消': 0,'提交': 2}, submit: function (v, h, f) {
          if(v == 2) {
            var reason = f.reason;
            $.ajax({
              type:"POST",
              url:"${ctx}/costReview/activity/batchApproveOrReject",
              data:{reviewIds:tmp,reason:reason,type:v},
              success:function(data){
                window.location.reload();
              },
              error:function(){
                alert('返回数据失败');
              }
            });
          }
        }, height: 250, width: 350
      });
    inquiryCheckBOX();
  }

  function milliFormat(s){//添加千位符
    if(/[^0-9\.\-]/.test(s)) return "invalid value";
    s=s.replace(/^(\d*)$/,"$1.");
    s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
    s=s.replace(".",",");
    var re=/(\d)(\d{3},)/;
    while(re.test(s)){
      s=s.replace(re,"$1,$2");
    }
    s=s.replace(/,(\d\d)$/,".$1");
    return s.replace(/^\./,"0.");
  }

  function expand(child,obj,id) {
    $.ajax({
      url:"${ctx}/cost/manager/payedRecord/",
      type:"POST",
      data:{id:id},
      success:function(data){
        var htmlstr="";
        var num = data.length;
        if(num>0){
          var str1='';
          for(var i =0;i<num;i++){
            var str = data[i].payvoucher.split("|");
            var idstr = data[i].ids.split("|");
            var index = str.length;
            if(index>0){
              for(var a=0;a<index;a++){
                str1+="<a class=\"downloadzfpz\" lang=\""+idstr[a]+"\">"+str[a]+"</a><br/>";
              }
            }
            htmlstr+="<tr><td class='tc'>"+data[i].payTypeName+"</td><td class='tc'>"+data[i].currency_mark+ milliFormat(parseFloat(data[i].amount).toFixed(2)) +"</td><td class='tc'>"+data[i].createDate+"</td><td class='tc'>"+"其它收入"+
                    "</td><td class='tc'>";
            if(data[i].isAsAccount == null) {
              htmlstr+="待收款";
            }else if(data[i].isAsAccount == 1) {
              htmlstr+="已达账";
            }else if(data[i].isAsAccount == 101) {
              htmlstr+="已撤销";
            }else if(data[i].isAsAccount == 102) {
              htmlstr+="已驳回";
            }
            htmlstr+="</td><td class='tc'>"+str1+"</td>"+"</tr>";
            str1='';
          }
        }
        $("#rpi").html(htmlstr);
      }
    });
    if ($(child).is(":hidden")) {
      $(obj).html("收起收款记录");
      $(obj).parents("tr").addClass("tr-hover");
      $(child).show();
      $(obj).parents("td").addClass("td-extend");
    } else {
      if (!$(child).is(":hidden")) {
        $(obj).parents("tr").removeClass("tr-hover");
        $(child).hide();
        $(obj).parents("td").removeClass("td-extend");
        $(obj).html("收款记录");
      }
    }
  }

</script>

<script type="text/javascript">
  function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}

  //运控-成本录入-添加项目--小计
  function costSums(obj,objshow,ordertype){
    var objMoney = {};
    obj.each(function(index, element) {
      var thisAccount = $(element).find("td[name='tdAccount']").text();
      if(thisAccount == '') {
        thisAccount = 1;
      }
      var thisPrice = $(element).find("td[name='tdPrice']").text();
      if(thisPrice.indexOf('-')!=-1) thisPrice = $(element).find("td[name='tdPrice']").next().next().next().text();
      var thisReview = $(element).find("td[name='tdReview']").text();
      var border=2;
      //去掉两边空格
      thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
      //找到金额中第一个数字位置
      for(var i=0;i<thisPrice.length;i++){
        if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
          border=i;
          break;
        }
      }
      var currencyName =thisPrice.substring(0,border).trim();
      //金额去掉第一个字符(币种)
      thisPrice=thisPrice.substring(border);
      if(thisPrice != ""){
    	  if(ordertype==2 || (ordertype==0 && trimStr(thisReview) != '已取消' && trimStr(thisReview) != '取消申请')|| (ordertype==1 && trimStr(thisReview) != '已取消' && (trimStr(thisReview) != '已驳回'&&trimStr(thisReview) != '审核失败(驳回)' && trimStr(thisReview) != '取消申请' && trimStr(thisReview) != '审批驳回') )){
    	        if(typeof objMoney[currencyName] == "undefined"){
    	          objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(new RegExp(",","gm"),""),10);
    	        }else{
    	          objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(new RegExp(",","gm"),""),10);
    	        }
    	   }
      }
    });
    //输出结果
    var strCurrency = "";
    var sign = " + ";
    for(var i in objMoney){
      var isNegative = /^\-/.test(objMoney[i]);
      if(isNegative){
        sign = " - ";
      }
      if(strCurrency != '' || (strCurrency == '' && isNegative)){
        strCurrency += sign;
      }
      strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');

    }
    if(objshow.length>0) objshow.text("  "+strCurrency);
  }

  $(function(){
	 
	    costSums($('tr.otherCost'),$('#otherCostShow'),2);
	    //实际成本录入-境内小计
	    costSums($('tr.budgetInCost'),$('#budgetInShow'),0);
	    //实际成本录入-境外小计
	    costSums($('tr.budgetOutCost'),$("#budgetOutShow"),0);
	    costSums($('tr.actualInCost'),$("#actualInShow"),1);
	    costSums($('tr.actualOutCost'),$("#actualOutShow"),1);

  });

  function callParentForAdd(budgetType) {
    if(budgetType == 2) {
      window.parent.addOtherCostHQX(budgetType);
    }else{
      window.parent.addCostHQX(budgetType);
    }
  }

  function callParentForUpdateOther(id) {
    window.parent.updateOtherCostHQX(id);
  }

  function callParentForUpdate(id) {
    window.parent.updateCostHQX(id);
  }

  function callParentDeleteCost(id, classType, groupId, visaId) {
    window.parent.deleteCost(id, classType, groupId, visaId);
  }

  function callParentCancelCost(id, classType) {
    window.parent.cancelCost(id, classType);
  }

  function callParentCancelPayCost(id) {
    window.parent.cancelPayCost(id);
  }

  function callParentDownload(ctx,costId,delFlag,hasVouchers) {
    window.parent.showDownloadWin(ctx,costId,delFlag,hasVouchers);
  }

  function callParentPassCost(dom,id) {
    window.parent.passCost(dom,id);
  }

  function callParentDenyCost(dom,id,reviewLevel) {
    window.parent.denyCost(dom,id,reviewLevel);
  }

  //需求0425
 $(function(){
	  if("${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}"=="true"){
		  var trs=$("#bugetTable").find("tr");
		  for(var i=0;i<trs.length;i++){
			  if($(trs[i]).find("td[name='costId']").text()=="${costId}"){
				  var tr=$(trs[i]);
				  if(tr.attr("class")=="budgetInCost"){
					  var trs=$(".budgetInCost");
					  var xiaoji=trs.last().next();
					  var td=trs.first().find("td").first();
					  tr.prepend(td);
					  tr.parent().prepend(xiaoji);
					  tr.parent().prepend(trs);
					  tr.parent().prepend(tr);
				  }else if(tr.attr("class")=="budgetOutCost"){
					  var trs=$(".budgetOutCost");
					  var xiaoji=trs.last().next();
					  var td=trs.first().find("td").first();
					  tr.prepend(td)
					  tr.parent().prepend(xiaoji);
					  tr.parent().prepend(trs);
					  tr.parent().prepend(tr);
				  }
			  }
		  }
		  var trss=$("#actualTable").find("tr");
		  for(var i=0;i<trss.length;i++){
			  if($(trss[i]).find("td[name='costId']").text()=="${costId}"){
				  var tr=$(trss[i]);
				  if(tr.attr("class")=="actualInCost"){
					  var trs=$(".actualInCost");
					  var xiaoji=trs.last().next();
					  var td=trs.first().find("td").first();
					  tr.prepend(td);
					  tr.parent().prepend(xiaoji);
					  tr.parent().prepend(trs);
					  tr.parent().prepend(tr);
				  }else if(tr.attr("class")=="actualOutCost"){
					  var trs=$(".actualOutCost");
					  var xiaoji=trs.last().next();
					  var td=trs.first().find("td").first();
					  tr.prepend(td)
					  tr.parent().prepend(xiaoji);
					  tr.parent().prepend(trs);
					  tr.parent().prepend(tr);
				  } 
			  }
		  }
		 /**/
	  }
  });
</script>
<div class="mod_information">
	<div class="mod_information_d">
		<div class="ydbz_tit">
			其它收入录入
			<c:if test="${activityGroup.lockStatus==1}">(已经锁定)</c:if>
		</div>
	</div>
</div>
<table
	class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
	<thead>
		<tr>
			<th width="7%">境内/外项目</th>
			<th width="10%">项目名称</th>
			<th width="10%">地接社/渠道商</th>
			<th width="7%">转换前<br>单价
			</th>
			<th width="7%">汇率</th>
			<th width="7%">转换后<br>总价
			</th>
			<th width="9%">已收金额<br>达账金额
			</th>
			<th width="7%">备注</th>
			<th width="7%">录入人</th>
			<th width="9%">操作</th>
		</tr>
	</thead>

	<c:forEach items="${otherCostList}" var="otherCost" varStatus="status">
		<tr class="otherCost">
			<c:if test="${status.count==1}">
				<td rowspan="${fn:length(otherCostList)}">收入明细</td>
			</c:if>
			<td class="tc" name="tdName">${otherCost.name}</td>
			<td class="tc" name="tdSupply">
				<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 --> <c:choose>
					<c:when
						test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && otherCost.supplyName=='非签约渠道'}"> 
			       直客
			   </c:when>
					<c:otherwise>
			      ${otherCost.supplyName}
			   </c:otherwise>
				</c:choose>
			</td>
			<td class="tr" name="tdPrice"><c:forEach items="${curlist}" var="currency">
					<c:if test="${currency.id==otherCost.currencyId}">
            ${currency.currencyMark}
          </c:if>
				</c:forEach> <fmt:formatNumber type="currency" pattern="#,##0.00"
					value="${otherCost.price}" /></td>
			<td class="tc">${otherCost.rate}</td>
			<td class="tr"><c:forEach items="${curlist}" var="currency">
					<c:if test="${currency.id==otherCost.currencyAfter}">
            ${currency.currencyMark}
          </c:if>
				</c:forEach> <fmt:formatNumber type="currency" pattern="#,##0.00"
					value="${otherCost.priceAfter}" /></td>
			<td class="p0 tr">
				<div class="yfje_dd">
					<span class="fbold"> <c:if
							test="${otherCost.payedMoney != '0.00' }">
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==otherCost.currencyId}">
                                      ${currency.currencyMark}
                                    </c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00"
								value="${otherCost.payedMoney }" />
						</c:if>
					</span>
				</div>
				<div class="dzje_dd">
					<span class="fbold"> <c:if
							test="${otherCost.confirmMoney != '0.00' }">
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==otherCost.currencyId}">
                                      ${currency.currencyMark}
                                    </c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00"
								value="${otherCost.confirmMoney }" />
						</c:if>
					</span>
				</div>
			</td>
			<td name="tdComment" class="tc">${fn:escapeXml(otherCost.comment)
				}</td>
			<td class="tc">${fns:getUserById(otherCost.createBy).name}</td>
			<td class="tc"><a
				onclick="expand('#child1',this,${otherCost.id})"
				href="javascript:void(0)">收款记录</a></td>
		</tr>
	</c:forEach>
	<c:if test="${! empty otherCostList}">
		<tr>
			<td>小计</td>
			<td colspan="12">&nbsp;<span id="otherCostShow"
				name="otherCostShow"></span></td>
		</tr>
	</c:if>
</table>

<div id="child1" class="activity_team_top1" style="display:none">
	<table id="teamTable"
		class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan"
		style="margin:0 auto;">
		<thead>
			<tr>
				<th class="tc" width="10%">收款方式</th>
				<th class="tc" width="10%">金额</th>
				<th class="tc" width="7%">日期</th>
				<th class="tc" width="5%">收款类型</th>
				<th class="tc" width="10%">状态确认</th>
				<th class="tc" width="8%">收款凭证</th>
			</tr>
		</thead>
		<tbody id='rpi'>

		</tbody>
	</table>
</div>

<div class="mod_information">
	<div class="mod_information_d">
		<div class="ydbz_tit">预算成本</div>
	</div>
</div>
<table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan" id="bugetTable">
		<thead>
			<tr>
				<th width="8%">境内/外项目</th>
				<th width="10%">项目名称</th>
				<th width="10%">地接社/渠道商</th>
				<th width="6%">数量</th>
				<th width="8%">转换前单价</th>
				<th width="6%">汇率</th>
				<th width="8%">转换前总价</th>
				<th width="8%">转换后总价</th>
				<th width="10%">备注</th>
				<th width="7%">录入人</th>
				<th width="10%">预算<br>审批状态</th>
				<c:if test="${read ne 1 }">
					<th width="6%">操作</th>
					<th width="5%">选择</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${budgetInList}" var="budgetIn" varStatus="status">
				<tr class="budgetInCost">
					<c:if test="${status.count==1}">
						<td rowspan="${fn:length(budgetInList)}">境内付款明细</td>
					</c:if>
					<td name="costId" style="display:none;">${budgetIn.costId }</td>
					<td name="tdName" class="tc">${budgetIn.name}</td>
					<td name="tdSupply" class="tc">
						<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && budgetIn.supplyName=='非签约渠道'}"> 
			      				直客
			  				</c:when>
							<c:otherwise>${budgetIn.supplyName}</c:otherwise>
						</c:choose>
					</td>
					<td name="tdAccount" class="tc">${budgetIn.quantity}</td>
					<td name="tdPrice" class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==budgetIn.currencyId}">
								${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetIn.price }" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tc">${budgetIn.rate }</td>
					<td  class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==budgetIn.currencyId}">
           						 ${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetIn.price * budgetIn.quantity}" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tr">￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetIn.priceAfter }" /></td>
					<td name="tdComment" class="tc">${budgetIn.comment}</td>
					<td class="tc">${fns:getUserNameById(budgetIn.createBy) }</td>
					<td class="tc" name="tdReview">
						<c:if test="${budgetIn.isNew eq 2 }">
							<c:choose>
								<c:when test="${budgetApproveStatus eq 1 and budgetEnble eq 1 and budgetIn.reviewType eq 0
										and budgetIn.review eq 2 and budgetIn.noReview eq 1}">
									无需审批
								</c:when>
								<c:when test="${budgetIn.reviewType ne 0 }">
					              ${fns:getChineseReviewStatus(budgetIn.status, budgetIn.current_reviewer)}
					            </c:when>
								<c:when test="${budgetIn.reviewType==0 && budgetIn.reviewId != -1 && not empty budgetIn.status && budgetIn.status != 2}">
					              ${fns:getChineseReviewStatus(budgetIn.status, budgetIn.current_reviewer)}
					            </c:when>
								<c:when test="${budgetIn.reviewType==0 and budgetIn.status eq 2 }">
					              审批通过
					            </c:when>
								<c:when test="${budgetIn.reviewType==0 and empty budgetIn.status }">
					              待提交审批
					            </c:when>
							</c:choose>
						</c:if>
						<c:if test="${budgetIn.isNew eq 1 }">
							<c:if test="${budgetIn.reviewType==0}">
					            ${fns:getNextCostReview(budgetIn.id)}
					        </c:if>
							<c:if test="${budgetIn.reviewType!=0}">
					            ${budgetIn.reviewStatus}
					        </c:if>
						</c:if>
					</td>
					<c:if test="${read ne 1 }">
						<td class="tc">
							<c:if test="${budgetType eq 0 }">
								<c:if test="${budgetIn.status eq 1 and fns:isCurReviewer(budgetIn.current_reviewer) and budgetIn.reviewType eq 0}">
									<a href="javascript:void(0)" onclick="callParentPassCost(this,'${budgetIn.reviewUuid}')">通过</a>&nbsp;
									<a href="javascript:void(0)" onclick="callParentDenyCost(this,'${budgetIn.reviewUuid}','0')">驳回</a>
								</c:if>
								<input type="hidden" id="itemId" value="${budgetIn.reviewUuid}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${budgetType eq 0 }">
								<c:if test="${budgetIn.status ==1 and fns:isCurReviewer(budgetIn.current_reviewer) and budgetIn.reviewType eq 0}">
									<input type="checkbox" value="${budgetIn.reviewUuid}" name="budgetId" />
								</c:if>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
			<c:if test="${! empty budgetInList}">
				<tr>
					<td>境内小计</td>
					<td colspan="12">&nbsp;<span id="budgetInShow" name="budgetInShow"></span></td>
				</tr>
			</c:if>
			<c:forEach items="${budgetOutList}" var="budgetOut" varStatus="status">
				<tr class="budgetOutCost">
					<c:if test="${status.count==1}">
						<td rowspan="${fn:length(budgetOutList)}">境外付款明细</td>
					</c:if>
					<td name="costId" style="display:none;">${budgetOut.costId }</td>
					<td name="tdName" class="tc">${budgetOut.name}</td>
					<td name="tdSupply" class="tc">
						<c:choose><%-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 --%>
							<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && budgetOut.supplyName=='非签约渠道'}"> 
			      				直客
			  				</c:when>
							<c:otherwise>${budgetOut.supplyName}</c:otherwise>
						</c:choose>
					</td>
					<td name="tdAccount" class="tc">${budgetOut.quantity}</td>
					<td name="tdPrice" class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==budgetOut.currencyId}">
           						 ${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetOut.price }"/>
							</c:if>
						</c:forEach>
					</td>
					<td class="tc">${budgetOut.rate }</td>
					<td  class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==budgetOut.currencyId}">
								${currency.currencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00"  value="${budgetOut.price * budgetOut.quantity}" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tr">￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetOut.priceAfter }" /></td>
					<td name="tdComment" class="tc">${budgetOut.comment}</td>
					<td class="tc">${fns:getUserNameById(budgetOut.createBy) }</td>
					<td class="tc" name="tdReview">
						<c:if test="${budgetOut.isNew eq 2 }">
							<c:choose>
								<c:when test="${budgetApproveStatus eq 1 and budgetEnble eq 1 and budgetOut.reviewType eq 0 and budgetOut.review eq 2 and budgetOut.noReview eq 1}">无需审批</c:when>
								<c:when test="${budgetOut.reviewType ne 0 }">
					              	${fns:getChineseReviewStatus(budgetOut.status, budgetOut.current_reviewer)}
					            </c:when>
								<c:when test="${budgetOut.reviewType==0 && budgetOut.reviewId != -1 && not empty budgetOut.status && budgetOut.status != 2}">
					              	${fns:getChineseReviewStatus(budgetOut.status, budgetOut.current_reviewer)}
					            </c:when>
								<c:when test="${budgetOut.reviewType==0 and budgetOut.status eq 2 }">
									 审批通过
           						 </c:when>
								<c:when test="${budgetOut.reviewType==0 and empty budgetOut.status }">
           							  待提交审批
            					</c:when>
							</c:choose>
						</c:if>
						<c:if test="${budgetOut.isNew eq 1 }">
							<c:if test="${budgetOut.reviewType==0}">
            					${fns:getNextCostReview(budgetOut.id)}
         					</c:if>
							<c:if test="${budgetOut.reviewType!=0}">
					            ${budgetOut.reviewStatus}
						  	</c:if>
						</c:if>
					</td>
					<c:if test="${read ne 1 }">
						<td class="tc">
							<c:if test="${budgetType eq 0 }">
								<c:if test="${budgetOut.status eq 1 and fns:isCurReviewer(budgetOut.current_reviewer) and budgetOut.reviewType eq 0 }">
									<a href="javascript:void(0)" onclick="callParentPassCost(this,'${budgetOut.reviewUuid}')">通过</a>&nbsp;
									<a href="javascript:void(0)" onclick="callParentDenyCost(this,'${budgetOut.reviewUuid}','0')">驳回</a>
								</c:if>
								<input type="hidden" id="itemId" value="${budgetOut.reviewUuid}" />
							</c:if>
						</td>
						<td>
							<c:if test="${budgetType eq 0 }">
								<c:if test="${budgetOut.status ==1 and fns:isCurReviewer(budgetOut.current_reviewer) and budgetOut.reviewType eq 0}">
									<input type="checkbox" value="${budgetOut.reviewUuid}" name="budgetId" />
								</c:if>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
			<c:if test="${! empty budgetOutList}">
				<tr>
					<td>境外小计</td>
					<td colspan="12">&nbsp;<span id="budgetOutShow" name="budgetOutShow"></span></td>
				</tr>
			</c:if>
		</tbody>
</table>
<div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
	<ul class="cost-ul" data-total="cost">
		<ul class="cost-ul" data-total="income">
			<li>预计总收入：&nbsp;￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney') }" /></li>
		</ul>
		<li>预计总成本：&nbsp;￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(budgetCost,'cost')}" /></li>
	</ul>
	<ul class="cost-ul" data-total="profit">
		<ul class="cost-ul" data-total="income">
			<li>预计退款合计：￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetrefund }" /></li>
		</ul>
		<li>预计总毛利：￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney')-fns:getSum(budgetCost,'cost') }" /></li>
	</ul>
	<c:if test="${budgetType eq 0 and read ne 1 }">
		<ul style="margin: 0px;float: right;">
			<input class="btn-primary" type="button" value="审批通过" onclick="saveCheckBox('budgetId')" />
		</ul>
	</c:if>
</div>

<div class="mod_information">
	<div class="mod_information_d">
		<div class="ydbz_tit">实际成本</div>
	</div>
</div>
<table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan" id="actualTable">
 		 <thead>
  			<tr>
				<th width="8%">境内/外项目</th>
				<th width="10%">项目名称</th>
				<th width="10%">地接社/渠道商</th>
				<th width="6%">数量</th>
				<th width="8%">转换前单价</th>
				<th width="6%">汇率</th>
				<th width="8%">转换前总价</th>
				<th width="8%">转换后总价</th>
				<th width="10%">备注</th>
				<th width="7%">录入人</th>
				<th width="8%">成本<br>审批状态</th>
				<c:if test="${read ne 1 }">
					<th width="6%">操作</th>
					<th width="5%">选择</th>
				</c:if>
				<c:if test="${read eq 1 }">
					<th width="7%">付款审<br>批状态</th>
					<th width="5%">操作</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${actualInList}" var="actualIn" varStatus="status">
				<tr class="actualInCost">
					<c:if test="${status.count==1}">
						<td rowspan="${fn:length(actualInList)}">境内付款明细</td>
					</c:if>
					<td name="costId" style="display:none">${actualIn.costId }</td>
					<td name="tdName" class="tc">${actualIn.name}</td>
					<td name="tdSupply" class="tc">
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && actualIn.supplyName=='非签约渠道'}"> 
			      				 直客
			  				</c:when>
							<c:otherwise>${actualIn.supplyName}</c:otherwise>
						</c:choose>
					</td>
					<td name="tdAccount" class="tc">${actualIn.quantity}</td>
					<td name="tdPrice" class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==actualIn.currencyId}">
            					${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualIn.price }" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tc">${actualIn.rate }</td>
					<td class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==actualIn.currencyId}">
            					${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualIn.price * actualIn.quantity}" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tr">￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualIn.priceAfter }" /></td>
					<td name="tdComment" class="tc">${actualIn.comment}</td>
					<td class="tc">${fns:getUserNameById(actualIn.createBy) }</td>
					<td class="tc" name="tdReview">
						<c:if test="${actualIn.isNew eq 2 }">
							<c:choose>
								<c:when test="${actualApproveStatus eq 1 and actualEnble eq 1 and actualIn.reviewType eq 0
											and actualIn.review eq 2 and actualIn.noReview eq 1}">
									无需审批
								</c:when>
								<c:when test="${actualIn.reviewType==0 and not empty actualIn.status }">
					              	${fns:getChineseReviewStatus(actualIn.status, actualIn.current_reviewer)}
					            </c:when>
								<c:when test="${actualIn.reviewType==0 and empty actualIn.status }">
					              	待提交审批
					            </c:when>
								<c:when test="${actualIn.reviewType!=0}">
					              	${fns:getChineseReviewStatus(actualIn.status, actualIn.current_reviewer)}
					            </c:when>
							</c:choose>
						</c:if> 
						<c:if test="${actualIn.isNew eq 1 }">
							<c:if test="${actualIn.reviewType==0}">
					            ${fns:getNextCostReview(actualIn.id)}
					        </c:if>
							<c:if test="${actualIn.reviewType!=0}">
								<font color="green">${actualIn.reviewStatus}</font>
							</c:if>
						</c:if>
					</td>
					<c:if test="${read ne 1 }">
						<td class="tc">
							<c:if test="${budgetType eq 1 }">
								<c:if test="${actualIn.status eq 1 and fns:isCurReviewer(actualIn.current_reviewer) and actualIn.reviewType eq 0}">
									<a href="javascript:void(0)" onclick="callParentPassCost(this,'${actualIn.reviewUuid}')">通过</a>&nbsp;
									<a href="javascript:void(0)" onclick="callParentDenyCost(this,'${actualIn.reviewUuid}','0')">驳回</a>
								</c:if>
							</c:if> <br /> 
							<a onclick="showDownloadWin('${ctx}',${actualIn.id },0,'${actualIn.costVoucher }')">下载附件</a>
						</td>
						<td>
							<c:if test="${budgetType eq 1 }">
								<c:if test="${actualIn.status ==1 and fns:isCurReviewer(actualIn.current_reviewer) and actualIn.reviewType eq 0}">
									<input type="checkbox" value="${actualIn.reviewUuid}" name="actualId" />
								</c:if>
							</c:if>
						</td>
					</c:if>
					<c:if test="${read eq 1 }">
						<td class="tc">
							<c:choose>
								<c:when test="${payApproveStatus eq 1 and actualIn.reviewType eq 0 and actualIn.payReview eq 2 and actualIn.noPayReview eq 1}">无需审批</c:when>
								<c:when test="${actualIn.reviewType==2}">
									<c:choose>
										<c:when test="${actualIn.reviewStatus eq '已取消'}">取消申请</c:when>
										<c:when test="${actualIn.reviewStatus eq '已驳回'}">审批驳回</c:when>
										<c:otherwise>${actualIn.reviewStatus}</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${empty actualIn.pay_status }">待提交审批</c:when>
								<c:otherwise>
					              	${fns:getChineseReviewStatus(actualIn.pay_status, actualIn.pay_current_reviewer)}
					            </c:otherwise>
							</c:choose>
						</td>
						<td class="tc">
							<a onclick="showDownloadWin('${ctx}',${actualIn.id },0,'${actualIn.costVoucher }')">下载附件</a>
						</td>
					</c:if>
				</tr>
			</c:forEach>

			<c:if test="${! empty actualInList}">
				<tr>
					<td>境内小计</td>
					<td colspan="12">&nbsp;<span id="actualInShow" name="actualInShow"></span></td>
				</tr>
			</c:if>
			<c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
				<tr class="actualOutCost">
					<c:if test="${status.count==1}">
						<td rowspan="${fn:length(actualOutList)}">境外付款明细</td>
					</c:if>
					<td name="costId" style="display:none">${actualOut.costId }</td>
					<td name="tdName" class="tc">${actualOut.name}</td>
					<td name="tdSupply" class="tc">
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && actualOut.supplyName=='非签约渠道'}"> 
			      				直客
			  				</c:when>
							<c:otherwise>${actualOut.supplyName}</c:otherwise>
						</c:choose>
					</td>
					<td name="tdAccount" class="tc">${actualOut.quantity}</td>
					<td name="tdPrice" class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==actualOut.currencyId}">
            					${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualOut.price }" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tc">${actualOut.rate }</td>
					<td class="tr">
						<c:forEach items="${curlist}" var="currency">
							<c:if test="${currency.id==actualOut.currencyId}">
            					${currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualOut.price * actualOut.quantity}" />
							</c:if>
						</c:forEach>
					</td>
					<td class="tr">￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualOut.priceAfter }"/></td>
					<td name="tdComment" class="tc">${actualOut.comment}</td>
					<td class="tc">${fns:getUserNameById(actualOut.createBy) }</td>
					<td class="tc" name="tdReview" width="8%">
						<c:if test="${actualOut.isNew eq 2 }">
							<c:choose>
								<c:when test="${actualApproveStatus eq 1 and actualEnble eq 1 and actualOut.reviewType eq 0
											and actualOut.review eq 2 and actualOut.noReview eq 1}">
									无需审批
								</c:when>
								<c:when test="${actualOut.reviewType==0 and not empty actualOut.status }">
					              	${fns:getChineseReviewStatus(actualOut.status, actualOut.current_reviewer)}
					            </c:when>
								<c:when test="${actualOut.reviewType==0 and empty actualOut.status }">
					              	待提交审批
					            </c:when>
								<c:when test="${actualOut.reviewType!=0}">
						              ${fns:getChineseReviewStatus(actualOut.status, actualOut.current_reviewer)}
						        </c:when>
							</c:choose>
						</c:if> 
						<c:if test="${actualOut.isNew eq 1 }">
							<c:if test="${actualOut.reviewType==0}">
					            ${fns:getNextCostReview(actualOut.id)}
					        </c:if>
							<c:if test="${actualOut.reviewType!=0}">
								<font color="green">${actualOut.reviewStatus}</font>
							</c:if>
						</c:if>
					</td>
					<c:if test="${read ne 1 }">
						<td class="tc"><c:if test="${budgetType eq 1 }">
								<c:if test="${actualOut.status eq 1 and fns:isCurReviewer(actualOut.current_reviewer) and actualOut.reviewType eq 0}">
									<a href="javascript:void(0)" onclick="callParentPassCost(this,'${actualOut.reviewUuid}')">通过</a>&nbsp;
									<a href="javascript:void(0)" onclick="callParentDenyCost(this,'${actualOut.reviewUuid}','0')">驳回</a>
								</c:if>
							</c:if> <br />
							<a onclick="showDownloadWin('${ctx}',${actualOut.id },0,'${actualOut.costVoucher }')">下载附件</a>
						</td>
						<td>
							<c:if test="${budgetType eq 1 }">
								<c:if test="${actualOut.status ==1 and fns:isCurReviewer(actualOut.current_reviewer) and actualOut.reviewType eq 0}">
									<input type="checkbox" value="${actualOut.reviewUuid}" name="actualId" />
								</c:if>
							</c:if>
						</td>
					</c:if>
					<c:if test="${read eq 1 }">
						<td class="tc">
							<c:choose>
								<c:when test="${payApproveStatus eq 1 and actualOut.reviewType eq 0 and actualOut.payReview eq 2
												and actualOut.noPayReview eq 1}">
									无需审批
								</c:when>
								<c:when test="${actualOut.reviewType==2}">
									<c:choose>
										<c:when test="${actualOut.reviewStatus eq '已取消'}">取消申请</c:when>
										<c:when test="${actualOut.reviewStatus eq '已驳回'}">审批驳回</c:when>
										<c:otherwise>${actualOut.reviewStatus}</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${empty actualOut.pay_status }">待提交审批</c:when>
								<c:otherwise>
					              	${fns:getChineseReviewStatus(actualOut.pay_status, actualOut.pay_current_reviewer)}
					            </c:otherwise>
							</c:choose>
						</td>
						<td class="tc">
							<a onclick="showDownloadWin('${ctx}',${actualOut.id },0,'${actualOut.costVoucher }')">下载附件</a>
						</td>
					</c:if>
				</tr>
			</c:forEach>
			<c:if test="${not empty actualOutList}">
				<tr>
					<td>境外小计</td>
					<td colspan="12">&nbsp;<span id="actualOutShow" name="actualOutShow"></span></td>
				</tr>
			</c:if>
		</tbody>
</table>
<div class="costSum  clearfix" style="width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
	<ul class="cost-ul" data-total="cost">
		<ul class="cost-ul" data-total="income">
			<li>实际总收入：&nbsp;¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney') }" /></li>
		</ul>
		<li>实际总成本：&nbsp;¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(actualCost,'cost')}" /></li>
	</ul>
	<ul class="cost-ul" data-total="profit">
		<ul class="cost-ul" data-total="income">
			<li>退款合计：¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualrefund }" /></li>
		</ul>
		<li>实际总毛利：¥<fmt:formatNumber type="currency" pattern="#,##0.00"
			 value="${fns:getSum(incomeList,'accountedMoney')-fns:getSum(actualCost,'cost') }" />
		</li>
	</ul>
	<c:if test="${budgetType eq 1 and read ne 1}">
		<ul style="margin: 0px;float: right;">
			<input class="btn-primary" type="button" value="审批通过" onclick="saveCheckBox('actualId')" />
		</ul>
	</c:if>
</div>
<%@ include file="/WEB-INF/views/review/cost/service_charge_list.jsp" %>

<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
	<table width="100%" style="padding:10px !important; border-collapse: separate;">
		<tr><td></td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td><p>请填写您的审批备注！</p></td>
		</tr>
		<tr>
			<td><label><textarea name="reason" id="reason" style="width: 290px;" maxlength="200"></textarea></label></td>
		</tr>
	</table>
</div>