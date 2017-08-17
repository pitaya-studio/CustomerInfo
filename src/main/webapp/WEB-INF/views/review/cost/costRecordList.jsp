<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head-wholesaler.jsp" %>

<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>

<script type="text/javascript">

  $(function(){
    $(document).delegate(".downloadzfpz","click",function(){
      window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
    });
  });

  function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}

  function saveCheckBox(id,budgetType){
    var tmp=0;
    var visaTemp=0;
    var msg = "";
    $("input[name='"+id+"']").each(function(){
      if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
        tmp=tmp +","+$(this).attr('value');
        visaTemp=visaTemp +","+$(this).attr('id');
        if(${companyUuid eq DHJQ}) {
          if ($(this).parent().next().html() == "") {
            msg = "请上传附件后重新提交";
          }
        }
      }
    });

    if(tmp=="0"){
      if (budgetType=='0')  alert("请选择预算成本");
      else alert("请选择实际成本");
      return;
    }

    if(msg != ""){
      alert(msg);
      return false;
    }

    if(${orderType eq 6}) {
      $.ajax({
        type: "POST",
        url: "${ctx}/costReview/visa/visaCostApply",
        cache:false,
        //dataType:"json",
        async:false,
        data:{
          costList:tmp,visaIds:visaTemp,groupId:"",orderType:"", deptId : '${deptId}', activityId : '${activityGroup.id}'},
        success: function(data){
          top.$.jBox.tip(data,'success');
          window.location.reload();
        },
        error : function(e){
          alert('请求失败。');
          return false;
        }
      });
    }else if(${orderType eq 7}){
      $.ajax({
        type: "POST",
        url: "${ctx}/costReview/airticket/airticketCostApply",
        cache:false,
// 			dataType:"json",
        async:false,
        data:{
          costList:tmp,visaIds:"",groupId:"",orderType:"",activityId : '${activityGroup.id}',deptId : '${deptId}' },
        success: function(data){
          $.jBox.tip(data, 'success');
          window.location.reload();
        },
        error : function(e){
          alert('请求失败。');
          return false;
        }
      });
    }else{
      $.ajax({
        type: "POST",
        url: "${ctx}/costReview/activity/costApply",
        cache:false,
        async:false,
        data:{costList:tmp, visaIds:visaTemp, groupId:${activityGroup.id}, orderType : '${orderType}', deptId : '${deptId}', activityId : '${activityId}'},
        success: function(data){
          top.$.jBox.tip(data,'success');
          window.location.reload();
        },
        error : function(e){
          alert('请求失败。');
          return false;
        }
      });
    }

  }

  function payCheckBox(id,budgetType){
    var tmp= '';
    $("input[name='"+id+"']").each(function(){
      if ($(this).is(":checked")){
        if(!tmp){
          tmp = $(this).val();
        }else{
          tmp = tmp + "," + $(this).val();
        }
      }
    });
    if(!tmp){
      alert("请选择需要付款审批的成本项");
      return false;
    }
    if(${orderType eq 6}) {
      $.ajax({
        type: "POST",
        url: "${ctx}/review/visa/payment/apply",
        cache:false,
        dataType:"json",
        async:false,
        data:{items:tmp},
        success: function(data){
          if(data.flag){
            $.jBox.tip('申请付款成功', 'success');
            window.location.reload();
          }else{
            $.jBox.tip('付款申请失败，' + data.msg, 'error');
            return false;
          }
        },
        error : function(e){
          $.jBox.tip('申请付款失败', 'error');
          return false;
        }
      });
    }else if(${orderType eq 7}){
      $.ajax({
        type: "POST",
        url: "${ctx}/review/airticket/payment/apply",
        cache:false,
        dataType:"json",
        async:false,
        data:{items:tmp},
        success: function(data){
          if(data.flag){
            $.jBox.tip('申请付款成功', 'success');
            window.location.reload();
          }else{
            $.jBox.tip('付款申请失败，' + data.msg, 'error');
            return false;
          }
        },
        error : function(e){
          $.jBox.tip('申请付款失败', 'error');
          return false;
        }
      });
    }else{
      $.ajax({
        type: "POST",
        url: "${ctx}/review/activity/payment/apply",
        cache:false,
        dataType:"json",
        async:false,
        data:{items:tmp},
        success: function(data){
          if(data.flag){
            $.jBox.tip('申请付款成功', 'success');
            window.location.reload();
          }else{
            $.jBox.tip('付款申请失败，原因如下：' + data.msg, 'error');
            return false;
          }
        },
        error : function(e){
          $.jBox.tip('申请付款失败', 'error');
          return false;
        }
      });
    }

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
      //var currencyName = $(element).find("td[name='tdCurrencyName']").text();
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
</script>
<%--bug 16766--%>
<%--添加了一个body overflow:hidden; 解决运控-成本录入-散拼产品滚动条 --%>
<body style="overflow: hidden;">
<div class="mod_information">
  <div class="mod_information_d">
    <div class="ydbz_tit">
      其它收入录入 <c:if test="${activityGroup.lockStatus==1}">(已经锁定)</c:if>
      <div class="button_addcb wpr20 xtjxm">
        <c:if test="${activityGroup.lockStatus==0}">
          <c:if test="${companyId!=71 || companyId==71 && isOperator==1}"><a class="button_addcb_a" onclick="callParentForAdd('2')">添加项目</a></c:if>
        </c:if>
      </div>
    </div>
  </div>
</div>
<table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
	<thead>
		<tr>
			<th class="tc" width="7%">境内/外项目</th>
			<th class="tc" width="10%">项目名称</th>
			<c:if test="${companyUuid eq TMYT }">
				<th class="tc" width="6%">是否为kb款</th>
			</c:if>
			<th class="tc" width="10%">地接社/渠道商</th>
			<th class="tc" width="7%">转换前<br>单价</th>
			<th class="tc" width="7%">汇率</th>
			<th class="tc" width="7%">转换后<br>总价</th>
			<th class="tc" width="9%">已收金额<br>达账金额</th>
			<th class="tc" width="7%">备注</th>
			<th class="tc" width="7%">录入人</th>
			<th class="tc" width="9%">操作</th>
		</tr>
	</thead>

	<c:forEach items="${otherCostList}" var="otherCost" varStatus="status">
		<tr class="otherCost">
			<c:if test="${status.count==1}">
				<td rowspan="${fn:length(otherCostList)}">收入明细</td>
			</c:if>
			<td class="tc" name="tdName">${otherCost.name}</td>
			<c:if test="${companyUuid eq TMYT }">
				<td class="tc"><c:if test="${otherCost.kb eq 0 }">否</c:if> <c:if test="${otherCost.kb eq 1 }">是</c:if></td>
			</c:if>
			<td class="tc" name="tdSupply">
				<c:choose>
					<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' && otherCost.supplyName=='非签约渠道'}">
		  				直客
		  			</c:when>
					<c:otherwise>
		  				${otherCost.supplyName}
		  			</c:otherwise>
				</c:choose>
			</td>
			<td class="tr" name="tdPrice">
				<c:forEach items="${curlist}" var="currency">
					<c:if test="${currency.id==otherCost.currencyId}">
           				${currency.currencyMark}
          			</c:if>
				</c:forEach>
				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.price}" />
			</td>
			<td class="tc">${otherCost.rate}</td>
			<td class="tr">
				<c:forEach items="${curlist}" var="currency">
					<c:if test="${currency.id==otherCost.currencyAfter}">
            			${currency.currencyMark}
          			</c:if>
				</c:forEach>
				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.priceAfter}" />
			</td>
			<td class="p0 tr">
				<div class="yfje_dd">
					<span class="fbold"> 
						<c:if test="${otherCost.payedMoney != '0.00' }">
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==otherCost.currencyId}">
                                      ${currency.currencyMark}
								</c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.payedMoney }" />
						</c:if>
					</span>
				</div>
				<div class="dzje_dd">
					<span class="fbold">
						<c:if test="${otherCost.confirmMoney != '0.00' }">
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==otherCost.currencyId}">
                                      ${currency.currencyMark}
								</c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.confirmMoney }" />
						</c:if> 
					</span>
				</div>
			</td>
			<td name="tdComment" class="tc">${fn:escapeXml(otherCost.comment)}</td>
			<td class="tc">${fns:getUserById(otherCost.createBy).name}</td>
			<td class="tc">
				<c:if test="${activityGroup.lockStatus==0 }">
					<c:if test="${companyId!=71 || companyId==71 && isOpt!=1 }">
						<a href="javascript:void(0)" onclick="callParentForUpdateOther('${otherCost.id}')">修改</a>&nbsp;&nbsp;
                        <!-- 修改bug 15568 对于已收款的其他收入不能再删除 update by shijun.liu -->
                        <c:if test="${empty otherCost.payedMoney or '0.00' eq otherCost.payedMoney }">
						  <a href="javascript:void(0)" onclick="callParentDeleteCost('${otherCost.id}','operator','','')">删除</a>
                        </c:if>
					</c:if>
				</c:if> 
				<c:if test="${activityGroup.lockStatus==1}">已经锁定</c:if> <br>
				<a href="${ctx}/cost/manager/paymentConfirm/${otherCost.id}/${otherCost.orderType}?payType=3&groupId=${otherCost.activityId }&orderType=${otherCost.orderType}&supplyName=${otherCost.supplyName}"
					target="_blank">收款</a>&nbsp;&nbsp; 
				<a onclick="expand('#child1',this,${otherCost.id})" href="javascript:void(0)">收款记录</a>
			</td>
		</tr>
	</c:forEach>
	<c:if test="${! empty otherCostList}">
   		<tr>
      		<td>小计</td>
      		<td colspan="12">&nbsp;<span id="otherCostShow" name="otherCostShow"></span></td>
    	</tr>
 	 </c:if>
</table>

<div id="child1" class="activity_team_top1" style="display:none">
	<table id="teamTable" class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan"
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
    	<div class="ydbz_tit">预算成本录入 
    		<c:if test="${activityGroup.lockStatus==1 or (companyUuid eq LMT and budgetLock eq 1 )}">(已经锁定)</c:if>
      		<div class="button_addcb wpr20 xtjxm">
        		<c:if test="${(companyUuid ne LMT and activityGroup.lockStatus eq 0) or (companyUuid eq LMT and budgetLock eq 0 and activityGroup.lockStatus eq 0 )}">
          			<c:if test="${companyId!=71 || companyId==71 && isOperator==1}">
          				<a class="button_addcb_a" onclick="callParentForAdd('0')">添加项目</a>
          			</c:if>
        		</c:if>
      		</div>
    	</div>
  	</div>
</div>
<table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
		<thead>
			<tr>
				<th class="tc" width="7%">境内/外项目</th>
				<th class="tc" width="9%">项目名称</th>
				<th class="tc" width="10%">地接社/渠道商</th>
				<th class="tc" width="5%">数量</th>
				<th class="tc" width="6%">转换前<br>单价</th>
				<th class="tc" width="6%">汇率</th>
				<th class="tc" width="6%">转换前<br>总价</th>
				<th class="tc" width="6%">转换后<br>总价</th>
				<th class="tc" width="7%">备注</th>
				<th class="tc" width="6%">录入人</th>
				<th class="tc" width="8%">预算审<br>批状态</th>
				<th class="tc" width="9%">操作</th>
				<th class="tc" width="5%">选择</th>
			</tr>
		</thead>
		<c:forEach items="${budgetInList}" var="budgetIn" varStatus="status">
			<tr class="budgetInCost">
				<c:if test="${status.count==1}">
					<td rowspan="${fn:length(budgetInList)}">境内付款明细</td>
				</c:if>
				<td class="tc" name="tdName">${budgetIn.name}</td>
				<td class="tc" name="tdSupply">
					<c:choose>
						<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && budgetIn.supplyName=='非签约渠道'}">
							直客
						</c:when>
						<c:otherwise>
							${budgetIn.supplyName}
						</c:otherwise>
					</c:choose>
				</td>
				<td class="tc" name="tdAccount">${budgetIn.quantity}</td>
				<td class="tr" name="tdPrice">
					<c:choose>
						<c:when test="${budgetIn.name eq '其他'}">-</c:when>
						<c:otherwise>
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==budgetIn.currencyId}">
               						${currency.currencyMark}
              					</c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetIn.price}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="tc">${budgetIn.rate}</td>
				<td class="tr" name="tdPriceSum">
					<c:choose>
						<c:when test="${budgetIn.name eq '其他'}">-</c:when>
						<c:otherwise>
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==budgetIn.currencyId}">
                					${currency.currencyMark}
              					</c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetIn.price * budgetIn.quantity}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="tr">
					<c:forEach items="${curlist}" var="currency">
						<c:if test="${currency.id==budgetIn.currencyAfter}">
            				${currency.currencyMark}
          				</c:if>
					</c:forEach>
					<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetIn.priceAfter}" />
				</td>
				<td name="tdComment" class="tc">${budgetIn.comment}</td>
				<td class="tc">${fns:getUserById(budgetIn.createBy).name}</td>
				<td class="tc" name="tdReview">
					<!-- reviewId 目前仅仅用于拉美途 签证子订单的需求C310，如果reviewId==-1表示是统计出来的数据，不是取自cost_record -->
					<c:if test="${budgetIn.isNew eq 2 }">
						<c:choose>
							<c:when test="${budgetApproveStatus eq 1 and budgetEnble eq 1 and budgetIn.reviewType eq 0
							        and budgetIn.review eq 2 and budgetIn.noReview eq 1 }">
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
						<c:if test="${budgetIn.reviewType==0 && budgetIn.reviewId != -1}">
            				${fns:getNextCostReview(budgetIn.id)}
          				</c:if>
						<c:if test="${budgetIn.reviewType!=0}">
            				${budgetIn.reviewStatus}
          				</c:if>
						<c:if test="${budgetIn.reviewId eq -1 }">
							<c:if test="${budgetIn.review eq 2 }">审核通过</c:if>
							<c:if test="${budgetIn.review eq 4 }">待提交审核</c:if>
						</c:if>
					</c:if>
				</td>
				<td class="tc">
					<c:if test="${((companyUuid ne LMT and activityGroup.lockStatus eq 0)
					    or (companyUuid eq LMT and budgetLock eq 0 and activityGroup.lockStatus eq 0 )) && budgetIn.reviewType==0 }">
						<c:choose>
							<c:when test="${budgetIn.reviewId eq -1 and budgetIn.status ne 2 }">
								<a href="javascript:void(0)" onclick="callParentDeleteCost('${budgetIn.id}','operator','${activityGroup.id }','${budgetIn.reviewId }')">删除</a>
							</c:when>
							<c:otherwise>
								<c:if test="${companyId!=71 || companyId==71 && isOperator==1 }">
									<c:if test="${(empty budgetIn.status && budgetIn.review eq 4) or budgetIn.status eq 0
									      or budgetIn.review eq 0 or budgetIn.status eq 3 or budgetIn.review eq 5}">
										<a href="javascript:void(0)" onclick="callParentForUpdate('${budgetIn.id}')">修改</a>&nbsp;&nbsp;
										<a href="javascript:void(0)" onclick="callParentDeleteCost('${budgetIn.id}','operator','','')">删除</a>
									</c:if>
									<c:if test="${budgetIn.status eq 1 and fns:isShowCancel(budgetIn.reviewUuid)}">
										<a href="javascript:void(0)" onclick="callParentCancelCost('${budgetIn.reviewUuid}','operator')">取消</a>
									</c:if>
								</c:if>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${activityGroup.lockStatus==1 or (companyUuid eq LMT and budgetLock eq 1 )}">
          				已经锁定
        			</c:if>
        			<input type="hidden" id="itemId" value="${budgetIn.id}" />
        		</td>
				<td>
					<c:if test="${ budgetIn.reviewType==0 && ((empty budgetIn.status && budgetIn.review eq 4) ||budgetIn.status eq 3 || budgetIn.review eq 5)
					  && ((companyUuid ne LMT and activityGroup.lockStatus eq 0) or (companyUuid eq LMT and budgetLock eq 0 and activityGroup.lockStatus eq 0 )) }">
						<input type="checkbox" value="${budgetIn.id}" id="<c:if test="${budgetIn.reviewId eq -1 }">${budgetIn.id }</c:if>" name="budgetId" />
					</c:if>
				</td>
			</tr>
		</c:forEach>

		<c:if test="${! empty budgetInList}">
			<tr>
				<td class="tc">境内小计</td>
				<td colspan="13">&nbsp;<span id="budgetInShow" name="budgetInShow"></span></td>
			</tr>
		</c:if>

		<c:forEach items="${budgetOutList}" var="budgetOut" varStatus="status">
			<tr class="budgetOutCost">
				<c:if test="${status.count==1}">
					<td rowspan="${fn:length(budgetOutList)}">境外付款明细</td>
				</c:if>
				<td class="tc" name="tdName">${budgetOut.name}</td>
				<td class="tc" name="tdSupply">
					<c:choose>
						<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && budgetOut.supplyName=='非签约渠道'}">
							直客
						</c:when>
						<c:otherwise>
							${budgetOut.supplyName}
						</c:otherwise>
					</c:choose>
				</td>
				<td class="tc" name="tdAccount">${budgetOut.quantity}</td>
				<td class="tr" name="tdPrice">
					<c:choose>
						<c:when test="${budgetOut.name eq '其他'}">-</c:when>
						<c:otherwise>
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==budgetOut.currencyId}">
                					${currency.currencyMark}
              					</c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetOut.price}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td class="tc">${budgetOut.rate}</td>
				<td class="tr" name="tdPriceSum">
                  <c:choose>
						<c:when test="${budgetOut.name eq '其他'}">-</c:when>
						<c:otherwise>
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.id==budgetOut.currencyId}">
                					${currency.currencyMark}
              					</c:if>
							</c:forEach>
							<fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetOut.price * budgetOut.quantity}" />
						</c:otherwise>
                  </c:choose>
				</td>
				<td class="tr">
                  <c:forEach items="${curlist}" var="currency">
						<c:if test="${currency.id==budgetOut.currencyAfter}">${currency.currencyMark}</c:if>
                  </c:forEach>
                  <fmt:formatNumber type="currency" pattern="#,##0.00" value="${budgetOut.priceAfter}" />
                </td>
				<td name="tdComment" class="tc">${budgetOut.comment}</td>
				<td class="tc">${fns:getUserById(budgetOut.createBy).name}</td>
				<td class="tc" name="tdReview">
					<c:if test="${budgetOut.isNew eq 2 }">
						<c:choose>
							<c:when test="${budgetApproveStatus eq 1 and budgetEnble eq 1 and budgetOut.reviewType eq 0
							        and budgetOut.review eq 2 and budgetOut.noReview eq 1}">
                                无需审批
                            </c:when>
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
				<td class="tc">
					<c:if test="${((companyUuid ne LMT and activityGroup.lockStatus eq 0) or
					        (companyUuid eq LMT and budgetLock eq 0 and activityGroup.lockStatus eq 0 )) && budgetOut.reviewType==0}">
						<c:if test="${companyId!=71 || companyId==71 && isOperator==1}">
							<c:if test="${(empty budgetOut.status && budgetOut.review eq 4) or budgetOut.status eq 0 or
							      budgetOut.review eq 0 or budgetOut.status eq 3 or budgetOut.review eq 5}">
								<a href="javascript:void(0)" onclick="callParentForUpdate('${budgetOut.id}')">修改</a>&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="callParentDeleteCost('${budgetOut.id}','operator','','')">删除</a>
							</c:if>
							<c:if test="${budgetOut.status eq 1 and fns:isShowCancel(budgetOut.reviewUuid)}">
								<a href="javascript:void(0)" onclick="callParentCancelCost('${budgetOut.reviewUuid}','operator')">取消</a>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${activityGroup.lockStatus==1 or (companyUuid eq LMT and budgetLock eq 1 )}">
          				已经锁定
        			</c:if>
        			<input type="hidden" id="itemId" value="${budgetOut.id}" />
                </td>
                <td>
                    <c:if test="${budgetOut.reviewType==0 && ((empty budgetOut.status && budgetOut.review eq 4) or budgetOut.status eq 3 or budgetOut.review eq 5)
                        && ((companyUuid ne LMT and activityGroup.lockStatus eq 0) or (companyUuid eq LMT and budgetLock eq 0 and activityGroup.lockStatus eq 0 ))}">
                      <input type="checkbox" value="${budgetOut.id}" id="" name="budgetId" />
                    </c:if>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${! empty budgetOutList}">
			<tr>
				<td class="tc">境外小计</td>
				<td colspan="13">&nbsp;<span id="budgetOutShow" name="budgetOutShow"></span></td>
			</tr>
		</c:if>
</table>
<div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
	<ul class="cost-ul" data-total="cost">
    	<ul class="cost-ul" data-total="income">
      		<li>预计总收入：&nbsp;¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney') }"/></li>
    	</ul>
    	<li>预计总成本：&nbsp;¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(budgetCost,'cost')}"/></li>
  	</ul>
  	<ul class="cost-ul" data-total="profit">
    	<ul class="cost-ul" data-total="income">
      	  <li>预计退款合计：¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${budgetrefund }"/></li>
    	</ul>
    	<li>预计总毛利：¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney')-fns:getSum(budgetCost,'cost')-budgetrefund }"/>
    	</li>
  	</ul>
  	<ul style="margin: 0px;float: right;">
    	<c:if test="${(companyUuid ne LMT and activityGroup.lockStatus eq 0) or (companyUuid eq LMT and budgetLock eq 0 and activityGroup.lockStatus eq 0 )}">
      		<input class="btn-primary" type="button" value="提交预算成本" onclick="saveCheckBox('budgetId','0')" />
    	</c:if>
  	</ul>
</div>

<c:if test="${companyId!=71 || companyId==71 && isOpt==1}">
  <div class="mod_information">
    <div class="mod_information_d">
        <div class="ydbz_tit">
            实际成本录入<c:if test="${activityGroup.lockStatus==1}">(已经锁定)</c:if>
            <div class="button_addcb wpr20 xtjxm">
                <c:if test="${ activityGroup.lockStatus ==0}">
                    <a onclick="callParentForAdd('1')" class="button_addcb_a">添加项目</a>
                </c:if>
            </div>
        </div>
    </div>
  </div>
  <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
    <thead>
        <tr>
            <th class="tc" width="7%">境内/外项目</th>
            <th class="tc" width="8%">项目名称</th>
            <th class="tc" width="7%">地接社/渠道商</th>
            <th class="tc" width="5%">数量</th>
            <th class="tc" width="5%">转换前<br>单价</th>
            <th class="tc" width="6%">汇率</th>
            <th class="tc" width="5%">转换前<br>总价</th>
            <th class="tc" width="6%">转换后<br>总价</th>
            <th class="tc" width="7%">备注</th>
            <th class="tc" width="6%">录入人</th>
            <th class="tc" width="7%">成本审<br>批状态</th>
            <th class="tc" width="5%">实际成本<br>申请</th>
            <th class="tc" style="display:none;">附件id</th>
            <th class="tc" width="8%">操作</th>
            <th class="tc" width="7%">付款审<br>批状态</th>
            <th class="tc" width="6%">成本付款<br>申请</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${actualInList}" var="actualIn" varStatus="status">
            <tr class="actualInCost">
                <c:if test="${status.count==1}">
                    <td rowspan="${fn:length(actualInList)}">境内付款明细</td>
                </c:if>
                <td class="tc" name="tdName">${actualIn.name}</td>
                <td class="tc" name="tdSupply">
                    <c:choose>
                        <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && actualIn.supplyName=='非签约渠道'}">
                            直客
                        </c:when>
                        <c:otherwise>
                            ${actualIn.supplyName}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="tc" name="tdAccount">${actualIn.quantity}</td>
                <td class="tr" name="tdPrice">
                    <c:choose>
                        <c:when test="${actualIn.name eq '其他'}">-</c:when>
                        <c:otherwise>
                            <c:forEach items="${curlist}" var="currency">
                                <c:if test="${currency.id==actualIn.currencyId}">
                                    ${currency.currencyMark}
                                </c:if>
                            </c:forEach>
                            <fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualIn.price}" />
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="tc">${actualIn.rate}</td>
                <td class="tr" name="tdPriceSum">
                    <c:choose>
                        <c:when test="${actualIn.name eq '其他'}">-</c:when>
                        <c:otherwise>
                            <c:forEach items="${curlist}" var="currency">
                                <c:if test="${currency.id==actualIn.currencyId}">
                                    ${currency.currencyMark}
                                </c:if>
                            </c:forEach>
                            <fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualIn.price * actualIn.quantity}" />
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="tr">
                    <c:forEach items="${curlist}" var="currency">
                        <c:if test="${currency.id==actualIn.currencyAfter}">
                            ${currency.currencyMark}
                        </c:if>
                    </c:forEach>
                    <fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualIn.priceAfter}" />
                </td>
                <td name="tdComment" class="tc">${actualIn.comment}</td>
                <td class="tc">${fns:getUserById(actualIn.createBy).name}</td>
                <td class="tc" name="tdReview">
                    <c:if test="${actualIn.isNew eq 2 and not empty actualIn.reviewUuid}">
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
                    <c:if test="${actualIn.isNew eq 1 or empty actualIn.reviewUuid}">
                        <c:if test="${actualIn.reviewType==0}">
                            ${fns:getNextCostReview(actualIn.id)}
                        </c:if>
                        <c:if test="${actualIn.reviewType!=0}">
                            ${actualIn.reviewStatus}
                        </c:if>
                    </c:if>
                </td>
                <td>
                    <c:if test="${actualIn.reviewType==0 && ((empty actualIn.status && actualIn.review eq 4) ||actualIn.status==3 || actualIn.review eq 5)
                              && activityGroup.lockStatus==0 }">
                        <input type="checkbox" value="${actualIn.id}" id="" name="actualId" />
                    </c:if>
                </td>
                <td id="fujian" style="display:none;">${actualIn.costVoucher}</td>
                <td class="tc">
                    <c:if test="${activityGroup.lockStatus==0 && actualIn.reviewType==0}">
                        <c:if test="${(empty actualIn.status && actualIn.review eq 4) || actualIn.status eq 0 || actualIn.review eq 0
                              || actualIn.status eq 3 || actualIn.review eq 5}">
                            <a href="javascript:void(0)" onclick="callParentForUpdate('${actualIn.id}')">修改</a>&nbsp;&nbsp;
                            <a href="javascript:void(0)" onclick="callParentDeleteCost('${actualIn.id}','operator','','')">删除</a><br/>
                        </c:if>
                        <c:if test="${ (actualIn.status==2 or actualIn.review==2) && ( actualIn.payReview==4 || actualIn.payReview==0 || actualIn.payReview==5)}">
                            <a href="javascript:void(0)" onclick="callParentForUpdate('${actualIn.id}')">修改</a>&nbsp;&nbsp;
                            <a href="javascript:void(0)" onclick="callParentDeleteCost('${actualIn.id}','operator','','')">删除</a><br/>
                        </c:if>
                        <c:if test="${actualIn.status==1 and fns:isShowCancel(actualIn.reviewUuid)}">
                            <a href="javascript:void(0)" onclick="callParentCancelCost('${actualIn.reviewUuid}','operator')">取消成本申请</a><br/>
                        </c:if>
                        <!-- 实际成本审核通过，并且当前登录用户是付款申请发起人,并审批状态是审批中的数据 add by shijun.liu -->
                        <!-- 暂不处理旧数据的取消，因为旧数据没有记录发起审批人 -->
                        <c:if test="${actualIn.status==2 and fns:isShowCancel(actualIn.pay_review_uuid)}">
                            <a href="javascript:void(0)" onclick="callParentCancelPayCost('${actualIn.pay_review_uuid}')">取消付款申请</a><br/>
                        </c:if>
                    </c:if>
                    <c:if test="${activityGroup.lockStatus==1}">
                        已经锁定
                    </c:if>
                    <a onclick="callParentDownload('${ctx}',${actualIn.id },1,'${actualIn.costVoucher }')">下载附件</a>
                    <input type="hidden" id=" " value="${actualIn.id}" />
                </td>
                <td class="tc">
                    <c:if test="${actualIn.isNew eq 2 or not empty actualIn.pay_review_uuid }">
                        <c:choose>
                            <c:when test="${payApproveStatus eq 1 and actualIn.reviewType eq 0 and actualIn.payReview eq 2
                                  and actualIn.noPayReview eq 1 }">
                              无需审批
                            </c:when>
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
                    </c:if>
                    <c:if test="${actualIn.isNew eq 1 and empty actualIn.pay_review_uuid }">
                        <c:choose>
                            <c:when test="${actualIn.reviewType==0}">
                                ${fns:getNextPayReview(actualIn.id)}
                            </c:when>
                            <c:when test="${actualIn.reviewType==2}">
                                ${actualIn.reviewStatus}
                            </c:when>
                            <c:otherwise>
                                不需审核
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
                <td>
                    <c:if test="${(actualIn.status==2 or actualIn.review==2) && (actualIn.payReview==4 || actualIn.payReview==5) && actualIn.reviewType==0 }">
                        <input type="checkbox" value="${actualIn.id}" name="payId" />
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${! empty actualInList}">
            <tr>
                <td class="tc">境内小计</td>
                <td colspan="15">&nbsp;<span id="actualInShow"name="actualInShow"></span></td>
            </tr>
        </c:if>
        <c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
            <tr class="actualOutCost">
                <c:if test="${status.count==1}">
                    <td rowspan="${fn:length(actualOutList)}">境外付款明细</td>
                </c:if>
                <td class="tc" name="tdName">${actualOut.name}</td>
                <td class="tc" name="tdSupply">
                    <c:choose>
                        <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && actualOut.supplyName=='非签约渠道'}">
                            直客
                        </c:when>
                        <c:otherwise>
                            ${actualOut.supplyName}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="tc" name="tdAccount">${actualOut.quantity}</td>
                <td class="tr" name="tdPrice">
                    <c:choose>
                        <c:when test="${actualOut.name eq '其他'}">-</c:when>
                        <c:otherwise>
                            <c:forEach items="${curlist}" var="currency">
                                <c:if test="${currency.id==actualOut.currencyId}">
                                    ${currency.currencyMark}
                                </c:if>
                            </c:forEach>
                            <fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualOut.price}" />
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="tc">${actualOut.rate}</td>
                <td class="tr" name="tdPriceSum">
                    <c:choose>
                        <c:when test="${actualOut.name eq '其他'}">-</c:when>
                        <c:otherwise>
                            <c:forEach items="${curlist}" var="currency">
                                <c:if test="${currency.id==actualOut.currencyId}">
                                    ${currency.currencyMark}
                                </c:if>
                            </c:forEach>
                            <fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualOut.price * actualOut.quantity}" />
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="tr">
                    <c:forEach items="${curlist}" var="currency">
                        <c:if test="${currency.id==actualOut.currencyAfter}">
                            ${currency.currencyMark}
                        </c:if>
                    </c:forEach>
                    <fmt:formatNumber type="currency" pattern="#,##0.00" value="${actualOut.priceAfter}" />
                </td>
                <td name="tdComment" class="tc">${actualOut.comment}</td>
                <td class="tc">${fns:getUserById(actualOut.createBy).name}</td>
                <td class="tc" name="tdReview">
                    <c:if test="${actualOut.isNew eq 2 and not empty actualOut.reviewUuid }">
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
                    <c:if test="${actualOut.isNew eq 1 or empty actualOut.reviewUuid}">
                        <c:if test="${actualOut.reviewType==0}">
                            ${fns:getNextCostReview(actualOut.id)}
                        </c:if>
                        <c:if test="${actualOut.reviewType!=0}">
                            ${actualOut.reviewStatus}
                        </c:if>
                    </c:if>
                </td>
                <td>
                    <c:if test="${actualOut.reviewType==0 && ((empty actualOut.status && actualOut.review eq 4) ||actualOut.status==3 || actualOut.review eq 5)
                            && activityGroup.lockStatus==0 }">
                        <input type="checkbox" value="${actualOut.id}" id="" name="actualId" />
                    </c:if>
                </td>
                <td id="fujian" style="display:none;">${actualOut.costVoucher}</td>
                <td class="tc">
                    <c:if test="${activityGroup.lockStatus==0 && actualOut.reviewType==0}">
                        <c:if test="${(empty actualOut.status && actualOut.review eq 4) || actualOut.status eq 0 || actualOut.review eq 0
                                || actualOut.status==3 || actualOut.review eq 5}">
                            <a href="javascript:void(0)" onclick="callParentForUpdate('${actualOut.id}')">修改</a>&nbsp;&nbsp;
                            <a href="javascript:void(0)" onclick="callParentDeleteCost('${actualOut.id}','operator','','')">删除</a>
                        </c:if>
                        <c:if test="${(actualOut.status==2 or actualOut.review==2) && ( actualOut.payReview==4 || actualOut.payReview==0|| actualOut.payReview==5)}">
                            <a href="javascript:void(0)" onclick="callParentForUpdate('${actualOut.id}')">修改</a>&nbsp;&nbsp;
                            <a href="javascript:void(0)" onclick="callParentDeleteCost('${actualOut.id}','operator','','')">删除</a>
                        </c:if>
                        <c:if test="${actualOut.status==1 and fns:isShowCancel(actualOut.reviewUuid)}">
                            <a href="javascript:void(0)" onclick="callParentCancelCost('${actualOut.reviewUuid}','operator')">取消成本申请</a>
                        </c:if>
                        <!-- 实际成本审核通过，并且当前登录用户是付款申请发起人,并审批状态是审批中的数据 add by shijun.liu -->
                        <!-- 暂不处理旧数据的取消，因为旧数据没有记录发起审批人 -->
                        <c:if test="${actualOut.status==2 and fns:isShowCancel(actualOut.pay_review_uuid)}">
                            <a href="javascript:void(0)" onclick="callParentCancelPayCost('${actualOut.pay_review_uuid}')">取消付款申请</a>
                        </c:if>
                    </c:if>
                    <br />
                    <c:if test="${activityGroup.lockStatus==1}">
                        已经锁定
                    </c:if>
                    <a onclick="callParentDownload('${ctx}',${actualOut.id },1,'${actualOut.costVoucher }')">下载附件</a>
                    <input type="hidden" id="itemId" value="${actualOut.id}" />
                </td>
                <td class="tc">
                    <c:if test="${actualOut.isNew eq 2 or not empty actualOut.pay_review_uuid }">
                        <c:choose>
                            <c:when test="${payApproveStatus eq 1 and actualOut.reviewType eq 0 and actualOut.payReview eq 2 and actualOut.noPayReview eq 1}">无需审批</c:when>
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
                    </c:if>
                    <c:if test="${actualOut.isNew eq 1 and empty actualOut.pay_review_uuid }">
                        <c:choose>
                            <c:when test="${actualOut.reviewType==0}">
                                ${fns:getNextPayReview(actualOut.id)}
                            </c:when>
                            <c:when test="${actualOut.reviewType==2}">
                                ${actualOut.reviewStatus}
                            </c:when>
                            <c:otherwise>
                                不需审核
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
                <td>
                    <c:if test="${(actualOut.status==2 or actualOut.review==2) &&  (actualOut.payReview==4||actualOut.payReview==5 ) && actualOut.reviewType==0 }">
                        <input type="checkbox" value="${actualOut.id}" name="payId" />
                    </c:if>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${! empty actualOutList}">
            <tr>
                <td class="tc">境外小计</td>
                <td colspan="15">&nbsp;<span id="actualOutShow" name="actualOutShow"></span></td>
            </tr>
        </c:if>
    </tbody>
  </table>
	<div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
    	<ul class="cost-ul" data-total="cost">
      		<ul class="cost-ul" data-total="income">
        		<li>实际总收入：&nbsp;¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney') }"/></li>
      		</ul>
      		<li>实际总成本：&nbsp;¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(actualCost,'cost')}"/></li>
    	</ul>
    	<ul class="cost-ul" data-total="profit">
      		<ul class="cost-ul" data-total="income">
       			<li>退款合计：¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${actualrefund }"/></li>
      		</ul>
      		<li>实际总毛利：¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney')-fns:getSum(actualCost,'cost')-actualrefund }"/>
      		</li>
    	</ul>
    	<ul style="margin: 0px;float: right;">
      		<c:if test="${activityGroup.lockStatus==0 }">
               <input  class="btn-primary" type="button" value="提交实际成本"  onclick="saveCheckBox('actualId','1')" />
            </c:if>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
            <c:if test="${activityGroup.lockStatus==0}">
              <input class="btn-primary" type="button" value="提交成本付款" onclick="payCheckBox('payId','1')" />
            </c:if>
    	</ul>
  	</div>
</c:if>
<%@ include file="/WEB-INF/views/review/cost/service_charge_list.jsp" %>
</body>
